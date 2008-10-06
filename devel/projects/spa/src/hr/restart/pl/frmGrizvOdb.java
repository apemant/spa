/****license*****************************************************************
**   file: frmGrizvOdb.java
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
import hr.restart.util.raMatPodaci;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;


public class frmGrizvOdb extends raMatPodaci {
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

  jpGrizvOdb jpDetail;


  public frmGrizvOdb(frmIzvjDef f, QueryDataSet tempParamQDS, short cizv, short cgrizv) {
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
      jpDetail.jlrCvrodb.forceFocLost();
      jpDetail.jlrCvrodb.requestFocus();
    } else if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jlrCvrodb, true);
      rcc.setLabelLaF(jpDetail.jlrNaziv, true);
      rcc.setLabelLaF(jpDetail.jbSelCvrodb, true);
      jpDetail.jlrCvrodb.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    getRaQueryDataSet().setShort("CIZV", cizv);
    getRaQueryDataSet().setShort("CGRIZV", cgrizv);
    if (vl.isEmpty(jpDetail.jlrCvrodb))
      return false;
    if (notUnique())
     {
       this.jpDetail.jlrCvrodb.requestFocus();
       JOptionPane.showConfirmDialog(this.jpDetail,"Zapis postoji !", "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
       return false;
      }
    return true;
  }

  private boolean notUnique()
  {
    return plUtil.getPlUtil().checkGrizvOdbUnique(cizv, cgrizv, getRaQueryDataSet().getShort("CVRODB"));
  }

  private void jbInit() throws Exception {
//    int x= (this.getToolkit().getDefaultToolkit().getScreenSize().width)-630;
//    int y= (this.getToolkit().getDefaultToolkit().getScreenSize().height)-260;
//    this.setLocation((int)x/2,(int)y/2);
//    this.getJpTableView().setMinimumSize(new Dimension(630, 150));
//    this.setSize(630, 315);
    this.setRaQueryDataSet(paramQDS);
    this.setTitle("Vrste odbitaka za grupu "+ fIzvjDef.getDetailSet().getString("NAZIV").toLowerCase());
    getJpTableView().addTableModifier(
        new hr.restart.swing.raTableColumnModifier("CVRODB",new String[] {"CVRODB","OPISVRODB"},dm.getVrsteodb())
      );
    this.setVisibleCols(new int[] {2});
    jpDetail = new jpGrizvOdb(this);
    this.setRaDetailPanel(jpDetail);
  }

  public boolean ValidacijaPrijeIzlaza(){
      fIzvjDef.raDetail.setEnabled(true);
      fIzvjDef.raMaster.setEnabled(true);
    return true;
   }
}
