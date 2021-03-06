package game.net.handlers.handlerbehavior.client

import akka.actor.Actor
import akka.actor.SupervisorStrategy.Stop
import game.Globals
import game.net.handlers.handlerbehavior.{IOBehavior, SocketHandler}
import game.net.model.LobbyMessages.{AddPlayer, AllPlayers}
import game.net.model.Player

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
