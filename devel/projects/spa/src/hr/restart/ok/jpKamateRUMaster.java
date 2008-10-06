/****license*****************************************************************
**   file: jpKamateRUMaster.java
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
package hr.restart.ok;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpKamateRUMaster extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmKamateRU fKamateRU;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBrojdok = new JLabel();
  JLabel jlCpar = new JLabel();
  JLabel jlDatdok = new JLabel();
  JLabel jlDatdosp = new JLabel();
  JLabel jlIznos = new JLabel();
  JraButton jbSelCpar = new JraButton();
  JraTextField jraBrojdok = new JraTextField();
  JraTextField jraDatdok = new JraTextField() {
    public void valueChanged() {
      fKamateRU.getDosp();
    }
  };
  JraTextField jraDatdosp = new JraTextField();
  JraTextField jraIznos = new JraTextField();
  JlrNavField jlrCpar = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazpar = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpKamateRUMaster(frmKamateRU md) {
    try {
      fKamateRU = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jraBrojdok.setDataSet(ds);
    jraDatdok.setDataSet(ds);
    jraDatdosp.setDataSet(ds);
    jraIznos.setDataSet(ds);
    jlrCpar.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(546);
    lay.setHeight(135);

    jbSelCpar.setText("...");
    jlBrojdok.setText("Broj ra\u010Duna");
    jlCpar.setText("Partner");
    jlDatdok.setText("Datum");
    jlDatdosp.setText("Dospje\u0107e");
    jlIznos.setText("Iznos");
    jraBrojdok.setColumnName("BROJDOK");
    jraDatdok.setColumnName("DATDOK");
    jraDatdosp.setColumnName("DATDOSP");
    jraIznos.setColumnName("IZNOS");

    jlrCpar.setColumnName("CPAR");
    jlrCpar.setColNames(new String[] {"NAZPAR"});
    jlrCpar.setTextFields(new JTextComponent[] {jlrNazpar});
    jlrCpar.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCpar.setSearchMode(0);
    jlrCpar.setRaDataSet(dm.getPartneri());
    jlrCpar.setNavButton(jbSelCpar);

    jlrNazpar.setColumnName("NAZPAR");
    jlrNazpar.setNavProperties(jlrCpar);
    jlrNazpar.setSearchMode(1);

    jpDetail.add(jbSelCpar, new XYConstraints(510, 20, 21, 21));
    jpDetail.add(jlBrojdok, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlCpar, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlDatdok, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlDatdosp, new XYConstraints(315, 70, -1, -1));
    jpDetail.add(jlIznos, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jlrCpar, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrNazpar, new XYConstraints(255, 20, 250, -1));
    jpDetail.add(jraBrojdok, new XYConstraints(150, 45, 355, -1));
    jpDetail.add(jraDatdok, new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jraDatdosp, new XYConstraints(405, 70, 100, -1));
    jpDetail.add(jraIznos, new XYConstraints(150, 95, 100, -1));

    /*jraDatdok.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        fKamateRU.getDosp();
      }
    });*/


    BindComponents(fKamateRU.getMasterSet());
    /**@todo: Odkomentirati sljedeæu liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
