package game

import akka.actor.Props
import game.net.LobbyActor
import game.net.LobbyActor.ConnectTo

object LaunchGame extends App {

//  MainMenuController.becomeActive()

  val lobbyActor = StaticData.system.actorOf(Props(new LobbyActor(null)))
  lobbyActor ! ConnectTo("localhost")

}
