/****license*****************************************************************
**   file: repInvIspSI.java
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

public class repInvIspSI implements sg.com.elixir.reportwriter.datasource.IDataProvider
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
  static int sumIdx = 0;
  int rowCount=0;


  String tempOL ="";
  String cs="";

  public repInvIspSI() {
    ru.setDataSet(ds);
  }

  public repInvIspSI(int idx) {
    ds.goToRow(idx);
    sumIdx = idx;
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
        rowCount= ds.getRowCount();
      }
      int indx=0;
      public Object nextElement() {
        return new repInvIspSI(indx++);
      }
      public boolean hasMoreElements() {
        sumIdx = indx;
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

   public String getDokument()
  {
    return ds.getString("dokument");
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
    else if(ispSI.getSelectedRB2()==1 || ispSI.getSelectedRB2()==2)
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

  public String getOblikListe()
  {
    String ol="";
    switch (oblikListe)
    {
      case 0:
        ol= ds.getString("brojkonta");
        break;
      case 1:
        ol= ds.getString("cgrupe");
        break;
      case 2:
        ol= ds.getString("clokacije");
        break;
      case 3:
        ol= ds.getString("cskupine");
        break;
      case 4:
        ol= ds.getString("cartikla");
        break;
    }
    return ol;
  }

  public String getLabelaListe()
  {
    String ll="";
    switch (oblikListe)
    {
      case 0:
        ll= "Konto";
        tempOL="kontu";
        break;
      case 1:
        ll= "Amor. grupa";
        tempOL="amortizacijskoj grupi";
        break;
      case 2:
        ll= "Lokacija";
        tempOL="lokaciji";
        break;
      case 3:
        ll= "Rev. skupina";
        tempOL="revalorizacijskoj skupini";
        break;
      case 4:
        ll= "Artikl";
        tempOL="artiklu";
        break;
    }
    return ll;
  }

   public String getLabelaListe1()
  {
    String ll="";
    switch (oblikListe)
    {
      case 0:
        ll= "konto";
        break;
      case 1:
        ll= "amor. grupa";
        break;
      case 2:
        ll= "lokacija";
        break;
      case 3:
        ll= "rev. skupina";
        break;
      case 4:
        ll= "artikl";
        break;
    }
    return ll;
  }

  public String getNazOblikListe()
  {
    ru.setDataSet(ds);
    String rez="";

    switch (oblikListe)
    {
      case 0:
        colname[0] = "brojkonta";
//        rez = ru.getSomething(colname,dm.getKonta() ,"nazivkonta").toString().trim();
        rez = ru.getSomething(colname,hr.restart.zapod.raKonta.getAnalitickaKonta() ,"nazivkonta").toString().trim();
        break;
      case 1:
        colname[0] = "cgrupe";
        rez = ru.getSomething(colname,dm.getOS_Amgrupe() ,"nazgrupe").toString().trim();
        break;
      case 2:
        colname[0] = "clokacije";
        rez = ru.getSomething(colname,dm.getOS_Lokacije() ,"nazlokacije").toString().trim();
        break;
      case 3:
        colname[0] = "cskupine";
        rez = ru.getSomething(colname,dm.getOS_Revskupine() ,"nazskupine").toString().trim();
        break;
      case 4:
        colname[0] = "cartikla";
        rez = ru.getSomething(colname,dm.getOS_Artikli() ,"nazartikla").toString().trim();
        break;
    }


    return rez;

  }

  public String getSum()
  {
    if(sumIdx==(rowCount - 1))
      return getLabelaListe() + " " + getOblikListe();
    return "";
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


  private String convertDouble(String valueStr)
  {
    String decPart = "";
    String intPart = "";
    String temp="";
    String parseStr="";
    int i = 0;
    do
    {
      parseStr = valueStr.substring(i,i+1);

      if (!(parseStr.equals(".") || (parseStr.equals(","))))
        temp = temp + parseStr;
      if (parseStr.equals("."))
      {
        intPart = temp;
        temp = "";
      }
      i++;
    }while(i<valueStr.length());
    decPart = temp;
    if (decPart.length()==1)
      decPart = decPart +"0";
    String returnValue = intPart + "," + decPart;
    return returnValue;
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

  public String getCaption()
  {
    return "ISPIS SITNOG INVENTARA";
  }

   public String getCaption2()
  {
     if(poPripCORG)
      return "( po org. jedinici i "+tempOL+" s inventarskim brojem s pripadajuæim org. jedinicama )";
    return "( po org. jedinici i "+tempOL+" s inventarskim brojem )";
  }

  public String getcaption3()
  {
    String vl="";
    switch (ispOS.getSelectedRB2()) {
      case 0:
        vl="Poèetno stanje";
        break;
      case 2:
        vl="Stanje na dan " + formatDatum(ispOS.getDan());
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
