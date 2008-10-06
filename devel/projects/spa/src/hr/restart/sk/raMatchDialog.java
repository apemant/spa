/****license*****************************************************************
**   file: raMatchDialog.java
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

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import hr.restart.baza.Condition;
import hr.restart.baza.PokriveniRadni;
import hr.restart.baza.Skstavke;
import hr.restart.baza.dM;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraKeyListener;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.NavigationAdapter;
import hr.restart.util.OKpanel;
import hr.restart.util.Util;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;
import hr.restart.util.raNavAction;
import hr.restart.zapod.raKonta;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.TooManyListenersException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.RowFilterListener;
import com.borland.dx.dataset.RowFilterResponse;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

public class raMatchDialog {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  lookupData ld = lookupData.getlookupData();

  jpMatchDetail jpDetail = new jpMatchDetail();
  JPanel main = new JPanel();
  JraDialog dlg;
  DataSet base;
  static String salcol = raSaldaKonti.colSaldo();

  raJPTableView rajp = new raJPTableView() {
    public void mpTable_doubleClicked() {
      table2Clicked();
    }
  };
  raMatchTableModifier mod;

  QueryDataSet view = new QueryDataSet() {
    public void refresh() {
      rajp.enableEvents(false);
      super.refresh();
      updateView();
      checkEmpty();
      rajp.enableEvents(true);
    }
    public boolean saveChangesSupported() {
      return false;
    }
    public void saveChanges() {}
  };
  
  StorageDataSet viewro = new StorageDataSet();

  String[] dfields = {"VRDOK", "OPIS", "OZNVAL", "TECAJ", "CSKSTAVKE", "ID", "IP", salcol};
  DataRow dbase, dthis;
  String oldcsk, cbase, cthis, oznval;
  String pokKonto, pokVal;
  boolean upl, kob, okpress, vrdokonto, matched, alldev;

  NavigationAdapter nav = new NavigationAdapter() {
    public void navigated(DataSet ds) {
      String csk = raSaldaKonti.findCSK(view);
      if (!csk.equals(oldcsk)) {
        oldcsk = csk;
        jpDetail.setFieldValues();
      }
    }
  };

  QueryDataSet pok;

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };

  raNavAction aSve, aIzn, aSave;

  jpMatchTotals tot = new jpMatchTotals();

  String baseKey;
  
  boolean unopened;

  public raMatchDialog() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void OKPress() {
    okpress = true;
    closeDialog();
  }

  private void cancelPress() {
    okpress = false;
    closeDialog();
  }

  public String getDeleteQuery(DataSet gks) {
    base = gks;
    try {
      findVrdok();
      return "DELETE FROM pokriveniradni WHERE "+cbase+"='"+raSaldaKonti.findGKCSK(gks)+"'";
    } catch (IllegalArgumentException e) {
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean isAccepted() {
    return okpress;
  }
  
  public boolean isFirstTime() {
    return unopened;
  }

  public DataSet getPokriveni() {
    return pok;
  }

  public DataSet getFirstSelectedRow() {
    if (pok.getRowCount() > 0)
      if (ld.raLocate(view, "CSKSTAVKE", pok.getString(cthis)))
        return view;
    return null;
  }

  public BigDecimal getTotalPokriveno() {
    return tot.getPokriveno();
  }
  
  public BigDecimal getTotalPokrivenoPv() {
    return tot.getPokrivenoPv();
  }

  public void show(Container parent) {
    showDialog(parent);
  }

  public void init(DataSet base, char mode, String oznval, boolean alldev) {
    initData(base, mode, oznval, alldev);
  }

  public void saveChanges(char mode) {
    try {
      findVrdok();
      setFilter();
      checkChangedPok();
      if (mode == 'N') {
        baseKey = raSaldaKonti.findGKCSK(base);
        for (pok.first(); pok.inBounds(); pok.next())
          pok.setString(cbase, baseKey);
      }
      pok.saveChanges();
    } catch (IllegalArgumentException e) {
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setTitle() {
    String dod = "";
    if (oznval != null && !oznval.equalsIgnoreCase(hr.restart.zapod.Tecajevi.getDomOZNVAL()))
      dod = " (" + oznval + ")";
    if (upl)
      dlg.setTitle("Ra\u010Duni (i OK) "+(matched ? "kojima je pokrivena uplata " : "za pokrivanje uplate ")+
                    base.getString("BROJDOK")+" od "+rdu.dataFormatter(base.getTimestamp("DATDOK"))+dod);
    else if (kob)
      dlg.setTitle((matched ? "Uplate kojima je pokrivena knjižna obavijest " : "Uplate za pokrivanje knjižne obavijesti ")+
                    base.getString("BROJDOK")+" od "+rdu.dataFormatter(base.getTimestamp("DATDOK"))+dod);
  }

  private void findBrojdok() {
    double best = 0;
    String cskb = null;
    String brojdok = base.getString("BROJDOK");
    for (view.first(); view.inBounds(); view.next()) {
      double n = Aus.heuristicCompare(brojdok, view.getString("BROJDOK"), 
          raOptimisticMatch.getIgnore());
      if (n > best) {
        best = n;
        cskb = view.getString("CSKSTAVKE");
      }
    }
    if (cskb != null)
      ld.raLocate(view, "CSKSTAVKE", cskb);
    else
      view.first();
  }

  private void setFilter() {
    Condition minim = Aus.getKnjigCond().and(Condition.equal("CPAR", base.getInt("CPAR"))).
      and(Condition.from("DATUMKNJ", Util.getUtil().getYearBegin(Aus.getFreeYear())));
    /*.and(Aus.getYearCond("DATUMKNJ", Valid.getValid().findYear(base.getTimestamp("DATDOK"))));*/
    String devf = "";
    if (!alldev) devf = " AND (" + (raSaldaKonti.isDomVal(oznval) ? 
        raSaldaKonti.getValCond() : Condition.equal("OZNVAL", oznval)) + ")";
    
    String filter = "SELECT * FROM skstavke WHERE "+minim+(raSaldaKonti.isSimple() ?
        " AND pokriveno='N'" : " AND pvpok='N'" + devf);
    String vrd = !upl ? " AND vrdok IN ('IPL','UPL')" :
      " AND (vrdok IN ('URN','IRN') OR (vrdok='OKK' AND (IP<0 or ID>0))" +
      " OR (vrdok='OKD' AND (ID<0 or IP>0)))" ;

    vrdokonto = false;
    try {
      String konto = base.getString("BROJKONTA");
      if (raKonta.isKupac(konto))
        vrd = (!upl ? " AND vrdok='UPL'" : " AND (vrdok='IRN' OR (vrdok='OKK' AND (IP<0 or ID>0)))")
              + " AND brojkonta = '"+konto+"'";
      else if (raKonta.isDobavljac(konto))
        vrd = (!upl ? " AND vrdok='IPL'" : " AND (vrdok='URN' OR (vrdok='OKD' AND (ID<0 or IP>0)))")
              + " AND brojkonta = '"+konto+"'";
      vrdokonto = true;
    } catch (Exception e) {}
    view.close();
    view.removeRowFilterListener(null);
    view.setQuery(new QueryDescriptor(dM.getDataModule().getDatabase1(), filter + vrd));
    view.open();
    view.refresh();
    try {
      view.addRowFilterListener(new MatchFilter());
    } catch (TooManyListenersException e) {
      e.printStackTrace();
    }
    rajp.setDataSet(view);
    view.setSort(new SortDescriptor(new String[] {"DATDOK"}));
    jpDetail.BindComponents(rajp.getDataSet());
  }

  /*private void refilterQuery() {
    Condition minim = Aus.getKnjigCond().and(Condition.equal("CPAR", base.getInt("CPAR"))).
      and(Condition.from("DATUMKNJ", Util.getUtil().getYearBegin(Aus.getFreeYear())));
    String filter = "SELECT * FROM skstavke WHERE "+minim+(raSaldaKonti.isSimple() ?
        " AND pokriveno='N'" : " AND pvpok='N' AND oznval='"+oznval+"'");    
    String vrd = !upl ? " AND vrdok IN ('IPL','UPL')" :
      " AND (vrdok IN ('URN','IRN') OR (vrdok='OKK' AND (IP<0 or ID>0))" +
      " OR (vrdok='OKD' AND (ID<0 or IP>0)))" ;
    if (pok.getRowCount() > 0)
      try {
        String konto = view.getString("BROJKONTA");
        if (raKonta.isKupac(konto))
          vrd = (!upl ? " AND vrdok='UPL'" : " AND (vrdok='IRN' OR (vrdok='OKK' AND (IP<0 or ID>0)))")
              + " AND brojkonta = '"+konto+"'";
        else if (raKonta.isDobavljac(konto))
          vrd = (!upl ? " AND vrdok='IPL'" : " AND (vrdok='URN' OR (vrdok='OKD' AND (ID<0 or IP>0)))")
              + " AND brojkonta = '"+konto+"'";
      } catch (Exception e) {}
    String tcsk = view.getString("CSKSTAVKE");
    view.close();
    view.setQuery(new QueryDescriptor(dM.getDataModule().getDatabase1(), filter + vrd));
    view.open();
    view.refresh();
    ld.raLocate(view, "CSKSTAVKE", tcsk);
  }*/

  private void checkChangedPok() {
    for (pok.first(); pok.inBounds();)
      if (!ld.raLocate(view, "CSKSTAVKE", pok.getString(cthis)))
        pok.deleteRow();
      else {
        pokKonto = view.getString("BROJKONTA");
        pokVal = view.getString("OZNVAL");
        pok.next();
      }
  }

  private void initData(DataSet base, char mode, String oznval, boolean alldev) {
    this.base = base;
    this.oznval = oznval;
    this.alldev = alldev;
    unopened = true;
    try {
      if (mode == 'N') {
        baseKey = "NEW";
        pok = PokriveniRadni.getDataModule().getTempSet("1=0");
        pok.open();
      } else {
        findVrdok();
        baseKey = raSaldaKonti.findGKCSK(base);
        pok = PokriveniRadni.getDataModule().getTempSet(
            cbase + "='"+baseKey+"'");
        pok.open();
      }
    } catch (IllegalArgumentException e) {
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void findVrdok() {
    String vrdok = base.getString("VRDOK");
    upl = "KUP".equalsIgnoreCase(vrdok) || "UPL".equalsIgnoreCase(vrdok) ||
          "IPL".equalsIgnoreCase(vrdok) || "K".equalsIgnoreCase(base.getString("POKRIVENO"));
    kob = "KOB".equalsIgnoreCase(vrdok) || "OKK".equalsIgnoreCase(vrdok) ||
          "OKD".equalsIgnoreCase(vrdok);
    if (!upl && !kob) throw new IllegalArgumentException("VRDOK nije ni uplata ni K.O!");
    cbase = upl ? "cuplate" : "cracuna";
    cthis = upl ? "cracuna" : "cuplate";
  }

  private void setData() {
    matched = false;
    findVrdok();
    setFilter();
    checkChangedPok();
    if (pok.getRowCount() == 0 && base.getString("BROJDOK").length() > 0)
      findBrojdok();
    
    checkChanges();
    /*tot.initTotal(base.getBigDecimal("ID").add(base.getBigDecimal("IP")));
    for (pok.first(); pok.inBounds(); pok.next())
      tot.addPokriveno(pok.getBigDecimal("IZNOS"));*/
    tot.setBaseText(upl ? "Uplata" : "Knjižna obavijest");
    //tot.setValuta(oznval);
    if (mod != null) rajp.removeTableModifier(mod);
    rajp.addTableModifier(mod = new raMatchTableModifier());
    mod.setIgnoreColor(matched);
  }

  private void showDialog(Container parent) {
    setData();
    unopened = false;
    Container realparent = null;
    if (parent instanceof JComponent)
      realparent = ((JComponent) parent).getTopLevelAncestor();
    else if (parent instanceof Window)
      realparent = parent;

    if (realparent instanceof Dialog)
      dlg = new JraDialog((Dialog) realparent, "", true);
    else if (realparent instanceof Frame)
      dlg = new JraDialog((Frame) realparent, "", true);
    else dlg = new JraDialog((Frame) null, "", true);
    setTitle();
    nav.install(rajp.getDataSet());
    dlg.setContentPane(main);
    Aus.removeSwingKeyRecursive(main, KeyEvent.VK_F6);
    Aus.removeSwingKeyRecursive(main, KeyEvent.VK_F8);
    dlg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    dlg.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        cancelPress();
      }
    });
    rajp.getColumnsBean().initialize();
    dlg.pack();
    if (realparent != null)
      dlg.setLocationRelativeTo(realparent);
    okp.registerOKPanelKeys(dlg);
    rajp.initKeyListener(dlg);
    dlg.show();
    nav.uninstall(rajp.getDataSet());
  }

  private void closeDialog() {
    if (dlg != null) {
      dlg.dispose();
      dlg = null;
    }
  }

  private void updateView() {
    DataSet pokrad = PokriveniRadni.getDataModule().getTempSet();
    pokrad.open();
    view.first();
    while (view.inBounds()) {
      while (ld.raLocate(pokrad, cthis, view.getString("CSKSTAVKE"))) {
        if (!pokrad.getString(cbase).equals(baseKey))
          view.setBigDecimal(salcol, view.getBigDecimal(salcol).
              subtract(pokrad.getBigDecimal("IZNOS")));
        pokrad.emptyRow();
      }
      /*if (view.getBigDecimal(salcol).signum() == 0) view.emptyRow();
      else if (!matched) view.next();
      else if (ld.raLocate(pok, cthis, view.getString("CSKSTAVKE"))) view.next();
      else view.emptyRow();*/
      view.next();
    }
    view.first();
    
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

  private void jbInit() throws Exception {

    Skstavke.getDataModule().setFilter(view, "1=0");
    view.open();
    view.getColumn("SALDO").setCaption("Saldo");
    view.getColumn("SSALDO").setCaption("Iznos");
    view.getColumn("PVSALDO").setCaption("Dev. saldo");
    view.getColumn("PVSSALDO").setCaption("Dev. iznos");
    dbase = new DataRow(view, dfields);
    dthis = new DataRow(view, dfields);
    main.setLayout(new BorderLayout());
    JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    main.add(jsp);

    jsp.setResizeWeight(1);
    jsp.setTopComponent(rajp);
    jsp.setBottomComponent(jpDetail);
    jpDetail.jlBrojizv.setVisible(false);
    jpDetail.jraBrojizv.setVisible(false);
    jpDetail.lay.setHeight(140);

    JPanel jpdown = new JPanel(new BorderLayout());
    jpdown.add(tot);
    jpdown.add(okp, BorderLayout.SOUTH);
    main.add(jpdown, BorderLayout.SOUTH);

    rcc.EnabDisabAll(jpDetail, false);
    rcc.EnabDisabAll(tot, false);
    if (raSaldaKonti.isSimple())
      rajp.setVisibleCols(new int[] {2,4,9,19,20,31});
    else rajp.setVisibleCols(new int[] {2,4,9,28,29,31});
    rajp.getColumnsBean().setSaveSettings(true);
    rajp.getColumnsBean().setSaveName(getClass().getName());

    rajp.getNavBar().addOption(aSve = new raNavAction("Pokrij / raskrij", raImages.IMGOPEN, KeyEvent.VK_ENTER) {
      public void actionPerformed(ActionEvent e) {
        JraKeyListener.enterNow = false;
        if (view.rowCount() > 0)
          enterPressed(true);
      }
    }, 0);

    rajp.getNavBar().addOption(aIzn = new raNavAction("Pokrij iznos", raImages.IMGPREFERENCES, KeyEvent.VK_F4) {
      public void actionPerformed(ActionEvent e) {
        if (view.rowCount() > 0)
          partialMatch();
      }
    }, 1);

    rajp.getNavBar().addOption(new raNavAction("Prikaži pokrivene", raImages.IMGSTAV, KeyEvent.VK_F6) {
      public void actionPerformed(ActionEvent e) {
        mod.setIgnoreColor(matched = true);
        //view.refresh();
        refilter();
        setTitle();
//        setFilter("");
      }
    }, 2);

    rajp.getNavBar().addOption(new raNavAction("Prikaži sve odgovaraju\u0107e", raImages.IMGHISTORY, KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
        mod.setIgnoreColor(matched = false);
        //view.refresh();
        refilter();
        setTitle();
//        addMatch();
      }
    }, 3);
  }

/*  private boolean isKob() {
    return (view.getString("VRDOK").equalsIgnoreCase("OKK") ||
            view.getString("VRDOK").equalsIgnoreCase("OKD"));
  } */

  private void enterPressed(boolean toNext) {
    int oldnum = pok.getRowCount();
    BigDecimal ts;
    BigDecimal up = tot.getTotal();
    BigDecimal bs = tot.getSaldo();
    BigDecimal cs = view.getBigDecimal(salcol);
    if (ld.raLocate(pok, cthis, view.getString("CSKSTAVKE")) &&
        pok.getBigDecimal("IZNOS").signum() != 0) {
      ts = pok.getBigDecimal("IZNOS").negate();
      pok.deleteRow();
    } else {
      if (up.signum() == 0) ts = cs.abs();
      else ts = bs.abs().min(cs.abs());
      if (ts.signum() == 0) {
        if (toNext) view.next();
        return;
      }
      if (!pok.getString(cthis).equals(view.getString("CSKSTAVKE"))) {
        pok.insertRow(false);
        pok.setString(cbase, baseKey);
        pok.setString(cthis, view.getString("CSKSTAVKE"));
      }
      pok.setBigDecimal("IZNOS", ts);
    }
//    temp.insertRow(false);
//    temp.setBigDecimal("SALDO", ts);
//    temp.setString("CSKSTAVKE", bset.getString("CSKSTAVKE"));
//    temp.setString("CUPLATE", view.getString("CSKSTAVKE"));
    //tot.addPokriveno(ts);
    pokKonto = view.getString("BROJKONTA");
    pokVal = view.getString("OZNVAL");
    checkChanges();
    
//    bset.setBigDecimal("SALDO", bs.subtract(ts));
//    view.setBigDecimal("SALDO", cs.subtract(ts));
      if (oldnum == 0 && pok.getRowCount() > 0 ||
          oldnum > 0 && pok.getRowCount() == 0)
        //refilterQuery();
        refilter();
    if (!toNext || !view.next()) rajp.fireTableDataChanged();
//    checkChanges();
  }
  
  void refilter() {
    long row = view.getInternalRow();
    view.refilter();    
    view.goToInternalRow(row);
  }
  
  private void checkChanges() {
    String val = oznval;
    if (alldev && pok.rowCount() > 0) val = pokVal; 
    tot.setValuta(val, alldev);
    boolean dom = raSaldaKonti.isDomVal(val);
    
    if (dom && !alldev) 
      tot.initTotal(base.getBigDecimal("ID").add(base.getBigDecimal("IP")));
    else {
      tot.initTotal(base.getBigDecimal("DEVID").add(base.getBigDecimal("DEVIP")));
      tot.initTotalPv(base.getBigDecimal("ID").add(base.getBigDecimal("IP")));
    }

    boolean baseRacTip = raVrdokMatcher.isRacunTip(base);
    BigDecimal rtecaj = tot.isTotalDefined() ? raSaldaKonti.calcTecaj(base) : null;
    for (pok.first(); pok.inBounds(); pok.next()) {
      BigDecimal iznos = pok.getBigDecimal("IZNOS");
      if (iznos.signum() != 0) {
        boolean baseAsRac = pok.getString("CRACUNA").equals(baseKey);
        BigDecimal tecaj = rtecaj;
        if (!raSaldaKonti.isSimple()) {
          DataRow other = ld.raLookup(viewro, "CSKSTAVKE", 
            pok.getString(baseAsRac ? "CUPLATE" : "CRACUNA"));          
          if (other != null) {
            if (!baseAsRac || tecaj == null) {
              tecaj = raSaldaKonti.calcTecaj(other);
            }
          } else System.err.println("illegal situation: "+pok);
        }
        BigDecimal ri = baseAsRac == baseRacTip ? iznos : iznos.negate();
        if (raSaldaKonti.isSimple()) {
          tot.addPokriveno(ri);
        } else {
          BigDecimal di = dom ? ri :
            ri.multiply(tecaj).setScale(2, BigDecimal.ROUND_HALF_UP);
          tot.addPokrivenoPv(di);
          tot.addPokriveno(ri);
        }
      }
    }
  }

  public void table2Clicked() {
    if (tot.getPokriveno().signum() == 0 && !tot.isTotalDefined() ||
        tot.getTotal().compareTo(view.getBigDecimal(salcol)) == 0) {
      enterPressed(false);
      if (tot.getPokriveno().signum() != 0 &&
          tot.getSaldo().signum() == 0) {
        OKPress();
      } else rajp.fireTableDataChanged();
    } else partialMatch();
  }

  private void partialMatch() {
/*    if (!tot.isTotalDefined()) {
      enterPressed(true);
      return;
    }*/
    int oldnum = pok.getRowCount();
    view.copyTo(dfields, view, dfields, dthis);
    view.copyTo(dfields, view, dfields, dbase);
    dbase.setString("VRDOK", upl ? "UPL" : "OKK");
    if (base.getString("OPIS").length() > 0)
      dbase.setString("OPIS", base.getString("OPIS"));
    else dbase.setString("OPIS", "Unos u tijeku");
    dbase.setString("CSKSTAVKE", baseKey);
    dbase.setBigDecimal("ID", dthis.getBigDecimal("ID"));
    dbase.setBigDecimal("IP", dthis.getBigDecimal("IP"));

    System.out.println("cthis: "+cthis+"  "+view.getString("CSKSTAVKE"));
    if (!ld.raLocate(pok, cthis, view.getString("CSKSTAVKE"))) {
      pok.insertRow(false);
      pok.setString(cbase, baseKey);
      pok.setString(cthis, view.getString("CSKSTAVKE"));
      pok.post();
    }
    BigDecimal orig = pok.getBigDecimal("IZNOS");
    dthis.setBigDecimal(salcol, dthis.getBigDecimal(salcol).subtract(orig));
    if (!tot.isTotalDefined()) {      
      dbase.setBigDecimal(salcol, dthis.getBigDecimal(salcol));
    } else {
      dbase.setBigDecimal(salcol, tot.getSaldo());
    }
    dlgCover pd = new dlgCover(dlg, "Djelomièno pokrivanje");
    if (pd.changePok(dbase, dthis, pok)) {
      pokKonto = view.getString("BROJKONTA");
      pokVal = view.getString("OZNVAL");
      //tot.addPokriveno(pok.getBigDecimal("IZNOS").subtract(orig));
      if (pok.getBigDecimal("IZNOS").signum() == 0)
        pok.deleteRow();
        if (oldnum == 0 && pok.getRowCount() > 0 ||
          oldnum > 0 && pok.getRowCount() == 0)
        //refilterQuery();
          refilter();
      rajp.fireTableDataChanged();
    } else if (pok.getBigDecimal("IZNOS").signum() == 0)
      pok.deleteRow();
    checkChanges();
  }

  private void checkEmpty() {
    boolean enab = view.rowCount() != 0;
    aSve.setEnabled(enab);
    aIzn.setEnabled(enab);
  }
  
  class MatchFilter implements RowFilterListener {

    public void filterRow(ReadRow row, RowFilterResponse resp) {
      boolean keep = true;
      
      if (row.getBigDecimal(salcol).signum() == 0) keep = false;
      else if (matched)
        keep = ld.raLocate(pok, cthis, row.getString("CSKSTAVKE"));
      else {
        if (!vrdokonto && pok.getRowCount() > 0 && 
            !row.getString("BROJKONTA").equals(pokKonto)) keep = false;
        if (alldev && pok.getRowCount() > 0 &&
            (raSaldaKonti.isDomVal(row) != raSaldaKonti.isDomVal(pokVal) ||
            (!raSaldaKonti.isDomVal(pokVal) && 
                !row.getString("OZNVAL").equals(pokVal)))) keep = false;
      }
      if (keep) resp.add();
      else resp.ignore();
    }
  }

  class raMatchTableModifier extends raTableModifier {
    Column dsCol;
    Variant v = new Variant();
    Variant s = new Variant();
    Color m = null;
    Color g = Color.green.darker().darker().darker();
    boolean ignore;

    private Color halfTone(Color cFrom, Color cTo, float factor) {
      return new Color((int) (cFrom.getRed() * (1 - factor) + cTo.getRed() * factor),
                       (int) (cFrom.getGreen() * (1 - factor) + cTo.getGreen() * factor),
                       (int) (cFrom.getBlue() * (1 - factor) + cTo.getBlue() * factor));
    }

    public void setIgnoreColor(boolean ignore) {
      this.ignore = ignore;
    }

    public boolean doModify() {
      if (getTable() instanceof JraTable2) {
        dsCol = ((JraTable2) getTable()).getDataSetColumn(getColumn());
        if (dsCol == null) return false;
        ((JraTable2) getTable()).getDataSet().getVariant("CSKSTAVKE", getRow(), v);
        if (dsCol.getColumnName().equalsIgnoreCase("RSALDO") ||
            dsCol.getColumnName().equalsIgnoreCase(salcol) || !ignore)
          return ld.raLocate(pok, cthis, v.toString());
      }
      return false;
    }
    public void modify() {
      if (dsCol != null && dsCol.getColumnName().equalsIgnoreCase("RSALDO")) {
        pok.getVariant("IZNOS", v);
        setComponentText(dsCol.format(v));
      } else if (dsCol != null && dsCol.getColumnName().equalsIgnoreCase(salcol)) {
        ((JraTable2) getTable()).getDataSet().getVariant(salcol, getRow(), s);
        pok.getVariant("IZNOS", v);
        s.setBigDecimal(s.getBigDecimal().subtract(v.getBigDecimal()));
        setComponentText(dsCol.format(s));
      }
      if (!ignore) {
        JComponent jRenderComp = (JComponent)renderComponent;
        if (!isSelected()) {
          if (m == null) m = halfTone(Color.yellow, jRenderComp.getBackground(), 0.75f);
          jRenderComp.setBackground(m);
        } else {
          jRenderComp.setBackground(g);
        }
      }
    }
    public void setComponentText(String txt) {
      if (renderComponent instanceof JLabel) {
        ((JLabel)renderComponent).setText(txt);
      } else if (renderComponent instanceof JTextComponent) {
        ((JTextComponent)renderComponent).setText(txt);
      }
    }
  }
}

