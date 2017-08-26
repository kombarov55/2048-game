package game.net

import java.net.Socket

import game.net.messages.{DeltaToConsume, ProducedDelta}


class SocketWrapperOnClient(socket: Socket) extends SocketWrapper(socket) {

  override def onDeltaReceived(delta: DeltaToConsume): Unit = context.parent ! delta

  override def sendDelta(delta: ProducedDelta): Unit = sendToTheOtherEnd(delta)
}
