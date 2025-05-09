package com.dinkygames.graaleditor;

import java.util.ArrayList;
import java.util.List;

public class StateManager {
   Level level;
   ScrollPane_Level scroll;
   List<Object[]> undoStack = new ArrayList();
   List<Object[]> redoStack = new ArrayList();
   public static final int TILEPAINT = 0;

   public StateManager(Level level, ScrollPane_Level scroll) {
      this.level = level;
      this.scroll = scroll;
   }

   public void layerPainted(short[][] tiles) {
      Object[] state = new Object[]{0, tiles};
      this.undoStack.add(0, state);
      this.manageUndoStates();
   }

   private void manageUndoStates() {
      while(this.undoStack.size() > 30) {
         this.undoStack.remove(this.undoStack.size() - 1);
      }

      this.scroll.main.toolbar_button_undo.setEnabled(this.undoStack.size() > 0);
      this.scroll.main.toolbar_button_redo.setEnabled(this.redoStack.size() > 0);
   }

   public void undo() {
      if (this.undoStack.size() > 0) {
         Object[] state = (Object[])this.undoStack.get(0);
         int type = (Integer)state[0];
         Object[] redostate = new Object[2];
         if (type == 0) {
            short[][] redoTiles = new short[64][64];

            for(int y = 0; y < 64; ++y) {
               for(int x = 0; x < 64; ++x) {
                  redoTiles[y][x] = this.level.getCurrentLayer().tiles[y][x];
               }
            }

            redostate = new Object[]{0, redoTiles};
            short[][] data = (short[][])state[1];
            this.level.getCurrentLayer().tiles = data;
         }

         ((Layer)this.level.layers.get(this.level.scroll.selectedLayer)).updateTileRender();
         this.scroll.canvas.repaint();
         this.undoStack.remove(0);
         this.redoStack.add(0, redostate);
         this.scroll.main.toolbar_button_undo.setEnabled(this.undoStack.size() > 0);
         this.scroll.main.toolbar_button_redo.setEnabled(this.redoStack.size() > 0);
      }
   }

   public void redo() {
      if (this.redoStack.size() > 0) {
         Object[] state = (Object[])this.redoStack.get(0);
         int type = (Integer)state[0];
         // Fixed comparison: check if type is 0 instead of null
         if (type != 0) {
            this.redoStack.remove(0);
         } else {
            Object[] undostate = new Object[2];
            // This block now only executes if type is 0
            short[][] undotiles = new short[64][64];

            for(int y = 0; y < 64; ++y) {
               for(int x = 0; x < 64; ++x) {
                  undotiles[y][x] = this.level.getCurrentLayer().tiles[y][x];
               }
            }

            undostate = new Object[]{0, undotiles};
            short[][] data = (short[][])state[1];
            this.level.getCurrentLayer().tiles = data;

            this.scroll.canvas.repaint();
            this.undoStack.add(0, undostate);
            this.redoStack.remove(0);
            this.scroll.main.toolbar_button_undo.setEnabled(this.undoStack.size() > 0);
            this.scroll.main.toolbar_button_redo.setEnabled(this.redoStack.size() > 0);
         }
      }
   }
}