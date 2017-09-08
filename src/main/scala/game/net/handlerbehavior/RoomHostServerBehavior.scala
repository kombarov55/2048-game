package game.net.handlerbehavior

import akka.actor.Actor
import game.net.RoomHostMessages.{CreateRoom, RoomCreated, TurnMade}
import game.net.{Room, ServerGlobals}

trait RoomHostServerBehavior extends Actor with IOBehavior {

  var myRoom: Room = _

  def roomHostBehavior: Receive = ioBehavior orElse {
    case CreateRoom =>
      myRoom = Room(connection)
      ServerGlobals.rooms = myRoom :: ServerGlobals.rooms
      sendToTheOtherEnd(RoomCreated)

    case TurnMade(cells, score) =>
      println("broadcasting turn")
      println("changed cells: " + cells)
      println("new score: " + score)
  }

}
