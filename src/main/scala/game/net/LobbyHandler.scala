package game.net

import java.net.InetSocketAddress

import akka.actor.Actor
import akka.io.Tcp.{PeerClosed, Received, Write}
import akka.util.ByteString
import game.net.LobbyHandler.SendToTheOtherEnd
import game.net.LobbyProtocol.{AddPlayer, AllPlayers}
import game.net.Server.{lobbyHandlers, players}

class LobbyHandler(remote: InetSocketAddress) extends Actor {

  override def receive: Receive = {
    case Received(data) =>
      val deserializedData = Serializer.deserialize(data.toArray)
      println("handler: deserialized=" + deserializedData)
      self forward deserializedData

    case PeerClosed =>
      players = players.filterNot { _.address == remote }
      broadcast(AllPlayers(players))


    case AddPlayer(name, address) =>
      players = Player(name, address) :: players
      broadcast(AllPlayers(players))

    case AllPlayers =>
      println("handler: received message about players. now sending")
      self forward SendToTheOtherEnd(players)

    case str: String => self forward SendToTheOtherEnd("echo: " + str)

    case SendToTheOtherEnd(anything) =>
      println("handler: Sending to the other end: " + anything)
      val bytes = Serializer.serialize(anything)
      sender() ! Write(ByteString(bytes))

    case unknown: Any => println("handler received unknown message: " + unknown)

  }

  def broadcast(message: AnyRef): Unit = {
    for (handler <- lobbyHandlers) {
      handler ! message
    }
  }
}

object LobbyHandler {

  private case class SendToTheOtherEnd(anything: Any) extends Serializable

}
