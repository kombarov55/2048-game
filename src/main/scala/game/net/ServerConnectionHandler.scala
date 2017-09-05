package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import game.net.ServerConnectionHandler.ConnectionType.Lobby
import game.net.ServerConnectionHandler.SetConnectionType
import game.net.handlerbehavior.{IOBehavior, LobbyBehavior}

class ServerConnectionHandler(val remoteAddress: InetSocketAddress, var connection: ActorRef)
  extends Actor with IOBehavior with LobbyBehavior {

  override def receive: Receive = ioBehavior orElse {
    case SetConnectionType(handlerType) => handlerType match {
      case Lobby => context.become(ioBehavior orElse lobbyBehavior)

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

  }

}
