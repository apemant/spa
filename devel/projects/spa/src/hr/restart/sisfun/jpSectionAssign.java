/****license*****************************************************************
**   file: jpSectionAssign.java
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

import hr.restart.baza.Logodat;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpSectionAssign extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmSectionAssign fSectionAssign;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCorg = new JLabel();
  JLabel jlId = new JLabel();
  JLabel jlVrdokvr = new JLabel();
  JraButton jbSelCorg = new JraButton();
  JraButton jbSelId = new JraButton();
  JraButton jbSelVrdok = new JraButton();
//  JraTextField jraVrdokvr = new JraTextField();
  raComboBox rcbVrdokvr = new raComboBox() {
    public void this_itemStateChanged() {
      super.this_itemStateChanged();
      comboChanged();
    }
  };
  JlrNavField jlrOpis = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrId = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazdok = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrVrdok = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public int combo = -1;

  public jpSectionAssign(frmSectionAssign f) {
    try {
      fSectionAssign = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    rcbVrdokvr.setRaDataSet(ds);
    jlrId.setDataSet(ds);
    jlrCorg.setDataSet(ds);
    jlrVrdok.setDataSet(ds);
  }

  public void setVrdokVisible(boolean vis) {
    jlrVrdok.setVisible(vis);
    jlrVrdok.setEnabled(vis);
    jlrNazdok.setVisible(vis);
    jlrNazdok.setEnabled(vis);
    jbSelVrdok.setVisible(vis);
    jbSelVrdok.setEnabled(vis);
  }

  public void comboChanged() {
    if (combo != rcbVrdokvr.getSelectedIndex()) {
      combo = rcbVrdokvr.getSelectedIndex();
      setVrdokVisible(combo == 4);
    }
  }

  public void setEdit() {
    rcc.setLabelLaF(rcbVrdokvr, false);
    rcc.setLabelLaF(jlrCorg, false);
    rcc.setLabelLaF(jlrNaziv, false);
    rcc.setLabelLaF(jbSelCorg, false);
    rcc.setLabelLaF(jlrVrdok, false);
    rcc.setLabelLaF(jlrNazdok, false);
    rcc.setLabelLaF(jbSelVrdok, false);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(621);
    lay.setHeight(110);

    jbSelCorg.setText("...");
    jbSelId.setText("...");
    jbSelVrdok.setText("...");
    jlCorg.setText("Org. jedinica");
    jlId.setText("Definicija");
    jlVrdokvr.setText("Dio izvještaja");

    rcbVrdokvr.setRaColumn("VRSEC");
    rcbVrdokvr.setRaItems(new String[][] {
    {"Header izlaznih dokumenata", "HID"},
    {"Footer izlaznih dokumenata", "FID"},
    {"Header internih dokumenata", "HUD"},
    {"Footer internih dokumenata", "FUD"},
    {"Podnožje dokumenata", "FDD"}
    });

    jlrId.setColumnName("ID");
    jlrId.setColNames(new String[] {"OPIS"});
    jlrId.setTextFields(new JTextComponent[] {jlrOpis});
    jlrId.setVisCols(new int[] {1}); /**@todo: Dodati visible cols za lookup frame */
    jlrId.setSearchMode(0);
    jlrId.setRaDataSet(Logodat.getDataModule().getFilteredDataSet("RBR = 0"));
    jlrId.setNavButton(jbSelId);
    jlrId.setVisible(false);
    jlrId.setEnabled(false);

    jlrOpis.setColumnName("OPIS");
    jlrOpis.setNavProperties(jlrId);
    jlrOpis.setSearchMode(1);

    jlrCorg.setColumnName("CORG");
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(dm.getOrgstruktura());
    jlrCorg.setNavButton(jbSelCorg);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);

    jlrVrdok.setColumnName("VRDOK");
    jlrVrdok.setColNames(new String[] {"NAZDOK"});
    jlrVrdok.setTextFields(new JTextComponent[] {jlrNazdok});
    jlrVrdok.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrVrdok.setSearchMode(0);
    jlrVrdok.setRaDataSet(dm.getVrdokum());
    jlrVrdok.setNavButton(jbSelVrdok);

    jlrNazdok.setColumnName("NAZDOK");
    jlrNazdok.setNavProperties(jlrVrdok);
    jlrNazdok.setSearchMode(1);

    jpDetail.add(jbSelCorg, new XYConstraints(585, 20, 21, 21));
    jpDetail.add(jbSelId, new XYConstraints(585, 70, 21, 21));
    jpDetail.add(jbSelVrdok, new XYConstraints(585, 45, 21, 21));
    jpDetail.add(jlCorg, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlId, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlVrdokvr, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlrCorg, new XYConstraints(150, 20, 90, -1));
    jpDetail.add(jlrId, new XYConstraints(140, 70, 5, -1));
    jpDetail.add(jlrNazdok, new XYConstraints(395, 45, 185, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(245, 20, 335, -1));
    jpDetail.add(jlrOpis, new XYConstraints(150, 70, 430, -1));
    jpDetail.add(jlrVrdok, new XYConstraints(340, 45, 50, -1));
    jpDetail.add(rcbVrdokvr, new XYConstraints(150, 45, 185, -1));

    /**@todo: Odkomentirati sljedeæu liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
