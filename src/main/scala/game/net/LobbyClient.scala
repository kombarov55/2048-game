package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, Props}
import akka.io.Tcp.{Connect, Connected, Received, Register, Write}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import game.Globals
import game.net.LobbyClient.Stop
import game.net.LobbyProtocol.{AddPlayer, AllPlayers}
import game.net.ServerConnectionHandler.{Lobby, SetConnectionType}

class LobbyClient(serverAddress: InetSocketAddress, onPlayersReceived: (Seq[Player]) => Unit) extends Actor {

  var connection: ActorRef = _

  override def preStart(): Unit = {
    IO(Tcp)(Globals.system) ! Connect(serverAddress)
  }

  override def receive: Receive = {
    case Connected(_, localAddress) =>
      connection = sender()
      connection ! Register(self)
      Globals.localAddress = localAddress
      sendToTheOtherEnd(SetConnectionType(Lobby))
      //TODO: Сообщение пропадает, если посылать их подряд. Нужно разобраться как получить то сообщение
      Thread.sleep(100)
      sendToTheOtherEnd(AddPlayer(Globals.userName, localAddress))


    case Received(data) =>
      val deserializedMessage = Serializer.deserialize(data.toArray)
      self ! deserializedMessage

    case ap @ AllPlayers => sendToTheOtherEnd(ap)

    case AllPlayers(players) => onPlayersReceived(players)

    case Stop => context stop self

    case other: Any => println("client: received unknown message: " + other)
  }

  def sendToTheOtherEnd(message: Any): Unit = {
    val serializedData = Serializer.serialize(message)
    connection ! Write(ByteString(serializedData))
  }

}

object LobbyClient {

  def props(inetSocketAddress: InetSocketAddress, onPlayersReceived: (Seq[Player]) => Unit): Props = Props(new LobbyClient(inetSocketAddress, onPlayersReceived))

  object Stop

}
