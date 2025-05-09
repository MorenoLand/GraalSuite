package com.dinkygames.graaleditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class Debug extends JFrame {
   private JPanel contentPane;
   private JTextArea textfield;
   private static final String NEWLINE = System.getProperty("line.separator");
   GraalEditor main;

   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
         }
      });
   }

   public Debug() {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         UIDefaults defaults = UIManager.getLookAndFeelDefaults();
         if (defaults.get("Table.alternateRowColor") == null) {
            defaults.put("Table.alternateRowColor", new Color(240, 240, 240));
         }
      } catch (InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | ClassNotFoundException var2) {
         var2.printStackTrace();
      }

      this.setTitle("Log Output");
      this.setDefaultCloseOperation(1);
      this.setBounds(100, 100, 720, 400);
      this.contentPane = new JPanel();
      this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      this.setContentPane(this.contentPane);
      this.contentPane.setLayout(new BorderLayout(0, 0));
      this.textfield = new JTextArea();
      this.textfield.setEditable(false);
      this.textfield.setFont(new Font("Courier New", 0, 12));
      this.textfield.setWrapStyleWord(true);
      this.textfield.setLineWrap(true);
      this.textfield.setText("Begin..." + NEWLINE);
      JScrollPane scrollPane = new JScrollPane(this.textfield);
      this.contentPane.add(scrollPane, "Center");
   }

   public void setVisibility(boolean b) {
      this.setVisible(b);
   }

   public void println(String txt) {
      this.textfield.setText(this.textfield.getText() + txt + NEWLINE);
   }
}
