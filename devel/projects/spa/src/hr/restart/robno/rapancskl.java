/****license*****************************************************************
**   file: rapancskl.java
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
import hr.restart.util.JlrNavField;

import java.awt.BorderLayout;

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

public class rapancskl extends JPanel {
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  dM dm;
  private com.borland.dx.dataset.DataSet raDataSet;
  private com.borland.dx.dataset.DataSet realDataSet;
//  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(true);
  private boolean EnableTransferFocus = true;

  public void setEnableTransferFocus(boolean how) {
    EnableTransferFocus=how;
  }

  private char raMode;
  JPanel jpCPAR = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  XYLayout xYLayout1 = new XYLayout();
  JlrNavField jrfCPAR = new JlrNavField() {
    public void after_lookUp() {
    	if (!isLastLookSuccessfull()) return;
      if (!jrfCPAR.getText().trim().equals("")) {
        disabCPAR();
        findFocusAfter();
      }
    }
  };
  JlrNavField jrfNAZPAR = new JlrNavField() {
    public void after_lookUp() {
    	if (!isLastLookSuccessfull()) return;
      if (!jrfCPAR.getText().trim().equals("")) {
        disabCPAR();
        findFocusAfter();
      }
    }
  };
  JraButton jbCPAR = new JraButton();
  JLabel jlCPAR = new JLabel();
  JPanel jpCSKL = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  JlrNavField jrfNAZSKL = new JlrNavField() {
    public void after_lookUp() {
    	if (!isLastLookSuccessfull()) return;
      if (!jrfCSKL.getText().trim().equals("")) {
        disabCSKL();
      }
      if (raMode=='S') {
        MYadderChangerCskl();
        findFocusAfter();
      }
      else {
        System.out.println("rekvestam fokus");
        jrfCPAR.requestFocus();
      }
    }
  };
  JraButton jbCSKL = new JraButton();
  JLabel jlCSKL = new JLabel();
  JLabel jLabel4 = new JLabel();
  JlrNavField jrfCSKL = new JlrNavField() {
    public void after_lookUp() {
    	if (!isLastLookSuccessfull()) return;
      if (!jrfCSKL.getText().trim().equals("")) {
        disabCSKL();
      }
      if (raMode=='S') {
        MYadderChangerCskl();
        findFocusAfter();
      }
      else {
        jrfCPAR.requestFocus();
      }
    }
  };
  JLabel jLabel3 = new JLabel();

  public rapancskl() {
    try {
      jbInit();
      jbInitadder();
      jpCSKL.add(jbCSKL, new XYConstraints(609, 25, 21, 21));
      jpCSKL.add(jrfNAZSKL, new XYConstraints(255, 25, 349, -1));
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  public rapancskl(String nazivLabSklad,String tocskl, boolean uptitle) {
    try {
      jbInit();
      this.jlCSKL.setText(nazivLabSklad);
//      jrfCSKL.setColumnName(tocskl);
      if (uptitle) jbInitadder();
      jpCSKL.add(jbCSKL, new XYConstraints(609, 25, 21, 21));
      jpCSKL.add(jrfNAZSKL, new XYConstraints(255, 25, 349, -1));
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  void jbInit() throws Exception {
   dm = hr.restart.baza.dM.getDataModule();
    this.setLayout(borderLayout1);
    jpCPAR.setLayout(xYLayout1);
    jrfCPAR.setColumnName("CPAR");
//    jrfCPAR.setNextFocusableComponent(jrfNAZPAR); zakomentirano zbog JDK1.4
    jrfCPAR.setColNames(new String[] {"NAZPAR"});
    jrfCPAR.setVisCols(new int[]{0,1,2});
    jrfCPAR.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPAR});
    jrfCPAR.setRaDataSet(dm.getPartneri());
    jrfCPAR.setNavButton(jbCPAR);
    jrfNAZPAR.setColumnName("NAZPAR");
    jrfNAZPAR.setSearchMode(1);
    jrfNAZPAR.setNavProperties(jrfCPAR);
    jlCPAR.setText("Poslovni partner");
    jpCSKL.setLayout(xYLayout2);

//    jrfNAZSKL.setNextFocusableComponent(jrfCSKL); zakomentirano zbog JDK1.4
    jrfNAZSKL.setColumnName("NAZSKL");
    jrfNAZSKL.setSearchMode(1);
    jrfNAZSKL.setNavProperties(jrfCSKL);

    jlCSKL.setText("Skladište");
    jLabel4.setHorizontalAlignment(SwingConstants.LEFT);
    jLabel4.setText("Naziv");
//    jrfCSKL.setNextFocusableComponent(jrfNAZSKL); zakomentirano zbog JDK1.4
    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setVisCols(new int[]{0,1});
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
    jrfCSKL.setNavButton(jbCSKL);
    jLabel3.setHorizontalAlignment(SwingConstants.LEFT);
    jLabel3.setText("Šifra");
    this.add(jpCPAR, BorderLayout.CENTER);
    jpCPAR.add(jlCPAR, new XYConstraints(15, 5, -1, -1));
    jpCPAR.add(jrfCPAR, new XYConstraints(150, 5, 100, -1));
    jpCPAR.add(jbCPAR, new XYConstraints(609, 5, 21, 21));
    jpCPAR.add(jrfNAZPAR,   new XYConstraints(255, 5, 349, -1));
    this.add(jpCSKL, BorderLayout.NORTH);
    jpCSKL.add(jlCSKL, new XYConstraints(15, 25, -1, -1));
    jpCSKL.add(jrfCSKL, new XYConstraints(150, 25, 100, -1));

  }
/*
  void jbInit1() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    this.setLayout(borderLayout1);
    jpCPAR.setLayout(xYLayout1);
    jrfCPAR.setColumnName("CPAR");
    jrfCPAR.setNextFocusableComponent(jrfNAZPAR);
    jrfCPAR.setColNames(new String[] {"NAZPAR"});
    jrfCPAR.setVisCols(new int[]{0,1,2});
    jrfCPAR.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPAR});
    jrfCPAR.setRaDataSet(dm.getPartneri());
    jrfNAZPAR.setColumnName("NAZPAR");
    jrfNAZPAR.setSearchMode(1);
    jrfNAZPAR.setNavProperties(jrfCPAR);
    jbCPAR.setText(res.getString("jbKEYF9_text"));
    jlCPAR.setText("Poslovni partner");
    jpCSKL.setLayout(xYLayout2);

    jrfNAZSKL.setNextFocusableComponent(jrfCSKL);
    jrfNAZSKL.setColumnName("NAZSKL");
    jrfNAZSKL.setSearchMode(1);
    jrfNAZSKL.setNavProperties(jrfCSKL);
    jbCSKL.setText(res.getString("jbKEYF9_text"));
    jbCSKL.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbCSKL_actionPerformed(e);
      }
    });
    jlCSKL.setText("Skladište");
    jLabel4.setHorizontalAlignment(SwingConstants.LEFT);
    jLabel4.setText("Naziv");
    jrfCSKL.setNextFocusableComponent(jrfNAZSKL);
    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setVisCols(new int[]{0,1});
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setRaDataSet(dm.getSklad());
    jLabel3.setHorizontalAlignment(SwingConstants.LEFT);
    jLabel3.setText("Šifra");
    this.add(jpCPAR, BorderLayout.CENTER);
    jpCPAR.add(jlCPAR, new XYConstraints(15, 5, -1, -1));
    jpCPAR.add(jrfCPAR, new XYConstraints(150, 5, 100, -1));
    jpCPAR.add(jbCPAR, new XYConstraints(609, 5, 21, 21));
    jpCPAR.add(jrfNAZPAR, new XYConstraints(255, 5, 345, -1));
    this.add(jpCSKL, BorderLayout.NORTH);
    jpCSKL.add(jlCSKL, new XYConstraints(15, 25, -1, -1));
    jpCSKL.add(jrfCSKL, new XYConstraints(150, 25, 100, -1));

  }
*/
  public void showMe() {
//      jrfCSKL.setText(hr.restart.sisfun.frmParam.getParam("robno","defCskl"));
      jrfCSKL.setText(hr.restart.sisfun.raUser.getInstance().getDefSklad());
  }

  void jbInitadder() throws Exception {

    jpCSKL.add(jLabel3, new XYConstraints(150, 8, 100, -1));
    jpCSKL.add(jLabel4, new XYConstraints(255, 8, 260, -1));

  }

/**
 * Setiranje dataseta u kojem su naši jlrNavFieldovi
 */
  public void setRaDataSet(com.borland.dx.dataset.DataSet newRaDataSet) {
    raDataSet = newRaDataSet;
    if (raMode=='S') {
      jrfCSKL.setRaDataSet(raDataSet);
    }
    else if (raMode=='P') {
      jrfCPAR.setRaDataSet(raDataSet);
    }
    else {
      jrfCPAR.setRaDataSet(raDataSet);
      jrfCSKL.setRaDataSet(raDataSet);
    }
    showMe();
  }
  public void setDataSet(com.borland.dx.dataset.DataSet newRaDataSet) {
    realDataSet = newRaDataSet;
    if (raMode=='S') {
      jrfCSKL.setDataSet(realDataSet);
    }
    else if (raMode=='P') {
      jrfCPAR.setDataSet(realDataSet);
    }
    else {
      jrfCPAR.setDataSet(realDataSet);
      jrfCSKL.setDataSet(realDataSet);
    }
  }
  public com.borland.dx.dataset.DataSet getRaDataSet() {
    return raDataSet;
  }
/**
 * Setiranje moda rada
 *  - 'S' - samo skladište
 *  - 'P' - samo partneri
 *  - 'A' - sve
 */
  public void setRaMode(char newRaMode) {
    raMode = newRaMode;
    if (raMode=='S') {
      jrfCPAR.setVisible(false);
      jrfNAZPAR.setVisible(false);
      jlCPAR.setVisible(false);
      jbCPAR.setVisible(false);
    }
    else if (raMode=='P') {
      jrfCSKL.setVisible(false);
      jrfNAZSKL.setVisible(false);
      jlCSKL.setVisible(false);
      jbCSKL.setVisible(false);
    }
  }
  public char getRaMode() {
    return raMode;
  }
/**
 * Setiranje skladišta
 */
  public void setCSKL(String tekst) {
    jrfCSKL.setText(tekst);
    jrfCSKL.forceFocLost();
    if (tekst.equals("")) {
      jrfCSKL.requestFocusLater();
    }
  }
/**
 * Setiranje partnera
 */
  public void setCPAR(String tekst) {
    jrfCPAR.setText(tekst);
    jrfCPAR.forceFocLost();
    if (tekst.equals("")) {
      jrfCPAR.requestFocus();
    }
  }
/**
 * Dohvat šifre skladišta
 */
  public String getCSKL() {
    return jrfCSKL.getText().trim();
  }
/**
 * Dohvat šifre partnera
 */
  public String getCPAR() {
    return jrfCPAR.getText().trim();
  }
/**
 * Disejbliranje pojednog dijela ekrana
 *  - 'S' - skladište
 *  - 'P' - partner
 *  - 'A' - sve
 *  - 'N' - ništa
 */
  public void setDisab(char mode) {
    if (mode=='S') {
      rcc.EnabDisabAll(this.jpCSKL, false);
    }
    else if (mode=='P') {
      rcc.EnabDisabAll(this.jpCPAR, false);
    }
    else if (mode=='A') {
      rcc.EnabDisabAll(this, false);
    }
    else if (mode=='N') {
      rcc.EnabDisabAll(this, true);
    }
  }
/**
 * Metoda za overrajdanje zbog transfera fokusa
 */
  public void findFocusAfter() {

/*    if (EnableTransferFocus){
      jrfNAZPAR.transferFocus();
    }*/
  }
  private void disabCPAR() {
    rcc.EnabDisabAll(this.jpCPAR, false);
  }
  private void disabCSKL() {
    rcc.EnabDisabAll(this.jpCSKL, false);
  }
  public void MYadderChangerCskl(){}

  public void defFocus(){
    this.jrfCSKL.requestFocus();
  }
}