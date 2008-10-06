/****license*****************************************************************
**   file: jpRadnici.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpRadnici extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  frmRadnici fRadnici;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCorg = new JLabel();
  JLabel jlCradnik = new JLabel();
//  JLabel jlIme = new JLabel();
  JLabel jlImeoca = new JLabel();
//  JLabel jlPrezime = new JLabel();
  JLabel jlTitula = new JLabel();
  JraButton jbSelCorg = new JraButton();
  JraTextField jraCradnik = new JraTextField();
  JraTextField jraIme = new JraTextField();
  JraTextField jraImeoca = new JraTextField();
  JraTextField jraPrezime = new JraTextField();
  JraTextField jraTitula = new JraTextField();
  JraCheckBox jcbAktiv = new JraCheckBox();
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  JLabel jlRadnik = new JLabel();

  public jpRadnici(frmRadnici f) {
    try {
      fRadnici = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }



  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(596);
    lay.setHeight(165);

    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fRadnici.getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N");

    jbSelCorg.setText("...");
    jlCorg.setText("Org. jedinica");
    jlCradnik.setText("Mati\u010Dni broj");
//    jlIme.setText("Ime");
    jlImeoca.setText("Ime oca");
//    jlPrezime.setText("Prezime");
    jlTitula.setText("Titula");
    jraCradnik.setColumnName("CRADNIK");
    jraCradnik.setDataSet(fRadnici.getRaQueryDataSet());
    jraIme.setColumnName("IME");
    jraIme.setDataSet(fRadnici.getRaQueryDataSet());
    jraImeoca.setColumnName("IMEOCA");
    jraImeoca.setDataSet(fRadnici.getRaQueryDataSet());
    jraPrezime.setColumnName("PREZIME");
    jraPrezime.setDataSet(fRadnici.getRaQueryDataSet());
    jraTitula.setColumnName("TITULA");
    jraTitula.setDataSet(fRadnici.getRaQueryDataSet());

    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(fRadnici.getRaQueryDataSet());
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(dm.getOrgstruktura());

    jlrCorg.setNavButton(jbSelCorg);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);

    jlRadnik.setText("Ime i prezime");

    jpDetail.add(jlCradnik, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jraCradnik,     new XYConstraints(150, 20, 125, -1));
    jpDetail.add(jcbAktiv,       new XYConstraints(492, 18, -1, -1));
    jpDetail.add(jlRadnik,  new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jraIme,    new XYConstraints(150, 45, 125, -1));
    jpDetail.add(jraPrezime,         new XYConstraints(280, 45, 275, -1));
    jpDetail.add(jlImeoca,  new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jraImeoca,   new XYConstraints(150, 70, 125, -1));
    jpDetail.add(jlTitula,  new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jraTitula,    new XYConstraints(150, 95, 125, -1));
    jpDetail.add(jlCorg,    new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jlrCorg,     new XYConstraints(150, 120, 125, -1));
    jpDetail.add(jlrNaziv,     new XYConstraints(280, 120, 275, -1));
    jpDetail.add(jbSelCorg,    new XYConstraints(560, 120, 21, 21));
//    jpDetail.add(jlIme,  new XYConstraints(150, 47, -1, -1));
//    jpDetail.add(jlPrezime,  new XYConstraints(280, 47, -1, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}

