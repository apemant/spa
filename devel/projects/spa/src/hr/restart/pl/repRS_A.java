/****license*****************************************************************
**   file: repRS_A.java
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
import hr.restart.sisfun.frmParam;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repRS_A implements sg.com.elixir.reportwriter.datasource.IDataProvider, raReportData {

  hr.restart.robno._Main main;
  frmRS fRS = frmRS.getInstance();
  DataSet ds = fRS.getRepSetRS_A();
  DataSet hs = fRS.getHead();
  static String sha1 = "";


  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repRS_A() {
//    System.out.println("fRS = " + fRS.toString());
    ru.setDataSet(ds);
   }

  public repRS_A(int idx) {
     if (idx == 0){
      sha1 = fRS.getSHA1();
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
      return new repRS_A(indx++);
    }
    public boolean hasMoreElements() {
      return (indx < ds.getRowCount());
    }
    };
  }

  public void close() {
  }

  private String knjig(){
    return hr.restart.zapod.OrgStr.getKNJCORG();
  }

  public String getINFO() {
    return frmParam.getParam("pl", "RSinfo", "", "Informacija koja se nalazi kod potpisa na RS obrascu npr. br.tel.");
  }
  public String getNAZIV(){
    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {knjig()});
    return dm.getLogotipovi().getString("NAZIVLOG");
  }

  public String getADRESA(){
    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {knjig()});
    return dm.getLogotipovi().getString("ADRESA").concat(" " + dm.getLogotipovi().getInt("PBR")).concat(" " + dm.getLogotipovi().getString("MJESTO"));
  }

  public String getMB(){
    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
    Integer mb; /*System.out.println("mb length = " + dm.getLogotipovi().getString("MATBROJ").length());*/
    if (dm.getLogotipovi().getString("MATBROJ").length() < 13){
      mb = Integer.valueOf(dm.getLogotipovi().getString("MATBROJ"));
      return vl.maskZeroInteger(mb, 8);
    }
    return "";
  }

  public String getJMBGVL(){
    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
    /*System.out.println("mb length = " + dm.getLogotipovi().getString("MATBROJ").length());*/
    if (dm.getLogotipovi().getString("MATBROJ").length() == 13){
//       mb = Integer.valueOf(dm.getLogotipovi().getString("MATBROJ"));
      return dm.getLogotipovi().getString("MATBROJ");
    }
    return "";
  }
  public String getOIB(){
    if (ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {hr.restart.zapod.OrgStr.getKNJCORG()})) {
      return dm.getLogotipovi().getString("OIB");
    }
    return "";
  }

  public String getVROB(){
//    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
//    if (dm.getLogotipovi().getString("MATBROJ").length() == 13){
//      return "02";
//    }
//    return "01";
    return fRS.getVrstaObveznika();
  }

  public String getIDENTIFIKATOR(){
    return hs.getString("IDENTIFIKATOR");
  }

  public String getMJESEC(){
    if (hs.getString("CSIF").equals("03") ||hs.getString("CSIF").equals("05") ||hs.getString("CSIF").equals("08")){
      return "00"; // zbog nemogucnosti komunikacije izmedju nekih klasa mora bit vako!!
    }
    return Valid.getValid().maskString(hs.getShort("MJESEC")+"",'0',2);
  }

  public short getGODINA(){
    return hs.getShort("GODINA");
  }

  public short getODDANA(){
    return hs.getShort("ODDANA");
  }

  public short getDODANA(){
    return hs.getShort("DODANA");
  }

  public String getSHIFRA(){
    return hs.getString("CSIF");
  }

  public BigDecimal getBRUTOMJ(){
     return ds.getBigDecimal("BRUTOMJ");
  }

  public BigDecimal getBRUTO(){
     return ds.getBigDecimal("BRUTO");
  }

  public BigDecimal getMIO1MJ(){
     return ds.getBigDecimal("MIO1MJ");
  }

  public BigDecimal getMIO1(){
     return ds.getBigDecimal("MIO1");
  }

  public BigDecimal getMIO2MJ(){
     return ds.getBigDecimal("MIO2MJ");
  }

  public BigDecimal getMIO2(){
     return ds.getBigDecimal("MIO2");
  }

  public BigDecimal getZO(){
     return ds.getBigDecimal("ZO");
  }

  public BigDecimal getZOMJ(){
     return ds.getBigDecimal("ZOMJ");
  }

  public BigDecimal getZAPOSMJ(){
     return ds.getBigDecimal("ZAPOSMJ");
  }

  public BigDecimal getZAPOS(){
     return ds.getBigDecimal("ZAPOS");
  }

  public BigDecimal getPOREZMJ(){
     return ds.getBigDecimal("POREZMJ");
  }

  public BigDecimal getPOREZ(){
     return ds.getBigDecimal("POREZ");
  }

  public BigDecimal getPRIREZ(){
     return ds.getBigDecimal("PRIREZ");
  }

  public BigDecimal getPRIREZMJ(){
     return ds.getBigDecimal("PRIREZMJ");
  }

  public BigDecimal getNETOPK(){
     return ds.getBigDecimal("NETOPK");
  }

  public int getBRRADNIKA(){
    return fRS.getBrojRadnika();
  }

  public String getBROJBSTR(){
    if (!fRS.isDisketa()){
      return String.valueOf(fRS.getBrojBStranica());
    }
    else
      return "";
  }

  public String getSH01(){
    if (fRS.isDisketa())
      return sha1.substring(0,20).trim();
    else
      return "";
  }

  public String getSH02(){
    if (fRS.isDisketa())
      return sha1.substring(20,40).trim();
    else
      return "";
  }

  public String getSH03(){
    if (fRS.isDisketa())
      return sha1.substring(40,60).trim();
    else
      return "";
  }

  public String getSH04(){
    if (fRS.isDisketa())
      return sha1.substring(60,sha1.length()).trim();
    else
      return "";
  }
  
  public String getGODMJISPL() {
    try {
      if (!fRS.jpHead.isMjesecIsplate()) return "";
      return fRS.getGODMJISPL();
    } catch (Exception e) {
      e.printStackTrace();
      return "SRAN - JE";
    }
  }
  
  public raReportData getRow(int i) {
    if (sha1.equals("") || i==0){
      sha1 = fRS.getSHA1();
    }
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.getRowCount();
  }
}