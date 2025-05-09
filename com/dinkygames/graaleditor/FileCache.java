package com.dinkygames.graaleditor;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;

public class FileCache {
   private static final String cachename = "GRAALSUITECACHE.txt";
   Options options;
   String graaldir;
   String graalcache;
   HashMap<String, Long> files = new HashMap();
   public static HashMap<String, String> cache = new HashMap();
   private CacheWarning warning;

   public FileCache(Options options) {
      this.options = options;
      this.graaldir = this.options.graaldir;
      if (this.graaldir != null) {
         this.updateHash();
      }
   }

   public void updateHash() {
      this.graaldir = this.options.graaldir;
      System.out.println("Checking cache... " + this.graaldir + "\\" + "GRAALSUITECACHE.txt");
      File f = new File(this.graaldir + "\\" + "GRAALSUITECACHE.txt");
      if (!f.isFile()) {
         this.updateCacheFile();
      } else {
         System.out.println("GRAALSUITECACHE.txt found. Loading.");
         this.cacheFromFile("GRAALSUITECACHE.txt");
      }

   }

   public void cacheFromFile(String cachefile) {
      if (cachefile == null) {
         cachefile = "GRAALSUITECACHE.txt";
      }

      try {
         BufferedReader br = new BufferedReader(new FileReader(this.graaldir + "\\" + cachefile));
         String line = br.readLine();

         while((line = br.readLine()) != null) {
            String file = line.substring(0, line.indexOf(","));
            cache.put((new File(file)).getName().toString(), file);
         }
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public void updateCacheFile() {
      if (!this.graaldir.toLowerCase().contains("graal")) {
         System.out.println("Did not create cache as no Graal folder was found.");
      } else {
         System.out.println("Creating cache...");
         this.files = new HashMap();
         SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            protected Void doInBackground() throws InterruptedException {
               FileCache.this.listFiles(FileCache.this.graaldir, FileCache.this.files);

               try {
                  PrintWriter out = new PrintWriter(FileCache.this.graaldir + "\\" + "GRAALSUITECACHE.txt");
                  Set<Entry<String, Long>> setx = FileCache.this.files.entrySet();
                  Iterator iteratorx = setx.iterator();

                  while(true) {
                     if (!iteratorx.hasNext()) {
                        out.close();
                        System.out.println("Cache created... " + FileCache.this.graaldir + "\\" + "GRAALSUITECACHE.txt");
                        break;
                     }

                     Entry<String, Long> mentry = (Entry)iteratorx.next();
                     FileCache.cache.put((new File((String)mentry.getKey())).getName().toString(), (String)mentry.getKey());
                     out.println((String)mentry.getKey() + "," + mentry.getValue());
                  }
               } catch (FileNotFoundException var5) {
                  var5.printStackTrace();
                  return null;
               }

               Set<Entry<String, Long>> set = FileCache.this.files.entrySet();
               Iterator<Entry<String, Long>> iterator = set.iterator();
               FileCache.this.files.clear();
               FileCache.this.files = null;
               return null;
            }

            protected void done() {
            }
         };
         worker.execute();
         this.warning = new CacheWarning();
         worker.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
               if (evt.getPropertyName().equals("state") && evt.getNewValue() == StateValue.DONE) {
                  FileCache.this.warning.dispose();
               }

            }
         });
         worker.execute();
         this.warning.pack();
         this.warning.setLocationRelativeTo((Component)null);
         this.warning.setVisible(true);
      }
   }

   private void listFiles(String directoryName, HashMap<String, Long> files) {
      File directory = new File(directoryName);
      int dirlen = this.graaldir.length() + 1;
      String[] extensionlist = new String[]{".png", ".gif", ".mng", ".jpg", ".jpeg", ".gani"};
      List<String> validextensions = Arrays.asList(extensionlist);
      File[] fileList = directory.listFiles();
      if (fileList != null) {
         File[] var11 = fileList;
         int var10 = fileList.length;

         for(int var9 = 0; var9 < var10; ++var9) {
            File file = var11[var9];
            String filepath;
            String partialpath;
            if (file.isFile()) {
               filepath = file.getPath();
               if (filepath.contains(".")) {
                  partialpath = filepath.substring(filepath.lastIndexOf("."));
                  if (validextensions.contains(partialpath)) {
                     files.put(filepath, file.lastModified());
                     this.warning.setStatus(file.getAbsolutePath());
                  }
               }
            } else if (file.isDirectory()) {
               filepath = file.getAbsolutePath();
               partialpath = filepath.substring(dirlen);
               if (partialpath.startsWith("webfiles")) {
                  return;
               }

               if (partialpath.startsWith("weblevels")) {
                  return;
               }

               if (partialpath.startsWith("scriptfiles")) {
                  return;
               }

               if (partialpath.startsWith("profiles")) {
                  return;
               }

               this.listFiles(filepath, files);
            }
         }
      }

   }
}
