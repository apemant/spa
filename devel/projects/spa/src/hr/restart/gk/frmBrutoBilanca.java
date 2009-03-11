/****license*****************************************************************
**   file: frmBrutoBilanca.java
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
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.dlgCompanyTree;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raImages;
import hr.restart.util.raUpitFat;
import hr.restart.util.sysoutTEST;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmBrutoBilanca extends raUpitFat {
  raPanKonto kontoPanel = new raPanKonto(); // ("B_NO");
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.util.Util util = hr.restart.util.Util.getUtil();
  Valid vl = Valid.getValid();

  raComboBox rcbPrivremenost = new raComboBox();
  raComboBox rcbPripOrgJed = new raComboBox() {
    public void this_itemStateChanged() {
      super.this_itemStateChanged();
      updateSelectTreeButton();
    }
  };
  raComboBox rcbIspis = new raComboBox() {
    public void this_itemStateChanged() {
      super.this_itemStateChanged();
      updateSelectTreeButton();
    }
  };
  raComboBox rcbBilanca = new raComboBox();
  
  JraButton selOrg = new JraButton();

  private String knjigovodstvo;// = hr.restart.zapod.OrgStr.getOrgStr().getPripKnjig(kontoPanel.getCorg());
  
//  JraCheckBox jcbPrivremeni = new JraCheckBox();
  JPanel jpDetail = new JPanel();
  StorageDataSet stds = new StorageDataSet();
  QueryDataSet beforeRepDS2 = new QueryDataSet();
  public QueryDataSet repDS2;
//  public QueryDataSet tableDS = new QueryDataSet();
  public QueryDataSet repSet;
  private QueryDataSet kontaSet = null;
//  Column colMjP = new Column();
//  Column colMjZ = new Column();
//  Column colGod = new Column();
  Column colSaldo = new Column();
  Column colPocID = new Column();
  Column colPocIP = new Column();
  Column colNK = new Column();
  XYLayout layDetail = new XYLayout();
  JLabel jlPeriod = new JLabel();
  private String knjigDifolt;
  public static Timestamp datumP;
  public static Timestamp datumZ;
  protected String corgRemember = "";
  boolean doubleClicked = false;
  boolean entered = false;
//  boolean makeRDS = true;
  boolean instance = false;
  LinkedList ll = new LinkedList();
  LinkedList pl = new LinkedList();
  String razdoblje, corgSS, privremeni, razdoblje1, corgSS1;
  dM dm = dM.getDataModule();
  
  private boolean isKompletBilanca = true;

  JraTextField jtGodina = new JraTextField();

  JraTextField jtMjesecPoc = new JraTextField() {
    public boolean maskCheck() {
      if (super.maskCheck()) {
        if (!checkMonthPoc()) {
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

  JraTextField jtMjesecZav = new JraTextField() {
    public boolean maskCheck() {
      if (super.maskCheck()) {
        if (!checkMonthZav()) {
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

  static frmBrutoBilanca fbb;
  JLabel jLabel2 = new JLabel();
  JraCheckBox jcbOrgStr = new JraCheckBox();
  Border border1;
  Border border2;

//  TitledBorder titledBorder1;
//  JraRadioButton jrbCijela = new JraRadioButton();
//  JraRadioButton jrbOdabrana = new JraRadioButton();
//  raButtonGroup buttonGroup1 = new raButtonGroup();
//  JPanel jPanel1 = new JPanel();
//  XYLayout xYLayout2 = new XYLayout();

//  JraRadioButton jrbSkupni = new JraRadioButton();
//  JraRadioButton jrbPojedinacni = new JraRadioButton();
//  XYLayout xYLayout1 = new XYLayout();
//  raButtonGroup buttonGroup3 = new raButtonGroup();
//  JPanel jPanel3 = new JPanel();

//  raButtonGroup buttonGroup2 = new raButtonGroup();
//  JraRadioButton jrbSintetikBilanca = new JraRadioButton();
//  JraRadioButton jrbAnalitikBilanca = new JraRadioButton();
//  JraRadioButton jrbKomplBilanca = new JraRadioButton();
//  JPanel jPanel2 = new JPanel();

  public frmBrutoBilanca() {
    try {
      jbInit();
      fbb = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public frmBrutoBilanca(String dva) {
    try {
      jbInit();
      setHightInstance();
//      fbb = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private int[] visibleColumns = new int[]{0,1,2,3,4,5};
  private boolean firstTime = true;

  public int[] navVisibleColumns(){
    return visibleColumns;
  }

  public String navDoubleClickActionName(){
    return "Bruto bilanca";
  }


  void setHightInstance() {
    jpDetail.setMinimumSize(new Dimension(600, 358));
    jpDetail.setPreferredSize(new Dimension(580, 205));
  }

  public frmBrutoBilanca(int tri) {
    this.instance = true;
    try {
      jbInit();
      fbb = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static frmBrutoBilanca getInstance() {
//    System.out.println("GETING INSTANCE OF frmBrutoBilanca");
    if (fbb == null) {
      fbb = new frmBrutoBilanca();
    }
    return fbb;
  }
  
  boolean isTreeSelected() {
    return stds.getString("ISPIS").equals("0") && stds.getString("ORGSTR").equals("0");
  }
  
  protected void updateSelectTreeButton() {
    selOrg.setEnabled(isTreeSelected());
  }

  
  
  public void resetDefaults() {
    super.resetDefaults();
    initialValues();
  }

  public void componentShow() {
    
    kontaSet = dm.getKonta();
    kontaSet.open();
    if (firstTime)
      initialValues();
    firstTime = false;
    rcc.EnabDisabAll(jpDetail, true);
    updateSelectTreeButton();
    setDataSet(null);
    kontoPanel.jlrKontoBroj.requestFocusLater();
  }

  public boolean runFirstESC() {
    if (entered) {
      return true;
    } else if (!kontoPanel.jlrKontoBroj.getText().equals("")){
      return true;
    }
    return false;
  }

  public void firstESC() {
//    System.out.println("rfesc");
//    System.out.println("ll.isEmpty() " + ll.isEmpty());
//    System.out.println("entered " + entered);
//    System.out.println("!kontoPanel.jlrKontoBroj.getText().equals(\"\") " + !kontoPanel.jlrKontoBroj.getText().equals(""));
    if (ll.isEmpty()) {
      if (entered){
        doubleClicked = false;
        kontoPanel.setNoLookup(false);
        entered = false;
//        showDefaultValues();
        
        rcc.EnabDisabAll(jpDetail, true);
        updateSelectTreeButton();
//        rcc.setLabelLaF(kontoPanel.jlrCorg, false);
//        rcc.setLabelLaF(kontoPanel.jlrNazorg, false);
//        rcc.setLabelLaF(kontoPanel.jbSelCorg, false);
        setDataSet(null);
        kontoPanel.jlrKontoBroj.requestFocus();        
        removeNav();
      } else if (!kontoPanel.jlrKontoBroj.getText().equals("")) {
        kontoPanel.jlrKontoBroj.setText("");
        kontoPanel.jlrKontoBroj.emptyTextFields();
        kontoPanel.jlrKontoBroj.requestFocus();
      }
    } else {
      llHandler();
//      this.changeIcon(1);
    }
  }

  public void cancelPress() {
    doubleClicked = false;
    kontoPanel.setNoLookup(false);
    if (!ll.isEmpty()) {
      ll.clear();
    }
    rcc.EnabDisabAll(jpDetail, true);
    setDataSet(null);
    super.cancelPress();
  }

  public boolean Validacija(){
    if (kontoPanel.jlrCorg.getText().equals("")) {
      kontoPanel.jlrCorg.requestFocus();
      JOptionPane.showConfirmDialog(null, "Unesite organizacijsku jedinicu !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      return false;
    }

    if ((jtMjesecPoc.getText().equals("") || jtMjesecZav.getText().equals("") || jtGodina.getText().equals("")) && !instance) {
      jtMjesecPoc.requestFocus();
      JOptionPane.showConfirmDialog(null, "Unesite mjesec i/ili godinu !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }


  public void okPress() {
    val = "";
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(stds);
    corgRemember = kontoPanel.getCorg();
    entered = true;
    
    if (stds.getString("jtMjesecPoc").equals("00")){
      visibleColumns = new int[]{0,1,2,3,4,5};
    } else {
      visibleColumns = new int[]{0,3,4,5};
    }
      
    knjigovodstvo = hr.restart.zapod.OrgStr.getOrgStr().getPripKnjig(kontoPanel.getCorg());
    
    String qStr = getQdsString(knjigovodstvo);

//    System.out.println("\n-----\n"+qStr+"\n-----\n");

    QueryDataSet qds = util.getNewQueryDataSet(qStr);
    if (qds.isEmpty()) setNoDataAndReturnImmediately();
    QueryDataSet qds2 = util.getNewQueryDataSet(qStr);



//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(qds);

    QueryDataSet jptvov = getValutaSet(deriveNumber(qds));
    jptvov.first();
//    this.getJPTV().setDataSetAndSums(jptvov, new String[] {"POCID","POCIP","ID","IP"});
    if (!doubleClicked){
//      qds.close();
//      qds.addColumn((Column)colNK.clone());
//      qds.addColumn((Column) colPocID.clone());
//      qds.addColumn((Column) colPocIP.clone());
//      qds.open();

//      repDS2 = getValutaSet(makeSomethingUsefull(qds));
      
      
      repDS2 = getValutaSet(makeSetForNewIspis(qds));
      
      repSet = makeSomethingUsefull(qds2);
      
//      sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//      st.showInFrame(repDS2,"Dataset Test Frame repDS2");
//      st.showInFrame(repSet,"Dataset Test Frame repSet");
      
      
      repSet.first();

      BigDecimal nulica = Aus.zero2;
      kumulativi[0] = nulica;
      kumulativi[1] = nulica;
      kumulativi[2] = nulica;
      kumulativi[3] = nulica;
      kumulativi[4] = nulica;

      repDS2.first();
      do {
        kumulativi[0] = kumulativi[0].add(repDS2.getBigDecimal("POCID"));
        kumulativi[1] = kumulativi[1].add(repDS2.getBigDecimal("POCIP"));
        kumulativi[2] = kumulativi[2].add(repDS2.getBigDecimal("ID"));
        kumulativi[3] = kumulativi[3].add(repDS2.getBigDecimal("IP"));
      } while (repDS2.next());
      kumulativi[4] = (kumulativi[0].subtract(kumulativi[1])).add(kumulativi[2].subtract(kumulativi[3]));

//      hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//      syst.prn(repDS2);

      isKompletBilanca = stds.getString("BILANCA").equals("K");
      
      killAllReports();
      addingReport(kontoPanel.jlrKontoBroj.getText().equals(""));

//      this.getJPTV().enableEvents(false);
//      this.getJPTV().getDataSet().first();
//      this.getJPTV().enableEvents(true);
    }
    jptvov.first();
    setDataSetAndSums(jptvov, new String[] {"POCID","POCIP","ID","IP","SALDO"});

//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(repDS2);
  }
  
  
  private String getQdsString(String knjigovodstvo) {
    String gkkumulativi = "gkkumulativi.corg";
    String gkstavkerad = "gkstavkerad.corg";
    String groupKumul = "gkkumulativi.corg,";
    String groupBystrad = "gkstavkerad.corg,";
    if (stds.getString("ISPIS").equals("1")) {
      gkkumulativi = "'"+knjigovodstvo+"'";
      gkstavkerad = "'"+knjigovodstvo+"'";
      groupKumul = "";
      groupBystrad = "";
    }
    
    String qStr = "select "+gkkumulativi+" as realcorg, gkkumulativi.brojkonta as brojkonta, " +
                  "sum(gkkumulativi.id) as id, "+
                  "sum(gkkumulativi.ip) as ip, gkkumulativi.godmj as godmj  "+
                  "from gkkumulativi where gkkumulativi.knjig='"+knjigovodstvo+"' and "+getCorgs("gkkumulativi")+" and "+
                  "gkkumulativi.brojkonta like '"+getBrKon()+"%' "+
                  "and gkkumulativi.godmj in ("+range(stds.getString("jtGodina"), stds.getString("jtMjesecPoc"), stds.getString("jtMjesecZav"))+") "+
                  "group by "+groupKumul+"gkkumulativi.brojkonta, gkkumulativi.godmj";
    
    if (stds.getString("PRIVREMENO").equals("1")){ //jcbPrivremeni.isSelected()){
      qStr +=     " UNION ALL "+
                  "select "+gkstavkerad+" as realcorg, gkstavkerad.brojkonta as brojkonta, " +
                  "sum (gkstavkerad.id) as id, "+
                  "sum (gkstavkerad.ip) as ip, gkstavkerad.godmj as godmj  "+
                  "from gkstavkerad where gkstavkerad.knjig='"+knjigovodstvo+"' and "+getCorgs("gkstavkerad")+" "+
                  "and gkstavkerad.brojkonta like '"+getBrKon()+"%' "+
                  "and gkstavkerad.godmj in ("+range(stds.getString("jtGodina"), stds.getString("jtMjesecPoc"), stds.getString("jtMjesecZav"))+") "+
                  "group by "+groupBystrad+"gkstavkerad.brojkonta, gkstavkerad.godmj";
    }
    return qStr;
  }

  protected String val;

  public String getVALUTA(){
    return val;
  }

  public QueryDataSet getValutaSet(QueryDataSet olDS){
    return olDS;
  }

  private QueryDataSet makeSetForNewIspis(QueryDataSet razvoj){

    QueryDataSet razvijeni = new QueryDataSet();
    razvijeni.setColumns(razvoj.cloneColumns());
    try {
    if (razvijeni.getColumn("CORG") == null)
      razvijeni.addColumn((Column) dm.getGkkumulativi().getColumn("CORG").clone());
    } catch (Exception e) {
      razvijeni.addColumn((Column) dm.getGkkumulativi().getColumn("CORG").clone());
//      e.printStackTrace();// TODO: handle exception
    }
    razvijeni.addColumn((Column)colNK.clone());
    razvijeni.addColumn((Column) colPocID.clone());
    razvijeni.addColumn((Column) colPocIP.clone());
    razvijeni.addColumn((Column) colSaldo.clone());

    razvijeni.open();
    razvoj.first();
    System.out.println("stds.getString(\"ISPIS\") - " + stds.getString("ISPIS")); //XDEBUG delete when no more needed
    
    Map transitions = null;
    if (isTreeSelected())
      transitions = dlgCompanyTree.get("bruto-bilanca").getTransitions(kontoPanel.getCorg());
    
    do {
      
//      if (stds.getString("ISPIS").equals("1")) {
//        razvoj.setString("CORG",knjigovodstvo);
//      }
      String realcorg = razvoj.getString("REALCORG");
      if (transitions != null && transitions.containsKey(realcorg))
        realcorg = (String) transitions.get(realcorg);
      
      if (!lookupData.getlookupData().raLocate(
          razvijeni,
          new String[] {"BROJKONTA","CORG"},
          new String[] {razvoj.getString("BROJKONTA"), realcorg})
          ){
        razvijeni.insertRow(false);
        razvijeni.setString("CORG", realcorg);
        razvijeni.setString("BROJKONTA", razvoj.getString("BROJKONTA"));
        razvijeni.setString("NK", getNazKOnto(razvoj.getString("BROJKONTA")));
        razvijeni.setString("GODMJ", razvoj.getString("GODMJ"));
        
        if (!razvoj.getString("GODMJ").substring(4,6).equals("00")){
          razvijeni.setBigDecimal("POCID",Aus.zero2);
          razvijeni.setBigDecimal("POCIP",Aus.zero2);
          razvijeni.setBigDecimal("ID",razvoj.getBigDecimal("ID"));
          razvijeni.setBigDecimal("IP",razvoj.getBigDecimal("IP"));
        } else {
          razvijeni.setBigDecimal("POCID",razvoj.getBigDecimal("ID"));
          razvijeni.setBigDecimal("POCIP",razvoj.getBigDecimal("IP"));
          razvijeni.setBigDecimal("ID",Aus.zero2);
          razvijeni.setBigDecimal("IP",Aus.zero2);
        }
      } else {
        if (!razvoj.getString("GODMJ").substring(4,6).equals("00")){
          razvijeni.setBigDecimal("ID",razvijeni.getBigDecimal("ID").add(razvoj.getBigDecimal("ID")));
          razvijeni.setBigDecimal("IP",razvijeni.getBigDecimal("IP").add(razvoj.getBigDecimal("IP")));
        } else {
          razvijeni.setBigDecimal("POCID",razvijeni.getBigDecimal("POCID").add(razvoj.getBigDecimal("ID")));
          razvijeni.setBigDecimal("POCIP",razvijeni.getBigDecimal("POCIP").add(razvoj.getBigDecimal("IP")));
        }
      }
      razvijeni.setBigDecimal("SALDO", razvijeni.getBigDecimal("SALDO").add((razvijeni.getBigDecimal("POCID").add(razvijeni.getBigDecimal("ID"))).subtract(razvijeni.getBigDecimal("POCIP").add(razvijeni.getBigDecimal("IP")))));
    } while (razvoj.next());
    
//    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//    st.showInFrame(razvijeni,"A");

    return razvijeni;
  }  
  
  private QueryDataSet makeSomethingUsefull(QueryDataSet razvoj) {
    QueryDataSet razvijeni = new QueryDataSet();
    razvijeni.setColumns(razvoj.cloneColumns());
    try {
      if (razvijeni.getColumn("CORG") == null)
        razvijeni.addColumn((Column) dm.getGkkumulativi().getColumn("CORG").clone());
      } catch (Exception e) {
        razvijeni.addColumn((Column) dm.getGkkumulativi().getColumn("CORG").clone());
//        e.printStackTrace();// TODO: handle exception
      }
    razvijeni.addColumn((Column)colNK.clone());
    razvijeni.addColumn((Column) colPocID.clone());
    razvijeni.addColumn((Column) colPocIP.clone());
    razvijeni.addColumn((Column) colSaldo.clone());

    razvijeni.open();
    
    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
    st.prn(razvoj);
    
    razvoj.first();
    
    
    Map transitions = null;
    if (isTreeSelected())
      transitions = dlgCompanyTree.get("bruto-bilanca").getTransitions(kontoPanel.getCorg());

    do {

      
//      if (stds.getString("ORGSTR").equals("1")) {
//        razvoj.setString("CORG",knjigovodstvo);
//      }
      
      
      if (stds.getString("BILANCA").equals("K") || stds.getString("BILANCA").equals("S")){
        for (int i = 1; i <= 3; i++) {
          if (lookupData.getlookupData().raLocate(kontaSet,"BROJKONTA",razvoj.getString("BROJKONTA").substring(0, i)) &&
              kontaSet.getString("ISPISBB").equals("1")){
            String realcorg = razvoj.getString("REALCORG");
            if (transitions != null && transitions.containsKey(realcorg))
              realcorg = (String) transitions.get(realcorg);
            
            if (!lookupData.getlookupData().raLocate(razvijeni,"CORG", realcorg)) {
              
              razvijeni.insertRow(false);
              razvijeni.setString("CORG", realcorg);
              razvijeni.setString("BROJKONTA", razvoj.getString("BROJKONTA").substring(0, i));
              razvijeni.setBigDecimal("ID", razvoj.getBigDecimal("ID"));
              razvijeni.setBigDecimal("IP", razvoj.getBigDecimal("IP"));
              razvijeni.setBigDecimal("POCID", Aus.zero2); //razvoj.getBigDecimal("POCID"));
              razvijeni.setBigDecimal("POCIP", Aus.zero2); //razvoj.getBigDecimal("POCIP"));
              razvijeni.setString("NK", getNazKOnto(razvoj.getString("BROJKONTA").substring(0, i)));
              razvijeni.setString("GODMJ", razvoj.getString("GODMJ"));
              
            }
          }
        }
      }
      if (stds.getString("BILANCA").equals("K") || stds.getString("BILANCA").equals("A")){
        if (lookupData.getlookupData().raLocate(kontaSet,"BROJKONTA",razvoj.getString("BROJKONTA")) &&
            kontaSet.getString("ISPISBB").equals("1")){
          String realcorg = razvoj.getString("REALCORG");
          if (transitions != null && transitions.containsKey(realcorg))
            realcorg = (String) transitions.get(realcorg);
          
          if (!lookupData.getlookupData().raLocate(razvijeni,"CORG", realcorg)) {
            razvijeni.insertRow(false);
            razvijeni.setString("CORG", realcorg);
            razvijeni.setString("BROJKONTA", razvoj.getString("BROJKONTA"));
            razvijeni.setBigDecimal("ID", razvoj.getBigDecimal("ID"));
            razvijeni.setBigDecimal("IP", razvoj.getBigDecimal("IP"));
            razvijeni.setBigDecimal("POCID", Aus.zero2); //razvoj.getBigDecimal("POCID"));
            razvijeni.setBigDecimal("POCIP", Aus.zero2); //razvoj.getBigDecimal("POCIP"));
            razvijeni.setString("NK", getNazKOnto(razvoj.getString("BROJKONTA")));
            razvijeni.setString("GODMJ", razvoj.getString("GODMJ"));
          }
        }
      }
      if (stds.getString("BILANCA").equals("A") && stds.getString("ISPIS").equals("1")){
        razvijeni.setString("CORG", knjigDifolt);
      }
      razvijeni.setBigDecimal("SALDO", razvijeni.getBigDecimal("SALDO").add((razvijeni.getBigDecimal("POCIP").add(razvoj.getBigDecimal("IP"))).subtract(razvijeni.getBigDecimal("POCID").add(razvoj.getBigDecimal("ID")))));
    } while (razvoj.next());
    if (stds.getString("BILANCA").equals("S") || stds.getString("BILANCA").equals("K")){
      sumSame(razvijeni,true);
    }
    st.prn(razvijeni);
    return setPocAndProm(razvijeni,true);
  }

//  private QueryDataSet makeSomethingUsefull(QueryDataSet razvoj) {
////    System.out.println("\n");
////
////    System.out.println("stds.getString(\"BILANCA\") - " + stds.getString("BILANCA"));
////    System.out.println("stds.getString(\"ISPIS\") - " + stds.getString("ISPIS"));
////
////    System.out.println("\n");
//
//    QueryDataSet razvijeni = new QueryDataSet();
//    razvijeni.setColumns(razvoj.cloneColumns());
//    razvijeni.addColumn((Column)colNK.clone());
//    razvijeni.addColumn((Column) colPocID.clone());
//    razvijeni.addColumn((Column) colPocIP.clone());
//
//    dm.getKonta().refresh();
//
//    razvijeni.open();
//    razvoj.first();
//
// //    System.out.println("stds.getString(\"BILANCA\") = " + stds.getString("BILANCA"));
//    do {
//      /*if (stds.getString("BILANCA").equals("K") || stds.getString("BILANCA").equals("S")){
//        //jrbKomplBilanca.isSelected() || jrbSintetikBilanca.isSelected()){
//        for (int i = 1; i <= 3; i++) {
//          if (lookupData.getlookupData().raLocate(dm.getKonta(),"BROJKONTA",razvoj.getString("BROJKONTA").substring(0, i)) &&
//              dm.getKonta().getString("ISPISBB").equals("1")){
//
//            razvijeni.insertRow(false);
//            razvijeni.setString("CORG", razvoj.getString("CORG"));
//            razvijeni.setString("BROJKONTA", razvoj.getString("BROJKONTA").substring(0, i));
//            razvijeni.setBigDecimal("ID", razvoj.getBigDecimal("ID"));
//            razvijeni.setBigDecimal("IP", razvoj.getBigDecimal("IP"));
//            razvijeni.setBigDecimal("POCID", razvoj.getBigDecimal("POCID"));
//            razvijeni.setBigDecimal("POCIP", razvoj.getBigDecimal("POCIP"));
//            razvijeni.setString("NK", getNazKOnto(razvoj.getString("BROJKONTA").substring(0, i)));
//            razvijeni.setString("GODMJ", razvoj.getString("GODMJ"));
//          }
//        }
//      }*/
//      if (stds.getString("BILANCA").equals("K") || stds.getString("BILANCA").equals("A")){
//      //jrbKomplBilanca.isSelected() || jrbAnalitikBilanca.isSelected()){
//        if (lookupData.getlookupData().raLocate(dm.getKonta(),"BROJKONTA",razvoj.getString("BROJKONTA")) &&
//            dm.getKonta().getString("ISPISBB").equals("1")){
//
//          razvijeni.insertRow(false);
//          razvijeni.setString("CORG", razvoj.getString("CORG"));
//          razvijeni.setString("BROJKONTA", razvoj.getString("BROJKONTA"));
//          razvijeni.setBigDecimal("ID", razvoj.getBigDecimal("ID"));
//          razvijeni.setBigDecimal("IP", razvoj.getBigDecimal("IP"));
//          razvijeni.setBigDecimal("POCID", Aus.zero2); //razvoj.getBigDecimal("POCID"));
//          razvijeni.setBigDecimal("POCIP", Aus.zero2); //razvoj.getBigDecimal("POCIP"));
//          razvijeni.setString("NK", getNazKOnto(razvoj.getString("BROJKONTA")));
//          razvijeni.setString("GODMJ", razvoj.getString("GODMJ"));
//        }
//      }
//      if (stds.getString("BILANCA").equals("A") /*jrbAnalitikBilanca.isSelected()*/ && stds.getString("ISPIS").equals("1")){ //jrbSkupni.isSelected()) {
//        repDS2.setString("CORG", knjigDifolt);
//      }
//    } while (razvoj.next());
//    if (stds.getString("BILANCA").equals("S") || stds.getString("BILANCA").equals("K")){//jrbSintetikBilanca.isSelected() || jrbKomplBilanca.isSelected()) {
//      return sumSame(razvijeni);
//    }
// //    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
// //    syst.prn(razvijeni);
//    return razvijeni;
//  }

  private QueryDataSet deriveNumber(QueryDataSet original){
    QueryDataSet firstStep = new QueryDataSet();
    firstStep.setColumns(new Column[] {
      (Column) dm.getGkkumulativi().getColumn("CORG").clone(),
      (Column) dm.getGkkumulativi().getColumn("BROJKONTA").clone(),
      (Column) colNK.clone(),
      (Column) colPocID.clone(),
      (Column) colPocIP.clone(),
      dm.createBigDecimalColumn("ID","Duguje",2),
      dm.createBigDecimalColumn("IP","Potražuje",2),
      (Column) colSaldo.clone(),
      (Column) dm.getGkkumulativi().getColumn("GODMJ").clone()
    });
    firstStep.open();

 //    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
 //    syst.prn(original);

    int position = kontoPanel.getBrKonLength()+1;
    if (position == 4) position = 10;

    original.first();
    do {
      firstStep.insertRow(false);
      firstStep.setString("CORG",original.getString("REALCORG"));
      try {
        firstStep.setString("BROJKONTA",original.getString("BROJKONTA").substring(0,position));
      }
      catch (Exception ex) {
        firstStep.setString("BROJKONTA",original.getString("BROJKONTA"));
      }
      firstStep.setString("NK",getNazKOnto(original.getString("BROJKONTA"))); // was orginal.getString"BROJK....
      firstStep.setString("GODMJ",original.getString("GODMJ"));

  //      if (!original.getString("GODMJ").substring(4,6).equals("00")){
  //        firstStep.setBigDecimal("POCID",Aus.zero2);
  //        firstStep.setBigDecimal("POCIP",Aus.zero2);
        firstStep.setBigDecimal("ID",original.getBigDecimal("ID"));
        firstStep.setBigDecimal("IP",original.getBigDecimal("IP"));
        firstStep.setBigDecimal("SALDO",original.getBigDecimal("ID").subtract(original.getBigDecimal("IP")));
  //      } else {
  //        firstStep.setBigDecimal("POCID",original.getBigDecimal("ID"));
  //        firstStep.setBigDecimal("POCIP",original.getBigDecimal("IP"));
  //        firstStep.setBigDecimal("ID",Aus.zero2);
  //        firstStep.setBigDecimal("IP",Aus.zero2);
  //      }
    } while (original.next());
  //    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
  //    syst.prn(firstStep);
  //    return sumSame(firstStep);
  return setPocAndProm(firstStep,false);
  }

  private QueryDataSet setPocAndProm(QueryDataSet nebr, boolean crgs){
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.showInFrame(nebr,"a");
    nebr.first();
//    try {
      do {
        try {
          if (!nebr.getString("GODMJ").substring(4,6).equals("00")){
            nebr.setBigDecimal("POCID",Aus.zero2);
            nebr.setBigDecimal("POCIP",Aus.zero2);
            //        nebr.setBigDecimal("ID",nebr.getBigDecimal("ID"));
            //        nebr.setBigDecimal("IP",nebr.getBigDecimal("IP"));
          } else {
            nebr.setBigDecimal("POCID",nebr.getBigDecimal("ID"));
            nebr.setBigDecimal("POCIP",nebr.getBigDecimal("IP"));
            nebr.setBigDecimal("ID",Aus.zero2);
            nebr.setBigDecimal("IP",Aus.zero2);
          }
        } catch (Exception sxe){
          sxe.printStackTrace(); 
          
          System.out.println("nebr.getString(\"GODMJ\") = " + nebr.getString("GODMJ")); //XDEBUG delete when no more needed
        }
      } while (nebr.next());
//    }
//    catch (Exception ex) {
//    }
      return sumSame(nebr,crgs);
  }

  BigDecimal[] kumulativi = new BigDecimal[5];

  private QueryDataSet sumSame(QueryDataSet differenses,boolean crgs){
    QueryDataSet sumamed = new QueryDataSet();
    sumamed.setColumns(differenses.cloneColumns());
    sumamed.setRowId("BROJKONTA",true);
    sumamed.getColumn("CORG").setVisible(0);
    sumamed.getColumn("GODMJ").setVisible(0);
    sumamed.getColumn("NK").setVisible(0);
    sumamed.open();
    differenses.first();
    
//    if (stds.getString("ORGSTR").equals("1")) {
//      differenses.setString("CORG",knjigovodstvo);
//    }
    if (crgs){
    do {
      if (lookupData.getlookupData().raLocate(sumamed,new String[] {"BROJKONTA","CORG"},
          new String[] {differenses.getString("BROJKONTA"),differenses.getString("CORG")})){
        sumamed.setBigDecimal("POCID",sumamed.getBigDecimal("POCID").add(differenses.getBigDecimal("POCID")));
        sumamed.setBigDecimal("POCIP",sumamed.getBigDecimal("POCIP").add(differenses.getBigDecimal("POCIP")));
        sumamed.setBigDecimal("ID",sumamed.getBigDecimal("ID").add(differenses.getBigDecimal("ID")));
        sumamed.setBigDecimal("IP",sumamed.getBigDecimal("IP").add(differenses.getBigDecimal("IP")));
        sumamed.setBigDecimal("SALDO",sumamed.getBigDecimal("SALDO").add(differenses.getBigDecimal("SALDO")));
      } else {
        sumamed.insertRow(false);
        differenses.copyTo(sumamed);
      }
    } while (differenses.next());
    } else {
      do {
        if (lookupData.getlookupData().raLocate(sumamed,new String[] {"BROJKONTA"},
            new String[] {differenses.getString("BROJKONTA")})){
          sumamed.setBigDecimal("POCID",sumamed.getBigDecimal("POCID").add(differenses.getBigDecimal("POCID")));
          sumamed.setBigDecimal("POCIP",sumamed.getBigDecimal("POCIP").add(differenses.getBigDecimal("POCIP")));
          sumamed.setBigDecimal("ID",sumamed.getBigDecimal("ID").add(differenses.getBigDecimal("ID")));
          sumamed.setBigDecimal("IP",sumamed.getBigDecimal("IP").add(differenses.getBigDecimal("IP")));
          sumamed.setBigDecimal("SALDO",sumamed.getBigDecimal("SALDO").add(differenses.getBigDecimal("SALDO")));
        } else {
          sumamed.insertRow(false);
          differenses.copyTo(sumamed);
        }
      } while (differenses.next());
      }
    sumamed.setSort(new SortDescriptor(new String[] {"BROJKONTA"}));
    if (!doubleClicked && crgs){
//      repDS2 = sumamed;
      repSet = sumamed;
    }

  //    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
  //    syst.prn(sumamed);
  //
  //    BigDecimal nulica = Aus.zero2;
  //    BigDecimal suma = Aus.zero2;
  //    kumulativi[0] = nulica;
  //    kumulativi[1] = nulica;
  //    kumulativi[2] = nulica;
  //    kumulativi[3] = nulica;
  //    kumulativi[4] = nulica;
  //
  //    sumamed.first();
  //    do {
  //      kumulativi[0] = kumulativi[0].add(sumamed.getBigDecimal("POCID"));
  //      kumulativi[1] = kumulativi[1].add(sumamed.getBigDecimal("POCIP"));
  //      kumulativi[2] = kumulativi[2].add(sumamed.getBigDecimal("ID"));
  //      kumulativi[3] = kumulativi[3].add(sumamed.getBigDecimal("IP"));
  //    } while (sumamed.next());
  //    kumulativi[4] = (kumulativi[0].subtract(kumulativi[1])).add(kumulativi[2].subtract(kumulativi[3]));
    return sumamed;
  }

  protected String getBrKon(){
    if (kontoPanel.getBrojKonta().equals(kontoPanel.jlrKontoBroj.getText().trim())) return kontoPanel.getBrojKonta();
    else return "";
  }

  protected String getCorgs(String table){
    String sqlCorgString = "";
    if (stds.getString("ORGSTR").equals("1")){ //jrbOdabrana.isSelected()) {
      sqlCorgString = table+".CORG ='" + kontoPanel.getCorg() + "'";
    } else {
      StorageDataSet ojs = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(kontoPanel.getCorg());
      if (ojs.rowCount()==1) return table+".CORG ='" + ojs.getString("CORG").trim() + "'";
      sqlCorgString = Condition.in("CORG",ojs).qualified(table).toString();
    }
    return sqlCorgString;
  }

  public boolean modifyOutSet() {
    return true;
  }

  private String range(String god, String mjPoc, String mjZav) {
    Integer PocMj = new Integer(mjPoc);
    Integer ZavMj = new Integer(mjZav);
    String returnStr = "";
    String tempStr = "";

    for (int i = PocMj.intValue(); i <= ZavMj.intValue(); i++) {
      if (i < 10) {
        tempStr = "'" + god + "0" + i + "'";
      } else {
        tempStr = "'" + god + i + "'";
      }
      if (i < ZavMj.intValue()) {
        returnStr += tempStr + ", ";
      } else {
        returnStr += tempStr;
      }
    }
    return returnStr;
  }


  protected void llHandler() {
      kontoPanel.jlrKontoBroj.setText(ll.removeLast().toString().trim());
      kontoPanel.jlrKontoBroj.forceFocLost();
      kontoPanel.jlrCorg.setText(corgRemember);
      kontoPanel.jlrCorg.forceFocLost();
      this.getJPTV().enableEvents(false);

      ok_action();

//      okPress();
//      this.getOKPanel().jBOK_actionPerformed();
      this.getJPTV().getDataSet().goToRow(Integer.parseInt(pl.removeLast().toString()));
      this.getJPTV().enableEvents(true);
  }

//  public void keyF6Press(){
//    jptv_doubleClick();
//  }

  public void jptv_doubleClick() {
//    System.out.println("doubleclick - " + doubleClicked); //XDEBUG delete when no more needed
//    System.out.println("ispis now - " + ispisNow()); //XDEBUG delete when no more needed
//    System.out.println("is ispis - "+isIspis()); //XDEBUG delete when no more needed
    if (!doubleClicked)
      kontoPanel.setNoLookup(true);
    doubleClicked = true;
//    System.out.println("\n"+this.getJPTV().getDataSet().getString("BROJKONTA"));
    if (this.getJPTV().getDataSet().getString("BROJKONTA").length() < 4) {
      ll.addLast(this.kontoPanel.jlrKontoBroj.getText().trim());
      pl.addLast(this.getJPTV().getDataSet().getRow()+"");
      kontoPanel.jlrKontoBroj.setText(this.getJPTV().getDataSet().getString("BROJKONTA"));
      kontoPanel.jlrKontoBroj.forceFocLost();
      kontoPanel.setcORG(corgRemember);
      this.getJPTV().enableEvents(false);

      ok_action();

//      okPress();
//
//      this.getOKPanel().jBOK_actionPerformed();
      this.getJPTV().getDataSet().first();
      this.getJPTV().enableEvents(true);
    } else {
//      BigDecimal _nula = Aus.zero0;
//      if (this.getJPTV().getDataSet().getBigDecimal("ID").compareTo(_nula) == 0 && // .doubleValue() == 0 &&
//          this.getJPTV().getDataSet().getBigDecimal("IP").compareTo(_nula) == 0 && // doubleValue() == 0 &&
//          this.getJPTV().getDataSet().getBigDecimal("POCID").compareTo(_nula) == 0 && // doubleValue() == 0 &&
//          this.getJPTV().getDataSet().getBigDecimal("POCIP").compareTo(_nula) == 0) { // doubleValue() == 0) {
//        this.getJPTV().requestFocus();
//        JOptionPane.showConfirmDialog(null, "Nema podataka na analitièkim kontima!", "Poruka", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
//        return;
//      }
      frmKarticeGK fkgk = new frmKarticeGK(false,0);
      String konto = this.getJPTV().getDataSet().getString("BROJKONTA");
      fkgk.stds.open();
      fkgk.stds.setTimestamp("pocDatum", newDatumP());
      fkgk.stds.setTimestamp("zavDatum", newDatumZ());
      fkgk.setSljed("K");// jrbDatKnjiz.setSelected(true);
      fkgk.pack();
      fkgk.setTitle("Kartica glavne knjige za konto "+ konto);
      fkgk.kontoPanel.jlrKontoBroj.setText(konto);
      fkgk.kontoPanel.jlrKontoBroj.forceFocLost();
      fkgk.kontoPanel.jlrCorg.setText(corgRemember);
      fkgk.kontoPanel.jlrCorg.forceFocLost();
      //fkgk.jcbPrivremeni.setSelected(stds.getString("PRIVREMENO").equals("1"));//this.jcbPrivremeni.isSelected());
      fkgk.setPrivremeno(stds.getString("PRIVREMENO"));
      fkgk.setLocation(this.getWindow().getX(), this.getWindow().getY());
      fkgk.show();
      return;
    }
  }

  protected void addingReport(boolean rekapitulacija){
    if (isKompletBilanca){
    this.addReport("hr.restart.gk.repBrutoBilancaRP2", "hr.restart.gk.repBrutoBilanca", "BrutoBilancaRP2", "Bruto bilanca - Bez naziva - PS - Promet - Saldo");
    this.addReport("hr.restart.gk.repBrutoBilancaRP3", "hr.restart.gk.repBrutoBilanca", "BrutoBilancaRP3", "Bruto bilanca - Bez naziva - Uk. promet - Saldo");
    this.addReport("hr.restart.gk.repBrutoBilancaRP4", "hr.restart.gk.repBrutoBilanca", "BrutoBilancaRP4", "Bruto bilanca - Bez naziva - PS - Promet - Uk. saldo");
    this.addReport("hr.restart.gk.repBrutoBilancaRP1", "hr.restart.gk.repBrutoBilanca", "BrutoBilancaRP1", "Bruto bilanca - Sa nazivom - Uk. promet - Saldo ");
    this.addReport("hr.restart.gk.repBrutoBilancaRP5", "hr.restart.gk.repBrutoBilanca", "BrutoBilancaRP5", "Bruto bilanca - Sa nazivom - PS - Promet - Uk. saldo - 3 reda");
    this.addReport("hr.restart.gk.repBrutoBilancaRP6", "hr.restart.gk.repBrutoBilanca", "BrutoBilancaRP6", "Bruto bilanca - Sa nazivom konta - Saldo");
    } else {
      this.addReport("hr.restart.gk.repBrutoBilancaRP2", "hr.restart.gk.repBrutoBilanca", "BrutoBilancaRP2_old", "Bruto bilanca - Bez naziva - PS - Promet - Saldo");
      this.addReport("hr.restart.gk.repBrutoBilancaRP3", "hr.restart.gk.repBrutoBilanca", "BrutoBilancaRP3_old", "Bruto bilanca - Bez naziva - Uk. promet - Saldo");
      this.addReport("hr.restart.gk.repBrutoBilancaRP4", "hr.restart.gk.repBrutoBilanca", "BrutoBilancaRP4_old", "Bruto bilanca - Bez naziva - PS - Promet - Uk. saldo");
      this.addReport("hr.restart.gk.repBrutoBilancaRP1", "hr.restart.gk.repBrutoBilanca", "BrutoBilancaRP1_old", "Bruto bilanca - Sa nazivom - Uk. promet - Saldo ");
      this.addReport("hr.restart.gk.repBrutoBilancaRP5", "hr.restart.gk.repBrutoBilanca", "BrutoBilancaRP5_old", "Bruto bilanca - Sa nazivom - PS - Promet - Uk. saldo - 3 reda");
      this.addReport("hr.restart.gk.repBrutoBilancaRP6", "hr.restart.gk.repBrutoBilanca", "BrutoBilancaRP6_old", "Bruto bilanca - Sa nazivom konta - Saldo");
    }
    
    
    if(rekapitulacija){
      this.addReport("hr.restart.gk.repBrutoBilancaRP7", "hr.restart.gk.repBrutoBilancaRekapitulacija", "BrutoBilancaRP7", "Rekapitulacija - PS - Promet - Saldo");
      this.addReport("hr.restart.gk.repBrutoBilancaRP8", "hr.restart.gk.repBrutoBilancaRekapitulacija", "BrutoBilancaRP8", "Rekapitulacija - Uk. promet - Saldo");
      this.addReport("hr.restart.gk.repBrutoBilancaRP9", "hr.restart.gk.repBrutoBilancaRekapitulacija", "BrutoBilancaRP9", "Rekapitulacija - PS - Promet - Uk. saldo - 3 reda");
//      this.addReport("hr.restart.gk.repBrutoBilancaRP9", "hr.restart.gk.repBrutoBilancaRP9", "BrutoBilancaRP9", "Rekapitulacija - PS - Promet - Uk. saldo - 3 reda");
    }


//    this.addReport("hr.restart.gk.repBrutoBilancaRP2", "Bruto bilanca - Bez naziva - PS - Promet - Saldo", 2);
//    this.addReport("hr.restart.gk.repBrutoBilancaRP3", "Bruto bilanca - Bez naziva - Uk. promet - Saldo", 2);
//    this.addReport("hr.restart.gk.repBrutoBilancaRP4", "Bruto bilanca - Bez naziva - PS - Promet - Uk. saldo", 2);
//    this.addReport("hr.restart.gk.repBrutoBilancaRP1", "Bruto bilanca - Sa nazivom - Uk. promet - Saldo ", 2);
//    this.addReport("hr.restart.gk.repBrutoBilancaRP5", "Bruto bilanca - Sa nazivom - PS - Promet - Uk. saldo - 3 reda", 2);
//    this.addReport("hr.restart.gk.repBrutoBilancaRP6", "Bruto bilanca - Sa nazivom konta - Saldo", 2);
//    if(rekapitulacija){
//      this.addReport("hr.restart.gk.repBrutoBilancaRP7", "Rekapitulacija - PS - Promet - Saldo", 2);
//      this.addReport("hr.restart.gk.repBrutoBilancaRP8", "Rekapitulacija - Uk. promet - Saldo", 2);
//      this.addReport("hr.restart.gk.repBrutoBilancaRP9", "Rekapitulacija - PS - Promet - Uk. saldo - 3 reda", 2);
//    }
  }

  private void jbInit() throws Exception {
    kontoPanel.setNoLookup(false);
    setProgressIdleDelay(130);
    setStepsNumber(4);
    this.setJPan(jpDetail);
    jpDetail.setLayout(layDetail);

//    colMjP = dm.createStringColumn("jtMjesecPoc", "Po\u010Detni mjesec", 2);
//    colMjZ = dm.createStringColumn("jtMjesecZav", "Krajnji mjesec", 2);
//    colGod = dm.createStringColumn("jtGodina", "Godina", 4);

    stds.setColumns(new Column[]{dm.createStringColumn("jtMjesecPoc", "Po\u010Detni mjesec", 2),
                                 dm.createStringColumn("jtMjesecZav", "Krajnji mjesec", 2),
                                 dm.createStringColumn("jtGodina", "Godina", 4),
                                 dm.createStringColumn("PRIVREMENO",1),
                                 dm.createStringColumn("ORGSTR",1),
                                 dm.createStringColumn("ISPIS",1),
                                 dm.createStringColumn("BILANCA",1)
    });

    rcbPrivremenost.setDataSet(stds);
    rcbPrivremenost.setRaColumn("PRIVREMENO");
    rcbPrivremenost.setRaItems(new String[][] {
      {"Knjiženi","0"},
      {"Svi","1"}
    });

    rcbPripOrgJed.setDataSet(stds);
    rcbPripOrgJed.setRaColumn("ORGSTR");
    rcbPripOrgJed.setRaItems(new String[][] {
      {"Cijela struktura organizacijske jedinice" ,"0"},
      {"Odabrana organizacijska jedinica ","1"}
    });
//    rcbPripOrgJed.addItemListener(new java.awt.event.ItemListener() {
//      public void itemStateChanged(java.awt.event.ItemEvent e) {
//        corging();
//      }
//    });

    rcbIspis.setDataSet(stds);
    rcbIspis.setRaColumn("ISPIS");
    rcbIspis.setRaItems(new String[][] {
      {"Skupno","1"},
      {"Pojedinaèno" ,"0"}
    });

    rcbBilanca.setDataSet(stds);
    rcbBilanca.setRaColumn("BILANCA");
    rcbBilanca.setRaItems(new String[][] {
      {"Kompletna" ,"K"},
      {"Sintetièka","S"},
      {"Analitièka","A"}
    });

//    column1 = dm.createTimestampColumn("pocDatum", "Po\u010Detni Datum");
//    column2 = dm.createTimestampColumn("zavDatum", "Krajnji datum");
//    stdsdates.setColumns(new Column[]{column1, column2});
    colSaldo = dm.createBigDecimalColumn("SALDO", "Saldo", 2);
    colPocID = dm.createBigDecimalColumn("POCID", "Po\u010Detno duguje", 2);
    colPocIP = dm.createBigDecimalColumn("POCIP", "Po\u010Detno potražuje", 2);
    colNK = dm.createStringColumn("NK", 0);

    jtMjesecPoc.setHorizontalAlignment(SwingConstants.CENTER);
    jtMjesecPoc.setColumnName("jtMjesecPoc");
    jtMjesecPoc.setDataSet(stds);

    jtMjesecZav.setHorizontalAlignment(SwingConstants.CENTER);
    jtMjesecZav.setColumnName("jtMjesecZav");
    jtMjesecZav.setDataSet(stds);

    jtGodina.setHorizontalAlignment(SwingConstants.CENTER);
    jtGodina.setDataSet(stds);
    jtGodina.setColumnName("jtGodina");

    border1 = new EtchedBorder(EtchedBorder.RAISED, new Color(224, 255, 255), new Color(109, 129, 140));
    border2 = BorderFactory.createEtchedBorder(new Color(224, 255, 255), new Color(109, 129, 140));
//    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(new Color(224, 255, 255), new Color(109, 129, 140)), "");

//    jcbPrivremeni.setHorizontalAlignment(SwingConstants.RIGHT);
//    jcbPrivremeni.setHorizontalTextPosition(SwingConstants.LEADING);
//    jcbPrivremeni.setText("Privremeni");
    layDetail.setWidth(578);
    layDetail.setHeight(358);
    jlPeriod.setText("Period (mm - mm gggg)");
//    kontoPanel.jlrKontoBroj.setRaDataSet(kontoPanel.getDohvatKonta(""));
    jLabel2.setText(" -");
//    jPanel1.setLayout(xYLayout2);
//    jPanel1.setBorder(titledBorder1);
//    jPanel2.setBorder(titledBorder1);
//    jPanel2.setLayout(xYLayout1);
    jLabel1.setText("Dokumenti / Org. str.");
//    jLabel3.setText("Izbor bilance");
    jpDetail.setMinimumSize(new Dimension(600, 358));
    jpDetail.setPreferredSize(new Dimension(580, 160));
    jLabel4.setText("Izbor bilance / izvješ\u0107a");
    selOrg.setIcon(raImages.getImageIcon(raImages.IMGOPEN));
    selOrg.setToolTipText("Izbor organizacijskih jedinica za grupiranje");
    selOrg.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (kontoPanel.getCorg().length() == 0)
          kontoPanel.jlrCorg.requestFocusLater();
        else dlgCompanyTree.get("bruto-bilanca").show(getWindow(), kontoPanel.getCorg(), 
            "Izbor organizacijskih jedinica za grupiranje");
      }
    });
//    jPanel3.setBorder(titledBorder1);
//    jPanel3.setLayout(xYLayout3);
    kontoPanel.setPreferredSize(new Dimension(580, 50));
//    buttonGroup1.setHorizontalTextPosition(SwingConstants.TRAILING);
//    buttonGroup2.setHorizontalTextPosition(SwingConstants.TRAILING);
//    buttonGroup3.setHorizontalTextPosition(SwingConstants.TRAILING);
//    buttonGroup1.add(jrbCijela, "Cijela struktura");
//    buttonGroup1.add(jrbOdabrana, "Odabrana jedinica");
//    buttonGroup2.add(jrbKomplBilanca, "Kompletna");
//    buttonGroup2.add(jrbSintetikBilanca, "Sinteti\u010Dka");
//    buttonGroup2.add(jrbAnalitikBilanca, "Analiti\u010Dka");
//    buttonGroup3.add(jrbSkupni, "Skupni ispis");
//    buttonGroup3.add(jrbPojedinacni, "Pojedina\u010Dni ispis po Org. jed.");
    jpDetail.add(kontoPanel, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlPeriod, new XYConstraints(15, 70, -1, -1));

    jpDetail.add(rcbPripOrgJed, new XYConstraints(255,95,285,-1));
    jpDetail.add(rcbPrivremenost, new XYConstraints(150,95,100,-1));
    jpDetail.add(jLabel1,   new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jLabel4,   new XYConstraints(15, 120, -1, -1));
    jpDetail.add(rcbBilanca, new XYConstraints(150,120,100,-1));
    jpDetail.add(rcbIspis, new XYConstraints(255,120,100,-1));
    jpDetail.add(selOrg, new XYConstraints(360,120,21,21));
//    jpDetail.add(jLabel3,   new XYConstraints(15, 145, -1, -1));

//    jpDetail.add(jcbPrivremeni, new XYConstraints(455, 70, -1, -1));

// -------------------------------------------------------------------------
//    jPanel1.add(jrbCijela, new XYConstraints(15, 0, -1, -1));
//    jPanel1.add(jrbOdabrana, new XYConstraints(145, 0, -1, -1));

//    jPanel2.add(jrbKomplBilanca, new XYConstraints(15, 0, -1, -1));
//    jPanel2.add(jrbSintetikBilanca, new XYConstraints(145, 0, -1, -1));
//    jPanel2.add(jrbAnalitikBilanca, new XYConstraints(270, 0, -1, -1));

//    jPanel3.add(jrbSkupni, new XYConstraints(15, 0, -1, -1));
//    jPanel3.add(jrbPojedinacni, new XYConstraints(145, 0, -1, -1));

//    jpDetail.add(jPanel1, new XYConstraints(150, 95, 390, -1));
//    jpDetail.add(jPanel2, new XYConstraints(150, 175, 390, -1));
//    jpDetail.add(jPanel3, new XYConstraints(150, 135, 390, -1));
// -------------------------------------------------------------------------

    jpDetail.add(jtMjesecPoc, new XYConstraints(150, 70, 35, -1));
    jpDetail.add(jtMjesecZav, new XYConstraints(215, 70, 35, -1));
    jpDetail.add(jtGodina, new XYConstraints(255, 70, 50, -1));
    jpDetail.add(jLabel2, new XYConstraints(195, 70, 14, -1));
    this.installResetButton();
    this.getJPTV().addTableColorModifier();
  }

  public DataSet getRep1QDS() {
    return repSet;
  }

  public BigDecimal[] getKumulative(){
    return kumulativi;
  }

  private boolean checkMonthPoc() {
    if (jtMjesecPoc.getText().equals("")) {
      return true;
    }
    try {
      int i = Integer.parseInt(jtMjesecPoc.getText());
      return (i < 13);
    } catch (Exception ex) {
      return false;
    }
  }
  private boolean checkMonthZav() {
    if (jtMjesecZav.getText().equals("")) {
      return true;
    }
    try {
      int i = Integer.parseInt(jtMjesecZav.getText());
      return (i < 13);
    } catch (Exception ex) {
      return false;
    }
  }
   // stds.getString("jtGodina"), stds.getString("jtMjesecPoc"), stds.getString("jtMjesecZav")

  public Timestamp newDatumP() {
    String pocetni;
    if (stds.getString("jtMjesecPoc").equals("00")) {
      pocetni = stds.getString("jtGodina") + "-01-01 00:00:00.000";
    } else {
      pocetni = stds.getString("jtGodina") + "-" + stds.getString("jtMjesecPoc") + "-01 00:00:00.000";
    }
//    System.out.println("pocetni "+pocetni);
    Timestamp dp = Timestamp.valueOf(pocetni);
    return util.getFirstDayOfMonth(dp);
  }

  public Timestamp newDatumZ() {
    String zadnji = stds.getString("jtGodina").trim() + "-" + stds.getString("jtMjesecZav") + "-27 23:59:00.000";
//    System.out.println("zadnji " + zadnji);
    Timestamp dz = Timestamp.valueOf(zadnji);
    return util.getLastDayOfMonth(dz);
  }

  public void showDefaultValues() {
//    System.out.println("Showing defoult walues....");
    
//    rcc.setLabelLaF(kontoPanel.jlrCorg, false);
//    rcc.setLabelLaF(kontoPanel.jlrNazorg, false);
//    rcc.setLabelLaF(kontoPanel.jbSelCorg, false);
//    kontoPanel.jlrKontoBroj.setText("");
//    kontoPanel.jlrKontoBroj.emptyTextFields();
//    buttonGroup1.setSelected(jrbCijela);
//    buttonGroup2.setSelected(jrbKomplBilanca);
//    buttonGroup3.setSelected(jrbSkupni);

    rcbPrivremenost.setSelectedIndex(0);
    rcbPripOrgJed.setSelectedIndex(0);
    rcbIspis.setSelectedIndex(0);
    rcbBilanca.setSelectedIndex(0);

    stds.setString("PRIVREMENO","0");
    stds.setString("ORGSTR","0");
    stds.setString("ISPIS","1");
    stds.setString("BILANCA","K");

//    jcbPrivremeni.setSelected(false);

//    SwingUtilities.invokeLater(new Runnable() {
//      public void run() {
        kontoPanel.jlrKontoBroj.setText("");
        kontoPanel.jlrKontoBroj.emptyTextFields();
        kontoPanel.jlrKontoBroj.requestFocus();
//        System.out.println("E sad bi tribalo radit lookup....");
//      }
//    });
//    kontoPanel.jlrKontoBroj_lookup();
//    makeRDS = true;
    
  }

  public void initialValues() {
    knjigDifolt = hr.restart.zapod.OrgStr.getKNJCORG();
    kontoPanel.setcORG(knjigDifolt);
    stds.setString("jtMjesecPoc", "00");
    stds.setString("jtMjesecZav", util.getMonth(vl.getToday()));
    stds.setString("jtGodina", vl.findYear(vl.getToday()));
//    rcc.setLabelLaF(kontoPanel.jlrCorg, false);
//    rcc.setLabelLaF(kontoPanel.jlrNazorg, false);
//    rcc.setLabelLaF(kontoPanel.jbSelCorg, false);
//    buttonGroup1.setSelected(jrbCijela);
//    buttonGroup2.setSelected(jrbKomplBilanca);
//    buttonGroup3.setSelected(jrbSkupni);
    
//    rcbPrivremenost.setSelectedIndex(0);
//    jcbPrivremeni.setSelected(false);
    kontoPanel.jlrKontoBroj.setText("");
    kontoPanel.jlrKontoBroj.emptyTextFields();
    kontoPanel.jlrKontoBroj.requestFocus();
    showDefaultValues();
  }

  public String getCORG() {
    return kontoPanel.jlrCorg.getText();
  }

  public String getNAZORG() {
    return kontoPanel.jlrNazorg.getText();
  }

  public boolean getPRIVREMENI() {
    return stds.getString("PRIVREMENO").equals("1");//jcbPrivremeni.isSelected();
  }

  public String getBILANCA() {
    return stds.getString("BILANCA");
//    if (jrbAnalitikBilanca.isSelected()) {
//      return "A";
//    }
//    if (jrbSintetikBilanca.isSelected()) {
//      return "S";
//    }
//    return "K";
  }

  public String getSKUPNI() {
    if (stds.getString("ISPIS").equals("1")){ //jrbSkupni.isSelected()) {
      return "ZBIRNO";
    }
    return "CORG";
  }

  public String getNazKOnto(String cKonto) {
    lookupData.getlookupData().raLocate(kontaSet,"BROJKONTA",cKonto);
    return kontaSet.getString("NAZIVKONTA");
  }

  JLabel jLabel1 = new JLabel();
//  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  XYLayout xYLayout3 = new XYLayout();

  public QueryDataSet getBrutoBilDS2() {
    if (isKompletBilanca) return repDS2;
    return repSet;
  }

  public String getGroupMode(){
//    System.out.println("ISPIS _ " + stds.getString("ISPIS")); //XDEBUG delete when no more needed
    return stds.getString("ISPIS");
  }

  public void corging(){
//    rcc.setLabelLaF(kontoPanel.jlrCorg, stds.getString("ORGSTR").equals("1"));
//    rcc.setLabelLaF(kontoPanel.jlrNazorg, stds.getString("ORGSTR").equals("1"));
//    rcc.setLabelLaF(kontoPanel.jbSelCorg, stds.getString("ORGSTR").equals("1"));

    if (stds.getString("ORGSTR").equals("1")) {
      kontoPanel.jlrCorg.requestFocus();
    } else {
      kontoPanel.setcORG(knjigDifolt);
      rcbPripOrgJed.requestFocus();
    }

//    rcbPripOrgJed.setDataSet(stds);
//    rcbPripOrgJed.setRaColumn("ORGSTR");

  }
}






/*
 * ovo je pomoæ iz nove BB
 * 
 * 
 * if (!doubleClicked) {
  repDS2 = getValutaSet(makeSetForNewIspis(qds));
  repSet = makeSomethingUsefull(qds2);

  repSet.first();

  BigDecimal nulica = Aus.zero2;
  kumulativi[0] = nulica;
  kumulativi[1] = nulica;
  kumulativi[2] = nulica;
  kumulativi[3] = nulica;
  kumulativi[4] = nulica;

  repDS2.first();
  do {
    kumulativi[0] = kumulativi[0].add(repDS2.getBigDecimal("POCID"));
    kumulativi[1] = kumulativi[1].add(repDS2.getBigDecimal("POCIP"));
    kumulativi[2] = kumulativi[2].add(repDS2.getBigDecimal("ID"));
    kumulativi[3] = kumulativi[3].add(repDS2.getBigDecimal("IP"));
  } while (repDS2.next());
  kumulativi[4] = (kumulativi[0].subtract(kumulativi[1])).add(kumulativi[2].subtract(kumulativi[3]));

  isKompletBilanca = stds.getString("BILANCA").equals("K");

  killAllReports();
  addingReport(kontoPanel.jlrKontoBroj.getText().equals(""));
}*/

