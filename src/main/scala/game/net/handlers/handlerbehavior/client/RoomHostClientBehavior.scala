package game.net.handlers.handlerbehavior.client

import akka.actor.SupervisorStrategy.Stop
import game.net.handlers.handlerbehavior.{IOBehavior, SocketHandler}
import game.net.model.RoomHostMessages.{CreateRoom, RoomCreated, TurnMade}

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
