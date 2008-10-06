/****license*****************************************************************
**   file: raPRV.java
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

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class raPRV extends raIzlazTemplate {
	
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
		what_kind_of_dokument = "PRV";
	}

	public void MyaddIspisMaster() {

		raMaster.getRepRunner().clearAllReports();
		if (getMasterSet().getString("CORG").equals("")
				&& getMasterSet().getString("CRADNIK").equals("")) {
			//       raMaster.getRepRunner().addReport("hr.restart.robno.repPREkol",
			// "Ispis povratnica reversa", 2);
			//       raMaster.getRepRunner().addReport("hr.restart.robno.repPRE",
			// "Ispis povratnica reversa s vrijednostima", 2);
			raMaster.getRepRunner().addReport("hr.restart.robno.repPRVkol",
					"hr.restart.robno.repREV", "PRVkol",
					"Ispis povratnice reversa");
			raMaster.getRepRunner().addReport("hr.restart.robno.repPRV",
					"hr.restart.robno.repREV", "PRV",
					"Ispis povratnice reversa s vrijednostima");
		} else {
			raMaster.getRepRunner().addReport("hr.restart.robno.repPRECorgkol",
					"Ispis povratnica reversa", 2);
			raMaster.getRepRunner().addReport("hr.restart.robno.repPRECorg",
					"Ispis povratnica reversa s vrijednostima", 2);
		}
		isMasterInitIspis = false;
	}

	public void MyaddIspisDetail() {

		raDetail.getRepRunner().clearAllReports();
		if (getMasterSet().getString("CORG").equals("")
				&& getMasterSet().getString("CRADNIK").equals("")) {
			//        raDetail.getRepRunner().addReport("hr.restart.robno.repPREkol",
			// "Ispis povratnica reversa", 2);
			//        raDetail.getRepRunner().addReport("hr.restart.robno.repPRE",
			// "Ispis povratnica reversa s vrijednostima", 2);
			raDetail.getRepRunner().addReport("hr.restart.robno.repPRVkol",
					"hr.restart.robno.repREV", "PRVkol",
					"Ispis povratnice reversa");
			raDetail.getRepRunner().addReport("hr.restart.robno.repPRV",
					"hr.restart.robno.repREV", "PRV",
					"Ispis povratnice reversa s vrijednostima");
		} else {
			raDetail.getRepRunner().addReport("hr.restart.robno.repPRECorgkol",
					"Ispis povratnica reversa", 2);
			raDetail.getRepRunner().addReport("hr.restart.robno.repPRECorg",
					"Ispis povratnica reversa s vrijednostima", 2);
		}
		isDetailInitIspis = false;
	}

	public raPRV() {
		setPreSel(presPRV.getPres());
		master_titel = "Povratnice reversa";
		detail_titel_mno = "Stavke povratnice reversa";
		detail_titel_jed = "Stavka povratnice reversa";
		zamraciMaster(dm.getZagPre());
        zamraciDetail(dm.getStPre());
		setMasterSet(dm.getZagPre());
		setDetailSet(dm.getStPre());
		MP.BindComp();
		DP.BindComp();
		ConfigViewOnTable();
		stozbroiti();

	}

	public boolean ValidacijaStanje() {
		return true;
	}

	public void SetFocusNoviExtends() {
	}

	public void RestPanelMPSetup() {
	}

	public boolean FirstPartValidDetail() {
		if (val.isEmpty(MP.panelRevers.jtfDATDOK))
			return false;
		return true;
	}

	public void RestPanelSetup() {
		DP.addRestOTP();
	}

	public boolean DodatnaValidacijaDetail() {
		getDetailSet()
				.setString("CRADNAL", getMasterSet().getString("CRADNAL"));
		return true;
	}

	public void ConfigViewOnTable() {
	    setVisibleColsMaster(new int[] {4,5,12,13,29});
		setVisibleColsDetail(new int[] { 4,
				Aut.getAut().getCARTdependable(5, 6, 7), 8, 9, 11, 33, 34 });
	}

	public void stozbroiti() {
		set_kum_detail(false);
	}

}