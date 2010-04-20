/****license*****************************************************************
**   file: frmCover.java
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
import hr.restart.baza.Pokriveni;
import hr.restart.baza.Skstavke;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraKeyListener;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raFrame;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.HashSet;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
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

public class frmCover extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  lookupData ld = lookupData.getlookupData();

  frmKartica base;

  raNavAction aSve, aIzn;

//  dlgCover pd;
  jpMatchDetail jpDetail;

  public boolean toMatch;
  public boolean toMatchAll;

//  StorageDataSet temp = new StorageDataSet();
  raTableModifier mod;

  jpMatchTotals tot;

  String csk, oldcsk;
  
  static String salcol = raSaldaKonti.colSaldo();

  QueryDataSet pok = Pokriveni.getDataModule().getFilteredDataSet("1=0");
//  QueryDataSet add = dm.getSkstavkeCover();
  QueryDataSet bset = null;// dm.getSkstavkeBase();
  
  QueryDataSet view = new QueryDataSet() {
    public void refresh() {
//      reverseChanges();
      pok.refresh();
      super.refresh();
      updateRdonlyView();
      jeprazno();
    }
//    public boolean open() {
//      return super.open();
//    }
//    public boolean saveChangesSupported() {
//      return false;
//    }
  };
  
  StorageDataSet viewro = new StorageDataSet();

  public frmCover() {
    super(2, raFrame.DIALOG, frmKartica.getInstance().getWindow());
    try {
      base = frmKartica.getInstance();
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  void updateRdonlyView() {
    if (viewro != null) viewro.close();
    viewro = new StorageDataSet();
    viewro.setColumns(view.cloneColumns());
    viewro.open();
    DataRow row = new DataRow(view);
    for (int i = 0; i < view.rowCount(); i++) {
      view.getDataRow(i, row);
      viewro.insertRow(false);
      row.copyTo(viewro);
    }
    viewro.post();
  }

  public void refreshBase() {

    base.getJpTableView().repaint();
    base.changed = true;
    
  }

  private void setTitle(boolean pokr) {
    String dod = "";
    if (!raSaldaKonti.isDomVal(base.valuta))
      dod = " (" + base.valuta + ")";
    if (base.vmat.isRacun()) {
      this.setTitle("Uplate (i OK) "+(pokr ? "kojima je pokriven ra\u010Dun " : "za pokrivanje ra\u010Duna ")+
                    base.getRaQueryDataSet().getString("BROJDOK")+" od "+
                    rdu.dataFormatter(base.getRaQueryDataSet().getTimestamp("DATDOK"))+dod);
      tot.setBaseText("Raèun");
    }
    if (base.vmat.isUplata()) {
      this.setTitle("Ra\u010Duni (i OK) "+(pokr ? "kojima je pokrivena uplata " : "za pokrivanje uplate ")+
                    base.getRaQueryDataSet().getString("BROJDOK")+" od "+
                    rdu.dataFormatter(base.getRaQueryDataSet().getTimestamp("DATDOK"))+dod);
      tot.setBaseText("Uplata");
    }
    if (base.vmat.isKob()) {
      String what = cbase.equals("cracuna") ? "Uplate" : "Raèuni";
      this.setTitle(what + (pokr ? " kojima je pokrivena knjižna obavijest " : " za pokrivanje knjižne obavijesti ")+
                    base.getRaQueryDataSet().getString("BROJDOK")+" od "+
                    rdu.dataFormatter(base.getRaQueryDataSet().getTimestamp("DATDOK"))+dod);
      tot.setBaseText("Knjižna obavijest");
    }
    tot.setValuta(base.valuta, false);
  }

  public String getMinimumMatch() {
    Condition oznCond = raSaldaKonti.isDomVal(base.valuta) ? 
        raSaldaKonti.getValCond() : Condition.equal("OZNVAL", base.valuta);
    return "SELECT * FROM skstavke WHERE knjig='" + OrgStr.getKNJCORG(false) +
            "' AND cpar="+base.cpar+" AND brojkonta='"+bset.getString("BROJKONTA")+
            (raSaldaKonti.isSimple() ? "' AND pokriveno='N' AND " : 
              "' AND (pokriveno='N' or pvpok='N') AND (" + oznCond + ") AND ") + 
            Condition.where("DATUMKNJ", Condition.FROM, 
                Util.getUtil().getYearBegin(base.beginGod)) +
            " AND " + base.vmat.getMatchString();
  }
  
  public String getAllMatch() {
    String oznCond = raSaldaKonti.isDomVal(base.valuta) ? "" :
      "(oznval='"+base.valuta+"' OR " + raSaldaKonti.getValCond() + ") AND ";
    return "SELECT * FROM skstavke WHERE knjig='" + OrgStr.getKNJCORG(false) +
            "' AND cpar="+base.cpar+" AND brojkonta='"+bset.getString("BROJKONTA")+
            (raSaldaKonti.isSimple() ? "' AND pokriveno='N' AND " : 
              "' AND (pokriveno='N' or pvpok='N') AND " + oznCond) + 
            Condition.where("DATUMKNJ", Condition.FROM, 
                Util.getUtil().getYearBegin(base.beginGod)) +
            " AND " + Aus.getVrdokCond(base.kupci) + " AND cskstavke!='" + base.csk + "'";
  }

  public void addMatch() {
    String match = toMatchAll ? getAllMatch() : getMinimumMatch();
    setFilter(" OR (" + match.substring(match.indexOf("WHERE") + 6) + ")");
  }

  public void setFilter(String toAdd) {
    long btime = System.currentTimeMillis();
    String pokList = "cskstavke IN (SELECT cuplate "+
        " FROM pokriveni WHERE pokriveni.cracuna='"+base.csk + 
        "') OR cskstavke IN (SELECT cracuna FROM pokriveni WHERE " +
         "pokriveni.cuplate='"+base.csk + "')";
    
    DataSet poks = Pokriveni.getDataModule().getTempSet(
        Condition.equal("CRACUNA", base.csk).or(
            Condition.equal("CUPLATE", base.csk)));
    poks.open();
    
    if (poks.rowCount() == 0) pokList = "1=0";
    else if (poks.rowCount() <= 200) {
      HashSet csks = new HashSet(poks.rowCount() * 2);
      for (poks.first(); poks.inBounds(); poks.next()) {
        csks.add(poks.getString("CUPLATE"));
        csks.add(poks.getString("CRACUNA"));
      }
      csks.remove(base.csk);
      pokList = Condition.in("CSKSTAVKE", 
          csks.toArray(new String[csks.size()])).toString();
    }
    System.out.println("List created in " + 
        (System.currentTimeMillis() - btime) + " ms");
    
    String filter = "SELECT * FROM skstavke WHERE " + pokList + toAdd;
    System.out.println(filter);
    this.getRaQueryDataSet().close();
    this.getRaQueryDataSet().setQuery(new QueryDescriptor(dm.getDatabase1(), filter));
    this.getRaQueryDataSet().open();
    updateRdonlyView();
    this.getJpTableView().fireTableDataChanged();
    setTitle(toAdd.length() == 0);
    jeprazno();
  }

  String cbase, cthis;
  private void findMatchSides() {
    cbase = base.vmat.getMatchSide();
    cthis = base.vmat.getOtherSide();
  }


  public void findPokriveni() {
    Pokriveni.getDataModule().setFilter(pok, 
        "cracuna='"+base.csk+"' or cuplate='"+base.csk+"'");
//    pok.close();
//    pok.setQuery(new QueryDescriptor(dm.getDatabase1(), "SELECT * FROM pokriveni WHERE "+
//      cbase+" = '"+base.csk+"'"));
    pok.open();
    if (mod != null) this.getJpTableView().removeTableModifier(mod);
    this.getJpTableView().addTableModifier(mod = new raTableModifier()/*,
      new String[] {"IZNOS"}, new String[] {"CSKSTAVKE"}, new String[] {cthis}, pok)*/ {
      Column dsCol;
      Variant v = new Variant();
      DataRow dr;
      public boolean doModify() {
        if (getTable() instanceof JraTable2) {
          dsCol = ((JraTable2) getTable()).getDataSetColumn(getColumn());
          if (dsCol == null) return false;
          ((JraTable2) getTable()).getDataSet().getVariant("CSKSTAVKE", getRow(), v);
          if (dsCol.getColumnName().equalsIgnoreCase("RSALDO")) {
            dr = ld.raLookup(pok, cthis, v.toString());
            return dr != null;
          } 
        }
        return false;
      }
      public void modify() {
        dr.getVariant("IZNOS", v);
        setComponentText(dsCol.format(v));
      }
      public void setComponentText(String txt) {
        if (renderComponent instanceof JLabel) {
          ((JLabel)renderComponent).setText(txt);
        } else if (renderComponent instanceof JTextComponent) {
          ((JTextComponent)renderComponent).setText(txt);
        }
  }
    });
  }

  private void findBestMatch() {
    int row = view.getRow(), bestRow = -1, bestValRow = -1;
    double bestMatch = 0, bestVal = 0;
    getJpTableView().enableEvents(false);
    // prvo probaj naci vec pokriveni dokument
    for (pok.first(); pok.inBounds() && bestMatch < 1; pok.next())
      if (ld.raLocate(view, "CSKSTAVKE", pok.getString(cthis))) {
        bestMatch = 1;
        bestRow = view.getRow();
      }

    for (view.first(); view.inBounds() && bestMatch < 1; view.next()) {
      BigDecimal bsal = bset.getBigDecimal(salcol), vsal = view.getBigDecimal(salcol);
      if (bsal.signum() != 0 || vsal.signum() != 0) {
        double dok = Aus.heuristicCompare(view.getString("BROJDOK"), bset.getString("BROJDOK"));
        double val = bsal.abs().min(vsal.abs()).doubleValue() /
                     bsal.abs().max(vsal.abs()).doubleValue();
        double bm = dok * val;
        if (bm > bestMatch) {
          bestMatch = bm;
          bestRow = view.getRow();
        }
        if (val == 1. && dok > bestVal) {
          bestVal = dok;
          bestValRow = view.getRow();
        }
      }
    }
    if (bestMatch == 1.) view.goToRow(bestRow);
    else if (bestValRow >= 0) view.goToRow(bestValRow);
    else if (bestMatch > 0.25) view.goToRow(bestRow);
    else view.first();
    getJpTableView().enableEvents(true);
  }

  public void beforeShow() {
//    if (skip) return;
    System.out.println("before show pow");
    oldcsk = "";
    bset = Skstavke.getDataModule().getTempSet(
        Condition.equal("CSKSTAVKE", dm.getSkstavkeBase().getString("CSKSTAVKE")));
    bset.open();
        
    findMatchSides();
    if (toMatch) {
//      toMatch = false;
      addMatch();
    } else setFilter("");
    findPokriveni();
    checkChanges();
    if (toMatch) findBestMatch();
//    checkEmpty();
  }

  public void table2Clicked() {
    if (raSaldaKonti.isDomVal(bset) != raSaldaKonti.isDomVal(view)) {
      enterPressed(false);
      return;
    }
    if (tot.getPokriveno().signum() == 0 &&
        tot.getTotal().compareTo(view.getBigDecimal(salcol)) == 0) {
      enterPressed(false);
      if (tot.getPokriveno().signum() != 0 &&
          tot.getSaldo().signum() == 0) {
        this.hide();
      } else getJpTableView().fireTableDataChanged();
    } else partialMatch();
  }

  public void partialMatch() {
//    if (isKob() || base.kob) {
//      enterPressed();
//      return;
//    }
    DataSet rset = bset, uset = view;
    if (!ld.raLocate(pok, "CRACUNA", view.getString("CSKSTAVKE")) &&
        !ld.raLocate(pok, "CUPLATE", view.getString("CSKSTAVKE"))) {
      pok.insertRow(false);
      pok.setString(cbase, bset.getString("CSKSTAVKE"));
      pok.setString(cthis, view.getString("CSKSTAVKE"));
      pok.post();
    }
    BigDecimal orig = pok.getBigDecimal("IZNOS");
    if (!pok.getString("CRACUNA").equals(bset.getString("CSKSTAVKE"))) {
      rset = view;
      uset = bset;
    }
      
    dlgCover pd;
    if (this.getWindow() instanceof Frame)
      pd = new dlgCover((Frame) this.getWindow(), "Djelomièno Pokrivanje");
    if (this.getWindow() instanceof Dialog)
      pd = new dlgCover((Dialog) this.getWindow(), "Djelomièno Pokrivanje");
    else pd = new dlgCover("Djelomièno Pokrivanje");
    if (pd.changePok(rset, uset, pok)) {
      raSaldaKonti.matchIznos(rset, uset, pok.getBigDecimal("IZNOS").subtract(orig));
      if (pok.getBigDecimal("IZNOS").signum() == 0)
        pok.deleteRow();
//      tot.addPokriveno(pok.getBigDecimal("IZNOS").subtract(orig));

//      totalc = totalc.add(orig).subtract(bset.getBigDecimal("SALDO"));
      
      if (!R2Handler.saveChangesInTransaction(new QueryDataSet[] {bset,view,pok})) {
        dM.getDataModule().getSynchronizer().markAsDirty("pokriveni");
      } else refreshBase();
      getJpTableView().fireTableDataChanged();
//      base.getJpTableView().fireTableDataChanged();
    } else pok.refresh();
    if (raSaldaKonti.isSimple() || raSaldaKonti.isDomVal(bset) == raSaldaKonti.isDomVal(view))
      checkChanges();
  }

  public void SetFokus(char mode) {
  }
  public boolean Validacija(char mode) {
    return false;
  }

//  private void reverseChanges() {
//    if (totalc.signum() != 0) {
//      raSaldaKonti.setSaldo(bset, bset.getBigDecimal("SALDO").add(totalc));
//      base.getJpTableView().fireTableDataChanged();
//      totalc = raSaldaKonti.n0;
//    }
//    checkChanges();
//  }

//  public void ZatvoriOstalo() {
//    if (totalc.signum() != 0)
//      reverseChanges();
//    base.setEnabled(true);
//  }

  private void enterPressed(boolean toNext) {
    BigDecimal ts;
    boolean pokBefore = ld.raLocate(pok, "CRACUNA", view.getString("CSKSTAVKE")) &&
                            pok.getBigDecimal("IZNOS").signum() != 0 ||
                          ld.raLocate(pok, "CUPLATE", view.getString("CSKSTAVKE")) &&
                            pok.getBigDecimal("IZNOS").signum() != 0;
    
    boolean tecr = !raSaldaKonti.isSimple() && raSaldaKonti.isDomVal(bset) != raSaldaKonti.isDomVal(view);
    String salc = tecr ? "SALDO" : salcol;
    BigDecimal bs = bset.getBigDecimal(salc);
    BigDecimal cs = view.getBigDecimal(salc);
    if (pokBefore) {
      ts = pok.getBigDecimal("IZNOS").negate();
      if (pok.getString("CRACUNA").equals(bset.getString("CSKSTAVKE")))
        raSaldaKonti.matchIznos(bset, view, ts);
      else raSaldaKonti.matchIznos(view, bset, ts);
      pok.deleteRow();
//      pok.setBigDecimal("IZNOS", raSaldaKonti.n0);
    } else {
      ts = bs.abs().min(cs.abs());
      if (ts.signum() == 0) {
        view.next();
        return;
      }
      if (base.vmat.isRacunSide() == base.vmat.isRacunTip() && ts.signum() != bs.signum() ||
          base.vmat.isRacunSide() != base.vmat.isRacunTip() && ts.signum() == bs.signum())
        ts = ts.negate();
      if (!ld.raLocate(pok, "CRACUNA", view.getString("CSKSTAVKE")) &&
          !ld.raLocate(pok, "CUPLATE", view.getString("CSKSTAVKE"))) {
        pok.insertRow(false);
        pok.setString(cbase, bset.getString("CSKSTAVKE"));
        pok.setString(cthis, view.getString("CSKSTAVKE"));
      }
      pok.setBigDecimal("IZNOS", ts);
      raSaldaKonti.matchIznos(bset, view, ts);
      if (pok.rowCount() > 0 && pok.getBigDecimal("IZNOS").signum() == 0)
        pok.deleteRow();
    }
//    temp.insertRow(false);
//    temp.setBigDecimal("SALDO", ts);
//    temp.setString("CSKSTAVKE", bset.getString("CSKSTAVKE"));
//    temp.setString("CUPLATE", view.getString("CSKSTAVKE"));
    //if (!tecr) tot.addPokriveno(ts);
//    totalc = base.kob ? totalc.subtract(ts) : totalc.add(ts);
//    raSaldaKonti.setSaldo(bset, base.kob ? bs.add(ts) : bs.subtract(ts));
//    raSaldaKonti.setSaldo(view, raSaldaKonti.isKob(view) ? cs.add(ts) : cs.subtract(ts));
//    bset.setBigDecimal("SALDO", bs.subtract(ts));
//    view.setBigDecimal("SALDO", cs.subtract(ts));
    
    if (R2Handler.saveChangesInTransaction(new QueryDataSet[] {bset,view,pok}))
      refreshBase();
    
    dM.getDataModule().getSynchronizer().markAsDirty("pokriveni");
//    base.getJpTableView().fireTableDataChanged();
    if (toNext) {
      if (!view.next()) getJpTableView().fireTableDataChanged();
    }
    checkChanges();
  }

/*  private void snimiPromjene() {
    for (pok.first(); pok.inBounds();) {
      System.out.println(pok);
      if (pok.getStatus() == RowStatus.INSERTED &&
          pok.getBigDecimal("IZNOS").signum() == 0)
        pok.emptyRow();
      else if (pok.getBigDecimal("IZNOS").signum() == 0)
        pok.deleteRow();
      else pok.next();
    }
//    try {
//      view.saveChanges();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//

    if (!raTransaction.saveChangesInTransaction(new QueryDataSet[] {pok, bset, view})) {
      JOptionPane.showMessageDialog(this.getWindow(), "Promjena nije uspjela!", "Greška",
                                    JOptionPane.ERROR_MESSAGE);
    } else {
      totalc = raSaldaKonti.n0;
      view.refresh();
      getJpTableView().fireTableDataChanged();
//      refreshBase();
    }
  }*/

  public void jeprazno() {
    super.jeprazno();
    boolean enab = view.rowCount() != 0;
    aSve.setEnabled(enab);
    aIzn.setEnabled(enab);
  }

  private void checkChanges() {
    tot.initTotal(bset.getBigDecimal(raSaldaKonti.colSSaldo()));
    tot.initTotalPv(bset.getBigDecimal("SSALDO"));
    boolean baseRacTip = base.vmat.isRacunTip();
    boolean baseDom = raSaldaKonti.isDomVal(bset);
    
    BigDecimal rtecaj = raSaldaKonti.calcTecaj(bset);
    //BigDecimal rjedval = raSaldaKonti.findJedVal(bset);
    for (pok.first(); pok.inBounds(); pok.next()) {
      BigDecimal iznos = pok.getBigDecimal("IZNOS");
      if (iznos.signum() != 0) {
        boolean baseAsRac = pok.getString("CRACUNA").equals(base.csk);
        boolean otherDom = true;
        BigDecimal tecaj = rtecaj; //, jedval = rjedval;
        if (!raSaldaKonti.isSimple()) {
          DataRow other = ld.raLookup(viewro, "CSKSTAVKE", 
            pok.getString(baseAsRac ? "CUPLATE" : "CRACUNA"));          
          if (other != null) {
            if (raSaldaKonti.isTecUplata() ^ !baseAsRac) {
              tecaj = raSaldaKonti.calcTecaj(other);
              //jedval = raSaldaKonti.findJedVal(other);
            }
            otherDom = raSaldaKonti.isDomVal(other);
          } else System.err.println("illegal situation: "+pok);
        }
        BigDecimal ri = baseAsRac == baseRacTip ? iznos : iznos.negate();
        if (raSaldaKonti.isSimple()) {
          tot.addPokriveno(ri);
        } else {
          BigDecimal di = baseDom != otherDom ? ri :
            ri.multiply(tecaj).setScale(2, BigDecimal.ROUND_HALF_UP);
          
          tot.addPokrivenoPv(di);
          if (baseDom || !otherDom)
            tot.addPokriveno(ri);
        }
      }
    }
  }

  public void raQueryDataSet_navigated(NavigationEvent e) {
    csk = raSaldaKonti.findCSK(this.getRaQueryDataSet());
    if (!csk.equals(oldcsk)) {
      oldcsk = csk;
      jpDetail.setFieldValues();
    }
  }
  
  public void ZatvoriOstalo() {
    base.refreshAll();
  }

  private void jbInit() throws Exception {
    boolean dispExt = frmParam.getParam("sk", "displayExt", "N", "Prikaži kolonu " +
        "dodatnog broja dokumenta na kartici SK (D/N)", true).equalsIgnoreCase("D");
    
    setAutoFirstOnShow(false);
    Skstavke.getDataModule().setFilter(view, "1 = 0");
    view.open();
    view.getColumn("PVSALDO").setCaption("Dev. saldo");
    view.getColumn("PVSSALDO").setCaption("Dev. iznos");
    view.getColumn("SALDO").setCaption("Saldo");
    view.getColumn("SSALDO").setCaption("Iznos");

//    temp.setColumns(new Column[] {
//      (Column) dm.getSkstavke().getColumn("CSKSTAVKE").clone(),
//      (Column) dm.getSkstavke().getColumn("SALDO").clone()
//    });
//    temp.open();
    this.setRaQueryDataSet(view);
    if (raSaldaKonti.isSimple())
      this.setVisibleCols(dispExt ? new int[] {2,4,9,11, 19,20,31} : new int[] {2,4,9,19,20,31});
    else setVisibleCols(dispExt ? new int[] {2,4,9,11,28,29,31} : new int[] {2,4,9,28,29,31});
    this.setTitle("Stavke za pokrivanje");
    jpDetail = new jpMatchDetail();
    tot = new jpMatchTotals();
    JPanel jpall = new JPanel(new BorderLayout());
    jpall.add(jpDetail);
    jpall.add(tot, BorderLayout.SOUTH);
    this.setRaDetailPanel(jpall);
    jpDetail.BindComponents(this.getRaQueryDataSet());
    jpDetail.setBorder(null);
//    this.getJpTableView().addTableModifier(new skCoverTableModifier());
    getJpTableView().getColumnsBean().setSaveSettings(true);
    getJpTableView().getColumnsBean().setSaveName(getClass().getName());
    this.setSort(new String[] {"DATDOK"});

//    pok = dm.getPokriveni()

    removeRnvCopyCurr();
    this.getNavBar().removeStandardOptions(new int[] {raNavBar.ACTION_ADD, raNavBar.ACTION_PRINT,
        raNavBar.ACTION_DELETE, raNavBar.ACTION_TOGGLE_TABLE, raNavBar.ACTION_UPDATE});

    this.addOption(aSve = new raNavAction("Pokrij / raskrij", raImages.IMGOPEN, KeyEvent.VK_ENTER) {
      public void actionPerformed(ActionEvent e) {
        JraKeyListener.enterNow = false;
        if (view.rowCount() > 0) {
          if (base.isOldDok()) return;
          enterPressed(true);
        }
      }
    }, 0);

    this.addOption(aIzn = new raNavAction("Pokrij iznos", raImages.IMGPREFERENCES, KeyEvent.VK_F4) {
      public void actionPerformed(ActionEvent e) {
        if (view.rowCount() > 0) {
          if (base.isOldDok()) return;
          partialMatch();
        }
      }
    }, 1);

    this.addOption(new raNavAction("Prikaži pokrivene", raImages.IMGSTAV, KeyEvent.VK_F6) {
      public void actionPerformed(ActionEvent e) {
        toMatch = false;
        toMatchAll = false;
        setFilter("");
      }
    }, 2);

    this.addOption(new raNavAction("Prikaži sve odgovaraju\u0107e", raImages.IMGHISTORY, KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
        if (base.isOldDok()) return;
        toMatch = true;
        toMatchAll = false;
        addMatch();
      }
    }, 3);

    this.addOption(new raNavAction("Prikaži sve nepokrivene", raImages.IMGMOVIE, KeyEvent.VK_F8) {
      public void actionPerformed(ActionEvent e) {
        //        frmKartica.this.setEnabled(false);
        if (base.isOldDok()) return;
        toMatch = true;
        toMatchAll = true;
        addMatch();
      }
    }, 4);
//    this.addOption(aSave = new raNavAction("Snimi promjene", raImages.IMGIMPORT, KeyEvent.VK_F10) {
//      public void actionPerformed(ActionEvent e) {
//        snimiPromjene();
//      }
//    }, 4);

/*    this.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_ENTER) {
          JraKeyListener.enterNow = false;
          if (view.rowCount() > 0)
            enterPressed();
          e.consume();
        }
      }
    }); */
  }
}