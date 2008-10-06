/****license*****************************************************************
**   file: frmDefvaluedok.java
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
package hr.restart.sisfun;

import hr.restart.baza.dM;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import javax.swing.text.JTextComponent;


public class frmDefvaluedok extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpDefvaluedok jpDetail;


  public frmDefvaluedok() {
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
      rcc.setLabelLaF(jpDetail.jlrVrdok, false);
      rcc.setLabelLaF(jpDetail.jlrNazdok, false);
      rcc.setLabelLaF(jpDetail.jbSelVrdok, false);
      rcc.setLabelLaF(jpDetail.jraKljuc, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jlrVrdok.forceFocLost();
      jpDetail.jlrVrdok.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraDefvalue.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jlrVrdok) || vl.isEmpty(jpDetail.jraKljuc))
      return false;
    if (mode == 'N' && vl.notUnique(new JTextComponent[] {jpDetail.jlrVrdok, jpDetail.jraKljuc}))
      return false;
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getDefvaluedok());
    this.setVisibleCols(new int[] {0, 1, 2});
    jpDetail = new jpDefvaluedok(this);
    this.setRaDetailPanel(jpDetail);
  }
}
