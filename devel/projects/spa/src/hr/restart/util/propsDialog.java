/****license*****************************************************************
**   file: propsDialog.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raExtendedTable;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class propsDialog extends JraDialog implements navFrame, loadFrame, okFrame {
sysoutTEST ST = new sysoutTEST(false);
  private String propfile = null;
  private boolean doSave = false;
  private boolean sysprop = false;
  
  private raNavBar navbar = new raNavBar(raNavBar.EMPTY);
  
  JraTable2 mpt = new raExtendedTable() {
    public void killFocus(java.util.EventObject e) {
      //okp.jPrekid.requestFocus();
    }
    public void tableDoubleClicked() {
      jp.mpTable_doubleClicked();
    }
    public void fireTableDataChanged() {
      super.fireTableDataChanged();
    }
  };
  public raJPTableView jp = new raJPTableView(mpt) {
    {
      setCreateCB(false);
    }
    public void mpTable_doubleClicked() {
      props_upd();
    }
  };
  
  OKpanel okpanel = new OKpanel() {
    public void jPrekid_actionPerformed() {
      action_jPrekid();
    }
    public void jBOK_actionPerformed() {
      action_jBOK();
    }
  };
  
  protected StorageDataSet data;
  protected Properties props4list;
  private propEdit propedit = new propEdit(this);
  /*private raNavBar navbar = new raNavBar(raNavBar.DBNAVIGATE) {
    public void add_action() {
      props_add();
    }

    public void update_action() {
      props_upd();
    }

    public void delete_action() {
      props_del();
    }
  };*/
  
  raNavAction rnvAdd = new raNavAction("Novi", raImages.IMGADD, KeyEvent.VK_F2) {
    public void actionPerformed(ActionEvent e) {
      props_add();
    }
  };
  raNavAction rnvEdit = new raNavAction("Izmjena", raImages.IMGCHANGE, KeyEvent.VK_F4) {
    public void actionPerformed(ActionEvent e) {
      props_upd();
    }
  };
  raNavAction rnvDelete = new raNavAction("Briši", raImages.IMGDELETE, KeyEvent.VK_F3) {
    public void actionPerformed(ActionEvent e) {
      props_del();
    }
  };
  raNavAction rnvDelAll = new raNavAction("Obriši sve",raImages.IMGDELALL,KeyEvent.VK_F8) {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        props_delall();
      }
  };
  public propsDialog(Properties props,String tit) {
    init(props,tit);
  }

  public propsDialog(Frame owner, Properties props,String tit) {
    super(owner,"",true);
    init(props,tit);
  }

  public propsDialog(Properties props) {
    init(props,"");
  }

  public propsDialog(Frame owner,Properties props) {
    super(owner,"",true);
    init(props,"");
  }

  public propsDialog(Frame own,String file, String tit) {
    super(own,tit,true);
    initWithFile(file,tit);
  }
  public propsDialog(Dialog own,String file, String tit) {
    super(own,tit,true);
    initWithFile(file,tit);
  }

  private void initWithFile(String file,String tit) {
    Properties cprop;
    if (file != null && file.equals("System.properties")) file = null;
    if (file == null) {
      cprop = System.getProperties();
      sysprop = true;
      tit = "System.properties";
    } else {
      cprop = FileHandler.getProperties(file);
    }
    if (tit.equals("")) tit = file;
    propfile = file;
    init(cprop,tit);
  }

  public propsDialog(String file,String tit) {
    this((Frame)null,file,tit);
  }

  public propsDialog(String file) {
    this(file,"");
  }

  private void init(Properties props,String tit) {
    setTitle(tit);
    if (props == null) {
      props = System.getProperties();
    }
    fillData(props);
    jp.setNavBar(navbar);
    navbar.setJpTabView(jp);
    
    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(jp);
    this.getContentPane().add(okpanel, BorderLayout.SOUTH);
    jp.setPreferredSize(new Dimension(100,100));
    if (!sysprop) {
      jp.add(navbar, BorderLayout.NORTH);
      jp.getNavBar().addOption(rnvAdd);
      jp.getNavBar().addOption(rnvEdit);
      jp.getNavBar().addOption(rnvDelete);
      jp.getNavBar().addOption(rnvDelAll);
    }
    props4list = props;
    startFrame.getStartFrame().centerFrame(propedit,0,"Izmjena");
  }
  private void fillData(Properties props) {
    data = new StorageDataSet();
    data.setColumns(new Column[] {
        dM.createStringColumn("PKEY", "Key", 50),
        dM.createStringColumn("PVAL", "Value", 50)
    });
    data.getColumn("PKEY").setPrecision(-1);
    data.getColumn("PVAL").setPrecision(-1);
    data.open();
    for (Iterator i = props.keySet().iterator(); i.hasNext(); ) {
      String key = (String) i.next();
      data.insertRow(false);
      data.setString("PKEY", key);
      data.setString("PVAL", props.getProperty(key));
    }
    data.post();
    jp.setDataSet(data);
  }
  public void refreshList() {
    fillData(props4list);
  }
  public void reload() {
    if (sysprop) return;
    props4list = FileHandler.getProperties(propfile);
    refreshList();
  }

  public propEdit getDPropEdit() {
    return propedit;
  }

  public void props_add() {
    propedit.setKey("");
    propedit.show();
    //refreshList();
    //data.last();
  }
  public void props_upd() {
    propedit.setKey(data.getString("PKEY"));
    propedit.show();
    data.setString("PVAL", props4list.getProperty(data.getString("PKEY")));
    jp.fireTableDataChanged();
  }
  public void props_del() {
    if (data.rowCount() == 0) return;
    props4list.remove(data.getString("PKEY"));
    data.emptyRow();
    jp.fireTableDataChanged();
    doSave = true;
  }
  public void props_delall() {
    props4list.clear();
    refreshList();
    doSave = true;
  }
  
  public void action_jPrekid() {
    setVisible(false);
  }
/**
 * Poziva se pritiskom na OK button, obavezno overridati
 */

  public OKpanel getOKpanel() {
    return okpanel;
  }


  public void action_jBOK() {
    if (propfile !=null) FileHandler.storeProperties(propfile,props4list);
  }
  
  public String getListSelectedKey() {
    return data.getString("PKEY");
  }

  public raNavBar getNavBar() {
    if (sysprop) return null;
    return jp.getNavBar();
  };

   class propertyMember {
    public propertyMember(String k,String v) {
      key = k;
      value = v;
    }
    String key;
    String value;
  }
  public boolean doSaving() {
    return doSave;
  }

  class propEdit extends JraDialog {
    OKpanel okp = new OKpanel() {
      public void jBOK_actionPerformed(){
         jBOK_action();
      }

      public void jPrekid_actionPerformed(){
        jPrekid_action();
      }
    };
    String propkey = "";
    JPanel jp = new JPanel();
    XYLayout xYLay = new XYLayout();
    JLabel jlkey = new JLabel();
    JLabel jlvalue = new JLabel();
    public JTextField jtkey = new JTextField();
    public JTextField jtvalue = new JTextField();

    public propEdit(Dialog owner) {
      super(owner,true);
      try {
        jbInit();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }

    private void jbInit() throws Exception {
      jlkey.setText("Key");
      jp.setLayout(xYLay);
      jlvalue.setText("Value");
      xYLay.setWidth(600);
      xYLay.setHeight(85);
      jp.add(jlkey, new XYConstraints(15, 20, -1, -1));
      jp.add(jlvalue, new XYConstraints(15, 45, -1, -1));
      jp.add(jtkey, new XYConstraints(80, 20, 300, -1));
      jp.add(jtvalue, new XYConstraints(80, 45, 500, -1));
      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(jp,BorderLayout.CENTER);
      getContentPane().add(okp,BorderLayout.SOUTH);
      addComponentListener(new java.awt.event.ComponentAdapter() {
        public void componentShown(java.awt.event.ComponentEvent e) {
          focusInit();
        }
      });
      jtkey.addKeyListener(new hr.restart.swing.JraKeyListener());
      jtvalue.addKeyListener(new hr.restart.swing.JraKeyListener());
      okp.registerOKPanelKeys(this);
    }

    void jBOK_action() {
      if (!Validacija()) return;
      props4list.setProperty(jtkey.getText(),jtvalue.getText());
      props4list.getProperty(propkey);
      if (isNew()) {
        data.insertRow(false);
        data.setString("PKEY", jtkey.getText());
        data.setString("PVAL", jtvalue.getText());
        data.post();
        propsDialog.this.jp.fireTableDataChanged();
      }
      doSave = true;
      hide();
    }

    boolean Validacija() {
      if (jtkey.getText().equals("")) {
        hr.restart.util.Valid.getValid().showValidErrMsg(null,'E');
        jtkey.requestFocus();
        return false;
      }
      return true;
    }

    void jPrekid_action() {
      hide();
    }

    void focusInit() {
      if (isNew()) {
        setTitle("New property");
        jtkey.setEnabled(true);
        jtkey.requestFocus();
        jtvalue.setText("");
      } else {
        setTitle("Set property");
        jtkey.setEnabled(false);
        jtvalue.setText(props4list.getProperty(propkey));
        jtvalue.requestFocus();
      }
      jtvalue.setEditable(!sysprop);
      jtkey.setText(propkey);
    }

    private boolean isNew() {
      return propkey.equals("");
    }

    public void setKey(String newPropkey) {
      if (newPropkey == null) newPropkey = "";
      propkey = newPropkey;
    }
  }
}
