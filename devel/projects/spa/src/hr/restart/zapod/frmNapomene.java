/****license*****************************************************************
**   file: frmNapomene.java
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

import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextField;
import hr.restart.util.raMatPodaci;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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

public class frmNapomene extends raMatPodaci {
//  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
//  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.baza.dM dm;
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlCNAP = new JLabel();
  JLabel jlNAZNAP = new JLabel();
  JraTextField jtfCNAP = new JraTextField();
  JraCheckBox jcbAKTIV = new JraCheckBox();
  hr.restart.swing.JraTextArea jtaNAZNAP = new hr.restart.swing.JraTextArea();
  JraScrollPane jScrollPane1 = new JraScrollPane();

  public frmNapomene() {
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
    this.setRaDetailPanel(jp);
    this.setRaQueryDataSet(dm.getAllNapomene());
    jcbAKTIV.setSelected(true);
    jcbAKTIV.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbAKTIV.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAKTIV.setDataSet(getRaQueryDataSet());
    jcbAKTIV.setColumnName("AKTIV");
    jcbAKTIV.setSelectedDataValue("D");
    jcbAKTIV.setUnselectedDataValue("N");
    jcbAKTIV.setText("Aktivan");
    jtfCNAP.setDataSet(getRaQueryDataSet());
    jtfCNAP.setColumnName("CNAP");
    jlNAZNAP.setText("Naziv");
    jlCNAP.setText("Oznaka");
    xYLayout1.setWidth(555);
    xYLayout1.setHeight(142);
    jp.setLayout(xYLayout1);
    jtaNAZNAP.setBorder(null);
    jtaNAZNAP.setColumnName("NAZNAP");
    jtaNAZNAP.setDataSet(getRaQueryDataSet());
    jp.add(jlCNAP, new XYConstraints(15, 20, -1, -1));
    jp.add(jtfCNAP, new XYConstraints(150, 20, 100, -1));
    jp.add(jlNAZNAP, new XYConstraints(15, 45, -1, -1));
    jScrollPane1.getViewport().add(jtaNAZNAP, null);
    jp.add(jScrollPane1,   new XYConstraints(150, 45, 391, 80));
    jp.add(jcbAKTIV, new XYConstraints(440, 20, 100, -1));

  }
  public boolean Validacija(char mode) {
    if (mode=='N') {
      if (hr.restart.util.Valid.getValid().notUnique(jtfCNAP)) {
        return false;
      }
    }
    if (hr.restart.util.Valid.getValid().isEmpty(jtaNAZNAP)) {
      return false;
    }
    return true;
  }

  public void SetFokus(char mode) {
    if (mode=='N') {
      rcc.setLabelLaF(jtfCNAP,true);
      jtfCNAP.requestFocus();
    }
    else if (mode=='I') {
      rcc.setLabelLaF(jtfCNAP,false);
      jtaNAZNAP.requestFocus();
    }
  }
/*
  public boolean DeleteCheck() {
    return util.isDeleteable("DOKI", "CNAP", dm.getNapomene().getString("CNAP"), util.MOD_STR);
  }
*/
  public void EntryPoint(char mode) {
  }


}