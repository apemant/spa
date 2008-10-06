/****license*****************************************************************
**   file: jpPilotParam.java
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

import hr.restart.swing.JraTextField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpPilotParam extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlIme = new JLabel();
  JLabel jlTip = new JLabel();
  JraTextField jraIme = new JraTextField();
  JraTextField jraOpis = new JraTextField();

  public jpPilotParam() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void prepareEdit(boolean isNew) {
    rcc.setLabelLaF(jraIme, isNew);
//    jraOpis.requestFocus();
  }
  
  public void setFocus(boolean isNew) {
    if (isNew) jraIme.requestFocusLater();
    else jraOpis.requestFocusLater();
  }

  public void BindComponents(DataSet ds) {
    jraIme.setDataSet(ds);
    jraOpis.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(640);
    lay.setHeight(60);

    jlIme.setText("Parametar");
    jraIme.setColumnName("IME");
    jraOpis.setColumnName("OPIS");

    jpDetail.add(jlIme, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jraIme, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jraOpis, new XYConstraints(255, 20, 370, -1));

    /**@todo: Odkomentirati sljedeæu liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
