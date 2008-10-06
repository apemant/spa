/****license*****************************************************************
**   file: jpGlobalMasterMaster.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpGlobalMasterMaster extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmGlobalMaster fGlobalMaster;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCpov = new JLabel();
  JLabel jlCvrodb = new JLabel();
  JLabel jlIznos = new JLabel();
  JLabel jlStopa = new JLabel();
  JraButton jbSelCpov = new JraButton();
  JraButton jbSelCvrodb = new JraButton();
  JraCheckBox jcbAktiv = new JraCheckBox();
  JraTextField jraIznos = new JraTextField();
  JraTextField jraStopa = new JraTextField();
  JlrNavField jlrOpisvrodb = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCvrodb = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazpov = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCpov = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpGlobalMasterMaster(frmGlobalMaster md) {
    try {
      fGlobalMaster = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(546);
    lay.setHeight(160);

    jbSelCpov.setText("...");
    jbSelCvrodb.setText("...");
    jlCpov.setText("Povjerioc - virman");
    jlCvrodb.setText("Vrsta odbitka");
    jlIznos.setText("Iznos");
    jlStopa.setText("Stopa");
    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fGlobalMaster.getMasterSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */
    jraIznos.setColumnName("IZNOS");
    jraIznos.setDataSet(fGlobalMaster.getMasterSet());
    jraStopa.setColumnName("STOPA");
    jraStopa.setDataSet(fGlobalMaster.getMasterSet());

    jlrCvrodb.setColumnName("CVRODB");
    jlrCvrodb.setDataSet(fGlobalMaster.getMasterSet());
    jlrCvrodb.setColNames(new String[] {"OPISVRODB"});
    jlrCvrodb.setTextFields(new JTextComponent[] {jlrOpisvrodb});
    jlrCvrodb.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCvrodb.setSearchMode(0);
    jlrCvrodb.setRaDataSet(dm.getVrsteodb());
    jlrCvrodb.setNavButton(jbSelCvrodb);

    jlrOpisvrodb.setColumnName("OPISVRODB");
    jlrOpisvrodb.setNavProperties(jlrCvrodb);
    jlrOpisvrodb.setSearchMode(1);

    jlrCpov.setColumnName("CPOV");
    jlrCpov.setDataSet(fGlobalMaster.getMasterSet());
    jlrCpov.setColNames(new String[] {"NAZPOV"});
    jlrCpov.setTextFields(new JTextComponent[] {jlrNazpov});
    jlrCpov.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCpov.setSearchMode(0);
    jlrCpov.setRaDataSet(dm.getPovjerioci());
    jlrCpov.setNavButton(jbSelCpov);

    jlrNazpov.setColumnName("NAZPOV");
    jlrNazpov.setNavProperties(jlrCpov);
    jlrNazpov.setSearchMode(1);

    jpDetail.add(jbSelCpov,  new XYConstraints(510, 70, 21, 21));
    jpDetail.add(jbSelCvrodb, new XYConstraints(510, 45, 21, 21));
    jpDetail.add(jcbAktiv,  new XYConstraints(435, 20, 70, -1));
    jpDetail.add(jlCpov, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlCvrodb, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlIznos, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jlStopa, new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jlrCpov, new XYConstraints(150, 70, 50, -1));
    jpDetail.add(jlrCvrodb, new XYConstraints(150, 45, 50, -1));
    jpDetail.add(jlrNazpov,  new XYConstraints(205, 70, 300, -1));
    jpDetail.add(jlrOpisvrodb, new XYConstraints(205, 45, 300, -1));
    jpDetail.add(jraIznos, new XYConstraints(150, 95, 100, -1));
    jpDetail.add(jraStopa, new XYConstraints(150, 120, 100, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
