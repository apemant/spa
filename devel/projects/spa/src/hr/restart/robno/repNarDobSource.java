/****license*****************************************************************
**   file: repNarDobSource.java
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
import hr.restart.util.lookupData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repNarDobSource extends repIzlazni {
  
//  protected repNarDobSource() {
//   this(true); 
//  }
//
//  protected repNarDobSource(boolean init) {
//    super(init);
//    ds = reportsQuerysCollector.getRQCModule().getQueryDataSet();    
//  }
  
  private String orgs = "";
  private String nazor = "";
  
  public String getCSNS(){
    
    if (orgs.equals("")){
      QueryDataSet oset = dm.getOrgstruktura();
      oset.open();
      orgs = getCSKL();
      lD.raLocate(oset,"CORG",orgs);
      nazor = oset.getString("NAZIV");
    }
   return orgs+"  "+nazor; 
  }
  
  protected void setCurrentDataset(){
    ds = reportsQuerysCollector.getRQCModule().getQueryDataSet();
  }
  
  
  public String getNATEM() {
    return frmNarDob.getInstance().getNaTemelju();
  }
  
  
  public String getCART() {
    int c = ds.getInt("CART");
    
    DataRow artikl = lookupData.getlookupData().raLookup(dm.getArtikli(),"CART",c+"");
    if (artikl != null && !artikl.getString("SIFZANAR").equals("")) {
     return artikl.getString("SIFZANAR"); 
    }
    return super.getCART();
  }
  public String getNAZART() {
    int c = ds.getInt("CART");
    
    DataRow artikl = lookupData.getlookupData().raLookup(dm.getArtikli(),"CART",c+"");
    if (artikl != null && !artikl.getString("NAZORIG").equals("")) {
     return artikl.getString("NAZORIG"); 
    }
    return super.getNAZART();
  }
  
  private static BigDecimal slovima;
  private static BigDecimal slovimap;
  
  public String getSLOVIMA() {
    if (ds.getRow() == 0){
      slovima = Aus.zero2;
    }
    if (isReportValute()) {
      slovima = slovima.add(ds.getBigDecimal("IPRODBP"));
    } else {
      slovima = slovima.add(ds.getBigDecimal("INAB"));
    }
    
    return ut.numToLet(slovima.doubleValue(),
        (isReportValute() ? ds.getString("OZNVAL") : null));
  }
  
  public String getSLOVIMApop() {
	  if (ds.getRow() == 0){
	      slovimap = Aus.zero2;
	    }
      slovimap = slovimap.add(ds.getBigDecimal("IBP"));
      return ut.numToLet(slovimap.doubleValue(), null);
  }
  
  public BigDecimal getNC() {
    if (isReportValute()) {
      return ds.getBigDecimal("FVC");
    }
    return super.getNC();
  }
  
  public BigDecimal getUPRAB(){
    return ds.getBigDecimal("UPRAB1");
  }
  
  public double getIBPNAR(){
    return ds.getBigDecimal("IBP").doubleValue();
  }
  
  public BigDecimal getINAB() {
    if (isReportValute()) {
      return ds.getBigDecimal("IPRODBP");
    }
    return super.getINAB();
  }
  
  public double getINABreal() {
    if (isReportValute()) {
      return ds.getBigDecimal("IPRODBP").doubleValue();
    }
    return super.getINABreal();
  }
}

