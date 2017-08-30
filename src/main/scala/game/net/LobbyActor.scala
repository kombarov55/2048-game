package game.net

import java.net.Socket

import akka.actor.{Actor, ActorRef, Props}
import game.StaticData
import game.controllers.MultiplayerController
import game.net.LobbyActor.ConnectTo

class LobbyActor(multiplayerController: MultiplayerController) extends Actor {

  var socketWrapper: ActorRef = _

  override def receive: Receive = {
    case ConnectTo(address) => connect(address)
  }

  def connect(address: String): Unit = {
    val socket = new Socket(address, 6666)
    socketWrapper = StaticData.system.actorOf(Props(new SocketWrapperOnClient(socket)))
  }

}

object LobbyActor {
  case class ConnectTo(address: String)
}

