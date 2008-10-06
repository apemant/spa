/****license*****************************************************************
**   file: repListaInventureCekova.java
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

import com.borland.dx.dataset.DataSet;

public class repListaInventureCekova implements raReportData {
  frmInventuraCekova fsc = frmInventuraCekova.getInstance();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  DataSet ds = fsc.getQDS();
  _Main main;
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repMemo rpm = repMemo.getrepMemo();
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();

  public repListaInventureCekova() {
    ds.open();
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

  public String getBrRac() {
    return ds.getString("CSKL") + "-" + ds.getString("VRDOK") + "-" +
        ds.getString("GOD") + "-" + val.maskZeroInteger(Integer.valueOf(String.valueOf(ds.getInt("BRDOK"))), 6);
  }

  public String getCBanke() {
    if(lookupData.getlookupData().raLocate(dm.getBanke(), new String[]{"CBANKA"}, new String[]{ds.getString("CBANKA")}))
    return ds.getString("CBANKA");
    return "";
  }

  public String getCSKL() {
    return ds.getString("CSKL");
  }

  public String getDatdok() {
    try {
      return rdu.dataFormatter(ds.getTimestamp("DATPRIM"));
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public java.sql.Date getDATPRIM(){
    return java.sql.Date.valueOf(ds.getTimestamp("DATPRIM").toString().substring(0,10));
  }

  public String getDatnap() {
    return rdu.dataFormatter(ds.getTimestamp("DATNAP"));
  }

  public java.sql.Date getDATNAP(){
    return java.sql.Date.valueOf(ds.getTimestamp("DATNAP").toString().substring(0,10));
  }

  public String getDatumIsp() {
    return rdu.dataFormatter(val.getToday());
  }

  public int getIdx() {
    return ds.getRow() + 1;
  }

  public double getIznos() {
    return ds.getBigDecimal("IZNOS").doubleValue();
  }

  public String getNazBanke() {
    String cbanka = getCBanke();
    if(lookupData.getlookupData().raLocate(dm.getBanke(), new String[]{"CBANKA"}, new String[]{cbanka}))
        return dm.getBanke().getString("NAZIV");
    return "                  -//-         ";
  }

  public String getNazSklad() {
    String cskl = getCSKL();
    lookupData.getlookupData().raLocate(dm.getSklad(), new String[]{"CSKL"}, new String[]{cskl});
    return dm.getSklad().getString("NAZSKL");
  }

  public String getSBC() {
    return ds.getString("SBC");
  }

  public String getTBG() {
    return ds.getString("TRG");
  }

  public String getPeriod(){
    return  "ZA PERIOD "+rdu.dataFormatter(fsc.getOdPrimljeno())+" - "+rdu.dataFormatter(fsc.getDoNaplate());
  }

  public String getDatumIspisa(){
    return rdu.dataFormatter(val.getToday());
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
}