package game.controllers

import game.swing.{Panel, Window}

trait Controller {

  val panel: Panel

  def initializeModel(): Unit = {}

  def bindPanelWithSelf(): Unit = {}

  def becomeActive(): Unit = {
    panel.addComponentsOnSelf()
    bindPanelWithSelf()
    initializeModel()
    Window.changePanelTo(panel)
  }

}
