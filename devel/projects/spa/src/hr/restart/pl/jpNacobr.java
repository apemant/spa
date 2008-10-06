/****license*****************************************************************
**   file: jpNacobr.java
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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpNacobr extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmNacobr fNacobr;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCobr = new JLabel();
  JLabel jlFormula = new JLabel();
  JraCheckBox jcbAktiv = new JraCheckBox();
  JraTextField jraCobr = new JraTextField();
  JraTextField jraOpis = new JraTextField();
  JraTextField jraFormula = new JraTextField();
  JPanel jPanel1 = new JPanel();
  JraCheckBox jcbSati = new JraCheckBox();
  XYLayout xYLayout1 = new XYLayout();
  JraCheckBox jcbKoef = new JraCheckBox();
  JraCheckBox jcbIznos = new JraCheckBox();
  private JLabel jlNacObr = new JLabel();
  private JLabel jlNazNacOb = new JLabel();

  public jpNacobr(frmNacobr f) {
    try {
      fNacobr = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(575);
    lay.setHeight(145);

    jlCobr.setText("Oznaka");
    jlFormula.setText("Formula izra\u010Duna");
    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fNacobr.getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */
    jraCobr.setColumnName("COBR");
    jraCobr.setDataSet(fNacobr.getRaQueryDataSet());
    jraOpis.setColumnName("OPIS");
    jraOpis.setDataSet(fNacobr.getRaQueryDataSet());
    jraFormula.setColumnName("FORMULA");
    jraFormula.setDataSet(fNacobr.getRaQueryDataSet());

    jcbSati.setColumnName("UNSATI");
    jcbSati.setDataSet(fNacobr.getRaQueryDataSet());
    jcbSati.setSelectedDataValue("D");
    jcbSati.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */

    jcbKoef.setColumnName("UNKOEF");
    jcbKoef.setDataSet(fNacobr.getRaQueryDataSet());
    jcbKoef.setSelectedDataValue("D");
    jcbKoef.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */

    jcbIznos.setColumnName("UNIZNOS");
    jcbIznos.setDataSet(fNacobr.getRaQueryDataSet());
    jcbIznos.setSelectedDataValue("D");
    jcbIznos.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */



    jPanel1.setBorder(BorderFactory.createEtchedBorder());
    jPanel1.setLayout(xYLayout1);
    jcbSati.setText("Unos sati");
    jcbKoef.setText("Unos koeficijenta");
    jcbIznos.setText("Unos iznosa");
//    this.setMinimumSize(new Dimension(575, 145));
//    this.setPreferredSize(new Dimension(575, 145));
    jlNacObr.setText("Na\u010Din obra\u010Duna");
    jlNazNacOb.setText("Opis");
    jpDetail.add(jcbAktiv,    new XYConstraints(490, 15, 70, -1));
    jpDetail.add(jlFormula,     new XYConstraints(15, 60, -1, -1));
    jpDetail.add(jraCobr,       new XYConstraints(150, 40, 50, -1));
    jpDetail.add(jraOpis,            new XYConstraints(205, 40, 355, -1));
    jpDetail.add(jraFormula,          new XYConstraints(150, 65, 410, -1));
    jpDetail.add(jPanel1,        new XYConstraints(150, 90, 410, 30));
    jPanel1.add(jcbSati,     new XYConstraints(15, 0, -1, -1));
    jPanel1.add(jcbIznos,  new XYConstraints(305, 0, -1, -1));
    jPanel1.add(jcbKoef,  new XYConstraints(143, 0, -1, -1));
    jpDetail.add(jlCobr, new XYConstraints(150, 21, -1, -1));
    jpDetail.add(jlNacObr,  new XYConstraints(15, 40, -1, -1));
    this.add(jpDetail, BorderLayout.CENTER);
    jpDetail.add(jlNazNacOb,   new XYConstraints(205, 21, -1, -1));

  }
}
