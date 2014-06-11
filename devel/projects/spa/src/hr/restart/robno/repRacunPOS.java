/****license*****************************************************************
**   file: repRacunPOS.java
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
import hr.restart.pos.frmMasterBlagajna;
import hr.restart.pos.presBlag;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.reports.mxRM;
import hr.restart.util.reports.mxReport;
import hr.restart.zapod.OrgStr;

import java.math.BigDecimal;
import java.util.StringTokenizer;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author REST-ART development team
 * @version 1.0
 */

public class repRacunPOS extends mxReport {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();
  String[] detail = new String[1];
  repUtil ru = repUtil.getrepUtil();
  hr.restart.robno.sgQuerys sgq = hr.restart.robno.sgQuerys.getSgQuerys();
  String god = "";
  DataSet master;

  String porezString;
  int width = 40;
  int dbWidth = width/2;
  String doubleLineSep, uk, oib, specForm, pcorg, dw, nw;
  boolean ispSif, oneRow, pop, cash, isnac, isw;
  
  BigDecimal pov;

  public repRacunPOS() {

  }

  public void makeReport(){
    String wdt = frmParam.getParam("pos", "sirPOSpr", "41", 
            "Sirina pos ispisa. Preporuka 39 - 46", true);
    ispSif = frmParam.getParam("pos", "ispSifra", "N",
        "Ispis šifre na raèunima POS-a (D,N)", true).equalsIgnoreCase("D");
    oneRow = frmParam.getParam("pos", "oneRow", "N",
        "Ispis raèuna u jednoj liniji (D,N)").equalsIgnoreCase("D");
    uk = frmParam.getParam("pos", "iznosStavka", "UKUPNO",
    "Kolona iznosa koja se prikazuje na pos raèunu (UKUPNO,IZNOS,NETO)");
    pop = "D".equalsIgnoreCase(frmParam.getParam("pos", 
    "popustPrikaz", "N", "Prikaz popusta na pos raèunima (D,N)"));
    cash = "D".equalsIgnoreCase(frmParam.getParam("pos",
      "autoCash", "D", "Otvoriti blagajnu kod ispisa raèuna (D,N)"));
    oib = frmParam.getParam("robno", "oibMode", "MB", 
          "Staviti matièni broj (MB) ili OIB?");
    isnac = "D".equalsIgnoreCase(frmParam.getParam("pos", "ispisNacpl", 
        "D", "Ispis naèina plaæanja na POS raèunu (D,N)"));
    pov = Aus.getDecNumber(frmParam.getParam("robno", "iznosPov", "0.5",
    "Iznos povratne naknade"));
    specForm = frmParam.getParam("pos", "formatBroj", "",
        "Format broja raèuna na POS-u");
    pcorg = frmParam.getParam("pos", "posCorg", "",
      "OJ za logotip na POS-u");
    dw = getParamStr(frmParam.getParam("pos", "doubleWidth", "\\u000E", "Komanda za dvostruki ispis", true));
    nw = getParamStr(frmParam.getParam("pos", "normalWidth", "\\u0014", "Komanda za normalni ispis", true));
    isw = dw.length() > 0;
    
    if (pcorg == null || pcorg.length() == 0)
      pcorg = OrgStr.getKNJCORG(false);

    width = Integer.parseInt(wdt);
    System.out.println("WIDTH - "+ width);
    dbWidth = width/2;
    doubleLineSep = getDoubleLineLength();
    setData();
    makeIspis();
    super.makeReport();
  }
  
  public void setData() {
    frmMasterBlagajna fmb = frmMasterBlagajna.getInstance();
    master = fmb.getMasterSet();
    god =master.getString("GOD");
//    hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
//    st.prn(master);
    this.setDataSet(hr.restart.baza.Stpos.getDataModule().getTempSet("cskl='"+master.getString("CSKL")+"' and vrdok = 'GRC' and god =  '"+master.getString("GOD")+"' and brdok =  "+master.getInt("BRDOK")));
    this.getDataSet().open();
    
    String vc = fmb.getRacDestination();
    if (vc == null || vc.length() == 0) {
      fmb.setRacDestination();
      vc = fmb.getRacDestination();
    }
    lD.raLocate(dm.getMxPrinterRM(), "CRM", vc);
    mxRM rm = new mxRM();
    rm.init(dm.getMxPrinterRM());
    setRM(rm);
  }

  private String breakLines(String src, boolean center) {
    if (!center)
      return new VarStr(src).append('|').replaceAll("|", "<$newline$>").toString();
    
    String[] parts = new VarStr(src).split('|');
    VarStr buf = new VarStr();
    for (int i = 0; i < parts.length; i++)
      if (parts[i].length() == 0) buf.append("<$newline$>");
      else buf.append("<#").append(parts[i]).append('|').
        append(width).append("|center#><$newline$>");
    return buf.toString();
  }
  
  private void makeIspis(){
     dm.getLogotipovi().open();
     
     lD.raLocate(dm.getLogotipovi(), "CORG", pcorg);
     
     
     
     String kh = breakLines(dm.getLogotipovi().getString("NAZIVLOG"), true)+
     "<#"+dm.getLogotipovi().getString("ADRESA")+ ", " +String.valueOf(dm.getLogotipovi().getInt("PBR"))+" "+dm.getLogotipovi().getString("MJESTO") +"|"+width+"|center#><$newline$>"+
     "<#OIB "+dm.getLogotipovi().getString("OIB")+"|"+width+"|center#><$newline$>"+ getPhones();
     

     QueryDataSet sks = hr.restart.baza.Sklad.getDataModule().getTempSet("cskl = '"+master.getString("CSKL")+"'");
     QueryDataSet prm = hr.restart.baza.Prod_mj.getDataModule().getTempSet("cprodmj = '"+master.getString("CPRODMJ")+"'");
     sks.open();
     prm.open();
     
     String ph = kh;
     if (!sks.getString("CORG").equals(OrgStr.getKNJCORG(false)) &&
         lD.raLocate(dm.getLogotipovi(), "CORG", sks.getString("CORG"))) {
       ph = breakLines(dm.getLogotipovi().getString("NAZIVLOG"), true)+
       "<#"+dm.getLogotipovi().getString("ADRESA")+ ", " +String.valueOf(dm.getLogotipovi().getInt("PBR"))+
       " "+dm.getLogotipovi().getString("MJESTO") +"|"+width+"|center#><$newline$>"+ 
       (dm.getLogotipovi().getString("OIB").length()== 0 ? "" : "<#OIB "+
           dm.getLogotipovi().getString("OIB")+"|"+width+"|center#><$newline$>")+ getPhones();
     }

     String prodMjesto = prm.getString("NAZPRODMJ");
     String user = master.getString("CUSER");

     ru.setDataSet(master);
     
     String prep = frmParam.getParam("pos", "addHeader", "",
         "Dodatni header ispred POS raèuna", true);
     
     if (prep.length() > 0)
       prep = breakLines(prep, true);
     
     String post = frmParam.getParam("pos", "addHeaderAfter", "", 
           "Dodatni header iza zaglavlja", true);
     if (post.length() > 0) 
       post = breakLines(post, true);
     
     String th = frmParam.getParam("pos", "posHeader", "",
         "POS header (1 - poslovnica, knjigovodstvo  2 - obrnuto, ostalo - samo knjigovodstvo)");
     String header = kh;
     if (th.equals("1") && !kh.equals(ph))
       header = ph + kh;
     if (th.equals("2") && !kh.equals(ph))
       header = kh + ph;
     
     header = prep + header + post;

     this.setPgHeader(
         (cash ? "\u0007" : "")+header+
//         "<#"+prodMjesto+"|"+width+"|center#><$newline$>"+
         "<$newline$>Datum: "+raDateUtil.getraDateUtil().dataFormatter(master.getTimestamp("DATDOK"))+"  "+getRazlikaWidthBlank()+"Vrijeme: " + master.getTimestamp("DATDOK").toString().substring(11,19) +   "<$newline$>"+ 
         jeliR1(master.getInt("BRDOK"), master.getInt("CKUPAC"))+
         (oneRow ? "<$newline$>" : doubleLineSep+"<$newline$>")+ getDetailHeader() +
         doubleLineSep+getManualDetail());
     detail[0] = (!ispSif ? "<#RBR|3|right#>  <#NAZART|"+(width-6)+"|left#><$newline$>" :
         Aut.getAut().getCARTdependable("<#RBR|3|right#> <#CART|7|left#> <#NAZART|"+(width-12)+"|left#><$newline$>",
                                        "<#RBR|3|right#> <#CART1|13|left#> <#NAZART|"+(width-18)+"|left#><$newline$>",
                                        "<#RBR|3|right#> <#BC|13|left#> <#NAZART|"+(width-18)+"|left#><$newline$>"))+     /** @todo prilagodit cart, cart1, bc uvjetima */
          (!pop ? "<#KOL|9|right#>   <#JM|3|left#> <#MC|11|right#> <#"+uk+"|"+(width-28)+"|right#>"
              : "<#KOL|9|right#>  <#JM|3|left#> <#MC|9|right#>   <#PPOPUST1|5|right#> <#"+uk+"|"+(width-33)+"|right#>");
     if (!oneRow) this.setDetail(detail);
     this.setRepFooter(
         doubleLineSep+"<$newline$>"+//(oneRow ? "<$newline$>" : "")+ 
         getUkupno(master) +
//         "<#UKUPNO |26|left#> <#"+master.getBigDecimal("UKUPNO")+"|15|right#><$newline$>"+
//         "<#POPUST |26|left#> <#"+master.getBigDecimal("UIPOPUST1").add(master.getBigDecimal("UIPOPUST2"))+"|15|right#><$newline$>"+
//         "<#PLATITI |26|left#> <#"+master.getBigDecimal("NETO")+"|15|right#><$newline$>"+   //   %sum(IZNOS|15|right)%
         (isnac ? (oneRow ? "" : doubleLineSep + "<$newline$>")+
         /*"NAÈIN PLAÆANJA - "+*/getNacinPlacanja(master.getInt("BRDOK"),master.getString("CSKL")) : "")+//"<$newline$>"+
         (oneRow ? "" : doubleLineSep)+"<$newline$>"+
         porezString+
         "<$newline$>"+ getPotpis_i_MP(master.getInt("CKUPAC")) +/*"<$newline$>"+ */
         getFooting()+
//         "<#HVALA NA POVJERENJU !|"+width+"|center#><$newline$>"+
         "<$newline$>"+
//         "<$newline$>"+
         getBlagajnaOperater(prodMjesto,user)+"<$newline$>"+
         (presBlag.isFiskal(master) && master.getString("FOK").equals("D") ? getFisk() : "") +
         getLastPadding() +
         //"\u001B\u0064\u0000"//+"\u0007" //"\07"
         getLastEscapeString()
    );
  }
  
  private String getLastPadding() {
    int num = Aus.getAnyNumber(frmParam.getParam("pos", "paddLines", "6", "Broj praznih linija na kraju POS raèuna"));
    VarStr padd = new VarStr();
    for (int i = 0; i < num; i++) padd.append("<$newline$>");
    return padd.toString();
  }
  
 /* private String getNazivSplit(String naziv) {
    if (naziv.indexOf('|') <= 0) return    
      "<#"+naziv+"|"+width+"|center#><$newline$>";
    
    String[] lines = new VarStr(naziv).split('|');
    String ret = "";
    for (int i = 0; i < lines.length; i++)
      ret = ret + "<#"+lines[i]+"|"+width+"|center#><$newline$>";
    
    return ret;
  }*/
  
  private String getFisk() {
    System.out.println("fisk string");
    //System.out.println(frmMasterBlagajna.getInstance().rtype);
    return "ZKI: " + presBlag.getFis("GRC", master.getString("CSKL")).generateZKI(frmMasterBlagajna.getInstance().getRacType(master)) + "<$newline$>" +
      "JIR: " + master.getString("JIR") + "<$newline$><$newline$>";
  }
  
  private String getDetailHeader() {
    if (oneRow) return "NAZIV" + Aus.spc(width-24) +  "KOL  CIJENA   IZNOS<$newline$>";
    return (!ispSif ? "RBR  NAZIV<$newline$>" :
      Aut.getAut().getCARTdependable("RBR ŠIFRA   NAZIV<$newline$>",
                                     "RBR OZNAKA        NAZIV<$newline$>",
                                     "RBR BARCODE       NAZIV<$newline$>")
                                     )+   /** @todo prilagodit cart, cart1, bc uvjetima */
      (!pop ? " KOLIÈINA   JM       CIJENA       "+getRazlikaWidthBlank()+"IZNOS<$newline$>"
          : " KOLIÈINA  JM     CIJENA   % POP  "+Aus.spc(width-39)+"IZNOS<$newline$>");
  }
  
  private String getManualDetail() {
    if (!oneRow) return "";
    String data = "";
    DataSet ds = this.getDataSet();
    for (ds.first(); ds.inBounds(); ds.next()) {
      data = data + "<$newline$><#"+ds.getString("NAZART")+"|"+(width-19)+"|left#>";
      BigDecimal kol = ds.getBigDecimal("KOL");      
      if (kol.setScale(0, BigDecimal.ROUND_DOWN).compareTo(kol) == 0)
        data = data + "<#" + kol.intValue() + "|3|right#>"+
                  "<#" + ds.getBigDecimal("MC") + "|8|right#>"+
                  "<#" + ds.getBigDecimal(uk)+"|8|right#>";
      else data = data + "<#1|3|right#>"+
         "<#" + ds.getBigDecimal("MC").multiply(ds.getBigDecimal("KOL")).
           setScale(2, BigDecimal.ROUND_HALF_UP) + "|8|right#>"+
         "<#" + ds.getBigDecimal(uk)+"|8|right#><$newline$>"+
         "    ("+ds.getBigDecimal("KOL")+" "+ds.getString("JM")+" x "+
                 ds.getBigDecimal("MC")+")";
    }
    return data;
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
  
  private String getPotpis_i_MP(int ckupac){
    if (ckupac != 0 && hr.restart.sisfun.frmParam.getParam("pos","potpisMP","D","Mjesto za peèat i potpis na POS raèunu").equalsIgnoreCase("D"))
      return "<$newline$><#MP|"+width+"|center#><$newline$>"+
             "<#_______________________________________|"+width+"|center#><$newline$>"+"<$newline$>"+"<$newline$>";

    return "";
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

  BigDecimal izpov = Aus.zero2;
  private void calculatePorez(QueryDataSet dset){
    porezString = "";
    if (dset == null || dset.rowCount() < 1 ) return;
    dset.first();

    porezString = (oneRow ? "" : "<#P R E G L E D  P O R E Z A|"+width+"|center#><$newline$>")+
                  "<#NAZIV|6|left#> <#STOPA|8|right#> <#OSNOVICA|12|right#> <#POREZ|"+(width-29)+"|right#><$newline$>"+
                  doubleLineSep+"<$newline$>";
    
    do {
      porezString += "<#"+dset.getString("NAZPOR")+"|6|left#> <#"+sgq.format(dset.getBigDecimal("UKUPOR"),2)+"%|8|right#> <#"+sgq.format(dset.getBigDecimal("NETO").subtract(dset.getBigDecimal("POV").add(dset.getBigDecimal("POR1")).add(dset.getBigDecimal("POR2").add(dset.getBigDecimal("POR3")))),2)+"|12|right#> <#"+sgq.format(dset.getBigDecimal("POREZ"),2)+"|"+(width-29)+"|right#>"+ "<$newline$>";
      System.out.println(porezString); //XDEBUG delete when no more needed
    } while (dset.next());
    if (izpov.signum() > 0) {
      porezString = porezString + "<$newline$>" +
          "POVRATNA NAKNADA  " + izpov + "<$newline$>";
    }
    porezString = porezString + doubleLineSep+"<$newline$>";
  }

  private String getNacinPlacanja(int cnp, String cskl){
//    String nacini = "";
    QueryDataSet npos = ut.getNewQueryDataSet("SELECT nacpl.naznacpl as naznacpl, rate.cbanka, rate.irata as irata FROM rate,nacpl "+
                                              "WHERE rate.cnacpl = nacpl.cnacpl "+
                                              "and rate.brdok = " + cnp + " and rate.vrdok = 'GRC' and rate.god='"+ god + //Aut.getAut().getKnjigodRobno() +
                                              "' and rate.cskl= '" + cskl + "'");
    
//    System.out.println("SELECT max(nacpl.naznacpl) as naznacpl, sum(rate.irata) as irata FROM rate,nacpl,banke "+
//                                              "WHERE rate.cnacpl = nacpl.cnacpl "+
//                                              "and brdok = " + cnp + " and vrdok = 'GRC' and god='"+ Aut.getAut().getKnjigodRobno() +
//                                              "' and cskl= '" + cskl + "' group by naznacpl");
//    
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(npos);
    npos.first();
    if (npos.rowCount() == 0) return "";
    
    //TODO obrati paznju na sgQuerys.getSgQuerys().format(BD,int) ;)

    String np = (oneRow ? "" : "<#P R E G L E D  P L A Æ A N J A|"+width+"|center#><$newline$>"+
//                  "<#NAÈINA PLAÆANJA|21|left#>               IZNOS<$newline$>"+
                  doubleLineSep+"<$newline$>");
    do {
      String nac = npos.getString("NAZNACPL");
      if (npos.getString("CBANKA").trim().length() > 0)
        nac = nac + " - " + npos.getString("CBANKA"); 
      np +=  "<#"+nac+"|27|left#> <#"+sgq.format(npos.getBigDecimal("IRATA"),2)+"|"+(width-28)+"|right#><$newline$>";
    } while (npos.next());

//    np += doubleLineSep+"<$newline$>";

    return np; //npos.getString("NAZNACPL").toUpperCase();
  }

  private DataSet getRekapitulacija(DataSet ds) {
    dm.getArtikli().open();
    dm.getPorezi().open();
    QueryDataSet qds = new QueryDataSet();
    Column bjesansam1 = (Column) dm.getPorezi().getColumn("NAZPOR").clone();
    bjesansam1.setColumnName("CPOR");
    Column bjesansam2 = (Column) dm.getPorezi().getColumn("POR1").clone();
    bjesansam2.setColumnName("POREZ");

    qds.setColumns(new Column [] {
        (Column) dm.getStdoki().getColumn("CSKL").clone(),
        (Column) dm.getStdoki().getColumn("VRDOK").clone(),
        (Column) dm.getStdoki().getColumn("GOD").clone(),
        (Column) dm.getStdoki().getColumn("BRDOK").clone(),
        bjesansam1 ,
        (Column) dm.getPorezi().getColumn("NAZPOR").clone(),
        (Column) dm.getPorezi().getColumn("UKUPOR").clone(),
        (Column) dm.getStpos().getColumn("NETO").clone(),
        bjesansam2 ,
        (Column) dm.getStdoki().getColumn("POR1").clone(),
        (Column) dm.getStdoki().getColumn("POR2").clone(),
        (Column) dm.getStdoki().getColumn("POR3").clone(),
        dM.createBigDecimalColumn("POV"),
        new com.borland.dx.dataset.Column("KEY","KEY",com.borland.dx.dataset.Variant.STRING)});

    qds.open();
    ds.open();
    ds.first();
    izpov = Aus.zero2;
    do {
      lD.raLocate(dm.getArtikli(), new String[]{"CART"}, new String[]{String.valueOf(ds.getInt("CART"))});
      lD.raLocate(dm.getPorezi(), new String[]{"CPOR"}, new String[]{dm.getArtikli().getString("CPOR")});
      
//      System.out.println("netto - " + ds.getBigDecimal("NETO")); //XDEBUG delete when no more needed
//      
//      sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//      st.prn(ds);
      if ("D".equals(dm.getArtikli().getString("POV")))
        izpov = izpov.add(pov.multiply(ds.getBigDecimal("KOL")).
                  setScale(2, BigDecimal.ROUND_HALF_UP));

      for (int i = 1 ; i<4;i++) {
        if(!dm.getPorezi().getString("NAZPOR"+String.valueOf(i)).equals("")){
          String key = ds.getString("CSKL").concat("-").concat(
              ds.getString("GOD")).concat("-").concat(
              ds.getString("VRDOK")).concat("-").concat(
              String.valueOf(ds.getInt("BRDOK"))).concat("-").concat(
              dm.getPorezi().getString("NAZPOR"+String.valueOf(i)));

          if(!lD.raLocate(qds, new String[]{"KEY"}, new String[]{key})){
            qds.insertRow(true);
            qds.setString("CSKL", ds.getString("CSKL"));
            qds.setString("VRDOK", ds.getString("VRDOK"));
            qds.setString("GOD", ds.getString("GOD"));
            qds.setInt("BRDOK", ds.getInt("BRDOK"));
            qds.setString("CPOR", dm.getPorezi().getString("NAZPOR"+String.valueOf(i)));
            qds.setString("NAZPOR", dm.getPorezi().getString("NAZPOR"+String.valueOf(i)));

            qds.setBigDecimal("UKUPOR", dm.getPorezi().getBigDecimal("POR"+String.valueOf(i)));
            qds.setBigDecimal("NETO", ds.getBigDecimal("NETO"));
            qds.setBigDecimal("POREZ", ds.getBigDecimal("POR"+String.valueOf(i)));
            qds.setBigDecimal("POR1", ds.getBigDecimal("POR1"));
            qds.setBigDecimal("POR2", ds.getBigDecimal("POR2"));
            qds.setBigDecimal("POR3", ds.getBigDecimal("POR3"));
            if ("D".equals(dm.getArtikli().getString("POV")))
              qds.setBigDecimal("POV", pov.multiply(ds.getBigDecimal("KOL")).
                  setScale(2, BigDecimal.ROUND_HALF_UP));

            qds.setString("KEY", key);
          } else {
            qds.setBigDecimal("NETO", qds.getBigDecimal("NETO").
                              add(ds.getBigDecimal("NETO")));
            qds.setBigDecimal("POREZ", qds.getBigDecimal("POREZ").add(ds.getBigDecimal("POR"+String.valueOf(i))));
            qds.setBigDecimal("POR1", qds.getBigDecimal("POR1").add(ds.getBigDecimal("POR1")));
            qds.setBigDecimal("POR2", qds.getBigDecimal("POR2").add(ds.getBigDecimal("POR2")));
            qds.setBigDecimal("POR3", qds.getBigDecimal("POR3").add(ds.getBigDecimal("POR3")));
            if ("D".equals(dm.getArtikli().getString("POV")))
              Aus.add(qds, "POV", pov.multiply(ds.getBigDecimal("KOL")).
                  setScale(2, BigDecimal.ROUND_HALF_UP));
          }
        }
      }
    } while (ds.next());
    
    qds.setSort(new SortDescriptor(new String[] {"NAZPOR"}));
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(qds);
    return qds;
  }

  private String jeliR1(int brdok, int ckupac){
    System.out.println("\n\nKUPAC - "+ckupac+"\n\n");
    String knjig = hr.restart.zapod.OrgStr.getKNJCORG(false);
    String r1 = frmParam.getParam("robno", "izlazObr"+knjig,
        "R-1", "Vrsta obrasca ispisa raèuna za knjigovodstvo "+knjig);
    
    calculatePorez((QueryDataSet)getRekapitulacija(this.getDataSet()));
    if(ckupac != 0){
     /*System.out.println("Refresham...");
     hr.restart.baza.dM.getDataModule().getKupci().refresh();*/
      DataRow dr = lD.raLookup(hr.restart.baza.dM.getDataModule().getKupci(),"CKUPAC", ckupac+"");
      if (dr != null){
        String kupac = "<$newline$>";
        kupac += "Kupac: "+
            dr.getString("IME")+" "+dr.getString("PREZIME")+"<$newline$>"+
            ((!dr.getString("ADR").equals(""))?"       "+dr.getString("ADR")+"<$newline$>":"")+
            ((dr.getInt("PBR")!=0)?"       "+dr.getInt("PBR")+" ":"")+
            ((!dr.getString("MJ").equals(""))?((dr.getInt("PBR")==0)? "       "+dr.getString("MJ"):dr.getString("MJ")):"")+
            getJMBG(dr);

        kupac += "<$newline$><$newline$><#RAÈUN "+r1+" br. " + getBRDOK() + "|"+(width-2)+"|left#><$newline$>";
            //"\u000E<#"+ru.getFormatBroj()+"|"+((width-2)/2)+"|center#>\u0014<$newline$>";
        return kupac;
      } System.out.println("Kupac je (ako ga ima) null!!!");
    }
//    porezString = "";
    
    String ractex = "<$newline$><#RAÈUN br. " + getBRDOK() + "|"+(width-2)+"|left#><$newline$>";
    if (presBlag.isFiskal(master) && !master.getString("FOK").equals("D") && 
        !master.getTimestamp("DATDOK").before(Aus.createTimestamp(2013, 4, 1)))
      ractex = "<$newline$><#PREDRAÈUN br. " + getBRDOK() + "|"+(width-2)+"|left#><$newline$>";
    
    return ractex;
//        "\u001B\u0045<#"+ru.getFormatBroj()+"|20|center#>\u001B\u0046<$newline$>";
        //"\u000E<#"+ru.getFormatBroj()+"|"+((width-2)/2)+"|center#>\u0014<$newline$>";
  }
  
  public String getBRDOK() {
    if (presBlag.isFiskal(master) && master.getString("FOK").equals("D")) {
      return master.getInt("FBR") + "-" + master.getString("FPP") + "-" + master.getInt("FNU");
    }
    if (specForm == null || specForm.length() == 0)
      return Integer.toString(getDataSet().getInt("BRDOK"));

    return Aus.formatBroj(getDataSet(), specForm);
  }
  
  public String getJMBG(DataRow dr) {
    String result = "";
    if (!oib.equalsIgnoreCase("MB")) {
      String br = dr.getString("OIB");
      if (br.length() == 0) result = "";
      else result = "<$newline$>       OIB: " + br;
    } 
    if (oib.equalsIgnoreCase("MB") || result.length() == 0) {
      String mb = dr.getString("JMBG");
      if (mb.length() == 0) result = "";
      else result = "<$newline$>       MB: " + mb; 
    }   
    System.out.println(result);
    return result;
  }

  private String getUkupno(DataSet qds) {
    
    BigDecimal ukupno = ut.setScale(qds.getBigDecimal("UKUPNO"),2);
    BigDecimal ppop = ut.setScale(qds.getBigDecimal("UPPOPUST2"),2);
    BigDecimal popust = ut.setScale((qds.getBigDecimal("UIPOPUST1").add(qds.getBigDecimal("UIPOPUST2"))),2);
    BigDecimal neto = ut.setScale(qds.getBigDecimal("NETO"),2);
    
    String val = " " + hr.restart.zapod.Tecajevi.getDomOZNVAL();
    
    String izn = sgq.format(neto,2);
    if ((qds.getBigDecimal("UIPOPUST1").add(qds.getBigDecimal("UIPOPUST2"))).compareTo(Aus.zero2) == 0)
      return dw + "<#PLATITI|7|left#>" + nw + val + Aus.spc(width- val.length() - (isw ? 34 :17)) + dw + "<#"+izn+"|10|right#>" + nw + "<$newline$>";
    

    return
      "<#UKUPNO |26|left#> <#"+sgq.format(ukupno,2)+"|"+(width-26)+"|right#><$newline$>"+
      "<#POPUST |10|left#> <#"+sgq.format(ppop,2)+" %|15|left#> <#"+sgq.format(popust,2)+"|"+(width-26)+"|right#><$newline$>"+
      dw + "<#PLATITI|7|left#>" + nw + val + Aus.spc(width - val.length() - (isw ? 34 :17)) + dw + "<#"+izn+"|10|right#>" + nw + "<$newline$>";
//          "<#UKUPNO |26|left#> <#"+qds.getBigDecimal("UKUPNO")+"|15|right#><$newline$>"+
//          "<#POPUST |26|left#> <#"+qds.getBigDecimal("UIPOPUST1").add(qds.getBigDecimal("UIPOPUST2"))+"|15|right#><$newline$>"+
//          "<#PLATITI |26|left#> <#"+qds.getBigDecimal("NETO")+"|15|right#><$newline$>"
//          ;
  }

  private String getBlagajnaOperater(String blag, String user){
    String blop = hr.restart.sisfun.frmParam.getParam("pos","BlOp","0","Ispis i pozicija blagajne i operatora na malim raèunima (0,1,2,3)");
    if (!blop.equalsIgnoreCase("0")){
      DataRow usr = lD.raLookup(hr.restart.baza.dM.getDataModule().getUseri(),"CUSER", user);
      String operater = usr == null ? "" : usr.getString("NAZIV");
      if (!presBlag.isUserOriented()) {
        usr = lD.raLookup(hr.restart.baza.dM.getDataModule().getBlagajnici(),"CBLAGAJNIK", master.getString("CBLAGAJNIK"));
        operater = usr == null ? "" : usr.getString("NAZBLAG");
      }
      if (blop.equalsIgnoreCase("1")){
        return "BLAGAJNA: "+blag+"<$newline$>"+
               "OPERATER: "+operater+"<$newline$>";
      } else if (blop.equalsIgnoreCase("2")) {
        return blag+", "+operater+"<$newline$>";
      } else if (blop.equalsIgnoreCase("3")) {
        //return "Poslužio: "+operater+"<$newline$>"+
        //"Broj stola: " + getStol() + "<$newline$>";
        return "Stol: " + getStol() + "   Poslužio: " + operater + "<$newline$>";
      } else if (blop.equalsIgnoreCase("4")) {
        return "OPERATER: "+operater+"<$newline$>";
      }
      
    }
    return "";
  }
  
  protected String getStol() {
    return frmMasterBlagajna.getInstance().getStol();
  }

  private String getFooting(){
    String sadrzaj = "Hvala na povjerenju";
    String footing = "";
    if (!sadrzaj.equals("")){
      footing = "<#"+sadrzaj+"|"+width+"|center#><$newline$>";
    }
    if (!presBlag.isFiskPDV(master)) {      
      footing = breakLines(frmParam.getParam("pos", "noPDV", 
          "PDV nije obraèunat sukladno èlanku 90|stavak 1. zakona o PDV-u|",
        "Tekst za osloboðeno od poreza za obrtnike na POS raèunu"), true) + footing;
    }
    return footing;
  }
  
  private String getDoubleLineLength(){
   String dl = "";
   for (int i=1; i <= width; i++){
     dl += oneRow ? "-" : "=";
   }
   return dl;
  }
  
  private String getRazlikaWidthBlank(){
    String bl = "";
    for (int i=0; i <(width-41); i++){
      bl += " ";
    }
    return bl;
  }
}