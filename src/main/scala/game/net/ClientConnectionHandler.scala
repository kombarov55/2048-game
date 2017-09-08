package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import game.net.ConnectionType.{ClientLobby, RoomHost, ServerLobby}
import game.net.ServerConnectionHandler.SetConnectionType
import game.net.handlerbehavior.{IOBehavior, LobbyClientBehavior, RoomHostClientBehavior, SocketHandler}

class ClientConnectionHandler(val connection: ActorRef,
                              val remoteAddress: InetSocketAddress,
                              val localAddress: InetSocketAddress)
  extends Actor with SocketHandler with IOBehavior with LobbyClientBehavior with RoomHostClientBehavior {

  override var onPlayersReceived: (Seq[Player]) => Unit = _

  override def receive: Receive = ioBehavior orElse {
    case SetConnectionType(connectionType) => connectionType match {
      case ClientLobby(onPlayersReceived) =>
        println("client becoming lobby")
        sendToTheOtherEnd(SetConnectionType(ServerLobby))
        this.onPlayersReceived = onPlayersReceived
        Thread.sleep(100)
        context.become(handleLobbyClient)

      case ServerLobby =>
        println("client becoming lobby")
        sendToTheOtherEnd(SetConnectionType(ServerLobby))
        Thread.sleep(100)
        context.become(handleLobbyClient)
      case RoomHost =>
        println("client becoming room host")
        sendToTheOtherEnd(SetConnectionType(RoomHost))
        Thread.sleep(100)
        context.become(roomHostClientBehavior)
    }

  }

}
