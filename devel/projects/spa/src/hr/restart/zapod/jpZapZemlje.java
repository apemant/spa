/****license*****************************************************************
**   file: jpZapZemlje.java
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
package hr.restart.zapod;

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpZapZemlje extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmZapZemlje fZapZemlje;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCzem = new JLabel();
  JLabel jlOznzem = new JLabel();
  JLabel jlTrgzem = new JLabel();
  JLabel jlaCzem = new JLabel();
  JLabel jlaNazivzem = new JLabel();
  JraTextField jraCzem = new JraTextField();
  JraTextField jraNazivzem = new JraTextField();
  JraTextField jraOznzem = new JraTextField();
  JraTextField jraTrgzem = new JraTextField();

  public jpZapZemlje(frmZapZemlje f) {
    try {
      fZapZemlje = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jraCzem.setDataSet(ds);
    jraNazivzem.setDataSet(ds);
    jraOznzem.setDataSet(ds);
    jraTrgzem.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(570);
    lay.setHeight(95);

    jlCzem.setText("Zemlja");
    jlOznzem.setText("Oznaka");
    jlTrgzem.setText("Trgova\u010Dka oznaka");
    jlaCzem.setHorizontalAlignment(SwingConstants.LEADING);
    jlaCzem.setText("Šifra");
    jlaNazivzem.setHorizontalAlignment(SwingConstants.LEADING);
    jlaNazivzem.setText("Naziv");
    jraCzem.setColumnName("CZEM");
    jraNazivzem.setColumnName("NAZIVZEM");
    jraOznzem.setColumnName("OZNZEM");
    jraTrgzem.setColumnName("TRGZEM");

    jpDetail.add(jlCzem, new XYConstraints(15, 30, -1, -1));
    jpDetail.add(jlOznzem, new XYConstraints(15, 55, -1, -1));
    jpDetail.add(jlTrgzem, new XYConstraints(355, 57, -1, -1));
    jpDetail.add(jlaCzem, new XYConstraints(151, 13, 73, -1));
    jpDetail.add(jlaNazivzem, new XYConstraints(231, 13, 323, -1));
    jpDetail.add(jraCzem, new XYConstraints(150, 30, 75, -1));
    jpDetail.add(jraNazivzem, new XYConstraints(230, 30, 325, -1));
    jpDetail.add(jraOznzem, new XYConstraints(150, 55, 75, -1));
    jpDetail.add(jraTrgzem, new XYConstraints(480, 55, 75, -1));

    /**@todo: Odkomentirati sljedeæu liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
