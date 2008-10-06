/****license*****************************************************************
**   file: repSveKartice.java
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
import com.borland.dx.dataset.DataSet;

public class repSveKartice implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  repMemo rpm = repMemo.getrepMemo();
  upSveKartice upk = upSveKartice.getupSveKartice();
  DataSet ds = upk.getQds();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  public repSveKartice() {
  }

  public repSveKartice(int idx) {
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
        ru.setDataSet(ds);
      }
      int indx=0;
      public Object nextElement() {

        return new repSveKartice(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }
  public void close() {}
  /*public BigDecimal getKOL() {
    return ds.getBigDecimal("KOL");
  }*/
  public String getCskl(){
    return upk.getCskl();
  }
 /* public String getNazivCskl(){
    dm.getSklad().interactiveLocate(upk.getCskl(),"CSKL",com.borland.dx.dataset.Locate.FIRST,false);
    return dm.getSklad().getString("NAZSKL");
  }
  public int getCart() {
    return upk.getCart();
  }
  public String getNazart() {
    dm.getArtikli().interactiveLocate(String.valueOf(getCart()),"CART",com.borland.dx.dataset.Locate.FIRST,false);
    return dm.getArtikli().getString("NAZART");
  }*/
  public String getPocDat(){
    return rdu.dataFormatter(upk.getPocDatum());
  }
  public String getZavDat(){
    return rdu.dataFormatter(upk.getZavDatum());
  }
  public String getCaption2(){
    return ("od "+getPocDat()+" do "+getZavDat());
  }
  public String getDatumIsp(){
    return rdu.dataFormatter(val.getToday());
  }
  public String getDatDok(){
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }
  public int getBrDok() {
    return ds.getInt("BRDOK");
  }
  public String getVrDok(){
    return ds.getString("VRDOK");
  }
  public String getKolUl(){
    return (ds.getBigDecimal("KOLUL").toString());
  }
  public String getKolIz(){
    return (ds.getBigDecimal("KOLIZ").toString());
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
}