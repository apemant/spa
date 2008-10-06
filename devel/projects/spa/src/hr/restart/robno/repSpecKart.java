/****license*****************************************************************
**   file: repSpecKart.java
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
import hr.restart.util.lookupData;

import java.sql.Date;
import java.sql.Timestamp;

import com.borland.dx.dataset.DataSet;

public class repSpecKart implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  repMemo rpm = repMemo.getrepMemo();
  DataSet ds = frmSpecKart.getInstance().getQDS();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  String param = frmSpecKart.getInstance().param;
  Timestamp datOd = frmSpecKart.getInstance().datOd;
  Timestamp datDo = frmSpecKart.getInstance().datDo;
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  int rowCount=0;

  public repSpecKart() {
  }

  public repSpecKart(int idx) {
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
        ru.setDataSet(ds);
        rowCount = ds.getRowCount();
      }
      int indx=0;
      public Object nextElement() {
        return new repSpecKart(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }
  public void close() {}

  public int getRowNum()
  {
    return rowCount;
  }

  public double getSuma()
  {
    return frmSpecKart.getInstance().suma;
  }

  public int getIdx()
  {
    return ds.getRow()+1;
  }

  public String getBrRac()
  {
    return ds.getString("CSKL")+"-"+ds.getString("VRDOK")+"-"+
    ds.getString("GOD")+"-"+ds.getInt("BRDOK");
  }

  public String getDatdok()
  {
    try
    {
      return rdu.dataFormatter(ds.getTimestamp("DATPRIM"));
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public Date getOrders() {
    if(param.equals("prim"))
      return Date.valueOf(ds.getTimestamp("DATNAP").toString().substring(0,10));
    return Date.valueOf(ds.getTimestamp("DATPRIM").toString().substring(0,10));
  }

  public String getDatnap()
  {
    return rdu.dataFormatter(ds.getTimestamp("DATNAP"));
  }

  public String getTBG()
  {
    return ds.getString("TRG");
  }

  public String getSBC()
  {
    return ds.getString("SBC");
  }

  public String getCBanke()
  {
    return ds.getString("CBANKA");
  }

  public String getNazBanke()
  {
    String cbanka = getCBanke();
//    System.out.println("CBANKE: " + cbanka);

    boolean t =lookupData.getlookupData().raLocate(dm.getBanke(), new String[]{"CBANKA"}, new String[]{cbanka});
    if(t)
      return dm.getBanke().getString("NAZIV");
    else
      lookupData.getlookupData().raLocate(dm.getKartice(), new String[]{"CBANKA"}, new String[]{cbanka});
    return dm.getKartice().getString("NAZIV");
  }

  public String getZiroBanke()
  {
    String cbanka = getCBanke();
    lookupData.getlookupData().raLocate(dm.getBanke(), new String[]{"CBANKA"}, new String[]{cbanka});
    return dm.getBanke().getString("ZIRO");
  }

  public double getIznos()
  {
    return ds.getBigDecimal("IZNOS").doubleValue();
  }

  public String getCSKL()
  {
    return ds.getString("CSKL");
  }

  public String getNazSklad()
  {
    String cskl = getCSKL();
    lookupData.getlookupData().raLocate(dm.getSklad(), new String[]{"CSKL"}, new String[]{cskl});
    return dm.getSklad().getString("NAZSKL");
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

  public String getDatumIsp()
  {
    return rdu.dataFormatter(val.getToday());
  }

  public String getCaption()
  {
    if(param.equals("prim"))
      return "po datumu primitka za period "+getPeriod();
    return "po datumu naplate za period "+getPeriod();
  }

  public String getSumLabela()
  {
    return "S V E U K U P N O";
  }

  private String getPeriod()
  {
    return rdu.dataFormatter(datOd)+"-"+rdu.dataFormatter(datDo);
  }
}
//
