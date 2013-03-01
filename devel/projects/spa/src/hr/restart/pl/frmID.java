/****license*****************************************************************
**   file: frmID.java
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
package hr.restart.pl;

import hr.restart.baza.Kumulrad;
import hr.restart.baza.Opcine;
import hr.restart.baza.dM;
import hr.restart.db.raVariant;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;
import hr.restart.util.sysoutTEST;
import hr.restart.zapod.dlgGetKnjig;

import java.awt.BorderLayout;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmID extends raUpitLite {

  dM dm = dM.getDataModule();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  Util ut = Util.getUtil();

  JPanel mainPanel = new JPanel();
  JPanel jPanel1 = new JPanel();
  private BorderLayout borderLayout1 = new BorderLayout();
  private XYLayout xYLayout1 = new XYLayout();

  JraTextField jraMjesec = new JraTextField();
  JraTextField jraGodina = new JraTextField();
  JraTextField jraInozemstvo = new JraTextField();
  JraTextField jraIdentifikator = new JraTextField();

  JLabel jlMjGod = new JLabel();
  JLabel jlMjesec = new JLabel();
  JLabel jlGodina = new JLabel();
  JLabel jlInozemstvo = new JLabel();
  JLabel jlIdentifikator = new JLabel("Identifikator");

  Column colMjesec = new Column();
  Column colGodina = new Column();
  Column colInozemstvo = new Column();
  Column colIdent = new Column();

  Column col_II_1 = new Column();
  Column col_II_2 = new Column();
  Column col_II_3 = new Column();
  Column col_III_1_1 = new Column();
  Column col_III_1_2 = new Column();
  Column col_III_1_3 = new Column();
  Column col_III_1_3A = new Column();
  Column col_III_1_4 = new Column();
  Column col_III_2 = new Column();
  Column col_III_3_1 = new Column();
  Column col_III_3_2 = new Column();
  Column col_III_3_3 = new Column();
  Column col_III_4 = new Column();
  Column col_III_5 = new Column();
  Column col_III_6 = new Column();
  Column col_III_7_1 = new Column();
  Column col_III_7_2 = new Column();
  Column col_V_3_6 = new Column();
  Column col_III_4_4_2_2005 = new Column();
  Column col_rkp010 = new Column();
  Column col_rkp020 = new Column();
  Column col_rkp030 = new Column();
  Column col_rkp040 = new Column();
  Column col_rkp050 = new Column();

  StorageDataSet fieldSet = new StorageDataSet();
  StorageDataSet repSet = new StorageDataSet();
  StorageDataSet repSetStrB = new StorageDataSet();
  QueryDataSet knjigovodstvo;
  private char repMode;
  private String wrapCVRO;
  public frmID() {
    this('A');
  }

  static frmID thisFrmID;

  public frmID(char _repMode) {
    try {
      repMode = _repMode;
      isObr = repMode == 'O';
      if (!isObr){
        thisFrmID = this;
        addRep();
      }
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  static boolean isObr = false;
  public static frmID getInstance(){
    if (isObr) return frmIDObr.getInstanceObr();
    return thisFrmID;
  }

  public void componentShow() {
    try {
      raIniciranje.getInstance().posOrgsPl(hr.restart.zapod.OrgStr.getKNJCORG());
      //ut.getMonth(dm.getOrgpl().getTimestamp("DATUMISPL"))
      fieldSet.setInt("GODINA", Integer.parseInt(ut.getYear(dm.getOrgpl().getTimestamp("DATUMISPL"))));
      fieldSet.setInt("MJESEC", Integer.parseInt(ut.getMonth(dm.getOrgpl().getTimestamp("DATUMISPL"))));
      fieldSet.setBigDecimal("INOZEMSTVO", new java.math.BigDecimal("0"));
    }
    catch (NumberFormatException sex) {
    }
    if (getRepMode() == 'O'){
      jraInozemstvo.requestFocus();
      jraMjesec.setEditable(false);
      jraGodina.setEditable(false);
    }
    else
      jraMjesec.selectAll();
  }

  public static String getOjWith() {
    return frmParam.getParam("pl", "ojwithID"+dlgGetKnjig.getKNJCORG(), "", "Sa kojim još knjigovodstvom da zbroji ID za knjigov. "+dlgGetKnjig.getKNJCORG());
  }
  public void okPress() {
    getOKPanel().requestFocus();
    rcc.EnabDisabAll(jPanel1, false);
    knjigovodstvo = ut.getNewQueryDataSet(getKnjigovodstvoSQL());

    /*kumulorgStr ="select sum(bruto) as bruto, sum(doprinosi) as doprinosi, sum(neto) as neto, "+
                 "sum(iskneop) as iskneop, sum(porosn) as porosn, sum(poruk) as poruk, "+
                 "sum(prir) as prir from " + this.getKumulorgTableName() +
                 getWhereSQL(getKumulorgTableName(),"where"); // + " and corg = '"+  +"'";*/

    String prefix_kumulorgStr = "select cvro, bruto as bruto, doprinosi as doprinosi, neto as neto, "+
                  "iskneop as iskneop, porosn as porosn, poruk as poruk, "+
                  "prir as prir from " + this.getKumulorgTableName();
    kumulorgStr = prefix_kumulorgStr + getWhereSQL(getKumulorgTableName(),"where",dlgGetKnjig.getKNJCORG()); 
    if (!"".equals(getOjWith())) {
      kumulorgStr = kumulorgStr + " UNION " + prefix_kumulorgStr + getWhereSQL(getKumulorgTableName(),"where",getOjWith());
    }
System.out.println("kumulorgStr::::: "+kumulorgStr);
/* ne koristi se jer se prirez i porez vuku iz odbitaka = virmani = rekapitulacija    
    if(isObr)idBsql = "SELECT poruk, prir, poriprir, copcine "+
                      "FROM Kumulrad, radnici, radnicipl, opcine "+
                      "WHERE kumulrad.cradnik = radnici.cradnik "+
                      "AND kumulrad.cradnik = radnicipl.cradnik "+
                      "AND radnici.cradnik = radnicipl.cradnik "+
                      "AND radnicipl.copcine = opcine.copcine "+
                      getWhereSQL("kumulrad", "and");


    else idBsql = "SELECT poruk, prir, poriprir, copcine "+
                  "FROM Kumulradarh "+
                  getWhereSQL("kumulradarh", "where");

    System.out.println("b strana sql - "+idBsql+"\nOBRACUN - "+isObr);
*/
    II_1    = "select COUNT (DISTINCT "+getRadniciTableNameQ()+".cradnik) from " + this.getKumulradTableName() +
              getWhereSQL(this.getKumulradTableName(),(this.getRepMode() == 'A')?"where":"and", dlgGetKnjig.getKNJCORG());

    II_3    = "select max(godobr) as godina, max(mjobr) as mjesec from " + this.getKumulorgTableName() +
              getWhereSQL(this.getKumulorgTableName(),"where", dlgGetKnjig.getKNJCORG()) +
              " group by mjobr, godobr";

    makeRepSet();
    isObr = getRepMode() == 'O';
    if (getRepMode() == 'A'){
//      SwingUtilities.invokeLater(new Runnable() {
//        public void run() {
//          getOKPanel().requestFocus();
//          rcc.EnabDisabAll(jPanel1, false);
//        }
//      });
    }
  }

  int godina, mjesec;
  java.math.BigDecimal zzuin;
  String ident;
  String II_1, II_3, kumulorgStr;
//  String idBsql; //zamijenjen sa idBporsql i idBprirsql

  public boolean Validacija(){
    godina = fieldSet.getInt("GODINA");
    mjesec = fieldSet.getInt("MJESEC");
    zzuin = fieldSet.getBigDecimal("INOZEMSTVO");
    ident = fieldSet.getString("IDENTIFIKATOR");
    if (jraMjesec.getText().equals("") || mjesec == 0) {
      javax.swing.JOptionPane.showMessageDialog(this.getContentPane(),
          new hr.restart.swing.raMultiLineMessage(new String[]{"Obavezan unos","Mjesec obrade"}),
          "Greška",
          javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (jraGodina.getText().equals("") || godina == 0) {
      javax.swing.JOptionPane.showMessageDialog(this.getContentPane(),
          new hr.restart.swing.raMultiLineMessage(new String[]{"Obavezan unos","Godina obrade"}),
          "Greška",
          javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }

    String controlStr = "select count(*) from " + getKumulorgTableName() + getWhereSQL(getKumulorgTableName(),"where", dlgGetKnjig.getKNJCORG());
    QueryDataSet controlSet = ut.getNewQueryDataSet(controlStr);
    int withcount = 0;
    if (!"".equals(getOjWith())) {
      withcount = vl.getSetCount(ut.getNewQueryDataSet(
          "select count(*) from " + getKumulorgTableName() + getWhereSQL(getKumulorgTableName(),"where", getOjWith())
          ),0);
    }
    if ((withcount+vl.getSetCount(controlSet,0)) == 0){
      javax.swing.JOptionPane.showMessageDialog(this.getContentPane(),
          new hr.restart.swing.raMultiLineMessage(new String[]{"Nema podataka za " + mjesec + ". mjesec " + godina + ". godine"}),
          "Pozor",
          javax.swing.JOptionPane.WARNING_MESSAGE);
      return false;
    }
//    knjigovodstvo = ut.getNewQueryDataSet(getKnjigovodstvoSQL());
//
//    /*kumulorgStr ="select sum(bruto) as bruto, sum(doprinosi) as doprinosi, sum(neto) as neto, "+
//                 "sum(iskneop) as iskneop, sum(porosn) as porosn, sum(poruk) as poruk, "+
//                 "sum(prir) as prir from " + this.getKumulorgTableName() +
//                 getWhereSQL(getKumulorgTableName(),"where"); // + " and corg = '"+  +"'";*/
//
//    kumulorgStr = "select bruto as bruto, doprinosi as doprinosi, neto as neto, "+
//                  "iskneop as iskneop, porosn as porosn, poruk as poruk, "+
//                  "prir as prir from " + this.getKumulorgTableName() +
//                  getWhereSQL(getKumulorgTableName(),"where"); // + " and corg = '"+  +"'";
//
//    idBsql = "SELECT poruk, prir, poriprir, copcine "+
//             "FROM Kumulradarh "+
//             getWhereSQL("kumulradarh","where");
//
//
//    System.out.println("b strana sql - "+idBsql);
//
//    II_1    = "select COUNT (DISTINCT cradnik) from " + this.getKumulradTableName() +
//              getWhereSQL(this.getKumulradTableName(),(this.getRepMode() == 'A')?"where":"and");
//
//    II_3    = "select max(godobr) as godina, max(mjobr) as mjesec from " + this.getKumulorgTableName() +
//              getWhereSQL(this.getKumulorgTableName(),"where") +
//              " group by mjobr, godobr";
//
//    makeRepSet();
    return true;
  }

  java.math.BigDecimal NULA = new java.math.BigDecimal(0);
  

  private java.math.BigDecimal getHartAttack(String rmiParam, String colName, boolean odbiciTabela){
//    System.out.println("rmiParam - " + rmiParam);
    if (rmiParam.equals("")) return NULA;
    String odbiciOrPrimanja = odbiciTabela?getOdbiciTableName():getPrimanjaTableName();
    String pref_pok = "select "+((this.getRepMode() == 'A')?"kumulradarh.cradnik":"radnici.cradnik")+", " + colName + " as "+ colName + " from " + odbiciOrPrimanja;
    String pok = pref_pok + getWhereSQL(odbiciOrPrimanja,"and", dlgGetKnjig.getKNJCORG()) + rmiParam;
    if (!"".equals(getOjWith())) {
      pok = pok + " UNION " + pref_pok + getWhereSQL(odbiciOrPrimanja,"and", getOjWith()) + rmiParam;
    }
    
System.out.println("POK - " + pok);
    
    QueryDataSet tidamtidamtidam = ut.getNewQueryDataSet(pok);
    tidamtidamtidam.first();
    java.math.BigDecimal sumum = NULA;
    do {
      sumum = sumum.add(tidamtidamtidam.getBigDecimal(colName));
    } while (tidamtidamtidam.next());
    return sumum;
  }
  private void makeRepSet(){   // A.K.A. SQL BOMBER :)
    if (!repSet.isOpen()) repSet.open();
    else repSet.deleteAllRows();
    repSet.insertRow(false);

    QueryDataSet qdsII_1 = ut.getNewQueryDataSet(II_1, true);
    repSet.setInt("II_1", vl.getSetCount(qdsII_1,0)); //qdsII_1.getInt("COUNT"));



    QueryDataSet temporary = ut.getNewQueryDataSet(kumulorgStr, true);
    QueryDataSet kumulorgQve = new QueryDataSet();
    kumulorgQve.setColumns(temporary.cloneColumns());
    kumulorgQve.open();
    kumulorgQve.insertRow(false);
    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);

    syst.prn(temporary);

    temporary.first();
//Uhvati cvro, ako ih ima više - sranje! Ionako je cijeli kod sranje
    wrapCVRO = temporary.getString("CVRO");
//    temporary.copyTo(kumulorgQve);
//
//    if (temporary.next()) {
      do {
        kumulorgQve.setBigDecimal("BRUTO",kumulorgQve.getBigDecimal("BRUTO").add(temporary.getBigDecimal("BRUTO")));
        kumulorgQve.setBigDecimal("DOPRINOSI",kumulorgQve.getBigDecimal("DOPRINOSI").add(temporary.getBigDecimal("DOPRINOSI")));
        kumulorgQve.setBigDecimal("NETO",kumulorgQve.getBigDecimal("NETO").add(temporary.getBigDecimal("NETO")));
        kumulorgQve.setBigDecimal("ISKNEOP",kumulorgQve.getBigDecimal("ISKNEOP").add(temporary.getBigDecimal("ISKNEOP")));
        kumulorgQve.setBigDecimal("POROSN",kumulorgQve.getBigDecimal("POROSN").add(temporary.getBigDecimal("POROSN")));
        kumulorgQve.setBigDecimal("PORUK",kumulorgQve.getBigDecimal("PORUK").add(temporary.getBigDecimal("PORUK")));
        kumulorgQve.setBigDecimal("PRIR",kumulorgQve.getBigDecimal("PRIR").add(temporary.getBigDecimal("PRIR")));

      } while (temporary.next());
//    }

    try {
      //temporary = ut.getNewQueryDataSet(idBsql);
      String _odbtab = "odbici".concat(isObr?"obr":"arh");
      String _radtab = isObr?"radnicipl":"kumulradarh";
      String _join = isObr?"radnicipl.cradnik = odbiciobr.cradnik"
          :"kumulradarh.godobr = odbiciarh.godobr"
          +" AND kumulradarh.mjobr = odbiciarh.mjobr"
          +" AND kumulradarh.rbrobr = odbiciarh.rbrobr"
          +" AND kumulradarh.cradnik = odbiciarh.cradnik";

      String idBporsql_prefix = "SELECT "+_odbtab+".cradnik, "+_odbtab+".obriznos, "+_odbtab+".ckey as copcine from "+_odbtab+", "+_radtab
        + " WHERE "+_join+" AND ("
        + raOdbici.getInstance().getOdbiciWhereQuery(raOdbici.POREZ_param,_odbtab)
        + " OR " + raOdbici.getInstance().getOdbiciWhereQuery(raOdbici.RAZPOREZA_param,_odbtab)+ ") ";
      String idBporsql = idBporsql_prefix + getWhereSQL(_radtab,"AND", dlgGetKnjig.getKNJCORG());
      if (!"".equals(getOjWith())) {
        idBporsql = idBporsql + " UNION " + idBporsql_prefix + getWhereSQL(_radtab,"AND", getOjWith());
      }
      
      String idBprirsql_prefix = "SELECT "+_odbtab+".cradnik, "+_odbtab+".obriznos, "+_odbtab+".ckey as copcine from "+_odbtab+", "+_radtab
	      + " WHERE "+_join+" AND ("
	      + raOdbici.getInstance().getOdbiciWhereQuery(raOdbici.PRIREZ_param,_odbtab)
          + " OR " + raOdbici.getInstance().getOdbiciWhereQuery(raOdbici.RAZPRIREZA_param,_odbtab)+ ") ";
      String idBprirsql = idBprirsql_prefix + getWhereSQL(_radtab,"AND", dlgGetKnjig.getKNJCORG());
      if (!"".equals(getOjWith())) {
        idBprirsql = idBprirsql + " UNION " + idBprirsql_prefix + getWhereSQL(_radtab,"AND", getOjWith());
      }
      
      System.out.println(idBporsql);
      System.out.println(idBprirsql);
      QueryDataSet idBporqds = Util.getNewQueryDataSet(idBporsql);
      QueryDataSet idBprirqds = Util.getNewQueryDataSet(idBprirsql);
      idBporqds.setRowId("CRADNIK", true);
      idBprirqds.setRowId("CRADNIK", true);
//      idBporqds.open();
//      idBprirqds.open();
//      sysoutTEST syst = new sysoutTEST(false);
//      syst.prn(temporary);
      try {
//        System.out.println("openiram repSetStrB");
//        repSetStrB.setColumns(temporary.cloneColumns());
        repSetStrB.setColumns(new Column[] {
           Opcine.getDataModule().getColumn("COPCINE").cloneColumn(),
           Kumulrad.getDataModule().getColumn("PORUK").cloneColumn(),
           Kumulrad.getDataModule().getColumn("PRIR").cloneColumn(),
           Kumulrad.getDataModule().getColumn("PORIPRIR").cloneColumn()
        });
        repSetStrB.open();
      } catch (Exception ex){
        ex.printStackTrace();
//        System.out.println("Brisem sve iz repSetStrB");
        repSetStrB.deleteAllRows();
      }
//      System.out.println("... done");
      for (idBporqds.first(); idBporqds.inBounds(); idBporqds.next()) {
        boolean raz = idBporqds.getString("CRADNIK").trim().equals(idBporqds.getString("COPCINE").trim());//razlika poreza
        //ugurati pravi copcine u razliku poreza 
        if (raz) {
          String rzsql = "SELECT copcine from "+_radtab+" where "+_radtab+".cradnik = '"+idBporqds.getString("CRADNIK")+"' "+getWhereSQL(_radtab,"AND");
System.err.println(rzsql);
          QueryDataSet rzopcset = Aus.q(rzsql);
          if (rzopcset.getRowCount() > 0) {
            rzopcset.first();
            idBporqds.setString("COPCINE", rzopcset.getString("COPCINE"));
            raz = false;
          } else {
            raz = true;
          }
        }
        if (!raz && !lookupData.getlookupData().raLocate(repSetStrB,"COPCINE",idBporqds.getString("COPCINE"))){
          repSetStrB.insertRow(false);
          repSetStrB.setString("COPCINE",idBporqds.getString("COPCINE").trim());
          repSetStrB.setBigDecimal("PORUK",idBporqds.getBigDecimal("OBRIZNOS"));
          repSetStrB.setBigDecimal("PORIPRIR",idBporqds.getBigDecimal("OBRIZNOS"));
          repSetStrB.post();
        } else {
          repSetStrB.setBigDecimal("PORUK",repSetStrB.getBigDecimal("PORUK")
              .add(idBporqds.getBigDecimal("OBRIZNOS")));
          repSetStrB.setBigDecimal("PORIPRIR",repSetStrB.getBigDecimal("PORIPRIR")
              .add(idBporqds.getBigDecimal("OBRIZNOS")));
          repSetStrB.post();          
        }
      }
      for (idBprirqds.first(); idBprirqds.inBounds(); idBprirqds.next()) {
        boolean raz = idBprirqds.getString("CRADNIK").trim().equals(idBprirqds.getString("COPCINE").trim());//razlika prireza
        if (raz) {//malo kopipejsta nije na odmet
          String rzsql = "SELECT copcine from "+_radtab+" where "+_radtab+".cradnik = '"+idBprirqds.getString("CRADNIK")+"' "+getWhereSQL(_radtab,"AND");
System.err.println(rzsql);
          QueryDataSet rzopcset = Aus.q(rzsql);
          if (rzopcset.getRowCount() > 0) {
            rzopcset.first();
            idBprirqds.setString("COPCINE", rzopcset.getString("COPCINE"));
            raz = false;
          } else {
            raz = true;
          }
        }
        if (raz || lookupData.getlookupData().raLocate(repSetStrB,"COPCINE",idBprirqds.getString("COPCINE"))){
          repSetStrB.setBigDecimal("PRIR",repSetStrB.getBigDecimal("PRIR")
              .add(idBprirqds.getBigDecimal("OBRIZNOS")));
          repSetStrB.setBigDecimal("PORIPRIR",repSetStrB.getBigDecimal("PORIPRIR")
              .add(idBprirqds.getBigDecimal("OBRIZNOS")));
          repSetStrB.post();        
        }
      }      
      sysoutTEST ST = new sysoutTEST(false);
      ST.prn(repSetStrB);
/*
      temporary.first();

      do {
        if (!lookupData.getlookupData().raLocate(repSetStrB,"COPCINE",temporary.getString("COPCINE"))){
          repSetStrB.insertRow(false);
          temporary.copyTo(repSetStrB);
        } else {
          repSetStrB.setBigDecimal("PORUK", repSetStrB.getBigDecimal("PORUK").add(temporary.getBigDecimal("PORUK")));
          repSetStrB.setBigDecimal("PRIR", repSetStrB.getBigDecimal("PRIR").add(temporary.getBigDecimal("PRIR")));
          repSetStrB.setBigDecimal("PORIPRIR", repSetStrB.getBigDecimal("PORIPRIR").add(temporary.getBigDecimal("PORIPRIR")));
        }
      } while (temporary.next());
*/
      //    sysoutTEST syst = new sysoutTEST(false);
//      syst.prn(repSetStrB);
    }
    catch (Exception ex) {
      ex.printStackTrace();
//      System.out.println("BLEEEEEEEEE..... exepshn :)");
    }

    repSet.setBigDecimal("II_2", kumulorgQve.getBigDecimal("BRUTO"));
    repSet.setBigDecimal("III_2", kumulorgQve.getBigDecimal("DOPRINOSI"));
    repSet.setBigDecimal("III_4", kumulorgQve.getBigDecimal("NETO"));
    repSet.setBigDecimal("III_5", kumulorgQve.getBigDecimal("ISKNEOP"));
    repSet.setBigDecimal("III_6", kumulorgQve.getBigDecimal("POROSN"));
    repSet.setBigDecimal("III_7_1", kumulorgQve.getBigDecimal("PORUK"));
    repSet.setBigDecimal("III_7_2", kumulorgQve.getBigDecimal("PRIR"));

    String tri="";
    if(this.getRepMode() == 'A'){
      QueryDataSet qdsII_3 = ut.getNewQueryDataSet(II_3, true);
      qdsII_3.setSort(new SortDescriptor(new String[] {"GODINA","MJESEC"}));
      qdsII_3.first();
      do {
        tri = tri + qdsII_3.getShort("MJESEC") + "/" + qdsII_3.getShort("GODINA") + "  ";
      } while (qdsII_3.next());
    } else {
      tri = tri + getMjesec() + "/" + getGodina() + "  ";
    }
    if (tri.length()>50) tri = tri.substring(0, 50);
    repSet.setString("II_3", tri);
    repSet.setBigDecimal("V_3_6", getHartAttack(odbiciWh(raIzvjestaji.ID03_3_6),"OBRIZNOS", true));
    repSet.setBigDecimal("III_1_1", getHartAttack(odbiciWh(raIzvjestaji.ID_1_1),"OBRIZNOS", true));
    repSet.setBigDecimal("III_1_2", getHartAttack(odbiciWh(raIzvjestaji.ID_1_2),"OBRIZNOS", true));
    repSet.setBigDecimal("III_1_3", getHartAttack(odbiciWh(raIzvjestaji.ID_1_3),"OBRIZNOS", true));
    repSet.setBigDecimal("III_1_4", getHartAttack(odbiciWh(raIzvjestaji.ID_1_4),"OBRIZNOS", true));
    repSet.setBigDecimal("III_3_1", getHartAttack(odbiciWh(raIzvjestaji.ID_3_1),"OBRIZNOS", true));
    repSet.setBigDecimal("III_3_2", getHartAttack(odbiciWh(raIzvjestaji.ID_3_2),"OBRIZNOS", true));
    repSet.setBigDecimal("III_3_3", getHartAttack(odbiciWh(raIzvjestaji.ID_3_3),"OBRIZNOS", true));
//    System.out.println("----------------------------------------------------------------------------------");
    repSet.setBigDecimal("III05_4_4_2", getHartAttack(odbiciWh(raIzvjestaji.ID05_4_4_2),"OBRIZNOS", true));
//    System.out.println("----------------------------------------------------------------------------------");
    repSet.setBigDecimal("RKP020", getHartAttack(odbiciWh(raIzvjestaji.ID_020),"BRUTO", false));
    repSet.setBigDecimal("RKP030", getHartAttack(odbiciWh(raIzvjestaji.ID_030),"BRUTO", false));
    repSet.setBigDecimal("RKP040", getHartAttack(odbiciWh(raIzvjestaji.ID_040),"BRUTO", false));
    repSet.setBigDecimal("RKP050", getHartAttack(odbiciWh(raIzvjestaji.ID_050),"BRUTO", false));
    repSet.setBigDecimal("RKP010", kumulorgQve.getBigDecimal("BRUTO").
                                     subtract(repSet.getBigDecimal("RKP050").
                                     subtract(repSet.getBigDecimal("RKP040").
                                     subtract(repSet.getBigDecimal("RKP030").
                                     subtract(repSet.getBigDecimal("RKP020"))))));

    repSet.setBigDecimal("III_1_3A", getHartAttack(odbiciWh(raIzvjestaji.ID_1_2002),"OBRIZNOS", true));//getIII_1_3A());
  }

  private java.math.BigDecimal getIII_1_3A(){
    /** @todo za neke buduce implementacije
    - prema plaæi koja se odnosi na razdoblje do 31. prosinca 2002. */
    return NULA;
  }
//Zakomentiraj ovu metodu da nadjes mjesta gdje ne uzima u obzir OjWith (razlike poreza)
  private String getWhereSQL(String table, String what) {
    return getWhereSQL(table, what, dlgGetKnjig.getKNJCORG());
  }
  
  
  private String getWhereSQL(String table, String what, String knjig) {
    String neki = " "+what+" ";
    table = new java.util.StringTokenizer(table,",").nextToken();
    if (this.getRepMode() == 'A'){
//      System.out.println("godina : " + godina);
//      System.out.println("mjesec : " + mjesec);
//      System.out.println("\nNeki prije  : " + neki);
      neki = neki.concat(raPlObrRange.getInQueryIsp(godina, mjesec, godina, mjesec, table.trim(), knjig)) + " and ";
//      System.out.println("\nNeki poslje : " + neki);
    }
    return neki.concat(" (corg in ").concat(
        hr.restart.zapod.OrgStr.getOrgStr().getInQuery(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(knjig))+")");
  }

  private String odbiciWh(short[] id){
    String rigowq = raIzvjestaji.getOdbiciWhQueryIzv(id);
    if (!rigowq.equals("")){
        return " and ".concat(rigowq);
    } else {
      return "";
    }
  }

  private String primanjaWh(short[] id){
    String rigpwq = raIzvjestaji.getPrimanjaWhQueryIzv(id);
    if (!rigpwq.equals("")){
    return " and ".concat(rigpwq);
    } else {
      return "";
    }
  }

  private String getKnjigovodstvoSQL() {
    String knjst = "SELECT Logotipovi.nazivlog as naziv, Logotipovi.mjesto, Logotipovi.adresa, Logotipovi.pbr as hpbroj, Logotipovi.ziro, " +
        (raObracunPL.isOIB()?"Logotipovi.oib":"Logotipovi.matbroj") +" AS MATBROJ" +		
    		", Logotipovi.sifdjel "+
                   "FROM Logotipovi "+
                   "WHERE logotipovi.corg ='" + hr.restart.zapod.OrgStr.getKNJCORG() + "'";

    return knjst;
  }
  private String getRadniciTableNameQ() {
    String tableName = "";
    if (this.getRepMode() == 'A') tableName = " kumulradarh";
    else tableName = " kumulrad";
    return tableName;    
  }
  private String getKumulradTableName() {
    String tableName = "";
    if (this.getRepMode() == 'A') tableName = " kumulradarh";
    else tableName = " kumulrad, radnici where kumulrad.cradnik = radnici.cradnik";
    return tableName;
  }

  private String getKumulorgTableName() {
    String tableName = "";
    if (this.getRepMode() == 'A') tableName = " kumulorgarh";
    else tableName = " kumulorg";
    return tableName;
  }

  private String getOdbiciTableName() {
    String tableName = "";
    if (this.getRepMode() == 'A') tableName = " odbiciarh, kumulradarh WHERE odbiciarh.godobr = kumulradarh.godobr AND odbiciarh.mjobr = kumulradarh.mjobr "+
      "AND odbiciarh.rbrobr = kumulradarh.rbrobr AND odbiciarh.cradnik = kumulradarh.cradnik ";
    else tableName = " odbiciobr, radnici WHERE odbiciobr.cradnik = radnici.cradnik ";
    return tableName;
  }

  private String getPrimanjaTableName() {
    String tableName = "";
    if (this.getRepMode() == 'A') tableName = " primanjaarh, kumulradarh WHERE primanjaarh.godobr = kumulradarh.godobr AND primanjaarh.mjobr = kumulradarh.mjobr "+
      "AND primanjaarh.rbrobr = kumulradarh.rbrobr AND primanjaarh.cradnik = kumulradarh.cradnik ";
    else tableName = " primanjaobr, radnici WHERE primanjaobr.cradnik = radnici.cradnik ";
    return tableName;
  }

  public void firstESC() {
    if (repMode == 'A'){
      rcc.EnabDisabAll(jPanel1, true);
      jraMjesec.requestFocus();
      jraMjesec.selectAll();
    }
  }
  public boolean runFirstESC() {
    return false;
  }
  public boolean isIspis() {
    return false;
  }
  public boolean ispisNow() {
    return true;
  }

  private void jbInit() throws Exception {
    colMjesec = dm.createIntColumn("MJESEC");
    colGodina = dm.createIntColumn("GODINA");
    colInozemstvo = dm.createBigDecimalColumn("INOZEMSTVO",2);
    colIdent = dm.createStringColumn("IDENTIFIKATOR",2);
    colIdent.setDefault("1");

    jraMjesec.setDataSet(fieldSet);
    jraMjesec.setColumnName("MJESEC");

    jraGodina.setDataSet(fieldSet);
    jraGodina.setColumnName("GODINA");

    jraInozemstvo.setDataSet(fieldSet);
    jraInozemstvo.setColumnName("INOZEMSTVO");

    jraIdentifikator.setDataSet(fieldSet);
    jraIdentifikator.setColumnName("IDENTIFIKATOR");

    setRepSet();

    try {
      fieldSet.setColumns(new Column[] {colMjesec, colGodina, colInozemstvo, colIdent});
      fieldSet.open();
    }
    catch (Exception ex) {
    }

    jlMjGod.setText("Izvješ\u0107e isplate u");

    jPanel1.setLayout(xYLayout1);
    xYLayout1.setWidth(400);
    xYLayout1.setHeight(100);
    mainPanel.setLayout(borderLayout1);
    this.setJPan(mainPanel);
    jlMjesec.setText("Mjesecu");
    jlGodina.setText("Godine");
    jlInozemstvo.setText("ZZ u inozemstvu");
    jPanel1.add(jlMjGod, new XYConstraints(15,15,-1,-1));
    jPanel1.add(jraMjesec, new XYConstraints(150, 15, 35, -1));
    jPanel1.add(jraGodina,    new XYConstraints(250, 15, 60, -1));
    jPanel1.add(jlMjesec,      new XYConstraints(190, 15, -1, -1));
    jPanel1.add(jlGodina,  new XYConstraints(315, 15, -1, -1));
    jPanel1.add(jlInozemstvo, new XYConstraints(15,40,-1,-1));
    jPanel1.add(jraInozemstvo, new XYConstraints(150, 40, 100, -1));
    jPanel1.add(jlIdentifikator, new XYConstraints(15,65,-1,-1));
    jPanel1.add(jraIdentifikator, new XYConstraints(150, 65, 50, -1));
    mainPanel.add(jPanel1, BorderLayout.CENTER);
  }

  public void addRep() {
//    this.addReport("hr.restart.pl.repID", "ID", 2); /** @todo maknit, il' ne maknit pitanje je sad. Pitanje za pitat Andreja. */
    /*this.addReport("hr.restart.pl.repID2003", "Obrazac ID", 2);
    this.addReport("hr.restart.pl.repID_B", "Obrazac ID - B strana", 2);
    this.addReport("hr.restart.pl.repIDD", "Obrazac IDD", 2);*/

    
    //this.addReport("hr.restart.pl.repID2003", "Obrazac ID", 2);
    this.addJasper("hr.restart.pl.repID2005", "hr.restart.pl.repID2003", "id.jrxml", "Obrazac ID");
    try {
      Class.forName("hr.restart.pl.repIDDisk");
      this.addReport("hr.restart.pl.repIDDisk", "Datoteka ID za e-poreznu");
    } catch (Exception e) {
    }
    this.addReport("hr.restart.pl.repID2005", "hr.restart.pl.repID2003", "ID2005", "Obrazac ID do 31.12.2010.");
    this.addReport("hr.restart.pl.repID_B", "hr.restart.pl.repID_B", "ID_B", "Obrazac ID - B strana");
    this.addReport("hr.restart.pl.repIDD", "hr.restart.pl.repIDD", "IDDiot", "Obrazac IDD");
  }

  private void setRepSet(){
    col_II_1 = dm.createIntColumn("II_1");
    col_II_2 = dm.createBigDecimalColumn("II_2");
    col_II_3 = dm.createStringColumn("II_3", 50);
    col_III_1_1 = dm.createBigDecimalColumn("III_1_1");
    col_III_1_2 = dm.createBigDecimalColumn("III_1_2");
    col_III_1_3 = dm.createBigDecimalColumn("III_1_3");
    col_III_1_3A = dm.createBigDecimalColumn("III_1_3A");
    col_III_1_4 = dm.createBigDecimalColumn("III_1_4");
    col_III_2 = dm.createBigDecimalColumn("III_2");
    col_III_3_1 = dm.createBigDecimalColumn("III_3_1");
    col_III_3_2 = dm.createBigDecimalColumn("III_3_2");
    col_III_3_3 = dm.createBigDecimalColumn("III_3_3");
    col_III_4 = dm.createBigDecimalColumn("III_4");
    col_III_5 = dm.createBigDecimalColumn("III_5");
    col_III_6 = dm.createBigDecimalColumn("III_6");
    col_III_7_1 = dm.createBigDecimalColumn("III_7_1");
    col_III_7_2 = dm.createBigDecimalColumn("III_7_2");
    col_V_3_6 = dm.createBigDecimalColumn("V_3_6");
    col_III_4_4_2_2005 = dm.createBigDecimalColumn("III05_4_4_2");
    /** @todo ovo ispod je samo zbog kompatibilnosti sa starim ispisom!! */
    col_rkp010 = dm.createBigDecimalColumn("RKP010");
    col_rkp020 = dm.createBigDecimalColumn("RKP020");
    col_rkp030 = dm.createBigDecimalColumn("RKP030");
    col_rkp040 = dm.createBigDecimalColumn("RKP040");
    col_rkp050 = dm.createBigDecimalColumn("RKP050");

    repSet.setColumns(new Column[] {col_II_1, col_II_2, col_II_3, col_III_1_1, col_III_1_2, col_III_1_3, col_III_1_3A, col_III_1_4,
                                    col_III_2, col_III_3_1, col_III_3_2, col_III_3_3 ,col_III_4,
                                    col_III_5, col_III_6, col_III_7_1, col_III_7_2,
                                    col_V_3_6,
                                    col_III_4_4_2_2005,
                                    col_rkp010, col_rkp020, col_rkp030, col_rkp040, col_rkp050});
    repSet.open();
  }

  public char getRepMode() {
    return repMode;
  }

  public int getGodina(){
    return godina;
  }

  public int getMjesec(){
    return mjesec;
  }

  public java.math.BigDecimal getZZuInozemstvu(){
    return zzuin;
  }
  
  public String getIdentifikator(){
    return ident;
  }

  public DataSet getRepSet(){
    return repSet;
  }

  public DataSet getRepSetStrB(){
    return repSetStrB;
  }

  public String getKnjNaziv(){
    return knjigovodstvo.getString("NAZIV");
  }

  public String getKnjAdresa(){
//    System.out.println("Adresa : " + knjigovodstvo.getString("ADRESA"));
    return knjigovodstvo.getString("ADRESA");
  }

  public int getKnjHpBroj(){
//    System.out.println("Hp broj : " + knjigovodstvo.getInt("HPBROJ"));
    return knjigovodstvo.getInt("HPBROJ");
  }

  public String getKnjMjesto(){
//    System.out.println("Mjesto : " + knjigovodstvo.getString("MJESTO"));
    return knjigovodstvo.getString("MJESTO");
  }

  public String getKnjZiro(){
    return knjigovodstvo.getString("ZIRO");
  }

  public String getKnjMatbroj(){
    return knjigovodstvo.getString("MATBROJ");
  }

  public String getKnjSifdjel(){
    return knjigovodstvo.getString("SIFDJEL");
  }

  public String getMjesecIzEkrana(){
    String mj;
    if (fieldSet.getInt("MJESEC") <10) mj = "0" + fieldSet.getInt("MJESEC");
    else mj = "" + fieldSet.getInt("MJESEC");
    return mj;
  }

  public String getGodinaIzEkrana(){
    String god = "" + fieldSet.getInt("GODINA");
    return god;
  }
  /**
   * Hoce li prikazati vrijednost na IDD u zadanoj koloni ovisno o sistemskom parametru vroIDDst+CVRO
   * @param colName ime kolone
   * @param stupac broj stupca 2-8
   * @return trazenu vrijednost ili null ako u parametru pise da za taj vro ide u drugu kolonu
   */
  public Object wrapVRO(String colName, int stupac) {
    try {
      int parStupac = new Integer(frmParam.getParam("pl","vroIDDst"+wrapCVRO.trim(),"3","U koji stupac IDD-a ide vro "+wrapCVRO+" (2-8)")).intValue();
      if (stupac == parStupac) return raVariant.getDataSetValue(getRepSet(),colName);
    } catch (Exception e) {
      //e.printStackTrace();
      System.out.println(e);
    }
    return null;
  }
  /**
   * Samo kasta Object wrapVRO u int (za II_1)
   * @param colName
   * @param stupac
   * @return
   */
  public int wrapVROint(String colName, int stupac) {
    try {
      Object intg = wrapVRO(colName, stupac);
      if (intg!=null) return ((Integer)intg).intValue();
    } catch (Exception e) {
      System.out.println(e);
    }
    return 0;
  }
  /**
   * Samo kasta Object wrapVRO u BigDecimal
   * @param colName
   * @param stupac
   * @return
   */
  public BigDecimal wrapVRObd(String colName, int stupac) {
    try {
      Object bd = wrapVRO(colName, stupac);
      if (bd!=null) return (BigDecimal)bd;
    } catch (Exception e) {
      System.out.println(e);
    }
    return null;
  }

  public BigDecimal getOsnovicaZaDoprinoseIDD(int stupac) {
    BigDecimal dop = wrapVRObd("III_2", stupac);
    if (dop==null || dop.signum()==0) return null;
    return wrapVRObd("II_2", stupac);
  }

  public BigDecimal getZZuInoIDD(int stupac) {
    try {
      int parStupac = new Integer(frmParam.getParam("pl","vroIDDst"+wrapCVRO.trim(),"3","U koji stupac IDD-a ide vro "+wrapCVRO+" (2-8)")).intValue();
      if (stupac == parStupac) return getZZuInozemstvu();
    } catch (Exception e) {
      //e.printStackTrace();
      System.out.println(e);
    }
    return null;
  }
}
