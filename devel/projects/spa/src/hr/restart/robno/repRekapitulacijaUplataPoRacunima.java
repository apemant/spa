/****license*****************************************************************
**   file: repRekapitulacijaUplataPoRacunima.java
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
import hr.restart.util.Valid;
import hr.restart.util.sysoutTEST;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;

public class repRekapitulacijaUplataPoRacunima implements raReportData { //sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  upRekapUplata uru = upRekapUplata.getInstance();
  DataSet ds = uru.getRepSetPR();
  DataSet rekap = uru.getRepSet();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repRekapitulacijaUplataPoRacunima() {
    ru.setDataSet(ds);
    setRekapitulacija();
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
  
  private static String nacin, ukupno;
  private BigDecimal sveukupno;
  
  private void setRekapitulacija(){
    nacin = "";
    ukupno = "";
    sveukupno = Aus.zero2;
    
    sysoutTEST st = new sysoutTEST(false);
    System.err.println("************************************rekapitulacija************************************");
    st.prn(rekap);
    
    rekap.first();
    
    sgQuerys sq = sgQuerys.getSgQuerys();
    
    StorageDataSet stds = new StorageDataSet();
    stds.setColumns(new Column[] {dm.createStringColumn("CNACPL",3),
        						  dm.createStringColumn("NAZNACPL",0),
        						  dm.createBigDecimalColumn("UKUNACPL",2)});
    stds.open();
    do {
      if (!hr.restart.util.lookupData.getlookupData().raLocate(stds,"CNACPL",rekap.getString("CNACPL"))){
        stds.insertRow(false);
        stds.setString("CNACPL",rekap.getString("CNACPL"));
        stds.setString("NAZNACPL",rekap.getString("NAZNACPL"));
        stds.setBigDecimal("UKUNACPL",rekap.getBigDecimal("UKUNACPL"));
      } else {
        continue;
      }
      sveukupno = rekap.getBigDecimal("UKUPNO");
    } while (rekap.next());
    
    stds.first();
    for (int i = 0;; i++) {
      nacin += stds.getString("NAZNACPL");
      ukupno += sq.format(stds.getBigDecimal("UKUNACPL"),2);
      
      if (stds.next()){
       nacin += "\n";
       ukupno += "\n";
      } else break;
    }
    
    System.out.println("nacin   "+nacin);
    System.out.println("ukupno  "+ukupno);
  }
  
  public String getREKNAZIVNP(){
    return nacin;
  }
  
  public String getUKUPNONP(){
    return ukupno;
  }
  
  public double getSVEUKUPNONP(){
    return sveukupno.doubleValue();
  }

//  public repRekapitulacijaUplataPoRacunima(int idx) {
//    if(idx==0){
// //      sysoutTEST syst = new sysoutTEST(false);
// //      syst.prn(ds);
//      sumIzGrupe = 0;
//    }
//    ds.goToRow(idx);
//  }
//
//  public java.util.Enumeration getData() {
//    return new java.util.Enumeration() {
//      {
//        ds.open();
//        ru.setDataSet(ds);
//      }
//      int indx=0;
//      public Object nextElement() {
//
//        return new repRekapitulacijaUplataPoRacunima(indx++);
//      }
//      public boolean hasMoreElements() {
//        return (indx < ds.getRowCount());
//      }
//    };
//  }
//
//  public void close() {
//  }

  public String getCCRACUN(){
//    System.out.println(Integer.parseInt(ds.getString("CRACUN").substring(9,ds.getString("CRACUN").length())));
//    return Integer.parseInt(ds.getString("CRACUN").substring(9,ds.getString("CRACUN").length()));
    String brrac = null; String vrdok = null;
    try{
      brrac = ds.getString("CRACUN").substring(9, ds.getString("CRACUN").length());//vl.maskZeroInteger(Integer.decode(ds.getString("CRACUN").substring(9, ds.getString("CRACUN").length())),
//                                 10);
      vrdok = ds.getString("CRACUN").substring(0, 3);
//      System.out.println(brrac+vrdok);
    } catch(Exception ex){
//      System.out.print(ds.getString("CRACUN").substring(9, ds.getString("CRACUN").length())+"  ");
//      System.out.println(Integer.decode(ds.getString("CRACUN").substring(9, ds.getString("CRACUN").length())));
//      ex.printStackTrace();
    }
    return brrac+vrdok;
  }

  public String getCRACUN(){
    return ds.getString("CRACUN"); //.substring(0,9) + vl.maskZeroInteger(Integer.decode(ds.getString("CRACUN").substring(9, ds.getString("CRACUN").length())),6);
//     return ds.getString("CRACUN");
  }

  public String getCNACPL(){
     return ds.getString("CNACPL");
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

  public BigDecimal getRATA(){
     return ds.getBigDecimal("RATA");
  }

  public BigDecimal getUIRAC(){
     return ds.getBigDecimal("UIRAC");
  }

  public String getDATUMNP(){
    if (ds.getString("CBANKA").equals("")) return "";
     return rdu.dataFormatter(ds.getTimestamp("DATUMNP"));
  }

  public java.sql.Date getSortDate(){
    if (!rdu.dataFormatter(ds.getTimestamp("DATUMNP")).equals(""))
      return java.sql.Date.valueOf(ds.getTimestamp("DATUMNP").toString().substring(0,10));
    return java.sql.Date.valueOf("01-01-1980");
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
    return uru.getPocDat() + " - " + uru.getZavDat();
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
