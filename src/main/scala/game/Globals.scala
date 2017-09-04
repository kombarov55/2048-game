package game

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import akka.serialization.SerializationExtension
import game.model.Field

object Globals {

  var field: Field = null

  var userName = "Безымянный"
  var serverAddress = new InetSocketAddress("localhost", 6666)

  var localAddress: InetSocketAddress = _

  var system = ActorSystem("my-system")

  val serialization = SerializationExtension(Globals.system)
}
