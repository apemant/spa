/****license*****************************************************************
**   file: jpStandOdbici.java
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
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpStandOdbici extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();


  raMatPodaci fOdbici2;

  XYLayout lay = new XYLayout();
  JLabel jlDatpoc = new JLabel();
  JLabel jlDatzav = new JLabel();
  JLabel jlGlavnica = new JLabel();
  JLabel jlIznos = new JLabel();
  JLabel jlPnb2 = new JLabel();
  JLabel jlRata = new JLabel();
  JLabel jlSaldo = new JLabel();
  JLabel jlStopa = new JLabel();
  JraTextField jraDatpoc = new JraTextField();
  JraTextField jraDatzav = new JraTextField();
  JraTextField jraGlavnica = new JraTextField();
  JraTextField jraIznos = new JraTextField() {
    public void valueChanged() {
      jraIznos_focusLost(null);
    }
  };
  JraTextField jraPnb1 = new JraTextField();
  JraTextField jraPnb2 = new JraTextField();
  JraTextField jraRata = new JraTextField();
  JraTextField jraSaldo = new JraTextField();
  JraTextField jraStopa = new JraTextField();
  JlrNavField jraOznVal = new JlrNavField();
  XYLayout xYLayout1 = new XYLayout();
  JraTextField jraRBROdb = new JraTextField();
  JLabel jlRBROdb = new JLabel();
  JLabel jlOznVal = new JLabel();
  JraButton jbOznVal = new JraButton();

  public jpStandOdbici(raMatPodaci f) {
    try {
      fOdbici2 = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {

    jlDatpoc.setText("Po\u010D. obustave");
    jlDatzav.setText("Zav. obustave");
    jlGlavnica.setText("Glavnica");
    jlIznos.setText("Iznos");
    jlPnb2.setText("Poziv na broj");
    jlRata.setText("Rata");
    jlSaldo.setText("Saldo");
    jlStopa.setText("Stopa");
    jraDatpoc.setColumnName("DATPOC");
    jraDatpoc.setDataSet(fOdbici2.getRaQueryDataSet());
    jraDatzav.setColumnName("DATZAV");
    jraDatzav.setDataSet(fOdbici2.getRaQueryDataSet());
    jraGlavnica.setColumnName("GLAVNICA");
    jraGlavnica.setDataSet(fOdbici2.getRaQueryDataSet());
    jraIznos.setColumnName("IZNOS");
    jraIznos.setDataSet(fOdbici2.getRaQueryDataSet());
    jraPnb1.setColumnName("PNB1");
    jraPnb1.setDataSet(fOdbici2.getRaQueryDataSet());
    jraPnb2.setColumnName("PNB2");
    jraPnb2.setDataSet(fOdbici2.getRaQueryDataSet());
    jraRata.setColumnName("RATA");
    jraRata.setDataSet(fOdbici2.getRaQueryDataSet());

    jraOznVal.setColumnName("OZNVAL");
    jraOznVal.setVisCols(new int[]{0,3});
    jraOznVal.setDataSet(fOdbici2.getRaQueryDataSet());
    jraOznVal.setRaDataSet(dm.getValute());
    jraOznVal.setNavButton(this.jbOznVal);
    jraOznVal.setSearchMode(0);

    jraSaldo.setColumnName("SALDO");
    jraSaldo.setDataSet(fOdbici2.getRaQueryDataSet());
    /*jraIznos.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jraIznos_focusLost(e);
      }
    });*/
    jraStopa.setColumnName("STOPA");
    jraStopa.setDataSet(fOdbici2.getRaQueryDataSet());
    jraRBROdb.setColumnName("RBRODB");
    jraRBROdb.setDataSet(fOdbici2.getRaQueryDataSet());

    this.setLayout(xYLayout1);
//    xYLayout1.setWidth(585);
//    xYLayout1.setHeight(185);

    jlRBROdb.setText("Rbr odbitka");
    jlOznVal.setText("Oznaka valute");
//    jlIznos.setVisible(false);
//    jlStopa.setVisible(false);
//    jraIznos.setVisible(false);
//    jraStopa.setVisible(false);
//    jraOznVal.setVisible(false);
//    jlOznVal.setVisible(false);

    jbOznVal.setText("...");
    this.add(jlPnb2,     new XYConstraints(15, 45, -1, -1));
    this.add(jraPnb1,      new XYConstraints(150, 45, 100, -1));
    this.add(jraPnb2,      new XYConstraints(255, 45, 300, -1));
    this.add(jraIznos,    new XYConstraints(150, 70, 100, -1));
    this.add(jlIznos,    new XYConstraints(15, 70, -1, -1));
    this.add(jraStopa,     new XYConstraints(150, 70, 100, -1));
    this.add(jlStopa,     new XYConstraints(15, 70, -1, -1));
    this.add(jraRata,        new XYConstraints(455, 120, 100, -1));
    this.add(jraOznVal,         new XYConstraints(455, 145, 75, -1));
    this.add(jlSaldo,     new XYConstraints(15, 145, -1, -1));
    this.add(jraSaldo,     new XYConstraints(150, 145, 100, -1));
    this.add(jraDatpoc,      new XYConstraints(150, 95, 100, -1));
    this.add(jlDatpoc,      new XYConstraints(15, 95, -1, -1));
    this.add(jraDatzav,        new XYConstraints(455, 95, 100, -1));
    this.add(jlGlavnica,     new XYConstraints(15, 120, -1, -1));
    this.add(jraGlavnica,      new XYConstraints(150, 120, 100, -1));
    this.add(jlDatzav,       new XYConstraints(360, 95, -1, -1));
    this.add(jlRata,      new XYConstraints(360, 120, -1, -1));
    this.add(jraRBROdb,    new XYConstraints(150, 20, 50, -1));
    this.add(jlRBROdb,   new XYConstraints(15, 20, -1, -1));
    this.add(jlOznVal,     new XYConstraints(360, 145, -1, -1));
    this.add(jbOznVal,    new XYConstraints(534, 145, 21, 21));

//    jlIznos.setVisible(false);
//    jlStopa.setVisible(false);
//    jraIznos.setVisible(false);
//    jraStopa.setVisible(false);

  }

  void jraIznos_focusLost(FocusEvent e) {
    if(fOdbici2.getRaQueryDataSet().getBigDecimal("RATA").compareTo(Aus.zero0)==0)
      fOdbici2.getRaQueryDataSet().setBigDecimal("RATA", fOdbici2.getRaQueryDataSet().getBigDecimal("IZNOS"));
  }
}
