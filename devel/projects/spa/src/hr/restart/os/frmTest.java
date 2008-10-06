/****license*****************************************************************
**   file: frmTest.java
**   Copyright 2006 Rest Art
**
**   Licensed under the Apache License, Version 2.0 (the "License");
**   you may not use this file except in compliance with the License.
**   You may obtain a copy of the License at
**
**       http://www.apache.org/licenses/LICENSE-2.0
**
**   Unless required by applicable law or agreed to in writing, software
**   distributed under the License is distributed on an "AS IS" BASIS,
**   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
**   See the License for the specific language governing permissions and
**   limitations under the License.
**
****************************************************************************/
package hr.restart.os;

import java.awt.BorderLayout;
import java.awt.MediaTracker;
import java.awt.event.ComponentEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author REST-ART development team
 * @version 1.0
 */

public class frmTest extends JDialog {
  JPanel jPanel1 = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  File rootFile = new File("C:/MP3-Hrvoje/");
  File selectedFile = new File("C:/MP3-Hrvoje/");
  JList playList = new JList(getDirs());
  String[] selectedItems;
  ImageIcon previewImg = null;
  JPanel jpPreviewImage = new JPanel() {
    public void paint(java.awt.Graphics g) {
      super.paint(g);
      if (previewImg!=null) g.drawImage(previewImg.getImage(),0,0,getWidth(),getHeight(),null);
    }
  };

  public frmTest() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    jPanel1.setLayout(xYLayout1);
    this.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        this_keyPressed(e);
      }
    });
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        this_componentShown(e);
      }
    });
//    playList.setBackground(new Color(176, 154, 255));
//    playList.setFont(new java.awt.Font("Dialog", 2, 14));
//    playList.setForeground(Color.red);
    playList.addInputMethodListener(new java.awt.event.InputMethodListener() {
      public void inputMethodTextChanged(InputMethodEvent e) {
      }
      public void caretPositionChanged(InputMethodEvent e) {
        playList_caretPositionChanged(e);
      }
    });
    this.getContentPane().add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(playList,     new XYConstraints(0, 0, 300, 300));
    jPanel1.add(jpPreviewImage,     new XYConstraints(300, 0, 300, 300));
  }
  String[] getDirs() {
    System.out.println("getDirs: "+this.selectedFile);
    this.selectedItems = this.selectedFile.list();
    return this.selectedItems;
  }

  void this_keyPressed(KeyEvent e) {
    if (e.getKeyCode()==e.VK_ENTER) {
      System.out.println("Enter: "+playList.getSelectedValue());
      this.selectedFile=new File(this.selectedFile.toString()+"/"+playList.getSelectedValue().toString());
      if (selectedFile.isDirectory()) {
        playList.clearSelection();
        playList.setListData(getDirs());
        playList.repaint();
        playList.setSelectedIndex(0);
      }
      else {
        System.out.println("Pjevam: "+selectedFile);
      }
    }
    else if (e.getKeyCode()==e.VK_ESCAPE) {
      System.out.println("Escape ");
      selectedFile=this.selectedFile.getParentFile();
      playList.clearSelection();
      playList.setListData(getDirs());
      playList.repaint();
      playList.setSelectedIndex(0);
    }
  }

  void this_componentShown(ComponentEvent e) {
    System.out.println("Component Shown");
//    this.playList.requestFocus();
    this.playList.setSelectedIndex(0);
  }

  void playList_caretPositionChanged(InputMethodEvent e) {
    System.out.println("posChanged: ");
  }
  boolean setImage(String filename) {
    try {
      if (filename == null) {
        previewImg = null;
//        jlfile.setText("");
        jpPreviewImage.paint(jpPreviewImage.getGraphics());
        return true;
      }
      previewImg = new ImageIcon(filename);
      if (previewImg.getImageLoadStatus() == MediaTracker.COMPLETE) {
//        jlfile.setText(filename);
        jpPreviewImage.paint(jpPreviewImage.getGraphics());
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
  }
}
