/****license*****************************************************************
**   file: repMxOTP.java
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

public class repMxOTP extends mxReport {
  public QueryDataSet ds;
  reportsQuerysCollector repQC = reportsQuerysCollector.getRQCModule();
  private QueryDataSet detailDS = new QueryDataSet();
  lookupData ld = lookupData.getlookupData();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  BigDecimal sumaBP = Aus.zero2;
  BigDecimal sumaSP = Aus.zero2;
  BigDecimal sumaPOR = Aus.zero2;
  BigDecimal sumaIneto = Aus.zero2;
  int brRedDetail =0;
  int brRedPartner =0;
  int brRedHeader = 5;
  int brRedPGHeader = 5;
  int brRedFooter = 6;
  int brRedRekap = 0;

  dM dm = dM.getDataModule();
  public repMxOTP() {
    try
    {
      init();
    }
    catch (Exception ex) {}

  }
  public void init() throws Exception
  {
    String param = hr.restart.sisfun.frmParam.getParam("robno","indiCart");
    ds = reportsQuerysCollector.getRQCModule().getQueryDataSet();
    setDataSet(detailDS);

//    mxRM matIsp = mxRM.getDefaultMxRM();
//
//    String prntcmd = "cmd /c C:\\util\\hrconv 2 4 < \""+System.getProperty("user.dir")+"\\#\" > \"\\\\Grga\\EPSON\"";
//    matIsp.setPrintCommand(prntcmd);

////////////////////////////
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
////////////////////////////
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

    String [] detail =new String[]{"<#RBR|3|right#>"+". "+"<#SIFRA|20|left#>"+" "+"<#NAZIV|87|left#>"+" "+
      "<#JM|6|right#>"+" "+"<#KOL|15|right#>"};
    this.setDetail(detail);
  }

  private void fill()
  {
    sumaBP = Aus.zero2;
    sumaSP = Aus.zero2;
    sumaPOR = Aus.zero2;
    sumaIneto = Aus.zero2;
    getDatum();
    String param = hr.restart.sisfun.frmParam.getParam("robno","indiCart");

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

    this.setFooter(new String[]{"----------------------------------------------------------------------------------------------------------------------------------------"+"<$newline$>"+"<$newline$>"+
        "               Izdao:                                                                                            Preuzeo:               "+addLines()});


    this.setHeader(new String[]{"                                                                                                                                                         "+"<$DoubleWidthON$>"+"R-1"+"<$DoubleWidthOFF$>"+"<$newline$>"+
                   "<$Reset$><$CondensedON$>"+"<$newline$>"+getPartner()+"<$newline$>"+getRacun()+getSkladiste()+
                   "----------------------------------------------------------------------------------------------------------------------------------------"+"<$newline$>"+
                   "R.B."+" Šifra               "+" Naziv artikla/usluge                                                                  "+"      JM"+"        Kolièina"+"<$newline$>"+
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

  private String formatStr(String i, int length)
  {

    return Valid.getValid().maskString(i+"",' ', length);
  }

  private String getPartner(){
    ds.open();
    ds.first();
    String PartnerTemp = ds.getInt("CPAR")+"";
    String kupacTemp = ds.getInt("CKUPAC")+"";
    if(!PartnerTemp.equals("0")){
      brRedPartner = 1;
      ld.raLocate(dm.getPartneri(), new String[]{"CPAR"}
                  , new String[]{PartnerTemp});
      String firstRow;
      String secondRow = "";
      String thirdRow = "";
      String fourthRow = "";
      firstRow = "PARTNER: "+formatStr(PartnerTemp, 6)+" <$CondensedOFF$>"+dm.getPartneri().getString("NAZPAR")
                 +"<$CondensedON$><$newline$>";
      if(!dm.getPartneri().getString("ADR").equals("")){
        secondRow = formatStr("", 14)+dm.getPartneri().getString("ADR");
        brRedPartner += 1;
      }
      if(!dm.getPartneri().getString("MJ").equals("")){
        secondRow += ", "+dm.getPartneri().getInt("PBR")+" "+dm.getPartneri().getString("MJ");
      }

      if(!dm.getPartneri().getString("MB").equals("")){
        if(secondRow.equals(""))
          secondRow = "<$newline$>"+formatStr("", 14)+"MB: "+dm.getPartneri().getString("MB")+"<$newline$>";
        else
          secondRow += ", MB: "+dm.getPartneri().getString("MB")+"<$newline$>";
        brRedPartner += 1;
      } else{
        fourthRow = "<$newline$>";
        brRedPartner += 1;
      }
      return firstRow+secondRow+thirdRow+fourthRow;
    } else if(!kupacTemp.equals("0")){
      brRedPartner = 1;
      ld.raLocate(dm.getKupci(), new String[]{"CKUPAC"}
                  , new String[]{kupacTemp});
      String firstRow;
      String secondRow = "";
      String thirdRow = "";
      String fourthRow = "";
      firstRow = "KUPAC: "+formatStr(kupacTemp, 6)+" <$CondensedOFF$>"+dm.getKupci().getString("IME")+" "+dm.getKupci().getString("PREZIME")
                 +"<$CondensedON$><$newline$>";
      if(!dm.getKupci().getString("ADR").equals("")){
        secondRow = formatStr("", 14)+dm.getKupci().getString("ADR");
        brRedPartner += 1;
      }
      if(!dm.getKupci().getString("MJ").equals("")){
        secondRow += ", "+dm.getKupci().getInt("PBR")+" "+dm.getKupci().getString("MJ");
      }

      if(!dm.getKupci().getString("JMBG").equals("")){
        /*if(secondRow.equals(""))
          secondRow = "<$newline$>"+formatStr("", 14)+"JMBG: "+dm.getKupci().getString("JMBG")+"<$newline$>";
        else
          secondRow += ", JMBG: "+dm.getKupci().getString("JMBG")+"<$newline$>";
        brRedPartner += 1;*/
      } else{
        fourthRow = "<$newline$>";
        brRedPartner += 1;
      }
      return firstRow+secondRow+thirdRow+fourthRow;
    }
    return "";
  }

  private String getSkladiste()
  {
    ds.open();
    ds.first();
    String skladTemp=ds.getString("CSKL");
    ld.raLocate(dm.getSklad(), new String []{"CSKL"}, new String []{skladTemp});
    String prMj = "PRODAJNO MJESTO:"+formatStr(skladTemp,12)+" "+dm.getSklad().getString("NAZSKL");
    String date = formatStr(getDatum(), 136-prMj.length());
    return prMj+date+"<$newline$>";
  }

  private String getRacun()
  {
    ds.open();
    ds.first();
    String vrati = "OTPREMNICA br.  "+ds.getString("VRDOK")+"-"+ds.getString("CSKL")+"/"+ds.getString("GOD")+"-"+ds.getInt("BRDOK");
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
      if(initBrRedova*koef>brRedPartner + brRedDetail+brRedHeader+brRedFooter+brRedPGHeader+brRedRekap)
      {
      brRedova = initBrRedova*koef;
      break;
    }
    }

    if(brRedPartner + brRedDetail+brRedHeader+brRedFooter+brRedPGHeader+brRedRekap<brRedova)
    {
      for(int i = brRedPartner + brRedDetail+brRedHeader+brRedFooter+brRedPGHeader+brRedRekap; i<(brRedova);i++)
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
    brRedRekap = qds_porez.getRowCount()-1;
    rekapitulacija +="<$newline$>"+"SLOVIMA: " +getSLOVIMA();
    return rekapitulacija;
  }
  public String getSLOVIMA() {
    return ut.numToLet(sumaSP.doubleValue());
  }

  private String getTotal()
  {
    return formatStr("UKUPNO S POREZOM   "+formatIznos(sumaSP,14), 86);
  }

//* dodavanje stringa u footeru ako postoji popust
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
