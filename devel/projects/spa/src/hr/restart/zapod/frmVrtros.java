/****license*****************************************************************
**   file: frmVrtros.java
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
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raSifraNaziv;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;



public class frmVrtros extends raSifraNaziv {

  XYLayout xYLayout1 = new XYLayout();

  JPanel jPanel1 = new JPanel();

  JlrNavField jrfVRDOK = new JlrNavField();
  JLabel jlVRDOK = new JLabel();
  JraButton jbVRDOK = new JraButton();
  JlrNavField jrfNAZDOK = new JlrNavField();
  JLabel jlPostOsn = new JLabel("Posto osnovice poreza");
  JraTextField jrfOSN = new JraTextField();
  
  
  dM dm;

  public frmVrtros() {

    try {

      jbInit();

    }

    catch(Exception e) {

      e.printStackTrace();

    }

  }

  private void jbInit() throws Exception {

    dm = hr.restart.baza.dM.getDataModule();

    this.setRaDataSet(dm.getAllVrtros());
    this.setRaColumnSifra("CVRTR");
    this.setRaColumnNaziv("NAZIV");
    this.setRaText("Vrsta troška");
    jPanel1.setLayout(xYLayout1);
    jlVRDOK.setText("Vrsta dokumenta");
    xYLayout1.setHeight(40);
    xYLayout1.setWidth(580);
    jrfVRDOK.setVisCols(new int[] {0,1});
    jrfVRDOK.setRaDataSet(dm.getVrdokum());
    jrfVRDOK.setDataSet(dm.getAllVrtros());
    
    jrfVRDOK.setColumnName("VRDOK");
    jrfVRDOK.setNavColumnName("VRDOK");
    
    jrfVRDOK.setTextFields(new JTextComponent[] {jrfNAZDOK});
    jrfVRDOK.setColNames(new String[] {"NAZDOK"});
    
    jrfVRDOK.setSearchMode(3);
	
    jrfVRDOK.setNavButton(jbVRDOK);
    jrfVRDOK.setNavProperties(jrfVRDOK);
    jrfVRDOK.setSearchMode(1);
    
    jrfNAZDOK.setColumnName("NAZDOK");
    jrfNAZDOK.setSearchMode(1);
    jrfNAZDOK.setNavProperties(jrfVRDOK);

    jrfOSN.setDataSet(dm.getAllVrtros());
    jrfOSN.setColumnName("OSNOVICA");


//    jPanel1.add(jlVRDOK, new XYConstraints(15, 0, -1, -1));
//    jPanel1.add(jrfVRDOK, new XYConstraints(150, 0, 100, -1));
//    jPanel1.add(jrfNAZDOK,   new XYConstraints(255, 0, 285, -1));
//    jPanel1.add(jbVRDOK,  new XYConstraints(545, 0, 21, 21));

    jPanel1.add(jlPostOsn, new XYConstraints(15, 0, -1, -1));
    jPanel1.add(jrfOSN, new XYConstraints(150, 0, 100, -1));

    

    this.jpRoot.add(jPanel1, BorderLayout.SOUTH);
    //raDataIntegrity.installFor(this).setProtectedColumns(new String[]{"OSNOVICA"});
    this.setVisibleCols(new int[] {0,1,2});
    

  }

  public boolean Validacija2(char mode) {
    return true;
  }
  
  public void SetFokus(char mode) {
  
    super.SetFokus(mode);
    if (mode == 'N')
    	jrfNAZDOK.setText("");
  }

}