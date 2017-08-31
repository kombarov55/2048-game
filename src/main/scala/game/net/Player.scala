package game.net

import java.net.SocketAddress

import akka.actor.ActorRef

case class Player(address: SocketAddress, socketRef: ActorRef, var name: String = "") extends Serializable
