/****license*****************************************************************
**   file: repRacuniArtikliPOSDataProvider.java
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
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;

public class repRacuniArtikliPOSDataProvider  implements raReportData {

  protected DataSet ds;
  upRacuniArtikliPOS urap = null;
  repMemo rm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();
  protected hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  reportsQuerysCollector repQC = reportsQuerysCollector.getRQCModule();
  protected String[] colname = new String[] {""};
  protected repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  protected raControlDocs rCD = new raControlDocs();
  protected hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();

  public repRacuniArtikliPOSDataProvider(){
    urap = upRacuniArtikliPOS.getInstance();
    ds = urap.getReportSet();
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
  
  
  public int getCART(){
    return ds.getInt("CART");
  }
  
  public String getNAZART(){
    return ds.getString("NAZART");
  }
  
  public String getCSKL(){
    return ds.getString("CSKL");
  }
  
  public String getNAZSKL(){
    return ds.getString("NAZSKL");
  }
  
  public String getCKASE(){
    return ds.getString("CPRODMJ");
  }
  
  public String getNAZKASE(){
    return ds.getString("NAZKASE");
  }
  
  public double getKOL(){
    return ds.getBigDecimal("KOL").doubleValue();
  }
  
  public String getJM(){
    return ds.getString("JM");
  }
  
  public double getMC(){
    return ds.getBigDecimal("MC").doubleValue();
  }
  
  public double getPOP(){
    return ds.getBigDecimal("POP").doubleValue();
  }
  
  public double getIBP(){
    return ds.getBigDecimal("IBP").doubleValue();
  }
  
  public double getPOR(){
    return ds.getBigDecimal("POR").doubleValue();
  }
  
  public double getISP(){
    return ds.getBigDecimal("ISP").doubleValue();
  }
  
  public int getDUMMYSORT(){
    return 0;
  }

  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
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
}
