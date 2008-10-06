/****license*****************************************************************
**   file: repBrBilRekapitulacijaPer.java
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
package hr.restart.gk;

import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repBrBilRekapitulacijaPer implements raReportData {

  frmBrBilancaPer fbb;
  DataSet ds;

  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  hr.restart.robno.repUtil ru = hr.restart.robno.repUtil.getrepUtil();
  hr.restart.robno.repMemo re = hr.restart.robno.repMemo.getrepMemo();
  String[] colname = new String[] {""};

  public repBrBilRekapitulacijaPer() {
    fbb = frmBrBilancaPer.getFrmBrBilancaPer();
    ds = fbb.getBrutoBilDS2();
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
    fbb = null;
  }
  public double getID() {
    return ds.getBigDecimal("ID").doubleValue();
  }
  public double getIP() {
    return ds.getBigDecimal("IP").doubleValue();
  }
  public double getPSID(){
    return ds.getBigDecimal("POCID").doubleValue();
  }
  public double getPSIP(){
    return ds.getBigDecimal("POCIP").doubleValue();
  }
  public double getIDALL() {
    return ds.getBigDecimal("ID").doubleValue() + ds.getBigDecimal("POCID").doubleValue();
  }
  public double getIPALL() {
    return ds.getBigDecimal("IP").doubleValue() + ds.getBigDecimal("POCIP").doubleValue();
  }
  public double getSALDO(){
    double bd1, bd2;
    bd1= getID() - getIP();
    bd2= getPSID() - getPSIP();
    return bd1+bd2;
  }

  public double getPSSALDO(){
    double bd;
    bd= (ds.getBigDecimal("POCID").subtract(ds.getBigDecimal("POCIP"))).doubleValue(); //getPSID() - getPSIP();
    return bd;
//    return getPSID() - getPSIP();
  }

  public double getPRSALDO() {
    double bd;
    bd = (ds.getBigDecimal("ID").subtract(ds.getBigDecimal("IP"))).doubleValue(); //getID() - getIP();
    return bd;
//    return getID() - getIP();
  }

  public BigDecimal getKumulativID() {
    return fbb.getKumulative()[2];
  }

  public BigDecimal getKumulativIP() {
    return fbb.getKumulative()[3];
  }

  public BigDecimal getKumulativPSID(){
    return fbb.getKumulative()[0];
  }

  public BigDecimal getKumulativPSIP(){
    return fbb.getKumulative()[1];
  }

  public BigDecimal getKumulativSALDO(){
    BigDecimal bd1, bd2;
    bd1 = fbb.getKumulative()[2].subtract(fbb.getKumulative()[3]);
    bd2 = fbb.getKumulative()[0].subtract(fbb.getKumulative()[1]);
    return bd1.add(bd2);
  }

  public BigDecimal getKumulativIDALL() {
    return fbb.getKumulative()[2].add(fbb.getKumulative()[0]);
  }

  public BigDecimal getKumulativIPALL() {
    return fbb.getKumulative()[3].add(fbb.getKumulative()[1]);
  }

  public String getZaPeriod() {
    return rdu.dataFormatter(fbb.datumP) + " do " + rdu.dataFormatter(fbb.datumZ);
  }


  public String getBROJKONTA() {
    return ds.getString("BROJKONTA");
  }

  public String getNAZIVKONTA() {
    return ds.getString("NK");
  }

  public String getNAZORG() {
      String nazorg ="";
      ru.setDataSet(ds);
      colname[0] = "CORG";
      nazorg = ru.getSomething(colname,dm.getOrgstruktura(),"NAZIV").getString();
      if (fbb.getSKUPNI().equals("ZBIRNO")) return nazorg + " - ZBIRNO";
      return nazorg;
  }

  public String getCORG() {
//    if (fbb.getSKUPNI().equals("ZBIRNO"))
//      return fbb.getCORG();
//    else
      return ds.getString("CORG");
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
  public String getLogoMjesto(){
    return re.getLogoMjesto();
  }
  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }
}


//package hr.restart.gk;
//
//import hr.restart.util.reports.raReportData;
//
//public class repBrBilRekapitulacijaPer implements raReportData {
//
//  public repBrBilRekapitulacijaPer() {
//  }
//  public raReportData getRow(int i) {
//    /**@todo Implement this hr.restart.util.reports.raReportData method*/
//    throw new java.lang.UnsupportedOperationException("Method getRow() not yet implemented.");
//  }
//  public int getRowCount() {
//    /**@todo Implement this hr.restart.util.reports.raReportData method*/
//    throw new java.lang.UnsupportedOperationException("Method getRowCount() not yet implemented.");
//  }
//  public void close() {
//    /**@todo Implement this hr.restart.util.reports.raReportData method*/
//    throw new java.lang.UnsupportedOperationException("Method close() not yet implemented.");
//  }
//}