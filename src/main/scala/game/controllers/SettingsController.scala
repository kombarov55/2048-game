package game.controllers
import java.net.InetSocketAddress

import game.Implicits.Function2ActionListener
import game.StaticData
import game.swing.SettingsPanel

class SettingsController extends Controller {

  override val panel = new SettingsPanel

  override def bindPanelWithSelf(): Unit = {
    panel.nameInput.setText(StaticData.userName)
    panel.addressInput.setText(StaticData.serverAddress.getHostName)

    panel.save.addActionListener { () =>
      StaticData.userName = panel.nameInput.getText()
      StaticData.serverAddress = new InetSocketAddress(panel.addressInput.getText(), 6666)
      // Должен показываться лейбл *Изменения сохранены*
      MainMenuController.becomeActive
    }
    panel.back.addActionListener(MainMenuController.becomeActive _)
  }
}
