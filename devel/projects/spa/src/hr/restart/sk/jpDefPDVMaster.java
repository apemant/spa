/****license*****************************************************************
**   file: jpDefPDVMaster.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpDefPDVMaster extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmDefPDV fDefPDV;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCiz = new JLabel();
  JLabel jlaCiz = new JLabel();
  JLabel jlaOpis = new JLabel();
  JraTextField jraCiz = new JraTextField();
  JraTextField jraOpis = new JraTextField();

  public jpDefPDVMaster(frmDefPDV md) {
    try {
      fDefPDV = md;
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

    jlCiz.setText("Izvještaj");
    jlaCiz.setHorizontalAlignment(SwingConstants.CENTER);
    jlaCiz.setText("Oznaka Stavke");
    jlaOpis.setHorizontalAlignment(SwingConstants.CENTER);
    jlaOpis.setText("Opis stavke");
    jraCiz.setColumnName("CIZ");
    jraCiz.setDataSet(fDefPDV.getMasterSet());
    jraOpis.setColumnName("OPIS");
    jraOpis.setDataSet(fDefPDV.getMasterSet());

    jpDetail.add(jlaCiz, new XYConstraints(151, 23, 98, -1));
    jpDetail.add(jlaOpis, new XYConstraints(256, 23, 298, -1));
    jpDetail.add(jlCiz, new XYConstraints(15, 40, -1, -1));
    jpDetail.add(jraCiz, new XYConstraints(150, 40, 100, -1));
    jpDetail.add(jraOpis, new XYConstraints(255, 40, 300, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
