/****license*****************************************************************
**   file: jpBankePL.java
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
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpBankePL extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmBankePL fBankePL;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBrdom = new JLabel();
  JLabel jlBrposl = new JLabel();
  JLabel jlCbanke = new JLabel();
  JLabel jlCpov = new JLabel();
  JLabel jlNazbanke = new JLabel();
  JraButton jbSelCpov = new JraButton();
  JraCheckBox jcbAktiv = new JraCheckBox();
  JraTextField jraBrdom = new JraTextField();
  JraTextField jraBrposl = new JraTextField();
  JraTextField jraCbanke = new JraTextField();
  JraTextField jraNazbanke = new JraTextField();
  JlrNavField jlrNazpov = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCpov = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JLabel jlBanka = new JLabel();

  public jpBankePL(frmBankePL f) {
    try {
      fBankePL = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(575);
    lay.setHeight(130);

    jbSelCpov.setText("...");
    jlBrdom.setText("Broj domicilne banke");

    jlBrposl.setText("Broj Poslovnice banke");
    jlCbanke.setText("Oznaka");
    jlCpov.setText("Povjerioc - virman");
    jlNazbanke.setText("Naziv");
    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fBankePL.getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */
    jraBrdom.setColumnName("BRDOM");
    jraBrdom.setDataSet(fBankePL.getRaQueryDataSet());
    jraBrposl.setColumnName("BRPOSL");
    jraBrposl.setDataSet(fBankePL.getRaQueryDataSet());
    jraCbanke.setColumnName("CBANKE");
    jraCbanke.setDataSet(fBankePL.getRaQueryDataSet());
    jraNazbanke.setColumnName("NAZBANKE");
    jraNazbanke.setDataSet(fBankePL.getRaQueryDataSet());

    jlrCpov.setColumnName("CPOV");
    jlrCpov.setDataSet(fBankePL.getRaQueryDataSet());
    jlrCpov.setColNames(new String[] {"NAZPOV"});
    jlrCpov.setTextFields(new JTextComponent[] {jlrNazpov});
    jlrCpov.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCpov.setSearchMode(0);
    jlrCpov.setRaDataSet(dm.getPovjerioci());
    jlrCpov.setNavButton(jbSelCpov);

    jlrNazpov.setColumnName("NAZPOV");
    jlrNazpov.setNavProperties(jlrCpov);
    jlrNazpov.setSearchMode(1);

    jlBanka.setText("Banka");
    jpDetail.add(jbSelCpov,        new XYConstraints(533, 65, 21, 21));
    jpDetail.add(jcbAktiv,     new XYConstraints(460, 15, 70, -1));
    jpDetail.add(jlBrdom,  new XYConstraints(15, 90, -1, -1));
    jpDetail.add(jlCpov,  new XYConstraints(15, 65, -1, -1));
    jpDetail.add(jlrCpov,  new XYConstraints(150, 65, 50, -1));
    jpDetail.add(jlrNazpov,      new XYConstraints(205, 65, 325, -1));
    jpDetail.add(jraBrdom,  new XYConstraints(150, 90, 100, -1));
    jpDetail.add(jraCbanke,   new XYConstraints(150, 40, 50, -1));
    jpDetail.add(jraNazbanke,      new XYConstraints(205, 40, 325, -1));
    jpDetail.add(jraBrposl,     new XYConstraints(430, 90, 100, -1));
    jpDetail.add(jlBrposl,    new XYConstraints(285, 90, -1, -1));
    jpDetail.add(jlNazbanke, new XYConstraints(205, 21, -1, -1));
    jpDetail.add(jlCbanke,  new XYConstraints(150, 21, -1, -1));
    jpDetail.add(jlBanka,   new XYConstraints(15, 40, -1, -1));
    this.add(jpDetail, BorderLayout.CENTER);

  }
}
