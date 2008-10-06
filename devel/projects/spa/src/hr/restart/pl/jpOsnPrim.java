/****license*****************************************************************
**   file: jpOsnPrim.java
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


public class jpOsnPrim extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmOsnPrim fOsnPrim;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCosn = new JLabel();
  JLabel jlaCosn = new JLabel();
  JLabel jlaOpis = new JLabel();
  JraButton jbSelCosn = new JraButton();
  JlrNavField jlrOpis = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCosn = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpOsnPrim(frmOsnPrim f) {
    try {
      fOsnPrim = f;
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

    jbSelCosn.setText("...");
    jlCosn.setText("Osnovica");
    jlaCosn.setHorizontalAlignment(SwingConstants.LEFT);
    jlaCosn.setText("Oznaka");
    jlaOpis.setHorizontalAlignment(SwingConstants.LEFT);
    jlaOpis.setText("Opis");

    jlrCosn.setColumnName("COSN");
    jlrCosn.setDataSet(fOsnPrim.getRaQueryDataSet());
    jlrCosn.setColNames(new String[] {"OPIS"});
    jlrCosn.setTextFields(new JTextComponent[] {jlrOpis});
    jlrCosn.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCosn.setSearchMode(0);
    jlrCosn.setRaDataSet(dm.getPlosnovice());
    jlrCosn.setNavButton(jbSelCosn);

    jlrOpis.setColumnName("OPIS");
    jlrOpis.setNavProperties(jlrCosn);
    jlrOpis.setSearchMode(1);

    jpDetail.add(jbSelCosn,  new XYConstraints(560, 30, 21, 21));
    jpDetail.add(jlCosn,  new XYConstraints(15, 30, -1, -1));
    jpDetail.add(jlaCosn,  new XYConstraints(151, 13, 98, -1));
    jpDetail.add(jlaOpis,  new XYConstraints(256, 13, 298, -1));
    jpDetail.add(jlrCosn,  new XYConstraints(150, 30, 100, -1));
    jpDetail.add(jlrOpis,  new XYConstraints(255, 30, 300, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
