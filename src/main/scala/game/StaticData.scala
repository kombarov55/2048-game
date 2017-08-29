package game

import java.net.InetAddress

import game.model.Field

object StaticData {

  var field: Field = null

  var userName: String = "Безымянный"

  var address: String = InetAddress.getLocalHost.getHostAddress

}
