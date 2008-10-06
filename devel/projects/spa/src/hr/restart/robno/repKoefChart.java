/****license*****************************************************************
**   file: repKoefChart.java
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
public class repKoefChart implements sg.com.elixir.reportwriter.datasource.IDataProvider {


  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  DataSet ds = raKoefObrt.getInstance().getqDSKoefObrtPOJ();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repUtil ru = repUtil.getrepUtil();
  String[] colname = new String[] {""};
  public repKoefChart() {
  }

  public repKoefChart(int idx) {
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
        return new repKoefChart(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }
  public int getCART(){

    return ds.getInt("CART");
  }
  public String getCARTN(){
    ru.setDataSet(ds);
    colname[0] = "CART";
    return String.valueOf(ds.getInt("CART")).concat("-").concat(
        ru.getSomething(colname,dm.getArtikli(),"NAZART").getString());

  }
  public String getCSKL(){
//    ru.setDataSet(ds);
    colname[0] = "CSKL";
    return ds.getString("CSKL").concat("-").concat(
        ru.getSomething(colname,dm.getSklad(),"NAZSKL").getString());
  }

  public double getKoeficijent(){
    return ds.getBigDecimal("KOEFICIJENT").doubleValue();
  }
  public double getDANZAL(){
    return ds.getBigDecimal("DANZAL").doubleValue();
  }

  public void close() {
  }
}