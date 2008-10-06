/****license*****************************************************************
**   file: ispRekap.java
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
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raIspisDialog;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

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

public class ispRekap extends raIspisDialog {
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.zapod.OrgStr knjOrgStr = hr.restart.zapod.OrgStr.getOrgStr();
  Valid vl = Valid.getValid();
  String currKnj="";

  public static double [] sume;
  public static String datumOd="";
  public static String datumDo="";
  public static int selectedRB = 0;
  public int OJ =1;
  public int KO =0;
  public int IB =0;
  Border border1;

  hr.restart.robno._Main main;
  QueryDataSet defQDS = new QueryDataSet();
  StorageDataSet fake = new StorageDataSet();
  TableDataSet tds = new TableDataSet();

//  TitledBorder titledBorder1;
//  TitledBorder titledBorder2;

  Column column1 = new Column();
  Column column2 = new Column();
  Column temp = new Column();
  Column temp1 = new Column();

  XYLayout xYLayout1 = new XYLayout();
  XYLayout xYLayout3 = new XYLayout();
  JLabel jLabel1 = new JLabel();
  JPanel jp = new JPanel();
  JPanel jPanel2 = new JPanel();
  JLabel jlKonto = new JLabel();
  JLabel jlVrIspisa = new JLabel();
  JLabel jlSifra = new JLabel();
  JLabel jlNaziv = new JLabel();
  JLabel jlOrgjJed = new JLabel();
  JraTextField jtfZavDatum = new JraTextField();
  JraTextField jtfPocDatum = new JraTextField();
  JraCheckBox jcbCorg = new JraCheckBox();
  JraCheckBox jcbInvBr = new JraCheckBox();
  JraCheckBox jcbKonto = new JraCheckBox();
  JraCheckBox jcbPoNazivu = new JraCheckBox();
  JraButton jbCOrg = new JraButton();
  JraButton jbKonto = new JraButton();
  JlrNavField jrfKonto = new JlrNavField();
  JlrNavField jrfNazKonto = new JlrNavField();
  JlrNavField jrfCOrg = new JlrNavField();
  JlrNavField jrfCOrgNaz = new JlrNavField();

  public ispRekap() {
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static int getSelectedRB() {
    return selectedRB;
  }

  private void jbInit() throws Exception {
    this.setModal(true);
    border1 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    jp.setLayout(xYLayout1);
    xYLayout1.setWidth(580);
    xYLayout1.setHeight(200);
    this.setJPan(jp);

    jp.setToolTipText("");
    jlOrgjJed.setText("Org jedinica");
    jlSifra.setText("Šifra");
    jlNaziv.setText("Naziv");
    jbCOrg.setText("...");

    jLabel1.setText("Datum (od-do)");

    jPanel2.setLayout(xYLayout3);
    jPanel2.setBorder(BorderFactory.createEtchedBorder());
    xYLayout3.setWidth(280);
    xYLayout3.setHeight(100);
    jcbCorg.setText("po org. jedinici");
//    jcbCorg.addFocusListener(new java.awt.event.FocusAdapter() {
//      public void focusGained(FocusEvent e) {
//        jcbCorg_focusGained(e);
//      }
//    });
    jcbCorg.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbCorg_actionPerformed(e);
      }
    });

    jcbCorg.setSelected(true);
    jcbInvBr.setText("po inv. broju");
    jcbInvBr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbInvBr_actionPerformed(e);
      }
    });
    jcbKonto.setText("po kontu");
    jcbKonto.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbKonto_actionPerformed(e);
      }
    });

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnjig, String newKnjig) {
        bindCorg();
      };
    });
    bindCorg();

    jbKonto.setText("...");
    jrfKonto.setRaDataSet(dm.getKonta());
    jrfKonto.setVisCols(new int[]{0,1});
    jrfKonto.setColNames(new String[] {"NAZIVKONTA"});
    jrfKonto.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazKonto});
    jrfKonto.setColumnName("BROJKONTA");
    jrfKonto.setDataSet(fake);
    jrfNazKonto.setColumnName("NAZIVKONTA");
    jrfNazKonto.setNavProperties(jrfKonto);
    jrfKonto.setSearchMode(3);

    jlKonto.setText("Konto");
    jlVrIspisa.setText("Vrsta ispisa");
    jcbPoNazivu.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbPoNazivu.setText("Ispis s nazivom");
    jp.add(jrfCOrgNaz,      new XYConstraints(255, 45, 275, -1));
    jp.add(jrfCOrg,     new XYConstraints(150, 45, 100, -1));
    jp.add(jlSifra,   new XYConstraints(150, 20, -1, -1));
    jp.add(jlNaziv,   new XYConstraints(235, 20, -1, -1));
    jp.add(jlOrgjJed,    new XYConstraints(15, 45, -1, -1));
    jp.add(jtfZavDatum,     new XYConstraints(255, 95, 100, -1));
    jp.add(jtfPocDatum,      new XYConstraints(150, 95, 100, -1));
    jp.add(jLabel1,    new XYConstraints(15, 95, -1, -1));
    jp.add(jPanel2,                  new XYConstraints(150, 125, 380, 50));
    jPanel2.add(jcbCorg,   new XYConstraints(10, 10, -1, -1));
    jPanel2.add(jcbInvBr,     new XYConstraints(280, 10, -1, -1));
    jPanel2.add(jcbKonto,   new XYConstraints(150, 10, -1, -1));
    jp.add(jbKonto,   new XYConstraints(533, 70, 21, 21));
    jp.add(jrfNazKonto,     new XYConstraints(255, 70, 275, -1));
    jp.add(jlKonto,   new XYConstraints(15, 70, -1, -1));
    jp.add(jlVrIspisa,    new XYConstraints(15, 125, -1, -1));
    jp.add(jbCOrg,   new XYConstraints(533, 45, 21, 21));
    jp.add(jrfKonto,  new XYConstraints(150, 70, 100, -1));
    jp.add(jcbPoNazivu,    new XYConstraints(427, 95, -1, -1));

//    column2.setCaption("zavDatum");
//    column2.setColumnName("zavDatum");
//    column2.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
//    column2.setDisplayMask("dd-MM-yyyy");
//    column2.setResolvable(false);
//    column2.setSqlType(0);
//    column2.setServerColumnName("NewColumn2");

//    column1.setCaption("pocDatum");
//    column1.setColumnName("pocDatum");
//    column1.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
//    column1.setDisplayMask("dd-MM-yyyy");
//    column1.setResolvable(false);
//    column1.setSqlType(0);
//    column1.setServerColumnName("NewColumn1");

//    tds.setColumns(new Column[] {column1, column2});

    //**** binding
//    temp.setColumnName("CORG");
//    temp.setDataType(com.borland.dx.dataset.Variant.STRING);
//    fake.setColumns(new Column[] {temp,temp1});
//    jrfCOrg.setDataSet(fake);
//    jrfCOrg.setRaDataSet(rdOSUtil.getUtil().getOrgStruktura(hr.restart.zapod.OrgStr.getKNJCORG()));
//    jrfCOrg.setVisCols(new int[]{0,1});
//    jrfCOrg.setColNames(new String[] {"NAZIV"});
//    jrfCOrg.setTextFields(new javax.swing.text.JTextComponent[] {jrfCOrgNaz});
//    jrfCOrg.setColumnName("CORG");
//    jrfCOrg.setSearchMode(0);
//    jrfCOrgNaz.setColumnName("NAZIV");
//    jrfCOrgNaz.setNavProperties(jrfCOrg);
//    jrfCOrg.setNavButton(this.jbCOrg);

    temp.setColumnName("CORG");
    temp.setDataType(com.borland.dx.dataset.Variant.STRING);
    fake.setColumns(new Column[] {temp,temp1});

    temp1.setColumnName("BROJKONTA");
    temp1.setDataType(com.borland.dx.dataset.Variant.STRING);
    jrfKonto.setRaDataSet(hr.restart.zapod.raKonta.getAnalitickaKonta());
//    jrfKonto.setRaDataSet(dm.getKonta());
    jrfKonto.setVisCols(new int[]{0,1});
    jrfKonto.setColNames(new String[] {"NAZIVKONTA"});
    jrfKonto.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazKonto});
    jrfKonto.setColumnName("BROJKONTA");
    jrfNazKonto.setColumnName("NAZIVKONTA");
    jrfNazKonto.setNavProperties(jrfKonto);
    jrfKonto.setNavButton(this.jbKonto);

    tds.setColumns(new Column[] {dM.createTimestampColumn("pocDatum"), dM.createTimestampColumn("zavDatum")});
    jtfZavDatum.setDataSet(tds);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setText("jraTextField2");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfPocDatum.setDataSet(tds);
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setText("jraTextField2");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
//    new raDateRange(jtfPocDatum, jtfZavDatum);
//    rcc.setLabelLaF(jtfPocDatum, false);
  }

  public void componentShow() {
    showDefaultValues();
    fieldControl();
  }

  public void showDefaultValues() {
    if(!tds.isOpen())
      tds.open();    
    currKnj = knjOrgStr.getKNJCORG();
    String qStr = rdOSUtil.getUtil().getDefaultRekap(currKnj);
    Aus.refilter(defQDS, qStr);
    
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

    tds.setTimestamp("pocDatum", lastY);
    tds.setTimestamp("zavDatum", tsCurrY);
    jrfCOrg.setText(currKnj);
    jrfCOrg.forceFocLost();

    jcbPoNazivu.setEnabled(false);
    SwingUtilities.invokeLater(new Runnable() {
     public void run() {
       jrfCOrg.requestFocus();
     }
    });
  }

  public boolean runFirstESC() {
    if(!jrfCOrg.getText().equals("") || !jrfKonto.getText().equals("")) {
      return true;
    }
    return false;
  }

  public void firstESC() {
    if(!jrfKonto.getText().equals("") ) {
      fake.setString("BROJKONTA", "");
      jrfNazKonto.setText("");
      jcbPoNazivu.setSelected(false);
      jcbPoNazivu.setEnabled(true);
      jrfKonto.requestFocus();
    } else {
      fake.setString("CORG", "");
      jrfCOrgNaz.setText("");
      jrfCOrg.requestFocus();
    }
  }

  public void getDefValues() {
    String qStr = rdOSUtil.getUtil().getDefaultRekap(currKnj);
    Aus.setFilter(defQDS, qStr);    
  }

  public boolean okPress() {
   if(!vl.findYear(defQDS.getTimestamp("DATUM")).equals(vl.findYear(tds.getTimestamp("zavDatum")))) {
     jtfZavDatum.requestFocus();
     JOptionPane.showConfirmDialog(this.jp,"Pogrešan završni datum !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
     return false;
    }

    selectedRB = OJ+KO+IB;
    if(selectedRB==0) {
      JOptionPane.showConfirmDialog(this.jp,"Obavezan odabir vrste ispisa !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }
    int rows = 0;

    qds = prepareIspis();

    try {
      rows = qds.rowCount();
    }
    catch (Exception ex) {
      System.err.println("exception....");
    }

    if (rows == 0) {
      JOptionPane.showConfirmDialog(this.jp,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }

    getRepRunner().clearAllReports();
//    System.out.println("selectedRB " + selectedRB);

    switch (selectedRB) {
      case 1:
        if(this.jcbPoNazivu.isSelected())
          this.addReport("hr.restart.os.repIspisRekap_1","Rekapitulacija", 5);
        else
          this.addReport("hr.restart.os.repIspisRekap_1BN","Rekapitulacija", 5);
        break;
      case 2:
        if(this.jcbPoNazivu.isSelected())
          this.addReport("hr.restart.os.repIspisRekap_2","Rekapitulacija", 5);
        else
          this.addReport("hr.restart.os.repIspisRekap_2BN","Rekapitulacija", 5);
        break;
      case 3:
        if(this.jcbPoNazivu.isSelected())
          this.addReport("hr.restart.os.repIspisRekap_3","Rekapitulacija", 5);
        else
          this.addReport("hr.restart.os.repIspisRekap_3BN","Rekapitulacija", 5);
        break;
      case 4:
        this.addReport("hr.restart.os.repIspisRekap_4","Rekapitulacija", 5);
        break;
      case 5:
        this.addReport("hr.restart.os.repIspisRekap_5","Rekapitulacija", 5);
        break;
      case 6:
        this.addReport("hr.restart.os.repIspisRekap_6","Rekapitulacija", 5);
        break;
      case 7:
        this.addReport("hr.restart.os.repIspisRekap_7","Rekapitulacija", 5);
        break;
    }
    return true;
  }

  //******* report
  public static QueryDataSet qds = new QueryDataSet();
  public QueryDataSet tempQds = new QueryDataSet();


  public static QueryDataSet getQdsIspis() {
    return qds;
  }

//  public static String getPocDat(){
//    return datumOd;
//  }
//
//
//  public static String getZavDat(){
//    return datumDo;
//  }

  public QueryDataSet prepareIspis() {
    String qStr="";
    StorageDataSet knjSDS = new StorageDataSet();
    BigDecimal sum3 = new BigDecimal(0);
    BigDecimal sum4 = new BigDecimal(0);
    BigDecimal sum6 = new BigDecimal(0);
    BigDecimal sum7 = new BigDecimal(0);
    BigDecimal sum8 = new BigDecimal(0);
    BigDecimal amor = new BigDecimal(0);
    double sum5 = 0;
    double sum10 = 0;
    double sum11 = 0;
    double sum12 = 0;
    BigDecimal sum9 = new BigDecimal(0);

    QueryDataSet tempSet = new QueryDataSet();
    QueryDataSet tempPsSet = new QueryDataSet();
    QueryDataSet finalSet = new QueryDataSet();
    QueryDataSet tempDnSet = new QueryDataSet();

//    qds.close();

    if (jrfCOrg.getText().trim().equals(""))
      knjSDS = knjOrgStr.getOrgstrFromCurrKnjig();
    else
      knjSDS = knjOrgStr.getOrgstrAndKnjig(jrfCOrg.getText().trim());
    if(knjSDS.getRowCount()==0)
      knjSDS = knjOrgStr.getOrgstrAndKnjig(jrfCOrg.getText().trim());
    if(knjSDS.getRowCount()==0)
      return null;

    knjSDS.first();
//    String inCorg2 = "'";
    String inCorg = Condition.in("CORG2",knjSDS,"CORG").toString().toLowerCase();

    System.out.println("inCorg " + inCorg);


//    do {
//      inCorg2 += knjSDS.getString("CORG")+"','";
//    } while (knjSDS.next());
//    inCorg2 = inCorg2.substring(0,inCorg2.length()-2);


    String dn = "select invbroj as invbroj, corg2 as corg, osnduguje, osnpotrazuje, (osnduguje - osnpotrazuje) as c1, ispduguje, isppotrazuje, (isppotrazuje - ispduguje) as c2  FROM OS_Promjene where "+
                "os_promjene.datpromjene >='"+ut.getFirstDayOfYear(tds.getTimestamp("pocDatum"))+"' and os_promjene.datpromjene < "+util.getTimestampValue(tds.getTimestamp("pocDatum"),0)+
                " and  os_promjene."+ inCorg +" order by invbroj";

    String ps = "SELECT invbroj as invbroj, brojkonta as brojkonta, corg2 as corg, osnpocetak as osnpocetak, isppocetak as isppocetak FROM OS_Sredstvo where"+
                "(os_sredstvo.aktiv='D' or os_sredstvo.datlikvidacije >= '"+ut.getFirstDayOfYear(tds.getTimestamp("pocDatum"))+"')"+
                "and  os_sredstvo."+ inCorg +" order by invbroj";

    System.out.println("dn string = " + dn);

    int gb = OJ+KO+IB;
//    System.out.println("gb="+gb);
    qStr = rdOSUtil.getUtil().getRekapitulacija(inCorg, util.getTimestampValue(tds.getTimestamp("pocDatum"),0), util.getTimestampValue(tds.getTimestamp("zavDatum"),1), this.jrfKonto.getText().trim(), gb);
    Aus.refilter(tempSet, qStr);    
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(tempSet);

    finalSet.setColumns(tempSet.cloneColumns());
    finalSet.open();

    Aus.refilter(tempPsSet, qStr);    
    tempPsSet.first();
    lookupData ld = lookupData.getlookupData();
    do {
      finalSet.insertRow(false);
      finalSet.setString("CORG",tempPsSet.getString("CORG"));
      finalSet.setString("BROJKONTA",tempPsSet.getString("BROJKONTA"));
      finalSet.setString("INVBROJ",tempPsSet.getString("INVBROJ"));
      finalSet.setBigDecimal("TRI",tempPsSet.getBigDecimal("OSNPOCETAK"));
      finalSet.setBigDecimal("CETIRI",tempPsSet.getBigDecimal("ISPPOCETAK"));
      finalSet.setDouble("PET",(tempPsSet.getBigDecimal("OSNPOCETAK").subtract(tempPsSet.getBigDecimal("ISPPOCETAK"))).doubleValue());
    } while (tempPsSet.next());

    if (!ut.getFirstSecondOfDay(tds.getTimestamp("pocDatum")).equals(ut.getFirstDayOfYear(tds.getTimestamp("pocDatum")))){
      tempDnSet = ut.getNewQueryDataSet(dn);
      finalSet.first();
      do {
        if (ld.raLocate(finalSet,new String[] {"INVBROJ","CORG"},new String[] {tempDnSet.getString("INVBROJ"),tempDnSet.getString("CORG")}))  {
          finalSet.setBigDecimal("TRI",finalSet.getBigDecimal("TRI").add(tempDnSet.getBigDecimal("OSNDUGUJE").subtract(tempDnSet.getBigDecimal("OSNPOTRAZUJE")))); //tempPsSet.getBigDecimal("OSNPOCETAK"));
          finalSet.setBigDecimal("CETIRI",finalSet.getBigDecimal("CETIRI").add(tempDnSet.getBigDecimal("ISPPOTRAZUJE").subtract(tempDnSet.getBigDecimal("ISPDUGUJE"))));//tempPsSet.getBigDecimal("ISPPOCETAK"));
          finalSet.setDouble("PET",(tempPsSet.getBigDecimal("OSNPOCETAK").subtract(tempPsSet.getBigDecimal("ISPPOCETAK"))).doubleValue());
        }
      } while (tempDnSet.next());


//      hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//      syst.prn(finalSet);
    }

    tempSet.first();
    do {
      if (ld.raLocate(finalSet,new String[] {"BROJKONTA","INVBROJ","CORG"},new String[] {tempSet.getString("BROJKONTA"),tempSet.getString("INVBROJ"),tempSet.getString("CORG")})){
        finalSet.setString("CORG",tempSet.getString("CORG"));
        finalSet.setString("BROJKONTA",tempSet.getString("BROJKONTA"));
        finalSet.setBigDecimal("SEST",finalSet.getBigDecimal("SEST").add(tempSet.getBigDecimal("SEST")));
        finalSet.setBigDecimal("SEDAM",finalSet.getBigDecimal("SEDAM").add(tempSet.getBigDecimal("SEDAM")));
        finalSet.setBigDecimal("OSAM",finalSet.getBigDecimal("OSAM").add(tempSet.getBigDecimal("OSAM")));
        if (tempSet.getString("CPROMJENE").equals("999")){
          finalSet.setBigDecimal("DEVET",finalSet.getBigDecimal("DEVET"));
          finalSet.setBigDecimal("AMOR",finalSet.getBigDecimal("AMOR").add(tempSet.getBigDecimal("DEVET")));
        } else {
          finalSet.setBigDecimal("DEVET",finalSet.getBigDecimal("DEVET").add(tempSet.getBigDecimal("DEVET")));
          finalSet.setBigDecimal("AMOR",finalSet.getBigDecimal("AMOR"));
        }
      }
    } while (tempSet.next());

    if((OJ+KO+IB)==1){
      finalSet = rdOSUtil.getUtil().refilterMemTable(finalSet,"CORG");
    }
    else if((OJ+KO+IB)==2 || (OJ+KO+IB)==3){
      finalSet = rdOSUtil.getUtil().refilterMemTable(finalSet,"BROJKONTA");
    }
    else if((OJ+KO+IB)==4){
      finalSet = rdOSUtil.getUtil().refilterMemTable(finalSet,"INVBROJ");
    }
    else if((OJ+KO+IB)==6){
      finalSet = rdOSUtil.getUtil().refilterMemTable2(finalSet,"BROJKONTA");
    }
    else if((OJ+KO+IB)>3){
//      System.out.println("NULIRA AMORTIZACIJU!!!!! ZASTO!!!");
//      finalSet = rdOSUtil.getUtil().replaceAmor_Isp(finalSet);
    }


    finalSet.first();
    do {
      finalSet.setDouble("DESET",(finalSet.getBigDecimal("TRI").doubleValue() + finalSet.getBigDecimal("SEST").doubleValue() - finalSet.getBigDecimal("SEDAM").doubleValue()));
      finalSet.setDouble("JEDANAEST",(finalSet.getBigDecimal("CETIRI").doubleValue() - finalSet.getBigDecimal("OSAM").doubleValue() + finalSet.getBigDecimal("DEVET").doubleValue() + finalSet.getBigDecimal("AMOR").doubleValue()));
      finalSet.setDouble("DVANAEST",(finalSet.getDouble("DESET") - finalSet.getDouble("JEDANAEST")));
    } while (finalSet.next());
    finalSet.first();
    finalSet.setRowId("CORG", true);
    do
    {
      if(finalSet.getString("CORG").equals("") && finalSet.getRowCount()>0)
        finalSet.deleteRow();
      sum3=sum3.add(finalSet.getBigDecimal("TRI"));
      sum4=sum4.add(finalSet.getBigDecimal("CETIRI"));
      sum6=sum6.add(finalSet.getBigDecimal("SEST"));
      sum7=sum7.add(finalSet.getBigDecimal("SEDAM"));
      sum8=sum8.add(finalSet.getBigDecimal("OSAM"));
      sum5=sum5+finalSet.getDouble("PET");
      sum9=sum9.add(finalSet.getBigDecimal("DEVET"));
      amor=amor.add(finalSet.getBigDecimal("AMOR"));
      sum10=sum10+finalSet.getDouble("DESET");
      sum11=sum11+finalSet.getDouble("JEDANAEST");
      sum12=sum12+finalSet.getDouble("DVANAEST");
      finalSet.next();
    }while(finalSet.inBounds());

    sume = new double[] {sum3.doubleValue(), sum4.doubleValue(), sum5, sum6.doubleValue(),
      sum7.doubleValue(), sum8.doubleValue(), sum9.doubleValue(),  sum10, sum11, sum12, amor.doubleValue()};
    datumOd= jtfPocDatum.getText();
    datumDo= jtfZavDatum.getText();

    return finalSet;
  }

  private String getPocDatum() {
    StringBuffer sb = new StringBuffer(util.getTimestampValue(tds.getTimestamp("datum"),0));
    return sb.replace(6, 11, "01-01").toString();
  }

  void jcbCorg_actionPerformed(ActionEvent e) {
    if(jcbCorg.isSelected())
      OJ = 1;
    else
      OJ = 0;
    fieldControl();
  }

  void jcbKonto_actionPerformed(ActionEvent e) {
    if(jcbKonto.isSelected())
      KO = 2;
    else
      KO = 0;
    fieldControl();
  }

  void jcbInvBr_actionPerformed(ActionEvent e) {
    if(jcbInvBr.isSelected())
      IB = 4;
    else
      IB = 0;
   fieldControl();
  }

  void jrfCOrgNaz_focusLost(FocusEvent e) {
    if(jrfCOrg.getText().trim().equals("")){
      jrfCOrg.requestFocus();
    } else {
      jcbCorg.requestFocus();
    }
  }

//  void jcbCorg_focusGained(FocusEvent e) {}
//  void jrbStNaDan_focusLost(FocusEvent e) {}
//  void jcbCorg_focusLost(FocusEvent e) {}

  void fieldControl() {
    selectedRB = OJ+KO+IB;
    fake.setString("BROJKONTA", "");
    jrfNazKonto.setText("");
    jcbPoNazivu.setSelected(false);
    switch (selectedRB) {
      case 1:
        jcbPoNazivu.setSelected(false);
        jcbPoNazivu.setEnabled(false);
        jrfNazKonto.setText("");
        fake.setString("BROJKONTA", "");
        break;
      case 2:
        jcbPoNazivu.setEnabled(true);
        rcc.setLabelLaF(jrfKonto, true);
        rcc.setLabelLaF(jrfNazKonto, true);
        jrfKonto.requestFocus();
        break;
      case 3:
        jcbPoNazivu.setEnabled(true);
        rcc.setLabelLaF(jrfKonto, true);
        rcc.setLabelLaF(jrfNazKonto, true);
        jrfKonto.requestFocus();
        break;
      default:
        jcbPoNazivu.setEnabled(false);
        jcbCorg.requestFocus();
        break;
    }
  }

  public boolean checkYear() {
    String dateYear = vl.findYear(tds.getTimestamp("zavdatum"));
    Integer y = new Integer(dateYear);
    int zavY= y.intValue();
    Integer cY = new Integer(vl.findYear(tds.getTimestamp("pocdatum")));
    int pocY = cY.intValue();

    if((zavY-pocY)>1)
      return false;
    else
      return true;
  }

  private void bindCorg() {
    jrfCOrg.setDataSet(fake);
    jrfCOrg.setRaDataSet(rdOSUtil.getUtil().getOrgStruktura(hr.restart.zapod.OrgStr.getKNJCORG()));
    jrfCOrg.setVisCols(new int[]{0,1});
    jrfCOrg.setColNames(new String[] {"NAZIV"});
    jrfCOrg.setTextFields(new javax.swing.text.JTextComponent[] {jrfCOrgNaz});
    jrfCOrg.setColumnName("CORG");
    jrfCOrg.setSearchMode(0);
    jrfCOrgNaz.setColumnName("NAZIV");
    jrfCOrgNaz.setNavProperties(jrfCOrg);
    jrfCOrg.setNavButton(this.jbCOrg);
  }
}
