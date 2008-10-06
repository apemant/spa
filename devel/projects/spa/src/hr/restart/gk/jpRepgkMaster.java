/****license*****************************************************************
**   file: jpRepgkMaster.java
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
package hr.restart.gk;

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpRepgkMaster extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmRepgk fRepgk;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCrepgk = new JLabel();
  JLabel jlHeader1 = new JLabel();
  JLabel jlTitle = new JLabel();
  JLabel jlaHeader1 = new JLabel();
  JLabel jlaHeader2 = new JLabel();
  JLabel jlaHeader3 = new JLabel();
  JraTextField jraCrepgk = new JraTextField();
  JraTextField jraHeader1 = new JraTextField();
  JraTextField jraHeader2 = new JraTextField();
  JraTextField jraHeader3 = new JraTextField();
  JraTextField jraTitle = new JraTextField();

  public jpRepgkMaster(frmRepgk md) {
    try {
      fRepgk = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jraCrepgk.setDataSet(ds);
    jraHeader1.setDataSet(ds);
    jraHeader2.setDataSet(ds);
    jraHeader3.setDataSet(ds);
    jraTitle.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(765);
    lay.setHeight(130);

    jlCrepgk.setText("Oznaka izvještaja");
    jlHeader1.setText("Zaglavlja");
    jlTitle.setText("Naslov");
    jlaHeader1.setHorizontalAlignment(SwingConstants.CENTER);
    jlaHeader1.setText("Kolona 1");
    jlaHeader2.setHorizontalAlignment(SwingConstants.CENTER);
    jlaHeader2.setText("Kolona 2");
    jlaHeader3.setHorizontalAlignment(SwingConstants.CENTER);
    jlaHeader3.setText("Kolona 3");
    jraCrepgk.setColumnName("CREPGK");
    jraHeader1.setColumnName("HEADER1");
    jraHeader2.setColumnName("HEADER2");
    jraHeader3.setColumnName("HEADER3");
    jraTitle.setColumnName("TITLE");

    jpDetail.add(jlCrepgk, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlHeader1, new XYConstraints(15, 90, -1, -1));
    jpDetail.add(jlTitle, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlaHeader1, new XYConstraints(151, 73, 195, -1));
    jpDetail.add(jlaHeader2, new XYConstraints(353, 73, 195, -1));
    jpDetail.add(jlaHeader3, new XYConstraints(555, 73, 194, -1));
    jpDetail.add(jraCrepgk, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jraHeader1, new XYConstraints(150, 90, 197, -1));
    jpDetail.add(jraHeader2, new XYConstraints(352, 90, 197, -1));
    jpDetail.add(jraHeader3, new XYConstraints(554, 90, 196, -1));
    jpDetail.add(jraTitle, new XYConstraints(150, 45, 600, -1));

    BindComponents(fRepgk.getMasterSet());
    /**@todo: Odkomentirati sljedeæu liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
