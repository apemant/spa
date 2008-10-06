/****license*****************************************************************
**   file: jpIsplMj.java
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
package hr.restart.pl;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpIsplMj extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmIsplMj fIsplMj;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCbanke = new JLabel();
  JLabel jlCisplmj = new JLabel();
  JLabel jlNaziv = new JLabel();
  JLabel jlTipfile = new JLabel();
  JLabel jlTipisplmj = new JLabel();
  JraButton jbSelCbanke = new JraButton();
  JraCheckBox jcbAktiv = new JraCheckBox();

  JraTextField jraCisplmj = new JraTextField();
  JraTextField jraNaziv = new JraTextField();
  JraTextField jraTipfile = new JraTextField();
  JraTextField jraTipisplmj = new JraTextField();
  JlrNavField jlrNazbanke = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCbanke = new JlrNavField() {
    public void after_lookUp() {
//      jcbNovspec.requestFocus();
    }
  };
  raComboBox jrbTipIM = new raComboBox();
  raComboBox jrbTipFile = new raComboBox();
  JLabel jIspMJ = new JLabel();
  JPanel jPanel1 = new JPanel();
  JraCheckBox jcbBanspec = new JraCheckBox();
  XYLayout xYLayout1 = new XYLayout();
  JraCheckBox jcbNovspec = new JraCheckBox();
  JLabel jlSpecifikacija = new JLabel();

  public jpIsplMj(frmIsplMj f) {
    try {
      fIsplMj = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(575);
    lay.setHeight(185);

    jbSelCbanke.setText("...");
    jlCbanke.setText("Banka");
    jlCisplmj.setText("Oznaka");
    jlNaziv.setText("Naziv");
    jlTipfile.setText("Tip datoteke");
    jlTipisplmj.setText("Tip isplatnog mjesta");

    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fIsplMj.getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */

    jraCisplmj.setColumnName("CISPLMJ");
    jraCisplmj.setDataSet(fIsplMj.getRaQueryDataSet());
    jraNaziv.setColumnName("NAZIV");
    jraNaziv.setDataSet(fIsplMj.getRaQueryDataSet());
    jraTipfile.setColumnName("TIPFILE");
    jraTipfile.setDataSet(fIsplMj.getRaQueryDataSet());
    jraTipisplmj.setColumnName("TIPISPLMJ");
    jraTipisplmj.setDataSet(fIsplMj.getRaQueryDataSet());

    jlrCbanke.setColumnName("CBANKE");
    jlrCbanke.setDataSet(fIsplMj.getRaQueryDataSet());
    jlrCbanke.setColNames(new String[] {"NAZBANKE"});
    jlrCbanke.setTextFields(new JTextComponent[] {jlrNazbanke});
    jlrCbanke.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCbanke.setSearchMode(0);
    jlrCbanke.setRaDataSet(dm.getBankepl());
    jlrCbanke.setNavButton(jbSelCbanke);

    jlrNazbanke.setColumnName("NAZBANKE");
    jlrNazbanke.setNavProperties(jlrCbanke);
    jlrNazbanke.setSearchMode(1);

    jIspMJ.setText("Isplatno mjesto");
    jPanel1.setBorder(BorderFactory.createEtchedBorder());
    jPanel1.setLayout(xYLayout1);

    jcbBanspec.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbBanspec.setText("Za banku");
    jcbBanspec.setColumnName("BANSPEC");
    jcbBanspec.setDataSet(fIsplMj.getRaQueryDataSet());
    jcbBanspec.setSelectedDataValue("D");
    jcbBanspec.setUnselectedDataValue("N");



    jcbNovspec.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbNovspec.setText("Nov\u010Danica");
    jcbNovspec.setColumnName("NOVSPEC");
    jcbNovspec.setDataSet(fIsplMj.getRaQueryDataSet());
    jcbNovspec.setSelectedDataValue("D");
    jcbNovspec.setUnselectedDataValue("N");




    jlSpecifikacija.setText("Specifikacija");
    jpDetail.add(jbSelCbanke,    new XYConstraints(533, 65, 21, 21));
    jpDetail.add(jcbAktiv,    new XYConstraints(460, 15, 70, -1));
    jpDetail.add(jlCbanke,  new XYConstraints(15, 65, -1, -1));
    jpDetail.add(jlTipisplmj,     new XYConstraints(15, 135, -1, -1));
    jpDetail.add(jlrCbanke,  new XYConstraints(150, 65, 50, -1));
    jpDetail.add(jlrNazbanke,    new XYConstraints(205, 65, 325, -1));
    jpDetail.add(jraCisplmj,  new XYConstraints(150, 40, 50, -1));
    jpDetail.add(jraNaziv,    new XYConstraints(205, 40, 325, -1));
    jpDetail.add(jrbTipIM,      new XYConstraints(150, 135, 130, -1));
    jpDetail.add(jlNaziv,  new XYConstraints(205, 21, -1, -1));
    jpDetail.add(jlCisplmj,  new XYConstraints(150, 21, -1, -1));
    jpDetail.add(jIspMJ,  new XYConstraints(15, 40, -1, -1));
    jpDetail.add(jrbTipFile,      new XYConstraints(400, 135, 130, -1));
    jpDetail.add(jlTipfile,     new XYConstraints(315, 135, -1, -1));
    jpDetail.add(jPanel1,       new XYConstraints(150, 90, 380, 40));
    jPanel1.add(jcbNovspec,  new XYConstraints(15, 5, -1, -1));
    jPanel1.add(jcbBanspec,     new XYConstraints(292, 5, -1, -1));

    jrbTipIM.setRaColumn("TIPISPLMJ");
    jrbTipIM.setRaDataSet(fIsplMj.getRaQueryDataSet());
    jrbTipIM.setRaItems(new String[][] {
      {"Gotovina","G"},
      {"Teku\u0107i","T"},
      {"Štednja","S"}
    });

    jrbTipFile.setRaColumn("TIPFILE");
    jrbTipFile.setRaDataSet(fIsplMj.getRaQueryDataSet());
    jrbTipFile.setRaItems(new String[][] {
      {" ","0"},
      {"Zaba","1"},
      {"Pbz","2"},
      {"Raiffeisen","3"},
      {"Splitska","4"}
    });


    this.add(jpDetail, BorderLayout.CENTER);
    jpDetail.add(jlSpecifikacija,  new XYConstraints(15, 90, -1, -1));
    this.jlrNazbanke.setNextFocusableComponent(this.jcbNovspec);
  }
//  {
//    PreSelect p;
//  }
}
