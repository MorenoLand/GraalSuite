package com.dinkygames.graaleditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class NPC extends Entity {
   private BufferedImage renderimage;
   public int x;
   public int y;
   public int partx;
   public int party;
   public int partw = 0;
   public int parth = 0;
   public int ap = 50;
   public int layer = 1;
   public float width;
   public float height;
   public float zoom = 1.0F;
   public String image;
   public String script;
   public String name = "NPC";
   public String ani = "idle";
   public String body = "body.png";
   public String shield = "shield1.png";
   public String sword = "sword1.png";
   public String horse = "ride.png";
   public String nick = "unknown";
   public String guild;
   public String chat;
   public String[] colors = new String[5];
   public boolean character = false;
   public boolean setimgpart = false;
   public boolean imagefound = false;
   public boolean setshape = false;
   private boolean nullimage = false;
   byte dir = 2;
   byte swordpower;
   byte shieldpower;
   Level level;
   short[] imagepart;
   String[] attrs = new String[30];

   public void cleanup() {
   }

   protected String getimage() {
      return this.image;
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

   protected int getLayer() {
      return this.layer;
   }

   protected void move(int x, int y) {
      int lasty = this.y;
      this.x = x;
      this.y = y;
      if (lasty != this.y) {
         this.level.sortNPCs();
      }

   }

   protected void setx(int x) {
      this.x = x;
   }

   protected void sety(int y) {
      this.y = y;
      this.level.sortNPCs();
   }

   protected float getwidth() {
      return this.width;
   }

   protected float getheight() {
      return this.height;
   }

   public NPC(Level level) {
      this.image = null;
      this.x = -64;
      this.y = -64;
      this.script = "";
      this.level = level;
      this.update();
      this.createOutline();
   }

   public NPC(String image, int x, int y, String script, Level level) {
      if (image != null) {
         this.image = image.trim();
      }

      this.x = x;
      this.y = y;
      this.script = script;
      this.level = level;
      this.update();
   }

   public void addScript(String script) {
      this.script = script;
   }

   private void parseScript() {
      if (this.level.scroll.main.options.scriptparsing) {
         BufferedReader br = new BufferedReader(new StringReader(this.script));
         int parenthesiscount = 0;
         this.setimgpart = false;
         ScriptEngineManager mgr = new ScriptEngineManager();
         ScriptEngine engine = mgr.getEngineByName("JavaScript");

         try {
            boolean readFirstLine = false;

            while(true) {
               String line;
               label63:
               while((line = br.readLine()) != null) {
                  line = line.trim().toLowerCase();
                  if (line.equals("//#clientside")) {
                     readFirstLine = false;
                  } else {
                     if (line.length() > 0 && !readFirstLine) {
                        this.parseLine(line, engine);
                        readFirstLine = true;
                     }

                     String checkline = line.trim();
                     checkline = checkline.replace(" ", "");
                     if (checkline.startsWith("functiononcreated(){") || checkline.startsWith("if(created") || checkline.startsWith("functiononplayerenters(){") || checkline.startsWith("if(playerenters")) {
                        ++parenthesiscount;

                        while(true) {
                           String scanline;
                           while(true) {
                              if ((scanline = br.readLine()) == null) {
                                 continue label63;
                              }

                              if (parenthesiscount != 1) {
                                 break;
                              }

                              if (!scanline.trim().startsWith("//")) {
                                 this.parseLine(scanline, engine);
                                 break;
                              }
                           }

                           if (scanline.trim().equals("}")) {
                              --parenthesiscount;
                              if (parenthesiscount <= 0) {
                                 break;
                              }
                           }
                        }
                     }
                  }
               }

               return;
            }
         } catch (IOException var9) {
            var9.printStackTrace();
         } catch (NumberFormatException var10) {
            var10.printStackTrace();
         } catch (ScriptException var11) {
            var11.printStackTrace();
         }

      }
   }

   private void parseLine(String scanline, ScriptEngine engine) throws NumberFormatException, ScriptException {
      scanline = scanline.trim();
      if (scanline.startsWith("this.")) {
         scanline = scanline.substring(5);
      }

      scanline = scanline.replace(",", " ");
      scanline = scanline.replace("\"", "");
      scanline = scanline.trim();
      if (scanline.contains(";")) {
         scanline = scanline.substring(0, scanline.indexOf(";")).trim();
      }

      if (scanline.endsWith(")")) {
         scanline = scanline.replaceFirst("\\(", " ");
         scanline = scanline.substring(0, scanline.length() - 1);
      }

      String[] parts = scanline.toLowerCase().split("\\s+");
      scanline = scanline.toLowerCase();
      if (scanline.startsWith("setimg ")) {
         if (parts.length <= 2) {
            this.image = parts[1];
         } else {
            System.out.println("Invalid setimg: " + scanline + " " + parts.length);
         }
      } else if (scanline.startsWith("setimgpart")) {
         if (parts.length <= 6) {
            this.image = parts[1];
            this.partx = (int)this.parseFloat(engine, parts[2]);
            this.party = (int)this.parseFloat(engine, parts[3]);
            this.partw = (int)this.parseFloat(engine, parts[4]);
            this.parth = (int)this.parseFloat(engine, parts[5]);
            this.setimgpart = true;
         } else {
            System.out.println("Invalid setimgpart: " + scanline + " " + parts.length);
         }
      } else if (scanline.startsWith("setshape ")) {
         if (parts.length <= 4) {
            this.width = (float)((int)this.parseFloat(engine, parts[2]) / 16);
            this.height = (float)((int)this.parseFloat(engine, parts[3]) / 16);
            this.setshape = true;
         } else {
            System.out.println("Invalid setshape: " + scanline + " " + parts.length);
         }
      } else if (parts.length == 3 && parts[1].equals("=") && parts[0].equals("zoom")) {
         this.zoom = Math.max(0.1F, this.parseFloat(engine, parts[2]));
      }

   }

   public int parseInt(ScriptEngine engine, String s) throws NumberFormatException, ScriptException {
      String allowedchars = "1234567890()*/+- ";
      return !s.matches("[1234567890\\(\\)\\*/\\+\\- \\.]+") ? 0 : Integer.parseInt(engine.eval(s).toString());
   }

   public float parseFloat(ScriptEngine engine, String s) throws NumberFormatException, ScriptException {
      String allowedchars = "1234567890()*/+- ";
      return !s.matches("[1234567890\\(\\)\\*/\\+\\- \\.]+") ? 0.0F : Float.parseFloat(engine.eval(s).toString());
   }

   public void update() {
      this.update((Object)null);
   }

   public void update(Object from) {
      this.parseScript();
      this.nullimage = "".equals(this.image) || this.image == null || this.image.endsWith(".mng");

      try {
         String var10000;
         if (this.nullimage) {
            var10000 = null;
         } else {
            var10000 = (String)FileCache.cache.get(this.image);
         }

         String filedir = (String)FileCache.cache.get(this.image);
         if (filedir != null && this.image != null && !this.image.endsWith(".mng")) {
            File f = new File(filedir);
            this.renderimage = ImageIO.read(f);
            this.imagefound = true;
         } else {
            this.renderimage = ImageIO.read(this.getClass().getResource("/res/images/blanknpc.png"));
            this.imagefound = false;
         }
      } catch (IOException var4) {
         this.imagefound = false;
         var4.printStackTrace();
      }

      if (!this.setimgpart) {
         this.partw = this.renderimage.getWidth();
         this.parth = this.renderimage.getHeight();
      }

      if (!this.setshape) {
         this.width = (float)this.partw / 16.0F;
         this.height = (float)this.parth / 16.0F;
      } else {
         this.width = Math.max(1.0F, this.width);
         this.height = Math.max(1.0F, this.height);
      }

      if (!this.nullimage) {
         this.width *= this.zoom;
         this.height *= this.zoom;
      }

      this.nullimage = "".equals(this.image) || this.image == null || !this.imagefound;
      if (from != null) {
         this.level.scroll.canvas.repaint();
      }

   }

   public boolean render(Graphics2D g2d) {
      return this.render(g2d, false);
   }

   public boolean render(Graphics2D g2d, boolean screenshotting) {
      float dz = this.nullimage ? 1.0F : this.zoom;
      int dw = (int)((float)this.partw * dz);
      int dh = (int)((float)this.parth * dz);
      if (screenshotting) {
         if (!this.nullimage) {
            g2d.drawImage(this.renderimage, this.x * 16, this.y * 16, this.x * 16 + dw, this.y * 16 + dh, this.partx, this.party, this.partw + this.partx, this.parth + this.party, (ImageObserver)null);
         }

         return true;
      } else {
         if (this.setshape && this.level.scroll.tilesetpane.mainwindow.options.visualsetshape && this.nullimage) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2.0F));
            g2d.drawRoundRect(this.x * 16 + 2, this.y * 16 + 2, (int)this.width * 16 - 4, (int)this.height * 16 - 4, 4, 4);
            g2d.setStroke(new BasicStroke());
            GradientPaint gp4 = new GradientPaint(0.0F, 0.0F, new Color(255, 255, 0, 75), 1.0F, 1.0F, new Color(255, 255, 255, 150), true);
            g2d.setPaint(gp4);
            g2d.fillRoundRect(this.x * 16 + 3, this.y * 16 + 3, (int)this.width * 16 - 6, (int)this.height * 16 - 6, 4, 4);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2.0F));
            g2d.drawRoundRect(this.x * 16, this.y * 16, (int)this.width * 16 - 4, (int)this.height * 16 - 4, 4, 4);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            if (this.image.equals((Object)null) || "".equals(this.image) || !this.imagefound) {
               return false;
            }
         }

         g2d.drawImage(this.renderimage, this.x * 16, this.y * 16, this.x * 16 + dw, this.y * 16 + dh, this.partx, this.party, this.partw + this.partx, this.parth + this.party, (ImageObserver)null);
         return true;
      }
   }

   public BufferedImage createOutline() {
      float dz = this.nullimage ? 1.0F : this.zoom;
      int dw = (int)((float)this.partw * dz);
      int dh = (int)((float)this.parth * dz);
      BufferedImage blackimage;
      if (this.setshape && this.level.scroll.tilesetpane.mainwindow.options.visualsetshape && this.nullimage) {
         blackimage = new BufferedImage((int)this.width * 16 + 4, (int)this.height * 16 + 4, 2);
         Graphics2D g = blackimage.createGraphics();
         g.fillRect(0, 0, blackimage.getWidth(), blackimage.getHeight());
         g.dispose();
         return blackimage;
      } else {
         blackimage = dye(this.renderimage, Color.WHITE);
         BufferedImage outlineimage = new BufferedImage(dw + this.partx + 4, dh + this.party + 4, 2);
         Graphics2D g = outlineimage.createGraphics();

         for(int i = 0; i <= 8; ++i) {
            int[][] deltas = new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, new int[2], {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
            int dx = 2 + deltas[i][0] * 2;
            int dy = 2 + deltas[i][1] * 2;
            g.drawImage(blackimage, dx, dy, dx + dw, dy + dh, this.partx, this.party, this.partw + this.partx, this.parth + this.party, (ImageObserver)null);
         }

         g.dispose();
         return outlineimage;
      }
   }

   public void renderOutline(Graphics2D g2d, BufferedImage outlineimage) {
      g2d.drawImage(outlineimage, this.x * 16 - 2, this.y * 16 - 2, (ImageObserver)null);
   }
   
   public boolean checkMouseinObject(int x, int y) {
      float dz = this.nullimage ? 1.0F : this.zoom;
      int dw;
      int dh;
      if (this.setshape) {
         dw = (int)this.width * 16;
         dh = (int)this.height * 16;
      } else {
         dw = (int)((float)this.partw * dz);
         dh = (int)((float)this.parth * dz);
      }

      if (x >= this.x * 16 && x <= this.x * 16 + dw) {
         if (y >= this.y * 16 && y <= this.y * 16 + dh) {
            if (!this.nullimage && this.imagefound && !this.setshape) {
               int ox = x - this.x * 16;
               int oy = y - this.y * 16;
               ox = (int)((float)ox / dz);
               oy = (int)((float)oy / dz);
               if (this.partx + ox <= this.renderimage.getWidth() && this.party + oy <= this.renderimage.getHeight()) {
                  int cx = Math.max(0, Math.min(this.renderimage.getWidth() - 1, this.partx + ox));
                  int cy = Math.max(0, Math.min(this.renderimage.getHeight() - 1, this.party + oy));
                  int pixel = this.renderimage.getRGB(cx, cy);
                  int alpha = pixel >> 24 & 255;
                  return alpha >= 25;
               } else {
                  return false;
               }
            } else {
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   int parseInt(String s) {
      try {
         return Integer.parseInt(s);
      } catch (NumberFormatException var3) {
         return 0;
      }
   }
}
