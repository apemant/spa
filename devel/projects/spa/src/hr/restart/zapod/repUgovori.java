/****license*****************************************************************
**   file: repUgovori.java
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

import hr.restart.robno.Aut;
import hr.restart.robno.raControlDocs;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.robno.reportsQuerysCollector;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repUgovori implements raReportData {

  protected DataSet ds;
  repMemo rm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();
  protected hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  reportsQuerysCollector repQC = reportsQuerysCollector.getRQCModule();
  protected String[] colname = new String[] {""};
  protected repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  protected raControlDocs rCD = new raControlDocs();
  protected hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();
  
  
  protected String lastDok = null;
  
  frmUgovori fuj = frmUgovori.getInstance();
  
  
  
  public void close() {
    ru.setDataSet(null);
    ds = null;
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.getRowCount();
  }
  
  private static String domval;

  public repUgovori(){
    domval = hr.restart.util.Util.getNewQueryDataSet(
        "select oznval from VALUTE where strval='N' ", true).getString(
        "OZNVAL");
    ds = fuj.getRepSet();
    ru.setDataSet(ds);
  }
  
  public String getBRDOK(){
    return ds.getString("CUGOVOR");
  }
  
  public String getFormatBroj(){
    return ds.getString("CUGOVOR");
  }

  public int getCPAR() {
    return ds.getInt("CPAR");
  }

  public String getADR() {
    String adr = "";
    if (ds.getInt("PJ") > 0 &&
        (hr.restart.sisfun.frmParam.getParam("robno","ispisPJ","D","Ispis poslovne jedinice na ROT-u (D-u adresi, I-kao isporuka, O-na oba mjesta, N-bez P.J.)").equalsIgnoreCase("D") ||
         hr.restart.sisfun.frmParam.getParam("robno","ispisPJ").equalsIgnoreCase("O"))){
      lookupData.getlookupData().raLocate(dm.getPjpar(),
          new String[] {"CPAR","PJ"},
          new String[] {ds.getInt("CPAR")+"",ds.getInt("PJ")+""});
      adr += dm.getPjpar().getString("ADRPJ");
    } else {
      if (lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",ds.getInt("CPAR")+""))
        adr = dm.getPartneri().getString("ADR");
      else
        adr = "";
    }
    return adr;
  }

  public String getMJ() {
    colname[0] = "CPAR";
    String mj = "";
    if (ds.getInt("PJ") > 0 &&
        (hr.restart.sisfun.frmParam.getParam("robno","ispisPJ","D","Ispis poslovne jedinice na ROT-u (D-u adresi, I-kao isporuka, O-na oba mjesta, N-bez P.J.)").equalsIgnoreCase("D") ||
         hr.restart.sisfun.frmParam.getParam("robno","ispisPJ").equalsIgnoreCase("O"))){
      lookupData.getlookupData().raLocate(dm.getPjpar(),
          new String[] {"CPAR","PJ"},
          new String[] {ds.getInt("CPAR")+"",ds.getInt("PJ")+""});
      if (dm.getPjpar().getInt("PBRPJ") >0)  mj = dm.getPjpar().getInt("PBRPJ") + " " + dm.getPjpar().getString("MJPJ");
      else mj = dm.getPjpar().getString("MJPJ");
    } else {
      if (!lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",ds.getInt("CPAR")+"")) return "";
      if (dm.getPartneri().getInt("PBR") >0)  mj = dm.getPartneri().getInt("PBR") + " " + dm.getPartneri().getString("MJ");
      else mj = dm.getPartneri().getString("MJ");
    }    
    return mj;
  }

  public String getMB() {
    String mb = "MB ";
    colname[0] = "CPAR";    
    if (ru.getSomething(colname,dm.getPartneri(),"MB").getString().equalsIgnoreCase("")) mb = "";
    return mb+ru.getSomething(colname,dm.getPartneri(),"MB").getString();
  }

  public String getCART() {
    String izlaz = Aut.getAut().getIzlazCARTdep(ds);
    if (hr.restart.sisfun.frmParam.getParam("robno","chForSpecial","N","Provjerava specijalne znakove (*,_,+,-) i odrezuje njih plus sve iza njih").equalsIgnoreCase("D")){
      if (Aut.getAut().getIzlazCART().equalsIgnoreCase("BC") && (izlaz.indexOf("*") > 0 || izlaz.indexOf("_") > 0 || izlaz.indexOf("+") > 0 || izlaz.indexOf("-") > 0)){
        int index = 13;
        if (izlaz.indexOf("*") > 0) index = izlaz.indexOf("*");
        if (izlaz.indexOf("_") > 0) index = izlaz.indexOf("_");
        if (izlaz.indexOf("+") > 0) index = izlaz.indexOf("+");
        if (izlaz.indexOf("-") > 0) index = izlaz.indexOf("-");
        
        izlaz = izlaz.substring(0,index);
      }
    }
    return izlaz;
  }
  
  public short getRBR() {
    return ds.getShort("RBR");
  }

  public String getNAZART() {
    return ds.getString("NAZART");
  }

  public String getJM() {
    return ds.getString("JM");
  }

  public BigDecimal getKOL() {
    return ds.getBigDecimal("KOL");
  }

  public BigDecimal getUPRAB() {
    return ds.getBigDecimal("UPRAB");
  }

  public String getLogoMjestoZarez(){
    return rm.getLogoMjesto();
  }

  public String SgetDATDOK() {
    return rdu.dataFormatter(ds.getTimestamp("DATUGOVOR"));
  }
  
  public String getVALUTA(){
   String valuta = domval; //"KN";
   if (ds.getBigDecimal("VAL_VC").compareTo(Aus.zero2) > 0) valuta = ds.getString("OZNVAL"); 
   return valuta;
  }
  
  public BigDecimal getFVC(){
    BigDecimal cijena = ds.getBigDecimal("FVC");
    if (ds.getBigDecimal("VAL_VC").compareTo(Aus.zero2) > 0) cijena = ds.getBigDecimal("VAL_VC");
    return cijena;
  }
  
  public BigDecimal getIZNOS(){
    BigDecimal iznos = ds.getBigDecimal("IPRODBP");
    if (ds.getBigDecimal("VAL_VC").compareTo(Aus.zero2) > 0) iznos = ds.getBigDecimal("KOL").multiply(ds.getBigDecimal("VAL_VC"));
    return iznos;
  }

  public String getNAZNACPL() {
    colname[0] = "CNACPL";
    return ru.getSomething(colname,dm.getNacpl(),"NAZNACPL").getString();
  }

  public String getNAZNAC() {
    colname[0] = "CNAC";
    return ru.getSomething(colname,dm.getNacotp(),"NAZNAC").getString();
  }

  public String getNAZFRA() {
    colname[0] = "CFRA";
    return ru.getSomething(colname,dm.getFranka(),"NAZFRA").getString();
  }

  public String getNAMJENA() {
    colname[0] = "CNAMJ";
    return ru.getSomething(colname,dm.getNamjena(),"NAZNAMJ").getString();
  }

  public String getNAZPAR() {    
    colname[0] = "CPAR";
    return ru.getSomething(colname,dm.getPartneri(),"NAZPAR").getString();
  }
  
  public String getOPIS(){
    return ds.getString("OPIS");
  }
  
  public int getLogoPotvrda() {
    if (rm.test()) return 1;
    return 0;
  }
  
  public String getLogoFullAdr() {
    if (!rm.test()) return "";
    else return (rm.getLogoAdresa() + " ,  " + rm.getLogoPbr() + " " + rm.getLogoMjesto());
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

  public String getLogoCorg(){return rm.getLogoCorg();}
  public String getLogoNazivlog(){return rm.getLogoNazivlog();}
  public String getLogoMjesto(){  return rm.getLogoMjesto();}
  public String getLogoPbr(){return rm.getLogoPbr();}
  public String getLogoAdresa(){return rm.getLogoAdresa();}
  public String getLogoZiro(){return   rm.getLogoZiro();}
  public String getLogoMatbroj(){return rm.getLogoMatbroj();}
  public String getLogoSifdjel(){return rm.getLogoSifdjel();}
  public String getLogoPorisp(){return rm.getLogoPorisp();}
  public String getLogoFax(){return rm.getLogoFax();}
  public String getLogoTel1(){return rm.getLogoTel1();}
  public String getLogoTel2(){return rm.getLogoTel2();}

  
}
