/****license*****************************************************************
**   file: frmFranka.java
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

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.sisfun.raDataIntegrity;
import hr.restart.swing.JraTextField;
import hr.restart.util.raSifraNaziv;

/**
 * Title:        Robno poslovanje
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      REST-ART
 * @author REST-ART development team
 * @version 1.0
 */

public class frmFranka extends raSifraNaziv {
  hr.restart.baza.dM dm;
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  
  JPanel jPanel1 = new JPanel();
  JraTextField jraSNAZ = new JraTextField();

  public frmFranka() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    this.setRaDataSet(dm.getFranka());
    this.setRaColumnSifra("CFRA");
    this.setRaColumnNaziv("NAZFRA");
    this.setRaText("Paritet");
    jPanel1.setLayout(new XYLayout(560, 40));
    jraSNAZ.setDataSet(getRaDataSet());
    jraSNAZ.setColumnName("SNAZFRA");
    jPanel1.add(new JLabel("Strani naziv"), new XYConstraints(15, 0, -1, -1));
    jPanel1.add(jraSNAZ, new XYConstraints(255, 0, 285, -1));

    this.jpRoot.add(jPanel1, BorderLayout.SOUTH);
    raDataIntegrity.installFor(this);
  }
}