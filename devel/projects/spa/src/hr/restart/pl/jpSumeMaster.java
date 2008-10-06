/****license*****************************************************************
**   file: jpSumeMaster.java
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


public class jpSumeMaster extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmSume fSume;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCsume = new JLabel();
  JLabel jlaCsume = new JLabel();
  JLabel jlaOpis = new JLabel();
  JraTextField jraCsume = new JraTextField();
  JraTextField jraOpis = new JraTextField();
  raComboBox jcbVrSume = new raComboBox();

  public jpSumeMaster(frmSume md) {
    try {
      fSume = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(570);
    lay.setHeight(95);

    jlCsume.setText("Suma");
    jlaCsume.setHorizontalAlignment(SwingConstants.LEFT);
    jlaCsume.setText("Oznaka");
    jlaOpis.setHorizontalAlignment(SwingConstants.LEFT);
    jlaOpis.setText("Opis");
    jraCsume.setColumnName("CSUME");
    jraCsume.setDataSet(fSume.getMasterSet());
    jraOpis.setColumnName("OPIS");
    jraOpis.setDataSet(fSume.getMasterSet());

    jpDetail.add(jlCsume,  new XYConstraints(15, 35, -1, -1));
    jpDetail.add(jlaCsume,   new XYConstraints(150, 17, -1, -1));
    jpDetail.add(jlaOpis,   new XYConstraints(255, 17, -1, -1));
    jpDetail.add(jraCsume,  new XYConstraints(150, 35, 100, -1));
    jpDetail.add(jraOpis,  new XYConstraints(255, 35, 300, -1));
    jpDetail.add(jcbVrSume,  new XYConstraints(150, 60, 100, -1));

    jcbVrSume.setRaColumn("VRSTA");
    jcbVrSume.setRaDataSet(fSume.getMasterSet());
    jcbVrSume.setRaItems(new String[][] {
      {"Bruto", "1"},
      {"Sati","2"}
    });

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
