package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, Props}
import akka.io.Tcp.{Bind, Bound, Command, CommandFailed, Connected, Register}
import akka.io.{IO, Tcp}
import game.Globals
import game.net.Server.lobbyHandlers

class Server extends Actor {

  override def preStart(): Unit = {
    IO(Tcp)(Globals.system) ! Bind(self, new InetSocketAddress("localhost", 6666))
  }

  override def receive: Receive = {
    case Bound(localAddress) => println("Server bound at " + localAddress)
    case CommandFailed(cmd: Command) => println("bounding failed..")

    case Connected(remote, _) =>
      val connection = sender()
      val handler = context.actorOf(Props(new ServerConnectionHandler(remote, connection)))
      lobbyHandlers = handler :: lobbyHandlers
      connection ! Register(handler)
  }
}

object Server {

  var lobbyHandlers = List.empty[ActorRef]
  var players = List.empty[Player]

}
