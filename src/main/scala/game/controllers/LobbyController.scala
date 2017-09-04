package game.controllers
import akka.actor.ActorRef
import game.Implicits.Function2ActionListener
import game.StaticData
import game.net.LobbyClient.Stop
import game.net.{LobbyClient, Player}
import game.swing.LobbyPanel

class LobbyController extends Controller {

  override val panel = new LobbyPanel

  var lobbyClient: ActorRef = _

  override def bindPanelWithSelf(): Unit = {
    panel.backButton.addActionListener { () =>
      lobbyClient ! Stop
      MainMenuController.becomeActive()
    }
  }

  override def initializeModel(): Unit = {
    val lobbyClientProps = LobbyClient.props(StaticData.serverAddress, displayPlayersOnPanel)
    lobbyClient = StaticData.system.actorOf(lobbyClientProps)
  }

  def displayPlayersOnPanel(players: Seq[Player]): Unit = {
    panel.playerList.clear()
    for (player <- players) {
      panel.playerList.addElement(player)
    }
  }
}