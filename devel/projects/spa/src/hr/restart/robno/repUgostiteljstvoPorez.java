/****license*****************************************************************
**   file: repUgostiteljstvoPorez.java
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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;

import com.borland.dx.dataset.DataSet;

public class repUgostiteljstvoPorez implements raReportData {
  RaUgostiteljstvoPorez ugo = RaUgostiteljstvoPorez.getInstance();
  DataSet ds = null;
  HashMap zaglavlje = null;

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo rm = repMemo.getrepMemo();

  

  public repUgostiteljstvoPorez() {
    ds = ugo.getRepSet();
    zaglavlje = ugo.getZaglavlje();
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
  
  public String getPodnaslov(){
   String podnaslov = "U periodu od";
   try {
   Timestamp datumod = (Timestamp) zaglavlje.get("DOD");
   Timestamp datumdo = (Timestamp) zaglavlje.get("DDO");
   podnaslov += rdu.dataFormatter(datumod) + " do " + rdu.dataFormatter(datumdo);
   } catch (Exception e) {
    e.printStackTrace();
  }
   
   return podnaslov;
  }
  
  public String getCSKL(){
   return zaglavlje.get("CUO").toString(); 
  }
  
  public String getNAZSKL(){
    return zaglavlje.get("NUO").toString();
  }
  
  public String getCGRART(){
    return ds.getString("CGRART");
  }
  
  public String getNAZGRART(){
    return ds.getString("NAZGRART");
  }
  
  public String getCPOR(){
    return ds.getString("CPOR");
  }

  public String getNAZPOR(){
    return ds.getString("NAZPOR");
  }
  
  public BigDecimal getPPOR(){
    return ds.getBigDecimal("PPOR");
  }
  
  public BigDecimal getOSN(){
    return ds.getBigDecimal("OSN");
  }
  
  public double getIPOR(){
    return ds.getBigDecimal("IPOR").doubleValue();
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

  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }
}
