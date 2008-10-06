/****license*****************************************************************
**   file: raListDialog.java
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
package hr.restart.util;

import hr.restart.swing.JraDialog;
import hr.restart.swing.JraScrollPane;
import hr.restart.util.propsDialog.propertyMember;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import com.borland.jbcl.layout.XYLayout;

/**
 * Title:        raListDialog
 * Description:  Dialog sa listboxom za odabrat opciju i okpanelom na dnu
 * Copyright:    Copyright (c) 2001
 * Company:      RA
 * @author AI
 * @version 1.0
 */

public abstract class raListDialog extends JraDialog implements okFrame {
  private String[] listData;
  JPanel jpMain = new JPanel();
  private int selectedIdx;
  BorderLayout borderLayout1 = new BorderLayout();
  JList jlist;
  JraScrollPane jscrollpane = new JraScrollPane();
  OKpanel okpanel = new OKpanel() {
    public void jPrekid_actionPerformed() {
      action_jPrekid();
    }
    public void jBOK_actionPerformed() {
      action_jBOK();
    }
  };
  XYLayout xYLayout1 = new XYLayout();


  public raListDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  public raListDialog(Dialog dialog, String title, boolean modal) {
    super(dialog, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  public raListDialog() {
    this((Frame)null, "", false);
  }
/**
 * listData je String[] u koji treba navesti textove u listboxu, u ovom seteru kleira se listbox, obavezno pozvati u konstruktoru
 */
  public void setListData(String[] newlistData) {
    listData=newlistData;
    initJlist(listData);
  }
  private void initJlist(Object[] lisdat) {
    jlist = new JList(lisdat);
    jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jscrollpane.setViewportView(jlist);
  }
  void setListObjects(Object[] listObjects) {
    initJlist(listObjects);
  }
/**
 * vraca jlist.getSelectedIndex(). koristi se pri action_jBOK da se vidi sto je uopce kliknuto
 */
  public int getListSelectedIndex() {
    return jlist.getSelectedIndex();
  }
  /**
   * Koji je key od oznacenog propertija
   * @return
   */
  public String getListSelectedKey() {
    return ((propertyMember)jlist.getSelectedValue()).key;
  }
  /**
   * Koji je value od oznacenog propertija
   * @return
   */  
  public String getListSelectedValue() {
    return ((propertyMember)jlist.getSelectedValue()).value;
  }
  
  public void setListSelectedIndex(int lidx) {
    jlist.setSelectedIndex(lidx);
  }

  void jbInit() throws Exception {
    jpMain.setLayout(borderLayout1);
    okpanel.setPreferredSize(new Dimension(200, 21));
    getContentPane().add(jpMain);
    jpMain.add(jscrollpane, BorderLayout.CENTER);
    jpMain.setPreferredSize(new Dimension(200, 21));
    this.getContentPane().add(okpanel, BorderLayout.SOUTH);
  }
/**
 * Poziva se pritiskom na prekid button, default je setVisible(false)
 */
  public void action_jPrekid() {
    setVisible(false);
  }
/**
 * Poziva se pritiskom na OK button, obavezno overridati
 */
  public void action_jBOK() {
  }

  public OKpanel getOKpanel() {
    return okpanel;
  }

  public abstract boolean doSaving();
}