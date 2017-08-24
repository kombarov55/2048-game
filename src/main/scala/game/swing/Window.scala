package game.swing

import java.awt.Dimension
import javax.swing.{JFrame, JPanel}

object Window extends JFrame {

  setVisible(true)
  setPreferredSize(new Dimension(500, 600))
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  pack()

  var prevPanel: Panel = _

  def changePanelTo(panel: Panel): Unit = {
    if (prevPanel != null) remove(prevPanel)
    prevPanel = panel
    add(panel)
    pack()
  }
}
