package game.model

import java.awt.event.KeyEvent

case class Command(direction: Direction, getLeftOrientatedRows: Field => Array[Array[Cell]]) {
  def opposite: Command = {
    val thisIndex = Command.all indexOf this
    Command.all((thisIndex + 2) / 4)
  }
}

object Command {

  val left = new Command(Direction.left, field => field.rows)

  val up = new Command(Direction.up, (field: Field) => Array.tabulate(4) { field column _ })

  val right = new Command(Direction.right, (field: Field) => field.rows.map(_.reverse))

  val down = new Command(Direction.down, (field: Field) => Array.tabulate(4) { field column _ reverse })

  private val all = List(left, up, right, down)

  def getByKeyEvent(e: KeyEvent): Command = {
    e.getKeyCode match {
      case KeyEvent.VK_LEFT => left
      case KeyEvent.VK_UP => up
      case KeyEvent.VK_RIGHT => right
      case KeyEvent.VK_DOWN => down
      case _ => null
    }
  }
}
