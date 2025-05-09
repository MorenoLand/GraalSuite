package com.dinkygames.graaleditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class LevelLink_Dialog extends JDialog {
   private final JPanel contentPanel = new JPanel();
   private JTable table;
   Level level;

   public LevelLink_Dialog(final Level level) {
      this.addWindowListener(new WindowAdapter() {
         public void windowClosed(WindowEvent e) {
            level.scroll.tilesetpane.mainwindow.dialog_levellinks = null;
            e.getWindow().dispose();
         }
      });
      this.setIconImage(Toolkit.getDefaultToolkit().getImage(LevelLink_Dialog.class.getResource("/res/icons/ico_links.png")));
      this.setDefaultCloseOperation(2);
      if (level == null) {
         this.dispose();
      } else {
         this.level = level;

         try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIDefaults defaults = UIManager.getLookAndFeelDefaults();
            if (defaults.get("Table.alternateRowColor") == null) {
               defaults.put("Table.alternateRowColor", new Color(240, 240, 240));
            }
         } catch (InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | ClassNotFoundException var12) {
            var12.printStackTrace();
         }

         this.setTitle("Link List");
         this.setAlwaysOnTop(true);
         this.setDefaultCloseOperation(2);
         this.setSize(600, 350);
         this.getContentPane().setLayout(new BorderLayout());
         this.contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
         this.getContentPane().add(this.contentPanel, "Center");
         this.contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
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
         String[] columns = new String[]{"Destination Level", "X", "Y", "W", "H", "New X", "New Y"};
         Object[][] links = new Object[level.links.size()][7];

         for(int i = 0; i < links.length; ++i) {
            Link link = (Link)level.links.get(i);
            links[i] = new Object[]{link.dest, link.x, link.y, link.width, link.height, link.destx, link.desty};
         }

         this.table.setModel(new DefaultTableModel(links, columns) {
            private static final long serialVersionUID = 1L;
            Class[] columnTypes = new Class[]{String.class, Integer.class, Integer.class, Integer.class, Integer.class, String.class, String.class};
            boolean[] columnEditables = new boolean[7];

            public Class getColumnClass(int columnIndex) {
               return this.columnTypes[columnIndex];
            }

            public boolean isCellEditable(int row, int column) {
               return this.columnEditables[column];
            }
         });
         DefaultTableCellRenderer var10000 = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
               super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
               this.setHorizontalAlignment(0);
               return this;
            }
         };
         DefaultTableCellRenderer leftAlign = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 7958342459340317177L;

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
               super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
               this.setHorizontalAlignment(2);
               this.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
               return this;
            }
         };

         for(int i = 0; i < columns.length; ++i) {
            this.table.getColumnModel().getColumn(i).setCellRenderer(leftAlign);
         }

         this.table.getColumnModel().getColumn(0).setPreferredWidth(200);
         this.table.getColumnModel().getColumn(1).setPreferredWidth(32);
         this.table.getColumnModel().getColumn(2).setPreferredWidth(32);
         this.table.getColumnModel().getColumn(3).setPreferredWidth(32);
         this.table.getColumnModel().getColumn(4).setPreferredWidth(32);
         this.table.setFillsViewportHeight(true);
         this.table.setRowHeight(22);
         scrollPane.setViewportView(this.table);
         JPanel buttonPane = new JPanel();
         buttonPane.setLayout(new FlowLayout(2));
         this.getContentPane().add(buttonPane, "South");
         JButton btnEdit = new JButton("Edit");
         btnEdit.setIcon(new ImageIcon(LevelLink_Dialog.class.getResource("/res/icons/ico_linkadd.png")));
         buttonPane.add(btnEdit);
         btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
               int selected = LevelLink_Dialog.this.table.getSelectedRow();
               if (selected >= 0) {
                  LevelLink_Dialog.this.level.scroll.createNewLink(selected, LevelLink_Dialog.this);
               }
            }
         });
         JButton btnNewButton = new JButton("Load");
         btnNewButton.setIcon(new ImageIcon(LevelLink_Dialog.class.getResource("/res/icons/ico_open.png")));
         buttonPane.add(btnNewButton);
         btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
               int selected = LevelLink_Dialog.this.table.getSelectedRow();
               if (selected >= 0) {
                  String dest = ((Link)level.links.get(selected)).dest;
                  File file = new File(level.filename);
                  String dir = file.getParent();
                  String openlevel = dir + "\\" + dest;
                  level.scroll.tilesetpane.mainwindow.openLevel(openlevel);
                  System.out.println("Opening... " + openlevel);
                  LevelLink_Dialog.this.dispose();
               }
            }
         });
         if (level.links != null) {
            this.updateTableData(level.getLinksAsArray());
         }

         JButton btnDelete = new JButton("Delete");
         btnDelete.setIcon(new ImageIcon(LevelLink_Dialog.class.getResource("/res/icons/ico_delete.png")));
         buttonPane.add(btnDelete);
         btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
               int selected = LevelLink_Dialog.this.table.getSelectedRow();
               if (selected >= 0) {
                  ((DefaultTableModel)LevelLink_Dialog.this.table.getModel()).removeRow(selected);
                  LevelLink_Dialog.this.table.repaint();
                  LevelLink_Dialog.this.level.removeLink(selected);
               }
            }
         });
         JButton btnClose = new JButton("Close");
         btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
               LevelLink_Dialog.this.dispose();
            }
         });
         btnClose.setActionCommand("Close");
         buttonPane.add(btnClose);
         this.pack();
         this.setVisible(true);
         this.setLocationRelativeTo(this.level.main.frame);
      }
   }

   public void updateTableData(Link[] data) {
      ((DefaultTableModel)this.table.getModel()).setRowCount(data.length);

      for(int r = 0; r < data.length; ++r) {
         Object[] linkdata = data[r].getLinkAsArray();

         for(int c = 0; c < linkdata.length; ++c) {
            try {
               this.table.setValueAt(linkdata[c], r, c);
            } catch (ClassCastException var6) {
            }
         }
      }

      this.table.repaint();
   }
}
