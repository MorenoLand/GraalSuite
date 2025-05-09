package com.dinkygames.graaleditor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.swing.JScrollPane;

public class ScrollPane_Level extends JScrollPane {
   private transient BufferedImage background;
   private CreateLink_Dialog dialog_createlink;
   public String uniqueID;
   private final int size = 64;
   public LevelCanvas canvas;
   GraalEditor main;
   ScrollPane_Tileset tilesetpane;
   Level level;
   String levelfile;
   int randomid = 0;
   public int selectedLayer = 0;
   public StateManager undohandler;

   public ScrollPane_Level(String levelfile, ScrollPane_Tileset tilesetpane, GraalEditor main) {
      this.main = main;
      this.uniqueID = UUID.randomUUID().toString();
      this.levelfile = levelfile;
      this.level = new Level(levelfile, this, main);
      this.canvas = new LevelCanvas(this, "/res/images/pics1.png", 64);
      this.setMinimumSize(new Dimension(400, 640));
      this.undohandler = new StateManager(this.level, this);
      this.tilesetpane = tilesetpane;
      this.randomid = (int)(Math.random() * 100000.0D);
      this.getVerticalScrollBar().setUnitIncrement(32);
      this.canvas.setPreferredSize(new Dimension(1024, 1024));

      try {
         String file = "/res/images/pics1.png";
         this.background = ImageIO.read(this.getClass().getResource(file));
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      this.setViewportView(this.canvas);
   }

   public void close() {
      if (ScriptEditor.getInstances().size() > 0) {
         Iterator var2 = ScriptEditor.getInstances().iterator();

         while(var2.hasNext()) {
            ScriptEditor i = (ScriptEditor)var2.next();
            if (i.npc.level.scroll.uniqueID == this.uniqueID) {
               i.dispose();
            }
         }
      }

   }

   public void cleanup() {
   }

   public void createNewLink() {
      TileSelection selection = this.canvas.tileselection;
      if (this.dialog_createlink != null) {
         this.dialog_createlink.dispose();
      }

      this.dialog_createlink = new CreateLink_Dialog(selection, this, -1);
   }

   public void createNewLink(int oldindex, LevelLink_Dialog dialogwindow) {
      if (this.dialog_createlink != null) {
         this.dialog_createlink.dispose();
      }

      this.dialog_createlink = new CreateLink_Dialog(oldindex, this, dialogwindow);
   }

   public void closeNewLink() {
      if (this.dialog_createlink != null) {
         this.dialog_createlink.dispose();
      }

   }

   public void paint(Graphics g) {
      super.paint(g);
   }

   public void undo() {
      this.undohandler.undo();
   }

   public void redo() {
      this.undohandler.redo();
   }

   public void paintComponents(Graphics g) {
      super.paintComponents(g);
      Graphics2D g2d = (Graphics2D)g;
      int x = (this.getWidth() - this.background.getWidth()) / 2;
      int y = (this.getHeight() - this.background.getHeight()) / 2;
      g2d.drawImage(this.background, x, y, this);
      g2d.dispose();
   }
}
