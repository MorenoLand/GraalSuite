package com.dinkygames.graaleditor;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImageCanvas extends JPanel {
   private BufferedImage image;

   public ImageCanvas(String file) {
      try {
         this.image = ImageIO.read(this.getClass().getResource(file));
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(this.image, 0, 0, this);
   }
}
