package game.net

import java.net.InetSocketAddress

case class Player(name: String = "", address: InetSocketAddress) extends Serializable {
  override def toString: String = name
}
