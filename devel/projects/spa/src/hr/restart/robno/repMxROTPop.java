/****license*****************************************************************
**   file: repMxROTPOP.java
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

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;

public class repMxROTPop extends repMxROT {
	
	protected void fill() {
		super.fill();
		String [] detail =new String[]{"<#RBR|3|right#>"+". "+"<#SIFRA|20|left#>"+" "+"<#NAZIV|48|left#>"+" "+
	      "<#KOL|12|right#>"+" "+"<#JM|6|right#>"+" "+"<#POREZ|10|right#>"+" "+"<#CIJENA|13|right#>"+" "+"<#IZNOS|16|right#>"};
		this.setDetail(detail);
		
		this.setHeader(new String[]{"                                                                                                                                                         "+"<$DoubleWidthON$>"+"R-1"+"<$DoubleWidthOFF$>"+"<$newline$>"+
        "<$Reset$><$CondensedON$>"+getPartner()+"<$Reset$><$DoubleWidthON$>"+formatStr("RAÈUN-OTPREMNICA br. ",71)+"<$newline$>"+getRacun()+getSkladiste()+
        "----------------------------------------------------------------------------------------------------------------------------------------"+"<$newline$>"+
        "R.B."+" Šifra               "+" Naziv artikla/usluge                            "+"     Kolièina"+"     JM"+"  Porez (%)"+"        Cijena"+"            Iznos"+"<$newline$>"+
"----------------------------------------------------------------------------------------------------------------------------------------"});
  }
	
	protected String getNP(){
		String np = super.getNP();
		
		String rpop =	"              REKAPITULACIJA POPUSTA<$newline$>"+
    							"      Naziv                         "+"   %"+"     Iznos<$newline$>";
		brRedRekPl += 3;
		
		DataSet pops = Aus.q("SELECT * FROM vtrabat where cskl ='"
				+ ds.getString("CSKL") + "' AND VRDOK='"
				+ ds.getString("VRDOK") + "' AND GOD='"
				+ ds.getString("GOD") + "' AND BRDOK="
				+ ds.getInt("BRDOK"));
		
		pops.setSort(new SortDescriptor(new String[] {"CRAB", "PRAB"}));
		pops.first();
		String crab = pops.getString("CRAB");
		BigDecimal prab = pops.getBigDecimal("PRAB");
		BigDecimal irab = Aus.zero2;

		for (pops.first(); pops.inBounds(); pops.next()) {
			if (!pops.getString("CRAB").equals(crab) || pops.getBigDecimal("PRAB").compareTo(prab) != 0) {
				ld.raLocate(dm.getRabati(), "CRAB", crab);
				rpop += "      " + getPadded(dm.getRabati().getString("NRAB"), 30) + 
					getFront(Aus.formatBigDecimal(prab), 5) + getFront(Aus.formatBigDecimal(irab), 10) + "<$newline$>";
				brRedRekPl++;				
				crab = pops.getString("CRAB");
				prab = pops.getBigDecimal("PRAB");
				irab = Aus.zero2; 
			}
			irab = irab.add(pops.getBigDecimal("IRAB"));
		}
		if (irab.signum() != 0) {
			ld.raLocate(dm.getRabati(), "CRAB", crab);
			rpop += "      " + getPadded(dm.getRabati().getString("NRAB"), 30) + 
					getFront(Aus.formatBigDecimal(prab), 5) + getFront(Aus.formatBigDecimal(irab), 10) + "<$newline$>";
			brRedRekPl++;
		}
		
		return rpop + "<$newline$>" + np;  
	}
	
	private String getPadded(String orig, int chars) {
	  if (orig.length() > chars)
	    return orig.substring(0, chars);
	  if (orig.length() < chars)
	    return orig.concat(Aus.string(chars - orig.length(), ' '));
	  return orig;
	}
	private String getFront(String orig, int chars) {
	  if (orig.length() > chars)
	    return orig.substring(0, chars);
	  if (orig.length() < chars)
	    return Aus.string(chars - orig.length(), ' ').concat(orig);
	  return orig;
	}
}
