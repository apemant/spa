/****license*****************************************************************
**   file: repIzvodi.java
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
package hr.restart.gk;

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.Valid;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repIzvodi implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  frmIzvodi fizv = frmIzvodi.getFrmIzvodi();
  DataSet ds = fizv.getMasterSet();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();
  public static double iznosGrupe;
  public static int rb;

  public repIzvodi() {
    ru.setDataSet(ds);
//    sysoutTEST ST = new sysoutTEST(false);
//    System.out.println("------------------------ MASTERSET -----------------------------");
//    ST.prn(ds);
//    System.out.println("-----------------------------------------------------");
  }

  public repIzvodi(int idx) {
    if(idx==0){
      rb = 0;
    }
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
      }
      int indx=0;
      public Object nextElement() {

        return new repIzvodi(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public int getBROJIZV(){
    return ds.getInt("BROJIZV");
  }

  public int getBROJSTAVKI(){
    return ds.getInt("BROJSTAVKI");
  }

  public BigDecimal getID(){
    return ds.getBigDecimal("ID");
  }

  public BigDecimal getIP(){
    return ds.getBigDecimal("IP");
  }

  public String getDATUM(){
    return rdu.dataFormatter(ds.getTimestamp("DATUM"));
  }

  public BigDecimal getPRETHSTANJE(){
    return ds.getBigDecimal("PRETHSTANJE");
  }

  public BigDecimal getNOVOSTANJE(){
    return ds.getBigDecimal("NOVOSTANJE");
  }
  public String getSTATUS(){
    String s = ds.getString("STATUS");
    if (s.equals("K")) return "Knjižen";
    if (s.equals("S")) return "Spreman";
    if (s.equals("N")) return "Neispravan";
    return "Goddamnit";
  }

  public String getNASLOV(){
    return "\nIZVODI " + fizv.idizvod;
  }

  public String getZaPeriod(){
    return getPocDat() + " do " +getZavDat();
  }

  public String getPocDat(){
    String date = fizv.getPreSelect().getSelRow().getTimestamp("DATUM-from").toString();
    String year = date.substring(0,4);
    String month = date.substring(5,7);
    String day = date.substring(8,10);

    return day+"."+month+"."+year+".";
  }

  public String getZavDat(){
    String date = fizv.getPreSelect().getSelRow().getTimestamp("DATUM-to").toString();
    String year = date.substring(0,4);
    String month = date.substring(5,7);
    String day = date.substring(8,10);

    return day+"."+month+"."+year+".";
  }

  public String getFirstLine(){
    return re.getFirstLine();
  }
  public String getSecondLine(){
    return re.getSecondLine();
  }
  public String getThirdLine(){
    return re.getThirdLine();
  }
  public String getLogoMjesto(){
    return re.getLogoMjesto();
  }
  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }
}