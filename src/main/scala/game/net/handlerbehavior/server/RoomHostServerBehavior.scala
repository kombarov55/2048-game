package game.net.handlerbehavior.server

import akka.actor.Actor
import game.net.RoomHostMessages.{CreateRoom, RoomCreated, TurnMade}
import game.net.ServerConnectionHandler.SendToTheOtherEnd
import game.net.handlerbehavior.IOBehavior
import game.net.{Room, ServerGlobals}

trait RoomHostServerBehavior extends Actor with IOBehavior {

  var myRoom: Room = _

  def roomHostBehavior: Receive = ioBehavior orElse {
    case CreateRoom =>
      myRoom = Room(remoteAddress, connection)
      ServerGlobals.rooms += myRoom
      sendToTheOtherEnd(RoomCreated)
      println(ServerGlobals.rooms)

    case TurnMade(cells, score) =>
      println("broadcasting " + cells, score)
      for (observer <- myRoom.observers) {
        observer ! SendToTheOtherEnd(TurnMade(cells, score))
      }
      println("broadcasted turn to " + myRoom.observers)
  }

}
