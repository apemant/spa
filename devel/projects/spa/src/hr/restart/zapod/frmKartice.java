/****license*****************************************************************
**   file: frmKartice.java
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
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raSifraNaziv;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;



/**

 * Title:        Robno poslovanje

 * Description:

 * Copyright:    Copyright (c) 2000

 * Company:      REST-ART

 * @author REST-ART development team

 * @version 1.0

 */



public class frmKartice extends raSifraNaziv {

  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");

  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();

  JPanel jp = new JPanel();

  XYLayout xYLayout1 = new XYLayout();

  JLabel jLabel4 = new JLabel();

  JLabel jLabel3 = new JLabel();

  JraTextField jtfMAX_IZNOS = new JraTextField();

  JraTextField jtfPROVIZIJA = new JraTextField();

  JLabel jLabel2 = new JLabel();

  JLabel jLabel1 = new JLabel();

  JraTextField jtfMAX_RATA = new JraTextField();

  JraTextField jtfMIN_IZNOS = new JraTextField();

  dM dm;

  JlrNavField jrfVRDOK = new JlrNavField();
  JLabel jlVRDOK = new JLabel();
  JraButton jbVRDOK = new JraButton();
  JlrNavField jrfNAZDOK = new JlrNavField();



  public frmKartice() {

    try {

      jbInit();

    }

    catch(Exception e) {

      e.printStackTrace();

    }

  }

  private void jbInit() throws Exception {

    dm = hr.restart.baza.dM.getDataModule();

    this.setRaDataSet(dm.getAllKartice());

    this.setRaColumnSifra("CBANKA");

    this.setRaColumnNaziv("NAZIV");

    this.setRaText("Kartice");

    jtfMIN_IZNOS.setDataSet(getRaDataSet());

    jtfMIN_IZNOS.setColumnName("MIN_IZNOS");

    jtfMAX_RATA.setDataSet(getRaDataSet());

    jtfMAX_RATA.setColumnName("MAX_RATA");

    jLabel1.setText("Minimalni iznos");

    jLabel2.setText("Maksimalni iznos");

    jtfPROVIZIJA.setColumnName("PROVIZIJA");

    jtfPROVIZIJA.setDataSet(getRaDataSet());

    jtfMAX_IZNOS.setDataSet(getRaDataSet());

    jtfMAX_IZNOS.setColumnName("MAX_IZNOS");

    jLabel3.setText("Maksimalni broj rata");

    jLabel4.setText("Provizija (%)");

    jp.setLayout(xYLayout1);

    xYLayout1.setWidth(580);

    xYLayout1.setHeight(115);

    jlVRDOK.setText("Vrsta dokumenta");
    jrfVRDOK.setColumnName("VRDOK");
    jrfVRDOK.setTextFields(new JTextComponent[] {jrfNAZDOK});
    jrfVRDOK.setColNames(new String[] {"NAZDOK"});
    jrfVRDOK.setSearchMode(3);
    jrfVRDOK.setDataSet(getRaDataSet());
    jrfVRDOK.setRaDataSet(dm.getVrdokum());
	jrfVRDOK.setVisCols(new int[] {0,1});
    jrfVRDOK.setNavButton(jbVRDOK);
    jrfNAZDOK.setColumnName("NAZDOK");
    jrfVRDOK.setNavProperties(jrfVRDOK);
    jrfVRDOK.setSearchMode(1);

    jp.add(jLabel1, new XYConstraints(15, 0, -1, -1));

    jp.add(jLabel3, new XYConstraints(15, 25, -1, -1));

    jp.add(jtfMAX_IZNOS, new XYConstraints(440, 0, 100, -1));

    jp.add(jLabel2, new XYConstraints(300, 0, -1, -1));

    jp.add(jtfMAX_RATA, new XYConstraints(150, 25, 100, -1));

    jp.add(jtfMIN_IZNOS, new XYConstraints(150, 0, 100, -1));

    jp.add(jLabel4, new XYConstraints(15, 50, -1, -1));

    jp.add(jtfPROVIZIJA, new XYConstraints(150, 50, 100, -1));

    jp.add(jrfVRDOK,   new XYConstraints(150, 75, 100, -1));
    jp.add(jlVRDOK,  new XYConstraints(15, 75, -1, -1));
    jp.add(jbVRDOK,   new XYConstraints(544, 75, 21, 21));
    jp.add(jrfNAZDOK,  new XYConstraints(260, 75, 280, -1));

    this.jpRoot.add(jp,java.awt.BorderLayout.SOUTH);

  }

  public boolean DeleteCheck() {

    return util.isDeleteable("RATE", "CBANKA", getRaDataSet().getString("CBANKA"), util.MOD_STR);

  }
  public void SetFokus(char mode) {
    super.SetFokus(mode);
    if (mode == 'N') {
  		jrfNAZDOK.setText("");
    }
  }


}