package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import akka.io.Tcp.{Connect, Connected, Register}
import akka.io.{IO, Tcp}
import game.Globals
import game.net.ServerConnectionHandler.{Lobby, SetConnectionType}
import game.net.handlerbehavior.IOBehavior

class RoomHostClient(serverAddress: InetSocketAddress) extends Actor with IOBehavior {

  var connection: ActorRef = Actor.noSender

  override def preStart(): Unit = {
    IO(Tcp)(Globals.system) ! Connect(serverAddress)
  }

  override def receive: Receive = {
    case Connected(_, localAddress) =>
      connection = sender()
      connection ! Register(self)
      // Нужно определять локальный адрес по другому!
      Globals.localAddress = localAddress
      sendToTheOtherEnd(SetConnectionType(Lobby))

    case other => println("room host client: received unknown message - " + other)
  }
}
