/****license*****************************************************************
**   file: raMeskla.java
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
import hr.restart.baza.Stanje;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raNavAction;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;

public class raMeskla extends hr.restart.util.raMasterDetail {

  _Main main;
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  rajpMasterMeskla raMM = new rajpMasterMeskla();
  rajpDetailMeskla raDM;
  Rbr rbr = Rbr.getRbr();
  String Godina ;
  String srcString = "";
  String vrCSKLIZ ;
  String vrCSKLUL ;
  java.lang.Integer Broj ;
  short nStavka=99;        // redni broj stavke
  hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
/// iz detailmeskla
  hr.restart.util.LinkClass lc = hr.restart.util.LinkClass.getLinkClass();
  raKalkulBDMeskla rKM = new raKalkulBDMeskla();
//  allStanje AST =  new allStanje();
//  allStanje ASTUL =  allStanje.getallStanje();
  DataSet StanjeIzlaz;
  DataSet StanjeUlaz;
  jpSelectMeskla jpSelectM = selectMES.getPres();
  hr.restart.swing.raTableColumnModifier TCM  ;
  hr.restart.swing.raTableColumnModifier TCM1 ;
  raControlDocs rCD = new raControlDocs();
  short oldRbr=0;
  boolean allowNeg = false;
  
  raNavAction rnvNacinPlac = new raNavAction("PRK u MES",
          raImages.IMGMOVIE, java.awt.event.KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
          ispisiizradaMES();
      }
  };

  raNavAction rnvTrans = new raNavAction("Prebaci sve",
      raImages.IMGSENDMAIL, java.awt.event.KeyEvent.VK_F8) {
  	public void actionPerformed(ActionEvent e) {
      transferCompleteSklad();
  	}
  };

  
  raNavAction rnvKartica = new raNavAction("Kartica artikla",
      raImages.IMGMOVIE, java.awt.event.KeyEvent.VK_F11) {
    public void actionPerformed(ActionEvent e) {
      keyActionShowKartica();
    }
  };

  public void setupColumns(){
/*
    Column[] koljone =  dm.getMesklaMES().getColumns();
    Column[] nove_koljone ;
    for (int i = 0; i< koljone.length; i++){
      dm.getMesklaMES().dropColumn(koljone[i]);
    }



//    koljone[1].getColumnName()


*/
  }

  public raMeskla() {

    setupColumns();

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

    setUserCheck(true);
    String[] key={"CSKLIZ","CSKLUL","VRDOK","GOD","BRDOK"};
    String[] dkey={"CSKLIZ","CSKLUL","VRDOK","GOD","BRDOK","RBR"};
    raDM = new rajpDetailMeskla(this);
    this.setJPanelMaster(this.raMM);
    this.setJPanelDetail(this.raDM);
    this.setNaslovMaster("Me\u0111uskladišnice");
    this.setNaslovDetail("Stavke me\u0111uskladišnice");
    setMasterSet(dm.getMesklaMES());
    setDetailSet(dm.getStmesklaMES());
    raMM.BindComp(getMasterSet());
    this.setMasterKey(key);
    this.setDetailKey(dkey);
    this.setVisibleColsMaster(new int[] {5,6,0,1});
    this.setVisibleColsDetail(new int[] {5,Aut.getAut().getCARTdependable(6,7,8),9,10,11});
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repMeskla","hr.restart.robno.repMeskla","Meskla","Me\u0111uskladišnice-koli\u010Dinske");
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repMeskla","hr.restart.robno.repMeskla","Meskla","Me\u0111uskladišnice-koli\u010Dinske");
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repMesklaSpec","hr.restart.robno.repMeskla","MesklaSpec","Me\u0111uskladišnica s cijenom i popustom");
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repMesklaSpec","hr.restart.robno.repMeskla","MesklaSpec","Me\u0111uskladišnica s cijenom i popustom");
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repMesklaExtendedVersion","hr.restart.robno.repMeskla","MesklaExtendedVersion","Me\u0111uskladišnice-vrijednosne");
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repMesklaExtendedVersion","hr.restart.robno.repMeskla","MesklaExtendedVersion","Me\u0111uskladišnice-vrijednosne");
/*    this.raMaster.getRepRunner().addReport("hr.restart.robno.repMesklaPnP","hr.restart.robno.repMesklaPnP","ROTSifKup","Me\u0111uskladišnica-raèun");
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repMesklaPnP","hr.restart.robno.repMesklaPnP","ROTSifKup","Me\u0111uskladišnica-raèun");*/
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repMesklaNiv","hr.restart.robno.repMesNivel","Nivel","Poravnanje - nivelacija");
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repMesklaNiv","hr.restart.robno.repMesNivel","Nivel","Poravnanje - nivelacija");
    EntryModeinDM();
    raMaster.removeRnvCopyCurr();
    raDetail.removeRnvCopyCurr();
    raDetail.addOption(rnvNacinPlac, 4);
    if (raUser.getInstance().isSuper())
    	raDetail.addOption(rnvTrans, 5, false);
    System.out.println("test meskla kraj");
    rCD.setisNeeded(hr.restart.sisfun.frmParam.getParam("robno",
				"kontKalk", "D",
				"Kontrola ispravosti redoslijeda unosa dokumenata")
				.equalsIgnoreCase("D"));
  }
  public void EntryPointMaster(char mode){}

  public void setSelect(jpSelectMeskla jSM){
    jpSelectM=jSM ;
  }

  public void beforeShowMaster() {
    setNaslovMaster("Meðuskladišnice za skladišta " +
        jpSelectM.getSelRow().getString("CSKLIZ") + " -> " +
        jpSelectM.getSelRow().getString("CSKLUL"));
    allowNeg = frmParam.getParam("robno", "allowNeg", "N", "Dopustiti odlaženje u minus").equals("D");
  }
  
  public void SetFokusMaster (char mode) {

    raMM.jpgetval.initJP(mode); //dodao andrej 02-11-2001 16:37
    raMM.setMode(mode);
    raMM.disabCskl();
    if (mode=='N') {
      jpSelectM.copySelValues();
      raMM.jtfCSKLIZ.forceFocLost();
      raMM.jtfCSKLUL.forceFocLost();
      getMasterSet().setTimestamp("DATDOK",jpSelectM.getSelRow().getTimestamp("DATDOK-to"));
      this.raMM.rajpBrDok.SetTextDOK((getMasterSet().getString("VRDOK")+" - "+getMasterSet().getString("CSKLIZ").trim()+" - "+getMasterSet().getString("CSKLUL").trim()+" / "+
        val.findYear(getMasterSet().getTimestamp("DATDOK"))));
      this.raMM.rajpBrDok.SetStatus();
    }
    raMM.jtfDATDOK.requestFocus();
  }

  public boolean DeleteCheckMaster() {

    boolean returnValue = true;
    srcString=util.getSeqString(getMasterSet());


      if (!util.checkSeq(srcString,Integer.toString(getMasterSet().getInt("BRDOK")))) {
        javax.swing.JOptionPane.showConfirmDialog(null,"Brisati možete samo posljednji dokument !",
                                      "Gre\u0161ka",javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE);
        returnValue = false;
      }
      else {
        if (!this.getDetailSet().isEmpty()) {
          javax.swing.JOptionPane.showConfirmDialog(null,"Nisu pobrisane stavke dokumenta !","Gre\u0161ka",
                                        javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE);
          returnValue = false;
        }
      }
      return returnValue;
  }

  public void AfterSaveMaster(char mode) {}


  public boolean ValidacijaMaster(char mode) {

    if (getMasterSet().getString("CSKLUL").equals(getMasterSet().getString("CSKLIZ"))){
     javax.swing.JOptionPane.showMessageDialog(null,
        "Ista skladišta !","Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
      return false ;
    }
    if (val.isEmpty(this.raMM.jtfDATDOK))
      return false;
//    if (mode=='N') {
//    	getBrojDokumenta(getMasterSet());
//    }
    return true;
  }

  public void getBrojDokumenta(com.borland.dx.dataset.DataSet ds) {
      Godina=val.findYear(getMasterSet().getTimestamp("DATDOK"));
      if (ds.hasColumn("SYSDAT") != null) ds.setTimestamp("SYSDAT", 
            hr.restart.util.Util.getUtil().getCurrentDatabaseTime());
      ds.setString("GOD",Godina);
      Broj=val.findSeqInteger(ds);
      
      ds.setInt("BRDOK",Broj.intValue());
  }

  public boolean ValidacijaDetail(char mode) {
    if (hr.restart.util.Valid.getValid().isEmpty(raDM.jtfKOL)) return false;
    findStanjaiCijene(false);

    int i = rKM.TestStanja();
    rKM.kalkStanja();
    if (i==-1 && !allowNeg){
        javax.swing.JOptionPane.showMessageDialog(null,
            "Koli\u010Dina je ve\u0107a nego koli\u010Dina na zalihi !",
            "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
        return false;
    }
    else {
       if (i == -2 && !allowNeg){
          String rezkol = hr.restart.sisfun.frmParam.getParam("robno","rezkol");
          if (!rezkol.equals("N")){
             javax.swing.JOptionPane.showMessageDialog(null,
              "Koristite rezervirane koli\u010Dine !",
              "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
             if (rezkol.equals("D")) return false;
          }
        }
        if (i>=0 && hr.restart.sisfun.frmParam.getParam("robno","minkol","N").equalsIgnoreCase("D") &&
            dm.getArtikli().getBigDecimal("MINKOL").doubleValue()!=0 &&
            rKM.isKolStanjeManjeOd(dm.getArtikli().getBigDecimal("MINKOL"))) {
              raDM.jtfKOL.requestFocus();
          javax.swing.JOptionPane.showMessageDialog(raDetail.getWindow(),
              "Koli\u010Dina nakon unosa dokumenta je "+rKM.getKolStanjeAfterMat()+" "+
              dm.getArtikli().getString("JM")+" "+
              " i pala je ispod dozvoljene minimalne koli\u010Dine koja iznosi "+
              dm.getArtikli().getBigDecimal("MINKOL") +dm.getArtikli().getString("JM")+" !!!! ",
              "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
          return false;
        }

        if (i>=0 && hr.restart.sisfun.frmParam.getParam("robno","sigkol","N").equalsIgnoreCase("D") &&
            dm.getArtikli().getBigDecimal("SIGKOL").doubleValue()!=0 &&
            rKM.isKolStanjeManjeOd(dm.getArtikli().getBigDecimal("SIGKOL"))){
              raDM.jtfKOL.requestFocus();
          if (!(javax.swing.JOptionPane.showConfirmDialog(raDetail,
              "Koli\u010Dina nakon unosa dokumenta je "+rKM.getKolStanjeAfterMat()+" "+
              dm.getArtikli().getString("JM")+" "+
              " i pala je ispod signalne koli\u010Dine koja iznosi "+
              dm.getArtikli().getBigDecimal("SIGKOL") +dm.getArtikli().getString("JM")+" !!!! Želite li nastaviti ?",
              "Upit",javax.swing.JOptionPane.YES_NO_OPTION,javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.YES_OPTION)){
            return false;
          }
        }

        if (!dlgSerBrojevi.getdlgSerBrojevi().findSB(raDM.rpcart, getDetailSet(), 'M', mode)) {
          return false;
        }
        SanityCheck.basicStmeskla(getDetailSet());
        return true;
    }
  }

  public void EntryPointDetail(char mode) {
    this.raDM.InitRaPanCartDP();
  }

  public void SetFokusDetail(char mode) {

    if (mode=='N') {
      findNSTAVKA();
      getDetailSet().setString("CSKLIZ", getMasterSet().getString("CSKLIZ"));
      getDetailSet().setString("CSKLUL", getMasterSet().getString("CSKLUL"));
      getDetailSet().setString("VRDOK", getMasterSet().getString("VRDOK"));
      getDetailSet().setString("GOD", getMasterSet().getString("GOD"));
      getDetailSet().setInt("BRDOK", getMasterSet().getInt("BRDOK"));
      getDetailSet().setShort("RBR",nStavka);
      getDetailSet().setInt("rbsid",rbr.getRbsID(getDetailSet()));
      this.raDM.enable_rapancart(true);
      this.raDM.enable_rest(false);
      this.raDM.rpcart.SetDefFocus();
      PrepMeskla4Add();
      EntryModeinDM();
    }
    else if (mode=='I') {
      this.raDM.enable_rapancart(false);
      this.raDM.enable_rest(true);
      PrepMeskla4Change();
      raDM.jtfKOL.selectAll();
      raDM.jtfKOL.requestFocus();
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
      if (returnValue && StanjeUlaz.getBigDecimal("KOL").compareTo(
      		getDetailSet().getBigDecimal("KOL")) < 0) {
      	JOptionPane.showConfirmDialog(raDetail.getWindow(),
  					"Brisanje nije moguæe. Kolièina na stanju je manja od kolièine na ovoj stavci!",
  					"Gre\u0161ka", JOptionPane.ERROR_MESSAGE);
  			return false;
      }
    }
    oldRbr = this.getDetailSet().getShort("RBR");
    return returnValue ;
  }

  public void AfterDeleteDetail(){
  //// treba dodati rekaklulacije rbr

//    lc.TransferFromClass2DB(StanjeIzlaz,rKM.stanjeiz);
//    lc.TransferFromClass2DB(StanjeUlaz,rKM.stanjeul);
//    StanjeIzlaz.saveChanges();
//    StanjeUlaz.saveChanges();
    nStavka=99;
    findNSTAVKA();
//    if (raDM.rpcart.getISB().equals("D")) {
//      dlgSerBrojevi.getdlgSerBrojevi().deleteSerBr('M');
//    }
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
    if (mode=='B') {
      lc.TransferFromClass2DB(StanjeIzlaz,rKM.stanjeiz);
      rCD.brisanjeIzlaz(StanjeIzlaz);
      raTransaction.saveChanges((QueryDataSet) StanjeIzlaz);

      lc.TransferFromClass2DB(StanjeUlaz,rKM.stanjeul);
      rCD.brisanjeKalkulacije(StanjeUlaz);
      raTransaction.saveChanges((QueryDataSet) StanjeUlaz);

      if (raDM.rpcart.getISB().equals("D")) {
        dlgSerBrojevi.getdlgSerBrojevi().deleteSerBr('M');
      }
      try {
        val.recountDataSet(raDetail, "RBR", oldRbr, false);
        raTransaction.saveChanges(getDetailSet());
      }
      catch (Exception ex) {
        ex.printStackTrace();
        return false;
        }
    }
    else {   /// mode 'N' i 'I'

      try {
      	lc.printAll(rKM.stanjeiz);
        lc.TransferFromClass2DB(StanjeIzlaz,rKM.stanjeiz);
        rCD.unosIzlaz(getDetailSet(),StanjeIzlaz); //???????
        raTransaction.saveChanges((QueryDataSet) StanjeIzlaz);
        if (StanjeUlaz.getRowCount()==0) {
           StanjeUlaz.insertRow(true);
           StanjeUlaz.setString("CSKL",getMasterSet().getString("CSKLUL"));
           StanjeUlaz.setString("GOD",getMasterSet().getString("GOD"));
           StanjeUlaz.setInt("CART",getDetailSet().getInt("CART"));
        }
        lc.TransferFromClass2DB(StanjeUlaz,rKM.stanjeul);
        if (mode=='N') {
          rCD.unosKalkulacije(getDetailSet(),StanjeUlaz); //???????
        } // else 
         
        StanjeUlaz.setTimestamp("DATZK", 
            getMasterSet().getTimestamp("DATDOK"));
        raTransaction.saveChanges((QueryDataSet) StanjeUlaz);
        raTransaction.saveChanges(getDetailSet());
      }
      catch (Exception ex) {
        System.out.println(ex);
        returnValue = false ;
      }
    }
    return returnValue;
  }

  public void AfterSaveDetail(char mode) {
    if (mode=='N') nStavka=(short)(nStavka+1);
  }

  void findNSTAVKA() {
//    if (this.nStavka==99)
      nStavka=rbr.vrati_rbr_mes("STMESKLA",getMasterSet().getString("CSKLIZ"),
              getMasterSet().getString("CSKLUL"),
              getMasterSet().getString("VRDOK"),getMasterSet().getString("GOD"),
              getMasterSet().getInt("BRDOK"));

//    return (rbr.vrati_rbr_mes("STMESKLA",getMasterSet().getString("CSKLIZ"),
//             getMasterSet().getString("CSKLUL"),
//             getMasterSet().getString("VRDOK"),getMasterSet().getString("GOD"),
//              getMasterSet().getInt("BRDOK")));

  }

  public boolean ValDPEscapeDetail(char mode) {
    if (mode=='N') {
      if (this.raDM.rpcart.getCART().trim().equals("")) {
        return true;
      }
      else {
        ClearStmeskla();
        raDM.TDS.Clean();
        this.raDM.enable_rapancart(true);
        this.raDM.enable_rest(false);
        this.raDM.rpcart.setCART();
        this.raDM.rpcart.SetDefFocus();
        return false;
      }
    }
    else {
      this.raDM.InitRaPanCartDP();
      return true;
    }
  }

  public String PrepSql(boolean detail){
    String sqldodat= "and meskla.csklul='"+getMasterSet().getString("CSKLUL")+
                     "' and meskla.cskliz='"+getMasterSet().getString("CSKLIZ")+"' "+

 					 "and meskla.god = '" + getMasterSet().getString("GOD")+
 					 "' ";
    if (detail) {
       sqldodat= sqldodat + " and meskla.brdok = '"+getMasterSet().getInt("BRDOK")+"' ";
    }
    return sqldodat;
  }

  public void Funkcija_ispisa_master(){

    reportsQuerysCollector.getRQCModule().ReSql(PrepSql(true),"MES");
    super.Funkcija_ispisa_master();

  }
  public void Funkcija_ispisa_detail(){
    reportsQuerysCollector.getRQCModule().ReSql(PrepSql(true),"MES");
    super.Funkcija_ispisa_detail();
  }

/// preba\u010Daj iz detailmeskla
  public void ClearStmeskla(){
    rKM.stavka.Init();
    rKM.stavkaold.Init();
    lc.TransferFromClass2DB(getDetailSet(),rKM.stavka);
  }

  public void findStanja(){


//    AST.findStanjeUnconditional(getDetailSet().getString("GOD"),
//                    getDetailSet().getString("CSKLIZ"),getDetailSet().getInt("CART"));
//    StanjeIzlaz = AST.gettrenSTANJE();
    StanjeIzlaz = hr.restart.util.Util.getNewQueryDataSet(
        "select * from stanje where god = '"+getDetailSet().getString("GOD")+
        "' and cskl='"+getDetailSet().getString("CSKLIZ")+
        "' and cart = "+getDetailSet().getInt("CART"));
    if (StanjeIzlaz.rowCount() == 0) {
    	StanjeIzlaz.insertRow(false);
      dM.copyColumns(getDetailSet(), StanjeIzlaz, 
              new String[] {"GOD", "CART"});
      StanjeIzlaz.setString("CSKL", getDetailSet().getString("CSKLIZ"));
      StanjeIzlaz.post();
    }
    //SanityCheck.stanjeArt(StanjeIzlaz, getDetailSet(), "CSKLIZ");

//    ASTUL.gettrenSTANJE(getDetailSet().getString("GOD"),
//                    getDetailSet().getString("CSKLUL"),getDetailSet().getInt("CART"));
//    StanjeUlaz =  ASTUL.gettrenSTANJE();
    StanjeUlaz = hr.restart.util.Util.getNewQueryDataSet(
        "select * from stanje where god = '"+getDetailSet().getString("GOD")+
        "' and cskl='"+getDetailSet().getString("CSKLUL")+
        "' and cart = "+getDetailSet().getInt("CART"));

    rKM.stanjeiz.Init();
    rKM.stanjeul.Init();

    lc.TransferFromDB2Class(StanjeIzlaz,rKM.stanjeiz);
    lc.TransferFromDB2Class(StanjeUlaz,rKM.stanjeul);

    rKM.stanjeiz.sVrSklad=allStanje.VrstaZalihaA(getDetailSet().getString("CSKLIZ"));
    rKM.stanjeul.sVrSklad=allStanje.VrstaZalihaA(getDetailSet().getString("CSKLUL"));
    raDM.TDS.Clean();
    allPorezi.getallPorezi().findCPORART(getDetailSet().getInt("CART"));
    rKM.stavka.reverzpostopor =allPorezi.getallPorezi().gettrenPOREZART().getBigDecimal("UKUNPOR");
    rKM.stavka.postopor =allPorezi.getallPorezi().gettrenPOREZART().getBigDecimal("UKUPOR");
  }

  public void findStanjaiCijene(boolean fetch){

    findStanja();
    raDM.TDS.SetMesklaTempSet(StanjeUlaz,StanjeIzlaz);
    if(fetch){
    	rKM.setupOldPrice();
      rKM.setupPrice();
        getDetailSet().setBigDecimal("ZC",rKM.stavka.zc);
        getDetailSet().setBigDecimal("ZCUL",rKM.stavka.zcul);
        getDetailSet().setBigDecimal("FC", StanjeIzlaz.getBigDecimal("VC"));
    } else {
    	izlazCijena();
    	ulazCijena();
    }
    
System.out.println("poc findStanjaiCijene()");
System.out.println("stanjeiz");
lc.printAll(rKM.stanjeiz);
System.out.println("stanjeul");
lc.printAll(rKM.stanjeul);
System.out.println("stavka");
lc.printAll(rKM.stavka);
//System.out.println("StanjeUlaz");
//ST.prn(StanjeUlaz);
//System.out.println("StanjeIzlaz");
//ST.prn(StanjeIzlaz);
  if (fetch && allowNeg) {
  	/*if (rKM.stanjeiz.kol.compareTo(rKM.stavkaold.kol) != 0)
  		raCommonClass.getraCommonClass().setLabelLaF(raDM.jraFC, false);*/
  	if (!rKM.stanjeiz.sVrSklad.equals("N") || rKM.stanjeul.sVrSklad.equals("N"))
  		raCommonClass.getraCommonClass().setLabelLaF(raDM.jraZCUL, false);
  }
System.out.println("kraj findStanjaiCijene()");
  }
  
  public void izlazCijena() {
  	rKM.stavka.zc = getDetailSet().getBigDecimal("ZC");
  	rKM.alterIzlaz();
  	kalk();
  }
  
  public void ulazCijena() {
  	rKM.stavka.zcul = getDetailSet().getBigDecimal("ZCUL");
  	rKM.alterUlaz();
  	kalk();
  }

  public void jtfKOL_focusLost(java.awt.event.FocusEvent e){

    if (raDM.jtfKOL.isValueChanged()){
//      lc.setBDField("KOL",getDetailSet().getBigDecimal("KOL"),rKM.stavka);
      rKM.stavka.kol = getDetailSet().getBigDecimal("KOL");
//lc.printAll(rKM.stavka);
      kalk();
    }
  }
  
  void kalk() {
  	rKM.Kalkulacija();
  	lc.TransferFromClass2DB(getDetailSet(),rKM.stavka);
//  	ST.prn(getDetailSet());

  	      if (raDetail.getMode()=='N') {
  	        raDM.TDS.AddSubStanje(getDetailSet().getBigDecimal("KOL"),
  	          getDetailSet().getBigDecimal("ZADRAZUL"),
  	          getDetailSet().getBigDecimal("ZADRAZIZ"));
  	      } else if (raDetail.getMode()=='I') {
  	        raDM.TDS.AddSubStanje(getDetailSet().getBigDecimal("KOL").
  	                subtract(rKM.stavkaold.kol),
  	          getDetailSet().getBigDecimal("ZADRAZUL").
  	                subtract(rKM.stavkaold.zadrazul),
  	          getDetailSet().getBigDecimal("ZADRAZIZ").
  	                subtract(rKM.stavkaold.zadraziz));
  	      }
  }

  public void PrepMeskla4Change(){
    rKM.stavka.Init();
    rKM.stavkaold.Init();
    lc.TransferFromDB2Class(getDetailSet(),rKM.stavka);
    lc.TransferFromDB2Class(getDetailSet(),rKM.stavkaold);

  }
  
  public void transferCompleteSklad() {
  	raProcess.runChild(raDetail, new Runnable() {
  		public void run() {
  			raDetail.getJpTableView().enableEvents(false);
  			try {
  				transferCompleteSklad_proc();
  			} finally {
  				raDetail.getJpTableView().enableEvents(true);	
  			}
  		}
  	});
  }
  
  void transferCompleteSklad_proc() {
  	raProcess.setMessage("Priprema podataka za meðuskladišnicu ... ", true);
  	
  	String izv = allStanje.VrstaZalihaA(getMasterSet().getString("CSKLIZ"));
  	String ulv = allStanje.VrstaZalihaA(getMasterSet().getString("CSKLUL"));
  	String[] mkey = {"CSKLIZ", "CSKLUL", "VRDOK", "GOD", "BRDOK"};
  	String[] akey = {"CART", "CART1", "BC", "NAZART", "JM"};
  	short mrbr = 0;
  	
  	lookupData ld = lookupData.getlookupData();
  	
  	raProcess.setMessage("Dohvat stanja sa skladišta ... ", false);
  	QueryDataSet izlazAll = Stanje.getDataModule().getTempSet("CART",
  			Condition.equal("CSKL", getMasterSet().getString("CSKLIZ")).
  			and(Condition.equal("GOD", getMasterSet())).and(
  					Condition.where("KOL", Condition.GREATER_THAN, Aus.zero0)));
  	raProcess.openScratchDataSet(izlazAll);
  	izlazAll.setSort(new SortDescriptor(new String[] {"CART"}));
  	int total = izlazAll.rowCount();
  	int mess = Math.min(17, (int) Math.round(Math.sqrt(total)) + 1);
  	int row = 0;
  	
  	for (izlazAll.first(); izlazAll.inBounds(); izlazAll.next()) {
  		raProcess.checkClosing();
      if (row++ % mess == 0)
      	raProcess.setMessage("Obrada artikla " + row + "/" + total + " ... ", false);
      
      if (!ld.raLocate(dm.getArtikli(), "CART", Integer.toString(izlazAll.getInt("CART")))) continue;
      if (!ld.raLocate(dm.getPorezi(), "CPOR", dm.getArtikli().getString("CPOR"))) continue;
            
      QueryDataSet izlaz = Stanje.getDataModule().getTempSet(
    			Condition.equal("CSKL", getMasterSet().getString("CSKLIZ")).
    			and(Condition.equal("GOD", getMasterSet())).and(
    					Condition.equal("CART", izlazAll)));
      izlaz.open();
      
      if (izlaz.rowCount() == 0 || izlaz.getBigDecimal("KOL").signum() <= 0) continue;
      
      QueryDataSet ulaz = Stanje.getDataModule().getTempSet(
    			Condition.equal("CSKL", getMasterSet().getString("CSKLUL")).
    			and(Condition.equal("GOD", getMasterSet())).and(
    					Condition.equal("CART", izlazAll)));
      ulaz.open();
      
      getDetailSet().insertRow(false);
      dM.copyColumns(getMasterSet(), getDetailSet(), mkey);
      dM.copyColumns(dm.getArtikli(), getDetailSet(), akey);
      getDetailSet().setShort("RBR", ++mrbr);
      
      PrepMeskla4Add();
      rKM.stanjeiz.Init();
      rKM.stanjeul.Init();
      lc.TransferFromDB2Class(izlaz,rKM.stanjeiz);
      lc.TransferFromDB2Class(ulaz,rKM.stanjeul);
      rKM.stanjeiz.sVrSklad=izv;
      rKM.stanjeul.sVrSklad=ulv;
      
      rKM.stavka.reverzpostopor = dm.getPorezi().getBigDecimal("UKUNPOR");
      rKM.stavka.postopor = dm.getPorezi().getBigDecimal("UKUPOR");

      rKM.setupOldPrice();
      rKM.setupPrice();
      
      rKM.stavka.kol = izlaz.getBigDecimal("KOL");

      rKM.Kalkulacija();
      lc.TransferFromClass2DB(getDetailSet(), rKM.stavka);
      
      rKM.kalkStanja();
      
      lc.TransferFromClass2DB(izlaz, rKM.stanjeiz);
      rCD.unosIzlaz(getDetailSet(), izlaz); //???????

      if (ulaz.getRowCount() == 0) {
      	ulaz.insertRow(true);
      	ulaz.setString("CSKL", getMasterSet().getString("CSKLUL"));
      	ulaz.setString("GOD", getMasterSet().getString("GOD"));
      	ulaz.setInt("CART", izlazAll.getInt("CART"));
      }
      lc.TransferFromClass2DB(ulaz, rKM.stanjeul);
      rCD.unosKalkulacije(getDetailSet(), ulaz); 
       
      ulaz.setTimestamp("DATZK", getMasterSet().getTimestamp("DATDOK"));
      raTransaction.saveChangesInTransaction(new QueryDataSet[] {ulaz, izlaz, getDetailSet()});
  	}

  }

  public boolean PrepMeskla4Del() {
    boolean returnValue= true;
    rCD.prepareFields(getDetailSet());
    findStanja();
    SanityCheck.stanjeArt(StanjeUlaz, getDetailSet(), "CSKLUL");
    returnValue = rCD.testIzlaz4Del(getDetailSet(),StanjeIzlaz);
    if (returnValue) {
      returnValue = rCD.testKalkulacije(getDetailSet(),StanjeUlaz);
    }

    if (returnValue) {
      rKM.stavka.Init();
      rKM.stavkaold.Init();
      lc.TransferFromDB2Class(getDetailSet(),rKM.stavkaold);
      lc.TransferFromDB2Class(StanjeUlaz,rKM.stanjeul);
      rKM.kalkStanja();
      rKM.returnOldPrice();
    }
    else {
      javax.swing.JOptionPane.showMessageDialog(null,
       rCD.errorMessage(),
       "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
       raDM.jtfKOL.requestFocus();
    }
    return returnValue;
  }

  public void PrepMeskla4Add(){
    rKM.stavka.Init();
    rKM.stavkaold.Init();
  }

  public void EntryModeinDM(){
    raDM.TDS.Clean();
    raDM.BindMeskla();
  }
  public boolean isKnjigen(){
    return getMasterSet().getString("STATKNJI").equalsIgnoreCase("K") ||
           getMasterSet().getString("STATKNJI").equalsIgnoreCase("P") ||
           getMasterSet().getString("STATKNJU").equalsIgnoreCase("K") ||
           getMasterSet().getString("STATKNJU").equalsIgnoreCase("P");
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
  public boolean isKPR(){
    return getMasterSet().getString("STAT_KPR").equalsIgnoreCase("D");
  }
  
  public void masterSet_navigated(com.borland.dx.dataset.NavigationEvent ne) {
    this.setNaslovDetail("Stavke meðuskladišnice "+
        getMasterSet().getString("CSKLIZ").trim()+" - "+
        getMasterSet().getString("CSKLUL").trim()+" / "+
        getMasterSet().getString("GOD") + " - " + 
        getMasterSet().getInt("BRDOK"));
  }
  
  void keyActionShowKartica() {
    if (getDetailSet().rowCount() == 0) return;
    
    String[] opts = {"Izlazno", "Ulazno", "Prekid"};
    
    int resp = JOptionPane.showOptionDialog(raDetail.getWindow(), 
        "Odaberite skladište za prikaz kartice:", "Kartica artikla",
        0, JOptionPane.QUESTION_MESSAGE, null, opts, opts[0]);
    
  }
  
  public void ispisiizradaMES(){
      
      Prk2MesGui prk2mes = new Prk2MesGui(){
          void interFunc(String cskl,String god,String vrdok,int brdok){
              operativaisnimanjeAutMesa(cskl,god,vrdok,brdok);
          }
      };
      prk2mes.setSkladiste(getMasterSet().getString("CSKLIZ"));
      prk2mes.setGodina(getMasterSet().getString("GOD"));
      prk2mes.findAll();
      prk2mes.setVisible(true);
      
  }
  
  public String greska ="";
  public void operativaisnimanjeAutMesa(final String cskl,
                                        final String god,
                                        final String vrdok,
                                        final int brdok){
      greska ="";
      
      raLocalTransaction rLT = new raLocalTransaction(){

          public boolean transaction() throws Exception {

              QueryDataSet primka = hr.restart.util.Util.getNewQueryDataSet(
                      "select * from doku where cskl='"+cskl+"' and vrdok='"+vrdok+"' and god='"+god+"' and brdok="+brdok);
              QueryDataSet stprimka = hr.restart.util.Util.getNewQueryDataSet(
                      "select * from stdoku where cskl='"+cskl+"' and vrdok='"+vrdok+"' and god='"+god+"' and brdok="+brdok);
      
              Primka2Meskla p2m = new Primka2Meskla();
              p2m.initALL(stprimka,getDetailSet(),getMasterSet());
              p2m.p2m();
      
              primka.setString("STATPLA","K");
              raTransaction.saveChanges(p2m.stanjeiz);
              raTransaction.saveChanges(p2m.stanjeul);
              raTransaction.saveChanges(p2m.stmeskla);
              raTransaction.saveChanges(primka);
              
              for (p2m.stanjeiz.first();p2m.stanjeiz.inBounds();p2m.stanjeiz.next()){
                  if (p2m.stanjeiz.getBigDecimal("KOL").compareTo(Aus.zero2)<0){
                      QueryDataSet as = hr.restart.util.Util.getNewQueryDataSet(
                              "select * from artikli where cart="+p2m.stanjeiz.getInt("CART"));
                      
                      if (!greska.equalsIgnoreCase("")) greska = greska+",";
                      greska = greska+" "+as.getString("NAZART");
                  }
              }
              if (!greska.equalsIgnoreCase("")) return false;
              return true;
        }};
      
      boolean res = rLT.execTransaction();
      if (res) {
          javax.swing.JOptionPane.showMessageDialog(this,
                  "Prijenos uspješno obavljen",
                  "Greška",javax.swing.JOptionPane.INFORMATION_MESSAGE);
           
          
      } else {
          if (!greska.equalsIgnoreCase("")) {
          javax.swing.JOptionPane.showMessageDialog(null,
                  "Nedovoljno stanje za "+greska,
                  "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
          } else {
              javax.swing.JOptionPane.showMessageDialog(null,
                      "Prijenos nije uspio !"+greska,
                      "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
          }
      }
      
      SwingUtilities.invokeLater(new Runnable(){
        public void run() {
            getDetailSet().refresh();
            raDetail.refreshTable();
            raDetail.getColumnsBean().rnvRefresh.actionPerformed(null);
//            raDetail.jpTableView.getColumnsBean().initialize();
        }});
      
      
  }
}
