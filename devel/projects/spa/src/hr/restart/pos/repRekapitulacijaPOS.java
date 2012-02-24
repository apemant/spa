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
import hr.restart.util.lookupData;
import hr.restart.util.reports.mxReport;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;
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
    this.setPgHeader("<$newline$>"+getDoubleLineLength()+"<$newline$>"+
                     "<#T O T A L|"+width+"|center#>"+
                     "<$newline$>"+getDoubleLineLength()+"<$newline$>"+
                     z3+
                     z2+
                     getDoubleLineLength()+"<$newline$>"+
                     z1);
    
    int artz = Aus.getAnyNumber(frmParam.getParam("pos", "artSir", "4",
        "Broj znamenki �ifre artikla na ispisu rekapitulacije"));
    if (artz < 0) artz = 3;
    if (artz > 20) artz = 20;

    detail[0] = "<#"+Aut.getAut().getCARTdependable("CART","CART1","BC")+
                "|"+artz+"|left#> <#NAZART|"+(width-18-artz)
                +"|left#> <#KOL|6|right#><#NETO|10|right#>";

    if (irrpos.getRekapitulacijaPoAretiklima()) this.setDetail(detail);

    this.setPgFooter(footer()+
                     "<$newline$><$newline$><$newline$>"+
                     "<$newline$><$newline$><$newline$>"+
                     "\u001B\u0064\u0000"

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
      z1+="<$newline$>";
      for (por.first(); por.inBounds(); por.next())
        z1+="<$newline$><#"+por.getString("NAZIV")+"|10|left#> <#"+
        sgq.format(por.getBigDecimal("IZNOS"),2)+"|"+
            (width-11)+"|right#>";
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
      }/*else */if (ts.getString("CNACPL").equals("�")){
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
        } else if (/*ts.goToRow(ts.getRow()-1) && */!ts.getString("CNACPL").equals("�")){
//          System.out.println("Ukupno cekova ima jos..."); //XDEBUG delete when no more needed
//          ts.goToRow(ts.getRow()-1);
          vrati +=getSinglLineLength()+"<$newline$>";
          vrati +="<#UKUPNO "+ts.getString("NACPL")+"|27|left#> <#"+sgq.format(ukucek,2)+"|"+(width-28)+"|right#><$newline$><$newline$>";
        }
        ts.goToRow(ts.getRow()-1);
//        if (ts.goToRow(ts.getRow()+1) && !ts.getString("CNACPL").equals("�")){
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
