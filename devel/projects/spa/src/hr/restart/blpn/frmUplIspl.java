/****license*****************************************************************
**   file: frmUplIspl.java
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
package hr.restart.blpn;

import hr.restart.baza.Condition;
import hr.restart.baza.Skstavke;
import hr.restart.baza.Skstavkerad;
import hr.restart.baza.UIstavke;
import hr.restart.baza.dM;
import hr.restart.gk.frmKnjSKRac;
import hr.restart.sisfun.frmParam;
import hr.restart.sk.frmIzborStavki;
import hr.restart.sk.frmSalKon;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;
import hr.restart.util.raTransaction;
import hr.restart.zapod.dlgGetKnjig;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

public class frmUplIspl extends raMasterDetail {

  sgStuff ss = sgStuff.getStugg();
  Valid vl = Valid.getValid();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Util ut = Util.getUtil();
  dM dm = dM.getDataModule();
  StorageDataSet preselectData;

  QueryDataSet repQDS = new QueryDataSet();
  QueryDataSet repQDS2 = new QueryDataSet();

  BigDecimal tmpIzdatak;
  BigDecimal tmpPVIzdatak;
  BigDecimal tmpPVPrimitak;
  BigDecimal tmpPVSaldo;
  BigDecimal razlikaUplatitiPN;
  BigDecimal tmpPrimitak;
  BigDecimal tmpSaldo;
  BigDecimal razlikaIsplatitiPN;

  String ulet = "";
  String _knjig;
  String _oznval;

  int tmpRbs;
  int _cblag;

  short _godina;

  boolean bezgotovinska;
  boolean isplacen;

  boolean nijenula;
  boolean cpLkp = false;
  boolean radLkp = false;

  raNavAction rnvKnjizenjeGumbic =
    new raNavAction("Zaklju\u010Divanje izvještaja", raImages.IMGHISTORY, KeyEvent.VK_F10) {
      public void actionPerformed(ActionEvent e) {
        zakljucivanje();
      }
    };

    jpMiniPres mp;

    raNavAction rnvRazlikaPNGumbic = new raNavAction("Isplata razlike po putnom nalogu", raImages.IMGCOMPOSEMAIL, KeyEvent.VK_F6) {
      public void actionPerformed(ActionEvent e) {
        try {
          mp = new jpMiniPres(preselectData);
          mp.setTitle("Putni nalog");
          mp.show();
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    };
    private String lastBLID;

  public static frmUplIspl getUplIspl() {
    return upisp;
  }

  static frmUplIspl upisp;

  public frmUplIspl() {
    try {
      upisp = this;
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void AfterCancelDetail() {
    oldPrim = Aus.zero2;
    oldIzd = Aus.zero2;
    if (raDetail.getMode() == 'N') {
      sweepSK(false, lastBLID);
    }
  }

  
  private void sweepSK(boolean transact, String blid) {
    System.out.println("sweepSK - MODE = "+raDetail.getMode() + "key = "+blid);
    QueryDataSet sk = getSkStavkerad(blid);
    if (sk.getRowCount() > 0) {
      if (!transact && !sweepQuestion()) return;
      sk.deleteAllRows();
      if (transact) {
        raTransaction.saveChanges(sk);
      } else {
        sk.saveChanges();
      }
    }
  }
  
  private boolean sweepQuestion() {
//    return JOptionPane.showConfirmDialog(getContentPane(), "Prekidom unosa stavke brišu se sve unešene URE vezane uz tu stavku!! Obrisati URE?", "Pozor!!!", 
//        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
     JOptionPane.showMessageDialog(getContentPane(), 
         "Prekidom unosa stavke ostaju se sve unešene URE vezane uz tu stavku!! Te URE æe se pojaviti na slijedeæoj novoj stavci pa ih pobrišite ruèno ili ostavite", "Pozor!!!", 
         JOptionPane.WARNING_MESSAGE);
     return false;
  }

  public void AfterDeleteDetail() {
    updateDelete();
    sweepSK(true, lastBLID);
  }
  
  public void AfterDeleteMaster() {
    raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[5], false);
  }

  public void AfterSaveDetail(char mode) {

    if (mode == 'N') {
    this.jpDetail.jlrCradnik.emptyTextFields();
    this.jpDetail.jlrStavka.emptyTextFields();
    this.getMasterSet().setBigDecimal("UKPRIMITAK", this.getMasterSet().getBigDecimal("UKPRIMITAK").add(this.getDetailSet().getBigDecimal("PRIMITAK")));
    this.getMasterSet().setBigDecimal("UKIZDATAK", this.getMasterSet().getBigDecimal("UKIZDATAK").add(this.getDetailSet().getBigDecimal("IZDATAK")));
    this.getMasterSet().setBigDecimal("SALDO", this.getMasterSet().getBigDecimal("UKPRIMITAK").subtract(this.getMasterSet().getBigDecimal("UKIZDATAK")));
    this.getMasterSet().setBigDecimal("UKSALDO", this.getMasterSet().getBigDecimal("SALDO").add(this.getMasterSet().getBigDecimal("PRIJENOS")));
    this.getMasterSet().setBigDecimal("PVUKPRIMITAK", this.getMasterSet().getBigDecimal("PVUKPRIMITAK").add(this.getDetailSet().getBigDecimal("PVPRIMITAK")));
    this.getMasterSet().setBigDecimal("PVUKIZDATAK", this.getMasterSet().getBigDecimal("PVUKIZDATAK").add(this.getDetailSet().getBigDecimal("PVIZDATAK")));
    this.getMasterSet().setBigDecimal("PVSALDO", this.getMasterSet().getBigDecimal("PVUKPRIMITAK").subtract(this.getMasterSet().getBigDecimal("PVUKIZDATAK")));
    this.getMasterSet().setBigDecimal("PVUKSALDO", this.getMasterSet().getBigDecimal("PVSALDO").add(this.getMasterSet().getBigDecimal("PVPRIJENOS")));
    if (this.getMasterSet().getTimestamp("DATDO").before(this.getDetailSet().getTimestamp("DATUM"))) this.getMasterSet().setTimestamp("DATDO",this.getDetailSet().getTimestamp("DATUM"));
    } else if (mode == 'I') {
      maketmps();
      updateEdit();
      this.getMasterSet().setBigDecimal("UKPRIMITAK", this.getMasterSet().getBigDecimal("UKPRIMITAK").add(this.getDetailSet().getBigDecimal("PRIMITAK")));
      this.getMasterSet().setBigDecimal("UKIZDATAK", this.getMasterSet().getBigDecimal("UKIZDATAK").add(this.getDetailSet().getBigDecimal("IZDATAK")));
      this.getMasterSet().setBigDecimal("SALDO", this.getMasterSet().getBigDecimal("UKPRIMITAK").subtract(this.getMasterSet().getBigDecimal("UKIZDATAK")));
      this.getMasterSet().setBigDecimal("UKSALDO", this.getMasterSet().getBigDecimal("SALDO").add(this.getMasterSet().getBigDecimal("PRIJENOS")));
      this.getMasterSet().setBigDecimal("PVUKPRIMITAK", this.getMasterSet().getBigDecimal("PVUKPRIMITAK").add(this.getDetailSet().getBigDecimal("PVPRIMITAK")));
      this.getMasterSet().setBigDecimal("PVUKIZDATAK", this.getMasterSet().getBigDecimal("PVUKIZDATAK").add(this.getDetailSet().getBigDecimal("PVIZDATAK")));
      this.getMasterSet().setBigDecimal("PVSALDO", this.getMasterSet().getBigDecimal("PVUKPRIMITAK").subtract(this.getMasterSet().getBigDecimal("PVUKIZDATAK")));
      this.getMasterSet().setBigDecimal("PVUKSALDO", this.getMasterSet().getBigDecimal("PVSALDO").add(this.getMasterSet().getBigDecimal("PVPRIJENOS")));
      
      
      
      this.getMasterSet().setTimestamp("DATDO", this.getMasterSet().getTimestamp("DATOD"));
      
//      this.getDetailSet().first();
//      do {
//        if (this.getMasterSet().getTimestamp("DATDO").before(this.getDetailSet().getTimestamp("DATUM"))) {
//          this.getMasterSet().setTimestamp("DATDO", this.getDetailSet().getTimestamp("DATUM"));
//        }
//      } while (this.getDetailSet().next());
      Variant v = new Variant();
      for (int r = 0; r < getDetailSet().rowCount(); r++) {
        getDetailSet().getVariant("DATUM", r, v);
        if (this.getMasterSet().getTimestamp("DATDO").before(v.getTimestamp())) {
          this.getMasterSet().setTimestamp("DATDO", v.getTimestamp());
        }
      }
    }
    
    try {
      raTransaction.saveChanges(this.getMasterSet());
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    raMaster.getJpTableView().fireTableDataChanged();
  }

  public boolean DeleteCheckDetail() {
    if (this.getMasterSet().getString("STATUS").equals("K")) {
      return false;
    }
    if (this.getMasterSet().getString("STATUS").equals("Z")) {
      return false;
    }
    if (false) {
      return false;
    }
    maketmps();
    lastBLID = getBLUID();
    if (getSkStavkerad().getRowCount() > 0) {
      JOptionPane.showMessageDialog(getContentPane(), "Potrebno je prvo obrisati URE vezane uz ovu stavku");
      return false;
    }
    return true;
  }

  public boolean DeleteCheckMaster() {
    if (this.getMasterSet().getString("STATUS").equals("K")) {
      return false;
    }
    if (this.getMasterSet().getString("STATUS").equals("Z")) {
      return false;
    }
    if (false) { /** @todo dodat flag iz prava */
      return false;
    }
    return true;
  }

  public void EntryPointDetail(char mode) {
    if (mode == 'N') {
//      this.getMasterSet().setTimestamp("DATDO", vl.getToday());
      this.getDetailSet().setTimestamp("DATUM", preselectData.getTimestamp("DATOD-to"));
        rcc.EnabDisabAll(jpDetail, true);
        changeCorgState(false);
        getPreSelect().copySelValues();
        this.getDetailSet().setString("VRDOK", "BL");
        this.getDetailSet().setString("CORG", preselectData.getString("KNJIG"));
        jpDetail.jlrCorg.forceFocLost();
        if (!preselectData.getString("OZNVAL").equals(hr.restart.zapod.Tecajevi.getDomOZNVAL())) {
          this.getDetailSet().setBigDecimal("TECAJ", ss.getTECAJ(preselectData.getString("OZNVAL"), this.getDetailSet().getTimestamp("DATUM")));
        rcc.setLabelLaF(this.jpDetail.jraPvizdatak, false);
        rcc.setLabelLaF(this.jpDetail.jraPvprimitak, false);
        rcc.setLabelLaF(this.jpDetail.jraTecaj, false);
        this.getDetailSet().setString("CSKL", "1");
        jpDetail.setStavka(this.getDetailSet().getString("CSKL"));
      }
    }
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jpMaster.jraDatod, false);
    }
  }

  public void Funkcija_ispisa_detail() {
    this.raDetail.getRepRunner().clearAllReports();
    if (this.raDetail.getSelectionTracker().countSelected() <= 1){
//      this.raDetail.getRepRunner().addReport("hr.restart.blpn.repUplIsplUplatnice", "Ispis uplatnice / isplatnice", 2);
      this.raDetail.getRepRunner().addReport("hr.restart.blpn.repUplIspl","hr.restart.blpn.repUplIsplUplatnice","UplIsplUplatnice","Ispis uplatnice / isplatnice"); //repSkupnaIsplatnica
      Aus.refilter(repQDS, ss.getRepQDSJednaUplatnica(this.getMasterSet(), this.getDetailSet(), "stavblag"));
    } else {
      System.out.println("ima selekshna");
      this.raDetail.getRepRunner().addReport("hr.restart.blpn.repSkupnaIspl","hr.restart.blpn.repUplIsplUplatnice","SkupnaIsplatnica","Ispis skupne isplatnice"); //repSkupnaIsplatnica
      String [] rbrs = new String[this.raDetail.getSelectionTracker().countSelected()];
      this.raDetail.getJpTableView().enableEvents(false);
      int position = this.getDetailSet().getRow();
      this.getDetailSet().first();
      int i = 0;
      do {
        if (this.raDetail.getSelectionTracker().isSelected(this.getDetailSet())){
          rbrs[i++] = this.getDetailSet().getInt("RBS")+"";
          System.out.println("rbrs["+(i-1)+"] = " + this.getDetailSet().getInt("RBS")); //XDEBUG delete when no more needed
        }
      } while (this.getDetailSet().next());
      this.getDetailSet().goToRow(position);
      this.raDetail.getJpTableView().enableEvents(true);
      
      Aus.refilter(repQDS, ss.getRepQDSSkupnaIsplatnica(this.getMasterSet(), this.getDetailSet(), "stavblag", rbrs));
    }
    super.Funkcija_ispisa_detail();
  }

  public void Funkcija_ispisa_master() {
  	Aus.refilter(repQDS, ss.getRepQDSIzvjestaj(this.getMasterSet(), "stavblag"));
  	//System.out.println(ss.getRepQDSSveUplatnice(this.getMasterSet(), "stavblag"));
  	Aus.refilter(repQDS2, ss.getRepQDSSveUplatnice(this.getMasterSet(), "stavblag"));
    super.Funkcija_ispisa_master();
  }
  
  private BigDecimal oldPrim = Aus.zero2;
  private BigDecimal oldIzd = Aus.zero2;

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
//      this.getMasterSet().setTimestamp("DATDO", vl.getToday());
      //this.getDetailSet().setTimestamp("DATUM", vl.getToday());
      this.getDetailSet().setTimestamp("DATUM", preselectData.getTimestamp("DATOD-to"));
      getPreSelect().copySelValues();
      this.getDetailSet().setString("VRDOK", "BL");
      this.getDetailSet().setString("CORG", preselectData.getString("KNJIG"));
      jpDetail.jlrCorg.forceFocLost();
      if (!preselectData.getString("OZNVAL").equals(hr.restart.zapod.Tecajevi.getDomOZNVAL())){
        this.getDetailSet().setBigDecimal("TECAJ", ss.getTECAJ(preselectData.getString("OZNVAL"), this.getDetailSet().getTimestamp("DATUM")));
      }
      rcc.setLabelLaF(this.jpDetail.jraPvizdatak, false);
      rcc.setLabelLaF(this.jpDetail.jraPvprimitak, false);
      rcc.setLabelLaF(this.jpDetail.jraTecaj, false);
      this.getDetailSet().setString("CSKL", "1");
      jpDetail.setStavka(this.getDetailSet().getString("CSKL"));
      jpDetail.jraDatum.requestFocus();
      jpDetail.jraDatum.selectAll();
    } else if (mode == 'I') {
//      rcc.EnabDisabAll(this.jpDetail, false);
      rcc.setLabelLaF(this.jpDetail.jraPrimitak,!isBigDecimalFieldZero("PRIMITAK"));
      rcc.setLabelLaF(this.jpDetail.jraIzdatak,!isBigDecimalFieldZero("IZDATAK"));
      oldPrim = this.getDetailSet().getBigDecimal("PRIMITAK");
      oldIzd = this.getDetailSet().getBigDecimal("IZDATAK");
      jpDetail.jraDatum.requestFocus();
      jpDetail.jraDatum.selectAll();
    }
    lastBLID = getBLUID();
    StorageDataSet sksr = getSkStavkerad();
    boolean isUra = sksr.getRowCount() > 0;
    containsURA(isUra);
    if (isUra) {
      getDetailSet().setBigDecimal("IZDATAK", frmSalKon.getBlagIznos(sksr));
      getDetailSet().setTimestamp("DATUM", sksr.getTimestamp("DATPRI"));
    } else jpDetail.initAdvancedDetail(mode);
  }
  
  private boolean isBigDecimalFieldZero(String field){
    return this.getDetailSet().getBigDecimal(field).equals(Aus.zero2);
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      getPreSelect().copySelValues();
      jpMaster.jraDatod.requestFocus();
    } else if (mode == 'I') {
    }
  }

  public QueryDataSet getSkStavkerad() {
    return getSkStavkerad(getBLUID());
  }
  public static QueryDataSet getSkStavkerad(String blid) {
    QueryDataSet _set = Skstavkerad.getDataModule().getFilteredDataSet(Condition.equal("ZIRO", blid));
    _set.open();
    return _set;
  }
  public static QueryDataSet getSkUIStavke(String blid) {
    QueryDataSet sks = Skstavke.getDataModule().getFilteredDataSet(Condition.equal("ZIRO", blid));
    sks.open();
    QueryDataSet uis = UIstavke.getDataModule().getTempSet(Condition.nil);
    uis.open();
    for (sks.first(); sks.inBounds(); sks.next()) {
      QueryDataSet _uis = frmKnjSKRac.getUIStavke(sks);
      for (_uis.first(); _uis.inBounds(); _uis.next()) {
        if (_uis.getInt("RBS")!=1) {
          uis.insertRow(false);
          dM.copyColumns(_uis, uis);
          uis.post();
        }
      }
    }
    return uis;
  }
  
  public boolean ValidacijaDetail(char mode) {
    if (this.getMasterSet().getString("STATUS").equals("K") || this.getMasterSet().getString("STATUS").equals("Z")) {
      return false;
    }
    if (ss.isEmpty(jpDetail.jraPrimitak, jpDetail.jraIzdatak)){
      return false;
    }

    if (jpDetail.advui == null) {//ako unosi konto ne mora i shemu
      if (vl.isEmpty(jpDetail.jlrStavka)){
        return false;
      }
    }

    if (this.getDetailSet().getTimestamp("DATUM").before(this.getMasterSet().getTimestamp("DATOD"))) {
      JOptionPane.showMessageDialog(
          jpDetail,
          "Datum mora biti veæi od datuma od kojeg je blagajnièki izvještaj!",
          ResourceBundle.getBundle("hr.restart.baza.dmRes").getString("errMain"),
          javax.swing.JOptionPane.ERROR_MESSAGE);

      jpDetail.jraDatum.requestFocusLater();
      return false;
    }
    QueryDataSet sk = getSkStavkerad();
    if (mode == 'N') {
      String vrsta = "";
      if (this.getDetailSet().getBigDecimal("PRIMITAK").compareTo(Aus.zero2) != 0) vrsta = "U";
      else vrsta = "I";
      
      this.getDetailSet().setInt("RBS", ss.getNoviBrStav(this.getMasterSet(), vrsta));
      this.getDetailSet().setString("VRSTA", vrsta);
      
      for (sk.first(); sk.inBounds(); sk.next()) {
        sk.setString("ZIRO", getBLUID());
        //if (sk.getInt("RBS") == 1) sk.setString("CGKSTAVKE", "N");//poslije
      }
      sk.saveChanges();//!!!!
      
//      if (this.getMasterSet().getTimestamp("DATDO").before(this.getDetailSet().getTimestamp("DATUM"))) {
//        this.getMasterSet().setTimestamp("DATDO", this.getDetailSet().getTimestamp("DATUM"));
//        raTransaction.saveChanges(this.getMasterSet());
//      }
      if (!preselectData.getString("OZNVAL").equals(hr.restart.zapod.Tecajevi.getDomOZNVAL())) {
        convertCurrency(this.getDetailSet().getTimestamp("DATUM"));
      } else {
        this.getDetailSet().setBigDecimal("PVPRIMITAK", this.getDetailSet().getBigDecimal("PRIMITAK"));
        this.getDetailSet().setBigDecimal("PVIZDATAK", this.getDetailSet().getBigDecimal("IZDATAK"));
      }
    } else if (mode == 'I') {

//      this.getMasterSet().setTimestamp("DATDO", this.getMasterSet().getTimestamp("DATOD"));
//
//      this.getDetailSet().first();
//      do {
//        if (this.getMasterSet().getTimestamp("DATDO").before(this.getDetailSet().getTimestamp("DATUM"))) {
//          this.getMasterSet().setTimestamp("DATDO", this.getDetailSet().getTimestamp("DATUM"));
//          raTransaction.saveChanges(this.getMasterSet());
//        }
//      } while (this.getDetailSet().next());
    }
    if (jpDetail.advui != null && sk.getRowCount() == 0) {
      if (!jpDetail.advui.validateData()) return false;
    }
    return true;
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jraDatod)) {
      jpMaster.jraDatod.requestFocus();
      JOptionPane.showMessageDialog(getJPanelMaster(),
          "Unesite datum izvješ\u0107a",
          "Greška",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (mode == 'N' && ss.isExistUIzv(preselectData)) {
      jpMaster.jraDatod.requestFocus();
      JOptionPane.showMessageDialog(getJPanelMaster(),
          new hr.restart.swing.raMultiLineMessage(new String[]{"Prethodni izvještaj nije zaklju\u010Den", "Nemogu\u0107e je otvaranje novog izvještaja"}),
          "Greška",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }

    if (ss.checkDates(this.getMasterSet())) {
      jpMaster.jraDatod.requestFocus();
      JOptionPane.showMessageDialog(getJPanelMaster(),
          new hr.restart.swing.raMultiLineMessage(new String[]{"Datum zadnje izmjene je manji od", "datuma zadnje izmjene prethodnog izvještaja"}),
          "Greška",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (mode == 'N') {
      this.getMasterSet().setInt("BRIZV", ss.getNoviBrIzv(preselectData));
      BigDecimal pr = ss.getPRIJENOS(preselectData);
      this.getMasterSet().setBigDecimal("PRIJENOS", pr);
      this.getMasterSet().setBigDecimal("UKSALDO", pr);
      BigDecimal pvp = ss.getPVPRIJENOS(preselectData);
      this.getMasterSet().setBigDecimal("PVPRIJENOS", pvp);
      this.getMasterSet().setBigDecimal("PVUKSALDO", pvp);
      raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[5], true);
    }
    return true;
  }

  private boolean arhiviranje() {
    try {
      refilterDetailSet();
      String[] kolone = new String[]{"KNJIG", "CBLAG", "OZNVAL", "GODINA", "BRIZV", "DATUM", "RBS", "PRIMITAK",
          "IZDATAK", "PVPRIMITAK", "PVIZDATAK", "TECAJ", "CRADNIK", "CPN", 
          "OPIS", "TKO", "CGRSTAV", "STAVKA", "CSKL", "VRDOK", "CORG", "VRSTA",
          "BROJKONTA", "DATDOK", "DATDOSP", "BROJDOK", "CPAR"};

      QueryDataSet dummyDetail = hr.restart.baza.Stavkeblarh.getDataModule().getTempSet("2=4");
      dummyDetail.open();
//      ut.getNewQueryDataSet("select * from stavkeblarh where 0=1");

      this.getDetailSet().first();
      do {
        dummyDetail.insertRow(false);
        this.getDetailSet().copyTo(kolone, this.getDetailSet(), kolone, dummyDetail);
      } while (this.getDetailSet().next());
      this.getDetailSet().deleteAllRows();
      //raTransaction.saveChangesInTransaction(new QueryDataSet[] {this.getDetailSet(), dummyDetail});
      raTransaction.saveChanges(getDetailSet());
      raTransaction.saveChanges(dummyDetail);
      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  public void beforeShowDetail() {
    String naslovDetail = "Stavke izvješ\u0107a broj " + this.getMasterSet().getInt("BRIZV");
    setNaslovDetail(naslovDetail);
    this.getDetailSet().refresh();
    if (preselectData.getString("OZNVAL").equals(hr.restart.zapod.Tecajevi.getDomOZNVAL())) {
      this.jpDetail.initDomesticVal();
    } else {
      this.jpDetail.initInoVal("UPLISPL");
    }
  }

  public void beforeShowMaster() {
    preselectData = getPreSelect().getSelRow();
    vl.execSQL(ss.getNazivBlagajne(preselectData));
    vl.RezSet.open();
    String blag = vl.RezSet.getString("NAZIV");
    int cbl = preselectData.getInt("CBLAG");
    _cblag = cbl;
    String val = preselectData.getString("OZNVAL");
    _oznval = val;
    _knjig = preselectData.getString("KNJIG");
    short god = preselectData.getShort("GODINA");
    _godina = god;
    String naslovMaster;
    if (!ss.getJeLiBlagajnaBezgotovinska(_knjig, cbl, val)) naslovMaster = "Blagajna: " + cbl + " " + blag + " [" + val + "] - UPLATA / ISPLATA";
    else naslovMaster = "Bezgotovinska blagajna: " + cbl + " " + blag + " [" + val + "] - UPLATA / ISPLATA";
    bezgotovinska = ss.getJeLiBlagajnaBezgotovinska(_knjig, _cblag, _oznval);
    setNaslovMaster(naslovMaster);
    if (!this.getMasterSet().getString("STATUS").equals("U")) {
      raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[5], false);
    } else {
      raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[5], true);
    }
  }


  void changeCorgState(boolean inejblaj) {
    rcc.setLabelLaF(jpDetail.jlrCorg, inejblaj);
    rcc.setLabelLaF(jpDetail.jlrNaziv, inejblaj);
    rcc.setLabelLaF(jpDetail.jbSelCorg, inejblaj);
  }

  public void convertCurrency(java.sql.Timestamp zaDatum) {
    this.getDetailSet().setBigDecimal("PVPRIMITAK",
        ss.currrencyConverterToKN(this.getDetailSet().getBigDecimal("PRIMITAK"), this.getDetailSet().getString("OZNVAL"), zaDatum));
    this.getDetailSet().setBigDecimal("PVIZDATAK",
        ss.currrencyConverterToKN(this.getDetailSet().getBigDecimal("IZDATAK"), this.getDetailSet().getString("OZNVAL"), zaDatum));
  }

  public void detailSet_navigated(com.borland.dx.dataset.NavigationEvent ne) {
    try {
      if (!ulet.equals(this.getDetailSet().getString("CSKL"))) {
        jpDetail.setStavka(this.getDetailSet().getString("CSKL"));
        ulet = this.getDetailSet().getString("CSKL");
      }
    } catch (Exception ex) {
    }
  }

  public boolean doWithSaveDetail(char mode) {
    if (this.getMasterSet().getTimestamp("DATDO").before(this.getDetailSet().getTimestamp("DATUM"))) {
      this.getMasterSet().setTimestamp("DATDO", this.getDetailSet().getTimestamp("DATUM"));
      raTransaction.saveChanges(this.getMasterSet());
    }
    return true;
  }

  public QueryDataSet getrepQDS() {
    repQDS.setSort(new SortDescriptor(new String[]{ss.getOrderBlagIzvField()}));
    return repQDS;
  }

  public QueryDataSet getrepQDS2() {
    repQDS2.setSort(new SortDescriptor(new String[]{ss.getOrderBlagIzvField()}));
    return repQDS2;
  }

  void handleCORG() {
    boolean isorgstr = false;
    try {
      //    String STAVKA = this.getDetailSet().getString("STAVKA");
      String brojkonta = hr.restart.gk.raKnjizenje.getBrojKonta("BL", this.getDetailSet().getString("CSKL"), this.getDetailSet().getString("STAVKA"));
      isorgstr = hr.restart.zapod.raKonta.isOrgStr(brojkonta);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
      if (!isorgstr) /*{ // was ***if (isorgstr)***
  //      jpDetail.jlrCorg.setText("");
  //      jpDetail.jlrCorg.emptyTextFields();
        changeCorgState(true);
      } else */{
        this.getDetailSet().setString("CORG", hr.restart.zapod.OrgStr.getKNJCORG(false));
        jpDetail.jlrCorg.forceFocLost();
      }
      changeCorgState(isorgstr);
  }

  private void jbInit() throws Exception {

    this.setMasterSet(dm.getBlagizv());
    this.setMasterDeleteMode(this.EMPTYDEL);
    this.setVisibleColsMaster(new int[]{1, 4, 7, 8, 10, 11});
    this.setMasterKey(new String[]{"KNJIG", "CBLAG", "OZNVAL", "GODINA", "BRIZV"});
    jpMaster = new jpUplIsplMaster(this);
    this.setJPanelMaster(jpMaster);
    this.raMaster.addOption(rnvKnjizenjeGumbic, 5);
    this.raMaster.getRepRunner().addReport("hr.restart.blpn.repUplIspl", "Ispis blagajni\u010Dkog izvještaja", 2);
    this.raMaster.getRepRunner().addReport("hr.restart.blpn.repUplIsplUplatniceAll", "Ispis  svih uplatnica / isplatnica", 2);

    this.setDetailSet(dm.getStavblag());
    this.setVisibleColsDetail(new int[]{5, 21, 6, 7, 8});
    this.setDetailKey(new String[]{"KNJIG", "CBLAG", "OZNVAL", "GODINA", "BRIZV", "RBS"});
    jpDetail = new jpUplIsplDetail(this);
    this.setJPanelDetail(jpDetail);
    this.raDetail.addOption(rnvRazlikaPNGumbic, 1, false);
    //selection tracker implementation...
    System.out.println("DiteljSet - " + getDetailSet().open());
    this.raDetail.getJpTableView().installSelectionTracker(/*new String[]{*/"RBS"/*, "VRSTA"}*/);
  }

  JLabel jlCPN = new JLabel();
  JLabel jlRadnik = new JLabel();
  JLabel jlVal = new JLabel();
  JlrNavField jlrCPN =
    new JlrNavField() {
      public void after_lookUp() {
        cpnlookup();
      }
    };
  JlrNavField jlrCradnik =
    new JlrNavField() {
      public void after_lookUp() {
        radniklookup();
      }
    };
  JlrNavField jlrIme =
    new JlrNavField();
  JlrNavField jlrOznval =
    new JlrNavField();
  JlrNavField jlrPrezime =
    new JlrNavField();
  public jpUplIsplDetail jpDetail;

  jpUplIsplMaster jpMaster;
  JPanel jpPNbroj = new JPanel();
  JPanel jpPVpanel = new JPanel();

  void maketmps() {
    tmpPrimitak = this.getDetailSet().getBigDecimal("PRIMITAK");
    tmpIzdatak = this.getDetailSet().getBigDecimal("IZDATAK");
    tmpPVPrimitak = this.getDetailSet().getBigDecimal("PVPRIMITAK");
    tmpPVIzdatak = this.getDetailSet().getBigDecimal("PVIZDATAK");
    tmpSaldo = tmpPrimitak.subtract(tmpIzdatak);
    tmpPVSaldo = tmpPVPrimitak.subtract(tmpPVIzdatak);
    tmpRbs = this.getDetailSet().getInt("RBS");
  }

  BigDecimal pvRazlikaIsplatitiPN;
  BigDecimal pvRazlikaUplatitiPN;

  private boolean questions() {
    int opshn = JOptionPane.showConfirmDialog(this.raMaster.getWindow(),
        new hr.restart.swing.raMultiLineMessage(new String[]{"Ova akcija Zaklju\u010Duje izvještaj",
                                                             "Da li to stvarno želite?"}),
        "Pozor!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    boolean ritrn;
    if (opshn == JOptionPane.YES_OPTION) {
      ritrn = true;
    } else {
      ritrn = false;
    }
    return ritrn;
  }

  void radniklookup() {
    if (!cpLkp){
    }
  }


  void cpnlookup() {
    if (!radLkp){
      cpLkp=true;
      jlrCradnik.forceFocLost();
      cpLkp=false;
    }
  }

  void updateDelete() {
    this.getMasterSet().setBigDecimal("UKPRIMITAK", this.getMasterSet().getBigDecimal("UKPRIMITAK").subtract(tmpPrimitak));
    this.getMasterSet().setBigDecimal("UKIZDATAK", this.getMasterSet().getBigDecimal("UKIZDATAK").subtract(tmpIzdatak));
    this.getMasterSet().setBigDecimal("PVUKPRIMITAK", this.getMasterSet().getBigDecimal("PVUKPRIMITAK").subtract(tmpPVPrimitak));
    this.getMasterSet().setBigDecimal("PVUKIZDATAK", this.getMasterSet().getBigDecimal("PVUKIZDATAK").subtract(tmpPVIzdatak));
    this.getMasterSet().setBigDecimal("SALDO", this.getMasterSet().getBigDecimal("SALDO").subtract(tmpSaldo));
    this.getMasterSet().setBigDecimal("PVSALDO", this.getMasterSet().getBigDecimal("PVSALDO").subtract(tmpPVSaldo));
    this.getMasterSet().setBigDecimal("UKSALDO", this.getMasterSet().getBigDecimal("UKSALDO").subtract(tmpSaldo));
    this.getMasterSet().setBigDecimal("PVUKSALDO", this.getMasterSet().getBigDecimal("PVUKSALDO").subtract(tmpPVSaldo));
    //vl.recountDataSet(this.raDetail, "RBS", tmpRbs);
    

    this.getMasterSet().setTimestamp("DATDO", this.getMasterSet().getTimestamp("DATOD"));
    
    this.getDetailSet().first();
    do {
      if (this.getMasterSet().getTimestamp("DATDO").before(this.getDetailSet().getTimestamp("DATUM"))) {
        this.getMasterSet().setTimestamp("DATDO", this.getDetailSet().getTimestamp("DATUM"));
      }
    } while (this.getDetailSet().next());

    try {
      raTransaction.saveChanges(this.getMasterSet());
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    raMaster.getJpTableView().fireTableDataChanged();
  }

  void updateEdit() {
    this.getMasterSet().setBigDecimal("UKPRIMITAK", this.getMasterSet().getBigDecimal("UKPRIMITAK").subtract(oldPrim));
    this.getMasterSet().setBigDecimal("UKIZDATAK", this.getMasterSet().getBigDecimal("UKIZDATAK").subtract(oldIzd));
    this.getMasterSet().setBigDecimal("PVUKPRIMITAK", this.getMasterSet().getBigDecimal("PVUKPRIMITAK").subtract(tmpPVPrimitak));
    this.getMasterSet().setBigDecimal("PVUKIZDATAK", this.getMasterSet().getBigDecimal("PVUKIZDATAK").subtract(tmpPVIzdatak));
    this.getMasterSet().setBigDecimal("SALDO", this.getMasterSet().getBigDecimal("SALDO").subtract(tmpSaldo));
    this.getMasterSet().setBigDecimal("PVSALDO", this.getMasterSet().getBigDecimal("PVSALDO").subtract(tmpPVSaldo));
    this.getMasterSet().setBigDecimal("UKSALDO", this.getMasterSet().getBigDecimal("UKSALDO").subtract(tmpSaldo));
    this.getMasterSet().setBigDecimal("PVUKSALDO", this.getMasterSet().getBigDecimal("PVUKSALDO").subtract(tmpPVSaldo));
    raTransaction.saveChanges(this.getMasterSet());
  }

  boolean validacijaPN() {
    if (vl.isEmpty(jlrCPN)){  //jlrCPN.getText().equals("")) {
      return false;
    }
    return true;
  }

  private void zakljucivanje() {
    int brIzvjestaja = this.getMasterSet().getInt("BRIZV");
    if (ss.getSTATUSPREDIZV(preselectData, (brIzvjestaja - 1)).equals("U")) { /** @todo iznac bolje rjesenje */
      JOptionPane.showMessageDialog(this.raMaster.getWindow(),
                                    new hr.restart.swing.raMultiLineMessage(new String[]{"Prethodni izvještaj nije zaklju\u010Den", "Nemogu\u0107e je zaklju\u010Divanje novog izvještaja"}),
                                    "Greška",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (ss.getJeLiBlagajnaBezgotovinska(_knjig, _cblag, _oznval)){
      if (this.getMasterSet().getBigDecimal("SALDO").compareTo(new BigDecimal(0)) != 0){
        JOptionPane.showMessageDialog(this.raMaster.getWindow(),
                                      new hr.restart.swing.raMultiLineMessage(new String[]{"Saldo blagajne nije nula", "Nemogu\u0107e je zaklju\u010Divanje izvještaja"}),
                                      "Bezgotovinska Blagajna",
                                      JOptionPane.INFORMATION_MESSAGE);
        return;
      }
    }
    if (this.getMasterSet().getBigDecimal("UKSALDO").compareTo(new BigDecimal(0)) < 0){
      JOptionPane.showMessageDialog(this.raMaster.getWindow(),
                                    new hr.restart.swing.raMultiLineMessage(new String[]{"Saldo blagajne je negativan", "Nemogu\u0107e je zaklju\u010Divanje izvještaja"}),
                                    "Blagajna" ,
                                    JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    if (this.getMasterSet().getString("STATUS").equals("K")) {
      return;
    }
    if (this.getMasterSet().getString("STATUS").equals("Z")) {
      return;
    }
    if (!questions()) {
      return;
    }
    raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[5], false);
//    System.out.println("apdejt blagajne " + ss.updateBlagajnaKodArhiviranja(this.getMasterSet(), fromPres));

    this.getMasterSet().setString("STATUS", "Z");
    hr.restart.util.raLocalTransaction newTransaction = new hr.restart.util.raLocalTransaction() {
      public boolean transaction() {
        try {
          raTransaction.runSQL(ss.updateBlagajnaKodArhiviranja(getMasterSet(), preselectData));
        }
        catch (Exception ex) {
          ex.printStackTrace();
          return false;
        }
        if (!arhiviranje()) {
          return false;
        }
        saveChanges(getMasterSet());
      return true;
      }};
    if (newTransaction.execTransaction()) {
      potvrdiSKStavkerad();
      getMasterSet().refresh();
      raMaster.getJpTableView().fireTableDataChanged();
    } else {
      JOptionPane.showMessageDialog(getContentPane(), "Potvrda nije uspjela !!!");
    }
  }

  private void potvrdiSKStavkerad() {
    String bluid = getBLUID(getMasterSet());
    QueryDataSet sk = Skstavkerad.getDataModule().getTempSet(Condition.equal("RBS", 1)
        .and(Condition.raw("ZIRO like '"+bluid+"%'")));
    sk.open();
    for (sk.first(); sk.inBounds(); sk.next()) {
      sk.setString("CGKSTAVKE", "N");
    }
    sk.saveChanges();
    frmIzborStavki.proknjizi(sk, true);
  }

  public String getUIKnjige() {
    return frmParam.getParam("blpn", "bluknj","A","Knjiga u koju se unose URE kroz blagajnu");
  }

  public String getBLUID() {
    return getBLUID(getDetailSet());
  }
  public static String getBLUID(ReadRow stavka) {
    String s = "BL#:"+stavka.getString("KNJIG")
        +"#"+stavka.getInt("CBLAG")
        +"#"+stavka.getString("OZNVAL")
        +"#"+stavka.getShort("GODINA")
        +"#"+stavka.getInt("BRIZV")+"#";
    if (stavka.hasColumn("RBS") != null) {
        s = s+stavka.getInt("RBS");
    }
    return s;
  }

  public void startURA() {
    if (frmSalKon.getInstance() == null) new frmSalKon();
    frmSalKon salkon = frmSalKon.getInstance();
    raDetail.setEnabled(false);
    salkon.setBlagajna(this);
    salkon.show();
  }

  public void containsURA(boolean contains) {
    if (contains) {
      getDetailSet().setString("BROJKONTA",getFakeURABrojkonta());
      getDetailSet().setString("CORG", dlgGetKnjig.getKNJCORG());
    }
    rcc.setLabelLaF(jpDetail.jraDatum, !contains);
    rcc.setLabelLaF(jpDetail.jraIzdatak, !contains);
    rcc.setLabelLaF(jpDetail.jraPrimitak, !contains);
    rcc.setLabelLaF(jpDetail.jlrCorg, !contains);
    rcc.setLabelLaF(jpDetail.jlrNaziv, !contains);
    rcc.setLabelLaF(jpDetail.jbSelCorg, !contains);
    rcc.EnabDisabAll(jpDetail.advui, !contains);
    jpDetail.advui.tsk.setLabelLafPar(!contains);
    if (contains) jpDetail.advui.tsk.rebind(getDetailSet());
  }
  
  public static String getFakeURABrojkonta() {
    return frmParam.getParam("blpn", "fakeskkto", "URA", "Broj konta koji podmecemo u stavku blagajne kad knjizimo URE kroz blagajnu");
  }

  public String getKontoURA() {
    String kto = frmParam.getParam("blpn", "ktoblura","","Konto koji se podmece na glavnu stavku URA kad se unose kroz blagajnu");
    if (kto.trim().equals("")) kto = null;
    return kto;
  }
}