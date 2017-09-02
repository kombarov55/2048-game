package game.swing

import java.awt.Font
import javax.swing.border.TitledBorder
import javax.swing.{BorderFactory, DefaultListModel, JButton, JList}

import game.net.Player

//  Это можно переписать по border-layout
class MultiplayerPanel extends Panel {

  val playerList = new DefaultListModel[Player]
  val jlist = new JList[Player](playerList)
  jlist.setBounds(125, 75, 250, 250)
  jlist.setFont(new Font("Arial", Font.PLAIN, 18))

  val border = BorderFactory.createTitledBorder("Выберите соперника")
  border.setTitleJustification(TitledBorder.CENTER)
  jlist.setBorder(border)

  val watchButton = new JButton("Смотреть")
  val versusButton = new JButton("Играть 1х1")

  watchButton.setBounds(100, 350, 300, 50)
  versusButton.setBounds(100, 400, 300, 50)
  
  override def addComponentsOnSelf(): Unit = {
    setLayout(null)

    add(watchButton)
    add(jlist)
    add(versusButton)
  }
}
