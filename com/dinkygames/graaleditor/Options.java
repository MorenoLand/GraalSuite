package com.dinkygames.graaleditor;

import java.io.BufferedReader;
import java.io.File;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;

public class Options {
   public GraalEditor main;
   public Preferences prefs;
   private static final String CONFIGFILE = "/res/config.txt";
   BufferedReader lines;
   String graaldir;
   String graalsuitedir;
   boolean contrastselection;
   boolean visualsetshape;
   boolean syntaxhighlighting;
   boolean mainwindow_size_maximized;
   boolean scriptparsing;
   boolean togglevis_links;
   boolean togglevis_signs;
   boolean togglevis_npcs;
   boolean usefilenamecache;
   boolean usetiledefsoffline;
   int mainwindow_position_x;
   int mainwindow_position_y;
   int mainwindow_size_width;
   int mainwindow_size_height;
   int scripttabwidth;

   public Options(GraalEditor main) {
       this.main = main;
       this.graalsuitedir = new File(System.getProperty("user.dir")).getAbsolutePath();
       
       this.prefs = Preferences.userNodeForPackage(GraalEditor.class);
       this.graaldir = this.prefs.get("graaldir", (String)null);
       this.contrastselection = this.prefs.getBoolean("contrastselection", true);
       this.visualsetshape = this.prefs.getBoolean("visualsetshape", true);
       this.usefilenamecache = this.prefs.getBoolean("usefilenamecache", true);
       this.usetiledefsoffline = this.prefs.getBoolean("usetiledefsoffline", true);
       this.scriptparsing = this.prefs.getBoolean("scriptparsing", true);
       this.syntaxhighlighting = this.prefs.getBoolean("syntaxhighlighting", true);
       this.mainwindow_position_x = this.prefs.getInt("mainwindow_position_x", 100);
       this.mainwindow_position_y = this.prefs.getInt("mainwindow_position_y", 100);
       this.mainwindow_size_width = this.prefs.getInt("mainwindow_size_width", 1024);
       this.mainwindow_size_height = this.prefs.getInt("mainwindow_size_height", 768);
       this.mainwindow_size_maximized = this.prefs.getBoolean("mainwindow_size_maximized", false);
       this.togglevis_links = this.prefs.getBoolean("togglevis_links", true);
       this.togglevis_signs = this.prefs.getBoolean("togglevis_signs", true);
       this.togglevis_npcs = this.prefs.getBoolean("togglevis_npcs", true);
       this.scripttabwidth = this.prefs.getInt("scripttabwidth", 2);
       if (this.graaldir == null) {
           JFileChooser chooser = new JFileChooser();
           chooser.setFileSelectionMode(1);
           chooser.setDialogTitle("Choose your Graal Directory");

           for(int returnVal = chooser.showOpenDialog(main.frame); returnVal != 0; returnVal = chooser.showOpenDialog(main.frame)) {
               chooser = new JFileChooser();
               chooser.setFileSelectionMode(1);
           }

           this.graaldir = chooser.getSelectedFile().toString();
           this.prefs.put("graaldir", this.graaldir);
       }
   }
   private String scanForGraalFolder() {
      String[] dirs = new String[]{System.getenv("USERPROFILE") + "\\Graal\\levels\\", System.getenv("ProgramFiles") + " (x86)\\Graal\\levels\\", System.getenv("ProgramFiles") + "\\Graal\\levels\\", System.getenv("APPDATA") + "\\..\\LocalLow\\toonslab\\GraalOnline Worlds\\levels\\", this.graalsuitedir + "\\levels\\", this.graalsuitedir};
      String[] var5 = dirs;
      int var4 = dirs.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         String i = var5[var3];
         File f = new File(i);
         System.out.println("Checking Folder... " + i + " " + f.isDirectory());
         if (f.isDirectory()) {
            return i;
         }
      }

      return null;
   }

   public void toggleLinkVisibility(boolean b) {
      this.togglevis_links = b;
      this.prefs.putBoolean("togglevis_links", this.togglevis_links);
   }

   public void toggleSignVisibility(boolean b) {
      this.togglevis_signs = b;
      this.prefs.putBoolean("togglevis_signs", this.togglevis_signs);
   }

   public void toggleNPCVisibility(boolean b) {
      this.togglevis_npcs = b;
      this.prefs.putBoolean("togglevis_npcs", this.togglevis_npcs);
   }

   public void updateGraalDir(String s) {
      this.graaldir = s;
      this.prefs.put("graaldir", this.graaldir);
   }

   public void save() {
      this.prefs.put("graaldir", this.graaldir);
      this.prefs.putBoolean("contrastselection", this.contrastselection);
      this.prefs.putBoolean("visualsetshape", this.visualsetshape);
      this.prefs.putBoolean("syntaxhighlighting", this.syntaxhighlighting);
      this.prefs.putBoolean("usefilenamecache", this.usefilenamecache);
      this.prefs.putBoolean("usetiledefsoffline", this.usetiledefsoffline);
      this.prefs.putBoolean("scriptparsing", this.scriptparsing);
      this.prefs.putInt("scripttabwidth", this.scripttabwidth);
   }

   public void saveWindowResize(int nw, int nh, boolean maximized) {
      if (!maximized) {
         this.mainwindow_size_width = nw;
         this.mainwindow_size_height = nh;
         this.prefs.putInt("mainwindow_size_width", this.mainwindow_size_width);
         this.prefs.putInt("mainwindow_size_height", this.mainwindow_size_height);
      }

      this.prefs.putBoolean("mainwindow_size_maximized", maximized);
   }

   public void saveWindowMove(int nw, int nh) {
      this.mainwindow_position_x = nw;
      this.mainwindow_position_y = nh;
      this.prefs.putInt("mainwindow_position_x", this.mainwindow_position_x);
      this.prefs.putInt("mainwindow_position_y", this.mainwindow_position_y);
   }
}
