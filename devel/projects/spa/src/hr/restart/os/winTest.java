/****license*****************************************************************
**   file: winTest.java
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
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class winTest extends JWindow {
  JPanel jPanel1 = new JPanel();
  File rootFile = new File("C:/");
  File selectedFile = new File("C:/");
  JList fileList = new JList(getDirs());
  JList playList = new JList(getPlay());
  String[] selectedItems;
  String[] playableItems;
  JButton jButton1 = new JButton();

  public winTest() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    jPanel1.setLayout(null);
    this.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        this_keyPressed(e);
      }
    });
    fileList.setBounds(new Rectangle(0, 0, 200, 300));
    playList.setBounds(new Rectangle(300, 0, 100, 300));
    jButton1.setBounds(new Rectangle(222, 25, 47, 42));
    this.getContentPane().add(jPanel1,  BorderLayout.CENTER);
    jPanel1.add(fileList, null);
    jPanel1.add(playList, null);
    jPanel1.add(jButton1, null);
  }
  String[] getDirs() {
    System.out.println("getDirs: "+this.selectedFile);
    selectedItems = selectedFile.list();
    return selectedItems;
  }
  String[] getPlay() {
    playableItems=new String[] {};
    return playableItems;
  }
  public void show() {
    super.show();
    fileList.requestFocus();
  }
  void this_keyPressed(KeyEvent e) {
    if (e.getKeyCode()==e.VK_ENTER) {
      System.out.println("Enter: "+fileList.getSelectedValue());
      selectedFile=new File(selectedFile.toString()+"/"+fileList.getSelectedValue().toString());
      if (selectedFile.isDirectory()) {
        fileList.clearSelection();
        fileList.setListData(getDirs());
        fileList.repaint();
        fileList.setSelectedIndex(0);
      }
      else {
        System.out.println("Pjevam: "+selectedFile);

        playList.clearSelection();
        playList.setListData(new String[] {selectedFile.toString()});
//        playList.setListData(getDirs());
        playList.repaint();
//        playList.setSelectedIndex(0);
        selectedFile=selectedFile.getParentFile();
      }
    }
    else if (e.getKeyCode()==e.VK_ESCAPE) {
      System.out.println("Escape ");
      selectedFile=selectedFile.getParentFile();
      fileList.clearSelection();
      fileList.setListData(getDirs());
      fileList.repaint();
      fileList.setSelectedIndex(0);
    }
  }
}