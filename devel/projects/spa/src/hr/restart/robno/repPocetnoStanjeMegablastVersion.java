/****license*****************************************************************
**   file: repPocetnoStanjeMegablastVersion.java
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

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repPocetnoStanjeMegablastVersion implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  repMemo rpm = repMemo.getrepMemo();
  DataSet ds = reportsQuerysCollector.getRQCModule().getQueryDataSet();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);

  public repPocetnoStanjeMegablastVersion() {
  }

  public repPocetnoStanjeMegablastVersion(int idx) {
//    if (idx == 0){
//      sysoutTEST syst = new sysoutTEST(false);
//      syst.prn(ds);
//    }
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
        ru.setDataSet(ds);
      }
      int indx=0;
      public Object nextElement() {

        return new repPocetnoStanjeMegablastVersion(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public double getUIPRPOR(){
    return ds.getBigDecimal("UIPRPOR").doubleValue();
  }
  public String getBRDOKUL(){
    return ds.getString("BRDOKUL");
  }
  //// dobavljac
  public int getCPAR() {
    return ds.getInt("CPAR");
  }

  public String getNAZPAR() {
    ru.setDataSet(ds);
    colname[0] = "CPAR";
    return ru.getSomething(colname,dm.getPartneri(),"NAZPAR").getString();
  }
  public String getCSKL() {
    return ds.getString("CSKL");
  }
  public String getNAZSKL(){
    ru.setDataSet(ds);
    colname[0] = "CSKL";
    return ru.getSomething(colname,dm.getSklad(),"NAZSKL").getString();
  }

  public String getDATDOK() {
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }
  public short getRBR() {
    return ds.getShort("RBR");
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

  public BigDecimal getKOL() {
    return ds.getBigDecimal("KOL");
  }
  public BigDecimal getDC() {
    return ds.getBigDecimal("DC");
  }
  public double getIRAB() {
    return ds.getBigDecimal("IRAB").doubleValue();
  }
  public double getIZT() {
    return ds.getBigDecimal("IZT").doubleValue();
  }
  public double getNC() {
    return ds.getBigDecimal("NC").doubleValue();
  }
  public double getZC() {
    return ds.getBigDecimal("ZC").doubleValue();
  }
  public double getINAB() {
    return ds.getBigDecimal("INAB").doubleValue();
  }
  public double getIMAR() {
    return ds.getBigDecimal("IMAR").doubleValue();
  }
  public double getIPOR() {
    return ds.getBigDecimal("IPOR").doubleValue();
  }
  public double getIZAD() {
    return ds.getBigDecimal("IZAD").doubleValue();
  }
  public double getVC() {
    return ds.getBigDecimal("VC").doubleValue();
  }
  public double getIBP() {
    return ds.getBigDecimal("IBP").doubleValue();
  }
  public double getPP() {
  /// ovo ne valja
    return ds.getBigDecimal("IZT").doubleValue();
  }
  public double getMC() {
    return ds.getBigDecimal("MC").doubleValue();
  }
  public double getISP() {
    return ds.getBigDecimal("ISP").doubleValue();
  }
  public double getUKPOR() {
    return (ds.getBigDecimal("POR1").add(ds.getBigDecimal("POR2"))).add(ds.getBigDecimal("POR3")).doubleValue();
  }
  public int getBRDOK(){
    return ds.getInt("BRDOK");
  }
  public String getFormatBroj(){
    return ru.getFormatBroj();
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
    return rdu.dataFormatter(val.getToday());
  }
}