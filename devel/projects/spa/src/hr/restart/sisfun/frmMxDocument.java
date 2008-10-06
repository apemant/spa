/****license*****************************************************************
**   file: frmMxDocument.java
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


public class frmMxDocument extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpMxDocument jpDetail;


  public frmMxDocument() {
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
      rcc.setLabelLaF(jpDetail.jraCdoc, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jraCdoc.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraOpis.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jraCdoc))
      return false;
    if (mode == 'N' && vl.notUnique(jpDetail.jraCdoc))
      return false;
    return true;
  }

  private void jbInit() throws Exception {
    dm.getMxDokument().open();
    this.setRaQueryDataSet(dm.getMxDokument());
    this.setVisibleCols(new int[] {0, 1, 2, 3});
    jpDetail = new jpMxDocument(this);
    jpDetail.BindComponents(this.getRaQueryDataSet());
    this.setRaDetailPanel(jpDetail);

  }
}
