/****license*****************************************************************
**   file: repDugPotPer.java
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

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repDugPotPer implements raReportData {
  private raDateUtil rdu = raDateUtil.getraDateUtil();
  private repMemo rpm = repMemo.getrepMemo();
  private Valid vl = Valid.getValid();
  private DataSet ds = upOpenRac.getInstance().getRepQDS();
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

  public double getCOL1() {
    return ds.getBigDecimal("TOTAL").doubleValue();
  }

  public double getCOL2() {
    return ds.getBigDecimal("NDOSP").doubleValue();
  }

  public double getCOL3() {
    return ds.getBigDecimal("PER1").doubleValue();
  }

  public double getCOL4() {
    return ds.getBigDecimal("PER2").doubleValue();
  }

  public double getCOL5() {
    return ds.getBigDecimal("PER3").doubleValue();
  }

  public double getCOL6() {
    return ds.getBigDecimal("PER4").doubleValue();
  }
  
  public double getCOL7() {
    return ds.getBigDecimal("PER5").doubleValue();
  }
  
  public double getUPL() {
    return ds.getBigDecimal("TOTALU").doubleValue();
  }

  public String getNASLOV() {
    return upOpenRac.getInstance().getNaslov();
  }
  
  public String getPODNASLOV() {
    return upOpenRac.getInstance().getPodnaslov();
  }

  public String getLABELMJESTO() {
    return upOpenRac.getInstance().getLabelMjesto();
  }

  public String getMJESTO() {
    return upOpenRac.getInstance().getMjesto();
  }
  
  public String getKontoLab() {
    return upOpenRac.getInstance().jpk.getKonto() == null || 
           upOpenRac.getInstance().jpk.getKonto().length() == 0 ? "" : "Konto";
  }
  
  public String getKonto() {
    return upOpenRac.getInstance().jpk.getKonto() == null ? "" : 
           upOpenRac.getInstance().jpk.getKonto();
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
    return rdu.dataFormatter(vl.getToday());
  }
}

