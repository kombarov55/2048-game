package game.net

import java.io.Serializable

import game.model.Cell

object RoomHostMessages {

  object CreateRoom extends Serializable

  object RoomCreated extends Serializable

  case class TurnMade(changes: Seq[Cell], score: Int) extends Serializable

}
