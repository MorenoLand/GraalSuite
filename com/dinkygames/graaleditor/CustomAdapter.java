package com.dinkygames.graaleditor;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

class CustomAdapter extends MouseAdapter {
   private JTabbedPane tabbedPane;
   private ScrollPane_Tileset scroll_tileset;
   private GraalEditor main;

   public CustomAdapter(GraalEditor main, JTabbedPane tabbedPane, ScrollPane_Tileset scroll_tileset) {
      this.main = main;
      this.scroll_tileset = scroll_tileset;
      this.tabbedPane = tabbedPane;
   }

   public void mousePressed(MouseEvent e) {
      final int index = this.tabbedPane.getUI().tabForCoordinate(this.tabbedPane, e.getX(), e.getY());
      if (index != -1) {
         if (SwingUtilities.isLeftMouseButton(e)) {
            if (this.tabbedPane.getSelectedIndex() != index) {
               this.tabbedPane.setSelectedIndex(index);
            } else if (this.tabbedPane.isRequestFocusEnabled()) {
               this.tabbedPane.requestFocusInWindow();
            }
         } else if (SwingUtilities.isMiddleMouseButton(e)) {
            this.main.closeTab(index);
         } else if (SwingUtilities.isRightMouseButton(e)) {
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem addNew = new JMenuItem("Add new");
            addNew.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                  CustomAdapter.this.main.newLevel();
               }
            });
            popupMenu.add(addNew);
            JMenuItem close = new JMenuItem("Close");
            close.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                  CustomAdapter.this.main.closeTab(index);
               }
            });
            popupMenu.add(close);
            JMenuItem closeAll = new JMenuItem("Close all");
            closeAll.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                  CustomAdapter.this.tabbedPane.removeAll();
               }
            });
            popupMenu.add(closeAll);
            JMenuItem closeOthers = new JMenuItem("Close Other Tabs");
            closeOthers.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                  CustomAdapter.this.main.removeTabsOther();
               }
            });
            popupMenu.add(closeOthers);
            JMenuItem closeRight = new JMenuItem("Close Tabs to Right");
            closeRight.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                  CustomAdapter.this.main.removeTabsRight();
               }
            });
            popupMenu.add(closeRight);
            Rectangle tabBounds = this.tabbedPane.getBoundsAt(index);
            popupMenu.show(this.tabbedPane, tabBounds.x, tabBounds.y + tabBounds.height);
         }
      }

   }
}
