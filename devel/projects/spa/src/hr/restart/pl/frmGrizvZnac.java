/****license*****************************************************************
**   file: frmGrizvZnac.java
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
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raKeyAction;
import hr.restart.util.raMatPodaci;

import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;


public class frmGrizvZnac extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  QueryDataSet paramQDS ;
  short cizv=0;
  short cgrizv=0;
  short cvrodb = 0;
  boolean param2 = false;
  frmIzvjDef fIzvjDef;
  frmVrstePrim fVP;

  jpGrizvZnac jpDetail;


  public frmGrizvZnac(frmIzvjDef f, QueryDataSet tempParamQDS, short cizv, short cgrizv) {
    super(2);
    try {
      fIzvjDef = f;
      paramQDS = tempParamQDS;
      fIzvjDef.raDetail.setEnabled(false);
      fIzvjDef.raMaster.setEnabled(false);
      this.cizv = cizv;
      this.cgrizv = cgrizv;

      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jlrCZnac.forceFocLost();
      jpDetail.jlrCZnac.requestFocus();
    } else if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jlrCZnac, true);
      rcc.setLabelLaF(jpDetail.jlrNaziv, true);
      rcc.setLabelLaF(jpDetail.jbSelCvrodb, true);
      jpDetail.jlrCZnac.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    getRaQueryDataSet().setShort("CIZV", cizv);
    getRaQueryDataSet().setShort("CGRIZV", cgrizv);
    if (vl.isEmpty(jpDetail.jlrCZnac))
      return false;
    if (notUnique())
     {
       this.jpDetail.jlrCZnac.requestFocus();
       JOptionPane.showConfirmDialog(this.jpDetail,"Zapis postoji !", "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
       return false;
      }
    return true;
  }

  private boolean notUnique()
  {
    return plUtil.getPlUtil().checkGrizvZnacUnique(cizv, cgrizv, getRaQueryDataSet().getShort("CZNAC"));
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(paramQDS);
    this.setTitle("Podaci za grupu "+ fIzvjDef.getDetailSet().getString("NAZIV").toLowerCase());
    getJpTableView().addTableModifier(
        new hr.restart.swing.raTableColumnModifier("CZNAC",new String[] {"CZNAC","ZNACOPIS"},dm.getPlZnacRad())
      );
    this.setVisibleCols(new int[] {2});
    jpDetail = new jpGrizvZnac(this);
    this.setRaDetailPanel(jpDetail);
    addKeyAction(new raKeyAction(KeyEvent.VK_F12,"Vrijednost za radnika") {
      public void keyAction() {
        String cradnik = JOptionPane.showInputDialog(getRaDetailPanel(), "Oznaka radnika");
        if (cradnik !=  null)
        JOptionPane.showMessageDialog(getRaDetailPanel(),raIzvjestaji.getCustomData_radnici(cradnik, getRaQueryDataSet().getShort("CIZV"), getRaQueryDataSet().getShort("CGRIZV")));
      }
    });
    
  }

  public boolean ValidacijaPrijeIzlaza(){
      fIzvjDef.raDetail.setEnabled(true);
      fIzvjDef.raMaster.setEnabled(true);
    return true;
   }
}
