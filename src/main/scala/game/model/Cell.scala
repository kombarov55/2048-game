package game.model

case class Cell(x: Int, y: Int, var score: Int, var alreadySummed: Boolean = false) {

  def equalCoords(that: Cell): Boolean = this.x == that.x && this.y == that.y

  def applyDelta(delta: CellDelta): Unit = {
    score = delta.cell.score
    alreadySummed = delta.cell.alreadySummed
  }

}
