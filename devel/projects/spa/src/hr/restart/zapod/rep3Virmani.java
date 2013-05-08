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

import hr.restart.util.sysoutTEST;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class rep3Virmani implements raReportData {

  frmVirmani fvir = frmVirmani.getInstance();
  String[] PK = fvir.PKs;
  DataSet ds = fvir.get3DataSet();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  hr.restart.robno.repUtil ru = hr.restart.robno.repUtil.getrepUtil();
  hr.restart.robno.repMemo rpm = hr.restart.robno.repMemo.getrepMemo();

  public rep3Virmani() {
    sysoutTEST syst = new sysoutTEST(false);
    syst.prn(ds);
//    ds = getSelectedDS();
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

//  private QueryDataSet getSelectedDS() {
//    QueryDataSet selQDS = hr.restart.baza.Virmani.getDataModule().getTempSet("APP='"+PK[0]+"' AND KNJIG='"+PK[1]+
//        "' AND CKEY='"+PK[2]+"' AND RBR="+PK[3]);
//    selQDS.open();
//    return selQDS;
//  }

  public String getCKEY_1(){
    return ds.getString("CKEY_1");
  }
  public String getCKEY_2(){
    return ds.getString("CKEY_2");
  }
  public String getCKEY_3(){
    return ds.getString("CKEY_3");
  }

  public short getRBR_1(){
    return ds.getShort("RBR_1");
  }
  public short getRBR_2(){
    return ds.getShort("RBR_2");
  }
  public short getRBR_3(){
    return ds.getShort("RBR_3");
  }

  public String getBROJRACUNAPRIMATELJA_1(){
    return ds.getString("BRRACUK_1");
  }
  public String getBROJRACUNAPRIMATELJA_2(){
    return ds.getString("BRRACUK_2");
  }
  public String getBROJRACUNAPRIMATELJA_3(){
     return ds.getString("BRRACUK_3");
  }

  public String getPRIMATELJ_1(){
    return ds.getString("UKORIST_1");
  }
  public String getPRIMATELJ_2(){
    return ds.getString("UKORIST_2");
  }
  public String getPRIMATELJ_3(){
     return ds.getString("UKORIST_3");
  }

  public String getBROJRACUNAPLATITELJA_1(){
    return ds.getString("BRRACNT_1");
  }
  public String getBROJRACUNAPLATITELJA_2(){
    return ds.getString("BRRACNT_2");
  }
  public String getBROJRACUNAPLATITELJA_3(){
     return ds.getString("BRRACNT_3");
  }

  public String getPLATITELJ_1(){
    return ds.getString("NATERET_1");
  }
  public String getPLATITELJ_2(){
    return ds.getString("NATERET_2");
  }
  public String getPLATITELJ_3(){
     return ds.getString("NATERET_3");
  }

  public String getOPISPLACANJA_1(){
    return ds.getString("SVRHA_1");
  }
  public String getOPISPLACANJA_2(){
    return ds.getString("SVRHA_2");
  }
  public String getOPISPLACANJA_3(){
     return ds.getString("SVRHA_3");
  }

  public String getMODELPLACANJA_1(){
    return ds.getString("PNBZ1_1");
  }
  public String getMODELPLACANJA_2(){
    return ds.getString("PNBZ1_2");
  }
  public String getMODELPLACANJA_3(){
     return ds.getString("PNBZ1_3");
  }

  public String getPOZIVNABROJZADUZENJA_1(){
    return ds.getString("PNBZ2_1");
  }
  public String getPOZIVNABROJZADUZENJA_2(){
    return ds.getString("PNBZ2_2");
  }
  public String getPOZIVNABROJZADUZENJA_3(){
     return ds.getString("PNBZ2_3");
  }

  public String getMODELODOBRENJA_1(){
    return ds.getString("PNBO1_1");
  }
  public String getMODELODOBRENJA_2(){
    return ds.getString("PNBO1_2");
  }
  public String getMODELODOBRENJA_3(){
     return ds.getString("PNBO1_3");
  }

  public String getPOZIVNABROJODOBRENJA_1(){
    return ds.getString("PNBO2_1");
  }
  public String getPOZIVNABROJODOBRENJA_2(){
    return ds.getString("PNBO2_2");
  }
  public String getPOZIVNABROJODOBRENJA_3(){
     return ds.getString("PNBO2_3");
  }

  public String getIZNOS_1(){
    return repMatrixVirmans.formatIznos(ds.getBigDecimal("IZNOS_1"),15);
  }
  public String getIZNOS_2(){
    return repMatrixVirmans.formatIznos(ds.getBigDecimal("IZNOS_2"),15);
  }
  public String getIZNOS_3(){
     return repMatrixVirmans.formatIznos(ds.getBigDecimal("IZNOS_3"),15);
  }

  public String getSIFRAOPISAPLACANJA_1(){
    return ds.getString("SIF1_1");
  }
  public String getSIFRAOPISAPLACANJA_2(){
    return ds.getString("SIF1_2");
  }
  public String getSIFRAOPISAPLACANJA_3(){
     return ds.getString("SIF1_3");
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

  public String getDATUMVALUTEUPLATEISPLATE_1(){
    if (hr.restart.sisfun.frmParam.getParam("zapod", "DatumValute", "D", "Ispis datuma valute na virmanu",true).equalsIgnoreCase("N")) return "";
    return rdu.dataFormatter(ds.getTimestamp("DATUMIZV_1"));
  }
  public String getDATUMVALUTEUPLATEISPLATE_2(){
    if (hr.restart.sisfun.frmParam.getParam("zapod", "DatumValute", "D", "Ispis datuma valute na virmanu",true).equalsIgnoreCase("N")) return "";
    return rdu.dataFormatter(ds.getTimestamp("DATUMIZV_2"));
  }
  public String getDATUMVALUTEUPLATEISPLATE_3(){
    if (hr.restart.sisfun.frmParam.getParam("zapod", "DatumValute", "D", "Ispis datuma valute na virmanu",true).equalsIgnoreCase("N")) return "";
    return rdu.dataFormatter(ds.getTimestamp("DATUMIZV_3"));
  }

  public String getDATUMPODNOSENJA_1(){
    if (hr.restart.sisfun.frmParam.getParam("zapod", "DatumPodnosenja", "D", "Ispis datuma podnosenja na virmanu",true).equalsIgnoreCase("N")) return "";
    return rdu.dataFormatter(ds.getTimestamp("DATUMIZV_1"));
  }
  public String getDATUMPODNOSENJA_2(){
    if (hr.restart.sisfun.frmParam.getParam("zapod", "DatumPodnosenja", "D", "Ispis datuma podnosenja na virmanu",true).equalsIgnoreCase("N")) return "";
    return rdu.dataFormatter(ds.getTimestamp("DATUMIZV_2"));
  }
  public String getDATUMPODNOSENJA_3(){
    if (hr.restart.sisfun.frmParam.getParam("zapod", "DatumPodnosenja", "D", "Ispis datuma podnosenja na virmanu",true).equalsIgnoreCase("N")) return "";
    return rdu.dataFormatter(ds.getTimestamp("DATUMIZV_3"));
  }

  public String getHitnost1_1(){
    String hitnost = " ";
    if (ds.getString("JEDZAV_1").substring(0,1).equals("D")) hitnost="X";
    return hitnost;
  }
  public String getHitnost1_2(){
    String hitnost = " ";
    if (ds.getString("JEDZAV_2").substring(0,1).equals("D")) hitnost="X";
    return hitnost;
  }
  public String getHitnost1_3(){
    String hitnost = " ";
    if (ds.getString("JEDZAV_3").substring(0,1).equals("D")) hitnost="X";
    return hitnost;
  }

  public String getHitnost2_1(){
    String hitnost = " ";
    if (ds.getString("JEDZAV_1").substring(1,2).equals("D")) hitnost="X";
    return hitnost;
  }
  public String getHitnost2_2(){
    String hitnost = " ";
    if (ds.getString("JEDZAV_2").substring(1,2).equals("D")) hitnost="X";
    return hitnost;
  }
  public String getHitnost2_3(){
    String hitnost = " ";
    if (ds.getString("JEDZAV_3").substring(1,2).equals("D")) hitnost="X";
    return hitnost;
  }

  public String getPrijenos_1(){
    String prijenos = " ";
    if (ds.getString("JEDZAV_1").substring(2,3).equals("D")) prijenos="X";
    if (!fvir.isNewVir()) prijenos="X";
    return prijenos;
  }
  public String getPrijenos_2(){
    String prijenos = " ";
    if (ds.getString("JEDZAV_2").substring(2,3).equals("D")) prijenos="X";
    if (!fvir.isNewVir()) prijenos="X";
    return prijenos;
  }
  public String getPrijenos_3(){
    String prijenos = " ";
    if (ds.getString("JEDZAV_3").substring(2,3).equals("D")) prijenos="X";
    if (!fvir.isNewVir()) prijenos="X";
    return prijenos;
  }

  public String getUplata_1(){
    String uplata = " ";
    if (ds.getString("JEDZAV_1").substring(3,4).equals("D")) uplata="X";
    return uplata;
  }
  public String getUplata_2(){
    String uplata = " ";
    if (ds.getString("JEDZAV_2").substring(3,4).equals("D")) uplata="X";
    return uplata;
  }
  public String getUplata_3(){
    String uplata = " ";
    if (ds.getString("JEDZAV_3").substring(3,4).equals("D")) uplata="X";
    return uplata;
  }

  public String getIsplata_1(){
    String isplata = " ";
    if (ds.getString("JEDZAV_1").substring(4,5).equals("D")) isplata="X";
    return isplata;
  }
  public String getIsplata_2(){
    String isplata = " ";
    if (ds.getString("JEDZAV_2").substring(4,5).equals("D")) isplata="X";
    return isplata;
  }
  public String getIsplata_3(){
    String isplata = " ";
    if (ds.getString("JEDZAV_3").substring(4,5).equals("D")) isplata="X";
    return isplata;
  }

}