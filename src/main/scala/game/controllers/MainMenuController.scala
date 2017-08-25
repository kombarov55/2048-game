package game.controllers

import game.Implicits.Function2ActionListener
import game.swing.MainMenuPanel

class MainMenuController extends Controller {

  val panel = new MainMenuPanel

  override def initializeModel(): Unit = {
    panel.playButton.addActionListener((new GameController).becomeActive _)
    panel.multiplayerButton.addActionListener((new MultiplayerController).becomeActive _)
    panel.exitButton.addActionListener(() => println("exit not implemented yet.."))
  }
}

object MainMenuController {
  def becomeActive(): Unit = {
    GameController.becomeActive()
  }
}
