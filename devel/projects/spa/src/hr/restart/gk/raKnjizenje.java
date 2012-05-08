/****license*****************************************************************
**   file: raKnjizenje.java
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
package hr.restart.gk;

import hr.restart.baza.Condition;
import hr.restart.baza.Gkstavke;
import hr.restart.baza.Skstavke;
import hr.restart.baza.Skstavkerad;
import hr.restart.baza.dM;
import hr.restart.db.raConnectionFactory;
import hr.restart.db.raPreparedStatement;
import hr.restart.sisfun.frmParam;
import hr.restart.sk.raVrdokMatcher;
import hr.restart.util.Aus;
import hr.restart.util.PreSelect;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raTransaction;
import hr.restart.util.statusHandler;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.Tecajevi;
import hr.restart.zapod.raKonta;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

public class raKnjizenje {
  private frmNalozi fnalozi;
  private com.borland.dx.dataset.DataSet dataSet;
  private Valid vl = Valid.getValid();
  private String errMsg;
  private boolean zbirno = false;
  /* Oznaka STAVKE naloga u formati KNJIG-GOD-CVRNAL-RBR-RBS */
  public static int CNALMODE_RBS = 0;
  /* Oznaka naloga u formati KNJIG-GOD-CVRNAL-RBR */
  public static int CNALMODE = 1;
  /* Osmeroznamenkasta oznaka naloga za Fink */
  public static int CNALMODE_8 = 2;
  /*
   * Odlucuje izmedju CNALMODE i CNALMODE_RBS ovisno o tome da li je Konta.NACPR
   * zbirno ili poredinacno
   */
  public static int CNALMODE_KONTO_NACPR = 4;
  private int cnalogaMode = CNALMODE_KONTO_NACPR;
  private StorageDataSet stavke_add = new StorageDataSet();
  private StorageDataSet stavke_addSK = new StorageDataSet();
  //za knjizenje u sk i to samo ako je SKRacKnj = true
  StorageDataSet skstavke = new StorageDataSet();
  StorageDataSet uistavke = new StorageDataSet();
  private BigDecimal nula = new BigDecimal(0.00);
  //za azuriranje cnaloga ili cgkstavke
  private HashMap azurList = new HashMap(); //cgkstavke, infoList
  private String[] infoKeys;
  private DataSet transferSet;
  private String infColName = "CNALOGA";
  private boolean transferInfoAddingEnabled = true;
  //
  private boolean SKRacKnj = true;
  public String originalCNALOGA = "";
  boolean fake;
  String cGK = null;
  raKnjizenjeSK kSk;

  //
  /**
   * Instancira tzv. worker klasu za knjizenje iz drugih modula upotreba bi isla
   * otprilike ovako:
   * 
   * @param dset
   *          dataset koji sadrzi napunjene kolone CVRNAL i DATUMKNJ
   */
  public raKnjizenje(DataSet dset) {
    dataSet = dset;
    jInit();
    
    
  }

  private void jInit() {
    setErrorMessage("Nepoznata greška");
    allColsSK = raConnectionFactory.getColumns(hr.restart.baza.dM
        .getDataModule().getDatabase1().getJdbcConnection(), "gkstavkerad");
    stavke_add.setColumns(hr.restart.baza.dM.getDataModule().getGkstavkerad()
        .cloneColumns());
    stavke_add.addColumn("ZAGK", Variant.BOOLEAN);
    stavke_add.open();
    stavke_addSK.setColumns(hr.restart.baza.dM.getDataModule().getGkstavkerad()
        .cloneColumns());
    stavke_addSK.addColumn("ZAGK", Variant.BOOLEAN);
    stavke_addSK.addColumn(dM.createStringColumn("CSKL", 12));
    stavke_addSK.addColumn(dM.createTimestampColumn("DATPRI"));
    stavke_addSK.addColumn(dM.createStringColumn("GLAVNA", 1));
    stavke_addSK.addColumn(dM.getDataModule().getSkstavke().getColumn("CGKSTAVKE").cloneColumn());
    stavke_addSK.open();
    uistavke.setColumns(hr.restart.baza.dM.getDataModule().getUIstavke()
        .cloneColumns());
    uistavke.open();
    skstavke.setColumns(hr.restart.baza.dM.getDataModule().getSkstavke()
        .cloneColumns());
    skstavke.open();
    kSk = raKnjizenjeSK.getRaKnjizenjeSK();
  }

  /**
   * Ako ds ima kolone VRDOK, CSKL, STAVKA nalazi pripadajuci konto u shemi knjizenja,
   * ako ga ne nadje i ako ds nema kolonu BROJKONTA vraca prazan string, a ako ima kolonu BROJKONTA
   * vraca vrijednost te kolone 
   * @param ds
   * @return
   */
  public static String getBrojKonta(DataSet ds) {
    String konto = "";
    if (ds.getColumn("STAVKA").getDataType() == Variant.SHORT) {
      konto = getKonto(ds.getString("VRDOK"), ds.getString("CSKL"), Short
          .toString(ds.getShort("STAVKA")));
    } else {
      konto = getKonto(ds.getString("VRDOK"), ds.getString("CSKL"), ds
          .getString("STAVKA"));
    }
    if (konto.equals("") && ds.hasColumn("BROJKONTA")!=null) {
      return ds.getString("BROJKONTA");
    }
    return konto;
  }

  public static String getBrojKonta(String vrdok, String cskl, String stavka) {
    return getKonto(vrdok, cskl, stavka);
  }

  public static void fixMisingCNALOGA() {
    DataSet ds = Gkstavke.getDataModule().getTempSet(
        "cnaloga is null or cnaloga=''");
    ds.open();
    for (ds.first(); ds.inBounds(); ds.next())
      ds.setString("CNALOGA", jpBrojNaloga.getCNaloga(ds));
    ds.saveChanges();
  }

  public static String getKonto(String vrdok, String cskl, String stavka) {
    DataSet shema = hr.restart.baza.dM.getDataModule().getShkonta();
    if (lookupData.getlookupData().raLocate(shema,
        new String[] { "VRDOK", "CSKL", "STAVKA" },
        new String[] { vrdok, cskl, stavka })) {
      return shema.getString("BROJKONTA");
    } else
      return "";
  }

  ///////////////////////
  //
  //
  /**
   * Selektira i otvara novi nalog prema vrijednostima u datasetu (ekranu
   * frmKnjizenje)
   * 
   * @return true ako je uspio, false ako nije
   */
  public boolean startKnjizenje() {
    return startKnjizenje(null);
  }

  private frmKnjizenje fKnjizenje; // za prevaru u isZbirno()

  /**
   * Selektira i otvara novi nalog prema vrijednostima u datasetu (ekranu
   * frmKnjizenje)
   * 
   * @param fknj
   *          ekran za knjizenje iz kojeg ce frmNalozi zvati commitTransfer pri
   *          knjizenju
   * @return true ako je uspio, false ako nije
   */
  public boolean startKnjizenje(frmKnjizenje fknj) {
    try {
      clearTransferInfo();
      kSk.clearErrors();
      fakePreSel();
      //      if (fake) {
      //        getFNalozi().obrada.fKnjizenje = null;
      //      } else {
      getFNalozi().obrada.fKnjizenje = fknj;
      //      }
      fKnjizenje = fknj;
      setProcessMessage("Kreiranje temeljnice ...");
      return newNalog();
    } catch (Exception ex) {
      ex.printStackTrace();
      setErrorMessage(ex.getMessage());
      return false;
    }
  }

  private void fakePreSel() {
    fakePreSel(null);
  }

  private void fillPreSelRow(PreSelect pres) {
    pres.getSelRow().open();
    pres.getSelRow().setString("CVRNAL", dataSet.getString("CVRNAL"));
    pres.getSelRow().setString("STATUS", "");
    pres.getSelRow().setTimestamp("DATUMKNJ-from",
        dataSet.getTimestamp("DATUMKNJ"));
    pres.getSelRow().setTimestamp("DATUMKNJ-to",
        dataSet.getTimestamp("DATUMKNJ"));
    pres.getSelRow().post();
  }

  private void fakePreSel(String cNaloga) {
    fnalozi = frmNalozi.getFrmNalozi();
    PreSelect pres = fnalozi.getPreSelect();
    if (pres == null)
      pres = preSelNalozi.getPreSelect();
    fnalozi.setPreSelect(pres);
    if (cNaloga == null) {
      fillPreSelRow(pres);
      pres.doSelect();
    } else {

      Aus.setFilter(fnalozi.getMasterSet(),
          "SELECT * FROM NALOZI WHERE CNALOGA = '" + cNaloga + "'");
    }
    fnalozi.getMasterSet().open();
    if (cNaloga != null) {
      fnalozi.getMasterSet().first();
      dataSet.setString("CVRNAL", fnalozi.getMasterSet().getString("CVRNAL"));
      dataSet.setTimestamp("DATUMKNJ", fnalozi.getMasterSet().getTimestamp(
          "DATUMKNJ"));
      fillPreSelRow(pres);
    }
  }

  private void insertNewNalog() throws Exception {
    fnalozi = frmNalozi.getFrmNalozi();
    fnalozi.getMasterSet().insertRow(false);
    fnalozi.jpMaster.jpBrNal.noviBrojNaloga(OrgStr.getKNJCORG(), vl
        .findYear(dataSet.getTimestamp("DATUMKNJ")), dataSet
        .getString("CVRNAL"));
    fnalozi.newNalog();
    fnalozi.getMasterSet().setTimestamp("DATUMKNJ",
        dataSet.getTimestamp("DATUMKNJ"));
    fnalozi.getMasterSet().post();
    fnalozi.jpDetail.jpBrNal.initJP(fnalozi.getMasterSet());
    //    fnalozi.refilterDetailSet(); //ovo prije prepisivanja iz stavke_add
    //    fnalozi.getDetailSet().open();
    stavke_add.open();
    stavke_add.empty();
    stavke_addSK.open();
    stavke_addSK.empty();
    originalCNALOGA = fnalozi.getMasterSet().getString("CNALOGA");
  }

  /**
   * Dodaje novi nalog - temeljnicu za zadanu vrstu naloga i datum knjiženja u
   * ekranu i NE RADI saveChanges(). Ako iz bilo kojeg razloga ne uspije lupa
   * poruku. Poziva se automatski pritiskom na OK pa nije preporu\u010Dljivo
   * pozivati (naro\u010Dito ako nije izvršena validacija ekrana). Poziva se u
   * slu\u010Daju da je knjiženjem potrebno napraviti više od jedne temeljnice -
   * naloga.
   * 
   * @return true ako je uspio dodati nalog, false ako mu nije uspjelo i user je
   *         upozoren porukom
   */
  public boolean newNalog() {
    try {
      insertNewNalog();
      return true;
    } catch (Exception ex) {
      //      JOptionPane.showMessageDialog(this,"Dodavanje novog naloga nije
      // uspjelo, ponovite postupak!","Greška",JOptionPane.ERROR_MESSAGE);
      setErrorMessage("Dodavanje novog naloga nije uspjelo! Greška: "
          + ex.getMessage());
      ex.printStackTrace();
      return false;
    }
  }

  private void insertNewStavka() throws Exception {
    fnalozi.jpDetail.jpBrNal.noviBrojStavke_offline(getStavka());
    getStavka().insertRow(false);
    fnalozi.newStavka();
  }

  /**
   * Dodaje novu stavku naloga sa dodijeljenim joj klju\u010Dem
   * 
   * @return true ako je uspio, false ako nije
   */
  private boolean newStavka() {
    if (fnalozi == null)
      return false;
    try {
      insertNewStavka();
      return true;
    } catch (Exception ex) {
      setErrorMessage("Dodavanje nove stavke nije uspjelo! Greška: "
          + ex.getMessage());
      return false;
    }
  }

  private boolean newStavka(String brojkonta, String corg) {
    if (!newStavka())
      return false;
    getStavka().setString("BROJKONTA", brojkonta);
    getStavka().setString("CORG", corg);
    return true;
  }

  /**
   * 
   * @return tekuci slog stavke naloga
   */
  public com.borland.dx.dataset.StorageDataSet getStavka() {
    if (fnalozi == null)
      return null;
    return stavke_add;
  }

  /**
   * 
   * @return tekuci slog stavke za salda konti koje uvijek idu pojedinacno te se
   *         pune paralelno sa getStavka() i poslije se knjize u skstavke i
   *         uistavke
   */
  public com.borland.dx.dataset.StorageDataSet getStavkaSK() {
    //    if (fnalozi == null) return null;
    return stavke_addSK;
  }

  /**
   * Vraca novu stavku naloga sa zadanim kontom i org.jedinicom ako takva ne
   * postoji i ako na tom kontu nije NACPR='Z', ako takva stavka postoji
   * pozicionira i vraca tu stavku
   * 
   * @param brojkonta
   * @param corg
   * @return stavku naloga (gkstavkerad) ili ako je nesto poslo po zlu vraca
   *         null
   */
  public com.borland.dx.dataset.StorageDataSet getNewStavka(String brojkonta,
      String corg) {
    if (fnalozi == null)
      return null;
    //stavka GK
    updatingGKStavka = false;
    com.borland.dx.dataset.StorageDataSet _stav = getStavka();
    zbirno = isZbirno(brojkonta) > 0;
    //raKonta.initRaKonta(hr.restart.baza.dM.getDataModule().getKonta());
    if (!raKonta.isOrgStr(brojkonta)) {
      corg = OrgStr.getKNJCORG();
    }
    if (fKnjizenje != null && getFNalozi().getMasterSet().
        getString("CVRNAL").equals("00") && !raKonta.isOrgStrPS(brojkonta))
      corg = OrgStr.getKNJCORG();

    if (zbirno) {
      if (!lookupData.getlookupData().raLocate(_stav,
          new String[] { "BROJKONTA", "CORG" },
          new String[] { brojkonta, corg })) {
        if (!newStavka(brojkonta, corg))
          return null;
      } else {
        fnalozi.loadStavka(getStavka());
        fnalozi.jpDetail.jpBrNal.initJP(getStavka().getInt("RBS"));
        updatingGKStavka = true;
        updatingOpis = getStavka().getString("OPIS");
        updatingCpar = getStavka().getInt("CPAR");
      }
    } else {
      if (!newStavka(brojkonta, corg))
        return null;
    }
    //    return _stav;
    //    pojedinacna stavka (SK)
    getStavkaSK().insertRow(false);
    /*
     * _stav.copyTo(getStavkaSK()); getStavkaSK().setBigDecimal("ID",nula);
     * getStavkaSK().setBigDecimal("IP",nula);
     * getStavkaSK().setBigDecimal("DEVID",nula);
     * getStavkaSK().setBigDecimal("DEVIP",nula);
     * getStavkaSK().setBigDecimal("SALDO",nula);
     */
    //
    return getStavkaSK();
  }

  /**
   * by andrej, comments by ab.f
   * Flag koji se ukljucuje kad se dodaje stavka na konto
   * koje se prenosi zbirno, oznacava da ce se stavka
   * dodati onoj koja vec postoji za taj konto.
   */
  private boolean updatingGKStavka = false;
  
  /**
   * Varijable za cuvanje prve vrijednosti opisa i
   * partnera na zbirnom kontu. Ako se pojavi drugaciji
   * opis ili drugaciji partner, onda se na kumulativnoj
   * stavci te informacije ponistavaju (zahtjev by Tamara) 
   */
  private String updatingOpis;
  private int updatingCpar;

  /**
   * Daje informaciju da li se konto zadnje (tekuce) stavke prenosi zbirno ili
   * pojedinacno u gk. Podobno za setiranje opisa i sl.
   * 
   * @return
   */
  public boolean isLastKontoZbirni() {
    return zbirno;
  }

  /**
   * return getNewStavka(getBrojKonta(ds),ds.getString("CORG"));
   * 
   * @param ds
   * @return
   */
  public com.borland.dx.dataset.StorageDataSet getNewStavka(DataSet ds) {
    return getNewStavka(getBrojKonta(ds), ds.getString("CORG"));
  }

  /**
   * return getNewStavka(getBrojKonta(ds),corg);
   * 
   * @param ds
   * @param corg
   * @return
   */
  public com.borland.dx.dataset.StorageDataSet getNewStavka(DataSet ds,
      String corg) {
    return getNewStavka(getBrojKonta(ds), corg);
  }

  /**
   * getStavka().setBigDecimal("ID",getStavka().getBigDecimal("ID").add(id));
   * 
   * @param id
   */
  public void setID(BigDecimal id) {
    getStavka().setBigDecimal("ID", getStavka().getBigDecimal("ID").add(id));
    getStavkaSK().setBigDecimal("ID", id);
  }

  /**
   * getStavka().setBigDecimal("IP",getStavka().getBigDecimal("IP").add(ip));
   * 
   * @param ip
   */
  public void setIP(BigDecimal ip) {
    getStavka().setBigDecimal("IP", getStavka().getBigDecimal("IP").add(ip));
    getStavkaSK().setBigDecimal("IP", ip);
  }

  public static boolean isEmpty(String colName, ReadRow ds) {
    Object val = hr.restart.db.raVariant.getDataSetValue(ds, colName);
    Column col = ds.getColumn(colName);
    return Valid.getValid().chkIsEmpty(col, val.toString());
  }

  private boolean isEmpty(String colName) {
    return isEmpty(colName, getStavka());
  }

  private boolean isEmptySK(String colName) {
    return isEmpty(colName, getStavkaSK());
  }

  private String[] copyColsSK = new String[] { "DATDOK", "BROJDOK", "DATDOSP", "OPIS", "CPAR"};
  private String[] allColsSK;

  private String toStringStavka() {
    String tss = " konto = " + getStavka().getString("BROJKONTA") + " opis = "
        + getStavka().getString("OPIS") + " org.jed = "
        + getStavka().getString("CORG") + " datum dok. = "
        + new java.sql.Date(getStavka().getTimestamp("DATDOK").getTime())
        + " duguje = "
        + getStavka().getBigDecimal("ID").setScale(2, BigDecimal.ROUND_HALF_UP)
        + " potražuje = "
        + getStavka().getBigDecimal("IP").setScale(2, BigDecimal.ROUND_HALF_UP)
        + " partner = " + getStavka().getInt("CPAR") + " broj dokumenta = "
        + getStavka().getString("BROJDOK") + " datum dosp. = "
        + new java.sql.Date(getStavka().getTimestamp("DATDOSP").getTime());
    return tss;
  }

  private void validacija() throws Exception {
    //copy from getStavkaSK() and obrnuto
    getStavkaSK().setString("BROJKONTA", getStavka().getString("BROJKONTA"));
    getStavkaSK().setString("CORG", getStavka().getString("CORG"));
    getStavkaSK().setTimestamp("DATUMKNJ", dataSet.getTimestamp("DATUMKNJ"));
    if (updatingGKStavka) {
      ReadRow.copyTo(copyColsSK, getStavkaSK(), copyColsSK, getStavka());
      if (getStavka().getString("OPIS").length() == 0 || 
          !getStavka().getString("OPIS").equals(updatingOpis)) {
        getStavka().setString("OPIS", updatingOpis = "Zbirni prijenos");
      }
      if (getStavka().getInt("CPAR") != updatingCpar)
        getStavka().setInt("CPAR", updatingCpar = 0);
    } else {
      ReadRow.copyTo(allColsSK, getStavkaSK(), allColsSK, getStavka());
      //      getStavkaSK().copyTo(getStavka());
    }
    //
    if (isEmpty("BROJKONTA"))
      throw new Exception("Nije ispravan broj konta!");
    if (isEmpty("OPIS"))
      throw new Exception("Opis mora biti upisan!");
    if (isEmpty("CORG"))
      throw new Exception("Org. jedinica mora biti upisana!");
    if (isEmpty("DATDOK"))
      throw new Exception("Nije ispravan datum dokumenta!");
    if (!isLastKontoZbirni() && isEmpty("ID") && isEmpty("IP"))
      throw new Exception("I dugovni i potrazni iznos su 0!");
    if (raKonta.isSaldak(getStavka().getString("BROJKONTA")) && isSKRacKnj()) {
      hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
      if (!lookupData.getlookupData().raLocate(dm.getPartneri(),
          new String[] { "CPAR" },
          new String[] { Integer.toString(getStavkaSK().getInt("CPAR")) }))
        throw new Exception("Nepostojeci partner!");
      if (isEmptySK("BROJDOK"))
        throw new Exception("Nije upisan broj dokumenta!");
      if (isEmptySK("DATDOSP"))
        throw new Exception("Nije upisan datum dospijeca!");
      /**
       * @todo odrediti vrstu dokumenta za skracune if (isEmpty("VRDOK"))
       *       getStavka().setString("VRDOK","KRN");//HC! Racun salda konti if
       *       (isEmpty("CKNJIGE")) getStavka().setString("CKNJIGE","A"); //HC!
       *       Knjiga A -
       */
    }
  }

  private void postStavka() throws Exception {
    fnalozi.prepareForSaveStavka(getStavka());
    getStavka().post();
    fnalozi.updStavka(getStavka(), false);
    getStavkaSK().post();
  }

  public void setErrorMessage(String msg) {
    errMsg = msg;
  }

  public String getErrorMessage() {
    return errMsg;
  }

  /**
   * Nakon punjenja jednog sloga stavke potrebno je pozvati ovu metodu koja
   * posta stavku i azurira totale zaglavlja naloga
   * 
   * @return true ako je uspio, false ako nije
   */
  public boolean saveStavka() {
    cGK = saveStavkaGetCGKStavke();
    if (cGK == null)
      return false;
    // add u listu
    if (infoKeys != null && transferSet != null)
      addTransferInfo(cGK);
    return true;
  }

  private boolean markNotForGK = false;
  /**
   * Ako je fixVrdok na false onda nece dodijeljivati vrdok u addToSK
   */
  public boolean fixVrdok = true;

  public String saveStavkaGetCGKStavke() {
    if (fnalozi == null)
      return null;
    try {
      try {
        validacija();
      } catch (Exception ex) {
        kSk.errMessage(ex.getMessage() + " stavka: " + toStringStavka());
      }
      String cgkstavke = null;
      int local_cnalogaMode;
      if (cnalogaMode == CNALMODE_KONTO_NACPR) {
        local_cnalogaMode = isLastKontoZbirni() ? CNALMODE : CNALMODE_RBS;
      } else {
        local_cnalogaMode = cnalogaMode;
      }
      if (local_cnalogaMode == CNALMODE_RBS) {
        //vidi raSaldaKonti.copyStavka(
        cgkstavke = fnalozi.jpMaster.jpBrNal.getCNaloga() + "-"
            + fnalozi.jpDetail.jpBrNal.rbs;
      } else if (local_cnalogaMode == CNALMODE) {
        cgkstavke = fnalozi.jpMaster.jpBrNal.getCNaloga();
      } else if (local_cnalogaMode == CNALMODE_8) {
        String god = fnalozi.jpMaster.jpBrNal.god.substring(2, 4);
        String vrnal = fnalozi.jpMaster.jpBrNal.cvrnal;
        String rbr = vl.maskZeroInteger(new Integer(
            fnalozi.jpMaster.jpBrNal.rbr), 4);
        cgkstavke = god.concat(vrnal).concat(rbr);
      }
      if (getStavkaSK().hasColumn("CGKSTAVKE") != null)
        getStavkaSK().setString("CGKSTAVKE", 
            fnalozi.jpMaster.jpBrNal.getCNaloga() + "-"
            + fnalozi.jpDetail.jpBrNal.rbs);
      postStavka();
      return cgkstavke;
    } catch (Exception ex) {
      setErrorMessage("Ažuriranje stavke nije uspjelo! Greška: "
          + ex.getMessage());
      return null;
    }
  }

  private void saveChanges() throws Exception {
    //    fnalozi.getMasterSet().saveChanges();
    //    fnalozi.getDetailSet().saveChanges();
    fnalozi.refilterDetailSet(); //ovo prije prepisivanja iz stavke_add
    fnalozi.getDetailSet().open();
    com.borland.dx.sql.dataset.QueryDataSet gkstavke_tem = fnalozi
        .getDetailSet();
    //sysoutTEST ST = new sysoutTEST(false);
    //ST.showInFrame(getStavkaSK(),"Stavke za SK");
    //ST.showInFrame(stavke_add,"Stavke za GK");
    if (stavke_add.getRowCount() > 0) {
      fnalozi.raDetail.getJpTableView().enableEvents(false);
      try {
        stavke_add.first();
        do {
          gkstavke_tem.insertRow(false);
          //        stavke_add.copyTo(gkstavke_tem);
          ReadRow.copyTo(allColsSK, stavke_add, allColsSK, gkstavke_tem);
          gkstavke_tem.post();
        } while (stavke_add.next());
      } finally {
        fnalozi.raDetail.getJpTableView().enableEvents(true);
      }
    }
    if (isSKRacKnj()) {
      addToSK();
      if (!checkAddingSk())
        throw new Exception("Prijenos za SK nije uspio");
    }
    //ST.showInFrame(fnalozi.getMasterSet(),"Nalozi");
    //ST.showInFrame(fnalozi.getDetailSet(),"Stavke naloga");
    raTransaction.saveChanges(fnalozi.getMasterSet());
    raTransaction.saveChanges(fnalozi.getDetailSet());
  }

  private void setProcessMessage(String msg) {
    frmKnjizenje fknj = getFNalozi().obrada.fKnjizenje;
    if (fknj == null)
      return;
    fknj.setProcessMessage(msg);
  }
  
  private static class SkData {
    BigDecimal skid, skip, uiid, uiip;
    String csk;
    public SkData(DataSet sk) {
      csk = sk.getString("CSKSTAVKE");
      skid = sk.getBigDecimal("ID");
      skip = sk.getBigDecimal("IP");
      uiid = uiip = Aus.zero2;
    }
  }

  private boolean checkAddingSk() {
    if (skstavke.getRowCount() == 0)
      return true;
    if (uistavke.getRowCount() == 0)
      return true;
    boolean checkRes = true;
    HashMap cmap = new HashMap();
    setProcessMessage("Provjera zaglavlja SK ...");
    skstavke.first();
    do {
      String skey = getSKCheckKey(skstavke);
      Object sval = cmap.get(skey);
      if (sval != null) {
        kSk
            .errMessage("Postoje dva dokumenta SK (raèuna) sa istim partnerom i brojem : "
                + skey.toString());
        checkRes = false;
      } else {
        //cmap.put(skey, new BigDecimal[] { skstavke.getBigDecimal("ID"),
        //    skstavke.getBigDecimal("IP"), nula, nula });
        cmap.put(skey, new SkData(skstavke));
      }
    } while (skstavke.next());
    setProcessMessage("Provjera stavaka SK ...");
    uistavke.first();
    do {
      String skey = getSKCheckKey(uistavke);
      SkData sval = (SkData) cmap.get(skey);
      if (sval == null) {
        //error: uistavke bez skstavke
        kSk.errMessage("Postoje stavke dokumenta SK (raèuna) bez zaglavlja : "
            + skey.toString());
        checkRes = false;
      } else {
        try {
          /*BigDecimal[] skiznosi = (BigDecimal[]) sval;
          skiznosi[2] = skiznosi[2].add(uistavke.getBigDecimal("ID"));
          skiznosi[3] = skiznosi[3].add(uistavke.getBigDecimal("IP"));
          cmap.put(skey, skiznosi);*/
          uistavke.setString("CSKSTAVKE", sval.csk);
          sval.uiid = sval.uiid.add(uistavke.getBigDecimal("ID"));
          sval.uiip = sval.uiip.add(uistavke.getBigDecimal("IP"));
        } catch (Exception ex) {
          ex.printStackTrace();
          //error:
          kSk.errMessage(ex.getMessage() + " : " + skey.toString());
          checkRes = false;
        }
      }
    } while (uistavke.next());
    setProcessMessage("Provjera balansa SK dokumenata ...");
    Set keyset = cmap.keySet();
    for (Iterator i = keyset.iterator(); i.hasNext();) {
      try {
        String skey = (String) i.next();
        SkData sval = (SkData) cmap.get(skey);
        sval.skid = sval.skid.setScale(2, BigDecimal.ROUND_HALF_UP);
        sval.skip = sval.skip.setScale(2, BigDecimal.ROUND_HALF_UP);
        sval.uiid = sval.uiid.setScale(2, BigDecimal.ROUND_HALF_UP);
        sval.uiip = sval.uiip.setScale(2, BigDecimal.ROUND_HALF_UP);
        if ((sval.skid.add(sval.skip).compareTo(sval.uiid) != 0 ||
            sval.uiid.compareTo(sval.uiip) != 0) && (sval.uiid.signum()!=0 || sval.uiip.signum()!=0)) {//ako su oba nula - nema stavaka - vjerojatno protustavka koja i tako ne ide u knjigu
/*        BigDecimal[] sval = (BigDecimal[]) cmap.get(skey);
        to2dec(sval);
        if (sval[0].add(sval[1]).compareTo(sval[2]) == 0
            && sval[0].add(sval[1]).compareTo(sval[3]) == 0
            && sval[2].compareTo(sval[3]) == 0) {
          //sve pet!
        } else {*/
          kSk.errMessage("Dokument SK " + skey.toString() + " nije u balansu; "
              + "Iznos na zaglavlju = " + sval.skid.add(sval.skip)
              + "; Dugovni iznos stavaka = " + sval.uiid
              + "; Potražni iznos stavaka = " + sval.uiip);
          checkRes = false;
        }
      } catch (Exception ex) {
        ex.printStackTrace();
        kSk.errMessage("Greška pri provjeri iznosa za SK : " + ex.getMessage());
        checkRes = false;
      }
    }
    return checkRes;
  }

  /*private void to2dec(BigDecimal[] sval) {
    for (int i = 0; i < sval.length; i++) {
      sval[i] = Util.getUtil().setScale(sval[i],2);
    }
    
  }*/

  private void cancelChanges() {
    fnalozi.getMasterSet().refresh();
    fnalozi.getDetailSet().refresh();
  }

  private void addToSK() {
    skstavke.empty();
    uistavke.empty();
    getStavkaSK().setSort(new SortDescriptor(new String[] {"BROJDOK", "VRDOK", "CPAR"}));
    String last = "";
    boolean glavna = false, err = false;
    boolean godob = frmNalozi.getFrmNalozi().getMasterSet().getString("CVRNAL").equals("00");
//    int saldaks = 0;
    Set haveGlavna = new HashSet();
//    Set noSk = new HashSet();
    Set saldakCache = new HashSet();
    Set nsalCache = new HashSet();
    Set okCache = new HashSet();
    
    for (getStavkaSK().first(); getStavkaSK().inBounds(); getStavkaSK().next()) {
      String now = getSKCheckKey(getStavkaSK());
      if (!isForSK()) continue;
      
      String konto = getStavkaSK().getString("BROJKONTA");
      if (!saldakCache.contains(konto) && !nsalCache.contains(konto))
        if (raKonta.isSaldak(konto))
          saldakCache.add(konto);
        else nsalCache.add(konto);
      
      boolean okajac = (getStavkaSK().getBigDecimal("ID").add(
          getStavkaSK().getBigDecimal("IP")).compareTo(nula) < 0);
      
      if (!now.equals(last)) {
        if (last.length() > 0 && glavna) haveGlavna.add(last);
        //if (last.length() > 0 && !glavna && saldaks == 0) noSk.add(last);
        last = now;
//        saldaks = 0;
        glavna = false;
      }
      if (getStavkaSK().getString("GLAVNA").equalsIgnoreCase("D")) {
        if (glavna) {
          err = true;
          kSk.errMessage("Postoje dvije stavke oznacene kao glavne za dokument: " + now);
        }
        glavna = true;
        if (okajac) okCache.add(now);
      }
      if (saldakCache.contains(konto) && okajac) okCache.add(now);
      //if (raKonta.isSaldak(getStavkaSK().getString("BROJKONTA"))) ++saldaks;
    }
    if (err) throw new RuntimeException("Greška u prijenosu SK!");
    if (last.length() > 0 && glavna) haveGlavna.add(last);
    //if (last.length() > 0 && !glavna && saldaks == 0) noSk.add(last);

    int rbui = 0, rbsk = 0;
    last = "";
    getStavkaSK().first();
    do {
      if (isForSK()) {
        String now = getSKCheckKey(getStavkaSK());        
        if (!now.equals(last)) {
          rbui = 1;
          rbsk =  haveGlavna.contains(now) ? 1 : 0;
          last = now;
        }
        
        String brojdok = getStavkaSK().getString("BROJDOK");
        String brojkonta = getStavkaSK().getString("BROJKONTA");
        //int cpar = getStavkaSK().getInt("CPAR");
        String cknjige = getStavkaSK().getString("CKNJIGE");
        short ckolone = getStavkaSK().getShort("CKOLONE");
        String vrdok = getStavkaSK().getString("VRDOK");
        boolean ulazni = getStavkaSK().getString("URAIRA").equals("U");
        if (!godob && fixVrdok ) {//ne talasaj 
          // razrada za okajce (iznos < 0)
          boolean okajac = okCache.contains(now);
          
          if (okajac) {
            vrdok = ulazni ? "OKD" : "OKK";
          } else {
            vrdok = ulazni ? "URN" : "IRN";
          }
        }
        //  dodati polje u getStavkaSK(), flag je li rijec o skstavci ako nije konto saldak
        // i prekontrolirati. Brojati koliko ima skstavki po ovom dokumentu:
        // ako ima 1, sve ok. Ako ima vise od jednog, drugi moraju promijeniti brojdok
        // ako ima 0, hmm, znaci da ona spika sa flagom nije prosla. Onda treba
        // izmisliti skstavku, iz jedne od prethodnih uistavaka.
        glavna = getStavkaSK().getString("GLAVNA").equalsIgnoreCase("D");
        boolean isskstavka = glavna || saldakCache.contains(brojkonta) /*||
           (noSk.contains(now) && rbsk == 0)*/;
        if (isskstavka) {
          //-> u skstavke          
          skstavke.insertRow(false);
          skstavke.setString("KNJIG", OrgStr.getKNJCORG());
          skstavke.setInt("CPAR", getStavkaSK().getInt("CPAR"));
          skstavke.setString("BROJKONTA", brojkonta);
          skstavke.setShort("STAVKA", getStavShemeForSK(vrdok, ckolone));
          
          if (getStavkaSK().isNull("CSKL"))
            skstavke.setString("CSKL", hr.restart.baza.dM.getDataModule()
                .getShkonta().getString("CSKL"));
          else skstavke.setString("CSKL", getStavkaSK().getString("CSKL"));
          
          skstavke.setString("VRDOK", vrdok);
          if (godob || glavna || ++rbsk == 1) {
            skstavke.setString("BROJDOK", brojdok);
            if (godob) skstavke.setString("EXTBRDOK", getStavkaSK().getString("EXTBRDOK"));
          } else {
            System.out.println("sranje kod "+brojdok);
            // ovo drugo su obavezno okajci jer mogu bit na svakakvim stranama konta
            skstavke.setString("VRDOK", ulazni ? "OKD" : "OKK");
            skstavke.setString("BROJDOK", brojdok + "-" + rbsk);
          }
//          skstavke.setInt("BROJIZV", godob ? getStavkaSK().getInt("BROJIZV") : 0);
          skstavke.setInt("BROJIZV", getStavkaSK().getInt("BROJIZV"));
          skstavke.setString("CORG", getStavkaSK().getString("CORG"));
          skstavke.setTimestamp("DATUMKNJ", getStavkaSK().getTimestamp(
              "DATUMKNJ"));
          skstavke.setTimestamp("DATDOK", getStavkaSK().getTimestamp("DATDOK"));
          skstavke.setTimestamp("DATDOSP", getStavkaSK()
              .getTimestamp("DATDOSP"));
          if (godob || (getStavkaSK().getString("OZNVAL") != null &&
              getStavkaSK().getString("OZNVAL").length() > 0)) {
            skstavke.setString("OZNVAL", getStavkaSK().getString("OZNVAL"));
            skstavke.setBigDecimal("TECAJ", getStavkaSK().getBigDecimal("TECAJ"));
          } else {
            skstavke.setString("OZNVAL", Tecajevi.getDomOZNVAL());
            skstavke.setBigDecimal("TECAJ", new BigDecimal(1.00));
          }
          skstavke.setString("OPIS", getStavkaSK().getString("OPIS"));
          skstavke.setBigDecimal("ID", getStavkaSK().getBigDecimal("ID"));
          skstavke.setBigDecimal("IP", getStavkaSK().getBigDecimal("IP"));
          // Postavi saldo
          skstavke.setBigDecimal("SALDO", skstavke.getBigDecimal("ID").add(
              skstavke.getBigDecimal("IP")));
          skstavke.setBigDecimal("SSALDO", skstavke.getBigDecimal("SALDO"));
          if (godob || (getStavkaSK().getString("OZNVAL") != null &&
              getStavkaSK().getString("OZNVAL").length() > 0)) {
            skstavke.setBigDecimal("PVID", getStavkaSK().getBigDecimal("DEVID"));
            skstavke.setBigDecimal("PVIP", getStavkaSK().getBigDecimal("DEVIP"));
          } else {
            skstavke.setBigDecimal("PVID", skstavke.getBigDecimal("ID"));
            skstavke.setBigDecimal("PVIP", skstavke.getBigDecimal("IP"));
          }
          skstavke.setBigDecimal("PVSALDO", skstavke.getBigDecimal("PVID").add(
              skstavke.getBigDecimal("PVIP")));
          skstavke.setBigDecimal("PVSSALDO", skstavke.getBigDecimal("PVSALDO"));
          skstavke.setString("CKNJIGE", cknjige);
          
          if (getStavkaSK().isNull("DATPRI") && !godob)
            skstavke.setTimestamp("DATPRI", getStavkaSK()
               .getTimestamp("DATUMKNJ"));
          else skstavke.setTimestamp("DATPRI", getStavkaSK()
              .getTimestamp("DATPRI"));
          
          skstavke.setTimestamp("DATUNOS", Valid.getValid().getToday());
          skstavke.setString("CGKSTAVKE", getStavkaSK().getString("CGKSTAVKE"));
          skstavke.setString("CSKSTAVKE", hr.restart.sk.raSaldaKonti
              .findCSK(skstavke));
          
          skstavke.post();          
        }
        // -> uistavke
        if (cknjige.length() > 0 && !godob) {
        int uirbs = isskstavka && (glavna || rbsk == 1) ? 1 : ++rbui;
            /*getUIRbs(getStavkaSK().getInt("CPAR"),
            vrdok, uistavke);*/
        String dugpot = getStavkaSK().getBigDecimal("ID").setScale(2,
            BigDecimal.ROUND_HALF_UP).compareTo(nula) == 0 ? "P" : "D";
        uistavke.insertRow(false);
        uistavke.setString("KNJIG", OrgStr.getKNJCORG());
        uistavke.setInt("CPAR", getStavkaSK().getInt("CPAR"));
        uistavke.setShort("STAVKA", getStavShemeForSK(vrdok, ckolone));
        uistavke.setString("CSKL", hr.restart.baza.dM.getDataModule()
            .getShkonta().getString("CSKL"));
        uistavke.setString("VRDOK", vrdok);
        uistavke.setString("BROJDOK", brojdok);
        uistavke.setString("BROJKONTA", brojkonta);
        uistavke.setInt("RBS", uirbs);
        uistavke.setString("CORG", getStavkaSK().getString("CORG"));
        uistavke.setBigDecimal("ID", getStavkaSK().getBigDecimal("ID"));
        uistavke.setBigDecimal("IP", getStavkaSK().getBigDecimal("IP"));
        uistavke.setString("CKNJIGE", cknjige);
        uistavke.setShort("CKOLONE", ckolone);
        uistavke.setString("URAIRA", getStavkaSK().getString("URAIRA"));
        uistavke.setString("DUGPOT", dugpot);
        uistavke.post();
        }
      }
    } while (getStavkaSK().next());
    //    sysoutTEST ST = new sysoutTEST(false);
    //    ST.showInFrame(uistavke,"Ui stafke");
    //    ST.showInFrame(skstavke,"Sk stafke");
  }

  private int getUIRbs(int _cpar, String _vrdok, DataSet _uistavke) {
    int uirbs = 1;
    Variant vuirbs = new Variant();
    Variant vvrdok = new Variant();
    Variant vcpar = new Variant();
    for (int i = 0; i < _uistavke.getRowCount(); i++) {
      _uistavke.getVariant("VRDOK", i, vvrdok);
      _uistavke.getVariant("CPAR", i, vcpar);
      _uistavke.getVariant("RBS", i, vuirbs);
      if (vvrdok.toString().equals(_vrdok) && vcpar.getInt() == _cpar
          && vuirbs.getInt() >= uirbs) {
        uirbs = vuirbs.getInt() + 1;
      }
    }
    return uirbs;
  }

  private short getStavShemeForSK(String _vrdok, short _ckolone) {
    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    dm.getShkonta().open();
    String[] kys = new String[] { _vrdok, _ckolone + "" };
    boolean succ;
    succ = lookupData.getlookupData().raLocate(dm.getShkonta(),
        new String[] { "VRDOK", "CKOLONE" }, kys);
    if (!succ)
      succ = lookupData.getlookupData().raLocate(dm.getShkonta(),
          new String[] { "VRDOK", "POLJE" }, kys);
    if (!succ)
      succ = lookupData.getlookupData().raLocate(dm.getShkonta(),
          new String[] { "VRDOK" }, new String[] { _vrdok });
    if (!succ)
      return Short.parseShort("0");
    return dm.getShkonta().getShort("STAVKA");
  }

  private boolean isForSK() {
    if (getStavkaSK().getString("BROJDOK") == null)
      return false;
    if (getStavkaSK().getString("BROJDOK").trim().equals(""))
      return false;
    if (getStavkaSK().getString("VRDOK").trim().equals("NK!"))
      return false;
    return true;
  }

  /**
   * Na kraju cijele obrade poziva se ova metoda koja konacno radi saveChanges
   * naloga i stavaka ako ne uspije radi refresh sto znaci da je sve vraceno na
   * pocetak
   * 
   * @return true ako je uspio, false ako nije
   */
  public boolean saveAll() {
    try {
      if (kSk.hasErrors()) {
        setErrorMessage("Greška pri izradi temeljnice");
        return false;
      }
      if (fake) {
        return true;
      }
      setProcessMessage("Snimanje temeljnice ...");
      String _cnaloga = fnalozi.getMasterSet().getString("CNALOGA");
      //status
      if (fnalozi.getMasterSet().getBigDecimal("ID").setScale(2,
          BigDecimal.ROUND_HALF_UP).compareTo(
          fnalozi.getMasterSet().getBigDecimal("IP").setScale(2,
              BigDecimal.ROUND_HALF_UP)) == 0) {
        fnalozi.getMasterSet().setBigDecimal("KONTRIZNOS",
            fnalozi.getMasterSet().getBigDecimal("ID"));
        fnalozi.getMasterSet().setString("STATUS", "S");
      }
      saveChanges();
      fakePreSel(_cnaloga);
      fnalozi = null;
      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
      setErrorMessage("Snimanje promjena u bazu nije uspjelo! Greška: "
          + ex.getMessage());
      cancelChanges();
      return false;
    }
  }

  /**
   * Povecava progresbar za jednu crticu
   * 
   * @param s
   *          poruka za statusbar, ako je null ne mijenja se poruka nego se samo
   *          povecava progres
   */
  public void nextStep(String msg) {
    invokeClientNextStep(msg);
  }

  private boolean client = true;

  private void invokeClientNextStep(String s) {
    // trebao bi poslati datom clientu zapovjed da poveca jebeni progressbar
    // za sada smo na clientu:
    if (client) {
      new statusHandler().next(s);
    } else {
      // server salje clientu porukicu
    }
  }

  //
  //
  //metode za izvode i ostala paralelna knjizenja
  public frmNalozi getFNalozi() {
    return fnalozi;
  }

  public boolean posNalozi(String cnaloga) {
    fakePreSel(cnaloga); //nemam datuma knjiženja
    fnalozi.getMasterSet().first();
    return (fnalozi.getMasterSet().rowCount() > 0);
  }

  /**
   * Flag za kumuliranje
   * 
   * @param brojKonta
   * @return ako nije nista upisano (pretocena konta od nekud) ili ako je
   *         konta.NACPR != "Z" vraca 0, ako nije nasao konto vraca -1, ako je
   *         konta.NACPR == "Z" vraca 1
   */
  public int isZbirno(String brojKonta) {
    if (fKnjizenje != null) {
      //ako je pocetno stanje OBAVEZNO POJEDINACNO
      if (getFNalozi().getMasterSet().getString("CVRNAL").equals("00")) return 0;
      if (fKnjizenje.dataSet.getString("NK").equals("Z")) return 1;

      if (fKnjizenje.dataSet.getString("NK").equals("P")) return 0;

    }
    int zbr = -1;
    try {
      zbr = raKonta.isZbirni(brojKonta) ? 1 : 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
/*    StorageDataSet kontaSet = getKontaSet(brojKonta);
    if (kontaSet != null) {
      zbr = (kontaSet.getString("NACPR").equals("Z")) ? 1 : 0;
    }*/
    return zbr;
  }

  /**
   * locira i radi hr.restart.zapod.raKonta.initRaKonta(dm.getKonta()) na
   * dm.getKonta();
   * 
   * @param brojKonta
   *          broj konta
   * @return vraca dm.getKonta() sa pointerom na slog sa zadanim brojem konta,
   *         ako takav slog ne postoji vraca null
   */
/*  public StorageDataSet getKontaSet(String brojKonta) {
    com.borland.dx.dataset.StorageDataSet kontaSet = hr.restart.baza.dM
        .getDataModule().getKonta();
    if (lookupData.getlookupData().raLocate(kontaSet,
        new String[] { "BROJKONTA" }, new String[] { brojKonta })) {
      hr.restart.zapod.raKonta.initRaKonta(kontaSet);
      return kontaSet;
    } else
      return null;
  }
*/
  /**
   * Prije ove metode (zove se automatski u saveStavka()) potrebno je podesiti:
   * 
   * <pre>
   * 
   *  setTransferSet(DataSet) - dataset iz kojeg se vrsi transfer u gk i na kojem treba azurirati cnaloga
   *  setInfoKeys(String[]) - kljucevi po kojima se locira transferset da bi mu se azurirao cnaloga
   *  setInfColName(String) - column name u transfersetu u koji se upisuje cnaloga default = &quot;CNALOGA&quot;
   *  
   * </pre>
   * 
   * @param cgk
   */
  public void addTransferInfo(String cgk) {
    addTransferInfo(cgk, null);
  }

  public void setTransferInfoAddingEnabled(boolean enab) {
    transferInfoAddingEnabled = enab;
  }

  public void addTransferInfo(String cgk, String[] _vals) {
    if (!transferInfoAddingEnabled)
      return;
    if (infoKeys == null)
      throw new NullPointerException("infoKeys must be set");
    if (transferSet == null)
      throw new NullPointerException("transferSet must be set");
    TransferInfo nfo;
    if (_vals != null)
      nfo = new TransferInfo(infoKeys, _vals, transferSet, infColName);
    else
      nfo = new TransferInfo(infoKeys, transferSet, infColName);
    HashSet infoList = (HashSet) azurList.get(cgk); //TransferInfo x n
    if (infoList == null)
      infoList = new HashSet();
    infoList.add(nfo);
    azurList.put(cgk, infoList);
  }

  public void clearTransferInfo() {
    setTransferSet(null);
    setInfoKeys(null);
    setInfColName("CNALOGA");
    azurList.clear();
  }

  public boolean commitTransfer() {
    //prvo prebaciti u sk
    if (!commitTransferSK())
      return false;
    //azuriranje promjena
    HashSet azursets = new HashSet();
    Set kset = azurList.keySet();
    for (Iterator i = kset.iterator(); i.hasNext();) {
      Object ky = i.next();
      HashSet infoList = (HashSet) azurList.get(ky);
      if (infoList != null) {
        for (Iterator i2 = infoList.iterator(); i2.hasNext();) {
          TransferInfo nfo = (TransferInfo) i2.next();
          boolean up = nfo.update((String) ky);
          if (nfo.azurSet instanceof com.borland.dx.sql.dataset.QueryDataSet)
            azursets.add(nfo.azurSet);
        }
      }
    }
    if (azursets.size() > 0) {
      com.borland.dx.sql.dataset.QueryDataSet[] qdss = new com.borland.dx.sql.dataset.QueryDataSet[azursets
          .size()];
      qdss = (com.borland.dx.sql.dataset.QueryDataSet[]) azursets.toArray(qdss);
      try {
        //ako nije unutar transakcije a je
        // raTransaction.saveChangesInTransaction(qdss);
        //ako je..
        for (int i = 0; i < qdss.length; i++) {
          QueryDataSet set = qdss[i];
          raTransaction.saveChanges(set);
        }
        
      } catch (Exception ex) {
        ex.printStackTrace();
        return false;
      }
    }
    return true;
  }

  /**
   * Metoda za prebacivanje temeljnice u modul salda konti, zove se na pocetku
   * metode commitTransfer() koju poziva frmKnjizenje.commitTransfer. Dakle, ako
   * overridas commitTransfer OBAVEZNO POZOVI OVU METODU prije svojeg koda
   * 
   * @return true ako je sve proslo OK, ako vrati false abortira operaciju i
   *         javlja da nije uspjelo
   */
  public boolean commitTransferSK() {
    if (!isSKRacKnj())
      return true;
    if (skstavke == null)
      return true;
    if (uistavke == null)
      return true;
    if (skstavke.getRowCount() == 0)
      return true;
    try {
      final raPreparedStatement addui = new raPreparedStatement("uistavke",
          raPreparedStatement.INSERT);
      final raPreparedStatement addsk = new raPreparedStatement("skstavke",
          raPreparedStatement.INSERT);
/* standalone transakcija
      boolean b = new raLocalTransaction() {
        public boolean transaction() throws Exception {
          return commitTransTransaction(addsk, addui);
        }
      }.execTransaction();
*/
      /* unutar neke transakcije */
      boolean b = commitTransTransaction(addsk, addui);
      /**/
      /*if (!b)
        hr.restart.sk.raSaldaKonti.setKumInvalid();*/
      return b;
    } catch (Exception ex) {
      ex.printStackTrace();
      //hr.restart.sk.raSaldaKonti.setKumInvalid();
      return false;
    }
  }
  
  private void fillInitialMap(Map extbrdoks, boolean kup) {
    
    Condition cond = Aus.getKnjigCond().and(Aus.getVrdokCond(kup, true)).
       and(Aus.getYearCond("DATUMKNJ", Util.getUtil().getYear(
           dataSet.getTimestamp("DATUMKNJ"))));
    
    DataSet sk = Skstavke.getDataModule().getTempSet("CKNJIGE EXTBRDOK", cond);
    sk.open();
    String prefix = kup ? "I-" : "U-";
    for (sk.first(); sk.inBounds(); sk.next()) {
      String key = bookDependant ? prefix + sk.getString("CKNJIGE") : prefix;
      int num = Aus.getNumber(sk.getString("EXTBRDOK"));
      if (!extbrdoks.containsKey(key) ||
          ((Integer) extbrdoks.get(key)).intValue() < num)
        extbrdoks.put(key, new Integer(num));
    }
    
    cond = Aus.getKnjigCond().and(Aus.getVrdokCond(kup)).
    and(Aus.getYearCond("DATDOK", Util.getUtil().getYear(
        dataSet.getTimestamp("DATUMKNJ"))));
    
    DataSet skr = Skstavkerad.getDataModule().getTempSet("CKNJIGE EXTBRDOK", cond);
    skr.open();
    for (skr.first(); skr.inBounds(); skr.next()) {
      String key = bookDependant ? prefix + skr.getString("CKNJIGE") : prefix;
      int num = Aus.getNumber(skr.getString("EXTBRDOK"));
      if (!extbrdoks.containsKey(key) ||
          ((Integer) extbrdoks.get(key)).intValue() < num)
        extbrdoks.put(key, new Integer(num));
    }
  }
  
  boolean autoinc, bookDependant;
  int extsize;
  
  private boolean prepareAutoSkIncrement(Map extbrdoks) {
    if (!frmParam.getParam("sk", "autoIncExt", "D", 
      "Automatsko poveæavanje dodatnog broja URA/IRA (D/N)").equalsIgnoreCase("D")) 
         return false;
    
    bookDependant = frmParam.getParam("sk", "extKnjiga", "D", 
      "Ima li svaka knjiga zaseban brojaè (D/N)").equalsIgnoreCase("D");
    extsize = Aus.getNumber(frmParam.getParam("sk", "extSize", "0",
      "Minimalna velicina broja URA/IRA (popunjavanje vedeæim nulama)"));
    if (extsize > 20) extsize = 20;

    fillInitialMap(extbrdoks, false);
    fillInitialMap(extbrdoks, true);

    return true;
  }
  
  private String getNextBrdok(Map extbrdoks, DataSet sk) {
    String prefix = raVrdokMatcher.isKup(sk) ? "I-" : "U-";
    String key = bookDependant ? prefix + sk.getString("CKNJIGE") : prefix;
    int next = !extbrdoks.containsKey(key) ? 1 :
      ((Integer) extbrdoks.get(key)).intValue() + 1;
    extbrdoks.put(key, new Integer(next));
    
    String result = Integer.toString(next);
    if (result.length() < extsize) 
      result = Aus.string(extsize - result.length(), '0') + result;
    return result;
  }
  
  private boolean commitTransTransaction(raPreparedStatement addsk, raPreparedStatement addui) throws Exception {
    skstavke.first();
    //hr.restart.sk.raSaldaKonti.setKumInvalid();
    HashMap extbrdoks = new HashMap();
    if (frmNalozi.getFrmNalozi().getMasterSet().getString("CVRNAL").equals("00")) autoinc = false;
    else autoinc = prepareAutoSkIncrement(extbrdoks);
    
    do {
      addsk.setValues(skstavke);
      if (skstavke.getString("CGKSTAVKE").equals(""))
        addsk.setString("CGKSTAVKE", originalCNALOGA, false);
      if (skstavke.getString("CSKSTAVKE").equals(""))
        addsk.setString("CSKSTAVKE", hr.restart.sk.raSaldaKonti
            .findCSK(skstavke), false);
      if (!raKonta.isSaldak(skstavke.getString("BROJKONTA"))) {
        addsk.setString("PVPOK", "X", false);
        addsk.setString("POKRIVENO", "X", false);
      }
      
/*      if (frmNalozi.getFrmNalozi().getMasterSet().getString("CVRNAL").equals("00")) {
        hr.restart.sk.raSaldaKonti.addToKumulativPS(skstavke);
      } else {
        hr.restart.sk.raSaldaKonti.addToKumulativ(skstavke);
      }
*/      
      // automatsko povecavanje extbrdoka.
      if (autoinc) addsk.setString("EXTBRDOK", getNextBrdok(extbrdoks, skstavke), false);
      addsk.execute();
    } while (skstavke.next());
    uistavke.first();
    if (uistavke.getRowCount() > 0) {
      do {
        addui.setValues(uistavke);
        addui.execute();
      } while (uistavke.next());
    }
    /*raTransaction.saveChanges(hr.restart.baza.dM.getDataModule()
        .getSkkumulativi());*/
    return true;    
  }
  /**
   * parametar da li knjizimo ujedno i racune za salda konti
   * 
   * @return
   */
  public boolean isSKRacKnj() {
    return SKRacKnj;
  }

  /**
   * parametar da li knjizimo ujedno i racune za salda konti, potrebno ga je
   * setirati na true ako se u temeljnici pojavljuju stavke salda konti
   * 
   * @param _p
   */
  public void setSKRacKnj(boolean _p) {
    SKRacKnj = _p;
  }

  public void setInfoKeys(String[] _infoKeys) {
    infoKeys = _infoKeys;
  }

  public void setTransferSet(DataSet _transferSet) {
    transferSet = _transferSet;
  }

  public void setInfColName(String _cn) {
    infColName = _cn;
  }
  
  private String getSKCheckKey(ReadRow r) {
    return r.getString("VRDOK").concat(
        vl.maskZeroInteger(new Integer(r.getInt("CPAR")), 10).concat("-")
            .concat(r.getString("BROJDOK")));
  }

  public void setCNalogaMode(int _mode) {
    if (_mode == CNALMODE || _mode == CNALMODE_8 || _mode == CNALMODE_RBS) {
      cnalogaMode = _mode;
    } else
      throw new IllegalArgumentException(
          "mode mora biti CNALMODE,CNALMODE_8 ili CNALMODE_RBS");
  }

  class TransferInfo {
    String[] keys;
    String[] values;
    String infoColName;
    DataSet azurSet;

    public TransferInfo(String[] _keys, String[] _values, DataSet _azurSet,
        String _infoColName) {
      keys = _keys;
      values = _values;
      azurSet = _azurSet;
      infoColName = _infoColName;
    }

    public TransferInfo(String[] _keys, DataSet _azurSet, String _infoColName) {
      keys = _keys;
      azurSet = _azurSet;
      values = getVals();
      infoColName = _infoColName;
    }

    private String[] getVals() {
      if (keys == null)
        throw new RuntimeException("Nisu zadani kljucevi");
      if (azurSet == null)
        throw new RuntimeException("Nije zadan set za azuriranje");
      String[] _vals = new String[keys.length];
      Variant v = new Variant();
      for (int i = 0; i < keys.length; i++) {
        azurSet.getVariant(keys[i], v);
        _vals[i] = v.toString();
      }
      return _vals;
    }

    public boolean locate() {
      return lookupData.getlookupData().raLocate(azurSet, keys, values);
    }

    public boolean update(String cgks) {
      if (locate()) {
        azurSet.setString(infoColName, cgks);
        return true;
      } else
        return false;
    }
  }
}