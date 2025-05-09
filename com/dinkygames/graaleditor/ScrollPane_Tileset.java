package com.dinkygames.graaleditor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JScrollPane;

public class ScrollPane_Tileset extends JScrollPane {
   TilesetPreviewIcon tilesetico;
   public BufferedImage tileset;
   transient GraalEditor mainwindow;
   private Canvas_Tileset canvas;

   public ScrollPane_Tileset(GraalEditor parent) {
      this.mainwindow = parent;
      this.ScrollPane_Tileset();
   }

   private void ScrollPane_Tileset() {
      this.tilesetico = new TilesetPreviewIcon(this.mainwindow);
      
      this.tileset = new BufferedImage(2048, 512, BufferedImage.TYPE_INT_ARGB);
      try {
         InputStream imageStream = null;
         imageStream = this.getClass().getResourceAsStream("/res/images/pics1.png");
         
         if (imageStream == null) {
            imageStream = this.getClass().getClassLoader().getResourceAsStream("res/images/pics1.png");
         }
         
         if (imageStream == null) {
            File resFile = new File("./res/images/pics1.png");
            if (resFile.exists()) {
               imageStream = new FileInputStream(resFile);
            }
         }
         
         if (imageStream != null) {
            BufferedImage loadedImage = ImageIO.read(imageStream);
            if (loadedImage != null) {
               this.tileset = loadedImage;
            }
            imageStream.close();
         } else {
            System.out.println("Warning: Could not find tileset resource");
         }
      } catch (Exception e) {
         System.out.println("Error loading tileset: " + e.getMessage());
      }
      
      this.updateTileset(false);
      this.getVerticalScrollBar().setUnitIncrement(16);
      this.setCorner("LOWER_RIGHT_CORNER", this.tilesetico);
      this.setVerticalScrollBarPolicy(22);
      this.canvas = new Canvas_Tileset("/res/images/pics1.png", this);
      this.canvas.setPreferredSize(new Dimension(2048, 512));
      this.setViewportView(this.canvas);
      this.setWheelScrollingEnabled(false);
      this.addMouseWheelListener(new MouseAdapter() {
         public void mouseWheelMoved(MouseWheelEvent evt) {
            int iScrollAmount = evt.getScrollAmount();
            int iNewValue = ScrollPane_Tileset.this.horizontalScrollBar.getValue() + ScrollPane_Tileset.this.horizontalScrollBar.getBlockIncrement() * iScrollAmount * evt.getWheelRotation();
            if (evt.getWheelRotation() >= 1) {
               ScrollPane_Tileset.this.horizontalScrollBar.setValue(Math.min(ScrollPane_Tileset.this.horizontalScrollBar.getMaximum(), iNewValue));
            } else if (evt.getWheelRotation() <= -1) {
               ScrollPane_Tileset.this.horizontalScrollBar.setValue(Math.max(0, iNewValue));
            }
         }
      });
   }

   public void updateTileset(boolean loadtiledefs) {
      Graphics2D g2d = null;

      try {
         this.tileset = new BufferedImage(2048, 512, BufferedImage.TYPE_INT_ARGB);
         InputStream imageStream = null;
         imageStream = this.getClass().getResourceAsStream("/res/images/pics1.png");
         
         if (imageStream == null) {
            imageStream = this.getClass().getClassLoader().getResourceAsStream("res/images/pics1.png");
         }
         
         if (imageStream == null) {
            File resFile = new File("./res/images/pics1.png");
            if (resFile.exists()) {
               imageStream = new FileInputStream(resFile);
            }
         }
         
         BufferedImage from = null;
         if (imageStream != null) {
            from = ImageIO.read(imageStream);
            imageStream.close();
         }
         
         g2d = this.tileset.createGraphics();
         if (from != null) {
            g2d.drawImage(from, 0, 0, (ImageObserver)null);
         }
      } catch (Exception e) {
         System.out.println("Error updating tileset: " + e.getMessage());
         if (g2d != null) {
            g2d.dispose();
         }
         return;
      }

      // Custom tileset loading - check level file first for TILESET commands
      if (this.mainwindow.getCurrentItem() != null && loadtiledefs) {
         Level level = this.mainwindow.getCurrentItem().level;
         if (level != null && level.filename != null) {
            tryLoadTilesetFromLevel(level, g2d);
         }
      }

      // Then check the tiledefs
      List<Object[]> tiledefs = this.mainwindow.getTileDefinitions();
      if (tiledefs != null && loadtiledefs) {
         String levelname = "";
         if (this.mainwindow.getCurrentItem() != null) {
            levelname = this.mainwindow.getCurrentItem().level.getVerbalName();
         }

         try {
            if (tiledefs.size() > 0) {
               Iterator var6 = tiledefs.iterator();

               while(var6.hasNext()) {
                  Object[] i = (Object[])var6.next();
                  boolean visible = (Boolean)i[0];
                  if (visible) {
                     String tilesetimage = (String)i[1];
                     String prefix = (String)i[2];
                     
                     if (levelname.startsWith(prefix)) {
                        int tiletype = (Integer)i[3];
                        int x = (Integer)i[4];
                        int y = (Integer)i[5];
                        
                        BufferedImage tiledef = null;
                        
                        // First check if image exists in the FileCache
                        String tilesetfiledir = (String)FileCache.cache.get(tilesetimage);
                        if (tilesetfiledir != null) {
                           tiledef = ImageIO.read(new File(tilesetfiledir));
                        } 
                        // Then check in levels/tiles/ directory
                        else {
                           File tileFile = new File(this.mainwindow.options.graaldir + "/levels/tiles/" + tilesetimage);
                           if (tileFile.exists()) {
                              tiledef = ImageIO.read(tileFile);
                           }
                        }
                        
                        if (tiledef != null) {
                           if (tiletype == 0) {
                              this.tileset = new BufferedImage(2048, 512, BufferedImage.TYPE_INT_ARGB);
                              g2d = this.tileset.createGraphics();
                           }
                           
                           g2d.drawImage(tiledef, x, y, (ImageObserver)null);
                           System.out.println("Applied tileset: " + tilesetimage + " for level: " + levelname);
                        } else {
                           System.out.println("Could not find tileset image: " + tilesetimage);
                        }
                     }
                  }
               }
            }
         } catch (IOException var16) {
            if (g2d != null) {
               g2d.dispose();
            }
            return;
         }

         this.mainwindow.updateTilesets(this.tileset);
         this.canvas.updateTileset(this.tileset);
      }
      
      if (g2d != null) {
         g2d.dispose();
      }
   }
   
   private void tryLoadTilesetFromLevel(Level level, Graphics2D g2d) {
         try {
            if (level.filename == null) return;
            
            // Skip resource files (they start with "/res" or "\res")
            if (level.filename.startsWith("/res") || level.filename.startsWith("\\res")) {
               return;
            }
            
            // Make sure the file actually exists
            File levelFile = new File(level.filename);
            if (!levelFile.exists() || !levelFile.isFile()) {
               return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(level.filename));
            String line;
            boolean foundTileset = false;
            
            while ((line = reader.readLine()) != null) {
               if (line.startsWith("TILESET ") || line.startsWith("TILESETIMAGE ")) {
                  String imageName;
                  if (line.startsWith("TILESET ")) {
                     imageName = line.substring(8).trim();
                  } else {
                     imageName = line.substring(12).trim();
                  }
                  
                  // First check in FileCache
                  String tilesetPath = (String)FileCache.cache.get(imageName);
                  BufferedImage customTileset = null;
                  
                  if (tilesetPath != null) {
                     File tilesetFile = new File(tilesetPath);
                     if (tilesetFile.exists()) {
                        customTileset = ImageIO.read(tilesetFile);
                     }
                  }
                  
                  // Then check in levels/tiles directory
                  if (customTileset == null) {
                     File tilesetFile = new File(this.mainwindow.options.graaldir + "/levels/tiles/" + imageName);
                     if (tilesetFile.exists()) {
                        customTileset = ImageIO.read(tilesetFile);
                     }
                  }
                  
                  if (customTileset != null) {
                     // Clear the existing tileset if this is the first custom tileset found
                     if (!foundTileset) {
                        this.tileset = new BufferedImage(2048, 512, BufferedImage.TYPE_INT_ARGB);
                        g2d = this.tileset.createGraphics();
                        foundTileset = true;
                     }
                     
                     // Draw the custom tileset
                     g2d.drawImage(customTileset, 0, 0, (ImageObserver)null);
                     System.out.println("Applied custom tileset from level file: " + imageName);
                  } else {
                     System.out.println("Could not find custom tileset: " + imageName);
                  }
               }
            }
            reader.close();
            
         } catch (IOException e) {
            // Don't print errors for resource files
            if (!(level.filename.startsWith("/res") || level.filename.startsWith("\\res"))) {
               System.out.println("Error loading tileset from level: " + e.getMessage());
            }
         }
      }

   public ScrollPane_Tileset() {
      this.ScrollPane_Tileset();
   }

   public void paint(Graphics g) {
      super.paint(g);
   }

   public void paintComponents(Graphics g) {
      super.paintComponents(g);
      Graphics2D g2d = (Graphics2D)g.create();
      int x = (this.getWidth() - this.tileset.getWidth()) / 2;
      int y = (this.getHeight() - this.tileset.getHeight()) / 2;
      g2d.drawImage(this.tileset, x, y, this);
      g2d.dispose();
   }

   public void cleanup() {
      this.tileset = null;
      this.canvas.cleanup();
   }
}