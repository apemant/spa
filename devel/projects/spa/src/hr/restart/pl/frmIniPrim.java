/****license*****************************************************************
**   file: frmIniPrim.java
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
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import com.borland.dx.dataset.NavigationEvent;


public class frmIniPrim extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  int RowPointer = 0;

  jpIniPrim jpDetail;


  public frmIniPrim() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if(mode=='N')
    {
      rcc.setLabelLaF(jpDetail.jraRbr, false);
      rcc.setLabelLaF(jpDetail.jcbSFond, true);
      jpDetail.jcbSFond.setSelectedIndex(0);
    }
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jlrCvrp, false);
      rcc.setLabelLaF(jpDetail.jlrNaziv, false);
      rcc.setLabelLaF(jpDetail.jbSelCvrp, false);
      rcc.setLabelLaF(jpDetail.jraRbr, false);
    }
//    jpDetail.nivoEntry();
  }
  private String cnivo = "";
  public void raQueryDataSet_navigated(NavigationEvent e) {
    cnivo = getRaQueryDataSet().getString("CNIVO");
  }
  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jlrCvrp.forceFocLost();
      jpDetail.jlrCvrp.requestFocus();
    } else if (mode == 'I') {
      if(getRaQueryDataSet().getString("SFOND").equals("X"))
        rcc.setLabelLaF(jpDetail.jraSati, true);
      else
        rcc.setLabelLaF(jpDetail.jraSati, false);
      jpDetail.jcbSFond.requestFocus();
    }
    jpDetail.nivoEntry(cnivo);
  }

  public boolean Validacija(char mode) {
      if (vl.isEmpty(jpDetail.jlrCvrp))
        return false;
      jpDetail.nivoBeforeSave();
      if (mode == 'N') {
        short rbr = plUtil.getPlUtil().getMaxIniPrimRBR(getRaQueryDataSet().getShort("CVRP"));
        getRaQueryDataSet().setShort("RBR", rbr);        
      }
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getAllIniprim());
    this.setVisibleCols(new int[] {0,1,2,3,4});
    jpDetail = new jpIniPrim(this);
    getJpTableView().addTableModifier(
      new raTableColumnModifier("CVRP", new String[]{"CVRP", "NAZIV"}, dm.getVrsteprim())
    );
    this.setRaDetailPanel(jpDetail);

  }

  public boolean BeforeDelete()
  {
    RowPointer = getRaQueryDataSet().getRow();
    delVrp = getRaQueryDataSet().getShort("CVRP");
    delRbr = getRaQueryDataSet().getShort("RBR");
    return true;
  }

  short delVrp;
  short delRbr;
  public void AfterDelete()
  {
    plUtil.getPlUtil().recalcIniPrimRBR(delVrp,delRbr);
/*    
    if(getRaQueryDataSet().getShort("RBR")==1 && getRaQueryDataSet().getRow()==0)
    {
      plUtil.getPlUtil().recalcIniPrimRBR(delVrp,
                                  this.getRaQueryDataSet().getShort("RBR"));
    }
    else
    {
        plUtil.getPlUtil().recalcIniPrimRBR(delVrp,
                                  (short)(getRaQueryDataSet().getShort("RBR")-1));
    }
*/
    this.getRaQueryDataSet().refresh();
    if (RowPointer == getRaQueryDataSet().getRowCount())
    {
      getRaQueryDataSet().goToRow(RowPointer-1);
    }
    else
    {
        getRaQueryDataSet().goToRow(RowPointer);
    }
  }
}
