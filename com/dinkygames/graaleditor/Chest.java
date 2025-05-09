package com.dinkygames.graaleditor;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Chest extends Entity {
   public int x;
   public int y;
   public int signindex;
   public float width = 2.0F;
   public float height = 2.0F;
   public String item;
   public String name = "Chest";
   private BufferedImage renderimage;

   public Chest(int x, int y, String item, int signindex) {
      this.x = x;
      this.y = y;
      this.item = item;
      this.signindex = signindex;

      try {
         this.renderimage = ImageIO.read(this.getClass().getResource("/res/images/chest.png"));
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
      return this.width;
   }

   protected float getheight() {
      return this.height;
   }

   protected boolean checkMouseinObject(int x, int y) {
      if (x >= this.x * 16 && !((float)x > ((float)this.x + this.width) * 16.0F)) {
         return y >= this.y * 16 && !((float)y > ((float)this.y + this.height) * 16.0F);
      } else {
         return false;
      }
   }

   public void renderOutline(Graphics2D g2d, BufferedImage outlineimage) {
      g2d.drawImage(outlineimage, this.x * 16 - 2, this.y * 16 - 2, (ImageObserver)null);
   }

   public BufferedImage createOutline() {
      BufferedImage outlineimage = new BufferedImage(36, 36, 2);
      Graphics2D g = outlineimage.createGraphics();
      g.fillRect(0, 0, 36, 36);
      g.dispose();
      return outlineimage;
   }

   public boolean render(Graphics2D g2d) {
      g2d.drawImage(this.renderimage, this.x * 16, this.y * 16, (ImageObserver)null);
      return true;
   }
}
