/****license*****************************************************************
**   file: jpGlobalMasterDetail.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpGlobalMasterDetail extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmGlobalMaster fGlobalMaster;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlDatpoc = new JLabel();
  JLabel jlDatzav = new JLabel();
  JLabel jlGlavnica = new JLabel();
  JLabel jlIznos = new JLabel();
  JLabel jlPnb1 = new JLabel();
  JLabel jlPnb2 = new JLabel();
  JLabel jlRata = new JLabel();
  JLabel jlSaldo = new JLabel();
  JLabel jlStopa = new JLabel();
  JraTextField jraDatpoc = new JraTextField();
  JraTextField jraDatzav = new JraTextField();
  JraTextField jraGlavnica = new JraTextField();
  JraTextField jraIznos = new JraTextField();
  JraTextField jraPnb1 = new JraTextField();
  JraTextField jraPnb2 = new JraTextField();
  JraTextField jraRata = new JraTextField();
  JraTextField jraSaldo = new JraTextField();
  JraTextField jraStopa = new JraTextField();

  public jpGlobalMasterDetail(frmGlobalMaster md) {
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
    lay.setWidth(410);
    lay.setHeight(260);

    jlDatpoc.setText("Poèetak obustave");
    jlDatzav.setText("Završetak obustave");
    jlGlavnica.setText("Glavnica");
    jlIznos.setText("Iznos");
    jlPnb1.setText("Poziv na broj 1");
    jlPnb2.setText("Poziv na broj");
    jlRata.setText("Rata");
    jlSaldo.setText("Saldo");
    jlStopa.setText("Stopa");
    jraDatpoc.setColumnName("DATPOC");
    jraDatpoc.setDataSet(fGlobalMaster.getDetailSet());
    jraDatzav.setColumnName("DATZAV");
    jraDatzav.setDataSet(fGlobalMaster.getDetailSet());
    jraGlavnica.setColumnName("GLAVNICA");
    jraGlavnica.setDataSet(fGlobalMaster.getDetailSet());
    jraIznos.setColumnName("IZNOS");
    jraIznos.setDataSet(fGlobalMaster.getDetailSet());
    jraPnb1.setColumnName("PNB1");
    jraPnb1.setDataSet(fGlobalMaster.getDetailSet());
    jraPnb2.setColumnName("PNB2");
    jraPnb2.setDataSet(fGlobalMaster.getDetailSet());
    jraRata.setColumnName("RATA");
    jraRata.setDataSet(fGlobalMaster.getDetailSet());
    jraSaldo.setColumnName("SALDO");
    jraSaldo.setDataSet(fGlobalMaster.getDetailSet());
    jraStopa.setColumnName("STOPA");
    jraStopa.setDataSet(fGlobalMaster.getDetailSet());

    jpDetail.add(jlDatpoc, new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jlDatzav, new XYConstraints(15, 145, -1, -1));
    jpDetail.add(jlGlavnica, new XYConstraints(15, 170, -1, -1));
    jpDetail.add(jlIznos, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlPnb1, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlPnb2, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlRata, new XYConstraints(15, 195, -1, -1));
    jpDetail.add(jlSaldo, new XYConstraints(15, 220, -1, -1));
    jpDetail.add(jlStopa, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jraDatpoc, new XYConstraints(150, 120, 100, -1));
    jpDetail.add(jraDatzav, new XYConstraints(150, 145, 100, -1));
    jpDetail.add(jraGlavnica, new XYConstraints(150, 170, 100, -1));
    jpDetail.add(jraIznos, new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jraPnb1, new XYConstraints(150, 20, 50, -1));
    jpDetail.add(jraPnb2, new XYConstraints(150, 45, 245, -1));
    jpDetail.add(jraRata, new XYConstraints(150, 195, 100, -1));
    jpDetail.add(jraSaldo, new XYConstraints(150, 220, 100, -1));
    jpDetail.add(jraStopa, new XYConstraints(150, 95, 100, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
