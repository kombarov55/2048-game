package game.swing

import javax.swing.JPanel

trait Panel extends JPanel {

  def addComponentsOnSelf(): Unit

}
