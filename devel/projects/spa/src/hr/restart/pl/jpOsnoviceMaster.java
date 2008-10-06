/****license*****************************************************************
**   file: jpOsnoviceMaster.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpOsnoviceMaster extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmOsnovice fOsnovice;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCosn = new JLabel();
  JLabel jlaCosn = new JLabel();
  JLabel jlaOpis = new JLabel();
  JraTextField jraCosn = new JraTextField();
  JraTextField jraOpis = new JraTextField();
  raComboBox jcbVrsta = new raComboBox();
  JLabel jlVrsta = new JLabel();

  public jpOsnoviceMaster(frmOsnovice md) {
    try {
      fOsnovice = md;
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

    jcbVrsta.setRaColumn("VRSTA");
    jcbVrsta.setRaDataSet(fOsnovice.getMasterSet());
    jcbVrsta.setRaItems(new String[][]{
      {"Bruto","1"},
      {"Netto","2"}
    });

    jlCosn.setText("Osnovica");
    jlaCosn.setHorizontalAlignment(SwingConstants.LEFT);
    jlaCosn.setText("Oznaka");
    jlaOpis.setHorizontalAlignment(SwingConstants.LEFT);
    jlaOpis.setText("Opis");
    jraCosn.setColumnName("COSN");
    jraCosn.setDataSet(fOsnovice.getMasterSet());
    jraOpis.setColumnName("OPIS");
    jraOpis.setDataSet(fOsnovice.getMasterSet());

    jlVrsta.setText("Vrsta");
    jpDetail.add(jlCosn, new XYConstraints(15, 40, -1, -1));
    jpDetail.add(jlaCosn, new XYConstraints(151, 23, 98, -1));
    jpDetail.add(jlaOpis, new XYConstraints(256, 23, 298, -1));
    jpDetail.add(jraCosn, new XYConstraints(150, 40, 100, -1));
    jpDetail.add(jcbVrsta, new XYConstraints(150, 65, 100, -1));
    jpDetail.add(jraOpis, new XYConstraints(255, 40, 300, -1));
    jpDetail.add(jlVrsta,  new XYConstraints(15, 65, -1, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
