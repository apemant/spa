/****license*****************************************************************
**   file: dlgTotalPromet.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Skstavke;
import hr.restart.baza.dM;
import hr.restart.sk.raVrdokMatcher;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.JraDialog;
import hr.restart.swing.KeyAction;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raProcess;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import bsh.util.Util;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class dlgTotalPromet {
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  lookupData ld = lookupData.getlookupData();
  jpTotalPromet pan = new jpTotalPromet() {
    public void begin() {
      if (busy) return;
      busy = true;
      new Thread() {
        public void run() {
          try {
            findPromet(dlg, pan.getGodina());
            pan.repaint();
          } finally {
            busy = false;
          }
        }
      }.start();      
    }
  };

  boolean busy = false;
  JraDialog dlg;
  String col;
  int key;

  public dlgTotalPromet(String maincol) {
    col = maincol;
  }

  public void show(Container owner, int key, String title) {
    this.key = key;
    if (!findPromet(owner, Valid.getValid().getKnjigYear("robno"))) return;
    if (owner instanceof JComponent)
      owner = ((JComponent) owner).getTopLevelAncestor();
    if (owner instanceof Frame)
      dlg = new JraDialog((Frame) owner, true);
    else if (owner instanceof Dialog)
      dlg = new JraDialog((Dialog) owner, true);
    else dlg = new JraDialog((Frame) null, true);

    dlg.setTitle(title);
    dlg.setContentPane(pan);
    dlg.pack();
    dlg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    AWTKeyboard.registerKeyStroke(dlg, AWTKeyboard.F10, new KeyAction() {
      public boolean actionPerformed() {
        pan.begin();
        return true;
      }
    });
    AWTKeyboard.registerKeyStroke(dlg, AWTKeyboard.ESC, new KeyAction() {
      public boolean actionPerformed() {
        hide();
        return true;
      }
    });
    dlg.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        hide();
      }
    });
    if (owner != null) dlg.setLocationRelativeTo(owner);
    dlg.show();
  }

  public void hide() {
    if (dlg != null) {
      dlg.dispose();
      dlg = null;
    }
  }

/*  private String findInitialYear() {
    DataSet kg = dm.getKnj*god();
    if (ld.raLocate(kg, "CORG", OrgStr.getKNJCORG(false)))
      return kg.getString("GOD");
    return Valid.getValid().findYear();
  } */


  boolean findPromet(Container owner, String god) {
    int g = Aus.getNumber(god);
    if (g < 20) g = 2000 + g;
    else if (g < 100) g = 1900 + g;

    if (g < 1950 || g > 2100) {
      JOptionPane.showMessageDialog(owner, "Pogrešna godina!", "Greška",
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    procGod = String.valueOf(g);
    if (dlg == null) pan.setGodina(procGod);
    raProcess.runChild(owner, "Promet kupca", "Operacija u tijeku ...", new Runnable() {
      public void run() {
        findPromet();
      }
    });
    return raProcess.isCompleted();
  }

  private String procGod;

  public static class Results {
    public BigDecimal rac, upl, nrac, drac, ndrac;
    public Results() {
      rac = upl = nrac = drac = ndrac = new BigDecimal(0);
    }
    public BigDecimal getSaldo() {
      return rac.add(nrac).subtract(upl);
    }
    
    public BigDecimal getDospSaldo() {
      return drac.add(ndrac).subtract(upl);
    }
  }

  public static Results findPromet(int cpar) {
    Results ret = new Results();
    String god = hr.restart.util.Valid.getValid().getLastKnjigYear("gk");
    if (god == null) god = "1980";
    Timestamp beg = hr.restart.util.Util.getUtil().getYearBegin(god);
    Timestamp today = hr.restart.util.Util.getUtil().getLastSecondOfDay(Valid.getValid().getToday());
    
    DataSet ds = Skstavke.getDataModule().getTempSet("ID IP VRDOK DATDOSP", 
        Condition.equal("CPAR", cpar).
      and(Aus.getKnjigCond()).and(Aus.getVrdokCond(true)).
      and(Condition.where("DATUMKNJ", Condition.FROM, beg)));
    //DataSet ds = dM.getDataModule().getSkstavke();
    if (!raProcess.isRunning()) ds.open();
    else raProcess.openDataSet(ds);
    for (ds.first(); ds.inBounds(); ds.next()) {
      ret.rac = ret.rac.add(ds.getBigDecimal("ID"));
      ret.upl = ret.upl.add(ds.getBigDecimal("IP"));
      if (raVrdokMatcher.isRacunTip(ds) && !ds.isNull("DATDOSP") 
          && !ds.getTimestamp("DATDOSP").after(today)) 
        ret.drac = ret.drac.add(ds.getBigDecimal("ID"));
    }

    DataSet skl = hr.restart.robno.Util.getSkladFromCorg();
    DataSet knj = OrgStr.getOrgStr().getOrgstrAndCurrKnjig();
    Condition sklDok = Condition.in("VRDOK", new String[] {"ROT", "POD"});
    Condition orgDok = Condition.in("VRDOK", new String[] {"RAC", "TER", "ODB"});
    String query = "SELECT SUM(uirac) AS uirac FROM doki WHERE "+
        Condition.equal("CPAR", cpar).and(Condition.equal("STATKNJ", "K").not()).
        and((sklDok.and(Condition.in("CSKL", skl))).
        or(orgDok.and(Condition.in("CSKL", knj, "CORG"))));
    ds = hr.restart.util.Util.getNewQueryDataSet(query, false);
    if (!raProcess.isRunning()) ds.open();
    else raProcess.openDataSet(ds);
    ret.nrac = ds.getBigDecimal("UIRAC");
    
    String dquery = "SELECT SUM(uirac) AS uirac FROM doki WHERE "+
        Condition.equal("CPAR", cpar).and(Condition.equal("STATKNJ", "K").not()).
        and(Condition.till("DATDOSP", today)).
        and((sklDok.and(Condition.in("CSKL", skl))).
        or(orgDok.and(Condition.in("CSKL", knj, "CORG"))));
    ds = hr.restart.util.Util.getNewQueryDataSet(dquery, false);
    if (!raProcess.isRunning()) ds.open();
    else raProcess.openDataSet(ds);
    ret.ndrac = ds.getBigDecimal("UIRAC");
    return ret;
  }

  private void findPromet() {

    BigDecimal z = new BigDecimal(0);
    String datCol = pan.isPoc() ? "DATUMKNJ" : "DATDOK";

    // saldak
    DataSet ds = Skstavke.getDataModule().getTempSet(
      "DATDOSP ID IP SALDO", Condition.equal(col, key).and(Aus.getKnjigCond()).
      and(Condition.between(datCol, ut.getYearBegin(procGod), ut.getYearEnd(procGod))).
      and(Condition.in("VRDOK", new String[] {"IRN", "UPL", "OKK"})));
    raProcess.setMessage("Dohvat stavaka salda konti ...", true);
    raProcess.openScratchDataSet(ds);    

    raProcess.setMessage("Kumuliranje stavaka salda konti ...", false);

    BigDecimal totalDug, totalPot;
    BigDecimal openDug, openPot;
    BigDecimal dospDug, nedospDug;
    totalDug = totalPot = openDug = openPot = dospDug = nedospDug = z;

    for (ds.first(); ds.inBounds(); ds.next()) {
      raProcess.checkClosing();
      totalDug = totalDug.add(ds.getBigDecimal("ID"));
      totalPot = totalPot.add(ds.getBigDecimal("IP"));
      if (ds.getBigDecimal("SALDO").signum() != 0) {
        if (ds.getBigDecimal("ID").signum() != 0) {
          openDug = openDug.add(ds.getBigDecimal("SALDO"));
          if (!ut.getFirstSecondOfDay(vl.getToday()).after(ds.getTimestamp("DATDOSP")))
            nedospDug = nedospDug.add(ds.getBigDecimal("SALDO"));
          else dospDug = dospDug.add(ds.getBigDecimal("SALDO"));
        } else openPot = openPot.add(ds.getBigDecimal("SALDO"));
      }
    }

    // racuni
    raProcess.checkClosing();
    raProcess.setMessage("Priprema raèuna ...", false);
    DataSet skl = rut.getSkladFromCorg();
    raProcess.checkClosing();
    DataSet knj = OrgStr.getOrgStr().getOrgstrAndCurrKnjig();
//    ArrayList orgList = new ArrayList();
//    for (knj.first(); knj.inBounds(); knj.next())
//      orgList.add(knj.getString("CORG"));
//    String[] corgs = (String[]) orgList.toArray(new String[orgList.size()]);

    Condition sklDok = Condition.in("VRDOK", new String[] {"ROT", "POD"});
    Condition orgDok = Condition.in("VRDOK", new String[] {"RAC", "TER", "ODB"});
    Condition period = Condition.between("DATDOK",
        ut.getYearBegin(procGod), ut.getYearEnd(procGod));

    /*
      Query.columns("datdok,uirac,uimar").tables("doki", "stdoki")
    */

    String query =
        "SELECT max(doki.datdok) as datdok, max(doki.uirac) as uirac, "+
        "sum(stdoki.iprodbp)-sum(stdoki.inab) as uimar, max(doki.statknj) as statknj "+
        "FROM doki,stdoki WHERE "+rut.getDoc("doki","stdoki")+" AND "+
        Condition.equal(col, key).and(period).
        and((sklDok.and(Condition.in("CSKL", skl))).
        or(orgDok.and(Condition.in("CSKL", knj, "CORG")))).qualified("doki") +
        " GROUP BY doki.cskl, doki.god, doki.vrdok, doki.brdok";

    System.out.println(query);
    raProcess.setMessage("Dohvat raèuna ...", false);
    ds = ut.getNewQueryDataSet(query, false);
    raProcess.openDataSet(ds);
    raProcess.setMessage("Obrada raèuna ...", false);
    raProcess.checkClosing();

    int neprokBroj = 0, totalBroj = ds.getRowCount();
    BigDecimal totalProd, totalRuc, neprokProd, neprokRuc;
    BigDecimal[] mjProd = new BigDecimal[12];
    BigDecimal[] mjRuc = new BigDecimal[12];
    for (int i = 0; i < 12; i++)
      mjProd[i] = mjRuc[i] = z;
    totalProd = totalRuc = neprokProd = neprokRuc = z;
    Variant v = new Variant();
    Calendar c = Calendar.getInstance();

    for (ds.first(); ds.inBounds(); ds.next()) {
      raProcess.checkClosing();
      totalProd = totalProd.add(ds.getBigDecimal("UIRAC"));
      ds.getVariant("UIMAR", v);
      totalRuc = totalRuc.add(v.getAsBigDecimal());
      c.setTime(ds.getTimestamp("DATDOK"));
      int mj = c.get(c.MONTH);
      mjProd[mj] = mjProd[mj].add(ds.getBigDecimal("UIRAC"));
      mjRuc[mj] = mjRuc[mj].add(v.getAsBigDecimal());
      if (!ds.getString("STATKNJ").equalsIgnoreCase("K")) {
        neprokProd = neprokProd.add(ds.getBigDecimal("UIRAC"));
        neprokRuc = neprokRuc.add(v.getAsBigDecimal());
        ++neprokBroj;
      }
    }

    raProcess.setMessage("Priprema prikaza rezultata ...", false);
    raProcess.checkClosing();

/*    "PROKP",
    "DOSPPOT", "DOSPSALDO",
    "NDOPOT", "NDOSALDO", "NEPROK", "NEPROKPROS", "NEPROKRUCPROS"}; */

    ds = pan.getData();
    ds.enableDataSetEvents(false);
    ds.setBigDecimal("PROKDUG", totalDug);
    ds.setBigDecimal("PROKPOT", totalPot);
    ds.setBigDecimal("PROKSALDO", totalDug.subtract(totalPot));

    ds.setBigDecimal("OTDUG", openDug);
    ds.setBigDecimal("OTP", rut.findPostotak(totalDug, openDug));
    ds.setBigDecimal("OTPOT", openPot);
    ds.setBigDecimal("OTSALDO", openDug.subtract(openPot));

    ds.setBigDecimal("DOSPDUG", dospDug);
    ds.setBigDecimal("DOSPP", rut.findPostotak(totalDug, dospDug));

    ds.setBigDecimal("NDODUG", nedospDug);
    ds.setBigDecimal("NDOP", rut.findPostotak(totalDug, nedospDug));

    ds.setInt("NEPROKBROJ", neprokBroj);
    ds.setBigDecimal("NEPROK", neprokProd);
    ds.setBigDecimal("NEPROKPROS", neprokBroj == 0 ? z :
       neprokProd.divide(new BigDecimal(neprokBroj), BigDecimal.ROUND_HALF_UP));
    ds.setBigDecimal("NEPROKRUCPROS", neprokBroj == 0 ? z :
       neprokRuc.divide(new BigDecimal(neprokBroj), BigDecimal.ROUND_HALF_UP));

    ds.setInt("UKUPBROJ", totalBroj);
    ds.setBigDecimal("UKUP", totalProd);
    ds.setBigDecimal("UKUPPROS", totalBroj == 0 ? z :
       totalProd.divide(new BigDecimal(totalBroj), BigDecimal.ROUND_HALF_UP));
    ds.setBigDecimal("UKUPRUCPROS", totalBroj == 0 ? z :
       totalRuc.divide(new BigDecimal(totalBroj), BigDecimal.ROUND_HALF_UP));
    
    ds.setBigDecimal("TOTAL", totalDug.subtract(totalPot).add(neprokProd));
    ds.setBigDecimal("LIMIT", Aus.zero2);
    ds.setBigDecimal("RAZLIKA", Aus.zero2);
    if (col.equals("CPAR") &&	ld.raLocate(dm.getPartneri(), "CPAR", Integer.toString(key))) {
    	ds.setBigDecimal("LIMIT", dm.getPartneri().getBigDecimal("LIMKRED"));
    	if (ds.getBigDecimal("LIMIT").signum() > 0)
    		Aus.sub(ds, "RAZLIKA", "LIMIT", "TOTAL");
    }

    for (int i = 0; i < 12; i++)
      pan.setMonthVals(i, mjProd[i], mjRuc[i]);
    ds.enableDataSetEvents(true);
    ds.goToRow(ds.getRow());
  }
}

