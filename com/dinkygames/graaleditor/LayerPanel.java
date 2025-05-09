package com.dinkygames.graaleditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class LayerPanel extends JPanel implements MouseListener {
   private GraalEditor main;
   public int layerindex = 0;
   public boolean selected = false;
   private JCheckBox checkbox_visibility;
   private JButton button_up;
   private JButton button_down;
   private JLabel icon;
   private JTextField name;

   public LayerPanel(GraalEditor main, final int layerindex) {
      this.main = main;
      this.addMouseListener(this);
      this.layerindex = layerindex;
      this.setAlignmentX(0.0F);
      this.setAlignmentY(0.0F);
      SpringLayout sl_panel_1 = new SpringLayout();
      this.setLayout(sl_panel_1);
      this.checkbox_visibility = new JCheckBox("");
      sl_panel_1.putConstraint("North", this.checkbox_visibility, 26, "North", this);
      sl_panel_1.putConstraint("West", this.checkbox_visibility, 5, "West", this);
      this.checkbox_visibility.setSelected(true);
      this.add(this.checkbox_visibility);
      this.checkbox_visibility.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            boolean state = LayerPanel.this.checkbox_visibility.isSelected();
            ((Layer)LayerPanel.this.main.getCurrentItem().level.layers.get(layerindex)).setVisibility(state);
         }
      });
      this.icon = new JLabel();
      sl_panel_1.putConstraint("North", this.icon, 5, "North", this);
      sl_panel_1.putConstraint("West", this.icon, 31, "West", this);
      this.icon.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/images/layerpreviewfiller.png")));
      this.add(this.icon);
      this.button_up = new JButton("");
      sl_panel_1.putConstraint("North", this.button_up, 0, "North", this.icon);
      sl_panel_1.putConstraint("East", this.button_up, -10, "East", this);
      this.button_up.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_fewerdetails.png")));
      this.add(this.button_up);
      this.button_down = new JButton("");
      sl_panel_1.putConstraint("South", this.button_down, 0, "South", this.icon);
      sl_panel_1.putConstraint("East", this.button_down, 0, "East", this.button_up);
      this.button_down.setIcon(new ImageIcon(GraalEditor.class.getResource("/res/icons/ico_moredetails.png")));
      this.add(this.button_down);
      this.button_up.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            LayerPanel.this.main.getCurrentItem().level.moveLayerUp(layerindex);
         }
      });
      this.button_down.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            LayerPanel.this.main.getCurrentItem().level.moveLayerDown(layerindex);
         }
      });
      if (this.layerindex == this.main.getCurrentItem().level.layers.size() - 1) {
         this.button_up.setEnabled(false);
      }

      if (this.layerindex == 0) {
         this.button_down.setEnabled(false);
      }

      if (this.selected) {
         this.setBackground(new Color(181, 200, 250, 255));
      } else {
         this.setBackground(UIManager.getColor("Panel.background"));
      }

      int h = 74;
      this.setMinimumSize(new Dimension(200, h));
      this.setMaximumSize(new Dimension(Integer.MAX_VALUE, h));
      this.setPreferredSize(new Dimension(200, h));
      this.name = new JTextField();
      sl_panel_1.putConstraint("North", this.name, 29, "North", this);
      sl_panel_1.putConstraint("West", this.name, 70, "West", this.icon);
      this.name.setBackground((Color)null);
      this.name.setBorder((Border)null);
      this.name.setEnabled(false);
      this.name.setDisabledTextColor(new Color(0, 0, 0, 255));
      this.name.addMouseListener(new MouseListener() {
         public void mouseClicked(MouseEvent arg0) {
            if (arg0.getClickCount() >= 2 && arg0.getButton() == 1) {
               final JPopupMenu popup = new JPopupMenu();
               final JTextField text = new JTextField("                                ");
               popup.add(text);
               LayerPanel.this.name.setComponentPopupMenu(popup);
               popup.show(LayerPanel.this.name, LayerPanel.this.name.getX() - 104, LayerPanel.this.name.getY() - 34);
               text.setText(LayerPanel.this.name.getText());
               text.requestFocusInWindow();
               text.selectAll();
               Document doc = text.getDocument();
               doc.addDocumentListener(new DocumentListener() {
                  public void changedUpdate(DocumentEvent arg0) {
                     this.update();
                  }

                  public void insertUpdate(DocumentEvent arg0) {
                     this.update();
                  }

                  public void removeUpdate(DocumentEvent arg0) {
                     this.update();
                  }

                  private void update() {
                  }
               });
               text.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent arg0) {
                     String newtext = text.getText();
                     LayerPanel.this.name.setText(newtext);
                     ((Layer)LayerPanel.this.main.getCurrentItem().level.layers.get(layerindex)).setName(newtext);
                     popup.setVisible(false);
                  }
               });
            } else {
               LayerPanel.this.main.changeSelectedLayer(LayerPanel.this.layerindex);
            }

         }

         public void mouseEntered(MouseEvent arg0) {
         }

         public void mouseExited(MouseEvent arg0) {
         }

         public void mousePressed(MouseEvent arg0) {
         }

         public void mouseReleased(MouseEvent arg0) {
         }
      });
      this.add(this.name);
      this.name.setColumns(10);
   }

   public void updateName(String s) {
      if (s == null) {
         s = "Layer " + this.layerindex;
      }

      this.name.setText(s);
   }

   public void updateThumbnail(BufferedImage image) {
      BufferedImage thumb = new BufferedImage(64, 64, 2);
      Graphics2D g2d = (Graphics2D)thumb.getGraphics();
      g2d.drawImage(GraalEditor.thumbnailbase, 0, 0, (ImageObserver)null);
      g2d.drawImage(image, 0, 0, 64, 64, 0, 0, 1024, 1024, (ImageObserver)null);
      this.icon.setIcon(new ImageIcon(thumb));
      g2d.dispose();
   }

   public void setSelected(boolean val) {
      this.selected = val;
      if (this.selected) {
         this.setBackground(new Color(181, 200, 250, 255));
      } else {
         this.setBackground(UIManager.getColor("Panel.background"));
      }

      this.repaint();
   }

   public void updateVisibilityCheckbox(boolean val) {
      this.checkbox_visibility.setSelected(val);
   }

   public void mouseClicked(MouseEvent e) {
      this.main.changeSelectedLayer(this.layerindex);
   }

   public void mouseEntered(MouseEvent e) {
   }

   public void mouseExited(MouseEvent e) {
   }

   public void mousePressed(MouseEvent e) {
   }

   public void mouseReleased(MouseEvent e) {
   }
}
