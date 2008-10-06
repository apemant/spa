/****license*****************************************************************
**   file: jpPilotArg.java
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
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpPilotArg extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlGet = new JLabel();
  JLabel jlIme = new JLabel();
  JLabel jlTip = new JLabel();
  JLabel jlKol = new JLabel();
  JLabel jlVis = new JLabel();
  JLabel jlDef = new JLabel();
  JLabel jlWid = new JLabel();

  JraTextField jraWid = new JraTextField() {
    public boolean maskCheck() {
      if (!super.maskCheck()) return false;
      return checkLimits();
    }
  };
  JraTextField jraDef = new JraTextField();
  JraTextField jraIme = new JraTextField();
  JraTextField jraOpis = new JraTextField();
  raComboBox rcbTip = new raComboBox() {
    public void this_itemStateChanged() {
      super.this_itemStateChanged();
      checkTip();
    }
  };
//  raComboBox rcbGet = new raComboBox();
  JraTextField jraGet = new JraTextField();
  JraTextField jraKol = new JraTextField();
  JraTextField jraVis = new JraTextField();


  public jpPilotArg() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public void setFocus(boolean isNew) {
    if (isNew) jraIme.requestFocusLater();
    else jraOpis.requestFocusLater();
  }

  public void prepareEdit(boolean isNew) {
    rcc.setLabelLaF(jraIme, isNew);
    checkTip();
//    jraOpis.requestFocus();
  }

  public void BindComponents(DataSet ds) {
    jraGet.setDataSet(ds);
//    rcbGet.setDataSet(ds);
    jraIme.setDataSet(ds);
    jraOpis.setDataSet(ds);
    rcbTip.setRaDataSet(ds);
    jraKol.setDataSet(ds);
    jraVis.setDataSet(ds);
    jraWid.setDataSet(ds);
    jraDef.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(640);
    lay.setHeight(125);

    jlIme.setText("Argument");
    jlTip.setText("Tip");
    jlGet.setText("Metoda za dohvat");
    jlKol.setText("Polja za dohvat");
    jlVis.setText("Kolone dohvata");
    jlDef.setText("Default");
    jlWid.setText("Širina");

    jraIme.setColumnName("IME");
    jraOpis.setColumnName("OPIS");
    jraGet.setColumnName("GET");
    jraWid.setColumnName("WIDTH");
    jraDef.setColumnName("DEFAULT");

    rcbTip.setRaColumn("TIP");
    rcbTip.setRaItems(new String[][] {{"Tekstualni", "S"}, {"Cjelobrojni", "I"},
        {"Decimalni, 2 mjesta", "2"}, {"Decimalni, 3 mjesta", "3"},
        {"Po\u010Detni datum", "F"}, {"Krajnji datum", "T"}
    });

//    String[] getters = hr.restart.baza.dM.getDataModule().getAllDataSetGetters();
//    String[][] dget = new String[getters.length][2];
//    for (int i = 0; i < getters.length; i++)
//      dget[i][0] = dget[i][1] = getters[i];
//    rcbGet.setRaColumn("GET");
//    rcbGet.setRaItems(dget);

    jraKol.setColumnName("KOLONE");
    jraVis.setColumnName("VISKOL");

    jpDetail.add(jlGet, new XYConstraints(390, 45, -1, -1));
    jpDetail.add(jlIme, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlTip, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlKol, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlVis, new XYConstraints(390, 70, -1, -1));
    jpDetail.add(jlWid, new XYConstraints(390, 95, -1, -1));

    jpDetail.add(jraGet, new XYConstraints(505, 45, 120, -1));
//    jpDetail.add(rcbGet, new XYConstraints(505, 45, 120, -1));
    jpDetail.add(jraIme, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jraOpis, new XYConstraints(255, 20, 370, -1));
    jpDetail.add(rcbTip, new XYConstraints(150, 45, 205, -1));
    jpDetail.add(jraWid, new XYConstraints(505, 95, 120, -1));
    jpDetail.add(jraKol, new XYConstraints(150, 70, 205, -1));
    jpDetail.add(jraVis, new XYConstraints(505, 70, 120, -1));
    jpDetail.add(jlDef, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jraDef, new XYConstraints(150, 95, 205, -1));
    /**@todo: Odkomentirati sljedeæu liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }

  private void checkTip() {
    int idx = rcbTip.getSelectedIndex();
    if (idx == 0 || idx == 1) {
      rcc.setLabelLaF(jraWid, true);
      rcc.setLabelLaF(jraKol, true);
      rcc.setLabelLaF(jraVis, true);
      rcc.setLabelLaF(jraGet, true);
      checkLimits();
    } else {
      jraWid.setText("");
      jraKol.setText("");
      jraVis.setText("");
      jraGet.setText("");
      rcc.setLabelLaF(jraWid, false);
      rcc.setLabelLaF(jraKol, false);
      rcc.setLabelLaF(jraVis, false);
      rcc.setLabelLaF(jraGet, false);
    }
  }

  public boolean checkLimits() {
    int idx = rcbTip.getSelectedIndex();
    int wid = jraWid.getDataSet().getInt("WIDTH");
    if (idx == 0) {
      if (wid == 0) jraWid.getDataSet().setInt("WIDTH", 12);
      else if (wid < 1) jraWid.getDataSet().setInt("WIDTH", 1);
      else if (wid > 100) jraWid.getDataSet().setInt("WIDTH", 100);
      else return true;
      return false;
    } else if (idx == 1) {
      if (wid == 0) jraWid.getDataSet().setInt("WIDTH", 5);
      else if (wid < 1) jraWid.getDataSet().setInt("WIDTH", 1);
      else if (wid > 9) jraWid.getDataSet().setInt("WIDTH", 9);
      else return true;
      return false;
    }
    return true;
  }
}
