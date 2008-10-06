/****license*****************************************************************
**   file: jpPartHistory.java
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
package hr.restart.zapod;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpPartHistory extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmPartHistory fPartHistory;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlApupos = new JLabel();
  JLabel jlCpar = new JLabel();
  JLabel jlPromPrvFaktura = new JLabel();
  JraButton jbSelCpar = new JraButton();
  JraTextField jraApupos = new JraTextField();
  JraTextField jraDatprf = new JraTextField();
  JraTextField jraUkuprom = new JraTextField();
  JlrNavField jlrCpar = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazpar = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpPartHistory(frmPartHistory f) {
    try {
      fPartHistory = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(591);
    lay.setHeight(110);

//    jbSelCpar.setText("...");
    jlApupos.setText("Aparati u posjedu");
    jlCpar.setText("Partner");
    jlPromPrvFaktura.setText("Promet / 1. faktura");
    jraApupos.setColumnName("APUPOS");
    jraApupos.setDataSet(fPartHistory.getRaQueryDataSet());
    jraDatprf.setColumnName("DATPRF");
    jraDatprf.setDataSet(fPartHistory.getRaQueryDataSet());
    jraUkuprom.setColumnName("UKUPROM");
    jraUkuprom.setDataSet(fPartHistory.getRaQueryDataSet());

    jlrCpar.setColumnName("CPAR");
    jlrCpar.setDataSet(fPartHistory.getRaQueryDataSet());
    jlrCpar.setColNames(new String[] {"NAZPAR"});
    jlrCpar.setTextFields(new JTextComponent[] {jlrNazpar});
    jlrCpar.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCpar.setSearchMode(0);
    jlrCpar.setRaDataSet(dm.getPartneri());
    jlrCpar.setNavButton(jbSelCpar);

    jlrNazpar.setColumnName("NAZPAR");
    jlrNazpar.setNavProperties(jlrCpar);
    jlrNazpar.setSearchMode(1);

    jpDetail.add(jlCpar, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlrCpar, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrNazpar, new XYConstraints(255, 20, 295, -1));
    jpDetail.add(jbSelCpar, new XYConstraints(555, 20, 21, 21));
  
    jpDetail.add(jlPromPrvFaktura, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jraUkuprom, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jraDatprf, new XYConstraints(255, 45, 100, -1));
    
    jpDetail.add(jlApupos, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jraApupos, new XYConstraints(150, 70, 400, -1));
    
    jpDetail.add(new JLabel("Napomena:"), new XYConstraints(15, 95, -1, -1));
    jpDetail.add(new JLabel("Pod \"Promet\" se upisuje zbirni promet do tekuæe godine"), new XYConstraints(150, 95, -1, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}


//package hr.restart.zapod;
//
//import java.awt.*;
//import java.awt.event.*;
//import javax.swing.*;
//import javax.swing.text.JTextComponent;
//import com.borland.jbcl.layout.*;
//import com.borland.dx.dataset.*;
//import com.borland.dx.sql.dataset.*;
//import hr.restart.swing.*;
//import hr.restart.baza.*;
//import hr.restart.util.*;
//
//
//public class jpPartHistory extends JPanel {
//  raCommonClass rcc = raCommonClass.getraCommonClass();
//  dM dm = dM.getDataModule();
//  Valid vl = Valid.getValid();
//
//  frmPartHistory fPartHistory;
//  JPanel jpDetail = new JPanel();
//
//  XYLayout lay = new XYLayout();
//  JLabel jlCpar = new JLabel();
//  JLabel jlDatprf = new JLabel();
//  JLabel jlUkuprom = new JLabel();
//  JraButton jbSelCpar = new JraButton();
//  JraTextField jraDatprf = new JraTextField();
//  JraTextField jraUkuprom = new JraTextField();
//  JlrNavField jlrCpar = new JlrNavField() {
//    public void after_lookUp() {
//    }
//  };
//  JlrNavField jlrNazpar = new JlrNavField() {
//    public void after_lookUp() {
//    }
//  };
//
//  public jpPartHistory(frmPartHistory f) {
//    try {
//      fPartHistory = f;
//      jbInit();
//    }
//    catch(Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  private void jbInit() throws Exception {
//    jpDetail.setLayout(lay);
//    lay.setWidth(591);
//    lay.setHeight(110);
//
//    jbSelCpar.setText("...");
//    jlCpar.setText("Partner");
////    jlDatprf.setText("Datum prve fakture");
//    jlUkuprom.setText("Promet / 1. faktura");
//    jraDatprf.setColumnName("DATPRF");
//    jraDatprf.setDataSet(fPartHistory.getRaQueryDataSet());
//    jraUkuprom.setColumnName("UKUPROM");
//    jraUkuprom.setDataSet(fPartHistory.getRaQueryDataSet());
//
//    jlrCpar.setColumnName("CPAR");
//    jlrCpar.setDataSet(fPartHistory.getRaQueryDataSet());
//    jlrCpar.setColNames(new String[] {"NAZPAR"});
//    jlrCpar.setTextFields(new JTextComponent[] {jlrNazpar});
//    jlrCpar.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
//    jlrCpar.setSearchMode(0);
//    jlrCpar.setRaDataSet(dm.getPartneri());
//    jlrCpar.setNavButton(jbSelCpar);
//
//    jlrNazpar.setColumnName("NAZPAR");
//    jlrNazpar.setNavProperties(jlrCpar);
//    jlrNazpar.setSearchMode(1);
//
//    jpDetail.add(jlCpar, new XYConstraints(15, 20, -1, -1));
//    jpDetail.add(jlrCpar, new XYConstraints(150, 20, 100, -1));
//    jpDetail.add(jlrNazpar, new XYConstraints(255, 20, 295, -1));
//    jpDetail.add(jbSelCpar, new XYConstraints(555, 20, 21, 21));
//    
//    jpDetail.add(jlUkuprom, new XYConstraints(15, 45, -1, -1));
//    jpDetail.add(jraUkuprom, new XYConstraints(150, 45, 100, -1));
//    jpDetail.add(jraDatprf, new XYConstraints(255, 45, 100, -1));
//    
//    jpDetail.add(new JLabel("Napomena:"), new XYConstraints(15, 80, -1, -1));
//    jpDetail.add(new JLabel("Pod \"Promet\" se upisuje zbirni promet do tekuæe godine"), new XYConstraints(150, 80, -1, -1));
//    
////    jpDetail.add(jlDatprf, new XYConstraints(15, 70, -1, -1));
////    jpDetail.add(jraDatprf, new XYConstraints(150, 70, 100, -1));
//
//    this.add(jpDetail, BorderLayout.CENTER);
//  }
//}
