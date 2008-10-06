/****license*****************************************************************
**   file: repArtikli.java
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
package hr.restart.robno;

import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;

public class repArtikli implements raReportData {

  _Main main;
  DataSet ds = hr.restart.baza.dM.getDataModule().getArtikli() ;
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();

  public repArtikli() {
        ds.open();
        ru.setDataSet(ds);
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.getRowCount();
  }

  public void close() {
//    upUlazIzlaz.getInstance().getJPTV().enableEvents(true);
    ds = null;
  }

//  public repArtikli(int idx) {
//    ds.goToRow(idx);
//  }
//
//  public java.util.Enumeration getData() {
//    return new java.util.Enumeration() {
//      {
//        ds.open();
//        ru.setDataSet(ds);
//      }
//      int indx=0;
//      public Object nextElement() {
//
//        return new repArtikli(indx++);
//      }
//      public boolean hasMoreElements() {
//        return (indx < ds.getRowCount());
//      }
//    };
//  }
//
//  public void close() {
//  }

  public String getOZNAKA()
  {
    return Aut.getAut().getCARTdependable(ds);
//    String sifra = hr.restart.sisfun.frmParam.getParam("robno","indiCart");
//    if (sifra.equals("CART"))
//    {
//      return ds.getInt("CART")+"";
//    }
//    else if(sifra.equals("CART1"))
//    {
//      return ds.getString("CART1");
//    }
//    else
//    {
//      return ds.getString("BC");
//    }
  }

  public String getCART(){
    return ds.getInt("CART")+"";
  }

  public String getCART1()
  {
    return ds.getString("CART1");
  }

  public String getBC()
  {
    return ds.getString("BC");
  }

  public String getNAZART()
  {
    return ds.getString("NAZART");
  }

  public String getJM()
  {
    return ds.getString("JM");
  }

  public String getCPOR()
  {
    return ds.getString("CPOR");
  }


  public String getTIPART()
  {
    String temp = ds.getString("TIPART");

    if (temp.equals("R"))
      return "Roba";
    else if (temp.equals("M"))
      return "Materijal";
    else if (temp.equals("U"))
      return "Usluga";
    else if (temp.equals("P"))
      return "Proizvod";

    return "";
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

  public String getGRART()
  {
    ru.setDataSet(ds);
    colname[0] = "CGRART";
    String prvi = ds.getString("CGRART");
    String drugi = ru.getSomething(colname,dm.getGrupart() ,"NAZGRART").toString();
    return (prvi.concat("-").concat(drugi));
  }
}
