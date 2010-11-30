/****license*****************************************************************
**   file: jpZnacRad.java
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
package hr.restart.pl;

import hr.restart.baza.dM;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpZnacRad extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  XYLayout lay = new XYLayout();
  
  frmZnacRad fZnacRad;
  JPanel jpDetail = new JPanel();
  JLabel jlZnac = new JLabel();
  JLabel jlDohattr = new JLabel();
  JLabel jlDohcols = new JLabel();
  
  
  JraTextField jraZnac = new JraTextField();
  JraTextField jraNaznac = new JraTextField();
  JLabel jlTip = new JLabel();
  raComboBox jcbTip = new raComboBox();
  JraCheckBox jcbObavez = new JraCheckBox();
  JraCheckBox jcbParam = new JraCheckBox();
  JraCheckBox jcbAktiv = new JraCheckBox();
  JraTextField jraDohattr = new JraTextField();
  JraTextField jraDohcols = new JraTextField();
  JraTextField jraSrt = new JraTextField();

  public jpZnacRad(frmZnacRad f) {
    try {
      fZnacRad = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jraZnac.setDataSet(ds);
    jcbTip.setRaDataSet(ds);
    jraDohattr.setDataSet(ds);
    jraDohcols.setDataSet(ds);
    jraSrt.setDataSet(ds);
    jcbParam.setDataSet(ds);
    jcbAktiv.setDataSet(ds);
    jraNaznac.setDataSet(ds);
    jcbObavez.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(555);
    lay.setHeight(155);

    jlZnac.setText("Podatak");
    jlDohattr.setText("Atributi dohvata");
    jlDohcols.setText("Kolone dohvata");
    
    jcbTip.setRaColumn("ZNACTIP");
    jcbTip.setRaItems(new String[][] {{"Tekstualni", "S"}, {"Datum", "D"}, {"Cjelobrojni", "I"},
                                      {"Decimalni, 2 mjesta", "2"}, {"Decimalni, 3 mjesta", "3"}});

    jraZnac.setColumnName("CZNAC");
    jraDohattr.setColumnName("DOHATTR");
    jraDohcols.setColumnName("DOHCOLS");
    jraSrt.setColumnName("SRT");
    jcbParam.setColumnName("ZNACDOH");
    jcbParam.setSelectedDataValue("D");
    jcbParam.setUnselectedDataValue("N");
    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setUnselectedDataValue("N");
    jraNaznac.setColumnName("ZNACOPIS");
    jcbObavez.setColumnName("ZNACREQ");
    jcbObavez.setSelectedDataValue("D");
    jcbObavez.setUnselectedDataValue("N");
    
    jcbObavez.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbObavez.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbObavez.setText("Obavezan unos  ");
    jcbParam.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbParam.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbParam.setText("Unos iz popisa  ");
    jcbAktiv.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setText("Aktivan ");

    jpDetail.add(jlZnac, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jraZnac, new XYConstraints(150, 20, 75, -1));
    jpDetail.add(jraNaznac, new XYConstraints(230, 20, 310, -1));
    jpDetail.add(jlTip, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jcbTip, new XYConstraints(150, 45, 140, -1));
    jpDetail.add(jcbObavez, new XYConstraints(420, 45, 120, -1));
    jpDetail.add(jcbParam, new XYConstraints(300, 45, 115, -1));
    
    jpDetail.add(jlDohattr, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlDohcols, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(new JLabel("Slijed"), new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jraDohattr, new XYConstraints(150, 70, 390, -1));
    jpDetail.add(jraDohcols, new XYConstraints(150, 95, 390, -1));
    jpDetail.add(jraSrt, new XYConstraints(150, 120, 140, -1));
    jpDetail.add(jcbAktiv, new XYConstraints(300, 120, 240, -1));

    /**@todo: Odkomentirati sljede?u liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}  
