package game.net.handlers.handlerbehavior.client

import java.net.InetSocketAddress

import akka.actor.Actor
import game.model.Cell
import game.net.handlers.handlerbehavior.{IOBehavior, SocketHandler}
import game.net.model.GameObserverMessages.{FailedToSubscribe, ListAllRoomsRequest, ListAllRoomsResponse, Subscribe, Subscribed}
import game.net.model.RoomHostMessages.TurnMade

trait ObserverClientBehavior extends Actor with SocketHandler with IOBehavior {

//  var onSubscribed: () => Unit
  var onTurnMade: (Seq[Cell], Int) => Unit

  var allRoomsResponseCallback: (Seq[InetSocketAddress]) => Unit = _

  def observerClientBehavior: Receive = ioBehavior orElse {

    case ListAllRoomsRequest(callback) =>
      allRoomsResponseCallback = callback
      println("list all rooms request")
      sendToTheOtherEnd(ListAllRoomsRequest)

    case ListAllRoomsResponse(rooms) =>
      allRoomsResponseCallback(rooms)
      allRoomsResponseCallback = null

    case msg @ Subscribe(_) =>
      println("sending " + msg)
      sendToTheOtherEnd(msg)

    case Subscribed =>
      println("subscribed successfully")
//      onSubscribed()

    case FailedToSubscribe =>
      println("Failed to Subscribe")

    case TurnMade(cells, score) =>
      onTurnMade(cells, score)

    case other => println("unknown message " + other)
  }

}
