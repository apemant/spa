/****license*****************************************************************
**   file: repDugPot.java
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
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;

public class repDugPot implements raReportData {

  frmDugPot fdp = frmDugPot.getInstance();
  DataSet ds = fdp.getrepQDS();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  Integer[] selection;

  public repDugPot() {
    selection = (Integer[]) fdp.jp.getSelection();
  }

  public raReportData getRow(int idx) {
    if (selection == null || selection.length == 0) ds.goToRow(idx);
    else lookupData.getlookupData().raLocate(ds, "CPAR", selection[idx].toString());
    return this;
  }

  public int getRowCount() {
    return selection == null || selection.length == 0 ? ds.getRowCount() : selection.length;
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

  public double getCOL1(){
     return ds.getBigDecimal("RAC").doubleValue();
  }

  public double getCOL2(){
     return ds.getBigDecimal("UPL").doubleValue();
  }

  public double getCOL3(){
     return ds.getBigDecimal("SALDO").doubleValue();
  }

  public double getCOL4(){
    return 0;
  }
  
  public String getKontoLab() {
    return fdp.konto == null || fdp.konto.length() == 0 ? "" : "Konto";
  }
  
  public String getKonto() {
    return fdp.konto == null ? "" : fdp.konto;
  }

  public String getMJESTO(){
     return fdp.getMjesto();
  }
  public String getLABELMJESTO(){
    return fdp.getLabelMjesto();
  }
  public String getNASLOV(){
    return "\n"+fdp.getNaslov();
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
}

