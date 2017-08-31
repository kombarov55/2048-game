package game

import akka.actor.Props
import game.net.LobbyActor
import game.net.Server.AllPlayersRequest

object LaunchGame extends App {

//  MainMenuController.becomeActive()

  val lobbyActor = StaticData.system.actorOf(Props(new LobbyActor("localhost", null)))
  lobbyActor ! AllPlayersRequest

}
