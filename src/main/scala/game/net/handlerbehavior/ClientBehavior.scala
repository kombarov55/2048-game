package game.net.handlerbehavior

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import akka.io.Tcp.{Connect, Connected, Register}
import akka.io.{IO, Tcp}
import game.Globals
import game.net.LobbyProtocol.AddPlayer
import game.net.ServerConnectionHandler.{Lobby, SetConnectionType}

//TODO: сделать получение Connect, при котором будет делать то что происходит в клиенте в preStart и в case Connected.
trait ClientBehavior extends Actor with IOBehavior {

  var connection: ActorRef
  val serverAddress: InetSocketAddress

  override def preStart(): Unit = {
    IO(Tcp)(Globals.system) ! Connect(serverAddress)
  }

  def clientBehavior: Receive = {

    case Connected(_, localAddress) =>
      connection = sender()
      connection ! Register(self)
      Globals.localAddress = localAddress
      sendToTheOtherEnd(SetConnectionType(Lobby))
      //TODO: Сообщение пропадает, если посылать их подряд. Нужно разобраться как получить то сообщение
      Thread.sleep(100)
      sendToTheOtherEnd(AddPlayer(Globals.userName, localAddress))
  }

}
