package game

import java.net.InetAddress

import akka.actor.{ActorSystem, Props}
import game.net.Server
import game.net.Server.Start

object LaunchServer extends App {

  val system = ActorSystem("my-system")
  val server = system.actorOf(Props(new Server(6666)), "server")
  server ! Start

  val address = InetAddress.getLocalHost.getHostAddress
  println(s"Сервер запущен. Адрес: $address:6666")
}
