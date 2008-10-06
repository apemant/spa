/****license*****************************************************************
**   file: repNalog.java
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
package hr.restart.gk;

import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;

public class repNalog implements raReportData {
 hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
 String[] colname = new String[]{""};
 hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
 DataSet ds = frmNalozi.getRepDetailSet();

 hr.restart.robno._Main main;
 hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
 hr.restart.robno.repMemo rpm = hr.restart.robno.repMemo.getrepMemo();
 hr.restart.robno.repUtil ru = hr.restart.robno.repUtil.getrepUtil();
 hr.restart.util.Valid val = hr.restart.util.Valid.getValid();

 public repNalog() {
   ru.setDataSet(ds);
 }

 public raReportData getRow(int idx) {
   ds.goToRow(idx);
   return this;
 }
 
 public int getRowCount() {
   return ds.rowCount();
 }

 public void close() {}

 public String getBrojKonta() {
  return ds.getString("BROJKONTA");
 }

 public String getCNaloga() {
  return frmNalozi.getRepMasterSet().getString("CNALOGA");
 }

 public String getCOrg() {
  return ds.getString("CORG");
 }

 public String getCaption1() {
  String s1 = frmNalozi.getRepMasterSet().getString("CNALOGA");
  String s2 = rdu.dataFormatter(ds.getTimestamp("DATUMKNJ"));
  return "Br. " + s1 + " na dan " + s2;
 }

 /*public java.util.Enumeration getData() {
  return
   new java.util.Enumeration() {
    {
     ds.open();
    }
    int indx = 0;
    public boolean hasMoreElements() {
     return (indx < ds.getRowCount());
    }
    public Object nextElement() {

     return new repNalog(indx++);
    }
   };
 }*/

 public String getDatumIsp() {
  return rdu.dataFormatter(val.getToday());
 }

 public String getFake() {
  return "a";
 }

 public double getID() {
  return ds.getBigDecimal("ID").doubleValue();
 }

 public double getIP() {
  return ds.getBigDecimal("IP").doubleValue();
 }

 public String getOpis() {
  return ds.getString("OPIS");
 }

 public int getRBS() {
  return ds.getInt("RBS");
 }

 public String getFirstLine() {
  return rpm.getFirstLine();
 }

 public String getSecondLine() {
  return rpm.getSecondLine();
 }

 public String getThirdLine() {
  return rpm.getThirdLine();
 }
}
