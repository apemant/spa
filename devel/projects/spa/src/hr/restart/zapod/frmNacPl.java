/****license*****************************************************************
**   file: frmNacPl.java
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

import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raComboBox;
import hr.restart.util.raSifraNaziv;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.NavigationEvent;
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

public class frmNacPl extends raSifraNaziv {
  hr.restart.baza.dM dm;
//  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jLabel1 = new JLabel();
  JraTextField jtfPOP = new JraTextField();
  JraTextField jtfVCINC = new JraTextField();
  JraTextField jtfVCDEC = new JraTextField();
//  JraCheckBox jdbCheckBox1 = new JraCheckBox();
//  JraCheckBox jdbCheckBox2 = new JraCheckBox();
  raComboBox jcbPlacFlags = new raComboBox();
  com.borland.dx.dataset.StorageDataSet comboSet;
  String[] stupidFlags = new String[] {"FL_CEK", "FL_KARTICA", "FL_GOT", "SALDAK"};

  JlrNavField jrfVRDOK = new JlrNavField();
  JLabel jlVRDOK = new JLabel();
  JraButton jbVRDOK = new JraButton();
  JlrNavField jrfNAZDOK = new JlrNavField();

  public frmNacPl() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    jLabel1.setText("Popust");
    xYLayout1.setWidth(555);
    xYLayout1.setHeight(95);
    jp.setLayout(xYLayout1);
    this.setRaDataSet(dm.getAllNacpl());
    this.setRaColumnSifra("CNACPL");
    this.setRaColumnNaziv("NAZNACPL");
    this.setRaText("Na\u010Din pla\u0107anja");
    jtfPOP.setDataSet(getRaDataSet());
    jtfPOP.setColumnName("POPUSTI");
    jtfVCINC.setDataSet(getRaDataSet());
    jtfVCINC.setColumnName("VCINC");
    jtfVCDEC.setDataSet(getRaDataSet());
    jtfVCDEC.setColumnName("VCDEC");
/*
    * Standardni naputak za uporabu:
    *
    *   rcbVRSKL.setRaDataSet(dm.getSklad());
    *   rcbVRSKL.setRaColumn("VRSKL");
    *   rcbVRSKL.setRaItems(new String[][] {
    *       {"Skladi\u0161te","S"},
    *       {"Trgovina","T"},
 *   });
*/
    /*getRaDataSet().addNavigationListener(new com.borland.dx.dataset.NavigationListener() {
      public void navigated(com.borland.dx.dataset.NavigationEvent e) {
        cbFlags_setFokus();
        jcbPlacFlags.findCombo();
      }
    });*/
    comboSet = new com.borland.dx.dataset.StorageDataSet();
    comboSet.addColumn("C1",com.borland.dx.dataset.Variant.STRING);
    comboSet.open();
    jcbPlacFlags.setRaDataSet(comboSet);
    jcbPlacFlags.setRaColumn("C1");//a poslije handlamo u Validacija i setFokus
    jcbPlacFlags.setRaItems(new String[][] {
      {"Èekovi","0"}, //FL_CEK='D', a svi ostali 'N'
      {"Kartice","1"}, //FL_KARTICA='D', a svi ostali 'N'
      {"Gotovinski","2"}, //FL_GOT='D', a svi ostali 'N'
      {"Bezgotovinski","3"} //SALDAK='D', a svi ostali 'N'
    });

    jrfVRDOK.setColumnName("VRDOK");
    jrfVRDOK.setDataSet(getRaDataSet());
    jrfVRDOK.setColNames(new String[] {"NAZDOK"});
    jrfVRDOK.setVisCols(new int[]{0,1});
    jrfVRDOK.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZDOK});
    jrfVRDOK.setRaDataSet(dm.getVrdokum());
    jrfVRDOK.setNavButton(jbVRDOK);
    jlVRDOK.setText("Vrsta dokumenta");
    jrfNAZDOK.setColumnName("NAZDOK");
    jrfNAZDOK.setSearchMode(1);
    jrfNAZDOK.setNavProperties(jrfVRDOK);
    
    jp.add(new JLabel("Vrsta",SwingConstants.RIGHT), new XYConstraints(270, 0, 65, -1));
    jp.add(jcbPlacFlags, new XYConstraints(340, 0, 200, -1));
    jp.add(jrfVRDOK,   new XYConstraints(150, 30, 100, -1));
    jp.add(jbVRDOK,     new XYConstraints(544, 30, 21, 21));
    jp.add(jrfNAZDOK,  new XYConstraints(260, 30, 280, -1));
    jp.add(jlVRDOK,  new XYConstraints(15, 30, -1, -1));
    jp.add(new JLabel("Poveæanje cijene"),  new XYConstraints(15, 55, -1, -1));
    jp.add(jtfVCINC,   new XYConstraints(150, 60, 100, -1));
    jp.add(new JLabel("Uèešæe"),  new XYConstraints(360, 60, -1, -1));
    jp.add(jtfVCDEC, new XYConstraints(440, 60, 100, -1));
    
    /*
    jdbCheckBox1.setHorizontalAlignment(SwingConstants.RIGHT);
    jdbCheckBox1.setHorizontalTextPosition(SwingConstants.LEFT);
    jdbCheckBox1.setText("\u010Cekovi");
    jdbCheckBox1.setColumnName("FL_CEK");
    jdbCheckBox1.setDataSet(getRaDataSet());
    jdbCheckBox1.setSelectedDataValue("D");
    jdbCheckBox1.setUnselectedDataValue("N");
    jdbCheckBox2.setHorizontalAlignment(SwingConstants.RIGHT);
    jdbCheckBox2.setHorizontalTextPosition(SwingConstants.LEFT);
    jdbCheckBox2.setText("Kartica");
    jdbCheckBox2.setColumnName("FL_KARTICA");
    jdbCheckBox2.setDataSet(getRaDataSet());
    jdbCheckBox2.setSelectedDataValue("D");
    jdbCheckBox2.setUnselectedDataValue("N");
    jp.add(jdbCheckBox1, new XYConstraints(340, 0, 200, -1));
    jp.add(jdbCheckBox2, new XYConstraints(340, 25, 200, -1));
    
*/
    jp.add(jLabel1, new XYConstraints(15, 0, -1, -1));
    jp.add(jtfPOP,   new XYConstraints(150, 0, 100, -1));
    this.jpRoot.add(jp,java.awt.BorderLayout.SOUTH);
  }
  
  public void raQueryDataSet_navigated(NavigationEvent e) {
    cbFlags_setFokus();
    jcbPlacFlags.findCombo();
  }
  
  public boolean DeleteCheck() {
/*
    if (util.isDeleteable("DOKI", "CNACPL", dm.getNacpl().getString("CNACPL"), util.MOD_STR)==false)
      return false;
    if (util.isDeleteable("RATE", "CNACPL", dm.getNacpl().getString("CNACPL"), util.MOD_STR)==false)
      return false;
*/
    return true;
  }
  private void cbFlags_setFokus() {
    for (int i = 0; i < stupidFlags.length; i++) {
      if (getRaDataSet().getString(stupidFlags[i]).equals("D")) {
        comboSet.setString("C1",i+"");
        return;
      }
    }
    comboSet.setString("C1","0");//default
  }
  private void cbFlags_valid() {
    int ord = Integer.parseInt(comboSet.getString("C1"));
    for (int i = 0; i < stupidFlags.length; i++)
      getRaDataSet().setString(stupidFlags[i],i==ord?"D":"N");
  }
  public void SetFokus2(char mode) {
    cbFlags_setFokus();
    jcbPlacFlags.findCombo();
  }
  public boolean Validacija2(char mode) {
    cbFlags_valid();
    return true;
  }
  public void SetFokus(char mode) {
    super.SetFokus(mode);
    if (mode == 'N') {
  		jrfNAZDOK.setText("");
    }
  }

}