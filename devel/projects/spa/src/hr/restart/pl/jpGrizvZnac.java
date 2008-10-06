/****license*****************************************************************
**   file: jpGrizvZnac.java
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

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpGrizvZnac extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmGrizvZnac fGrizvZnac;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCvrodb = new JLabel();
  JraButton jbSelCvrodb = new JraButton();
  JlrNavField jlrCZnac = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpGrizvZnac(frmGrizvZnac f) {
    try {
      fGrizvZnac = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(596);
    lay.setHeight(60);

    jbSelCvrodb.setText("...");
    jlCvrodb.setText("Podatak");

    jlrCZnac.setColumnName("CZNAC");
    jlrCZnac.setDataSet(fGrizvZnac.getRaQueryDataSet());
    jlrCZnac.setColNames(new String[] {"ZNACOPIS"});
    jlrCZnac.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCZnac.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCZnac.setSearchMode(0);
    jlrCZnac.setRaDataSet(dm.getPlZnacRad());
    jlrCZnac.setNavButton(jbSelCvrodb);

    jlrNaziv.setColumnName("ZNACOPIS");
    jlrNaziv.setNavProperties(jlrCZnac);
    jlrNaziv.setSearchMode(1);

    jpDetail.add(jbSelCvrodb, new XYConstraints(560, 20, 21, 21));
    jpDetail.add(jlCvrodb, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlrCZnac, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 20, 300, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
