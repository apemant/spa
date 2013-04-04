/****license*****************************************************************
**   file: repMxGRN.java
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

import hr.restart.util.Aus;
import hr.restart.util.reports.mxReport;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class repMxGRN extends mxReport {
  repMemo rm = repMemo.getrepMemo();
  repUtil ru = repUtil.getrepUtil();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
//  reportsQuerysCollector repQC = reportsQuerysCollector.getRQCModule();
  QueryDataSet ds;// = repQC.getQueryDataSet();
  String[] detail = new String[1];
  int width = 40;
  int dbWidth = width/2;
  String doubleLineSep;
  hr.restart.robno.sgQuerys sgq = hr.restart.robno.sgQuerys.getSgQuerys();

  public repMxGRN() {
//    initAndPrepare();
//    System.out.println("I S P I S___I N I C I R A N___I___S P R E M A N___Z A___I S P I S");
  }

  public void makeReport(){
    String wdt = hr.restart.sisfun.frmParam.getParam("pos", "sirPOSpr", "41", "Sirina pos ispisa. Preporuka 39 - 46",true);
    System.out.println("WDT - "+wdt);
    width = Integer.parseInt(wdt);
    System.out.println("WIDTH - "+ width);
    dbWidth = width/2;
    doubleLineSep = getDoubleLineLength();
    initAndPrepare();
    super.makeReport();
  }

  private void initAndPrepare() {
    ds = reportsQuerysCollector.getRQCModule().getQueryDataSet();
    ds.getColumn("KOL").setDisplayMask("###,###,##0.000");
    ds.open();
    
//    sysoutTEST s = new sysoutTEST(false);
//    s.prn(ds);

//    System.out.println("ds.getInt(\"BRDOK\") = " + ds.getInt("BRDOK"));
    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(ds);
//    syst.showInFrame(ds,"degeneracija");


     QueryDataSet sks = hr.restart.baza.Sklad.getDataModule().getTempSet("cskl = '"+ds.getString("CSKL")+"'");
     sks.open();

     String prodavaonica = sks.getString("NAZSKL");

     System.out.println("PRODAVAONICA : '" + prodavaonica + "'");

    rVR.rakapitulacija(ds);
    ru.setDataSet(ds);
    ds.first();

    //    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.showInFrame(ds,"degeneracija");
    //    syst.prn(ds);
    //    syst.prn(dm.getLogotipovi());

    setDataSet(ds);
    try{
      if(hr.restart.sisfun.frmParam.getParam("robno", "ispLogo", "N", "Ispis loga na dokumentima (D/N)").equals("D")){
        setPgHeader(
            "\u0007"+
            "<#"+rm.getLogoNazivlog()+"|"+width+"|center#><$newline$>"+
            "<#"+rm.getLogoAdresa()+"|"+width+"|center#><$newline$>"+
            "<#"+rm.getLogoPbr()+" "+rm.getLogoMjesto()+"|"+width+"|center#><$newline$>"+
            "<#OIB "+rm.getLogoOIB()+"|"+width+"|center#><$newline$>"+
            ((prodavaonica.equals("")) ? "" : "<#"+prodavaonica+"|"+width+"|center#><$newline$>")+
            findPrintString(ds.getInt("CKUPAC"))+
            "<$newline$>"+
            "\u000E<#"+racunString+"|"+((width-2)/2)+"|center#>\u0014<$newline$>"+ // br."+ds.getInt("BRDOK")+"|39|center#><$newline$>"+//"<#"+getRacunWithR1()+"|39|center#><$newline$>"+
            "\u000E<#"+getFormatBroj()+"|"+((width-2)/2)+"|center#>\u0014<$newline$>"+
            doubleLineSep+"<$newline$>"+
            Aut.getAut().getCARTdependable("RBR ŠIFRA   NAZIV<$newline$>",
                                           "RBR OZNAKA        NAZIV<$newline$>",
                                           "RBR BARCODE       NAZIV<$newline$>")+
            " KOLIÈINA   JM       CIJENA         "+getRazlikaWudthBlank()+"IZNOS<$newline$>"+
            doubleLineSep);

      } else{
        setPgHeader(""); /// iz navodnjaka: <$newline$><$newline$><$newline$><$newline$>
      }
    } catch(Exception ex){
      ex.printStackTrace();
    }
//    BigDecimal kalkul = ds.getBigDecimal("KOL").multiply(ds.getBigDecimal("FMCPRP"));

    detail[0] = Aut.getAut().getCARTdependable("<#RBR|3|right#> <#CART|7|left#> <#NAZART|"+(width-12)+"|left#><$newline$>",
                                               "<#RBR|3|right#> <#CART1|13|left#> <#NAZART|"+(width-18)+"|left#><$newline$>",
                                               "<#RBR|3|right#> <#BC|13|left#> <#NAZART|"+(width-18)+"|left#><$newline$>")+
                                               "<#KOL|9|right#>   <#JM|3|right#> <#FMCPRP|11|right#> <&calc(<#KOL#> * <#FMCPRP#>|"+(width-28)+"|right)&>";

//    detail[0] = Aut.getAut().getCARTdependable("<#RBR|3|right#> <#CART|7|left#> <#NAZART|29|left#><$newline$>",
//                                               "<#RBR|3|right#> <#CART1|13|left#> <#NAZART|23|left#><$newline$>",
//                                               "<#RBR|3|right#> <#BC|13|left#> <#NAZART|23|left#><$newline$>")+
//                                               "<#KOL|9|right#>   <#JM|3|right#> <#FMCPRP|11|right#> <&calc(<#KOL#> * <#FMCPRP#>|13|right)&>";
    setDetail(detail);

    setRepFooter(
        doubleLineSep+"<$newline$>"+
        getUkupnost(ds)+

//        "PLATITI                  <#UIRAC|16|right#><$newline$>"+
        doubleLineSep+"<$newline$>"+
        nacinPlacanja(ds.getInt("BRDOK"),ds.getString("CSKL"),ds.getString("VRDOK"))+
        getRekPor()+
        getBlagajnaOperater(ds.getString("CUSER"))+
        "<$newline$>"+getPotpis_i_MP(ds.getInt("CKUPAC"))+
        getFooting()+
        getDatumVrijeme() +
        "<$newline$><$newline$><$newline$>"+
        "<$newline$><$newline$><$newline$>"+
        "<$newline$><$newline$><$newline$>"+
//        "\u001B\u0069" // epson code
        "\u001B\u0064\u0000" // star code
        );
  }

  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  String racunString = "";

  private String findPrintString(int kupac) {
    String strSql;
    String kupacString;
    strSql="select * from kupci where ckupac='"+kupac+"'";
    vl.execSQL(strSql);
    vl.RezSet.open();
    if (vl.RezSet.rowCount() == 0) {
      kupacString = "";
      racunString = "RAÈUN";
    } else {
      racunString = "RAÈUN R-1";
      kupacString = "<$newline$>Kupac: "+
                    vl.RezSet.getString("IME")+" "+vl.RezSet.getString("PREZIME")+"<$newline$>"+
                    ((!vl.RezSet.getString("ADR").equals(""))?"       "+vl.RezSet.getString("ADR")+"<$newline$>":"")+
                    ((vl.RezSet.getInt("PBR")!=0)?"       "+vl.RezSet.getInt("PBR")+" ":"")+
                    ((!vl.RezSet.getString("MJ").equals(""))?"       "+vl.RezSet.getString("MJ")+"<$newline$>":"<$newline$>");
    }
    return kupacString;
  }

  private raVectorRekap rVR = new raVectorRekap();

  public String getFormatBroj(){
    return ru.getFormatBroj();
  }

  private String getRekPor() {
    String cVrati="";
    rVR.rakapitulacija(ds);
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(ds);
    DataSet porSet = rVR.getPoreziSet();
//    syst.prn(porSet);
    porSet.first();
    cVrati = "<#P R E G L E D  P O R E Z A|"+width+"|center#><$newline$>"+
             "<#NAZIV|6|left#> <#STOPA|8|right#> <#OSNOVICA|12|right#> <#POREZ|12|right#><$newline$>"+
             doubleLineSep+"<$newline$>";
    do {
      cVrati += "<#"+porSet.getString("CPOR")+"|6|left#> "+
               "<#"+sgq.format(porSet.getBigDecimal("UKUPOR").setScale(2, java.math.BigDecimal.ROUND_HALF_UP),2)+"%|8|right#> "+
               "<#"+sgq.format(porSet.getBigDecimal("IPRODBP").setScale(2, java.math.BigDecimal.ROUND_HALF_UP),2)+"|12|right#> "+
               "<#"+sgq.format(porSet.getBigDecimal("POR1").setScale(2,java.math.BigDecimal.ROUND_HALF_UP),2)+"|"+(width-29)+"|right#>"+
               "<$newline$>";
    } while (rVR.getPoreziSet().next());
    cVrati += doubleLineSep+"<$newline$>";
    return cVrati;
  }

  private String nacinPlacanja(int brdok, String cskl, String vrdok){
//    String nacini = "";
//    System.out.println("SELECT max(nacpl.naznacpl) as naznacpl, sum(rate.irata) as irata FROM rate,nacpl,banke "+
//                                              "WHERE rate.cnacpl = nacpl.cnacpl "+
//                                              "and rate.brdok = " + brdok + " and rate.vrdok = '"+vrdok+"' and rate.god='"+ Aut.getAut().getKnjigodRobno() +
//                                              "' and rate.cskl= '" + cskl + "' group by naznacpl");


    QueryDataSet npos = hr.restart.util.Util.getUtil().getNewQueryDataSet("SELECT nacpl.naznacpl, sum(rate.irata) as irata FROM rate,nacpl "+
                                              "WHERE rate.cnacpl = nacpl.cnacpl "+
                                              "and rate.brdok = " + brdok + " and rate.vrdok = '"+vrdok+"' and rate.god='"+ Aut.getAut().getKnjigodRobno() +
                                              "' and rate.cskl= '" + cskl + "' group by nacpl.naznacpl");
    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
    syst.prn(npos);
    npos.first();

    String np = "<#P R E G L E D  P L A Æ A N J A|"+width+"|center#><$newline$>"+
//                  "<#NAÈINA PLAÆANJA|21|left#>               IZNOS<$newline$>"+
    doubleLineSep+"<$newline$>";
    do {
      np +=  "<#"+npos.getString("NAZNACPL").toUpperCase()+"|21|left#> <#"+sgq.format(npos.getBigDecimal("IRATA"),2)+"|"+(width-22)+"|right#><$newline$>";
    } while (npos.next());

    np += doubleLineSep+"<$newline$>";

    return np; //npos.getString("NAZNACPL").toUpperCase();
  }

  private String getDatumVrijeme() {
    String dtmvr = "Datum: "+
                   raDateUtil.getraDateUtil().dataFormatter(ds.getTimestamp("DATDOK"))+
                   "    "+getRazlikaWudthBlank()+"Vrijeme: " + ds.getTimestamp("DATDOK").toString().substring(11,19) +
                   "<$newline$><$newline$>";
    return dtmvr;
  }

  private String getFooting(){
    String sadrzaj = "Hvala na povjerenju";
    String footing = "";
    if (!sadrzaj.equals("")){
      footing = "<#"+sadrzaj+"|"+width+"|center#><$newline$><$newline$>";
    }
    return footing;
  }

  private String getUkupnost(QueryDataSet qds){

//    System.out.println("UIRAB : " + qds.getBigDecimal("UIRAB"));
//    if ((qds.getBigDecimal("UIRAB")).compareTo(Aus.zero2) == 0){
//      return "<#PLATITI |26|left#> <#"+qds.getBigDecimal("UIRAC")+"|15|right#><$newline$>";
//    } else {
      BigDecimal ukupno = Aus.zero2;
      qds.first();
      do {
        ukupno = ukupno.add(qds.getBigDecimal("KOL").multiply(qds.getBigDecimal("FMCPRP")));
      } while (qds.next());
      ukupno = ukupno.setScale(2,BigDecimal.ROUND_HALF_UP);

      System.out.println("POPUST : " + ukupno.subtract(qds.getBigDecimal("UIRAC")));

      if (ukupno.subtract(qds.getBigDecimal("UIRAC")).compareTo(Aus.zero2) == 0)

        return
            "<#PLATITI |26|left#> <#"+sgq.format(qds.getBigDecimal("UIRAC"),2)+"|"+(width-26)+"|right#><$newline$>";
      	return
          "<#UKUPNO |26|left#> <#"+sgq.format(ukupno,2)+"|"+(width-26)+"|right#><$newline$>"+
          "<#POPUST |26|left#> <#"+sgq.format(ukupno.subtract(qds.getBigDecimal("UIRAC")),2)+"|"+(width-26)+"|right#><$newline$>"+
          "<#PLATITI |26|left#> <#"+sgq.format(qds.getBigDecimal("UIRAC"),2)+"|"+(width-26)+"|right#><$newline$>";
//    }
  }

  private String getPotpis_i_MP(int ckupac){
    if (ckupac != 0 && hr.restart.sisfun.frmParam.getParam("pos","potpisMP","D","Mjesto za peèat i potpis na POS raèunu").equalsIgnoreCase("D"))
      return "<$newline$><#MP|"+width+"|center#><$newline$>"+
             "<#_______________________________________|"+width+"|center#><$newline$>"+"<$newline$>"+"<$newline$>";

    return "";
  }

  private String getBlagajnaOperater(String user){
    String blop = hr.restart.sisfun.frmParam.getParam("pos","BlOp","0","Ispis i pozicija blagajne i operatora na malim raèunima (0,1,2)");
    if (!blop.equalsIgnoreCase("0")){
      DataRow usr = hr.restart.util.lookupData.getlookupData().raLookup(hr.restart.baza.dM.getDataModule().getUseri(),"CUSER", user);
      String operater = usr.getString("NAZIV");
      if (blop.equalsIgnoreCase("1")){
        return
               "OPERATER: "+operater+"<$newline$>";
      } else {
        return operater+"<$newline$>";
      }
    }
    return "";
  }

  
  private String getDoubleLineLength(){
   String dl = "";
   for (int i=1; i <= width; i++){
     dl += "=";
   }
   return dl;
  }
  
  private String getRazlikaWudthBlank(){
    String bl = "";
    for (int i=0; i <(width-41); i++){
      bl += " ";
    }
    return bl;
  }

}
