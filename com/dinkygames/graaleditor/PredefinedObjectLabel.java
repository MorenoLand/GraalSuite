package com.dinkygames.graaleditor;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PredefinedObjectLabel extends JLabel {
   public PredefinedObject object;
   private GraalEditor main;
   private BufferedImage tileset;
   private BufferedImage tileobject = null;
   JPanel parent;

   public PredefinedObjectLabel(final PredefinedObject object, JPanel parent, final GraalEditor main) {
      if (object != null) {
         this.object = object;
         this.parent = parent;
         this.main = main;
         this.tileset = main.scroll_tileset.tileset;
         this.tileobject = new BufferedImage(this.object.width * 16, this.object.height * 16, 2);
         Graphics2D g = this.tileobject.createGraphics();

         for(int y = 0; y < object.height; ++y) {
            for(int x = 0; x < object.width; ++x) {
               Point p = TileFunctions.tileToPoint(this.object.tiles[y][x]);
               int sx = (int)p.getX();
               int sy = (int)p.getY();
               g.drawImage(this.tileset, x * 16, y * 16, x * 16 + 16, y * 16 + 16, sx, sy, sx + 16, sy + 16, (ImageObserver)null);
            }
         }

         g.dispose();
         this.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent arg0) {
               if (main.contentPane.getTabCount() > 0) {
                  int ox = arg0.getX();
                  int oy = arg0.getY();
                  int[] offsets = new int[]{ox, oy};
                  if (main.getCurrentItem().canvas.tileselection != null && main.getCurrentItem().canvas.tileselection.getx() > 0 && main.getCurrentItem().canvas.tileselection.gety() > 0) {
                     main.getCurrentItem().canvas.clearAndPlaceTileSelection();
                     main.getCurrentItem().canvas.repaint();
                  }

                  main.getCurrentItem().canvas.setTileSelection(object.tiles, offsets);
               }
            }

            public void mouseEntered(MouseEvent arg0) {
            }

            public void mouseExited(MouseEvent arg0) {
            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent arg0) {
            }
         });
         this.rescale();
      }
   }

   public void update() {
      this.tileset = this.main.scroll_tileset.tileset;
      this.tileobject = new BufferedImage(this.object.width * 16, this.object.height * 16, 2);
      Graphics2D g = this.tileobject.createGraphics();

      for(int y = 0; y < this.object.height; ++y) {
         for(int x = 0; x < this.object.width; ++x) {
            Point p = TileFunctions.tileToPoint(this.object.tiles[y][x]);
            int sx = (int)p.getX();
            int sy = (int)p.getY();
            g.drawImage(this.tileset, x * 16, y * 16, x * 16 + 16, y * 16 + 16, sx, sy, sx + 16, sy + 16, (ImageObserver)null);
         }
      }

      g.dispose();
      this.rescale();
   }

   public void rescale() {
      Dimension d = this.parent.getSize();
      float scalex = (float)Math.min(d.width, this.object.width * 16);
      float scaley = (float)this.tileobject.getHeight() * (scalex / (float)this.tileobject.getWidth());
      if (!(scalex <= 0.0F) && !(scaley <= 0.0F)) {
         ImageIcon imageIcon = new ImageIcon(this.tileobject.getScaledInstance((int)scalex, (int)scaley, 1));
         this.setIcon(imageIcon);
         this.repaint();
         this.revalidate();
      }
   }
}
