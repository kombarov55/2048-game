package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import game.model.Cell
import game.net.ConnectionType.{ClientLobby, ObserverClient, ObserverServer, RoomHostClient, RoomHostServer, ServerLobby}
import game.net.ServerConnectionHandler.SetConnectionType
import game.net.handlerbehavior.client.{LobbyClientBehavior, ObserverClientBehavior, RoomHostClientBehavior}
import game.net.handlerbehavior.{IOBehavior, SocketHandler}

class ClientConnectionHandler(val connection: ActorRef,
                              val remoteAddress: InetSocketAddress,
                              val localAddress: InetSocketAddress)
  extends Actor with SocketHandler with IOBehavior with LobbyClientBehavior with RoomHostClientBehavior with ObserverClientBehavior {

  override var onPlayersReceived: (Seq[Player]) => Unit = _


  def onTurnMadee(cells: Seq[Cell], score: Int) = {
    println("changes: " + cells)
    println("score: " + score)
  }

  override var onTurnMade: (Seq[Cell], Int) => Unit = onTurnMadee

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
