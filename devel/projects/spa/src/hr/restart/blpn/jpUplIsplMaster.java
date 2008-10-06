/****license*****************************************************************
**   file: jpUplIsplMaster.java
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
package hr.restart.blpn;

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpUplIsplMaster extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  raMasterDetail mainClass;

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlDatod = new JLabel();
  JraTextField jraDatod = new JraTextField();

  public jpUplIsplMaster(raMasterDetail md) {
    try {
      mainClass = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(265);
    lay.setHeight(60);

    jlDatod.setText("Datum izvješ\u0107a");
    jraDatod.setColumnName("DATOD");
    if (mainClass instanceof frmUplIspl) jraDatod.setDataSet(((frmUplIspl)mainClass).getMasterSet());
    else jraDatod.setDataSet(((frmBlagIzv)mainClass).getMasterSet());
    jraDatod.setHorizontalAlignment(SwingConstants.CENTER);

    jpDetail.add(jlDatod, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jraDatod, new XYConstraints(150, 20, 100, -1));
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
