package game.net.handlers.handlerbehavior.server

import akka.actor.Actor
import game.net.ServerGlobals
import game.net.handlers.handlerbehavior.{IOBehavior, SocketHandler}
import game.net.model.GameObserverMessages.{FailedToSubscribe, ListAllRoomsRequest, ListAllRoomsResponse, Subscribe, Subscribed}

trait ObserverServerBehavior extends Actor with SocketHandler with IOBehavior {

  def observerServerBehavior: Receive = ioBehavior orElse {

    case ListAllRoomsRequest =>
      println("sending all rooms ")
      sendToTheOtherEnd(ListAllRoomsResponse(ServerGlobals.rooms.map { room => room.hostAddress }))

    case Subscribe(hostAddress) =>
      println("Subscribe received")

      var roomFound = false

      val optionRoom = ServerGlobals.rooms.find { room => room.hostAddress == hostAddress }
      if (optionRoom.isDefined) {
        println("subscribing to " + optionRoom.get)
        val room = optionRoom.get
        room.observers += self
        optionRoom.get.observers += connection

        sendToTheOtherEnd(Subscribed)
      } else {
        println("room not found ;(")
        sendToTheOtherEnd(FailedToSubscribe)
      }

    case other => println("observer handler received unknown message: " + other)
  }

}
