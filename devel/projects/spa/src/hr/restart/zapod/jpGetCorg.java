/****license*****************************************************************
**   file: jpGetCorg.java
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
import hr.restart.swing.layout.raXYConstraints;
import hr.restart.swing.layout.raXYLayout;
import hr.restart.util.JlrNavField;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;



public class jpGetCorg extends JPanel {

  XYLayout xYLay = new XYLayout();

  raXYLayout xYLayNaz = new raXYLayout(true);

  JPanel jpNaz = new JPanel();

  JLabel jlOJ = new JLabel();

  JlrNavField jlrCORG = new JlrNavField();

  JlrNavField jlrNAZIV = new JlrNavField();

  JraButton jbGetCorg = new JraButton();

  dM dm = dM.getDataModule();

  JLabel jlCORG = new JLabel();

  JLabel jlNAZIV = new JLabel();

  public jpGetCorg() {

    try {

      jbInit();

    }

    catch(Exception ex) {

      ex.printStackTrace();

    }

  }

  void jbInit() throws Exception {

    jlOJ.setText("Org. jedinica");

    this.setLayout(xYLay);

    jpNaz.setLayout(xYLayNaz);

    jlrCORG.setColumnName("CORG");

    jlrCORG.setColNames(new String[] {"NAZIV"});

    jlrCORG.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZIV});

    jlrCORG.setVisCols(new int[] {0,1});

    jlrCORG.setRaDataSet(dm.getOrgstruktura());

    jlrCORG.setSearchMode(0);

    jlrCORG.setNavButton(jbGetCorg);

    jlrCORG.setNavProperties(null);

    jlrNAZIV.setColumnName("NAZIV");

    jlrNAZIV.setSearchMode(1);

    jlrNAZIV.setNavProperties(jlrCORG);

    jlCORG.setText("Oznaka");

    jlNAZIV.setText("Naziv");

    add(jlOJ,    new XYConstraints(15, 20, -1, -1));

    add(jlrCORG,  new XYConstraints(150, 20, 100, -1));

    add(jlCORG,     new XYConstraints(150, 5, -1, -1));

    add(jpNaz,  new XYConstraints(255, 0, -1, -1));

    jpNaz.add(jlrNAZIV,  new raXYConstraints(0, 20, 300, -1));

    jpNaz.add(jbGetCorg,    new raXYConstraints(305, 20, 21, 21));

    jpNaz.add(jlNAZIV,  new raXYConstraints(0, 5, -1, -1));

  }



  public void setDataSet(com.borland.dx.dataset.DataSet ds) {

    jlrCORG.setDataSet(ds);

  }

}