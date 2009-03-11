/****license*****************************************************************
**   file: frmStanjeKonto.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpit;

import java.awt.Dimension;
import java.awt.Font;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmStanjeKonto extends raUpit {
  hr.restart.robno._Main main;
  hr.restart.robno.Util utilRobno = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util util = hr.restart.util.Util.getUtil();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  StorageDataSet stds = new StorageDataSet();
  StorageDataSet ojs;

  QueryDataSet outSet = new QueryDataSet();
  QueryDataSet repSet = new QueryDataSet();

  Column colMj = new Column();
  Column colGod = new Column();
  Column colFirst = new Column();
  Column colRbR = new Column();
  Column colSaldo = new Column();
  Column colFirstZbirno = new Column();
  java.text.DateFormatSymbols dfs = new java.text.DateFormatSymbols();
  static frmStanjeKonto frmStKon;

  BigDecimal sumIP = Aus.zero0;
  BigDecimal sumID = Aus.zero0;
  BigDecimal sumSaldo = Aus.zero0;
  BigDecimal psIP = Aus.zero0;
  BigDecimal psID = Aus.zero0;
  BigDecimal psSaldo = Aus.zero0;
  private static String corgDifolt;
  raPanKonto kontoPanel = new raPanKonto();
  JPanel jpDetail;
  XYLayout layDetail = new XYLayout();
  JLabel jlStd = new JLabel();
  JraTextField jtGodina = new JraTextField();

  JraTextField jtMjesec = new JraTextField() {
    public boolean maskCheck() {
      if (super.maskCheck()) {
        if (!checkMonth()) {
          this.setErrText("Nepostoje\u0107i mjesec");
          this_ExceptionHandling(new java.lang.Exception());
          return false;
        } else {
          return true;
        }
      } else {
        return false;
      }
    }
  };

  raComboBox rcbPrivremenost = new raComboBox();
  raComboBox rcbZbirno = new raComboBox();
  raComboBox rcbPripOrgJed = new raComboBox();

  JLabel jlCrtica = new JLabel();

  public frmStanjeKonto() {
    try {
      jbInit();
      frmStKon = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static frmStanjeKonto getFrmStanjeKonto() {
    if (frmStKon == null) {
      frmStKon = new frmStanjeKonto();
    }
    return frmStKon;
  }

  public void componentShow() {
    initialValues();
  }
  private boolean checkMonth() {
    if (jtMjesec.getText().equals("")) {
      return true;
    }
    try {
      int i = Integer.parseInt(jtMjesec.getText());
      return (i < 13);
    } catch (Exception ex) {
      return false;
    }
  }

  public void firstESC() {
    if (OKisPressed) {
      showDefaultValuesAlter();
      OKisPressed = false;
    } else {
      showDefaultValues();
    }
  }

  public void cancelPress() {
    if (!this.getOKPanel().jBOK.isEnabled()) {
      rcc.setLabelLaF(this.getOKPanel().jBOK, true);
    }
    super.cancelPress();
  }

  boolean OKisPressed = true;

  JLabel jlOrgStrukt = new JLabel();

  public boolean runFirstESC() {
    rcc.setLabelLaF(this.getOKPanel().jBOK, true);
    if (!kontoPanel.jlrKontoBroj.getText().equals("")) {
      return true;
    }
    return false;
  }

  String sqlKontoStringPr = "";
  String sqlCorgStringPr = "";

  public boolean Validacija() {
    if (kontoPanel.jlrCorg.getText().equals("")) {
      kontoPanel.jlrCorg.requestFocus();
      JOptionPane.showConfirmDialog(null, "Unesite organizacijsku jedinicu !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (kontoPanel.jlrKontoBroj.getText().equals("")) {
      kontoPanel.jlrKontoBroj.requestFocus();
      JOptionPane.showConfirmDialog(null, "Unesite broj konta !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (jtMjesec.getText().equals("") || jtGodina.getText().equals("")) {
      jtMjesec.requestFocus();
      JOptionPane.showConfirmDialog(null, "Unesite mjesec i/ili godinu !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public void okPress() {
    OKisPressed = true;

    QueryDataSet workingSet = util.getNewQueryDataSet(getQueryString());
    workingSet.setSort(new SortDescriptor(new String[] {"GODMJ"}));

    if (workingSet.rowCount() <= 0) setNoDataAndReturnImmediately();
    makeJob(workingSet);

    this.getJPTV().setDataSet(outSet);
    this.getJPTV().getMpTable().requestFocus();
    if (stds.getString("ZBIRNO").equals("1")) {//jcbZbirno.isSelected()) {
      rcc.setLabelLaF(this.getOKPanel().jBOK, false);
    }
  }

  private void makeJob(QueryDataSet working){
    if (stds.getString("ZBIRNO").equals("1")) zbirno(working); //jcbZbirno.isSelected()) zbirno(working);
    else {
      notZbirno(working);
      makeReportQDS();
    }
  }

  private String getQueryString(){
    String sqlUpit="";

    sqlUpit = "SELECT gkkumulativi.ID as id, gkkumulativi.IP as ip, gkkumulativi.godmj from gkkumulativi " +
              "where gkkumulativi.godmj <= '" + MakeGodMj() + "' and gkkumulativi.godmj >= '"+MakeGodPocSt()+"' " +
              inCorg("gkkumulativi") + inKontas("gkkumulativi");
    if (stds.getString("PRIVREMENO").equals("1")) { // jcbPrivremeni.isSelected()){
      sqlUpit += " UNION ALL SELECT gkstavkerad.ID as id, gkstavkerad.IP as ip, gkstavkerad.godmj from gkstavkerad " +
                 "where gkstavkerad.godmj <= '" + MakeGodMj() + "' and gkstavkerad.godmj >= '"+MakeGodPocSt()+"' " +
                 inCorg("gkstavkerad") + inKontas("gkstavkerad");
    }
    System.out.println("STANJE NA KONTU : " + sqlUpit);
    return sqlUpit;
  }

  private String inCorg(String tablica){
    return " AND "+ Aus.getCorgInCond(kontoPanel.getCorg()).toString();
//    ojs = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(kontoPanel.getCorg());
//    String sqlCorgString = "";
//    if (!kontoPanel.getIsOrgstr() || stds.getString("ORGSTR").equals("1")) { //jrbOdabrana.isSelected()) {
//      sqlCorgString = " and "+tablica+".corg = '" + kontoPanel.jlrCorg.getText() + "' ";
//    } else {
//      ojs.first();
//      sqlCorgString = " and "+tablica+".corg in (";
//      do {
//        sqlCorgString = sqlCorgString + "'" + ojs.getString("CORG").trim() + "',";
//      } while (ojs.next());
//      sqlCorgString = sqlCorgString.substring(0, sqlCorgString.trim().length()) + ") ";
//    }
//    return sqlCorgString;
  }

  private String inKontas(String tablica){
    String sqlKontoString = "";
    if (hr.restart.zapod.raKonta.isAnalitik(kontoPanel.jlrKontoBroj.getText())) {
      sqlKontoString = " and "+tablica+".brojkonta = '" + kontoPanel.jlrKontoBroj.getText()+"' ";
    } else {
      sqlKontoString = " and "+tablica+".brojkonta like '" + kontoPanel.jlrKontoBroj.getText().trim() + "%' ";
    }
    return sqlKontoString;
  }

  private void zbirno(QueryDataSet working) {
    outSet.setColumns(new Column[]{
      (Column) colFirstZbirno.clone(),
      dm.createBigDecimalColumn("ID","Duguje",2),
      dm.createBigDecimalColumn("IP","Potražuje",2),
      (Column) colSaldo.clone()
    });
    nullBigDecs();
    outSet.open();
    for (int i = 0; i < 3; i++) {
      outSet.insertRow(false);
      outSet.setString("MM", "");
      outSet.setBigDecimal("ID", sumID);
      outSet.setBigDecimal("IP", sumIP);
      outSet.setBigDecimal("SALDO", sumSaldo);
      outSet.post();
    }
    outSet.first();
    working.first();
    do {
      if (working.getString("godmj").trim().substring(4, 6).equals("00")) {
        outSet.goToRow(0);
        outSet.setString("MM", "PO\u010CETNO STANJE");
        outSet.setBigDecimal("ID", outSet.getBigDecimal("ID").add(working.getBigDecimal("ID")));
        psID = outSet.getBigDecimal("ID");
        outSet.setBigDecimal("IP", outSet.getBigDecimal("IP").add(working.getBigDecimal("IP")));
        psIP = outSet.getBigDecimal("IP");
        outSet.setBigDecimal("SALDO", outSet.getBigDecimal("SALDO").add(working.getBigDecimal("ID").subtract(working.getBigDecimal("IP"))));
        psSaldo = outSet.getBigDecimal("ID").subtract(outSet.getBigDecimal("IP"));
        continue;
      } else if (!working.getString("godmj").trim().substring(4, 6).equals("00") && working.atFirst()) {
        outSet.goToRow(0);
        outSet.setString("MM", "PO\u010CETNO STANJE");
      }
      if (!working.getString("godmj").trim().substring(4, 6).equals("00")) {
        sumIP = sumIP.add(working.getBigDecimal("IP"));
        sumID = sumID.add(working.getBigDecimal("ID"));
        sumSaldo = sumSaldo.add(working.getBigDecimal("ID").subtract(working.getBigDecimal("IP")));
      }
    } while (working.next());
    outSet.goToRow(1);
    outSet.setString("MM", "PROMET");
    outSet.setBigDecimal("ID", sumID);
    outSet.setBigDecimal("IP", sumIP);
    outSet.setBigDecimal("SALDO", sumSaldo);

    outSet.goToRow(2);

    outSet.setString("MM", "UKUPNO");
    outSet.setBigDecimal("ID", sumID.add(psID));
    outSet.setBigDecimal("IP", sumIP.add(psIP));
    outSet.setBigDecimal("SALDO", sumSaldo.add(psSaldo));
  }

  private void notZbirno(QueryDataSet working) {
//    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//    st.showInFrame(working,"Dataset Test Frame");
    outSet.setColumns(new Column[]{
      (Column) colFirst.clone(),
      dm.createBigDecimalColumn("ID","Duguje",2),
      dm.createBigDecimalColumn("IP","Potražuje",2),
      (Column) colSaldo.clone()
    });
    nullBigDecs();
    outSet.open();
    for (int i = 0; i < Integer.parseInt(jtMjesec.getText()) + 3; i++) {
      outSet.insertRow(false);
      outSet.setString("MJ", "");
      outSet.setBigDecimal("ID", sumID);
      outSet.setBigDecimal("IP", sumIP);
      outSet.setBigDecimal("SALDO", sumSaldo);
      outSet.post();
    }
    outSet.first();
    working.first();
    do {
      for (int i = 0; i < Integer.parseInt(jtMjesec.getText()) + 1; i++) {

        if (i == Integer.parseInt(working.getString("godmj").trim().substring(4, 6))) {
          if (working.getString("godmj").trim().substring(4, 6).equals("00")) {
            outSet.goToRow(0);
            outSet.setString("MJ", "PO\u010CETNO STANJE");
            outSet.setBigDecimal("ID", outSet.getBigDecimal("ID").add(working.getBigDecimal("ID")));
            psID = outSet.getBigDecimal("ID");
            outSet.setBigDecimal("IP", outSet.getBigDecimal("IP").add(working.getBigDecimal("IP")));
            psIP = outSet.getBigDecimal("IP");
            outSet.setBigDecimal("SALDO", outSet.getBigDecimal("SALDO").add(working.getBigDecimal("ID").subtract(working.getBigDecimal("IP"))));
            psSaldo = outSet.getBigDecimal("ID").subtract(outSet.getBigDecimal("IP"));
//            working.next();
            continue;
          } else if (!working.getString("godmj").trim().substring(4, 6).equals("00") && working.atFirst()) {
            outSet.goToRow(0);
            outSet.setString("MJ", "PO\u010CETNO STANJE");
          }
          if (!working.getString("godmj").trim().substring(4, 6).equals("00")) {
            outSet.goToRow(i);
            outSet.setString("MJ", getMjName(Integer.parseInt(working.getString("godmj").trim().substring(4, 6))).toUpperCase());
            outSet.setBigDecimal("ID", outSet.getBigDecimal("ID").add(working.getBigDecimal("ID")));
            sumID = sumID.add(working.getBigDecimal("ID"));
            outSet.setBigDecimal("IP", outSet.getBigDecimal("IP").add(working.getBigDecimal("IP")));
            sumIP = sumIP.add(working.getBigDecimal("IP"));
            outSet.setBigDecimal("SALDO", outSet.getBigDecimal("SALDO").add(working.getBigDecimal("ID").subtract(working.getBigDecimal("IP"))));
            sumSaldo = sumSaldo.add(working.getBigDecimal("ID").subtract(working.getBigDecimal("IP")));
//            working.next();
          }
        } else {
          outSet.goToRow(i);
          outSet.setString("MJ", getMjName(i).toUpperCase());
        }
      }
    } while (working.next());
    outSet.goToRow(outSet.rowCount() - 2);
    outSet.setString("MJ", "UKUPNI PROMET");
    outSet.setBigDecimal("ID", sumID);
    outSet.setBigDecimal("IP", sumIP);
    outSet.setBigDecimal("SALDO", sumSaldo);

    outSet.goToRow(outSet.rowCount() - 1);
    outSet.setString("MJ", "SVE UKUPNO");
    outSet.setBigDecimal("ID", sumID.add(psID));
    outSet.setBigDecimal("IP", sumIP.add(psIP));
    outSet.setBigDecimal("SALDO", sumSaldo.add(psSaldo));
//    st.showInFrame(outSet,"Dataset Test Frame"); //XDEBUG briss
  
  }

  public QueryDataSet getRepQDS() {
    return repSet;
  }

  private void jbInit() throws Exception {
    jpDetail = new JPanel();
    this.addReport("hr.restart.gk.repStanjeKonto", "Ispis izvještaja stanje na kontu", 2);
    this.setJPan(jpDetail);
    jpDetail.setLayout(layDetail);
    layDetail.setWidth(570);
    layDetail.setHeight(135);

    colMj = dm.createStringColumn("jtMjesec", "Mjesec", 2);
    colGod = dm.createStringColumn("jtGodina", "Godina", 4);
    colFirst = dm.createStringColumn("MJ", "Mjesec", 0);
    colRbR = dm.createIntColumn("RBR", "Redni broj");
    colFirstZbirno = dm.createStringColumn("MM", "Zbirno", 0);
    colSaldo = dm.createBigDecimalColumn("SALDO", "Saldo", 2);

    stds.setColumns(new Column[]{colMj, colGod, dm.createStringColumn("PRIVREMENO",1), dm.createStringColumn("ZBIRNO",1), dm.createStringColumn("ORGSTR",1)});

    jtMjesec.setHorizontalAlignment(SwingConstants.CENTER);
    jtMjesec.setColumnName("jtMjesec");
    jtMjesec.setDataSet(stds);
    jtGodina.setHorizontalAlignment(SwingConstants.CENTER);
    jtGodina.setDataSet(stds);
    jtGodina.setColumnName("jtGodina");
    jlStd.setText("Do / Org. struktura");

    rcbPrivremenost.setDataSet(stds);
    rcbPrivremenost.setRaColumn("PRIVREMENO");
    rcbPrivremenost.setRaItems(new String[][] {
      {"Knjiženi","0"},
      {"Svi","1"}
    });

    rcbZbirno.setDataSet(stds);
    rcbZbirno.setRaColumn("ZBIRNO");
    rcbZbirno.setRaItems(new String[][] {
      {"Mjeseèna","0"},
      {"Zbirna","1"}
    });

    rcbPripOrgJed.setDataSet(stds);
    rcbPripOrgJed.setRaColumn("ORGSTR");
    rcbPripOrgJed.setRaItems(new String[][] {
      {"Cijela struktura organizacijske jedinice" ,"0"},
      {"Odabrana organizacijska jedinica ","1"}
    });

    kontoPanel.jlrKontoBroj.setRaDataSet(kontoPanel.getDohvatKonta("S"));

    jlCrtica.setText("-");
    jlOrgStrukt.setText("Dokumenti / Lista");
    jpDetail.setPreferredSize(new Dimension(580, 135));

    jpDetail.add(kontoPanel, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlStd, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jtGodina, new XYConstraints(200, 70, 50, -1));
    jpDetail.add(jtMjesec, new XYConstraints(150, 70, 35, -1));
    jpDetail.add(jlOrgStrukt, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(rcbPripOrgJed, new XYConstraints(255,70,285,-1));
    jpDetail.add(rcbPrivremenost, new XYConstraints(150,95,100,-1));
    jpDetail.add(rcbZbirno, new XYConstraints(255,95,100,-1));
    jpDetail.add(jlCrtica, new XYConstraints(190, 70, -1, -1));
    corgDifolt = hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG().trim();
    this.getJPTV().addTableColorModifier();
    this.getJPTV().addTableModifier(new BoldTotalModifier());
  }

  private String getMjName(int moon) {
    String mjesec;
    try {
      mjesec = dfs.getMonths()[moon - 1];
      return mjesec;
    } catch (Exception ex) {
      return "PO\u010CETNO STANJE";
    }
  }

  private void nullBigDecs() {
    sumIP = Aus.zero0;
    sumID = Aus.zero0;
    sumSaldo = Aus.zero0;
    psIP = Aus.zero0;
    psID = Aus.zero0;
    psSaldo = Aus.zero0;
  }

  private void showDefaultValues() {
    rcc.EnabDisabAll(jpDetail, true);
    rcc.setLabelLaF(kontoPanel.jlrCorg, false);
    rcc.setLabelLaF(kontoPanel.jlrNazorg, false);
    rcc.setLabelLaF(kontoPanel.jbSelCorg, false);
    stds.setString("jtMjesec", util.getMonth(vl.getToday()));
    stds.setString("jtGodina", vl.findYear(vl.getToday()));
    defaultComboValues();
    kontoPanel.setcORG(hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG());
    kontoPanel.jlrKontoBroj.setText("");
    kontoPanel.jlrKontoBroj.emptyTextFields();
    kontoPanel.jlrKontoBroj.requestFocus();
    outSet.empty();
    outSet.close();
    this.getJPTV().setDataSet(null);
  }

  private void defaultComboValues() {
    rcbPrivremenost.setSelectedIndex(0);
    rcbZbirno.setSelectedIndex(0);
    rcbPripOrgJed.setSelectedIndex(0);
    stds.setString("PRIVREMENO","0");
    stds.setString("ZBIRNO","0");
    stds.setString("ORGSTR","0");
  }

  private void showDefaultValuesAlter() {
    rcc.EnabDisabAll(jpDetail, true);
    stds.setString("jtMjesec", util.getMonth(vl.getToday()));
    stds.setString("jtGodina", vl.findYear(vl.getToday()));
    defaultComboValues();
    rcc.setLabelLaF(kontoPanel.jlrCorg, false);
    rcc.setLabelLaF(kontoPanel.jlrNazorg, false);
    rcc.setLabelLaF(kontoPanel.jbSelCorg, false);
    kontoPanel.jlrKontoBroj.requestFocus();
    kontoPanel.jlrKontoBroj_lookup();
    outSet.empty();
    outSet.close();
    this.getJPTV().setDataSet(null);
  }

  private void initialValues() {
    stds.setString("jtMjesec", util.getMonth(vl.getToday()));
    stds.setString("jtGodina", vl.findYear(vl.getToday()));
    defaultComboValues();
    rcc.setLabelLaF(kontoPanel.jlrCorg, false);
    rcc.setLabelLaF(kontoPanel.jlrNazorg, false);
    rcc.setLabelLaF(kontoPanel.jbSelCorg, false);
    kontoPanel.jlrKontoBroj.setText("");
    kontoPanel.jlrKontoBroj.emptyTextFields();
    kontoPanel.jlrKontoBroj.requestFocus();
    kontoPanel.setcORG(hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG());
    outSet.empty();
    outSet.close();
    this.getJPTV().setDataSet(null);
  }

  private String MakeGodMj() {
    String konkatir = stds.getString("jtGodina")+stds.getString("jtMjesec"); //jtGodina.getText().trim().concat(jtMjesec.getText());
    return konkatir;
  }

  private String MakeGodPocSt() {
    return stds.getString("jtGodina")+"00";
  }

  public boolean getZbirno() {
    return stds.getString("ZBIRNO").equals("1");//jcbZbirno.isSelected();
  }

  public String getCORG() {
    return kontoPanel.getCorg();
  }

  public String getNAZORG() {
    return kontoPanel.jlrNazorg.getText();
  }

  public String getCKON() {
    return kontoPanel.getBrojKonta();
  }

  public String getNAZKON() {
    return kontoPanel.jlrKontoNaziv.getText();
  }

  public String getMJ() {
    return jtMjesec.getText();
  }

  public String getGOD() {
    return jtGodina.getText();
  }

  public BigDecimal getSUMIP() {
    return sumIP;
  }

  public BigDecimal getSUMID() {
    return sumID;
  }

  public BigDecimal getSUMSALDO() {
    return sumSaldo;
  }

  public BigDecimal getPsIP() {
    return psIP;
  }

  public BigDecimal getPsID() {
    return psID;
  }

  public BigDecimal getPsSaldo() {
    return psSaldo;
  }

  private void makeReportQDS() {
    if (repSet.isOpen()) {
      repSet.deleteAllRows();
      repSet.close();
    }
    repSet.setColumns(new Column[]{
        (Column) colRbR.clone(),
        (Column) colFirst.clone(),
        (Column) hr.restart.baza.dM.getDataModule().getGkkumulativi().getColumn("ID").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getGkkumulativi().getColumn("IP").clone(),
        (Column) colSaldo.clone()
        });
    repSet.open();
    for (int i = 0; i < Integer.parseInt(stds.getString("jtMjesec")); i++) {
      repSet.insertRow(false);
      repSet.setInt("RBR", 0);
      repSet.setString("MJ", "");
      repSet.setBigDecimal("ID", Aus.zero0);
      repSet.setBigDecimal("IP", Aus.zero0);
      repSet.setBigDecimal("SALDO", Aus.zero0);
      repSet.post();
    }
    repSet.goToRow(0);
    outSet.goToRow(1);
    for (int i = 0; i < Integer.parseInt(stds.getString("jtMjesec")); i++) {
      repSet.setInt("RBR", i + 1);
      repSet.setString("MJ", outSet.getString("MJ"));
      repSet.setBigDecimal("ID", outSet.getBigDecimal("ID"));
      repSet.setBigDecimal("IP", outSet.getBigDecimal("IP"));
      repSet.setBigDecimal("SALDO", outSet.getBigDecimal("SALDO"));
      repSet.post();
      repSet.next();
      outSet.next();
    }
    outSet.last();
  }

  public void jptv_doubleClick(){
    if (this.getJPTV().getDataSet().getBigDecimal("ID").compareTo(Aus.zero2) == 0 &&
        this.getJPTV().getDataSet().getBigDecimal("IP").compareTo(Aus.zero2) == 0){
      if (this.getJPTV().getDataSet().getString("MJ").equalsIgnoreCase(getMjName(this.getJPTV().getDataSet().getRow())) &&
          getJPTV().getDataSet().getRow() > 0) {
        JOptionPane.showConfirmDialog(null, "Nema prometa u mjesecu!", "Upozorenje", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      } else {
        JOptionPane.showConfirmDialog(null, "Nema po?etnog stanja!", "Upozorenje", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      }
      return;
    } else {
      int ps = 0;
      String nas = "";

      if (this.getJPTV().getDataSet().getRow() != TableVievCount()-1) {
        ps = 1;
        nas = "promet - " + getMjName(this.getJPTV().getDataSet().getRow()).toLowerCase();
      }
      if (this.getJPTV().getDataSet().getRow() == 0) {
        ps = 2;
        nas = "po?etno stanje";
      }
      if (this.getJPTV().getDataSet().getRow() == TableVievCount()-1){
        nas = "sve ukupno";
      } else
      if (this.getJPTV().getDataSet().getRow() == TableVievCount()-2){
        nas = "ukupno promet";
      }
      frmKarticeGK fkgk = new frmKarticeGK(false,ps); /** @todo pocetno stanje */
      String konto = kontoPanel.getBrojKonta();
      fkgk.stds.open();
      fkgk.stds.setTimestamp("pocDatum", datumP(this.getJPTV().getDataSet().getRow()));
      fkgk.stds.setTimestamp("zavDatum", datumZ(this.getJPTV().getDataSet().getRow()));
      fkgk.setSljed("K");// jrbDatKnjiz.setSelected(true);
      fkgk.pack();
      fkgk.setTitle("Kartica glavne knjige za konto "+ konto+ " "+nas);
      fkgk.kontoPanel.jlrKontoBroj.setText(konto);
      fkgk.kontoPanel.jlrKontoBroj.forceFocLost();
      fkgk.kontoPanel.jlrCorg.setText(kontoPanel.getCorg());
      fkgk.kontoPanel.jlrCorg.forceFocLost();
      fkgk.setPrivremeno(stds.getString("PRIVREMENO"));
      fkgk.setLocation(this.getWindow().getX()-25, this.getWindow().getY()-25);
      fkgk.show();
      return;
    }
  }

  private int TableVievCount(){
    return this.getJPTV().getDataSet().getRowCount();
  }

  private java.sql.Timestamp datumTmp(int mj){
    String mxsec = "";
    if (mj < 10) mxsec = "0"+mj;
    else mxsec = String.valueOf(mj);
    String middleDat = stds.getString("jtGodina") + "-" + mxsec + "-15 00:00:00.000";
    return java.sql.Timestamp.valueOf(middleDat);
  }

  private java.sql.Timestamp datumP(int m){
    if (stds.getString("ZBIRNO").equals("0")){ //!jcbZbirno.isSelected()){
      try {
        if (this.getJPTV().getDataSet().getString("MJ").equalsIgnoreCase(getMjName(this.getJPTV().getDataSet().getRow())) &&
            m <= 12 && m > 0){
          return util.getFirstDayOfMonth(datumTmp(m));
        }
      }
      catch (Exception ex) {
        System.out.println("exception!!!");
      }
      if (this.getJPTV().getDataSet().getRow() == (TableVievCount()-2) || this.getJPTV().getDataSet().getRow() == (TableVievCount()-1) || m == 0){
        return util.getFirstDayOfMonth(datumTmp(1));
      }
    } else {
      return util.getFirstDayOfMonth(datumTmp(1));
    }
    return null;
  }

  private java.sql.Timestamp datumZ(int m){
    if (stds.getString("ZBIRNO").equals("0")){ //!jcbZbirno.isSelected()){
      try {
        if (this.getJPTV().getDataSet().getString("MJ").equalsIgnoreCase(getMjName(this.getJPTV().getDataSet().getRow())) &&
            m <= 12 && m > 0){
          return util.getLastSecondOfDay(util.getLastDayOfMonth(datumTmp(m)));
        }
      }
      catch (Exception ex) {
        System.out.println("exception!!!");
      }
      if (this.getJPTV().getDataSet().getRow() == (TableVievCount()-2)){
        return util.getLastSecondOfDay(util.getLastDayOfMonth(datumTmp(Integer.parseInt(stds.getString("jtMjesec")))));
      } else if (this.getJPTV().getDataSet().getRow() == (TableVievCount()-1) || m == 0){
        return util.getLastSecondOfDay(util.getLastDayOfMonth(datumTmp(12)));
      }
    } else {
      return util.getLastSecondOfDay(util.getLastDayOfMonth(datumTmp(Integer.parseInt(stds.getString("jtMjesec")))));
    }
    return null;
  }

  class BoldTotalModifier extends raTableModifier {
    // A.K.A. BoredTotalModifier :)
    Column vcol = new Column();
    Variant v = new Variant();
    Font bf = null, of = null;
    public boolean doModify() {
      if (getTable() instanceof JraTable2) {
        JraTable2 tab = (JraTable2) getTable();
        if (tab.getDataSet().getRowCount() > 0 &&
            tab.getDataSet().hasColumn("MJ") != null) {
          tab.getDataSet().getVariant("MJ", getRow(), v);
          return (v.getString().equals("SVE UKUPNO") ||
                  v.getString().equals("UKUPNI PROMET"));
        } else if (tab.getDataSet().getRowCount() > 0 &&
            tab.getDataSet().hasColumn("MM") != null) {
          tab.getDataSet().getVariant("MM", getRow(), v);
          return v.getString().equals("UKUPNO");
        }
      }
      return false;
    }

    public void modify() {
      Font tf = renderComponent.getFont();
      if (of == null) of = tf;
      if (bf == null || !of.equals(tf)) {
        of = tf;
        bf = tf.deriveFont(Font.BOLD);
      }
      renderComponent.setFont(bf);
    }
  }
}