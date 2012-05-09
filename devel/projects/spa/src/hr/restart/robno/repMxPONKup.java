/****license*****************************************************************
**   file: repMxPONKup.java
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

import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.reports.mxPrinter;
import hr.restart.util.reports.mxRM;
import hr.restart.util.reports.mxReport;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class repMxPONKup extends mxReport {
  public QueryDataSet ds;
  raDateUtil rdu = raDateUtil.getraDateUtil();
  reportsQuerysCollector repQC = reportsQuerysCollector.getRQCModule();
  private QueryDataSet detailDS = new QueryDataSet();
  lookupData ld = lookupData.getlookupData();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  BigDecimal sumaBP = Aus.zero2;
  BigDecimal sumaSP = Aus.zero2;
  BigDecimal sumaPOR = Aus.zero2;
  BigDecimal sumaIneto = Aus.zero2;
  int brRedDetail =0;
  int brRedPartner =0;
  int brRedHeader = 6;
  int brRedPGHeader = 5;
  int brRedFooter = 3;
  int brRedRekap = 0;
  int brRedRekPl = 4;

  ArrayList nacinPlacanja,rata,datumNaplate ;
  dM dm = dM.getDataModule();
  public repMxPONKup() {
    try
    {
      init();
    }
    catch (Exception ex) {}

  }

  public void init() throws Exception
  {
    ds = reportsQuerysCollector.getRQCModule().getQueryDataSet();
    setDataSet(detailDS);
    nacPl();
//    mxRM matIsp = mxRM.getDefaultMxRM();
//
//    String prntcmd = "cmd /c C:\\util\\hrconv 2 4 < \""+System.getProperty("user.dir")+"\\#\" > \"\\\\Grga\\EPSON\"";
//    matIsp.setPrintCommand(prntcmd);

///////////////////////////
    QueryDataSet printerRM = hr.restart.baza.dM.getDataModule().getMxPrinterRM();
    printerRM.open();
    printerRM.first();

    mxRM matIsp = new mxRM();
    try {
      matIsp.init(printerRM);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
////////////////////////////////
    setRM(matIsp);

    detailDS.addColumn(new Column("RBR","RedBr",Variant.SHORT));
    detailDS.addColumn(new Column("SIFRA","Sifra",Variant.STRING));
    detailDS.addColumn(new Column("NAZIV","Naziv",Variant.STRING));
    detailDS.addColumn(new Column("JM","JedMj",Variant.STRING));
    detailDS.addColumn(new Column("KOL","Kol",Variant.STRING));
    detailDS.addColumn(new Column("CIJENA","CijenaBP",Variant.STRING));
    detailDS.addColumn(new Column("IZNOS","Iznos",Variant.STRING));
//*** dodano
    detailDS.addColumn(new Column("POPUST","Popust",Variant.STRING));
    detailDS.addColumn(new Column("POREZ","Porez",Variant.STRING));
    String kupac =getPartner();
    String skladiste = getSkladiste();

    String [] detail =new String[]{"<#RBR|3|right#>"+". "+"<#SIFRA|20|left#>"+" "+"<#NAZIV|36|left#>"+" "+
      "<#KOL|12|right#>"+" "+"<#JM|6|right#>"+" "+"<#POPUST|11|right#>"+" "+"<#POREZ|10|right#>"+" "+"<#CIJENA|13|right#>"+" "+"<#IZNOS|16|right#>"};
    this.setDetail(detail);
  }

  protected void fill()
  {
    sumaBP = Aus.zero2;
    sumaSP = Aus.zero2;
    sumaPOR = Aus.zero2;
    sumaIneto = Aus.zero2;
    getDatum();
    String param = Aut.getAut().getIzlazCART();

    ds.open();
    ds.first();
    brRedDetail = ds.getRowCount();
    detailDS.open();
    detailDS.empty();
    while(ds.inBounds())
    {
      detailDS.insertRow(false);
      detailDS.setShort("RBR", ds.getShort("RBR"));
      if(param.equals("CART"))
        detailDS.setString("SIFRA", ds.getInt("CART")+"");
      else if(param.equals("CART1"))
        detailDS.setString("SIFRA", ds.getString("CART1"));
      else
        detailDS.setString("SIFRA", ds.getString("BC"));
      detailDS.setString("NAZIV", ds.getString("NAZART"));
      detailDS.setString("JM", ds.getString("JM"));
      detailDS.setString("KOL", replaceFullStop(ds.getBigDecimal("KOL").toString()));
      detailDS.setString("CIJENA", formatIznos(ds.getBigDecimal("FC"), 13));
      detailDS.setString("IZNOS", formatIznos(ds.getBigDecimal("INETO"),16));
      detailDS.setString("POPUST", replaceFullStop(ds.getBigDecimal("UPRAB1").toString()));
      detailDS.setString("POREZ", replaceFullStop(ds.getBigDecimal("UPPOR").toString()));
      sumaBP=sumaBP.add(ds.getBigDecimal("IPRODBP"));
      sumaSP=sumaSP.add(ds.getBigDecimal("IPRODSP"));
      sumaIneto=sumaIneto.add(ds.getBigDecimal("INETO"));
      detailDS.post();
      ds.next();
    }

    this.setFooter(new String[]{"----------------------------------------------------------------------------------------------------------------------------------------"+"<$newline$>"+
                   ukBPStr()+
                   ukPopustStr()+
                   "              REKAPITULACIJA POREZA                                                                   UKUPNO BEZ POREZA   "+formatIznos(sumaBP,14)+"<$newline$>"+
                   "      Grupa      %         Osnovica          Iznos                                                         UKUPNO POREZ   "+formatIznos(sumaSP.add(sumaBP.negate()),14)+"<$newline$>"+
        getPorezRekapitulacija()/*+getNP()*/+"<$newline$><$newline$>" + getNapomenaOpis() +addLines()});

    this.setHeader(new String[]{"                                                                                                                                                         "+"<$DoubleWidthON$>"+"R-1"+"<$DoubleWidthOFF$>"+"<$newline$>"+
                   "<$Reset$><$CondensedON$>"+"<$newline$>"+getPartner()+getRacun()+getSkladiste()+
                   "----------------------------------------------------------------------------------------------------------------------------------------"+"<$newline$>"+
                   "R.B."+" Šifra               "+" Naziv artikla/usluge                "+"     Kolièina"+"     JM"+"  Popust (%)"+"  Porez (%)"+"        Cijena"+"            Iznos"+"<$newline$>"+
        "----------------------------------------------------------------------------------------------------------------------------------------"});
    this.setPgHeader(
        "<$Reset$>"+Util.getUtil().getDefaultMxHeader()+
        ""
        );
    brRedDetail = detailDS.getRowCount();
  }

  private String formatIznos(BigDecimal izn, int len)
  {
    if(izn.compareTo(new BigDecimal(0))==0)
       return Valid.getValid().maskString("0,00",' ', len);
    String finalStr ="";
    int idx=0;
    String temp="";
    String iznos = izn.toString();
    String iznosInt=iznos.substring(0,iznos.indexOf("."));
    String iznosDec=iznos.substring(iznos.indexOf(".")+1, iznos.length());
    for(int i= 0; i<iznosInt.length();i++)
    {
      temp = iznosInt.substring((iznosInt.length()-(i+1)),(iznosInt.length()-(i)))+temp;
      idx++;
      if(idx==3 && iznosInt.length()>3)
      {
        temp="."+temp;
        idx=0;
      }
    }
    finalStr = temp+","+iznosDec;
    return Valid.getValid().maskString(finalStr,' ', len);
  }

  protected String formatStr(String i, int length)
  {
    return Valid.getValid().maskString(i+"",' ', length);
  }

  protected String getPartner()
  {
    ds.open();
    ds.first();
    String kupacTemp=ds.getInt("CKUPAC")+"";
    if(!kupacTemp.equals("0") )
    {
      brRedPartner = 1;
      ld.raLocate(dm.getKupci(), new String []{"CKUPAC"}, new String []{kupacTemp});
      String firstRow;
      String secondRow ="";
      String thirdRow="<$newline$>";
      firstRow = "KUPAC: "+formatStr(kupacTemp,6)+" <$CondensedOFF$>"+dm.getKupci().getString("IME")+" "+dm.getKupci().getString("PREZIME")
               +"<$CondensedON$><$newline$>";
      if(!dm.getKupci().getString("ADR").equals(""))
      {
        secondRow = formatStr("",14)+dm.getKupci().getString("ADR");
        brRedPartner+=1;
      }
      if(!dm.getKupci().getString("MJ").equals(""))
      {
        if (dm.getKupci().getInt("PBR") != 0)
          secondRow += ", " +dm.getKupci().getInt("PBR")+" "+dm.getKupci().getString("MJ");
        else
          secondRow += ", " +dm.getKupci().getString("MJ");
      }
      return firstRow+secondRow+thirdRow+thirdRow;
    }
    return "";
  }

  protected String getSkladiste()
  {
    ds.open();
    ds.first();
    String skladTemp=ds.getString("CSKL");
    ld.raLocate(dm.getSklad(), new String []{"CSKL"}, new String []{skladTemp});
    String prMj = "PRODAJNO MJESTO:"+formatStr(skladTemp,12)+" "+dm.getSklad().getString("NAZSKL");
    String date = formatStr(getDatum(), 136-prMj.length());
    return prMj+date+"<$newline$>";
  }

  protected String getRacun()
  {
    ds.open();
    ds.first();
    String vrati = "PONUDA br.  "+ds.getString("VRDOK")+"-"+ds.getString("CSKL")+"/"+ds.getString("GOD")+"-"+ds.getInt("BRDOK");
    return "<$Reset$><$DoubleWidthON$>"+formatStr(vrati, vrati.length()+(40-vrati.length())/2)+"<$DoubleWidthOFF$>"+"<$CondensedON$>"+"<$newline$>"+"<$newline$>";
  }

  private String addLines()
  {
    String lines ="";
    int red = 0;
    int brRedova=0;

    int initBrRedova = mxPrinter.getDefaultMxPrinter().getDefaultMxPrinterA5();
    for (int koef = 1; koef<10;koef++)
    {
      if(initBrRedova*koef>brRedPartner + brRedDetail+brRedHeader+brRedFooter+brRedPGHeader+brRedRekap/*+brRedRekPl*/)
      {
      brRedova = initBrRedova*koef;
      break;
    }
    }

    if(brRedPartner + brRedDetail+brRedHeader+brRedFooter+brRedPGHeader+brRedRekap/*+brRedRekPl*/<brRedova)
    {
      for(int i = brRedPartner + brRedDetail+brRedHeader+brRedFooter+brRedPGHeader+brRedRekap/*+brRedRekPl*/; i<(brRedova);i++)
      {
        lines += "<$newline$>";
        red++;
      }
    }
    return lines;
  }

  private String getDatum()
  {
    ds.open();
    ds.first();
    ld.raLocate(dm.getSklad(), new String []{"CSKL"}, new String []{ds.getString("CSKL")});
    String corg = dm.getSklad().getString("CORG");
    ld.raLocate(dm.getOrgstruktura(), new String []{"CORG"}, new String []{corg});
    String mjesto = dm.getOrgstruktura().getString("MJESTO");

    return mjesto+", "+raDateUtil.getraDateUtil().dataFormatter(Valid.getValid().getToday())+".";
  }

  private String getPorezRekapitulacija()
  {
    DataSet qds_porez = repQC.getPoreziSet(ds.getString("CSKL"), ds.getString("VRDOK"), ds.getString("GOD"), ds.getInt("BRDOK"));
    if(!qds_porez.isOpen()) qds_porez.open();
    qds_porez.first();
    String rekapitulacija = "";
    while (qds_porez.inBounds())
    {
      rekapitulacija += formatStr(qds_porez.getString("CPOR").trim(),11)+" "+ formatStr(qds_porez.getBigDecimal("UKUPOR").toString()+"%",8)+" "+
                        formatStr(qds_porez.getBigDecimal("IPRODBP").toString(),14)+" "+formatStr(qds_porez.getBigDecimal("POR1").toString(),14);
                        if(qds_porez.getRow()==0)
                          rekapitulacija += getTotal();
                        rekapitulacija += "<$newline$>";
                        qds_porez.next();
    }
    brRedRekap = qds_porez.getRowCount()+2;
    rekapitulacija += getNPP();
    rekapitulacija +="<$newline$>"+"<$newline$>"+"SLOVIMA: " +getSLOVIMA();
    return rekapitulacija;
  }
  public String getSLOVIMA() {
    return ut.numToLet(sumaSP.doubleValue());
  }

  private String getTotal()
  {
    return formatStr("UKUPNO S POREZOM   "+formatIznos(sumaSP,14), 86);
  }

  private String ukBPStr()
  {
    BigDecimal popust = sumaIneto.add(sumaBP.negate());
    if( popust.compareTo(new BigDecimal(0))==0)
      return "";
    return formatStr("UKUPNO BEZ POPUSTA   "+formatIznos(sumaIneto,14), 136)+"<$newline$>";
  }

  private String ukPopustStr()
  {
    BigDecimal popust = sumaIneto.add(sumaBP.negate());
    if( popust.compareTo(new BigDecimal(0))==0)
    {
      return "";
    }
    brRedFooter=8;
    return formatStr("UKUPNO POPUST   "+formatIznos(sumaIneto.add(sumaBP.negate()),14), 136)+"<$newline$>";
  }

//  private void nacPl(){
//    nacinPlacanja = new ArrayList();
//    rata = new ArrayList();
//    datumNaplate  = new ArrayList();
//    try
//    {
//      String upit = "SELECT rbr, cnacpl, cbanka, datum, irata FROM rate "+
//                    "WHERE cskl='"+ds.getString("CSKL")+
//                    "' AND vrdok='"+ds.getString("VRDOK")+"' AND brdok='" + ds.getInt("BRDOK") +
//                    "' and god='"+ds.getString("GOD")+"'";
//      QueryDataSet rate = ut.getNewQueryDataSet(upit);
//      if (rate.rowCount() == 0){
//        nacinPlacanja.add("Gotovina");
//        String jednaRata = sgQuerys.getSgQuerys().format(ds, "UIRAC");
//        if (jednaRata.indexOf(',') <0)
//          rata.add(jednaRata+",00");
//      } else {
//        rate.first();
//        do {
//          try {
//            lookupData.getlookupData().raLocate(dm.getNacpl(), "CNACPL", rate.getString("CNACPL"));
//            if (rate.getString("CNACPL").equals("K")){
//              lookupData.getlookupData().raLocate(dm.getKartice(), "CBANKA", rate.getString("CBANKA"));
//              nacinPlacanja.add(dm.getNacpl().getString("NAZNACPL")+" - "+dm.getKartice().getString("NAZIV"));
//            } else if (rate.getString("CNACPL").equals("\u010C")){
//              lookupData.getlookupData().raLocate(dm.getBanke(), "CBANKA", rate.getString("CBANKA"));
//              nacinPlacanja.add(dm.getNacpl().getString("NAZNACPL")+" - "+dm.getBanke().getString("NAZIV"));
//            } else {
//              nacinPlacanja.add(dm.getNacpl().getString("NAZNACPL"));
//            }
//          }
//          catch (Exception ex) {
//            ex.printStackTrace();
//          }
//          String jednaRata = sgQuerys.getSgQuerys().format(rate, "IRATA");
//          if (jednaRata.indexOf(',') <0)
//            rata.add(jednaRata+",00");
//          else
//            rata.add(jednaRata);
//          if (!rate.getString("CNACPL").equals("G") && !rate.getString("CNACPL").equals("V"))
//            datumNaplate.add(rdu.dataFormatter(rate.getTimestamp("DATUM")));
//          else
//            datumNaplate.add("");
//
//        } while (rate.next());
//      }
//    }
//    catch (Exception ex)
//    {
//      ex.printStackTrace();
//    }
//  }

  private void nacPl(){
    nacinPlacanja = new ArrayList();
    rata = new ArrayList();
    datumNaplate  = new ArrayList();
    try
    {
      String upit = "SELECT rbr, cnacpl, cbanka, datum, irata FROM rate "+
                    "WHERE cskl='"+ds.getString("CSKL")+
                    "' AND vrdok='"+ds.getString("VRDOK")+"' AND brdok='" + ds.getInt("BRDOK") +
                    "' and god='"+ds.getString("GOD")+"'";
      QueryDataSet rate = ut.getNewQueryDataSet(upit);
      if (rate.rowCount() == 0){
        nacinPlacanja.add("Gotovina");
        String jednaRata = sgQuerys.getSgQuerys().format(ds, "UIRAC");
        if (jednaRata.indexOf(',') <0)
          rata.add(jednaRata+",00");
      } else {
        rate.first();
        do {
          try {
            lookupData.getlookupData().raLocate(dm.getNacpl(), "CNACPL", rate.getString("CNACPL"));
//            if (rate.getString("CNACPL").equals("K")){
             if (dm.getNacpl().getString("FL_KARTICA").equals("D")){
              lookupData.getlookupData().raLocate(dm.getKartice(), "CBANKA", rate.getString("CBANKA"));
              nacinPlacanja.add(dm.getNacpl().getString("NAZNACPL")+" - "+dm.getKartice().getString("NAZIV"));
//            } else if (rate.getString("CNACPL").equals("\u010C")){
              } else if (dm.getNacpl().getString("FL_CEK").equals("D")){
              lookupData.getlookupData().raLocate(dm.getBanke(), "CBANKA", rate.getString("CBANKA"));
              nacinPlacanja.add(dm.getNacpl().getString("NAZNACPL")+" - "+dm.getBanke().getString("NAZIV"));
            } else {
              nacinPlacanja.add(dm.getNacpl().getString("NAZNACPL"));
            }
          }
          catch (Exception ex) {
            ex.printStackTrace();
          }
          String jednaRata = sgQuerys.getSgQuerys().format(rate, "IRATA");
          if (jednaRata.indexOf(',') <0)
            rata.add(jednaRata+",00");
          else
            rata.add(jednaRata);
//          if (!rate.getString("CNACPL").equals("G") && !rate.getString("CNACPL").equals("V"))
          if (!dm.getNacpl().getString("FL_GOT").equals("D"))
            datumNaplate.add(rdu.dataFormatter(rate.getTimestamp("DATUM")));
          else
            datumNaplate.add("");

        } while (rate.next());
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  protected String getNPP() {
  	return "";
  }

  protected String getNP()
  {
    String np="";
    if(rata.size()<=0)
    {
      if(nacinPlacanja.size()==0)
        nacinPlacanja.add("Gotovina");
      rata.add(formatIznos(sumaSP,38-nacinPlacanja.get(0).toString().length()));
      datumNaplate.add("");
    }
    np="              REKAPITULACIJA PLAÆANJA:\n";
    for(int i=0;i<nacinPlacanja.size();i++)
    {
      np += nacinPlacanja.get(i)+myFormatStr(rata.get(i).toString(),50-nacinPlacanja.get(i).toString().length() )+
              "  "+  datumNaplate.get(i).toString()+"\n";

    }
    np += "\n"+"SLOVIMA: " +getSLOVIMA();
    brRedRekPl = brRedRekPl+ rata.size();
    return np;
  }

  private String getNapomenaOpis(){
    if (lookupData.getlookupData().raLocate(dm.getNapomene(),"CNAP",ds.getString("CNAP"))){
      if (!ds.getString("OPIS").equals("")){
        return dm.getNapomene().getString("NAZNAP")+"<$newline$>"+ds.getString("OPIS");
      } else {
        return dm.getNapomene().getString("NAZNAP");
      }
    }
    if (!ds.getString("OPIS").equals("")) return ds.getString("OPIS");
    return "";
  }

  private String myFormatStr(String str, int length)
  {
    String temp ="";
    for(int i = 0; i<(length-str.length());i++)
    {
      temp=" "+temp;
    }
    return temp+str;
  }

  private String replaceFullStop(String str)
  {
    VarStr v = new VarStr(str);
    v.replace(".",",");
    return v.toString();
  }

  public void makeReport()
  {
    fill();
    super.makeReport();
  }

}