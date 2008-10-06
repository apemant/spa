/****license*****************************************************************
**   file: frmVrodn.java
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
import hr.restart.util.raNavAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;


public class frmVrodn extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpVrodn jpDetail;

// definicija rnv buttona
  raNavAction rnvVrOdb = new raNavAction("Odbici",raImages.IMGHISTORY,KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      vrOdb_action();
    }
  };

// konstruktor
  public frmVrodn() {
    super(2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

// provjera stavki prije brisanja
  public boolean BeforeDelete()
  {
    plUtil.getPlUtil().deleteStandOdb("VR", this.getRaQueryDataSet().getString("CVRO"));
//     if(plUtil.getPlUtil().checkVrOdnStavke(getRaQueryDataSet().getString("CVRO")))
//    {
//      JOptionPane.showConfirmDialog(this, "Nisu pobrisane stavke !", "Greška!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
//      return false;
//    }
    return true;
  }


  public void EntryPoint(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraCvro, false);
    }
  }

// setiranje fokusa ovisno o moduu rada
  public void SetFokus(char mode) {
    if (mode == 'N') {
      getRaQueryDataSet().setBigDecimal("KOEF", new BigDecimal("100"));
      jpDetail.jraCvro.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraNazivro.requestFocus();
    }
  }

// validacija
  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jraCvro) || vl.isEmpty(jpDetail.jraNazivro))
      return false;
    if (mode == 'N' && vl.notUnique(jpDetail.jraCvro))
      return false;
    return true;
  }

// init
  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getVrodn());
    this.setVisibleCols(new int[] {0, 1, 2});
    jpDetail = new jpVrodn(this);
    this.addOption(rnvVrOdb, 3);
    this.setRaDetailPanel(jpDetail);
    raDataIntegrity.installFor(this);
  }

// pozivanje globalne klase Odbitaka ovisno o parametru (vrsti odbitaka) preko rnv buttona s predefiniranim dataset-om
  void vrOdb_action() {
    frmGlobalMaster fGM = new frmGlobalMaster(this, "VR", getRaQueryDataSet().getString("CVRO"),"VR");
    fGM.show();
  }

  public void AfterSave(char mode)
{
  if(mode=='N')
  {
    plUtil.getPlUtil().addStandOdbici("VR", getRaQueryDataSet().getString("CVRO"));
  }
  }
}
