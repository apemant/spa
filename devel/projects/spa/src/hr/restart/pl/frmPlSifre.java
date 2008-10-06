/****license*****************************************************************
**   file: frmPlSifre.java
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
import hr.restart.util.Util;

import java.util.LinkedList;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;

public class frmPlSifre extends hr.restart.zapod.frmSifrarnici{

  dM dm = dM.getDataModule();
  QueryDataSet qds = new QueryDataSet();

  public frmPlSifre() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    this.setMasterSet(plUtil.getPlUtil().getSifrarniciDS());
    rebindMaster();
  }

  public boolean ValidacijaMaster(char mode)
  {
    if (!super.ValidacijaMaster(mode)) return false;
    if(mode=='N')
    {
      if(!this.getMasterSet().getString("VRSTASIF").startsWith("PL"))
      {
//        super.sifraRequestFocus();
        JOptionPane.showConfirmDialog(this.raMaster.getRaMasterDetail().getJPanelMaster(),"Šifra ne po\u010Dinje sa 'PL' !", "Greška!",
                                      JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
        return false;
      }
    }
    return true;
  }

//  Ako se extenda neka klasa i mijenja joj se dataset potrebno je pozvati ovu metodu
  private void rebindMaster() {
    LinkedList dbcomps = Util.getUtil().getDBComps(getJPanelMaster());
    for (int i = 0; i < dbcomps.size(); i++) {
      try {
        com.borland.dx.dataset.ColumnAware col = (com.borland.dx.dataset.ColumnAware)dbcomps.get(i);
        col.setDataSet(getMasterSet());
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

}