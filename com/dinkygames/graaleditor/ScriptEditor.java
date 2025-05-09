package com.dinkygames.graaleditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

public class ScriptEditor extends JFrame {
   private JTextField text_imagefilename;
   private JTextField text_x;
   private JTextField text_y;
   private RSyntaxTextArea txtrFunctionOncreated;
   public NPC npc;
   private static List<ScriptEditor> instances = new ArrayList();

   public static List<ScriptEditor> getInstances() {
      return instances;
   }

   public ScriptEditor(NPC npc) {
      if (npc == null) {
         npc = new NPC("", 0, 0, "", (Level)null);
      }

      if ("-".equals(npc.image)) {
         npc.image = "";
      }

      this.addWindowListener(new WindowAdapter() {
         public void windowClosed(WindowEvent e) {
            ScriptEditor.instances.remove(ScriptEditor.this);
            e.getWindow().dispose();
         }
      });
      if (instances.size() > 0) {
         Iterator var3 = instances.iterator();
         if (var3.hasNext()) {
            final ScriptEditor i = (ScriptEditor)var3.next();
            EventQueue.invokeLater(new Runnable() {
               public void run() {
                  if (this != null) {
                     i.getExtendedState();
                     int sta = 0;
                     i.setExtendedState(sta);
                     i.setAlwaysOnTop(true);
                     i.toFront();
                     i.requestFocus();
                     i.setAlwaysOnTop(false);
                  }

               }
            });
            return;
         }
      }

      this.setIconImage(Toolkit.getDefaultToolkit().getImage(CreateLink_Dialog.class.getResource("/res/icons/ico_npcs.png")));

      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | ClassNotFoundException var15) {
         var15.printStackTrace();
      }

      this.setDefaultCloseOperation(2);
      this.setMinimumSize(new Dimension(400, 300));
      this.npc = npc;
      this.setTitle("Edit NPC");
      this.getContentPane().setLayout(new BorderLayout(0, 0));
      JPanel buttonPane = new JPanel();
      buttonPane.setLayout(new FlowLayout(2));
      this.getContentPane().add(buttonPane, "South");
      JButton cancelButton = new JButton("Cancel");
      cancelButton.setActionCommand("Cancel");
      buttonPane.add(cancelButton);
      JButton okButton = new JButton("OK");
      okButton.setActionCommand("OK");
      buttonPane.add(okButton);
      this.getRootPane().setDefaultButton(okButton);
      final JPanel panel = new JPanel();
      panel.setPreferredSize(new Dimension(panel.getPreferredSize().width, 32));
      this.getContentPane().add(panel, "North");
      panel.setLayout((LayoutManager)null);
      this.text_imagefilename = new JTextField();
      this.text_imagefilename.setBounds(48, 4, 200, 24);
      this.text_imagefilename.setText(npc.image);
      panel.add(this.text_imagefilename);
      this.text_imagefilename.setColumns(10);
      JLabel label_image = new JLabel("Image:");
      label_image.setBounds(7, 9, 46, 14);
      panel.add(label_image);
      JLabel label_x = new JLabel("X:");
      label_x.setBounds(350, 9, 24, 14);
      panel.add(label_x);
      this.text_x = new JTextField();
      this.text_x.setBounds(364, 4, 32, 24);
      this.text_x.setText(String.valueOf(npc.x));
      this.text_x.setHorizontalAlignment(4);
      panel.add(this.text_x);
      this.text_x.setColumns(10);
      JLabel label_y = new JLabel("Y:");
      label_y.setBounds(408, 9, 24, 14);
      panel.add(label_y);
      this.text_y = new JTextField();
      this.text_y.setBounds(422, 4, 32, 24);
      this.text_y.setText(String.valueOf(npc.y));
      this.text_y.setHorizontalAlignment(4);
      panel.add(this.text_y);
      this.text_y.setColumns(10);
      JButton button_browse = new JButton("Browse");
      button_browse.setBounds(252, 4, 89, 24);
      panel.add(button_browse);
      button_browse.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files(*.png, *.gif, *.mng, *.jpg, *jpeg)", new String[]{"png", "gif", "mng", "jpg", "jpeg"});
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(panel);
            if (returnVal == 0) {
               ScriptEditor.this.text_imagefilename.setText(chooser.getSelectedFile().getName());
            }

         }
      });
      JSplitPane splitPane = new JSplitPane();
      splitPane.setResizeWeight(0.75D);
      this.getContentPane().add(splitPane, "Center");
      System.setProperty("http.agent", "Chrome");
      splitPane.setResizeWeight(0.75D);
      JScrollPane scrollPane = new JScrollPane();
      JTextArea scriptReferencePane = new JTextArea() {
         public Dimension getMinimumSize() {
            Dimension d = super.getMinimumSize();
            d.width = 300;
            return d;
         }
      };
      scriptReferencePane.setEditable(false);

      try {
         scriptReferencePane.setFont(new Font("courier new", 0, 12));
         scriptReferencePane.setLineWrap(false);
         
         // Fix for the NullPointerException - check if resource exists before trying to read it
         InputStream scriptFunctionStream = this.getClass().getResourceAsStream("/res/scriptfunctions_client.txt");
         if (scriptFunctionStream != null) {
            scriptReferencePane.read(new InputStreamReader(scriptFunctionStream), (Object)null);
            scriptFunctionStream.close();
         } else {
            scriptReferencePane.setText("Script functions reference not available.");
         }
      } catch (IOException var14) {
         var14.printStackTrace();
         scriptReferencePane.setText("Error loading script functions reference.");
      }

      splitPane.setRightComponent(scrollPane);
      splitPane.getRightComponent().setVisible(false);
      splitPane.setResizeWeight(0.6D);
      scrollPane.setViewportView(scriptReferencePane);
      this.txtrFunctionOncreated = new RSyntaxTextArea();
      final RTextScrollPane scrollPane_1 = new RTextScrollPane(this.txtrFunctionOncreated, true);
      splitPane.setLeftComponent(scrollPane_1);
      this.txtrFunctionOncreated.setCodeFoldingEnabled(true);
      if (npc.level != null && npc.level.scroll != null && npc.level.scroll.main != null && 
          npc.level.scroll.main.options != null && npc.level.scroll.main.options.syntaxhighlighting) {
         this.txtrFunctionOncreated.setSyntaxEditingStyle("text/javascript");
      } else {
         this.txtrFunctionOncreated.setSyntaxEditingStyle("text/plain");
      }

      // Set tab width safely
      try {
         if (npc.level != null && npc.level.scroll != null && npc.level.scroll.main != null &&
             npc.level.scroll.main.options != null) {
            this.txtrFunctionOncreated.setTabSize(npc.level.scroll.main.options.scripttabwidth);
         } else {
            this.txtrFunctionOncreated.setTabSize(2); // Default tab width
         }
      } catch (Exception e) {
         this.txtrFunctionOncreated.setTabSize(2); // Default if any error
      }
      
      this.txtrFunctionOncreated.setCurrentLineHighlightColor(new Color(245, 245, 245));
      this.txtrFunctionOncreated.setCodeFoldingEnabled(true);
      this.txtrFunctionOncreated.setAnimateBracketMatching(false);
      this.txtrFunctionOncreated.setText(npc.script != null ? npc.script : "");
      scrollPane_1.setViewportView(this.txtrFunctionOncreated);
      this.pack();
      this.setVisible(true);
      instances.add(this);
      this.setSize(800, 600);
      this.setLocationRelativeTo(npc.level != null && npc.level.scroll != null && 
                                npc.level.scroll.main != null ? npc.level.scroll.main.frame : null);
      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ScriptEditor.this.dispose();
         }
      });
      okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ScriptEditor.this.saveNPC(ScriptEditor.this.npc);
         }
      });
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent) {
            ScriptEditor.this.saveNPC(ScriptEditor.this.npc);
         }
      });
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            if (ScriptEditor.this.npc != null) {
               scrollPane_1.getViewport().setViewPosition(new Point(0, 0));
            }

         }
      });
   }

   private void saveNPC(NPC npc) {
      if (npc != null) {
         if (npc.level != null) {
            this.npc.image = this.text_imagefilename.getText();
            this.npc.script = this.txtrFunctionOncreated.getText();
            
            try {
               this.npc.x = Integer.parseInt(this.text_x.getText());
            } catch (NumberFormatException e) {
               this.npc.x = 0;
            }
            
            try {
               this.npc.y = Integer.parseInt(this.text_y.getText());
            } catch (NumberFormatException e) {
               this.npc.y = 0;
            }
            
            this.npc.level.scroll.canvas.repaint();
            this.npc.update();
            this.dispose();
         }
      }
   }
}