package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, Props}
import akka.io.Tcp.{Bind, Bound, CommandFailed, Connected, Register}
import akka.io.{IO, Tcp}
import game.Globals
import game.net.handlers.ServerConnectionHandler

import scala.util.Random

class Server extends Actor {

  override def preStart(): Unit = {
    IO(Tcp)(Globals.system) ! Bind(self, new InetSocketAddress("localhost", 6666))
  }

  override def receive: Receive = {
    case Bound(localAddress) => println("Server bound at " + localAddress)
    case CommandFailed(_) => println("bounding failed..")

    case Connected(remote, local) =>
      val connection = sender()
      val handler = context.actorOf(Props(new ServerConnectionHandler(remote, local, connection)), name = "serverHandler" + Random.nextInt())
      ServerGlobals.lobbyHandlers = handler :: ServerGlobals.lobbyHandlers
      connection ! Register(handler)
  }
}
