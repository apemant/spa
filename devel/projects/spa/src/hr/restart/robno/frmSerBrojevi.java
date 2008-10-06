/****license*****************************************************************
**   file: frmSerBrojevi.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.raMatPodaci;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.jbcl.layout.VerticalFlowLayout;
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

public class frmSerBrojevi extends raMatPodaci {
  JPanel jp = new JPanel();
  JPanel jpFilter = new JPanel();
  dM dm;
  JraTextField jtfCARTsb = new JraTextField();
  JraTextField jtfCSKLsb = new JraTextField();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JraTextField jraTextField1 = new JraTextField();
  JraTextField jraTextField2 = new JraTextField();
  JraTextField jraTextField3 = new JraTextField();
  JraTextField jraTextField4 = new JraTextField();
  JraTextField jraTextField5 = new JraTextField();
  JraTextField jraTextField6 = new JraTextField();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JraTextField jraTextField7 = new JraTextField();
  JraTextField jraTextField8 = new JraTextField();
  JLabel jLabel6 = new JLabel();
  TableDataSet tableDataSet1 = new TableDataSet();
  Column column1 = new Column();
  Column column2 = new Column();
  Column column3 = new Column();
  Column column4 = new Column();

  public frmSerBrojevi() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    jtfCSKLsb.setColumnName("CSKL");
    jtfCARTsb.setColumnName("CART");
    jpFilter.setLayout(verticalFlowLayout1);
    jp.setLayout(xYLayout1);
    jLabel1.setText("Šifra skladišta");
    jLabel2.setText("Šifra artikla");
    jLabel3.setToolTipText("");
    jLabel3.setText("Serijski brojevi od");
    jLabel4.setText("do");
    jLabel5.setText("Pozicija broja\u010Da");
    jLabel6.setText("do");
    xYLayout1.setWidth(594);
    xYLayout1.setHeight(160);
    column1.setColumnName("br1");
    column1.setDataType(com.borland.dx.dataset.Variant.INT);
    column1.setResolvable(false);
    column1.setServerColumnName("NewColumn1");
    column1.setSqlType(0);
    column2.setColumnName("br2");
    column2.setDataType(com.borland.dx.dataset.Variant.INT);
    column2.setResolvable(false);
    column2.setServerColumnName("NewColumn2");
    column2.setSqlType(0);
    column3.setColumnName("ser1");
    column3.setDataType(com.borland.dx.dataset.Variant.STRING);
    column3.setResolvable(false);
    column3.setServerColumnName("NewColumn1");
    column3.setSqlType(0);
    column4.setColumnName("ser2");
    column4.setDataType(com.borland.dx.dataset.Variant.STRING);
    column4.setResolvable(false);
    column4.setServerColumnName("NewColumn2");
    column4.setSqlType(0);
    tableDataSet1.setColumns(new Column[] {column1, column2, column3, column4});
    jpFilter.add(jtfCSKLsb, null);
    jpFilter.add(jtfCARTsb, null);
    jp.add(jLabel1, new XYConstraints(15, 20, -1, -1));
    jp.add(jLabel2, new XYConstraints(15, 45, -1, -1));
    jp.add(jraTextField1, new XYConstraints(150, 20, 120, -1));
    jp.add(jraTextField2, new XYConstraints(150, 45, 120, -1));
    jp.add(jraTextField4, new XYConstraints(275, 20, 300, -1));
    jp.add(jraTextField5, new XYConstraints(275, 45, 300, -1));
    jp.add(jLabel3, new XYConstraints(15, 105, -1, -1));
    jp.add(jraTextField3, new XYConstraints(150, 105, 120, -1));
    jp.add(jraTextField6, new XYConstraints(305, 105, 130, -1));
    jp.add(jLabel5, new XYConstraints(15, 80, -1, -1));
    jp.add(jraTextField7, new XYConstraints(150, 80, 45, -1));
    jp.add(jraTextField8, new XYConstraints(225, 80, 45, -1));
    jp.add(jLabel4, new XYConstraints(280, 105, -1, -1));
    jp.add(jLabel6, new XYConstraints(200, 80, -1, -1));
  }
  public boolean Validacija(char mode) {return true;}
  public void SetFokus(char mode) {}
}