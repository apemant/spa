/****license*****************************************************************
**   file: frmZavTr.java
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
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raSifraNaziv;

import java.awt.event.FocusEvent;

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

public class frmZavTr extends raSifraNaziv {
  _Main main;

  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  hr.restart.baza.dM dm;
  JLabel jlPZT = new JLabel();
  JLabel jlIZT = new JLabel();
  JLabel jlCPAR = new JLabel();
  JraTextField jtfPZT = new JraTextField() {
    public void valueChanged() {
      jtfPZT_focusLost(null);
    }
  };
  JraTextField jtfIZT = new JraTextField() {
    public void valueChanged() {
      jtfIZT_focusLost(null);
    }
  };
  JlrNavField jrfCPAR = new JlrNavField();
  JlrNavField jrfNAZPAR = new JlrNavField();
  JraButton jbCPAR = new JraButton();
  JraCheckBox jcbZTNAZT = new JraCheckBox();
  JLabel jlBROJKONTA = new JLabel();
  JlrNavField jrfBROJKONTA = new JlrNavField();
  JlrNavField jrfNAZIVKONTA = new JlrNavField();
  JraButton jbBROJKONTA = new JraButton();

  public frmZavTr() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    this.setRaDataSet(dm.getZavtr());
    this.setRaColumnSifra("CZT");
    this.setRaColumnNaziv("NZT");
    this.setRaText("Zavisni trošak");
    jp.setLayout(xYLayout1);
    jlPZT.setText(res.getString("jlPZT_text"));
    jlIZT.setText(res.getString("jlIZT_text"));
    jlCPAR.setText(res.getString("jlCPAR_text"));
    jlBROJKONTA.setText(res.getString("jlBROJKONTA_text"));
    jtfPZT.setColumnName("PZT");
    jtfPZT.setDataSet(dm.getZavtr());
    /*jtfPZT.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfPZT_focusLost(e);
      }
    });*/
    jtfIZT.setColumnName("IZT");
    jtfIZT.setDataSet(dm.getZavtr());
    /*jtfIZT.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfIZT_focusLost(e);
      }
    });*/
    jrfCPAR.setColumnName("CPAR");
    jrfCPAR.setDataSet(dm.getZavtr());
    jrfCPAR.setColNames(new String[] {"NAZPAR"});
    jrfCPAR.setVisCols(new int[]{0,1,2});
    jrfCPAR.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPAR});
    jrfCPAR.setRaDataSet(dm.getPartneri());
    jrfNAZPAR.setColumnName("NAZPAR");
    jrfNAZPAR.setSearchMode(1);
    jrfNAZPAR.setNavProperties(jrfCPAR);
    jbCPAR.setText(res.getString("jbKEYF9_text"));
    jrfCPAR.setNavButton(this.jbCPAR);

    jrfBROJKONTA.setColumnName("BROJKONTA");
    jrfBROJKONTA.setDataSet(dm.getZavtr());
    jrfBROJKONTA.setColNames(new String[] {"NAZIVKONTA"});
    jrfBROJKONTA.setVisCols(new int[]{0,1});
    jrfBROJKONTA.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZIVKONTA});
    jrfBROJKONTA.setRaDataSet(hr.restart.zapod.raKonta.getAnalitickaKonta());
    jrfNAZIVKONTA.setColumnName("NAZIVKONTA");
    jrfNAZIVKONTA.setSearchMode(1);
    jrfNAZIVKONTA.setNavProperties(jrfBROJKONTA);
    jcbZTNAZT.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbZTNAZT.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbZTNAZT.setText("ZTnZT");
    jcbZTNAZT.setColumnName("ZTNAZT");
    jcbZTNAZT.setDataSet(dm.getZavtr());
    jcbZTNAZT.setSelectedDataValue("D");
    jcbZTNAZT.setUnselectedDataValue("N");
    jrfBROJKONTA.setNavButton(this.jbBROJKONTA);
    jbBROJKONTA.setText(res.getString("jbKEYF9_text"));
    xYLayout1.setWidth(555);
    xYLayout1.setHeight(91);
    jp.add(jcbZTNAZT, new XYConstraints(460, 0, 80, -1));
    jp.add(jlCPAR, new XYConstraints(15, 25, -1, -1));
    jp.add(jlBROJKONTA, new XYConstraints(15, 50, -1, -1));
    jp.add(jrfCPAR, new XYConstraints(150, 25, 100, -1));
    jp.add(jrfBROJKONTA, new XYConstraints(150, 50, 100, -1));
    jp.add(jbCPAR, new XYConstraints(519, 25, 21, 21));
    jp.add(jbBROJKONTA, new XYConstraints(519, 50, 21, 21));
    jp.add(jrfNAZPAR, new XYConstraints(260, 25, 255, -1));
    jp.add(jrfNAZIVKONTA, new XYConstraints(260, 50, 255, -1));
    jp.add(jlIZT, new XYConstraints(15, 0, -1, -1));
    jp.add(jtfIZT, new XYConstraints(150, 0, 100, -1));
    jp.add(jlPZT, new XYConstraints(260, 0, -1, -1));
    jp.add(jtfPZT, new XYConstraints(350, 0, 100, -1));
    this.jpRoot.add(jp,java.awt.BorderLayout.SOUTH);
  }

  void jtfPZT_focusLost(FocusEvent e) {
    if (dm.getZavtr().getBigDecimal("PZT").doubleValue()>0) {
      dm.getZavtr().setBigDecimal("IZT", main.nul);
    }
  }

  void jtfIZT_focusLost(FocusEvent e) {
    if (dm.getZavtr().getBigDecimal("IZT").doubleValue()>0) {
      dm.getZavtr().setBigDecimal("PZT",main.nul);
    }
  }

  public boolean DeleteCheck() {
    if (util.chkIsDeleteable("VTZAVTR", "CZT", dm.getZavtr().getString("CZT"), util.MOD_STR)==false)
      return false;
    if (util.isDeleteable("VSHZTR_ZTR", "CZT", dm.getZavtr().getString("CZT"), util.MOD_STR)==false)
      return false;
    return true;
  }

}