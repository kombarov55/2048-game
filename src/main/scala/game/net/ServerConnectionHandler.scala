package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import game.net.ConnectionType.{Lobby, RoomHost}
import game.net.ServerConnectionHandler.SetConnectionType
import game.net.handlerbehavior.{IOBehavior, LobbyServerBehavior, RoomHostServerBehavior, SocketHandler}

class ServerConnectionHandler(val remoteAddress: InetSocketAddress, val localAddress: InetSocketAddress, val connection: ActorRef)
  extends Actor with SocketHandler with IOBehavior with LobbyServerBehavior with RoomHostServerBehavior {

  override def receive: Receive = ioBehavior orElse {
    case SetConnectionType(handlerType) => handlerType match {
      case Lobby =>
        println("becoming lobby handler")
        context.become(lobbyBehavior)
      case RoomHost =>
        println("becoming room host handler")
        context.become(roomHostBehavior)

      case other: Any => println(s"default handler: unknown type - $other")
    }

    case other: Any => println(s"default handler: unknown message - $other")
  }

}

object ServerConnectionHandler {

  case class SendToTheOtherEnd(anything: Any) extends Serializable

  case class SetConnectionType(handlerType: ConnectionType)

}
