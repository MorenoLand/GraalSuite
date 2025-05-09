package com.dinkygames.graaleditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public final class TilesetDefinitions_Dialog extends JFrame implements ActionListener {
   private final JPanel contentPanel = new JPanel();
   private JTable table;
   public TileDefinitions tiledefs;
   private JButton button_delete;
   private JButton button_edit;
   public EditTiledef_Dialog dialogadd;
   private boolean updating = false;

   public TilesetDefinitions_Dialog(final TileDefinitions tiledefs) {
      if (tiledefs != null) {
         this.tiledefs = tiledefs;
         this.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
               tiledefs.main.tiledef_dialog = null;
               e.getWindow().dispose();
            }
         });
      }

      this.setIconImage(Toolkit.getDefaultToolkit().getImage(TilesetDefinitions_Dialog.class.getResource("/res/icons/ico_tiledefs.png")));
      this.setAlwaysOnTop(true);

      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         UIDefaults defaults = UIManager.getLookAndFeelDefaults();
         if (defaults.get("Table.alternateRowColor") == null) {
            defaults.put("Table.alternateRowColor", new Color(240, 240, 240));
         }
      } catch (InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | ClassNotFoundException var9) {
         var9.printStackTrace();
      }

      this.setTitle("Tile Definitions");
      this.setPreferredSize(new Dimension(600, 400));
      this.getContentPane().setLayout(new BorderLayout());
      this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
      this.getContentPane().add(this.contentPanel, "Center");
      this.contentPanel.setLayout(new GridLayout(1, 0, 0, 0));
      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setHorizontalScrollBarPolicy(31);
      scrollPane.setViewportBorder(new BevelBorder(1, (Color)null, (Color)null, (Color)null, (Color)null));
      this.contentPanel.add(scrollPane);
      this.table = new JTable();
      this.table.setFont(new Font("Tahoma", 0, 12));
      this.table.setBorder((Border)null);
      this.table.setShowVerticalLines(false);
      this.table.setShowHorizontalLines(false);
      this.table.setShowGrid(false);
      this.table.setSelectionMode(0);
      String[] columns = new String[]{"Visible", "Image", "Prefix", "Type", "X", "Y"};
      this.table.setModel(new DefaultTableModel((Object[][])null, columns) {
         private static final long serialVersionUID = 7925307397685722499L;
         Class[] columnTypes = new Class[]{Boolean.class, String.class, Object.class, Integer.class, Integer.class, Integer.class};
         boolean[] columnEditables = new boolean[]{true, false, false, false, false, false, false};

         public Class getColumnClass(int columnIndex) {
            return this.columnTypes[columnIndex];
         }

         public boolean isCellEditable(int row, int column) {
            return this.columnEditables[column];
         }

         public void updateRow(int index, Object[] values) {
            for(int i = 0; i < values.length; ++i) {
               this.setValueAt(values[i], index, i);
            }

         }
      });
      DefaultTableCellRenderer leftAlign = new DefaultTableCellRenderer() {
         private static final long serialVersionUID = 7958342459340317177L;

         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            this.setHorizontalAlignment(2);
            this.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
            return this;
         }
      };

      for(int i = 1; i < columns.length; ++i) {
         this.table.getColumnModel().getColumn(i).setCellRenderer(leftAlign);
      }

      this.table.getColumnModel().getColumn(0).setPreferredWidth(55);
      this.table.getColumnModel().getColumn(1).setPreferredWidth(320);
      this.table.getColumnModel().getColumn(2).setPreferredWidth(250);
      this.table.getColumnModel().getColumn(3).setPreferredWidth(55);
      this.table.getColumnModel().getColumn(4).setPreferredWidth(80);
      this.table.getColumnModel().getColumn(5).setPreferredWidth(80);
      scrollPane.setViewportView(this.table);
      this.table.setRowHeight(22);
      this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent event) {
            TilesetDefinitions_Dialog.this.button_edit.setEnabled(true);
            TilesetDefinitions_Dialog.this.button_delete.setEnabled(true);
         }
      });
      JPanel buttonPane = new JPanel();
      buttonPane.setLayout(new FlowLayout(2));
      this.getContentPane().add(buttonPane, "South");
      JButton button_add = new JButton("Add");
      button_add.addActionListener(this);
      button_add.setActionCommand("add");
      button_add.setIcon(new ImageIcon(LevelLink_Dialog.class.getResource("/res/icons/ico_tiledefadd.png")));
      buttonPane.add(button_add);
      this.button_edit = new JButton("Edit");
      this.button_edit.addActionListener(this);
      this.button_edit.setActionCommand("edit");
      this.button_edit.setIcon(new ImageIcon(LevelLink_Dialog.class.getResource("/res/icons/ico_tiledefs.png")));
      buttonPane.add(this.button_edit);
      this.button_edit.setEnabled(false);
      JButton button_import = new JButton("Import");
      button_import.setToolTipText("Import tiledefs from a snippet of gscript");
      button_import.addActionListener(this);
      button_import.setEnabled(false);
      button_import.setActionCommand("import");
      buttonPane.add(button_import);
      this.button_delete = new JButton("Delete");
      this.button_delete.addActionListener(this);
      this.button_delete.setActionCommand("delete");
      this.button_delete.setIcon(new ImageIcon(LevelLink_Dialog.class.getResource("/res/icons/ico_delete.png")));
      buttonPane.add(this.button_delete);
      this.button_delete.setEnabled(false);
      JButton button_close = new JButton("Close");
      button_close.addActionListener(this);
      button_close.setActionCommand("close");
      buttonPane.add(button_close);
      if (tiledefs != null) {
         this.updateTableData(tiledefs.getTileDefsAsArray());
      }

      this.setDefaultCloseOperation(2);
      this.pack();
      this.setVisible(true);
      this.setLocationRelativeTo(this.tiledefs.main.frame);
   }

   public void updateTableData(Object[][] data) {
      this.updating = true;
      ((DefaultTableModel)this.table.getModel()).setRowCount(data.length);

      for(int r = 0; r < data.length; ++r) {
         for(int c = 0; c < data[r].length; ++c) {
            try {
               this.table.setValueAt(data[r][c], r, c);
            } catch (ClassCastException var5) {
            }
         }
      }

      this.updating = false;
      this.table.getModel().addTableModelListener(new TableModelListener() {
         public void tableChanged(TableModelEvent e) {
            if (e != null && !TilesetDefinitions_Dialog.this.updating) {
               int selectedDef = e.getFirstRow();
               if (selectedDef >= 0 && e.getColumn() >= 0) {
                  Boolean checked = (Boolean)((TableModel)e.getSource()).getValueAt(selectedDef, e.getColumn());
                  Object[] newdef = (Object[])TilesetDefinitions_Dialog.this.tiledefs.tiledefinitions.get(selectedDef);
                  newdef[0] = checked;
                  TilesetDefinitions_Dialog.this.tiledefs.tiledefinitions.set(selectedDef, newdef);
                  TilesetDefinitions_Dialog.this.tiledefs.main.updateTileset();
               }
            }
         }
      });
      this.tiledefs.main.updateTileset();
      this.table.repaint();
   }

   public void actionPerformed(ActionEvent action) {
      String cmd = action.getActionCommand();
      if (cmd.equals("close")) {
         this.tiledefs.save();
         this.dispose();
      } else {
         int sel;
         if (cmd.equals("delete")) {
            sel = this.table.getSelectedRow();
            this.tiledefs.tiledefinitions.remove(sel);
            ((DefaultTableModel)this.table.getModel()).removeRow(sel);
            this.table.repaint();
            this.tiledefs.main.updateTileset();
            this.button_edit.setEnabled(false);
            this.button_delete.setEnabled(false);
         } else if (cmd == "add") {
            this.dialogadd = new EditTiledef_Dialog(this, -1);
         } else if (cmd == "edit") {
            sel = this.table.getSelectedRow();
            Object[] tiledef = (Object[])this.tiledefs.tiledefinitions.get(sel);
            this.dialogadd = new EditTiledef_Dialog(this, sel);
         }
      }

   }
}
