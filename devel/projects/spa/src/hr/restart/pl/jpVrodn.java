/****license*****************************************************************
**   file: jpVrodn.java
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
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpVrodn extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmVrodn fVrodn;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCvro = new JLabel();
  JLabel jlKoef = new JLabel();
  JLabel jlNazivro = new JLabel();
  JraCheckBox jcbAktiv = new JraCheckBox();
  JraTextField jraCvro = new JraTextField();
  JraTextField jraKoef = new JraTextField();
  JraTextField jraNazivro = new JraTextField();
  JLabel jlVrOdn = new JLabel();

  public jpVrodn(frmVrodn f) {
    try {
      fVrodn = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(570);
    lay.setHeight(100);

    jlCvro.setText("Oznaka");
    jlKoef.setText("Koeficijent");
    jlNazivro.setText("Naziv");
    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fVrodn.getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N");
    jraCvro.setColumnName("CVRO");
    jraCvro.setDataSet(fVrodn.getRaQueryDataSet());
    jraKoef.setColumnName("KOEF");
    jraKoef.setDataSet(fVrodn.getRaQueryDataSet());
    jraNazivro.setColumnName("NAZIVRO");
    jraNazivro.setDataSet(fVrodn.getRaQueryDataSet());

    jlVrOdn.setText("Vrsta radnog odnosa");
    jpDetail.add(jcbAktiv,   new XYConstraints(485, 10, 70, -1));
    jpDetail.add(jlCvro,     new XYConstraints(150, 13, -1, -1));
    jpDetail.add(jlKoef,  new XYConstraints(15, 60, -1, -1));
    jpDetail.add(jraCvro,    new XYConstraints(150, 35, 100, -1));
    jpDetail.add(jraKoef,  new XYConstraints(150, 60, 100, -1));
    jpDetail.add(jraNazivro,   new XYConstraints(255, 35, 300, -1));
    jpDetail.add(jlNazivro,   new XYConstraints(255, 13, -1, -1));
    jpDetail.add(jlVrOdn,   new XYConstraints(15, 35, -1, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
