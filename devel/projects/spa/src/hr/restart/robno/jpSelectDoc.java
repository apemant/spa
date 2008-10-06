/****license*****************************************************************
**   file: jpSelectDoc.java
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
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;

import java.awt.Font;

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

public class jpSelectDoc extends PreSelect {
  static jpSelectDoc jpselectdoc;
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  JPanel jpSelDoc = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JraTextField jtfVRDOK = new JraTextField();
  JlrNavField jrfNAZSKL = new JlrNavField();
  JraButton jbCSKL = new JraButton();
  JLabel jlCPAR = new JLabel();
  JLabel jlCSKL = new JLabel();
  JlrNavField jrfCPAR = new JlrNavField();
  JlrNavField jrfNAZPAR = new JlrNavField();
  JraButton jbCPAR = new JraButton();
  JlrNavField jrfCSKL = new JlrNavField();
  JraTextField jtfDATUMOD = new JraTextField();
  JraTextField jtfDATUMDO = new JraTextField();
  dM dm;
  private String raDokument;
  private boolean terodb;

  public jpSelectDoc() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    jpselectdoc = this;
  }
  public static void showJpSelectDoc(String raDocumentC, raMasterDetail owner, boolean showPres) {
    showJpSelectDoc(raDocumentC, owner, showPres, "", true);
  }
  public static void showJpSelectDoc(String raDocumentC, raMasterDetail owner, boolean showPres, String tit) {
    showJpSelectDoc(raDocumentC, owner, showPres, tit, true);
  }
  public static jpSelectDoc getJpSelectDoc() {
    return jpselectdoc;
  }
  public static void showJpSelectDoc(String raDocumentC, raMasterDetail owner, boolean showPres, String tit, boolean imaPartner) {
    if (jpselectdoc==null) {
      jpselectdoc=new jpSelectDoc();
    }
    jpselectdoc.setRaDokument(raDocumentC);
    jpselectdoc.prepareSelPanel();
    jpselectdoc.setSelDataSet(owner.getMasterSet());
    jpselectdoc.addSelRange(jpselectdoc.jtfDATUMOD,jpselectdoc.jtfDATUMDO);
    jpselectdoc.setSelPanel(jpselectdoc.jpSelDoc);
    if (!imaPartner) {
      jpselectdoc.jlCPAR.setVisible(false);
      jpselectdoc.jrfCPAR.setVisible(false);
      jpselectdoc.jrfNAZPAR.setVisible(false);
      jpselectdoc.jbCPAR.setVisible(false);
    }
    else {
      jpselectdoc.jlCPAR.setVisible(true);
      jpselectdoc.jrfCPAR.setVisible(true);
      jpselectdoc.jrfNAZPAR.setVisible(true);
      jpselectdoc.jbCPAR.setVisible(true);
    }
    if (showPres) {
      jpselectdoc.showPreselect(owner ,"Predselekcija - "+tit);
    } else {
      jpselectdoc.prepareRaDokument();
      jpselectdoc.doSelect();
      owner.go();
    }
  }
  public void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    jLabel1.setText("Vrsta dokumenta");
    jpSelDoc.setLayout(xYLayout1);
    jLabel4.setText("Datum  (od - do)");
    jbCSKL.setText("...");
    jbCPAR.setText("...");
    jtfVRDOK.setColumnName("VRDOK");
    jtfVRDOK.setFont(jtfVRDOK.getFont().deriveFont(Font.BOLD));
    jtfVRDOK.setOpaque(false);
    jtfVRDOK.setEnabled(false);
    jtfVRDOK.setEnablePopupMenu(false);
    jtfVRDOK.setDisabledTextColor(jtfVRDOK.getForeground());
    jtfVRDOK.setBorder(null);
    jtfDATUMOD.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATUMOD.setColumnName("DATDOK");
    jtfDATUMDO.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATUMDO.setColumnName("DATDOK");
    xYLayout1.setWidth(575);
    xYLayout1.setHeight(136);
    jpSelDoc.add(jLabel1, new XYConstraints(15, 20, -1, -1));
    jpSelDoc.add(jtfVRDOK, new XYConstraints(150, 20, 50, -1));
    jpSelDoc.add(jlCSKL, new XYConstraints(15, 45, -1, -1));
    jpSelDoc.add(jrfCSKL, new XYConstraints(150, 45, 100, -1));
    jpSelDoc.add(jrfNAZSKL, new XYConstraints(260, 45, 275, -1));
    jpSelDoc.add(jbCSKL, new XYConstraints(539, 45, 21, 21));
    jpSelDoc.add(jrfCPAR, new XYConstraints(150, 70, 100, -1));
    jpSelDoc.add(jlCPAR, new XYConstraints(15, 70, -1, -1));
    jpSelDoc.add(jrfNAZPAR, new XYConstraints(260, 70, 275, -1));
    jpSelDoc.add(jbCPAR, new XYConstraints(539, 70, 21, 21));
    jpSelDoc.add(jLabel4, new XYConstraints(15, 95, -1, -1));
    jpSelDoc.add(jtfDATUMOD, new XYConstraints(150, 95, 100, -1));
    jpSelDoc.add(jtfDATUMDO, new XYConstraints(260, 95, 100, -1));
  }
  public void setRaDokument(String newRaDokument) {
    raDokument = newRaDokument;
    if (raDokument.equals("TER") || raDokument.equals("ODB") || raDokument.equals("RAC") || raDokument.equals("PRD")) terodb = true;
    else terodb = false;
  }

  void prepareSelPanel() {
    jrfCSKL.setColumnName("CSKL");
    if (terodb) {
      jlCSKL.setText("Organizacijska jedinica");
      jrfCSKL.setNavColumnName("CORG");
      jrfCSKL.setColNames(new String[] {"NAZIV"});
      jrfCSKL.setRaDataSet(dm.getOrgstruktura());
      jrfNAZSKL.setColumnName("NAZIV");
    } else {
      jlCSKL.setText(res.getString("jlCSKL_text"));
      jrfCSKL.setNavColumnName("CSKL");
      jrfCSKL.setColNames(new String[] {"NAZSKL"});
      jrfCSKL.setRaDataSet(dm.getSklad());
      jrfNAZSKL.setColumnName("NAZSKL");
    }
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setVisCols(new int[]{0,1});
    jrfCSKL.setNavButton(jbCSKL);
    jrfNAZSKL.setSearchMode(1);
    jrfNAZSKL.setNavProperties(jrfCSKL);
    if (raDokument.equals("IZD")) {
      jlCPAR.setText("Mjesto troška");
      jrfCPAR.setColumnName("CORG");
      jrfCPAR.setColNames(new String[] {"NAZIV"});
      jrfCPAR.setVisCols(new int[]{0,1,2});
      jrfCPAR.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPAR});
      jrfCPAR.setRaDataSet(dm.getOrgstruktura());
      jrfCPAR.setNavButton(jbCPAR);
      jrfNAZPAR.setColumnName("NAZIV");
      jrfNAZPAR.setSearchMode(1);
      jrfNAZPAR.setNavProperties(jrfCPAR);
    } else {
      jlCPAR.setText(res.getString("jlCPAR_text"));
      jrfCPAR.setColumnName("CPAR");
      jrfCPAR.setColNames(new String[] {"NAZPAR"});
      jrfCPAR.setVisCols(new int[]{0,1,2});
      jrfCPAR.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPAR});
      jrfCPAR.setRaDataSet(dm.getPartneri());
      jrfCPAR.setNavButton(jbCPAR);
      jrfNAZPAR.setColumnName("NAZPAR");
      jrfNAZPAR.setSearchMode(1);
      jrfNAZPAR.setNavProperties(jrfCPAR);
    }
  }

  void prepareRaDokument() {
    getSelRow().setString("VRDOK",raDokument);
    if (!terodb) getSelRow().setString("CSKL",hr.restart.robno.Util.getUtil().findCSKL());
    else getSelRow().setString("CSKL", "");
  }
  void prepareDATDOK() {
    getSelRow().setTimestamp("DATDOK-from",hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(vl.findYear())));
    getSelRow().setTimestamp("DATDOK-to",hr.restart.util.Valid.getValid().getToday());
    getSelRow().post();
  }

  public void SetFokus() {
    prepareRaDokument();
    prepareDATDOK();
    jrfCPAR.setText("");
    jrfCPAR.forceFocLost();
    jrfCSKL.forceFocLost();
    jrfCSKL.requestFocus();
  }

  public java.sql.Timestamp getDefDate() {
    return getSelRow().getTimestamp("DATDOK-to");
  }
  public String getRaDokument() {
    return raDokument;
  }
  public boolean Validacija() {
    if (hr.restart.util.Valid.getValid().isEmpty(this.jrfCSKL)) return false;
    return true;
  }
}
