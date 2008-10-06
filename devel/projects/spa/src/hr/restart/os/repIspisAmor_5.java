/****license*****************************************************************
**   file: repIspisAmor_5.java
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

public class repIspisAmor_5 implements sg.com.elixir.reportwriter.datasource.IDataProvider
{

  _Main main;
  DataSet ds = ispAmor.getQdsIspis();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();
  boolean poPripCORG = ispAmor.poPripCorg;


  double osn;
  double isp;
  int oblikListe = ispAmor.getSelectedRB();
  //static int sumIdx = 0;
  int rowCount=0;

  public repIspisAmor_5() {
    ru.setDataSet(ds);
  }

  public repIspisAmor_5(int idx) {
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
        return new repIspisAmor_5(indx++);
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

  public String getNazSredstva()
  {
    return ds.getString("nazsredstva");
  }

  public double getOsnVr()
  {
    return ds.getBigDecimal("OSNOVICA").doubleValue();
  }

   public double getIspVr()
  {
    return ds.getBigDecimal("ISPRAVAK").doubleValue();
  }

  public double getAmVr()
  {
    return ds.getBigDecimal("AMORTIZACIJA").doubleValue();
  }

   public double getPamVr()
  {
    return ds.getBigDecimal("PAMORTIZACIJA").doubleValue();
  }

  public double getSadVr()
  {
    return ds.getBigDecimal("SADVRIJED").doubleValue();
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

  public String getDatumProm(){
    return rdu.dataFormatter(ds.getTimestamp("DATPROMJENE"));
  }


  public String getSumLabela()
  {
    return "S V E U K U P N O";
  }

  public double getSumIsp()
  {
    return ispAmor.sume[1];
  }

  public double getSumSadvr()
  {
    return ispAmor.sume[4];
  }

  public double getSumAm()
  {
    return ispAmor.sume[2];
  }

  public double getSumPam()
  {
    return ispAmor.sume[3];
  }

  public double getSumOsn()
  {
    return ispAmor.sume[0];
  }

  public String getCaption2()
  {
    if(poPripCORG)
      return "( po inventarskom broju s pripadajuæim org. jedinicama )";
    else
       return "( po inventarskom broju )";
  }

  public String getCaption()
  {
    return "GODIŠNJI OBRAÈUN AMORTIZACIJE";
  }


  public String getcaption3()
  {
    String datum1 = rdu.dataFormatter(ds.getTimestamp("DATUMOD"));
    String datum2 = rdu.dataFormatter(ds.getTimestamp("DATUMDO"));
    return  datum1+" do "+datum2;
  }

  private String formatDatum(String datum)
  {
    StringBuffer sb = new StringBuffer(datum);
    sb.replace(2,3,".");
    sb.replace(5,6,".");
    return sb.toString();
  }

   public String getTipAmor()
  {
    String sCase = ds.getString("TIPAMOR");

    if(sCase.equals("P")) return "\nGODIŠNJI PREDRAÈUN AMORTIZACIJE";
    else if(sCase.equals("M")) return "\nMJESEÈNI OBRAÈUN AMORTIZACIJE";
    else if(sCase.equals("S")) return "\nSIMULACIJA GODIŠNJEG OBRAÈUNA AMORTIZACIJE";
    else return "\nGODIŠNJI OBRAÈUN AMORTIZACIJE";
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
