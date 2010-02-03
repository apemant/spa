/****license*****************************************************************
**   file: frmArtNap.java
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
package hr.restart.robno;

import hr.restart.baza.dM;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.sql.dataset.QueryDataSet;


public class frmArtNap extends raMasterDetail {

  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();

  JPanel jpMasterMain;
  JPanel jpDetailMain = new JPanel();
  JPanel jpDetail;
  
//  private String deleteSQL;
//  private boolean unlock = false;


  protected QueryDataSet mast = new QueryDataSet() {
//    public void saveChanges() {
//      this.post();
//    }
    public boolean saveChangesSupported() {
      return false;
    }
  };


  public frmArtNap() {
    this.setMasterDeleteMode(DELDETAIL);
  }

  public void beforeShowMaster() {
    mast.refresh();
//    refilterDetailSet();
    this.getDetailSet().open();
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

  public void EntryPointDetail(char mode) {
    if (mode == 'N') {
      EraseFields();
    } else if (mode == 'I') {
      rcc.EnabDisabAll(jpDetail, true);
    }
  }

  public void SetFokusDetail(char mode) {
//    rpc.InitRaPanCart();
    if (mode == 'N') {
      EraseFields();
    } else if (mode == 'I' ){
      SetFokusIzmjena();
    }
    /*if (unlock) {
      unlock = false;
      raDetail.setLockedMode('O');
    }*/
  }

  public void SetFokusIzmjena() {
    
  }

  public boolean ValidacijaDetail(char mode) {
    return Validacija(mode);
  }

  public boolean Validacija(char mode) {
    return true;
  }

  public void AfterSaveDetail(char mode) {
    if (mode == 'N') {

    }
  }

  public boolean ValDPEscapeDetail(char mode) {
    return true;
  }

  public void ClearFields() {}

  protected void EraseFields() {
    ClearFields();
    rcc.EnabDisabAll(jpDetail, false);
  }

  public void enabAll() {
    rcc.EnabDisabAll(jpDetail, true);
  }

  protected boolean MasterNotUnique() {
    //vl.execSQL(CheckMasterKeySQLString());
    vl.RezSet.open();
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
}
