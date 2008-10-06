/****license*****************************************************************
**   file: raKnjizenjeSK.java
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
package hr.restart.gk;

import java.util.HashSet;
import java.util.Set;

import hr.restart.baza.Condition;
import hr.restart.baza.Pokriveni;
import hr.restart.baza.PokriveniRadni;
import hr.restart.baza.Skstavke;
import hr.restart.baza.dM;
import hr.restart.sk.R2Handler;
import hr.restart.sk.raSaldaKonti;
import hr.restart.sk.raVrdokMatcher;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;


public class raKnjizenjeSK {
  private static raKnjizenjeSK knjizSK;
  private hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
  private QueryDataSet shk = dM.getDataModule().getShkonta();
  private StorageDataSet errorSet = new StorageDataSet();

  private QueryDataSet pok;
  private DataSet skpok;
//  private String copycolumns = new String[] {"KNJIG","CPAR"};
  protected raKnjizenjeSK() {
    errorSet.addColumn("ERRTXT","Opis greške",Variant.STRING);
    errorSet.getColumn("ERRTXT").setWidth(300);
    errorSet.open();
  }
  public static raKnjizenjeSK getRaKnjizenjeSK() {
    if (knjizSK == null) knjizSK = new raKnjizenjeSK();
    return knjizSK;
  }

//  private QueryDataSet getSKStavke(String knjig, int cpar) {
//    QueryDataSet sks = Skstavke.getDataModule().getTempSet(
//        "knjig = '"+knjig+"' AND cpar = "+cpar
//    );
//    sks.open();
//    return sks;
//  }

  private String[] gkCols = new String[] {
    "KNJIG", "CPAR", "VRDOK", "BROJDOK", "EXTBRDOK", "CORG", "DATUMKNJ", "DATDOK",
    "DATDOSP", "BROJIZV", "CNACPL", "OPIS", "DEVID", "DEVIP", "TECAJ", "ID", "IP",
    "BROJKONTA"
  };

  private String[] skCols = new String[] {
    "KNJIG", "CPAR", "VRDOK", "BROJDOK", "EXTBRDOK", "CORG", "DATUMKNJ", "DATDOK",
    "DATDOSP", "BROJIZV", "CNACPL", "OPIS", "ID", "IP", "TECAJ", "PVID", "PVIP",
    "BROJKONTA"
  };

  public static String[] skKeyNames =
    {"KNJIG", "CPAR", "VRDOK", "BROJKONTA", "BROJDOK", "BROJIZV"};

  public String findCSK(DataSet sks) {
    return raSaldaKonti.findCSK(sks);
/*
    return sks.getString("KNJIG")+"|"+
           sks.getInt("CPAR")+"|"+
           sks.getShort("STAVKA")+"|"+
           sks.getString("CSKL")+"|"+
           sks.getString("VRDOK")+"|"+
           sks.getString("BROJDOK")+"|"+
           sks.getInt("BROJIZV");
*/
  }
  public void clearErrors() {
    errorSet.empty();
  }
  public boolean copyGKuSK(frmIzvodi fiz, boolean pokriv) {
    return copyGKuSK(fiz.getDetailSet(), pokriv, fiz.ziro, fiz.oznval);
  }

  /**
   * Pokrece udjuturativno knjizenje vise stavaka u sk, priprema npr. skkumulative
   * @return dal je uspio il nije
   */
  public boolean openSKknjizenje() {
    try {
      clearErrors();
      /*pokrad = PokriveniRadni.getDataModule().getTempSet();
      porad.open();*/
      pok = Pokriveni.getDataModule().getTempSet("1=0");
      pok.open();
      skpok = Skstavke.getDataModule().getTempSet("1=0");
      skpok.open();
      //raSaldaKonti.setKumInvalid();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Kopira jednu stavku od zadanog reda (gk)
   * @param gk ReadRow za kopiranje u sk
   * @param pokriv pokriti automatsko pokrivanje
   * @param ziro Ziro racun
   * @param val oznaka valute, ako je null uzima iz gkstavke.getString(OZNVAL)
   * @return jel uspio il nije
   */
  public boolean copyGKstavka(ReadRow gk, boolean pokriv, String ziro, String val) {
    try {
      return copyStavka(gk, pokriv, ziro, val == null ? gk.getString("OZNVAL") : val);
    } catch (Exception e) {
      e.printStackTrace();
      //raSaldaKonti.setKumInvalid();
      return false;
    }
  }

  /**
   * snima sve nastalo nakon openSKknjizenje i niza copyGKstavka
   * @return jel uspio il nije
   */
  public boolean saveSKknjizenje() {
    try {
      //hr.restart.util.raTransaction.saveChanges(dM.getDataModule().getSkkumulativi());
      hr.restart.util.raTransaction.saveChanges(pok);
      //hr.restart.util.raTransaction.saveChanges(pokrad);
      //ai: Nis ne kuzim ali se ubacujem sa:
      R2Handler.saveR2Changes();
      
      if (skpok.rowCount() > 0)
        for (skpok.first(); skpok.inBounds(); skpok.next())
          raSaldaKonti.matchIfYouCan(skpok, true);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      //raSaldaKonti.setKumInvalid();
      return false;
    }

  }

  private boolean copyStavka(ReadRow gk, boolean pokriv, String ziro, String oznval) {
    if (hr.restart.zapod.raKonta.isSaldak(gk.getString("BROJKONTA"))) {
      QueryDataSet sk = Skstavke.getDataModule().getTempSet(Condition.nil);
      /*
          Condition.equal("KNJIG", gk.getString("KNJIG")).
          and(Condition.equal("OZNVAL", oznval)).
          and(Condition.equal("CPAR", gk.getInt("CPAR"))));*/
      sk.open();

      // Probaj na\u0107i skstavku sa potencijalno istim klju\u010Dem,
      // ako na\u0111eš, što onda? Zasad preska\u010Demo...
      /** @todo pitati Andreja što onda? */
      if (Skstavke.getDataModule().getRowCount(Condition.whereAllEqual(skKeyNames, gk)) > 0) {
        errMessage("Dokument broj "+gk.getString("BROJDOK")+" od partnera "+
                   String.valueOf(gk.getInt("CPAR")) +
                   " ve\u0107 postoji u evidenciji kupaca i dobavlja\u010Da");
        return false;
      }

      // Ubaci novu stavku
      sk.insertRow(false);

      // Ve\u0107inu polja kopiraj direktno, bez filozofiranja
      DataSet.copyTo(gkCols, gk, skCols, sk);

      // Postavi saldo
      sk.setBigDecimal("SALDO", sk.getBigDecimal("ID").add(sk.getBigDecimal("IP")));
      sk.setBigDecimal("SSALDO", sk.getBigDecimal("SALDO"));
      sk.setBigDecimal("PVSALDO", sk.getBigDecimal("PVID").add(sk.getBigDecimal("PVIP")));
      sk.setBigDecimal("PVSSALDO", sk.getBigDecimal("PVSALDO"));

      // Traži neku stavku sheme koja definira konto iz gkstavke.
      if (findCertainStavkaShemeMatchingSomeKontoAndVrdok(
          gk.getString("BROJKONTA"), gk.getString("VRDOK")))   {
        // Ubaci polja veze s kontom
        sk.setString("CSKL", shk.getString("CSKL"));
        sk.setShort("STAVKA", shk.getShort("STAVKA"));
      }

      // Ziro i valuta
      sk.setString("ZIRO", ziro);
      sk.setString("OZNVAL", oznval);

      // Zmenglaj neko \u010Dudno polje cgkstavke = cnaloga+rbs - VRLO VAZAN PODATAK za daljnju vezu gkstavke - skstavke (ai)
      sk.setString("CGKSTAVKE", gk.getString("CNALOGA")+"-"+gk.getInt("RBS"));

      // Zmenglaj klju\u010D ove skstavke u polje cskstavke i zapamti ga zbog pokrivanja
      String thiscsk = findCSK(sk);
      sk.setString("CSKSTAVKE", thiscsk);

      //ako broj izvoda nije unesen stavi ga na 0
      if (sk.isNull("BROJIZV") || sk.getInt("BROJIZV") == 0) {
        sk.setInt("BROJIZV", 0);
      }
      if (sk.hasColumn("KOMP") != null && gk.getString("POKRIVENO").equalsIgnoreCase("K"))
        sk.setString("KOMP", "D");
      sk.post();
      
      /*if ("00".equals(gk.getString("CVRNAL")))
        raSaldaKonti.addToKumulativPS(sk);
      else raSaldaKonti.addToKumulativ(sk);*/

      // provjeri radno pokrivanje
      String cbase = raVrdokMatcher.getMatchSide(gk);
      String cthis = raVrdokMatcher.getOtherSide(gk);

      QueryDataSet pokrad = PokriveniRadni.getDataModule().getTempSet(
      		Condition.equal(cbase, raSaldaKonti.findGKCSK(gk)));
      pokrad.open();
      Set csks = new HashSet();
      for (pokrad.first(); pokrad.inBounds(); pokrad.next()) {
      	if (pokrad.getString(cthis).equals(thiscsk)) {
          errMessage("Greška prilikom pokrivanja dokumenta broj "+gk.getString("BROJDOK")+
                     " od partnera "+String.valueOf(gk.getInt("CPAR")));
      	}
      	csks.add(pokrad.getString(cthis));
      }
      QueryDataSet psk = Skstavke.getDataModule().getTempSet(
      		Condition.in("CSKSTAVKE", csks.toArray()));
      psk.open();
      for (pokrad.first(); pokrad.inBounds(); pokrad.next()) {
      	if (!ld.raLocate(psk, "CSKSTAVKE", pokrad.getString(cthis))) {
          errMessage("Dokument broj "+gk.getString("BROJDOK")+" od partnera "+
                     String.valueOf(gk.getInt("CPAR")) + " je pokriven s nepostojeæim dokumentom");
        } else {
          raSaldaKonti.matchIznos(sk, psk, pok, pokrad.getBigDecimal("IZNOS"));
        }
      }
      pokrad.deleteAllRows();
      hr.restart.util.raTransaction.saveChanges(pokrad);
      hr.restart.util.raTransaction.saveChanges(psk);
      
      /*while (ld.raLocate(pokrad, cbase, raSaldaKonti.findGKCSK(gk))) {
        if (pokrad.getString(cthis).equals(thiscsk)) {
          errMessage("Greška prilikom pokrivanja dokumenta broj "+gk.getString("BROJDOK")+
                     " od partnera "+String.valueOf(gk.getInt("CPAR")));
        } else if (!ld.raLocate(sk, "CSKSTAVKE", pokrad.getString(cthis))) {
          errMessage("Dokument broj "+gk.getString("BROJDOK")+" od partnera "+
                     String.valueOf(gk.getInt("CPAR")) + " je pokriven s nepostojeæim dokumentom");
        } else {
          raSaldaKonti.matchIznos(sk, pok, pokrad.getBigDecimal("IZNOS"), thiscsk);
        }
        pokrad.deleteRow();
      }*/
      
      hr.restart.util.raTransaction.saveChanges(sk);
      if (pokriv && ld.raLocate(sk, "CSKSTAVKE", thiscsk) && sk.getBigDecimal("PVSALDO").signum() != 0) {
        skpok.insertRow(false);
        sk.copyTo(skpok);
      }
    }
    return true;
  }

  public boolean copyGKuSK(QueryDataSet gksrad, boolean pokriv, String fiz_ziro, String fiz_oznval) {
    if (!openSKknjizenje()) return false;
    gksrad.open();
    for (gksrad.first(); gksrad.inBounds(); gksrad.next())
      if (!copyGKstavka(gksrad, pokriv, fiz_ziro, fiz_oznval)) return false;
    if (!saveSKknjizenje()) return false;
    return true;
  }

//hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
//ST.prn(gksrad);
/*      gksrad.first();
      while (gksrad.inBounds()) {
        if (hr.restart.zapod.raKonta.isSaldak(gksrad.getString("BROJKONTA"))) {


          // Probaj na\u0107i skstavku sa potencijalno istim klju\u010Dem,
          // ako na\u0111eš, što onda? Zasad preska\u010Demo...
          if (Skstavke.getDataModule().getRowCount(Condition.whereAllEqual(skKeyNames, gksrad)) == 0) {

            // Ubaci novu stavku
            skstavke.insertRow(false);

            // Ve\u0107inu polja kopiraj direktno, bez filozofiranja
            DataSet.copyTo(gkCols, gksrad, skCols, skstavke);

            // Postavi saldo
            skstavke.setBigDecimal("SALDO", skstavke.getBigDecimal("ID").add(
                                            skstavke.getBigDecimal("IP")));
            skstavke.setBigDecimal("SSALDO", skstavke.getBigDecimal("SALDO"));
            skstavke.setBigDecimal("PVSALDO", skstavke.getBigDecimal("PVID").add(
                                              skstavke.getBigDecimal("PVIP")));
            skstavke.setBigDecimal("PVSSALDO", skstavke.getBigDecimal("PVSALDO"));

            // Traži neku stavku sheme koja definira konto iz gkstavke.
            if (findCertainStavkaShemeMatchingSomeKontoAndVrdok(
              gksrad.getString("BROJKONTA"), gksrad.getString("VRDOK")))   {
              // Ubaci polja veze s kontom
              skstavke.setString("CSKL", shk.getString("CSKL"));
              skstavke.setShort("STAVKA", shk.getShort("STAVKA"));
            }

            // Kopiraj polja iz izvoda ovih gkstavkirad fIzvodi dependant!!!!!
            skstavke.setString("ZIRO", fiz_ziro);
            skstavke.setString("OZNVAL", fiz_oznval);

            // Zmenglaj neko \u010Dudno polje cgkstavke = cnaloga+rbs
            skstavke.setString("CGKSTAVKE", gksrad.getString("CNALOGA")+"-"+gksrad.getInt("RBS"));

            // Zmenglaj klju\u010D ove skstavke u polje cskstavke i zapamti ga zbog pokrivanja
            String thiscsk = findCSK(skstavke);
            skstavke.setString("CSKSTAVKE", thiscsk);
            newsks.add(thiscsk);

            //ako broj izvoda nije unesen stavi ga na 0
            if (skstavke.isAssignedNull("BROJIZV") || skstavke.isUnassignedNull("BROJIZV") || skstavke.getInt("BROJIZV") == 0) {
              skstavke.setInt("BROJIZV", 0);
            }
            skstavke.post();
            if ("00".equals(gksrad.getString("CVRNAL")))
              raSaldaKonti.addToKumulativPS(skstavke);
            else raSaldaKonti.addToKumulativ(skstavke);

            // provjeri radno pokrivanje
            String cbase = raVrdokMatcher.getMatchSide(gksrad);
            String cthis = raVrdokMatcher.getOtherSide(gksrad);

            while (ld.raLocate(pokrad, cbase, raSaldaKonti.findGKCSK(gksrad))) {
              if (pokrad.getString(cthis).equals(thiscsk)) {
                errMessage("Greška prilikom pokrivanja dokumenta broj "+gksrad.getString("BROJDOK")+
                           " od partnera "+String.valueOf(gksrad.getInt("CPAR")));
              } else if (!ld.raLocate(skstavke, "CSKSTAVKE", pokrad.getString(cthis))) {
                errMessage("Dokument broj "+gksrad.getString("BROJDOK")+" od partnera "+
                String.valueOf(gksrad.getInt("CPAR")) + " je pokriven s nepostojeæim dokumentom");
              } else {
                raSaldaKonti.matchIznos(skstavke, pok, pokrad.getBigDecimal("IZNOS"), thiscsk);
              }
              pokrad.deleteRow();
            }

//System.out.println("skstaveeeeeeeeeeeeeeeee.....");
//ST.prnc(skstavke);
          } else {
            errMessage("Dokument broj "+gksrad.getString("BROJDOK")+" od partnera "
                       +String.valueOf(gksrad.getInt("CPAR")) + " ve\u0107 postoji u evidenciji kupaca i dobavlja\u010Da");
            return false;
          }
        }
        gksrad.next();
      }
      if (skstavke.getRowCount() == 0) {
        errMessage("Nema podataka za pokrivanje i prijenos u salda konti!");
        return true;
      }
      //ovo za svaku stavke: raSaldaKonti.addToKumulativ(skstavke);
      //skstavke.saveChanges();
      //dM.getDataModule().getSkkumulativi().saveChanges();
//      System.err.println("copyGKuSK: saving everything "+(System.currentTimeMillis()-tim));
      hr.restart.util.raTransaction.saveChanges(skstavke);
      hr.restart.util.raTransaction.saveChanges(dM.getDataModule().getSkkumulativi());
      hr.restart.util.raTransaction.saveChanges(pok);
      hr.restart.util.raTransaction.saveChanges(pokrad);
//      if (pokriv) raSaldaKonti.matchThemAll(skstavke);
//      System.err.println("copyGKuSK: matching "+(System.currentTimeMillis()-tim));
      if (pokriv) {
        for (Iterator it = newsks.iterator(); it.hasNext(); ) {
          String csk = (String) it.next();
          if (ld.raLocate(skstavke, "CSKSTAVKE", csk))
            hr.restart.sk.raSaldaKonti.matchIfYouCan(skstavke, true);
        }
      }
//      System.err.println("copyGKuSK: done "+(System.currentTimeMillis()-tim));
      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
      errMessage(ex.getMessage());
      return false;
    }
  }*/

  private boolean findCertainStavkaShemeMatchingSomeKontoAndVrdok(String konto, String vrdok) {
    return ld.raLocate(shk, new String[] {"VRDOK", "BROJKONTA"}, new String[] {vrdok, konto});
  }

  public void errMessage(String err) {
    errorSet.insertRow(false);
    errorSet.setString("ERRTXT",err);
    errorSet.post();
  }

  public void showErrors() {
    javax.swing.JDialog jDErr = new hr.restart.swing.JraDialog();
    hr.restart.util.raJPTableView jptv = new hr.restart.util.raJPTableView();
    jptv.setDataSet(errorSet);
    jptv.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    jDErr.getContentPane().setLayout(new java.awt.BorderLayout());
    jDErr.getContentPane().add(jptv,java.awt.BorderLayout.CENTER);
    jptv.initKeyListener(jDErr);
    jptv.getColumnsBean().initialize();
    hr.restart.util.startFrame.getStartFrame().centerFrame(jDErr,0,"Pregled grešaka pri prijenosu u SK");
    hr.restart.util.startFrame.getStartFrame().showFrame(jDErr);
  }
  public boolean hasErrors() {
    return (errorSet.getRowCount() > 0);
  }
}

