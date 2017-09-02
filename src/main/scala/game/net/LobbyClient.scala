package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, Props}
import akka.io.Tcp.{Connect, Connected, Received, Register, Write}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import game.StaticData
import game.net.LobbyClient.SendToTheOtherEnd
import game.net.LobbyProtocol.{AddPlayer, AllPlayers}

class LobbyClient(serverAddress: InetSocketAddress, onPlayersReceived: (Seq[Player]) => Unit) extends Actor {

  var connection: ActorRef = _

  override def preStart(): Unit = {
    IO(Tcp)(StaticData.system) ! Connect(serverAddress)
  }

  override def receive: Receive = {
    case Connected(_, localAddress) =>
      connection = sender()
      connection ! Register(self)
      self ! SendToTheOtherEnd(AddPlayer("Николай", localAddress))


    case Received(data) =>
      println("client received message, now deserializing..")
      val deserializedMessage = Serializer.deserialize(data.toArray)
      self ! deserializedMessage

    case request @ AllPlayers => self forward SendToTheOtherEnd(request)

    case AllPlayers(players) if players != null => onPlayersReceived(players)

    case SendToTheOtherEnd(data) =>
      val serializedData = Serializer.serialize(data)
      println("client: serialized " + data)
      connection ! Write(ByteString(serializedData))
      println(s"client: sent $data to the other end.")

    case other: Any => println("client: received unknown message: " + other)
  }

}

object LobbyClient {

  def props(inetSocketAddress: InetSocketAddress, onPlayersReceived: (Seq[Player]) => Unit): Props = Props(new LobbyClient(inetSocketAddress, onPlayersReceived))

  private case class SendToTheOtherEnd(data: Any)

}
