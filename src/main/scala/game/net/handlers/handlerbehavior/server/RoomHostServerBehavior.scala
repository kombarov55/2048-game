package game.net.handlers.handlerbehavior.server

import akka.actor.Actor
import game.net.ServerGlobals
import game.net.handlers.ServerConnectionHandler.SendToTheOtherEnd
import game.net.handlers.handlerbehavior.IOBehavior
import game.net.model.Room
import game.net.model.RoomHostMessages.{CreateRoom, RoomCreated, TurnMade}

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
