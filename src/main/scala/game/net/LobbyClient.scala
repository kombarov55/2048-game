package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, Props}
import akka.io.Tcp.{Connect, Connected, Received, Register, Write}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import game.StaticData
import game.net.LobbyClient.SendToTheOtherEnd
import game.net.LobbyProtocol.{AddPlayer, AllPlayers}

/**
  * В конструктор должна передаться лямба, что сделать после настройки. Параметр в ней - self.
  */
class LobbyClient(serverAddress: InetSocketAddress) extends Actor {

  var connection: ActorRef = _

  override def preStart(): Unit = {
    IO(Tcp)(StaticData.system) ! Connect(serverAddress)
  }

  override def receive: Receive = {
    case Connected(_, localAddress) =>
      StaticData.localSocketAddress = localAddress
      connection = sender()
      connection ! Register(self)
      self ! SendToTheOtherEnd(AddPlayer("Николай", localAddress))


    case Received(data) =>
      println("client received message, now deserializing..")
      val deserializedMessage = Serializer.deserialize(data.toArray)
      self ! deserializedMessage

    case request @ AllPlayers => self forward SendToTheOtherEnd(request)

    case AllPlayers(players) if players != null => onPlayerListReceived(players)

    case SendToTheOtherEnd(data) =>
      val serializedData = Serializer.serialize(data)
      println("client: serialized " + data)
      connection ! Write(ByteString(serializedData))
      println(s"client: sent $data to the other end.")

    case other: Any => println("client: received unknown message: " + other)
  }

  def onPlayerListReceived(players: Seq[Player]): Unit = {
    for (player <- players) {
      println(player)
    }
  }
}

object LobbyClient {
  def props(ip: String, port: Int): Props = Props(new LobbyClient(new InetSocketAddress(ip, port)))

  private case class SendToTheOtherEnd(data: Any)

}
