package game.net

import java.net.InetSocketAddress

import akka.actor.ActorRef

case class Room(playerAddress: InetSocketAddress, var observers: Seq[ActorRef] = List.empty[ActorRef]) {

}
