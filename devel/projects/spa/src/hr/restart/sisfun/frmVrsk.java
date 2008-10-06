/****license*****************************************************************
**   file: frmVrsk.java
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
import hr.restart.baza.Vrdokum;
import hr.restart.baza.Vrshemek;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raSifraNaziv;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.sql.dataset.QueryDataSet;
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

public class frmVrsk extends raSifraNaziv {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  Valid vl = Valid.getValid();

  JraTextField jraApp = new JraTextField();
  private String myApp;
  private QueryDataSet vrsk, vrd;

  JlrNavField jlrVD = new JlrNavField();
  JlrNavField jlrNazVD = new JlrNavField();
  JLabel jlVD = new JLabel();
  JraButton jbVD = new JraButton();

  public frmVrsk(String app) {
    try {
      myApp = app;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @deprecated hehehe
   * @return QueryDataSet.
   */
  public QueryDataSet getVrsk() {
    return vrsk;
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jlrVD.setText("");
      jlrVD.forceFocLost();
    }
    super.SetFokus(mode);
  }

  public boolean Validacija(char mode) {
//    if (!super.Validacija(mode))
//      return false;
    if (vl.isEmpty(jtfCSIFRA)) return false;
    this.getRaDataSet().setString("APP", myApp);
    jraApp.setText(myApp);
    if (mode == 'N' && vl.notUnique(new JTextComponent[] {jraApp, jtfCSIFRA}))
      return false;
    this.getRaDataSet().setString("VRDOK", jlrVD.getText());
    return true;
  }

  private void jbInit() throws Exception {
    vrsk = Vrshemek.getDataModule().getFilteredDataSet("app = '"+myApp+"'");
    vrd = Vrdokum.getDataModule().getFilteredDataSet("app = '"+myApp+"'");
    jraApp.setColumnName("APP");
    jraApp.setDataSet(vrsk);
    this.setRaDataSet(vrsk);
    this.setRaColumnSifra("CVRSK");
    this.setRaColumnNaziv("OPISVRSK");
    this.setRaText("Vrsta sheme");
    this.setVisibleCols(new int[] {0,1,2});

    jlVD.setText("Vrsta dokumenta");
    jbVD.setText("...");

    jlrVD.setColumnName("VRDOK");
    jlrVD.setDataSet(this.getRaDataSet());
    jlrVD.setColNames(new String[] {"NAZDOK"});
    jlrVD.setTextFields(new JTextComponent[] {jlrNazVD});
    jlrVD.setSearchMode(0);
    jlrVD.setRaDataSet(vrd);
    jlrVD.setVisCols(new int[] {0,3});
    jlrVD.setNavButton(jbVD);

    jlrNazVD.setColumnName("NAZDOK");
    jlrNazVD.setNavProperties(jlrVD);
    jlrNazVD.setSearchMode(1);

    JPanel jp = (JPanel) this.getRaDetailPanel().getComponent(0);
    ((XYLayout) jp.getLayout()).setHeight(100);
    ((XYLayout) jp.getLayout()).setWidth(575);

    jp.add(jlVD, new XYConstraints(15, 65, -1, -1));
    jp.add(jlrVD, new XYConstraints(150, 65, 100, -1));
    jp.add(jlrNazVD, new XYConstraints(255, 65, 285, -1));
    jp.add(jbVD, new XYConstraints(545, 65, 21, 21));
  }
}
