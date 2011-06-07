/****license*****************************************************************
**   file: frmBlagIzv.java
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
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;
import hr.restart.util.raTransaction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;

public class frmBlagIzv extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  Util ut = Util.getUtil();
  sgStuff ss = sgStuff.getStugg();

  QueryDataSet repQDS = new QueryDataSet();
  QueryDataSet repQDS2 = new QueryDataSet();

  jpUplIsplMaster jpMaster;
  jpUplIsplDetail jpDetail;

  public frmBlagIzv() {
    try {
      blizv = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  raNavAction rnvDearhiviranjeGumbic = new raNavAction("Dearhiviranje izvještaja", raImages.IMGHOME, KeyEvent.VK_F10) {
    public void actionPerformed(ActionEvent e) {
      dearhivir();
    }
  };

  static frmBlagIzv blizv;

  public static frmBlagIzv getBlagIzv(){
    return blizv;
  }

  boolean bezgotovinska;

  public void beforeShowMaster() {
    vl.execSQL(ss.getNazivBlagajne(getPreSelect().getSelRow()));
    vl.RezSet.open();
    String blag = vl.RezSet.getString("NAZIV");
    int cbl = getPreSelect().getSelRow().getInt("CBLAG");
    String val = getPreSelect().getSelRow().getString("OZNVAL");
    short god = getPreSelect().getSelRow().getShort("GODINA");
    String knjig = getPreSelect().getSelRow().getString("KNJIG");
    String naslovMaster;
    if (!ss.getJeLiBlagajnaBezgotovinska(knjig, cbl, val)) {
      naslovMaster = "Blagajna: " + cbl + " " + blag + " [" + val + "] - Pregled izvještaja";
    } else {
      naslovMaster = "Bezgotovinska blagajna: " + cbl + " " + blag + " [" + val + "] - Pregled izvještaja";
    }
    bezgotovinska = ss.getJeLiBlagajnaBezgotovinska(knjig, cbl, val);
    setNaslovMaster(naslovMaster);
    enabDisabNavBarBotuns();
  }

  private void enabDisabNavBarBotuns() {
    System.out.println(this.getMasterSet().getString("STATUS")); //XDEBUG delete when no more needed
    if (this.getMasterSet().getString("STATUS").equals("Z") || this.getMasterSet().getString("STATUS").equals("K")){
      raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[3],true);
//      raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[4],false);
      if (this.getMasterSet().getString("STATUS").equals("K"))
        //raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[5],false);
        raMaster.setEnabledNavAction(rnvDearhiviranjeGumbic,false);
      else
        raMaster.setEnabledNavAction(rnvDearhiviranjeGumbic,true);
        //raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[5],true);
    }
    else{
      raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[3],false);
//      raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[4],false);
      //raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[5],false);
      raMaster.setEnabledNavAction(rnvDearhiviranjeGumbic,false);
    }
  }

  public void beforeShowDetail(){
    String naslovDetail = "Stavke izvješ\u0107a broj " + this.getMasterSet().getInt("BRIZV");
    setNaslovDetail(naslovDetail);
    if (getPreSelect().getSelRow().getString("OZNVAL").equals(hr.restart.zapod.Tecajevi.getDomOZNVAL()))
      this.jpDetail.initDomesticVal();
    else
      this.jpDetail.initInoVal("BLAGIZV");
  }

  public void Funkcija_ispisa_detail() {
  	Aus.refilter(repQDS, ss.getRepQDSJednaUplatnica(this.getMasterSet(), this.getDetailSet(), "stavkeblarh"));
    super.Funkcija_ispisa_detail();
  }

  public void Funkcija_ispisa_master() {
  	Aus.refilter(repQDS, ss.getRepQDSIzvjestaj(this.getMasterSet(), "stavkeblarh"));
  	Aus.refilter(repQDS2, ss.getRepQDSSveUplatnice(this.getMasterSet(), "stavkeblarh"));    
    super.Funkcija_ispisa_master();
  }

  public QueryDataSet getrepQDS() {
    repQDS.setSort(new SortDescriptor(new String[]{ss.getOrderBlagIzvField()}));
    return repQDS;
  }

  public QueryDataSet getrepQDS2() {
    repQDS2.setSort(new SortDescriptor(new String[]{ss.getOrderBlagIzvField()}));
    return repQDS2;
  }

  int red = 999;

  public void masterSet_navigated(com.borland.dx.dataset.NavigationEvent e){
    if(this.getMasterSet().getRow() != red){
      red = this.getMasterSet().getRow();
      enabDisabNavBarBotuns();
    }
  }

  private void jbInit() throws Exception {
    this.raMaster.addOption(rnvDearhiviranjeGumbic, 5);
    this.setMasterSet(dm.getBlagizv());
    this.setMasterDeleteMode(this.DELDETAIL);
    this.setVisibleColsMaster(new int[] {1, 4, 7, 8, 10});
    this.setMasterKey(new String[] {"KNJIG", "CBLAG", "OZNVAL", "GODINA", "BRIZV"});
    jpMaster = new jpUplIsplMaster(this);
    this.setJPanelMaster(jpMaster);
    raMaster.setEditEnabled(false);
    this.raMaster.getRepRunner().addReport("hr.restart.blpn.repBlagIzv","Ispis blagajni\u010Dkog izvještaja",2);
    this.raMaster.getRepRunner().addReport("hr.restart.blpn.repBlagIzvUplatniceAll","Ispis  svih uplatnica / isplatnica",2);

    this.setDetailSet(dm.getStavkeblarh());
    this.setVisibleColsDetail(new int[] {5, 6, 7, 8});
    this.setDetailKey(new String[] {"KNJIG", "CBLAG", "OZNVAL", "GODINA", "BRIZV", "RBS"});
    jpDetail = new jpUplIsplDetail(this);
    this.setJPanelDetail(jpDetail);
    raDetail.setEditEnabled(false);
    this.raDetail.getRepRunner().clearAllReports();
    this.raDetail.getRepRunner().addReport("hr.restart.blpn.repBlagIzvUplatnice","Ispis uplatnice / isplatnice",2);
  }

  void dearhivir(){
    if(this.getMasterSet().getString("STATUS").equals("K")) return;
    if (!questions()) return;
    if (!dearhiviranje()) return;
    this.getMasterSet().setString("STATUS", "U");
    this.getMasterSet().saveChanges();
    getMasterSet().refresh();
    raMaster.getJpTableView().fireTableDataChanged();
    enabDisabNavBarBotuns();
  }

  private boolean questions(){
    int opshn = JOptionPane.showConfirmDialog(this.raMaster.getWindow(), new hr.restart.swing.raMultiLineMessage(new String[] {"Ova akcija vra\u0107a izvještaj","Da li to stvarno želite?"}),
        "Pozor!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//    boolean retrun;
//    if (opshn == JOptionPane.YES_OPTION) retrun = true;
//    else retrun = false;
    return (opshn == JOptionPane.YES_OPTION);
  }

  boolean dearhiviranje() {
    try {
      refilterDetailSet();
      String[] kolone = new String[] {"KNJIG", "CBLAG", "OZNVAL", "GODINA", "BRIZV", "DATUM", "RBS", "PRIMITAK", 
          "IZDATAK", "PVPRIMITAK", "PVIZDATAK", "TECAJ", "CRADNIK", "CPN", 
          "OPIS", "TKO", "CGRSTAV", "STAVKA", "CSKL", "VRDOK", "CORG", "VRSTA",
          "BROJKONTA", "DATDOK", "DATDOSP", "BROJDOK", "CPAR"};
      QueryDataSet dummyDetail = hr.restart.baza.Stavblag.getDataModule().getTempSet("1=3");
      dummyDetail.open();
//      ut.getNewQueryDataSet("select * from stavkeblarh where 0=1");
      this.getDetailSet().first();
      do {
        dummyDetail.insertRow(false);
        this.getDetailSet().copyTo(kolone, this.getDetailSet(), kolone, dummyDetail);

      } while (this.getDetailSet().next());
      this.getDetailSet().deleteAllRows();
      raTransaction.saveChangesInTransaction(new QueryDataSet[] {this.getDetailSet(), dummyDetail});
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }
}