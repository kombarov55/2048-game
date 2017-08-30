package game.net

import java.net.InetAddress

import akka.actor.ActorRef

case class Player(address: InetAddress, socketRef: ActorRef, var name: String = "") extends Serializable
