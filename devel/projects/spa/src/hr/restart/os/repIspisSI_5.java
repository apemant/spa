/****license*****************************************************************
**   file: repIspisSI_5.java
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

public class repIspisSI_5 implements sg.com.elixir.reportwriter.datasource.IDataProvider
{

  _Main main;
  DataSet ds = ispSI.getQdsIspis();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();
  boolean poPripCORG = ispSI.poPripCorg;


  double osn;
  double isp;
  int oblikListe = ispSI.getSelectedRB();
  //static int sumIdx = 0;
  int rowCount=0;
  static String tempOL="";

  public repIspisSI_5() {
    ru.setDataSet(ds);
  }

  public repIspisSI_5(int idx) {
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
        return new repIspisSI_5(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public String getCOrg()
  {
    return ds.getString("corg");
  }

  public String getInvBr()
  {
    System.out.println("INV BROJ: " + ds.getString("invbr"));
    return ds.getString("invbr");
  }

  public String getNazSredstva()
  {
    return ds.getString("nazsredstva");
  }

  public String getDatumOS()
  {
    String datum = rdu.dataFormatter(ds.getTimestamp("datum"));
    if (datum.equals("01.01.1970"))
      return "";
    else
      return datum;

  }

  public String getFirstLine(){
    return rpm.getFirstLine();
  }

  public double getOsnVr()
  {
    if(ispSI.getSelectedRB2()==0)
    {
      osn = ds.getBigDecimal("OSNPOCETAK").doubleValue();
    }
    else if(ispSI.getSelectedRB2()==1 || ispSI.getSelectedRB2()==2)
    {
      osn = (ds.getBigDecimal("OSNDUGUJE").add(ds.getBigDecimal("OSNPOTRAZUJE").negate())).doubleValue();
    }
    return osn;
  }

   public double getIspVr()
  {
    if(ispSI.getSelectedRB2()==0)
    {
      isp = ds.getBigDecimal("ISPPOCETAK").doubleValue();
    }
    else if (ispSI.getSelectedRB2()==1 || ispSI.getSelectedRB2()==2)
    {
      isp = (ds.getBigDecimal("ISPPOTRAZUJE").add(ds.getBigDecimal("ISPDUGUJE").negate())).doubleValue();
    }
    return isp;
  }

  public String getNazOrg()
  {
    ru.setDataSet(ds);
    colname[0] = "corg";
    String rez = ru.getSomething(colname,dm.getOrgstruktura() ,"naziv").toString().trim();
    return rez;
  }

  public double getSadVr()
  {
    return (osn-isp);
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
    return ispSI.sume[1];
  }

  public double getSumOsn_Isp()
  {
    return ispSI.sume[2];
  }

  public double getSumOsn()
  {
    return ispSI.sume[0];
  }

  public String getDokument()
  {
    return ds.getString("dokument");
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
      return "( po org. jedinici s inventarskim brojem i pripadajuæim org. jedinicama )";
    return "( po org. jedinici s inventarskim brojem )";
  }

  public String getCaption()
  {
    return "ISPIS SITNOG INVENTARA";
  }

  public String getcaption3()
  {
    String vl="";
    switch (ispSI.getSelectedRB2()) {
      case 0:
        vl="Poèetno stanje";
        break;
      case 2:
        vl="Stanje na dan " + formatDatum(ispSI.getDan());
        break;
      case 1:
        vl="Trenutno stanje";
        break;
    }
    return vl;
  }

  private String formatDatum(String datum)
  {
    StringBuffer sb = new StringBuffer(datum);
    sb.replace(2,3,".");
    sb.replace(5,6,".");
    return sb.toString();
  }

  public String getStatus()
{
  String strStatus = "Sav SI";
  if(ispSI.statusSI.equals("I"))
    strStatus = "Investicije u tijeku";
  else if(ispSI.statusSI.equals("P"))
    strStatus = "SI u pripremi";
  else if(ispSI.statusSI.equals("A"))
    strStatus = "SI u upotrebi";
  System.out.println("STATUS : " + strStatus);
  return strStatus;
  }

  public String getPorijeklo()
  {
    String strPorijeklo = "Sva porijekla";
    if(ispSI.porijekloSI.equals("1"))
      strPorijeklo = "Tuzemstvo";
    else if(ispSI.porijekloSI.equals("2"))
      strPorijeklo = "Inozemstvo";
    else if(ispSI.porijekloSI.equals("3"))
      strPorijeklo = "Vrijednosnice";
    return strPorijeklo;
  }

  public String getAktivnost()
  {
    String strAktivnost = "Sav SI";
    if(ispSI.aktivnostSI.equals("D"))
      strAktivnost = "Aktivni SI";
    else if (ispSI.aktivnostSI.equals("N"))
      strAktivnost = "Neaktivni SI";
    return strAktivnost;
  }
}
