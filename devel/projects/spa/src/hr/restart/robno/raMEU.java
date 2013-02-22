/****license*****************************************************************
**   file: raMEU.java
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
package hr.restart.robno;
import hr.restart.util.raTransaction;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;

public class raMEU extends raMEI {

  public void Setup() {
    bMEI = false;
    setSelect(selectMEU.getPres());

    raDM = new jpMesklaDetail2(){
      public void enable_rest(boolean istina){
          hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(jtfKOL,istina);
          hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(jraFC,istina);
          hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(jraFC1,istina);
          hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(jraFC2,istina);
          hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(jraPMAR,istina);
//          jraFC.setEditable(false);
          hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(jraINETO,istina);
      }
      public void addRest(){
        add4MEU();
      }
    };
    String[] key={"CSKLIZ","CSKLUL","VRDOK","GOD","BRDOK"};
    String[] dkey={"CSKLIZ","CSKLUL","VRDOK","GOD","BRDOK","RBR"};
    setNaslovMaster("Me\u0111uskladišnica ulaz");
    setNaslovDetail("Stavke me\u0111uskladišnice ulaz");
    set_kum_detail(true);
    stozbrojiti_detail(new String[] {"INABUL"});
    setMasterSet(dm.getMesklaMEU());
    setDetailSet(dm.getStmesklaMEU());

    setJPanelMaster(raMM);
    setJPanelDetail(raDM);
    raMM.BindComp(getMasterSet());
    raDM.setDataSet(getDetailSet());

    setMasterKey(key);
    setDetailKey(dkey);
    this.setVisibleColsMaster(new int[] {5,6,0,1});
    this.setVisibleColsDetail(new int[] {5,Aut.getAut().getCARTdependable(6,7,8),9,10,11,12,22});

    this.raMaster.getRepRunner().clearAllCustomReports();
    this.raDetail.getRepRunner().clearAllCustomReports();
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repMeu","hr.restart.robno.repMeskla","Meu","Me\u0111uskladišnica ulazna");
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repMeu","hr.restart.robno.repMeskla","Meu","Me\u0111uskladišnica ulazna");
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repMeuExtendedVersion","hr.restart.robno.repMeskla","MeuExtendedVersion","Me\u0111uskladišnica ulazna - vrijednosna");
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repMeuExtendedVersion","hr.restart.robno.repMeskla","MeuExtendedVersion","Me\u0111uskladišnica ulazna - vrijednosna");
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repMeuNiv","hr.restart.robno.repMesNivel","Nivel","Poravnanje - nivelacija");
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repMeuNiv","hr.restart.robno.repMesNivel","Nivel","Poravnanje - nivelacija");
    
    rCD.setisNeeded(hr.restart.sisfun.frmParam.getParam("robno",
				"kontKalk", "D",
				"Kontrola ispravosti redoslijeda unosa dokumenata")
				.equalsIgnoreCase("D"));

  }
  public void Funkcija_ispisa_Over(boolean what){
    reportsQuerysCollector.getRQCModule().ReSql(PrepSql(what),"MEU");
  }

  public void AfterSaveDetail(char mode) {}


  public boolean doWithSaveDetail(char mode){

    boolean returnValue = true;
    if (mode=='N'){
      if (StanjeUlaz.getRowCount() == 0){
        StanjeUlaz.insertRow(true);
        StanjeUlaz.setString("CSKL",getDetailSet().getString("CSKLUL"));
        StanjeUlaz.setString("GOD",getDetailSet().getString("GOD"));
        StanjeUlaz.setInt("CART",getDetailSet().getInt("CART"));
      }
      lc.TransferFromClass2DB(StanjeUlaz,rKM.stanjeul);
      rCD.unosKalkulacije(getDetailSet(),StanjeUlaz);
      StanjeUlaz.setTimestamp("DATZK", 
          getMasterSet().getTimestamp("DATDOK"));
      raTransaction.saveChanges(getDetailSet());
      raTransaction.saveChanges((QueryDataSet) StanjeUlaz);
    }
    else if (mode=='I'){
      lc.TransferFromClass2DB(StanjeUlaz,rKM.stanjeul);
      StanjeUlaz.setTimestamp("DATZK", 
          getMasterSet().getTimestamp("DATDOK"));
      raTransaction.saveChanges((QueryDataSet) StanjeUlaz);
    }
    else if (mode=='B') {

      rCD.brisanjeKalkulacije(StanjeUlaz);
      lc.TransferFromClass2DB(StanjeUlaz,rKM.stanjeul);
      raTransaction.saveChanges((QueryDataSet) StanjeUlaz);
      if (raDM.rpcart.getISB().equals("D")) {
        dlgSerBrojevi.getdlgSerBrojevi().deleteSerBr('M');
      }
      try {
         val.recountDataSet(raDetail, "RBR", oldRbr, false);
         raTransaction.saveChanges(getDetailSet());
       }
       catch (Exception ex) {
         ex.printStackTrace();
         return false;
        }
    }
    return returnValue;
  }

  public void beforeShowDetail() {
//ST.prn("beforeShowDetail()");
    raDM.rpcart.setGodina(hr.restart.util.Valid.getValid().findYear(getMasterSet().getTimestamp("DATDOK")));
    raDM.rpcart.setCskl(getMasterSet().getString("CSKLUL"));
    raDM.rpcart.setMode("N");
  }
  
  public boolean DeleteCheckDetail(){
  	if (!super.DeleteCheckDetail()) return false;
  	if (StanjeUlaz.getBigDecimal("KOL").compareTo(
    		getDetailSet().getBigDecimal("KOL")) < 0) {
    	JOptionPane.showConfirmDialog(raDetail.getWindow(),
					"Brisanje nije moguæe. Kolièina na stanju je manja od kolièine na ovoj stavci!",
					"Gre\u0161ka", JOptionPane.ERROR_MESSAGE);
			return false;
    }
  	return true;
  }
  public void AfterDeleteDetail(){}

  public boolean isUpdatePossible(){
    StanjeUlaz.refresh();
    return rCD.testKalkulacije(getDetailSet(),StanjeUlaz);
  }

  public void ponistavanjeDijela(){
    rKM.ponistavanjeIzlaza();
  }
  public void InitRPC() {

    raDM.rpcart.setGodina(hr.restart.util.Valid.getValid().findYear(getMasterSet().getTimestamp("DATDOK")));
    raDM.rpcart.setCskl(getMasterSet().getString("CSKLUL"));
    raDM.rpcart.setTabela(getDetailSet());
    raDM.rpcart.setParam(hr.restart.sisfun.frmParam.getParam("robno","indiCart"));
    raDM.rpcart.setnextFocusabile(raDM.jtfKOL);
    raDM.rpcart.InitRaPanCart();
//    raDM.rpcart.setSearchable(false);
    raDM.rpcart.setMode("N");
  }
  public void jtfKOL_focusLost(){

    if (this.raDetail.getMode()=='N' || this.raDetail.getMode()=='I') {
//      if (raDM.jtfKOL.isValueChanged()){
        lc.TransferFromDB2Class(getDetailSet(),rKM.stavka);
        rKM.Kalkulacija();
        this.ponistavanjeDijela();
        lc.TransferFromClass2DB(getDetailSet(),rKM.stavka);
//      }
      raDM.jtfKOL.selectAll();
    }
  }

  public boolean PrepMeskla4Del() {
    boolean returnValue = true;
    rCD.prepareFields(getDetailSet());
    findStanja();
    returnValue = rCD.testKalkulacije(getDetailSet(),StanjeUlaz);

    if (returnValue) {
      rKM.stavka.Init();
      rKM.stavkaold.Init();
      lc.TransferFromDB2Class(getDetailSet(),rKM.stavkaold);
      lc.TransferFromDB2Class(StanjeUlaz,rKM.stanjeul);
      rKM.kalkStanja();
      rKM.returnOldPrice();
    }
    else {
      javax.swing.JOptionPane.showMessageDialog(null,
       rCD.errorMessage(),
       "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
       raDM.jtfKOL.requestFocus();
    }
    return returnValue;
  }
  
  public void beforeShowMaster() {
    setNaslovMaster("Ulazne meðuskladišnice za skladišta " +
        jpSelectM.getSelRow().getString("CSKLIZ") + " -> " +
        jpSelectM.getSelRow().getString("CSKLUL"));
  }

  public void masterSet_navigated(com.borland.dx.dataset.NavigationEvent ne) {
    this.setNaslovDetail("Stavke ulazne meðuskladišnice "+
        getMasterSet().getString("CSKLIZ").trim()+" - "+
        getMasterSet().getString("CSKLUL").trim()+" / "+
        getMasterSet().getString("GOD") + " - " + 
        getMasterSet().getInt("BRDOK"));
  }
  
  public void findStanja() {

    rKM.stanjeiz.Init();
    rKM.stanjeul.Init();
    ASTUL.findStanjeUnconditional(getMasterSet().getString("GOD"),
            getMasterSet().getString("CSKLUL"),
            getDetailSet().getInt("CART"));
    StanjeUlaz =  ASTUL.gettrenSTANJE();
    lc.TransferFromDB2Class(StanjeUlaz,rKM.stanjeul);
    rKM.stanjeul.sVrSklad=ASTUL.VrstaZaliha(getMasterSet().getString("CSKLUL"));
  }

  public boolean isKnjigen(){
    return getMasterSet().getString("STATKNJU").equalsIgnoreCase("K") ||
           getMasterSet().getString("STATKNJU").equalsIgnoreCase("P");
  }

  public void setupPrice(){
      rKM.setupPriceMEU();
      rKM.setupOldPrice();
  }


}