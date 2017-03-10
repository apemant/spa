/****license*****************************************************************
**   file: frmNamjena.java
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
package hr.restart.robno;

import hr.restart.sisfun.raDataIntegrity;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.raSifraNaziv;

import javax.swing.JLabel;
import javax.swing.JPanel;

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

public class frmNamjena extends raSifraNaziv {
  hr.restart.baza.dM dm;
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  private JPanel jp = new JPanel();
  private JraCheckBox jcbPorez = new JraCheckBox();
  
  JraTextField jraSNAZ = new JraTextField();

  public frmNamjena() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    jcbPorez.setColumnName("POREZ");
    jcbPorez.setDataSet(dm.getNamjena());
    jcbPorez.setText("Obra�un poreza");
    jcbPorez.setUnselectedDataValue("N");
    jcbPorez.setSelectedDataValue("D");
    jp.setLayout(new XYLayout(580, 70));
    this.setRaDataSet(dm.getNamjena());
    this.setRaColumnSifra("CNAMJ");
    this.setRaColumnNaziv("NAZNAMJ");
    this.setRaText("Namjena");
    jraSNAZ.setDataSet(getRaDataSet());
    jraSNAZ.setColumnName("SNAZNAMJ");
    jp.add(new JLabel("Strani naziv"), new XYConstraints(15, 0, -1, -1));
    jp.add(jraSNAZ, new XYConstraints(255, 0, 285, -1));
    jp.add(jcbPorez,  new XYConstraints(15, 30, -1, -1));
    this.jpRoot.add(jp,java.awt.BorderLayout.SOUTH);
    raDataIntegrity.installFor(this);
  }
}