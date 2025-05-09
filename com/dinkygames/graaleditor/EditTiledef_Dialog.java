package com.dinkygames.graaleditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class EditTiledef_Dialog extends JDialog implements ActionListener {
   private final JPanel contentPanel = new JPanel();
   private JTextField text_image;
   private JTextField text_prefix;
   private final JPanel panel = new JPanel();
   private JTextField text_x;
   private JTextField text_y;
   private JComboBox combo_type;
   private TilesetDefinitions_Dialog parent;
   private int oldindex = -1;

   public EditTiledef_Dialog(TilesetDefinitions_Dialog parent, int oldindex) {
      if (parent == null) {
         this.dispose();
      } else {
         this.oldindex = oldindex;
         Object[] from = new Object[]{true, "", "", 0, 0, 0};
         if (oldindex >= 0) {
            from = (Object[])parent.tiledefs.tiledefinitions.get(oldindex);
         }

         this.parent = parent;
         this.setIconImage(Toolkit.getDefaultToolkit().getImage(CreateLink_Dialog.class.getResource("/res/icons/ico_tiledefs.png")));
         this.setTitle("Edit Tile Definition");

         try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIDefaults var4 = UIManager.getLookAndFeelDefaults();
         } catch (InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | ClassNotFoundException var18) {
            var18.printStackTrace();
         }

         this.setResizable(false);
         this.setBounds(parent.getX() + (parent.getWidth() - 440) / 2, parent.getY() + (parent.getHeight() - 184) / 2, 440, 184);
         this.getContentPane().setLayout(new BorderLayout());
         this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
         this.getContentPane().add(this.contentPanel, "Center");
         this.contentPanel.setLayout((LayoutManager)null);
         JLabel lblImage = new JLabel("Image:");
         lblImage.setBounds(10, 12, 46, 14);
         this.contentPanel.add(lblImage);
         this.text_image = new JTextField((String)from[1]);
         this.text_image.setBounds(50, 8, 282, 21);
         this.contentPanel.add(this.text_image);
         this.text_image.setColumns(10);
         JButton btnBrowse = new JButton("Browse");
         btnBrowse.setBounds(335, 7, 89, 23);
         btnBrowse.setEnabled(false);
         this.contentPanel.add(btnBrowse);
         JLabel lblPrefix = new JLabel("Prefix:");
         lblPrefix.setBounds(10, 41, 46, 14);
         this.contentPanel.add(lblPrefix);
         this.text_prefix = new JTextField((String)from[2]);
         this.text_prefix.setBounds(50, 37, 374, 21);
         this.contentPanel.add(this.text_prefix);
         this.text_prefix.setColumns(10);
         JPanel panel_1 = new JPanel();
         panel_1.setBorder(new EmptyBorder(0, 4, 0, 4));
         panel_1.setBounds(20, 61, 111, 21);
         this.contentPanel.add(panel_1);
         JLabel lblNewLabel = new JLabel("Definition Attributes");
         panel_1.add(lblNewLabel);
         this.panel.setBorder(new EtchedBorder(1, (Color)null, (Color)null));
         this.panel.setBounds(10, 74, 415, 43);
         this.contentPanel.add(this.panel);
         this.panel.setLayout((LayoutManager)null);
         int[] typelookup = new int[]{0, 1, 0, 0, 3, 2};
         this.combo_type = new JComboBox();
         this.combo_type.setBounds(48, 11, 170, 20);
         this.combo_type.setModel(new DefaultComboBoxModel(new String[]{"0 - Default Style", "1 - New World Style", "5 - 3D Terrain Style", "4 - addtiledef2"}));
         this.panel.add(this.combo_type);
         this.combo_type.setSelectedIndex(typelookup[(Integer)from[3]]);
         JLabel lblType = new JLabel("Type:");
         lblType.setBounds(10, 14, 46, 14);
         this.panel.add(lblType);
         JLabel lblX = new JLabel("X:");
         lblX.setBounds(230, 14, 20, 14);
         this.panel.add(lblX);
         String xtxt = from[4].toString();
         this.text_x = new JTextField();
         this.text_x.setText(xtxt == null ? "0" : xtxt);
         this.text_x.setBounds(250, 11, 60, 20);
         this.panel.add(this.text_x);
         this.text_x.setColumns(10);
         this.text_x.setEnabled(false);
         JLabel lblY = new JLabel("Y:");
         lblY.setBounds(324, 14, 20, 14);
         this.panel.add(lblY);
         String ytxt = from[5].toString();
         this.text_y = new JTextField(from[5].toString());
         this.text_y.setText(ytxt == null ? "0" : ytxt);
         this.text_y.setColumns(10);
         this.text_y.setBounds(344, 11, 60, 20);
         this.text_y.setEnabled(false);
         this.panel.add(this.text_y);
         JPanel buttonPane = new JPanel();
         buttonPane.setLayout(new FlowLayout(2));
         this.getContentPane().add(buttonPane, "South");
         JButton okButton = new JButton("OK");
         okButton.addActionListener(this);
         okButton.setActionCommand("ok");
         buttonPane.add(okButton);
         JButton cancelButton = new JButton("Cancel");
         cancelButton.addActionListener(this);
         cancelButton.setActionCommand("cancel");
         buttonPane.add(cancelButton);
         this.combo_type.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
               EditTiledef_Dialog.this.updateComboList();
            }
         });
         this.updateComboList();
         this.setModalityType(ModalityType.APPLICATION_MODAL);
         this.setAlwaysOnTop(true);
         this.setVisible(true);
         this.repaint();
      }
   }

   private void updateComboList() {
      if (this.combo_type.getSelectedIndex() != 3) {
         this.text_x.setEnabled(false);
         this.text_y.setEnabled(false);
      } else {
         this.text_x.setEnabled(true);
         this.text_y.setEnabled(true);
      }

   }

   public void actionPerformed(ActionEvent action) {
      String cmd = action.getActionCommand();
      if (cmd == "ok") {
         try {
            int[] typelookup = new int[]{0, 1, 5, 4};
            String image = this.text_image.getText();
            String prefix = this.text_prefix.getText();
            int type = typelookup[this.combo_type.getSelectedIndex()];
            int x = Integer.parseInt(this.text_x.getText());
            int y = Integer.parseInt(this.text_y.getText());
            Object[] newdef = new Object[]{true, image, prefix, type, x, y};
            if (this.oldindex < 0) {
               this.parent.tiledefs.addDefinition(newdef);
            } else {
               this.parent.tiledefs.updateDefinition(this.oldindex, newdef);
            }

            this.parent.updateTableData(this.parent.tiledefs.getTileDefsAsArray());
            this.parent.dialogadd = null;
            this.dispose();
         } catch (NullPointerException | NumberFormatException var10) {
            JOptionPane.showMessageDialog(this, "Value must be numeric only!", "ERROR!", 1);
            return;
         }
      } else if (cmd == "cancel") {
         this.parent.dialogadd = null;
         this.dispose();
      }

   }
}
