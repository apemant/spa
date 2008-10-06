/****license*****************************************************************
**   file: frmVrart.java
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

import hr.restart.baza.Vrart;
import hr.restart.baza.dM;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;


public class frmVrart extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpVrart jpDetail;


  public frmVrart() {
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
      rcc.setLabelLaF(jpDetail.jraCvrart, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jlrKontopot.forceFocLost();
      jpDetail.jlrKontopnp.forceFocLost();
      jpDetail.jlrKontopdv.forceFocLost();
      jpDetail.jlrKontopri.forceFocLost();
      jpDetail.jraCvrart.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraNazvrart.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jraCvrart) || vl.isEmpty(jpDetail.jraNazvrart))
      return false;
    if (mode == 'N' && vl.notUnique(jpDetail.jraCvrart))
      return false;
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(Vrart.getDataModule().copyDataSet());
    this.setVisibleCols(new int[] {0, 1, 2, 3, 4, 5});
    jpDetail = new jpVrart(this);
    this.setRaDetailPanel(jpDetail);
  }
}
