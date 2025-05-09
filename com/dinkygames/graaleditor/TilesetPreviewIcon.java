package com.dinkygames.graaleditor;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class TilesetPreviewIcon extends JLabel {
   private BufferedImage image;
   private String file = "/res/images/pics1.png";
   short defaulttile = 2047;
   GraalEditor mainwindow;
   TileFunctions tf = new TileFunctions();

   public TilesetPreviewIcon(GraalEditor mainwindow) {
      this.mainwindow = mainwindow;
      this.image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
      
      try {
         InputStream imageStream = null;
         imageStream = this.getClass().getResourceAsStream(this.file);
         
         if (imageStream == null) {
            imageStream = this.getClass().getClassLoader().getResourceAsStream(this.file.substring(1));
         }
         
         if (imageStream == null) {
            File resFile = new File("." + this.file);
            if (resFile.exists()) {
               imageStream = new FileInputStream(resFile);
            }
         }
         
         if (imageStream != null) {
            BufferedImage loadedImage = ImageIO.read(imageStream);
            if (loadedImage != null) {
               this.image = loadedImage;
            }
            imageStream.close();
         } else {
            System.out.println("Warning: Could not find resource: " + this.file);
         }
      } catch (Exception e) {
         System.out.println("Error loading tileset preview: " + e.getMessage());
      }
   }

   public TilesetPreviewIcon(String file) {
      this.image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
      
      try {
         InputStream imageStream = null;
         imageStream = this.getClass().getResourceAsStream(file);
         
         if (imageStream == null) {
            imageStream = this.getClass().getClassLoader().getResourceAsStream(file.substring(1));
         }
         
         if (imageStream == null) {
            File resFile = new File("." + file);
            if (resFile.exists()) {
               imageStream = new FileInputStream(resFile);
            }
         }
         
         if (imageStream != null) {
            BufferedImage loadedImage = ImageIO.read(imageStream);
            if (loadedImage != null) {
               this.image = loadedImage;
            }
            imageStream.close();
         } else {
            System.out.println("Warning: Could not find resource: " + file);
         }
      } catch (Exception e) {
         System.out.println("Error loading tileset preview: " + e.getMessage());
      }
   }

   public void updateDefaultTile() {
   }

   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D)g;
      if (this.image != null && this.mainwindow != null) {
         Point tilecoord = TileFunctions.tileToPoint(this.mainwindow.defaultTile);
         g2d.drawImage(this.image, 0, 0, 16, 16, tilecoord.x, tilecoord.y, tilecoord.x + 16, tilecoord.y + 16, (ImageObserver)null);
      }
      g2d.dispose();
   }
}