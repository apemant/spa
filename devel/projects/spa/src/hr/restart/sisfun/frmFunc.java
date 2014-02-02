/****license*****************************************************************
**   file: frmFunc.java
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
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raSifraNaziv;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import bsh.EvalError;

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

public class frmFunc extends raSifraNaziv {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();

  JlrNavField jlrApp = new JlrNavField();
  JlrNavField jlrNazapp = new JlrNavField();
  JLabel jlApp = new JLabel();
  JraButton jbApp = new JraButton();
  JraTextField jraSpec = new JraTextField();
  JraTextField jraData = new JraTextField();

  public frmFunc() {
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
    }
  }


  public void SetFokus(char mode) {
    if (mode == 'N') {
      jlrApp.setText("");
      jlrApp.forceFocLost();
    }
    super.SetFokus(mode);
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(this.jtfCSIFRA)) return false;
    if (mode == 'N' && vl.notUnique(new JTextComponent[] {jtfCSIFRA, jlrApp})) return false;
    int p = getRaQueryDataSet().getString("DATA").indexOf(" => ");
    if (p > 0) {
      try {
        new bsh.Interpreter().eval(getRaQueryDataSet().getString("DATA").substring(p + 4));
      } catch (EvalError e) {
        JOptionPane.showMessageDialog(this.getWindow(), new raMultiLineMessage(e.getMessage()), "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaDataSet(dm.getFunkcije());
    this.setRaColumnSifra("CFUNC");
    this.setRaColumnNaziv("OPISFUNC");
    this.setRaText("Funkcija");

    jlApp.setText("Aplikacija");
    jbApp.setText("...");

    jlrApp.setColumnName("APP");
    jlrApp.setDataSet(this.getRaQueryDataSet());
    jlrApp.setColNames(new String[] {"OPIS"});
    jlrApp.setTextFields(new JTextComponent[] {jlrNazapp});
    jlrApp.setSearchMode(0);
    jlrApp.setRaDataSet(dm.getAplikacija());
    jlrApp.setVisCols(new int[] {0,3});
    jlrApp.setNavButton(jbApp);

    jlrNazapp.setColumnName("OPIS");
    jlrNazapp.setNavProperties(jlrApp);
    jlrNazapp.setSearchMode(1);
    
    jraSpec.setDataSet(this.getRaQueryDataSet());
    jraSpec.setColumnName("SPEC");
    jraData.setDataSet(this.getRaQueryDataSet());
    jraData.setColumnName("DATA");

    JPanel jp = (JPanel) this.getRaDetailPanel().getComponent(0);
    ((XYLayout) jp.getLayout()).setHeight(160);
    ((XYLayout) jp.getLayout()).setWidth(575);

    jp.add(jlApp, new XYConstraints(15, 65, -1, -1));
    jp.add(jlrApp, new XYConstraints(150, 65, 100, -1));
    jp.add(jlrNazapp, new XYConstraints(255, 65, 285, -1));
    jp.add(jbApp, new XYConstraints(545, 65, 21, 21));
    jp.add(new JLabel("Definicija"), new XYConstraints(15, 100, -1, -1));
    jp.add(jraSpec, new XYConstraints(150, 100, 390, -1));
    jp.add(new JLabel("Dodatni podaci"), new XYConstraints(15, 125, -1, -1));
    jp.add(jraData, new XYConstraints(150, 125, 390, -1));
  }
}