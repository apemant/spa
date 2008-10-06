/****license*****************************************************************
**   file: repZaduzenjeKPR.java
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

import hr.restart.baza.dM;
import hr.restart.util.Valid;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.borland.dx.dataset.DataSet;

/**
 * @author S.G.
 *
 * Started 2005.07.20
 * 
 */

public class repZaduzenjeKPR implements raReportData {
  
  _Main main;
  repMemo rpm = repMemo.getrepMemo();
  UpPregledZaduzenjeKPR upzkpr = UpPregledZaduzenjeKPR.getInstance();
  DataSet ds;
  dM dm = dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  Valid val = Valid.getValid();
  
  public repZaduzenjeKPR(){
    ds = upzkpr.getReportSet();
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

  public String getCART() {
    String izlaz = Aut.getAut().getIzlazCARTdep(ds);
    if (hr.restart.sisfun.frmParam.getParam("robno","chForSpecial","N","Provjerava specijalne znakove (*,_,+,-) i odrezuje njih plus sve iza njih").equalsIgnoreCase("D")){
      if (Aut.getAut().getIzlazCART().equalsIgnoreCase("BC") && (izlaz.indexOf("*") > 0 || izlaz.indexOf("_") > 0 || izlaz.indexOf("+") > 0 || izlaz.indexOf("-") > 0)){
        
        int index = 13;
        
        if (izlaz.indexOf("*") > 0) index = izlaz.indexOf("*");
        if (izlaz.indexOf("_") > 0) index = izlaz.indexOf("_");
        if (izlaz.indexOf("+") > 0) index = izlaz.indexOf("+");
        if (izlaz.indexOf("-") > 0) index = izlaz.indexOf("-");
        
        izlaz = izlaz.substring(0,index);
      }
    }
    return izlaz;
  }

  public short getRBR() {
    return ds.getShort("RBR");
  }
  
  public String getCartTitle(){
    return Aut.getAut().getCARTdependable("Šifra","Naziv","Barcode");
  }

  public Timestamp getDATDOK() {
    return ds.getTimestamp("DATDOK");
  }
  
  public String SgetDATDOK() {
    return rdu.dataFormatter(getDATDOK());
  }
  
  public long getSortDatum(){
   return ds.getTimestamp("DATDOK").getTime()/10000000;  
  }
  
  public String getNAZART(){
   return ds.getString("NAZART"); 
  }

  public String getJM() {
    return ds.getString("JM");
  }

  public BigDecimal getKOL() {
    return ds.getBigDecimal("KOL");
  }
  
  public double getIPRODSP() {
    return ds.getBigDecimal("IPRODSP").doubleValue();
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
