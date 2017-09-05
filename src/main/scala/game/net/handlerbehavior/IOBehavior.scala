package game.net.handlerbehavior

import akka.actor.{Actor, ActorRef}
import akka.io.Tcp.{Received, Write}
import akka.util.ByteString
import game.net.Serializer
import game.net.ServerConnectionHandler.SendToTheOtherEnd

trait IOBehavior extends Actor {

  val connection: ActorRef

  def ioBehavior: Receive = {
    case Received(byteString) =>
      val deserializedData = Serializer.deserialize(byteString.toArray)
      self ! deserializedData

    case SendToTheOtherEnd(message) => sendToTheOtherEnd(message)
  }

  def sendToTheOtherEnd(message: Any): Unit = {
    val bytes = Serializer.serialize(message)
    connection ! Write(ByteString(bytes))
  }
}
