package game

import java.awt.event.{ActionEvent, ActionListener, KeyAdapter, KeyEvent}

object Implicits {

  implicit def Function2KeyAdapter(fun: (KeyEvent) => Any): KeyAdapter = new KeyAdapter {
    override def keyPressed(e: KeyEvent): Unit = fun(e)
  }

  implicit def Function2ActionListener(fun: () => Any): ActionListener = new ActionListener {
    override def actionPerformed(e: ActionEvent) = fun()
  }

  implicit def Function2Runnable(fun: () => Any): Runnable = new Runnable {
    override def run() = fun()
  }

  implicit class RichOption[T](option: Option[T]) {

    def ifPresent(fun: (T) => Unit): RichOption[T] = {
      if (option.isDefined)
        fun(option.get)
      this
    }

    def otherwise(unit: Unit): Unit = {
      if (option.isEmpty) {
        unit
      }
    }

    def otherwise(fun: () => Unit): Unit = otherwise(fun())


  }

}