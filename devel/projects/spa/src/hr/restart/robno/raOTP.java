/****license*****************************************************************
**   file: raOTP.java
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

import com.borland.dx.dataset.DataSet;
import com.borland.jb.util.TriStateProperty;

public class raOTP extends raIzlazTemplate {

	public void zamraciMaster(DataSet ds) {
	}

	public void zamraciDetail(DataSet ds) {

		ds.getColumn("CRADNAL").setVisible(TriStateProperty.TRUE);
		ds.getColumn("UPRAB").setVisible(TriStateProperty.TRUE);
		ds.getColumn("UIRAB").setVisible(TriStateProperty.TRUE);
		ds.getColumn("UPZT").setVisible(TriStateProperty.TRUE);
		ds.getColumn("UIZT").setVisible(TriStateProperty.TRUE);
		ds.getColumn("FC").setVisible(TriStateProperty.TRUE);
		ds.getColumn("INETO").setVisible(TriStateProperty.TRUE);
		ds.getColumn("FVC").setVisible(TriStateProperty.TRUE);
		ds.getColumn("IPRODBP").setVisible(TriStateProperty.TRUE);
		ds.getColumn("POR1").setVisible(TriStateProperty.TRUE);
		ds.getColumn("POR2").setVisible(TriStateProperty.TRUE);
		ds.getColumn("POR3").setVisible(TriStateProperty.TRUE);
		ds.getColumn("FMC").setVisible(TriStateProperty.TRUE);
		ds.getColumn("IPRODSP").setVisible(TriStateProperty.TRUE);
		ds.getColumn("NC").setVisible(TriStateProperty.TRUE);
		ds.getColumn("INAB").setVisible(TriStateProperty.TRUE);
		ds.getColumn("IMAR").setVisible(TriStateProperty.TRUE);
		ds.getColumn("VC").setVisible(TriStateProperty.TRUE);
		ds.getColumn("IBP").setVisible(TriStateProperty.TRUE);
		ds.getColumn("IPOR").setVisible(TriStateProperty.TRUE);
		ds.getColumn("MC").setVisible(TriStateProperty.TRUE);
		ds.getColumn("ZC").setVisible(TriStateProperty.TRUE);
		ds.getColumn("ISP").setVisible(TriStateProperty.TRUE);
		ds.getColumn("IRAZ").setVisible(TriStateProperty.TRUE);
		ds.getColumn("BRPRI").setVisible(TriStateProperty.TRUE);
		ds.getColumn("RBRPRI").setVisible(TriStateProperty.TRUE);
		ds.getColumn("PPOR1").setVisible(TriStateProperty.TRUE);
		ds.getColumn("PPOR2").setVisible(TriStateProperty.TRUE);
		ds.getColumn("PPOR3").setVisible(TriStateProperty.TRUE);
		ds.getColumn("CARTNOR").setVisible(TriStateProperty.TRUE);
		ds.getColumn("FMCPRP").setVisible(TriStateProperty.TRUE);
		ds.getColumn("REZKOL").setVisible(TriStateProperty.TRUE);
		ds.getColumn("VEZA").setVisible(TriStateProperty.TRUE);
		ds.getColumn("ID_STAVKA").setVisible(TriStateProperty.TRUE);

	}

	public void initialiser() {
		what_kind_of_dokument = "OTP";
	}

	public void MyaddIspisMaster() {
		//    raMaster.getRepRunner().addReport("hr.restart.robno.repOTP","Otpremnica",2);
		//    raMaster.getRepRunner().addReport("hr.restart.robno.repOTPvri","Otpremnica
		// vrijednosna",2);
		raMaster.getRepRunner().addReport("hr.restart.robno.repOTP",
				"hr.restart.robno.repOTP", "OTP", "Otpremnica");
		raMaster.getRepRunner().addReport("hr.restart.robno.repOTPvri",
				"hr.restart.robno.repOTPvri", "OTPvri",
				"Otpremnica vrijednosna");
		raMaster.getRepRunner().addReport("hr.restart.robno.repOTPSKL",
            "hr.restart.robno.repIzlazni","OTP2",
            "Otpremnica dvije jedinice mjere");
		raMaster.getRepRunner().addReport("hr.restart.robno.repOTPsif",
				"hr.restart.robno.repRacuniPnP",
				"OTPsifKup",
				"Otpremnica sa šifrom kupca");
		raMaster.getRepRunner().addReport("hr.restart.robno.repMxOTP",
				"Matri\u010Dni ispis otpremnice");
	}

	public void MyaddIspisDetail() {
		//    raDetail.getRepRunner().addReport("hr.restart.robno.repOTP","Otpremnica",2);
		//    raDetail.getRepRunner().addReport("hr.restart.robno.repOTPvri","Otpremnica
		// vrijednosna",2);
		raDetail.getRepRunner().addReport("hr.restart.robno.repOTP",
				"hr.restart.robno.repOTP", "OTP", "Otpremnica");
		raDetail.getRepRunner().addReport("hr.restart.robno.repOTPvri",
				"hr.restart.robno.repOTPvri", "OTPvri",
				"Otpremnica vrijednosna");
		raDetail.getRepRunner().addReport("hr.restart.robno.repOTPSKL",
                "hr.restart.robno.repIzlazni","OTP2",
                "Otpremnica dvije jedinice mjere");
		raDetail.getRepRunner().addReport("hr.restart.robno.repOTPsif",
				"hr.restart.robno.repRacuniPnP",
				"OTPsifKup",
				"Otpremnica sa šifrom kupca");
		raDetail.getRepRunner().addReport("hr.restart.robno.repMxOTP",
				"Matri\u010Dni ispis otpremnice");
	}

	public void ConfigViewOnTable() {
		this.setVisibleColsMaster(new int[] { 4, 5, 6, 31, 32, 34 }); // Requested
//		// by
//		// Mladen
//		// (Siniša)
//		this
//				.setVisibleColsDetail(new int[] { 4,
//						Aut.getAut().getCARTdependable(5, 6, 7), 8, 11, 16, 12,
//						19, 24 });
		setVisibleColsDetail(new int[] { 4,
				Aut.getAut().getCARTdependable(5, 6, 7), 8, 9, 11, 33, 34 });
		
	}

	public raOTP() {
		setPreSel((jpPreselectDoc) presOTP.getPres());

		master_titel = "Otpremnice";
		detail_titel_mno = "Stavke otpremnice";
		detail_titel_jed = "Stavka otpremnice";
		// Added by Siniša
		stozbroiti();
		ConfigViewOnTable();
		zamraciMaster(dm.getZagOtp());
		zamraciDetail(dm.getStOtp());
		setMasterSet(dm.getZagOtp());
		setDetailSet(dm.getStOtp());
		
		raDetail.addOption(rnvKartica,4, false);
		
		MP.BindComp();
		DP.BindComp();
		DP.jraLOT.setDataSet(getDetailSet());
		DP.jraPAK.setDataSet(getDetailSet());
	}

	public void stozbroiti(){
		stozbrojiti_detail(new String[] { "INAB", "IMAR", "IPOR", "IRAZ" });
		setnaslovi_detail(new String[] { "Razduženje nab. vri.",
				"Razduženje marže", "Razduženje poreza", "Razduženje zalihe" });
		set_kum_detail(true);		
	}
	
	
	public void SetFokusMaster(char mode) {
		if (mode == 'N') {
			MP.panelBasic.jrbPartner.setSelected(true);
			MP.panelBasic.partnerSelected(true);
			MP.panelBasic.jrbKupac.setSelected(false);
			MP.panelBasic.kupacSelected(false);
			pressel.copySelValues();
			SetFocusNovi();
		} else if (mode == 'I') {
			if (getMasterSet().getInt("CPAR") != 0) {
				MP.panelBasic.jrbPartner.setSelected(true);
				MP.panelBasic.partnerSelected(true);
				MP.panelBasic.jrbKupac.setSelected(false);
				MP.panelBasic.kupacSelected(false);
			} else { //if (getMasterSet().getInt("CKUPAC") != 0) {
				MP.panelBasic.jrbPartner.setSelected(false);
				MP.panelBasic.partnerSelected(false);
				MP.panelBasic.jrbKupac.setSelected(true);
				MP.panelBasic.kupacSelected(true);
			}
		} else if (mode == 'B') {
			if (getMasterSet().getInt("CPAR") != 0) {
				MP.panelBasic.jrbPartner.setSelected(true);
			} else {//if (getMasterSet().getInt("CKUPAC") != 0) {
				MP.panelBasic.jrbKupac.setSelected(true);
			}
			MP.panelBasic.enableAll(false);
		}
		findBRDOK();
	}

	public boolean LocalValidacijaMaster() {
		if (MP.panelBasic.jrbPartner.isSelected()) {
			if (val.isEmpty(MP.panelBasic.jrfCPAR))
				return false;
			return true;
		}
		//    if (MP.panelBasic.jrbKupac.isSelected()) {
		//      if (val.isEmpty(MP.panelBasic.rpku.jraPrezime)) return false;
		//      return true;
		//    }
		return true;
	}

	public boolean DodatnaValidacijaDetail() {
		return true;
	}

	public void RestPanelSetup() {
		DP.addRestOTP();
		DP.addLOT();
	}

	public void RestPanelMPSetup() {
		//    MP.setupTwo();
	}

	public boolean ValidacijaMasterExtend() {
		MP.panelBasic.rpku.updateRecords();
		return true;
	}
}