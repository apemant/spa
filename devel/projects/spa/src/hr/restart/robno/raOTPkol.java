/****license*****************************************************************
**   file: raOTPkol.java
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
/*
 * Created on 2005.04.20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.robno;

import com.borland.dx.dataset.DataSet;
import com.borland.jb.util.TriStateProperty;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class raOTPkol extends raOTP {

	public void ConfigViewOnTable() {
		setVisibleColsMaster(new int[] { 4, 5, 12, 13, 29 });
		setVisibleColsDetail(new int[] { 4,
				Aut.getAut().getCARTdependable(5, 6, 7), 8, 9, 10 });
	}

	public void zamraciMaster(DataSet ds) {
	}

	public void zamraciDetail(DataSet ds) {

		ds.getColumn("CRADNAL").setVisible(TriStateProperty.FALSE);
		ds.getColumn("UPRAB").setVisible(TriStateProperty.FALSE);
		ds.getColumn("UIRAB").setVisible(TriStateProperty.FALSE);
		ds.getColumn("UPZT").setVisible(TriStateProperty.FALSE);
		ds.getColumn("UIZT").setVisible(TriStateProperty.FALSE);
		ds.getColumn("FC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("INETO").setVisible(TriStateProperty.FALSE);
		ds.getColumn("FVC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IPRODBP").setVisible(TriStateProperty.FALSE);
		ds.getColumn("POR1").setVisible(TriStateProperty.FALSE);
		ds.getColumn("POR2").setVisible(TriStateProperty.FALSE);
		ds.getColumn("POR3").setVisible(TriStateProperty.FALSE);
		ds.getColumn("FMC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IPRODSP").setVisible(TriStateProperty.FALSE);
		ds.getColumn("NC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("INAB").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IMAR").setVisible(TriStateProperty.FALSE);
		ds.getColumn("VC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IBP").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IPOR").setVisible(TriStateProperty.FALSE);
		ds.getColumn("MC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("ISP").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IRAZ").setVisible(TriStateProperty.FALSE);
		ds.getColumn("BRPRI").setVisible(TriStateProperty.FALSE);
		ds.getColumn("RBRPRI").setVisible(TriStateProperty.FALSE);
		ds.getColumn("PPOR1").setVisible(TriStateProperty.FALSE);
		ds.getColumn("PPOR2").setVisible(TriStateProperty.FALSE);
		ds.getColumn("PPOR3").setVisible(TriStateProperty.FALSE);
		ds.getColumn("CARTNOR").setVisible(TriStateProperty.FALSE);
		ds.getColumn("FMCPRP").setVisible(TriStateProperty.FALSE);
		ds.getColumn("REZKOL").setVisible(TriStateProperty.FALSE);
		ds.getColumn("VEZA").setVisible(TriStateProperty.FALSE);
		ds.getColumn("ID_STAVKA").setVisible(TriStateProperty.FALSE);
		ds.getColumn("ZC").setVisible(TriStateProperty.FALSE);

	}

	public void stozbroiti() {
		set_kum_detail(false);
	}

	public void RestPanelSetup() {
		DP.addRestOnlyKol();
	}
}