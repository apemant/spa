/****license*****************************************************************
**   file: raGRN.java
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

import hr.restart.baza.Partneri;
import hr.restart.sisfun.frmParam;
import hr.restart.util.JlrNavField;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;

public class raGRN extends raIzlazTemplate  {

  public void initialiser(){
    what_kind_of_dokument = "GRN";  // treba biti grn
  }
  raNavAction rnvNacinPlac = new raNavAction("Na\u010Din pla\u0107anja",raImages.IMGEXPORT,java.awt.event.KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
          keyNacinPlac();
      }
  };

  public void keyNacinPlac(){
    frmPlacanje.entryRate(this);
  }
  raNavAction rnvIzradaOtp = new raNavAction("Izrada i ispis otpremnica",raImages.IMGMOVIE,java.awt.event.KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      ispisiizradaOTP();
    }
  };

  public void ispisiizradaOTP(){  	
    raAutoOtpfromRacMask rAOFRM = new raAutoOtpfromRacMask();
    rAOFRM.setCskl(getMasterSet().getString("CSKL"));
    rAOFRM.setBrdok(getMasterSet().getInt("BRDOK"));
    rAOFRM.setGod(getMasterSet().getString("GOD"));
    rAOFRM.setVrdok(getMasterSet().getString("VRDOK"));
    rAOFRM.ispisiizradaOTP(false, false);
  }


  public void ExitPointDetail(){
    frmPlacanje.checkRate(this);
  }

  public raGRN() {
    isOJ=true;
    isMaloprodajnaKalkulacija = true;
    addButtons(true,true);
    raMaster.addOption(rnvFisk, 5, false);
//    raMaster.addOption(rnvDellAll,3);
    raDetail.addOption(rnvDellAllStav,3);
    raDetail.addOption(rnvKartica, 5);
    setPreSel((jpPreselectDoc) presGRN.getPres());

    master_titel = "Gotovinski raèuni";
    detail_titel_mno = "Stavke gotovinskog ra\u010Duna";
    detail_titel_jed = "Stavka gotovinskog ra\u010Duna";
    setMasterSet(dm.getZagGrn());
    setDetailSet(dm.getStGrn());
    MP.BindComp();
    DP.BindComp();
//    set_kum_detail(false);
    DP.rpcart.addSkladField(hr.restart.robno.Util.getSkladFromCorg());
    DP.resizeDP();
    MP.panelBasicExt.jlrCNACPL.setRaDataSet(dm.getNacplG());
    raDetail.addOption(rnvNacinPlac,4);
    raMaster.addOption(rnvIzradaOtp,6);
    setVisibleColsMaster(new int[] {4,5,9});
    defNacpl = hr.restart.sisfun.frmParam.getParam("robno","gotNacPl");
  }

  public void MyaddIspisMaster(){
    raMaster.getRepRunner().clearAllCustomReports();
    raMaster.getRepRunner().addReport("hr.restart.robno.repGrnRac","hr.restart.robno.repIzlazni","GrnRac","Raèun 1 red");
    raMaster.getRepRunner().addReport("hr.restart.robno.repGrnRac2","hr.restart.robno.repIzlazni","GrnRac2","Raèun 2 red");
    raMaster.getRepRunner().addReport("hr.restart.robno.repRacRnalKupac","hr.restart.robno.repIzlazni","RacRnalKupac",ReportValuteTester.titleRACFROMRNAL);
    raMaster.getRepRunner().addReport("hr.restart.robno.repMxGRN", "Matrièni ispis raèuna");
    if (repFISBIH.isFISBIH()) {
      if (getMasterSet().getInt("FBR")>0) {
        raMaster.getRepRunner().addReport("hr.restart.robno.repFISBIHDupli","Ispis DUPLIKATA FISKALNOG ra\u010Duna");
        raMaster.getRepRunner().addReport("hr.restart.robno.repFISBIHRekRN","REKLAMIRANJE FISKALNOG ra\u010Duna");
      } else raMaster.getRepRunner().addReport("hr.restart.robno.repFISBIHRN","FISKALNI ispis ra\u010Duna");
    }
  }
  public void ConfigViewOnTable(){
//    this.setVisibleColsMaster(new int[] {4,5,6});
    setVisibleColsDetail(new int[] {4,Aut.getAut().getCARTdependable(5,6,7),8,11,42,12,23,24});
  }

  public void MyaddIspisDetail(){
    raDetail.getRepRunner().clearAllCustomReports();
    raDetail.getRepRunner().addReport("hr.restart.robno.repGrnRac","hr.restart.robno.repIzlazni","GrnRac","Ra\u010Dun 1 red");
    raDetail.getRepRunner().addReport("hr.restart.robno.repGrnRac2","hr.restart.robno.repIzlazni","GrnRac2","Ra\u010Dun 2 red");
    raDetail.getRepRunner().addReport("hr.restart.robno.repRacRnalKupac","hr.restart.robno.repIzlazni","RacRnalKupac",ReportValuteTester.titleRACFROMRNAL);
    raDetail.getRepRunner().addReport("hr.restart.robno.repMxGRN", "Matrièni ispis raèuna");
//    if (repFISBIH.isFISBIH()) raDetail.getRepRunner().addReport("hr.restart.robno.repFISBIHRN","FISKALNI ispis ra\u010Duna");
  }

  public boolean LocalValidacijaMaster(){
    return true;
  }
  public void RestPanelMPSetup(){
//    MP.setupOneA();
  }
  public boolean ValidacijaMasterExtend(){
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
/* ovo sam sredio u raIzlazTemplate
  public void UpdateStanje(){
  }
*/

  public boolean ValidacijaStanje(){
    return testStanjeRACGRN() ;
  }
  
  public void prepareQuery(String odabrano) {
    boolean pk = !frmParam.getParam("zapod", "parToKup", "N",
      "Dodati/brisati slog kupca kod unosa/brisanja partnera (D,N,A)?").equalsIgnoreCase("N");    
    String ck = MP.panelBasic.rpku.jraCkupac.getText();
    if (!pk) {
      if (ck.length() == 0) ck = "";//ck = " and 1=0";
      else ck = " and rn.ckupac = " + ck;
      dodatakRN = " and rn.kuppar = 'K'" + ck;
    } else {
      if (ck.length() == 0) dodatakRN = "";
      else {
        DataSet par = Partneri.getDataModule().getTempSet("ckupac = " + ck);
        par.open();
        if (par.rowCount() == 0)
          dodatakRN = " and rn.kuppar = 'K' and rn.ckupac = " + ck;
        else dodatakRN = " and ((rn.kuppar = 'K' and rn.ckupac = " + ck + 
          ") or (rn.kuppar = 'P' and rn.ckupac = " + par.getInt("CPAR") + "))"; 
      }
    }
//    dodatak = "";
    super.prepareQuery(odabrano);
/*
    if (qDS != null) qDS = null;
    qDS = new QueryDataSet();
    qDS.close();
    qDS.closeStatement();

    if (odabrano.equals("RN")) {
    qDS.setQuery(new QueryDescriptor(dm.getDatabase1(),
            aSS.getS4raCatchDocRN(val.findYear(pressel.getSelRow().getTimestamp("DATDOK-to")),
                                pressel.getSelRow().getString("CSKL"),
                                dodatakRN)));
    }
    else {
    qDS.setQuery(new QueryDescriptor(dm.getDatabase1(),
            aSS.getS4raCatchDoc(val.findYear(pressel.getSelRow().getTimestamp("DATDOK-to")),
                                pressel.getSelRow().getString("CSKL"),
                                odabrano,dodatak)));
    }
    qDS.open();
*/
  }
  public void RestPanelSetup(){
    DP.addRestGRNGOT();
  }
  
//	public boolean DeleteCheckDetailLocal(){
//	      if (getDetailSet().getString("STATUS").equalsIgnoreCase("P")){
//	          JOptionPane.showConfirmDialog(this.raDetail,
//	                  "Za ovaj je dokument veæ napravljena otpremnica i nije ga dozvoljeno brisati !!!" 
//	          		,"Gre\u0161ka",JOptionPane.DEFAULT_OPTION,
//					JOptionPane.ERROR_MESSAGE);
//	          return false;
//	      }
//	      return true;
//	}  
  
  public boolean DodatnaValidacijaDetail() {
    if (val.isEmpty(DP.jtfKOL)) return false;
    if (val.isEmpty(DP.jraFMC)) return false;
    if (manjeNula()) return false;
    if (raDetail.getMode()=='I'){
      if (getDetailSet().getString("STATUS").equalsIgnoreCase("P")){
        if (rKD.stavkaold.kol.compareTo(rKD.stavka.kol)!=0){
          JOptionPane.showConfirmDialog(this.raDetail,
                                        "Ne smije se mijenjati kolièina jer je za ovu stavku veæ napravljena otpremnica !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
          return false;
        }
      }
    }

    return isPriceToBig(true);
//    return true;
  }
  public void Funkcija_ispisa_detail(){
    if (frmPlacanje.justCheckRate(getMasterSet())) { // ovdje dolazi sinišina provjera rata
      isDetailInitIspis = false;
      super.Funkcija_ispisa_detail();
    }
    else {
      JOptionPane.showConfirmDialog(this.raDetail,"Iznos pla\u0107anja je nejednak iznosu ra\u010Duna !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    }
  }

  public void Funkcija_ispisa_master(){
  isMasterInitIspis = false;
    super.Funkcija_ispisa_master();
  }
  public void cskl2csklart(){
    getDetailSet().setString("REZKOL", "D");
  }
  public boolean ValidacijaPrijeIzlazaDetail() {
  	System.out.println(hr.restart.sisfun.frmParam.getParam
            ("robno","autoOTP","N","Automatska izrada OTP iz RAC-a i GRN-a"));
    if (!frmPlacanje.checkRate(this)) return false;
  	
  	if (hr.restart.sisfun.frmParam.getParam
            ("robno","autoOTP","N","Automatska izrada OTP iz RAC-a i GRN-a")
            .equalsIgnoreCase("N")){
          return true;
        }
  	

    if (hr.restart.util.Util.getNewQueryDataSet("select * from stdoki where cskl='"+
        getMasterSet().getString("CSKL")+"' and vrdok='"+getMasterSet().getString("VRDOK")+
        "' and god='"+getMasterSet().getString("GOD")+"' and brdok="+getMasterSet().getInt("BRDOK")+" and status='N'",true).getRowCount()==0){
      return true;
    }
    raAutoOtpfromRacMask rAOFRM = new raAutoOtpfromRacMask();
    rAOFRM.setCskl(getMasterSet().getString("CSKL"));
    rAOFRM.setBrdok(getMasterSet().getInt("BRDOK"));
    rAOFRM.setGod(getMasterSet().getString("GOD"));
    rAOFRM.setVrdok(getMasterSet().getString("VRDOK"));
    return rAOFRM.start();
  }

  public boolean LocalDeleteCheckDetail() {
		if (isCurrentOtrpremnicaExist()) {
	      	return (javax.swing.JOptionPane
					.showConfirmDialog(this.raDetail,
							"Za ovaj dokument su napravljene otpremnice. Da li želite nastaviti ?",
							"Upit", javax.swing.JOptionPane.YES_NO_OPTION,
							javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.YES_OPTION);
			
			
			
//			JOptionPane
//					.showConfirmDialog(
//							this.raDetail,
//							"Namoguce izbrisati raèun jer su po njemu veæ napravljene otpremnice !",
//							"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
//							JOptionPane.ERROR_MESSAGE);
//			return false;
		}
		return true;
	}

	public void keyActionDellAllStav() {
		if (isOtremniceExist()) {
			JOptionPane
					.showConfirmDialog(
							this.raDetail,
							"Namoguce izbrisati stavke jer su po njemu veæ napravljene otpremnice !",
							"Poruka", JOptionPane.DEFAULT_OPTION,
							JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (javax.swing.JOptionPane.showConfirmDialog(this,
				"Obrisati sve stavke ?", "Upit",
				javax.swing.JOptionPane.YES_NO_OPTION,
				javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.YES_OPTION) {
			deleteAllDoc rAD = new deleteAllDoc();
			rAD.delStavke(getDetailSet());
			getDetailSet().refresh();
			raDetail.getJpTableView().fireTableDataChanged();
		}
	}  
	

  
}

