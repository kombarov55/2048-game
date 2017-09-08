package game.net

trait ConnectionType

object ConnectionType {

  object Lobby extends ConnectionType with Serializable

  object GameMonitoring extends ConnectionType with Serializable

  object RoomHost extends ConnectionType with Serializable

}
