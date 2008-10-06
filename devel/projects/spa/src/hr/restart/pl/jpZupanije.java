/****license*****************************************************************
**   file: jpZupanije.java
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


public class jpZupanije extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmZupanije fZupanije;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCzup = new JLabel();
  JLabel jlNazivzup = new JLabel();
  JraCheckBox jcbAktiv = new JraCheckBox();
  JraTextField jraCzup = new JraTextField();
  JraTextField jraNazivzup = new JraTextField();
  JLabel jlZupanija = new JLabel();

  public jpZupanije(frmZupanije f) {
    try {
      fZupanije = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(570);
    lay.setHeight(80);

    jlCzup.setText("Oznaka");
    jlNazivzup.setText("Naziv");
    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fZupanije.getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */
    jraCzup.setColumnName("CZUP");
    jraCzup.setDataSet(fZupanije.getRaQueryDataSet());
    jraNazivzup.setColumnName("NAZIVZUP");
    jraNazivzup.setDataSet(fZupanije.getRaQueryDataSet());

    jlZupanija.setText("Županija");
    jpDetail.add(jcbAktiv,       new XYConstraints(485, 10, 70, -1));
    jpDetail.add(jraCzup,     new XYConstraints(150, 35, 50, -1));
    jpDetail.add(jraNazivzup,        new XYConstraints(205, 35, 350, -1));
    jpDetail.add(jlCzup,     new XYConstraints(150, 13, -1, -1));
    jpDetail.add(jlNazivzup,    new XYConstraints(205, 13, -1, -1));
    jpDetail.add(jlZupanija,    new XYConstraints(15, 35, -1, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
