/****license*****************************************************************
**   file: frmNarDob.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Pjpar;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raColors;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.MasterDetailChooser;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raTransaction;
import hr.restart.zapod.OrgStr;

import java.awt.Color;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

public class frmNarDob extends raMasterDetail {
	boolean isVTtext = false;
	boolean isVTtextzag = false;
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  lookupData ld = lookupData.getlookupData();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  raControlDocs rCD = new raControlDocs();

  jpNarDobMaster jpMaster;
  jpNarDobDetail jpDetail;
  String key4del;
  public QueryDataSet vttext = null;
  public QueryDataSet vttextzag = null; 
  
  QueryDataSet zahStavkaNew = null;
  QueryDataSet zahStavkaOld = null;


  String corg, nazpar;
  int cpar, lastCpar = 0;
  Timestamp datfrom, datto;

  String[] mkey = Util.mkey;
  
  private String[] vkey = {"CSKL", "VRDOK", "GOD", "BRDOK", "RBSID"};

  static frmNarDob frm;


  public frmNarDob() {
    super(1,3);
    frm = this;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmNarDob getInstance() {
    return frm;
  }

  private void setPreselectValues() {
    corg = getPreSelect().getSelRow().getString("CSKL");
    cpar = getPreSelect().getSelRow().getInt("CPAR");
    datfrom = getPreSelect().getSelRow().getTimestamp("DATDOK-from");
    datto = getPreSelect().getSelRow().getTimestamp("DATDOK-to");
    nazpar = ((presNDO) getPreSelect()).jrfNAZPAR.getText();
  }

  public void setTitleMaster() {
    VarStr title = new VarStr("Narudžbe dobavljaèu ");
    if (cpar != 0) title.append(cpar).append(' ').append(nazpar);
    title.append("  od ").append(rdu.dataFormatter(datfrom));
    title.append(" do ").append(rdu.dataFormatter(datto));
  }

  public void beforeShowMaster() {
    setPreselectValues();
    setTitleMaster();
    jpMaster.jp1.jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndKnjig(corg));
  }
  
  public void beforeShowDetail() {
    // TODO Auto-generated method stub
    jpDetail.rpc.setExtraSklad(null);
    if (getDetailSet().rowCount() > 0)
      jpDetail.rpc.setExtraSklad(getDetailSet().getString("CSKLART"));
  }

  public void EntryPointMaster(char mode) {
    rcc.setLabelLaF(jpMaster.jp2.jlrNAZNAC, false);
    rcc.setLabelLaF(jpMaster.jp2.jlrNAZNACPL, false);
    if (mode == 'N') {
      jpMaster.tabs.setSelectedIndex(0);
      getPreSelect().copySelValues();
      jpMaster.jp1.jlrCpar.forceFocLost();
      jpMaster.jp1.jlrCorg.forceFocLost();
      getMasterSet().setTimestamp("DATDOK", ut.getToday(datfrom, datto));
      getMasterSet().setTimestamp("DATDOSP", getMasterSet().getTimestamp("DATDOK"));
      jpMaster.brdok.SetDefTextDOK(mode);
      setDefaultNacs();
    }
  }
  
  public void EntryPointDetail(char mode) {
    super.EntryPointDetail(mode);
    jpDetail.rcc.setLabelLaF(jpDetail.trans, mode == 'N');
    zahStavkaNew = zahStavkaOld = null;
    oldKol = mode == 'N' ? Aus.zero3 :
      this.getDetailSet().getBigDecimal("KOL");
  }

  private void setDefaultNacs() {
    String defNacotp = hr.restart.sisfun.frmParam.getParam("robno","defNacotp");
    String defNacpl = hr.restart.sisfun.frmParam.getParam("robno","defNacpl");
    if (defNacotp != null && defNacotp.length() > 0 &&
        ld.raLocate(dm.getNacotp(), "CNAC", defNacotp)) {
      getMasterSet().setString("CNAC", defNacotp);
      jpMaster.jp2.jlrCNAC.forceFocLost();
    }
    if (defNacpl != null && defNacpl.length() > 0 &&
        ld.raLocate(dm.getNacpl(), "CNACPL", defNacpl)) {
      getMasterSet().setString("CNACPL", defNacpl);
      jpMaster.jp2.jlrCNACPL.forceFocLost();
    }
  }

  public void SetFokusMaster(char mode) {
  	jpMaster.jp1.val.initJP(mode);
  	vttextzag = null;
  	if (mode=='N'){
  		jpMaster.jp1.val.setValutaEditable(true);
  	} else {
  		jpMaster.jp1.val.setValutaEditable(false);	
  	}
    if (mode != 'B') setPJ(true);
    if (mode != 'N') jpMaster.brdok.SetDefTextDOK(mode);
    
    if (mode == 'N') {
      jpMaster.jp1.jlrCpar.requestFocus();
    } else if (mode == 'I') {
      jpMaster.jp1.jlrCorg.requestFocus();
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jp1.jlrCpar) || vl.isEmpty(jpMaster.jp1.jlrCorg) ||
        vl.isEmpty(jpMaster.jp1.jraDatdok))
      return false;
    return true;
  }

  public boolean doBeforeSaveMaster(char mode) {
    if (mode == 'N') hr.restart.robno.Util.getUtil().getBrojDokumenta(getMasterSet());
    return true;
  }

  public boolean doWithSaveMaster(char mode) {
    if (mode == 'B') {
      hr.restart.robno.Util.getUtil().delSeq(delstr, true);
    }
    if (mode != 'B') {
      if (vttextzag != null && isVTtextzag) {
          isVTtextzag = false;
          if (mode == 'N') {
              vttextzag.setString("CKEY", rCD.getKey(getMasterSet()));
          }
          raTransaction.saveChanges(vttextzag);
          raMaster.markChange("vttext");
      }
    }
    return true;
  }

  public void refilterDetailSet() { 
    super.refilterDetailSet();
    jpDetail.rpc.setGodina(vl.findYear(this.getMasterSet().getTimestamp("DATDOK")));
    //jpDetail.rpc.setCskl(this.getMasterSet().getString("CSKL"));
  }


  String delstr;
  public boolean DeleteCheckMaster() {
    DataSet ds = getMasterSet();
    delstr = rut.getSeqString(ds);
    if (getDetailSet().rowCount()>0) {
      JOptionPane.showConfirmDialog(null,"Nisu pobrisane stavke dokumenta !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return hr.restart.robno.Util.getUtil().checkSeq(delstr, String.valueOf(ds.getInt("BRDOK")));
  }

  public void SetFokusDetail(char mode) {
  	if (mode=='N') vttext = null;
    jpDetail.initJP(mode);
    
    
  }

  void findNSTAVKA() {
    getDetailSet().setShort("RBR",
    Rbr.getRbr().vrati_rbr("STDOKI" ,getMasterSet().getString("CSKL"),
    getMasterSet().getString("VRDOK"),getMasterSet().getString("GOD"),getMasterSet().getInt("BRDOK")));
    getDetailSet().setInt("RBSID",Rbr.getRbr().getRbsID(getDetailSet()));
    
  }

  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jpDetail.jraKol))
      return false;
    if (mode=='N') findNSTAVKA();
    
    if (zahStavkaNew != null) {
      if (checkDohChanged()) return false;
      /*if (getDetailSet().getBigDecimal("KOL").compareTo(
          zahStavkaNew.getBigDecimal("KOL")) != 0) {
        if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(jpDetail,
            "Kolièina na narudžbenici se razlikuje od kolièine na trebovanju!" +
            " Nastaviti ipak?", "Upozorenje", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE)) return false;
      }*/
      if (!zahStavkaNew.getString("STATUS").equalsIgnoreCase("N")) {
        JOptionPane.showMessageDialog(jpDetail, 
            "Odabrana stavka trebovanju je veæ naruèena!",
            "Prijenos", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    findOldStavka(mode);
    if (zahStavkaNew == null && zahStavkaOld != null) {
      if (getDetailSet().getBigDecimal("KOL").compareTo(
          zahStavkaOld.getBigDecimal("KOL")) != 0) {
        if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(jpDetail,
            "Kolièina na narudžbenici se razlikuje od kolièine na trebovanju!" +
            " Nastaviti ipak?", "Upozorenje", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE)) return false;
      }
    }
    if (checkDohChanged()) return false;
    jpDetail.rpc.setExtraSklad(getDetailSet().getString("CSKLART"));
    return true;
  }
  
  private boolean checkDohChanged() {
    if (zahStavkaNew == null) return false;
    zahStavkaNew.refresh();
    if (zahStavkaNew.rowCount() == 0 || 
        zahStavkaNew.getInt("CART") != getDetailSet().getInt("CART")) {
      JOptionPane.showMessageDialog(jpDetail, 
          "Odabrana stavka trebovanja je u meðuvremenu promijenjena!",
          "Prijenos", JOptionPane.ERROR_MESSAGE);
      return true;
    }
    return false;
  }

  private void findOldStavka(char mode) {
    if (mode == 'I' || mode == 'B') {
      VarStr veza = new VarStr(getDetailSet().getString("VEZA"));
      if (veza.length() > 0 && veza.countOccurences('-') >= 4) {
        zahStavkaOld = stdoki.getDataModule().getTempSet(
            Condition.whereAllEqual(vkey, veza.splitTrimmed('-')));
        zahStavkaOld.open();
        if (zahStavkaOld.rowCount() != 1) {
          new Throwable("Greška kod prijenosa ROT->PRK").printStackTrace();
          zahStavkaOld = null;
        }
      }
    }
  }
  
  short oldRbr;
  BigDecimal oldKol;

  public boolean DeleteCheckDetail() {
    oldRbr = this.getDetailSet().getShort("RBR");
    oldKol = this.getDetailSet().getBigDecimal("KOL");
    key4del = rCD.getKey(getDetailSet());
    findOldStavka('B');
System.out.println("key4del "+ key4del);    
    return true;
  }
  
  public boolean doBeforeSaveDetail(char mode) {
    if (mode == 'N') {
      getDetailSet().setString("ID_STAVKA",
          raControlDocs.getKey(getDetailSet(), new String[] { "cskl",
                  "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
    }
    return true;
  }

  public boolean doWithSaveDetail(char mode) {
    try {
      if (!updateZahStavka(mode)) return false;
      if (mode == 'B') {
        vl.recountDataSet(this.getDetailSet(), "RBR", oldRbr, false);
        raTransaction.saveChanges(getDetailSet());
        QueryDataSet tmpVTTEXT = hr.restart.util.Util.getNewQueryDataSet(
				"SELECT * FROM vttext WHERE CKEY='"
						+ key4del + "'", true);
        if (tmpVTTEXT.getRowCount()==1){
        	tmpVTTEXT.deleteRow();
        	raTransaction.saveChanges(tmpVTTEXT);
        	raDetail.markChange("vttext");
        }
        
      }
		if (vttext != null && isVTtext) {
			isVTtext = false;
			if (mode=='N'){
				vttext.setString("CKEY",rCD.getKey(getDetailSet()));
			}
			raTransaction.saveChanges(vttext);
			raDetail.markChange("vttext");
		}
      
      
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
  
  boolean updateZahStavka(char mode) {
    try {
      System.out.println("updateZahStavka:");
      System.out.println(zahStavkaOld);
      System.out.println(zahStavkaNew);
      
      // mogucnosti:
      // 1. stavka nije prebacena iz ZAH-a ni prije ni sad
      if (zahStavkaOld == null && zahStavkaNew == null) return true;
      
      // 2. stavka je od prije prebacena, a sad se brise
      if (mode == 'B') return removeOldLink();
      
      // 2. stavka je od prije prebacena, a sad se samo mijenja kalkulacija
      if (zahStavkaNew == null) return updateZahStavka(zahStavkaOld);
      
      // 3. stavka je povezana sa ZAH stavkom, a prije nije bila
      //  (nevezano radi li se o unosu nove ili izmjeni stare, nepovezane stavke)
      if (zahStavkaOld == null) return updateZahStavka(zahStavkaNew);
      
      // 4. stavka je povezana opet s istom stavkom kao i prije. Svodi se na 2/3
      String diff = dM.compareColumns(zahStavkaNew, zahStavkaOld, vkey);
      if (diff == null) return updateZahStavka(zahStavkaOld);
      
      // 5. stavka je povezana s novom, a prije je bila s nekom drugom
      removeOldLink();
      updateZahStavka(zahStavkaNew);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
  
  private boolean removeOldLink() {
    Aus.sub(zahStavkaOld, "KOL1", oldKol);
    if (zahStavkaOld.getBigDecimal("KOL1").signum() < 0) {
      Aus.clear(zahStavkaOld, "KOL1");
      System.out.println("KOL < 0?? " + zahStavkaOld);
    }
    if (Aus.comp(zahStavkaOld, "KOL1", "KOL") < 0) {
      zahStavkaOld.setString("STATUS", "N");
      QueryDataSet master = doki.getDataModule().getTempSet(
          Condition.whereAllEqual(Util.mkey, zahStavkaOld));
      master.open();
      if (master.getRowCount() == 1) {
        master.setString("STATIRA", "N");
        raTransaction.saveChanges(master);
      } else new Throwable("Greška kod prijenosa ROT->PRK").printStackTrace();
    }
    raTransaction.saveChanges(zahStavkaOld);
    return true;
  }
  
  private boolean updateZahStavka(QueryDataSet stavka) {
    //stavka.setString("STATUS", "P");
    //stavka.setString("VEZA", getDetailSet().getString("ID_STAVKA"));
    getDetailSet().setString("VEZA", stavka.getString("ID_STAVKA"));
    
    if (zahStavkaOld == stavka) Aus.sub(stavka, "KOL1", oldKol);
    Aus.add(stavka, "KOL1", getDetailSet(), "KOL");
    if (Aus.comp(stavka, "KOL1", "KOL") >= 0) {
      stavka.setString("STATUS", "P");
      DataSet nonp = stdoki.getDataModule().getTempSet(Condition.whereAllEqual(
          Util.mkey, stavka).and(Condition.equal("STATUS", "N")));
      nonp.open();
      if (nonp.rowCount() == 0 || (nonp.rowCount() == 1 &&
          dM.compareColumns(stavka, nonp, vkey) == null)) {
        // postavi status mastera na prenesen
        QueryDataSet master = doki.getDataModule().getTempSet(
            Condition.whereAllEqual(Util.mkey, stavka));
        master.open();
        if (master.getRowCount() == 1) {
          master.setString("STATIRA", "P");
          raTransaction.saveChanges(master);
        } else new Throwable("Greška kod prijenosa ROT->PRK").printStackTrace();
      }
    }
    raTransaction.saveChanges(stavka);
    raTransaction.saveChanges(getDetailSet());
    return true;
  }

  public boolean ValDPEscapeDetail(char mode) {
  	vttext = null;
    if (mode == 'N' && jpDetail.rpcLostFocus) {
      jpDetail.rpcLostFocus = false;
      jpDetail.rpc.EnabDisab(true);
      jpDetail.rpc.setCART();
      rcc.setLabelLaF(jpDetail.jraKol, false);
      rcc.setLabelLaF(jpDetail.jraNc, false);
      rcc.setLabelLaF(jpDetail.jraPop, false);
      rcc.setLabelLaF(jpDetail.jraVc, false);
      rcc.setLabelLaF(jpDetail.jraIvc, false);
      getDetailSet().setBigDecimal("KOL", _Main.nul);
      getDetailSet().setBigDecimal("NC", _Main.nul);
      getDetailSet().setBigDecimal("INAB", _Main.nul);
      getDetailSet().setBigDecimal("UPRAB", _Main.nul);
      getDetailSet().setBigDecimal("VC", _Main.nul);
      getDetailSet().setBigDecimal("IBP", _Main.nul);
      jpDetail.rcc.setLabelLaF(jpDetail.trans, true);
      zahStavkaNew = zahStavkaOld = null;
      return false;
    } else if (mode == 'N' && !jpDetail.rpc.jlrSklad.getText().equals("")){
      rcc.EnabDisabAll(jpDetail.rpc.jlrSklad,true);
      jpDetail.rpc.jlrSklad.setText("");
      jpDetail.rpc.jlrSklad.emptyTextFields();
      jpDetail.rpc.jlrSklad.requestFocus();
      return false;
    }
    return true;
  }

  public void AfterSaveDetail(char mode) {
    if (mode == 'N') {
      /*SwingUtilities.invokeLater(new Runnable(){
        public void run() {
          jpDetail.rpc.EnabDisab(true);
          rcc.EnabDisabAll(jpDetail.rpc.jlrSklad,true);
          rcc.EnabDisabAll(jpDetail.rpc.jlrNazSklad,true);
          rcc.EnabDisabAll(jpDetail.rpc.jbSklad,true);
          jpDetail.rpc.jlrSklad.setText("");
          jpDetail.rpc.jlrSklad.emptyTextFields();
          jpDetail.rpc.jlrSklad.requestFocus();
        }
      });*/
    }
    vttext = null;
    
  }

  public boolean isDetailExist(){

    if (stdoki.getDataModule().getRowCount(Condition.whereAllEqual(
        new String[] {"CSKL","GOD","VRDOK","BRDOK"}, getMasterSet())) > 0) return true;
    else {
      javax.swing.JOptionPane.showMessageDialog(null,
          "Ne postoje stavke ovog dokumenta. Nemogu\u0107 ispis!",
          "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }

  public void Funkcija_ispisa_master() {
    if (!isDetailExist()) return;
    reportsQuerysCollector.getRQCModule().ReSql(" AND " +
        Condition.whereAllEqual(mkey, getMasterSet()).qualified("doki"), "NDO");
    super.Funkcija_ispisa_master();
  }

  public void Funkcija_ispisa_detail() {
    if (!isDetailExist()) return;
    reportsQuerysCollector.getRQCModule().ReSql(" AND " +
        Condition.whereAllEqual(mkey, getMasterSet()).qualified("doki"), "NDO");
    super.Funkcija_ispisa_detail();
  }

  public String getNaTemelju() {    
    if (getMasterSet().getString("OPIS").length() == 0) return "";
    if (jpMaster.jp1.rcbUvjet.getSelectedItem().toString().length()==0) 
      return getMasterSet().getString("OPIS");
    return jpMaster.jp1.rcbUvjet.getSelectedItem() + " " + getMasterSet().getString("OPIS") + " " +
        jpMaster.jp1.rcbAkcija.getSelectedItem();
  }

  void setPJ(boolean force) {
    if (getMasterSet().getInt("CPAR") != lastCpar || force) {
      lastCpar = getMasterSet().getInt("CPAR");
      if (lastCpar == 0) jpMaster.jp2.jrfPJ.setRaDataSet(null);
      else jpMaster.jp2.jrfPJ.setRaDataSet(Pjpar.getDataModule().getFilteredDataSet(
          Condition.equal("CPAR", lastCpar)));
      jpMaster.jp2.jrfPJ.setText("");
      jpMaster.jp2.jrfPJ.forceFocLost();
    }
  }

  void afterDatdok() {
    getMasterSet().setTimestamp("DATDOSP",
      ut.addDays(getMasterSet().getTimestamp("DATDOK"), getMasterSet().getShort("DDOSP")));
  }

  void afterDaniz() {
    getMasterSet().setTimestamp("DATDOSP",
      ut.addDays(getMasterSet().getTimestamp("DATDOK"), getMasterSet().getShort("DDOSP")));
  }

  void afterDatiz() {
    getMasterSet().setShort("DDOSP", (short) rdu.DateDifference(
        getMasterSet().getTimestamp("DATDOK"),
        getMasterSet().getTimestamp("DATDOSP")));
  }

  boolean findDOBART() {
    dm.getDob_art().open();
    return hr.restart.util.lookupData.getlookupData().raLocate(dm.getDob_art(),
        new com.borland.dx.dataset.DataSet[] {getMasterSet(),getDetailSet()},
        new java.lang.String[] {"CPAR","CART"},
        new java.lang.String[] {"CPAR","CART"});
  }

  void afterArt() {
    if (zahStavkaNew != null) {
      Aus.set(getDetailSet(), "KOL", zahStavkaNew);
      Aus.sub(getDetailSet(), "KOL", zahStavkaNew, "KOL1");
      getDetailSet().setString("NAZART", zahStavkaNew.getString("NAZART"));
      afterKOL();
    }
    if (findDOBART()) {
      getDetailSet().setBigDecimal("NC", dm.getDob_art().getBigDecimal("DC"));
      afterNC();
    } else if (jpDetail.rpc.AST.findStanje(this.getMasterSet().getString("GOD"),
         this.getMasterSet().getString("CSKL"),this.getDetailSet().getInt("CART"))) {
      getDetailSet().setBigDecimal("NC", jpDetail.rpc.gettrenStanje().getBigDecimal("NC"));
      afterNC();
    } else if (ld.raLocate(dm.getArtikli(), "CART", 
        Integer.toString(getDetailSet().getInt("CART")))) {
      System.out.println("našao");
      getDetailSet().setBigDecimal("NC", dm.getArtikli().getBigDecimal("DC"));
      afterNC();
    }
//    rcc.EnabDisabAll(jpDetail.jpCSKL, false);
    rcc.EnabDisabAll(jpDetail.rpc.jlrSklad,true);
    rcc.EnabDisabAll(jpDetail.rpc.jlrNazSklad,true);
    rcc.EnabDisabAll(jpDetail.rpc.jbSklad,true);

    rcc.setLabelLaF(jpDetail.jraKol, true);
    rcc.setLabelLaF(jpDetail.jraNc, true);
    rcc.setLabelLaF(jpDetail.jraPop, true);
    rcc.setLabelLaF(jpDetail.jraVc, true);
    rcc.setLabelLaF(jpDetail.jraIvc, true);
    rcc.setLabelLaF(jpDetail.jraNcDOB, true);
    if (zahStavkaNew == null && lookupData.getlookupData().
          raLocate(dm.getArtikli(), "CART", jpDetail.rpc.getCART())) {
      getDetailSet().setBigDecimal("KOL", dm.getArtikli().getBigDecimal("KOLZANAR"));
      afterKOL();
    }
    if (zahStavkaNew == null) jpDetail.jraKol.requestFocusLater();
    else jpDetail.jraNc.requestFocusLater();
  }

  void afterKOL() {
    getDetailSet().setBigDecimal("INAB", rut.multiValue(getDetailSet().getBigDecimal("NC"),
        getDetailSet().getBigDecimal("KOL")));
    
    if (getDetailSet().getBigDecimal("KOL1").signum() == 0 && 
        ld.raLocate(dm.getArtikli(), "CART", Integer.toString(getDetailSet().getInt("CART")))) {
      
      if (dm.getArtikli().getBigDecimal("BRJED").signum() != 0) {
        getDetailSet().setBigDecimal("KOL1", getDetailSet().getBigDecimal("KOL").
            divide(dm.getArtikli().getBigDecimal("BRJED"), 3, BigDecimal.ROUND_HALF_UP));
      } else {
        getDetailSet().setBigDecimal("KOL1", Aus.zero3);
      }
    }

    afterAll();
  }

  void afterNC() {
    getDetailSet().setBigDecimal("INAB", rut.multiValue(getDetailSet().getBigDecimal("NC"),
        getDetailSet().getBigDecimal("KOL")));
    afterAll();
  }
  void afterPop() {
	getDetailSet().setBigDecimal("INAB", rut.multiValue(getDetailSet().getBigDecimal("NC"),
	    getDetailSet().getBigDecimal("KOL")));
    afterAll();
  }
  void afterNCDOB() {
    getDetailSet().setBigDecimal("IPRODBP", rut.multiValue(getDetailSet().getBigDecimal("FVC"),
            getDetailSet().getBigDecimal("KOL")));
    getDetailSet().setBigDecimal("NC", rut.multiValue(getDetailSet().getBigDecimal("FVC"),
        getMasterSet().getBigDecimal("TECAJ")));
    afterNC();
  }
  void afterVc() {
	    getDetailSet().setBigDecimal("IBP", rut.multiValue(getDetailSet().getBigDecimal("VC"),
	            getDetailSet().getBigDecimal("KOL")));
	    getDetailSet().setBigDecimal("UPRAB", rut.negateValue(rut.sto, rut.findPostotak(getDetailSet().getBigDecimal("INAB"), getDetailSet().getBigDecimal("IBP"))));
  }
  void afterIvc() {
	    getDetailSet().setBigDecimal("VC", rut.divideValue(getDetailSet().getBigDecimal("IBP"),
	            getDetailSet().getBigDecimal("KOL")));
	    getDetailSet().setBigDecimal("UPRAB", rut.negateValue(rut.sto, rut.findPostotak(getDetailSet().getBigDecimal("INAB"), getDetailSet().getBigDecimal("IBP"))));
	  }
  void afterAll() {
	getDetailSet().setBigDecimal("IBP", findMxFormula(getDetailSet().getBigDecimal("INAB"),
	            getDetailSet().getBigDecimal("UPRAB")));
	getDetailSet().setBigDecimal("VC", rut.divideValue(getDetailSet().getBigDecimal("IBP"),
        getDetailSet().getBigDecimal("KOL")));
    /*getDetailSet().setBigDecimal("IBP", rut.multiValue(getDetailSet().getBigDecimal("VC"),
            getDetailSet().getBigDecimal("KOL")));*/
	  
  }
  private BigDecimal findMxFormula(BigDecimal osnovica, BigDecimal posto) {
	  return rut.multiValue(osnovica, rut.negateValue(rut.one, rut.divideValue(posto, rut.sto)));
  }
  private void jbInit() throws Exception {
    this.setUserCheck(true);
    this.setMasterSet(dm.getZagNdo());
    this.setNaslovMaster("Narudžbe dobavljaèu");
    this.setVisibleColsMaster(new int[] {4, 5, 6});
    this.setMasterKey(mkey);
    jpMaster = new jpNarDobMaster(this);
    this.setJPanelMaster(jpMaster);
    this.raMaster.getJpTableView().addTableModifier(
        new raTableColumnModifier("CPAR", new String[] {"CPAR", "NAZPAR"}, dm.getPartneri()));

    dm.getStNdo().open();
    dm.getStNdo().getColumn("FVC").setCaption("Fakturna cijena bez poreza");
    dm.getStNdo().getColumn("IBP").setCaption("Fakturni iznos bez poreza");
    dm.getStNdo().getColumn("NC").setCaption("Cijena bez poreza");
    dm.getStNdo().getColumn("INAB").setCaption("Iznos bez poreza");
    dm.getStNdo().getColumn("KOL2").setCaption("Isporuèeno");
    this.setDetailSet(dm.getStNdo());
    this.setNaslovDetail("Stavke narudžbe");
    this.setVisibleColsDetail(new int[] {4, 8, 11, 25, 26});
    this.setDetailKey(Util.dkey);
    jpDetail = new jpNarDobDetail(this);
    this.setJPanelDetail(jpDetail);
    jpDetail.initRpcart();
    jpDetail.rpc.enableNameChange(true);
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repNarDob", "hr.restart.robno.repNarDobSource", "NarDob", "Narudžbe dobavljaèu");
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repNarPop", "hr.restart.robno.repNarDobSource", "NarPop", "Narudžbe dobavljaèu s popustom");
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repNarDobV", "hr.restart.robno.repNarDobSource", "NarDob", "Narudžbe dobavljaèu u valuti");
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repNarDobKol", "hr.restart.robno.repNarDobSource", "NarDobKol", "Narudžbe dobavljaèu - kolièinska");
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repNarDobKolTwo", "hr.restart.robno.repNarDobSource", "NarDobKolTwo", "Narudžbe dobavljaèu - dvije kolièine");
    
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repNarDob", "hr.restart.robno.repNarDobSource", "NarDob", "Narudžbe dobavljaèu");
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repNarPop", "hr.restart.robno.repNarDobSource", "NarPop", "Narudžbe dobavljaèu s popustom");
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repNarDobV", "hr.restart.robno.repNarDobSource", "NarDob", "Narudžbe dobavljaèu u valuti");
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repNarDobKol", "hr.restart.robno.repNarDobSource", "NarDobKol", "Narudžbe dobavljaèu - kolièinska");
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repNarDobKolTwo", "hr.restart.robno.repNarDobSource", "NarDobKolTwo", "Narudžbe dobavljaèu - dvije kolièine");
  }
	public boolean updateTxt() {
		if (raDetail.getMode() == 'N') {
			QueryDataSet tmpVTTEXT = hr.restart.util.Util.getNewQueryDataSet(
					"SELECT * FROM vttext WHERE CKEY='"
							+ rCD.getKey(jpDetail.rpc.jrfCART.getRaDataSet())
							+ "'", true);

System.out.println(tmpVTTEXT.getQuery().getQueryString());			

			if (tmpVTTEXT.getRowCount() > 0) {
				vttext = hr.restart.util.Util.getNewQueryDataSet(
						"SELECT * FROM vttext WHERE 1=0", true);
				vttext.insertRow(true);
				vttext.setString("CKEY", rCD.getKey(getDetailSet()));
				vttext.setString("TEXTFAK", tmpVTTEXT.getString("TEXTFAK"));
				isVTtext = true;
			}
		}
		return true;
	}  
	
	protected void zahDohvat() {
	  String art = jpDetail.rpc.getCART().trim();
	  boolean haveArt = art.length() > 0;
	  String cskl = jpDetail.rpc.getCskl().trim();
	  boolean haveCskl = cskl.length() > 0;
	    
	  boolean fullZah = frmParam.getParam("robno", "dohvatZAH", "D", 
	        "Prikazati sve stavke trebovanja kod dohvata na narudžbenici (D,N)", true).
	          equalsIgnoreCase("D");
	  
	  String dod = "";
	  if (haveArt) dod = " AND stdoki.cart=" + art;
	  else if (!fullZah) dod = " AND stdoki.status='N'";
	  if (haveCskl) dod = dod + " AND stdoki.cskl='" + cskl + "'";
	  
	  String[] mcols = {"CUSER", "CORG", "CSKL", "VRDOK", "GOD", 
	        "BRDOK", "DATDOK", "DATDOSP", "OPIS", "BRDOKIZ"};
	    
	  String[] dcols = {"RBR", "CART", "CART1", "BC", "NAZART", 
	        "JM", "KOL", "KOL1", "KOL2", "STATUS"};
	  
	  String q = 
	      "SELECT doki.cuser, doki.corg, doki.cskl, doki.vrdok, doki.god, " +
          "doki.brdok, doki.datdok, doki.datdosp, doki.opis, doki.brdokiz, " +
          "stdoki.rbr, stdoki.cart, stdoki.cart1, stdoki.bc, " +
          "stdoki.nazart, stdoki.jm, stdoki.kol, stdoki.kol1, " +
          "stdoki.kol2, stdoki.status FROM doki,stdoki WHERE " +
          Util.getUtil().getDoc("doki", "stdoki") + 
          " AND doki.god='" + getMasterSet().getString("GOD") +
          "' AND doki.vrdok='TRE' AND doki.statira='N'" + dod;
	  System.out.println(q);
	  
	  QueryDataSet doh = new QueryDataSet();
	    Aus.setFilter(doh, q);
	    List cols = new ArrayList();
	    for (int i = 0; i < mcols.length; i++)
	      cols.add(doki.getDataModule().getColumn(mcols[i]).clone());
	    for (int i = 0; i < dcols.length; i++)
	      cols.add(stdoki.getDataModule().getColumn(dcols[i]).clone());
	    doh.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+
	        MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
	    doh.setColumns((Column[]) cols.toArray(new Column[cols.size()]));
	    doh.getColumn("KOL1").setCaption("Naruèeno");
	    doh.getColumn("KOL2").setCaption("Isporuèeno");
	    doh.open();
	    if (doh.rowCount() == 0) {
	      JOptionPane.showMessageDialog(jpDetail, 
	          "Nema nenaruèenih stavaka trebovanja u ovoj godini!",
	          "Prijenos", JOptionPane.ERROR_MESSAGE);
	      return;
	    }
	    doh.setSort(new SortDescriptor(new String[] {
	        "DATDOK", "CSKL", "BRDOK", "RBR"}));
	    
	    MasterDetailChooser mdc = new MasterDetailChooser(doh, 
	        haveArt ? "ndo-dohvat-zah-art" : "zah-dohvat-zah", 
	            mcols, dcols, new int[] {2, 4, 5, 6, 10,
	            Aut.getAut().getCARTdependable(11, 12, 13),14,15,16});
	    mdc.addModifier(new StatusColorModifier(mdc.isTurboTable()));
	    
	    if (mdc.show(raDetail.getWindow(), 
	        "Dohvat stavki trebovanja za naruèivanje")) {
	      if (!doh.getString("STATUS").equals("N")) {
	        JOptionPane.showMessageDialog(jpDetail, 
	            "Odabrana stavka je veæ od prije naruèena!",
	            "Prijenos", JOptionPane.ERROR_MESSAGE);
	        return;
	      }
	      zahStavkaNew = stdoki.getDataModule().getTempSet(
	          Condition.whereAllEqual(Util.dkey, doh));
	      zahStavkaNew.open();
	      getDetailSet().setString("CSKLART", zahStavkaNew.getString("CSKL"));
//          jpDetail.rpc.setCskl(zahStavkaNew.getString("CSKL"));
	      if (haveArt) afterArt();
	      else jpDetail.rpc.setCART(doh.getInt("CART"));
	    }
	}
	
	static class StatusColorModifier extends raTableModifier {
	    Variant shared = new Variant();
	    HashSet dset = new HashSet(Arrays.asList(new String[]
	       {"RBR", "CART", "CART1", "BC", "NAZART", 
	        "JM", "KOL", "KOL1", "KOL2", "FVC", "FMC", "STATUS"}));
	    
	    boolean turboTable;
	    
	    public StatusColorModifier(boolean turboTable) {
	      this.turboTable = turboTable;
	    }

	    public boolean doModify() {
	      if (getTable() instanceof JraTable2) {
	        JraTable2 tab = (JraTable2) getTable();
	        if (tab.getDataSet().getRowCount() > 0 && 
	            tab.getDataSet().hasColumn("STATUS") != null) {
	          if (turboTable && !dset.contains(tab.getDataSetColumn(getColumn()).
	              getColumnName().toUpperCase())) return false;
	          tab.getDataSet().getVariant("STATUS", this.getRow(), shared);
	          return !shared.getString().equals("N");
	        }
	      }
	      return false;
	    }
	    
	    public void modify() {
	      JComponent jRenderComp = (JComponent) renderComponent;
	      if (isSelected()) {
	        jRenderComp.setBackground(raColors.green);
	        jRenderComp.setForeground(Color.black);
	      } else {
	        //jRenderComp.setBackground(getTable().getBackground());
	        jRenderComp.setForeground(Color.green.darker().darker());
	      }
	    }
	  }
}
