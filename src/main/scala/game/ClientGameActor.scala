package game

import akka.actor.Actor
import game.controllers.GameController
import game.net.messages.ConsumingDelta

class ClientGameActor(gameController: GameController) extends Actor {



  override def receive: Receive = {
    case x: ConsumingDelta =>
  }
}
