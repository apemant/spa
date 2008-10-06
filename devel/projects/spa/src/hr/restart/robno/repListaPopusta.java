/****license*****************************************************************
**   file: repListaPopusta.java
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
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;

public class repListaPopusta implements raReportData {

  upPopustList upol = upPopustList.getInstance();
  DataSet ds = upol.getRepSet();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo rm = repMemo.getrepMemo();

  public repListaPopusta() {
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ds=null;
  }

  public String getNASLOV(){
    return "LISTA POPUSTA\nod " + rdu.dataFormatter(upol.getDatumOd()) + " do " + rdu.dataFormatter(upol.getDatumDo());
  }

  public java.sql.Date getDatDok(){
    return java.sql.Date.valueOf(ds.getTimestamp("DATDOK").toString().substring(0,10));
  }

  public String getBRDOK(){
    return ds.getString("CSKL") + "-" + ds.getString("VRDOK") + "-" +
        ds.getString("GOD") + "-" + vl.maskZeroInteger(Integer.valueOf(String.valueOf(ds.getInt("BRDOK"))), 6);
  }

  public String getCSKL(){
    return ds.getString("CSKL");
  }

  public String getNAZSKL(){
    lookupData.getlookupData().raLocate(dm.getSklad(),"CSKL",ds.getString("CSKL"));
    return dm.getSklad().getString("NAZSKL");
  }

  public String getCART() {
    return Aut.getAut().getCARTdependable(ds);
  }

  public String getNAZART() {
    return ds.getString("NAZART");
  }

  public String getJM() {
    return ds.getString("JM");
  }

  public double getUPRAB(){
    return ds.getBigDecimal("UPRAB").doubleValue();
  }

  public double getUIRAB(){
    return ds.getBigDecimal("UIRAB").doubleValue();
  }

  public double getINETO(){
    return ds.getBigDecimal("INETO").doubleValue();
  }

  public double getIPRODBP(){
    return ds.getBigDecimal("IPRODBP").doubleValue();
  }

  public double getIPRODSP(){
    return ds.getBigDecimal("IPRODSP").doubleValue();
  }

  public String getFirstLine(){
    return rm.getFirstLine();
  }

  public String getSecondLine(){
    return rm.getSecondLine();
  }

  public String getThirdLine(){
    return rm.getThirdLine();
  }

  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }
}