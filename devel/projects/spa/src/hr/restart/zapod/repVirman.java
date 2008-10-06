/****license*****************************************************************
**   file: repVirman.java
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
package hr.restart.zapod;

import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class repVirman implements raReportData {

  frmVirmani fvir = frmVirmani.getInstance();
  String[] PK = fvir.PKs;
  DataSet ds;// = fvir.getRaQueryDataSet();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  hr.restart.robno.repUtil ru = hr.restart.robno.repUtil.getrepUtil();
  hr.restart.robno.repMemo rpm = hr.restart.robno.repMemo.getrepMemo();

  public repVirman() {
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(ds);
    ds = getSelectedDS();
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ds = null;
  }

  private QueryDataSet getSelectedDS() {
    QueryDataSet selQDS = hr.restart.baza.Virmani.getDataModule().getTempSet("APP='"+PK[0]+"' AND KNJIG='"+PK[1]+
        "' AND CKEY='"+PK[2]+"' AND RBR="+PK[3]);
    selQDS.open();
    return selQDS;
  }

  public String getCKEY(){
     return ds.getString("CKEY");
  }

  public short getRBR(){
    return ds.getShort("RBR");
  }

  public String getBROJRACUNAPRIMATELJA(){
     return ds.getString("BRRACUK");
  }

  public String getPRIMATELJ(){
     return ds.getString("UKORIST");
  }

  public String getBROJRACUNAPLATITELJA(){
     return ds.getString("BRRACNT");
  }

  public String getPLATITELJ(){
     return ds.getString("NATERET");
  }

  public String getOPISPLACANJA(){
     return ds.getString("SVRHA");
  }

  public String getMODELPLACANJA(){
     return ds.getString("PNBZ1");
  }

  public String getPOZIVNABROJZADUZENJA(){
     return ds.getString("PNBZ2");
  }

  public String getMODELODOBRENJA(){
     return ds.getString("PNBO1");
  }

  public String getPOZIVNABROJODOBRENJA(){
     return ds.getString("PNBO2");
  }

  public String getIZNOS(){
     return repMatrixVirmans.formatIznos(ds.getBigDecimal("IZNOS"),15);
  }

  public String getSIFRAOPISAPLACANJA(){
     return ds.getString("SIF1");
  }

  /*


  protected String getDatum() {
    String dat = datumParser(qds.getTimestamp("DATUMIZV").toString());
    if (hr.restart.sisfun.frmParam.getParam("zapod", "DatumValute", "D", "Ispis datuma valute na virmanu",true).equalsIgnoreCase("N")) return "";
    return dat;
  }

  protected String getDatumPod() {
    String dat = datumParser(qds.getTimestamp("DATUMPR").toString());
    if (hr.restart.sisfun.frmParam.getParam("zapod", "DatumPodnosenja", "D", "Ispis datuma podnosenja na virmanu",true).equalsIgnoreCase("N")) return "";
    return dat;
  }
  */

  public String getDATUMVALUTEUPLATEISPLATE(){
    if (hr.restart.sisfun.frmParam.getParam("zapod", "DatumValute", "D", "Ispis datuma valute na virmanu",true).equalsIgnoreCase("N")) return "";
    return rdu.dataFormatter(ds.getTimestamp("DATUMIZV"));
  }

  public String getDATUMPODNOSENJA(){
    if (hr.restart.sisfun.frmParam.getParam("zapod", "DatumPodnosenja", "D", "Ispis datuma podnosenja na virmanu",true).equalsIgnoreCase("N")) return "";
    return rdu.dataFormatter(ds.getTimestamp("DATUMIZV"));
  }

  public String getHitnost1(){
    String hitnost = " ";
    if (ds.getString("JEDZAV").substring(0,1).equals("D")) hitnost="X";
    return hitnost;
  }

  public String getHitnost2(){
    String hitnost = " ";
    if (ds.getString("JEDZAV").substring(1,2).equals("D")) hitnost="X";
    return hitnost;
  }

  public String getPrijenos(){
    String prijenos = " ";
    if (ds.getString("JEDZAV").substring(2,3).equals("D")) prijenos="X";
    if (!fvir.isNewVir()) prijenos="X";
    return prijenos;
  }

  public String getUplata(){
    String uplata = " ";
    if (ds.getString("JEDZAV").substring(3,4).equals("D")) uplata="X";
    return uplata;
  }

  public String getIsplata(){
    String isplata = " ";
    if (ds.getString("JEDZAV").substring(4,5).equals("D")) isplata="X";
    return isplata;
  }

}