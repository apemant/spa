/****license*****************************************************************
**   file: lookupFrame.java
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

import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raSelectTableModifier;
import hr.restart.util.columnsbean.ColumnsBean;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.DataSetException;

/**
 * Title:        LookupFrame
 * Description:  Po ulaznim parametrima prikazuje podatke za dohvat
 * Copyright:    Copyright (c) 2001
 * Company:      RA
 * @author AI
 * @version 1.0
 */

public class lookupFrame extends JraDialog {
//  private com.borland.dx.dataset.Column columnToSearch;
//  private String dataToSearch;
//  private int[] visibleTabCols;
//  private com.borland.dx.dataset.DataSet raDataSet;
/*
  private com.borland.dx.dataset.NavigationListener lookupNavListener = new com.borland.dx.dataset.NavigationListener() {
      public void navigated(NavigationEvent e) {
        set_navigated(e);
      }
  };
*/
//static sysoutTEST ST=new sysoutTEST(false);
//static TimeTrack TT = new TimeTrack(false);
  private String[] retValuesUI;
  private String lookupSaveName = "";
  static lookupFrame luframe;
  JPanel jPTable = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JraScrollPane jScrollPane1 = new JraScrollPane();
  JraTable2 jdbTable1 = new JraTable2() {
    public void tableDoubleClicked() {
      oKpanel_jBOK();
    }
    public void killFocus(java.util.EventObject e) {
      columnsBean1.focusCombo();
    }
    public void setTableColumnsUI() {
      super.setTableColumnsUI();
      if (columnsBean1 != null) columnsBean1.updateColumnWidths();
    }
  };
  JPanel jPcbean = new JPanel();
  ColumnsBean columnsBean1 = new ColumnsBean();
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  OKpanel oKpanel1 = new OKpanel() {
    public void jPrekid_actionPerformed() {
      oKpanel_jPrekid();
    }
    public void jBOK_actionPerformed() {
      oKpanel_jBOK();
    }
  };
  KeyListener adap;

//  com.borland.dx.dataset.DataSet raDataSet;
/**
 * Konstruktor
 */
  public lookupFrame(Frame frame, com.borland.dx.dataset.DataSet raDataSet,int[] visibleCols) {
    super(frame,"Pregled",true);
    frameInit(raDataSet,visibleCols);
    luframe = this;
  }
  public lookupFrame(Dialog frame, com.borland.dx.dataset.DataSet raDataSet,int[] visibleCols) {
    super(frame,"Pregled",true);
    frameInit(raDataSet,visibleCols);
    luframe = this;
  }
  private void frameInit(com.borland.dx.dataset.DataSet raDataSet,int[] visibleCols) {

    try {
      jdbTable1.setDataSet(raDataSet);
      columnsBean1.setAditionalCols(visibleCols);
      jbInit();
    } catch (Exception e) {System.out.println(e);}

  }
  /*private void removeOKPButtonListeners() {
    removeButtonListener(oKpanel1.jBOK);
    removeButtonListener(oKpanel1.jPrekid);
    /*
    java.util.EventListener[] evs = oKpanel1.jBOK.getListeners(java.awt.event.KeyListener.class);
    oKpanel1.jBOK.removeKeyListener((hr.restart.swing.JraKeyListener)evs[0]);
    evs = oKpanel1.jPrekid.getListeners(java.awt.event.KeyListener.class);
    oKpanel1.jPrekid.removeKeyListener((hr.restart.swing.JraKeyListener)evs[0]);
    */
  /*}
  private void removeButtonListener(hr.restart.swing.JraButton b) {
    java.util.EventListener[] evs = b.getListeners(java.awt.event.KeyListener.class);
    for (int i=0;i<evs.length;i++) b.removeKeyListener((java.awt.event.KeyListener)evs[i]);
  }*/

  public static lookupFrame getLookupFrame(Frame frame, com.borland.dx.dataset.DataSet raDataSet,int[] visibleCols) {
    if (luframe==null || !luframe.getOwner().equals(frame)) {
      if (luframe != null) luframe.destroy();
      luframe=new lookupFrame(frame,raDataSet,visibleCols);
    } else {
      dataInit(raDataSet,visibleCols);
    }
    return luframe;
  }
  public static lookupFrame getLookupFrame(Dialog frame, com.borland.dx.dataset.DataSet raDataSet,int[] visibleCols) {
    if (luframe==null || !luframe.getOwner().equals(frame)) {
      if (luframe != null) luframe.destroy();
      luframe=new lookupFrame(frame,raDataSet,visibleCols);
    } else {
      dataInit(raDataSet,visibleCols);
    }
    return luframe;
  }
  private static void dataInit(com.borland.dx.dataset.DataSet raDataSet,int[] visibleCols) {
//ST.prn("reusing luframe....");
//    if (luframe.jdbTable1.getDataSet().getTableName().equals(raDataSet.getTableName())) return;
    luframe.jdbTable1.setDataSet(raDataSet);
    luframe.columnsBean1.setRaJdbTable(luframe.jdbTable1);
    luframe.columnsBean1.setAditionalCols(visibleCols);
    //luframe.columnsBean1.initialize();
  }
  public void setNavListener(com.borland.dx.dataset.DataSet setToHandle) {
//    setToHandle.addNavigationListener(lookupNavListener);
  }
/*
  private void set_navigated(NavigationEvent e) {

  }
*/
  private void jbInit() throws Exception {

    jPTable.setLayout(borderLayout1);
//jdb    jdbTable1.setRowHeaderVisible(false);
//jdb    jdbTable1.setEditable(false);
//    jdbTable1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    jdbTable1.setCellSelectionEnabled(false);
    this.getContentPane().setLayout(borderLayout3);
    jPcbean.setLayout(borderLayout2);
    columnsBean1.setRaJdbTable(jdbTable1);
//    this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent e) {
        this_compShown();
      }
    });
//    oKpanel1.setMaximumSize(new Dimension(32767, 27));
//    oKpanel1.setPreferredSize(new Dimension(138, 25));

    this.getContentPane().add(jPTable, BorderLayout.CENTER); //UVIJEK TREBA KREIRATI PRIJE JdbTable od ColumnsBeana
    this.getContentPane().add(jPcbean, BorderLayout.NORTH);
    jPcbean.add(columnsBean1, BorderLayout.NORTH);
    this.getContentPane().add(oKpanel1, BorderLayout.SOUTH);
    jScrollPane1.setHorizontalScrollBarPolicy(JraScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jScrollPane1.setVerticalScrollBarPolicy(JraScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    jPTable.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(jdbTable1, null);
    setTitle("Pregled");

    adap = new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        jdbTable1.processTableKeyEvent(e);
        if (lookupData.getlookupData().multi != null)
          processSelectKey(e);
      }
      public void keyTyped(KeyEvent e) {
        jdbTable1.processTableKeyTyped(e);
      }
    };
    /*this.addKeyListener(adap);
    jdbTable1.addKeyListener(adap);
    columnsBean1.registerComboKeyListener(adap);
    removeOKPButtonListeners();
    oKpanel1.setEnterEnabled(true);
    oKpanel1.registerOKPanelKeys(this);
    oKpanel1.registerOKPanelKeys(jdbTable1);
//    oKpanel1.registerOKPanelKeys(oKpanel1.jBOK);
//    oKpanel1.registerOKPanelKeys(oKpanel1.jPrekid);
    columnsBean1.registerColumnsBeanKeys(this);*/
    AWTKeyboard.registerKeyListener(this, adap);
    oKpanel1.setEnterEnabled(true);
    oKpanel1.registerOKPanelKeys(this);

    setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        oKpanel_jPrekid();
      }
    });
  }
  /**
   * zove ShowCenter(true, 0, 0);
   */
  public void ShowCenter() {
    ShowCenter(true, 0, 0);
  }

  public void setSaveName(String saveName) {
    lookupSaveName = saveName == null ? "" : "-"+saveName;
  }
  
  public void ShowCenter(boolean isCentered, int inWidth, int inHeigth) {
    ShowCenter(isCentered, true, inWidth, inHeigth);
  }
  /**
   * Metoda koja prikazuje frame na centru ekrana;
   * Ako je isCentered = true - centrira
   * Ako nije onda sa inWidth i inHeigth
  */
  public void ShowCenter(boolean isCentered, boolean first, int inWidth, int inHeigth) {
    this.columnsBean1.setSaveName("Pregled" + lookupSaveName);
    this.columnsBean1.initialize();
    //this.columnsBean1.rnvRefresh.actionPerformed(null);
    if (first) jdbTable1.getDataSet().first();
    try {
    	Column col = jdbTable1.getDataSet().getColumn(lookupData.getlookupData().getCOLNAMES()[0]);
    	if (col.getVisible() != 0) columnsBean1.setComboSelectedItem(col.getCaption());
    	else columnsBean1.setFirstItem();
    } catch (Exception e) {
      System.err.println("non-fatal");
      e.printStackTrace();
    }
    //jdbTable1.updateTableColumns();
    this.pack();
    Point cbpl = columnsBean1.getPreferredLocationOnScreen();
    if (cbpl != null) setLocation(cbpl);
    else if (isCentered) {
      // center frame on screen (AI)
      Dimension screenSize = hr.restart.start.getSCREENSIZE();
      Dimension winSize = this.getSize();
      if (winSize.height > screenSize.height)
        winSize.height = screenSize.height;
      if (winSize.width > screenSize.width)
        winSize.width = screenSize.width;
      this.setLocation((screenSize.width - winSize.width) / 2, (screenSize.height - winSize.height) / 2);
    }
    else {
      this.setLocation(inWidth,inHeigth);
    }
    this.setVisible(true);
  }
  void oKpanel_jPrekid() {
      retValuesUI=null;
      this.hide();
  }

  void oKpanel_jBOK() {
    get_submit();
    this.hide();
    /*if (oKpanel1.getLastKeyEvent()!=null) if (oKpanel1.getLastKeyEvent().getKeyCode() == KeyEvent.VK_ENTER)
      hr.restart.swing.JraKeyListener.enterNow = false;*/
  }
  public String[] getRetValuesUI(){
    return retValuesUI;
  }
  
  public JraTable2 getTableView() { 
    return jdbTable1;
  }
/**
 * vraca odabranu vrijednost i current record u zadanom datasetu
 */
  void get_submit() {
    try {
      hr.restart.util.lookupData LD;
      LD=hr.restart.util.lookupData.getlookupData();
      String[] colNamesUI = LD.getCOLNAMES();
      retValuesUI=(String[])colNamesUI.clone();
      for (int i=0;i<colNamesUI.length;i++) {
        retValuesUI[i]=LD.getCurrRowColValue(jdbTable1.getDataSet(),colNamesUI[i],jdbTable1.getDataSet().getRow());
      }
    } catch (Exception e) {
    // koristi lookupFrame bez lookupData
      retValuesUI = new String[] {""};
    }
  }
  
  void processSelectKey(KeyEvent e) {
    raSelectTableModifier selMod = lookupData.getlookupData().multi;
    DataSet ds = jdbTable1.getDataSet();
    if (selMod != null && !e.isConsumed() && ds.rowCount() > 0) {
      if (((e.getKeyCode() == e.VK_ENTER && !raSelectTableModifier.space) 
          || e.getKeyCode() == e.VK_SPACE)) {
        e.consume();
        /*JraKeyListener.enterNow = false;*/
        if (e.getKeyCode() == e.VK_ENTER)
          AWTKeyboard.ignoreKeyRelease(AWTKeyboard.ENTER);
        selMod.toggleSelection(jdbTable1.getDataSet());
        if (!ds.next()) jdbTable1.fireTableDataChanged();
      } else if (e.getKeyCode() == e.VK_A && e.isControlDown()) {
        e.consume();
        int row = ds.getRow();
        jdbTable1.stopFire();
        ds.enableDataSetEvents(false);
        for (ds.first(); ds.inBounds(); ds.next())
          selMod.toggleSelection(ds);
        ds.goToRow(row);
        ds.enableDataSetEvents(true);
        jdbTable1.startFire();
      }
    }
  }
/*
  void this_keyPressed(KeyEvent e) {
//    if (e.getKeyCode()==e.VK_ENTER) oKpanel_jBOK();
//    if (e.getKeyCode()==e.VK_ESCAPE) oKpanel_jPrekid();
    jdbTable1.processTableKeyEvent(e);
  }
*/
  void destroy() {
    /*removeKeyListener(adap);
    columnsBean1.unregisterComboKeyListener(adap);
    oKpanel1.unregisterOKPanelKeys(this);
    columnsBean1.unregisterColumnsBeanKeys(this);*/
    AWTKeyboard.unregisterComponent(this);    
    dispose();
  }
  
  void this_compShown() {
    //oKpanel1.jBOK.requestFocus();
  }
}//EOC
/* IZ HELPA
void queryDataSet1_filterRow(ReadRow row, RowFilterResponse
 response) {
   try {
     if (formatter == null || columnName == null ||
   columnValue == null || columnName.length() == 0 ||
   columnValue.length() == 0)
   // user set field(s) are blank, so add all rows
   response.add();
     else {
   row.getVariant(columnName, v);
   // fetches row's value of column
   // formats this to a string
   String s = formatter.format(v);
   // true means show this row
   if (columnValue.equals(s))
    response.add();
   else response.ignore();
  }
 }
 catch (Exception e) {
 System.err.println("Filter example failed");
   }
}
*/