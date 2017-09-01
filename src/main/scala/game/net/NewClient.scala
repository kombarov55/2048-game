package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import akka.io.Tcp.{Connect, Connected, Received, Register, Write}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import game.StaticData

class NewClient extends Actor {

  var connection: ActorRef = _

  override def preStart(): Unit = {
    IO(Tcp)(StaticData.system) ! Connect(new InetSocketAddress("localhost", 6666))

  }

  override def receive: Receive = {
    case c @ Connected(remote, local) =>
      println("client: connected to " + remote)
      println("client: my address: " + local)
      connection = sender()
      connection ! Register(self)

    case data: ByteString => connection ! Write(data)
    case Received(data) => {
      val serializer = StaticData.serialization.findSerializerFor(data)
      val obj = serializer.fromBinary(data.toArray)
      println("client: received message: " + obj)
    }

  }
}
