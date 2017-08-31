package game.net

import java.net.{ServerSocket, SocketAddress}

import akka.actor.{Actor, Props}
import game.Implicits.{Function2Runnable, RichOption}
import game.net.Server.{AllPlayersRequest, AllPlayersResponse, Handshake, Start}
import game.net.SocketWrapper.Send

class Server(port: Int) extends Actor {

  val serverSocket = new ServerSocket(port)

  var players = List[Player]()


  override def receive: Receive = {
    case Handshake(address, name) => onHandshake(address, name)
    case AllPlayersRequest(callbackIp) => sendAllPlayersTo(callbackIp)
    case Start => launchJoiner()
  }

  def launchJoiner(): Unit = {
    new Thread({ () =>
      while (true) {
        val socket = serverSocket.accept()
        val socketOnServerSide = context.actorOf(Props(new SocketWrapper(socket)))
        players = Player(socket.getRemoteSocketAddress, socketOnServerSide) :: players
        println("all players: " + players)
      }
    }).start()
  }

  def onHandshake(address: SocketAddress, name: String): Unit = {
    val player = players.find(_.address == address)
    if (player.isDefined) {
      player.get.name = name
    } else {
      println("Игрок не найден..")
    }
  }

  def sendAllPlayersTo(address: SocketAddress): Unit = {
    players
    .find(_.address == address)
    .ifPresent { player =>
      val socketRef = player.socketRef
      socketRef ! Send(AllPlayersResponse(players))
    }.otherwise {
      println("Не кому отправлять список всех игроков..")
    }
  }

}

object Server {

  case class Handshake(address: SocketAddress, name: String)

  case class AllPlayersRequest(callbackIp: SocketAddress = null) extends Serializable

  case class AllPlayersResponse(players: Seq[Player]) extends Serializable

  case class Start()

}
