/****license*****************************************************************
**   file: repIspisAmortizacija.java
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
package hr.restart.os;


import hr.restart.robno._Main;
import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;

public class repIspisAmortizacija implements raReportData {
  _Main main;
  ispAmor_NextGeneration iamor = ispAmor_NextGeneration.getInstance();
  DataSet ds;
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();

  String[] colname = new String[] {""};

//  int rowCount=0;
  static String tempOL="";

  public repIspisAmortizacija() {
    ds = iamor.getIspisDataSet();
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
    ds = null;
  }

  public String getNazSredstva() {
    return ds.getString("nazsredstva");
  }

  public double getOsnVr() {
    return ds.getBigDecimal("OSNOVICA").doubleValue();
  }

  public double getIspVr() {
    return ds.getBigDecimal("ISPRAVAK").doubleValue();
  }

  public double getAmVr() {
    return ds.getBigDecimal("AMORTIZACIJA").doubleValue();
  }

  public double getPamVr() {
    return ds.getBigDecimal("PAMORTIZACIJA").doubleValue();
  }

  public double getSadVr() {
    return ds.getBigDecimal("SADVRIJED").doubleValue();
  }

  public String getDatumProm() {
    return rdu.dataFormatter(ds.getTimestamp("DATPROMJENE"));
  }

  public String getCOrg() {
    return ds.getString("corg");
  }


  public String getNazOrg() {
    ru.setDataSet(ds);
    colname[0] = "corg";
    String rez = ru.getSomething(colname,dm.getOrgstruktura() ,"naziv").toString().trim();
    return rez;
  }

  public String getTipAmor() {
    String sCase = ds.getString("TIPAMOR");

    if(sCase.equals("P")) return "\nGODIŠNJI PREDRAÈUN AMORTIZACIJE";
    else if(sCase.equals("M")) return "\nMJESEÈNI OBRAÈUN AMORTIZACIJE";
    else if(sCase.equals("S")) return "\nSIMULACIJA GODIŠNJEG OBRAÈUNA AMORTIZACIJE";
    else return "\nGODIŠNJI OBRAÈUN AMORTIZACIJE";
  }

  public String getDatumIsp() {
    return rdu.dataFormatter(val.getToday());
  }

  public String getCaption2() {
    if(iamor.isPripOrgJed())
      return "( po org. jedinici s pripadajuæim org. jedinicama )";
    else
      return "( po org. jedinici )";
  }

  public String getcaption3() {
    String datum1 = rdu.dataFormatter(ds.getTimestamp("DATUMOD"));
    String datum2 = rdu.dataFormatter(ds.getTimestamp("DATUMDO"));
    return  datum1+" do "+datum2;
  }

  public String getOblikListe() {
    return ds.getString(iamor.getOblikIspisa());
  }

  public String getNazOblikListe() {
    colname[0] = iamor.getOblikIspisa();
    String dohvatkolona = "";
    com.borland.dx.sql.dataset.QueryDataSet targetset;

    if (colname[0].equals("BROJKONTA")) {
      targetset = hr.restart.zapod.raKonta.getAnalitickaKonta();
      dohvatkolona =  "NAZIVKONTA";
    }
    else if (colname[0].equals("CGRUPE")) {
      targetset = dm.getOS_Amgrupe();
      dohvatkolona =  "NAZGRUPE";
    }
    else if (colname[0].equals("CLOKACIJE")) {
      targetset = dm.getOS_Lokacije();
      dohvatkolona =  "NAZLOKACIJE";
    }
    else if (colname[0].equals("CSKUPINE")) {
      targetset = dm.getOS_Revskupine();
      dohvatkolona =  "NAZSKUPINE";
    }
    else if (colname[0].equals("CARTIKLA")) {
      targetset = dm.getOS_Artikli();
      dohvatkolona =  "NAZARTIKLA";
    }
    else return "";

    return ru.getSomething(colname, targetset, dohvatkolona).toString().trim();
  }

//  public String getZakStopa()
//  {
//    return ds.getBigDecimal("ZAKSTOPA").setScale(2).toString();
//  }
//
//  public String getOdlStopa()
//  {
//    return ds.getBigDecimal("ODLSTOPA").setScale(2).toString();
//  }

  public double getDodatak()
  {
    return (ds.getBigDecimal("ZAKSTOPA").add(ds.getBigDecimal("ODLSTOPA"))).setScale(2).doubleValue();

//    if(oblikListe==1)
//      return getZakStopa().replace('.',',') + " + " + getOdlStopa().replace('.',',');
//    return "";
  }

  public String getLabelaListe() {
    String ll = iamor.getOblikIspisa();
    if (ll.equals("BROJKONTA")) return "Br. konta";
    else if (ll.equals("CGRUPE")) return "Amor. grupa";
    else if (ll.equals("CLOKACIJE")) return "Lokacija";
    else if (ll.equals("CSKUPINE")) return "Rev. skupina";
    else if (ll.equals("CARTIKLA")) return "Artikl";
    return "";
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

  public String getInvBr()
  {
    return ds.getString("invbroj");
  }

  public String getNazSred()
  {
    return ds.getString("nazsredstva");
  }

  public double getNabVr()
  {
    return ds.getBigDecimal("NABVRIJED").doubleValue();
  }

  public double getIspravak()
  {
    return ds.getBigDecimal("ISPRAVAK").doubleValue();
  }

  public String getDatPr()
  {
    return rdu.dataFormatter(ds.getTimestamp("DATPR"));
  }

  public String getDatLik()
  {
    return rdu.dataFormatter(ds.getTimestamp("DATLIK"));
  }
  public double getAmortizacija()
  {
    return ds.getBigDecimal("AMORTIZACIJA").doubleValue();
  }

  public double getStIspravak()
  {
    return rdOSUtil.getBDouble(ds, "STVARNIISP");
  }
  
  public double getJedanMinusCetri(){
    return ds.getBigDecimal("NABVRIJED").doubleValue() - getStIspravak();
  }
  
  public double getSadVrUl()
  {
    return (getNabVr()-getStIspravak());
  }
  
  public String getBrMjeseci()
  {
    return ds.getString("MJESEC");
  }
  
  public String getDatRange()
  {
    return rdu.dataFormatter(iamor.getDatumOd()) + " do " + rdu.dataFormatter(iamor.getDatumDo());
  }


  public String getFake()
  {
    return"a";
  }

//  public String getFirstLine(){
//    return rpm.getFirstLine();
//  }
//  public String getSecondLine(){
//    return rpm.getSecondLine();
//  }
//  public String getThirdLine(){
//    return rpm.getThirdLine();
//  }
//  public String getDatumIsp(){
//    return rdu.dataFormatter(val.getToday());
//  }
}