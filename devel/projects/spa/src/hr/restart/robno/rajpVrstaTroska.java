/****license*****************************************************************
**   file: rajpVrstaTroska.java
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

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class rajpVrstaTroska extends JPanel {
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  XYLayout xYLayout1 = new XYLayout();
  JraTextField jraCVRTR = new JraTextField();
  JraTextField jraNAZIV = new JraTextField();
  JLabel jLCVRTR = new JLabel();
  JLabel jLNaziv = new JLabel();
  JLabel jLKonto = new JLabel();
  JlrNavField jrfBROJKONTA = new JlrNavField() {
    public void after_lookUp() {
/*      findBRDOK(frmDokIzlaz.getmMode());
      after_lookUpCPAR();*/
    }
  };

  JlrNavField jrfNAZKONTA = new JlrNavField(){
    public void after_lookUp() {
//      after_lookUpCPAR();
    }
  };

  //JraButton
  JraButton jbKONTA = new JraButton();



  public rajpVrstaTroska() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  void jbInit() throws Exception {
    this.setLayout(xYLayout1);
    jraCVRTR.setColumnName("CVRTR");
    jraCVRTR.setDataSet(dm.getVrtros());
    jraNAZIV.setColumnName("NAZIV");
    jraNAZIV.setDataSet(dm.getVrtros());
    jLCVRTR.setText("Šifra ");
    jLNaziv.setText("Naziv");
    jLKonto.setText("Konto");

    jrfBROJKONTA.setColumnName("BROJKONTA");
    jrfBROJKONTA.setDataSet(dm.getVrtros());
    jrfBROJKONTA.setColNames(new String[] {"NAZIVKONTA"});
    jrfBROJKONTA.setVisCols(new int[]{0,1,2});
    jrfBROJKONTA.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZKONTA});
    jrfBROJKONTA.setRaDataSet(dm.getKonta());
    jrfBROJKONTA.setNavButton(jbKONTA);

    jrfNAZKONTA.setColumnName("NAZIVKONTA");
    jrfNAZKONTA.setSearchMode(1);
    jrfNAZKONTA.setNavProperties(jrfBROJKONTA);


    xYLayout1.setWidth(516);
    xYLayout1.setHeight(111);
    this.add(jraCVRTR, new XYConstraints(73, 15, 100, -1));
    this.add(jraNAZIV, new XYConstraints(73, 45, 300, -1));
    this.add(jLCVRTR, new XYConstraints(15, 15, -1, -1));
    this.add(jLNaziv, new XYConstraints(15, 45, -1, -1));
    this.add(jLKonto, new XYConstraints(15, 75, -1, -1));
    this.add(jrfBROJKONTA, new XYConstraints(73, 75, 100, -1));
    this.add(jrfNAZKONTA, new XYConstraints(180, 75, 300, -1));
    this.add(jbKONTA, new XYConstraints(485, 75, 21, 21));
  }

}