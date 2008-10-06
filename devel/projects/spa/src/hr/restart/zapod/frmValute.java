/****license*****************************************************************
**   file: frmValute.java
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
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
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

public class frmValute extends raMatPodaci {
  static frmValute fvalute;
  Valid vl = Valid.getValid();
  JPanel jp = new JPanel();
  raCommonClass rcc = new raCommonClass();
  XYLayout xYLayout1 = new XYLayout();
  dM dm;
  JLabel jlCVALUTE = new JLabel();
  JraTextField jtCVALUTE = new JraTextField();
  JLabel jlOZNAKA = new JLabel();
  JraTextField jtOZNAKA = new JraTextField();
  JLabel jlNAZIV = new JLabel();
  JraTextField jtNAZIV = new JraTextField();
  JLabel jlJEDINICA = new JLabel();
  JraTextField jtJEDINICA = new JraTextField();
  JraCheckBox jcbSTRVAL = new JraCheckBox();
  JraCheckBox jcbAktivan = new JraCheckBox();
  JraCheckBox jcbREFVAL = new JraCheckBox();
  JLabel jlNIZAJEDINICA = new JLabel();
  JraTextField jtNIZAJEDINICA = new JraTextField();

  public frmValute() {
    try {
      jbInit();
      fvalute = this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dm = dM.getDataModule();
    this.setRaDetailPanel(jp);
    this.setRaQueryDataSet(dm.getAllValute());
    this.setVisibleCols(new int[] {0,1,2,3,4});

    jlCVALUTE.setText("Šifra");
    jp.setLayout(xYLayout1);
    jtCVALUTE.setToolTipText("");
    jtCVALUTE.setColumnName("CVAL");
    jtCVALUTE.setDataSet(getRaQueryDataSet());
    jlOZNAKA.setText("Oznaka");
    jtOZNAKA.setDataSet(getRaQueryDataSet());
    jtOZNAKA.setColumnName("OZNVAL");
    jtOZNAKA.setToolTipText("");
    jlNAZIV.setText("Naziv");
    jtNAZIV.setColumnName("NAZVAL");
    jtNAZIV.setDataSet(getRaQueryDataSet());
    xYLayout1.setWidth(465);
    xYLayout1.setHeight(159);
    jlJEDINICA.setText("Jedinica");
    jtJEDINICA.setColumnName("JEDVAL");
    jtJEDINICA.setDataSet(getRaQueryDataSet());
    jcbSTRVAL.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbSTRVAL.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbSTRVAL.setText("Strana valuta");
    jcbSTRVAL.setColumnName("STRVAL");
    jcbSTRVAL.setDataSet(getRaQueryDataSet());
    jcbSTRVAL.setSelectedDataValue("D");
    jcbSTRVAL.setUnselectedDataValue("N");

    jcbAktivan.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktivan.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktivan.setSelected(true);
    jcbAktivan.setText("Aktivan");
    jcbAktivan.setColumnName("AKTIV");
    jcbAktivan.setDataSet(getRaQueryDataSet());
    jcbAktivan.setSelectedDataValue("D");
    jcbAktivan.setUnselectedDataValue("N");
    jcbREFVAL.setUnselectedDataValue("N");
    jcbREFVAL.setSelectedDataValue("D");
    jcbREFVAL.setDataSet(getRaQueryDataSet());
    jcbREFVAL.setColumnName("REFVAL");
    jcbREFVAL.setText("Referentna valuta");
    jcbREFVAL.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbREFVAL.setHorizontalAlignment(SwingConstants.RIGHT);
    jlNIZAJEDINICA.setText("Niža jedinica");
    jtNIZAJEDINICA.setToolTipText("");
    jtNIZAJEDINICA.setColumnName("NIZJED");
    jtNIZAJEDINICA.setDataSet(getRaQueryDataSet());
    jp.add(jlCVALUTE, new XYConstraints(15, 20, -1, -1));
    jp.add(jtCVALUTE, new XYConstraints(150, 20, 45, -1));
    jp.add(jlOZNAKA, new XYConstraints(15, 45, -1, -1));
    jp.add(jtOZNAKA, new XYConstraints(150, 45, 45, -1));
    jp.add(jcbAktivan, new XYConstraints(200, 20, 250, 21));
    jp.add(jtNAZIV, new XYConstraints(149, 95, 300, -1));
    jp.add(jlNAZIV, new XYConstraints(14, 95, -1, -1));
    jp.add(jtJEDINICA, new XYConstraints(150, 70, 45, -1));
    jp.add(jlJEDINICA, new XYConstraints(15, 70, -1, -1));
    jp.add(jcbSTRVAL, new XYConstraints(200, 69, 250, 21));
    jp.add(jcbREFVAL, new XYConstraints(200, 45, 250, 21));
    jp.add(jlNIZAJEDINICA, new XYConstraints(15, 120, -1, -1));
    jp.add(jtNIZAJEDINICA, new XYConstraints(150, 120, 300, -1));
  }
  public static frmValute getFrmValute() {
    if (fvalute==null) fvalute= new frmValute();

    return fvalute;
  }
  public boolean Validacija(char mode) {
    if (mode=='N') {
      if (vl.notUnique(jtCVALUTE)) return false;
      if (vl.notUnique(jtOZNAKA)) return false;
    } else {
      if (vl.notUniqueUPD(jtOZNAKA,new String[] {"CVAL"})) return false;
    }
    if (vl.isEmpty(jtNAZIV)) return false;
    if (vl.isEmpty(jtJEDINICA)) return false;
    return true;
  }

//  public void SetFokus(char mode) {
//    if (mode=='N') {
//      jtCVALUTE.setEnabled(true);
//      jtCVALUTE.requestFocus();
//    } else {
//      jtCVALUTE.setEnabled(false);
//      jtOZNAKA.requestFocus();
//    }
//  }

  public void SetFokus(char mode) {
  if (mode=='N') {
    rcc.setLabelLaF(jtCVALUTE, true);
//    jtCVALUTE.setEnabled(true);
    jtCVALUTE.requestFocus();
  }
  if(mode=='I') {
    rcc.setLabelLaF(jtCVALUTE, false);
//    jtCVALUTE.setEnabled(false);
    jtOZNAKA.requestFocus();
  }
}

}