/****license*****************************************************************
**   file: repIOS.java
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

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.robno.sgQuerys;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.dlgRunReport;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repIOS implements raReportData { //sg.com.elixir.reportwriter.datasource.IDataProvider {

  raIspisKartica rik = raIspisKartica.getInstance(raIspisKartica.IOS);
  DataSet ds;
  DataSet totals;

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();
  String oib;

  public repIOS() {
    ru.setDataSet(ds);
    try {
      String prov = dlgRunReport.getCurrentDlgRunReport().getCurrentDescriptor().getName();
      rik.outrange = !prov.equals("hr.restart.sk.repDospDan");
      rik.plus = 0;
      if (prov.equals("hr.restart.sk.repDospDan7")) rik.plus = 7;
      if (prov.equals("hr.restart.sk.repDospDan30")) rik.plus = 30;
      ds = rik.getDataSet();
    } finally {
      rik.outrange = true;
    }
    if (rik.stm != null && rik.stm.countSelected() > 1)
      ds = rik.stm.getSelectedView(ds);
    oib = frmParam.getParam("robno", "oibMode", "MB", 
      "Staviti matièni broj (MB) ili OIB?");
  //sysoutTEST ST = new sysoutTEST(false);
  //ST.prn(ds);
  }

//  public repIOS(int idx) {
//    if (idx == 0){
//      sysoutTEST ST = new sysoutTEST(false);
//      ST.prn(ds);
//    }
//    ds.goToRow(idx);
//  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    totals = rik.getTotals(ds.getInt("CPAR"));
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ru.setDataSet(null);
    if (rik.stm != null && rik.stm.countSelected() > 1)
      rik.stm.destroySelectedView();
    ds = null;
    raIspisKartica.dokformat = null;
//    fbb = null;
  }


//  public java.util.Enumeration getData() {
//    return new java.util.Enumeration() {
//    {
//      ds.open();
//    }
//    int indx=0;
//      public Object nextElement() {
//
//        return new repIOS(indx++);
//      }
//      public boolean hasMoreElements() {
//        return (indx < ds.getRowCount());
//      }
//    };
//  }
//
//  public void close() {
//  }

  public int getCPAR(){
     return ds.getInt("CPAR");
  }

  public int getPartner(){
    return totals.getRow();
  }
  
  public String getNAZPAR(){
    return rik.getNazivPartnera(ds.getInt("CPAR"));
  }

  public String getMjestoIpbrPARTNERA() {
      lookupData.getlookupData().raLocate(dm.getPartneri(), "CPAR", String.valueOf(ds.getInt("CPAR")));
      return dm.getPartneri().getInt("PBR")+" "+dm.getPartneri().getString("MJ");
  }

  public String getAdresaPARTNERA() {
      lookupData.getlookupData().raLocate(dm.getPartneri(), "CPAR", String.valueOf(ds.getInt("CPAR")));
      return dm.getPartneri().getString("ADR");
  }
  
  public String getPartnerText() {
    lookupData.getlookupData().raLocate(dm.getPartneri(), "CPAR", String.valueOf(ds.getInt("CPAR")));
    return getCPAR() + "    " + getNAZPAR() + "    " + getMjestoIpbrPARTNERA() + ", " + getAdresaPARTNERA() +
      (dm.getPartneri().getString("OIB").length() == 0 ? "" :
        ", OIB " + dm.getPartneri().getString("OIB"));
}

  public String getMBPAR(){
    String result = "";
    if (!oib.equalsIgnoreCase("MB")) {
      if (lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",ds.getInt("CPAR")+""))
        result = "OIB " + dm.getPartneri().getString("OIB");
    } 
    if (oib.equalsIgnoreCase("MB") || result.length() == 0) {
      colname[0] = "CPAR";
      if (lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",ds.getInt("CPAR")+""))
        result = "MB " + dm.getPartneri().getString("MB"); 
    }
    return result;
  }

  public String getCORG(){
     return ds.getString("CORG");
  }

//  public String getVRDOK(){
//     return ds.getString("VRDOK");
//  }

  public String getBROJDOK(){
     //return ds.getString("BROJDOK");
    return raIspisKartica.createIOSDokText(ds);
  }

  public String getDATDOK(){
     return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }

  public String getDATDOSP(){
     return rdu.dataFormatter(ds.getTimestamp("DATDOSP"));
  }

  public double getID(){
    if (ds.getBigDecimal("ID").signum() != 0)
      return ds.getBigDecimal("SALDO").doubleValue();
    return raSaldaKonti.n0.doubleValue();
  }

  public double getIP(){
    if (ds.getBigDecimal("IP").signum() != 0)    
      return ds.getBigDecimal("SALDO").doubleValue(); 
    return raSaldaKonti.n0.doubleValue(); 
  }

  public BigDecimal getSALDO(){
     return ds.getBigDecimal("SALDO");
  }

  public String getZIRO(){
//    String vrdok = ds.getString("VRDOK");
//    String cskl = ds.getString("CSKL");
//    String stavka = Short.toString(ds.getShort("STAVKA"));
//    String konto = hr.restart.gk.raKnjizenje.getBrojKonta(vrdok,cskl,stavka);
//    System.out.println("vrdok " + vrdok + " cskl " +cskl + " stavka " + stavka + "    KONTO: " + konto);
    return Integer.toString(ds.getInt("CPAR"));
  }

  public String getLabelKUPDOB(){
    if (rik.isBoth()) return "Partner";
    if (rik.isKupac()) return "Kupac";
    return "Dobavljaè";
  }
  
  public String getLabelKUPDOBS(){
    if (rik.isBoth()) return "Saldo partnera";
    if (rik.isKupac()) return "Saldo kupca";
    return "Saldo dobavljaèa";
  }

  public String getNADAN(){
    return rdu.dataFormatter(rik.getLastDay());
  }

  public String getPODNASLOVPRILOGA(){
    return "OTVORENIH STAVAKA NA DAN ".concat(getNADAN());
  }

  public BigDecimal getPOKAZNISALDO(){
    return rik.getSaldo(getCPAR());
  }
  
  public BigDecimal getPOKAZNISALDO2(){
    if (rik.isKupac())
      return rik.getSaldo(getCPAR());
    return rik.getSaldo(getCPAR()).negate();
  }

  public String getEnterFirstLine(){
    return "\n"+re.getFirstLine();
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
  
  public String getOneLine(){
    return re.getOneLine();
  }
  
  public String getZiroLine(){
//  System.out.println("ziro racun!!"); //XDEBUG delete when no more needed
    return "ŽIRO RAÈUN:  " + re.getLogoZiro(); 
  }
  
  public String getTelLine(){
//  System.out.println("ziro racun!!"); //XDEBUG delete when no more needed
    if (re.getLogoTel2() == null || re.getLogoTel2().length() == 0)
      return "Tel:  " + re.getLogoTel1(); 
    if (re.getLogoTel1() == null || re.getLogoTel1().length() == 0)
      return "Tel:  " + re.getLogoTel2();
    return "Tel:  " + re.getLogoTel1() + ", " + re.getLogoTel2();
  }
  
  public String getFaxLine(){
//  System.out.println("ziro racun!!"); //XDEBUG delete when no more needed
    return "Fax: " + re.getLogoFax(); 
  }


  public String getUMJESTUDANA(){
    return hr.restart.sisfun.frmParam.getParam("sk","uMjestu","U Zagrebu,","u mjestu za izvjestaje koji to trebaju",true)+" "+rdu.dataFormatter(vl.getToday());
  }
  
  public String getPremaEvidenciji(){
//   return "Vaš raèun broj "+getZIRO()+" prema našoj poslovnoj evidenciji na dan "+getNADAN()+" sastoji se iz sljedeæih otvorenih stavaka (PRILOG 1)"; 
    return "Prema našoj poslovnoj evidenciji na dan "+getNADAN()+
      (rik.isKupac() ? " vaša" : " naša")+
      " dugovanja se sastoje od sljedeæih otvorenih stavaka (PRILOG 1)";
  }
  
  public String getPokazniSaldo(){
    BigDecimal sal =  getPOKAZNISALDO();
    String side = Aus.leg(sal.signum(), "u VAŠU korist", "", "u NAŠU korist");
    //String side = sal.signum() < 0 ? "u VAŠU korist" : "u NAŠU korist";
    return "\nPokazuje dug od    " + sgQuerys.getSgQuerys().format(sal.abs(), 2) + 
           " Kn    "+side; 
  }
  
  public String getSuglasniSaldo(){
    BigDecimal sal =  getPOKAZNISALDO();
    String side = Aus.leg(sal.signum(),
        "u korist " + rik.getNazivPartnera(ds.getInt("CPAR")), "",
        "u korist " + re.getFirstLine());
    //String side = sal.signum() < 0 ? rik.getNazivPartnera(ds.getInt("CPAR")) : re.getFirstLine();
    return "Potvrðujemo suglasnost duga od " + hr.restart.robno.sgQuerys.getSgQuerys().format(sal.abs(),2) + 
           " kn " + side; 
  }

  public String getNASLOV(){
    return "IZVADAK\nOTVORENIH STAVAKA NA DAN "+rdu.dataFormatter(vl.getToday());
  }
  
  public String getKONTAKTOSOBA(){
    return hr.restart.sisfun.frmParam.getParam("sk","ZaVjerovnika","","Ime osobe vjerovnika na opomeni",true); 
    // + "\nTel: "+hr.restart.sisfun.frmParam.getParam("sk","TelefonVj","","Broj telefona vjerovnika",true);
  }
  
  public String getKONTAKTTEL(){
    return hr.restart.sisfun.frmParam.getParam("sk","TelefonVj","","Broj telefona vjerovnika",true);
  }
  
  public String getNAZPARL(){
    if (lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",ds.getInt("CPAR")+""))
      return dm.getPartneri().getString("NAZPAR");
    else
      return "";
  }
}
