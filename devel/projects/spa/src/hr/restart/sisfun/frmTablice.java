/****license*****************************************************************
**   file: frmTablice.java
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
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raSifraNaziv;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

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

public class frmTablice extends raSifraNaziv {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();

  JlrNavField jlrApp = new JlrNavField();
  JlrNavField jlrNazapp = new JlrNavField();
  JraTextField jraIme = new JraTextField();
  JraTextField jraKlasa = new JraTextField();
  JLabel jlIme = new JLabel();
  JLabel jlApp = new JLabel();
  JraButton jbApp = new JraButton();

  public frmTablice() {
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
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaDataSet(dm.getTablice());
    this.setRaColumnSifra("CTAB");
    this.setRaColumnNaziv("OPISTAB");
    this.setRaText("Tablica");

    jlApp.setText("Aplikacija");
    jbApp.setText("...");

    jlIme.setText("Ime i klasa");

    jraIme.setColumnName("IMETAB");
    jraIme.setDataSet(this.getRaQueryDataSet());
    jraKlasa.setColumnName("KLASATAB");
    jraKlasa.setDataSet(this.getRaQueryDataSet());

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

    JPanel jp = (JPanel) this.getRaDetailPanel().getComponent(0);
    ((XYLayout) jp.getLayout()).setHeight(125);
    ((XYLayout) jp.getLayout()).setWidth(575);

    jp.add(jlIme, new XYConstraints(15, 65, -1, -1));
    jp.add(jraIme, new XYConstraints(150, 65, 100, -1));
    jp.add(jraKlasa, new XYConstraints(255, 65, 285, -1));
    jp.add(jlApp, new XYConstraints(15, 90, -1, -1));
    jp.add(jlrApp, new XYConstraints(150, 90, 100, -1));
    jp.add(jlrNazapp, new XYConstraints(255, 90, 285, -1));
    jp.add(jbApp, new XYConstraints(545, 90, 21, 21));
    jraIme.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        if (!jraIme.getText().equals(""))
          getRaQueryDataSet().setString("KLASATAB", "hr.restart.baza." + jraIme.getText());
      }
    });

  }
}
