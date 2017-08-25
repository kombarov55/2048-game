package game.net.messages

import game.model.Cell

case class DeltaToConsume(score: Int, changes: Seq[Cell])
