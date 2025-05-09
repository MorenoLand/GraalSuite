package com.dinkygames.graaleditor;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

public class Hex_Dialog extends JFrame {
   private JPanel contentPane;
   private JTextField hex_singleline;
   private JTextArea hex_multiline;
   private static String NEWLINE = System.getProperty("line.separator");

   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               Hex_Dialog frame = new Hex_Dialog((short[][])null);
               frame.setVisible(true);
            } catch (Exception var2) {
               var2.printStackTrace();
            }

         }
      });
   }

   public Hex_Dialog(short[][] tiles) {
      this.update(tiles);
   }

   public void update(short[][] tiles) {
      if (tiles == null) {
         this.dispose();
      } else {
         this.setAlwaysOnTop(true);
         this.setIconImage(Toolkit.getDefaultToolkit().getImage(Hex_Dialog.class.getResource("/res/icons/ico_hex.png")));
         this.setTitle("Tile Hex Array");
         this.setDefaultCloseOperation(2);
         this.setBounds(100, 100, 450, 300);
         this.contentPane = new JPanel();
         this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
         this.setContentPane(this.contentPane);
         SpringLayout sl_contentPane = new SpringLayout();
         this.contentPane.setLayout(sl_contentPane);
         int longestlength = 0;

         for(int y = 0; y < tiles.length; ++y) {
            for(int x = 0; x < tiles[0].length; ++x) {
               if (Integer.toHexString(tiles[y][x]).length() > longestlength) {
                  longestlength = Integer.toHexString(tiles[y][x]).length();
               }
            }
         }

         String format = "%" + (longestlength + 2) + "s";
         String single = new String("");
         String multi = new String("{" + NEWLINE + "  ");

         for(int y = 0; y < tiles.length; ++y) {
            for(int x = 0; x < tiles[0].length; ++x) {
               String tile = String.format(format, "0x" + Integer.toHexString(tiles[y][x]).toUpperCase());
               single = single + "0x" + Integer.toHexString(tiles[y][x]).toUpperCase() + ", ";
               multi = multi + tile + ", ";
            }

            multi = multi + NEWLINE + "  ";
         }

         single = single.substring(0, single.length() - 2);
         multi = multi.substring(0, multi.length() - 2) + "};";
         this.hex_singleline = new JTextField();
         this.hex_singleline.setText(single);
         this.hex_singleline.setFont(new Font("courier new", 0, 12));
         sl_contentPane.putConstraint("North", this.hex_singleline, 10, "North", this.contentPane);
         sl_contentPane.putConstraint("West", this.hex_singleline, 10, "West", this.contentPane);
         sl_contentPane.putConstraint("East", this.hex_singleline, -10, "East", this.contentPane);
         this.contentPane.add(this.hex_singleline);
         this.hex_singleline.setColumns(10);
         this.hex_singleline.selectAll();
         JScrollPane scrollPane = new JScrollPane();
         sl_contentPane.putConstraint("North", scrollPane, 10, "South", this.hex_singleline);
         sl_contentPane.putConstraint("West", scrollPane, 0, "West", this.hex_singleline);
         sl_contentPane.putConstraint("South", scrollPane, -10, "South", this.contentPane);
         sl_contentPane.putConstraint("East", scrollPane, 0, "East", this.hex_singleline);
         this.contentPane.add(scrollPane);
         this.hex_multiline = new JTextArea();
         this.hex_multiline.setText(multi);
         this.hex_multiline.setFont(new Font("courier new", 0, 12));
         scrollPane.setViewportView(this.hex_multiline);
         this.setVisible(true);
      }
   }
}
