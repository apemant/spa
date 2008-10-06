/****license*****************************************************************
**   file: frmApl.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raMatPodaci;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class frmApl extends raMatPodaci {
  Valid vl = Valid.getValid();
  JPanel jp = new JPanel();
  XYLayout xYLay = new XYLayout();
  JLabel jlAPP = new JLabel();
  JraTextField jrAPP = new JraTextField();
  dM dm;
  JLabel jlKLASA = new JLabel();
  JraTextField jrKLASA = new JraTextField();
  JLabel jlOPIS = new JLabel();
  JraTextField jrOPIS = new JraTextField();
  JraCheckBox jrcbINSTALIRANA = new JraCheckBox();

  public frmApl() {
    super(2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    jlAPP.setText("Aplikacija");
    jrAPP.setColumnName("APP");
    jrAPP.setDataSet(dm.getAplikacija());
    jp.setLayout(xYLay);
    jlKLASA.setText("Klasa");
    jrKLASA.setColumnName("KLASA");
    jrKLASA.setDataSet(dm.getAplikacija());
    xYLay.setWidth(464);
    xYLay.setHeight(109);
    jlOPIS.setText("Opis / Naziv");
    jrOPIS.setDataSet(dm.getAplikacija());
    jrOPIS.setColumnName("OPIS");
    jrcbINSTALIRANA.setHorizontalAlignment(SwingConstants.RIGHT);
    jrcbINSTALIRANA.setHorizontalTextPosition(SwingConstants.LEADING);
    jrcbINSTALIRANA.setText("Instalirana");
    jrcbINSTALIRANA.setColumnName("INSTALIRANA");
    jrcbINSTALIRANA.setDataSet(dm.getAplikacija());
    jrcbINSTALIRANA.setSelectedDataValue("D");
    jrcbINSTALIRANA.setUnselectedDataValue("N");
    jp.add(jlAPP, new XYConstraints(15, 20, -1, -1));
    jp.add(jrAPP, new XYConstraints(150, 20, 80, -1));
    jp.add(jlKLASA, new XYConstraints(15, 45, -1, -1));
    jp.add(jrKLASA, new XYConstraints(150, 45, 250, -1));
    jp.add(jlOPIS, new XYConstraints(15, 70, -1, -1));
    jp.add(jrOPIS, new XYConstraints(150, 70, 300, -1));
    jp.add(jrcbINSTALIRANA, new XYConstraints(235, 20, 215, -1));
    this.setRaDetailPanel(jp);
    this.setRaQueryDataSet(dm.getAplikacija());
    this.setVisibleCols(new int[] {0,3});
  }
  public void SetFokus(char mode) {
    if (mode=='N') {
      jrAPP.setEnabled(true);
      jrAPP.requestFocus();
    } else if (mode=='I') {
      jrAPP.setEnabled(false);
      jrKLASA.requestFocus();
    }
  }
  public boolean Validacija(char mode) {
    if (mode=='N') {
      if (vl.notUnique(jrAPP)) return false;
    }
    if (vl.isEmpty(jrKLASA)) return false;
    if (vl.isEmpty(jrOPIS)) return false;
    return true;
  }
}