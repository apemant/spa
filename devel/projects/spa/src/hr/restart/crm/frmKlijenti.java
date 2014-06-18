/****license*****************************************************************
**   file: frmKlijenti.java
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
package hr.restart.crm;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.NavigationEvent;

import hr.restart.baza.Klijenti;
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.raMatPodaci;

public class frmKlijenti extends raMatPodaci {
  
  Valid vl = Valid.getValid();
  
  jpKlijent jp;

  public frmKlijenti() {
    super(2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    this.setRaQueryDataSet(Klijenti.getDataModule().getFilteredDataSet(""));
    jp = new jpKlijent();
    jp.BindComponents(getRaQueryDataSet());
    this.setRaDetailPanel(jp);
    this.setVisibleCols(new int[] {0,1,2,10});
    raDataIntegrity.installFor(this);
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      if (jp.rcbStatus.getSelectedIndex() < 0)
        jp.rcbStatus.setSelectedIndex(0);
      jp.rcbStatus.this_itemStateChanged();
      if (jp.rcbSegment.getSelectedIndex() < 0)
        jp.rcbSegment.setSelectedIndex(0);
      jp.rcbSegment.this_itemStateChanged();
    }
    if (mode != 'B') jp.jraNAZIV.requestFocus();
  }
  
  public void AfterCancel() {
    jp.setColor();
  }
  
  public void raQueryDataSet_navigated(NavigationEvent e) {
    jp.setColor();
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jp.jraNAZIV))
      return false;
    if (jp.jraOIB.isEmpty()) {
      if (JOptionPane.showConfirmDialog(this, "OIB nije upisan. Spremiti ipak?", 
          "Potvrda", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return false;
    } else if (vl.notUniqueUPD(jp.jraOIB, new String[] {"CKLIJENT"})) return false;
   
    return true;
  }
}