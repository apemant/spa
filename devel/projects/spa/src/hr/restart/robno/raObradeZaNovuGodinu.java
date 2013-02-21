/****license*****************************************************************
**   file: raObradeZaNovuGodinu.java
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
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;
import hr.restart.util.raUpitLite;

import java.awt.event.ItemEvent;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public abstract class raObradeZaNovuGodinu extends raUpitLite {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  sgQuerys ss = sgQuerys.getSgQuerys();
  int rbr;

  int arrayCounter;
  String updateSkladArray[];

  QueryDataSet fieldSet = new QueryDataSet();
  QueryDataSet skladistaSet; // = new QueryDataSet();
  QueryDataSet dummyDoku;
  QueryDataSet dummyStanje;
  QueryDataSet dummyStDoku;

//  QueryDataSet dummyDokuF;
//  QueryDataSet dummyStanjeF;
//  QueryDataSet dummyStDokuF;

  Column godina = new Column();
  Column skladiste = new Column();
  Column stara = new Column();
  Column knjig = new Column();

  JPanel mainPanel = new JPanel();

  protected XYLayout xYLayout1 = new XYLayout();

  JraTextField jraGodina = new JraTextField();
  JraTextField jraStGodina = new JraTextField();
  JLabel jlGodina = new JLabel();
  JLabel jlStGodina = new JLabel();

  JLabel jlKnjig = new JLabel();
  JLabel jlaKnjig = new JLabel();
  JLabel jlaNazKnjig = new JLabel();
  JraButton jbSelKnjig = new JraButton();
  JlrNavField jlrKnjig = new JlrNavField() {
    public void after_lookUp() {
      godina(true);
    }
  };
  JlrNavField jlrNazKnjig = new JlrNavField() {
    public void after_lookUp() {
      godina(true);
    }
  };
  JLabel jlSklad = new JLabel();
  JraButton jbSelSklad = new JraButton();
  JlrNavField jlrSklad = new JlrNavField() {
    public void after_lookUp() {
//      skladische();
    }
  };
  JlrNavField jlrNazSkl = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  JraCheckBox jcbStanjeArtiklaNula = new JraCheckBox();
  JraCheckBox jcbRazlikaPoZaokruzenju = new JraCheckBox();
  JraCheckBox jcbPrijelazBezObrade = new JraCheckBox("Prijelaz bez obrade");
  
  BigDecimal zero = new BigDecimal(0).setScale(2);
  
  protected static raObradeZaNovuGodinu inst; 

  public raObradeZaNovuGodinu() {
    try {
      inst = this;
      jbInit();
    }
    catch (Exception ex) {
    }
  }
  
  public QueryDataSet getSklads() {
    return skladistaSet;
  }

  public void componentShow() {
    rcc.setLabelLaF(jraGodina, false);
    rcc.setLabelLaF(jlrKnjig, false);
    rcc.setLabelLaF(jlrNazKnjig, false);
    defolts();
  }

  //public abstract void okPress();

  public void firstESC() {}

  public boolean runFirstESC() {
    if(fieldSet.getString("CORG").equals("")) return true;
    return false;
  }

  public boolean isIspis() {
    return false;
  }

  public boolean ispisNow() {
    return false;
  }

  protected void jbInit() throws Exception {

    godina = dm.createStringColumn("GODINA",0);
    skladiste = dm.createStringColumn("CSKL",0);
    stara = dm.createStringColumn("SGOD",0);
    knjig = dm.createStringColumn("CORG",0);

    fieldSet.setColumns(new Column[] {godina, skladiste, knjig, stara});

    jlKnjig.setText("Organizacija");
    jlaKnjig.setHorizontalAlignment(SwingConstants.CENTER);
    jlaKnjig.setText("Šifra");
    jlaNazKnjig.setHorizontalAlignment(SwingConstants.CENTER);
    jlaNazKnjig.setText("Naziv");

    jlrKnjig.setColumnName("CORG");
    jlrKnjig.setColNames(new String[] {"NAZIV"});
    jlrKnjig.setDataSet(fieldSet);
    jlrKnjig.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazKnjig});
    jlrKnjig.setVisCols(new int[] {0, 1});
    jlrKnjig.setSearchMode(0);
    jlrKnjig.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getKnjigovodstva());
    jlrKnjig.setNavButton(jbSelKnjig);

    jbSelKnjig.setVisible(false);

    jlrNazKnjig.setColumnName("NAZIV");
    jlrNazKnjig.setNavProperties(jlrKnjig);
    jlrNazKnjig.setSearchMode(1);

    jlrSklad.setColumnName("CSKL");
    jlrSklad.setColNames(new String[] {"NAZSKL"});
    jlrSklad.setDataSet(fieldSet);
    jlrSklad.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazSkl});
    jlrSklad.setVisCols(new int[] {0, 1});
    jlrSklad.setSearchMode(0);
    jlrSklad.setRaDataSet(skladistaSet);
    jlrSklad.setNavButton(jbSelSklad);

    jlrNazSkl.setColumnName("NAZSKL");
    jlrNazSkl.setNavProperties(jlrSklad);
    jlrNazSkl.setSearchMode(1);

    jlGodina.setText("Godina");

    jraGodina.setHorizontalAlignment(SwingConstants.CENTER);
    jraGodina.setDataSet(fieldSet);
    jraGodina.setColumnName("GODINA");

    jraStGodina.setHorizontalAlignment(SwingConstants.CENTER);
    jraStGodina.setDataSet(fieldSet);
    jraStGodina.setColumnName("SGOD");

    jcbStanjeArtiklaNula.setText("Prijenos artikala sa koli\u010Dinom nula");
    jcbStanjeArtiklaNula.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        jcbStanjeArtiklaNula_itemStateChanged(e);
      }
    });
    jcbRazlikaPoZaokruzenju.setText("Prijenos razlika po zaokruženju");

    mainPanel.setLayout(xYLayout1);

    this.setJPan(mainPanel);

    xYLayout1.setWidth(595);
    xYLayout1.setHeight(160); //135

    mainPanel.add(jlaKnjig, new XYConstraints(151, 13, 98, -1));
    mainPanel.add(jlaNazKnjig, new XYConstraints(256, 13, 293, -1));
//    mainPanel.add(jbSelKnjig, new XYConstraints(555, 30, 21, 21));
    mainPanel.add(jlKnjig, new XYConstraints(15, 30, -1, -1));
    mainPanel.add(jlrKnjig, new XYConstraints(150, 30, 100, -1));
    mainPanel.add(jlrNazKnjig, new XYConstraints(255, 30, 295, -1));
    mainPanel.add(jlGodina,    new XYConstraints(15, 55, -1, -1));
    mainPanel.add(jraGodina,    new XYConstraints(150, 55, 100, -1));
    mainPanel.add(jcbStanjeArtiklaNula, new XYConstraints(150, 80, -1, -1));
    mainPanel.add(jcbRazlikaPoZaokruzenju, new XYConstraints(150, 105, -1, -1));
    mainPanel.add(jcbPrijelazBezObrade, new XYConstraints(150, 130, -1, -1));
  }

  public void addToDokuStdoku(String skladiste, String godina){
    String gdn;
    if(godina.equals("")) gdn=knjigodSet.getString("GOD"); /** @todo godina iz knjigod -DONE, BUT?!?!*/
    else gdn = godina;
    QueryDataSet qdsStanjeArtikli = ss.getObradeRadDvijeGodineSetDokuStdokuPocetnoStanje(skladiste, gdn, isPrijenosStanjaNula());

    if (qdsStanjeArtikli.rowCount() >0){
//      sysoutTEST syst = new sysoutTEST(false);
//      syst.prn(qdsDokuStdoku);
      Integer broj=vl.findSeqInteger(qdsStanjeArtikli.getString("CSKL") + "PST" + fieldSet.getString("GODINA"));
      insertIntoDoku(qdsStanjeArtikli, broj);
      rbr = 0;
      do {
        if (qdsStanjeArtikli.getBigDecimal("KOL").compareTo(_Main.nul) != 0 ||         
            isRazlikaPoZaokruzenju() || isPrijenosStanjaNula())
          insertIntoStDoku(qdsStanjeArtikli);
      } while (qdsStanjeArtikli.next());
    }
  }
  
  //Punjenje zaglavlja - POÈETNO STANJE
  public void insertIntoDoku(QueryDataSet qdsStanjeArtikli, Integer broj) { 
    dummyDoku.insertRow(false);
    dummyDoku.setString("CSKL", qdsStanjeArtikli.getString("CSKL"));
    dummyDoku.setString("VRDOK", "PST");
    dummyDoku.setString("CUSER", hr.restart.sisfun.raUser.getInstance().getUser());
    dummyDoku.setString("GOD",fieldSet.getString("GODINA")); //NOVA GODINA
    dummyDoku.setInt("BRDOK",broj.intValue()); //BROJ DOKUMENTA - lookup na SEQ
    dummyDoku.setTimestamp("SYSDAT", vl.getToday());
    dummyDoku.setTimestamp("DATDOK", ut.getYearBegin(fieldSet.getString("GODINA"))); //Postavlja datum dokumenta na 01.01. nove godine
    qdsStanjeArtikli.first();
  }

  raControlDocs rCD = new raControlDocs();   // dodao /.V radi provjera datuma i kalkulacija i bla bla

  // pretpostavlja da je dummyDoku podešen
  public void insertIntoStDoku(QueryDataSet qdsStanjeArtikli) {  
    
    String vrzal = qdsStanjeArtikli.getString("VRZAL");
    /**
     * Ne pušta u poèetno stanje artikle kojih nema na skladištu
     */
    /*if (qdsStanjeArtikli.getBigDecimal("KOL").compareTo(_Main.nul) != 0 || 
        (qdsStanjeArtikli.getBigDecimal("KOL").compareTo(_Main.nul) == 0 && 
            isRazlikaPoZaokruzenju())){*/
      
      //Brojaè RBR-a stavki
      ++rbr;
      
      //Novi red u tablici
      dummyStDoku.insertRow(false);
      
      //Polja koja se prepisuju
      dummyStDoku.setString("CSKL", qdsStanjeArtikli.getString("CSKL"));
      dummyStDoku.setString("VRDOK", "PST");
      dummyStDoku.setString("GOD", dummyDoku.getString("GOD"));//Nova godina
      dummyStDoku.setInt("BRDOK", dummyDoku.getInt("BRDOK"));
      dummyStDoku.setShort("RBR", (short)rbr);
      dummyStDoku.setInt("RBSID", rbr);
      dummyStDoku.setInt("CART", qdsStanjeArtikli.getInt("CART"));
      dummyStDoku.setString("CART1", qdsStanjeArtikli.getString("CART1"));
      dummyStDoku.setString("BC", qdsStanjeArtikli.getString("BC"));
      dummyStDoku.setString("NAZART", qdsStanjeArtikli.getString("NAZART"));
      dummyStDoku.setString("JM", qdsStanjeArtikli.getString("JM"));
      
      //Polja kolièina, cijene, zaduženja
      dummyStDoku.setBigDecimal("KOL", qdsStanjeArtikli.getBigDecimal("KOL")); //- kolièina
      dummyStDoku.setBigDecimal("NC", qdsStanjeArtikli.getBigDecimal("NC"));  //- "nabavna" cijena
      dummyStDoku.setBigDecimal("VC", qdsStanjeArtikli.getBigDecimal("VC"));  //- "veleprodajna" cijena
      dummyStDoku.setBigDecimal("MC", qdsStanjeArtikli.getBigDecimal("MC"));  //- "maloprodajna" cijena
      dummyStDoku.setBigDecimal("ZC", qdsStanjeArtikli.getBigDecimal("ZC"));  //- cijena zalihe ^ima li neki razlog zašto je ne bi prepisivao???
      dummyStDoku.setBigDecimal("DC", qdsStanjeArtikli.getBigDecimal("NC"));  //- "dobavljaèeva" cijena
      
      /**
       * Kod punjenja IZAD-a prepisivanjem vrijednost sa stanja da li radim dobro??
       * Naime, da li je vrijednost (VRI) stanja po metodi voðenja skladišta???
       */
      dummyStDoku.setBigDecimal("IZAD", qdsStanjeArtikli.getBigDecimal("VRI"));  // iznos zaduženja je vrijednost sa stanja
      
      /**
       * Provjera da li je vrijednost zalihe jednaka kolièini pomnoženoj sa cijenom zalihe
       */
      /*if (qdsStanjeArtikli.getBigDecimal("VRI").compareTo(qdsStanjeArtikli.getBigDecimal("KOL").multiply(qdsStanjeArtikli.getBigDecimal("ZC"))) != 0) {
        if (qdsStanjeArtikli.getBigDecimal("KOL").signum() != 0) {  
          BigDecimal zc = qdsStanjeArtikli.getBigDecimal("VRI").divide(
              qdsStanjeArtikli.getBigDecimal("KOL"), 2, BigDecimal.ROUND_HALF_UP);
          if (zc.subtract(qdsStanjeArtikli.getBigDecimal("ZC")).abs().
              compareTo(new BigDecimal("0.01")) > 0)
            System.out.println("Razlika na artiklu "+qdsStanjeArtikli.getInt("CART")+
                " zc " + zc + " <=> " + qdsStanjeArtikli.getBigDecimal("ZC"));
        }
      } */
      checkmul(dummyStDoku, "IZAD", "ZC");
      
      //prvo poništavam iznose
      dummyStDoku.setBigDecimal("INAB" , _Main.nul);
      dummyStDoku.setBigDecimal("IMAR" , _Main.nul);
      dummyStDoku.setBigDecimal("IPOR" , _Main.nul);
      
      dummyStDoku.setBigDecimal("INAB", qdsStanjeArtikli.getBigDecimal("NABUL").subtract(qdsStanjeArtikli.getBigDecimal("NABIZ")));
      // provjera metode voðenja skladišta!!
      
      if (vrzal.equals("N")) { //-skladište se vodi po metodi prosjeèna nabavna cijena
        checkeq(dummyStDoku, "INAB", "IZAD");  
        checkmul(dummyStDoku, "INAB", "NC");
        //dummyStDoku.setBigDecimal("INAB" , qdsStanjeArtikli.getBigDecimal("NC").multiply(qdsStanjeArtikli.getBigDecimal("KOL"))); //-nabavni iznos
        
      } else if (vrzal.equals("V")) { //-skladište se vodi po metodi zadnja prodajna cijena bez poreza
        
        //dummyStDoku.setBigDecimal("INAB" , qdsStanjeArtikli.getBigDecimal("NC").multiply(qdsStanjeArtikli.getBigDecimal("KOL"))); //-nabavni iznos
        
        //dummyStDoku.setBigDecimal("IBP" , qdsStanjeArtikli.getBigDecimal("VC").multiply(qdsStanjeArtikli.getBigDecimal("KOL")));  //-iznos bez poreza
        
        dummyStDoku.setBigDecimal("IMAR", qdsStanjeArtikli.getBigDecimal("MARUL").subtract(qdsStanjeArtikli.getBigDecimal("MARIZ")));
        dummyStDoku.setBigDecimal("IBP" , dummyStDoku.getBigDecimal("INAB").add(dummyStDoku.getBigDecimal("IMAR")));

        checkmul(dummyStDoku, "IBP", "VC");
        
//        dummyStDoku.setBigDecimal("IMAR" , dummyStDoku.getBigDecimal("IBP").
//            subtract(dummyStDoku.getBigDecimal("INAB")));  //-iznos marže (IBP-INAB)
        
      } else { //-skladište se vodi po metodi zadnja prodajna cijena s porezom
        
        dummyStDoku.setBigDecimal("IMAR", qdsStanjeArtikli.getBigDecimal("MARUL").subtract(qdsStanjeArtikli.getBigDecimal("MARIZ")));
        dummyStDoku.setBigDecimal("IPOR", qdsStanjeArtikli.getBigDecimal("PORUL").subtract(qdsStanjeArtikli.getBigDecimal("PORIZ")));
        
        dummyStDoku.setBigDecimal("IBP" , dummyStDoku.getBigDecimal("INAB").add(dummyStDoku.getBigDecimal("IMAR")));
        dummyStDoku.setBigDecimal("ISP" , dummyStDoku.getBigDecimal("IBP").add(dummyStDoku.getBigDecimal("IPOR")));
        
        //dummyStDoku.setBigDecimal("INAB" , qdsStanjeArtikli.getBigDecimal("NC").multiply(qdsStanjeArtikli.getBigDecimal("KOL"))); //-nabavni iznos
        //dummyStDoku.setBigDecimal("IBP" , qdsStanjeArtikli.getBigDecimal("VC").multiply(qdsStanjeArtikli.getBigDecimal("KOL")));  //-iznos bez poreza
        //dummyStDoku.setBigDecimal("ISP" , qdsStanjeArtikli.getBigDecimal("MC").multiply(qdsStanjeArtikli.getBigDecimal("KOL")));  //-iznos s porezom
        checkeq(dummyStDoku, "IZAD", "ISP");
        checkmul(dummyStDoku, "IBP", "VC");
        checkmul(dummyStDoku, "ISP", "MC");
//      dummyStDoku.setBigDecimal("IMAR" , dummyStDoku.getBigDecimal("IBP").
//            subtract(dummyStDoku.getBigDecimal("INAB")));  //-iznos marže (IBP-INAB)
//        dummyStDoku.setBigDecimal("IPOR" , dummyStDoku.getBigDecimal("ISP").
//            subtract(dummyStDoku.getBigDecimal("IBP")));  //-iznos poreza (ISP -IBP)
        
        /**
         * U sluèaju razlièitih poreza, punim polja POR1 ili POR2 ili POR3
         * Nisam siguran koliko je relevantno i zašto puniti poreze (mogao bi biti dostatan ukupan iznos poreza)
         * u poèetnom stanju. Za sada ovako.
         */
        /*if (lookupData.getlookupData().raLocate(dm.getArtikli(), "CART", String.valueOf(qdsStanjeArtikli.getInt("CART"))) && 
            lookupData.getlookupData().raLocate(dm.getPorezi(), "CPOR", dm.getArtikli().getString("CPOR"))) {
          dummyStDoku.setBigDecimal("POR1", qdsStanjeArtikli.getBigDecimal("VC").multiply(dm.getPorezi().getBigDecimal("POR1").divide(new BigDecimal("100.00"), 2, BigDecimal.ROUND_HALF_UP)));
          dummyStDoku.setBigDecimal("POR2", qdsStanjeArtikli.getBigDecimal("VC").multiply(dm.getPorezi().getBigDecimal("POR2").divide(new BigDecimal("100.00"), 2, BigDecimal.ROUND_HALF_UP)));
          dummyStDoku.setBigDecimal("POR3", qdsStanjeArtikli.getBigDecimal("VC").multiply(dm.getPorezi().getBigDecimal("POR3").divide(new BigDecimal("100.00"), 2, BigDecimal.ROUND_HALF_UP)));
        } else {
          dummyStDoku.setBigDecimal("POR1", _Main.nul);
          dummyStDoku.setBigDecimal("POR2", _Main.nul);
          dummyStDoku.setBigDecimal("POR3", _Main.nul);
        }*/
      }
      
      /**
       * Provjera INAB+IMAR+IPOR = IZAD
       */
      
      BigDecimal sumnmp = dummyStDoku.getBigDecimal("INAB").add(dummyStDoku.getBigDecimal("IMAR").add(dummyStDoku.getBigDecimal("IPOR")));
      
      if (sumnmp.compareTo(dummyStDoku.getBigDecimal("IZAD")) != 0){
        if (vrzal.equals("N")) {
          dummyStDoku.setBigDecimal("IMAR", zero);
          dummyStDoku.setBigDecimal("IPOR", zero);
          dummyStDoku.setBigDecimal("INAB", dummyStDoku.getBigDecimal("IZAD"));
        } else {
          if (vrzal.equals("V")) dummyStDoku.setBigDecimal("IPOR", zero);
          dummyStDoku.setBigDecimal("IMAR",(dummyStDoku.getBigDecimal("IMAR").add(dummyStDoku.getBigDecimal("IZAD").subtract(sumnmp))));
        }
        System.out.println("Provjera INAB+IMAR+IPOR = IZAD - POPRAVLJENO! "+
            dummyStDoku.getInt("CART"));
      } else {
//        System.out.println("Provjera INAB+IMAR+IPOR = IZAD - OK!");
      }
//    }
    
    /*
     dummyStDoku.setBigDecimal("INAB" , qdsStanjeArtikli.getBigDecimal("NC").multiply(qdsStanjeArtikli.getBigDecimal("KOL"))); //-nabavni iznos
     dummyStDoku.setBigDecimal("IBP" , qdsStanjeArtikli.getBigDecimal("VC").multiply(qdsStanjeArtikli.getBigDecimal("KOL")));  //-iznos bez poreza
     dummyStDoku.setBigDecimal("ISP" , qdsStanjeArtikli.getBigDecimal("MC").multiply(qdsStanjeArtikli.getBigDecimal("KOL")));  //-iznos s porezom
     */
    
    /* 
     //    ================================
      if(qdsStanjeArtikli.getString("VRZAL").equals("N")) {
      try {
      dummyStDoku.setBigDecimal("NC", qdsStanjeArtikli.getBigDecimal("VRI").divide(qdsStanjeArtikli.getBigDecimal("KOL"), BigDecimal.ROUND_HALF_UP));
      }
      catch (Exception ex) {
      dummyStDoku.setBigDecimal("NC", _Main.nul);
      }
      dummyStDoku.setBigDecimal("INAB", qdsStanjeArtikli.getBigDecimal("VRI"));
      dummyStDoku.setBigDecimal("ZC", dummyStDoku.getBigDecimal("NC"));
      } else {
      dummyStDoku.setBigDecimal("INAB", qdsStanjeArtikli.getBigDecimal("KOL").multiply(qdsStanjeArtikli.getBigDecimal("NC")));
      dummyStDoku.setBigDecimal("NC", qdsStanjeArtikli.getBigDecimal("NC"));
      if (qdsStanjeArtikli.getString("VRZAL").equals("V")){
      dummyStDoku.setBigDecimal("ZC", dummyStDoku.getBigDecimal("VC"));
      dummyStDoku.setBigDecimal("IMAR",(dummyStDoku.getBigDecimal("IZAD").subtract(dummyStDoku.getBigDecimal("INAB"))));
      } else if (qdsStanjeArtikli.getString("VRZAL").equals("M")){
      
      try {
      dummyStDoku.setBigDecimal("IPOR",dummyStDoku.getBigDecimal("IZAD").multiply(dm.getPorezi().getBigDecimal("UKUNPOR").multiply(new BigDecimal("0.01"))));
      dummyStDoku.setBigDecimal("IMAR",(dummyStDoku.getBigDecimal("IZAD").subtract(dummyStDoku.getBigDecimal("INAB").add(dummyStDoku.getBigDecimal("IPOR")))));
      }
      catch (Exception ex) {
      dummyStDoku.setBigDecimal("IMAR" , dummyStDoku.getBigDecimal("IBP").subtract(dummyStDoku.getBigDecimal("INAB")));
      dummyStDoku.setBigDecimal("IPOR" , dummyStDoku.getBigDecimal("ISP").subtract(dummyStDoku.getBigDecimal("IBP")));
      }
      
      dummyStDoku.setBigDecimal("ZC", dummyStDoku.getBigDecimal("MC"));
      }
      }
      try {
      dummyStDoku.setBigDecimal("MAR" , dummyStDoku.getBigDecimal("IMAR").divide(dummyStDoku.getBigDecimal("KOL"),2,BigDecimal.ROUND_HALF_UP));
      }
      catch (Exception ex) {
      dummyStDoku.setBigDecimal("MAR" , _Main.nul);
      }
      
      
      //    ================================
       
       
       //qdsDokuStdoku.getBigDecimal("VC").subtract(qdsDokuStdoku.getBigDecimal("NC")));
        
        //    dummyStDoku.setBigDecimal("IPOR" , dummyStDoku.getBigDecimal("ISP").subtract(dummyStDoku.getBigDecimal("IBP")));
         //    dummyStDoku.setBigDecimal("IMAR" , dummyStDoku.getBigDecimal("IBP").subtract(dummyStDoku.getBigDecimal("INAB")));
          ////    dummyStDoku.setBigDecimal("IDOB", qdsDokuStdoku.getBigDecimal("NC").multiply(qdsDokuStdoku.getBigDecimal("KOL")));
           
           lookupData.getlookupData().raLocate(dm.getStanje(),
           new String[] {"GOD", "CSKL", "CART"},
           new String[] {fieldSet.getString("SGOD"), qdsStanjeArtikli.getString("CSKL"), String.valueOf(qdsStanjeArtikli.getInt("CART"))});
           
           //    System.out.println("dm.getstanje = " + dm.getStanje());
            
            
            //    System.out.println("qdsDokuStdoku.getBigDecimal(\"KOL\").compareTo(_Main.nul) != 0 " + (qdsDokuStdoku.getBigDecimal("KOL").compareTo(_Main.nul) != 0));
             //    System.out.println("(qdsDokuStdoku.getBigDecimal(\"KOL\").compareTo(_Main.nul) == 0 && isRazlikaPoZaokruzenju()) " + (qdsDokuStdoku.getBigDecimal("KOL").compareTo(_Main.nul) == 0 && isRazlikaPoZaokruzenju()));
              //    System.out.println("qdsDokuStdoku.getBigDecimal(\"KOL\").compareTo(_Main.nul) == 0 " + (qdsDokuStdoku.getBigDecimal("KOL").compareTo(_Main.nul) == 0));
               //    System.out.println("isRazlikaPoZaokruzenju() " + isRazlikaPoZaokruzenju());
                
                if (qdsStanjeArtikli.getBigDecimal("KOL").compareTo(_Main.nul) != 0 || 
                (qdsStanjeArtikli.getBigDecimal("KOL").compareTo(_Main.nul) == 0 && 
                isRazlikaPoZaokruzenju())){
                
                
                //      if(qdsDokuStdoku.getString("VRZAL").equals("N")) {
                 //        dummyStDoku.setBigDecimal("IPOR", _Main.nul);
                  //        dummyStDoku.setBigDecimal("IMAR", _Main.nul);
                   //      }
                    //      if(qdsDokuStdoku.getString("VRZAL").equals("V")) {
                     //        dummyStDoku.setBigDecimal("IPOR", _Main.nul);
                      //        dummyStDoku.setBigDecimal("IMAR", dm.getStanje().getBigDecimal("MARUL").subtract(
                       //                                          dm.getStanje().getBigDecimal("MARIZ")));
                        //      }
                         //      if(qdsDokuStdoku.getString("VRZAL").equals("M")){
                          //        dummyStDoku.setBigDecimal("IPOR", dm.getStanje().getBigDecimal("PORUL").subtract(
                           //                                          dm.getStanje().getBigDecimal("PORIZ")));
                            
                            //        dummyStDoku.setBigDecimal("IMAR", dm.getStanje().getBigDecimal("MARUL").subtract(
                             //                                          dm.getStanje().getBigDecimal("MARIZ")));
                              
                              if (lookupData.getlookupData().raLocate(dm.getArtikli(),"CART",String.valueOf(qdsStanjeArtikli.getInt("CART"))) &&
                              lookupData.getlookupData().raLocate(dm.getPorezi(),"CPOR",dm.getArtikli().getString("CPOR"))){
                              
                              if ((dm.getPorezi().getBigDecimal("POR1").compareTo(_Main.nul)) != 0){
                              BigDecimal porez1 = qdsStanjeArtikli.getBigDecimal("VC").multiply(dm.getPorezi().getBigDecimal("POR1").divide(new BigDecimal("100.00"),2,BigDecimal.ROUND_HALF_UP));
                              dummyStDoku.setBigDecimal("POR1", porez1);
                              }
                              else {
                              dummyStDoku.setBigDecimal("POR1", _Main.nul);
                              }
                              if ((dm.getPorezi().getBigDecimal("POR2").compareTo(_Main.nul)) != 0){
                              BigDecimal porez2 = qdsStanjeArtikli.getBigDecimal("VC").multiply(dm.getPorezi().getBigDecimal("POR2").divide(new BigDecimal("100.00"),2,BigDecimal.ROUND_HALF_UP));
                              dummyStDoku.setBigDecimal("POR2", porez2);
                              }
                              else {
                              dummyStDoku.setBigDecimal("POR2", _Main.nul);
                              }
                              if ((dm.getPorezi().getBigDecimal("POR3").compareTo(_Main.nul)) != 0){
                              BigDecimal porez3 = qdsStanjeArtikli.getBigDecimal("VC").multiply(dm.getPorezi().getBigDecimal("POR3").divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP));
                              dummyStDoku.setBigDecimal("POR3", porez3);
                              }
                              else {
                              dummyStDoku.setBigDecimal("POR3", _Main.nul);
                              }
                              }
                              if(qdsStanjeArtikli.getString("VRZAL").equals("M")){
                              if (dummyStDoku.getBigDecimal("IZAD").compareTo(dummyStDoku.getBigDecimal("ISP")) != 0){
                              
                              dummyStDoku.setBigDecimal("ISP", dummyStDoku.getBigDecimal("IZAD"));
                              
                              }
                              
                              }
                              } else {
                              dummyStDoku.setBigDecimal("INAB", _Main.nul);
                              dummyStDoku.setBigDecimal("IZAD", _Main.nul);
                              dummyStDoku.setBigDecimal("IPOR", _Main.nul);
                              dummyStDoku.setBigDecimal("IMAR", _Main.nul);
                              }
                              //    dummyStDoku.setString("SKAL",rCD.getKey(dummyStDoku));
                               
                               */
  }
  
  private void checkeq(QueryDataSet ds, String f1, String f2) {
    if (ds.getBigDecimal(f1).compareTo(ds.getBigDecimal(f2)) != 0)
      System.out.println("Razlika na artiklu "+ds.getInt("CART")+
          " "+f1+" " + ds.getBigDecimal(f1) + " <=> "+f2+" " + ds.getBigDecimal(f2));
  }
  
  private void checkmul(QueryDataSet ds, String fv, String fc) {
    if (ds.getBigDecimal("KOL").signum() != 0) {
      BigDecimal rc = ds.getBigDecimal(fv).divide(
          ds.getBigDecimal("KOL"), 2, BigDecimal.ROUND_HALF_UP);
      if (rc.compareTo(ds.getBigDecimal(fc)) != 0)
        System.out.println("Razlika na artiklu "+ds.getInt("CART")+
            " "+fc+" " + rc + " <=> " + ds.getBigDecimal(fc));
    }
  }

  public void addToStanje(String skladiste, String godina){
//    System.out.println("uso u addToStanje");
    String gdn;
    if(godina.equals("")) gdn=knjigodSet.getString("GOD"); /** @todo godina iz knjigod -DONE, BUT?!?!*/
    else gdn = godina;
//    System.out.println("gdn = " + gdn);
//    System.out.println("isPrijenosStanjaNula() tj. je li checkiran prijenos stanja nula : "+ isPrijenosStanjaNula());
//    System.out.println("isRazlikaPoZaokruzenju() tj. je li checkiran razlika po zaokruz : "+ isRazlikaPoZaokruzenju());
    QueryDataSet qdsStanje = ss.getObradeRadDvijeGodineSetDokuStdokuPocetnoStanje(skladiste, gdn, isPrijenosStanjaNula());
//    System.out.println("qdsStanje napravljeno, sad bi triba izac sysoutTEST");
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(qdsStanje);
//    System.out.println("qdsStanje.rowCount() " + qdsStanje.rowCount());
    if (qdsStanje.rowCount()>0){
      qdsStanje.first();
//      int i=1;
      do {
        insetrIntoStanje(qdsStanje, fieldSet.getString("GODINA"));
      } while (qdsStanje.next());
      qdsStanje.close();
    }
  }

  public void insetrIntoStanje(QueryDataSet stanje, String god) {
    String vrzal = stanje.getString("VRZAL");
    
    dummyStanje.insertRow(false);

    // ----------------- kljucevi -----------------
    dummyStanje.setString("GOD", god);//-Nova godina
    dummyStanje.setString("CSKL", stanje.getString("CSKL"));
    dummyStanje.setInt("CART", stanje.getInt("CART"));

    // ----------------- kolièine --------------------
    /**
     * Kolièine poèetnog stanja, ulaza i zalihe se prepisuju iz kolièine stanja u staroj godini
     */
    dummyStanje.setBigDecimal("KOLPS", stanje.getBigDecimal("KOL"));
    dummyStanje.setBigDecimal("KOLUL", stanje.getBigDecimal("KOL"));
    dummyStanje.setBigDecimal("KOL", stanje.getBigDecimal("KOL"));
    dummyStanje.setBigDecimal("KOLSKLADPS", stanje.getBigDecimal("KOLSKLAD"));
    dummyStanje.setBigDecimal("KOLSKLADUL", stanje.getBigDecimal("KOLSKLAD"));
    dummyStanje.setBigDecimal("KOLSKLAD", stanje.getBigDecimal("KOLSKLAD"));

    // ----------------- cijene -------------------
    /**
     * Cijene se kompletno prepisuju iz stanja u staroj godini
     */
    dummyStanje.setBigDecimal("NC", stanje.getBigDecimal("NC"));
    dummyStanje.setBigDecimal("VC", stanje.getBigDecimal("VC"));
    dummyStanje.setBigDecimal("MC", stanje.getBigDecimal("MC"));
    dummyStanje.setBigDecimal("ZC", stanje.getBigDecimal("ZC"));

    //poništavanje iznosa poèetnog stanja, te kolièina i iznose ulaza i izlaza
    
    dummyStanje.setBigDecimal("NABPS", _Main.nul);
    dummyStanje.setBigDecimal("MARPS", _Main.nul);
    dummyStanje.setBigDecimal("PORPS", _Main.nul);
    dummyStanje.setBigDecimal("VIZ", _Main.nul);
    
    dummyStanje.setBigDecimal("NABUL", _Main.nul);
    dummyStanje.setBigDecimal("MARUL", _Main.nul);
    dummyStanje.setBigDecimal("PORUL", _Main.nul);
    dummyStanje.setBigDecimal("VIZ", _Main.nul);
    
    dummyStanje.setBigDecimal("NABIZ", _Main.nul);
    dummyStanje.setBigDecimal("MARIZ", _Main.nul);
    dummyStanje.setBigDecimal("PORIZ", _Main.nul);
    dummyStanje.setBigDecimal("KOLIZ", _Main.nul);
    dummyStanje.setBigDecimal("VIZ", _Main.nul);

    dummyStanje.setBigDecimal("KOLREZ", _Main.nul);

    if(vrzal.equals("N")) { //-skladište se vodi po metodi prosjeèna nabavna cijena
      
      /**
       * NABPS = KOL*NC - nabavni iznos poèetnoga stanja
       * 
       * NABUL = KOL*NC - nabavni iznos ulaza
       * 
       * Kod formiranja poèetnoga stanja pretpostavka je:
       * NABPS = NABUL
       */
      
      dummyStanje.setBigDecimal("NABPS", 
          stanje.getBigDecimal("NABUL").subtract(stanje.getBigDecimal("NABIZ"))); //-nabavni iznos poèetnog stanja
      dummyStanje.setBigDecimal("NABUL", dummyStanje.getBigDecimal("NABPS")); //-nabavni iznos ulaza
      
            
    } else if(vrzal.equals("V")) { //-skladište se vodi po metodi zadnja prodajna cijena bez poreza
      
      /**
       * NABPS = KOL*NC - nabavni iznos poèetnoga stanja
       * MARPS = (VC*KOL)-(NC*KOL) - iznos marže poèetnog stanja
       * 
       * NABUL = KOL*NC - nabavni iznos ulaza
       * MARUL = (VC*KOL)-(NC*KOL) - iznos marže ulaza
       * 
       * Kod formiranja poèetnoga stanja pretpostavka je:
       * NABPS = NABUL
       * MARPS = MARUL
       */
      
      dummyStanje.setBigDecimal("NABPS", stanje.getBigDecimal("NABUL").subtract(stanje.getBigDecimal("NABIZ")));
      dummyStanje.setBigDecimal("MARPS", stanje.getBigDecimal("MARUL").subtract(stanje.getBigDecimal("MARIZ")));
      
      //dummyStanje.setBigDecimal("NABPS", stanje.getBigDecimal("KOL").multiply(stanje.getBigDecimal("NC"))); //-nabavni iznos poèetnog stanja
      //dummyStanje.setBigDecimal("MARPS", stanje.getBigDecimal("VC").multiply(stanje.getBigDecimal("KOL")).
		//	 							 subtract(stanje.getBigDecimal("NC").multiply(stanje.getBigDecimal("KOL")))); //-iznos marže poèetnog stanja

      dummyStanje.setBigDecimal("NABUL", dummyStanje.getBigDecimal("NABPS")); //-nabavni iznos ulaza
      dummyStanje.setBigDecimal("MARUL", dummyStanje.getBigDecimal("MARPS")); //-iznos marže ulaza
    
    } else { //-skladište se vodi po metodi zadnja prodajna cijena s porezom
      
      /**
       * NABPS = KOL*NC - nabavni iznos poèetnoga stanja
       * MARPS = (VC*KOL)-(NC*KOL) - iznos marže poèetnog stanja
       * PORPS = (MC*KOL)-(VC*KOL) - iznos poreza poèetnog stanja
       * 
       * NABUL = KOL*NC - nabavni iznos ulaza
       * MARUL = (VC*KOL)-(NC*KOL) - iznos marže ulaza
       * PORUL = (MC*KOL)-(VC*KOL) - iznos poreza ulaza
       * 
       * Kod formiranja poèetnoga stanja pretpostavka je:
       * NABPS = NABUL
       * MARPS = MARUL
       * PORPS = PORUL
       */
      
      dummyStanje.setBigDecimal("NABPS", stanje.getBigDecimal("NABUL").subtract(stanje.getBigDecimal("NABIZ")));
      dummyStanje.setBigDecimal("MARPS", stanje.getBigDecimal("MARUL").subtract(stanje.getBigDecimal("MARIZ")));
      dummyStanje.setBigDecimal("PORPS", stanje.getBigDecimal("PORUL").subtract(stanje.getBigDecimal("PORIZ")));
      
      /*dummyStanje.setBigDecimal("NABPS", stanje.getBigDecimal("KOL").multiply(stanje.getBigDecimal("NC"))); //-nabavni iznos poèetnog stanja
      dummyStanje.setBigDecimal("MARPS", stanje.getBigDecimal("VC").multiply(stanje.getBigDecimal("KOL")).
			 							 subtract(stanje.getBigDecimal("NC").multiply(stanje.getBigDecimal("KOL")))); //-iznos marže poèetnog stanja
      dummyStanje.setBigDecimal("PORPS", stanje.getBigDecimal("MC").multiply(stanje.getBigDecimal("KOL")).
			 							 subtract(stanje.getBigDecimal("VC").multiply(stanje.getBigDecimal("KOL")))); //-iznos poreza poèetnog stanja
*/
      dummyStanje.setBigDecimal("NABUL", dummyStanje.getBigDecimal("NABPS")); //-nabavni iznos ulaza
      dummyStanje.setBigDecimal("MARUL", dummyStanje.getBigDecimal("MARPS")); //-iznos marže ulaza
      dummyStanje.setBigDecimal("PORUL", dummyStanje.getBigDecimal("PORPS")); //-iznos poreza ulaza
    
    }

    // ----------------------- vrijednosti --------------------------
    /**
     * Po nekoj logici ako je KOLPS = KOLUL = KOL = STAROSTANJE.KOL logièno je zakljuèiti sljedeæe
     * VPS = VUL = VRI = STAROSTANJE.VRI - Ispravite me ako griješim!!! 
     * Pa onda postavljam sve na vrijednost zalihe u staroj godini.
     */
    
    dummyStanje.setBigDecimal("VPS", stanje.getBigDecimal("VRI")); //- vrijednost poèetnoga stanja
    dummyStanje.setBigDecimal("VUL", stanje.getBigDecimal("VRI")); //- vrijednost ulaza
    dummyStanje.setBigDecimal("VRI", stanje.getBigDecimal("VRI")); //- vrijednost zalihe
    
    /**
     * Provjera da li je vrijednost zalihe jednaka kolièini pomnoženoj sa cijenom zalihe
     */
    checkmul(dummyStanje, "VRI", "ZC");
    /*if (stanje.getBigDecimal("VRI").compareTo(stanje.getBigDecimal("KOL").multiply(stanje.getBigDecimal("ZC"))) != 0) {
      dummyStanje.setBigDecimal("VPS", stanje.getBigDecimal("KOL").multiply(stanje.getBigDecimal("ZC")));
      dummyStanje.setBigDecimal("VUL", stanje.getBigDecimal("KOL").multiply(stanje.getBigDecimal("ZC")));
      dummyStanje.setBigDecimal("VRI", stanje.getBigDecimal("KOL").multiply(stanje.getBigDecimal("ZC")));
    }*/

    BigDecimal ulazi = dummyStanje.getBigDecimal("NABUL").add(dummyStanje.getBigDecimal("MARUL").add(dummyStanje.getBigDecimal("PORUL")));
    BigDecimal pocst = dummyStanje.getBigDecimal("NABPS").add(dummyStanje.getBigDecimal("MARPS").add(dummyStanje.getBigDecimal("PORPS")));
    
    if (ulazi.compareTo(dummyStanje.getBigDecimal("VUL")) != 0){
      if (vrzal.equals("N")) {
        dummyStanje.setBigDecimal("MARUL", zero);
        dummyStanje.setBigDecimal("PORUL", zero);
        dummyStanje.setBigDecimal("NABUL", dummyStanje.getBigDecimal("VUL"));
      } else {
        if (vrzal.equals("V")) dummyStanje.setBigDecimal("PORUL", zero);
        dummyStanje.setBigDecimal("MARUL",(dummyStanje.getBigDecimal("MARUL").add(dummyStanje.getBigDecimal("VUL").subtract(ulazi))));
      }
      System.out.println("Provjera NABUL+MARUL+PORUL = VUL - POPRAVLJENO!");
    } 

    if (pocst.compareTo(dummyStanje.getBigDecimal("VPS")) != 0){
      if (vrzal.equals("N")) {
        dummyStanje.setBigDecimal("MARPS", zero);
        dummyStanje.setBigDecimal("PORPS", zero);
        dummyStanje.setBigDecimal("NABPS", dummyStanje.getBigDecimal("VPS"));
      } else {
        if (vrzal.equals("V")) dummyStanje.setBigDecimal("PORPS", zero);
        dummyStanje.setBigDecimal("MARPS",(dummyStanje.getBigDecimal("MARPS").add(dummyStanje.getBigDecimal("VPS").subtract(pocst))));
      }
      System.out.println("Provjera NABPS+MARPS+PORPS = VPS - POPRAVLJENO!");
    }
    
    if (stanje.getBigDecimal("KOL").compareTo(_Main.nul) == 0) {
      if (!isPrijenosStanjaNula() || (isPrijenosStanjaNula() && !isRazlikaPoZaokruzenju())){
        dummyStanje.setBigDecimal("VRI", _Main.nul);
        dummyStanje.setBigDecimal("NABPS", _Main.nul);
        dummyStanje.setBigDecimal("MARPS", _Main.nul);
        dummyStanje.setBigDecimal("PORPS", _Main.nul);
        dummyStanje.setBigDecimal("NABUL", _Main.nul);
        dummyStanje.setBigDecimal("MARUL", _Main.nul);
        dummyStanje.setBigDecimal("PORUL", _Main.nul);
      }
    }
  }

  public void setDummys(){
    dummyDoku = hr.restart.baza.Doku.getDataModule().getTempSet("1=0");
    dummyStDoku = hr.restart.baza.Stdoku.getDataModule().getTempSet("1=0");
    dummyStanje = hr.restart.baza.Stanje.getDataModule().getTempSet("1=0");
    dummyDoku.open();
    dummyStDoku.open();
    dummyStanje.open();
  }

  protected boolean radU2God = false;
  DataSet knjigodSet;

  public void godina(boolean lukap){

    knjigodSet = hr.restart.baza.Knjigod.getDataModule().getFilteredDataSet("CORG='" + fieldSet.getString("CORG") + "' AND APP='robno'");
    knjigodSet.open();

//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(knjigodSet);

    if (lukap) {
      fieldSet.setString("CSKL","");
      jlrSklad.forceFocLost();
      skladistaSet = ss.getObradeRadDvijeGodineDohvatSkladista(fieldSet.getString("CORG"));
      try {
        radU2God = knjigodSet.getString("STATRADA").equals("D");
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      jlrSklad.setRaDataSet(skladistaSet);
//      System.out.println("rad u dvi godine = " + radU2God);
    }

    try { /** @todo godina sad ide iz knjigod -DONE!!!*/
      int god = Integer.parseInt(knjigodSet.getString("GOD"))+1;
      fieldSet.setString("SGOD", knjigodSet.getString("GOD"));
      fieldSet.setString("GODINA", String.valueOf(god));
    } catch(Exception ex) {
      ex.printStackTrace();
      fieldSet.setString("SGOD", "");
      fieldSet.setString("GODINA", "");
    }
  }

  public void defolts(){
    fieldSet.setString("CORG", hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG());
    jlrKnjig.forceFocLost();
    fieldSet.setString("CSKL", "");
    jlrSklad.forceFocLost();
    godina(true);
    jcbStanjeArtiklaNula.setSelected(false);
    jcbPrijelazBezObrade.setSelected(false);
    enabDisabCbox();
//    jlrKnjig.selectAll();
    jcbStanjeArtiklaNula.requestFocus();
  }

  public void onOff(boolean shto){
    rcc.EnabDisabAll(mainPanel, shto);
    if(shto){
      rcc.setLabelLaF(jraGodina, !shto);
      rcc.setLabelLaF(jlrKnjig, !shto);
      rcc.setLabelLaF(jlrNazKnjig, !shto);
    }
  }

  void jcbStanjeArtiklaNula_itemStateChanged(ItemEvent e) {
    enabDisabCbox();
  }

  void enabDisabCbox(){
    rcc.setLabelLaF(jcbRazlikaPoZaokruzenju, jcbStanjeArtiklaNula.isSelected());
    if (!jcbStanjeArtiklaNula.isSelected()){
      jcbRazlikaPoZaokruzenju.setSelected(jcbStanjeArtiklaNula.isSelected());
    }
    rcc.setLabelLaF(jcbPrijelazBezObrade,radU2God);
  }

  protected boolean isPrijenosStanjaNula(){
    return jcbStanjeArtiklaNula.isSelected();
  }

  protected boolean isRazlikaPoZaokruzenju(){
    return jcbRazlikaPoZaokruzenju.isSelected();
  }

  String updateKnjigod, updateSklad;

  protected boolean saveDataInMyneTransaction(final boolean isBrisanjeStanja, final boolean isSnimiStanje, final boolean isSnimiPromete, final boolean isAzuriratiKnjigodSklad){
//    if (true) return false;
    raLocalTransaction saveMyData = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        
        try {
          if (isSnimiPromete){
//            System.out.println("A - snimam promete"); //XDEBUG delete when no more needed
//            System.out.println("doku.rowcount   = "+dummyDoku.rowCount()); //XDEBUG delete when no more needed
//            System.out.println("stdoku.rowcount = "+dummyStDoku.rowCount()); //XDEBUG delete when no more needed
            raTransaction.saveChanges(dummyDoku);
//            System.out.println("Gotovo - doku"); //XDEBUG delete when no more needed
            raTransaction.saveChanges(dummyStDoku);
//            System.out.println("Gotovo - stdoku"); //XDEBUG delete when no more needed
          } 
          if (isSnimiStanje){
//            System.out.println("B - snimam stanje"); //XDEBUG delete when no more needed
            raTransaction.saveChanges(dummyStanje);
//            System.out.println("Gotovo"); //XDEBUG delete when no more needed
          }

          if (isAzuriratiKnjigodSklad){
//            System.out.println("C - azuriram tablice"); //XDEBUG delete when no more needed
            raTransaction.runSQL(updateKnjigod);
            raTransaction.runSQL(updateSklad);
//            System.out.println("Gotovo"); //XDEBUG delete when no more needed
          }

          if (isBrisanjeStanja){

            raPrepDelStatments rpds = new raPrepDelStatments(){
              public void  execute_plus(){}
            };

            for(int f=0; f<updateSkladArray.length; f++){
//              try {
//                System.out.println(fieldSet.getString("SGOD") + "  updateSkladArray[" + f + "] = " + updateSkladArray[f] + " check - " + rpds.del_obrada_promet__outTransaction(updateSkladArray[f], fieldSet.getString("SGOD")));
//                System.out.println("                              check - " + rpds.del_obrada_prometOJ_outTransaction(updateSkladArray[f], fieldSet.getString("SGOD")));

              rpds.del_obrada_promet__outTransaction(updateSkladArray[f], fieldSet.getString("SGOD"));
              rpds.del_obrada_prometOJ_outTransaction(updateSkladArray[f], fieldSet.getString("SGOD"));
//              }
//              catch (Exception ex) {
//                System.err.print("Exception: ");
//                System.err.println(ex.getMessage());
//              }
            }
          }

        return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };

//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(dummyStanje);
//    syst.prn(dummyDoku);
//    syst.prn(dummyStDoku);
    return (saveMyData.execTransaction());
  }
}
