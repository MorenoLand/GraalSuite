package com.dinkygames.graaleditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.JPanel;

public class Canvas_Tileset extends JPanel implements MouseMotionListener, MouseListener {
   private ScrollPane_Tileset parent;
   private BufferedImage image;
   private BufferedImage bufferimage;
   int[] selection = new int[]{-1, -1, -1, -1};
   int ox;
   int oy = 0;
   boolean mouseDown = false;
   int[] tileselection;

   public Canvas_Tileset(String file, ScrollPane_Tileset parent) {
      this.setBackground(Color.BLACK);
      this.parent = parent;
      this.addMouseListener(this);
      this.addMouseMotionListener(this);
      this.bufferimage = parent.tileset;
      this.image = new BufferedImage(this.bufferimage.getWidth(), this.bufferimage.getHeight(), 2);
      Graphics2D g2d = this.image.createGraphics();
      g2d.drawImage(this.bufferimage, 0, 0, (ImageObserver)null);
      g2d.dispose();
   }

   public void updateTileset(BufferedImage tileset) {
      this.image = tileset;
      this.bufferimage = new BufferedImage(this.image.getWidth(), this.image.getHeight(), 2);
      Graphics2D g2d = this.image.createGraphics();
      g2d.drawImage(this.image, 0, 0, (ImageObserver)null);
      g2d.dispose();
      this.repaint();
   }

   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2D = (Graphics2D)g;
      BufferedImage grid = new BufferedImage(32, 32, 2);
      Graphics2D g2 = (Graphics2D)grid.getGraphics();
      g2.setColor(Color.WHITE);
      g2.fillRect(0, 0, 32, 32);
      g2.setColor(Color.LIGHT_GRAY);
      g2.fillRect(0, 0, 16, 16);
      g2.fillRect(16, 16, 16, 16);
      g2D.setPaint(new TexturePaint(grid, new Rectangle(0, 0, 32, 32)));
      g2D.fillRect(0, 0, 2048, 512);
      g2D.drawImage(this.image, 0, 0, this);
      if (this.selection[0] >= 0 && this.selection[1] >= 0) {
         this.drawHighlight(g, this.selection);
      }

      g.dispose();
      g2.dispose();
      g2D.dispose();
   }

   private void drawHighlight(Graphics g, int[] s) {
      this.drawHighlight(g, s[0], s[1], s[2], s[3]);
   }

   private void drawHighlight(Graphics g, int x, int y, int w, int h) {
      Graphics bg = this.bufferimage.getGraphics();
      bg.drawImage(this.image, 0, 0, (ImageObserver)null);
      bg.dispose();
      int dx = x * 16;
      int dy = y * 16;
      int dw = w * 16;
      int dh = h * 16;
      this.drawInvertedRect(dx - 1, dy - 1, dw + 2, dh + 2);
      this.drawInvertedRect(dx, dy, dw, dh);
      g.drawImage(this.bufferimage, 0, 0, this);
      if (this.parent.mainwindow.options.contrastselection && (w > 0 || h > 0)) {
         --dx;
         --dy;
         dw += 2;
         dh += 2;
         g.setColor(new Color(0, 0, 0, 125));
         int[] xPoints = new int[]{0, 2056, 2056, 0, 0, dx, dx + dw, dx + dw, dx, dx};
         int[] yPoints = new int[]{0, 0, 512, 512, 0, dy, dy, dy + dh, dy + dh, dy};
         g.fillPolygon(xPoints, yPoints, xPoints.length);
      }

      g.dispose();
   }

   private void drawInvertedRect(int x, int y, int w, int h) {
      --w;
      --h;

      int i;
      for(i = 0; i < w; ++i) {
         this.drawInvertedPixel(this.bufferimage, x + i, y);
      }

      for(i = 0; i < w; ++i) {
         this.drawInvertedPixel(this.bufferimage, x + i, y + h);
      }

      for(i = 0; i < h; ++i) {
         this.drawInvertedPixel(this.bufferimage, x, y + i);
      }

      for(i = 0; i < h; ++i) {
         this.drawInvertedPixel(this.bufferimage, x + w, y + i);
      }

   }

   public void drawInvertedPixel(BufferedImage drawingGraphics, int x, int y) {
      if (x >= 0 && y >= 0) {
         if (x < this.image.getWidth() && y < this.image.getHeight()) {
            this.bufferimage.setRGB(x, y, this.getInvertedColor(this.image.getRGB(x, y)));
         }
      }
   }

   private int getInvertedColor(int rgb) {
      int a = 255 & rgb >> 24;
      return a == 0 ? -65281 : 16777215 - rgb | -16777216;
   }

   public void mouseDragged(MouseEvent arg0) {
      if (this.parent.mainwindow.contentPane.getTabCount() > 0) {
         int mx = Math.min(Math.max(arg0.getX() + 8, 0), 2056) / 16;
         int my = Math.min(Math.max(arg0.getY() + 8, 0), 512) / 16;
         if (mx < this.ox) {
            this.selection[0] = mx;
            this.selection[2] = Math.abs(this.ox - mx);
         } else {
            this.selection[0] = this.ox;
            this.selection[2] = mx - this.selection[0];
         }

         if (my < this.oy) {
            this.selection[1] = my;
            this.selection[3] = Math.abs(this.oy - my);
         } else {
            this.selection[1] = this.oy;
            this.selection[3] = my - this.selection[1];
         }

         this.repaint();
         String output = String.format("(%d, %d) -> (%d, %d) = (%d, %d)", this.ox, this.oy, mx, my, this.selection[2], this.selection[3]);
         this.parent.mainwindow.setStatusBarRight(output);
      }
   }

   public void mouseMoved(MouseEvent arg0) {
      int mx = Math.min(Math.max(arg0.getX() + 8, 0), 2040) / 16;
      int my = Math.min(Math.max(arg0.getY() + 8, 0), 496) / 16;
      String sx = String.valueOf(mx % 16);
      String sy = String.valueOf(mx / 16 * 32 + my);
      this.parent.mainwindow.setStatusBarRight("Select tile (" + sx + ", " + sy + ")");
   }

   public void mouseClicked(MouseEvent arg0) {
      if (this.parent.mainwindow.contentPane.getTabCount() > 0) {
         this.releaseSelection();
         if (arg0.getClickCount() == 2) {
            ScrollPane_Level level = (ScrollPane_Level)this.parent.mainwindow.contentPane.getSelectedComponent();
            level.canvas.dragging = false;
            level.canvas.selectedObject = null;
            level.canvas.tileselection = null;
            level.canvas.repaint();
            short tile = TileFunctions.pointToTile(arg0.getPoint());
            this.parent.mainwindow.updateDefaultTile(tile);
         }

      }
   }

   public void mouseEntered(MouseEvent arg0) {
   }

   public void mouseExited(MouseEvent arg0) {
   }

   public void mousePressed(MouseEvent arg0) {
      if (this.parent.mainwindow.contentPane.getTabCount() > 0) {
         if (!this.mouseDown) {
            ScrollPane_Level level = (ScrollPane_Level)this.parent.mainwindow.contentPane.getSelectedComponent();
            if (level.canvas.tileselection != null && level.canvas.tileselection.getx() > 0 && level.canvas.tileselection.gety() > 0) {
               level.canvas.clearAndPlaceTileSelection();
               level.canvas.repaint();
            }

            int mx = Math.min(Math.max(arg0.getX() + 8, 0), 2056) / 16;
            int my = Math.min(Math.max(arg0.getY() + 8, 0), 512) / 16;
            this.ox = mx;
            this.oy = my;
            this.selection = new int[]{mx, my, 0, 0};
         }

         this.mouseDown = true;
         this.repaint();
      }
   }

   public void mouseReleased(MouseEvent arg0) {
      if (this.parent.mainwindow.contentPane.getTabCount() > 0) {
         int y;
         int x;
         short tile;
         if (this.selection[2] > 0 && this.selection[3] > 0) {
            short[][] tiles = new short[this.selection[3]][this.selection[2]];

            for(y = 0; y < this.selection[3]; ++y) {
               for(x = 0; x < this.selection[2]; ++x) {
                  tile = TileFunctions.pointToTile(new Point((this.selection[0] + x) * 16, (this.selection[1] + y) * 16));
                  tiles[y][x] = tile;
               }
            }

            y = arg0.getX() - this.selection[0] * 16;
            x = arg0.getY() - this.selection[1] * 16;
            ScrollPane_Level level = (ScrollPane_Level)this.parent.mainwindow.contentPane.getSelectedComponent();
            int[] offsets = new int[]{y, x};
            level.canvas.setTileSelection(tiles, offsets);
            this.tileselection = new int[]{5};
            this.releaseSelection();
         } else {
            ScrollPane_Level level = (ScrollPane_Level)this.parent.mainwindow.contentPane.getSelectedComponent();
            y = Math.min(Math.max(arg0.getX(), 0), 2056);
            x = Math.min(Math.max(arg0.getY(), 0), 512);
            tile = TileFunctions.pointToTile(new Point(y, x));
            short[][] tiles = new short[][]{{tile}};
            int[] offsets = new int[]{arg0.getX() - y, arg0.getY() - x};
            if (level.canvas.tileselection != null && level.canvas.tileselection.getx() > 0 && level.canvas.tileselection.gety() > 0) {
               level.canvas.clearAndPlaceTileSelection();
               level.canvas.repaint();
            }

            level.canvas.setTileSelection(tiles, offsets);
            this.tileselection = new int[]{5};
            this.releaseSelection();
         }
      }
   }

   private void releaseSelection() {
      this.selection = new int[]{-1, -1, -1, -1};
      this.tileselection = null;
      this.mouseDown = false;
      this.repaint();
   }

   public void cleanup() {
      this.image = null;
      this.bufferimage = null;
   }
}
