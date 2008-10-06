/****license*****************************************************************
**   file: jpGetVrsteNaloga.java
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
package hr.restart.gk;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpGetVrsteNaloga extends JPanel {
//  raXYLayout xYLay = new raXYLayout(false);
  XYLayout xYLay = new XYLayout();
  JlrNavField jlrCVRNAL = new JlrNavField() {
    public void after_lookUp() {
      after_lookup_vrn();
    }
  };
  JLabel jlVrsteNaloga = new JLabel();
  JlrNavField jlrOPISVRNAL = new JlrNavField() {
    public void after_lookUp() {
      after_lookup_vrn();
    }
  };
  JLabel jlCVRNAL = new JLabel();
  JLabel jlOPISVRNAL = new JLabel();
  JraButton jBgetVrsteNal = new JraButton();
  dM dm;
  private com.borland.dx.dataset.StorageDataSet dataSet;

  public jpGetVrsteNaloga() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    dm = dM.getDataModule();
    jlrCVRNAL.setColumnName("CVRNAL");
    jlrCVRNAL.setColNames(new String[] {"OPISVRNAL"});
    jlrCVRNAL.setVisCols(new int[] {0,1});
    jlrCVRNAL.setTextFields(new javax.swing.text.JTextComponent[] {jlrOPISVRNAL});
    jlrCVRNAL.setRaDataSet(dm.getVrstenaloga());
    jlrCVRNAL.setSearchMode(0);
    jlrCVRNAL.setNavProperties(null);
    jlrCVRNAL.setNavButton(jBgetVrsteNal);
    jlrOPISVRNAL.setColumnName("OPISVRNAL");
    jlrOPISVRNAL.setSearchMode(1);
    jlrOPISVRNAL.setNavProperties(jlrCVRNAL);
    setLayout(xYLay);
    jlVrsteNaloga.setText("Vrsta naloga");
    xYLay.setWidth(555);
    xYLay.setHeight(42);
    jlCVRNAL.setText("Oznaka");
    jlOPISVRNAL.setText("Opis");
//    this.setPreferredSize(new Dimension(518, 42));
//    add(jlVrsteNaloga,  new raXYConstraints(15, 20, -1, -1));
//    add(jlrCVRNAL,  new raXYConstraints(150, 20, 80, -1));
//    add(jlrOPISVRNAL,    new raXYConstraints(235, 20, 250, -1));
//    add(jlCVRNAL,   new raXYConstraints(150, 5, -1, -1));
//    add(jlOPISVRNAL,  new raXYConstraints(235, 5, -1, -1));
//    add(jBgetVrsteNal,  new raXYConstraints(490, 20, 21, 21));


    add(jlVrsteNaloga,  new XYConstraints(15, 20, -1, -1));
    add(jlrCVRNAL,   new XYConstraints(150, 20, 100, -1));
    add(jlrOPISVRNAL,         new XYConstraints(255, 20, 265, -1));
    add(jlCVRNAL,   new XYConstraints(150, 5, -1, -1));
    add(jlOPISVRNAL,   new XYConstraints(255, 5, -1, -1));
    add(jBgetVrsteNal,     new XYConstraints(525, 20, 21, 21));
  }

  public void setDataSet(com.borland.dx.dataset.StorageDataSet dataSetC) {
    dataSet = dataSetC;
    jlrCVRNAL.setDataSet(dataSet);
  }

  public com.borland.dx.dataset.StorageDataSet getStorageDataSet() {
    return dataSet;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return (com.borland.dx.sql.dataset.QueryDataSet)dataSet;
  }
  
  public void after_lookup_vrn() {
    
  }

}