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
      {"sisfun.numberMask", "calc", "Vrsta numerièke maske (calc/old)", LOCAL, BOOT, OPEN, ""},
      {"sisfun.showToggleTable", "N", "Prikazati uopce gumbic >>Promijeni tablicni prikaz<< (D/N)", LOCAL, BOOT, SPEC, BOOL},
      {"sisfun.smartResize", "false", "Resizanje ekrana ovisno odabranom tablicnom ili detaljnom prikazu", LOCAL, QUICK, SPEC, BOOL},
      {"sisfun.speedSearch", "0", "Naèin prekapèanja brze pretrage s poèetka na sredinu (0,1,2)", LOCAL, QUICK, SPEC, ""},
      {"sisfun.srchmodrplc0", "", "Koji search mode u JlrNavFieldu podmetnuti umjesto 0?", LOCAL, QUICK, OPEN, ""},
      {"sisfun.srchmodrplc1", "1", "Koji search mode u JlrNavFieldu podmetnuti umjesto 1?", LOCAL, QUICK, OPEN, ""},
      {"sisfun.webSync", "N", "Web sinkronizacija (D,N)", SYSTEM, BOOT, SPEC, BOOL},
      
      {"zapod.dosp", "7", "Broj dana dospjeæa za partnera po defaultu", LOCAL, QUICK, SPEC, NUM},
      {"zapod.mbUnique", "N", "Forsirati jedinstvenost matiènog broja partnera (D,N)?", GLOBAL, QUICK, SPEC, BOOL},
      {"zapod.parToKup", "N", "Dodati/brisati slog kupca kod unosa/brisanja partnera (D,N,A)?", GLOBAL, QUICK, SPEC, BOOL},
      {"zapod.prHisPAr", "N", "Prikaz menu opcije \"Povijest Partnera\"", GLOBAL, BOOT, SPEC, BOOL},
      {"zapod.teleserv", "Telemarketer", "Caption telemarketera na formi partnera", GLOBAL, BOOT, SPEC, ""},
      {"zapod.zrUnique", "N", "Forsirati jedinstvenost žiro raèuna partnera (D,N)?", GLOBAL, QUICK, SPEC, BOOL},
      
      {"sisfun.globalFont", "", "Ime fonta koji zamjenjuje Lucida Bright na ispisu", LOCAL, QUICK, OPEN, ""}
  };  
}
