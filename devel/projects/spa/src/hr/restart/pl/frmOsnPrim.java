/****license*****************************************************************
**   file: frmOsnPrim.java
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


public class frmOsnPrim extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  QueryDataSet paramDS = new QueryDataSet();
  short cvrp=0;
  frmVrstePrim fVP;
  jpOsnPrim jpDetail;


  public frmOsnPrim(frmVrstePrim f, QueryDataSet _paramDS, short _cvrp) {
    super(2);
    fVP = f;
    paramDS = _paramDS;
    cvrp = _cvrp;
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
//      rcc.setLabelLaF(jpDetail.jlrCosn, false);
//      rcc.setLabelLaF(jpDetail.jlrOpis, false);
//      rcc.setLabelLaF(jpDetail.jbSelCosn, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jlrCosn.forceFocLost();
      jpDetail.jlrCosn.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jlrCosn.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    getRaQueryDataSet().setShort("CVRP", cvrp);
    if (vl.isEmpty(jpDetail.jlrCosn))
      return false;
    if (mode == 'N' && notUnique())
    {
      this.jpDetail.jlrCosn.requestFocus();
      JOptionPane.showConfirmDialog(jpDetail, "Zapis postoji !", "Greška!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  private boolean notUnique()
  {
    return plUtil.getPlUtil().checkOsnPrimUnique(getRaQueryDataSet().getShort("CVRP"),
                                                  getRaQueryDataSet().getShort("COSN"));
  }

  private void jbInit() throws Exception {
//    this.setTitle("Osnovice za vrstu primanja");
    this.setTitle("Osnovice za vrstu primanja "+ fVP.getRaQueryDataSet().getString("NAZIV").toLowerCase());
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
      new raTableColumnModifier("COSN",new String[] {"COSN","OPIS"},dm.getPlosnovice())
    );
    this.setVisibleCols(new int[] { 1});
    jpDetail = new jpOsnPrim(this);
    this.setRaDetailPanel(jpDetail);
  }

   public boolean ValidacijaPrijeIzlaza(){
      fVP.setEnabled(true);
      return true;
   }
}
