/****license*****************************************************************
**   file: frmObrPrimanja.java
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
import hr.restart.util.Util;
import hr.restart.util.raLocalTransaction;
import hr.restart.zapod.OrgStr;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;

public class frmObrPrimanja extends frmObradaPL {
  raIniciranje inicir = raIniciranje.getInstance();
  OrgStr ors = OrgStr.getOrgStr();
  private String corg;
  public frmObrPrimanja() {
  }

  public boolean Validacija() {
    if (!super.Validacija()) return false;
    corg = jlrCorg.getText();
    if (!inicir.isInitObr(this)) {
      JOptionPane.showMessageDialog(jp,"Obrada nije inicirana!","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    } else {
      return JOptionPane.showOptionDialog(jp,
          "Prera\u010Dunati primanja za org. jedinicu "+corg+" i sve niže?",
          "Obra\u010Dun primanja",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null
          ) == 0;
    }
  }

  public void doAfterLookup() {
    setEnableCorg(true);
    setEnableObr(false);
  }
  boolean succ=false;

  public void okPress() {
    succ = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        QueryDataSet radpl = Util.getNewQueryDataSet("SELECT * FROM radnicipl where corg in "+
            ors.getInQuery(ors.getOrgstrAndKnjig(corg)));
        raCalcPrimanja.getRaCalcPrimanja().calcPrimanja(radpl,null,true);
        return true;
      }
    }.execTransaction();
  }
  public void showMessage() {
    frmObrPor.showMessage(this,"Prera\u010Dun primanja","an",succ);
  }
}