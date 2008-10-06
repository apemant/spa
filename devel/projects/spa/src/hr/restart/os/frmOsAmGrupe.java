/****license*****************************************************************
**   file: frmOsAmGrupe.java
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
package hr.restart.os;

import hr.restart.baza.dM;
import hr.restart.robno._Main;
import hr.restart.swing.JraTextField;
import hr.restart.util.raSifraNaziv;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmOsAmGrupe extends raSifraNaziv {

  dM dm;
  _Main main;

  JPanel jp = new JPanel();
  JraTextField jrfZakStopa = new JraTextField();
  JraTextField jrfStopaPoOdl = new JraTextField();
  JLabel jlZakStopa = new JLabel();
  JLabel jlStopaPoOdl = new JLabel();

  XYLayout xYLayout1 = new XYLayout();


  public frmOsAmGrupe()
  {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }
  private void jbInit() throws Exception
  {
    dm = dM.getDataModule();

    jp.setLayout(xYLayout1);
    this.setVisibleCols(new int[] {0,1,2,3});

    this.setRaDataSet(dm.getOS_Amgrupe());

    this.setRaText("Grupa");
    this.setRaColumnSifra("CGRUPE");
    this.setRaColumnNaziv("NAZGRUPE");

    jrfZakStopa.setDataSet(dm.getOS_Amgrupe());
    jrfZakStopa.setColumnName("ZAKSTOPA");

    jrfStopaPoOdl.setDataSet(dm.getOS_Amgrupe());
    jrfStopaPoOdl.setColumnName("ODLSTOPA");

    jlZakStopa.setText("Zakonska stopa");
    jlStopaPoOdl.setText("Stopa po odluci");
    xYLayout1.setWidth(585);
    xYLayout1.setHeight(35);
    jpRoot.add(jp, java.awt.BorderLayout.SOUTH);

    jp.add(jrfZakStopa,  new XYConstraints(150, 0, 100, -1));
    jp.add(jlZakStopa,   new XYConstraints(15, 3, -1, -1));
    jp.add(jrfStopaPoOdl,     new XYConstraints(440, 0, 100, -1));
    jp.add(jlStopaPoOdl,     new XYConstraints(341, 3, -1, -1));
  }
}