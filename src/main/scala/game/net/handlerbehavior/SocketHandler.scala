package game.net.handlerbehavior

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}

trait SocketHandler extends Actor {

  val connection: ActorRef
  val remoteAddress: InetSocketAddress
  val localAddress: InetSocketAddress

}
