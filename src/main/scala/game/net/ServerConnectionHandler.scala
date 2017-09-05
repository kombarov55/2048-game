package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import game.net.ServerConnectionHandler.{Lobby, SetConnectionType}
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

  case class SetConnectionType(handlerType: HandlerType)

  trait HandlerType extends Serializable

  object Lobby extends HandlerType with Serializable
  object GameMonitoring extends HandlerType with Serializable
}
