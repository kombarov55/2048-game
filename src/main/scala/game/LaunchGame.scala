package game

import game.Globals.clientIO
import game.net.ClientIO.ConnectAs
import game.net.ConnectionType.ClientLobby
import game.net.LobbyMessages.AddPlayer

import scala.io.StdIn

object LaunchGame extends App {

//    MainMenuController.becomeActive()

  clientIO ! ConnectAs(ClientLobby(players => {
    println(players)
  }), to = Globals.serverAddress)
  StdIn.readLine()
  clientIO ! AddPlayer

}
