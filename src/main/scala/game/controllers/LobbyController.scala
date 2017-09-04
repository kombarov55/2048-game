package game.controllers
import javax.swing.event.ListSelectionEvent

import akka.actor.ActorRef
import game.Globals
import game.Implicits.{Function2ActionListener, Function2ListSelectionListener}
import game.net.LobbyClient.Stop
import game.net.{LobbyClient, Player}
import game.swing.LobbyPanel

class LobbyController extends Controller {

  override val panel = new LobbyPanel

  var lobbyClient: ActorRef = _

  override def bindPanelWithSelf(): Unit = {
    panel.watchButton.setEnabled(false)
    panel.versusButton.setEnabled(false)

    panel.backButton.addActionListener { () =>
      lobbyClient ! Stop
      MainMenuController.becomeActive()
    }
    panel.jlist.addListSelectionListener { e: ListSelectionEvent =>
      val selectedPlayer = panel.playerList.get(e.getFirstIndex)
      println(s"selected $selectedPlayer")
      panel.watchButton.setEnabled(true)
      panel.versusButton.setEnabled(true)

    }
  }

  override def initializeModel(): Unit = {
    val lobbyClientProps = LobbyClient.props(Globals.serverAddress, displayPlayersOnPanel)
    lobbyClient = Globals.system.actorOf(lobbyClientProps)
  }

  def displayPlayersOnPanel(players: Seq[Player]): Unit = {
    panel.playerList.clear()
    for (player <- players if player.address != Globals.localAddress) {
      panel.playerList.addElement(player)
    }
  }
}