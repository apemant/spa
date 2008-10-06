/****license*****************************************************************
**   file: repKoefObrt.java
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

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repKoefObrt implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  raKoefObrt rko = raKoefObrt.getInstance();
  DataSet ds = rko.getQDSKoefObrt();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
  raDateUtil rdu = new raDateUtil();
  String[] colname = new String[] {""};
  String[] colname1 = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();

  public repKoefObrt() {}

  public repKoefObrt(int idx) {
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
        return new repKoefObrt(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }
  public String getCSKL() {
    return ds.getString("CSKL");
  }
  public String getNAZSKL() {
    ru.setDataSet(ds);
    colname[0] = "CSKL";
    return ru.getSomething(colname,dm.getSklad(),"NAZSKL").getString();
  }
  public int getCART() {
    return ds.getInt("CART");
  }
  public String getNAZART() {
    ru.setDataSet(ds);
    colname[0] = "CART";
    return ru.getSomething(colname,dm.getArtikli(),"NAZART").getString();
  }
  public String getJM() {
    ru.setDataSet(ds);
    colname[0] = "CART";
    return ru.getSomething(colname,dm.getArtikli(),"JM").getString();
  }
  public BigDecimal getNABAVA() {
    return ds.getBigDecimal("NABAVA");
  }
  public BigDecimal getPRODAJA() {
    return ds.getBigDecimal("PRODAJA");
  }
  public BigDecimal getKOEFICIJENT() {
    return ds.getBigDecimal("KOEFICIJENT");
  }
  public BigDecimal getSUMNAB() {
    return ds.getBigDecimal("SUMNAB");
  }
  public BigDecimal getSUMPRO() {
    return ds.getBigDecimal("SUMPRO");
  }
  public BigDecimal getKOEFSUM() {
    return ds.getBigDecimal("KOEFSUM");
  }
  public BigDecimal getDANZAL() {
    return ds.getBigDecimal("DANZAL");
  }
  public BigDecimal getDANZALSUM() {
    return ds.getBigDecimal("DANZALSUM");
  }
  public String getZaPeriod() {
    return "Za period od " + rdu.dataFormatter(rko.getPocDatum()) + " do " + rdu.dataFormatter(rko.getZavDatum());
  }
  public String getCGRART() {
    if (rko.isGrupa()) return rko.getCGART();
    return "";
  }
  public String getNAZGRART() {
    if (rko.isGrupa()) return rko.getNAZGRART();
    return "";
  }
  public String getGROUPLABEL() {
    if (rko.isGrupa()) return "Grupa artikala";
    return "";
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