package game.net

import java.io.{ObjectInputStream, ObjectOutputStream, Serializable}
import java.net.Socket

import akka.actor.Actor
import game.Implicits.Function2Runnable
import game.net.SocketWrapper.{Send, ToBeReceived}

class SocketWrapper(socket: Socket) extends Actor {

  val toSocket = new ObjectOutputStream(socket.getOutputStream)
  val fromSocket = new ObjectInputStream(socket.getInputStream)

  launchListener()

  override def receive: Receive = {
    case Send(obj) => sendToTheOtherEnd(obj)
    case ToBeReceived(obj) => onReceived(obj)
    case x: Any => println(x)
  }

  def sendToTheOtherEnd(obj: Serializable): Unit = {
    toSocket.writeObject(ToBeReceived(obj))
    toSocket.flush()
    println("sent: " + obj)
  }

  def onReceived(obj: Serializable): Unit = {
    println("received: " + obj)
    context.parent ! obj
  }

  def launchListener(): Unit = {
    new Thread(() => {
      while (true) {
        self ! fromSocket.readObject()
      }
    }).start()
  }
}

object SocketWrapper {
  case class Send(serializable: Serializable) extends Serializable
  case class ToBeReceived(serializable: Serializable) extends Serializable
}
