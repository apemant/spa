/****license*****************************************************************
**   file: repIspisAmor_0.java
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

public class repIspisAmor_0 implements sg.com.elixir.reportwriter.datasource.IDataProvider
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


  int oblikListe = ispAmor.getSelectedRB();

  int rowCount=0;
  static String tempOL="";

  public repIspisAmor_0() {
    ru.setDataSet(ds);
  }

  public repIspisAmor_0(int idx) {
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
        return new repIspisAmor_0(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

   public String getFake()
  {
    return "a";
  }

  public void close() {
  }

  public String getNazSredstva()
  {
    return ds.getString("nazsredstva");
  }

  public String getFirstLine(){
    return rpm.getFirstLine();
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

  public String getDatumProm(){
    return rdu.dataFormatter(ds.getTimestamp("DATPROMJENE"));
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

  public int getRowNum()
  {
    return rowCount;
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

  public String getTipAmor()
  {
    String sCase = ds.getString("TIPAMOR");

    if(sCase.equals("P")) return "\nGODIŠNJI PREDRAÈUN AMORTIZACIJE";
    else if(sCase.equals("M")) return "\nMJESEÈNI OBRAÈUN AMORTIZACIJE";
    else if(sCase.equals("S")) return "\nSIMULACIJA GODIŠNJEG OBRAÈUNA AMORTIZACIJE";
    else return "\nGODIŠNJI OBRAÈUN AMORTIZACIJE";
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

  public String getCaption2()
  {
     if(poPripCORG)
       return "( po org. jedinici s pripadajuæim org. jedinicama )";
     else
       return "( po org. jedinici )";
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
}
