/****license*****************************************************************
**   file: jpRSPeriod.java
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


public class jpRSPeriod extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  frmRSPeriod fRSPeriod;
  JPanel jpDetail = new JPanel();
  XYLayout lay = new XYLayout();
  JLabel jlOddana = new JLabel();
  JLabel jlRsoo = new JLabel();
  JraButton jbSelRsoo = new JraButton();
  JraTextField jraDodana = new JraTextField();
  JraTextField jraOddana = new JraTextField();
  JLabel jlCop = new JLabel();
  JraButton jbSelCop = new JraButton();

  JlrNavField jlrOpis = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrRsoo = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCop = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivOp = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpRSPeriod(frmRSPeriod f) {
    try {
      fRSPeriod = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(456);
    lay.setHeight(115);

    jbSelRsoo.setText("...");
    jlOddana.setText("Period (od-do)");
    jlRsoo.setText("Osnova osiguranja");
    jraDodana.setColumnName("DODANA");
    jraDodana.setDataSet(fRSPeriod.getRaQueryDataSet());
    jraOddana.setColumnName("ODDANA");
    jraOddana.setDataSet(fRSPeriod.getRaQueryDataSet());

    jlrRsoo.setColumnName("RSOO");
    jlrRsoo.setNavColumnName("CSIF");
    jlrRsoo.setDataSet(fRSPeriod.getRaQueryDataSet());
    jlrRsoo.setColNames(new String[] {"NAZIV"});
    jlrRsoo.setTextFields(new JTextComponent[] {jlrOpis});
    jlrRsoo.setVisCols(new int[] {0, 1});
    jlrRsoo.setSearchMode(0);
    jlrRsoo.setRaDataSet(hr.restart.zapod.Sifrarnici.getSifre("PLOO"));
    jlrRsoo.setNavButton(jbSelRsoo);

    jlrOpis.setColumnName("NAZIV");
    jlrOpis.setNavProperties(jlrRsoo);
    jlrOpis.setSearchMode(1);

    jlCop.setText("Opæina rada");
    jbSelCop.setText("...");
    jlrCop.setColumnName("COPCINE");
    jlrCop.setDataSet(fRSPeriod.getRaQueryDataSet());
    jlrCop.setColNames(new String[] {"NAZIVOP"});
    jlrCop.setTextFields(new JTextComponent[] {jlrNazivOp});
    jlrCop.setVisCols(new int[] {0, 1});
    jlrCop.setSearchMode(0);
    jlrCop.setRaDataSet(dm.getOpcine());
    jlrCop.setNavButton(jbSelCop);

    jlrNazivOp.setColumnName("NAZIVOP");
    jlrNazivOp.setNavProperties(jlrCop);
    jlrNazivOp.setSearchMode(1);
    
    jpDetail.add(jbSelRsoo, new XYConstraints(420, 20, 21, 21));
    jpDetail.add(jlOddana, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlRsoo, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlrOpis, new XYConstraints(215, 20, 200, -1));
    jpDetail.add(jlrRsoo, new XYConstraints(150, 20, 60, -1));
    jpDetail.add(jraDodana,  new XYConstraints(215, 45, 60, -1));
    jpDetail.add(jraOddana, new XYConstraints(150, 45, 60, -1));

    jpDetail.add(jbSelCop, new XYConstraints(420, 70, 21, 21));
    jpDetail.add(jlCop, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlrNazivOp, new XYConstraints(215, 70, 200, -1));
    jpDetail.add(jlrCop, new XYConstraints(150, 70, 60, -1));
    
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
