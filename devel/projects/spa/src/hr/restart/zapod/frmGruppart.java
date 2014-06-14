/****license*****************************************************************
**   file: frmGruppart.java
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
import hr.restart.util.JlrNavField;
import hr.restart.util.raSifraNaziv;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;



public class frmGruppart extends raSifraNaziv {

  XYLayout xYLayout1 = new XYLayout();

  JlrNavField jlrNazAgenta = new JlrNavField();

  JLabel jlAgent = new JLabel();

  JPanel jPanel1 = new JPanel();

  JlrNavField jlrAgent = new JlrNavField();

  JraButton jbSelAgent = new JraButton();

  dM dm;



  public frmGruppart() {

    try {

      jbInit();

    }

    catch(Exception e) {

      e.printStackTrace();

    }

  }



  public void SetFokus(char mode) {

    super.SetFokus(mode);

    if (mode == 'N') {

      jlrNazAgenta.setText("");

    }

  }



  private void jbInit() throws Exception {

   dm = hr.restart.baza.dM.getDataModule();

   setRaDataSet(dm.getAllGruppart());

   setRaColumnSifra("CGRPAR");

   setRaColumnNaziv("NAZIV");

   setRaText("Grupe partnera");



   jPanel1.setLayout(xYLayout1);

   jlAgent.setText("Agent");



   xYLayout1.setWidth(580);

   xYLayout1.setHeight(40);



   jlrAgent.setColumnName("CAGENT");

   jlrAgent.setTextFields(new JTextComponent[] {jlrNazAgenta});

   jlrAgent.setColNames(new String[] {"NAZAGENT"});

   jlrAgent.setSearchMode(0);

   jlrAgent.setDataSet(getRaDataSet());

   jlrAgent.setRaDataSet(dm.getAgenti());

   jlrAgent.setVisCols(new int[] {0,1});

   jlrAgent.setNavButton(jbSelAgent);



   jlrNazAgenta.setSearchMode(1);

   jlrNazAgenta.setNavProperties(jlrAgent);

   jlrNazAgenta.setColumnName("NAZAGENT");


   jPanel1.add(jlAgent, new XYConstraints(15, 0, -1, -1));

   jPanel1.add(jlrAgent, new XYConstraints(150, 0, 100, -1));

   jPanel1.add(jlrNazAgenta,      new XYConstraints(255, 0, 285, -1));

   jPanel1.add(jbSelAgent,    new XYConstraints(545, 0, 21, 21));

   jpRoot.add(jPanel1, BorderLayout.SOUTH);

  }

}