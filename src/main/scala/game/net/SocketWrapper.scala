package game.net

import akka.actor.Actor
import game.net.messages.{ConsumingDelta, ProducingDelta}

abstract class SocketWrapper extends Actor {

  def onConsumingDeltaReceived(consumingDelta: ConsumingDelta): Unit
  def onProducingDeltaReceived(producingDelta: ProducingDelta): Unit

  //Потом передаваться будут объекты внутри этих case классов, а не сами они
  override def receive: Receive = {
    case x: ConsumingDelta => onConsumingDeltaReceived(x)
    case x: ProducingDelta => onProducingDeltaReceived(x)
  }
}
