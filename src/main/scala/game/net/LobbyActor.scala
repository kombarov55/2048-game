package game.net

import java.net.Socket

import akka.actor.{Actor, ActorRef, Props}
import game.StaticData
import game.controllers.MultiplayerController
import game.net.Server.{AllPlayersRequest, AllPlayersResponse, Handshake}
import game.net.SocketWrapper.Send

class LobbyActor(address: String, multiplayerController: MultiplayerController) extends Actor {

  var socketWrapper: ActorRef = _

  override def preStart(): Unit = {
    val socket = new Socket(address, 6666)
    StaticData.localSocketAddress = socket.getLocalSocketAddress
    socketWrapper = context.actorOf(Props(new SocketWrapper(socket)), "clientSocket")
    socketWrapper ! Send(Handshake(StaticData.localSocketAddress, StaticData.userName))
  }

  override def receive: Receive = {
    case AllPlayersRequest => socketWrapper ! Send(AllPlayersRequest(StaticData.localSocketAddress))
    case AllPlayersResponse(players) => onPlayersReceived(players)
    case x: Any => println(x)
  }

  def onPlayersReceived(players: Seq[Player]): Unit = {
    multiplayerController.onPlayersReceived(players)
  }

}