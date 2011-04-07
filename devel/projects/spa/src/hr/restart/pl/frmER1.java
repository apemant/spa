/****license*****************************************************************
**   file: frmER1.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadWriteRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmER1 extends raUpitLite {

  dM dm = dM.getDataModule();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  hr.restart.zapod.OrgStr orgs = hr.restart.zapod.OrgStr.getOrgStr();
  Util ut = Util.getUtil();

  short[] s = new short[4];

  JPanel mainPanel = new JPanel();
  private JPanel jpPanel1 = new JPanel();

  StorageDataSet fieldSet = new StorageDataSet();
  QueryDataSet repSet = new QueryDataSet();
  QueryDataSet knjigovodstvo;
  QueryDataSet orgpl;
  QueryDataSet godPrim;

  Column colRadnik = new Column();
  Column colDatumOd = new Column();
  Column colDatumDo = new Column();
  Column colCorg = new Column();


  JraTextField jraDatOd = new JraTextField();
  JraTextField jraDatDo = new JraTextField();
  JlrNavField jlrCorg = new JlrNavField();
  JlrNavField jlrNazorg = new JlrNavField();
  JlrNavField jlrCradnik = new JlrNavField() {
    public void after_lookUp() {
      jraImePrezime.setText(" " + jlrIme.getText()+" "+jlrPrezime.getText());
    }
  };
  JraTextField jlrPrezime = new JraTextField();
  JraTextField jlrIme = new JraTextField();
  JTextField jraImePrezime = new JTextField();

  JraButton jbSelCorg = new JraButton();
  JraButton jbSelCradnik = new JraButton();

  JLabel jlCorg = new JLabel();
  JLabel jlRadnik = new JLabel();
  JLabel jlPeriod = new JLabel();

  private BorderLayout borderLayout1 = new BorderLayout();
  private XYLayout xYLayout1 = new XYLayout();

  static frmER1 fer1;

  public frmER1() {
    try {
      jbinit();
      fer1 = this;
    }
    catch (Exception ex) {}
  }

  public static frmER1 getInstance(){
    return fer1;
  }

  public void componentShow() {
    fieldSet.setString("CORG", OrgStr.getKNJCORG());
    jlrCorg.forceFocLost();
    jlrCradnik.setText("");
    jlrCradnik.emptyTextFields();
    rcc.setLabelLaF(jraImePrezime, false);
    fieldSet.setTimestamp("DATOD", ut.getFirstDayOfYear());
    fieldSet.setTimestamp("DATDO", ut.getLastDayOfYear());
    jlrCorg.requestFocus();
    jlrCorg.selectAll();
  }

  public void okPress() {
    int god1 = Integer.parseInt(ut.getYear(fieldSet.getTimestamp("DATOD")));
    int god2 = Integer.parseInt(ut.getYear(fieldSet.getTimestamp("DATDO")));
    int mj1 = Integer.parseInt(ut.getMonth(fieldSet.getTimestamp("DATOD")));
    int mj2 = Integer.parseInt(ut.getMonth(fieldSet.getTimestamp("DATDO")));
System.out.println(god1+"/"+mj1+" - "+god2+"/"+mj2);
    String kumulIn = raPlObrRange.getInQueryIsp(god1,mj1,god2,mj2,"kumulradarh");

    knjigovodstvo = ut.getNewQueryDataSet(getKnjigovodstvoSQL());
    orgpl = ut.getNewQueryDataSet(getOrgplSQL());
    String repqry = getRepQdsString(kumulIn);
    System.out.println(repqry);
    repSet = ut.getNewQueryDataSet(repqry);

    godPrim = ut.getNewQueryDataSet(getGodPrimSQL());

    Column colS1 = new Column();
    Column colS2 = new Column();
    Column colS3 = new Column();
    Column colS4 = new Column();
    Column colS5 = new Column();
    Column colAlikvotni = new Column();

    colS1 = dm.createBigDecimalColumn("SATIPUNORV");
    colS2 = dm.createBigDecimalColumn("SATIDUZE");
    colS3 = dm.createBigDecimalColumn("SATIKRACE");
    colS4 = dm.createBigDecimalColumn("SATIBOL");
    colS5 = dm.createBigDecimalColumn("FONDSATI");
    colAlikvotni = dm.createBigDecimalColumn("ALIKVOTNI");

    repSet.addColumn(colS1);
    repSet.addColumn(colS2);
    repSet.addColumn(colS3);
    repSet.addColumn(colS4);
    repSet.addColumn(colS5);
    repSet.addColumn(colAlikvotni);

    repSet.getColumn("GODOBRDOH").setRowId(true);
    repSet.getColumn("MJOBRDOH").setRowId(true);

    makeRepSet();

    String kv = "SELECT kumulorgarh.godobr,kumulorgarh.mjobr FROM kumulorgarh where kumulorgarh.datumispl between "+
                "'" + fieldSet.getTimestamp("DATOD") + "' and '" + fieldSet.getTimestamp("DATDO") + "' group by kumulorgarh.godobr,kumulorgarh.mjobr";

    QueryDataSet qdse = ut.getNewQueryDataSet(kv);
    qdse.first();
    s[0] = qdse.getShort("GODOBR");
    s[1] = qdse.getShort("MJOBR");
    qdse.last();
    s[2] = qdse.getShort("GODOBR");
    s[3] = qdse.getShort("MJOBR");

    String s16 = raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ER1_6);
    String s17 = raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ER1_7);
    String s18 = raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ER1_9);

    rcc.EnabDisabAll(mainPanel, false);

    setSume();
  }

  public void firstESC() {
    rcc.EnabDisabAll(mainPanel, true);
    rcc.setLabelLaF(jraImePrezime, false);
    jlrCorg.requestFocus();
    jlrCorg.selectAll();
  }

  public boolean runFirstESC() {
    return false;
//    throw new java.lang.UnsupportedOperationException("Method runFirstESC() not yet implemented.");
  }

  public boolean Validacija(){

    if (vl.isEmpty(jlrCorg) || vl.isEmpty(jlrCradnik) ) return false;
    if (getTimestamp(jraDatOd).after(getTimestamp(jraDatDo))) {
      javax.swing.JOptionPane.showMessageDialog(
          this,
          "Datumski period nije ispravan",
          "GREŠKA",
          javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }

//  //    sysoutTEST ST = new sysoutTEST(false);
//  //    ST.prn(fieldSet);
//  //    ST.prn(repSet);
//
//    int god1 = Integer.parseInt(ut.getYear(fieldSet.getTimestamp("DATOD")));
//    int god2 = Integer.parseInt(ut.getYear(fieldSet.getTimestamp("DATDO")));
//    int mj1 = Integer.parseInt(ut.getMonth(fieldSet.getTimestamp("DATOD")));
//    int mj2 = Integer.parseInt(ut.getMonth(fieldSet.getTimestamp("DATDO")));
//
//  //    System.out.println("(god1,mj1,god2,mj2,\"kumulradarh\") = ("+""+god1+","+mj1+","+god2+","+mj2+",\"kumulradarh\")");
//
//    String kumulIn = raPlObrRange.getInQueryIsp(god1,mj1,god2,mj2,"kumulradarh");
//
//  //    String kumulIn = "";
//
//    knjigovodstvo = ut.getNewQueryDataSet(getKnjigovodstvoSQL());
//    orgpl = ut.getNewQueryDataSet(getOrgplSQL());
//  //    System.out.println("TATARATIRAAAAA getRepQdsString(kumulIn) : " + getRepQdsString(kumulIn));
//    repSet = ut.getNewQueryDataSet(getRepQdsString(kumulIn));
//
//  //    sysoutTEST syst = new sysoutTEST(false);
//  //    syst.prn(repSet);
//
//    godPrim = ut.getNewQueryDataSet(getGodPrimSQL());
//
//    Column colS1 = new Column();
//    Column colS2 = new Column();
//    Column colS3 = new Column();
//    Column colS4 = new Column();
//    Column colS5 = new Column();
//    Column colAlikvotni = new Column();
//
//    colS1 = dm.createBigDecimalColumn("SATIPUNORV");
//    colS2 = dm.createBigDecimalColumn("SATIDUZE");
//    colS3 = dm.createBigDecimalColumn("SATIKRACE");
//    colS4 = dm.createBigDecimalColumn("SATIBOL");
//    colS5 = dm.createBigDecimalColumn("FONDSATI");
//    colAlikvotni = dm.createBigDecimalColumn("ALIKVOTNI");
//
//    repSet.addColumn(colS1);
//    repSet.addColumn(colS2);
//    repSet.addColumn(colS3);
//    repSet.addColumn(colS4);
//    repSet.addColumn(colS5);
//    repSet.addColumn(colAlikvotni);
//
//    repSet.getColumn("GODOBRDOH").setRowId(true);
//    repSet.getColumn("MJOBRDOH").setRowId(true);
//
//    makeRepSet();
//
//    String kv = "SELECT kumulorgarh.godobr,kumulorgarh.mjobr FROM kumulorgarh where kumulorgarh.datumispl between "+
//                "'" + fieldSet.getTimestamp("DATOD") + "' and '" + fieldSet.getTimestamp("DATDO") + "' group by kumulorgarh.godobr,kumulorgarh.mjobr";
//
//    QueryDataSet qdse = ut.getNewQueryDataSet(kv);
//    qdse.first();
//    s[0] = qdse.getShort("GODOBR");
//    s[1] = qdse.getShort("MJOBR");
//    qdse.last();
//    s[2] = qdse.getShort("GODOBR");
//    s[3] = qdse.getShort("MJOBR");
//
//  //    s = raPlObrRange.getMinAndMaxObrada(); //perhaps malo nelogicno...
//
//    String s16 = raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ER1_6);
//    String s17 = raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ER1_7);
//    String s18 = raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ER1_9);
//
//    rcc.EnabDisabAll(mainPanel, false);
    return true;
  }

  /**
   * @param jraDatOd2
   * @return
   */
  private Timestamp getTimestamp(JraTextField jraDat) {
    try {
      return jraDat.getDataSet().getTimestamp(jraDat.getColumnName());
    } catch (Exception e) {
      return vl.getToday();
    }
  }

  private void makeRepSet() {
    repSet.first();

    do{
      java.math.BigDecimal fondSati = raIzvjestaji.getFond(repSet.getShort("GODOBRDOH"),repSet.getShort("MJOBRDOH")).satiUk;
      repSet.setBigDecimal("FONDSATI", fondSati);

      repSet.setBigDecimal("ALIKVOTNI", new java.math.BigDecimal(0)); /** @todo ALIKVOTNI DIO PLACHE */
      fillSpecSati(raIzvjestaji.ER1_6, "SATIDUZE");
      fillSpecSati(raIzvjestaji.ER1_7, "SATIKRACE");
      fillSpecSati(raIzvjestaji.ER1_9, "SATIBOL");
//      String er6, er7, er8;
//      if ((er6 = raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ER1_6)).equals("")){
//        repSet.setBigDecimal("SATIDUZE", new java.math.BigDecimal(0));
//      } else {
//        System.out.println("ER1_6 ="+er6);
//        
//      }
//
//      if ((er7=raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ER1_7)).equals("")){
//        repSet.setBigDecimal("SATIKRACE", new java.math.BigDecimal(0));
//      } else {
//        System.out.println("ER1_7 ="+er7);
//      }
//
//      if ((er8=raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ER1_9)).equals("")){
//        repSet.setBigDecimal("SATIBOL", new java.math.BigDecimal(0));
//      } else {
//        System.out.println("ER1_8 ="+er8);
//      }

      repSet.setBigDecimal("SATIPUNORV", repSet.getBigDecimal("SATI").subtract(repSet.getBigDecimal("SATIDUZE")).subtract(repSet.getBigDecimal("SATIKRACE")));
    } while (repSet.next());
  }
  private void fillSpecSati(short[] iz, String cn) {
    String izq;
    if ((izq=raIzvjestaji.getPrimanjaWhQueryIzv(iz)).equals("")){
      repSet.setBigDecimal(cn, new java.math.BigDecimal(0));
    } else {
      System.out.println(izq);
      repSet.setBigDecimal(cn, Aus.q(getPrimSql(repSet.getShort("mjobrdoh"), repSet.getShort("godobrdoh"), izq)).getBigDecimal("sumsati"));
      repSet.post();
    }
  }
  private String getKnjigovodstvoSQL() {
    String qstr = "SELECT Orgstruktura.naziv, Orgstruktura.mjesto, Orgstruktura.adresa, Orgstruktura.hpbroj, Logotipovi.matbroj "+
                  "FROM Orgstruktura, Logotipovi "+
                  "WHERE orgstruktura.corg = logotipovi.corg and orgstruktura.corg ='" + orgs.getKNJCORG() + "'"; // fieldSet.getString("CORG") + "'";
    return qstr;
  }

  private String getOrgplSQL(){
    String qstr = "select orgpl.podruredzo from orgpl where orgpl.corg='" + orgs.getKNJCORG() + "'";
    return qstr;
  }

  private String getRepQdsString(String kumul) {
    /*String qstr = "select kumulorgarh.datumispl, kumulradarh.sati, kumulradarh.bruto, kumulradarh.naknade,"+
                  " kumulradarh.mjobr as mjobrdoh, kumulradarh.godobr as godobrdoh, kumulradarh.netopk,"+
                  " radnici.ime, radnici.prezime, radnici.cradnik, radnicipl.jmbg,"+
                  " radnicipl.brobveze, radnicipl.brosigzo, radnicipl.clanomf"+

                  " from Kumulorgarh, Kumulradarh, Radnici, Radnicipl"+

                  " WHERE kumulorgarh.godobr = kumulradarh.godobr"+
                  " AND kumulorgarh.mjobr = kumulradarh.mjobr"+
                  " AND kumulorgarh.rbrobr = kumulradarh.rbrobr"+
                  " AND kumulradarh.cvro = kumulorgarh.cvro"+
                  " AND kumulradarh.corg = kumulorgarh.corg"+
                  " AND kumulradarh.cradnik = radnici.cradnik"+
                  " AND radnici.corg = kumulorgarh.corg"+
                  " AND kumulradarh.cradnik = radnicipl.cradnik"+
                  " AND radnici.cradnik = radnicipl.cradnik"+
                  " AND radnicipl.cvro = kumulorgarh.cvro"+
                  " AND radnicipl.corg = kumulorgarh.corg"+
                  " and kumulorgarh.datumispl between '"+ fieldSet.getTimestamp("DATOD") + "' and '" + ut.getLastSecondOfDay(fieldSet.getTimestamp("DATDO")) + "'" +
                  " and kumulradarh.cradnik = '"+ fieldSet.getInt("CRADNIK") + "'" +
                  " and kumulradarh.corg in " + orgs.getInQuery(orgs.getOrgstrAndKnjig(fieldSet.getString("CORG")))+
                  " and " + kumul +
//                  " group by godobrdoh, mjobrdoh " +
                  " order by radnici.prezime " + vl.getCollateSQL() + " , kumulradarh.mjobr "; /// PROXIMITY WARNING
    System.out.println("repQdsSQL = " + qstr);*/

    String qstr = "select " +
                  "max(kumulorgarh.datumispl) as datumispl, "+
                  "cast(sum(kumulradarh.sati) as numeric(17,2)) as sati, "+
                  "cast(sum(kumulradarh.bruto) as numeric(17,2)) as bruto, "+
                  "cast(sum(kumulradarh.naknade) as numeric(17,2)) as naknade, "+
                  "max(kumulradarh.mjobr) as mjobrdoh, "+
                  "max(kumulradarh.godobr) as godobrdoh, "+
                  "cast(sum(kumulradarh.netopk) as numeric(17,2)) as netopk, "+
                  "max(radnici.ime) as ime, "+
                  "max(radnici.prezime) as prezime, "+
                  "max(radnici.cradnik) as cradnik, "+
                  "max(radnicipl.oib) as jmbg, "+
                  "max(radnicipl.brobveze) as brobveze, "+
                  "max(radnicipl.brosigzo) as brosigzo, "+
                  "max(radnicipl.clanomf) as clanomf "+
                  "from  "+
                  "Kumulorgarh, Kumulradarh, Radnici, Radnicipl "+" WHERE kumulorgarh.godobr = kumulradarh.godobr"+
                  " AND kumulorgarh.mjobr = kumulradarh.mjobr"+
                  " AND kumulorgarh.rbrobr = kumulradarh.rbrobr"+
                  " AND kumulradarh.cvro = kumulorgarh.cvro"+
                  " AND kumulradarh.corg = kumulorgarh.corg"+
                  " AND kumulradarh.cradnik = radnici.cradnik"+
//                  " AND radnici.corg = kumulorgarh.corg"+
                  " AND kumulradarh.cradnik = radnicipl.cradnik"+
                  " AND radnici.cradnik = radnicipl.cradnik"+
//                  " AND radnicipl.cvro = kumulorgarh.cvro"+
//                  " AND radnicipl.corg = kumulorgarh.corg"+
                  " and kumulorgarh.datumispl between '"+ fieldSet.getTimestamp("DATOD") + "' and '" + ut.getLastSecondOfDay(fieldSet.getTimestamp("DATDO")) + "'" +
                  " and kumulradarh.cradnik = '"+ fieldSet.getInt("CRADNIK") + "'" +
//                  " and (kumulradarh.corg in " + orgs.getInQuery(orgs.getOrgstrAndKnjig(fieldSet.getString("CORG")),"kumulradarh.corg")+")"+
                  " and " + kumul +
                  " group by kumulradarh.mjobr, kumulradarh.godobr "+
                  "order by prezime " + vl.getCollateSQL() + " , kumulradarh.godobr, kumulradarh.mjobr";
    return qstr;
  }

  private String getPrimSql(short mjobr,short godobr,String izvjqry) {
    String qstr = "select sum(primanjaarh.sati) as sumsati" +
    		    " from primanjaarh " +
    		    " where primanjaarh.cradnik = '"+ fieldSet.getInt("CRADNIK") + "'" +
    		    " AND primanjaarh.mjobr = "+mjobr+
    				" AND primanjaarh.godobr = "+godobr+
    				" AND "+izvjqry;
    return qstr;
    
  }
  private String getGodPrimSQL(){
    String qstr = "select kumulorgarh.datumispl,vrsteprim.naziv,primanjaarh.bruto,primanjaarh.irazod,primanjaarh.irazdo"+
                  " from Vrsteprim, Primanjaarh, Kumulorgarh, radnicipl"+
                  " WHERE vrsteprim.cvrp = primanjaarh.cvrp"+
                  " AND primanjaarh.godobr = kumulorgarh.godobr"+
                  " AND primanjaarh.mjobr = kumulorgarh.mjobr"+
                  " AND primanjaarh.rbrobr = kumulorgarh.rbrobr"+
                  " AND primanjaarh.corg = kumulorgarh.corg"+
                  " AND radnicipl.cradnik = primanjaarh.cradnik"+
                  " AND radnicipl.cvro = kumulorgarh.cvro"+
                  " AND kumulorgarh.datumispl between '"+ fieldSet.getTimestamp("DATOD") +
                  "' and '" + ut.getLastSecondOfDay(fieldSet.getTimestamp("DATDO")) + "'" +
                  " AND primanjaarh.cradnik = '" + fieldSet.getInt("CRADNIK") + "'"+
                  " AND vrsteprim.regres = 'D'"+
                  " order by kumulorgarh.datumispl";

        /*"select kumulorgarh.datumispl,vrsteprim.naziv,primanjaarh.bruto,primanjaarh.irazod,primanjaarh.irazdo"+
        "from Vrsteprim, Primanjaarh, Kumulorgarh"+
        "WHERE vrsteprim.cvrp = primanjaarh.cvrp"+
        "AND primanjaarh.godobr = kumulorgarh.godobr"+
        "AND primanjaarh.mjobr = kumulorgarh.mjobr"+
        "AND primanjaarh.rbrobr = kumulorgarh.rbrobr"+
        "AND primanjaarh.corg = kumulorgarh.corg"+
        "AND vrsteprim.regres = 'D'"+
        "AND primanjaarh.cradnik = '" + fieldSet.getInt("CRADNIK") + "'"+
        "order by kumulorgarh.datumispl";*/

//    System.out.println("getGodPrimSQL = " + qstr);

    return qstr;
  }

  private void jbinit() throws Exception {
    this.addReport("hr.restart.pl.repER1", "ER1", 2);

    jbSelCradnik.setText("...");
    jbSelCorg.setText("...");
    jlCorg.setText("Org. jedinica");
    jlRadnik.setText("Radnik");
    jlPeriod.setText("Za period");

    colRadnik = dm.createIntColumn("CRADNIK", "Oznaka radnika");
    colDatumOd = dm.createTimestampColumn("DATOD");
    colDatumDo = dm.createTimestampColumn("DATDO");
    colCorg = dm.createStringColumn("CORG","Oznaka organizacijske jedinice",0);

    try {
      fieldSet.setColumns(new Column[] {colCorg, colRadnik, colDatumOd, colDatumDo});
      fieldSet.open();
    }
    catch (Exception ex) {
    }

    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(fieldSet);
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazorg});
    jlrCorg.setVisCols(new int[] {0, 1, 2});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbSelCorg);

    jlrNazorg.setSearchMode(1);
    jlrNazorg.setNavProperties(jlrCorg);
    jlrNazorg.setColumnName("NAZIV");

    jlrCradnik.setHorizontalAlignment(SwingConstants.LEFT);
    jlrCradnik.setColumnName("CRADNIK");
    jlrCradnik.setDataSet(fieldSet);
    jlrCradnik.setColNames(new String[] {"IME", "PREZIME"});
    jlrCradnik.setTextFields(new javax.swing.text.JTextComponent[] {jlrIme, jlrPrezime});
    jlrCradnik.setVisCols(new int[] {0, 1, 2});
    jlrCradnik.setSearchMode(0);
    jlrCradnik.setRaDataSet(dm.getAllRadnici());
    jlrCradnik.setNavButton(jbSelCradnik);

    jlrIme.setColumnName("IME");
    jlrPrezime.setColumnName("PREZIME");

    jraDatOd.setDataSet(fieldSet);
    jraDatOd.setColumnName("DATOD");

    jraDatDo.setDataSet(fieldSet);
    jraDatDo.setColumnName("DATDO");

    mainPanel.setLayout(borderLayout1);
    this.setJPan(mainPanel);
    jpPanel1.setLayout(xYLayout1);
    xYLayout1.setHeight(125);
    xYLayout1.setWidth(600);
    jpPanel1.add(jlCorg, new XYConstraints(15,15,-1,-1));
    jpPanel1.add(jlrCorg, new XYConstraints(150, 15, 100, -1));
    jpPanel1.add(jlrNazorg, new XYConstraints(255, 15, 300, -1));
    jpPanel1.add(jbSelCorg, new XYConstraints(560, 15, 21, 21));
    jpPanel1.add(jlRadnik, new XYConstraints(15,40,-1,-1));
    jpPanel1.add(jlrCradnik, new XYConstraints(150,40,100,-1));
    jpPanel1.add(jraImePrezime, new XYConstraints(255,40,300,-1));
    jpPanel1.add(jbSelCradnik, new XYConstraints(560, 40, 21, 21));
    jpPanel1.add(jlPeriod, new XYConstraints(15,90,-1,-1));
    jpPanel1.add(jraDatOd, new XYConstraints(150, 90, 100, -1));
    jpPanel1.add(jraDatDo, new XYConstraints(255, 90, 100, -1));
    mainPanel.add(jpPanel1, BorderLayout.CENTER);
  }

  public boolean isIspis() {
    return false;
  }
  public boolean ispisNow() {
    return true;
  }
  public DataSet getRepSet(){
    return repSet;
  }
  public String getKnjNaziv(){
    return knjigovodstvo.getString("NAZIV");
  }
  public String getKnjAdresa(){
    return knjigovodstvo.getString("ADRESA");
  }
  public String getKnjHpBroj(){
    return knjigovodstvo.getString("HPBROJ");
  }
  public String getKnjMjesto(){
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
  public Timestamp getDatOd(){
    return fieldSet.getTimestamp("DATOD");
  }
  public Timestamp getDatDo(){
    return fieldSet.getTimestamp("DATDO");
  }
  public String getZaMjesece(){
    String zamjs = s[1]+"/"+s[0]+" - "+s[3]+"/"+s[2];
    return zamjs;
  }
  public String getPodrUred(){
    return orgpl.getString("PODRUREDZO");
  }

  java.math.BigDecimal NULA = new java.math.BigDecimal(0);
  java.math.BigDecimal sumPlaca;
  java.math.BigDecimal sumNetopk;
  java.math.BigDecimal sumSatiMjesecno;
  java.math.BigDecimal sumAlikvotni;
  java.math.BigDecimal sumFondSati;
  java.math.BigDecimal sumSatiPunoRV;
  java.math.BigDecimal sumSatiDuze;
  java.math.BigDecimal sumSatiKrace;
  java.math.BigDecimal sumSatiBol;

  void setSume(){
    {
    sumPlaca = NULA;
    sumNetopk = NULA;
    sumSatiMjesecno = NULA;
    sumAlikvotni = NULA;
    sumFondSati = NULA;
    sumSatiPunoRV = NULA;
    sumSatiDuze = NULA;
    sumSatiKrace = NULA;
    sumSatiBol = NULA;
//    System.out.println("NULIRANO!!!");
    }
    repSet.first();

    do {
      sumSatiMjesecno = sumSatiMjesecno.add(repSet.getBigDecimal("SATI"));
      sumPlaca = sumPlaca.add(repSet.getBigDecimal("BRUTO").add(repSet.getBigDecimal("NAKNADE")));
      sumNetopk = sumNetopk.add(repSet.getBigDecimal("NETOPK"));
      sumSatiDuze = sumSatiDuze.add(repSet.getBigDecimal("SATIDUZE"));
      sumSatiKrace = sumSatiKrace.add(repSet.getBigDecimal("SATIKRACE"));
      sumSatiBol = sumSatiBol.add(repSet.getBigDecimal("SATIBOL"));
      sumFondSati = sumFondSati.add(repSet.getBigDecimal("FONDSATI"));
      sumAlikvotni = sumAlikvotni.add(repSet.getBigDecimal("ALIKVOTNI"));
      sumSatiPunoRV = sumSatiPunoRV.add(repSet.getBigDecimal("SATIPUNORV"));
    } while (repSet.next());
  }

  public double getSumSatiMj(){
    return sumSatiMjesecno.doubleValue();
  }
  public double getSumPlaca(){
    return sumPlaca.doubleValue();
  }
  public double getSumNetopk(){
    return sumNetopk.doubleValue();
  }
  public double getSumSatiDuze(){
    return sumSatiDuze.doubleValue();
  }
  public double getSumSatiKrace(){
    return sumSatiKrace.doubleValue();
  }
  public double getSumSatiBol(){
    return sumSatiBol.doubleValue();
  }
  public double getSumFondSati(){
    return sumFondSati.doubleValue();
  }
  public double getSumAlikvotni(){
    return sumAlikvotni.doubleValue();
  }
  public double getSumSatiPunoRV(){
    return sumSatiPunoRV.doubleValue();
  }
}