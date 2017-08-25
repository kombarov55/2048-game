package game

import akka.actor.Actor
import game.controllers.GameController
import game.model.Cell
import game.net.messages.DeltaToConsume

class ClientActor(gameController: GameController) extends Actor {

  override def receive: Receive = {
    case DeltaToConsume(score, changes) => askToRender(score, changes)
  }

  def askToRender(score: Int, changes: Seq[Cell]): Unit = {
    gameController.renderCells(changes)
    // Переделать это потом в вызов функции
    gameController.panel.scoreLabel.setText(score.toString)
  }
}
