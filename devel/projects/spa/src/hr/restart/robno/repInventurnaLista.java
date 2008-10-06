/****license*****************************************************************
**   file: repInventurnaLista.java
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

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright Rest-Art(c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author unascribed
 * @version 1.0
 */

public class repInventurnaLista implements raReportData { //sg.com.elixir.reportwriter.datasource.IDataProvider {

//  Object o = new Object();

//  _Main main;
  raInventurnaLista ril = raInventurnaLista.getInstanceOf();
  DataSet ds = ril.getRepSet();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

//  String[] colname = new String[] {""};
//  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo rm = repMemo.getrepMemo();

  public repInventurnaLista() {
//    System.out.println("INITIALIZING INVENTURNA LISTA");
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ds = null;
  }

//  public repInventurnaLista(int idx) {
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
//        return new repInventurnaLista(indx++);
//      }
//      public boolean hasMoreElements() {
//        return (indx < ds.getRowCount());
//      }
//    };
//  }
//
//  public void close() {
//  }

  public String getCART() {
    return Aut.getAut().getCARTdependable(ds);
  }

  public String getNAZART() {
    return ds.getString("NAZART");
  }

  public String getJM() {
    return ds.getString("JM");
  }

  public String getCSKL() {
    return ds.getString("CSKL");
  }

  public String getNAZSKL() {
    return ds.getString("NAZSKL");
  }

  public String getDatumIsp(){
	 return rdu.dataFormatter(vl.getToday());
  }

  public String getDATINV() {
    return String.valueOf(rdu.dataFormatter(ds.getTimestamp("DATINV")));
  }

  public BigDecimal getZC() {
    return ds.getBigDecimal("ZC");
  }

  public BigDecimal getKOLINV() {
    return ds.getBigDecimal("KOLINV");
  }

  public double getVRIINV() {
  return ds.getBigDecimal("VRIINV").doubleValue();
  }

  public BigDecimal getKOLKNJ() {
    return ds.getBigDecimal("KOLKNJ");
  }

  public double getVRIKNJ() {
    return ds.getBigDecimal("VRIKNJ").doubleValue();
  }

  public BigDecimal getKOLMANJ() {
    return ds.getBigDecimal("KOLMANJ");
  }

  public double getVRIMANJ() {
    return ds.getBigDecimal("VRIMANJ").doubleValue();
  }

  public double getVRIVIS() {
    return ds.getBigDecimal("VRIVIS").doubleValue();
  }

  public BigDecimal getKOLVIS() {
    return ds.getBigDecimal("KOLVIS");
  }

  public String getFirstLine(){
    return rm.getFirstLine();
  }

  public String getSecondLine(){
    return rm.getSecondLine();
  }

  public String getThirdLine(){
    return rm.getThirdLine();
  }

  public String getSORTER(){
    String SORTER;
    if (ril.getSORTER().equals("NAZART")) SORTER = ds.getString("NAZART");
    else SORTER = getCART();
    return SORTER;
  }
}