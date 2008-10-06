/****license*****************************************************************
**   file: frmKonsignacijaNalog.java
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

public class frmKonsignacijaNalog extends raIzlazTemplate {
	

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
		what_kind_of_dokument = "KON";
	}

	public void MyaddIspisMaster() {
	}

	public void MyaddIspisDetail() {
	}

	public void ConfigViewOnTable() {
		this.setVisibleColsMaster(new int[] { 4, 5, 6, 31, 32, 34 }); // Requested
		setVisibleColsDetail(new int[] { 4,
				Aut.getAut().getCARTdependable(5, 6, 7), 8, 9, 11, 33, 34 });
		
	}

	public frmKonsignacijaNalog() {
		setPreSel((jpPreselectDoc) presKON.getPres());
		master_titel = "Nalozi za odjavu";
		detail_titel_mno = "Stavke naloga za odjavu";
		detail_titel_jed = "Stavka naloga za odjavu";
		// Added by Siniša
		stozbroiti();
		ConfigViewOnTable();
		zamraciMaster(dm.getZagKon());
		zamraciDetail(dm.getStKon());
		setMasterSet(dm.getZagKon());
		setDetailSet(dm.getStKon());
		MP.BindComp();
		DP.BindComp();
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
	}

	public void RestPanelMPSetup() {
		//    MP.setupTwo();
	}

	public boolean ValidacijaMasterExtend() {
		MP.panelBasic.rpku.updateRecords();
		return true;
	}
}