package game

import java.net.{InetAddress, SocketAddress}

import akka.actor.ActorSystem
import game.model.Field

object StaticData {

  var field: Field = null

  var userName: String = "Безымянный"

  var address: String = InetAddress.getLocalHost.getHostAddress
  var localAddress: InetAddress = _

  var localSocketAddress: SocketAddress = _

  var system = ActorSystem("my-system")
}
