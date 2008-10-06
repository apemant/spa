/****license*****************************************************************
**   file: jpOrgrad.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpOrgrad extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmOrgrad fOrgrad;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCorg = new JLabel();
  JLabel jlCradnik = new JLabel();
  JLabel jlUdiorada = new JLabel();
  JraButton jbSelCorg = new JraButton();
  JraTextField jraCradnik = new JraTextField();
  JraTextField jraUdiorada = new JraTextField();
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JLabel jlPosto = new JLabel();

  public jpOrgrad(frmOrgrad f) {
    try {
      fOrgrad = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(596);
    lay.setHeight(90);

    jbSelCorg.setText("...");
    jlCorg.setText("Org. jedinica");
    jlCradnik.setText("Maticni broj");
    jlUdiorada.setText("Udio rada u drugoj o.j.");
    jraCradnik.setColumnName("CRADNIK");
    jraCradnik.setDataSet(fOrgrad.getRaQueryDataSet());
    jraUdiorada.setColumnName("UDIORADA");
    jraUdiorada.setDataSet(fOrgrad.getRaQueryDataSet());

    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(fOrgrad.getRaQueryDataSet());
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(dm.getOrgstruktura());
    jlrCorg.setNavButton(jbSelCorg);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);

    jlPosto.setText("%");
    jpDetail.add(jbSelCorg, new XYConstraints(560, 20, 21, 21));
    jpDetail.add(jlCorg, new XYConstraints(15, 20, -1, -1));
   // jpDetail.add(jlCradnik, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlUdiorada, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlrCorg, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 20, 300, -1));
    //jpDetail.add(jraCradnik, new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jraUdiorada, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jlPosto,   new XYConstraints(255, 45, -1, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
