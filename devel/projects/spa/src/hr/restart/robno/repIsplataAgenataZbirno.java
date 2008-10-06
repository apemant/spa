/****license*****************************************************************
**   file: repIsplataAgenataZbirno.java
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
/*
 * Created on 2004.11.15
 */
package hr.restart.robno;

import hr.restart.baza.dM;
import hr.restart.util.Valid;
import hr.restart.util.sysoutTEST;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;


/**
 * @author abf
 */
public class repIsplataAgenataZbirno implements raReportData {
  DataSet ds;
  frmIsplataAgenta fia = frmIsplataAgenta.getInstance();
  repMemo rm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  
  public repIsplataAgenataZbirno() {
    ds = fia.getJPTV().getSelectionView();
    ru.setDataSet(ds);
    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
    st.prn(ds);
  }
    
  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ru.setDataSet(null);
    fia.getJPTV().destroySelectionView();
    ds = null;
  }
  
  public String getPodnaslov(){
   return "U PERIODU " + fia.getPocDat() + " - " + fia.getZavDat(); 
  }
  
  public int getCAGENT() {
    try{
    return ds.getInt("CAGENT");
    } catch (Exception e) {
      return fia.getCAGENT();
    }
  }
  
  public String getNAZAGENT() {
    try {
    return ds.getString("NAZAGENT");
    } catch (Exception e) {
      return fia.getNAZAGENT();
    }
  }
  
  public double getUkupno() {
    return ds.getBigDecimal("RAC").doubleValue();
  }
  
  public double getNenaplaceno() {
    return ds.getBigDecimal("RAC").subtract(ds.getBigDecimal("NAP")).doubleValue();
  }  
  
  public double getNaplaceno() {
    return ds.getBigDecimal("NAP").doubleValue();
  }
  
  public double getPostotak() {
    return ds.getBigDecimal("POST").doubleValue();
  }
  
  public double getProvizija() {
    return ds.getBigDecimal("PROV").doubleValue();
  }
  
  public double getIsplaceno() {
    return ds.getBigDecimal("PLAC").doubleValue();
  }
  
  public double getOstatak() {
    return ds.getBigDecimal("SALDO").doubleValue();
  }
  
  public String getFirstLine(){
    return rm.getFirstLine();
  }

  public String getEnterFirstLine(){
    return "\n"+rm.getFirstLine();
  }

  public String getSecondLine(){
    return rm.getSecondLine();
  }

  public String getThirdLine(){
    return rm.getThirdLine();
  }
  
  
  //TODO for future use if needed ;)
  
  /*
  public String getDatumOd() {
    return fia.getPocDat();
  }
  
  public String getDatumDo() {
    return fia.getZavDat();
  }
  */

  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }
  
  public String getVrsta(){
    return ds.getString("VRDOK");
  }
  
  public String getDatum(){
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }
  
  public double getIznos(){
    return ds.getBigDecimal("IZNOS").doubleValue();
  }
  
  public int getBroj(){
    return ds.getInt("BRDOK");
  }
  
  public String getStatusNaplaceno(){
    return ds.getString("STATNAP");
  }
}
