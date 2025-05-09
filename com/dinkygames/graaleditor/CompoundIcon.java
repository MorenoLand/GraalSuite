package com.dinkygames.graaleditor;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

public class CompoundIcon implements Icon {
   public static final float TOP = 0.0F;
   public static final float LEFT = 0.0F;
   public static final float CENTER = 0.5F;
   public static final float BOTTOM = 1.0F;
   public static final float RIGHT = 1.0F;
   private Icon[] icons;
   private CompoundIcon.Axis axis;
   private int gap;
   private float alignmentX;
   private float alignmentY;

   public CompoundIcon(Icon... icons) {
      this(CompoundIcon.Axis.X_AXIS, icons);
   }

   public CompoundIcon(CompoundIcon.Axis axis, Icon... icons) {
      this(axis, 0, icons);
   }

   public CompoundIcon(CompoundIcon.Axis axis, int gap, Icon... icons) {
      this(axis, gap, 0.5F, 0.5F, icons);
   }

   public CompoundIcon(CompoundIcon.Axis axis, int gap, float alignmentX, float alignmentY, Icon... icons) {
      this.alignmentX = 0.5F;
      this.alignmentY = 0.5F;
      this.axis = axis;
      this.gap = gap;
      this.alignmentX = alignmentX > 1.0F ? 1.0F : (alignmentX < 0.0F ? 0.0F : alignmentX);
      this.alignmentY = alignmentY > 1.0F ? 1.0F : (alignmentY < 0.0F ? 0.0F : alignmentY);

      for(int i = 0; i < icons.length; ++i) {
         if (icons[i] == null) {
            String message = "Icon (" + i + ") cannot be null";
            throw new IllegalArgumentException(message);
         }
      }

      this.icons = icons;
   }

   public CompoundIcon.Axis getAxis() {
      return this.axis;
   }

   public int getGap() {
      return this.gap;
   }

   public float getAlignmentX() {
      return this.alignmentX;
   }

   public float getAlignmentY() {
      return this.alignmentY;
   }

   public int getIconCount() {
      return this.icons.length;
   }

   public Icon getIcon(int index) {
      return this.icons[index];
   }

   public int getIconWidth() {
      int width = 0;
      Icon currentIcon;
      int var3;
      int var4;
      Icon[] var5;
      if (this.axis == CompoundIcon.Axis.X_AXIS) {
         width += (this.icons.length - 1) * this.gap;
         var4 = (var5 = this.icons).length;

         for(var3 = 0; var3 < var4; ++var3) {
            currentIcon = var5[var3];
            width += currentIcon.getIconWidth();
         }
      } else {
         var4 = (var5 = this.icons).length;

         for(var3 = 0; var3 < var4; ++var3) {
            currentIcon = var5[var3];
            width = Math.max(width, currentIcon.getIconWidth());
         }
      }

      return width;
   }

   public int getIconHeight() {
      int height = 0;
      Icon currentIcon;
      int var3;
      int var4;
      Icon[] var5;
      if (this.axis == CompoundIcon.Axis.Y_AXIS) {
         height += (this.icons.length - 1) * this.gap;
         var4 = (var5 = this.icons).length;

         for(var3 = 0; var3 < var4; ++var3) {
            currentIcon = var5[var3];
            height += currentIcon.getIconHeight();
         }
      } else {
         var4 = (var5 = this.icons).length;

         for(var3 = 0; var3 < var4; ++var3) {
            currentIcon = var5[var3];
            height = Math.max(height, currentIcon.getIconHeight());
         }
      }

      return height;
   }

   public void paintIcon(Component c, Graphics g, int x, int y) {
      int width;
      Icon currentIcon;
      int var7;
      int var8;
      Icon[] var9;
      int iconX;
      if (this.axis == CompoundIcon.Axis.X_AXIS) {
         width = this.getIconHeight();
         var8 = (var9 = this.icons).length;

         for(var7 = 0; var7 < var8; ++var7) {
            currentIcon = var9[var7];
            iconX = this.getOffset(width, currentIcon.getIconHeight(), this.alignmentY);
            currentIcon.paintIcon(c, g, x, y + iconX);
            x += currentIcon.getIconWidth() + this.gap;
         }
      } else if (this.axis == CompoundIcon.Axis.Y_AXIS) {
         width = this.getIconWidth();
         var8 = (var9 = this.icons).length;

         for(var7 = 0; var7 < var8; ++var7) {
            currentIcon = var9[var7];
            iconX = this.getOffset(width, currentIcon.getIconWidth(), this.alignmentX);
            currentIcon.paintIcon(c, g, x + iconX, y);
            y += currentIcon.getIconHeight() + this.gap;
         }
      } else {
         width = this.getIconWidth();
         int height = this.getIconHeight();
         Icon[] var16;
         int var15 = (var16 = this.icons).length;

         for(var8 = 0; var8 < var15; ++var8) {
            Icon currentLayerIcon = var16[var8];
            int layerIconX = this.getOffset(width, currentLayerIcon.getIconWidth(), this.alignmentX);
            int layerIconY = this.getOffset(height, currentLayerIcon.getIconHeight(), this.alignmentY);
            currentLayerIcon.paintIcon(c, g, x + layerIconX, y + layerIconY);
         }
      }

   }

   private int getOffset(int maxValue, int iconValue, float alignment) {
      float offset = (float)(maxValue - iconValue) * alignment;
      return Math.round(offset);
   }

   public static enum Axis {
      X_AXIS,
      Y_AXIS,
      Z_AXIS;
   }
}