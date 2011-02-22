/****license*****************************************************************
**   file: repID_B.java
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
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;

public class repID_B implements raReportData { //sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  frmID frID = frmID.getInstance();
  DataSet ds = frID.getRepSetStrB();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repID_B() {
//    System.out.println("aj em alive!!!!");
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(ds);
    ru.setDataSet(ds);
  }

  private static String copcine;
/*
  public repID_B(int idx) {
//    if (idx == 0){
//      sysoutTEST syst = new sysoutTEST(false);
//      syst.prn(ds);
//    }
    ds.goToRow(idx);
    copcine = getCOPCINE();
  }


  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
    {
      ds.open();
    }
    int indx=0;
    public Object nextElement() {

      return new repID_B(indx++);
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
    copcine = getCOPCINE();
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ru.setDataSet(null);
    ds = null;
  }

  String getCOPCINE(){
    VarStr invert= new VarStr();
//    System.out.println("inicijaliziran invert");
    VarStr original = new VarStr(raIzvjestaji.convertCopcineToRS(ds.getString("COPCINE")));
//    System.out.println("orginal : " + original.toString());
    original.trim();
//    System.out.println("inicijaliziran orginal - ulazim u pelju");
    for (int i=original.length()-1; i >=0  ; i-- ) {
//      System.out.println("petlja - " + i);
      try {
        invert.append(original.charAt(i));
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }

//    System.out.println("invert.toString() "+invert.toString());

    return invert.toString();
  }

  public double getPOREZ(){
     return ds.getBigDecimal("PORUK").doubleValue();
  }

  public double getPRIREZ(){
     return ds.getBigDecimal("PRIR").doubleValue();
  }

  public double getUKUPNO(){
     return ds.getBigDecimal("PORIPRIR").doubleValue();
  }

  public String getCOPCINE_Z1(){
    try {
      return getCOPCINE().substring(3,4);
    }
    catch (Exception ex) {
      return " ";
    }
  }

  public String getNAZIVOPCINE(){
    lookupData.getlookupData().raLocate(dm.getOpcine(),"COPCINE",ds.getString("COPCINE"));
    return dm.getOpcine().getString("NAZIVOP");
  }


  public String getCOPCINE_Z2(){
    try {
      return getCOPCINE().substring(2,3);
    }
    catch (Exception ex) {
      return " ";
    }
  }

  public String getCOPCINE_Z3(){
    try {
      return getCOPCINE().substring(1,2);
    }
    catch (Exception ex) {
      return " ";
    }
  }

  public String getCOPCINE_Z4(){
    try {
      return getCOPCINE().substring(0,1);
    }
    catch (Exception ex) {
      return " ";
    }
  }
  public String getPORBROJ() {
    return frID.getKnjMatbroj();
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
}