/****license*****************************************************************
**   file: jpKamateDetail.java
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
package hr.restart.sk;

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpKamateDetail extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmKamate fKamate;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlDani = new JLabel();
  JLabel jlDatum = new JLabel();
  JLabel jlVrsta = new JLabel();
  JLabel jlStopa = new JLabel();
  JraTextField jraDani = new JraTextField() {
    public boolean isFocusTraversable() {
      return false;
    }
  };
  JraTextField jraDatum = new JraTextField();
  JraTextField jraStopa = new JraTextField();
  raComboBox rcbVrsta = new raComboBox() {
    public void this_itemStateChanged() {
      super.this_itemStateChanged();
      if (fKamate.raDetail.getMode() == 'N')
        fKamate.setDani();
    }
  };

  public jpKamateDetail(frmKamate md) {
    try {
      fKamate = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jraDani.setDataSet(ds);
    jraDatum.setDataSet(ds);
    jraStopa.setDataSet(ds);
    rcbVrsta.setRaDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
//    lay.setWidth(370);
//    lay.setHeight(110);

    lay.setWidth(475);
    lay.setHeight(55);


    jlDani.setText("Broj dana");
    jlDani.setHorizontalAlignment(SwingConstants.TRAILING);
    jlDatum.setText("Datum");
    jlDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jlVrsta.setText("Vrsta");
    jlVrsta.setHorizontalAlignment(SwingConstants.LEADING);
    jlStopa.setText("Stopa");
    jlStopa.setHorizontalAlignment(SwingConstants.TRAILING);
    jraDani.setColumnName("DANI");
    jraDatum.setColumnName("DATUM");
    jraDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jraStopa.setColumnName("STOPA");
    rcbVrsta.setRaColumn("VRSTA");
    rcbVrsta.setRaItems(new String[][] {
      {"Godišnja", "G"},
      {"Mjese\u010Dna", "M"}
    });

//    jpDetail.add(jlDani, new XYConstraints(15, 70, -1, -1));
//    jpDetail.add(jlDatum, new XYConstraints(15, 20, -1, -1));
//    jpDetail.add(jlStopa, new XYConstraints(15, 45, -1, -1));
//    jpDetail.add(jraDani, new XYConstraints(150, 70, 100, -1));
//    jpDetail.add(jraDatum, new XYConstraints(150, 20, 100, -1));
//    jpDetail.add(jraStopa, new XYConstraints(150, 45, 100, -1));
//    jpDetail.add(rcbVrsta, new XYConstraints(255, 45, 100, -1));

    jpDetail.add(jlDatum, new XYConstraints(30, 7, 100, -1));
    jpDetail.add(jlStopa, new XYConstraints(135, 7, 99, -1));
    jpDetail.add(jlVrsta, new XYConstraints(240, 7, 100, -1));
    jpDetail.add(jlDani, new XYConstraints(345, 7, 99, -1));

    jpDetail.add(jraDatum, new XYConstraints(30, 25, 100, -1));
    jpDetail.add(jraStopa, new XYConstraints(135, 25, 100, -1));
    jpDetail.add(rcbVrsta, new XYConstraints(240, 25, 100, -1));
    jpDetail.add(jraDani, new XYConstraints(345, 25, 100, -1));

    jraDatum.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        if (fKamate.raDetail.getMode() == 'N')
          fKamate.setDani();
      }
    });

    BindComponents(fKamate.getDetailSet());
    /**@todo: Odkomentirati sljedeæu liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
