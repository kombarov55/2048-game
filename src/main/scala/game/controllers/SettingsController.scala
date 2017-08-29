package game.controllers
import game.StaticData
import game.swing.SettingsPanel

import game.Implicits.Function2ActionListener

class SettingsController extends Controller {

  override val panel = new SettingsPanel

  override def bindPanelWithSelf(): Unit = {
    panel.nameInput.setText(StaticData.userName)
    panel.addressInput.setText(StaticData.address)

    panel.save.addActionListener { () =>
      StaticData.userName = panel.nameInput.getText()
      StaticData.address = panel.addressInput.getText()
      MainMenuController.becomeActive
    }
    panel.back.addActionListener(MainMenuController.becomeActive _)
  }
}
