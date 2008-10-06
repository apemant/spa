/****license*****************************************************************
**   file: frmRNHints.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.RN_znachint;
import hr.restart.baza.dM;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;


public class frmRNHints extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpRNHints jpDetail;
  short cvrsubj;

  public frmRNHints() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setVrsub(short _cvrs) {
    cvrsubj = _cvrs;
    RN_znachint.getDataModule().setFilter(Condition.equal("CVRSUBJ", _cvrs));
    getRaQueryDataSet().open();
  }

  public void SetFokus(char mode) {
    if (mode != 'B') {
      jpDetail.jraZhint.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jraZhint))
      return false;

    return true;
  }

  public boolean doBeforeSave(char mode) {
    try {
      if (mode == 'N') {
        vl.execSQL("SELECT MAX(chint) as hnext FROM RN_znachint WHERE cvrsubj="+cvrsubj);
        vl.RezSet.open();
        getRaQueryDataSet().setShort("CHINT", (short) (vl.RezSet.getShort("HNEXT") + 1));
        getRaQueryDataSet().setShort("CVRSUBJ", cvrsubj);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private void jbInit() throws Exception {
    dm.getRN_znachint().open();
    this.setRaQueryDataSet(dm.getRN_znachint());
    this.setVisibleCols(new int[] {2});
    jpDetail = new jpRNHints(this);
    this.setRaDetailPanel(jpDetail);
    jpDetail.BindComponents(this.getRaQueryDataSet());
  }
}
