package game

import java.net.InetSocketAddress

import game.Globals.clientIO
import game.model.Field
import game.net.ClientIO.ConnectAs
import game.net.ConnectionType.{ObserverClient, RoomHostClient}
import game.net.GameObserverMessages.{ListAllRoomsRequest, Subscribe}
import game.net.RoomHostMessages.{CreateRoom, TurnMade}

import scala.io.StdIn

object LaunchGame extends App {

  //    MainMenuController.becomeActive()

//  host()
  client()

  def client(): Unit = {
    clientIO ! ConnectAs(ObserverClient, to = Globals.serverAddress)
    StdIn.readLine()
    clientIO ! ListAllRoomsRequest
    val port = StdIn.readLine("input port").toInt
    clientIO ! Subscribe(new InetSocketAddress("127.0.0.1", port))
  }

  def host(): Unit = {
    clientIO ! ConnectAs(RoomHostClient, to = Globals.serverAddress)
    StdIn.readLine()
    clientIO ! CreateRoom
    val field = new Field
    while (true) {
      StdIn.readLine("enter to make turn")
      clientIO ! TurnMade(field.cells, field.score)
    }
  }

}
