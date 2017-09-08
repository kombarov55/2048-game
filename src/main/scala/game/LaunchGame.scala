package game

import game.Globals.clientIO
import game.net.ClientIO.ConnectAs
import game.net.ConnectionType.RoomHost
import game.net.RoomHostMessages.CreateRoom

import scala.io.StdIn

object LaunchGame extends App {

//    MainMenuController.becomeActive()

  clientIO ! ConnectAs(RoomHost, to = Globals.serverAddress)
  StdIn.readLine()
  clientIO ! CreateRoom

}
