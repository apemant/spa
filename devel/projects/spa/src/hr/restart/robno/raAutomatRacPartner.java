/****license*****************************************************************
**   file: raAutomatRacPartner.java
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

import hr.restart.baza.VTText;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raFrame;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.startFrame;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * Automatski prijenos aktivnih ugovora u racun
 * @author unascribed
 * @version 1.0
 */

//public class raAutomatRac extends JraDialog {
public class raAutomatRacPartner extends raFrame {

  private static raAutomatRacPartner rAR;
  private raCommonClass rcc =raCommonClass.getraCommonClass();
  private boolean escPresBefore;
//  private static boolean inHouse = true;
  public static raAutomatRacPartner getraAutomatRac(){
    if (rAR== null) rAR = new raAutomatRacPartner();
//    inHouse = false;
    return rAR;
  }
  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
//  private raControlDocs rCD = new raControlDocs();
  private String greska = "";
  private hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  private Column cGODINA = new Column();
  private Column cMJESEC = new Column();
  private Column cDATUM = new Column();
  private Column cOPIS = new Column();
  private Column cDVO = new Column();
  private Column cDATDOSP = new Column();
  private Column cDANIDOSP = new Column();
  private Column cCNAP ;
  private hr.restart.util.reports.raRunReport rr = hr.restart.util.reports.raRunReport.getRaRunReport();
  private QueryDataSet DummyArtiklSet  = new QueryDataSet();
  private raselectPartner rsP = new raselectPartner();
  private QueryDataSet DummySet = new QueryDataSet();  
  private hr.restart.util.lookupData lD  = hr.restart.util.lookupData.getlookupData();
  
  private QueryDataSet prenosPartner;
  
  {
    cCNAP = (Column) dm.getNapomene().getColumn("CNAP").clone();
    cGODINA.setColumnName("GODINA");
    cGODINA.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    cGODINA.setPrecision(8);
    cGODINA.setDisplayMask("yyyy");
    cMJESEC.setColumnName("MJESEC");
    cMJESEC.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    cMJESEC.setPrecision(8);
    cMJESEC.setDisplayMask("MM");

    cDATUM.setColumnName("DATUM");
    cDATUM.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    cDATUM.setPrecision(8);
    cDATUM.setDisplayMask("dd-MM-yyyy");

    cDVO.setColumnName("DVO");
    cDVO.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    cDVO.setPrecision(8);
    cDVO.setDisplayMask("dd-MM-yyyy");

    cDANIDOSP.setColumnName("DANIDOSP");
    cDANIDOSP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    cDANIDOSP.setPrecision(4);

    cDATDOSP.setColumnName("DATDOSP");
    cDATDOSP.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    cDATDOSP.setPrecision(8);
    cDATDOSP.setDisplayMask("dd-MM-yyyy");

    cOPIS.setColumnName("OPIS");
    cOPIS.setPrecision(200);    // mijenjati na 200
    cOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    DummySet.setColumns(new Column[] {cGODINA,cMJESEC,cDATUM,cDVO,cDATDOSP,cDANIDOSP,cOPIS,cCNAP});
    DummySet.open();
    
  }
  
  private QueryDataSet zagRac;
  private QueryDataSet stavRac;
  private QueryDataSet vtText;

  private QueryDataSet allForIspis = new QueryDataSet();
  private String[][] mnemonicTable = new String[2][2] ;
  private QueryDataSet Ugovors = null;
  
  private hr.restart.util.OKpanel okp = new hr.restart.util.OKpanel(){
    public void  jBOK_actionPerformed(){
      presOK();
    }
    public void  jPrekid_actionPerformed(){
      presCancel();
    }
  };

  private JPanel panel1 = new JPanel();
  private JPanel panelmali = new JPanel();
  private BorderLayout borderLayout2 = new BorderLayout();
  private XYLayout xYLayout1 = new XYLayout();
  private XYLayout XYLayoutmali = new XYLayout();
  private JLabel lmjesec = new JLabel("Mjesec i godina");
  private JraTextField godina = new JraTextField();
  private JraTextField mjesec = new JraTextField();
  private JLabel ldatum = new JLabel("Datum dokumenta");
  private JraTextField datum  = new JraTextField() {
    public void valueChanged() {
      datum_focusLost(null);
    }
  };
  private JLabel ldvo = new JLabel("DVO");
  private JraTextField dvo  = new JraTextField() {
    public void valueChanged() {
      dvo_focusLost(null);
    }
  };
  private JLabel ldanidosp = new JLabel("Dani dospije\u0107a");
  private JraTextField danidosp  = new JraTextField() {
    public void valueChanged() {
      danidosp_focusLost(null);
    }
  };
  private JLabel ldatdosp = new JLabel("Datum dospije\u0107a");
  private JraTextField datosp  = new JraTextField() {
    public void valueChanged() {
      datosp_focusLost(null);
    }
  };
  private JLabel jlCORG = new JLabel();
  private JlrNavField jlrCORG = new JlrNavField();
  private JlrNavField jlrNAZIV = new JlrNavField();
  private JraButton jbGetCorg = new JraButton();
  private JLabel jlARTIKL = new JLabel("Stavka ra\u010Duna");
  private JlrNavField jlrCART = new JlrNavField();
  private JlrNavField jlrCART1 = new JlrNavField();
  private JlrNavField jlrBC = new JlrNavField();
  private JlrNavField jlrNAZART = new JlrNavField();
  private JlrNavField jlrJM = new JlrNavField();
  private JraButton jbGetCart = new JraButton();

  private JLabel jlZiro = new JLabel("Žiro ra\u010Dun");
  private JlrNavField jlrZiro = new JlrNavField();
  private JraButton jbGetZiro = new JraButton();
  private JLabel lgodina = new JLabel("Za godinu");
  private hr.restart.swing.JraRadioButton jbpartneri =
      new hr.restart.swing.JraRadioButton("Partneri");
  private hr.restart.swing.raButtonGroup BG =
      new hr.restart.swing.raButtonGroup();

  private JLabel jlNapomenaD = new JLabel("Dodatna napomena");
  private hr.restart.swing.JraTextArea jraNapomena =
      new hr.restart.swing.JraTextArea();

  private JLabel jlNapomena = new JLabel("Napomena ");
  private JlrNavField jlrCNAP = new JlrNavField();
  private JlrNavField jlrNAZNAP = new JlrNavField(){
    public void after_lookUp() {
        jlrNAZNAP.setCaretPosition(0);
      }
  };
  private JraButton jbGetNap = new JraButton();

  public raAutomatRacPartner() {

    zagRac = doki.getDataModule().getTempSet("1=0");
    stavRac = stdoki.getDataModule().getTempSet("1=0");
    vtText = VTText.getDataModule().getTempSet("1=0");
    
    Valid.setApp(this.getClass());
    try {
      rAR = this;
      Column [] dokicols = dm.getDoki().getColumns();
      Column [] stdokicols = dm.getStdoki().getColumns();
      Column [] vttextcols = dm.getVTText().getColumns();
      HashMap hm = new HashMap();

      for (int i = 0;i< dokicols.length;i++) {
        allForIspis.addColumn((Column) dokicols[i].clone());
      }
      ArajToHashMap(hm,allForIspis.getColumnNames(allForIspis.getColumnCount()));
      for (int i = 0;i< stdokicols.length;i++) {
        if (!hm.containsKey(stdokicols[i].getColumnName())) {
          allForIspis.addColumn((Column) stdokicols[i].clone());
        }
      }

      ArajToHashMap(hm,allForIspis.getColumnNames(allForIspis.getColumnCount()));

      for (int i = 0;i< vttextcols.length;i++) {
        if (!hm.containsKey(vttextcols[i].getColumnName())) {
          allForIspis.addColumn((Column) vttextcols[i].clone());
        }
      }
      allForIspis.open();
      rr.clearAllReports();
      rr.addReport("hr.restart.robno.repRacUsluga","hr.restart.robno.repUslIzlazni","RacUsluga","Ispis ra\u010Duna");
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {

    rr.setOwner(this,getClass().getName());
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig,String newKnjig) {
        jlrCORG.getRaDataSet().refresh();
        jlrNAZIV.getRaDataSet().refresh();
      }
    });

    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent e) {
        componentShow();
      }
    });
    this.addKeyListener(new java.awt.event.KeyAdapter(){
      public void keyPressed(java.awt.event.KeyEvent e){
        if (e.getKeyCode()==java.awt.event.KeyEvent.VK_F10){
          presOK();
        }
        else if (e.getKeyCode()==java.awt.event.KeyEvent.VK_ESCAPE){

          presCancel();
        }
      }
    });

    jbpartneri.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        jbpartneri_itemStateChanged(e);
      }
    });
    /*datum.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        datum_focusLost(e);
      }
    });*/
    /*dvo.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        dvo_focusLost(e);
      }
    });*/
    /*datosp.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        datosp_focusLost(e);
      }
    });*/
    /*danidosp.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        danidosp_focusLost(e);
      }
    });*/

    BG.add(jbpartneri);
    jbpartneri.setHorizontalTextPosition(SwingUtilities.RIGHT);

    jraNapomena.setColumnName("OPIS");
    jraNapomena.setBorder(datosp.getBorder());
    jraNapomena.setDataSet(DummySet);
    jraNapomena.addKeyListener(new java.awt.event.KeyAdapter(){
      public void keyPressed(java.awt.event.KeyEvent e){
        keyPresseda(e);
      }
    });

    jbGetZiro.setText("...");
    jlrZiro.setColumnName("ZIRO");
    jlrZiro.setVisCols(new int[]{0,1});
    jlrZiro.setNavButton(jbGetZiro);

    jlCORG.setText("Org. jedinica");
    jlrCORG.setColumnName("CORG");
    jlrCORG.setColNames(new String[] {"NAZIV"});
    jlrCORG.setVisCols(new int[]{0,1,2});
    jlrCORG.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZIV});
    jlrCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrNAZIV.setColumnName("NAZIV");
    jlrNAZIV.setSearchMode(1);
    jlrNAZIV.setNavProperties(jlrCORG);
    jbGetCorg.setText("...");
    jlrCORG.setNavButton(jbGetCorg);
 // odavde je cart ne zaboraviiii   treba promijeniti


    jlrCART.setColumnName("CART");
    jlrCART.setColNames(new String[] {"CART1","BC","NAZART","JM"});
    jlrCART.setVisCols(new int[]{2,3,4,5});
    jlrCART.setTextFields(new javax.swing.text.JTextComponent[] {jlrCART1,jlrBC,jlrNAZART,jlrJM});
//    jlrCART.setRaDataSet(DummyArtiklSet);
    jlrCART1.setColumnName("CART1");
    jlrCART1.setSearchMode(1);
//    jlrCART1.setNavProperties(jlrCART);
    jlrBC.setColumnName("BC");
    jlrBC.setSearchMode(3);
//    jlrBC.setNavProperties(jlrCART);

    jlrNAZART.setColumnName("NAZART");
    jlrNAZART.setSearchMode(1);
    jlrJM.setColumnName("JM");
    jlrJM.setSearchMode(-1);
//    jlrNAZART.setNavProperties(jlrCART);
    jbGetCart.setText("...");
    jlrCART.setNavButton(jbGetCart);
/////

    panel1.setBorder(BorderFactory.createEtchedBorder());
//    panel1.setPreferredSize(new Dimension (400,150));
    this.getContentPane().setLayout(borderLayout2);

    lmjesec.setPreferredSize(new Dimension(100,21));
    lmjesec.setMinimumSize(lmjesec.getPreferredSize());
    mjesec.setPreferredSize(new Dimension(100,21));
    mjesec.setMinimumSize(mjesec.getPreferredSize());

    mjesec.setDataSet(DummySet);
    mjesec.setColumnName("MJESEC");
    mjesec.setHorizontalAlignment(SwingUtilities.CENTER);

    datum.setDataSet(DummySet);
    datum.setColumnName("DATUM");
    datum.setHorizontalAlignment(SwingUtilities.CENTER);

    dvo.setDataSet(DummySet);
    dvo.setColumnName("DVO");
    dvo.setHorizontalAlignment(SwingUtilities.CENTER);

    danidosp.setDataSet(DummySet);
    danidosp.setColumnName("DANIDOSP");
    danidosp.setHorizontalAlignment(SwingUtilities.CENTER);

    datosp.setDataSet(DummySet);
    datosp.setColumnName("DATDOSP");
    datosp.setHorizontalAlignment(SwingUtilities.CENTER);


    jlrCNAP.setColumnName("CNAP");
    jlrCNAP.setDataSet(DummySet);
    jlrCNAP.setColNames(new String[] {"NAZNAP"});
    jlrCNAP.setVisCols(new int[]{0,1});
    jlrCNAP.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZNAP});
    jlrCNAP.setRaDataSet(dm.getNapomene());
    jlrCNAP.setNavButton(jbGetNap);
    jlrNAZNAP.setColumnName("NAZNAP");
//    jlrNAZNAP.setSearchMode(1);
    jlrNAZNAP.setNavProperties(jlrCNAP);
    jbGetNap.setText("...");

    lgodina.setPreferredSize(new Dimension(100,21));
    lgodina.setMinimumSize(lgodina.getPreferredSize());
    godina.setPreferredSize(new Dimension(100,21));
    godina.setMinimumSize(godina.getPreferredSize());

    godina.setDataSet(DummySet);
    godina.setColumnName("GODINA");
    godina.setHorizontalAlignment(SwingUtilities.CENTER);

    getContentPane().add(panel1, BorderLayout.CENTER);
    getContentPane().add(okp, BorderLayout.SOUTH);
    panel1.setLayout(xYLayout1);
    xYLayout1.setHeight(310);
    xYLayout1.setWidth(570);

    panelmali.setLayout(XYLayoutmali);
    panelmali.setBorder(BorderFactory.createEtchedBorder());
    XYLayoutmali.setHeight(42);
    XYLayoutmali.setWidth(270);
    panelmali.add(jbpartneri,       new XYConstraints(180, 10, -1, -1));
    panel1.add(panelmali,       new XYConstraints(257, 60, -1, -1));

    panel1.add(jlCORG,    new XYConstraints(15,  10, -1, -1));
    panel1.add(jlrCORG,   new XYConstraints(150, 10, 100, -1));
    panel1.add(jlrNAZIV,  new XYConstraints(255, 10, 275, -1));
    panel1.add(jbGetCorg, new XYConstraints(539, 10, 21, 21));

    panel1.add(jlARTIKL,     new XYConstraints(15, 135, -1, -1));
    panel1.add(jlrCART,    new XYConstraints(150, 135, 100, -1));
    panel1.add(jlrCART1,    new XYConstraints(255, 135, 120, -1));
    panel1.add(jlrBC,      new XYConstraints(380, 135, 150, -1));
    panel1.add(jbGetCart,  new XYConstraints(539, 135, 21, 21));
    panel1.add(jlrNAZART,              new XYConstraints(150, 160, 380, -1));
    panel1.add(jlrJM,          new XYConstraints(539, 160, 25, -1));
    panel1.add(jlZiro,    new XYConstraints(15, 185, -1, -1));
    panel1.add(jbGetZiro,  new XYConstraints(539, 185, 21, 21));
    panel1.add(jlrZiro,     new XYConstraints(150, 185, 380, -1));
    panel1.add(ldatum,   new XYConstraints(15, 35, -1, -1) );
    panel1.add(datum,    new XYConstraints(150, 35, 100, -1) );
    panel1.add(ldvo,   new XYConstraints(255, 35, -1, -1) );
    panel1.add(dvo,    new XYConstraints(300, 35, 100, -1) );

    panel1.add(ldanidosp,   new XYConstraints(405, 35, -1, -1) );

    panel1.add(ldatdosp,   new XYConstraints(15, 60, -1, -1) );
    panel1.add(datosp,    new XYConstraints(150, 60, 100, -1) );
    panel1.add(lmjesec,    new XYConstraints(15, 85, -1, -1) );
    panel1.add(mjesec,     new XYConstraints(150, 85, 35, -1) );
    panel1.add(godina,      new XYConstraints(190, 85, 60, -1) );
    panel1.add(jlNapomena,     new XYConstraints(15, 210, -1, -1)); //110
    panel1.add(jlrCNAP,  new XYConstraints(150, 210, 100, -1) );
    panel1.add(jlrNAZNAP,  new XYConstraints(255, 210, 275, -1) );
    panel1.add(jbGetNap,new XYConstraints(539, 210, 21, 21) );

    panel1.add(jlNapomenaD,     new XYConstraints(15, 235, -1, -1));
    panel1.add(jraNapomena,    new XYConstraints(150, 235, 381, 60));
    panel1.add(danidosp,  new XYConstraints(500, 35, 30, -1));

  }

  public void keyPresseda(java.awt.event.KeyEvent e){
    if (e.isConsumed()) return;
    if (e.getKeyCode()==KeyEvent.VK_F9){
      int[] viscols={0,1};
//      String[] result = lD.lookUp(this.getOwner(),dm.getNapomene(),viscols);
      String[] result = lD.lookUp(this.getWindow(),dm.getNapomene(),viscols);
      if (result!=null) {
        jraNapomena.setText(result[3]);
      }
      e.consume();
    }
  }


  /**
   *
   * @return  true  --> postoji artikl usluge
   *          false --> ne postoji artikl usluge
   *
   */

  private boolean isArtiklUslugaExist(){
    boolean retValue =  hr.restart.util.Util.getNewQueryDataSet
                       ("select * FROM Artikli where vrart = 'U'",true).getRowCount()>0;
    return retValue;
  }

  /**
   *
   */

  public void componentShow() {
//    inHouse = true;
    escPresBefore = false;
    hr.restart.zapod.OrgStr.getOrgStr().getKnjigziro(
                         OrgStr.getKNJCORG());
    hr.restart.zapod.OrgStr.getOrgStr().getCurrentKnjigziro().open();

    DummyArtiklSet = hr.restart.util.Util.getNewQueryDataSet
//                     ("select * from artikli where vrart ='U'",true);
                     ("SELECT * FROM Artikli,Porezi WHERE artikli.cpor = porezi.cpor and vrart = 'U'",true);

    jlrCART.setText("");
    jlrCART.emptyTextFields();
    jlrCART.setRaDataSet(DummyArtiklSet);
    jlrCART1.setNavProperties(jlrCART);
    jlrBC.setNavProperties(jlrCART);
    jlrNAZART.setNavProperties(jlrCART);

    jlrZiro.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getCurrentKnjigziro());

    hr.restart.util.lookupData.getlookupData().raLocate(hr.restart.zapod.OrgStr.getOrgStr().getCurrentKnjigziro(),
        new String[] {"CORG"},
        new String[] {OrgStr.getKNJCORG()});
    jlrZiro.setText(hr.restart.zapod.OrgStr.getOrgStr().getCurrentKnjigziro().getString("ZIRO"));
//    jlrZiro
    if (DummyArtiklSet.getRowCount()==1) jlrCART.keyF9Pressed();

    DummySet.reset();
//    DummySet.setTimestamp("GODINA",getDatumMjesecManji(val.getToday())); // ovo se možda i vrati
//    DummySet.setTimestamp("MJESEC",getDatumMjesecManji(val.getToday()));
    DummySet.setTimestamp("GODINA",val.getToday());
    DummySet.setTimestamp("MJESEC",val.getToday());
    DummySet.setTimestamp("DATUM",val.getToday());
/*
    DummySet.setTimestamp("DATDOSP",val.getToday());
    DummySet.setTimestamp("DVO",val.getToday());
*/
    DummySet.setShort("DANIDOSP",(short)7);
    datum_focusLost(null);


//    jlrCORG.setText("");
//    jlrCORG.emptyTextFields();
    jlrCORG.setText(OrgStr.getKNJCORG());
    jlrCORG.forceFocLost();

  }
  public java.sql.Timestamp getDatumMjesecManji(java.sql.Timestamp orgTime){
    return new java.sql.Timestamp(
        raDateUtil.getraDateUtil().addMonth(new java.util.Date(orgTime.getTime()),-1).getTime());
  }

  public boolean Validacija(){

    if (val.isEmpty(jlrCART)) return false;
    boolean retValue = isArtiklUslugaExist();
    if (!retValue) {
      javax.swing.JOptionPane.showConfirmDialog(this,"Ne postoji u tabeli artikala artikl usluge !","Greška !",
          javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.DEFAULT_OPTION);
      return false;
    }

    if (val.isEmpty(jlrCORG)) {
    return false;
    }
    if (val.isEmpty(datum))  {
      return false;
    }
    if (val.isEmpty(mjesec)) {
      return false;
    }
    if (val.isEmpty(godina)) {
      return false;
    }
    return retValue;
  }

  public boolean addHeaders(){
    startFrame.getStartFrame().getStatusBar().startTask(100,"Priprema za kreiranje dokumenata iz ugovora");
    //dm.getDoki().open();
    //dm.getStdoki().open();
    //dm.getVTText().open();
    if (Ugovors.getRowCount()<1) {
      greska = "Ne postoje ugovori za generiranje više ra\u010Duna !";
      startFrame.getStartFrame().getStatusBar().finnishTask();
      return false;
    }
    allForIspis.emptyAllRows();
    Ugovors.first();
    startFrame.getStartFrame().getStatusBar().next("Kreiranje dokumenata iz ugovora");
    int brdok = 0;
    String cOpis = jlrCORG.getText()+
                "RAC"+val.findYear(DummySet.getTimestamp("DATUM"));
    try {
//     brdok = val.findSeqInteger(cOpis,false).intValue();
     brdok = val.findSeqInteger(cOpis,false,true).intValue();
    } catch (Exception ex) {
      val.unlockCurrentSeq(true);
      ex.printStackTrace();
      greska = "Greška u kreiranju više raèuna !";
      startFrame.getStartFrame().getStatusBar().finnishTask();
      javax.swing.JOptionPane.showConfirmDialog(this,greska,"Greška !",
        javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.DEFAULT_OPTION);
      return false;
    }
    do{
      try {
      addAll(brdok);
      brdok++;
      } catch (Exception ex) {
        ex.printStackTrace();
        greska = "Greška u kreiranju više raèuna !";
        startFrame.getStartFrame().getStatusBar().finnishTask();
        javax.swing.JOptionPane.showConfirmDialog(this,greska,"Greška !",
          javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.DEFAULT_OPTION);
        return false;
      }
    } while (Ugovors.next());
//    val.setSeqFilter(cOpis);
//System.out.println(cOpis+"-"+brdok+"---seq je "+ dm.getSeq().getString("OPIS"));
    dm.getSeq().setDouble("BROJ", brdok-1);
    startFrame.getStartFrame().getStatusBar().finnishTask();
    return true;
  }

  public boolean addHeadersPartner(){
System.out.println("addHeadersPartner");

    startFrame.getStartFrame().getStatusBar().startTask(100,"Priprema za izradu dokumenata");
    prenosPartner = null;
    prenosPartner = rsP.getDesniSet();
ST.prn(prenosPartner);
    if (prenosPartner == null) {
      greska = "Nisu pravilno odabrani partneri za generiranje više ra\u010Duna ! (null)";
      startFrame.getStartFrame().getStatusBar().finnishTask();
      return false;
    }
    if (prenosPartner.getRowCount()<1) {
      greska = "Nisu pravilno odabrani partneri za generiranje više ra\u010Duna !";
      startFrame.getStartFrame().getStatusBar().finnishTask();
      return false;
    }
    /**@todo dali postoji bez naziva stavke i iznosa*/
    allForIspis.emptyAllRows();
    prenosPartner.first();
    /**@todo dali postoji bez naziva stavke i iznosa*/
    boolean isOK = true;
    do {
      if (prenosPartner.getBigDecimal("IZNOS").doubleValue()== 0) isOK = false;
      if (prenosPartner.getString("TEXTSTAV").equals("")) isOK = false;
      if (!isOK) break;
    } while (prenosPartner.next());
    if (!isOK) {
      greska = "Postoje partneri za koje nije zadana cijena ili naziv !";
      startFrame.getStartFrame().getStatusBar().finnishTask();
      return false;
    }
    prenosPartner.first();
    //dm.getDoki().open();
    //dm.getStdoki().open();
    //dm.getVTText().open();
    startFrame.getStartFrame().getStatusBar().next("Kreiranje dokumenata za odabrane partnere");
    int brdok = 0;
    String cOpis = jlrCORG.getText()+
                "RAC"+val.findYear(DummySet.getTimestamp("DATUM"));
    try {
     brdok = val.findSeqInteger(cOpis,false,true).intValue();
//     brdok = val.findSeqInteger(cOpis,false).intValue();
    } catch (Exception ex) {
      val.unlockCurrentSeq(true);
      ex.printStackTrace();
      greska = "Greška u kreiranju više raèuna !";
      startFrame.getStartFrame().getStatusBar().finnishTask();
      javax.swing.JOptionPane.showConfirmDialog(this,greska,"Greška !",
        javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.DEFAULT_OPTION);
      return false;
    }

    do{
      try {
        addAll(brdok);
        brdok++;
      } catch (Exception ex) {
        ex.printStackTrace();
        greska = "Greška u kreiranju više raèuna !";
        startFrame.getStartFrame().getStatusBar().finnishTask();
        javax.swing.JOptionPane.showConfirmDialog(this,greska,"Greška !",
          javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.DEFAULT_OPTION);
       return false;
      }
    } while (prenosPartner.next());
//    val.setSeqFilter(cOpis);
    dm.getSeq().setDouble("BROJ", brdok-1);
    startFrame.getStartFrame().getStatusBar().finnishTask();
    return true;
  }

  public boolean transakcija(){
    return (new raLocalTransaction(){
      public boolean transaction() throws Exception {
        try {
          saveChanges(zagRac);
          saveChanges(stavRac);
          saveChanges(vtText);
          saveChanges(dm.getSeq());
          return true;
        }
        catch (Exception ex) {
          ex.printStackTrace();
          return false;
        } finally {
          val.unlockCurrentSeq(false);
          startFrame.getStartFrame().getStatusBar().finnishTask();
        }
        }}.execTransaction());

  }

  boolean isPartneriSelected = false;
  boolean isUgovoriSelected = false;

  public void presOK(){
    isPartneriSelected = jbpartneri.isSelected();
    if (Validacija()) {
      rcc.EnabDisabAll(this,false);
      Thread thr = new Thread() {
        public void run() {
          boolean retValue = true;
          zagRac.refresh();
          stavRac.refresh();
          vtText.refresh();
          if (isPartneriSelected) {
            retValue = addHeadersPartner();
            System.out.println("retValue "+retValue);
          }
          else {
            retValue = addHeaders();
          }
          if (retValue) {
            startFrame.getStartFrame().getStatusBar().startTask(10,"Snimanje dokumenata u tijeku ...!");
            if (transakcija()) {
              dm.getSynchronizer().markAsDirty("vttext");
            startFrame.getStartFrame().getStatusBar().finnishTask();
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                Greska();
                if (kveshcn("Želite li ispis iskreiranih raèuna ?") == javax.swing.JOptionPane.YES_OPTION){
                  ispis();
                }

              }
            });
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                resetInitMeth();
              }
            });
            }
          }
          }};
      thr.start();

    }
//    else {
//      javax.swing.JOptionPane.showConfirmDialog(this,greska,"Greška !",
//          javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.DEFAULT_OPTION);
//    }
  }

  public void resetInitMeth(){
System.out.println("resetInitMeth()");
    rcc.EnabDisabAll(this,true);
    clearAll(false);
  }
  
  public void presCancel(){
    if (escPresBefore) {
//    this.dispose();
    this.hide();
    }
    else {
      escPresBefore = true;
      clearAll(true);
    }
  }

  public void initMnemonicTable(){

    mnemonicTable[0][0] = "<mje>";
    mnemonicTable[0][1] =mjesec.getText();
    mnemonicTable[1][0] = "<god>";
    mnemonicTable[1][1] =godina.getText();
//    mnemonicTable[2][0] = "<broj>";
//    mnemonicTable[2][1] =godina.getText();
//    mnemonicTable[3][0] = "<pnb>";
//    mnemonicTable[3][1] =godina.getText();
//    mnemonicTable[2][0] = "<mje>";    trebalo bi enter u novi red
//    mnemonicTable[2][1] = "\n";    trebalo bi enter u novi red
/*
    for (int i = 0; i<mnemonicTable[0].length;i++) {
      for(int j = 0; j<mnemonicTable.length;j++) {

        System.out.println("mnemonicTable["+i+"]["+j+"] = "+mnemonicTable[i][j]);
      }
    }
*/
  }

  public String changeMnemonics(String text) {

    initMnemonicTable();
    StringBuffer buffy = new StringBuffer(text);
    int offset;
    for (int i =0 ;i< mnemonicTable.length;i++) {
      offset = 0;
      while ((offset = text.indexOf(mnemonicTable[i][0])) != -1) {
        buffy.replace(offset, offset + mnemonicTable[i][0].length(),mnemonicTable[i][1] );
        text = buffy.toString();
      }
    }
    return buffy.toString();
  }

  public void clearAll(boolean all){
    if (all) {
        jlrCART.setText("");
        jlrCART.emptyTextFields();
        DummySet.reset();
        DummySet.setTimestamp("GODINA",val.getToday());
        DummySet.setTimestamp("MJESEC",val.getToday());
        DummySet.setTimestamp("DATUM",val.getToday());
        DummySet.setString("OPIS","");
        jlrCORG.setText("");
        jlrCORG.emptyTextFields();
        jraNapomena.setText("");
        rsP.ocisti();
        jlrCNAP.setText("");
        jlrCNAP.emptyTextFields();
        jlrCORG.requestFocus();
    }
  }

  public void Greska(){
    javax.swing.JOptionPane.showConfirmDialog(this,"Generiranje ra\u010Duna uspješno obavljeno !","Poruka",
          javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.DEFAULT_OPTION);
  }

  public int kveshcn(String str){
    return javax.swing.JOptionPane.showConfirmDialog(this,str,"Upit",
          javax.swing.JOptionPane.YES_NO_OPTION,javax.swing.JOptionPane.DEFAULT_OPTION);
  }

  public void ispis(){
    rr.go();
  }

  public void commonTask4prepareQuery(QueryDataSet zaglav,QueryDataSet stavke ){
      
    com.borland.dx.dataset.ReadRow.copyTo(zaglav.getColumnNames(zaglav.getColumnCount()),
            zaglav,zaglav.getColumnNames(zaglav.getColumnCount()),allForIspis);
    com.borland.dx.dataset.ReadRow.copyTo(stavke.getColumnNames(stavke.getColumnCount()),
            stavke,stavke.getColumnNames(stavke.getColumnCount()),allForIspis);
    hr.restart.util.lookupData.getlookupData().raLocate(dm.getVTText(),new String[] {"CKEY"},
            new String[] {raControlDocs.getKey(stavke)});
    com.borland.dx.dataset.ReadRow.copyTo(dm.getVTText().getColumnNames(dm.getVTText().getColumnCount()),
            dm.getVTText(),dm.getVTText().getColumnNames(dm.getVTText().getColumnCount()),allForIspis);  	
  	
  }
  
  public void prepareQuery(String dodatak) {
      
    dm.getVTText().open();
    allForIspis.emptyAllRows();
    
    String sqlzaglav =
    	"select * from doki WHERE vrdok ='"+dm.getZagRac().getString("VRDOK")+"' "+
        "and cskl='"+dm.getZagRac().getString("CSKL")+"' "+
        "and god='"+dm.getZagRac().getString("GOD")+"' "+
        dodatak;
    String sqlstavke =
    	"select * from stdoki WHERE vrdok ='"+dm.getZagRac().getString("VRDOK")+"' "+
        "and cskl='"+dm.getZagRac().getString("CSKL")+"' "+
        "and god='"+dm.getZagRac().getString("GOD")+"' "+
        dodatak;
    QueryDataSet zaglav = hr.restart.util.Util.getNewQueryDataSet
      (sqlzaglav,true);
    QueryDataSet stavke;    
    for (zaglav.first();zaglav.inBounds();zaglav.next()){
    	sqlstavke =
        	"select * from stdoki WHERE vrdok ='"+dm.getZagRac().getString("VRDOK")+"' "+
            "and cskl='"+dm.getZagRac().getString("CSKL")+"' "+
            "and god='"+dm.getZagRac().getString("GOD")+"' "+
            "and brdok="+zaglav.getInt("BRDOK");
    	stavke = hr.restart.util.Util.getNewQueryDataSet
	      (sqlstavke,true);
    	for (stavke.first();stavke.inBounds() ;stavke.next()){
    		allForIspis.insertRow(true);
    		commonTask4prepareQuery(zaglav,stavke);  		
    		  System.out.println(zaglav.getInt("BRDOK"));
    	}
    }
    ST.prn(allForIspis);
  }
  
  public void prepareQuery() {

System.err.println("NE SMIJEM BITI TU");  	
  	
    dm.getVTText().open();
    allForIspis.emptyAllRows();
    allForIspis.insertRow(true);
    QueryDataSet zaglav = hr.restart.util.Util.getNewQueryDataSet
      ("select * from doki WHERE vrdok ='"+dm.getZagRac().getString("VRDOK")+"' "+
            "and cskl='"+dm.getZagRac().getString("CSKL")+"' "+
            "and god='"+dm.getZagRac().getString("GOD")+"' "+
            "and brdok="+dm.getZagRac().getInt("BRDOK"),true);
    QueryDataSet stavke = hr.restart.util.Util.getNewQueryDataSet
      ("select * from stdoki WHERE vrdok ='"+dm.getZagRac().getString("VRDOK")+"' "+
            "and cskl='"+dm.getZagRac().getString("CSKL")+"' "+
            "and god='"+dm.getZagRac().getString("GOD")+"' "+
            "and brdok="+dm.getZagRac().getInt("BRDOK"),true);
    
    commonTask4prepareQuery(zaglav,stavke);


  }

  public QueryDataSet getQuery(){
  	allForIspis.first();
  	ST.prn(allForIspis);
  	allForIspis.first();  	
    return allForIspis;
  }

  private void ArajToHashMap(HashMap hm,String [] arah){
    for (int i = 0;i<arah.length;i++) {
      hm.put(arah[i],"");
    }
  }


  void jbpartneri_itemStateChanged(ItemEvent e) {
    if (jbpartneri.isSelected()) {
      rsP.pack();
      startFrame.getStartFrame().centerFrame(rsP,0,"Odabir partnera za više ra\u010Duna");
      startFrame.getStartFrame().showFrame(rsP);
    }
  }
  public void addAll(int brdok) throws Exception{


      /// zaglavlje
      zagRac.insertRow(true);
      zagRac.setString("CUSER",hr.restart.sisfun.raUser.getInstance().getUser());
      zagRac.setString("CSKL",jlrCORG.getText());
      zagRac.setString("VRDOK","RAC");
      zagRac.setString("GOD",val.findYear(DummySet.getTimestamp("DATUM")));
      zagRac.setInt("BRDOK",brdok);
      zagRac.setString("CNACPL",hr.restart.sisfun.frmParam.getParam("robno","defNacpl"));
      zagRac.setString("PARAM","_A_");
      if (isUgovoriSelected) {
        zagRac.setInt("CPAR",Ugovors.getInt("CPAR"));
        if (Ugovors.getInt("PJ") !=0) {
          zagRac.setInt("PJ",Ugovors.getInt("PJ"));
        }
      }
      else {
        zagRac.setInt("CPAR",prenosPartner.getInt("CPAR"));
      }
      zagRac.setTimestamp("DATDOK",DummySet.getTimestamp("DATUM"));

      if (isUgovoriSelected) {
        zagRac.setString("CUG",Ugovors.getString("CUGOVOR"));
        zagRac.setTimestamp("DATUG",Ugovors.getTimestamp("DATUGOVOR"));
      }

      zagRac.setShort("DDOSP",DummySet.getShort("DANIDOSP"));
      zagRac.setTimestamp("DVO",DummySet.getTimestamp("DVO"));
      zagRac.setTimestamp("DATDOSP",DummySet.getTimestamp("DATDOSP"));

      zagRac.setString("OPIS",changeMnemonics(jraNapomena.getText()));
/*
      if (isUgovoriSelected) {
        zagRac.setString("CNAP",Ugovors.getString("CNAP"));
      }
*/
      zagRac.setString("CNAP",DummySet.getString("CNAP"));

      zagRac.setString("ZIRO",jlrZiro.getText());
      zagRac.setString("PNBZ2",raPozivNaBroj.getraPozivNaBrojClass().getPozivNaBroj(zagRac));

      /// stavke
      stavRac.insertRow(true);
      stavRac.setString("CSKL",zagRac.getString("CSKL"));
      stavRac.setString("VRDOK","RAC");
      stavRac.setString("GOD",zagRac.getString("GOD"));
      stavRac.setInt("BRDOK",zagRac.getInt("BRDOK"));
      stavRac.setShort("RBR",(short) 1);
      stavRac.setInt("RBSID", 1);
      stavRac.setInt("CART",DummyArtiklSet.getInt("CART"));
//System.out.println("DummyArtiklSet.getInt(CART) ->"+DummyArtiklSet.getInt("CART"));
      stavRac.setString("CART1",DummyArtiklSet.getString("CART1"));
      stavRac.setString("BC",DummyArtiklSet.getString("BC"));
      stavRac.setString("NAZART",DummyArtiklSet.getString("NAZART"));
      stavRac.setString("JM",DummyArtiklSet.getString("JM"));
      stavRac.setBigDecimal("KOL",new java.math.BigDecimal("1.000"));
      stavRac.setBigDecimal("UPRAB",new java.math.BigDecimal("0.00"));
      stavRac.setBigDecimal("UIRAB",new java.math.BigDecimal("0.00"));
      stavRac.setBigDecimal("UPZT",new java.math.BigDecimal("0.00"));
      stavRac.setBigDecimal("UIZT",new java.math.BigDecimal("0.00"));
      if (isUgovoriSelected) {
        stavRac.setBigDecimal("FC",Ugovors.getBigDecimal("IZNOS"));
        stavRac.setBigDecimal("INETO",Ugovors.getBigDecimal("IZNOS"));
        stavRac.setBigDecimal("FVC",Ugovors.getBigDecimal("IZNOS"));
        stavRac.setBigDecimal("IPRODBP",Ugovors.getBigDecimal("IZNOS"));
      }
      else {
        stavRac.setBigDecimal("FC",prenosPartner.getBigDecimal("IZNOS"));
        stavRac.setBigDecimal("INETO",prenosPartner.getBigDecimal("IZNOS"));
        stavRac.setBigDecimal("FVC",prenosPartner.getBigDecimal("IZNOS"));
        stavRac.setBigDecimal("IPRODBP",prenosPartner.getBigDecimal("IZNOS"));
      }
      stavRac.setBigDecimal("PPOR1",DummyArtiklSet.getBigDecimal("POR1"));
      stavRac.setBigDecimal("PPOR2",DummyArtiklSet.getBigDecimal("POR2"));
      stavRac.setBigDecimal("PPOR3",DummyArtiklSet.getBigDecimal("POR3"));

      stavRac.setBigDecimal("POR1",stavRac.getBigDecimal("IPRODBP").multiply(
                                    DummyArtiklSet.getBigDecimal("POR1")).divide(
                                    new BigDecimal("100.00"),4,BigDecimal.ROUND_HALF_UP));

      if (DummyArtiklSet.getString("PORNAPOR2").equals("D")) {
        stavRac.setBigDecimal("POR2",(stavRac.getBigDecimal("IPRODBP").add(
                                             stavRac.getBigDecimal("POR1"))).multiply(
                                      DummyArtiklSet.getBigDecimal("POR2")).divide(
                                      new BigDecimal("100.00"),4,BigDecimal.ROUND_HALF_UP));
      }
      else {
        stavRac.setBigDecimal("POR2",stavRac.getBigDecimal("IPRODBP").multiply(
                                      DummyArtiklSet.getBigDecimal("POR2")).divide(
                                      new BigDecimal("100.00"),4,BigDecimal.ROUND_HALF_UP));
      }

      if (DummyArtiklSet.getString("PORNAPOR3").equals("D")) {
        stavRac.setBigDecimal("POR3",(stavRac.getBigDecimal("IPRODBP").add(
                                             stavRac.getBigDecimal("POR1")).add(
                                             stavRac.getBigDecimal("POR2"))).multiply(
                                      DummyArtiklSet.getBigDecimal("POR2")).divide(
                                      new BigDecimal("100.00"),4,BigDecimal.ROUND_HALF_UP));
      }
      else {
        stavRac.setBigDecimal("POR3",stavRac.getBigDecimal("IPRODBP").multiply(
                                      DummyArtiklSet.getBigDecimal("POR3")).divide(
                                      new BigDecimal("100.00"),4,BigDecimal.ROUND_HALF_UP));
      }

      stavRac.setBigDecimal("UPPOR",DummyArtiklSet.getBigDecimal("UKUPOR"));
      stavRac.setBigDecimal("UIPOR",stavRac.getBigDecimal("POR1").add(
                                   stavRac.getBigDecimal("POR2")).add(
                                   stavRac.getBigDecimal("POR3")));
      stavRac.setBigDecimal("FMC",stavRac.getBigDecimal("IPRODBP").add(
                                   stavRac.getBigDecimal("UIPOR")));
      stavRac.setBigDecimal("IPRODSP",stavRac.getBigDecimal("FMC"));
        zagRac.setBigDecimal("UIRAC",stavRac.getBigDecimal("FMC"));

      vtText.insertRow(true);
      vtText.setString("CKEY",raControlDocs.getKey(stavRac));
      if (isUgovoriSelected) {
        vtText.setString("TEXTFAK",changeMnemonics(Ugovors.getString("TEXTFAK")));
      }
      else {
        vtText.setString("TEXTFAK",changeMnemonics(prenosPartner.getString("TEXTSTAV")));
      }
      allForIspis.insertRow(true);


      com.borland.dx.dataset.ReadRow.copyTo(zagRac.getColumnNames(zagRac.getColumnCount()),
                                      zagRac,
                                      zagRac.getColumnNames(zagRac.getColumnCount()),
                                      allForIspis);
      com.borland.dx.dataset.ReadRow.copyTo(stavRac.getColumnNames(stavRac.getColumnCount()),
                                      stavRac,
                                      stavRac.getColumnNames(stavRac.getColumnCount()),
                                      allForIspis);
      com.borland.dx.dataset.ReadRow.copyTo(vtText.getColumnNames(vtText.getColumnCount()),
                                      vtText,
                                      vtText.getColumnNames(vtText.getColumnCount()),
                                      allForIspis);

 }

  void datum_focusLost(FocusEvent e) {
    DummySet.setTimestamp("DVO",DummySet.getTimestamp("DATUM"));
    dvo_focusLost(null);
  }

  void dvo_focusLost(FocusEvent e) {
    java.util.Date Datum = new java.util.Date(DummySet.getTimestamp("DVO").getTime());
    DummySet.setTimestamp("DATDOSP",new java.sql.Timestamp(
    raDateUtil.getraDateUtil().addDate(Datum, (int) DummySet.getShort("DANIDOSP")).getTime()));
  }

  void datosp_focusLost(FocusEvent e) {
    DummySet.setShort("DANIDOSP",(short)
      raDateUtil.getraDateUtil().DateDifference(new java.util.Date(
      DummySet.getTimestamp("DVO").getTime()),
      new java.util.Date(DummySet.getTimestamp("DATDOSP").getTime())));
  }

  void danidosp_focusLost(FocusEvent e) {
    dvo_focusLost(null);
  }
/*
    String defNamjena = hr.restart.sisfun.frmParam.getParam("robno","defNamjena");
        getMasterSet().setString("CNAMJ",defNamjena);
   String defNacotp = hr.restart.sisfun.frmParam.getParam("robno","defNacotp");
        getMasterSet().setString("CNAC",defNacotp);
  String defNacpl = hr.restart.sisfun.frmParam.getParam("robno","defNacpl");
        getMasterSet().setString("CNACPL",defNacpl);
*/
}