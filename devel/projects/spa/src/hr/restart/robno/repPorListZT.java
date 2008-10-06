/****license*****************************************************************
**   file: repPorListZT.java
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

import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;

public class repPorListZT implements raReportData {

  _Main main;
  repMemo rpm = repMemo.getrepMemo();
  upPorezList upl = upPorezList.getInstance();
  DataSet ds = upl.getReportSetZT();
  DataSet sum = upl.getSumSet();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();

  public repPorListZT() {
  }

  // Obvezatno !!! ¡

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ds = null;
  }

  // Obvezatno !!! 

  public String getCSKL(){
    return /*upl.jlrCskl.getText(); //*/ds.getString("CSKL");
  }

  public String getNAZSKL(){
    if (lookupData.getlookupData().raLocate(dm.getSklad(),"CSKL",ds.getString("CSKL")))
      return dm.getSklad().getString("NAZSKL");
    else if (lookupData.getlookupData().raLocate(dm.getOrgstruktura(),"CORG",ds.getString("CSKL")))
      return dm.getOrgstruktura().getString("NAZIV");
    return "Sva skladišta";
  }

//  public java.sql.Date getDATUMDOK(){
//    return java.sql.Date.valueOf(ds.getTimestamp("DATUM").toString().substring(0,10));
//  }
  public String getDATUMDOK(){
    return rdu.dataFormatter(
    hr.restart.util.Util.getUtil().getFirstSecondOfDay(
        ds.getTimestamp("DATUM")));
  }
  public String getBRDOK(){
    if (ds.getShort("RBRC") == 0)
    return ds.getString("CSKL") + "-" + ds.getString("VRDOK") + "-" +
        ds.getString("GOD") + "-" + val.maskZeroInteger(Integer.valueOf(String.valueOf(ds.getInt("BRDOK"))), 6);
    else return ds.getString("CSKL") + "-" + ds.getString("VRDOK") + "-" +
    ds.getString("GOD") + "-" + val.maskZeroInteger(Integer.valueOf(String.valueOf(ds.getInt("BRDOK"))), 6) +
    " ("+ds.getShort("RBRC")+")";
  }
  
  public String getPartner(){
    if (ds.getInt("CPAR") != 0){
      if (lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",ds.getInt("CPAR")+"")){
        return ds.getInt("CPAR")+" - " + dm.getPartneri().getString("NAZPAR");
      } else 
      return ds.getInt("CPAR")+"";
    }
    return "Raèun graðana";
  }
  
  public String getBrojevi(){
    return ds.getString("BROJ");
  }

  public double getUKUPNO(){
    try {
      return ds.getBigDecimal("ISP").doubleValue();
     } catch (Exception e) {
       BigDecimal tmpBD = new BigDecimal(ds.getDouble("ISP"));
       tmpBD.setScale(2,BigDecimal.ROUND_HALF_UP);
       return tmpBD.doubleValue();
     }
//    return ds.getBigDecimal("ISP");
  }

  public double getBEZPOR(){
    try {
     return ds.getBigDecimal("IBP").doubleValue();
    } catch (Exception e) {
      BigDecimal tmpBD = new BigDecimal(ds.getDouble("IBP"));
      tmpBD.setScale(2,BigDecimal.ROUND_HALF_UP);
      return tmpBD.doubleValue();
    }
  }

  public double getPRETPOR(){
     return ds.getBigDecimal("PREPOR").doubleValue();
  }

  Variant v = new Variant();

  public double getOPOR(){
    ds.getVariant("POR",v);
    return v.getAsBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
  }

  public BigDecimal getSUMUKUPNO(){
    return sum.getBigDecimal("SUMISP");
  }

  public BigDecimal getSUMBEZPOR(){
    return sum.getBigDecimal("SUMIBP");
  }

  public BigDecimal getSUMPRETPOREZ(){
    return sum.getBigDecimal("SUMPRPOR");
  }

  public BigDecimal getSUMOPOR(){
    return sum.getBigDecimal("SUMPOR");
  }

  public BigDecimal getRAZLIKAVRATITI(){
    return sum.getBigDecimal("RAZPOVZT");
  }

  public BigDecimal getRAZLIKAPLATIT(){
    return sum.getBigDecimal("RAZPLAZT");
  }

  public String getNASLOV(){
    return "POREZ ZA RAZDOBLJE od " + rdu.dataFormatter(upl.getDatumOD()) + 
    	   " do " + rdu.dataFormatter(upl.getDatumDO());
  }

  public String getFirstLine(){
    return rpm.getFirstLine();
  }
  public String getSecondLine(){
    return rpm.getSecondLine();
  }
  public String getThirdLine(){
    return rpm.getThirdLine();
  }
  public String getDatumIsp(){
    return rdu.dataFormatter(val.getToday());
  }
}
