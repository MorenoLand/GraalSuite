package com.dinkygames.graaleditor;

public class PredefinedObject {
   String name;
   String tiledata;
   short[][] tiles;
   int width;
   int height = 0;

   public PredefinedObject(String name, int width, int height, short[][] tiles) {
      this.name = name;
      this.width = width;
      this.height = height;
      this.tiles = tiles;
   }
}
