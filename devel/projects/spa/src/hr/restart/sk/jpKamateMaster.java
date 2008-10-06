/****license*****************************************************************
**   file: jpKamateMaster.java
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
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpKamateMaster extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmKamate fKamate;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCkam = new JLabel();
  JraTextField jraCkam = new JraTextField();
  JraTextField jraOpis = new JraTextField();

  public jpKamateMaster(frmKamate md) {
    try {
      fKamate = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jraCkam.setDataSet(ds);
    jraOpis.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(565);
    lay.setHeight(60);

    jlCkam.setText("Tablica kamata");
    jraCkam.setColumnName("CKAM");
    jraOpis.setColumnName("OPIS");

    jpDetail.add(jlCkam, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jraCkam, new XYConstraints(150, 20, 75, -1));
    jpDetail.add(jraOpis, new XYConstraints(230, 20, 320, -1));

    BindComponents(fKamate.getMasterSet());
    /**@todo: Odkomentirati sljedeæu liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
