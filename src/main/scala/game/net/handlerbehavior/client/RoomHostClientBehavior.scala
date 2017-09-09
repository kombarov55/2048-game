package game.net.handlerbehavior.client

import akka.actor.SupervisorStrategy.Stop
import game.net.RoomHostMessages.{CreateRoom, RoomCreated, TurnMade}
import game.net.handlerbehavior.{IOBehavior, SocketHandler}

trait RoomHostClientBehavior extends SocketHandler with IOBehavior {

  def roomHostClientBehavior: Receive = ioBehavior orElse {
    case CreateRoom =>
      sendToTheOtherEnd(CreateRoom)

    case RoomCreated =>
      println("Room created")

    case msg @ TurnMade(cells, score) =>
      sendToTheOtherEnd(msg)

    case Stop => context stop self
  }

}
