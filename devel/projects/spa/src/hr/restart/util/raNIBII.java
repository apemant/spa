/****license*****************************************************************
**   file: raNIBII.java
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
package hr.restart.util;

import java.awt.Dimension;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class raNIBII extends javax.swing.JPanel  {
//javax.swing.JPanel {

  static java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.util.Res_");
  hr.restart.util.raCommonClass myCC=hr.restart.util.raCommonClass.getraCommonClass();
  java.awt.GridLayout gridLayout1 = new java.awt.GridLayout();
  java.awt.BorderLayout borderLayout1 = new java.awt.BorderLayout();
  /// Deklaracija JButtona

  hr.restart.swing.JraButton jBNovi = new hr.restart.swing.JraButton();
  hr.restart.swing.JraButton jBIzmjena = new hr.restart.swing.JraButton();
  hr.restart.swing.JraButton jBBrisanje = new hr.restart.swing.JraButton();
  hr.restart.swing.JraButton jBIspis = new hr.restart.swing.JraButton();
  hr.restart.swing.JraButton jBIzlaz = new hr.restart.swing.JraButton();

//  javax.swing.JButton DummyButton= new   javax.swing.JButton();
//  javax.swing.JPanel jPanelB = new javax.swing.JPanel();
  public raNIBII() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {

//    jPanelB.setLayout(gridLayout1);
    /// jbNOVi
    jBNovi.setPreferredSize(new java.awt.Dimension(100, 27));
    jBNovi.setIcon(raImages.getImageIcon(raImages.IMGADD));
    jBNovi.setText(res.getString("jBNovi"));
    jBNovi.setToolTipText(res.getString("jBNovi"));
    jBNovi.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        jBNovi_actionPerformed();
      }
    });


///  jBIzmjena

    jBIzmjena.setPreferredSize(new java.awt.Dimension(100, 27));
    jBIzmjena.setIcon(raImages.getImageIcon(raImages.IMGCHANGE));
    jBIzmjena.setText(res.getString("jBIzmjena"));
    jBIzmjena.setToolTipText(res.getString("jBIzmjena"));
    jBIzmjena.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        jBIzmjena_actionPerformed();
      }
    });


/// jBBrisanje

    jBBrisanje.setPreferredSize(new java.awt.Dimension(140, 27));
    jBBrisanje.setIcon(raImages.getImageIcon(raImages.IMGDELETE));
    jBBrisanje.setText(res.getString("jBBrisanje"));
    jBBrisanje.setToolTipText(res.getString("jBBrisanje"));
    jBBrisanje.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        jBBrisanje_actionPerformed();
      }
    });


/// jBIspis

    jBIspis.setPreferredSize(new java.awt.Dimension(100, 27));
    jBIspis.setIcon(raImages.getImageIcon(raImages.IMGPRINT));
    jBIspis.setText(res.getString("jBIspis"));
    jBIspis.setToolTipText(res.getString("jBIspis"));
    jBIspis.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        jBIspis_actionPerformed();
      }
    });


/// jBizlaz

    jBIzlaz.setPreferredSize(new java.awt.Dimension(100, 27));
    jBIzlaz.setIcon(raImages.getImageIcon(raImages.IMGEXIT));
    jBIzlaz.setText(res.getString("jIzlaz"));
    jBIzlaz.setToolTipText(res.getString("jIzlaz"));
    jBIzlaz.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        jBIzlaz_actionPerformed();
      }
    });

//    this.setLayout(borderLayout1);
    this.setLayout(gridLayout1);
    this.setPreferredSize(new Dimension(580, 27));
    this.add(jBNovi,null);
    this.add(jBIzmjena,null);
    this.add(jBBrisanje,null);
    this.add(jBIspis,null);
    this.add(jBIzlaz,null);
  }
  public void jBNovi_actionPerformed() {}
  public void jBIzmjena_actionPerformed() {}
  public void jBBrisanje_actionPerformed() {}
  public void jBIspis_actionPerformed() {}
  public void jBIzlaz_actionPerformed() {}
}