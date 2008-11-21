/****license*****************************************************************
**   file: repIspisRekap.java
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

public class repIspisRekap implements raReportData {

  _Main main;
//  ispRekap_NextGeneration ispRek = ispRekap_NextGeneration.get
  DataSet ds;// = ispRekap_NextGeneration.getQdsIspis(); //ispRekap.getQdsIspis();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();

  static int rowCount=0;

  public repIspisRekap() {
    ds = ispRekap_NextGeneration.getQdsIspis();
    rowCount=ds.rowCount();
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

  public String getInvBr() {
    return ds.getString("invbroj");
  }

  public String getNazSred() {
    ru.setDataSet(ds);
    colname[0] = "invbroj";
    String rez = ru.getSomething(colname,dm.getOS_Sredstvo() ,"nazsredstva").toString().trim();
    return rez;
  }


  public int getRowNum() {
    return rowCount;
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

  public String getKonto() {
    return ds.getString("brojkonta");
  }

  public String getBrNazKonta() {
    ru.setDataSet(ds);
    colname[0] = "brojkonta";
    String rez = ru.getSomething(colname,dm.getKonta() ,"nazivkonta").toString().trim();
    return rez;
  }

  public String getCorgNazOrg() {
    ru.setDataSet(ds);
    colname[0] = "corg";
    String rez = ds.getString("corg") + " " + ru.getSomething(colname,dm.getOrgstruktura() ,"naziv").toString().trim();
    return rez;
  }

  public String getNazKonta() {
    ru.setDataSet(ds);
    colname[0] = "brojkonta";
    String rez = ru.getSomething(colname,dm.getKonta() ,"nazivkonta").toString().trim();
    return rez;
  }

  public String getNazSredstva() {
    return ds.getString("nazsredstva");
  }

  public String getDatumOS() {
    String datum = rdu.dataFormatter(ds.getTimestamp("datum"));
    if (datum.equals("01.01.1970"))
      return "";
    else
      return datum;
  }

  double osn;
  double isp;

  public double getOsnVr() {
    if(ispOS.getSelectedRB2()==0) {
      osn = ds.getBigDecimal("OSNPOCETAK").doubleValue();
    }
    else if(ispOS.getSelectedRB2()==1 || ispOS.getSelectedRB2()==2) {
      osn = (ds.getBigDecimal("OSNDUGUJE").add(ds.getBigDecimal("OSNPOTRAZUJE").negate())).doubleValue();
    }
    return osn;
  }

  public String getSumLabela() {
    return "S V E U K U P N O";
  }

  public double getTRI() {
    return ds.getBigDecimal("TRI").doubleValue();
  }

  public double getCETIRI() {
    return ds.getBigDecimal("CETIRI").doubleValue();
  }

  public double getPET() {
    return ds.getBigDecimal("PET").doubleValue();
  }

  public double getSEST() {
    return ds.getBigDecimal("SEST").doubleValue();
  }

  public double getSEDAM() {
    return ds.getBigDecimal("SEDAM").doubleValue();
  }

  public double getOSAM() {
    return ds.getBigDecimal("OSAM").doubleValue();
  }

  public double getDEVET() {
    return ds.getBigDecimal("DEVET").doubleValue();
  }

  public double getAMOR() {
    return ds.getBigDecimal("AMOR").doubleValue();
  }

  public double getSumeAmor() {
    return ispRekap_NextGeneration.getSume()[10];
  }

  public double getDESET() {
    return ds.getBigDecimal("DESET").doubleValue();
  }

  public double getJEDANAEST() {
    return ds.getBigDecimal("JEDANAEST").doubleValue();
  }

  public double getDVANAEST() {
    return ds.getBigDecimal("DVANAEST").doubleValue();
  }

  public double getSumeTri() {
    return ispRekap_NextGeneration.getSume()[0];
  }

  public double getSumeCetiri() {
    return ispRekap_NextGeneration.getSume()[1];
  }

  public double getSumePet() {
    return ispRekap_NextGeneration.getSume()[2];
  }

  public double getSumeSest() {
    return ispRekap_NextGeneration.getSume()[3];
  }

  public double getSumeSedam() {
    return ispRekap_NextGeneration.getSume()[4];
  }

  public double getSumeOsam() {
    return ispRekap_NextGeneration.getSume()[5];
  }

  public double getSumeDevet() {
    return ispRekap_NextGeneration.getSume()[6];
  }

  public double getSumeDeset() {
    return ispRekap_NextGeneration.getSume()[7];
  }

  public double getSumeJedanaest() {
    return ispRekap_NextGeneration.getSume()[8];
  }

  public double getSumeDvanaest() {
    return ispRekap_NextGeneration.getSume()[9];
  }

  public String getPocDat() {
    String datum = rdu.dataFormatter(ispRekap_NextGeneration.getPocDat());
    return "OSNOVNA SREDSTVA " + datum;
  }

  public String getZavDat() {
    String datum = rdu.dataFormatter(ispRekap_NextGeneration.getZavDat());
    return "OSNOVNA SREDSTVA " + datum;
  }

  public String getFake() {
    return "000";
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