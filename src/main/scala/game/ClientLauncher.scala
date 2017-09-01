package game

import game.net.LobbyClient
import game.net.LobbyProtocol.AllPlayers

import scala.io.StdIn

object ClientLauncher extends App {


  val client = StaticData.system.actorOf(LobbyClient.props("localhost", 6666), "client")

  println("Вводите сообщения")

  while (true) {
    val line = StdIn.readLine()
    client ! AllPlayers
  }

}