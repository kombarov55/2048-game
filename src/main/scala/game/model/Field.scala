package game.model

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.Random

class Field(val rows: Array[Array[Cell]] =
                 Array.tabulate(4)((y: Int) => {
                   Array.tabulate(4)((x: Int) => Cell(x, y, 0))
                 })) {


  var score = 0

  def makeTurn(command: Command): Unit = {
    currentDelta = ListBuffer[CellDelta]()
    foldAndUpdateDelta(command.getLeftOrientatedRows(this))
    setAlreadySummedToFalse()
    recalculateScore()
    if (anyMovementInLastTurn) spawnNewCell()
  }

  def createCellsOnStart(): Unit = {
    0 to 4 foreach { i => spawnNewCell() }
  }

  // Лучше превратить в кеш фиксированного размера
  private var currentDelta: mutable.Buffer[CellDelta] = ListBuffer[CellDelta]()

  def emptyCells(): Seq[Cell] = rows.flatten.filter { _.score == 0 }

  // сложение ряда - в отдельный метод!
  def foldAndUpdateDelta(leftOrientiredRows: Array[Array[Cell]]): Unit = {

    for (row <- leftOrientiredRows) {
      for (i <- 1 to 3) {
        for (j <- i to 1 by -1) {
          val c1 = row(j - 1)
          val c2 = row(j)
          val delta = deltaOfFolding(c1, c2)
          addOrUpdateExistingElements(delta)
          if (delta.nonEmpty) {
            c1.applyDelta(delta(0))
            c2.applyDelta(delta(1))
          }
        }
      }
    }
  }

  def recalculateScore(): Unit = {
    val onlySummed = currentDelta.filter { it => it.movementType == MovementType.summed }
    score += onlySummed.foldLeft(0) { _ + _.cell.score / 2 }
  }

  def setAlreadySummedToFalse(): Unit = rows.flatten.foreach { _.alreadySummed = false }

  // Норм
  def getRandomEmptyCell: Cell = {
    val cells = emptyCells()

    cells(Random.nextInt(cells.size))
  }

  def spawnNewCell(): Unit = {
    val newCell = getRandomEmptyCell
     newCell.score = (Random.nextInt(2) + 1) * 2
    currentDelta += CellDelta(newCell, MovementType.created)
  }


  def anyMovementInLastTurn: Boolean = currentDelta.nonEmpty

  def isGameOver: Boolean = emptyCells().isEmpty

  private def deltaOfFolding(c1: Cell, c2: Cell): Seq[CellDelta] = {
    (c1, c2) match {
      case (Cell(_, _, 0, _), Cell(_, _, score, _)) if score != 0 =>
        List(
              CellDelta(c1.copy(score = c2.score), MovementType.moved),
              CellDelta(c2.copy(score = 0), MovementType.disappeared))
      case (Cell(_, _, score1, false), Cell(_, _, score2, false)) if score1 == score2 && score1!= 0 && score2 != 0 =>
        List(
              CellDelta(c1.copy(score = c1.score * 2, alreadySummed = true), MovementType.summed),
              CellDelta(c2.copy(score = 0), MovementType.disappeared))
      case _ => List.empty[CellDelta]
    }
  }

  // Adhoc, это вообще надо удалять. Не факт что нужна была, и не факт что исправляет то, что должна была
  private def addOrUpdateExistingElements(xs: Seq[CellDelta]): Unit = {
    xs foreach { delta =>
      currentDelta.find { alreadyStoredDelta => alreadyStoredDelta.cell equalCoords delta.cell } match {
        case Some(x) => x.cell.score = delta.cell.score
        case None => currentDelta += delta
      }
    }
  }

  def row(index: Int): Array[Cell] = rows(index)

  def column(index: Int): Array[Cell] = {
    (for (i <- 0 to 3)
      yield rows(i)(index))
    .toArray
  }

  override def toString: String = {
    var ret = ""
    for (row <- rows)
      ret += row.map(_.score).mkString(", ") + "\n"
    ret
  }
}
