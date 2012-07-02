/****license*****************************************************************
**   file: raenginePRD.java
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

import hr.restart.baza.Rnser;
import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;

import java.math.BigDecimal;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class raenginePRD {

  dM dm = dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
  StorageDataSet sds = null;
  private BigDecimal faktormat = null;
  private BigDecimal faktorpro = null;
  private BigDecimal faktorrob = null;
  private BigDecimal faktorusl = null;
  private BigDecimal faktorost = null;

  /**
   * prvo treba provjeriti da li je radninalog zatvoren
   */

  public raenginePRD() {
  }

  /**
   * traži izdatnice za radni nalog i redni broj radnog naloga
   * @param cradnal
   * @return
   */

  public QueryDataSet selectIzdatnice(String cradnal,int rbsrn, boolean single){
    String selectIzdat = single ? "select * from stdoki where cradnal = '"+	cradnal+"' and vrdok='IZD'"  
    												: "select * from stdoki where cradnal = '"+
                         cradnal+"' and vrdok='IZD' and rbsrn="+rbsrn;
    return  hr.restart.util.Util.getNewQueryDataSet(selectIzdat,true);
  }

  public void initParams(){

  	faktormat = new BigDecimal( 
  		hr.restart.sisfun.frmParam.getParam("robno", "faktorMat", "1.00",
  			"Koef. kojim se množi suma izd sa sklad tipa M za predatnice"));
  	faktorpro =new BigDecimal(	
  		hr.restart.sisfun.frmParam.getParam("robno", "faktorPro", "1.00",
			"Koef. kojim se množi suma izd sa sklad tipa P za predatnice"));
  	faktorrob = new BigDecimal(	
  		hr.restart.sisfun.frmParam.getParam("robno", "faktorRob", "1.00",
		"Koef. kojim se množi suma izd sa sklad tipa R za predatnice"));
  	faktorusl = new BigDecimal(	
  		hr.restart.sisfun.frmParam.getParam("robno", "faktorUsl", "1.00",
		"Koef. kojim se množe iznosi usluga za predatnice"));
  	faktorost = new BigDecimal(	
  		hr.restart.sisfun.frmParam.getParam("robno", "faktorOst", "1.00",
		"Koef. kojim se množi suma izd sa sklad koji nisu (M,P,R) za pred."));
  
  }
  
  public void prepareVtPred(String cradnal,int rbsrn, boolean single){
  	sds = new StorageDataSet();
  	sds.setColumns(dm.getVTPred().cloneColumns());
  	sds.open();
  	sds.insertRow(false);
  	initParams();
  	QueryDataSet materijal = selectIzdatnice(cradnal,rbsrn, single);
    for (materijal.first();materijal.inBounds();materijal.next()){
    	if (ld.raLocate(dm.getSklad(),"CSKL",materijal.getString("CSKL"))){
    		if (dm.getSklad().getString("TIPSKL").equalsIgnoreCase("M")){
    			sds.setBigDecimal("MAT_I",sds.getBigDecimal("MAT_I").
    					add(materijal.getBigDecimal("IRAZ")));
    			
    		}else if (dm.getSklad().getString("TIPSKL").equalsIgnoreCase("P")){
    			sds.setBigDecimal("PRO_I",sds.getBigDecimal("PRO_I").
    					add(materijal.getBigDecimal("IRAZ")));
    		}else if (dm.getSklad().getString("TIPSKL").equalsIgnoreCase("R")){
    			sds.setBigDecimal("ROB_I",sds.getBigDecimal("ROB_I").
    					add(materijal.getBigDecimal("IRAZ")));
    		}else if (dm.getSklad().getString("TIPSKL").equalsIgnoreCase("L")){
              sds.setBigDecimal("POL_I",sds.getBigDecimal("POL_I").
                  add(materijal.getBigDecimal("IRAZ")));
    		} 
    	} else {
    		throw new RuntimeException("Skladište "+materijal.getString("CSKL")+" nisam uspio pronaæi sa raLocate !!! "); 
    	}
    }
	sds.setBigDecimal("USL_I",iznosTroskovaUsluge(cradnal,rbsrn));
	faktorizacija();
	
  	
  } 
  private void faktorizacija(){
  	sds.setBigDecimal("MAT_F",faktormat);
  	sds.setBigDecimal("MAT_FI",(sds.getBigDecimal("MAT_I").
  			multiply(faktormat).setScale(2,BigDecimal.ROUND_HALF_UP)));
  	sds.setBigDecimal("PRO_F",faktorpro);
  	sds.setBigDecimal("PRO_FI",(sds.getBigDecimal("PRO_I").
  			multiply(faktorpro).setScale(2,BigDecimal.ROUND_HALF_UP)));
  	sds.setBigDecimal("ROB_F",faktorrob);
  	sds.setBigDecimal("ROB_FI",(sds.getBigDecimal("ROB_I").
  			multiply(faktorrob).setScale(2,BigDecimal.ROUND_HALF_UP)));
  	sds.setBigDecimal("USL_F",faktorusl);
  	sds.setBigDecimal("USL_FI",(sds.getBigDecimal("USL_I").
  			multiply(faktorusl).setScale(2,BigDecimal.ROUND_HALF_UP)));
  	sds.setBigDecimal("POL_F",faktorost);
  	sds.setBigDecimal("POL_FI",(sds.getBigDecimal("POL_I").
  			multiply(faktorost).setScale(2,BigDecimal.ROUND_HALF_UP)));
  	sds.setBigDecimal("TOTAL",sds.getBigDecimal("MAT_FI").add(
            sds.getBigDecimal("PRO_FI")).add(
            sds.getBigDecimal("ROB_FI")).add(
            sds.getBigDecimal("USL_FI")).add(
            sds.getBigDecimal("POL_FI")));
  }
  public StorageDataSet getVtPred(){  	
  	return sds;
  }
  
  public BigDecimal getProIzn(){
  	return sds.getBigDecimal("TOTAL");
  }
  
  /*public BigDecimal iznosTroskovaMaterijala(String cradnal,int rbsrn){

  	
  	QueryDataSet materijal = selectIzdatnice(cradnal,rbsrn);
    BigDecimal suma = Aus.zero2;
    
    for (materijal.first();materijal.inBounds();materijal.next()){
//      suma = suma.add(materijal.getBigDecimal("IRAZ"));
      suma = suma.add(materijal.getBigDecimal("INAB"));
    }
    return suma;

  }*/


  public BigDecimal iznosTroskovaUsluge(String cradnal,int rbsrn) {

      BigDecimal suma = Aus.zero2;
      QueryDataSet rns = Rnser.getDataModule().getTempSet("cradnal='"+cradnal+"' and rbsid="+rbsrn);
      rns.open();
      for (rns.first();rns.inBounds();rns.next()){
        suma = suma.add(rns.getBigDecimal("VRI"));
      }
      
      if (rns.rowCount() == 0) {
        String selectRN = "select cart,kol from stdoki where cradnal = '"+
                             cradnal+"' and vrdok='RNL' and rbsid="+rbsrn;
        QueryDataSet radninalog = hr.restart.util.Util.getNewQueryDataSet(selectRN,true);
        QueryDataSet normativUs = Aut.getAut().expandArt(radninalog.getInt("CART"),radninalog.getBigDecimal("KOL"),false);
  
        for (normativUs.first();normativUs.inBounds();normativUs.next()){
          if (raVart.isUsluga(normativUs.getInt("CART"))) {
              //Aut.getAut().artTipa(normativUs.getInt("CART"),"U")) {
            suma = suma.add(dm.getArtikli().getBigDecimal("NC")).multiply(normativUs.getBigDecimal("KOL"));
          }
        }
      }
      
      return suma;
  }
  
  
  public QueryDataSet findArtikl(int cart) {

   return  hr.restart.util.Util.getNewQueryDataSet("SELECT * from Artikli where "+
       "cart="+cart,true);

  }

}