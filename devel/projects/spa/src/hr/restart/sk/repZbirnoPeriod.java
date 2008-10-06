/****license*****************************************************************
**   file: repZbirnoPeriod.java
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
package hr.restart.sk;

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.util.Valid;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;

public class repZbirnoPeriod implements raReportData {

  upZbirnoPeriod uzp = upZbirnoPeriod.getInstance();
  DataSet ds;

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();  

  public repZbirnoPeriod() {
    ds = uzp.getRepQDS();    
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);    
    return this;
  }

  public int getRowCount() {
    return ds.getRowCount();
  }


  public void close() {
    ds = null;
  }

  public int getCPAR(){
    return ds.getInt("CPAR");
  }

  public String getNAZPAR(){
     return ds.getString("NAZPAR");
  }

  public double getPROMETR(){
     return ds.getBigDecimal("PROMETR").doubleValue();
  }

  public double getPROMETU(){
     return ds.getBigDecimal("PROMETU").doubleValue();
  }

  public double getSALDO(){
     return ds.getBigDecimal("SALDO").doubleValue();
  }  

  public String getMJESTO(){
     return uzp.getMjesto();
  }
  public String getLABELMJESTO(){
     return uzp.getLabelMjesto();
  }
  
  public String getKontoLab() {
    return uzp.jpk.getKonto() == null || uzp.jpk.getKonto().length() == 0 ? "" : "Konto";
  }
  
  public String getKonto() {
    return uzp.jpk.getKonto() == null ? "" : uzp.jpk.getKonto();
  }
  
  public String getNASLOV(){
    return uzp.getNaslov();
  }
  
  public String getPODNASLOV(){
    return uzp.getPodnaslov();
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
  public String getLogoMjesto(){
    return re.getLogoMjesto();
  }
  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }
}