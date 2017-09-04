package game.swing

import java.awt.Component
import javax.swing.{JLabel, JList, ListCellRenderer}

import game.net.Player

class PlayerRenderer extends ListCellRenderer[Player] {
  override def getListCellRendererComponent(list: JList[_ <: Player], value: Player, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component = {
    val label = new JLabel(value.name)

    label.setOpaque(true)

    if (isSelected) {
      label.setBackground(list.getSelectionBackground)
      label.setForeground(list.getSelectionForeground)
    } else {
      label.setBackground(list.getBackground)
      label.setForeground(list.getForeground)
    }

    label
  }
}
