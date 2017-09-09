package game

import java.net.InetSocketAddress

import game.Globals.clientIO
import game.controllers.MainMenuController
import game.net.ClientIO.ConnectAs
import game.net.model.ConnectionType.ObserverClient
import game.net.model.GameObserverMessages.{ListAllRoomsRequest, Subscribe}

import scala.io.StdIn

object LaunchGame extends App {

  MainMenuController.becomeActive()

//  launchConsoleObserver()

  def launchConsoleObserver(): Unit = {
    clientIO ! ConnectAs(ObserverClient, to = Globals.serverAddress)
    StdIn.readLine()
    clientIO ! ListAllRoomsRequest(rooms => println(rooms))
    val port = StdIn.readLine("input port").toInt
    clientIO ! Subscribe(new InetSocketAddress("127.0.0.1", port), onTurnMade = { (cells, score) =>
      println(cells)
      println(score)
    })
  }

}
