package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import game.net.ServerConnectionHandler.ConnectionType
import game.net.handlerbehavior.{ClientBehavior, IOBehavior}

class RoomHostClient(val serverAddress: InetSocketAddress) extends Actor with IOBehavior with ClientBehavior {

  var connection: ActorRef = Actor.noSender

  val connectionType: ConnectionType = ConnectionType.GameMonitoring


  override def preStart(): Unit = {
    super.preStart()

  }

  override def receive: Receive = {



    case other => println("room host client: received unknown message - " + other)
  }
}
