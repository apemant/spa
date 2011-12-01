/****license*****************************************************************
**   file: frmSkladiste.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raComboBox;
import hr.restart.util.raMatPodaci;

import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:        Robno poslovanje
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      REST-ART
 * @author REST-ART development team
 *
 * 20.09.2001 - Version 1.0
 */

public class frmSkladiste extends raMatPodaci {
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.baza.dM dm;
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlCORG = new JLabel();
  JLabel jlSTARIDATINV = new JLabel();
  JLabel jlDATINV = new JLabel();
  JraTextField jtfDATINV = new JraTextField();
  JraCheckBox jcbSTATRAD = new JraCheckBox();
  JraTextField jtfSTARIDATINV = new JraTextField();
  JraCheckBox jcbSTATINV = new JraCheckBox();
  JlrNavField jrfCORG = new JlrNavField();
  JlrNavField jrfNAZORG = new JlrNavField();
  JraButton jbCORG = new JraButton();
  raComboBox rcbVRSKL = new raComboBox();
  raComboBox rcbVRZAL = new raComboBox();
  raComboBox rcbTIPSKL = new raComboBox();
  JLabel jlVRSKL = new JLabel();
  JLabel jlVRZAL = new JLabel();
  JLabel jlTIPSKL = new JLabel();
  JLabel jLabel1 = new JLabel();
  JLabel jlSifra = new JLabel();
  JLabel jlNaziv = new JLabel();
  JraTextField jtfCSKL = new JraTextField();
  JraTextField jtfNAZSKL = new JraTextField();
  JraCheckBox jcbAKTIV = new JraCheckBox();
  private JLabel jlNACOBR = new JLabel();
  private raComboBox rcbNACOBR = new raComboBox();

  public frmSkladiste() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    jp.setLayout(xYLayout1);
    this.setRaDetailPanel(jp);
    this.setRaQueryDataSet(dm.getAllSklad());
    this.setVisibleCols(new int[] {0,1,4,7});
    this.getRepRunner().addReport("hr.restart.robno.repSklad","Formatirani ispis skladišta",5);
    jcbSTATINV.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbSTATINV.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbSTATINV.setText(res.getString("jcbSTATINV_text"));
    jcbSTATRAD.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbSTATRAD.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbSTATRAD.setText(res.getString("jcbSTATRAD_text"));
    jlDATINV.setText(res.getString("jlDATINV_text"));
    jlSTARIDATINV.setText(res.getString("jlSTARIDATINV_text"));
    jlCORG.setText(res.getString("jlCORG_text"));
    jlVRSKL.setText(res.getString("jlVRSKL_text"));
    jlVRZAL.setText(res.getString("jlVRZAL_text"));
    jlTIPSKL.setText(res.getString("jlTIPSKL_text"));
    jcbSTATINV.setDataSet(getRaQueryDataSet());
    jcbSTATINV.setSelectedDataValue("D");
    jcbSTATINV.setUnselectedDataValue("N");
    jcbSTATINV.setColumnName("STATINV");
    jtfSTARIDATINV.setDataSet(getRaQueryDataSet());
    jtfSTARIDATINV.setHorizontalAlignment(SwingConstants.CENTER);
    jtfSTARIDATINV.setColumnName("STARIDATINV");
    jcbSTATRAD.setDataSet(getRaQueryDataSet());
    jcbSTATRAD.setSelectedDataValue("D");
    jcbSTATRAD.setUnselectedDataValue("N");
    jcbSTATRAD.setColumnName("STATRAD");
    jtfDATINV.setDataSet(getRaQueryDataSet());
    jtfDATINV.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATINV.setColumnName("DATINV");
    jrfCORG.setColumnName("CORG");
    jrfCORG.setDataSet(getRaQueryDataSet());
    jrfCORG.setRaDataSet(dM.getDataModule().getAllOrgstruktura());
    jrfCORG.setVisCols(new int[] {0,1});
    jrfCORG.setSearchMode(0);
    jrfCORG.setColNames(new String[] {"NAZIV"});
    jrfCORG.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZORG});
    jrfCORG.setNavButton(jbCORG);
    jrfNAZORG.setColumnName("NAZIV");
    jrfNAZORG.setSearchMode(1);
    jrfNAZORG.setNavProperties(jrfCORG);
    jbCORG.setText("...");

    rcbVRSKL.setRaDataSet(getRaQueryDataSet());
    rcbVRSKL.setRaColumn("VRSKL");
    rcbVRSKL.setRaItems(new String[][] {
    {res.getString("rcbVRSKLskladiste_text"),"S"},
    {res.getString("rcbVRSKLtrgovina_text"),"T"},
    {"Tranzit","Z"}
    });
    rcbVRZAL.setRaDataSet(getRaQueryDataSet());
    rcbVRZAL.setRaColumn("VRZAL");
    rcbVRZAL.setRaItems(new String[][] {
    {res.getString("rcbVRZALnabavna_text"),"N"},
    {res.getString("rcbVRZALvele_text"),"V"},
    {res.getString("rcbVRZALmalo_text"),"M"}
    });
    rcbTIPSKL.setRaDataSet(getRaQueryDataSet());
    rcbTIPSKL.setRaColumn("TIPSKL");
    rcbTIPSKL.setRaItems(new String[][] {
    {res.getString("rcbTIPSKLroba_text"),"R"},
    {"Poluproizvod","L"},
	{res.getString("rcbTIPSKLproizvod_text"),"P"},
    {res.getString("rcbTIPSKLmaterijal_text"),"M"},
    {"Reklamni materijal","V"}
    });
    rcbTIPSKL.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        rcbTIPSKL_actionPerformed(e);
      }
    });
    xYLayout1.setWidth(645);
    xYLayout1.setHeight(250);
    jLabel1.setText("Skladište");
    jlSifra.setText("Šifra");
    jlNaziv.setText("Naziv");
    jtfCSKL.setColumnName("CSKL");
    jtfCSKL.setDataSet(getRaQueryDataSet());
    jtfNAZSKL.setColumnName("NAZSKL");
    jtfNAZSKL.setDataSet(getRaQueryDataSet());
    jcbAKTIV.setText(res.getString("jcbAKTIV_text"));
    jcbAKTIV.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAKTIV.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbAKTIV.setColumnName("AKTIV");
    jcbAKTIV.setDataSet(getRaQueryDataSet());
    jcbAKTIV.setSelectedDataValue("D");
    jcbAKTIV.setUnselectedDataValue("N");
    jlNACOBR.setText("Naèin obraèuna");
    rcbNACOBR.setRaItems(new String[][] {
    {"Automatski","A"},
    {"Periodièno","P"}
    });
    rcbNACOBR.setRaColumn("NACOBR");
    rcbNACOBR.setRaDataSet(getRaQueryDataSet());
    jp.add(jLabel1, new XYConstraints(15, 38, -1, -1));
    jp.add(jlCORG, new XYConstraints(15, 63, -1, -1));
    jp.add(jrfCORG, new XYConstraints(150, 63, 100, -1));
    jp.add(jbCORG,   new XYConstraints(609, 63, 21, 21));
    jp.add(jrfNAZORG,  new XYConstraints(260, 63, 345, -1));
    jp.add(jlSifra, new XYConstraints(150, 15, -1, -1));
    jp.add(jlNaziv, new XYConstraints(260, 15, -1, -1));
    jp.add(jtfCSKL, new XYConstraints(150, 38, 100, -1));
    jp.add(jtfNAZSKL,   new XYConstraints(260, 38, 345, -1));
    jp.add(jcbAKTIV,       new XYConstraints(505, 15, 100, -1));
    jp.add(jcbSTATINV,    new XYConstraints(465, 100, 140, -1));
    jp.add(jcbSTATRAD,  new XYConstraints(455, 125, 150, -1));
    jp.add(jlVRSKL, new XYConstraints(15, 100, -1, -1));
    jp.add(rcbVRSKL, new XYConstraints(150, 100, 250, -1));
    jp.add(jlVRZAL, new XYConstraints(15, 125, -1, -1));
    jp.add(rcbVRZAL, new XYConstraints(150, 125, 250, -1));
    jp.add(jlTIPSKL, new XYConstraints(15, 150, -1, -1));
    jp.add(rcbTIPSKL, new XYConstraints(150, 150, 250, -1));
    jp.add(jlSTARIDATINV, new XYConstraints(15, 220, -1, -1));
    jp.add(jtfSTARIDATINV, new XYConstraints(150, 220, 100, -1));
    jp.add(jlDATINV, new XYConstraints(360, 221, -1, -1));
    jp.add(jtfDATINV, new XYConstraints(505, 220, 100, -1));
    jp.add(jlNACOBR,   new XYConstraints(15, 175, -1, -1));
    jp.add(rcbNACOBR,  new XYConstraints(150, 175, 250, -1));

//    raDataIntegrity.installFor(this)
  }
  public boolean Validacija(char mode) {
    if (mode=='N') {
      if (hr.restart.util.Valid.getValid().notUnique(jtfCSKL))
        return false;
      getRaQueryDataSet().setTimestamp("DATINV", util.findLastDayOfYear((new java.math.BigDecimal(hr.restart.util.Valid.getValid().findYear())).intValue()-1));
      getRaQueryDataSet().setTimestamp("DATULDOK", getRaQueryDataSet().getTimestamp("DATINV"));
      getRaQueryDataSet().setTimestamp("DATKNJIZ", getRaQueryDataSet().getTimestamp("DATINV"));
    }
    if (hr.restart.util.Valid.getValid().isEmpty(jtfNAZSKL))
      return false;
    if (hr.restart.util.Valid.getValid().isEmpty(jrfCORG))
      return false;
    if (hr.restart.util.Valid.getValid().chkIsEmpty(jtfSTARIDATINV)){
      getRaQueryDataSet().setTimestamp("STARIDATINV", util.findLastDayOfYear((new java.math.BigDecimal(hr.restart.util.Valid.getValid().findYear())).intValue()-1));
      getRaQueryDataSet().setTimestamp("DATINV", getRaQueryDataSet().getTimestamp("STARIDATINV"));
      getRaQueryDataSet().setTimestamp("DATULDOK", getRaQueryDataSet().getTimestamp("STARIDATINV"));
      getRaQueryDataSet().setTimestamp("DATKNJIZ", getRaQueryDataSet().getTimestamp("STARIDATINV"));
    }
    getRaQueryDataSet().setString("KNJIG", hr.restart.zapod.OrgStr.getOrgStr().getPripKnjig(getRaQueryDataSet().getString("CORG")));
    getRaQueryDataSet().setString("GODINA", hr.restart.util.Valid.getValid().findYear());
    return true;
  }
  public void SetFokus(char mode) {
    checkNACOBR();
    if (mode=='N') {
      rcc.setLabelLaF(jtfCSKL,true);
      jtfCSKL.requestFocus();
      jrfNAZORG.setText("");
    }
    else if (mode=='I'){
      rcc.setLabelLaF(jtfCSKL,false);
      rcc.setLabelLaF(jtfSTARIDATINV,false);
      jtfNAZSKL.requestFocus();
    }
  }
  public boolean DeleteCheck() {
    return util.isDeleteable("STANJE", "CSKL", getRaQueryDataSet().getString("CSKL"), util.MOD_STR);
  }
  public void EntryPoint(char mode) {
    rcc.setLabelLaF(jcbSTATINV,false);
    rcc.setLabelLaF(jcbSTATRAD,false);
    rcc.setLabelLaF(jtfDATINV,false);
  }
  private void checkNACOBR() {
    if (getRaQueryDataSet().getString("TIPSKL").equals("M")) {
      rcc.setLabelLaF(rcbNACOBR, true);
      jlNACOBR.setEnabled(true);
    }
    else {
      rcc.setLabelLaF(rcbNACOBR, false);
      jlNACOBR.setEnabled(false);
    }
  }

  void rcbTIPSKL_actionPerformed(ActionEvent e) {
    checkNACOBR();
  }
}