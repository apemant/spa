/****license*****************************************************************
**   file: repKPR.java
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
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;

//public class repKPR implements raReportData {//sg.com.elixir.reportwriter.datasource.IDataProvider {
public class repKPR implements raReportData {

  /** @todo for future use ovo ode gori :)) */

  _Main main;
  repMemo rpm = repMemo.getrepMemo();
  upKPR upk = upKPR.getInstance();
//  upKPR upK = upKPR.getInstance();
  DataSet ds = upk.getQds();
  dM dm = dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  Valid val = Valid.getValid();
  static int start;

  public repKPR() {
    ds.open();
    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
    syst.prn(ds);
    ru.setDataSet(ds);
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(ds);
  }
/*
  public repKPR(int idx) {
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {

    return new java.util.Enumeration() {
      int indx;
      {
        ds.open();
        ru.setDataSet(ds);

        if (ds.getShort("RBR") == 0){
          start = 1;
          indx = 1;
        } else {
          start = 0;
          indx = 0;
        }

      }

      public Object nextElement() {
        return new repKPR(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }
  public void close() {}
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

  public String getBRDOK(){
    return ds.getInt("BRDOK")+"";
  }

  public String getDATDOK(){
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }

  public String getVRDOK(){
    return ds.getString("VRDOK");
  }

  public double getSUMZAD(){
    return ds.getBigDecimal("SUMZAD").doubleValue();
  }

  public double getSUMRAZ(){
    return ds.getBigDecimal("SUMRAZ").doubleValue();
  }

   public String getKOLND(){
    return ds.getString("KOLND");
  }


  public int getFAKE(){
    return ((ds.getRow() - start)/44);
  }

  public String getCSKL(){
    return ds.getString("KOLSK");
  }

  public String getNSKL(){
    return ds.getString("KOLNSK");
  }

  public short getRBR(){
    return ds.getShort("RBR");
  }

  public double getDONOSZAD(){
    return upk.getDonosZad();
  }

  public double getDONOSRAZ(){
    return upk.getDonosRaz();
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

  public String getNSPRAVOSOB(){
    String nspo = "";
    if (lookupData.getlookupData().raLocate(dm.getOrgstruktura(),"CORG",hr.restart.zapod.OrgStr.getKNJCORG())){
      nspo = dm.getOrgstruktura().getString("NAZIV") + ", " +
             dm.getOrgstruktura().getString("ADRESA") + ", " +
             dm.getOrgstruktura().getString("HPBROJ") + " " +
             dm.getOrgstruktura().getString("MJESTO");
    } else nspo = rpm.getFirstLine() + " " + rpm.getSecondLine() + ", " + rpm.getThirdLine();
    return nspo;
  }

  public String getPRODAVAONICA(){
    String pj ="";
//    if (lookupData.getlookupData().raLocate(dm.getOrgstruktura(),"CORG",ds.getString("KOLSK"))){
//      pj = dm.getOrgstruktura().getString("NAZIV") + ", " +
//           dm.getOrgstruktura().getString("ADRESA") + ", " +
//           dm.getOrgstruktura().getString("HPBROJ") + " " +
//           dm.getOrgstruktura().getString("MJESTO");
//    }
    pj = ds.getString("KOLNSK");
    return pj;
  }

  public String getPOSLGOD(){
    String pg="";
    lookupData.getlookupData().raLocate(dm.getSklad(),"CSKL",ds.getString("KOLSK"));
    pg = dm.getSklad().getString("GODINA");
    return pg;
  }

  public String getDatumIsp(){
    return rdu.dataFormatter(val.getToday());
  }
}