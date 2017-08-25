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

    if (Window.prevPanel != null) Window.remove(Window.prevPanel)
    Window.add(panel)
    Window.prevPanel = panel
    Window.pack()
  }

}