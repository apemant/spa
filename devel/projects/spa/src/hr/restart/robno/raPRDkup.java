/****license*****************************************************************
**   file: raPRDkup.java
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
import java.awt.event.ActionEvent;

import hr.restart.sisfun.frmParam;
import hr.restart.util.JlrNavField;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;


import javax.swing.JOptionPane;


public class raPRDkup extends raIzlazTemplate {
  
  raNavAction rnvNacinPlac = new raNavAction("Na\u010Din pla\u0107anja",raImages.IMGEXPORT,java.awt.event.KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
        keyNacinPlac();
    }
  };

  public void initialiser(){
    what_kind_of_dokument = "PRD";
    bPonudaZaKupca= true;
    bDvaRabat = frmParam.getParam("robno", "dvaRabata", "N", "Unos 2 rabata na maloprodajnim dokumentima (D,N)?").equals("D");
  }
  public void MyaddIspisMaster(){
    raMaster.getRepRunner().addReport("hr.restart.robno.repPredracuniKup","hr.restart.robno.repIzlazni","PredracuniKup","Ra�un za predujam 1 red");
    raMaster.getRepRunner().addReport("hr.restart.robno.repPredracuniKup2p","hr.restart.robno.repIzlazni","PredracuniKup2p","Ra�un za predujam 1 red s 2 popusta");
    raMaster.getRepRunner().addReport("hr.restart.robno.repPredracuni2Kup","hr.restart.robno.repIzlazni","Predracuni2Kup","Ra\u010Dun za predujam 2 red");
  }

  public void MyaddIspisDetail(){
    raDetail.getRepRunner().addReport("hr.restart.robno.repPredracuniKup","hr.restart.robno.repIzlazni","PredracuniKup","Ra�un za predujam 1 red");
    raDetail.getRepRunner().addReport("hr.restart.robno.repPredracuniKup2p","hr.restart.robno.repIzlazni","PredracuniKup2p","Ra�un za predujam 1 red s 2 popusta");    
    raDetail.getRepRunner().addReport("hr.restart.robno.repPredracuni2Kup","hr.restart.robno.repIzlazni","Predracuni2Kup","Ra�un za predujam 2 red");
  }


  public void ConfigViewOnTable(){
//    this.setVisibleColsMaster(new int[] {4,5,6});
    this.setVisibleColsDetail(new int[]
                              {4,Aut.getAut().getCARTdependable(5,6,7),8,11,42,12,23,24});
  }
  
  public raPRDkup() {
    isMaloprodajnaKalkulacija = true;
    setPreSel((jpPreselectDoc) presPRDkup.getPres());

    raDetail.addOption(rnvDellAllStav,3);
    master_titel = "Ra\u010Duni za predujam";
    detail_titel_mno = "Stavke ra\u010Duna za predujam";
    detail_titel_jed = "Stavka ra\u010Duna za predujam";
    
    raDetail.addOption(rnvNacinPlac,4);
    raMaster.addOption(rnvFisk, 5, false);
    setMasterSet(dm.getZagPrdKup());
    setDetailSet(dm.getStPrdKup());
    MP.BindComp();
    DP.BindComp();
    MP.panelBasicExt.jlrCNACPL.setRaDataSet(dm.getNacplG());
    setVisibleColsMaster(new int[] {4,5,9});
    set_kum_detail(true);
    stozbrojiti_detail(new String[] {"IPRODSP"});
    raDetail.addOption(rnvKartica,5, false);
    defNacpl = hr.restart.sisfun.frmParam.getParam("robno","gotNacPl");
  }

  public boolean LocalValidacijaMaster(){
    return isDatumToday();
  }

  public void RestPanelMPSetup(){
//    MP.setupOneA();
  }
  
  public boolean ValidacijaStanje(){
    return true ;
  }
  
  public boolean ValidacijaMasterExtend(){
    getMasterSet().setString("PARAM","K");
    MP.panelBasic.rpku.updateRecords();
    return true;
  }

  public void SetFocusIzmjenaExtends() {
    MP.rcc.setLabelLaF(MP.panelBasic.rpku.jraCkupac,true);
    MP.panelBasic.rpku.jraCkupac.requestFocus();
  }

  public void SetFocusNoviExtends(){

    if (MP.panelBasic.jrfCPAR.getText().equals("")){
      MP.panelBasic.rpku.jraCkupac.requestFocus();
    }
    else {
      ((JlrNavField)MP.panelBasic.rpku.jraCkupac).forceFocLost();
      MP.panelBasic.jtfDATDOK.requestFocus();
    }
  }
  public boolean afterWish(){
    return true;
  }

  public void Funkcija_ispisa_master(){
    if (frmPlacanje.justCheckRate(getMasterSet())) { // ovdje dolazi sini�ina provjera rata
      raMaster.getRepRunner().clearAllCustomReports();
      isMasterInitIspis = false;
      super.Funkcija_ispisa_master();
    }
    else {
      JOptionPane.showConfirmDialog(this.raMaster,"Iznos pla\u0107anja je nejednak iznosu ra\u010Duna !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    }
  }

  public void Funkcija_ispisa_detail(){
    if (frmPlacanje.justCheckRate(getMasterSet())) { // ovdje dolazi sini�ina provjera rata
      raDetail.getRepRunner().clearAllCustomReports();
      isDetailInitIspis = false;
      super.Funkcija_ispisa_detail();
    }
    else {
      JOptionPane.showConfirmDialog(this.raDetail,"Iznos pla\u0107anja je nejednak iznosu ra\u010Duna !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    }

  }
  
  public void keyNacinPlac(){
    frmPlacanje.entryRate(this);
  }
  
  public void ExitPointDetail(char mode){
    //frmPlacanje.checkRate(this);
  }
  
  public boolean ValidacijaPrijeIzlazaDetail() {
    return frmPlacanje.checkRate(this);
  }

  public void RestPanelSetup(){
    DP.addRestGRNGOT();
  }

  public boolean DodatnaValidacijaDetail() {
    if (val.isEmpty(DP.jtfKOL)) return false;
    if (val.isEmpty(DP.jraFMC)) return false;
    if (manjeNula()) return false;

    return isPriceToBig(true);
  }
}