/****license*****************************************************************
**   file: ispOS_NextGeneration.java
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
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCorg;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.Aus;
import hr.restart.util.raComboBox;
import hr.restart.util.raUpitLite;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.math.BigDecimal;
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

public class ispOS_NextGeneration extends raUpitLite {
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.zapod.OrgStr knjOrgStr = hr.restart.zapod.OrgStr.getOrgStr();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();

  QueryDataSet qds;
  QueryDataSet defQDS = new QueryDataSet();

  StorageDataSet fake = new StorageDataSet();
  StorageDataSet knjSDS = new StorageDataSet();
  StorageDataSet statusDS = new StorageDataSet();

//  TableDataSet tds = new TableDataSet();

  String currKnj="";

  Timestamp oldValue;

  double[] sume;

  boolean okpressed = false;

  jpCorg jpC = new jpCorg(100,275,true);

  JPanel jp = new JPanel();
  JPanel jpVrstaIspisa = new JPanel();
  JPanel jpOblikIspisa = new JPanel();
  JPanel jpOrgJedinica = new JPanel();
  JPanel jpJcbOblikIspisa = new JPanel();

  XYLayout xYLayout1 = new XYLayout();
  XYLayout xYLayout2 = new XYLayout();
  XYLayout xYLayout3 = new XYLayout();
  XYLayout xYLayout4 = new XYLayout();
  XYLayout xyLayout5 = new XYLayout();

  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  TitledBorder titledBorder3;

//  Border border1;

  JraRadioButton jrbTrStanje = new JraRadioButton();
  JraRadioButton jrbStNaDan = new JraRadioButton();
  JraRadioButton jrbPocStanje = new JraRadioButton();
  JraRadioButton jrbIspKont = new JraRadioButton();
  JraRadioButton jrbIspAmGr = new JraRadioButton();
  JraRadioButton jrbLokacije = new JraRadioButton();
  JraRadioButton jrbIspRevSk = new JraRadioButton();
  JraRadioButton jrbIspAtrikl = new JraRadioButton();

  raButtonGroup rbgVrsta = new raButtonGroup();
  raButtonGroup rbgOblik = new raButtonGroup();

  JraTextField jtfStNaDan = new JraTextField();
  JraTextField jrfGodProizPoc = new JraTextField();
  JraTextField jrfGodProizZav = new JraTextField();

  JraCheckBox jcbInvBr = new JraCheckBox();
  JraCheckBox jcbOrgJed = new JraCheckBox();
  JraCheckBox jcbOblikListe = new JraCheckBox();
//  JraCheckBox jcbPripOrgJed = new JraCheckBox();

  JLabel jlStatus = new JLabel();
  JLabel jlPorijeklo = new JLabel();
  JLabel jlGodProiz = new JLabel();
  JLabel jlAktivnost = new JLabel();
//  JLabel jlDohvat = new JLabel();
  JLabel jlDo = new JLabel();

//  JlrNavField jrfCOrgNaz = new JlrNavField();
//  JlrNavField jrfCOrg = new JlrNavField();

  JraButton jbCOrg = new JraButton();

  raComboBox rcbStatus = new raComboBox();
  raComboBox rcbPorijeklo = new raComboBox();
  raComboBox rcbAktivnost = new raComboBox();

  static ispOS_NextGeneration iong;

  public ispOS_NextGeneration() {
    try {
      initializer();
      iong = this;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static ispOS_NextGeneration getInstance(){
    if (iong == null) iong = new ispOS_NextGeneration();
    return iong;
  }

  public void componentShow() {
    oldValue=null;
    rcc.setLabelLaF(jtfStNaDan, false);
    fake.setString("CORG",hr.restart.zapod.OrgStr.getKNJCORG());
    jpC.setCorg(hr.restart.zapod.OrgStr.getKNJCORG());//this.jrfCOrg.forceFocLost();
    showDefaultValues();
  }

  public void showDefaultValues() {
//    if(!tds.isOpen()) tds.open();
    if(!fake.isOpen()) fake.open();
    if(!statusDS.isOpen()) statusDS.open();
    if(oldValue==null) {      
      currKnj = knjOrgStr.getKNJCORG();
      String qStr = rdOSUtil.getUtil().getDefaultRekap(currKnj);
      Aus.refilter(defQDS, qStr);      
      defQDS.first();

      Timestamp  tsCurrY;

      String currY = vl.findYear(defQDS.getTimestamp("DATUM"));
      String trenGod = vl.findYear();

     if(rdOSUtil.getUtil().StrToInt(currY)!=rdOSUtil.getUtil().StrToInt(trenGod)) {
       tsCurrY = hr.restart.util.Util.getUtil().getLastDayOfYear(defQDS.getTimestamp("DATUM"));
     } else {
       tsCurrY = vl.findDate(false, 0);
    }
      fake.setTimestamp("datum", tsCurrY);
    }
    reset();
  }

  public boolean Validacija(){
    if(vl.isEmpty(jpC.corg)) return false;
/*    if(jrbStNaDan.isSelected() && !vl.findYear(defQDS.getTimestamp("DATUM")).equals(vl.findYear(tds.getTimestamp("datum")))) {
      jtfStNaDan.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Pogrešan datum !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }*/
    if(!jcbOrgJed.isSelected() && !jcbOblikListe.isSelected() && !jcbInvBr.isSelected()) {
      EnabDisabOrg(0);
      jcbOrgJed.requestFocus();
      JOptionPane.showConfirmDialog(this.getJPan(),"Obavezno odabrati vrstu ispisa !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  public void okPress() {
    /** @todo prouciti i ispraviti slucaj kad nakon ulaska u ispSI_NextGeneration nestanu ispisi na ovom ekranu */
    okpressed = true;
    if (preparedQueryDataSet().rowCount() != 0) {
      killAllReports();
      if(!jcbOrgJed.isSelected() && !jcbOblikListe.isSelected() && !jcbInvBr.isSelected()) {
        System.out.println("\nOS_0\n");
        this.addReport("hr.restart.os.repIspOS","hr.restart.os.repIspisOS_0","repIspisOS_0","Ispis osnovnih sredstava");
      }
      else if(jcbOrgJed.isSelected() && !jcbOblikListe.isSelected() && !jcbInvBr.isSelected()) {
        System.out.println("\nOS_1\n");
        this.addReport("hr.restart.os.repIspOS_01","hr.restart.os.repIspOS","IspOS_01","Ispis osnovnih sredstava IspOS01 NEW");
//        this.addReport("hr.restart.os.repIspisOS_1","hr.restart.os.repIspOS","IspisOS_1","Ispis osnovnih sredstava repIspisOS_1 OLD");
      }
      else if(!jcbOrgJed.isSelected() && jcbOblikListe.isSelected() && !jcbInvBr.isSelected()) {
        System.out.println("\nOS_2\n");
        this.addReport("hr.restart.os.repIspOS2","hr.restart.os.repIspOS","IspOS","Ispis osnovnih sredstava NEW");
//        this.addReport("hr.restart.os.repIspisOS_2","hr.restart.os.repIspOS","IspisOS_2","Ispis osnovnih sredstava repIspisOS_2 OLD");
//        this.addReport("hr.restart.os.repIspisOS_2","Ispis osnovnih sredstava", 5);
      }
      else if(jcbOrgJed.isSelected() && jcbOblikListe.isSelected() && !jcbInvBr.isSelected()) {
        System.out.println("\nOS\n");
        this.addReport("hr.restart.os.repIspOS","hr.restart.os.repIspOS","IspOS","Ispis osnovnih sredstava NEW");
//        this.addReport("hr.restart.os.repIspisOS","hr.restart.os.repIspOS","IspisOS","Ispis osnovnih sredstava repIspisOS OLD");
//        this.addReport("hr.restart.os.repIspisOS","Ispis osnovnih sredstava", 5);
      }
      else if(!jcbOrgJed.isSelected() && !jcbOblikListe.isSelected() && jcbInvBr.isSelected()) {
        System.out.println("\nOS_4\n");
        this.addReport("hr.restart.os.repIspOS4","hr.restart.os.repIspOS","IspOS_04","Ispis osnovnih sredstava NEW");
//        this.addReport("hr.restart.os.repIspisOS_4","hr.restart.os.repIspOS","IspisOS_4","Ispis osnovnih sredstava repIspisOS_4 OLD");
//      this.addReport("hr.restart.os.repIspisOS_4","Ispis osnovnih sredstava", 5);
      }
      else if(jcbOrgJed.isSelected() && !jcbOblikListe.isSelected() && jcbInvBr.isSelected()) {
        System.out.println("\nOS_5\n");
        this.addReport("hr.restart.os.repIspOS5","hr.restart.os.repIspOS","IspOS_05","Ispis osnovnih sredstava NEW");
//        this.addReport("hr.restart.os.repIspisOS_5","hr.restart.os.repIspOS","IspisOS_5","Ispis osnovnih sredstava repIspisOS_5 OLD");
//        this.addReport("hr.restart.os.repIspisOS_5","Ispis osnovnih sredstava", 5);
      }
      else if(!jcbOrgJed.isSelected() && jcbOblikListe.isSelected() && jcbInvBr.isSelected()) {
        System.out.println("\nOS_6\n");
        this.addReport("hr.restart.os.repIspOS1","hr.restart.os.repIspOS","IspOS_06","Ispis osnovnih sredstava NEW");
//        this.addReport("hr.restart.os.repIspisOS_6","hr.restart.os.repIspOS","IspisOS_6","Ispis osnovnih sredstava repIspisOS_6 OLD");
//        this.addReport("hr.restart.os.repIspisOS_6","Ispis osnovnih sredstava", 5);
      }
      else if(jcbOrgJed.isSelected() && jcbOblikListe.isSelected() && jcbInvBr.isSelected()) {
        System.out.println("\nInvIspOS\n");
        this.addReport("hr.restart.os.repIspOS1","hr.restart.os.repIspOS","InvIspOS","Ispis osnovnih sredstava NEW");
//        this.addReport("hr.restart.os.repInvIspOS","hr.restart.os.repIspOS","InvIspOS","Ispis osnovnih sredstava repInvIspOS OLD");
//        this.addReport("hr.restart.os.repInvIspOS","Ispis osnovnih sredstava", 5);
      }
    } else {
      setNoDataAndReturnImmediately();
    }
  }

  public QueryDataSet preparedQueryDataSet() {
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(statusDS);
    qds = new QueryDataSet();
    String qStr ="";
    if (jrbPocStanje.isSelected()) { // Pocetno sranje
      qStr = rdOSUtil.getUtil().getPST_OSIspisV2(fake.getString("CORG"),
                                               jcbOrgJed.isSelected(),
                                               jcbOblikListe.isSelected(),
                                               jcbInvBr.isSelected(),
                                               jpC.isRecursive(), // jcbPripOrgJed.isSelected(),
                                               statusDS.getString("STATUS"),
                                               fake.getString("GPP"),
                                               fake.getString("GPZ"),
                                               statusDS.getString("PORIJEKLO"),
                                               statusDS.getString("AKTIV"),
                                               getOblikIspisa());
    } else if (jrbStNaDan.isSelected()) { // Stanje na dan
      qStr = rdOSUtil.getUtil().getSND_OSIspisV2(fake.getString("CORG"),
                                               getPocDatum(),
                                               util.getTimestampValue(fake.getTimestamp("datum"),1),
                                               jcbOrgJed.isSelected(),
                                               jcbOblikListe.isSelected(),
                                               jcbInvBr.isSelected(),
                                               jpC.isRecursive(), //jcbPripOrgJed.isSelected(),
                                               statusDS.getString("STATUS"),
                                               fake.getString("GPP"),
                                               fake.getString("GPZ"),
                                               statusDS.getString("PORIJEKLO"),
                                               statusDS.getString("AKTIV"),
                                               getOblikIspisa());
    } else if (jrbTrStanje.isSelected()) { //  Trenutno stanje
      qStr = rdOSUtil.getUtil().getTST_OSIspisV2(fake.getString("CORG"),
                                               jcbOrgJed.isSelected(),
                                               jcbOblikListe.isSelected(),
                                               jcbInvBr.isSelected(),
                                               jpC.isRecursive(), //jcbPripOrgJed.isSelected(),
                                               statusDS.getString("STATUS"),
                                               fake.getString("GPP"),
                                               fake.getString("GPZ"),
                                               statusDS.getString("PORIJEKLO"),
                                               statusDS.getString("AKTIV"),
                                               getOblikIspisa());
    }
    System.out.println("qstr - \n"+qStr+"\n");
    Aus.refilter(qds, qStr);    
    qds.getColumn("CORG").setRowId(true);
    qds.first();
    for(int j=0;j<qds.getRowCount();j++) {
      if (qds.getString("CORG").equals("")) {
         qds.deleteRow();
      }
      qds.next();
    }
    if(qds.getRowCount()==0) return qds;

    if((!jcbOrgJed.isSelected() && jpC.isRecursive() /*jcbPripOrgJed.isSelected()*/)) {
      qds.first();
      for(int j=0;j<qds.getRowCount();j++) {
        qds.setString("CORG", jpC.getCorg()); //this.jrfCOrg.getText());
      }
      qds.next();
    }

    BigDecimal osnSum = new BigDecimal(0);
    BigDecimal ispSum = new BigDecimal(0);
    BigDecimal osn_ispSum = new BigDecimal(0);

    qds.open();
    qds.first();
    do {
      if (jrbPocStanje.isSelected()) {                                        // pocetno st
        osnSum=osnSum.add(qds.getBigDecimal("OSNPOCETAK"));
        ispSum=ispSum.add(qds.getBigDecimal("ISPPOCETAK"));
      } else if (jrbTrStanje.isSelected()){ //jrbStNaDan.isSelected()) {      // tr stanje
        qds.setBigDecimal("OSNDUGUJE", qds.getBigDecimal("OSNDUGUJE").add(qds.getBigDecimal("OSNPOCETAK")));
        qds.setBigDecimal("ISPPOTRAZUJE", qds.getBigDecimal("ISPPOTRAZUJE").add(qds.getBigDecimal("ISPPOCETAK")));
        osnSum=osnSum.add(qds.getBigDecimal("OSNDUGUJE").subtract(qds.getBigDecimal("OSNPOTRAZUJE")));
        ispSum=ispSum.add(qds.getBigDecimal("ISPPOTRAZUJE").subtract(qds.getBigDecimal("ISPDUGUJE")));
      } else if (jrbStNaDan.isSelected()){ //jrbTrStanje.isSelected()) {      // st na dan
//        System.out.println("trenutno stanje");
        osnSum=osnSum.add(qds.getBigDecimal("OSNDUGUJE").subtract(qds.getBigDecimal("OSNPOTRAZUJE")));
        ispSum=ispSum.add(qds.getBigDecimal("ISPPOTRAZUJE").subtract(qds.getBigDecimal("ISPDUGUJE")));
//        System.out.println("osnSum - " + osnSum);
//        System.out.println("ispSum - " + ispSum);
      }
      qds.next();
    } while(qds.inBounds());
    osn_ispSum = osn_ispSum.add(osnSum.add(ispSum.negate()));
    sume = new double[] {osnSum.doubleValue(), ispSum.doubleValue(), osn_ispSum.doubleValue()};
    return qds;
  }

  public int getOblikIspisa(){
    if (jrbIspKont.isSelected()) return 0;
    else if (jrbIspAmGr.isSelected()) return 1;
    else if (jrbLokacije.isSelected()) return 2;
    else if (jrbIspRevSk.isSelected()) return 3;
    else if (jrbIspAtrikl.isSelected()) return 4;
    return -1;
  }

  public void firstESC() {
    if (okpressed){
      rcc.EnabDisabAll(this.getJPan(),true);
      setPrevious();
    } else {
      reset();
      fake.setString("CORG","");
      jpC.corg.forceFocLost(); //jrfCOrg.forceFocLost();
    }
    jpC.corg.requestFocus();
    okpressed = false;
  }

  public boolean runFirstESC() {
    if (okpressed) {
      return true;
    } else if (!fake.getString("CORG").equals("")) {
      return true;
    }
    return false;
  }

  private void initializer() throws Exception {
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(/*Color.white,new Color(148, 145, 140)*/),"Vrsta ispisa");
    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(/*Color.white,new Color(148, 145, 140)*/));//,"Oblik ispisa");
    titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(/*Color.white,new Color(148, 145, 140)*/),"Org. jedinica");
//    border1 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));

    //XYLayout setting
    xYLayout1.setWidth(480);
    xYLayout1.setHeight(430);
    xYLayout3.setWidth(280);
    xYLayout3.setHeight(120);

    xyLayout5.setWidth(50);
    xyLayout5.setHeight(10);

    //setting panelz
    jp.setLayout(xYLayout1);
    jpVrstaIspisa.setBorder(titledBorder1);
    jpVrstaIspisa.setLayout(xYLayout3);
    jpOblikIspisa.setBorder(titledBorder2);
    jpOblikIspisa.setLayout(xYLayout2);
    jpOrgJedinica.setLayout(xYLayout4);
    jpOrgJedinica.setBorder(titledBorder3);

    jpJcbOblikIspisa.setLayout(xyLayout5);

    rbgVrsta.add(jrbPocStanje,"Poèetno stanje");
    rbgVrsta.add(jrbStNaDan,"Stanje na dan");
    rbgVrsta.add(jrbTrStanje,"Trenutno stanje");
    rbgVrsta.setHorizontalTextPosition(SwingConstants.TRAILING);
    jrbStNaDan.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent i) {
        jrbStNaDan_itemStateChanged(i);
      }
    });

    rbgOblik.add(jrbIspKont,"Ispis po kontima");
    rbgOblik.add(jrbIspAmGr,"Ispis po amortizacijskim grupama");
    rbgOblik.add(jrbLokacije,"Ispis po lokacijama");
    rbgOblik.add(jrbIspRevSk,"Ispis po revalorizacijskim skupinama");
    rbgOblik.add(jrbIspAtrikl,"Ispis po artiklima");
    rbgOblik.setHorizontalTextPosition(SwingConstants.TRAILING);

//    jcbPripOrgJed.setText("Ispis pripadajuæih org. jedinca");
    jcbOrgJed.setText("Ispis po org. jedinici");
    jcbInvBr.setText("Ispis s inventarskim brojem");
    jcbOblikListe.setText("Oblik ispisa");
//    jcbOblikListe.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbOblikListe.addItemListener(new java.awt.event.ItemListener(){
      public void itemStateChanged(ItemEvent i){
        jcbOblikListe_itemStateChanged(i);
      }
    });

//    tds.setColumns(new Column[] {
//      dm.createTimestampColumn("datum")
//    });

    setFieldAsociations();
    setCheckBoxes();
    setDataSets();

    jtfStNaDan.setColumnName("datum");
    jtfStNaDan.setDataSet(fake);
    jtfStNaDan.setHorizontalAlignment(SwingConstants.CENTER);

    jrfGodProizPoc.setHorizontalAlignment(SwingConstants.RIGHT);
    jrfGodProizZav.setHorizontalAlignment(SwingConstants.RIGHT);

    jlStatus.setText("Status");
    jlPorijeklo.setText("Porijeklo");
    jlGodProiz.setText("God. pr.");
    jlAktivnost.setText("Aktivnost");
//    jlDohvat.setText("Dohvat");
    jlDo.setText("-");

    jp.setMinimumSize(new Dimension(478, 450));
    jp.setPreferredSize(new Dimension(478, 450));

    this.setJPan(jp);

    //adding components

    jpJcbOblikIspisa.add(jcbOblikListe,         new XYConstraints(5, 0, -1, -1));
    jp.add(jpJcbOblikIspisa,         new XYConstraints(31, 235, 94, 25));

    jpVrstaIspisa.add(jrbPocStanje, new XYConstraints(8, 10, -1, -1));
    jpVrstaIspisa.add(jrbStNaDan, new XYConstraints(8, 35, -1, -1));
    jpVrstaIspisa.add(jrbTrStanje, new XYConstraints(8, 60, -1, -1));
    jpVrstaIspisa.add(jtfStNaDan,   new XYConstraints(130, 35, 100, -1));

    jpOblikIspisa.add(jrbIspKont, new XYConstraints(8, 15, -1, -1));
    jpOblikIspisa.add(jrbIspAmGr, new XYConstraints(8, 40, -1, -1));
    jpOblikIspisa.add(jrbLokacije, new XYConstraints(8, 65, -1, -1));
    jpOblikIspisa.add(jrbIspRevSk, new XYConstraints(8, 90, -1, -1));
    jpOblikIspisa.add(jrbIspAtrikl, new XYConstraints(8, 115, -1, -1));

//    jpOrgJedinica.add(jrfCOrg,   new XYConstraints(15, 7, 100, -1));
//    jpOrgJedinica.add(jrfCOrgNaz,     new XYConstraints(120, 7, 275, -1));
//    jpOrgJedinica.add(jbCOrg,      new XYConstraints(401, 7, 21, 21));

    jpC.lay.setHeight(30);
    jpC.lay.setWidth(430);
//    jpC.setBackground(Color.yellow);
    jpC.add(jpC.corg, new XYConstraints(15, 0, 100, -1));
    jpC.add(jpC.naziv, new XYConstraints(120, 0, 275, -1));
    jpC.add(jpC.but, new XYConstraints(401,0,21,21));

    jp.add(jpC.rcb, new XYConstraints(345, 104, 120, -1));
    jp.add(new JLabel("Dohvat"),new XYConstraints(280, 104, -1, -1));

    jpOrgJedinica.add(jpC,   new XYConstraints(0, 7, -1, -1));

    jp.add(rcbStatus,     new XYConstraints(345, 129, 120, -1));

    jp.add(rcbPorijeklo,      new XYConstraints(345, 154, 120, -1));
    jp.add(jlPorijeklo,  new XYConstraints(280, 154, -1, -1));
    jp.add(rcbAktivnost,     new XYConstraints(345, 179, 120, -1));
    jp.add(jlAktivnost,   new XYConstraints(280, 179, -1, -1));
//    jp.add(jcbPripOrgJed,    new XYConstraints(36, 103, -1, -1));

    jp.add(jcbInvBr,       new XYConstraints(280, 415, -1, -1));
    jp.add(jcbOrgJed,       new XYConstraints(36, 415, -1, -1));

    jp.add(jlStatus,     new XYConstraints(280, 129, -1, -1));
    jp.add(jlGodProiz,    new XYConstraints(280, 204, -1, -1));
    jp.add(jrfGodProizPoc,       new XYConstraints(345, 204, 50, -1));
    jp.add(jlDo,    new XYConstraints(402, 206, -1, -1));
    jp.add(jrfGodProizZav,   new XYConstraints(413, 204, 50, -1));

    jp.add(jpOrgJedinica, new XYConstraints(15, 20, 450, 70));
    jp.add(jpOblikIspisa,             new XYConstraints(15, 246, 450, 162));  // --> y od jcbOblikIspisa + 11
    jp.add(jpVrstaIspisa,   new XYConstraints(15, 95, 260, 131));


//    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
//      public void knjigChanged(String oldKnjig, String newKnjig) {
//        setFieldAsociations();
//      };
//    });
  }

  private void setDataSets() {
    fake.setColumns(new Column[] {
      dm.createStringColumn("CORG","Organizacijska jedinica",10),
      dm.createStringColumn("GPP", 10),
      dm.createStringColumn("GPZ", 10),
      dm.createTimestampColumn("datum")
    });

    statusDS.setColumns(new Column[] {
      dm.createStringColumn("STATUS",2),
      dm.createStringColumn("PORIJEKLO",2),
      dm.createStringColumn("AKTIV",2)
    });
  }

  public void setCheckBoxes() {
    rcbStatus.setRaColumn("STATUS");
    rcbStatus.setRaDataSet(statusDS);
    rcbStatus.setRaItems(new String[][] {
      {"Sva OS","S"},
      {"OS u pripremi","P"},
      {"OS u upotrebi","A"}
    });

    rcbAktivnost.setRaColumn("AKTIV");
    rcbAktivnost.setRaDataSet(statusDS);
    rcbAktivnost.setRaItems(new String[][] {
      {"Sva OS",""},
      {"Aktivna OS","D"},
      {"Neaktivna OS","N"}
    });

    rcbPorijeklo.setRaColumn("PORIJEKLO");
    rcbPorijeklo.setRaDataSet(statusDS);
    rcbPorijeklo.setRaItems(new String[][] {
      {"Sva porijekla",""},
      {"Tuzemstvo","1"},
      {"Inozemstvo","2"},
      {"Vrijednosnice","3"}
    });
  }

  private void setFieldAsociations() {
    jrfGodProizPoc.setDataSet(fake);
    jrfGodProizPoc.setColumnName("GPP");

    jrfGodProizZav.setDataSet(fake);
    jrfGodProizZav.setColumnName("GPZ");

    jpC.bind(fake);

//    jrfCOrg.setDataSet(fake);
//    jrfCOrg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
//    jrfCOrg.setVisCols(new int[]{0,1});
//    jrfCOrg.setColNames(new String[] {"NAZIV"});
//    jrfCOrg.setTextFields(new javax.swing.text.JTextComponent[] {jrfCOrgNaz});
//    jrfCOrg.setColumnName("CORG");
//    jrfCOrg.setSearchMode(0);
//    jrfCOrg.setNavButton(jbCOrg);

//    jrfCOrgNaz.setColumnName("NAZIV");
//    jrfCOrgNaz.setNavProperties(jrfCOrg);
  }

  private void setPrevious(){
    rcc.EnabDisabAll(jpOblikIspisa, jcbOblikListe.isSelected());
    rcc.setLabelLaF(this.jtfStNaDan, jrbStNaDan.isSelected());
  }

  private void reset() {
    fake.setString("GPP","1900");
    fake.setString("GPZ", hr.restart.util.Valid.getValid().findYear());

    rbgVrsta.setSelected(jrbPocStanje);
    rbgOblik.setSelected(jrbIspKont);
    jcbOrgJed.setSelected(true);
    jcbOblikListe.setSelected(false);
    jcbInvBr.setSelected(false);
    jpC.rcb.setSelectedIndex(0);

//    jcbPripOrgJed.setSelected(false);

    rcc.EnabDisabAll(jpOblikIspisa, false);
    rcbStatus.setSelectedIndex(0);
    statusDS.setString("STATUS","S");
    rcbAktivnost.setSelectedIndex(0);
    statusDS.setString("AKTIV","");
    rcbPorijeklo.setSelectedIndex(0);
    statusDS.setString("PORIJEKLO","");
    jpC.corg.requestFocus();//jrfCOrg.requestFocus();
    jpC.corg.selectAll();//jrfCOrg.selectAll();
  }

  public void EnabDisabOrg(int i) {
//    if (i==0) {
      rcc.EnabDisabAll(jpC,(i==0));
//      rcc.setLabelLaF(jrfCOrg, true);
//      rcc.setLabelLaF(jrfCOrgNaz, true);
//      rcc.setLabelLaF(jbCOrg, true);
//    } else {
//      jcbPripOrgJed.requestFocus();
//      rcc.setLabelLaF(jrfCOrg, false);
//      rcc.setLabelLaF(jrfCOrgNaz, false);
//      rcc.setLabelLaF(jbCOrg, false);
//    }
  }

  void jrbStNaDan_itemStateChanged(ItemEvent e) {
    rcc.setLabelLaF(this.jtfStNaDan, jrbStNaDan.isSelected());
  }

  void jcbOblikListe_itemStateChanged(ItemEvent e) {
    rcc.EnabDisabAll(jpOblikIspisa, jcbOblikListe.isSelected());
  }

  public boolean isIspis(){
    return false;
  }

  public boolean ispisNow(){
    return true;
  }

  public double[] getSume(){
    return sume;
  }

  public String getPocDatum() {
    Integer tempYear = new Integer((fake.getTimestamp("datum").toString()).substring(0,4));
    int year = tempYear.intValue();
    return ((year-1)+"-12-31 00:00:00.0");
  }

  public Timestamp getNaDan(){
    return fake.getTimestamp("datum");
  }

  public String getStatus(){
    return statusDS.getString("STATUS");
  }

  public String getAktivnost(){
    return statusDS.getString("AKTIV");
  }

  public String getPorjeklo(){
    return statusDS.getString("PORIJEKLO");
  }

  public QueryDataSet getQdsIspis(){
    return qds;
  }

  public boolean getPoOrgJedinici(){
    return jcbOrgJed.isSelected();
  }

  public boolean getPripadnostOrgJedinici(){
    return jpC.isRecursive(); //jcbPripOrgJed.isSelected();
  }

  public boolean getOblikListe(){
    return jcbOblikListe.isSelected();
  }

  public boolean getPocetnoStanje(){
    return jrbPocStanje.isSelected();
  }

  public boolean getTrenutnoStanje(){
    return jrbTrStanje.isSelected();
  }

  public boolean getStanjeNaDan(){
    return jrbStNaDan.isSelected();
  }

}
