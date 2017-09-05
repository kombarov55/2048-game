package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import game.net.ServerConnectionHandler.ConnectionType.{Lobby, RoomHost}
import game.net.ServerConnectionHandler.SetConnectionType
import game.net.handlerbehavior.{IOBehavior, LobbyBehavior, RoomHostBehavior}

class ServerConnectionHandler(val remoteAddress: InetSocketAddress, var connection: ActorRef)
  extends Actor with IOBehavior with LobbyBehavior with RoomHostBehavior {

  override def receive: Receive = ioBehavior orElse {
    case SetConnectionType(handlerType) => handlerType match {
      case Lobby =>
        println("becoming lobby handler")
        context.become(ioBehavior orElse lobbyBehavior)
      case RoomHost =>
        println("becoming room host handler")
        context.become(ioBehavior orElse roomHostBehavior)

      case other: Any => println(s"default handler: unknown type - $other")
    }

    case other: Any => println(s"default handler: unknown message - $other")
  }

}

object ServerConnectionHandler {

  case class SendToTheOtherEnd(anything: Any) extends Serializable

  case class SetConnectionType(handlerType: ConnectionType)

  trait ConnectionType extends Serializable

  object ConnectionType {

    object Lobby extends ConnectionType with Serializable

    object GameMonitoring extends ConnectionType with Serializable

    object RoomHost extends ConnectionType with Serializable

  }

}
