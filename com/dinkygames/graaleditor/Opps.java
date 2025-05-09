package com.dinkygames.graaleditor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Opps extends JLabel implements MouseListener {
   private static final long serialVersionUID = 1L;
   GraalEditor main;

   public Opps(GraalEditor main) {
      this.main = main;
      this.addMouseListener(this);
      this.setHorizontalAlignment(2);
      this.setBounds(0, 0, 226, 230);
      this.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/images/opps_current.png")));
   }

   public void mouseClicked(MouseEvent e) {
   }

   public void mouseEntered(MouseEvent e) {
   }

   public void mouseExited(MouseEvent e) {
   }

   public void mousePressed(MouseEvent e) {
      if (this.main.contentPane.getTabCount() > 0) {
         this.main.toolbar_checkbox_npcs.setSelected(true);
         this.main.options.toggleNPCVisibility(true);
         this.main.getCurrentItem().canvas.repaint();

         try {
            int x = e.getX();
            int y = e.getY();
            if (this.in(x, 0, 45) && this.in(y, 182, 230)) {
               int[] offsets = new int[]{e.getX(), e.getY() - 182};
               Entity object = this.main.getCurrentItem().level.addNPC();
               this.main.getCurrentItem().canvas.setSelectedObject(object, offsets);
            }
         } catch (NullPointerException var6) {
            var6.printStackTrace();
         }

      }
   }

   private boolean in(int value, int min, int max) {
      return value >= min && value <= max;
   }

   public void mouseReleased(MouseEvent e) {
   }
}
