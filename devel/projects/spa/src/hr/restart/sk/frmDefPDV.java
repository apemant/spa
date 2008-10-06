/****license*****************************************************************
**   file: frmDefPDV.java
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
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;

import com.borland.dx.sql.dataset.QueryDataSet;


public class frmDefPDV extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  Util ut = Util.getUtil();

  jpDefPDVMaster jpMaster;
  jpDefPDVDetail jpDetail;


  public frmDefPDV() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void beforeShowMaster(){
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jpMaster.jraCiz, false);
    }
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      jpMaster.jraCiz.requestFocus();
    } else if (mode == 'I') {
      jpMaster.jraOpis.requestFocus();
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jraCiz) || vl.isEmpty(jpMaster.jraOpis))
      return false;
    return true;
  }

  public void afterSetModeDetail(char oldM,char newM){
    if (newM == 'B'){
      jpDetail.jlrCknjige.setRaDataSet(dm.getKnjigeUI());
      jpDetail.jlrCkolone.setRaDataSet(dm.getKoloneknjUI());
    }
  }

  public void beforeShowDetail(){
    setNaslovDetail(getMasterSet().getString("OPIS"));
    jpDetail.rebinder(getDetailSet().getString("URAIRA"));
  }

  public void EntryPointDetail(char mode) {
    if (mode == 'N'){}
    if (mode == 'I'){}
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      jpDetail.jrbURA.setSelected();
      jpDetail.jrbURA.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jlrCknjige.requestFocus();
    }
  }

  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jpDetail.jlrCknjige) || vl.isEmpty(jpDetail.jlrCkolone))
      return false;
    if (mode == 'N' && notUniqueDetail()){
      javax.swing.JOptionPane.showMessageDialog(
          this.jpDetail,
          "Zapis postoji!",
          "Greška",
          javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }

    jpDetail.jlrCknjige.emptyTextFields();
    jpDetail.jlrCkolone.emptyTextFields();
    return true;
  }

  public boolean notUniqueDetail() {
    QueryDataSet junik = ut.getNewQueryDataSet("SELECT ciz FROM StIzvjPDV where cknjige='"+
        getDetailSet().getString("CKNJIGE") + "' and ckolone= "+
        getDetailSet().getShort("CKOLONE") + " and uraira='"+
        getDetailSet().getString("URAIRA")+"'");

    return !junik.isEmpty();
  }

  private void jbInit() throws Exception {
    this.setMasterSet(dm.getIzvjPDV());
    this.setNaslovMaster("Definicije obrasca PDV");
    this.setVisibleColsMaster(new int[] {0, 1});
    this.setMasterKey(new String[] {"CIZ"});
    jpMaster = new jpDefPDVMaster(this);
    this.setJPanelMaster(jpMaster);
    setMasterDeleteMode(DELDETAIL);

    this.setDetailSet(dm.getStIzvjPDV());
    this.setVisibleColsDetail(new int[] {0, 1, 2});
    this.setDetailKey(new String[] {"CIZ", "CKNJIGE", "CKOLONE", "URAIRA"});
    jpDetail = new jpDefPDVDetail(this);
    this.setJPanelDetail(jpDetail);

    raDetail.getJpTableView().addTableModifier(
        new hr.restart.swing.raTableColumnModifier(
        "CKNJIGE",
        new String[] {"CKNJIGE","NAZKNJIGE"},
        new String[] {"CKNJIGE","URAIRA"},
        dm.getKnjigeUI()
        ));

    raDetail.getJpTableView().addTableModifier(
        new hr.restart.swing.raTableColumnModifier(
        "CKOLONE",
        new String[] {"CKOLONE","NAZIVKOLONE"},
        new String[] {"CKOLONE","URAIRA"},
        dm.getKoloneknjUI()
        ));
  }

  public void detailSet_navigated(com.borland.dx.dataset.NavigationEvent e){
    jpDetail.rebinder(getDetailSet().getString("URAIRA"));
  }

  int rowPosMaster = 0;

  public void masterSet_navigated(com.borland.dx.dataset.NavigationEvent e){
    if (getMasterSet().row() != rowPosMaster) {
      rowPosMaster = getMasterSet().row();
      setNaslovDetail(getMasterSet().getString("OPIS"));
    }
  }
}
