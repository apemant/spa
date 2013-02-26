/****license*****************************************************************
**   file: raIspisKartica.java
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
import hr.restart.swing.raSelectTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raProcess;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raIspisKartica {
  public static final String SINGLE = "Single";
  public static final String MULTI = "Multi";
  public static final String IOS = "IOS";
//  public static final String DOSP = "Dosp";
  public static final String DOSP120 = "Dosp120";

  private dM dm = dM.getDataModule();
  private lookupData ld = lookupData.getlookupData();
  private hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

  private static Map instances = new HashMap();
  private StorageDataSet totals = new StorageDataSet();
  private QueryDataSet repQDS = new QueryDataSet();
  private DataSet outData = null;

  private int vrsta, status, cparSingle;
  private boolean kupci, datdok, both, prepared, oldout;
  private Timestamp dfrom, dto;
  private String konto = null;
  public static String dokformat;
  public boolean outrange = true;
  public int plus = 0;
  
  public Timestamp poc;
  {
    Calendar c = Calendar.getInstance();
    c.set(c.YEAR, 2012);
    c.set(c.MONTH, c.OCTOBER);
    c.set(c.DATE, 1);
    c.set(c.HOUR_OF_DAY, 0);
    c.set(c.MINUTE, 0);
    c.set(c.SECOND, 0);
    c.set(c.MILLISECOND, 0);
    poc = new Timestamp(c.getTime().getTime());
  }
  
  raSelectTableModifier stm = null;
  
  private raIspisKartica() {
    setTotalSet();
  }
  
  public static raIspisKartica getInstance(String name) {
    raIspisKartica inst = (raIspisKartica) instances.get(name);
    if (inst == null) instances.put(name, inst = new raIspisKartica());
    return inst;
  }
  public static String createIOSDokText(ReadRow skstavke) {
    return createDokText(skstavke, "formatIOS");
  }
  public static String createKartDokText(ReadRow skstavke) {
    return createDokText(skstavke, "formatKart");
  }

  /**
   * Neee nije kopipejst iz frmKnjSKRac.knjiziUIStavku(...)
   * @param skstavke
   * @return
   */
  public static String createDokText(ReadRow skstavke, String param) {
//System.out.println("**********\n***********\n** "+skstavke);    
    VarStr opis = new VarStr();
    if (dokformat == null) findFormat(param);
    opis.append(dokformat);
    if (skstavke.hasColumn("OPIS")!=null) opis.replaceAll("$O", skstavke.getString("OPIS"));
    if (skstavke.hasColumn("BROJDOK")!=null) opis.replaceAll("$B", skstavke.getString("BROJDOK"));
    if (skstavke.hasColumn("EXTBRDOK")!=null) opis.replaceAll("$E", skstavke.getString("EXTBRDOK"));
    if (skstavke.hasColumn("BROJIZV")!=null) { 
      opis.replaceAll("$I", (skstavke.getInt("BROJIZV") == 0)?"":(""+skstavke.getInt("BROJIZV")));
    }
    if (skstavke.hasColumn("CPAR")!=null) { 
      opis.replaceAll("$C", Integer.toString(skstavke.getInt("CPAR")));
      if (opis.indexOf("$P") >= 0) {
        String nazpar = "";
        if (lookupData.getlookupData().raLocate(dM.getDataModule().getPartneri(), "CPAR", 
            Integer.toString(skstavke.getInt("CPAR"))))
              nazpar = dM.getDataModule().getPartneri().getString("NAZPAR");
        opis.replaceAll("$P", nazpar);
      }
    }
    return opis.toString();
  }
  public DataSet getDataSet() {
    if (!prepared || oldout != outrange) prepare();
    return outData == null ? repQDS : outData;
  }

  public void setQuery(String qstr, boolean toPrepare) {
    repQDS.close();
    repQDS.setQuery(new QueryDescriptor(dm.getDatabase1(),qstr));
    outData = null;
    if (toPrepare) prepareNow();
  }
  
  public void setQuery(String qstr) {
    setQuery(qstr, false);
  }
  
  void prepareNow() {
    if (prepared) {
      close();
      repQDS.setQuery(repQDS.getQuery());
      System.out.println("requery" + repQDS.getQuery().getQueryString());
    }
    prepared = true;
    oldout = outrange;
    repQDS.setMetaDataUpdate(repQDS.getMetaDataUpdate() & ~MetaDataUpdate.ROWID);
    repQDS.open();
    repQDS.getColumn("CSKSTAVKE").setRowId(true);
    checkOutOfBoundsDocs(repQDS);
    findCparTotals(repQDS);
  }
  
  public static void findFormat(String param) {
    dokformat = frmParam.getParam("sk", param, "$B", 
      "Format opisa Dokumenta kod ispisa IOSa/Kartice ($O-opis, $B-br.dok., $E-br.URA, $I-izvod)"); 
  }

  void prepare() {
    if (prepared && oldout == outrange) return;
    raProcess.runChild(new Runnable() {
      public void run() {
        prepareNow();
      }
    });
  }
  
  public void close() {
    totals.empty();
    repQDS.empty();
    repQDS.close();
  }
  
  void checkOutOfBoundsDocs(DataSet ds) {
    if (outrange) {
      Condition cparCond = null;
      if (cparSingle != -999) cparCond = Condition.equal("CPAR", cparSingle);
      else if (stm == null) cparCond = Condition.none;
      else if (stm.getSelectionCondition() == null)
        cparCond = Condition.equal("CPAR", repQDS);
      else cparCond = stm.getSelectionCondition();
    
      Condition remDoc = Aus.getKnjigCond().and(cparCond).and(Aus.getVrdokCond(kupci)).
          and(Condition.where(datdok ? "DATDOK" : "DATUMKNJ", Condition.AFTER, dto));
      
      raSaldaKonti.updateOutOfRangeSaldo(ds, remDoc);
    }
    
    ds.first();
    while (ds.inBounds())
      if ((ds.getBigDecimal("SALDO").signum() == 0 && status == 1) ||
          (ds.getBigDecimal("SALDO").signum() != 0 && status == 2))
        ds.emptyRow();
      else ds.next();

  }
  
  public void setDataSet(DataSet ds) {
    prepared = true;
    oldout = outrange;
    checkOutOfBoundsDocs(ds);
    findCparTotals(outData = ds);
  }
  
  public void setSelection(raSelectTableModifier rstm) {
    stm = rstm;
  }

  public void setParams(int _vrsta, int _status, boolean _kupci, boolean _datdok,
                        Timestamp _dfrom, Timestamp _dto) { 
    this.vrsta = _vrsta;
    this.status = _status;
    this.kupci = _kupci;
    this.datdok = _datdok;
    this.dfrom = _dfrom;
    this.dto = _dto;
    stm = null;
    konto = null;
    prepared = false;
    oldout = outrange;
    cparSingle = -999;
    totals.empty();
  }
  
  public void setBoth(boolean yes) {
    both = yes;
  }
  
  public void setKonto(String konto) {
    this.konto = konto;
  }
  
  public void setPartner(int cpar) {
    cparSingle = cpar;
  }
  
  public String getKonto() {
    return konto;
  }
  
  public DataSet getTotals(int cpar) {
    ld.raLocate(totals, "CPAR", String.valueOf(cpar));
    return totals;
  }

  public boolean isKupac() {
    return kupci;
  }
  
  public boolean isBoth() {
    return both;
  }

  public boolean needTot() {
    return (vrsta == 0 || vrsta == 4);
  }

  public boolean needVrdok() {
    return needTot();
  }

  public boolean needDosp() {
    return vrsta != 2;
  }

  public boolean needIzvod() {
    return (vrsta != 1 && vrsta != 3);
  }

  public String getNaslov() {
    String[][] what = new String[][] {
      {"SVI DOKUMENTI", "SVI OTVORENI DOKUMENTI", "SVI ZATVORENI DOKUMENTI"},
      {"RAÈUNI", "OTVORENI RAÈUNI", "ZATVORENI RAÈUNI"},
      {"UPLATE", "OTVORENE UPLATE", "ZATVORENE UPLATE"},
      {"OBAVIJESTI KNJIŽENJA", "OTVORENE OBAVIJESTI KNJIŽENJA", "ZATVORENE OBAVIJESTI KNJIŽENJA"},
      {"RAÈUNI I UPLATE", "OTVORENI RAÈUNI I UPLATE", "ZATVORENI RAÈUNI I UPLATE"}
    };
    if (isBoth()) return "\nKARTICA SALDA KONTI PARTNERA - " + what[vrsta][status];
    return "\nKARTICA SALDA KONTI " + (kupci ? "KUPACA - " : "DOBAVLJAÈA - ") + what[vrsta][status];
  }

  public String getPeriod() {
    hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
    return "\nZA PERIOD OD "+ rdu.dataFormatter(dfrom) + " DO " + rdu.dataFormatter(dto) +
           (datdok ? " PO DATUMU DOKUMENTA" : " PO DATUMU KNJIŽENJA");
  }

  public Timestamp getLastDay() {
    return dto;
  }

  public BigDecimal getSaldo(int cpar) {
    return getTotals(cpar).getBigDecimal("SALDO");
  }
  
  public BigDecimal getSaldoDosp(int cpar) {
    return getTotals(cpar).getBigDecimal("DOSPSALDO");
  }

  public String getNazivPartnera(int cpar) {
    ld.raLocate(dm.getPartneri(), "CPAR", String.valueOf(cpar));
    return dm.getPartneri().getString("NAZPAR");
  }

  public void insertEmptyTotal(int cpar) {
    BigDecimal n = raSaldaKonti.n0;
    totals.insertRow(false);
    totals.setInt("CPAR", cpar);
    totals.setBigDecimal("TOTALKRN", n);
    totals.setBigDecimal("SALDOKRN", n);
    totals.setBigDecimal("TOTALKUP", n);
    totals.setBigDecimal("SALDOKUP", n);
    totals.setBigDecimal("TOTALKOB", n);
    totals.setBigDecimal("SALDOKOB", n);
    totals.setBigDecimal("TOTALKOBU", n);
    totals.setBigDecimal("SALDOKOBU", n);
    totals.setBigDecimal("SALDO", n);
    totals.post();
  }
  
  private void findCparTotals(DataSet ds) {    
    int cpar, oldcpar = -1;
    String vrdok;
    BigDecimal id, ip, saldo, tsaldo;
    for (ds.first(); ds.inBounds(); ds.next()) {
      if (plus > 0 && !ds.getTimestamp("DATDOK").after(poc)) continue;
      cpar = ds.getInt("CPAR");
      vrdok = ds.getString("VRDOK");
      id = ds.getBigDecimal("ID");
      ip = ds.getBigDecimal("IP");
      saldo = ds.getBigDecimal("SALDO");
      tsaldo = id.signum() != 0 ? saldo : saldo.negate();
      if (cpar != oldcpar) {
        oldcpar = cpar;
        if (!ld.raLocate(totals, "CPAR", String.valueOf(cpar)))
          insertEmptyTotal(cpar);
      }
      if (vrdok.equals("IRN"))
        totals.setBigDecimal("TOTALKRN", totals.getBigDecimal("TOTALKRN").add(id));
      else if (vrdok.equals("UPL"))
        totals.setBigDecimal("TOTALKUP", totals.getBigDecimal("TOTALKUP").add(ip));
      else if (vrdok.equals("URN"))
        totals.setBigDecimal("TOTALKRN", totals.getBigDecimal("TOTALKRN").add(ip));
      else if (vrdok.equals("IPL"))
        totals.setBigDecimal("TOTALKUP", totals.getBigDecimal("TOTALKUP").add(id));
      else if (vrdok.equals("OKK")) {
        totals.setBigDecimal("TOTALKOB", totals.getBigDecimal("TOTALKOB").add(id));
        totals.setBigDecimal("TOTALKOBU", totals.getBigDecimal("TOTALKOBU").add(ip));
      } else if (vrdok.equals("OKD")) {
        totals.setBigDecimal("TOTALKOB", totals.getBigDecimal("TOTALKOB").add(ip));
        totals.setBigDecimal("TOTALKOBU", totals.getBigDecimal("TOTALKOBU").add(id));
      }

      totals.setBigDecimal("SALDO", totals.getBigDecimal("SALDO").add(tsaldo));
      
      if (plus == 0) {
        if (!ds.getTimestamp("DATDOSP").after(dto))
          totals.setBigDecimal("DOSPSALDO", totals.getBigDecimal("DOSPSALDO").add(tsaldo));
//      if (mode == IOS) {
//        if (vrdok.equals("IRN") || vrdok.equals("IPL") || vrdok.equals("OKK"))
//          ds.setBigDecimal("ID", saldo);
//        else ds.setBigDecimal("IP", saldo);
//      }
      } else {
         if (!Util.getUtil().addDays(ds.getTimestamp("DATDOK"), 30-plus).after(dto))
           totals.setBigDecimal("DOSPSALDO", totals.getBigDecimal("DOSPSALDO").add(tsaldo));
      }

      if (vrdok.equals("IRN") || vrdok.equals("URN"))
        totals.setBigDecimal("SALDOKRN", totals.getBigDecimal("SALDOKRN").add(saldo));
      else if (vrdok.equals("UPL") || vrdok.equals("IPL"))
        totals.setBigDecimal("SALDOKUP", totals.getBigDecimal("SALDOKUP").add(saldo));
      else if (vrdok.equals("OKK") && id.signum() != 0 || 
               vrdok.equals("OKD") && ip.signum() != 0)
        totals.setBigDecimal("SALDOKOB", totals.getBigDecimal("SALDOKOB").add(saldo));
      else totals.setBigDecimal("SALDOKOBU", totals.getBigDecimal("SALDOKOBU").add(saldo));
    }
    totals.post();
//    if (mode == IOS) ds.post();
  }
  
  private void setTotalSet() {
    totals.setColumns(new Column[] {
      (Column) dm.getSkstavke().getColumn("CPAR").clone(),
      dM.createBigDecimalColumn("TOTALKRN"),
      dM.createBigDecimalColumn("SALDOKRN"),
      dM.createBigDecimalColumn("TOTALKUP"),
      dM.createBigDecimalColumn("SALDOKUP"),
      dM.createBigDecimalColumn("TOTALKOB"),
      dM.createBigDecimalColumn("SALDOKOB"),
      dM.createBigDecimalColumn("TOTALKOBU"),
      dM.createBigDecimalColumn("SALDOKOBU"),
      dM.createBigDecimalColumn("SALDO"),
      dM.createBigDecimalColumn("DOSPSALDO")
    });
    totals.open();
  }
}