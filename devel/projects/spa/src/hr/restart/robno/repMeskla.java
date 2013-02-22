/****license*****************************************************************
**   file: repMeskla.java
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

import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.borland.dx.dataset.DataSet;

public class repMeskla implements raReportData {

  DataSet ds = reportsQuerysCollector.getRQCModule().getQueryDataSet();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = new raDateUtil();
  String[] colname = new String[] {""};
  String[] colname1 = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();

  public repMeskla() {
    ds.open();
    ru.setDataSet(ds);
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(ds);
  }

  public int getRowCount() {
    return ds.getRowCount();
  }

  public raReportData getRow(int idx) {
    ds.goToRow(idx);
    return this;
  }

  public void close() {
    ru.setDataSet(null);
    ds = null;
  }

  public String getCSKLIZ() {
    return ds.getString("CSKLIZ");
  }

  public String getNAZCSKLIZ() {
    ru.setDataSet(ds);
    colname[0] = "CSKL";
    colname1[0] = "CSKLIZ";
    return ru.getSomething2(colname,colname1,ds,dm.getSklad(),"NAZSKL").getString();
  }

  public String getCSKLUL() {
    return ds.getString("CSKLUL");
  }

  public String getNAZCSKLUL() {
    ru.setDataSet(ds);
    colname[0] = "CSKL";
    colname1[0] = "CSKLUL";
    return ru.getSomething2(colname,colname1,ds,dm.getSklad(),"NAZSKL").getString();
  }

  public String getFormatBroj(){
    ru.setDataSet(ds);
    return ru.getFormatBrojME();
  }

  public short getRBR() {
    return ds.getShort("RBR");
  }

  public String getVRDOK() {
    return ds.getString("VRDOK");
  }

  public String getGOD() {
    return ds.getString("GOD");
  }

  public int getBRDOK() {
    return ds.getInt("BRDOK");
  }

  public Timestamp getDATDOK() {
    return ds.getTimestamp("DATDOK");
  }

  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }

  public String SgetDATDOK() {
    return rdu.dataFormatter(getDATDOK());
  }

  public String getCART() {
    return Aut.getAut().getCARTdependable(ds);
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

  public BigDecimal getKOL() {
    return ds.getBigDecimal("KOL");
  }

  public BigDecimal getZC(){
    return ds.getBigDecimal("ZC");
  }

  public BigDecimal getNC(){
    return ds.getBigDecimal("NC");
  }

  public double getNABVRI(){
    if (ds.getString("VRDOK").equalsIgnoreCase("MEI"))
      return ds.getBigDecimal("INABIZ").doubleValue();
    if (ds.getString("VRDOK").equalsIgnoreCase("MEU"))
      return ds.getBigDecimal("INABUL").doubleValue();
     return (ds.getBigDecimal("KOL").multiply(ds.getBigDecimal("NC"))).doubleValue();
  }

  public BigDecimal getVC(){
    return ds.getBigDecimal("VC");
  }

  public double getVPVRI(){
     return (ds.getBigDecimal("KOL").multiply(ds.getBigDecimal("VC"))).doubleValue();
  }

  public BigDecimal getMC(){
    return ds.getBigDecimal("MC");
  }

  public double getMPVRI(){
     return (ds.getBigDecimal("KOL").multiply(ds.getBigDecimal("MC"))).doubleValue();
  }

  public double getZADRAZIZ(){
    return ds.getBigDecimal("ZADRAZIZ").doubleValue();
  }

  public BigDecimal getZCUL(){
    return ds.getBigDecimal("ZCUL");
  }
  
  public BigDecimal getFC(){
    return ds.getBigDecimal("FC");
  }
  
  public BigDecimal getPPOP(){
    return ds.getBigDecimal("PPOP");
  }
  
  public BigDecimal getFCP(){
    return getFC().multiply(Aus.one0.subtract(
        getPPOP().movePointLeft(2))).setScale(2, BigDecimal.ROUND_HALF_UP);
  }

  public double getZADRAZUL(){
    return ds.getBigDecimal("ZADRAZUL").doubleValue();
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

  public String getLogoMjesto(){
    return rpm.getLogoMjesto();
  }

  public String getOPIS() {
    return ds.getString("OPIS");
  }
}

