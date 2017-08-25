package game.swing

import java.awt.Font
import javax.swing.border.TitledBorder
import javax.swing.{BorderFactory, DefaultListModel, JButton, JList, JScrollPane}

import game.Implicits.Function2ActionListener

class MultiplayerPanel extends Panel {

  val participantList = new DefaultListModel[String]
  val jlist = new JList[String](participantList)
  jlist.setBounds(125, 75, 250, 250)
  jlist.setFont(new Font("Arial", Font.PLAIN, 18))

  val border = BorderFactory.createTitledBorder("Выберите соперника")
  border.setTitleJustification(TitledBorder.CENTER)
  jlist.setBorder(border)

  participantList.addElement("local")
  participantList.addElement("local")
  participantList.addElement("local")
  participantList.addElement("local")

  val watchButton = new JButton("Смотреть")
  val versusButton = new JButton("Играть 1х1")

  watchButton.setBounds(100, 350, 300, 50)
  versusButton.setBounds(100, 400, 300, 50)

//  watchButton.addActionListener()

  override def addComponentsOnSelf(): Unit = {
    setLayout(null)

    add(watchButton)
    add(jlist)
    add(versusButton)
  }
}
