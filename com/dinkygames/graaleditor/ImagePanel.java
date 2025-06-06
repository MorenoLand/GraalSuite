package com.dinkygames.graaleditor;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
   private BufferedImage image;

   public ImagePanel(String file) {
      try {
         this.image = ImageIO.read(new File(file));
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(this.image, 0, 0, this);
   }
}
