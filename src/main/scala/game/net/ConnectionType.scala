package game.net

trait ConnectionType

object ConnectionType {

  object ServerLobby extends ConnectionType with Serializable

  case class ClientLobby(onPlayersReceived: (Seq[Player]) => Unit) extends ConnectionType with Serializable

  object GameMonitoring extends ConnectionType with Serializable

  object RoomHost extends ConnectionType with Serializable

}
