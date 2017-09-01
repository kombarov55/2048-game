package game.net

import java.net.{ServerSocket, SocketAddress}

import akka.actor.{Actor, ActorRef, Props}
import game.Implicits.Function2Runnable
import game.net.Server.{AllPlayersRequest, AllPlayersResponse, Handshake, Start}
import game.net.SocketWrapper.Send

class Server(port: Int) extends Actor {

  val serverSocket = new ServerSocket(port)

  var players = List[Player]()

  var playersToSockets = Map[Player, ActorRef]()


  override def receive: Receive = {
    case Handshake(address, name) => onHandshake(address, name)
    case AllPlayersRequest(callbackIp) => sendAllPlayersTo(callbackIp)
    case Start => launchJoiner()
  }

  def launchJoiner(): Unit = {
    new Thread({ () =>
      while (true) {
        val socket = serverSocket.accept()
        val socketOnServerSide = context.actorOf(Props(new SocketWrapper(socket)), "serverSocket")
        playersToSockets += Player(socket.getRemoteSocketAddress) -> socketOnServerSide
      }
    }).start()
  }

  def onHandshake(address: SocketAddress, name: String): Unit = {
    val optionPair = playersToSockets.find { pair => pair._1.address == address }
    if (optionPair.isDefined) {
      optionPair.get._1.name = name
    } else {
      println("Игрок не найден при приветствии")
    }
  }

  def sendAllPlayersTo(address: SocketAddress): Unit = {
    val optionPair = playersToSockets.find { pair => pair._1.address == address }
    if (optionPair.isDefined) {
      val players = playersToSockets.keys
      optionPair.get._2 ! Send(AllPlayersResponse(players.toSeq))
    } else {
      println("Некому отправлять игроков")
    }
  }
}

object Server {

  case class Handshake(address: SocketAddress, name: String)

  case class AllPlayersRequest(callbackIp: SocketAddress = null) extends Serializable

  case class AllPlayersResponse(players: Seq[Player]) extends Serializable

  case class Start()

}
