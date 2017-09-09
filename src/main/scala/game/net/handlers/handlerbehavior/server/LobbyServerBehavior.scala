package game.net.handlers.handlerbehavior.server

import akka.actor.Actor
import akka.io.Tcp.{ErrorClosed, PeerClosed}
import game.net.ServerGlobals.{lobbyHandlers, players}
import game.net.handlers.ServerConnectionHandler.SendToTheOtherEnd
import game.net.handlers.handlerbehavior.IOBehavior
import game.net.model.LobbyMessages.{AddPlayer, AllPlayers}
import game.net.model.Player

trait LobbyServerBehavior extends Actor with IOBehavior {

  def lobbyBehavior: Receive = ioBehavior orElse {
    case PeerClosed => stop()

    case ErrorClosed(_) => stop()

    case AddPlayer(name, address) =>
      val newPlayer = Player(name, address)
      players = newPlayer :: players
      broadcast(AllPlayers(players))

    case SendToTheOtherEnd(message) => sendToTheOtherEnd(message)

    case unknown: Any => println("lobby handler received unknown message: " + unknown)
  }

  def stop(): Unit = {
    players = players.filterNot { _.address == remoteAddress }
    lobbyHandlers = lobbyHandlers.filterNot { actorRef => actorRef == connection }
    broadcast(AllPlayers(players))
    context stop self
  }

  def broadcast(message: AnyRef): Unit = {
    println(s"lobby behavior: broadcasting $message")
    for (handler <- lobbyHandlers) {
      handler ! SendToTheOtherEnd(message)
    }
  }

}
