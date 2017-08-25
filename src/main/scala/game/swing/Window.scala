package game.swing

import java.awt.Dimension
import javax.swing.{JFrame, JPanel}

object Window extends JFrame {

  var prevPanel: Panel = _

  setVisible(true)
  setPreferredSize(new Dimension(500, 600))
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  pack()

}
