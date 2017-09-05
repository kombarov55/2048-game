package game.net

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import game.net.RoomHostClient.Connect
import game.net.RoomHostMessages.{CreateRoom, RoomCreated, TurnMade}
import game.net.ServerConnectionHandler.ConnectionType
import game.net.handlerbehavior.{ClientBehavior, IOBehavior}

class RoomHostClient(val serverAddress: InetSocketAddress) extends Actor with IOBehavior with ClientBehavior {

  var connection: ActorRef = Actor.noSender

  val connectionType: ConnectionType = ConnectionType.RoomHost


  override def preStart(): Unit = {
    super.preStart()

  }

  override def receive: Receive = ioBehavior orElse clientBehavior orElse {
    case Connect =>
      println("connecting")
      sendToTheOtherEnd(CreateRoom)

    case msg @ TurnMade(_, _) => sendToTheOtherEnd(msg)
//      implicit val timeout = Timeout(5000)
//      ask(connection, CreateRoom).onSuccess {
//        case RoomCreated => println("room created. ")
//      }

    case RoomCreated => println("Room created")
    case other => println("room host client: received unknown message - " + other)
  }
}

object RoomHostClient {
  case object Connect
}
