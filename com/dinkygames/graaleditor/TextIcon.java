package com.dinkygames.graaleditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JComponent;

public class TextIcon implements Icon, PropertyChangeListener {
   private JComponent component;
   private TextIcon.Layout layout;
   private String text;
   private Font font;
   private Color foreground;
   private int padding;
   private int iconWidth;
   private int iconHeight;
   private String[] strings;
   private int[] stringWidths;

   public TextIcon(JComponent component, String text) {
      this(component, text, TextIcon.Layout.HORIZONTAL);
   }

   public TextIcon(JComponent component, String text, TextIcon.Layout layout) {
      this.padding = 4;
      this.component = component;
      this.layout = layout;
      this.setText(text);
      component.addPropertyChangeListener("font", this);
   }

   public TextIcon.Layout getLayout() {
      return this.layout;
   }

   public String getText() {
      return this.text;
   }

   public void setText(String text) {
      this.text = text;
      this.calculateIconDimensions();
   }

   public Font getFont() {
      return this.font == null ? this.component.getFont() : this.font;
   }

   public void setFont(Font font) {
      this.font = font;
      this.calculateIconDimensions();
   }

   public Color getForeground() {
      return this.foreground == null ? this.component.getForeground() : this.foreground;
   }

   public void setForeground(Color foreground) {
      this.foreground = foreground;
      this.component.repaint();
   }

   public int getPadding() {
      return this.padding;
   }

   public void setPadding(int padding) {
      this.padding = padding;
      this.calculateIconDimensions();
   }

   private void calculateIconDimensions() {
      Font font = this.getFont();
      FontMetrics fm = this.component.getFontMetrics(font);
      this.iconHeight = fm.stringWidth(this.text) + this.padding * 2;
      this.iconWidth = fm.getHeight();
      this.component.revalidate();
   }

   public int getIconWidth() {
      return this.iconWidth;
   }

   public int getIconHeight() {
      return this.iconHeight;
   }

   public void paintIcon(Component c, Graphics g, int x, int y) {
      Graphics2D g2 = (Graphics2D)g.create();
      Toolkit toolkit = Toolkit.getDefaultToolkit();
      Map map = (Map)toolkit.getDesktopProperty("awt.font.desktophints");
      if (map != null) {
         g2.addRenderingHints(map);
      } else {
         g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      }

      g2.setFont(this.getFont());
      g2.setColor(this.getForeground());
      FontMetrics fm = g2.getFontMetrics();
      if (this.layout == TextIcon.Layout.HORIZONTAL) {
         g2.translate(x, y + fm.getAscent());
         g2.drawString(this.text, this.padding, 0);
      } else if (this.layout == TextIcon.Layout.VERTICAL) {
         g2.translate(x, y);
         g2.rotate(1.5707963267948966D);
         g2.drawString(this.text, this.padding, 0);
      }

      g2.dispose();
   }

   public void propertyChange(PropertyChangeEvent e) {
      if (this.font == null) {
         this.calculateIconDimensions();
      }

   }

   public static enum Layout {
      HORIZONTAL,
      VERTICAL;
   }
}
