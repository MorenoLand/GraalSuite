package com.dinkygames.graaleditor.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public class PlaceTilesEdit extends AbstractUndoableEdit {
   private final short[][][] tiles;
   private final int x;
   private final int y;

   public PlaceTilesEdit(short[][][] tiles, int x, int y) {
      this.tiles = tiles;
      this.x = x;
      this.y = y;
   }

   public void undo() throws CannotUndoException {
      super.undo();
   }

   public void redo() throws CannotRedoException {
      super.redo();
   }

   public String getPresentationName() {
      return "Place Tiles";
   }
}
