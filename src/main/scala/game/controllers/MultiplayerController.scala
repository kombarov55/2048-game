package game.controllers
import game.swing.{MultiplayerPanel, Panel}

class MultiplayerController extends Controller {

  override val panel: Panel = new MultiplayerPanel

  override def initializeModel(): Unit = {
//    StaticData.myActor = StaticData.system.actorOf()
  }
}