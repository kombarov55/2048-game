package game.controllers

import game.Implicits.Function2ActionListener
import game.swing.{MainMenuPanel, Window}

class MainMenuController extends Controller {

  val panel = new MainMenuPanel

  override def bindPanelWithSelf(): Unit = {
    panel.playButton.addActionListener((new GameController).becomeActive _)
    panel.multiplayerButton.addActionListener((new MultiplayerController).becomeActive _)
    panel.settingsButton.addActionListener((new SettingsController).becomeActive _)
    panel.exitButton.addActionListener { () =>
      Window.dispose()
      System.exit(0)
    }
  }
}

object MainMenuController {
  def becomeActive(): Unit = {
    GameController.becomeActive()
  }
}
