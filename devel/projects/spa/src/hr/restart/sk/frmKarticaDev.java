/****license*****************************************************************
**   file: frmKarticaDev.java
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
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraTable2;
import hr.restart.swing.dataSetTableModel;
import hr.restart.swing.raCurrencyTableModifier;
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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;


public class frmKarticaDev extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();

  raNavAction aPres;
  
  presKarticaDev pres;
  jpMatchDetail jpDetail;
  
  static frmKarticaDev frm;
  
  String nazpar, csk, oldcsk, corg, nazorg, valuta, konto;

  int cpar, vrsta, status;

  boolean kupci, datdok, datdosp, changed;

  boolean ignoreNav, dom;
  
  VarStr vals = new VarStr();
  VarStr valid = new VarStr();
  VarStr valip = new VarStr();
  VarStr valsal = new VarStr();

  Timestamp dfrom, dto;
  
  public frmKarticaDev() {
    super(2);
    try {
      frm = this;
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static frmKarticaDev getInstance() {
    return frm;
  }
  
  public void SetFokus(char mode) {
  }
  
  public boolean Validacija(char mode) {
    return false;
  }
  
  private void setPreselectValues() {
    kupci = pres.jpp.isKupci();
    datdok = pres.rcbDat.getSelectedIndex() == 0;

    vrsta = pres.rcbVrsta.getSelectedIndex();
    status = pres.rcbStatus.getSelectedIndex();
    //    System.out.println(presq);
    cpar = pres.jpp.getCpar();
    //    ld.raLocate(dm.getPartneri(), new String[] {"CPAR"}, new String[]
    // {String.valueOf(cpar)});
    //    nazpar = dm.getPartneri().getString("NAZPAR");
    nazpar = pres.jpp.getNazpar();
    konto = pres.getKonto();
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
    String[][] what = new String[][]{{"Devizna kartica", 
      "Devizna kartica otvorenih dokumenata", 
      "Devizna kartica zatvorenih dokumenata"}, 
      {"Ra\u010Duni u valuti", "Otvoreni ra\u010Duni u valuti", "Zatvoreni ra\u010Duni u valuti"}, 
      {"Uplate u valuti", "Otvorene uplate u valuti", "Zatvorene uplate u valuti"}, 
      {"Knjižne obavijesti u valuti", "Otvorene knjižne obavijesti u valuti", "Zatvorene knjižne obavijesti u valuti"}, 
      {"Ra\u010Duni i uplate u valuti", "Otvoreni ra\u010Duni i uplate u valuti", "Zatvoreni ra\u010Duni i uplate u valuti"}};
    VarStr title = new VarStr();
    title.append(what[vrsta][status]);
    title.append(kupci ? " kupca " : " dobavlja\u010Da ").append(cpar).append(" ").append(nazpar);
    title.append("  od ").append(rdu.dataFormatter(dfrom));
    title.append(" do ").append(rdu.dataFormatter(dto));
    this.setTitle(title.toString());
  }
  
  public String getNaslov() {
    String[][] what = new String[][] {
      {"SVI DOKUMENTI", "SVI OTVORENI DOKUMENTI", "SVI ZATVORENI DOKUMENTI"},
      {"RAÈUNI", "OTVORENI RAÈUNI", "ZATVORENI RAÈUNI"},
      {"UPLATE", "OTVORENE UPLATE", "ZATVORENE UPLATE"},
      {"OBAVIJESTI KNJIŽENJA", "OTVORENE OBAVIJESTI KNJIŽENJA", "ZATVORENE OBAVIJESTI KNJIŽENJA"},
      {"RAÈUNI I UPLATE", "OTVORENI RAÈUNI I UPLATE", "ZATVORENI RAÈUNI I UPLATE"}
    };
    return "\nDEVIZNA KARTICA SALDA KONTI " + (kupci ? "KUPACA - " : "DOBAVLJAÈA - ") + what[vrsta][status];
  }
  
  public String getPeriod() {
    hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
    return "\nZA PERIOD OD "+ rdu.dataFormatter(dfrom) + " DO " + rdu.dataFormatter(dto) +
           (datdok ? " PO DATUMU DOKUMENTA" : " PO DATUMU KNJIŽENJA");
  }
  
  public void beforeShow() {
    oldcsk = "";
    changed = false;
    setPreselectValues();
    //    prepareColumns();
    modifySaldoOutOfRange(false);
    setTitle();
    //this.getJpTableView().fireTableDataChanged();
  }
  
  public void navbar_afterRefresh() {
    modifySaldoOutOfRange(false);
  }
  
  void modifySaldoOutOfRange(boolean refresh) {
    Condition c = Aus.getKnjigCond().and(Condition.equal("CPAR", cpar)).
            and(Aus.getVrdokCond(kupci)).
            and(Condition.where(datdok ? "DATDOK" : "DATUMKNJ", Condition.AFTER, dto));

    // pamti trenutni polozaj u datasetu.
    QueryDataSet ds = getRaQueryDataSet();
    String memCsk = ds.getString("CSKSTAVKE");
    getJpTableView().enableEvents(false);
    setRaQueryDataSet(null);
    jpDetail.BindComponents(null);
    
    if (refresh) ds.refresh();

    raSaldaKonti.updateOutOfRangeSaldo(ds, c);
    
    // prodji ponovo kroz cijeli dataset i makni sve redove koji ne odgovaraju
    // uvjetima u predselekciji.
    ds.first();
    while (ds.inBounds()) {
      boolean zat = status == 0 || ds.getBigDecimal("SALDO").signum() == 0 &&
                                   ds.getBigDecimal("PVSALDO").signum() == 0;
      if (status != 0 && (status == 1 && zat || status == 2 && !zat))
        ds.emptyRow();
      else ds.next();
    }
    setRaQueryDataSet(ds);
    jpDetail.BindComponents(ds);
    getJpTableView().setKumTak(getJpTableView().getStoZbrojiti() != null);
    getJpTableView().init_kum();

    if (!ld.raLocate(ds, "CSKSTAVKE", memCsk)) ds.first();
    getJpTableView().enableEvents(true);
    getJpTableView().fireTableDataChanged();
  }
  
  class ValData {
    BigDecimal id, ip;
    public ValData(DataSet row) {
      id = row.getBigDecimal("PVID");
      ip = row.getBigDecimal("PVIP");
      if (status == 1 && id.signum() != 0) id = row.getBigDecimal("PVSALDO");
      if (status == 1 && ip.signum() != 0) ip = row.getBigDecimal("PVSALDO");
    }
    
    public void add(DataSet row) {
      ValData v = new ValData(row);
      id = id.add(v.id);
      ip = ip.add(v.ip);
    }
  }
  
  public void findTotals(DataSet ds) {
    vals.clear();
    valid.clear();
    valip.clear();
    valsal.clear();
    Map valmap = new HashMap();
    String domval = hr.restart.zapod.Tecajevi.getDomOZNVAL();
    for (ds.first(); ds.inBounds(); ds.next()) {
      String oznval = ds.getString("OZNVAL");
      if (oznval == null || oznval.length() == 0) oznval = domval;
      if (!valmap.containsKey(oznval)) valmap.put(oznval, new ValData(ds));
      else ((ValData) valmap.get(oznval)).add(ds);
    }
    for (Iterator i = valmap.keySet().iterator(); i.hasNext(); ) {
      String iv = (String) i.next();
      vals.append(iv).append('\n');
      ValData vd = (ValData) valmap.get(iv);
      valid.append(Aus.formatBigDecimal(vd.id)).append('\n');
      valip.append(Aus.formatBigDecimal(vd.ip)).append('\n');
      valsal.append(Aus.formatBigDecimal(vd.id.subtract(vd.ip))).append('\n');
    }
    vals.chop();
    valid.chop();
    valip.chop();
    valsal.chop();
  }
  
  public String getSUMVAL()
  {
    return vals.toString();
  }
  
  public String getDEVIDSUM()
  {
    return valid.toString();
  }

  public String getDEVIPSUM()
  {
    return valip.toString();
  }

  public String getDEVSALDOSUM()
  {
    return valsal.toString();
  }

  
  private void jbInit() throws Exception {
    boolean dispExt = frmParam.getParam("sk", "displayExt", "N", "Prikaži kolonu " +
        "dodatnog broja dokumenta na kartici SK (D/N)", true).equalsIgnoreCase("D");
    
    this.setRaQueryDataSet(Skstavke.getDataModule().getFilteredDataSet("1=0"));
    this.setVisibleCols(dispExt ? new int[]{2, 4, 9, 11, 26, 27, 29} : new int[]{2, 4, 9, 26, 27, 29});
    this.setTitle("Kartica");
    this.setkum_tak(true);
    this.setstozbrojiti(new String[]{"PVID", "PVIP", "PVSALDO"});

    jpDetail = new jpMatchDetail();
    jpDetail.jlNacpl.setText("Iznos u kunama");
    jpDetail.jraNacpl.setColumnName("SSALDO");
    this.setRaDetailPanel(jpDetail);
    jpDetail.BindComponents(this.getRaQueryDataSet());
    jpDetail.setBorder(null);
    
    //    this.getJpTableView().addTableModifier(new skCoverTableModifier());
    getJpTableView().getColumnsBean().setSaveSettings(false);
    this.setSort(new String[]{"DATDOK"});
    
    this.getRepRunner().addReport("hr.restart.sk.repKarticaDev", "hr.restart.sk.repKarticaDev", "DevKartica", "Ispis devizne kartice");

    this.getJpTableView().addTableModifier(new raCurrencyTableModifier("PVSALDO"));
    removeRnvCopyCurr();
    this.getNavBar().removeStandardOptions(new int[]{raNavBar.ACTION_ADD, raNavBar.ACTION_DELETE, raNavBar.ACTION_TOGGLE_TABLE, raNavBar.ACTION_UPDATE});
    pres = new presKarticaDev(this);
    
    final JraTable2 tab = (JraTable2) getJpTableView().getMpTable();
    final raTableSumRow summer = ((dataSetTableModel) tab.getModel()).getTableSumRow();
    summer.setCustomSummer(new raTableSumRow.CustomSummer() {
      public BigDecimal getSum(String col) {
        if (!col.equalsIgnoreCase("PVSALDO") || status == 2) return null;
        //if (status == 1) {
          BigDecimal sal = raSaldaKonti.n0;          
          Variant v = new Variant();
          for (int i = 0; i < getRaQueryDataSet().getRowCount(); i++) {
            getRaQueryDataSet().getVariant("PVID", i, v);
            boolean id = v.getBigDecimal().signum() != 0;
            getRaQueryDataSet().getVariant("PVSALDO", i, v);
            if (id) sal = sal.add(v.getBigDecimal());
            else sal = sal.subtract(v.getBigDecimal());
          }
          return kupci ? sal : sal.negate();
      }
    });
    
    this.addOption(aPres = new raNavAction("Predselekcija", raImages.IMGZOOM, KeyEvent.VK_F12) {
      public void actionPerformed(ActionEvent e) {
        pres.showPreselect(frmKarticaDev.this, "Devizna kartica");
      }
    }, 2);
  }
}
