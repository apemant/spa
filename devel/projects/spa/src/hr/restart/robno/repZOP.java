/****license*****************************************************************
**   file: repZOP.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;

public class repZOP implements raReportData {

  upZOP up = upZOP.getInstance();
  DataSet ds = up.getDataSet();
  repMemo rpm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  lookupData ld = lookupData.getlookupData();
  boolean tst;

  public repZOP() {
    tst = frmParam.getParam("robno","ispLogo","N",
        "Ispis loga na dokumentima (D/N)").equals("D");
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }
  
  public int getRowCount() {
    return ds.rowCount();
  }
  
  public void close() {
    //
  }
  
  public String getCSKL() {
    return up.getCSKL();
  }

  public String getNAZSKL() {
    return up.getNAZSKL();
  }

  public int getRBR() {
    return ds.getRow() + 1;
  }
  
  public String SgetDATDOK() {
    return Aus.formatTimestamp(ds.getTimestamp("DATDOK"));
  }
  
  public String getFormatBroj() {
    return "br. " + up.getBroj() + "/" + up.getGOD();
  }
  
  public String getBROJDOK() {
    return ds.getString("VRDOK") + "-" + getCSKL() + "/" + up.getGOD() + 
            "-" + vl.maskZeroInteger(new Integer(ds.getInt("BRDOK")), 6);
  }
  
  public double getIPRODSP() {
    return ds.getBigDecimal("IPRODSP").doubleValue();
  }
  
  public double getIRAZ() {
    return ds.getBigDecimal("IRAZ").doubleValue();
  }
  
  public double getUIRAB() {
    return ds.getBigDecimal("UIRAB").doubleValue();
  }
  
  public String getFirstLine(){
    return tst ? rpm.getFirstLine() : "";
  }
  public String getSecondLine(){
    return tst ? rpm.getSecondLine() : "";
  }
  public String getThirdLine(){
    return tst ? rpm.getThirdLine() : "";
  }
  public String getDatumIsp(){
    return tst ? rdu.dataFormatter(vl.getToday()) : "";
  }
}

