/****license*****************************************************************
**   file: frmZnacRad.java
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
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;


public class frmZnacRad extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpZnacRad jpDetail;


  public frmZnacRad() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jraZnac.requestFocus();
      getRaQueryDataSet().setString("ZNACTIP", "S");
      getRaQueryDataSet().setString("AKTIV", "D");
    } else if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraZnac, false);
      jpDetail.jraNaznac.requestFocusLater();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jraZnac))
      return false;
    if (mode == 'N' && vl.notUnique(jpDetail.jraZnac)) /**@todo: Provjeriti jedinstvenost kljuca */
      return false;
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getPlZnacRad());
    this.setVisibleCols(new int[] {0, 1});
    jpDetail = new jpZnacRad(this);
    this.setRaDetailPanel(jpDetail);
    jpDetail.BindComponents(getRaQueryDataSet());
  }
}
