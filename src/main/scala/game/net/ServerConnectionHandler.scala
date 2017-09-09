package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import game.net.ConnectionType.{ObserverServer, RoomHostServer, ServerLobby}
import game.net.ServerConnectionHandler.SetConnectionType
import game.net.handlerbehavior.{IOBehavior, LobbyServerBehavior, ObserverServerBehavior, RoomHostServerBehavior, SocketHandler}

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
