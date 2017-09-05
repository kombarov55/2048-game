package game.net

import akka.actor.ActorRef

case class Room(playerActor: ActorRef, var observers: Seq[ActorRef] = List.empty[ActorRef]) {

}
