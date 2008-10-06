/****license*****************************************************************
**   file: jpSumePrim.java
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


public class jpSumePrim extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmSumePrim fSumePrim;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCsume = new JLabel();
  JLabel jlaCsume = new JLabel();
  JLabel jlaOpis = new JLabel();
  JraButton jbSelCsume = new JraButton();
  JlrNavField jlrOpis = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCsume = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpSumePrim(frmSumePrim f) {
    try {
      fSumePrim = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(596);
    lay.setHeight(70);

    jbSelCsume.setText("...");
    jlCsume.setText("Suma");
    jlaCsume.setHorizontalAlignment(SwingConstants.CENTER);
    jlaCsume.setText("Oznaka");
    jlaOpis.setHorizontalAlignment(SwingConstants.CENTER);
    jlaOpis.setText("Opis");

    jlrCsume.setColumnName("CSUME");
    jlrCsume.setDataSet(fSumePrim.getRaQueryDataSet());
    jlrCsume.setColNames(new String[] {"OPIS"});
    jlrCsume.setTextFields(new JTextComponent[] {jlrOpis});
    jlrCsume.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCsume.setSearchMode(0);
    jlrCsume.setRaDataSet(dm.getSume());
    jlrCsume.setNavButton(jbSelCsume);

    jlrOpis.setColumnName("OPIS");
    jlrOpis.setNavProperties(jlrCsume);
    jlrOpis.setSearchMode(1);

    jpDetail.add(jbSelCsume,  new XYConstraints(560, 30, 21, 21));
    jpDetail.add(jlCsume,  new XYConstraints(15, 30, -1, -1));
    jpDetail.add(jlaCsume,  new XYConstraints(151, 13, 98, -1));
    jpDetail.add(jlaOpis,  new XYConstraints(256, 13, 298, -1));
    jpDetail.add(jlrCsume,  new XYConstraints(150, 30, 100, -1));
    jpDetail.add(jlrOpis,  new XYConstraints(255, 30, 300, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
