package game.net

import java.io.{ObjectInputStream, ObjectOutputStream, Serializable}
import java.net.Socket

import akka.actor.{Actor, ActorSystem}
import game.Implicits.Function2Runnable
import game.net.messages.{DeltaToConsume, ProducedDelta}

abstract class SocketWrapper(socket: Socket) extends Actor {

  launchListener()

  def onDeltaReceived(delta: DeltaToConsume): Unit
  def sendDelta(delta: ProducedDelta): Unit

  override def receive: Receive = {
    case x: DeltaToConsume => onDeltaReceived(x)
    case x: ProducedDelta => sendDelta(x)
  }

  val system = ActorSystem("mySystem")

  val toSocket = new ObjectOutputStream(socket.getOutputStream)
  val fromSocket = new ObjectInputStream(socket.getInputStream)


  def launchListener(): Unit = {
    new Thread(() => {
      while (true) {
        self ! fromSocket.readObject()
      }
    }).start()
  }

  def sendToTheOtherEnd(obj: Serializable): Unit = {
    toSocket.writeObject(obj)
    toSocket.flush()
  }
}
