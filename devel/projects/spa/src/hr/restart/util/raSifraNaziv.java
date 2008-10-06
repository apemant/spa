/****license*****************************************************************
**   file: raSifraNaziv.java
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
package hr.restart.util;

import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <pre>
 * Title:        raSifraNaziv de luxe
 * Description:  Standardni ekran za unos u tablice polja Sifra i Naziv
 * Copyright:    Copyright (c) 2001
 * Company:      REST-ART
 * author        Rest-art development team
 * version       1.0
 *
 *
 *
 *
 * Ovo je cijela pricha koju trebamo napraviti za Shifra/Naziv:
 *
 *
 * public class frmFrankature extends raSifraNaziv {
 * dM dm;
 *
 * public frmFrankature() {
 * try {
 *     jbInit();
 *   }
 *   catch(Exception e) {
 *     e.printStackTrace();
 *   }
 * }
 *
 * private void jbInit() throws Exception {
 *   dm = hr.restart.util.dM.getDataModule();
 *   this.setRaDataSet(dm.getFranka());
 *   this.setRaColumnSifra("CFRA");
 *   this.setRaColumnNaziv("NAZFRA");
 * }
 *
 * </pre>
 */

public class raSifraNaziv extends raMatPodaci {

  public raSifraNaziv() {
    super(2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  private com.borland.dx.sql.dataset.QueryDataSet raDataSet;
  private String raColumnSifra;
  private String raColumnNaziv;
  public XYLayout xYLayout1 = new XYLayout();
  public JPanel jp = new JPanel();
  public JPanel jpRoot = new JPanel(new java.awt.BorderLayout());
  public JraTextField jtfNAZIV = new JraTextField();
  public JLabel jlNaziv = new JLabel();
  public JLabel jlSifra = new JLabel();
  public JraTextField jtfCSIFRA = new JraTextField();
  public JraCheckBox jcbAktivan = new JraCheckBox();
  public JLabel jlText = new JLabel();
  private String raText;
  
  private void jbInit() throws Exception {
    this.setRaDetailPanel(jpRoot);
    this.setVisibleCols(new int[] {0,1});
    jp.setLayout(xYLayout1);
    jcbAktivan.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktivan.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbAktivan.setText("Aktivan");
    jcbAktivan.setColumnName("AKTIV");
    jcbAktivan.setSelectedDataValue("D");
    jcbAktivan.setUnselectedDataValue("N");
    jcbAktivan.setHorizontalAlignment(SwingConstants.RIGHT);
    jlSifra.setText("\u0160ifra");
    jlNaziv.setText("Naziv");
    jlText.setText("Item");
    defaultAdd2Panel(555,73);
    jpRoot.add(jp,java.awt.BorderLayout.CENTER);
  }
  
  public void defaultAdd2Panel(int width,int heigh){
    xYLayout1.setWidth(width);
    xYLayout1.setHeight(heigh);
    jp.add(jlSifra, new XYConstraints(150, 15, 100, -1));
    jp.add(jlNaziv, new XYConstraints(255, 15, 200, -1));
    jp.add(jcbAktivan, new XYConstraints(440, 7, 100, -1));
    jp.add(jlText, new XYConstraints(15, 38, -1, -1));
    jp.add(jtfCSIFRA, new XYConstraints(150, 32, 100, -1));
    jp.add(jtfNAZIV,  new XYConstraints(255, 32, 285, -1));
  }
  
  public void setRaDataSet(com.borland.dx.sql.dataset.QueryDataSet newRaDataSet) {
    raDataSet = newRaDataSet;
    this.setRaQueryDataSet(raDataSet);
    jtfCSIFRA.setDataSet(raDataSet);
    jtfNAZIV.setDataSet(raDataSet);
    jcbAktivan.setDataSet(raDataSet);
  }
  public com.borland.dx.sql.dataset.QueryDataSet getRaDataSet() {
    return raDataSet;
  }
  public void setRaColumnSifra(String newRaColumnSifra) {
    raColumnSifra = newRaColumnSifra;
    jtfCSIFRA.setColumnName(raColumnSifra);
  }
  public String getRaColumnSifra() {
    return raColumnSifra;
  }
  public void setRaColumnNaziv(String newRaColumnNaziv) {
    raColumnNaziv = newRaColumnNaziv;
    jtfNAZIV.setColumnName(raColumnNaziv);
  }
  public String getRaColumnNaziv() {
    return raColumnNaziv;
  }
  public boolean Validacija(char mode) {
    if (mode=='N') {
      if (hr.restart.util.Valid.getValid().notUnique(jtfCSIFRA))
        return false;
    }
    if (hr.restart.util.Valid.getValid().isEmpty(jtfNAZIV))
      return false;
    return Validacija2(mode);
  }

  public void SetFokus(char mode) {
    SetFokus2(mode);
    if (mode=='N') {
      rcc.setLabelLaF(jtfCSIFRA,true);
      jtfCSIFRA.requestFocus();
    }
    else if (mode=='I') {
      rcc.setLabelLaF(jtfCSIFRA,false);
      jtfNAZIV.requestFocus();
    }
  }
  public boolean DeleteCheck() {
    return true;
  }
  public void setRaText(String newRaText) {
    raText = newRaText;
    this.jlText.setText(raText);
  }
  public String getRaText() {
    return raText;
  }
  public void SetFokus2(char mode) {
  }
  public boolean Validacija2(char mode) {
    return true;
  }
}