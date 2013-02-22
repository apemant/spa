/****license*****************************************************************
**   file: repRekapitulacijaPOS.java
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
package hr.restart.pos;

import hr.restart.robno.Aut;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.reports.mxRM;
import hr.restart.util.reports.mxReport;
import hr.restart.zapod.OrgStr;

import java.math.BigDecimal;
import java.util.StringTokenizer;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;


public class repRekapitulacijaPOS extends mxReport {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();
  hr.restart.robno.ispRekapitulacijaRacunaPOS irrpos = hr.restart.robno.ispRekapitulacijaRacunaPOS.getInstance();
  String[] detail = new String[1];
  hr.restart.robno.sgQuerys sgq = hr.restart.robno.sgQuerys.getSgQuerys();


  String z1,z2,z3;
  java.math.BigDecimal s1;

  int width = 40;

  public repRekapitulacijaPOS() {}

  public void makeReport(){
    String wdt = hr.restart.sisfun.frmParam.getParam("pos", "sirPOSpr", "41", "Sirina pos ispisa. Preporuka 39 - 46",true);
    width = Integer.parseInt(wdt);
//    System.out.println("WIDTH - "+ width);
    dataSet();
    makeLittleZaglavlje();
    makeIspis();
    super.makeReport();
  }

  private void makeIspis(){
    
    String pcorg = frmParam.getParam("pos", "posCorg", "",
    "OJ za logotip na POS-u");
    if (pcorg == null || pcorg.length() == 0)
      pcorg = OrgStr.getKNJCORG(false);
    
    dm.getLogotipovi().open();
    
    lD.raLocate(dm.getLogotipovi(), "CORG", pcorg);
    
    String kh = getNazivSplit(dm.getLogotipovi().getString("NAZIVLOG"))+
    "<#"+dm.getLogotipovi().getString("ADRESA")+ ", " +String.valueOf(dm.getLogotipovi().getInt("PBR"))+" "+dm.getLogotipovi().getString("MJESTO") +"|"+width+"|center#><$newline$>"+
    "<#OIB "+dm.getLogotipovi().getString("OIB")+"|"+width+"|center#><$newline$>"+ getPhones();
    

    QueryDataSet sks = hr.restart.baza.Sklad.getDataModule().getTempSet("cskl = '"+irrpos.getCSKL()+"'");
    sks.open();
    
    String ph = kh;
    if (!sks.getString("CORG").equals(OrgStr.getKNJCORG(false)) &&
        lD.raLocate(dm.getLogotipovi(), "CORG", sks.getString("CORG"))) {
      ph = getNazivSplit(dm.getLogotipovi().getString("NAZIVLOG"))+
      "<#"+dm.getLogotipovi().getString("ADRESA")+ ", " +String.valueOf(dm.getLogotipovi().getInt("PBR"))+
      " "+dm.getLogotipovi().getString("MJESTO") +"|"+width+"|center#><$newline$>"+ 
      (dm.getLogotipovi().getString("OIB").length()== 0 ? "" : "<#OIB "+
          dm.getLogotipovi().getString("OIB")+"|"+width+"|center#><$newline$>")+ getPhones();
    }

    String prep = frmParam.getParam("pos", "addHeader", "",
        "Dodatni header ispred POS raèuna", true);
    
    if (prep.length() > 0) {
      String[] parts = new VarStr(prep).split('|');
      VarStr buf = new VarStr();
      for (int i = 0; i < parts.length; i++)
        buf.append("<#").append(parts[i]).append('|').
          append(width).append("|center#><$newline$>");
      prep = buf.toString();
    }
    
    String th = frmParam.getParam("pos", "posHeader", "",
        "POS header (1 - poslovnica, knjigovodstvo  2 - obrnuto, ostalo - samo knjigovodstvo)");
    String header = prep + kh;
    if (th.equals("1") && !kh.equals(ph))
      header = ph + kh;
    if (th.equals("2") && !kh.equals(ph))
      header = kh + ph;
    
    String vc = hr.restart.sisfun.frmParam.getParam(
        "sisfun", "printerRMcmnd", "1", "Radno mjesto", true);
    lD.raLocate(dm.getMxPrinterRM(), "CRM", vc);
    mxRM rm = new mxRM();
    rm.init(dm.getMxPrinterRM());
    setRM(rm);
    
    
    this.setPgHeader("<$newline$>"+header+"<$newline$>"+getDoubleLineLength()+"<$newline$>"+
                     "<#T O T A L|"+width+"|center#>"+
                     "<$newline$>"+getDoubleLineLength()+"<$newline$>"+
                     z3+
                     z2+
                     getDoubleLineLength()+"<$newline$>"+
                     z1);
    
    int artz = Aus.getAnyNumber(frmParam.getParam("pos", "artSir", "4",
        "Broj znamenki šifre artikla na ispisu rekapitulacije"));
    if (artz < 0) artz = 3;
    if (artz > 20) artz = 20;

    detail[0] = "<#"+Aut.getAut().getCARTdependable("CART","CART1","BC")+
                "|"+artz+"|left#> <#NAZART|"+(width-18-artz)
                +"|left#> <#KOL|6|right#><#NETO|10|right#>";

    if (irrpos.getRekapitulacijaPoAretiklima()) this.setDetail(detail);

    this.setPgFooter(footer()+
                     "<$newline$><$newline$><$newline$>"+
                     "<$newline$><$newline$><$newline$>"+
                     getLastEscapeString()

                     /*"=========================================<$newline$>"/*+
                     "<#UKUPNO|20|left#> <%sum(IRATA|20|right)%><$newline$>"+ // IRATA se stalno zbraja!!!!????
                     "=========================================<$newline$>"*/);
  }

  private void dataSet(){
    if (irrpos.getRekapitulacijaPoAretiklima())this.setDataSet((DataSet)irrpos.getWhatSoEverArtikli());
    else this.setDataSet((DataSet)irrpos.getWhatSoEver());
//    System.out.println("SETIRAM DATASET");
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(this.getDataSet());
  }
  
  private String getNazivSplit(String naziv) {
    if (naziv.indexOf('|') <= 0) return    
      "<#"+naziv+"|"+width+"|center#><$newline$>";
    
    String[] lines = new VarStr(naziv).split('|');
    String ret = "";
    for (int i = 0; i < lines.length; i++)
      ret = ret + "<#"+lines[i]+"|"+width+"|center#><$newline$>";
    
    return ret;
  }
  
  private String getPhones(){
    if (dm.getLogotipovi().getString("TEL1").equals("")) return "";
    String phoneString = "<#Tel. ";
    if (!dm.getLogotipovi().getString("TEL1").equals(""))
    phoneString += dm.getLogotipovi().getString("TEL1");
    if (!dm.getLogotipovi().getString("TEL2").equals(""))
      if (dm.getLogotipovi().getString("TEL1").equals(""))
        phoneString += dm.getLogotipovi().getString("TEL2");
      else
        phoneString += ", "+dm.getLogotipovi().getString("TEL2");
   return phoneString+"|"+width+"|center#><$newline$>"; 
  }
  
  private String getLastEscapeString() {
    int crm = dm.getMxPrinterRM().getInt("CRM");//jebiga
    String str = frmParam.getParam("sisfun", "endPOSRM"+crm, "\\u001B\\u0064\\u0000", 
        "Sekvenca koja dolazi na kraju ispisa POS racuna za rm "+crm);
    return getParamStr(str);
  }
  
  private String getParamStr(String str) {
    //String str = frmParam.getParam("sisfun", "endPOSRM"+crm, "\\u001B\\u0064\\u0000", "Sekvenca koja dolazi na kraju ispisa POS racuna za rm "+crm);
  //String str = "\\u0041\\u004e\\u0044\\u0052\\u0045\\u004a";
        try {
          StringTokenizer tok = new StringTokenizer(str,"\\u");
          char[] ret = new char[tok.countTokens()];
          int i=0;
          while (tok.hasMoreTokens()) {
            ret[i] = (char)Integer.parseInt(tok.nextToken(), 16);
            i++;
          }
          return new String(ret);
        } catch (NumberFormatException e) {
          e.printStackTrace();
          return "";
        }
  }

  private void makeLittleZaglavlje(){
    z1 = "";

    s1 = new java.math.BigDecimal("0.00");
    QueryDataSet ts = irrpos.getWhatSoEver();
    ts.first();
    do {
//      z1+="<#"+ts.getString("NACPL")+"|20|left#> <#"+ts.getBigDecimal("IZNOS")+"|"+(width-21)+"|right#><$newline$>";
      s1 = s1.add(ts.getBigDecimal("IZNOS"));
    } while (ts.next());
    
    z1 += getRekapitulacijuPoNacinimaPlacanja(ts);
    
    z1+=getDoubleLineLength()+"<$newline$>"+
        "<#SVEUKUPNO|20|left#> <#"+sgq.format(s1,2)+"|"+(width-21)+"|right#>";
    
    DataSet por = irrpos.getPorezi();
    if (por.rowCount() > 0) {
      
      por.setSort(new SortDescriptor(new String[] {"NAZIV"}));
      z1 += "<$newline$><$newline$><#P R E G L E D  P O R E Z A|"+width+"|center#><$newline$>"+
        "<#NAZIV|6|left#> <#STOPA|8|right#> <#OSNOVICA|12|right#> <#POREZ|"+(width-29)+"|right#><$newline$>"+
        getDoubleLineLength()+"<$newline$>";

      for (por.first(); por.inBounds(); por.next()) {    
          z1 += "<#"+por.getString("NAZIV")+"|6|left#> <#"+Aus.formatBigDecimal(por.getBigDecimal("STOPA"))+"%|8|right#> <#"+
          Aus.formatBigDecimal(por.getBigDecimal("OSNOVICA"))+"|12|right#> <#"+
          Aus.formatBigDecimal(por.getBigDecimal("IZNOS"))+"|"+(width-29)+"|right#>"+ "<$newline$>";
      }      
      
      if (irrpos.izpov.signum() > 0) {
        z1 += "<$newline$>POVRATNA NAKNADA<#" + Aus.formatBigDecimal(irrpos.izpov) + "|" + (width - 16) + "|right#><$newline$>";
      }
      /*z1+="<$newline$>";
      for (por.first(); por.inBounds(); por.next())
        z1+="<$newline$><#"+por.getString("NAZIV")+"|10|left#> <#"+
        sgq.format(por.getBigDecimal("IZNOS"),2)+"|"+
            (width-11)+"|right#>";*/
    }
    
    QueryDataSet grs = irrpos.getGrupeArt();
    if (grs != null && grs.rowCount() > 0) {
      z1+="<$newline$>"+getDoubleLineLength();
      for (grs.first(); grs.inBounds(); grs.next()) {
        z1+="<$newline$><#"+grs.getString("OPIS")+"|20|left#> <#"+
          sgq.format(grs.getBigDecimal("IZNOS"),2)+"|"+
          (width-21)+"|right#>";
      }
    }

    if(irrpos.getRekapitulacijaPoAretiklima()){
      z1 += "<$newline$>"+getDoubleLineLength()+"<$newline$>"+
          "<#REKAPITULACIJA PO ARTIKLIMA|"+width+"|center#><$newline$>"+
          getSinglLineLength()+"";
    }
    if (irrpos.datumskiPeriod())
      z2 = "<#"+irrpos.getPocetniDatum()+" DO "+irrpos.getZavrsniDatum()+"|"+width+"|center#><$newline$>"+
           "<#"+irrpos.getPrviBroj()+" DO "+irrpos.getZadnjiBroj()+"|"+width+"|center#><$newline$>";
    else
      z2 = "<#"+irrpos.getPrviBroj()+" DO "+irrpos.getZadnjiBroj()+"|"+width+"|center#><$newline$>"+
           "<#"+irrpos.getPocetniDatum()+" DO "+irrpos.getZavrsniDatum()+"|"+width+"|center#><$newline$>";
    
    if (!irrpos.getBlagajnik().equals(""))
      z3 = "<#Blagajnik "+irrpos.getBlagajnik()+"|"+width+"|center#><$newline$>";
    else 
      z3 = "";

  }
  
  private String getRekapitulacijuPoNacinimaPlacanja(QueryDataSet ts){
    String vrati = "";
    ts.first();
    BigDecimal ukucek = Aus.zero2;
    BigDecimal ukukar = Aus.zero2;
    
    do {
      if (ts.getString("CNACPL").equals("G"))
        vrati +="<#UKUPNO "+ts.getString("NACPL")+"|20|left#> <#"+sgq.format(ts.getBigDecimal("IZNOS"),2)+"|"+(width-21)+"|right#><$newline$><$newline$>";

      if (ts.getString("CNACPL").equals("R"))
        vrati +="<#UKUPNO "+ts.getString("NACPL")+"|25|left#> <#"+sgq.format(ts.getBigDecimal("IZNOS"),2)+"|"+(width-26)+"|right#><$newline$><$newline$>";
      
      if (ts.getString("CNACPL").equals("V"))
        vrati +="<#UKUPNO "+ts.getString("NACPL")+"|25|left#> <#"+sgq.format(ts.getBigDecimal("IZNOS"),2)+"|"+(width-26)+"|right#><$newline$><$newline$>";
      
      if (ts.getString("CNACPL").startsWith("K") && ts.getString("CNACPL").length() > 1)
        vrati +="<#UKUPNO "+ts.getString("NACPL")+"|25|left#> <#"+sgq.format(ts.getBigDecimal("IZNOS"),2)+"|"+(width-26)+"|right#><$newline$><$newline$>";
      
      /*else */if (ts.getString("CNACPL").equals("K")){
//        System.out.println("Kartice"); //XDEBUG delete when no more needed
//        System.out.println("BANKA _ "+ts.getString("CBANKA"));
        lookupData.getlookupData().raLocate(dm.getKartice(), "CBANKA", ts.getString("CBANKA"));
        vrati += "<#* " + dm.getKartice().getString("NAZIV") + "|25|left#> <#" + sgq.format(ts.getBigDecimal("IZNOS"),2) + "|" + (width - 26) + "|right#><$newline$>";
        ukukar = ukukar.add(ts.getBigDecimal("IZNOS"));
        boolean goToRow = ts.goToRow(ts.getRow()+1);
        if (!goToRow){
//          System.out.println("Ukupno kartica zadnji red"); //XDEBUG delete when no more needed
          vrati +=getSinglLineLength()+"<$newline$>";
          vrati +="<#UKUPNO "+ts.getString("NACPL")+"|27|left#> <#"+sgq.format(ukukar,2)+"|"+(width-28)+"|right#><$newline$><$newline$>";
          break;
        } else if (/*ts.goToRow(ts.getRow()-1) && */!ts.getString("CNACPL").equals("K")){
          ts.goToRow(ts.getRow()-1);
//          System.out.println("Ukupno kartica, ima jos..."); //XDEBUG delete when no more needed
//          ts.goToRow(ts.getRow()-1);
          vrati +=getSinglLineLength()+"<$newline$>";
          vrati +="<#UKUPNO "+ts.getString("NACPL")+"|27|left#> <#"+sgq.format(ukukar,2)+"|"+(width-28)+"|right#><$newline$><$newline$>";
          ts.goToRow(ts.getRow()+1);
        }
        ts.goToRow(ts.getRow()-1);
      }/*else */if (ts.getString("CNACPL").equals("È")){
//        System.out.println("cekovi"); //XDEBUG delete when no more needed
        lookupData.getlookupData().raLocate(dm.getBanke(), "CBANKA", ts.getString("CBANKA"));
        vrati += "<#* " + dm.getBanke().getString("NAZIV") + "|25|left#> <#" + sgq.format(ts.getBigDecimal("IZNOS"),2) + "|" + (width - 26) + "|right#><$newline$>";
        ukucek = ukucek.add(ts.getBigDecimal("IZNOS"));
        boolean goToRow = ts.goToRow(ts.getRow()+1);
        if (!goToRow){
//          System.out.println("Ukupno cekova zadnji red"); //XDEBUG delete when no more needed
          vrati +=getSinglLineLength()+"<$newline$>";
          vrati +="<#UKUPNO "+ts.getString("NACPL")+"|27|left#> <#"+sgq.format(ukucek,2).trim()+"|"+(width-28)+"|right#><$newline$><$newline$>";
          break;
        } else if (/*ts.goToRow(ts.getRow()-1) && */!ts.getString("CNACPL").equals("È")){
//          System.out.println("Ukupno cekova ima jos..."); //XDEBUG delete when no more needed
//          ts.goToRow(ts.getRow()-1);
          vrati +=getSinglLineLength()+"<$newline$>";
          vrati +="<#UKUPNO "+ts.getString("NACPL")+"|27|left#> <#"+sgq.format(ukucek,2)+"|"+(width-28)+"|right#><$newline$><$newline$>";
        }
        ts.goToRow(ts.getRow()-1);
//        if (ts.goToRow(ts.getRow()+1) && !ts.getString("CNACPL").equals("È")){
//          ts.goToRow(ts.getRow()-1);
//          vrati +="<#UKUPNO "+ts.getString("NACPL")+"|20|left#> <#"+ukucek+"|"+(width-21)+"|right#><$newline$>";
//        }
      } 
    } while (ts.next());
//    System.out.println(vrati);
    
    return vrati; 
  }

  private String footer(){
    String rgp = getRekapGrupePorez();
    if (irrpos.getRekapitulacijaPoAretiklima()){
      BigDecimal kol = Aus.sum("KOL", getDataSet());
      rgp = getSinglLineLength()+"<$newline$>Ukupno kolièina:  " + sgq.format(kol, 3) + "<$newline$><$newline$>" + rgp;
      
      if (rgp.length() > 0) return rgp;
      return getDoubleLineLength()+"<$newline$><$newline$>";
    }
    return rgp;
  }
  
  private String getRekapGrupePorez() {
    DataSet gr = irrpos.getGrupePor();
    if (gr == null || gr.rowCount() < 2) return "";
    String ms = width >= 44 ? " " : "";
    int ss = width >= 44 ? 11 : 10;
    
    String ret = getDoubleLineLength() + "<$newline$>" +
         "GRUPA ARTIKALA (PODGRUPA)<$newline$>" + 
         Aus.spc(width - 8 - ss * 3) + 
         "OSNOVICA" + ms + "       PDV" + ms + 
         "      PNP" + ms  +"     UKUPNO<$newline$>"+
       getSinglLineLength() + "<$newline$>";
    for (gr.first(); gr.inBounds(); gr.next()) {
      if (gr.getString("CGRART").length() == 0)
        ret = ret + getSinglLineLength() + "<$newline$>";
      else ret = ret + gr.getString("NAZGRART") + "<$newline$>";
      
      ret = ret + "<#" + sgq.format(gr.getBigDecimal("OSNOVICA"), 2) 
                + "|" + (width - ss * 3) + "|right#><#" +
         sgq.format(gr.getBigDecimal("PDV"), 2) + "|" + ss + "|right#><#" +
         sgq.format(gr.getBigDecimal("PNP"), 2) + "|" + (ss - 1) + "|right#><#" +
         sgq.format(gr.getBigDecimal("IZNOS"), 2) + "|" + (ss + 1) + "|right#><$newline$>";
    }
    return ret+"<$newline$><$newline$>";
  }
  
  private String getDoubleLineLength(){
   String dl = "";
   for (int i=1; i <= width; i++){
     dl += "=";
   }
   return dl;
  }
  
  private String getSinglLineLength(){
   String dl = "";
   for (int i=1; i <= width; i++){
     dl += "-";
   }
   return dl;
  }

}
