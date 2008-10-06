/****license*****************************************************************
**   file: jpSalKonUpDetail.java
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
package hr.restart.sk;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpSalKonUpDetail extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmSalKonUp fSalKonUp;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBrojdok = new JLabel();
  JLabel jlCorg = new JLabel();
  JLabel jlCpar = new JLabel();
  JLabel jlCskl = new JLabel();
  JLabel jlDatdok = new JLabel();
  JLabel jlIznos = new JLabel();
  JLabel jlOpis = new JLabel();
  JraButton jbSelCorg = new JraButton();
  JraButton jbSelCpar = new JraButton();
  JraButton jbSelCskl = new JraButton();
  JraTextField jraBrojdok = new JraTextField();
  JraTextField jraDatdok = new JraTextField();
  JraTextField jraExtbrojdok = new JraTextField();
  JraTextField jraIznos = new JraTextField();
  JraTextField jraOpis = new JraTextField();
  JlrNavField jlrCskl = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCpar = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazpar = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpSalKonUpDetail(frmSalKonUp md) {
    try {
      fSalKonUp = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(646);
    lay.setHeight(185);

    jbSelCorg.setText("...");
    jbSelCpar.setText("...");
    jbSelCskl.setText("...");
    jlBrojdok.setText("Broj dokumenta");
    jlCorg.setText("Org. jedinica");
    jlCpar.setText("Partner");
    jlCskl.setText("Shema knjiženja");
    jlDatdok.setText("Datum");
    jlIznos.setText("Iznos");
    jlOpis.setText("Opis");
    jraBrojdok.setColumnName("BROJDOK");
    jraBrojdok.setDataSet(fSalKonUp.getDetailSet());
    jraDatdok.setColumnName("DATDOK");
    jraDatdok.setDataSet(fSalKonUp.getDetailSet());
    jraDatdok.setHorizontalAlignment(SwingConstants.CENTER);
    jraExtbrojdok.setColumnName("EXTBROJDOK");
    jraExtbrojdok.setDataSet(fSalKonUp.getDetailSet());
    jraIznos.setColumnName("IZNOS");
    jraIznos.setDataSet(fSalKonUp.getDetailSet());
    jraOpis.setColumnName("OPIS");
    jraOpis.setDataSet(fSalKonUp.getDetailSet());

    jlrCskl.setColumnName("CSKL");
    jlrCskl.setNavColumnName("CVRSK");
    jlrCskl.setDataSet(fSalKonUp.getDetailSet());
    jlrCskl.setVisCols(new int[] {0, 1});
    jlrCskl.setSearchMode(0);
    jlrCskl.setRaDataSet(frmVrskSk.getInstance().getRaQueryDataSet());
    jlrCskl.setNavButton(jbSelCskl);

    jlrCpar.setColumnName("CPAR");
    jlrCpar.setDataSet(fSalKonUp.getDetailSet());
    jlrCpar.setColNames(new String[] {"NAZPAR"});
    jlrCpar.setTextFields(new JTextComponent[] {jlrNazpar});
    jlrCpar.setVisCols(new int[] {0, 1});
    jlrCpar.setSearchMode(0);
    jlrCpar.setRaDataSet(dm.getPartneri());
    jlrCpar.setNavButton(jbSelCpar);

    jlrNazpar.setColumnName("NAZPAR");
    jlrNazpar.setNavProperties(jlrCpar);
    jlrNazpar.setSearchMode(1);

    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(fSalKonUp.getDetailSet());
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0, 1});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbSelCorg);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);

    jpDetail.add(jbSelCorg, new XYConstraints(610, 20, 21, 21));
    jpDetail.add(jbSelCpar, new XYConstraints(610, 45, 21, 21));
    jpDetail.add(jbSelCskl, new XYConstraints(255, 145, 21, 21));
    jpDetail.add(jlBrojdok, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jlCorg, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlCpar, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlCskl, new XYConstraints(15, 145, -1, -1));
    jpDetail.add(jlDatdok, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlIznos, new XYConstraints(415, 145, -1, -1));
    jpDetail.add(jlOpis, new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jlrCorg, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrCpar, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jlrCskl, new XYConstraints(150, 145, 100, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 20, 350, -1));
    jpDetail.add(jlrNazpar, new XYConstraints(255, 45, 350, -1));
    jpDetail.add(jraBrojdok, new XYConstraints(150, 95, 300, -1));
    jpDetail.add(jraDatdok, new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jraExtbrojdok, new XYConstraints(455, 95, 150, -1));
    jpDetail.add(jraIznos, new XYConstraints(505, 145, 100, -1));
    jpDetail.add(jraOpis, new XYConstraints(150, 120, 455, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
