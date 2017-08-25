package game.controllers

import java.awt.Color
import javax.swing.JButton

import game.Implicits.Function2ActionListener
import game.StaticData
import game.model.{Cell, Command, Field}
import game.swing.{GamePanel, KeyboardListener}

class GameController extends Controller {

  override val panel = new GamePanel

  override def bindPanelWithSelf(): Unit = {
    panel.pauseButton.addActionListener(onPauseClicked _)
    panel.addKeyListener(new KeyboardListener(e => proceedCommand(Command.getByKeyEvent(e))))
  }

  override def initializeModel(): Unit = {
    if (StaticData.field == null) {
      StaticData.field = new Field
      StaticData.field.createCellsOnStart()
    }
    renderCells()
    panel.scoreLabel.setText(StaticData.field.score.toString)
  }

  def proceedCommand(command: Command): Unit = {
    StaticData.field.makeTurn(command)
    panel.scoreLabel.setText(StaticData.field.score.toString)
    if (StaticData.field.anyMovementInLastTurn) StaticData.field.spawnNewCell()
    renderCells()
    if (StaticData.field.isGameOver) {
      (new GameoverController).becomeActive()
    }
  }

  def renderCells(changes: Seq[Cell] = StaticData.field.rows.flatten.toSeq): Unit = {
    for (cell <- changes) {
      val button = getButton(cell.x, cell.y)
      button.setText(cell.score match {
        case 0 => ""
        case _ => cell.score.toString
      })
      button.setBackground(scoreToColor(cell.score))
    }
  }

  def onPauseClicked(): Unit = {
    MainMenuController.becomeActive()
  }

  def getButton(x: Int, y: Int): JButton = panel.buttons(y * 4 + x)

  val scoreToColor = Map(
                          0 -> new Color(255, 255, 255),
                          2 -> new Color(215, 250, 190),
                          4 -> new Color(170, 250, 120),
                          8 -> new Color(55, 165, 100),
                          16 -> new Color(160, 215, 250),
                          32 -> new Color(30, 155, 240),
                          64 -> new Color(215, 85, 240),
                          128 -> new Color(155, 55, 165),
                          256 -> new Color(240, 200, 135),
                          512 -> new Color(215, 155, 30),
                          1024 -> new Color(244, 240, 55),
                          2048 -> new Color(25, 245, 115),
                          4096 -> new Color(245, 70, 55))
}

object GameController {
  def becomeActive(): Unit = {
    (new MainMenuController).becomeActive()
  }
}
