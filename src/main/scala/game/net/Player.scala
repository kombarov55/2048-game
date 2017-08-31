package game.net

import java.net.SocketAddress

case class Player(address: SocketAddress, var name: String = "") extends Serializable
