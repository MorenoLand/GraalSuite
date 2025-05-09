package com.dinkygames.graaleditor;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TileSelection extends Entity {
   public String name = "Tile Selection";
   public int x;
   public int y;
   public int ox;
   public int oy;
   public int oox;
   public int ooy;
   private int resizeoffsetx = 0;
   private int resizeoffsety = 0;
   public short resizing = 0;
   public float width = 2.0F;
   public float height = 2.0F;
   public float owidth;
   public float oheight;
   short[][] tiles;
   short[][] originaltiles;
   public LevelCanvas canvas;
   private BufferedImage tileset;
   public boolean clearonmove = false;
   private GraalEditor main;

   public TileSelection(short[][] tiles, LevelCanvas canvas, boolean clearonmove) {
      this.x = this.ox = -1000;
      this.y = this.ox = -1000;
      this.tiles = this.originaltiles = tiles;
      this.width = this.owidth = (float)tiles[0].length;
      this.height = this.oheight = (float)tiles.length;
      this.canvas = canvas;
      this.clearonmove = clearonmove;
      this.main = canvas.scroll.tilesetpane.mainwindow;

      try {
         this.tileset = ImageIO.read(this.getClass().getResource("/res/images/pics1.png"));
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public TileSelection(short[][] tiles, LevelCanvas canvas) {
      this.x = this.ox = -1000;
      this.y = this.ox = -1000;
      this.tiles = tiles;
      this.width = (float)tiles[0].length;
      this.height = (float)tiles.length;
      this.canvas = canvas;
      this.clearonmove = false;

      try {
         this.tileset = ImageIO.read(this.getClass().getResource("/res/images/pics1.png"));
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   protected boolean checkMouseinObject(int x, int y) {
      if (x >= this.x * 16 - 3 && !((float)x > ((float)this.x + this.width) * 16.0F + 3.0F)) {
         return y >= this.y * 16 - 3 && !((float)y > ((float)this.y + this.height) * 16.0F + 3.0F);
      } else {
         return false;
      }
   }

   public BufferedImage createOutline() {
      BufferedImage outlineimage = new BufferedImage((int)this.width * 16 + 10, (int)this.height * 16 + 10, 2);
      Graphics2D g = outlineimage.createGraphics();
      g.fillRect(3, 3, outlineimage.getWidth() - 6, outlineimage.getHeight() - 6);
      g.clearRect(5, 5, outlineimage.getWidth() - 10, outlineimage.getHeight() - 10);
      if (!this.canvas.lmbHeld && (this.owidth > 1.0F || this.oheight > 1.0F)) {
         g.fillRect(1, 1, 6, 6);
         g.fillRect(1 + outlineimage.getWidth() / 2 - 3, 1, 6, 6);
         g.fillRect(1, 1 + outlineimage.getHeight() / 2 - 3, 6, 6);
         g.fillRect(outlineimage.getWidth() - 6 - 1, 1, 6, 6);
         g.fillRect(1, outlineimage.getHeight() - 6 - 1, 6, 6);
         g.fillRect(1 + outlineimage.getWidth() / 2 - 3, outlineimage.getHeight() - 6 - 1, 6, 6);
         g.fillRect(outlineimage.getWidth() - 6 - 1, outlineimage.getHeight() - 6 - 1, 6, 6);
         g.fillRect(outlineimage.getWidth() - 6 - 1, 1 + outlineimage.getHeight() / 2 - 3, 6, 6);
      }

      g.dispose();
      return outlineimage;
   }

   public boolean render(Graphics2D g2d) {
      for(int y = 0; y < this.tiles.length; ++y) {
         for(int x = 0; x < this.tiles[y].length; ++x) {
            if (this.canvas.tileset == null) {
               return false;
            }

            int tile = this.tiles[y][x];
            Point tilecoord = TileFunctions.tileToPoint(tile);
            int tx = (int)tilecoord.getX();
            int ty = (int)tilecoord.getY();
            int dx = (this.x + x) * 16;
            int dy = (this.y + y) * 16;
            g2d.drawImage(this.canvas.tileset, dx, dy, dx + 16, dy + 16, tx, ty, tx + 16, ty + 16, (ImageObserver)null);
         }
      }

      return true;
   }

   public void clearTiles(Level level, boolean hardDelete) {
      this.canvas.addUndoState_Tiles(0);
      short tile = hardDelete ? -1 : this.main.defaultTile;

      for(int y = 0; y < this.tiles.length; ++y) {
         for(int x = 0; x < this.tiles[y].length; ++x) {
            int var10001 = this.y + y;
            level.getCurrentLayer().tiles[var10001][this.x + x] = tile;
         }
      }

      ((Layer)this.canvas.level.layers.get(this.canvas.scroll.selectedLayer)).updateTileRender();
   }

   public void placeTiles(Level level) {
      this.canvas.addUndoState_Tiles(0);

      for(int y = 0; y < this.tiles.length; ++y) {
         for(int x = 0; x < this.tiles[y].length; ++x) {
            int putx = this.y + y;
            int puty = this.x + x;
            if (putx >= 0 && puty >= 0 && putx <= 63 && puty <= 63 && this.tiles[y][x] >= 0) {
               int var10001 = this.y + y;
               level.getCurrentLayer().tiles[var10001][this.x + x] = this.tiles[y][x];
            }
         }
      }

      ((Layer)this.canvas.level.layers.get(this.canvas.scroll.selectedLayer)).updateTileRender();
   }

   public short canResize(int x, int y) {
      if (this.owidth < 2.0F && this.oheight < 2.0F) {
         return 0;
      } else {
         int mx = x - this.x * 16;
         int my = y - this.y * 16;
         if (mx < 3 && my < 3) {
            return 1;
         } else if (mx < 3 && (float)my > this.height * 16.0F / 2.0F - 3.0F && (float)my < this.height * 16.0F / 2.0F + 3.0F) {
            return 2;
         } else if (mx < 3 && (float)my > this.height * 16.0F - 3.0F) {
            return 3;
         } else if ((float)mx > this.width * 16.0F / 2.0F - 3.0F && (float)mx < this.width * 16.0F / 2.0F + 3.0F && (float)my > this.height * 16.0F - 3.0F) {
            return 4;
         } else if ((float)mx > this.width * 16.0F - 3.0F && (float)my > this.height * 16.0F - 3.0F) {
            return 5;
         } else if ((float)mx > this.width * 16.0F - 3.0F && (float)my > this.height * 16.0F / 2.0F - 3.0F && (float)my < this.height * 16.0F / 2.0F + 3.0F) {
            return 6;
         } else if ((float)mx > this.width * 16.0F - 3.0F && my < 3) {
            return 7;
         } else {
            return (short)((float)mx > this.width * 16.0F / 2.0F - 3.0F && (float)mx < this.width * 16.0F / 2.0F + 3.0F && my < 3 ? 8 : 0);
         }
      }
   }

   public void resize(short direction, int mx, int my) {
      this.resizing = direction;
      mx = Math.min(Math.max(mx + 8, 0), 1024) / 16;
      my = Math.min(Math.max(my + 8, 0), 1024) / 16;
      if (direction == 1) {
         if ((float)mx < (float)this.x + this.width) {
            this.x = Math.max(0, mx);
            this.width = Math.max(1.0F, (float)this.ox + this.owidth - (float)mx);
         }

         if ((float)my < (float)this.y + this.height) {
            this.y = Math.max(0, my);
            this.height = Math.max(1.0F, (float)this.oy + this.oheight - (float)my);
         }
      } else if (direction == 2 && (float)mx < (float)this.x + this.width) {
         this.x = Math.max(0, mx);
         this.width = Math.max(1.0F, (float)this.ox + this.owidth - (float)mx);
      } else if (direction == 3 && (float)mx < (float)this.x + this.width) {
         this.x = Math.max(0, mx);
         this.width = Math.max(1.0F, (float)this.ox + this.owidth - (float)mx);
         this.height = (float)Math.max(1, my - this.oy);
      } else if (direction == 4) {
         this.height = (float)Math.max(1, my - this.oy);
      } else if (direction == 5) {
         this.width = (float)Math.max(1, mx - this.ox);
         this.height = (float)Math.max(1, my - this.oy);
      } else if (direction == 6) {
         this.width = (float)Math.max(1, mx - this.ox);
      } else if (direction == 7) {
         this.width = (float)Math.max(1, mx - this.ox);
         if ((float)my < (float)this.y + this.height) {
            this.y = Math.max(0, my);
            this.height = Math.max(1.0F, (float)this.oy + this.oheight - (float)my);
         }
      } else if (direction == 8 && (float)my < (float)this.y + this.height) {
         this.y = Math.max(0, my);
         this.height = Math.max(1.0F, (float)this.oy + this.oheight - (float)my);
      }

      this.tiles = new short[(int)this.height][(int)this.width];
      int offsetx = 0;
      int offsety = 0;
      if (this.x < this.ox) {
         offsetx = Math.abs(this.x - this.ox) % (int)this.owidth;
      }

      if (this.y < this.oy) {
         offsety = Math.abs(this.y - this.oy) % (int)this.oheight;
      }

      for(int y = 0; y < this.tiles.length; ++y) {
         for(int x = 0; x < this.tiles[y].length; ++x) {
            this.tiles[y][x] = this.originaltiles[this.mod(y + offsety, this.originaltiles.length)][this.mod(x + offsetx, this.originaltiles[0].length)];
         }
      }

   }

   private int mod(int num, int whole) {
      int r = num % whole;
      if (r < 0) {
         r += whole;
      }

      return r;
   }

   protected int getx() {
      return this.x;
   }

   protected int gety() {
      return this.y;
   }

   protected void setx(int x) {
      this.x = this.ox = x;
   }

   protected void sety(int y) {
      this.y = this.oy = y;
      if (y > -1000) {
         this.ooy = y;
      }

   }

   protected void clearResizing() {
      for(int y = 0; y < this.originaltiles.length; ++y) {
         for(int x = 0; x < this.originaltiles[0].length; ++x) {
            this.originaltiles[y][x] = this.tiles[y][x];
         }
      }

      this.ox = this.x;
      this.oy = this.y;
      this.owidth = this.width;
      this.oheight = this.height;
      this.resizing = 0;
   }

   protected void move(int x, int y) {
      if (this.resizing <= 0) {
         if (this.clearonmove) {
            this.clearTiles(this.canvas.level, this.canvas.scroll.selectedLayer > 0);
         }

         this.clearonmove = false;
         this.x = x;
         this.y = y;
         this.owidth = this.width;
         this.oheight = this.height;
         if (x > -1000) {
            this.oox = x;
         }

         if (y > -1000) {
            this.ooy = y;
         }

      }
   }

   protected float getwidth() {
      return this.width;
   }

   protected float getheight() {
      return this.height;
   }

   public void renderOutline(Graphics2D g2d, BufferedImage outlineimage) {
      g2d.drawImage(outlineimage, this.x * 16 - 2 - 3, this.y * 16 - 2 - 3, (ImageObserver)null);
   }
}
