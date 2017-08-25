package game.net.messages

import game.model.Cell

case class ProducedDelta(score: Int, changes: Seq[Cell])