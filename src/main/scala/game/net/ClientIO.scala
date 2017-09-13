package game.net

import java.net.InetSocketAddress

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, ActorRef, Props}
import akka.io.Tcp.{Connect, Connected, Register}
import akka.io.{IO, Tcp}
import game.Globals
import game.net.ClientIO.{ConnectAs, Disconnect}
import game.net.handlers.ClientConnectionHandler
import game.net.handlers.ServerConnectionHandler.SetConnectionType
import game.net.model.ConnectionType

import scala.collection.mutable
import scala.util.Random

class ClientIO extends Actor {

  override def receive = readyToConnect

  var currentHandler: ActorRef = _

  def readyToConnect: Receive = {
    case ConnectAs(connectionType, remoteAddress) =>
      IO(Tcp)(Globals.system) ! Connect(remoteAddress)
      ClientIO.RemoteAddress_ConnectionType += remoteAddress -> connectionType
    case Connected(remote, local) =>
      val connectionType = ClientIO.RemoteAddress_ConnectionType.remove(remote).get
      currentHandler = context.actorOf(Props(new ClientConnectionHandler(sender(), remote, local)), name = "clientHandler" + Random.nextInt())
      sender() ! Register(currentHandler)
      currentHandler ! SetConnectionType(connectionType)
      context become forwardToHandler

    case other => println("client: received unknown message " + other)
  }

  def forwardToHandler: Receive = {
    case Disconnect =>
      currentHandler ! Stop
      context become readyToConnect

    case message => currentHandler forward message
  }
}

object ClientIO {

  var RemoteAddress_ConnectionType = mutable.Map.empty[InetSocketAddress, ConnectionType]

  case class ConnectAs(connectionType: ConnectionType, to: InetSocketAddress)

  object Disconnect

}
