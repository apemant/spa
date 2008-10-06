/****license*****************************************************************
**   file: frmVrdok.java
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
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTextMask;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmVrdok extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  JPanel jpDetail = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlKratica = new JLabel();
  JLabel jlNaziv = new JLabel();
  JraTextField jraKrat = new JraTextField();
  JraTextField jraNaziv = new JraTextField();
  JLabel jlVrsta = new JLabel();
  raComboBox jcbVrsta = new raComboBox();
  JLabel jlTip = new JLabel();
  raComboBox jcbTip = new raComboBox();
  JlrNavField jlrApp = new JlrNavField();
  JlrNavField jlrNazapp = new JlrNavField();
  JraButton jbApp = new JraButton();
  JLabel jlApp = new JLabel();
  JraCheckBox jraKnjiz = new JraCheckBox();
  JLabel jlId = new JLabel();
  JraTextField jraId = new JraTextField();

  public frmVrdok() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void EntryPoint(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jraKrat, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jlrApp.forceFocLost();
      jraKrat.requestFocus();
    } else {
      jraNaziv.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jraKrat) || vl.isEmpty(jraNaziv) || vl.isEmpty(jlrApp))
      return false;
    if (mode == 'N' && vl.notUnique(jraKrat))
      return false;
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaDetailPanel(this.jpDetail);
    this.setRaQueryDataSet(Vrdokum.getDataModule().copyDataSet());
    this.setVisibleCols(new int[] {0,1,2,3,5});

    jraKrat.setDataSet(this.getRaQueryDataSet());
    jraKrat.setColumnName("VRDOK");
    jraNaziv.setDataSet(this.getRaQueryDataSet());
    jraNaziv.setColumnName("NAZDOK");
    jraId.setDataSet(this.getRaQueryDataSet());
    jraId.setColumnName("ID");
    new raTextMask(jraId, 2, false, raTextMask.DIGITS);
    jlKratica.setText("Kratica");
    xYLayout1.setWidth(520);
    xYLayout1.setHeight(130);
    jpDetail.setLayout(xYLayout1);
    jlNaziv.setText("Naziv");
    jlVrsta.setText("Vrsta");
    jlTip.setText("Tip");
    jlApp.setText("Aplikacija");
    jlId.setText("Šifra za PNB");
    jbApp.setText("...");

    jcbVrsta.setRaColumn("VRSDOK");
    jcbVrsta.setRaDataSet(this.getRaQueryDataSet());
    jcbVrsta.setRaItems(new String[][] {{"Ulazni", "U"}, {"Izlazni", "I"}, {"Oboje", "UI"}});

    jcbTip.setRaColumn("TIPDOK");
    jcbTip.setRaDataSet(this.getRaQueryDataSet());
    jcbTip.setRaItems(new String[][] {{"Ostalo", "O"}, {"Skladišni", "S"}, {"Financijski", "F"}, {"Sklad-fin", "SF"}});


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

    jraKnjiz.setHorizontalTextPosition(SwingConstants.LEADING);
    jraKnjiz.setText("Knjiženje  ");
    jraKnjiz.setDataSet(this.getRaQueryDataSet());
    jraKnjiz.setColumnName("KNJIZ");
    jraKnjiz.setSelectedDataValue("D");
    jraKnjiz.setUnselectedDataValue("N");

    jpDetail.add(jlNaziv, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlKratica, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jraKrat, new XYConstraints(150, 20, 75, -1));
    jpDetail.add(jraNaziv, new XYConstraints(150, 45, 210, -1));
    jpDetail.add(jlVrsta, new XYConstraints(245, 20, -1, -1));
    jpDetail.add(jcbVrsta, new XYConstraints(285, 20, 75, -1));
    jpDetail.add(jlTip, new XYConstraints(375, 20, -1, -1));
    jpDetail.add(jcbTip, new XYConstraints(405, 20, 75, -1));
    jpDetail.add(jlrApp, new XYConstraints(150, 70, 75, -1));
    jpDetail.add(jlrNazapp, new XYConstraints(230, 70, 250, -1));
    jpDetail.add(jbApp, new XYConstraints(485, 70, 21, 21));
    jpDetail.add(jlApp, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jraKnjiz, new XYConstraints(375, 44, -1, -1));
    jpDetail.add(jlId, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jraId, new XYConstraints(150, 95, 75, -1));
  }
}