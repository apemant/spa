/****license*****************************************************************
**   file: frmZbirno.java
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
import hr.restart.baza.Skstavke;
import hr.restart.baza.dM;
import hr.restart.swing.JraKeyListener;
import hr.restart.util.Aus;
import hr.restart.util.Int2;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raFrame;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;
import hr.restart.util.raNavAction;
import hr.restart.util.raProcess;
import hr.restart.util.startFrame;
import hr.restart.util.reports.JTablePrintRun;
import hr.restart.util.reports.raRunReport;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;


public class frmZbirno extends raFrame {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();

  static frmZbirno frm;

  JTablePrintRun printer = new JTablePrintRun();
  raJPTableView jp = new raJPTableView() {
    public void mpTable_doubleClicked() {
      doubleClick();
    }
    public void mpTable_killFocus(java.util.EventObject e) {
      jp.getColumnsBean().focusCombo();
    }
    public void navBar_afterRefresh() {
      if (frmZbirno.this.isShowing())
        findDataInProcess();
    }
  };

  raNavAction aPok, aMog;

  raSelectValuta val = new raSelectValuta();

//  jpKarticaDetail jpDetail;
  presZbirnoGodina pres;
//  raSelectTableModifier stm;

  String presq, nazpar, corg, nazorg, mjesto, zup, agent, konto;
  int cpar;
  boolean kupci, resize = true, ps;
  Timestamp dfrom, dto;

  String oznval;
  BigDecimal tecaj;

//  StorageDataSet repQDS = new StorageDataSet();
  QueryDataSet zbir = new QueryDataSet() {
    public boolean saveChangesSupported() {
      return false;
    }
    public void refresh() {
      //if (frmZbirno.this.isShowing())
      //   findDataInProcess();
    }
  };

  public static frmZbirno getInstance() {
    return frm;
  }

  public frmZbirno() {
    try {
      frm = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public QueryDataSet getrepQDS() {
    return zbir;
  }

  public String getMjesto() {
    if (agent != null) return agent;
    else if (mjesto != null) return mjesto;
    else if (zup != null) return zup;
    else return "";
  }

  public String getLabelMjesto() {
    if (agent != null) return "Agent :";
    else if (mjesto != null) return "Grad :";
    else if (zup != null) return "Županija :";
    else return "";
  }

  public boolean isKupci() {
    return kupci;
  }

  public raRunReport getRepRunner() {
    jp.getNavBar().getColBean().setRaJdbTable(jp.getMpTable());
    printer.setInterTitle(getClass().getName());
    printer.setColB(jp.getNavBar().getColBean());
    printer.setRTitle(this.getTitle());
    return printer.getReportRunner();
  }

  private void setPreselectValues() {
    kupci = pres.jpp.isKupci();
    presq = pres.getPresCondition().toString();
    cpar = pres.jpp.getCpar();
//    ld.raLocate(dm.getPartneri(), new String[] {"CPAR"}, new String[] {String.valueOf(cpar)});
//    nazpar = dm.getPartneri().getString("NAZPAR");
    nazpar = pres.jpp.getNazpar();
//    sysoutTEST sys = new sysoutTEST(false);
//    sys.prn(pres.getSelRow());
    corg = pres.getCorg();
    nazorg = pres.jpc.getNaziv();
    mjesto = pres.getMjesto();
    agent = pres.getAgent();
    zup = pres.getZup();
    //god = pres.getGodina();
    konto = pres.getKonto();
    
    if (ps != pres.isPS()) resize = true;
    ps = pres.isPS();
//    dfrom = ut.getFirstSecondOfDay(pres.getSelRow().getTimestamp("DATUMKNJ-from"));
//    dto = ut.getLastSecondOfDay(pres.getSelRow().getTimestamp("DATUMKNJ-to"));
    dto = ut.getLastSecondOfDay(pres.getSelRow().getTimestamp("DATUMKNJ-to"));
    dfrom = Aus.getGkYear(dto);
    /*dto = ut.getYearEnd(god);
    if (dto.after(ut.getLastSecondOfDay(vl.getToday())))
        dto = ut.getLastSecondOfDay(vl.getToday());*/
  }

  void setTitle() {
    VarStr title = new VarStr();
    title.append("Zbirni pregled ");
    if (cpar == 0) {
      title.append(kupci ? "kupaca " : "dobavlja\u010Da ");
      if (agent != null) title.append(" (agent - ").append(agent).append(") ");
      else if (mjesto != null) title.append(" (grad - ").append(mjesto).append(") ");
      else if (zup != null) title.append(" (županija - ").append(zup).append(") ");
    } else {
      title.append(kupci ? "kupca " : "dobavlja\u010Da ");
      title.append(cpar).append(" ").append(nazpar).append(" ");
    }
    title.append("  od ").append(rdu.dataFormatter(dfrom));
    title.append(" do ").append(rdu.dataFormatter(dto));
    //title.append(" za ").append(god).append(". godinu");
    if (!raSaldaKonti.isDomVal(oznval))
      title.append(" (").append(oznval).append(")");
    setTitle(title.toString());
  }
  
  void findDataInProcess() {
    raProcess.runChild(this.getWindow(), new Runnable() {
      public void run() {
        findData();
      }
    });
  }
  
  PartnerCache pCache;
  Map zbirMap;
  
  void findData() {
    
    pCache = new PartnerCache(kupci);
    zbirMap = new HashMap();
    
    raProcess.setMessage("Dohvat dokumenata salda konti ...", true);
    
    QueryDataSet docs = ut.getNewQueryDataSet(
        "SELECT vrdok,cpar,datumknj,id,ip FROM skstavke WHERE "+
        presq + " AND "+Aus.getVrdokCond(kupci)+" AND pokriveno!='X' AND "+
        Aus.getCurrGKDatumCond("DATUMKNJ", dto), false);
    docs.open();
    
    BigDecimal mulVal = new BigDecimal(1);
    if (!raSaldaKonti.isDomVal(oznval)) {
      if (ld.raLocate(dm.getValute(), "OZNVAL", oznval))
        mulVal = new BigDecimal(dm.getValute().getInt("JEDVAL")).
                     divide(tecaj, 10, BigDecimal.ROUND_HALF_UP);
    }
    
    raProcess.setMessage("Zbrajanje i priprema kumulativa...", false);
    
    Timestamp d2 = ut.addDays(Aus.getGkYear(dto), 1);
    PartnerCache.Data pData;
    for (docs.first(); docs.inBounds(); docs.next()) {
      if ((pData = pCache.getData(docs.getInt("CPAR"))) != null) {
        if ((cpar == 0 || cpar == docs.getInt("CPAR")) && 
            pres.checkPartner(pData.getAgent(), pData.getZup(), pData.getPbr(), pData.getGrupa())) {
          Integer ip = new Integer(docs.getInt("CPAR"));
          ZbirData zd = (ZbirData) zbirMap.get(ip);
          if (zd == null) zbirMap.put(ip, zd = new ZbirData());
          if (docs.getTimestamp("DATUMKNJ").before(d2)) {
            zd.pocR = zd.pocR.add(docs.getBigDecimal(kupci ? "ID" : "IP"));
            zd.pocU = zd.pocU.add(docs.getBigDecimal(kupci ? "IP" : "ID"));
          } else {
            zd.prometR = zd.prometR.add(docs.getBigDecimal(kupci ? "ID" : "IP"));
            zd.prometU = zd.prometU.add(docs.getBigDecimal(kupci ? "IP" : "ID"));
          }
        }
      }
    }
    
    raProcess.setMessage("Priprema prikaza...", false);
    zbir.open();
    jp.enableEvents(false);
    zbir.empty();
    
    SortedSet sortedZbir = new TreeSet(zbirMap.keySet());
    for (Iterator i = sortedZbir.iterator(); i.hasNext(); ) {
      Integer ip = (Integer) i.next();
      ZbirData zd = (ZbirData) zbirMap.get(ip);
      zbir.insertRow(false);
      zbir.setInt("CPAR", ip.intValue());
      zbir.setString("NAZPAR", pCache.getName(ip.intValue()));
      if (ld.raLocate(dm.getAgenti(), "CAGENT", ip.toString()))
        zbir.setString("NAZAGENT", dm.getAgenti().getString("NAZAGENT"));
      zbir.setBigDecimal("POCSTRAC", zd.pocR);
      zbir.setBigDecimal("POCSTUPL", zd.pocU);
      zbir.setBigDecimal("PROMETR", zd.prometR);
      zbir.setBigDecimal("PROMETU", zd.prometU);
      BigDecimal saldo = zd.prometR.subtract(zd.prometU);
      if (ps) saldo = saldo.add(zd.pocR).subtract(zd.pocU);
      zbir.setBigDecimal("SALDO", saldo);
    }
    zbir.post();
    zbir.first();
    jp.enableEvents(true);
    jp.removeSelection();
    this.getRepRunner().disableReport("hr.restart.sk.repKartica");

//    jp.removeAllTableModifiers();
//    jp.addTableModifier(new raTableColumnModifier("CPAR", new String[] {"CPAR", "NAZPAR"}, part));
//    sysoutTEST sys = new sysoutTEST(false);
//    sys.prn(allpar);

  }

  private void prepareIspis() {
    Condition sel = jp.getSelectCondition();
    if (sel == null) sel = Condition.equal("CPAR", zbir);
    String qstr = "SELECT cpar, corg, cgkstavke as cnaloga, vrdok, datumknj, brojdok, brojizv, datdok, datdosp, "+
                  "id, ip, saldo, cnacpl, tecaj, oznval, cskstavke, " +
                  "extbrdok, opis"+
                  " FROM skstavke WHERE " + presq + " AND pokriveno!='X'"+
                  " AND "+Condition.between("DATUMKNJ",  dfrom, dto)+
                  " AND vrdok in " + (kupci ? "('IRN','UPL','OKK')" : "('URN','IPL','OKD')") +
                  " AND " + sel.and(pres.jpc.getCondition()) + " ORDER BY cpar,datumknj";
    System.out.println(qstr);
    raIspisKartica.getInstance(raIspisKartica.SINGLE).setParams(0, 0, kupci, false, dfrom, dto);
    raIspisKartica.getInstance(raIspisKartica.SINGLE).setSelection(jp.getSelectionTracker());
    if (jp.getSelectCount() > 1) prepareSortedTotals();
    raIspisKartica.getInstance(raIspisKartica.SINGLE).setKonto(konto);
    raIspisKartica.getInstance(raIspisKartica.SINGLE).setQuery(qstr);
  }
  
  void prepareSortedTotals() {
    raIspisKartica rik = raIspisKartica.getInstance(raIspisKartica.SINGLE);
    int row = zbir.getRow();
    jp.enableEvents(false);
    for (zbir.first(); zbir.inBounds(); zbir.next())
      if (jp.getSelectionTracker().isSelected(zbir))
        rik.insertEmptyTotal(zbir.getInt("CPAR"));
    zbir.goToRow(row);
    jp.enableEvents(true);
  }

  public void beforeShow() {
    setPreselectValues();
    val.reset(vl.getToday());
    findDataInProcess();
    setTitle();
    jp.fireTableDataChanged();
    jp.getColumnsBean().eventInit();
    if (resize) {
      resize = false;
      this.pack();
    }
    /*jp.setVisibleCols(new int[] {0,1,2,3,4,5,6,7});
    jp.getColumnsBean().initialize();
    if (!ps) jp.setVisibleCols(new int[] {0,1,5,6,7});
    else jp.setVisibleCols(new int[] {0,1,3,4,5,6,7});
    jp.getColumnsBean().initialize();*/
  }

  public void show() {
    beforeShow();
    super.show();
  }

  public String getNaslov() {
    return "ZBIRNI PREGLED "+(isKupci() ? "KUPACA" : "DOBAVLJAÈA")+
        " PO PROMETU I SALDU" + (raSaldaKonti.isDomVal(oznval) ? "" : (" ("+oznval+")"))
        + "\nna dan "+rdu.dataFormatter(dto);
  }

  private void setDataSet() {
    zbir.setColumns(new Column[] {
      (Column) dm.getPartneri().getColumn("CPAR").clone(),
      (Column) dm.getPartneri().getColumn("NAZPAR").clone(),
      (Column) dm.getAgenti().getColumn("NAZAGENT").clone(),
      (Column) dm.getSkkumulativi().getColumn("POCSTRAC").clone(),
      (Column) dm.getSkkumulativi().getColumn("POCSTUPL").clone(),
      (Column) dm.getSkkumulativi().getColumn("PROMETR").clone(),
      (Column) dm.getSkkumulativi().getColumn("PROMETU").clone(),
      (Column) dm.getSkstavke().getColumn("SALDO").clone()
    });
    zbir.getColumn("CPAR").setWidth(7);
//    zbir.getColumn("ID").setCaption("Ra\u010Duni");
//    zbir.getColumn("IP").setCaption("Uplate");
    zbir.getColumn("NAZPAR").setCaption("Naziv partnera");
    zbir.getColumn("NAZAGENT").setCaption("Agent");
    zbir.getColumn("NAZAGENT").setWidth(20);
    zbir.setLocale(Aus.hr);
    zbir.open();
//    repQDS.setColumns(zbir.cloneColumns());
//    repQDS.open();
    jp.setDataSet(zbir);
    jp.setKumTak(true);
    jp.setStoZbrojiti(new String[] {"POCSTRAC", "POCSTUPL", "PROMETR", "PROMETU", "SALDO"});    
  }
  
  static class ZbirData {
    BigDecimal prometR, prometU, pocR, pocU;
    public ZbirData() {
      prometR = prometU = pocR = pocU = raSaldaKonti.n0;
    }
  }

  private void jbInit() throws Exception {
    pres = new presZbirnoGodina();
    setDataSet();
    jp.getColumnsBean().setSaveSettings(true);
    jp.getColumnsBean().setSaveName(getClass().getName());    
    jp.setVisibleCols(new int[] {0,1,5,6,7});
    this.getContentPane().add(jp);
    this.getRepRunner().addReport("hr.restart.sk.repZbirno", "hr.restart.sk.repZbirno", "Zbirno", "Ispis zbirnog pregleda");
    this.getRepRunner().addReport("hr.restart.sk.repZbirnoPS", "hr.restart.sk.repZbirno", "ZbirnoPS", "Ispis zbirnog pregleda s po\u010Detnim stanjima");
    this.getRepRunner().addReport("hr.restart.sk.repKartica", "hr.restart.sk.repKartica", "Kartica", "Grupni ispis kartica ozna\u010Denih partnera");
    this.getRepRunner().addReport("hr.restart.sk.repKumKartica", "hr.restart.sk.repKartica", "KumKartica", "Ispis kartica s kumulativnim saldom");

    jp.installSelectionTracker("CPAR");
    this.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_F10) {
          e.consume();
//        } else if (e.getKeyCode() == e.VK_ESCAPE) {
//          e.consume();
//          frmZbirno.this.hide();
//        } else if (e.getKeyCode() == e.VK_ENTER) {
//          if (zbir.rowCount() > 0)
//            toggleSelect();
//          e.consume();
        }
      }
    });

//    jp.getNavBar().addOption(new raNavAction("Ozna\u010Di", raImages.IMGADD, KeyEvent.VK_ENTER) {
//      public void actionPerformed(ActionEvent e) {
//      }
//    });
    jp.getNavBar().addOption(new raNavAction("Detalji", raImages.IMGSTAV, KeyEvent.VK_F6) {
      public void actionPerformed(ActionEvent e) {
        if (zbir.rowCount() > 0)
          doubleClick();
      }
    });
    /*jp.getNavBar().addOption(new raNavAction("Pokrivanje po broju", raImages.IMGHISTORY,KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
        if (zbir.rowCount() > 0) tryMatching();
      }
    });
    jp.getNavBar().addOption(new raNavAction("Pokrivanje po saldu", raImages.IMGALIGNJUSTIFY, KeyEvent.VK_F8) {
      public void actionPerformed(ActionEvent e) {
        if (zbir.rowCount() > 0) {
          raProcess.runChild(getWindow(), new Runnable() {
            public void run() {
              Condition c = jp.getSelectCondition();
              if (c == null) c = Condition.equal("CPAR", zbir.getInt("CPAR"));
              raOptimisticMatch.find(10, c.and(Aus.getVrdokCond(kupci)));
            }
          });
          if (raProcess.isCompleted() && raOptimisticMatch.isAnythingFound())
            raOptimisticMatch.showResultDialog(getWindow(), "Prikaz dokumenata za pokrivanje po saldu");
          else if (!raProcess.isInterrupted()) JOptionPane.showMessageDialog(
              getWindow(), "Nema dokumenata za pokrivanje po saldu!",
              "Poruka", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    });*/
    jp.getNavBar().addOption(new raNavAction("Valuta", raImages.IMGMOVIE,KeyEvent.VK_F11) {
      public void actionPerformed(ActionEvent e) {
        val.show(frmZbirno.this.getWindow());
        if (val.getOznval() != null) {
          oznval = val.getOznval();
          tecaj = val.getTecaj();
          findDataInProcess();
          setTitle();
        }
      }
    });
    jp.getNavBar().addOption(new raNavAction("Ispis", raImages.IMGPRINT, KeyEvent.VK_F5) {
      public void actionPerformed(ActionEvent e) {
        prepareReports();
        prepareIspis();
        getRepRunner();
        int row = zbir.getRow();
        jp.enableEvents(false);
        printer.runIt();
        zbir.goToRow(row);
        jp.enableEvents(true);
        jp.removeSelection();
      }
    });
    jp.getNavBar().addOption(new raNavAction("Predselekcija", raImages.IMGZOOM,KeyEvent.VK_F12) {
      public void actionPerformed(ActionEvent e) {
        pres.showPreselect(frmZbirno.this, "Zbirni pregled za godini");
        if (frmZbirno.this.isShowing()) beforeShow();
      }
    });
    jp.getNavBar().addOption(new raNavAction("Izlaz", raImages.IMGX, KeyEvent.VK_ESCAPE) {
      public void actionPerformed(ActionEvent e) {
        frmZbirno.this.hide();
      }
    });

    jp.initKeyListener(this);

     this.addKeyListener(new JraKeyListener());
//    this.setSize(640, 400);
//    jp.setSize(640, 400);
  }

  Int2 total = new Int2();
  private void tryMatching() {
    total.clear();
    raProcess.runChild(jp, new Runnable() {
      public void run() {
        if (jp.getSelectCount() == 0) total = matchPartner(zbir.getInt("CPAR"), false);
        else {
          Integer[] cps = (Integer[]) jp.getSelection();
          for (int i = 0; i < cps.length; i++)
            total.add(matchPartner(cps[i].intValue(), true));
        }
      }
    });

    if (total.one == 0 && raProcess.isCompleted())
      JOptionPane.showMessageDialog(jp, "Nema stavaka za pokrivanje!",
                                    "Poruka", JOptionPane.INFORMATION_MESSAGE);
    else if (total.one > 0) JOptionPane.showMessageDialog(jp, Aus.getNumDep(total.one,
        "Pokriven "+total.one+(total.one == 1 ? " broj" : " razlièit broj"),
        "Pokrivena "+total.one+" razlièita broja",
        "Pokriveno "+total.one+" razlièitih brojeva") +
        " na ukupno "+Aus.getNum(total.two, "stavci.", "stavke.", "stavaka."),
        "Poruka", JOptionPane.INFORMATION_MESSAGE);
  }

  private Int2 matchPartner(int cpar, boolean mess) {
    if (mess) raProcess.setMessage("Provjera "+
        (kupci ? "kupca " : "dobavljaèa ")+cpar+" ...", false);
    QueryDataSet sks = Skstavke.getDataModule().getTempSet(
      Aus.getKnjigCond().and(Aus.getFreeYearCond()).
      and(Condition.equal("CPAR", String.valueOf(cpar))).
      and(Aus.getVrdokCond(kupci)).
      and(Condition.equal(raSaldaKonti.colPok(), "N")));
    raProcess.openScratchDataSet(sks);
    System.out.println(sks.getQuery().getQueryString());
    return raSaldaKonti.matchBrojdok(sks);
  }

  void doubleClick() {
    if (zbir.rowCount() > 0) {
      frmKartica fk = (frmKartica) startFrame.getStartFrame().
                      showFrame("hr.restart.sk.frmKartica", 15, "Kartica", false);

      fk.pres.setInsideCall();
      System.out.println("corg = "+corg);
      fk.pres.setForceCpar(kupci, zbir.getInt("CPAR"), corg, konto, dfrom, dto);
//      fk.pres.initPreSeldialog();
      fk.pres.doSelect();
      /*System.out.println(fk.isShowing());
      if (fk.isShowing()) {
        fk.beforeShow();
        fk.fro
      } else*/ startFrame.getStartFrame().showFrame(fk);
//      fk.pres.showPreselect(fk, "Pojedina\u010Dni pregled");
    }
  }

  private void prepareReports() {
    this.getRepRunner().enableReport("hr.restart.sk.repKartica");
    this.getRepRunner().getReport("hr.restart.sk.repKartica").
        setTitle(jp.getSelectCount() <= 1 ?
        "Ispis kartice odabranog partnera" :
        "Grupni ispis kartica odabranih partnera");
  }
}
