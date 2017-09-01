package game.net

import java.net.InetSocketAddress

import akka.actor.Actor
import akka.io.Tcp.{Received, Write}
import akka.util.ByteString
import game.net.LobbyHandler.SendToTheOtherEnd

class LobbyHandler() extends Actor {

  override def receive: Receive = {
    case Received(data) =>
      val deserializedData = Serializer.deserialize(data.toArray)
      println("handler: deserialized=" + deserializedData)
      self forward deserializedData

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

  case class AddPlayer(name: String, address: InetSocketAddress)

  case class RemovePlayer(address: InetSocketAddress)

  case class AllPlayers(players: Seq[Player] = null)
}
