package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import akka.io.Tcp.{ErrorClosed, PeerClosed, Received, Write}
import akka.util.ByteString
import game.net.LobbyHandler.SendToTheOtherEnd
import game.net.LobbyProtocol.{AddPlayer, AllPlayers}
import game.net.Server.{lobbyHandlers, players}

class LobbyHandler(remote: InetSocketAddress, connection: ActorRef) extends Actor {

  override def receive: Receive = {
    case Received(data) =>
      val deserializedData = Serializer.deserialize(data.toArray)
      println("handler: deserialized=" + deserializedData)
      self ! deserializedData

    case PeerClosed =>
      players = players.filterNot { _.address == remote }
      lobbyHandlers = lobbyHandlers.filterNot { actorRef => actorRef == connection }
      broadcast(AllPlayers(players))

    case ErrorClosed(_) =>
      players = players.filterNot { _.address == remote }
      lobbyHandlers = lobbyHandlers.filterNot { actorRef => actorRef == connection }
      broadcast(AllPlayers(players))

    case AddPlayer(name, address) =>
      val newPlayer = Player(name, address)
      players = newPlayer :: players
      broadcast(AllPlayers(players))

    case AllPlayers =>
      self ! SendToTheOtherEnd(players)

    case SendToTheOtherEnd(anything) =>
      val bytes = Serializer.serialize(anything)
      connection ! Write(ByteString(bytes))

    case unknown: Any => println("handler received unknown message: " + unknown)

  }

  def  broadcast(message: AnyRef): Unit = {
    for (handler <- lobbyHandlers) {
      handler ! SendToTheOtherEnd(message)
    }
  }

}

object LobbyHandler {

  private case class SendToTheOtherEnd(anything: Any) extends Serializable

}
