/****license*****************************************************************
**   file: frmZemlje.java
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
package hr.restart.blpn;

import hr.restart.baza.dM;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;


public class frmZemlje extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpZemlje jpDetail;


  public frmZemlje() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    jpDetail.rcbIndPuta.requestFocus();
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraCzemlje, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jlrOznval.forceFocLost();
      jpDetail.jraCzemlje.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraNazivzem.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    jpDetail.rcbIndPuta.this_itemStateChanged();
    if (vl.isEmpty(jpDetail.jraCzemlje) || vl.isEmpty(jpDetail.jlrOznval) || vl.isEmpty(jpDetail.jraDnevnica))
      return false;
    if (mode == 'N' && vl.notUnique(jpDetail.jraCzemlje))
      return false;
    return true;
  }

  public void AfterAfterSave(char mode){
    super.AfterAfterSave(mode);
    if (mode == 'N'){
      jpDetail.rcbIndPuta.setSelectedItem("Inozemstvo");
    }
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getZemlje());
    this.setVisibleCols(new int[] {0, 1, 2});
    jpDetail = new jpZemlje(this);
    this.setRaDetailPanel(jpDetail);
  }
}
