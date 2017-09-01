package game.net

import java.net.InetSocketAddress

case class Player(address: InetSocketAddress, var name: String = "") extends Serializable
