/****license*****************************************************************
**   file: frmGodObr.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Gkkumulativi;
import hr.restart.baza.Gkstavkerad;
import hr.restart.baza.Konta;
import hr.restart.baza.Pokriveni;
import hr.restart.baza.Skstavke;
import hr.restart.baza.Skstavkerad;
import hr.restart.baza.UIstavke;
import hr.restart.baza.dM;
import hr.restart.sisfun.dlgErrors;
import hr.restart.sk.R2Handler;
import hr.restart.sk.raSaldaKonti;
import hr.restart.sk.raVrdokMatcher;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTextMask;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raLLFrames;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class frmGodObr extends frmKnjizenje {
  raCommonClass rcc = raCommonClass.getraCommonClass();

  static frmGodObr frm;

  JraTextField jraGod = new JraTextField() {
    public boolean maskCheck() {
      if (!super.maskCheck()) return false;
      if (goodYear())
        dataSet.setTimestamp("DATUMKNJ",
          ut.getYearBegin(String.valueOf(Aus.getNumber(jraGod.getText()) + 1)));
      return true;
    }
  };
  JLabel jlGod = new JLabel();
//  JraCheckBox jcbBrisGk = new JraCheckBox();
//  JraCheckBox jcbBrisRGk = new JraCheckBox();
//  JraCheckBox jcbBrisSk = new JraCheckBox();
//  JraCheckBox jcbBrisRSk = new JraCheckBox();
//  JLabel jlBris = new JLabel();
//  JLabel jlBrisR = new JLabel();

  String god, godLast, knjig;
//  boolean brisGk, brisSk;

  dlgErrors errView;

  Condition condKnjig, condDatum, condGod, condGodMj, condPok;
  
  QueryDataSet uistR2;

  /**
   * Konstruktor (zacudo).
   */
  public frmGodObr() {
    try {
      frm = this;
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpCommon.remove(jlDATUMDO);
    jtDATUMDO.setVisible(false);
    jtDATUMDO.setEnabled(false);
    jpCommon.remove(jtDATUMDO);

    jraGod.setHorizontalAlignment(SwingConstants.CENTER);
    jlGod.setText("Za godinu");
    new raTextMask(jraGod, 4, false, raTextMask.DIGITS);

//    jlBris.setText("Brisanje stare godine");
//
//    jcbBrisSk.setText(" Salda konti ");
//    jcbBrisGk.setText(" Glavna knjiga ");
//    jcbBrisRSk.setText(" Salda konti ");
//    jcbBrisRGk.setText(" Glavna knjiga ");
//    jcbBrisSk.setSelected();
//    jcbBrisGk.setSelected();
//    jcbBrisRSk.setSelected();
//    jcbBrisRGk.setSelected();

    jpCommon.add(jraGod, new XYConstraints(470, 45, 50, -1));
    jpCommon.add(jlGod, new XYConstraints(395, 45, -1, -1));
//    jpCommon.add(jlBris, new XYConstraints(15, 70, -1, -1));
//    jpCommon.add(jcbBrisGk, new XYConstraints(150, 70, -1, -1));
//    jpCommon.add(jcbBrisSk, new XYConstraints(275, 70, -1, -1));
//    jp.add(jlBrisR, new XYConstraints(15, 70, -1, -1));
//    jp.add(jcbBrisRGk, new XYConstraints(225, 70, -1, -1));
//    jp.add(jcbBrisRSk, new XYConstraints(350, 70, -1, -1));
    jpCommon.add(jtDATUMDO, new XYConstraints(15, 70, 5, -1));
  }

  /**
   * <p>Ulazna za postavljanje fokusa.</p>
   * @see hr.restart.gk.frmKnjizenje#SetFokus()
   */
  public void SetFokus() {
    if (godLast == null)
      jraGod.requestFocus();
    else
      okpanel.jBOK.requestFocus();
  }

  void clearConditions() {
    condKnjig = condDatum = condGod = condGodMj = null;
  }

  void findConditions() {
    condKnjig = Condition.equal("KNJIG", knjig);
    condDatum = godLast == null ? Condition.where("DATUMKNJ", Condition.TILL, ut.getYearEnd(god)) :
                    Condition.between("DATUMKNJ", ut.getYearBegin(godLast), ut.getYearEnd(god));
    condGod = godLast == null ? Condition.where("GOD", Condition.LESS_OR_EQUAL, god) :
                  Condition.between("GOD", godLast, god);
    condGodMj = godLast == null ? Condition.where("GODMJ", Condition.LESS_OR_EQUAL, god.concat("12")) :
                   Condition.between("GODMJ", godLast.concat("00"), god.concat("12"));
    condPok = Condition.where("POKRIVENO", Condition.NOT_EQUAL, "X");
  }

  /**
   * @see hr.restart.gk.frmKnjizenje#initInputValues()
   */
  public void initInputValues() {
//    super.initInputValues();
//    System.out.println(jtDATUMDO.getText());
//    System.out.println(jtDATUMDO.getColumnName());
//    System.out.println(jtDATUMDO.getDataSet());
//    vl.isEmpty(jtDATUMDO);
//    dataSet.setTimestamp("DATUMKNJ",
//      ut.getYearBegin(String.valueOf(Aus.getNumber(jraGod.getText()) + 1)));
//    dataSet.setString("CVRNAL", "00");
  }

  /**
   * @see hr.restart.swing.JraDialog#show()
   */
  public void show() {
    knjig = hr.restart.zapod.OrgStr.getKNJCORG(false);
    godLast = Valid.getValid().getLastKnjigYear("gk");
    if (godLast != null) {
      god = godLast;
      jraGod.setText(god);
      rcc.setLabelLaF(jraGod, false);
      if (Aus.getNumber(god) > Aus.getNumber(vl.findYear())) {
        JOptionPane.showMessageDialog(raLLFrames.getRaLLFrames().getMsgStartFrame(),
          "Godišnja obrada je veæ obavljena!", "Poruka",
          JOptionPane.INFORMATION_MESSAGE);
        return;
      }
    } else {
      rcc.setLabelLaF(jraGod, true);
      if (jraGod.getText().length() == 0)
        jraGod.setText(String.valueOf(Aus.getNumber(vl.findYear()) - 1));
    }

    if (ld.raLocate(dm.getVrstenaloga(), "CVRNAL", "00")) {
      dataSet.setString("CVRNAL", "00");
      jpGetVrnal.jlrCVRNAL.forceFocLost();
      rcc.EnabDisabAll(jpGetVrnal, false);
    }
    dataSet.setTimestamp("DATUMKNJ",
      ut.getYearBegin(String.valueOf(Aus.getNumber(jraGod.getText()) + 1)));
    dataSet.setTimestamp("DATUMDO",vl.getToday());
    rcc.setLabelLaF(jtDATUMKNJ, false);
    super.show();
  }

  private boolean checkRadniPodaci() {
    boolean radsk = Skstavkerad.getDataModule().getRowCount(condKnjig.and(condDatum)) > 0;
    boolean radgk = Gkstavkerad.getDataModule().getRowCount(condKnjig.and(condGod)) > 0;
    boolean radskk = Skstavke.getDataModule().getRowCount(condKnjig.and(condDatum) + 
        " AND (CGKSTAVKE='' OR CGKSTAVKE IS NULL)") > 0;

    if (radsk || radgk) {
      JOptionPane.showMessageDialog(this,
        "U odabranoj godini ima neproknjiženih stavaka " + (radsk ? "salda konti" : "") +
        (radsk && radgk ? " i " : "") + (radgk ? "glavne knjige" : "") + ".", "Poruka",
        JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (radskk) {
      JOptionPane.showMessageDialog(this, "U odabranoj godini ima stavaka salda konti\n"+
          "koje nisu proknjizene u glavnu knjigu!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  boolean goodYear() {
    god = jraGod.getText();
    int g = Aus.getNumber(god);
    return (g >= 1970 && g <= Aus.getNumber(vl.findYear()) &&
           (godLast == null || g >= Aus.getNumber(godLast)));
  }

  StorageDataSet createKumErrorSet() {
    StorageDataSet errs = new StorageDataSet();
    errs.setColumns(new Column[] {
      dM.createStringColumn("OPIS", "Opis greške", 100),
      dM.createBigDecimalColumn("VRI", "Vrijednost"),
      dM.createBigDecimalColumn("VRI2", "Ispravna")
    });
    errs.open();
    return errs;
  }

  private void addError(DataSet errs, String opis, DataSet kum, String uloga, BigDecimal now, BigDecimal should) {
    errs.insertRow(false);
    ld.raLocate(dm.getOrgstruktura(), "CORG", kum.getString("CORG"));
    ld.raLocate(dm.getPartneri(), "CPAR", String.valueOf(kum.getInt("CPAR")));
    String ul = (uloga.equalsIgnoreCase("K")) ? "kupca " : "dobavlja\u010Da ";
    String opis2 = opis + " u OJ "+kum.getString("CORG")+" za "+ul+kum.getInt("CPAR")+
    " "+dm.getPartneri().getString("NAZPAR")+", konto "+kum.getString("BROJKONTA");
    errs.setString("OPIS", opis2.substring(0, Math.min(100, opis2.length())));
    errs.setBigDecimal("VRI", now);
    errs.setBigDecimal("VRI2", should);
    errs.post();
  }

  boolean checkIntegrity() {
    raProcess.runChild(this, 10, new Runnable() {
      public void run() {
        /*raProcess.setMessage("Provjera GK kumulativa ...", false);
        StorageDataSet err = raGKKontrole.checkKumul(raGKKontrole.createCheckErrors(false));
        if (err.rowCount() > 0) {
          errView = new dlgErrors(frmGodObr.this, "Greške u kumulativima glavne knjige", false);
          errView.setDataSet(err);
          raProcess.fail();
        }
        checkSKIntegrity();*/
        checkGKSKIntegrity();
      }
    });
    return checkAndShowErrors();
  }

  private boolean checkAndShowErrors() {
    if (raProcess.isFailed())
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          errView.show();
          errView = null;
        }
      });
    if (errView != null && errView.countErrors() == 0) errView.hide();
    return raProcess.isCompleted();
  }
  
  void checkGKSKIntegrity() {
    
    StorageDataSet results = new StorageDataSet();
    results.setColumns(new Column[] {
        dM.createStringColumn("OPIS", "Opis", 60),
        dM.createStringColumn("BROJKONTA", "Konto", 8),
        dM.createBigDecimalColumn("SID", 2),
        dM.createBigDecimalColumn("SIP", 2),
        dM.createBigDecimalColumn("GID", 2),
        dM.createBigDecimalColumn("GIP", 2),
    });
    results.open();
    
    raProcess.setMessage("Provjera konzistentnosti GK i SK stavaka...", true);
    DataSet konsk = Konta.getDataModule().getTempSet(
        Condition.in("SALDAK", "K D"));
    raProcess.openScratchDataSet(konsk);
    
    raProcess.setMessage("Dohvat SK stavaka ...", false);
    DataSet sk = Util.getNewQueryDataSet(
        "SELECT brojkonta, sum(id) as id, sum(ip) as ip FROM skstavke WHERE "+Aus.getKnjigCond().
        and(Condition.where("POKRIVENO", Condition.NOT_EQUAL, "X")).and(
        Condition.between("DATUMKNJ", ut.getYearBegin(godLast), ut.getYearEnd(god)))+
        " GROUP BY brojkonta");
    raProcess.openScratchDataSet(sk);
    
    raProcess.setMessage("Dohvat GK stavaka ...", false);
    DataSet gk = Util.getNewQueryDataSet(
        "SELECT brojkonta, sum(id) as id, sum(ip) as ip FROM gkstavke WHERE "+Aus.getKnjigCond().
        and(Condition.between("GOD", godLast, god))+" GROUP BY brojkonta");
    raProcess.openScratchDataSet(gk);
    
    for (konsk.first(); konsk.inBounds(); konsk.next()) {
      raProcess.checkClosing();
      boolean issk = ld.raLocate(sk, "BROJKONTA", konsk.getString("BROJKONTA"));
      boolean isgk = ld.raLocate(gk, "BROJKONTA", konsk.getString("BROJKONTA"));
      System.out.println("konto "+konsk.getString("BROJKONTA")+" "+issk+" "+isgk);
      if (!issk && !isgk) continue;
      if (!issk || !isgk || sk.getBigDecimal("ID").compareTo(gk.getBigDecimal("ID")) != 0 ||
          sk.getBigDecimal("IP").compareTo(gk.getBigDecimal("IP")) != 0) {
        results.insertRow(false);
        if (!issk)
          results.setString("OPIS", "Konto ne postoji u salda konti");
        else if (!isgk)
          results.setString("OPIS", "Konto ne postoji u glavnoj knjizi");
        else results.setString("OPIS", "Nejednaki iznosi na kontima u SK i GK");
        results.setString("BROJKONTA", konsk.getString("BROJKONTA"));
        if (issk) {
          results.setBigDecimal("SID", sk.getBigDecimal("ID"));
          results.setBigDecimal("SIP", sk.getBigDecimal("IP"));
        }
        if (isgk) {
          results.setBigDecimal("GID", gk.getBigDecimal("ID"));
          results.setBigDecimal("GIP", gk.getBigDecimal("IP"));
        }
        results.post();
      }
    }
    
    if (results.rowCount() == 0) return;
    errView = new dlgErrors(this, "Greške u vezama izmedju GK i SK", false);
    errView.setDataSet(results);
    raProcess.fail();
  }

  /*void checkSKIntegrity() {
    Condition condGodina = godLast == null ? Condition.where("GODINA", Condition.LESS_OR_EQUAL, god) :
                  Condition.between("GODINA", godLast, god);
    raProcess.setMessage("Provjera SK kumulativa ...", false);
    QueryDataSet kums = Skkumulativi.getDataModule().getTempSet(condKnjig.and(condGodina));
    raProcess.openDataSet(kums);
    QueryDataSet sks = Skstavke.getDataModule().getTempSet(condKnjig.and(condDatum));
    raProcess.openDataSet(sks);
    StorageDataSet kumerr = createKumErrorSet();
    BigDecimal n0 = new BigDecimal(0.0);
    Timestamp d2 = Util.getUtil().addDays(Util.getUtil().getYearBegin(god), 1);
    String[] cols = new String[] {"CORG", "CPAR", "ULOGA", "BROJKONTA"};
    String[] vals = new String[] {"", "", "", ""};
    for (sks.first(); sks.inBounds(); sks.next()) {
      raProcess.checkClosing();
      vals[0] = sks.getString("CORG");
      vals[1] = String.valueOf(sks.getInt("CPAR"));
      String vrdok = sks.getString("VRDOK");
      if (vrdok.equals("IRN") || vrdok.equals("UPL") || vrdok.equals("OKK")) vals[2] = "K";
      else vals[2] = "D";
      vals[3] = sks.getString("BROJKONTA");
      if (vals[3] == null || vals[3].length() == 0) {
        kumerr.insertRow(false);
        kumerr.setString("OPIS", "Ne postoji shema "+sks.getString("CSKL")+" za dokumente "+
          sks.getString("VRDOK")+" (ili nedostaje stavka "+sks.getShort("STAVKA")+")");
        kumerr.post();
      } else if (ld.raLocate(kums, cols, vals)) {
        BigDecimal racun = sks.getBigDecimal(vals[2].equals("K") ? "ID" : "IP");
        BigDecimal uplata = sks.getBigDecimal(vals[2].equals("K") ? "IP" : "ID");
        String rac = sks.getTimestamp("DATUMKNJ").before(d2) ? "POCSTRAC" : "PROMETR";
        String upl = rac.equals("POCSTRAC") ? "POCSTUPL" : "PROMETU";
        kums.setBigDecimal(rac, kums.getBigDecimal(rac).subtract(racun));
        kums.setBigDecimal(upl, kums.getBigDecimal(upl).subtract(uplata));
        kums.post();
      } else
        addError(kumerr, "Ne postoji kumulativ", sks, vals[2], n0,
                 sks.getBigDecimal("ID").add(sks.getBigDecimal("IP")));
    }
    QueryDataSet origkums = Skkumulativi.getDataModule().getTempSet(condKnjig.and(condGodina));
    raProcess.openDataSet(origkums);
    for (kums.first(); kums.inBounds(); kums.next()) {
      raProcess.checkClosing();
      vals[0] = kums.getString("CORG");
      vals[1] = String.valueOf(kums.getInt("CPAR"));
      vals[2] = kums.getString("ULOGA");
      vals[3] = kums.getString("BROJKONTA");
      ld.raLocate(origkums, cols, vals);
      String[][] checks =
        {{"POCSTRAC", "Pogrešno po\u010Detno stanje ra\u010Duna"},
         {"POCSTUPL", "Pogrešno po\u010Detno stanje uplata"},
         {"PROMETR", "Pogrešan promet ra\u010Duna"},
         {"PROMETU", "Pogrešan promet uplata"}};
      for (int i = 0; i < checks.length; i++)
        if (kums.getBigDecimal(checks[i][0]).signum() != 0)
          addError(kumerr, checks[i][1], kums, vals[2], origkums.getBigDecimal(checks[i][0]),
            origkums.getBigDecimal(checks[i][0]).subtract(kums.getBigDecimal(checks[i][0])));

    }
    if (kumerr.rowCount() == 0) return;
    errView = new dlgErrors(this, "Greške u kumulativima salda konti", false);
    errView.setDataSet(kumerr);
    raProcess.fail();
    raSaldaKonti.checkKumulativProc(god);
    if (raProcess.getErrors().countErrors() == 0) return;
    errView = raProcess.getErrors();
    raProcess.fail();
  }*/

  /**
   * @return true ako su podaci na panelu u redu.
   * @see hr.restart.gk.frmKnjizenje#Validacija()
   */
  public boolean Validacija() {
    if (vl.isEmpty(jraGod)) return false;
    if (!goodYear()) {
      JOptionPane.showMessageDialog(this, "Pogrešna godina!", "Greška",
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    findConditions();
    if (!checkRadniPodaci()) return false;
    if (Gkkumulativi.getDataModule().getRowCount(condKnjig.and(condGodMj)) == 0) {
      JOptionPane.showMessageDialog(this, "Nema podataka za godišnju obradu!", "Greška",
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (errView != null) errView.hide();
    if (!checkIntegrity()) return false;

    return true;
  }

  Map outRac = null;
  Map newToOld = null;
  /**
   * @return true ako je temeljnica uspjesno kreirana.
   * @see hr.restart.gk.frmKnjizenje#okPress()
   */
  public boolean okPress() {
    uistR2 = UIstavke.getDataModule().getTempSet(Condition.nil);
    uistR2.open();
    QueryDataSet kum = ut.getNewQueryDataSet("SELECT corg, brojkonta, id, ip "+
        "FROM gkkumulativi WHERE "+condKnjig.and(condGodMj)+" ORDER BY brojkonta,corg", false);
    String opis = "Po\u010Detno stanje, prijenos iz "+god+". godine";
//    brisGk = jcbBrisGk.isSelected();
//    brisSk = jcbBrisSk.isSelected();
    if (!raObrKumGK.getInstance().createTem(this, kum, opis, true, false)) return false;
    /* Nadji sve stavke salda konti koje nisu pokrivene */
    
    HashSet saldak = new HashSet();
    DataSet skk = Konta.getDataModule().getTempSet("BROJKONTA", 
          Condition.in("SALDAK", new String[] {"K","D"}));
    skk.open();
    for (skk.first(); skk.inBounds(); skk.next())
      saldak.add(skk.getString("BROJKONTA"));
    
    QueryDataSet sk = 
      ut.getNewQueryDataSet("SELECT knjig, corg, brojkonta, cpar, vrdok, cskstavke, opis, "+
        " id, ip, saldo, pokriveno, brojdok, datdok, datdosp, oznval, tecaj, extbrdok, brojizv,"+
        " pvid, pvip, pvsaldo, pvpok, datpri, cskl, cknjige FROM skstavke WHERE "+condKnjig.and(condDatum).and(condPok)+
        " ORDER BY brojkonta, corg", false);
    sk.setMetaDataUpdate(sk.getMetaDataUpdate() & ~MetaDataUpdate.ROWID);
    setProcessMessage("Dohvat nepokrivenih stavki salda konti ...", true);
    sk.open();
    sk.getColumn("CSKSTAVKE").setRowId(true);
    setProcessMessage("Provjera pokrivenosti uplatama iz nove godine ...", true);
    //Map outUpl = new HashMap();
    outRac = new HashMap();
    newToOld = new HashMap();
    Condition newc = condKnjig.and(condPok).and(Condition.from("DATUMKNJ",
            ut.getYearBegin(String.valueOf(Aus.getNumber(god) + 1))));
    raSaldaKonti.updateOutOfRangeSaldo(sk, newc, outRac);
    //System.out.println("Warning: out of date upl: "+outUpl);
    System.out.println("Warning: out of date rac: "+outRac);
    /*if (outUpl.size() > 0) {
      getKnjizenje().kSk.errMessage("Postoje uplate iz prošle godine pokrivene raèunima iz tekuæe");
      return false;
    }*/
    
    BigDecimal n0 = new BigDecimal(0);
    String[] cc = {"KNJIG", "VRDOK", "CPAR", "BROJKONTA", "DATDOK", "DATDOSP", "OZNVAL", "TECAJ", "EXTBRDOK", "BROJIZV", "DATPRI", "CSKL", "CKNJIGE"};
    setProcessMessage("Formiranje temeljnice...");

    for (sk.first(); sk.inBounds(); sk.next()) {
      BigDecimal saldo = sk.getBigDecimal("SALDO");
      if (saldo.signum() != 0 && saldak.contains(sk.getString("BROJKONTA"))) {
        StorageDataSet stavka = getKnjizenje().
           getNewStavka(sk.getString("BROJKONTA"),sk.getString("CORG"));
        boolean id = sk.getBigDecimal("ID").signum() != 0;
        getKnjizenje().setID(id ? saldo : n0);
        getKnjizenje().setIP(id ? n0 : saldo);
        stavka.setBigDecimal("DEVID", id ? sk.getBigDecimal("PVSALDO") : n0);
        stavka.setBigDecimal("DEVIP", id ? n0 : sk.getBigDecimal("PVSALDO"));
        opis = sk.getString("OPIS");
        if (opis.length() <= 95) opis = opis.concat(" (PS)");
        stavka.setString("OPIS", opis);
        String brd = sk.getString("BROJDOK");
        QueryDataSet uis = null;
        
// AI :: uistavke R2         
        if (R2Handler.isR2Shema(sk.getString("CSKL"), sk.getString("VRDOK")) && ut.sameDay(sk.getTimestamp("DATPRI"), R2Handler.endofmankind)) {
          uis = frmKnjSKRac.getUIStavke(sk);
        }
        
        if (brd.indexOf(':') == 6 && brd.startsWith("PS")) {
          int ly = Aus.getAnyNumber(brd.substring(2, 6));
          if (ly <= Aus.getNumber(god) && ly >= Aus.getNumber(god) - 5)
            brd = brd.substring(7);
        }
        brd = "PS" + (Aus.getNumber(god) + 1) + ":" + brd;
        if (brd.length() > 50) brd = brd.substring(0, 50);
        stavka.setString("BROJDOK", brd);
        dM.copyColumns(sk, stavka, cc);
        
// AI :: uistavke R2 
        if (uis!=null) {
          for (uis.first(); uis.inBounds(); uis.next()) {
            uistR2.insertRow(false);
            dM.copyColumns(uis, uistR2);
            uistR2.setString("BROJDOK", brd);
            uistR2.post();
          }
        }
        
        if (outRac.containsKey(sk.getString("CSKSTAVKE")))
          newToOld.put(raSaldaKonti.findCSK(stavka), sk.getString("CSKSTAVKE"));
        if (!getKnjizenje().saveStavka()) return false;
      }
    }
    return getKnjizenje().saveAll();
  }

  boolean houseKeeping() throws Exception{
    /*    setProcessMessage("Brisanje veza pokrivanja ...");
    raTransaction.runSQL("DELETE FROM pokriveni WHERE cracuna IN (SELECT cskstavke "+
                         "FROM skstavke WHERE "+condKnjig.and(condDatum)+")");
    raTransaction.runSQL("DELETE FROM pokriveni WHERE cuplate IN (SELECT cskstavke "+
                         "FROM skstavke WHERE "+condKnjig.and(condDatum)+")");

    setProcessMessage("Brisanje neproknjiženih podataka ...");
    raTransaction.runSQL("DELETE FROM skstavkerad WHERE "+condKnjig.and(condDatum));
    raTransaction.runSQL("DELETE FROM gkstavkerad WHERE "+condKnjig.and(condGod));

    setProcessMessage("Brisanje stare godine GK ...");
    raTransaction.runSQL("DELETE FROM gkstavke WHERE "+condKnjig.and(condGod));
    raTransaction.runSQL("DELETE FROM gkkumulativi WHERE "+condKnjig.and(condGodMj));

    setProcessMessage("Brisanje zatvorenih stavaka SK ...");

    PreparedStatement stm = dm.getDatabase1().getJdbcConnection().prepareStatement(
        "DELETE FROM uistavke WHERE knjig = ? AND cpar = ? AND vrdok = ? AND brojdok = ?");

    QueryDataSet skz = Skstavke.getDataModule().getTempSet(condKnjig.and(condDatum).and(condZat));
    skz.open();
    for (skz.first(); skz.inBounds(); skz.next()) {
      stm.setString(1, knjig);
      stm.setInt(2, skz.getInt("CPAR"));
      stm.setString(3, skz.getString("VRDOK"));
      stm.setString(4, skz.getString("BROJDOK"));
      stm.executeUpdate();
    }
    skz.deleteAllRows();
    raTransaction.saveChanges(skz);*/
    /*setProcessMessage("Prebacivanje otvorenih stavaka SK ...");
    raPreparedStatement ski = new raPreparedStatement(dm.getSkstavke(), raPreparedStatement.INSERT);
    hr.restart.sk.raSaldaKonti.setKumInvalid();
    QueryDataSet sk = Skstavke.getDataModule().getTempSet(condKnjig.and(condDatum).and(condPok));
    sk.open();
    String ly = "-".concat(String.valueOf(Aus.getNumber(god) - 1));
    for (sk.first(); sk.inBounds(); sk.next()) {
      sk.setTimestamp("DATUMKNJ", dataSet.getTimestamp("DATUMKNJ"));
      if (sk.getBigDecimal("ID").signum() != 0)
        sk.setBigDecimal("ID", sk.getBigDecimal("SALDO"));
      else sk.setBigDecimal("IP", sk.getBigDecimal("SALDO"));
      String brd = sk.getString("BROJDOK");
      if (brd.endsWith(ly)) brd = brd.substring(0, brd.length() - ly.length());
      sk.setString("BROJDOK", brd + "-" + god);
      hr.restart.sk.raSaldaKonti.addToKumulativPS(sk);
      ski.setValues(sk);
      if (!ski.execute()) return false;
    }
    portun:  42 936 724.09
    setProcessMessage("Ažuriranje kumulativa SK ...");
    raTransaction.saveChanges(dm.getSkkumulativi());*/
    setProcessMessage("Ažuriranje pokrivanja iz nove godine ...");
    if (newToOld.size() > 0) {
      System.out.println(newToOld);
      QueryDataSet sk = ut.getNewQueryDataSet("SELECT knjig, cpar, vrdok, brojkonta, brojdok, brojizv, cskstavke, id, ip, ssaldo, " +
          "saldo, pokriveno, tecaj, oznval, pvssaldo, pvsaldo, pvpok FROM skstavke WHERE "+
          condKnjig.and(condDatum).and(condPok), false);
      setProcessMessage("Dohvat pokrivenih stavki salda konti ...", true);
      sk.open();
      QueryDataSet pok = Pokriveni.getDataModule().getTempSet();
      setProcessMessage("Dohvat veza pokrivanja ...", true);
      pok.open();
      QueryDataSet skn = ut.getNewQueryDataSet("SELECT knjig, cpar, vrdok, brojkonta, brojdok, brojizv, cskstavke, id, ip, ssaldo, " +
           "saldo, pokriveno, tecaj, oznval, pvssaldo, pvsaldo, pvpok FROM skstavke " +
           "WHERE cgkstavke like '"+getKnjizenje().originalCNALOGA+"%'", false);
      System.out.println(skn.getQuery().getQueryString());
      setProcessMessage("Dohvat prebaèenih stavki salda konti ...", true);
      skn.open();
      System.out.println(skn.rowCount());
      boolean err = false;
      String[] pkey = {"CRACUNA", "CUPLATE"};
      String[] pval = new String[2];
      
      for (Iterator it = newToOld.entrySet().iterator(); it.hasNext(); ) {
        Map.Entry me = (Map.Entry) it.next();
        String newCsk = (String) me.getKey();
        String oldCsk = (String) me.getValue();
        boolean oldFind = ld.raLocate(sk, "CSKSTAVKE", oldCsk);
        boolean racTip = raVrdokMatcher.isRacunTip(sk);
        boolean domVal = raSaldaKonti.isDomVal(sk);
        BigDecimal rtecaj = null;
        if (!raSaldaKonti.isSimple()) {
          rtecaj = raSaldaKonti.calcTecaj(sk);
          /*
          rtecaj = sk.getBigDecimal("TECAJ");
          rjedval = raSaldaKonti.findJedVal(sk);
          if (rjedval == null) rjedval = new BigDecimal(1);*/
        }

        List ups = (List) outRac.get(oldCsk);
        for (Iterator il = ups.iterator(); il.hasNext(); ) {
          raSaldaKonti.OutDoc od = (raSaldaKonti.OutDoc) il.next();
          if (od.racSide) {
            pval[0] = od.csk;
            pval[1] = oldCsk;
          } else {
            pval[0] = oldCsk;
            pval[1] = od.csk;
          }
          if (ld.raLocate(pok, pkey, pval)) {
            pok.setString(od.racSide ? "CUPLATE" : "CRACUNA", newCsk);
            boolean newFind = ld.raLocate(skn, "CSKSTAVKE", newCsk);
            if (!oldFind || !newFind) {
              err = true;
              if (!oldFind) getKnjizenje().kSk.errMessage("Dokument "+oldCsk+
                  " ne mogu pronaæi u staroj godini");
              if (!newFind) getKnjizenje().kSk.errMessage("Dokument "+newCsk+
                " ne mogu pronaæi u novoj godini");
            } else {
              BigDecimal tecaj = rtecaj;
              if (!raSaldaKonti.isSimple() && raSaldaKonti.isTecUplata() ^ od.racSide) {
                tecaj = od.tecaj;
                /*jedval = raSaldaKonti.findJedVal(od.val);
                if (jedval == null) jedval = Aus.one0;*/
              }
              if (raSaldaKonti.isSimple()) {
                raSaldaKonti.modifyMatchSaldo(sk, od.iznos, od.racSide != racTip);
                raSaldaKonti.modifyMatchSaldo(skn, od.iznos, od.racSide == racTip);
              } else {
                if (domVal || !raSaldaKonti.isDomVal(od.val)) {
                  raSaldaKonti.modifyMatchPVSaldo(sk, od.iznos, od.racSide != racTip);
                  raSaldaKonti.modifyMatchPVSaldo(skn, od.iznos, od.racSide == racTip);
                }
                BigDecimal domIznos = domVal != raSaldaKonti.isDomVal(od.val) ? od.iznos :
                  od.iznos.multiply(tecaj).setScale(2, BigDecimal.ROUND_HALF_UP);
                raSaldaKonti.modifyMatchSaldo(sk, domIznos, od.racSide != racTip);
                raSaldaKonti.modifyMatchSaldo(skn, domIznos, od.racSide == racTip);
              }
            }
          } else {
            err = true;
            getKnjizenje().kSk.errMessage("Ne mogu pronaæi vezu "+pval[0]+" <-> "+pval[1]);
          }
        }
      }
      if (err) getKnjizenje().kSk.showErrors();
      if (err) return false;
      
      raTransaction.saveChanges(sk);
      raTransaction.saveChanges(skn);
      raTransaction.saveChanges(pok);
 // AI :: uistavke R2
      raTransaction.saveChanges(uistR2);
      // todo
    }
    setProcessMessage("Ažuriranje statusa obrade ...");
    dm.getKnjigod().open();
    dm.getKnjigod().insertRow(false);
    dm.getKnjigod().setString("APP", "gk");
    dm.getKnjigod().setString("CORG", knjig);
    dm.getKnjigod().setString("GOD", Integer.toString(Aus.getNumber(god) + 1));
    raTransaction.saveChanges(dm.getKnjigod());
    return true;
  }

  /**
   * Metoda koja se poziva iz superclass, nakon knjizenja temeljnice.
   * @return true ako je sve proslo u redu.
   * @see hr.restart.gk.frmKnjizenje#commitTransfer()
   */
  public boolean commitTransfer() {
    if (!super.commitTransfer()) return false;
    try {
      return houseKeeping();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
