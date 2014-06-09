/****license*****************************************************************
**   file: lookupFrame.java
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
package hr.restart.util;

import hr.restart.baza.dM;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.borland.dx.dataset.DataRow;


public class ParamHandler {
  
  public static String GLOBAL = "GLOBAL";
  public static String SYSTEM = "SYSTEM";
  public static String LOCAL = "LOCAL";
  
  public static String BOOT = "BOOT";
  public static String QUICK = "QUICK";
  
  public static String SPEC = "SPEC";
  public static String OPEN = "OPEN";
  
  public static String TEXT = "TEXT";
  public static String BOOL = "TEXT";
  public static String NUM = "NUM";
  public static String DEC = "DEC";

  
  public static final ParamHandler inst = new ParamHandler();
  private Map cache = new HashMap();
  private Map trace = new HashMap();
  private Map params = new HashMap();
  
  private ParamHandler() {
    createParamList();
  }
  
  public static void init() {
    //
  }

  public static boolean bool(String tag) {
    return ((Boolean) inst.cached(tag)).booleanValue();
    
  }
  
  public static int num(String tag) {
    return ((Integer) inst.cached(tag)).intValue();
  }
  
  public static BigDecimal dec(String tag) {
    return (BigDecimal) inst.cached(tag);
  }
  
  public static String text(String tag) {
    return (String) inst.cached(tag);
  }
  
  public static boolean option(String tag, String opt) {
    return opt.equalsIgnoreCase(text(tag));
  }
  
  private Object cached(String tag) {
    if (!trace.containsKey(tag)) {
      StackFrame sf = Aus.getStackFrame();
      while (sf != null && sf.getClassName().equals("ParamHandler")) sf = sf.getParent();
      Set all = new HashSet();
      while (sf != null) {
        if (sf.getPackageName().startsWith("hr.restart"))
          all.add(sf.getPackageName() + "." + sf.getClassName());
        sf = sf.getParent();
      }
      trace.put(tag, all);
    }
      
    Object val = cache.get(tag);
    if (val == null) val = fetch(tag);
    return val;
  }
  
  private synchronized Object fetch(String tag) {
    try {
      dM dm = dM.getDataModule();
      String val = null;
    
      DataRow param = lookupData.getlookupData().raLookup(dm.getParametri(),
                        new String[] {"APP","PARAM"}, new VarStr(tag).split('.'));
      if (param == null) val = createParam(tag);
      else {
        val = param.getString("VRIJEDNOST");
        updateParam(tag, param);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  private String createParam(String tag) {
    return "";
  }
  
  private void updateParam(String tag, DataRow old) {
    
  }
  
  
  private void createParamList() {
    
    
    for (int i = 0; i < table.length; i++)
      params.put(table[i][0], table[i]);
  }

  // parametri
  
  public boolean defined(String tag) {
    return params.containsKey(tag);
  }
  
  private String[][] table = new String[][] {
      {"robno.priceChIzl", "D", "Dopustiti izmjenu cijena na OTP, MEI, INM, OTR...(D/N)", GLOBAL, QUICK, SPEC, BOOL},
      {"robno.iznosChange", "N", "Dapustiti izmjenu iznosa s porezom (D/N)", GLOBAL, QUICK, SPEC, BOOL},
      
      {"gk.skRaz", "N", "Opcija za knjiženje razlika u saldu SK dokumenata (D,N)", GLOBAL, BOOT, SPEC, BOOL},
      
      {"pos.skladPos", "N", "Da li se POS vodi na nivou skladišta (D) ili prodajnog mjesta", GLOBAL, BOOT, SPEC, BOOL},
      
      {"robno.FISBIH", "N", "Koristi li se fiskalizacija za BiH? (D/N)", GLOBAL, BOOT, SPEC, BOOL},
      {"robno.cijenaDec", "2", "Broj decimala za cijenu na izlazu (2-4)", GLOBAL, BOOT, SPEC, NUM},
      {"robno.distron", "N", "Koristiti modul distribucije D/N", GLOBAL, BOOT, SPEC, BOOL},
      {"robno.ediCskl", "", "Šifra OJ ili skladišta za EDI narudžbe", GLOBAL, QUICK, SPEC, ""},
      {"robno.ediOrder", "N", "Omoguæiti import narudžbi kupca preko EDI (D,N,I)", GLOBAL, BOOT, SPEC, BOOL},
      {"robno.ediUlaz", "N", "Panel za unos HCCP podataka na ulazu (D,N)", GLOBAL, BOOT, SPEC, BOOL},
      {"robno.isLikeFr", "N", "Agenti i telemarketeri postoje u firmi (D,N)", GLOBAL, BOOT, SPEC, BOOL},
      {"robno.lotOpcija", "N", "Omoguæiti unos lota na dokumente s vanjskog ureðaja (D,N)", GLOBAL, BOOT, SPEC, BOOL},
      {"robno.skladDec", "2", "Broj decimala za skladišne cijene (2-4)", GLOBAL, BOOT, SPEC, NUM},
      {"robno.ulazValDec", "2", "Broj decimala za valutne iznose na ulazu (2-4)", GLOBAL, BOOT, SPEC, NUM},
      {"robno.ROTzarada", "D", "Izraèunati stvarnu zaradu na ROT-u (D,N)", GLOBAL, QUICK, SPEC, BOOL},
      {"robno.akcijaPrefiks", "", "Prefiks naziva artikla za artikle na akciji (prazno za onemogucavanje)", GLOBAL, QUICK, SPEC, ""},
      {"robno.cartSize", "0", "Broj mjesta za upis oznake artikala (0 za proizvoljno)", GLOBAL, BOOT, OPEN, NUM},
      {"robno.cartSizeBC", "0", "Broj mjesta za upis barkoda artikala (0 za proizvoljno)", GLOBAL, BOOT, OPEN, NUM},
      {"robno.checkLimit", "D", "Provjera limita kreditiranja", GLOBAL, QUICK, SPEC, BOOL},
      {"robno.defCskl", "", "Defaultno skladište", LOCAL, QUICK, OPEN, ""},
      {"robno.focusCart", "SIFRA", "", LOCAL, QUICK, OPEN, "SIFRA=Po šifri|NAZIV=Po nazivu"},
      {"robno.gotPar", "N", "Gotovinski raèuni za partnere (D,N)", GLOBAL, BOOT, SPEC, BOOL},
      {"robno.idpartner", "N", "ID parner instaliran na dokumentima (D,N)", GLOBAL, BOOT, SPEC, BOOL},
      {"robno.indiCart", "CART1", "Indikator naèina unosa artikla (CART, CART1 ili BC)", LOCAL, QUICK, OPEN, "CART=Po Šifri|CART1=Po oznaci|BC=Po barkodu"},
      {"robno.ispLogo", "N", "Ispis loga na dokumentima (D/N)", GLOBAL, QUICK, OPEN, BOOL},
      {"robno.kontKalk", "D", "Kontrola ispravosti redoslijeda unosa dokumenata", GLOBAL, QUICK, SPEC, BOOL},
      {"robno.lazyIZDpre", "N", "Dopustiti prebacivanje radnih naloga u predatnice prije izdavanja robe? (D,N)", GLOBAL, QUICK, SPEC, BOOL},
      {"robno.megaEdi", "N", "Dodati opciju za export u megatrend (D,N)", GLOBAL, BOOT, SPEC, BOOL},
      {"robno.nabDirect", "N", "Moguænost unosa nabavnog iznosa na raèunima (D,N)", GLOBAL, BOOT, SPEC, BOOL},
      {"robno.pnbBrWid", "0", "Minimalna širina broja dokumenta u pozivu na broj (vodeæe nule)", GLOBAL, BOOL, SPEC, NUM},
      {"robno.prikazKol", "N", "Prikaz kolièina na rapancartu (D/N)", GLOBAL, BOOT, OPEN, BOOL},
      {"robno.userCheck", "D", "Da li se provjerava korisnik kod izmjene dokumenta (D/N)", GLOBAL, QUICK, SPEC, BOOL},
      
      {"sisfun.OKfocus", "N", "Dopustiti fokusiranje OK dugmeta na OK-panelu (D,N)", LOCAL, QUICK, OPEN, BOOL},
      {"sisfun.alterAlpha", "10", "Faktor zastupljenosti boje svakog drugog reda (1-100)", LOCAL, QUICK, OPEN, NUM},
      {"sisfun.alterCol", "gray", "Boja pozadine svakog drugog reda (ime ili hex)", LOCAL, QUICK, OPEN, ""},
      {"sisfun.calcMask", "N", "Kalkulator na svim decimalnim maskama (D/N)", LOCAL, QUICK, SPEC, BOOL},
      {"sisfun.crmDriver", "", "Driver za CRM", SYSTEM, BOOT, SPEC, ""},
      {"sisfun.crmPass", "", "Password za CRM bazu", SYSTEM, BOOT, SPEC, ""},
      {"sisfun.crmURL", "", "Url za CRM bazu", SYSTEM, BOOT, SPEC, ""},
      {"sisfun.crmUser", "sa", "User za CRM bazu", SYSTEM, BOOT, SPEC, ""},
      {"sisfun.keepRow", "D", "Zapamtiti zadnji red kod prošlog dohvata (D,N)", LOCAL, QUICK, OPEN, BOOL},
      {"sisfun.mojiDok", "D", "Da li je na user predselekcijama inicijalno odabran 'Moji dokumenti' (D/N)", LOCAL, QUICK, OPEN, BOOL},
      {"sisfun.mojiDokEn", "D", "Smije li na user predselekcijama odabrati 'Moji dokumenti' ili 'Svi dokumenti' (D/N)", GLOBAL, BOOT, OPEN, BOOL},
      {"sisfun.msgTimer", "2000", "Interval provjera poruka u milisekundama", LOCAL, BOOT, SPEC, NUM},
      {"sisfun.numberMask", "calc", "Vrsta numerièke maske (calc/old)", LOCAL, BOOT, OPEN, "calc=Moderna|old=Klasièna"},
      {"sisfun.showToggleTable", "N", "Prikazati uopce gumbic >>Promijeni tablicni prikaz<< (D/N)", LOCAL, BOOT, SPEC, BOOL},
      {"sisfun.smartResize", "false", "Resizanje ekrana ovisno odabranom tablicnom ili detaljnom prikazu", LOCAL, QUICK, SPEC, BOOL},
      {"sisfun.speedSearch", "0", "Naèin prekapèanja brze pretrage s poèetka na sredinu (0,1,2)", LOCAL, QUICK, SPEC, "0=Pomoæu zvjezdice|1=Automatski|2=Uvijek ukljuèeno"},
      {"sisfun.srchmodrplc0", "", "Koji search mode u JlrNavFieldu podmetnuti umjesto 0?", LOCAL, QUICK, OPEN, ""},
      {"sisfun.srchmodrplc1", "1", "Koji search mode u JlrNavFieldu podmetnuti umjesto 1?", LOCAL, QUICK, OPEN, ""},
      {"sisfun.webSync", "N", "Web sinkronizacija (D,N)", SYSTEM, BOOT, SPEC, BOOL},
      {"sisfun.spaceSel", "N", "Oznaèavanje redova samo sa razmaknicom (D,N)?", LOCAL, QUICK, OPEN, BOOL},
      {"sisfun.srchmodrplc2", "", "Koji search mode u JlrNavFieldu podmetnuti umjesto 2?", LOCAL, QUICK, SPEC, ""},
      {"sisfun.srchmodrplc3", "", "Koji search mode u JlrNavFieldu podmetnuti umjesto 3?", LOCAL, QUICK, SPEC, ""},
      {"sisfun.localCVSTime", "D", "Uzeti lokalno vrijeme promjene fajla, umjesto datuma iz CVS/Entries? (D,N)", LOCAL, QUICK, SPEC, BOOL},
      
      {"zapod.dosp", "7", "Broj dana dospjeæa za partnera po defaultu", LOCAL, QUICK, SPEC, NUM},
      {"zapod.mbUnique", "N", "Forsirati jedinstvenost matiènog broja partnera (D,N)?", GLOBAL, QUICK, SPEC, BOOL},
      {"zapod.parToKup", "N", "Dodati/brisati slog kupca kod unosa/brisanja partnera (D,N,A)?", GLOBAL, QUICK, SPEC, BOOL},
      {"zapod.prHisPAr", "N", "Prikaz menu opcije \"Povijest Partnera\"", GLOBAL, BOOT, SPEC, BOOL},
      {"zapod.teleserv", "Telemarketer", "Caption telemarketera na formi partnera", GLOBAL, BOOT, SPEC, ""},
      {"zapod.zrUnique", "N", "Forsirati jedinstvenost žiro raèuna partnera (D,N)?", GLOBAL, QUICK, SPEC, BOOL},
      {"zapod.vert2Chooser", "N", "Vertikalna varijanta izbornika s dvije tablice (D/N)", LOCAL, QUICK, SPEC, BOOL},
      
      {"robno.allowMinus", "N", "Dopustiti odlazak u minus na izlazima (D,N)?", GLOBAL, QUICK, SPEC, BOOL},
      {"robno.autoFixPor", "N", "Automatska provjera ukupnog poreza (D,N)?", LOCAL, QUICK, SPEC, BOOL},
      {"robno.autoValuta", "N", "Preraèunati iznos iz valute u kune na izlazima (N,D,A)", GLOBAL, QUICK, SPEC, ""},
      {"robno.chStanjeRiG", "N", "Provjera stanja kod GRN i RAC -a", GLOBAL, QUICK, SPEC, BOOL},
      {"robno.defNacpl", "", "Predefinirani naèin plaæanja", GLOBAL, QUICK, SPEC, ""},
      {"robno.defcrab", "", "Predefinirana šifra rabata stavke", GLOBAL, QUICK, SPEC, ""},
      {"robno.docBefDatKnj", "N", "Dozvoliti izradu dokumenta u periodu koje je veæ knjižen (D,N) ", GLOBAL, QUICK, SPEC, BOOL},
      {"robno.dospDatdok", "N", "Dospjeæe se raèuna od datuma dokumenta (D,N)", GLOBAL, QUICK, SPEC, BOOL},
      {"robno.minkol", "Provjera minimalne kolièine na stanju (D,N)", "N", GLOBAL, QUICK, SPEC, BOOL},
      {"robno.popustuFMC", "N", "Popust s artikla uraèunati u FMC (D,N)", GLOBAL, QUICK, SPEC, BOOL},
      {"robno.rezkol", "N", "Automatsko rezerviranje kolièina putem ponude (D,N,O)", GLOBAL, QUICK, SPEC, ""},
      {"robno.rezkol4Stanje", "O", "Kalkulacija stanja kod rezervacije (D/N/O)", GLOBAL, QUICK, SPEC, ""},
      {"robno.sigkol", "Provjera signalne kolièine na stanju (D,N)", "N", GLOBAL, QUICK, SPEC, BOOL},
      {"robno.singleKOL", "N", "Defaultna kolièina od 1 kom (D,N)?", GLOBAL, QUICK, SPEC, BOOL},
      
      {"sisfun.localCVSTime", "D", "Uzeti lokalno vrijeme promjene fajla, umjesto datuma iz CVS/Entries? (D,N)", LOCAL, QUICK, SPEC, BOOL},
      {"sisfun.printerRMcmnd", "1", "Radno mjesto za matrièni ispis", LOCAL, QUICK, SPEC, ""},
      {"sisfun.sortgetview", "D", "Sortirati podatke kod dohvata (F9) po polju u kojem je stisnuto (D,N)", GLOBAL, QUICK, OPEN, BOOL},
      {"sisfun.trepHandled", "D", "Ukljuciti 'teperenje hack'? (D/N)", LOCAL, QUICK, SPEC, BOOL},
      
      {"sk.autoIncExt", "D", "Automatsko poveæavanje dodatnog broja URA/IRA (D/N)", GLOBAL, QUICK, SPEC, BOOL},
      {"sk.defcorgUIRN", "D", "Da nudi knjigovodstvo kao defaultni corg pri unosu URA/IRA (D,N)", GLOBAL, QUICK, SPEC, BOOL},
      {"sk.directKonto", "N", "Unos konta na ulaznim racunima (D/N)", LOCAL, QUICK, SPEC, BOOL},
      {"sk.displayExt", "N", "Prikaži kolonu dodatnog broja dokumenta na kartici SK (D/N)", LOCAL, QUICK, SPEC, BOOL},
      {"sk.extKnjiga", "D", "Ima li svaka knjiga zaseban brojaè (D/N)", GLOBAL, QUICK, SPEC, BOOL},
      {"sk.extSize", "0", "Minimalna velicina broja URA/IRA (popunjavanje vedeæim nulama)", GLOBAL, QUICK, SPEC, ""},
      {"sk.extendedMatch", "N", "Pokrivanje po dodatnom broju (D/N)", LOCAL, QUICK, SPEC, BOOL},
      {"sk.nazparext", "N", "Prikazati naziv partnera zajedno sa adresom i telefonom (D/N)", GLOBAL, QUICK, SPEC, BOOL},
      {"sk.saldoTecaj", "U", "Saldo teèajnih razlika ostaviti na raèunima ili uplatama (R,U)?", GLOBAL, QUICK, SPEC, ""},
      {"sk.simpleDev", "N", "Pojednostavljeno rukovanje deviznim saldom (D/N)", GLOBAL, QUICK, SPEC, BOOL},

      //
      {"robno.allowMinusU", "N", "Dopustiti odlazak u minus na ulazima (D,N)?", GLOBAL, QUICK, SPEC, BOOL},
      {"robno.kalkchVC", "N", "Promjena izlazne cijene na kalkulacijama (D,N,M)?", GLOBAL, QUICK, SPEC, ""},
      {"robno.preselDatum", "D", "Poèetni datum na predselekciji u robnom (D,M,Y)?", LOCAL, QUICK, SPEC, ""},
      {"robno.prkFromNar", "N", "Omoguæiti prebacivanje narudžbe u primku (D,N)", GLOBAL, QUICK, SPEC, BOOL},
      {"robno.recalcPRK", "N", "Omoguæiti rekalkulaciju primke sa stavkama (D/N)", GLOBAL, QUICK, SPEC, BOOL},
      {"robno.updcijeneulaz", "N", "Ažurirati cijenu na artiklu kod ulaza (D,N)", GLOBAL, QUICK, SPEC, BOOL},
      {"sisfun.webApikey", "", "ApiKey za web sync", GLOBAL, QUICK, SPEC, ""},
      {"sisfun.webMainSkl", "", "Skladište za web artikle", GLOBAL, QUICK, SPEC, ""},
      {"sisfun.webOJ", "", "Org.jed web sync", GLOBAL, QUICK, SPEC, ""},
      {"sisfun.webSklads", "", "Skladišta za web sync", GLOBAL, QUICK, SPEC, ""},
      //
      
      {"sisfun.globalFont", "", "Ime fonta koji zamjenjuje Lucida Bright na ispisu", LOCAL, QUICK, OPEN, ""}
  };  
}
