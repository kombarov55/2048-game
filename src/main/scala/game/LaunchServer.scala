package game

import akka.actor.Props
import game.net.Server

object LaunchServer extends App {

  val server = Globals.system.actorOf(Props[Server], "server")

}
