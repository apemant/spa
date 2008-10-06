/****license*****************************************************************
**   file: frmSalKonUp.java
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
package hr.restart.sk;

import hr.restart.baza.dM;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;

import java.sql.Timestamp;


public class frmSalKonUp extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpSalKonUpMaster jpMaster;
  jpSalKonUpDetail jpDetail;

  presSalKonUp pres;


  public frmSalKonUp() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokusMaster(char mode) {
    Timestamp td = vl.getToday();
    if (mode == 'N') {
      pres.copySelValues();
      this.getMasterSet().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG());
      if (pres.jlrZiro.getText().equals(""))
        jpMaster.jlrZiro.requestFocus();
      else {
        rcc.setLabelLaF(jpMaster.jlrZiro, false);
        rcc.setLabelLaF(jpMaster.jbSelZiro, false);
        jpMaster.jraBrojizv.requestFocus();
      }
      if (td.before(pres.getSelRow().getTimestamp("DATUM-from")))
        td = pres.getSelRow().getTimestamp("DATUM-from");
      if (td.after(pres.getSelRow().getTimestamp("DATUM-to")))
        td = pres.getSelRow().getTimestamp("DATUM-to");
      this.getMasterSet().setTimestamp("DATUM", td);
    } else if (mode == 'I') {
      jpMaster.jraDatum.requestFocus();
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jlrZiro) || vl.isEmpty(jpMaster.jraBrojizv))
      return false;
    return true;
  }

  public boolean isNewDetailNeeded() {
    return false;
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      jpDetail.jlrCorg.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraIznos.requestFocus();
    }
  }

  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jpDetail.jlrCorg) || vl.isEmpty(jpDetail.jlrCpar) ||
        vl.isEmpty(jpDetail.jraBrojdok) || vl.isEmpty(jpDetail.jlrCskl))
      return false;
    if (mode == 'N' && notUnique()) /**@todo: Provjeriti jedinstvenost kljuca detaila */
      return false;
    return true;
  }

  public boolean notUnique() {
    return false;
    /**@todo: Implementirati notUnique metodu */
  }

  private void jbInit() throws Exception {
    this.setMasterSet(dm.getIzvodi());
    this.setNaslovMaster("Izvodi"); /**@todo: Naslov mastera */
    this.setVisibleColsMaster(new int[] {1, 2, 8});
    this.setMasterKey(new String[] {"KNJIG", "ZIRO", "BROJIZV"});
    jpMaster = new jpSalKonUpMaster(this);
    this.setJPanelMaster(jpMaster);

    this.setDetailSet(dm.getSkstavkerad());
    this.setNaslovDetail("Stavke Skstavkerad"); /**@todo: Naslov detaila */
    this.setVisibleColsDetail(new int[] {1, 5, 10});
    this.setDetailKey(new String[] {"KNJIG", "ZIRO", "BROJIZV", "BROJDOK", "RBS"});
    jpDetail = new jpSalKonUpDetail(this);
    this.setJPanelDetail(jpDetail);

    pres = new presSalKonUp(this);
    pres.setSelDataSet(this.getMasterSet());
    pres.setSelPanel(pres.jpDetail);
  }
}
