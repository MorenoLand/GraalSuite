package com.dinkygames.graaleditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Link {
   public int x;
   public int y;
   public int width;
   public int height;
   public String dest;
   public String destx;
   public String desty;

   public Link(String dest, int x, int y, int w, int h, String destx, String desty) {
      this.dest = dest;
      this.x = x;
      this.y = y;
      this.width = w;
      this.height = h;
      this.destx = destx;
      this.desty = desty;
   }

   public boolean render(Graphics2D g2d) {
      g2d.setStroke(new BasicStroke());
      g2d.setColor(new Color(255, 255, 0, 75));
      g2d.fillRect(this.x * 16, this.y * 16, this.width * 16, this.height * 16);
      g2d.setColor(Color.BLACK);
      g2d.drawRect(this.x * 16 - 1, this.y * 16 - 1, this.width * 16 + 1, this.height * 16 + 1);
      g2d.setColor(Color.YELLOW);
      float[] dash = new float[]{1.0F, 4.0F};
      g2d.setStroke(new BasicStroke(2.0F, 2, 0, 10.0F, dash, 0.0F));
      g2d.drawRect(this.x * 16 - 1, this.y * 16 - 1, this.width * 16 + 2, this.height * 16 + 2);
      return true;
   }

   public Object[] getLinkAsArray() {
      return new Object[]{this.dest, this.x, this.y, this.width, this.height, this.destx, this.desty};
   }
}
