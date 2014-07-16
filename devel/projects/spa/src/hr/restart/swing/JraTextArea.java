/****license*****************************************************************
**   file: JraTextArea.java
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
package hr.restart.swing;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.borland.dbswing.JdbTextArea;

public class JraTextArea extends JdbTextArea {
  //JTextField jtborder = new JTextField();
  private int rowsText = -1;
  public static JraTextArea currentFocus = null;
  
  public JraTextArea() {
    addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        checkRows();
      }
    });
    dataBinder.setPostOnFocusLost(false);
    dataBinder.setPostOnRowPosted(false);
    
    addFocusListener(new FocusListener() {
      public void focusLost(FocusEvent e) {
        if (currentFocus == JraTextArea.this) posText();
        currentFocus = null;
      }
      public void focusGained(FocusEvent e) {
        System.out.println("focgained");
        currentFocus = JraTextArea.this;
      }
    });
  }
  
  public void posText() {
    currentFocus = null;
    System.out.println("pos text");
    dataBinder.postText();
  }
  /*private void checkUI() {
    if (jtborder == null) return;
    jtborder.updateUI();
    setFont(jtborder.getFont());
  }
  public void updateUI() {
    super.updateUI();
    checkUI();
  }*/
  private void checkRows() {
    if (getRowsText() <= 0) return;
    if (getWrittenRows() > getRowsText()) {
      int lfc = getText().lastIndexOf("\n");
      String newText = getText().substring(0,lfc);
      setText(newText);
    }
  }
  public int getWrittenRows() {
    char[] txt = getText().toCharArray();
    char lf = "\n".toCharArray()[0];
    int rws = 1;
    for (int i = 0; i < txt.length; i++) {
      if (txt[i] == lf) rws = rws+1;
    }
    return rws;
  }
  /*public void setBorder(Border b) {
    if (jtborder != null) jtborder.setBorder(b);
    super.setBorder(b);
  }*/
  public void setRowsText(int rowsText) {
    this.rowsText = rowsText;
  }
  public int getRowsText() {
    return rowsText;
  }
}