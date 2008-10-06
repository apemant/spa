/****license*****************************************************************
**   file: jpRepgkDetail.java
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

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpRepgkDetail extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmRepgk fRepgk;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlDescription = new JLabel();
  JLabel jlKto1 = new JLabel();
  JLabel jlKto2 = new JLabel();
  JLabel jlKto3 = new JLabel();
  JLabel jlRbsrepgk = new JLabel();
  JraTextField jraDescription = new JraTextField();
  JraTextField jraKto1 = new JraTextField();
  JraTextField jraKto2 = new JraTextField();
  JraTextField jraKto3 = new JraTextField();
  JraTextField jraRbsrepgk = new JraTextField();

  public jpRepgkDetail(frmRepgk md) {
    try {
      fRepgk = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jraDescription.setDataSet(ds);
    jraKto1.setDataSet(ds);
    jraKto2.setDataSet(ds);
    jraKto3.setDataSet(ds);
    jraRbsrepgk.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(565);
    lay.setHeight(160);

    jlDescription.setText("Opis");
    jlKto1.setText("Definicija 1. iznosa");
    jlKto2.setText("Definicija 2. iznosa");
    jlKto3.setText("Definicija 3. iznosa");
    jlRbsrepgk.setText("Rbr");
    jraDescription.setColumnName("DESCRIPTION");
    jraKto1.setColumnName("KTO1");
    jraKto2.setColumnName("KTO2");
    jraKto3.setColumnName("KTO3");
    jraRbsrepgk.setColumnName("RBSREPGK");

    jpDetail.add(jlDescription, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlKto1, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlKto2, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jlKto3, new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jlRbsrepgk, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jraDescription, new XYConstraints(150, 45, 400, -1));
    jpDetail.add(jraKto1, new XYConstraints(150, 70, 400, -1));
    jpDetail.add(jraKto2, new XYConstraints(150, 95, 400, -1));
    jpDetail.add(jraKto3, new XYConstraints(150, 120, 400, -1));
    jpDetail.add(jraRbsrepgk, new XYConstraints(150, 20, 100, -1));

    BindComponents(fRepgk.getDetailSet());
    /**@todo: Odkomentirati sljedeæu liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
