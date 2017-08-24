package game.swing

import java.awt.event.{KeyAdapter, KeyEvent}

class KeyboardListener(handleKey: (KeyEvent) => Any) extends KeyAdapter {

  private def notifyChanges(e: KeyEvent): Unit = {
    if (isArrowKey(e))
      handleKey(e)
  }

  override def keyReleased(e: KeyEvent): Unit = notifyChanges(e)

  private val allowedKeys = List(KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN)

  private def isArrowKey(e: KeyEvent) = allowedKeys contains e.getKeyCode
}
