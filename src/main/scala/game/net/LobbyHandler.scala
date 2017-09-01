package game.net

import akka.actor.Actor
import akka.io.Tcp.{Received, Write}
import akka.util.ByteString
import game.net.LobbyHandler.SendToTheOtherEnd
import game.net.LobbyProtocol.{AddPlayer, AllPlayers}

class LobbyHandler() extends Actor {

  override def receive: Receive = {
    case Received(data) =>
      val deserializedData = Serializer.deserialize(data.toArray)
      println("handler: deserialized=" + deserializedData)
      self forward deserializedData

    case AddPlayer(name, address) =>
      Server.players = Player(name, address) :: Server.players
    case AllPlayers(players) if players == null =>
      println("handler: received message about players. now sending")
      self forward SendToTheOtherEnd(Server.players)

    case str: String => self forward SendToTheOtherEnd("echo: " + str)

    case SendToTheOtherEnd(anything) =>
      println("handler: Sending to the other end: " + anything)
      val bytes = Serializer.serialize(anything)
      sender() ! Write(ByteString(bytes))

    case unknown: Any => println("handler received unknown message: " + unknown)

  }
}

object LobbyHandler {

  private case class SendToTheOtherEnd(anything: Any) extends Serializable

}
