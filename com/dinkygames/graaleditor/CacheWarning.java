package com.dinkygames.graaleditor;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class CacheWarning extends JDialog {
   private static final long serialVersionUID = 1875449211068554728L;
   private JLabel status;

   public CacheWarning() {
      this.setTitle("Please Wait...");
      this.setResizable(false);
      this.setDefaultCloseOperation(0);
      this.setAlwaysOnTop(true);
      this.setSize(303, 128);
      this.getContentPane().setLayout(new BorderLayout(0, 0));
      JLabel lblNewLabel = new JLabel("Scanning Graal Folder and Creating a Cache...");
      lblNewLabel.setAlignmentX(0.5F);
      lblNewLabel.setIcon(new ImageIcon(CacheWarning.class.getResource("/res/loading_32x.gif")));
      lblNewLabel.setBorder(new EmptyBorder(17, 17, 17, 17));
      this.getContentPane().add(lblNewLabel);
      this.status = new JLabel("Loading...");
      this.status.setBorder(new EmptyBorder(4, 4, 4, 4));
      this.getContentPane().add(this.status, "South");
      this.setModalityType(ModalityType.APPLICATION_MODAL);
   }

   public void setStatus(String s) {
      this.status.setText(s);
   }
}
