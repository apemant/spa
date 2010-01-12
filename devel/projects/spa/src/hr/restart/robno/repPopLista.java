/****license*****************************************************************
**   file: repPopLista.java
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

public class repPopLista implements raReportData { //sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  raPopListaInv rpl = raPopListaInv.getInstance();
  DataSet ds = rpl.getRepSet();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  String[] colname = new String[] {""};
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo rm = repMemo.getrepMemo();


  public repPopLista() {
    ds.enableDataSetEvents(false);
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ds.enableDataSetEvents(true);
    ds = null;
  }

  public String getCART() {
    return Aut.getAut().getIzlazCARTdep(ds);
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

  public String getFirstLine(){
    return rm.getFirstLine();
  }

  public String getSecondLine(){
    return rm.getSecondLine();
  }

  public String getThirdLine(){
    return rm.getThirdLine();
  }

  public String getDatum(){
    return rdu.dataFormatter(rpl.getDatum());
  }

  public String getDatumIsp(){
	 return rdu.dataFormatter(vl.getToday());
  }

  public String getSORTER(){
    String SORTER;
    if (rpl.getSORTER().equals("NAZART")) SORTER = ds.getString("NAZART");
    else SORTER = getCART();
//    return SORTER;
    return "";
  }
}