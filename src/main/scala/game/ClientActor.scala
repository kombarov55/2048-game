package game

import java.net.Socket

import akka.actor.{Actor, ActorRef, Props}
import game.controllers.GameController
import game.model.Cell
import game.net.SocketWrapperOnClient
import game.net.messages.{DeltaToConsume, ProducedDelta}

class ClientActor(gameController: GameController) extends Actor {

  var socketWrapper: ActorRef = _

  override def receive: Receive = {
    case DeltaToConsume(score, changes) => askToRender(score, changes)
    case x: ProducedDelta => socketWrapper ! x
    case Connect(address) => connect(address)
  }

  def askToRender(score: Int, changes: Seq[Cell]): Unit = {
    gameController.renderCells(changes)
    // Переделать это потом в вызов функции
    gameController.panel.scoreLabel.setText(score.toString)
  }

  def connect(address: String) = {
    val socket = new Socket(address, 6666)
    socketWrapper = context.actorOf(Props(new SocketWrapperOnClient(socket)))
  }
}

case class Connect(address: String)
