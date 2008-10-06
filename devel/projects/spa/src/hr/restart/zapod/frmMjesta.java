/****license*****************************************************************
**   file: frmMjesta.java
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
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;


public class frmMjesta extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpMjesta jpDetail;


  public frmMjesta() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraCmjesta, false);
    }
  }

  private void findNextCmjesta() {
    vl.execSQL("SELECT MAX(cmjesta) as next FROM mjesta");
    vl.RezSet.open();
    if (vl.RezSet.rowCount() < 1) getRaQueryDataSet().setInt("CMJESTA", 1);
    else getRaQueryDataSet().setInt("CMJESTA", vl.RezSet.getInt("NEXT") + 1);
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jlrCzup.forceFocLost();
      jpDetail.jlrCzem.forceFocLost();
      findNextCmjesta();
      jpDetail.jraNazmjesta.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraNazmjesta.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jraCmjesta) || vl.isEmpty(jpDetail.jraNazmjesta))
      return false;
    if (mode == 'N' && vl.notUnique(jpDetail.jraCmjesta))
      return false;
    return true;
  }

  private void jbInit() throws Exception {
    dm.getAllMjesta().open();
    this.setRaQueryDataSet(dm.getAllMjesta());
    this.setVisibleCols(new int[] {0, 1, 2, 3});
    jpDetail = new jpMjesta(this);
    jpDetail.BindComponents(getRaQueryDataSet());
    this.setRaDetailPanel(jpDetail);
  }
}
