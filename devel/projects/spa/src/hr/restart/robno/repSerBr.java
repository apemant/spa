/****license*****************************************************************
**   file: repSerBr.java
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

public class repSerBr implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  repMemo rpm = repMemo.getrepMemo();
  frmSerBr upk = frmSerBr.getupSerBr();
  DataSet ds = upk.getQds();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  public repSerBr() {
  }

  public repSerBr(int idx) {
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

        return new repSerBr(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }
  public void close() {}


  public String getBrdok()
  {
    return ds.getInt("BRDOKIZ")+"";
  }


  public String getSerBr()
  {
    return ds.getString("CSERBR");
  }

  public String getVrdok()
  {
    return ds.getString("VRDOK");
  }

  public String getRbr()
  {
    return ds.getShort("RBR")+"";
  }

  public String getCskl()
  {
    return ds.getString("CSKLIZ");
  }

  public String getGod()
  {
    return ds.getString("GOD");
  }

  public String getNazskl()
  {
    return ds.getString("NAZSKL");
  }


  public String getCart()
  {
    return ds.getInt("CART")+"";
  }

  public String getNazart()
  {
    return ds.getString("NAZART");
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

  public int getDummy()
  {
    return 1;
  }
}