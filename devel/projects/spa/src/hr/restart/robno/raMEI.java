/****license*****************************************************************
**   file: raMEI.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraTextField;
import hr.restart.swing.JraTextMultyKolField;
import hr.restart.util.LinkClass;
import hr.restart.util.SanityException;
import hr.restart.util.raTransaction;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raMEI extends hr.restart.util.raMasterDetail{

  hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  raDateUtil rdu = raDateUtil.getraDateUtil();
  LinkClass lc = LinkClass.getLinkClass();  //  (ab.f)  double polja
  raKalkulBDMeskla rKM = new raKalkulBDMeskla();
  rajpMasterMeskla raMM = new rajpMasterMeskla();
  jpMesklaDetail2 raDM;
  Rbr rbr = Rbr.getRbr();
  String Godina ;
  java.lang.Integer Broj ;
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  jpSelectMeskla jpSelectM;
  hr.restart.swing.raTableColumnModifier TCM  ;
  hr.restart.swing.raTableColumnModifier TCM1 ;
  boolean bMEI = true;
  boolean allowNeg = false;
  raControlDocs rCD = new raControlDocs();
  allStanje AST = new allStanje();
  allStanje ASTUL = new allStanje();
  DataSet StanjeIzlaz;
  DataSet StanjeUlaz;
  private String srcString="";
  private hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  short oldRbr=0;

  public void setSelect(jpSelectMeskla jSM){
    jpSelectM=jSM ;
  }

  public raMEI() {
    super(1,3);
    setUserCheck(true);
    TCM =  new hr.restart.swing.raTableColumnModifier("CSKLUL",
                               new String [] {"CSKL","NAZSKL"},
                               new String[] {"CSKLUL"},
                               new String[] {"CSKL"},
                               dm.getSklad());
    TCM1 =  new hr.restart.swing.raTableColumnModifier("CSKLIZ",
                               new String [] {"CSKL","NAZSKL"},
                               new String[] {"CSKLIZ"},
                               new String[] {"CSKL"},
                               dm.getSklad());

    raMaster.getJpTableView().addTableModifier( TCM);
    raMaster.getJpTableView().addTableModifier( TCM1);
    Setup();
  }

  public void Setup() {

    jpSelectM = selectMEI.getPres();
    raDM = new jpMesklaDetail2(){
      public void addRest(){
        add4MEI();
      }
    };
    String[] key={"CSKLIZ","CSKLUL","VRDOK","GOD","BRDOK"};
    String[] dkey={"CSKLIZ","CSKLUL","VRDOK","GOD","BRDOK","RBR"};
    setNaslovMaster("Me\u0111uskladišnica izlaz");
    setNaslovDetail("Stavke me\u0111uskladišnice izlaz");
    set_kum_detail(false);
    setMasterSet(dm.getMesklaMEI());
    setDetailSet(dm.getStmesklaMEI());
    setJPanelMaster(raMM);
    setJPanelDetail(raDM);
    raMM.BindComp(getMasterSet());
    raDM.setDataSet(getDetailSet());

    setMasterKey(key);
    setDetailKey(dkey);
    this.setVisibleColsMaster(new int[] {5,6,0,1});
    setVisibleColsDetail(new int[] {5,Aut.getAut().getCARTdependable(6,7,8),9,10,11});
    
    rCD.setisNeeded(hr.restart.sisfun.frmParam.getParam("robno",
        "kontKalk", "D",
        "Kontrola ispravosti redoslijeda unosa dokumenata")
        .equalsIgnoreCase("D"));

    raMaster.getRepRunner().clearAllReports();
    raDetail.getRepRunner().clearAllReports();
    raMaster.getRepRunner().addReport("hr.restart.robno.repMei","hr.restart.robno.repMeskla","Mei","Me\u0111uskladišnica izlaz");
    raDetail.getRepRunner().addReport("hr.restart.robno.repMei","hr.restart.robno.repMeskla","Mei","Me\u0111uskladišnica izlaz");
    raMaster.getRepRunner().addReport("hr.restart.robno.repMeiExtendedVersion","hr.restart.robno.repMeskla","MeiExtendedVersion","Me\u0111uskladišnica izlaz - vrijednosna");
    raDetail.getRepRunner().addReport("hr.restart.robno.repMeiExtendedVersion","hr.restart.robno.repMeskla","MeiExtendedVersion","Me\u0111uskladišnica izlaz - vrijednosna");
  }

/// MASTER DIO
  public void EntryPointMaster(char mode){}

  public void SetFokusMaster(char mode) {

    raMM.jpgetval.initJP(mode); //dodao andrej 02-11-2001 16:37
    raMM.setMode(mode);
    raMM.disabCskl();

    if (mode=='N') {
      jpSelectM.copySelValues();
      raMM.jtfCSKLIZ.forceFocLost();
      raMM.jtfCSKLUL.forceFocLost();
      getMasterSet().setTimestamp("DATDOK",jpSelectM.getSelRow().getTimestamp("DATDOK-to"));
      raMM.jtfDATDOK.selectAll();
      raMM.jtfDATDOK.requestFocus();
    }
    else if (mode=='I'){
      raMM.jtfDATDOK.selectAll();
      raMM.jtfDATDOK.requestFocus();
    }
    BrojDok(mode);
  }

  public void BrojDok(char mode) {
    if (mode=='N') {
      this.raMM.rajpBrDok.SetTextDOK((getMasterSet().getString("VRDOK")+" - "+
                                      getMasterSet().getString("CSKLIZ").trim()+" - "+
                                      getMasterSet().getString("CSKLUL").trim()+" / "+
                                      val.findYear(getMasterSet().getTimestamp("DATDOK"))));
    } else {
      this.raMM.rajpBrDok.SetTextDOK((getMasterSet().getString("VRDOK")+" - "+
                                      getMasterSet().getString("CSKLIZ").trim()+" - "+
                                      getMasterSet().getString("CSKLUL").trim()+" / "+
                                      val.findYear(getMasterSet().getTimestamp("DATDOK"))+" - "+
                                      getMasterSet().getInt("BRDOK")));
    }
    this.raMM.rajpBrDok.SetStatus();
  }

  public boolean DeleteCheckMaster() {

    boolean returnValue = true;
    srcString=util.getSeqString(getMasterSet());
    returnValue =  util.checkSeq(srcString,Integer.toString(getMasterSet().getInt("BRDOK")));
    if (returnValue) {
      if (!this.getDetailSet().isEmpty()) {
        javax.swing.JOptionPane.showConfirmDialog(null,"Nisu pobrisane stavke dokumenta !","Gre\u0161ka",
            javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE);
        returnValue = false;
      }
    }
    return returnValue;
  }

  public void AfterDeleteMaster() {}
  public void AfterSaveMaster(char mode) {}

  public boolean ValidacijaMaster(char mode) {
    if (val.isEmpty(this.raMM.jtfDATDOK)) {
      return false;
    }
    /*if (!isKnjigDataOK()){
      javax.swing.JOptionPane.showMessageDialog(null,
        "Datum u periodu koji je veæ knjižen !","Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
      this.raMM.jtfDATDOK.requestFocus();
      return false;
    }*/
//    if (mode=='N') {
//    	getBrojDokumenta(getMasterSet());
//    }
    return true;
  }

  public void getBrojDokumenta(com.borland.dx.dataset.DataSet ds){
//      dm.getSeq().refresh();
      Godina=val.findYear(getMasterSet().getTimestamp("DATDOK"));
      ds.setString("GOD",Godina);
      Broj=val.findSeqInteger(ds);
      
      ds.setInt("BRDOK",Broj.intValue());
  }

/// metode detail part-a

  public void beforeShowDetail() {
    InitRpcart();
  }
  
  public void beforeShowMaster() {
    setNaslovMaster("Izlazne meðuskladišnice za skladišta " +
        jpSelectM.getSelRow().getString("CSKLIZ") + " -> " +
        jpSelectM.getSelRow().getString("CSKLUL"));
    allowNeg = frmParam.getParam("robno", "allowNeg", "N", "Dopustiti odlaženje u minus").equals("D");
  }
  
  public void masterSet_navigated(com.borland.dx.dataset.NavigationEvent ne) {
    this.setNaslovDetail("Stavke izlazne meðuskladišnice "+
        getMasterSet().getString("CSKLIZ").trim()+" - "+
        getMasterSet().getString("CSKLUL").trim()+" / "+
        getMasterSet().getString("GOD") + " - " + 
        getMasterSet().getInt("BRDOK"));
  }

  public void InitRpcart(){
    raDM.rpcart.setGodina(hr.restart.util.Valid.getValid().findYear(getMasterSet().getTimestamp("DATDOK")));
    raDM.rpcart.setCskl(getMasterSet().getString("CSKLIZ"));
    raDM.rpcart.setMode("N");
  }

  public void SetFokusDetail(char mode) {

    if (mode=='N') {

      raDM.enable_rapancart(true);
      raDM.enable_rest(false);
      raDM.rpcart.SetDefFocus();
      PrepMeskla4Add();
      raDM.rpcart.setMode("N");

    }
    else if (mode=='I') {

      raDM.rpcart.setMode("N");
      raDM.enable_rapancart(false);
      raDM.enable_rest(true);
      rKM.stavka.Init();
      rKM.stavkaold.Init();
      lc.TransferFromDB2Class(getDetailSet(),rKM.stavka);
      lc.TransferFromDB2Class(getDetailSet(),rKM.stavkaold);
      raDM.jtfKOL.selectAll();
      raDM.jtfKOL.requestFocus();

    }
  }

  public void AfterSaveDetail(char mode) {}

  public void ponistavanjeDijela(){
    rKM.ponistavanjeUlaza();
  }

  public boolean isUpdatePossible(){
    return true;
  }

  public boolean ValidacijaDetail(char mode) {

    if (hr.restart.util.Valid.getValid().isEmpty(raDM.jtfKOL)) return false;

    findStanja();
    if (mode=='N'){
      getDetailSet().setShort("RBR",findNSTAVKA());
      getDetailSet().setInt("rbsid",rbr.getRbsID(getDetailSet()));
    }
    else if (!(mode=='I' && isUpdatePossible())) {
      javax.swing.JOptionPane.showMessageDialog(null,
          "Nije moguæe ispraviti ovu stavku jer postoji promet po kome je ona razdužena !",
          "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
//      System.out.println("!(mode=='I' && isUpdatePossible())" +"->"+ !(mode=='I' && isUpdatePossible()));
      return false ;
    }

    int i = rKM.TestStanja();

    if (i==-1&& bMEI && !allowNeg){
        javax.swing.JOptionPane.showMessageDialog(null,
            "Koli\u010Dina je ve\u0107a nego koli\u010Dina na zalihi !",
            "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
        raDM.jtfKOL.requestFocus();
        return false;
    }
    else {
       if (i == -2 && bMEI && !allowNeg){
          String rezkol =hr.restart.sisfun.frmParam.getParam("robno","rezkol");
          if (!rezkol.equals("N")){
             javax.swing.JOptionPane.showMessageDialog(null,
              "Koristite rezervirane koli\u010Dine !",
              "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
              raDM.jtfKOL.requestFocus();
              if (rezkol.equals("D")) return false;
          }
        }

        if (hr.restart.sisfun.frmParam.getParam("robno","minkol","N").equalsIgnoreCase("D") &&
            raDM.rpcart.jrfCART.getRaDataSet().getBigDecimal("MINKOL").doubleValue()!=0 &&
            rKM.isKolStanjeManjeOd(raDM.rpcart.jrfCART.getRaDataSet().getBigDecimal("MINKOL")) && bMEI) {
              raDM.jtfKOL.requestFocus();
          javax.swing.JOptionPane.showMessageDialog(raDetail.getWindow(),
              "Koli\u010Dina nakon unosa dokumenta je "+rKM.getKolStanjeAfterMat()+" "+
              raDM.rpcart.jrfCART.getRaDataSet().getString("JM")+" "+
              " i pala je ispod dozvoljene minimalne koli\u010Dine koja iznosi "+
              raDM.rpcart.jrfCART.getRaDataSet().getBigDecimal("MINKOL") +
              raDM.rpcart.jrfCART.getRaDataSet().getString("JM")+" !!!! ",
              "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
          return false;
        }

        if (hr.restart.sisfun.frmParam.getParam("robno","sigkol","N").equalsIgnoreCase("D") &&
            raDM.rpcart.jrfCART.getRaDataSet().getBigDecimal("SIGKOL").doubleValue()!=0 &&
            rKM.isKolStanjeManjeOd(raDM.rpcart.jrfCART.getRaDataSet().getBigDecimal("SIGKOL"))&& bMEI){
              raDM.jtfKOL.requestFocus();
          if (!(javax.swing.JOptionPane.showConfirmDialog(raDetail,
              "Koli\u010Dina nakon unosa dokumenta je "+rKM.getKolStanjeAfterMat()+" "+
              raDM.rpcart.jrfCART.getRaDataSet().getString("JM")+" "+
              " i pala je ispod signalne koli\u010Dine koja iznosi "+
              raDM.rpcart.jrfCART.getRaDataSet().getBigDecimal("SIGKOL") +
              raDM.rpcart.jrfCART.getRaDataSet().getString("JM")+" !!!! Želite li nastaviti ?",
              "Upit",javax.swing.JOptionPane.YES_NO_OPTION,javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.YES_OPTION)){
            return false;
          }
        }
        if (!dlgSerBrojevi.getdlgSerBrojevi().beforeDeleteSerBr(getDetailSet(), 'M')) {
          return false;
        }
        rKM.kalkStanja();
        SanityCheck.basicStmeskla(getDetailSet());
        return true;
    }
  }

  public boolean DeleteCheckDetail(){

    boolean returnValue = true ;
    if (raDM.rpcart.getISB().equals("D")) {
      if (!dlgSerBrojevi.getdlgSerBrojevi().beforeDeleteSerBr(getDetailSet(), 'M')) {
        returnValue = false ;
      }
    }
    if (returnValue) {
      returnValue = PrepMeskla4Del();
    }
    oldRbr = this.getDetailSet().getShort("RBR");
    return returnValue ;
  }

//  public boolean testSpecial(){
//    return rCD.testKalkulacije(getDetailSet(),StanjeUlaz);
//  }


  public boolean PrepMeskla4Del() {
    boolean returnValue = true;
    rCD.prepareFields(getDetailSet());
    findStanja();
    returnValue = rCD.testIzlaz4Del(getDetailSet(),StanjeIzlaz);
    if (returnValue) {

      rKM.stavka.Init();
      rKM.stavkaold.Init();
      lc.TransferFromDB2Class(getDetailSet(),rKM.stavkaold);
      lc.TransferFromDB2Class(StanjeIzlaz,rKM.stanjeiz);
      rKM.kalkStanja();
    }
    else {
      javax.swing.JOptionPane.showMessageDialog(null,
       rCD.errorMessage(),
       "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
       raDM.jtfKOL.requestFocus();
    }
    return returnValue;
  }
  public boolean doBeforeSaveMaster(char mode){
    if (mode=='N') {
      getBrojDokumenta(getMasterSet());
    }
    if (mode != 'B') 
			SanityCheck.basicMeskla(getMasterSet());
    return true;
  }

  public boolean doWithSaveMaster(char mode){
        if (mode=='B') {
          try {
            util.delSeq(srcString, true);
          }
          catch (Exception ex) {
            ex.printStackTrace();
            return false;
          }
        }
        return true;
  }
  
  public boolean doBeforeSaveDetail(char mode) {

    if (mode == 'N') {
        getDetailSet().setString("ID_STAVKA",
            raControlDocs.getKey(getDetailSet(), new String[] { "cskliz", "csklul",
                    "vrdok", "god", "brdok", "rbsid" }, "stmeskla"));
    }
    
    return true;
  }

  public boolean doWithSaveDetail(char mode){

    boolean returnValue = true;

//    if (mode=='N') {
    if (mode!='B') {

      try {
        lc.TransferFromClass2DB(StanjeIzlaz,rKM.stanjeiz);
        rCD.unosIzlaz(getDetailSet(),StanjeIzlaz); //???????
        raTransaction.saveChanges(getDetailSet());
        raTransaction.saveChanges((QueryDataSet) StanjeIzlaz);
      }
      catch (Exception ex) {
        System.out.println(ex);
        returnValue = false ;
      }
    }
//    else if (mode=='I'){}
    else if (mode=='B') {
        
        try {
            val.recountDataSet(raDetail, "RBR", oldRbr, false);
            raTransaction.saveChanges(getDetailSet());
          }
          catch (Exception ex) {
            ex.printStackTrace();
            return false;
          }


      rCD.brisanjeIzlaz(StanjeIzlaz);
      lc.TransferFromClass2DB(StanjeIzlaz,rKM.stanjeiz);
      raTransaction.saveChanges((QueryDataSet) StanjeIzlaz);
      if (raDM.rpcart.getISB().equals("D")) {
        dlgSerBrojevi.getdlgSerBrojevi().deleteSerBr('M');
      }
    }
    return returnValue;
  }

  public void findStanja() {

    AST.findStanjeUnconditional(getMasterSet().getString("GOD"),
        getMasterSet().getString("CSKLIZ"),
                    getDetailSet().getInt("CART"));
    StanjeIzlaz = AST.gettrenSTANJE();
    ASTUL.findStanjeUnconditional(getMasterSet().getString("GOD"),
        getMasterSet().getString("CSKLUL"),
                    getDetailSet().getInt("CART"));
    StanjeUlaz =  ASTUL.gettrenSTANJE();
    rKM.stanjeiz.Init();
    rKM.stanjeul.Init();
    lc.TransferFromDB2Class(StanjeIzlaz,rKM.stanjeiz);
    lc.TransferFromDB2Class(StanjeUlaz,rKM.stanjeul);
    rKM.stanjeiz.sVrSklad=AST.VrstaZaliha(getMasterSet().getString("CSKLIZ"));
    rKM.stanjeul.sVrSklad=ASTUL.VrstaZaliha(getMasterSet().getString("CSKLUL"));

  }

  public void setupPrice(){

    rKM.setupPrice();

  }

  public void Myafter_lookUp(){

    if (raDetail.getMode()!='B') {
      findStanja();
      if (raDetail.getMode()=='N') {
        setupPrice();
        lc.TransferFromClass2DB(getDetailSet(),rKM.stavka);
      }
      findajPorez();
    }
  }

  public void findajPorez() {
    allPorezi.getallPorezi().findCPORART(getDetailSet().getInt("CART"));
    rKM.stavka.reverzpostopor =allPorezi.getallPorezi().gettrenPOREZART().getBigDecimal("UKUNPOR");
    rKM.stavka.postopor =allPorezi.getallPorezi().gettrenPOREZART().getBigDecimal("UKUPOR");
  }

  public void detailSet_navigated(com.borland.dx.dataset.NavigationEvent ne){
    findajPorez();
  }

  public void jtfKOL_focusLost(){
    if (this.raDetail.getMode()=='N' || this.raDetail.getMode()=='I') {
      if (raDM.jtfKOL.isValueChanged()){
        lc.TransferFromDB2Class(getDetailSet(),rKM.stavka);
        rKM.Kalkulacija();
        this.ponistavanjeDijela();
        lc.TransferFromClass2DB(getDetailSet(),rKM.stavka);
      }
      raDM.jtfKOL.selectAll();
    }
  }

  public void PrepMeskla4Add(){
    rKM.stavka.Init();
    rKM.stavkaold.Init();
  }
//// ostatak
  public short findNSTAVKA() {

      return (rbr.vrati_rbr_mes("STMESKLA",getMasterSet().getString("CSKLIZ"),
              getMasterSet().getString("CSKLUL"),
              getMasterSet().getString("VRDOK"),getMasterSet().getString("GOD"),
              getMasterSet().getInt("BRDOK")));

  }

  public String PrepSql(boolean detail){
    String table = "meskla.";
    String sqldodat= " and "+table+"god='"+getMasterSet().getString("GOD")+"' ";
//    if (detail) {
    sqldodat= sqldodat + " and "+table+"brdok = '"+getMasterSet().getInt("BRDOK")+"' ";
    sqldodat= sqldodat + " and "+table+"csklul= '"+getMasterSet().getString("CSKLUL")+"' ";
    sqldodat= sqldodat + " and "+table+"cskliz= '"+getMasterSet().getString("CSKLIZ")+"' ";
//    }
    return sqldodat;
  }

  public void Funkcija_ispisa_Over(boolean what){
    reportsQuerysCollector.getRQCModule().ReSql(PrepSql(what),"MEI");
  }

  public void Funkcija_ispisa_master(){
    Funkcija_ispisa_Over(false);
    super.Funkcija_ispisa_master();
  }

  public void Funkcija_ispisa_detail(){
    Funkcija_ispisa_Over(true);
    super.Funkcija_ispisa_detail();
  }
  public boolean ValDPEscapeDetail(char mode) {
    if (mode=='N') {
      if (this.raDM.rpcart.getCART().trim().equals("")) {
        return true;
      }
      else {

        rKM.stavka.Init();
        rKM.stavkaold.Init();
        lc.TransferFromClass2DB(getDetailSet(),rKM.stavka);
        raDM.enable_rapancart(true);
        raDM.enable_rest(false);
        this.raDM.rpcart.setCART();
        this.raDM.rpcart.SetDefFocus();
        return false;
      }
    }
    else {
      return true;
    }
  }

  public void after_lookUp(){

    Myafter_lookUp();
    MyselectAll();
  }
  public void MyselectAll(){

    if (this.raDetail.getMode()=='N' || this.raDetail.getMode()=='I')
      raDM.jtfKOL.selectAll();
  }

  public void kalk(String how){
    getDetailSet().enableDataSetEvents(false);
    lc.TransferFromDB2Class(getDetailSet(),rKM.stavka);
    if (this.raDetail.getMode()=='I') {
      findajPorez();
      findVrZaliha();
    }
    if (how.equals("INETO") && rKM.stavka.kol.signum() != 0) {
      if (rKM.stanjeul.sVrSklad.equalsIgnoreCase("N")) {
        rKM.stavka.nc = rKM.stavka.zadrazul.divide(rKM.stavka.kol, 4, BigDecimal.ROUND_HALF_UP);
        if ("D".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno","kalkchVC","D"))) {
          how = "PMAR";
        }
        else {
          how = "PMARA";
        }
      } else if (rKM.stanjeul.sVrSklad.equalsIgnoreCase("V")) {
        rKM.stavka.vc = rKM.stavka.zadrazul.divide(rKM.stavka.kol, BigDecimal.ROUND_HALF_UP);
        how = "VC";
      } else if (rKM.stanjeul.sVrSklad.equalsIgnoreCase("M")) {
        rKM.stavka.mc = rKM.stavka.zadrazul.divide(rKM.stavka.kol, BigDecimal.ROUND_HALF_UP);
        how = "MC";
      }
    }
    rKM.kalkPrice(how);
    rKM.Kalkulacija();
    ponistavanjeDijela();
    lc.TransferFromClass2DB(getDetailSet(),rKM.stavka);
    getDetailSet().enableDataSetEvents(true);
  }

  public void findVrZaliha(){
     if (lD.raLocate(dm.getSklad(), "CSKL", getDetailSet().getString("CSKLUL"))){
       rKM.stanjeul.sVrSklad=dm.getSklad().getString("VRZAL");
     } else throw new SanityException("Nepoznata vrsta zalihe skladišta!");
     if (lD.raLocate(dm.getSklad(), "CSKL", getDetailSet().getString("CSKLIZ"))){
       rKM.stanjeiz.sVrSklad=dm.getSklad().getString("VRZAL");
     } else throw new SanityException("Nepoznata vrsta zalihe skladišta!");
  }


  public void kalkVC(){
    kalk("VC");
  }

  public void kalkMC(){
    kalk("MC");
  }

  public void kalkPMAR(){
    kalk("PMAR");
  }

///////////////// klasa panel detaljni
  class jpMesklaDetail2 extends JPanel {

//    rapancart rpcart = new rapancartNew(1){
    rapancart rpcart = new rapancart(1){
      public void nextTofocus(){
        enable_rapancart(false);
        enable_rest(true);
      }
      public void metToDo_after_lookUp(){
        jtfKOL.requestFocus();
        after_lookUp();
      }
    };

  JPanel jpDetailCenter = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  XYLayout xYLayoutDC = new XYLayout();
  XYLayout xYLayoutDC1 = new XYLayout();
	JraTextMultyKolField jtfKOL = new JraTextMultyKolField(){

		public void propertyChange(PropertyChangeEvent evt) {
			
			if (evt.getPropertyName().equalsIgnoreCase("KOL")) 
				jtfKOL_focusLost();
			
		}
        
        public void valueChanged() {
          jtfKOL_focusLost();
        }
     };	
  
  
  JraTextField jraFC = new JraTextField() {
    public void valueChanged() {
      if (!isValueChanged()) return;
      if ("D".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno","kalkchVC","D"))) {
        kalkPMAR();
      }
      else {
        kalk("PMARA");
      }
    }
  };
  JLabel jlKOL = new JLabel();
  JLabel jlFC = new JLabel();
  JLabel jlIznos = new JLabel();
  JraTextField jraINETO = new JraTextField() {
    public void valueChanged() {
      if (!isValueChanged()) return;
      kalk("INETO");
    }
  };
  JTextField focusField = new JTextField();

/////
  JLabel jlFC1 = new JLabel();
  JLabel jlFC2 = new JLabel();
  JLabel jlPMAR = new JLabel();
  JraTextField jraFC1 = new JraTextField() {
    public void valueChanged() {
      if (!isValueChanged()) return;
      kalkVC();
    }
  };
  JraTextField jraFC2 = new JraTextField() {
    public void valueChanged() {
      if (!isValueChanged()) return;
      kalkMC();
    }
  };
  JraTextField jraPMAR = new JraTextField() {
    public void valueChanged() {
      if (!isValueChanged()) return;
      kalkPMAR();
    }
  };

/////

  void jbInit() throws Exception {

//    rpcart.setnextFocusabile(jtfKOL);
    add(rpcart, new XYConstraints(0, 0, 660,90));
    rpcart.setFocusCycleRoot(true);

    jlKOL.setText("Koli\u010Dina ");
    jlFC.setText("Cijena");
    this.setLayout(new XYLayout());
    jlIznos.setText("Iznos");

    jraFC.setColumnName("ZC");
    jtfKOL.setColumnName("KOL");
    jraINETO.setColumnName("ZADRAZIZ");
    /*jtfKOL.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfKOL_focusLost();
      }
    });*/
    addRest();
    rpcart.setMyAfterLookupOnNavigate(false);
  }

  public void add4MEU(){

    setPreferredSize(new Dimension(660,200));
//    jraINETO.setNextFocusableComponent(jtfKOL);  zakomentirano zbog JDK1.4
    jlFC.setText("Nabavna cijena");
    jlPMAR.setText("Razlika u cijeni (%)");
    jlFC1.setText("Cijena bez poreza");
    jlFC2.setText("Cijena s porezom");
    jraFC.setColumnName("NC");
    jraPMAR.setColumnName("PMAR");
    jraFC1.setColumnName("VC");
    jraFC2.setColumnName("MC");
    jraINETO.setColumnName("ZADRAZUL");
    /*jraPMAR.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        if (jraPMAR.isValueChanged()) {
          kalkPMAR();
        }
      }
    });*/
    /*jraFC.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        if (jraFC.isValueChanged()) {
          if ("D".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno","kalkchVC","D"))) {
            kalkPMAR();
          }
          else {
            kalk("PMARA");
          }
       }
      }
    });*/
    /*jraFC1.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        if (jraFC1.isValueChanged()) {
          kalkVC();
        }
      }
    });*/
    /*jraFC2.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        if (jraFC2.isValueChanged()) {
          kalkMC();
        }
      }
    });*/

    jpDetailCenter.setLayout(xYLayoutDC);
    jpDetailCenter.setBorder(BorderFactory.createEtchedBorder());
    jpDetailCenter.setPreferredSize(new Dimension(200, 110));
    add(jpDetailCenter, new XYConstraints(0, 90, 660,220));
    jpDetailCenter.add(jlKOL,    new XYConstraints(20,  15,  -1, -1));
    jpDetailCenter.add(jtfKOL,   new XYConstraints(154, 15, 100, -1));
    jpDetailCenter.add(jlFC,     new XYConstraints(404, 15,  -1, -1));
    jpDetailCenter.add(jraFC,    new XYConstraints(509, 15, 100, -1));
    jpDetailCenter.add(jlPMAR,   new XYConstraints(20,  45,  -1, -1));
    jpDetailCenter.add(jraPMAR,  new XYConstraints(154, 45, 100, -1));
    jpDetailCenter.add(jlFC1,    new XYConstraints(404, 45,  -1, -1));
    jpDetailCenter.add(jraFC1,   new XYConstraints(509, 45, 100, -1));
    jpDetailCenter.add(jlFC2,    new XYConstraints(20,  75,  -1, -1));
    jpDetailCenter.add(jraFC2,   new XYConstraints(154, 75, 100, -1));
    jpDetailCenter.add(jlIznos,  new XYConstraints(404, 75,  -1, -1));
    jpDetailCenter.add(jraINETO, new XYConstraints(509, 75, 100, -1));

  }

  public void add4MEI(){

    setPreferredSize(new Dimension(650, 140));
    jpDetailCenter.setLayout(xYLayoutDC1);
    xYLayoutDC1.setHeight(660);
    xYLayoutDC1.setWidth(90);
    jpDetailCenter.setBorder(BorderFactory.createEtchedBorder());
    jpDetailCenter.add(jlKOL, new XYConstraints(17, 10, -1, -1)); //20,15
    jpDetailCenter.add(jtfKOL, new XYConstraints(108, 10, 130, -1)); //154, 15, 100, -1));
    jpDetailCenter.add(focusField, new XYConstraints(699, 20, 10, -1)); //633
    jpDetailCenter.add(jlFC, new XYConstraints(243, 10, -1, -1));    //(20, 45, -1, -1));
    jpDetailCenter.add(jraFC, new XYConstraints(308, 10, 130, -1)); //154, 45, 100, -1))
    jpDetailCenter.add(jlIznos, new XYConstraints(442, 10, -1, -1));    // 340, 45, -1, -1));
    jpDetailCenter.add(jraINETO, new XYConstraints(502, 10, 130, -1)); //445, 45, 100, -1));

    add(jpDetailCenter, new XYConstraints(0,90, 660, 90  ));

    focusField.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfKOL.requestFocus();
      }
    });
  }

  public void addRest(){}
  public jpMesklaDetail2() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setDataSet(DataSet ds){
    jtfKOL.setDataSet(ds);
    jraFC.setDataSet(ds);
    jraPMAR.setDataSet(ds);
    jraFC1.setDataSet(ds);
    jraFC2.setDataSet(ds);
    jraINETO.setDataSet(ds);
    rpcart.setTabela((QueryDataSet) ds);
    rpcart.setDefParam();
//    rpcart.setSearchable(false);
    rpcart.InitRaPanCart();
  }

  public void enable_rapancart(boolean istina){
    rpcart.EnabDisab(istina);
  }

  public void enable_rest(boolean istina){

    hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(jtfKOL,istina);
//parametrizirati
    if (raIzlazTemplate.allowPriceChange()) {
      hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(jraFC,istina);
    } else {
      hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(jraFC,false);
      //jraFC.setEditable(false);
    }
    hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(jraINETO,false);

  }
 }
 public boolean isKnjigen(){
   return getMasterSet().getString("STATKNJI").equalsIgnoreCase("K") ||
          getMasterSet().getString("STATKNJI").equalsIgnoreCase("P");
 }
 public boolean isPrenesen(){
   return getMasterSet().getString("STATUS").equalsIgnoreCase("P");
 }

 public boolean checkAddEnabled() {
     return !Aut.getAut().isWrongKnjigYear(this, true);
  }

 public boolean checkAccess(){
   if (isKnjigen()) {setUserCheckMsg("Korisnik ne može promijeniti dokument jer je proknjižen !", false);
      return false;
   }
   if (isPrenesen()) {setUserCheckMsg("Korisnik ne može promijeniti dokument jer je prenesen u ili iz druge baze !", false);
      return false;
   }
   if (Aut.getAut().isWrongKnjigYear(this)) return false;
   if (isKPR()) {setUserCheckMsg("Dokument je ušao u knjigu popisa i ne smije se mijenjati !!!", false);
      return false;
   }
   restoreUserCheckMessage();
   return true;
  }

  public boolean isKnjigDataOK(){

    String seqKnjig = "select max(datknj) as datknj from meskla where "+
                      "vrdok='"+getMasterSet().getString("VRDOK")+"' and csklul='"+
                      getMasterSet().getString("CSKLUL")+"' and cskliz='"+
                      getMasterSet().getString("CSKLIZ")+"' and god='"+
                      val.findYear(getMasterSet().getTimestamp("DATDOK"))+"'";
    QueryDataSet qdstmp = hr.restart.util.Util.getNewQueryDataSet(seqKnjig,true);
    if (qdstmp.getRowCount()==0) return true;
//System.out.println(seqKnjig);
//System.out.println("qdstmp.getTimestamp(DATKNJ) "+qdstmp.getTimestamp("DATKNJ"));
    return !getMasterSet().getTimestamp("DATDOK").before(
        hr.restart.util.Util.getUtil().getFirstSecondOfDay(qdstmp
            .getTimestamp("DATKNJ")));

//    return rdu.isGrater(getMasterSet().getTimestamp("DATDOK"),qdstmp.getTimestamp("DATKNJ"));

  }
  public boolean isKPR(){
    return getMasterSet().getString("STAT_KPR").equalsIgnoreCase("D");
  }
}