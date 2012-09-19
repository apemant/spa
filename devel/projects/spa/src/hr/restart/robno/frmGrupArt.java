/****license*****************************************************************
**   file: frmGrupArt.java
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

import hr.restart.baza.raDataSet;
import hr.restart.swing.JrCheckBox;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raComboBox;
import hr.restart.util.raMatPodaci;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

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

public class frmGrupArt extends raMatPodaci {
  char cMode;
  short findCGRART=0;
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.baza.dM dm;
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlCGRART = new JLabel();
  JPanel jpCGRARTPRIP = new JPanel();
  JraTextField jtfNAZGRART = new JraTextField();
  JrCheckBox jcbCGRARTPRIP = new JrCheckBox();
  JraCheckBox jcbAKTIV = new JraCheckBox();
  JraTextField jtfCGRART = new JraTextField();
  JLabel jlCGRARTPRIP = new JLabel();
  XYLayout xYLayout2 = new XYLayout();
  TitledBorder tbPripGrupArt;
  JlrNavField jrfCGRART = new JlrNavField();
  JlrNavField jrfCGRARTPRIPNAZ = new JlrNavField();
  JraButton jbCGRARTPRIP = new JraButton();
  JLabel jlNaziv1 = new JLabel();
  JLabel jlSifra1 = new JLabel();
  JraTextField jtfPPOP = new JraTextField();
  JraTextField jtfCAR = new JraTextField();
  JraTextField jtfOTR = new JraTextField();
  JraTextField jtfTRO = new JraTextField();

  raComboBox jraTipKalkul = new raComboBox();
  raComboBox rcmbKOMADNO = new raComboBox();
  
  JLabel JlNap = new JLabel("Grupa napomena");
  JlrNavField jlrCAN = new JlrNavField();
  JlrNavField jlrNAZAN = new JlrNavField();
  JraButton jbNap = new JraButton();
  
  JLabel jLabel1 = new JLabel();
  com.borland.dx.sql.dataset.QueryDataSet grart;

  public frmGrupArt() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

  }
  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    grart = hr.restart.baza.Grupart.getDataModule().getFilteredDataSet("");
    jp.setLayout(xYLayout1);
    this.setRaDetailPanel(jp);
    this.setRaQueryDataSet(dm.getAllGrupart());
    this.setVisibleCols(new int[] {0,1,2});
    this.getRepRunner().addReport("hr.restart.robno.repGrupArt","Popis grupe artikala",2);
    jpCGRARTPRIP.setLayout(xYLayout2);
    tbPripGrupArt = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),res.getString("jtpCGRARTPRIP_text"));
    jpCGRARTPRIP.setBorder(tbPripGrupArt);
    hr.restart.zapod.frmOrgStr os;

    jlCGRART.setText(res.getString("jlCGRART_text"));
    jcbCGRARTPRIP.setText(res.getString("jcbCGRARTPRIP_text"));
    jcbAKTIV.setText(res.getString("jcbAKTIV_text"));
    jlCGRARTPRIP.setText(res.getString("jlCGRARTPRIP_text"));
    jbCGRARTPRIP.setText(res.getString("jbKEYF9_text"));

    jtfNAZGRART.setColumnName("NAZGRART");
    jtfNAZGRART.setDataSet(getRaQueryDataSet());
    /*jtfNAZGRART.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfNAZGRART_focusLost(e);
      }
    });*/
    jtfCGRART.setColumnName("CGRART");
    jtfCGRART.setDataSet(getRaQueryDataSet());
    /*jtfCGRART.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfCGRART_focusLost(e);
      }
    });*/
    jcbCGRARTPRIP.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbCGRARTPRIP_actionPerformed(e);
      }
    });
    jcbAKTIV.setUnselectedDataValue("N");
    jcbAKTIV.setSelectedDataValue("D");
    jcbAKTIV.setColumnName("AKTIV");
    jcbAKTIV.setDataSet(getRaQueryDataSet());
    jcbAKTIV.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAKTIV.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbAKTIV.setSelected(true);

    jrfCGRART.setColumnName("CGRARTPRIP");
    jrfCGRART.setNavColumnName("CGRART");
    jrfCGRART.setDataSet(getRaQueryDataSet());
    jrfCGRART.setColNames(new String[] {"NAZGRART"});
    jrfCGRART.setVisCols(new int[]{0,1,2});
    jrfCGRART.setTextFields(new javax.swing.text.JTextComponent[] {jrfCGRARTPRIPNAZ});
    jrfCGRART.setRaDataSet(grart);
    jrfCGRART.setNavButton(jbCGRARTPRIP);
    
    jrfCGRARTPRIPNAZ.setColumnName("NAZGRART");
    jrfCGRARTPRIPNAZ.setSearchMode(1);
    jrfCGRARTPRIPNAZ.setNavProperties(jrfCGRART);
    
    raDataSet artnap = new raDataSet();
    frmArtNap.createMain(artnap);
    artnap.setTableName("artnap");
    
    jlrCAN.setColumnName("CAN");
    jlrCAN.setColNames(new String[] {"NAZAN"});
    jlrCAN.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZAN});
    jlrCAN.setVisCols(new int[] {0,1});
    jlrCAN.setSearchMode(0);
    jlrCAN.setRaDataSet(artnap);
    jlrCAN.setDataSet(getRaQueryDataSet());
    jlrCAN.setNavButton(jbNap);
    
    jlrNAZAN.setColumnName("NAZAN");
    jlrNAZAN.setSearchMode(1);
    jlrNAZAN.setNavProperties(jlrCAN);
    
    jlNaziv1.setText("Naziv");
    jlSifra1.setText("Šifra");
    xYLayout1.setWidth(570);
    xYLayout1.setHeight(265);
    
    jtfPPOP.setColumnName("PPOP");
    jtfPPOP.setDataSet(getRaQueryDataSet());
    
    jtfCAR.setColumnName("CARINA");
    jtfCAR.setDataSet(getRaQueryDataSet());
    
    jtfOTR.setColumnName("POSTOINV");
    jtfOTR.setDataSet(getRaQueryDataSet());
    
    jtfTRO.setColumnName("TROSARINA");
    jtfTRO.setDataSet(getRaQueryDataSet());
    
    jraTipKalkul.setRaDataSet(getRaQueryDataSet());
    jraTipKalkul.setRaColumn("TIPINV");
    jraTipKalkul.setRaItems(new String[][] {
        {"Ukupni izlaz", "I"} 
    });
    
    rcmbKOMADNO.setRaItems(new String[][] {
        {"NE","N"},
        {"DA","D"}
      });
    rcmbKOMADNO.setDataSet(getRaQueryDataSet());
    rcmbKOMADNO.setRaColumn("KOMADNO");
    
    
    jLabel1.setText("Popust na POS-u (%)");
    jp.add(jlCGRART, new XYConstraints(15, 38, -1, -1));
    jp.add(jpCGRARTPRIP, new XYConstraints(15, 190, 525, 65));
    jpCGRARTPRIP.add(jlCGRARTPRIP, new XYConstraints(15, 5, -1, -1));
    jpCGRARTPRIP.add(jrfCGRARTPRIPNAZ, new XYConstraints(240, 5, 245, -1));
    jpCGRARTPRIP.add(jrfCGRART, new XYConstraints(130, 5, 100, -1));
    jpCGRARTPRIP.add(jbCGRARTPRIP, new XYConstraints(489, 5, 21, 21));
    jp.add(jtfNAZGRART, new XYConstraints(260, 38, 280, -1));
    
    
    jp.add(jcbAKTIV, new XYConstraints(440, 15, 100, -1));
    jp.add(jtfCGRART, new XYConstraints(150, 38, 100, -1));
    jp.add(jlSifra1, new XYConstraints(150, 15, -1, -1));
    jp.add(jlNaziv1, new XYConstraints(260, 15, -1, -1));
    jp.add(jLabel1, new XYConstraints(15, 90, -1, -1));
    jp.add(jtfPPOP, new XYConstraints(150, 88, 100, -1));

    jp.add(new JLabel("Postotak dopuštenog otpisa"), 
        new XYConstraints(275, 90, 164, -1));
    jp.add(jtfOTR, new XYConstraints(440, 88, 100, -1));
    jp.add(new JLabel("Naèin raèunanja otpisa"), 
        new XYConstraints(275, 115, 164, -1));
    jp.add(jraTipKalkul, new XYConstraints(440, 113, 100, -1));
    
    jp.add(new JLabel("Carina"), new XYConstraints(15, 65, 100, -1));
    jp.add(jtfCAR, new XYConstraints(150, 63, 100, -1));
    jp.add(new JLabel("Trošarina"), new XYConstraints(275, 65, 100, -1));
    jp.add(jtfTRO, new XYConstraints(440, 63, 100, -1));
    jp.add(new JLabel("Komadna prodaja"), new XYConstraints(15, 115, 100, -1));
    jp.add(rcmbKOMADNO, new XYConstraints(150, 113, 100, -1));
    jp.add(JlNap, new XYConstraints(15, 140, -1, -1));
    jp.add(jlrCAN, new XYConstraints(150, 140, 100, -1));
    jp.add(jlrNAZAN, new XYConstraints(260, 140, 280, -1));
    jp.add(jbNap, new XYConstraints(545, 140, 21, 21));
    
    jp.add(jcbCGRARTPRIP, new XYConstraints(15, 165, -1, -1));
  }

  public boolean Validacija(char mode) {
    if (mode=='N') {
      if (hr.restart.util.Valid.getValid().notUnique(jtfCGRART))
        return false;
    }
    if (hr.restart.util.Valid.getValid().isEmpty(jtfNAZGRART))
      return false;
    if (mode=='N') {
      if (getRaQueryDataSet().getString("CGRARTPRIP").trim().equals("")) {
      	getRaQueryDataSet().setString("CGRARTPRIP", getRaQueryDataSet().getString("CGRART"));
      }
    }
    else {
      if (jcbCGRARTPRIP.isSelected()) {
      	getRaQueryDataSet().setString("CGRARTPRIP", getRaQueryDataSet().getString("CGRART"));
      }
    }
    return true;
  }
  
  public void SetFokus(char mode) {
    if (mode=='N') {
      jrfCGRART.setText("");
      jrfCGRARTPRIPNAZ.setText("");
      rcc.setLabelLaF(jtfCGRART,true);
      jtfCGRART.requestFocus();
    }
    else if (mode=='I') {
      rcc.setLabelLaF(jtfCGRART,false);
      jtfNAZGRART.requestFocus();
    }
  }
  public boolean DeleteCheck() {
    return util.isDeleteable("GRUPART", "CGRARTPRIP", 
    		getRaQueryDataSet().getString("CGRART"), util.MOD_STR, true);
  }

  public void EntryPoint(char mode) {
    cMode=mode;
    jrfCGRART.setText(getRaQueryDataSet().getString("CGRARTPRIP"));
    if (mode=='I') {
      if (getRaQueryDataSet().getString("CGRARTPRIP").trim().
      		equals(getRaQueryDataSet().getString("CGRART").trim())) {
        jcbCGRARTPRIP.setSelected(true);
      }
      else {
        jcbCGRARTPRIP.setSelected(false);
      }
    }
    else {
      jcbCGRARTPRIP.setSelected(true);
    }
    jcbCGRARTPRIP_actionPerformed(null);
  }

  void jcbCGRARTPRIP_actionPerformed(ActionEvent e) {
    if (jcbCGRARTPRIP.isSelected()) {
      rcc.EnabDisabAll(this.jpCGRARTPRIP,false);
      jrfCGRART.setText(getRaQueryDataSet().getString("CGRART"));
      jrfCGRARTPRIPNAZ.setText(getRaQueryDataSet().getString("NAZGRART"));
    }
    else {
      rcc.EnabDisabAll(this.jpCGRARTPRIP,true);
      jrfCGRART.requestFocus();
    }
  }

/*  private com.borland.dx.sql.dataset.QueryDataSet getgrart2() {
    System.out.println("getgrart");
    if (grart==null) {
      System.out.println("getGrart stvarni");
      grart = new com.borland.dx.sql.dataset.QueryDataSet();
      try {
        grart.setQuery(dm.getGrupart().getQuery());
        grart.setColumns(dm.getGrupart().cloneColumns());
        grart.open();
      } catch (Exception e) {e.printStackTrace();}
    }
    return grart;
  }*/
}
