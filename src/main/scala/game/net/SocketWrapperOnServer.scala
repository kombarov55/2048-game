package game.net

import java.net.Socket

import game.net.messages.{DeltaToConsume, ProducedDelta}

class SocketWrapperOnServer(socket: Socket) extends SocketWrapper(socket) {

  override def onDeltaReceived(delta: DeltaToConsume): Unit = ???

  override def sendDelta(delta: ProducedDelta): Unit = ???

}

