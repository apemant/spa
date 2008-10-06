/****license*****************************************************************
**   file: repBrBilancaPer.java
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

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.Valid;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repBrBilancaPer implements raReportData {

  frmBrBilancaPer fbb;
  DataSet ds;

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();
  String[] colname = new String[] {""};

  static BigDecimal SL;

  public repBrBilancaPer() {
    fbb = frmBrBilancaPer.getFrmBrBilancaPer();
    ds = fbb.getBrutoBilQDS();
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

//  public double getSALDOALL() {
//    return getIDALL() - getIPALL();
//  }


  public double getUkupnoID(){
    return getID() + getPSID();
  }

  public double getUkupnoIP(){
    return getIP() + getPSIP();
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

  public double getSALDO(){
    double bd1, bd2;
    bd1 = (ds.getBigDecimal("ID").subtract(ds.getBigDecimal("IP"))).doubleValue(); //getID() - getIP();
    bd2= (ds.getBigDecimal("POCID").subtract(ds.getBigDecimal("POCIP"))).doubleValue(); //getPSID() - getPSIP();
    return bd1+bd2;
  }

//  public BigDecimal getSUMSAL(){
//    SL =  SL.add((ds.getBigDecimal("ID").add(ds.getBigDecimal("POCID"))).subtract(ds.getBigDecimal("IP").add(ds.getBigDecimal("POCIP"))));
//    System.out.println("SL: " + SL);
//    return SL;
//  }

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

//  public BigDecimal getKumulativSALDOALL(){
//    BigDecimal bd1, bd2;
//    bd1 = fbb.getKumulative()[2].subtract(fbb.getKumulative()[3]);
//    bd2 = fbb.getKumulative()[0].subtract(fbb.getKumulative()[1]);
//    return bd1.add(bd2);
//  }



  public String getZaPeriod() {
    return rdu.dataFormatter(fbb.datumP) + " do " + rdu.dataFormatter(fbb.datumZ);
  }

  public String getBROJKONTA() {
    return ds.getString("BROJKONTA");
  }

  public String getKLASE() {
    return ds.getString("BROJKONTA").substring(0,1);
  }

  public String getRKLASE() {
    return "RAZRED - "+ds.getString("BROJKONTA").substring(0,1);
  }

  public String getNAZIVKLASE() {
    return fbb.getNazKOnto(ds.getString("BROJKONTA").substring(0,1));
  }

  public String getKLASE2() {
    return ds.getString("BROJKONTA").substring(0,2);
  }

  public String getNAZIVKLASE2() {
    return fbb.getNazKOnto(ds.getString("BROJKONTA").substring(0,2));
  }

  public String getKLASE3() {
    return ds.getString("BROJKONTA").substring(0,3);
  }

  public String getNAZIVKLASE3() {
    return fbb.getNazKOnto(ds.getString("BROJKONTA").substring(0,3));
  }

  public String getNAZIVKONTA() {
    return ds.getString("NK");
  }

  public String getNAZORG() {
      String nazorg ="";
      ru.setDataSet(ds);
      colname[0] = "CORG";
//      sysoutTEST ST = new sysoutTEST(false);
//      ST.prn(dm.getOrgstruktura());
      nazorg = ru.getSomething(colname,dm.getOrgstruktura(),"NAZIV").getString();
//      System.out.println("nazorg --->" + nazorg + "<---nazorg od shifre: " + getCORG());
      if (fbb.getSKUPNI().equals("ZBIRNO")) return nazorg + " - ZBIRNO";
      return nazorg;
  }

  public String getCORG() {
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

  public String getGrouper(){
    if (fbb.getGroupMode().equals("1")) return "a";
    return ds.getString("CORG");
  }

//  public String getGrouper(){
//    /** @todo ovo isto :)) */
//    return "";
////    if (fbb.getGroupMode().equals("0")) return "a";
////    return ds.getString("CORG");
//  }

//  public String getVALUTA(){
//    String ozv = fbb.getVALUTA();
//    if (ozv.equals("")) return "";
//    else {
//      String nazv = "";
//      int jed = 0;
//      BigDecimal tecaj = new BigDecimal("0.000000");
//      DataRow dr = lookupData.getlookupData().raLookup(dm.getValute(), "OZNVAL", ozv);
//      if (dr != null) {
//        nazv = dr.getString("NAZVAL");
//        jed = dr.getInt("JEDVAL");
//        tecaj = hr.restart.zapod.Tecajevi.getTecaj(vl.getToday(), ozv);
//      }
//      return "U valuti " + nazv + " ("+ozv+") po teèaju - " + jed + " kuna = " + tecaj + " " + ozv;
//    }
//  }


}

//package hr.restart.gk;
//
//import hr.restart.util.reports.raReportData;
//
//public class repBrBilancaPer implements raReportData {
//
//  public repBrBilancaPer() {
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