/****license*****************************************************************
**   file: repDiskZap.java
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

import hr.restart.robno.repMemo;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.mxPrinter;

import java.util.StringTokenizer;

import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;

public class repDiskZapUN extends repDisk {
  QueryDataSet qds;
  QueryDataSet cjelineDS = new QueryDataSet();
  hr.restart.baza.dM dm;
  Valid vl;
  repMemo memo = repMemo.getrepMemo();
  public String fileName="";
  private static String vrstaNalogaUDatoteci = null;//"1";

  public repDiskZapUN() {
    super(1000);
    try {
      String datum  =Valid.getValid().getToday().toString();
//      fileName = datum.substring(6,10)+datum.substring(5,7)+datum.substring(2,4)+".01";
      fileName = datumParser(datum, 2)+".txt";
      try {
         this.setPrinter(mxPrinter.getDefaultMxPrinter());
         this.getPrinter().setNewline(System.getProperty("line.separator"));
       }
       catch (Exception ex) {
         ex.printStackTrace();
      }
      this.setPrint("UN"+fileName);
      fill();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void fill() throws Exception
  {
    vl = Valid.getValid();
    dm = hr.restart.baza.dM.getDataModule();
    qds = (QueryDataSet)frmVirmani.getInstance().getRaQueryDataSet();
    qds.open();
    qds.getColumn("BRRACNT").setRowId(true);
    qds.setSort(new SortDescriptor(new String[] {"BRRACNT"}));
    qds.first();
    diskzap.open();
    diskzap.insertRow(false);
    diskzap.setString("REDAK", createLabela());
    diskzap.post();
    getCjelineDS(qds.getString("CKEY"));
    cjelineDS.first();
    while(cjelineDS.inBounds())
    {
      diskzap.insertRow(false);
      diskzap.setString("REDAK", insertSlogCjelina());
      insertSlogPojNal(cjelineDS.getString("BRRACNT"));
      cjelineDS.next();
    }
    diskzap.insertRow(false);
    diskzap.setString("REDAK", createEnd());
    diskzap.post();
  }
  /**
   * slog 399
   * @return
   */
  private String createEnd() {
    StringBuffer sb = getNullSB();
    sb.replace(997, 997+3, "399");// TIP SLOGA
    return new String(sb);
  }

  /**
   * slog 300
   * @return
   */
  private String createLabela()
  {
    String datumpodnosenja = Valid.getValid().getToday().toString();
    StringBuffer sb = getNullSB();
    String param = hr.restart.sisfun.frmParam.getParam("zapod", "znoIzdok","   ");
    if (param.length()<3) param="   ";
    sb.replace(0, 7, datumParser(datumpodnosenja,2));
    sb.replace(8, 8, getVrstaNalogaUDatoteci()); //1 - nacionalna plaæanja 4 - plaæe i ostala redovna primanja!!!HC!!!
    sb.replace(9, 11, param);
    if (getVrstaNalogaUDatoteci().equals("4")) {
      sb.replace(12, 13, frmParam.getParam("pl", "nacizUN"+OrgStr.getKNJCORG(), "1", "Naèin izvrš. u UN datoteke za placu:1-raèun firme nije u toj banci,2-raèun firme JE u toj banci"));
      sb.replace(13, 24, memo.getLogoOIB());
      sb.replace(24, 35, vl.maskZeroInteger(new Integer(memo.getLogoMatbroj()), 8)+"000");
      sb.replace(46, 57, memo.getLogoOIB());
    }
    sb.replace(997, 997+3, "300");// TIP SLOGA
    return new String(sb);
  }

  public static String getVrstaNalogaUDatoteci() {
    return vrstaNalogaUDatoteci ;
  }
  /**
   *  Vrsta naloga u datoteci
   * 1 nacionalna plaæanja u HRK
   * 2 meðunarodna plaæanja u valuti HRK ili stranoj valuti
   * 3 nacionalna plaæanja u stranoj valuti
   * 4 plaæe, ostala redovna i povremena primanja
   * @param _vrstaNalogaUDatoteci
   */
  public static void setVrstaNalogaUDatoteci(String _vrstaNalogaUDatoteci) {
    vrstaNalogaUDatoteci = _vrstaNalogaUDatoteci;
  }
  private String datumParser(String datum, int i)
  {
    if(i==0) //DDMMGG
    {
      return datum.substring(8,10)+datum.substring(5,7)+datum.substring(2,4);
    }
    else if (i==1) //DDMMGGGG
    {
      return datum.substring(8,10)+datum.substring(5,7)+datum.substring(0,4);
    }
    else {//GGGGMMDD
      return datum.substring(0,4)+datum.substring(5,7)+datum.substring(8,10);
    }
  }
  private String zrParser(String zr)
  {
//    String newZR = "";
//    StringTokenizer st = new StringTokenizer(zr,"-");
//    {
//      while(st.hasMoreTokens())
//      {
//        newZR += st.nextToken();
//      }
//    }
//    return newZR;
    return frmVirmani.getIBAN_HR(zr, false);
  }

  private void getCjelineDS(String ckey)
  {
    String zr="";
    cjelineDS = hr.restart.baza.Virmani.getDataModule().getFilteredDataSet("1=0");
    cjelineDS.open();

    qds.first();
    while(qds.inBounds())
    {
      if(!zr.equals(qds.getString("BRRACNT")))// IBANNT?
      {
        zr= qds.getString("BRRACNT");
        cjelineDS.insertRow(false);
        qds.copyTo(cjelineDS);
        cjelineDS.post();
      }
      qds.next();
    }
  }
 /**
  * tip 301
  * @return
  */
  private String insertSlogCjelina()
  {
    StringBuffer sb = getNullSB();
    String svota,brNal, zirorac;
//    String nalogodavatelj, mjesto,  DI, identifikator;
    String datumizvParsed1 = datumParser(cjelineDS.getTimestamp("DATUMIZV").toString(), 2);
//    String datumizvParsed1 = datumParser(Valid.getValid().getToday().toString(), 2);
//    int z = cjelineDS.getString("NATERET").indexOf("\n");
//    if(z>0)
//      nalogodavatelj = cjelineDS.getString("NATERET").substring(0,cjelineDS.getString("NATERET").indexOf("\n")).trim();
//    else
//      nalogodavatelj = cjelineDS.getString("NATERET").trim();
//    lookupData.getlookupData().raLocate(dm.getOrgstruktura(), new String [] {"NAZIV"}, new String[]{nalogodavatelj});
//    mjesto = dm.getOrgstruktura().getString("MJESTO")+"          ";
//    mjesto = mjesto.substring(0,10);
    zirorac = zrParser(cjelineDS.getString("BRRACNT"));
//    try {
//      DI = dm.getOrgstruktura().getString("ZIRO").substring(0,dm.getOrgstruktura().getString("ZIRO").indexOf("-"));
//    }
//    catch (Exception ex) {
//      throw new RuntimeException("Neispravan format ziro-racuna '" + zirorac+"'");
//    }

    svota = getSvotaCJ(cjelineDS.getString("BRRACNT"), cjelineDS.getString("CKEY"));
    brNal = getBrNaloga(cjelineDS.getString("BRRACNT"), cjelineDS.getString("CKEY"));
//    identifikator=zirorac.substring(0,3);
//
//    if(identifikator.equals("301") || identifikator.equals("601"))
//    {
//      throw new RuntimeException("Stari format ziro-racuna");
//    }
//    else
//    {
//      sb.replace(18,18+nalogodavatelj.length(),nalogodavatelj);
//
//      sb.replace(68,68+10/*mjesto.length()*/,mjesto+"          ");
//      sb.replace(78, 78+svota.length(),svota);
//      sb.replace(93,93+brNal.length(),brNal);
////      sb.replace(98,98+DI.length(),DI);
////      sb.replace(105,105+zirorac.length(),zirorac);
//      sb.replace(98,98+zirorac.length(),zirorac);
//      sb.replace(123,123+datumizvParsed1.length()
//                 ,datumizvParsed1);
//      sb.replace(249,250,"9");
//    }
    sb.replace(0, zirorac.length(), zirorac);
    sb.replace(21, 21+3, "HRK");
    //racun naknade, oznaka valute naknade
    sb.replace(48, 48+brNal.length(), brNal);
    sb.replace(53, 53+svota.length(), svota);
    sb.replace(73, 73+datumizvParsed1.length(), datumizvParsed1);
    sb.replace(997, 997+3, "301");// TIP SLOGA
    
    
    return new String(sb);
  }

  /**
   * 309
   * @param zr
   */
  private void insertSlogPojNal(String zr)
  {

    String primatelj, mjesto, zirorac, pnbz1,pnbz2, pnbo1, pnbo2, svrha, DI,svota, temp;
    zirorac = zrParser(cjelineDS.getString("BRRACUK"));
    StringBuffer sb;
    qds.first();
    while(qds.inBounds())
    {
      if(qds.getString("BRRACNT").equals(zr))
      {
        sb = getNullSB();
        int z = qds.getString("UKORIST").indexOf("\n");
        if(z>0)
          primatelj = qds.getString("UKORIST").substring(0,qds.getString("UKORIST").indexOf("\n")).trim();
        else
          primatelj = qds.getString("UKORIST").trim();
        lookupData.getlookupData().raLocate(dm.getPartneri(), new String [] {"NAZPAR"}, new String[]{primatelj});

        svota = getSvotaPN(qds.getString("BRRACUK"), qds.getShort("RBR"));
//        svota = formatString(temp,13);
        pnbz1 = //formatString(qds.getString("PNBZ1"),2);
          dodajHR(qds.getString("PNBZ1"));//ako hoce nule nek ih upise u virman
        pnbz2 = qds.getString("PNBZ2");
        pnbo1 = //formatString(qds.getString("PNBO1"),2);
          dodajHR(qds.getString("PNBO1"));//ako hoce nule nek ih upise u virman
        pnbo2 = qds.getString("PNBO2");

        String sif1 = qds.getString("SIF1");
        if (sif1.trim().length() < 4) sif1 = "    ";
        svrha = qds.getString("SVRHA").trim();
//        if(svrha.length()>36)
//          svrha = svrha.substring(0,36).trim();
        mjesto = dm.getPartneri().getString("MJ")+"          ";
        mjesto = mjesto.substring(0,10);
//        try {
//          DI= qds.getString("BRRACUK").substring(0, qds.getString("BRRACUK").indexOf("-"));
//        }
//        catch (Exception ex) {
//          throw new RuntimeException("Neispravan oblik žiro-ra\u010Duna !");
//        }
        zirorac = zrParser(qds.getString("BRRACUK")) ;
        sb.replace(0, zirorac.length(), zirorac);
        sb.replace(34, 34+(primatelj.length()<70?primatelj.length():70), primatelj);
        sb.replace(177,177+pnbz1.length(),pnbz1);
        sb.replace(181,181+pnbz2.length(),pnbz2);
        sb.replace(203,203+sif1.length(),sif1);
        sb.replace(207, 207+(svrha.length()<140?svrha.length():140), svrha);
        sb.replace(347,347+svota.length(),svota);
        sb.replace(362,362+pnbo1.length(),pnbo1);
        sb.replace(366,366+pnbo2.length(),pnbo2);
        if (getVrstaNalogaUDatoteci().equals("4")) {
          sb.replace(548, 551, "100"); //sifra vrste osobnog primanja
        }
        sb.replace(546, 546+1, "3");//troskovna opcija
        
        sb.replace(997, 997+3, "309");// TIP SLOGA
//        
//        sb.replace(18,18+primatelj.length(),primatelj);
//        sb.replace(68,68+10/*mjesto.length()*/,mjesto);
//        sb.replace(78,78+pnbz1.length(),pnbz1);
//        sb.replace(80,80+pnbz2.length(),pnbz2);
//        sb.replace(102,102+svrha.length(),svrha);
//        sb.replace(157,157+pnbo1.length(),pnbo1);
//        sb.replace(159,159+pnbo2.length(),pnbo2);
//        sb.replace(144,144+svota.length(),svota);
////        sb.replace(181,181+DI.length(), DI);
////        sb.replace(188,188+zirorac.length(), zirorac);
//        sb.replace(181,181+zirorac.length(), zirorac);
//        sb.replace(249,250,"1");
        
        
        diskzap.insertRow(false);
        diskzap.setString("REDAK", new String(sb));
        diskzap.post();
      }
      qds.next();
    }
  }

  public static String dodajHR(String pnb) {
    if (pnb == null) return "    ";
    if (pnb.trim().length()==2) {
      return "HR"+pnb;
    }
    if (pnb.trim().length()==1) {
      return "HR0"+pnb;
    }
    if (pnb.trim().length()==4) {
      return pnb;
    }
    return "    ";
  }

  private StringBuffer getNullSB()
  {
    String dummy = "";
    for (int i = 0; i<sirina;i++)
    {
      dummy+=" ";
    };
    return new StringBuffer(dummy);
  }

  private String getSvotaCJ(String zr, String ckey)
  {
    String svota, temp;
    String qStr = "select sum(iznos) as iznos from virmani where brracnt='"+zr+"' and ckey = '"+ckey+"'";
    QueryDataSet ds = Util.getNewQueryDataSet(qStr);
    ds.open();
    temp = ds.getBigDecimal("IZNOS").toString();
    svota = temp.substring(0,temp.indexOf("."))+temp.substring(temp.indexOf(".")+1, temp.length());
    return formatString(svota,20);
  }

  private String getSvotaPN(String zr, short rbr)
  {
    String svota, temp;
/*
 //ANDREJ: Ovo je totalna nebuloza i nemrem dokuciti cemu sluzi ovaj maliciozni kod 
    String qStr = "select iznos as iznos from virmani where brracuk='"+zr+"' and rbr="+rbr;
    QueryDataSet ds = Util.getNewQueryDataSet(qStr);
    ds.open();
*/
    temp = qds.getBigDecimal("IZNOS").toString();
    svota = temp.substring(0,temp.indexOf("."))+temp.substring(temp.indexOf(".")+1, temp.length());

    return formatString(svota,15);
  }
  private String getBrNaloga(String zr, String ckey)
  {
    String qStr = "select count(*) as brn from virmani where brracnt='"+zr+"' and ckey ='"+ckey+"'";
    QueryDataSet ds = Util.getNewQueryDataSet(qStr);
    ds.open();
    return formatString(ds.getInt("brn")+"",5);
  }

  private String formatString(String str, int len)
  {
    if(str.length()>len)
      return str.substring(0,len);
    else
      return vl.maskString(str,'0',len);
  }
}
