/****license*****************************************************************
**   file: raObrKamata.java
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
import hr.restart.baza.Kamate;
import hr.restart.baza.Konta;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raDelayWindow;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCpar;
import hr.restart.swing.raButtonGroup;
import hr.restart.swing.raDateRange;
import hr.restart.util.*;
import hr.restart.util.reports.raRunReport;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class raObrKamata extends raFrame implements ResetEnabled {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();

  static raObrKamata rok;

  raRunReport RepRun;

  JPanel jpDetail = new JPanel();

  JPanel jpC = new JPanel();
  XYLayout xyC = new XYLayout();

  JPanel jpO = new JPanel();
  XYLayout xyO = new XYLayout();

  JPanel jpM = new JPanel();
  XYLayout xyM = new XYLayout();

  JPanel jpV = new JPanel();
  XYLayout xyV = new XYLayout();

  XYLayout lay = new XYLayout();

  jpCpar jpp = new jpCpar(100, 310, true, false) {
    protected void kupSelected() {
      super.kupSelected();
      if (isDirect()) checkKupDob(true);
    }
    protected void dobSelected() {
      super.dobSelected();
      if (isDirect()) checkKupDob(false);
    }
  };
  
/*  raComboBox rcbCpar = new raComboBox() {
    public void this_itemStateChanged() {
      if (this.getSelectedIndex() == 0) kupSelected();
      else dobSelected();
    }
  };
  JlrNavField jlrCpar = new JlrNavField();
  JlrNavField jlrNazpar = new JlrNavField();

  JraButton jbSelCpar = new JraButton(); */

  JLabel jlKam = new JLabel();
  JlrNavField jlrKam = new JlrNavField();
  JlrNavField jlrOpis = new JlrNavField();
  JraButton jbKam = new JraButton();

  JLabel jlCorg = new JLabel();
  JlrNavField jlrCorg = new JlrNavField();
  JlrNavField jlrNaziv = new JlrNavField();
  JraButton jbCorg = new JraButton();

  JLabel jlDan = new JLabel();
  JLabel jlIznos = new JLabel();
  JraTextField jraIznos = new JraTextField();
  JraTextField jraOd = new JraTextField();
  JraTextField jraDo = new JraTextField();

  JLabel jlRac = new JLabel();
  raButtonGroup bg1 = new raButtonGroup();
  JraRadioButton jrbPla = new JraRadioButton();
  JraRadioButton jrbNepla = new JraRadioButton();
  JraRadioButton jrbSvi = new JraRadioButton();

  JLabel jlMet = new JLabel();
  raButtonGroup bg2 = new raButtonGroup();
  JraRadioButton jrbProg = new JraRadioButton();
  JraRadioButton jrbLin = new JraRadioButton();
  JraRadioButton jrbKomb = new JraRadioButton();

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      if (!busy)
        OKPress();
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };
  
  public jpSelKonto jpk = new jpSelKonto(100, 310, true) {
    public void afterLookUp(boolean succ) {
      afterKonto(succ);
    }
  };

  // DataSet za ekranske komponente
  StorageDataSet fset = new StorageDataSet();

  // DataSet-ovi za master i detail tablice kamatnih stopa
  QueryDataSet kamtab, stope;

  // DataSet za broj nepokrivenih uplata po partneru. Za
  // odredjivanje kojim partnerima se ne obracunavaju kamate
  // na neplacene racune
  QueryDataSet nepokriveneUplate;

  // Glavna tablica koja se popunjava podacima za kamate
  // po racunima, uplatama i periodima kamatnih stopa
  QueryDataSet table = new QueryDataSet() {
    public boolean saveChangesSupported() {
      return false;
    }
  };
  
  QueryDataSet table2 = new QueryDataSet() {
    public boolean saveChangesSupported() {
      return false;
    }
  };

  // Podaci sa predselekcije
  boolean busy, kupac, placeni, neplaceni, outside, standalone;
  int cpar, ckam, brojRacuna, brojStavke;
  String nazpar, corg, naziv;
  Timestamp datumOd, datumDo;
  BigDecimal minIznos;

  // DataSet-ovi za vanjsko pozivanje procedure za
  // obracun kamata
  DataSet outsideRac, outsideUpl;

  // metoda obracuna: 1 - konformna, 2 - proporcionalna, 3 - kombinirana
  int obrMetoda;
  
  Condition kon;

  public static final int METHOD_PROGRESSIVE = 1;
  public static final int METHOD_LINEAR = 2;
  public static final int METHOD_COMBINED = 3;

  /**
   * Staticki getter klase.
   * @return singleton instancu.
   */
  public static raObrKamata getInstance() {
    if (rok == null) new raObrKamata();
    return rok;
  }

  public boolean isBusy() {
    return busy;
  }

  public boolean isStandAlone() {
    return standalone;
  }

  public void setStandAlone(boolean yesno) {
    if (standalone != yesno) {
      standalone = yesno;
      jpDetail.remove(jpC);
      jpDetail.remove(jpO);
      if (standalone) {
        lay.setHeight(165);
        jpDetail.add(jpO, new XYConstraints(0, 45, -1, -1));
      } else {
        lay.setHeight(190 + (isDirect() ? 25 : 0));
        jpDetail.add(jpC, new XYConstraints(0, 45, -1, -1));
        jpDetail.add(jpO, new XYConstraints(0, 70 + (isDirect() ? 25 : 0), -1, -1));
      }
    }
  }

  /**
   * Vraca tablicu kamata (za ispis).
   * @return tablicu kamata.
   */
  public QueryDataSet getRepQDS() {
    return table;
  }
  
  public QueryDataSet getRepQDS2() {
    return table2;
  }

  public int getCPAR() {
    return cpar;
  }

  public String getNAZPAR() {
    return nazpar;
  }

  public Timestamp getDATUMOD() {
    return datumOd;
  }

  public Timestamp getDATUMDO() {
    return datumDo;
  }


  public raObrKamata() {
    try {
      rok = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  protected boolean isDirect() {
    return !standalone && raSaldaKonti.isDirect();
  }

  public raRunReport getRepRunner() {
    if (RepRun == null) {
      RepRun = raRunReport.getRaRunReport();
      RepRun.setOwner(this.getWindow(), getClass().getName());
    }
    return RepRun;
  }

  boolean firstTime = true;
  public void SetFokus() {
    if (firstTime) resetDefaults();
  }
  
  public void resetDefaults() {
    firstTime = false;
    kamtab.refresh();
    if (kamtab.rowCount() > 0) {
      fset.setInt("CKAM", kamtab.getInt("CKAM"));
      jlrKam.forceFocLost();
//      fset.setString("OPIS", kamtab.getString("OPIS"));
    }
    if (!standalone) {
      fset.setString("CORG", OrgStr.getKNJCORG(false));
      jlrCorg.forceFocLost();
    }
    fset.setTimestamp("DATUMOD", ut.getFirstDayOfYear(vl.getToday()));
    fset.setTimestamp("DATUMDO", vl.getToday());
  }

  public boolean Validacija() {
    if (vl.isEmpty(jlrKam) || (!standalone && vl.isEmpty(jlrCorg))) return false;
    if (fset.getTimestamp("DATUMOD").after(fset.getTimestamp("DATUMDO"))) {
      jraOd.requestFocus();
      JOptionPane.showMessageDialog(jraOd,
        "Po\u010Detni datum ve\u0107i od završnog !","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }

    // Prepisi podatke sa predselekcije
    kon = jpk.getCondition();
    corg = fset.getString("CORG");
    ckam = fset.getInt("CKAM");
    cpar = fset.getInt("CPAR");
    nazpar = fset.getString("NAZPAR");
    naziv = fset.getString("NAZIV");
    datumOd = ut.getFirstSecondOfDay(fset.getTimestamp("DATUMOD"));
    datumDo = ut.getLastSecondOfDay(fset.getTimestamp("DATUMDO"));
    minIznos = fset.getBigDecimal("MINIZ");
    kupac = jpp.isKupci();
    placeni = jrbPla.isSelected() || jrbSvi.isSelected();
    neplaceni = jrbNepla.isSelected() || jrbSvi.isSelected();
    return true;
  }

  boolean fromBeg = false;
  private void process() {
    raDelayWindow proc = raDelayWindow.show(this.getWindow(), 100).setInterruptable(true);
    outside = false;

    try {
      fromBeg = frmParam.getParam("sk", "placFromBeg", "P", "Kamate na plaæene raèune " +
            "raèunaju se od dospjeæa ili od poèetka perioda (D,P)?").equalsIgnoreCase("D");
      findKamate();     // Popuni tablicu kamata
      proc.close();
    } catch (AssertionNotTrueException e) {
      proc.close();
      JOptionPane.showMessageDialog(this.getWindow(), "Nepotpuna tablica kamata!",
                                    "Greška", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
      // Nepredvidjena greska
      proc.close();
      e.printStackTrace();
    }
    // Ako je delaywindow zatvoren, prekini odmah
    if (proc.isInterrupted()) return;

    // Provjeri ima li nepokrivenih uplata za bilo kojeg partnera i prikazi
    // upozorenje ako ima
    boolean nepokriveno = false;
    if (nepokriveneUplate != null)
      for (nepokriveneUplate.first(); nepokriveneUplate.inBounds(); nepokriveneUplate.next())
        if (nepokriveneUplate.getInt("NUM") > 0) nepokriveno = true;

    if (table.getRowCount() == 0) {
      if (nepokriveno)
        JOptionPane.showMessageDialog(this.getWindow(), (cpar > 0 ? "Za partnera nema" : "Nema") +
                                    " plaæenih zakašnjelih ra\u010Duna, a neplaæeni se\n"+"ne mogu obraèunati "+
                                    "jer postoje nepokrivene uplate!", "Obavijest", JOptionPane.WARNING_MESSAGE);
      else JOptionPane.showMessageDialog(this.getWindow(), (cpar > 0 ? "Za partnera nema" : "Nema") +
                                    " nepla\u0107enih ili zakašnjelih ra\u010Duna!",
                                    "Obavijest", JOptionPane.INFORMATION_MESSAGE);
      return;
    }


    if (nepokriveno)
      JOptionPane.showMessageDialog(this.getWindow(), (cpar > 0 ? "Za partnera" : "Za neke partnere")+
        " ima nepokrivenih uplata,\n pa se za "+(cpar > 0 ? "njega" : "njih")+" ne obra\u010Dunavaju"+
        " kamate na nepla\u0107ene ra\u010Dune!", "Upozorenje", JOptionPane.WARNING_MESSAGE);

    getRepRunner().go();
//    sysoutTEST sys = new sysoutTEST(false);
//    sys.prn(table);
  }

  /**
   * Postavlja DataSet-ove za popis racuna i uplata (s pokrivanjem)
   * za izvanjski poziv obracuna. DataSet racuna mora imati kolone:
   * CPAR (partner), BROJDOK (broj racuna), DATDOSP (datum dospjeca),
   * IZNOS (dugovni ili potrazni iznos, ovisno o vrsti partnera)
   * te CRACUNA (jedinstveni kljuc stavke). DataSet stavki mora imati
   * kolone BROJDOK (broj uplate), DATDOK (datum uplate), IZNOS (iznos
   * uplate) i CRACUNA (CSKSTAVKE racuna koje uplata pokriva).
   *
   * @param racuni DataSet racuna.
   * @param uplate DataSet uplata.
   */
  public void setData(DataSet racuni, DataSet uplate) {
    outsideRac = racuni;
    outsideUpl = uplate;
    nepokriveneUplate = null;
    outside = true;
  }

  // Obracunava jedan period (od 'begin' do 'end') iz tablice kamatnih stopa za
  // jednu uplatu, iznosa 'glavnica'. Kamatna stopa mora biti u postocima za
  // zadani broj dana (365 ili 366 ako je stopa godisnja, ili 28, 29, 30 ili
  // 31 ako je stopa mjesecna). Metoda popunjava red tablice kamata i vraca
  // iznos kamate za ovaj period. Metoda obracuna je 'konformna', ili
  // 'proporcionalna' ovisno o parametru obrMetoda.
  
  private BigDecimal processPeriod(Timestamp begin, Timestamp end, BigDecimal glavnica,
                                  BigDecimal stopa, int dana, int obrMet) {
    table.setTimestamp("DATDOSP", begin);
    table.setTimestamp("DATPLAC", end);
    table2.setTimestamp("DATPLAC", end);
    table.setBigDecimal("STOPA", stopa);
    table2.setBigDecimal("STOPA", stopa);
    table.setBigDecimal("GLAVNICA", glavnica);
    table2.setBigDecimal("GLAVNICA", glavnica);
    begin = ut.getFirstSecondOfDay(begin);
    end = ut.getFirstSecondOfDay(end);

    // Izracunaj kasnjenje u periodu
    int kasni = Math.round(ut.getHourDifference(begin, end) / (float) 24);
    table.setInt("DANA", kasni);
    table2.setInt("DANA", kasni);
    
    // Stopa null oznacava da je ovo grupni period u kojem ima vise kamatnih
    // stopa, pa se neke vrijednosti ne popunjavaju
    if (stopa != null) {
      double koef = (obrMet == METHOD_PROGRESSIVE)
        ? Math.pow(1 + stopa.movePointLeft(2).doubleValue(), (double) kasni / dana) - 1
        : stopa.movePointLeft(2).doubleValue() * kasni / dana;
      table.setBigDecimal("KOEF", new BigDecimal(koef));
      table.setBigDecimal("KAMATA", new BigDecimal(glavnica.doubleValue() * koef).
                          setScale(2, BigDecimal.ROUND_HALF_UP));
      table2.setBigDecimal("KOEF", table.getBigDecimal("KOEF"));
      table2.setBigDecimal("KAMATA", table.getBigDecimal("KAMATA"));
      return table.getBigDecimal("KAMATA");
    } else return raSaldaKonti.n0;
  }

  private void addRacRow(DataSet rac) {
    int cpar = rac.getInt("CPAR");
    String brdok = rac.getString("BROJDOK");
    BigDecimal iznos = rac.getBigDecimal("IZNOS");
    Timestamp dosp = rac.getTimestamp("DATDOSP");
    table2.insertRow(false);
    table2.setInt("CPAR", cpar);
    table2.setString("BROJDOK", brdok);
    table2.setBigDecimal("GLAVNICA", iznos);
    table2.setString("FLAG", "R");
    table2.setTimestamp("DATDOSP", dosp);
    table2.setInt("DANA", -1);
    table2.setInt("RBR", ++brojRacuna);
    table2.setInt("LRBR", brojStavke = 1);
  }

  // Dodaje jedan red u tablicu kamata. Flag 'first' oznacava prvi red
  // svakog racuna.
  private void addTableRow(DataSet rac, String upl, boolean first) {

    // Prepisuje podatke o partneru iz DataSeta 'rac', jer to moze
    // biti referenca na DataSet 'table', ciji se kurzor ovdje pomice.
    int cpar = rac.getInt("CPAR");
    String brdok = rac.getString("BROJDOK");
    if (first) addRacRow(rac);
    table.insertRow(false);
    table.setInt("CPAR", cpar);
    table.setString("BROJDOK", brdok);
    table.setString("BROJUPL", upl);
    table.setString("FLAG", "O");
    table2.insertRow(false);
    table2.setInt("CPAR", cpar);    
    table2.setString("BROJUPL", upl);
    table2.setString("FLAG", "O");

    // Brojaci RBR i LRBR sluze za sortiranje racuna i uplata iz Elixira.
    // RBR Oznacava redni broj racuna, a LRBR redni broj stavke unutar
    // rastava racuna po uplatama i periodima kamatnih stopa.
    
    table.setInt("RBR", brojRacuna);
    table.setInt("LRBR", brojStavke);
    table2.setInt("RBR", brojRacuna);
    table2.setInt("LRBR", ++brojStavke);
  }

  // Obracunava jedan period iz tablice kamatnih stopa za jednu uplatu,
  // vodeci racuna o metodi obracuna.
  private BigDecimal processPeriod2(Timestamp begin, Timestamp end,
                                    Timestamp endGodina, BigDecimal glavnica,
                                    BigDecimal orig, BigDecimal stopa, int dana) {
    int MO = obrMetoda;
    BigDecimal kamata = raSaldaKonti.n0;
    if (MO == METHOD_COMBINED) {
      if (endGodina.before(ut.getFirstSecondOfDay(begin)))
        MO = METHOD_LINEAR;
      else if (end.before(ut.getFirstSecondOfDay(endGodina)))
        MO = METHOD_PROGRESSIVE;
      else {
        MO = METHOD_PROGRESSIVE;
        kamata = processPeriod(begin, endGodina, glavnica, stopa, dana, MO);
        addTableRow(table, table.getString("BROJUPL"), false);
        begin = new Timestamp(endGodina.getTime());
        MO = METHOD_LINEAR;
      }
    }

    kamata = kamata.add(processPeriod(begin, end,
      MO == METHOD_PROGRESSIVE ? glavnica : orig, stopa, dana, MO));

    return kamata;
  }

  // Izracunava kamatu za jednu uplatu nekog racuna dospjeca 'dosp', placenu na
  // dan 'plac', u iznosu od 'glavnica'.
  private BigDecimal processUplata(Timestamp dosp, Timestamp plac, BigDecimal glavnica) {

    // Premotaj kurzor tablice kamatnih stopa.
    stope.first();

    // Postavi pocetak perioda i datum prve kamatne stope.
    Timestamp beginPeriod = dosp;
    Timestamp beginStopa = ut.getFirstSecondOfDay(stope.getTimestamp("DATUM"));

    // Datum jedne godine kasnjenja, za kombiniranu metodu
    Timestamp endGodina = ut.addYears(dosp, 1);

    // Zahtjevaj da je tablica kamatnih stopa definirana od pocetka
    // obracunskog perioda.
    Assert.is(!beginPeriod.before(beginStopa));

    BigDecimal stopa = raSaldaKonti.n0, total = raSaldaKonti.n0, kamata;
    BigDecimal origGlavnica = glavnica;
    int dana = 0;
    boolean multiPeriod = false;

    // Prolazi kroz tablicu kamatnih stopa sve dok je pocetak perioda
    // kamatne stope unutar ili ispred obracunskog perioda.
    for (stope.first(); stope.inBounds() && (beginStopa = 
         ut.getFirstSecondOfDay(stope.getTimestamp("DATUM"))).before(plac); stope.next()) {
//      System.out.println(beginPeriod + "   " + beginStopa);
      if (beginPeriod.before(beginStopa)) {
        if (!multiPeriod && (multiPeriod = true))
          processPeriod(dosp, plac, glavnica, null, 0, 0);

        addTableRow(table, table.getString("BROJUPL"), false);
        kamata = processPeriod2(beginPeriod, beginStopa, endGodina, glavnica,
                                origGlavnica, stopa, dana);

        glavnica = glavnica.add(kamata);
        total = total.add(kamata);
        beginPeriod = new Timestamp(beginStopa.getTime());
      }
      stopa = stope.getBigDecimal("STOPA");
      dana = stope.getInt("DANI");
    }
    if (multiPeriod) addTableRow(table, table.getString("BROJUPL"), false);
    return total.add(processPeriod2(beginPeriod, plac, endGodina, glavnica,
                                    origGlavnica, stopa, dana));
  }

  private DataSet getUplate() {
    if (outside) return outsideUpl;
    
    Condition minimumCond = Aus.getKnjigCond().and(jpp.getCondition()); 
    
    /*      "knjig = '"+hr.restart.zapod.OrgStr.getKNJCORG(false)+
                             (cpar > 0 ? "' AND cpar="+cpar : "'");*/

    nepokriveneUplate = null;
    if (standalone)
      return ut.getNewQueryDataSet("SELECT brojdok, datdok, iznos, cracuna, iznos as signi " +
            "FROM kamupl WHERE "+minimumCond+" AND datdok <= '"+datumDo+
             "' ORDER BY cracuna,datdok", true);

    if (neplaceni) {
        nepokriveneUplate = ut.getNewQueryDataSet("SELECT cpar, COUNT(*) AS num"+
            " FROM skstavke WHERE "+minimumCond+(kon == Condition.none ? "" : " AND " + kon) +
            " AND "+(kupac ? /* vrdok="+ (kupac ? "'UPL'" : "'IPL'")+*/
            "(VRDOK='UPL' OR (VRDOK='OKK' AND (IP > 0 OR ID < 0)))" :    
            "(VRDOK='IPL' OR (VRDOK='OKD' AND (ID > 0 OR IP < 0)))")+
            " AND skstavke.datumknj >= '"+ut.getYearBegin(Aus.getFreeYear())+
            "' AND skstavke.datdok <= '"+datumDo+
            "' AND saldo > 0 GROUP BY cpar", true);
        if (nepokriveneUplate.rowCount() == 1 && !placeni &&
            nepokriveneUplate.getInt("NUM") > 0)
          return null;
      }
      return ut.getNewQueryDataSet(
             "SELECT skstavke.brojdok, skstavke.datdok, skstavke.oznval, "+
             " pokriveni.iznos, pokriveni.cracuna, skstavke.ssaldo as signi, " +
             " skstavke.tecaj, 'RAC' as strana FROM skstavke,pokriveni"+
             " WHERE "+minimumCond + (kon == Condition.none ? "" : " AND " + kon) +
             " AND "+(kupac ? /* vrdok="+ (kupac ? "'UPL'" : "'IPL'")+*/
                 "(VRDOK='UPL' OR (VRDOK='OKK' AND (IP > 0 OR ID < 0)))" :    
                 "(VRDOK='IPL' OR (VRDOK='OKD' AND (ID > 0 OR IP < 0)))")+
             " AND skstavke.cskstavke = pokriveni.cuplate AND skstavke.datdok <= '"+
             datumDo+"'"+raSaldaKonti.getCorgAndPripCondition(corg)+
             " UNION ALL "+
             "SELECT skstavke.brojdok, skstavke.datdok, skstavke.oznval, "+
             " pokriveni.iznos, pokriveni.cuplate as cracuna, skstavke.ssaldo as signi, " +
             " skstavke.tecaj, 'UPL' as strana FROM skstavke,pokriveni"+
             " WHERE "+minimumCond + (kon == Condition.none ? "" : " AND " + kon) +
             " AND "+(kupac ? /* vrdok="+ (kupac ? "'UPL'" : "'IPL'")+*/
                 "(VRDOK='UPL' OR (VRDOK='OKK' AND (IP > 0 OR ID < 0)))" :    
                 "(VRDOK='IPL' OR (VRDOK='OKD' AND (ID > 0 OR IP < 0)))")+
             " AND skstavke.cskstavke = pokriveni.cracuna AND skstavke.datdok <= '"+
             datumDo+"'"+raSaldaKonti.getCorgAndPripCondition(corg)+
             " ORDER BY 5, 2", true);
  }

  private DataSet getRacuni() {
    if (outside) return outsideRac;

    Condition minimumCond = Aus.getKnjigCond().and(jpp.getCondition()); 
      
/*      "knjig = '"+hr.restart.zapod.OrgStr.getKNJCORG(false)+
                         (cpar > 0 ? "' AND cpar="+cpar : "'");*/
    if (standalone)
      return ut.getNewQueryDataSet("SELECT cpar, brojdok, datdosp, iznos, cracuna"+
             " FROM kamrac WHERE "+minimumCond+" AND dugpot="+(kupac ? "'D'" : "'P'")+
             " AND datdosp < '"+ut.getFirstSecondOfDay(datumDo)+"' ORDER BY cpar,datdosp", true);
    return ut.getNewQueryDataSet("SELECT cpar, brojdok, datdosp, ssaldo as iznos,"+
             " oznval, tecaj, datumknj, cskstavke AS cracuna FROM skstavke WHERE "+
             minimumCond + (kon == Condition.none ? "" : " AND " + kon) +
             " AND " + /*vrdok="+ (kupac ? "'IRN'" : "'URN'")+*/ (kupac ?
                 "(VRDOK='IRN' OR (VRDOK='OKK' AND ID > 0))" :
                   "(VRDOK='URN' OR (VRDOK='OKD' AND IP > 0))"
                 ) + " AND skstavke.datdosp < '"+
             ut.getFirstSecondOfDay(datumDo)+"'"+raSaldaKonti.getCorgAndPripCondition(corg)+
            " ORDER BY cpar,datdosp", true);
  }

  public void findKamate() {
    DataSet rac, upl;
    table.empty();
    table2.empty();
    brojRacuna = 0;
    stope = Kamate.getDataModule().getTempSet(Condition.equal("CKAM", ckam)
        + " ORDER BY datum");
    stope.open();
    Timestamp lastknj = ut.getYearBegin(Aus.getFreeYear());
    Assert.is(stope.rowCount() > 0);

    int lastCpar = -1;
    boolean nepokrivenihUplata = false;
    boolean beginRacDosp = fromBeg && placeni && !neplaceni;

    obrMetoda = METHOD_COMBINED;
    if (jrbProg.isSelected()) obrMetoda = METHOD_PROGRESSIVE;
    if (jrbLin.isSelected()) obrMetoda = METHOD_LINEAR;

    if (null == (upl = getUplate())) return;
    rac = getRacuni();
    for (rac.first(); rac.inBounds(); rac.next()) {
      boolean firstEntry = true;
      String oznval = null;
      BigDecimal tecaj = null;
      BigDecimal pokriveno = raSaldaKonti.n0, kamata = raSaldaKonti.n0;
      String crac = rac.getString("CRACUNA");
      Timestamp racDosp = ut.getFirstSecondOfDay(rac.getTimestamp("DATDOSP"));
      Timestamp beginRacun = !beginRacDosp && racDosp.before(datumOd) ? datumOd : racDosp;
      if (rac.getInt("CPAR") != lastCpar) {
        lastCpar = rac.getInt("CPAR");
        nepokrivenihUplata = (nepokriveneUplate != null &&
            ld.raLocate(nepokriveneUplate, "CPAR", String.valueOf(lastCpar)) &&
            nepokriveneUplate.getInt("NUM") > 0);
      }
      if (rac.hasColumn("OZNVAL") != null) {
        oznval = rac.getString("OZNVAL");
        tecaj = rac.getBigDecimal("TECAJ");
      }
      if (ld.raLocate(upl, "CRACUNA", crac)) {
        for (; upl.inBounds() && upl.getString("CRACUNA").equals(crac); upl.next()) {
          Timestamp uplDat = ut.getFirstSecondOfDay(upl.getTimestamp("DATDOK"));
          BigDecimal iznos = upl.getBigDecimal("IZNOS");
          boolean ignore = false;
          if (oznval == null) {
            pokriveno = pokriveno.add(iznos.abs());
          } else {
            String uval = upl.getString("OZNVAL");
            BigDecimal rtecaj = tecaj;
            if (upl.getString("STRANA").equalsIgnoreCase("UPL")) {
              rtecaj = upl.getBigDecimal("TECAJ");
              iznos = iznos.negate();
              ignore = true;
            }
            if (!raSaldaKonti.isDomVal(uval) && !raSaldaKonti.isDomVal(oznval)) {
              iznos = iznos.multiply(tecaj).divide(
                  raSaldaKonti.findJedVal(oznval), 2, BigDecimal.ROUND_HALF_UP);
            }
            pokriveno = pokriveno.add(iznos);
          }
          if (!ignore && placeni && upl.getBigDecimal("SIGNI").signum() > 0 && 
              beginRacun.before(uplDat) && !uplDat.before(datumOd)) {
            addTableRow(rac, upl.getString("BROJDOK"), firstEntry);
            table2.setString("FLAG", "U");
            table.setString("FLAG", firstEntry && !(firstEntry = false) ? "R" : "U");

            kamata = kamata.add(processUplata(beginRacun, uplDat, iznos));
          }
        }
      }
      if (neplaceni && !nepokrivenihUplata && 
          (standalone || rac.getTimestamp("DATUMKNJ").compareTo(lastknj) >= 0) &&
          rac.getBigDecimal("IZNOS").compareTo(pokriveno) > 0) {
        BigDecimal nepokriveno = rac.getBigDecimal("IZNOS").subtract(pokriveno);
        addTableRow(rac, "Nepla\u0107eno", firstEntry);
        table2.setString("FLAG", "U");
        table.setString("FLAG", firstEntry && !(firstEntry = false) ? "R" : "U");

        kamata = kamata.add(processUplata(beginRacun, datumDo, nepokriveno));
      }
      if (kamata.compareTo(minIznos) < 0 && !firstEntry) {
        int cpar = table.getInt("CPAR");
        String brdok = table.getString("BROJDOK");
        while (table.rowCount() > 0 && table.getInt("CPAR") == cpar &&
               table.getString("BROJDOK").equals(brdok))
          table.emptyRow();
        while (table2.rowCount() > 0 && table2.getInt("CPAR") == cpar &&
            table2.getString("BROJDOK").equals(brdok))
       table2.emptyRow();
//          table.last();
      }
    }
  }

  private void OKPress() {
    if (busy || !Validacija()) return;
    busy = true;
    new Thread() {
      public void run() {
        try {
          process();
        } catch (Exception e) {}
        busy = false;
      }
    }.start();
  }

  private void cancelPress() {
    this.hide();
  }

  public void show() {
    super.show();
    SetFokus();
    jpp.focusCparLater();
  }
  
  

  private void jbInit() throws Exception {
    
    int dkAdd = isDirect() ? 25 : 0;

    kamtab = new QueryDataSet();
    hr.restart.sisfun.Asql.createMasterKamate(kamtab);

    fset.setColumns(new Column[] {
      dM.createStringColumn("CORG", "Org. jedinica", 12),
      dM.createStringColumn("NAZIV", "Naziv", 50),
      dM.createStringColumn("BROJKONTA", "Konto", 8),
      dM.createIntColumn("CKAM", "Tablica kamata"),
      dM.createStringColumn("OPIS", "Opis tablice kamata", 50),
      dM.createTimestampColumn("DATUMOD", "Datum od"),
      dM.createTimestampColumn("DATUMDO", "Datum do"),
      dM.createBigDecimalColumn("MINIZ", "Minimalni iznos", 2),
      dM.createIntColumn("CPAR", "Partner"),
      dM.createStringColumn("NAZPAR", "Naziv partnera", 50)
    });

    table.setColumns(new Column[] {
      dM.createIntColumn("CPAR"),
      dM.createIntColumn("RBR"),
      dM.createIntColumn("LRBR"),
      dM.createStringColumn("BROJDOK", 50),
      dM.createStringColumn("BROJUPL", 50),
      dM.createTimestampColumn("DATDOSP"),
      dM.createTimestampColumn("DATPLAC"),
      dM.createIntColumn("DANA"),
      dM.createBigDecimalColumn("STOPA", 2),
      dM.createBigDecimalColumn("KOEF", 12),
      dM.createBigDecimalColumn("GLAVNICA", 2),
      dM.createBigDecimalColumn("KAMATA", 2),
      dM.createStringColumn("FLAG", 1)
    });
    table.open();
    table2.setColumns(table.cloneColumns());
    table2.open();

    /*rcbCpar.setRaItems(new String[][] {
      {"Kupac", "K"},
      {"Dobavlja\u010D", "D"}
    });
    rcbCpar.setSelectedIndex(0);*/
    jpp.bind(fset);
    jpp.setKupci(true);
    jpp.init();
    
    jpp.setAllowMultiple(true);
    
    okp.addResetButton(this);

    getRepRunner().addReport("hr.restart.sk.repObrKamata", "hr.restart.sk.repObrKamata", 
         "ObrKamata", "Ispis obra\u010Duna kamata");
    getRepRunner().addReport("hr.restart.sk.repObrKamata2", "hr.restart.sk.repObrKamata2",
        "ObrKamata2", "Ispis s posebnim redom za ukupni iznos raèuna");

    jbKam.setText("...");
    //jbSelCpar.setText("...");

    bg1.setHorizontalAlignment(SwingConstants.LEADING);
    bg1.setHorizontalTextPosition(SwingConstants.TRAILING);
    bg1.add(jrbPla, " Pla\u0107eni ");
    bg1.add(jrbNepla, " Nepla\u0107eni ");
    bg1.add(jrbSvi, " Svi ");
    bg1.setSelected(jrbSvi);

    bg2.setHorizontalAlignment(SwingConstants.LEADING);
    bg2.setHorizontalTextPosition(SwingConstants.TRAILING);
    bg2.add(jrbProg, " Konformna ");
    bg2.add(jrbLin, " Proporcionalna ");
    bg2.add(jrbKomb, " Kombinirana ");
    bg2.setSelected(jrbLin);

    jrbKomb.setToolTipText("<HTML>Komforna za kašnjenje do godine dana,<br>proporcionalna za duže periode</HTML>");

/*    jlrCpar.setColumnName("CPAR");
    jlrCpar.setDataSet(fset);
    jlrCpar.setColNames(new String[] {"NAZPAR"});
    jlrCpar.setTextFields(new JTextComponent[] {jlrNazpar});
    jlrCpar.setVisCols(new int[] {0, 1});
    jlrCpar.setSearchMode(0);
    jlrCpar.setRaDataSet(dm.getPartneri());
    jlrCpar.setNavButton(jbSelCpar);

    jlrNazpar.setDataSet(fset);
    jlrNazpar.setColumnName("NAZPAR");
    jlrNazpar.setNavProperties(jlrCpar);
    jlrNazpar.setSearchMode(1); */

    jlrKam.setColumnName("CKAM");
    jlrKam.setDataSet(fset);
    jlrKam.setColNames(new String[] {"OPIS"});
    jlrKam.setTextFields(new JTextComponent[] {jlrOpis});
    jlrKam.setVisCols(new int[] {0, 1});
    jlrKam.setSearchMode(0);
    jlrKam.setRaDataSet(kamtab);
    jlrKam.setNavButton(jbKam);

    jlrOpis.setColumnName("OPIS");
    jlrOpis.setNavProperties(jlrKam);
    jlrOpis.setSearchMode(1);

    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(fset);
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0, 1});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbCorg);

    jlrNaziv.setDataSet(fset);
    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);

    jlCorg.setText("Org. jedinica");
    jlRac.setText("Ra\u010Duni");
    jlMet.setText("Metoda obraèuna");
    jlDan.setText("Datum (od - do)");
    jlKam.setText("Tablica kamata");
    jlIznos.setText("Minimalni iznos");
    jraIznos.setColumnName("MINIZ");
    jraIznos.setDataSet(fset);

    jraOd.setHorizontalAlignment(SwingConstants.CENTER);
    jraOd.setColumnName("DATUMOD");
    jraOd.setDataSet(fset);

    jraDo.setHorizontalAlignment(SwingConstants.CENTER);
    jraDo.setColumnName("DATUMDO");
    jraDo.setDataSet(fset);
    
    jpk.bind(fset);
    jpk.setKontaSet(Konta.getDataModule().getFilteredDataSet(Condition.in("SALDAK", "K D")));

    jpC.setLayout(xyC);
    xyC.setWidth(605);
    xyC.setHeight(25 + dkAdd);
    
/*    jpC.add(jlKam, new XYConstraints(15, 0, -1, -1));
    jpC.add(jlrKam, new XYConstraints(150, 0, 100, -1));
    jpC.add(jlrOpis, new XYConstraints(255, 0, 310, -1));
    jpC.add(jbKam, new XYConstraints(570, 0, 21, 21));*/

    jpC.add(jlCorg, new XYConstraints(15, 0, -1, -1));
    jpC.add(jlrCorg, new XYConstraints(150, 0, 100, -1));
    jpC.add(jlrNaziv, new XYConstraints(255, 0, 310, -1));
    jpC.add(jbCorg, new XYConstraints(570, 0, 21, 21));
    if (isDirect()) 
      jpC.add(jpk, new XYConstraints(0, 25, -1, -1));

    jpO.setLayout(xyO);
    xyO.setWidth(605);
    xyO.setHeight(110);


    /*jpO.add(rcbCpar, new XYConstraints(15, 25 + dkAdd, 130, -1));
    jpO.add(jlrCpar, new XYConstraints(150, 25 + dkAdd, 100, -1));
    jpO.add(jlrNazpar, new XYConstraints(255, 25 + dkAdd, 310, -1));
    jpO.add(jbSelCpar, new XYConstraints(570, 25 + dkAdd, 21, 21));*/
    jpO.add(jpp, new XYConstraints(0, 0, -1, -1));
    jpO.add(jlDan, new XYConstraints(15, 28, -1, -1));
    jpO.add(jraOd, new XYConstraints(150, 25, 100, -1));
    jpO.add(jraDo, new XYConstraints(255, 25, 100, -1));
    jpO.add(jlIznos, new XYConstraints(375, 27, -1, -1));
    jpO.add(jraIznos, new XYConstraints(465, 25, 100, -1));

    jpV.setLayout(xyV);
    xyV.setWidth(411);
    xyV.setHeight(25);
    jpV.setBorder(BorderFactory.createEtchedBorder());
    jpV.add(jrbPla, new XYConstraints(20, 0, 120, -1));
    jpV.add(jrbNepla, new XYConstraints(155, 0, 120, -1));
    jpV.add(jrbSvi, new XYConstraints(290, 0, 120, -1));

    jpM.setLayout(xyM);
    xyM.setWidth(411);
    xyM.setHeight(25);
    jpM.setBorder(BorderFactory.createEtchedBorder());
    jpM.add(jrbProg, new XYConstraints(20, 0, 120, -1));
    jpM.add(jrbLin, new XYConstraints(155, 0, 120, -1));
    jpM.add(jrbKomb, new XYConstraints(290, 0, 120, -1));


    jpO.add(jlRac, new XYConstraints(15, 54, -1, -1));
    jpO.add(jpV, new XYConstraints(150, 50, -1, -1));
    jpO.add(jlMet, new XYConstraints(15, 84, -1, -1));
    jpO.add(jpM, new XYConstraints(150, 80, -1, -1));

    jpDetail.setLayout(lay);
    lay.setWidth(605);
    lay.setHeight(190 + dkAdd);

    jpDetail.add(jlKam, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlrKam, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrOpis, new XYConstraints(255, 20, 310, -1));
    jpDetail.add(jbKam, new XYConstraints(570, 20, 21, 21));

    jpDetail.add(jpC, new XYConstraints(0, 45, -1, -1));
    jpDetail.add(jpO, new XYConstraints(0, 70 + dkAdd, -1, -1));

    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldk, String newk) {
        jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });

    new raDateRange(jraOd, jraDo);

    this.getContentPane().add(jpDetail, BorderLayout.CENTER);
    this.getContentPane().add(okp, BorderLayout.SOUTH);
    okp.registerOKPanelKeys(this);
  }

  /*protected void kupSelected() {
    jlrCpar.setRaDataSet(dm.getPartneriKup());
//    jlrCpar.setText("");
    jlrCpar.forceFocLost();
  }*/
  
  protected void afterKonto(boolean succ) {
    DataRow konto = !succ ? null : jpk.getKontoRow();
    if (konto != null) {
      if (konto.getString("SALDAK").equalsIgnoreCase("D") && jpp.isKupci()) 
        jpp.setKupci(false);
      else if (konto.getString("SALDAK").equalsIgnoreCase("K") && !jpp.isKupci())
        jpp.setKupci(true);
    }
    if (succ) jpp.focusCparLater();
  }
  
  void checkKupDob(boolean kupac) {
    DataRow konto = jpk.getKontoRow();
    if (konto != null) {
      if (konto.getString("SALDAK").equalsIgnoreCase("D") && jpp.isKupci())
        jpk.clear();
      else if (konto.getString("SALDAK").equalsIgnoreCase("K") && !jpp.isKupci())
        jpk.clear();
    }
  }

  /*protected void dobSelected() {
    jlrCpar.setRaDataSet(dm.getPartneriDob());
//    jlrCpar.setText("");
    jlrCpar.forceFocLost();
  }*/
}
