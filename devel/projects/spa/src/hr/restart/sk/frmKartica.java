/****license*****************************************************************
**   file: frmKartica.java
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
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraTable2;
import hr.restart.swing.dataSetTableModel;
import hr.restart.swing.raTableRunningSum;
import hr.restart.swing.raTableSumRow;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;
import hr.restart.util.startFrame;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

public class frmKartica extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();

  dM dm = dM.getDataModule();

  Valid vl = Valid.getValid();

  lookupData ld = lookupData.getlookupData();

  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();

  static frmKartica frm;

  raNavAction aPok, aMog, aMogAll, aPres;

  frmCover stavke;

  jpMatchDetail jpDetail;

  presKartica pres;

  String nazpar, csk, oldcsk, corg, nazorg, valuta;

  int cpar, vrsta, status;

  boolean kupci, datdok, datdosp, changed;

  boolean ignoreNav, dom;

  raVrdokMatcher vmat = new raVrdokMatcher();

  Timestamp dfrom, dto;
  
  String beginGod;
  
  public static frmKartica getInstance() {
    return frm;
  }
  
  
  /* Popravlja saldo racuna koji su pokriveni uplatama NAKON krajnjeg datuma
   * perioda (ne i one PRIJE pocetnog datuma, ne znam cemu bi to sluzilo.)
   */
  void modifySaldoOutOfRange(boolean refresh) {
    Condition c = Aus.getKnjigCond().and(Condition.equal("CPAR", cpar)).
            and(Aus.getVrdokCond(kupci)).
            and(Condition.where(datdok ? "DATDOK" : "DATUMKNJ", Condition.AFTER, dto));

    // pamti trenutni polozaj u datasetu.
    QueryDataSet ds = getRaQueryDataSet();
    //getColumnsBean().memorizeWidths();
    String memCsk = ds.getString("CSKSTAVKE");
    getJpTableView().enableEvents(false);
    setRaQueryDataSet(null);
    jpDetail.BindComponents(null);
    
    if (refresh) ds.refresh();

    raSaldaKonti.updateOutOfRangeSaldo(ds, c);
    
    // prodji ponovo kroz cijeli dataset i makni sve redove koji ne odgovaraju
    // uvjetima u predselekciji.
    ds.first();
    while (ds.inBounds())
      if ((ds.getString("POKRIVENO").equalsIgnoreCase("N") && status == 2) ||
          (ds.getString("POKRIVENO").equalsIgnoreCase("D") && status == 1))
        ds.emptyRow();
      else {
        if (ds.getBigDecimal("IP").signum() != 0) {
          Aus.clear(ds, "RSALDO");
          Aus.sub(ds, "RSALDO", "SALDO");
        } else Aus.set(ds, "RSALDO", "SALDO");
        ds.next();
      }

    setRaQueryDataSet(ds);
    //getColumnsBean().restoreWidths();
    jpDetail.BindComponents(ds);
    getJpTableView().setKumTak(getJpTableView().getStoZbrojiti() != null);
    getJpTableView().init_kum();

    if (!ld.raLocate(ds, "CSKSTAVKE", memCsk)) ds.first();
    getJpTableView().enableEvents(true);
    getJpTableView().fireTableDataChanged();
  }

  private void setPreselectValues() {
    kupci = pres.jpp.isKupci();
    datdok = pres.rcbDat.getSelectedIndex() == 0;
    datdosp = pres.datDosp;
    vrsta = pres.rcbVrsta.getSelectedIndex();
    status = pres.rcbStatus.getSelectedIndex();
    //    System.out.println(presq);
    cpar = pres.jpp.getCpar();
    //    ld.raLocate(dm.getPartneri(), new String[] {"CPAR"}, new String[]
    // {String.valueOf(cpar)});
    //    nazpar = dm.getPartneri().getString("NAZPAR");
    nazpar = pres.jpp.getNazpar();
    //    sysoutTEST sys = new sysoutTEST(false);
    //    sys.prn(pres.getSelRow());
    corg = pres.getCorg();
    nazorg = pres.jpc.getNaziv();
    dfrom = ut.getFirstSecondOfDay(pres.getSelRow().getTimestamp("DATDOK-from"));
    dto = ut.getLastSecondOfDay(pres.getSelRow().getTimestamp("DATDOK-to"));
  }

  /*
   * private void replaceString(StringBuffer s, String orig, String chg) { int
   * offset; while ((offset = s.toString().indexOf(orig)) != -1)
   * s.replace(offset, offset + orig.length(), chg); }
   */

  private void setTitle() {
    String[][] what = new String[][]{{"Kartica", "Kartica otvorenih dokumenata", "Kartica zatvorenih dokumenata"}, {"Ra\u010Duni", "Otvoreni ra\u010Duni", "Zatvoreni ra\u010Duni"}, {"Uplate", "Otvorene uplate", "Zatvorene uplate"}, {"Knjižne obavijesti", "Otvorene knjižne obavijesti", "Zatvorene knjižne obavijesti"}, {"Ra\u010Duni i uplate", "Otvoreni ra\u010Duni i uplate", "Zatvoreni ra\u010Duni i uplate"}};
    VarStr title = new VarStr();
    title.append(what[vrsta][status]);
    title.append(kupci ? " kupca " : " dobavlja\u010Da ").append(cpar).append(" ").append(nazpar);
    title.append("  od ").append(rdu.dataFormatter(dfrom));
    title.append(" do ").append(rdu.dataFormatter(dto));
    this.setTitle(title.toString());
  }

  /*
   * private void prepareColumns() { // int dk; // if (datdok) { // dk = 9; //
   * this.getRaQueryDataSet().getColumn("DATUMKNJ").setCaption("Datum
   * knjiženja"); // } else { // dk = 8; //
   * this.getRaQueryDataSet().getColumn("DATDOK").setCaption("Datum dokumenta"); //
   * this.getRaQueryDataSet().getColumn("DATUMKNJ").setCaption("Datum"); // } if
   * (vrsta == 0 || status == 4) { this.setVisibleCols(new int[]
   * {4,5,10,17,18,20}); } else { if ((vrsta == 2) == kupci)
   * this.setVisibleCols(new int[] {4,5,10,18,20}); else this.setVisibleCols(new
   * int[] {4,5,10,17,20}); } // this.getJpTableView().getColumnsBean(). //
   * this.getJpTableView().getColumnsBean().initialize(); }
   */

  public void refreshAll() {
    modifySaldoOutOfRange(true);
  }
  
  public void navbar_afterRefresh() {
    modifySaldoOutOfRange(false);
  }

  public void setReports() {
    if (vrsta == 0 || vrsta == 4) {
      this.getRepRunner().disableReport("hr.restart.sk.repSaldoRU");
      this.getRepRunner().enableReport("hr.restart.sk.repKartica", "Ispis u formatu duguje - potražuje");
      this.getRepRunner().enableReport("hr.restart.sk.repSaldoRU", "Ispis samo s iznosom");
    } else if (vrsta == 1) {
      this.getRepRunner().disableReport("hr.restart.sk.repKartica");
      this.getRepRunner().enableReport("hr.restart.sk.repSaldoRU", "Formatirani ispis ra\u010Duna");
    } else if (vrsta == 2) {
      this.getRepRunner().disableReport("hr.restart.sk.repKartica");
      this.getRepRunner().enableReport("hr.restart.sk.repSaldoRU", "Formatirani ispis uplata");
    } else if (vrsta == 3) {
      this.getRepRunner().disableReport("hr.restart.sk.repKartica");
      this.getRepRunner().enableReport("hr.restart.sk.repSaldoRU", "Formatirani ispis knjižnih obavijesti");
    }

    if (kupci) {
      this.getRepRunner().enableReport("hr.restart.sk.repPnP");
      this.getRepRunner().enableReport("hr.restart.sk.repOpomena");
    } else {
      this.getRepRunner().disableReport("hr.restart.sk.repPnP");
      this.getRepRunner().disableReport("hr.restart.sk.repOpomena");
    }
    //    this.getRepRunner().removeReport("hr.restart.sk.repKartica");
    //    if (vrsta == 0)
    //      this.getRepRunner().addReport("hr.restart.sk.repKartica", "Ispis
    // kartice", 5);
    //    else
    //      this.getRepRunner().addReport("hr.restart.sk.repKartica", "Ispis u
    // formatu duguje - potražuje", 5);
  }

  public void beforeShow() {
    oldcsk = "";
    changed = false;
    beginGod = Aus.getFreeYear();
    setPreselectValues();
    modifySaldoOutOfRange(false);
    //    prepareColumns();
    setTitle();
    setReports();
    findVrdok();
    //this.getJpTableView().fireTableDataChanged();
    if (this.getRaQueryDataSet().rowCount() == 0) {
      aPok.setEnabled(false);
      aMog.setEnabled(false);
      aMogAll.setEnabled(false);
    } else {
      aPok.setEnabled(true);
      aMog.setEnabled(true);
      aMogAll.setEnabled(true);
    }
  }

  public void Funkcija_ispisa() {
    String dq = this.getRaQueryDataSet().getQuery().getQueryString();
    String qstr = "SELECT cpar, corg, cgkstavke as cnaloga, vrdok, datumknj, brojdok, " 
        + "brojizv, datdok, datdosp, id, ip, saldo, cnacpl, oznval, tecaj, cskstavke, "
        + "extbrdok, opis "
        + "FROM skstavke "
        + dq.substring(dq.toLowerCase().indexOf("where"));
    if (qstr.toLowerCase().indexOf("order by") < 0) qstr = qstr + " ORDER BY cpar, datdok";

    System.err.println(qstr);
    raIspisKartica.getInstance(raIspisKartica.SINGLE).setParams(vrsta, status, kupci, datdok, dfrom, dto);
    raIspisKartica.getInstance(raIspisKartica.SINGLE).setKonto(pres.getKonto());
    raIspisKartica.getInstance(raIspisKartica.SINGLE).setPartner(cpar);
    raIspisKartica.getInstance(raIspisKartica.SINGLE).setQuery(qstr);

    qstr = "SELECT cpar, corg, vrdok, cskl, stavka, brojdok, datdok, datdosp, "
        + "id, ip, saldo, ziro, oznval, tecaj, cskstavke, "
        + "extbrdok, opis, brojizv "
        + "FROM skstavke WHERE "
        + pres.jpc.getCondition().and(Aus.getCurrGKDatumCond(dto)).
          and(Condition.equal("CPAR", cpar)).and(raSaldaKonti.isDirect() ? 
              pres.jpk.getCondition() : Condition.none).and(Aus.getVrdokCond(kupci)) + 
          /*(raSaldaKonti.isSimple() ? " AND pokriveno='N'" : " AND pvpok='N'")*/
         " AND pokriveno!='X' ORDER BY cpar, datdosp";
    System.out.println(qstr);
    raIspisKartica.getInstance(raIspisKartica.IOS).setParams(0, 1, kupci, false, dfrom, dto);
    raIspisKartica.getInstance(raIspisKartica.IOS).setKonto(pres.getKonto());
    raIspisKartica.getInstance(raIspisKartica.IOS).setPartner(cpar);
    raIspisKartica.getInstance(raIspisKartica.IOS).setQuery(qstr);
    super.Funkcija_ispisa();
  }

  public void table2Clicked() {
    if (getRaQueryDataSet().getBigDecimal("SALDO").signum() != 0)
      stavke.toMatch = true;
    startFrame.getStartFrame().showFrame(stavke);
  }

  public void SetFokus(char mode) {

  }

  public boolean Validacija(char mode) {
    return false;
  }

  public frmKartica() {
    super(2);
    try {
      frm = this;
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void findVrdok() {
    vmat.setStavka(getRaQueryDataSet());
    valuta = getRaQueryDataSet().getString("OZNVAL");
  }

  public void ZatvoriOstalo() {
    frmDugPot fdp = frmDugPot.getInstance();
    if (changed && fdp != null && fdp.isShowing())
    	fdp.refetchPartner(cpar);
  }

  public void raQueryDataSet_navigated(NavigationEvent e) {
    if (ignoreNav)
      return;
    csk = raSaldaKonti.findCSK(this.getRaQueryDataSet());
    if (!csk.equals(oldcsk)) {
      oldcsk = csk;
      if (getRaQueryDataSet().rowCount() > 0)
        findVrdok();
      jpDetail.setFieldValues();
      if (stavke.isShowing())
        stavke.beforeShow();
    }
  }
  
  public boolean isOldDok() {
    if (Valid.getValid().findYear(getRaQueryDataSet().
        getTimestamp("DATUMKNJ")).compareTo(beginGod) < 0) {
      JOptionPane.showMessageDialog(getWindow(), "Dokumenti iz zakljuèenih godina" +
          " ne mogu se mijenjati!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
      return true;
    }
    return false;
  }

  private void jbInit() throws Exception {
    boolean dispExt = frmParam.getParam("sk", "displayExt", "N", "Prikaži kolonu " +
        "dodatnog broja dokumenta na kartici SK (D/N)", true).equalsIgnoreCase("D");
    
    dm.getSkstavkeBase().getColumn("RSALDO").setCaption("Kumulativni saldo");
    this.setRaQueryDataSet(dm.getSkstavkeBase());
    this.setVisibleCols(dispExt ? new int[]{2, 4, 9, 11, 16, 17, 20} : new int[]{2, 4, 9, 16, 17, 20});
    this.setTitle("Kartica");
    this.setkum_tak(true);
    this.setstozbrojiti(new String[]{"ID", "IP", "SALDO"});
    jpDetail = new jpMatchDetail();
    this.setRaDetailPanel(jpDetail);
    jpDetail.BindComponents(this.getRaQueryDataSet());
    jpDetail.setBorder(null);
    //    this.getJpTableView().addTableModifier(new skCoverTableModifier());
    getJpTableView().getColumnsBean().setSaveSettings(true);
    getJpTableView().getColumnsBean().setSaveName(getClass().getName());
    this.setSort(new String[]{"DATDOK"});

    this.getRepRunner().addReport("hr.restart.sk.repKartica", "hr.restart.sk.repKartica", "Kartica", "Ispis u formatu duguje - potražuje");
    this.getRepRunner().addReport("hr.restart.sk.repSaldoRU", "hr.restart.sk.repSaldoRU", "SaldoRU", "Ispis samo s iznosom");
    this.getRepRunner().addReport("hr.restart.sk.repKumKartica", "hr.restart.sk.repKartica", "KumKartica", "Ispis s kumulativnim saldom");    
    this.getRepRunner().addReport("hr.restart.sk.repIOS", "hr.restart.sk.repIOS", "IOS", "Ispis otvorenih stavki partnera");
    this.getRepRunner().addReport("hr.restart.sk.repDosp","hr.restart.sk.repKarticaDosp","Dosp", "Ispis dospjelih raèuna partnera");
    this.getRepRunner().addReport("hr.restart.sk.repPnP","hr.restart.sk.repOpomena","PodsjetnikNaPlacanje", "Ispis podsjetnika za plaæanje partneru");
    this.getRepRunner().addReport("hr.restart.sk.repOpomena", "hr.restart.sk.repOpomena", "Opomena", "Ispis opomene partneru");
    
    beginGod = Aus.getFreeYear();
    
    removeRnvCopyCurr();
    this.getNavBar().removeStandardOptions(new int[]{raNavBar.ACTION_ADD, raNavBar.ACTION_DELETE, raNavBar.ACTION_TOGGLE_TABLE, raNavBar.ACTION_UPDATE});

    stavke = (frmCover) startFrame.getStartFrame().showFrame("hr.restart.sk.frmCover", 15, "Pokrivanje", false);
    //    stavke.setBase(this);

    final JraTable2 tab = (JraTable2) getJpTableView().getMpTable();
    final raTableSumRow summer = ((dataSetTableModel) tab.getModel()).getTableSumRow();
    summer.setCustomSummer(new raTableSumRow.CustomSummer() {
      public BigDecimal getSum(String col) {
        if (!col.equalsIgnoreCase("SALDO") || status == 2) return null;
        //if (status == 1) {
          BigDecimal sal = raSaldaKonti.n0;          
          Variant v = new Variant();
          for (int i = 0; i < getRaQueryDataSet().getRowCount(); i++) {
            getRaQueryDataSet().getVariant("ID", i, v);
            boolean id = v.getBigDecimal().signum() != 0;
            getRaQueryDataSet().getVariant("SALDO", i, v);
            if (id) sal = sal.add(v.getBigDecimal());
            else sal = sal.subtract(v.getBigDecimal());
          }
          return kupci ? sal : sal.negate();
        /*}
        BigDecimal id = (BigDecimal) summer.getValueAt(null, 
          getRaQueryDataSet().getRowCount(), tab.getModelColumnIndex("ID"));
        BigDecimal ip = (BigDecimal) summer.getValueAt(null, 
          getRaQueryDataSet().getRowCount(), tab.getModelColumnIndex("IP"));
        if (id == null || ip == null) return null;
        return kupci ? id.subtract(ip) : ip.subtract(id);*/
      }
    });
    getJpTableView().addTableModifier(new raTableRunningSum("RSALDO"));
    
    pres = new presKartica(this);
    this.addOption(aPok = new raNavAction("Pokriveni dokumenti", raImages.IMGSTAV, KeyEvent.VK_F6) {
      public void actionPerformed(ActionEvent e) {
        //        frmKartica.this.setEnabled(false);
        stavke.toMatch = false;
        stavke.toMatchAll = false;
        startFrame.getStartFrame().showFrame(stavke);//stavke.show();
      }
    }, 0);
    this.addOption(aMog = new raNavAction("Pokrivanje / Raskrivanje", raImages.IMGHISTORY, KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
        //        frmKartica.this.setEnabled(false);
        if (isOldDok()) return;
        stavke.toMatch = true;
        stavke.toMatchAll = false;
        startFrame.getStartFrame().showFrame(stavke);//stavke.show();
        //        stavke.addMatch();
      }
    }, 1);
    this.addOption(aMogAll = new raNavAction("Pokrivanje / Raskrivanje teèajnih razlika", raImages.IMGMOVIE, KeyEvent.VK_F8) {
      public void actionPerformed(ActionEvent e) {
        //        frmKartica.this.setEnabled(false);
        if (isOldDok()) return;
        stavke.toMatch = true;
        stavke.toMatchAll = true;
        startFrame.getStartFrame().showFrame(stavke);//stavke.show();
        //        stavke.addMatch();
      }
    }, 2);
    this.addOption(aPres = new raNavAction("Predselekcija", raImages.IMGZOOM, KeyEvent.VK_F12) {
      public void actionPerformed(ActionEvent e) {
        pres.setInsideCall();
        pres.showPreselect(frmKartica.this, "Pojedina\u010Dni pregled");
      }
    }, 3, false);
  }  
}