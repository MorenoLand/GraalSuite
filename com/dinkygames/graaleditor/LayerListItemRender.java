package com.dinkygames.graaleditor;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class LayerListItemRender extends JLabel implements ListCellRenderer<LayerListItem> {
   public LayerListItemRender() {
      this.setOpaque(true);
   }

   public Component getListCellRendererComponent(JList<? extends LayerListItem> list, LayerListItem layer, int index, boolean isSelected, boolean cellHasFocus) {
      String code = layer.toString();
      ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("layerpreviewfiller.png"));
      this.setIcon(imageIcon);
      this.setText(layer.toString());
      if (isSelected) {
         this.setBackground(list.getSelectionBackground());
         this.setForeground(list.getSelectionForeground());
      } else {
         this.setBackground(list.getBackground());
         this.setForeground(list.getForeground());
      }

      return this;
   }
}
