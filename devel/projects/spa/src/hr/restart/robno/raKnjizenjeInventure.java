/****license*****************************************************************
**   file: raKnjizenjeInventure.java
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

import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;
import hr.restart.util.raUpitLite;

import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raKnjizenjeInventure extends raUpitLite {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  sgQuerys ss = sgQuerys.getSgQuerys();
  hr.restart.robno.raInventuraUtil riut = hr.restart.robno.raInventuraUtil.getInventuraUtil();

  QueryDataSet fieldSet = new QueryDataSet();
  static private QueryDataSet qdsUpitNaVisak;
  static private QueryDataSet qdsUpitNaManjak;
  QueryDataSet dummyDokU;
  QueryDataSet dummyDokI;
  QueryDataSet dummyStDokU;
  QueryDataSet dummyStDokI;
  QueryDataSet dummyStanjeFull;
  QueryDataSet isThisTheEnd;

  Column cskl = new Column();

  JPanel mainPanel = new JPanel();

  XYLayout xYLayout1 = new XYLayout();

  JLabel jlCskl = new JLabel();
  JLabel jlaCskl = new JLabel();
  JLabel jlaNazskl = new JLabel();
  JraButton jbSelCskl = new JraButton();
  JlrNavField jlrCskl = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazskl = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrGod = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  TitledBorder titledBorder1;

  public raKnjizenjeInventure() {
    try {
      jbInit();
    }
    catch (Exception ex) {
    }
//    System.out.println("NEW SHOES!!!");
  }

  public void componentShow() {
    difolts();
  }

  private void difolts() {
      onAndOffPanel(true);
      fieldSet.setString("CSKL", "");
      jlrCskl.emptyTextFields();
      jlrCskl.requestFocus();
  }

  void onAndOffPanel(boolean ocul){
    rcc.EnabDisabAll(mainPanel, ocul);
  }

  public void okPress() {
    settingValues();
  }

  public void afterOKPress(){
    String theEnd= ss.getStringUsporedba(fieldSet.getString("CSKL"));
    isThisTheEnd = ut.getNewQueryDataSet(theEnd);
    isThisTheEnd.setString("STATINV", "N");
    Timestamp stariDatum = isThisTheEnd.getTimestamp("DATINV");
    isThisTheEnd.setTimestamp("STARIDATINV", stariDatum);
    if (saveDataInMyneTransaction()){
//      System.out.println("OVER GON FINISH DAN AUT - SI JU");
      if (!ocemolJos()){
        this.cancelPress();
      } else {
        difolts();
      }
    } else {
//      System.out.println("NIJE DOBRO, NIJE DOBRO, NIJE DOBRO NE NE NE.....");
      JOptionPane.showMessageDialog(this.mainPanel,
                                    new String[]{"Knjženje inventure nije uspjelo"},/*,
                                    "Došlo je do greške u programu",
                                    "Preporuèljivo nazvati Rest-art èim prije"},*/
                                    "Greška",/*"Jako velika greška!!",*/
                                    javax.swing.JOptionPane.ERROR_MESSAGE);
    }
  }

  public boolean Validacija() {
    if (jlrCskl.getText().equals("")){
      jlrCskl.requestFocus();
      JOptionPane.showConfirmDialog(this.mainPanel,"Obvezatan unos - SKLADIŠTE !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    onAndOffPanel(false);
    return askYourself();
  }

  public void firstESC() {
    difolts();
  }

  public boolean runFirstESC() {
    if (!fieldSet.getString("CSKL").equals("")) return true;
    return false;
  }

  public boolean isIspis() {
    return false;
  }

  public boolean ispisNow() {
    return false;
  }

  private void jbInit() throws Exception {
//    cskl.setColumnName("CSKL");
//    cskl.setDataType(com.borland.dx.dataset.Variant.STRING);

    cskl = dm.createStringColumn("CSKL",0);

    fieldSet.setColumns(new Column[] {cskl});

    mainPanel.setLayout(xYLayout1);

    jbSelCskl.setText("...");
    jlCskl.setText("Skladište");
    jlaCskl.setHorizontalAlignment(SwingConstants.CENTER);
    jlaCskl.setText("Šifra");
    jlaNazskl.setHorizontalAlignment(SwingConstants.CENTER);
    jlaNazskl.setText("Naziv");

    jlrCskl.setColumnName("CSKL");
    jlrCskl.setColNames(new String[] {"NAZSKL","GODINA"});
    jlrCskl.setDataSet(fieldSet);
    jlrCskl.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazskl,jlrGod});
    jlrCskl.setVisCols(new int[] {0, 1});
    jlrCskl.setSearchMode(0);
    jlrCskl.setRaDataSet(sgQuerys.getSgQuerys().getSkladistaUInventuri(hr.restart.zapod.OrgStr.getKNJCORG()));
    jlrCskl.setNavButton(jbSelCskl);

    jlrNazskl.setColumnName("NAZSKL");
    jlrNazskl.setNavProperties(jlrCskl);
    jlrNazskl.setSearchMode(1);

    jlrGod.setColumnName("GODINA");
    jlrGod.setNavProperties(jlrCskl);
    jlrGod.setSearchMode(1);
    jlrGod.setVisible(false);

    this.setJPan(mainPanel);

    xYLayout1.setWidth(595);
    xYLayout1.setHeight(70);

    mainPanel.add(jlaCskl, new XYConstraints(151, 13, 98, -1));
    mainPanel.add(jlaNazskl, new XYConstraints(256, 13, 293, -1));
    mainPanel.add(jbSelCskl, new XYConstraints(555, 30, 21, 21));
    mainPanel.add(jlCskl, new XYConstraints(15, 30, -1, -1));
    mainPanel.add(jlrCskl, new XYConstraints(150, 30, 100, -1));
    mainPanel.add(jlrNazskl, new XYConstraints(255, 30, 295, -1));

//    initQdss();
  }

  private boolean askYourself() {
    int answ = javax.swing.JOptionPane.showOptionDialog(
    null,
    new hr.restart.swing.raMultiLineMessage(new String[] {"Ova akcija proknjižava inventurne vrijednosti",
                                                          "Da li želite nastaviti?"}),
                                                          "Pitanje",
                                                          javax.swing.JOptionPane.YES_NO_OPTION,
                                                          javax.swing.JOptionPane.QUESTION_MESSAGE,
                                                          null,new String[] {"Da","Ne"},"Ne");
    if (answ == JOptionPane.YES_OPTION) {
      return true;
    } else {
      firstESC();
    }
    return false;
  }

  private void initQdss(){
    dummyDokI = hr.restart.baza.doki.getDataModule().getTempSet("1=0");
    dummyDokI.open();
    dummyDokU = hr.restart.baza.Doku.getDataModule().getTempSet("1=0");
    dummyDokU.open();
    dummyStDokI = hr.restart.baza.stdoki.getDataModule().getTempSet("1=0");
    dummyStDokI.open();
    dummyStDokU = hr.restart.baza.Stdoku.getDataModule().getTempSet("1=0");
    dummyStDokU.open();
    System.out.println("god = '"+jlrGod.getText().trim()+"' and cskl='"+fieldSet.getString("CSKL")+"'");
    String god = vl.getKnjigYear("robno");
    dummyStanjeFull = hr.restart.baza.Stanje.getDataModule().getTempSet(
        "god = '"+god+"' and cskl='"+fieldSet.getString("CSKL")+"'"); // "god = '2004' and cskl='1'");
    dummyStanjeFull.open();
  }

//  sysoutTEST syst = new sysoutTEST(false);

  private void settingValues(){
    initQdss();
    if ( ss.isThereAnything(fieldSet.getString("CSKL"), "kolvis>")){
//      System.out.println("ima viska");
      qdsUpitNaVisak = ss.getInventuraKnjizenjeSetupVisakManjak(fieldSet.getString("CSKL"), "kolvis");
      dummyDokU.insertRow(false);
      /** @todo staviti usera koji je proknjizio inventuru */
      dummyDokU.setString("CUSER", hr.restart.sisfun.raUser.getInstance().getUser());
      dummyDokU.setString("CSKL", fieldSet.getString("CSKL"));
      dummyDokU.setString("VRDOK", "INV");
      dummyDokU.setTimestamp("DATDOK", qdsUpitNaVisak.getTimestamp("DATINV"));
      dummyDokU.setTimestamp("SYSDAT", vl.getToday()); /** @todo datum inventure vs danasnji datum */
      getBrojDokumenta("INV");
      int i=1;
      do {
        dummyStDokU.insertRow(false);
        dummyStDokU.setString("CSKL", fieldSet.getString("CSKL"));
        dummyStDokU.setString("VRDOK", "INV");
        dummyStDokU.setInt("BRDOK", dummyDokU.getInt("BRDOK"));
        dummyStDokU.setString("GOD", dummyDokU.getString("GOD"));
        dummyStDokU.setShort("RBR", (short)i++ );
        dummyStDokU.setBigDecimal("KOL", qdsUpitNaVisak.getBigDecimal("KOLVIS"));
        dummyStDokU.setInt("CART", qdsUpitNaVisak.getInt("CART"));
        try {
          dummyStDokU.setString("CART1", qdsUpitNaVisak.getString("CART1"));
        }
        catch (Exception ex) {
//          System.err.println("CART11 bullshit DOKU");
          dummyStDokU.setString("CART1", qdsUpitNaVisak.getString("CART11"));
        }
        dummyStDokU.setString("BC", qdsUpitNaVisak.getString("BC"));
        dummyStDokU.setString("NAZART", qdsUpitNaVisak.getString("NAZART"));
        dummyStDokU.setString("JM", qdsUpitNaVisak.getString("JM"));
        dummyStDokU.setBigDecimal("ZC", qdsUpitNaVisak.getBigDecimal("ZC"));
        dummyStDokU.setBigDecimal("NC", qdsUpitNaVisak.getBigDecimal("NC"));
        dummyStDokU.setBigDecimal("VC", qdsUpitNaVisak.getBigDecimal("VC"));
        dummyStDokU.setBigDecimal("MC", qdsUpitNaVisak.getBigDecimal("MC"));
        dummyStDokU.setBigDecimal("INAB", qdsUpitNaVisak.getBigDecimal("NC").multiply(qdsUpitNaVisak.getBigDecimal("KOLVIS")));
        dummyStDokU.setBigDecimal("IBP" , qdsUpitNaVisak.getBigDecimal("VC").multiply(qdsUpitNaVisak.getBigDecimal("KOLVIS")));
        dummyStDokU.setBigDecimal("ISP" , qdsUpitNaVisak.getBigDecimal("MC").multiply(qdsUpitNaVisak.getBigDecimal("KOLVIS")));
        dummyStDokU.setBigDecimal("IZAD", qdsUpitNaVisak.getBigDecimal("ZC").multiply(qdsUpitNaVisak.getBigDecimal("KOLVIS")));
        //if(qdsUpitNaVisak.getString("VRZAL").equals("N")) {
          dummyStDokU.setBigDecimal("IPOR", _Main.nul);
          dummyStDokU.setBigDecimal("IMAR", _Main.nul);
        //}
        if(qdsUpitNaVisak.getString("VRZAL").equals("V") || qdsUpitNaVisak.getString("VRZAL").equals("M")) {
          dummyStDokU.setBigDecimal("IPOR", _Main.nul);
          dummyStDokU.setBigDecimal("IMAR", qdsUpitNaVisak.getBigDecimal("KOLVIS").multiply(qdsUpitNaVisak.getBigDecimal("VC").subtract(qdsUpitNaVisak.getBigDecimal("NC"))));
        }
        if(qdsUpitNaVisak.getString("VRZAL").equals("M")){
          dummyStDokU.setBigDecimal("IPOR", qdsUpitNaVisak.getBigDecimal("KOLVIS").multiply(qdsUpitNaVisak.getBigDecimal("MC").subtract(qdsUpitNaVisak.getBigDecimal("VC"))));
          dummyStDokU.setBigDecimal("IMAR", qdsUpitNaVisak.getBigDecimal("KOLVIS").multiply(qdsUpitNaVisak.getBigDecimal("VC").subtract(qdsUpitNaVisak.getBigDecimal("NC"))));
        }
        /** @todo visak */
//        riut./*getInventuraUtil().*/updateStanjeZaVishak(_Main.nul, _Main.nul, _Main.nul, _Main.nul, _Main.nul, dummyDokU, dummyStDokU);


    if (lookupData.getlookupData().raLocate(dummyStanjeFull,"CART",qdsUpitNaVisak.getInt("CART")+"")){
      dummyStanjeFull.setBigDecimal("KOLUL",     riut.sumValue(dummyStanjeFull.getBigDecimal("KOLUL"), dummyStDokU.getBigDecimal("KOL")));
      dummyStanjeFull.setBigDecimal("NABUL",     riut.sumValue(dummyStanjeFull.getBigDecimal("NABUL"), dummyStDokU.getBigDecimal("INAB")));
      dummyStanjeFull.setBigDecimal("MARUL",     riut.sumValue(dummyStanjeFull.getBigDecimal("MARUL"), dummyStDokU.getBigDecimal("IMAR")));
      dummyStanjeFull.setBigDecimal("PORUL",     riut.sumValue(dummyStanjeFull.getBigDecimal("PORUL"), dummyStDokU.getBigDecimal("IPOR")));
      dummyStanjeFull.setBigDecimal("VUL",       riut.sumValue(dummyStanjeFull.getBigDecimal("VUL"), dummyStDokU.getBigDecimal("IZAD")));
      dummyStanjeFull.setBigDecimal("VRI",       riut.negateValue(dummyStanjeFull.getBigDecimal("VUL"), dummyStanjeFull.getBigDecimal("VIZ")));
      dummyStanjeFull.setBigDecimal("KOL",       riut.negateValue(dummyStanjeFull.getBigDecimal("KOLUL"), dummyStanjeFull.getBigDecimal("KOLIZ")));
    }


      } while(qdsUpitNaVisak.next());
    }
    if (ss.isThereAnything(fieldSet.getString("CSKL"), "kolmanj>")){
//      System.out.println("ima manjka");
      qdsUpitNaManjak = ss.getInventuraKnjizenjeSetupVisakManjak(fieldSet.getString("CSKL"), "kolmanj");
      dummyDokI.insertRow(false);
      dummyDokI.setString("CUSER", hr.restart.sisfun.raUser.getInstance().getUser());
      dummyDokI.setString("CSKL", fieldSet.getString("CSKL"));
      dummyDokI.setString("VRDOK", "INM");
      dummyDokI.setTimestamp("DATDOK", qdsUpitNaManjak.getTimestamp("DATINV"));
      dummyDokI.setTimestamp("SYSDAT", vl.getToday()); /** @todo datum inventure vs danasnji datum */
      getBrojDokumenta("INM");
      int i=1;
      do {
        dummyStDokI.insertRow(false);
        dummyStDokI.setString("CSKL", fieldSet.getString("CSKL"));
        dummyStDokI.setString("VRDOK", "INM");
        dummyStDokI.setInt("BRDOK", dummyDokI.getInt("BRDOK"));
        dummyStDokI.setString("GOD", dummyDokI.getString("GOD"));
        dummyStDokI.setShort("RBR", (short)i++ );
        dummyStDokI.setBigDecimal("KOL", qdsUpitNaManjak.getBigDecimal("KOLMANJ"));
        dummyStDokI.setInt("CART", qdsUpitNaManjak.getInt("CART"));
        try {
          dummyStDokI.setString("CART1", qdsUpitNaManjak.getString("CART1"));
        }
        catch (Exception ex) {
          System.err.println("CART11 bullshit DOKI");
          dummyStDokI.setString("CART1", qdsUpitNaManjak.getString("CART11"));
        }
        dummyStDokI.setString("BC", qdsUpitNaManjak.getString("BC"));
        dummyStDokI.setString("NAZART", qdsUpitNaManjak.getString("NAZART"));
        dummyStDokI.setString("JM", qdsUpitNaManjak.getString("JM"));
        dummyStDokI.setBigDecimal("ZC", qdsUpitNaManjak.getBigDecimal("ZC"));
        dummyStDokI.setBigDecimal("NC", qdsUpitNaManjak.getBigDecimal("NC"));
        dummyStDokI.setBigDecimal("VC", qdsUpitNaManjak.getBigDecimal("VC"));
        dummyStDokI.setBigDecimal("MC", qdsUpitNaManjak.getBigDecimal("MC"));
        dummyStDokI.setBigDecimal("INAB", qdsUpitNaManjak.getBigDecimal("NC").multiply(qdsUpitNaManjak.getBigDecimal("KOLMANJ")));
        dummyStDokI.setBigDecimal("ISP", qdsUpitNaManjak.getBigDecimal("MC").multiply(qdsUpitNaManjak.getBigDecimal("KOLMANJ")));
        dummyStDokI.setBigDecimal("IRAZ", qdsUpitNaManjak.getBigDecimal("ZC").multiply(qdsUpitNaManjak.getBigDecimal("KOLMANJ")));
        //if(qdsUpitNaManjak.getString("VRZAL").equals("N")) {
          dummyStDokI.setBigDecimal("IPOR", _Main.nul);
          dummyStDokI.setBigDecimal("IMAR", _Main.nul);
        //}
        if(qdsUpitNaManjak.getString("VRZAL").equals("V") || qdsUpitNaManjak.getString("VRZAL").equals("M")) {
          dummyStDokI.setBigDecimal("IPOR", _Main.nul);
          dummyStDokI.setBigDecimal("IMAR", qdsUpitNaManjak.getBigDecimal("KOLMANJ").multiply(qdsUpitNaManjak.getBigDecimal("VC").subtract(qdsUpitNaManjak.getBigDecimal("NC"))));
        }
        if(qdsUpitNaManjak.getString("VRZAL").equals("M")){
          dummyStDokI.setBigDecimal("IPOR", qdsUpitNaManjak.getBigDecimal("KOLMANJ").multiply(qdsUpitNaManjak.getBigDecimal("MC").subtract(qdsUpitNaManjak.getBigDecimal("VC"))));
          dummyStDokI.setBigDecimal("IMAR", qdsUpitNaManjak.getBigDecimal("KOLMANJ").multiply(qdsUpitNaManjak.getBigDecimal("VC").subtract(qdsUpitNaManjak.getBigDecimal("NC"))));
        }
        /** @todo manjak */
//        riut.updateStanjeZaManjak(_Main.nul, _Main.nul, _Main.nul, _Main.nul, _Main.nul, dummyDokI, dummyStDokI);
//        updateStanjeZaManjak(qdsUpitNaManjak.getInt("CART")+"", _Main.nul, _Main.nul, _Main.nul, _Main.nul, _Main.nul, dummyDokI, dummyStDokI);


    if (lookupData.getlookupData().raLocate(dummyStanjeFull,"CART",qdsUpitNaManjak.getInt("CART")+"")){
      
//      if (qdsUpitNaManjak.getInt("CART") == 1015){
//        System.out.println("1051 - " + dummyStanjeFull.getBigDecimal("VRI")); //XDEBUG delete when no more needed
//      }
      
      dummyStanjeFull.setBigDecimal("KOLIZ",     riut.sumValue(dummyStanjeFull.getBigDecimal("KOLIZ"), dummyStDokI.getBigDecimal("KOL")));
      dummyStanjeFull.setBigDecimal("NABIZ",     riut.sumValue(dummyStanjeFull.getBigDecimal("NABIZ"), dummyStDokI.getBigDecimal("INAB")));
      dummyStanjeFull.setBigDecimal("MARIZ",     riut.sumValue(dummyStanjeFull.getBigDecimal("MARIZ"), dummyStDokI.getBigDecimal("IMAR")));
      dummyStanjeFull.setBigDecimal("PORIZ",     riut.sumValue(dummyStanjeFull.getBigDecimal("PORIZ"), dummyStDokI.getBigDecimal("IPOR")));
      dummyStanjeFull.setBigDecimal("VIZ",       riut.sumValue(dummyStanjeFull.getBigDecimal("VIZ"), dummyStDokI.getBigDecimal("IRAZ")));
      dummyStanjeFull.setBigDecimal("VRI",       riut.negateValue(dummyStanjeFull.getBigDecimal("VRI"), dummyStDokI.getBigDecimal("IRAZ")/*dummyStanjeFull.getBigDecimal("VIZ")*/));
      dummyStanjeFull.setBigDecimal("KOL",       riut.negateValue(dummyStanjeFull.getBigDecimal("KOLUL"), dummyStanjeFull.getBigDecimal("KOLIZ")));
      
//      if (qdsUpitNaManjak.getInt("CART") == 1015){
//        System.out.println("1051 - " + dummyStanjeFull.getBigDecimal("VRI")); //XDEBUG delete when no more needed
//      }
    }



      } while(qdsUpitNaManjak.next());
    }
//hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//syst.prn();
//    System.out.println("dummyDokI -------------");
//    syst.prn(dummyDokI);
//    System.out.println("dummyDokU -------------");
//    syst.prn(dummyDokU);
//    System.out.println("dummyStDokI -------------");
//    syst.prn(dummyStDokI);
//    System.out.println("dummyStDokU -------------");
//    syst.prn(dummyStDokU);

//    zavrsiInventuru();
//    try {
//      hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//      syst.prn(dummyStanjeFull);
//    }
//    catch (Exception ex) {
//      ex.printStackTrace();
//    }
  }

//  public void updateStanjeZaManjak(String cart, BigDecimal oldKOL, BigDecimal oldNAB, BigDecimal oldMAR, BigDecimal oldPOR, BigDecimal oldZAD, QueryDataSet doki, QueryDataSet stdoki){ //char mode, boolean isFind, char doc){
//    dm.getStanje().open();
//    if (lookupData.getlookupData().raLocate(dummyStanjeFull,"CART",cart)){
// //      System.out.println("update Stanje za artikli... " + stdoki.getString("NAZART"));
// //      System.out.println("i za skladishte............ " + doki.getString("CSKL"));
//
//      dummyStanjeFull.setBigDecimal("KOLIZ",     riut.negateValue(riut.sumValue(dummyStanjeFull.getBigDecimal("KOLIZ"), stdoki.getBigDecimal("KOL")), oldKOL));
//      dummyStanjeFull.setBigDecimal("NABIZ",     riut.negateValue(riut.sumValue(dummyStanjeFull.getBigDecimal("NABIZ"), stdoki.getBigDecimal("INAB")), oldNAB));
//      dummyStanjeFull.setBigDecimal("MARIZ",     riut.negateValue(riut.sumValue(dummyStanjeFull.getBigDecimal("MARIZ"), stdoki.getBigDecimal("IMAR")),  oldMAR));
//      dummyStanjeFull.setBigDecimal("PORIZ",     riut.negateValue(riut.sumValue(dummyStanjeFull.getBigDecimal("PORIZ"), stdoki.getBigDecimal("IPOR")),  oldPOR));
//      dummyStanjeFull.setBigDecimal("VIZ",       riut.negateValue(riut.sumValue(dummyStanjeFull.getBigDecimal("VIZ"), stdoki.getBigDecimal("IRAZ")), oldZAD));
//      dummyStanjeFull.setBigDecimal("VRI",       riut.negateValue(dummyStanjeFull.getBigDecimal("VRI"), dummyStanjeFull.getBigDecimal("VIZ")));
//      dummyStanjeFull.setBigDecimal("KOL",       riut.negateValue(dummyStanjeFull.getBigDecimal("KOLUL"), dummyStanjeFull.getBigDecimal("KOLIZ")));
//    }
//    //    dm.getStanje().post();
//    //    dm.getStanje().saveChanges();  // <-- u transakciji tamo odakle se poziva!!!
//  }

//  public void updateStanjeZaVishak(String cart, BigDecimal oldKOL, BigDecimal oldNAB, BigDecimal oldMAR, BigDecimal oldPOR, BigDecimal oldZAD, QueryDataSet doku, QueryDataSet stdoku){
// //  dm.getStanje().open();
// //    System.out.println("update Stanje za artikli... " + stdoku.getString("NAZART"));
// //    System.out.println("i za skladishte............ " + doku.getString("CSKL"));
//    if (lookupData.getlookupData().raLocate(dummyStanjeFull,"CART",cart)){
//      dummyStanjeFull.setBigDecimal("KOLUL",     riut.negateValue(riut.sumValue(dummyStanjeFull.getBigDecimal("KOLUL"), stdoku.getBigDecimal("KOL")), oldKOL));
//      dummyStanjeFull.setBigDecimal("NABUL",     riut.negateValue(riut.sumValue(dummyStanjeFull.getBigDecimal("NABUL"), stdoku.getBigDecimal("INAB")), oldNAB));
//      dummyStanjeFull.setBigDecimal("MARUL",     riut.negateValue(riut.sumValue(dummyStanjeFull.getBigDecimal("MARUL"), stdoku.getBigDecimal("IMAR")),  oldMAR));
//      dummyStanjeFull.setBigDecimal("PORUL",     riut.negateValue(riut.sumValue(dummyStanjeFull.getBigDecimal("PORUL"), stdoku.getBigDecimal("IPOR")),  oldPOR));
//      dummyStanjeFull.setBigDecimal("VUL",       riut.negateValue(riut.sumValue(dummyStanjeFull.getBigDecimal("VUL"), stdoku.getBigDecimal("IZAD")), oldZAD));
//      dummyStanjeFull.setBigDecimal("VRI",       riut.negateValue(dummyStanjeFull.getBigDecimal("VUL"), dummyStanjeFull.getBigDecimal("VIZ")));
//      dummyStanjeFull.setBigDecimal("KOL",       riut.negateValue(dummyStanjeFull.getBigDecimal("KOLUL"), dummyStanjeFull.getBigDecimal("KOLIZ")));
//    }
//    //dm.getStanje().post();
//    //    dm.getStanje().saveChanges();  // <-- u transakciji tamo odakle se poziva!!!
//  }

  private void getBrojDokumenta(String visakIliManjak) {
    String Godina;
    Integer Broj;
    if (visakIliManjak.equals("INV")){
      Godina=vl.getKnjigYear("robno");
      Broj=vl.findSeqInteger(qdsUpitNaVisak.getString("CSKL") + visakIliManjak + Godina);
      dummyDokU.setString("GOD",Godina);
      dummyDokU.setInt("BRDOK",Broj.intValue());
    } else if (visakIliManjak.equals("INM")){
      Godina=vl.getKnjigYear("robno");
      Broj=vl.findSeqInteger(qdsUpitNaManjak.getString("CSKL") + visakIliManjak + Godina);
      dummyDokI.setString("GOD",Godina);
      dummyDokI.setInt("BRDOK",Broj.intValue());
    }
  }

//  void zavrsiInventuru(){
//  }

  protected boolean saveDataInMyneTransaction(){
    raLocalTransaction saveOurData = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        try {

          raTransaction.saveChanges(dummyDokU);
          raTransaction.saveChanges(dummyStDokU);
          raTransaction.saveChanges(dummyDokI);
          raTransaction.saveChanges(dummyStDokI);
          raTransaction.saveChanges(dummyStanjeFull);
          raTransaction.saveChanges(isThisTheEnd);
          //raTransaction.saveChanges(dm.getStanje());

          String qDeleteInventura = ss.getStringDeleteInventura(fieldSet.getString("CSKL"));
          raTransaction.runSQL(qDeleteInventura);
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
    return saveOurData.execTransaction();
  }

  boolean ocemolJos(){
    int onda = JOptionPane.showOptionDialog(this.mainPanel,
        new String[]{"Inventura uspješno proknjižena",
        "Želite li knjižiti još jednu?"},
        "Pitanje",
        javax.swing.JOptionPane.YES_NO_OPTION,
        javax.swing.JOptionPane.QUESTION_MESSAGE,
        null,new String[] {"Da","Ne"},"Ne");

    if (onda == JOptionPane.YES_OPTION) return true;
    return false;
  }
}