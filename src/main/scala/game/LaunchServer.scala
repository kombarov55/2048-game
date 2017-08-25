package game

import java.net.InetAddress

import akka.actor.{ActorSystem, Props}
import game.net.Server

object LaunchServer extends App {

  val system = ActorSystem("my-system")
  val server = system.actorOf(Props(new Server(6666)))
  server ! "start"

  val address = InetAddress.getLocalHost.getHostAddress
  println(s"Сервер запущен. Адрес: $address:6666")
}
