/****license*****************************************************************
**   file: ispLikvi.java
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
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCorg;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.raUpitLite;

import java.sql.Timestamp;

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

public class ispLikvi extends raUpitLite {

  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.zapod.OrgStr knjOrgStr = hr.restart.zapod.OrgStr.getOrgStr();
  hr.restart.robno._Main main;

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
//  JraButton jbCOrg = new JraButton();
//  JLabel jlOrgjJed = new JLabel();
//  JLabel jlSifra = new JLabel();
//  JLabel jlNaziv = new JLabel();

  XYLayout xYLayout1 = new XYLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  Valid vl = Valid.getValid();
  String currKnj="";

  public static double [] sume;
  public static String datumOd="";
  public static String datumDo="";
  public static int selectedRB = 0;
//  static int OJ =1;
//  static int KO =0;
//  static int IB =0;
  boolean selected;

  QueryDataSet defQDS = new QueryDataSet();
  TableDataSet tds = new TableDataSet();
  StorageDataSet fake = new StorageDataSet();

  public ispLikvi() {
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
    border1 = new EtchedBorder(/*EtchedBorder.RAISED,Color.white,new Color(148, 145, 140)*/);
    xYLayout1.setWidth(555);
    xYLayout1.setHeight(85);
    jp.setLayout(xYLayout1);
    this.setJPan(jp);
    jp.setToolTipText("");
//    jlOrgjJed.setText("Org jedinica");
//    jlSifra.setText("Šifra");
//    jlNaziv.setText("Naziv");
//    jbCOrg.setText("...");
    jLabel1.setText("Datum (od-do)");
//    jcbPripOJ.setText("Ispis pripadajuæih org. jedinica");
    jp.add(jpC, new XYConstraints(0,20,-1,-1));
//    jp.add(jrfCOrgNaz,       new XYConstraints(255, 45, 255, -1));
//    jp.add(jrfCOrg,     new XYConstraints(150, 45, 100, -1));
//    jp.add(jlSifra,   new XYConstraints(150, 20, -1, -1));
//    jp.add(jlNaziv,     new XYConstraints(255, 20, -1, -1));
//    jp.add(jlOrgjJed,    new XYConstraints(15, 45, -1, -1));
    jp.add(jtfPocDatum,    new XYConstraints(150, 45, 100, -1));
    jp.add(jtfZavDatum,      new XYConstraints(255, 45, 100, -1));
    jp.add(jLabel1,   new XYConstraints(15, 45, -1, -1));
//    jp.add(jbCOrg,   new XYConstraints(515, 45, 21, 21));
//    jp.add(jcbPripOJ,      new XYConstraints(150, 95, -1, -1));

    fake.setColumns(new Column[] {dm.createStringColumn("CORG","Org. jedinica",10)});
//    jrfCOrg.setNavButton(this.jbCOrg);

    bindCorg();
//    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
//      public void knjigChanged(String oldKnjig, String newKnjig) {
//        bindCorg();
//      };
//    });

    tds.setColumns(new Column[] {dM.createTimestampColumn("pocDatum"), dM.createTimestampColumn("ZavDatum")});
    jtfZavDatum.setDataSet(tds);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setText("jraTextField2");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfPocDatum.setDataSet(tds);
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setText("jraTextField2");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
  }

  public void componentShow() {
    showDefaultValues();
  }

  public void showDefaultValues() {
    if(!tds.isOpen())
      tds.open();
    if(!defQDS.isOpen()) {
      getDefValues();
      defQDS.open();
    }


    Timestamp lastY, tsCurrY;

    String currY = vl.findYear(defQDS.getTimestamp("DATUM"));
    String trenGod = vl.findYear();

    if(rdOSUtil.getUtil().StrToInt(currY)!=rdOSUtil.getUtil().StrToInt(trenGod)) {
      lastY = Timestamp.valueOf((rdOSUtil.getUtil().StrToInt(currY))+"-01-01 00:00:00.0");
      tsCurrY = hr.restart.util.Util.getUtil().getLastDayOfYear(defQDS.getTimestamp("DATUM"));
    } else {
      lastY = Timestamp.valueOf((rdOSUtil.getUtil().StrToInt(currY))+"-01-01 00:00:00.0");
      tsCurrY = vl.findDate(false, 0);
    }

    tds.setTimestamp(1, tsCurrY);
    tds.setTimestamp(0, lastY);

    jpC.setCorg(hr.restart.zapod.OrgStr.getKNJCORG());
    jpC.rcb.requestFocus();
//    jrfCOrg.forceFocLost();
//    jrfCOrg.requestFocus();
  }

  public boolean runFirstESC() {
    if(!jpC.getCorg().equals(""))
      return true;

    return false;
  }

  public void firstESC() {
    if (okpr) {
      rcc.EnabDisabAll(jp,true);
      jpC.rcb.requestFocus();
      okpr = !okpr;
      return;
    }
    if(!jpC.getCorg().equals("")) {
      fake.setString("CORG", "");
      jpC.setCorg("");
      jpC.rcb.requestFocus();
//      jrfCOrg.requestFocus();
    }
  }

  public void getDefValues() {
    currKnj = knjOrgStr.getKNJCORG();
    String qStr = rdOSUtil.getUtil().getDefaultRekap(currKnj);
    Aus.setFilter(defQDS, qStr);    
  }

  public boolean Validacija() {
    if (vl.isEmpty(jpC.corg)) return false;
    if (tds.getTimestamp("zavDatum").before(tds.getTimestamp("pocDatum"))) {
      jtfPocDatum.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Datumski period nije ispravan","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  protected boolean okpr = false;

  public void okPress(){
    okpr= true;
    int rows = 0;
    rows = prepareIspis();
    if (rows == 0) setNoDataAndReturnImmediately();
    killAllReports();
    if(selected)
      addReport("hr.restart.os.repLikKum","LikvidacijaOS", 5);
    else
      addReport("hr.restart.os.repLik","LikvidacijaOS", 5);
  }

  //******* report
  public static QueryDataSet qds = new QueryDataSet();
  public QueryDataSet tempQds = new QueryDataSet();
  JraTextField jtfZavDatum = new JraTextField();
  JraTextField jtfPocDatum = new JraTextField();
  JLabel jLabel1 = new JLabel();
  Border border1;
//  JraCheckBox jcbPripOJ = new JraCheckBox();

  public static QueryDataSet getQdsIspis() {
    return qds;
  }

  public int prepareIspis() {
    String qStr="";
    selected = jpC.isRecursive();//jcbPripOJ.isSelected();
    
    qStr = rdOSUtil.getUtil().getLikvi(jpC.getCorg(),//jrfCOrg.getText().trim(),
                                       util.getTimestampValue(tds.getTimestamp("pocDatum"),0),
                                       util.getTimestampValue(tds.getTimestamp("zavDatum"),1),
                                       selected);

    datumOd= jtfPocDatum.getText();
    datumDo= jtfZavDatum.getText();
    double osn = 0;
    double isp = 0;
    double saldo = 0;

    Aus.refilter(qds, qStr);
    qds.first();
    do {
      osn=osn + qds.getBigDecimal("OSNOVICA").doubleValue();
      isp=isp + qds.getBigDecimal("ISPRAVAK").doubleValue();
      saldo=saldo+qds.getBigDecimal("SALDO").doubleValue();
      qds.next();
    } while(qds.inBounds());
    sume = new double[] {osn, isp, saldo};
    return qds.getRowCount();
  }

//  private String getPocDatum() {
//    StringBuffer sb = new StringBuffer(util.getTimestampValue(tds.getTimestamp("datum"),0));
//    return sb.replace(6, 11, "01-01").toString();
//  }

  public void show() {
    this.setTitle("Ispis likvidiranih sredstava");
    super.show();
  }

  public void bindCorg() {
    jpC.bind(fake);
//    jrfCOrg.setVisCols(new int[]{0,1});
//    jrfCOrg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
//    jrfCOrg.setColNames(new String[] {"NAZIV"});
//    jrfCOrg.setTextFields(new javax.swing.text.JTextComponent[] {jrfCOrgNaz});
//    jrfCOrg.setColumnName("CORG");
//    jrfCOrg.setSearchMode(0);
//    jrfCOrgNaz.setColumnName("NAZIV");
//    jrfCOrgNaz.setNavProperties(jrfCOrg);
  }

  public boolean isIspis(){
    return false;
  }

  public boolean ispisNow(){
    return true;
  }
}
