/****license*****************************************************************
**   file: frmRepgk.java
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
package hr.restart.gk;

import hr.restart.baza.dM;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;

import com.borland.dx.dataset.Column;


public class frmRepgk extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpRepgkMaster jpMaster;
  jpRepgkDetail jpDetail;


  public frmRepgk() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPointMaster(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jpMaster.jraCrepgk, false);
    }
  }
  
  public void EntryPointDetail(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraRbsrepgk, false);
    }
  }


  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      jpMaster.jraCrepgk.requestFocusLater();
    } else if (mode == 'I') {
      jpMaster.jraTitle.requestFocusLater();
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jraCrepgk) || vl.isEmpty(jpMaster.jraTitle))
      return false;
    return true;
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      jpDetail.jraRbsrepgk.requestFocusLater();
    } else if (mode == 'I') {
      jpDetail.jraDescription.requestFocusLater();
    }
  }

  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jpDetail.jraRbsrepgk) || vl.isEmpty(jpDetail.jraDescription))
      return false;
    if (mode == 'N' && notUnique())
      return false;
    return true;
  }

  public boolean notUnique() {
    boolean exist = vl.chkExistsSQL(new Column[] {
        getDetailSet().getColumn("CREPGK"),
        getDetailSet().getColumn("RBSREPGK"),
    }, new String[] {
        (getDetailSet().getInt("CREPGK")+""),
        (getDetailSet().getInt("RBSREPGK")+"")
    });
    if (exist) {
      vl.showValidErrMsg(jpDetail.jraRbsrepgk, 'U');
      return true;
    }
    return false;
    //return vl.notUnique(new JTextComponent[] {jpDetail.jraRbsrepgk,jpMaster.jraCrepgk});
  }

  private void jbInit() throws Exception {
    this.setMasterSet(dm.getRepgk());
    this.setNaslovMaster("Definicije izvještaja GK");
    this.setVisibleColsMaster(new int[] {0, 1});
    this.setMasterKey(new String[] {"CREPGK"});
    setMasterDeleteMode(raMasterDetail.DELDETAIL);
    jpMaster = new jpRepgkMaster(this);
    this.setJPanelMaster(jpMaster);

    this.setDetailSet(dm.getDetrepgk());
    this.setNaslovDetail("Stavke izvještaja GK");
    this.setVisibleColsDetail(new int[] {1, 2});
    this.setDetailKey(new String[] {"CREPGK", "RBSREPGK"});
    jpDetail = new jpRepgkDetail(this);
    this.setJPanelDetail(jpDetail);
  }
}
