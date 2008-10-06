/****license*****************************************************************
**   file: Aut.java
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
import hr.restart.baza.raDataSet;
import hr.restart.sisfun.Asql;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raMasterDetail;
import hr.restart.util.sysoutTEST;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: Aut</p>
 * <p>Description: Utility klasa</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: REST-ART</p>
 * <p>Klasa sa raznim utility metodama korisnima na raznim mjestima u robnom knjigovodstvu.</p>
 * @author ab.f
 * @version 1.0
 */

public class Aut {
  sysoutTEST sys = new sysoutTEST(false);
//  TimeTrack tt = new TimeTrack(false);
  private static Aut aut;
  private dM dm = dM.getDataModule();
  private Valid vl = Valid.getValid();
  private lookupData ld = lookupData.getlookupData();
  private String userskl = "";
  private String knjigod = "";

  private QueryDataSet qdsFakeArtikl;
//  private QueryDataSet qdsVrsub;
  private QueryDataSet qdsNorme;
  private QueryDataSet qdsUsluge;
  private QueryDataSet qdsArtExpander;
  private StorageDataSet expandRow;

  private Aut() {
  }


  /**
   * Stati\u010Dki getter klase.<p>
   * @return static instancu klase.
   */
  public static Aut getAut() {
    if (aut == null) {
      aut = new Aut();
      aut.setArtExpander();
    }
    return aut;
  }

  private void setArtExpander() {
    qdsArtExpander = new QueryDataSet();
    qdsArtExpander.setColumns(new Column[] {
      (Column) dm.getArtikli().getColumn("CART").clone(),
      (Column) dm.getArtikli().getColumn("CART1").clone(),
      (Column) dm.getArtikli().getColumn("BC").clone(),
      (Column) dm.getArtikli().getColumn("NAZART").clone(),
      (Column) dm.getArtikli().getColumn("JM").clone(),
      (Column) dm.getStdoki().getColumn("KOL").clone(),
      dM.createStringColumn("BRANCH", "Grana normativa", 120)
    });
    qdsArtExpander.open();
    expandRow = new StorageDataSet();
    expandRow.setColumns(dm.getSortedNorme().cloneColumns());
    expandRow.open();
  }

  /**
   * Vra\u0107a <code>QueryDataSet</code> s kolonama CART, CART1, BC, NAZART i JM, koji služi
   * da se podmetne rapancartu ako treba raditi sa tablicom koja nema te kolone.<p>
   * @return <code>QueryDataSet</code> s kolonama CART, CART1, BC, NAZART i JM
   */

  public QueryDataSet getFakeArtikl() {
    if (qdsFakeArtikl == null) {
      qdsFakeArtikl = new QueryDataSet();
      qdsFakeArtikl.setColumns(new Column[] {
        (Column) dm.getArtikli().getColumn("CART").clone(),
        (Column) dm.getArtikli().getColumn("CART1").clone(),
        (Column) dm.getArtikli().getColumn("BC").clone(),
        (Column) dm.getArtikli().getColumn("NAZART").clone(),
        (Column) dm.getArtikli().getColumn("JM").clone()
      });
    };
    qdsFakeArtikl.open();
    return qdsFakeArtikl;
  }

  /**
   * Vra\u0107a <code>QueryDataSet</code> sa šifrom i nazivom vrste subjekta (za radne naloge)
   * grabanog iz tablice atributa (RN_znacajke).<p>
   * @return <code>QueryDataSet</code> s kolonama CVRSUBJ i NAZVRSUBJ.
   */

/*  public QueryDataSet getVrsub() {
    if (qdsVrsub == null) {
      qdsVrsub = new raDataSet();
      qdsVrsub.setQuery(new QueryDescriptor(dM.getDataModule().getDatabase1(),
        "SELECT cvrsubj, MAX(nazvrsubj) as nazvrsubj FROM RN_znacajke GROUP BY cvrsubj"));
      qdsVrsub.setColumns(new Column[] {
        (Column) dm.getRN_znacajke().getColumn("CVRSUBJ").clone(),
        (Column) dm.getRN_znacajke().getColumn("NAZVRSUBJ").clone()
      });
      qdsVrsub.setTableName("Vrsubj");
      ((raDataSet) qdsVrsub).addDependency("RN_znacajke");
    } else qdsVrsub.close();
    qdsVrsub.open();
    return qdsVrsub;
  } */

  /**
   * Vra\u0107a <code>QueryDataSet</code> sa šifrom i nazivom skupine artikala, zgrabanog
   * iz tablice skupart.<p>
   * @return <code>QueryDataSet</code> s kolonama CSKUPART i NAZSKUPART.
   */

  public QueryDataSet getNorme() {
    if (qdsNorme == null) {
      qdsNorme = new raDataSet();
      Aus.setFilter(qdsNorme, "SELECT cskupart, MAX(nazskupart) as nazskupart FROM skupart GROUP BY cskupart");      
      qdsNorme.setColumns(new Column[] {
        (Column) dm.getSkupart().getColumn("CSKUPART").clone(),
        (Column) dm.getSkupart().getColumn("NAZSKUPART").clone()
      });
      qdsNorme.setTableName("Skup_art");
      /*((raDataSet) qdsNorme).addDependency("Skupart");*/
    } else qdsNorme.close();
    qdsNorme.open();
    //System.out.println(qdsNorme);
    return qdsNorme;
  }

  /**
   * Vra\u0107a <code>QueryDataSet</code>, podskup tablice Artikli, u kojem su samo artikli
   * tipa U (usluga) i P (proizvod).<p>
   * @return <code>QueryDataSet</code>, podskup tablice artikala.
   * @deprecated ne koristiti.
   */

  public QueryDataSet getUsluge() {
    if (qdsUsluge == null) {
      qdsUsluge = new QueryDataSet();
      Aus.setFilter(qdsUsluge, "SELECT * FROM artikli WHERE vrart = 'U' OR vrart = 'P'");
      qdsUsluge.setColumns(dm.getArtikli().cloneColumns());
      qdsUsluge.setTableName("Usluge");
    } else qdsUsluge.close();
    qdsUsluge.open();
    return qdsUsluge;
  }

  private Object getCARTdependable_impl(Object CARTobj, Object CART1obj, Object BCobj) {
    String cart = getIndiCART();
    if ("CART".equals(cart))
      return CARTobj;
    else if ("CART1".equals(cart))
      return CART1obj;
    else if ("BC".equals(cart))
      return BCobj;
    else return CARTobj;
  }

  /**
   * Vra\u0107a jedan od ulaznih parametara, ovisno o parametru indiCart (šifra
   * artikla s kojom se radi).<p>
   * @param CARTobj Objekt koji se vra\u0107a ako je indiCart postavljen na CART.
   * @param CART1obj Objekt koji se vra\u0107a ako je indiCart postavljen na CART1.
   * @param BCobj Objekt koji se vra\u0107a ako je indiCart postavljen na BC.
   * @return Jedan od gornja tri objekta.
   */

  public Object getCARTdependable(Object CARTobj, Object CART1obj, Object BCobj) {
    return getCARTdependable_impl(CARTobj, CART1obj, BCobj);
  }

  /**
   * Vra\u0107a jedan od ulaznih parametara, ovisno o parametru indiCart (šifra
   * artikla s kojom se radi).<p>
   * @param CARTstr <code>String</code> koji se vra\u0107a ako je indiCart postavljen na CART.
   * @param CART1str <code>String</code> koji se vra\u0107a ako je indiCart postavljen na CART1.
   * @param BCstr <code>String</code> koji se vra\u0107a ako je indiCart postavljen na BC.
   * @return Jedan od gornja tri <code>String</code>-a.
   */

  public String getCARTdependable(String CARTstr, String CART1str, String BCstr) {
    return (String) getCARTdependable_impl(CARTstr, CART1str, BCstr);
  }

  /**
   * Vra\u0107a jedan od ulaznih parametara, ovisno o parametru indiCart (šifra
   * artikla s kojom se radi).<p>
   * @param CARTint Broj koji se vra\u0107a ako je indiCart postavljen na CART.
   * @param CART1int Broj koji se vra\u0107a ako je indiCart postavljen na CART1.
   * @param BCint Broj koji se vra\u0107a ako je indiCart postavljen na BC.
   * @return Jedan od gornja tri broja.
   */
  public int getCARTdependable(int CARTint, int CART1int, int BCint) {
    String cart = getIndiCART();
    if ("CART".equals(cart))
      return CARTint;
    else if ("CART1".equals(cart))
      return CART1int;
    else if ("BC".equals(cart))
      return BCint;
    else return CARTint;
  }

  private Object getIzlazCARTdep_impl(Object CARTobj, Object CART1obj, Object BCobj) {
    String cart = getIzlazCART();
    if ("CART".equals(cart))
      return CARTobj;
    else if ("CART1".equals(cart))
      return CART1obj;
    else if ("BC".equals(cart))
      return BCobj;
    else return CARTobj;
  }

  public Object getIzlazCARTdep(Object CARTobj, Object CART1obj, Object BCobj) {
    return getIzlazCARTdep_impl(CARTobj, CART1obj, BCobj);
  }

  public String getIzlazCARTdep(String CARTstr, String CART1str, String BCstr) {
    return (String) getIzlazCARTdep_impl(CARTstr, CART1str, BCstr);
  }

  public int getIzlazCARTwidth() {
    int cartwid = Aus.getNumber(frmParam.getParam("robno", "izlazCartSir", "38",
        "Širina šifre artikla na izlaznim dokumentima (u pikselima)"));
    int cart1wid = Aus.getNumber(frmParam.getParam("robno", "izlazCart1Sir", "70",
        "Širina oznake artikla na izlaznim dokumentima (u pikselima)"));
    int bcwid = Aus.getNumber(frmParam.getParam("robno", "izlazBcSir", "70",
        "Širina bar-koda artikla na izlaznim dokumentima (u pikselima)"));
    if (cartwid <= 0) cartwid = 38;
    if (cart1wid <= 0) cart1wid = 70;
    if (bcwid <= 0) bcwid = 70;
    return getIzlazCARTdep(cartwid * 20, cart1wid * 20, bcwid * 20);
  }
  
  public int getIzlazCARTdep(int CARTint, int CART1int, int BCint) {
    String cart = getIzlazCART();
    if ("CART".equals(cart))
      return CARTint;
    else if ("CART1".equals(cart))
      return CART1int;
    else if ("BC".equals(cart))
      return BCint;
    else return CARTint;
  }

  public String getIzlazCART() {
    return hr.restart.sisfun.frmParam.getParam("robno", "izlazCart", "BC",
        "Šifra artikala na izlaznim dokumentima (CART, CART1, BC)");
  }

  public String getIndiCART() {
    return hr.restart.sisfun.frmParam.getParam("robno", "indiCart", "CART1",
        "Indikator naèina unosa artikla (CART, CART1 ili BC)");
  }

  private String deptString(DataSet ds, String cart){
    if ("CART".equals(cart))
      return String.valueOf(ds.getInt("CART"));
    else if ("CART1".equals(cart))
      return ds.getString("CART1");
    else if ("BC".equals(cart)) {
      return ds.getString("BC");
    }
    else { System.out.println("oðe nije dobro");
      return String.valueOf(ds.getInt("CART"));
    }
  }

  public String getIzlazCARTdep(DataSet ds) {
    return deptString(ds, getIzlazCART());
  }

  public String getCARTdependable(DataSet ds) {
    return deptString(ds, getIndiCART());
  }

  public String getCradnal(String corg, String god, int brdok) {
    return "RNL-"+corg+"/"+god+"-"+brdok;
  }

  public String getCradnal(String corg, String god, String brdok) {
    return "RNL-"+corg+"/"+god+"-"+brdok;
  }

  /**
   * Baca standardni dialog s greškom ako se pokušava obrisati zaglavlje koje još
   * ima stavki. (Bilo bi dobro da se to nalazi u <code>raMasterDetail</code> frameworku.)
   * Zvati iz metode <code>DeleteCheckMaster()</code>.<p>
   * @param frame raMasterDetail koji poziva ovu metodu.
   * @return true ako ne postoji nijedna stavka, false ako ima.
   */

  public boolean standardDeleteCheckMaster(raMasterDetail frame) {
    frame.refilterDetailSet();
    if (frame.getDetailSet().rowCount() > 0) {
      JOptionPane.showMessageDialog(frame.getJPanelMaster(),
         "Nije mogu\u0107e brisati zaglavlje dok se ne pobrišu stavke!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    } else
      return true;
  }

  /**
   * Metoda koja generira grešku u <code>rapancart</code>-u. Ovisno o parametru indiCart,
   * zacrveni jedno od polja <code>rapancart</code>-a i postavi fokus na njega.<p>
   * @param rpc <code>rapancart</code> u kojem se generira greška.
   * @param msg Poruka koja \u0107e se ispisati u statusnoj liniji.
   */

  public void handleRpcErr(rapancart rpc, String msg) {
    final JlrNavField errf;
    if (rpc.getParam().equals("CART")) errf = rpc.jrfCART;
    else if (rpc.getParam().equals("CART1")) errf = rpc.jrfCART1;
    else errf = rpc.jrfBC;
    rpc.EnabDisab(true);
    //errf.setText("");
    errf.setErrText(msg);
    errf.emptyTextFields();
    errf.this_ExceptionHandling(new Exception());
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        errf.setErrText(null);
      }
    });
  }

  /**
   * Standardna metoda za rekalkulaciju rednih brojeva stavki nekog dokumenta
   * nakon brisanja jedne stavke. Najbolje zvati iz metode <code>AfterDeleteDetail()</code>,
   * nakon što se u metode <code>DeleteCheckDetail()</code> zapamti redni broj stavke
   * koja se briše.<p>
   * @param frame raMasterDetail koji poziva ovu metodu.
   * @param col ime kolone rednog broja.
   * @param oldRbr Redni broj obrisane stavke.
   * @deprecated Koristi se Valid.getValid().recountDataSet().
   */

  public void recalculateRbr(raMasterDetail frame, String col, short oldRbr) {
//    short thisRbr;
//    frame.getDetailSet().first();
//    while (frame.getDetailSet().inBounds()) {
//      if ((thisRbr = frame.getDetailSet().getShort(col)) > oldRbr)
//        frame.getDetailSet().setShort(col, (short) (thisRbr - 1));
//      frame.getDetailSet().next();
//    }
//    frame.getDetailSet().saveChanges();
    Valid.getValid().recountDataSet(frame.raDetail, col, oldRbr, false);
  }

  private String[] normName = new String[] {"CARTNOR"};
  private String[] normVal = new String[] {""};
  private VarStr branch = new VarStr();
  /**
   * Metoda vrši ekspanziju nekog DataSet-a koji ima polja CART, CART1, BC, NAZART, JM i KOL
   * tako da za svaki artikl koji je normiran dohva\u0107a sve artikle koje mu pripadaju
   * (rekurzivno). Rezultiraju\u0107i DataSet sadržavat \u0107e sve krajnje artikle (bez
   * normiranih) uz adekvatno izra\u010Dunate koli\u010Dine.<p>
   * @param ds DataSet sa odre\u0111enim brojem slogova (artikala) koji se ekspandaju ako
   * su normirani.
   * @param total true za rekurzivnu ekspnaziju.
   * @return QueryDataSet sa krajnjim rezultatom ekspanzije.
   */

  public QueryDataSet expandArts(DataSet ds, boolean total) {
    BigDecimal kol = new BigDecimal(1);
    qdsArtExpander.empty();
    branch.clear();
    dm.getSortedNorme().open();
    ds.open();
//    System.out.println("expandArts "+kol);
    for (ds.first(); ds.inBounds(); ds.next())
      expandOne(ds, kol, total);
    qdsArtExpander.post();
    qdsArtExpander.first();
    return qdsArtExpander;
  }

  /**
   * Metoda sli\u010Dna metodi expandArts, vrši ekspanziju samo jednog artikla.<p>
   * @param cart Šifra artikla koji se ekspanda (CART).
   * @param kol Koli\u010Dina navedenog artikla.
   * @param total true za rekurzivnu ekspnaziju.
   * @return QueryDataSet sa krajnjim rezultatom ekspanzije.
   */

  public QueryDataSet expandArt(int cart, BigDecimal kol, boolean total) {
    qdsArtExpander.empty();
    branch.clear();
    dm.getSortedNorme().open();
    normVal[0] = String.valueOf(cart);
    if (lookupData.getlookupData().raLocate(dm.getSortedNorme(), normName, normVal))
      expandNorm(cart, kol, total);
    qdsArtExpander.post();
    qdsArtExpander.first();
    return qdsArtExpander;
  }

  public QueryDataSet expandArt(DataSet ds, boolean total) {
    qdsArtExpander.empty();
    branch.clear();
    dm.getSortedNorme().open();
    expandOne(ds, new BigDecimal(1), total);
    qdsArtExpander.post();
    qdsArtExpander.first();
    return qdsArtExpander;
  }

  private void expandOne(DataSet ds, BigDecimal kol, boolean total) {
    int cart = ds.getInt("CART");
    normVal[0] = String.valueOf(cart);
    if (!lookupData.getlookupData().raLocate(dm.getSortedNorme(), normName, normVal))
      insertOne(ds, kol);
    else
      expandNorm(cart, ds.getBigDecimal("KOL").multiply(kol), total);
  }

  private void expandNorm(int cart, BigDecimal kol, boolean total) {
    branch.append(cart).append(':');
    for (DataSet sn = dm.getSortedNorme(); sn.inBounds() && sn.getInt("CARTNOR") == cart; sn.next())
      if (!total) insertOne(sn, kol);
      else {
        int current = sn.getRow();
        dM.copyColumns(sn, expandRow);
        expandOne(expandRow, kol, true);
        sn.goToRow(current);
      }
    branch.truncate(branch.lastIndexOf(':', branch.length() - 1) + 1);
  }

  private void insertOne(DataSet ds, BigDecimal kol) {
    qdsArtExpander.insertRow(false);
    copyArtFields(qdsArtExpander, ds);
    qdsArtExpander.setString("BRANCH", branch.toString().
                   concat(String.valueOf(qdsArtExpander.getInt("CART"))));
    qdsArtExpander.setBigDecimal("KOL", ds.getBigDecimal("KOL").multiply(kol).setScale(3, BigDecimal.ROUND_HALF_UP));
  }

  private String[] searchNames = new String[] {"CRADNAL", "CART"};
  private String[] searchValues = new String[] {"", ""};

  /**
   * <p>Provjerava ima li u tablici stdoki stavka odre\u0111enog artikla vezanog uz odre\u0111eni
   * radni nalog. Koristi se za provjeru knjiženja neke stavke radnog naloga.</p>
   * <p>NAPOMENA: Metoda koristi <code>vl.execSQL()</code>, što zna\u010Di da se ne smije
   * pozivati npr. unutar petlje koja browsa po vl.RezSet-u. Ako je to potrebno,
   * može se uraditi nešto kao</p><p>
   * <code>QueryDataSet tmp = vl.RezSet;</code></p><p>
   * pa dalje raditi sa <code>tmp</code>.</p>
   * @param cart Šifra CART traženog artikla.
   * @param cradnal Šifra CRADNAL radnog naloga.
   * @return true ako u tablici stdoki postoji doti\u010Dni artikl za navedeni radni nalog.
   */

  public boolean checkStavkaRNL(int cart, String cradnal) {
    boolean alreadyThere = false;
    QueryDataSet st = Asql.getArtikliNaloga(cradnal);
    QueryDataSet norme = expandArt(cart, _Main.nul, true);
    searchValues[0] = cradnal;
    for (norme.first(); norme.inBounds(); norme.next()) {
      searchValues[1] = "" + norme.getInt("CART");
      if (lookupData.getlookupData().raLocate(st, searchNames, searchValues)) {
        alreadyThere = true;
        break;
      }
    }
    return alreadyThere;
  }

  /**
   * <p>Provjerava ima li u tablici stdoki stavka odre\u0111enog artikla vezanog uz odre\u0111eni
   * radni nalog. Koristi se za provjeru knjiženja neke stavke radnog naloga. Razlikuje
   * se od prethodne metode po tome što ne provjerava je li artikl normiran i konzekventno
   * ne radi rekurzivnu provjeru artikala normativa. Zvati kad je sigurno da artikl nije
   * normiran (recimo ako je artikl dobiven iz ekspanzije).<p>
   * @param cart Šifra CART traženog artikla.
   * @param cradnal Šifra CRADNAL radnog naloga.
   * @return true ako u tablici stdoki postoji doti\u010Dni artikl za navedeni radni nalog.
   */

  public boolean checkStavkaRM(int cart, String cradnal) {
    return (Asql.getArtiklNaloga(cart, cradnal).rowCount() > 0);
  }


  private String[] artName = new String[] {"CART"};
  private String[] artValue = new String[] {""};

  /**
   * Provjerava je li artikl odre\u0111enog tipa (usluga, materijal, roba, proizvod).<p>
   * @param cart CART artikla koji se provjerava.
   * @param types <code>String</code> sa popisom tipova. (i.e. "RM" kao roba ili materijal).
   * @return true ako je artikl jednog od tipova navedenih u parametru <code>types</code>.
   * @deprecated koristi raVrart klasu
   */

  public boolean artTipa(int cart, String types) {
    artValue[0] = String.valueOf(cart);
    if (!lookupData.getlookupData().raLocate(dm.getArtikli(),artName,artValue)) return false;
    String vrart = dm.getArtikli().getString("VRART");
    return (types.indexOf(vrart) >= 0);
  }

  /**
   * Kopira vrijednosti kolona CART, CART1, BC, NAZART i JM iz jednog DataSeta u drugi.
   * (Ne vrši nikakve provjere).<p>
   * @param dest Odredišni DataSet.
   * @param source Izvorni DataSet.
   */

  public void copyArtFields(DataSet dest, DataSet source) {
    dest.setInt("CART", source.getInt("CART"));
    dest.setString("CART1", source.getString("CART1"));
    dest.setString("BC", source.getString("BC"));
    dest.setString("NAZART", source.getString("NAZART"));
    dest.setString("JM", source.getString("JM"));
  }

  /*public String[] getRow(QueryDataSet ds, String[] colNames, String[] colValues) {
    boolean[] inverse = new boolean[colNames.length];
    for (int i = 0; i < inverse.length; i++) inverse[i] = false;
    return getRow(ds, colNames, colValues, inverse);
  }

  public String[] getRow(QueryDataSet ds, String[] colNames, String[] colValues, boolean[] inverse) {
    String query = vl.getNoWhereQuery(ds);
    String prep = " WHERE"
    String conds = "";
    for (int i = 0; i < colNames.length; i++) {
      if (!colValues[i].equals("")) {
        conds = conds + prep + (inverse[i] ? " NOT " : "") +
      }
    }

  } */

  /**
   * Popunjava neka polja skladišne strane ulaznih i izlaznih dokumenata.
   * Prije poziva potrebno je postaviti vrijednosti kolona KOL, NC, MC, VC i ZC.
   * Metoda \u0107e iz tih vrijednosti izra\u010Dunati INAB, IMAR, IBP, IPOR, ISP te
   * IRAZ odn. IZAD.<p>
   * @param ds DataSet s kojim se radi (stdoki ili stdoku)
   * @param ui Oznaka vrste dokumenta. true za ulazne, false za izlazne.
   */
  public void calcSkladFigures(DataSet ds, boolean ui) {
    ds.setBigDecimal("INAB", ds.getBigDecimal("NC").multiply(ds.getBigDecimal("KOL")).setScale(2, BigDecimal.ROUND_HALF_UP));
    ds.setBigDecimal("IBP", ds.getBigDecimal("VC").multiply(ds.getBigDecimal("KOL")).setScale(2, BigDecimal.ROUND_HALF_UP));
    ds.setBigDecimal("ISP", ds.getBigDecimal("MC").multiply(ds.getBigDecimal("KOL")).setScale(2, BigDecimal.ROUND_HALF_UP));
    ds.setBigDecimal(ui ? "IZAD" : "IRAZ", ds.getBigDecimal("ZC").multiply(ds.getBigDecimal("KOL")).setScale(2, BigDecimal.ROUND_HALF_UP));
    String vrzal = "X";
    if (lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL", ds.getString("CSKL")))
      vrzal = dm.getSklad().getString("VRZAL");
    System.out.println("calc sklad figures, vrzal="+vrzal);
    if (vrzal.equalsIgnoreCase("N")) ds.setBigDecimal("IMAR", _Main.nul);
    else ds.setBigDecimal("IMAR", ds.getBigDecimal("IBP").subtract(ds.getBigDecimal("INAB")).setScale(2, BigDecimal.ROUND_HALF_UP));
    if (vrzal.equalsIgnoreCase("N") || vrzal.equalsIgnoreCase("V")) ds.setBigDecimal("IPOR", _Main.nul);
    else ds.setBigDecimal("IPOR", ds.getBigDecimal("ISP").subtract(ds.getBigDecimal("IBP")).setScale(2, BigDecimal.ROUND_HALF_UP));
  }

  /**
   * Vra\u0107a default skladište za trenuta\u010Dno prijavljenog korisnika.
   * @return Default skladište, ili prazan string ako default skladište nije
   * definirano za aktivnog korisnika.
   */
  public String getDefaultSklad() {
    if (!userskl.equals("")) return userskl;
    if (!lookupData.getlookupData().raLocate(dm.getUsersklad(),
       "CUSER", raUser.getInstance().getUser()))
      return "";
    userskl = dm.getUsersklad().getString("CSKL");
    return userskl;
  }

  /**
   * Vra\u0107a godinu za aktivno knjigovodstvo.
   * @return Godinu ili prazan string ako nije definirana.
   */
  public String getKnjigodRobno() {
//    if (!knjigod.equals("")) return knjigod;
    String god = Valid.getValid().getKnjigYear("robno");
    if (god.equals(dm.getKnjigod().getString("GOD")) &&
        "D".equals(dm.getKnjigod().getString("STATRADA")))
      return Integer.toString(Aus.getNumber(god) + 1);
    return god;
//    return knjigod;
  }

  public boolean isWrongKnjigYear(raMasterDetail md) {
    return isWrongKnjigYear(md, false);
  }

  public boolean isWrongKnjigYear(raMasterDetail md, boolean addAction) {
    String datYear = null;
    if (md.getPreSelect() instanceof jpPreselectDoc)
      datYear = vl.findYear(((jpPreselectDoc) md.getPreSelect()).getDefDate());
    else if (md.getPreSelect() instanceof jpSelectMeskla ||
             md.getPreSelect() instanceof jpSelectRevers)
      datYear = vl.findYear(md.getPreSelect().getSelRow().getTimestamp("DATDOK-to"));
    if (datYear != null) {
      String kPrevYear, kThisYear = Valid.getValid().getKnjigYear("robno");
      if (dm.getKnjigod().getString("STATRADA").equalsIgnoreCase("D"))
        kPrevYear = String.valueOf(Aus.getNumber(kThisYear) + 1); // bilo (ili puls) -1 ispravio T.V. Ante Ante !!!
      else kPrevYear = kThisYear;
      boolean wrong = !datYear.equals(kThisYear) && !datYear.equals(kPrevYear);
      if (wrong) {
        if (addAction)
          JOptionPane.showMessageDialog(md.raMaster.getWindow(),
            "Ne mogu se dodavati dokumenti u "+datYear+". godinu!!",
            "Poruka", JOptionPane.WARNING_MESSAGE);
        else md.setUserCheckMsg("Dokument iz prošle godine ne može se mijenjati!", false);
      }
      return wrong;
    } else return false;
  }

 /* public void doTest() {
    String[] artName = new String[] {"CART"};
    String[] artVal = new String[] {""};
    int reps = 100;

    vl.execSQL("SELECT * FROM norme");
    lookupData.getlookupData();

    tt.Start("QUERY");
    for (int i = 1; i < reps; i++) {
      vl.execSQL("SELECT * FROM artikli where cart = " + (i % 15 + 1));
      vl.RezSet.open();
      System.out.print(vl.RezSet.getInt("CART")+" ");
    }
    System.out.println();
    tt.ReStart("LOCATE");
    dm.getArtikli().open();
    for (int i = 1; i < reps * 10; i++) {
      artVal[0] = "" + (i % 15 + 1);
      lookupData.getlookupData().raLocate(dm.getArtikli(), artName, artVal);
      System.out.print(dm.getArtikli().getInt("CART")+" ");
    }
    dm.getArtikli().locate()
    System.out.println();
    tt.Stop();
  } */

  public String getDefaultValue(String vrdok, String key) {
    if (!lookupData.getlookupData().raLocate(dm.getDefvaluedok(),
        new String[] {"VRDOK", "KLJUC"}, new String[] {vrdok, key}))
      return "";
    return dm.getDefvaluedok().getString("DEFVALUE");
  }

  /**
   * Pretvara String u int. Korisno ako se o\u010Dekuje pozitivan broj,
   * jer u slu\u010Daju bilo kakve greške, metoda vra\u0107a 0.
   * @param snum String reprezentacija broja.
   * @return 0 u slu\u010Daju greške, ina\u010De doti\u010Dni broj
   */
  public int getNumber(String snum) {
    try {
      return Integer.parseInt(snum);
    } catch (Exception e) {
      return 0;
    }
  }
  
  public String getSBTekst(short cvrsubj) {
    String sb = null;
    if (ld.raLocate(dm.getRN_vrsub(), "CVRSUBJ", String.valueOf(cvrsubj)))
      sb = dm.getRN_vrsub().getString("nazserbr");
    return sb == null || sb.length() == 0 ? "S/B" : sb;
  }
  
  public String getSifraTekst(short cvrsubj) {
    String sif = null;
    if (ld.raLocate(dm.getRN_vrsub(), "CVRSUBJ", String.valueOf(cvrsubj)))
      sif = dm.getRN_vrsub().getString("nazsif");
    return sif == null || sif.length() == 0 ? "Šifra" : sif;
  }
  
  public Map getPodgrupe(Collection tops) {
    Map allgr = new HashMap();
    DataSet gr = dm.getGrupart();
    gr.open();
    for (gr.first(); gr.inBounds(); gr.next())
      allgr.put(gr.getString("CGRART"), gr.getString("CGRARTPRIP"));
    for (Iterator i = allgr.keySet().iterator(); i.hasNext(); ) {
      String g = (String) i.next();
      String pr = g;
      while (allgr.containsKey(pr) && !tops.contains(pr) 
          && !pr.equals(allgr.get(pr)))
        pr = (String) allgr.get(pr);
      allgr.put(g, pr);
    }
    for (Iterator i = allgr.values().iterator(); i.hasNext(); ) {
      String pr = (String) i.next();
      if (!tops.contains(pr)) i.remove();
    }
    return allgr;
  }
}

/*

//	Ante, prchin ti chempres, eo ode imadesh kako dobit kliko ima dana izmedju dva
//	tajmstempa. Eto, ako imas shto dodat fil fri :))

	public int calculateSatiIzDatuma(Timestamp datod, Timestamp datdo){
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();
    c1.setTime(datod);
    c2.setTime(datdo);
    int brojDanaPuta = 0;
    if (c2.get(c2.YEAR) == c1.get(c1.YEAR))
    	brojDanaPuta = c2.get(c2.DAY_OF_YEAR) - c1.get(c1.DAY_OF_YEAR);
    else {
      Calendar c1a = Calendar.getInstance();
      Calendar c2a = Calendar.getInstance();
      c1a.setTime(ut.getLastDayOfYear(datod));
      c2a.setTime(ut.getFirstDayOfYear(datdo));
      brojDanaPuta = (c2.get(c2.DAY_OF_YEAR) - c2a.get(c2a.DAY_OF_YEAR)) + (c1a.get(c1a.DAY_OF_YEAR) - c1.get(c1.DAY_OF_YEAR)) +1;
    }
    return brojDanaPuta;
	}


*/