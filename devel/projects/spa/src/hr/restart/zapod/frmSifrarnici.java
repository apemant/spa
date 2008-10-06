/****license*****************************************************************
**   file: frmSifrarnici.java
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
package hr.restart.zapod;

import hr.restart.baza.dM;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;

import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


public class frmSifrarnici extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  public jpSifrarniciMaster jpMaster;
  public jpSifrarniciDetail jpDetail;

  String[] key = new String[] {"VRSTASIF"};

  public frmSifrarnici() {
    super(1,2);
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
      rcc.setLabelLaF(jpMaster.jraVrstasif, false);
    }
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      jpMaster.jraVrstasif.requestFocus();
    } else if (mode == 'I') {
      jpMaster.jraOpisvrsif.requestFocus();
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jraVrstasif))
      return false;
    if (mode == 'N' && vl.notUnique(jpMaster.jraVrstasif)) // notUniqueM())
      return false;
    return true;
  }

  public void EntryPointDetail(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraCsif, false);
    }
  }
  public void detailSet_navigated(NavigationEvent e) {
    jpDetail.jlCsifprip.setSelected(!getDetailSet().getString("CSIF").equals(getDetailSet().getString("CSIFPRIP")));
  }
  public void beforeShowDetail() {
    applySifChange();
    setNaslovDetail(getMasterSet().getString("OPISVRSIF").concat(" - šifrarnik"));
    raMaster.setEnabled(false);
  }
  public boolean ValidacijaPrijeIzlazaDetail() {
    raMaster.setEnabled(true);
    return true;
  }

  private void applySifChange() {
    StorageDataSet sifreds = Sifrarnici.getSifre(getMasterSet().getString("VRSTASIF"));
    jpDetail.jlrCsifprip.setRaDataSet(null);
    jpDetail.jlrNaziv.setRaDataSet(null);
    jpDetail.jlrCsifprip.setRaDataSet(sifreds);
    jpDetail.jlrNaziv.setRaDataSet(sifreds);
  }

  public void SetFokusDetail(char mode) {
    applySifChange();
    jpDetail.jlrCsifprip.getRaDataSet().refresh();
    if (mode == 'N') {
      jpDetail.jraCsif.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraNaziv.requestFocus();
    }
    jpDetail.setNavs(jpDetail.jlCsifprip.isSelected());
  }

  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jpDetail.jraCsif))
      return false;
//      System.out.println("jpDetail.jraCsif : " + jpDetail.jraCsif.getText() + " jpMaster.jraVrstasif : " + jpMaster.jraVrstasif.getText());
    if (mode == 'N' && notUniqueD()) //vl.notUnique(new JTextComponent[] {jpDetail.jraCsif, jpMaster.jraVrstasif})) //notUniqueD())
      return false;
    return true;
  }

/*
  public boolean notUniqueM() {
    QueryDataSet qds = new QueryDataSet();
    String qstr = "select vrstasif from vrstesif where vrstasif ='" + jpMaster.jraVrstasif.getText().trim() + "'";
//    System.out.println("qstr: " + qstr);
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(),qstr));
    qds.open();
    if (!qds.isEmpty()) {
      jpMaster.jraVrstasif.setText("");
      SetFokusMaster('N');
      vl.showValidErrMsg(jpMaster.jraOpisvrsif,'U');
    }
    return (!qds.isEmpty());
  }
*/

  public boolean notUniqueD() {    
    String qstr = "select csif from sifrarnici where csif='" + jpDetail.jraCsif.getText().trim() + "' and vrstasif ='" + jpMaster.jraVrstasif.getText().trim() + "'";
    QueryDataSet qds = Util.getNewQueryDataSet(qstr);
    if (!qds.isEmpty()){
      jpDetail.jraCsif.setText("");
      SetFokusDetail('N');
      vl.showValidErrMsg(jpMaster.jraOpisvrsif,'U');
    }
    return (!qds.isEmpty());
  }

  private void jbInit() throws Exception {
    this.setMasterSet(dm.getVrstesif());
    this.setNaslovMaster("Šifrarnici");
    this.setMasterDeleteMode(this.DELDETAIL);
    this.setVisibleColsMaster(new int[] {0, 1});
    this.setMasterKey(key);
    jpMaster = new jpSifrarniciMaster(this);
    this.setJPanelMaster(jpMaster);

    this.setDetailSet(dm.getSifrarnici());
    this.setNaslovDetail("Stavke šifrarnici");
    this.setVisibleColsDetail(new int[] {0, 2});
    this.setDetailKey(new String[] {"VRSTASIF", "CSIF"});
    jpDetail = new jpSifrarniciDetail(this);
    this.setJPanelDetail(jpDetail);
  }

  public void jraVrstasifRF(){
    jpMaster.jraVrstasif.requestFocus();
  }
}