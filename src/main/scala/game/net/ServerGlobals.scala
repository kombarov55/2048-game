package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, Props}
import akka.io.Tcp.{Bind, Bound, CommandFailed, Connected, Register}
import akka.io.{IO, Tcp}
import game.Globals
import game.net.ServerGlobals.lobbyHandlers

class ServerGlobals extends Actor {

  override def preStart(): Unit = {
    IO(Tcp)(Globals.system) ! Bind(self, new InetSocketAddress("localhost", 6666))
  }

  override def receive: Receive = {
    case Bound(localAddress) => println("Server bound at " + localAddress)
    case CommandFailed(_) => println("bounding failed..")

    case Connected(remote, local) =>
      val connection = sender()
      val handler = context.actorOf(Props(new ServerConnectionHandler(remote, local, connection)), name = "serverHandler")
      lobbyHandlers = handler :: lobbyHandlers
      connection ! Register(handler)
  }
}

object ServerGlobals {

  var lobbyHandlers = List.empty[ActorRef]
  var players = List.empty[Player]

  var rooms = List.empty[Room]

}
