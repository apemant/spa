/****license*****************************************************************
**   file: frmArhivaPN.java
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
package hr.restart.blpn;

import hr.restart.baza.dM;
import hr.restart.util.Aus;


public class frmArhivaPN extends frmObracunPN_V1_1 {

  public void beforeShowMaster(){}

  public void beforeShowDetail(){
    this.setNaslovDetail("Stavke arhiviranog putnog naloga broj ".concat(this.getMasterSet().getString("CPN")));
    changeUkupnoSet();
    raDetail.setEnabledNavAction(raDetail.getNavBar().getNavContainer().getNavActions()[0],false);
    raDetail.setEnabledNavAction(raDetail.getNavBar().getNavContainer().getNavActions()[1],false);
    raDetail.setEnabledNavAction(raDetail.getNavBar().getNavContainer().getNavActions()[2],false);
  }

  protected void jbInit() throws Exception {

    this.setMasterSet(dm.getPutnalarh());
    this.setNaslovMaster("Arhiva putnih naloga");
    this.setVisibleColsMaster(new int[] {1, 3, 4, 5, 12, 18});
    this.setMasterKey(new String[] {"KNJIG", "GODINA", "BROJ", "INDPUTA"});
    jpMaster = new jpPrijavaPN(this.raMaster);
    this.setJPanelMaster(jpMaster);

    this.setDetailSet(dm.getStavpnarh());
    this.setVisibleColsDetail(new int[] {3, 5, 7, 10});
    this.setDetailKey(new String[] {"KNJIG", "GODINA", "BROJ", "INDPUTA", "RBS"});
    jpDetail = new jpObracunPNDetail_V1_1(this);
    this.setJPanelDetail(jpDetail);
    raMaster.getJpTableView().addTableModifier(
        new hr.restart.swing.raTableColumnModifier(
        "CRADNIK",
        new String[] {"CRADNIK","IME","PREZIME"},
        dm.getRadnici()
        ));
    raDetail.getJpTableView().addTableModifier(
        new hr.restart.swing.raTableColumnModifier(
        "CSKL",
        new String[] {"CVRSK","OPISVRSK"},
        new String[] {"CSKL"},
        new String[] {"CVRSK"},
        ss.getVrshemekZaPN()
        ));
//    this.raMaster.getRepRunner().clearAllReports();
    this.raMaster.getRepRunner().addReport("hr.restart.blpn.repPutniRacun","Putni ra\u010Dun",2);
    this.raMaster.getRepRunner().addReport("hr.restart.blpn.repPutniRacunPV","Putni ra\u010Dun - protuvrijednost",2);
    this.raDetail.getRepRunner().addReport("hr.restart.blpn.repPutniRacun","Putni ra\u010Dun",2);
    this.raDetail.getRepRunner().addReport("hr.restart.blpn.repPutniRacunPV","Putni ra\u010Dun - protuvrijednost",2);
    setTimeStorige();
    setSplashStorige();
    raMaster.getNavBar().getNavContainer().remove(toRemove0);
    raMaster.getNavBar().getNavContainer().remove(toRemove1);
    raMaster.getNavBar().getNavContainer().remove(toRemove2);
  }

  public void masterSet_navigated(com.borland.dx.dataset.NavigationEvent e){
   this.setNaslovDetail("Stavke arhiviranog putnog naloga broj ".concat(this.getMasterSet().getString("CPN")));
    renewUkupnoSet();
  }

  public void Funkcija_ispisa_master(){
//    changeUkupnoSet();
    refilterDetailSet();
    renewUkupnoSet();
    Aus.refilter(masterSet, ss.getMasterSetPN(this.getMasterSet().getString("CPN")));
    Aus.setFilter(detailSet, ss.getDetailSetPN(this.getMasterSet().getString("CPN")));
    detailSet.setColumns(dM.getDataModule().getStavkepn().cloneColumns());
    detailSet.open();
//    setInfoPanel();
//    System.out.println("UD : " + ukupnjaci.getBigDecimal("UKUDNEV"));
//    System.out.println("funkcija ispisa master");
    super.Funkcija_ispisa_master();
  }

  public void Funkcija_ispisa_detail(){
//    System.out.println("funkcija ispisa ditelj");
  	Aus.refilter(masterSet, ss.getMasterSetPN(this.getMasterSet().getString("CPN")));
    Aus.setFilter(detailSet, ss.getDetailSetPN(this.getMasterSet().getString("CPN")));
    detailSet.setColumns(dM.getDataModule().getStavkepn().cloneColumns());
    detailSet.open();
    super.Funkcija_ispisa_detail();
  }

}
