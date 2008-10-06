/****license*****************************************************************
**   file: repOtpisIzvjestaj.java
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

import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;

import com.borland.dx.sql.dataset.QueryDataSet;

public class repOtpisIzvjestaj implements raReportData {
  
  raOtpisIzvjestaj roi = raOtpisIzvjestaj.getInstance();
  QueryDataSet ds=null;
  repMemo rpm = repMemo.getrepMemo();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repUtil ru = repUtil.getrepUtil();
  HashMap generalije = null;
  
  static String period = null;
  
  public repOtpisIzvjestaj(){
    ds = roi.getReportSet();
    generalije = roi.getGeneralije();
    ru.setDataSet(ds);
    

    period = "Od ";
    period += rdu.dataFormatter((Timestamp)generalije.get("PDAT"));
    period += " do ";
    period += rdu.dataFormatter((Timestamp)generalije.get("ZDAT"));
    
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
    ds=null;
  }
  
  public String getCCLABEL(){
    return generalije.get("CCLABEL").toString();
  }
  
  public String getCORGCSKL(){
    return generalije.get("CORGCSKL").toString();
  }
    
  public String getNAZORGNAZSKL(){
    return generalije.get("NAZORGNAZSKL").toString();
  }
  
//    CART
//    CART1
//    BC
  
  public String getCARTING(){
    return Aut.getAut().getCARTdependable(ds.getInt("CART")+"",ds.getString("CART1"),ds.getString("BC"));
  }
  
//    NAZART
  
  public String getNAZART(){
    return ds.getString("NAZART");
  }

  public String getSIFNAZART(){
    return Aut.getAut().getCARTdependable(ds.getInt("CART")+"",ds.getString("CART1"),ds.getString("BC")) + "   " + ds.getString("NAZART");
  }
  
//    JM
  
  public String getJM(){
    return ds.getString("JM");
  }
  
//    ZC
  
  public BigDecimal getZC(){
    return ds.getBigDecimal("ZC");
  }
  
//    KOL_IZLAZ
  
  public BigDecimal getKOL_IZLAZ(){
    return ds.getBigDecimal("KOL_IZLAZ");
  }
  
//    VRI_IZLAZ
  
  public double getVRI_IZLAZ(){
    return ds.getBigDecimal("VRI_IZLAZ").doubleValue();
  }
  
//    KOL_OTPIS
  
  public BigDecimal getKOL_OTPIS(){
    return ds.getBigDecimal("KOL_OTPIS");
  }
  
//    VRI_OTPIS
  
  public double getVRI_OTPIS(){
    return ds.getBigDecimal("VRI_OTPIS").doubleValue();
  }
  
//    KOL_OTPIS_DOZ
  
  public BigDecimal getKOL_OTPIS_DOZ(){
    return ds.getBigDecimal("KOL_OTPIS_DOZ");
  }
  
//    VRI_OTPIS_DOZ
  
  public double getVRI_OTPIS_DOZ(){
    return ds.getBigDecimal("VRI_OTPIS_DOZ").doubleValue();
  }
  
//    KOL_RAZ
  
  public BigDecimal getKOL_RAZ(){
    return ds.getBigDecimal("KOL_RAZ");
  }
  
//    VRI_RAZ
  
  public double getVRI_RAZ(){
    return ds.getBigDecimal("VRI_RAZ").doubleValue();
  }
  
//    POSTO
  
  public BigDecimal getPOSTO(){
    return ds.getBigDecimal("POSTO");
  }
  
//  OSTALO :))
  
  public double getKOL_OTPISD(){
    return ds.getBigDecimal("KOL_OTPISD").doubleValue();
  }
  public double getVRI_OTPISD(){
    return ds.getBigDecimal("VRI_OTPISD").doubleValue();
  }
  public double getKOL_OTPISN(){
    return ds.getBigDecimal("KOL_OTPISN").doubleValue();
  }
  public double getVRI_OTPISN(){
    return ds.getBigDecimal("VRI_OTPISN").doubleValue();
  }
  
  
  

  public String getDatumIsp() {
    return rdu.dataFormatter(hr.restart.util.Valid.getValid().getToday());
  }
  
  public String getPeriod() {
    return period;
  }

  public String getFirstLine() {
    return rpm.getFirstLine();
  }

  public String getSecondLine() {
    return rpm.getSecondLine();
  }

  public String getThirdLine() {
    return rpm.getThirdLine();
  }
}
