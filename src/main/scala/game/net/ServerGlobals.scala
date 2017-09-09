package game.net

import akka.actor.ActorRef
import game.net.model.{Player, Room}

import scala.collection.mutable

object ServerGlobals {

  var lobbyHandlers = List.empty[ActorRef]
  var players = List.empty[Player]

  var rooms = mutable.Buffer.empty[Room]

}
