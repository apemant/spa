/****license*****************************************************************
**   file: repIspisRekap_3.java
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

import com.borland.dx.dataset.DataSet;
/**
 * <p>Title: </p> repOrgOS
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repIspisRekap_3 implements sg.com.elixir.reportwriter.datasource.IDataProvider
{

  _Main main;
  DataSet ds = ispRekap_NextGeneration.getQdsIspis(); //ispRekap.getQdsIspis();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();


  double osn;
  double isp;
  //static int sumIdx = 0;
  int rowCount=0;

  public repIspisRekap_3() {
    ru.setDataSet(ds);
  }

  public repIspisRekap_3(int idx) {
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
        rowCount= ds.getRowCount();
      }
      int indx=0;
      public Object nextElement() {
        return new repIspisRekap_3(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public String getInvBr()
  {
    return ds.getString("invbroj");
  }

  public int getRowNum()
  {
    return rowCount;
  }

  public String getCOrg()
  {
//    System.out.println("CORG REKAP: " + ds.getString("corg"));
    return ds.getString("corg");
  }

  public String getNazOrg()
  {
    ru.setDataSet(ds);
    colname[0] = "corg";
    String rez = ru.getSomething(colname,dm.getOrgstruktura() ,"naziv").toString().trim();
    return rez;
  }

  public String getCorgNazOrg()
  {
    ru.setDataSet(ds);
    colname[0] = "corg";
    String rez = ds.getString("corg") + " " + ru.getSomething(colname,dm.getOrgstruktura() ,"naziv").toString().trim();
    return rez;
  }

  public String getKonto()
  {
    return ds.getString("brojkonta");
  }

  public String getNazKonta()
  {
    ru.setDataSet(ds);
    colname[0] = "brojkonta";
    String rez = ru.getSomething(colname,dm.getKonta() ,"nazivkonta").toString().trim();
    return rez;
  }

  public String getKontoNazKonta()
  {
    ru.setDataSet(ds);
    colname[0] = "brojkonta";
    String rez = ds.getString("brojkonta") + " " + ru.getSomething(colname,dm.getKonta() ,"nazivkonta").toString().trim();
    return rez;
  }

  public String getSumLabela()
  {
    return "SVEUKUPNO";
  }

  public double getTRI()
  {
    return ds.getBigDecimal("TRI").doubleValue();
  }

  public double getCETIRI()
  {
    return ds.getBigDecimal("CETIRI").doubleValue();
  }

  public double getPET()
  {
    return ds.getDouble("PET");
  }

  public double getSEST()
  {
    return ds.getBigDecimal("SEST").doubleValue();
  }

  public double getSEDAM()
  {
    return ds.getBigDecimal("SEDAM").doubleValue();
  }

  public double getOSAM()
  {
    return ds.getBigDecimal("OSAM").doubleValue();
  }

  public double getDEVET()
  {
    return ds.getBigDecimal("DEVET").doubleValue();
  }

  public double getAMOR()
  {
    return ds.getBigDecimal("AMOR").doubleValue();
  }
  public double getSumeAmor()
  {
    return ispRekap.sume[10];
  }
  public double getDESET()
  {
    return ds.getDouble("DESET");
  }

  public double getJEDANAEST()
  {
    return ds.getDouble("JEDANAEST");
  }

  public double getDVANAEST()
  {
    return ds.getDouble("DVANAEST");
  }

  public double getSumeTri()
  {
    return ispRekap.sume[0];
  }

  public double getSumeCetiri()
  {
    return ispRekap.sume[1];
  }

  public double getSumePet()
  {
    return ispRekap.sume[2];
  }

  public double getSumeSest()
  {
    return ispRekap.sume[3];
  }

  public double getSumeSedam()
  {
    return ispRekap.sume[4];
  }

  public double getSumeOsam()
  {
    return ispRekap.sume[5];
  }

  public double getSumeDevet()
  {
    return ispRekap.sume[6];
  }

  public double getSumeDeset()
  {
    return ispRekap.sume[7];
  }

  public double getSumeJedanaest()
  {
    return ispRekap.sume[8];
  }

  public double getSumeDvanaest()
  {
    return ispRekap.sume[9];
  }

  public String getPocDat()
  {
    String datum = rdu.dataFormatter(ispRekap_NextGeneration.getPocDat());
    return "OSNOVNA SREDSTVA " + datum;
  }

  public String getZavDat()
  {
    String datum = rdu.dataFormatter(ispRekap_NextGeneration.getZavDat());
    return "OSNOVNA SREDSTVA " + datum;
  }

  public String getFake()
  {
    return"a";
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
