package game

import akka.util.ByteString
import game.net.{LobbyClient, Serializer}

import scala.io.StdIn

object ClientLauncher extends App {


  val client = StaticData.system.actorOf(LobbyClient.props("localhost", 6666), "client")

  println("Вводите сообщения")

  while (true) {
    val line = StdIn.readLine()
    client ! ByteString(Serializer.serialize(line))

  }

}