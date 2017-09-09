package game.controllers

import javax.swing.event.ListSelectionEvent

import akka.actor.ActorRef
import game.Globals
import game.Implicits.{Function2ActionListener, Function2ListSelectionListener}
import game.net.ClientIO.ConnectAs
import game.net.model.ConnectionType.ObserverClient
import game.net.model.GameObserverMessages.ListAllRoomsRequest
import game.net.model.Player
import game.swing.LobbyPanel

class LobbyController extends Controller {

  override val panel = new LobbyPanel

  var lobbyClient: ActorRef = _

  override def bindPanelWithSelf(): Unit = {
    panel.watchButton.setEnabled(false)
    panel.versusButton.setEnabled(false)

    panel.backButton.addActionListener { () =>
      //      lobbyClient ! Stop
      MainMenuController.becomeActive()
    }
    panel.jlist.addListSelectionListener { e: ListSelectionEvent =>
      val selectedPlayer = panel.playerList.get(e.getFirstIndex)
      println(s"selected $selectedPlayer")
      panel.watchButton.setEnabled(true)
      panel.versusButton.setEnabled(true)
    }

    panel.watchButton.addActionListener { () =>
      val selectedAddress = panel.jlist.getSelectedValue.address
      val observerView = new GameObserverController(selectedAddress)
      observerView.becomeActive()
    }
  }

  override def initializeModel(): Unit = {
    Globals.clientIO ! ConnectAs(ObserverClient, to = Globals.serverAddress)
    Thread.sleep(100)
    Globals.clientIO ! ListAllRoomsRequest(callback = { rooms =>
      for (address <- rooms)
        panel.playerList.addElement(Player("", address))
    })
  }

  def displayPlayersOnPanel(players: Seq[Player]): Unit = {
    panel.playerList.clear()
    for (player <- players if player.address != Globals.localAddress) {
      panel.playerList.addElement(player)
    }
  }
}