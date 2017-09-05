package game

import akka.actor.Props
import game.net.ServerGlobals

object LaunchServer extends App {

  val server = Globals.system.actorOf(Props[ServerGlobals], "server")

}
