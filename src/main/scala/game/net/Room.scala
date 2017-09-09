package game.net

import java.net.InetSocketAddress

import akka.actor.ActorRef

import scala.collection.mutable

case class Room(hostAddress: InetSocketAddress, playerActor: ActorRef, observers: mutable.Buffer[ActorRef] = mutable.Buffer.empty[ActorRef]) {

}
