package game

import java.net.InetAddress

import akka.actor.{ActorRef, ActorSystem}
import game.model.Field

object StaticData {

  var field: Field = null

  var userName: String = "Безымянный"

  var address: String = InetAddress.getLocalHost.getHostAddress
  var localAddress: InetAddress = _

  var myActor: ActorRef = _
  var system = ActorSystem("my-system")
}
