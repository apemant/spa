/****license*****************************************************************
**   file: raKonta.java
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
package hr.restart.zapod;

import hr.restart.baza.Konta;
import hr.restart.baza.dM;
import hr.restart.util.Stopwatch;

import java.util.HashMap;
import java.util.Map;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;

public class raKonta {
  private static String lastSearchedKonto;
  private static Konto current;
  
  private static Map konta;
  private static int serialKonto = -1;
/*  private static String SALDAK = "";
  private static String KARAKTERISTIKA = "";
  private static String ORGSTR = "";
  private static String VRSTAKONTA = "";*/
//  private static com.borland.dx.sql.dataset.QueryDataSet anKonta;
  protected raKonta() {
  }
  public static void clearBuffer() {
    lastSearchedKonto = null;
  }
  public static com.borland.dx.sql.dataset.QueryDataSet getAnalitickaKonta() {
 /*   if (anKonta == null) {
      hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
      hr.restart.util.Util Ut = hr.restart.util.Util.getUtil();
      anKonta = new com.borland.dx.sql.dataset.QueryDataSet();
      Aus.setFilter(anKonta, "SELECT * FROM KONTA WHERE VRSTAKONTA = 'A' ORDER by BROJKONTA");
      anKonta.setColumns(Ut.cloneCols(dm.getKonta().getColumns()));
    }
    anKonta.refresh();
    anKonta.open();
    return anKonta;*/
    return dM.getDataModule().getKontaAnalitic();
  }
  private static void getKonto(String brKonta) {
    /*
    hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
    vl.execSQL("SELECT BROJKONTA,SALDAK,KARAKTERISTIKA,ORGSTR,VRSTAKONTA FROM konta where BROJKONTA='"+brKonta+"'");
    vl.RezSet.open();
    if (vl.RezSet.getRowCount()==0) throw new IllegalArgumentException("raKonta: nepostojeci broj konta "+brKonta);
    initRaKonta(vl.RezSet);*/
    int nowSerial = dM.getSynchronizer().getSerialNumber("Konta");
    if (nowSerial != serialKonto || konta == null) {
      Stopwatch st = Stopwatch.start("loading konta");
      DataSet ds = Konta.getDataModule().getTempSet();
      ds.open();
      serialKonto = nowSerial;
      konta = new HashMap();
      for (ds.first(); ds.inBounds(); ds.next())
        konta.put(ds.getString("BROJKONTA"), new Konto(ds));
      st.report("loaded");
    }
    
    current = (Konto) konta.get(brKonta);
    if (current == null)
      throw new IllegalArgumentException("raKonta: nepostojeci broj konta "+brKonta);
    lastSearchedKonto = brKonta;
  }
/**
 * inicijalizira klasu raKonta sa zadanim slogom tako da boolean metode bez parametara imaju smisla
 * @param ds dataset koji sadrzi polja BROJKONTA,SALDAK,KARAKTERISTIKA,ORGSTR,VRSTAKONTA
 */
/*  public static void initRaKonta(com.borland.dx.dataset.ReadRow row) {
    try {
      lastSearchedKonto = row.getString("BROJKONTA");
      SALDAK = row.getString("SALDAK");
      KARAKTERISTIKA = row.getString("KARAKTERISTIKA");
      ORGSTR = row.getString("ORGSTR");
      VRSTAKONTA = row.getString("VRSTAKONTA");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }*/

///
/**
 * @param brKonta BROJKONTA za koji se vrsi provjera
 * @return da li je SALDAK = K za zadani konto
 */
  public static boolean isKupac(String brKonta) {//SALDAK = 'K')
    getKonto(brKonta);
    return isKupac();
  }
/**
 * @param brKonta BROJKONTA za koji se vrsi provjera
 * @return da li je SALDAK = D za zadani konto
 */
  public static boolean isDobavljac(String brKonta) {//SALDAK = 'D')
    getKonto(brKonta);
    return isDobavljac();
  }
/**
 * @param brKonta BROJKONTA za koji se vrsi provjera
 * @return da li je SALDAK = K ili D za zadani konto
 */
  public static boolean isSaldak(String brKonta) {//SALDAK in ('K','D'))
    getKonto(brKonta);
    return isSaldak();
  }

/**
 * @param brKonta BROJKONTA za koji se vrsi provjera
 * @return da li je SALDAK != K ili D za zadani konto
 */
  public static boolean isFinancijski(String brKonta) {
    getKonto(brKonta);
    return isFinancijski();
  }
/**
 * @param brKonta BROJKONTA za koji se vrsi provjera
 * @return da li je KARAKTERISTIKA = D ili O za zadani konto
 */
  public static boolean isDugovni(String brKonta) {//KARAKTERISTIKA in ('D','O')
    getKonto(brKonta);
    return isDugovni();
  }
/**
 * @param brKonta BROJKONTA za koji se vrsi provjera
 * @return da li je KARAKTERISTIKA = P ili O za zadani konto
 */
  public static boolean isPotrazni(String brKonta) {//KARAKTERISTIKA in ('P','O')
    getKonto(brKonta);
    return isPotrazni();
  }
/**
 * @param brKonta BROJKONTA za koji se vrsi provjera
 * @return da li je ORGSTR = D za zadani konto
 */
  public static boolean isOrgStr(String brKonta) {//ORGSTR = 'D'
    getKonto(brKonta);
    return isOrgStr();
  }
  
  public static boolean isOrgStrPS(String brKonta) {//ORGSTR = 'D'
    getKonto(brKonta);
    return isOrgStrPS();
  }
/**
 * @param brKonta BROJKONTA za koji se vrsi provjera
 * @return da li je zadani konto ANALITICKI (KARAKTERISTIKA = 'A')
 */
  public static boolean isAnalitik(String brKonta) {
    getKonto(brKonta);
    return isAnalitik();
  }
/**
 * @param brKonta BROJKONTA za koji se vrsi provjera
 * @return da li je zadani konto SINTETI\u010CKI (KARAKTERISTIKA = 'S')
 */
  public static boolean isSintetic(String brKonta) {
    getKonto(brKonta);
    return isSintetik();
  }
  
  public static boolean isZbirni(String brKonta) {
    getKonto(brKonta);
    return isZbirni();
  }

//
/**
 * Vraca vrijednosti od zadnjeg konta pretrazivanog sa metodama sa brKonta parametrom
 * ili inicijaliziranog sa initRaKonta(ReadRow)
 * @return da li je VRSTAKONTA = A
 */
  public static boolean isAnalitik() {
    return current.analitic;
  }
/**
 * Vraca vrijednosti od zadnjeg konta pretrazivanog sa metodama sa brKonta parametrom
 * ili inicijaliziranog sa initRaKonta(ReadRow)
 * @return da li je KARAKTERISTIKA != A
 */
  public static boolean isSintetik() {
    return !current.analitic;
  }
/**
 * Vraca vrijednosti od zadnjeg konta pretrazivanog sa metodama sa brKonta parametrom
 * ili inicijaliziranog sa initRaKonta(ReadRow)
 * @return da li je SALDAK = K
 */
  public static boolean isKupac() {//SALDAK = 'K')
    return current.kup;
  }
/**
 * Vraca vrijednosti od zadnjeg konta pretrazivanog sa metodama sa brKonta parametrom
 * ili inicijaliziranog sa initRaKonta(ReadRow)
 * @return da li je SALDAK = D
 */
  public static boolean isDobavljac() {//SALDAK = 'D')
    return current.dob;
  }
/**
 * Vraca vrijednosti od zadnjeg konta pretrazivanog sa metodama sa brKonta parametrom
 * ili inicijaliziranog sa initRaKonta(ReadRow)
 * @return da li je SALDAK = K ili D
 */
  public static boolean isSaldak() {//SALDAK in ('K','D'))
    return current.kup || current.dob;
  }
/**
 * Vraca vrijednosti od zadnjeg konta pretrazivanog sa metodama sa brKonta parametrom
 * ili inicijaliziranog sa initRaKonta(ReadRow)
 * @return da li je SALDAK = F
 */
  public static boolean isFinancijski() {
    return !current.kup && !current.dob;
  }
/**
 * Vraca vrijednosti od zadnjeg konta pretrazivanog sa metodama sa brKonta parametrom
 * ili inicijaliziranog sa initRaKonta(ReadRow)
 * @return da li je KARAKTERISTIKA = D ili O
 */
  public static boolean isDugovni() {//KARAKTERISTIKA in ('D','O')
    return current.dug;
  }
/**
 * Vraca vrijednosti od zadnjeg konta pretrazivanog sa metodama sa brKonta parametrom
 * ili inicijaliziranog sa initRaKonta(ReadRow)
 * @return da li je KARAKTERISTIKA = P ili O
 */
  public static boolean isPotrazni() {//KARAKTERISTIKA in ('P','O')
    return current.pot;
  }
/**
 * Vraca vrijednosti od zadnjeg konta pretrazivanog sa metodama sa brKonta parametrom
 * ili inicijaliziranog sa initRaKonta(ReadRow)
 * @return da li je ORGSTR = D
 */
  public static boolean isOrgStr() {//ORGSTR = 'D'
    return current.ojPrip;
  }
  
  public static boolean isOrgStrPS() {//ORGSTR = 'D'
    return current.ojPripPS;
  }
  
  public static boolean isZbirni() {
    return current.zbir;
  }

///
/**
 * @return Oznaku zadnjeg konta pretrazivanog sa metodama sa brKonta parametrom
 * ili inicijaliziranog sa initRaKonta(ReadRow)
 */
  public static String getBROJKONTA() {
    return lastSearchedKonto;
  }
  
  public static String getNazivKonta(String konto) {
    getKonto(konto);
    return current.naziv;
  }

/*  public static String getKARAKTERISTIKA() {
    return KARAKTERISTIKA;
  }
  
  public static String getORGSTR() {
    return ORGSTR;
  }

  public static String getSALDAK() {
    return SALDAK;
  }

  public static String getVRSTAKONTA() {
    return VRSTAKONTA;
  } */
  
  static class Konto {
    String naziv;
    boolean analitic;
    boolean kup, dob;
    boolean dug, pot;
    boolean zbir;
    boolean ojPrip;
    boolean ojPripPS;
    
    public Konto(ReadRow row) {
      naziv = row.getString("NAZIVKONTA");
      analitic = row.getString("VRSTAKONTA").equalsIgnoreCase("A");
      
      String sk = row.getString("SALDAK");
      kup = sk.equalsIgnoreCase("K");
      dob = sk.equalsIgnoreCase("D");
      
      String kar = row.getString("KARAKTERISTIKA");
      dug = kar.equalsIgnoreCase("D");
      pot = kar.equalsIgnoreCase("P");
      if (kar.equalsIgnoreCase("O"))
        dug = pot = true;
      
      zbir = !row.getString("NACPR").equalsIgnoreCase("P");
      ojPrip = row.getString("ORGSTR").equalsIgnoreCase("D");
      ojPripPS = row.getString("PSORGSTR").equalsIgnoreCase("D");
    }
  }
}