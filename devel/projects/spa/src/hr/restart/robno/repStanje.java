/****license*****************************************************************
**   file: repStanje.java
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

public class repStanje implements raReportData { // sg.com.elixir.reportwriter.datasource.IDataProvider
                                                  // {

  _Main main;

  repMemo rpm = repMemo.getrepMemo();
  UpStanjeRobno ups = UpStanjeRobno.getInstance();
  DataSet ds = ups.getQds();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[]{""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();

  public repStanje() {
    ru.setDataSet(ds);
  }

  /*
   * public repStanje(int idx) { // if (idx == 0) { // sysoutTEST syst = new
   * sysoutTEST(false); // syst.showInFrame(ds,"aaaaaaaaaaaaaaaaaaaaaaaaaaa"); // }
   * ds.goToRow(idx); } public java.util.Enumeration getData() { return new
   * java.util.Enumeration() { { ds.open(); ru.setDataSet(ds); } int indx=0;
   * public Object nextElement() { return new repStanje(indx++); } public
   * boolean hasMoreElements() { return (indx < ds.getRowCount()); } }; } public
   * void close() {}
   */

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

  public String getCSKL() {
    try {
      return ds.getString("CSKL");
    } catch (Exception e) {
      return "ERROR!!";
      // return ups.getCskl();
    }
  }

  hr.restart.util.lookupData lD = hr.restart.util.lookupData.getlookupData();

  public String getNAZCSKL() {

    if (lD.raLocate(dm.getSklad(), "CSKL", ds.getString("CSKL")))
      return dm.getSklad().getString("NAZSKL");
    return "";
  }

  public String getCART() {
    return Aut.getAut().getCARTdependable(ds);
  }

  public String getNAZART() {
    return ds.getString("NAZART");
  }

  public String getCAPTION() {
    String datum = "";
    if (!ups.saStanja()){
      datum = rdu.dataFormatter(ups.getDatum());
    } else {
      datum = rdu.dataFormatter(val.getToday());
    }
    if (!ups.getCskl().equals(""))
     return ("Stanje za : "+ups.getCskl()+" "+getNAZCSKL() + " na " + datum);
    return ("Stanje na svim skladištima na "+ datum);
  }

  public String getCaptionDate() {
     if (!ups.saStanja())
     return "iz prometa zakljuèno sa "+rdu.dataFormatter(ups.getDatum())+".";
     return "";//"sa kumulativa za " + ups.getZaGodinu() + ". godinu";
  }

  public String getJM() {
    colname[0] = "CART";
    return ru.getSomething(colname, dm.getArtikli(), "JM").toString();
  }

  public double getKOL() {
    return ds.getBigDecimal("KOL").doubleValue();
  }

  public String getJMPAK() {
    colname[0] = "CART";
    return ru.getSomething(colname, dm.getArtikli(), "JMPAK").toString();
  }

  public double getKOLPAK() {
    try {
      colname[0] = "CART";
      BigDecimal brjed = ru.getSomething(colname, dm.getArtikli(), "BRJED").getAsBigDecimal();

      return (ds.getBigDecimal("KOL").divide(brjed, 3, BigDecimal.ROUND_HALF_UP)).doubleValue();
    } catch (Exception e) {
      return 0.00;
    }
  }

  public BigDecimal getZC() {
    return ds.getBigDecimal("ZC");
  }

  public BigDecimal getVC() {
    return ds.getBigDecimal("VC");
  }

  public double getVri() {
    return ds.getBigDecimal("VRI").doubleValue();
  }

  public int getDummy() {
    return 1;
  }

  public String getSORTING() {
    // if (ups.isSortPoNazivu()){
    // return ds.getString("NAZART");
    // }
    // return getCART();
    return "";
  }

  public String getDatumIsp() {
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