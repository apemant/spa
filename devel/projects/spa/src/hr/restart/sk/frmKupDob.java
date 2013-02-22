/****license*****************************************************************
**   file: frmKupDob.java
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
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raFrame;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;
import hr.restart.util.raNavAction;
import hr.restart.util.raProcess;
import hr.restart.util.reports.JTablePrintRun;
import hr.restart.util.reports.raRunReport;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;


public class frmKupDob extends raFrame {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();

  static frmKupDob frm;

  JTablePrintRun printer = new JTablePrintRun();
  raJPTableView jp = new raJPTableView() {
    public void mpTable_doubleClicked() {
      doubleClick();
    }
    public void mpTable_killFocus(java.util.EventObject e) {
      jp.getColumnsBean().focusCombo();
    }
    public void navBar_afterRefresh() {
      if (frmKupDob.this.isShowing())
        findDataInProcess();
    }
  };

  raNavAction aPok, aMog;

  String oznval;
  BigDecimal tecaj;
  raSelectValuta val = new raSelectValuta();

//  jpKarticaDetail jpDetail;
  presKupDob pres;

  String presq, nazpar, corg, nazorg, mjesto, konto;
  int cpar;
  boolean resize = true, ps;
  Timestamp dfrom, dto;

//  StorageDataSet repQDS = new StorageDataSet();
  QueryDataSet zbir = new QueryDataSet() {
    public boolean saveChangesSupported() {
      return false;
    }
    public void refresh() {
    }
  };

  public static frmKupDob getInstance() {
    return frm;
  }

  public frmKupDob() {
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
    return mjesto;
  }

  public raRunReport getRepRunner() {
    jp.getNavBar().getColBean().setRaJdbTable(jp.getMpTable());
    printer.setInterTitle(getClass().getName());
    printer.setColB(jp.getNavBar().getColBean());
    printer.setRTitle(this.getTitle());
    return printer.getReportRunner();
  }

  private void setPreselectValues() {
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
    //god = pres.getGodina();
    konto = pres.getKonto();
//    dfrom = ut.getFirstSecondOfDay(pres.getSelRow().getTimestamp("DATUMKNJ-from"));
//    dto = ut.getLastSecondOfDay(pres.getSelRow().getTimestamp("DATUMKNJ-to"));
    dto = ut.getLastSecondOfDay(pres.getSelRow().getTimestamp("DATUMKNJ-to"));
    dfrom = Aus.getGkYear(dto);
    
    ps = pres.isPS();
    /*dfrom = ut.getYearBegin(god);
    dto = ut.getYearEnd(god);
    if (dto.after(ut.getLastSecondOfDay(vl.getToday())))
        dto = ut.getLastSecondOfDay(vl.getToday()):*/
  }

  void setTitle() {
    VarStr title = new VarStr();
    title.append("Usporedni pregled ");
    if (cpar == 0) {
      title.append("kupaca-dobavlja\u010Da ");
    } else {
      title.append("kupca-dobavlja\u010Da ");
      title.append(cpar).append(" ").append(nazpar).append(" ");
    }
    title.append("  od ").append(rdu.dataFormatter(dfrom));
    title.append(" do ").append(rdu.dataFormatter(dto));
    //title.append(" za ").append(god).append(". godinu");
    if (!raSaldaKonti.isDomVal(oznval))
      title.append(" (").append(oznval).append(")");
    setTitle(title.toString());
  }

  /*private void addRow(DataSet row, BigDecimal mult) {
    boolean kupac = row.getString("ULOGA").equalsIgnoreCase("K");
    String rac = kupac ? "ID" : "IP";
    String upl = kupac ? "IP" : "ID";
    zbir.setBigDecimal(rac, zbir.getBigDecimal(rac).add(row.getBigDecimal("PROMETR").multiply(mult).setScale(2, BigDecimal.ROUND_HALF_UP)));
    zbir.setBigDecimal(upl, zbir.getBigDecimal(upl).add(row.getBigDecimal("PROMETU").multiply(mult).setScale(2, BigDecimal.ROUND_HALF_UP)));
    zbir.setBigDecimal("SALDO", zbir.getBigDecimal("ID").subtract(zbir.getBigDecimal("IP")));
  }*/
  
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
    
    pCache = new PartnerCache();
    zbirMap = new HashMap();
    
    raProcess.setMessage("Dohvat dokumenata salda konti ...", true);
    
    QueryDataSet docs = ut.getNewQueryDataSet(
        "SELECT vrdok,cpar,id,ip FROM skstavke WHERE "+
        presq + " AND pokriveno!='X' AND "+
        Aus.getCurrGKDatumCond("DATUMKNJ", dto), false);
    docs.open();
    
    BigDecimal mulVal = new BigDecimal(1);
    if (!raSaldaKonti.isDomVal(oznval)) {
      if (ld.raLocate(dm.getValute(), "OZNVAL", oznval))
        mulVal = new BigDecimal(dm.getValute().getInt("JEDVAL")).
                     divide(tecaj, 10, BigDecimal.ROUND_HALF_UP);
    }
    
    raProcess.setMessage("Zbrajanje i priprema kumulativa...", false);
    
    PartnerCache.Data pData;
    for (docs.first(); docs.inBounds(); docs.next()) {
      if ((pData = pCache.getData(docs.getInt("CPAR"))) != null) {
        if ((cpar == 0 || cpar == docs.getInt("CPAR")) && 
            pres.checkPartner(pData.getAgent(), pData.getZup(), pData.getPbr(), pData.getGrupa())) {
          Integer cp = new Integer(docs.getInt("CPAR"));
          ZbirData zd = (ZbirData) zbirMap.get(cp);
          if (zd == null) zbirMap.put(cp, zd = new ZbirData());

          BigDecimal id = docs.getBigDecimal("ID");
          BigDecimal ip = docs.getBigDecimal("IP");
          zd.id = zd.id.add(id);
          zd.ip = zd.ip.add(ip);
          if (raVrdokMatcher.isKup(docs)) {
            zd.kup = true;
            zd.salkup = zd.salkup.add(id).subtract(ip);
          } else {
            zd.dob = true;
            zd.saldob = zd.saldob.add(ip).subtract(id);
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
      if (ps || zd.kup && zd.dob) {
        zbir.insertRow(false);
        zbir.setInt("CPAR", ip.intValue());
        zbir.setString("NAZPAR", pCache.getName(ip.intValue()));
        if (ld.raLocate(dm.getAgenti(), "CAGENT", ip.toString()))
          zbir.setString("NAZAGENT", dm.getAgenti().getString("NAZAGENT"));
        zbir.setBigDecimal("PVID", zd.salkup);
        zbir.setBigDecimal("PVIP", zd.saldob);
        zbir.setBigDecimal("ID", zd.id);
        zbir.setBigDecimal("IP", zd.ip);
        zbir.setBigDecimal("SALDO", zd.id.subtract(zd.ip));
      }
    }
    zbir.post();
    zbir.first();
    jp.enableEvents(true);
    jp.removeSelection();
  }
  
  static class ZbirData {
    BigDecimal id, ip, salkup, saldob;
    boolean kup, dob;
    public ZbirData() {
      id = ip = salkup = saldob = raSaldaKonti.n0;
    }
  }
  
  private void prepareIspis() {
    Condition sel = jp.getSelectCondition();
    if (sel == null) sel = Condition.equal("CPAR", zbir);
    String qstr = "SELECT cpar, corg, cgkstavke as cnaloga, vrdok, datumknj, brojdok, brojizv, datdok, datdosp, "+
                  "id, ip, saldo, cnacpl, tecaj, oznval, cskstavke, " +
                  "extbrdok, opis"+
                  " FROM skstavke WHERE " + presq + " AND pokriveno!='X'"+
                  " AND "+Condition.between("DATUMKNJ",  dfrom, dto).and(sel)+
                  " ORDER BY cpar,datumknj";
    System.out.println(qstr);
    raIspisKartica rik = raIspisKartica.getInstance(raIspisKartica.SINGLE);
    rik.setParams(0, 0, true, false, dfrom, dto);
    rik.setSelection(jp.getSelectionTracker());
    if (jp.getSelectCount() > 1) prepareSortedTotals(rik);
    rik.setBoth(true);
    rik.setQuery(qstr);
    
    sel = jp.getSelectCondition();
    if (sel == null) sel = Condition.equal("CPAR", zbir);
    qstr = "SELECT cpar, corg, vrdok, cskl, stavka, brojdok, datdok, datdosp, "+
                  "id, ip, saldo, ziro, tecaj, oznval, cskstavke, " +
                  "extbrdok, opis, brojizv"+
                  " FROM skstavke WHERE " + presq +
                  " AND pokriveno != 'X' AND "+
                  Aus.getCurrGKDatumCond(dto).and(sel)+ 
                  " ORDER BY cpar, datdok";
    System.out.println(qstr);
    rik = raIspisKartica.getInstance(raIspisKartica.IOS);
    rik.setParams(0, 1, true, false, dfrom, dto);
    rik.setSelection(jp.getSelectionTracker());
    if (jp.getSelectCount() > 1) prepareSortedTotals(rik);
    rik.setBoth(true);
    rik.setQuery(qstr);
  }
  
  void prepareSortedTotals(raIspisKartica rik) {
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
    findDataInProcess();
    setTitle();
    jp.fireTableDataChanged();
    jp.getColumnsBean().eventInit();
    if (resize) {
      resize = false;
      this.pack();
    }
  }

  public void show() {
    beforeShow();

    super.show();
  }

  private void setDataSet() {
    zbir.setColumns(new Column[] {
      (Column) dm.getPartneri().getColumn("CPAR").clone(),
      (Column) dm.getPartneri().getColumn("NAZPAR").clone(),
      (Column) dm.getAgenti().getColumn("NAZAGENT").clone(),
      (Column) dm.getSkstavke().getColumn("PVID").clone(),
      (Column) dm.getSkstavke().getColumn("PVIP").clone(),
      (Column) dm.getSkstavke().getColumn("ID").clone(),
      (Column) dm.getSkstavke().getColumn("IP").clone(),
      (Column) dm.getSkstavke().getColumn("SALDO").clone()
    });
    zbir.getColumn("PVID").setCaption("Saldo kupca");
    zbir.getColumn("PVIP").setCaption("Saldo dobavljaèa");
    zbir.getColumn("ID").setCaption("Duguje");
    zbir.getColumn("IP").setCaption("Potražuje");
    zbir.getColumn("NAZPAR").setCaption("Naziv partnera");
    zbir.getColumn("NAZAGENT").setCaption("Agent");
    zbir.getColumn("NAZAGENT").setWidth(20);
    zbir.open();
//    repQDS.setColumns(zbir.cloneColumns());
//    repQDS.open();
    jp.setDataSet(zbir);
    jp.setKumTak(true);
    jp.setStoZbrojiti(new String[] {"PVID", "PVIP", "ID", "IP", "SALDO"});
    jp.setVisibleCols(new int[] {0,1,3,4,5});
  }

  private void jbInit() throws Exception {
    pres = new presKupDob();
    setDataSet();
    jp.installSelectionTracker("CPAR");
    jp.getColumnsBean().setSaveSettings(true);
    jp.getColumnsBean().setSaveName(getClass().getName());    
    jp.setVisibleCols(new int[] {0,1,3,4,7});
    this.getContentPane().add(jp);
//    this.getRepRunner().addReport("hr.restart.sk.repZbirno", "Ispis zbirnog pregleda", 42);
//    this.getRepRunner().addReport("hr.restart.sk.repZbirnoPS", "Ispis zbirnog pregleda s po\u010Detnim stanjima", 42);
    this.getRepRunner().addReport("hr.restart.sk.repIOSintTot","hr.restart.sk.repIOS","IOSint", "Skraæeni ispis otvorenih stavki");
    this.getRepRunner().addReport("hr.restart.sk.repKumKarticaTot", "hr.restart.sk.repKartica", "KumKartica", "Ispis kratica s kumulativnim saldom");

    jp.getColumnsBean().setSaveSettings(false);

//    jp.addTableModifier(new skSelectTableModifier());

    jp.getNavBar().addOption(new raNavAction("Valuta", raImages.IMGMOVIE,KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
        val.show(frmKupDob.this.getWindow());
        if (val.getOznval() != null) {
          oznval = val.getOznval();
          tecaj = val.getTecaj();
          findDataInProcess();
          setTitle();
        }
      }
    });
    jp.getNavBar().addOption(new raNavAction("Predselekcija", raImages.IMGZOOM,KeyEvent.VK_F12) {
      public void actionPerformed(ActionEvent e) {
        pres.showPreselect(frmKupDob.this, "Zbirni pregled za godini");
        if (frmKupDob.this.isShowing()) beforeShow();
      }
    });
    jp.getNavBar().addOption(new raNavAction("Ispis", raImages.IMGPRINT, KeyEvent.VK_F5) {
      public void actionPerformed(ActionEvent e) {
        getRepRunner();
        prepareIspis();
        jp.enableEvents(false);
        printer.runIt();
        jp.enableEvents(true);
      }
    });
    jp.getNavBar().addOption(new raNavAction("Izlaz", raImages.IMGX, KeyEvent.VK_ESCAPE) {
      public void actionPerformed(ActionEvent e) {
        frmKupDob.this.hide();
      }
    });

    jp.initKeyListener(this);

    //    this.addKeyListener(new JraKeyListener());
//    this.setSize(640, 400);
//    jp.setSize(640, 400);
  }

  private void doubleClick() {

  }
}
