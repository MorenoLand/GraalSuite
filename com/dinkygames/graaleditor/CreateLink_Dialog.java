package com.dinkygames.graaleditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class CreateLink_Dialog extends JDialog {
   private JTextField text_xpos;
   private JTextField text_ypos;
   private JTextField text_width;
   private JTextField text_height;
   private JTextField text_destlevel;
   private JTextField text_newx;
   private JTextField text_newy;
   private ScrollPane_Level scroll;
   private int oldindex = -1;

   public CreateLink_Dialog(int oldindex, ScrollPane_Level scroll) {
      this.CreateLinkDialog((TileSelection)null, scroll, (LevelLink_Dialog)null, oldindex);
   }

   public CreateLink_Dialog(int oldindex, ScrollPane_Level scroll, LevelLink_Dialog dialogwindow) {
      this.CreateLinkDialog((TileSelection)null, scroll, dialogwindow, oldindex);
   }

   public CreateLink_Dialog(TileSelection selection, ScrollPane_Level scroll, int oldindex) {
      this.CreateLinkDialog(selection, scroll, (LevelLink_Dialog)null, oldindex);
   }

   private void CreateLinkDialog(TileSelection selection, final ScrollPane_Level scroll, final LevelLink_Dialog dialogwindow, final int oldindex) {
      this.oldindex = oldindex;
      this.scroll = scroll;
      this.setResizable(false);
      this.setIconImage(Toolkit.getDefaultToolkit().getImage(CreateLink_Dialog.class.getResource("/res/icons/ico_linkadd.png")));

      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         UIDefaults defaults = UIManager.getLookAndFeelDefaults();
         if (defaults.get("Table.alternateRowColor") == null) {
            defaults.put("Table.alternateRowColor", new Color(240, 240, 240));
         }
      } catch (InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | ClassNotFoundException var29) {
         var29.printStackTrace();
      }

      int x = 0;
      int y = 0;
      int width = 2;
      int height = 2;
      String newy = "";
      this.setAlwaysOnTop(true);
      this.setDefaultCloseOperation(2);
      this.setTitle("Edit Link");
      this.setSize(320, 261);
      this.getContentPane().setLayout((LayoutManager)null);
      JPanel buttonPane = new JPanel();
      buttonPane.setBorder((Border)null);
      buttonPane.setBounds(10, 180, 284, 33);
      buttonPane.setLayout(new FlowLayout(2));
      this.getContentPane().add(buttonPane);
      JButton okButton = new JButton("OK");
      okButton.setActionCommand("OK");
      buttonPane.add(okButton);
      this.getRootPane().setDefaultButton(okButton);
      JButton cancelButton = new JButton("Cancel");
      cancelButton.setActionCommand("Cancel");
      buttonPane.add(cancelButton);
      JPanel panel_1 = new JPanel();
      panel_1.setBorder(new EmptyBorder(0, 4, 0, 4));
      panel_1.setBounds(20, 8, 60, 20);
      this.getContentPane().add(panel_1);
      JLabel lblWarpField = new JLabel("Warp Field");
      panel_1.add(lblWarpField);
      JPanel panel_3 = new JPanel();
      panel_3.setBorder(new EmptyBorder(0, 4, 0, 4));
      panel_3.setBounds(16, 88, 101, 20);
      this.getContentPane().add(panel_3);
      JLabel lblNewLabel = new JLabel("Destination Level:");
      panel_3.add(lblNewLabel);
      JPanel panel = new JPanel();
      panel.setBorder(new EtchedBorder(1, (Color)null, (Color)null));
      panel.setBounds(10, 20, 284, 40);
      this.getContentPane().add(panel);
      panel.setLayout((LayoutManager)null);
      JLabel label_xpos = new JLabel("X:");
      label_xpos.setBounds(10, 14, 46, 14);
      panel.add(label_xpos);
      this.text_xpos = new JTextField();
      label_xpos.setLabelFor(this.text_xpos);
      this.text_xpos.setBounds(24, 11, 30, 20);
      panel.add(this.text_xpos);
      this.text_xpos.setColumns(3);
      JLabel label_ypos = new JLabel("Y:");
      label_ypos.setBounds(65, 14, 46, 14);
      panel.add(label_ypos);
      this.text_ypos = new JTextField();
      label_ypos.setLabelFor(this.text_ypos);
      this.text_ypos.setBounds(79, 11, 30, 20);
      panel.add(this.text_ypos);
      this.text_ypos.setColumns(3);
      JLabel label_width = new JLabel("Width:");
      label_width.setBounds(119, 14, 46, 14);
      panel.add(label_width);
      this.text_width = new JTextField();
      label_width.setLabelFor(this.text_width);
      this.text_width.setBounds(156, 11, 30, 20);
      panel.add(this.text_width);
      this.text_width.setColumns(10);
      JLabel label_height = new JLabel("Height:");
      label_height.setBounds(196, 14, 46, 14);
      panel.add(label_height);
      this.text_height = new JTextField();
      label_height.setLabelFor(this.text_height);
      this.text_height.setBounds(236, 11, 30, 20);
      panel.add(this.text_height);
      this.text_height.setColumns(3);
      JLabel lblDestination = new JLabel("Destination:");
      lblDestination.setBounds(10, 70, 60, 14);
      this.getContentPane().add(lblDestination);
      this.text_destlevel = new JTextField();
      this.text_destlevel.setBounds(72, 67, 222, 20);
      this.getContentPane().add(this.text_destlevel);
      this.text_destlevel.setColumns(10);
      JPanel panel_2 = new JPanel();
      panel_2.setBorder(new EtchedBorder(1, (Color)null, (Color)null));
      panel_2.setBounds(10, 100, 284, 74);
      this.getContentPane().add(panel_2);
      panel_2.setLayout((LayoutManager)null);
      JLabel level_newx = new JLabel("New X:");
      level_newx.setBounds(10, 15, 46, 14);
      panel_2.add(level_newx);
      this.text_newx = new JTextField();
      this.text_newx.setBounds(50, 12, 224, 20);
      panel_2.add(this.text_newx);
      this.text_newx.setColumns(10);
      JLabel label_newy = new JLabel("New Y:");
      label_newy.setBounds(10, 43, 46, 14);
      panel_2.add(label_newy);
      this.text_newy = new JTextField();
      this.text_newy.setBounds(50, 40, 224, 20);
      panel_2.add(this.text_newy);
      this.text_newy.setColumns(10);
      this.setVisible(true);
      if (selection == null && oldindex >= 0) {
         Link old = (Link)scroll.level.links.get(oldindex);
         x = old.x;
         y = old.y;
         width = old.width;
         height = old.height;
         String newx = old.destx;
         newy = old.desty;
         String dest = old.dest;
         this.text_newx.setText(newx);
         this.text_newy.setText(newy);
         this.text_destlevel.setText(dest);
      } else if (selection != null) {
         x = selection.ox;
         y = selection.oy;
         width = selection.tiles[0].length;
         height = selection.tiles.length;
      }

      this.text_xpos.setText(String.valueOf(x));
      this.text_ypos.setText(String.valueOf(y));
      this.text_width.setText(String.valueOf(width));
      this.text_height.setText(String.valueOf(height));
      this.text_destlevel.requestFocusInWindow();
      okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               int x = Integer.valueOf(CreateLink_Dialog.this.text_xpos.getText());
               int y = Integer.valueOf(CreateLink_Dialog.this.text_ypos.getText());
               int w = Integer.valueOf(CreateLink_Dialog.this.text_width.getText());
               int h = Integer.valueOf(CreateLink_Dialog.this.text_height.getText());
               String dest = CreateLink_Dialog.this.text_destlevel.getText();
               String destx = CreateLink_Dialog.this.text_newx.getText();
               String desty = CreateLink_Dialog.this.text_newy.getText();
               if (!"".equals(dest) && !"".equals(destx) && !"".equals(desty) && dest != null && destx != null && desty != null) {
                  if (!dest.endsWith(".nw") && !dest.endsWith(".gmap")) {
                     JOptionPane.showMessageDialog((Component)null, "Destination must be a valid .nw or .gmap file!", "ERROR!", 1);
                  } else {
                     if (oldindex >= 0) {
                        scroll.canvas.level.updateLink(oldindex, new Link(dest, x, y, w, h, destx, desty));
                        if (dialogwindow != null) {
                           dialogwindow.updateTableData(scroll.level.getLinksAsArray());
                        }
                     } else {
                        scroll.canvas.level.addLink(dest, x, y, w, h, destx, desty);
                        scroll.canvas.clearTileSelection();
                     }

                     CreateLink_Dialog.this.dispose();
                  }
               } else {
                  JOptionPane.showMessageDialog((Component)null, "All forms must be filled!", "ERROR!", 1);
               }
            } catch (NullPointerException | NumberFormatException var9) {
               JOptionPane.showMessageDialog(CreateLink_Dialog.this, "Value must be numeric only!", "ERROR!", 1);
            }
         }
      });
      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            CreateLink_Dialog.this.dispose();
         }
      });
      this.setLocationRelativeTo(this.scroll.main.frame);
   }
}
