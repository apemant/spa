/****license*****************************************************************
**   file: repRekapitulacijaUplata.java
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

import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repRekapitulacijaUplata implements raReportData { //sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  upRekapUplata uru = upRekapUplata.getInstance();
  DataSet ds = uru.getRepSet();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repRekapitulacijaUplata() {
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(ds);
    ru.setDataSet(ds);
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

//  public repRekapitulacijaUplata(int idx) {
//    if(idx==0){
// //      sysoutTEST syst = new sysoutTEST(false);
// //      syst.prn(ds);
//      sumIzGrupe = 0;
//    }
//    ds.goToRow(idx);
//  }

//  public java.util.Enumeration getData() {
//    return new java.util.Enumeration() {
//      {
//        ds.open();
//        ru.setDataSet(ds);
//      }
//      int indx=0;
//      public Object nextElement() {
//
//        return new repRekapitulacijaUplata(indx++);
//      }
//      public boolean hasMoreElements() {
//        return (indx < ds.getRowCount());
//      }
//    };
//  }

//  public void close() {
//  }

  public String getCNACPL(){
    return ds.getString("CNACPL");
  }

  public String getCCNACPL(){
    if (lookupData.getlookupData().raLocate(dm.getNacpl(),"CNACPL",ds.getString("CNACPL")) &&
        dm.getNacpl().getString("FL_GOT").equals("D")) return (0+ds.getString("CNACPL"));
    else return (1+ds.getString("CNACPL"));
  }

  public String getNAZNACPL(){
     return ds.getString("NAZNACPL");
  }

  public String getCBANKA(){
     return ds.getString("CBANKA");
  }

  public String getNAZBANKA(){
    return ds.getString("NAZBANKA");
  }

  public BigDecimal getSUMRATA(){
     return ds.getBigDecimal("SUMRATA");
  }

  public BigDecimal getUKUBANKA(){
     return ds.getBigDecimal("UKUBANKA");
  }

  public BigDecimal getUKUNACPL(){
     return ds.getBigDecimal("UKUNACPL");
  }

  public BigDecimal getUKUPNO(){
     return ds.getBigDecimal("UKUPNO");
  }

  public String getCSKL(){
    return uru.getCSKL();
  }

  public String getNAZSKL(){
    return uru.getNAZSKL();
  }
  
  public String getOPERLABEL(){
   if (uru.getCOPERATER().equals(""))return "";
   return "Operater";
  }
  
  public String getCOPER(){
    return uru.getCOPERATER();
  }
  
  public String getCOPERBAR(){
    return uru.getNAZIVOPERATER();
  }

  public String getPODNASLOV() {
    String podnaslov = uru.getPocDat() + " - " + uru.getZavDat()+"\n"+uru.getMinBr() + " - " + uru.getMaxBr();
    
    return podnaslov; 
  }

  public String getFirstLine(){
    return re.getFirstLine();
  }

  public String getSecondLine(){
    return re.getSecondLine();
  }

  public String getThirdLine(){
    return re.getThirdLine();
  }

  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }

  public String getDOKUMENT(){
    String vrd = uru.getVrstaDokumenta();
    if (vrd.equals("")) return "Svi dokumenti";
    else{
      if(vrd.equals("GOT"))return "Gotovinski raèuni - otpremnice";
      if(vrd.equals("GRN"))return "Gotovinski raèuni";
      if(vrd.equals("GRC"))return "Gotovinski raèuni - POS";
      else return "";
    }
  }

}
