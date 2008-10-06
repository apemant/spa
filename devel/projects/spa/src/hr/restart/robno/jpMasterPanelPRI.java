/****license*****************************************************************
**   file: jpMasterPanelPRI.java
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
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class jpMasterPanelPRI extends JPanel {

  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  private java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  BorderLayout borderLayout1 = new BorderLayout();
  XYLayout xYLayout3 = new XYLayout();

  JPanel jpMasterCenter = new JPanel();
  rajpBrDok jpBRDOK = new rajpBrDok();

  JLabel jlBRDOKUL = new JLabel();
  JLabel jlDATDOKUL = new JLabel();
  JLabel jlCPAR = new JLabel();
  JLabel jlDATDOK = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel1 = new JLabel();
  JraTextField jtfDATDOKUL = new JraTextField();
  JraTextField jtfDATDOK = new JraTextField() {
    public void valueChanged() {
      jtfDATDOK_focusLost(null);
    }
  };
  JraTextField jtfBRDOKUL = new JraTextField();
//  javax.swing.JFormattedTextFields JFTF = new JFormattedTextFields();

  JlrNavField jrfCPAR = new JlrNavField();
  JlrNavField jrfNAZPAR = new JlrNavField();
  JraButton jbCPAR = new JraButton();

  public jpMasterPanelPRI() {
    try {
      jbInit();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {

    jpMasterCenter.setBorder(BorderFactory.createEtchedBorder());
    jpMasterCenter.setLayout(xYLayout3);
    this.setLayout(borderLayout1);
    jtfDATDOKUL.setColumnName("DATDOKUL");
    jrfCPAR.setColumnName("CPAR");
    jrfCPAR.setColNames(new String[] {"NAZPAR"});
    jrfCPAR.setVisCols(new int[]{0,1,2});
    jrfCPAR.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPAR});
    jrfCPAR.setRaDataSet(dm.getPartneri());
    jrfNAZPAR.setColumnName("NAZPAR");
    jrfNAZPAR.setSearchMode(1);
    jrfNAZPAR.setNavProperties(jrfCPAR);
    jrfCPAR.setNavButton(jbCPAR);
    xYLayout3.setWidth(645);
    xYLayout3.setHeight(125);
    jtfDATDOK.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATDOK.setColumnName("DATDOK");
    /*jtfDATDOK.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfDATDOK_focusLost(e);
      }
    });*/
    jlBRDOKUL.setText(res.getString("jlBRDOKUL_text"));
    jtfDATDOKUL.setHorizontalAlignment(SwingConstants.CENTER);
    jlDATDOKUL.setText(res.getString("jlDATDOKUL_text"));
    jlCPAR.setText(res.getString("jlCPAR_text"));



    jlDATDOK.setText(res.getString("jlDATDOK_text"));
    jtfBRDOKUL.setColumnName("BRDOKUL");
    jLabel2.setText("Naziv");
    jLabel1.setText("Šifra");

    jpMasterCenter.add(jlCPAR, new XYConstraints(15, 35, -1, -1));
    jpMasterCenter.add(jrfCPAR, new XYConstraints(150, 35, 100, -1));
    jpMasterCenter.add(jrfNAZPAR, new XYConstraints(260, 35, 345, -1));
    jpMasterCenter.add(jbCPAR, new XYConstraints(609, 35, 21, 21));
    jpMasterCenter.add(jlDATDOK, new XYConstraints(15, 60, -1, -1));
    jpMasterCenter.add(jlBRDOKUL, new XYConstraints(15, 85, -1, -1));
    jpMasterCenter.add(jtfDATDOK, new XYConstraints(150, 60, 100, -1));
    jpMasterCenter.add(jtfBRDOKUL, new XYConstraints(150, 85, 200, -1));
    jpMasterCenter.add(jlDATDOKUL, new XYConstraints(400, 85, -1, -1));
    jpMasterCenter.add(jtfDATDOKUL, new XYConstraints(505, 85, 100, -1));

    this.add(jpBRDOK, BorderLayout.NORTH);
    this.add(jpMasterCenter, BorderLayout.CENTER);

    jpMasterCenter.add(jLabel1, new XYConstraints(150, 15, -1, -1));
    jpMasterCenter.add(jLabel2, new XYConstraints(260, 15, -1, -1));

  }

  void jtfDATDOK_focusGained(FocusEvent e) {}
  void jtfDATDOK_focusLost(FocusEvent e) {}
//
//  void jbCPAR_actionPerformed(ActionEvent e) {
//    jrfCPAR.keyF9Pressed();
//  }

  public void initPanel(char mode) {
    jpBRDOK.SetDefTextDOK(mode);
    if (presPRK.getPres().jrfCPAR.getText().equals("")) {
      if (mode!='B') {
        rcc.setLabelLaF(jrfCPAR, true);
        rcc.setLabelLaF(jrfNAZPAR, true);
        rcc.setLabelLaF(jbCPAR, true);
        jrfCPAR.requestFocus();
      }
    }
    else {
      rcc.setLabelLaF(jrfCPAR, false);
      rcc.setLabelLaF(jrfNAZPAR, false);
      rcc.setLabelLaF(jbCPAR, false);
      jrfCPAR.forceFocLost();
      jtfDATDOK.requestFocus();
    }
  }

  void setDataSet(com.borland.dx.sql.dataset.QueryDataSet qds) {

    jtfDATDOKUL.setDataSet(qds);
    jrfCPAR.setDataSet(qds);
    jtfDATDOK.setDataSet(qds);
    jtfBRDOKUL.setDataSet(qds);
    jpBRDOK.setDataSet(qds);

  }

}