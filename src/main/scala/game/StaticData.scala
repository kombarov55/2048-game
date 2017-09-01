package game

import java.net.{InetAddress, InetSocketAddress}

import akka.actor.ActorSystem
import akka.serialization.SerializationExtension
import game.model.Field

object StaticData {

  var field: Field = null

  var userName: String = "Безымянный"

  var address: String = InetAddress.getLocalHost.getHostAddress
  var localAddress: InetAddress = _

  var localSocketAddress: InetSocketAddress = _

  var system = ActorSystem("my-system")

  val serialization = SerializationExtension(StaticData.system)
}
