/****license*****************************************************************
**   file: raRobno.java
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
 * Created on 2005.07.12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.robno;

import hr.restart.baza.Doku;
import hr.restart.baza.Sklad;
import hr.restart.baza.VTZtr;
import hr.restart.baza.doki;
import hr.restart.zapod.OrgStr;

import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class raRobno {

	static boolean isKnjigOK(QueryDataSet qds) {
		String pripadknjig = "";
		for (qds.first(); qds.inBounds(); qds.next()) {
			if (TypeDoc.getTypeDoc().isDocOJ(qds.getString("VRDOK"))) {
				pripadknjig = OrgStr.getOrgStr().getPripKnjig(
						qds.getString("CSKL"));
				if (pripadknjig.equalsIgnoreCase(OrgStr.getKNJCORG(true)))
					return true;
			} else {
				QueryDataSet sklad =Sklad.getDataModule().getTempSet(
						"cskl='" + qds.getString("CSKL") + "'");
				sklad.open();
				pripadknjig = OrgStr.getOrgStr().getPripKnjig(sklad.getString("KNJIG"));
				sklad.close();
				if (pripadknjig.equalsIgnoreCase(OrgStr.getKNJCORG(true)))
					return true;
			}
		}
		return false;
	}

	public static boolean isDocumentExist(String brrac, int cpar, boolean ulaz) {

		QueryDataSet qds;
		if (ulaz) {
			if ((Doku.getDataModule().getRowCount(
					"cpar=" + cpar + " and brrac='" + brrac + "'") + VTZtr
					.getDataModule().getRowCount(
							"cpar=" + cpar + " and brrac='" + brrac + "'")) > 0) {
				qds = Doku.getDataModule().getTempSet(
						"cpar=" + cpar + " and brrac='" + brrac + "'");
				qds.open();
				if (qds.getRowCount() > 0) {
					if (isKnjigOK(qds))
						return true;
				}
				qds.close();
				qds = VTZtr.getDataModule().getTempSet(
						"cpar=" + cpar + " and brrac='" + brrac + "'");
				qds.open();
				if (qds.getRowCount() > 0) {
					boolean forreturn =isKnjigOK(qds);
					qds.close();
					return forreturn;
				}
				qds.close();
			}
			return false;
		} 
			if (doki.getDataModule().getRowCount(
					"cpar=" + cpar + " and pnbz2='" + brrac + "'") > 0) {
				qds = doki.getDataModule().getTempSet(
						"cpar=" + cpar + " and pnbz2='" + brrac + "'");
				qds.open();
				if (qds.getRowCount() > 0) {
					return isKnjigOK(qds);
				}
			}
			return false;
		
	}
}
