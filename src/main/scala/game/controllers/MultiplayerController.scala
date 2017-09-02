package game.controllers
import akka.actor.ActorRef
import game.StaticData
import game.net.{LobbyClient, Player}
import game.swing.MultiplayerPanel

class MultiplayerController extends Controller {

  override val panel = new MultiplayerPanel

  var lobbyClient: ActorRef = _

  override def bindPanelWithSelf(): Unit = {
    val lobbyClientProps = LobbyClient.props(StaticData.serverAddress, displayPlayersOnPanel)
    lobbyClient = StaticData.system.actorOf(lobbyClientProps)
  }

  def displayPlayersOnPanel(players: Seq[Player]): Unit = {
    panel.participantList.clear()
    for (player <- players) {
      panel.participantList.addElement(player.name)
    }
  }

}