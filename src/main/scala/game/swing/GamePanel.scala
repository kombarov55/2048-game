package game.swing

import java.awt.{BorderLayout, Dimension, FlowLayout, Font, GridLayout}
import javax.swing.{JButton, JLabel, JPanel}
import game.Implicits.Function2ActionListener

class GamePanel extends Panel {

  val buttons: Seq[JButton] = Seq.fill(16)({
    val button = new JButton()
    button.setOpaque(true)
    button.setEnabled(false)
    button.setFont(new Font("Arial", Font.BOLD, 32))
    button
  })

  val grid: JPanel = new JPanel(new GridLayout(4, 4))
  for (button <- buttons)
    grid.add(button)

  val scoreLabel = new JLabel("0")
  scoreLabel.setFont(new Font("Arial", Font.ITALIC, 32))

  val pauseButton = new JButton("Пауза")

  val infoPanel = new JPanel(new FlowLayout)
  infoPanel.setPreferredSize(new Dimension(500, 150))
  infoPanel.add(scoreLabel)
  infoPanel.add(pauseButton)

  override def addComponentsOnSelf(): Unit = {
    setLayout(new BorderLayout)
    add(infoPanel, BorderLayout.NORTH)
    add(grid, BorderLayout.CENTER)
    setPreferredSize(new Dimension(500, 600))
    setFocusable(true)
  }
}
