/****license*****************************************************************
**   file: repIDD.java
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
import hr.restart.util.sysoutTEST;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repIDD implements raReportData { //sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  frmID frID = frmID.getInstance();
  DataSet ds = frID.getRepSet();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repIDD() {
    sysoutTEST st = new sysoutTEST(false);
    st.prn(ds);
    ru.setDataSet(ds);
  }
/*
  public repIDD(int idx) {
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

      return new repIDD(indx++);
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

  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday())+".";
  }

  public String getPODNOSITELJ_IZVJESCA(){
    return frID.getKnjNaziv();
  }

  public String getADRESA_PODNOSITELJA(){
    return frID.getKnjAdresa() + ", " + frID.getKnjHpBroj() + " " + frID.getKnjMjesto();
  }

  public String getMB_JMBG_PODNOSITELJA(){
    return frID.getKnjMatbroj();
  }
  
  //toèka 2.
  
  
  public int getI2(){
    //return ds.getInt("II_1");
    return frID.wrapVROint("II_1",2);
  }
  
  public BigDecimal getII2(){
    //return ds.getBigDecimal("II_2");
    return frID.wrapVRObd("II_2",2);
  }
  
  public BigDecimal getIII2(){
    return frID.wrapVRObd("III_5", 3);
  }
  
  public BigDecimal getIV2(){
    return frID.getOsnovicaZaDoprinoseIDD(2);
  }
  
  public BigDecimal getV2(){
    //return ds.getBigDecimal("II_2");
    return zbroji(getV12(),getV22(),getV32(),getV42(),getV52(),getV62());
  }
  
  public BigDecimal getVI2(){
    //return ds.getBigDecimal("III_1_1");
    return frID.wrapVRObd("III_2",2);
  }
  
  public BigDecimal getVII2(){
    //return ds.getBigDecimal("III_1_2");
    return porOsnovica(2);
  }
  
  public BigDecimal getVIII2(){
    //return ds.getBigDecimal("III_4");
    return frID.wrapVRObd("III_7_1",2);
  }
  
  public BigDecimal getIX2(){
    //return ds.getBigDecimal("III_7_1").add(ds.getBigDecimal("III_7_2"));
    return frID.wrapVRObd("III_7_2",2);
  }
  
  public BigDecimal getX2(){
    //return ds.getBigDecimal("III_7_1");
    return zbroji(getVIII2(),getIX2(),null,null,null,null);
  }
  
  public BigDecimal getXI2(){
    //return ds.getBigDecimal("III_7_2");
    return frID.wrapVRObd("III_7_2",2);
  }
  
  public BigDecimal getXII2(){
    //return ds.getBigDecimal("III_1_3");
    return frID.wrapVRObd("III_1_3",2);
  }
  
  //toèka3. 25%
  
/*  
  public BigDecimal getI3(){
    return null;
  }
  
  public BigDecimal getII3(){
    return null;
  }
  
  public BigDecimal getIII3(){
    return null;
  }
  
  public BigDecimal getIV3(){
    return null;
  }
  
  public BigDecimal getV3(){
    return null;
  }
  
  public BigDecimal getVI3(){
    return null;
  }
  
  public BigDecimal getVII3(){
    return null;
  }
  
  public BigDecimal getVIII3(){
    return null;
  }
  
  public BigDecimal getIX3(){
    return null;
  }
  
  public BigDecimal getX3(){
    return null;
  }
  
  public BigDecimal getXI3(){
    return null;
  }
  
  public BigDecimal getXII3(){
    return null;
  }
*/
  public int getI3(){
    //return ds.getInt("II_1");
    return frID.wrapVROint("II_1",3);
  }
  
  public BigDecimal getII3(){
    //return ds.getBigDecimal("II_2");
    return frID.wrapVRObd("II_2",3);
  }
  
  public BigDecimal getIII3(){
    return frID.wrapVRObd("III_5", 3);
  }
  
  public BigDecimal getIV3(){
    return frID.getOsnovicaZaDoprinoseIDD(3);
  }
  
  public BigDecimal getV3(){
    //return ds.getBigDecimal("II_2");
    return zbroji(getV13(),getV23(),getV33(),getV43(),getV53(),getV63());
  }
  
  public BigDecimal getVI3(){
    //return ds.getBigDecimal("III_1_1");
    return frID.wrapVRObd("III_2",3);
  }
  
  public BigDecimal getVII3(){
    //return ds.getBigDecimal("III_1_2");
    return porOsnovica(3);
  }
  
  public BigDecimal getVI_II_3(){
    try {
      return frID.wrapVRObd("III_1_1",3).add(frID.wrapVRObd("III_1_2",3));
    } catch (NullPointerException e) {
      return null;
    }
  }
  
  public BigDecimal getVI_II_9(){
    try {
      return frID.wrapVRObd("III_1_1",3).add(frID.wrapVRObd("III_1_2",3));
    } catch (NullPointerException e) {
      return null;
    }
  }
  
  public BigDecimal getVIII3(){
    //return ds.getBigDecimal("III_4");
    return frID.wrapVRObd("III_7_1",3);
  }
  
  public BigDecimal getIX3(){
    //return ds.getBigDecimal("III_7_1").add(ds.getBigDecimal("III_7_2"));
    return frID.wrapVRObd("III_7_2",3);
  }
  
  public BigDecimal getX3(){
    //return ds.getBigDecimal("III_7_1");
    return zbroji(getVIII3(),getIX3(),null,null,null,null);  }
  
  public BigDecimal getXI3(){
    //return ds.getBigDecimal("III_7_2");
    return frID.wrapVRObd("III_7_2",3);
  }
  
  public BigDecimal getXII3(){
    //return ds.getBigDecimal("III_1_3");
    return frID.wrapVRObd("III_1_3",3);
  }  
  //toèka3. 40% tuzemni
  
  /*
  public BigDecimal getI4(){
    return null;
  }
  
  public BigDecimal getII4(){
    return null;
  }
  
  public BigDecimal getIII4(){
    return null;
  }
  
  public BigDecimal getIV4(){
    return null;
  }
  
  public BigDecimal getV4(){
    return null;
  }
  
  public BigDecimal getVI4(){
    return null;
  }
  
  public BigDecimal getVII4(){
    return null;
  }
  
  public BigDecimal getVIII4(){
    return null;
  }
  
  public BigDecimal getIX4(){
    return null;
  }
  
  public BigDecimal getX4(){
    return null;
  }
  
  public BigDecimal getXI4(){
    return null;
  }
  
  public BigDecimal getXII4(){
    return null;
  }
  */
  public int getI4(){
    //return ds.getInt("II_1");
    return frID.wrapVROint("II_1",4);
  }
  
  public BigDecimal getII4(){
    //return ds.getBigDecimal("II_2");
    return frID.wrapVRObd("II_2",4);
  }
  
  public BigDecimal getIII4(){
    return frID.wrapVRObd("III_5", 4);
  }
  
  public BigDecimal getIV4(){
    return frID.getOsnovicaZaDoprinoseIDD(4);
  }
  
  public BigDecimal getV4(){
    //return ds.getBigDecimal("II_2");
    return zbroji(getV14(),getV24(),getV34(),getV44(),getV54(),getV64());
  }
  
  public BigDecimal getVI4(){
    //return ds.getBigDecimal("III_1_1");
    return frID.wrapVRObd("III_2",4);
  }
  
  public BigDecimal getVII4(){
    //return ds.getBigDecimal("III_1_2");
    return porOsnovica(4);
  }
  
  public BigDecimal getVIII4(){
    //return ds.getBigDecimal("III_4");
    return frID.wrapVRObd("III_7_1",4);
  }
  
  public BigDecimal getIX4(){
    //return ds.getBigDecimal("III_7_1").add(ds.getBigDecimal("III_7_2"));
    return frID.wrapVRObd("III_7_2",4);

  }
  
  public BigDecimal getX4(){
    //return ds.getBigDecimal("III_7_1");
    return zbroji(getVIII4(),getIX4(),null,null,null,null);  }
  
  public BigDecimal getXI4(){
    //return ds.getBigDecimal("III_7_2");
    return frID.wrapVRObd("III_7_2",4);
  }
  
  public BigDecimal getXII4(){
    //return ds.getBigDecimal("III_1_3");
    return frID.wrapVRObd("III_1_3",4);
  }
  //toèka 5. 40% inozemni
  
  /*
  public BigDecimal getI5(){
    return null;
  }
  
  public BigDecimal getII5(){
    return null;
  }
  
  public BigDecimal getIII5(){
    return null;
  }
  
  public BigDecimal getIV5(){
    return null;
  }
  
  public BigDecimal getV5(){
    return null;
  }
  
  public BigDecimal getVI5(){
    return null;
  }
  
  public BigDecimal getVII5(){
    return null;
  }
  
  public BigDecimal getVIII5(){
    return null;
  }
  
  public BigDecimal getIX5(){
    return null;
  }
  
  public BigDecimal getX5(){
    return null;
  }
  
  public BigDecimal getXI5(){
    return null;
  }
  
  public BigDecimal getXII5(){
    return null;
  }
  */
  public int getI5(){
    //return ds.getInt("II_1");
    return frID.wrapVROint("II_1",5);
  }
  
  public BigDecimal getII5(){
    //return ds.getBigDecimal("II_2");
    return frID.wrapVRObd("II_2",5);
  }
  
  public BigDecimal getIII5(){
    return frID.wrapVRObd("III_5", 5);
  }
  
  public BigDecimal getIV5(){
    return frID.getOsnovicaZaDoprinoseIDD(5);
  }
  
  public BigDecimal getV5(){
    //return ds.getBigDecimal("II_2");
    return zbroji(getV15(),getV25(),getV35(),getV45(),getV55(),getV65());
  }
  
  public BigDecimal getVI5(){
    //return ds.getBigDecimal("III_1_1");
    return frID.wrapVRObd("III_2",5);
  }
  
  public BigDecimal getVII5(){
    //return ds.getBigDecimal("III_1_2");
    return porOsnovica(5);
  }
  
  public BigDecimal getVIII5(){
    //return ds.getBigDecimal("III_4");
    return frID.wrapVRObd("III_7_1",5);
  }
  
  public BigDecimal getIX5(){
    //return ds.getBigDecimal("III_7_1").add(ds.getBigDecimal("III_7_2"));
    return frID.wrapVRObd("III_7_2",5);
  }
  
  public BigDecimal getX5(){
    //return ds.getBigDecimal("III_7_1");
    return zbroji(getVIII5(),getIX5(),null,null,null,null);  }
  
  public BigDecimal getXI5(){
    //return ds.getBigDecimal("III_7_2");
    return frID.wrapVRObd("III_7_2",5);
  }
  
  public BigDecimal getXII5(){
    //return ds.getBigDecimal("III_1_3");
    return frID.wrapVRObd("III_1_3",5);
  }
  // toèka 6. 25% + 40%
  
  /*
  public BigDecimal getI6(){
    return null;
  }
  
  public BigDecimal getII6(){
    return null;
  }
  
  public BigDecimal getIII6(){
    return null;
  }
  
  public BigDecimal getIV6(){
    return null;
  }
  
  public BigDecimal getV6(){
    return null;
  }
  
  public BigDecimal getVI6(){
    return null;
  }
  
  public BigDecimal getVII6(){
    return null;
  }
  
  public BigDecimal getVIII6(){
    return null;
  }
  
  public BigDecimal getIX6(){
    return null;
  }
  
  public BigDecimal getX6(){
    return null;
  }
  
  public BigDecimal getXI6(){
    return null;
  }
  
  public BigDecimal getXII6(){
    return null;
  }
  */
  public int getI6(){
    //return ds.getInt("II_1");
    return frID.wrapVROint("II_1",6);
  }
  
  public BigDecimal getII6(){
    //return ds.getBigDecimal("II_2");
    return frID.wrapVRObd("II_2",6);
  }
  
  public BigDecimal getIII6(){
    return frID.wrapVRObd("III_5", 6);
  }
  
  public BigDecimal getIV6(){
    return frID.getOsnovicaZaDoprinoseIDD(6);
  }
  
  public BigDecimal getV6(){
    //return ds.getBigDecimal("II_2");
    return zbroji(getV16(),getV26(),getV36(),getV46(),getV56(),getV66());
  }
  
  public BigDecimal getVI6(){
    //return ds.getBigDecimal("III_1_1");
    return frID.wrapVRObd("III_2",6);
  }
  
  public BigDecimal getVII6(){
    //return ds.getBigDecimal("III_1_2");
    return porOsnovica(6);
  }
  
  public BigDecimal getVIII6(){
    //return ds.getBigDecimal("III_4");
    return frID.wrapVRObd("III_7_1",6);
  }
  
  public BigDecimal getIX6(){
    //return ds.getBigDecimal("III_7_1").add(ds.getBigDecimal("III_7_2"));
    return frID.wrapVRObd("III_7_2",6);

  }
  
  public BigDecimal getX6(){
    //return ds.getBigDecimal("III_7_1");
    return zbroji(getVIII6(),getIX6(),null,null,null,null);  }
  
  public BigDecimal getXI6(){
    //return ds.getBigDecimal("III_7_2");
    return frID.wrapVRObd("III_7_2",6);
  }
  
  public BigDecimal getXII6(){
    //return ds.getBigDecimal("III_1_3");
    return frID.wrapVRObd("III_1_3",6);
  }
  //Primitak do 1500,00
  
  /*
  public BigDecimal getI7(){
    return null;
  }
  
  public BigDecimal getII7(){
    return null;
  }
  
  public BigDecimal getIII7(){
    return null;
  }
  
  public BigDecimal getIV7(){
    return null;
  }
  
  public BigDecimal getV7(){
    return null;
  }
  
  public BigDecimal getVI7(){
    return null;
  }
  
  public BigDecimal getVII7(){
    return null;
  }
  
  public BigDecimal getVIII7(){
    return null;
  }
  
  public BigDecimal getIX7(){
    return null;
  }
  
  public BigDecimal getX7(){
    return null;
  }
  
  public BigDecimal getXI7(){
    return null;
  }
  
  public BigDecimal getXII7(){
    return null;
  }
  */
  public int getI7(){
    //return ds.getInt("II_1");
    return frID.wrapVROint("II_1",7);
  }
  
  public BigDecimal getII7(){
    //return ds.getBigDecimal("II_2");
    return frID.wrapVRObd("II_2",7);
  }
  
  public BigDecimal getIII7(){
    return frID.wrapVRObd("III_5", 7);
  }
  
  public BigDecimal getIV7(){
    return frID.getOsnovicaZaDoprinoseIDD(2);
  }
  
  public BigDecimal getV7(){
    //return ds.getBigDecimal("II_2");
    return zbroji(getV17(),getV27(),getV37(),getV47(),getV57(),getV67());
  }
  
  public BigDecimal getVI7(){
    //return ds.getBigDecimal("III_1_1");
    return frID.wrapVRObd("III_2",7);
  }
  
  public BigDecimal getVII7(){
    //return ds.getBigDecimal("III_1_2");
    return porOsnovica(7);
  }
  
  public BigDecimal getVIII7(){
    //return ds.getBigDecimal("III_4");
    return frID.wrapVRObd("III_7_1",7);
  }
  
  public BigDecimal getIX7(){
    //return ds.getBigDecimal("III_7_1").add(ds.getBigDecimal("III_7_2"));
    return frID.wrapVRObd("III_7_2",7);
  }
  
  public BigDecimal getX7(){
    //return ds.getBigDecimal("III_7_1");
    return zbroji(getVIII7(),getIX7(),null,null,null,null);
  }
  
  public BigDecimal getXI7(){
    //return ds.getBigDecimal("III_7_2");
    return frID.wrapVRObd("III_7_2",7);
  }
  
  public BigDecimal getXII7(){
    //return ds.getBigDecimal("III_1_3");
    return frID.wrapVRObd("III_1_3",7);
  }
  //Primitak iznad 1500,00
  
  /*
  public BigDecimal getI8(){
    return null;
  }
  
  public BigDecimal getII8(){
    return null;
  }
  
  public BigDecimal getIII8(){
    return null;
  }
  
  public BigDecimal getIV8(){
    return null;
  }
  
  public BigDecimal getV8(){
    return null;
  }
  
  public BigDecimal getVI8(){
    return null;
  }
  
  public BigDecimal getVII8(){
    return null;
  }
  
  public BigDecimal getVIII8(){
    return null;
  }
  
  public BigDecimal getIX8(){
    return null;
  }
  
  public BigDecimal getX8(){
    return null;
  }
  
  public BigDecimal getXI8(){
    return null;
  }
  
  public BigDecimal getXII8(){
    return null;
  }
  */
  public int getI8(){
    //return ds.getInt("II_1");
    return frID.wrapVROint("II_1",8);
  }
  
  public BigDecimal getII8(){
    //return ds.getBigDecimal("II_2");
    return frID.wrapVRObd("II_2",8);
  }
  
  public BigDecimal getIII8(){
    return frID.wrapVRObd("III_5", 8);
  }
  
  public BigDecimal getIV8(){
    return frID.getOsnovicaZaDoprinoseIDD(2);
  }
  
  public BigDecimal getV8(){
    //return ds.getBigDecimal("II_2");
    return zbroji(getV18(),getV28(),getV38(),getV48(),getV58(),getV68());
  }
  
  public BigDecimal getVI8(){
    //return ds.getBigDecimal("III_1_1");
    return frID.wrapVRObd("III_2",8);
  }
  
  public BigDecimal getVII8(){
    //return ds.getBigDecimal("III_1_2");
    return porOsnovica(8);
  }
  
  public BigDecimal getVIII8(){
    //return ds.getBigDecimal("III_4");
    return frID.wrapVRObd("III_7_1",8);
  }
  
  public BigDecimal getIX8(){
    //return ds.getBigDecimal("III_7_1").add(ds.getBigDecimal("III_7_2"));
    return frID.wrapVRObd("III_7_2",8);

  }
  
  public BigDecimal getX8(){
    //return ds.getBigDecimal("III_7_1");
    return zbroji(getVIII8(),getIX8(),null,null,null,null);
  }
  
  public BigDecimal getXI8(){
    //return ds.getBigDecimal("III_7_2");
    return frID.wrapVRObd("III_7_2",8);
  }
  
  public BigDecimal getXII8(){
    //return ds.getBigDecimal("III_1_3");
    return frID.wrapVRObd("III_1_3",8);
  }
  //UKUPNO
  
  
  public int getI9(){
    int I9 = ds.getInt("II_1");
    return I9;
  }
  
  public BigDecimal getII9(){
    BigDecimal II9 = ds.getBigDecimal("II_2");
    return II9;
  }
  
  public BigDecimal getIII9(){
    BigDecimal III9 = ds.getBigDecimal("III_5");
    return III9;
  }
  
  public BigDecimal getIV9(){
    BigDecimal dop = ds.getBigDecimal("III_2");
    return (dop==null||dop.signum()==0)?null:ds.getBigDecimal("II_2");
  }
  
  public BigDecimal getV9(){
    return zbroji(getV19(),getV29(),getV39(),getV49(),getV59(),getV69());
  }
  
  public BigDecimal getVI9(){
    BigDecimal VI9 = ds.getBigDecimal("III_2");
    return VI9;
  }
  
  public BigDecimal getVII9(){
    return porOsnovica(9);
  }
  
  public BigDecimal getVIII9(){
    BigDecimal VIII9 = ds.getBigDecimal("III_7_1");
    return VIII9;
  }
  
  public BigDecimal getIX9(){
    BigDecimal IX9 = ds.getBigDecimal("III_7_2");
    return IX9;
  }
  
  public BigDecimal getX9(){
    BigDecimal X9 = ds.getBigDecimal("III_7_1").add(ds.getBigDecimal("III_7_2"));
    return X9;
  }
  
  public BigDecimal getXI9(){
    BigDecimal XI9 = ds.getBigDecimal("III_7_2");
    return XI9;
  }
  
  public BigDecimal getXII9(){
    BigDecimal XII9 = ds.getBigDecimal("III_1_3");
    return XII9;
  }

  public double getV_3_7(){
    return frID.getZZuInozemstvu().doubleValue();
  }
  
  public BigDecimal getV12() {
    return frID.wrapVRObd("III_1_1", 2);
  }
  public BigDecimal getV13() {
    return frID.wrapVRObd("III_1_1", 3);
  }
  public BigDecimal getV14() {
    return frID.wrapVRObd("III_1_1", 4);
  }
  public BigDecimal getV15() {
    return frID.wrapVRObd("III_1_1", 5);
  }
  public BigDecimal getV16() {
    return frID.wrapVRObd("III_1_1", 6);
  }
  public BigDecimal getV17() {
    return frID.wrapVRObd("III_1_1", 7);
  }
  public BigDecimal getV18() {
    return frID.wrapVRObd("III_1_1", 8);
  }
  public BigDecimal getV19() {
    return ds.getBigDecimal("III_1_1");
  }
  
  public BigDecimal getV22() {
    return null;
  }
  public BigDecimal getV23() {
    return null;
  }
  public BigDecimal getV24() {
    return null;
  }
  public BigDecimal getV25() {
    return null;
  }
  public BigDecimal getV26() {
    return null;
  }
  public BigDecimal getV27() {
    return null;
  }
  public BigDecimal getV28() {
    return null;
  }
  public BigDecimal getV29() {
    return null;
  }
  
  //---------------------------
  public BigDecimal getV32() {
    return frID.wrapVRObd("III_1_2", 2);
  }
  public BigDecimal getV33() {
    return frID.wrapVRObd("III_1_2", 3);
  }
  public BigDecimal getV34() {
    return frID.wrapVRObd("III_1_2", 4);
  }
  public BigDecimal getV35() {
    return frID.wrapVRObd("III_1_2", 5);
  }
  public BigDecimal getV36() {
    return frID.wrapVRObd("III_1_2", 6);
  }
  public BigDecimal getV37() {
    return frID.wrapVRObd("III_1_2", 7);
  }
  public BigDecimal getV38() {
    return frID.wrapVRObd("III_1_2", 8);
  }
  public BigDecimal getV39() {
    return ds.getBigDecimal("III_1_2");
  }
  
  //---------------------------
  public BigDecimal getV42() {
    return frID.wrapVRObd("III_1_3", 2);
  }
  public BigDecimal getV43() {
    return frID.wrapVRObd("III_1_3", 3);
  }
  public BigDecimal getV44() {
    return frID.wrapVRObd("III_1_3", 4);
  }
  public BigDecimal getV45() {
    return frID.wrapVRObd("III_1_3", 5);
  }
  public BigDecimal getV46() {
    return frID.wrapVRObd("III_1_3", 6);
  }
  public BigDecimal getV47() {
    return frID.wrapVRObd("III_1_3", 7);
  }
  public BigDecimal getV48() {
    return frID.wrapVRObd("III_1_3", 8);
  }
  public BigDecimal getV49() {
    return ds.getBigDecimal("III_1_3");
  }
  
  //********************************
  public BigDecimal getV52() {
    return null;
  }
  public BigDecimal getV53() {
    return null;
  }
  public BigDecimal getV54() {
    return null;
  }
  public BigDecimal getV55() {
    return null;
  }
  public BigDecimal getV56() {
    return null;
  }
  public BigDecimal getV57() {
    return null;
  }
  public BigDecimal getV58() {
    return null;
  }
  public BigDecimal getV59() {
    return null;
  }
  
  //********************************
  public BigDecimal getV62() {
    return frID.getZZuInoIDD(2);
  }
  public BigDecimal getV63() {
    return frID.getZZuInoIDD(3);
  }
  public BigDecimal getV64() {
    return frID.getZZuInoIDD(4);
  }
  public BigDecimal getV65() {
    return frID.getZZuInoIDD(5);
  }
  public BigDecimal getV66() {
    return frID.getZZuInoIDD(6);
  }
  public BigDecimal getV67() {
    return frID.getZZuInoIDD(7);
  }
  public BigDecimal getV68() {
    return frID.getZZuInoIDD(8);
  }
  public BigDecimal getV69() {
    return frID.getZZuInozemstvu();
  }
 
  
  //helper
  private BigDecimal zbroji(BigDecimal b1, 
                            BigDecimal b2, 
                            BigDecimal b3, 
                            BigDecimal b4, 
                            BigDecimal b5, 
                            BigDecimal b6) {
    BigDecimal bsum = ((b1==null)?Aus.zero2:b1)
       .add((b2==null)?Aus.zero2:b2)
       .add((b3==null)?Aus.zero2:b3)
       .add((b4==null)?Aus.zero2:b4)
       .add((b5==null)?Aus.zero2:b5)
       .add((b6==null)?Aus.zero2:b6);
    return bsum.signum()==0?null:bsum;
  }

  private BigDecimal porOsnovica(int stupac) {//2 - 3 - 6
    BigDecimal II = stupac==9?ds.getBigDecimal("II_2"):frID.wrapVRObd("II_2",stupac);
    BigDecimal III = stupac==9?ds.getBigDecimal("III_5"):frID.wrapVRObd("III_5", stupac);
    BigDecimal VI = stupac==9?ds.getBigDecimal("III_2"):frID.wrapVRObd("III_2", stupac);
    BigDecimal bsum = ((II==null)?Aus.zero2:II)
      .add(((III==null)?Aus.zero2:III).negate())
      .add(((VI==null)?Aus.zero2:VI).negate());
    return bsum.signum()==0?null:bsum;
  }

}




/*

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
//   return ds.getBigDecimal("III_4").subtract(getIII_3());
  return ds.getBigDecimal("III_4");
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
}*/