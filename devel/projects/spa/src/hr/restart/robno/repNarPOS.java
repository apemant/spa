/****license*****************************************************************
**   file: repNarPOS.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.VTText;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.reports.mxRM;
import hr.restart.util.reports.mxReport;

import java.math.BigDecimal;
import java.util.StringTokenizer;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author REST-ART development team
 * @version 1.0
 */

public class repNarPOS extends mxReport {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();
  String[] detail = new String[1];
  hr.restart.pos.frmMasterBlagajna fmb = hr.restart.pos.frmMasterBlagajna.getInstance();
  repUtil ru = repUtil.getrepUtil();
  hr.restart.robno.sgQuerys sgq = hr.restart.robno.sgQuerys.getSgQuerys();
  String god = "";

  String porezString;
  int width = 40;
  int dbWidth = width/2;
  String doubleLineSep;
  boolean ispSif, oneRow;

  public repNarPOS() {
  }

  public void makeReport(){
    String wdt = frmParam.getParam("pos", "sirPOSnar", "42", 
            "Sirina pos ispisa. Preporuka 39 - 46", true);
    oneRow = frmParam.getParam("pos", "oneRow", "N",
    "Ispis raèuna u jednoj liniji (D,N)").equalsIgnoreCase("D");
    width = Integer.parseInt(wdt);
    dbWidth = width/2;
    doubleLineSep = getDoubleLineLength();
    makeIspis();
    super.makeReport();
  }

  private void makeIspis(){
     dm.getLogotipovi().open();
     QueryDataSet master = fmb.getMasterSet();
     god =master.getString("GOD");
//     hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
//     st.prn(master);
     this.setDataSet(fmb.getNarSet());
     String vc = fmb.getDestination();
     lD.raLocate(dm.getMxPrinterRM(), "CRM", vc);
     mxRM rm = new mxRM();
     rm.init(dm.getMxPrinterRM());
     setRM(rm);

     QueryDataSet prm = hr.restart.baza.Prod_mj.getDataModule().getTempSet("cprodmj = '"+master.getString("CPRODMJ")+"'");
     prm.open();
     String prodMjesto = prm.getString("NAZPRODMJ");
     String user = master.getString("CUSER");

     DataRow usr = lD.raLookup(hr.restart.baza.dM.getDataModule().getUseri(),"CUSER", user);
     String operater = usr.getString("NAZIV");

     ru.setDataSet(master);

     this.setPgHeader(
         "<$newline$>\u000E<#NARUDŽBA br. " + master.getInt("BRDOK") + "|"+((width-2)/2)+"|center#>\u0014<$newline$>"+
         "<$newline$>"+
         (fmb.getNarSet().getString("AKTIV").equalsIgnoreCase("D") ? "" :
           Aus.spc((width - 9) / 2) + "(kopija!)<$newline$>") + 
           "Stol: " + fmb.getStol() + "   Poslužio: " + operater + "<$newline$>" + 
           doubleLineSep + "<$newline$>"+ getDetailHeader() +
         doubleLineSep+""+getManualDetail());
     detail[0] = oneRow ? "<#NAZART|"+(width-8)+"|left#> <#KOL|7|right#>" :
         Aut.getAut().getCARTdependable(
             "<#RBR|3|right#> <#CART|7|left#> <#NAZART|"+(width-12)+"|left#><$newline$>",
             "<#RBR|3|right#> <#CART1|13|left#> <#NAZART|"+(width-18)+"|left#><$newline$>",
             "<#RBR|3|right#> <#BC|13|left#> <#NAZART|"+(width-18)+"|left#><$newline$>")+
                                        Aus.spc(width-14)+"<#JM|5|left#> <#KOL|8|right#>";
     //this.setDetail(detail);
     this.setRepFooter(
         doubleLineSep+"<$newline$><$newline$>"+
         "Nadnevak: "+raDateUtil.getraDateUtil().dataFormatter(
             master.getTimestamp("DATDOK"))+Aus.spc(width-38)+
         "Vrijeme: " + master.getTimestamp("DATDOK").
             toString().substring(11,19) + "<$newline$>"+
         "<$newline$><$newline$><$newline$>"+
         "<$newline$><$newline$><$newline$>"+
         //"\u001B\u0064\u0000"//+"\u0007" //"\07"
         getLastEscapeString()
    );
  }
  
  private String getManualDetail() {
    String data = "";
    DataSet ds = this.getDataSet();
    int slijed = 1;
    for (ds.first(); ds.inBounds(); ds.next()) {
      if (ds.getShort("SLIJED") > slijed) {
        slijed = ds.getShort("SLIJED");
        data = data + "<$newline$><$newline$>";
        data = data + "<#- " + slijed + ". SLIJED -|"+width+"|center#>";
        data = data + "<$newline$>";
      }
        
      slijed = ds.getShort("SLIJED");
      data = data + "<$newline$><#"+ds.getString("NAZART")+"|"+(width-8)+"|left#>";
      BigDecimal kol = ds.getBigDecimal("KOL");
      data = data + "<#"+kol+"|7|right#>";
      QueryDataSet vttx = VTText.getDataModule().getTempSet(Condition.equal(
          "CKEY", raControlDocs.getKey(ds, "stpos")));
      vttx.open();
      System.out.println(ds);
      System.out.println(raControlDocs.getKey(ds, "stpos"));
      if (vttx.getRowCount() > 0) {
        String[] lin = new VarStr(vttx.getString("TEXTFAK")).splitTrimmed(',');
        for (int i = 0; i < lin.length; i++) {
          data = data + "<$newline$>   "+lin[i];
        }
      }
    }
    return data;
  }
  
  private String getDetailHeader() {
    if (oneRow) return "NAZIV" + Aus.spc(width-8) + "KOL<$newline$>"; 
    return Aut.getAut().getCARTdependable(
        "RBR ŠIFRA   NAZIV<$newline$>",
        "RBR OZNAKA        NAZIV<$newline$>",
        "RBR BARCODE       NAZIV<$newline$>") +
        Aus.spc(width-14)+"JM    KOLIÈINA<$newline$>";
  }
  
  private String getLastEscapeString() {
    try {
      //int crm = dm.getMxPrinterRM().getInt("CRM");//jebiga
      String crm = fmb.getDestination();
      String str = frmParam.getParam("sisfun", "endnarPOSRM"+crm, "\\u001B\\u0064\\u0000", "Sekvenca koja dolazi na kraju ispisa POS narudžbe za rm "+crm);
//String str = "\\u0041\\u004e\\u0044\\u0052\\u0045\\u004a";
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

  private String getBlagajnaOperater(String blag, String user){
    String blop = hr.restart.sisfun.frmParam.getParam("pos","BlOp","0","Ispis i pozicija blagajne i operatora na malim raèunima (0,1,2)");
    if (!blop.equalsIgnoreCase("0")){
      DataRow usr = lD.raLookup(hr.restart.baza.dM.getDataModule().getUseri(),"CUSER", user);
      String operater = usr.getString("NAZIV");
      if (blop.equalsIgnoreCase("1")){
        return "BLAGAJNA: "+blag+"<$newline$>"+
               "OPERATER: "+operater+"<$newline$>";
      } else {
        return blag+", "+operater+"<$newline$>";
      }
    }
    return "";
  }

  private String getDoubleLineLength() {
   String dl = "";
   for (int i=1; i <= width; i++){
     dl += "=";
   }
   return dl;
  }
}
