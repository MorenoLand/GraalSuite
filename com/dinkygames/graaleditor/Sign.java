package com.dinkygames.graaleditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Sign {
   public int x;
   public int y;
   public String text;

   public Sign(String text, int x, int y) {
      this.text = text;
      this.x = x;
      this.y = y;
   }

   public void updateSign(String text, int x, int y) {
      this.text = text;
      this.x = x;
      this.y = y;
   }

   public void updateText(String text) {
      this.text = text;
   }

   public void updateX(int x) {
      this.x = x;
   }

   public void updateY(int y) {
      this.y = y;
   }

   public boolean render(Graphics2D g2d) {
      g2d.setStroke(new BasicStroke());
      g2d.setColor(new Color(255, 0, 0, 75));
      g2d.fillRect(this.x * 16, this.y * 16, 32, 16);
      g2d.setColor(Color.BLACK);
      g2d.drawRect(this.x * 16 - 1, this.y * 16 - 1, 33, 17);
      g2d.setColor(Color.RED);
      float[] dash = new float[]{1.0F, 4.0F};
      g2d.setStroke(new BasicStroke(2.0F, 2, 0, 10.0F, dash, 0.0F));
      g2d.drawRect(this.x * 16 - 1, this.y * 16 - 1, 34, 18);
      return true;
   }
}
