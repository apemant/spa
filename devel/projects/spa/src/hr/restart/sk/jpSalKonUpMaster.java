/****license*****************************************************************
**   file: jpSalKonUpMaster.java
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
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpSalKonUpMaster extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmSalKonUp fSalKonUp;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBrojizv = new JLabel();
  JLabel jlDatum = new JLabel();
  JLabel jlZiro = new JLabel();
  JraButton jbSelZiro = new JraButton();
  JraTextField jraBrojizv = new JraTextField();
  JraTextField jraDatum = new JraTextField();
  JlrNavField jlrZiro = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpSalKonUpMaster(frmSalKonUp md) {
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
    lay.setWidth(341);
    lay.setHeight(110);

    jbSelZiro.setText("...");
    jlBrojizv.setText("Broj izvoda");
    jlDatum.setText("Datum");
    jlZiro.setText("Žiro ra\u010Dun");
    jraBrojizv.setColumnName("BROJIZV");
    jraBrojizv.setDataSet(fSalKonUp.getMasterSet());
    jraDatum.setColumnName("DATUM");
    jraDatum.setDataSet(fSalKonUp.getMasterSet());
    jraDatum.setHorizontalAlignment(SwingConstants.CENTER);

    jlrZiro.setColumnName("ZIRO");
    jlrZiro.setDataSet(fSalKonUp.getMasterSet());
    jlrZiro.setVisCols(new int[] {1});
    jlrZiro.setSearchMode(0);
    jlrZiro.setRaDataSet(OrgStr.getOrgStr().getKnjigziro(OrgStr.getKNJCORG()));
    jlrZiro.setNavButton(jbSelZiro);

    jpDetail.add(jbSelZiro, new XYConstraints(305, 20, 21, 21));
    jpDetail.add(jlBrojizv, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlDatum, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlZiro, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlrZiro, new XYConstraints(150, 20, 150, -1));
    jpDetail.add(jraBrojizv, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jraDatum, new XYConstraints(150, 70, 100, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
