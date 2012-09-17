/****license*****************************************************************
**   file: Asql.java
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
package hr.restart.sisfun;

import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.robno.Aut;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raMasterDetail;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Asql {
  private static Valid vl = Valid.getValid();
  private static hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  private static dM dm = dM.getDataModule();
  private static HashMap lq = new HashMap();
  private static Variant var = new Variant();

  private Asql() {
  }
  
  public static int getNextRbs(String table, String column, Condition where) {
    return getNextRbs(table, column, where.toString());
  }
  
  public static int getNextRbs(String table, String column) {
    return getNextRbs(table, column, "");
  }
  
  public static int getNextRbs(String table, String column, String where) {
    DataSet ds = Util.getNewQueryDataSet("SELECT MAX("+column+") as mx FROM "+table+
        (where.length() == 0 ? "" : " WHERE "+where), true);
    ds.getVariant("MX", var);
    return var.getAsInt() + 1;
  }

  /**
   * Vra\u0107a QueryDataSet svih artikala neke skupine artikala, otvoren i
   * premotan na prvi slog. Ne klonira kolone tako da nije pogodan za
   * tabli\u010Dni prikaz nego samo za interne kalkulacije. DataSet sadrži kolone
   * tablice skupart te kolonu artikli.vrart.<p>
   * @param cskupart Šifra tražene skupine artikala.
   * @return QueryDataSet artikala skupine.
   */

  public static QueryDataSet getArtikliSkupine(String cskupart) {
    vl.execSQL("SELECT skupart.*,artikli.vrart, "+
           "artikli.cart as dummy FROM skupart,artikli "+
           "WHERE skupart.cart = artikli.cart AND skupart.cskupart = '"+cskupart+"'");
    vl.RezSet.open();
    vl.RezSet.first();
    return vl.getDataAndClear();
  }

  /**
   * Vra\u0107a QueryDataSet svih stavki odre\u0111enog radnog naloga. Stavke
   * mogu biti artikli usluge ili normirani artikli. Koristiti u paru sa
   * metodom getRNLstavkeSkupine(). DataSet ima samo kolone cart, cart1, bc,
   * nazart, jm i kol, tako da je pogodan samo za interne kalkulacije.<p>
   * @param cradnal šifra radnog naloga.
   * @return QueryDataSet s kolonama cart, cart1, bc, nazart, jm i kol.
   */

  public static QueryDataSet getRNLstavke(String cradnal) {
    vl.execSQL("SELECT cart,cart1,bc,nazart,jm,kol FROM stdoki "+
         "WHERE stdoki.vrdok = 'RNL' AND cradnal = '"+cradnal+"'");
    vl.RezSet.open();
    vl.RezSet.first();
    return vl.getDataAndClear();
  }

  /**
   * Vra\u0107a QueryDataSett stavki skupine artikala po kojima je
   * nalog ra\u0111en, a koje nisu u stavkama samog radnog naloga (e.g.
   * roba i materijali). Koristiti u paru sa metodom getRNLstavke().<p>
   * @param cskupart šifra skupine artikala (sa zaglavlja radnog naloga)
   * @param cradnal šifra radnog naloga.
   * @return QueryDataSet s kolonama cart, cart1, bc, nazart, jm i kol.
   */

  public static QueryDataSet getRNLstavkeSkupine(String cskupart, String cradnal) {
    vl.execSQL("SELECT cart,cart1,bc,nazart,jm,kol FROM skupart "+
           "WHERE cskupart = '"+cskupart+"' AND NOT EXISTS "+
           "(SELECT * FROM stdoki WHERE stdoki.vrdok = 'RNL'"+
           " AND cradnal = '"+cradnal+ "' AND skupart.cart = stdoki.cart)");
    vl.RezSet.open();
    vl.RezSet.first();
    return vl.getDataAndClear();
  }

  /**
   * Postavlja query na QueryDataSetu za dohvat stavki svih izlaznih dokumenata
   * odre\u0111enog tipa sa zadanog skladišta u nekom vremenskom razdoblju.
   * Koristi se za print providere master dijela. QueryDataSet ima sve
   * kolone tablica doki i stdoki, te kolone sklad.nazskl i partneri.nazpar.<p>
   * @param ds QueryDataSet kojem se setira query. Mora biti unaprijed instanciran.
   * @param source pozivaju\u0107i raMasterDetail. Iz njega se uzimaju podaci o skladištu,
   * vrsti dokumenta i datumskom rasponu.
   */

  public static void createIzlazMaster(QueryDataSet ds, raMasterDetail source) {
    ds.close();
    ds.closeStatement();
    DataSet pres = source.getPreSelect().getSelRow();
    ds.setQuery(new QueryDescriptor(dm.getDatabase1(),
      "SELECT * FROM doki,stdoki WHERE "+rut.getDoc("doki","stdoki")+" AND "+
      Condition.between("doki.datdok", pres.getTimestamp("DATDOK-from"),
      pres.getTimestamp("DATDOK-to"))+
      " AND stdoki.cskl = '"+source.getMasterSet().getString("CSKL")+
      "' AND stdoki.vrdok = '"+source.getMasterSet().getString("VRDOK")+"'"
    ));
    ds.open();
  }

  /**
   * Postavlja query na QueryDataSetu za dohvat stavki odre\u0111enog izlaznog dokumenta.
   * Koristi se za print providere detail dijela. QueryDataSet ima sve kolone tablica
   * doki i stdoki, te kolone sklad.nazskl i partneri.nazpar.<p>
   * @param ds QueryDataSet kojem se setira query. Mora biti unaprijed instanciran.
   * @param source pozivaju\u0107i raMasterDetail.
   */

  public static void createIzlazDetail(QueryDataSet ds, raMasterDetail source) {
    ds.close();
    ds.closeStatement();
    ds.setQuery(new QueryDescriptor(dm.getDatabase1(),
      "SELECT * FROM doki,stdoki WHERE "+rut.getDoc("doki","stdoki")+
      " AND stdoki.cskl = '"+source.getMasterSet().getString("CSKL")+
      "' AND stdoki.vrdok = '"+source.getMasterSet().getString("VRDOK")+
      "' AND stdoki.god = '"+source.getMasterSet().getString("GOD")+
      "' AND stdoki.brdok = "+source.getMasterSet().getInt("BRDOK")
    ));
    ds.open();
  }

  /**
   * Postavlja QueryDataSet za fejkani master za normirane artikle.
   * Sadrži kolone cartnor, cart, cart1, bc, nazart i jm, koje se
   * odnose na normirani artikl (cartnor = cart).<p>
   * @param mast QueryDataSet koji \u0107e saržavati rezultat. Mora biti
   * instanciran unaprijed.
   */
  public static void createMasterNorme(QueryDataSet mast) {
    //vl.execSQL(sql);
    mast.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),
      "SELECT norme.cartnor as cartnor, MAX(artikli.cart) as cart, "+
      "MAX(artikli.cart1) as cart1, MAX(artikli.bc) as bc, MAX(artikli.nazart) as nazart, "+
      "MAX(artikli.jm) as jm, MAX(artikli.cgrart) as cgrart FROM norme,artikli WHERE norme.cartnor = artikli.cart GROUP BY cartnor"
    ));
    //part = vl.RezSet;
    mast.setColumns(new Column[] {
      (Column) dm.getNorme().getColumn("CARTNOR").clone(),
      (Column) dm.getArtikli().getColumn("CART").clone(),
      (Column) dm.getArtikli().getColumn("CART1").clone(),
      (Column) dm.getArtikli().getColumn("BC").clone(),
      (Column) dm.getArtikli().getColumn("NAZART").clone(),
      (Column) dm.getArtikli().getColumn("JM").clone(),
      (Column) dm.getArtikli().getColumn("CGRART").clone()
    });

    mast.open();
    mast.setRowId("CARTNOR", true);
    mast.setRowId("CART", false);
    mast.setRowId("CART1", false);
    mast.setRowId("BC", false);
    mast.setRowId("NAZART", false);
    mast.setRowId("JM", false);
    mast.setRowId("CGRART", false);
    mast.setTableName("norme_master");
  }

  /**
   * Postavlja QueryDataSet za fejkani master za cjenik.
   * Sadrži kolone cpar, nazpar, cskl i nazskl. <b> i corg po novome</b> by S.G.<p>
   * @param mast QueryDataSet koji \u0107e saržavati rezultat. Mora biti
   * instanciran unaprijed.
   */

  public static void createMasterCjenik(QueryDataSet mast, String what, String dummy) {
    String nesTi,eksept;
    if (what.equalsIgnoreCase("CSKL")) {
      nesTi = "cjenik.cskl, "+
		"max(sklad.nazskl) as nazskl " +
		"FROM cjenik,partneri,sklad "+
      "WHERE cjenik.cpar = partneri.cpar AND cjenik.cskl = sklad.cskl ";
      eksept = "and cjenik.cskl != '"+dummy+"' GROUP BY cjenik.cskl,cjenik.cpar";
    } else {
      nesTi = "max ('"+dummy+"') as cskl, max ('"+dummy+"') as nazskl " + 
        "FROM cjenik,partneri "+
      "WHERE cjenik.cpar = partneri.cpar  ";
      eksept = "and cjenik.corg != '"+dummy+"' GROUP BY cjenik.corg,cjenik.cpar";
    }
    
//  "SELECT cjenik.cpar, MAX(partneri.nazpar) as nazpar, " +
//  "cjenik.cskl, MAX(sklad.nazskl) as nazskl, cjenik.corg FROM cjenik,partneri,sklad " +
//  "WHERE cjenik.cpar = partneri.cpar AND cjenik.cskl = sklad.cskl " +
//  "GROUP BY cjenik.cpar, cjenik.cskl"  
    
    mast.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),
        "SELECT "+ 
        "cjenik.cpar as cpar, "+ 
        "max(partneri.nazpar) as nazpar, " +
        (what.equalsIgnoreCase("CSKL")?
        "max(cjenik.corg) as corg, ":"cjenik.corg as corg, ") + 

        nesTi+ 

        eksept
        
        
    ));

    mast.setColumns(new Column[] {
      (Column) dm.getCjenik().getColumn("CPAR").clone(),
      (Column) dm.getPartneri().getColumn("NAZPAR").clone(),
      (Column) dm.getCjenik().getColumn("CSKL").clone(),
      (Column) dm.getSklad().getColumn("NAZSKL").clone(),
      (Column) dm.getOrgstruktura().getColumn("CORG").clone()
    });
    mast.getColumn("NAZPAR").setCaption("Naziv partnera");
    mast.getColumn("NAZSKL").setCaption("Naziv skladišta");

    mast.open();
    mast.setRowId("CPAR", true);
    mast.setRowId("NAZPAR", false);
    mast.setRowId("CSKL", true);
    mast.setRowId("NAZSKL", false);
    mast.setRowId("CORG", false);
    mast.setTableName("Cjenik_master");
  }
  
  public static void createMasterRab(QueryDataSet mast) {   
    mast.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),
        "SELECT rabshema.cpar as cpar, "+ 
        "max(partneri.nazpar) as nazpar FROM rabshema,partneri " +
        "WHERE rabshema.cpar = partneri.cpar GROUP BY rabshema.cpar"
    ));

    mast.setColumns(new Column[] {
      (Column) dm.getCjenik().getColumn("CPAR").clone(),
      (Column) dm.getPartneri().getColumn("NAZPAR").clone(),
    });
    mast.getColumn("NAZPAR").setCaption("Naziv partnera");
    
    mast.open();
    mast.setRowId("CPAR", true);
    mast.setRowId("NAZPAR", false);
    mast.setTableName("Rabshema_master");
  }


  /**
   * Vra\u0107a QueryDataSet svih normativa nekog normiranog artikla.<p>
   * @param cartnor šifra normiranog artikla.
   * @return QueryDataSet.
   */

  public static QueryDataSet getNormArt(int cartnor) {
    vl.execSQL("SELECT * FROM norme WHERE cartnor = " + cartnor);
    vl.RezSet.open();
    return vl.getDataAndClear();
  }

  /**
   * Postavlja QueryDataSet za fejkani master za skupine artikala.
   * Sadrži kolone cskupart i nazskupart.<p>
   * @param mast QueryDataSet koji \u0107e sadržavati rezultat. Mora biti
   * instanciran unaprijed.
   */

  public static void createMasterSkupart(QueryDataSet mast) {
    mast.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),
      "SELECT cskupart, MAX(nazskupart) as nazskupart "+
      "FROM skupart GROUP BY cskupart"
    ));
    mast.setColumns(new Column[] {
      (Column) dm.getSkupart().getColumn("CSKUPART").clone(),
      (Column) dm.getSkupart().getColumn("NAZSKUPART").clone()
    });
    mast.open();
    mast.setRowId("CSKUPART", true);
    mast.setRowId("NAZSKUPART", false);
    mast.setTableName("skupart_master");
  }

  /**
   * Postavlja QueryDataSet za fejkani master za tablice kamate.
   * Sadrži kolone ckam i opis.<p>
   * @param mast QueryDataSet koji \u0107e sadržavati rezultat. Mora biti
   * instanciran unaprijed.
   */

  public static void createMasterKamate(QueryDataSet mast) {
    mast.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),
      "SELECT kamate.ckam as ckam, MAX(opis) as opis "+
      "FROM kamate GROUP BY ckam"
    ));
    mast.setColumns(new Column[] {
      (Column) dm.getKamate().getColumn("CKAM").clone(),
      (Column) dm.getKamate().getColumn("OPIS").clone()
    });
    mast.open();
    mast.setRowId("CKAM", true);
    mast.setRowId("OPIS", false);
    mast.setTableName("kamate_master");
  }

  /**
   * Vra\u0107a QueryDataSet svih zna\u010Dajki odre\u0111ene vrste subjekta.<p>
   * @param cvrsubj Šifra vrste subjekta.
   * @return QueryDataSet.
   */
  public static QueryDataSet getZnacVrsub(short cvrsubj) {
    vl.execSQL("SELECT * FROM RN_znacajke WHERE cvrsubj = "+cvrsubj+" ORDER BY cznac DESCENDING");
    vl.RezSet.open();
    return vl.getDataAndClear();
  }

  /**
   * Vra\u0107a QueryDataSet koji sadrži neku zna\u010Dajku subjekta (1 slog).<p>
   * @param cvrsubj vrsta subjekta.
   * @param cznac Šifra znažajke.
   * @return QueryDataSet.
   */
  public static QueryDataSet getZnac(short cvrsubj, short cznac) {
    vl.execSQL("SELECT * FROM RN_znacajke WHERE cvrsubj = "+cvrsubj+" AND cznac = "+cznac);
    vl.RezSet.open();
    return vl.getDataAndClear();
  }

  /**
   * Postavlja QueryDataSet za fejkani master za vrste subjekata.
   * Sadrži kolone cvrsubj i nazvrsubj.<p>
   * @param mast QueryDataSet koji \u0107e sadržavati rezultat. Mora biti
   * instanciran unaprijed.
   */

/*  public static void createMasterZnac(QueryDataSet mast) {
    mast.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),
      "SELECT cvrsubj, MAX(nazvrsubj) as nazvrsubj FROM RN_znacajke GROUP BY cvrsubj"
    ));
    mast.setColumns(new Column[] {
      (Column) dm.getRN_znacajke().getColumn("CVRSUBJ").clone(),
      (Column) dm.getRN_znacajke().getColumn("NAZVRSUBJ").clone()
    });
    mast.open();
    mast.setRowId("CVRSUBJ", true);
    mast.setRowId("NAZVRSUBJ", false);
    mast.setTableName("znacajke_master");
  } */

  /**
   * Postavlja QueryDataSet za fejkani master za šifrarnik zna\u010Dajki.
   * Sadrži kolone cvrsubj, nazvrsubj, cznac i znacopis.<p>
   * @param mast QueryDataSet koji \u0107e sadržavati rezultat. Mora biti
   * instanciran unaprijed.
   */
/*  public static void createMasterSifznac(QueryDataSet mast) {
    String sql = "SELECT RN_sifznac.cvrsubj, MAX(RN_znacajke.nazvrsubj) as nazvrsubj, "+
                 "RN_sifznac.cznac, MAX(RN_znacajke.znacopis) as znacopis "+
                 "FROM RN_sifznac, RN_znacajke "+
                 "WHERE RN_sifznac.cznac = RN_znacajke.cznac "+
                 "AND RN_sifznac.cvrsubj = RN_znacajke.cvrsubj "+
                 "GROUP BY RN_sifznac.cvrsubj, RN_sifznac.cznac";

    //vl.execSQL(sql);
    mast.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),
      "SELECT RN_sifznac.cvrsubj, MAX(RN_znacajke.nazvrsubj) as nazvrsubj, "+
      "RN_sifznac.cznac, MAX(RN_znacajke.znacopis) as znacopis "+
      "FROM RN_sifznac, RN_znacajke "+
      "WHERE RN_sifznac.cznac = RN_znacajke.cznac "+
      "AND RN_sifznac.cvrsubj = RN_znacajke.cvrsubj "+
      "GROUP BY RN_sifznac.cvrsubj, RN_sifznac.cznac"
    ));
    //part = vl.RezSet;

    mast.setColumns(new Column[] {
      (Column) dm.getRN_sifznac().getColumn("CVRSUBJ").clone(),
      (Column) dm.getRN_znacajke().getColumn("NAZVRSUBJ").clone(),
      (Column) dm.getRN_sifznac().getColumn("CZNAC").clone(),
      (Column) dm.getRN_znacajke().getColumn("ZNACOPIS").clone()
    });

    mast.open();
    mast.setRowId("CVRSUBJ", true);
    mast.setRowId("NAZVRSUBJ", false);
    mast.setRowId("CZNAC", true);
    mast.setRowId("ZNACOPIS", false);
    mast.setTableName("sifznac_master");
  }
*/
  /**
   * Vra\u0107a SQL naredbu za dohvat vrijednosti neke zna\u010Dajke odre\u0111enog subjekta.
   * @param cvrsubj šifra vrste subjekta.
   * @param cznac šifra zna\u010Dajke.
   * @param vriznac
   * @return
   */
 /* public static String SQL_getVriznac(short cvrsubj, short cznac, String vriznac) {
    return "SELECT * FROM RN_sifznac WHERE cvrsubj = "+cvrsubj+
           " AND cznac = "+cznac+" AND vriznac = '"+vriznac+"'";
  } */

  /**
   * Vra\u0107a maksimalni broj kolone CKEY u tablici Kljucevi.<p>
   * @param cuser user.
   * @return MAX(ckey).
   */
  public static int getMaxKey(String cuser) {
    vl.execSQL("SELECT MAX(ckey) FROM Kljucevi WHERE cuser ='"+cuser+"'");
    return vl.getSetCount(vl.getDataAndClear(), 0);
  }

  /**
   * Postavlja QueryDataSet za fejkani master za defaultne sheme kontiranja.
   * Sadrži kolone vrdok i nazdok.<p>
   * @param mast QueryDataSet koji \u0107e sadržavati rezultat. Mora biti
   * instanciran unaprijed.
   */
  public static void createMasterDefshkonta(QueryDataSet mast) {
    mast.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),
      "SELECT vrdokum.vrdok,MAX(vrdokum.nazdok) as nazdok,MAX(defshkonta.app) as app FROM defshkonta,vrdokum "+
      "WHERE defshkonta.vrdok = vrdokum.vrdok GROUP BY vrdokum.vrdok"
    ));
    mast.setColumns(new Column[] {
      (Column) dm.getDefshkonta().getColumn("VRDOK").clone(),
      (Column) dm.getVrdokum().getColumn("NAZDOK").clone(),
      (Column) dm.getDefshkonta().getColumn("APP").clone()
    });
    mast.open();
    mast.setRowId("VRDOK", true);
    mast.setRowId("NAZDOK", true);
    mast.setRowId("APP", false);
    mast.setTableName("defshkonta_master");
  }

  /**
   * Vra\u0107a QueryDataSet stavki defaultne sheme kontiranja za
   * neku vrstu dokumenta.<p>
   * @param vrdok vrsta dokumenta.
   * @return QueryDataSet.
   */
  public static QueryDataSet getStavkeDefshkonta(String vrdok) {
    vl.execSQL("SELECT * from defshkonta WHERE vrdok = '" + vrdok + "'");
    vl.RezSet.open();
    return vl.getDataAndClear();
  }

  /**
   * Vra\u0107a QueryDataSet stavke defaultne sheme kontiranja za
   * neku vrstu dokumenta (1 slog).<p>
   * @param vrdok vrsta dokumenta.
   * @param stavka šifra stavke.
   * @return QueryDataSet.
   */
  public static QueryDataSet getStavkaDefshkonta(String vrdok, String stavka) {
    vl.execSQL("SELECT * from defshkonta WHERE vrdok = '" +
       vrdok + "' and stavka = " + stavka);
    vl.RezSet.open();
    return vl.getDataAndClear();
  }

  /**
   * Vra\u0107a QueryDataSet sa shemom kontiranja za traženu aplikaciju,
   * skladište (odn. vrsta sheme) i vrstu dokumenta.
   * @param app šifra aplikacije
   * @param cskl skladište odn. vrsta sheme.
   * @param vrdok vrsta dokumenta.
   * @return QueryDataSet sa stavkama sheme.
   */
  public static QueryDataSet getShkonta(String app, String cskl, String vrdok) {
    String key = app + ":" + ((cskl == null) ? "" : cskl) + ":" + vrdok;
    if (!lq.containsKey(key)) {
      String where = "vrdok = '" + vrdok + ((cskl == null) ? "" : "' AND cskl = '" + cskl) + "'";
      QueryDataSet newd = hr.restart.baza.Shkonta.getDataModule().getFilteredDataSet(where);
      lq.put(key, newd);
    }
    return (QueryDataSet) lq.get(key);
  }

  /**
   * Izvodi propagaciju promjene vrijednosti kolona fejkanog mastera koje
   * nisu kljucevi (jer se oni ne mogu promijeniti). Npr. ako se promijeni
   * naziv skupine artikala, isti se mora propagirati po svim slogovima tablice
   * koji pripadaju istoj skupini artikala.<p>
   * @param table ime tablice sa fejkanim masterom.
   * @param master DataSet fejkanog mastera, mora biti postavljen na slog u kojem
   * se nalaze ispravne vrijednosti kljuca i ostalih kolona fejkanog mastera.
   * @param key niz imena kolona koje predstavljaju klju\u010D fejkanog mastera.
   * @param prop niz imena kolona \u010Dije promjene se propagiraju.
   */
  public static void propagateMasterChanges(String table, DataSet master, String[] key, String[] prop) {
    Variant v = new Variant();
    VarStr sets = new VarStr(64);
    VarStr wheres = new VarStr(64);
    for (int i = 0; i < prop.length; i++) {
      master.getVariant(prop[i], v);
      sets.append(prop[i]).append(" = '").append(v).append("', ");
    }
    sets.chop(2);
    for (int i = 0; i < key.length; i++) {
      master.getVariant(key[i], v);
      wheres.append(key[i]).append(" = '").append(v).append("' AND ");
    }
    wheres.chop(5);
    System.out.println("UPDATE "+table+" SET "+sets+" WHERE "+wheres);
    vl.runSQL("UPDATE "+table+" SET "+sets+" WHERE "+wheres);
  }

  /**
   * Vra\u0107a QueryDataSet sa vrijednostima u šifrarniku za neku zna\u010Dajku.<p>
   * @param cvrsubj vrsta subjekta.
   * @param cznac šifra zna\u010Dajke.
   * @return QueryDataSet, pogodan za jlrNavField.
   */
  public static QueryDataSet getVriSifZnac(short cvrsubj, short cznac) {
    vl.execSQL("SELECT * FROM RN_sifznac WHERE "+
               "cvrsubj = "+cvrsubj+" AND cznac = "+cznac);
    vl.RezSet.open();
    vl.RezSet.getColumn("VRIZNAC").setCaption("Vrijednost");
    vl.RezSet.getColumn("OPIS").setCaption("Opis vrijednosti");
    return vl.getDataAndClear();
  }

  /**
   * Vra\u0107a QueryDataSet sa vrijednostima zna\u010Dajki nekog subjekta.<p>
   * @param csubrn šifra subjekta.
   * @return QueryDataSet.
   */
  public static QueryDataSet getVriznacSubjekta(String csubrn) {
    vl.execSQL("SELECT * FROM RN_znacsub WHERE csubrn = '"+csubrn+"' and cradnal =''");
    vl.RezSet.open();
    return vl.getDataAndClear();
  }
  
  /**
   * Vra\u0107a QueryDataSet sa popisom skladišta koja na stanju imaju odre\u0111eni artikl.<p>
   * @param cart šifra traženog artikla.
   * @param open otvoriti.
   * @return QueryDataSet.
   */
  public static QueryDataSet getArtiklSklad(String cart, boolean open) {
    return getArtiklSklad(cart,open,hr.restart.util.Util.getUtil().getYear(hr.restart.util.Util.getUtil().getFirstDayOfYear()));
  }

  public static QueryDataSet getArtiklSklad(String cart, boolean open, String god) {
    vl.execSQL("SELECT sklad.cskl, sklad.nazskl, stanje.kol, stanje.vc "+
      "FROM sklad,stanje WHERE sklad.cskl = stanje.cskl AND stanje.cart = " + cart +
      " and stanje.god='"+god+"' ORDER BY stanje.cskl");
    vl.RezSet.setColumns(new Column[] {
      (Column) dm.getSklad().getColumn("CSKL").clone(),
      (Column) dm.getSklad().getColumn("NAZSKL").clone(),
      (Column) dm.getStanje().getColumn("KOL").clone(),
      (Column) dm.getStanje().getColumn("VC").clone(),
    });
    if (open) vl.RezSet.open();
    return vl.getDataAndClear();
  }

  /**
   * Vra\u0107a QueryDataSet sa svim artiklima izdanim na temelju nekog
   * radnog naloga. Artikli stavki samog radnog naloga se ne uzimaju
   * u obzir.<p>
   * @param cradnal šifra radnog naloga u kojem se traže artikli.
   * @return QueryDataSet.
   */

  public static QueryDataSet getArtikliNaloga(String cradnal) {
    return getArtiklNaloga(0, cradnal);
  }

  /**
   * Vra\u0107a QueryDataSet sa odre\u0111enim artiklom izdanim na temelju nekog
   * radnog naloga.<p>
   * @param cart šifra traženog artikla.
   * @param cradnal šifra radnog naloga u kojem se traži artikl.
   * @return QueryDataSet.
   */

  public static QueryDataSet getArtiklNaloga(int cart, String cradnal) {
    vl.execSQL("SELECT * FROM stdoki WHERE cradnal = '"+cradnal+"' "+
       " AND vrdok != 'RNL'" + ((cart == 0) ? "" : " AND cart = "+cart));
    vl.RezSet.open();
    return vl.getDataAndClear();
  }

  public static QueryDataSet getStavkeShkonta(String vrdok, String cskl, String csklul) {
    vl.execSQL("SELECT * from shkonta WHERE vrdok = '" + vrdok +
       "' and cskl = '" + cskl + "' and csklul = '" + csklul + "'");
    vl.RezSet.open();
    return vl.getDataAndClear();
  }

  public static void copyAllShemeKonta(String cskl, String csklul) {
    vl.runSQL("INSERT INTO shkonta SELECT lokk, aktiv, vrdok, '" + cskl + "' as cskl, "+
       "stavka, opis, polje, brojkonta, karakteristika, app, '"+csklul+"' as csklul, cknjige, ckolone FROM defshkonta "+
       "WHERE NOT EXISTS (SELECT * FROM shkonta WHERE shkonta.vrdok = defshkonta.vrdok AND "+
       "shkonta.stavka = defshkonta.stavka AND shkonta.cskl = '" + cskl + "' AND shkonta.csklul = '" + csklul + "')");
  }

  public static void copyShemeKonta(String vrdok, String cskl, String csklul) {
    vl.runSQL("INSERT INTO shkonta SELECT lokk, aktiv, vrdok, '" + cskl + "' as cskl, "+
       "stavka, opis, polje, brojkonta, karakteristika, app, '"+ csklul +
       "' as csklul, cknjige, ckolone, sqlcondition FROM defshkonta WHERE vrdok = '" + vrdok + "' "+
       "AND NOT EXISTS (SELECT * FROM shkonta WHERE shkonta.vrdok = defshkonta.vrdok AND "+
       "shkonta.stavka = defshkonta.stavka AND shkonta.cskl = '" + cskl +
       "' AND shkonta.csklul = '" + csklul + "')");
  }
  
  public static QueryDataSet prepareHorizontalDataSet(short cvrsubj) {
    vl.execSQL("SELECT cznac, znacopis, znactip FROM RN_znacajke WHERE cvrsubj = " + cvrsubj);
    QueryDataSet znaclist = vl.getDataAndClear();
    znaclist.open();
    Column[] cols = new Column[3 + znaclist.rowCount()];
    znaclist.first();
    for (int i = 0; i < znaclist.rowCount(); i++) {
      Column col = new Column();
      String tip = znaclist.getString("ZNACTIP");
      col.setCaption(znaclist.getString("Znacopis"));
      col.setColumnName("ZNAC" + znaclist.getShort("CZNAC"));
      col.setPrecision(50);
      col.setRowId(false);
      if (tip.equals("S")) {
        col.setDataType(Variant.STRING);
        col.setWidth(15);
      } else if (tip.equals("I")) {
        col.setDataType(Variant.INT);
        col.setWidth(6);
      } else if (tip.equals("2")) {
        col.setDataType(Variant.BIGDECIMAL);
        col.setScale(2);
        col.setDisplayMask("###,###,##0.00");
        col.setWidth(9);
      } else if (tip.equals("3")) {
        col.setDataType(Variant.BIGDECIMAL);
        col.setScale(3);
        col.setDisplayMask("###,###,##0.000");
        col.setWidth(9);
      } else {
        col.setDataType(Variant.TIMESTAMP);
        col.setDisplayMask("dd-MM-yyyy");
        col.setWidth(9);
      }
      cols[3 + i] = col;
      znaclist.next();
    }
    cols[0] = (Column) dm.getRN_subjekt().getColumn("CVRSUBJ").clone();
    cols[0].setRowId(true);
    cols[1] = (Column) dm.getRN_subjekt().getColumn("CSUBRN").clone();
    cols[1].setRowId(true);
    cols[1].setCaption(Aut.getAut().getSifraTekst(cvrsubj));
    cols[2] = (Column) dm.getRN_subjekt().getColumn("BROJ").clone();
    cols[2].setCaption(Aut.getAut().getSBTekst(cvrsubj));
    cols[2].setRowId(false);
    QueryDataSet sub = new QueryDataSet() {
      public boolean refreshSupported() {
        return false;
      }
    };
    sub.setColumns(cols);
    sub.open();
    sub.setTableName("RN_subjekt"+cvrsubj);
    return sub;
  }
  
  public static void updateHorizontalDataSet(DataSet sub, String csubrn, short cvrsubj) {
    vl.execSQL("SELECT RN_subjekt.*, RN_znacsub.cznac, RN_znacsub.vriznac "+
        "FROM RN_subjekt, RN_znacsub WHERE RN_subjekt.csubrn=RN_znacsub.csubrn "+
        "AND RN_subjekt.cvrsubj = RN_znacsub.cvrsubj AND RN_subjekt.cvrsubj=" + 
        cvrsubj + " AND RN_subjekt.csubrn = '" + csubrn + "' AND "+ 
        "(RN_znacsub.cradnal='' OR RN_znacsub.cradnal IS NULL)");
    fillHorizontalDataSet(sub, vl.getDataAndClear(), true);
  }
  
  public static void updateZnac(DataSet sub, DataSet ds) {
    try {
      String cname = "ZNAC" + ds.getShort("CZNAC");
      String vri = ds.getString("VRIZNAC");
      if (vri.length() == 0) {
        sub.setUnassignedNull(cname);
        return;
      }
      
      int ctip = sub.getColumn(cname).getDataType();
      if (ctip == Variant.STRING)
        sub.setString(cname, vri);
      else if (ctip == Variant.INT)
        sub.setInt(cname, Integer.parseInt(vri));
      else if (ctip == Variant.BIGDECIMAL)
        sub.setBigDecimal(cname, new BigDecimal(vri));
      else if (ctip == Variant.TIMESTAMP)
        sub.setTimestamp(cname, Timestamp.valueOf(vri));
    } catch (Exception e) {
      if (++errorNum <= 10) System.out.println(e);
      else if ((errorNum - 10) % 101 == 0) {
        System.out.println("...100 errors...");
        System.out.println(e);
      } else if ((errorNum - 10) % 101 == 1) {
        System.out.println("...more errors...");
      }
    }
  }
  
  private static int errorNum;
  public static void fillHorizontalDataSet(DataSet sub, DataSet ds, boolean update) {
    String lastc = "";
    errorNum = 0;
    ds.open();
    for (ds.first(); ds.inBounds(); ds.next()) {
      if (!lastc.equals(ds.getString("CSUBRN"))) {
        lastc = ds.getString("CSUBRN");
        if (!update || !lookupData.getlookupData().raLocate(sub, "CSUBRN", lastc))
          sub.insertRow(false);
        sub.setShort("CVRSUBJ", ds.getShort("CVRSUBJ"));
        sub.setString("CSUBRN", lastc);
        sub.setString("BROJ", ds.getString("BROJ"));
      }
      updateZnac(sub, ds);
    }
    sub.post();
  }
  
  public static QueryDataSet getSubjektiDetail(short cvrsubj) {
    QueryDataSet sub = prepareHorizontalDataSet(cvrsubj);
    
    vl.execSQL("SELECT RN_subjekt.*, RN_znacsub.cznac, RN_znacsub.vriznac "+
        "FROM RN_subjekt, RN_znacsub WHERE RN_subjekt.csubrn=RN_znacsub.csubrn "+
        "AND RN_subjekt.cvrsubj = RN_znacsub.cvrsubj AND RN_subjekt.cvrsubj=" + 
        cvrsubj + " AND (RN_znacsub.cradnal='' OR RN_znacsub.cradnal IS NULL) " + 
        "ORDER BY RN_subjekt.csubrn");
    fillHorizontalDataSet(sub, vl.getDataAndClear(), false);
    return sub;
  }
  
  public static QueryDataSet getSubjektiDetail(short cvrsubj, String sublist) {
    QueryDataSet sub = prepareHorizontalDataSet(cvrsubj);

    vl.execSQL("SELECT RN_subjekt.*, RN_znacsub.cznac, RN_znacsub.vriznac "+
        "FROM RN_subjekt, RN_znacsub WHERE RN_subjekt.csubrn=RN_znacsub.csubrn "+
        "AND RN_subjekt.cvrsubj = RN_znacsub.cvrsubj AND RN_subjekt.cvrsubj=" + 
        cvrsubj + " AND (RN_znacsub.cradnal='' OR RN_znacsub.cradnal IS NULL) " + 
        " AND RN_subjekt.csubrn IN (" + sublist + ") ORDER BY RN_subjekt.csubrn");
    fillHorizontalDataSet(sub, vl.getDataAndClear(), false);
    return sub;
  }
}
