package game.model

case class Direction(direction: String)

object Direction {

  object left extends Direction("left")

  object up extends Direction("up")

  object right extends Direction("right")

  object down extends Direction("down")

}
