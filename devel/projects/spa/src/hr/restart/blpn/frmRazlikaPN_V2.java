/****license*****************************************************************
**   file: frmRazlikaPN_V2.java
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
import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raTransaction;
import hr.restart.util.startFrame;
import hr.restart.util.sysoutTEST;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class frmRazlikaPN_V2 extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  QueryDataSet repQDS = new QueryDataSet();
  QueryDataSet repQDSall = new QueryDataSet();
  StorageDataSet fromPres;
  StorageDataSet directIspl = new StorageDataSet();
  Util ut = Util.getUtil();
  sgStuff ss = sgStuff.getStugg();
//  PreSelect psel;
  static StorageDataSet razlika = new StorageDataSet();
  String updateStavPN;

//  private String currCpn = "";

  jpRazlikaPN_V2 jpDetail;

  static frmRazlikaPN_V2 frpn;

  public frmRazlikaPN_V2(StorageDataSet exPress) {
//    super(2);
    this.fromPres = exPress;
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(this.fromPres);
    frpn = this;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmRazlikaPN_V2 getFRPN(){
    return frpn;
  }

//  public void setPreSelect(PreSelect prs){
//    psel = prs;
//  }

//  public PreSelect getPreSelect(){
//    return psel;
//  }

  public void beforeShow(){
//    System.out.println("frmRazlikaPN_V2 - BEFORESHOW");
    startFrame.getStartFrame().centerFrame(this, 0 ,"");
    setTitle("Uplata - isplata razlike po putnom nalogu broj " + fromPres.getString("CPN"));
    if (!razlika.isOpen()){
      razlika.setColumns(new Column[] {
        dm.createStringColumn("VALUTA", 3),
        dm.createBigDecimalColumn("RAZLIKA")
      });
      razlika.open();
    }
    razlika.insertRow(false);
  }
  
  

  public DataSet getDirectIsplata(){
    return directIspl;
  }

  public void EntryPoint(char mode){
    rcc.setLabelLaF(jpDetail.jraIzdatak, false);
    rcc.setLabelLaF(jpDetail.jraPrimitak, false);
    rcc.setLabelLaF(jpDetail.jraCpn, false);
    rcc.setLabelLaF(jpDetail.jlrCblag, false);
    rcc.setLabelLaF(jpDetail.jlrNaziv, false);
    rcc.setLabelLaF(jpDetail.jlrOznval, false);
    directIspl.deleteAllRows();
    initial();
    paliGasi();
    if (mode == 'N') {
      this.getRaQueryDataSet().setInt("CBLAG", fromPres.getInt("CBLAG"));
      this.getRaQueryDataSet().setString("OZNVAL", fromPres.getString("OZNVAL"));
      this.getRaQueryDataSet().setString("CRADNIK", fromPres.getString("CRADNIK"));
      this.getRaQueryDataSet().setString("CPN", fromPres.getString("CPN"));
      this.getRaQueryDataSet().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG());
      this.getRaQueryDataSet().setTimestamp("DATUM", ut.clearTime(vl.getToday()));
      this.getRaQueryDataSet().setString("VRDOK", "BL");
      //this.getRaQueryDataSet().setShort("GODINA", Short.parseShort(vl.findYear(vl.getToday())));
      //datasets();
      initSets();
    }
  }

  public void refreshTable() {
    // TODO Auto-generated method stub
    super.refreshTable();
  }
  
  public void SetFokus(char mode) {
//    System.out.println("frmRazlikaPN_V2 - SETFOKUS");
    if (mode == 'N') {
      this.getRaQueryDataSet().setInt("CBLAG", fromPres.getInt("CBLAG"));
      this.getRaQueryDataSet().setString("OZNVAL", fromPres.getString("OZNVAL"));
      this.getRaQueryDataSet().setString("CRADNIK", fromPres.getString("CRADNIK"));
      this.getRaQueryDataSet().setString("CPN", fromPres.getString("CPN"));
      this.getRaQueryDataSet().setString("CRADNIK", fromPres.getString("CRADNIK"));
      this.getRaQueryDataSet().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG());
      this.getRaQueryDataSet().setTimestamp("DATUM", ut.clearTime(vl.getToday()));
      this.getRaQueryDataSet().setString("VRDOK", "BL");
      //this.getRaQueryDataSet().setShort("GODINA", Short.parseShort(vl.findYear(vl.getToday())));
      jpDetail.jraDatum.requestFocus();
    }
  }

  public boolean Validacija(char mode) {

    if(jpDetail.jcbUValuti.isSelected() && vl.isEmpty(jpDetail.jlrValuta)) return false;
    if(jpDetail.jcbStavka.isSelected() && vl.isEmpty(jpDetail.jlrRbs)) return false;

    if (this.getRaQueryDataSet().getBigDecimal("PRIMITAK").compareTo(new BigDecimal(0)) == 0 &&
        this.getRaQueryDataSet().getBigDecimal("IZDATAK").compareTo(new BigDecimal(0)) == 0) return false;
    
    getRaQueryDataSet().setShort("GODINA", Short.parseShort(vl.findYear(getRaQueryDataSet().getTimestamp("DATUM"))));
    
    int brizv = ss.getBrizv(this.getRaQueryDataSet().getInt("CBLAG"), this.getRaQueryDataSet().getString("KNJIG"),
                    this.getRaQueryDataSet().getString("OZNVAL"), this.getRaQueryDataSet().getShort("GODINA"));
    if (brizv == 0) {
      jpDetail.jlrCblag.requestFocus();
      JOptionPane.showConfirmDialog(this.jpDetail, "Nema otvorenog izvješ\u0107a za blagajnu \"".concat(this.jpDetail.jlrNaziv.getText()).concat("\"") , "Upozorenje", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      return false;
    }
    if (!this.getRaQueryDataSet().getString("OZNVAL").equals(hr.restart.zapod.Tecajevi.getDomOZNVAL())){
      this.getRaQueryDataSet().setBigDecimal("TECAJ", ss.getTECAJ(this.getRaQueryDataSet().getString("OZNVAL"),this.getRaQueryDataSet().getTimestamp("DATUM")));
      BigDecimal pvizd = ss.currrencyConverterToKN(this.getRaQueryDataSet().getBigDecimal("IZDATAK"),this.getRaQueryDataSet().getString("OZNVAL"),this.getRaQueryDataSet().getTimestamp("DATUM"));
      this.getRaQueryDataSet().setBigDecimal("PVIZDATAK", pvizd);
      BigDecimal pvpri = ss.currrencyConverterToKN(this.getRaQueryDataSet().getBigDecimal("PRIMITAK"),this.getRaQueryDataSet().getString("OZNVAL"),this.getRaQueryDataSet().getTimestamp("DATUM"));
      this.getRaQueryDataSet().setBigDecimal("PVPRIMITAK", pvpri);
    } else {
      this.getRaQueryDataSet().setBigDecimal("PVIZDATAK", this.getRaQueryDataSet().getBigDecimal("IZDATAK"));
      this.getRaQueryDataSet().setBigDecimal("PVPRIMITAK", this.getRaQueryDataSet().getBigDecimal("PRIMITAK"));
    }
    this.getRaQueryDataSet().setString("TKO", ss.getIme(this.getRaQueryDataSet().getString("CRADNIK")));
    String opis = "Razlika po PN ".concat(this.getRaQueryDataSet().getString("CPN")); //.concat(" u ").concat(this.getRaQueryDataSet().getString("OZNVAL"));
    this.getRaQueryDataSet().setString("CORG", hr.restart.zapod.OrgStr.getKNJCORG());
    this.getRaQueryDataSet().setString("OPIS", opis);
    this.getRaQueryDataSet().setInt("BRIZV", brizv);
    String vrsta = "";
    if (this.getRaQueryDataSet().getBigDecimal("PRIMITAK").compareTo(Aus.zero2) != 0) vrsta = "U";
    else vrsta = "I";
//    System.out.println("vrsta - " + vrsta);
    this.getRaQueryDataSet().setString("VRSTA", vrsta);
    this.getRaQueryDataSet().setInt("RBS", ss.getNextRBSstavblag(this.getRaQueryDataSet().getString("KNJIG"),
                                    this.getRaQueryDataSet().getInt("CBLAG"),
                                    this.getRaQueryDataSet().getString("OZNVAL"),
                                    this.getRaQueryDataSet().getShort("GODINA"),
                                    brizv,vrsta));
    this.getRaQueryDataSet().setString("CSKL", "6");
    this.getRaQueryDataSet().setString("STAVKA", "2");

//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(this.getRaQueryDataSet());

    return true; // false; /** @todo ispravit u konacnici!!! */
  }

//  public boolean doWithSave(char mode){
//    try {
//      raTransaction.runSQL(updateStavPN);
//      return true;
//    }
//    catch (Exception ex) {
//      return false;
//    }
//  }

  public void AfterSave(char mode){
    ss.setNeisplaceneStavkePN(fromPres.getString("CPN"), true);
    ss.setNeisplaceneStavkeUValPN(fromPres.getString("CPN"), fromPres.getString("OZNVAL"), true);

    ss.updatePutniNalogObracunUplaceno(this.getRaQueryDataSet().getString("CPN"),this.getRaQueryDataSet().getBigDecimal("PVPRIMITAK").subtract(this.getRaQueryDataSet().getBigDecimal("PVIZDATAK")));

    String knjig = this.getRaQueryDataSet().getString("KNJIG");
    String oznval = this.getRaQueryDataSet().getString("OZNVAL");
    int cblag = this.getRaQueryDataSet().getInt("CBLAG");
    int brizv = this.getRaQueryDataSet().getInt("BRIZV");
    short god = this.getRaQueryDataSet().getShort("GODINA");
    BigDecimal izdatak = this.getRaQueryDataSet().getBigDecimal("IZDATAK");
    BigDecimal pvizdatak = this.getRaQueryDataSet().getBigDecimal("PVIZDATAK");
    BigDecimal primitak = this.getRaQueryDataSet().getBigDecimal("PRIMITAK");
    BigDecimal pvprimitak = this.getRaQueryDataSet().getBigDecimal("PVPRIMITAK");
    Timestamp datum = this.getRaQueryDataSet().getTimestamp("DATUM");
    ss.updateBlagizvPriAkontacijiRazlici(knjig, oznval, cblag, brizv, god, izdatak, primitak, pvizdatak, pvprimitak, datum);

    kontrola();
    this.getOKpanel().jPrekid_actionPerformed();
  }
  
  public void initSets(){
    datasets();
    clc();
  }

  void kontrola(){
    initSets();
    if (preostalaRazlikaIzObracuna.compareTo(NULLA)==0 || preostalaRazlikaIzObracunaPV.compareTo(NULLA)==0){
      ss.setPutninalogIsplacen(this.getRaQueryDataSet().getString("CPN"));
      ss.calculateTecRazlika(this.getRaQueryDataSet().getString("CPN"));
      this.setEnabledNavAction(this.getNavBar().getNavContainer().getNavActions()[0],false);
      JOptionPane.showMessageDialog(this.getWindow(),
                                    "Putni nalog je ispla\u0107en",
                                    "Obavijest", JOptionPane.INFORMATION_MESSAGE);

      int arhiviranje = JOptionPane.showConfirmDialog(this.getWindow(),
          "Arhiviati putni nalog "+fromPres.getString("CPN")+"?",
          "Arhiviranje",
          JOptionPane.YES_NO_OPTION);

      if (arhiviranje == JOptionPane.YES_OPTION){
//        System.out.println("ARHIVIRANJE!!!");
        arhiviranjeNaloga();
      }
    }
  }

  private void arhiviranjeNaloga(){
    String[] koloneMaster = new String[]
    {"LOKK", "AKTIV", "KNJIG", "GODINA", "BROJ", "CPN", "CRADNIK", "DATUMODL",
      "DATOBR", "TRAJANJE", "RAZLOGPUTA", "MJESTA", "CPRIJSRED", "INDPUTA", "STATUS", "AKONTACIJA", "TROSKOVI",
      "RAZLIKA", "UPLRAZLIKA", "TECRAZLIKA", "CZEMLJE", "CORG"};

    String[] koloneDitelj = new String[]
    {"LOKK","AKTIV","KNJIG","GODINA","BROJ", "RBS","STAVKA","CSKL","VRDOK",
      "CZEMLJE","IZNOS","PVIZNOS","TECAJ","OZNVAL", "ODMJ","DOMJ","CPRIJSRED",
      "BROJDNK","DATUMODL","DATUMDOL","VRIJODL","VRIJDOL", "BROJSATI","INDPUTA",
      "CPN","ISPL"};

    QueryDataSet dummyMaster = hr.restart.baza.PutniNalog.getDataModule().getTempSet(Condition.equal("CPN",getRaQueryDataSet()));
    QueryDataSet dummyDetail = hr.restart.baza.Stavkepn.getDataModule().getTempSet(Condition.equal("CPN",getRaQueryDataSet()));
    QueryDataSet dummyArchMaster = hr.restart.baza.Putnalarh.getDataModule().getTempSet("1=0");
    QueryDataSet dummyArchDetail = hr.restart.baza.Stavpnarh.getDataModule().getTempSet("1=0");

    dummyMaster.open();
    dummyDetail.open();
    dummyArchMaster.open();
    dummyArchDetail.open();

//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(dummyMaster);
//    syst.prn(dummyDetail);

    try {
      dummyDetail.first();

      do {
        dummyArchDetail.insertRow(false);
        DataSet.copyTo(koloneDitelj, dummyDetail, koloneDitelj, dummyArchDetail);
      } while (dummyDetail.next());

      dummyMaster.first();

      do {
      dummyArchMaster.insertRow(false);
      DataSet.copyTo(koloneMaster, dummyMaster, koloneMaster, dummyArchMaster);
      
      dummyArchMaster.setBigDecimal("TROSKOVI",dummyArchMaster.getBigDecimal("UPLRAZLIKA").negate());
      dummyArchMaster.setBigDecimal("RAZLIKA",dummyArchMaster.getBigDecimal("UPLRAZLIKA"));
      
      } while (dummyMaster.next());

      dummyDetail.deleteAllRows();
      dummyMaster.deleteAllRows();

      raTransaction.saveChangesInTransaction(new QueryDataSet[] {dummyMaster, dummyDetail, dummyArchMaster, dummyArchDetail});

      JOptionPane.showMessageDialog(this.getWindow(),
                                    "Putni nalog je arhiviran",
                                    "Obavijest", JOptionPane.INFORMATION_MESSAGE);
      dM.getSynchronizer().markAsDirty("PutniNalog_Radnici");
    }
    catch (Exception ex) {
      JOptionPane.showMessageDialog(this.getWindow(),
                                    "Putni nalog nije arhiviran",
                                    "Pozor", JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    directIspl.setColumns(new Column[] {
      dm.createShortColumn("RBS", "Broj Stavke"),
      dm.createStringColumn("OPIS", "Opis", 30),
      dm.createBigDecimalColumn("IZNOS", "Trošak"),
      dm.createBigDecimalColumn("PVIZNOS", "Trošak u dom. val."),
      dm.createStringColumn("OZNVAL", "Oznaka valute", 3)
    });

/** @todo set storage */
//    setStorageDS();

    this.setEnabledNavAction(this.getNavBar().getNavContainer().getNavActions()[1],false);
    this.setEnabledNavAction(this.getNavBar().getNavContainer().getNavActions()[2],false);
    this.setRaQueryDataSet(ss.stavblagFilteredForRazlikaPN(fromPres.getString("CRADNIK"), fromPres.getString("CPN")));
    this.setVisibleCols(new int[] {13, 1, 2, 6, 7, 8});
    jpDetail = new jpRazlikaPN_V2(this);
    this.setRaDetailPanel(jpDetail);
  }

  public void updateValutaLabel(){
    if (!jpDetail.jlrCblag.getText().equals("")){
      this.getRaQueryDataSet().setString("OZNVAL", ss.getOznvalPrikoBlagajne(this.getRaQueryDataSet().getString("KNJIG"), jpDetail.jlrCblag.getText()));
      jpDetail.jlValuta.setText(ss.getOznvalPrikoBlagajne(this.getRaQueryDataSet().getString("KNJIG"), jpDetail.jlrCblag.getText()));
      jpDetail.jlValuta2.setText(ss.getOznvalPrikoBlagajne(this.getRaQueryDataSet().getString("KNJIG"), jpDetail.jlrCblag.getText()));
    }
  }

  /** @todo ovo ispod */

//  StorageDataSet razlike = new StorageDataSet();
//
//  void setStorageDS(){
//    razlike.setColumns( new Column[] {
//    dm.createStringColumn("OZNVAL",5),
//    dm.createBigDecimalColumn("IZNOS",2),
//    dm.createBigDecimalColumn("PVIZNOS",6)});
//    razlike.open();
//  }

  QueryDataSet obrPN;
  QueryDataSet akontacija01;
  QueryDataSet akontacija02;
  QueryDataSet isplata01;
  QueryDataSet isplata02;
  QueryDataSet uplata01;
  QueryDataSet uplata02;

  void datasets(){
    String stavke = "SELECT oznval, iznos, pviznos, ispl "+
                    "FROM Stavkepn "+
                    "WHERE cpn='" + fromPres.getString("CPN") + "' ";

    //String updPN = "update stavkepn set ispl='D' where cpn='" + fromPres.getString("CPN") + "' ";

    String akont01 = "SELECT oznval, izdatak as iznos, pvizdatak as pviznos "+
                     "FROM Stavblag WHERE cskl='6' AND stavka='1' and cpn='" +
                     fromPres.getString("CPN") + "' ";

    String akont02 = "SELECT oznval, izdatak as iznos, pvizdatak as pviznos "+
                     "FROM Stavkeblarh WHERE cskl='6' AND stavka='1' and cpn='" +
                     fromPres.getString("CPN") + "' ";

    String ispl01 = "SELECT oznval, izdatak as iznos, pvizdatak as pviznos "+
                    "FROM Stavblag WHERE cskl='6' AND stavka='2' and cpn='" +
                    fromPres.getString("CPN") + "' ";

    String ispl02 = "SELECT oznval, izdatak as iznos, pvizdatak as pviznos "+
                    "FROM Stavkeblarh WHERE cskl='6' AND stavka='2' and cpn='" +
                    fromPres.getString("CPN") + "' ";

    String upl01 = "SELECT oznval, primitak as iznos, pvprimitak as pviznos "+
                   "FROM Stavblag WHERE cskl='6' AND stavka='2' and cpn='" +
                   fromPres.getString("CPN") + "' ";

    String upl02 = "SELECT oznval, primitak as iznos, pvprimitak as pviznos "+
                   "FROM Stavkeblarh WHERE cskl='6' AND stavka='2' and cpn='" +
                   fromPres.getString("CPN") + "' ";

    obrPN = ut.getNewQueryDataSet(stavke);
    akontacija01 = ut.getNewQueryDataSet(akont01);
    akontacija02 = ut.getNewQueryDataSet(akont02);
    isplata01 = ut.getNewQueryDataSet(ispl01);
    isplata02 = ut.getNewQueryDataSet(ispl02);
    uplata01 = ut.getNewQueryDataSet(upl01);
    uplata02 = ut.getNewQueryDataSet(upl02);

    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
    System.out.println("update   : " + updateStavPN);
//******************
    System.out.println("**** stavke     : " + stavke);
    st.prn(obrPN);
    System.out.println("**** akont01  : " + akont01);
    st.prn(akontacija01);
    System.out.println("**** akont02  : " + akont02);
    st.prn(akontacija02);
    System.out.println("**** ispl01   : " + ispl01);
    st.prn(isplata01);
    System.out.println("**** ispl02   : " + ispl02);
    st.prn(isplata02);
    System.out.println("**** upl01    : " + upl01);
    st.prn(uplata01);
    System.out.println("**** upl02    : " + upl02);
    st.prn(uplata02);
  }
  
  void clc_set_add(QueryDataSet set) {
    preostalaRazlikaIzObracuna = preostalaRazlikaIzObracuna.add(set.getBigDecimal("IZNOS"));
    preostalaRazlikaIzObracunaPV = preostalaRazlikaIzObracunaPV.add(set.getBigDecimal("PVIZNOS"));    
  }
  
  void clc_set_subtract(QueryDataSet set) {
    preostalaRazlikaIzObracuna = preostalaRazlikaIzObracuna.subtract(set.getBigDecimal("IZNOS"));
    preostalaRazlikaIzObracunaPV = preostalaRazlikaIzObracunaPV.subtract(set.getBigDecimal("PVIZNOS"));    
  }

  void clc_set(QueryDataSet set, boolean add, String dbginfo) {
System.out.println("clc_set add = "+add);    
    for (set.first(); set.inBounds(); set.next()) {
      System.out.println("Processing "+dbginfo+": "+set);        
      if (jpDetail.jrbValBlag.isSelected()){
System.out.println("jpDetail.jrbValBlag.isSelected()");
        if (set.getString("OZNVAL").equals(fromPres.getString("OZNVAL"))){
System.out.println("set.getString(\"OZNVAL\").equals(fromPres.getString(\"OZNVAL\"))");
          if (add) clc_set_add(set);
          else clc_set_subtract(set);
        }
      } else {
System.out.println("ELSE::jpDetail.jrbValBlag.isSelected()");
        if (jpDetail.jcbUValuti.isSelected() && set.getString("OZNVAL").equals(directIspl.getString("OZNVAL"))){
System.out.println("jpDetail.jcbUValuti.isSelected() && obrPN.getString(\"OZNVAL\").equals(directIspl.getString(\"OZNVAL\"))");
          if (add) clc_set_add(set);
          else clc_set_subtract(set);
        } else {
System.out.println("ELSE::jpDetail.jcbUValuti.isSelected() && obrPN.getString(\"OZNVAL\").equals(directIspl.getString(\"OZNVAL\"))");
          if (add) clc_set_add(set);
          else clc_set_subtract(set);
        }
      }      
    }    
  }
  void clc(){
    
    if (obrPN == null) return;
    
    preostalaRazlikaIzObracuna = NULLA;
    preostalaRazlikaIzObracunaPV = NULLA;

    try {
      clc_set(obrPN, true, "obrPN");
      clc_set(akontacija01, false, "akontacija01");
      clc_set(akontacija02, false, "akontacija02");
      clc_set(isplata01, false, "isplata01");
      clc_set(isplata02, false, "isplata02");
      clc_set(uplata01, true, "uplata01");
      clc_set(uplata02, true, "uplata02");
//      sysoutTEST syst = new sysoutTEST(false);
//      syst.prn(obrPN);
      
//      obrPN.first();
//      do {
//System.out.println("Processing obrPN: "+obrPN+"...");        
//        if (jpDetail.jrbValBlag.isSelected()){
//System.out.println("jpDetail.jrbValBlag.isSelected()");
//          if (obrPN.getString("OZNVAL").equals(fromPres.getString("OZNVAL"))){
//System.out.println("obrPN.getString(\"OZNVAL\").equals(fromPres.getString(\"OZNVAL\"))");
//            preostalaRazlikaIzObracuna = preostalaRazlikaIzObracuna.add(obrPN.getBigDecimal("IZNOS"));
//            preostalaRazlikaIzObracunaPV = preostalaRazlikaIzObracunaPV.add(obrPN.getBigDecimal("PVIZNOS"));
//          }
//        } else {
//System.out.println("ELSE::jpDetail.jrbValBlag.isSelected()");
//          if (jpDetail.jcbUValuti.isSelected() && obrPN.getString("OZNVAL").equals(directIspl.getString("OZNVAL"))){
//System.out.println("jpDetail.jcbUValuti.isSelected() && obrPN.getString(\"OZNVAL\").equals(directIspl.getString(\"OZNVAL\"))");
//            preostalaRazlikaIzObracuna = preostalaRazlikaIzObracuna.add(obrPN.getBigDecimal("PVIZNOS"));
//            preostalaRazlikaIzObracunaPV = preostalaRazlikaIzObracunaPV.add(obrPN.getBigDecimal("PVIZNOS"));
//          } else {
//System.out.println("ELSE::jpDetail.jcbUValuti.isSelected() && obrPN.getString(\"OZNVAL\").equals(directIspl.getString(\"OZNVAL\"))");
//            preostalaRazlikaIzObracuna = preostalaRazlikaIzObracuna.add(obrPN.getBigDecimal("PVIZNOS"));
//            preostalaRazlikaIzObracunaPV = preostalaRazlikaIzObracunaPV.add(obrPN.getBigDecimal("PVIZNOS"));
//          }
//        }
//      } while (obrPN.next());
//
//      akontacija01.first();
//      do {
//System.out.println("Processing akontacija01: "+akontacija01+"...");
//        if (jpDetail.jrbValBlag.isSelected()){
//System.out.println("jpDetail.jrbValBlag.isSelected()");
//          if (akontacija01.getString("OZNVAL").equals(fromPres.getString("OZNVAL"))){
//System.out.println("akontacija01.getString(\"OZNVAL\").equals(fromPres.getString(\"OZNVAL\"))");
//            preostalaRazlikaIzObracuna = preostalaRazlikaIzObracuna.subtract(akontacija01.getBigDecimal("IZDATAK"));
//            preostalaRazlikaIzObracunaPV = preostalaRazlikaIzObracunaPV.subtract(akontacija01.getBigDecimal("PVIZDATAK"));
//          }
//        } else {
//System.out.println("ELSE::jpDetail.jrbValBlag.isSelected()");
//          if (jpDetail.jcbUValuti.isSelected() && akontacija01.getString("OZNVAL").equals(directIspl.getString("OZNVAL"))){
//System.out.println("jpDetail.jcbUValuti.isSelected() && akontacija01.getString(\"OZNVAL\").equals(directIspl.getString(\"OZNVAL\"))");
//            preostalaRazlikaIzObracuna = preostalaRazlikaIzObracuna.subtract(akontacija01.getBigDecimal("PVIZDATAK"));
//            preostalaRazlikaIzObracunaPV = preostalaRazlikaIzObracunaPV.subtract(akontacija01.getBigDecimal("PVIZDATAK"));
//          } else {
//System.out.println("ELSE::jpDetail.jcbUValuti.isSelected() && obrPN.getString(\"OZNVAL\").equals(directIspl.getString(\"OZNVAL\"))");
//            preostalaRazlikaIzObracuna = preostalaRazlikaIzObracuna.subtract(akontacija01.getBigDecimal("PVIZDATAK"));
//            preostalaRazlikaIzObracunaPV = preostalaRazlikaIzObracunaPV.subtract(akontacija01.getBigDecimal("PVIZDATAK"));
//          }
//        }
//      } while (akontacija01.next());
//
//      akontacija02.first();
//      do {
//System.out.println("Processing akontacija02: "+akontacija02+"...");
//        preostalaRazlikaIzObracuna = preostalaRazlikaIzObracuna.subtract(akontacija02.getBigDecimal("IZDATAK"));
//        preostalaRazlikaIzObracunaPV = preostalaRazlikaIzObracunaPV.subtract(akontacija02.getBigDecimal("PVIZDATAK"));
//      } while (akontacija02.next());
//
//      isplata01.first();
//      do {
//System.out.println("Processing isplata01: "+isplata01+"...");
//        preostalaRazlikaIzObracuna = preostalaRazlikaIzObracuna.subtract(isplata01.getBigDecimal("IZDATAK"));
//        preostalaRazlikaIzObracunaPV = preostalaRazlikaIzObracunaPV.subtract(isplata01.getBigDecimal("PVIZDATAK"));
//      } while (isplata01.next());
//
//      isplata02.first();
//      do {
//System.out.println("Processing isplata01: "+isplata02+"...");
//        preostalaRazlikaIzObracuna = preostalaRazlikaIzObracuna.subtract(isplata02.getBigDecimal("IZDATAK"));
//        preostalaRazlikaIzObracunaPV = preostalaRazlikaIzObracunaPV.subtract(isplata02.getBigDecimal("PVIZDATAK"));
//      } while (isplata02.next());
//      
//      uplata01.first();
//      do {
//System.out.println("Processing uplata01: "+uplata01+"...");
//        preostalaRazlikaIzObracuna = preostalaRazlikaIzObracuna.add(uplata01.getBigDecimal("PRIMITAK"));
//        preostalaRazlikaIzObracunaPV = preostalaRazlikaIzObracunaPV.add(uplata01.getBigDecimal("PVPRIMITAK"));
//      } while (uplata01.next());
//      
//      uplata02.first();
//      do {
//System.out.println("Processing uplata02: "+uplata02+"...");
//        preostalaRazlikaIzObracuna = preostalaRazlikaIzObracuna.add(uplata02.getBigDecimal("PRIMITAK"));
//        preostalaRazlikaIzObracunaPV = preostalaRazlikaIzObracunaPV.add(uplata02.getBigDecimal("PVPRIMITAK"));
//      } while (uplata02.next());

      System.out.println("preostalaRazlikaIzObracuna = "+preostalaRazlikaIzObracuna);
      System.out.println("preostalaRazlikaIzObracunaPV = "+preostalaRazlikaIzObracunaPV);

      BigDecimal _razl = jpDetail.jrbValBlag.isSelected()?preostalaRazlikaIzObracuna:preostalaRazlikaIzObracunaPV;
      getRaQueryDataSet().setBigDecimal("PRIMITAK",(_razl.signum()<0)?_razl.negate():Aus.zero2);
      getRaQueryDataSet().setBigDecimal("IZDATAK",(_razl.signum()>=0)?_razl:Aus.zero2);
      
//    setRazliku(obrPN.getString("OZNVAL"),preostalaRazlikaIzObracuna);
//    setIznose();
      

    }
    catch (Exception ex) {
      ex.printStackTrace();
//      System.out.println("exepshn!!");
    }
  }

  /*void calculate(){
//    System.out.println("frmRazlikaPN_V2 - CALCULATE");
    hmAkont.clear();
    preostalaRazlikaIzObracuna = NULLA;
    preostalaRazlikaIzObracunaPV = NULLA;

    String stavke = "SELECT max(oznval) as oznval, sum(iznos) as iznos, sum(pviznos) as pviznos "+
                    "FROM Stavkepn "+
                    "WHERE cpn='" + fromPres.getString("CPN") + "' ";
    String prValStr = "AND oznval = '" + directIspl.getString("OZNVAL") + "' ";
    String blValStr = "AND oznval = '" + fromPres.getString("OZNVAL") + "' ";
    String jedStav = "AND rbs='" + directIspl.getShort("RBS") + "' ";
    String isplN = "AND ispl='N' ";
    String group = "GROUP BY oznval";
    String endStr = isplN.concat(group);
    String updPN = "update stavkepn set ispl='D' where cpn='" + fromPres.getString("CPN") + "' ";

    String akont01 = "SELECT max(oznval) as oznval, sum(izdatak) as izdatak, sum(pvizdatak) as pvizdatak "+
                     "FROM Stavblag WHERE cskl='6' AND stavka='1' and cpn='" +
                     fromPres.getString("CPN") + "' ";
    String akont02 = "SELECT max(oznval) as oznval, sum(izdatak) as izdatak, sum(pvizdatak) as pvizdatak "+
                     "FROM Stavkeblarh WHERE cskl='6' AND stavka='1' and cpn='" +
                     fromPres.getString("CPN") + "' ";

    String stPNispl = "SELECT max(oznval) as oznval, sum(iznos) as iznos, "+
                      "sum(pviznos) as pviznos FROM Stavkepn WHERE ispl='T' and cpn='" +
                      fromPres.getString("CPN") + "' ";

    String upit = "";

    if (jpDetail.jrbValBlag.isSelected()){
      if (jpDetail.jcbStavka.isSelected()){
        upit = stavke.concat(jedStav.concat(endStr));
        updateStavPN = updPN.concat(jedStav);
      } else {
        upit = stavke.concat(blValStr.concat(endStr));
        updateStavPN = updPN.concat(blValStr);
      }
      akont01 = akont01.concat(blValStr);
      akont02 = akont02.concat(blValStr);
      stPNispl = stPNispl.concat(blValStr)+group;
    } else {
      if (jpDetail.jcbUValuti.isSelected()){
        if (jpDetail.jcbStavka.isSelected()){
          upit = stavke.concat(jedStav.concat(endStr));
          updateStavPN = updPN.concat(jedStav);
          akont01 = akont01.concat(prValStr);
          akont02 = akont02.concat(prValStr);
          stPNispl = stPNispl.concat(prValStr)+group;
        } else {
          upit = stavke.concat(prValStr.concat(endStr));
          updateStavPN = updPN.concat(prValStr);
          stPNispl = stPNispl.concat(group);
        }
      } else {
        if (jpDetail.jcbStavka.isSelected()){
          upit = stavke.concat(jedStav.concat(endStr));
          updateStavPN = updPN.concat(jedStav);
//          akont01 = akont01.concat(prValStr);
//          akont02 = akont02.concat(prValStr);
//          stPNispl = stPNispl.concat(prValStr);
        } else {
          upit = stavke.concat(isplN);
          updateStavPN = updPN.concat(isplN);
        }
        stPNispl = stPNispl.concat(group);
      }
    }

    
//      Izra\u010Dunati ponovo stavke koje su ispla\u010Dene i to usporedit s akontacijom u njihovoj valuti.
//      Oduzeti od sume upla\u010Denih stavki upla\u010Denu akontaciju, i razliku oduzeti od iznosa za uplatu/vra\u0107anje
//      za doti\u010Dnu valutu.
//      Ako je PV, onda to isto za PV :))
//
//    System.out.println("upit     : " + upit);
//    System.out.println("update   : " + updateStavPN);
//    System.out.println("stPNispl : " + stPNispl);
//    System.out.println("akont01  : " + akont01);
//    System.out.println("akont02  : " + akont02);

    QueryDataSet qds = ut.getNewQueryDataSet(upit);
    QueryDataSet isplU_Obr = ut.getNewQueryDataSet(stPNispl);
    QueryDataSet akontacijaR = ut.getNewQueryDataSet(akont01);
    QueryDataSet akontacijaA = ut.getNewQueryDataSet(akont02);

//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(qds);
//    syst.prn(isplU_Obr);
//    syst.prn(akontacijaR);
//    syst.prn(akontacijaA);

    if (!isplU_Obr.isEmpty() || qds.isEmpty()){
      preostalaRazlikaIzObracuna = akontacijaR.getBigDecimal("IZDATAK").
                                   add(akontacijaA.getBigDecimal("IZDATAK")).
                                   subtract(isplU_Obr.getBigDecimal("IZNOS"));
      if (preostalaRazlikaIzObracuna.compareTo(NULLA) != 0){
        preostalaRazlikaIzObracunaPV = akontacijaR.getBigDecimal("PVIZDATAK").
                                       add(akontacijaA.getBigDecimal("PVIZDATAK")).
                                       subtract(isplU_Obr.getBigDecimal("PVIZNOS"));
      }
    }

//    System.out.println("Razlika iz obracuna     : " + preostalaRazlikaIzObracuna);
//    System.out.println("Razlika iz obracuna upv : " + preostalaRazlikaIzObracunaPV);

//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(qds);
//    setRazliku(qds);
    setIznose();
  }*/

  java.util.HashMap hmAkont = new java.util.HashMap();
  BigDecimal preostalaRazlikaIzObracuna;
  BigDecimal preostalaRazlikaIzObracunaPV;
  BigDecimal NULLA = Aus.zero2;


//  void setRazliku(String oznval, BigDecimal iznos){
//    razlika.setString("VALUTA", oznval);
//    if (jpDetail.jrbValBlag.isSelected()){
//      razlika.setBigDecimal("RAZLIKA", iznos);
//    } else {
//      razlika.setBigDecimal("RAZLIKA", iznos);
//    }
//  }

//  public void setIznose(){
////    System.out.println("frmRazlikaPN_V2 - SETIZNOSE");
//    if (razlika.getBigDecimal("RAZLIKA").compareTo(new BigDecimal(0)) < 0){
//      this.getRaQueryDataSet().setBigDecimal("PRIMITAK", razlika.getBigDecimal("RAZLIKA").negate());
//      this.getRaQueryDataSet().setBigDecimal("IZDATAK", new BigDecimal(0));
//    } else if (razlika.getBigDecimal("RAZLIKA").compareTo(new BigDecimal(0)) >= 0){
//      this.getRaQueryDataSet().setBigDecimal("PRIMITAK", new BigDecimal(0));
//      this.getRaQueryDataSet().setBigDecimal("IZDATAK", razlika.getBigDecimal("RAZLIKA"));
//    }/* else if (razlika.getBigDecimal("RAZLIKA").compareTo(new BigDecimal(0)) == 0){
//      this.getRaQueryDataSet().setBigDecimal("IZDATAK", new BigDecimal(0));
//      this.getRaQueryDataSet().setBigDecimal("PRIMITAK", new BigDecimal(0));
//    }*/
//  }

  void initial(){
    jpDetail.jrbValBlag.setSelected(true);
    jpDetail.jrbProtuvr.setSelected(false);
    jpDetail.jcbUValuti.setSelected(false);
    jpDetail.jcbStavka.setSelected(false);
  }

  public void paliGasi(){
    if (fromPres.getString("OZNVAL").equals(hr.restart.zapod.Tecajevi.getDomOZNVAL())){
      rcc.setLabelLaF(jpDetail.jbSelStavka, (jpDetail.jcbStavka.isSelected()));
      rcc.setLabelLaF(jpDetail.jlrValuta, jpDetail.jcbUValuti.isSelected() && jpDetail.jrbProtuvr.isSelected());
      rcc.setLabelLaF(jpDetail.jbSelValuta, jpDetail.jcbUValuti.isSelected() && jpDetail.jrbProtuvr.isSelected());
      rcc.setLabelLaF(jpDetail.jcbUValuti, jpDetail.jrbProtuvr.isSelected());

      if(jpDetail.jrbValBlag.isSelected()){
          ss.setNeisplaceneStavkeUValPN(fromPres.getString("CPN"), fromPres.getString("OZNVAL"), false);
          jpDetail.jlrRbs.setRaDataSet(ss.getNeisplaceneStavkePNUVal());
      }
      if(jpDetail.jrbProtuvr.isSelected()){
        if (!directIspl.getString("OZNVAL").equals("") && jpDetail.jcbUValuti.isSelected()){
          ss.setNeisplaceneStavkeUValPN(fromPres.getString("CPN"), directIspl.getString("OZNVAL"), false);
          jpDetail.jlrRbs.setRaDataSet(ss.getNeisplaceneStavkePNUVal());
        } else {
          ss.setNeisplaceneStavkePN(fromPres.getString("CPN"), false);
          jpDetail.jlrRbs.setRaDataSet(ss.getNeisplaceneStavkePN());
        }
      }
    } else {
      rcc.setLabelLaF(jpDetail.jrbProtuvr, false);
      rcc.setLabelLaF(jpDetail.jrbValBlag, false);
      rcc.setLabelLaF(jpDetail.jcbUValuti, false);
      rcc.setLabelLaF(jpDetail.jlrValuta, false);
      rcc.setLabelLaF(jpDetail.jbSelValuta, false);
    }
    rcc.setLabelLaF(jpDetail.jlrRbs, jpDetail.jcbStavka.isSelected());
    rcc.setLabelLaF(jpDetail.jlrOpis, jpDetail.jcbStavka.isSelected());
  }
}