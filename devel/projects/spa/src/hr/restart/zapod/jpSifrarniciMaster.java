/****license*****************************************************************
**   file: jpSifrarniciMaster.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpSifrarniciMaster extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmSifrarnici fSifrarnici;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlVrstasif = new JLabel();
  JLabel jlaOpisvrsif = new JLabel();
  JLabel jlaVrstasif = new JLabel();
  JraTextField jraOpisvrsif = new JraTextField();
  JraTextField jraVrstasif = new JraTextField();

  public jpSifrarniciMaster(frmSifrarnici md) {
    try {
      fSifrarnici = md;
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

    jlVrstasif.setText("Šifrarnik");
    jlaOpisvrsif.setHorizontalAlignment(SwingConstants.CENTER);
    jlaOpisvrsif.setText("Opis");
    jlaVrstasif.setHorizontalAlignment(SwingConstants.CENTER);
    jlaVrstasif.setText("Šifra");
    jraOpisvrsif.setColumnName("OPISVRSIF");
    jraOpisvrsif.setDataSet(fSifrarnici.getMasterSet());
    jraVrstasif.setColumnName("VRSTASIF");
    jraVrstasif.setDataSet(fSifrarnici.getMasterSet());

    jpDetail.add(jlVrstasif, new XYConstraints(15, 40, -1, -1));
    jpDetail.add(jlaOpisvrsif, new XYConstraints(256, 23, 298, -1));
    jpDetail.add(jlaVrstasif, new XYConstraints(151, 23, 98, -1));
    jpDetail.add(jraOpisvrsif, new XYConstraints(255, 40, 300, -1));
    jpDetail.add(jraVrstasif, new XYConstraints(150, 40, 100, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
