/****license*****************************************************************
**   file: ispAmor_NextGeneration.java
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

import hr.restart.baza.Condition;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.jpCorg;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.raComboBox;
import hr.restart.util.raUpitLite;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class ispAmor_NextGeneration extends raUpitLite {

  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.Util util = hr.restart.util.Util.getUtil();
  hr.restart.zapod.OrgStr knjOrgStr = hr.restart.zapod.OrgStr.getOrgStr();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();

  JPanel jp = new JPanel();
  JPanel corgPanel = new JPanel();
//  JPanel vrstaPanel = new JPanel();
  JPanel oblikPanel = new JPanel();
  JPanel jpJcbOblikIspisa = new JPanel();

  jpCorg jpC = new jpCorg(100,275,true);

  XYLayout jpLayout = new XYLayout();
  XYLayout corgPanellayout = new XYLayout();
  XYLayout vrstaPanelLayout = new XYLayout();
  XYLayout oblikPanelLayout = new XYLayout();
  XYLayout xyLayout5 = new XYLayout();

//  JlrNavField jlrCorg = new JlrNavField();
//  JlrNavField jlrNazCorg = new JlrNavField();
//  JraButton jbCorgBotun = new JraButton();

//  JraRadioButton rbAmortizacija = new JraRadioButton();
//  JraRadioButton rbLikvidacija = new JraRadioButton();
//  raButtonGroup radioGroupVrsta = new raButtonGroup();

  raComboBox rcbAktivnost = new raComboBox();

  JraRadioButton rbIspKont = new JraRadioButton();
  JraRadioButton rbIspAmGr = new JraRadioButton();
  JraRadioButton rbIspLokacije = new JraRadioButton();
  JraRadioButton rbIspRevSk = new JraRadioButton();
  JraRadioButton rbIspAtrikl = new JraRadioButton();
  raButtonGroup radioGroupOblik = new raButtonGroup();

//  JraCheckBox jcbPripOJ = new JraCheckBox();
  JraCheckBox jcbPoOJ = new JraCheckBox();
  JraCheckBox jcbOblikIsp = new JraCheckBox();
  JraCheckBox jcbSInvBr = new JraCheckBox();

  StorageDataSet mainStorage = new StorageDataSet();

  QueryDataSet ispisDataSet = new QueryDataSet();

  static ispAmor_NextGeneration iaNG;

  public ispAmor_NextGeneration() {
    try {
      initializer();
      iaNG = this;
    } catch (Exception ex) {
    }
  }

  public static ispAmor_NextGeneration getInstance(){
    if (iaNG == null) return new ispAmor_NextGeneration();
    else return iaNG;
  }

  public void componentShow() {
    defaultState();
    ispisDataSet = null;
//    output();
  }

  private void podaciIzMetaobrade(){
    QueryDataSet metaobrada = util.getNewQueryDataSet("select corg as corg, datumod as datumod, datumdo as datumdo from os_metaobrada");
    mainStorage.setTimestamp("DATUMOD",metaobrada.getTimestamp("DATUMOD"));
    mainStorage.setTimestamp("DATUMDO",metaobrada.getTimestamp("DATUMDO"));
  }

  private void defaultState() {
    jpC.corg.setText(hr.restart.zapod.OrgStr.getKNJCORG());
    jpC.corg.forceFocLost();
    jpC.rcb.setSelectedIndex(0);
    mainStorage.setString("VRSTA","A");
    rcbAktivnost.setSelectedIndex(0);//radioGroupVrsta.setSelected(rbAmortizacija);
//    jcbPripOJ.setSelected(false);
    jcbPoOJ.setSelected();
    jcbOblikIsp.setSelected(false);
    jcbSInvBr.setSelected(false);
    jpC.corg.requestFocus();
    jpC.corg.selectAll();
  }

  public boolean Validacija() {
//    return !vl.isEmpty(jlrCorg);
    if (vl.isEmpty(jpC.corg)) return false;
    if (!jcbPoOJ.isSelected() && !jcbOblikIsp.isSelected() && !jcbSInvBr.isSelected()){
      jcbPoOJ.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Obavezno odabrati vrstu ispisa !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  private boolean okpressed;

  public void okPress() {
    okpressed = true;
    podaciIzMetaobrade();
    ispisDataSet = mainWork();
    output();
    if (ispisDataSet == null || ispisDataSet.getRowCount() == 0) setNoDataAndReturnImmediately();
    setReportTemplates();
  }

  private QueryDataSet mainWork(){
    QueryDataSet workSet;
    String query;
    if (mainStorage.getString("VRSTA").equals("A")){
      query = queryString_Amortizacija();
    } else {
      query = queryString_Likvidacija();
    }
    System.out.println("QUERY : \n"+query);
      workSet = util.getNewQueryDataSet(query);
    return workSet;
  }

  private String queryString_Amortizacija(){
    String corging = "";
    String group1, group2, group3;

    if (isPripOrgJed()) {
      StorageDataSet ojs = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(mainStorage.getString("CORG"));
      corging += Condition.in("CORG",ojs).qualified("OS_OBRADA2").toString();
    } else
      corging += "OS_OBRADA2.CORG = '" + mainStorage.getString("CORG").trim() + "'";

    group1 = (isPoOrgJed()   ? "os_obrada2.corg" : "");
    group2 = (isOblikIsp()   ? ((isPoOrgJed()) ? ", os_obrada2."+mainStorage.getString("OBLIK") : " os_obrada2."+mainStorage.getString("OBLIK")) : "");
    group3 = (isSInvBrojem() ? ((isOblikIsp() || isPoOrgJed()) ? ", os_obrada2.invbroj" : " os_obrada2.invbroj") : "");


    String qStr = "select max(os_metaobrada.tipamor) as tipamor, max(os_obrada2.corg) as corg, max(os_obrada2.clokacije) as clokacije, max(os_obrada2.cartikla) as cartikla, max(os_obrada2.brojkonta) as brojkonta, max(os_obrada2.invbroj) as invbroj, "+
                  "max(os_obrada2.cgrupe) as cgrupe, max(os_obrada2.cskupine) as cskupine, max(os_obrada2.nazsredstva) as nazsredstva, max(os_obrada2.zakstopa) as zakstopa, max(os_obrada2.odlstopa) as odlstopa, "+
                  "max(os_obrada2.datpromjene) as datpromjene, sum(os_obrada2.osnovica) as osnovica, sum(os_obrada2.ispravak) as ispravak, sum(os_obrada2.sadvrijed) as sadvrijed, "+
                  "sum(os_obrada2.amortizacija) as amortizacija, sum(os_obrada2.pamortizacija) as pamortizacija, min(os_metaobrada.datumod) as datumod, max(os_metaobrada.datumdo) as datumdo from os_obrada2, os_metaobrada"+
                  " where " + corging + " and os_obrada2.corg = os_metaobrada.corg group by " + group1 + group2 + group3;
    return qStr;
  }

  private String queryString_Likvidacija(){
    String corging = "";
    String qStr = "";

    if (isPripOrgJed()) {
      StorageDataSet ojs = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(mainStorage.getString("CORG"));
      corging += Condition.in("CORG",ojs).qualified("OS_OBRADA4").toString();
    } else {
      corging += "OS_OBRADA4.CORG = '" + mainStorage.getString("CORG").trim() + "'";
    }
    String datrange = Condition.between("DATLIKVIDACIJE", getDatumOd(), getDatumDo()).toString();
    if (isSInvBrojem()){
      qStr = "select corg as corg, invbroj as invbroj, nazsredstva as nazsredstva, datpromjene as datpr, datlikvidacije as datlik, "+
             "osnovica as nabvrijed, ispravak as ispravak, amortizacija as amortizacija, "+
             "(ispravak + amortizacija) as stvarniisp, mjesec as mjesec from os_obrada4 "+
             "where " + corging + " AND " + datrange + " order by corg";
    } else {
      qStr = "select corg, sum(osnovica) as nabvrijed,"+
             "sum(ispravak) as ispravak, sum(amortizacija) as amortizacija, "+
             "sum(ispravak + amortizacija) as stvarniisp from os_obrada4 "+
             "where " + corging + " AND " + datrange + " group by corg";
    }
    return qStr;
  }

  public boolean runFirstESC() {
    if(!mainStorage.getString("CORG").equals("")) {
      return true;
    }
    return false;
  }

  public void firstESC() {
    if (okpressed) {
      rcc.EnabDisabAll(jp,true);
      if (mainStorage.getString("VRSTA").equals("A")){
        rcc.EnabDisabAll(oblikPanel,isOblikIsp());
      } else {
        rcc.setLabelLaF(jcbOblikIsp,false);
        rcc.EnabDisabAll(oblikPanel,false);
      }
        rcc.EnabDisabAll(oblikPanel,isOblikIsp());
        jpC.corg.requestFocus();
        okpressed = false;

    } else {
      defaultState();
      jpC.corg.setText("");
      jpC.corg.forceFocLost();
      jpC.corg.requestFocus();
    }
  }

  private void initializer() throws Exception {
    handleSets();
    handleCorg();
    handleRadioButtons();

    jp.setLayout(jpLayout);
    corgPanel.setLayout(corgPanellayout);
    corgPanel.setBorder(new TitledBorder  (BorderFactory.createEtchedBorder(/*Color.white,new Color(148, 145, 140)*/),"Org. jedinica"));
//    vrstaPanel.setLayout(vrstaPanelLayout);
//    vrstaPanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Vrsta ispisa"));
    oblikPanel.setLayout(oblikPanelLayout);
    oblikPanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(/*Color.white,new Color(148, 145, 140)*/)));//,"Oblik ispisa"));

    jpLayout.setWidth(480);
    jpLayout.setHeight(338);

    xyLayout5.setWidth(50);
    xyLayout5.setHeight(10);

    jpJcbOblikIspisa.setLayout(xyLayout5);

//    jcbPripOJ.setText("S pripadajuæim org. jedinicama");
    jcbPoOJ.setText("Ispis po org. jedinici");
    jcbOblikIsp.setText("Oblik Ispisa");
    jcbSInvBr.setText("Ispis s inventarskim brojem");
//    jcbOblikIsp.setHorizontalTextPosition(SwingConstants.LEADING);

    addItemListenersOnBoxesAndButtons();

    rcbAktivnost.setRaColumn("VRSTA");
    rcbAktivnost.setRaDataSet(mainStorage);
    rcbAktivnost.setRaItems(new String[][] {
      {"U upotrebi","A"},
      {"Likvidirana","L"}});


    this.setJPan(jp);
    jpC.lay.setHeight(30);
    jpC.lay.setWidth(430);
//    jpC.setBackground(Color.yellow);
    jpC.add(jpC.corg, new XYConstraints(15, 0, 100, -1));
    jpC.add(jpC.naziv, new XYConstraints(120, 0, 275, -1));
    jpC.add(jpC.but, new XYConstraints(401,0,21,21));

    jp.add(jpC.rcb, new XYConstraints(345, 104, 120, -1));
    jp.add(new JLabel("Dohvat"),new XYConstraints(280, 104, -1, -1));
    jp.add(rcbAktivnost, new XYConstraints(142, 104, 120, -1));
    jp.add(new JLabel("Sredstva"), new XYConstraints(15, 104, -1, -1));


    jpJcbOblikIspisa.add(jcbOblikIsp,         new XYConstraints(5, 0, -1, -1));
    jp.add(jpJcbOblikIspisa,         new XYConstraints(31, 125, 94, 25)); // -100 za pocetak -15 nadodano +3 nadodabno

    corgPanel.add(jpC,   new XYConstraints(0, 7, -1, -1));

//    corgPanel.add(jlrCorg, new XYConstraints(15,7,100,-1));
//    corgPanel.add(jlrNazCorg, new XYConstraints(120,7,275,-1));
//    corgPanel.add(jbCorgBotun, new XYConstraints(401,7,21,21));

//    vrstaPanel.add(rbAmortizacija,new XYConstraints(8, 5, -1, -1));
//    vrstaPanel.add(rbLikvidacija,new XYConstraints(8, 30, -1, -1));

    oblikPanel.add(rbIspKont, new XYConstraints(8, 15, -1, -1));
    oblikPanel.add(rbIspAmGr, new XYConstraints(8, 40, -1, -1));
    oblikPanel.add(rbIspLokacije, new XYConstraints(8, 65, -1, -1));
    oblikPanel.add(rbIspRevSk, new XYConstraints(8, 90, -1, -1));
    oblikPanel.add(rbIspAtrikl, new XYConstraints(8, 115, -1, -1));

//    jp.add(jcbPripOJ, new XYConstraints(36, 100, -1, -1));
//    jp.add(jcbPoOJ,  new XYConstraints(36, 220, -1, -1));
//    jp.add(jcbOblikIsp,      new XYConstraints(357, 220, -1, -1));
//    jp.add(jcbSInvBr,      new XYConstraints(36, 415, -1, -1));


    jp.add(jcbSInvBr,       new XYConstraints(280, 303, -1, -1)); // -100 za pocetak -15 nadodano +3 nadodabno
    jp.add(jcbPoOJ,       new XYConstraints(36, 303, -1, -1));  // -100 za pocetak -15 nadodano +3 nadodabno


    jp.add(corgPanel,new XYConstraints(15,24,450,70));
//    jp.add(vrstaPanel,      new XYConstraints(15, 128, 259, 85));
    jp.add(oblikPanel,   new XYConstraints(15, 136, 450, 162)); // -100 za pocetak -15 nadodano +3 nadodabno

//    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
//      public void knjigChanged(String oldKnjig, String newKnjig) {
//        handleCorg();
//      };
//    });
  }

  private void handleSets(){
    mainStorage.setColumns(new Column[] {dm.createStringColumn("CORG","Organizacijska jedinica",12),
                                         dm.createStringColumn("VRSTA",1),
                                         dm.createStringColumn("OBLIK",15),
                                         dm.createTimestampColumn("DATUMOD"),
                                         dm.createTimestampColumn("DATUMDO"),
                                         dm.createStringColumn("AKTIV",1)});

  }

  private void handleCorg() {
    jpC.corg.setDataSet(mainStorage);
//    jlrCorg.setVisCols(new int[]{0,1});
//    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
//    jlrCorg.setColNames(new String[] {"NAZIV"});
//    jlrCorg.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazCorg});
//    jlrCorg.setColumnName("CORG");
//    jlrCorg.setSearchMode(0);
//    jlrCorg.setNavButton(this.jbCorgBotun);
//    jlrNazCorg.setColumnName("NAZIV");
//    jlrNazCorg.setNavProperties(jlrCorg);
  }

  private void handleRadioButtons(){
//    radioGroupVrsta.setHorizontalTextPosition(SwingConstants.TRAILING);
//    radioGroupVrsta.setDataSet(mainStorage);
//    radioGroupVrsta.setColumnName("VRSTA");
//    radioGroupVrsta.add(rbAmortizacija,"Amortizacija","A");
//    radioGroupVrsta.add(rbLikvidacija,"Likvidirana sredstva","L");

    radioGroupOblik.setHorizontalTextPosition(SwingConstants.TRAILING);
    radioGroupOblik.setDataSet(mainStorage);
    radioGroupOblik.setColumnName("OBLIK");
    radioGroupOblik.add(rbIspKont,"Ispis po kontima","BROJKONTA");
    radioGroupOblik.add(rbIspAmGr,"Ispis po amortizacijskim grupama","CGRUPE");
    radioGroupOblik.add(rbIspLokacije,"Ispis po Lokacijama","CLOKACIJE");
    radioGroupOblik.add(rbIspRevSk,"Ispis po revalorizacijskim skupinama","CSKUPINE");
    radioGroupOblik.add(rbIspAtrikl,"Ispis po Artiklima","CARTIKLA");
  }

  private void addItemListenersOnBoxesAndButtons(){
    jcbOblikIsp.addItemListener(new java.awt.event.ItemListener(){
      public void itemStateChanged(ItemEvent i){
        handleEventOblikIspisa(i);
      }
    });

    rcbAktivnost.addActionListener(new java.awt.event.ActionListener(){
      public void actionPerformed(ActionEvent ae){
        handleEventLikvidacija(ae);
      }
    });
//    rbLikvidacija.addItemListener(new java.awt.event.ItemListener(){
//      public void itemStateChanged(ItemEvent i){
//        handleEventLikvidacija(i);
//      }
//    });
  }

  public boolean isPripOrgJed(){
    return jpC.isRecursive();//jcbPripOJ.isSelected();
  }

  public boolean isPoOrgJed(){
    return jcbPoOJ.isSelected();
  }

  public boolean isOblikIsp(){
    return jcbOblikIsp.isSelected();
  }

  private void handleEventOblikIspisa(ItemEvent ie){
    rcc.EnabDisabAll(oblikPanel, (ie.getStateChange() == ie.SELECTED));
    radioGroupOblik.setSelected(rbIspKont);
  }

  private void handleEventLikvidacija(ActionEvent ie){
    if (!okpressed){
      rcc.EnabDisabAll(jpJcbOblikIspisa,(rcbAktivnost.getSelectedIndex()!=1));
      rcc.EnabDisabAll(oblikPanel,(rcbAktivnost.getSelectedIndex()!=1 && isOblikIsp()));
    }
  }

  public boolean isSInvBrojem(){
    return jcbSInvBr.isSelected();
  }

  public QueryDataSet getIspisDataSet(){
    return ispisDataSet;
  }

  public String getOgrJed(){
    return mainStorage.getString("CORG");
  }

  public String getVrstaIspisa(){
    return mainStorage.getString("VRSTA");
  }

  public String getOblikIspisa(){
    return mainStorage.getString("OBLIK");
  }

  public boolean isIspis(){
    return false;
  }

  public boolean ispisNow(){
    return true;
  }

  public Timestamp getDatumOd(){
    return mainStorage.getTimestamp("DATUMOD");
  }

  public Timestamp getDatumDo(){
    return mainStorage.getTimestamp("DATUMDO");
  }

  private void setReportTemplates() {
    killAllReports();
    if(getVrstaIspisa().equals("A")) {
      if (jcbPoOJ.isSelected() && !jcbOblikIsp.isSelected() && !jcbSInvBr.isSelected()) {
        System.out.println("repIspisAmor_1");
        this.addReport("hr.restart.os.repIspisAmor_1", "hr.restart.os.repIspisAmortizacija", "IspisAmor_1", "Ispis osnovnih sredstava");
      } else if (!jcbPoOJ.isSelected() && jcbOblikIsp.isSelected() && !jcbSInvBr.isSelected()) {
        System.out.println("repIspisAmor_2");
        if (rbIspAmGr.isSelected()){
          this.addReport("hr.restart.os.repIspisAmor_2", "hr.restart.os.repIspisAmortizacija", "IspisAmor_2_V03", "Ispis osnovnih sredstava");
        } else {
          this.addReport("hr.restart.os.repIspisAmor_2", "hr.restart.os.repIspisAmortizacija", "IspisAmor_2_V02", "Ispis osnovnih sredstava");
        }
      } else if (jcbPoOJ.isSelected() && jcbOblikIsp.isSelected() && !jcbSInvBr.isSelected()) {
        System.out.println("repIspisAmor_3");
        if (rbIspAmGr.isSelected()){
          this.addReport("hr.restart.os.repIspisAmor_3", "hr.restart.os.repIspisAmortizacija", "IspisAmor_3_V02", "Ispis osnovnih sredstava");
        } else {
          this.addReport("hr.restart.os.repIspisAmor_3", "hr.restart.os.repIspisAmortizacija", "IspisAmor_3", "Ispis osnovnih sredstava");
        }
      } else if (!jcbPoOJ.isSelected() && !jcbOblikIsp.isSelected() && jcbSInvBr.isSelected()) {
        System.out.println("repIspisAmor_4");
        this.addReport("hr.restart.os.repIspisAmor_4", "hr.restart.os.repIspisAmortizacija", "IspisAmor_4", "Ispis osnovnih sredstava");
      } else if (jcbPoOJ.isSelected() && !jcbOblikIsp.isSelected() && jcbSInvBr.isSelected()) {
        System.out.println("repIspisAmor_5");
        this.addReport("hr.restart.os.repIspisAmor_5", "hr.restart.os.repIspisAmortizacija", "IspisAmor_5_V02", "Ispis osnovnih sredstava");
      } else if (!jcbPoOJ.isSelected() && jcbOblikIsp.isSelected() && jcbSInvBr.isSelected()) {
        System.out.println("repIspisAmor_6");
        if (rbIspAmGr.isSelected()){
          this.addReport("hr.restart.os.repIspisAmor_6", "hr.restart.os.repIspisAmortizacija", "IspisAmor_6_V02", "Ispis osnovnih sredstava");
        } else {
          this.addReport("hr.restart.os.repIspisAmor_6", "hr.restart.os.repIspisAmortizacija", "IspisAmor_6_V02", "Ispis osnovnih sredstava");
        }
      } else if (jcbPoOJ.isSelected() && jcbOblikIsp.isSelected() && jcbSInvBr.isSelected()) {
        System.out.println("repIspisAmor_7");
        if (rbIspAmGr.isSelected()){
          this.addReport("hr.restart.os.repIspisAmor_7", "hr.restart.os.repIspisAmortizacija", "IspisAmor_7_V02", "Ispis osnovnih sredstava");
        } else {
          this.addReport("hr.restart.os.repIspisAmor_7", "hr.restart.os.repIspisAmortizacija", "IspisAmor_7_V02", "Ispis osnovnih sredstava");
        }
      } else setNoDataAndReturnImmediately();
    } else {
      if(isSInvBrojem()){
        System.out.println("repAmRev");
        this.addReport("hr.restart.os.repAmRev", "hr.restart.os.repIspisAmortizacija", "AmRev", "Likvidacija osnovnih sredstava");
      } else {
        System.out.println("repAmRevTot");
        this.addReport("hr.restart.os.repAmRevTot", "hr.restart.os.repIspisAmortizacija", "AmRevTot", "Likvidacija osnovnih sredstava");
      }
    }


      /*
      switch (OJ+OL+IB) {
        case 0:
          this.addReport("hr.restart.os.repIspisAmor_0","Ispis osnovnih sredstava", 5);
          System.out.println("repIspisAmor_0");
          break;
        case 1:
          this.addReport("hr.restart.os.repIspisAmor_1","Ispis osnovnih sredstava", 5);
          System.out.println("repIspisAmor_1");
          break;
        case 2:
          this.addReport("hr.restart.os.repIspisAmor_2","Ispis osnovnih sredstava", 5);
          System.out.println("repIspisAmor_2");
          break;
        case 3:
          this.addReport("hr.restart.os.repIspisAmor_3","Ispis osnovnih sredstava", 5);
          System.out.println("repIspisAmor_3");
          break;
        case 4:
          this.addReport("hr.restart.os.repIspisAmor_4","Ispis osnovnih sredstava", 5);
          System.out.println("repIspisAmor_4");
          break;
        case 5:
          this.addReport("hr.restart.os.repIspisAmor_5","Ispis osnovnih sredstava", 5);
          System.out.println("repIspisAmor_5");
          break;
        case 6:
          this.addReport("hr.restart.os.repIspisAmor_6","Ispis osnovnih sredstava", 5);
          System.out.println("repIspisAmor_6");
          break;
        case 7:
          this.addReport("hr.restart.os.repIspisAmor_7","Ispis osnovnih sredstava", 5);
          System.out.println("repIspisAmor_7");
          break;
      }
    } else {
      if(jcbInvBr.isSelected()){
        this.addReport("hr.restart.os.repAmRev", "Likvidacija osnovnih sredstava", 5);
        System.out.println("repAmRev");
      } else {
        this.addReport("hr.restart.os.repAmRevTot", "Likvidacija osnovnih sredstava", 5);
        System.out.println("repAmRevTot");
      }
    }*/
  }

  private void output(){
    System.out.println("*****************************");
    System.out.println("getOgrJed()      " + getOgrJed());
    System.out.println("getVrstaIspisa() " + getVrstaIspisa());
    System.out.println("getOblikIspisa() " + getOblikIspisa());
    System.out.println("isPripOrgJed()   " + isPripOrgJed());
    System.out.println("isPoOrgJed()     " + isPoOrgJed());
    System.out.println("isOblikIsp()     " + isOblikIsp());
    System.out.println("isSInvBrojem()   " + isSInvBrojem());
    System.out.println("*****************************");
    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
    try {
      syst.prn(ispisDataSet);
    }
    catch (Exception ex) {
      System.out.println("ispisDataSet      - NULL");
    }
  }
}
