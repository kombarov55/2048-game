package game.swing

import java.awt.Font
import javax.swing.{JButton, JLabel, JTextArea}

class SettingsPanel extends Panel {

  val namePromptLabel = new JLabel("Ваше имя:")
  val nameInput = new JTextArea()
  val addressPromptLabel = new JLabel("Адрес сервера:")
  val addressInput = new JTextArea()
  val save = new JButton("Сохранить")
  val back = new JButton("Назад")


  namePromptLabel.setBounds(100, 50, 300, 50)
  nameInput.setBounds(100, 100, 300, 50)
  addressPromptLabel.setBounds(100, 150, 300, 50)
  addressInput.setBounds(100, 200, 300, 50)
  save.setBounds(100, 350, 300, 50)
  back.setBounds(100, 400, 300, 50)

  nameInput.setFont(new Font("Arial", Font.BOLD, 32))
  addressInput.setFont(new Font("Arial", Font.BOLD, 32))

  override def addComponentsOnSelf(): Unit = {
    setLayout(null)
    add(namePromptLabel)
    add(nameInput)
    add(addressPromptLabel)
    add(addressInput)
    add(save)
    add(back)
  }
}
