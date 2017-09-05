package game.net.handlerbehavior

import akka.actor.Actor
import game.net.RoomHostMessages.{CreateRoom, RoomCreated}

trait RoomHostBehavior extends Actor with IOBehavior {

  def roomHostBehavior: Receive = {
    case CreateRoom =>
      println("create room received")
      sendToTheOtherEnd(RoomCreated)
  }

}
