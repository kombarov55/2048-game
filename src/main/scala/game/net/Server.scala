package game.net

import java.net.{InetAddress, ServerSocket}

import akka.actor.{Actor, Props}
import game.Implicits.{Function2Runnable, RichOption}
import game.net.Server.{AllPlayersRequest, AllPlayersResponse, ConnectionResponse, Handshake, Start}
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
        players = Player(socket.getInetAddress, socketOnServerSide) :: players
        println("all players: " + players)

        socketOnServerSide ! Send(ConnectionResponse(socket.getInetAddress))
      }
    }).start()
  }

  def onHandshake(address: InetAddress, name: String): Unit = {
    players
    .find(_.address == address)
    .ifPresent { player =>
      player.name = name
    }
    .otherwise { println("Игрок не найден") }
  }

  def sendAllPlayersTo(address: InetAddress): Unit = {
    players
    .find(_.address == address)
    .ifPresent { player =>
      val socketRef = player.socketRef
      socketRef ! Send(AllPlayersResponse(players))
    }
  }

}

object Server {
  case class ConnectionResponse(yourAddress: InetAddress)
  case class Handshake(address: InetAddress, name: String)
  case class AllPlayersRequest(callbackIp: InetAddress = null) extends Serializable
  case class AllPlayersResponse(players: Seq[Player]) extends Serializable
  case class Start()
}
