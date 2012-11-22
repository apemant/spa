/****license*****************************************************************
**   file: frmBrBilAll.java
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
import hr.restart.baza.Konta;
import hr.restart.baza.dM;
import hr.restart.robno.raDateUtil;
import hr.restart.robno.rdUtil;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.Stopwatch;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.dlgCompanyTree;
import hr.restart.util.raComboBox;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.raUpitFat;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKonta;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * BB complete new
 */

public class frmBrBilAll extends raUpitFat {
  dM dm = dM.getDataModule();
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.util.Util util = hr.restart.util.Util.getUtil();
  Valid vl = Valid.getValid();
  Map sume = new HashMap();

  private boolean doubleClicked = false;
  
  raPanKonto kontoPanel = new raPanKonto();
  hr.restart.zapod.jpGetValute jpVal = new hr.restart.zapod.jpGetValute();

  LinkedList ll = new LinkedList();
  LinkedList pl = new LinkedList();
  
  JPanel jpDetail = new JPanel();
  JPanel jpPeriodBB = new JPanel();
  JPanel jpPeriodBP = new JPanel();
  
  XYLayout layDetail = new XYLayout();
  XYLayout layPeriod = new XYLayout();

  private String knjigDifolt;
  private String modeCache = "";
  protected String corgRemember = "";
  private String domaval = "";
    
  //private Map nazKonta;
  //private int kontaSerial = -1;
  private boolean bbKum;
  
  private int seekPos = -1;
  private String seekKonto = null;
  
  private BigDecimal[] kumulativi = new BigDecimal[5];

  protected raNavAction rnvAnotherClickinTheWall = new raNavAction("Razvoj grupa", 
      raImages.IMGALLFORWARD, java.awt.event.KeyEvent.VK_F8){
    public void actionPerformed(java.awt.event.ActionEvent e){
      System.out.println("F8 pressed..."); //XDEBUG delete when no more needed
      shifts("right");
    }
  };
  
  protected raNavAction rnvAnotherBackwardClickinTheWall = new raNavAction("Kolaps grupa", 
      raImages.IMGALLBACK, java.awt.event.KeyEvent.VK_F7){
    public void actionPerformed(java.awt.event.ActionEvent e){
      System.out.println("F7 pressed..."); //XDEBUG delete when no more needed
      shifts("left");
    }
  };
  
  protected raNavAction rnvShow = new raNavAction("Prikaži u tablici", raImages.IMGHISTORY, KeyEvent.VK_V, KeyEvent.ALT_MASK) {
    public void actionPerformed(java.awt.event.ActionEvent e){
      showTable();
    }
  };
  
  protected void addNavBarOptions(){
    super.addNavBarOptions();
    this.getJPTV().getNavBar().addOption(rnvShow, 2);
    this.getJPTV().getNavBar().addOption(rnvAnotherBackwardClickinTheWall, 3);
    this.getJPTV().getNavBar().addOption(rnvAnotherClickinTheWall, 4);
  }

  public void navbarremoval(){
    super.navbarremoval();
    try{
      this.getJPTV().getNavBar().removeOption(rnvShow);
      this.getJPTV().getNavBar().removeOption(rnvAnotherBackwardClickinTheWall);
      this.getJPTV().getNavBar().removeOption(rnvAnotherClickinTheWall);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  raComboBox rcbMode = new raComboBox();
  raComboBox rcbPrivremenost = new raComboBox();
//  raComboBox rcbPripOrgJed = new raComboBox();
//  raComboBox rcbIspis = new raComboBox();
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
  raComboBox rcbPocStanje = new raComboBox();

  // BB
  JraTextField jtGodina = new JraTextField();
  JraTextField jtMjesecPoc = new JraTextField() {
    public boolean maskCheck() {
      if (super.maskCheck()) {
        if (!checkMonthValidity(this)) {
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
        if (!checkMonthValidity(this)) {
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
  ///BB
  //BP
  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfZavDatum = new JraTextField();
  ///BP
  StorageDataSet stds = new StorageDataSet();
  //private QueryDataSet kontaSet = null;
  private QueryDataSet repKontaSet;
  private QueryDataSet repSintetikAnalitikSet;
  private QueryDataSet repRekapitulacijaSet = null;
  private QueryDataSet repPoNalozimaSet = null;
  
  private static frmBrBilAll instanceOfMe = null;
  
  public static frmBrBilAll getInstance(){
    if (instanceOfMe == null) instanceOfMe = new frmBrBilAll();
    return instanceOfMe;
  }
  
  public frmBrBilAll() {
    //Mounted on frmGK.java - temporarly
    try {
      initializer();
      instanceOfMe = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  ///BP4
  
  JraButton selOrg = new JraButton();
  
  boolean isTreeSelected() {
    System.out.println("is Tree Selected - " + (stds.getString("ISPIS").equals("0") && stds.getString("ORGSTR").equals("0"))); //XDEBUG delete when no more needed
    return stds.getString("ISPIS").equals("0") && stds.getString("ORGSTR").equals("0");
  }
  
  protected void updateSelectTreeButton() {
    selOrg.setEnabled(isTreeSelected());
  }
  
  /// antonijeve berzerkarije :D
  
  private boolean firstTime = true;
  
  public void resetDefaults() {
    super.resetDefaults();
    initialValues();
  }
  
  
  boolean showzero = true;
  public int sind = 3;
  public void componentShow() {
    //kontaSet = dm.getKonta();
    //kontaSet.open();
    if (firstTime) {
      initialValues();
      modeCache = incKum ? "BB" : "BP";
    }
    showzero = "D".equalsIgnoreCase(frmParam.getParam("gk", "showZeroBB", "D",
        "Prikazati na bruto bilanci i ona konta koja imaju ID=IP=0 (D,N)"));
    String digs = frmParam.getParam("gk", "sinDigits", "3",
        "Broj znamenki sintetièkih konta");
    sind = Aus.getNumber(digs);
    if (sind < 3 || sind > 4) sind = 3;
      
    firstTime = false;
    updateSelectTreeButton();
  }
  
  public String navDoubleClickActionName() {
    return "Detaljnije";
  }
  
  private int[] visibleColumns = new int[]{0,1,2,3,4,5};

  public int[] navVisibleColumns(){
    return visibleColumns;
  }

  public void initialValues() {
    knjigDifolt = hr.restart.zapod.OrgStr.getKNJCORG();
    /*modeCache = "BB";*/
    kontoPanel.setcORG(knjigDifolt);
    
    stds.setString("POCMJ", "01");
    stds.setString("ZAVMJ", util.getMonth(vl.getToday()));
    stds.setString("GODINA", vl.findYear(vl.getToday()));
    stds.setTimestamp("POCDAT", hr.restart.util.Util.getUtil().getFirstDayOfYear());
    stds.setTimestamp("ZAVDAT", hr.restart.util.Util.getUtil().getLastDayOfMonth());
    
    stds.setString("MODE",incKum ? "BB" : "BP");
    rcbMode.setSelectedIndex(0);
    modeChanged();
    updateSelectTreeButton();
    
    domaval = hr.restart.zapod.Tecajevi.getDomOZNVAL();
    jpVal.jtOZNVAL.setText(domaval);
    jpVal.jtOZNVAL.forceFocLost();
    showDefaultValues();
  }

/*  private void initNazKonta() {
    DataSet kon = dm.getKonta();
    kon.open();
    int newSerial = dM.getSynchronizer().getSerialNumber("konta");
    
    if (nazKonta == null || kontaSerial != newSerial) {
      kontaSerial = newSerial;
      if (nazKonta != null) nazKonta.clear();
      else nazKonta = new HashMap();
      for (kon.first(); kon.inBounds(); kon.next())
        nazKonta.put(kon.getString("BROJKONTA"), kon.getString("NAZIVKONTA"));
    }
  }*/
  
  public void showDefaultValues() {
    handleEnablement();

    rcbPrivremenost.setSelectedIndex(0);
    rcbPripOrgJed.setSelectedIndex(0);
    rcbIspis.setSelectedIndex(0);
    rcbBilanca.setSelectedIndex(0);
    rcbPocStanje.setSelectedIndex(0);

    stds.setString("PRIVREMENO","0");
    stds.setString("ORGSTR","0");
    stds.setString("ISPIS","1");
    stds.setString("BILANCA","K");
    stds.setString("PS","D");

    kontoPanel.jlrKontoBroj.setText("");
    kontoPanel.jlrKontoBroj.emptyTextFields();
    kontoPanel.jlrKontoBroj.requestFocus();
        
    setDataSet(null);
  }

  private void handleEnablement() {
    rcc.EnabDisabAll(jpDetail, true);
//    rcc.setLabelLaF(kontoPanel.jlrCorg, false);
//    rcc.setLabelLaF(kontoPanel.jlrNazorg, false);
//    rcc.setLabelLaF(kontoPanel.jbSelCorg, false);
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
    if (ll.isEmpty()) {
      if (entered){
        doubleClicked = false;
        kontoPanel.setNoLookup(false);
        entered = false;
        handleEnablement();

        kontoPanel.jlrKontoBroj.requestFocusLater();
        
        setDataSet(null);
        updateSelectTreeButton();
        removeNav();
        isShifted = false;
        resetTitle();
      } else if (kontoPanel.jlrKontoBroj.getText().length() > 0) {
        kontoPanel.jlrKontoBroj.setText("");
        kontoPanel.jlrKontoBroj.emptyTextFields();
        kontoPanel.jlrKontoBroj.requestFocusLater();
      }
    } else {
      llHandler();
    }
  }

  private void resetTitle() {
    setTitle("Bruto bilanca");
  }
  private void setTitlePer() {
    setTitle("Bruto bilanca za period "
        +raDateUtil.getraDateUtil().dataFormatter(stds.getTimestamp("POCDAT"))+" - "
        +raDateUtil.getraDateUtil().dataFormatter(stds.getTimestamp("ZAVDAT")));
  }

  public void cancelPress() {
//    doubleClicked = false;
//    kontoPanel.setNoLookup(false);
//    if (!ll.isEmpty()) {
//      ll.clear();
//    }
    showDefaultValues();
    super.cancelPress();
    pl.clear();
    ll.clear();
  }

  boolean entered = false;
  Stopwatch sok = null;
  public void okPress() {
    bbKum = stds.getString("MODE").equals("BB");
    
    sok = Stopwatch.start("okPress");
    entered = true;
    corgRemember = kontoPanel.getCorg(); //hr.restart.zapod.OrgStr.getOrgStr().getPripKnjig(kontoPanel.getCorg());

    //QueryDataSet qds = util.getNewQueryDataSet(getQdsString(hr.restart.zapod.OrgStr.getOrgStr().getPripKnjig(kontoPanel.getCorg())));
//    QueryDataSet qds2 = util.getNewQueryDataSet(getQdsString(hr.restart.zapod.OrgStr.getOrgStr().getPripKnjig(kontoPanel.getCorg())));
    checkClosing();
    List data = loadQuery(getQdsString(OrgStr.getOrgStr().getPripKnjig(kontoPanel.getCorg())));
    sok.report("opened data");

    if (data.size() == 0)
      setNoDataAndReturnImmediately();
      
    
    checkClosing();

    if (stds.getString("PS").equals("D")){
      visibleColumns = new int[]{0,1,2,4,5,9};
    } else if (stds.getString("PS").equals("P")){
      visibleColumns = new int[]{0,1,2,7};
    } else {
      visibleColumns = new int[]{0,4,5,7};
    }
    
    if (!isShifted){
      handleDoubleClickIcon(true);
      if (!doubleClicked){
        handleArrowExpand(true);
        handleArrowColapse(false);
      } else {
        handleArrowExpand(false);
        handleArrowColapse(false);
      }
      addingReport();
      okPressActions(data);
    } else {
      QueryDataSet prik = deriveRawSetShifts(data, level, false);
      positionDataSet(prik);
      setDataSetAndSums(prik, new String[] {"POCID","POCIP","SALPS","ID","IP","SALPROM","DUG","POT","SALDO"});
    }
    setTitlePer();
    sok.report("over");
  }
  

  
  private QueryDataSet deriveRawSetShifts(List data, int digits, boolean forPrint) {
/*    int digitala = digits;
    if (digits > 3) digitala = 100; 
    QueryDataSet forReturn = new QueryDataSet();
    forReturn.setColumns(new Column[] {
        (Column) dm.getGkkumulativi().getColumn("CORG").clone(),
        (Column) dm.getGkkumulativi().getColumn("BROJKONTA").clone(),
        dm.createStringColumn("NK", 0),
        dm.createBigDecimalColumn("POCID", "Po\u010DetnoD", 2),
        dm.createBigDecimalColumn("POCIP", "Po\u010DetnoP", 2),
        dm.createBigDecimalColumn("SALPS","SaldoPS",2),
        dm.createBigDecimalColumn("ID","PrometD",2),
        dm.createBigDecimalColumn("IP","PrometP",2),
        dm.createBigDecimalColumn("SALPROM","SaldoPR",2),
        dm.createBigDecimalColumn("DUG","DugujeUK",2),
        dm.createBigDecimalColumn("POT","PotražujeUK",2),
        dm.createBigDecimalColumn("SALDO","SaldoUK",2),
        (Column) dm.getGkkumulativi().getColumn("GODMJ").clone(),
        (Column) dm.getGkstavke().getColumn("CVRNAL").clone()
    });
    forReturn.open(); */
    List ret = new ArrayList();
    for (int i = 0; i < data.size(); i++) {
      MainQueryData mr = (MainQueryData) data.get(i);
      FormData fr = new FormData();
      fr.corg = mr.corg;
      if (digits > sind || mr.brojkonta.length() < digits)
        fr.brojkonta = mr.brojkonta;
      else fr.brojkonta = mr.brojkonta.substring(0, digits);
      fr.nk = raKonta.getNazivKonta(fr.brojkonta);
      fr.godmj = fr.cvrnal = "";
      if (bbKum) fr.godmj = mr.godmj;
      else fr.cvrnal = mr.godmj;
      
      if ((bbKum && !fr.godmj.endsWith("00")) ||
          (!bbKum && !fr.cvrnal.equals("00"))) {
         fr.id = mr.id;
         fr.ip = mr.ip;
         fr.pocid = fr.pocip = Aus.zero2;
      } else {
        fr.pocid = mr.id;
        fr.pocip = mr.ip;
        fr.id = fr.ip = Aus.zero2;
      }
      ret.add(fr);
    }
    
    /*rawset.first();
    do {
      forReturn.insertRow(false);
      forReturn.setString("CORG",rawset.getString("REALCORG"));
      try {
        forReturn.setString("BROJKONTA",rawset.getString("BROJKONTA").substring(0,digitala));
      }
      catch (Exception ex) {
        forReturn.setString("BROJKONTA",rawset.getString("BROJKONTA"));
      }
      forReturn.setString("NK",getNazKonto(rawset.getString("BROJKONTA"))); // was orginal.getString"BROJK....
      try {
      forReturn.setString("GODMJ",rawset.getString("GODMJ"));
      } catch (Exception e) {
        forReturn.setString("CVRNAL",rawset.getString("CVRNAL"));
      }
      
      forReturn.setBigDecimal("ID",rawset.getBigDecimal("ID"));
      forReturn.setBigDecimal("IP",rawset.getBigDecimal("IP"));
      forReturn.setBigDecimal("DUG",rawset.getBigDecimal("ID"));
      forReturn.setBigDecimal("POT",rawset.getBigDecimal("IP"));
      forReturn.setBigDecimal("SALDO",rawset.getBigDecimal("ID").subtract(rawset.getBigDecimal("IP")));
    } while (rawset.next());
    
      return setPocAndProm(forReturn, forPrint);*/
    checkClosing();
    return sumSame(ret, forPrint);
  }

  private void okPressActions(List data) {
    QueryDataSet jptvov = deriveRawSetShifts(data, kontoPanel.getBrKonLength() + 1, false);
    checkClosing();
    sok.report("after derived");
    
    if (!doubleClicked) {

//      sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
      if (isKompletnaBilanca()){
        repKontaSet = makeSetForNewIspis(data);
        repKontaSet.setSort(new SortDescriptor(new String[] {"CORG", "BROJKONTA"}));
        sok.report("after kompl bilanca");
//        st.showInFrame(repKontaSet,"KONTA SET");
      } else {
        repSintetikAnalitikSet = getClassedBilance(sumKontaCorg(data, 0, false));
        repSintetikAnalitikSet.setSort(new SortDescriptor(new String[] {"CORG", "BROJKONTA"}));
        sok.report("after classed bilanca");
//        st.showInFrame(repSintetikAnalitikSet,"KLASSED SET");
      }
      
      if (!bbKum) {
        repPoNalozimaSet = valutaConvertedSet(makeSetIspisPoNalozima(data));
        repPoNalozimaSet.setSort(new SortDescriptor(new String[] {"CORG", "BROJKONTA"}));
        sok.report("after po nalozima bilanca");
      }
      
      if (kontoPanel.jlrKontoBroj.getText().length() < sind) { // rekapitulacija
        repRekapitulacijaSet = valutaConvertedSet(makeRekapitulacija(data));
        repRekapitulacijaSet.setSort(new SortDescriptor(new String[] {"CORG", "BROJKONTA"}));
        sok.report("after rekap bilanca");
      }
    }
    
    positionDataSet(jptvov);
    setDataSetAndSums(jptvov, new String[] {"POCID","POCIP","SALPS","ID","IP","SALPROM","DUG","POT","SALDO"});
  }
  
  private void positionDataSet(DataSet ds) {
    if (seekPos >= 0) {
      ds.goToClosestRow(seekPos);
      seekPos = -1;
    } else if (seekKonto != null) {
      for (ds.first(); ds.inBounds(); ds.next())
        if (ds.getString("BROJKONTA").startsWith(seekKonto) ||
            seekKonto.startsWith(ds.getString("BROJKONTA"))) break;
      if (!ds.inBounds()) ds.first();
      seekKonto = null;
    } else ds.first();
  }
  
  void showTable() {
    StorageDataSet ds = isKompletnaBilanca() ? 
        getBroutoBilancaReportSet() : getSintetikAnalitikSet(); 

    if (ds.hasColumn("GODMJ") != null) 
      ds.getColumn("GODMJ").setVisible(0);
    if (ds.hasColumn("CVRNAL") != null) 
      ds.getColumn("CVRNAL").setVisible(0);
        frmTableDataView view = new frmTableDataView();
        view.setDataSet(ds);
        view.setTitle("Bruto bilanca  za period od "+
            Aus.formatTimestamp(stds.getTimestamp("POCDAT")) + " do " +
            Aus.formatTimestamp(stds.getTimestamp("ZAVDAT")));
        view.show();
  }
  
  private QueryDataSet makeSetIspisPoNalozima(List data){
    QueryDataSet razvijeni = new QueryDataSet();
    razvijeni.setResolvable(false);
    razvijeni.setColumns(new Column[] {
        (Column) dm.getGkkumulativi().getColumn("CORG").clone(),
        (Column) dm.getGkkumulativi().getColumn("BROJKONTA").clone(),
        dm.createStringColumn("NK", "Naziv konta", 0),
        dm.createBigDecimalColumn("POCID", "Po\u010DetnoD", 2),
        dm.createBigDecimalColumn("POCIP", "Po\u010DetnoP", 2),
        dm.createBigDecimalColumn("SALPS","SaldoPS",2),
        dm.createBigDecimalColumn("ID","PrometD",2),
        dm.createBigDecimalColumn("IP","PrometP",2),
        dm.createBigDecimalColumn("SALPROM","Saldo PR",2),
        dm.createBigDecimalColumn("SALDO","Saldo",2),
        (Column) dm.getGkkumulativi().getColumn("GODMJ").clone(),
        (Column) dm.getGkstavke().getColumn("CVRNAL").clone()
    });
    razvijeni.open();
    /*try {
      if (razvijeni.getColumn("CORG") == null)
        razvijeni.addColumn((Column) dm.getGkkumulativi().getColumn("CORG").clone());
      } catch (Exception e) {
        razvijeni.addColumn((Column) dm.getGkkumulativi().getColumn("CORG").clone());
      }
    razvijeni.open();
    razvoj.first(); */
    
    /*Map trans = null;
    if (isTreeSelected())
      trans = dlgCompanyTree.get("bruto-bilanca").getTransitions(kontoPanel.getCorg());

    do {
      String realcorg = razvoj.getString("REALCORG");
      if (trans != null && trans.containsKey(realcorg)){
        realcorg = (String) trans.get(realcorg);
      }
      
      if (!lookupData.getlookupData().raLocate(
          razvijeni,
          new String[] {"BROJKONTA","CORG","CVRNAL"},
          new String[] {razvoj.getString("BROJKONTA"), realcorg,razvoj.getString("CVRNAL")})
          ){
        razvijeni.insertRow(false);
        razvijeni.setString("CORG", realcorg);
//        razvijeni.setString("REALCORG", realcorg);
        try {
        razvijeni.setString("GOD", razvoj.getString("GOD"));
        } catch (Exception e) {
        }
        razvijeni.setString("BROJKONTA", razvoj.getString("BROJKONTA"));
        razvijeni.setString("NK", getNazKonto(razvoj.getString("BROJKONTA")));
        razvijeni.setString("CVRNAL", razvoj.getString("CVRNAL"));
        razvijeni.setBigDecimal("POCID", Aus.zero2);
        razvijeni.setBigDecimal("POCIP", Aus.zero2);
        razvijeni.setBigDecimal("ID", razvoj.getBigDecimal("ID"));
        razvijeni.setBigDecimal("IP", razvoj.getBigDecimal("IP"));
      } else {
        razvijeni.setBigDecimal("ID", razvijeni.getBigDecimal("ID").add(razvoj.getBigDecimal("ID")));
        razvijeni.setBigDecimal("IP", razvijeni.getBigDecimal("IP").add(razvoj.getBigDecimal("IP")));
      }
      razvijeni.setBigDecimal("SALDO", razvijeni.getBigDecimal("SALDO").add((razvijeni.getBigDecimal("POCID").add(razvoj.getBigDecimal("ID"))).subtract(razvijeni.getBigDecimal("POCIP").add(razvoj.getBigDecimal("IP")))));
    } while (razvoj.next());*/
    
    List sums = sumKontaCorg(data, 0, true);
    for (int i = 0; i < sums.size(); i++) {
      FormData fd = (FormData) sums.get(i);
      if (fd.cvrnal.equals("00")) {
        fd.id = fd.pocid;
        fd.ip = fd.pocip;
        fd.pocid = fd.pocip = Aus.zero2;
      }
      if (showzero || !fd.allZero()) {
        razvijeni.insertRow(false);
        fd.fillRow(razvijeni, false, true);
      }
    }
    return razvijeni;
  }
  
  private QueryDataSet makeRekapitulacija(List data){
    QueryDataSet rekapitulacija = new QueryDataSet();
    rekapitulacija.setResolvable(false);
    rekapitulacija.setColumns(new Column[] {
        (Column) dm.getGkkumulativi().getColumn("CORG").clone(),
        (Column) dm.getGkkumulativi().getColumn("BROJKONTA").clone(),
        dm.createStringColumn("NK", 0),
        dm.createBigDecimalColumn("POCID", "Po\u010DetnoD", 2),
        dm.createBigDecimalColumn("POCIP", "Po\u010DetnoP", 2),
        dm.createBigDecimalColumn("SALPS","SaldoPS",2),
        dm.createBigDecimalColumn("ID","PrometD",2),
        dm.createBigDecimalColumn("IP","PrometP",2),
        dm.createBigDecimalColumn("SALPROM","SaldoPR",2),
        dm.createBigDecimalColumn("SALDO","SaldoUK",2),
//        (Column) dm.getGkkumulativi().getColumn("GODMJ").clone(),
//        (Column) dm.getGkstavke().getColumn("CVRNAL").clone()
    });
    rekapitulacija.open();
    
/*    sirovi.first();
    
    Map transitions = null;
    if (isTreeSelected())
      transitions = dlgCompanyTree.get("bruto-bilanca").getTransitions(kontoPanel.getCorg());

    do {
      //checkClosing();
      //if (stds.getString("ISPIS").equals("1")) {
      //  sirovi.setRowId("CORG",false);
      //  sirovi.setString("CORG",kontoPanel.getCorg());
//        sirovi.setRowId("CORG",true);
//      }
      
      String realcorg = sirovi.getString("REALCORG");
      if (transitions != null && transitions.containsKey(realcorg)){
        realcorg = (String) transitions.get(realcorg);
      }
      
      if (stds.getString("MODE").equals("BB")) {
        if (!lookupData.getlookupData().raLocate(
            rekapitulacija,
            new String[] {"BROJKONTA","CORG"},
            new String[] {sirovi.getString("BROJKONTA").substring(0,1),realcorg})
        ){
          rekapitulacija.insertRow(false);
          rekapitulacija.setString("CORG", realcorg);
          rekapitulacija.setString("BROJKONTA", sirovi.getString("BROJKONTA").substring(0,1));
          rekapitulacija.setString("NK", getNazKonto(sirovi.getString("BROJKONTA").substring(0,1)));
//          rekapitulacija.setString("GODMJ", sirovi.getString("GODMJ"));
          
          if (!sirovi.getString("GODMJ").substring(4,6).equals("00")){
            rekapitulacija.setBigDecimal("POCID", Aus.zero2);
            rekapitulacija.setBigDecimal("POCIP", Aus.zero2);
            rekapitulacija.setBigDecimal("ID",sirovi.getBigDecimal("ID"));
            rekapitulacija.setBigDecimal("IP",sirovi.getBigDecimal("IP"));
            rekapitulacija.setBigDecimal("SALPROM", (rekapitulacija.getBigDecimal("ID").subtract(rekapitulacija.getBigDecimal("IP"))));
          } else {
            rekapitulacija.setBigDecimal("POCID",sirovi.getBigDecimal("ID"));
            rekapitulacija.setBigDecimal("POCIP",sirovi.getBigDecimal("IP"));
            rekapitulacija.setBigDecimal("ID", Aus.zero2);
            rekapitulacija.setBigDecimal("IP", Aus.zero2);
            rekapitulacija.setBigDecimal("SALPS", (rekapitulacija.getBigDecimal("POCID").subtract(rekapitulacija.getBigDecimal("POCIP"))));
          }
        } else {
          if (!sirovi.getString("GODMJ").substring(4,6).equals("00")){
            rekapitulacija.setBigDecimal("ID",rekapitulacija.getBigDecimal("ID").add(sirovi.getBigDecimal("ID")));
            rekapitulacija.setBigDecimal("IP",rekapitulacija.getBigDecimal("IP").add(sirovi.getBigDecimal("IP")));
            rekapitulacija.setBigDecimal("SALPROM", (rekapitulacija.getBigDecimal("ID").subtract(rekapitulacija.getBigDecimal("IP"))));
          } else {
            rekapitulacija.setBigDecimal("POCID",rekapitulacija.getBigDecimal("POCID").add(sirovi.getBigDecimal("ID")));
            rekapitulacija.setBigDecimal("POCIP",rekapitulacija.getBigDecimal("POCIP").add(sirovi.getBigDecimal("IP")));
            rekapitulacija.setBigDecimal("SALPS", (rekapitulacija.getBigDecimal("POCID").subtract(rekapitulacija.getBigDecimal("POCIP"))));
          }
        }
      } else { // PERIOD
        
        if (!lookupData.getlookupData().raLocate(
            rekapitulacija,
            new String[] {"BROJKONTA","CORG"},
            new String[] {sirovi.getString("BROJKONTA").substring(0,1),realcorg})
            ){
          rekapitulacija.insertRow(false);
          rekapitulacija.setString("CORG", realcorg);
          rekapitulacija.setString("BROJKONTA", sirovi.getString("BROJKONTA").substring(0,1));
          rekapitulacija.setString("NK", getNazKonto(sirovi.getString("BROJKONTA").substring(0,1)));
//          rekapitulacija.setString("CVRNAL", sirovi.getString("CVRNAL"));
          if (!sirovi.getString("CVRNAL").equals("00")){
            rekapitulacija.setBigDecimal("POCID", Aus.zero2);
            rekapitulacija.setBigDecimal("POCIP", Aus.zero2);
            rekapitulacija.setBigDecimal("ID",sirovi.getBigDecimal("ID"));
            rekapitulacija.setBigDecimal("IP",sirovi.getBigDecimal("IP"));
            rekapitulacija.setBigDecimal("SALPROM", (rekapitulacija.getBigDecimal("ID").subtract(rekapitulacija.getBigDecimal("IP")))); 
          } else {
            rekapitulacija.setBigDecimal("POCID",sirovi.getBigDecimal("ID"));
            rekapitulacija.setBigDecimal("POCIP",sirovi.getBigDecimal("IP"));
            rekapitulacija.setBigDecimal("ID", Aus.zero2);
            rekapitulacija.setBigDecimal("IP", Aus.zero2);
            rekapitulacija.setBigDecimal("SALPS", (rekapitulacija.getBigDecimal("POCID").subtract(rekapitulacija.getBigDecimal("POCIP")))); 
          }
        } else {
          if (!sirovi.getString("CVRNAL").equals("00")){
            rekapitulacija.setBigDecimal("ID",rekapitulacija.getBigDecimal("ID").add(sirovi.getBigDecimal("ID")));
            rekapitulacija.setBigDecimal("IP",rekapitulacija.getBigDecimal("IP").add(sirovi.getBigDecimal("IP")));
            rekapitulacija.setBigDecimal("SALPROM", (rekapitulacija.getBigDecimal("ID").subtract(rekapitulacija.getBigDecimal("IP")))); 
          } else {
            rekapitulacija.setBigDecimal("POCID",rekapitulacija.getBigDecimal("POCID").add(sirovi.getBigDecimal("ID")));
            rekapitulacija.setBigDecimal("POCIP",rekapitulacija.getBigDecimal("POCIP").add(sirovi.getBigDecimal("IP")));
            rekapitulacija.setBigDecimal("SALPS", (rekapitulacija.getBigDecimal("POCID").subtract(rekapitulacija.getBigDecimal("POCIP")))); 
          }
        }
      }
      rekapitulacija.setBigDecimal("SALDO", ((rekapitulacija.getBigDecimal("POCID").add(rekapitulacija.getBigDecimal("ID"))).subtract(rekapitulacija.getBigDecimal("POCIP").add(rekapitulacija.getBigDecimal("IP")))));
    }  while (sirovi.next());
  */  
//    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//    st.prn(rekapitulacija);
    
    List sums = sumKontaCorg(data, kontoPanel.jlrKontoBroj.getText().length() + 1, false);
    for (int i = 0; i < sums.size(); i++) {
      FormData fd = (FormData) sums.get(i);
      if (showzero || !fd.allZero()) {
        rekapitulacija.insertRow(false);
        fd.fillRow(rekapitulacija, false, false);
      }
    }
    
    return rekapitulacija;
  }
  
  private List sumKontaCorg(List data, int rekap, boolean nal) {
    checkClosing();
    
    Map transitions = null;
    if (isTreeSelected())
      transitions = dlgCompanyTree.get("bruto-bilanca").getTransitions(kontoPanel.getCorg());
    
    Map sums = new HashMap();
    
    for (int i = 0; i < data.size(); i++) {
      MainQueryData md = (MainQueryData) data.get(i);
      
      boolean pocst = (bbKum && md.godmj.endsWith("00")) || (!bbKum && md.godmj.equals("00"));
      
      if (transitions != null && transitions.containsKey(md.corg)){
        md.corg = (String) transitions.get(md.corg);
      }
      String konto = rekap > 0 ? md.brojkonta.substring(0, rekap) : md.brojkonta;
      String key = !nal ? konto + "-" + md.corg : konto + "-" + md.corg + "-" + md.godmj;

      FormData sum = (FormData) sums.get(key);
      if (sum == null) {
        sums.put(key, sum = new FormData());
        sum.corg = md.corg;
        sum.brojkonta = konto;
        sum.nk = raKonta.getNazivKonta(konto);
        sum.godmj = sum.cvrnal = "";
        if (bbKum) sum.godmj = md.godmj;
        else sum.cvrnal = md.godmj;
        
        if (!pocst) {
          sum.pocid = sum.pocip = Aus.zero2;
          sum.id = md.id;
          sum.ip = md.ip;
        } else {
          sum.pocid = md.id;
          sum.pocip = md.ip;
          sum.id = sum.ip = Aus.zero2;
        }
      } else {
        if (!pocst) {
          sum.id = sum.id.add(md.id);
          sum.ip = sum.ip.add(md.ip);
        } else {
          sum.pocid = sum.pocid.add(md.id);
          sum.pocip = sum.pocip.add(md.ip);
        }
      }
    }
    return new ArrayList(sums.values());
  }

  private QueryDataSet makeSetForNewIspis(List data) {
    checkClosing();
    QueryDataSet obradjeni = new QueryDataSet();
    obradjeni.setResolvable(false);
    obradjeni.setColumns(new Column[] {
        (Column) dm.getGkkumulativi().getColumn("CORG").clone(),
        (Column) dm.getGkkumulativi().getColumn("BROJKONTA").clone(),
        dm.createStringColumn("NK", "Naziv konta", 0),
        dm.createBigDecimalColumn("POCID", "Po\u010DetnoD", 2),
        dm.createBigDecimalColumn("POCIP", "Po\u010DetnoP", 2),
        dm.createBigDecimalColumn("SALPS","SaldoPS",2),
        dm.createBigDecimalColumn("ID","PrometD",2),
        dm.createBigDecimalColumn("IP","PrometP",2),
        dm.createBigDecimalColumn("SALPROM","SaldoPR",2),
        dm.createBigDecimalColumn("SALDO","SaldoUK",2),
        (Column) dm.getGkkumulativi().getColumn("GODMJ").clone(),
        (Column) dm.getGkstavke().getColumn("CVRNAL").clone()
    });
//    obradjeni.setRowId("CORG",true);
//    obradjeni.setRowId("BROJKONTA",true);
    obradjeni.open();

    /*sirovi.first();
    
    Map transitions = null;
    if (isTreeSelected())
      transitions = dlgCompanyTree.get("bruto-bilanca").getTransitions(kontoPanel.getCorg());

    do {
      //checkClosing();
      //if (stds.getString("ISPIS").equals("1")) {
        //sirovi.setRowId("CORG",false);
        //sirovi.setString("CORG",kontoPanel.getCorg());
//        sirovi.setRowId("CORG",true);
      //}
      
      String realcorg = sirovi.getString("REALCORG");
      if (transitions != null && transitions.containsKey(realcorg)){
        realcorg = (String) transitions.get(realcorg);
      }
      
      if (stds.getString("MODE").equals("BB")) {
        if (!lookupData.getlookupData().raLocate(
            obradjeni,
            new String[] {"BROJKONTA","CORG"},
            new String[] {sirovi.getString("BROJKONTA"),realcorg})
        ){
          obradjeni.insertRow(false);
          obradjeni.setString("CORG", realcorg);
          obradjeni.setString("BROJKONTA", sirovi.getString("BROJKONTA"));
          obradjeni.setString("NK", getNazKonto(sirovi.getString("BROJKONTA")));
          obradjeni.setString("GODMJ", sirovi.getString("GODMJ"));
          
          if (!sirovi.getString("GODMJ").substring(4,6).equals("00")){
            obradjeni.setBigDecimal("POCID", Aus.zero2);
            obradjeni.setBigDecimal("POCIP", Aus.zero2);
            obradjeni.setBigDecimal("ID",sirovi.getBigDecimal("ID"));
            obradjeni.setBigDecimal("IP",sirovi.getBigDecimal("IP"));
            obradjeni.setBigDecimal("SALPROM", (obradjeni.getBigDecimal("ID").subtract(obradjeni.getBigDecimal("IP"))));
          } else {
            obradjeni.setBigDecimal("POCID",sirovi.getBigDecimal("ID"));
            obradjeni.setBigDecimal("POCIP",sirovi.getBigDecimal("IP"));
            obradjeni.setBigDecimal("ID", Aus.zero2);
            obradjeni.setBigDecimal("IP", Aus.zero2);
            obradjeni.setBigDecimal("SALPS", (obradjeni.getBigDecimal("POCID").subtract(obradjeni.getBigDecimal("POCIP"))));
          }
        } else {
          if (!sirovi.getString("GODMJ").substring(4,6).equals("00")){
            obradjeni.setBigDecimal("ID",obradjeni.getBigDecimal("ID").add(sirovi.getBigDecimal("ID")));
            obradjeni.setBigDecimal("IP",obradjeni.getBigDecimal("IP").add(sirovi.getBigDecimal("IP")));
            obradjeni.setBigDecimal("SALPROM", (obradjeni.getBigDecimal("ID").subtract(obradjeni.getBigDecimal("IP"))));
          } else {
            obradjeni.setBigDecimal("POCID",obradjeni.getBigDecimal("POCID").add(sirovi.getBigDecimal("ID")));
            obradjeni.setBigDecimal("POCIP",obradjeni.getBigDecimal("POCIP").add(sirovi.getBigDecimal("IP")));
            obradjeni.setBigDecimal("SALPS", (obradjeni.getBigDecimal("POCID").subtract(obradjeni.getBigDecimal("POCIP"))));
          }
        }
      } else { // PERIOD
        
        if (!lookupData.getlookupData().raLocate(
            obradjeni,
            new String[] {"BROJKONTA","CORG"},
            new String[] {sirovi.getString("BROJKONTA"),realcorg})
            ){
          obradjeni.insertRow(false);
          obradjeni.setString("CORG", realcorg);
          obradjeni.setString("BROJKONTA", sirovi.getString("BROJKONTA"));
          obradjeni.setString("NK", getNazKonto(sirovi.getString("BROJKONTA")));
          obradjeni.setString("CVRNAL", sirovi.getString("CVRNAL"));
          if (!sirovi.getString("CVRNAL").equals("00")){
            obradjeni.setBigDecimal("POCID", Aus.zero2);
            obradjeni.setBigDecimal("POCIP", Aus.zero2);
            obradjeni.setBigDecimal("ID",sirovi.getBigDecimal("ID"));
            obradjeni.setBigDecimal("IP",sirovi.getBigDecimal("IP"));
            obradjeni.setBigDecimal("SALPROM", (obradjeni.getBigDecimal("ID").subtract(obradjeni.getBigDecimal("IP")))); 
          } else {
            obradjeni.setBigDecimal("POCID",sirovi.getBigDecimal("ID"));
            obradjeni.setBigDecimal("POCIP",sirovi.getBigDecimal("IP"));
            obradjeni.setBigDecimal("ID", Aus.zero2);
            obradjeni.setBigDecimal("IP", Aus.zero2);
            obradjeni.setBigDecimal("SALPS", (obradjeni.getBigDecimal("POCID").subtract(obradjeni.getBigDecimal("POCIP")))); 
          }
        } else {
          if (!sirovi.getString("CVRNAL").equals("00")){
            obradjeni.setBigDecimal("ID",obradjeni.getBigDecimal("ID").add(sirovi.getBigDecimal("ID")));
            obradjeni.setBigDecimal("IP",obradjeni.getBigDecimal("IP").add(sirovi.getBigDecimal("IP")));
            obradjeni.setBigDecimal("SALPROM", (obradjeni.getBigDecimal("ID").subtract(obradjeni.getBigDecimal("IP")))); 
          } else {
            obradjeni.setBigDecimal("POCID",obradjeni.getBigDecimal("POCID").add(sirovi.getBigDecimal("ID")));
            obradjeni.setBigDecimal("POCIP",obradjeni.getBigDecimal("POCIP").add(sirovi.getBigDecimal("IP")));
            obradjeni.setBigDecimal("SALPS", (obradjeni.getBigDecimal("POCID").subtract(obradjeni.getBigDecimal("POCIP")))); 
          }
        }
        
      }
      
      
      
      obradjeni.setBigDecimal("SALDO", ((obradjeni.getBigDecimal("POCID").add(obradjeni.getBigDecimal("ID"))).subtract(obradjeni.getBigDecimal("POCIP").add(obradjeni.getBigDecimal("IP")))));
      
    } while (sirovi.next()); */
    
    List sums = sumKontaCorg(data, 0, false);
    for (int i = 0; i < sums.size(); i++) {
      FormData fd = (FormData) sums.get(i);
      if (showzero || !fd.allZero()) {
        obradjeni.insertRow(false);
        fd.fillRow(obradjeni, false, true);
      }
    }
    
    return valutaConvertedSet(obradjeni);
  }
  
  private QueryDataSet getClassedBilance(List data){
//    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//    st.showInFrame(fullConts,"Dataset Test Frame");
    checkClosing();
    QueryDataSet razvijeni = new QueryDataSet();
    razvijeni.setResolvable(false);
    razvijeni.setColumns(new Column[] {
        (Column) dm.getGkkumulativi().getColumn("CORG").clone(),
        (Column) dm.getGkkumulativi().getColumn("BROJKONTA").clone(),
        dm.createStringColumn("NK", "Naziv konta", 0),
        dm.createBigDecimalColumn("POCID", "Po\u010DetnoD", 2),
        dm.createBigDecimalColumn("POCIP", "Po\u010DetnoP", 2),
        dm.createBigDecimalColumn("SALPS","SaldoPS",2),
        dm.createBigDecimalColumn("ID","PrometD",2),
        dm.createBigDecimalColumn("IP","PrometP",2),
        dm.createBigDecimalColumn("SALPROM","SaldoPR",2),
        dm.createBigDecimalColumn("SALDO","SaldoUK",2)
    }); 
    razvijeni.setRowId("CORG",true);
    razvijeni.setRowId("BROJKONTA",true);
    razvijeni.open();
    
/*    fullConts.first();
    
    Map transitions = null;
    if (isTreeSelected())
      transitions = dlgCompanyTree.get("bruto-bilanca").getTransitions(kontoPanel.getCorg());
    
    do {
      //checkClosing();
      

      
      String realcorg = fullConts.getString("CORG");
      if (transitions != null && transitions.containsKey(realcorg)){
        realcorg = (String) transitions.get(realcorg);
      }
      
      
      */
    
      /*if (stds.getString("BILANCA").equals("S")){
        for (int i = 1; i <= 3; i++) {
          checkClosing();
          if (!lookupData.getlookupData().raLocate(
              razvijeni,
              new String[] {"BROJKONTA","CORG"},
              new String[] {fullConts.getString("BROJKONTA"),realcorg})
          ){
              
              razvijeni.insertRow(false);
              razvijeni.setString("CORG", realcorg);
              razvijeni.setString("BROJKONTA", fullConts.getString("BROJKONTA").substring(0, i));
              razvijeni.setString("NK", getNazKOnto(fullConts.getString("BROJKONTA").substring(0, i)));
              razvijeni.setBigDecimal("ID", fullConts.getBigDecimal("ID"));
              razvijeni.setBigDecimal("IP", fullConts.getBigDecimal("IP"));
              razvijeni.setBigDecimal("POCID", fullConts.getBigDecimal("POCID"));
              razvijeni.setBigDecimal("POCIP", fullConts.getBigDecimal("POCIP"));
          } else {
            razvijeni.setBigDecimal("ID", razvijeni.getBigDecimal("ID").add(fullConts.getBigDecimal("ID")));
            razvijeni.setBigDecimal("IP", razvijeni.getBigDecimal("IP").add(fullConts.getBigDecimal("IP")));
            razvijeni.setBigDecimal("POCID", razvijeni.getBigDecimal("POCID").add(fullConts.getBigDecimal("POCID")));
            razvijeni.setBigDecimal("POCIP", razvijeni.getBigDecimal("POCIP").add(fullConts.getBigDecimal("POCIP")));
          }
        }
     } else {*/
  /*      if (!lookupData.getlookupData().raLocate(
            razvijeni,
            new String[] {"BROJKONTA","CORG"},
            new String[] {fullConts.getString("BROJKONTA"),realcorg})
        ){
            razvijeni.insertRow(false);
            razvijeni.setString("CORG", realcorg);
            razvijeni.setString("BROJKONTA", fullConts.getString("BROJKONTA"));
            razvijeni.setBigDecimal("ID", fullConts.getBigDecimal("ID"));
            razvijeni.setBigDecimal("IP", fullConts.getBigDecimal("IP"));
            razvijeni.setBigDecimal("SALPROM",fullConts.getBigDecimal("SALPROM"));
            razvijeni.setBigDecimal("POCID", fullConts.getBigDecimal("POCID"));
            razvijeni.setBigDecimal("POCIP", fullConts.getBigDecimal("POCIP"));
            razvijeni.setBigDecimal("SALPS",fullConts.getBigDecimal("SALPS"));
            razvijeni.setBigDecimal("SALDO",fullConts.getBigDecimal("SALDO"));
            razvijeni.setString("NK", fullConts.getString("NK"));
        } else {
          razvijeni.setBigDecimal("ID", razvijeni.getBigDecimal("ID").add(fullConts.getBigDecimal("ID")));
          razvijeni.setBigDecimal("IP", razvijeni.getBigDecimal("IP").add(fullConts.getBigDecimal("IP")));
          razvijeni.setBigDecimal("SALPROM",razvijeni.getBigDecimal("SALPROM").add(fullConts.getBigDecimal("SALPROM")));
          razvijeni.setBigDecimal("POCID", razvijeni.getBigDecimal("POCID").add(fullConts.getBigDecimal("POCID")));
          razvijeni.setBigDecimal("POCIP", razvijeni.getBigDecimal("POCIP").add(fullConts.getBigDecimal("POCIP")));
          razvijeni.setBigDecimal("SALPS",razvijeni.getBigDecimal("SALPS").add(fullConts.getBigDecimal("SALPS")));
          razvijeni.setBigDecimal("SALDO",razvijeni.getBigDecimal("SALDO").add(fullConts.getBigDecimal("SALDO")));
        }
      //  }
     // }
    } while (fullConts.next());
    razvijeni.setSort(new SortDescriptor(new String[] {"CORG","BROJKONTA"}));

//    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//    st.showInFrame(razvijeni,"Dataset Test Frame");
    
    
//    razvijeni.setSort(new SortDescriptor("CORG", new String[] {"CORG","BROJKONTA"},new boolean[] {false,stds.getString("BILANCA").equals("S")},false,false,Locale.getDefault().toString()));
    
//    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//    st.showInFrame(razvijeni,"Dataset Test Frame");
    
    QueryDataSet summed;

    if (stds.getString("BILANCA").equals("S")){
      
      razvijeni.first();
      
      summed = new QueryDataSet();
      summed.setResolvable(false);
      summed.setColumns(razvijeni.cloneColumns());
      do {
        //checkClosing();
        

        
        String realcorg = razvijeni.getString("CORG");
        if (transitions != null && transitions.containsKey(realcorg)){
          realcorg = (String) transitions.get(realcorg);
        }
        
        
//        if (lookupData.getlookupData().raLocate(summed,new String[] {"BROJKONTA","CORG"},
//            new String[] {razvijeni.getString("BROJKONTA"),razvijeni.getString("CORG")})){
        if (lookupData.getlookupData().raLocate(
            summed,
            new String[] {"BROJKONTA","CORG"},
            new String[] {razvijeni.getString("BROJKONTA"),realcorg})
        ){
          
          summed.setBigDecimal("POCID",summed.getBigDecimal("POCID").add(razvijeni.getBigDecimal("POCID")));
          summed.setBigDecimal("POCIP",summed.getBigDecimal("POCIP").add(razvijeni.getBigDecimal("POCIP")));
          summed.setBigDecimal("ID",summed.getBigDecimal("ID").add(razvijeni.getBigDecimal("ID")));
          summed.setBigDecimal("IP",summed.getBigDecimal("IP").add(razvijeni.getBigDecimal("IP")));
        } else {
          summed.insertRow(false);
          razvijeni.copyTo(summed);
        }
      } while (razvijeni.next());
    } else {
      summed = razvijeni;
      summed.open();
    }
    */
//  if (stds.getString("BILANCA").equals("S")){
//  summed.setBigDecimal("SALPS",summed.getBigDecimal("POCID").subtract(summed.getBigDecimal("POCIP")));
//  summed.setBigDecimal("SALPROM",summed.getBigDecimal("ID").subtract(summed.getBigDecimal("IP")));
//  summed.setBigDecimal("SALDO",summed.getBigDecimal("SALPS").add(summed.getBigDecimal("SALPROM")));
//} else {
//  summed.setBigDecimal("SALPS",summed.getBigDecimal("SALPS").add(razvijeni.getBigDecimal("SALPS")));
//  summed.setBigDecimal("SALPROM",summed.getBigDecimal("SALPROM").add(razvijeni.getBigDecimal("SALPROM")));
//  summed.setBigDecimal("SALDO",summed.getBigDecimal("SALDO").add(razvijeni.getBigDecimal("SALDO")));
//}
    
    for (int i = 0; i < data.size(); i++) {
      FormData fd = (FormData) data.get(i);
      if (showzero || !fd.allZero()) {
        razvijeni.insertRow(false);
        fd.fillRow(razvijeni, false, false);
      }
    }
    
    return valutaConvertedSet(razvijeni); 
  } 
  
  void okAndInit() {
    try {
      needInit = false;
      ok_action();
    } finally {
      needInit = true;
    }
  }
  
  boolean needInit = true;
  public void initColBean() {
    if (needInit) super.initColBean();    
  }

  
  public void jptv_doubleClick() {
    if (isShifted) return;
    if (!doubleClicked) {
      handleArrowColapse(false);
      handleArrowExpand(false);
      kontoPanel.setNoLookup(true);
      doubleClicked = true;
    }
    if (this.getJPTV().getDataSet().getString("BROJKONTA").length() < 4) {
      ll.addLast(this.kontoPanel.jlrKontoBroj.getText().trim());
      pl.addLast(this.getJPTV().getDataSet().getRow()+"");
      kontoPanel.jlrKontoBroj.setText(this.getJPTV().getDataSet().getString("BROJKONTA"));
      kontoPanel.jlrKontoBroj.forceFocLost();
      kontoPanel.setcORG(corgRemember);
      this.getJPTV().enableEvents(false);
      
      okAndInit();
      
      this.getJPTV().enableEvents(true);
    } else {
      frmKarticeGK fkgk = new frmKarticeGK(false,0);
      String konto = this.getJPTV().getDataSet().getString("BROJKONTA");
      fkgk.stds.open();
      fkgk.stds.setTimestamp("pocDatum", stds.getTimestamp("POCDAT"));
      fkgk.stds.setTimestamp("zavDatum", stds.getTimestamp("ZAVDAT"));
      fkgk.setSljed("K");
      fkgk.pack();
      fkgk.setTitle("Kartica glavne knjige za konto "+ konto);
      fkgk.kontoPanel.jlrKontoBroj.setText(konto);
      fkgk.kontoPanel.jlrKontoBroj.forceFocLost();
      fkgk.kontoPanel.jlrCorg.setText(corgRemember);
      fkgk.kontoPanel.jlrCorg.forceFocLost();
      fkgk.setPrivremeno(stds.getString("PRIVREMENO"));
      fkgk.setLocation(this.getWindow().getX(), this.getWindow().getY());
      fkgk.show();
      return;
    }
  }
  
  // Outgoing getters... //XDEBUG geteri za reporte
  
  public boolean isKompletnaBilanca(){
    return stds.getString("BILANCA").equals("K");
  }
  
  public boolean isSintetik(){
    return stds.getString("BILANCA").equals("S");
  }
  
  public boolean isAnalitik(){
    return stds.getString("BILANCA").equals("A");
  }
  
  public BigDecimal getTecaj(){
    return tecaj;
  }
  
  public String getCval(){
    return stds.getString("OZNVAL");
  }
  
  public String getNazval(){
    return jpVal.jtNAZVAL.getText();
  }
  
  public String getBilanca() {
    return stds.getString("BILANCA");
  }

  public Timestamp getPocDat(){
    return stds.getTimestamp("POCDAT");
  }
  
  public Timestamp getZavDat(){
    return stds.getTimestamp("ZAVDAT");
  }
  
  public String getPocMj(){
    return stds.getString("POCMJ");
  }
  
  public String getZavMj(){
    return stds.getString("ZAVMJ");
  }
  
  public String getGodina(){
    return stds.getString("GODINA");
  }
  
  public String getVRSTA() {
    return stds.getString("MODE");
  }

  public String getSKUPNI() {
    if (stds.getString("ISPIS").equals("1")){ //jrbSkupni.isSelected()) {
      return "ZBIRNO";
    }
    return "CORG";
  }
  
  public String getPRIPADNOST(){
    return stds.getString("ORGSTR");
  }

  public String getCORG() {
    return kontoPanel.jlrCorg.getText();
  }
  
  public boolean isZaValutu(){
    if (stds.getString("OZNVAL").equals("")) return false;
    return  !stds.getString("OZNVAL").equalsIgnoreCase(domaval);
  }
  
  public QueryDataSet getBroutoBilancaReportSet(){
    return repKontaSet;
  }
  
  public QueryDataSet getSintetikAnalitikSet(){
    return repSintetikAnalitikSet;
  }
  
  public QueryDataSet getRekapitulacijaSet(){
    return repRekapitulacijaSet;
  }
  
  public QueryDataSet getPoNalozimaSet(){
    return repPoNalozimaSet;
  }

  public BigDecimal[] getKumulative(){
    return null; //TODO rjesiti ovaj null..
  }
  
  public String getPromPocSt(){
   return stds.getString("PS"); 
  }
  
  ///Outgoing getters...
  
  protected void llHandler() {
    kontoPanel.jlrKontoBroj.setText(ll.removeLast().toString().trim());
    kontoPanel.jlrKontoBroj.forceFocLost();
    kontoPanel.jlrCorg.setText(corgRemember);
    kontoPanel.jlrCorg.forceFocLost();
    this.getJPTV().enableEvents(false);

    seekPos = Integer.parseInt(pl.removeLast().toString());
    okAndInit();
    
    //this.getJPTV().getDataSet().goToRow(Integer.parseInt(pl.removeLast().toString()));
    this.getJPTV().enableEvents(true);
    if (ll.isEmpty()){
      handleArrowColapse(false);
      handleArrowExpand(true);
    }
  }
  
/*  private QueryDataSet deriveRawSet(QueryDataSet rawset, boolean forPrint) {
    QueryDataSet forReturn = new QueryDataSet();
    forReturn.setColumns(new Column[] {
        (Column) dm.getGkkumulativi().getColumn("CORG").clone(),
        (Column) dm.getGkkumulativi().getColumn("BROJKONTA").clone(),
        dm.createStringColumn("NK", 0),
        dm.createBigDecimalColumn("POCID", "Po\u010DetnoD", 2),
        dm.createBigDecimalColumn("POCIP", "Po\u010DetnoP", 2),
        dm.createBigDecimalColumn("SALPS","SaldoPS",2),
        dm.createBigDecimalColumn("ID","PrometD",2),
        dm.createBigDecimalColumn("IP","PrometP",2),
        dm.createBigDecimalColumn("SALPROM","SaldoPR",2),
        dm.createBigDecimalColumn("DUG","DugujeUK",2),
        dm.createBigDecimalColumn("POT","PotražujeUK",2),
        dm.createBigDecimalColumn("SALDO","SaldoUK",2),
        (Column) dm.getGkkumulativi().getColumn("GODMJ").clone(),
        (Column) dm.getGkstavke().getColumn("CVRNAL").clone()
    });
    forReturn.setResolvable(false);
    forReturn.open();
    rawset.first();
    int position = kontoPanel.getBrKonLength()+1;
    if (position == 4) position = 100;
    
    rawset.first();
    do {
      forReturn.insertRow(false);
      forReturn.setString("CORG",rawset.getString("REALCORG"));
      try {
        forReturn.setString("BROJKONTA",rawset.getString("BROJKONTA").substring(0,position));
      }
      catch (Exception ex) {
        forReturn.setString("BROJKONTA",rawset.getString("BROJKONTA"));
      }
      forReturn.setString("NK",getNazKonto(rawset.getString("BROJKONTA"))); // was orginal.getString"BROJK....
      try {
      forReturn.setString("GODMJ",rawset.getString("GODMJ"));
      } catch (Exception e) {
        forReturn.setString("CVRNAL",rawset.getString("CVRNAL"));
      }
      
      forReturn.setBigDecimal("ID",rawset.getBigDecimal("ID"));
      forReturn.setBigDecimal("IP",rawset.getBigDecimal("IP"));
      forReturn.setBigDecimal("DUG",rawset.getBigDecimal("ID"));
      forReturn.setBigDecimal("POT",rawset.getBigDecimal("IP"));
      forReturn.setBigDecimal("SALDO",rawset.getBigDecimal("ID").subtract(rawset.getBigDecimal("IP")));
    } while (rawset.next());
    
//    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//    st.prn(forReturn);
    
      return setPocAndProm(forReturn, forPrint);
  }*/

  /*private QueryDataSet setPocAndProm(List nebr, boolean forPrint){
    sok.report("before poc and prom");
    for (int i = 0; i < nebr.size(); i++) {
      FormData fr = (FormData) nebr.get(i);
      if ((bbKum && !fr.godmj.endsWith("00")) ||
          (!bbKum && !fr.cvrnal.equals("00"))) {
        fr.pocid =  fr.pocip = Aus.zero2;
      } else {
        fr.pocid = fr.id;
        fr.pocip = fr.ip;
        fr.id =  fr.ip = Aus.zero2;
      }
    }
    checkClosing();
    sok.report("before sum same");
    return sumSame(nebr, forPrint);
    
    long d1 = 0, d2 = 0, d3 = 0, d4 = 0;
    nebr.first();
      do {
        long now = System.currentTimeMillis();
        if (stds.getString("MODE").equals("BB") && !nebr.getString("GODMJ").substring(4,6).equals("00")){
          nebr.setBigDecimal("POCID", Aus.zero2);
          nebr.setBigDecimal("POCIP", Aus.zero2);
          nebr.setBigDecimal("SALPROM", nebr.getBigDecimal("ID").subtract(nebr.getBigDecimal("IP")));
          d1 += (System.currentTimeMillis() - now);
        } else if (stds.getString("MODE").equals("BP") && !nebr.getString("CVRNAL").equals("00")){
          nebr.setBigDecimal("POCID", Aus.zero2);
          nebr.setBigDecimal("POCIP", Aus.zero2);
          nebr.setBigDecimal("SALPROM", nebr.getBigDecimal("ID").subtract(nebr.getBigDecimal("IP")));
          d2 += (System.currentTimeMillis() - now);
        } else {
          nebr.setBigDecimal("POCID",nebr.getBigDecimal("ID"));
          nebr.setBigDecimal("POCIP",nebr.getBigDecimal("IP"));
          nebr.setBigDecimal("SALPS", nebr.getBigDecimal("ID").subtract(nebr.getBigDecimal("IP")));
          nebr.setBigDecimal("ID", Aus.zero2);
          nebr.setBigDecimal("IP", Aus.zero2);
          d3 += (System.currentTimeMillis() - now);
        }
        now = System.currentTimeMillis();
        nebr.post();
        d4 += (System.currentTimeMillis() - now);
      } while (nebr.next());
      sok.report("before sum same");
      sok.report("d1 = " + d1 + "  d2 = " + d2 + " d3 = " + d3 + " d4 = " + d4 + "  sum = " + (d1+d2+d3+d4));
      return sumSame(nebr,forPrint);
    
  }*/

  private QueryDataSet sumSame(List data, boolean forPrint){
    
    QueryDataSet sumamed = new QueryDataSet();
    sumamed.setResolvable(false);
    sumamed.setColumns(new Column[] {
        (Column) dm.getGkkumulativi().getColumn("CORG").clone(),
        (Column) dm.getGkkumulativi().getColumn("BROJKONTA").clone(),
        dm.createStringColumn("NK", "Naziv konta", 0),
        dm.createBigDecimalColumn("POCID", "Po\u010DetnoD", 2),
        dm.createBigDecimalColumn("POCIP", "Po\u010DetnoP", 2),
        dm.createBigDecimalColumn("SALPS","SaldoPS",2),
        dm.createBigDecimalColumn("ID","PrometD",2),
        dm.createBigDecimalColumn("IP","PrometP",2),
        dm.createBigDecimalColumn("SALPROM","SaldoPR",2),
        dm.createBigDecimalColumn("DUG","DugujeUK",2),
        dm.createBigDecimalColumn("POT","PotražujeUK",2),
        dm.createBigDecimalColumn("SALDO","SaldoUK",2),
        (Column) dm.getGkkumulativi().getColumn("GODMJ").clone(),
        (Column) dm.getGkstavke().getColumn("CVRNAL").clone()
    });
    sumamed.setRowId("BROJKONTA",true);
    sumamed.setRowId("CORG",true);
    sumamed.getColumn("CORG").setVisible(0);
    sumamed.getColumn("GODMJ").setVisible(0);
    sumamed.getColumn("CVRNAL").setVisible(0);
    sumamed.getColumn("NK").setVisible(0);
    if (!stds.getString("PS").equals("D")){
      sumamed.getColumn("DUG").setVisible(0);
      sumamed.getColumn("POT").setVisible(0);
    }
    sumamed.open();
    
    Map sums = new HashMap();
    
    for (int i = 0; i < data.size(); i++) {
      FormData fd = (FormData) data.get(i);
      if (!forPrint) fd.corg = kontoPanel.getCorg();
      
      String key = fd.brojkonta + "-" + fd.corg;
      FormData sum = (FormData) sums.get(key);
      if (sum == null) sums.put(key, fd);
      else {
        sum.pocid = sum.pocid.add(fd.pocid);
        sum.pocip = sum.pocip.add(fd.pocip);
        sum.id = sum.id.add(fd.id);
        sum.ip = sum.ip.add(fd.ip);
      }
    }
    checkClosing();
    for (Iterator i = sums.values().iterator(); i.hasNext(); ) {
      FormData fd = (FormData) i.next();
      if (showzero || !fd.allZero()) {
        sumamed.insertRow(false);
        fd.fillRow(sumamed, true, true);
      }
    }
    
    /*
    differenses.first();
    
    do {
      
      if (!forPrint) {
        differenses.setString("CORG",kontoPanel.getCorg());
      }
      
      if (lookupData.getlookupData().raLocate(
            sumamed,
            new String[] {"BROJKONTA","CORG"},
            new String[] {differenses.getString("BROJKONTA"),differenses.getString("CORG")})
         ) {
        sumamed.setBigDecimal("POCID",sumamed.getBigDecimal("POCID").add(differenses.getBigDecimal("POCID")));
        sumamed.setBigDecimal("POCIP",sumamed.getBigDecimal("POCIP").add(differenses.getBigDecimal("POCIP")));
        sumamed.setBigDecimal("ID",sumamed.getBigDecimal("ID").add(differenses.getBigDecimal("ID")));
        sumamed.setBigDecimal("IP",sumamed.getBigDecimal("IP").add(differenses.getBigDecimal("IP")));
        sumamed.setBigDecimal("DUG",sumamed.getBigDecimal("DUG").add(differenses.getBigDecimal("DUG")));
        sumamed.setBigDecimal("POT",sumamed.getBigDecimal("POT").add(differenses.getBigDecimal("POT")));
        sumamed.setBigDecimal("SALDO",sumamed.getBigDecimal("SALDO").add(differenses.getBigDecimal("SALDO")));
        sumamed.setBigDecimal("SALPS",sumamed.getBigDecimal("SALPS").add(differenses.getBigDecimal("SALPS")));
        sumamed.setBigDecimal("SALPROM",sumamed.getBigDecimal("SALPROM").add(differenses.getBigDecimal("SALPROM")));
      } else {
        sumamed.insertRow(false);
        differenses.copyTo(sumamed);
      }
    } while (differenses.next()); */
    
    
    sumamed.setSort(new SortDescriptor(new String[] {"BROJKONTA"}));
    
    /*if (!doubleClicked){
      repSet = sumamed;
    }*/
    return valutaConvertedSet(sumamed);
  }
  
  BigDecimal tecaj;
  BigDecimal jedVl;
  private String valuta = "";
  
  private QueryDataSet valutaConvertedSet(QueryDataSet originSet){
    checkClosing();
    if (isZaValutu()) {
      tecaj = hr.restart.zapod.Tecajevi.getTecaj(vl.getToday(),stds.getString("OZNVAL"));
      jedVl = hr.restart.zapod.Tecajevi.getJedVal(stds.getString("OZNVAL"));
      
      if (tecaj.compareTo(Aus.zero2) == 0){
        return originSet;
      }
      
      BigDecimal jedinicniTecaj = tecaj.divide(jedVl,6,BigDecimal.ROUND_HALF_UP);
      
      originSet.first();
      do {
        originSet.setBigDecimal("POCID",originSet.getBigDecimal("POCID").divide(jedinicniTecaj,6,BigDecimal.ROUND_HALF_UP));
        originSet.setBigDecimal("POCIP",originSet.getBigDecimal("POCIP").divide(jedinicniTecaj,6,BigDecimal.ROUND_HALF_UP));
        originSet.setBigDecimal("ID",originSet.getBigDecimal("ID").divide(jedinicniTecaj,6,BigDecimal.ROUND_HALF_UP));
        originSet.setBigDecimal("IP",originSet.getBigDecimal("IP").divide(jedinicniTecaj,6,BigDecimal.ROUND_HALF_UP));
        originSet.setBigDecimal("DUG",originSet.getBigDecimal("DUG").divide(jedinicniTecaj,6,BigDecimal.ROUND_HALF_UP));
        originSet.setBigDecimal("POT",originSet.getBigDecimal("POT").divide(jedinicniTecaj,6,BigDecimal.ROUND_HALF_UP));
        originSet.setBigDecimal("SALDO",originSet.getBigDecimal("SALDO").divide(jedinicniTecaj,6,BigDecimal.ROUND_HALF_UP));
        originSet.setBigDecimal("SALPS",originSet.getBigDecimal("SALPS").divide(jedinicniTecaj,6,BigDecimal.ROUND_HALF_UP));
        originSet.setBigDecimal("SALPROM",originSet.getBigDecimal("SALPROM").divide(jedinicniTecaj,6,BigDecimal.ROUND_HALF_UP));
      } while (originSet.next());
      
      valuta = jpVal.jtNAZVAL.getText() + " Teèaj: "+jedVl+" "+jpVal.jtOZNVAL.getText()+" - "+tecaj+" kn, na dan "+hr.restart.robno.raDateUtil.getraDateUtil().dataFormatter(jpVal.getTecajDate());
      
    } else {
      tecaj = Aus.one0.setScale(6);
      jedVl = Aus.one0;
    }

    if (stds.getString("PS").equals("D")){
      originSet.setTableName("postprom");
    } else if (stds.getString("PS").equals("P")){
      originSet.setTableName("post");
    } else {
      originSet.setTableName("prom");
    }
    
    return originSet;
  }
  
  //podaci za ispise
  
  public String getValTec(){
    String tcj = "Hrvatska Kuna";
    if (tecaj.compareTo(Aus.one0) != 0){
      tcj = valuta;
    }
    return tcj;
  }
  
  ///podaci za ispise
  
  static class FormData {
    String corg, brojkonta, nk;
    BigDecimal pocid, pocip;
    BigDecimal id, ip;
    String godmj, cvrnal;

    public void fillRow(DataSet ds, boolean dugpot, boolean gmcv) {
      ds.setString("CORG", corg);
      ds.setString("BROJKONTA", brojkonta);
      ds.setString("NK", nk);
      ds.setBigDecimal("POCID", pocid);      
      ds.setBigDecimal("POCIP", pocip);
      ds.setBigDecimal("SALPS", pocid.subtract(pocip));
      ds.setBigDecimal("ID", id);      
      ds.setBigDecimal("IP", ip);
      ds.setBigDecimal("SALPROM", id.subtract(ip));
      if (dugpot) {
        ds.setBigDecimal("DUG", pocid.add(id));      
        ds.setBigDecimal("POT", pocip.add(ip));
      }
      ds.setBigDecimal("SALDO", pocid.subtract(pocip).add(id).subtract(ip));
      if (gmcv) {
        ds.setString("GODMJ", godmj);
        ds.setString("CVRNAL", cvrnal);
      }
      ds.post();
    }
    
    public boolean allZero() {
      return id.signum() == 0 && ip.signum() == 0 && 
          pocid.signum() == 0 && pocip.signum() == 0;
    }
  }
  
  static class MainQueryData {
    String corg;
    String brojkonta;
    String godmj;
    BigDecimal id;
    BigDecimal ip;
    public MainQueryData(ResultSet row) {
      try {
        corg = row.getString(1).trim();
        brojkonta = row.getString(2).trim();
        godmj = row.getString(5).trim();
        id = new BigDecimal(row.getDouble(3)).setScale(2, BigDecimal.ROUND_HALF_UP);
        ip = new BigDecimal(row.getDouble(4)).setScale(2, BigDecimal.ROUND_HALF_UP);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Error loading main query data");
      }
    }
  }
  
  public List loadQuery(String query) {
  	DataSet nokds = Konta.getDataModule().getTempSet("BROJKONTA", Condition.equal("ISPISBB", "0"));
  	nokds.open();
  	HashSet nokon = new HashSet();
  	for (nokds.first(); nokds.inBounds(); nokds.next())
  		nokon.add(nokds.getString("BROJKONTA"));
  	
    List ret = new ArrayList();
    ResultSet rs = Util.openQuickSet(query);
    try {
      while (rs.next()) 
      	if (!nokon.contains(rs.getString(2).trim()))
      		ret.add(new MainQueryData(rs));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    Util.closeQuickSet(rs);
    System.out.println("ROWS: " + ret.size());
    return ret;
  }
  
  private String getQdsString(String knjigovodstvo) {
    String qStr = "";
    String ps = "";
    String gkkumulativi = "gkkumulativi.corg";
    String gkstavkerad = "gkstavkerad.corg";
    String gkstavke = "gkstavke.corg";
    if (stds.getString("ISPIS").equals("1")) {
      gkkumulativi = "'"+knjigovodstvo+"'";
      gkstavkerad = "'"+knjigovodstvo+"'";
      gkstavke = "'"+knjigovodstvo+"'";
    }
    
    if (stds.getString("MODE").equals("BB")){
      
//      if (stds.getString("PS").equals("D")){
//        ps = "and gkkumulativi.godmj != '"+stds.getString("GODINA")+"00' ";
//      }
      
      qStr = "select "+gkkumulativi+" as realcorg, gkkumulativi.brojkonta as brojkonta, gkkumulativi.id as id, "+
      "gkkumulativi.ip as ip, gkkumulativi.godmj as godmj  "+
      "from gkkumulativi where gkkumulativi.knjig='"+knjigovodstvo+"' and "+getCorgs("gkkumulativi")+" and "+
      "gkkumulativi.brojkonta like '"+getBrKon()+"%' "+
      "and gkkumulativi.godmj in ("+range(stds.getString("GODINA"), stds.getString("POCMJ"), stds.getString("ZAVMJ"))+")";
      
      if (stds.getString("PRIVREMENO").equals("1")){
        qStr += " UNION ALL "+
        "select "+gkstavkerad+" as realcorg, gkstavkerad.brojkonta as brojkonta, gkstavkerad.id as id, "+
        "gkstavkerad.ip as ip, gkstavkerad.godmj as godmj  "+
        "from gkstavkerad where gkstavkerad.knjig='"+knjigovodstvo+"' and "+getCorgs("gkstavkerad")+" and "+
        "gkstavkerad.brojkonta like '"+getBrKon()+"%' "+
        "and gkstavkerad.godmj in ("+range(stds.getString("GODINA"), stds.getString("POCMJ"), stds.getString("ZAVMJ"))+") ";
      }
    } else {
      
      if (stds.getString("PS").equals("N")){
        ps = "and gkstavke.cvrnal != '00' ";
      } else if (stds.getString("PS").equals("P")){
        ps = "and gkstavke.cvrnal = '00' ";
      }
      
      qStr = "select "+gkstavke+" as realcorg, gkstavke.brojkonta as brojkonta, " +
            "sum(gkstavke.id) as id, sum(gkstavke.ip) as ip, gkstavke.cvrnal as cvrnal "+
      "from gkstavke where gkstavke.knjig='"+knjigovodstvo+"' and "+getCorgs("gkstavke")+" and "+
      "gkstavke.brojkonta like '"+getBrKon()+"%' "+ps+
      "and (gkstavke."+getRange(false) + 
      " GROUP BY gkstavke.corg,gkstavke.brojkonta,gkstavke.cvrnal";
      
      if (stds.getString("PRIVREMENO").equals("1") && !stds.getString("PS").equals("P")){
        qStr +=" UNION ALL "+
        "select "+gkstavkerad+" as realcorg, gkstavkerad.brojkonta as brojkonta, " +
         "sum(gkstavkerad.id) as id, sum(gkstavkerad.ip) as ip, gkstavkerad.cvrnal as cvrnal "+
        "from gkstavkerad where gkstavkerad.knjig='"+knjigovodstvo+"' and "+getCorgs("gkstavkerad")+" and "+
        "gkstavkerad.brojkonta like '"+getBrKon()+"%' "+
        "and (gkstavkerad."+getRange(true) +
        " GROUP BY gkstavkerad.corg,gkstavkerad.brojkonta,gkstavkerad.cvrnal";
      }
    }
    
    System.out.println(qStr); //XDEBUG delete when no more needed
    return qStr;
  }
  
  private boolean checkMonthValidity(JraTextField jtf) {
    if (jtf.getText().equals("")) {
      return true;
    }
    try {
      int i = Integer.parseInt(jtf.getText());
      return ((i > 0) && (i < 13));
    } catch (Exception ex) {
      return false;
    }
  }
  
  private void modeChanged(/*ActionEvent e*/){
    System.out.println("mode changed"); //XDEBUG delete when no more needed
    if (/*e.getID() == e.ACTION_PERFORMED && */!modeCache.equals(stds.getString("MODE"))){
      modeCache = stds.getString("MODE");
      if (stds.getString("MODE").equalsIgnoreCase("BB")) {
        jpDetail.remove(jpPeriodBP);
        jpDetail.remove(jpPeriodBB);
        jpDetail.add(jpPeriodBB, new XYConstraints(150, 70, 300, 25));
      } else {
        jpDetail.remove(jpPeriodBP);
        jpDetail.remove(jpPeriodBB);
        jpDetail.add(jpPeriodBP, new XYConstraints(150, 70, 300, 25));
      }
      jpDetail.updateUI();
    }
  }

  private String getBrKon(){
    if (kontoPanel.getBrojKonta().equals(kontoPanel.jlrKontoBroj.getText().trim())) return kontoPanel.getBrojKonta();
    else return "";
  }

  private String getCorgs(String table){
    String sqlCorgString = "";
    if (stds.getString("ORGSTR").equals("1")){
      sqlCorgString = table+".CORG ='" + kontoPanel.getCorg() + "'";
    } else {
      StorageDataSet ojs = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(kontoPanel.getCorg());
      if (ojs.rowCount()==1) return table+".CORG ='" + ojs.getString("CORG").trim() + "'";
      sqlCorgString = Condition.in("CORG",ojs).qualified(table).toString();
    }
    return sqlCorgString;
  }

  private String range(String god, String mjPoc, String mjZav) {
    int PocMj = new Integer(mjPoc).intValue();
    int ZavMj = new Integer(mjZav).intValue();
    String returnStr = "";
    String tempStr = "";
    int tmp = 0;

    if (stds.getString("PS").equals("P")){
      return god+"00";
    }

    for (int i = PocMj; i <= ZavMj; i++) {
      if (tmp == 0) {
        if (stds.getString("PS").equals("D")){
          tmp++;
          returnStr = "'" + god + "00', ";
        } else {
          tmp++;
        }
      }
      
      if (i < 10) {
        tempStr = "'" + god + "0" + i + "'";
      } else {
        tempStr = "'" + god +""+ i + "'";
      }
      if (i < ZavMj) {
        returnStr += tempStr + ", ";
      } else {
        returnStr += tempStr;
      }
    }
    return returnStr;
  }
  
  
  private String getRange(boolean rad){
//    String pocstr = "";
//    if (stds.getString("PS").equals("D")){
//     pocstr =  " or gkstavke.cvrnal = '00' ";
//    }
    
    String pocstr = "";
    if (!rad){
      if (stds.getString("PS").equals("D") && (stds.getTimestamp("POCDAT").after(util.getLastSecondOfDay(util.getFirstDayOfYear())) && stds.getString("MODE").equals("BP"))){
       pocstr =  " or (gkstavke.cvrnal = '00' and gkstavke.god = '"+util.getYear(stds.getTimestamp("POCDAT"))+"') ";
      }
    }/* else {
      if (stds.getString("PS").equals("D")){
       pocstr =  " or gkstavke.cvrnal = '00' ";
      }
    }*/
    
    return Condition.between("datumknj",stds.getTimestamp("POCDAT"),stds.getTimestamp("ZAVDAT")).toString()+pocstr+")";
  }
  
  boolean incKum = false;
  
  private void initializer() throws Exception {
    
    stds.setColumns(new Column[]{dm.createStringColumn("POCMJ", "Po\u010Detni mjesec", 2),
                                 dm.createStringColumn("ZAVMJ", "Krajnji mjesec", 2),
                                 dm.createStringColumn("GODINA", "Godina", 4),
                                 dm.createTimestampColumn("POCDAT", "Od"),
                                 dm.createTimestampColumn("ZAVDAT", "Do"),
                                 dm.createStringColumn("MODE",2),  // BB - ex. bruto bilanca; BP - ex Bruto bilanca za period. 
                                 dm.createStringColumn("PRIVREMENO",1),
                                 dm.createStringColumn("ORGSTR",1),
                                 dm.createStringColumn("ISPIS",1),
                                 dm.createStringColumn("BILANCA",1),
                                 dm.createStringColumn("PS",1),
                                 dm.createStringColumn("OZNVAL","Oznaka valute",3)
                                 }
    );
    stds.open(); // hm... 

    rcbMode.setDataSet(stds);
    rcbMode.setRaColumn("MODE");
    rcbMode.setRaItems(new String[][] {
      {"Period (mm - mm gggg)","BB"},
      {"Period (od - do)","BP"}
    });
    rcbMode.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        modeChanged(/*e*/);
      }
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
    

    rcbPocStanje.setDataSet(stds);
    rcbPocStanje.setRaColumn("PS");
    rcbPocStanje.setRaItems(new String[][] {
      {"Poèetno st. + promet" ,"D"},
      {"Promet","N"},
      {"Poèetno stanje","P"}
    });
    
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
    
    //// BB
    jtMjesecPoc.setHorizontalAlignment(SwingConstants.CENTER);
    jtMjesecPoc.setDataSet(stds);
    jtMjesecPoc.setColumnName("POCMJ");

    jtMjesecZav.setHorizontalAlignment(SwingConstants.CENTER);
    jtMjesecZav.setDataSet(stds);
    jtMjesecZav.setColumnName("ZAVMJ");

    jtGodina.setHorizontalAlignment(SwingConstants.CENTER);
    jtGodina.setDataSet(stds);
    jtGodina.setColumnName("GODINA");

    //// BP
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfZavDatum.setDataSet(stds);
    jtfZavDatum.setColumnName("ZAVDAT");

    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfPocDatum.setDataSet(stds);
    jtfPocDatum.setColumnName("POCDAT");
    
    this.setJPan(jpDetail);
    
    layDetail.setWidth(578);
    layDetail.setHeight(458);
    
    layPeriod.setWidth(300);
    layPeriod.setHeight(25);
    
    jpDetail.setLayout(layDetail);
    jpPeriodBB.setLayout(layPeriod);
    jpPeriodBP.setLayout(layPeriod);

    jpPeriodBB.add(jtMjesecPoc, new XYConstraints(0, 0, 35, -1)); /// 150
    jpPeriodBB.add(jtMjesecZav, new XYConstraints(65, 0, 35, -1)); /// 215
    jpPeriodBB.add(jtGodina, new XYConstraints(105, 0, 50, -1)); /// 255
    jpPeriodBB.add(new JLabel("-"), new XYConstraints(45, 0, 14, -1)); /// 195

    jpPeriodBP.add(jtfPocDatum, new XYConstraints(0, 0, 100, -1)); /// 150
    jpPeriodBP.add(jtfZavDatum, new XYConstraints(105, 0, 100, -1)); /// 255

    jpDetail.setMinimumSize(new Dimension(600, 458));
    jpDetail.setPreferredSize(new Dimension(580, 200));
    
    kontoPanel.setNoLookup(true);
    kontoPanel.setPreferredSize(new Dimension(580, 50));
    jpDetail.add(kontoPanel, new XYConstraints(15, 20, -1, -1));

    incKum = frmParam.getFrmParam().getParam("gk","kumulBB","N","Prikaz bruto bilance i preko kumulativa D/N").equals("D");
    if (incKum)
      jpDetail.add(rcbMode, new XYConstraints(15, 70, 130, -1));
    else
      jpDetail.add(new JLabel("Period"), new XYConstraints(15, 70, 130, -1));
    
    jpDetail.add(jpPeriodBB, new XYConstraints(150, 70, 300, 25));

    jpDetail.add(new JLabel("Dokumenti / Org. st."),   new XYConstraints(15, 95, -1, -1));
    jpDetail.add(rcbPrivremenost, new XYConstraints(150,95,100,-1));
    jpDetail.add(rcbPripOrgJed, new XYConstraints(255,95,285,-1));
    
    jpDetail.add(new JLabel("Izbor bilance"), new XYConstraints(15, 120, -1, -1));
    jpDetail.add(rcbIspis, new XYConstraints(150,120,100,-1));
    jpDetail.add(rcbBilanca, new XYConstraints(255,120,100,-1));
    jpDetail.add(rcbPocStanje, new XYConstraints(360,120,180,-1));
    jpDetail.add(selOrg, new XYConstraints(545,120,21,21));

    
    jpVal.xYLayout1.setWidth(570);
    jpVal.setAlwaysSelected(true);
    jpVal.add(jpVal.jtCVAL, new XYConstraints(200, 20, 50, -1));
    jpVal.add(jpVal.jtNAZVAL, new XYConstraints(255, 20, 285, -1));
    jpVal.add(jpVal.jlNAZVAL, new XYConstraints(255, 2, -1, -1));
    jpVal.add(jpVal.jbGetVal,  new XYConstraints(545, 20, 21, 21));
    this.getJPan().add(jpVal, new XYConstraints(0, 150, -1, -1));

    jpVal.setRaDataSet(stds);
    jpVal.initJP('N');
    jpVal.setAlwaysSelected(true);
    
    jpDetail.add(jpVal, new XYConstraints(0, 145, -1, -1));
    this.installResetButton();
    
//    kontaSet = dm.getKonta();
//    kontaSet.open();
//    initialValues();
  }

  /*public String getNazKonto(String cKonto) {
    //lookupData.getlookupData().raLocate(kontaSet,"BROJKONTA",cKonto);
    //return kontaSet.getString("NAZIVKONTA");
    String kon = (String) nazKonta.get(cKonto);
    return kon != null ? kon : "";
  }*/
  
  
  public boolean Validacija() {
    if ((!stds.getString("POCMJ").equals("01") && stds.getString("MODE").equals("BB")) || 
        (stds.getTimestamp("POCDAT").after(util.getLastSecondOfDay(util.getFirstDayOfYear(stds.getTimestamp("POCDAT")))) && stds.getString("MODE").equals("BP"))){
//      int pocst = JOptionPane.showConfirmDialog(this.getJPan(), new String[]{"Ukljuèiti i poèetno stanje iako promet nije od poèetka godine?"}, "Poèetno stanje", javax.swing.JOptionPane.YES_NO_CANCEL_OPTION);
//      if (pocst == JOptionPane.NO_OPTION){
        rcbPocStanje.setSelectedIndex(1);
        stds.setString("PS","N");
//      } else if (pocst == JOptionPane.CANCEL_OPTION) return false;
    }
    return true;
  }
  
  
  
  
  private boolean isShifted = false;
  private int level = 1;
  
  private void shifts(String plusminus){
    if (!isShifted){
      level = 1;
      if (plusminus.equals("right")){ 
        level += 1;
        isShifted = true;
          System.out.println("mièem desno level "+ level); //XDEBUG delete when no more needed
      } else System.out.println("ignoriram"); //XDEBUG delete when no more needed
    } else {
      if (plusminus.equals("right")){ 
        if (level < 4)
        level += 1;
        System.out.println("mièem desno level "+ level); //XDEBUG delete when no more needed
      } else {
        level -= 1;
        System.out.println("mièem ljevo level "+ level); //XDEBUG delete when no more needed
      } 
    }
    if (level <= 4 && level >= 1 && isShifted) {
      this.getJPTV().enableEvents(false);

      seekKonto = this.getJPTV().getDataSet().getString("BROJKONTA");
      okAndInit();

      this.getJPTV().enableEvents(true);

      handleArrowExpand(level < 4);
      handleArrowColapse(level > 1);
      handleDoubleClickIcon(level == 1);
    }
    if (level == 1) isShifted = false;
  }
  
  private void handleArrowExpand(boolean enabled){
    rcc.setLabelLaF(rnvAnotherClickinTheWall, enabled); 
  }
  
  private void handleArrowColapse(boolean enabled){
    rcc.setLabelLaF(rnvAnotherBackwardClickinTheWall, enabled); 
  }
  
  private void handleDoubleClickIcon(boolean enabled){
    rcc.setLabelLaF(rnvDoubleClick, enabled); 
  }
  

  protected void addingReport(){
    killAllReports();
    this.addReport("hr.restart.gk.repBBNAMEA1", "hr.restart.gk.repBrBilAllSource", "BBNAMEprpsGeneric", "Bruto bilanca, NAZIV, poèetno stanje, promet, saldo"); // ***
    this.addJasper("repRDG", "hr.restart.gk.repBrBillRDG", "rdg.jrxml", "Raèun dobiti i gubitka (bilanca stanja)");
//    this.addReport("hr.restart.gk.repBBNAMEA1", "hr.restart.gk.repBrBilAllSource", "BBNAMEprpsWideExtendedTEST", "Bruto bilanca, NAZIV, poèetno stanje, promet, saldo"); // ***
    if (isKompletnaBilanca()) { // kompletna bilanca
      if (stds.getString("PS").equals("D")) { // poèetno stanje + promet
        this.addReport("hr.restart.gk.repBBrepA1", "hr.restart.gk.repBrBilAllSource", "BBpromPs01", "Bruto bilanca, poèetno stanje, promet, saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepA1a", "hr.restart.gk.repBrBilAllSource", "BBpromPs01a", "Bruto bilanca, poèetno stanje, promet, saldo duguje i potražuje, ukupni saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepA2", "hr.restart.gk.repBrBilAllSource", "BBpromPs02", "Bruto bilanca, duguje, potražuje, saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepA3", "hr.restart.gk.repBrBilAllSource", "BBnameSaldo01", "Bruto bilanca, naziv, konta, saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepA4", "hr.restart.gk.repBrBilAllSource", "BBpromPs03", "Bruto bilanca, naziv konta, poèetno stanje, promet, saldo u 3 reda"); // ***
        this.addReport("hr.restart.gk.repBBrepA5", "hr.restart.gk.repBrBilAllSource", "BBprprNoNaziv3Reda", "Bruto bilanca, poèetno stanje, promet, saldo u 3 reda"); // ***
      } else if (stds.getString("PS").equals("N")) { // samo promet BBrep1PSPR
        this.addReport("hr.restart.gk.repBBrepA1", "hr.restart.gk.repBrBilAllSource", "BBprom01", "Bruto bilanca, promet, saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepA3", "hr.restart.gk.repBrBilAllSource", "BBnameSaldo01Prom", "Bruto bilanca, naziv konta, saldo promet"); // ***
      } else if (stds.getString("PS").equals("P")) { // samo poèetno satnje
        this.addReport("hr.restart.gk.repBBrepA1", "hr.restart.gk.repBrBilAllSource", "BBpocst01", "Bruto bilanca, poèetno stanje, saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepA3", "hr.restart.gk.repBrBilAllSource", "BBnameSaldo01PocSt", "Bruto bilanca, naziv konta, saldo poèetno stanje"); // ***
      }
      this.addReport("hr.restart.gk.repBBrepA0", "hr.restart.gk.repBrBilAllSource", "BBnazUkuPSaldo", "Bruto bilanca, naziv, uk. promet, saldo"); // *** **X**
    } else if (isSintetik()) { // sintetièka bilanca
      if (stds.getString("PS").equals("D")) { // poèetno stanje + promet
        this.addReport("hr.restart.gk.repBBrepB1", "hr.restart.gk.repBrBilAllSource", "BBpromPs01Sintetik", "Bruto bilanca, poèetno stanje, promet, saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepB1a", "hr.restart.gk.repBrBilAllSource", "BBpromPs01aSintetik", "Bruto bilanca, poèetno stanje, promet, saldo duguje i potražuje, ukupni saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepB2", "hr.restart.gk.repBrBilAllSource", "BBpromPs02Sintetik", "Bruto bilanca, duguje, potražuje, saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepB3", "hr.restart.gk.repBrBilAllSource", "BBnameSaldo01Sintetik", "Bruto bilanca, naziv, konta, saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepB4", "hr.restart.gk.repBrBilAllSource", "BBpromPs03Sintetik", "Bruto bilanca, naziv konta, poèetno stanje, promet, saldo u 3 reda"); // ***
        this.addReport("hr.restart.gk.repBBrepB5", "hr.restart.gk.repBrBilAllSource", "BBprprNoNaziv3RedaSintetik", "Bruto bilanca, poèetno stanje, promet, saldo u 3 reda"); // ***
      } else if (stds.getString("PS").equals("N")) { // samo promet BBrep1PSPR
        this.addReport("hr.restart.gk.repBBrepB1", "hr.restart.gk.repBrBilAllSource", "BBprom01SintetikProm", "Bruto bilanca, promet, saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepB3", "hr.restart.gk.repBrBilAllSource", "BBnameSaldo01SintetikProm", "Bruto bilanca, naziv konta, saldo promet"); // ***
      } else if (stds.getString("PS").equals("P")) { // samo poèetno satnje
        this.addReport("hr.restart.gk.repBBrepB1", "hr.restart.gk.repBrBilAllSource", "BBpocst01Sintetik", "Bruto bilanca, poèetno stanje, saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepB3", "hr.restart.gk.repBrBilAllSource", "BBnameSaldo01SintetikPocSt", "Bruto bilanca, naziv konta, saldo poèetno stanje"); // ***
      }
      this.addReport("hr.restart.gk.repBBrepA0", "hr.restart.gk.repBrBilAllSource", "BBnazUkuPSaldoSintetik", "Bruto bilanca, naziv, uk. promet, saldo"); // *** **S**
    } else if (isAnalitik()) { // analitièka bilanca
      if (stds.getString("PS").equals("D")) { // poèetno stanje + promet
        this.addReport("hr.restart.gk.repBBrepC1", "hr.restart.gk.repBrBilAllSource", "BBpromPs01Analitik", "Bruto bilanca, poèetno stanje, promet, saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepC1a", "hr.restart.gk.repBrBilAllSource", "BBpromPs01aAnalitik", "Bruto bilanca, poèetno stanje, promet, saldo duguje i potražuje, ukupni saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepC2", "hr.restart.gk.repBrBilAllSource", "BBpromPs02Analitik", "Bruto bilanca, duguje, potražuje, saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepC4", "hr.restart.gk.repBrBilAllSource", "BBnameSaldo01Analitik", "Bruto bilanca, naziv, konta, saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepC3", "hr.restart.gk.repBrBilAllSource", "BBpromPs03Analitik", "Bruto bilanca, naziv konta, poèetno stanje, promet, saldo u 3 reda"); // ***
        this.addReport("hr.restart.gk.repBBrepC5", "hr.restart.gk.repBrBilAllSource", "BBprprNoNaziv3RedaAnalitik", "Bruto bilanca, poèetno stanje, promet, saldo u 3 reda"); // ***
      } else if (stds.getString("PS").equals("N")) { // samo promet BBrep1PSPR
        this.addReport("hr.restart.gk.repBBrepC1", "hr.restart.gk.repBrBilAllSource", "BBprom01Analitik", "Bruto bilanca, promet, saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepC3", "hr.restart.gk.repBrBilAllSource", "BBnameSaldo01AnalitikProm", "Bruto bilanca, naziv konta, saldo promet"); // ***
      } else if (stds.getString("PS").equals("P")) { // samo poèetno stanje
        this.addReport("hr.restart.gk.repBBrepC1", "hr.restart.gk.repBrBilAllSource", "BBpocSt01Analitik", "Bruto bilanca, poèetno stanje, saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepC3", "hr.restart.gk.repBrBilAllSource", "BBnameSaldo01AnalitikPocSt", "Bruto bilanca, naziv konta, saldo poèetno stanje"); // ***
      }
      this.addReport("hr.restart.gk.repBBrepA0", "hr.restart.gk.repBrBilAllSource", "BBnazUkuPSaldoAnalitik", "Bruto bilanca, naziv, uk. promet, saldo"); // *** **A**
    } 
    
    if (kontoPanel.jlrKontoBroj.getText().length() < 3) { // rekapitulacija
      if (stds.getString("PS").equals("D")) { // poèetno stanje + promet
        this.addReport("hr.restart.gk.repBBrepD1", "hr.restart.gk.repBrBilAllRekapitulacijaSource", "BBrekap01", "Rekapitulacija, poèetno stanje, promet, saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepD1a", "hr.restart.gk.repBrBilAllRekapitulacijaSource", "BBrekap01a", "Rekapitulacija, poèetno stanje, promet, saldo duguje i potražuje, ukupni saldo"); // ***
        this.addReport("hr.restart.gk.repBBrepD1b", "hr.restart.gk.repBrBilAllRekapitulacijaSource", "BBrekap01b", "Rekapitulacija, poèetno stanje, promet, saldo u 3 reda"); // ***
        this.addReport("hr.restart.gk.repBBrepD2", "hr.restart.gk.repBrBilAllRekapitulacijaSource", "BBrekap02", "Rekapitulacija, duguje, potražuje, saldo"); // ***
      } else if (stds.getString("PS").equals("N")) { // samo promet BBrep1PSPR
        this.addReport("hr.restart.gk.repBBrepD1", "hr.restart.gk.repBrBilAllRekapitulacijaSource", "BBrekap01Promet", "Rekapitulacija, promet, saldo"); // ***
//        this.addReport("hr.restart.gk.repBBrepD2", "hr.restart.gk.repBrBilAllRekapitulacijaSource", "BBrekap02", "Test report (B)A7.B4.C4"); // ***
      } else if (stds.getString("PS").equals("P")) { // samo poèetno satnje
        this.addReport("hr.restart.gk.repBBrepD1", "hr.restart.gk.repBrBilAllRekapitulacijaSource", "BBrekap01PocSt", "Rekapitulacija, poèetno stanje, saldo"); // ***
//        this.addReport("hr.restart.gk.repBBrepD2", "hr.restart.gk.repBrBilAllRekapitulacijaSource", "BBrekap02PocSt", "Test report (C)A7.B4.C4");
      }
      this.addReport("hr.restart.gk.repBBrepD3", "hr.restart.gk.repBrBilAllRekapitulacijaSource", "BBnameSaldoRekap", "Rekapitulacija, naziv konta, saldo"); // ***
    }
    if (!stds.getString("MODE").equals("BB")) {
      this.addReport("hr.restart.gk.repBBnal", "hr.restart.gk.repBrBilancaPerNalozi", "BBnalozi", "Bruto bilanca po nalozima"); // *** template
    }
  }
}