package com.dinkygames.graaleditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GraalEditor {
   public static final String TILESETPATH = "/res/images/pics1.png";
   public JFrame frame;
   public JLabel statusBarRight;
   public JLabel statusBarLeft;
   static String extra;
   short defaultTile = 511;
   public JTabbedPane contentPane;
   ScrollPane_Tileset scroll_tileset;
   FileCache filecache;
   private JComboBox<String> combobox_predefCategories;
   private JComboBox<String> combobox_predefObjects;
   public JTabbedPane tabbedPane;
   Options options;
   private JTable table;
   public PredefinedObject[] predefinedobjects;
   public PredefinedObjectLabel predefinedObject_Label;
   Long lasttap = 0L;
   public LevelLink_Dialog dialog_levellinks;
   public Signs_Dialog dialog_signs;
   private CreateLink_Dialog dialog_createlink;
   public JCheckBox toolbar_checkbox_links;
   public JCheckBox toolbar_checkbox_signs;
   public JCheckBox toolbar_checkbox_npcs;
   public JButton toolbar_button_cut;
   public JButton toolbar_button_copy;
   public JButton toolbar_button_delete;
   public JButton toolbar_button_fliph;
   public JButton toolbar_button_flipv;
   public JButton toolbar_button_addlink;
   public JButton toolbar_button_addsign;
   public JButton toolbar_button_hex;
   public JButton toolbar_button_close;
   public JButton toolbar_button_save;
   public JButton toolbar_button_saveas;
   public JButton toolbar_button_undo;
   public JButton toolbar_button_redo;
   public JButton toolbar_button_paste;
   public JButton toolbar_button_links;
   public JButton toolbar_button_signs;
   public JButton toolbar_button_addnpc;
   public JButton toolbar_button_npcs;
   public JButton btnDeleteLayer;
   public TileDefinitions tiledefs;
   JMenuItem menu_button_file_save;
   JMenuItem menu_button_file_saveas;
   JMenuItem menu_button_file_exit;
   JMenuItem menu_button_edit_cut;
   JMenuItem menu_button_edit_copy;
   JMenuItem menu_button_edit_paste;
   JMenuItem menu_button_edit_undo;
   JMenuItem menu_button_edit_redo;
   JMenuItem menu_button_edit_delete;
   Debug debugger;
   public JPanel predefinedobject_panel;
   public JPanel layerControlPanel;
   TilesetDefinitions_Dialog tiledef_dialog = null;
   public Entity clipboard;
   public JSlider layer_slider;
   public static BufferedImage thumbnailbase;
   private ScrollPane_Level lastLevelTab = null;

   public static void main(final String[] args) {
      Object var1 = null;

      try {
         new PrintStream(new FileOutputStream("graalsuite_output.log"));
      } catch (FileNotFoundException var3) {
      }

      extra = Arrays.toString(args);
      System.out.println("Launching...");
      System.out.println("Arguments: " + extra);
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               GraalEditor window;
               if (args.length > 0) {
                  if (args[0].endsWith(".nw")) {
                     window = new GraalEditor(args[0]);
                  } else {
                     window = new GraalEditor();
                  }
               } else {
                  window = new GraalEditor();
               }

               window.frame.setMinimumSize(new Dimension(780, 480));
               window.frame.setVisible(true);
               window.frame.setTitle("Graal Suite Beta v0.1.02");
               window.frame.setIconImage(ImageIO.read(GraalEditor.class.getResource("/res/graalico_editor.png")));
            } catch (Exception var2) {
               var2.printStackTrace();
            }

         }
      });
      Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
         public void run() {
         }
      }, "Shutdown-thread"));
   }

   public GraalEditor() {
      this.initialize();
   }

   public GraalEditor(String s) {
      if (s == null) {
         this.initialize();
      } else {
         this.initialize(s);
      }

   }

   private void initialize() {
      this.initialize((String)null);
   }

   public static void log(PrintWriter tolog, Exception e) {
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      String exceptionAsString = sw.toString();
      tolog.println(exceptionAsString);
   }

   public static void log(Exception e) {
   }

   public void updateTileDefinitions() {
      this.tiledefs.updateTileDefinitions(this.options.graaldir);
   }

   private void initialize(String s) {
      try {
         thumbnailbase = ImageIO.read(this.getClass().getResource("/res/images/thumbnailbase.png"));
      } catch (IOException var52) {
      }

      this.debugger = new Debug();
      this.debugger.println("Launching Arguments: " + extra);
      this.debugger.println("Initializing with opening file: " + s);
      this.options = new Options(this);
      this.filecache = new FileCache(this.options);
      this.debugger.println("Graal Directory set to: " + this.options.graaldir);
      this.tiledefs = new TileDefinitions(this.options.graaldir, this);
      this.frame = new JFrame();
      this.frame.setDefaultCloseOperation(3);
      this.frame.setBounds(this.options.mainwindow_position_x, this.options.mainwindow_position_y, this.options.mainwindow_size_width, this.options.mainwindow_size_height);
      this.frame.addComponentListener(new ComponentAdapter() {
         public void componentResized(ComponentEvent componentEvent) {
            GraalEditor.this.options.saveWindowResize(GraalEditor.this.frame.getWidth(), GraalEditor.this.frame.getHeight(), GraalEditor.this.frame.getExtendedState() == 6);
         }

         public void componentMoved(ComponentEvent componentEvent) {
            GraalEditor.this.options.saveWindowMove(GraalEditor.this.frame.getX(), GraalEditor.this.frame.getY());
         }
      });
      JMenuBar menuBar = new JMenuBar();
      this.frame.setJMenuBar(menuBar);
      JMenu menu_item_file = new JMenu("File");
      JMenu menu_item_edit = new JMenu("Edit");
      menuBar.add(menu_item_file);
      menuBar.add(menu_item_edit);
      JMenuItem menu_button_file_newnw = new JMenuItem("New NW");
      menu_button_file_newnw.setAccelerator(KeyStroke.getKeyStroke(78, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
      menu_item_file.add(menu_button_file_newnw);
      JMenuItem menu_button_file_newgmap = new JMenuItem("New GMAP");
      menu_button_file_newgmap.setEnabled(false);
      menu_item_file.add(menu_button_file_newgmap);
      JMenuItem menu_button_file_newgani = new JMenuItem("New GANI");
      menu_button_file_newgani.setEnabled(false);
      menu_item_file.add(menu_button_file_newgani);
      menu_item_file.addSeparator();
      JMenuItem menu_button_file_open = new JMenuItem("Open");
      menu_button_file_open.setAccelerator(KeyStroke.getKeyStroke(79, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
      menu_item_file.add(menu_button_file_open);
      this.menu_button_file_save = new JMenuItem("Save");
      this.menu_button_file_save.setAccelerator(KeyStroke.getKeyStroke(83, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
      menu_item_file.add(this.menu_button_file_save);
      this.menu_button_file_saveas = new JMenuItem("Save As");
      menu_item_file.add(this.menu_button_file_saveas);
      menu_item_file.addSeparator();
      this.menu_button_file_exit = new JMenuItem("Exit                      ");
      menu_item_file.add(this.menu_button_file_exit);
      this.menu_button_edit_undo = new JMenuItem("Undo");
      this.menu_button_edit_undo.setAccelerator(KeyStroke.getKeyStroke(90, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
      menu_item_edit.add(this.menu_button_edit_undo);
      this.menu_button_edit_redo = new JMenuItem("Redo");
      this.menu_button_edit_redo.setAccelerator(KeyStroke.getKeyStroke(89, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
      menu_item_edit.add(this.menu_button_edit_redo);
      menu_item_edit.addSeparator();
      this.menu_button_edit_cut = new JMenuItem("Cut");
      this.menu_button_edit_cut.setAccelerator(KeyStroke.getKeyStroke(88, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
      menu_item_edit.add(this.menu_button_edit_cut);
      this.menu_button_edit_copy = new JMenuItem("Copy");
      this.menu_button_edit_copy.setAccelerator(KeyStroke.getKeyStroke(67, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
      menu_item_edit.add(this.menu_button_edit_copy);
      this.menu_button_edit_paste = new JMenuItem("Paste");
      this.menu_button_edit_paste.setAccelerator(KeyStroke.getKeyStroke(86, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
      menu_item_edit.add(this.menu_button_edit_paste);
      this.menu_button_edit_delete = new JMenuItem("Delete");
      menu_item_edit.add(this.menu_button_edit_delete);
      menu_item_edit.addSeparator();
      JMenuItem menu_button_edit_tiledefs = new JMenuItem("Tile Definitions");
      menu_item_edit.add(menu_button_edit_tiledefs);
      menu_button_edit_tiledefs.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.openTileDefs();
         }
      });
      JMenuItem menu_button_edit_debug = new JMenuItem("Log Window");
      menu_item_edit.add(menu_button_edit_debug);
      menu_button_edit_debug.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.debugger.setVisible(true);
         }
      });
      this.menu_button_edit_cut.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.cut();
         }
      });
      this.menu_button_edit_copy.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.copy();
         }
      });
      this.menu_button_edit_paste.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.paste();
         }
      });
      this.menu_button_edit_delete.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.deleteSelection();
         }
      });
      JMenuItem menu_button_file_config = new JMenuItem("Preferences");
      menu_item_edit.add(menu_button_file_config);
      menu_button_file_config.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            new Options_Dialog(GraalEditor.this.options);
         }
      });
      menu_button_file_open.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.open();
         }
      });
      menu_button_file_newnw.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.newLevel();
         }
      });
      this.menu_button_file_exit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            System.exit(0);
         }
      });
      this.menu_button_file_save.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.saveCurrentLevel();
         }
      });
      this.menu_button_file_saveas.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.saveCurrentLevelAs();
         }
      });
      this.menu_button_edit_undo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.getCurrentItem().canvas.scroll.undo();
         }
      });
      this.menu_button_edit_redo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.getCurrentItem().canvas.scroll.redo();
         }
      });
      JToolBar toolBar = new JToolBar();
      this.frame.getContentPane().add(toolBar, "North");
      JPanel statusBar = new JPanel();
      statusBar.setPreferredSize(new Dimension(statusBar.getPreferredSize().width, 24));
      this.frame.getContentPane().add(statusBar, "South");
      SpringLayout sl_statusBar = new SpringLayout();
      statusBar.setLayout(sl_statusBar);
      JLabel statusRight = new JLabel("Status Bar Right ");
      statusRight.setHorizontalAlignment(4);
      sl_statusBar.putConstraint("North", statusRight, 0, "North", statusBar);
      sl_statusBar.putConstraint("West", statusRight, -159, "East", statusBar);
      sl_statusBar.putConstraint("South", statusRight, 24, "North", statusBar);
      sl_statusBar.putConstraint("East", statusRight, 0, "East", statusBar);
      statusRight.setBorder(new CompoundBorder(new EmptyBorder(new Insets(1, 4, 1, 1)), BorderFactory.createLoweredBevelBorder()));
      statusBar.add(statusRight);
      JLabel statusLeft = new JLabel("Status Bar Left");
      sl_statusBar.putConstraint("North", statusLeft, 0, "North", statusRight);
      sl_statusBar.putConstraint("West", statusLeft, 10, "West", statusBar);
      sl_statusBar.putConstraint("South", statusLeft, 24, "North", statusBar);
      sl_statusBar.putConstraint("East", statusLeft, 210, "West", statusBar);
      statusBar.add(statusLeft);
      JButton toolbar_button_newnw = new JButton("");
      toolbar_button_newnw.setToolTipText("New NW");
      toolbar_button_newnw.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_newnw.png")));
      this.toolbar_button_close = new JButton("");
      this.toolbar_button_close.setToolTipText("Close");
      this.toolbar_button_close.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_close.png")));
      toolBar.setFloatable(false);
      toolBar.add(toolbar_button_newnw);
      JButton button = new JButton("");
      button.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_newgmap.png")));
      button.setEnabled(false);
      button.setToolTipText("New GMAP");
      toolBar.add(button);
      JButton button_1 = new JButton("");
      button_1.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_newgani.png")));
      button_1.setToolTipText("New GANI");
      button_1.setEnabled(false);
      toolBar.add(button_1);
      toolBar.add(this.toolbar_button_close);
      Component horizontalStrut = Box.createRigidArea(new Dimension(20, 20));
      toolBar.add(horizontalStrut);
      JButton toolbar_button_open = new JButton("");
      toolbar_button_open.setToolTipText("Open");
      toolbar_button_open.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_open.png")));
      toolBar.add(toolbar_button_open);
      this.toolbar_button_save = new JButton("");
      this.toolbar_button_save.setToolTipText("Save");
      this.toolbar_button_save.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_save.png")));
      toolBar.add(this.toolbar_button_save);
      this.toolbar_button_saveas = new JButton("");
      this.toolbar_button_saveas.setToolTipText("Save As");
      this.toolbar_button_saveas.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_saveas.png")));
      toolBar.add(this.toolbar_button_saveas);
      Component horizontalStrut_1 = Box.createRigidArea(new Dimension(20, 20));
      toolBar.add(horizontalStrut_1);
      this.toolbar_button_undo = new JButton("");
      this.toolbar_button_undo.setToolTipText("Undo");
      this.toolbar_button_undo.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_undo.png")));
      this.toolbar_button_undo.setEnabled(false);
      toolBar.add(this.toolbar_button_undo);
      this.toolbar_button_undo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.getCurrentItem().canvas.scroll.undo();
         }
      });
      this.toolbar_button_redo = new JButton("");
      this.toolbar_button_redo.setToolTipText("Redo");
      this.toolbar_button_redo.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_redo.png")));
      this.toolbar_button_redo.setEnabled(false);
      toolBar.add(this.toolbar_button_redo);
      this.toolbar_button_redo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.getCurrentItem().canvas.scroll.redo();
         }
      });
      Component horizontalStrut_2 = Box.createRigidArea(new Dimension(20, 20));
      toolBar.add(horizontalStrut_2);
      this.toolbar_button_cut = new JButton("");
      this.toolbar_button_cut.setToolTipText("Cut");
      this.toolbar_button_cut.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_cut.png")));
      toolBar.add(this.toolbar_button_cut);
      this.toolbar_button_cut.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.cut();
         }
      });
      this.toolbar_button_copy = new JButton("");
      this.toolbar_button_copy.setToolTipText("Copy");
      this.toolbar_button_copy.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_copy.png")));
      toolBar.add(this.toolbar_button_copy);
      this.toolbar_button_copy.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.copy();
         }
      });
      this.toolbar_button_paste = new JButton("");
      this.toolbar_button_paste.setToolTipText("Paste");
      this.toolbar_button_paste.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_paste.png")));
      toolBar.add(this.toolbar_button_paste);
      this.toolbar_button_paste.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.paste();
         }
      });
      this.toolbar_button_delete = new JButton("");
      this.toolbar_button_delete.setToolTipText("Delete");
      this.toolbar_button_delete.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_delete.png")));
      toolBar.add(this.toolbar_button_delete);
      this.toolbar_button_delete.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            GraalEditor.this.deleteSelection();
         }
      });
      Component horizontalStrut_6 = Box.createRigidArea(new Dimension(10, 20));
      toolBar.add(horizontalStrut_6);
      this.toolbar_button_fliph = new JButton("");
      this.toolbar_button_fliph.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_fliphorz.png")));
      this.toolbar_button_fliph.setToolTipText("Flip Horizontally");
      this.toolbar_button_fliph.setEnabled(false);
      toolBar.add(this.toolbar_button_fliph);
      this.toolbar_button_flipv = new JButton("");
      this.toolbar_button_flipv.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_flipvert.png")));
      this.toolbar_button_flipv.setToolTipText("Flip Vertically");
      this.toolbar_button_flipv.setEnabled(false);
      toolBar.add(this.toolbar_button_flipv);
      Component horizontalStrut_3 = Box.createRigidArea(new Dimension(48, 20));
      toolBar.add(horizontalStrut_3);
      this.toolbar_button_addlink = new JButton("");
      this.toolbar_button_addlink.setToolTipText("Create Link");
      this.toolbar_button_addlink.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_linkadd.png")));
      toolBar.add(this.toolbar_button_addlink);
      this.toolbar_button_links = new JButton("");
      this.toolbar_button_links.setToolTipText("View Links");
      this.toolbar_button_links.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_links.png")));
      toolBar.add(this.toolbar_button_links);
      this.toolbar_checkbox_links = new JCheckBox("");
      this.toolbar_checkbox_links.setSelected(this.options.togglevis_links);
      this.toolbar_checkbox_links.setToolTipText("Toggle Links");
      toolBar.add(this.toolbar_checkbox_links);
      Component horizontalStrut_4 = Box.createRigidArea(new Dimension(10, 20));
      toolBar.add(horizontalStrut_4);
      this.toolbar_button_addsign = new JButton("");
      this.toolbar_button_addsign.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_signadd.png")));
      this.toolbar_button_addsign.setToolTipText("Create Sign");
      toolBar.add(this.toolbar_button_addsign);
      this.toolbar_button_signs = new JButton("");
      this.toolbar_button_signs.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_signs.png")));
      this.toolbar_button_signs.setToolTipText("View Signs");
      toolBar.add(this.toolbar_button_signs);
      this.toolbar_checkbox_signs = new JCheckBox("");
      this.toolbar_checkbox_signs.setSelected(this.options.togglevis_signs);
      this.toolbar_checkbox_signs.setToolTipText("Toggle Signs");
      toolBar.add(this.toolbar_checkbox_signs);
      Component horizontalStrut_5 = Box.createRigidArea(new Dimension(10, 20));
      toolBar.add(horizontalStrut_5);
      this.toolbar_button_addnpc = new JButton("");
      this.toolbar_button_addnpc.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_npcadd.png")));
      this.toolbar_button_addnpc.setToolTipText("Create NPC");
      toolBar.add(this.toolbar_button_addnpc);
      this.toolbar_button_npcs = new JButton("");
      this.toolbar_button_npcs.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_npcs.png")));
      this.toolbar_button_npcs.setToolTipText("View NPCs");
      toolBar.add(this.toolbar_button_npcs);
      this.toolbar_checkbox_npcs = new JCheckBox("");
      this.toolbar_checkbox_npcs.setSelected(this.options.togglevis_npcs);
      this.toolbar_checkbox_npcs.setToolTipText("Toggle NPCs");
      toolBar.add(this.toolbar_checkbox_npcs);
      Component horizontalStrut_7 = Box.createRigidArea(new Dimension(32, 20));
      toolBar.add(horizontalStrut_7);
      JButton toolbar_button_tiledef = new JButton("");
      toolbar_button_tiledef.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_tiledefs.png")));
      toolbar_button_tiledef.setToolTipText("Edit Tile Definitions");
      toolBar.add(toolbar_button_tiledef);
      this.toolbar_button_hex = new JButton("");
      this.toolbar_button_hex.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_hex.png")));
      this.toolbar_button_hex.setToolTipText("Convert Tile Selection to Hex");
      toolBar.add(this.toolbar_button_hex);
      toolbar_button_tiledef.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.openTileDefs();
         }
      });
      toolbar_button_newnw.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.newLevel();
         }
      });
      toolbar_button_open.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.open();
         }
      });
      this.toolbar_button_save.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.saveCurrentLevel();
         }
      });
      this.toolbar_button_saveas.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.saveCurrentLevelAs();
         }
      });
      this.toolbar_button_close.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.closeCurrentTab();
         }
      });
      this.toolbar_button_addsign.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ScrollPane_Level cur = GraalEditor.this.getCurrentItem();
            if (cur != null) {
               if (cur.canvas.tileselection != null) {
                  int x = cur.canvas.tileselection_ox;
                  int y = cur.canvas.tileselection_oy;
                  cur.canvas.clearTileSelection();
                  Sign sign = new Sign("<New Text>", x, y);
                  if (GraalEditor.this.dialog_signs == null) {
                     GraalEditor.this.dialog_signs = new Signs_Dialog(GraalEditor.this.getCurrentItem().level, sign);
                  } else {
                     GraalEditor.this.dialog_signs.addNewSign(sign);
                     GraalEditor.this.dialog_signs.requestFocus();
                  }

               }
            }
         }
      });
      this.toolbar_button_signs.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if (GraalEditor.this.dialog_signs == null) {
               GraalEditor.this.dialog_signs = new Signs_Dialog(GraalEditor.this.getCurrentItem().level, (Sign)null);
            } else {
               GraalEditor.this.dialog_signs.requestFocus();
            }

         }
      });
      this.toolbar_button_addlink.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if (GraalEditor.this.dialog_levellinks != null) {
               GraalEditor.this.dialog_levellinks.dispose();
            }

            GraalEditor.this.getCurrentItem().createNewLink();
         }
      });
      this.toolbar_button_links.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if (GraalEditor.this.dialog_levellinks == null) {
               GraalEditor.this.dialog_levellinks = new LevelLink_Dialog(GraalEditor.this.getCurrentItem().level);
            } else {
               GraalEditor.this.dialog_levellinks.requestFocus();
            }

         }
      });
      this.toolbar_checkbox_links.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.options.toggleLinkVisibility(GraalEditor.this.toolbar_checkbox_links.isSelected());
            GraalEditor.this.getCurrentItem().canvas.repaint();
         }
      });
      this.toolbar_checkbox_signs.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.options.toggleSignVisibility(GraalEditor.this.toolbar_checkbox_signs.isSelected());
            GraalEditor.this.getCurrentItem().canvas.repaint();
         }
      });
      this.toolbar_checkbox_npcs.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.options.toggleNPCVisibility(GraalEditor.this.toolbar_checkbox_npcs.isSelected());
            GraalEditor.this.getCurrentItem().canvas.repaint();
         }
      });
      this.toolbar_button_hex.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.getCurrentItem().canvas.getHexCode();
         }
      });
      JSplitPane splitPane = new JSplitPane();
      splitPane.setDoubleBuffered(true);
      this.frame.getContentPane().add(splitPane, "Center");
      JTabbedPane tabbedPane = new JTabbedPane(4);
      splitPane.setRightComponent(tabbedPane);
      this.scroll_tileset = new ScrollPane_Tileset(this);
      TextIcon tico = new TextIcon(tabbedPane, "TILES", TextIcon.Layout.VERTICAL);
      ImageIcon image = new ImageIcon("ico_temp.png");
      CompoundIcon ci_tileset = new CompoundIcon(CompoundIcon.Axis.Y_AXIS, new Icon[]{image, tico});
      JPanel panel_predefinedobjects = new JPanel();
      tico = new TextIcon(tabbedPane, "PREDEFINED TILES", TextIcon.Layout.VERTICAL);
      image = new ImageIcon("ico_temp.png");
      CompoundIcon ci_predef = new CompoundIcon(CompoundIcon.Axis.Y_AXIS, new Icon[]{image, tico});
      JPanel tab_npcs = new JPanel();
      tico = new TextIcon(tabbedPane, "NPCS", TextIcon.Layout.VERTICAL);
      image = new ImageIcon("ico_temp.png");
      CompoundIcon ci_npcs = new CompoundIcon(CompoundIcon.Axis.Y_AXIS, new Icon[]{image, tico});
      JPanel tab_layers = new JPanel();
      tico = new TextIcon(tabbedPane, "LAYERS", TextIcon.Layout.VERTICAL);
      image = new ImageIcon("ico_temp.png");
      CompoundIcon ci_layers = new CompoundIcon(CompoundIcon.Axis.Y_AXIS, new Icon[]{image, tico});
      tabbedPane.addTab("", ci_tileset, this.scroll_tileset, (String)null);
      tabbedPane.addTab("", ci_predef, panel_predefinedobjects, (String)null);
      tabbedPane.addTab("", ci_npcs, tab_npcs, (String)null);
      tab_npcs.setLayout((LayoutManager)null);
      JPanel panel_1 = new JPanel();
      panel_1.setBounds(10, 10, 176, 230);
      FlowLayout flowLayout = (FlowLayout)panel_1.getLayout();
      flowLayout.setVgap(0);
      flowLayout.setHgap(0);
      flowLayout.setAlignment(0);
      tab_npcs.add(panel_1);
      Opps opps = new Opps(this);
      panel_1.add(opps);
      tabbedPane.addTab("", ci_layers, tab_layers, (String)null);
      tab_layers.setLayout(new BoxLayout(tab_layers, 1));
      JToolBar toolBar_layers = new JToolBar();
      toolBar_layers.setAlignmentX(1.0F);
      tab_layers.add(toolBar_layers);
      toolBar_layers.setFloatable(false);
      JButton btnAddLayer = new JButton("Add");
      toolBar_layers.add(btnAddLayer);
      btnAddLayer.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.getCurrentItem().level.addLayer();
            GraalEditor.this.updateLayerPanel();
         }
      });
      this.btnDeleteLayer = new JButton("Delete");
      toolBar_layers.add(this.btnDeleteLayer);
      this.btnDeleteLayer.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            GraalEditor.this.getCurrentItem().level.deleteLayer(GraalEditor.this.getCurrentItem().selectedLayer);
            GraalEditor.this.updateLayerPanel();
         }
      });
      this.layer_slider = new JSlider();
      this.layer_slider.setBorder(new EtchedBorder(1, (Color)null, (Color)null));
      this.layer_slider.setValue(100);
      toolBar_layers.add(this.layer_slider);
      this.layer_slider.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent arg0) {
            JSlider source = (JSlider)arg0.getSource();
            float value = (float)source.getValue() / 100.0F;
            ((Layer)GraalEditor.this.getCurrentItem().level.layers.get(GraalEditor.this.getCurrentItem().selectedLayer)).alpha = value;
            GraalEditor.this.getCurrentItem().canvas.repaint();
         }
      });
      JScrollPane scrollPane = new JScrollPane(this.table);
      scrollPane.setViewportBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
      scrollPane.setVerticalScrollBarPolicy(22);
      scrollPane.setHorizontalScrollBarPolicy(31);
      tab_layers.add(scrollPane);
      this.layerControlPanel = new JPanel();
      scrollPane.setViewportView(this.layerControlPanel);
      this.layerControlPanel.setLayout(new BoxLayout(this.layerControlPanel, 1));
      SpringLayout sl_panel_predefinedobjects = new SpringLayout();
      panel_predefinedobjects.setLayout(sl_panel_predefinedobjects);
      this.combobox_predefCategories = new JComboBox();
      sl_panel_predefinedobjects.putConstraint("North", this.combobox_predefCategories, 7, "North", panel_predefinedobjects);
      sl_panel_predefinedobjects.putConstraint("East", this.combobox_predefCategories, -74, "East", panel_predefinedobjects);
      this.combobox_predefCategories.setEditable(false);
      panel_predefinedobjects.add(this.combobox_predefCategories);
      JLabel lblNewLabel = new JLabel("Group:");
      sl_panel_predefinedobjects.putConstraint("West", this.combobox_predefCategories, 6, "East", lblNewLabel);
      sl_panel_predefinedobjects.putConstraint("North", lblNewLabel, 10, "North", panel_predefinedobjects);
      sl_panel_predefinedobjects.putConstraint("West", lblNewLabel, 10, "West", panel_predefinedobjects);
      panel_predefinedobjects.add(lblNewLabel);
      this.combobox_predefObjects = new JComboBox();
      sl_panel_predefinedobjects.putConstraint("East", this.combobox_predefObjects, 0, "East", this.combobox_predefCategories);
      this.combobox_predefObjects.setEditable(false);
      panel_predefinedobjects.add(this.combobox_predefObjects);
      JLabel lblObject = new JLabel("Object");
      sl_panel_predefinedobjects.putConstraint("North", lblObject, 17, "South", lblNewLabel);
      sl_panel_predefinedobjects.putConstraint("West", lblObject, 10, "West", panel_predefinedobjects);
      sl_panel_predefinedobjects.putConstraint("North", this.combobox_predefObjects, -3, "North", lblObject);
      sl_panel_predefinedobjects.putConstraint("West", this.combobox_predefObjects, 7, "East", lblObject);
      panel_predefinedobjects.add(lblObject);
      JButton predef_btn_new = new JButton("");
      predef_btn_new.setContentAreaFilled(false);
      sl_panel_predefinedobjects.putConstraint("North", predef_btn_new, -2, "North", this.combobox_predefCategories);
      sl_panel_predefinedobjects.putConstraint("West", predef_btn_new, 3, "East", this.combobox_predefCategories);
      sl_panel_predefinedobjects.putConstraint("South", predef_btn_new, 22, "North", this.combobox_predefCategories);
      sl_panel_predefinedobjects.putConstraint("East", predef_btn_new, 25, "East", this.combobox_predefCategories);
      predef_btn_new.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_newnw.png")));
      panel_predefinedobjects.add(predef_btn_new);
      JButton predef_btn_save = new JButton("");
      predef_btn_save.setContentAreaFilled(false);
      sl_panel_predefinedobjects.putConstraint("North", predef_btn_save, 0, "North", predef_btn_new);
      sl_panel_predefinedobjects.putConstraint("West", predef_btn_save, 3, "East", predef_btn_new);
      sl_panel_predefinedobjects.putConstraint("South", predef_btn_save, 24, "North", predef_btn_new);
      sl_panel_predefinedobjects.putConstraint("East", predef_btn_save, 25, "East", predef_btn_new);
      predef_btn_save.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_save.png")));
      panel_predefinedobjects.add(predef_btn_save);
      this.predefinedobject_panel = new JPanel();
      sl_panel_predefinedobjects.putConstraint("North", this.predefinedobject_panel, 10, "South", this.combobox_predefObjects);
      sl_panel_predefinedobjects.putConstraint("West", this.predefinedobject_panel, 10, "West", panel_predefinedobjects);
      sl_panel_predefinedobjects.putConstraint("South", this.predefinedobject_panel, -10, "South", panel_predefinedobjects);
      sl_panel_predefinedobjects.putConstraint("East", this.predefinedobject_panel, -10, "East", panel_predefinedobjects);
      this.predefinedobject_panel.setBorder((Border)null);
      panel_predefinedobjects.add(this.predefinedobject_panel);
      this.contentPane = new JTabbedPane(1);
      this.contentPane.setTabLayoutPolicy(1);
      splitPane.setLeftComponent(this.contentPane);
      this.contentPane.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            JTabbedPane tabP = (JTabbedPane)e.getSource();
            int currIndex = tabP.getSelectedIndex();
            if (currIndex >= 0) {
               ScrollPane_Level tab = (ScrollPane_Level)GraalEditor.this.contentPane.getComponentAt(currIndex);
               if (tab != null) {
                  if (GraalEditor.this.lastLevelTab != null && !tab.equals(GraalEditor.this.lastLevelTab)) {
                     GraalEditor.this.lastLevelTab.cleanup();
                  }

                  tab.tilesetpane.updateTileset(true);
                  GraalEditor.this.updateLayerPanel();
                  GraalEditor.this.lastLevelTab = tab;
               }
            }
         }
      });
      this.contentPane.addMouseListener(new CustomAdapter(this, this.contentPane, this.scroll_tileset) {
      });
      splitPane.setResizeWeight(0.75D);
      tabbedPane.setMinimumSize(new Dimension(300, 600));
      splitPane.setResizeWeight(0.9900000095367432D);
      this.statusBarRight = statusRight;
      this.statusBarLeft = statusLeft;
      this.statusBarLeft.setText(extra);
      this.loadPredefinedObjects((String)null);
      this.combobox_predefCategories.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            Long currentTime = System.currentTimeMillis() / 1000L;
            if (!((float)currentTime < (float)GraalEditor.this.lasttap + 0.01F)) {
               if (e.getStateChange() == 1) {
                  GraalEditor.this.loadPredefinedObjects((String)GraalEditor.this.combobox_predefCategories.getSelectedItem());
                  GraalEditor.this.updatePredefinedObjectLabel(GraalEditor.this.predefinedobject_panel);
                  GraalEditor.this.lasttap = currentTime;
               }

            }
         }
      });
      this.combobox_predefCategories.addMouseWheelListener(new MouseWheelListener() {
         public void mouseWheelMoved(MouseWheelEvent arg0) {
            int newindex = Math.max(0, Math.min(GraalEditor.this.combobox_predefCategories.getItemCount() - 1, GraalEditor.this.combobox_predefCategories.getSelectedIndex() + arg0.getWheelRotation()));
            GraalEditor.this.combobox_predefCategories.setSelectedIndex(newindex);
         }
      });
      this.combobox_predefObjects.addMouseWheelListener(new MouseWheelListener() {
         public void mouseWheelMoved(MouseWheelEvent arg0) {
            int newindex = Math.max(0, Math.min(GraalEditor.this.combobox_predefObjects.getItemCount() - 1, GraalEditor.this.combobox_predefObjects.getSelectedIndex() + arg0.getWheelRotation()));
            GraalEditor.this.combobox_predefObjects.setSelectedIndex(newindex);
            GraalEditor.this.updatePredefinedObjectLabel(GraalEditor.this.predefinedobject_panel);
            GraalEditor.this.frame.repaint();
         }
      });
      this.combobox_predefObjects.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == 1) {
               Long currentTime = System.currentTimeMillis() / 1000L;
               if ((float)currentTime < (float)GraalEditor.this.lasttap + 0.01F) {
                  return;
               }

               GraalEditor.this.lasttap = currentTime;
               GraalEditor.this.updatePredefinedObjectLabel(GraalEditor.this.predefinedobject_panel);
            }

         }
      });
      this.predefinedobject_panel.addComponentListener(new ComponentAdapter() {
         public void componentResized(ComponentEvent e) {
            if (GraalEditor.this.predefinedObject_Label != null) {
               GraalEditor.this.predefinedObject_Label.rescale();
            }

         }
      });
      this.combobox_predefCategories.setEnabled(this.combobox_predefCategories.getItemCount() > 0);
      this.combobox_predefObjects.setEnabled(this.combobox_predefObjects.getItemCount() > 0);
      if (this.combobox_predefObjects.getItemCount() > 0) {
         this.combobox_predefObjects.setSelectedIndex(0);
         this.updatePredefinedObjectLabel(this.predefinedobject_panel);
      }

      this.frame.setBounds(this.options.mainwindow_position_x, this.options.mainwindow_position_y, this.options.mainwindow_size_width, this.options.mainwindow_size_height);
      this.frame.setExtendedState(this.options.mainwindow_size_maximized ? 6 : 0);
      if (s == null) {
         this.newLevel();
      } else {
         this.openLevel(s);
      }

   }

   public void openTileDefs() {
      if (this.tiledef_dialog == null) {
         this.tiledef_dialog = new TilesetDefinitions_Dialog(this.tiledefs);
      } else {
         this.tiledef_dialog.requestFocus();
      }

   }

   public List<Object[]> getTileDefinitions() {
      return this.tiledefs == null ? null : this.tiledefs.tiledefinitions;
   }

   public void updatePredefinedList() {
      this.loadPredefinedObjects((String)null);
      this.combobox_predefCategories.setEnabled(this.combobox_predefCategories.getItemCount() > 0);
      this.combobox_predefObjects.setEnabled(this.combobox_predefObjects.getItemCount() > 0);
      if (this.combobox_predefObjects.getItemCount() > 0) {
         this.combobox_predefObjects.setSelectedIndex(0);
         this.updatePredefinedObjectLabel(this.predefinedobject_panel);
      }

   }

   public void updateLayerPanel() {
      this.layerControlPanel.removeAll();
      int layercount = this.getCurrentItem().level.layers.size();
      LayerPanel[] layers = new LayerPanel[layercount];

      for(int i = layercount - 1; i >= 0; --i) {
         layers[i] = new LayerPanel(this, i);
         layers[i].updateVisibilityCheckbox(((Layer)this.getCurrentItem().level.layers.get(i)).visible);
         this.layerControlPanel.add(layers[i]);
         layers[i].updateName(((Layer)this.getCurrentItem().level.layers.get(i)).name);
         layers[i].updateThumbnail(((Layer)this.getCurrentItem().level.layers.get(i)).getTileRender());
      }

      if (this.getCurrentItem().selectedLayer < 0) {
         this.getCurrentItem().selectedLayer = 0;
      }

      layers[this.getCurrentItem().selectedLayer].setSelected(true);
      this.btnDeleteLayer.setEnabled(this.getCurrentItem().selectedLayer > 0);
      this.layerControlPanel.repaint();
      this.frame.repaint();
   }

   public void updateLayerPanelThumbnails() {
      Component[] panels = this.layerControlPanel.getComponents();
      Component[] var5 = panels;
      int var4 = panels.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Component i = var5[var3];
         LayerPanel panel = (LayerPanel)i;
         if (panel != null) {
            panel.updateThumbnail(((Layer)this.getCurrentItem().level.layers.get(panel.layerindex)).getTileRender());
         }
      }

   }

   public void updateLayerPanelThumbnail(int index) {
      Component[] panels = this.layerControlPanel.getComponents();
      Component[] var6 = panels;
      int var5 = panels.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Component i = var6[var4];
         LayerPanel panel = (LayerPanel)i;
         if (panel != null && panel.layerindex == index) {
            panel.updateThumbnail(((Layer)this.getCurrentItem().level.layers.get(panel.layerindex)).getTileRender());
            break;
         }
      }

   }

   public void changeSelectedLayer(int index) {
      this.getCurrentItem().selectedLayer = index;
      Component[] var5;
      int var4 = (var5 = this.layerControlPanel.getComponents()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Component i = var5[var3];
         if (i.getClass().toString().endsWith("LayerPanel")) {
            LayerPanel panel = (LayerPanel)i;
            panel.setSelected(panel.layerindex == index);
         }
      }

      Layer cur = (Layer)this.getCurrentItem().level.layers.get(index);
      this.layer_slider.setValue((int)(cur.alpha * 100.0F));
      this.btnDeleteLayer.setEnabled(this.getCurrentItem().selectedLayer > 0);
   }

   public void deleteSelection() {
      this.getCurrentItem().canvas.deleteSelection(false);
   }

   public void updateTileset() {
      this.getCurrentItem().tilesetpane.updateTileset(true);
      if (this.predefinedObject_Label != null) {
         this.predefinedObject_Label.update();
      }

   }

   public void updateTilesets(BufferedImage tileset) {
      this.getCurrentItem().canvas.updateTileset(tileset);
      if (this.predefinedObject_Label != null) {
         this.predefinedObject_Label.update();
      }

   }

   public void toggleButtonEnabled(JButton obj, boolean b) {
      obj.setEnabled(b);
   }

   private final void updatePredefinedObjectLabel(JPanel parent) {
      int ind = this.combobox_predefObjects.getSelectedIndex();
      if (ind >= 0) {
         if (this.predefinedobjects[ind] != null) {
            parent.removeAll();
            this.predefinedObject_Label = new PredefinedObjectLabel(this.predefinedobjects[ind], parent, this);
            parent.add(this.predefinedObject_Label);
            parent.repaint();
            this.frame.repaint();
         }
      }
   }

   public void newLevel() {
      this.contentPane.addTab("<untitled>", new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_newnw.png")), new ScrollPane_Level("/res/levels/new.nw", this.scroll_tileset, this), (String)null);
      this.contentPane.setSelectedIndex(this.contentPane.getTabCount() - 1);
      this.updateIOButtons();
   }

   public void openLevel(String level) {
      this.contentPane.addTab((new File(level)).getName(), new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_newnw.png")), new ScrollPane_Level(level, this.scroll_tileset, this), (String)null);
      this.contentPane.setSelectedIndex(this.contentPane.getTabCount() - 1);
      this.updateIOButtons();
   }

   public ScrollPane_Level getCurrentItem() {
      if (this.contentPane == null) {
         return null;
      } else {
         return this.contentPane.getComponentCount() <= 0 ? null : (ScrollPane_Level)this.contentPane.getSelectedComponent();
      }
   }

   public void closeCurrentTab() {
      if (this.contentPane.getTabCount() > 0) {
         this.closeTab(this.contentPane.getSelectedIndex());
         this.updateIOButtons();
      }
   }

   public void closeTab(int index) {
      if (this.contentPane.getTabCount() > 0) {
         Boolean edited = ((ScrollPane_Level)this.contentPane.getComponentAt(index)).level.edited;
         if (edited) {
            int result = JOptionPane.showConfirmDialog((Component)null, "Save Changes?", "  Warning", 1);
            if (result == 0) {
               this.saveLevelAs(index);
            } else if (result == 2) {
               return;
            }
         }

         ((ScrollPane_Level)this.contentPane.getComponentAt(index)).close();
         this.contentPane.remove(index);
         this.updateIOButtons();
      }
   }

   public void cut() {
      if (this.contentPane.getTabCount() > 0) {
         this.getCurrentItem().canvas.cut();
      }
   }

   public void copy() {
      if (this.contentPane.getTabCount() > 0) {
         this.getCurrentItem().canvas.copy();
      }
   }

   public void paste() {
      if (this.contentPane.getTabCount() > 0) {
         this.getCurrentItem().canvas.paste();
      }
   }

   public void open() {
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Graal Files(*.nw, *.gmap)", new String[]{"nw", "gmap"});
      chooser.setFileFilter(filter);
      int returnVal = chooser.showOpenDialog(this.frame);
      if (returnVal == 0) {
         String newfile = chooser.getSelectedFile().toString();
         this.contentPane.addTab((new File(newfile)).getName(), (Icon)null, new ScrollPane_Level(newfile, this.scroll_tileset, this), (String)null);
         this.contentPane.setSelectedIndex(this.contentPane.getTabCount() - 1);
      }

      this.updateIOButtons();
   }

   public void saveTabLevel(int index, String filename) {
      if (this.contentPane.getTabCount() > 0) {
         Level level = ((ScrollPane_Level)this.contentPane.getComponentAt(index)).level;
         level.saveLevel(filename);
         level.scroll.canvas.setEdited(false);
         this.updateIOButtons();
      }
   }

   public void saveTabLevel(int index) {
      if (this.contentPane.getTabCount() > 0) {
         this.saveTabLevel(index, (String)null);
         this.updateIOButtons();
      }
   }

   public void saveCurrentLevel() {
      if (this.contentPane.getTabCount() > 0) {
         if (this.getCurrentItem().level.levelname == null) {
            this.saveCurrentLevelAs();
         } else {
            this.saveTabLevel(this.contentPane.getSelectedIndex());
            this.updateIOButtons();
         }
      }
   }

   public void saveLevelAs(int index) {
      if (this.contentPane.getTabCount() > 0) {
         JFileChooser fileChooser = new JFileChooser();
         fileChooser.setDialogTitle("Save Graal Level");
         FileNameExtensionFilter filter = new FileNameExtensionFilter("Graal Files(*.nw)", new String[]{"nw"});
         fileChooser.setFileFilter(filter);
         int choice = fileChooser.showSaveDialog(this.frame);
         if (choice == 0) {
            File file = fileChooser.getSelectedFile();
            String filepath = file.getAbsolutePath();
            if (filepath.lastIndexOf(".") >= 0) {
               filepath = filepath.substring(0, filepath.lastIndexOf("."));
            }

            filepath = filepath + ".nw";
            Boolean existsAlready = (new File(filepath)).exists();
            if (existsAlready) {
               int dialogResult = JOptionPane.showConfirmDialog((Component)null, filepath + " already exists.\nDo you want to replace it?", "Warning", 0);
               if (dialogResult == 1) {
                  this.saveLevelAs(index);
                  return;
               }
            }

            this.saveTabLevel(index, filepath);
         }

         this.updateIOButtons();
      }
   }

   public void saveCurrentLevelAs() {
      this.saveLevelAs(this.contentPane.getSelectedIndex());
   }

   public void updateIOButtons() {
      boolean enabled = this.contentPane.getTabCount() > 0;
      this.toolbar_button_close.setEnabled(enabled);
      this.toolbar_button_save.setEnabled(enabled);
      this.toolbar_button_saveas.setEnabled(enabled);
      this.toolbar_button_paste.setEnabled(enabled);
      this.toolbar_button_links.setEnabled(enabled);
      this.toolbar_button_signs.setEnabled(enabled);
      this.toolbar_button_addsign.setEnabled(enabled);
      this.toolbar_button_addnpc.setEnabled(false);
      this.toolbar_button_npcs.setEnabled(false);
      this.toolbar_checkbox_links.setEnabled(enabled);
      this.toolbar_checkbox_signs.setEnabled(enabled);
      this.toolbar_checkbox_npcs.setEnabled(enabled);
      this.menu_button_file_save.setEnabled(enabled);
      this.menu_button_file_saveas.setEnabled(enabled);
      this.menu_button_file_exit.setEnabled(enabled);
      this.menu_button_edit_cut.setEnabled(enabled);
      this.menu_button_edit_copy.setEnabled(enabled);
      this.menu_button_edit_paste.setEnabled(enabled);
      this.menu_button_edit_undo.setEnabled(enabled);
      this.menu_button_edit_redo.setEnabled(enabled);
      this.menu_button_edit_delete.setEnabled(enabled);
      if (this.contentPane.getTabCount() > 0) {
         this.toolbar_button_undo.setEnabled(this.getCurrentItem().undohandler.undoStack.size() > 0);
         this.toolbar_button_redo.setEnabled(this.getCurrentItem().undohandler.redoStack.size() > 0);
      } else {
         this.toolbar_button_undo.setEnabled(false);
         this.toolbar_button_redo.setEnabled(false);
      }

   }

   public void updateDefaultTile(short t) {
      this.defaultTile = t;
      this.scroll_tileset.tilesetico.repaint();
   }

   public final void setStatusBarRight(String text) {
      this.statusBarRight.setText(text + " ");
   }

   public void loadPredefinedObjects(String selectedgroup) {
      if (this.options.graaldir != null) {
         File file;
         int index;
         String line;
         if (selectedgroup == null) {
            file = new File(this.options.graaldir + "/tileobjects");

            try {
               String[] var6;
               index = (var6 = file.list()).length;

               for(int var4 = 0; var4 < index; ++var4) {
                  String i = var6[var4];
                  if (i.startsWith("objects")) {
                     line = i.substring(7, i.indexOf(".txt"));
                     this.combobox_predefCategories.addItem(line);
                     if (line.equals("Standard") && selectedgroup == null) {
                        this.combobox_predefCategories.setSelectedItem("Standard");
                     }
                  }
               }

               this.loadPredefinedObjects("Standard");
            } catch (NullPointerException var20) {
            }
         } else {
            this.combobox_predefObjects.removeAllItems();
            file = new File(this.options.graaldir + "/tileobjects/objects" + selectedgroup + ".txt");
            int objectCount = 0;

            try {
               Scanner scanner = new Scanner(file);
               String text = scanner.useDelimiter("\\A").next();
               objectCount = text.split("OBJECTEND", -1).length;
               scanner.close();
            } catch (FileNotFoundException var18) {
               var18.printStackTrace();
            }

            if (objectCount <= 0) {
               return;
            }

            this.predefinedobjects = new PredefinedObject[objectCount];
            index = 0;

            try {
               BufferedReader br = new BufferedReader(new FileReader(file));

               while(true) {
                  String[] parts;
                  int width;
                  int height;
                  do {
                     do {
                        if ((line = br.readLine()) == null) {
                           return;
                        }
                     } while(!line.startsWith("OBJECT "));

                     parts = line.split(" ");
                     width = Integer.parseInt(parts[1]);
                     height = Integer.parseInt(parts[2]);
                  } while(parts[3] == null);

                  String name = line.substring(line.indexOf(parts[3])).trim();
                  line = br.readLine();
                  short[][] tiledata = new short[height][width];

                  String tilestring;
                  for(tilestring = ""; !line.equals("OBJECTEND"); line = br.readLine()) {
                     tilestring = tilestring + line;
                  }

                  if (name != null) {
                     this.combobox_predefObjects.addItem(name);
                  }

                  tilestring = tilestring.trim();

                  for(int y = 0; y < height; ++y) {
                     for(int x = 0; x < width; ++x) {
                        String t = tilestring.substring(y * width * 2 + x * 2, y * width * 2 + x * 2 + 2);
                        short tile = TileFunctions.base64toTile(t);
                        tiledata[y][x] = tile;
                     }
                  }

                  this.predefinedobjects[index] = new PredefinedObject(name, width, height, tiledata);
                  ++index;
               }
            } catch (IOException var19) {
               var19.printStackTrace();
            }
         }

      }
   }

   public void removeTabsOther() {
      int tabcount = this.contentPane.getTabCount();

      for(String currentTabID = this.getCurrentItem().uniqueID; tabcount > 1; tabcount = this.contentPane.getTabCount()) {
         for(int i = 0; i < tabcount; ++i) {
            String checkTabID = ((ScrollPane_Level)this.contentPane.getComponentAt(i)).uniqueID;
            if (!checkTabID.equals(currentTabID)) {
               this.closeTab(i);
               tabcount = this.contentPane.getTabCount();
            }
         }
      }

   }

   public void removeTabsRight() {
      int tabcount = this.contentPane.getTabCount() - 1;

      for(int i = 0; i < tabcount; ++i) {
         if (i >= this.contentPane.getSelectedIndex()) {
            this.closeTab(this.contentPane.getSelectedIndex() + 1);
         }
      }

   }
}
