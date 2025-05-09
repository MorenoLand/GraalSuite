package com.dinkygames.graaleditor;

public class LayerListItem {
   private int layerindex;

   public LayerListItem(int layerindex) {
      this.layerindex = layerindex;
   }

   public int getIndex() {
      return this.layerindex;
   }

   public void setIndex(int layerindex) {
      this.layerindex = layerindex;
   }

   public String toString() {
      return Integer.toString(this.layerindex);
   }
}
