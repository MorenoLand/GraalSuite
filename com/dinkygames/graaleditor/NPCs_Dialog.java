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

public class NPCs_Dialog extends JFrame {
   private JPanel contentPane;
   private JTable table;
   private JTextField text_xpos;
   private JTextField text_ypos;
   private JTextField text_image;
   private boolean updating = false;
   private Level level;
   private int lastRow = -1;

   public NPCs_Dialog(final Level level) {
      this.addWindowListener(new WindowAdapter() {
         public void windowClosed(WindowEvent e) {
            level.main.dialog_npcs = null;
            e.getWindow().dispose();
         }
      });
      this.level = level;
      this.setIconImage(Toolkit.getDefaultToolkit().getImage(NPCs_Dialog.class.getResource("/res/icons/ico_npcs.png")));
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

      this.setTitle("NPCs");
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
      button_new.setIcon(new ImageIcon(NPCs_Dialog.class.getResource("/res/icons/ico_npcadd.png")));
      sl_button_panel.putConstraint("North", button_new, 0, "North", button_panel);
      sl_button_panel.putConstraint("West", button_new, 0, "West", button_panel);
      button_panel.add(button_new);
      JButton button_edit = new JButton("Edit Script");
      button_edit.setIcon(new ImageIcon(NPCs_Dialog.class.getResource("/res/icons/ico_npcs.png")));
      sl_button_panel.putConstraint("West", button_edit, 6, "East", button_new);
      button_panel.add(button_edit);
      JButton button_delete = new JButton("Delete");
      button_delete.setIcon(new ImageIcon(NPCs_Dialog.class.getResource("/res/icons/ico_delete.png")));
      sl_button_panel.putConstraint("West", button_delete, 6, "East", button_edit);
      button_panel.add(button_delete);
      JButton button_close = new JButton("Close");
      sl_button_panel.putConstraint("East", button_close, 0, "East", button_panel);
      button_panel.add(button_close);
      JPanel panel = new JPanel();
      this.contentPane.add(panel, "Center");
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      JPanel panel_1 = new JPanel();
      panel.add(panel_1);
      GridBagLayout gbl_panel_1 = new GridBagLayout();
      gbl_panel_1.columnWidths = new int[]{534, 0};
      gbl_panel_1.rowHeights = new int[]{157, 150, 0};
      gbl_panel_1.columnWeights = new double[]{1.0D, Double.MIN_VALUE};
      gbl_panel_1.rowWeights = new double[]{0.0D, 1.0D, Double.MIN_VALUE};
      panel_1.setLayout(gbl_panel_1);
      String[] columns = new String[]{"NPC", "X", "Y", "Image", "Script Length"};
      this.table = new JTable();
      this.table.setModel(new DefaultTableModel((Object[][])null, columns) {
         private static final long serialVersionUID = 8712756922443733037L;
         Class[] columnTypes = new Class[]{Integer.class, Integer.class, Integer.class, String.class, Integer.class};
         boolean[] columnEditables = new boolean[5];

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
      this.table.getColumnModel().getColumn(3).setPreferredWidth(150);
      this.table.getColumnModel().getColumn(4).setPreferredWidth(80);
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
      gbc_scrollPane.fill = GridBagConstraints.BOTH;
      gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
      gbc_scrollPane.gridx = 0;
      gbc_scrollPane.gridy = 0;
      panel_1.add(scrollPane, gbc_scrollPane);
      JPanel panel_2 = new JPanel();
      GridBagConstraints gbc_panel_2 = new GridBagConstraints();
      gbc_panel_2.fill = GridBagConstraints.BOTH;
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
      FlowLayout fl_panel_4 = new FlowLayout(FlowLayout.LEFT, 5, 5);
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
      JLabel lblImage = new JLabel("Image:");
      panel_4.add(lblImage);
      this.text_image = new JTextField();
      panel_4.add(this.text_image);
      this.text_image.setColumns(15);
      this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      this.pack();
      this.setVisible(true);
      this.setLocationRelativeTo(this.level.main.frame);
      button_new.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            NPC newNpc = NPCs_Dialog.this.level.addNPC();
            newNpc.setx(30);
            newNpc.sety(30);
            NPCs_Dialog.this.level.scroll.canvas.setEdited(true);
            NPCs_Dialog.this.level.scroll.canvas.repaint();
            NPCs_Dialog.this.updateTableData();
            NPCs_Dialog.this.table.setRowSelectionInterval(NPCs_Dialog.this.level.npcs.size() - 1, NPCs_Dialog.this.level.npcs.size() - 1);
            NPCs_Dialog.this.updateNPCComponents(NPCs_Dialog.this.level.npcs.size() - 1);
            new ScriptEditor(newNpc);
         }
      });
      button_edit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            int selected = NPCs_Dialog.this.table.getSelectedRow();
            if (selected >= 0 && selected <= NPCs_Dialog.this.level.npcs.size() - 1) {
               boolean success = NPCs_Dialog.this.updateNPCData(selected);
               if (success) {
                  NPC npc = (NPC)NPCs_Dialog.this.level.npcs.get(selected);
                  new ScriptEditor(npc);
               }
            }
         }
      });
      button_delete.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            int selected = NPCs_Dialog.this.table.getSelectedRow();
            if (selected >= 0 && selected <= NPCs_Dialog.this.level.npcs.size() - 1) {
               NPCs_Dialog.this.level.npcs.remove(selected);
               NPCs_Dialog.this.level.scroll.canvas.repaint();
               NPCs_Dialog.this.level.scroll.canvas.setEdited(true);
               NPCs_Dialog.this.updateTableData();
               NPCs_Dialog.this.text_xpos.setText("");
               NPCs_Dialog.this.text_ypos.setText("");
               NPCs_Dialog.this.text_image.setText("");
               NPCs_Dialog.this.lastRow = -1;
            }
         }
      });
      button_close.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            boolean success = NPCs_Dialog.this.updateNPCData();
            if (success) {
               NPCs_Dialog.this.dispose();
            }
         }
      });
      this.text_xpos.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent e) {
         }

         public void focusLost(FocusEvent e) {
            if (!e.isTemporary()) {
               int selected = NPCs_Dialog.this.table.getSelectedRow();
               if (selected < 0 || selected > NPCs_Dialog.this.level.npcs.size() - 1) {
                  return;
               }

               boolean success = NPCs_Dialog.this.updateNPCData(selected);
               if (!success) {
                  return;
               }

               NPCs_Dialog.this.updateNPCComponents(selected);
            }
         }
      });
      this.text_ypos.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent e) {
         }

         public void focusLost(FocusEvent e) {
            if (!e.isTemporary()) {
               int selected = NPCs_Dialog.this.table.getSelectedRow();
               if (selected < 0 || selected > NPCs_Dialog.this.level.npcs.size() - 1) {
                  return;
               }

               boolean success = NPCs_Dialog.this.updateNPCData(selected);
               if (!success) {
                  return;
               }

               NPCs_Dialog.this.updateNPCComponents(selected);
            }
         }
      });
      this.text_image.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent e) {
         }

         public void focusLost(FocusEvent e) {
            if (!e.isTemporary()) {
               int selected = NPCs_Dialog.this.table.getSelectedRow();
               if (selected < 0 || selected > NPCs_Dialog.this.level.npcs.size() - 1) {
                  return;
               }

               boolean success = NPCs_Dialog.this.updateNPCData(selected);
               if (!success) {
                  return;
               }

               NPCs_Dialog.this.updateNPCComponents(selected);
            }
         }
      });
      this.text_xpos.setEnabled(false);
      this.text_ypos.setEnabled(false);
      this.text_image.setEnabled(false);
      
      this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting() && !NPCs_Dialog.this.updating) {
               if (NPCs_Dialog.this.lastRow >= 0 && NPCs_Dialog.this.lastRow < NPCs_Dialog.this.level.npcs.size()) {
                  boolean success = NPCs_Dialog.this.updateNPCData(NPCs_Dialog.this.lastRow);
                  if (!success) {
                     return;
                  }
               }

               int selected = NPCs_Dialog.this.table.getSelectedRow();
               NPCs_Dialog.this.updateNPCComponents(selected);
               NPCs_Dialog.this.lastRow = selected;
            }
         }
      });
   }

   public void updateTableData() {
      this.updating = true;
      Object[][] data = new Object[this.level.npcs.size()][5];

      for(int r = 0; r < this.level.npcs.size(); ++r) {
         NPC npc = (NPC)this.level.npcs.get(r);
         String imageName = npc.image;
         if (imageName == null || imageName.equals("-")) {
            imageName = "";
         }
         data[r] = new Object[]{r, npc.x, npc.y, imageName, npc.script.length()};
      }

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
            if (e != null && !NPCs_Dialog.this.updating) {
               int selectedNpc = e.getFirstRow();
               if (selectedNpc < 0 || e.getColumn() < 0) {
                  return;
               }
            }
         }
      });
      
      if (this.text_xpos != null) {
         this.text_xpos.setEnabled(false);
         this.text_ypos.setEnabled(false);
         this.text_image.setEnabled(false);
      }

      this.table.repaint();
   }

   private boolean updateNPCData() {
      int index = this.table.getSelectedRow();
      if (index >= 0 && index < this.level.npcs.size()) {
         return this.updateNPCData(index);
      }
      return true;
   }

   private boolean updateNPCData(int index) {
      if (index >= 0 && index <= this.level.npcs.size() - 1) {
         NPC npc = (NPC)this.level.npcs.get(index);
         String image = this.text_image.getText();
         npc.image = image;
         this.table.setValueAt(image, index, 3);
         
         boolean failed = false;
         try {
            int x = Integer.valueOf(this.text_xpos.getText());
            this.table.setValueAt(x, index, 1);
            npc.setx(x);
         } catch (NullPointerException | NumberFormatException var7) {
            failed = true;
         }

         try {
            int y = Integer.valueOf(this.text_ypos.getText());
            this.table.setValueAt(y, index, 2);
            npc.sety(y);
         } catch (NullPointerException | NumberFormatException var6) {
            failed = true;
         }

         if (failed) {
            JOptionPane.showMessageDialog(this, "Position values must be numeric only!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            this.lastRow = -1;
            return false;
         } else {
            npc.update();
            this.level.scroll.canvas.repaint();
            this.level.scroll.canvas.setEdited(true);
            return true;
         }
      } else {
         this.text_xpos.setEnabled(false);
         this.text_ypos.setEnabled(false);
         this.text_image.setEnabled(false);
         return false;
      }
   }

   private void updateNPCComponents(int index) {
      if (index >= 0 && index <= this.level.npcs.size() - 1) {
         NPC npc = (NPC)this.level.npcs.get(index);
         this.text_xpos.setText(String.valueOf(npc.x));
         this.text_ypos.setText(String.valueOf(npc.y));
         String image = npc.image;
         if (image == null || image.equals("-")) {
            image = "";
         }
         this.text_image.setText(image);
         this.text_xpos.setEnabled(true);
         this.text_ypos.setEnabled(true);
         this.text_image.setEnabled(true);
         this.lastRow = index;
      } else {
         this.text_xpos.setEnabled(false);
         this.text_ypos.setEnabled(false);
         this.text_image.setEnabled(false);
      }
   }
}