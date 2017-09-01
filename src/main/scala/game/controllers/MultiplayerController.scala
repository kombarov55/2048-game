package game.controllers
import akka.actor.Props
import game.StaticData
import game.net.Server.AllPlayersRequest
import game.net.{LobbyActor, Player}
import game.swing.MultiplayerPanel

class MultiplayerController extends Controller {

  var lobbyClient = StaticData.system.actorOf(Props(new LobbyActor(StaticData.address, this)), "lobbyClient")

  override val panel = new MultiplayerPanel

  override def bindPanelWithSelf(): Unit = {
    lobbyClient ! AllPlayersRequest

  }

  def onPlayersReceived(players: Seq[Player]): Unit = {
    for (player <- players) {
      panel.participantList.addElement(player.name)
    }
  }
}