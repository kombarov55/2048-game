package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import game.Globals
import game.net.ConnectionType.Lobby
import game.net.LobbyMessages.AddPlayer
import game.net.ServerConnectionHandler.SetConnectionType
import game.net.handlerbehavior.{IOBehavior, LobbyClientBehavior, SocketHandler}

class ClientConnectionHandler(val connection: ActorRef,
                              val remoteAddress: InetSocketAddress,
                              val localAddress: InetSocketAddress)
  extends Actor with SocketHandler with IOBehavior with LobbyClientBehavior {


  override def receive: Receive = ioBehavior orElse {
    case SetConnectionType(connectionType) => connectionType match {
      case Lobby =>
        println("client becoming lobby")
        sendToTheOtherEnd(SetConnectionType(Lobby))
        Thread.sleep(100)
        sendToTheOtherEnd(AddPlayer(Globals.userName, localAddress))
        context.become(handleLobbyClient)
    }
  }

}
