package com.dinkygames.graaleditor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;

public class Baddy extends Entity implements Serializable {
   public int x;
   public int y;
   public int w;
   public int h;
   public int width;
   public int height;
   short type;
   public String[] dialog;
   String name = "Baddy";
   private transient BufferedImage renderimage;
   private int[] spriteinfo;
   private String[] baddynames = new String[]{"Gray Soldier", "Blue Soldier", "Red Soldier", "Shooting Soldier", "Swamp Soldier", "Frog", "Spider", "Golden Warrior", "Lizardon", "Dragon"};
   private int[][] spritecoords = new int[][]{{0, 0, 44, 64}, {44, 0, 44, 64}, {88, 0, 44, 64}, {132, 0, 44, 50}, {0, 68, 52, 58}, {52, 64, 24, 26}, {52, 92, 32, 34}, {84, 64, 44, 64}, {132, 50, 44, 64}, {132, 115, 44, 56}};
   private int[][] offsets = new int[][]{{-8, -16}, {-8, -16}, {-8, -16}, {-6, -16}, {-20, -16}, {4, 4}, new int[2], {-8, -16}, {-8, -16}, {-8, -18}};

   public Baddy(int x, int y, short type, String[] dialog) {
      this.x = x;
      this.y = y;
      this.type = type;
      this.dialog = dialog;
      this.name = this.baddynames[type];
      this.spriteinfo = this.spritecoords[this.type];
      this.w = this.spriteinfo[2];
      this.h = this.spriteinfo[3];

      for(int i = 0; i < 3; ++i) {
         if (this.dialog[i] == null) {
            this.dialog[i] = "";
         }
      }

      try {
         this.renderimage = ImageIO.read(this.getClass().getResource("/res/images/opps.png"));
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public String getName() {
      return this.name;
   }

   protected int getx() {
      return this.x;
   }

   protected int gety() {
      return this.y;
   }

   protected void move(int x, int y) {
      this.x = x;
      this.y = y;
   }

   protected void setx(int x) {
      this.x = x;
   }

   protected void sety(int y) {
      this.y = y;
   }

   protected float getwidth() {
      return (float)this.w / 16.0F;
   }

   protected float getheight() {
      return (float)this.h / 16.0F;
   }

   protected boolean checkMouseInImage(int x, int y) {
      return true;
   }

   public BufferedImage createOutline() {
      BufferedImage blackimage = dye(this.renderimage, Color.WHITE);
      BufferedImage outlineimage = new BufferedImage(this.spriteinfo[0] + this.w + 4, this.spriteinfo[1] + this.h + 4, 2);
      Graphics2D g = outlineimage.createGraphics();

      for(int i = 0; i <= 8; ++i) {
         int[][] deltas = new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, new int[2], {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
         int dx = 2 + deltas[i][0] * 2;
         int dy = 2 + deltas[i][1] * 2;
         g.drawImage(blackimage, dx, dy, dx + this.w, dy + this.h, this.spriteinfo[0], this.spriteinfo[1], this.spriteinfo[0] + this.w, this.spriteinfo[1] + this.h, (ImageObserver)null);
      }

      g.dispose();
      return outlineimage;
   }

   public boolean checkMouseinObject(int x, int y) {
      int baddyx = this.x * 16 + this.offsets[this.type][0];
      int baddyy = this.y * 16 + this.offsets[this.type][1];
      if (x >= baddyx && x <= baddyx + this.w) {
         if (y >= baddyy && y <= baddyy + this.h) {
            int ox = x - baddyx;
            int oy = y - baddyy;
            if (ox <= this.spriteinfo[2] && oy <= this.spriteinfo[3]) {
               int cx = Math.max(0, Math.min(this.renderimage.getWidth() - 1, this.spriteinfo[0] + ox));
               int cy = Math.max(0, Math.min(this.renderimage.getHeight() - 1, this.spriteinfo[1] + oy));
               int pixel = this.renderimage.getRGB(cx, cy);
               int alpha = pixel >> 24 & 255;
               return alpha >= 1;
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean render(Graphics2D g2d) {
      int dx = this.x * 16 + this.offsets[this.type][0];
      int dy = this.y * 16 + this.offsets[this.type][1];
      g2d.drawImage(this.renderimage, dx, dy, dx + this.w, dy + this.h, this.spriteinfo[0], this.spriteinfo[1], this.spriteinfo[0] + this.w, this.spriteinfo[1] + this.h, (ImageObserver)null);
      return true;
   }

   public void renderOutline(Graphics2D g2d, BufferedImage outlineimage) {
      int dx = this.x * 16 + this.offsets[this.type][0];
      int dy = this.y * 16 + this.offsets[this.type][1];
      g2d.drawImage(outlineimage, dx - 2, dy - 2, (ImageObserver)null);
   }
}
