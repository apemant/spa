/****license*****************************************************************
**   file: jpOpcine.java
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


public class jpOpcine extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmOpcine fOpcine;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCopcine = new JLabel();
  JLabel jlCzup = new JLabel();
  JLabel jlNazivop = new JLabel();
  JraButton jbSelCzup = new JraButton();
  JraCheckBox jcbAktiv = new JraCheckBox();
  JraTextField jraCopcine = new JraTextField();
  JraTextField jraNazivop = new JraTextField();
  JlrNavField jlrNazivzup = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCzup = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JLabel jlOpcina = new JLabel();

  public jpOpcine(frmOpcine f) {
    try {
      fOpcine = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);

    jbSelCzup.setText("...");
    jlCopcine.setText("Oznaka");
    jlCzup.setText("Županija");
    jlNazivop.setText("Naziv");
    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fOpcine.getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */
    jraCopcine.setColumnName("COPCINE");
    jraCopcine.setDataSet(fOpcine.getRaQueryDataSet());
    jraNazivop.setColumnName("NAZIVOP");
    jraNazivop.setDataSet(fOpcine.getRaQueryDataSet());

    jlrCzup.setColumnName("CZUP");
    jlrCzup.setDataSet(fOpcine.getRaQueryDataSet());
    jlrCzup.setColNames(new String[] {"NAZIVZUP"});
    jlrCzup.setTextFields(new JTextComponent[] {jlrNazivzup});
    jlrCzup.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCzup.setSearchMode(0);
    jlrCzup.setRaDataSet(dm.getZupanije());
    jlrCzup.setNavButton(jbSelCzup);

    jlrNazivzup.setColumnName("NAZIVZUP");
    jlrNazivzup.setNavProperties(jlrCzup);
    jlrNazivzup.setSearchMode(1);

    lay.setWidth(570);
    lay.setHeight(105);
    jlOpcina.setText("Op\u0107ina");

    jpDetail.add(jbSelCzup,           new XYConstraints(533, 60, 21, 21));
    jpDetail.add(jcbAktiv,           new XYConstraints(460, 10, 70, -1));
    jpDetail.add(jlCzup,   new XYConstraints(15, 60, -1, -1));
    jpDetail.add(jlrCzup,    new XYConstraints(150, 60, 50, -1));
    jpDetail.add(jlrNazivzup,        new XYConstraints(205, 60, 325, -1));
    jpDetail.add(jraCopcine,     new XYConstraints(150, 35, 50, -1));
    jpDetail.add(jraNazivop,      new XYConstraints(205, 35, 325, -1));
    jpDetail.add(jlCopcine,   new XYConstraints(150, 13, -1, -1));
    jpDetail.add(jlNazivop,    new XYConstraints(205, 13, -1, -1));
    jpDetail.add(jlOpcina,    new XYConstraints(15, 35, -1, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
