/****license*****************************************************************
**   file: raPONkup.java
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
import hr.restart.util.JlrNavField;

public class raPONkup extends raIzlazTemplate  {

    public void initialiser(){
      what_kind_of_dokument = "PON";
      bPonudaZaKupca= true;
    }

    public void MyaddIspisMaster(){
      raMaster.getRepRunner().addReport("hr.restart.robno.repPonudaKup","hr.restart.robno.repIzlazni","PonudaKup","Ponuda 1 red");
      raMaster.getRepRunner().addReport("hr.restart.robno.repPonudaVKup","hr.restart.robno.repIzlazni","PonudaKup","Ponuda 1 red u valuti");
      raMaster.getRepRunner().addReport("hr.restart.robno.repPonuda2Kup","hr.restart.robno.repIzlazni","Ponuda2Kup","Ponuda 2 red");
      raMaster.getRepRunner().addReport("hr.restart.robno.repPonuda2VKup","hr.restart.robno.repIzlazni","Ponuda2Kup","Ponuda 2 red u valuti");
      raMaster.getRepRunner().addReport("hr.restart.robno.repPonudaNop",
          "hr.restart.robno.repIzlazni", "PonudaNop", "Ponuda bez cijena stavki");
      raMaster.getRepRunner().addReport("hr.restart.robno.repMxPONKup","Matri\u010Dni ispis ponude");
      raMaster.getRepRunner().addReport("hr.restart.robno.repMxPONKupPop","Matri\u010Dni ispis ponude s više popusta");
    }
    public void MyaddIspisDetail(){
      raDetail.getRepRunner().addReport("hr.restart.robno.repPonudaKup","hr.restart.robno.repIzlazni","PonudaKup","Ponuda 1 red");
      raDetail.getRepRunner().addReport("hr.restart.robno.repPonudaVKup","hr.restart.robno.repIzlazni","PonudaKup","Ponuda 1 red u valuti");
      raDetail.getRepRunner().addReport("hr.restart.robno.repPonuda2Kup","hr.restart.robno.repIzlazni","Ponuda2Kup","Ponuda 2 red");
      raDetail.getRepRunner().addReport("hr.restart.robno.repPonuda2VKup","hr.restart.robno.repIzlazni","Ponuda2Kup","Ponuda 2 red u valuti");
      raDetail.getRepRunner().addReport("hr.restart.robno.repPonudaNop",
          "hr.restart.robno.repIzlazni", "PonudaNop", "Ponuda bez cijena stavki");
      raDetail.getRepRunner().addReport("hr.restart.robno.repMxPONKup","Matri\u010Dni ispis ponude");
      raDetail.getRepRunner().addReport("hr.restart.robno.repMxPONKupPop","Matri\u010Dni ispis ponude s više popusta");
    }

    public void ConfigViewOnTable(){
//      this.setVisibleColsMaster(new int[] {4,5,6});
      this.setVisibleColsDetail(new int[]
        {4,Aut.getAut().getCARTdependable(5,6,7),8,11,42,12,23,24});
    }

    /*public void SetFocusMasterBefore() {

		if (raMaster.getMode() == 'N') {
			if (hr.restart.sisfun.frmParam.getParam("robno", "rezkol",
					"Rezerviranje kolièine D/N", "D").equalsIgnoreCase("D")) {
				getMasterSet().setString("REZKOL", "D");
			} else {
				getMasterSet().setString("REZKOL", "N");
			}
		}
	}*/
    
    public raPONkup() {
      isMaloprodajnaKalkulacija = true;
      setPreSel((jpPreselectDoc) presPONkup.getPres());
      addButtons(true,true);

//      raMaster.addOption(rnvDellAll,3);
      raDetail.addOption(rnvDellAllStav,3);
      raDetail.addOption(rnvKartica,5, false);
      master_titel = "Ponude";
      detail_titel_mno = "Stavke ponude";
      detail_titel_jed = "Stavka ponude";
      setMasterSet(dm.getZagPonKup());
      setDetailSet(dm.getStPonKup());

      rCD.setisNeeded(false);
      MP.BindComp();
      DP.BindComp();
      this.setVisibleColsMaster(new int[] {4,5,9});
      DP.resizeDP();
      raDetail.addOption(rnvCopyPon, 6, false);
    }

    boolean gotovin = true;

    public boolean ValidacijaStanje(){
      return true ;
    }

    public boolean DodatnaValidacijaDetail() {

//      if (val.isEmpty(DP.jtfKOL)) return false;
//      if (val.isEmpty(DP.jraFC)) return false;
//      if (manjeNula()) return false;
//      return true;
      if (val.isEmpty(DP.jtfKOL)) return false;
      if (val.isEmpty(DP.jraFMC)) return false;
      if (manjeNula()) return false;
      return isPriceToBig(true);

    }


  public boolean ValidacijaMasterExtend(){
/*    int i = MP.panelBasic.rpku.manipulateKupci();
    if (i!=-1){
      getMasterSet().setInt("CKUPAC",i);
    }*/

    /* Andrej:
     * ne znam jel treba,
     * ovisi sto i kada radi rpku.setDataSet(fDI.getMasterSet())
     * u rajpIzlazMPTemplate.BindComp();
     */
    //MP.panelBasic.rpku.setDataSet(getMasterSet());
    /* Andrej:
     * isto sto i ono gore zakomentirano
     */
    getMasterSet().setString("PARAM","K");
    MP.panelBasic.rpku.updateRecords();
    return true;
  }

  public void SetFocusIzmjenaExtends() {
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
  
  public void SetFokusDetail(char mode) {
		super.SetFokusDetail(mode);
		if (mode=='N'){
			DP.setRezervacija();
		}
  }

  public boolean LocalValidacijaMaster(){
    return true;
  }
  public void RestPanelSetup(){
    DP.addRestGRNGOT();
    DP.instalRezervaciju();
  }

  public boolean isKPR() {
      return false;
  }
}