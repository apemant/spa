/****license*****************************************************************
**   file: repIspisOS_4.java
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

public class repIspisOS_4 implements sg.com.elixir.reportwriter.datasource.IDataProvider
{

  _Main main;
  DataSet ds = ispOS.getQdsIspis();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();

  boolean poPripCORG = ispOS.poPripCorg;
  double osn;
  double isp;
  int oblikListe = ispOS.getSelectedRB();
  //static int sumIdx = 0;
  int rowCount=0;

  public repIspisOS_4() {
    ru.setDataSet(ds);
//    sysoutTEST ST = new sysoutTEST(false);
//    ST.prn(ds);
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


  public repIspisOS_4(int idx) {
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
        return new repIspisOS_4(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public String getFake()
  {
    return"a";
  }

  public String getInvBr()
  {
    return ds.getString("invbr");
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
    if(ispOS.getSelectedRB2()==0)
    {
      osn = ds.getBigDecimal("OSNPOCETAK").doubleValue();
    }
    else if(ispOS.getSelectedRB2()==1 || ispOS.getSelectedRB2()==2)
    {
      osn = (ds.getBigDecimal("OSNDUGUJE").add(ds.getBigDecimal("OSNPOTRAZUJE").negate())).doubleValue();
    }
    return osn;
  }

   public double getIspVr()
  {
    if(ispOS.getSelectedRB2()==0)
    {
      isp = ds.getBigDecimal("ISPPOCETAK").doubleValue();
    }
    else if (ispOS.getSelectedRB2()==1 || ispOS.getSelectedRB2()==2)
    {
      try {
        isp = (ds.getBigDecimal("ISPPOTRAZUJE").add(ds.getBigDecimal("ISPDUGUJE").negate()).add(ds.getBigDecimal("AMORTIZACIJA")).add(ds.getBigDecimal("PAMORTIZACIJA"))).doubleValue();
      }
      catch (Exception ex) {
        System.err.println("EXCEPTION - radim bez amortizacije!!");
        isp = (ds.getBigDecimal("ISPPOTRAZUJE").add(ds.getBigDecimal("ISPDUGUJE").negate())).doubleValue();
      }

//      isp = (ds.getBigDecimal("ISPPOTRAZUJE").add(ds.getBigDecimal("ISPDUGUJE").negate())).doubleValue();
    }
    return isp;
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
    return ispOS.sume[1];
  }

  public double getSumOsn_Isp()
  {
    return ispOS.sume[2];
  }

  public double getSumOsn()
  {
    return ispOS.sume[0];
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
    return "ISPIS OSNOVNIH SREDSTAVA";
  }
  public String getCaption2()
  {
    if(poPripCORG)
      return "( po inventarskom broju i pripadajuæim org. jedinicama )";
    else
      return "( po inventarskom broju )";
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

  public String getStatus()
  {
    String strStatus = "Sva OS";
    if(ispOS.status.equals("I"))
      strStatus = "Investicije u tijeku";
    else if(ispOS.status.equals("P"))
      strStatus = "OS u pripremi";
    else if(ispOS.status.equals("A"))
      strStatus = "OS u upotrebi";
    return strStatus;
  }

  public String getPorijeklo()
  {
    String strPorijeklo = "Sva porijekla";
    if(ispOS.porijeklo.equals("1"))
      strPorijeklo = "Tuzemstvo";
    else if(ispOS.porijeklo.equals("2"))
      strPorijeklo = "Inozemstvo";
    else if(ispOS.porijeklo.equals("3"))
      strPorijeklo = "Vrijednosnice";
    return strPorijeklo;
  }

  public String getAktivnost()
  {
    String strAktivnost = "Sva OS";
    if(ispOS.aktivnost.equals("D"))
      strAktivnost = "Aktivna OS";
    else if (ispOS.aktivnost.equals("N"))
      strAktivnost = "Neaktivna OS";
    return strAktivnost;
  }

//  public String getAktivnost()
// {
//   String strAktivnost = "Sva OS";
//   if(ispOS.aktivnost.equals("D"))
//     strAktivnost = "Aktivna OS";
//   else if (ispOS.aktivnost.equals("N"))
//     strAktivnost = "Neaktivna OS";
//   if(poPripCORG)
//     return strAktivnost+"\n"+getCOrg()+" "+getNazOrg()+" sa pripadajuæim org. jedinicama";
//   return strAktivnost;
// }
//
// public String getAktivnostLab()
// {
//   if(poPripCORG)
//     return "Aktivnost\nOrg.jed.";
//   else
//     return "Aktivnost";
//  }

  private String formatDatum(String datum)
  {
    StringBuffer sb = new StringBuffer(datum);
    sb.replace(2,3,".");
    sb.replace(5,6,".");
    return sb.toString();
  }
}
