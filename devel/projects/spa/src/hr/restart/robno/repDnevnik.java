/****license*****************************************************************
**   file: repDnevnik.java
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

import com.borland.dx.dataset.DataSet;


public class repDnevnik implements raReportData {

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  DataSet ds = upDnevnik.getInstance().getQds();
  raDateUtil rdu = raDateUtil.getraDateUtil();
//  String[] colname = new String[] {""};
  //repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();

  public repDnevnik() {
    ds.enableDataSetEvents(false);
    ds.open();
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public raReportData getRow(int idx) {
    ds.goToRow(idx);
    return this;
  }

//  private boolean test() {
//    return hr.restart.sisfun.frmParam.getParam("robno","ispLogo").equals("D");
//  }

  public void close() {
    ds.enableDataSetEvents(true);
    ds = null;
  }

//  public int getLogo() {
//    if (test()) return 1;
//    else return 0;
//  }

  public Timestamp getDatumOd() {
    return upDnevnik.getInstance().getUpit().getTimestamp("DATDOKfrom");
  }

  public Timestamp getDatumDo() {
    return upDnevnik.getInstance().getUpit().getTimestamp("DATDOKto");
  }

  public String getRANGE() {
    return rdu.dataFormatter(getDatumOd()) + " - " + rdu.dataFormatter(getDatumDo());
  }

  public String getCSKL() {
    return ds.getString("CSKL");
  }

  public String getNAZSKL() {
    /*ru.setDataSet(ds);
    colname[0] = "CSKL";
    return ru.getSomething(colname, dm.getSklad(), "NAZSKL").toString(); */
    return upDnevnik.getInstance().getNAZSKL();
    //return (ds.getString("CSKL") + "-" + foreign);
  }

  public String getDATDOK() {

    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
//    return ds.getTimestamp("DATDOK");
  }

  public String getBRDOK() {
    return String.valueOf(ds.getInt("BRDOK"));
  }

  public String getVRDOK() {
    return ds.getString("VRDOK");
  }

  public String getCART() {
    return String.valueOf(ds.getInt("CART"));
  }

  public String getSIFRA() {
    return Aut.getAut().getCARTdependable(ds);
  }

  public String getNAZART() {
    /*ru.setDataSet(ds);
    colname[0] = "CART";
    return ru.getSomething(colname, dm.getArtikli(), "NAZART").toString(); */
    return ds.getString("NAZART");
  }

  public BigDecimal getKOLUL() {
    return ds.getBigDecimal("KOLUL");
  }

  public BigDecimal getKOLIZ() {
    return ds.getBigDecimal("KOLIZ");
  }

  public BigDecimal getZC() {
    return ds.getBigDecimal("ZC");
  }

  public BigDecimal getIZAD() {
    return ds.getBigDecimal("IZAD");
  }

  public BigDecimal getIRAZ() {
    return ds.getBigDecimal("IRAZ");
  }

  public BigDecimal getSIZAD() {
    return upDnevnik.getInstance().getTotUlaz();
  }
  public BigDecimal getSIRAZ() {
    return upDnevnik.getInstance().getTotIzlaz();
  }
  public BigDecimal getSALDO() {
    return getSIZAD().subtract(getSIRAZ());
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
