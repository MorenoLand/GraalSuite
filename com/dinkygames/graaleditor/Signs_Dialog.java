package com.dinkygames.graaleditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Signs_Dialog extends JFrame {
   private JPanel contentPane;
   private JTable table;
   private JTextField text_xpos;
   private JTextField text_ypos;
   private boolean updating = false;
   private Level level;
   private JTextPane signtext;
   private int lastRow = -1;

   public Signs_Dialog(final Level level, Sign newsign) {
      this.addWindowListener(new WindowAdapter() {
         public void windowClosed(WindowEvent e) {
            level.main.dialog_signs = null;
            e.getWindow().dispose();
         }
      });
      this.level = level;
      this.setIconImage(Toolkit.getDefaultToolkit().getImage(TilesetDefinitions_Dialog.class.getResource("/res/icons/ico_signs.png")));
      this.setAlwaysOnTop(true);

      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         UIDefaults defaults = UIManager.getLookAndFeelDefaults();
         if (defaults.get("Table.alternateRowColor") == null) {
            defaults.put("Table.alternateRowColor", new Color(240, 240, 240));
         }
      } catch (InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | ClassNotFoundException var24) {
         var24.printStackTrace();
      }

      this.setTitle("Signs");
      this.setPreferredSize(new Dimension(560, 380));
      this.contentPane = new JPanel();
      this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      this.setContentPane(this.contentPane);
      this.contentPane.setLayout(new BorderLayout(0, 0));
      this.setMinimumSize(new Dimension(560, 380));
      JPanel button_panel = new JPanel();
      button_panel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 29));
      button_panel.setBorder(new EmptyBorder(4, 0, 0, 0));
      this.contentPane.add(button_panel, "South");
      SpringLayout sl_button_panel = new SpringLayout();
      button_panel.setLayout(sl_button_panel);
      JButton button_new = new JButton("New");
      button_new.setIcon(new ImageIcon(Signs_Dialog.class.getResource("/res/icons/ico_signadd.png")));
      sl_button_panel.putConstraint("North", button_new, 0, "North", button_panel);
      sl_button_panel.putConstraint("West", button_new, 0, "West", button_panel);
      button_panel.add(button_new);
      JButton button_update = new JButton("Update");
      button_update.setIcon(new ImageIcon(Signs_Dialog.class.getResource("/res/icons/ico_signadd.png")));
      sl_button_panel.putConstraint("West", button_update, 6, "East", button_new);
      button_panel.add(button_update);
      JButton button_delete = new JButton("Delete");
      button_delete.setIcon(new ImageIcon(Signs_Dialog.class.getResource("/res/icons/ico_delete.png")));
      sl_button_panel.putConstraint("West", button_delete, 6, "East", button_update);
      button_panel.add(button_delete);
      JButton button_close = new JButton("Close");
      sl_button_panel.putConstraint("East", button_close, 0, "East", button_panel);
      button_panel.add(button_close);
      JPanel panel = new JPanel();
      this.contentPane.add(panel, "Center");
      panel.setLayout(new BoxLayout(panel, 1));
      JPanel panel_1 = new JPanel();
      panel.add(panel_1);
      GridBagLayout gbl_panel_1 = new GridBagLayout();
      gbl_panel_1.columnWidths = new int[]{534, 0};
      gbl_panel_1.rowHeights = new int[]{157, 150, 0};
      gbl_panel_1.columnWeights = new double[]{1.0D, Double.MIN_VALUE};
      gbl_panel_1.rowWeights = new double[]{0.0D, 1.0D, Double.MIN_VALUE};
      panel_1.setLayout(gbl_panel_1);
      String[] columns = new String[]{"Sign", "X", "Y", "Text"};
      this.table = new JTable();
      this.table.setModel(new DefaultTableModel((Object[][])null, columns) {
         private static final long serialVersionUID = 8712756922443733037L;
         Class[] columnTypes = new Class[]{Integer.class, Integer.class, Integer.class, String.class};
         boolean[] columnEditables = new boolean[4];

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
         private static final long serialVersionUID = 7601477725135378911L;

         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            this.setHorizontalAlignment(2);
            this.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
            return this;
         }
      };
      this.table.getColumnModel().getColumn(0).setPreferredWidth(38);
      this.table.getColumnModel().getColumn(1).setPreferredWidth(38);
      this.table.getColumnModel().getColumn(2).setPreferredWidth(38);
      this.table.getColumnModel().getColumn(3).setPreferredWidth(450);
      this.table.setBorder((Border)null);
      this.table.setShowVerticalLines(false);
      this.table.setShowHorizontalLines(false);
      this.table.setShowGrid(false);
      this.table.setSelectionMode(0);
      this.table.setFont(new Font("Tahoma", 0, 12));
      this.table.setRowHeight(22);

      for(int i = 0; i < columns.length; ++i) {
         this.table.getColumnModel().getColumn(i).setCellRenderer(leftAlign);
      }

      this.updateTableData();
      JScrollPane scrollPane = new JScrollPane(this.table);
      GridBagConstraints gbc_scrollPane = new GridBagConstraints();
      gbc_scrollPane.weighty = 1.0D;
      gbc_scrollPane.fill = 1;
      gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
      gbc_scrollPane.gridx = 0;
      gbc_scrollPane.gridy = 0;
      panel_1.add(scrollPane, gbc_scrollPane);
      JPanel panel_2 = new JPanel();
      GridBagConstraints gbc_panel_2 = new GridBagConstraints();
      gbc_panel_2.fill = 1;
      gbc_panel_2.gridx = 0;
      gbc_panel_2.gridy = 1;
      panel_1.add(panel_2, gbc_panel_2);
      panel_2.setLayout(new BorderLayout(0, 0));
      JPanel panel_3 = new JPanel();
      panel_3.setBorder((Border)null);
      panel_2.add(panel_3, "Center");
      panel_3.setLayout(new BorderLayout(0, 0));
      JPanel panel_4 = new JPanel();
      panel_3.add(panel_4, "North");
      FlowLayout fl_panel_4 = new FlowLayout(0, 5, 5);
      panel_4.setLayout(fl_panel_4);
      JLabel lblX = new JLabel("X:");
      panel_4.add(lblX);
      this.text_xpos = new JTextField();
      panel_4.add(this.text_xpos);
      this.text_xpos.setColumns(4);
      JLabel lblY = new JLabel("Y:");
      panel_4.add(lblY);
      this.text_ypos = new JTextField();
      panel_4.add(this.text_ypos);
      this.text_ypos.setColumns(4);
      JScrollPane scrollPane_1 = new JScrollPane();
      panel_3.add(scrollPane_1, "Center");
      this.signtext = new JTextPane();
      scrollPane_1.setViewportView(this.signtext);
      this.setDefaultCloseOperation(2);
      this.pack();
      this.setVisible(true);
      this.setLocationRelativeTo(this.level.main.frame);
      button_new.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Signs_Dialog.this.addNewSign((Sign)null);
         }
      });
      button_update.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            int selected = Signs_Dialog.this.table.getSelectedRow();
            if (selected >= 0 && selected <= Signs_Dialog.this.level.signs.size() - 1) {
               boolean success = Signs_Dialog.this.updateSignData(selected);
               if (success) {
                  Signs_Dialog.this.updateSignComponents(selected);
               }
            }
         }
      });
      button_delete.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            int selected = Signs_Dialog.this.table.getSelectedRow();
            if (selected >= 0 && selected <= Signs_Dialog.this.level.signs.size() - 1) {
               Signs_Dialog.this.level.signs.remove(selected);
               Signs_Dialog.this.level.scroll.canvas.repaint();
               Signs_Dialog.this.updateTableData();
               Signs_Dialog.this.signtext.setText("");
               Signs_Dialog.this.text_xpos.setText("");
               Signs_Dialog.this.text_ypos.setText("");
               Signs_Dialog.this.lastRow = -1;
            }
         }
      });
      button_close.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            boolean success = Signs_Dialog.this.updateSignData();
            if (success) {
               Signs_Dialog.this.dispose();
            }
         }
      });
      this.text_xpos.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent e) {
         }

         public void focusLost(FocusEvent e) {
            if (!e.isTemporary()) {
               int selected = Signs_Dialog.this.table.getSelectedRow();
               if (selected < 0 || selected > Signs_Dialog.this.level.signs.size() - 1) {
                  return;
               }

               boolean success = Signs_Dialog.this.updateSignData(selected);
               if (!success) {
                  return;
               }

               Signs_Dialog.this.updateSignComponents(selected);
            }

         }
      });
      this.text_ypos.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent e) {
         }

         public void focusLost(FocusEvent e) {
            if (!e.isTemporary()) {
               int selected = Signs_Dialog.this.table.getSelectedRow();
               if (selected < 0 || selected > Signs_Dialog.this.level.signs.size() - 1) {
                  return;
               }

               boolean success = Signs_Dialog.this.updateSignData(selected);
               if (!success) {
                  return;
               }

               Signs_Dialog.this.updateSignComponents(selected);
            }

         }
      });
      this.signtext.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent e) {
            if (Signs_Dialog.this.signtext.getText().equals("<New Text>")) {
               Signs_Dialog.this.signtext.setForeground(Color.BLACK);
               Signs_Dialog.this.signtext.setText("");
            }

         }

         public void focusLost(FocusEvent e) {
            if (!e.isTemporary()) {
               int selected = Signs_Dialog.this.table.getSelectedRow();
               if (selected < 0 || selected > Signs_Dialog.this.level.signs.size() - 1) {
                  return;
               }

               boolean success = Signs_Dialog.this.updateSignData(selected);
               if (!success) {
                  return;
               }

               Signs_Dialog.this.updateSignComponents(selected);
            }

         }
      });
      this.text_xpos.setEnabled(false);
      this.text_ypos.setEnabled(false);
      this.signtext.setEnabled(false);
      if (newsign != null) {
         this.addNewSign(newsign);
      }

   }

   public void updateTableData() {
      this.updating = true;
      Object[][] data = new Object[this.level.signs.size()][4];

      int r;
      for(r = 0; r < this.level.signs.size(); ++r) {
         Sign sign = (Sign)this.level.signs.get(r);
         data[r] = new Object[]{r, sign.x, sign.y, sign.text};
      }

      ((DefaultTableModel)this.table.getModel()).setRowCount(data.length);

      for(r = 0; r < data.length; ++r) {
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
            if (e != null && !Signs_Dialog.this.updating) {
               int selectedDef = e.getFirstRow();
               if (selectedDef < 0 || e.getColumn() < 0) {
                  ;
               }
            }
         }
      });
      this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting() && !Signs_Dialog.this.updating) {
               if (Signs_Dialog.this.lastRow >= 0 && Signs_Dialog.this.lastRow < Signs_Dialog.this.level.signs.size()) {
                  boolean success = Signs_Dialog.this.updateSignData(Signs_Dialog.this.lastRow);
                  if (!success) {
                     return;
                  }
               }

               int selected = Signs_Dialog.this.table.getSelectedRow();
               Signs_Dialog.this.updateSignComponents(selected);
               Signs_Dialog.this.lastRow = selected;
            }
         }
      });
      if (this.signtext != null) {
         this.text_xpos.setEnabled(false);
         this.text_ypos.setEnabled(false);
         this.signtext.setEnabled(false);
      }

      this.table.repaint();
   }

   public void addNewSign(Sign newsign) {
      boolean success = this.updateSignData();
      if (success) {
         if (newsign == null) {
            newsign = new Sign("<New Text>", 0, 0);
         }

         this.level.signs.add(newsign);
         this.level.scroll.canvas.repaint();
         int newsignindex = this.level.signs.size() - 1;
         this.updateTableData();
         this.table.setRowSelectionInterval(newsignindex, newsignindex);
         this.updateSignComponents(newsignindex);
         if (this.signtext.getText().equals("<New Text>")) {
            this.signtext.setForeground(Color.gray);
         }

         this.level.scroll.canvas.setEdited(true);
      }
   }

   private boolean updateSignData() {
      int index = this.table.getSelectedRow();
      if (index >= 0 && index < this.level.signs.size()) {
         this.updateSignData(index);
      }

      return true;
   }

   private boolean updateSignData(int index) {
      if (index >= 0 && index <= this.level.signs.size() - 1) {
         Sign sign = (Sign)this.level.signs.get(index);
         String text = this.signtext.getText();
         if (text.equals("<New Text>")) {
            text = "";
         }

         sign.updateText(text);
         this.table.setValueAt(text, index, 3);
         boolean failed = false;

         int y;
         try {
            y = Integer.valueOf(this.text_xpos.getText());
            this.table.setValueAt(y, index, 1);
            sign.updateX(y);
         } catch (NullPointerException | NumberFormatException var7) {
            failed = true;
         }

         try {
            y = Integer.valueOf(this.text_ypos.getText());
            this.table.setValueAt(y, index, 2);
            sign.updateY(y);
         } catch (NullPointerException | NumberFormatException var6) {
            failed = true;
         }

         if (failed) {
            JOptionPane.showMessageDialog(this, "Value must be numeric only!", "ERROR!", 1);
            this.lastRow = -1;
            this.text_xpos.setText("");
            this.text_ypos.setText("");
            this.signtext.setText("");
            return false;
         } else {
            return true;
         }
      } else {
         this.text_xpos.setEnabled(false);
         this.text_ypos.setEnabled(false);
         this.signtext.setEnabled(false);
         return false;
      }
   }

   private void updateSignComponents(int index) {
      if (index >= 0 && index <= this.level.signs.size() - 1) {
         Sign sign = (Sign)this.level.signs.get(index);
         this.text_xpos.setText(String.valueOf(sign.x));
         this.text_ypos.setText(String.valueOf(sign.y));
         this.signtext.setForeground(Color.BLACK);
         this.signtext.setText(sign.text);
         this.signtext.setCaretPosition(0);
         this.text_xpos.setEnabled(true);
         this.text_ypos.setEnabled(true);
         this.signtext.setEnabled(true);
         this.lastRow = index;
      } else {
         this.text_xpos.setEnabled(false);
         this.text_ypos.setEnabled(false);
         this.signtext.setEnabled(false);
      }
   }
}
