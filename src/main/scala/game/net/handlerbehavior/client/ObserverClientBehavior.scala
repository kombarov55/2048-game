package game.net.handlerbehavior.client

import akka.actor.Actor
import game.model.Cell
import game.net.GameObserverMessages.{FailedToSubscribe, ListAllRoomsRequest, ListAllRoomsResponse, Subscribe, Subscribed}
import game.net.RoomHostMessages.TurnMade
import game.net.handlerbehavior.{IOBehavior, SocketHandler}

trait ObserverClientBehavior extends Actor with SocketHandler with IOBehavior {

//  var onSubscribed: () => Unit
  var onTurnMade: (Seq[Cell], Int) => Unit

  def observerClientBehavior: Receive = ioBehavior orElse {

    case ListAllRoomsRequest =>
      println("list all rooms request")
      sendToTheOtherEnd(ListAllRoomsRequest)

    case ListAllRoomsResponse(rooms) =>
      println(rooms)

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
