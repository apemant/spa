/****license*****************************************************************
**   file: frmPartHistory.java
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


public class frmPartHistory extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpPartHistory jpDetail;


  public frmPartHistory() {
    super(2);
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
      rcc.setLabelLaF(jpDetail.jlrCpar, false);
      rcc.setLabelLaF(jpDetail.jlrNazpar, false);
      rcc.setLabelLaF(jpDetail.jbSelCpar, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jlrCpar.forceFocLost();
      jpDetail.jlrCpar.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraUkuprom.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jlrCpar))
      return false;
    if (mode == 'N' && vl.notUnique(jpDetail.jlrCpar))
      return false;
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getVTCparHist());
    this.setVisibleCols(new int[] {0, 1, 2});
    jpDetail = new jpPartHistory(this);
    this.setRaDetailPanel(jpDetail);
    this.getJpTableView().addTableModifier(
        new hr.restart.swing.raTableColumnModifier("CPAR", new String[] {
            "CPAR", "NAZPAR" }, dm.getPartneri()));
  }
}
