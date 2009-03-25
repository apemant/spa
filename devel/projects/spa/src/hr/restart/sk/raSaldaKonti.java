/****license*****************************************************************
**   file: raSaldaKonti.java
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
package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.Gkstavke;
import hr.restart.baza.Konta;
import hr.restart.baza.Pokriveni;
import hr.restart.baza.PokriveniRadni;
import hr.restart.baza.Skstavke;
import hr.restart.baza.Skstavkerad;
import hr.restart.baza.UIstavke;
import hr.restart.baza.dM;
import hr.restart.gk.jpBrojNaloga;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraButton;
import hr.restart.util.Aus;
import hr.restart.util.Int2;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKonta;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Locate;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.ReadWriteRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raSaldaKonti {

  /**
   * Oznaka da je knjizenje uspjelo.
   */
  public static final int OK = 0;
  
  /**
   * Oznaka da nedostaje konto za knjizenje.
   */
  public static final int NO_KONTO = -1;
  
  /**
   * Oznaka da nedostaje valuta na dokumentu koji se knjizi.
   */
  public static final int NO_VALUTA = -2;
  
  /**
   * Oznaka da je dokument istog kljuca vec proknjizen.
   */
  public static final int DUPLICATE_KEY = -3;

  /**
   * Oznaka da je shema pogresna.
   */
  public static final int NO_SHEMA = -4;

  /**
   * Oznaka da dokument nije u balansu.
   */
  public static final int UNBALANCED = -5;

  private static hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  /**
   * BigDecimal nula. 
   */
  public static final BigDecimal n0 = new BigDecimal(0.).setScale(2, BigDecimal.ROUND_HALF_UP);
//  public static HashMap sheme = new HashMap();
  
  private static Map jedvals = new HashMap();
  private static QueryDataSet lastSks;
  private static String domval;
  private static boolean kumInvalid = false;
  static boolean simpleDev = false;
  static boolean directKonto = false;
  static boolean extMatch = false;
  static boolean tecUplata = false;
  static {
    lastSks = Skstavke.getDataModule().getTempSet("1 = 0");
    lastSks.open();
    simpleDev = !frmParam.getParam("sk", "simpleDev", "N", 
        "Pojednostavljeno rukovanje deviznim saldom (D/N)").equalsIgnoreCase("N");
    directKonto = frmParam.getParam("sk", "directKonto", "N", 
        "Unos konta na ulaznim racunima (D/N)", true).equalsIgnoreCase("D");
    extMatch = frmParam.getParam("sk", "extendedMatch", "N", 
        "Pokrivanje po dodatnom broju (D/N)", true).equalsIgnoreCase("D");
    tecUplata = frmParam.getParam("sk", "saldoTecaj", "U", "Saldo teèajnih razlika " +
            "ostaviti na raèunima ili uplatama (R,U)?").equalsIgnoreCase("R");
  }

  private raSaldaKonti() {
    // Prazan konstruktor
  }

  public static boolean isTecUplata() {
    return tecUplata;
  }
  
  public static boolean isSimple() {
    return simpleDev;
  }
  
  public static boolean isDirect() {
    return directKonto;
  }
  
  public static boolean isExtendedMatch() {
    return extMatch;
  }
  
  public static String colPok() {
    return simpleDev ? "POKRIVENO" : "PVPOK";
  }
  
  public static String colSaldo() {
    return simpleDev ? "SALDO" : "PVSALDO";
  }
  
  public static String colSSaldo() {
    return simpleDev ? "SSALDO" : "PVSSALDO";
  }
  
  public static BigDecimal calcTecaj(ReadRow row) {
	 BigDecimal dom = null, ino = null;
	 if (row.hasColumn("PVSSALDO") != null) {
		 dom = row.getBigDecimal("SSALDO");
		 ino = row.getBigDecimal("PVSSALDO");
	 } else if (row.hasColumn("DEVID") != null) {
		 dom = row.getBigDecimal("ID").add(row.getBigDecimal("IP"));
		 ino = row.getBigDecimal("DEVID").add(row.getBigDecimal("DEVIP"));
	 } else if (row.hasColumn("PVID") != null) {
		 dom = row.getBigDecimal("ID").add(row.getBigDecimal("IP"));
		 ino = row.getBigDecimal("PVID").add(row.getBigDecimal("PVIP"));
	 }
	 if (ino != null && ino.signum() != 0) {
	   int dl = dom.unscaledValue().toString().length();
	   int il = ino.unscaledValue().toString().length();
	   return dom.divide(ino, Math.max(dl, il) + 2, BigDecimal.ROUND_HALF_UP);
	 }
	 return row.getBigDecimal("TECAJ").
	   divide(getJedVal(row.getString("OZNVAL")), 8, BigDecimal.ROUND_HALF_UP);
  }
//  public static QueryDataSet getVrsk(String vrdok) {
//    if (sheme.containsKey(vrdok))
//      return (QueryDataSet) sheme.get(vrdok);
//    QueryDataSet vrsk = Vrshemek.getDataModule().getFilteredDataSet(
//        "app = 'sk' AND vrdok = '"+vrdok+"'");
//    sheme.put(vrdok, vrsk);
//    return vrsk;
//  }

 public static String getCorgAndPripCondition(String corg) {
    StorageDataSet corgs = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(corg);
    if (corgs.rowCount() == 0) return "";
    else if (corgs.rowCount() == 1) return " AND corg = '" + corg + "'";
    else return " AND " + Condition.in("corg", corgs); 
    //" AND corg in " + hr.restart.zapod.OrgStr.getOrgStr().getInQuery(corgs);
  }

  public static boolean isDomVal(ReadRow row) {
    return isDomVal(row.getString("OZNVAL"));
  }

  public static boolean isDomVal(String val) {
    if (domval == null)
      domval = hr.restart.zapod.Tecajevi.getDomOZNVAL();
    return val == null || val.length() == 0 || val.equalsIgnoreCase(domval);
  }
  
  public static Condition getValCond() {
    isDomVal("");
    return Condition.emptyString("OZNVAL", false).or(Condition.equal("OZNVAL", domval));
  }
  
  public static BigDecimal findJedVal(ReadRow row) {
    return findJedVal(row.getString("OZNVAL"));
  }
  
  public static BigDecimal findJedVal(String val) {
    if (isDomVal(val)) return Aus.one0;
    BigDecimal ret = (BigDecimal) jedvals.get(val);
    if (ret != null) return ret;
    if (!lookupData.getlookupData().raLocate(dM.getDataModule().getValute(),
        "OZNVAL", val)) {
      System.err.println("nema oznake valute!");
      return null;
    }
    ret = new BigDecimal(dM.getDataModule().getValute().getInt("JEDVAL"));
    jedvals.put(val, ret);
    return ret;
  }

  public static String findGKCSK(ReadRow gks) {
    return gks.getString("CNALOGA")+":"+gks.getInt("RBSRAC");
  }

  public static String findCSK(ReadRow sks) {
    return sks.getString("KNJIG")+"|"+
           sks.getInt("CPAR")+"|"+
           sks.getString("VRDOK")+"|"+
           sks.getString("BROJKONTA")+"|"+
           sks.getString("BROJDOK")+"|"+
           /*
   (sks.hasColumn("REALBROJ") == null ?
           sks.getString("BROJDOK") : sks.getString("REALBROJ"))+"|"+
*/
           sks.getInt("BROJIZV");
  }

/*  public static String getFilter(DataSet ds, boolean matchVrdok) {
    VarStr filter = new VarStr();
    filter.append("knjig = '").append(ds.getString("KNJIG"));
    filter.append("' AND brojkonta = '").append(ds.getString("BROJKONTA"));
    filter.append("' AND brojdok = '").append(ds.getString("BROJDOK"));
    if (matchVrdok)
      filter.append("' AND vrdok = '").append(ds.getString("VRDOK"));
    filter.append("' AND cpar = ").append(ds.getInt("CPAR"));
    return filter.toString();
  }

  private static QueryDataSet getSkstavke(DataSet ds) {
    Skstavke.getDataModule().setFilter(getFilter(ds, true));
    return dM.getDataModule().getSkstavke();
  }

  private static QueryDataSet getSkstavkeMatch(DataSet ds) {
    Skstavke.getDataModule().setFilter(getFilter(ds, false));
    return dM.getDataModule().getSkstavke();
  }

  private static QueryDataSet getUIstavke(DataSet ds) {
    UIstavke.getDataModule().setFilter(getFilter(ds, true));
    return dM.getDataModule().getUIstavke();
  } */

  public static BigDecimal getJedVal(String oznval) {
    if (!isDomVal(oznval) && lookupData.getlookupData().raLocate(
        dM.getDataModule().getValute(), "OZNVAL", oznval))
      return new BigDecimal(dM.getDataModule().getValute().getInt("JEDVAL"));
    return new BigDecimal(1.0);
  }
  
  public static BigDecimal getDokSaldo(DataSet ds) {
    BigDecimal saldo = raSaldaKonti.n0;
    ds.open();
    for (ds.first(); ds.inBounds(); ds.next())
      saldo = saldo.add(ds.getBigDecimal("ID")).subtract(ds.getBigDecimal("IP"));
    return saldo;
  }
  

  public static int knjiziStavku(DataSet ds) {
    if (getDokSaldo(ds).signum() != 0) return UNBALANCED;
    
    lookupData.getlookupData().raLocate(ds, "RBS", "1");
    String konto = ds.getString("BROJKONTA");
    if (konto == null || konto.length() == 0) {
      String vrdok = ds.getString("VRDOK");
      if (vrdok.equalsIgnoreCase("OKK")) vrdok = "IRN";
      else if (vrdok.equalsIgnoreCase("OKD")) vrdok = "URN";
      konto = getKonto(vrdok, ds.getString("CSKL"), String.valueOf(ds.getShort("STAVKA")));
    }
    if (konto == null || konto.length() == 0) {
      System.out.println(ds);
      System.out.println("no konto");
      return NO_KONTO;
    }
    if (isDomVal(ds.getString("OZNVAL"))) {
      ds.setString("OZNVAL", domval);
      ds.setBigDecimal("TECAJ", new BigDecimal(1.0));
    }
    if (!lookupData.getlookupData().raLocate(dM.getDataModule().getValute(),
        "OZNVAL", ds.getString("OZNVAL"))) return NO_VALUTA;
    BigDecimal jedval = getJedVal(ds.getString("OZNVAL"));

    String[] key = {"KNJIG", "CPAR", "VRDOK", "BROJDOK", "BROJKONTA"};
    Condition c = Condition.whereAllEqual(key, ds);
    Skstavke.getDataModule().setFilter(c);
    UIstavke.getDataModule().setFilter(c);
    QueryDataSet sks = dM.getDataModule().getSkstavke();
    QueryDataSet uis = dM.getDataModule().getUIstavke();
    
    String[] skscols = new String[] {"KNJIG", "CPAR", "STAVKA", "CSKL", "VRDOK",
         "BROJDOK", "CORG", "DATPRI", "DATDOK", "DATDOSP", "DATUNOS", "EXTBRDOK", "BROJIZV",
         "CNACPL", "OZNVAL", "TECAJ", "OPIS", "CKNJIGE", "ZIRO" /*, "ORIGBROJ"*/};//u ZIRO je veza sa blagajnom
    String[] uiscols = new String[] {"KNJIG", "CPAR", "STAVKA", "CSKL", "VRDOK",
         "BROJDOK", "RBS", "CORG", "CKOLONE", "DUGPOT"};

    sks.open();
    uis.open();
    if (sks.rowCount() != 0 || uis.rowCount() != 0) {

//      sysoutTEST sys = new sysoutTEST(false);
//      sys.prn(sks);
//      sys.prn(uis);
      System.out.println("identic");
      return DUPLICATE_KEY;
    }

    sks.insertRow(false);
    DataSet.copyTo(skscols, ds, skscols, sks);
    DataSet dummysk = Skstavke.getDataModule().getTempSet("1=0");
    dummysk.open();
    dummysk.insertRow(false);
    DataSet.copyTo(skscols, ds, skscols, dummysk);
    dummysk.post();

    String cknjige = ds.getString("CKNJIGE"), uraira = ds.getString("URAIRA");
//    sks.setBigDecimal("SSALDO", ds.getBigDecimal("SALDO"));
    if (ds.getString("CGKSTAVKE").equalsIgnoreCase("N") ||
        !raKonta.isSaldak(konto)) {
      sks.setString("PVPOK", "X");
      sks.setString("POKRIVENO", "X");
    }
    if (ds.getString("CGKSTAVKE").equalsIgnoreCase("N"))
      sks.setString("CGKSTAVKE", "#");
    sks.setTimestamp("DATUMKNJ", ds.getTimestamp("DATPRI"));
    sks.setString("BROJKONTA", konto);
    String cskstav = findCSK(sks);
    sks.setString("CSKSTAVKE", cskstav);
    /*sks.setBigDecimal("ID", ds.getBigDecimal("PVID"));
    sks.setBigDecimal("IP", ds.getBigDecimal("PVIP"));*/
    sks.setBigDecimal("PVID", ds.getBigDecimal("ID"));
    sks.setBigDecimal("PVIP", ds.getBigDecimal("IP"));
    sks.setBigDecimal("ID", ds.getBigDecimal("ID").
        multiply(sks.getBigDecimal("TECAJ")).divide(jedval, 2, BigDecimal.ROUND_HALF_UP));
      sks.setBigDecimal("IP", ds.getBigDecimal("IP").
        multiply(sks.getBigDecimal("TECAJ")).divide(jedval, 2, BigDecimal.ROUND_HALF_UP));
    sks.setBigDecimal("SALDO", sks.getBigDecimal("IP").add(sks.getBigDecimal("ID")));
    sks.setBigDecimal("PVSALDO", sks.getBigDecimal("PVIP").add(sks.getBigDecimal("PVID")));
    sks.setBigDecimal("SSALDO", sks.getBigDecimal("SALDO"));
    sks.setBigDecimal("PVSSALDO", sks.getBigDecimal("PVSALDO"));
//    sks.setTimestamp();
    R2Handler.handleDatPrimitka(sks);
    sks.post();
    sks.copyTo(lastSks);
//    addToKumulativ(sks);

    int dodsk = 0;
    uis.refresh();
    for (ds.first(); ds.inBounds(); ds.next()) {
      if (ds.getBigDecimal("ID").signum() == 0 && 
          ds.getBigDecimal("IP").signum() == 0) continue;
      String k = ds.getString("BROJKONTA");
      if (k == null || k.length() == 0) k = getKonto(ds);
      if (k == null || k.length() == 0 || 
          (!k.equals(ds.getString("BROJKONTA")) && ds.getInt("RBS") != 1)) return NO_SHEMA; 
      
      uis.insertRow(false);
      DataSet.copyTo(uiscols, ds, uiscols, uis);
      uis.setString("CKNJIGE", cknjige);
      uis.setString("URAIRA", uraira);
      uis.setString("BROJKONTA", ds.getString("BROJKONTA"));
      uis.setString("CSKSTAVKE", cskstav);
      uis.setBigDecimal("ID", ds.getBigDecimal("ID").
        multiply(sks.getBigDecimal("TECAJ")).divide(jedval, 2, BigDecimal.ROUND_HALF_UP));
      uis.setBigDecimal("IP", ds.getBigDecimal("IP").
        multiply(sks.getBigDecimal("TECAJ")).divide(jedval, 2, BigDecimal.ROUND_HALF_UP));
//      uis.setBigDecimal("ID", ds.getBigDecimal("PVID"));
//      uis.setBigDecimal("IP", ds.getBigDecimal("PVIP"));
//      uis.setString("CSKSTAVKE", findCSK(sks));
      
      // ako je konto salda konti, dodaj jos jednu sk stavku ali bez pripadajuæih uistavki
      if (ds.getInt("RBS") != 1 && raKonta.isSaldak(k)) {
        sks.insertRow(false);
        DataSet.copyTo(skscols, dummysk, skscols, sks);
        sks.setString("CGKSTAVKE", "#");
        sks.setTimestamp("DATUMKNJ", dummysk.getTimestamp("DATPRI"));
        sks.setString("BROJKONTA", k);
        sks.setString("VRDOK", uraira.equalsIgnoreCase("U") ? "OKD" : "OKK");
        sks.setString("BROJDOK", dummysk.getString("BROJDOK") + "-" + ++dodsk);
        
        if (Skstavke.getDataModule().getRowCount(Condition.whereAllEqual(key, sks)) > 0) {
          System.out.println("identic");
          return DUPLICATE_KEY;
        }

        if (cknjige.length() < 5) sks.setString("CKNJIGE", "-" + cknjige);
        else sks.setString("CKNJIGE", "-" + cknjige.substring(1));
        sks.setString("CSKSTAVKE", findCSK(sks));
        sks.setBigDecimal("PVID", ds.getBigDecimal("ID"));
        sks.setBigDecimal("PVIP", ds.getBigDecimal("IP"));
        sks.setBigDecimal("ID", ds.getBigDecimal("ID").
            multiply(sks.getBigDecimal("TECAJ")).divide(jedval, 2, BigDecimal.ROUND_HALF_UP));
        sks.setBigDecimal("IP", ds.getBigDecimal("IP").
            multiply(sks.getBigDecimal("TECAJ")).divide(jedval, 2, BigDecimal.ROUND_HALF_UP));
        sks.setBigDecimal("SALDO", sks.getBigDecimal("IP").add(sks.getBigDecimal("ID")));
        sks.setBigDecimal("PVSALDO", sks.getBigDecimal("PVIP").add(sks.getBigDecimal("PVID")));
        sks.setBigDecimal("SSALDO", sks.getBigDecimal("SALDO"));
        sks.setBigDecimal("PVSSALDO", sks.getBigDecimal("PVSALDO"));
        sks.post();
      }
    }
    uis.post();
//    System.out.println("succeed");
    return OK;
//    sks.saveChanges();
//    Valid.getValid().execSQL("SELECT * FROM skstavkerad WHERE knjig = '"+ds.getString("KNJIG")+
//       "' AND cpar = "+ds.getInt("CPAR")+" AND vrdok = '"+ds.getString("VRDOK")+
//       "' AND brojdok = '"+ds.getString("BROJDOK")+"'");
//    QueryDataSet res = Valid.getValid().RezSet;
//    res.open();
//    res.first();
//    while (res.inBounds()) {
//      uis.insertRow(false);
//      DataSet.copyTo(uiscols, res, uiscols, uis);
//      uis.setString("BRDOK", res.getString("BROJDOK"));
//      res.next();
//    }
//    uis.saveChanges();
  }
  
  public static void updateOutOfRangeSaldo(DataSet ds, Condition outCond) {
    updateOutOfRangeSaldo(ds, outCond, null);
  }

  /*
   * Popravlja saldo dokumenata koji su pokriveni dokumentima NAKON krajnjeg
   * datuma perioda (ne i one PRIJE pocetnog datuma, ne znam cemu bi to
   * sluzilo.) Zapravo samo azurira saldo svih dokumenata u datasetu, koji su
   * pokriveni dokumentima koji odgovaraju zadanom uvjetu. Na onom tko poziva metodu
   * lezi odgovornost da ispravno zada uvjet tako da bude dosljedan sa samim
   * datasetom ds. retDoc moze biti null, a ako nije null, u njega ce se ubaciti
   * popis dokumentata izvan perioda za svaki azurirani dokument.
   */
  public static void updateOutOfRangeSaldo(DataSet ds, Condition outCond, Map retDoc) {
    long time = System.currentTimeMillis();
    System.out.println("Out of range: " + ds.rowCount());
    System.out.println("Simple: " + isSimple());
    DataSet outSet = Skstavke.getDataModule().getTempSet(
    		"CSKSTAVKE OZNVAL TECAJ SSALDO PVSSALDO", outCond);
    outSet.open();
    System.out.println("Opened out dataset, rows " + outSet.rowCount());

    // prebaci ih odmah u HashMap radi jednostavnosti i brzine.
    Map remDoc = new HashMap();
    for (outSet.first(); outSet.inBounds(); outSet.next())
      remDoc.put(outSet.getString("CSKSTAVKE"), new OutDoc(outSet));

    // otvori set pokrivenih tako da odgovara racunima iz ds. Potencijalno
    // sporo.
    DataSet pok = dM.getDataModule().getPokriveni();
    pok.open();
    System.out.println("Opened pokriveni. " + pok.rowCount());

    // prodji kroz sve pokrivene u potrazi za dokumentima izvan perioda,
    Map linkDoc = new HashMap();

    for (pok.first(); pok.inBounds(); pok.next())
      for (int vrdok = 0; vrdok < 2; ++vrdok) {
        String outCsk = pok.getString(vrdok == 0 ? "CUPLATE" : "CRACUNA");
        OutDoc out = (OutDoc) remDoc.get(outCsk);
        if (out != null) {
          String mainCsk = pok.getString(vrdok == 0 ? "CRACUNA" : "CUPLATE");
          ArrayList outs = (ArrayList) linkDoc.get(mainCsk);
          if (outs == null) linkDoc.put(mainCsk, outs = new ArrayList());
          outs.add(new OutDoc(out, pok.getBigDecimal("IZNOS"), vrdok == 1));
        }
      }

    System.out.println("Passed pok.");
    // System.out.println(updateRac);

    // prolazi kroz dataset i azurira saldo dokumenata pokrivenih dokumentima
    // izvan perioda.
    for (ds.first(); ds.inBounds(); ds.next()) {
      String csk = ds.getString("CSKSTAVKE");
      ArrayList outs = (ArrayList) linkDoc.get(csk);
      if (outs != null) {
        // za godisnu obradu: vracanje popisa pokrivenih dokumenata izvan perioda
        if (retDoc != null) retDoc.put(csk, outs);
        
        boolean racTip = raVrdokMatcher.isRacunTip(ds);
        boolean domVal = isDomVal(ds);
        
        BigDecimal rtecaj = null;//, rjedval = null;
        if (!isSimple()) {
          rtecaj = calcTecaj(ds);
          /*rjedval = findJedVal(ds);
          if (rjedval == null) rjedval = Aus.one0;*/
        }
        // za sve dokumente izvan raspona kojima je pokriven ovaj dokument, 
        // azuriraj saldo na dokumentu.
        for (int i = 0; i < outs.size(); i++) {
          OutDoc out = (OutDoc) outs.get(i);
         
          BigDecimal tecaj = rtecaj;//, jedval = rjedval;
          // ako vanjski dokument nije bio racun, onda svaki dokument iz liste ima svoj tecaj.
          if (!isSimple() && tecUplata ^ out.racSide) {
            tecaj = out.tecaj;
            /*jedval = findJedVal(out.val);
            if (jedval == null) jedval = Aus.one0;*/
          }
          
          // azuriraj saldo na ovom racunu, ovisno o flagu simpleDev.
          if (isSimple())
            modifyMatchSaldo(ds, out.iznos.negate(), racTip == out.racSide);
          else {
            if (ds.hasColumn("PVPOK") != null && (domVal || !isDomVal(out.val)))
              modifyMatchPVSaldo(ds, out.iznos.negate(), racTip == out.racSide);
            BigDecimal domIznos = domVal != isDomVal(out.val) ? out.iznos :
               out.iznos.multiply(tecaj).setScale(2, BigDecimal.ROUND_HALF_UP);
            modifyMatchSaldo(ds, domIznos.negate(), racTip == out.racSide);
          }
        }
      }
    }
    
    outSet.close();
    outSet = null;
    System.out.println("Total update time "
        + (System.currentTimeMillis() - time));
  }

  public static class OutDoc {
    public String csk;
    public String val;
    public BigDecimal tecaj;
    public BigDecimal iznos;
    public boolean racSide;

    public OutDoc(DataSet sk) {
      csk = sk.getString("CSKSTAVKE");
      val = sk.getString("OZNVAL");
      tecaj = calcTecaj(sk);
    }
    
    public OutDoc(OutDoc copy, BigDecimal pok, boolean rac) {
      csk = copy.csk;
      val = copy.val;
      tecaj = copy.tecaj;
      iznos = pok;
      racSide = rac;
    }
  }
  
  public static boolean existingDocument(int cpar, String brdok, boolean ulaz) {
    Condition c = Condition.equal("CPAR", cpar).and(Condition.equal("BROJDOK", brdok)).
       and(Aus.getKnjigCond()).and(Condition.in("VRDOK", ulaz ? new String[] {"URN", "OKD"} :
         new String[] {"IRN", "OKK"}));
    return Skstavke.getDataModule().getRowCount(c) + 
          Skstavkerad.getDataModule().getRowCount(c) > 0;  
  }

  public static String getKonto(String vrdok, String cskl, String stavka) {
    return hr.restart.gk.raKnjizenje.getBrojKonta(vrdok, cskl, stavka);
  }

  public static String getKonto(ReadRow ds) {
    String vrdok = ds.getString("VRDOK");
    if (vrdok.equalsIgnoreCase("OKK")) vrdok = "IRN";
    if (vrdok.equalsIgnoreCase("OKD")) vrdok = "URN";
    return getKonto(vrdok, ds.getString("CSKL"), String.valueOf(ds.getShort("STAVKA")));
  }
  
/*
  public static void recalculateKumulativ() {
    recalculateKumulativ(Valid.getValid().getToday());
  }*/
  
  static HashSet getSaldak(String vrsta) {
    HashSet konta = new HashSet();
    DataSet saldak = Konta.getDataModule().getTempSet(Condition.equal("SALDAK", vrsta));
    raProcess.openScratchDataSet(saldak);
    for (saldak.first(); saldak.inBounds(); saldak.next())
      konta.add(saldak.getString("BROJKONTA"));
    return konta;
  }
  
  private static class SalDoc {
    String val;
    BigDecimal tecaj;

    public SalDoc(DataSet sk) {
      val = sk.getString("OZNVAL");
      tecaj = calcTecaj(sk);
    }
  }
  
  private static class PokDoc {
    BigDecimal iznos;
    String csk;
    boolean racSide;
    
    public PokDoc(boolean rac, String csk, BigDecimal iznos) {
      racSide = rac;
      this.iznos = iznos;
      this.csk = csk;
    }
  }
  
  private static class PokriveniDok {
    //BigDecimal total = n0;
    List others = new ArrayList();
    public PokriveniDok(PokDoc other) {
      others.add(other);
    }
    
    public void add(PokDoc other) {
      others.add(other);
    }
    
    public boolean checkEmpty() {
      for (int i = 0; i < others.size(); i++)
        if (((PokDoc) others.get(i)).csk.trim().length() == 0) return true;
        
      return false;
    }
    
    public String getNonExistant(Map allSk) {
      String ret = null;
      for (int i = 0; i < others.size(); i++)
        if (!allSk.containsKey(ret = ((PokDoc) others.get(i)).csk)) return ret;
      
      return null;
    }
    
    public BigDecimal getTotal(boolean isRac) {
      BigDecimal total = Aus.zero2;
      for (int i = 0; i < others.size(); i++) {
        PokDoc pd = (PokDoc) others.get(i);
        total = total.add(isRac == pd.racSide ? pd.iznos.negate() : pd.iznos);
      }
      return total;
    }
    
    public void checkStavka(DataSet sk, Map allSk, boolean fix) {
      BigDecimal iznos = sk.getBigDecimal("SSALDO");
      BigDecimal saldo = sk.getBigDecimal("SALDO");
      BigDecimal rsaldo = iznos;
      BigDecimal pviznos = sk.getBigDecimal("PVSSALDO");
      BigDecimal pvsaldo = sk.getBigDecimal("PVSALDO");
      BigDecimal pvrsaldo = pviznos;
      BigDecimal tecaj = calcTecaj(sk);
      //BigDecimal jedval = findJedVal(sk);
      boolean domVal = isDomVal(sk);
      boolean racTip = raVrdokMatcher.isRacunTip(sk);
      for (int i = 0; i < others.size(); i++) {
        PokDoc pd = (PokDoc) others.get(i);
        
        if (fix && !allSk.containsKey(pd.csk)) continue;
        //BigDecimal pi = (BigDecimal) vals.get(i);
        SalDoc sd = (SalDoc) allSk.get(pd.csk);
        BigDecimal rTecaj = tecaj;
        //BigDecimal rJedval = jedval;
        if (tecUplata ^ pd.racSide) {
          rTecaj = sd.tecaj;
          //rJedval = findJedVal(sd.val);
        }
        boolean sdDomVal = isDomVal(sd.val);
        BigDecimal domIznos = sdDomVal != domVal ? pd.iznos :
            pd.iznos.multiply(rTecaj).setScale(2, BigDecimal.ROUND_HALF_UP);

        if (domVal || !sdDomVal)
          pvrsaldo = pvrsaldo.subtract(racTip == pd.racSide ? pd.iznos.negate() : pd.iznos);
        rsaldo = rsaldo.subtract(racTip == pd.racSide ? domIznos.negate() : domIznos);
      }
      if (pvsaldo.compareTo(pvrsaldo) != 0)
        if (fix) sk.setBigDecimal("PVSALDO", pvrsaldo);
        else raProcess.addError("Nekonzistentnost deviznog pokrivenog iznosa", 
            sk, pviznos.subtract(pvrsaldo));
      if (saldo.compareTo(rsaldo) != 0) {
        if (fix) sk.setBigDecimal("SALDO", rsaldo);
        else {
          sk.setBigDecimal("PVSSALDO", iznos);
          sk.setBigDecimal("PVSALDO", saldo);
          raProcess.addError("Nekonzistentnost kunskog pokrivenog iznosa", 
            sk, iznos.subtract(rsaldo));
        }
      }
    }
  }
  
  public static void checkPokriveniProc(String fromGod, String toGod) {
    System.out.println("Checking consistency from "+fromGod+" to "+toGod);
    raProcess.installErrorTrace(dM.getDataModule().getSkstavke(), 
        new String[] {"CSKSTAVKE", "DATUMKNJ", colSSaldo(), colSaldo()},
        new Column[] {dM.createBigDecimalColumn("POKRIVENO", "Pokriveno", 2)},
        "Greške u vezama pokrivanja");
    
    raProcess.setMessage("Dohvat stavaka salda konti...", false);
    String scols = simpleDev ? "SSALDO SALDO" : "OZNVAL TECAJ SSALDO SALDO PVSSALDO PVSALDO";
    String ssalcol = colSSaldo(), salcol = colSaldo();
    DataSet sk = Skstavke.getDataModule().getTempSet("CSKSTAVKE DATUMKNJ ID IP VRDOK "+scols,
        Aus.getKnjigCond().and(Condition.where("POKRIVENO", Condition.NOT_EQUAL, "X")).and(
            Condition.between("DATUMKNJ", ut.getYearBegin(fromGod), ut.getYearEnd(toGod))));
    sk.open();
    System.out.println(sk.rowCount() + " rows in skstavke");
    
    Map allSk = new HashMap();
    for (sk.first(); sk.inBounds(); sk.next())
      allSk.put(sk.getString("CSKSTAVKE"), simpleDev ? null : new SalDoc(sk));

    raProcess.setMessage("Dohvat veza pokrivanja dokumenata...", true);
    DataSet pok = Pokriveni.getDataModule().getTempSet();
    pok.open();
    Map poka = new HashMap();
    for (pok.first(); pok.inBounds(); pok.next()) {
      String csku = pok.getString("CUPLATE");
      String cskr = pok.getString("CRACUNA");
      BigDecimal sal = pok.getBigDecimal("IZNOS");
      PokriveniDok du = (PokriveniDok) poka.get(csku);
      PokriveniDok dr = (PokriveniDok) poka.get(cskr);
      
      if (du != null) du.add(new PokDoc(true, cskr, sal));
      else poka.put(csku, new PokriveniDok(new PokDoc(true, cskr, sal)));
      
      if (dr != null) dr.add(new PokDoc(false, csku, sal));
      else poka.put(cskr, new PokriveniDok(new PokDoc(false, csku, sal)));
    }
    pok.close();
    pok = null;

    for (sk.first(); sk.inBounds(); sk.next()) {
      boolean rac = raVrdokMatcher.isRacunTip(sk);
      String csk = sk.getString("CSKSTAVKE");
      BigDecimal ssal = sk.getBigDecimal(ssalcol);
      BigDecimal sal = sk.getBigDecimal(salcol);
      
      if (ssal.signum() == 0)
        raProcess.addError("Iznos stavke je nula", sk, n0);
      else if ((ssal.signum() < 0 && sal.signum() > 0) || 
          (ssal.signum() < 0 && sal.signum() > 0))
        raProcess.addError("Saldo i iznos stavke imaju razlièit predznak", sk, n0);
      else {
        String bad = null;
        PokriveniDok pp = (PokriveniDok) poka.get(csk);
        if (pp != null) {
          if (pp.checkEmpty())
            raProcess.addError("Stavka je pokrivena null stavkom", sk, pp.getTotal(rac));
          else if (null != (bad = pp.getNonExistant(allSk)))
            raProcess.addError("Stavka je pokrivena nepostojeæom stavkom "+bad, 
                sk, pp.getTotal(rac));
          else {
            if (simpleDev) {
              ssal = ssal.subtract(pp.getTotal(rac));
              if (ssal.compareTo(sal) != 0)
                raProcess.addError("Nekonzistentnost pokrivenog iznosa", sk, pp.getTotal(rac));
            } else pp.checkStavka(sk, allSk, false);
          }
        } else if (ssal.compareTo(sal) != 0)
          raProcess.addError("Nedostaje veza za pokrivanje ili greška u saldu", sk, n0);
      }
    }
    sk.close();
  }
  
  public static void checkPokriveno() {
    raProcess.runChild(new Runnable() {
      public void run() {
        checkPokriveniProc(
            Valid.getValid().getKnjigYear("gk"), 
            Valid.getValid().findYear());
      }
    });
/*    raProcess.getErrors().jp.setKumTak(true);
    raProcess.getErrors().jp.setStoZbrojiti(
        new String[] {"IZNOS"});
   raProcess.getErrors().jp.init_kum();*/
    raProcess.report("Provjera završena, grešaka: "+raProcess.getErrors().countErrors());
    //query za izmjenu
  }
  
  public static void checkPokriveno(final String fromGod, final String toGod) {
    raProcess.runChild(new Runnable() {
      public void run() {
        checkPokriveniProc(fromGod, toGod);
      }
    });
    raProcess.report("Provjera završena, grešaka: "+raProcess.getErrors().countErrors());
  }
  
  
  public static void checkSkstavkeVrdokontoProc(String fromGod, String toGod) {
    raProcess.installErrorTrace(dM.getDataModule().getSkstavke(), 
        new String[] {"BROJDOK", "CPAR", "DATDOK", "BROJKONTA", "VRDOK", "ID", "IP"},
        "Greške u vrstama dokumenata");
    raProcess.getErrors().setSize(760, 400);
    String knjig = hr.restart.zapod.OrgStr.getKNJCORG(false);
    raProcess.setMessage("Dohvat konta za salda konti", true);
    HashSet ksaldak = getSaldak("K");
    HashSet dsaldak = getSaldak("D");
    
    DataSet ds = Skstavke.getDataModule().getTempSet(
        "BROJDOK CPAR DATDOK BROJKONTA VRDOK ID IP", Condition.equal("KNJIG", knjig).
        and(Condition.where(colPok(), Condition.NOT_EQUAL, "X")).
        and(Condition.between("DATUMKNJ", ut.getYearBegin(fromGod), ut.getYearEnd(toGod))));
    raProcess.setMessage("Otvaranje stavaka salda konti", false);
    raProcess.openScratchDataSet(ds);
    raProcess.setMessage("Provjera stavaka", false);
    for (ds.first(); ds.inBounds(); ds.next()) {
      raProcess.checkClosing();
      String konto = ds.getString("BROJKONTA");
      if (!ksaldak.contains(konto) && !dsaldak.contains(konto))
        raProcess.addError("Konto nije salda konti", ds);
      else if (raVrdokMatcher.isKup(ds) && !ksaldak.contains(konto) ||
          raVrdokMatcher.isDob(ds) && !dsaldak.contains(konto))
        raProcess.addError("Pogrešna vrsta dokumenta ili tip konta", ds);
    }
    ds.close();
  }
  
  public static void checkSkstavkeVrdokonto() {
    raProcess.runChild(new Runnable() {
      public void run() {
        checkSkstavkeVrdokontoProc(
            Valid.getValid().getKnjigYear("gk"), 
            Valid.getValid().findYear());
      }
    });
    /*raProcess.getErrors().jp.setKumTak(true);
    raProcess.getErrors().jp.setStoZbrojiti(
        new String[] {"IZNOS"});
    raProcess.getErrors().jp.init_kum();*/
//ispisi query za update
    String chcond = "SELECT * FROM SKSTAVKE WHERE (KNJIG = '"+OrgStr.getKNJCORG()+"') AND (";
    StorageDataSet errs = raProcess.getErrors().getDataSet();
    for (errs.first(); errs.inBounds(); errs.next()) {
      chcond = chcond+"("
      	+Condition.whereAllEqual(new String[] {"BROJDOK", "CPAR", "BROJKONTA", "VRDOK"},errs)
      	+") OR ";
    }
    chcond = new VarStr(chcond).rightChop(4).toString()+")";
    System.out.println("checkSkstavkeVrdokonto.chcond = "+chcond);
//end of query za update
    raProcess.report("Provjera završena, grešaka: "+raProcess.getErrors().countErrors());
    //query za izmjenu
  }

  /*public static void checkKumulativ() {
    checkKumulativ(Valid.getValid().getKnjigYear("gk"));
  }

  static JraButton fix = null;

  static void checkMissingKum(DataSet st, boolean kup) {
    DataSet kum = Skkumulativi.getDataModule().getTempSet("1=0");
    kum.open();
    kum.insertRow(false);

    raProcess.openScratchDataSet(st);
    BigDecimal pr = n0, pu = n0;
    int lastpar = -1001;
    String lastk = "";
    for (st.first(); st.inBounds(); st.next()) {
      if (lastpar != st.getInt("CPAR") || !lastk.equals(st.getString("BROJKONTA"))) {
        if (lastk.length() > 0)
          raProcess.addError("Ne postoji kumulativ " + (kup ? "kupca" : "dobavljaèa") +
               " a postoje stavke", kum, new BigDecimal[] {n0, n0, pr, pu});
        lastpar = st.getInt("CPAR");
        lastk = st.getString("BROJKONTA");
        dM.copyColumns(st, kum, new String[] {"CPAR", "BROJKONTA"});
        pr = pu = n0;
      }
      pr = pr.add(st.getBigDecimal(kup ? "ID" : "IP"));
      pu = pu.add(st.getBigDecimal(kup ? "IP" : "ID"));
    }
    if (lastk.length() > 0)
      raProcess.addError("Ne postoji kumulativ " + (kup ? "kupca" : "dobavljaèa") +
          " a postoje stavke", kum, new BigDecimal[] {n0, n0, pr, pu});
  }
  
  public static void checkKumulativProc(String god) {
    raProcess.installErrorTrace(dM.getDataModule().getSkkumulativi(),
        new String[] {"CPAR", "BROJKONTA"}, new Column[] {
          dM.createBigDecimalColumn("KUMULR", "Kumulativ raèuna", 2),
          dM.createBigDecimalColumn("KUMULU", "Kumulativ uplata", 2),
          dM.createBigDecimalColumn("TOTALR", "Zbroj raèuna", 2),
          dM.createBigDecimalColumn("TOTALU", "Zbroj uplata", 2)
        }, "Greške u kumulativima salda konti");
      raProcess.setErrorColumnWidth(30);
      raProcess.getErrors().setSize(760, 400);
      String knjig = hr.restart.zapod.OrgStr.getKNJCORG(false);
      Condition range = Condition.between("DATUMKNJ",
          Util.getUtil().getYearBegin(god), Util.getUtil().getYearEnd(god));
      Timestamp d2 = Util.getUtil().addDays(Util.getUtil().getYearBegin(god), 1);
      DataSet ds = Skkumulativi.getDataModule().getTempSet(
          Condition.equal("KNJIG", knjig).and(Condition.equal("GODINA", god)));
      raProcess.openDataSet(ds);
      for (ds.first(); ds.inBounds(); ds.next()) {
        raProcess.checkClosing();
        raProcess.setMessage("Provjera kumulativa "+ds.getRow()+"/"+ds.getRowCount(), false);
        boolean kupac = "K".equalsIgnoreCase(ds.getString("ULOGA"));
        DataSet st = Skstavke.getDataModule().getTempSet("DATUMKNJ ID IP",
          Condition.equal("KNJIG", knjig).and(range).and(
          Condition.whereAllEqual(new String[] {"CPAR", "CORG", "BROJKONTA"}, ds)).
          and(Condition.where(colPok(), Condition.NOT_EQUAL, "X")).
          and(kupac ? Condition.in("VRDOK", new String[] {"IRN", "UPL", "OKK"})
                    : Condition.in("VRDOK", new String[] {"URN", "IPL", "OKD"}))
        );
        raProcess.openScratchDataSet(st);
        int rows = st.getRowCount();
        BigDecimal pr = n0, pu = n0, psr = n0, psu = n0;
        BigDecimal kpr = ds.getBigDecimal("PROMETR");
        BigDecimal kpu = ds.getBigDecimal("PROMETU");
        BigDecimal kpsr = ds.getBigDecimal("POCSTRAC");
        BigDecimal kpsu = ds.getBigDecimal("POCSTUPL");
        for (st.first(); st.inBounds(); st.next()) {
          if (st.getTimestamp("DATUMKNJ").before(d2)) {
            psr = psr.add(st.getBigDecimal(kupac ? "ID" : "IP"));
            psu = psu.add(st.getBigDecimal(kupac ? "IP" : "ID"));
          } else {
            pr = pr.add(st.getBigDecimal(kupac ? "ID" : "IP"));
            pu = pu.add(st.getBigDecimal(kupac ? "IP" : "ID"));
          }
        }

        if (psr.compareTo(kpsr) != 0 ||
            psu.compareTo(kpsu) != 0)
          raProcess.addError("Pogrešno poèetno stanje "+
            (kupac ? "kupca" : "dobavljaèa"), ds, new BigDecimal[] {kpsr, kpsu, psr, psu});
        if (pr.compareTo(kpr) != 0 ||
            pu.compareTo(kpu) != 0)
          raProcess.addError("Kumulativ " + (kupac ? "kupca" : "dobavljaèa") +
             " nije jedan zbroju stavaka", ds, new BigDecimal[] {kpr, kpu, pr, pu});
        if (psr.signum() == 0 && psu.signum() == 0 &&
            kpsr.signum() == 0 && kpsu.signum() == 0 &&
            pr.signum() == 0 && pu.signum() == 0 &&
            kpr.signum() == 0 && kpu.signum() == 0 && rows == 0)
          raProcess.addError("Suvišan kumulativ " + (kupac ? "kupca" : "dobavljaèa"), ds,
              new BigDecimal[] {kpr, kpu, pr, pu});
      }
      raProcess.setMessage("Provjera nepostojeæih kumulativa dobavljaèa ...", true);
      
      Valid.getValid().execSQL("SELECT * FROM skstavke WHERE "+
          Condition.equal("KNJIG", knjig).and(range).
          and(Condition.in("VRDOK", new String[] {"URN","IPL","OKD"})) +
          " AND pvpok != 'X' AND NOT EXISTS (SELECT * FROM skkumulativi WHERE"+
          " skkumulativi.knjig='"+knjig+"' AND skkumulativi.godina='"+god+"' AND"+
          " skkumulativi.corg=skstavke.corg AND skkumulativi.cpar=skstavke.cpar AND"+
          " skkumulativi.brojkonta=skstavke.brojkonta AND skkumulativi.uloga='D')"+
          " ORDER BY cpar");

      checkMissingKum(Valid.getValid().getDataAndClear(), false);

      raProcess.setMessage("Provjera nepostojeæih kumulativa kupca ...", false);
      Valid.getValid().execSQL("SELECT * FROM skstavke WHERE "+
          Condition.equal("KNJIG", knjig).and(range).
          and(Condition.in("VRDOK", new String[] {"IRN","UPL","OKK"})) +
          " AND pvpok != 'X' AND NOT EXISTS (SELECT * FROM skkumulativi WHERE"+
          " skkumulativi.knjig='"+knjig+"' AND skkumulativi.godina='"+god+"' AND"+
          " skkumulativi.corg=skstavke.corg AND skkumulativi.cpar=skstavke.cpar AND"+
          " skkumulativi.brojkonta=skstavke.brojkonta AND skkumulativi.uloga='K')"+
          " ORDER BY cpar");
      checkMissingKum(Valid.getValid().getDataAndClear(), true);
  }
  
  public static void checkKumulativ(final String god) {
    fix = null;
    raProcess.runChild(new Runnable() {
      public void run() {
        checkKumulativProc(god);
      }
    });
    raProcess.getErrors().jp.setKumTak(true);
    raProcess.getErrors().jp.setStoZbrojiti(
        new String[] {"KUMULR", "KUMULU", "TOTALR", "TOTALU"});
    raProcess.getErrors().jp.init_kum();
//    raProcess.getErrors().jp.setDataSetAndSums(raProcess.getErrors().jp.getStorageDataSet(),
//        new String[] {"PROMETR", "PROMETU", "TOTALR", "TOTALU"});
    fix = new JraButton();
    fix.setText("Popravi");
    raProcess.getErrors().setActionButton(fix);
    fix.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fix) {
          if (JOptionPane.showConfirmDialog(fix.getTopLevelAncestor(),
              "Kumulativi æe biti napunjeni zbrojem stavaka. Nastaviti?", "Potvrda",
              JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) ==
              JOptionPane.OK_OPTION) {
            recalculateKumulativ(god);
            ((Window) fix.getTopLevelAncestor()).dispose();
          }
        }
      }
    });
    raProcess.report("Provjera završena, grešaka: "+raProcess.getErrors().countErrors());
  }

 
  public static void recalculateKumulativ(Timestamp t) {
    recalculateKumulativ(ut.getYear(t));
  }

  public static void recalculateKumulativ(final String god) {
    raProcess.runChild(new Runnable() {
      public void run() {
        raLocalTransaction recalc = new raLocalTransaction() {
          public boolean transaction() throws Exception {
            try {
              String knjig = hr.restart.zapod.OrgStr.getKNJCORG(false);
              Condition range = Condition.between("DATUMKNJ",
                  Util.getUtil().getYearBegin(god), Util.getUtil().getYearEnd(god));
              Timestamp d2 = Util.getUtil().addDays(Util.getUtil().getYearBegin(god), 1);
              //Skstavke.getDataModule().setFilter(Condition.equal("KNJIG", knjig).and(range));
              raProcess.setMessage("Inicijalizacija kumulativa ...", false);
              raTransaction.runSQL("DELETE FROM skkumulativi WHERE "+Condition.equal("KNJIG", knjig).
                  and(Condition.equal("GODINA", god)));              
//              raSaldaKonti.setKumInvalid();
              DataSet ds = Skstavke.getDataModule().getTempSet(
                  "KNJIG CPAR CORG VRDOK BROJKONTA DATUMKNJ ID IP POKRIVENO PVPOK", 
                  Condition.equal("KNJIG", knjig).and(range));
              raProcess.setMessage("Dohvat stavaka ...", false);
              raProcess.openScratchDataSet(ds);
              raProcess.setMessage("Ažuriranje kumulativa ...", false);
              for (ds.first(); ds.inBounds(); ds.next())
                if (ds.getTimestamp("DATUMKNJ").before(d2))
                  addToKumulativPS(ds);
              else addToKumulativ(ds);
              raProcess.setMessage("Spremanje promjena ...", false);
              raTransaction.saveChanges(dM.getDataModule().getSkkumulativi());              
              return true;
            } catch (Exception e) {
              e.printStackTrace();
//              setKumInvalid();
              return false;
            }
          }
        };
        if (!recalc.execTransaction()) raProcess.fail();
      }
    });
    if (raProcess.isCompleted())
      JOptionPane.showMessageDialog(null, "Popravak završen.", "Poruka",
                                    JOptionPane.INFORMATION_MESSAGE);
    else if (raProcess.isFailed())
      JOptionPane.showMessageDialog(null, "Popravak neuspješan!", "Poruka",
                                    JOptionPane.ERROR_MESSAGE);
  }*/

  public static void setSaldo(ReadWriteRow stavka, BigDecimal saldo) {
    stavka.setBigDecimal("SALDO", saldo);
    if (stavka.hasColumn("POKRIVENO") == null) return;
    if (saldo.signum() == 0)
      stavka.setString("POKRIVENO", "D");
    else stavka.setString("POKRIVENO", "N");
  }

  public static void setPVSaldo(ReadWriteRow stavka, BigDecimal saldo) {
    stavka.setBigDecimal("PVSALDO", saldo);
    if (saldo.signum() == 0)
      stavka.setString("PVPOK", "D");
    else stavka.setString("PVPOK", "N");
  }

  public static void modifyMatchSaldo(ReadWriteRow stavka, BigDecimal iznos) {
    modifyMatchSaldo(stavka, iznos, raVrdokMatcher.isKob(stavka));
  }
  
  public static void modifyMatchSaldo(ReadWriteRow stavka, BigDecimal iznos, boolean invert) {
    setSaldo(stavka, stavka.getBigDecimal("SALDO").add(invert ? iznos : iznos.negate()));
  }
  
  public static void modifyMatchPVSaldo(ReadWriteRow stavka, BigDecimal iznos) {
    modifyMatchPVSaldo(stavka, iznos, raVrdokMatcher.isKob(stavka));
  }

  public static void modifyMatchPVSaldo(ReadWriteRow stavka, BigDecimal iznos, boolean invert) {
    setPVSaldo(stavka, stavka.getBigDecimal("PVSALDO").add(invert ? iznos : iznos.negate()));
  }

  public static void matchIznos(DataSet sk, DataSet pok, BigDecimal iznos, String otherCsk) {
    matchIznos(sk, sk, pok, iznos, otherCsk);
  }

  public static void matchIznos(DataSet sk, DataSet sk2, BigDecimal iznos) {
    matchIznos(sk, sk2, null, iznos, null);
  }
  
  public static void matchIznos(DataSet sk, DataSet sk2, DataSet pok, BigDecimal iznos) {
    matchIznos(sk, sk2, pok, iznos, sk2.getString("CSKSTAVKE"));
  }

  // pokriva stavke u 'sk' i 'sk2' s iznosom 'iznos', i sprema vezu u 'pok'.
  // sk i sk2 mogu biti isti dataset, a u tom slucaju sk je pozicioniran na
  // prvu stavku, a otherCsk je cskstavke one druge stavke.
  private static void matchIznos(DataSet sk, DataSet sk2, DataSet pok, BigDecimal iznos, String otherCsk) {
    // zapamti skstavke trenutno pozicionirane stavke u datasetu sk.
    String thisCsk = sk.getString("CSKSTAVKE");
    
    // nadji na koju stranu (cracuna ili cuplate) tablice pokriveni ide ova stavka.
    // druga stavka ide na drugu stranu, bez provjere. Na taj naèin moguæe je pokriti
    // i raèun s raèunom ili uplatu s uplatom, bez provjere.
    String thisVrsta = raVrdokMatcher.getMatchSide(sk);
    String otherVrsta = raVrdokMatcher.getOtherSide(sk);   // tecraz
 
    boolean thisAsRac = thisVrsta.equals("cracuna");
    
    // prokl. borland trosi isti timestamp objekt za sve redove tako da ga moram klonirat
    Timestamp thisDat = new Timestamp(sk.getTimestamp("DATDOK").getTime());
    
    // tecaj se uvijek uzima sa stavke racuna (ne bas). Ako je ovo racun, uzmi tecaj.
    BigDecimal tecaj = tecUplata ^ thisAsRac ? calcTecaj(sk) : null;
    
    // provjeri pokrivanje strane valute s domicilnom: tecajne razlike
    boolean domValThis = isDomVal(sk);
    
    // nadju stranu druge stavke. Ako sk2 pokazuje na isti dataset kao sk, pozicioniraj
    // se na drugu stavku uz pomoc sifre otherCsk.
    if (sk == sk2) lookupData.getlookupData().raLocate(sk2, "CSKSTAVKE", otherCsk);
    //String otherVrsta = raVrdokMatcher.getMatchSide(sk2);         // tecraz
    Timestamp otherDat = new Timestamp(sk2.getTimestamp("DATDOK").getTime());
    
    // podesi tecaj ako vec nije podesen.
    if (tecaj == null) tecaj = calcTecaj(sk2);
    
//  provjeri pokrivanje strane valute s domicilnom: tecajne razlike
    boolean domValOther = isDomVal(sk2);
    
    // provjeri je li sve u redu sa stranama u pokrivanju, javi gresku ako nije. nemoj: tecraz
    /*if (thisVrsta == null && otherVrsta == null || thisVrsta.equalsIgnoreCase(otherVrsta)) {
      System.err.println("greska u vrstama dokumenata! "+thisCsk+" + "+otherCsk);
      return;
    }*/
    
    // pronadji jedinicu valute u kojoj se pokriva. Nije potrebno ako je
    // globalni flag (simpleDev) ukljucen jer se tada uvijek pokriva u kunama.
    /*BigDecimal jedval = null;    
    if (!isSimple()) {
      jedval = findJedVal(sk2);
      if (jedval == null) jedval = Aus.one0;
    }*/
    
    // zapamti vezu pokrivanja u datasetu pok.
    if (otherCsk != null) {
      pok.insertRow(false);
      pok.setString(thisVrsta, thisCsk);
      pok.setString(otherVrsta, otherCsk);
      pok.setBigDecimal("IZNOS", iznos);
      pok.post();
    }
    
    if (isSimple()) {
      // simpleDev: pokrivaj uvijek u kunama; kad je saldo u kunama nula
      // postavi i devizni saldo u nula. Hack.
      modifyMatchSaldo(sk2, iznos, thisAsRac == raVrdokMatcher.isRacunTip(sk2));
      if (sk2.getBigDecimal("SALDO").signum() == 0)
        setPVSaldo(sk2, n0);
      
      // azuriraj saldo i na drugoj stavci (pozicioniraj se ako smo na istom datasetu).
      if (sk == sk2) lookupData.getlookupData().raLocate(sk, "CSKSTAVKE", thisCsk);
      
      modifyMatchSaldo(sk2, iznos, thisAsRac != raVrdokMatcher.isRacunTip(sk));
      if (sk.getBigDecimal("SALDO").signum() == 0)
        setPVSaldo(sk, n0);
    } else {
      boolean invertOther = thisAsRac == raVrdokMatcher.isRacunTip(sk2);
      // !simpleDev: strogo pazi na devizna kunska salda. Iz deviznog iznosa
      // uz pomoc tecaja s racuna i jedinice valute nadji iznos u kunama.
      // ako se radi o pokrivanju strane valute s domicilnom
      // onda azuriraj samo kunski saldo.
      BigDecimal domIznos = domValThis != domValOther ? iznos : 
        iznos.multiply(tecaj).setScale(2, BigDecimal.ROUND_HALF_UP);
      if (domValOther || !domValThis) modifyMatchPVSaldo(sk2, iznos, invertOther);
      modifyMatchSaldo(sk2, domIznos, invertOther);
      
      // azuriraj saldo i na drugoj strani. 
      if (sk == sk2) lookupData.getlookupData().raLocate(sk, "CSKSTAVKE", thisCsk);
      boolean invertThis = thisAsRac != raVrdokMatcher.isRacunTip(sk);
      if (domValThis || !domValOther) modifyMatchPVSaldo(sk, iznos, invertThis);
      modifyMatchSaldo(sk, domIznos, invertThis);
    }
/*
    System.out.println("sk = "+sk);
    System.out.println("sk2 = "+sk2);*/
    
    // pozovi R2handler, modif. by ab.f. Umjesto one turboslozene provjere
    // sto je racun a sto uplata, koristim sljedece:
    
    // pogledaj je li sk racun, ako jest, onda je otherDat datum uplate (ranije zapamcen).
    if (raVrdokMatcher.isRacunTip(sk))
      R2Handler.matchPerformed(sk, otherDat); 
    else {
      // u suprotnom, provjeri je li druga strana racun. ako jest, thisDat je datum uplate.
      if (sk == sk2) lookupData.getlookupData().raLocate(sk, "CSKSTAVKE", otherCsk);
      if (raVrdokMatcher.isRacunTip(sk2))
        R2Handler.matchPerformed(sk2, thisDat);
      
      // else nam ne treba; ako nijedna strana nije racun, onda je ocito
      // rijec o pokrivanju uplate sa stornom uplate, a tu R2 ne treba.
      if (sk == sk2) lookupData.getlookupData().raLocate(sk, "CSKSTAVKE", thisCsk);
    }
//end andrej test
  }

  public static void matchThemAll(final DataSet ds) {
    raLocalTransaction matchAll = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        try {
          for (ds.first(); ds.inBounds(); ds.next())
            matchIfYouCan(ds, true);
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
    matchAll.execTransaction();
  }

  public static void matchThemAll() {
    raLocalTransaction matchAll = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        try {
          QueryDataSet allOfThem = dM.getDataModule().getSkstavkeCover();
          Skstavke.getDataModule().setFilter(allOfThem, Aus.getKnjigCond().
              and(Condition.equal(colPok(), "N")));
          allOfThem.open();
          for (allOfThem.first(); allOfThem.inBounds(); allOfThem.next())
            matchIfYouCan(allOfThem, true);
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
    matchAll.execTransaction();
  }
  
  static void addResultRow(DataSet result, String opis, DataSet sk, DataSet gk) {
    result.insertRow(false);
    result.setString("OPIS", opis);
    if (sk != null) {
      result.setString("CSKSTAVKE", sk.getString("CSKSTAVKE"));
      result.setBigDecimal("SID", sk.getBigDecimal("ID"));
      result.setBigDecimal("SIP", sk.getBigDecimal("IP"));
      result.setTimestamp("SDATUMKNJ", sk.getTimestamp("DATUMKNJ"));
      result.setString("SCG", sk.getString("CGKSTAVKE"));
    }
    if (gk != null) {
      result.setString("CGKSTAVKE", gk.getString("CNALOGA")+"-"+gk.getInt("RBS"));
      result.setBigDecimal("GID", gk.getBigDecimal("ID"));
      result.setBigDecimal("GIP", gk.getBigDecimal("IP"));
      result.setTimestamp("GDATUMKNJ", gk.getTimestamp("DATUMKNJ"));
    }
  }
  
  public static DataSet findConsistencyErrors(String fromGod, String toGod, boolean strict) {
    StorageDataSet results = new StorageDataSet();
    results.setColumns(new Column[] {
        dM.createStringColumn("OPIS", "Opis", 60),
        dM.createStringColumn("CSKSTAVKE", "Stavka SK", 90),
        dM.createBigDecimalColumn("SID", 2),
        dM.createBigDecimalColumn("SIP", 2),
        dM.createStringColumn("SCG", "Veza SK->GK", 38),
        dM.createTimestampColumn("SDATUMKNJ", "Knjiženo u SK"),
        dM.createStringColumn("CGKSTAVKE", "Stavka GK", 38),
        dM.createBigDecimalColumn("GID", 2),
        dM.createBigDecimalColumn("GIP", 2),
        dM.createTimestampColumn("GDATUMKNJ", "Knjiženo u GK"),
    });
    results.open();
    
    raProcess.setMessage("Provjera konzistentnosti GK i SK stavaka...",
        true);
    DataSet konsk = Konta.getDataModule().getTempSet(
        Condition.in("SALDAK", "K D"));
    raProcess.openScratchDataSet(konsk);

    for (konsk.first(); konsk.inBounds(); konsk.next()) {
      raProcess.checkClosing();
      String konto = konsk.getString("BROJKONTA");
      raProcess.setMessage("Dohvat SK stavaka konta " + konto, false);
      DataSet sk = Skstavke.getDataModule().getTempSet(
          "CSKSTAVKE ID IP DATUMKNJ CGKSTAVKE POKRIVENO",
          Aus.getKnjigCond().and(Condition.equal("BROJKONTA", konto)).and(
          Condition.between("DATUMKNJ", ut.getYearBegin(fromGod), ut.getYearEnd(toGod))));
      raProcess.openScratchDataSet(sk);

      raProcess.setMessage("Dohvat GK stavaka konta " + konto, false);
      DataSet gk = Gkstavke.getDataModule().getTempSet(
          "KNJIG GOD CVRNAL RBR RBS DATUMKNJ ID IP CNALOGA", Aus.getKnjigCond().and(
          Condition.between("GOD", fromGod, toGod).and(Condition.equal("BROJKONTA", konto))));
      raProcess.openScratchDataSet(gk);
      DataRow srch = new DataRow(gk, "CNALOGA");
      for (gk.first(); gk.inBounds(); gk.next())
        gk.setString("CNALOGA", jpBrojNaloga.getCNaloga(gk));
      gk.setSort(new SortDescriptor(new String[] {"CNALOGA"}));

      raProcess.setMessage("Usporedba stavaka konta " + konto, false);
      for (sk.first(); sk.inBounds(); sk.next()) {
        raProcess.checkClosing();
        if (sk.getString("POKRIVENO").equals("X")) continue;
        String cgk = sk.getString("CGKSTAVKE");
        int rbs = -1;
        int lastHyphen = cgk.lastIndexOf('-');
        if (lastHyphen > 5 && cgk.length() - lastHyphen <= 5) {
          rbs = Aus.getNumber(cgk.substring(lastHyphen + 1));
          cgk = cgk.substring(0, lastHyphen);
        }
        srch.setString("CNALOGA", cgk);
        if (gk.locate(srch, Locate.FIRST)) {
          if (rbs > 0) {
            while (gk.inBounds() && gk.getString("CNALOGA").equals(cgk)
                && gk.getInt("RBS") != rbs)
              gk.next();
            if (gk.inBounds() && gk.getString("CNALOGA").equals(cgk)) {
              if (gk.getBigDecimal("ID").compareTo(sk.getBigDecimal("ID")) != 0
                  || gk.getBigDecimal("IP").compareTo(sk.getBigDecimal("IP")) != 0)
                addResultRow(results, "Nejednaki iznosi SK <-> GK", sk, gk);
              else if (!ut.getYear(sk.getTimestamp("DATUMKNJ")).equals(
                  ut.getYear(gk.getTimestamp("DATUMKNJ"))))
                addResultRow(results, "Nejednake godine knjiženja SK <-> GK", sk, gk);
              else if (!ut.getMonth(sk.getTimestamp("DATUMKNJ")).equals(
                  ut.getMonth(gk.getTimestamp("DATUMKNJ"))))
                addResultRow(results, "Nejednaki mjeseci knjiženja SK <-> GK", sk, gk);
              else if (!sk.getTimestamp("DATUMKNJ").toString().substring(0, 10)
                  .equals(gk.getTimestamp("DATUMKNJ").toString().substring(0, 10)) && strict)
                addResultRow(results, "Nejednaki datumi knjiženja SK <-> GK", sk, gk);
              gk.emptyRow();
            } else addResultRow(results, "Nepovezana SK stavka - krivi RBS", sk, null);
          } else {
            while (gk.inBounds()  && gk.getString("CNALOGA").equals(cgk)
                && (gk.getBigDecimal("ID").compareTo(sk.getBigDecimal("ID")) != 0 || 
                    gk.getBigDecimal("IP").compareTo(sk.getBigDecimal("IP")) != 0))
              gk.next();
            if (gk.inBounds() && gk.getString("CNALOGA").equals(cgk)) {
              if (!sk.getTimestamp("DATUMKNJ").toString().substring(0, 10).equals(
                  gk.getTimestamp("DATUMKNJ").toString().substring(0, 10)) && strict)
                addResultRow(results, "Nejednaki datumi knjiženja SK <-> GK", sk, gk);
              gk.emptyRow();
            } else
              addResultRow(results, "Nepovezana SK stavka - nepostojeæi iznos", sk, null);
          }
        } else
          addResultRow(results, "Nepovezana SK stavka - nema CGK", sk, null);
      }
      raProcess.checkClosing();
      for (gk.first(); gk.inBounds(); gk.next()) {
        addResultRow(results, "Nepovezana GK stavka", null, gk);
      }
    }
    
    return results;
  }
  
  public static void recreateSaldo(String from, String to, int cpar) {
    System.out.println("Checking consistency from "+from+" to "+to);
    
    raProcess.setMessage("Dohvat stavaka salda konti...", false);
    String scols = simpleDev ? "SSALDO SALDO" : "OZNVAL TECAJ SSALDO SALDO PVSSALDO PVSALDO";
    String ssalcol = colSSaldo(), salcol = colSaldo();
    DataSet sk = Skstavke.getDataModule().getTempSet(
        //KNJIG CPAR BROJKONTA VRDOK BROJDOK BROJIZV 
        "CSKSTAVKE DATUMKNJ ID IP VRDOK "+scols,
        Aus.getKnjigCond().and(Condition.where("POKRIVENO", Condition.NOT_EQUAL, "X")).
            and(Condition.equal("CPAR", cpar)).and(
            Condition.between("DATUMKNJ", ut.getYearBegin(from), ut.getYearEnd(to))));
    sk.open();
    
    for (int i = 0; i < sk.columnCount(); i++)
      System.out.println(sk.getColumn(i));
    
    System.out.println(sk.rowCount() + " rows in skstavke");
    
    Map allSk = new HashMap();
    for (sk.first(); sk.inBounds(); sk.next())
      allSk.put(sk.getString("CSKSTAVKE"), simpleDev ? null : new SalDoc(sk));

    raProcess.setMessage("Dohvat veza pokrivanja dokumenata...", true);
    DataSet pok = Pokriveni.getDataModule().getTempSet();
    pok.open();
    Map poka = new HashMap();
    for (pok.first(); pok.inBounds(); pok.next()) {
      String csku = pok.getString("CUPLATE");
      String cskr = pok.getString("CRACUNA");
      BigDecimal sal = pok.getBigDecimal("IZNOS");
      PokriveniDok du = (PokriveniDok) poka.get(csku);
      PokriveniDok dr = (PokriveniDok) poka.get(cskr);
      
      if (du != null) du.add(new PokDoc(true, cskr, sal));
      else poka.put(csku, new PokriveniDok(new PokDoc(true, cskr, sal)));
      
      if (dr != null) dr.add(new PokDoc(false, csku, sal));
      else poka.put(cskr, new PokriveniDok(new PokDoc(false, csku, sal)));
    }
    pok.close();
    pok = null;

    for (sk.first(); sk.inBounds(); sk.next()) {
      boolean rac = raVrdokMatcher.isRacunTip(sk);
      String csk = sk.getString("CSKSTAVKE");
      BigDecimal ssal = sk.getBigDecimal(ssalcol);
      BigDecimal sal = sk.getBigDecimal(salcol);
      
      PokriveniDok pp = (PokriveniDok) poka.get(csk);
      if (pp != null) {
        if (simpleDev) {
           ssal = ssal.subtract(pp.getTotal(rac));
           if (ssal.compareTo(sal) != 0)
             sk.setBigDecimal("SALDO", ssal);
        } else pp.checkStavka(sk, allSk, true);
      } else if (ssal.compareTo(sal) != 0)
        sk.setBigDecimal("SALDO", ssal);
    }
    sk.saveChanges();
  }
  
  public static void recreateSaldo(final int cpar) {
    raProcess.runChild(new Runnable() {
      public void run() {
        recreateSaldo(
            Valid.getValid().getKnjigYear("gk"), 
            Valid.getValid().findYear(), cpar);
      }
    });
  }
  
  public static void checkSkConsistency() {
    raProcess.runChild(new Runnable() {
      public void run() {
        DataSet results = findConsistencyErrors(
            Valid.getValid().getKnjigYear("gk"), 
            Valid.getValid().findYear(), true);
        raProcess.yield(results);
      }
    });
    if (raProcess.isCompleted()) {
      frmTableDataView view = new frmTableDataView();
      view.setDataSet((StorageDataSet) raProcess.getReturnValue());
      view.setTitle("Rezultati usporedbe SK-GK  za "+Valid.getValid().getKnjigYear("gk")+
          ". godinu");
      view.show();
    }
  }
  
  // raskriva sve skstavke raskrivenog naloga u datasetu gks, i popunjava te
  // stavke odgovarajuæim kolonama iz skstavki, koje nedostaju.
  public static boolean deArchiveSk(QueryDataSet gks) {
    String[] gksk = {"CPAR", "CAGENT", "DATDOSP", "BROJDOK", "VRDOK", "EXTBRDOK", "CNACPL", "CKNJIGE"};
    
    // osvjezi redne brojeve u gks za eventualno pokrivanje
    int rbsrac = 0;
    for (gks.first(); gks.inBounds(); gks.next())
      gks.setInt("RBSRAC", ++rbsrac);
    
    // dohvati sve skstavke koje su nastale proknjizavanjem naloga cije stavke su u gks
    String cnaloga = gks.getString("CNALOGA");
    QueryDataSet sks = Skstavke.getDataModule().getTempSet(
        "cgkstavke like '" + cnaloga + "%'");
    sks.open();
    // pripremi dataset PokriveniRadni gdje æe se ubacivati podaci o pokrivenosti
    QueryDataSet pokr = PokriveniRadni.getDataModule().getTempSet("1=0");
    pokr.open();
    
    // Mapa skstavki koje su pokrivene (potpuno ili djelomièno) sa stavkama
    // ovog naloga. Da bi im se mogao ažurirati status i saldo.    
    HashMap others = new HashMap();
    
    for (sks.first(); sks.inBounds(); sks.next()) {
      String base = raVrdokMatcher.getMatchSide(sks);
      String other = raVrdokMatcher.getOtherSide(sks);
      
      // pronaði redak u gks koji odgovara ovom retku sk. po nastavku iz CGKSTAVKE
      String cgk = sks.getString("CGKSTAVKE");
      String rbs = null;
      
      if (cgk.length() > cnaloga.length() && cgk.charAt(cnaloga.length()) == '-') {
        rbs = cgk.substring(cnaloga.length() + 1);
        if (!lookupData.getlookupData().raLocate(pokr, "RBS", rbs)) rbs = null;
      }
      
      // pronaði sve stavke pokrivene s tekuæom 
      QueryDataSet pok = Pokriveni.getDataModule().getFilteredDataSet(
          Condition.equal(base, sks.getString("CSKSTAVKE")));
      pok.open();
      
      if (pok.rowCount() > 0) {
        // prebaci ih sve u pokriveneradne
        // ali samo ako se moze uspjesno povezati sa gkstavkerad      
        for (pok.first(); pok.inBounds(); pok.next()) {
          String ocsk = pok.getString(other);
          BigDecimal iznos = pok.getBigDecimal("IZNOS");
          if (rbs != null) {
            pokr.insertRow(false);
            pokr.setBigDecimal("IZNOS", iznos);
            pokr.setString(base, findGKCSK(gks));
            pokr.setString(other, ocsk);
          }
          others.put(ocsk, iznos);
        }
        pok.deleteAllRows();
        raTransaction.saveChanges(pok);
      }
      
      // prebaci podatke iz sk u gkrad
      if (rbs != null) dM.copyColumns(sks, gks, gksk);
      
      if (others.size() > 0) {
        QueryDataSet osk = Skstavke.getDataModule().getTempSet(
            Condition.in("CSKSTAVKE", others.keySet().toArray(new String[others.size()])));
        osk.open();
        for (osk.first(); osk.inBounds(); osk.next()) {
          BigDecimal iznos = (BigDecimal) others.get(osk.getString("CSKSTAVKE"));
          matchIznos(sks, osk, iznos.negate());
        }
        raTransaction.saveChanges(osk);
      }
      if (sks.getBigDecimal("SALDO").compareTo(sks.getBigDecimal("SSALDO")) != 0) {
        System.err.println("Greška: raskrivena skstavka ima neispravan saldo");
        System.err.println(sks);
        return false;
      }
    }
    sks.deleteAllRows();
    raTransaction.saveChanges(sks);
    return true;
  }
  
  private static boolean findThisStavka(DataSet filt, ReadRow orig) {
    return lookupData.getlookupData().raLocate(filt,
        new String[] {"BROJIZV", "VRDOK"},
        new String[] {Integer.toString(orig.getInt("BROJIZV")),orig.getString("VRDOK")});
  }

  public static void fixMisingCSK() {
    DataSet ds = Skstavke.getDataModule().getTempSet("cskstavke is null or cskstavke=''");
    ds.open();
    for (ds.first(); ds.inBounds(); ds.next())
      ds.setString("CSKSTAVKE", findCSK(ds));
    ds.saveChanges();
  }

  public static Int2 matchBrojdok(QueryDataSet sks) {
    Int2 ret = new Int2();

//    Skstavke.getDataModule().setFilter(Aus.getKnjigCond().and(
//        Aus.getYearCond("DATUMKNJ", year)).and(
//        Condition.equal("CPAR", String.valueOf(cpar))));
//    QueryDataSet sks = dM.getDataModule().getSkstavke();
    QueryDataSet pok = Pokriveni.getDataModule().getFilteredDataSet("1=0");
//    sks.open();
    pok.open();

    HashMap dokoz = new HashMap();
    for (sks.first(); sks.inBounds(); sks.next()) {
      MatchRow mr = new MatchRow(sks);
      String key = mr.getMasterKey() + "$" + sks.getString("BROJDOK");
      System.out.println("processing: "+key);
      if (!dokoz.containsKey(key)) dokoz.put(key, new PotentialMatch(mr));
      else ((PotentialMatch) dokoz.get(key)).addStavka(mr);
    }
    for (Iterator it = dokoz.keySet().iterator(); it.hasNext();) {
      PotentialMatch pm = (PotentialMatch) dokoz.get(it.next());
      ret = ret.add(pm.realize(sks, pok));
    }
    if (ret.one > 0) try {
      R2Handler.saveChangesInTransaction(new QueryDataSet[] {pok,sks});
    } catch (Exception e) {
      e.printStackTrace();
      ret.one = ret.two = 0;
    }
    dM.getDataModule().getSynchronizer().markAsDirty("pokriveni");
    return ret;
  }

  public static boolean matchIfYouCan(ReadRow skstavka, boolean trans) {
    Skstavke.getDataModule().setFilter(
      Condition.whereAllEqual(new String[] {"KNJIG", "CPAR", "BROJKONTA", "BROJDOK", "OZNVAL"}, skstavka));
    QueryDataSet sks = dM.getDataModule().getSkstavke();
    QueryDataSet pok = Pokriveni.getDataModule().getFilteredDataSet("1=0");
    sks.open();
    pok.open();
    if (!findThisStavka(sks, skstavka)) {
      System.out.println("Silly error?!");
      return false;
    }
    String kyear = ut.getYear(sks.getTimestamp("DATUMKNJ"));
    String lastgk = Valid.getValid().getLastKnjigYear("gk");
    if (lastgk == null) lastgk = "1970";
    PotentialMatch pm = new PotentialMatch();
    for (sks.first(); sks.inBounds(); sks.next())
      if (ut.getYear(sks.getTimestamp("DATUMKNJ")).compareTo(lastgk) >= 0)
        pm.addStavka(sks);

    ArrayList poks = pm.getPokList();
    if (poks.size() > 0) {
      for (int i = 0; i < poks.size(); i++) {
        MatchRow vm = (MatchRow) poks.get(i);
        System.out.println("matching: "+vm);
        lookupData.getlookupData().raLocate(sks, "CSKSTAVKE", vm.getCSK());
        for (int j = 0; j < vm.getPokCount(); j++)
          matchIznos(sks, pok, vm.getPokIznos(j), vm.getPokCsk(j));
      }
      if (trans) {
        raTransaction.saveChanges(pok);
        raTransaction.saveChanges(sks);
        try {
          R2Handler.saveR2Changes();
        } catch (SQLException e) {
          e.printStackTrace();
          return false;
        }
        return true;
      }
      return R2Handler.saveChangesInTransaction(new QueryDataSet[] {pok,sks});
    } 
    return false;

/*    BigDecimal saldo = sks.getBigDecimal("PVSALDO").abs();
    if (saldo.compareTo(sks.getBigDecimal("PVID").add(sks.getBigDecimal("PVIP")).abs()) != 0)
      return false;


    Timestamp datknj = new Timestamp(sks.getTimestamp("DATUMKNJ").getTime());
    String val = sks.getString("OZNVAL");
    raVrdokMatcher mat = new raVrdokMatcher(sks);
    for (sks.first(); sks.inBounds(); sks.next()) {
      if (mat.matches(sks) && "N".equalsIgnoreCase(sks.getString("PVPOK")) &&
          sks.getString("OZNVAL").equals(val) && saldo.compareTo(
          sks.getBigDecimal("PVID").add(sks.getBigDecimal("PVIP")).abs()) == 0 &&
          ut.getYear(sks.getTimestamp("DATUMKNJ")).equals(ut.getYear(datknj))) {
        System.out.println("found match: ");
        System.out.println(skstavka);
        System.out.println(sks);

        matchIznos(sks, pok, saldo, findCSK(skstavka));
        if (trans) {
          raTransaction.saveChanges(pok);
          raTransaction.saveChanges(sks);
          return true;
        } else
          return raTransaction.saveChangesInTransaction(new QueryDataSet[] {pok,sks});
      }
    }
//    System.out.println("didnt find match for:");
//    System.out.println(skstavka);
    return false;*/
  }

  public static boolean matchLast() {
    return matchIfYouCan(lastSks, false);
  }

/*  public static void setKumInvalid() {
    kumInvalid = true;
  }

  public static void checkKumValid() {
    if (kumInvalid) {
      kumInvalid = false;
      dM.getDataModule().getSkkumulativi().refresh();
    }
  }

  public static void addToKumulativPS(ReadRow stavka) {
    addToKumulativ(stavka, true);
  }

  public static void addToKumulativ(ReadRow stavka) {
    addToKumulativ(stavka, false);
  }

  private static void addToKumulativ(ReadRow stavka, boolean ps) {
//    System.out.println(stavka);
    if (stavka.hasColumn(colPok()) != null && 
        stavka.getString(colPok()).equalsIgnoreCase("X")) return;
    
//    checkKumValid();
    QueryDataSet kum = hr.restart.baza.dM.getDataModule().getSkkumulativi();
    String god = ut.getYear(stavka.getTimestamp("DATUMKNJ"));
    String knjig = stavka.getString("KNJIG");
    String corg = stavka.getString("CORG");
    String konto = stavka.getString("BROJKONTA");
    int cpar = stavka.getInt("CPAR");
    String vrdok = stavka.getString("VRDOK"), uloga;
    if (vrdok.equals("IRN") || vrdok.equals("UPL") || vrdok.equals("OKK"))
      uloga = "K";
    else uloga = "D";

    if (!lookupData.getlookupData().raLocate(kum,
        new String[] {"KNJIG", "GODINA", "CORG", "CPAR", "ULOGA", "BROJKONTA"},
        new String[] {knjig, god, corg, String.valueOf(cpar), uloga, konto}
       )) {
      kum.insertRow(false);
      kum.setString("KNJIG", knjig);
      kum.setString("GODINA", god);
      kum.setString("CORG", corg);
      kum.setInt("CPAR", cpar);
      kum.setString("ULOGA", uloga);
      kum.setString("BROJKONTA", konto);
    }
//    if (stavka.getString("CSKSTAVKE").length() == 0)
//      stavka.setString("CSKSTAVKE", findCSK(stavka));
    BigDecimal racun = stavka.getBigDecimal(uloga.equals("K") ? "ID" : "IP");
    BigDecimal uplata = stavka.getBigDecimal(uloga.equals("K") ? "IP" : "ID");
    String rac = ps ? "POCSTRAC" : "PROMETR";
    String upl = ps ? "POCSTUPL" : "PROMETU";
    kum.setBigDecimal(rac, kum.getBigDecimal(rac).add(racun));
    kum.setBigDecimal(upl, kum.getBigDecimal(upl).add(uplata));
    kum.post();
  }*/

  // Popravlja OZNVAL i TECAJ za domicilnu valutu
  public static void fixValute() {
    String val = hr.restart.zapod.Tecajevi.getDomOZNVAL();
    if (val == null || val.length() == 0) {
      JOptionPane.showMessageDialog(null, "Domicilna valuta nije definirana!", "Greška",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }
    Valid.getValid().runSQL(
      "UPDATE skstavke SET oznval='"+val+"', tecaj=1 WHERE oznval in ('','"+val+"')"
    );
    JOptionPane.showMessageDialog(null, "Operacija završena!", "Greška",
                                    JOptionPane.INFORMATION_MESSAGE);
  }
  
  public static QueryDataSet getSkstavkaFromGK(DataSet gk) {
    try {
      if (raKonta.isSaldak(gk.getString("BROJKONTA"))) {
        String cgk = jpBrojNaloga.getCNaloga(gk)+"-"+gk.getInt("RBS");
        QueryDataSet qds = Skstavke.getDataModule().getTempSet(Condition.equal("CGKSTAVKE",cgk));
        qds.open();
        if (qds.getRowCount() == 1) return qds;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /*public static void fixSkstavkerad() {
    raProcess.runChild(startFrame.getStartFrame(), new Runnable() {
      public void run() {
        fixThem();
      }
    });
  }

  private static void fixThem() {
    String[] key = {"KNJIG", "CPAR", "VRDOK", "BROJDOK"};
    QueryDataSet stav = Skstavkerad.getDataModule().getTempSet("1 = 0");
    QueryDataSet all = Skstavkerad.getDataModule().getTempSet(
        Condition.equal("KNJIG", OrgStr.getKNJCORG(false)).
        and(Condition.equal("RBS", 1)));
    all.open();
    int rbs = 0, total = all.rowCount();
    int errTotal = 0, errFix = 0;
    for (all.first(); all.inBounds(); all.next()) {
      raProcess.setMessage("Provjeravam " + (++rbs) + " od " + total + " ...", false);
      Skstavkerad.getDataModule().setFilter(stav, Condition.whereAllEqual(key, all)).open();
      if (all.getString("VRDOK").equalsIgnoreCase("URN") ||
          all.getString("VRDOK").equalsIgnoreCase("IRN")) {
        all.setString("URAIRA", all.getString("VRDOK").substring(0, 1));
        if (all.isNull("DATPRI") && !all.isNull("DATUMKNJ"))
          all.setTimestamp("DATPRI", all.getTimestamp("DATUMKNJ"));
        if (!all.getString("DUGPOT").equalsIgnoreCase("D") &&
            !all.getString("DUGPOT").equalsIgnoreCase("P"))
          all.setString("DUGPOT", all.getBigDecimal("ID").signum() != 0 ? "D" : "P");
      }
      int numz = 0;
      BigDecimal id = n0, ip = n0;
      for (stav.first(); stav.inBounds(); stav.next()) {
        if (stav.getShort("STAVKA") < 6 && stav.getInt("RBS") > 1)
          ++numz;
        else if (stav.getBigDecimal("ID").signum() == 0 &&
            stav.getBigDecimal("IP").signum() == 0)
          numz += (stav.getInt("RBS") == 1 ? 2 : 1);
        else  {
          id = id.add(stav.getBigDecimal("ID"));
          ip = ip.add(stav.getBigDecimal("IP"));
        }
      }
      if (id.compareTo(ip) != 0 || id.signum() == 0) {
        System.out.print("Greška u dokumentu:  ");
        System.out.println(all);
        ++errTotal;
        if (numz == 1) {
          for (stav.first(); stav.inBounds(); stav.next())
            if (stav.getBigDecimal("ID").signum() == 0 &&
                stav.getBigDecimal("IP").signum() == 0 ||
                stav.getShort("STAVKA") < 6 && stav.getShort("STAVKA") > 1) {
              stav.setBigDecimal(id.compareTo(ip) > 0 ? "IP" : "ID", id.subtract(ip).abs());
              stav.setBigDecimal("SALDO", id.subtract(ip).abs());
              System.out.print("  Setiram stavku:  ");
              System.out.println(stav);
              stav.saveChanges();
              ++errFix;
              break;
            }
        } else System.out.println(numz + " nul-stavki, ne mogu popraviti");
      }
    }
    all.saveChanges();
    System.out.println("Ukupno pogrešnih totala: "+errTotal);
    System.out.println("Popravljeno: "+errFix);
  }*/
}
