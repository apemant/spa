/****license*****************************************************************
**   file: repIzKnjizSIKum.java
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

public class repIzKnjizSIKum implements sg.com.elixir.reportwriter.datasource.IDataProvider
{

  _Main main;
  DataSet ds = ispKnjizSI.getQdsIspis();
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

  public repIzKnjizSIKum() {
    ru.setDataSet(ds);
  }

  public repIzKnjizSIKum(int idx) {
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
        return new repIzKnjizSIKum(indx++);
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

  public String getKonto()
  {
    return ds.getString("brojkonta");
  }

  public String getNazKonta()
  {
    ru.setDataSet(ds);
    colname[0] = "brojkonta";
//    String rez = ru.getSomething(colname,dm.getKonta() ,"nazivkonta").toString().trim();
    String rez = ru.getSomething(colname,hr.restart.zapod.raKonta.getAnalitickaKonta() ,"nazivkonta").toString().trim();
    return rez;
  }

  public String getSumLabela()
  {
    return "S V E U K U P N O";
  }

  public double getOsnDug()
  {
    return ds.getBigDecimal("OSNDUGUJE").doubleValue();
  }

  public double getOsnPot()
  {
    return ds.getBigDecimal("OSNPOTRAZUJE").doubleValue();
  }

  public double getIspDug()
  {
    return ds.getBigDecimal("ISPDUGUJE").doubleValue();
  }

  public double getIspPot()
  {
    return ds.getBigDecimal("ISPPOTRAZUJE").doubleValue();
  }

  public double getSaldo()
  {
    return ds.getBigDecimal("SALDO").doubleValue();
  }

  public double getSumOsnDug()
  {
    return ispKnjizSI.sume[0];
  }

  public double getSumOsnPot()
  {
    return ispKnjizSI.sume[1];
  }

  public double getSumIspDug()
  {
    return ispKnjizSI.sume[2];
  }

  public double getSumIspPot()
  {
    return ispKnjizSI.sume[3];
  }

  public double getSumSaldo()
  {
    return ispKnjizSI.sume[4];
  }

  public String getPocDat()
  {
    StringBuffer sb = new StringBuffer(ispKnjizSI.datumOd);
    sb.replace(2,3,".");
    sb.replace(5,6,".");
    return sb.toString();
  }

  public String getZavDat()
  {
    StringBuffer sb = new StringBuffer(ispKnjizSI.datumDo);
    sb.replace(2,3,".");
    sb.replace(5,6,".");
    return sb.toString();
  }

  public String getDatRange()
  {
    return getPocDat() + " do " + getZavDat();
  }

  public String getDatumProm(){
    return rdu.dataFormatter(ds.getTimestamp("DATPROMJENE"));
  }

  public String getVrPr()
  {
    return ds.getString("CPROMJENE");
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
