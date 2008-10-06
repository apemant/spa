/****license*****************************************************************
**   file: jpKontaIsp.java
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
package hr.restart.os;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
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


public class jpKontaIsp extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmKontaIsp fKontaIsp;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBrojkonta = new JLabel();
  JLabel jlBrojkonta1 = new JLabel();
  JraButton jbSelBrojkonta = new JraButton();
  JraButton jbSelBrojkonta1 = new JraButton();
  JraCheckBox jcbAktiv = new JraCheckBox();
  JlrNavField jlrBrojkonta = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivkonta = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrBrojkonta1 = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivkonta1 = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpKontaIsp(frmKontaIsp f) {
    try {
      fKontaIsp = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(560);
    lay.setHeight(95);

    jbSelBrojkonta.setText("...");
    jbSelBrojkonta1.setText("...");
    jlBrojkonta.setText("Konto osnovice");
    jlBrojkonta1.setText("Konto ispravka");
    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fKontaIsp.getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */

    jlrBrojkonta.setColumnName("BROJKONTA");
    jlrBrojkonta.setDataSet(fKontaIsp.getRaQueryDataSet());
    jlrBrojkonta.setColNames(new String[] {"NAZIVKONTA"});
    jlrBrojkonta.setTextFields(new JTextComponent[] {jlrNazivkonta});
    jlrBrojkonta.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrBrojkonta.setSearchMode(3);
    jlrBrojkonta.setRaDataSet(hr.restart.zapod.raKonta.getAnalitickaKonta());
    jlrBrojkonta.setNavButton(jbSelBrojkonta);

    jlrNazivkonta.setColumnName("NAZIVKONTA");
    jlrNazivkonta.setNavProperties(jlrBrojkonta);
    jlrNazivkonta.setSearchMode(1);

    jlrBrojkonta1.setColumnName("KONTOISP");
    jlrBrojkonta1.setNavColumnName("BROJKONTA");
    jlrBrojkonta1.setDataSet(fKontaIsp.getRaQueryDataSet());
    jlrBrojkonta1.setColNames(new String[] {"NAZIVKONTA"});
    jlrBrojkonta1.setTextFields(new JTextComponent[] {jlrNazivkonta1});
    jlrBrojkonta1.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrBrojkonta1.setSearchMode(3);
    jlrBrojkonta1.setRaDataSet(hr.restart.zapod.raKonta.getAnalitickaKonta());
    jlrBrojkonta1.setNavButton(jbSelBrojkonta1);

    jlrNazivkonta1.setColumnName("NAZIVKONTA");
    jlrNazivkonta1.setNavProperties(jlrBrojkonta1);
    jlrNazivkonta1.setSearchMode(1);

    jpDetail.add(jbSelBrojkonta,    new XYConstraints(520, 30, 21, 21));
    jpDetail.add(jbSelBrojkonta1,    new XYConstraints(520, 55, 21, 21));
    jpDetail.add(jcbAktiv,     new XYConstraints(445, 5, 70, -1));
    jpDetail.add(jlBrojkonta,  new XYConstraints(15, 30, -1, -1));
    jpDetail.add(jlBrojkonta1,  new XYConstraints(15, 55, -1, -1));
    jpDetail.add(jlrBrojkonta,   new XYConstraints(150, 30, 100, -1));
    jpDetail.add(jlrBrojkonta1,   new XYConstraints(150, 55, 100, -1));
    jpDetail.add(jlrNazivkonta,       new XYConstraints(255, 30, 260, -1));
    jpDetail.add(jlrNazivkonta1,     new XYConstraints(255, 55, 260, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
