package game.net

import akka.actor.ActorRef

case class Player(address: String, socketRef: ActorRef, var name: String = "")
