package game.controllers

import game.Implicits.Function2ActionListener
import game.swing.MainMenuPanel

class MainMenuController extends Controller {

  val panel = new MainMenuPanel

  def onPlayClicked(): Unit = {
    (new GameController).becomeActive()
  }

  def onExitClicked(): Unit = {
    println("exit not implemented yet..")
  }

  override def initializeModel(): Unit = {
    panel.playButton.addActionListener(onPlayClicked _)
    panel.exitButton.addActionListener(onExitClicked _)
  }
}
