/****license*****************************************************************
**   file: raMasterFakeDetailArtikl.java
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

/**
 * Apstraktna klasa koja simulira master-detail odnos iz jedne tablice, gdje je
 * detail dio odre\u0111en artiklom (uz dodatna polja po potrebi). Potrebno naslijediti,
 * a zatim overridati odre\u0111ene metode. Za master dio:
 * - definirati detail panel.
 * - definirati (protected) QueryDataSet mast pomo\u0107u odgovaraju\u0107eg SQL upita.
 * - overridati metode EntryPointMaster, SetFokusMaster i ValidacijaMaster.
 * Za detail dio:
 * - definirati donji dio detail panela (u gornjem je rapancart).
 * - overridati (abstract) metodu SetFokusIzmjena, kojom treba postaviti fokus
 *   na odgovaraju\u0107e polje ispod rapancarta. Metoda se poziva prilikom izmjene (F4)
 *   ili nakon odabira artikla u rapancartu.
 * - (opcionalno) overridati metodu ClearFields, u kojoj se mogu izbrisati polja
 *   ispod rapancarta.
 * Zajedni\u010Dki paramteri:
 * - metoda CheckMasterKeySQLString treba vratiti string koji sadrži SQL upit
 *   kojim se nalaze svi slogovi sa istim master klju\u010Dem.
 *
 */
public abstract class raMasterFakeDetailArtikl extends raMasterDetail {

  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();

  JPanel jpMasterMain;
  JPanel jpDetailMain = new JPanel();
  JPanel jpDetail;

  private String deleteSQL;
//  private boolean unlock = false;

  protected rapancart rpc = new rapancart() {
    public void metToDo_after_lookUp() {
      if (!rpcLostFocus && raDetail.getMode() == 'N') {
        rpcLostFocus = true;
        rpcOut();
      };
    }
  };

  protected boolean rpcLostFocus/*, rpcUpdated*/;

  protected QueryDataSet mast = new QueryDataSet() {
//    public void saveChanges() {
//      this.post();
//    }
    public boolean saveChangesSupported() {
      return false;
    }
  };


  public raMasterFakeDetailArtikl() {
    this.setMasterDeleteMode(canDeleteMaster() ? DELDETAIL : NODEL);
  }

  public void beforeShowMaster() {
    mast.refresh();
//    refilterDetailSet();
    this.getDetailSet().open();
    initRpcart();
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

  public boolean canDeleteMaster() {
    return false;
  }

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
      rpc.setCART();
      EraseFields();
    } else if (mode == 'I') {
      rcc.EnabDisabAll(jpDetail, true);
    }
  }

  public void SetFokusDetail(char mode) {
//    rpc.InitRaPanCart();
    if (mode == 'N') {
      rpc.setCART();
      EraseFields();
    } else if (mode == 'I' ){
      SetFokusIzmjena();
    }
    /*if (unlock) {
      unlock = false;
      raDetail.setLockedMode('O');
    }*/
  }

  public abstract void SetFokusIzmjena();

  public boolean ValidacijaDetail(char mode) {
    if (rpc.getCART().equals("")) {
      EraseFields();
      JOptionPane.showMessageDialog(this.jpDetail,"Obavezan unos Artikla!","Greška",
        JOptionPane.ERROR_MESSAGE);
      rpc.EnabDisab(true);
      EraseFields();
      return false;
    }
    if (mode == 'N' && artNotUnique(rpc.getCART())) {
      EraseFields();
      JOptionPane.showMessageDialog(this.jpDetail,"Artikl ve\u0107 u tablici!","Greška",
        JOptionPane.ERROR_MESSAGE);
      rpc.EnabDisab(true);
      EraseFields();
      rpc.setCART();
      return false;
    }
//    if (!rpcUpdated && mode == 'N') return false;
    return Validacija(mode);
  }

  public boolean Validacija(char mode) {
    return true;
  }

  public void AfterSaveDetail(char mode) {
    if (mode == 'N') {
      rpc.EnabDisab(true);
    }
  }

  public boolean ValDPEscapeDetail(char mode) {
    if (mode == 'N' && rpcLostFocus) {
      rpc.EnabDisab(true);
      rpc.setCART();
      EraseFields();
      return false;
    }
    return true;
  }

  public void ClearFields() {}

  public abstract String CheckMasterKeySQLString();

  protected void EraseFields() {
    rpcLostFocus = false;
//    rpcUpdated = false;
    ClearFields();
    rcc.EnabDisabAll(jpDetail, false);
  }

  public void enabAll() {
    rcc.EnabDisabAll(jpDetail, true);
  }

  protected boolean MasterNotUnique() {
    vl.execSQL(CheckMasterKeySQLString());
    vl.RezSet.open();
    return (vl.RezSet.rowCount() > 0);
  }

  /*
   * Metoda koja provjerava ima li ve\u0107 u dokumentu stavka s istim artiklom.
   */
  protected boolean artNotUnique(String art) {
    vl.execSQL(CheckMasterKeySQLString() + " and cart = " + art);
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

  protected boolean rpcOut() {
    // provjeri nalazi li ve\u0107 u dokumentu isti artikl
    if (artNotUnique(rpc.getCART())) {
//      JOptionPane.showMessageDialog(this.jpDetail,"Artikl ve\u0107 u tablici!","Greška",
//        JOptionPane.ERROR_MESSAGE);
      EraseFields();
      Aut.getAut().handleRpcErr(rpc, "Artikl ve\u0107 u tablici!");
//      rpc.jrfCART.this_ExceptionHandling(new Exception());
      return false;
    }
    //rcc.EnabDisabAll(jPanel2, true);
    enabAll();
    SetFokusIzmjena();
    return true;
  }
  
  

  public void detailSet_navigated(NavigationEvent e) {
    rpcLostFocus = true;
  }
  
  public void SetPanels(JPanel master, JPanel detail, boolean detailBorder) {
    jpDetail = detail;
    if (detailBorder)
      jpDetail.setBorder(BorderFactory.createEtchedBorder());
    jpDetailMain.setLayout(new BorderLayout());
    jpDetailMain.add(rpc, BorderLayout.NORTH);
    jpDetailMain.add(jpDetail, BorderLayout.CENTER);
    this.setJPanelDetail(jpDetailMain);

    jpMasterMain = master;
    this.setJPanelMaster(jpMasterMain);
  }

  protected void initRpcart() {
    //rpc.setGodina(hr.restart.util.Valid.getValid().findYear(dm.getDoku().getTimestamp("DATDOK")));
    //rpc.setCskl(dm.getStdoku().getString("CSKL"));
    rpc.setFocusCycleRoot(true);
    rpc.setMode("DOH");
    rpc.setDefParam();
    rpc.InitRaPanCart();
  }
}
