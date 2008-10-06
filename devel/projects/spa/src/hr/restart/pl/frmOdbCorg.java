/****license*****************************************************************
**   file: frmOdbCorg.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.PVROdbCorg;
import hr.restart.baza.dM;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;


public class frmOdbCorg extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  private short cvrodb;
  jpOdbCorg jpDetail;


  public frmOdbCorg() {
    super(2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      rcc.EnabDisabAll(jpDetail, true);
      jpDetail.jlrCorg.requestFocus();
    } else if (mode == 'I') {
      rcc.EnabDisabAll(jpDetail, false);
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jlrCorg) || vl.isEmpty(jpDetail.jlrCpov))
      return false;
//    if (mode == 'N' && vl.notUnique(new JTextComponent[] {jpDetail.jlrCpov,jpDetail.jlrCorg})) 
//      return false;
    if (mode == 'N') getRaQueryDataSet().setShort("CVRODB", cvrodb);
    return true;
  }

  private void jbInit() throws Exception {
    this.setVisibleCols(new int[] {0, 1});
    jpDetail = new jpOdbCorg(this);
    this.setRaDetailPanel(jpDetail);
    getJpTableView().addTableModifier(new raTableColumnModifier("CPOV", 
      new String[] {"CPOV", "NAZPOV"}, dm.getPovjerioci()));
    getJpTableView().addTableModifier(new raTableColumnModifier("CORG", 
      new String[] {"CORG", "NAZIV"}, dm.getOrgstruktura()));
      
  }
  
  public void setCvrodb(short _cvrodb) {
    cvrodb = _cvrodb;
    setRaQueryDataSet(PVROdbCorg.getDataModule().getFilteredDataSet(Condition.equal("CVRODB",cvrodb)));
    jpDetail.BindComponents(getRaQueryDataSet());
    setTitle("Povjerioci za druge OJ - vrsta odbitka: "+cvrodb);
  }
}
