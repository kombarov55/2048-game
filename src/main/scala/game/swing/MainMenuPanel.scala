package game.swing

import java.awt.Dimension
import javax.swing.JButton

class MainMenuPanel() extends Panel {

  val playButton = new JButton("Одиночная игра")
  val multiplayerButton = new JButton("Играть по сети")
  val settingsButton = new JButton("Настройки")
  val highscoreButton = new JButton("Рекорды")
  val exitButton = new JButton("Выход")

  playButton.setBounds(100, 200, 300, 50)
  multiplayerButton.setBounds(100, 250, 300, 50)
  settingsButton.setBounds(100, 300, 300, 50)
  highscoreButton.setBounds(100, 350, 300, 50)
  exitButton.setBounds(100, 400, 300, 50)

  override def addComponentsOnSelf(): Unit = {
    setLayout(null)
    setPreferredSize(new Dimension(500, 600))

    add(playButton)
    add(multiplayerButton)
    add(settingsButton)
    add(highscoreButton)
    add(exitButton)
  }
}
