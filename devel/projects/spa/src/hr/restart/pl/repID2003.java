/****license*****************************************************************
**   file: repID2003.java
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
package hr.restart.pl;

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repID2003 implements raReportData { //sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  frmID frID = frmID.getInstance();
  DataSet ds = frID.getRepSet();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repID2003() {
//    System.out.println("frID " + frID);
    ru.setDataSet(ds);
  }
/*
  public repID2003(int idx) {
    if (idx == 0){
    }
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
    {
      ds.open();
    }
    int indx=0;
    public Object nextElement() {

      return new repID2003(indx++);
    }
    public boolean hasMoreElements() {
      return (indx < ds.getRowCount());
    }
    };
  }

  public void close() {
  }
*/
  

  
  public raReportData getRow(int i) {
    ds.goToRow(i);    
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ru.setDataSet(null);
    ds = null;
  }
  
  
  public String getMjesec(){
    return frID.getMjesecIzEkrana().concat(".");
  }

  public String getGodina(){
    return frID.getGodinaIzEkrana().concat(".");
  }
  
  public String getDOPTEXT() {
    return "te doprinosima za obvezna osiguranja u mjesecu " + 
      getMjesec() + " godine " + getGodina();
  }

  public String getI_1(){
    return frID.getKnjNaziv();
  }

  public String getI_2(){
    return frID.getKnjAdresa() + ", " + frID.getKnjHpBroj() + " " + frID.getKnjMjesto();
  }

  public String getI_3(){
    return frID.getKnjMatbroj();
  }

  public String getI_3a(){
    return frID.getKnjSifdjel();
  }

  public String getI_4(){
    return frID.getKnjZiro();
  }

  public int getII_1(){
    return ds.getInt("II_1");
  }

  public BigDecimal getII_2(){
    return ds.getBigDecimal("II_2");
  }

  public String getII_3(){
    return ds.getString("II_3");
  }

  public BigDecimal getIII_1(){
    return ds.getBigDecimal("III_1_1").add(ds.getBigDecimal("III_1_2")).add(ds.getBigDecimal("III_1_3A"));
  }

  public BigDecimal getIII_1A(){
    return ds.getBigDecimal("III_1_1").add(ds.getBigDecimal("III_1_2"));
  }

  public BigDecimal getIII_1_1(){
    return ds.getBigDecimal("III_1_1");
  }

  public BigDecimal getIII_1_2(){
    return ds.getBigDecimal("III_1_2");
  }

  public BigDecimal getIII_1_1_b(){
    return ds.getBigDecimal("III_1_1");
  }

  public BigDecimal getIII_1_2_b(){
    return ds.getBigDecimal("III_1_2");
  }

  public BigDecimal getIII_1_1_c(){
    return Aus.zero0;
  }

  public BigDecimal getIII_1_2_c(){
    return Aus.zero0;
  }

  public BigDecimal getIII_1_3(){
    return ds.getBigDecimal("III_1_3");
  }

  public BigDecimal getIII_1_3A(){
    return ds.getBigDecimal("III_1_3A");
  }

  public BigDecimal getIII_1_4(){
    return ds.getBigDecimal("III_1_4");
  }

  public BigDecimal getIII_2(){
    return ds.getBigDecimal("III_2");
  }

  public BigDecimal getIII_3(){
    return ds.getBigDecimal("III_3_1").add(ds.getBigDecimal("III_3_2").add(ds.getBigDecimal("III_3_3")));
  }

  public BigDecimal getIII_3_1(){
    return ds.getBigDecimal("III_3_1");
  }

  public BigDecimal getIII_3_2(){
    return ds.getBigDecimal("III_3_2");
  }

  public BigDecimal getIII_3_3(){
    return ds.getBigDecimal("III_3_3");
  }

  public BigDecimal getIII_4(){
    /** @todo ovo je neto... zar tu ne bi vec tribalo bit izracunato??? */
    return ds.getBigDecimal("III_4").subtract(getIII_3());
  }

  public BigDecimal getIII_5(){
    return ds.getBigDecimal("III_5");
  }

  public BigDecimal getIII_6(){
    return ds.getBigDecimal("III_6");
  }

  public BigDecimal getIII_7(){
    return ds.getBigDecimal("III_7_1").add(ds.getBigDecimal("III_7_2"));
  }

  public BigDecimal getIII_7_1(){
    return ds.getBigDecimal("III_7_1");
  }

  public BigDecimal getIII_7_2(){
    return ds.getBigDecimal("III_7_2");
  }

  public BigDecimal getV_3_3(){
    return Aus.zero0;
  }

  public BigDecimal getV_3_4(){
    return Aus.zero0;
  }

  public BigDecimal getV_3_6(){
    return ds.getBigDecimal("V_3_6");
  }

  public double getV_3_7(){
    return frID.getZZuInozemstvu().doubleValue();
  }

  public BigDecimal getV(){
    return (ds.getBigDecimal("V_3_6").add(ds.getBigDecimal("III_1_3")).add(ds.getBigDecimal("III_1_4")).add(frID.getZZuInozemstvu()));
  }
  
  public BigDecimal getNIII_1_III_3(){
   return getIII_1().add(getIII_3()); 
  }
  
  //TODO identifikator
  public String getIdentifikator(){
    return frID.getIdentifikator();
  }
  
  //TODO poseban doprinos za zapošljavanje osoba s invaliditetom
  public BigDecimal getZaZaposljavanjeInvaliditet(){
    return ds.getBigDecimal("III05_4_4_2");
  }

  public BigDecimal getRKP010(){
    return ds.getBigDecimal("RKP010");
  }

  public BigDecimal getRKP020(){
    return ds.getBigDecimal("RKP020");
  }

  public BigDecimal getRKP030(){
    return ds.getBigDecimal("RKP030");
  }

  public BigDecimal getRKP040(){
    return ds.getBigDecimal("RKP040");
  }

  public BigDecimal getRKP050(){
    return ds.getBigDecimal("RKP050");
  }

  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }
}