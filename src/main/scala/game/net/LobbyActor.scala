package game.net

import java.net.{InetAddress, Socket}

import akka.actor.{Actor, ActorRef, Props}
import game.StaticData
import game.controllers.MultiplayerController
import game.net.Server.{AllPlayersRequest, AllPlayersResponse, ConnectionResponse, Handshake}
import game.net.SocketWrapper.Send

class LobbyActor(address: String, multiplayerController: MultiplayerController) extends Actor {

  var socketWrapper: ActorRef = _

  override def receive: Receive = {
    case ConnectionResponse(address) => onConnectionResponse(address)
    case AllPlayersResponse(players) => onPlayersReceived(players)
    case x: Any => println(x)
  }

  override def preStart(): Unit = {
    val socket = new Socket(address, 6666)
    socketWrapper = StaticData.system.actorOf(Props(new SocketWrapper(socket)))
    socketWrapper ! "start"
    socketWrapper ! Send(Handshake(StaticData.localAddress, StaticData.userName))
  }

  def onPlayersReceived(players: Seq[Player]): Unit = {
    println(players)
  }

  def onConnectionResponse(address: InetAddress): Unit = {
    StaticData.localAddress = address
    socketWrapper ! Send(AllPlayersRequest(StaticData.localAddress))
  }
}