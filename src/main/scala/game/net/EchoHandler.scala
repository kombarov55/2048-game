package game.net

import akka.actor.Actor
import akka.io.Tcp.{Received, Write}

class EchoHandler extends Actor {
  override def receive: Receive = {
    case Received(data) =>
      println("server handler: reveiced message: " + data.toString())
      sender() ! Write(data)
  }
}
