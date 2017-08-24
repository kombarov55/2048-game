package game.swing

import java.awt.Dimension
import javax.swing.JButton

import game.Implicits.Function2ActionListener

class MainMenuPanel() extends Panel {

  val playButton = new JButton("Играть")
  val exitButton = new JButton("Выход")

  override def addComponentsOnSelf(): Unit = {
    setLayout(null)
    setPreferredSize(new Dimension(500, 600))

    playButton.setBounds(100, 200, 300, 50)
    exitButton.setBounds(100, 250, 300, 50)

    add(playButton)
    add(exitButton)
  }
}
