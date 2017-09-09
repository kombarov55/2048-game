package game.net.model

import java.net.InetSocketAddress

case class Player(name: String = "", address: InetSocketAddress) extends Serializable
