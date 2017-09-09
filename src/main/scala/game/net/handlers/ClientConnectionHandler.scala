package game.net.handlers

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import game.net.handlers.ServerConnectionHandler.SetConnectionType
import game.net.handlers.handlerbehavior.client.{LobbyClientBehavior, ObserverClientBehavior, RoomHostClientBehavior}
import game.net.handlers.handlerbehavior.{IOBehavior, SocketHandler}
import game.net.model.ConnectionType.{ClientLobby, ObserverClient, ObserverServer, RoomHostClient, RoomHostServer, ServerLobby}
import game.net.model.Player

class ClientConnectionHandler(val connection: ActorRef,
                              val remoteAddress: InetSocketAddress,
                              val localAddress: InetSocketAddress)
  extends Actor with SocketHandler with IOBehavior with LobbyClientBehavior with RoomHostClientBehavior with ObserverClientBehavior {

  override var onPlayersReceived: (Seq[Player]) => Unit = _


  override def receive: Receive = ioBehavior orElse {
    case SetConnectionType(connectionType) => connectionType match {
      case ClientLobby(onPlayersReceived) =>
        sendToTheOtherEnd(SetConnectionType(ServerLobby))
        this.onPlayersReceived = onPlayersReceived
        Thread.sleep(100)
        context.become(handleLobbyClient)

      case ServerLobby =>
        sendToTheOtherEnd(SetConnectionType(ServerLobby))
        Thread.sleep(100)
        context.become(handleLobbyClient)

      case RoomHostClient =>
        sendToTheOtherEnd(SetConnectionType(RoomHostServer))
        Thread.sleep(100)
        context.become(roomHostClientBehavior)

      case ObserverClient =>
        sendToTheOtherEnd(SetConnectionType(ObserverServer))
        Thread.sleep(100)
        context.become(observerClientBehavior)


    }

  }

}
