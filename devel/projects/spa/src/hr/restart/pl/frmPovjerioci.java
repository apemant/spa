/****license*****************************************************************
**   file: frmPovjerioci.java
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
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raMnemonics;
import hr.restart.util.raNavAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


public class frmPovjerioci extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  jpPovjerioci jpDetail;

  raNavAction rnvMnem = new raNavAction("Mnemonici",raImages.IMGTIP,KeyEvent.VK_F1) {
    public void actionPerformed(ActionEvent e) {
      helpMnem();
    }
  };
  public frmPovjerioci() {
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
      rcc.setLabelLaF(jpDetail.jraCpov, false);
      jpDetail.f9FlagNP=false;
      jpDetail.f9FlagS=false;
      jpDetail.f9FlagZ=false;
      jpDetail.f9FlagPBO=false;
      jpDetail.f9FlagPBZ=false;
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jraCpov.requestFocus();
      jpDetail.jrbNacIsp.setSelectedIndex(0);
    } else if (mode == 'I') {
      jpDetail.jraNazpov.requestFocus();
    }
  }
//jraPnbo2
  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jraCpov) || vl.isEmpty(jpDetail.jraNazpov)  || vl.isEmpty(jpDetail.jraMjesto) || vl.isEmpty(jpDetail.jraPnbo2) || vl.isEmpty(jpDetail.jraAdresa) || vl.isEmpty(jpDetail.jraZiro) || vl.isEmpty(jpDetail.jraSvrha))
      return false;
    if (mode == 'N' && vl.notUnique(jpDetail.jraCpov))
      return false;
    return true;
  }

  private void jbInit() throws Exception {
    this.addOption(rnvMnem, 4);
    this.setRaQueryDataSet(dm.getPovjerioci());
    this.setVisibleCols(new int[] {0, 1, 13});
    jpDetail = new jpPovjerioci(this);
    this.setRaDetailPanel(jpDetail);
    raDataIntegrity.installFor(this);
  }

  private void helpMnem()
  {
    raVirPlMnWorker worker = raVirPlMnWorker.getInstance();
    raMnemonics.getVarTextDlg(worker.getID(),"",this, raMnemonics.GETVARNAME);
  }
}
