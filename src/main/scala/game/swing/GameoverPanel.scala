package game.swing

import java.awt.Dimension
import javax.swing.{JButton, JLabel}

import game.Implicits.Function2ActionListener

class GameoverPanel extends Panel {

  val label = new JLabel()
  label.setBounds(100, 100, 300, 50)

  val exitButton = new JButton("Выход")
  exitButton.setBounds(100, 200, 300, 50)

  override def addComponentsOnSelf(): Unit = {
    setPreferredSize(new Dimension(500, 600))
    setLayout(null)
    add(label)
    add(exitButton)
  }
}
