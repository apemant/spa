/****license*****************************************************************
**   file: repIRA.java
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
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repIRA implements raReportData {
  raIspisUraIra rui = raIspisUraIra.getInstance();
  DataSet ds = rui.getDataSet();
  repMemo re = repMemo.getrepMemo();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  public repIRA() {
  }

  public raReportData getRow(int idx) {
    ds.goToRow(idx);
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ds = null;
  }

  public String getKNJIGA() {
    return rui.getCKNJIGE();
  }

  public String getNAZIVKNJIGE() {
    return rui.getNAZKNJIGE();
  }

  public int getRbr() {
    return ds.getInt("RBS");
  }

  public int getFake() {
    return ds.getInt("BROJ");
  }

  public String getBROJDOK() {
    return ds.getInt("RBS") == 0 ? "" : ds.getString("BROJDOK");
  }
  public String getEXTBRDOK() {
    return ds.getInt("RBS") == 0 ? "" : ds.getString("EXTBRDOK");
  }
  public String getDATUM() {
    return ds.getInt("RBS") == 0 ? "" : rui.rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }

  public String getNAZIV() {
    return ds.getString("OPISPAR");
  }

  public String getMB() {
    return ds.getInt("RBS") == 0 ? "" : ds.getString("MB");
  }

  public BigDecimal getKOL6() {
    return raIspisUraIra.getKolona(ds,"6");
  }

  public BigDecimal getKOL7() {
    return raIspisUraIra.getKolona(ds,"7");
  }

  public BigDecimal getKOL8() {
    return raIspisUraIra.getKolona(ds,"8");
  }

  public BigDecimal getKOL9() {
    return raIspisUraIra.getKolona(ds,"9");
  }

  public BigDecimal getKOL10() {
    return raIspisUraIra.getKolona(ds,"10");
  }

  public BigDecimal getKOL11() {
    return raIspisUraIra.getKolona(ds,"11");
  }

  public BigDecimal getKOL12() {
    return raIspisUraIra.getKolona(ds,"12");
  }

  public BigDecimal getKOL13() {
    return raIspisUraIra.getKolona(ds,"13");
  }

  public BigDecimal getKOL14() {
    return raIspisUraIra.getKolona(ds,"14");
  }
  
  public BigDecimal getKOL15() {
    return raIspisUraIra.getKolona(ds,"15");
  }
  
  public BigDecimal getKOL16() {
    return raIspisUraIra.getKolona(ds,"16");
  }
  
  public BigDecimal getKOL17() {
    return raIspisUraIra.getKolona(ds,"17");
  }
  
  public BigDecimal getKOL18() {
    return raIspisUraIra.getKolona(ds,"18");
  }
  
  public BigDecimal getKOL19() {
    return raIspisUraIra.getKolona(ds,"19");
  }
  
  public BigDecimal getKOL20() {
    return raIspisUraIra.getKolona(ds,"20");
  }
  
  public BigDecimal getKOL21() {
    return raIspisUraIra.getKolona(ds,"21");
  }
  
  public BigDecimal getKOL22() {
    return raIspisUraIra.getKolona(ds,"22");
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