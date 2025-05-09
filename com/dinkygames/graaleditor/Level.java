package com.dinkygames.graaleditor;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Level {
   private static final long serialVersionUID = 1L;
   public boolean edited = false;
   String filename;
   String levelname;
   ScrollPane_Level scroll;
   BufferedReader level_br;
   String file;
   List<Layer> layers = new ArrayList();
   List<Link> links = new ArrayList();
   List<Sign> signs = new ArrayList();
   List<NPC> npcs = new ArrayList();
   List<Chest> chests = new ArrayList();
   List<Baddy> baddies = new ArrayList();
   boolean readingSign;
   boolean readingNPC;
   NPC tempnpc;
   GraalEditor main;

   public Level(String file, ScrollPane_Level scroll, GraalEditor main) {
      this.main = main;
      this.filename = file;
      this.scroll = scroll;
      this.levelname = (new File(file)).getName();
      if (this.filename.startsWith("/res")) {
         this.levelname = null;
      }

      main.debugger.println("Level Object created with path: " + file);
      main.debugger.println("                    Level name: " + this.levelname);

      try {
         if (file.startsWith("/res/")) {
            this.level_br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(file)));
         } else {
            this.level_br = new BufferedReader(new FileReader(file));
         }
      } catch (FileNotFoundException var15) {
         var15.printStackTrace();
      }

      String line = "";

      try {
         line = this.level_br.readLine();
         if (!line.equals("GLEVNW01")) {
            System.out.println((new File(file)).getName() + " is not a valid NW level!");
            return;
         }

         Layer newLayer = this.addLayer(0);
         int lastlayer = 0;

         label144:
         while(true) {
            while(true) {
               while(true) {
                  if ((line = this.level_br.readLine()) == null) {
                     break label144;
                  }

                  String[] data;
                  int xPos;
                  int yPos;
                  int xCoord;
                  int signindex;
                  if (line.startsWith("BOARD ")) {
                     data = line.trim().substring(5).trim().split(" ");
                     if (data.length == 5) {
                        xPos = Integer.parseInt(data[3]);
                        if (xPos != lastlayer) {
                           newLayer = this.addLayer();
                        }

                        xCoord = Integer.parseInt(data[0]);
                        yPos = Integer.parseInt(data[1]);
                        signindex = Integer.parseInt(data[2]);
                        String tiletextdata = data[4];
                        short[] tilelinedata = new short[Math.min(tiletextdata.length() / 2, 64)];
                        Arrays.fill(tilelinedata, (short)-1);

                        for(int i = 0; i < tilelinedata.length; ++i) {
                           tilelinedata[i] = TileFunctions.base64toTile(tiletextdata.substring(i * 2, i * 2 + 2));
                        }

                        newLayer.setLayerRow(xCoord, yPos, tilelinedata);
                        lastlayer = xPos;
                     } else {
                        System.out.println("Invalid BOARD Data: " + line);
                     }
                  } else if (line.startsWith("LAYERNAME ")) {
                     line = line.trim().substring(10).trim();
                     data = line.split(" ");
                     if (data.length >= 2) {
                        xPos = Integer.parseInt(data[0]);
                        String layername = line.substring(line.indexOf(data[1]));
                        ((Layer)this.layers.get(xPos)).setName(layername);
                     }
                  } else {
                     String image;
                     int i;
                     if (line.startsWith("LINK ")) {
                        data = line.trim().substring(4).trim().split(" ");
                        if (data.length == 7) {
                           image = data[0];
                           xPos = Integer.parseInt(data[1]);
                           yPos = Integer.parseInt(data[2]);
                           signindex = Integer.parseInt(data[3]);
                           i = Integer.parseInt(data[4]);
                           String destx = data[5];
                           String desty = data[6];
                           this.links.add(new Link(image, xPos, yPos, signindex, i, destx, desty));
                        } else {
                           System.out.println("Invalid LINK Data: " + line);
                        }
                     } else if (line.startsWith("CHEST ")) {
                        data = line.trim().substring(5).trim().split(" ");
                        if (data.length == 4) {
                           xCoord = Integer.parseInt(data[0]);
                           xPos = Integer.parseInt(data[1]);
                           String item = data[2];
                           signindex = Integer.parseInt(data[3]);
                           this.chests.add(new Chest(xCoord, xPos, item, signindex));
                        } else {
                           System.out.println("Invalid CHEST Data: " + line);
                        }
                     } else if (line.startsWith("BADDY ")) {
                        data = line.trim().substring(5).trim().split(" ");
                        if (data.length != 3) {
                           System.out.println("Invalid BADDY Data: " + line);
                        } else {
                           xCoord = Integer.parseInt(data[0]);
                           xPos = Integer.parseInt(data[1]);
                           short baddyType = Short.parseShort(data[2]);
                           String[] dialog = new String[3];

                           for(i = 0; i < 3; ++i) {
                              line = this.level_br.readLine();
                              if (line.equals("BADDYEND")) {
                                 break;
                              }

                              dialog[i] = line;
                           }

                           this.baddies.add(new Baddy(xCoord, xPos, baddyType, dialog));
                        }
                     } else if (line.startsWith("SIGN ")) {
                        data = line.trim().substring(4).trim().split(" ");
                        if (data.length != 2) {
                           System.out.println("Invalid SIGN Data: " + line);
                        } else {
                           this.readingSign = true;
                           image = "";
                           xCoord = Integer.parseInt(data[0]);

                           for(yPos = Integer.parseInt(data[1]); !(line = this.level_br.readLine()).equals("SIGNEND"); image = image + line + "\r\n") {
                           }

                           if (image.endsWith("\r\n")) {
                              image = image.substring(0, image.length() - 2);
                           }

                           this.signs.add(new Sign(image, xCoord, yPos));
                        }
                     } else if (line.startsWith("NPC ")) {
                        data = line.trim().substring(3).trim().split(" ");
                        if (data.length != 3) {
                           System.out.println("Invalid NPC Data: " + line);
                        } else {
                           image = data[0];
                           if (image.equals("-")) {
                              image = "";
                           }

                           xCoord = Integer.parseInt(data[1]);
                           yPos = Integer.parseInt(data[2]);

                           String script;
                           for(script = ""; !(line = this.level_br.readLine()).equals("NPCEND"); script = script + line + "\r\n") {
                           }

                           script = script.trim();
                           this.npcs.add(new NPC(image, xCoord, yPos, script, this));
                        }
                     }
                  }
               }
            }
         }
      } catch (IOException var16) {
         var16.printStackTrace();
      }

      this.sortNPCs();
   }

   public void renderTiles(Graphics2D g2d) {
      Composite defaultComp = g2d.getComposite();

      for(int z = 0; z < this.layers.size(); ++z) {
         Layer layer = (Layer)this.layers.get(z);
         if (layer.visible) {
            BufferedImage layerimage = layer.getTileRender();
            AlphaComposite ac = AlphaComposite.getInstance(3, layer.alpha);
            g2d.setComposite(ac);
            g2d.drawImage(layerimage, 0, 0, (ImageObserver)null);
            g2d.setComposite(defaultComp);
            if (this.scroll.canvas.tileselection != null && z == this.scroll.selectedLayer) {
               this.scroll.canvas.tileselection.render(g2d);
               if (this.scroll.canvas.selectedObject.equals(this.scroll.canvas.tileselection)) {
                  BufferedImage outlineimage = this.scroll.canvas.selectedObject.createOutline();
                  g2d.setPaintMode();
                  g2d.setXORMode(Color.BLACK);
                  this.scroll.canvas.selectedObject.renderOutline(g2d, outlineimage);
                  g2d.setPaintMode();
               }
            }
         }
      }

   }

   public void renderEntities(Graphics2D g2d, boolean screenshotting) {
      Options options = this.scroll.tilesetpane.mainwindow.options;
      Chest chestItem;
      Iterator var5;
      Baddy baddyItem;
      NPC npcItem;
      if (screenshotting) {
         var5 = this.chests.iterator();

         while(var5.hasNext()) {
            chestItem = (Chest)var5.next();
            chestItem.render(g2d);
         }

         var5 = this.baddies.iterator();

         while(var5.hasNext()) {
            baddyItem = (Baddy)var5.next();
            baddyItem.render(g2d);
         }

         if (options.togglevis_npcs) {
            var5 = this.npcs.iterator();

            while(var5.hasNext()) {
               npcItem = (NPC)var5.next();
               if (npcItem != null && npcItem.imagefound) {
                  npcItem.render(g2d, true);
               }
            }
         }
      } else {
         var5 = this.chests.iterator();

         while(var5.hasNext()) {
            chestItem = (Chest)var5.next();
            chestItem.render(g2d);
         }

         var5 = this.baddies.iterator();

         while(var5.hasNext()) {
            baddyItem = (Baddy)var5.next();
            baddyItem.render(g2d);
         }

         if (options.togglevis_npcs) {
            var5 = this.npcs.iterator();

            while(var5.hasNext()) {
               npcItem = (NPC)var5.next();
               if (npcItem != null) {
                  npcItem.render(g2d);
               }
            }
         }

         if (options.togglevis_links) {
            var5 = this.links.iterator();

            while(var5.hasNext()) {
               Link linkItem = (Link)var5.next();
               linkItem.render(g2d);
            }
         }

         if (options.togglevis_signs) {
            var5 = this.signs.iterator();

            while(var5.hasNext()) {
               Sign signItem = (Sign)var5.next();
               signItem.render(g2d);
            }
         }

         if (this.scroll.canvas.selectedObject != null && !this.scroll.canvas.dragging && !this.scroll.canvas.selectedObject.equals(this.scroll.canvas.tileselection)) {
            BufferedImage outlineimage = this.scroll.canvas.selectedObject.createOutline();
            g2d.setXORMode(Color.BLACK);
            this.scroll.canvas.selectedObject.renderOutline(g2d, outlineimage);
            g2d.setPaintMode();
            this.scroll.canvas.selectedObject.render(g2d);
         }
      }

   }

   public int getLayerIndex(Layer layer) {
      return this.layers.indexOf(layer);
   }

   public Layer getCurrentLayer() {
      return (Layer)this.layers.get(this.scroll.selectedLayer);
   }

   public short[][] getCurrentLayerTiles() {
      return ((Layer)this.layers.get(this.scroll.selectedLayer)).tiles;
   }

   public void moveLayerUp(int index) {
      if (index != this.layers.size() - 1) {
         Collections.swap(this.layers, index, index + 1);
         if (this.scroll.selectedLayer == index) {
            this.scroll.selectedLayer = index + 1;
         } else if (this.scroll.selectedLayer == index + 1) {
            this.scroll.selectedLayer = index;
         }

         this.main.updateLayerPanel();
      }
   }

   public void moveLayerDown(int index) {
      if (index > 0) {
         Collections.swap(this.layers, index, index - 1);
         if (this.scroll.selectedLayer == index) {
            this.scroll.selectedLayer = index - 1;
         } else if (this.scroll.selectedLayer == index - 1) {
            this.scroll.selectedLayer = index;
         }

         this.main.updateLayerPanel();
      }
   }

   public Level deepCopy() throws Exception {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(bos);
      out.writeObject(this);
      ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
      ObjectInputStream in = new ObjectInputStream(bis);
      Level copied = (Level)in.readObject();
      return copied;
   }

   public Layer addLayer() {
      Layer newLayer = new Layer(this, (short[][])null);
      this.layers.add(newLayer);
      return newLayer;
   }

   public void deleteLayer(int index) {
      if (index >= 1 && index <= this.layers.size() - 1) {
         System.out.println("test: " + index);
         if (this.scroll.selectedLayer == index) {
            this.scroll.selectedLayer = index - 1;
         }

         this.layers.remove(index);
      }
   }

   public Layer addLayer(int layerindex) {
      Layer newLayer = new Layer(this, (short[][])null);
      this.layers.add(layerindex, newLayer);
      return newLayer;
   }

   public void addLink(String dest, int x, int y, int w, int h, String destx, String desty) {
      this.scroll.canvas.setEdited(true);
      this.links.add(new Link(dest, x, y, w, h, destx, desty));
   }

   public void addLink(Link link) {
      this.scroll.canvas.setEdited(true);
      this.links.add(link);
   }

   public void removeLink(int index) {
      if (index >= 0 && index < this.links.size()) {
         this.scroll.canvas.setEdited(true);
         this.links.remove(index);
         this.scroll.canvas.repaint();
         this.scroll.closeNewLink();
      }
   }

   public void updateLink(int index, Link link) {
      this.links.set(index, link);
      this.scroll.canvas.repaint();
   }

   public NPC addNPC() {
      this.scroll.canvas.setEdited(true);
      NPC npc = new NPC((String)null, -1000, -1000, "", this);
      this.npcs.add(npc);
      return npc;
   }

   public NPC addNPC(NPC from, int x, int y) {
      this.scroll.canvas.setEdited(true);
      NPC npc = new NPC((String)null, x, y, "", this);
      this.npcs.add(from);
      return npc;
   }

   public Link[] getLinksAsArray() {
      Link[] links = new Link[this.links.size()];

      for(int i = 0; i < links.length; ++i) {
         links[i] = (Link)this.links.get(i);
      }

      return links;
   }

   public void saveLevel() {
      this.saveLevel((String)null);
   }

   public String getVerbalName() {
      return this.levelname == null ? "<untitled>" : this.levelname;
   }

   public void sortNPCs() {
      for(int a = 0; a < this.npcs.size(); ++a) {
         for(int b = a + 1; b < this.npcs.size(); ++b) {
            if (((NPC)this.npcs.get(b)).gety() + ((NPC)this.npcs.get(b)).getLayer() * 16 < ((NPC)this.npcs.get(a)).gety() + ((NPC)this.npcs.get(a)).getLayer() * 16) {
               NPC results = (NPC)this.npcs.get(b);
               this.npcs.set(b, (NPC)this.npcs.get(a));
               this.npcs.set(a, results);
            }
         }
      }

   }

public void saveLevel(String path) {
      if (path != null) {
         this.filename = path;
         this.levelname = (new File(this.filename)).getName();
         if (this.filename.startsWith("/res")) {
            this.levelname = null;
         }

         this.scroll.tilesetpane.updateTileset(true);
      }

      String NEWLINE = System.getProperty("line.separator");
      String output = "GLEVNW01" + NEWLINE;
      
      // Add any TILESET/TILESETIMAGE entries from tiledefs that match this level's prefix
      if (this.scroll.main.tiledefs != null && this.scroll.main.tiledefs.tiledefinitions.size() > 0) {
         for (Object[] def : this.scroll.main.tiledefs.tiledefinitions) {
            if ((Boolean)def[0] && def[2] != null && this.getVerbalName().startsWith((String)def[2])) {
               if ((Integer)def[3] == 0) {
                  output += "TILESET " + def[1] + NEWLINE;
               } else {
                  output += "TILESETIMAGE " + def[1] + NEWLINE;
               }
            }
         }
      }

      int i;
      Layer layer;
      int j;
      for(i = 0; i < this.layers.size(); ++i) {
         layer = (Layer)this.layers.get(i);

         for(j = 0; j < layer.tiles.length; ++j) {
            String tiledata = "";
            boolean firstblank = false;
            int width = layer.getRowWidth(j);
            if (width > 0) {
               String linedata = layer.getRowData(i, j);
               output = output + linedata + NEWLINE;
            }
         }
      }

      String img;
      for(i = 0; i < this.layers.size(); ++i) {
         layer = (Layer)this.layers.get(i);
         if (layer.name != null) {
            img = String.format("LAYERNAME %d %s", i, layer.name);
            output = output + img + NEWLINE;
         }
      }

      for(i = 0; i < this.links.size(); ++i) {
         Link link = (Link)this.links.get(i);
         img = String.format("LINK %s %d %d %d %d %s %s", link.dest, link.x, link.y, link.width, link.height, link.destx, link.desty);
         output = output + img + NEWLINE;
      }

      for(i = 0; i < this.signs.size(); ++i) {
         Sign sign = (Sign)this.signs.get(i);
         output = output + "SIGN " + sign.x + " " + sign.y + NEWLINE;
         output = output + sign.text + NEWLINE;
         output = output + "SIGNEND" + NEWLINE;
      }

      for(i = 0; i < this.chests.size(); ++i) {
         Chest chest = (Chest)this.chests.get(i);
         img = chest.item != null && !chest.item.equals("") ? chest.item : "greenrupee";
         output = output + "CHEST " + chest.x + " " + chest.y + " " + img + " " + chest.signindex + NEWLINE;
      }

      for(i = 0; i < this.baddies.size(); ++i) {
         Baddy baddy = (Baddy)this.baddies.get(i);
         output = output + "BADDY " + baddy.x + " " + baddy.y + " " + baddy.type + NEWLINE;

         for(j = 0; j < 3; ++j) {
            output = output + baddy.dialog[j] + NEWLINE;
         }

         output = output + "BADDYEND" + NEWLINE;
      }

      for(i = 0; i < this.npcs.size(); ++i) {
         NPC npc = (NPC)this.npcs.get(i);
         if (npc.getx() >= -900 && npc.gety() >= -900) {
            img = npc.image != null && !npc.image.equals("") ? npc.image : "-";
            output = output + "NPC " + img + " " + npc.x + " " + npc.y + NEWLINE;
            output = output + npc.script.trim() + NEWLINE;
            output = output + "NPCEND" + NEWLINE;
         }
      }

      try {
         FileWriter writer = new FileWriter(this.filename);
         try {
            BufferedWriter bw = new BufferedWriter(writer);
            try {
               bw.write(output);
            } finally {
               bw.close();
            }
         } finally {
            writer.close();
         }
      } catch (IOException var26) {
         var26.printStackTrace();
      }
   }
}