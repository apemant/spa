/****license*****************************************************************
**   file: frmKnjigod.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmKnjigod extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlApp = new JLabel();
  JLabel jlCorg = new JLabel();
  JLabel jlGod = new JLabel();
  JraButton jbSelApp = new JraButton();
  JraButton jbSelCorg = new JraButton();
  JraTextField jraGod = new JraTextField();
  JlrNavField jlrNazorg = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrApp = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrOpis = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public frmKnjigod() {
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
      rcc.setLabelLaF(jlrCorg, false);
      rcc.setLabelLaF(jlrNazorg, false);
      rcc.setLabelLaF(jbSelCorg, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jlrApp.forceFocLost();
      jlrCorg.forceFocLost();
      jlrCorg.requestFocus();
    } else if (mode == 'I') {
      jraGod.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jlrCorg) || vl.isEmpty(jraGod))
      return false;
    if (mode == 'N' && vl.notUnique(new JTextComponent[] {jlrCorg, jlrApp, jraGod}))
      return false;
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaDetailPanel(this.jpDetail);
    this.setRaQueryDataSet(dm.getKnjigod());
    this.setVisibleCols(new int[] {0, 1, 2});

    jpDetail.setLayout(lay);
    lay.setWidth(571);
    lay.setHeight(110);

    jbSelApp.setText("...");
    jbSelCorg.setText("...");
    jlApp.setText("Aplikacija");
    jlCorg.setText("Knjigovodstvo");
    jlGod.setText("Godina");
    jraGod.setColumnName("GOD");
    jraGod.setDataSet(this.getRaQueryDataSet());

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

    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(this.getRaQueryDataSet());
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNazorg});
    jlrCorg.setVisCols(new int[] {0, 1});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(dm.getKnjig());
    jlrCorg.setNavButton(jbSelCorg);

    jlrNazorg.setColumnName("NAZIV");
    jlrNazorg.setNavProperties(jlrCorg);
    jlrNazorg.setSearchMode(1);

    jpDetail.add(jbSelApp, new XYConstraints(535, 45, 21, 21));
    jpDetail.add(jbSelCorg, new XYConstraints(535, 20, 21, 21));
    jpDetail.add(jlApp, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlCorg, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlGod, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlrApp, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jlrCorg, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrNazorg, new XYConstraints(255, 20, 275, -1));
    jpDetail.add(jlrOpis, new XYConstraints(255, 45, 275, -1));
    jpDetail.add(jraGod, new XYConstraints(150, 70, 100, -1));
  }
}
