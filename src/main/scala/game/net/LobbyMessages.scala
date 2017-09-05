package game.net

import java.net.InetSocketAddress

object LobbyMessages {

  case class AddPlayer(name: String, address: InetSocketAddress)

  case class RemovePlayer(address: InetSocketAddress)

  case class AllPlayers(players: Seq[Player] = null)

}
