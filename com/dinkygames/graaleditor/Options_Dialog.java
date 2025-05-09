package com.dinkygames.graaleditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;

public class Options_Dialog extends JDialog {
   private JTextField textField;
   private JTextField textfield_graaldir;
   public Options options;

   public Options_Dialog(final Options options) {
      this.options = options;

      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | ClassNotFoundException var19) {
         var19.printStackTrace();
      }

      this.setTitle("Options");
      this.setResizable(false);
      this.setModal(true);
      this.setAlwaysOnTop(true);
      this.setDefaultCloseOperation(2);
      this.setMinimumSize(new Dimension(400, 250));
      this.setBounds(options.main.frame.getX() + (options.main.frame.getWidth() - 450) / 2, options.main.frame.getY() + (options.main.frame.getHeight() - 240) / 2, 450, 240);
      this.getContentPane().setLayout(new BorderLayout());
      JPanel buttonPane = new JPanel();
      buttonPane.setLayout(new FlowLayout(2));
      this.getContentPane().add(buttonPane, "South");
      JButton button_refreshCache = new JButton("Refresh Cache");
      buttonPane.add(button_refreshCache);
      button_refreshCache.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Options_Dialog.this.options.main.filecache.updateCacheFile();
            options.main.scroll_tileset.updateTileset(true);
         }
      });
      Component horizontalStrut = Box.createHorizontalStrut(20);
      horizontalStrut.setPreferredSize(new Dimension(184, 0));
      buttonPane.add(horizontalStrut);
      JButton saveButton = new JButton("Save");
      saveButton.setActionCommand("Save");
      buttonPane.add(saveButton);
      JButton cancelButton = new JButton("Cancel");
      cancelButton.setActionCommand("Cancel");
      buttonPane.add(cancelButton);
      this.getRootPane().setDefaultButton(saveButton);
      JTabbedPane tabbedPane = new JTabbedPane(1);
      this.getContentPane().add(tabbedPane, "Center");
      JPanel panel = new JPanel();
      tabbedPane.addTab("General", (Icon)null, panel, (String)null);
      panel.setLayout((LayoutManager)null);
      JLabel lblGraalDirectory_1 = new JLabel("Graal Directory");
      lblGraalDirectory_1.setHorizontalAlignment(0);
      lblGraalDirectory_1.setOpaque(true);
      lblGraalDirectory_1.setBounds(24, 2, 82, 20);
      panel.add(lblGraalDirectory_1);
      final JCheckBox chckbxContrastSelection = new JCheckBox("Contrast Selection");
      chckbxContrastSelection.setSelected(options.contrastselection);
      chckbxContrastSelection.setToolTipText("Darkens non-selected tiles");
      chckbxContrastSelection.setBounds(149, 87, 119, 23);
      panel.add(chckbxContrastSelection);
      final JCheckBox chckbxVisualizeSetshape = new JCheckBox("Visualize setShape");
      chckbxVisualizeSetshape.setSelected(options.visualsetshape);
      chckbxVisualizeSetshape.setToolTipText("Gives NPCs with setShape a visual boundary.");
      chckbxVisualizeSetshape.setBounds(149, 63, 119, 23);
      panel.add(chckbxVisualizeSetshape);
      final JCheckBox chckbxSytaxHighlighting = new JCheckBox("Syntax Highlighting");
      chckbxSytaxHighlighting.setSelected(options.syntaxhighlighting);
      chckbxSytaxHighlighting.setToolTipText("Provides syntax highlighting in the script editor");
      chckbxSytaxHighlighting.setBounds(270, 87, 119, 23);
      panel.add(chckbxSytaxHighlighting);
      this.textField = new JTextField();
      this.textField.setText(Integer.toString(options.scripttabwidth));
      this.textField.setBounds(263, 112, 24, 20);
      panel.add(this.textField);
      this.textField.setColumns(2);
      JLabel lblIndentAmount = new JLabel("Tab Width");
      lblIndentAmount.setBounds(290, 115, 78, 14);
      panel.add(lblIndentAmount);
      this.textfield_graaldir = new JTextField();
      this.textfield_graaldir.setText(options.graaldir);
      this.textfield_graaldir.setBounds(24, 23, 281, 20);
      panel.add(this.textfield_graaldir);
      this.textfield_graaldir.setColumns(10);
      JButton button_browse = new JButton("Browse");
      button_browse.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
         }
      });
      button_browse.setBounds(315, 21, 89, 24);
      panel.add(button_browse);
      JLabel lblGraalDirectory = new JLabel("");
      lblGraalDirectory.setVerticalAlignment(1);
      lblGraalDirectory.setBounds(10, 11, 408, 45);
      lblGraalDirectory.setBorder(new EtchedBorder(1, (Color)null, (Color)null));
      panel.add(lblGraalDirectory);
      final JCheckBox chckbxUseFilenamecache = new JCheckBox("Use FILENAMECACHE");
      chckbxUseFilenamecache.setSelected(options.usefilenamecache);
      chckbxUseFilenamecache.setSelected(false);
      chckbxUseFilenamecache.setBounds(10, 63, 137, 23);
      panel.add(chckbxUseFilenamecache);
      chckbxUseFilenamecache.setEnabled(false);
      final JCheckBox chckbxUsetiledefsoffline = new JCheckBox("Use tiledefsOffline");
      chckbxUsetiledefsoffline.setSelected(options.usetiledefsoffline);
      chckbxUsetiledefsoffline.setSelected(true);
      chckbxUsetiledefsoffline.setBounds(10, 89, 119, 23);
      panel.add(chckbxUsetiledefsoffline);
      chckbxUsetiledefsoffline.setEnabled(false);
      final JCheckBox chckbxScriptParsing = new JCheckBox("Script Parsing");
      chckbxScriptParsing.setSelected(options.scriptparsing);
      chckbxScriptParsing.setBounds(270, 63, 97, 23);
      panel.add(chckbxScriptParsing);
      chckbxScriptParsing.setToolTipText("Disable if you experience crashing when loading levels.");
      button_browse.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(1);
            int returnVal = chooser.showOpenDialog(Options_Dialog.this);
            if (returnVal == 0) {
               options.updateGraalDir(chooser.getSelectedFile().toString());
               Options_Dialog.this.textfield_graaldir.setText(options.graaldir);
               options.main.filecache.updateHash();
               options.main.updatePredefinedList();
               options.main.updateTileDefinitions();
               options.main.scroll_tileset.updateTileset(true);
            }

         }
      });
      saveButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               Options_Dialog.this.options.scripttabwidth = Integer.parseInt(Options_Dialog.this.textField.getText());
            } catch (NullPointerException | NumberFormatException var3) {
               JOptionPane.showMessageDialog((Component)null, "Tab Width must be a numeric value!", "ERROR!", 1);
               return;
            }

            Options_Dialog.this.options.contrastselection = chckbxContrastSelection.isSelected();
            Options_Dialog.this.options.visualsetshape = chckbxVisualizeSetshape.isSelected();
            Options_Dialog.this.options.syntaxhighlighting = chckbxSytaxHighlighting.isSelected();
            Options_Dialog.this.options.usefilenamecache = chckbxUseFilenamecache.isSelected();
            Options_Dialog.this.options.usetiledefsoffline = chckbxUsetiledefsoffline.isSelected();
            Options_Dialog.this.options.scriptparsing = chckbxScriptParsing.isSelected();
            Options_Dialog.this.options.save();
            Options_Dialog.this.dispose();
         }
      });
      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Options_Dialog.this.dispose();
         }
      });
      this.pack();
      this.setVisible(true);
      this.setBounds(options.main.frame.getX() + (options.main.frame.getWidth() - 450) / 2, options.main.frame.getY() + (options.main.frame.getHeight() - 240) / 2, 450, 240);
   }
}
