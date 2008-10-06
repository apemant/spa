/****license*****************************************************************
**   file: jpOsnDetail.java
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
import hr.restart.swing.JraCheckBox;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpOsnDetail extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmOsnovice fOsn;
  JPanel jpDetail = new JPanel();


  XYLayout lay = new XYLayout();
  JLabel jlCvrp = new JLabel();
  JLabel jlaCvrp = new JLabel();
  JLabel jlaNaziv = new JLabel();
  JraCheckBox jcbAktiv = new JraCheckBox();
  JraButton jbSelCvrp = new JraButton();
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCvrp = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpOsnDetail(frmOsnovice md) {
    fOsn = md;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(596);
    lay.setHeight(80);

    jbSelCvrp.setText("...");
    jlCvrp.setText("Vrsta primanja");
    jlaCvrp.setHorizontalAlignment(SwingConstants.LEFT);
    jlaCvrp.setText("Oznaka");
    jlaNaziv.setHorizontalAlignment(SwingConstants.LEFT);
    jlaNaziv.setText("Opis");

    jlrCvrp.setColumnName("CVRP");
    jlrCvrp.setDataSet(fOsn.getDetailSet());
    jlrCvrp.setColNames(new String[] {"NAZIV"});
    jlrCvrp.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCvrp.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCvrp.setSearchMode(0);
    jlrCvrp.setRaDataSet(dm.getVrsteprim());
    jlrCvrp.setNavButton(jbSelCvrp);

    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fOsn.getDetailSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N");

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCvrp);
    jlrNaziv.setSearchMode(1);

    jpDetail.add(jbSelCvrp, new XYConstraints(560, 40, 21, 21));
    jpDetail.add(jlCvrp, new XYConstraints(15, 40, -1, -1));
    jpDetail.add(jlaCvrp,   new XYConstraints(150, 23, -1, -1));
    jpDetail.add(jlaNaziv,   new XYConstraints(255, 23, -1, -1));
    jpDetail.add(jlrCvrp, new XYConstraints(150, 40, 100, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 40, 300, -1));
    jpDetail.add(jcbAktiv,   new XYConstraints(485, 15, 70, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
