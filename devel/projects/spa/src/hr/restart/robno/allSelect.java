/****license*****************************************************************
**   file: allSelect.java
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
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;

import java.util.ArrayList;

public class allSelect {
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  String queryString = "";
  static boolean ispisDA = true;
  public allSelect() {
    queryString ="";
  }

  public void ispisQuery(){
    if (ispisDA)
      System.out.println(queryString);
  }

  public String getS4raCatchDoc(String godc,String cskl,
                        String odabrana_vrsta_dok,String dodatak,boolean isCORG)  {

    if (!isCORG) {
      return getS4raCatchDoc(godc,cskl,odabrana_vrsta_dok,dodatak);
    }
    return "statira='N' and "+godc+" vrdok= '"+odabrana_vrsta_dok+"'"+dodatak+
                    " and cskl in (select cskl from sklad where sklad.corg in ('"+cskl+"') "+
                    " or sklad.knjig in ('"+cskl+"')) "; //samo trenutno
  }

  public String getS4raCatchDoc(String godc,String cskl,
                        String odabrana_vrsta_dok,String dodatak)  {

    if (odabrana_vrsta_dok.equalsIgnoreCase("OTP") || odabrana_vrsta_dok.equalsIgnoreCase("IZD")) {
      queryString = "statira='N' and "+godc+" vrdok= '"+odabrana_vrsta_dok+"'"+dodatak+
                    " and cskl in (select cskl from sklad where sklad.corg in ('"+cskl+"') "+
                    " or sklad.knjig in ('"+cskl+"')) "; //samo trenutno
     } else {
       queryString = "statira='N' and "+godc+" cskl='" +cskl +"' "+dodatak+
                     " and vrdok= '"+odabrana_vrsta_dok+"'" ; //samo trenutno
     }
     ispisQuery();
     return queryString;
  }

  public String getS4raCatchDocRN(String god,String cskl,String dodatak)  {

/*     queryString = "select * from RN where "+
                "status='O' and aktiv='D' and god = '"+god+"' "+dodatak ;*/
     boolean lazyIZD = frmParam.getParam("robno", "lazyIZD", "N", "Dopustiti prebacivanje" +
            " radnih naloga u raèune prije izdavanja robe? (D,N)").equalsIgnoreCase("D");

     String gc = " and god in ('" + god + "','" + (Aus.getNumber(god) - 1) + "') ";
     
     queryString = (lazyIZD ? "(status='O' or status='P')" : "status='O'") +
         " and cskl='"+cskl+"' " + gc + dodatak ;

     ispisQuery();
     return queryString;
  }

  public String getS4raGRC(String god,String cskl) {

     queryString = "select cskl,god,vrdok,brdok from rn where "+
                "god = '"+god+"' and cskl='" +cskl +
                "' and status='P'" ; //samo trenutno
     ispisQuery();
     return queryString;
  }


  public String getQuery4rCD4findZaglavlje(String god,String cskl,String vrdok,
                                           int brdok ){
     queryString = "select * from doki where "+
            "god = '"    +god+"' "+
            " and cskl='" +cskl+"' "+
            " and vrdok='"+vrdok+"' "+
            " and brdok=" +brdok;
     ispisQuery();
     return queryString;
  }

  public String getQuery4rCD4findStavke(String god,String cskl,String vrdok,
                                           int brdok ){
     queryString =  "select * from stdoki where "+
             " god = '"    +god+"' "+
             " and cskl='" +cskl+"' "+
             " and vrdok='"+vrdok+"' "+
             " and brdok=" +brdok;
     ispisQuery();
     return queryString;
  }

  public String getQuery4rFP4getClone(int uvijet) {
    queryString = "select * from pjpar where cpar = "+uvijet;
    ispisQuery();
    return queryString;
  }

  public String getQuery4rDDZT4findRabat(String cskl,String vrdok,
                                 String god,int brdok, int rbr){
    queryString = "select vtzavtr.lrbr,vtzavtr.czt,zavtr.nzt, vtzavtr.pzt,"+
         "vtzavtr.ztnazt from zavtr,vtzavtr where zavtr.czt=vtzavtr.czt and " +
         "vtzavtr.cskl = '"  + cskl+"' and "+
         "vtzavtr.vrdok = '" + vrdok+"' and "+
         "vtzavtr.god = '"   + god+"' and "+
         "vtzavtr.brdok = "  + brdok + " and "+
         "rbr="+rbr+" order by lrbr";
    ispisQuery();
    return queryString;
  }

  public String getQuery4RbrCD4vrati_rbr(String tabela,String uvjet){
    queryString = "select max(rbr) as mrbr from " + tabela +" "+ uvjet ;
    ispisQuery();
    return queryString;
  }

  public String getQuery4RbrCD4vrati_rbr(String tabela,String field,String uvjet){
    queryString = "select max("+field+") as mrbr from " + tabela +" "+ uvjet ;
    ispisQuery();
    return queryString;
  }

  public String getQuery4RbrCD4vrati_rbr(String tabela,String skladiste,
                                         String vrdok,String god,int brdok){
    queryString = "select max(rbr) as mrbr from " + tabela + " where cskl = '" + skladiste + "'" +
                  " and vrdok='"+vrdok+"' and god='"+god+"' and brdok=" + brdok;// + "'";
    ispisQuery();
    return queryString;
  }

  public String getQuery4RbrCD4vrati_rbr_mes(String tabela,String skladiz,
                              String skladul,String vrdok,String god,int brdok){
    queryString = "select max(rbr) as mrbr from " + tabela +
                  " where cskliz = '" + skladiz + "' " +
                  " and csklul = '"+skladul + "' "+
                  " and vrdok='"+vrdok+"' and god='"+god+
                  "' and brdok=" + brdok; //+ "'";
    ispisQuery();
    return queryString;
  }

  public String getQuery4aP4findCPOR(String text){
    queryString = "select * from porezi where CPOR = '" + text + "'";
    ispisQuery();
    return queryString;
  }

  public String getQuery4aP4findCPORART(int cart){
    queryString = "select * from porezi,artikli where porezi.CPOR = artikli.CPOR and cart =" + cart;
    ispisQuery();
    return queryString;
  }

  public String getQuery4rDDRB4findRabat(String cskl,String vrdok,String god,
                                          int brdok, int rbr){

    queryString = "select vtrabat.lrbr,vtrabat.crab,rabati.nrab, vtrabat.prab,"+
         "vtrabat.rabnarab from rabati,vtrabat where rabati.crab=vtrabat.crab and " +
         "vtrabat.cskl = '"  + cskl +"' and "+
         "vtrabat.vrdok = '" + vrdok+"' and "+
         "vtrabat.god = '"   + god  +"' and "+
         "vtrabat.brdok = "  + brdok + " and "+
         "rbr="+rbr+" order by lrbr";
    ispisQuery();
    return queryString;
  }

  public String getQuery4rSA4raStanje_Artfilter(){
    queryString  = "select stanje.god, stanje.cskl,stanje.cart,"+
                        "stanje.kolps,stanje.kolul,stanje.koliz,stanje.kolrez,"+
                        "stanje.nabps,stanje.marps,stanje.porps,stanje.vps,stanje.nabul,"+
                        "stanje.marul,stanje.porul,stanje.vul,stanje.nabiz,stanje.mariz,"+
                        "stanje.poriz,stanje.viz,stanje.kol,stanje.zc,stanje.vri,stanje.nc as snc,"+
                        "stanje.vc as svc,stanje.mc as smc,"+
                        "artikli.cart1,artikli.bc,artikli.nazart,artikli.jm,artikli.cpor,"+
                        "artikli.cgrart,artikli.tipart,artikli.vrart,artikli.nazproiz,artikli.sifzanar,"+
                        "artikli.nazorig,artikli.kolzanar,artikli.sigkol,artikli.minkol,artikli.dc,artikli.zt,"+
                        "artikli.nc as anc,artikli.pmar,artikli.vc as avc,artikli.ipor,artikli.mc as amc,"+
                        "artikli.nazpak,artikli.jmpak,artikli.brjed,artikli.tezpak,artikli.isb,artikli.nazsb,"+
                        "artikli.oznval,artikli.ncref,artikli.vcref,artikli.mcref from stanje,artikli where "+
                        "artikli.cart=stanje.cart"  ;
    ispisQuery();
    return queryString;
  }

  public String getQuery4rSA4raStanje_Artfilter(String cskl,String god){
    queryString = getQuery4rSA4raStanje_Artfilter()+
        " and stanje.cskl='"+cskl+"' and stanje.god='"+god+"'";
    ispisQuery();
    return queryString;
  }

  public String getQuery4rEDBM4DeleteVTrabat(String cskl,
                                             String vrdok,
                                             String god,
                                             int brdok,
                                             int rbr1) {

    queryString ="delete from vtrabat where " +
         "cskl  = '" + cskl  +"' and "+
         "vrdok = '" + vrdok +"' and "+
         "god   = '" + god   +"' and "+
         "brdok = "  + brdok +" and "+
         "rbr="      + rbr1;

    ispisQuery();
    return queryString;
  }

  public String getQuery4rEDBM4DeleteVTrabat() {

    queryString ="delete from vtrabat where " +
         "cskl  = ? and vrdok = ? and god  = ? and brdok = ? and rbr= ?";
    ispisQuery();
    return queryString;
  }

  public String getQuery4rEDBM4DeleteVTrabatQ(String cskl,String vrdok, String god,int brdok,int rbr) {

    queryString ="select * from vtrabat where " +
         "cskl  = '"+cskl+"' and vrdok = '"+vrdok+"' and god  = '"+god+"' and brdok = "+brdok+
         " and rbr= "+rbr;
    ispisQuery();
    return queryString;
  }

  public String getQuery4rEDBM4InsertVTrabat(String cskl,
                                             String vrdok,
                                             String god,
                                             int brdok,
                                             int rbr1,String cshrab) {

    queryString = "insert into vtrabat " +
         "select lokk,aktiv,'"+cskl+"' as cskl,'"+
         vrdok+"' as vrdok,'"+
         god+"' as god,"+
         brdok+" as brdok,"+
         rbr1+" as rbr, rbr as lrbr,crab,prab,0 as irab,rabnarab from vshrab_rab where cshrab = '"+
         cshrab+"'";
    ispisQuery();
    return queryString;
  }


  public String getQuery4rEDBM4SelectVTrabat(String cskl,
                                             String vrdok,
                                             String god,
                                             int brdok)  {

    queryString ="select * from vtrabat where " +
         "cskl  = '" + cskl  +"' and "+
         "vrdok = '" + vrdok +"' and "+
         "god   = '" + god   +"' and "+
         "brdok = "  + brdok ;

    ispisQuery();
    return queryString;
  }

  public String getQuery4rEDBM4DeleteVTzavtr(){
    queryString = "delete from vtzavtr where cskl = ? and "+
         "vrdok = ? and god = ? and brdok = ? and rbr= ?";

    ispisQuery();
    return queryString;
  }

  public String getQuery4rEDBM4SelectVTzavtr(String cskl,
                                             String vrdok,
                                             String god,
                                             int brdok) {

    queryString = "select * from vtzavtr where " +
         "cskl = '"  + cskl +"' and "+
         "vrdok = '" + vrdok+"' and "+
         "god = '"   + god  +"' and "+
         "brdok = "  + brdok;
    ispisQuery();
    return queryString;
  }

  public String getQuery4rEDBM4DeleteVTzavtr(String cskl,
                                             String vrdok,
                                             String god,
                                             int brdok,
                                             int rbr1) {

    queryString = "delete from vtzavtr where " +
         "cskl = '"  + cskl +"' and "+
         "vrdok = '" + vrdok+"' and "+
         "god = '"   + god  +"' and "+
         "brdok = "  + brdok   +" and "+
         "rbr="      + rbr1;
    ispisQuery();
    return queryString;
  }

  public String getQuery4rEDBM4InsertVTzavtr(String cskl,
                                             String vrdok,
                                             String god,
                                             int brdok,
                                             int rbr1,String cshzt) {

    queryString = "insert into vtzavtr " +
         "select lokk,aktiv,'"+cskl+"' as cskl,'"+
         vrdok+"' as vrdok,'"+
         god+"' as god,"+
         brdok+" as brdok,"+
         rbr1+" as rbr,rbr as lrbr, czt,  pzt, 0 as izt,0 as uiprpor, ztnazt,'' as brojkonta "+
         "from vshztr_ztr where cshzt = '"+cshzt+"'";
    ispisQuery();
    return queryString;
  }

  public String getQuery4rRSselectStdoku(String cskl,String god, int cart){
    queryString = "select * from doku, stdoku  where " +
          "doku.cskl=stdoku.cskl and doku.vrdok=stdoku.vrdok and "+
          "doku.god=stdoku.god and doku.brdok=stdoku.brdok ";
    if (!cskl.equals("")) {
      queryString= queryString.concat(" and doku.cskl='"+cskl+"'");
    }
    if (!god.equals("")) {
      queryString= queryString.concat(" and doku.god='"+god+"'");
    }
    if (cart!=0) {
      queryString= queryString.concat(
          " and stdoku.cart="+cart+" ");
    }
    queryString= queryString.concat(" order by cart");
    ispisQuery();
    return queryString;
  }

  public String getQuery4rRSselectStdoki(String cskl,String god,int cart){
    queryString = "select * from doki, stdoki where " +
          "doki.cskl=stdoki.cskl and doki.vrdok=stdoki.vrdok and "+
          "doki.god=stdoki.god and doki.brdok=stdoki.brdok ";
    if (!cskl.equals("")) {
      queryString= queryString.concat(" and doki.cskl='"+cskl+"' ");
    }
    if (!god.equals("")) {
      queryString= queryString.concat(" and doki.god='"+god+"' ");
    }
    if (cart!=0) {
      queryString= queryString.concat(
          " and stdoki.cart="+cart+" ");
    }

    queryString= queryString.concat("order by cart");
    ispisQuery();
    return queryString;
  }

  public String getQuery4rRSselectStmesklaUL(String cskl,String god,int cart){
    queryString =
         "select * from meskla, stmeskla where meskla.csklul= stmeskla.csklul "+
         " and meskla.cskliz=stmeskla.cskliz and meskla.vrdok=stmeskla.vrdok "+
         " and meskla.god=stmeskla.god and meskla.brdok=stmeskla.brdok and "+
         "(meskla.vrdok='MES' or meskla.vrdok='MEU') ";
    if (!cskl.equals("")) {
      queryString= queryString.concat(
          " and meskla.csklul='"+cskl+"' ");
    }
    if (cart!=0) {
      queryString= queryString.concat(
          " and stmeskla.cart="+cart+" ");
    }

    if (!god.equals("")) {
      queryString= queryString.concat(" and meskla.god='"+god+"' ");
    }
      queryString= queryString.concat(" order by cart");
    ispisQuery();
    return queryString;
  }

  public String getQuery4rRSselectStmesklaIZ(String cskl,String god,int cart){
    queryString =
         "select * from meskla, stmeskla where meskla.csklul= stmeskla.csklul "+
         " and meskla.cskliz=stmeskla.cskliz and meskla.vrdok=stmeskla.vrdok "+
         " and meskla.god=stmeskla.god and meskla.brdok=stmeskla.brdok and "+
         "(meskla.vrdok='MES' or meskla.vrdok='MEI') ";
    if (!cskl.equals("")) {
      queryString= queryString.concat(
          " and meskla.cskliz='"+cskl+"' ");
    }
    if (!god.equals("")) {
      queryString= queryString.concat(" and meskla.god='"+god+"'");
    }
    if (cart!=0) {
      queryString= queryString.concat(
          " and stmeskla.cart="+cart+" ");
    }

    queryString= queryString.concat(" order by cart");
    ispisQuery();
    return queryString;
  }

  public String getQuery4rRSselectAllStanje(String cskl,String god,int cart){
    queryString =
         "select * from stanje ";
    if (!cskl.equals("") && !god.equals("")) {
      queryString= queryString.concat(
          "where cskl='"+cskl+"' and god='"+god+"'");
    }
    else if (!cskl.equals("") && god.equals("")) {
      queryString= queryString.concat("where cskl='"+cskl+"'");
    }
    else if (cskl.equals("") && !god.equals("")) {
      queryString= queryString.concat(" where god='"+god+"'");
    }
    if (cart!=0) {
      queryString= queryString.concat(
          " and stanje.cart="+cart+" ");
    }

    ispisQuery();
    return queryString;
  }

  public String getQuery4rRS(String cskl,String god, int cart){
    queryString  = "select artikli.vrart as avrart, stanje.*, sklad.vrzal as svrzal " +
                   "from artikli,stanje,sklad where artikli.cart=stanje.cart and "+
                   "sklad.cskl=stanje.cskl and stanje.cskl='"+cskl+
                   "' and stanje.god='"+god+"' and cart = "+cart;
    ispisQuery();
    return queryString;
  }

  public String getQuery4rRSselectStdokuStanje(String cskl,String god){

    queryString = "select doku.*,sklad.vrzal, stanje.*, stdoku.*, stdoku.kol as koldoc, "+
         "stanje.kol as kolstanj, stanje.mc as mcstanj, stanje.vc as vcstanj, "+
          "stanje.nc as ncstanj, stanje.zc as zcstanj, stdoku.nc as ncdoc, "+
          "stdoku.vc as vcdoc, stdoku.mc as mcdoc "+
          "from sklad,doku, stdoku ,stanje where " +
          "doku.cskl=stdoku.cskl and doku.vrdok=stdoku.vrdok and "+
          "doku.god=stdoku.god and doku.brdok=stdoku.brdok and stdoku.cskl=stanje.cskl "+
          " and stdoku.cart=stanje.cart and sklad.cskl= stanje.cskl ";

    if (!cskl.equals("")) {
      queryString= queryString.concat(" and doku.cskl='"+cskl+"' ");
    }
    if (!god.equals("")) {
      queryString= queryString.concat(" and doku.god='"+god+"' ");
    }
    ispisQuery();
    return queryString;
  }

  public String getQuery4rRSselectStdokiStanje(String cskl,String god){
    queryString = "select stanje.*, stdoki.*, stdoki.kol as koldoc, "+
          "stanje.kol as kolstanj, stanje.mc as mcstanj, stanje.vc as vcstanj, "+
          "stanje.nc as ncstanj, stanje.zc as zcstanj, stdoki.nc as ncdoc, "+
          "stdoki.vc as vcdoc, stdoki.mc as mcdoc "+
          "from doki, stdoki ,stanje where " +
          "doki.cskl=stdoki.cskl and doki.vrdok=stdoki.vrdok and "+
          "doki.god=stdoki.god and doki.brdok=stdoki.brdok and stdoki.cskl=stanje.cskl "+
          " and stdoki.cart=stanje.cart ";
    if (!cskl.equals("")) {
      queryString= queryString.concat(" and doki.cskl='"+cskl+"' ");
    }
    if (!god.equals("")) {
      queryString= queryString.concat(" and doki.god='"+god+"' ");
    }
    ispisQuery();
    return queryString;
  }
  public String getQuery4ZaglavljeSet(String ime_table,String cskl,String god,String vrdok, ArrayList brdoks) {
    VarStr var= new VarStr("(");
    for (int i = 0;i<brdoks.size();i++){
      var = var.append((Integer) brdoks.get(i)).append(",");
    }
      var.chopRight(1).append(")");

    queryString = "select * from "+ime_table+" where " +
          ime_table+".cskl='"+cskl+"' and "+ime_table+".vrdok='"+vrdok+"' and "+
          ime_table+".god='"+god+"' and "+ime_table+".brdok in "+var;
    ispisQuery();
    return queryString;
  }

  public String getQuery4ZaglavljeSet(String ime_table,String cskl,String god,String vrdok, int brdok) {
    queryString = "select * from "+ime_table+" where " +
          ime_table+".cskl='"+cskl+"' and "+ime_table+".vrdok='"+vrdok+"' and "+
          ime_table+".god='"+god+"' and "+ime_table+".brdok="+brdok;
    ispisQuery();
    return queryString;
  }

  public String getQuery4RabatiSet (String cskl,String god,String vrdok, int brdok,short rbr) {
    queryString = "select * from vtrabat where " +
          "cskl='"+cskl+"' and vrdok='"+vrdok+"' and "+
          "god='"+god+"' and brdok="+brdok+" and rbr="+rbr;
    ispisQuery();
    return queryString;
  }

  public String getQuery4ZavtrSet (String cskl,String god,String vrdok, int brdok,short rbr) {
    queryString = "select * from vtzavtr where " +
          "cskl='"+cskl+"' and vrdok='"+vrdok+"' and "+
          "god='"+god+"' and brdok="+brdok+" and rbr="+rbr;
    ispisQuery();
    return queryString;
  }

  public String getQuery4AllNorArt() {
    queryString = "select * from artikli where cart in (select distinct(cartnor) from norme)";
    ispisQuery();
    return queryString;
  }

  public String getQuery4Sastojak(int cartnor) {
    queryString = "select * from norme where cartnor = "+cartnor;
    ispisQuery();
    return queryString;
  }

  public String getQuery4raPrenosVT (String imeTable,String keysrc,String value) {
    queryString = "select * from "+imeTable+" where "+keysrc+"='"+value+"'";
    ispisQuery();
    return queryString;
  }
}
