/****license*****************************************************************
**   file: repObracunRadnogNaloga2.java
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

import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repObracunRadnogNaloga2 implements raReportData {//sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  hr.restart.util.sysoutTEST sys = new hr.restart.util.sysoutTEST(false);
  frmServis rnl = frmServis.getInstance();
  DataSet prijava = rnl.getrepQDSprijava();
  DataSet detalji = rnl.getrepQDSdetails();
  DataSet ds = prijava;
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();

  String[] colname = new String[] {""};
  String[] colname1 = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo rm = repMemo.getrepMemo();

  public repObracunRadnogNaloga2() {
    ru.setDataSet(detalji);
    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
    syst.prn(prijava);
  }

  public raReportData getRow(int i) {
    detalji.goToRow(i);
    prijava.goToRow(0);
    return this;
  }

  public int getRowCount() {
    return detalji.rowCount();
  }

  public void close() {
    ru.setDataSet(null);
    prijava = null;
    detalji = null;
  }

  /*
      Sekcija za detalje radnog naloga
  */

  public String getVRDOK() {
    return detalji.getString("VRDOK");
  }
  public int getBRDOK() {
    return detalji.getInt("BRDOK");
  }
  public String getGOD() {
    return detalji.getString("GOD");
  }
  
  ///TODO NEW SHOES
  
  public String getBRNAR(){
   return prijava.getString("BRNAR");
  }
  
  public String getDATUMOTVARANJA(){
    return rdu.dataFormatter(prijava.getTimestamp("DATDOK"));
  }
  
  public String getDATUMOBRADE(){
    try {
      return rdu.dataFormatter(prijava.getTimestamp("DATUMO"));
    } catch (Exception e) {
      return "";
    }
  }
  
  public String getDATUMZATVARANJA(){
    try {
      return rdu.dataFormatter(prijava.getTimestamp("DATUMZ"));
    } catch (Exception e) {
      return "";
    }
  }
  
  /// END OF NEW SHOES
  
  public String getDATUMDOK(){
     return rdu.dataFormatter(prijava.getTimestamp("DATDOK"));
  }
//  public String getDATUMOBRADE(){
//     return rdu.dataFormatter(prijava.getTimestamp("DATUMO"));
//  }
//  public String getDATUMZATVARANJA(){
//     return rdu.dataFormatter(prijava.getTimestamp("DATUMZ"));
//  }
  public String getJEDANBROJ(){
    if (detalji.getString("VRDOK").equals("RNU") || detalji.getString("VRDOK").equals("RNL")) return "";
    ru.setDataSet(detalji);
    return "Izdatnica " + ru.getFormatBroj();
  }
  public String getRADOVIMATERIJAL() {
    if (detalji.getString("VRDOK").equals("RNU") || detalji.getString("VRDOK").equals("RNL")) return "Radne operacije";
    return "Ugraðeni dijelovi";
  }
  public String getCART() {
      return Aut.getAut().getIzlazCARTdep(detalji);
  }
  public String getNAZART() {
    return detalji.getString("NAZART");
  }
  public String getJM() {
    return detalji.getString("JM");
  }
  public BigDecimal getKOL() {
    return detalji.getBigDecimal("KOL");
  }
  public BigDecimal getVC() {
    return detalji.getBigDecimal("VC");
  }
  public double getIZNOS() {
    return (getKOL().multiply(getVC())).doubleValue();
//    return detalji.getBigDecimal("IPRODSP").doubleValue();
  }

  /*
      Sekcija za dio radnog naloga koji je isti kao u prijavi radnog naloga.
  */

  public String getCVLASNIK() {
    return prijava.getInt("CKUPAC")+"";
  }
  public String getVLASNIKIME() {
    return "\n"+rnl.getVlasnikIme(prijava);
  }
  public String getVLASNIKADRESA() {
    return rnl.getVlasnikAdr(prijava);
  }
  public String getVLASNIKPBR() {
    return rnl.getVlasnikPBR(prijava);
  }
  public String getVLASNIKMJESTO() {
    return rnl.getVlasnikMJ(prijava);
  }
  public String getVLASNIKMB(){
    return rnl.getVlasnikMatBr(prijava);
  }
  public String getVLASNIKTEL() {
    return rnl.getVlasnikTel(prijava);
  }
  public String getATTRNAZ() {
    return rnl.getNazvrsubj(prijava);
  }
  public String getATTRIBNAME() {
    return rnl.getAtrNames(prijava,1,0) + " ";
  }
  public String getATTRIBVALUE() {
    return rnl.getAtrValues(prijava,1,0) + " ";
  }
  public int getBRDOKRNL() {
    return prijava.getInt("BRDOK");
  }
  public String getOPIS() {
    return prijava.getString("OPIS");
  }
  public int getCPAR() {
    return prijava.getInt("CPAR");
  }
  public String getNAZPAR() {
    ru.setDataSet(prijava);
    colname[0] = "CPAR";
    if (getCPAR()!=0) return ru.getSomething(colname,dm.getPartneri(),"NAZPAR").getString();
    return "";
  }
  public String getOsigurac(){
    if (getCPAR()!=0) return "Osiguravatelj:";
    return "";
  }
  public String getFUSE1() {
    if (getCPAR()!=0) return getCPAR()+"";
    return "";
  }
  public String getFUSE2() {
    if (getCPAR()!=0) return getNAZPAR();
    return "";
  }
//  public String getFUSE() {
//    if (getCPAR()!=0) return "Osiguravatelj:  " + getCPAR() + "  " +  getNAZPAR();
//    return "";
//  }
  public String getNAZNAP01() {
    ru.setDataSet(prijava);
    colname[0] = "CNAP1";
    colname1[0] = "CNAP";
    return ru.getSomething2(colname1,colname,prijava,dm.getNapomene(),"NAZNAP").getString();
  }
  public String getNAZNAP02() {
    ru.setDataSet(prijava);
    colname[0] = "CNAP2";
    colname1[0] = "CNAP";
    return ru.getSomething2(colname1,colname,prijava,dm.getNapomene(),"NAZNAP").getString();
  }
  public String getNAZNAP() {
    String napomena = getNAZNAP01();
    if (getNAZNAP01().equals("")) napomena = getNAZNAP02();
    if (!getNAZNAP01().equals("") && !getNAZNAP02().equals("")) napomena = getNAZNAP01() + "\n" + getNAZNAP02();
    if (ds.getString("NAPOM").length() > 0) {
      if (napomena.length() == 0) napomena = ds.getString("NAPOM");
      else napomena = napomena + "\n" + ds.getString("NAPOM");
    }
    return napomena;
  }
  public String getFormatBroj(){
    ru.setDataSet(prijava);
    return ru.getFormatBroj();
  }
  public int getLogoPotvrda() {
    if (rm.test()) return 1;
    return 0;
  }
  public String getLogoFullAdr() {
    if (!rm.test()) return "";
    else return rm.getLogoAdresa() + " ,  " + rm.getLogoPbr() + " " + rm.getLogoMjesto();
  }

  public String getTelFax(){
    String telNfax = "";
    boolean imaTelefon = false;
    if (!getLogoTel1().equals("") || !getLogoTel2().equals("")) {
      telNfax = "Tel ";
      imaTelefon = true;
      if (!getLogoTel1().equals("")) telNfax = telNfax+getLogoTel1();
      if (!getLogoTel2().equals("")){
        if (!getLogoTel1().equals("")){
          telNfax = telNfax+", "+getLogoTel2();
        } else {
          telNfax = telNfax+getLogoTel2();
        }
      }
    }
    if (!getLogoFax().equals("")) {
      if (imaTelefon){
        telNfax = telNfax+", Fax "+getLogoFax();
      } else {
        telNfax = "Fax "+getLogoFax();
      }
    }
    return telNfax;
  }

  public String getUSEROBRACUNAO(){
    String user = prijava.getString("CUSEROBRAC");
    dm.getUseri().open();
    if (lookupData.getlookupData().raLocate(dm.getUseri(),"CUSER",user)) return dm.getUseri().getString("NAZIV");
    return "";
  }

  public String getUSERPOSLOVAO(){
    String user = prijava.getString("CUSERPOSLOV");
    dm.getUseri().open();
    if (lookupData.getlookupData().raLocate(dm.getUseri(),"CUSER",user)) return dm.getUseri().getString("NAZIV");
    return "";
  }

  public String getSTATUS(){
    String s = prijava.getString("STATUS");
    if (s.equals("P")) return "PRIJAVLJEN";
    if (s.equals("O")) return "OBRAÈUNAT";
    if (s.equals("Z")) return "ZATVOREN";
    return "";
  }

  public String getKORISNIK(){
    return rm.getFirstLine();
  }

  public String getZiroMb(){
    String ziroNmb = "Žiro raèun <b>" + getLogoZiro() + "</b>, MB "+ getLogoMatbroj();
    return ziroNmb;
  }
  public String getLineName(){return ClassLoader.getSystemResource("hr/restart/robno/reports/line2.jpg").toString();}
  public String getLogoCorg(){return rm.getLogoCorg();}
  public String getLogoNazivlog(){return rm.getLogoNazivlog();}
  public String getLogoMjesto(){return rm.getLogoMjesto();}
  public String getLogoMjestoZarez(){return dm.getLogotipovi().getString("MJESTO")+",";}
  public String getLogoPbr(){return rm.getLogoPbr();}
  public String getLogoAdresa(){return rm.getLogoAdresa();}
  public String getLogoZiro(){return rm.getLogoZiro();}
  public String getLogoMatbroj(){return rm.getLogoMatbroj();}
  public String getLogoSifdjel(){return rm.getLogoSifdjel();}
  public String getLogoPorisp(){return rm.getLogoPorisp();}
  public String getLogoFax(){return rm.getLogoFax();}
  public String getLogoTel1(){return rm.getLogoTel1();}
  public String getLogoTel2(){return rm.getLogoTel2();}
}