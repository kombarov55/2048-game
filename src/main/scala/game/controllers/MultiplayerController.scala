package game.controllers
import game.swing.MultiplayerPanel

class MultiplayerController extends Controller {

  override val panel = new MultiplayerPanel

  override def bindPanelWithSelf(): Unit = {
  }

}