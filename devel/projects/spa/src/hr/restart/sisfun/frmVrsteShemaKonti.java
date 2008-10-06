/****license*****************************************************************
**   file: frmVrsteShemaKonti.java
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
package hr.restart.sisfun;
import hr.restart.baza.Vrdokum;
import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raSifraNaziv;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

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

public class frmVrsteShemaKonti extends raSifraNaziv {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();

  JlrNavField jlrApp = new JlrNavField() {
    public void after_lookUp() {
      setApp();
    }
  };
  JlrNavField jlrNazapp = new JlrNavField() {
    public void after_lookUp() {
      setApp();
    }
  };
  JLabel jlApp = new JLabel();
  JraButton jbApp = new JraButton();

  JlrNavField jlrVD = new JlrNavField();
  JlrNavField jlrNazVD = new JlrNavField();
  JLabel jlVD = new JLabel();
  JraButton jbVD = new JraButton();

  public frmVrsteShemaKonti() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jlrApp, false);
      rcc.setLabelLaF(jlrNazapp, false);
      rcc.setLabelLaF(jbApp, false);

      rcc.setLabelLaF(jlrVD, false);
      rcc.setLabelLaF(jlrNazVD, false);
      rcc.setLabelLaF(jbVD, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jlrApp.setText("");
      jlrApp.forceFocLost();
      jlrVD.setText("");
      jlrVD.forceFocLost();
    }
    super.SetFokus(mode);
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(this.jtfCSIFRA)) return false;
    if (mode == 'N' && vl.notUnique(new JTextComponent[] {jtfCSIFRA, jlrApp})) return false;
    this.getRaDataSet().setString("VRDOK", jlrVD.getText());
    return true;
  }


  private void jbInit() throws Exception {
    this.setRaDataSet(dm.getVrshemek());
    this.setRaColumnSifra("CVRSK");
    this.setRaColumnNaziv("OPISVRSK");
    this.setRaText("Vrsta sheme");

    jlApp.setText("Aplikacija");
    jbApp.setText("...");

    jlrApp.setColumnName("APP");
    jlrApp.setDataSet(this.getRaDataSet());
    jlrApp.setColNames(new String[] {"OPIS"});
    jlrApp.setTextFields(new JTextComponent[] {jlrNazapp});
    jlrApp.setSearchMode(0);
    jlrApp.setRaDataSet(dm.getAplikacija());
    jlrApp.setVisCols(new int[] {0,3});
    jlrApp.setNavButton(jbApp);

    jlrNazapp.setColumnName("OPIS");
    jlrNazapp.setNavProperties(jlrApp);
    jlrNazapp.setSearchMode(1);

    jlVD.setText("Vrsta dokumenta");
    jbVD.setText("...");

    jlrVD.setColumnName("VRDOK");
    jlrVD.setDataSet(this.getRaDataSet());
    jlrVD.setColNames(new String[] {"NAZDOK"});
    jlrVD.setTextFields(new JTextComponent[] {jlrNazVD});
    jlrVD.setSearchMode(0);
    jlrVD.setRaDataSet(dm.getVrdokumRadni());
    jlrVD.setVisCols(new int[] {0,3});
    jlrVD.setNavButton(jbVD);

    jlrNazVD.setColumnName("NAZDOK");
    jlrNazVD.setNavProperties(jlrVD);
    jlrNazVD.setSearchMode(1);

    JPanel jp = (JPanel) this.getRaDetailPanel().getComponent(0);
    ((XYLayout) jp.getLayout()).setHeight(125);
    ((XYLayout) jp.getLayout()).setWidth(575);

    jp.add(jlApp, new XYConstraints(15, 65, -1, -1));
    jp.add(jlrApp, new XYConstraints(150, 65, 100, -1));
    jp.add(jlrNazapp, new XYConstraints(255, 65, 285, -1));
    jp.add(jbApp, new XYConstraints(545, 65, 21, 21));

    jp.add(jlVD, new XYConstraints(15, 90, -1, -1));
    jp.add(jlrVD, new XYConstraints(150, 90, 100, -1));
    jp.add(jlrNazVD, new XYConstraints(255, 90, 285, -1));
    jp.add(jbVD, new XYConstraints(545, 90, 21, 21));
  }

  private void setApp() {
    Vrdokum.getDataModule().setFilter("app = '"+getRaDataSet().getString("APP")+"'");
    dm.getVrdokumRadni().open();
    jlrVD.forceFocLost();
  }
}