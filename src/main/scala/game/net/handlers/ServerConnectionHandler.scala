package game.net.handlers

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import game.net.handlers.ServerConnectionHandler.SetConnectionType
import game.net.handlers.handlerbehavior.server.{LobbyServerBehavior, ObserverServerBehavior, RoomHostServerBehavior}
import game.net.handlers.handlerbehavior.{IOBehavior, SocketHandler}
import game.net.model.ConnectionType
import game.net.model.ConnectionType.{ObserverServer, RoomHostServer, ServerLobby}

class ServerConnectionHandler(val remoteAddress: InetSocketAddress, val localAddress: InetSocketAddress, val connection: ActorRef)
  extends Actor with SocketHandler with IOBehavior with LobbyServerBehavior with RoomHostServerBehavior with ObserverServerBehavior {

  override def receive: Receive = ioBehavior orElse {
    case SetConnectionType(handlerType) => handlerType match {
      case ServerLobby =>
        println("becoming lobby handler")
        context.become(lobbyBehavior)
      case RoomHostServer =>
        println("becoming room host handler")
        context.become(roomHostBehavior)

      case ObserverServer =>
        println("handler becoming observer")
        context.become(observerServerBehavior)

      case other: Any => println(s"default handler: unknown type - $other")
    }

    case other: Any => println(s"default handler: unknown message - $other")
  }

}

object ServerConnectionHandler {

  case class SendToTheOtherEnd(anything: Any) extends Serializable

  case class SetConnectionType(handlerType: ConnectionType)

}
