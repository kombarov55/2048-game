package game.net.handlerbehavior

import akka.actor.SupervisorStrategy.Stop
import game.net.RoomHostMessages.{CreateRoom, RoomCreated}

trait RoomHostClientBehavior extends SocketHandler with IOBehavior {

  def roomHostClientBehavior: Receive = ioBehavior orElse {
    case CreateRoom =>
      sendToTheOtherEnd(CreateRoom)

    case RoomCreated =>
      println("Room created")

    case Stop => context stop self
  }

}
