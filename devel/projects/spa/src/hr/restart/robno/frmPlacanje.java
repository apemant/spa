/****license*****************************************************************
**   file: frmPlacanje.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Rate;
import hr.restart.baza.dM;
import hr.restart.pos.frmMasterBlagajna;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.ActionExecutor;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.KeyAction;
import hr.restart.swing.SharedFlag;
import hr.restart.swing.raInputDialog;
import hr.restart.swing.raNumberMask;
import hr.restart.util.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class frmPlacanje extends raMatPodaci {
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  String tm="";                     // Temporarni string za citac magnetskih kartica
  char mod='B';                     // U kojem smo modu rada
  short delStavka;                   // stavka koju smo pobrisali
  boolean cekFlag;

  int oldch = 65535;
  private JLabel jLabel12 = new JLabel();

  private static String brojkart;   // Broj tekuceg racuna ili kartice
  private static String banka;      // Sifra banke ili karticara
  private static String vlasnik;    // Vlasnik
  private static String brojchek;   // Broj ceka ili slipa
  private static String vrijedido;  // Vrijedi do

  boolean autoImport;               // Da li ima citac magnetskih kartica
  boolean detMode;                  // Da li se prikazuje broj tekuceg, broj ceka
  static frmPlacanje inst;
  static QueryDataSet qdsRate;
  static QueryDataSet mDoki;

  private static raCommonClass rcc = raCommonClass.getraCommonClass();
  private Valid vl = Valid.getValid();
  private Util util = Util.getUtil();

  private static JPanel jp = new JPanel();
  private XYLayout xYLayout1 = new XYLayout();
  private JLabel jLabel1 = new JLabel();
  private JLabel jLabel2 = new JLabel();
  private JLabel jLabel3 = new JLabel();
  private JLabel jLabel4 = new JLabel();
  private JLabel jLabel5 = new JLabel();
  private JLabel jLabel6 = new JLabel();
  private JLabel jLabel7 = new JLabel();
  private JLabel jLabel13 = new JLabel();
//  private JLabel jLabel14 = new JLabel();
  private JLabel jLabel15 = new JLabel();
  private JraTextField jtfIZNOS = new JraTextField();
//  private JraTextField jtfBRRATA = new JraTextField();
  private JraTextField jtfBRCEK = new JraTextField();




  private static JraTextField jtfDATDOK = new JraTextField();
  private JlrNavField jrfCNACPL = new JlrNavField() {
    public void after_lookUp() {
     afterCNACPL(mod);
    }
  };
  private JlrNavField jrfNAZNACPL = new JlrNavField() {
    public void after_lookUp() {
      afterCNACPL(mod);
    }
  };
  private JlrNavField jrfCBANKA = new JlrNavField();
  private JlrNavField jrfNAZBANKA = new JlrNavField();
  private JraTextField jtfBROJ_TRG = new JraTextField();
  private JraTextField jtfBROJ_CEK = new JraTextField();
  private JraTextField jtfIRATA = new JraTextField();
  private JraTextField jtfDATUM = new JraTextField();
  private static JraTextField jtfUIRAC = new JraTextField();
  private JLabel jLabel8 = new JLabel();
  private JraButton jbCNACPL = new JraButton();
  private JraButton jbCBANKA = new JraButton();
  private JLabel jLabel9 = new JLabel();
  private JLabel jLabel10 = new JLabel();
  private static JraTextField jtfBROJ = new JraTextField();
  private static JraTextField jtfNAPLAC = new JraTextField();
  private static TableDataSet tdsTemp = new TableDataSet();

  private java.math.BigDecimal oldIznos = new java.math.BigDecimal(0);
  private dM dm = dM.getDataModule();

  private Column column1 = dM.createBigDecimalColumn("naplac");
  private Column column2 = dM.createStringColumn("broj", 15);
  private Column column3 = dM.createBigDecimalColumn("uirac");
  private Column column4 = dM.createTimestampColumn("datum");
  private Column column5 = dM.createStringColumn("reader", 120);
  private Column column6 = dM.createBigDecimalColumn("iznos");
//  private Column column7 = dM.createIntColumn("brrata");
  private Column column8 = dM.createIntColumn("brcek");

  private JLabel jLabel11 = new JLabel();
  private JraTextField jtfVLASNIK = new JraTextField();
  private JraTextField jtfVRIJEDIDO = new JraTextField();

  public static frmPlacanje getInstance() {
    if (inst == null) {
      inst = new frmPlacanje();
    }
    return inst;
  }

  public frmPlacanje() {
    super(3,raFrame.DIALOG, startFrame.getStartFrame());
    getJdialog().setModal(true);

//  	super(3);

    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    inst = this;
  }

  private void jbInit() throws Exception {
    this.setRaDetailPanel(jp);
    this.setRaQueryDataSet(qdsRate);
    this.setVisibleCols(new int[] {4,5,6,10,11});
    //this.getJpTableView().getColumnsBean().setSaveSettings(false);

    jp.setLayout(xYLayout1);
    jLabel1.setText("Naèin plaæanja");
    jLabel2.setText("Banka");
    jLabel3.setText("Tekuæi raèun");
    jLabel4.setText("Serijski broj èeka");
    jLabel5.setText("Iznos");
    jLabel6.setText("Datum za naplatu");
    jLabel7.setText("Iznos raèuna");
    jLabel8.setText("Datum raèuna");

    jLabel13.setText("Iznos za rate");
//    jLabel14.setText("Broj rata");
    jLabel15.setText("Broj rata");

    xYLayout1.setWidth(560);
    xYLayout1.setHeight(290);
    jbCNACPL.setText("...");
    jbCBANKA.setText("...");

    jtfDATDOK.setDataSet(tdsTemp);
    jtfDATDOK.setColumnName("DATUM");
    jtfDATDOK.setFont(jtfDATDOK.getFont().deriveFont(Font.BOLD));
    jtfDATDOK.setForeground(Color.red);
    jtfDATDOK.setHorizontalAlignment(SwingConstants.CENTER);

    jtfUIRAC.setDataSet(tdsTemp);
    jtfUIRAC.setColumnName("UIRAC");
    jtfUIRAC.setFont(jtfUIRAC.getFont().deriveFont(Font.BOLD));
    jtfUIRAC.setForeground(Color.red);

    jtfBROJ.setColumnName("BROJ");
    jtfBROJ.setDataSet(tdsTemp);
    jtfBROJ.setFont(jtfBROJ.getFont().deriveFont(Font.BOLD));
    jtfBROJ.setForeground(Color.red);
    jtfBROJ.setHorizontalAlignment(SwingConstants.CENTER);
    jtfNAPLAC.setColumnName("NAPLAC");
    jtfNAPLAC.setDataSet(tdsTemp);
    jtfNAPLAC.setFont(jtfNAPLAC.getFont().deriveFont(Font.BOLD));
    jtfNAPLAC.setForeground(Color.red);
  

    jtfBROJ_TRG.setDataSet(qdsRate);
    jtfBROJ_TRG.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        jtfBROJ_TRG_keyTyped(e);
      }
    });
    jtfBROJ_TRG.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        jtfBROJ_TRG_keyReleased(e);
      }
    });
    jtfBROJ_TRG.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfBROJ_TRG_focusGained(e);
      }
    });
    jtfBROJ_TRG.setColumnName("BROJ_TRG");
    jtfBROJ_CEK.setDataSet(qdsRate);
    jtfBROJ_CEK.setColumnName("BROJ_CEK");
    jtfIRATA.setDataSet(qdsRate);
    jtfIRATA.setColumnName("IRATA");
    jtfDATUM.setDataSet(qdsRate);
    jtfDATUM.setColumnName("DATUM");
    jtfBROJ.setDataSet(tdsTemp);
    jtfBROJ.setColumnName("BROJ");
    jtfNAPLAC.setDataSet(tdsTemp);
    jtfNAPLAC.setColumnName("NAPLAC");

    jtfIZNOS.setDataSet(tdsTemp);
    jtfIZNOS.setColumnName("IZNOS");
//    jtfBRRATA.setDataSet(tdsTemp);
//    jtfBRRATA.setColumnName("BRRATA");
    jtfBRCEK.setDataSet(tdsTemp);
    jtfBRCEK.setColumnName("BRCEK");


    jrfCNACPL.setColumnName("CNACPL");
    jrfCNACPL.setDataSet(qdsRate);
    jrfCNACPL.setColNames(new String[] {"NAZNACPL"});
    jrfCNACPL.setVisCols(new int[]{0,1});
    jrfCNACPL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZNACPL});
    jrfCNACPL.setRaDataSet(dm.getNacpl());
    jrfCNACPL.setNavButton(jbCNACPL);
    jrfCNACPL.setRaDataSet(dm.getNacpl());
    jrfNAZNACPL.setColumnName("NAZNACPL");
    jrfNAZNACPL.setNavProperties(jrfCNACPL);
    jrfNAZNACPL.setSearchMode(1);
    jrfCNACPL.setAfterLookAlways(true);

    jrfCBANKA.setColumnName("CBANKA");
    jrfCBANKA.setDataSet(qdsRate);
    jrfCBANKA.setColNames(new String[] {"NAZIV"});
    jrfCBANKA.setVisCols(new int[]{0,1});
    jrfCBANKA.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZBANKA});
    jrfCBANKA.setNavButton(jbCBANKA);
    jrfCBANKA.setRaDataSet(dm.getBanke());
    jrfNAZBANKA.setColumnName("NAZIV");
    jrfNAZBANKA.setNavProperties(jrfCBANKA);
    jrfNAZBANKA.setRaDataSet(dm.getBanke());
    jrfNAZBANKA.setSearchMode(1);

    jLabel9.setText("Broj raèuna");
    jLabel10.setText("Naplaæeno");
    tdsTemp.setColumns(new Column[] {column1, column2, column3, column4, column5, column6, column8});
    jLabel11.setText("Vrijedi do");
    jtfVLASNIK.setColumnName("VLASNIK");
    jtfVLASNIK.setDataSet(qdsRate);
    jtfVRIJEDIDO.setColumnName("VRIJEDIDO");
    jtfVRIJEDIDO.setDataSet(qdsRate);
    jLabel12.setText("Vlasnik");
    jp.add(jLabel1, new XYConstraints(15, 75, -1, -1));
    jp.add(jrfNAZNACPL, new XYConstraints(255, 75, 265, -1));
    jp.add(jbCNACPL, new XYConstraints(524, 75, 21, 21));
    jp.add(jrfCNACPL,  new XYConstraints(150, 75, 100, -1));
    jp.add(jLabel7,  new XYConstraints(15, 45, -1, -1));
    jp.add(jLabel10,   new XYConstraints(300, 45, -1, -1));
    jp.add(jtfNAPLAC,    new XYConstraints(420, 45, 100, -1));
    jp.add(jLabel8,    new XYConstraints(300, 20, -1, -1));
    jp.add(jLabel9,  new XYConstraints(15, 20, -1, -1));
    jp.add(jtfDATDOK,  new XYConstraints(420, 20, 100, -1));
    jp.add(jtfBROJ,   new XYConstraints(150, 20, 100, -1));
    jp.add(jtfUIRAC,   new XYConstraints(150, 45, 100, -1));
    jp.add(jLabel2, new XYConstraints(15, 100, -1, -1));
    jp.add(jrfCBANKA, new XYConstraints(150, 100, -1, -1));
    jp.add(jrfNAZBANKA, new XYConstraints(255, 100, 265, -1));
    jp.add(jbCBANKA, new XYConstraints(524, 100, 21, 21));

    jp.add(jLabel13, new XYConstraints(15, 125, -1, -1));
    jp.add(jtfIZNOS, new XYConstraints(150, 125, 100, -1));
//    jp.add(jLabel14, new XYConstraints(260, 125, -1, -1));
//    jp.add(jtfBRRATA, new XYConstraints(335, 125, 50, -1));
    jp.add(jLabel15, new XYConstraints(405, 125, -1, -1));
    jp.add(jtfBRCEK, new XYConstraints(470, 125, 50, -1));

    jp.add(jLabel3, new XYConstraints(15, 175, -1, -1));
    jp.add(jtfBROJ_TRG, new XYConstraints(150, 175, 180, -1));
    jp.add(jLabel12, new XYConstraints(15, 200, -1, -1));
    jp.add(jtfVLASNIK, new XYConstraints(150, 200, 370, -1));
    jp.add(jLabel4, new XYConstraints(15, 225, -1, -1));
    jp.add(jtfBROJ_CEK, new XYConstraints(150, 225, -1, -1));
    jp.add(jLabel5, new XYConstraints(15, 250, -1, -1));
    jp.add(jtfIRATA, new XYConstraints(150, 250, -1, -1));
    jp.add(jLabel11, new XYConstraints(300, 225, -1, -1));
    jp.add(jtfVRIJEDIDO, new XYConstraints(420, 225, -1, -1));
    jp.add(jLabel6, new XYConstraints(300, 250, -1, -1));
    jp.add(jtfDATUM, new XYConstraints(420, 250, -1, -1));

  }
  public static boolean justCheckRate(QueryDataSet master) {
    java.math.BigDecimal tmp=hr.restart.robno.Util.getUtil().getNaplac(master);
    System.out.println(tmp.doubleValue()+"="+master.getBigDecimal("UIRAC").doubleValue());
    if (tmp.doubleValue()==master.getBigDecimal("UIRAC").doubleValue()) {
    	System.out.println("return true");
    	return true;
    }
    else {
    	System.out.println("return false");
    	return false;
    }
  }
  
  private static void insertSingle(QueryDataSet master, String nacpl) {
    qdsRate.insertRow(false);
    qdsRate.setString("CSKL", master.getString("CSKL"));
    qdsRate.setString("VRDOK", master.getString("VRDOK"));
    qdsRate.setString("GOD", master.getString("GOD"));
    qdsRate.setInt("BRDOK", master.getInt("BRDOK"));
    if (master.getString("VRDOK").equals("GRC")) {
      qdsRate.setString("CPRODMJ", master.getString("CPRODMJ"));
    }
    else {
      qdsRate.setString("CPRODMJ", "XXX");
    }
    qdsRate.setShort("RBR", (short) 1);
    qdsRate.setTimestamp("DATUM", master.getTimestamp("DATDOK"));
    qdsRate.setTimestamp("DATDOK", master.getTimestamp("DATDOK"));
    qdsRate.setBigDecimal("IRATA", master.getBigDecimal("UIRAC"));
    qdsRate.setString("CNACPL", nacpl);
    master.setString("CNACPL", nacpl);
  }
  
  static StorageDataSet allNac(boolean simple) {
    DataSet dsn = dM.getDataModule().getNacpl();
    DataSet dsb = dM.getDataModule().getBanke();
    DataSet dsk = dM.getDataModule().getKartice();
    dsn.open();
    dsb.open();
    dsk.open();
    StorageDataSet nac = new StorageDataSet();
    nac.setColumns(new Column[] {
       dM.createIntColumn("SORT"),
       dsn.getColumn("CNACPL").cloneColumn(),
       dM.createStringColumn("NAZIV", "Naziv", 250),
       dM.createStringColumn("CBANKA", "Banka", 4)
    });
    nac.open();
    int sort = 10;
    for (dsn.first(); dsn.inBounds(); dsn.next()) {
      if (!simple && dsn.getString("FL_KARTICA").equals("D") && dsk.rowCount() > 0) {
        for (dsk.first(); dsk.inBounds(); dsk.next()) {
          nac.insertRow(false);
          nac.setInt("SORT", ++sort);
          nac.setString("CNACPL", dsn.getString("CNACPL"));
          nac.setString("CBANKA", dsk.getString("CBANKA"));
          nac.setString("NAZIV", dsn.getString("NAZNACPL") 
              + " - " + dsk.getString("NAZIV"));
          nac.post();
        }
      } else if (!simple && dsn.getString("FL_CEK").equals("D") && dsb.rowCount() > 0) {
        for (dsb.first(); dsb.inBounds(); dsb.next()) {
          nac.insertRow(false);
          nac.setInt("SORT", ++sort);
          nac.setString("CNACPL", dsn.getString("CNACPL"));
          nac.setString("CBANKA", dsb.getString("CBANKA"));
          nac.setString("NAZIV", dsn.getString("NAZNACPL") 
              + " - " + dsb.getString("NAZIV"));
          nac.post();
        }
      } else {
        nac.insertRow(false);
        nac.setInt("SORT", ++sort);
        nac.setString("CNACPL", dsn.getString("CNACPL"));
        if (dsn.getString("CNACPL").equals("G"))
          nac.setInt("SORT", 1);
        nac.setString("NAZIV", dsn.getString("NAZNACPL"));
        nac.post();
      }
    }
    return nac;
  }
  
  private static int prevRow = -1;
  private static boolean createSingleRata(raMasterDetail rmd, boolean simple) {    
    QueryDataSet master = rmd.getMasterSet();
    
    StorageDataSet nac = allNac(simple);
    nac.insertRow(false);
    nac.setInt("SORT", 100);
    nac.setString("CNACPL", "");
    nac.setString("NAZIV", "Odabir više rata");
    nac.post();
    if (qdsRate.getRowCount() == 0 || 
      !lookupData.getlookupData().raLocate(nac, 
          new String[] {"CNACPL", "CBANKA"}, new String[] 
             {qdsRate.getString("CNACPL"), qdsRate.getString("CBANKA")}))
      if (prevRow >= 0) nac.goToClosestRow(prevRow);
      else nac.first();

    lookupData.getlookupData().saveName = "dohvat-nacpl";
    lookupData.getlookupData().frameTitle = "Odabir naèina plaæanja";
    lookupData.getlookupData().setLookMode(lookupData.INDIRECT);
    try {
      String[] result = lookupData.getlookupData().lookUp(
            rmd.raDetail.getWindow(), nac,
            new String[] {"SORT", "CNACPL", "CBANKA"}, 
            new String[] {"", "", ""}, new int[] {1,2});
      if (result == null) return true;
      if (result[1].length() == 0) {
        nac.last();
        prevRow = nac.getRow();
        return false;
      }
      lookupData.getlookupData().raLocate(nac, 
          new String[] {"SORT", "CNACPL", "CBANKA"}, result);
      prevRow = nac.getRow();
      if (qdsRate.getRowCount()==1) {
        qdsRate.setBigDecimal("IRATA", master.getBigDecimal("UIRAC"));
        qdsRate.setString("CNACPL", result[1]); 
      } else insertSingle(master, result[1]);
      qdsRate.setString("CBANKA", result[2]);
      qdsRate.saveChanges();
      master.setString("CNACPL", result[1]);
      return true;
    } finally {
      lookupData.getlookupData().saveName = null;
      lookupData.getlookupData().frameTitle = "Dohvat";
    }
  }
  
  public static boolean checkRate(raMasterDetail rmd) {
    QueryDataSet master = rmd.getMasterSet();
    java.math.BigDecimal tmp=hr.restart.robno.Util.getUtil().getNaplac(master);
    hr.restart.baza.Rate tempRate = hr.restart.baza.Rate.getDataModule();
    if (qdsRate==null) qdsRate = tempRate.getTempSet(Condition.nil);
    if (!master.getString("VRDOK").equals("GRC"))
      tempRate.setFilter(qdsRate, Condition.whereAllEqual(Util.mkey, master));
    else tempRate.setFilter(qdsRate, Condition.
        whereAllEqual(frmMasterBlagajna.key, master));
    qdsRate.open();
    if (master.getBigDecimal("UIRAC").compareTo(_Main.nul)==0) {
      qdsRate.deleteAllRows();
      qdsRate.saveChanges();
      //hr.restart.robno.Util.getUtil().emptyTable(qdsRate);
      return true;
    }
    else if (tmp.compareTo(_Main.nul)==0) {
      insertSingle(master, frmParam.getParam("robno","gotNacPl"));
      qdsRate.saveChanges();
      return true;
    }
    else if (qdsRate.getRowCount()==1 /* && qdsRate.getString("CNACPL").equals(hr.restart.sisfun.frmParam.getParam("robno","gotNacPl"))*/) {
      qdsRate.setBigDecimal("IRATA", master.getBigDecimal("UIRAC"));
      qdsRate.saveChanges();
      master.setString("CNACPL", qdsRate.getString("CNACPL"));
      return true;
    }
    else if (tmp.setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(master.getBigDecimal("UIRAC").setScale(2,BigDecimal.ROUND_HALF_UP))==0) {
      master.setString("CNACPL", qdsRate.getString("CNACPL"));
      return true;
    }
    else if (checkAutoNaplata()) {
//      return true;
    }
    qdsRate.open();
    mDoki=master;

    getFrmPlacanje();

    tdsTemp.open();
    System.out.println("master.getBigDecimal(UIRAC) "+master.getBigDecimal("UIRAC"));
    if (tdsTemp.getRowCount()==0) tdsTemp.insertRow(false);
    String tmpString="000000"+master.getInt("BRDOK");
    tdsTemp.setString("broj", master.getString("GOD")+"-"+tmpString.substring(tmpString.length()-6, tmpString.length()));
    tdsTemp.setBigDecimal("uirac", master.getBigDecimal("UIRAC"));
    tdsTemp.setTimestamp("datum", master.getTimestamp("DATDOK"));
//    tdsTemp.setTimestamp("datdok", master.getTimestamp("DATDOK"));
//    tdsTemp.setBigDecimal("naplac", new java.math.BigDecimal(0.00));
    tdsTemp.setBigDecimal("NAPLAC", hr.restart.robno.Util.getUtil().getNaplac(mDoki));
    frmplacanje.show();
    if (qdsRate.rowCount() == 1)
      master.setString("CNACPL", qdsRate.getString("CNACPL"));
    rmd.raDetail.requestFocus();

    return justCheckRate(master);
  }
  
  protected static frmPlacanje frmplacanje;
  public static boolean entryRate(raMasterDetail rmd) {
    QueryDataSet master = rmd.getMasterSet();
  	System.out.println("EntryRate"+jtfBROJ.isEnabled());

  	hr.restart.baza.Rate tempRate = hr.restart.baza.Rate.getDataModule();
    if (qdsRate==null) qdsRate = tempRate.getTempSet(Condition.nil);
    if (!master.getString("VRDOK").equals("GRC"))
      tempRate.setFilter(qdsRate, Condition.whereAllEqual(Util.mkey, master));
    else tempRate.setFilter(qdsRate, Condition.
        whereAllEqual(frmMasterBlagajna.key, master));
    qdsRate.open();
    mDoki=master;
    
    if ("D".equals(frmParam.getParam("zapod", "fancyPlac", "D",
        "Novi naèin naplate (D,N)", true))) {
      return new FancyPlac().show(rmd.raDetail.getWindow());
    }
    
    if (qdsRate.rowCount() <= 1) {
      String sr = frmParam.getParam("zapod", "singleRata", "D",
          "Izbor jedinstvene rate plaæanja (D,N,S)", true);
      if (!sr.equalsIgnoreCase("N") && 
          createSingleRata(rmd, sr.equalsIgnoreCase("S"))) return true;
    }

    getFrmPlacanje();

    
    tdsTemp.open();
    if (tdsTemp.getRowCount()==0) tdsTemp.insertRow(false);
    String tmpString="000000"+master.getInt("BRDOK");
    tdsTemp.setString("broj", master.getString("GOD")+"-"+tmpString.substring(tmpString.length()-6, tmpString.length()));
    tdsTemp.setBigDecimal("uirac", master.getBigDecimal("UIRAC"));
    tdsTemp.setTimestamp("datum", master.getTimestamp("DATDOK"));
//    tdsTemp.setTimestamp("datdok", master.getTimestamp("DATDOK"));
//    tdsTemp.setBigDecimal("naplac", new java.math.BigDecimal(0.00));
    tdsTemp.setBigDecimal("NAPLAC", hr.restart.robno.Util.getUtil().getNaplac(mDoki));
    rcc.EnabDisabAll(jp,false);

    while (true) {
      frmplacanje.show();
      if (Aus.sum("IRATA", qdsRate).compareTo(master.getBigDecimal("UIRAC")) == 0
        || JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
            rmd.raDetail.getWindow(), "Suma rata ne odgovara iznosu raèuna!", 
            "Greška", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.ERROR_MESSAGE)) break;
    }
    fillMasterNac();
    rmd.raDetail.requestFocus();
    return true;
  }
  
  static void fillMasterNac() {
    if (qdsRate.rowCount() == 0)
      mDoki.setString("CNACPL", "");
    else if (qdsRate.rowCount() == 1)
      mDoki.setString("CNACPL", 
          qdsRate.getString("CBANKA").length() > 0 ?
            qdsRate.getString("CBANKA") :
            qdsRate.getString("CNACPL"));
    else {
      VarStr all = new VarStr();
      for (qdsRate.first(); qdsRate.inBounds(); qdsRate.next()) {
        all.append(qdsRate.getString("CBANKA").length() > 0 ?
            qdsRate.getString("CBANKA") :
            qdsRate.getString("CNACPL")).append('+');
      }
      mDoki.setString("CNACPL", all.chop().truncate(
          mDoki.getColumn("CNACPL").getPrecision()).toString());
    }
  }

  public static String frmPlacanjeClassName = "hr.restart.robno.frmPlacanje"; 
  protected static void getFrmPlacanje() {
    if (frmplacanje == null)
        frmplacanje = (frmPlacanje)_Main.getStartFrame().showFrame(frmPlacanjeClassName,0, "Plaæanje",false);
  }
  public void EntryPoint(char mode) {
    rcc.setLabelLaF(jtfBROJ, false);
    rcc.setLabelLaF(jtfDATDOK, false);
    rcc.setLabelLaF(jtfUIRAC, false);
    rcc.setLabelLaF(jtfNAPLAC, false);
  }
  public void SetFokus(char mode) {
    mod=mode;
    if (mode=='N') {
      enableRateFileds(false);
      enableCekFields(false);
      qdsRate.setString("CSKL", mDoki.getString("CSKL"));
      qdsRate.setString("VRDOK", mDoki.getString("VRDOK"));
      qdsRate.setString("GOD", mDoki.getString("GOD"));
      qdsRate.setInt("BRDOK", mDoki.getInt("BRDOK"));
      if (mDoki.getString("VRDOK").equals("GRC")) {
        qdsRate.setString("CPRODMJ", mDoki.getString("CPRODMJ"));
      }
      else {
        qdsRate.setString("CPRODMJ", "XXX");
      }
      qdsRate.setShort("RBR", util.getMaxRbr4Rate(mDoki));
      qdsRate.setTimestamp("DATUM", vl.getToday());
      qdsRate.setTimestamp("DATDOK", mDoki.getTimestamp("DATDOK"));
      qdsRate.setBigDecimal("IRATA", util.negateValue(tdsTemp.getBigDecimal("UIRAC"), tdsTemp.getBigDecimal("NAPLAC")));
      jrfNAZNACPL.setText("");
      oldIznos=util.nul;
    }
    else if (mode=='I') {
      enableRateFileds(detMode);
      enableCekFields(false);
      rcc.setLabelLaF(jrfCNACPL, false);
      rcc.setLabelLaF(jrfNAZNACPL, false);
      rcc.setLabelLaF(jbCNACPL, false);
      oldIznos=qdsRate.getBigDecimal("IRATA");
    }
    if (mode=='N') jrfCNACPL.requestFocus();
    else jtfIRATA.requestFocus();
  }
  public boolean Validacija(char mode) {
    if (mode == 'B') return false;
/*    if (tdsTemp.getBigDecimal("UIRAC").doubleValue()<tdsTemp.getBigDecimal("NAPLAC").doubleValue()+qdsRate.getBigDecimal("IRATA").doubleValue()) {
      JOptionPane.showConfirmDialog(jtfBRCEK.getParent(),"Iznos naplate veæi od iznosa raèuna !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }*/
    if (vl.isEmpty(jrfCNACPL)) return false;
    if (mode=='N' && cekFlag) {
      if (vl.isEmpty(jtfBRCEK)) return false;
//      if (vl.isEmpty(jtfBRRATA)) return false;
      if (vl.isEmpty(jtfIZNOS)) return false;
//      if (tdsTemp.getInt("BRRATA")>tdsTemp.getInt("BRCEK")) {
//        JOptionPane.showConfirmDialog(jtfBRCEK.getParent(),"Broj rata veæi od broja èekova !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
//        return false;
//      }
      int brc=tdsTemp.getInt("BRCEK");
      short maxrat=util.getMaxRbr4Rate(mDoki);
      String cnacplac=qdsRate.getString("CNACPL");
      String csifbank=qdsRate.getString("CBANKA");
      String cvlasnik=qdsRate.getString("VLASNIK");
      String ctrgkart=qdsRate.getString("BROJ_TRG");
      String cvrijedi=qdsRate.getString("VRIJEDIDO");
      java.math.BigDecimal druga=new java.math.BigDecimal(tdsTemp.getBigDecimal("IZNOS").intValue()/tdsTemp.getInt("BRCEK"));
      java.math.BigDecimal prva=tdsTemp.getBigDecimal("IZNOS").subtract(druga.multiply(new java.math.BigDecimal(tdsTemp.getInt("BRCEK")-1)));
      this.getRaQueryDataSet().cancel();
      for (int i=0;i<brc;i++) {
        this.getRaQueryDataSet().insertRow(false);
        this.getRaQueryDataSet().setString("VRDOK", mDoki.getString("VRDOK"));
        this.getRaQueryDataSet().setInt("BRDOK", mDoki.getInt("BRDOK"));
        if (mDoki.getString("VRDOK").equals("GRC")) {
          qdsRate.setString("CPRODMJ", mDoki.getString("CPRODMJ"));
        }
        else {
          qdsRate.setString("CPRODMJ", "XXX");
        }

        this.getRaQueryDataSet().setString("GOD", mDoki.getString("GOD"));
        this.getRaQueryDataSet().setString("CSKL", mDoki.getString("CSKL"));
        this.getRaQueryDataSet().setShort("RBR", (short)(maxrat+i));
        this.getRaQueryDataSet().setString("CNACPL", cnacplac);
        this.getRaQueryDataSet().setString("BROJ_TRG", ctrgkart);
        this.getRaQueryDataSet().setString("VLASNIK", cvlasnik);
        this.getRaQueryDataSet().setString("VRIJEDIDO", cvrijedi);

        this.getRaQueryDataSet().setString("CBANKA", csifbank);
        this.getRaQueryDataSet().setTimestamp("DATDOK", mDoki.getTimestamp("DATDOK"));
        this.getRaQueryDataSet().setTimestamp("DATUM",  ut.addDays(mDoki.getTimestamp("DATDOK"),i*30));
        if (i==0) {
          this.getRaQueryDataSet().setBigDecimal("IRATA", prva);
        }
        else {
          this.getRaQueryDataSet().setBigDecimal("IRATA", druga);
        }
        this.getRaQueryDataSet().post();
      }
    }
    return true;
  }
  public boolean ValDPEscape(char mode) {
    mod='B';
    tdsTemp.setBigDecimal("IZNOS", _Main.nul);
    jrfNAZBANKA.setText("");
    jrfNAZNACPL.setText("");
    return super.ValDPEscape(mode);
  }
  public boolean doWithSave(char mode) {
    return true;
  }
  hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();
  private void afterCNACPL(char mod) {
    autoImport=false;
    dm.getNacpl().open();
//    dm.getNacpl().interactiveLocate(qdsRate.getString("CNACPL"), "CNACPL" ,Locate.FIRST, false);
    if (!lD.raLocate(dm.getNacpl(),"CNACPL",qdsRate.getString("CNACPL"))){
      return;
    }
    if (dm.getNacpl().getString("FL_CEK").equals("D")) {
      detMode=true;
      jLabel2.setText("Banka");
      jLabel3.setText("Tekuæi raèun");
      jLabel4.setText("Serijski broj èeka");

      jrfCBANKA.setColumnName("CBANKA");
      jrfCBANKA.setDataSet(qdsRate);
      jrfCBANKA.setColNames(new String[] {"NAZIV"});
      jrfCBANKA.setVisCols(new int[]{0,1});
      jrfCBANKA.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZBANKA});
      jrfCBANKA.setNavButton(jbCBANKA);
      jrfCBANKA.setRaDataSet(dm.getBanke());
      jrfNAZBANKA.setColumnName("NAZIV");
      jrfNAZBANKA.setNavProperties(jrfCBANKA);
      jrfNAZBANKA.setRaDataSet(dm.getBanke());
      jrfNAZBANKA.setSearchMode(1);
      if (mod=='B') {
      	rcc.EnabDisabAll(jp,false);
      }
      else {
        enableRateFileds(true);
        enableCekFields(true);
        fillScreen();
        if (mod=='N') {
            cekFlag=true;
            tdsTemp.setInt("brcek", 1);
            tdsTemp.setBigDecimal("IZNOS", util.negateValue(tdsTemp.getBigDecimal("UIRAC"), tdsTemp.getBigDecimal("NAPLAC")));
            rcc.setLabelLaF(jtfIRATA, false);
            rcc.setLabelLaF(jtfDATUM, false);
            rcc.setLabelLaF(jtfBROJ_CEK, false);
          }
          else {
            rcc.setLabelLaF(jtfIZNOS, false);
            rcc.setLabelLaF(jtfBRCEK, false);
          }
      }

    }
    else if (dm.getNacpl().getString("FL_KARTICA").equals("D")) {
      detMode=true;
      if (mod!='B') {
        enableRateFileds(true);
        enableCekFields(true);
        fillScreen();
      }
      jLabel2.setText("Kartica");
      jLabel3.setText("Broj kartice");
      jLabel4.setText("Serijski broj slipa");

      jrfCBANKA.setColumnName("CBANKA");
      jrfCBANKA.setDataSet(qdsRate);
      jrfCBANKA.setColNames(new String[] {"NAZIV"});
      jrfCBANKA.setVisCols(new int[]{0,1});
      jrfCBANKA.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZBANKA});
      jrfCBANKA.setNavButton(jbCBANKA);
      jrfCBANKA.setRaDataSet(dm.getKartice());
      jrfNAZBANKA.setColumnName("NAZIV");
      jrfNAZBANKA.setNavProperties(jrfCBANKA);
      jrfNAZBANKA.setRaDataSet(dm.getKartice());
      jrfNAZBANKA.setSearchMode(1);

      if ("D".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno","mcReader"))) {
        autoImport=true;
      }

      rcc.setLabelLaF(jtfIZNOS, false);
      rcc.setLabelLaF(jtfBRCEK, false);
//      rcc.setLabelLaF(jtfBRCEK, true);
    }
    else {
      if (this.mod!='B') rcc.setLabelLaF(jtfIRATA, true);
      detMode=false;
      if (qdsRate.getRowCount()>0) {
        qdsRate.setString("CBANKA", "");
        jrfCBANKA.forceFocLost();
        qdsRate.setString("BROJ_TRG", "");
        qdsRate.setString("BROJ_CEK", "");
        qdsRate.setString("VLASNIK", "");
        qdsRate.setString("VRIJEDIDO", "");
      }
    }
  }
  private void enableRateFileds(boolean aha) {
    rcc.setLabelLaF(jrfCBANKA, aha);
    rcc.setLabelLaF(jbCBANKA, aha);
    rcc.setLabelLaF(jrfNAZBANKA, aha);
    rcc.setLabelLaF(jtfBROJ_CEK, aha);
    rcc.setLabelLaF(jtfBROJ_TRG, aha);
    rcc.setLabelLaF(jtfDATUM, aha);
    rcc.setLabelLaF(jtfVLASNIK, aha);
    rcc.setLabelLaF(jtfVRIJEDIDO, aha);
    rcc.setLabelLaF(jtfIZNOS, aha);
    jrfCBANKA.forceFocLost();
  }
  private void enableCekFields(boolean aha) {
    rcc.setLabelLaF(jtfIZNOS, aha);
//    rcc.setLabelLaF(jtfBRRATA, aha);
    rcc.setLabelLaF(jtfBRCEK, aha);
  }

  public void AfterSave(char mode) {
    if (cekFlag) {
      tdsTemp.setBigDecimal("NAPLAC", util.sumValue(tdsTemp.getBigDecimal("NAPLAC"), tdsTemp.getBigDecimal("IZNOS")));
      cekFlag=false;
    }
    else {
      tdsTemp.setBigDecimal("NAPLAC", util.sumValue(tdsTemp.getBigDecimal("NAPLAC"), qdsRate.getBigDecimal("IRATA"), oldIznos.negate()));
    }
    if (detMode) {
/*      banka = qdsRate.getString("CBANKA");
      brojkart = qdsRate.getString("BROJ_TRG");
      brojchek = qdsRate.getString("BROJ_CEK");
      vlasnik = qdsRate.getString("VLASNIK");
      vrijedido = qdsRate.getString("VRIJEDIDO");
*/
    }
    tdsTemp.setBigDecimal("IZNOS", _Main.nul);
    tdsTemp.setInt("BRCEK", 0);
    mod='B';
  }
  public void AfterAfterSave(char mode) {
    if (tdsTemp.getBigDecimal("NAPLAC").equals(tdsTemp.getBigDecimal("UIRAC"))) {
      super.setMode('I');
      super.AfterAfterSave('I');
    }
    else {
      super.AfterAfterSave(mode);
    }
  }
  public boolean DeleteCheck() {
    oldIznos=qdsRate.getBigDecimal("IRATA");
    return true;
  }
  public boolean BeforeDelete() {
    delStavka=getRaQueryDataSet().getShort("RBR");
    return true;
  }
  public void AfterDelete() {
    hr.restart.robno.Util.getUtil().recalcRBR(getRaQueryDataSet() , delStavka, 'N');
    tdsTemp.setBigDecimal("NAPLAC", util.sumValue(tdsTemp.getBigDecimal("NAPLAC"), oldIznos.negate()));
  }

  String getVlasnik(String st) {
    for (int i=0; i<st.length(); i++) {
//      System.out.println("Slovo: "+st.charAt(i));
    }
    return st;
  }
  String getVrijediDo(String st) {
    return st.substring(2,4)+"/"+"20"+st.substring(0,2);
  }

  void jtfBROJ_TRG_focusGained(FocusEvent e) {
    tm="";
  }

  void jtfBROJ_TRG_keyReleased(KeyEvent e) {
    if (tm=="end") return;
    if (tm.length()==7 && (tm.substring(0,2).equals("vd"))) {
      qdsRate.setString("VRIJEDIDO", getVrijediDo(tm.substring(3, tm.length())));
      tm="end";// flag za kraj
      return;
    }
    else if (e.getKeyCode()==18 && oldch==17 && tm.substring(0,2).equals("ip")) {
      qdsRate.setString("VLASNIK", getVlasnik(tm.substring(3, tm.length())).trim());
      tm="vd";// flag za vrijedi do
    }
    else if (e.getKeyCode()==18 && oldch==17) {
      qdsRate.setString("BROJ_TRG", tm.substring(3, tm.length()-1).trim());
      tm="ip";// flag za ime i prezime
    }
    tm=tm+e.getKeyChar();
    oldch=e.getKeyCode();
  }

  void jtfBROJ_TRG_keyTyped(KeyEvent e) {
    if (autoImport) e.consume();
  }
  private static boolean checkAutoNaplata() {
    return false;
  }
  private void clearScreen() {
    brojkart="";
    banka="";
    vlasnik="";
    brojchek="";
    vrijedido="";
  }
  private void fillScreen() {
/*    qdsRate.setString("CBANKA", banka);
    jrfCBANKA.forceFocLost();
    qdsRate.setString("BROJ_TRG", brojkart);
    qdsRate.setString("BROJ_CEK", brojchek);
    qdsRate.setString("VLASNIK", vlasnik);
    qdsRate.setString("VRIJEDIDO", vrijedido);*/
  }

  static class FancyPlac extends raInputDialog {
    
    JPanel main = new JPanel();
    XYLayout lay = new XYLayout();
    List combo = new ArrayList();
    List rata = new ArrayList();
    List act = new ArrayList();
    int num;
    
    StorageDataSet nacs;
    String[][] itm;
    
    JraTextField jraTotal = new JraTextField();
    JraButton jbAdd = new JraButton();
    
    SharedFlag csf = new SharedFlag();
    ActionExecutor exec = new ActionExecutor(csf) {
      public void run() {
        press((JraButton) obj);
      }
    };
    ActionListener comm = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        exec.invoke(e.getSource());
      }
    };
    
    public FancyPlac() {
      
    }
    
    protected boolean checkOk() {
      Set placs = new HashSet();
      for (int i = 0; i < combo.size(); i++) {
        raComboBox cb = (raComboBox) combo.get(i);
        if (!placs.add(cb.getDataValue())) {
          cb.requestFocus();
          JOptionPane.showMessageDialog(win, "Naèin plaæanja se ne smije ponavljati!",
              "Greška", JOptionPane.ERROR_MESSAGE);
          return false;
        }
        JraTextField tf = (JraTextField) rata.get(i);
        BigDecimal num = Aus.getDecNumber(tf.getText());
        if (num.signum() == 0) {
          tf.requestFocus();
          JOptionPane.showMessageDialog(win, "Rata ne smije biti nula!",
              "Greška", JOptionPane.ERROR_MESSAGE);
          return false;
        }
        if (num.signum() != mDoki.getBigDecimal("UIRAC").signum()) {
          tf.requestFocus();
          JOptionPane.showMessageDialog(win, "Rata ne smije biti suprotnog predznaka od raèuna!",
              "Greška", JOptionPane.ERROR_MESSAGE);
          return false;
        }
      }
      
      if (mDoki.getString("RDOK").length() > 0 
          && JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(
              win, "Raèun je veæ razdužen. Želite li ažurirati naplatu?",
              "Promjena razduženja", JOptionPane.OK_CANCEL_OPTION,
              JOptionPane.WARNING_MESSAGE)) return false;
      
      return true;
    }
        
    public boolean show(Container parent) {
      generatePanel();
      
      if (super.show(parent, main, "Naplata")) {
        int i = 0;
        for (qdsRate.first(); qdsRate.inBounds() && i < combo.size(); 
            qdsRate.next(), i++) {
          String sort = ((raComboBox) combo.get(i)).getDataValue();
          lookupData.getlookupData().raLocate(nacs, "SORT", sort);
          BigDecimal irata = Aus.getDecNumber(((JraTextField) rata.get(i)).getText());
          qdsRate.setBigDecimal("IRATA", irata);
          qdsRate.setString("CNACPL", nacs.getString("CNACPL"));
          qdsRate.setString("CBANKA", nacs.getString("CBANKA"));
        }
        while (qdsRate.inBounds() && qdsRate.rowCount() > combo.size()) 
          qdsRate.deleteRow();
        for ( ; i < combo.size(); i++) {
          String sort = ((raComboBox) combo.get(i)).getDataValue();
          lookupData.getlookupData().raLocate(nacs, "SORT", sort);
          BigDecimal irata = Aus.getDecNumber(((JraTextField) rata.get(i)).getText());
          insertSingle(mDoki, nacs.getString("CNACPL"));
          qdsRate.setString("CBANKA", nacs.getString("CBANKA"));
          qdsRate.setBigDecimal("IRATA", irata);
          qdsRate.setShort("RBR", (short) (i + 1));
        }
        qdsRate.saveChanges();
        fillMasterNac();
        if (mDoki.getString("RDOK").length() > 0)
          updateRate(mDoki.getString("RDOK"));
      }
      return true;
    }
    
    void updateRate(String rdok) {
      String q =
        "select m.cskl, r.cnacpl, r.cbanka, r.irata, r.cprodmj "+
        "from pos m, rate r where " + Util.getUtil().getDoc("m", "r") +
        " AND m.rdok='"+rdok+"'";
      
      DataSet rate = raPOS.getRate(q);
      
      String[] cols = {"CSKL", "CNACPL", "CBANKA", "IRATA", "CPRODMJ"};
      
      String[] parts = new VarStr(rdok).splitTrimmed('-');
      QueryDataSet oldr = Rate.getDataModule().getTempSet(
          Condition.equal("CSKL", parts[0]).
          and(Condition.equal("VRDOK", parts[1])).
          and(Condition.equal("GOD", parts[2])).
          and(Condition.equal("BRDOK", Aus.getNumber(parts[3]))));
      oldr.open();
      short rbr = 0;
      Timestamp dat = null;
      for (oldr.first(), rate.first(); oldr.inBounds() && rate.inBounds(); oldr.next(), rate.next()) {
        dat = oldr.getTimestamp("DATDOK");
        if (oldr.getShort("RBR") > rbr)
          rbr = oldr.getShort("RBR");
        dM.copyColumns(rate, oldr);
      }
      while (oldr.inBounds() && oldr.rowCount() > rate.rowCount())
        oldr.deleteRow();
      for ( ; rate.inBounds(); rate.next()) {
        oldr.insertRow(false);
        oldr.setString("CSKL", parts[0]);
        oldr.setString("VRDOK", parts[1]);
        oldr.setString("GOD", parts[2]);
        oldr.setInt("BRDOK", Aus.getNumber(parts[3]));
        oldr.setShort("RBR", ++rbr);
        oldr.setTimestamp("DATDOK", dat);
        oldr.setTimestamp("DATUM", dat);
        dM.copyColumns(rate, oldr);
      }
      
      oldr.saveChanges();
    }
    
    protected void beforeShow() {
      super.beforeShow();
      AWTKeyboard.registerKeyStroke(win, AWTKeyboard.F2,
          new KeyAction() {
            public boolean actionPerformed() {
              if (combo.size() >= 8) return false;
              exec.invokeLater(jbAdd);
              return true;
            }
          });
      AWTKeyboard.registerKeyStroke(win, AWTKeyboard.F3,
          new KeyAction() {
        public boolean actionPerformed() {
          if (combo.size() <= 1) return false;
          Component comp = AWTKeyboard.getFocusedComponent();
          if (combo.indexOf(comp) >= 0)
            exec.invokeLater(act.get(combo.indexOf(comp)));
          else if (rata.indexOf(comp) >= 0)
            exec.invokeLater(act.get(rata.indexOf(comp)));
          else return false;
          return true;
          }
        });
    }

    void generatePanel() {
      if (qdsRate.rowCount() == 0)
        insertSingle(mDoki, frmParam.getParam("robno","gotNacPl"));
      
      num = qdsRate.getRowCount();
      
      lay.setWidth(500);
      lay.setHeight(60 + 30 * num);
      main.setLayout(lay);

      new raNumberMask(jraTotal, 2);
      jraTotal.setText(Aus.formatBigDecimal(mDoki.getBigDecimal("UIRAC")));
      raCommonClass.getraCommonClass().setLabelLaF(jraTotal, false);
      jbAdd.setIcon(raImages.getImageIcon(raImages.IMGADD));
      jbAdd.setToolTipText("Dodaj ratu");
      jbAdd.addActionListener(comm);

      main.add(new JLabel("Iznos raèuna" +
          (mDoki.getString("RDOK").length() > 0 ? "  (razdužen!)" : "")), 
          new XYConstraints(15, 15, -1, -1));
      main.add(jraTotal, new XYConstraints(350, 15, 100, -1));
      main.add(jbAdd, new XYConstraints(460, 15, 21, 21));
      
      nacs = allNac(false);
      nacs.setSort(new SortDescriptor(new String[] {"SORT"}));
      itm = new String[nacs.rowCount()][2];
      int n = 0;
      for (nacs.first(); nacs.inBounds(); nacs.next(), n++) {
        itm[n][0] = nacs.getString("NAZIV");
        itm[n][1] = Integer.toString(nacs.getInt("SORT"));
      }
      
      n = 0;
      for (qdsRate.first(); qdsRate.inBounds(); qdsRate.next(), n++) {
        createLine(n);
        if (qdsRate.getString("CBANKA").length() == 0)
          lookupData.getlookupData().raLocate(nacs, "CNACPL",
              qdsRate.getString("CNACPL"));
        else lookupData.getlookupData().raLocate(nacs, 
            new String[] {"CNACPL", "CBANKA"}, 
            new String[] {qdsRate.getString("CNACPL"),
                          qdsRate.getString("CBANKA")});
        ((raComboBox) combo.get(n)).setDataValue(Integer.toString(nacs.getInt("SORT")));
        ((JraTextField) rata.get(n)).setText(Aus.formatBigDecimal(qdsRate.getBigDecimal("IRATA")));
        
        addLine(n, 50);
      }
    }
    
    void calc(JraTextField tf) {
      if (rata.size() == 1) {
        if (tf.getText().equals(jraTotal.getText())) return;
        tf.setText(jraTotal.getText());
      } else {
        int idx = rata.indexOf(tf);
        BigDecimal total = Aus.zero2;
        for (int i = 0; i < rata.size(); i++) {
          total = total.add(Aus.getDecNumber(
              ((JraTextField) rata.get(i)).getText()));
        }
        total = total.subtract(mDoki.getBigDecimal("UIRAC"));
        if (total.signum() == 0) return;
        
        JraTextField sub = (JraTextField) rata.get(
            idx == rata.size() - 1 ? 0 : rata.size() - 1);
        sub.setText(Aus.formatBigDecimal(
            Aus.getDecNumber(sub.getText()).subtract(total)));
      }
    }
    
    void press(JraButton but) {
      if (but == jbAdd) {
        lay.setHeight(90 + 30 * combo.size());
        for (int i = combo.size() - 1; i >= 0; i--) {
          removeLine(i);
          addLine(i, 80);
        }
        createLine(0);
        ((JraTextField) rata.get(0)).setText(
            Aus.formatBigDecimal(Aus.zero2));
        addLine(0, 50);
        win.pack();
        ((JraTextField) rata.get(0)).requestFocusLater();
        return;
      }
      if (act.size() == 1) return;

      int idx = act.indexOf(but);
      
      main.remove((Component) combo.remove(idx));
      main.remove((Component) rata.remove(idx));
      main.remove((Component) act.remove(idx));
      for (int i = idx; i < combo.size(); i++) {
        removeLine(i);
        addLine(i, 50);
      }
      calc((JraTextField) rata.get(0)); 
      lay.setHeight(60 + 30 * combo.size());
      win.pack();
      ((JraTextField) rata.get(
          rata.size() == idx ? idx - 1 : idx)).requestFocusLater();
    }
    
    void removeLine(int i) {
      main.remove((Component) combo.get(i));
      main.remove((Component) rata.get(i));
      main.remove((Component) act.get(i));
    }
    
    void addLine(int i, int y) {
      main.add((Component) combo.get(i), 
          new XYConstraints(15, y + i*30, 325, -1));
      main.add((Component) rata.get(i), 
          new XYConstraints(350, y + i*30, 100, -1));
      main.add((Component) act.get(i), 
          new XYConstraints(460, y + i*30, 21, 21));
    }
    
    void createLine(int i) {
      raComboBox cb = new raComboBox();
      cb.setRaItems(itm);
      combo.add(i, cb);
      cb.setMaximumRowCount(16);
      
      JraTextField tf = new JraTextField() {
        public void valueChanged() {
          calc(this);
        }
      };
      new raNumberMask(tf, 2);
      rata.add(i, tf);
      
      JraButton but = new JraButton();
      but.setIcon(raImages.getImageIcon(raImages.IMGDELETE));
      but.setToolTipText("Obriši ratu");
      act.add(i, but);
      but.addActionListener(comm);
    }
  }
}
