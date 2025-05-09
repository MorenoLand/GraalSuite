package com.dinkygames.graaleditor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Arrays;

public class Layer {
   public short[][] tiles;
   public boolean visible = true;
   private BufferedImage tilerender = new BufferedImage(1024, 1024, 2);
   Level level;
   public float alpha = 1.0F;
   public String name = null;

   public Layer(Level level, short[][] tiles) {
      this.level = level;
      this.tiles = tiles;
      int layerindex = this.level.getLayerIndex(this);
      if (this.tiles == null) {
         this.tiles = new short[64][64];

         for(int i = 0; i < 64; ++i) {
            Arrays.fill(this.tiles[i], (short)-1);
         }
      }

   }

   public void setName(String name) {
      this.name = name;
   }

   public void cleanup() {
      this.tilerender = null;
   }

   public void updateTileRender() {
      Graphics2D g2d = (Graphics2D)this.tilerender.getGraphics();
      g2d.setBackground(new Color(0, 0, 0, 0));
      g2d.clearRect(0, 0, 1024, 1024);

      int y;
      for(y = 0; y < 64; ++y) {
         for(int x = 0; x < this.tiles[y].length; ++x) {
            int tile = this.tiles[y][x];
            if (tile >= 0) {
               Point tilecoord = TileFunctions.tileToPoint(tile);
               int tx = (int)tilecoord.getX();
               int ty = (int)tilecoord.getY();
               g2d.drawImage(this.level.scroll.canvas.tileset, x * 16, y * 16, x * 16 + 16, y * 16 + 16, tx, ty, tx + 16, ty + 16, (ImageObserver)null);
            }
         }
      }

      y = this.level.getLayerIndex(this);
      this.level.main.updateLayerPanelThumbnail(y);
      g2d.dispose();
   }

   public BufferedImage getTileRender() {
      return this.tilerender;
   }

   public void setVisibility(boolean val) {
      this.visible = val;
      this.level.scroll.canvas.repaint();
   }

   public void setLayerRow(int x, int y, short[] tiles) {
      for(int i = 0; i < tiles.length; ++i) {
         this.tiles[y][x + i] = tiles[i];
      }

   }

   public String getRowData(int z, int y) {
      String NEWLINE = System.getProperty("line.separator");
      String finaldata = "";
      String data = "";
      int tilex = -1;
      int width = 0;

      for(int i = 0; i < 64; ++i) {
         short tile = this.tiles[y][i];
         if (tile >= 0) {
            if (tilex < 0) {
               tilex = i;
            }

            ++width;
            data = data + TileFunctions.tiletoBase64(tile);
         } else {
            if (data.length() > 0) {
               if (finaldata.length() > 0) {
                  finaldata = finaldata + NEWLINE;
               }

               String newdata = String.format("BOARD %d %d %d %d %s", tilex, y, width, z, data);
               finaldata = finaldata + newdata;
               data = "";
               width = 0;
            }

            tilex = -1;
         }
      }

      if (data.length() > 0) {
         if (finaldata.length() > 0) {
            finaldata = finaldata + NEWLINE;
         }

         String newdata = String.format("BOARD %d %d %d %d %s", tilex, y, width, z, data);
         finaldata = finaldata + newdata;
         data = "";
      }

      return finaldata;
   }

   public int getRowWidth(int y) {
      if (this.tiles[y] == null) {
         return 0;
      } else {
         for(int i = 63; i >= 0; --i) {
            if (this.tiles[y][i] >= 0) {
               return i + 1;
            }
         }

         return 0;
      }
   }

   public int getRowX(int y) {
      if (this.tiles[y] == null) {
         return 0;
      } else {
         for(int i = 0; i < 64; ++i) {
            if (this.tiles[y][i] >= 0) {
               return i;
            }
         }

         return 0;
      }
   }
}
