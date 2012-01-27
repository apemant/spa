/****license*****************************************************************
**   file: upKPR_NextGeneration.java
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

import hr.restart.baza.dM;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Date;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class upKPR_NextGeneration extends raUpit {
  BorderLayout borderLayout1 = new BorderLayout();
  dM dm = dM.getDataModule();
  Timestamp date;
  Date dateP = null;
  Date dateZ = null;
  boolean naslovnica;

  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();

  JLabel jLabel1 = new JLabel();
  JLabel jlRbr = new JLabel();
  JPanel jPanel3 = new JPanel();
  JPanel jp = new JPanel();
  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfZavDatum = new JraTextField();

  JraTextField jtfPocRbr = new JraTextField();

  hr.restart.robno._Main main;
  QueryDataSet qdsKPR ;
  QueryDataSet qdsDonos;
  QueryDataSet qdsJPTV;
  raCommonClass rcc = raCommonClass.getraCommonClass();
  java.math.BigDecimal initZAD;
  java.math.BigDecimal initRAZ;

  hr.restart.robno.rapancskl rpcskl = new hr.restart.robno.rapancskl() {
    public void findFocusAfter() {
      jtfPocDatum.requestFocus();
  }};

  TableDataSet tds = new TableDataSet();
  java.math.BigDecimal tempRAZ = main.nul;
  java.math.BigDecimal tempZAD = main.nul;
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  Valid vl;
  XYLayout xYLayout1 = new XYLayout();


  static upKPR_NextGeneration upk;
  public static upKPR_NextGeneration getInstance() {
    if (upk == null) {
      upk = new upKPR_NextGeneration();
    }
    return upk;
  }

  public upKPR_NextGeneration() {
    try {
      jbInit();
      upk = this;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }


  public void componentShow() {
    showDefaultValues();
    String skl = raUser.getInstance().getDefSklad();
    boolean sklPostoji = false;
    QueryDataSet qdsS = new QueryDataSet();

    qdsS = rut.getMPSklDataset();
    qdsS.open();
    qdsS.first();
    while (qdsS.inBounds()) {
      if (skl.equals(qdsS.getString("CSKL"))) {
        sklPostoji = true;
      }
      qdsS.next();
    }
    if (sklPostoji) {
      rpcskl.setCSKL(skl);
      this.jtfPocDatum.requestFocus();
    } else {
      rpcskl.jrfCSKL.requestFocus();
    }
  }

  void showDefaultValues() {
//    System.out.println("prvi  "+ rut.findFirstDayOfYear(Integer.parseInt(vl.findYear())));
//    System.out.println("drugi "+ Valid.getValid().findDate(false, 0));

    tds.setTimestamp("pocDatum", rut.findFirstDayOfYear(Integer.parseInt(vl.findYear())));
    tds.setTimestamp("zavDatum", Valid.getValid().findDate(false, 0));
    tds.setInt("pocRbr",1);
    jp.setPreferredSize(jp.getPreferredSize());
    this.getJPTV().setDataSet(null);
  }


  public void okPress() {
    noData= false;
    naslovnica=false;
//    System.out.println("OKPRESSSSED!!!!");
    makeSet();
  }

  private boolean noData;


  public void firstESC() {
    System.out.println("first esc..." + (this.getJPTV().getDataSet() == null)+ " nodata - "+ noData);
    if (this.getJPTV().getDataSet() == null) {
      if (noData){
        noData = false;
        rcc.EnabDisabAll(this.jPanel3, true);
        tds.setInt("pocRbr",1);
        this.jtfPocDatum.requestFocus();
        return;
      }
      rcc.EnabDisabAll(this.rpcskl, true);
//      rcc.EnabDisabAll(this.jPanel3, true);
      rpcskl.setCSKL("");
    } else {
      this.getJPTV().setDataSet(null);
//      rcc.EnabDisabAll(this.getJPan(), true);
      tds.setInt("pocRbr",1);
      rcc.EnabDisabAll(this.jPanel3, true);
      this.jtfPocDatum.requestFocus();
    }
  }

  private void makeSet(){
    String don = "select rbr, datum, god, kljuc, opis, zad, raz, cskl from KPR where cskl = '"+rpcskl.getCSKL()+"' and god = '"+ut.getYear(tds.getTimestamp("pocDatum"))+"' and datum >= '"+ut.getFirstDayOfYear(tds.getTimestamp("pocDatum"))+"' and datum < '"+ut.getFirstSecondOfDay(tds.getTimestamp("pocDatum"))+"'";
    String kpr = "select rbr, datum, god, kljuc, opis, zad, raz, cskl from KPR where cskl = '"+rpcskl.getCSKL()+"' and god = '"+ut.getYear(tds.getTimestamp("pocDatum"))+"' and datum >= '"+ut.getFirstSecondOfDay(tds.getTimestamp("pocDatum"))+"' and datum <= '"+ut.getLastSecondOfDay(tds.getTimestamp("zavDatum"))+"'";

    boolean donos = false;

    if (tds.getInt("pocRbr") > 1){
      donos = true;
      don = "select rbr, datum, god, kljuc, opis, zad, raz, cskl from KPR where cskl = '"+rpcskl.getCSKL()+"' and god = '"+ut.getYear(tds.getTimestamp("pocDatum"))+"' and rbr < '"+tds.getInt("pocRbr")+"'";
      kpr = "select rbr, datum, god, kljuc, opis, zad, raz, cskl from KPR where cskl = '"+rpcskl.getCSKL()+"' and god = '"+ut.getYear(tds.getTimestamp("pocDatum"))+"' and rbr >= '"+tds.getInt("pocRbr")+"'";
    }

    System.out.println("\n"+don);
    System.out.println("\n"+kpr+"\n");

    qdsDonos = ut.getNewQueryDataSet(don);
    qdsKPR = ut.getNewQueryDataSet(kpr,false);
    qdsKPR.setSort(new SortDescriptor(new String[] {"RBR"}));
    qdsKPR.open();

    if (qdsKPR.isEmpty()) {
      noData = true;
      setNoDataAndReturnImmediately();
    }

    qdsJPTV = new QueryDataSet();
    qdsJPTV.setColumns(qdsKPR.cloneColumns());

    qdsJPTV.getColumn("GOD").setVisible(0);
    qdsJPTV.getColumn("CSKL").setVisible(0);
    qdsJPTV.getColumn("KLJUC").setVisible(0);

    qdsJPTV.getColumn("RBR").setCaption("Rbr");
    qdsJPTV.getColumn("RBR").setWidth(4);
    qdsJPTV.getColumn("DATUM").setCaption("Datum");
    qdsJPTV.getColumn("DATUM").setDisplayMask("dd-MM-yyyy");
    qdsJPTV.getColumn("DATUM").setWidth(10);
    qdsJPTV.getColumn("OPIS").setCaption("Opis");
    qdsJPTV.getColumn("OPIS").setWidth(40);
    qdsJPTV.getColumn("ZAD").setCaption("Iznos zaduženja");
    qdsJPTV.getColumn("ZAD").setDisplayMask("###,###,##0.00");
    qdsJPTV.getColumn("RAZ").setCaption("Iznos razduženja");
    qdsJPTV.getColumn("RAZ").setDisplayMask("###,###,##0.00");


    qdsJPTV.open();

    initZAD = _Main.nul;
    initRAZ = _Main.nul;

    if ((!ut.getFirstDayOfYear(tds.getTimestamp("pocDatum")).equals(ut.getFirstSecondOfDay(tds.getTimestamp("pocDatum")))
         && !qdsDonos.isEmpty()) || donos){
      uguziDonos(qdsDonos);
      qdsJPTV.insertRow(false);
      qdsJPTV.setString("OPIS", "Donos");
      qdsJPTV.setString("KLJUC","DON-120/"+ut.getYear(tds.getTimestamp("pocDatum"))+"-0");
      qdsJPTV.setInt("RBR",0);
      qdsJPTV.setString("GOD",ut.getYear(tds.getTimestamp("pocDatum")));
      if (donos){
        qdsDonos.last();
        qdsJPTV.setTimestamp("DATUM",qdsDonos.getTimestamp("DATUM"));
      }
      else
        qdsJPTV.setTimestamp("DATUM",tds.getTimestamp("pocDatum"));
      qdsJPTV.setBigDecimal("ZAD",initZAD);
      qdsJPTV.setBigDecimal("RAZ",initRAZ);
      qdsJPTV.setString("CSKL",rpcskl.getCSKL());
    }
    qdsKPR.first();
    do {
      qdsJPTV.insertRow(false);
      qdsJPTV.setString("OPIS", qdsKPR.getString("OPIS"));
      qdsJPTV.setString("KLJUC",qdsKPR.getString("KLJUC"));
      qdsJPTV.setInt("RBR",qdsKPR.getInt("RBR"));
      qdsJPTV.setTimestamp("DATUM",qdsKPR.getTimestamp("DATUM"));
      qdsJPTV.setString("GOD",qdsKPR.getString("GOD"));
      qdsJPTV.setBigDecimal("ZAD",qdsKPR.getBigDecimal("ZAD"));
      qdsJPTV.setBigDecimal("RAZ",qdsKPR.getBigDecimal("RAZ"));
      qdsJPTV.setString("CSKL",qdsKPR.getString("CSKL"));
    } while (qdsKPR.next());

//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(qdsDonos);
//    syst.prn(qdsKPR);
//    syst.prn(qdsJPTV);

    fillJPTV(qdsJPTV);
  }

  private void uguziDonos(QueryDataSet donos){
    donos.first();
    do {
      initZAD = initZAD.add(donos.getBigDecimal("ZAD"));
      initRAZ = initRAZ.add(donos.getBigDecimal("RAZ"));
    } while (donos.next());

//    System.out.println("DONOS : zaduženje ["+initZAD+"] razduženje ["+initRAZ+"]");

  }

  private void fillJPTV(QueryDataSet ds){
    this.getJPTV().setDataSetAndSums(ds, new String[] {"ZAD","RAZ"});
  }


  public boolean runFirstESC() {
    return !rpcskl.getCSKL().equals("");
  }

  private void jbInit() throws Exception {
    this.addReport("hr.restart.robno.repKPR_NextGeneration","hr.restart.robno.repKPR_NextGeneration", "KPR", "Knjiga popisa robe");
    rpcskl.jrfCSKL.setRaDataSet(rut.getMPSklDataset());
    setJPan(jp);
    jPanel3.setMinimumSize(new Dimension(604, 40));
    jPanel3.setPreferredSize(new Dimension(655, 58));
    jPanel3.setLayout(xYLayout1);
    jLabel1.setText("Datum (od-do)");
    jlRbr.setText("Po\u010Detni redni broj");
    vl = Valid.getValid();
    jp.setLayout(borderLayout1);
    jp.setMinimumSize(new Dimension(555, 88));
    jp.setPreferredSize(new Dimension(650, 105));
    rpcskl.setMinimumSize(new Dimension(10, 20));
    rpcskl.setPreferredSize(new Dimension(10, 20));
    xYLayout1.setWidth(655);
    xYLayout1.setHeight(110);
    jp.add(rpcskl, BorderLayout.CENTER);
    rpcskl.setRaMode('S');
    tds.setColumns(new Column[]{dM.createTimestampColumn("pocDatum","Poèetni datum"),
                                dM.createTimestampColumn("zavDatum","Završni dautm"),
                                dM.createIntColumn("pocRbr","Poèetni redni broj"),
                                dM.createIntColumn("zavRbr","Završni redni broj")});
    tds.open();

    jtfZavDatum.setDataSet(tds);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);

    jtfPocDatum.setDataSet(tds);
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);

    jtfPocRbr.setDataSet(tds);
    jtfPocRbr.setColumnName("pocRbr");

    new raDateRange(jtfPocDatum, jtfZavDatum);
    jPanel3.add(jLabel1, new XYConstraints(15, 5, -1, -1));
    jPanel3.add(jtfPocDatum, new XYConstraints(150, 4, 100, -1));
    jPanel3.add(jtfZavDatum, new XYConstraints(255, 4, 100, -1));
    jPanel3.add(jtfPocRbr, new XYConstraints(150, 29, 50, -1));
    jPanel3.add(jlRbr, new XYConstraints(15, 30, -1, -1));
    jp.add(jPanel3, BorderLayout.SOUTH);
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        rpcskl.jrfCSKL.setRaDataSet(rut.getMPSklDataset());
      }
    });
  }

  public void jptv_doubleClick(){
    System.out.println("Not inplementid jet :)");
  }

  public QueryDataSet getRepSet(){
    return qdsJPTV;
  }

  public double getDonosZad(){
    return initZAD.doubleValue();
  }

  public double getDonosRaz(){
    return initRAZ.doubleValue();
  }

  public boolean getNaslovnica(){
    return naslovnica;
  }
}
