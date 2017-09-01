package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, Props}
import akka.io.Tcp.{Connect, Connected, Received, Register, Write}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import game.StaticData

/**
  * Тот, кто будет выполнять обращения к серверу, когда пользователь в лобби. Должен:
  * 1) Регистрировать себя у сервера
  * 2) Запрашивать список всех присутствующих
  * 3) При выходе посылать запрос на удаления себя из того списка
  */
class LobbyClient(serverAddress: InetSocketAddress) extends Actor {

  var connection: ActorRef = _

  override def preStart(): Unit = {
    IO(Tcp)(StaticData.system) ! Connect(serverAddress)
  }

  override def receive: Receive = {
    case Connected(_, localAddress) =>

      StaticData.localSocketAddress = localAddress
      connection = sender()
      connection ! Register(self)

      connection ! Server.AddPlayer(StaticData.userName, StaticData.localSocketAddress)

    case Received(data) =>
      println("client received message, now deserializing..")
      val deserializedMessage = Serializer.deserialize(data.toArray)
      self ! deserializedMessage

    case str: String => println("client: received string: " + str)

    case data: ByteString => connection ! Write(data)
  }
}

object LobbyClient {
  def props(ip: String, port: Int): Props = Props(new LobbyClient(new InetSocketAddress(ip, port)))
}
