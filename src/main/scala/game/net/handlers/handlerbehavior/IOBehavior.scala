package game.net.handlers.handlerbehavior

import akka.actor.Actor
import akka.io.Tcp.{Received, Write}
import akka.util.ByteString
import game.net.Serializer
import game.net.handlers.ServerConnectionHandler.SendToTheOtherEnd

trait IOBehavior extends Actor with SocketHandler {

  def ioBehavior: Receive = {
    case Received(byteString) => deserializeAndReceiveAgain(byteString)

    case SendToTheOtherEnd(message) => sendToTheOtherEnd(message)
  }

  def deserializeAndReceiveAgain(byteString: ByteString): Unit = {
    val deserializedData = Serializer.deserialize(byteString.toArray)
    self ! deserializedData
  }

  def sendToTheOtherEnd(message: Any): Unit = {
    val bytes = Serializer.serialize(message)
    connection ! Write(ByteString(bytes))
  }
}
