package game

import java.awt.event.{ActionEvent, ActionListener, KeyAdapter, KeyEvent}

object Implicits {

  implicit def Function2KeyAdapter(fun: (KeyEvent) => Any): KeyAdapter = new KeyAdapter {
    override def keyPressed(e: KeyEvent): Unit = fun(e)
  }

  implicit def Function2ActionListener(fun: () => Any): ActionListener = new ActionListener {
    override def actionPerformed(e: ActionEvent) = fun()
  }

}