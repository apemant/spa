/****license*****************************************************************
**   file: ispRekap_NextGeneration.java
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
import hr.restart.swing.jpCorg;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raUpitLite;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class ispRekap_NextGeneration extends raUpitLite {
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.zapod.OrgStr knjOrgStr = hr.restart.zapod.OrgStr.getOrgStr();
  Valid vl = Valid.getValid();


  hr.restart.robno._Main main;
  QueryDataSet defQDS;// = new QueryDataSet();
  StorageDataSet dateStDS = new StorageDataSet();
  TableDataSet tds = new TableDataSet();

  TitledBorder titledBorder1;
  TitledBorder titledBorder2;

  Column column1 = new Column();
  Column column2 = new Column();
  Column temp = new Column();
  Column temp1 = new Column();

  XYLayout xYLayout1 = new XYLayout();
  XYLayout xYLayout3 = new XYLayout();
  JLabel jLabel1 = new JLabel();
  JPanel jp = new JPanel();

  jpCorg jpC = new jpCorg(100,255,false){
    public void afterLookUp(boolean succ){
      if (succ) {
//        jrfLokacija.setRaDataSet(rdOSUtil.getUtil().getLokacijeDS(jpC.getCorg(),jpC.isRecursive()));
      }
    }
  };

  JPanel jPanel2 = new JPanel();
  JLabel jlKonto = new JLabel();
  JLabel jlVrIspisa = new JLabel();
//  JLabel jlSifra = new JLabel();
//  JLabel jlNaziv = new JLabel();
//  JLabel jlOrgjJed = new JLabel();
  JraTextField jtfZavDatum = new JraTextField();
  JraTextField jtfPocDatum = new JraTextField();

  JraCheckBox jcbCorg = new JraCheckBox();
  JraCheckBox jcbInvBr = new JraCheckBox();
  JraCheckBox jcbKonto = new JraCheckBox();
  JraCheckBox jcbPoNazivu = new JraCheckBox();

//  JraButton jbCOrg = new JraButton();
  JraButton jbKonto = new JraButton();
  JlrNavField jrfKonto = new JlrNavField();
  JlrNavField jrfNazKonto = new JlrNavField();
//  JlrNavField jrfCOrg = new JlrNavField();
//  JlrNavField jrfCOrgNaz = new JlrNavField();
  public static QueryDataSet qds = new QueryDataSet();
  public static int selectedRB = 0;
  public int OJ =1;
  public int KO =0;
  public int IB =0;
  private static double [] sume;



  public ispRekap_NextGeneration() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  static Timestamp datumOd, datumDo;

  public static QueryDataSet getQdsIspis() {
    return qds;
  }

  public static Timestamp getPocDat(){
    return datumOd;
  }


  public static Timestamp getZavDat(){
    return datumDo;
  }


  public void componentShow() {
    OJ = 1;
    KO = 0;
    IB = 0;
    showDefaultValues();
    fieldControl();
    jcbCorg.requestFocus();
  }

  public void showDefaultValues() {
    if(!tds.isOpen())
      tds.open();
    bindCorg();
    String ks = knjOrgStr.getKNJCORG();
    String qStr = rdOSUtil.getUtil().getDefaultRekap(ks);
//    System.out.println("qStr - " + qStr);
    defQDS = ut.getNewQueryDataSet(qStr);
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

    jcbCorg.setSelected(true);
    jcbInvBr.setSelected(false);
    jcbKonto.setSelected(false);


    tds.setTimestamp("pocDatum", lastY);
    tds.setTimestamp("zavDatum", tsCurrY);
    dateStDS.setString("CORG",ks);
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(dateStDS);
    jpC.setCorg(ks);
//    jrfCOrg.forceFocLost();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
//        System.out.println("invoke later");
        jrfKonto.requestFocus();
//        jrfCOrg.selectAll();
      }
    });
  }

  void fieldControl() {
    selectedRB = OJ+KO+IB;
    System.out.println("OJ+KO+IB = " + selectedRB);
    switch (selectedRB) {
      case 1:
        jcbPoNazivu.setSelected(false);
        jcbPoNazivu.setEnabled(false);
        jrfNazKonto.setText("");
        dateStDS.setString("BROJKONTA", "");
        break;
      case 2:
        jcbPoNazivu.setEnabled(true);
        rcc.setLabelLaF(jrfKonto, true);
        rcc.setLabelLaF(jrfNazKonto, true);
//        jrfKonto.requestFocus();
        break;
      case 3:
        jcbPoNazivu.setEnabled(true);
        rcc.setLabelLaF(jrfKonto, true);
        rcc.setLabelLaF(jrfNazKonto, true);
//        jrfKonto.requestFocus();
        break;
      default:
        jcbPoNazivu.setEnabled(false);
        jcbPoNazivu.setSelected(false);
//        jcbCorg.requestFocus();
        break;
    }
  }

  public boolean Validacija(){
    if (vl.isEmpty(jpC.corg)) return false;

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
    return true;
  }

  private boolean okPressed;

  public void okPress() {
    okPressed = true;
    datumOd = tds.getTimestamp("pocDatum");
    datumDo = tds.getTimestamp("zavDatum");
    int rows;
    qds = prepareIspis();
    try {
      rows = qds.rowCount();
    }
    catch (Exception ex) {
      rows = 0;
    }
    if (rows == 0)  setNoDataAndReturnImmediately();
    setReports();
  }

  private void setReports() {
    killAllReports();

    switch (selectedRB) {
      case 1:
//        if(this.jcbPoNazivu.isSelected())
//          this.addReport("hr.restart.os.repIspisRekap_1","Rekapitulacija", 5);
//          this.addReport("hr.restart.os.repIspisRekap_1","hr.restart.os.repIspisRekap","IspisRekap_1","Rekapitulacija");
//        else
//          this.addReport("hr.restart.os.repIspisRekap_1BN","Rekapitulacija", 5);
System.out.println("IspisRekap_1BN");
          this.addReport("hr.restart.os.repIspisRekap_1BN","hr.restart.os.repIspisRekap","IspisRekap_1BN","Rekapitulacija");
        break;
      case 2:
        if(this.jcbPoNazivu.isSelected()){
//          this.addReport("hr.restart.os.repIspisRekap_2","Rekapitulacija", 5);
System.out.println("IspisRekap_2");
          this.addReport("hr.restart.os.repIspisRekap_2","hr.restart.os.repIspisRekap","IspisRekap_2","Rekapitulacija");
        }else{
//          this.addReport("hr.restart.os.repIspisRekap_2BN","Rekapitulacija", 5);
System.out.println("IspisRekap_2BN");
          this.addReport("hr.restart.os.repIspisRekap_2BN","hr.restart.os.repIspisRekap","IspisRekap_2BN","Rekapitulacija");
        }
        break;
      case 3:
        if(this.jcbPoNazivu.isSelected()){
//          this.addReport("hr.restart.os.repIspisRekap_3","Rekapitulacija", 5);
System.out.println("IspisRekap_3");
          this.addReport("hr.restart.os.repIspisRekap_3NAME","hr.restart.os.repIspisRekap","IspisRekap_3NAME","Rekapitulacija");
        }else{
//          this.addReport("hr.restart.os.repIspisRekap_3BN","Rekapitulacija", 5);
System.out.println("IspisRekap_3BN");
          this.addReport("hr.restart.os.repIspisRekap_3BN","hr.restart.os.repIspisRekap","IspisRekap_3BN","Rekapitulacija");
        }
        break;
      case 4:
//        this.addReport("hr.restart.os.repIspisRekap_4","Rekapitulacija", 5);
System.out.println("IspisRekap_4");
        this.addReport("hr.restart.os.repIspisRekap_4_SE","hr.restart.os.repIspisRekap","IspisRekap_4_SE","Rekapitulacija");
        break;
      case 5:
//        this.addReport("hr.restart.os.repIspisRekap_5","Rekapitulacija", 5);
System.out.println("IspisRekap_5");
        this.addReport("hr.restart.os.repIspisRekap_5","hr.restart.os.repIspisRekap","IspisRekap_5","Rekapitulacija");
        break;
      case 6:
//        this.addReport("hr.restart.os.repIspisRekap_6","Rekapitulacija", 5);
System.out.println("IspisRekap_6");
        this.addReport("hr.restart.os.repIspisRekap_6","hr.restart.os.repIspisRekap","IspisRekap_6BN","Rekapitulacija");
        break;
      case 7:
//        this.addReport("hr.restart.os.repIspisRekap_7","Rekapitulacija", 5);
System.out.println("IspisRekap_7");
//        this.addReport("hr.restart.os.repIspisRekap_7","hr.restart.os.repIspisRekap","IspisRekap_7","Rekapitulacija");
        this.addReport("hr.restart.os.repIspisRekap_7","hr.restart.os.repIspisRekap","IspisRekap_7BN","Rekapitulacija");
        break;
    }
  }

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
    if (jpC.getCorg().equals(""))
      knjSDS = knjOrgStr.getOrgstrFromCurrKnjig();
    else
      knjSDS = knjOrgStr.getOrgstrAndKnjig(jpC.getCorg());
    if(knjSDS.getRowCount()==0)
      knjSDS = knjOrgStr.getOrgstrAndKnjig(jpC.getCorg());
    if(knjSDS.getRowCount()==0)
      return null;

    knjSDS.first();
    String inCorg = Condition.in("CORG2",knjSDS,"CORG").toString().toLowerCase();
    String inCorg2 = Condition.in("CORG2",knjSDS,"CORG").qualified("os_sredstvo").toString().toLowerCase();
    String kontoStr = "";

    if (!this.jrfKonto.getText().trim().equals("")){
      kontoStr =  " and os_sredstvo.brojkonta ='" + this.jrfKonto.getText().trim()+"'";
    }

    String dn = "select invbroj as invbroj, corg2 as corg, osnduguje, osnpotrazuje, CAST((osnduguje - osnpotrazuje) AS numeric(15,2)) as c1, ispduguje, isppotrazuje, CAST((isppotrazuje - ispduguje) AS numeric(15,2)) as c2  FROM OS_Promjene where "+
                "os_promjene.datpromjene >='"+ut.getFirstDayOfYear(tds.getTimestamp("pocDatum"))+"' and os_promjene.datpromjene < "+util.getTimestampValue(tds.getTimestamp("pocDatum"),0)+
                " and "+ inCorg + " order by invbroj";

    String ps = "SELECT invbroj as invbroj, brojkonta as brojkonta, corg2 as corg, osnpocetak as osnpocetak, isppocetak as isppocetak "+
                "FROM OS_Sredstvo where"+
                "(os_sredstvo.aktiv='D' or os_sredstvo.datlikvidacije >= '"+ut.getFirstDayOfYear(tds.getTimestamp("pocDatum"))+"')"+
                "and "+ inCorg + kontoStr + " order by invbroj";

    qStr = rdOSUtil.getUtil().getRekapitulacija(inCorg2, util.getTimestampValue(tds.getTimestamp("pocDatum"),0), util.getTimestampValue(tds.getTimestamp("zavDatum"),1), this.jrfKonto.getText().trim(), (OJ+KO+IB));//gb);
    Aus.refilter(tempSet, qStr);    

    finalSet.setColumns(tempSet.cloneColumns());
    finalSet.open();

System.out.println("\nps - " + ps);
System.out.println("\ndn - " + dn);

	Aus.refilter(tempPsSet, ps);
    tempPsSet.first();
    lookupData ld = lookupData.getlookupData();
    do {
      finalSet.insertRow(false);
      finalSet.setString("CORG",tempPsSet.getString("CORG"));
      finalSet.setString("BROJKONTA",tempPsSet.getString("BROJKONTA"));
      finalSet.setString("INVBROJ",tempPsSet.getString("INVBROJ"));
      finalSet.setBigDecimal("TRI",tempPsSet.getBigDecimal("OSNPOCETAK"));
      finalSet.setBigDecimal("CETIRI",tempPsSet.getBigDecimal("ISPPOCETAK"));
      finalSet.setBigDecimal("PET",(tempPsSet.getBigDecimal("OSNPOCETAK").subtract(tempPsSet.getBigDecimal("ISPPOCETAK"))));
    } while (tempPsSet.next());

    if (!ut.getFirstSecondOfDay(tds.getTimestamp("pocDatum")).equals(ut.getFirstDayOfYear(tds.getTimestamp("pocDatum")))){
      tempDnSet = ut.getNewQueryDataSet(dn);
      finalSet.first();
      do {
        if (ld.raLocate(finalSet,new String[] {"INVBROJ","CORG"},new String[] {tempDnSet.getString("INVBROJ"),tempDnSet.getString("CORG")}))  {
          finalSet.setBigDecimal("TRI",finalSet.getBigDecimal("TRI").add(tempDnSet.getBigDecimal("OSNDUGUJE").subtract(tempDnSet.getBigDecimal("OSNPOTRAZUJE")))); //tempPsSet.getBigDecimal("OSNPOCETAK"));
          finalSet.setBigDecimal("CETIRI",finalSet.getBigDecimal("CETIRI").add(tempDnSet.getBigDecimal("ISPPOTRAZUJE").subtract(tempDnSet.getBigDecimal("ISPDUGUJE"))));//tempPsSet.getBigDecimal("ISPPOCETAK"));
          finalSet.setBigDecimal("PET",(tempPsSet.getBigDecimal("OSNPOCETAK").subtract(tempPsSet.getBigDecimal("ISPPOCETAK"))));
        }
      } while (tempDnSet.next());
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
    
    System.out.println("(OJ+KO+IB) = " + (OJ+KO+IB)); //XDEBUG delete when no more needed

    if((OJ+KO+IB)==1){
      finalSet = rdOSUtil.getUtil().refilterMemTable(finalSet,"CORG");
    }
    else if((OJ+KO+IB)==2){
      finalSet = rdOSUtil.getUtil().refilterMemTable(finalSet,"BROJKONTA");
    }
    else if((OJ+KO+IB)==3){
      finalSet = rdOSUtil.getUtil().refilterMemTable(finalSet,"CORG","BROJKONTA");
    }
    else if((OJ+KO+IB)==4){
      finalSet = rdOSUtil.getUtil().refilterMemTable(finalSet,"INVBROJ");
    }
    else if((OJ+KO+IB)==6){
      finalSet = rdOSUtil.getUtil().refilterMemTable2(finalSet,"BROJKONTA");
    }

    finalSet.first();
    do {
      finalSet.setBigDecimal("DESET",(finalSet.getBigDecimal("TRI").add(finalSet.getBigDecimal("SEST")).subtract(finalSet.getBigDecimal("SEDAM"))));
      finalSet.setBigDecimal("JEDANAEST",finalSet.getBigDecimal("CETIRI").subtract(finalSet.getBigDecimal("OSAM")).add(finalSet.getBigDecimal("DEVET")).add(finalSet.getBigDecimal("AMOR")));
      finalSet.setBigDecimal("DVANAEST",finalSet.getBigDecimal("DESET").subtract(finalSet.getBigDecimal("JEDANAEST")));
    } while (finalSet.next());
    finalSet.first();
    finalSet.setRowId("CORG", true);
    do {
      if(finalSet.getString("CORG").equals("") && finalSet.getRowCount()>0)
        finalSet.deleteRow();
      sum3=sum3.add(finalSet.getBigDecimal("TRI"));
      sum4=sum4.add(finalSet.getBigDecimal("CETIRI"));
      sum6=sum6.add(finalSet.getBigDecimal("SEST"));
      sum7=sum7.add(finalSet.getBigDecimal("SEDAM"));
      sum8=sum8.add(finalSet.getBigDecimal("OSAM"));
      sum5=sum5+finalSet.getBigDecimal("PET").doubleValue();
      sum9=sum9.add(finalSet.getBigDecimal("DEVET"));
      amor=amor.add(finalSet.getBigDecimal("AMOR"));
      sum10=sum10+finalSet.getBigDecimal("DESET").doubleValue();
      sum11=sum11+finalSet.getBigDecimal("JEDANAEST").doubleValue();
      sum12=sum12+finalSet.getBigDecimal("DVANAEST").doubleValue();
      finalSet.next();
    } while(finalSet.inBounds());

    sume = new double[] {sum3.doubleValue(), sum4.doubleValue(), sum5, sum6.doubleValue(),
      sum7.doubleValue(), sum8.doubleValue(), sum9.doubleValue(),  sum10, sum11, sum12, amor.doubleValue()};

    return finalSet;
  }

  public void firstESC() {
    if (okPressed) {
      rcc.EnabDisabAll(jp,true);
      rcc.setLabelLaF(jcbPoNazivu,(jcbKonto.isSelected() && ! jcbInvBr.isSelected()));
      if(!jrfKonto.getText().equals("") ) {
        jrfKonto.setText("");
        jrfKonto.forceFocLost();
      }
      jpC.corg.requestFocus();
//      jrfCOrg.selectAll();
      okPressed = false;
    } else {
      if(!jrfKonto.getText().equals("") ) {
        dateStDS.setString("BROJKONTA", "");
        jrfNazKonto.setText("");
        jrfKonto.requestFocus();
      } else {
        dateStDS.setString("CORG", "");
        jpC.setCorg("");
        jpC.corg.requestFocus();
      }
    }
  }

  public boolean runFirstESC() {
    if(!jpC.getCorg().equals("") || !jrfKonto.getText().equals("")) {
      return true;
    }
    return false;
  }

  private void jbInit() throws Exception {
    tds.setColumns(new Column[] {dM.createTimestampColumn("pocDatum"), dM.createTimestampColumn("zavDatum")});
    jtfZavDatum.setDataSet(tds);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setText("jraTextField2");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfPocDatum.setDataSet(tds);
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setText("jraTextField2");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);

    temp = dm.createStringColumn("CORG","Organizacijska jedinica",10);
    temp1 = dm.createStringColumn("BROJKONTA","Broj konta",15);

    dateStDS.setColumns(new Column[] {temp,temp1});

    jp.setLayout(xYLayout1);
    xYLayout1.setWidth(555);
    xYLayout1.setHeight(170);
    this.setJPan(jp);

    jp.setToolTipText("");
//    jlOrgjJed.setText("Org. jedinica");
//    jlSifra.setText("Šifra");
//    jlNaziv.setText("Naziv");
//    jbCOrg.setText("...");

    jLabel1.setText("Datum (od-do)");

    jPanel2.setLayout(xYLayout3);
    jPanel2.setBorder(BorderFactory.createEtchedBorder());
    xYLayout3.setWidth(280);
    xYLayout3.setHeight(100);
    jcbCorg.setText("po org. jedinici");
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

//    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
//      public void knjigChanged(String oldKnjig, String newKnjig) {
//        bindCorg();
//      };
//    });
    bindCorg();

    jbKonto.setText("...");
    jrfKonto.setRaDataSet(dm.getKonta());
    jrfKonto.setVisCols(new int[]{0,1});
    jrfKonto.setColNames(new String[] {"NAZIVKONTA"});
    jrfKonto.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazKonto});
    jrfKonto.setColumnName("BROJKONTA");
    jrfKonto.setDataSet(dateStDS);
    jrfNazKonto.setColumnName("NAZIVKONTA");
    jrfNazKonto.setNavProperties(jrfKonto);
    jrfKonto.setSearchMode(3);

    this.setJPan(jp);

    jlKonto.setText("Konto");
    jlVrIspisa.setText("Vrsta ispisa");
    jcbPoNazivu.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbPoNazivu.setText("s nazivom");

    jp.add(jpC, new XYConstraints(0,20,-1,-1));

//    jp.add(jrfCOrgNaz,      new XYConstraints(255, 45, 275, -1));
//    jp.add(jrfCOrg,     new XYConstraints(150, 45, 100, -1));
//    jp.add(jlSifra,   new XYConstraints(150, 20, -1, -1));
//    jp.add(jlNaziv,   new XYConstraints(235, 20, -1, -1));
//    jp.add(jlOrgjJed,    new XYConstraints(15, 45, -1, -1));
//    jp.add(jbCOrg,   new XYConstraints(533, 45, 21, 21));

    jp.add(jtfZavDatum,     new XYConstraints(255, 70, 100, -1));
    jp.add(jtfPocDatum,      new XYConstraints(150, 70, 100, -1));
    jp.add(jLabel1,    new XYConstraints(15, 70, -1, -1));
//    jp.add(jcbPoNazivu,    new XYConstraints(427, 70, -1, -1));

    jp.add(jbKonto,   new XYConstraints(515, 45, 21, 21));
    jp.add(jrfNazKonto,     new XYConstraints(255, 45, 255, -1));
    jp.add(jlKonto,   new XYConstraints(15, 45, -1, -1));
    jp.add(jrfKonto,  new XYConstraints(150, 45, 100, -1));

    jPanel2.add(jcbCorg,     new XYConstraints(5,  10, -1, -1));
    jPanel2.add(jcbPoNazivu, new XYConstraints(185, 10, -1, -1));
    jPanel2.add(jcbKonto,    new XYConstraints(115, 10, -1, -1));
    jPanel2.add(jcbInvBr,    new XYConstraints(275, 10, -1, -1));

    jp.add(jlVrIspisa,    new XYConstraints(15, 112, -1, -1));
    jp.add(jPanel2,                  new XYConstraints(150, 100, 380, 50));

    jrfKonto.setRaDataSet(hr.restart.zapod.raKonta.getAnalitickaKonta());
    jrfKonto.setVisCols(new int[]{0,1});
    jrfKonto.setColNames(new String[] {"NAZIVKONTA"});
    jrfKonto.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazKonto});
    jrfKonto.setColumnName("BROJKONTA");
    jrfNazKonto.setColumnName("NAZIVKONTA");
    jrfNazKonto.setNavProperties(jrfKonto);
    jrfKonto.setNavButton(this.jbKonto);
  }

  private void bindCorg() {
    jpC.bind(dateStDS);
//    jrfCOrg.setDataSet(dateStDS);
//    jrfCOrg.setRaDataSet(rdOSUtil.getUtil().getOrgStruktura(hr.restart.zapod.OrgStr.getKNJCORG()));
//    jrfCOrg.setVisCols(new int[]{0,1});
//    jrfCOrg.setColNames(new String[] {"NAZIV"});
//    jrfCOrg.setTextFields(new javax.swing.text.JTextComponent[] {jrfCOrgNaz});
//    jrfCOrg.setColumnName("CORG");
//    jrfCOrg.setSearchMode(0);
//    jrfCOrgNaz.setColumnName("NAZIV");
//    jrfCOrgNaz.setNavProperties(jrfCOrg);
//    jrfCOrg.setNavButton(this.jbCOrg);
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

  public static double[] getSume(){
    return sume;
  }

  public boolean isIspis(){
    return false;
  }

  public boolean ispisNow(){
    return true;
  }
}
