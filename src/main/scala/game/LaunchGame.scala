package game

import game.net.Client.ConnectAs
import game.net.ConnectionType.Lobby

object LaunchGame extends App {

  //  MainMenuController.becomeActive()

  Globals.client ! ConnectAs(Lobby, Globals.serverAddress)

}
