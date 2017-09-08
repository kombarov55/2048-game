package game.net.handlerbehavior

import akka.actor.Actor
import akka.actor.SupervisorStrategy.Stop
import game.net.LobbyMessages.AllPlayers

trait LobbyClientBehavior extends Actor with SocketHandler with IOBehavior {

  def handleLobbyClient: Receive = ioBehavior orElse {
    case ap @ AllPlayers => sendToTheOtherEnd(ap)

    case AllPlayers(players) =>
      println(players)
//      onPlayersReceived(players)

    case Stop => context stop self

    case other: Any => println("client: received unknown message: " + other)
  }

}
