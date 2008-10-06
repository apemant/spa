/****license*****************************************************************
**   file: allStanje.java
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

import hr.restart.baza.Cjenik;
import hr.restart.baza.Condition;
import hr.restart.baza.Sklad;
import hr.restart.baza.Stanje;
import hr.restart.baza.kup_art;
import hr.restart.baza.raDataSet;
import hr.restart.util.LinkClass;
import hr.restart.util.SanityException;
import hr.restart.zapod.OrgStr;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


public class allStanje {

  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  LinkClass lc = LinkClass.getLinkClass();
  String queryString;
  private static allStanje als;
  private QueryDataSet trenSTANJE = new QueryDataSet();
  private QueryDataSet tmpCijenik = new raDataSet();
  //private QueryDataSet tmpCijenikAll = new QueryDataSet();
  //private QueryDataSet tmpKupArt = new QueryDataSet();
  private QueryDataSet tmpKupArtAll = new QueryDataSet();
  private int kupcpar = -1025;
  java.math.BigDecimal Nula = new java.math.BigDecimal(0);
  hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
  /// optimizacija
  String OLDGodina="";
  String OLDCskl = "";
  int OLDcart =-1;
  int cjcpar = -1025;

  public allStanje() {
    trenSTANJE = this.gettrenSTANJE();
    trenSTANJE.setResolver(dm.getQresolver());
  }

  public static allStanje getallStanje(){
    if (als == null) {
      als = new allStanje();
    }
    return als;
  }

  public QueryDataSet gettrenSTANJE() {
    return trenSTANJE;
  }
  /*public QueryDataSet gettmpCijenik() {
    return tmpCijenik;
  }*/
  /*public QueryDataSet gettmpKupArt() {
    return tmpKupArt;
  }*/
  public QueryDataSet gettrenSTANJE(String godina,String skladiste, int artikl) {
    findSTANJE(godina,skladiste,artikl);
    return trenSTANJE;
  }
  public boolean findStanjeFor(DataSet stavka, boolean oj) {
  	if (stavka == null || stavka.getRowCount() == 0) return false;
  	
  	trenSTANJE = Stanje.getDataModule().getTempSet(
  	    Condition.equal("CSKL", stavka.getString(oj ? "CSKLART" : "CSKL")).
  	    and(Condition.whereAllEqual(new String[] {"GOD", "CART"}, stavka)));
  	trenSTANJE.open();
  	return trenSTANJE.rowCount() > 0;
  }
  public void findStanjeUnconditional(String godina,String skladiste, int artikl){
    if (godina == null) {
      System.out.println("godina == null");
      return;
    } else if (skladiste == null) {
      System.out.println("skladiste == null");
      return;
    }
//hr.restart.util.TimeTrack TT = new hr.restart.util.TimeTrack(false);
//TT.Start("findStanjeUnconditional");
queryString = "select * from stanje where god = '"+
               godina +"'"+"and cskl='" +skladiste+"'"+ "and cart=" + artikl ;//new Integer(artikl).toString() ;
System.out.println(queryString);

trenSTANJE = hr.restart.util.Util.getNewQueryDataSet(queryString,true);


//TT.Stop();
  }

  /**
   * Metoda koja pronalazi prema zadanim kriterijima slog u cijeniku
   *
   * @param godina
   * @param skladiste
   * @param artikl
   * @param partner
   */
/*  public void findCijenik(String skladiste, int artikl,int partner){
      tmpCijenik.close();
      tmpCijenik.closeStatement();
      queryString = "select * from cjenik where cpar = "+new Integer(partner).toString()+
                    " and cskl='" +skladiste+"'"+ "and cart=" + new Integer(artikl).toString() ;
      tmpCijenik.setQuery(new QueryDescriptor(dm.getDatabase1(),queryString, null, true, Load.UNCACHED));
      tmpCijenik.executeQuery();
  } */
  
  private void findCjenikPart(int partner) {
    if (partner != cjcpar) {
      cjcpar = partner;
      Cjenik.getDataModule().setFilter(tmpCijenik, Condition.equal("CPAR", cjcpar));
    }
    tmpCijenik.open();
  }

  /*public QueryDataSet getCijenik4All(String skladiste,int partner,boolean find){
    if (find) {
      tmpCijenikAll.close();
      tmpCijenikAll.closeStatement();
      queryString = "select * from cjenik where cpar = "+new Integer(partner).toString()+
                    " and cskl='" +skladiste+"'";
      tmpCijenikAll.setQuery(new QueryDescriptor(dm.getDatabase1(),queryString, null, true, Load.UNCACHED));
      tmpCijenikAll.executeQuery();
   }
   return tmpCijenikAll;
  }*/
  
  /**
   * Trazi slog u cjeniku za artikl nekog partnera, na dokumentu vrste vrdok i
   * skladista ili organizacijeske jedinice csklcorg (ovisno o vrsti dokumenta)
   */
  public QueryDataSet getCijenik(String vrdok, String csklcorg, int cpar, int cart) {
    findCjenikPart(cpar);
    
    // ako je csklcorg ili vrdok null, pogledaj cjenik za knjigovodstvo
    if (csklcorg == null || vrdok == null) {
      if (ld.raLocate(tmpCijenik, new String[] {"CORG", "CART"},
          new String[] {OrgStr.getKNJCORG(false), Integer.toString(cart)}))
        return tmpCijenik;
      return null;
    }
    
    // ako je dokument skladišni, csklcorg je šifra skladišta
    if (TypeDoc.getTypeDoc().isCsklSklad(vrdok)) {
      if (ld.raLocate(tmpCijenik, new String[] {"CSKL", "CART"}, 
          new String[] {csklcorg, Integer.toString(cart)}))
        return tmpCijenik;
      
      // ako nema cjenika za to skladište, uzmi org.jed tog skladišta i nastavi dalje
      if (!ld.raLocate(dm.getSklad(), "CSKL", csklcorg)) return null;
      csklcorg = dm.getSklad().getString("CORG");
    }
    
    // u suprotnom, csklcorg je org.jed, traži cjenik prema gore do knjigovodstva
    while (csklcorg != null && csklcorg.length() > 0) {
      if (ld.raLocate(tmpCijenik, new String[] {"CORG", "CART"}, 
          new String[] {csklcorg, Integer.toString(cart)}))
        return tmpCijenik;
      
      if (ld.raLocate(dm.getOrgstruktura(), "CORG", csklcorg) &&
          !dm.getOrgstruktura().getString("PRIPADNOST").equals(csklcorg))
        csklcorg = dm.getOrgstruktura().getString("PRIPADNOST");
      else csklcorg = null; 
    }
    return null;
  }

  /**
   * Vra\u0107a da li cijenik za postoje\u0107e uvijete postoji
   * Prije toga radi ponovni upit na bazu
   * @param godina
   * @param skladiste
   * @param artikl
   * @param partner
   * @return
   */
/*
  public boolean isCijenikExist(String skladiste, int artikl,int partner){
    findCijenik(skladiste,artikl,partner);
    return !tmpCijenik.isEmpty();
  } */
  /**
   * Vra\u0107a VC iz prije na\u0111enog cijenika
   * @return
   */
  public java.math.BigDecimal getVCfromCijenik(){
    return tmpCijenik.getBigDecimal("VC");
  }
  /**
   * Pronalazi cijenik i vra\u0107a VC
   * @param skladiste
   * @param artikl
   * @param partner
   * @return
   */
  public java.math.BigDecimal getVCfromCijenik(String skladiste, int artikl,int partner){
    //findCijenik(skladiste,artikl,partner);
    findCjenikPart(partner);
    ld.raLocate(tmpCijenik, new String[] {"CSKL", "CART"}, 
        new String[] {skladiste, Integer.toString(artikl)});
    return getVCfromCijenik();
  }
  /**
   * Vra\u0107a MC iz prije na\u0111enog cijenika
   * @return
   */
  public java.math.BigDecimal getMCfromCijenik(){
    return tmpCijenik.getBigDecimal("MC");
  }
  /**
   * Pronalazi cijenik i vra\u0107a VC
   * @param godina
   * @param skladiste
   * @param artikl
   * @param partner
   * @return
   */
  public java.math.BigDecimal getMCfromCijenik(String skladiste, int artikl,int partner){
    //findCijenik(skladiste,artikl,partner);
    findCjenikPart(partner);
    ld.raLocate(tmpCijenik, new String[] {"CSKL", "CART"}, 
        new String[] {skladiste, Integer.toString(artikl)});
    return getMCfromCijenik();
  }
/**
 * Pronalazi slog kup-art za odre\u0111enog partnera i artikl
 * @param artikl
 * @param partner
 */

  /*public void findKupArt(int artikl,int partner){
      tmpKupArt.close();
      tmpKupArt.closeStatement();
      queryString = "select * from kup_art where cpar = "+new Integer(partner).toString()+
                    " and cart=" + new Integer(artikl).toString() ;
      tmpKupArt.setQuery(new QueryDescriptor(dm.getDatabase1(),queryString, null, true, Load.UNCACHED));
      tmpKupArt.executeQuery();
  }*/

  public QueryDataSet getKupArtAll(int partner,boolean find){
    if (partner != kupcpar) {
      kupcpar = partner;
      kup_art.getDataModule().setFilter(tmpKupArtAll, 
          Condition.equal("CPAR", kupcpar));
    }
    tmpKupArtAll.open();
   return tmpKupArtAll;
  }
  /**
   * Vra\u0107a da li Kup_art egzistira za odre\u0111enog partnera i artikl
   * Prije toga radi ponovni upit na bazu
   * @return
   */
  /*public boolean isKupArtExist(int artikl,int partner){
    findKupArt(artikl,partner);
    return !tmpKupArt.isEmpty();
  }
  public boolean isKupArtExist(){
    return !tmpKupArt.isEmpty();
  }*/
  /**
   *
   * Pronalazi slog KupArt-a i vra\u0107a iznos PRAB
   * @param artikl
   * @param partner
   * @return
   */
  /*public java.math.BigDecimal getPRabatfromKupArt(int artikl,int partner){
    findKupArt(artikl,partner);
    return getPRabatfromKupArt();
  }

  public java.math.BigDecimal getPRabatfromKupArt(){
    return tmpKupArt.getBigDecimal("PRAB");
  }*/

////////////////////////////

  void findSTANJE(String godina,String skladiste, int artikl){
    if (!(godina.equals(this.OLDGodina) && skladiste.equals(this.OLDCskl)&& artikl==this.OLDcart)) {
      OLDGodina= godina;
      OLDCskl = skladiste;
      OLDcart = artikl;
      findStanjeUnconditional(godina,skladiste,artikl);
    }
  }

  public boolean findStanje(String godina,String skladiste, int artikl){
    findSTANJE(godina,skladiste,artikl);
    return !trenSTANJE.isEmpty();
  }

  public java.math.BigDecimal findNC(String godina,String skladiste, int artikl){
    if (findStanje(godina,skladiste, artikl))
      return trenSTANJE.getBigDecimal("NC");
    else
      return Nula;
  }

  public java.math.BigDecimal findVC(String godina,String skladiste, int artikl){
    if (findStanje(godina,skladiste, artikl))
      return trenSTANJE.getBigDecimal("VC");
    else
      return Nula;
  }

  public String VrstaZaliha(){
    dm.getSklad().open();
    if (!ld.raLocate(dm.getSklad(),"CSKL",trenSTANJE.getString("CSKL"))){
      System.err.println("ERRRRRRORRR nema skladišta class allStanje linija 246");
      throw new SanityException("Nepoznata vrsta zalihe skladišta!");
    }
//    dm.getSklad().interactiveLocate(trenSTANJE.getString("CSKL"),
//                  "CSKL",com.borland.dx.dataset.Locate.FIRST, false);
    return dm.getSklad().getString("VRZAL");
  }
  public String VrstaZaliha(String cskl){
    dm.getSklad().open();
    if (!ld.raLocate(dm.getSklad(),"CSKL",cskl)){
      System.err.println("ERRRRRRORRR nema skladišta class allStanje linija 262");
      throw new SanityException("Nepoznata vrsta zalihe skladišta!");
    }
    return dm.getSklad().getString("VRZAL");
  }

  public static String VrstaZalihaA(String cskl){
  	DataSet ds = Sklad.getDataModule().getTempSet(Condition.equal("CSKL", cskl));
  	ds.open();
  	if (ds.rowCount() == 0)
  		throw new SanityException("Nepoznato vrsta zalihe skladišta!");
    return ds.getString("VRZAL");
  }

  public java.math.BigDecimal findZC(String godina,String skladiste, int artikl){
    if (findStanje(godina,skladiste, artikl))
      return trenSTANJE.getBigDecimal("ZC");
    else
      return Nula;
  }
  public java.math.BigDecimal findZC(){
      return trenSTANJE.getBigDecimal("ZC");
  }

  public java.math.BigDecimal findMC(String godina,String skladiste, int artikl){
    if (findStanje(godina,skladiste, artikl))
      return trenSTANJE.getBigDecimal("MC");
    else
      return Nula;
  }

  public java.math.BigDecimal findKOL(String godina,String skladiste, int artikl){
    if (findStanje(godina,skladiste, artikl)){
      return trenSTANJE.getBigDecimal("KOL");
      }
    else
      return Nula;
  }
  public java.math.BigDecimal findKOLSKLAD(String godina,String skladiste, int artikl){
    if (findStanje(godina,skladiste, artikl)){
      return trenSTANJE.getBigDecimal("KOLSKLAD");
      }
    else
      return Nula;
  }
  public java.math.BigDecimal findVRI(String godina,String skladiste, int artikl){
    if (findStanje(godina,skladiste, artikl)){
      return trenSTANJE.getBigDecimal("VRI");
      }
    else
      return Nula;
  }
  public void initSTANJE(String godina, String skladiste, int artikl){
    if (trenSTANJE.isOpen())
      trenSTANJE.close();
    trenSTANJE.open();
    trenSTANJE.insertRow(true);
    trenSTANJE.setString("GOD",godina);
    trenSTANJE.setString("CSKL",skladiste);
    trenSTANJE.setInt("CART",artikl);
  }
}