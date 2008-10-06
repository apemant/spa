/****license*****************************************************************
**   file: TabelaSume.java
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

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.dataset.Variant;

/**
 * mali ekrancic koji se automatski pojavi i prikazuje kumulative
 * on bi htio da bude na gumb pa se prikaze
 */

public class TabelaSume extends JraDialog {
  JPanel jp = new JPanel();
//  OKpanel okp = new OKpanel() {
//    {
//      jBOK.setVisible(false);
//    }
//    public void jBOK_actionPerformed() {
//      prekid();
//    }
//    public void jPrekid_actionPerformed() {
//      prekid();
//    }
//  };
  JTabbedPane jTab = new JTabbedPane();
  raNavBar navbar = new raNavBar(raNavBar.DBNAVIGATE) {
    public void add_action() {
      rnvAdd_action();
    }
    public void delete_action() {
      rnvDelete_action();
    }
    public void print_action() {
      rnvPrint_action();
    }
    public void exit_action() {
      rnvExit_action();
    }
  };

//  JraScrollPane jsp = new JraScrollPane();
//  JraTable2 jTable1 = new JraTable2();//(podaci,new String []  {"Naziv","Total"});
  raJPTableView jptv = new raJPTableView(false) {
    public void mpTable_killFocus(java.util.EventObject ev) {
      navbar.rnvAdd.requestFocus();
    }
  };
  raJPTableView callerJPTV = null;
  hr.restart.util.reports.JTablePrintRun printer = new hr.restart.util.reports.JTablePrintRun();
  TableDataSet TSSet = new TableDataSet();
  Column TSDesc = new Column("TXT","Naziv",Variant.STRING);
  Column TSTotal = new Column("TOTAL","Total",Variant.BIGDECIMAL);
  StorageDataSet columnsSet = new StorageDataSet();
  Column colName = new Column("COLUMN","Naziv kolone",Variant.STRING);
  Column colCaption = new Column("CAPTION","Kolona",Variant.STRING);

  public TabelaSume(java.awt.Frame frm) {
    super(frm,false);
    setTitle(frm.getTitle());
    InitTas();
  }

  public TabelaSume(java.awt.Dialog dlg) {
    super(dlg,false);
    setTitle(dlg.getTitle());
    InitTas();
  }
  private void InitTas() {

//    jp.setLayout(new BorderLayout());
//    jp.setPreferredSize(new Dimension(220,190));
//    jp.add(jsp,BorderLayout.CENTER);

//    jsp.getViewport().setLayout(bl3);
//    jsp.getViewport().add(jTable1);
    TSTotal.setDisplayMask("###,###,##0.00");
    TSTotal.setWidth(14);
    TSSet.setColumns(new Column[] {TSDesc,TSTotal});
    columnsSet.setColumns(new Column[] {colName, colCaption});
//    jTable1.setDataSet(TSSet);
//    okp.registerOKPanelKeys(this);
    navbar.getNavContainer().remove(navbar.rnvToggleTable);
    navbar.getNavContainer().remove(navbar.rnvUpdate);
    jp.setLayout(new BorderLayout());
    getContentPane().setLayout(new BorderLayout());
    jptv.setDataSet(TSSet);
    jp.add(jptv,BorderLayout.CENTER);
    jp.add(navbar,BorderLayout.NORTH);
    jTab.addTab("Kumulativi",jp);
    getContentPane().add(jTab);
    navbar.registerNavBarKeys(this);
    jptv.initKeyListener(this);
    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentHidden(java.awt.event.ComponentEvent e) {
        if (callerJPTV != null) callerJPTV.makeKumul();
      }
    });
  }
  public void prekid() {
    hide();
  }
  public void transfer(Object[] data ) {
/*
     for (int i=0; i<data.length;i++)
          podaci[i][0]=(String) data[i];
*/
    TSSet.open();
    TSSet.emptyAllRows();
    if (data == null) return;
    for (int i=0;i<data.length;i++) {
      TSSet.insertRow(false);
      TSSet.setString("TXT",(String)data[i]);
    }
    jptv.fireTableDataChanged();
  }

  public void go(int x,int y) {
    pack();
    setLocation(x,y-getSize().height);
    show();
  }
  public void go(raJPTableView _jptv) {
    callerJPTV = _jptv;
    go((java.awt.Component)callerJPTV);
  }
  public void go(java.awt.Component comp) {
    pack();
    int vX = comp.getLocationOnScreen().x - this.getSize().width + comp.getSize().width;
    int vY = comp.getLocationOnScreen().y - this.getSize().height +comp.getSize().height;
    setLocation(vX,vY);
    show();
  }
  /**
   * @param value
   * @param poz
   */
  public void napuni(Variant value ,int poz) {
    TSSet.open();
    TSSet.goToRow(poz);
    TSSet.setBigDecimal("TOTAL",value.getAsBigDecimal());
    TSSet.post();
  }
  private void makeColumnsSet() {
    columnsSet.empty();
    if (callerJPTV == null) return;
    columnsSet.open();
    Column[] cols = callerJPTV.getStorageDataSet().getColumns();
    for (int i = 0; i < cols.length; i++) {
      String name = cols[i].getColumnName();
      String caption = cols[i].getCaption();
      if (cols[i].getVisible() != com.borland.jb.util.TriStateProperty.FALSE && checkAddColumnsSet(name)) {
        columnsSet.insertRow(false);
        columnsSet.setString("COLUMN",name);
        columnsSet.setString("CAPTION",caption);
        columnsSet.post();
      }
    }
  }

  private boolean checkAddColumnsSet(String cname) {
    if (callerJPTV.getStoZbrojiti() == null) {
      return true;
    } else {
      return !Util.getUtil().containsArr(callerJPTV.getStoZbrojiti(),cname);
    }
  }

  private void toggleKumul(String colName) {
    callerJPTV.toggleKumul(colName);
    transfer(callerJPTV.getNaslovi());
    callerJPTV.Zbrajalo();
    jptv.fireTableDataChanged();
  }

  private void rnvAdd_action() {
    if (callerJPTV == null) return;
    makeColumnsSet();
    String[] getCols = lookupData.getlookupData().lookUp(this,columnsSet,new int[] {1});
    if (getCols != null && getCols[0] != "") toggleKumul(getCols[0]);
  }

  private void rnvDelete_action() {
    if (callerJPTV == null) return;
    toggleKumul(callerJPTV.getStoZbrojiti()[jptv.getMpTable().getSelectedRow()]);
  }

  private void rnvPrint_action() {
    navbar.getColBean().setRaJdbTable(jptv.getMpTable());
    printer.setColB(navbar.getColBean());
    printer.setRTitle(this.getTitle().concat("\nKumulativi"));
    printer.runIt();
  }

  private void rnvExit_action() {
    prekid();
  }

}
