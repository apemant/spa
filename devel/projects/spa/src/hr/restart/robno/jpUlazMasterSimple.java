/****license*****************************************************************
**   file: jpUlazMasterSimple.java
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

import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class jpUlazMasterSimple extends JPanel {
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel jlCSKL1 = new JLabel();
  BorderLayout borderLayout3 = new BorderLayout();
  JraTextField jtfDATDOK = new JraTextField();
  XYLayout xYLayout7 = new XYLayout();
  JPanel jpPocStanjeCenter = new JPanel();
  JraButton jbCSKL1 = new JraButton();
  JPanel jpPocStanje = new JPanel();
  JLabel jlDATDOK1 = new JLabel();
  JlrNavField jrfNAZSKL = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  rajpBrDok jpPocStanjeHeader = new rajpBrDok();
  JlrNavField jrfCSKL = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpUlazMasterSimple() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  void jbInit() throws Exception {
    jpPocStanjeCenter.setLayout(xYLayout7);
    jpPocStanjeCenter.setBorder(BorderFactory.createEtchedBorder());
    xYLayout7.setHeight(86);
    xYLayout7.setWidth(645);
    jtfDATDOK.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATDOK.setNextFocusableComponent(jrfCSKL);
    jtfDATDOK.setColumnName("DATDOK");
    jlCSKL1.setText(res.getString("jlCSKL_text"));
    this.setLayout(borderLayout1);
    jbCSKL1.setNextFocusableComponent(jlDATDOK1);
    jbCSKL1.setText(res.getString("jbKEYF9_text"));
    jpPocStanje.setLayout(borderLayout3);
    jlDATDOK1.setText(res.getString("jlDATDOK_text"));
    jrfCSKL.setRaDataSet(dm.getSklad());
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setVisCols(new int[]{0,1});
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setNextFocusableComponent(jrfNAZSKL);
    jrfNAZSKL.setColumnName("NAZSKL");
    jrfNAZSKL.setSearchMode(1);
    jrfNAZSKL.setNavProperties(jrfCSKL);
    jpPocStanjeCenter.add(jlCSKL1, new XYConstraints(15, 20, -1, -1));
    jpPocStanjeCenter.add(jrfNAZSKL, new XYConstraints(260, 20, 340, -1));
    jpPocStanjeCenter.add(jbCSKL1, new XYConstraints(609, 20, 21, 21));
    jpPocStanjeCenter.add(jlDATDOK1, new XYConstraints(15, 45, -1, -1));
    jpPocStanjeCenter.add(jtfDATDOK, new XYConstraints(150, 45, 100, -1));
    jpPocStanjeCenter.add(jrfCSKL, new XYConstraints(150, 20, 100, -1));
    jpPocStanje.add(jpPocStanjeHeader, BorderLayout.NORTH);
    jpPocStanje.add(jpPocStanjeCenter, BorderLayout.CENTER);
    this.add(jpPocStanje, BorderLayout.NORTH);
    jpPocStanjeHeader.addBorder();
  }
  void setDataSet(com.borland.dx.sql.dataset.QueryDataSet qds) {
    jpPocStanjeHeader.setDataSet(qds);
    jtfDATDOK.setDataSet(qds);
    jrfCSKL.setDataSet(qds);
    jrfCSKL.forceFocLost();
  }
}