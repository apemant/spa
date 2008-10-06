/****license*****************************************************************
**   file: repTotKar.java
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

import com.borland.dx.dataset.DataSet;

public class repTotKar implements raReportData {// implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  repMemo rpm = repMemo.getrepMemo();
  upTotKar upk = upTotKar.getupTotKar();
  DataSet ds/* = upk.getQds()*/;
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[]{""};
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();

  public repTotKar() {
    ds = upk.getQds();
  }
  
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

  public String getNAZART() {
    return ds.getString("NAZART");
  }

  public String getJM() {
    return ds.getString("JM");
  }

  public BigDecimal getKOLDON() {
    return ds.getBigDecimal("KOLDON");
  }

  public BigDecimal getKOLUL() {
    return ds.getBigDecimal("KOLUL");
  }

  public BigDecimal getKOLIZ() {
    return ds.getBigDecimal("KOLIZ");
  }

  public double getKOLDZAD() {
    return ds.getBigDecimal("KOLDZAD").doubleValue();
  }

  public double getKOLZAD() {
    return ds.getBigDecimal("KOLZAD").doubleValue();
  }

  public double getKOLRAZ() {
    return ds.getBigDecimal("KOLRAZ").doubleValue();
  }

  public double getSKOL() {
    return ds.getBigDecimal("SKOL").doubleValue();
  }

  public double getSIZN() {
    return ds.getBigDecimal("SIZN").doubleValue();
  }

  public double getZC() {
    return ds.getBigDecimal("ZC").doubleValue();
  }

  public String getCART() {
    return Aut.getAut().getCARTdependable(ds);
  }

  public String getCSKL() {
    return upk.getCskl(); // ds.getString("KOLSK");
  }

  public String getNSKL() {
    return upk.getNazSkl(); // ds.getString("KOLNSK");
  }

  public int getDummy() {
    return 1;
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

  public String getDatumIsp() {
    return rdu.dataFormatter(val.getToday());
  }

  public String getCaption() {
    return (rdu.dataFormatter(upk.getPocDatum()/* ds.getTimestamp("KOLPD") */) + " do " + rdu.dataFormatter(upk.getZavDatum()/* ds.getTimestamp("KOLZD") */));
  }
}