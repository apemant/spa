/****license*****************************************************************
**   file: repZbirno.java
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

public class repZbirno implements raReportData {

  frmZbirno fzb = frmZbirno.getInstance();
  DataSet ds = fzb.getrepQDS();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  Integer[] selection;

  public repZbirno() {
    selection = (Integer[]) fzb.jp.getSelection();
  }

  public raReportData getRow(int i) {
    if (selection == null || selection.length == 0) ds.goToRow(i);
    else lookupData.getlookupData().raLocate(ds, "CPAR", selection[i].toString());
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

  public double getPSRAC(){
     return ds.getBigDecimal("POCSTRAC").doubleValue();
  }

  public double getPSUPL(){
     return ds.getBigDecimal("POCSTUPL").doubleValue();
  }

  public double getPROMETR(){
     return ds.getBigDecimal("PROMETR").doubleValue();
  }

  public double getPROMETU(){
     return ds.getBigDecimal("PROMETU").doubleValue();
  }

  public double getSALDO(){
    if (fzb.ps) 
      return ds.getBigDecimal("SALDO").add(ds.getBigDecimal("POCSTUPL")).
             subtract(ds.getBigDecimal("POCSTRAC")).doubleValue();
    return ds.getBigDecimal("SALDO").doubleValue();
  }
  
  public double getSALDOsaPS(){
    if (!fzb.ps)
      return ds.getBigDecimal("SALDO").add(ds.getBigDecimal("POCSTRAC")).
              subtract(ds.getBigDecimal("POCSTUPL")).doubleValue();
    return ds.getBigDecimal("SALDO").doubleValue();
  }

  public String getMJESTO(){
     return fzb.getMjesto();
  }
  public String getLABELMJESTO(){
     return fzb.getLabelMjesto();
  }
  public String getKontoLab() {
    return fzb.konto == null || fzb.konto.length() == 0 ? "" : "Konto";
  }
  
  public String getKonto() {
    return fzb.konto == null ? "" : fzb.konto;
  }
  public String getNASLOV(){
    return fzb.getNaslov();
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