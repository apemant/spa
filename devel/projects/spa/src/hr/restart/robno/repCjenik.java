/****license*****************************************************************
**   file: repCjenik.java
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
import hr.restart.util.sysoutTEST;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repCjenik implements raReportData {

  DataSet ds = frmCjenik.getInstance().getrepQDS();
  repMemo rpm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  boolean tst;

  public repCjenik() {
    tst = hr.restart.sisfun.frmParam.getParam("robno","ispLogo","N","Ispis loga na dokumentima (D/N)").equals("D");
    sysoutTEST st = new sysoutTEST(false);
    st.prn(ds);
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }
  public int getRowCount() {
    return ds.rowCount();
  }
  public void close() {
  }
  public int getLogo() {
    if (tst) return 1;
    else return 0;
  }

  public String getCSKL() {
    return ds.getString(frmCjenik.getInstance().getCCS());
  }

  public String getNAZSKL() {
    try {
    if (!ds.getString("NAZSKL").equalsIgnoreCase(frmCjenik.getInstance().getDUMMY())) return ds.getString("NAZSKL");
    else {
      hr.restart.util.lookupData.getlookupData().raLocate(hr.restart.baza.dM.getDataModule().getOrgstruktura(),"CORG",ds.getString("CORG"));
      return hr.restart.baza.dM.getDataModule().getOrgstruktura().getString("NAZIV");
    }
    } catch (Exception eexc){
      hr.restart.util.lookupData.getlookupData().raLocate(hr.restart.baza.dM.getDataModule().getOrgstruktura(),"CORG",ds.getString("CORG"));
      return hr.restart.baza.dM.getDataModule().getOrgstruktura().getString("NAZIV");
    }
  }

  public String getCPAR() {
    return "" + ds.getInt("CPAR");
  }

  public String getNAZPAR() {
    return ds.getString("NAZPAR");
  }

  public String getSIFRA() {
    return Aut.getAut().getCARTdependable(ds);
  }

  public String getCART() {
    return "" + ds.getInt("CART");
  }

  public String getCART1() {
    return ds.getString("CART1");
  }

  public String getBC() {
    return ds.getString("BC");
  }

  public String getNAZART() {
    return ds.getString("NAZART");
  }

  public String getJM() {
    return ds.getString("JM");
  }

  public String getSECTION() {
    return ds.getString("CSKL") + ds.getInt("CPAR");
  }

  public BigDecimal getVC() {
    return ds.getBigDecimal("VC");
  }

  public BigDecimal getMC() {
    return ds.getBigDecimal("MC");
  }

  public String getVAL() {
    return ds.getString("OZNVAL").toLowerCase();
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

