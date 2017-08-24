package game.controllers

import game.Implicits.Function2ActionListener
import game.StaticData
import game.swing.GameoverPanel

class GameoverController extends Controller {

  val panel = new GameoverPanel

  override def bindPanelWithSelf(): Unit = {
    panel.label.setText(s"Игра окончена. Ваш счет: ${StaticData.field.score}")
    panel.exitButton.addActionListener(openMainMenu _)
  }

  def openMainMenu(): Unit = {
    StaticData.field = null
    (new MainMenuController).becomeActive()
  }
}
