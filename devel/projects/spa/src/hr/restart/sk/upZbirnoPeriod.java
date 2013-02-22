/****license*****************************************************************
**   file: upZbirnoPeriod.java
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
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCorg;
import hr.restart.swing.jpCpar;
import hr.restart.swing.jpZupGrad;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raProcess;
import hr.restart.util.raUpitLite;

import java.awt.Graphics;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class upZbirnoPeriod extends raUpitLite {
  private dM dm = dM.getDataModule();
  private lookupData ld = lookupData.getlookupData();
  private raCommonClass rcc = raCommonClass.getraCommonClass();
  private hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  private hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();

  private JPanel pan = new JPanel() {
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
//      raMatPodaci.drawEtchedLine(pan, g, 15, 130, 545, 130);
    }
  };
  XYLayout lay = new XYLayout();

  public jpCorg jpc = new jpCorg(100, 290, true) {
    public void afterLookUp(boolean succ) {
      if (succ) jpp.focusCpar();
    }
  };
  public jpCpar jpp = new jpCpar(100, 290, true) {
    public void afterLookUp(boolean succ) {
      if (succ) jpzg.init();
    }
    protected void kupSelected() {
      super.kupSelected();
      if (raSaldaKonti.isDirect()) checkKupDob(true);
    }
    protected void dobSelected() {
      super.dobSelected();
      if (raSaldaKonti.isDirect()) checkKupDob(false);
    }
  };
  public jpSelKonto jpk = new jpSelKonto(100, 290, true) {
    public void afterLookUp(boolean succ) {
      afterKonto(succ);
    }
  };
  public jpZupGrad jpzg = new jpZupGrad(100, 290) {
    public void afterLookUp(boolean succ) {
      if (succ) jpp.init();
    }
  };  

  JraTextField jraDatumfrom = new JraTextField();
  JraTextField jraDatumto = new JraTextField();

  raComboBox rcbDat = new raComboBox();

  JLabel jlGroups = new JLabel();
  raComboBox rcbGroups = new raComboBox() {
    public void this_itemStateChanged() {
      groupChanged();
    }
  };
  
  raComboBox rcbDomIno = new raComboBox() {
    public void this_itemStateChanged() {
      setDomIno();
    }
  };
  String domIno = "";

  int gr = -1, dkAdd = 0;
  boolean firstReset = false;

  StorageDataSet fields = new StorageDataSet();
  StorageDataSet repQDS;

  static upZbirnoPeriod upThis;

  public upZbirnoPeriod() {
    try {
      upThis = this;
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static upZbirnoPeriod getInstance() {
    return upThis;
  }

  public StorageDataSet getRepQDS() {
    return repQDS;
  }

  public void resetDefaults() {
    fields.setString("CORG", hr.restart.zapod.OrgStr.getKNJCORG(false));
    jpp.init();
    jpc.init();
    jpzg.init();
    jpk.setKontaAllow(false);
    fields.setTimestamp("DATUMOD", ut.getYearBegin(
        Valid.getValid().getKnjigYear("gk")));
    fields.setTimestamp("DATUMDO", Valid.getValid().getToday());
  }
  
  public void componentShow() {
    changeIcon(1);
    jpp.setKupci(jpp.isKupci());
    jpk.setKontaAllow(jpk.isKontaAllow());
    if (!firstReset) {
      firstReset = true;
      resetDefaults();
    }
    if (raSaldaKonti.isDirect() && !jpk.isKontaAllow()) jpk.enabDohvat(false);
    jpp.focusCombo();
  }

  public boolean Validacija() {
    if (!Aus.checkGKDateRange(jraDatumfrom, jraDatumto)) return false;
    return true;
  }
  
  public boolean isIspis() {
    return false;
  }

  public void okPress() {    
    if (gr == 0) prepareGroup();
    else prepareSingle();
  }
  
  private void setDomIno(){
    if (rcbDomIno.getSelectedIndex() == 0) domIno = " ";
    else if (rcbDomIno.getSelectedIndex() == 1) domIno = " TUZEMNIH ";
    else if (rcbDomIno.getSelectedIndex() == 2) domIno = " INOZEMNIH ";
  }

  private void prepareSingle() {

    String datcol = rcbDat.getSelectedIndex() == 0 ? "DATDOK" : "DATUMKNJ";    
    Condition dat = Condition.between(datcol,
        fields.getTimestamp("DATUMOD"), fields.getTimestamp("DATUMDO"));
    Condition kon = raSaldaKonti.isDirect() ? jpk.getCondition() : Condition.none;
    String sort, select;
    if ("$NAZPAR".equals(getSortColumn())) {
      sort = getOrderBy()+", "+datcol;
      select = "cpar,partneri.nazpar,corg,cgkstavke as cnaloga,datumknj,datdok,brojdok," +
                "saldo,datdosp,vrdok,id,ip,tecaj,oznval,cskstavke FROM skstavke,partneri "+
                "WHERE partneri.cpar=skstavke.cpar AND ";
    } else {
      sort = " ORDER BY cpar, "+datcol;
      select = "cpar,corg,cgkstavke as cnaloga,datumknj,datdok,brojdok,saldo,datdosp," +
                "vrdok,id,ip,tecaj,oznval,cskstavke FROM skstavke WHERE ";    
    }
    String vrd = jpp.isKupci() ? "('IRN','UPL','OKK')" : "('URN','IPL','OKD')";
    String que = "SELECT "+select+Aus.getKnjigCond().and(jpc.getCondition()).and(dat).and(kon) 
                 + " AND pvpok!='X' AND vrdok IN "+vrd+sort;    
    QueryDataSet ds = Util.getNewQueryDataSet(que);
    raProcess.setMessage("Dohvat podataka...", false);
    openScratchDataSet(ds);
    StorageDataSet filt = new StorageDataSet();
    filt.setColumns(ds.cloneColumns());
    filt.open();

    boolean porjeklo = true;
    int cpar = 0;
    boolean first = true, okPartner = true;
    for (ds.first(); ds.inBounds(); ds.next()) {
      checkClosing();
      if (first || cpar != ds.getInt("CPAR")) {
        first = false;
        cpar = ds.getInt("CPAR");
        boolean f = ld.raLocate(dm.getPartneri(), "CPAR", String.valueOf(cpar));
        
        if (rcbDomIno.getSelectedIndex() != 0){
          porjeklo = dm.getPartneri().getString("DI").equalsIgnoreCase(rcbDomIno.getDataValue());
        }
        
        okPartner = f && porjeklo && jpzg.checkPartner(dm.getPartneri()) &&
                     (jpp.getCpar() == 0 || jpp.getCpar() == cpar);
      }
      if (okPartner) {
        filt.insertRow(false);
        ds.copyTo(filt);
      }
    }
    if (filt.rowCount() == 0) setNoDataAndReturnImmediately();
    raIspisKartica rik = raIspisKartica.getInstance(raIspisKartica.SINGLE);
    rik.setParams(0, 0, jpp.isKupci(), rcbDat.getSelectedIndex() == 0, 
        fields.getTimestamp("DATUMOD"), fields.getTimestamp("DATUMDO"));
    rik.setKonto(jpk.getKonto());
    rik.setDataSet(filt);
    this.killAllReports();
    this.addReport("hr.restart.sk.repKartica", "hr.restart.sk.repKartica", "Kartica", "Grupni ispis kartica ozna\u010Denih partnera");
  }

  private void prepareGroup() {
    String datcol = rcbDat.getSelectedIndex() == 0 ? "DATDOK" : "DATUMKNJ";    
    Condition dat = Condition.between(datcol,
        fields.getTimestamp("DATUMOD"), fields.getTimestamp("DATUMDO"));
    Condition kon = raSaldaKonti.isDirect() ? jpk.getCondition() : Condition.none;
    String sort, select;
    /*if ("$NAZPAR".equals(getSortColumn())) {
      sort = getOrderBy();
      select = "cpar,partneri.nazpar,ssaldo,datdosp,vrdok,id,ip FROM skstavke,partneri "+
               "WHERE partneri.cpar=skstavke.cpar AND ";
    } else {*/
      sort = " ORDER BY cpar";
      select = "cpar,ssaldo,datdosp,vrdok,id,ip FROM skstavke WHERE ";    
    //}
    String vrd = jpp.isKupci() ? "('IRN','UPL','OKK')" : "('URN','IPL','OKD')";
    String que = "SELECT "+select+Aus.getKnjigCond().and(jpc.getOptCondition()).and(dat).and(kon) +
                 " AND pvpok!='X' AND vrdok IN "+vrd+sort;
    System.out.println(que);
    QueryDataSet ds = Util.getNewQueryDataSet(que);
    raProcess.setMessage("Dohvat podataka...", false);
    openScratchDataSet(ds);
    if (ds.rowCount() == 0) setNoDataAndReturnImmediately();
    createGroupDataSet();    
    raProcess.setMessage("Obrada i zbrajanje raèuna...", false);
    
    boolean porjeklo = true;
    int cpar = 0;
    boolean first = true, okPartner = true;
    for (ds.first(); ds.inBounds(); ds.next()) {
      checkClosing();
      if (first || cpar != ds.getInt("CPAR")) {
        first = false;
        cpar = ds.getInt("CPAR");
        boolean f = ld.raLocate(dm.getPartneri(), "CPAR", String.valueOf(cpar));
        
        if (rcbDomIno.getSelectedIndex() != 0){
          porjeklo = dm.getPartneri().getString("DI").equalsIgnoreCase(rcbDomIno.getDataValue());
        }
        
        okPartner = f && porjeklo && jpzg.checkPartner(dm.getPartneri()) &&
                     (jpp.getCpar() == 0 || jpp.getCpar() == cpar);
        if (okPartner) {
          repQDS.insertRow(false);
          repQDS.setInt("CPAR", cpar);
          repQDS.setString("NAZPAR", dm.getPartneri().getString("NAZPAR"));
        }
      }
      if (!okPartner) continue;
      BigDecimal sal = ds.getBigDecimal("SSALDO");
      if (raVrdokMatcher.isUplataTip(ds))
        repQDS.setBigDecimal("PROMETU", repQDS.getBigDecimal("PROMETU").add(sal));
      else repQDS.setBigDecimal("PROMETR", repQDS.getBigDecimal("PROMETR").add(sal));
      repQDS.setBigDecimal("SALDO", repQDS.getBigDecimal("PROMETR").
                                        subtract(repQDS.getBigDecimal("PROMETU")));      
    }
    if (repQDS.rowCount() == 0) setNoDataAndReturnImmediately();
    this.killAllReports();
    this.addReport("hr.restart.sk.repZbirnoPeriod", "hr.restart.sk.repZbirnoPeriod", "ZbirnoPeriod", "Ispis");
    //if (!"$NAZPAR".equals(getSortColumn()))
      repQDS.setSort(getSelectedSort());
  }
  
  private void createGroupDataSet() {
    repQDS = new StorageDataSet();
    repQDS.setColumns(new Column[] {
        (Column) dm.getPartneri().getColumn("CPAR").clone(),
        (Column) dm.getPartneri().getColumn("NAZPAR").clone(),
        (Column) dm.getSkkumulativi().getColumn("PROMETR").clone(),
        (Column) dm.getSkkumulativi().getColumn("PROMETU").clone(),
        (Column) dm.getSkstavke().getColumn("SALDO").clone()
    });
    repQDS.getColumn("CPAR").setWidth(7);
    repQDS.open();
  }

  public void firstESC() {
    rcc.EnabDisabAll(pan, true);
    if (raSaldaKonti.isDirect() && !jpk.isKontaAllow()) jpk.enabDohvat(false);
    jpp.focusCombo();
  }

  public boolean runFirstESC() {
    return false;
  }

  public boolean ispisNow() {
    return true;
  }

  private void groupChanged() {
    if (rcbGroups.getSelectedIndex() != gr) {
      gr = rcbGroups.getSelectedIndex();
      //setSortVisible(gr == 0);
      if (gr != 0) {
        clearAllSorts();
        addSortOption("CPAR", "Šifra partnera");
        addSortOption("$NAZPAR", "Naziv partnera");
      } else {
        clearAllSorts();
        addSortOption("CPAR", "Šifra partnera");
        addSortOption("$NAZPAR", "Naziv partnera");
        addSortOption("-PROMETR", "Promet raèuna");
        addSortOption("-PROMETU", "Promet uplata");
        addSortOption("-SALDO", "Saldo");
      }
    }
  }
  
  public String getNaslov() {
    if (gr == 0) return "\nZBIRNI PREGLED PROMETA "+ domIno +(jpp.isKupci() ? "KUPACA" : "DOBAVLJAÈA");
    return "";
  }
  
  public String getPodnaslov() {
    return "\nu periodu od " + Aus.formatTimestamp(fields.getTimestamp("DATUMOD")) +
            " do " + Aus.formatTimestamp(fields.getTimestamp("DATUMDO")) +
            (rcbDat.getSelectedIndex() == 0 ? " po datumu dokumenta " : " po datumu knjiženja");
  }
  
  public String getMjesto() {
    if (jpzg.getAgent() != null) return jpzg.getAgent();
    else if (jpzg.getGrad() != null) return jpzg.getGrad();
    else if (jpzg.getZupanija() != null) return jpzg.getZupanija();
    else return "";
  }

  public String getLabelMjesto() {
    if (jpzg.getAgent() != null) return "Agent";
    else if (jpzg.getGrad() != null) return "Grad";
    else if (jpzg.getZupanija() != null) return "Županija";
    else return "";
  }
  
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

  private void jbInit() throws Exception {
    fields.setColumns(new Column[] {
      dM.createStringColumn("CORG", "Org. jedinica", 12),
      dM.createIntColumn("CPAR", "Partner"),
      dM.createIntColumn("NAZPAR", "Partner"),
      dM.createShortColumn("CZUP", "Županija"),
      dM.createStringColumn("BROJKONTA", "Konto", 8),
      dM.createStringColumn("CGRPAR", "Grupa", 10),
      dM.createIntColumn("PBR", "Poštanski broj"),
      dM.createStringColumn("VRDAT", "Vrsta datuma", 8),
      dM.createTimestampColumn("DATUMOD", "Poèetni datum"),
      dM.createTimestampColumn("DATUMDO", "Krajnji datum"),
      dM.createIntColumn("CAGENT", "Agent")
    });
    fields.open();
    
    dkAdd = raSaldaKonti.isDirect() ? 25 : 0;

    pan.setLayout(lay);
    lay.setWidth(585);
    lay.setHeight(160 + dkAdd);    

    jpc.bind(fields);
    jpp.bind(fields);
    jpzg.bind(fields);
    jpk.bind(fields);
    jraDatumfrom.setDataSet(fields);
    jraDatumfrom.setColumnName("DATUMOD");
    jraDatumto.setDataSet(fields);
    jraDatumto.setColumnName("DATUMDO");

    rcbDat.setRaItems(new String[][] {
      {"Datum dokumenta", "DATDOK"},
      {"Datum knjiženja", "DATUMKNJ"}
    });
    rcbDat.setColumnName("VRDAT");
    rcbDat.setDataSet(fields);
    rcbDat.setSelectedIndex(1);

    addSortOption("CPAR", "Šifra partnera");
    addSortOption("$NAZPAR", "Naziv partnera");
    addSortOption("-PROMETR", "Promet raèuna");
    addSortOption("-PROMETU", "Promet uplata");
    addSortOption("-SALDO", "Saldo");

    jlGroups.setText("Grupiranje / porijeklo");
    rcbGroups.setRaItems(new String[][] {
      {"Zbirno za sve partnere", "Z"},
      {"Pojedinaèno po partnerima", "P"}
    });
    rcbGroups.setSelectedIndex(gr = 0);

    rcbDomIno.setRaItems(new String[][] {
        {"Tuzemni i inozemni"," "},
        {"Tuzemni","D"},
        {"Inozemni","I"}
    });
    rcbDomIno.setSelectedIndex(0);

    pan.add(jpc, new XYConstraints(0, 20, -1, -1));
    if (raSaldaKonti.isDirect())
      pan.add(jpk, new XYConstraints(0, 45, -1, -1));
    pan.add(jpp, new XYConstraints(0, 45 + dkAdd, -1, -1));
    pan.add(jpzg, new XYConstraints(0, 70 + dkAdd, -1, -1));
//    pan.add(jlMjesto, new XYConstraints(385, 72, -1, -1));
//    pan.add(jraMjesto, new XYConstraints(445, 70, 100, -1));
    pan.add(rcbDat, new XYConstraints(15, 95 + dkAdd, 130, -1));
    pan.add(jraDatumfrom, new XYConstraints(150, 95 + dkAdd, 100, -1));
    pan.add(jraDatumto, new XYConstraints(255, 95 + dkAdd, 100, -1));
    pan.add(jlGroups, new XYConstraints(15, 120 + dkAdd, -1, -1));
    pan.add(rcbGroups, new XYConstraints(150, 120 + dkAdd, 205, -1));
    pan.add(rcbDomIno, new XYConstraints(360, 120 + dkAdd, 185, -1));

    this.setJPan(pan);
  }
}

