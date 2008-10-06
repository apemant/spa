/****license*****************************************************************
**   file: ispKnjiz.java
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
package hr.restart.os;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCorg;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raUpitLite;

import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class ispKnjiz extends raUpitLite { ///raIspisDialog{
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.zapod.OrgStr knjOrgStr = hr.restart.zapod.OrgStr.getOrgStr();
  hr.restart.robno._Main main;
  Valid vl = Valid.getValid();
  String currKnj="";

  JPanel jp = new JPanel();

  jpCorg jpC = new jpCorg(100,255,true){
    public void afterLookUp(boolean succ){
      if (succ) {
//        jrfLokacija.setRaDataSet(rdOSUtil.getUtil().getLokacijeDS(jpC.getCorg(),jpC.isRecursive()));
      }
    }
  };


//  JlrNavField jrfCOrg = new JlrNavField();
//  JlrNavField jrfCOrgNaz = new JlrNavField();

//  JLabel jlOrgjJed = new JLabel();
//  JLabel jlSifra = new JLabel();
//  JLabel jlNaziv = new JLabel();
  JLabel jlVrPr = new JLabel();
  JLabel jlKonto = new JLabel();
  JLabel jLabel1 = new JLabel();
  JlrNavField jrfVrPromjene = new JlrNavField();
  JlrNavField jrfNazVrPromjene = new JlrNavField();
  JlrNavField jrfKonto = new JlrNavField();
  JlrNavField jrfNazKonto = new JlrNavField();
  JraButton jbKonto = new JraButton();
  JraButton jbVrPr = new JraButton();
//  JraButton jbCOrg = new JraButton();
  JraTextField jtfZavDatum = new JraTextField();
  JraTextField jtfPocDatum = new JraTextField();
//  JraCheckBox jcbPripOJ = new JraCheckBox();
  Border border1;

  XYLayout xYLayout1 = new XYLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;

  public static double [] sume;
  public static String datumOd="";
  public static String datumDo="";
  public static int selectedRB = 0;
  static int OJ =1;
  static int KO =0;
  static int IB =0;
  boolean selected;

  public static QueryDataSet qds = new QueryDataSet();
  public QueryDataSet tempQds = new QueryDataSet();
  QueryDataSet defQDS = new QueryDataSet();
  StorageDataSet fake = new StorageDataSet();
  TableDataSet tds = new TableDataSet();
//  Column column1 = new Column();
//  Column column2 = new Column();
  Column tempCorg = new Column();
  Column tempKonto = new Column();
  Column tempProm = new Column();
  public ispKnjiz() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static int getSelectedRB() {
    return selectedRB;
  }

  private void jbInit() throws Exception {
//    this.setModal(true);
    border1 = new EtchedBorder(/*EtchedBorder.RAISED,Color.white,new Color(148, 145, 140)*/);
    jp.setLayout(xYLayout1);
    xYLayout1.setWidth(555);
    xYLayout1.setHeight(135);
    this.setJPan(jp);
    jp.setToolTipText("");
//    jlOrgjJed.setText("Org jedinica");
//    jlSifra.setText("Šifra");
//    jlNaziv.setText("Naziv");
//    jbCOrg.setText("...");
    jLabel1.setText("Datum (od-do)");

//    jcbPripOJ.setText("Ispis pripadajuæih org. jedinica");

    jp.add(jpC, new XYConstraints(0,20,-1,-1));

//    jp.add(jrfCOrgNaz,     new XYConstraints(255, 45, 255, -1));
//    jp.add(jrfCOrg,     new XYConstraints(150, 45, 100, -1));
//    jp.add(jlSifra,   new XYConstraints(150, 20, -1, -1));
//    jp.add(jlNaziv,     new XYConstraints(255, 20, -1, -1));
//    jp.add(jlOrgjJed,    new XYConstraints(15, 45, -1, -1));
//    jp.add(jbCOrg,   new XYConstraints(515, 45, 21, 21));
    jp.add(jtfPocDatum,   new XYConstraints(150, 95, 100, -1));
    jp.add(jtfZavDatum,    new XYConstraints(255, 95, 100, -1));
    jp.add(jLabel1,  new XYConstraints(15, 95, -1, -1));
    jp.add(jrfVrPromjene,    new XYConstraints(150, 70, 100, -1));
    jp.add(jbVrPr,    new XYConstraints(515, 70, 21, 21));
    jp.add(jrfNazVrPromjene,    new XYConstraints(255, 70, 255, -1));
    jp.add(jlVrPr,    new XYConstraints(15, 70, -1, -1));
    jp.add(jrfKonto,    new XYConstraints(150, 45, 100, -1));
    jp.add(jlKonto,   new XYConstraints(15, 45, -1, -1));
    jp.add(jrfNazKonto,    new XYConstraints(255, 45, 255, -1));
    jp.add(jbKonto,    new XYConstraints(515, 45, 21, 21));
//    jp.add(jcbPripOJ,      new XYConstraints(150, 145, -1, -1));

//    column2.setCaption("zavDatum");
//    column2.setColumnName("zavDatum");
//    column2.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
//    column2.setDisplayMask("dd-MM-yyyy");
//    column2.setResolvable(false);
//    column2.setSqlType(0);
//    column2.setServerColumnName("NewColumn2");
//
//    column1.setCaption("pocDatum");
//    column1.setColumnName("pocDatum");
//    column1.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
//    column1.setDisplayMask("dd-MM-yyyy");
//    column1.setResolvable(false);
//    column1.setSqlType(0);
//    column1.setServerColumnName("NewColumn1");



    //**** binding
    tempProm.setColumnName("CPROMJENE");
    tempProm.setDataType(com.borland.dx.dataset.Variant.STRING);
    jbVrPr.setText("...");
    jlVrPr.setText("Vrsta promjene");
    jrfVrPromjene.setDataSet(fake);
    jrfVrPromjene.setRaDataSet(dm.getOS_Vrpromjene());
    jrfVrPromjene.setVisCols(new int[]{0,1});
    jrfVrPromjene.setColNames(new String[] {"NAZPROMJENE"});
    jrfVrPromjene.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazVrPromjene});
    jrfVrPromjene.setColumnName("CPROMJENE");
    jrfNazVrPromjene.setColumnName("NAZPROMJENE");
    jrfNazVrPromjene.setNavProperties(jrfVrPromjene);
    jrfNazVrPromjene.setSearchMode(1);

    jrfVrPromjene.setNavButton(this.jbVrPr);

    jbKonto.setText("...");
    jlKonto.setText("Konto");
    tempKonto.setColumnName("BROJKONTA");
    tempKonto.setDataType(com.borland.dx.dataset.Variant.STRING);
    jrfKonto.setRaDataSet(hr.restart.zapod.raKonta.getAnalitickaKonta());
    jrfKonto.setVisCols(new int[]{0,1});
    jrfKonto.setColNames(new String[] {"NAZIVKONTA"});
    jrfKonto.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazKonto});
    jrfKonto.setColumnName("BROJKONTA");
    jrfNazKonto.setColumnName("NAZIVKONTA");
    jrfNazKonto.setNavProperties(jrfKonto);
    jrfKonto.setDataSet(fake);
    jrfKonto.setNavButton(this.jbKonto);
    jrfKonto.setSearchMode(3);

    tempCorg.setColumnName("CORG");
    tempCorg.setDataType(com.borland.dx.dataset.Variant.STRING);

    bindCorg();
//    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
//      new hr.restart.zapod.raKnjigChangeListener(){
//        public void knjigChanged(String oldKnjig, String newKnjig) {
//          bindCorg();
//        };
//      });
    tds.setColumns(new Column[] {dM.createTimestampColumn("pocDatum"),dM.createTimestampColumn("ZavDatum") });
    jtfZavDatum.setDataSet(tds);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setText("jraTextField2");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfPocDatum.setDataSet(tds);
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setText("jraTextField2");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    fake.setColumns(new Column[] {tempCorg, tempKonto, tempProm});
  }

  public void componentShow() {
    showDefaultValues();
  }

  public void showDefaultValues() {
    if(!tds.isOpen())
      tds.open();    

//    String qStr = rdOSUtil.getUtil().getDefaultRekap();
//    defQDS.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
//    defQDS.open();

    currKnj = knjOrgStr.getKNJCORG();
    String qStr = rdOSUtil.getUtil().getDefaultRekap(currKnj);
    Aus.refilter(qds, qStr);    

//    Timestamp lastY, tsCurrY;
    
    getDefValues();
    
//     String currY = vl.findYear(defQDS.getTimestamp("DATUM"));
//     String trenGod = vl.findYear();
//
//     if(rdOSUtil.getUtil().StrToInt(currY)!=rdOSUtil.getUtil().StrToInt(trenGod)) {
//       lastY = Timestamp.valueOf((rdOSUtil.getUtil().StrToInt(currY))+"-01-01 00:00:00.0");
//       tsCurrY = hr.restart.util.Util.getUtil().getLastDayOfYear(defQDS.getTimestamp("DATUM"));
//     } else {
//       lastY = Timestamp.valueOf((rdOSUtil.getUtil().StrToInt(currY))+"-01-01 00:00:00.0");
//       tsCurrY = vl.findDate(false, 0);
//    }

    tds.setTimestamp("zavDatum",hr.restart.util.Util.getUtil().getFirstSecondOfDay(defQDS.getTimestamp("DATUMDO"))/*tsCurrY*/ );
    tds.setTimestamp("pocDatum",hr.restart.util.Util.getUtil().getLastSecondOfDay(defQDS.getTimestamp("DATUMOD"))/*lastY*/ );

    fake.setString("BROJKONTA", "");
    fake.setString("CPROMJENE", "");
    jpC.rcb.setSelectedIndex(0);

    jpC.setCorg(hr.restart.zapod.OrgStr.getKNJCORG());
//    jpC.corg.forceFocLost();
    jpC.rcb.requestFocus();
  }

  public boolean runFirstESC() {
    if(!jpC.getCorg().equals("") || !jrfKonto.getText().equals("") || !jrfVrPromjene.getText().equals("")) {
      return true;
    }
    return false;
  }

  public void firstESC() {
    if (okpr){
      rcc.EnabDisabAll(jp,true);
      fake.setString("CPROMJENE", "");
      jrfNazVrPromjene.setText("");
      fake.setString("BROJKONTA", "");
      jrfNazKonto.setText("");
      jrfKonto.requestFocus();
      okpr = !okpr;
      return;
    } if(!jrfVrPromjene.getText().equals("")) {
      fake.setString("CPROMJENE", "");
      jrfNazVrPromjene.setText("");
      jrfVrPromjene.requestFocus();
    } else if(!jrfKonto.getText().equals("")) {
      fake.setString("BROJKONTA", "");
      jrfNazKonto.setText("");
      jrfKonto.requestFocus();
    } else {
      fake.setString("CORG", "");
      jpC.setCorg("");
      jpC.rcb.requestFocus();
    }
  }

  public void getDefValues() {
//    hr.restart.robno._Main.getStartFrame().statusMSG("Priprema podataka");
    String qStr = rdOSUtil.getUtil().getDefaultRekap();
//    defQDS = hr.restart.util.Util.getUtil().getNewQueryDataSet(qStr);
    Aus.setFilter(defQDS, qStr);
    defQDS.open();
//    hr.restart.robno._Main.getStartFrame().statusMSG();
  }

  public boolean Validacija() {
    if (vl.isEmpty(jpC.corg)) return false;
//    if(jpC.getCorg().equals("")) {
//      JOptionPane.showConfirmDialog(this.jp,"Obavezan unos org. jedinice !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
//      jpC.corg.requestFocus();
//      return false;
//    }
    if(tds.getTimestamp("zavDatum").before(tds.getTimestamp("pocDatum"))) {
      jtfPocDatum.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Poèetni datum veæi od završnog!","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }
//    String os_kYear = vl.findYear(defQDS.getTimestamp("DATUM"));
//    String jtfYear = vl.findYear(tds.getTimestamp("pocDatum"));
//    if(rdOSUtil.getUtil().StrToInt(jtfYear) != (rdOSUtil.getUtil().StrToInt(os_kYear)-1)  )
//    {
//      jtfPocDatum.requestFocus();
//      JOptionPane.showConfirmDialog(this.jp,"Pogrešan poèetni datum !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
//      return false;
//    }
//    if(!vl.findYear(defQDS.getTimestamp("DATUM")).equals(vl.findYear(tds.getTimestamp("zavDatum"))))
//    {
//      jtfZavDatum.requestFocus();
//      JOptionPane.showConfirmDialog(this.jp,"Pogrešan završni datum !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
//      return false;
//    }
//
//    int rows = 0;
//    rows = prepareIspis();
//    if (rows == 0)
//    {
//      JOptionPane.showConfirmDialog(this.jp,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
//      return false;
//    }
    return true;
  }

  boolean okpr = false;

  public void okPress(){
    okpr=true;
    int rows = 0;
    rows = prepareIspis();

    if (rows == 0) setNoDataAndReturnImmediately();

    killAllReports();
//    if(selected)
//      this.addReport("hr.restart.os.repIzKnjizKum","hr.restart.os.repIzKnjiz","IzKnjizKum","Rekapitulacija");
//    else
      this.addReport("hr.restart.os.repIzKnjiz","hr.restart.os.repIzKnjiz","IzKnjiz","Rekapitulacija");
  }

  public boolean isIspis(){
    return false;
  }

  public boolean ispisNow(){
    return true;
  }

  public static QueryDataSet getQdsIspis() {
    return qds;
  }

  protected int prepareIspis() {
    String qStr="";    
    selected = jpC.isRecursive();//jcbPripOJ.isSelected();
    qStr = rdOSUtil.getUtil().getIzKnjiz(/*jrfCOrg.getText().trim()*/jpC.getCorg(), jrfKonto.getText().trim(), jrfVrPromjene.getText().trim(),
        util.getTimestampValue(tds.getTimestamp("pocDatum"),0), util.getTimestampValue(tds.getTimestamp("zavDatum"),1), selected);
    System.out.println("KNIZ: " + qStr);    

    BigDecimal osnDuguje = new BigDecimal(0);
    BigDecimal osnPotrazuje = new BigDecimal(0);
    BigDecimal ispDuguje = new BigDecimal(0);
    BigDecimal ispPotrazuje = new BigDecimal(0);
    BigDecimal saldo = new BigDecimal(0);

    Aus.refilter(qds, qStr);
    qds.first();
    do {
      osnDuguje=osnDuguje.add(qds.getBigDecimal("OSNDUGUJE"));
      osnPotrazuje=osnPotrazuje.add(qds.getBigDecimal("OSNPOTRAZUJE"));
      ispDuguje=ispDuguje.add(qds.getBigDecimal("ISPDUGUJE"));
      ispPotrazuje=ispPotrazuje.add(qds.getBigDecimal("ISPPOTRAZUJE"));
      saldo=saldo.add(qds.getBigDecimal("SALDO"));
      qds.next();
    } while(qds.inBounds());

    sume = new double[] {osnDuguje.doubleValue(), osnPotrazuje.doubleValue(), ispDuguje.doubleValue(),
      ispPotrazuje.doubleValue(), saldo.doubleValue()};
    datumOd= jtfPocDatum.getText();
    datumDo= jtfZavDatum.getText();
    return qds.getRowCount();
  }

//  private String getPocDatum() {
//    StringBuffer sb = new StringBuffer(util.getTimestampValue(tds.getTimestamp("datum"),0));
//    return sb.replace(6, 11, "01-01").toString();
//  }

  private void bindCorg() {
    jpC.bind(fake);
//    jrfCOrg.setVisCols(new int[]{0,1});
//    jrfCOrg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
//    jrfCOrg.setColNames(new String[] {"NAZIV"});
//    jrfCOrg.setTextFields(new javax.swing.text.JTextComponent[] {jrfCOrgNaz});
//    jrfCOrg.setColumnName("CORG");
//    jrfCOrg.setSearchMode(0);
//    jrfCOrgNaz.setColumnName("NAZIV");
//    jrfCOrgNaz.setNavProperties(jrfCOrg);
//    jrfCOrg.setDataSet(fake);
//    jrfCOrg.setNavButton(this.jbCOrg);
  }
}
