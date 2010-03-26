/****license*****************************************************************
**   file: ispInv.java
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

import hr.restart.swing.JraButton;
import hr.restart.swing.jpCorg;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.raUpitLite;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class ispInv extends raUpitLite {
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();

  JPanel jp = new JPanel();

  JLabel jlLokacija = new JLabel();
  JLabel jlOrgjJed = new JLabel();
  JLabel jlSifra = new JLabel();
  JLabel jlNaziv = new JLabel();

  jpCorg jpC = new jpCorg(100,255,true){
    public void afterLookUp(boolean succ){
      if (succ) {
        jrfLokacija.setRaDataSet(rdOSUtil.getUtil().getLokacijeDS(jpC.getCorg(),jpC.isRecursive()));
      }
    }
  };

  JraButton jbLokacija = new JraButton();
//  JraButton jbCOrg = new JraButton();

//  JraCheckBox jcbPripOJ = new JraCheckBox();

  XYLayout xYLayout1 = new XYLayout();

  static QueryDataSet qds = new QueryDataSet();
  QueryDataSet defQDS = new QueryDataSet();
  StorageDataSet fake = new StorageDataSet();
  TableDataSet tds = new TableDataSet();

  JlrNavField jrfNazLokacije = new JlrNavField();
//  JlrNavField jrfCOrgNaz = new JlrNavField();
  JlrNavField jrfLokacija = new JlrNavField();
//  JlrNavField jrfCOrg = new JlrNavField(){
//    public void after_lookUp() {
//      jrfLokacija.setRaDataSet(rdOSUtil.getUtil().getLokacijeDS(jrfCOrg.getText()));
//    }
//  };

  public ispInv() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    tds.setColumns(new Column[] {dm.createTimestampColumn("pocDatum","Poèetni datum"), dm.createTimestampColumn("zavDatum","Završni datum")});
    fake.setColumns(new Column[] {dm.createStringColumn("CORG","Org. jedinica",10), dm.createStringColumn("CLOKACIJE","Lokacija",10)});

    jp.setLayout(xYLayout1);
    xYLayout1.setWidth(555);
    xYLayout1.setHeight(95);
    this.setJPan(jp);
    jp.setToolTipText("");
    jlOrgjJed.setText("Org jedinica");
    jlSifra.setText("Šifra");
    jlNaziv.setText("Naziv");
//    jbCOrg.setText("...");


    jbLokacija.setText("...");
    jrfLokacija.setDataSet(fake);
    jrfLokacija.setVisCols(new int[]{2,3});
    jrfLokacija.setColNames(new String[] {"NAZLOKACIJE"});
    jrfLokacija.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazLokacije});
    jrfLokacija.setColumnName("CLOKACIJE");
    jrfLokacija.setNavButton(this.jbLokacija);

    jrfNazLokacije.setColumnName("NAZLOKACIJE");
    jrfNazLokacije.setNavProperties(jrfLokacija);
    jlLokacija.setText("Lokacija");
//    jcbPripOJ.setText("Ispis pripadajuæih org. jedinica");
    jp.add(jpC, new XYConstraints(0,20,-1,-1));
//    jp.add(jrfCOrgNaz,     new XYConstraints(255, 45, 255, -1));
//    jp.add(jrfCOrg,     new XYConstraints(150, 45, 100, -1));
//    jp.add(jlSifra,   new XYConstraints(150, 20, -1, -1));
//    jp.add(jlNaziv,     new XYConstraints(255, 20, -1, -1));
//    jp.add(jlOrgjJed,    new XYConstraints(15, 45, -1, -1));
//    jp.add(jbCOrg,    new XYConstraints(515, 45, 21, 21));
    jp.add(jrfLokacija,    new XYConstraints(150, 45, 100, -1));
    jp.add(jlLokacija,   new XYConstraints(15, 47, -1, -1));
    jp.add(jrfNazLokacije,    new XYConstraints(255, 45, 255, -1));
    jp.add(jbLokacija,    new XYConstraints(515, 45, 21, 21));
//    jp.add(jcbPripOJ,     new XYConstraints(150, 95, -1, -1));

    bindCorg();
//    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
//        new hr.restart.zapod.raKnjigChangeListener(){
//      public void knjigChanged(String oldKnjig, String newKnjig) {
//        bindCorg();
//      };
//    });
  }

  public void componentShow() {
    showDefaultValues();
  }

  public void showDefaultValues() {
    if(!tds.isOpen())
      tds.open();

    String qStr = rdOSUtil.getUtil().getDefaultRekap();
    Aus.refilter(defQDS, qStr);
    tds.setTimestamp("pocDatum", defQDS.getTimestamp("DATUMOD"));
    tds.setTimestamp("zavDatum", defQDS.getTimestamp("DATUMDO"));
    jpC.rcb.setSelectedIndex(0);
    jpC.setCorg(hr.restart.zapod.OrgStr.getKNJCORG());
    jpC.rcb.requestFocus();
//    jpC.corg.requestFocus();
//    jpC.corg.selectAll();

//    jrfLokacija.requestFocus();
  }

  public boolean runFirstESC() {
    if(!jpC.getCorg().equals("") || !this.jrfLokacija.getText().equals("") ) {
      return true;
    } return false;
  }

  public void firstESC() {
    System.out.println("first esc - okp = " + okpr);
    if (okpr) {
      rcc.EnabDisabAll(jp,true);
      fake.setString("CLOKACIJE", "");
      jrfNazLokacije.setText("");
      jrfLokacija.requestFocus();
      okpr = !okpr;
      return;
    }
    if(!jrfLokacija.getText().equals("")) {
      fake.setString("CLOKACIJE", "");
      jrfNazLokacije.setText("");
      jrfLokacija.requestFocus();
    } else {
      fake.setString("CORG", "");
      jpC.setCorg("");
      jpC.rcb.requestFocus();
    }
  }

  public void getDefValues() {
    String qStr = rdOSUtil.getUtil().getDefaultRekap();
    Aus.setFilter(defQDS, qStr);    
  }

  public boolean Validacija() {
    if (hr.restart.util.Valid.getValid().isEmpty(jpC.corg)) return false;
    return true;
  }

  protected boolean okpr;

  public void okPress(){
    okpr = true;
    int rows = 0;
    rows = prepareIspis();
    if (rows == 0) setNoDataAndReturnImmediately();
    killAllReports();
    //addReport("hr.restart.os.repIspisInv","Rekapitulacija", 5);
    addJasper("repIspisInv", "hr.restart.os.repIspisInv", "repIspisInv.jrxml", "Inventurna lista");
  }

  //******* report

  public static QueryDataSet getQdsIspis() {
    return qds;
  }

  public int prepareIspis() {
    String qStr="";    
    boolean selected = jpC.isRecursive(); //jcbPripOJ.isSelected();
    qStr = rdOSUtil.getUtil().getInv(jpC.getCorg().trim(), jrfLokacija.getText().trim(), selected);
    Aus.refilter(qds, qStr);
    return qds.getRowCount();
  }

//  private String getPocDatum() {
//    StringBuffer sb = new StringBuffer(util.getTimestampValue(tds.getTimestamp("datum"),0));
//    return sb.replace(6, 11, "01-01").toString();
//  }

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
//    jrfCOrg.setDataSet(fake);
//    jrfCOrg.setNavButton(this.jbCOrg);
  }

  public boolean isIspis(){
    return false;
  }

  public boolean ispisNow(){
    return true;
  }

}

