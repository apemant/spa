/****license*****************************************************************
**   file: frmMenus.java
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
package hr.restart.util.menus;

import hr.restart.baza.Condition;
import hr.restart.baza.Menus;
import hr.restart.baza.dM;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import javax.swing.JOptionPane;


public class frmMenus extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpMenus jpDetail;

  public frmMenus() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokus(char mode) {
    jpDetail.setFocus(mode);
  }
  
  String cacheKey, cacheValue;
  public boolean BeforeDelete() {
    cacheKey = getRaQueryDataSet().getString("CMENU") + "#" +
              getRaQueryDataSet().getString("PARENTCMENU");
    return true;
  }
  
  public void AfterDelete() {    
    MenuFactory.getMenuProperties().remove(cacheKey);
  }
  
  public void AfterSave(char mode) {
    MenuFactory.getMenuProperties().setProperty(cacheKey, cacheValue);
  }
  
  public boolean Validacija(char mode) {
    if (!jpDetail.validate(mode)) return false;
    if (mode == 'N') {
      if (getRaQueryDataSet().getString("PARENTCMENU").length() == 0)
        getRaQueryDataSet().setString("PARENTCMENU", 
            getRaQueryDataSet().getString("CMENU"));
      if (Menus.getDataModule().getRowCount(Condition.whereAllEqual(
          new String[] {"CMENU", "PARENTCMENU"}, getRaQueryDataSet())) > 0) {
        jpDetail.jraCmenu.requestFocus();
        JOptionPane.showMessageDialog(this.getWindow(), "Identièna stavka izbornika veæ postoji!",
            "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    cacheKey = getRaQueryDataSet().getString("CMENU") + "#" +
                getRaQueryDataSet().getString("PARENTCMENU");
    cacheValue = jpDetail.getDescription();
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getMenus());
    dm.getMenus().open();
    this.setVisibleCols(new int[] {0, 2});
    jpDetail = new jpMenus(this);
    jpDetail.BindComponents(dm.getMenus());
    this.setRaDetailPanel(jpDetail);
  }
}
