package game.net

import java.net.ServerSocket

import akka.actor.{Actor, Props}
import game.Implicits.{Function2Runnable, RichOption}
import game.net.Server.Handshake

import scala.collection.mutable

class Server(port: Int) extends Actor {

  val serverSocket = new ServerSocket(port)

  var players = mutable.Buffer[Player]()

  override def receive: Receive = {
    case Handshake(address, name) =>
    case "start" => launchJoiner()
  }

  def launchJoiner(): Unit = {
    new Thread({ () =>
      while (true) {
        val socket = serverSocket.accept()
        val socketOnServerSide = context.actorOf(Props(new SocketWrapperOnServer(socket)))
        players += Player(socket.getRemoteSocketAddress.toString, socketOnServerSide)
        println(socket.getRemoteSocketAddress + " connected")
      }
    }).start()
  }

  def updatePlayer(address: String, name: String): Unit = {
    players
    .find(_.address == address)
    .ifPresent { player => player.name = name }
  }

}

object Server {
  case class Handshake(address: String, name: String)
  case class RequestAllNames(callbackIp: String) extends Serializable
}
