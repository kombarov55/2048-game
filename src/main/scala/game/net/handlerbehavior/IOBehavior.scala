package game.net.handlerbehavior

import akka.actor.{Actor, ActorRef}
import akka.io.Tcp.{Received, Write}
import akka.util.ByteString
import game.net.Serializer
import game.net.ServerConnectionHandler.SendToTheOtherEnd

trait IOBehavior extends Actor {

  // var потому что connection у клиента получаем после первого запроса, и инициализировать его не получается
  var connection: ActorRef

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
