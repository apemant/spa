/****license*****************************************************************
**   file: frmPartneri.java
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
package hr.restart.zapod;

import hr.restart.baza.Condition;
import hr.restart.baza.Kupci;
import hr.restart.baza.Telehist;
import hr.restart.baza.Telemark;
import hr.restart.baza.VTCartPart;
import hr.restart.baza.dM;
import hr.restart.robno.raWebSync;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraLabel;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raTransaction;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:        Robno poslovanje
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      REST-ART
 * @author REST-ART development team
 * @version 1.0
 */

public class frmPartneri extends raMatPodaci {
  static frmPartneri fpartneri;
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle(hr.restart.zapod.frmZapod.RESBUNDLENAME);
  dM dm;
  raCommonClass rcc = new raCommonClass();

  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  char mymode='N';
  BorderLayout borderLayout1 = new BorderLayout();

  JLabel jlTELFAX = new JLabel();
  JraTextField jtfTEL = new JraTextField();
  JraTextField jtfADR = new JraTextField();
  JraTextField jtfCDJEL = new JraTextField();
  JPanel jpOsnovniPodaci = new JPanel();
  JLabel jlCPAR = new JLabel();
  JraTextField jtfEMADR = new JraTextField();
  JLabel jlMB = new JLabel();
  JLabel jlKO = new JLabel();
  JraTextField jtfNAZPAR = new JraTextField();
  JLabel jlZR = new JLabel();
  JLabel jlNAZPAR = new JLabel();
  JLabel jlCDJEL = new JLabel();
  JlrNavField jlrPBR = new JlrNavField();
  JlrNavField jlrMJ = new JlrNavField();
  JlrNavField jlrCMJ = new JlrNavField();
  JlrNavField jlrCZUP = new JlrNavField();
  JraButton jbGetMj = new JraButton();
  JraCheckBox jcbAKTIV = new JraCheckBox();
  JLabel jlEMADR = new JLabel();
  JLabel jlTEL = new JLabel();
  JLabel jlADR = new JLabel();
  JraTextField jtfTELFAX = new JraTextField();
  JraTextField jtfMB = new JraTextField();
  JraTextField jtfKO = new JraTextField();
  JraTextField jtfCPAR = new JraTextField();
  XYLayout xYLayout1 = new XYLayout();
  JraTextField jtfZR = new JraTextField();
  JLabel jlMJ = new JLabel();
  JraLabel jdblNAZPAR2 = new JraLabel();
  JLabel jlNAZPAR2 = new JLabel();
  XYLayout xYLayout2 = new XYLayout();

  BorderLayout borderLayout5 = new BorderLayout();
  dlgTotalPromet dtp = new dlgTotalPromet("CPAR");

  raNavAction rnvPJ = new raNavAction("Adrese - poslovne jedinice",raImages.IMGHISTORY,KeyEvent.VK_F6) {
    public void actionPerformed(ActionEvent e) {
      rnvPJ_actionPerformed(e);
    }
  };

  raNavAction rnvZiro = new raNavAction("Žiro ra\u010Duni",raImages.IMGALIGNJUSTIFY,KeyEvent.VK_F8) {
    public void actionPerformed(ActionEvent e) {
      rnvZiro_actionPerformed(e);
    }
  };

  raNavAction rnvKosobe = new raNavAction("Kontakt osobe",raImages.IMGALIGNCENTER,KeyEvent.VK_F9) {
    public void actionPerformed(ActionEvent e) {
      rnvKosobe_actionPerformed(e);
    }
  };
  
  raNavAction rnvSifArt = new raNavAction("Šifre Artikala",raImages.IMGOPEN,KeyEvent.VK_F12){
    public void actionPerformed(ActionEvent e) {
      rnvSifArt_actionPerformed(e);
    }
    
  };
  
  JraScrollPane vp = new JraScrollPane();
  
  JEditorPane msg = new JEditorPane() {
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }
  };

//* MAKNUTO ZBOG GRESKE U PROJEKTU BAZE partneri.agent int(4,0) == agenti.Cagent int(6,0) ????

  JLabel jlpartner = new JLabel();
  JLabel jlPBR = new JLabel();
  JLabel jlULICA = new JLabel();
  JLabel jlBrojevi = new JLabel();
  JLabel jlkontakt = new JLabel();
  JLabel jlULOGA = new JLabel();
  raComboBox raCUloga = new raComboBox();
  JraTextField jtfPRAB = new JraTextField();
  JraTextField jtfLIMKRED = new JraTextField();
  JLabel jlUGOVOR = new JLabel();
  JLabel jlLIMKRED = new JLabel();
  JlrNavField jlrCGRPAR = new JlrNavField();
  JlrNavField jlrNAZGRPAR = new JlrNavField();
  JLabel jlPRAB = new JLabel();
  JraButton jbGetAgent = new JraButton();
  JraButton jbGetGrupa = new JraButton();
  JraTextField jtfUGOVOR = new JraTextField();
  JLabel jlCGRPAR = new JLabel();
  JlrNavField jlrNAZAGENT = new JlrNavField();
  JLabel jlCAGENT = new JLabel();
  JlrNavField jlrCAGENT = new JlrNavField();
  raComboBox raCDI = new raComboBox();
  JLabel jlULOGA1 = new JLabel();
  JraTextField jtfDOSP = new JraTextField();
  JLabel jlDOSP = new JLabel();
  JLabel jluvjeti = new JLabel();
  JLabel jlkarakt = new JLabel();
  JLabel jlagent = new JLabel();
  JLabel jlnazagent = new JLabel();
  raComboBox raCSTATUS = new raComboBox();
  JLabel jlSTATUS = new JLabel();
  
  JLabel jlOIB = new JLabel();
  JraTextField jtfOIB = new JraTextField();
  JraTextField jtfGLN = new JraTextField();
  JraCheckBox jcbR2 = new JraCheckBox();
  
  JlrNavField jlrTELIME = new JlrNavField();
  JLabel jlCTEL = new JLabel();
  JlrNavField jlrCTEL = new JlrNavField();
  JraButton jbGetTel = new JraButton();
//*/

  public frmPartneri() {
    try {
      jbInit();
      fpartneri = this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmPartneri getFrmPartneri() {
    if (fpartneri==null) {
      fpartneri = new frmPartneri();
    }
    return fpartneri;
  }

  private void jbInit() throws Exception {
    dm=dM.getDataModule();
    this.setRaDetailPanel(jpOsnovniPodaci);
    this.setRaQueryDataSet(dm.getAllPartneri());
    this.setVisibleCols(new int[] {0,1,2});
//TEMP!!!!!!
//    this.setkum_tak(true);
//    this.setstozbrojiti(new String[] {"LIMKRED"});
//    this.setnaslovi(new String[] {"Limit kreditiranja"});
//    this.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
//ENDTEMP!!!!!!
    jlTELFAX.setText(res.getString("jlTELFAX_text"));
    jlMJ.setText(res.getString("jlPtMJ_text"));
    jlCPAR.setText(res.getString("jlCPAR_text"));
    jlMB.setText(res.getString("jlMB_text"));
    jlKO.setText(res.getString("jlKO_text"));
    jlZR.setText(res.getString("jlZR_text"));
    jlNAZPAR.setText(res.getString("jlNAZPAR_text"));
    jlCDJEL.setText(res.getString("jlCDJEL_text"));
    jcbAKTIV.setText(res.getString("jcbAKTIV_text"));
    jlEMADR.setText(res.getString("jlEMADR_text"));
    jlTEL.setText(res.getString("jlTEL_text"));
    jlADR.setText(res.getString("jlADR_text"));
    jlNAZPAR2.setText(res.getString("jlNAZPAR_text"));
    jtfTEL.setColumnName("TEL");
    jtfTEL.setDataSet(getRaQueryDataSet());
    jtfADR.setColumnName("ADR");
    jtfADR.setDataSet(getRaQueryDataSet());
    jtfCDJEL.setColumnName("CDJEL");
    jtfCDJEL.setDataSet(getRaQueryDataSet());
    jtfEMADR.setColumnName("EMADR");
    jtfEMADR.setDataSet(getRaQueryDataSet());
    jtfNAZPAR.setColumnName("NAZPAR");
    jtfNAZPAR.setDataSet(getRaQueryDataSet());
    jlrPBR.setSearchMode(-1);
    jlrPBR.setColumnName("PBR");
    jlrPBR.setDataSet(getRaQueryDataSet());
    jlrPBR.setColNames(new String[] {"CMJESTA", "NAZMJESTA", "CZUP"});
    jlrPBR.setTextFields(new javax.swing.text.JTextComponent[] {jlrCMJ, jlrMJ, jlrCZUP});
    jlrPBR.setVisCols(new int[] {0, 1, 2});
    jlrPBR.setRaDataSet(dm.getMjesta());
    jlrPBR.setNavButton(jbGetMj);
    jlrPBR.setFocusLostOnShow(false);
    jlrPBR.setAfterLookUpOnClear(false);
    jlrPBR.setSearchMode(1);

    jlrMJ.setSearchMode(-1);
    jlrMJ.setColumnName("MJ");
    jlrMJ.setNavProperties(jlrPBR);
    jlrMJ.setNavColumnName("NAZMJESTA");
    jlrMJ.setDataSet(getRaQueryDataSet());
    jlrMJ.setFocusLostOnShow(false);
    jlrMJ.setAfterLookUpOnClear(false);
    jlrMJ.setSearchMode(1);

    jlrCMJ.setSearchMode(-1);
    jlrCMJ.setColumnName("CMJESTA");
    jlrCMJ.setNavProperties(jlrPBR);
    jlrCMJ.setDataSet(getRaQueryDataSet());
    jlrCMJ.setVisible(false);
    jlrCMJ.setEnabled(false);
    jlrCMJ.setFocusLostOnShow(false);
    jlrCMJ.setAfterLookUpOnClear(false);

    jlrCZUP.setSearchMode(-1);
    jlrCZUP.setColumnName("CZUP");
    jlrCZUP.setNavProperties(jlrPBR);
    jlrCZUP.setDataSet(getRaQueryDataSet());
    jlrCZUP.setVisible(false);
    jlrCZUP.setEnabled(false);
    jlrCZUP.setFocusLostOnShow(false);
    jlrCZUP.setAfterLookUpOnClear(false);
    jtfMB.setColumnName("MB");
    jtfMB.setDataSet(getRaQueryDataSet());
    jtfKO.setColumnName("KO");
    jtfKO.setDataSet(getRaQueryDataSet());
    jtfCPAR.setColumnName("CPAR");
    jtfCPAR.setDataSet(getRaQueryDataSet());
    jtfZR.setColumnName("ZR");
    jtfZR.setDataSet(getRaQueryDataSet());
    jdblNAZPAR2.setFont(jdblNAZPAR2.getFont().deriveFont(1));
    jdblNAZPAR2.setColumnName("NAZPAR");
    jdblNAZPAR2.setDataSet(getRaQueryDataSet());
    jtfTELFAX.setColumnName("TELFAX");
    jtfTELFAX.setDataSet(getRaQueryDataSet());
    jcbAKTIV.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAKTIV.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbAKTIV.setColumnName("AKTIV");
    jcbAKTIV.setDataSet(getRaQueryDataSet());
    jcbAKTIV.setSelectedDataValue("D");
    jcbAKTIV.setUnselectedDataValue("N");
    jpOsnovniPodaci.setLayout(xYLayout1);
//    jBpj.setPreferredSize(new Dimension(100, 27));
//    jBpj.setText("Adrese - poslovne jedinice");
//    jBpj.setToolTipText(jBpj.getText());
//    jBpj.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jBpj_actionPerformed(e);
//      }
//    });
    jlpartner.setText("Partner");
    jlPBR.setText("PT Broj");
    jlULICA.setText("Ulica i broj");
    jlBrojevi.setText("Brojevi");
    jlkontakt.setText("Kontakt");
    jlULOGA.setText(res.getString("jlULOGA_text"));
    raCUloga.setRaDataSet(getRaQueryDataSet());
    raCUloga.setRaColumn("ULOGA");
    raCUloga.setRaItems(new String[][] {
      { "Oboje", "O"},
      { "Kupac","K" },
      { "Dobavlja\u010D", "D" }
    });

    jtfPRAB.setColumnName("PRAB");
    jtfPRAB.setDataSet(getRaQueryDataSet());
    jtfLIMKRED.setColumnName("LIMKRED");
    jtfLIMKRED.setDataSet(getRaQueryDataSet());
    jlUGOVOR.setText(res.getString("jlUGOVOR_text"));
    jlLIMKRED.setText(res.getString("jlLIMKRED_text"));
    jlrCGRPAR.setColumnName("CGRPAR");
    jlrCGRPAR.setNavColumnName("CGPART");
    jlrCGRPAR.setDataSet(getRaQueryDataSet());
    jlrCGRPAR.setColNames(new String[] {"NAZIV"});
    jlrCGRPAR.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZGRPAR});
    jlrCGRPAR.setVisCols(new int[] {0, 1});
    jlrCGRPAR.setRaDataSet(dm.getGruppart());
    jlrCGRPAR.setSearchMode(0);
    jlrCGRPAR.setNavButton(jbGetGrupa);
    jlrNAZGRPAR.setColumnName("NAZIV");
    jlrNAZGRPAR.setNavProperties(jlrCGRPAR);
    jlrNAZGRPAR.setSearchMode(1);
    jlPRAB.setText(res.getString("jlPRAB_text"));
    jtfUGOVOR.setColumnName("UGOVOR");
    jtfUGOVOR.setDataSet(getRaQueryDataSet());
    jlCGRPAR.setText(res.getString("jlCGRPAR_text"));
    jlrNAZAGENT.setSearchMode(1);
    jlrNAZAGENT.setColumnName("NAZAGENT");
    jlrNAZAGENT.setNavProperties(jlrCAGENT);
    jlCAGENT.setText(res.getString("jlAGENT_text"));
    jlrCAGENT.setColNames(new String[] {"NAZAGENT"});
    jlrCAGENT.setRaDataSet(dm.getAgenti());
    jlrCAGENT.setDataSet(getRaQueryDataSet());
    jlrCAGENT.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZAGENT});
    jlrCAGENT.setColumnName("CAGENT");
    jlrCAGENT.setSearchMode(0);
    jlrCAGENT.setVisCols(new int[] {0,1});
    jlrCAGENT.setNavButton(jbGetAgent);
    raCDI.setRaDataSet(getRaQueryDataSet());
    raCDI.setRaColumn("DI");
    raCDI.setRaItems(new String[][] {
      { "Tuzemni", "D"},
      { "Inozemni","I" },
      { "Virutalni","V" }
    });
    jlULOGA1.setText(res.getString("jlDI_text"));
    jtfDOSP.setDataSet(getRaQueryDataSet());
    jtfDOSP.setColumnName("DOSP");
    jlDOSP.setText(res.getString("jlDOSP_text"));
    jluvjeti.setText("Uvjeti");
    jlkarakt.setText("Karakteristike");
    jlagent.setText("Agent");
    jlnazagent.setText("Ime i prezime");
//    jpDodatniPodaci.add(jlNAZPAR2, new XYConstraints(15, 20, -1, -1));
//    jpDodatniPodaci.add(jdblNAZPAR2, new XYConstraints(150, 20, 200, -1));
//  MAKNUTO ZBOG GRESKE U PROJEKTU BAZE partneri.agent int(4,0) == agenti.Cagent int(6,0) ????
    xYLayout1.setWidth(600);
    xYLayout1.setHeight(535);
    raCSTATUS.setRaColumn("STATUS");
    raCSTATUS.setRaDataSet(getRaQueryDataSet());
    raCSTATUS.setRaItems(new String[][] {
      { "A - Slobodno fakturiranje", "A"},
      { "B - Fakturiranje uz provjeru","B" },
      { "C - Zabranjeno fakturiranje","C" }
    });
    
    
    jlCTEL.setText(frmParam.getParam("zapod", "teleserv", "Telemarketer", "Caption telemarketera na formi partnera"));
    jlrCTEL.setColNames(new String[] {"IME"});
    jlrCTEL.setRaDataSet(Telemark.getDataModule().getQueryDataSet());
    jlrCTEL.setDataSet(getRaQueryDataSet());
    jlrCTEL.setTextFields(new javax.swing.text.JTextComponent[] {jlrTELIME});
    jlrCTEL.setColumnName("CTEL");
    jlrCTEL.setSearchMode(0);
    jlrCTEL.setVisCols(new int[] {0,1});
    jlrCTEL.setNavButton(jbGetTel);
    
    jlrTELIME.setSearchMode(1);
    jlrTELIME.setColumnName("IME");
    jlrTELIME.setNavProperties(jlrCTEL);

    jlSTATUS.setText("Status");
    
    jcbR2.setText("Partner je obrtnik R2");
    jcbR2.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbR2.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbR2.setColumnName("OBRT");
    jcbR2.setDataSet(getRaQueryDataSet());
    jcbR2.setSelectedDataValue("D");
    jcbR2.setUnselectedDataValue("N");
    
    jtfOIB.setColumnName("OIB");
    jtfOIB.setDataSet(getRaQueryDataSet());
    jtfGLN.setColumnName("GLN");
    jtfGLN.setDataSet(getRaQueryDataSet());
    jlOIB.setText("OIB / GLN");
    
    jpOsnovniPodaci.add(jtfCPAR,   new XYConstraints(150, 30, 100, -1));
    jpOsnovniPodaci.add(jlCPAR,  new XYConstraints(150, 12, -1, -1));
    jpOsnovniPodaci.add(jlpartner,  new XYConstraints(15, 30, -1, -1));
    jpOsnovniPodaci.add(jlrNAZAGENT,  new XYConstraints(255, 335, 310, -1));
    jpOsnovniPodaci.add(jlADR,  new XYConstraints(15, 95, -1, -1));
    jpOsnovniPodaci.add(jtfADR,   new XYConstraints(150, 95, 205, -1));
    jpOsnovniPodaci.add(jtfMB,  new XYConstraints(150, 135, 100, -1));
    jpOsnovniPodaci.add(jtfTEL,  new XYConstraints(150, 175, 260, -1));
    jpOsnovniPodaci.add(jlrMJ,   new XYConstraints(425, 95, 140, -1));
    jpOsnovniPodaci.add(jlrPBR,   new XYConstraints(360, 95, 60, -1));
    jpOsnovniPodaci.add(jbGetMj,   new XYConstraints(570, 95, 21, 21));
    jpOsnovniPodaci.add(jlrCMJ,   new XYConstraints(567, 95, 1, -1));
    jpOsnovniPodaci.add(jlrCZUP,   new XYConstraints(568, 95, 1, -1));
    jpOsnovniPodaci.add(jlMJ,   new XYConstraints(425, 77, -1, -1));
    jpOsnovniPodaci.add(jlPBR,   new XYConstraints(360, 77, -1, -1));
    jpOsnovniPodaci.add(jlULICA,  new XYConstraints(150, 77, -1, -1));
    jpOsnovniPodaci.add(jlMB,   new XYConstraints(150, 118, -1, -1));
    jpOsnovniPodaci.add(jlCDJEL,  new XYConstraints(255, 118, -1, -1));
    jpOsnovniPodaci.add(jtfCDJEL,  new XYConstraints(255, 135, 100, -1));
    jpOsnovniPodaci.add(jtfZR,  new XYConstraints(360, 135, 205, -1));
    jpOsnovniPodaci.add(jlZR,  new XYConstraints(360, 118, -1, -1));
    jpOsnovniPodaci.add(jlBrojevi,  new XYConstraints(15, 135, -1, -1));
    jpOsnovniPodaci.add(jlkontakt,  new XYConstraints(15, 175, -1, -1));
    jpOsnovniPodaci.add(jlTEL,  new XYConstraints(150, 158, -1, -1));
    jpOsnovniPodaci.add(jlTELFAX,  new XYConstraints(415, 158, -1, -1));
    jpOsnovniPodaci.add(jtfTELFAX,  new XYConstraints(415, 175, 150, -1));
    jpOsnovniPodaci.add(raCDI,  new XYConstraints(150, 255, 100, -1));
    jpOsnovniPodaci.add(jlrCAGENT,  new XYConstraints(150, 335, 100, -1));
    jpOsnovniPodaci.add(jlrCGRPAR,  new XYConstraints(360, 255, 50, -1));
    jpOsnovniPodaci.add(jlrNAZGRPAR,  new XYConstraints(415, 255, 150, -1));
    jpOsnovniPodaci.add(jbGetGrupa,  new XYConstraints(570, 255, 21, 21));
    jpOsnovniPodaci.add(raCUloga,  new XYConstraints(255, 255, 100, -1));
    jpOsnovniPodaci.add(jlULOGA,  new XYConstraints(255, 238, -1, -1));
    jpOsnovniPodaci.add(jlCGRPAR,  new XYConstraints(360, 238, -1, -1));
    jpOsnovniPodaci.add(jlULOGA1,  new XYConstraints(150, 238, -1, -1));
    jpOsnovniPodaci.add(jtfLIMKRED,  new XYConstraints(465, 295, 100, -1));
    jpOsnovniPodaci.add(jtfPRAB,  new XYConstraints(255, 295, 100, -1));
    jpOsnovniPodaci.add(jlPRAB,  new XYConstraints(255, 278, -1, -1));
    jpOsnovniPodaci.add(jlUGOVOR,  new XYConstraints(360, 278, -1, -1));
    jpOsnovniPodaci.add(jtfUGOVOR,  new XYConstraints(360, 295, 100, -1));
    jpOsnovniPodaci.add(jlLIMKRED,  new XYConstraints(465, 278, -1, -1));
    jpOsnovniPodaci.add(jtfDOSP,  new XYConstraints(150, 295, 100, -1));
    jpOsnovniPodaci.add(jlDOSP,  new XYConstraints(150, 278, -1, -1));
    jpOsnovniPodaci.add(jbGetAgent,  new XYConstraints(570, 335, 21, 21));
    jpOsnovniPodaci.add(jluvjeti,  new XYConstraints(15, 295, -1, -1));
    jpOsnovniPodaci.add(jlkarakt,  new XYConstraints(15, 255, -1, -1));
    jpOsnovniPodaci.add(jlCAGENT,  new XYConstraints(150, 318, -1, -1));
    jpOsnovniPodaci.add(jlagent,  new XYConstraints(15, 335, -1, -1));
    jpOsnovniPodaci.add(jlnazagent,  new XYConstraints(255, 318, -1, -1));
    jpOsnovniPodaci.add(jtfKO,  new XYConstraints(150, 215, 260, -1));
    jpOsnovniPodaci.add(jlKO,  new XYConstraints(150, 198, -1, -1));
    jpOsnovniPodaci.add(jtfEMADR,  new XYConstraints(415, 215, 150, -1));
    jpOsnovniPodaci.add(jlEMADR,  new XYConstraints(415, 198, -1, -1));
    jpOsnovniPodaci.add(jtfNAZPAR,  new XYConstraints(150, 55, 415, -1));
    jpOsnovniPodaci.add(jlNAZPAR,  new XYConstraints(15, 55, -1, -1));
    jpOsnovniPodaci.add(jcbAKTIV,      new XYConstraints(465, 31, 100, 20));
    jpOsnovniPodaci.add(raCSTATUS,         new XYConstraints(255, 30, 200, -1));
    jpOsnovniPodaci.add(jlSTATUS,  new XYConstraints(255, 12, -1, -1));

    
    jpOsnovniPodaci.add(jlCTEL,  new XYConstraints(15, 360, -1, -1));
    jpOsnovniPodaci.add(jlrCTEL,  new XYConstraints(150, 360, 100, -1));
    jpOsnovniPodaci.add(jlrTELIME,  new XYConstraints(255, 360, 310, -1));
    jpOsnovniPodaci.add(jbGetTel,  new XYConstraints(570, 360, 21, 21));
    
    jpOsnovniPodaci.add(jlOIB,  new XYConstraints(15, 385, -1, -1));
    jpOsnovniPodaci.add(jtfOIB,  new XYConstraints(150, 385, 100, -1));
    jpOsnovniPodaci.add(jtfGLN,  new XYConstraints(255, 385, 120, -1));
    jpOsnovniPodaci.add(jcbR2,  new XYConstraints(390, 385, 175, 20));
    jpOsnovniPodaci.add(new JLabel("Napomene"), new XYConstraints(15, 415, -1, -1));
    jpOsnovniPodaci.add(vp,  new XYConstraints(150, 415, 415, 100));    
    
    //vp.setPreferredSize(new Dimension(500, 200));
    vp.setViewportView(msg);
    

    this.addOption(rnvPJ,3);
    this.addOption(rnvZiro,4);
    this.addOption(rnvKosobe,5);
    this.addOption(new raNavAction("Promet", raImages.IMGMOVIE, KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
        showPromet();
      }
    }, 6);
    this.addOption(rnvSifArt,7);
    raDataIntegrity.installFor(this);
  }
  
  
  public boolean doBeforeSave(char mode) {    
    try {
      if (mode != 'B')
        getRaQueryDataSet().setString("NAPS", msg.getText());
        
      checkTelemark(mode, mode == 'B' ? delCpar :
        getRaQueryDataSet().getInt("CPAR"));
      /*String mb = getRaQueryDataSet().getString("MB");
      if (mb == null || mb.length() == 0) return true;*/
/*      if (mode == 'B' || mode == 'I') {
        QueryDataSet delChg = TempCRM.getDataModule().getTempSet(Condition.equal("MB", mb));
        delChg.open();
        if (mode == 'B') delChg.deleteAllRows();
        else if (delChg.rowCount() == 0) {
          delChg.insertRow(false);
          delChg.setString("MB", mb);
        } else return true;
        raTransaction.saveChanges(delChg);
      } */
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    try {
      if (mode == 'N' && addKupac) saveKupac();
      if (mode == 'B' && delKupac) {
        raTransaction.runSQL("DELETE FROM kupci WHERE CKUPAC=" + delCkupac);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
  private int getNextTelHist() {
    DataSet ds = Util.getNewQueryDataSet("SELECT MAX(chist) FROM telehist");
    Variant v = new Variant();
    ds.getVariant(0, v);
    return v.getAsInt()  + 1;
  }
  private void checkTelemark(char mode, int cpar) throws Exception {
    System.out.println("mode "+mode+" parnter "+cpar);
    System.out.println("oldtel "+getRaQueryDataSet().getInt("CTEL"));
    System.out.println("oldtel='"+jlrCTEL.getText()+"'");
    if (mode == 'B')
      raTransaction.runSQL("DELETE FROM telehist WHERE cpar="+cpar);
    else if (mode == 'I') {
      QueryDataSet ds = Telehist.getDataModule().getTempSet(Condition.equal("CPAR", cpar));
      ds.open();
      Timestamp now = Util.getUtil().getCurrentDatabaseTime();
      if (ds.rowCount() > 0) {
        ds.setSort(new SortDescriptor(new String[] {"DATUMOD"}));
        ds.last();
        if (ds.getInt("CTEL") == getRaQueryDataSet().getInt("CTEL")) return;
        ds.setTimestamp("DATUMDO", new Timestamp(now.getTime()));
      }
      if (jlrCTEL.getText().length() > 0 && getRaQueryDataSet().getInt("CTEL") > 0) {
        ds.insertRow(false);
        ds.setInt("CPAR", cpar);
        ds.setInt("CTEL", getRaQueryDataSet().getInt("CTEL"));
        ds.setTimestamp("DATUMOD", new Timestamp(now.getTime()));
        ds.setInt("CHIST", getNextTelHist());
        ds.post();
      }
      if (ds.rowCount() > 0) raTransaction.saveChanges(ds);
    } else if (mode == 'N' && jlrCTEL.getText().length() > 0 && 
        getRaQueryDataSet().getInt("CTEL") > 0) {
      QueryDataSet ds = Telehist.getDataModule().getTempSet("1=0");
      ds.open();
      ds.insertRow(false);
      ds.setInt("CPAR", cpar);
      ds.setInt("CTEL", getRaQueryDataSet().getInt("CTEL"));
      ds.setTimestamp("DATUMOD", Util.getUtil().getCurrentDatabaseTime());
      ds.setInt("CHIST", getNextTelHist());
      ds.post();
      raTransaction.saveChanges(ds);
    }
  }
  
  private void showPromet() {
    int cpar = getRaQueryDataSet().getInt("CPAR");
    if (cpar == 0) return;
    if (getRaQueryDataSet().getString("ULOGA").equalsIgnoreCase("D")) {
      JOptionPane.showMessageDialog(this.getWindow(), "Partner je dobavljaè!",
         "Poruka", JOptionPane.WARNING_MESSAGE);
      return;
    }
    dtp.show(frmPartneri.this.getWindow(), cpar,
       "Promet kupca "+getRaQueryDataSet().getInt("CPAR")+" "+
       getRaQueryDataSet().getString("NAZPAR"));
  }

  public boolean Validacija(char mode) {
    String[] key = new String[] {"CPAR"};
    boolean forceUniqueMb = frmParam.getParam("zapod", "mbUnique", "D", 
      "Forsirati jedinstvenost matiènog broja partnera (D,N)?").equalsIgnoreCase("D");
    boolean forceUniqueZr = frmParam.getParam("zapod", "zrUnique", "D", 
      "Forsirati jedinstvenost žiro raèuna partnera (D,N)?").equalsIgnoreCase("D");
    if (mode=='N') {
      if (vl.notUnique(jtfCPAR))
        return false;
      if (forceUniqueZr && vl.notUnique(jtfZR))
        return false;
      if (forceUniqueMb && vl.notUnique(jtfMB))
        return false;
      if (vl.notUnique(jtfOIB)) return false;
    } else if (mode == 'I') {
      if (forceUniqueZr && vl.notUniqueUPD(jtfZR,key))
        return false;
      if (forceUniqueMb && vl.notUniqueUPD(jtfMB,key))
        return false;
      if (vl.notUniqueUPD(jtfOIB,key)) return false;
    }
    if (vl.isEmpty(jtfNAZPAR) || vl.isEmpty(jtfOIB)) {
      return false;
    }

    if (!getRaQueryDataSet().isNull("CMJESTA")) {
      DataRow mj = ld.raLookup(dm.getMjesta(), "CMJESTA",
         String.valueOf(getRaQueryDataSet().getInt("CMJESTA")));
      if (mj == null || !mj.getString("NAZMJESTA").equalsIgnoreCase(
          getRaQueryDataSet().getString("MJ")) || mj.getInt("PBR") !=
          getRaQueryDataSet().getInt("PBR")) {
        getRaQueryDataSet().setAssignedNull("CMJESTA");
        getRaQueryDataSet().setAssignedNull("CZUP");
      }
    }

    addKupac = false;
    if (mode != 'B') {
      if (!frmKosobe.addKontakt(getRaQueryDataSet().getInt("CPAR"),getRaQueryDataSet().getString("KO"),"-")) return false;
      if (!frmZiroPar.addZiroPar(getRaQueryDataSet().getInt("CPAR"), getRaQueryDataSet().getString("ZR"),null)) return false;
      if (mode == 'N' && !copyToKup()) return false;
    }
    return true;
  }
  
  boolean addKupac = false;
  private boolean copyToKup() {
    String ck = frmParam.getParam("zapod", "parToKup", "N",
      "Dodati/brisati slog kupca kod unosa/brisanja partnera (D,N,A)?");
    if (ck.equals("N")) return true;
    if (ck.equals("A")) {
      int response = JOptionPane.showConfirmDialog(jpOsnovniPodaci, 
          "Dodati slog kupca za ovog partnera?", "Kupac", 
          JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
      if (response == JOptionPane.NO_OPTION) return true;
      if (response != JOptionPane.YES_OPTION) return false;
    }
    return addKupac = true;
  }
    
  private void saveKupac() {
    System.out.println("snimam kupca");
    QueryDataSet kup = Kupci.getDataModule().getTempSet("1=0");
    kup.open();
    kup.insertRow(false);
    
    String naziv = getRaQueryDataSet().getString("NAZPAR");
    if (naziv.length() <= 50) {
      kup.setString("IME", naziv.toString());
      kup.setString("PREZIME", "");
    } else {
      int sp = naziv.substring(0, 50).lastIndexOf(' ');
      if (sp > 10 && sp <= 50) {
        kup.setString("IME", naziv.substring(0, sp));
        String prez = naziv.substring(sp + 1);
        kup.setString("PREZIME", prez.length() <= 50 ? 
            prez : prez.substring(0, 50));
      } else {
        kup.setString("IME", naziv.substring(0, 50));
        kup.setString("PREZIME", "");
      }
    }
    
    //if (getRaQueryDataSet().getString("MB").length() == 13)
      kup.setString("JMBG", getRaQueryDataSet().getString("MB"));
      kup.setString("OIB", getRaQueryDataSet().getString("OIB"));
    kup.setString("ADR", getRaQueryDataSet().getString("ADR"));
    kup.setInt("PBR", getRaQueryDataSet().getInt("PBR"));
    kup.setString("MJ", getRaQueryDataSet().getString("MJ"));
    String tel = getRaQueryDataSet().getString("TEL");
    if (tel.length() > 20) tel = tel.substring(0, 20);
    kup.setString("TEL", tel);
    String email = getRaQueryDataSet().getString("EMADR");
    if (email.length() > 30) email = email.substring(0, 30);
    kup.setString("EMADR", email);
    DataSet ds = Util.getNewQueryDataSet("SELECT MAX(ckupac) as ckupac FROM kupci");
    kup.setInt("CKUPAC", ds.getInt("CKUPAC") + 1);
    getRaQueryDataSet().setInt("CKUPAC", kup.getInt("CKUPAC"));
    raTransaction.saveChanges(kup);
  }

  public void AfterSave(char mode) {
    if (mode == 'N') {
//      if (ziroPar == null) ziroPar = new frmZiroPar();
//      ziroPar.setCPAR(getRaQueryDataSet().getInt("CPAR"));
//      ziroPar.initDS.open();
//      ziroPar.initDS.insertRow(true);
//      ziroPar.initDS.setInt("CPAR",getRaQueryDataSet().getInt("CPAR"));
//      ziroPar.initDS.setString("ZIRO",getRaQueryDataSet().getString("ZR"));
//      ziroPar.initDS.setString("OZNVAL",Tecajevi.getDomOZNVAL());
//      ziroPar.initDS.setString("DEV","N");
//      ziroPar.initDS.post();
//      ziroPar.initDS.saveChanges();
    }
    if (mode == 'N' || mode == 'I') {
      if (raWebSync.active && getRaQueryDataSet().getString("EMADR").length() > 0 &&
          getRaQueryDataSet().getString("EMADR").indexOf('@') > 0)
        raWebSync.updatePartner(getRaQueryDataSet());
    }

  }

  public int getNewCPAR() {
    QueryDataSet qdNewPar = Util.getNewQueryDataSet("SELECT max(CPAR) FROM partneri");
    int newCPAR = qdNewPar.getInt(0)+1;
    return newCPAR;
  }

  public void SetFokus(char mode) {
    mymode=mode;
//    jtpPartneri.setSelectedIndex(0);
    if (mode=='N') {
      getRaQueryDataSet().setInt("CPAR",getNewCPAR());
      getRaQueryDataSet().setShort("DOSP",Short.parseShort(
         hr.restart.sisfun.frmParam.getParam("zapod","dosp","7")));
      getRaQueryDataSet().setString("UGOVOR", "");
      rcc.setLabelLaF(jtfCPAR, true);
//      jtfCPAR.setEnabled(true);
//      jtfZR.setEnabled(true);
//      jtfMB.setEnabled(true);
      jtfCPAR.requestFocus();
//      rnvPJ.setEnabled(false);
    }

    else if (mode=='I') {
//      jtfZR.setEnabled(false);
//      jtfMB.setEnabled(false);
      rcc.setLabelLaF(jtfCPAR, false);
//      jtfCPAR.setEnabled(false);
      jtfNAZPAR.requestFocus();
//      rnvPJ.setEnabled(true);
    }
  }

  public void EntryPoint(char mode) {
//    jcbAKTIV.setEnabled(false);
//    raCUloga.findCombo();
    if (mode == 'N') msg.setText("");
    else msg.setText(getRaQueryDataSet().getString("NAPS"));
  }

  private boolean pjValidacija() {
    if (Validacija(mymode)) {
      return true;
    } else {
      javax.swing.JOptionPane.showMessageDialog(null,res.getString("errPj_unos"),this.getTitle(), javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }

  boolean delKupac = false;
  int delCpar, delCkupac;
  public boolean DeleteCheck() {
//    fpjpar.pjparInit(); //filteriranje PJ
    delKupac = false;
    delCpar = getRaQueryDataSet().getInt("CPAR");
    delCkupac = getRaQueryDataSet().getInt("CKUPAC");
    String ck = frmParam.getParam("zapod", "parToKup", "N",
      "Dodati/brisati slog kupca kod unosa/brisanja partnera (D,N,A)?");
    if (ck.equals("N")) return true;
    if (ck.equals("A")) {
      if (Kupci.getDataModule().getRowCount(
          Condition.equal("CKUPAC", getRaQueryDataSet())) == 0)
        return true;
      int response = JOptionPane.showConfirmDialog(jpOsnovniPodaci, 
          "Obrisati slog kupca za ovog partnera?", "Kupac", 
          JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
      if (response == JOptionPane.NO_OPTION) return true;
      if (response != JOptionPane.YES_OPTION) return false;
    }
    return delKupac = true; //za sada
  }


  public void raQueryDataSet_navigated(NavigationEvent e) {
    if (getRaQueryDataSet().rowCount() == 0) msg.setText("");
    else msg.setText(getRaQueryDataSet().getString("NAPS"));
  }


  public void AfterDelete() {
//    dm.getPjpar().deleteAllRows();
//    dm.getPjpar().saveChanges();
//    super.AfterDelete();
    
    if (raWebSync.active) raWebSync.deletePartner(delCpar);
  }



  void rnvZiro_actionPerformed(ActionEvent e) {
    QueryDataSet qds = rdZapodUtil.getrdZPUtil().getZiroParDS(getRaQueryDataSet().getInt("CPAR"));
    frmZiroPar fZP = new frmZiroPar(this, qds, this.getRaQueryDataSet().getInt("CPAR"));
    hr.restart.util.startFrame.getStartFrame().centerFrame(fZP,0,"Žiro raèuni partnera "+getRaQueryDataSet().getInt("CPAR"));
    fZP.show();
  }



  void rnvKosobe_actionPerformed(ActionEvent e) {
    QueryDataSet qds = hr.restart.baza.Kosobe.getDataModule().getTempSet("CPAR ="+getRaQueryDataSet().getInt("CPAR"));
    frmKosobe fKo = new frmKosobe(this, qds, this.getRaQueryDataSet().getInt("CPAR"));
    hr.restart.util.startFrame.getStartFrame().centerFrame(fKo,0,"Kontakt osobe partnera "+getRaQueryDataSet().getInt("CPAR"));
    fKo.show();
  }



  void rnvPJ_actionPerformed(ActionEvent e) {
    QueryDataSet qds = rdZapodUtil.getrdZPUtil().getPjDS(getRaQueryDataSet().getInt("CPAR"));
    frmPjpar fPj = new frmPjpar(this, qds, this.getRaQueryDataSet().getInt("CPAR"));
    hr.restart.util.startFrame.getStartFrame().centerFrame(fPj,0,"Poslovne jedinice partnera "+getRaQueryDataSet().getInt("CPAR"));
    fPj.show();
  }
  
  void rnvSifArt_actionPerformed(ActionEvent e) {
    QueryDataSet qds = VTCartPart.getDataModule().getTempSet("CPAR ="+getRaQueryDataSet().getInt("CPAR"));
    FrmPartneriArtikli fPa = new FrmPartneriArtikli(this, qds, this.getRaQueryDataSet().getInt("CPAR"));
    hr.restart.util.startFrame.getStartFrame().centerFrame(fPa,0,"Šifre artikala partnera " + getRaQueryDataSet().getString("NAZPAR"));
    fPa.show();
  }

}