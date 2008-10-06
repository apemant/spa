/****license*****************************************************************
**   file: jpGrizvPrim2.java
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


public class jpGrizvPrim2 extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmGrizvPrim fGrizvPrim;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCgrizv = new JLabel();
  JLabel jlCizv = new JLabel();
  JLabel jlaCizv = new JLabel();
  JLabel jlaOpis = new JLabel();
  JraButton jbSelCgrizv = new JraButton();
  JraButton jbSelCizv = new JraButton();
  JlrNavField jlrCgrizv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrOpis = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCizv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpGrizvPrim2(frmGrizvPrim f) {
    try {
      fGrizvPrim = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(596);
    lay.setHeight(105);

    jbSelCgrizv.setText("...");
    jbSelCizv.setText("...");
    jlCgrizv.setText("Grupa izvještaja");
    jlCizv.setText("Izvještaj");
    jlaCizv.setHorizontalAlignment(SwingConstants.LEFT);
    jlaCizv.setText("Oznaka");
    jlaOpis.setHorizontalAlignment(SwingConstants.LEFT);
    jlaOpis.setText("Opis");

    jlrCgrizv.setColumnName("CGRIZV");
    jlrCgrizv.setDataSet(fGrizvPrim.getRaQueryDataSet());
    jlrCgrizv.setColNames(new String[] {"NAZIV"});
    jlrCgrizv.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCgrizv.setVisCols(new int[] {1, 2}); /**@todo: Dodati visible cols za lookup frame */
    jlrCgrizv.setSearchMode(0);
    jlrCgrizv.setRaDataSet(dm.getGrupeizv());
    jlrCgrizv.setNavButton(jbSelCgrizv);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCgrizv);
    jlrNaziv.setSearchMode(1);

    jlrCizv.setColumnName("CIZV");
    jlrCizv.setDataSet(fGrizvPrim.getRaQueryDataSet());
    jlrCizv.setColNames(new String[] {"OPIS"});
    jlrCizv.setTextFields(new JTextComponent[] {jlrOpis});
    jlrCizv.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCizv.setSearchMode(0);
    jlrCizv.setRaDataSet(dm.getPlizv());
    jlrCizv.setNavButton(jbSelCizv);

    jlrOpis.setColumnName("OPIS");
    jlrOpis.setNavProperties(jlrCizv);
    jlrOpis.setSearchMode(1);

    jpDetail.add(jbSelCgrizv,  new XYConstraints(560, 55, 21, 21));
    jpDetail.add(jbSelCizv,  new XYConstraints(560, 30, 21, 21));
    jpDetail.add(jlCgrizv,  new XYConstraints(15, 55, -1, -1));
    jpDetail.add(jlCizv,  new XYConstraints(15, 30, -1, -1));
    jpDetail.add(jlaCizv,  new XYConstraints(151, 13, 98, -1));
    jpDetail.add(jlaOpis,  new XYConstraints(256, 13, 298, -1));
    jpDetail.add(jlrCgrizv,  new XYConstraints(150, 55, 100, -1));
    jpDetail.add(jlrCizv,  new XYConstraints(150, 30, 100, -1));
    jpDetail.add(jlrNaziv,  new XYConstraints(255, 55, 300, -1));
    jpDetail.add(jlrOpis,  new XYConstraints(255, 30, 300, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
