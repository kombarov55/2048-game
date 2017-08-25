package game.swing

import javax.swing.JButton

import game.Implicits.Function2ActionListener

class MultiplayerPanel extends Panel {

  val watchButton = new JButton("Смотреть")
  val versusButton = new JButton("Играть 1х1")

  watchButton.setBounds(100, 250, 300, 50)
  versusButton.setBounds(100, 300, 300, 50)

//  watchButton.addActionListener()

  override def addComponentsOnSelf(): Unit = {
    setLayout(null)

    add(watchButton)
    add(versusButton)
  }
}
