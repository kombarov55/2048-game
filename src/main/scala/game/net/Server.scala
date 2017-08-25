package game.net

import java.net.ServerSocket

import game.Implicits.Function2Runnable
import akka.actor.{Actor, ActorRef, Props}
import game.net.messages.{DeltaToConsume, ProducedDelta}

import scala.collection.mutable

class Server(port: Int) extends Actor {

  val serverSocket = new ServerSocket(port)

  val recipients = mutable.Buffer[ActorRef]()

  override def receive: Receive = {
    case ProducedDelta(score, cells) => broadcast(DeltaToConsume(score, cells))
    case "start" => launchJoiner()
  }

  def launchJoiner(): Unit = {
    new Thread({ () =>
      while (true) {
        val socket = serverSocket.accept()
        recipients += context.actorOf(Props(new SocketWrapperOnServer(socket)), name = s"serverListener${recipients.size}")
      }
    }).start()
  }

  def broadcast(delta: DeltaToConsume): Unit = {
    for (recipient <- recipients) {
      recipient ! delta
    }
  }
}
