package game.net.handlerbehavior

import akka.actor.Actor
import akka.actor.SupervisorStrategy.Stop
import game.Globals
import game.net.LobbyMessages.{AddPlayer, AllPlayers}
import game.net.Player

trait LobbyClientBehavior extends Actor with SocketHandler with IOBehavior {

  var onPlayersReceived: (Seq[Player]) => Unit

  def handleLobbyClient: Receive = ioBehavior orElse {
    case ap @ AllPlayers => sendToTheOtherEnd(ap)

    case AddPlayer =>
      sendToTheOtherEnd(AddPlayer(Globals.userName, localAddress))

    case AllPlayers(players) =>
      onPlayersReceived(players)

    case Stop => context stop self

    case other: Any => println("client: received unknown message: " + other)
  }

}
