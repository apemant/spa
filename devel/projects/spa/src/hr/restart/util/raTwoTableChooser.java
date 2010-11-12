/****license*****************************************************************
**   file: raTwoTableChooser.java
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

import hr.restart.sisfun.frmParam;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.borland.dx.dataset.StorageDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raTwoTableChooser extends JPanel {
  sysoutTEST ST = new sysoutTEST(false);
  private com.borland.dx.dataset.StorageDataSet leftDataSet;
  private com.borland.dx.dataset.StorageDataSet rightDataSet;
  private boolean tableUI = true;
  private boolean horizontal = true;
  private raJPTableView leftJPTW = new raJPTableView() {
    public void mpTable_killFocus(java.util.EventObject e) {
      super.mpTable_killFocus(e);
      initLeftKeys();
    }
    public void mpTable_doubleClicked() {
      actionLtoR();
    }
  };
  private raJPTableView rightJPTW = new raJPTableView(){
    public void mpTable_killFocus(java.util.EventObject e) {
      super.mpTable_killFocus(e);
      initRightKeys();
    }
    public void mpTable_doubleClicked() {
      actionRtoL();
    }
  };
  public raNavAction rnvLtoR = new raNavAction("Prebaci",raImages.IMGFORWARD,KeyEvent.VK_UNDEFINED) {
    public void actionPerformed(ActionEvent e) {
      actionLtoR();
    }
  };
  public raNavAction rnvRtoL = new raNavAction("Prebaci",raImages.IMGBACK,KeyEvent.VK_UNDEFINED) {
    public void actionPerformed(ActionEvent e) {
      actionRtoL();
    }
  };
  public raNavAction rnvLtoR_all = new raNavAction("Prebaci sve",raImages.IMGALLFORWARD,KeyEvent.VK_UNDEFINED) {
    public void actionPerformed(ActionEvent e) {
      actionLtoR_all();
    }
  };
  public raNavAction rnvRtoL_all = new raNavAction("Prebaci sve",raImages.IMGALLBACK,KeyEvent.VK_UNDEFINED) {
    public void actionPerformed(ActionEvent e) {
      actionRtoL_all();
    }
  };

  public raNavAction rnvSave = new raNavAction("Snimi",raImages.IMGSAVE,KeyEvent.VK_F10) {
    public void actionPerformed(ActionEvent e) {
      saveChoose();
    }
  };

  private JPanel jpControls = new JPanel();
  private JPanel jpMiddle = new JPanel();
  private java.awt.Component keyParent;

  public raTwoTableChooser() {
    try {
      horizontal = !frmParam.getParam("zapod", "vert2Chooser", "N", 
          "Vertikalna varijanta izbornika s dvije tablice (D/N)", true).equalsIgnoreCase("D");
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public raTwoTableChooser(Component cKeyParent) {
    this();
    setKeyParent(cKeyParent);
  }

  public raTwoTableChooser(raFrame cKeyParent) {
    this();
    setKeyParent(cKeyParent);
  }
  
  public raTwoTableChooser(boolean vertical) {
    try {
      horizontal = !vertical;
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public raTwoTableChooser(Component cKeyParent, boolean vertical) {
    this(vertical);
    setKeyParent(cKeyParent);
  }

  public raTwoTableChooser(raFrame cKeyParent, boolean vertical) {
    this(vertical);
    setKeyParent(cKeyParent);
  }

  void jbInit() throws Exception {
    
    if (horizontal) {
      
  //    setLayout(new BorderLayout());
      GridBagLayout gb = new GridBagLayout();
      setLayout(gb);
      jpControls.setLayout(null);
      rnvLtoR.setBounds(0,0,rnvLtoR.getPreferredSize().width,rnvLtoR.getPreferredSize().height);
      rnvRtoL.setBounds(0,rnvLtoR.getPreferredSize().height+1,rnvRtoL.getPreferredSize().width,rnvRtoL.getPreferredSize().height);
      rnvLtoR_all.setBounds(0,(rnvLtoR.getPreferredSize().height+1)*2,rnvLtoR_all.getPreferredSize().width,rnvLtoR_all.getPreferredSize().height);
      rnvRtoL_all.setBounds(0,(rnvLtoR.getPreferredSize().height+1)*3,rnvRtoL_all.getPreferredSize().width,rnvRtoL_all.getPreferredSize().height);
      rnvSave.setBounds(0,(rnvLtoR.getPreferredSize().height+1)*4,rnvRtoL_all.getPreferredSize().width,rnvRtoL_all.getPreferredSize().height);
      jpControls.add(rnvLtoR,null);
      jpControls.add(rnvRtoL,null);
      jpControls.add(rnvLtoR_all,null);
      jpControls.add(rnvRtoL_all,null);
      jpControls.add(rnvSave,null);
      jpControls.setPreferredSize(new Dimension(rnvLtoR.getPreferredSize().width,rnvLtoR.getPreferredSize().height*5+4));
      jpControls.setMinimumSize(new Dimension(rnvLtoR.getPreferredSize().width,rnvLtoR.getPreferredSize().height*5+4));
      jpControls.setMaximumSize(jpControls.getPreferredSize());
      jpMiddle.setLayout(new GridBagLayout());
      jpMiddle.add(jpControls);
      leftJPTW.setBorder(BorderFactory.createEtchedBorder());
      rightJPTW.setBorder(BorderFactory.createEtchedBorder());
  //    leftJPTW.setFewRowSize();
  //    rightJPTW.setFewRowSize();
      /*leftJPTW.setPreferredSize(new Dimension(400,200));
      rightJPTW.setPreferredSize(new Dimension(400,200));*/
  /*
      add(leftJPTW,BorderLayout.WEST);
      add(rightJPTW,BorderLayout.EAST);
      add(jpMiddle,BorderLayout.CENTER);
  */
      GridBagConstraints gc = new GridBagConstraints();
  
      gc.fill = gc.BOTH;
  //    gc.gridwidth = 1;
      gc.weighty = 1.0;
      gc.weightx = 1.0;
      gb.setConstraints(leftJPTW,gc);
      add(leftJPTW);
  
      gc.fill = gc.NONE;
  //    gc.gridwidth = 1;
      gc.weighty = 0.0;
      gc.weightx = 0.0;
      gb.setConstraints(jpMiddle,gc);
      add(jpMiddle);
  
      gc.fill = gc.BOTH;
  //    gc.gridwidth = 1;
      gc.weighty = 1.0;
      gc.weightx = 1.0;
      gb.setConstraints(rightJPTW,gc);
      add(rightJPTW);
    } else {
      setLayout(new BorderLayout());
      jpControls.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));    
      jpControls.add(rnvLtoR);
      jpControls.add(rnvRtoL);
      jpControls.add(rnvLtoR_all);
      jpControls.add(rnvRtoL_all);
      jpControls.add(Box.createHorizontalStrut(5));
      jpControls.add(rnvSave);
      
      rnvLtoR.setIcon(raImages.getImageIcon(raImages.IMGDOWN));
      rnvRtoL.setIcon(raImages.getImageIcon(raImages.IMGUP));
      rnvLtoR_all.setIcon(raImages.getImageIcon(raImages.IMGALLDOWN));
      rnvRtoL_all.setIcon(raImages.getImageIcon(raImages.IMGALLUP));
      
      /*leftJPTW.setBorder(BorderFactory.createEtchedBorder());
      rightJPTW.setBorder(BorderFactory.createEtchedBorder());*/
      /*leftJPTW.setPreferredSize(new Dimension(640,240));
      rightJPTW.setPreferredSize(new Dimension(640,160));*/
      JPanel left = new JPanel(new BorderLayout());
      JPanel right = new JPanel(new BorderLayout());
      left.add(leftJPTW);
      right.add(rightJPTW);
      
      JSplitPane up = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, left, jpControls);
      up.setResizeWeight(1);
      up.setDividerSize(4);
      JSplitPane main = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, up, right);
      main.setResizeWeight(0.5);
      main.setDividerSize(4);
      add(main);
    }
  }
  
  public void setLeftPrefferedSize(Dimension d) {
    leftJPTW.setPreferredSize(d);    
  }
  
  public void setRightPrefferedSize(Dimension d) {    
    rightJPTW.setPreferredSize(d);
  }

  public void initialize() {
    leftJPTW.setDataSet(leftDataSet);
    rightJPTW.setDataSet(rightDataSet);
    leftJPTW.getColumnsBean().rnvRefresh.setEnabled(false);
    leftJPTW.getColumnsBean().setSaveName(getClass().getName()+"-left");
    leftJPTW.getColumnsBean().initialize();
    rightJPTW.getColumnsBean().rnvRefresh.setEnabled(false);
    rightJPTW.getColumnsBean().setSaveName(getClass().getName()+"-left");
    rightJPTW.getColumnsBean().initialize();
    rightJPTW.getColumnsBean().setSaveName(getClass().getName()+"-right");
    if (keyParent == null) keyParent = getTopLevelAncestor();
    initDefaultKeys();
  }
  public void initDefaultKeys() {
    initLeftKeys();
    rnvLtoR.registerNavKey(keyParent);
    rnvRtoL.registerNavKey(keyParent);
    rnvLtoR_all.registerNavKey(keyParent);
    rnvRtoL_all.registerNavKey(keyParent);
  }
  public void setLeftDataSet(com.borland.dx.dataset.StorageDataSet leftDS) {
    leftDataSet = leftDS;
  }
  public com.borland.dx.dataset.StorageDataSet getLeftDataSet() {
    return leftDataSet;
  }
  public void setRightDataSet(com.borland.dx.dataset.StorageDataSet rightDS) {
    rightDataSet = rightDS;
  }
  public com.borland.dx.dataset.StorageDataSet getRightDataSet() {
    return rightDataSet;
  }

  public void fireTableDataChanged() {
    if (!tableUI) return;
    rightJPTW.fireTableDataChanged();
    leftJPTW.fireTableDataChanged();
  }

  private void moveRow(StorageDataSet fromDSet,StorageDataSet toDSet) {
    if (fromDSet.rowCount() == 0) return;
    toDSet.last();
    toDSet.insertRow(false);
    fromDSet.copyTo(toDSet);
    fromDSet.deleteRow();
    toDSet.post();
    fireTableDataChanged();
    fromDSet.goToClosestRow(fromDSet.getRow());
    toDSet.goToClosestRow(toDSet.getRow());
  }

  private void moveAllRows(StorageDataSet fromDSet,StorageDataSet toDSet) {
      tableUI = false;
      int rowCnt = fromDSet.getRowCount();
      for (int i=0;i<rowCnt;i++) {
        fromDSet.first();
        moveRow(fromDSet,toDSet);
      }
      tableUI = true;
      fireTableDataChanged();
      toDSet.goToClosestRow(toDSet.getRow());
  }

  public void actionLtoR() {
    if (rnvLtoR.isEnabled()) moveRow(leftDataSet,rightDataSet);
  }
  public void actionRtoL() {
    if (rnvRtoL.isEnabled()) moveRow(rightDataSet,leftDataSet);
  }
  public void actionLtoR_all() {
    if (rnvLtoR_all.isEnabled()) moveAllRows(leftDataSet,rightDataSet);
  }
  public void actionRtoL_all() {
    if (rnvLtoR_all.isEnabled()) moveAllRows(rightDataSet,leftDataSet);
  }

  public void initLeftKeys() {
    rightJPTW.rmKeyListener(keyParent);
    leftJPTW.initKeyListener(keyParent);
  }

  public void initRightKeys() {
    leftJPTW.rmKeyListener(keyParent);
    rightJPTW.initKeyListener(keyParent);
  }

  public void setKeyParent(java.awt.Component sKeyParent) {
    keyParent = sKeyParent;
  }

  public void setKeyParent(raFrame sKeyParent) {
    keyParent = sKeyParent.getSource();
  }

  public java.awt.Component getKeyParent() {
    return keyParent;
  }

  public void saveChoose() {
  }
  public raJPTableView getLeftJPTW() {
    return leftJPTW;
  }
  public raJPTableView getRightJPTW() {
    return rightJPTW;
  }
}
