/****license*****************************************************************
**   file: repUlaziZavTros.java
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
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * @author S.G.
 *
 * Started 2005.02.05
 * 
 */

public class repUlaziZavTros  implements raReportData {
  
  UpUlaziZavTr ulzt = UpUlaziZavTr.getInstance(); 
  DataSet ds = null;
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repMemo rm = repMemo.getrepMemo();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  List zavTros = new ArrayList();
  private static BigDecimal tempIZT;
  repUtil ru = repUtil.getrepUtil();
  
  public repUlaziZavTros() {
    ds = ulzt.getReportSet();
    calculateUkZt();
    rekapitulacija();
    ru.setDataSet(ds);
  }
  
  private void calculateUkZt(){
   tempIZT = Aus.zero2;	
   ds.first();
   do {
     tempIZT = tempIZT.add(ds.getBigDecimal("IZT"));
   } while (ds.next());
   System.out.println("temIZT = " + tempIZT);
  }
  
  public void close() {
    ds=null;
  }
  
  public raReportData getRow(int i) {
    ds.goToRow(i);
    if (ds.getBigDecimal("IZT").compareTo(Aus.zero2) != 0){
      zavTros = ulzt.getZavTr(ds.getString("CSKL"),ds.getString("GOD"),ds.getInt("BRDOK"),ds.getString("VRDOK"));
      
    } else {
      zavTros = null;
    }
    return this;
  }
  
  public int getRowCount() {
    return ds.rowCount();
  }
  
  public String getPodnaslov(){
    return "od "+rdu.dataFormatter(ulzt.getDatumOd())+" do "+rdu.dataFormatter(ulzt.getDatumDo());
  }
  
  public String getCSKL(){
    return ds.getString("CSKL");
  }
  
  
  public String getNazSklad(){
//    return ulzt.getNazSkl();
    try {
    String[] colname = new String[] {"CSKL"};
    return ru.getSomething(colname,dm.getSklad(),"NAZSKL").getString();
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }
  
  public String getBROJ() {
    if (zavTros != null){
      String ztstr = "";
      for (Iterator iter = zavTros.iterator(); iter.hasNext();) {
        
        Object current = iter.next();
        Object[] value = (Object[])current;

        ztstr += "\n"+(String)value[0];
      }
      return ds.getString("BROJ")+ztstr;
    }
    return ds.getString("BROJ");
  }
  
  public String getDATDOK(){
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }
  
  public int getCPAR(){
    return ds.getInt("CPAR");
  }

  public String getNAZPAR(){
    lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",ds.getInt("CPAR")+"");
    if (zavTros != null){
      String ztstr = "";
      for (Iterator iter = zavTros.iterator(); iter.hasNext();) {
        
        Object current = iter.next();
        Object[] value = (Object[])current;

        ztstr += "\n"+(String)value[1];
      }
      return dm.getPartneri().getString("NAZPAR")+ztstr;
    }
    return dm.getPartneri().getString("NAZPAR");
  }
  
  public double getIDOB(){
    return ds.getBigDecimal("IDOB").doubleValue();
  }
  
  public String getIZT(){
    if (zavTros != null){
      String ztstr = "";
      for (Iterator iter = zavTros.iterator(); iter.hasNext();) {
        
        Object current = iter.next();
        Object[] value = (Object[])current;

        ztstr += "\n"+(String)value[2];
      }
      return sgQuerys.getSgQuerys().format(ds.getBigDecimal("IZT"),2)+ztstr;
    }
    return sgQuerys.getSgQuerys().format(ds.getBigDecimal("IZT"),2);
  }
  
  public double getZTZASUM(){
    return ds.getBigDecimal("IZT").doubleValue();
  }
  
  public double getINAB(){
    return ds.getBigDecimal("INAB").doubleValue();
  }

  public String getDatumIsp(){
    return rdu.dataFormatter(Valid.getValid().getToday());
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
  
  private static String RKPCZT;
  private static String RKPNZT;
  private static String RKPIZT;
  private static String SUMRKP;
  private static BigDecimal RKPSUM;
  
  private void rekapitulacija(){
    RKPCZT = "";
    RKPNZT = "";
    RKPIZT = "";
    SUMRKP = "";
    RKPSUM = Aus.zero2;
    
    QueryDataSet sumZt = ulzt.getRekapSet();
    if (sumZt != null) {
    sumZt.first();
      do {
        RKPCZT += sumZt.getShort("CZT") + "\n";
        RKPNZT += sumZt.getString("NZT") + "\n";
        RKPIZT += sgQuerys.getSgQuerys().format(sumZt.getBigDecimal("IZT"), 2) + "\n";
        RKPSUM = RKPSUM.add(sumZt.getBigDecimal("IZT"));
      } while (sumZt.next());
      SUMRKP = sgQuerys.getSgQuerys().format(RKPSUM,2);
      if (RKPSUM.compareTo(tempIZT) != 0){
        RKPCZT += "---";
        RKPNZT += "Nedefinirani ZT";
        RKPIZT += sgQuerys.getSgQuerys().format((tempIZT.subtract(RKPSUM)), 2);
        SUMRKP = sgQuerys.getSgQuerys().format(tempIZT,2);
      }
    } else {
      RKPCZT = "---";
      RKPNZT = "Nema zavisnih troškova";
      RKPIZT = "---";
      SUMRKP = "---";
    }
  }

  public String getRKPCZT() {
    return RKPCZT;
  }
  
  public String getRKPIZT() {
    return RKPIZT;
  }
  
  public String getRKPNZT() {
    return RKPNZT;
  }
  
  public String getSUMRKP() {
    return SUMRKP;
  }
}
