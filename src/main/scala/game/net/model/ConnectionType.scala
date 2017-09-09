package game.net.model

trait ConnectionType

object ConnectionType {

  object ServerLobby extends ConnectionType with Serializable

  case class ClientLobby(onPlayersReceived: (Seq[Player]) => Unit) extends ConnectionType with Serializable

  object RoomHostServer extends ConnectionType with Serializable

  object RoomHostClient extends ConnectionType with Serializable

  object ObserverClient extends ConnectionType with Serializable

  object ObserverServer extends ConnectionType with Serializable

}
