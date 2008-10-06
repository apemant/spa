/****license*****************************************************************
**   file: frmVrNaloga.java
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

import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.sysoutTEST;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmVrNaloga extends raMatPodaci {
sysoutTEST ST = new sysoutTEST(false);
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  com.borland.dx.sql.dataset.QueryDataSet vrnaloga;
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlCVRNAL = new JLabel();
  JraTextField jrCVRNAL = new JraTextField();
  JLabel jlOPISVRNAL = new JLabel();
  JraTextField jrOPISVRNAL = new JraTextField();
  Valid vl = Valid.getValid();
  raCommonClass rCC = raCommonClass.getraCommonClass();

  public frmVrNaloga() {
    super(2);
    try {
    ST.showMembers(com.borland.jb.util.TriStateProperty.class);
    dm.getVrstenaloga().getColumn("CVRNAL").getVisible();
    dm.getVrstenaloga().getColumn("OPISVRNAL").getVisible();
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokus(char mod) {
    if (mod == 'N') {
      rCC.setLabelLaF(jrCVRNAL,true);
      jrCVRNAL.requestFocus();
    } else if (mod == 'I') {
      rCC.setLabelLaF(jrCVRNAL,false);
      jrOPISVRNAL.requestFocus();
    }
  }

  public boolean Validacija(char mod) {
    if (mod == 'N') {
      if (vl.notUnique(jrCVRNAL)) {
        return false;
      }
      return !vl.isEmpty(jrOPISVRNAL);
    } else if (mod == 'I') {
      return !vl.isEmpty(jrOPISVRNAL);
    }
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getVrstenaloga());
    jlCVRNAL.setText("Oznaka");
    xYLayout1.setWidth(469);
    xYLayout1.setHeight(86);
    jp.setLayout(xYLayout1);
    jrCVRNAL.setColumnName("CVRNAL");
    jrCVRNAL.setDataSet(getRaQueryDataSet());
    jlOPISVRNAL.setText("Opis");
    jrOPISVRNAL.setDataSet(getRaQueryDataSet());
    jrOPISVRNAL.setColumnName("OPISVRNAL");
    jp.add(jlCVRNAL,  new XYConstraints(15, 20, -1, -1));
    jp.add(jrCVRNAL,   new XYConstraints(150, 20, 100, -1));
    jp.add(jlOPISVRNAL,  new XYConstraints(15, 45, -1, -1));
    jp.add(jrOPISVRNAL,    new XYConstraints(150, 45, 300, -1));
    setVisibleCols(new int[] {0,1});
    setRaDetailPanel(jp);
  }
}