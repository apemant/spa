/****license*****************************************************************
**   file: repKPR_fake.java
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
public class repKPR_fake implements raReportData {

  /** @todo for future use ovo ode gori :)) */

  _Main main;
  repMemo rpm = repMemo.getrepMemo();
  upKPRFake upk = upKPRFake.getInstance();
  DataSet ds;
  dM dm = dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  Valid val = Valid.getValid();
  static int start;

  public repKPR_fake() {
    ds = upk.getRepSet();
//    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//    st.prn(ds);
    ds.open();
    ru.setDataSet(ds);
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
    ds = null;
  }

  public String getBRDOK(){
    return ds.getInt("BRDOK")+"";
  }

  public String getDATDOK(){
    return rdu.dataFormatter(ds.getTimestamp("DATUM"));
  }

  public String getVRDOK(){
    return ds.getString("VRDOK");
  }

  public double getSUMZAD(){
    return ds.getBigDecimal("ZAD").doubleValue();
  }

  public double getSUMRAZ(){
    return ds.getBigDecimal("RAZ").doubleValue();
  }

   public String getKOLND(){
    return ds.getString("OPIS");
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

  public int getRBR(){
    return ds.getInt("RBR");
  }

  public double getDONOSZAD(){
    return 0.00;
  }

  public double getDONOSRAZ(){
    return 0.00;
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

    if (lookupData.getlookupData().raLocate(dm.getSklad(),"CSKL",ds.getString("CSKL"))){//lookupData.getlookupData().raLocate(dm.getOrgstruktura(),"CORG",dm.getSklad().getString("CORG"))){
      pj = /*dm.getOrgstruktura().getString("NAZIV") + ", " +
           dm.getOrgstruktura().getString("ADRESA") + ", " +
           dm.getOrgstruktura().getString("HPBROJ") + " " +
           dm.getOrgstruktura().getString("MJESTO");*/
           dm.getSklad().getString("NAZSKL");
    }
    return pj;
  }

  public String getPOSLGOD(){
    String pg="";
    lookupData.getlookupData().raLocate(dm.getSklad(),"CSKL",ds.getString("CSKL"));
    pg = dm.getSklad().getString("GODINA");
    return pg;
  }

  public String getDatumIsp(){
    return rdu.dataFormatter(val.getToday());
  }
}
//package hr.restart.robno;
//
///**
// * @author S.G.
// *
// * Started 2005.07.20
// * 
// */
//
//public class repKPR_fake {
//
//}
