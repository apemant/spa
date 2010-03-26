/****license*****************************************************************
**   file: repIspisInv.java
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
/**
 * <p>Title: </p> repOrgOS
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repIspisInv implements raReportData, sg.com.elixir.reportwriter.datasource.IDataProvider
{

  _Main main;
  DataSet ds = ispInv.getQdsIspis();
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

  public repIspisInv() {
    ru.setDataSet(ds);
  }

  public repIspisInv(int idx) {
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
        return new repIspisInv(indx++);
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
    return ds.getString("corg");
  }

  public String getNazOrg()
  {
    ru.setDataSet(ds);
    colname[0] = "corg";
    String rez = ru.getSomething(colname,dm.getOrgstruktura() ,"naziv").toString().trim();
    return rez;
  }

  public String getCorgNazorg(){
    ru.setDataSet(ds);
    colname[0] = "corg";
    String rez = ds.getString("corg") + " " + ru.getSomething(colname,dm.getOrgstruktura() ,"naziv").toString().trim();
    return rez;
  }

  public String getLokacija()
  {
    return ds.getString("clokacije");
  }

  public String getNazLokacije()
  {
    ru.setDataSet(ds);
    colname[0] = "clokacije";
    String rez = ru.getSomething(colname,dm.getOS_Lokacije() ,"nazlokacije").toString().trim();
    return rez;
  }

  public String getLokacijaNazLokacije()
  {
    ru.setDataSet(ds);
    colname[0] = "clokacije";
    String rez = ds.getString("clokacije") + " " + ru.getSomething(colname,dm.getOS_Lokacije() ,"nazlokacije").toString().trim();
    return rez;
  }

  public String getNazSred()
  {
    return ds.getString("NAZSREDSTVA");
  }

  public String getFake()
  {
    return"000";
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

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.getRowCount();
  }

}
