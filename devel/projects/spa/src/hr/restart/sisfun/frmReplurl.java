/****license*****************************************************************
**   file: frmReplurl.java
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
package hr.restart.sisfun;

import hr.restart.baza.dM;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;


public class frmReplurl extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpReplurl jpDetail;


  public frmReplurl() {
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
      rcc.setLabelLaF(jpDetail.jraRbr_url, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jraRbr_url.requestFocus();
    } else if (mode == 'I') {
//      jpDetail.jcbUrl.requestFocus();
      jpDetail.jcbUrl.setSelectedIndex(0);
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jraRbr_url) || vl.isEmpty(jpDetail.jraUsr) || vl.isEmpty(jpDetail.jraPass))
      return false;
    if (mode == 'N' && vl.notUnique(jpDetail.jraRbr_url))
      return false;
    this.getRaQueryDataSet().setString("URL", jpDetail.jcbUrl.getSelectedItem().toString());
    this.getRaQueryDataSet().setString("DRIVER", jpDetail.jcbDriver.getSelectedItem().toString());
    return true;

  }

  public boolean BeforeDelete()
  {
    if(this.getRaQueryDataSet().getShort("RBR_URL")==0)
    {
      JOptionPane.showConfirmDialog(this,"Slog s identifikatorm \"0\" nije mogu\u0107e brisati !",
                                      "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
        return false;
    }

    if(!checkUnique())
    {
      JOptionPane.showConfirmDialog(this,"URL ima zapis u tablici \"REPLDEF\" !",
                                      "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  private boolean checkUnique()
  {
    String qStr = "select * from repldef where rbr_url="+this.getRaQueryDataSet().getShort("RBR_URL")+
                  " or rbr_url_u="+this.getRaQueryDataSet().getShort("RBR_URL");
    QueryDataSet qds = Util.getNewQueryDataSet(qStr, true);
    if(qds.getRowCount()>0)
      return false;
    return true;
  }
  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getReplurl());
    this.setVisibleCols(new int[] {0, 1});
    jpDetail = new jpReplurl(this);
    this.setRaDetailPanel(jpDetail);
  }
}
