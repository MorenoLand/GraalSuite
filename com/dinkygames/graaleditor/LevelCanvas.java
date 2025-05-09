package com.dinkygames.graaleditor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class LevelCanvas extends JPanel implements MouseMotionListener, MouseListener, KeyListener {
   private static final long serialVersionUID = 1L;
   public transient BufferedImage tileset;
   public transient BufferedImage image;
   ScrollPane_Level scroll;
   private int size = 64;
   Point mousepos = new Point();
   Level level;
   Entity selectedObject;
   int[] selectionoffset = new int[2];
   int tileselection_ox;
   int tileselection_oy;
   int[] tileselection_coords = new int[]{-1, -1, -1, -1};
   TileSelection tileselection;
   public boolean dragging = false;
   public boolean processingclick = false;
   public boolean selectingtiles = false;
   public boolean mouseEntered = false;
   public boolean lmbHeld = false;
   private boolean screenshotting = false;
   boolean[] bucketFilledTiles = new boolean[0];
   GraalEditor main;
   Hex_Dialog hex;
   private int bucketfillcount = 0;
   private boolean painted = false;

   public LevelCanvas() {
   }

   public LevelCanvas(ScrollPane_Level scroll, String file, int size) {
      this.scroll = scroll;
      this.level = scroll.level;
      this.setBackground(Color.BLACK);
      this.addMouseMotionListener(this);
      this.addMouseListener(this);
      this.addKeyListener(this);
      this.setFocusable(true);
      this.size = size;
      this.selectedObject = null;
      this.setPreferredSize(new Dimension(16 * size, 16 * size));

      // Create blank images as defaults
      this.tileset = new BufferedImage(2048, 512, BufferedImage.TYPE_INT_ARGB);
      this.image = new BufferedImage(2048, 512, BufferedImage.TYPE_INT_ARGB);
      
      try {
          // Try different approaches to load the resource
          InputStream imageStream = null;
          
          // Try as resource
          imageStream = this.getClass().getResourceAsStream(file);
          
          // Try with class loader if that failed
          if (imageStream == null) {
              imageStream = this.getClass().getClassLoader().getResourceAsStream(file.substring(1));
          }
          
          // Try with direct file access (for development)
          if (imageStream == null) {
              File resFile = new File("." + file);
              if (resFile.exists()) {
                  imageStream = new FileInputStream(resFile);
              }
          }
          
          // If we found the image, load it
          if (imageStream != null) {
              BufferedImage loadedImage = ImageIO.read(imageStream);
              if (loadedImage != null) {
                  this.tileset = loadedImage;
                  this.image = loadedImage;
              }
              imageStream.close();
          } else {
              System.out.println("Warning: Cannot load file: " + file);
          }
      } catch (Exception e) {
          System.out.println("Error loading tileset: " + e.getMessage());
      }
   }

   protected void paintComponent(Graphics g) {
      Options options = this.scroll.tilesetpane.mainwindow.options;
      this.main = this.scroll.tilesetpane.mainwindow;
      super.paintComponent(g);
      boolean repaint = false;
      if (!this.painted) {
         Iterator var5 = this.level.layers.iterator();

         while(var5.hasNext()) {
            Layer i = (Layer)var5.next();
            i.updateTileRender();
         }

         this.painted = true;
      }

      if (this.selectedObject != null && this.selectedObject.getClass().toString().endsWith("NPC") && !this.scroll.tilesetpane.mainwindow.options.togglevis_npcs) {
         this.selectedObject = null;
      }

      boolean enabletoolbarbtns = this.selectedObject != null && this.tileselection != null;
      this.main.toggleButtonEnabled(this.main.toolbar_button_addlink, enabletoolbarbtns);
      this.main.toggleButtonEnabled(this.main.toolbar_button_addsign, enabletoolbarbtns);
      this.main.toggleButtonEnabled(this.main.toolbar_button_copy, this.selectedObject != null);
      this.main.toggleButtonEnabled(this.main.toolbar_button_cut, this.selectedObject != null);
      this.main.toggleButtonEnabled(this.main.toolbar_button_delete, this.selectedObject != null);
      this.main.toggleButtonEnabled(this.main.toolbar_button_hex, enabletoolbarbtns);
      Graphics2D g2d = (Graphics2D)g;
      this.level.renderTiles(g2d);
      if (this.selectingtiles && !this.screenshotting) {
         BufferedImage outlineimage = new BufferedImage(this.tileselection_coords[2] * 16 + 4, this.tileselection_coords[3] * 16 + 4, 2);
         Graphics2D graphics = outlineimage.createGraphics();
         graphics.fillRect(0, 0, outlineimage.getWidth(), outlineimage.getHeight());
         graphics.clearRect(2, 2, outlineimage.getWidth() - 4, outlineimage.getHeight() - 4);
         g2d.setXORMode(Color.BLACK);
         g2d.drawImage(outlineimage, this.tileselection_coords[0] * 16 - 2 - 3, this.tileselection_coords[1] * 16 - 2 - 3, (ImageObserver)null);
         g2d.setPaintMode();
         graphics.dispose();
      }

      this.level.renderEntities(g2d, this.screenshotting);
      g2d.dispose();
   }

   private void drawInvertedRect(int x, int y, int w, int h) {
      --w;
      --h;

      int i;
      for(i = 0; i < w; ++i) {
         this.drawInvertedPixel(this.tileset, x + i, y);
      }

      for(i = 0; i < w; ++i) {
         this.drawInvertedPixel(this.tileset, x + i, y + h);
      }

      for(i = 0; i < h; ++i) {
         this.drawInvertedPixel(this.tileset, x, y + i);
      }

      for(i = 0; i < h; ++i) {
         this.drawInvertedPixel(this.tileset, x + w, y + i);
      }

   }

   public void drawInvertedPixel(BufferedImage drawingGraphics, int x, int y) {
      if (x >= 0 && y >= 0) {
         if (x < this.image.getWidth() && y < this.image.getHeight()) {
            this.tileset.setRGB(x, y, this.getInvertedColor(this.image.getRGB(x, y)));
         }
      }
   }

   private int getInvertedColor(int rgb) {
      int a = 255 & rgb >> 24;
      return a == 0 ? -65281 : 16777215 - rgb | -16777216;
   }

   private void dragObject(int x, int y) {
      if (this.selectedObject != null) {
         this.setEdited(true);
         int newx = (x - this.selectionoffset[0]) / 16;
         int newy = (y - this.selectionoffset[1]) / 16;
         this.selectedObject.move(newx, newy);
         if (this.selectedObject == this.tileselection) {
            String output = String.format("(%d, %d) -> (%d, %d) = (%d, %d)", x / 16, y / 16, x / 16 + (int)this.tileselection.width, y / 16 + (int)this.tileselection.height, (int)this.tileselection.width, (int)this.tileselection.height);
            this.scroll.tilesetpane.mainwindow.setStatusBarRight(output);
         } else {
            this.scroll.tilesetpane.mainwindow.setStatusBarRight(this.selectedObject.getName() + " -> ( " + newx + ", " + newy + ")");
         }

         this.repaint();
      }

   }

   public void mouseMoved(MouseEvent arg0) {
      int mx = Math.min(Math.max(arg0.getX() + 8, 0), 1008) / 16;
      int my = Math.min(Math.max(arg0.getY() + 8, 0), 1008) / 16;
      Point tilesetpos = TileFunctions.tileToPoint(this.level.getCurrentLayer().tiles[my][mx]);
      String output = String.format("(%d, %d) - tile (%d, %d)", mx, my, tilesetpos.x / 16 % 16, tilesetpos.x / 16 / 16 * 32 + tilesetpos.y / 16);
      this.scroll.tilesetpane.mainwindow.setStatusBarRight(output);
      if (this.selectedObject != null && this.dragging) {
         this.dragObject(arg0.getX(), arg0.getY());
      }

      short resize = 0;
      Entity object = null;
      if (this.scroll.tilesetpane.mainwindow.options.togglevis_npcs) {
         object = this.findObject(this.level.npcs, arg0.getX(), arg0.getY(), true);
      }

      if (object == null) {
         object = this.findObject(this.level.baddies, arg0.getX(), arg0.getY(), true);
      }

      if (object == null) {
         object = this.findObject(this.level.chests, arg0.getX(), arg0.getY(), true);
      }

      if (object == null && this.tileselection != null) {
         object = this.findObject((Entity)this.tileselection, arg0.getX(), arg0.getY());
         if (object != null && object.equals(this.tileselection)) {
            resize = object.canResize(arg0.getX(), arg0.getY());
         }
      }

      if (object == null && !this.dragging) {
         this.setCursor(Cursor.getDefaultCursor());
      } else if (resize > 0) {
         Cursor[] cursors = new Cursor[]{new Cursor(6), new Cursor(10), new Cursor(4), new Cursor(9), new Cursor(5), new Cursor(11), new Cursor(7), new Cursor(8)};
         this.setCursor(cursors[resize - 1]);
      } else {
         this.setCursor(new Cursor(13));
      }

   }

   private Entity findObject(List<?> array, int x, int y) {
      return this.findObject(array, x, y, false);
   }

   private Entity findObject(Entity obj, int x, int y) {
      boolean check = obj.checkMouseinObject(x, y);
      return check ? obj : null;
   }

   private Entity findObject(List<?> array, int x, int y, boolean reverse) {
      int i;
      Entity obj;
      boolean selected;
      if (reverse) {
         for(i = array.size() - 1; i >= 0; --i) {
            obj = (Entity)array.get(i);
            if (obj != null) {
               selected = obj.checkMouseinObject(x, y);
               if (selected) {
                  return obj;
               }
            }
         }
      } else {
         for(i = 0; i < array.size(); ++i) {
            obj = (Entity)array.get(i);
            if (obj != null) {
               selected = obj.checkMouseinObject(x, y);
               if (selected) {
                  return obj;
               }
            }
         }
      }

      return null;
   }

   public void updateTileset(BufferedImage tileset) {
      this.tileset = tileset;
      this.image = tileset;
      Iterator var3 = this.level.layers.iterator();

      while(var3.hasNext()) {
         Layer i = (Layer)var3.next();
         i.updateTileRender();
      }

      this.repaint();
   }

   public void setTileSelection(short[][] tiles, int[] offset) {
      this.setTileSelection(tiles, offset, false);
   }

   public void setSelectedObject(Entity o, int[] offset) {
      this.tileselection = null;
      this.selectedObject = o;
      this.selectionoffset = offset;
      this.dragging = true;
   }

   public void setTileSelection(short[][] tiles, int[] offset, boolean clearonmove) {
      this.tileselection = new TileSelection(tiles, this, clearonmove);
      this.selectionoffset = offset;
      this.selectedObject = this.tileselection;
      this.dragging = true;
   }

   public void mouseDragged(MouseEvent arg0) {
      if (this.selectingtiles) {
         int mx = Math.min(Math.max(arg0.getX() + 8, 0), 1024) / 16;
         int my = Math.min(Math.max(arg0.getY() + 8, 0), 1024) / 16;
         if (mx < this.tileselection_ox) {
            this.tileselection_coords[0] = mx;
            this.tileselection_coords[2] = Math.abs(this.tileselection_ox - mx);
         } else {
            this.tileselection_coords[0] = this.tileselection_ox;
            this.tileselection_coords[2] = mx - this.tileselection_coords[0];
         }

         if (my < this.tileselection_oy) {
            this.tileselection_coords[1] = my;
            this.tileselection_coords[3] = Math.abs(this.tileselection_oy - my);
         } else {
            this.tileselection_coords[1] = this.tileselection_oy;
            this.tileselection_coords[3] = my - this.tileselection_coords[1];
         }

         String output = String.format("(%d, %d) -> (%d, %d) = (%d, %d)", this.tileselection_coords[0], this.tileselection_coords[1], mx, my, this.tileselection_coords[2], this.tileselection_coords[3]);
         this.scroll.tilesetpane.mainwindow.setStatusBarRight(output);
         this.repaint();
      } else if (this.selectedObject != null) {
         if (this.selectedObject.equals(this.tileselection) && this.tileselection.resizing > 0 && this.lmbHeld && this.tileselection.resizing > 0) {
            this.tileselection.resize(this.tileselection.resizing, arg0.getX(), arg0.getY());
            this.repaint();
            return;
         }

         this.dragObject(arg0.getX(), arg0.getY());
      }

   }

   public void mouseClicked(MouseEvent arg0) {
   }

   public void mouseEntered(MouseEvent arg0) {
   }

   public void mouseExited(MouseEvent arg0) {
   }

   public void mousePressed(MouseEvent arg0) {
      this.requestFocusInWindow();
      if (!this.selectingtiles) {
         if (arg0.getButton() == 1) {
            this.lmbHeld = true;
         }

         if (this.dragging) {
            this.dragging = false;
            if (arg0.getButton() == 3 && this.tileselection != null && this.selectedObject != null) {
               String selectedObjectType = this.selectedObject.getClass().toString();
               if (selectedObjectType.endsWith("TileSelection")) {
                  int clickX = arg0.getX() / 16;
                  int clickY = arg0.getY() / 16;
                  this.addUndoState_Tiles(0);
                  this.bucketFilledTiles = new boolean[4096];
                  this.bucketFill(clickX, clickY, this.level.getCurrentLayer().tiles[clickY][clickX], this.tileselection.tiles);
                  this.tileselection = null;
                  this.selectedObject = null;
                  this.setCursor(Cursor.getDefaultCursor());
                  this.bucketFilledTiles = new boolean[0];
                  ((Layer)this.level.layers.get(this.scroll.selectedLayer)).updateTileRender();
               }
            }

            this.repaint();
         } else {
            if (this.tileselection != null && this.selectedObject != null) {
               String selectedObjectType = this.selectedObject.getClass().toString();
               if (this.lmbHeld && arg0.getButton() == 3 && selectedObjectType.endsWith("TileSelection")) {
                  int clickX = arg0.getX() / 16;
                  int clickY = arg0.getY() / 16;
                  this.bucketfillcount = 0;
                  this.addUndoState_Tiles(0);
                  this.bucketFilledTiles = new boolean[4096];
                  this.bucketFill(clickX, clickY, this.level.getCurrentLayer().tiles[clickY][clickX], this.tileselection.tiles);
                  this.tileselection = null;
                  this.selectedObject = null;
                  this.setCursor(Cursor.getDefaultCursor());
                  ((Layer)this.level.layers.get(this.scroll.selectedLayer)).updateTileRender();
                  this.repaint();
                  this.bucketFilledTiles = new boolean[0];
                  return;
               }

               if (arg0.getButton() == 1) {
                  TileSelection foundTileSelection = (TileSelection)this.findObject(this.selectedObject, arg0.getX(), arg0.getY());
                  if (foundTileSelection != null) {
                     short resize = this.tileselection.canResize(arg0.getX(), arg0.getY());
                     if (resize > 0) {
                        this.tileselection.resize(resize, arg0.getX(), arg0.getY());
                     }
                  } else {
                     this.tileselection.clearResizing();
                  }
               }
            }

            Entity object = null;
            if (object == null && this.selectedObject != null) {
               object = this.findObject(this.selectedObject, arg0.getX(), arg0.getY());
            }

            if (object == null) {
               List[] objectlists = new List[]{this.scroll.tilesetpane.mainwindow.options.togglevis_npcs ? this.level.npcs : null, this.level.baddies, this.level.chests};

               for(int listIndex = 0; listIndex < objectlists.length; ++listIndex) {
                  List<?> list = objectlists[listIndex];
                  object = this.findObject(list, arg0.getX(), arg0.getY(), true);
                  if (object != null) {
                     break;
                  }
               }
            }

            boolean sameObject = false;
            if (object != null) {
               sameObject = object.equals(this.selectedObject);
            } else if (arg0.getButton() == 1 && arg0.getClickCount() >= 2 && this.level.links.size() > 0 && this.scroll.tilesetpane.mainwindow.options.togglevis_links) {
               int clickX = arg0.getX();
               int clickY = arg0.getY();
               Iterator var7 = this.level.links.iterator();

               while(var7.hasNext()) {
                  Link i = (Link)var7.next();
                  if (clickX >= i.x * 16 - 8 && clickX < (i.x + i.width) * 16 + 4 && clickY >= i.y * 16 - 4 && clickY < (i.y + i.height) * 16 + 4) {
                     File file = new File(this.level.filename);
                     String dir = file.getParent();
                     String openlevel = dir + "\\" + i.dest;
                     this.scroll.tilesetpane.mainwindow.openLevel(openlevel);
                     System.out.println("Opening... " + openlevel);
                  }
               }
            }

            if (this.selectedObject != null && (object == null || !sameObject)) {
               String foundObjectType = this.selectedObject.getClass().toString();
               if (foundObjectType.endsWith("TileSelection")) {
                  this.tileselection.placeTiles(this.level);
                  this.tileselection = null;
                  this.selectedObject = null;
                  this.repaint();
               }
            }

            if (object == null) {
               this.dragging = false;
               this.selectedObject = null;
               int clickX = Math.min(Math.max(arg0.getX() + 8, 0), 1024) / 16;
               int clickY = Math.min(Math.max(arg0.getY() + 8, 0), 1024) / 16;
               if (arg0.getButton() == 1) {
                  if (arg0.getClickCount() >= 2) {
                     short tile = this.level.getCurrentLayer().tiles[clickY][clickX];
                     this.level.scroll.tilesetpane.mainwindow.updateDefaultTile(tile);
                  } else {
                     this.tileselection_ox = clickX;
                     this.tileselection_oy = clickY;
                     this.tileselection_coords = new int[]{clickX, clickY, 0, 0};
                     this.selectingtiles = true;
                  }

                  this.repaint();
               } else if (arg0.getButton() == 3) {
                  clickX = Math.min(Math.max(arg0.getX(), 0), 1024) / 16;
                  clickY = Math.min(Math.max(arg0.getY(), 0), 1024) / 16;
                  this.addUndoState_Tiles(0);
                  this.bucketFill(clickX, clickY, this.level.getCurrentLayer().tiles[clickY][clickX], this.level.scroll.tilesetpane.mainwindow.defaultTile);
                  ((Layer)this.level.layers.get(this.scroll.selectedLayer)).updateTileRender();
                  this.repaint();
                  this.bucketFilledTiles = new boolean[0];
               }

            } else {
               int offsetX = arg0.getX() - object.getx() * 16;
               int offsetY = arg0.getY() - object.gety() * 16;
               this.selectionoffset[0] = offsetX;
               this.selectionoffset[1] = offsetY;
               if (arg0.getButton() == 3) {
                  try {
                     Entity copiedObject = object.cloneObject(object);
                     String foundObjectType = object.getClass().toString();
                     if (foundObjectType.endsWith("NPC") && this.scroll.tilesetpane.mainwindow.options.togglevis_npcs) {
                        this.level.npcs.add((NPC)copiedObject);
                     } else if (foundObjectType.endsWith("Baddy")) {
                        this.level.baddies.add((Baddy)copiedObject);
                     } else if (foundObjectType.endsWith("Chest")) {
                        this.level.chests.add((Chest)copiedObject);
                     } else if (foundObjectType.endsWith("TileSelection")) {
                        this.tileselection.placeTiles(this.level);
                        this.tileselection = (TileSelection)copiedObject;
                        this.tileselection.clearonmove = false;
                     }

                     this.selectedObject = copiedObject;
                  } catch (CloneNotSupportedException var11) {
                     var11.printStackTrace();
                  }
               } else {
                  this.selectedObject = object;
               }

               if (this.scroll.tilesetpane.mainwindow.options.togglevis_npcs && arg0.getClickCount() >= 2 && arg0.getButton() == 1) {
                  this.openScript(object);
               }

               this.repaint();
            }
         }
      }
   }

   public void openScript(Object object) {
      String objectType = object.getClass().toString();
      if (objectType.endsWith("NPC")) {
         new ScriptEditor((NPC)object);
      }

   }

   public void getHexCode() {
      if (this.tileselection != null) {
         if (this.hex != null) {
            this.hex.update(this.tileselection.tiles);
         } else {
            this.hex = new Hex_Dialog(this.tileselection.tiles);
         }

      }
   }

   public void clearTileSelection() {
      if (this.tileselection != null) {
         this.tileselection = null;
         this.selectedObject = null;
      }
   }

   public void clearAndPlaceTileSelection() {
      if (this.tileselection != null) {
         this.tileselection.placeTiles(this.level);
         this.tileselection = null;
         this.selectedObject = null;
      }
   }

   public void mouseReleased(MouseEvent arg0) {
      if (arg0.getButton() == 1) {
         this.lmbHeld = false;
      }

      if (this.tileselection != null) {
         this.tileselection.clearResizing();
      }

      this.processingclick = false;
      if (this.selectingtiles && this.tileselection_coords != null) {
         int tx = this.tileselection_coords[0];
         int ty = this.tileselection_coords[1];
         int tw = this.tileselection_coords[2];
         int th = this.tileselection_coords[3];
         if (tw >= 1 && th >= 1) {
            short[][] tiledata = new short[th][tw];

            for(int y = 0; y < th; ++y) {
               for(int x = 0; x < tw; ++x) {
                  tiledata[y][x] = this.level.getCurrentLayer().tiles[ty + y][tx + x];
               }
            }

            int[] offset = new int[]{arg0.getX() - tx * 16, arg0.getY() - ty * 16};
            this.setTileSelection(tiledata, offset, true);
            this.tileselection.setx((arg0.getX() - this.selectionoffset[0]) / 16);
            this.tileselection.sety((arg0.getY() - this.selectionoffset[1]) / 16);
            this.dragging = false;
         }
      }

      this.selectingtiles = false;
      this.repaint();
   }

   public void keyPressed(KeyEvent arg0) {
      int keycode = arg0.getKeyCode();
      if (keycode == 127) {
         boolean hardDelete = arg0.getModifiers() == 1;
         this.deleteSelection(hardDelete);
      } else if (keycode != 90 && keycode == 49) {
         this.screenshot();
      }

   }

   public void cut() {
      if (this.selectedObject != null) {
         this.copy();
         this.deleteSelection(false);
      }
   }

   public void copy() {
      if (this.selectedObject != null) {
         try {
            this.scroll.main.clipboard = this.selectedObject.cloneObject(this.selectedObject);
         } catch (CloneNotSupportedException var2) {
            var2.printStackTrace();
         }

      }
   }

   public void paste() {
      if (this.scroll.main.clipboard != null) {
         Entity clipboard = this.scroll.main.clipboard;

         try {
            String classtype = clipboard.getClass().toString();
            int[] offset = new int[]{(int)(clipboard.getwidth() * 16.0F / 2.0F), (int)(clipboard.getheight() * 16.0F / 2.0F)};
            if (offset[0] == 0 && offset[1] == 0) {
               offset[0] = offset[1] = 24;
            }

            int mx = -1000;
            int my = -1000;
            if (this.getMousePosition() != null) {
               mx = this.getMousePosition().x / 16;
               my = this.getMousePosition().y / 16;
            }

            clipboard.setx(mx - offset[0] / 16);
            clipboard.sety(my - offset[1] / 16);
            Entity pastedObject = clipboard.cloneObject(clipboard);
            pastedObject.setx(mx - offset[0] / 16);
            pastedObject.sety(my - offset[1] / 16);
            if (classtype.endsWith("TileSelection")) {
               TileSelection tileobj = (TileSelection)pastedObject;
               tileobj.setx(mx - offset[0] / 16);
               tileobj.sety(my - offset[1] / 16);
               this.setTileSelection(tileobj.tiles, offset);
               this.tileselection.setx(mx - offset[0] / 16);
               this.tileselection.sety(my - offset[1] / 16);
               System.out.println(this.tileselection.getx() + " : " + this.tileselection.gety());
            } else {
               if (classtype.endsWith("NPC")) {
                  this.level.addNPC((NPC)pastedObject, mx - offset[0] / 16, my - offset[1] / 16);
               }

               this.setSelectedObject(pastedObject, offset);
            }

            this.repaint();
         } catch (CloneNotSupportedException var8) {
            var8.printStackTrace();
         } catch (NullPointerException var9) {
         }

      }
   }

   public void cleanup() {
      this.tileset = null;
      this.image = null;
      Iterator var2 = this.level.layers.iterator();

      while(var2.hasNext()) {
         Layer i = (Layer)var2.next();
         i.cleanup();
      }

   }

   public void deleteSelection(boolean hardDelete) {
      if (this.selectedObject != null) {
         String objectType = this.selectedObject.getClass().toString();
         if (objectType.endsWith("NPC")) {
            this.level.npcs.remove(this.selectedObject);
         } else if (objectType.endsWith("Baddy")) {
            this.level.baddies.remove(this.selectedObject);
         } else if (objectType.endsWith("Chest")) {
            this.level.chests.remove(this.selectedObject);
         } else if (objectType.endsWith("TileSelection")) {
            this.tileselection.clearTiles(this.level, hardDelete);
         }

         this.selectedObject = null;
         this.tileselection = null;
         this.setCursor(Cursor.getDefaultCursor());
         this.repaint();
      }

   }

   public void screenshot() {
      this.selectedObject = null;
      this.tileselection = null;
      this.lmbHeld = false;
      this.screenshotting = true;
      BufferedImage saveimage = new BufferedImage(1024, 1024, 1);
      Graphics2D cg = saveimage.createGraphics();
      this.paintAll(cg);

      try {
         if (ImageIO.write(saveimage, "png", new File("./screenshot.png"))) {
            System.out.println("-- saved");
         }
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      this.screenshotting = false;
      this.repaint();
   }

   public void keyReleased(KeyEvent arg0) {
   }

   public void keyTyped(KeyEvent arg0) {
   }

   public void bucketFill(int mx, int my, short oldTile, short newTile) {
      if (mx >= 0 && mx <= 64 && my >= 0 && my <= 64 && oldTile != newTile) {
         short[][] tiles = this.level.getCurrentLayer().tiles;
         tiles[my][mx] = newTile;

         try {
            if (my - 1 >= 0 && tiles[my - 1][mx] == oldTile) {
               this.bucketFill(mx, my - 1, oldTile, newTile);
            }

            if (mx - 1 >= 0 && tiles[my][mx - 1] == oldTile) {
               this.bucketFill(mx - 1, my, oldTile, newTile);
            }

            if (my + 1 < 64 && tiles[my + 1][mx] == oldTile) {
               this.bucketFill(mx, my + 1, oldTile, newTile);
            }

            if (mx + 1 < 64 && tiles[my][mx + 1] == oldTile) {
               this.bucketFill(mx + 1, my, oldTile, newTile);
            }
         } catch (ArrayIndexOutOfBoundsException var7) {
            System.out.println("error: " + mx + " : " + my);
         }

      }
   }

   public void bucketFill(int mx, int my, short oldTile, short[][] newTiles) {
      if (mx >= 0 && mx <= 64 && my >= 0 && my <= 64) {
         int tilepos = my * 64 + mx;
         if (!this.bucketFilledTiles[tilepos]) {
            short[][] tiles = this.level.getCurrentLayer().tiles;
            tiles[my][mx] = newTiles[my % newTiles.length][mx % newTiles[0].length];
            this.bucketFilledTiles[tilepos] = true;

            try {
               if (my - 1 >= 0 && tiles[my - 1][mx] == oldTile) {
                  this.bucketFill(mx, my - 1, oldTile, newTiles);
               }

               if (mx - 1 >= 0 && tiles[my][mx - 1] == oldTile) {
                  this.bucketFill(mx - 1, my, oldTile, newTiles);
               }

               if (my + 1 < 64 && tiles[my + 1][mx] == oldTile) {
                  this.bucketFill(mx, my + 1, oldTile, newTiles);
               }

               if (mx + 1 < 64 && tiles[my][mx + 1] == oldTile) {
                  this.bucketFill(mx + 1, my, oldTile, newTiles);
               }
            } catch (ArrayIndexOutOfBoundsException var8) {
               System.out.println("error: " + mx + " : " + my);
            }

         }
      }
   }

   public void setEdited(boolean b) {
      this.level.edited = b;
      int index = this.main.contentPane.getSelectedIndex();
      String currentTitle = (b ? "*" : "") + this.level.getVerbalName();
      this.main.contentPane.setTitleAt(index, currentTitle);
   }

   public void addUndoState_Tiles(int type) {
      this.scroll.undohandler.redoStack.clear();
      this.setEdited(true);
      short[][] tiles = this.level.getCurrentLayer().tiles;
      if (type == 0) {
         short[][] oldTiles = new short[64][64];

         for(int y = 0; y < 64; ++y) {
            for(int x = 0; x < 64; ++x) {
               oldTiles[y][x] = tiles[y][x];
            }
         }

         this.scroll.undohandler.layerPainted(oldTiles);
      }

   }
}