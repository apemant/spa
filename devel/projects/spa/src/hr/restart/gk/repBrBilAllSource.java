/****license*****************************************************************
**   file: repBrBilAllSource.java
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
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;
import hr.restart.zapod.raKonta;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repBrBilAllSource implements raReportData {

  frmBrBilAll fbb;
  DataSet ds;

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();
  String[] colname = new String[] {""};

//  static BigDecimal SL;

  public repBrBilAllSource() {
    fbb = frmBrBilAll.getInstance();
    if (fbb.isKompletnaBilanca()){
      System.out.println("ds = fbb.getBroutoBilancaReportSet()"); //XDEBUG delete when no more needed
      ds = fbb.getBroutoBilancaReportSet();
    } else {
      System.out.println("ds = fbb.getSintetikAnalitikSet()"); //XDEBUG delete when no more needed
      ds = fbb.getSintetikAnalitikSet();
    }
    ru.setDataSet(ds);
//    sysoutTEST s = new sysoutTEST(false);
//    s.prn(ds);
//    s.showInFrame(ds,"...");
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
    fbb = null;
  }
  
  
  ///////
  public double getID() {
    return ds.getBigDecimal("ID").doubleValue();
  }

  public double getIP() {
    return ds.getBigDecimal("IP").doubleValue();
  }
  
  public double getIDALL() {
    return (ds.getBigDecimal("ID").add(ds.getBigDecimal("POCID"))).doubleValue();
  }

  public double getIPALL() {
    return (ds.getBigDecimal("IP").add(ds.getBigDecimal("POCIP"))).doubleValue();
  }

  public double getPSID(){
    return ds.getBigDecimal("POCID").doubleValue();
  }

  public double getPSIP(){
    return ds.getBigDecimal("POCIP").doubleValue();
  }
  
  public double getSALPS(){
    return ds.getBigDecimal("SALPS").doubleValue();
  }
  
  public double getSALPROM(){
    return ds.getBigDecimal("SALPROM").doubleValue();
  }
  
  public double getSALDO(){
    return ds.getBigDecimal("SALDO").doubleValue();
  }
  //////
  
  

  
  
  /////// za sume kod sintetičke bilance
  public double getIDS() {
    if (ds.getString("BROJKONTA").length() > 1) return 0;
    return ds.getBigDecimal("ID").doubleValue();
  }

  public double getIPS() {
    if (ds.getString("BROJKONTA").length() > 1) return 0;
    return ds.getBigDecimal("IP").doubleValue();
  }

  public double getPSIDS(){
    if (ds.getString("BROJKONTA").length() > 1) return 0;
    return ds.getBigDecimal("POCID").doubleValue();
  }

  public double getPSIPS(){
    if (ds.getString("BROJKONTA").length() > 1) return 0;
    return ds.getBigDecimal("POCIP").doubleValue();
  }
  
  public double getSALPSS(){
    if (ds.getString("BROJKONTA").length() > 1) return 0;
    return ds.getBigDecimal("SALPS").doubleValue();
  }
  
  public double getSALPROMS(){
    if (ds.getString("BROJKONTA").length() > 1) return 0;
    return ds.getBigDecimal("SALPROM").doubleValue();
  }
  
  public double getSALDOS(){
    if (ds.getString("BROJKONTA").length() > 1) return 0;
    return ds.getBigDecimal("SALDO").doubleValue();
  }
  //////
  
  
  
  public String getBROJKONTA() {
    return ds.getString("BROJKONTA");
  }

  public String getNAZIVKONTA() {
    return ds.getString("NK");
  }

  public String getKLASE() {
    return ds.getString("BROJKONTA").substring(0,1);
  }

  public String getRKLASE() {
    return "RAZRED - "+ds.getString("BROJKONTA").substring(0,1);
  }

  public String getNAZIVKLASE() {
    return raKonta.getNazivKonta(ds.getString("BROJKONTA").substring(0,1));
  }

  public String getKLASE2() {
    return ds.getString("BROJKONTA").substring(0,2);
  }

  public String getNAZIVKLASE2() {
    return raKonta.getNazivKonta(ds.getString("BROJKONTA").substring(0,2));
  }

  public String getKLASE3() {
    return ds.getString("BROJKONTA").substring(0,3);
  }

  public String getNAZIVKLASE3() {
    return raKonta.getNazivKonta(ds.getString("BROJKONTA").substring(0,3));
  }

  public String getCORGLAVA() {
    if (!fbb.getSKUPNI().equals("ZBIRNO")) {
      return fbb.getCORG();
    }
    return ds.getString("CORG");
  }

  public String getNAZORGLAVA() {
    lookupData.getlookupData().raLocate(dm.getOrgstruktura(),"CORG",fbb.getCORG());
    return dm.getOrgstruktura().getString("NAZIV");
  }

  public String getOJGR() {
    if (fbb.getSKUPNI().equals("ZBIRNO")) {
      return "";
    }
    return "Organizaciska jedinica";
  }

  public String getCORG() {
    if (fbb.getSKUPNI().equals("ZBIRNO")) {
      return "";
    }
    return ds.getString("CORG");
  }

  public String getNAZORG() {
    if (fbb.getSKUPNI().equals("ZBIRNO")) {
      return "";
    }
    String nazorg ="";
    ru.setDataSet(ds);
    colname[0] = "CORG";
    nazorg = ru.getSomething(colname,dm.getOrgstruktura(),"NAZIV").getString();
    return nazorg;
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

  public String getGrouper(){
    return ds.getString("CORG");
  }
  
  public String getZaPeriod() {
    String prefix = "Početno stanje i promet ";
    if (fbb.getPromPocSt().equals("N")) prefix = "Promet ";
    if (fbb.getPromPocSt().equals("P")) return "Početno stanje " + fbb.getGodina() + " godine";
    if (fbb.getVRSTA().equals("BB")){
      if (fbb.getPocMj().equals(fbb.getZavMj()))
        return prefix + "za " + fbb.getPocMj() + " mjesec " + fbb.getGodina();
      else 
        return prefix + "u periodu od "  + fbb.getPocMj() + " mjeseca do " + fbb.getZavMj()+ " mjeseca " + fbb.getGodina();
    } else {
      return prefix + "u periodu od "  + rdu.dataFormatter(fbb.getPocDat()) + " do " + rdu.dataFormatter(fbb.getZavDat());
    }
  }
  
  public String getCVAL(){
    return fbb.getCval();
  }
  
  public String getNAZVAL(){
    return fbb.getNazval();
  }
  
  public BigDecimal getTECAJ() {
    return fbb.getTecaj();
  }
  
  public String getVRBIL(){
    String vrbil = "";
    String kompletnost = "Kompletna bilanca";
    String zbirnost = ", pojedinačno";
    String struktura = " za cjelokupnu strukturu org. jed. "+getNAZORGLAVA();
    String fromFbb = fbb.getBilanca();
    if (fromFbb.equals("A")) kompletnost = "Analitička bilanca";
    if (fromFbb.equals("S")) kompletnost = "Sintetička bilanca";
    if (fbb.getSKUPNI().equals("ZBIRNO")) zbirnost = " skupno";
    if (fbb.getPRIPADNOST().equals("1")) struktura = " za odabranu org. jed.";
    
    
    vrbil = kompletnost+zbirnost+struktura;
    return vrbil;
  }
}