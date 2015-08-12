/****license*****************************************************************
**   file: sgQuerys.java
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

import hr.restart.baza.Artikli;
import hr.restart.baza.Condition;
import hr.restart.baza.Pjpar;
import hr.restart.baza.dM;
import hr.restart.sk.PartnerCache;
import hr.restart.util.Aus;
import hr.restart.util.HashDataSet;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.zapod.OrgStr;

import java.math.BigDecimal;
import java.util.Locale;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;

public class sgQuerys {
  private static sgQuerys sgQ;
//  private QueryDataSet dohvat;
//  private QueryDataSet qdsInventuraVisMan;
//  private QueryDataSet qdsPopisnaLista;
//  private QueryDataSet qdsInventurnaLista;
//  private QueryDataSet qdsInventura;
//*-----------------------------------------*
//*  private QueryDataSet qdsVrstaTroska;   *
//*  private QueryDataSet qdsMjestoTroska;  *
//*-----------------------------------------*
//  private QueryDataSet qdsDohvatSkladista;
//  private QueryDataSet qdsDohvatPartnera;
//  private QueryDataSet qdsDohvatKupaca;
//  private QueryDataSet qdsDohvatArtikala;
//  private QueryDataSet qdsSkladiste;
//  private QueryDataSet qdsDokuStdokuPocetnaStanja;
//  private QueryDataSet qdsStanjePocetnaStanja;
//  private QueryDataSet qdsInventuraKnjizenjeVisMan;
//  private QueryDataSet qdsSkupArt;
  private dM dm;
//  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  private Util ut;
  private Valid vl;
  
  /**
   * <b>Konstruktor</b>
   */
  
  protected sgQuerys() {
    dm = dM.getDataModule();
    ut = Util.getUtil();
    vl = Valid.getValid();
  }
  
  /**
   * Statièki geter za ovu klasu
   *
   * @return <b>sgQuerys</b>
   */

  public static sgQuerys getSgQuerys() {
    if (sgQ==null) {
      sgQ=new sgQuerys();
    }
    return sgQ;
  }

  /* ------------------------------ DATA SETOVI ------------------------------ */

  /**
   * <a name="getQueryObradaDvijeGodinePredselekcija"></a>
   * Geter koji vraæa QueryDataSet iz upita <b>select corg, naziv from orgstruktura where corg=pripadnost</b><br>
   * Pozivaa se iz klase: <b>frmDvijeGodine</b>
   * @return <b>dohvat</b>
   */

//  public QueryDataSet getQueryObradaDvijeGodinePredselekcija(){
//    String queryString = "select * from orgstruktura where corg=pripadnost";
//    QueryDataSet dohvat = ut.getNewQueryDataSet(queryString, false);
//    dohvat.setColumns(dm.getOrgstruktura().cloneColumns());
//    dohvat.open();
//    return dohvat;
//  }

//  /**
//   * Geter koji vraæa QueryDataSet iz upita koji je malo veæi, pa ga sad ne bih htio izdvajati, ali radi svoj posa.<br>
//   * Poziva se iz klasa <b> frmPregledManjak, frmPregledVisak</b><br>
//   *
//   * @param cskl Skladište
//   * @param god Godina
//   * @param cart Artikl
//   * @return <b>qdsInventuraVisMan</b>
//   *
//   */
//  public QueryDataSet getQueryInventuraVisMan(String cskl,String god,String cart) {
//    String queryString = "select stdoku.cskl, stdoku.cart, stdoku.cart1, stdoku.bc, stdoku.zc, stdoku.nazart, stdoku.jm, " +
//                         "sklad.datinv, sklad.vrzal, stanje.nc, stanje.vc, stanje.mc from stdoku, sklad, stanje " +
//                         "where stdoku.cskl='" + cskl + "' and " +
//                         "stdoku.god='" + god + "' and " +
//                         "stdoku.cart='" + cart + "' and stdoku.cskl = sklad.cskl and stdoku.cskl = stanje.cskl and " +
//                         "stdoku.cart = stanje.cart";
//    if(qdsInventuraVisMan==null) {
//      qdsInventuraVisMan = new QueryDataSet();
//      qdsInventuraVisMan.setColumns(new Column[] {
//      (Column) dm.getStdoku().getColumn("CSKL").clone(),
//      (Column) dm.getStdoku().getColumn("CART").clone(),
//      (Column) dm.getStdoku().getColumn("CART1").clone(),
//      (Column) dm.getStdoku().getColumn("BC").clone(),
//      (Column) dm.getStdoku().getColumn("ZC").clone(),
//      (Column) dm.getStdoku().getColumn("NAZART").clone(),
//      (Column) dm.getStdoku().getColumn("JM").clone(),
//      (Column) dm.getSklad().getColumn("DATINV").clone(),
//      (Column) dm.getSklad().getColumn("VRZAL").clone(),
//      (Column) dm.getStanje().getColumn("NC").clone(),
//      (Column) dm.getStanje().getColumn("VC").clone(),
//      (Column) dm.getStanje().getColumn("MC").clone()
//      });
//    } else {
//      qdsInventuraVisMan.close();
//    }
//    qdsInventuraVisMan.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),queryString, null, true, Load.ALL));
//    qdsInventuraVisMan.open();
//    return qdsInventuraVisMan;
//  }
  /**
   * Geter koji vraæa QueryDataSet iz upita koji je malo veæi, pa ga sad ne bih htio izdvajati, ali radi svoj posa.<br>
   * poziva se iz klase <b>raPopisnaLista</b><br>
   * @param cskl Skladište
   * @param stanjeNula Podstring stanja nula (dio upita u ovisnosti o check box-u)
   * @param sort Sortiranje
   * @return <b>qdsPopisnaLista</b>
   */

  public String getInventuraPopisnaListaQuery(String cskl, String stanjeNula, String sort,String god){
    /** @todo ovo osmislit na neki bolji nacin, mozda */
    System.out.println("SORT '" + sort + "'"); //XDEBUG delete when no more needed
    String _sort = "artikli."+sort; // _was_ [QUOTE] String _sort = ""; [/QUOTE]
    if (sort.equals("NAZART")){ // _was_ [QUOTE] if (!sort.equals("")){ [/QUOTE]
      _sort = _sort+" "+vl.getCollateSQL(); // _was_ [QUOTE] _sort = sort+" "+vl.getCollateSQL();
    }
    String queryString = "select stanje.cskl,stanje.cart,artikli.cart1, " +
                         "artikli.bc, artikli.nazart, artikli.jm, artikli.cgrart, sklad.nazskl, artikli.sortkol from artikli, " +
                         "stanje, sklad where artikli.cart=stanje.cart and stanje.cskl='" +
                         cskl + "' and stanje.god= '"+/*sklad.godina stara varijanta*/god+"' and stanje.cskl=sklad.cskl " + stanjeNula +
                         " order by " + _sort; /// PROXIMITY WARNING
    System.out.println("Query String : " + queryString);
//    QueryDataSet qdsPopisnaLista = hr.restart.util.Util.getNewQueryDataSet(queryString);
    return queryString;
  }

  /**
   * Geter koji vraæa QueryDataSet iz upita koji je malo veæi, pa ga sad ne bih htio izdvajati, ali radi svoj posa.<br>
   * poziva se iz klase <b>raInventurnaLista</b><br>
   * @param cskl Skladište
   * @param sort Kriterij za sortiranje
   * @return qdsInventurnaLista
   */

  public String getInventuraInventurnaLista(String cskl, String sort){
    String queryString ="select inventura.cskl, inventura.cart, inventura.cart1, " +
                        "inventura.bc, inventura.nazart, inventura.jm, inventura.kolknj, " +
                        "inventura.kolinv, inventura.zc, inventura.vriknj, inventura.vriinv, " +
                        "inventura.kolmanj, inventura.kolvis, inventura.vrimanj, inventura.vrivis, " +
                        "sklad.datinv, sklad.nazskl from inventura, sklad where inventura.cskl='" + cskl + "' " +
                        "and sklad.cskl='" + cskl + "' order by " + sort;

                        /** @todo sranje sa instanciranjem :(( */

//    System.out.println("QS : " + queryString);
    return queryString;
  }
  
  public String getInventuraInventurnaListabezNula(String cskl, String sort){
      String queryString ="select inventura.cskl, inventura.cart, inventura.cart1, " +
                          "inventura.bc, inventura.nazart, inventura.jm, inventura.kolknj, " +
                          "inventura.kolinv, inventura.zc, inventura.vriknj, inventura.vriinv, " +
                          "inventura.kolmanj, inventura.kolvis, inventura.vrimanj, inventura.vrivis, " +
                          "sklad.datinv, sklad.nazskl from inventura, sklad where inventura.cskl='" + cskl + "' " +
                          "and not (inventura.kolinv =0 and inventura.kolknj =0) and sklad.cskl='" + cskl + "' order by " + sort;

                          /** @todo sranje sa instanciranjem :(( */

System.out.println("QS : " + queryString);
      return queryString;
    }
  
  
  /**
   * Geter koji vraæa QueryDataSet iz upita <b>select cskl, nazskl, datinv from sklad where statinv='D'</b><br>
   * Koristi se za predselekciju u klasama
   * <b>raPan4UnosInv</b>, <b>raKnjizenjeInventure</b> i <b>raInventurnaLista</b>
   * @param corg - knjigovodstvo
   * @return qdsInventura
   *
   */

  public QueryDataSet getSkladistaUInventuri(String corg) {
    String queryString = "select * from sklad where statinv='D' and knjig ='" + corg + "'";
    //    System.out.println("qs : " + queryString);
    QueryDataSet qdsInventura = ut.getNewQueryDataSet(queryString, false);
    qdsInventura.setColumns(dm.getSklad().cloneColumns());
    qdsInventura.open();
    return qdsInventura;
  }

  /**
   * <a name="1"></a>
   * Geter koji vraca string difoltnog skladišta iz tablice <b>parametri</b>
   * @return CSKL
   */

//  public String getDefSklad() {
//    String queryString = "select max(vrijednost) as CSKL from parametri where param='defCskl'";
//    vl.execSQL(queryString);
//    vl.RezSet.open();
//    vl.RezSet.first();
//    String CSKL= vl.RezSet.getString("CSKL");
//    return CSKL;
//  }

  //  select vrijednost from parametri where param='defCskl'


  /**
   * Geter koji vraæa QueryDataSet iz upita <b>select cvrtr, naziv from vrtros</b><br>
   * Koristi se u klasi <b>upPregledTroskova</b> za dohvat u jraNavField
   * @return <b>qdsVrstaTroska</b>
   */

  public QueryDataSet getVrstaNazivTroska() {
    String queryString = "select cvrtr, naziv from vrtros";
    QueryDataSet qdsVrstaTroska = new QueryDataSet();
    qdsVrstaTroska.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),queryString, null, true, Load.ALL));
    qdsVrstaTroska.setColumns(new Column[] {
      (Column) dm.getVrtros().getColumn("CVRTR").clone(),
      (Column) dm.getVrtros().getColumn("NAZIV").clone()
      });
    qdsVrstaTroska.open();
    return qdsVrstaTroska;
  }
  /**
   * Geter koji vraæa QueryDataSet iz upita <b>select corg, naziv from orgstruktura</b><br>
   * Koristi se u klasi <b>upPregledTroskova</b> za dohvat u jraNavField
   * @return <b>qdsMjestoTroska</b>
   */

  public QueryDataSet getMjestoTroska() {
    String queryString = "select corg, naziv from orgstruktura where pripadnost='" + hr.restart.zapod.OrgStr.getKNJCORG() + "'";
    QueryDataSet qdsMjestoTroska = new QueryDataSet();
    qdsMjestoTroska.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),queryString, null, true, Load.ALL));
    qdsMjestoTroska.setColumns(new Column[] {
      (Column) dm.getOrgstruktura().getColumn("CORG").clone(),
      (Column) dm.getOrgstruktura().getColumn("NAZIV").clone()
      });
    qdsMjestoTroska.open();
    return qdsMjestoTroska;
  }
  /**
   * * Geter koji vraæa QueryDataSet iz upita <b>select cskl, nazskl from sklad</b><br>
   * Koristi se u klasama <b> ispStatPar</b> i <b>ispStatArt</b> za dohvat u jraNavFild<br>
   * @param whereWhat dio stringa koji glasi: <b>where cskl='CSKL'</b> dobiven iz <b>getDefSklad</b> metode.
   * @return <b>qdsDohvatSkladista</b>
   * @see <b><a href=sgQuerys.html#1>getDefSklad</a></b>
   */

  /*public QueryDataSet getDohvatSkladista(String whereWhat) {
    String queryString = "select * from sklad" + whereWhat;
    QueryDataSet qdsDohvatSkladista = ut.getNewQueryDataSet(queryString, false);
//    qdsDohvatSkladista.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),queryString, null, true, Load.ALL));
    qdsDohvatSkladista.setColumns(dm.getSklad().cloneColumns());
    qdsDohvatSkladista.open();
    return qdsDohvatSkladista;
  }*/
  /**
   * Geter koji vraæa QueryDataSet iz upita <b>select max(cpar) as cpar, max(nazpar) as nazpar from partneri group by cpar</b><br>
   * Koristi se u klasama <b> ispStatPar</b> i <b>ispStatArt</b> za dohvat u jraNavFild<br>
   * @return <b>qdsDohvatPartnera</b>
   */

//  public QueryDataSet getDohvatPartnera() {
//    String queryString = "select max(cpar) as cpar, max(nazpar) as nazpar from partneri group by cpar";
//    qdsDohvatPartnera = new QueryDataSet();
//    qdsDohvatPartnera.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),queryString, null, true, Load.ALL));
//    qdsDohvatPartnera.open();
//    return qdsDohvatPartnera;
//  }

  /**
   * Geter koji vraæa QueryDataSet iz upita <b>select max(cpar) as ckup, max(nazpar) as nazkup from partneri group by cpar</b><br>
   * Koristi se u klasama <b> ispStatPar</b> i <b>ispStatArt</b> za dohvat u jraNavFild<br>
   * @return <b>qdsDohvatKupca</b>
   */

//  public QueryDataSet getDohvatKupaca() {
//    String queryString = "select max(cpar) as ckup, max(nazpar) as nazkup from partneri group by cpar";
//    qdsDohvatKupaca = new QueryDataSet();
//    qdsDohvatKupaca.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),queryString, null, true, Load.ALL));
//    qdsDohvatKupaca.open();
//    return qdsDohvatKupaca;
//  }
  /**
   * Geter koji vraæa QueryDataSet iz upita <b>select max(cart) as cart, max(nazart) as nazart from artikli group by cart</b><br>
   * Ne koristi se nigdje, ali bi moga poslužit....
   * @return <b>qdsDohvatArtikla</b>
   */

//  public QueryDataSet getDohvatArtikla() {
//    String queryString = "select max(cart) as cart, max(naziv) as nazart from artikli group by cart";
//    qdsDohvatArtikala = new QueryDataSet();
//    qdsDohvatArtikala.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),queryString, null, true, Load.ALL));
//    qdsDohvatArtikala.open();
//    return qdsDohvatArtikala;
//  }
  /**
   * Geter koji vraæa QueryDataSet iz upita <b>select * from sklad where knjig='knj' and lokk='N' and aktiv='D'</b><br>
   * Koristi se u klasama <b> frmDvijeGodine</b> i <b>frmSretnaNovaGodina</b> za dohvat skladišta prema šifri organizacije<br>
   * @param knj Šifra organizacije
   * @return <b>qdsSkladište
   */

  public QueryDataSet getObradeRadDvijeGodineDohvatSkladista(String knj){
    String queryString = "select * from sklad where knjig='" + knj + "' and lokk='N' and aktiv='D'";
    QueryDataSet qdsSkladiste = ut.getNewQueryDataSet(queryString, false);
    qdsSkladiste.setColumns(dm.getSklad().cloneColumns());
    qdsSkladiste.open();
    qdsSkladiste.first();
    return qdsSkladiste;
  }

  /* @todo public QueryDataSet getObradeRadDvijeGodineSetDokuStdokuPocetnoStanje(String skladiste, String godina, boolean bezNula) */

  public QueryDataSet getObradeRadDvijeGodineSetDokuStdokuPocetnoStanje(String skladiste, String godina, boolean saNulama){
    String queryString = "select stanje.*, artikli.cart1, artikli.bc, artikli.nazart, " +
                                "artikli.jm, sklad.vrzal,  sklad.statrad from stanje, artikli, sklad where stanje.cskl='" + skladiste +
                                "' and stanje.god='" + godina + "'and stanje.cart=artikli.cart and stanje.cskl=sklad.cskl";
    String bezArtikalaNula = " and stanje.kol != 0";
    String ordering = " order by stanje.cart";
    if (!saNulama)
      queryString = queryString + bezArtikalaNula + ordering;
    else
      queryString = queryString + ordering;

    System.out.println("getObradeRadDvijeGodineSetDokuStdokuPocetnoStanje queryString \n"+ queryString);
    QueryDataSet qdsDokuStdokuPocetnaStanja = ut.getNewQueryDataSet(queryString);
    return qdsDokuStdokuPocetnaStanja;
  }

//  public QueryDataSet getObradeRadDvijeGodineSetStanjePocetnoStanje(String skladiste, String godina){
//    String queryString ="select stanje.*,  sklad.statrad from stanje, sklad  where cskl='" + skladiste +
//                                 "' and sklad.godina='" + godina + "' and stanje.cskl=sklad.cskl and stanje.god=sklad.godina";
//
//    QueryDataSet qdsStanjePocetnaStanja = ut.getNewQueryDataSet(queryString);
//    return qdsStanjePocetnaStanja;
//  }

  /**
   * Geter koji vraæa QueryDataSet iz upita koji je malo veæi, pa ga sad ne bih htio izdvajati, ali radi svoj posa.<br>
   * Koristi se u klasi <b>raKnjizenjeInventure</b> za knjiženje viška ili manjka (ovisno o parametru visman)
   * @param cskl Skladište
   * @param visman Višak ili manjak (kolvis ili kolmanj)
   * @return <b>qdsInventuraKnjizenjeVisMan</b>
   */

  public QueryDataSet getInventuraKnjizenjeSetupVisakManjak(String cskl, String visman){
    String queryString = "select inventura.cskl, inventura.cart, inventura.cart1, inventura.bc, inventura.zc, inventura.nazart, " +
                          "inventura.jm, inventura." + visman + ", sklad.datinv, sklad.vrzal, stanje.nc, stanje.vc, stanje.mc " +
                          "from inventura, sklad, stanje where inventura.cskl='" + cskl + "' and " + visman + ">'0' " +
                          "and inventura.cskl = sklad.cskl and inventura.cskl = stanje.cskl and inventura.cart = stanje.cart " +
                          "and stanje.god=sklad.godina";
    QueryDataSet qdsInventuraKnjizenjeVisMan = ut.getNewQueryDataSet(queryString);
    return qdsInventuraKnjizenjeVisMan;
  }

//  /**
//   * Geter koji vraæa QueryDataSet iz upita <b>select max(cskupart) as cskupart, max(nazskupart) as nazskupart from skupart group by cskupart</b><br>
//   * Ne koristi se nigdje, ali bi moga poslužit, recimo za selekciju skupine artikala (možda??)
//   * @return <b>qdsSkupArt</b>
//   */
//
//  public QueryDataSet getQuerySkupArt() {
//    String queryString = "select max(cskupart) as cskupart, max(nazskupart) as nazskupart from skupart group by cskupart";
//    if (qdsSkupArt==null) {
//      qdsSkupArt = new QueryDataSet();
//      qdsSkupArt.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),queryString, null, true, Load.ALL));
//      qdsSkupArt.setColumns(new Column[] {
//      (Column) dm.getSkupart().getColumn("CSKUPART").clone(),
//      (Column) dm.getSkupart().getColumn("NAZSKUPART").clone()
//      });
//    }
//    qdsSkupArt.open();
//    return qdsSkupArt;
//  }

  /* ------------------------------ BULINI ------------------------------ */
  /**
   * Bulin (Rest-Artovski naziv za boolean :) )<br>
   * String <b>select * from inventura where cskl='cskl' and kolmanj='0' (ili kolvis='0')</b><br>
   * Koristi se u klasi <b>raKnjizenjeInventure</b>
   * @param cskl Skladište
   * @param manvis Višak ili manjak (kolvis ili kolmanj)
   * @return da li je vl.RezSet.rowCount veæi od nule (true or false, jeli? :) )
   */

  public boolean isThereAnything(String cskl, String manvis) {
    String queryString = "select * from inventura where cskl='" + cskl + "' and " + manvis + "'0'";
    vl.execSQL(queryString);
    vl.RezSet.open();
    return (vl.RezSet.rowCount() > 0);
  }

  /* ------------------------------ STRINGOVI ------------------------------ */
  /**
   * Geter koji vraæa String <b>select statinv, datinv, staridatinv from sklad where cskl='cskl'</b><br>
   * za daljnju upotrebu u rezoult set-u u klasama <b>raPripInv</b> i <b>raKnjizenjeInventure</b>
   * @param cskl Skladište
   * @return <b>queryString</b>
   */
  public String getStringUsporedba(String cskl){
    String queryString="select statinv, datinv, staridatinv from sklad where cskl='" + cskl + "'";
    return queryString;
  }
  /**
   * Geter koji vraæa String koji je malo veèi (vidi detalje u source-u ako te baš zanima)<br>
   * za daljnju upotrebu u rezoult set-u u klasi <b>raPripInv</b>
   * @param cskl Skladište
   * @param tekucaGodina Tekuèa (ova) godina
   * @return <b>queryString</b>
   */

  public String getStringInsertToInventura(String cskl, String tekucaGodina){
    String queryString = "insert into inventura (cskl, cart, cart1, bc, kolknj, kolinv, zc, vriknj, vriinv, nazart, jm)" +
                                  " select  stanje.cskl, stanje.cart, artikli.cart1, artikli.bc, stanje.kol, stanje.kol, stanje.zc, stanje.vri, stanje.vri, artikli.nazart, artikli.jm from stanje, artikli where stanje.cskl='"
                                  + cskl + "' and stanje.god='" + tekucaGodina + "' and stanje.cart = artikli.cart";
    return queryString;
  }
  /**
   * Geter koji vraæa String <b>update inventura set kolmanj='0', kolvis='0', vrimanj='0', vrivis='0' where cskl='cskl'</b><br>
   * za daljnju upotrebu u rezoult set-u u klasi <b>raPripInv</b>
   * @param cskl Skladište
   * @return <b>queryString</b>
   */

  public String getStringInsertToInventuraUpdateRest(String cskl){
    String queryString = "update inventura set " +
            "inventura.kolmanj=0.000, " +
            "inventura.kolvis=0.000, " +
            "inventura.vrimanj=0.00, " +
            "inventura.vrivis=0.00 " + 
            "where inventura.cskl = '" + cskl + "'";
    return queryString;
  }
  /**
   * Geter koji vraæa String <b>delete from inventura where cskl='cskl'</b><br>
   * za daljnju upotrebu u rezoult set-u u klasama <b>raPripInv</b> i <b>raKnjizenjeInventure</b>
   * @param cskl Skladište
   * @return <b>queryString</b>
   */

  public String getStringDeleteInventura(String cskl){
    String queryString = "delete from inventura where cskl='" + cskl +"'";
    return queryString;
  }
  /**
   * Geter koji vraæa String <b>update Sklad set statrad='D' where cskl='cskl'</b><br>
   * za daljnju upotrebu u rezoult set-u u klasi <b>frmDvijeGodine</b>
   * @param corg Knjigovodstvo
   * @param god Godina
   * @return <b>queryString</b>
   */

  public String getStringObradeDvijeGodineUpdateKnjigod(String corg, String god){
    String queryString;
    if (god.equals(""))
      queryString = "update knjigod set statrada='D' where corg='" + corg + "' and app='robno'";
    else
      queryString = "update knjigod set statrada='N', god='" + god + "' where corg='" + corg + "' and app='robno'";
    return queryString;
  }

  public String getStringObradeDvijeGodineUpdateSklad(String corg, String god){
    String queryString;
    if (god.equals(""))
      queryString = "update sklad set statrad='D' where knjig='" + corg + "'";
    else
      queryString = "update sklad set statrad='N', godina='" + god + "' where knjig='" + corg + "'";
    return queryString;
  }
//  /**
//   * Geter koji vraèa èak dva stringa koji su oba jako velika (vidi source).<br>
//   * Koriste se u klasi <b>frmPregledVisak</b> za rezSet koji se šalje <br>report provideru u ovisnosti da li se poziva iz Mastera
//   * ili Detaila.
//   * @param isMaster Bulin koji sudjeluje u odabiru da li je za master ili detail
//   * @param fromDate Poèetni datum
//   * @param toDate Krajnji datum
//   * @param cskl Skladište
//   * @param vrdok Vrsta dokumenta (u ovom sluèaju INV)
//   * @param god Godina (zbog kljuèa u tablici)
//   * @param brdok Broj dokumenta
//   * @return <b>queryString</b>
//   */
//
//  public String getStringInventuraVisakFunkcijaIspisa(boolean isMaster, String fromDate, String toDate, String cskl, String vrdok, String god, int brdok){
//    String queryString;
//    if (isMaster){
//      queryString = "SELECT stdoku.cskl, stdoku.vrdok, stdoku.brdok, " +
//             "stdoku.rbr, stdoku.cart, stdoku.cart1, stdoku.bc, stdoku.nazart, stdoku.jm, "+
//             "stdoku.kol, stdoku.zc, stdoku.izad, sklad.nazskl, doku.datdok "+
//             "FROM doku, stdoku, sklad  WHERE stdoku.cskl = sklad.cskl AND "+
//             "doku.datdok >= "+fromDate+" AND "+ "doku.datdok <= "+toDate+" AND "+
//             Util.getUtil().getDoc("doku","stdoku")+
//             " AND stdoku.cskl = '"+cskl+ "' AND stdoku.vrdok = '"+vrdok+"'";
//      return queryString;
//    } else {
//      queryString = "SELECT stdoku.cskl, stdoku.vrdok, stdoku.brdok, " +
//             "stdoku.rbr, stdoku.cart, stdoku.cart1, stdoku.bc, stdoku.nazart, stdoku.jm, "+
//             "stdoku.kol, stdoku.zc, stdoku.izad, sklad.nazskl, doku.datdok  "+
//             "FROM doku, stdoku, sklad  WHERE stdoku.cskl = sklad.cskl AND "+
//             Util.getUtil().getDoc("doku","stdoku")+
//             " AND stdoku.cskl = '"+cskl+
//             "' AND stdoku.vrdok = '"+vrdok+
//             "' AND stdoku.god = '"+god+
//             "' AND stdoku.brdok = "+brdok;
//      return queryString;
//    }
//  }
//  /**
//   * Geter koji vraèa èak dva stringa koji su oba jako velika (vidi source).<br>
//   * Koriste se u klasi <b>frmPregledManjak</b> za rezSet koji se šalje <br>report provideru u ovisnosti da li se poziva iz Mastera
//   * ili Detaila.
//   * @param isMaster Bulin koji sudjeluje u odabiru da li je za master ili detail
//   * @param a Poèetni datum
//   * @param b Krajnji datum
//   * @param cskl Skladište
//   * @param vrdok Vrsta dokumenta (u ovom sluèaju INM)
//   * @param god Godina (zbog kljuèa u tablici)
//   * @param brdok Broj dokumenta
//   * @return <b>queryString</b>
//   */
//
//  public String getStringInventuraManjakFunkcijaIspisa(boolean isMaster, String a, String b, String cskl, String vrdok, String god, int brdok){
//    String queryString;
//    if (isMaster){
//      queryString = "SELECT stdoki.cskl, stdoki.vrdok, stdoki.brdok, " + // stdoki.rbr, stdoki.cart, "+
//             "stdoki.rbr, stdoki.cart, stdoki.cart1, stdoki.bc, stdoki.nazart, stdoki.jm, "+
//             "stdoki.kol, stdoki.zc, stdoki.iraz, sklad.nazskl, doki.datdok "+
//             "FROM doki, stdoki, sklad  WHERE stdoki.cskl = sklad.cskl AND "+
//             "doki.datdok >= "+a+" AND "+ "doki.datdok <= "+b+" AND "+
//             Util.getUtil().getDoc("doki","stdoki")+
//             " AND stdoki.cskl = '"+cskl+ "' AND stdoki.vrdok = '"+vrdok+"'";
//      return queryString;
//    } else {
//      queryString = "SELECT stdoki.cskl, stdoki.vrdok, stdoki.brdok, " + // stdoki.rbr, stdoki.cart, "+
//             "stdoki.rbr, stdoki.cart, stdoki.cart1, stdoki.bc, stdoki.nazart, stdoki.jm, "+
//             "stdoki.kol, stdoki.zc, stdoki.iraz, sklad.nazskl, doki.datdok  "+
//             "FROM doki, stdoki, sklad  WHERE stdoki.cskl = sklad.cskl AND "+
//             Util.getUtil().getDoc("doki","stdoki")+
//             " AND stdoki.cskl = '"+cskl+
//             "' AND stdoki.vrdok = '"+vrdok+
//             "' AND stdoki.god = '"+god+
//             "' AND stdoki.brdok = "+brdok;
//      return queryString;
//    }
//  }
  /**
   * Geter koji vraèa String <b>select * from stanje where cskl = 'cskl' and cart = 'cart'</b><br>
   * za daljnju upotrebu u rezoult set-u u... NEMA :((
   * @param cskl Skladište
   * @param cart Artikl
   * @return <b>queryString</b>
   */

  public String getStringInventuraVisakManjakAkoNePostoji(String cskl, String cart){
    String queryString = "select * from stanje where " + "cskl = '" + cskl + "' and " + "cart = '" + cart + "'";
    return queryString;
  }
  /**
   * Geter za provjeru da li je stavka unikatna.<br>
   * Koristi se u klasama <b>frmPregledVisak</b> i <b>frmPregledManjak</b>
   * @param stdWhat Tabela <b>stdoku</b> ili <b>stdoki</b>
   * @param cskl Skladište
   * @param vrdok Vrsta dokumenta (INV ili INM)
   * @param god Godina za koju se radi inventura
   * @param brdok Broj dokumenta
   * @param art Artikl
   * @return <b>queryString</b>
   */

  public String getStringAreNotUnique(String stdWhat, String cskl, String vrdok, String god, int brdok, String art){
    String queryString =  "select * from " + stdWhat + " where cskl = '" +
                          cskl + "' and vrdok = '" +
                          vrdok + "' and god = '" +
                          god + "' and brdok = " +
                          brdok + " and cart = " +
                          art;
    return queryString;
  }

  public QueryDataSet getIspStatArtDS(String dateP, String dateZ, String sortum, String cskl, String cag, String ckup, String pjkup, String cpart, String artikli, String carting, String corg) {
    String ckupca = " ", pjKupca = " ", dobart=" ", caprDobart="", sklad="", cagenta="";
//    System.out.println("CPAR = " + cpart);
    if (!ckup.equals("")) ckupca = "and doki.cpar='" + ckup + "' ";
    if (!pjkup.equals("")) pjKupca = "and doki.pj='" + pjkup + "' ";
    if (!cpart.equals("")){
      dobart = ", dob_art ";
      ckupca = ckupca + "and dob_art.cpar = '" + cpart + "' and dob_art.cart = stdoki.cart ";
      caprDobart = ", dob_art.cpar as dcp ";
    }
    if (cag.length() > 0) cagenta = "and doki.cagent=" + cag + " ";
    
    if (cskl.equals("")){
      
      QueryDataSet skls;
      
      if (corg.equals("")) skls = dm.getSklad();
      else skls = hr.restart.robno.Util.getUtil().getSkladFromCorg();//ut.getNewQueryDataSet("SELECT * FROM Sklad WHERE corg = '"+corg+"'");
      
      skls.open();
      skls.first();
      
      String sifskl = "";
      
      do {
        sifskl += "'"+skls.getString("CSKL")+"'";
        if (skls.next()) sifskl += ",";
        else break;
      } while (true);

      sklad = " AND DOKI.CSKL in ("+sifskl+")";
    } else {
      sklad = " AND DOKI.CSKL = '"+cskl+"'";
    }
    
    Condition cin = OrgStr.getCorgsCond("CSKL", corg).qualified("doki");
    String inq = cin == Condition.nil ? "1=1" : cin.toString();
    
    /*String inq;
    StorageDataSet corgs = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(corg);
    if (corgs.rowCount() == 0 || corg.equals("")) inq = "1=1";
    else if (corgs.rowCount() == 1) inq = "DOKI.CSKL = '" + corg + "'";
    else inq = "(DOKI.CSKL in " + hr.restart.zapod.OrgStr.getOrgStr().getINQuery(corgs,"DOKI.CSKL")+") ";*/
    hr.restart.baza.Condition oj = hr.restart.baza.Condition.in("DOKI.VRDOK", TypeDoc.araj_docsOJ);
    String exInClude = "AND (("+oj+" AND "+inq+") OR ("+oj.not()+sklad+")) ";

    String queryString = "select "+
    					 "doki.vrdok, "+
                         "stdoki.cskl, "+
                         "stdoki.cart, "+
                         "stdoki.cart1, "+
                         "stdoki.bc, "+
                         "artikli.vrart, "+
                         "stdoki.nazart, "+
                         "stdoki.jm, "+
                         "stdoki.kol, "+
                         "stdoki.iraz, "+
                         "stdoki.iprodbp, "+
                         "CAST ((stdoki.iprodsp - stdoki.iprodbp) AS numeric(12,2)) as por, "+
                         "stdoki.iprodsp, "+
                         "stdoki.inab, stdoki.rinab"+
                         "CAST ((stdoki.iprodbp-stdoki.inab) AS numeric(12,2)) as ruc "+caprDobart+
                         "from artikli, doki, stdoki "+dobart+
                         "where doki.cskl=stdoki.cskl and doki.brdok=stdoki.brdok "+
                         "and doki.god=stdoki.god and doki.vrdok=stdoki.vrdok AND stdoki.cart = artikli.cart "+
//                         " AND doki.vrdok != 'PON' AND doki.vrdok != 'NDO' AND doki.vrdok != 'NKU' AND stdoki.vrdok != 'RNL' AND stdoki.vrdok != 'REV' AND stdoki.vrdok != 'PRV' "+
                         " AND doki.vrdok not in ('PON','TRE','ZAH','NDO','NKU','RNL','REV','PRV','OTR','OTP','INM','INV','IZD','OTP', 'DOS') "+
                         exInClude+
                         cagenta+ckupca+
                         pjKupca+
                         artikli+
                         carting+
                         " and doki.datdok between "+
                         dateP+" "+"and "+dateZ;

    System.out.println("\n----------<SQL>----------\n"+queryString+"\n---------<\\SQL>----------\n");

    QueryDataSet ts = ut.getNewQueryDataSet(queryString);
    if (ts.rowCount() == 0) return null;

    System.out.println("loaded");
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(ts);

    QueryDataSet stds = new QueryDataSet();
    stds.setLocale(Locale.getDefault());
    stds.setColumns(new Column[] {
      (Column) dm.getStdoki().getColumn("CSKL").clone(),
      (Column) dm.getStdoki().getColumn("CART").clone(),
      (Column) dm.getStdoki().getColumn("CART1").clone(),
      (Column) dm.getStdoki().getColumn("BC").clone(),
      (Column) dm.getStdoki().getColumn("NAZART").clone(),
      (Column) dm.getStdoki().getColumn("JM").clone(),
      (Column) dm.getStdoki().getColumn("KOL").clone(),
      (Column) dm.getStdoki().getColumn("IRAZ").clone(),
      (Column) dm.getStdoki().getColumn("INAB").clone(),
      dm.createBigDecimalColumn("RUC","RuC",2),
      dm.createBigDecimalColumn("PostoRUC","% RuC",2),
      dm.createBigDecimalColumn("JRUC","Jed. RuC", 3),
      (Column) dm.getStdoki().getColumn("IPRODBP").clone(),
      dm.createBigDecimalColumn("POR","Porez",2),
      (Column) dm.getStdoki().getColumn("IPRODSP").clone(),
      (Column) dm.getDob_art().getColumn("CPAR").clone()
    });
    stds.open();
    
    ts.setSort(new SortDescriptor(new String[] {"CART"}));
    ts.first();
    
    //Artikli.getDataModule().fixSort();
    
    //hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
/*    HashMap cpar2row = new HashMap(); */
    HashDataSet arts = new HashDataSet(dm.getArtikli(), "CART");
    
    
    int cart = -1027;
    do {
/*      if (cpar2row.containsKey(new Integer(ts.getInt("CART")))) {
        
        cpar2row.put(new Integer(ts.getInt("CART")),
            new Integer(stds.getRow()));
      } else {
        stds.goToRow(((Integer) cpar2row.get(new Integer(ts.getInt("CART")))).intValue());
      } */
      
/*      c2r = {}
 *      if cart in c2r:
 *        ds.gotorow(c2r[cart])
 *      else:
 *        c2r[cart] = ds.getRow()
 *  
  
      
      */
      if (ts.getInt("CART") != cart) {
        stds.insertRow(false);
        stds.setString("CSKL",ts.getString("CSKL"));
        stds.setInt("CART",cart = ts.getInt("CART"));
        stds.setString("CART1",ts.getString("CART11"));
        stds.setString("BC",ts.getString("BC"));
        if (!raVart.isStanje(ts)){
         if (arts.loc(ts)) stds.setString("NAZART",arts.get().getString("NAZART"));
         else stds.setString("NAZART",ts.getString("NAZART"));
        } else {
        stds.setString("NAZART",ts.getString("NAZART"));
        }
        stds.setString("JM",ts.getString("JM"));
        /*if (ts.getString("CSKL").equals(cskl))*/ stds.setBigDecimal("KOL", stds.getBigDecimal("KOL").add(ts.getBigDecimal("KOL")));//stds.setBigDecimal("KOL", ts.getBigDecimal("KOL"));
        
        /*if (ts.getString("VRDOK").equals("PRD") ||
            ts.getString("VRDOK").equals("RAC") ||
            ts.getString("VRDOK").equals("TER") ||
            ts.getString("VRDOK").equals("ODB") ||
            ts.getString("VRDOK").equals("GRN") ||
            ts.getString("VRDOK").equals("KAL") ||
            ts.getString("VRDOK").equals("POS") ||
            ts.getString("VRDOK").equals("ROT") ||
            ts.getString("VRDOK").equals("POD") 
            ) {
//          System.out.println("ts.getString(\"VRDOK\") " + ts.getString("VRDOK")); //XDEBUG delete when no more needed
        stds.setBigDecimal("KOL", stds.getBigDecimal("KOL").add(ts.getBigDecimal("KOL")));
        }*/
        
//        else stds.setBigDecimal("KOL", new java.math.BigDecimal("0.00"));
        stds.setBigDecimal("IRAZ", ts.getBigDecimal("IRAZ"));
        stds.setBigDecimal("IPRODBP", ts.getBigDecimal("IPRODBP"));
        stds.setBigDecimal("POR", ts.getBigDecimal("POR"));
        stds.setBigDecimal("IPRODSP", ts.getBigDecimal("IPRODSP"));
        if (TypeDoc.getTypeDoc().isDocSklad(ts.getString("VRDOK"))) {
          stds.setBigDecimal("INAB", ts.getBigDecimal("INAB"));
          stds.setBigDecimal("RUC", ts.getBigDecimal("RUC"));
        } else if (raIzlazTemplate.isNabDirect()) {
          stds.setBigDecimal("INAB", ts.getBigDecimal("RINAB"));
          stds.setBigDecimal("RUC", ts.getBigDecimal("IPRODBP").subtract(ts.getBigDecimal("RINAB")));
        }
        /*
        stds.setBigDecimal("KOL", stds.getBigDecimal("KOL").add(ts.getBigDecimal("KOL")));
        stds.setBigDecimal("IRAZ", stds.getBigDecimal("IRAZ").add(ts.getBigDecimal("IRAZ")));
        stds.setBigDecimal("IPRODBP", stds.getBigDecimal("IPRODBP").add(ts.getBigDecimal("IPRODBP")));
        stds.setBigDecimal("POR", stds.getBigDecimal("POR").add(new java.math.BigDecimal(ts.getBigDecimal("POR"))));
        stds.setBigDecimal("IPRODSP", stds.getBigDecimal("IPRODSP").add(ts.getBigDecimal("IPRODSP")));
        stds.setBigDecimal("INAB", stds.getBigDecimal("INAB").add(ts.getBigDecimal("INAB")));
        stds.setBigDecimal("RUC", stds.getBigDecimal("RUC").add(new java.math.BigDecimal(ts.getBigDecimal("RUC"))));
        */
        try {
          stds.setBigDecimal("PostoRUC",stds.getBigDecimal("RUC").divide(stds.getBigDecimal("INAB"),4,java.math.BigDecimal.ROUND_HALF_UP).multiply(new java.math.BigDecimal("100.00")));
        }
        catch (Exception ex) {
          stds.setBigDecimal("PostoRUC",new java.math.BigDecimal("0.00"));
        }
        if (stds.getBigDecimal("KOL").signum() != 0)
          Aus.div(stds, "JRUC", "RUC", "KOL");
      } else {
        /*if (ts.getString("CSKL").equals(cskl))*/ stds.setBigDecimal("KOL", stds.getBigDecimal("KOL").add(ts.getBigDecimal("KOL")));

        /*if (ts.getString("VRDOK").equals("PRD") ||
            ts.getString("VRDOK").equals("RAC") ||
            ts.getString("VRDOK").equals("TER") ||
            ts.getString("VRDOK").equals("ODB") ||
            ts.getString("VRDOK").equals("GRN") ||
            ts.getString("VRDOK").equals("KAL") ||
            ts.getString("VRDOK").equals("POS") ||
            ts.getString("VRDOK").equals("ROT") ||
            ts.getString("VRDOK").equals("POD") 
            ) {
//          System.out.println("ts.getString(\"VRDOK\") " + ts.getString("VRDOK")); //XDEBUG delete when no more needed
        stds.setBigDecimal("KOL", stds.getBigDecimal("KOL").add(ts.getBigDecimal("KOL")));
        }*/
        
        stds.setBigDecimal("IRAZ", stds.getBigDecimal("IRAZ").add(ts.getBigDecimal("IRAZ")));
        stds.setBigDecimal("IPRODBP", stds.getBigDecimal("IPRODBP").add(ts.getBigDecimal("IPRODBP")));
        stds.setBigDecimal("POR", stds.getBigDecimal("POR").add(ts.getBigDecimal("POR")));
        stds.setBigDecimal("IPRODSP", stds.getBigDecimal("IPRODSP").add(ts.getBigDecimal("IPRODSP")));
        if (TypeDoc.getTypeDoc().isDocSklad(ts.getString("VRDOK"))) {
          stds.setBigDecimal("INAB", stds.getBigDecimal("INAB").add(ts.getBigDecimal("INAB")));
          stds.setBigDecimal("RUC", stds.getBigDecimal("RUC").add(ts.getBigDecimal("RUC")));
        } else if (raIzlazTemplate.isNabDirect()) {
          stds.setBigDecimal("INAB", stds.getBigDecimal("INAB").add(ts.getBigDecimal("RINAB")));
          stds.setBigDecimal("RUC", stds.getBigDecimal("RUC").add(ts.getBigDecimal("IPRODBP").subtract(ts.getBigDecimal("RINAB"))));
        }
        try {
          stds.setBigDecimal("PostoRUC",stds.getBigDecimal("RUC").divide(stds.getBigDecimal("INAB"),4,java.math.BigDecimal.ROUND_HALF_UP).multiply(new java.math.BigDecimal("100.00")));
        }
        catch (Exception ex) {
          stds.setBigDecimal("PostoRUC",new java.math.BigDecimal("0.00"));
        }
        if (stds.getBigDecimal("KOL").signum() != 0)
          Aus.div(stds, "JRUC", "RUC", "KOL");
      }
    } while (ts.next());
    if (sortum.equals("CART") || sortum.equals("CART1") || sortum.equals("BC") || sortum.equals("NAZART"))stds.setSort(new SortDescriptor(new String[] {sortum}));
    else stds.setSort(new SortDescriptor(new String[] {sortum},true,true));

    return stds;
  }

  public QueryDataSet getIspStatArtDetailsDS(String dateP, String dateZ, String sortum, String cskl, String cag, String ckup, String cpart, String artikli, String carting, String corg) {
    String ckupca = " ", dobart=" ", caprDobart="", sklad = "", cagenta="";
//    System.out.println("CPAR = " + cpart);
    if (!ckup.equals("")) ckupca = "and doki.cpar='" + ckup + "' ";
    if (!cpart.equals("")){
      dobart = ", dob_art ";
      ckupca = ckupca + "and dob_art.cpar = '" + cpart + "' and dob_art.cart = stdoki.cart ";
      caprDobart = ", dob_art.cpar as dcp ";
    }
    if (cag.length() > 0) cagenta = "and doki.cagent=" + cag + " ";
    
    if (cskl.equals("")){
      
      QueryDataSet skls;
      
      if (corg.equals("")) skls = dm.getSklad();
      else skls = hr.restart.robno.Util.getUtil().getSkladFromCorg(); // ut.getNewQueryDataSet("SELECT * FROM Sklad WHERE corg = '"+corg+"'");
      
      skls.open();
      skls.first();
      
      String sifskl = "";
      
      do {
        sifskl += "'"+skls.getString("CSKL")+"'";
        if (skls.next()) sifskl += ",";
        else break;
      } while (true);

      sklad = " AND DOKI.CSKL in ("+sifskl+")";
    } else {
      sklad = " AND DOKI.CSKL = '"+cskl+"'";
    }
    
    
    Condition cin = OrgStr.getCorgsCond("CSKL", corg).qualified("doki");
    String inq = cin == Condition.nil ? "1=1" : cin.toString();
    
    /*String inq;
    StorageDataSet corgs = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(corg);
    if (corgs.rowCount() == 0) inq = "1=1";
    else if (corgs.rowCount() == 1) inq = "DOKI.CSKL = '" + corg + "'";
    else inq = "(DOKI.CSKL in " + hr.restart.zapod.OrgStr.getOrgStr().getINQuery(corgs,"DOKI.CSKL")+") ";*/
    hr.restart.baza.Condition oj = hr.restart.baza.Condition.in("DOKI.VRDOK", TypeDoc.araj_docsOJ);
    String exInClude = "AND (("+oj+" AND "+inq+") OR ("+oj.not()+sklad+")) ";

    String queryString = "select doki.cpar, doki.pj, "+
//                         "doki.brdok "+
                         "stdoki.cskl, "+
                         "stdoki.cart, "+
                         "stdoki.cart1, "+
                         "stdoki.bc, "+
                         "artikli.vrart, "+
//                         "artikli.cgrart, "+
                         "stdoki.nazart, "+
                         "stdoki.jm, "+
                         "stdoki.kol, "+
                         "stdoki.iraz, "+
                         "stdoki.iprodbp, "+
                         "(stdoki.iprodsp - stdoki.iprodbp) as por, "+
                         "stdoki.iprodsp, "+
                         "stdoki.inab, stdoki.rinab, "+
                         "stdoki.iprodsp + stdoki.uirab as itot, "+
                         "(stdoki.iprodbp-stdoki.inab) as ruc "+caprDobart+
                         "from artikli, doki, stdoki " +dobart+
                         "where doki.cskl=stdoki.cskl and doki.brdok=stdoki.brdok "+
                         "and doki.god=stdoki.god and doki.vrdok=stdoki.vrdok AND stdoki.cart = artikli.cart " +
//                         " AND doki.vrdok != 'PON' AND doki.vrdok != 'NDO' AND doki.vrdok != 'NKU' AND stdoki.vrdok != 'RNL' AND stdoki.vrdok != 'REV' AND stdoki.vrdok != 'PRV' "+
                        " AND doki.vrdok not in ('PON','TRE','ZAH','NDO','NKU','RNL','REV','PRV','OTR','OTP','INM','INV','IZD','OTP', 'DOS') "+
                         exInClude+
                         cagenta+ckupca+
                         artikli+
                         carting+
                         " and doki.datdok between " +
                         dateP + " " + "and " + dateZ;

    /*String queryString ="select "+
                        "partneri.cpar, "+
                        "partneri.nazpar, "+
                        "stdoki.cskl, "+
                        "stdoki.cart, "+
                        "stdoki.cart1, "+
                        "stdoki.bc, " +
                        "stdoki.nazart, "+
                        "stdoki.jm, " +
                        "stdoki.kol, "+
                        "stdoki.iraz, "+
                        "stdoki.iprodbp, "+
                        "(stdoki.iprodsp - stdoki.iprodbp) as por, "+
                        "stdoki.iprodsp, "+
                        "stdoki.inab, "+
                        "(stdoki.iprodbp-stdoki.inab) as ruc "+caprDobart+
                        "from stdoki, doki, artikli, partneri" +dobart+
                        "where doki.cskl=stdoki.cskl and doki.brdok=stdoki.brdok and doki.god=stdoki.god and doki.vrdok=stdoki.vrdok AND stdoki.cart = artikli.cart " +
                        "and stdoki.vrdok in ('GOT','ROT','GRN','RAC','POD') "+
                        "and partneri.cpar = doki.cpar "+
                        cskl+
                        ckupca+
                        artikli+
                        carting+
                        " and doki.datdok between " +
                        dateP + " " + "and " + dateZ;*/

    System.err.println("\n-------------------------------------\n" + queryString + "\n-------------------------------------\n");

    QueryDataSet ts = ut.getNewQueryDataSet(queryString);
    if (ts.rowCount() == 0) return null;

    PartnerCache pc = new PartnerCache(true);
//    System.out.println("ts rowcount = " + ts.rowCount());

    QueryDataSet stds = new QueryDataSet();
    stds.setLocale(Locale.getDefault());
        stds.setColumns(new Column[] {
          dm.getPartneri().getColumn("CPAR").cloneColumn(),
          dm.getPartneri().getColumn("NAZPAR").cloneColumn(),
          dm.getPjpar().getColumn("PJ").cloneColumn(),
          dm.getPjpar().getColumn("NAZPJ").cloneColumn(),
          dm.getStdoki().getColumn("CSKL").cloneColumn(),
          dm.getStdoki().getColumn("CART").cloneColumn(),
          dm.getStdoki().getColumn("CART1").cloneColumn(),
          dm.getStdoki().getColumn("BC").cloneColumn(),
          dm.getStdoki().getColumn("NAZART").cloneColumn(),
          dm.getStdoki().getColumn("JM").cloneColumn(),
          dm.getStdoki().getColumn("KOL").cloneColumn(),
          dm.getStdoki().getColumn("IRAZ").cloneColumn(),
          dm.getStdoki().getColumn("INAB").cloneColumn(),
          dm.createBigDecimalColumn("RUC","RuC",2),
          dm.createBigDecimalColumn("PostoRUC","% RuC",2),
          dm.getStdoki().getColumn("IPRODBP").cloneColumn(),
          dm.createBigDecimalColumn("POR","Porez",2),
          dm.getStdoki().getColumn("IPRODSP").cloneColumn(),
          dm.createBigDecimalColumn("ITOT","Bez popusta",2)
        });
    stds.open();
    ts.setSort(new SortDescriptor(new String[] {"CPAR", "PJ", "CART"}));
    ts.first();
    DataSet pjp = dm.getPjpar();
    hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
    
    HashDataSet arts = new HashDataSet(dm.getArtikli(), "CART");

//    System.out.println("ts rowcount = " + ts.rowCount());
    //dm.getPartneri().open();
    boolean dbl = false;
    try {
      ts.getBigDecimal("POR");
    } catch (RuntimeException e) {
      dbl = true;
    }

    int cpar = -1027, cart = -1027, pj = -1027;
    do {
      if (ts.getInt("CPAR") != cpar || ts.getInt("PJ") != pj || ts.getInt("CART") != cart) {
        stds.insertRow(false);
        stds.setInt("CPAR", cpar = ts.getInt("CPAR"));
        stds.setInt("PJ", pj = ts.getInt("PJ"));
        stds.setString("NAZPJ","");
        if (pj > 0) {
        	DataRow dr = ld.raLookup(pjp, new String[] {"CPAR", "PJ"}, ts);
        	if (dr != null) stds.setString("NAZPJ", dr.getString("NAZPJ"));
        }
        /*ld.raLocate(dm.getPartneri(), "CPAR",ts.getInt("CPAR")+"");
        stds.setString("NAZPAR",dm.getPartneri().getString("NAZPAR"));*/
        stds.setString("NAZPAR",pc.getNameNotNull(cpar));
        stds.setString("CSKL",ts.getString("CSKL"));
        stds.setInt("CART",cart = ts.getInt("CART"));
        stds.setString("CART1",ts.getString("CART11"));
        stds.setString("BC",ts.getString("BC"));
        
        if (!raVart.isStanje(ts)) {
          if (arts.loc(ts)) stds.setString("NAZART", arts.get().getString("NAZART"));
          else stds.setString("NAZART",ts.getString("NAZART"));
        } else {
          stds.setString("NAZART",ts.getString("NAZART"));
        }
        
        stds.setString("JM",ts.getString("JM"));

        /*if (ts.getString("CSKL").equals(cskl))*/ stds.setBigDecimal("KOL", ts.getBigDecimal("KOL"));

        /*if (ts.getString("VRDOK").equals("PRD") ||
            ts.getString("VRDOK").equals("RAC") ||
            ts.getString("VRDOK").equals("TER") ||
            ts.getString("VRDOK").equals("ODB") ||
            ts.getString("VRDOK").equals("GRN") ||
            ts.getString("VRDOK").equals("KAL") ||
            ts.getString("VRDOK").equals("POS") ||
            ts.getString("VRDOK").equals("ROT") ||
            ts.getString("VRDOK").equals("POD") 
            ) {
//          System.out.println("ts.getString(\"VRDOK\") " + ts.getString("VRDOK")); //XDEBUG delete when no more needed
        stds.setBigDecimal("KOL", stds.getBigDecimal("KOL").add(ts.getBigDecimal("KOL")));
        }*/
        
//        else stds.setBigDecimal("KOL", new java.math.BigDecimal("0.00"));

//        stds.setBigDecimal("KOL", stds.getBigDecimal("KOL").add(ts.getBigDecimal("KOL")));
        stds.setBigDecimal("IRAZ", ts.getBigDecimal("IRAZ"));
        stds.setBigDecimal("IPRODBP", ts.getBigDecimal("IPRODBP"));
        stds.setBigDecimal("POR", dbl ? new BigDecimal(ts.getDouble("POR")) : ts.getBigDecimal("POR"));
        stds.setBigDecimal("IPRODSP", ts.getBigDecimal("IPRODSP"));
        if (TypeDoc.getTypeDoc().isDocSklad(ts.getString("VRDOK"))) {
          stds.setBigDecimal("INAB", ts.getBigDecimal("INAB"));
          stds.setBigDecimal("RUC", dbl ? new BigDecimal(ts.getDouble("RUC")) : ts.getBigDecimal("RUC"));
        } else if (raIzlazTemplate.isNabDirect()) {
          stds.setBigDecimal("INAB", ts.getBigDecimal("RINAB"));
          stds.setBigDecimal("RUC", ts.getBigDecimal("IPRODBP").subtract(ts.getBigDecimal("RINAB")));
        }
        stds.setBigDecimal("ITOT", stds.getBigDecimal("ITOT").add(dbl ? new BigDecimal(ts.getDouble("ITOT")) : ts.getBigDecimal("ITOT")));
        try {
          stds.setBigDecimal("PostoRUC",stds.getBigDecimal("RUC").divide(stds.getBigDecimal("INAB"),4,java.math.BigDecimal.ROUND_HALF_UP).multiply(new java.math.BigDecimal("100.00")));
        }
        catch (Exception ex) {
          stds.setBigDecimal("PostoRUC",new java.math.BigDecimal("0.00"));
        }
      } else {
//        stds.setBigDecimal("KOL", stds.getBigDecimal("KOL").add(ts.getBigDecimal("KOL")));
        /*if (ts.getString("CSKL").equals(cskl))*/ stds.setBigDecimal("KOL", stds.getBigDecimal("KOL").add(ts.getBigDecimal("KOL")));
        
        /*if (ts.getString("VRDOK").equals("PRD") ||
            ts.getString("VRDOK").equals("RAC") ||
            ts.getString("VRDOK").equals("TER") ||
            ts.getString("VRDOK").equals("ODB") ||
            ts.getString("VRDOK").equals("GRN") ||
            ts.getString("VRDOK").equals("KAL") ||
            ts.getString("VRDOK").equals("POS") ||
            ts.getString("VRDOK").equals("ROT") ||
            ts.getString("VRDOK").equals("POD") 
            ) {
//          System.out.println("ts.getString(\"VRDOK\") " + ts.getString("VRDOK")); //XDEBUG delete when no more needed
        stds.setBigDecimal("KOL", stds.getBigDecimal("KOL").add(ts.getBigDecimal("KOL")));
        }*/
        
        stds.setBigDecimal("IRAZ", stds.getBigDecimal("IRAZ").add(ts.getBigDecimal("IRAZ")));
        stds.setBigDecimal("IPRODBP", stds.getBigDecimal("IPRODBP").add(ts.getBigDecimal("IPRODBP")));
        stds.setBigDecimal("POR", stds.getBigDecimal("POR").add(dbl ? new BigDecimal(ts.getDouble("POR")) : ts.getBigDecimal("POR")));
        stds.setBigDecimal("IPRODSP", stds.getBigDecimal("IPRODSP").add(ts.getBigDecimal("IPRODSP")));
        if (TypeDoc.getTypeDoc().isDocSklad(ts.getString("VRDOK"))) {
          stds.setBigDecimal("INAB", stds.getBigDecimal("INAB").add(ts.getBigDecimal("INAB")));
          stds.setBigDecimal("RUC", stds.getBigDecimal("RUC").add(dbl ? new BigDecimal(ts.getDouble("RUC")) : ts.getBigDecimal("RUC")));
        } else if (raIzlazTemplate.isNabDirect()) {
          stds.setBigDecimal("INAB", stds.getBigDecimal("INAB").add(ts.getBigDecimal("RINAB")));
          stds.setBigDecimal("RUC", stds.getBigDecimal("RUC").add(ts.getBigDecimal("IPRODBP").subtract(ts.getBigDecimal("RINAB"))));
        }
        stds.setBigDecimal("ITOT", stds.getBigDecimal("ITOT").add(dbl ? new BigDecimal(ts.getDouble("ITOT")) : ts.getBigDecimal("ITOT")));
        try {
          stds.setBigDecimal("PostoRUC",stds.getBigDecimal("RUC").divide(stds.getBigDecimal("INAB"),4,java.math.BigDecimal.ROUND_HALF_UP).multiply(new java.math.BigDecimal("100.00")));
        }
        catch (Exception ex) {
          stds.setBigDecimal("PostoRUC",new java.math.BigDecimal("0.00"));
        }
      }
//      System.out.println("ts row = " + ts.getRow());
    } while (ts.next());
    if (sortum.equals("CART") || sortum.equals("CART1") || sortum.equals("BC"))stds.setSort(new SortDescriptor(new String[] {sortum}));
    //else stds.setSort(new SortDescriptor(new String[] {sortum},true,true));
    else stds.setSort(new SortDescriptor(new String[] {"NAZART"}));

//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(stds);

    return stds;
  }

//  public String getStringIspStatArt(String dateP, String dateZ, String qDodatakNaStr, String artikliFilter) {
//    String queryString ="select "+
//                        "max(stdoki.cskl) as cskl, "+
//                        "max(stdoki.cart) as cart, "+
//                        "max(stdoki.cart1) as cart1, "+
//                        "max(stdoki.bc) as bc, " +
//                        "max(stdoki.nazart) as nazart, "+
//                        "max(stdoki.jm) as jm, " +
//                        "sum(stdoki.kol) as kol, "+
//                        "sum(stdoki.iraz) as iraz, "+
//                        "sum(stdoki.iprodbp) as iprodbp, "+
//                        "sum(stdoki.iprodsp - stdoki.iprodbp) as por, "+
//                        "sum(stdoki.iprodsp) as iprodsp, "+
//                        "sum(stdoki.inab) as inab, "+
//                        "sum(stdoki.iprodbp-stdoki.inab) as ruc "+
//                        "from stdoki, doki, artikli " +
//                        "where doki.cskl=stdoki.cskl and doki.brdok=stdoki.brdok and doki.god=stdoki.god and doki.vrdok=stdoki.vrdok AND stdoki.cart = artikli.cart " +
//                        "and (stdoki.vrdok='GOT' or stdoki.vrdok='ROT') "+
//                        qDodatakNaStr +
//                        "and doki.datdok between " + dateP + " " + "and " + dateZ + " " +
//                        artikliFilter +
//                        "group by cskl, cart, nazart ";
//    return queryString;
//  }

//  public void debugPanel(){
//    javax.swing.JOptionPane.showMessageDialog(null,
//        new hr.restart.swing.raMultiLineMessage(new String[] {"Pritisni za dalje"}),
//        "Debug by SrkY",
//        javax.swing.JOptionPane.PLAIN_MESSAGE);
//  }

//  public static Column bd = hr.restart.baza.dM.createBigDecimalColumn("c2");

  public String format(DataSet set, String colName) {
    return format(set,colName,2);
  }

  public String format(DataSet set, String colName, int precision) {
    Column bd = hr.restart.baza.dM.createBigDecimalColumn("c2",precision);
    com.borland.dx.text.VariantFormatter formater = bd.getFormatter();
    com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
    set.getVariant(colName,v);

    return formater.format(v);
  }

  public String format2(DataSet set, String colName) {
    Column bd = set.getColumn(colName);//hr.restart.baza.dM.createBigDecimalColumn("c2",2);
    com.borland.dx.text.VariantFormatter formater = bd.getFormatter();
    com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
    set.getVariant(colName,v);

    return formater.format(v);
  }

  public String format(BigDecimal bigd, int precision) {
    StorageDataSet ds = new StorageDataSet();
    ds.setColumns(new Column[] {dm.createBigDecimalColumn("DUMMY",precision)});
    ds.open();
    ds.setBigDecimal("DUMMY",bigd);
    Column bd = hr.restart.baza.dM.createBigDecimalColumn("c2",precision);
    com.borland.dx.text.VariantFormatter formater = bd.getFormatter();
    com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
    ds.getVariant("DUMMY",v);

    try {
      return formater.format(v);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

//  /**
//   * Geter koji vraèa malo veèi string.<br>
//   * Koristi se u klasi <b>upPregledTroškova</b> i služi za slaganje rezSet-a koji prikazuje pregled troškova<br>
//   * u razdoblju omeðenom poèetnim i krajnjim datumom.
//   * @param dateP Poèetni datum razdoblja za koji se pregledavaju troškovi (default - prvi dan u tekuèoj godini)
//   * @param dateZ Krajnji datum razdoblja za koji se pregledavaju troškovi (default - današnji dan)
//   * @param qDodatakNaStr dodatak kojim se odreðuju neke sitnice - vidi <b>upPregledTroskova</b>
//   * @return <b>queryString</b>
//   * @see <b>upPregledTroskova</b>
//   */

//  public String getStringUpPregledTroskova(String dateP, String dateZ, String qDodatakNaStr) {
//    String queryString ="SELECT max(doki.corg) as corg, max(doki.cskl) as cskl, max(doki.datdok) as datdok, max(doki.vrdok) as vrdok, " +
//                        "max(doki.cradnal) as cradnal, max(doki.brdok) as brdok, max(stdoki.cart) as cart, max(stdoki.nazart) as nazart, " +
//                        "max(stdoki.jm) as jm, max(stdoki.kol) as kol, sum(stdoki.iraz) as iraz, max(sklad.nazskl) as nazskl, " +
//                        "max(orgstruktura.naziv) as nazorg, max(vrtros.naziv) as vrstros " +
//                        "FROM DOKI,STDOKI,SKLAD,ORGSTRUKTURA,VRTROS " +
//                        "WHERE DOKI.VRDOK='IZD' " + qDodatakNaStr +
//                        " AND " + util.getDoc("DOKI", "STDOKI") +
//                        " AND DOKI.DATDOK >=" + dateP +
//                        " AND DOKI.DATDOK <=" + dateZ +
//                        " AND DOKI.CSKL=SKLAD.CSKL AND DOKI.CORG=ORGSTRUKTURA.CORG" +
//                        " GROUP BY brdok, cskl";
//    return queryString;
//  }
}
