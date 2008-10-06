/****license*****************************************************************
**   file: jpOdbCorg.java
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
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpOdbCorg extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmOdbCorg fOdbCorg;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCorg = new JLabel();
  JLabel jlCpov = new JLabel();
  JraButton jbSelCorg = new JraButton();
  JraButton jbSelCpov = new JraButton();
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCpov = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazpov = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpOdbCorg(frmOdbCorg f) {
    try {
      fOdbCorg = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jlrCpov.setDataSet(ds);
    jlrCorg.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(596);
    lay.setHeight(85);

    jbSelCorg.setText("...");
    jbSelCpov.setText("...");
    jlCorg.setText("Org. jedinica");
    jlCpov.setText("Povjerioc");

    jlrCpov.setColumnName("CPOV");
    jlrCpov.setColNames(new String[] {"NAZPOV"});
    jlrCpov.setTextFields(new JTextComponent[] {jlrNazpov});
    jlrCpov.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCpov.setSearchMode(0);
    jlrCpov.setRaDataSet(dm.getPovjerioci());
    jlrCpov.setNavButton(jbSelCpov);

    jlrNazpov.setColumnName("NAZPOV");
    jlrNazpov.setNavProperties(jlrCpov);
    jlrNazpov.setSearchMode(1);

    jlrCorg.setColumnName("CORG");
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(dm.getKnjig());
    jlrCorg.setNavButton(jbSelCorg);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);

    jpDetail.add(jbSelCorg, new XYConstraints(560, 20, 21, 21));
    jpDetail.add(jbSelCpov, new XYConstraints(560, 45, 21, 21));
    jpDetail.add(jlCorg, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlCpov, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlrCorg, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrCpov, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 20, 300, -1));
    jpDetail.add(jlrNazpov, new XYConstraints(255, 45, 300, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
