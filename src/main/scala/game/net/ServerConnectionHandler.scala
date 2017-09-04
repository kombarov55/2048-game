package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import akka.io.Tcp.{ErrorClosed, PeerClosed, Received, Write}
import akka.util.ByteString
import game.net.LobbyProtocol.{AddPlayer, AllPlayers}
import game.net.Server.{lobbyHandlers, players}
import game.net.ServerConnectionHandler.{Lobby, SendToTheOtherEnd, SetConnectionType}

class ServerConnectionHandler(remote: InetSocketAddress, connection: ActorRef) extends Actor {

  override def receive: Receive = {
    case Received(data) => deserializeAndReceiveAgain(data)

    case SetConnectionType(handlerType) => handlerType match {
      case Lobby =>
        println("default handler: now becoming lobby handler")
        context.become(handleLobby)
        println("now it is lobby handler")
      case other: Any => println(s"default handler: unknown type - $other")
    }
    case other: Any => println(s"default handler: unknown message - $other")
  }

  def handleLobby: Receive = {
    case Received(byteString) => deserializeAndReceiveAgain(byteString)

    case PeerClosed => stop()

    case ErrorClosed(_) => stop()

    case AddPlayer(name, address) =>
      val newPlayer = Player(name, address)
      players = newPlayer :: players
      broadcast(AllPlayers(players))

    case AllPlayers => sendToTheOtherEnd(players)

    case SendToTheOtherEnd(message) => sendToTheOtherEnd(message)

    case unknown: Any => println("lobby handler received unknown message: " + unknown)
  }

  def deserializeAndReceiveAgain(byteString: ByteString): Unit = {
    val deserializedData = Serializer.deserialize(byteString.toArray)
    println("handler: deserialized = " + deserializedData)
    self ! deserializedData
  }

  def sendToTheOtherEnd(message: Any): Unit = {
    val bytes = Serializer.serialize(message)
    connection ! Write(ByteString(bytes))
  }

  def broadcast(message: AnyRef): Unit = {
    for (handler <- lobbyHandlers) {
      handler ! SendToTheOtherEnd(message)
    }
  }

  def stop(): Unit = {
    println("stopping handler")
    players = players.filterNot { _.address == remote }
    lobbyHandlers = lobbyHandlers.filterNot { actorRef => actorRef == connection }
    broadcast(AllPlayers(players))
    context stop self
  }

}

object ServerConnectionHandler {

  private case class SendToTheOtherEnd(anything: Any) extends Serializable

  case class SetConnectionType(handlerType: HandlerType)

  trait HandlerType extends Serializable

  object Lobby extends HandlerType with Serializable
  object GameMonitoring extends HandlerType with Serializable
}
