/****license*****************************************************************
**   file: jpMxPrinterRM.java
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
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpMxPrinterRM extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmMxPrinterRM fMxPrinterRM;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCprinter = new JLabel();
  JLabel jlCrm = new JLabel();
  JLabel jlLocalcom = new JLabel();
  JLabel jlRemotecom = new JLabel();
  JraButton jbSelCprinter = new JraButton();
  JraTextField jraCrm = new JraTextField();
  JraTextField jraLocalcom = new JraTextField();
  JraTextField jraOpis = new JraTextField();
  JraTextField jraRemotecom = new JraTextField();
  JlrNavField jlrOpis = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCprinter = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpMxPrinterRM(frmMxPrinterRM f) {
    try {
      fMxPrinterRM = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jraCrm.setDataSet(ds);
    jraLocalcom.setDataSet(ds);
    jraOpis.setDataSet(ds);
    jraRemotecom.setDataSet(ds);
    jlrCprinter.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(591);
    lay.setHeight(135);

    jbSelCprinter.setText("...");
    jlCprinter.setText("Printer");
    jlCrm.setText("Radno mjesto");
    jlLocalcom.setText("Naredba (local)");
    jlRemotecom.setText("Naredba (remote)");
    jraCrm.setColumnName("CRM");
    jraLocalcom.setColumnName("LOCALCOM");
    jraOpis.setColumnName("OPIS");
    jraRemotecom.setColumnName("REMOTECOM");

    jlrCprinter.setColumnName("CPRINTER");
    jlrCprinter.setColNames(new String[] {"OPIS"});
    jlrCprinter.setTextFields(new JTextComponent[] {jlrOpis});
    jlrCprinter.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCprinter.setSearchMode(0);
    jlrCprinter.setRaDataSet(dm.getMxPrinter());
    jlrCprinter.setNavButton(jbSelCprinter);

    jlrOpis.setColumnName("OPIS");
    jlrOpis.setNavProperties(jlrCprinter);
    jlrOpis.setSearchMode(1);

    jpDetail.add(jbSelCprinter, new XYConstraints(555, 45, 21, 21));
    jpDetail.add(jlCprinter, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlCrm, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlLocalcom, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlRemotecom, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jlrCprinter, new XYConstraints(150, 45, 75, -1));
    jpDetail.add(jlrOpis, new XYConstraints(230, 45, 320, -1));
    jpDetail.add(jraCrm, new XYConstraints(150, 20, 75, -1));
    jpDetail.add(jraLocalcom, new XYConstraints(150, 70, 400, -1));
    jpDetail.add(jraOpis, new XYConstraints(230, 20, 320, -1));
    jpDetail.add(jraRemotecom, new XYConstraints(150, 95, 400, -1));

    /**@todo: Odkomentirati sljedeæu liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
