package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, Props}
import akka.io.Tcp.{Connect, Connected, Register}
import akka.io.{IO, Tcp}
import game.Globals
import game.net.Client.ConnectAs
import game.net.ServerConnectionHandler.SetConnectionType

class Client extends Actor {

  override def receive: Receive = {
    case ConnectAs(connectionType, remoteAddress) =>
      IO(Tcp)(Globals.system) ! Connect(remoteAddress)
      Client.remoteaddressToconnectionType += remoteAddress -> connectionType
    case Connected(remote, local) =>
      val connectionType = Client.remoteaddressToconnectionType(remote)
      val clientHandler = context.actorOf(Props(new ClientConnectionHandler(sender(), remote, local)), name = "clientHandler")
      sender() ! Register(clientHandler)
      clientHandler ! SetConnectionType(connectionType)
  }
}

object Client {

  var remoteaddressToconnectionType = Map.empty[InetSocketAddress, ConnectionType]

  case class ConnectAs(connectionType: ConnectionType, remoteAddress: InetSocketAddress)

}
