/****license*****************************************************************
**   file: frmSumePrim.java
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

import java.awt.Dimension;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;


public class frmSumePrim extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  QueryDataSet paramDS = new QueryDataSet();
  short cvrp=0;
  frmVrstePrim fVP;

  jpSumePrim jpDetail;


  public frmSumePrim(frmVrstePrim f, QueryDataSet _paramDS, short _cvrp) {
    super(2);
    paramDS = _paramDS;
    cvrp = _cvrp;
    fVP = f;
    fVP.setEnabled(false);
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
//      rcc.setLabelLaF(jpDetail.jlrCsume, false);
//      rcc.setLabelLaF(jpDetail.jlrOpis, false);
//      rcc.setLabelLaF(jpDetail.jbSelCsume, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jlrCsume.forceFocLost();
      jpDetail.jlrCsume.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jlrCsume.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    getRaQueryDataSet().setShort("CVRP", cvrp);
//    sysoutTEST ST = new sysoutTEST(false);
//    ST.prn(getRaQueryDataSet());
    if (vl.isEmpty(jpDetail.jlrCsume))
      return false;
    if (mode == 'N' && notUnique())
    {
      this.jpDetail.jlrCsume.requestFocus();
      JOptionPane.showConfirmDialog(jpDetail, "Zapis postoji !", "Greška!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  private boolean notUnique()
  {
    return plUtil.getPlUtil().checkSumePrimUnique(getRaQueryDataSet().getShort("CVRP"),
                                                  getRaQueryDataSet().getInt("CSUME"));
  }
  private void jbInit() throws Exception {
    this.setTitle("Sume za vrstu primanja "+ fVP.getRaQueryDataSet().getString("NAZIV").toLowerCase());
    this.setSize(625, 325);
    this.getJpTableView().setMinimumSize(new Dimension(630, 150));
    int x= (this.getToolkit().getDefaultToolkit().getScreenSize().width)-630;
    int y= (this.getToolkit().getDefaultToolkit().getScreenSize().height)-260;
    this.setLocation((int)x/2,(int)y/2);

    this.setRaQueryDataSet(paramDS);
    getJpTableView().addTableModifier(
      new raTableColumnModifier("CVRP",new String[] {"CVRP","NAZIV"},dm.getVrsteprim())
    );
    getJpTableView().addTableModifier(
      new raTableColumnModifier("CSUME",new String[] {"CSUME","OPIS"},dm.getSume())
    );
    this.setVisibleCols(new int[] {1});
    jpDetail = new jpSumePrim(this);
    this.setRaDetailPanel(jpDetail);
  }

   public boolean ValidacijaPrijeIzlaza(){
      fVP.setEnabled(true);
      return true;
   }
}