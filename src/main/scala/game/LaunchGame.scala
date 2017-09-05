package game

import akka.actor.Props
import game.model.Field
import game.net.RoomHostClient
import game.net.RoomHostClient.Connect
import game.net.RoomHostMessages.TurnMade

import scala.io.StdIn

object LaunchGame extends App {

  //  MainMenuController.becomeActive()

  val client = Globals.system.actorOf(Props(new RoomHostClient(Globals.serverAddress)))
  StdIn.readLine()
  client ! Connect

  val cells = (new Field).rows.flatten
  val score = 42
  while (true) {
    StdIn.readLine()
    client ! TurnMade(cells, score)
  }


}
