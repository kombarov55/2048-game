package game.model

case class MovementType(direction: String)

object MovementType {

  object moved extends MovementType("moved")

  object summed extends MovementType("summed")

  object disappeared extends MovementType("disappeared")

  object created extends MovementType("created")

}
