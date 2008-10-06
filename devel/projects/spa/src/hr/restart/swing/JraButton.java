/****license*****************************************************************
**   file: JraButton.java
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

import hr.restart.util.Aus;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class JraButton extends
/*
hr.restart.util.raNavAction {
  private java.awt.event.ActionListener alis;
  private javax.swing.border.Border standardBorder = javax.swing.BorderFactory.createEtchedBorder();
  public JraButton() {
    super("",null,0,true);
    setBorder(standardBorder);
    setIcon(null);
  }
  public JraButton(javax.swing.Icon ic) {
    super("");
    setBorder(standardBorder);
    setIcon(ic);
  }
  public void setEnabled(boolean en) {
    super.setEnabled(en);
    setNavBorder(null);
  }
  public void addActionListener(java.awt.event.ActionListener l) {
    alis = l;
  }

  public void actionPerformed(java.awt.event.ActionEvent e) {
    alis.actionPerformed(e);
  }
  public void setNavBorder(javax.swing.border.Border bord) {
    if (bord==null) bord = standardBorder;
    super.setNavBorder(bord);
  }
  public void doClick() {
    alis.actionPerformed(null);
  }
}
*/

  JButton {

  static boolean insideAction = false;  // Ante
  private JraButton_FocusListener automaticFocusLost;

  // ante
  public static boolean isInsideAction() {
    return insideAction;
  }
  // ante

  {
    this.setDefaultCapable(false);
    automaticFocusLost = new JraButton_FocusListener();
    /*addKeyListener(new JraKeyListener(null,true));*/
//    this.addKeyListener(new JraButton_KeyListener());
    // ante
    this.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
//        System.err.println("press");
        insideAction = true;
      }

      public void mouseReleased(MouseEvent e) {
//        System.err.println("release");
        insideAction = false;
      }
    });
// ante


  }
  public boolean isFocusTraversable() {
    return false;
  }

  // ab.f
  // Odredjuje hoce li button automatski fokusirati
  // sljedecu komponentu ako je privremeno izgubio fokus.
  // Buttoni koji otvaraju modalne dialoge trebaju ovo imati
  // ukljuceno (da prebace fokus dalje prije otvaranja nekakvog
  // dohvata i slicno), no ostali buttoni bi ovo trebali ikljuciti.
  // OKPanel buttoni su tipican primjer.
  public void setAutomaticFocusLost(boolean auto) {
    this.removeFocusListener(automaticFocusLost);
    if (auto) this.addFocusListener(automaticFocusLost);
  }
  class JraButton_FocusListener extends java.awt.event.FocusAdapter {
    public void focusGained(java.awt.event.FocusEvent e) {
    }
    public void focusLost(java.awt.event.FocusEvent e) {
      if (e.isTemporary()) JraKeyListener.focusNext(e);
    }
  }
  
  public void addNotify() {
    super.addNotify();
    if (isFocusTraversable())
      Aus.installEnterRelease(this);
  }
  
  public void removeNotify() {
    super.removeNotify();
    AWTKeyboard.unregisterComponent(this);
  }
}