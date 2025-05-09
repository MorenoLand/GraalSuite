package com.dinkygames.graaleditor;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Entity implements Cloneable {
   String image;
   String name = "Entity";
   public int x;
   public int y;
   public int layer = 1;
   float width;
   float height;
   private BufferedImage renderimage;

   public BufferedImage createOutline() {
      return new BufferedImage(32, 32, 2);
   }

   protected static BufferedImage dye(BufferedImage image, Color color) {
      int w = image.getWidth();
      int h = image.getHeight();
      BufferedImage dyed = new BufferedImage(w, h, 2);
      Graphics2D g = dyed.createGraphics();
      g.drawImage(image, 0, 0, (ImageObserver)null);
      g.setComposite(AlphaComposite.SrcAtop);
      g.setColor(color);
      g.fillRect(0, 0, w, h);
      g.dispose();
      return dyed;
   }

   public String getName() {
      return this.name;
   }

   protected String getimage() {
      return this.image;
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

   protected int getLayer() {
      return this.layer;
   }

   protected float getwidth() {
      return this.width;
   }

   protected float getheight() {
      return this.height;
   }

   public short canResize(int x, int y) {
      return 0;
   }

   public void resize(short direction, int x, int y) {
   }

   protected boolean checkMouseinObject(int x, int y) {
      return false;
   }

   public void changeUID() {
   }

   public Entity cloneObject(Entity entity) throws CloneNotSupportedException {
      Entity clone = (Entity)entity.clone();
      return clone;
   }

   public void renderOutline(Graphics2D g2d, BufferedImage outlineimage) {
   }

   public boolean render(Graphics2D g2d) {
      return false;
   }
}
