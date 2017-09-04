package game.controllers
import java.net.InetSocketAddress

import game.Globals
import game.Implicits.Function2ActionListener
import game.swing.SettingsPanel

class SettingsController extends Controller {

  override val panel = new SettingsPanel

  override def bindPanelWithSelf(): Unit = {
    panel.nameInput.setText(Globals.userName)
    panel.addressInput.setText(Globals.serverAddress.getHostName)

    panel.save.addActionListener { () =>
      Globals.userName = panel.nameInput.getText()
      Globals.serverAddress = new InetSocketAddress(panel.addressInput.getText(), 6666)
      // Должен показываться лейбл *Изменения сохранены*
      MainMenuController.becomeActive
    }
    panel.back.addActionListener(MainMenuController.becomeActive _)
  }
}
