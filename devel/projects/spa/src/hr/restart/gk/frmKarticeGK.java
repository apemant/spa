/****license*****************************************************************
**   file: frmKarticeGK.java
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
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.swing.raTableModifier;
import hr.restart.swing.raTableRunningSum;
import hr.restart.swing.raTableUIModifier;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raUpit;
import hr.restart.util.raUpitFat;
import hr.restart.zapod.Tecajevi;

import java.awt.Dimension;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmKarticeGK extends raUpit {
  hr.restart.robno._Main main;
  hr.restart.robno.Util rutil = hr.restart.robno.Util.getUtil();
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.util.Util util = hr.restart.util.Util.getUtil();
  Valid vl = Valid.getValid();
  hr.restart.zapod.OrgStr oj;
  lookupData ld = lookupData.getlookupData();
  dM dm = dM.getDataModule();
  StorageDataSet stds = new StorageDataSet();
  StorageDataSet ojs;
  QueryDataSet outSet = new QueryDataSet();
//  Column column1 = new Column();
//  Column column2 = new Column();
  Column colSaldo = new Column();
  Column colRbR = new Column();
  java.sql.Date dateZ = null;
  java.sql.Date dateP = null;

  raComboBox rcbPripOrgJed = new raComboBox();
  raComboBox rcbPrivremenost = new raComboBox();
  raComboBox rcbSaldo = new raComboBox();
  raComboBox rcbDonos = new raComboBox();
  raComboBox rcbSljed = new raComboBox();
  raComboBox rcbZakKnj = new raComboBox();
  

//  public JraCheckBox jcbPrivremeni = new JraCheckBox();
//  JraCheckBox jcbSaldo = new JraCheckBox();

  JPanel jpDetail = new JPanel();
  XYLayout layDetail = new XYLayout();


  int piturajOd;
  public static String knjigDifolt;
  public static boolean datdok = true;
  raPanKonto kontoPanel = new raPanKonto();
  boolean solo = true;
  protected boolean dev = false;
  int pocstMetod = 0;

  JlrNavField jlrCknjig = new JlrNavField();
  JlrNavField jlrNazknjig = new JlrNavField();
  JLabel jlPeriod = new JLabel();
  JLabel jlSljed = new JLabel();
  JLabel jlKnjigovodstvo = new JLabel();
  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfZavDatum = new JraTextField();

  static frmKarticeGK fkgk;
  
  raTableColumnModifier cpm = new raTableColumnModifier("CPAR", new String[] {"CPAR", "NAZPAR"}, dm.getPartneri());

  public frmKarticeGK() {
    try {
      jbInit();
      fkgk = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean noJpView = false;

  public frmKarticeGK(boolean noJpViev) {
    noJpView = noJpViev;
    try {
      jbInit();
      fkgk = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public frmKarticeGK(String dva) {
    try {
      jbInit();
      setHeightInstance();
      fkgk = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public frmKarticeGK(boolean solo1, int metoda) {
    this.solo = solo1;
    this.pocstMetod = metoda;
//    this.sPS = saPSom;
    try {
      jbInit();
      fkgk = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static frmKarticeGK getFrmKarticeGK() {
    if (fkgk == null) {
      fkgk = new frmKarticeGK();
    }
    return fkgk;
  }

  private boolean firstTime = true;
  public void componentShow() {
    if (solo) {
      if (firstTime) initialValues();
      else {
        this.getJPTV().setDataSet(null);
        setPripadnostOJ(stds.getString("ORGSTR"));
        setPrivremeno(stds.getString("Privremeno"));
        setSaldo(stds.getString("SALDO"));
        setSljed(stds.getString("SLJED"));
        setDonos(stds.getString("DONOS"));
      }
      firstTime = false;
    } else initialValues();
  }

  public boolean Validacija(){
    if (kontoPanel.jlrKontoBroj.getText().equals("")) {
      kontoPanel.jlrKontoBroj.requestFocus();
      JOptionPane.showConfirmDialog(null, "Unesite broj konta !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      return false;
    }
//    System.out.println("kontoPanel.getBrojKonta() - " + kontoPanel.getBrojKonta());
    if (!hr.restart.zapod.raKonta.isAnalitik(kontoPanel.getBrojKonta())) {///kontoPanel.jlrKontoBroj.getText().length()<6) {
      kontoPanel.jlrKontoBroj.requestFocus();
      JOptionPane.showConfirmDialog(null, "Nije analiti\u010Dki konto !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (kontoPanel.jlrCorg.getText().equals("")) {
      kontoPanel.jlrCorg.requestFocus();
      JOptionPane.showConfirmDialog(null, "Unesite organizacijsku jedinicu !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      return false;
    }
//    if(!vl.isValidRange(jtfPocDatum,jtfZavDatum)) return false;

//sysoutTEST syst = new sysoutTEST(false);
//syst.prn(this.getJPTV().getDataSet());

      return true;
  }

  public boolean okpresssed, nemaPodatakaZaValutu;

  public void okPress() {
    okpresssed = true;
    nemaPodatakaZaValutu = false;
    dateP = new java.sql.Date(this.stds.getTimestamp("pocDatum").getTime());
    dateZ = new java.sql.Date(this.stds.getTimestamp("zavDatum").getTime());
    String newDateP;
    String newDateZ;
    newDateP = rutil.getTimestampValue(stds.getTimestamp("pocDatum"), 0);
    newDateZ = rutil.getTimestampValue(stds.getTimestamp("zavDatum"), 1);
    String kontoJe = ".brojkonta='" + kontoPanel.jlrKontoBroj.getText().trim() +"' ";
    if (!stds.getString("ODKONTA").equals("") && !stds.getString("DOKONTA").equals("")){
      kontoJe = ".brojkonta between '"+ stds.getString("ODKONTA")+"' and '"+stds.getString("DOKONTA")+"' ";
    }
    String queryString = "";
    String a = "select gkstavke.cpar, gkstavke.brojkonta, gkstavke.rbr, gkstavke.knjig, gkstavke.ip, gkstavke.id, gkstavke.cvrnal , gkstavke.datumknj, gkstavke.datdok,"+
               " gkstavke.opis, gkstavke.corg, nalozi.cnaloga, nalozi.status, gkstavke.brojizv, gkstavke.rbs" + 
               (dev ? ", gkstavke.oznval, gkstavke.tecaj, gkstavke.devid, gkstavke.devip" : "") + " from gkstavke, nalozi where gkstavke.knjig = '" +
               knjigDifolt.trim() + "' and gkstavke" + kontoJe +
               "and gkstavke.datumknj >=" + newDateP + " and gkstavke.datumknj <=" + newDateZ +
               "and gkstavke.rbr=nalozi.rbr and gkstavke.knjig=nalozi.knjig and gkstavke.god=nalozi.god and gkstavke.cvrnal=nalozi.cvrnal";
    String b = " union all select gkstavkerad.cpar, gkstavkerad.brojkonta, gkstavkerad.rbr, gkstavkerad.knjig, gkstavkerad.ip, gkstavkerad.id, gkstavkerad.cvrnal , gkstavkerad.datumknj, "+
               "gkstavkerad.datdok, gkstavkerad.opis, gkstavkerad.corg, nalozi.cnaloga, nalozi.status, gkstavkerad.brojizv, gkstavkerad.rbs" +
               (dev ? ", gkstavkerad.oznval, gkstavkerad.tecaj, gkstavkerad.devid, gkstavkerad.devip" : "") + " from gkstavkerad, nalozi where gkstavkerad.knjig='" +
               knjigDifolt.trim() + "' and gkstavkerad" + kontoJe +
               "and gkstavkerad.datumknj >=" + newDateP + " and gkstavkerad.datumknj <=" + newDateZ +
               "and gkstavkerad.rbr=nalozi.rbr and gkstavkerad.knjig=nalozi.knjig and gkstavkerad.god=nalozi.god and gkstavkerad.cvrnal=nalozi.cvrnal";
    String c = " and " + getCorgs("gkstavke") + " "; //" and gkstavke.corg='" + kontoPanel.jlrCorg.getText().trim() + "'";
    String cc = " and " + getCorgs("gkstavkerad") + " "; //" and gkstavkerad.corg='" + kontoPanel.jlrCorg.getText().trim() + "'";
    String slj = " order by ";
    if (stds.getString("SLJED").equals("D")) slj = slj+"2, 9 ";
    else slj = slj+"2, 8 ";
    slj = slj+", 7 , 15 ";
//    String d = " order by ";

//    System.out.println("solo " + solo + "POcetno StAnje - " + pocstMetod);

    if (stds.getString("PRIVREMENO").equals("0")) { //!jcbPrivremeni.isSelected()) {
      queryString = a;
//      if ( /*!kontoPanel.jlrCorg.getText().equals(knjigDifolt.trim()) && !kontoPanel.jlrCorg.getText().equals("")*/) {
        queryString = queryString.concat(c);
//      }
      if (!solo && pocstMetod == 1){
        queryString += " and gkstavke.cvrnal != '00'";
      } else if (!solo && pocstMetod == 2){
        queryString += " and gkstavke.cvrnal = '00'";
      }
    } else {
      queryString = a.concat(b);
//      if (!kontoPanel.jlrCorg.getText().equals(knjigDifolt.trim()) && !kontoPanel.jlrCorg.getText().equals("")) {
        queryString = a.concat(c).concat(b).concat(cc);
//      }
      if (!solo && pocstMetod == 1){
        queryString += " and gkstavkerad.cvrnal != '00'";
      } else if (!solo && pocstMetod == 2){
        queryString += " and gkstavkerad.cvrnal = '00'";
      }
    }

    queryString += slj;

    /** @todo razvoj donosa.... */
    String uvjetA = "";
    String uvjetB = "";
    if (!kontoPanel.jlrCorg.getText().equals(knjigDifolt.trim()) && !kontoPanel.jlrCorg.getText().equals("")) {
      uvjetA = c;
      uvjetB = cc;
    }
    QueryDataSet donosSet = null;

    if (!dev && stds.getString("DONOS").equals("D")){
      String donosQueryString = "";
      String da = "select gkstavke.brojkonta as brojkonta, sum(gkstavke.ip) as ip, sum(gkstavke.id) as id "+
                  "from gkstavke, nalozi where "+getCorgs("gkstavke") +" "+uvjetA+
                  " and gkstavke" + kontoJe + " and gkstavke.datumknj >='" + util.getFirstDayOfYear(stds.getTimestamp("pocDatum")) + "' "+
                  "and gkstavke.datumknj < " + newDateP + " and gkstavke.rbr=nalozi.rbr and gkstavke.knjig=nalozi.knjig and gkstavke.god=nalozi.god and gkstavke.cvrnal=nalozi.cvrnal group by brojkonta";
      String db = " union all select gkstavkerad.brojkonta as brojkonta, sum(gkstavkerad.ip) as ip, sum(gkstavkerad.id) as id "+
                  "from gkstavkerad, nalozi where "+getCorgs("gkstavkerad")+" " + uvjetB+
                  " and gkstavkerad" + kontoJe + " and gkstavkerad.datumknj >='" + util.getFirstDayOfYear(stds.getTimestamp("pocDatum")) + "' "+
                  "and gkstavkerad.datumknj < " + newDateP + " and gkstavkerad.rbr=nalozi.rbr and gkstavkerad.knjig=nalozi.knjig and gkstavkerad.god=nalozi.god and gkstavkerad.cvrnal=nalozi.cvrnal group by brojkonta";

//      System.out.println("donos : " + da + "\n");
//      System.out.println("donos sa neproknj : " + da+db + "\n");

      if (stds.getString("PRIVREMENO").equals("0")) {
        donosQueryString = da;
      } else {
        donosQueryString = da+db;
      }

      donosSet = util.getNewQueryDataSet(donosQueryString);
    }
//    if (!solo && pocstMetod == 1){
//      queryString += " and gkstavke.cvrnal != '00'";
//    } else if (!solo && pocstMetod == 2){
//      queryString += " and gkstavke.cvrnal = '00'";
//    }

//    System.out.println("queryString ::: "+queryString);
System.out.println(queryString);
    QueryDataSet exRez = util.getNewQueryDataSet(queryString);

//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(exRez);

    exRez.first();

    rcc.EnabDisabAll(jpDetail, false);
    if (exRez.rowCount() <= 0) {
      /*JOptionPane.showConfirmDialog(null, "Nema podataka koji zadovoljavaju traženi uvjet !", "Upozorenje", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      firstESC();*/
//      nemaPodataka = true;
//      this.getJPTV().setDataSet(null);
//      if (!solo) nemaPodatakaZaValutu = true;
      this.getJPTV().setDataSet(null);
      okpresssed = false;
      setNoDataAndReturnImmediately();
    }

//    if (jcbSaldo.isSelected()){
//      if (jrbDatKnjiz.isSelected()) {
//        exRez.setSort(new SortDescriptor(new String[]{"DATUMKNJ"}));
//      } else if (jrbDatDokum.isSelected()) {
//        exRez.setSort(new SortDescriptor(new String[]{"DATDOK"}));
//      }
//    }
/*
//    if (jcbSaldo.isSelected()){
      if (stds.getString("SLJED").equals("K")){ // jrbDatKnjiz.isSelected()) {
        exRez.setSort(new SortDescriptor(new String[]{"DATUMKNJ","CNALOGA"}));
//        outSet.setSort(new SortDescriptor(new String[]{"DATUMKNJ","CNALOGA"}));
      } else if (stds.getString("SLJED").equals("D")){ //jrbDatDokum.isSelected()) {
        exRez.setSort(new SortDescriptor(new String[]{"DATDOK","CNALOGA"}));
//        outSet.setSort(new SortDescriptor(new String[]{"DATDOK","CNALOGA"}));
      }
//    }
*/
      exRez.first();

      if (outSet.isOpen()) {
        outSet.empty();
        outSet.close();
      }

      outSet.setColumns(new Column[]{
        (Column) hr.restart.baza.dM.getDataModule().getGkstavke().getColumn("BROJKONTA").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getGkstavke().getColumn("RBR").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getGkstavke().getColumn("CORG").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getNalozi().getColumn("CNALOGA").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getGkstavke().getColumn("BROJIZV").clone(),
        dm.createTimestampColumn("DATUMKNJ","Knjiženo"),//(Column) hr.restart.baza.dM.getDataModule().getGkstavke().getColumn("DATUMKNJ").clone(),
        dm.createBigDecimalColumn("ID","Duguje",2),//(Column) hr.restart.baza.dM.getDataModule().getGkstavke().getColumn("ID").clone(),
        dm.createBigDecimalColumn("IP","Potražuje",2),//(Column) hr.restart.baza.dM.getDataModule().getGkstavke().getColumn("IP").clone(),
        (Column) colSaldo.clone(),
        dm.createTimestampColumn("DATDOK","Dokument"),//(Column) hr.restart.baza.dM.getDataModule().getGkstavke().getColumn("DATDOK").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getGkstavke().getColumn("OPIS").clone(),
        dm.createIntColumn("CPAR", "Partner")
      });
      if (dev) {
      	outSet.addColumn(dm.createBigDecimalColumn("TECAJ","Tecaj",6));
      	outSet.addColumn(dm.createStringColumn("OZNVAL", "Valuta", 3));
      	outSet.addColumn(dm.createBigDecimalColumn("DEVID","Duguje u valuti",2));
      	outSet.addColumn(dm.createBigDecimalColumn("DEVIP","Potražuje u valuti",2));
      	outSet.getColumn("DEVID").setWidth(10);
        outSet.getColumn("DEVIP").setWidth(10);
        outSet.getColumn("TECAJ").setWidth(8);
        outSet.getColumn("OZNVAL").setWidth(4);
      }
      outSet.addColumn(dm.createStringColumn("STATUS","Status naloga",1));
      outSet.getColumn("BROJIZV").setCaption("Izvod");
      outSet.getColumn("CORG").setWidth(6);
      outSet.getColumn("CNALOGA").setWidth(13);
      outSet.getColumn("DATUMKNJ").setWidth(9);
      outSet.getColumn("DATDOK").setWidth(9);
      outSet.getColumn("ID").setWidth(10);
      outSet.getColumn("IP").setWidth(10);
      outSet.getColumn("SALDO").setWidth(10);
      outSet.getColumn("OPIS").setWidth(30);
      boolean izvodNeed = false;

      String domv = Tecajevi.getDomOZNVAL();
      //BigDecimal saldoStavki = Aus.zero0;

      if (stds.getString("SALDO").equals("D")) { //jcbSaldo.isSelected()) {
        exRez.first();
        outSet.open();
        outSet.first();
        int i = 1;
        String olKon = "", nKon = "";
        do {
          nKon = exRez.getString("BROJKONTA");
          if (!olKon.equals(nKon)){
            olKon = nKon;
            if (!dev && stds.getString("DONOS").equals("D") && lookupData.getlookupData().raLocate(donosSet,"BROJKONTA",exRez.getString("BROJKONTA"))){
//              System.out.println("************************RACUNAM S DONOSOM**************************");
              outSet.insertRow(false);
              outSet.setInt("RBR", i);
              outSet.setInt("CPAR", exRez.getInt("CPAR"));
              outSet.setString("BROJKONTA", exRez.getString("BROJKONTA"));
              outSet.setString("CORG", exRez.getString("CORG"));
              outSet.setString("CNALOGA", "");
              outSet.setUnassignedNull("BROJIZV");
              outSet.setTimestamp("DATUMKNJ", stds.getTimestamp("pocDatum"));
              outSet.setTimestamp("DATDOK", stds.getTimestamp("pocDatum"));
              outSet.setString("OPIS", "Donos");
              outSet.setBigDecimal("ID", donosSet.getBigDecimal("ID"));
              outSet.setBigDecimal("IP", donosSet.getBigDecimal("IP"));
              //saldoStavki = saldoStavki.add(donosSet.getBigDecimal("ID")).subtract(donosSet.getBigDecimal("IP"));
              outSet.setBigDecimal("SALDO", donosSet.getBigDecimal("ID").subtract(donosSet.getBigDecimal("IP")));
              i++;
            /*} else {
              saldoStavki = Aus.zero2;*/
            }
          }
          outSet.insertRow(false);
          outSet.setInt("RBR", i);
          outSet.setInt("CPAR", exRez.getInt("CPAR"));
          outSet.setString("BROJKONTA", exRez.getString("BROJKONTA"));
          outSet.setString("CORG", exRez.getString("CORG"));
          outSet.setString("CNALOGA", exRez.getString("CNALOGA"));
          outSet.setString("STATUS",exRez.getString("STATUS"));
          outSet.setTimestamp("DATUMKNJ", exRez.getTimestamp("DATUMKNJ"));
          outSet.setTimestamp("DATDOK", exRez.getTimestamp("DATDOK"));
          outSet.setString("OPIS", exRez.getString("OPIS"));
          outSet.setBigDecimal("ID", exRez.getBigDecimal("ID"));
          outSet.setBigDecimal("IP", exRez.getBigDecimal("IP"));
          if (dev) {
          	outSet.setBigDecimal("TECAJ", exRez.getBigDecimal("TECAJ"));
          	outSet.setString("OZNVAL", exRez.getString("OZNVAL"));
          	if (exRez.getString("OZNVAL").trim().length() == 0)
          	  outSet.setString("OZNVAL", domv);
          	outSet.setBigDecimal("DEVID", exRez.getBigDecimal("DEVID"));
            outSet.setBigDecimal("DEVIP", exRez.getBigDecimal("DEVIP"));
          }
          //saldoStavki = saldoStavki.add(exRez.getBigDecimal("ID")).subtract(exRez.getBigDecimal("IP"));
          outSet.setBigDecimal("SALDO", exRez.getBigDecimal("ID").subtract(exRez.getBigDecimal("IP")));
          //outSet.setBigDecimal("SALDO", saldoStavki);
          if (!exRez.isNull("BROJIZV")) {
            izvodNeed = true;
            outSet.setInt("BROJIZV", exRez.getInt("BROJIZV"));
          }
          i++;
        } while (exRez.next());
      } else {
        exRez.first();
        outSet.open();
        outSet.first();
        int i = 1;
        String olKon = "", nKon = "";
        do {
          nKon = exRez.getString("BROJKONTA");
          if (!olKon.equals(nKon)){
            olKon = nKon;
            if (!dev && stds.getString("DONOS").equals("D") && lookupData.getlookupData().raLocate(donosSet,"BROJKONTA",exRez.getString("BROJKONTA"))){
              outSet.insertRow(false);
              outSet.setInt("RBR", i);
              outSet.setInt("CPAR", exRez.getInt("CPAR"));
              outSet.setString("BROJKONTA", exRez.getString("BROJKONTA"));
              outSet.setString("CORG", exRez.getString("CORG"));
              outSet.setString("CNALOGA", "");
              outSet.setTimestamp("DATUMKNJ", stds.getTimestamp("pocDatum"));
              outSet.setTimestamp("DATDOK", stds.getTimestamp("pocDatum"));
              outSet.setString("OPIS", "Donos");
              outSet.setBigDecimal("ID", donosSet.getBigDecimal("ID"));
              outSet.setBigDecimal("IP", donosSet.getBigDecimal("IP"));
//              saldoStavki = saldoStavki.add(donosSet.getBigDecimal("ID")).subtract(donosSet.getBigDecimal("IP"));
//              outSet.setBigDecimal("SALDO", saldoStavki);
              i++;
            /*} else {
              saldoStavki = Aus.zero2;*/
            }
          }
          outSet.insertRow(false);
          outSet.setInt("RBR", i);
          outSet.setInt("CPAR", exRez.getInt("CPAR"));
          outSet.setString("BROJKONTA", exRez.getString("BROJKONTA"));
          outSet.setString("CORG", exRez.getString("CORG"));
          outSet.setString("CNALOGA", exRez.getString("CNALOGA"));
          outSet.setString("STATUS",exRez.getString("STATUS"));
          outSet.setTimestamp("DATDOK", exRez.getTimestamp("DATDOK"));
          outSet.setTimestamp("DATUMKNJ", exRez.getTimestamp("DATUMKNJ"));
          outSet.setBigDecimal("ID", exRez.getBigDecimal("ID"));
          outSet.setBigDecimal("IP", exRez.getBigDecimal("IP"));
          if (dev) {
          	outSet.setBigDecimal("TECAJ", exRez.getBigDecimal("TECAJ"));
          	outSet.setString("OZNVAL", exRez.getString("OZNVAL"));
          	outSet.setBigDecimal("DEVID", exRez.getBigDecimal("DEVID"));
            outSet.setBigDecimal("DEVIP", exRez.getBigDecimal("DEVIP"));
          }
//          saldoStavki = saldoStavki.add(exRez.getBigDecimal("ID")).subtract(exRez.getBigDecimal("IP"));
//          outSet.setBigDecimal("SALDO", saldoStavki);
          outSet.setString("OPIS", exRez.getString("OPIS"));
          if (!exRez.isNull("BROJIZV")) {
            izvodNeed = true;
            outSet.setInt("BROJIZV", exRez.getInt("BROJIZV"));
          }
          i++;
        } while (exRez.next());
      }

//      outSet.setSort(new SortDescriptor(new String[]{"BROJKONTA"})); //FAK DIS

      if (stds.getString("ZAKLJKNJ").equals("D")){
//        System.out.println("zakljucno knjizenje");
        outSet.first();
        String olKon = "", nKon = "";
        nKon = outSet.getString("BROJKONTA");
        olKon = nKon;
        BigDecimal sumirano = Aus.zero2;
        int i = 10000000;
        do {
          nKon = outSet.getString("BROJKONTA");
          if (olKon.equals(nKon)  && ((outSet.row()+1) != outSet.rowCount())) {
//            System.out.println("olkon = newkon");
            sumirano = sumirano.add(outSet.getBigDecimal("ID").subtract(outSet.getBigDecimal("IP")));
          } else {
            if ((outSet.row()+1) == outSet.rowCount()){
              sumirano = sumirano.add(outSet.getBigDecimal("ID").subtract(outSet.getBigDecimal("IP")));
            }
//            System.out.println("sad bi triba ubacit red");
            if (sumirano.compareTo(Aus.zero2) < 0){
              outSet.insertRow(false);
              outSet.setInt("RBR", i);
              outSet.setInt("CPAR", exRez.getInt("CPAR"));
              outSet.setString("BROJKONTA", olKon);
              outSet.setString("CORG", exRez.getString("CORG"));
              String god = vl.findYear(stds.getTimestamp("zavDatum"));
              String cnal = "x-".concat(god.concat("-00-000003"));
              outSet.setString("CNALOGA", cnal);
              outSet.setString("STATUS",exRez.getString("STATUS"));
              outSet.setTimestamp("DATDOK", util.getLastDayOfYear(stds.getTimestamp("zavDatum")));
              outSet.setTimestamp("DATUMKNJ", util.getLastDayOfYear(stds.getTimestamp("zavDatum")));
              outSet.setBigDecimal("ID", sumirano.negate());
              outSet.setBigDecimal("IP", Aus.zero2);
              outSet.setString("OPIS", "Zakljuèno knjiženje");
              i++;
            } else {
              outSet.insertRow(false);
              outSet.setInt("RBR", i);
              outSet.setInt("CPAR", exRez.getInt("CPAR"));
              outSet.setString("BROJKONTA", olKon);
              outSet.setString("CORG", exRez.getString("CORG"));
              String god = vl.findYear(stds.getTimestamp("zavDatum"));
              String cnal = "x-".concat(god.concat("-00-000003"));
              outSet.setString("CNALOGA", cnal);
              outSet.setString("STATUS",exRez.getString("STATUS"));
              outSet.setTimestamp("DATDOK", util.getLastDayOfYear(stds.getTimestamp("zavDatum")));
              outSet.setTimestamp("DATUMKNJ", util.getLastDayOfYear(stds.getTimestamp("zavDatum")));
              outSet.setBigDecimal("ID", Aus.zero2);
              outSet.setBigDecimal("IP", sumirano);
              outSet.setString("OPIS", "Zakljuèno knjiženje");
              i++;
            }
            sumirano = Aus.zero2;
            olKon = nKon;
            if ((outSet.row()+1) != outSet.rowCount()) outSet.prior();
          }

        } while (outSet.next());
      }


      if (!frmParam.getParam("gk", "gkKarticaIzvod", "D",
          "Prikazati broj izvoda na konto karticama (D,N)").equalsIgnoreCase("D"))
        izvodNeed = false;

      outSet.getColumn("BROJIZV").setVisible(izvodNeed ? 1 : 0);
      outSet.getColumn("CPAR").setVisible(frmParam.getParam("gk", "gkKarticaPar", "D",
        "Prikazati partnera na konto karticama (D,N)").equalsIgnoreCase("D") ? 1 : 0);
      outSet.getColumn("RBR").setVisible(0);
      outSet.getColumn("BROJKONTA").setVisible(0);
      outSet.getColumn("SALDO").setVisible(stds.getString("SALDO").equals("D") ? 1 : 0);
      /*if (!stds.getString("SALDO").equals("D")) { //) { //!jcbSaldo.isSelected()) {
        outSet.getColumn("DATDOK").setVisible(1);
        outSet.getColumn("DATUMKNJ").setVisible(1);
        outSet.getColumn("SALDO").setVisible(0);
      } else {
        outSet.getColumn("SALDO").setVisible(1);
      }*/
//      if (/*jcbSaldo.isSelected()*/ stds.getString("SALDO").equals("D") && stds.getString("SLJED").equals("K")/*jrbDatKnjiz.isSelected()*/) {
//        outSet.getColumn("DATDOK").setVisible(0);
//        outSet.getColumn("DATUMKNJ").setVisible(1);
//      } else if (/*jcbSaldo.isSelected()*/ stds.getString("SALDO").equals("D") && stds.getString("SLJED").equals("D")/*jrbDatDokum.isSelected()*/) {
//        outSet.getColumn("DATDOK").setVisible(1);
//        outSet.getColumn("DATUMKNJ").setVisible(0);
//      }

      /** @todo shit!!! */

      selectReport();
//
//    if (stds.getString("SALDO").equals("D")) { //jcbSaldo.isSelected()){
//      if (stds.getString("SLJED").equals("K")){ //jrbDatKnjiz.isSelected()) {
//        exRez.setSort(new SortDescriptor(new String[]{"DATUMKNJ"/*,"CNALOGA"*/},false,false));
//        outSet.setSort(new SortDescriptor(new String[]{"DATUMKNJ"/*,"CNALOGA"*/},false,false));
//      } else if (stds.getString("SLJED").equals("D")){ //jrbDatDokum.isSelected()) {
//        exRez.setSort(new SortDescriptor(new String[]{"DATDOK"/*,"CNALOGA"*/},false,false));
//        outSet.setSort(new SortDescriptor(new String[]{"DATDOK"/*,"CNALOGA"*/},false,false));
//      }
//    }
//      System.out.println("!outSet.isEmpty() " + !outSet.isEmpty());
    if (!noJpView) {
      if (modifyOutSet()) {
        outSet.last();
        /*if (!solo) this.getJPTV().setDataSetAndSums(outSet, new String[] {"ID","IP"});
         * 
        else */
        this.getJPTV().removeTableModifier(cpm);
        if (outSet.getColumn("CPAR").getVisible() == 1)
            getJPTV().addTableModifier(cpm);
        this.getJPTV().setDataSetAndSums(outSet, new String[] {"ID","IP"});
        
      } else {
//        System.out.println("nema podataka");
        nemaPodatakaZaValutu = true;
        this.getJPTV().setDataSet(null);
        okpresssed = false;
        setNoDataAndReturnImmediately();
      }
    }
  }
  
  public String navDoubleClickActionName(){
    return "Prikaz dokumenta";
  }

  public int[] navVisibleColumns(){
    return new int[] {0,1,3,4,5,6,7,8,9};
  }

  public void jptv_doubleClick(){
//    System.out.println("this.getJPTV().getDataSet().getRow() " + this.getJPTV().getDataSet().getRow());
//    System.out.println("this.getJPTV().getDataSet() - CNALOGA = '" + this.getJPTV().getDataSet().getString("CNALOGA")+"'");
    java.util.StringTokenizer st = new java.util.StringTokenizer(this.getJPTV().getDataSet().getString("CNALOGA"), "-");
    String[] ki = new String[4];
    int kounter = 0;
    while (st.hasMoreTokens()) {
      ki[kounter++] = st.nextToken();
    }
//    System.out.println("ki 0 " + ki[0]);
//    System.out.println("ki 1 " + ki[1]);
//    System.out.println("ki 2 " + ki[2]);
//    System.out.println("ki 3 " + ki[3]);
    String ki3Demask = "";
    for (int i =0 ; i < ki[3].length() ; i++) {
      if (!ki[3].substring(i,i+1).equals("0")){
        ki3Demask = ki[3].substring(i,ki[3].length());
        break;
      }
    }
//    System.out.println("demask " + ki3Demask);
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(this.getJPTV().getDataSet());
      raMasterDetail.showRecord("hr.restart.gk.frmNalozi",
                                new String[] {"KNJIG","GOD","CVRNAL","RBR"},
                                new String[] {ki[0], ki[1], ki[2], ki3Demask}
      );
  }


  public String getNoDataMessage() {
//    System.out.println("nema podataka mesidz");
    if (nemaPodatakaZaValutu) return "Nema podataka za traženu valutu!";
    return "Nema podataka koji zadovoljavaju tražene uvjete!";
  }


  public boolean modifyOutSet() {
    return true;
  }
  protected void selectReport() {
    killAllReports();
    if (stds.getString("SALDO").equals("D")) { //jcbSaldo.isSelected()) {
      this.addReport("hr.restart.gk.repKarticeGK_Saldirana","hr.restart.gk.repKarticeGK_Saldirana","KarticeGK_Saldirana", "Ispis izvještaja konto kartica");
    } else {
      this.addReport("hr.restart.gk.repKarticeGK", "hr.restart.gk.repKarticeGK", "KarticeGK", "Ispis izvještaja konto kartica");
    }
    this.addReport("hr.restart.gk.repKarticeGKpar", "hr.restart.gk.repKarticeGKpar", "KarticeGKpartneri", "Ispis izvještaja konto kartica po partnerima");
    this.addReport("hr.restart.gk.repKarticeGKcorg", "hr.restart.gk.repKarticeGKcorg", "KarticeGKcorg", "Ispis izvještaja konto kartica po org. jedinicama");
  }
//  Border border1;
//  TitledBorder titledBorder1;
//  JPanel jPanel1 = new JPanel();
  JLabel jlDokSaldo = new JLabel();
//  JraRadioButton jrbDatKnjiz = new JraRadioButton();
//  XYLayout xYLayout1 = new XYLayout();
//  JraRadioButton jrbDatDokum = new JraRadioButton();
//  raButtonGroup buttonGroup1 = new raButtonGroup();


  public void firstESC() {
    System.out.println("okpress " + okpresssed);
    if (solo) {
      rcc.EnabDisabAll(jpDetail, true);
//    setSortPanelLabelLaf();
//    rcc.setLabelLaF(kontoPanel.jlrCorg, false);
//    rcc.setLabelLaF(kontoPanel.jlrNazorg, false);
//    rcc.setLabelLaF(kontoPanel.jbSelCorg, false);
    kontoPanel.jlrKontoBroj_lookup();
    outSet.empty();
    outSet.close();
    killAllReports();
    this.getJPTV().clearDataSet();
    //removeNav();
//      showDefaultValues();
    if (!okpresssed){
      kontoPanel.setcORG(hr.restart.zapod.OrgStr.getKNJCORG());
      kontoPanel.jlrKontoBroj.setText("");
      kontoPanel.jlrKontoBroj.emptyTextFields();
      rcc.setLabelLaF(kontoPanel.jlrCorg, false);
      rcc.setLabelLaF(kontoPanel.jlrNazorg, false);
      rcc.setLabelLaF(kontoPanel.jbSelCorg, false);
    } else okpresssed = false;
    kontoPanel.jlrKontoBroj.requestFocusLater();
    } else {
      //solo = true;
      super.cancelPress();
    }
  }

  public boolean runFirstESC() {
    if (!kontoPanel.jlrKontoBroj.getText().equals("")) {
      return true;
    }
    return false;
  }

  private void jbInit() throws Exception {
    System.out.println("frmKarticaGK.java   -  **************************"); //XDEBUG delete when no more needed
    setStepsNumber(1);
//    border1 = new EtchedBorder(EtchedBorder.RAISED, new Color(224, 255, 255), new Color(109, 129, 140));
//    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(new Color(224, 255, 255), new Color(109, 129, 140)), "");
    this.setJPan(jpDetail);

//    column1 = dm.createTimestampColumn("pocDatum");
//    column2 = dm.createTimestampColumn("zavDatum");

    stds.setColumns(new Column[]{dm.createTimestampColumn("pocDatum"),
                                 dm.createTimestampColumn("zavDatum"),
                                 dm.createStringColumn("PRIVREMENO",1),
                                 dm.createStringColumn("SLJED",1),
                                 dm.createStringColumn("SALDO",1),
                                 dm.createStringColumn("DONOS",1),
                                 dm.createStringColumn("ZAKLJKNJ",1),
                                 dm.createStringColumn("ODKONTA",10),
                                 dm.createStringColumn("ORGSTR",1),
                                 dm.createStringColumn("DOKONTA",10)
    });

    rcbPripOrgJed.setDataSet(stds);
    rcbPripOrgJed.setRaColumn("ORGSTR");
    rcbPripOrgJed.setRaItems(new String[][] {
      {"Cijela struktura org. jed." ,"0"},
      {"Odabrana org. jed.","1"}
    });
//    rcbPripOrgJed.addItemListener(new java.awt.event.ItemListener() {
//      public void itemStateChanged(java.awt.event.ItemEvent e) {
//        corging();
//      }
//    });

    rcbPrivremenost.setDataSet(stds);
    rcbPrivremenost.setRaColumn("PRIVREMENO");
    rcbPrivremenost.setRaItems(new String[][] {
      {"Knjiženi","0"},
      {"Svi","1"}
    });

    rcbSaldo.setDataSet(stds);
    rcbSaldo.setRaColumn("SALDO");
    rcbSaldo.setRaItems(new String[][] {
      {"Iskljuèen","N"},
      {"Ukljuèen","D"}
    });

    rcbDonos.setDataSet(stds);
    rcbDonos.setRaColumn("DONOS");
    rcbDonos.setRaItems(new String[][] {
      {"Bez prikaza donosa","N"},
      {"Prikazati donos","D"}
    });

    rcbSljed.setDataSet(stds);
    rcbSljed.setRaColumn("SLJED");
    rcbSljed.setRaItems(new String[][] {
      {"Datum knjiženja","K"},
      {"Datum dokumenta","D"}
    });

    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(stds);
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);

    jtfZavDatum.setDataSet(stds);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);

//    colRbR.setColumnName("RBR");
//    colRbR.setDataType(com.borland.dx.dataset.Variant.INT);
//    colRbR.setResolvable(false);
//    colRbR.setSqlType(0);
//    colRbR.setCaption(" ");

    colRbR = dm.createIntColumn("RBR","Redni br.");

//    colSaldo.setColumnName("SALDO");
//    colSaldo.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    colSaldo.setDisplayMask("###,###,##0.00");
//    colSaldo.setDefault("0");
//    colSaldo.setResolvable(false);
//    colSaldo.setSqlType(0);
//    colSaldo.setCaption("Saldo");

    colSaldo = dm.createBigDecimalColumn("SALDO","Saldo",2);

    jpDetail.setLayout(layDetail);
//    jcbSaldo.setText("Saldo");
//    jcbSaldo.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jcbSaldo_actionPerformed(e);
//      }
//    });
//    jcbSaldo.setHorizontalAlignment(SwingConstants.RIGHT);
//    jcbSaldo.setHorizontalTextPosition(SwingConstants.LEADING);
//    jcbSaldo.addKeyListener(new hr.restart.swing.JraKeyListener());

//    buttonGroup1.setHorizontalTextPosition(SwingConstants.TRAILING);
    layDetail.setWidth(582);
    layDetail.setHeight(300);
//    buttonGroup1.add(jrbDatDokum, "Datumu dokumenta");
//    buttonGroup1.add(jrbDatKnjiz, "Datumu knjiženja");

//    jcbPrivremeni.setText("Privremeni");
//    jcbPrivremeni.setHorizontalTextPosition(SwingConstants.LEADING);
//    jcbPrivremeni.addKeyListener(new hr.restart.swing.JraKeyListener());
    knjigDifolt = hr.restart.zapod.OrgStr.getKNJCORG();
    jlPeriod.setText("Period (OD - DO)");
    jlKnjigovodstvo.setText("Knjigovodstvo");

    kontoPanel.jlrKontoBroj.setRaDataSet(kontoPanel.getDohvatKonta("A"));

//    jPanel1.setBorder(titledBorder1);
//    jPanel1.setLayout(xYLayout1);
    jlDokSaldo.setText("Dok. / Saldo / Donos");
    jlSljed.setText("Sljed / Org. str.");

    jpDetail.add(kontoPanel, new XYConstraints(15, 20, -1, -1));

//    System.out.println("search mode " + kontoPanel.jlrKontoBroj.getSearchMode());
//    System.out.println("search mode " + kontoPanel.jlrKontoNaziv.getSearchMode());

    jpDetail.setPreferredSize(new Dimension(580, 160));
    jpDetail.add(jlPeriod, new XYConstraints(15, 70, -1, -1));



//    jpDetail.add(jcbPrivremeni, new XYConstraints(380, 70, -1, -1));

    jpDetail.add(rcbPrivremenost, new XYConstraints(150, 95, 100, -1));
    jpDetail.add(rcbSaldo, new XYConstraints(255, 95, 100, -1));
    
    jpDetail.add(rcbSljed, new XYConstraints(150, 120, 205, -1));
    jpDetail.add(rcbPripOrgJed, new XYConstraints(360, 120, 180, -1));
    jpDetail.add(jlSljed, new XYConstraints(15, 120, 100, -1));



//    jpDetail.add(jcbSaldo, new XYConstraints(482, 70, -1, -1));


    jpDetail.add(jtfPocDatum, new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jtfZavDatum, new XYConstraints(255, 70, 100, -1));

    jpDetail.add(rcbDonos, new XYConstraints(360, 95, 180, -1));

//    jpDetail.add(jPanel1, new XYConstraints(150, 120, 390, -1));

    jpDetail.add(jlDokSaldo,  new XYConstraints(15, 95, -1, -1));
//    jPanel1.add(jrbDatKnjiz, new XYConstraints(15, 0, -1, -1));
//    jPanel1.add(jrbDatDokum, new XYConstraints(205, 0, -1, -1));

    /**/
    this.getJPTV().setKumTak(true);
    this.getJPTV().setStoZbrojiti(new String[] {"ID","IP"});
    /**/
    this.getJPTV().getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    this.getJPTV().addTableColorModifier();
    this.getJPTV().addTableModifier(new gkStatusColorModifier(getRaTableUIModifier()));
    this.getJPTV().addTableModifier(new raTableRunningSum("SALDO"));
    new raDateRange(jtfPocDatum, jtfZavDatum);
    if (solo) this.installResetButton();
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnj, String newKnj){
        knjigDifolt = hr.restart.zapod.OrgStr.getKNJCORG();
      }
    });
  }

  /**
   * @return
   */
  private raTableUIModifier getRaTableUIModifier() {
    raTableModifier[] ar = getJPTV().getTableModifiers();
    for (int i = 0; i < ar.length; i++) {
      if (ar[i] instanceof raTableUIModifier) {
        return (raTableUIModifier)ar[i];
      }
    }
    return null;
  }

  void setHeightInstance() {
    jpDetail.setPreferredSize(new Dimension(580, 210));
  }

  public QueryDataSet getRepQDS() {
    return outSet;
  }
  
  public void beforeReport() {
    fkgk = this;
    super.beforeReport();
  }

  public void showDefaultValues() {
//    kontoPanel.jlrKontoBroj.setText("");
//    kontoPanel.jlrKontoNaziv.setText("");
    if (!okpresssed){
    kontoPanel.setcORG(hr.restart.zapod.OrgStr.getKNJCORG());
    kontoPanel.jlrKontoBroj.setText("");
    kontoPanel.jlrKontoBroj.emptyTextFields();
  rcc.setLabelLaF(kontoPanel.jlrCorg, false);
  rcc.setLabelLaF(kontoPanel.jlrNazorg, false);
  rcc.setLabelLaF(kontoPanel.jbSelCorg, false);
    } else {

//      jcbPrivremeni.setSelected(false);

//      rcbPrivremenost.setSelectedIndex(0);
//      stds.setString("Privremeno","0");
      setPripadnostOJ("0");
      setPrivremeno("0");
      setSaldo("N");
      setSljed("K");
      setDonos("N");
//      jcbSaldo.setSelected(false);
//      buttonGroup1.setSelected(jrbDatKnjiz);
      okpresssed = false;
    }
    rcc.EnabDisabAll(jpDetail, true);
//    setSortPanelLabelLaf();
//    rcc.setLabelLaF(kontoPanel.jlrCorg, false);
//    rcc.setLabelLaF(kontoPanel.jlrNazorg, false);
//    rcc.setLabelLaF(kontoPanel.jbSelCorg, false);
    kontoPanel.jlrKontoBroj_lookup();
    kontoPanel.jlrKontoBroj.requestFocus();
    outSet.empty();
    outSet.close();
    killAllReports();
    this.getJPTV().setDataSet(null);
  }

  public void corging(){
    rcc.setLabelLaF(kontoPanel.jlrCorg, stds.getString("ORGSTR").equals("1"));
    rcc.setLabelLaF(kontoPanel.jlrNazorg, stds.getString("ORGSTR").equals("1"));
    rcc.setLabelLaF(kontoPanel.jbSelCorg, stds.getString("ORGSTR").equals("1"));

    if (stds.getString("ORGSTR").equals("1")) {
      kontoPanel.jlrCorg.requestFocus();
    } else {
      kontoPanel.setcORG(knjigDifolt);
      rcbPripOrgJed.requestFocus();
    }

//    rcbPripOrgJed.setDataSet(stds);
//    rcbPripOrgJed.setRaColumn("ORGSTR");

  }

  protected String getCorgs(String table){
//    sysoutTEST st = new sysoutTEST(false);
//    st.prn(stds);
    String sqlCorgString = "";
    if (stds.getString("ORGSTR").equals("1")){ //jrbOdabrana.isSelected()) {
      sqlCorgString = table+".CORG ='" + kontoPanel.getCorg() + "'";
    } else {
      StorageDataSet ojs = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(kontoPanel.getCorg());
//      st.prn(ojs);
      if (ojs.rowCount()==1) return table+".CORG ='" + ojs.getString("CORG").trim() + "'";
      sqlCorgString = Condition.in("CORG",ojs).qualified(table).toString();
    }
//    System.out.println("CONDITION = " + sqlCorgString);
    return sqlCorgString;
  }

  public void resetDefaults() {
    initialValues();
  }
  
  public void initialValues() {
//    setSortPanelLabelLaf();
    if (solo) {
      stds.setTimestamp("pocDatum", hr.restart.robno.Util.getUtil().findFirstDayOfYear());
      stds.setTimestamp("zavDatum", hr.restart.util.Util.getUtil().getLastDayOfMonth());
//      kontoPanel.jlrCorg.setText(knjigDifolt);
//      kontoPanel.jlrCorg.forceFocLost();

//      jcbPrivremeni.setSelected(false);

//      rcbPrivremenost.setSelectedIndex(0);
//      stds.setString("Privremeno","0");
      setPripadnostOJ("0");
      setPrivremeno("0");
      setSaldo("D");

//      jcbSaldo.setSelected(false);
      kontoPanel.setcORG(knjigDifolt);
      rcc.setLabelLaF(kontoPanel.jlrCorg, false);
      rcc.setLabelLaF(kontoPanel.jlrNazorg, false);
      rcc.setLabelLaF(kontoPanel.jbSelCorg, false);
//      kontoPanel.jlrKontoBroj.setText("");
//      kontoPanel.jlrKontoNaziv.setText("");
//      kontoPanel.setBrojKonta("");
      kontoPanel.jlrKontoBroj.setText("");
      kontoPanel.jlrKontoBroj.emptyTextFields();
//      buttonGroup1.setSelected(jrbDatKnjiz);
      setSljed("K");
      setDonos("N");
      outSet.empty();
      outSet.close();
      this.getJPTV().setDataSet(null);
      kontoPanel.jlrKontoBroj.requestFocus();
    } else {
      if (Validacija()) okPress();
      this.getOKPanel().jBOK.setText("Ispis");
      this.getOKPanel().jBOK.setIcon(raImages.getImageIcon(raImages.IMGPRINT));
    }
  }
  
  public void setPripadnostOJ(String pripadnost){
    rcbPripOrgJed.setSelectedIndex(Integer.parseInt(pripadnost));
    stds.setString("ORGSTR",pripadnost);
  }

  public void setPrivremeno(String privremeno){
      rcbPrivremenost.setSelectedIndex(Integer.parseInt(privremeno));
      stds.setString("Privremeno",privremeno);
  }

  public void setSaldo(String saldo){
    stds.setString("SALDO",saldo);
    if (!saldo.equals("D")) rcbSaldo.setSelectedIndex(0);
    else rcbSaldo.setSelectedIndex(1);
  }

  public void setSljed(String sljed){
    stds.setString("SLJED",sljed);
    if (sljed.equals("K")) rcbSljed.setSelectedIndex(0);
    else rcbSljed.setSelectedIndex(1);
  }

  public void setDonos(String donos){
    stds.setString("DONOS",donos);
    if (donos.equals("N")) rcbDonos.setSelectedIndex(0);
    else rcbDonos.setSelectedIndex(1);
  }

  public void setZakljucnoKnjizenje(String zakKnjiz){
    stds.setString("ZAKLJKNJ",zakKnjiz);
    if (zakKnjiz.equals("D")) rcbZakKnj.setSelectedIndex(1);
    else rcbZakKnj.setSelectedIndex(0);
  }

  public boolean isPrivremeno(){
    return stds.getString("PRIVREMENO").equals("1");
  }

  public boolean isPrikazSalda(){
    return stds.getString("SALDO").equals("D");
  }

  public String getSljed(){
    return stds.getString("SLJED");
  }

  public String getCKON() {
    return kontoPanel.getBrojKonta();
  }


  public String getNAZKON() {
    return kontoPanel.jlrKontoNaziv.getText();
  }

  public java.sql.Timestamp getPocDatum() {
    return stds.getTimestamp("pocDatum");
  }

  public java.sql.Timestamp getZavDatum() {
    return stds.getTimestamp("zavDatum");
  }

//  void jcbSaldo_actionPerformed(ActionEvent e) {
//    setSortPanelLabelLaf();
//  }

//  private void setSortPanelLabelLaf() {
//    if (jcbSaldo.isSelected()) {
//      rcc.EnabDisabAll(jPanel1, true);
//    } else {
//      rcc.EnabDisabAll(jPanel1, false);
//    }
//  }

}
