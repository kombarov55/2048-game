package game.controllers

import game.Globals
import game.Implicits.Function2ActionListener
import game.swing.GameoverPanel

class GameoverController extends Controller {

  val panel = new GameoverPanel

  override def bindPanelWithSelf(): Unit = {
    panel.label.setText(s"Игра окончена. Ваш счет: ${Globals.field.score}")
    panel.exitButton.addActionListener(openMainMenu _)
  }

  def openMainMenu(): Unit = {
    Globals.field = null
    (new MainMenuController).becomeActive()
  }
}
