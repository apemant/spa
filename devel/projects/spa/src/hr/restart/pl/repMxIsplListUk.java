/****license*****************************************************************
**   file: repMxIsplListUk.java
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
package hr.restart.pl;

import hr.restart.baza.dM;
import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.mxRM;
import hr.restart.util.reports.mxReport;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class repMxIsplListUk extends mxReport {
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo rpm = repMemo.getrepMemo();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  frmIspList fil = frmIspList.getInstance();
  public DataSet radnici = fil.getRadnici();
  private QueryDataSet detailDS = new QueryDataSet();
  lookupData ld = lookupData.getlookupData();
  Valid vl = Valid.getValid();
  int addLines = 0;

  //-> member varijable
  int _strana = 1;
  String _cOrg="";
  String _cOrgNaz="";
  String _firstLine= rpm.getFirstLine();
  boolean isArh = fil.isArh;
  String primanja,doprinosi, naknade, krediti;

  dM dm = dM.getDataModule();
  public repMxIsplListUk() {
   try
   {
     init();
   }
    catch (Exception ex) {
      ex.printStackTrace();
    }

 }
  public void init() throws Exception
  {
    setDataSet(detailDS);
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

    setRM(matIsp);

    fill();
  }

  private void fill()
  {

    radnici.open();
//    radnici.setSort(new SortDescriptor(new String[]{"CRADNIK"}));
    radnici.first();

    String detail ="";
    while (radnici.inBounds())
    {
      String nn=getNadNaslov();
      findStrings(radnici.getString("CRADNIK"));

      detail += "<$Reset$>"+"<$newline$>"+"      "+_firstLine+getDateSpace(_firstLine)+getDatIspl()+"<$newline$>"+
                "      "+ getCorg()+"-"+getNazivCorg()+getStrana()+"<$newline$>"+"<$newline$>"+//"   "+
                "      "+"<$DoubleWidthON$>"/*+ getRadnik()+" "*/+getPrezime().toUpperCase()+" ("+getRadnik()+")"+"<$DoubleWidthOFF$>"+"<$newline$>"+"<$newline$>"+"<$newline$>"+//"<$newline$>"+


                "<$DoubleWidthON$>"+"           ISPLATNI LISTI\u0106"+"<$DoubleWidthOFF$>"+"<$newline$>"+
                formatStr(nn,(int)((82-nn.length())/2)+nn.length())+"<$newline$>"+"<$newline$>"+"         "+


//                "<$DoubleWidthON$>"+ getRadnik()+" "+getPrezime()+"<$DoubleWidthOFF$>"+"<$newline$>"+"<$newline$>"+




                "   Radno mjesto: "+this.getRadnoMjesto()+" "+this.getNazivRadnogMjesta()+"<$newline$>"+"<$newline$>"+
                separator()+"<$newline$>"+
                "   " +getNaslov1()+primanja+
                separator()+
                "<$newline$>"+"   UKUPNO"+formatStr(formatIznos(radnici.getBigDecimal("SATI")),29)+
                formatStr(formatIznos(radnici.getBigDecimal("BRUTO")),26)+formatStr(formatIznos(radnici.getBigDecimal("NETO2")),14)+
                "<$newline$>"+"<$newline$>"+
                "   "+getNaslov2()+doprinosi+"<$newline$>"+
                "   Doprinosi ukupno"+formatStr(formatIznos(totalStopa),45)+formatStr(formatIznos(radnici.getBigDecimal("DOPRINOSI")),14)+"<$newline$>"+
                separator()+"<$newline$>"+
                "   DOHODAK"+formatStr(formatIznos(radnici.getBigDecimal("SATI")),29)+formatStr(formatIznos(radnici.getBigDecimal("NETO")),39)+"<$newline$>"+
                separator()+"<$newline$>"+
                "   Porezne olakšice     :"+formatStr(formatIznos(getKoefOlaksice()),6)+" * "+
                    formatStr(formatIznos(getMinimalac()),9)+formatStr(formatIznos(getNeoporezivo()),35)+"<$newline$>"+
                "   Premija životnog osiguranja                  :"+formatStr(formatIznos(get3_1_odIDa()),29)+"<$newline$>"+
                "   Premija dopunskog zdravstvenog osiguranja    :"+formatStr(formatIznos(get3_2_odIDa()),29)+"<$newline$>"+
                "   Premija dobrovoljnog mirovinskog osiguranja  :"+formatStr(formatIznos(get3_3_odIDa()),29)+"<$newline$>"+
                "   Iskorištene olakšice :"+formatStr(formatIznos(getNeoporezivo2()),53)+"<$newline$>"+
                "   Osnovica za porez    :"+formatStr(formatIznos(getPorezOsnovica()),53)+"<$newline$>"+
                "   Porez 1              :"+formatStr(formatIznos(getPorez15()),53)+"<$newline$>"+
                "   Porez 2              :"+formatStr(formatIznos(getPorez25()),53)+"<$newline$>"+
                "   Porez 3              :"+formatStr(formatIznos(getPorez35()),53)+"<$newline$>"+
                "   Porez 4              :"+formatStr(formatIznos(getPorez45()),53)+"<$newline$>"+
                "   Porez ukupno"+formatStr(formatIznos(radnici.getBigDecimal("PORUK")),63)+"<$newline$>"+separator()+"<$newline$>"+
                "   Prirez               :"+formatStr(formatIznos(radnici.getBigDecimal("PRIR")),53)+"<$newline$>"+
                "   Porez i prirez ukupno:"+formatStr(formatIznos(radnici.getBigDecimal("PORIPRIR")),53)+"<$newline$>"+separator()+"<$newline$>"+
                "   NAKNADE                        SATI"+"<$newline$>"+naknade +"<$newline$>"+
                "   Naknade ukupno"+formatStr(formatIznos(getTotalNaknade()),61)+"<$newline$>"+
                "   Netto prije odbitaka"+formatStr(formatIznos(getNetoPlusNaknade()),55)+"<$newline$>"+separator()+"<$newline$>"+
                "   ODBICI"+"<$newline$>"+krediti+"<$newline$>"+
                "   Odbici ukupno"+formatStr(formatIznos(getTotalKrediti()),62)+"<$newline$>"+separator()+"<$newline$>"+
                "   "+getIsplataString().trim()+formatStr(getLovaNaRukeKeshovina(),75-getIsplataString().length())+addLinesEndPage();
      _strana++;

      radnici.next();
    }

    this.setDetail(new String[]{detail});

  }
//-> metode za formatiranje
  private String formatStr(String str, int length)
  {
    String temp ="";
    for(int i = 0; i<(length-str.length());i++)
    {
      temp=" "+temp;
    }
    return temp+str;
  }

//-> metode za dohvat podataka
  private String getDateSpace(String fl)
  {
    int len = 61 - fl.length();
    String razmak ="";
    for (int i=0; i < len;i++)
      razmak+=" ";
    return razmak;
  }

  private String getDatIspl()
  {
    return rdu.dataFormatter(radnici.getTimestamp("DATISP"));
  }

  public String getCorg() {
    _cOrg = radnici.getString("CORG");
    return _cOrg;
  }

  public String getNazivCorg() {
    fil.ld.raLocate(dm.getOrgstruktura(), new String[] {"CORG"}, new String[] {radnici.getString("CORG")});
    _cOrgNaz=dm.getOrgstruktura().getString("NAZIV");
    return _cOrgNaz;
  }

  public String getStrana()
  {
    int len = 58 - _cOrg.length()-_cOrgNaz.length();
    String razmak ="";
    for (int i=0; i < len;i++)
      razmak+=" ";
    return razmak+ "Strana:"+ formatStr(_strana+"", 5);
  }

  public String getNadNaslov(){
     if (fil.getRepMode() == 'A')
       return fil.getObracun(radnici.getShort("GODOBR"), radnici.getShort("MJOBR"), radnici.getShort("RBROBR"));
     return fil.getObracun();
  }

  public String getRadnik() {
    return radnici.getString("CRADNIK");
  }

  public String getPrezime() {
    return radnici.getString("PREZIME").concat(" " + radnici.getString("IME"));
  }

  public String getRadnoMjesto() {
    return  cradmj;
  }

  public String getNazivRadnogMjesta() {
    return nazradmj;
  }

  public String separator()
  {
    return "  -----------------------------------------------------------------------------";
  }

  public String getNaslov1()
  {
    return "VRSTE  PRIMANJA                SATI     U\u010CINAK%        BRUTTO         NETTO"+"<$newline$>";
  }

  public String getNaslov2()
  {
    return "DOPRINOSI                              OSNOVICA         STOPA         IZNOS"+"<$newline$>";
  }


  //-> rastezljivi stringovi
  public String getPrimanja() {
    return fil.getPrimanja();
  }

  public String getSati() {
    return fil.getSati();
  }

  public String getUcinak() {
    return fil.getUcinak();
  }

  public String getBruto() {
    return fil.getBruto();
  }

  public String getNeto() {
    return fil.getNeto();
  }

  public String getDoprinosi() {
    return fil.getDoprinosi();
  }

  public String getOsnovicaDop() {
    return fil.getOsnovicaDoprinosa();
  }

  public String getStopa() {
    return fil.getStopa();
  }

  public String getIznosDoprinosa() {
    return fil.getIznosDoprinosa();
  }

  public String getNaknade() {
    return fil.getNaknade();
  }

  public String getIznosNaknada() {
    return fil.getIznosNaknada();
  }

  public String getKrediti() {
    return fil.getKrediti();
  }

  public String getIznosKredita() {
    return fil.getIznosKredita();
  }


  //-> find strings

  QueryDataSet SQLprimanja = new QueryDataSet();
  BigDecimal totalStopa;
  String nazivPrim, sati, koef, neto, bruto, nazivDop, osnovicaDop, stopa, iznos;
  String nazivNak, iznosNak, nazivKred, iznosKred;
  String cradmj, nazradmj;
  String brojtek, nazbanke, tipIsplate;
  short cisplmj;

  public QueryDataSet getPrimanjaSet(String rad) {


    String sql = "SELECT primanja"+(isArh?"arh":"obr")+".*, vrsteprim.naziv FROM primanja"+(isArh?"arh":"obr")+", vrsteprim "+
                 "WHERE cradnik = '"+rad+"' AND primanja"+(isArh?"arh":"obr")+".cvrp = vrsteprim.cvrp "+
                fil.getBetweenAhrQuery("primanjaarh") +" "+
                 "ORDER BY "+(isArh?"primanjaarh.godobr, primanjaarh.mjobr, primanjaarh.rbrobr,":"")+" primanja"+(isArh?"arh":"obr")+".cvrp, primanja"+(isArh?"arh":"obr")+".rbr";


    SQLprimanja.close();
    SQLprimanja.closeStatement();
    SQLprimanja.setQuery(new QueryDescriptor(dm.getDatabase1(),sql));
    SQLprimanja.open();

    return SQLprimanja;
  }
  public void findStrings(String crad) {
    DataSet ds = getPrimanjaSet(crad);
    StringBuffer _nazivN = new StringBuffer();
    StringBuffer _iznosN = new StringBuffer();

    primanja ="";
    doprinosi = "";
    naknade = "";
    krediti ="";
    addLines = 0;
    for (ds.first(); ds.inBounds(); ds.next()) {
      ld.raLocate(fil.getVrprim(), new String[] {"CVRP"}, new String[] {""+ds.getShort("CVRP")});
      if (raParam.getParam(fil.getVrprim(), 1).equals("D")) {  // Primanja
        String pr = "   "+fil.getVrprim().getString("NAZIV");
        String s = format(ds, "SATI");
        String k = format(ds, "KOEF");
        String b = format(ds, "BRUTO");
        String n = format(ds, "NETO");
        primanja +=pr+formatStr(s, (38-pr.length()))+formatStr(k, 12)+formatStr(b, 14)+formatStr(n, 14)+"\n";
        addLines++;

      }
      if (raParam.getParam(fil.getVrprim(), 1).equals("N") &&
          raParam.getParam(fil.getVrprim(), 2).equals("N")) {  // Naknade
        String n ="   "+ fil.getVrprim().getString("NAZIV");
        String in = format(ds, "NETO");
        String s = format(ds, "SATI");
//        naknade += n+formatStr(in, (78-n.length()))+"\n";  formatStr(formatIznos(radnici.getBigDecimal("SATI")),29)
//        naknade += n + formatStr(s, (20-s.length())) + formatStr(in, (78-n.length()-s.length()))+"\n"; // was 78-n.length())
        naknade += n + formatStr(s,38-n.length()) + formatStr(in, (51-n.length()))+"\n";
        addLines++;
      }
    }

    ds = fil.getDoprinosiSet(crad);
    totalStopa = new BigDecimal(0.0);

    for (ds.first(); ds.inBounds(); ds.next()) {
      ld.raLocate(fil.getVrodb(), new String[] {"CVRODB"}, new String[] {""+ds.getShort("CVRODB")});
      String d ="   "+fil.getVrodb().getString("OPISVRODB");
      String o = format(ds, "OBROSN");
      String st = format(ds, "OBRSTOPA");
      String i = format(ds, "OBRIZNOS");
      doprinosi +=d+formatStr(o, (50-d.length()))+formatStr(st, 14)+formatStr(i, 14)+"\n";
      totalStopa = totalStopa.add(ds.getBigDecimal("OBRSTOPA"));
      addLines++;
    }

    ds = fil.getKreditiSet(crad);

    for (ds.first(); ds.inBounds(); ds.next()) {
      ld.raLocate(fil.getVrodb(), new String[] {"CVRODB"}, new String[] {""+ds.getShort("CVRODB")});
      String nk ="   "+ fil.getVrodb().getString("OPISVRODB");
      String ik = format(ds, "OBRIZNOS");
      krediti += nk+formatStr(ik, (78-nk.length()))+"\n";
      addLines++;
    }

    ld.raLocate(fil.radpl, "CRADNIK", crad);
    cradmj = fil.radpl.getString("CRADMJ");
    brojtek = fil.radpl.getString("BROJTEK");
    cisplmj = fil.radpl.getShort("CISPLMJ");
//    System.out.println("cisplmj: " + cisplmj);
    ld.raLocate(fil.radmj, "CRADMJ", cradmj);
    nazradmj = fil.radmj.getString("NAZIVRM");
    ld.raLocate(dm.getIsplMJ(), "CISPLMJ" ,String.valueOf(cisplmj));
    tipIsplate = dm.getIsplMJ().getString("TIPISPLMJ");
//    System.out.println("tispl: " + tipIsplate);
    ld.raLocate(dm.getBankepl(), "CBANKE" ,String.valueOf(dm.getIsplMJ().getInt("CBANKE")));
    nazbanke = dm.getBankepl().getString("NAZBANKE");

  }

  protected String format(DataSet set, String colName) {
    com.borland.dx.text.VariantFormatter formater = dm.getVrsteodb().getColumn("IZNOS").getFormatter();
    com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
    set.getVariant(colName,v);
    return formater.format(v);
  }

  private String formatIznos(BigDecimal izn)
 {
    if(izn.toString().equals("0"))
    {
      izn = Aus.zero2;
    }
  try {
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
     return finalStr;
  }
  catch (Exception ex) {
    ex.printStackTrace();
    return null;
  }
  }

  public BigDecimal getKoefOlaksice() {
    return radnici.getBigDecimal("NEOP").divide(getMinimalac(), 2, BigDecimal.ROUND_HALF_UP);
  }

  public BigDecimal getNeoporezivo() {
    return radnici.getBigDecimal("NEOP");
  }

  public BigDecimal getNeoporezivo2() {
    return radnici.getBigDecimal("ISKNEOP");
  }

  public BigDecimal getMinimalac() {
    return dm.getParametripl().getBigDecimal("MINPL");
  }

  public BigDecimal get3_1_odIDa(){
    return radnici.getBigDecimal("ZIVOTNOOSIG");
  }

  public BigDecimal get3_2_odIDa(){
    return radnici.getBigDecimal("ZDRAVSTVENOOSIG");
  }

  public BigDecimal get3_3_odIDa(){
     return radnici.getBigDecimal("MIROVINSKOOSIG");
  }

  public BigDecimal getNeoporezivoIskoristeno() {
    return radnici.getBigDecimal("ISKNEOP");
  }

  public BigDecimal getPorezOsnovica() {
    return radnici.getBigDecimal("POROSN");
  }

  public BigDecimal getPorez15() {
    return radnici.getBigDecimal("POR1");
  }

  public BigDecimal getPorez25() {
    return radnici.getBigDecimal("POR2");
  }

  public BigDecimal getPorez35() {
    return radnici.getBigDecimal("POR3");
  }

  public BigDecimal getPorez45() {
    return radnici.getBigDecimal("POR4");
  }

  public BigDecimal getTotalPorez() {
    return radnici.getBigDecimal("PORUK");
  }

  public BigDecimal getPrirez() {
    return radnici.getBigDecimal("PRIR");
  }

  public BigDecimal getTotalPorezPrirez() {
    return radnici.getBigDecimal("PORIPRIR");
  }

  public BigDecimal getTotalNaknade() {
    return radnici.getBigDecimal("NAKNADE");
  }

  public BigDecimal getNetoPlusNaknade() {
    return radnici.getBigDecimal("NETOPK");
  }

  public BigDecimal getTotalKrediti() {
    return radnici.getBigDecimal("KREDITI");
  }

  public String getLovaNaRukeKeshovina() {
    if (fil.getPrikazIsplate())
      return fil.format(radnici, "NARUKE");
    else
      return "";
  }

 private String addLinesEndPage()
 {
   String lines="";
   for(int i = 0; i<(25-addLines);i++)
     lines += "<$newline$>";
   return lines;
 }

 public String getIsplataString(){
   String isplataString = "";
   if (tipIsplate.equals("T")){
     isplataString = "Isplata - ".concat(brojtek).concat(", ").concat(nazbanke);
   } else if (tipIsplate.equals("G")) {
     isplataString = "Isplata u gotovini";
   } else if (tipIsplate.equals("S")) {
     isplataString = "Isplata na štednu knjižicu";
   }
   return isplataString;
 }

}

//