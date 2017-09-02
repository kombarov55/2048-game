package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import akka.io.Tcp.{PeerClosed, Received, Write}
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
      broadcast(AllPlayers(players))

    case AddPlayer(name, address) =>
      val newPlayer = Player(name, address)
      players = newPlayer :: players
      println(s"handler: $newPlayer added. players now: $players")
      broadcast(AllPlayers(players))
      println("handler: all players broadcasted")

    case AllPlayers =>
      println("handler: received message about players. now sending")
      self ! SendToTheOtherEnd(players)

    case SendToTheOtherEnd(anything) =>
      println("handler: Sending to the other end: " + anything)
      val bytes = Serializer.serialize(anything)
      connection ! Write(ByteString(bytes))
      println("handler: message broadcasted")

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
