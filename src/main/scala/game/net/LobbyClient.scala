package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, Props}
import game.net.LobbyClient.Stop
import game.net.LobbyProtocol.AllPlayers
import game.net.ServerConnectionHandler.ConnectionType
import game.net.ServerConnectionHandler.ConnectionType.Lobby
import game.net.handlerbehavior.{ClientBehavior, IOBehavior}

class LobbyClient(val serverAddress: InetSocketAddress, onPlayersReceived: (Seq[Player]) => Unit)
  extends Actor with IOBehavior with ClientBehavior {

  var connection: ActorRef = _

  val connectionType: ConnectionType = Lobby

  override def receive: Receive = ioBehavior orElse clientBehavior orElse {
    case ap @ AllPlayers => sendToTheOtherEnd(ap)

    case AllPlayers(players) => onPlayersReceived(players)

    case Stop => context stop self

    case other: Any => println("client: received unknown message: " + other)
  }
}

object LobbyClient {

  def props(inetSocketAddress: InetSocketAddress, onPlayersReceived: (Seq[Player]) => Unit): Props = Props(new LobbyClient(inetSocketAddress, onPlayersReceived))

  object Stop

}
