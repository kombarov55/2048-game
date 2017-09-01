package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, Props}
import akka.io.Tcp.{Bind, Bound, Command, CommandFailed, Connected, Register}
import akka.io.{IO, Tcp}
import game.StaticData

/**
  * Хранит списки игроков и handler-ов
  * Операции:
  * 1) Записать игрока
  * 2) Убрать игрока
  * 3) Получить список всех игроков
  */
class Server extends Actor {

  override def preStart(): Unit = {
    IO(Tcp)(StaticData.system) ! Bind(self, new InetSocketAddress("localhost", 6666))
  }

  override def receive: Receive = {
    case Bound(localAddress) => println("I was bound at " + localAddress)
    case CommandFailed(cmd: Command) => println("bounding failed..")

    case Connected(remote, local) =>
      println(s"someone connected. remote:local = $remote:$local")
      val handler = context.actorOf(Props(new LobbyHandler()), "handler")
      sender() ! Register(handler)
  }
}

object Server {

  var players = List.empty[Player]

}
