package game

import game.controllers.MainMenuController

object Launcher extends App {

  // Может сделать этот вызов как то покрасивее? Например через Companion-object. Логика будет некрасиво запрятана и так и так
  (new MainMenuController).becomeActive()

}
