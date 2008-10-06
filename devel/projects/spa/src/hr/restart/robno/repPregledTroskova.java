/****license*****************************************************************
**   file: repPregledTroskova.java
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

import hr.restart.util.Valid;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;

public class repPregledTroskova implements raReportData {// sg.com.elixir.reportwriter.datasource.IDataProvider {
  
  _Main main;
  upPregledTroskova upt = upPregledTroskova.getInstance();
  DataSet ds/* = upt.getReportQDS()*/;

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();
  public static double iznosGrupe;
  public static double sumIzGrupe;

  public repPregledTroskova() {
    ds = upt.getReportQDS();
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
    ru.setDataSet(null);
    ds = null;
  }
  
//  public repPregledTroskova(int idx) {
//    if(idx==0){
//      sumIzGrupe = 0;
//    }
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
//        return new repPregledTroskova(indx++);
//      }
//      public boolean hasMoreElements() {
//        return (indx < ds.getRowCount());
//      }
//    };
//  }
//
//  public void close() {
//  }

  public String getCORG() {
    return ds.getString("CORG");
  }

  public String getNAZORG() {
    return ds.getString("NAZORG");
  }

  public String getCART() {
    if (upt.rpcart.jrfCART.getText().equals("") && !upt.jcbPoArtiklima.isSelected()) return "";
    return Aut.getAut().getCARTdependable(ds.getInt("CART")+"",ds.getString("CART1"),ds.getString("BC"));
  }

  public String getNAZART() {
    if (upt.rpcart.jrfNAZART.getText().equals("") && !upt.jcbPoArtiklima.isSelected()) return "Za sve artikle";
    return ds.getString("NAZART");
  }

  public String getCVRTR() {
    if (upt.getCvrtr().equals("")) return "";
    return upt.getCvrtr();
  }

  public String getVRSTROS() {
    if (upt.getCvrtr().equals("")) return "Svi Troškovi";
    return ds.getString("VRSTROS");
  }

  public String getCSKL() {
    return ds.getString("CSKL");
  }

  public String getNAZSKL() {
    return ds.getString("NAZSKL");
  }

  public String getCRADNAL() {
    return ds.getString("CRADNAL");
  }

  public String getStringDATDOK() {
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }

  public String getODDATUMA() {
    return rdu.dataFormatter(upt.getPocDatum());
  }

  public String getDODATUMA() {
    return rdu.dataFormatter(upt.getZavDatum());
  }

  public String getPodnaslov() {
    return "Za razdoblje od " + getODDATUMA() + " do " + getDODATUMA();
  }

  public int getBRDOK() {
    return ds.getInt("BRDOK");
  }

  public String getVRDOK() {
    return ds.getString("VRDOK");
  }

  public String getJM() {
    if (getCART().equalsIgnoreCase("")) return "";
    return ds.getString("JM");
  }

  public double getKOL() {
    if (getCART().equalsIgnoreCase("")) return main.nul.doubleValue();
    return ds.getBigDecimal("KOL").doubleValue();
  }


  public double getIRAZ() {
    return ds.getBigDecimal("IRAZ").doubleValue();
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

  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }

}