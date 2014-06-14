/****license*****************************************************************
**   file: frmDugPot.java
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
import hr.restart.swing.JraKeyListener;
import hr.restart.swing.raExtendedTable;
import hr.restart.util.Aus;
import hr.restart.util.Util;
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
import hr.restart.util.reports.raReportDescriptor;
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

import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jb.util.TriStateProperty;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmDugPot extends raFrame {
  
  public static final int SHOW_PARAM = 0;
  public static final int SHOW_RAC = 1;
  public static final int SHOW_UPL = 2;
  
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();

  static frmDugPot frm;

//  raNavAction aPok, aMog, aPres;

  JTablePrintRun printer = new JTablePrintRun();
  raJPTableView jp = new raJPTableView() {
    public void mpTable_doubleClicked() {
      doubleClick(SHOW_PARAM);
    }
    public void mpTable_killFocus(java.util.EventObject e) {
      jp.getColumnsBean().focusCombo();
    }
    public void navBar_afterRefresh() {
      if (frmDugPot.this.isShowing())
        findDataInProcess();
    }
  };

//  HashSet sel = new HashSet();

//  raNavAction aPok, aMog, aPres;

//  jpKarticaDetail jpDetail;
  presZbirnoDan pres;
//  raSelectTableModifier stm;

  String oznval;
  String datumCol;
  BigDecimal tecaj;
  raSelectValuta val = new raSelectValuta();

  String presq, nazpar, corg, nazorg, god, mjesto, zup, konto, agent;
  int cpar;
  boolean kupci, resize = true;
  Timestamp dto, dfrom;
  BigDecimal miz, n0 = new BigDecimal(0.);

//  QueryDataSet repQDS = new QueryDataSet();
  QueryDataSet zbir = new QueryDataSet() {
    public boolean saveChangesSupported() {
      return false;
    }
    public void refresh() {
      //if (frmDugPot.this.isShowing())
      //  findDataInProcess();
    }
  };

  public static frmDugPot getInstance() {
    return frm;
  }

  public frmDugPot() {
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

  public String getNaslov() {
    return (kupci ? "POTRAŽIVANJA OD KUPACA" : "DUGOVANJA DOBAVLJAÈIMA")+" na dan "+
        rdu.dataFormatter(dto)+(raSaldaKonti.isDomVal(oznval) ? "" : (" ("+oznval+")"));
  }

  public raRunReport getRepRunner() {
    jp.getNavBar().getColBean().setRaJdbTable(jp.getMpTable());
    printer.setInterTitle(getClass().getName());
    printer.setColB(jp.getNavBar().getColBean());
    printer.setRTitle(this.getTitle());
    return printer.getReportRunner();
  }

  private void setPreselectValues() {
    System.out.println("zbirno copy presel");
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
    konto = pres.getKonto();
    
    datumCol = pres.getDatumCol();

//    dfrom = ut.getFirstSecondOfDay(pres.getSelRow().getTimestamp("DATUMKNJ-from"));
    dto = ut.getLastSecondOfDay(pres.getSelRow().getTimestamp("DATUMKNJ-to"));
    dfrom = ut.getFirstSecondOfDay(ut.addDays(dto, 1));
    miz = pres.getIznos();
    System.out.println(miz);
  }

  void setTitle() {
    VarStr title = new VarStr();
    title.append("Zbirni pregled otvorenog prometa ");
    if (cpar == 0) {
      title.append(kupci ? "kupaca " : "dobavlja\u010Da ");
      if (agent != null) title.append(" (agent - ").append(agent).append(") ");
      else if (mjesto != null) title.append(" (grad - ").append(mjesto).append(") ");
      else if (zup != null) title.append(" (županija - ").append(zup).append(") ");
    } else {
      title.append(kupci ? "kupca " : "dobavlja\u010Da ");
      title.append(cpar).append(" ").append(nazpar).append(" ");
    }
    title.append(" na dan ").append(rdu.dataFormatter(dto));
    if (!raSaldaKonti.isDomVal(oznval))
      title.append(" (").append(oznval).append(")");
    setTitle(title.toString());
  }
  
/*  private String getRacVrdok(boolean kup) {
    if (kup) return "(VRDOK='IRN' OR (VRDOK='OKK' AND ID != 0))";
    return "(VRDOK='URN' OR (VRDOK='OKD' AND IP != 0))";
  }
  
  private String getUplVrdok(boolean kup) {
    if (kup) return "(VRDOK='UPL' OR (VRDOK='OKK' AND IP != 0))";
    return "(VRDOK='IPL' OR (VRDOK='OKD' AND ID != 0))";
  }*/
  
  private void processData(Map data, boolean rac, boolean dosp, BigDecimal mulVal) {
    PartnerCache.Data pData;
    for (Iterator i = data.entrySet().iterator(); i.hasNext(); ) {
    	Map.Entry me = (Map.Entry) i.next();
    	Integer ip = (Integer) me.getKey();
    	BigDecimal sal = (BigDecimal) me.getValue();
    	
    	if (pCache != null) {
    		if (miz.signum() != 0 && sal.abs().compareTo(miz) < 0) continue;
    		if ((pData = pCache.getData(ip.intValue())) == null) continue;
            if (cpar != 0 && cpar != ip.intValue()) continue;
            if (cpar == 0 && !pres.checkPartner(pData.getAgent(), pData.getZup(), pData.getPbr(), pData.getGrupa())) continue;
    	}
      BigDecimal saldo = sal.multiply(mulVal).setScale(2, BigDecimal.ROUND_HALF_UP);
      ZbirData zd = (ZbirData) zbirMap.get(ip);
      if (zd == null) zbirMap.put(ip, zd = new ZbirData());
      if (rac) {
        zd.rac = zd.rac.add(saldo);
        if (dosp) zd.dosp = saldo;
        else zd.nedosp = saldo;
      } else {
        zd.upl = zd.upl.add(saldo);
        saldo = saldo.negate();
      }
      zd.saldo = zd.saldo.add(saldo);
    }
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
  
  
  void updateMap(Condition par, boolean inProc) {
  	Map before = new HashMap();
  	Map after = new HashMap();
  	Map oupl = new HashMap();
  	
  	String ps = "-00-00";
  	boolean isps = pres.isPS();
  	
  	if (inProc) raProcess.setMessage("Dohvat otvorenih dokumenata ...", true);
  	QueryDataSet docs = ut.getNewQueryDataSet(
        "SELECT vrdok,cpar,cskstavke,tecaj,oznval,cgkstavke,datdosp,id,ip,saldo FROM skstavke WHERE "+
        presq + " AND "+Aus.getVrdokCond(kupci).and(par)+" AND pokriveno!='X' AND "+
        Aus.getCurrGKDatumCond(datumCol, dto), false);
  	System.out.println(docs.getQuery().getQueryString());
    docs.setMetaDataUpdate(docs.getMetaDataUpdate() & ~MetaDataUpdate.ROWID);
    if (inProc) raProcess.openScratchDataSet(docs);
    else docs.open();
    docs.getColumn("CSKSTAVKE").setRowId(true);

    if (inProc) raProcess.setMessage("Popravak salda dokumenata...", false);
    // izbaci iz pokrivenih sve one koji su pokriveni uplatama nakon trazenog dana
    raSaldaKonti.updateOutOfRangeSaldo(docs, Condition.raw(presq+" AND "+
        Aus.getVrdokCond(kupci).and(par).and(Condition.from(datumCol, dfrom))));

    if (inProc) raProcess.setMessage("Zbrajanje i priprema kumulativa...", false);
    // zbroji racune po partnerima, u ovisnosti od dospjeca.
    for (docs.first(); docs.inBounds(); docs.next()) {
      if (!isps && docs.getString("CGKSTAVKE").indexOf(ps) >= 0) continue;
      Map which = raVrdokMatcher.isUplataTip(docs) ? oupl :
        docs.getTimestamp("DATDOSP").after(dto) ? after : before;
      Integer ip = new Integer(docs.getInt("CPAR"));
      BigDecimal sal = (BigDecimal) which.get(ip);
      if (sal == null) sal = docs.getBigDecimal("SALDO");
      else sal = sal.add(docs.getBigDecimal("SALDO"));
      which.put(ip, sal);
    }
    
    BigDecimal mulVal = new BigDecimal(1.0);
    if (!raSaldaKonti.isDomVal(oznval)) {
      if (ld.raLocate(dm.getValute(), "OZNVAL", oznval))
        mulVal = new BigDecimal(dm.getValute().getInt("JEDVAL")).
                     divide(tecaj, 10, BigDecimal.ROUND_HALF_UP);
    }
    
//  raèuni - dospjeli
    processData(before, true, true, mulVal);
    
    // raèuni - nedospjeli
    processData(after, true, false, mulVal);
    
    // uplate
    processData(oupl, false, false, mulVal);
    
  }
  
  public void refetchPartner(int cpar) {
  	if (!ld.raLocate(zbir, "CPAR", Integer.toString(cpar))) return;
  	Integer ip = new Integer(zbir.getInt("CPAR"));
  	zbirMap.put(ip, new ZbirData());

  	updateMap(Condition.equal("CPAR", zbir), false);
  	 
    ZbirData zd = (ZbirData) zbirMap.get(ip);
    zbir.setBigDecimal("RAC", zd.rac);
    zbir.setBigDecimal("DOSP", zd.dosp);
    zbir.setBigDecimal("NEDOSP", zd.nedosp);
    zbir.setBigDecimal("UPL", zd.upl);
    zbir.setBigDecimal("SALDO", zd.saldo);
    zbir.post();
    
    jp.fireTableDataChanged();
  }
  
  void findData() {
    pCache = new PartnerCache(kupci);
    zbirMap = new HashMap();
    
    updateMap(Condition.none, true);

    SortedSet sortedZbir = new TreeSet(zbirMap.keySet());
    
    jp.enableEvents(false);
    zbir.empty();
    for (Iterator i = sortedZbir.iterator(); i.hasNext(); ) {
      Integer ip = (Integer) i.next();
      ZbirData zd = (ZbirData) zbirMap.get(ip);
      zbir.insertRow(false);
      zbir.setInt("CPAR", ip.intValue());
      zbir.setString("NAZPAR", pCache.getName(ip.intValue()));
      zbir.setString("OIB", (isMBinOIB()?pCache.getData(ip.intValue()).getMB():pCache.getData(ip.intValue()).getOIB()));
      zbir.setBigDecimal("RAC", zd.rac);
      zbir.setBigDecimal("DOSP", zd.dosp);
      zbir.setBigDecimal("NEDOSP", zd.nedosp);
      zbir.setBigDecimal("UPL", zd.upl);
      zbir.setBigDecimal("SALDO", zd.saldo);
      if (pres.getGrupa() != null) zbir.setString("CGRPAR", pres.gmsz.getString("CGRPAR"));
    }
    zbir.post();
    zbir.first();
    if (pres.getGrupa() != null) {
    	((raExtendedTable) jp.getMpTable()).addToGroup("CGRPAR", true, new String[] {"#", "NAZIV"}, dm.getGruppart(), true);
    }
    jp.enableEvents(true);
    pCache.dispose();
    pCache = null;
  }

  private boolean isMBinOIB() {
    return frmParam.getParam("sk", "MBinOIB", "N", "Da li se u OIB polje na pregledu otv. prometa ubacuje MB (D/N)").equalsIgnoreCase("D");
  }

  public void beforeShow() {
    setPreselectValues();
    val.reset(vl.getToday());
    oznval = null;
    findDataInProcess();
    setTitle();
    this.getRepRunner().setReportTitle("hr.restart.sk.repDugPot",
      kupci ? "Zbirni ispis potraživanja od kupaca" : "Zbirni ispis dugovanja dobavlja\u010Dima");
    
    jp.fireTableDataChanged();
    jp.getColumnsBean().eventInit();
    if (resize) {
      resize = false;
      pack();
    }
    
    if (kupci) {
      this.getRepRunner().enableReport("hr.restart.sk.repPnP");
      this.getRepRunner().enableReport("hr.restart.sk.repOpomena");
      this.getRepRunner().enableReport("hr.restart.sk.repOpomenaPT");
      this.getRepRunner().enableReport("hr.restart.sk.repTamara");
    } else {
      this.getRepRunner().disableReport("hr.restart.sk.repPnP");
      this.getRepRunner().disableReport("hr.restart.sk.repOpomena");
      this.getRepRunner().disableReport("hr.restart.sk.repOpomenaPT");
      this.getRepRunner().disableReport("hr.restart.sk.repTamara");
    }
  }

  public void show() {
    beforeShow();

    super.show();
  }
  
  static class ZbirData {
    BigDecimal rac, dosp, nedosp, upl, saldo;
    public ZbirData() {
      rac = dosp = nedosp = upl = saldo = raSaldaKonti.n0;
    }
  }

  private void setDataSet() {
    zbir.setColumns(new Column[] {
      (Column) dm.getPartneri().getColumn("CPAR").clone(),
      (Column) dm.getPartneri().getColumn("NAZPAR").clone(),
      (Column) dm.getPartneri().getColumn("OIB").clone(),
      (Column) dm.getAgenti().getColumn("NAZAGENT").clone(),
      dM.createBigDecimalColumn("RAC", "Raèuni", 2),
      dM.createBigDecimalColumn("DOSP", "Dospjeli", 2),
      dM.createBigDecimalColumn("NEDOSP", "Nedospjeli", 2),
      dM.createBigDecimalColumn("UPL", "Uplate", 2),
      dM.createBigDecimalColumn("SALDO", "Saldo", 2),
  	  dM.createStringColumn("CGRPAR", "Grupa", 20)
    });
    zbir.getColumn("CPAR").setWidth(7);
    zbir.getColumn("NAZAGENT").setCaption("Agent");
    zbir.getColumn("NAZAGENT").setWidth(20);
    zbir.getColumn("CGRPAR").setVisible(TriStateProperty.FALSE);
    zbir.setLocale(Aus.hr);
    zbir.open();
    jp.setDataSet(zbir);
    jp.setKumTak(true);
    jp.setStoZbrojiti(new String[] {"RAC", "DOSP", "NEDOSP", "UPL", "SALDO"});
    jp.setVisibleCols(new int[] {0,1,3,6,7});
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

  private void prepareIspis() {
//                   " AND cpar = " + zbir.getInt("CPAR");
//    String parq = " AND cpar = " + zbir.getInt("CPAR");
//    if (!sel.isEmpty()) {
//      StringBuffer listpar = new StringBuffer();
//      for (Iterator i = sel.iterator(); i.hasNext();)
//        listpar.append(i.next()).append(",");
//      parq = listpar.substring(0, listpar.length() - 1);
//      if (parq.indexOf(",") > 0) parq = " AND cpar in (" + parq + ")";
//      else parq = " AND cpar = " + parq;
//    }
    Condition sel = jp.getSelectCondition();
    if (sel == null) sel = Condition.equal("CPAR", zbir);
    String qstr = "SELECT cpar, corg, vrdok, cskl, stavka, brojdok, datdok, datdosp, "+
                  "id, ip, saldo, ziro, tecaj, oznval, cskstavke, EXTBRDOK, OPIS, BROJIZV FROM skstavke WHERE " + presq +
                  " AND pokriveno != 'X' AND "+Aus.getCurrGKDatumCond(datumCol, dto).and(sel)+ 
                  " AND vrdok in " + (kupci ? "('IRN','UPL','OKK')" : "('URN','IPL','OKD')") +
                  " ORDER BY cpar, datdok";
    System.out.println(qstr);
    raIspisKartica rik = raIspisKartica.getInstance(raIspisKartica.IOS);
    rik.setParams(0, 1, kupci, datumCol.equals("DATDOK"), dfrom, dto);
    rik.setSelection(jp.getSelectionTracker());
    if (jp.getSelectCount() > 1) prepareSortedTotals(rik);
    rik.setKonto(konto);
    rik.setQuery(qstr);
    
    /*qstr = "SELECT cpar, corg, vrdok, cskl, stavka, brojdok, datdok, datdosp, "+
    "id, ip, saldo, ziro, tecaj, oznval, cskstavke FROM skstavke WHERE " + presq +*/
    /*" AND pokriveno = 'N'*/  /*" AND "+Aus.getCurrGKDatumCond(dto)+ 
    " AND vrdok in " + (kupci ? "('IRN','UPL','OKK')" : "('URN','IPL','OKD')") +" AND " +
    Condition.till("DATDOSP", dto).orNull().and(sel) +
    " ORDER BY cpar, datdok";
    System.out.println(qstr);
    rik = raIspisKartica.getInstance(raIspisKartica.DOSP);
    rik.setParams(0, 1, kupci, false, dfrom, dto);
    rik.setSelection(jp.getSelectionTracker());
    if (jp.getSelectCount() > 1) prepareSortedTotals(rik);
    rik.setKonto(konto);
    rik.setQuery(qstr);*/
        
    int year = Aus.getNumber(Util.getUtil().getYear(dto)) - 1;
    Timestamp dto120 = ut.getLastSecondOfDay(Aus.createTimestamp(year, 11, 1));
    qstr = "SELECT cpar, corg, vrdok, cskl, stavka, brojdok, datdok, datdosp, "+
    "id, ip, saldo, ziro, tecaj, oznval, cskstavke, EXTBRDOK, OPIS, BROJIZV FROM skstavke WHERE " + presq +
    " AND pokriveno != 'X' AND "+Aus.getCurrGKDatumCond(datumCol, dto)+ 
    " AND vrdok in " + (kupci ? "('IRN','OKK')" : "('URN','OKD')") +" AND " +
    Condition.till("DATDOSP", dto120).andNotNull().and(sel) +
    " ORDER BY cpar, datdok";
    System.out.println(qstr);
    rik = raIspisKartica.getInstance(raIspisKartica.DOSP120);
    rik.setParams(0, 1, kupci, datumCol.equals("DATDOK"), dfrom, dto);
    rik.setSelection(jp.getSelectionTracker());
    if (jp.getSelectCount() > 1) prepareSortedTotals(rik);
    rik.setKonto(konto);
    rik.setQuery(qstr);
  }

  private void prepareReports() {
    raReportDescriptor ios = this.getRepRunner().getReport("hr.restart.sk.repIOS");
    if (jp.getSelectCount() == 0) ios.setTitle("Ispis otvorenih stavki ozna\u010Denog partnera");
    else if (jp.getSelectCount() == 1) ios.setTitle("Ispis otvorenih stavki odabranog partnera");
    else ios.setTitle("Grupni ispis otvorenih stavki odabranih partnera");
    raReportDescriptor opo = this.getRepRunner().getReport("hr.restart.sk.repOpomena");
    if (jp.getSelectCount() == 0) opo.setTitle("Ispis opomene ozna\u010Denog partnera");
    else if (jp.getSelectCount() == 1) opo.setTitle("Ispis opomene odabranog partnera");
    else opo.setTitle("Grupni ispis opomena odabranih partnera");
    raReportDescriptor opopt = this.getRepRunner().getReport("hr.restart.sk.repOpomenaPT");
    if (jp.getSelectCount() == 0) opopt.setTitle("Ispis opomene pred tužbu ozna\u010Denog partnera");
    else if (jp.getSelectCount() == 1) opopt.setTitle("Ispis opomene pred tužbu odabranog partnera");
    else opopt.setTitle("Grupni ispis opomena pred tužbu odabranih partnera");
    raReportDescriptor dosp = this.getRepRunner().getReport("hr.restart.sk.repDosp");
    if (jp.getSelectCount() == 0) dosp.setTitle("Ispis dospjelih raèuna ozna\u010Denog partnera");
    else if (jp.getSelectCount() == 1) dosp.setTitle("Ispis dospjelih raèuna odabranog partnera");
    else dosp.setTitle("Grupni ispis dospjelih raèuna odabranih partnera");
  }

  private void doubleClick(int showMode) {
    if (zbir.rowCount() > 0) {
      if (showMode == SHOW_PARAM) {
        String sm = frmParam.getParam("sk", "dugPot2click", "S", "Dvoklik na prikazu " +
           "dugovanja/potraživanja prikazuje otvorene raèune, uplate ili sve (R/U/S)?", true);
        if (sm.equalsIgnoreCase("R")) showMode = SHOW_RAC;
        else if (sm.equalsIgnoreCase("U")) showMode = SHOW_UPL; 
      }
      
      frmKartica fk = (frmKartica) startFrame.getStartFrame().
                      showFrame("hr.restart.sk.frmKartica", 15, "Kartica", false);

      fk.pres.setInsideCall();

      System.out.println("corg = "+corg);

      //String kgod = vl.getKnjigYear("gk");
      Timestamp tfrom = datumCol.equals("DATDOK") ?
        ut.getFirstDayOfYear(ut.addYears(vl.getToday(), -20)) :
          ut.getYearBegin(vl.getKnjigYear("gk"));
      fk.pres.setForceCpar(kupci, zbir.getInt("CPAR"), corg, konto, tfrom, dto);
      fk.pres.setForceDosp(showMode, datumCol.equals("DATDOK"));
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


  private void jbInit() throws Exception {
    pres = new presZbirnoDan(this);
    setDataSet();
    jp.getColumnsBean().setSaveSettings(true);
    jp.getColumnsBean().setSaveName(getClass().getName());
    this.getContentPane().add(jp);
    this.getRepRunner().addReport("hr.restart.sk.repDugPot", "hr.restart.sk.repDugPot", "DugPot", "Zbirni ispis potraživanja");
    this.getRepRunner().addReport("hr.restart.sk.repIOS","hr.restart.sk.repIOS","IOS", "Ispis otvorenih stavki oznaèenog partnera"/*, 42*/);
    this.getRepRunner().addReport("hr.restart.sk.repIOSint","hr.restart.sk.repIOS","IOSint", "Skraæeni ispis otvorenih stavki"/*, 42*/);
    this.getRepRunner().addReport("hr.restart.sk.repDosp","hr.restart.sk.repKarticaDosp","Dosp", "Ispis dospjelih raèuna partnera"/*, 42*/);
    this.getRepRunner().addReport("hr.restart.sk.repDospDan","hr.restart.sk.repKarticaDosp","Dosp", "Ispis dospjelih raèuna partnera otvorenih do danas"/*, 42*/);
    this.getRepRunner().addReport("hr.restart.sk.repDospDan7","hr.restart.sk.repKarticaDosp","Dosp", "Ispis dospjelih raèuna u sljedeæih 7 dana"/*, 42*/);
    this.getRepRunner().addReport("hr.restart.sk.repDospDan30","hr.restart.sk.repKarticaDosp","Dosp", "Ispis dospjelih raèuna u sljedeæih 30 dana"/*, 42*/);
    this.getRepRunner().addReport("hr.restart.sk.repPnP","hr.restart.sk.repOpomena","PodsjetnikNaPlacanje", "Ispis podsjetnika za plaæanje oznaèenog partnera"/*, 42*/);
    this.getRepRunner().addReport("hr.restart.sk.repOpomena","hr.restart.sk.repOpomena","Opomena", "Ispis opomene oznaèenog partnera"/*, 42*/);
    this.getRepRunner().addReport("hr.restart.sk.repOpomenaPT","hr.restart.sk.repOpomena","OpomenaPT", "Ispis opomene pred tužbu oznaèenog partnera"/*, 42*/);
    this.getRepRunner().addReport("hr.restart.sk.repTamara","hr.restart.sk.repKarticaTamara","Dosp", "Ispis neplaæenih raèuna dospjelih 60 dana prije nove godine"/*, 42*/);
//    this.getRepRunner().addReport("hr.restart.sk.repKartica", "Grupni ispis kartica ozna\u010Denih partnera", 42);

    jp.installSelectionTracker("CPAR");

    this.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_F10) {
          e.consume();
        }
      }
    });
    jp.getNavBar().addOption(new raNavAction("Otvoreni raèuni", raImages.IMGSTAV, KeyEvent.VK_F6) {
      public void actionPerformed(ActionEvent e) {
        if (zbir.rowCount() > 0)
          doubleClick(SHOW_RAC);
      }
    });
    jp.getNavBar().addOption(new raNavAction("Otvorene uplate", raImages.IMGHISTORY, KeyEvent.VK_F6, KeyEvent.SHIFT_MASK) {
      public void actionPerformed(ActionEvent e) {
        if (zbir.rowCount() > 0)
          doubleClick(SHOW_UPL);
      }
    });
    jp.getNavBar().addOption(new raNavAction("Obraèun kamata", raImages.IMGSENDMAIL, KeyEvent.VK_F8) {
      public void actionPerformed(ActionEvent e) {
        if (zbir.rowCount() > 0) {
          if (hr.restart.util.raLoader.isLoaderLoaded("hr.restart.sk.raObrKamata")) {
            if (raObrKamata.getInstance().isBusy()) return;
            if (raObrKamata.getInstance().isStandAlone()) {
              if (raObrKamata.getInstance().isShowing())
                raObrKamata.getInstance().hide();
              raObrKamata.getInstance().setStandAlone(false);
            }
          }
          startFrame.getStartFrame().showFrame("hr.restart.sk.raObrKamata", "Obraèun kamata");
          
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              raObrKamata rok = raObrKamata.getInstance();
              if (jp.getSelectCount() > 1) {
                rok.jpp.nazpar.setText(VarStr.join(jp.getSelection(), ',').toString());
                rok.jpp.cpar.setLastNavValues();
              } else {
                int cp = jp.getSelectCount() == 0 ? zbir.getInt("CPAR")
                    : ((Integer) jp.getSelection()[0]).intValue();
                rok.jpp.setCpar(cp);                  
              }
              rok.fset.setTimestamp("DATUMDO", dto);
              rok.fset.setTimestamp("DATUMOD", dfrom);
              if (raSaldaKonti.isDirect())                
                rok.jpk.setKonto(konto);
            }
          });
        }
      }
    });
/*    jp.getNavBar().addOption(new raNavAction("Ispis", raImages.IMGPRINT, KeyEvent.VK_F5) {
      public void actionPerformed(ActionEvent e) {
        prepareIspis();
        printer.runIt();
      }
    }); */
    jp.getNavBar().addOption(new raNavAction("Valuta", raImages.IMGMOVIE,KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
        val.show(frmDugPot.this.getWindow());
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
        pres.showPreselect(frmDugPot.this, "Zbirni pregled dugovanja / potraživanja");
        if (frmDugPot.this.isShowing()) beforeShow();
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
    jp.getNavBar().addOption(new raNavAction("Izlaz", raImages.IMGX, KeyEvent.VK_ESCAPE) {
      public void actionPerformed(ActionEvent e) {
        frmDugPot.this.hide();
      }
    });

    jp.initKeyListener(this);

     this.addKeyListener(new JraKeyListener());
//    this.setSize(640, 400);
//    jp.setSize(640, 400);
  }
}

