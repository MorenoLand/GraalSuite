package com.dinkygames.graaleditor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TileDefinitions {
   public static final int DEFAULT = 0;
   public static final int NEW = 1;
   public static final int TILEDEF2 = 4;
   public static final int TERRAIN = 5;
   public GraalEditor main;
   private static final String NEWLINE = System.getProperty("line.separator");
   public static final Class[] arrayStructure = new Class[]{String.class, String.class, Integer.class, Integer.class, Integer.class};
   String graaldir;
   public List<Object[]> tiledefinitions = new ArrayList();

   public TileDefinitions(String graaldir, GraalEditor main) {
      this.main = main;
      this.graaldir = graaldir;
      this.updateTileDefinitions((String)null);
   }

   public Object[][] getTileDefsAsArray() {
      Object[][] defs = new Object[this.tiledefinitions.size()][5];

      for(int i = 0; i < defs.length; ++i) {
         defs[i] = (Object[])this.tiledefinitions.get(i);
      }

      return defs;
   }

   public void sortTileDefinitions() {
      for(int a = 0; a < this.tiledefinitions.size(); ++a) {
         for(int b = a + 1; b < this.tiledefinitions.size(); ++b) {
            Object[] obja = (Object[])this.tiledefinitions.get(a);
            String prefa = (String)obja[2];
            int moda = (Integer)obja[3] * 100;
            Object[] objb = (Object[])this.tiledefinitions.get(b);
            String prefb = (String)objb[2];
            int modb = (Integer)objb[3] * 100;
            if (prefb.length() + modb < prefa.length() + moda) {
               Object[] results = (Object[])this.tiledefinitions.get(b);
               this.tiledefinitions.set(b, (Object[])this.tiledefinitions.get(a));
               this.tiledefinitions.set(a, results);
            }
         }
      }

      ScrollPane_Level cur = this.main.getCurrentItem();
      if (cur != null) {
         this.main.getCurrentItem().canvas.repaint();
      }
   }

   public void save() {
      String tiledeffile = "tiledefsOffline.txt";
      File filedir = new File(this.graaldir + "\\levels\\tiledefs\\");
      if (!filedir.exists()) {
         System.out.println("tiledefs folder does not exist. Creating...");
         filedir.mkdirs();
      }

      File file = new File(this.graaldir + "\\levels\\tiledefs\\" + tiledeffile);
      if (this.tiledefinitions.size() > 0) {
         String output = "";

         String line;
         for(Iterator var6 = this.tiledefinitions.iterator(); var6.hasNext(); output = output + line) {
            Object[] i = (Object[])var6.next();
            line = String.format("%s,%s,%d,%d,%d" + NEWLINE, i[1], i[2], i[3], i[4], i[5]);
         }

         output = output.substring(0, output.length() - 2);

         try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(output);
            bw.close();
         } catch (IOException var8) {
            var8.printStackTrace();
         }
      }
   }

   public void addDefinition(Object[] newdef) {
      this.tiledefinitions.add(newdef);
      this.sortTileDefinitions();
   }

   public void updateDefinition(int index, Object[] newdef) {
      this.tiledefinitions.set(index, newdef);
   }

   public void updateTileDefinitions(String graaldir) {
      if (graaldir != null) {
         this.graaldir = graaldir;
      }

      File file = new File(this.graaldir + "\\levels\\tiledefs\\tiledefsOffline.txt");
      if (!file.exists()) {
         System.out.println("No tile definitions found.");
      } else {
         String line = "";
         System.out.println("Loading tile definitions...");

         try {
            BufferedReader level_br = new BufferedReader(new FileReader(file));

            while((line = level_br.readLine()) != null) {
               String[] parts = line.split(",");
               if (parts.length >= 5) {
                  boolean visible = true;
                  String image = parts[0];
                  String prefix = parts[1];
                  int tiledeftype = Integer.parseInt(parts[2]);
                  int x = Integer.parseInt(parts[3]);
                  int y = Integer.parseInt(parts[4]);
                  Object[] newdef = new Object[]{visible, image, prefix, tiledeftype, x, y};
                  this.tiledefinitions.add(newdef);
                  System.out.println("Tile definition loaded: " + Arrays.toString(newdef));
               }
            }
            
            level_br.close();
         } catch (FileNotFoundException var13) {
            var13.printStackTrace();
         } catch (IOException var14) {
            var14.printStackTrace();
         }

         this.sortTileDefinitions();
      }
   }
   
   public void exportTileDefsToLevel(String levelFilename, String levelName) {
      try {
         File levelFile = new File(levelFilename);
         if (!levelFile.exists()) {
            return;
         }
         
         BufferedReader reader = new BufferedReader(new FileReader(levelFile));
         StringBuilder content = new StringBuilder();
         String line;
         
         // Read header line
         if ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
         }
         
         // Add tileset definitions
         for (Object[] def : tiledefinitions) {
            if (def[0] == Boolean.TRUE && def[2] != null && levelName.startsWith((String)def[2])) {
               if ((Integer)def[3] == 0) {
                  content.append("TILESET ").append(def[1]).append("\n");
               } else {
                  content.append("TILESETIMAGE ").append(def[1]).append("\n");
               }
            }
         }
         
         // Continue reading file, skipping existing TILESET lines
         while ((line = reader.readLine()) != null) {
            if (!line.startsWith("TILESET ") && !line.startsWith("TILESETIMAGE ")) {
               content.append(line).append("\n");
            }
         }
         reader.close();
         
         // Write the updated content back to the file
         FileWriter writer = new FileWriter(levelFile);
         writer.write(content.toString());
         writer.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   public void importTileDefsFromLevel(String levelFilename) {
      try {
         File levelFile = new File(levelFilename);
         if (!levelFile.exists()) {
            return;
         }
         
         String levelName = levelFile.getName();
         levelName = levelName.substring(0, levelName.lastIndexOf('.'));
         
         BufferedReader reader = new BufferedReader(new FileReader(levelFile));
         String line;
         
         while ((line = reader.readLine()) != null) {
            if (line.startsWith("TILESET ")) {
               String image = line.substring(8).trim();
               boolean exists = false;
               
               for (Object[] def : tiledefinitions) {
                  if (def[1].equals(image) && def[2].equals(levelName)) {
                     exists = true;
                     break;
                  }
               }
               
               if (!exists) {
                  Object[] newDef = new Object[]{true, image, levelName, 0, 0, 0};
                  tiledefinitions.add(newDef);
               }
            } else if (line.startsWith("TILESETIMAGE ")) {
               String image = line.substring(12).trim();
               boolean exists = false;
               
               for (Object[] def : tiledefinitions) {
                  if (def[1].equals(image) && def[2].equals(levelName)) {
                     exists = true;
                     break;
                  }
               }
               
               if (!exists) {
                  Object[] newDef = new Object[]{true, image, levelName, 4, 0, 0};
                  tiledefinitions.add(newDef);
               }
            }
         }
         reader.close();
         sortTileDefinitions();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}