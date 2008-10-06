/****license*****************************************************************
**   file: jpDefvaluedok.java
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
package hr.restart.sisfun;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
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


public class jpDefvaluedok extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmDefvaluedok fDefvaluedok;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlKljuc = new JLabel();
  JLabel jlVrdok = new JLabel();
  JLabel jlaDefvalue = new JLabel();
  JLabel jlaKljuc = new JLabel();
  JraButton jbSelVrdok = new JraButton();
  JraTextField jraDefvalue = new JraTextField();
  JraTextField jraKljuc = new JraTextField();
  JlrNavField jlrNazdok = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrVrdok = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpDefvaluedok(frmDefvaluedok f) {
    try {
      fDefvaluedok = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(521);
    lay.setHeight(105);

    jbSelVrdok.setText("...");
    jlKljuc.setText("Default");
    jlVrdok.setText("Vrsta dokumenta");
    jlaDefvalue.setHorizontalAlignment(SwingConstants.CENTER);
    jlaDefvalue.setText("Vrijednost");
    jlaKljuc.setHorizontalAlignment(SwingConstants.CENTER);
    jlaKljuc.setText("Kolona");
    jraDefvalue.setColumnName("DEFVALUE");
    jraDefvalue.setDataSet(fDefvaluedok.getRaQueryDataSet());
    jraKljuc.setColumnName("KLJUC");
    jraKljuc.setDataSet(fDefvaluedok.getRaQueryDataSet());

    jlrVrdok.setColumnName("VRDOK");
    jlrVrdok.setDataSet(fDefvaluedok.getRaQueryDataSet());
    jlrVrdok.setColNames(new String[] {"NAZDOK"});
    jlrVrdok.setTextFields(new JTextComponent[] {jlrNazdok});
    jlrVrdok.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrVrdok.setSearchMode(0);
    jlrVrdok.setRaDataSet(dm.getVrdokum());
    jlrVrdok.setNavButton(jbSelVrdok);

    jlrNazdok.setColumnName("NAZDOK");
    jlrNazdok.setNavProperties(jlrVrdok);
    jlrNazdok.setSearchMode(1);

    jpDetail.add(jbSelVrdok, new XYConstraints(485, 20, 21, 21));
    jpDetail.add(jlKljuc, new XYConstraints(15, 65, -1, -1));
    jpDetail.add(jlVrdok, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlaDefvalue, new XYConstraints(256, 48, 223, -1));
    jpDetail.add(jlaKljuc, new XYConstraints(151, 48, 98, -1));
    jpDetail.add(jlrNazdok, new XYConstraints(255, 20, 225, -1));
    jpDetail.add(jlrVrdok, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jraDefvalue, new XYConstraints(255, 65, 225, -1));
    jpDetail.add(jraKljuc, new XYConstraints(150, 65, 100, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
