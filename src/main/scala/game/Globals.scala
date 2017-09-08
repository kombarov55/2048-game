package game

import java.net.InetSocketAddress

import akka.actor.{ActorSystem, Props}
import game.model.Field
import game.net.Client

object Globals {

  var field: Field = null

  var userName = "Безымянный"
  var serverAddress = new InetSocketAddress("localhost", 6666)

  var localAddress: InetSocketAddress = _

  var system = ActorSystem("my-system")

  val client = system.actorOf(Props[Client], "client")
}
