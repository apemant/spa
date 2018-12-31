package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.IzvjPDV;
import hr.restart.baza.Orgpl;
import hr.restart.baza.Partneri;
import hr.restart.baza.Skstavke;
import hr.restart.baza.StIzvjPDV;
import hr.restart.baza.UIstavke;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.pl.raIzvjestaji;
import hr.restart.robno.raDateUtil;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.swing.XYPanel;
import hr.restart.swing.raInputDialog;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.*;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.dlgGetKnjig;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.jar.JarInputStream;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;
import com.borland.jbcl.layout.constraintsGetter;

public class frmPDV2 extends raUpitFat {
  
  JPanel datePanel;
  JLabel jlPer = new JLabel("Razdoblje");
  JraTextField jraPoctDat = new JraTextField();
  JraTextField jraKrajDat = new JraTextField();
    
  JraComboBox jraObrazac = new JraComboBox(new String[] {"Obrazac PDV","Obrazac PDV-S","Obrazac ZP","Obrazac PDV-K", 
      "Obrazac JOPPD (plaæe)", "Obrazac JOPPD (p.n.)", "Obrazac PPO", "Obrazac OPZ"});
  XYLayout xYlay = new XYLayout();
  StorageDataSet stds = new StorageDataSet();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  Util ut = Util.getUtil();
  Valid vl = Valid.getValid();
  QueryDataSet izvjpdv = null;//IzvjPDV.getDataModule().getFilteredDataSet("CIZ like 'Pdv%'");
  private String _izvjpdv_ciz_prefix = "Pdv";
  QueryDataSet izvjpdv_k = IzvjPDV.getDataModule().getTempSet("CIZ like 'Pok%'");
  QueryDataSet izvjpdv_k_all = IzvjPDV.getDataModule().getTempSet("CIZ like 'Pdv%' or CIZ like 'Pod%' or CIZ like 'Pok%'");
  String opcinarada;
  JOPPDhndlr JOPPD;
  JOPPDhndlrPN JOPPDPN;
  
  public JOPPDhndlr getJOPPD() {
    return JOPPD;
  }
  public frmPDV2() {
    try {
      datePanel = new JPanel();
      datePanel.setLayout(xYlay);
      xYlay.setWidth(700);
      xYlay.setHeight(50);
      stds.setColumns(new Column[] {
        dM.createTimestampColumn("DATUMOD"),
        dM.createTimestampColumn("DATUMDO")
      });
      JOPPD = new JOPPDhndlr(this);
      JOPPDPN = new JOPPDhndlrPN(this);
      jraPoctDat.setDataSet(stds);
      jraPoctDat.setColumnName("DATUMOD");

      jraKrajDat.setDataSet(stds);
      jraKrajDat.setColumnName("DATUMDO");
      datePanel.add(jlPer, new XYConstraints(230,15,-1,21));
      datePanel.add(jraPoctDat, new XYConstraints(340, 15, 100, 21));
      datePanel.add(jraKrajDat, new XYConstraints(445, 15, 100, 21));
      datePanel.add(jraObrazac, new XYConstraints(15, 15, 200, 21));
      setJPan(datePanel);
      jraObrazac.addItemListener(new ItemListener() {
        

        public void itemStateChanged(ItemEvent e) {
          clearJop();
          XYConstraints old = constraintsGetter.get(xYlay, jraKrajDat);
          if (jraObrazac.getSelectedIndex() == 4) {
            datePanel.remove(jraKrajDat);
            datePanel.add(JOPPD.getBtns(), old);
            jlPer.setText("Datum isplate");
            QueryDataSet orgpl = Orgpl.getDataModule().getTempSet(Condition.equal("CORG", OrgStr.getKNJCORG()));
            orgpl.open();
            orgpl.first();
            opcinarada = "0"+raIzvjestaji.convertCopcineToRS(orgpl.getString("COPCINE"));
            stds.setTimestamp("DATUMOD", orgpl.getTimestamp("DATUMISPL"));
            stds.post();
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                datePanel.repaint();
              }
            });
          } else if (jraObrazac.getSelectedIndex() == 5) {
            datePanel.remove(jraKrajDat);
            datePanel.add(JOPPDPN.getBtns(), old);
            jlPer.setText("Datum isplate");
            stds.setTimestamp("DATUMOD", vl.getToday());
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                datePanel.repaint();
              }
            });
          } else if (jraObrazac.getSelectedIndex() == 7) {
            if (vl.getToday().before(Aus.createTimestamp(2016, 5, 1))) {
              stds.setTimestamp("DATUMOD", Aus.createTimestamp(2015, 1, 1));
              stds.setTimestamp("DATUMDO", Aus.createTimestamp(2015, 12, 31));
            } else {
              int month = Integer.parseInt(ut.getMonth(vl.getToday()));
              int year = Integer.parseInt(ut.getYear(vl.getToday()));
              if (month < 2) stds.setTimestamp("DATUMOD", Aus.createTimestamp(year - 1, 10, 1));
              else if (month < 5) stds.setTimestamp("DATUMOD", Aus.createTimestamp(year, 1, 1));
              else if (month < 8) stds.setTimestamp("DATUMOD", Aus.createTimestamp(year, 4, 1));
              else if (month < 11) stds.setTimestamp("DATUMOD", Aus.createTimestamp(year, 7, 1));
              else stds.setTimestamp("DATUMOD", Aus.createTimestamp(year, 10, 1));
              stds.setTimestamp("DATUMDO", ut.getLastDayOfMonth(ut.addMonths(stds.getTimestamp("DATUMOD"), 2)));
            }
          } else if (jraObrazac.getSelectedIndex() < 4) {
            stds.setTimestamp("DATUMOD", ut.getFirstDayOfMonth(ut.addMonths(vl.getToday(), -1)));
            stds.setTimestamp("DATUMDO", ut.getLastDayOfMonth(ut.addMonths(vl.getToday(), -1)));
          }
          datePanel.validate();
          //System.err.println("jraObrazac.itemStateChanged");
        }
      });
      getJPTV().getNavBar().addOption(new raNavAction("Dodaj", raImages.IMGADD, KeyEvent.VK_F2) {
        public void actionPerformed(ActionEvent e) {
          addNew();
        }
      });
      getJPTV().getNavBar().addOption(new raNavAction("Obriši", raImages.IMGDELETE, KeyEvent.VK_F3) {
        public void actionPerformed(ActionEvent e) {
          delete();
        }
      });
      getJPTV().getNavBar().addOption(new raNavAction("Alati", raImages.IMGPREFERENCES, KeyEvent.VK_F9) {
        public void actionPerformed(ActionEvent e) {
          alati();
        }
      });
      _this = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
/*
 
 */
  protected void alati() {
    if (jraObrazac.getSelectedIndex() == 4) {
      JOPPD.alati();
    }
    if (jraObrazac.getSelectedIndex() == 7) {
      OPZ_alati();
    }
  }
  protected void delete() {
    int pick = jraObrazac.getSelectedIndex();
    if (pick == 0 || pick == 3) return;
    if (JOptionPane.showConfirmDialog(getWindow(), "Jeste li sigurni da želite obrisati red?", "Pozor", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
      getJPTV().getStorageDataSet().deleteRow();
      getJPTV().fireTableDataChanged();
    }
  }
  
  void OPZ_alati() {
      JraDialog dlgAl = new JraDialog(getJframe(), "Alati");
      dlgAl.setModal(true);
//      XYLayout xyl = new XYLayout(500,500);
      JPanel jp = new JPanel(new GridLayout(0, 1, 5, 5));
      jp.add(new JButton(new AbstractAction("Popravi PDV na raèunima iz PS (oprez!)") {
        public void actionPerformed(ActionEvent e) {
          fixPorez();
        }
      }));
      jp.add(new JButton(new AbstractAction("Popravi lipe na prevelikom PDV-u") {
        public void actionPerformed(ActionEvent e) {
          fixPorez2();
        }
      }));
      dlgAl.setContentPane(jp);
      dlgAl.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      startFrame.getStartFrame().centerFrame(dlgAl, 0, "Alati");
      dlgAl.show();
  }
  
  void fixPorez() {
    try {
      Timestamp cutoff = Aus.createTimestamp(2012, 3, 1);
      BigDecimal marg = new BigDecimal("0.02");
      BigDecimal pdv25 = new BigDecimal(25).movePointLeft(2);
      BigDecimal pdv23 = new BigDecimal(23).movePointLeft(2);
      pdv25 = Aus.one0.subtract(Aus.one0.divide(Aus.one0.add(pdv25), 20, BigDecimal.ROUND_HALF_UP));
      pdv23 = Aus.one0.subtract(Aus.one0.divide(Aus.one0.add(pdv23), 20, BigDecimal.ROUND_HALF_UP));
      
      getJPTV().enableEvents(false);
      for (setOPZ.first(); setOPZ.inBounds(); setOPZ.next()) 
        if (setOPZ.getString("PS").equals("D") && setOPZ.getBigDecimal("PDV").signum() == 0)
          if (setOPZ.getTimestamp("DATDOK").before(cutoff))
            setOPZ.setBigDecimal("PDV", setOPZ.getBigDecimal("SSALDO").multiply(pdv23).setScale(2, BigDecimal.ROUND_HALF_UP));
          else setOPZ.setBigDecimal("PDV", setOPZ.getBigDecimal("SSALDO").multiply(pdv25).setScale(2, BigDecimal.ROUND_HALF_UP));
    } finally {
      getJPTV().enableEvents(true);
    }
    getJPTV().fireTableDataChanged();
  }
  
  void fixPorez2() {
    try {
      BigDecimal marg = new BigDecimal("0.02");
      BigDecimal pdv25 = new BigDecimal(25).movePointLeft(2);
      pdv25 = Aus.one0.subtract(Aus.one0.divide(Aus.one0.add(pdv25), 20, BigDecimal.ROUND_HALF_UP));
      
      getJPTV().enableEvents(false);
      for (setOPZ.first(); setOPZ.inBounds(); setOPZ.next()) 
        if (setOPZ.getBigDecimal("PDV").subtract(marg).compareTo(
            setOPZ.getBigDecimal("SSALDO").multiply(pdv25).setScale(2, BigDecimal.ROUND_HALF_UP)) > 0) {
          setOPZ.setBigDecimal("PDV", setOPZ.getBigDecimal("SSALDO").multiply(pdv25).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
    } finally {
      getJPTV().enableEvents(true);
    }
    getJPTV().fireTableDataChanged();
  }
  
  public boolean Validacija() {
    if (jraObrazac.getSelectedIndex() == 5)
      return JOPPDPN.checkPeriod();
    if (jraObrazac.getSelectedIndex() == 7) {
      String yod = ut.getYear(getDatumOd());
      String ydo = ut.getYear(getDatumDo());
      
      if (!yod.equals(ydo)) {
        JOptionPane.showMessageDialog(getWindow(), "Razdoblje mora biti unutar iste godine!", "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      
      if (yod.equals("2015")) {
        if (!ut.sameDay(getDatumOd(), ut.getYearBegin(yod)) ||
            !ut.sameDay(getDatumDo(), ut.getYearEnd(ydo))) {
          JOptionPane.showMessageDialog(getWindow(), "Za 2015. godinu razdoblje mora biti cijela godina!", "Greška", JOptionPane.ERROR_MESSAGE);
          return false;
        }
      } else {
        if (!ut.sameDay(getDatumOd(), ut.getFirstDayOfMonth(getDatumOd())) ||
            !ut.sameDay(getDatumDo(), ut.getLastDayOfMonth(getDatumDo())) ||
            !ut.sameDay(ut.addMonths(getDatumOd(), 2), ut.getFirstDayOfMonth(getDatumDo()))) {
          JOptionPane.showMessageDialog(getWindow(), "Razdoblje mora biti kvartalno!", "Greška", JOptionPane.ERROR_MESSAGE);
          return false;
        }
      }
    }
    return true;
  }

  public void okPress() {
    setDataSet(null);
    int pick = jraObrazac.getSelectedIndex();
    System.err.println("***** PICK:"+pick);
    switch (pick) {
    case 0:
      doPDV();
      break;
      
    case 1:
      doPDV_S();
      break;
      
    case 2:
      doZP();
      break;
      
    case 3:
      doPDV_K();
      break;
      
    case 4:
      JOPPD.doJOPPD();
      break;
      
    case 5:
      JOPPDPN.doJOPPD();
      break;
      
/*    case 6:
      doPDV13();
      break;
*/
    case 6:
      doPPO20132014();
      break;
      
    case 7:
      doOPZ();
      break;
      
    default:
      break;
    }

  }
  protected void addNew() {
    int pick = jraObrazac.getSelectedIndex();
    System.err.println("***** PICK:"+pick);
    switch (pick) {
    case 0:
      //addPDV();
      break;
      
    case 1:
      addPDV_S();
      break;
      
    case 2:
      addZP();
      break;
      
    case 3:
      //addPDV_K();
      break;
      
    case 4:
      addJOPPD();
      break;
    
    case 5:
      addJOPPD();
      break;
      
    case 6:
      addPPO();
      break;
      
    case 7:
      addOPZ();
      break;
      
    default:
      break;
    }
  }
  
  private void addJOPPD() {
    addGeneric_part1();
    getJPTV().getStorageDataSet().setString("COPRADA", opcinarada);
//    getAllSifreFor(null, getJPTV().getStorageDataSet());
    addGeneric_part2();    
  }

  private void addZP() {
    addGeneric();
  }

  private void addPDV_S() {
    addGeneric();
  }
  private void addPPO() {
    addGeneric();
  }
  private void addOPZ() {
    getJPTV().getStorageDataSet().last();
    getJPTV().getStorageDataSet().insertRow(false);
    getJPTV().getStorageDataSet().setBigDecimal("SSALDO", Aus.zero2);
    addGeneric_part2();
  }
  private void addGeneric() {
    addGeneric_part1();
    addGeneric_part2();
  }

  private void addGeneric_part2() {
    getJPTV().getStorageDataSet().post();
    getJPTV().fireTableDataChanged();
    jptv_doubleClick();
  }

  private void addGeneric_part1() {
    int rbr = getJPTV().getStorageDataSet().rowCount()+1;
    getJPTV().getStorageDataSet().last();
    getJPTV().getStorageDataSet().insertRow(false);
    getJPTV().getStorageDataSet().setInt("RBR", rbr);
  }
  
  public boolean runFirstESC() {
    return getJPTV().getStorageDataSet() != null;
  }

  public void firstESC() {
    raCommonClass.getraCommonClass().EnabDisabAll(this.getJPan(),true);
    killAllReports();
    this.getJPTV().clearDataSet();
    removeNav();
    
    clearJop();
    setPDV = null;
    setPDVS = null;
    setZP = null;
    JOPPD.firstESC();
 
    jraObrazac.setSelectedIndex(0);
    jraPoctDat.requestFocusLater();
    System.gc();
  }
  private void clearJop() {
    JOPPD.setMode("Dohvat");
    JOPPD.currOIB = null;
    JOPPDPN.currOIB = null;
    jlPer.setText("Razdoblje");
    XYConstraints old = constraintsGetter.get(xYlay, JOPPD.getBtns());
    if (old == null) old = constraintsGetter.get(xYlay, JOPPDPN.getBtns());
    if (old == null) old = constraintsGetter.get(xYlay, jraKrajDat);
    datePanel.remove(JOPPD.getBtns());
    datePanel.remove(JOPPDPN.getBtns());
    datePanel.add(jraKrajDat, old);
    stds.setTimestamp("DATUMOD", ut.getFirstDayOfMonth(ut.addMonths(vl.getToday(), -1)));
    stds.setTimestamp("DATUMDO", ut.getLastDayOfMonth(ut.addMonths(vl.getToday(), -1)));
  }

  public void componentShow() {
    clearJop();
    jraObrazac.setSelectedIndex(0);
  }
  
  StorageDataSet setOPZ;
  public StorageDataSet getSetOPZ() {
    return setOPZ;
  }
  
  private void doOPZ() {
    String kkonta = frmParam.getParam("sk", "opzKonta", "12*", "Popis konta (moguæa zvjezdica) kupaca za OPZ");
    String pkonta = frmParam.getParam("sk", "opzKontaPor", "2*", "Popis konta (moguæa zvjezdica) PDV-a za OPZ");
    
    String[] kka = kkonta.indexOf(',') > 0 ? new VarStr(kkonta).splitTrimmed(',') : new VarStr(kkonta).split();
    String[] pka = pkonta.indexOf(',') > 0 ? new VarStr(pkonta).splitTrimmed(',') : new VarStr(pkonta).split();
    
    raGlob[] kg = new raGlob[kka.length];
    raGlob[] pg = new raGlob[pka.length];
    
    for (int i = 0; i < kka.length; i++)
      kg[i] = new raGlob(kka[i]);
    
    for (int i = 0; i < pka.length; i++)
      pg[i] = new raGlob(pka[i]);
    
    HashSet kk = new HashSet();
    HashSet pk = new HashSet();
    
    DataSet akon = dm.getKontaAnalitic();
    akon.open();
    for (akon.first(); akon.inBounds(); akon.next()) {
      String kon = akon.getString("BROJKONTA");
      String saldak = akon.getString("SALDAK");
      if (saldak.equalsIgnoreCase("K"))
        for (int i = 0; i < kg.length; i++)
          if (kg[i].matches(kon)) {
            kk.add(kon);
            break;
          }
      if (saldak.equalsIgnoreCase("F"))
        for (int i = 0; i < pg.length; i++)
          if (pg[i].matches(kon)) {
            pk.add(kon);
            break;
          }
    }
    
    System.out.println("saldak: " + kk);
    System.out.println("porez: " + pk);
    
    Timestamp nadan = stds.getTimestamp("DATUMDO");
    Timestamp from = ut.getFirstSecondOfDay(ut.addDays(ut.addMonths(nadan, -72), 1));
    
    Timestamp psdat = ut.getYearBegin(Aus.getFreeYear());
    String psnal = "-" + Aus.getFreeYear() + "-00-";
    
    setOPZ = Aus.createSet("{Tip}TIP Partneri.OIB .NAZPAR PS:1 Skstavke.BROJDOK .DATDOK .DATDOSP .SSALDO .SALDO PDV.2 {Status}OPIS:100");
    
    QueryDataSet sk = Skstavke.getDataModule().openTempSet("CPAR VRDOK BROJDOK DATUMKNJ DATDOK DATDOSP OPIS ID IP SSALDO SALDO CSKSTAVKE CGKSTAVKE OZNVAL TECAJ",
        //Aus.getKnjigCond().and(Condition.from("DATUMKNJ", Aus.getGkYear(nadan))).and(Condition.till("DATDOSP", nadan)).
        Aus.getKnjigCond().and(Aus.getCurrGKDatumCond(nadan)).and(Condition.till("DATDOSP", nadan)).
        and(Condition.from("DATDOK", from)).and(Aus.getVrdokCond(true, true)).
        and(Condition.in("BROJKONTA", kk)).and(Condition.diff("POKRIVENO", "X")));
    
    System.out.println("input: " + sk.getOriginalQueryString());
    HashMap fisks = null;
    if (frmParam.getParam("sk", "opzFisk", "N", "Fiskalni brojevi raèuna na OPZ-STAT-1 (D,N)?").equalsIgnoreCase("D")) {
      fisks = new HashMap();
      DataSet foks = doki.getDataModule().openTempSet("CSKL GOD VRDOK BRDOK PNBZ2", 
          Condition.equal("FOK", "D").and(Condition.from("DATDOK", Aus.createTimestamp(2013, 7, 1))));
      for (foks.first(); foks.inBounds(); foks.next()) 
        if (!foks.getString("PNBZ2").startsWith("*"))
          fisks.put(foks.getString("CSKL") + "-" + foks.getString("GOD") + "-"
              + foks.getString("VRDOK") + "-" + foks.getInt("BRDOK"), foks.getString("PNBZ2"));
    }
    
    Timestamp dfrom = ut.addMonths(ut.getFirstDayOfMonth(nadan), 2);
    Condition c = Aus.getKnjigCond().and(Aus.getVrdokCond(true)).and(Condition.from("DATDOK", dfrom));
    
    raSaldaKonti.updateOutOfRangeSaldo(sk, c);
    
    QueryDataSet osk = Skstavke.getDataModule().openTempSet("CPAR VRDOK BROJDOK SSALDO",
        Aus.getKnjigCond().and(Condition.from("DATUMKNJ", Aus.getGkYear(nadan)).not()).
        and(Aus.getVrdokCond(true, true)).and(Condition.in("BROJKONTA", kk)));
    
    System.out.println("olds: " + osk.getOriginalQueryString());
    
    VarStr buf = new VarStr();
    HashSet fmp = new HashSet();
    // devilish prvi mjesec nakon godišnje obrade
    if (psdat.after(nadan) && ut.sameDay(nadan, ut.getLastDayOfYear(nadan)) ) {
      QueryDataSet nskf = Skstavke.getDataModule().openTempSet("CPAR VRDOK BROJDOK DATUMKNJ DATDOK DATDOSP ID IP SSALDO SALDO CSKSTAVKE CGKSTAVKE OZNVAL TECAJ",
          //Aus.getKnjigCond().and(Condition.from("DATUMKNJ", Aus.getGkYear(nadan))).and(Condition.till("DATDOSP", nadan)).
          Aus.getKnjigCond().and(Condition.from("DATUMKNJ", ut.getFirstDayOfYear(dfrom))).and(Condition.till("DATDOSP", nadan)).
          and(Condition.from("DATDOK", from)).and(Aus.getVrdokCond(true, true)).
          and(Condition.in("BROJKONTA", kk)).and(Condition.diff("POKRIVENO", "X")));
      
      raSaldaKonti.updateOutOfRangeSaldo(nskf, c);
      for (nskf.first(); nskf.inBounds(); nskf.next()) {
        if (nskf.getBigDecimal("SALDO").signum() == 0 &&
            nskf.getString("BROJDOK").startsWith("PS" + ut.getYear(dfrom) + ":")) 
          fmp.add(buf.clear().append(nskf.getInt("CPAR")).append('|').append(nskf.getString("VRDOK")).
              append('|').append(nskf.getString("BROJDOK")).toString());
      }
      System.out.println("Poks:" + fmp);
    }
    
    HashMap oldmap = new HashMap();
    for (osk.first(); osk.inBounds(); osk.next()) {
      String key = buf.clear().append(osk.getInt("CPAR")).append('|').append(osk.getString("VRDOK")).
          append('|').append(osk.getString("BROJDOK")).toString();
      oldmap.put(key, osk.getBigDecimal("SSALDO"));
    }

    QueryDataSet ui = UIstavke.getDataModule().openTempSet("CPAR VRDOK BROJDOK IP", 
        Aus.getKnjigCond().and(Condition.in("BROJKONTA", pk)).
        and(Condition.where("IP", Condition.NOT_EQUAL, Aus.zero0)));
    
    System.out.println("ui: " + ui.getOriginalQueryString());
    
    HashMap uimap = new HashMap();
    
    for (ui.first(); ui.inBounds(); ui.next()) {
      String key = buf.clear().append(ui.getInt("CPAR")).append('|').append(ui.getString("VRDOK")).
          append('|').append(ui.getString("BROJDOK")).toString();
      BigDecimal old = (BigDecimal) uimap.get(key);
      if (old == null) old = ui.getBigDecimal("IP");
      else old = old.add(ui.getBigDecimal("IP"));
      uimap.put(key, old);
    }
    
    BigDecimal porf = new BigDecimal("0.2");
    for (sk.first(); sk.inBounds(); sk.next()) {
      if (sk.getBigDecimal("SALDO").signum() == 0) continue;
            
      String key = buf.clear().append(sk.getInt("CPAR")).append('|').append(sk.getString("VRDOK")).
          append('|').append(sk.getString("BROJDOK")).toString();
      
      boolean ps = sk.getString("BROJDOK").startsWith("PS") && sk.getString("BROJDOK").indexOf(':') > 0;
      if (ps) key = buf.clear().append(sk.getInt("CPAR")).append('|').append(sk.getString("VRDOK")).
        append('|').append(sk.getString("BROJDOK").substring(sk.getString("BROJDOK").indexOf(':') + 1)).toString();
      
      if (fmp.size() > 0 && fmp.contains(buf.insert(buf.lastIndexOf('|') + 1, "PS" + ut.getYear(dfrom) + ":").truncate(50).toString())) {
        System.out.println("found to skip: " + sk);
        continue;
      }
      
      setOPZ.insertRow(false);
      if (ps) setOPZ.setString("PS", "D");
      else if (ut.sameDay(sk.getTimestamp("DATUMKNJ"), psdat) &&
          sk.getString("CGKSTAVKE").indexOf(psnal) > 0) setOPZ.setString("PS", "D");
      else setOPZ.setString("PS", "N");
      if (!Partneri.loc(sk)) setOPZ.setString("OPIS", "Greška: nepoznat partner - " +sk.getInt("CPAR"));
      else {
        setOPZ.setString("OIB", Partneri.get().getString("OIB"));
        setOPZ.setString("NAZPAR", Partneri.get().getString("NAZPAR"));
        setOPZ.setInt("TIP", Util.checkOIB(setOPZ.getString("OIB")) ? 1 : 3);
      }
      
      setOPZ.setTimestamp("DATDOK", sk.getTimestamp("DATDOK"));
      setOPZ.setTimestamp("DATDOSP", sk.getTimestamp("DATDOSP"));
      Aus.set(setOPZ, "SALDO", sk);
      if (!ps) {
        setOPZ.setString("BROJDOK", sk.getString("BROJDOK"));
        Aus.set(setOPZ, "SSALDO", sk);
        BigDecimal por = (BigDecimal) uimap.get(key);
        setOPZ.setBigDecimal("PDV", por == null ? Aus.zero2 : por);
        if (por == null) setOPZ.setString("OPIS", "Nepoznat iznos poreza");
      } else {
        setOPZ.setString("BROJDOK", sk.getString("BROJDOK").substring(sk.getString("BROJDOK").indexOf(':') + 1));
        BigDecimal iznos = (BigDecimal) oldmap.get(key);
        if (iznos != null) setOPZ.setBigDecimal("SSALDO", iznos);
        else {
          Aus.set(setOPZ, "SSALDO", sk);
          setOPZ.setString("OPIS", "Nepoznat originalni raèun");
        }
        BigDecimal por = (BigDecimal) uimap.get(key);
        if (por != null) setOPZ.setBigDecimal("PDV", por);
        else if (iznos == null)
          setOPZ.setString("OPIS", "Nepoznat originalni raèun i originalni iznos poreza");
        else setOPZ.setString("OPIS", "Nepoznat originalni iznos poreza");
      }
      if (setOPZ.getString("OPIS").length() == 0) {
        if (setOPZ.getBigDecimal("SSALDO").multiply(porf).setScale(2, BigDecimal.ROUND_HALF_UP)
            .compareTo(setOPZ.getBigDecimal("PDV")) < 0)
          setOPZ.setString("OPIS", "Iznos poreza je prevelik " + Aus.formatBigDecimal(
              setOPZ.getBigDecimal("PDV").subtract(setOPZ.getBigDecimal("SSALDO").multiply(porf).setScale(2, BigDecimal.ROUND_HALF_UP))));
      }
      if (fisks != null && sk.getString("OPIS").startsWith("Dokument ")) {
        String oldb = new VarStr(sk.getString("OPIS")).split()[1];
        String newb = (String) fisks.get(oldb);
        if (newb != null) setOPZ.setString("BROJDOK", newb);
      }
    }
    setOPZ.setTableName("setOPZ");
    setDataSetAndSums(setOPZ, new String[] {"SSALDO","SALDO","PDV"});
    killAllReports();
    addReport("hr.restart.sk.repOPZDisk", "Datoteka OPZ-STAT-1 za e-poreznu");
    setTitle("Obrazac OPZ-STAT-1 za period "+raDateUtil.getraDateUtil().dataFormatter(getDatumOd())+" - "+raDateUtil.getraDateUtil().dataFormatter(getDatumDo()));
  }

  StorageDataSet setZP;
  public StorageDataSet getSetZP() {
    return setZP;
  }
  
 
  
  private void doZP() {
    setZP = new StorageDataSet();
    setZP.addColumn(dM.createIntColumn("RBR"));
    setZP.addColumn(dM.createIntColumn("CPAR", "Partner"));
    setZP.addColumn(dM.createStringColumn("KODDRZ", "Kod države", 2));
    setZP.addColumn(dM.createStringColumn("PDVID", "Porezni broj", 30));
    setZP.addColumn(dM.createBigDecimalColumn("I1","Isporuka dobara"));
    setZP.addColumn(dM.createBigDecimalColumn("I2","Isporuka dobara 42 i 63"));
    setZP.addColumn(dM.createBigDecimalColumn("I3","Isporuka dobara - trostrani posao"));
    setZP.addColumn(dM.createBigDecimalColumn("I4","Obavljene usluge"));
    setZP.open();
    String qryZPcommon = getQryCommon();
    
    fillsetP("ZPCIZdob", setZP, qryZPcommon, "I1");
    fillsetP("ZPCIZdob42", setZP, qryZPcommon, "I2");
    fillsetP("ZPCIZdobTro", setZP, qryZPcommon, "I3");
    fillsetP("ZPCIZusl", setZP, qryZPcommon, "I4");
    
    int rbr = 0;
    for (setZP.first(); setZP.inBounds(); setZP.next())
      setZP.setInt("RBR", ++rbr);
    
    setZP.setTableName("setZP");
    setDataSetAndSums(setZP, new String[] {"I1","I2","I3","I4"});
    getJPTV().addTableModifier(
        new raTableColumnModifier("CPAR", new String[] {"CPAR", "NAZPAR"}, new String[] {"CPAR"}, new String[] {"CPAR"}, dM.getDataModule().getPartneri()));
    killAllReports();
    addReport("hr.restart.sk.repZPDisk", "Datoteka ZP za e-poreznu");
    setTitle("Obrazac ZP za period "+raDateUtil.getraDateUtil().dataFormatter(getDatumOd())+" - "+raDateUtil.getraDateUtil().dataFormatter(getDatumDo()));

  }

  StorageDataSet setPPO;
  public StorageDataSet getSetPPO() {
    return setPPO;
  }
  private void doPPO20132014() {
    setPPO = new StorageDataSet();
    setPPO.addColumn(dM.createIntColumn("RBR"));
    setPPO.addColumn(dM.createIntColumn("CPAR", "Partner"));
    setPPO.addColumn(dM.createStringColumn("OIB", "OIB",12));
    setPPO.addColumn(dM.createBigDecimalColumn("VRI","Vrijednost"));
    setPPO.addColumn(dM.createBigDecimalColumn("POR","Porez"));
    setPPO.addColumn(dM.createTimestampColumn("DATUMOD", "Datum od"));
    setPPO.addColumn(dM.createTimestampColumn("DATUMDO", "Datum do"));
    setPPO.open();
    fillsetPPO("PPOCIZvri","VRI");
//    fillsetPPO("PPOCIZpor","POR");
    setPPO.setTableName("setPPO");
    setDataSetAndSums(setPPO, new String[] {"VRI"});
    getJPTV().addTableModifier(
        new raTableColumnModifier("CPAR", new String[] {"CPAR", "NAZPAR"}, new String[] {"CPAR"}, new String[] {"CPAR"}, dM.getDataModule().getPartneri()));
    killAllReports();
    addReport("hr.restart.sk.repPPO20132014Disk", "Datoteka PPO za e-poreznu");
    setTitle("Obrazac PPO za period "+raDateUtil.getraDateUtil().dataFormatter(getDatumOd())+" - "+raDateUtil.getraDateUtil().dataFormatter(getDatumDo()));

    
  }
  private void fillsetPPO(String param, String col) {
    String qrycommon = getPPOQryCommon();
    String orovi = getOrovi(param);
    if (orovi == null) return;
    QueryDataSet upitP = Aus.q(qrycommon + orovi);
    dm.getPartneri().open();
    int rbr = 0;
    for (upitP.first(); upitP.inBounds(); upitP.next()) {
      if (!lookupData.getlookupData().raLocate(setPPO, new String[]{"CPAR","DATUMOD"}, 
          new String[]{upitP.getInt("CPAR")+"",ut.getFirstDayOfMonth(upitP.getTimestamp("DATPRI")).toString()})) {//TU PO cpar i datumOD
        if (lookupData.getlookupData().raLocate(dm.getPartneri(), "CPAR", upitP.getInt("CPAR")+"")) {
          String oib =  dm.getPartneri().getString("OIB");
          setPPO.insertRow(false);
          rbr++;
          setPPO.setInt("RBR", rbr);
          setPPO.setInt("CPAR", upitP.getInt("CPAR"));
          setPPO.setString("OIB", oib);
          setPPO.setTimestamp("DATUMOD", ut.getFirstDayOfMonth(upitP.getTimestamp("DATPRI")));
          setPPO.setTimestamp("DATUMDO", ut.getLastDayOfMonth(upitP.getTimestamp("DATPRI")));
          setPPO.setBigDecimal("VRI", Aus.zero2);
          setPPO.setBigDecimal("POR", Aus.zero2);
        } else {
          System.err.println("!!!!! nisam pronašao partnera "+upitP.getInt("CPAR"));
          continue;
        }
      }
      setPPO.setBigDecimal(col, setPPO.getBigDecimal(col).add(upitP.getBigDecimal("VAL")));
      setPPO.post();
    }
  }
  private String getQryCommon() {
    return getQryCommon("");
  }
  private String getQryCommon(String extsql) {
    return "SELECT skstavke.cpar, (uistavke.ID+uistavke.IP) as val " + ((extsql==null)?"":extsql) +
        "FROM skstavke INNER JOIN uistavke ON uistavke.knjig = skstavke.knjig AND uistavke.cpar = skstavke.cpar " +
        "AND uistavke.vrdok = skstavke.vrdok AND uistavke.brojdok = skstavke.brojdok AND uistavke.cknjige = skstavke.cknjige " +
        "WHERE skstavke.knjig='"+dlgGetKnjig.getKNJCORG()+"' AND "+
        Condition.between("DATPRI", frmPDV2.getInstance().getDatumOd(), frmPDV2.getInstance().getDatumDo()).qualified("skstavke")+
        " AND ";
  }
  
  private String getPPOQryCommon() {
    return "SELECT skstavke.cpar, (uistavke.ID+uistavke.IP) as val, skstavke.datpri " +
        "FROM skstavke INNER JOIN uistavke ON uistavke.knjig = skstavke.knjig AND uistavke.cpar = skstavke.cpar " +
        "AND uistavke.vrdok = skstavke.vrdok AND uistavke.brojdok = skstavke.brojdok AND uistavke.cknjige = skstavke.cknjige " +
        "WHERE skstavke.knjig='"+dlgGetKnjig.getKNJCORG()+"' AND "+ 
        Aus.getCurrGKDatumCond("DATUMKNJ", "DATPRI", frmPDV2.getInstance().getDatumDo()).
        and(Condition.from("DATPRI", frmPDV2.getInstance().getDatumOd())).qualified("skstavke")+
        " AND ";
  }

  
  StorageDataSet setPDVS;
  public StorageDataSet getSetPDVS() {
    return setPDVS;
  }

  private void doPDV_S() {
    setPDVS = new StorageDataSet();
    setPDVS.addColumn(dM.createIntColumn("RBR"));
    setPDVS.addColumn(dM.createIntColumn("CPAR", "Partner"));
    setPDVS.addColumn(dM.createStringColumn("KODDRZ", "Kod države", 2));
    setPDVS.addColumn(dM.createStringColumn("PDVID", "Porezni broj", 30));
    setPDVS.addColumn(dM.createBigDecimalColumn("I1","Vrijednost dobara"));
    setPDVS.addColumn(dM.createBigDecimalColumn("I2","Vrijednost usluga"));
    setPDVS.open();
    String qryPDVScommon = getQryCommon();

    fillsetP("PDVSCIZroba", setPDVS, qryPDVScommon, "I1");
    fillsetP("PDVSCIZusl", setPDVS, qryPDVScommon, "I2");

    setPDVS.setTableName("setPDVS");
    setDataSetAndSums(setPDVS, new String[] {"I1","I2"});
    getJPTV().addTableModifier(
        new raTableColumnModifier("CPAR", new String[] {"CPAR", "NAZPAR"}, new String[] {"CPAR"}, new String[] {"CPAR"}, dM.getDataModule().getPartneri()));
    killAllReports();
    addReport("hr.restart.sk.repPDVSDisk", "Datoteka PDV-S za e-poreznu");
    setTitle("Obrazac PDV-S za period "+raDateUtil.getraDateUtil().dataFormatter(getDatumOd())+" - "+raDateUtil.getraDateUtil().dataFormatter(getDatumDo()));
//    getJPTV().getColumnsBean().setSaveName("frmPDV2-PDV-S");
  }
  
  private void fillsetP(String param, StorageDataSet setP, String qrycommon, String col) {
    String orovi = getOrovi(param);
    if (orovi == null) return;
    QueryDataSet upitP = Aus.q(qrycommon + orovi);
    dm.getPartneri().open();
    int rbr = 0;
    if (setP.rowCount() > 0) rbr = setP.getInt("RBR");
    for (upitP.first(); upitP.inBounds(); upitP.next()) {
      if (!lookupData.getlookupData().raLocate(setP, "CPAR", upitP.getInt("CPAR")+"")) {
        if (lookupData.getlookupData().raLocate(dm.getPartneri(), "CPAR", upitP.getInt("CPAR")+"")) {
          String oib =  dm.getPartneri().getString("OIB");
          setP.insertRow(false);
          setP.setInt("RBR", ++rbr);
          setP.setInt("CPAR", upitP.getInt("CPAR"));
          setP.setString("KODDRZ", oib.substring(0, 2));
          setP.setString("PDVID", oib.substring(2));
          setP.setBigDecimal(col, Aus.zero2);
        } else {
          System.err.println("!!!!! nisam pronašao partnera "+upitP.getInt("CPAR"));
          continue;
        }
      }
      setP.setBigDecimal(col, setP.getBigDecimal(col).add(upitP.getBigDecimal("VAL")));
      setP.post();
    }
  }
  private String getOrovi(String param) {
    //init
    frmParam.getParam("sk", "PDVSCIZroba","Pdv305o,Pdv306o,Pdv307o","Oznake definicija PDV-a iz kojih se prenosi vrijednost dobara u PDVS");
    frmParam.getParam("sk", "PDVSCIZusl","Pdv308o,Pdv309o,Pdv310o","Oznake definicija PDV-a iz kojih se prenosi vrijednost usluga u PDVS");
    frmParam.getParam("sk", "ZPCIZdob","Pdv103","Oznake definicija PDV-a iz kojih se prenosi vrijednost dobara u ZP");
    frmParam.getParam("sk", "ZPCIZdob42","Pdv106","Oznake definicija PDV-a iz kojih se prenosi vrijednost dobara 42 i 63 u ZP");
    frmParam.getParam("sk", "ZPCIZdobTro","Pdv107","Oznake definicija PDV-a iz kojih se prenosi vrijednost dobara tro.pos. u ZP");
    frmParam.getParam("sk", "ZPCIZusl","Pdv104","Oznake definicija PDV-a iz kojih se prenosi vrijednost usluga u ZP");
    frmParam.getParam("sk", "PPOCIZvri","Pdv204o","Oznake definicija PDV-a iz kojih se prenosi osnovica u PPO");
    frmParam.getParam("sk", "PPOCIZpor","Pdv204p","Oznake definicija PDV-a iz kojih se prenosi porez u PPO");
    //
    VarStr cond= new VarStr("STIZVJPDV.CIZ in (");
    StringTokenizer cizovi = new StringTokenizer(frmParam.getParam("sk", param), ",");
    while (cizovi.hasMoreTokens()) {
      cond.append("'"+cizovi.nextToken()+"',");
    }
    cond.chop().append(")");
    QueryDataSet stizvj = StIzvjPDV.getDataModule().getTempSet(cond.toString());
System.out.println("stizvjqry :: " +stizvj.getQuery().getQueryString());
    stizvj.open();
    if (stizvj.getRowCount() == 0) return null;
    VarStr orovi = new VarStr("(");//uistavke.cknjige, uistavke.ckolone, uistavke.uraira
    for (stizvj.first(); stizvj.inBounds(); stizvj.next()) {
      orovi.append(
          "(uistavke.cknjige='"+stizvj.getString("CKNJIGE")+
          "' AND uistavke.ckolone="+stizvj.getShort("CKOLONE")+
          " AND uistavke.uraira='"+stizvj.getString("URAIRA")+"') OR ");
    }
    orovi.chop(4).append(")");
    return orovi.toString();
  }
  
  public void fillsetPDV_K(StorageDataSet mset) {
    izvjpdv_k.open();
    for (izvjpdv_k.first(); izvjpdv_k.inBounds(); izvjpdv_k.next()) {
      mset.insertRow(false);
      mset.setString("POZ", izvjpdv_k.getString("CIZ"));
      mset.setBigDecimal("OSN", Aus.zero2);
      mset.setBigDecimal("PDV", Aus.zero2);
      mset.post();
    }
  }
  public void fillsetPDV(StorageDataSet mset) {
    izvjpdv.open();
    //init
    for (izvjpdv.first(); izvjpdv.inBounds(); izvjpdv.next()) {
      String ciz = izvjpdv.getString("CIZ");
      String poz;
      boolean p = ciz.trim().endsWith("p");
      if (p) {
        poz = new VarStr(ciz).replaceLast("p", "o").toString();
      } else {
        poz = ciz;
      }
      if (!lookupData.getlookupData().raLocate(mset, "POZ", poz)) {
        mset.insertRow(false);
        mset.setString("POZ", poz);
        mset.setBigDecimal("OSN", Aus.zero2);
        mset.setBigDecimal("PDV", Aus.zero2);
        mset.post();
      }
    }
    //make filters
    HashMap filters = new HashMap();
 
    QueryDataSet stizvj = StIzvjPDV.getDataModule().getTempSet("CIZ like '" + _izvjpdv_ciz_prefix + "%'");
    stizvj.open();
    for (stizvj.first(); stizvj.inBounds(); stizvj.next()) {
      HashSet filt = (HashSet)filters.get(stizvj.getString("CIZ"));
      if (filt == null) filt = new HashSet();
      filt.add(new colFilter0107(stizvj));
      filters.put(stizvj.getString("CIZ"), filt);
    }
    QueryDataSet stizvj3006 = Aus.q("SELECT s.ciz, s.cknjige, s.ckolone, s.uraira, i.parametri FROM StIzvjPDV s, IzvjPDV i WHERE s.ciz = i.ciz and s.CIZ like 'I%' and i.PARAMETRI like '" + _izvjpdv_ciz_prefix + "%'");
        //StIzvjPDV.getDataModule().getTempSet("CIZ like 'I%' and PARAMETRI like 'Pod%'");
    stizvj3006.open();
    for (stizvj3006.first(); stizvj3006.inBounds(); stizvj3006.next()) {
      HashSet filt = (HashSet)filters.get(stizvj3006.getString("PARAMETRI").trim());
      if (filt == null) filt = new HashSet();
      filt.add(new colFilter3006(stizvj3006));
      filters.put(stizvj3006.getString("PARAMETRI").trim(), filt);
    }
    
    //query data
    
    // ova verzija je negdje 20 puta brža u gali... 
    
    String kverchina = "SELECT uistavke.cknjige, uistavke.ckolone, uistavke.uraira, cast(uistavke.ID+uistavke.IP as numeric(17,2)) as val, skstavke.datpri as datpri " +
            "FROM skstavke INNER JOIN uistavke ON uistavke.knjig = skstavke.knjig AND uistavke.cpar = skstavke.cpar " +
            "AND uistavke.vrdok = skstavke.vrdok AND uistavke.brojdok = skstavke.brojdok AND uistavke.cknjige = skstavke.cknjige " +
            "WHERE skstavke.knjig='"+dlgGetKnjig.getKNJCORG()+"' AND "+
            Condition.between("DATPRI", frmPDV2.getInstance().getDatumOd(), frmPDV2.getInstance().getDatumDo()).qualified("skstavke");
    
    
    /*String kverchina = "SELECT cknjige, ckolone, uraira, (ID+IP) as val FROM uistavke " +
        "WHERE EXISTS (SELECT * FROM SKSTAVKE where knjig='"+dlgGetKnjig.getKNJCORG()+"' AND "+Condition.between("DATPRI", frmPDV.getInstance().getDatumOd(), frmPDV.getInstance().getDatumDo())+
        " AND uistavke.knjig = skstavke.knjig AND uistavke.cpar = skstavke.cpar AND uistavke.vrdok = skstavke.vrdok AND uistavke.brojdok = skstavke.brojdok)";
    System.err.println("kverchina :: "+kverchina);*/
    
    QueryDataSet stui = Aus.q(kverchina);
    for (stui.first(); stui.inBounds(); stui.next()) {
      for (mset.first(); mset.inBounds(); mset.next()) {
        String ciz = mset.getString("POZ"); //pazi ako ima o, ima i p

        sumFiltered(mset, ciz, stui, "OSN", filters);
        if (ciz.endsWith("o")) {
          sumFiltered(mset, new VarStr(ciz).replaceLast("o", "p").toString(), stui, "PDV", filters);
        }
        mset.post();
      }
    }
  }
  
  private void sumFiltered(StorageDataSet mset, String ciz, ReadRow stui, String column, HashMap filters) {
    HashSet filt = (HashSet)filters.get(ciz);
    if (filt == null) return;
    BigDecimal bd = mset.getBigDecimal(column);
    for (Iterator iterator2 = filt.iterator(); iterator2.hasNext();) {
      colFilter cF = (colFilter) iterator2.next();
      if (cF.pass(stui)) bd = bd.add(stui.getBigDecimal("VAL"));
    }
    mset.setBigDecimal(column, bd);
  }
  
  private void recalcPOR(StorageDataSet mset, StorageDataSet izvj) {
    int perc = 0;
    for (izvj.first(); izvj.inBounds(); izvj.next()) 
      if (izvj.getString("CIZ").trim().toLowerCase().endsWith("o")) {
        String[] tok = new VarStr(izvj.getString("OPIS")).split();
        for (int i = 0; i < tok.length; i++) {
          if (tok[i].equals("25%")) perc = 25;
          else if (tok[i].equals("13%")) perc = 13;
          else if (tok[i].equals("5%")) perc = 5;
          else continue;
          
          if (lookupData.getlookupData().raLocate(mset, "POZ", izvj.getString("CIZ")))
            mset.setBigDecimal("OSN", mset.getBigDecimal("PDV").
                divide(BigDecimal.valueOf(perc).movePointLeft(2), 2, BigDecimal.ROUND_HALF_UP));
        }
      }
  }
  
  private void recalcPDVSet(StorageDataSet mset, StorageDataSet izvj) {
    izvj.open();
    //StringTokenizer sums = new StringTokenizer("101+203+404+505", "+", true);
    for (izvj.first(); izvj.inBounds(); izvj.next()) {
      if (!"".equals(izvj.getString("PARAMETRI").trim()) && !izvj.getString("CIZ").toLowerCase().trim().endsWith("p")) {
        String param = izvj.getString("PARAMETRI").trim();
        boolean samopdv = false;
        boolean samoosn = false;
        if (param.startsWith("p")) {
          samopdv = true;
          param = param.substring(1);
        } else if (param.startsWith("o")) {
          samoosn = true;
          param = param.substring(1);
        }
        BigDecimal osn = Aus.zero2;
        BigDecimal pdv = Aus.zero2;
        StringTokenizer sums = new StringTokenizer(param,"+-",true);
        String predznak = "+";
        while (sums.hasMoreTokens()) {
          String tok = sums.nextToken().trim();
          if (tok.equals("+") || tok.equals("-")) {
            predznak = tok;
          } else {
            String poz = _izvjpdv_ciz_prefix+tok;
            boolean located = lookupData.getlookupData().raLocate(mset, "POZ", poz);
            if (!located) {
              located = lookupData.getlookupData().raLocate(mset, "POZ", poz+"o");
            }
            if (!located) {
              poz = "Pok"+tok;//pdv-k
              located = lookupData.getlookupData().raLocate(mset, "POZ", poz);
            }
            if (located) {
              if (predznak.equals("-")) {
                if (!samopdv) osn = osn.subtract(mset.getBigDecimal("OSN"));
                if (!samoosn) pdv = pdv.subtract(mset.getBigDecimal("PDV"));
              } else {
                if (!samopdv) osn = osn.add(mset.getBigDecimal("OSN"));
                if (!samoosn) pdv = pdv.add(mset.getBigDecimal("PDV"));
              }
            } else {
              System.err.println(poz + " ili "+poz+"o nije pronadjen");
            }
          }
        }//tokens
        if (lookupData.getlookupData().raLocate(mset, "POZ", izvj.getString("CIZ"))) {
          mset.setBigDecimal("OSN", osn);
          mset.setBigDecimal("PDV", pdv);
          mset.post();
        } else {
          System.err.println("Ne mogu naci "+izvj.getString("CIZ")+" za snimanje sume");
        }
      }
    }
  }
  
  StorageDataSet setPDV_K = null;
  public StorageDataSet getSetPDV_K() {
    return setPDV_K;
  }
  private void doPDV_K() {
    _izvjpdv_ciz_prefix = "Pod";
    izvjpdv = IzvjPDV.getDataModule().getTempSet("CIZ like 'Pod%'");
    setPDV_K = new StorageDataSet();
    setPDV_K.addColumn(dM.createStringColumn("POZ","Pozicija",200));
    setPDV_K.addColumn(dM.createBigDecimalColumn("OSN", "Osnovica"));
    setPDV_K.addColumn(dM.createBigDecimalColumn("PDV", "PDV"));
    setPDV_K.open();
    fillsetPDV(setPDV_K);
    fillsetPDV_K(setPDV_K);
    recalcPDVSet(setPDV_K, izvjpdv_k_all);
    recalcPDVSet(setPDV_K, izvjpdv_k_all);
    setPDV_K.setSort(new SortDescriptor(new String[] {"POZ"}));
    setPDV_K.setTableName("setPDV_K");
    setDataSet(setPDV_K);
    getJPTV().removeAllTableModifiers();
    izvjpdv_k_all.open();
    getJPTV().addTableModifier(
        new raTableColumnModifier("POZ", new String[] {"CIZ", "OPIS"}, new String[] {"POZ"}, new String[] {"CIZ"}, izvjpdv_k_all) {
          public String formatShared(Variant sh, String colname) {
            if ("CIZ".equalsIgnoreCase(colname)) {
              char[] orig = sh.toString().toCharArray();
              StringBuffer ret = new StringBuffer();
              for (int i = 0; i < orig.length; i++) {
                if (Character.isDigit(orig[i])) ret.append(orig[i]);
              }
              return ret.toString();
            } else {
              return shared.toString();
            }
          }
    });
    killAllReports();
    addReport("hr.restart.sk.repPDVKDisk", "Datoteka PDV-K za e-poreznu");
    setTitle("Obrazac PDV-K za period "+raDateUtil.getraDateUtil().dataFormatter(getDatumOd())+" - "+raDateUtil.getraDateUtil().dataFormatter(getDatumDo()));

  }
  
  StorageDataSet setPDV = null;
  public StorageDataSet getSetPDV() {
    return setPDV;
  }
  
  private void doPDV() {
    try {
      _izvjpdv_ciz_prefix = "Pdv";
      izvjpdv = IzvjPDV.getDataModule().getTempSet("CIZ like 'Pdv%'");
      doPDVcommon();
      addReport("hr.restart.sk.repPDVDisk15", "Datoteka PDV za e-poreznu");
      addReport("hr.restart.sk.repPDVDisk", "Datoteka PDV za e-poreznu (do 2014)");
      setTitle("Obrazac PDV za period "+raDateUtil.getraDateUtil().dataFormatter(getDatumOd())+" - "+raDateUtil.getraDateUtil().dataFormatter(getDatumDo()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private void doPDV13() {
    try {
      _izvjpdv_ciz_prefix = "Pod";
      izvjpdv = IzvjPDV.getDataModule().getTempSet("CIZ like 'Pod%'");
      doPDVcommon();
      addReport("hr.restart.sk.repPDVDisk13", "Datoteka PDV 2013 za e-poreznu");
      setTitle("Obrazac PDV (2013) za period "+raDateUtil.getraDateUtil().dataFormatter(getDatumOd())+" - "+raDateUtil.getraDateUtil().dataFormatter(getDatumDo()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private void doPDVcommon() {
    setPDV = new StorageDataSet();
    setPDV.addColumn(dM.createStringColumn("POZ","Pozicija",200));
    setPDV.addColumn(dM.createBigDecimalColumn("OSN", "Osnovica"));
    setPDV.addColumn(dM.createBigDecimalColumn("PDV", "PDV"));
    setPDV.open();
    //
    fillsetPDV(setPDV);
    if (frmParam.getParam("sk", "PDVcalcPor","D","Izraèunati osnovicu iz poreza na PDV obrascu (D,N)?").equals("D"))
      recalcPOR(setPDV, izvjpdv);
    recalcPDVSet(setPDV, izvjpdv);
    recalcPDVSet(setPDV, izvjpdv);
    //
    setPDV.setSort(new SortDescriptor(new String[] {"POZ"}));
    setPDV.setTableName("setPDV");
    setDataSet(setPDV);
    getJPTV().removeAllTableModifiers();
    getJPTV().addTableModifier(
        new raTableColumnModifier("POZ", new String[] {"CIZ", "OPIS"}, new String[] {"POZ"}, new String[] {"CIZ"}, izvjpdv) {
          public String formatShared(Variant sh, String colname) {
            if ("CIZ".equalsIgnoreCase(colname)) {
              char[] orig = sh.toString().toCharArray();
              StringBuffer ret = new StringBuffer();
              for (int i = 0; i < orig.length; i++) {
                if (Character.isDigit(orig[i])) ret.append(orig[i]);
              }
              return ret.toString();
            } else {
              return shared.toString();
            }
          }
    });
    killAllReports();
  }
  
  private static frmPDV2 _this;
  public static frmPDV2 getInstance() {
    if (_this == null) {
      startFrame.getStartFrame().showFrame("hr.restart.sk.frmPDV2",0, "Obrasci za poreznu upravu", false);
    }
    return _this;
  }
  public java.sql.Timestamp getDatumOd(){
    return stds.getTimestamp("DATUMOD");
  }

  public java.sql.Timestamp getDatumDo(){
    return stds.getTimestamp("DATUMDO");
  }
  
  public java.sql.Timestamp getNextMonth(){
    return ut.getLastDayOfMonth(ut.addMonths(ut.getFirstDayOfMonth(stds.getTimestamp("DATUMDO")), 1));
  }

  public String navDoubleClickActionName() {
    return "Izmjena";
  }

  public int[] navVisibleColumns() {
    // TODO Auto-generated method stub
    return null;
  }
  
  private interface colFilter {
    public boolean pass(ReadRow uis);
  }
  private class colFilterImpl implements colFilter {
    private colFilterImpl(ReadRow set) {
      ciz = set.getString("CIZ");
      cknjige = set.getString("CKNJIGE");
      ckolone = set.getShort("CKOLONE");
      uraira = set.getString("URAIRA");
    }
    public boolean pass(ReadRow uis) {
      return (
          uis.getString("URAIRA").equals(uraira) &&
          uis.getString("CKNJIGE").equals(cknjige) &&
          uis.getShort("CKOLONE")== ckolone
          );
    }
    String ciz;
    String cknjige;
    short ckolone;
    String uraira;
  }
  private class colFilter0107 extends colFilterImpl {
    public colFilter0107(QueryDataSet set) {
      super(set);
    }
    Timestamp d0107 = new Timestamp(1372629600000l);//2013-07-01 00:00:00.0000
    public boolean pass(ReadRow uis) {
//      System.err.println("************** colFilter0107 ***********");
      if (uis.getTimestamp("DATPRI").before(d0107)) return false;
      return super.pass(uis);
    }
  }
  private class colFilter3006 extends colFilterImpl {
    public colFilter3006(QueryDataSet set) {
      super(set);
    }
    Timestamp d3006 = new Timestamp(1372629599999l);//2013-06-30 23:59:59.999
    public boolean pass(ReadRow uis) {
//      System.err.println("************** colFilter3006 ***********");
      if (uis.getTimestamp("DATPRI").after(d3006)) return false;
      return super.pass(uis);
    }
  }
  
  public void jptv_doubleClick() {
    int pick = jraObrazac.getSelectedIndex();
    System.err.println("***** PICK:"+pick);
    switch (pick) {
    case 0:
      updPDV();
      break;
      
    case 1:
      updPDV_S();
      break;
      
    case 2:
      updZP();
      break;
      
    case 3:
      updPDV_K();
      break;

    case 4:
      updJOPPD();
      break;
      
    case 5:
      updJOPPDPN();
      break;
      
/*    case 6:
      updPDV();
      break;
*/            
    case 6:
      updPPO();
      break;
      
    case 7:
      updOPZ();
      break;
            
    default:
      break;
    }
  }

  private void updJOPPD() {
    JOPPD.getJOPPD_BDialog().show();
  }
  
  private void updJOPPDPN() {
    JOPPDPN.getJOPPD_BDialog().show();
  }


  private void updPDV_K() {
    updPDV(setPDV_K);
  }
  
  private void updOPZ() {
    
    XYPanel pan = new XYPanel(setOPZ).configWid(0, 50, 120, 120, 245, 350).configPos(15, 200);
    
    pan.label("Partner").nav("OIB NAZPAR", dm.getPartneri()).nl();
    pan.label("Broj dokumenta").text("BROJDOK").skip(150).label("Vrsta poreznog broja").skip(150).text("TIP").nl();
    pan.label("Datum / valuta").text("DATDOK").text("DATDOSP").nl();
    pan.label("Iznos / saldo").text("SSALDO").text("SALDO").nl();
    pan.label("Iznos poreza").text("PDV").nl().expand();
    pan.getNav("NAZPAR").setDataSet(setOPZ);
    pan.getNav("OIB").setSearchMode(2);
    pan.getNav("OIB").setFocusLostOnShow(false);
    pan.getNav("NAZPAR").setFocusLostOnShow(false);
    
    DataRow old = new DataRow(setOPZ);
    dM.copyColumns(setOPZ, old);
    
    raInputDialog upd = new raInputDialog();
    if (!upd.show(getWindow(), pan, "Izmjena stavke OPZ")) {
      dM.copyColumns(old, setOPZ);
    } else getJPTV().fireTableDataChanged();
  }

  private JraDialog dlgPPO;
  private void updPPO() {
    dlgPPO = JOPPDhndlr.getDialogForSet(setPPO, false, 
    new Runnable() {
      public void run() {
        setPPO.post();
        frmPDV2.this.getJPTV().fireTableDataChanged();
        dlgPPO.dispose();
      }
    },
    new Runnable() {
      public void run() {
        setPPO.cancel();
        dlgPPO.dispose();
      }
    });
    dlgPPO.show();
  }
  
  private void updZP() {
    final JraDialog jdZP = new JraDialog((JFrame)getWindow());
    JPanel jpZP = new JPanel(new BorderLayout()); 
    JPanel jpcont = new JPanel();
    OKpanel okpan = new OKpanel() {
      
      public void jPrekid_actionPerformed() {
        setZP.cancel();
        jdZP.dispose();
      }
      
      public void jBOK_actionPerformed() {
        setZP.post();
        frmPDV2.this.getJPTV().fireTableDataChanged();
        jdZP.dispose();
      }
    };
    XYLayout xyl = new XYLayout(420, 205);
    lookupData.getlookupData().raLocate(dM.getDataModule().getPartneri(), "CPAR", setZP.getInt("CPAR")+"");
    String capt = dM.getDataModule().getPartneri().getString("OIB")+" - "+dM.getDataModule().getPartneri().getString("NAZPAR");
    JLabel jlCaption = new JLabel(capt);
    JLabel jlKODDRZ = new JLabel("Kod države");
    JLabel jlPDVID = new JLabel("Porezni broj");
    JLabel jlI1 = new JLabel("Vrijednost isporuke dobara");
    JLabel jlI2 = new JLabel("Vrijednost isporuke dobara 42 i 63");
    JLabel jlI3 = new JLabel("Vrijednost isporuke dobara trostrani posao");
    JLabel jlI4 = new JLabel("Vrijednost isporuke obavljenih usluga");
    JraTextField jrKODDRZ = new JraTextField();
    jrKODDRZ.setDataSet(setZP);
    jrKODDRZ.setColumnName("KODDRZ");

    JraTextField jrPDVID = new JraTextField();
    jrPDVID.setDataSet(setZP);
    jrPDVID.setColumnName("PDVID");
    
    JraTextField jrI1 = new JraTextField();
    jrI1.setDataSet(setZP);
    jrI1.setColumnName("I1");

    JraTextField jrI2 = new JraTextField();
    jrI2.setDataSet(setZP);
    jrI2.setColumnName("I2");

    JraTextField jrI3 = new JraTextField();
    jrI3.setDataSet(setZP);
    jrI3.setColumnName("I3");

    JraTextField jrI4 = new JraTextField();
    jrI4.setDataSet(setZP);
    jrI4.setColumnName("I4");

    jpcont.setLayout(xyl);
    jpcont.add(jlCaption, new XYConstraints(15,15,-1,-1));
    jpcont.add(jlKODDRZ, new XYConstraints(15,40,-1,-1));
    jpcont.add(jrKODDRZ, new XYConstraints(200,40,100,-1));
    jpcont.add(jlPDVID, new XYConstraints(15,65,-1,-1));
    jpcont.add(jrPDVID, new XYConstraints(200,65,200,-1));
    jpcont.add(jlI1, new XYConstraints(15,90,-1,-1));
    jpcont.add(jrI1, new XYConstraints(300,90,100,-1));
    jpcont.add(jlI2, new XYConstraints(15,115,-1,-1));
    jpcont.add(jrI2, new XYConstraints(300,115,100,-1));
    jpcont.add(jlI3, new XYConstraints(15,140,-1,-1));
    jpcont.add(jrI3, new XYConstraints(300,140,100,-1));
    jpcont.add(jlI4, new XYConstraints(15,165,-1,-1));
    jpcont.add(jrI4, new XYConstraints(300,165,100,-1));
    jdZP.getContentPane().setLayout(new BorderLayout());
    jdZP.getContentPane().add(jpcont,BorderLayout.CENTER);
    jdZP.getContentPane().add(okpan, BorderLayout.SOUTH);
//    jdZP.pack();
    startFrame.getStartFrame().centerFrame(jdZP, 0, "Izmjena stavke ZP");
    jdZP.show();
  }

  private void updPDV_S() {
    final JraDialog jdPDVS = new JraDialog((JFrame)getWindow());
    JPanel jpPDVS = new JPanel(new BorderLayout()); 
    JPanel jpcont = new JPanel();
    OKpanel okpan = new OKpanel() {
      
      public void jPrekid_actionPerformed() {
        setPDVS.cancel();
        jdPDVS.dispose();
      }
      
      public void jBOK_actionPerformed() {
        setPDVS.post();
        frmPDV2.this.getJPTV().fireTableDataChanged();
        jdPDVS.dispose();
      }
    };
    XYLayout xyl = new XYLayout(420, 150);
    lookupData.getlookupData().raLocate(dM.getDataModule().getPartneri(), "CPAR", setPDVS.getInt("CPAR")+"");
    String capt = dM.getDataModule().getPartneri().getString("OIB")+" - "+dM.getDataModule().getPartneri().getString("NAZPAR");
    JLabel jlCaption = new JLabel(capt);
    JLabel jlKODDRZ = new JLabel("Kod države");
    JLabel jlPDVID = new JLabel("Porezni broj");
    JLabel jlI1 = new JLabel("Vrijednost steèenih dobara");
    JLabel jlI2 = new JLabel("Vrijednost primljenih usluga");
    JraTextField jrKODDRZ = new JraTextField();
    jrKODDRZ.setDataSet(setPDVS);
    jrKODDRZ.setColumnName("KODDRZ");

    JraTextField jrPDVID = new JraTextField();
    jrPDVID.setDataSet(setPDVS);
    jrPDVID.setColumnName("PDVID");
    
    JraTextField jrI1 = new JraTextField();
    jrI1.setDataSet(setPDVS);
    jrI1.setColumnName("I1");

    JraTextField jrI2 = new JraTextField();
    jrI2.setDataSet(setPDVS);
    jrI2.setColumnName("I2");

    jpcont.setLayout(xyl);
    jpcont.add(jlCaption, new XYConstraints(15,15,-1,-1));
    jpcont.add(jlKODDRZ, new XYConstraints(15,40,-1,-1));
    jpcont.add(jrKODDRZ, new XYConstraints(200,40,100,-1));
    jpcont.add(jlPDVID, new XYConstraints(15,65,-1,-1));
    jpcont.add(jrPDVID, new XYConstraints(200,65,200,-1));
    jpcont.add(jlI1, new XYConstraints(15,90,-1,-1));
    jpcont.add(jrI1, new XYConstraints(300,90,100,-1));
    jpcont.add(jlI2, new XYConstraints(15,115,-1,-1));
    jpcont.add(jrI2, new XYConstraints(300,115,100,-1));
    jdPDVS.getContentPane().setLayout(new BorderLayout());
    jdPDVS.getContentPane().add(jpcont,BorderLayout.CENTER);
    jdPDVS.getContentPane().add(okpan, BorderLayout.SOUTH);
    startFrame.getStartFrame().centerFrame(jdPDVS, 0, "Izmjena stavke PDV-S");
    jdPDVS.show();    
  }

  private void updPDV() {
    updPDV(setPDV);
  }
  
 
  
  private void updPDV(final StorageDataSet set_p) {
    izvjpdv_k_all.open();
    final JraDialog jdPDV = new JraDialog((JFrame)getWindow());
    JPanel jpPDV = new JPanel(new BorderLayout()); 
    JPanel jpcont = new JPanel();
    OKpanel okpan = new OKpanel() {
      
      public void jPrekid_actionPerformed() {
        set_p.cancel();
        jdPDV.dispose();
      }
      
      public void jBOK_actionPerformed() {
        set_p.post();
        int pos = set_p.getRow();
        getJPTV().enableEvents(false);
        recalcPDVSet(set_p, izvjpdv_k_all);
        recalcPDVSet(set_p, izvjpdv_k_all);
//        frmPDV2.this.getJPTV().fireTableDataChanged();
        set_p.goToRow(pos);
        getJPTV().enableEvents(true);
        jdPDV.dispose();
      }
    };
    XYLayout xyl = new XYLayout(270, 150);
    lookupData.getlookupData().raLocate(izvjpdv_k_all, "CIZ", set_p.getString("POZ"));
    String capt = izvjpdv.getString("OPIS");
    JTextArea jta = new JTextArea(capt);
    jta.setLineWrap(true);
    jta.setWrapStyleWord(true);
    jta.setEditable(false);
    jta.setOpaque(false);
    
    JLabel jlOSN = new JLabel("Porezna osnovica");
    JLabel jlPDV = new JLabel("PDV");
    
    JraTextField jrOSN = new JraTextField();
    jrOSN.setDataSet(set_p);
    jrOSN.setColumnName("OSN");

    JraTextField jrPDV = new JraTextField();
    jrPDV.setDataSet(set_p);
    jrPDV.setColumnName("PDV");

    jpcont.setLayout(xyl);
    jpcont.add(jta, new XYConstraints(15,15,250,85));
    jpcont.add(jlOSN, new XYConstraints(15,90,-1,-1));
    jpcont.add(jrOSN, new XYConstraints(150,90,100,-1));
    jpcont.add(jlPDV, new XYConstraints(15,115,-1,-1));
    jpcont.add(jrPDV, new XYConstraints(150,115,100,-1));
    jdPDV.getContentPane().setLayout(new BorderLayout());
    jdPDV.getContentPane().add(jpcont,BorderLayout.CENTER);
    jdPDV.getContentPane().add(okpan, BorderLayout.SOUTH);
    startFrame.getStartFrame().centerFrame(jdPDV, 0, "Izmjena stavke PDV");
    jdPDV.show();        
  }
  

}