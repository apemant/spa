/****license*****************************************************************
**   file: raIzvjPDV.java
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
package hr.restart.sk;

import hr.restart.util.Util;
import hr.restart.util.lookupData;

import java.math.BigDecimal;
import java.util.HashSet;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class raIzvjPDV {

  Util ut = Util.getUtil();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  static raIzvjPDV rip;

  StorageDataSet PDVSet;
  HashSet CKNJIGEset, CKOLONEset, URAIRAset;

  public raIzvjPDV() {
    rip = this;
  }

  public static raIzvjPDV getInstance(){
    if (rip == null) rip = new raIzvjPDV();
    return rip;
  }

  final String SQL = "SELECT * FROM StIzvjPDV WHERE ciz='";

  private void createPDVSet() {
    PDVSet = new StorageDataSet();
    QueryDataSet izvjpdv = dm.getIzvjPDV();
    int i=0;
    izvjpdv.open();
    izvjpdv.first();
    do {

      PDVSet.addColumn(dm.createBigDecimalColumn("COL"+(i++),izvjpdv.getString("CIZ"),2));
//      System.out.println("COL"+(i-1)+" = "+izvjpdv.getString("CIZ"));
    } while (izvjpdv.next());
    PDVSet.open();
    PDVSet.insertRow(false);
  }

  public StorageDataSet napuniPDVSet(QueryDataSet stafkeUI) {
    if (PDVSet == null) {
      createPDVSet();
    } else {
      PDVSet.first();
      for (int i = 0; i < PDVSet.getColumnCount(); i++) {
        PDVSet.setBigDecimal(i,new java.math.BigDecimal("0.00"));
      }
    }
    stafkeUI.first();
    do {
////debug:
//ReadRow row = stafkeUI;
//System.out.println("TRAZIM za "+row.getString("CKNJIGE")+" :: "+row.getShort("CKOLONE")+" :: "+row.getString("URAIRA"));
      for (int i = 0; i < PDVSet.getColumnCount(); i++) {
        pribroji(PDVSet.getColumn(i),stafkeUI);
      }
    } while (stafkeUI.next());
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(PDVSet);
    return PDVSet;
  }

  private void pribroji(Column col, ReadRow row) {
    if (validate(col.getCaption(),row)) {
//      System.out.println(col.getCaption() + " = "+col.getDataSet().getBigDecimal(col.getColumnName())+" + "+row.getBigDecimal("ID").add(row.getBigDecimal("IP")));
      BigDecimal val = row.getBigDecimal("ID").subtract(row.getBigDecimal("IP"));
      if (row.getString("URAIRA").equals("I")) val = val.negate();
      /*if (row.getBigDecimal("SSALDO").signum() < 0) val = val.negate();*/
      col.getDataSet().setBigDecimal(col.getColumnName(),
        col.getDataSet().getBigDecimal(col.getColumnName()).add(val));
      
    }
  }
  private QueryDataSet tmp_stizvjPDV;
  private QueryDataSet getStIzvjPDV() {
    if (tmp_stizvjPDV == null) tmp_stizvjPDV = hr.restart.baza.StIzvjPDV.getDataModule().copyDataSet();
    return tmp_stizvjPDV;
  }
  public boolean validate(String CIZ, ReadRow row/*, DataSet stPDV*/){
    boolean retVal;
    try {
      retVal = lookupData.getlookupData().raLocate(getStIzvjPDV(),
          new String [] {"CIZ","CKNJIGE","CKOLONE","URAIRA"},
          new String [] {CIZ,row.getString("CKNJIGE"),row.getShort("CKOLONE")+"",row.getString("URAIRA")});
    }
    catch (Exception ex) {
      ex.printStackTrace();
      retVal = false;
    }
//debug:
/*    if (retVal) {
      System.out.println("NASAO za "+CIZ+" :: "+row.getString("CKNJIGE")+" :: "+row.getShort("CKOLONE")+" :: "+row.getString("URAIRA"));
    }*/
    return retVal;
  }

  public String getQueryString(String tablica, String CIZ) {
    String tabl;
    if (tablica.equals("")) tabl = "";
    else tabl = tablica + ".";
    QueryDataSet pdv = ut.getNewQueryDataSet(SQL + CIZ + "'");
    if (pdv.isEmpty()) return "";
    setHashes(pdv);
    Object [] cknj = CKNJIGEset.toArray();
    Object [] ckol = CKOLONEset.toArray();
    Object [] urir = URAIRAset.toArray();

    String condition = "(" + tabl + "cknjige " + formatValues(cknj) + "and " + tabl + "ckolone " + formatValues(ckol) + "and " + tabl + "uraira " + formatValues(urir) + ")";

    return condition;
  }

  public String getQueryString(String CIZ) {
    return getQueryString("",CIZ);
  }

  void setHashes(DataSet ipdv){
    CKNJIGEset = new HashSet();
    CKOLONEset = new HashSet();
    URAIRAset = new HashSet();
    ipdv.first();
    do{
      CKNJIGEset.add(ipdv.getString("CKNJIGE"));
      CKOLONEset.add(String.valueOf(ipdv.getShort("CKOLONE")));
      URAIRAset.add(ipdv.getString("URAIRA"));
    } while (ipdv.next());
  }

  String formatValues(Object[] erej){
    String vrati="in (";
    if (erej.length == 1) return "= '" + erej[0].toString() + "' ";
    for (int i=0 ; i<erej.length ; i++) {
      if (i != (erej.length-1)) vrati = vrati + "'" + erej[i].toString() + "', ";
      else vrati = vrati + "'" + erej[i].toString() + "') ";
    }
    return vrati;
  }
}