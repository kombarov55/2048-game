package game.net.handlerbehavior

import akka.actor.Actor
import game.net.GameObserverMessages.{FailedToSubscribe, ListAllRoomsRequest, ListAllRoomsResponse, Subscribe, Subscribed}
import game.net.ServerGlobals

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
