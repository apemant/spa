/****license*****************************************************************
**   file: repMarzaKol.java
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

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repMarzaKol implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  repMemo rpm = repMemo.getrepMemo();
  DataSet ds = dlgPregledRUC.getInstance().getJPTV().getDataSet();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  String param = dlgPregledRUC.getInstance().getParam();
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  public repMarzaKol() {
  }

  public repMarzaKol(int idx) {
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

        return new repMarzaKol(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }
  public void close() {}

  public BigDecimal getKol()
 {
   return ds.getBigDecimal("KOL");
  }

  public String getNazArt()
  {
    return ds.getString("NAZART");
  }

  public String getNazSklad()
  {
    String cskl = ds.getString("CSKL");
    lookupData.getlookupData().raLocate(dm.getSklad(), new String[]{"CSKL"}, new String[]{cskl});
    return dm.getSklad().getString("NAZSKL");
  }

  public int getIdx()
  {
    return ds.getRow()+1;
  }


  public String getSifra()
  {
    if (param.equals("CART"))
    {
      return ds.getInt("CART")+"";
    }
    else if(param.equals("CART1"))
    {
      return ds.getString("CART1");
    }
    else
    {
      return ds.getString("BC");
    }
  }

  public String getCSKL()
  {
    return ds.getString("CSKL");
  }

  public String getJM()
  {
    return ds.getString("JM");
  }

  public double getNC()
  {
    return ds.getBigDecimal("NC").doubleValue();
  }

  public double getVC()
  {
    return ds.getBigDecimal("VC").doubleValue();
  }

 public double getMarza()
 {
   return ds.getBigDecimal("MARZA").doubleValue();
 }

 public double getRuc()
{
  return ds.getBigDecimal("RUC").doubleValue();
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
    return "";
    //return (ds.getString("KOLPD")+ " do " + ds.getString("KOLZD"));
  }
}