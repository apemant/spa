/****license*****************************************************************
**   file: frmAllParams.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmAllParams extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlApp = new JLabel();
  JLabel jlIme = new JLabel();
  JLabel jlOpis = new JLabel();
  JLabel jlVri = new JLabel();
  JLabel jlTip = new JLabel();
  JraButton jbSelApp = new JraButton();
  JraRadioButton jrbSistemski1 = new JraRadioButton();
  JraRadioButton jrbSistemski2 = new JraRadioButton();
  JraRadioButton jrbSistemski3 = new JraRadioButton();
  JraTextField jraIme = new JraTextField();
  JraTextField jraOpis = new JraTextField();
  JraTextField jraVri = new JraTextField();
  raButtonGroup bg1 = new raButtonGroup();
  JlrNavField jlrOpis = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrApp = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public frmAllParams() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jlrApp, false);
      rcc.setLabelLaF(jlrOpis, false);
      rcc.setLabelLaF(jbSelApp, false);
      rcc.setLabelLaF(jraIme, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      bg1.setSelected(jrbSistemski2);
//      jrbSistemski2.setSelected();
      jlrApp.forceFocLost();
      jlrApp.requestFocus();
    } else if (mode == 'I') {
      jraOpis.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jlrApp) || vl.isEmpty(jraIme))
      return false;
    if (mode == 'N' && vl.notUnique(new JTextComponent[] {jlrApp, jraIme}))
      return false;
//      vl.execSQL("SELECT * FROM parametri WHERE app = '"+this.getRaQueryDataSet().getString("APP")+
//         "' AND param = '"+this.getRaQueryDataSet().getString("PARAM")+"'");
//      vl.RezSet.open();
//      if (vl.RezSet.rowCount() > 0) {
//        jraIme.requestFocus();
//        JOptionPane.showMessageDialog(jpDetail, "Parametar ve\u0107 postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
//        return false;
//      }
//    }
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaDetailPanel(this.jpDetail);
    this.setRaQueryDataSet(dm.getParametri());
    this.setVisibleCols(new int[] {0, 1, 2, 3, 4});

    jpDetail.setLayout(lay);
    lay.setWidth(540);
    lay.setHeight(160);

    bg1.setColumnName("SISTEMSKI");
    bg1.setDataSet(this.getRaQueryDataSet());
    bg1.add(jrbSistemski1, "Sistemski", "S");
    bg1.add(jrbSistemski2, "Globalni", "G");
    bg1.add(jrbSistemski3, "Lokalni", "L");
    bg1.setHorizontalTextPosition(SwingConstants.TRAILING);
    jbSelApp.setText("...");
    jlTip.setText("Vrsta parametra");
    jlApp.setText("Aplikacija");
    jlIme.setText("Naziv parametra");
    jlOpis.setText("Opis parametra");
    jlVri.setText("Vrijednost parametra");
    jraIme.setColumnName("PARAM");
    jraIme.setDataSet(this.getRaQueryDataSet());
    jraOpis.setColumnName("OPISPAR");
    jraOpis.setDataSet(this.getRaQueryDataSet());
    jraVri.setColumnName("VRIJEDNOST");
    jraVri.setDataSet(this.getRaQueryDataSet());

    jlrApp.setColumnName("APP");
    jlrApp.setDataSet(this.getRaQueryDataSet());
    jlrApp.setColNames(new String[] {"OPIS"});
    jlrApp.setTextFields(new JTextComponent[] {jlrOpis});
    jlrApp.setVisCols(new int[] {0, 3});
    jlrApp.setSearchMode(0);
    jlrApp.setRaDataSet(dm.getAplikacija());
    jlrApp.setNavButton(jbSelApp);

    jlrOpis.setColumnName("OPIS");
    jlrOpis.setNavProperties(jlrApp);
    jlrOpis.setSearchMode(1);

    jpDetail.add(jbSelApp, new XYConstraints(500, 20, 21, 21));
    jpDetail.add(jlApp, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlTip, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlIme, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlOpis, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jlVri, new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jlrApp, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrOpis, new XYConstraints(255, 20, 240, -1));
    jpDetail.add(jraIme, new XYConstraints(150, 70, 200, -1));
    jpDetail.add(jraOpis, new XYConstraints(150, 95, 345, -1));
    jpDetail.add(jraVri, new XYConstraints(150, 120, 200, -1));
    jpDetail.add(jrbSistemski1, new XYConstraints(150, 45, 75, -1));
    jpDetail.add(jrbSistemski2, new XYConstraints(255, 45, 75, -1));
    jpDetail.add(jrbSistemski3, new XYConstraints(360, 45, 75, -1));
  }
}
