/****license*****************************************************************
**   file: repZakList.java
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

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.Valid;

import com.borland.dx.dataset.DataSet;

public class repZakList implements sg.com.elixir.reportwriter.datasource.IDataProvider {

//  "hr.restart.gk.repZakList", "Zaklju\u010Dni list", 2

  frmZakList zl = frmZakList.getFrmZakList();
  DataSet ds = zl.getReportQDS();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();
  public static double iznosGrupe;
  public static int rb;

  public repZakList() {
    ru.setDataSet(ds);
  }

  public repZakList(int idx) {
    if(idx==0){
      rb = 0;
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

        return new repZakList(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }
 public double getID() {
    return ds.getBigDecimal("ID").doubleValue();
  }
  public double getIP() {
    return ds.getBigDecimal("IP").doubleValue();
  }
  public double getPSID(){
    return ds.getBigDecimal("POCID").doubleValue();
  }
  public double getPSIP(){
    return ds.getBigDecimal("POCIP").doubleValue();
  }
  public double getSALDOID(){
    return ds.getBigDecimal("SALDOID").doubleValue();
  }
  public double getSALDOIP(){
    return ds.getBigDecimal("SALDOIP").doubleValue();
  }
  public String getSORTER1(){
    return ds.getString("SORT1").trim();
  }
  public String getSORTER2(){
    return ds.getString("SORT2").trim();
  }
  public String getSORTER3(){
    return ds.getString("SORT3").trim();
  }

  public String getZaPeriod() {
    return "Do " + zl.jtMjesecZav.getText().trim() + " mjeseca " + zl.jtGodina.getText().trim() + ". godine";
  }


  public String getBROJKONTA() {
    return ds.getString("BROJKONTA");
  }

  public String getNAZIVKONTA() {
    return ds.getString("NK");
  }

  public String getNAZORG() {
      return zl.getNAZORG();
  }

  public String getCORG() {
      return zl.getCORG();
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
  public String getLogoMjesto(){
    return re.getLogoMjesto();
  }
  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }
}