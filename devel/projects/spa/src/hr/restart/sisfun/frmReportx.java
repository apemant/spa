/****license*****************************************************************
**   file: frmReportx.java
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
package hr.restart.sisfun;

import hr.restart.baza.Artnap;
import hr.restart.baza.dM;
import hr.restart.sisfun.Asql;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmReportx extends raMasterDetail {

  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();

  JPanel jpMasterMain;
  JPanel jpDetailMain = new JPanel();
  JPanel jpDetail;
  
  JraTextField jraCREP = new JraTextField();
  JraTextField jraNAZREP = new JraTextField();
  
  JraButton jbSelApp = new JraButton();
  JlrNavField jlrOpis = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrApp = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  
  public frmReportx() {
    try {
      this.setMasterDeleteMode(DELDETAIL);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  
  public void EntryPointMaster(char mode) {
    if (mode == 'N') {
      rcc.EnabDisabAll(jpMasterMain, true);
    }
    if (mode == 'I') {
      rcc.EnabDisabAll(jpMasterMain, false);
    }
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      rcc.EnabDisabAll(jpMasterMain, true);
    }
  }
  
  public boolean ValidacijaMaster(char mode) {
    if (mode == 'N' && MasterNotUnique()) {
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Normativ ve\u0107 postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  
/*  public boolean DeleteCheckMaster() {
    deleteSQL = "";
    this.refilterDetailSet();
    if (this.getDetailSet().rowCount() > 0 && !canDeleteMaster()) {
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Nije mogu\u0107e brisati zaglavlje dok se ne pobrišu stavke!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    } else {
      if (this.getDetailSet().rowCount() > 0) {
        String selSQL = this.CheckMasterKeySQLString();
        deleteSQL = "DELETE" + selSQL.substring(selSQL.indexOf("*") + 1);
      }
      return true;
    }
  }

  public void AfterDeleteMaster() {
    if (!deleteSQL.equals(""))
      vl.runSQL(deleteSQL);
  } */

 /* public void AfterAfterSaveMaster(char mode) {
    super.AfterAfterSaveMaster(mode);
    raDetail.setLockedMode('0');
  }*/

  public void ZatvoriOstaloDetail() {
    int row = this.getMasterSet().getRow();
      this.getMasterSet().refresh();
      raMaster.getJpTableView().fireTableDataChanged();
      this.getMasterSet().goToClosestRow(row);
  }

  public void SetFokusDetail(char mode) {
  
  }

  public boolean ValidacijaDetail(char mode) {
    return true;
  }

  protected boolean MasterNotUnique() {
    return (vl.RezSet.rowCount() > 0);
  }

  /*public void handleError(String msg) {
    JlrNavField errf;
    if (rpc.getParam().equals("CART")) errf = rpc.jrfCART;
    else if (rpc.getParam().equals("CART1")) errf = rpc.jrfCART1;
    else errf = rpc.jrfBC;

    rpc.EnabDisab(true);
    //rpc.setCART();
    EraseFields();
    errf.setText("");
    errf.setErrText(msg);
    errf.this_ExceptionHandling(new Exception());
    errf.setErrText(null);
  }*/


  public void detailSet_navigated(NavigationEvent e) {

  }
  
  public void masterSet_navigated(NavigationEvent e) {
    //new Throwable().printStackTrace();
  }
  
  public void SetPanels(JPanel master, JPanel detail, boolean detailBorder) {
    jpDetail = detail;
    if (detailBorder)
      jpDetail.setBorder(BorderFactory.createEtchedBorder());
    jpDetailMain.setLayout(new BorderLayout());
    jpDetailMain.add(jpDetail, BorderLayout.CENTER);
    this.setJPanelDetail(jpDetailMain);

    jpMasterMain = master;
    this.setJPanelMaster(jpMasterMain);
  }
  
  private void jbInit() throws Exception {

    this.setMasterSet(null);
    this.setNaslovMaster("Grupe napomena");
    this.setVisibleColsMaster(new int[] {0, 1});
    this.setMasterKey(new String[] {"CAN"});

    this.setDetailSet(Artnap.getDataModule().getFilteredDataSet("1=0"));
    this.setNaslovDetail("Napomene grupe");
    this.setVisibleColsDetail(new int[] {0,2,3});
    this.setDetailKey(new String[] {"CAN", "TEXTNAP"});

    SetPanels(null, null, false);
  }
  
}
