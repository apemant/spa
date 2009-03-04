/****license*****************************************************************
**   file: raKalkulBDUlaz.java
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

import hr.restart.util.Aus;

import java.math.BigDecimal;

public class raKalkulBDUlaz {
	private TypeDoc TD = TypeDoc.getTypeDoc();
	private BigDecimal Sto = new BigDecimal("100.00");
	private BigDecimal Jedan = new BigDecimal("1.00");
	private BigDecimal Nula = Aus.zero2;
	private BigDecimal tmpBD = Aus.zero2;
	private BigDecimal tmpBD2 = Aus.zero2;
	private BigDecimal tmpBDMC = Aus.zero2;
	private BigDecimal tmpPosto = Aus.zero2;
	public raFakeBDStdoku stavka = new raFakeBDStdoku();
	public raFakeBDStdoku stavkaold = new raFakeBDStdoku();
	public raFakeBDStanje stanje = new raFakeBDStanje();
	public String vrzal = "";

	public raKalkulBDUlaz() {
		super();
	}
	
	public void delStanje(){
		stanje.kolul=stanje.kolul.subtract(stavkaold.kol);
		stanje.nabul=stanje.nabul.subtract(stavkaold.inab);
/*		stanje.vc = stavka.svc;
		stanje.mc = stavka.smc;*/
		stanje.porul = stanje.porul.subtract(stavkaold.ipor);
		stanje.porul = stanje.porul.subtract(stavkaold.dioporpor);
		stanje.marul = stanje.marul.subtract(stavkaold.imar);
		stanje.marul = stanje.marul.subtract(stavka.dioporpor);
		stanje.vul = stanje.vul.subtract(stavkaold.izad);
		stanje.vul = stanje.vul.subtract(stavkaold.porav);
		stanje.vri = stanje.vul.subtract(stanje.viz);
		stanje.kol = stanje.kolul.subtract(stanje.koliz);
		if (stanje.kol.doubleValue() !=0){
			tmpBD = stanje.nabul.subtract(stanje.nabiz);
			stanje.nc = tmpBD.divide(stanje.kol,2,BigDecimal.ROUND_HALF_UP);
		} else if (stanje.kolul.doubleValue() !=0) {
	        stanje.nc = stanje.nabul.divide(stanje.kolul,2,BigDecimal.ROUND_HALF_UP);
		} else {
			stanje.nc = Nula;
		}
		
		if (vrzal.equalsIgnoreCase("N")){
			stavka.zc = stavka.nc;
			
		} else if (vrzal.equalsIgnoreCase("V")){
			stavka.zc = stavka.vc;
			
		} else if (vrzal.equalsIgnoreCase("M")){
			stavka.zc = stavka.mc;
			
		}
	}
	
	public void updateStanje(){
	
		stanje.kolul=stanje.kolul.add(stavka.kol).subtract(stavkaold.kol);
		stanje.nabul=stanje.nabul.add(stavka.inab).subtract(stavkaold.inab);
		stanje.nc = stavka.nc;
		stanje.vc = stavka.vc;
		stanje.mc = stavka.mc;
		stanje.porul = stanje.porul.add(stavka.ipor).subtract(stavkaold.ipor);
		stanje.porul = stanje.porul.add(stavka.dioporpor).subtract(stavkaold.dioporpor);
		stanje.marul = stanje.marul.add(stavka.imar).subtract(stavkaold.imar);
		stanje.marul = stanje.marul.add(stavka.diopormar).subtract(stavka.dioporpor);
		stanje.vul = stanje.vul.add(stavka.izad).subtract(stavkaold.izad);
		stanje.vul = stanje.vul.add(stavka.porav).subtract(stavkaold.porav);
		stanje.vri = stanje.vul.subtract(stanje.viz);
		stanje.kol = stanje.kolul.subtract(stanje.koliz);
		
System.out.println("vrzal "+vrzal);		
		if (vrzal.equalsIgnoreCase("N")){
			if (stanje.kol.doubleValue()!=0){
				stanje.zc = stanje.vri.divide(stanje.kol,2,BigDecimal.ROUND_HALF_UP);
				stanje.nc = stanje.zc;
			}
			stavka.zc = stavka.nc;
		} else if (vrzal.equalsIgnoreCase("V")){
			stavka.zc = stavka.vc;
			stanje.zc = stanje.vc;
			
		} else if (vrzal.equalsIgnoreCase("M")){
			stavka.zc = stavka.mc;
			stanje.zc = stanje.mc;			
		}
	}
}