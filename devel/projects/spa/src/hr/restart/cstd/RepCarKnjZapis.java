/****license*****************************************************************
**   file: RepCarKnjZapis.java
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
package hr.restart.cstd;

import hr.restart.robno.Aut;
import hr.restart.robno.raControlDocs;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.sysoutTEST;
import hr.restart.util.reports.raReportData;
import hr.restart.util.reports.raStringCache;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


/**
 * @author S.G.
 *
 * Started 2005.09.22
 * 
 */

public class RepCarKnjZapis implements raReportData {

  DataSet ds;
  repMemo rm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  FrmCarKnjZapis fcp = FrmCarKnjZapis.getInstance();
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  protected raControlDocs rCD = new raControlDocs();
  protected hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();
  
  protected raStringCache cache = new raStringCache();
  
  protected String lastDok = null;
  
  private static String domval = "";
  private static String cval = "";
  private static String nazval = "";
  
  public RepCarKnjZapis(){
    ds = fcp.getReportData();
    
    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//    st.showInFrame(ds,"Dataset Test Frame");
    st.prn(ds);
    
    QueryDataSet dval = hr.restart.baza.Valute.getDataModule().getTempSet("STRVAL = 'N'");
    dval.open();
    domval = dval.getString("OZNVAL");

    QueryDataSet val = ut.getNewQueryDataSet("Select * from valute where oznval = '"+ds.getString("OZNVAL")+"'");
    cval = val.getShort("CVAL")+"";
    nazval = val.getString("NAZVAL");
    
    cache.clear();
  }
  
  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ru.setDataSet(null);
    ds = null;
  }
  
  public String getDOMVAL(){
    return domval;
  }

  public String getFirstLine(){
    return rm.getFirstLine();
  }
  
  public String getSecondLine(){
    return rm.getSecondLine();
  }
  
  public String getThirdLine(){
    return rm.getThirdLine();
  }
  
  // MASTER
  
  public String getCBRDOK(){
    return ds.getString("CBRDOK");
  }
  
  public String getDATDOK(){
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }
  
  public int getCPAR() {
    return ds.getInt("CPAR");
  }

  public String getPRINCIP(){   
    String cached = cache.getValue("NAZPAR", Integer.toString(ds.getInt("CPAR")));
    if (cached != null) return cached;
    lookupData.getlookupData().raLocate(dm.getPartneri(),new String[] {"CPAR"}, new String[] {ds.getInt("CPAR")+""});
    String np = dm.getPartneri().getString("NAZPAR")+"\n"+
    dm.getPartneri().getString("ADR")+"\n"+
    dm.getPartneri().getInt("PBR")+" "+dm.getPartneri().getString("MJ");

    return cache.returnValue(np);
  }
  
  public String getCSKL() {
    return ds.getString("CSKL");
  }

  public String getNAZIVSKLADISTA(){
    String cached = cache.getValue("NAZSKL", ds.getString("CSKL"));
    if (cached != null) return cached;
    lookupData.getlookupData().raLocate(dm.getSklad(),new String[] {"CSKL"}, new String[] {ds.getString("CSKL")});
    String ns = dm.getSklad().getString("NAZSKL");
    
    return cache.returnValue(ns);
  }
  
  public String getRACUNPRINCIPALA(){
    return ds.getString("RACPRI");
  }
  
  public String getCVAL(){
    return cval;
  }

  public String getOZNVAL() {
    return ds.getString("OZNVAL");
  }
  
  public String getNAZVAL(){
    return nazval;
  }
  
  public BigDecimal getTECAJ() {
    return ds.getBigDecimal("TECAJ");
  }
  
  public String getODGOVORNAOSOBA(){
    return ds.getString("ODGOSOBA");
  }
  
  public String getKOMENTAR(){
    return ds.getString("COMMENT");
  }
  
  // END OF MASTER
  
  // RAZNORAZNE ŠIFRE - U DETAIL-u
   
  public String getJCD(){
    return ds.getString("JCD_CCAR")+"/"+ds.getString("JCD_BROJ")+"/"+ds.getString("JCD_CPP37");
  }
  
  public String getJCD_CCAR() {
  	return ds.getString("JCD_CCAR");
  }
  
  public String getJCD_BROJ(){
    return ds.getString("JCD_BROJ");
  }
  
  public String getJCD_DATUM(){
    return rdu.dataFormatter(ds.getTimestamp("JCD_DATUM"));
  }
  
  public String getJCD_CPP37(){
    return ds.getString("JCD_CPP37");
  }
  
  public String getJCD_CCAR_KONAC() {
  	return ds.getString("JCD_CCAR_KONAC");
  }
  
  public String getJCD_BROJ_KONAC(){
    return ds.getString("JCD_BROJ_KONAC");
  }
  
  public String getJCD_DATUM_KONAC(){
    return rdu.dataFormatter(ds.getTimestamp("JCD_DATUM_KONAC"));
  }
  
  public String getJCD_CPP37_KONAC(){
    return ds.getString("JCD_CPP37_KONAC");
  }
  
  
  // KRAJ RAZNIH ŠIFRI - U DETAIL-u----------------

  // DETAIL
  
  public short getRBR(){
    return ds.getShort("RBR");
  }
  
  public String getTARIFNIBROJ(){
    return ds.getString("CTG");
  }
  
  public BigDecimal getSTOPA1(){
    return ds.getBigDecimal("STOPA1");
  }

  public String getCART() {
    String izlaz = Aut.getAut().getIzlazCARTdep(ds);
    if (hr.restart.sisfun.frmParam.getParam("robno","chForSpecial","N","Provjerava specijalne znakove (*,_,+,-) i odrezuje njih plus sve iza njih").equalsIgnoreCase("D")){
      if (Aut.getAut().getIzlazCART().equalsIgnoreCase("BC") && (izlaz.indexOf("*") > 0 || izlaz.indexOf("_") > 0 || izlaz.indexOf("+") > 0 || izlaz.indexOf("-") > 0)){
        int index = 13;
        if (izlaz.indexOf("*") > 0) index = izlaz.indexOf("*");
        if (izlaz.indexOf("_") > 0) index = izlaz.indexOf("_");
        if (izlaz.indexOf("+") > 0) index = izlaz.indexOf("+");
        if (izlaz.indexOf("-") > 0) index = izlaz.indexOf("-");
        izlaz = izlaz.substring(0,index);
      }
    }
    return izlaz;
  }
  
  public String getNAZART(){
    return ds.getString("NAZART");
  }
  
  public String getJM(){
    return ds.getString("JM");
  }
  
  public String getPORIJEKLO(){
    return ds.getString("CSIFDRV");
  }
  
  public String getPREFERENCIJAL(){
    return ds.getString("PREF");
  }
  
  public String getPOTVRDAOPORJEKLU(){
    return ds.getString("CPOTPOR");
  }
  
  public double getKOL(){
  	return ds.getBigDecimal("KOL").doubleValue();
  }
  
  public double getNETTOKG(){
  	return ds.getBigDecimal("NETTO_KG").doubleValue();
  }
  
  public double getKOLICINAKOM(){
  	return ds.getBigDecimal("KOL_KOM").doubleValue();
  }
  
  public double getCIJENAVALUTNA(){
  	return ds.getBigDecimal("CIJENA_VAL").doubleValue();
  }
  
  public double getVRIJEDNOSTVALUTNA(){
  	return ds.getBigDecimal("VRIJEDNOST_VAL").doubleValue();
  }
  
  public double getOSNOVICA(){
  	return ds.getBigDecimal("OSNOVICA").doubleValue();
  }
  
  public double getIZNOSCAR(){
  	return ds.getBigDecimal("IZNOSCAR").doubleValue();
  }
  
  public double getPOSTOPOREZ(){
  	return ds.getBigDecimal("PPOREZ").doubleValue();
  }
  
  public double getPOREZ(){
  	return ds.getBigDecimal("POREZ").doubleValue();
  }
  
  public double getPOSEBNIPOREZ(){
  	return ds.getBigDecimal("TROS").doubleValue();
  }
  
  //END OF DETAIL
}