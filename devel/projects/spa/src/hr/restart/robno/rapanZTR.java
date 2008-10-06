/****license*****************************************************************
**   file: rapanZTR.java
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

import hr.restart.util.raMatPodaci;

import javax.swing.JPanel;

import com.borland.dbswing.JdbTextField;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;



public class rapanZTR extends raMatPodaci {

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  JPanel panZTR = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JdbTextField jdbTextField1 = new JdbTextField();

  public rapanZTR() {
    super(2);

    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  void jbInit() throws Exception {

    jdbTextField1.setColumnName("PZT");
    jdbTextField1.setDataSet(dm.getVtzavtr());
    panZTR.setLayout(xYLayout1);
    panZTR.add(jdbTextField1, new XYConstraints(81, 21, 103, 23));
    setRaQueryDataSet (dm.getVtzavtr());
    this.setRaDetailPanel(panZTR);
  }
  public boolean Validacija(char metod){
    return true ;
  }
  public void SetFokus(char metod){

  }
}