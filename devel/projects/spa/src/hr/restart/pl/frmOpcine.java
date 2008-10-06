/****license*****************************************************************
**   file: frmOpcine.java
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
package hr.restart.pl;

import hr.restart.baza.dM;
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class frmOpcine extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();


  jpOpcine jpDetail;

  raNavAction rnvVrOdb = new raNavAction("Odbici", raImages.IMGHISTORY, KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      vrOdb_action();
    }
  };

  public frmOpcine() {
    super(2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraCopcine, false);
    }
  }

  public boolean BeforeDelete()
  {
    plUtil.getPlUtil().deleteStandOdb("OP", this.getRaQueryDataSet().getString("COPCINE"));
//    if(plUtil.getPlUtil().checkOpcineStavke(getRaQueryDataSet().getString("COPCINE")))
//    {
//      JOptionPane.showConfirmDialog(this, "Nisu pobrisane stavke !", "Greška!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
//      return false;
//    }
    return true;
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jlrCzup.forceFocLost();
      jpDetail.jraCopcine.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraNazivop.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jraCopcine) || vl.isEmpty(jpDetail.jraNazivop) || vl.isEmpty(jpDetail.jlrCzup))
      return false;
    if (mode == 'N' && vl.notUnique(jpDetail.jraCopcine))
      return false;
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getOpcine());
    this.setVisibleCols(new int[] {0, 1, 2});
    jpDetail = new jpOpcine(this);
    this.addOption(rnvVrOdb, 3);
    this.setRaDetailPanel(jpDetail);
    raDataIntegrity.installFor(this);
  }

  void vrOdb_action() {
    frmGlobalMaster fGM = new frmGlobalMaster(this, "OP", getRaQueryDataSet().getString("COPCINE"),"OP");
    fGM.show();
  }

  public void AfterSave(char mode)
  {
    if(mode=='N')
    {
      plUtil.getPlUtil().addStandOdbici("OP", getRaQueryDataSet().getString("COPCINE"));
    }
  }
}
