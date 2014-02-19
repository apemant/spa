package hr.restart.sk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableColumn;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.ReadWriteRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;
import com.elixirtech.a.d;

import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacpdv.v7_0.SRazdoblje;
import hr.restart.baza.Condition;
import hr.restart.baza.IzvjPDV;
import hr.restart.baza.JoppdA;
import hr.restart.baza.JoppdB;
import hr.restart.baza.Kumulorgarh;
import hr.restart.baza.Opcine;
import hr.restart.baza.Orgpl;
import hr.restart.baza.Radplsifre;
import hr.restart.baza.Sifrarnici;
import hr.restart.baza.StIzvjPDV;
import hr.restart.baza.Vrsteprim;
import hr.restart.baza.dM;
import hr.restart.db.raVariant;
import hr.restart.pl.plUtil;
import hr.restart.pl.raIniciranje;
import hr.restart.pl.raIzvjestaji;
import hr.restart.pl.raOdbici;
import hr.restart.pl.raParam;
import hr.restart.pl.raPlObrRange;
import hr.restart.robno.dlgKupac;
import hr.restart.robno.raDateUtil;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCustomAttrib;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.OKpanel;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.lookupFrame;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raLookUpDialog;
import hr.restart.util.raNavAction;
import hr.restart.util.raTransaction;
import hr.restart.util.raUpit;
import hr.restart.util.raUpitFat;
import hr.restart.util.startFrame;
import hr.restart.util.sysoutTEST;
import hr.restart.util.VarStr;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.dlgGetKnjig;

public class frmPDV2 extends raUpitFat {
  
  JPanel datePanel;
  JLabel jlPer = new JLabel("Razdoblje");
  JraTextField jraPoctDat = new JraTextField();
  JraTextField jraKrajDat = new JraTextField();
  JraButton jbJOPPD_A = new JraButton();
  JraComboBox jraObrazac = new JraComboBox(new String[] {"Obrazac PDV","Obrazac PDV-S","Obrazac ZP","Obrazac PDV-K", "Obrazac JOPPD", "Obrazac PDV za 2013"});
  XYLayout xYlay = new XYLayout();
  StorageDataSet stds = new StorageDataSet();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  Util ut = Util.getUtil();
  Valid vl = Valid.getValid();
  QueryDataSet izvjpdv = null;//IzvjPDV.getDataModule().getFilteredDataSet("CIZ like 'Pdv%'");
  private String _izvjpdv_ciz_prefix = "Pdv";
  QueryDataSet izvjpdv_k = IzvjPDV.getDataModule().getFilteredDataSet("CIZ like 'Pok%'");
  QueryDataSet izvjpdv_k_all = IzvjPDV.getDataModule().getFilteredDataSet("CIZ like 'Pdv%' or CIZ like 'Pod%' or CIZ like 'Pok%'");
  private String opcinarada;
  
  public frmPDV2() {
    try {
      datePanel = new JPanel();
      datePanel.setLayout(xYlay);
      xYlay.setWidth(700);
      xYlay.setHeight(50);
      stds.setColumns(new Column[] {
        dm.createTimestampColumn("DATUMOD"),
        dm.createTimestampColumn("DATUMDO")
      });

      jraPoctDat.setDataSet(stds);
      jraPoctDat.setColumnName("DATUMOD");
      jbJOPPD_A.setText("Dohvat");
//      jbJOPPD_A.setText("Strana A");
      jbJOPPD_A.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          jbJOPPD_A_action();
        }
      });
      jraKrajDat.setDataSet(stds);
      jraKrajDat.setColumnName("DATUMDO");
      datePanel.add(jlPer, new XYConstraints(15,15,-1,-1));
      datePanel.add(jraPoctDat, new XYConstraints(150, 15, 100, -1));
      datePanel.add(jraKrajDat, new XYConstraints(255, 15, 100, -1));
      datePanel.add(jraObrazac, new XYConstraints(360, 15, 150, -1));
      setJPan(datePanel);
      jraObrazac.addItemListener(new ItemListener() {
        

        public void itemStateChanged(ItemEvent e) {
          if (jraObrazac.getSelectedIndex() == 4) {
            datePanel.remove(jraKrajDat);
            datePanel.add(jbJOPPD_A, new XYConstraints(255, 15, 100, -1));
            jlPer.setText("Datum isplate");
            QueryDataSet orgpl = Orgpl.getDataModule().getTempSet(Condition.equal("CORG", OrgStr.getKNJCORG()));
            orgpl.open();
            orgpl.first();
            opcinarada = "0"+raIzvjestaji.convertCopcineToRS(orgpl.getString("COPCINE"));
            stds.setTimestamp("DATUMOD", orgpl.getTimestamp("DATUMISPL"));
            stds.post();
          } else {
            clearJop();
          }
          datePanel.validate();
          System.err.println("jraObrazac.itemStateChanged");
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
      _this = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
/*
 
 */
  protected void delete() {
    int pick = jraObrazac.getSelectedIndex();
    if (pick == 0 || pick == 3) return;
    if (JOptionPane.showConfirmDialog(null, "Jeste li sigurni da želite obrisati red?", "Pozor", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
      getJPTV().getStorageDataSet().deleteRow();
      getJPTV().fireTableDataChanged();
    }
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
      doJOPPD();
      break;
      
    case 5:
      doPDV13();
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
    return true;
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
    strBset = null;
    strAset = null;
    rs = null;
    jraObrazac.setSelectedIndex(0);
    jraPoctDat.requestFocusLater();
    System.gc();
  }
  private void clearJop() {
    jbJOPPD_A.setText("Dohvat");
    currOIB = null;
    jlPer.setText("Razdoblje");
    datePanel.remove(jbJOPPD_A);
    datePanel.add(jraKrajDat, new XYConstraints(255, 15, 100, -1));
    stds.setTimestamp("DATUMOD", ut.getFirstDayOfMonth(ut.addMonths(vl.getToday(), -1)));
    stds.setTimestamp("DATUMDO", ut.getLastDayOfMonth(ut.addMonths(vl.getToday(), -1)));
  }

  public void componentShow() {
    clearJop();
    jraObrazac.setSelectedIndex(0);
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
    
    setZP.setTableName("setZP");
    setDataSetAndSums(setZP, new String[] {"I1","I2","I3","I4"});
    getJPTV().addTableModifier(
        new raTableColumnModifier("CPAR", new String[] {"CPAR", "NAZPAR"}, new String[] {"CPAR"}, new String[] {"CPAR"}, dM.getDataModule().getAllPartneri()));
    killAllReports();
    addReport("hr.restart.sk.repZPDisk", "Datoteka ZP za e-poreznu");
    setTitle("Obrazac ZP za period "+raDateUtil.getraDateUtil().dataFormatter(getDatumOd())+" - "+raDateUtil.getraDateUtil().dataFormatter(getDatumDo()));

  }

  private String getQryCommon() {
    return "SELECT skstavke.cpar, (uistavke.ID+uistavke.IP) as val " +
        "FROM skstavke INNER JOIN uistavke ON uistavke.knjig = skstavke.knjig AND uistavke.cpar = skstavke.cpar " +
        "AND uistavke.vrdok = skstavke.vrdok AND uistavke.brojdok = skstavke.brojdok AND uistavke.cknjige = skstavke.cknjige " +
        "WHERE skstavke.knjig='"+dlgGetKnjig.getKNJCORG()+"' AND "+
        Condition.between("DATPRI", frmPDV2.getInstance().getDatumOd(), frmPDV2.getInstance().getDatumDo()).qualified("skstavke")+
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
        new raTableColumnModifier("CPAR", new String[] {"CPAR", "NAZPAR"}, new String[] {"CPAR"}, new String[] {"CPAR"}, dM.getDataModule().getAllPartneri()));
    killAllReports();
    addReport("hr.restart.sk.repPDVSDisk", "Datoteka PDV-S za e-poreznu");
    setTitle("Obrazac PDV-S za period "+raDateUtil.getraDateUtil().dataFormatter(getDatumOd())+" - "+raDateUtil.getraDateUtil().dataFormatter(getDatumDo()));
//    getJPTV().getColumnsBean().setSaveName("frmPDV2-PDV-S");
  }
  
  private void fillsetP(String param, StorageDataSet setP, String qrycommon, String col) {
    String orovi = getOrovi(param);
    if (orovi == null) return;
    QueryDataSet upitP = Aus.q(qrycommon + orovi);
    dm.getAllPartneri().open();
    int rbr = 0;
    for (upitP.first(); upitP.inBounds(); upitP.next()) {
      if (!lookupData.getlookupData().raLocate(setP, "CPAR", upitP.getInt("CPAR")+"")) {
        if (lookupData.getlookupData().raLocate(dm.getAllPartneri(), "CPAR", upitP.getInt("CPAR")+"")) {
          String oib =  dm.getAllPartneri().getString("OIB");
          setP.insertRow(false);
          rbr++;
          setP.setInt("RBR", rbr);
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
    
    // ova verzija je negdje 20 puta brža u gali... :)
    
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
    izvjpdv = IzvjPDV.getDataModule().getFilteredDataSet("CIZ like 'Pod%'");
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
      izvjpdv = IzvjPDV.getDataModule().getFilteredDataSet("CIZ like 'Pdv%'");
      doPDVcommon();
      addReport("hr.restart.sk.repPDVDisk", "Datoteka PDV za e-poreznu");
      setTitle("Obrazac PDV za period "+raDateUtil.getraDateUtil().dataFormatter(getDatumOd())+" - "+raDateUtil.getraDateUtil().dataFormatter(getDatumDo()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private void doPDV13() {
    try {
      _izvjpdv_ciz_prefix = "Pod";
      izvjpdv = IzvjPDV.getDataModule().getFilteredDataSet("CIZ like 'Pod%'");
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
    return _this;
  }
  public java.sql.Timestamp getDatumOd(){
    return stds.getTimestamp("DATUMOD");
  }

  public java.sql.Timestamp getDatumDo(){
    return stds.getTimestamp("DATUMDO");
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
      updPDV();
      break;
            
    default:
      break;
    }
  }

  private void updJOPPD() {
    getJOPPD_BDialog().show();
    
  }


  private void updPDV_K() {
    updPDV(setPDV_K);
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
    lookupData.getlookupData().raLocate(dM.getDataModule().getAllPartneri(), "CPAR", setZP.getInt("CPAR")+"");
    String capt = dM.getDataModule().getAllPartneri().getString("OIB")+" - "+dM.getDataModule().getAllPartneri().getString("NAZPAR");
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
    lookupData.getlookupData().raLocate(dM.getDataModule().getAllPartneri(), "CPAR", setPDVS.getInt("CPAR")+"");
    String capt = dM.getDataModule().getAllPartneri().getString("OIB")+" - "+dM.getDataModule().getAllPartneri().getString("NAZPAR");
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
  
  //JOPPD 
  private StorageDataSet strAset = null;
  private String oib = null;
  public StorageDataSet getStrAset() {
    if (strAset == null) {
      strAset = new StorageDataSet();
      strAset.addColumn(dM.createStringColumn("OZNIZV","Oznaka izvješæa",5));
      strAset.addColumn(dM.createIntColumn("VRSTAIZV", "Vrsta izvješæa"));
      strAset.addColumn(dM.createStringColumn("OZNPOD", "Oznaka podnositelja",1));
      strAset.addColumn(dM.createBigDecimalColumn("PORPRIRPL","V.1.1.Por.i.prir.na plaæu"));
      strAset.addColumn(dM.createBigDecimalColumn("PORPRIRMIR","V.1.2.Por.i.prir.na mirovinu"));
      strAset.addColumn(dM.createBigDecimalColumn("PORPRIRKAP","V.2.Por.i.prir.na kapital"));
      strAset.addColumn(dM.createBigDecimalColumn("PORPRIRIMO","V.3.Por.i.prir.na imovinu"));
      strAset.addColumn(dM.createBigDecimalColumn("PORPRIROS","V.4.Por.i.prir.na osiguranje"));
      strAset.addColumn(dM.createBigDecimalColumn("PORPRIRDD","V.5.Por.i.prir.na drugi dohodak"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO1PL","VI.1.1.MIO 1.stup na plaæu"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO1DD","VI.1.2.MIO 1.stup na drugi dohodak"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO1POD","VI.1.3.MIO 1.stup na poduzetnièku plaæu"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO1PP","VI.1.4.MIO 1.stup posebni propisi"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO1OO","VI.1.5.MIO 1.stup odreðene okolnosti"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO1STAZ","VI.1.6.MIO 1.stup ben.staž"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO2PL","VI.2.1.MIO 2.stup na plaæu"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO2DD","VI.2.2.MIO 2.stup na drugi dohodak"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO2POD","VI.2.3.MIO 2.stup na poduzetnièku plaæu"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO2PP","VI.2.4.MIO 2.stup posebni propisi"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO2STAZ","VI.2.5.MIO 2.stup ben.staž"));
      strAset.addColumn(dM.createBigDecimalColumn("ZDRPL","VI.3.1.Zdravstveno os. iz plaæe"));
      strAset.addColumn(dM.createBigDecimalColumn("ZASNRPL","VI.3.2.Zaštita na radu iz plaæe"));
      strAset.addColumn(dM.createBigDecimalColumn("ZDRPOD","VI.3.3.Zdravstveno os. iz poduzetnièke plaæe"));
      strAset.addColumn(dM.createBigDecimalColumn("ZASNRPOD","VI.3.4.Zaštita na radu iz poduzetnièke plaæe"));
      strAset.addColumn(dM.createBigDecimalColumn("ZDRDD","VI.3.5.Zdravstveno os. drugi dohodak"));
      strAset.addColumn(dM.createBigDecimalColumn("ZDRINO","VI.3.6.Zdravstveno os. u inozemstvu"));
      strAset.addColumn(dM.createBigDecimalColumn("ZDRPENZ","VI.3.7.Zdravstveno os. umirovljenici"));
      strAset.addColumn(dM.createBigDecimalColumn("ZDRPP","VI.3.8.Zdravstveno os. posebni propisi"));
      strAset.addColumn(dM.createBigDecimalColumn("ZASNRPP","VI.3.9.Zaštita na radu posebni propisi"));
      strAset.addColumn(dM.createBigDecimalColumn("ZASNROO","VI.3.10.Zaštita na radu odreðene okolnosti"));
      strAset.addColumn(dM.createBigDecimalColumn("ZAP","VI.4.1.Zapošljavanje na plaæe"));
      strAset.addColumn(dM.createBigDecimalColumn("ZAPOSINV","VI.4.2.Zapošljavanje osoba s invaliditetom"));
      strAset.addColumn(dM.createBigDecimalColumn("NEOP","Neoporezivi primici"));
      strAset.addColumn(dM.createBigDecimalColumn("KAMATA","Kamata MO2"));
      strAset.open();
    }
    return strAset;
  }

  public int getBrojOsoba() {
    getJPTV().enableEvents(false);
    oibs.clear();
    for (strBset.first();strBset.inBounds(); strBset.next()) {
       oibs.add(strBset.getString("OIB"));
    }
    getJPTV().enableEvents(true);
    return oibs.size();
  }
  
  public int getBrojRedaka() {
    if (strBset == null) return 0;
    return strBset.rowCount();
  }

  public void sumStrA() {
    if (strAset == null || strBset == null) return;
    strAset.first();
    allZero(strAset);
    for (strBset.first(); strBset.inBounds(); strBset.next()) {
      addBigDec(strAset, strBset, new String[] {"PORPRIRPL","POR","PRIR"}, "JOS=0001-0039,0201,5403;");
      addBigDec(strAset, strBset, new String[] {"PORPRIRMIR","POR","PRIR"}, "JOS=0101-0119,0121;");
      addBigDec(strAset, strBset, new String[] {"PORPRIRKAP","POR","PRIR"}, "JOS=1001-1009;");
      addBigDec(strAset, strBset, new String[] {"PORPRIRIMO","POR","PRIR"}, "JOS=2001-2009;");
      addBigDec(strAset, strBset, new String[] {"PORPRIRIOS","POR","PRIR"}, "JOS=3001-3009;");
      addBigDec(strAset, strBset, new String[] {"PORPRIRIDD","POR","PRIR"}, "JOS=4001-4009,5501;");

      addBigDec(strAset, strBset, new String[] {"MIO1PL","MIO1"}, "JOS=0001-0003,0005-0009,0021-0029,5701-5799;");
      addBigDec(strAset, strBset, new String[] {"MIO1DD","MIO1"}, "JOS=0201,4002;");
      addBigDec(strAset, strBset, new String[] {"MIO1POD","MIO1"}, "JOS=0031-0039;");
      addBigDec(strAset, strBset, new String[] {"MIO1PP","MIO1"}, "JOS=5401-5403,5608;");
      addBigDec(strAset, strBset, new String[] {"MIO1OO","MIO1"}, "JOS= 5302,5501,5604,5606,5607;");
      addBigDec(strAset, strBset, new String[] {"MIO1STAZ","MIO1STAZ"}, "");

      addBigDec(strAset, strBset, new String[] {"MIO2PL","MIO2"}, "JOS=0001-0003,0005-0009,0021-0029,5701-5799;");
      addBigDec(strAset, strBset, new String[] {"MIO2DD","MIO2"}, "JOS=0201,4002;");
      addBigDec(strAset, strBset, new String[] {"MIO2POD","MIO2"}, "JOS=0031-0039;");
      addBigDec(strAset, strBset, new String[] {"MIO2PP","MIO2"}, "JOS=5101-5103,5201-5299,5301,5401-5403,5608;");
      addBigDec(strAset, strBset, new String[] {"MIO2STAZ","MIO2STAZ"}, "");

      addBigDec(strAset, strBset, new String[] {"ZDRPL","ZDR"}, "JOS=0001,0005,0008,0009,0021-0029,5701;");
      addBigDec(strAset, strBset, new String[] {"ZASNRPL","ZASNR"}, "JOS=0001,0005,0008,0009,0021-0029,5701;");
      addBigDec(strAset, strBset, new String[] {"ZDRPOD","ZDR"}, "JOS=0031-0039;");
      addBigDec(strAset, strBset, new String[] {"ZASNRPOD","ZASNR"}, "JOS=0031-0039;");
      addBigDec(strAset, strBset, new String[] {"ZDRDD","ZDR"}, "JOS=0201,4002;");
      addBigDec(strAset, strBset, new String[] {"ZDRINO","ZDRINO"}, "JOS=0001-0039,5001-5009,5402;");      
      addBigDec(strAset, strBset, new String[] {"ZDRPENZ","ZDR"}, "JOS=0101-0119;");      
      addBigDec(strAset, strBset, new String[] {"ZDRPP","ZDR"}, "JOS=5401,5403,5601,5602,5603,5605,5608;");      
      addBigDec(strAset, strBset, new String[] {"ZASNRPP","ZASNR"}, "JOS=5401,5403,5601,5602,5603,5605,5608;");      
      addBigDec(strAset, strBset, new String[] {"ZASNROO","ZASNR"}, "JOS=5302,5501,5604,5606,5607;");
      addBigDec(strAset, strBset, new String[] {"ZAP","ZAP"}, "");
      addBigDec(strAset, strBset, new String[] {"ZAPOSINV","ZAPOSINV"}, "");

      addBigDec(strAset, strBset, new String[] {"NEOP","NEOP"}, "");
      addBigDec(strAset, strBset, new String[] {"KAMATA","MIO2","MIO2STAZ"}, "JOP=5271;");
      
      

      strAset.post();
    }
  }

  private void addBigDec(StorageDataSet sA, StorageDataSet sB, String[] augs, String cond) {
    boolean rez = false;
//System.out.println("*** COL: "+augs[0]+" ***");        
    if (cond == null || "".equals(cond.trim())) {
      rez = true;
    } else {
      StringTokenizer condflds = new StringTokenizer(cond,";");
      while (condflds.hasMoreTokens()) {
        String condf = condflds.nextToken();
        StringTokenizer f = new StringTokenizer(condf,"=");
        String fld = f.nextToken();
        String cnd = f.nextToken();
        String val = sB.getString(fld).trim();
        StringTokenizer cnds = new StringTokenizer(cnd, ",");
        while (cnds.hasMoreTokens()) {
          String c = cnds.nextToken();
          if (c.contains("-")) {
            try {
              StringTokenizer oddo = new StringTokenizer(c,"-");
              String _od = oddo.nextToken();
              String _do = oddo.nextToken();
              boolean b = val.compareTo(_od.trim()) >= 0 && val.compareTo(_do.trim()) <= 0;
//  System.out.println(val+" between "+_od+" and "+_do+" ... "+b);
              rez = rez || b;
            } catch (Exception e) {
              e.printStackTrace();
            }
            //range
          } else {
            boolean b = val.equals(c.trim());
//  System.out.println(val+" = "+c+" ... "+b);
            rez = rez || b;
          }
        }
      }
    }
    if (rez) { // do the add
      try {
        String cA = augs[0];
        for (int i = 1; i < augs.length; i++) {
          sA.setBigDecimal(cA, sA.getBigDecimal(cA).add(sB.getBigDecimal(augs[i])));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void allZero(StorageDataSet s) {
    allZero(s, null);
  }
  private void allZero(StorageDataSet s, String[] cols2zero) {
    Column[] c = s.getColumns();
    for (int i = 0; i < c.length; i++) {
      if (c[i].getDataType() == Variant.BIGDECIMAL && (cols2zero==null || Arrays.asList(cols2zero).contains(c[i].getColumnName()))) {
        s.setBigDecimal(c[i].getColumnName(), Aus.zero2);
      }
    }
    s.post();
  }

  //Strana B
  private StorageDataSet strBset = null;
  public StorageDataSet getStrBset() {
    if (strBset == null) {
      strBset = new StorageDataSet();
      strBset.addColumn(dM.createIntColumn("RBR","1.RBR"));
      strBset.addColumn(dM.createStringColumn("COPCINE","2.Opæina stanovanja",5));
      strBset.addColumn(dM.createStringColumn("COPRADA","3.Opæina rada",5));
      strBset.addColumn(dM.createStringColumn("OIB","4.OIB",20));
      strBset.addColumn(dM.createStringColumn("IMEPREZ","5.Ime i prezime",100));
      strBset.addColumn(dM.createStringColumn("JOS","6.1.Oznaka stjecatelja/osiguranika",4));
      strBset.addColumn(dM.createStringColumn("JOP","6.2.Oznaka primitka/obveze doprinosa",4));
      strBset.addColumn(dM.createStringColumn("JOB","7.1.Oznaka za staž s poveæanim trajanjem",1));
      strBset.addColumn(dM.createStringColumn("JOZ","7.2.Obveza dopr.za zapošljavanje invalida",1));
      strBset.addColumn(dM.createStringColumn("JOM","8.Oznaka prvog/zadnjeg mj.u osig.po istoj osnovi",1));
      strBset.addColumn(dM.createStringColumn("JRV","9.Oznaka punog/nepunog radnog vremena",1));
      strBset.addColumn(dM.createIntColumn("SATI", "10.Sati rada"));
      strBset.addColumn(dM.createTimestampColumn("ODJ", "10.1.Razdoblje obr. od"));
      strBset.addColumn(dM.createTimestampColumn("DOJ", "10.2.Razdoblje obr. do"));

      strBset.addColumn(dM.createBigDecimalColumn("BRUTO","11.Iznos primitka (oporezivi)"));
      strBset.addColumn(dM.createBigDecimalColumn("OSNDOP","12.Osnovica za doprinose"));
      strBset.addColumn(dM.createBigDecimalColumn("MIO1","12.1. Doprinos MIO 1.stup"));
      strBset.addColumn(dM.createBigDecimalColumn("MIO2","12.2. Doprinos MIO 2.stup"));
      strBset.addColumn(dM.createBigDecimalColumn("ZDR","12.3. Doprinos za zdravstveno osiguranje"));
      strBset.addColumn(dM.createBigDecimalColumn("ZASNR","12.4. Doprinos za zaštitu na radu"));
      strBset.addColumn(dM.createBigDecimalColumn("ZAP","12.5. Doprinos za zapošljavanje"));
      strBset.addColumn(dM.createBigDecimalColumn("MIO1STAZ","12.6. MIO 1-poveæani staž"));
      strBset.addColumn(dM.createBigDecimalColumn("MIO2STAZ","12.7. MIO 2-poveæani staž"));
      strBset.addColumn(dM.createBigDecimalColumn("ZDRINO","12.8. Zdravstveno u inozemstvu"));
      strBset.addColumn(dM.createBigDecimalColumn("ZAPOSINV","12.9. Dopr.za zap.invalida"));
      strBset.addColumn(dM.createBigDecimalColumn("IZDATAK","13.1. Izdatak"));
      strBset.addColumn(dM.createBigDecimalColumn("IZDATAKMIO","13.2. Izdatak-uplaæeni dopr.za MIO"));
      strBset.addColumn(dM.createBigDecimalColumn("DOHODAK","13.3. Dohodak"));
      strBset.addColumn(dM.createBigDecimalColumn("ISKNEOP","13.4. Osobni odbitak"));
      strBset.addColumn(dM.createBigDecimalColumn("POROSN","13.5. Porezna osnovica"));
      strBset.addColumn(dM.createBigDecimalColumn("POR","14.1. Porez"));
      strBset.addColumn(dM.createBigDecimalColumn("PRIR","14.2. Prirez"));
      strBset.addColumn(dM.createStringColumn("JNP","15.1. Oznaka neoporezivog primitka",2));
      strBset.addColumn(dM.createBigDecimalColumn("NEOP","15.2. Iznos neoporezivog primitka"));
      strBset.addColumn(dM.createStringColumn("JNI","16.1. Oznaka naèina isplate",1));
      strBset.addColumn(dM.createBigDecimalColumn("NETOPK","16.2. Iznos za isplatu"));
      strBset.addColumn(dM.createBigDecimalColumn("BRUTOOBR","17. Obraèunata plaæa"));
      strBset.open();
    }
    return strBset;  
    
  }
  private void addStrBSet(String cn, BigDecimal aug) {
    if (samojedna) {
      if (multipliers.containsKey(cn)) {
        strBset.setBigDecimal(cn, rs.getBigDecimal((String)multipliers.get(cn)));
      } else {
        strBset.setBigDecimal(cn, rs.getBigDecimal(cn));
      }
    } else {
      Aus.add(strBset, cn, aug);
    }
  }
  QueryDataSet rpls;
  HashSet oibs = null;
  boolean samojedna = false;
  HashMap multipliers = new HashMap();
  private QueryDataSet rs;
  private String currOIB = null;
  /**
   * The ... gulp!
   */
  private void doJOPPD() {
    multipliers.clear();
    multipliers.put("OSNDOP", "BRUTO");
    multipliers.put("ZDR", "ZO");
    multipliers.put("ZAP", "ZAPOS");
    multipliers.put("ISKNEOP", "OSODB");
    multipliers.put("POR", "POREZ");
    multipliers.put("PRIR", "PRIREZ");
    oibs = new HashSet();
  if (currOIB == null) {
    //strA
    getStrAset();
    strAset.empty();
    strAset.insertRow(false);
    strAset.setString("OZNIZV", getOZNJOPPD(getDatumOd()));
    strAset.setInt("VRSTAIZV", 1);
    strAset.setString("OZNPOD",frmParam.getParam("pl", "jpod"+dlgGetKnjig.getKNJCORG(), "1", "Vrsta podnositelja JOPPD za knjigovodstvo "+dlgGetKnjig.getKNJCORG()));
    //strB
    getStrBset();
    strBset.empty();
    rs = getRSperiod();
    int rbr = 0;
    for (rs.first(); rs.inBounds(); rs.next()) {
      boolean naknada = isNaknada(rs.getShort("CVRP")+"");
      if (getSifraFor("JOS", rs).trim().equals("0001") && naknada) continue;//prijevoz !?!?!
      BigDecimal zasnr = Aus.zero2;
      BigDecimal zapinv = Aus.zero2;
      BigDecimal omjer = Aus.zero2;
      if (!isOOmatch(rs)) continue;
      if (rs.getBigDecimal("BRUTO").signum()!=0) omjer = rs.getBigDecimal("PBTO").divide(rs.getBigDecimal("BRUTO"),4,BigDecimal.ROUND_HALF_UP);
      
/*****D*E*B*U*G***B*R*I*Š*I*******/
//System.out.println(strBset);
//System.err.println(VarStr.join( new String[] {rs.getString("JMBG"), 
//          getSifraFor("JOS", rs),getSifraFor("JOP", rs),getSifraFor("JOB", rs),getSifraFor("JOZ", rs),getSifraFor("JOM", rs), 
//          getSifraFor("JRV", rs),getSifraFor("JNP", rs),getSifraFor("JNI", rs), 
//          getDatum(rs, "ODDANA")+""}, '#'));
/*****D*E*B*U*G***B*R*I*Š*I*******/
      if (lookupData.getlookupData().raLocate(strBset, new String[] {"OIB","JOS","JOP","JOB","JOZ","JOM","JRV","JNI","JNP","ODJ"}, 
          new String[] {rs.getString("JMBG"), 
          getSifraFor("JOS", rs),getSifraFor("JOP", rs),getSifraFor("JOB", rs),getSifraFor("JOZ", rs),
          getSifraFor("JOM", rs),getSifraFor("JRV", rs),getSifraFor("JNI", rs), getSifraFor("JNP", rs),
          getDatum(rs, "ODDANA")+""})) {
        if (rs.getBigDecimal("PBTO").add(strBset.getBigDecimal("OSNDOP")).subtract(rs.getBigDecimal("BRUTO")).abs().compareTo(new BigDecimal("0.1"))<=0) {
          //sve je zbrajano u jedan red - treba uzeti gotove sume a ne omjer
          samojedna = true;
        } else {
          samojedna = false;
        }
        addStrBSet("BRUTO", rs.getBigDecimal("PBTO"));
        addStrBSet("OSNDOP", rs.getBigDecimal("PBTO"));
        addStrBSet("MIO1", rs.getBigDecimal("MIO1").multiply(omjer));
        addStrBSet("MIO2", rs.getBigDecimal("MIO2").multiply(omjer));
        //temp hack
        if (rs.getBigDecimal("ZO").signum() != 0) {
          zasnr = rs.getBigDecimal(samojedna?"BRUTO":"PBTO").multiply(new BigDecimal("0.005")).setScale(2,BigDecimal.ROUND_HALF_UP);
          zapinv = isZapInv()?
              rs.getBigDecimal(samojedna?"BRUTO":"PBTO").multiply(new BigDecimal("0.001")).setScale(2,BigDecimal.ROUND_HALF_UP)
              :Aus.zero2;
        }
        
        if (samojedna) {
          strBset.setBigDecimal("ZDR", rs.getBigDecimal("ZO").subtract(zasnr));
//          strBset.setBigDecimal("ZAP", rs.getBigDecimal("ZAPOS").subtract(zapinv));
          strBset.setBigDecimal("ZASNR", zasnr);
          strBset.setBigDecimal("ZAPOSINV", zapinv);
        } else {
          addStrBSet("ZDR", rs.getBigDecimal("ZO").multiply(omjer).subtract(zasnr));
//          addStrBSet("ZAP", rs.getBigDecimal("ZAPOS").multiply(omjer).subtract(zapinv));
          addStrBSet("ZASNR", zasnr);
          addStrBSet("ZAPOSINV", zapinv);
        }
        addStrBSet("ZAP", rs.getBigDecimal("ZAPOS").multiply(omjer));
        strBset.setBigDecimal("IZDATAKMIO", strBset.getBigDecimal("MIO1").add(strBset.getBigDecimal("MIO2")));
        strBset.setBigDecimal("DOHODAK",
            strBset.getBigDecimal("BRUTO")
            .subtract(strBset.getBigDecimal("IZDATAKMIO"))
//            .subtract(strBset.getBigDecimal("IZDATAK"))
            );
        addStrBSet("ISKNEOP",rs.getBigDecimal("OSODB").multiply(omjer));
        strBset.setBigDecimal("POROSN",strBset.getBigDecimal("DOHODAK").subtract(strBset.getBigDecimal("ISKNEOP")));
        addStrBSet("POR", rs.getBigDecimal("POREZ").multiply(omjer));
        addStrBSet("PRIR", rs.getBigDecimal("PRIREZ").multiply(omjer));
        if (getSifraFor("JOP", rs).equals("0000") && !getSifraFor("JNP",rs).equals("0")) {
          strBset.setString("JNP", getSifraFor("JNP",rs));
          strBset.setBigDecimal("NEOP", rs.getBigDecimal("PBTO")); //naknada posebno
        } else if (rs.getBigDecimal("NAKNADE").signum() > 0 && !getSifraFor("JNP",rs).equals("0")) {
          strBset.setString("JNP", getSifraFor("JNP",rs));
          strBset.setBigDecimal("NEOP", rs.getBigDecimal("NAKNADE")); //kumulrad
        } else {
          strBset.setString("JNP", "0");
          strBset.setBigDecimal("NEOP", Aus.zero2);
        }
        strBset.setString("JNI", getSifraFor("JNI",rs));
        strBset.setBigDecimal("NETOPK", strBset.getBigDecimal("DOHODAK")
            .subtract(strBset.getBigDecimal("POR").add(strBset.getBigDecimal("PRIR")))
            .add(strBset.getBigDecimal("NEOP"))
            );
      } else {
        strBset.insertRow(false);
        strBset.setInt("RBR", ++rbr);
        strBset.setString("COPCINE", getOpcinaStanovanja(rs.getString("CRADNIK")));
        strBset.setString("COPRADA", "0"+raIzvjestaji.convertCopcineToRS(rs.getString("COPCINE")));
        strBset.setString("OIB", rs.getString("JMBG"));
        strBset.setString("IMEPREZ", rs.getString("IME")+" "+rs.getString("PREZIME"));
        strBset.setString("JOS", getSifraFor("JOS",rs));
        strBset.setString("JOP", getSifraFor("JOP",rs));
        strBset.setString("JOB", getSifraFor("JOB",rs));
        strBset.setString("JOZ", getSifraFor("JOZ",rs));
        strBset.setString("JOM", getSifraFor("JOM",rs));
        strBset.setString("JRV", getSifraFor("JRV",rs));
        strBset.setInt("SATI", rs.getBigDecimal("SATI").intValue());
        strBset.setTimestamp("ODJ", getDatum(rs, "ODDANA"));
        strBset.setTimestamp("DOJ", getDatum(rs, "DODANA"));
        strBset.setBigDecimal("BRUTO", rs.getBigDecimal("PBTO"));
        strBset.setBigDecimal("OSNDOP", rs.getBigDecimal("PBTO"));
        strBset.setBigDecimal("MIO1", rs.getBigDecimal("MIO1").multiply(omjer));
        strBset.setBigDecimal("MIO2", rs.getBigDecimal("MIO2").multiply(omjer));
        //temp hack
        if (rs.getBigDecimal("ZO").signum() != 0) {
          zasnr = strBset.getBigDecimal("OSNDOP").multiply(new BigDecimal("0.005")).setScale(2,BigDecimal.ROUND_HALF_UP);
//          zasnr = rs.getBigDecimal(samojedna?"BRUTO":"PBTO").multiply(new BigDecimal("0.005")).setScale(2,BigDecimal.ROUND_HALF_UP);
          zapinv = isZapInv()?
              strBset.getBigDecimal("OSNDOP").multiply(new BigDecimal("0.001")).setScale(2,BigDecimal.ROUND_HALF_UP)
              :Aus.zero2;

        }
        strBset.setBigDecimal("ZDR", rs.getBigDecimal("ZO").multiply(omjer).subtract(zasnr));
        strBset.setBigDecimal("ZASNR", zasnr);
        strBset.setBigDecimal("ZAP", rs.getBigDecimal("ZAPOS").multiply(omjer));
        strBset.setBigDecimal("ZAPOSINV",zapinv);
        strBset.setBigDecimal("IZDATAKMIO", strBset.getBigDecimal("MIO1").add(strBset.getBigDecimal("MIO2")));
        strBset.setBigDecimal("DOHODAK",
            strBset.getBigDecimal("BRUTO")
            .subtract(strBset.getBigDecimal("IZDATAKMIO"))
//            .subtract(strBset.getBigDecimal("IZDATAK"))
            );
        strBset.setBigDecimal("ISKNEOP",rs.getBigDecimal("OSODB").multiply(omjer));
        strBset.setBigDecimal("POROSN",strBset.getBigDecimal("DOHODAK").subtract(strBset.getBigDecimal("ISKNEOP")));
        strBset.setBigDecimal("POR", rs.getBigDecimal("POREZ").multiply(omjer));
        strBset.setBigDecimal("PRIR", rs.getBigDecimal("PRIREZ").multiply(omjer));
        if (getSifraFor("JOP", rs).equals("0000") && !getSifraFor("JNP",rs).equals("0")) {
          strBset.setString("JNP", getSifraFor("JNP",rs));
          strBset.setBigDecimal("NEOP", rs.getBigDecimal("PBTO")); //naknada posebno
        } else if (rs.getBigDecimal("NAKNADE").signum() > 0 && !getSifraFor("JNP",rs).equals("0") && !oibs.contains(rs.getString("JMBG"))) {
          strBset.setString("JNP", getSifraFor("JNP",rs));
          strBset.setBigDecimal("NEOP", rs.getBigDecimal("NAKNADE").multiply(omjer)); //kumulrad
        } else {
          strBset.setString("JNP", "0");
          strBset.setBigDecimal("NEOP", Aus.zero2);
        }
        strBset.setString("JNI", getSifraFor("JNI",rs));
        strBset.setBigDecimal("NETOPK", strBset.getBigDecimal("DOHODAK")
            .subtract(strBset.getBigDecimal("POR").add(strBset.getBigDecimal("PRIR")))
            .add(strBset.getBigDecimal("NEOP"))
            //rs.getBigDecimal("NETOPK")
            );
        if (naknada) strBset.setBigDecimal("NETOPK",strBset.getBigDecimal("NEOP"));
        strBset.setBigDecimal("BRUTOOBR", rs.getBigDecimal("BRUTO"));
        if (strBset.getString("JNI").equals("5")) {
          strBset.setTimestamp("ODJ", Util.getUtil().getFirstDayOfYear(getDatumOd()));
          strBset.setTimestamp("DOJ", Util.getUtil().getLastDayOfYear(getDatumOd()));
          strBset.setBigDecimal("BRUTOOBR", Aus.zero2);
        }
      }
      //tweakovi
      if (naknada) {
        allZero(strBset, new String[] {"BRUTO","OSNDOP","MIO1","MIO2","ZDR","ZASNR","ZAP","IZDATAKMIO","DOHODAK","ISKNEOP","POROSN","POR","PRIR","BRUTOOBR"});
        if (strBset.getBigDecimal("NEOP").signum() == 0) strBset.setBigDecimal("NEOP", strBset.getBigDecimal("NETOPK"));
      }
      if (strBset.getString("JRV").equals("0")) {//nema radnog vremena
        strBset.setString("COPRADA","00000");
      }
      if (strBset.getString("JOP").equals("0041")) {
        allZero(strBset, new String[] {"BRUTO","IZDATAKMIO","DOHODAK","ISKNEOP","POROSN","POR","PRIR","NEOP","NETOPK"});
        strBset.setString("JNP", "0");
//        strBset.setBigDecimal("BRUTO", Aus.zero2);
//        strBset.setBigDecimal("IZDATAKMIO", Aus.zero2);
//        strBset.setBigDecimal("DOHODAK", Aus.zero2);
//        strBset.setBigDecimal("ISKNEOP", Aus.zero2);
//        strBset.setBigDecimal("POROSN", Aus.zero2);
//        strBset.setBigDecimal("POR", Aus.zero2);
//        strBset.setBigDecimal("PRIR", Aus.zero2);
//        strBset.setBigDecimal("NEOP", Aus.zero2);
//        strBset.setBigDecimal("NETOPK", Aus.zero2);
      }      
      if (strBset.getString("JOP").equals("0051")) {
        allZero(strBset, new String[] {"OSNDOP","MIO1","MIO2","ZDR","ZASNR","ZAP","ZAPOSINV"});
      }
      
      oibs.add(rs.getString("JMBG"));
      strBset.post();
    }
    currOIB = getKnjCurrOIB();
    sumStrA();
  } //currOib
    strBset.setSort(new SortDescriptor(new String[] {"RBR"}));
    strBset.setTableName("JOPPDB");
    getJPTV().getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    setDataSet(strBset);
    getJPTV().removeAllTableModifiers();
    killAllReports();
    addReport("hr.restart.pl.repJOPPDDisk", "Datoteka JOPPD za e-poreznu");
    setTitle("Obrazac JOPPD za dan "+raDateUtil.getraDateUtil().dataFormatter(getDatumOd()));
    jbJOPPD_A.setText("Strana A");
    jbJOPPD_A.setEnabled(true);
//    jraPoctDat.setEnabled(true);
    raCommonClass.getraCommonClass().setLabelLaF(jraPoctDat, true);
  }
  
  private boolean isZapInv() {
    // TODO Auto-generated method stub
    return frmParam.getParam("pl", "jzapinv"+hr.restart.zapod.OrgStr.getKNJCORG(), "N", "Ukljuèiti doprinos za zap.inv. u JOPPD za "+hr.restart.zapod.OrgStr.getKNJCORG()).equalsIgnoreCase("D");
  }
  private String getKnjCurrOIB() {
    QueryDataSet oibset = Aus.q("SELECT OIB from logotipovi WHERE corg = '"+hr.restart.zapod.OrgStr.getKNJCORG()+"'");
    oibset.open();
    oibset.first();
    return oibset.getString("OIB");
  }
  
  private boolean isNaknada(String cvrp) {
    Vrsteprim.getDataModule().getQueryDataSet().open();
    if (lookupData.getlookupData().raLocate(Vrsteprim.getDataModule().getQueryDataSet(), "CVRP", cvrp)) {
      return Vrsteprim.getDataModule().getQueryDataSet().getString("PARAMETRI").toUpperCase().startsWith("NN");
    }
    return false;
  }

  private Timestamp getDatum(QueryDataSet rs, String col) {
    Calendar c = Calendar.getInstance();
    int y, m, d;
    if (rs.hasColumn("GODOBR") != null) {
      y = rs.getShort("GODOBR");
      m = rs.getShort("MJOBR");
    } else {
      dM.getDataModule().getOrgpl().open();
      raIniciranje.getInstance().posOrgsPl(rs.getString("CORG"));
      y = dM.getDataModule().getOrgpl().getShort("GODOBR");
      m = dM.getDataModule().getOrgpl().getShort("MJOBR");
    }
    d = rs.getShort(col);
    c.set(y, m-1, d);
    return Util.getUtil().getFirstSecondOfDay(new Timestamp(c.getTimeInMillis()));
  }

  private QueryDataSet vrstaprim;
  private QueryDataSet getVrstaPrim(short cvrp) {
    if (vrstaprim == null) {
      vrstaprim = Vrsteprim.getDataModule().copyDataSet();
    }
    vrstaprim.open();
    if (lookupData.getlookupData().raLocate(vrstaprim, "CVRP", cvrp+"")) return vrstaprim;
    return null;
  }
  // da li je RS00 na 
  private boolean isOOmatch(ReadRow rs) {
    try {
      String rs_OO = rs.getString("RSOO");
      String vp_OO = getVrstaPrim(rs.getShort("CVRP")).getString("RSOO");
      if (vp_OO.trim().equals("")) vp_OO = "10";
      return rs_OO.equals(vp_OO);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }
  private String getSifraFor(String col, ReadRow rs) {
    if (rpls == null) {
      rpls = Radplsifre.getDataModule().getTempSet();
      rpls.open();
    }
    String cradnik = rs.getString("CRADNIK");
    String cvrp = "P"+rs.getShort("CVRP");
    if (lookupData.getlookupData().raLocate(rpls, "CRADNIK", cvrp)) {
      String vrstaprim_rsOO = getVrstaPrim(rs.getShort("CVRP")).getString("RSOO");
      if (!vrstaprim_rsOO.equals(rs.getString("RSOO"))) {
        //ako se razlikuje RSOO na periodu i vrsti primanja treba pronaci sifru za RSOO sa perioda
        for (vrstaprim.first(); vrstaprim.inBounds(); vrstaprim.next()) {
          if (vrstaprim.getString("RSOO").equals(rs.getString("RSOO"))) {
            if (lookupData.getlookupData().raLocate(rpls, "CRADNIK", "P"+vrstaprim.getShort("CVRP"))) {
System.err.println(
    "cradnik:" + rs.getString("CRADNIK")
    + "  CVRP:" +rs.getShort("CVRP")
    + "  rs.RSOO:" +rs.getString("RSOO")
    + "  vp.RSOO:" +vrstaprim_rsOO
    + "  returnForCol "+col+" = "+rpls.getString(col)
    );
              return rpls.getString(col);
            }
          }
        }
      }
    } else if (lookupData.getlookupData().raLocate(rpls, "CRADNIK", cradnik)) {
//      return rpls.getString(col);
    } else if (lookupData.getlookupData().raLocate(rpls, "CRADNIK", "#"+OrgStr.getKNJCORG())) {//2.7
      //
    } else if (lookupData.getlookupData().raLocate(rpls, "CRADNIK", "#")) {
//      return rpls.getString(col);
    } else {
      rpls.insertRow(false);
//      rpls.setString("CRADNIK", "#");
//      rpls.setString("JOM", "3");
//      rpls.setString("JNP", "19");
      setDefaultRPLS(rpls);
      rpls.post();
      rpls.saveChanges();
    }
    return rpls.getString(col);
  }

  private void getAllSifreFor(String[] cols, ReadWriteRow rs) {
    if (cols == null) cols = new String[] {"JOS","JOP","JOB","JOZ","JOM","JRV","JNP","JNI"};
    for (int i = 0; i < cols.length; i++) {
      if (rs.hasColumn(cols[i]) != null) rs.setString(cols[i], getSifraFor(cols[i], rs)); 
    }
  }
  private static void setDefaultRPLS(ReadWriteRow r) {
    r.setString("CRADNIK", "#");
    r.setString("JOS", "0001");
    r.setString("JOP", "0001");
    r.setString("JOB", "0");
    r.setString("JOZ", "0");
    r.setString("JOM", "3");
    r.setString("JRV", "1");
    r.setString("JNP", "19");
    r.setString("JNI", "1");
  }
  
  private String getOpcinaStanovanja(String cradnik) {
    QueryDataSet rpl = Aus.q("SELECT copcine from radnicipl where cradnik='"+cradnik+"'");
    rpl.open();
    rpl.first();
    return "0"+raIzvjestaji.convertCopcineToRS(rpl.getString("COPCINE"));
  }

  private QueryDataSet getRSperiod() {
    int m1, m2;
    String qry = "SELECT rsperiodobr.*, radnici.corg, radnici.ime, radnici.prezime, kumulrad.sati as satiuk, kumulrad.naknade, primanjaobr.cvrp, primanjaobr.bruto as PBTO, primanjaobr.sati as sativp FROM rsperiodobr, radnici, kumulrad, primanjaobr where "+
        " radnici.cradnik = rsperiodobr.cradnik AND kumulrad.cradnik = rsperiodobr.cradnik AND primanjaobr.cradnik = rsperiodobr.cradnik AND ";
    QueryDataSet orgpl = Orgpl.getDataModule().getTempSet(Condition.equal("CORG", OrgStr.getKNJCORG()));
    orgpl.open();
    orgpl.first();
//    Calendar c = Calendar.getInstance();
//    c.setTimeInMillis(orgpl.getTimestamp("DATUMISPL").getTime());
//    m1 = c.get(Calendar.MONTH);
//    c.setTimeInMillis(getDatumOd().getTime());
//    m2 = c.get(Calendar.MONTH);
    String orgqrs = "(";
//    if (m1 == m2) {
    if (Util.getUtil().getFirstSecondOfDay(orgpl.getTimestamp("DATUMISPL")).equals(Util.getUtil().getFirstSecondOfDay(getDatumOd()))) {
      orgqrs = orgqrs + "(radnici.corg in "+
          OrgStr.getOrgStr().getInQuery(OrgStr.getOrgStr().getOrgstrAndCurrKnjig(),"radnici.corg")+") ";
      String joincorg = frmParam.getParam("pl", "jopjoin"+OrgStr.getKNJCORG(), "", "Koju još O.J. spojiti na isti joppd");
      if (!joincorg.equals("")) {
        orgqrs = orgqrs + " OR " + "(radnici.corg in "+
            OrgStr.getOrgStr().getInQuery(OrgStr.getOrgStr().getOrgstrAndKnjig(joincorg),"radnici.corg")+") ";
      }
      orgqrs = orgqrs + ")";
      
      qry = qry+orgqrs;
    } else {      
      qry = qry+" 0=1";
      QueryDataSet orgarh = Kumulorgarh.getDataModule().getTempSet(
          Condition.between("DATUMISPL", Util.getUtil().getFirstDayOfMonth(getDatumOd()), Util.getUtil().getLastDayOfMonth(getDatumOd()))
          .and(Condition.equal("KNJIG", OrgStr.getKNJCORG()))
          );
      orgarh.open();
      orgarh.first();
      raPlObrRange range = new raPlObrRange(orgarh.getShort("GODOBR"),orgarh.getShort("MJOBR"), orgarh.getShort("RBROBR"));
      long diff = Long.MAX_VALUE;
      if (orgarh.getRowCount() > 1) {
        for (orgarh.first(); orgarh.inBounds(); orgarh.next()) {
          long d = Math.abs(orgarh.getTimestamp("DATUMISPL").getTime() - getDatumDo().getTime());
          if (d<diff) {
            range = new raPlObrRange(orgarh.getShort("GODOBR"),orgarh.getShort("MJOBR"), orgarh.getShort("RBROBR"));
            diff = d;
          }
        }
      }
      if (JOptionPane.showConfirmDialog(this, 
          "Pronaðen je obraèun za "+range.getMJOBRfrom()+". mjesec "+range.getGODOBRfrom()+". godine. Izraditi obrazac za taj obraèun?",
          "Pitanje",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        qry = "SELECT rsperiodarh.*, radnici.corg, radnici.ime, radnici.prezime, kumulradarh.sati as satiuk, kumulradarh.naknade, Primanjaarh.cvrp, Primanjaarh.bruto as PBTO, Primanjaarh.sati as sativp FROM rsperiodarh, radnici, kumulradarh, primanjaarh where "+
            " radnici.cradnik = rsperiodarh.cradnik AND kumulradarh.cradnik = rsperiodarh.cradnik AND Primanjaarh.cradnik = rsperiodarh.cradnik AND (radnici.corg in "+
            OrgStr.getOrgStr().getInQuery(OrgStr.getOrgStr().getOrgstrAndCurrKnjig(),"radnici.corg")+") "
            +" AND rsperiodarh.godobr="+range.getGODOBRfrom()
            +" AND rsperiodarh.mjobr="+range.getMJOBRfrom()
            +" AND rsperiodarh.rbrobr="+range.getRBROBRfrom()
            +" AND kumulradarh.godobr="+range.getGODOBRfrom()
            +" AND kumulradarh.mjobr="+range.getMJOBRfrom()
            +" AND kumulradarh.rbrobr="+range.getRBROBRfrom()
            +" AND Primanjaarh.godobr="+range.getGODOBRfrom()
            +" AND Primanjaarh.mjobr="+range.getMJOBRfrom()
            +" AND Primanjaarh.rbrobr="+range.getRBROBRfrom()
            ;
        raOdbici.getInstance().setObrRange(range);
      }
    }
    System.out.println(qry);
    QueryDataSet rs = Aus.q(qry);
    rs.open();
    rs.setSort(new SortDescriptor(new String[] {"JMBG"}));
    return rs;
  }

  public static String getOZNJOPPD(Timestamp datum) {
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(datum.getTime());
    return String.valueOf(c.get(Calendar.YEAR)).substring(2)
        +Valid.getValid().maskZeroInteger(Integer.valueOf(c.get(Calendar.DAY_OF_YEAR)), 3);
  }
  
  private JraDialog getJOPPD_BDialog() {
    JraDialog dlg = getDialogForSet(strBset, true);
    startFrame.getStartFrame().centerFrame(dlg, 0, "JOPPD strana B - izmjena");
    return dlg;
  }
  private JraDialog getJOPPD_ADialog() {
    JraDialog dlg = getDialogForSet(strAset, false);
    startFrame.getStartFrame().centerFrame(dlg, 0, "JOPPD strana A - izmjena");
    return dlg;
  }

  private JraDialog getDialogForSet(final StorageDataSet set, final boolean recalc) {
    return getDialogForSet(set, recalc, new Runnable() {
      public void run() {
        set.post();
        int pos = set.getRow();
        if (recalc) {
          getJPTV().enableEvents(false);
          if (!isAutoSumLimit()) sumStrA();
          set.goToRow(pos);
          getJPTV().enableEvents(true);          
        } else {
          saveJOPPD();
          frmPDV2.this.getJPTV().fireTableDataChanged();
        }
      }
    }, new Runnable() {
      
      public void run() {
        set.cancel();
      }
    });
  }
  
  public static JraDialog getDialogForSet(final StorageDataSet set, final boolean recalc, final Runnable okAction, final Runnable cancelAction) {
    final JraDialog dlg = new JraDialog();
    JPanel contpane = new JPanel(new GridLayout(0,1));
    Column[] cols = set.getColumns();
    for (int i = 0; i < cols.length; i++) {
      XYLayout xyl = new XYLayout(570,30);
      JPanel row = new JPanel(xyl);
      row.add(new JLabel(cols[i].getCaption()), new XYConstraints(15,5,-1,-1));
      JraTextField jt = new JraTextField();
      jt.setColumnName(cols[i].getColumnName());
      jt.setDataSet(cols[i].getDataSet());
      int size = getJTSize(cols[i]);
      JraButton bget = getJTButton(cols[i], dlg);
      int bsize = 0;
      if (bget!=null) {
        bsize = 26;
        row.add(bget, new XYConstraints(539, 5, 21, 21));
      }
      row.add(jt, new XYConstraints(560-size-bsize, 5, size, 21));
      contpane.add(row);
    }
    OKpanel okp = new OKpanel() {
      
      public void jPrekid_actionPerformed() {
        cancelAction.run();
        dlg.dispose();
      }
      
      public void jBOK_actionPerformed() {
        okAction.run();
        dlg.dispose();
      }
    };
    dlg.getContentPane().setLayout(new BorderLayout());
    dlg.getContentPane().add(new JScrollPane(contpane), BorderLayout.CENTER);
    dlg.getContentPane().add(okp, BorderLayout.SOUTH);
    dlg.setMinimumSize(new Dimension(600, 500));
    dlg.pack();
    return dlg;
  }

  private static JraButton getJTButton(final Column c, final JraDialog dlg) {
    JraButton b = null;
    if (c.getColumnName().toUpperCase().startsWith("COP")) {
      b = new JraButton();
      b.setText("...");
      final QueryDataSet opcine = Opcine.getDataModule().copyDataSet();
      opcine.open();
      b.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String[] rez = lookupData.getlookupData().lookUp(dlg, opcine, new int[] {0,1});
            if (rez!=null) c.getDataSet().setString(c.getColumnName(), "0"+raIzvjestaji.convertCopcineToRS(opcine.getString("COPCINE")));
        }
      });
    } else if (c.getColumnName().equalsIgnoreCase("OIB") || c.getColumnName().equalsIgnoreCase("IMEPREZ")) {
      b = new JraButton();
      b.setText("...");
      final QueryDataSet radnici = Aus.q("SELECT radnici.cradnik, radnici.ime, radnici.prezime, radnicipl.oib, radnicipl.copcine "
          + "FROM radnici, radnicipl where radnici.cradnik = radnicipl.cradnik and radnici."+plUtil.getPlUtil().getRadCurKnjig());
      
      radnici.open();
      b.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String[] rez = lookupData.getlookupData().lookUp(dlg, radnici, new int[] {0,1,2,3});
            if (rez!=null) {
              c.getDataSet().setString("IMEPREZ", radnici.getString("IME")+" "+radnici.getString("PREZIME"));
              c.getDataSet().setString("OIB", radnici.getString("OIB"));
              c.getDataSet().setString("COPCINE", "0"+raIzvjestaji.convertCopcineToRS(radnici.getString("COPCINE")));
            }
        }
      });
      
    } else {
      if (c.getColumnName().equalsIgnoreCase("ODJ") || c.getColumnName().equalsIgnoreCase("DOJ")) return null; 
      b = new JraButton();
      b.setText("...");
      String vrsif = c.getColumnName().equalsIgnoreCase("OZNPOD")?"PLPD":"PL"+c.getColumnName().substring(1);
      if (vrsif.trim().length()>4) return null;
      final QueryDataSet sifre = Sifrarnici.getDataModule().getFilteredDataSet(Condition.equal("VRSTASIF", vrsif));
      sifre.open();
      if (sifre.getRowCount() == 0) return null;
      b.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String[] rez = lookupData.getlookupData().lookUp(dlg, sifre, new int[] {0,2,3});
            if (rez!=null) c.getDataSet().setString(c.getColumnName(), sifre.getString("CSIF"));
        }
      });
    }
    return b;
  }

  private static int getJTSize(Column c) {
    if (c.getDataType() == Variant.STRING) {
      if (c.getPrecision() > 5) return 250;
      return 60;
    }
    if (c.getDataType() == Variant.TIMESTAMP) return 100;
    if (c.getDataType() == Variant.INT) return 50;
    return 160;
  }
  
  /**
   * zbrojeno porez i prirez nesamostalni rad /A.P1/
   */
  public BigDecimal getSumPorPrirUk() {
    return strAset.getBigDecimal("PORPRIRPL").add(strAset.getBigDecimal("PORPRIRMIR"));
  }
  /**
   * zbrojeno porez i prirez nesam. rad /A.P11
   */
  public BigDecimal getSumPorPrirPl() {
    return strAset.getBigDecimal("PORPRIRPL");
  }
  /**
   * zbrojeno porez i prirez umrvljenici /A.P12
   */
  public BigDecimal getSumPorPrirMir() {
    return strAset.getBigDecimal("PORPRIRMIR");
  }
  /**
   * porez i prirez na kapitaliste /A.P2
   */
  public BigDecimal getSumPorPrirKap() {
    return strAset.getBigDecimal("PORPRIRKAP");
  }
  
  /**
   * porez i prirez na feudalce /A.P3
   */
  public BigDecimal getSumPorPrirImo() {
    return strAset.getBigDecimal("PORPRIRIMO");
  }
  
  /**
   * porez i prirez na kamatare /A.P4
   */
  public BigDecimal getSumPorPrirOs() {
    return strAset.getBigDecimal("PORPRIROS");
  }
  
  /**
   * porez i prirez na honorarce /A.P5
   */
  public BigDecimal getSumPorPrirDD() {
    return strAset.getBigDecimal("PORPRIRDD");
  }

  /**
   * doprinos za MIO1 plaæe /A.gen.P1
   */
  public BigDecimal getSumMIO1Pl() {
    return strAset.getBigDecimal("MIO1PL");
  }
  /**
   * doprinos za MIO1 za drugi dohodak - honorarce /A.gen.P2
   */
  public BigDecimal getSumMIO1DD() {
    return strAset.getBigDecimal("MIO1DD");
  }
  /**
   * doprinos za MIO1 poduzetnicka placa /A.gen.P3
   */
  public BigDecimal getSumMIO1Pod() {
    return strAset.getBigDecimal("MIO1POD");
  }
  /**
   * doprinos za MIO1 posebni propisi /A.gen.P4
   */
  public BigDecimal getSumMIO1PP() {
    return strAset.getBigDecimal("MIO1PP");
  }
  /**
   * doprinos za MIO1 odredene okolnosti /A.gen.P5
   */
  public BigDecimal getSumMIO1OO() {
    return strAset.getBigDecimal("MIO1OO");
  }
  /**
   * doprinos za MIO1 staz sa povecanim trajanjem /A.gen.P6
   */
  public BigDecimal getSumMIO1Staz() {
    return strAset.getBigDecimal("MIO1STAZ");
  }
  
  /**
   * doprinos za MIO2 plaæe /A.kap.P1
   */
  public BigDecimal getSumMIO2Pl() {
    return strAset.getBigDecimal("MIO2PL");  
  }
  /**
   * doprinos za MIO2 za drugi dohodak - honorarce /A.kap.P2
   */
  public BigDecimal getSumMIO2DD() {
    return strAset.getBigDecimal("MIO2DD");  
  }
  /**
   * doprinos za MIO2 poduzetnicka placa /A.kap.P3
   */
  public BigDecimal getSumMIO2Pod() {
    return strAset.getBigDecimal("MIO2POD");  
  }
  /**
   * doprinos za MIO2 posebni propisi /A.kap.P4
   */
  public BigDecimal getSumMIO2PP() {
    return strAset.getBigDecimal("MIO2PP");  
  }
  /**
   * doprinos za MIO2 staz sa povecanim trajanjem /A.kap.P5
   */
  public BigDecimal getSumMIO2Staz() {
    return strAset.getBigDecimal("MIO2STAZ");  
  }
  /**
   * doprinos zdravstvo plaæe /A.zos.P1
   */
  public BigDecimal getSumZdrPl() {
    return strAset.getBigDecimal("ZDRPL");  
  }
  /**
   * doprinos zaštite na radu plaæe /A.zos.P2
   */
  public BigDecimal getSumZasNRPl() {
    return strAset.getBigDecimal("ZASNRPL");  
  }
  /**
   * doprinos zdravstvo poduzetnicka placa /A.zos.P3
   */
  public BigDecimal getSumZdrPod() {
    return strAset.getBigDecimal("ZDRPOD");  
  }
  /**
   * doprinos zastite na radu poduzetnicka placa /A.zos.P4
   */
  public BigDecimal getSumZasNRPod() {
    return strAset.getBigDecimal("ZASNRPOD");  
  }
  /**
   * doprinos zdravstvo drugi dohodak - honorarci /A.zos.P5
   */
  public BigDecimal getSumZdrDD() {
    return strAset.getBigDecimal("ZDRDD");  
  }
  /**
   * doprinos zdravstvo u inozemstvu /A.zos.P6
   */
  public BigDecimal getSumZdrINO() {
    return strAset.getBigDecimal("ZDRINO");  
  }
  /**
   * doprinos zdravstvo za korisnike mirovina /A.zos.P7
   */
  public BigDecimal getSumZdrPenzici() {
    return strAset.getBigDecimal("ZDRPENZ");  
  }
  /**
   * doprinos zdreavstvo po posebnim propisima /A.zos.P8
   */
  public BigDecimal getSumZdrPP() {
    return strAset.getBigDecimal("ZDRPP");  
  }
  /**
   * doprinos zastite na radu posebni propisi /A.zos.P9
   */
  public BigDecimal getSumZasNRPP() {
    return strAset.getBigDecimal("ZASNRPP");  
  }
  /**
   * doprinos zastite na radu odreðene okolnosti /A.zos.P10
   */
  public BigDecimal getSumZasNROO() {
    return strAset.getBigDecimal("ZASNROO");  
  }
  /**
   * doprinos za zaposljavanje /A.zap.P1
   */
  public BigDecimal getSumZap() {
    return strAset.getBigDecimal("ZAP");  
  }
  /**
   * doprinos za zaposljavanje osoba s invaliditetom /A.zap.P2
   */
  public BigDecimal getSumZapOsInv() {
    return strAset.getBigDecimal("ZAPOSINV");  
  }
  /**
   * isplaceni neoporezivi primici
   */
  public BigDecimal getSumNeoporeziviPrimici() {
    return strAset.getBigDecimal("NEOP");  
  }
  /**
   * naplacena kamata za MIO2
   */
  public BigDecimal getKamataMO2() {
    return strAset.getBigDecimal("KAMATA");  
  }
  private void jbJOPPD_A_action() {
    if (jbJOPPD_A.getText().equalsIgnoreCase("Dohvat")) {
      QueryDataSet getSet = Aus.q("SELECT IDIZV, DATUM from JoppdA order by idizv");
      lookupFrame lf = lookupFrame.getLookupFrame(getJframe(), getSet, new int[] {0,1});
      lf.setTitle("Dohvat pohranjenih obrazaca");
      lf.ShowCenter();
      if (lf.getRetValuesUI() != null) {
        loadJOPPD(getSet.getString("IDIZV"));
      }
    } else {
      if (isAutoSumLimit()) {
        if (JOptionPane.showConfirmDialog(null, "Prezbrojiti podatke sa strane B", "Pitanje", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
          sumStrA();
        }
      }
      strAset.setString("OZNIZV", getOZNJOPPD(getDatumOd()));
      getJOPPD_ADialog().show();
    }
  }
  private boolean isAutoSumLimit() {
    int lt = 0; 
    try {
      lt = Integer.parseInt(frmParam.getParam("pl", "jopAutoSum", "200", "Do koliko redova JOPPD-a automatski sumira"));
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return strBset.getRowCount() > lt;
  }
  private String getIDIZV(StorageDataSet a) {
    return a.getString("OZNIZV")+"-"+currOIB+"-"+a.getInt("VRSTAIZV");
  }
  private void saveJOPPD() {
    if (strBset == null || strBset.getRowCount() == 0) return;
    String idizv = getIDIZV(strAset);
    QueryDataSet qsA = JoppdA.getDataModule().getFilteredDataSet(Condition.equal("IDIZV", idizv));
    qsA.open();
    boolean isNew = qsA.getRowCount() == 0;
    String message = isNew?"Pohraniti izvješæe "+idizv+" ?":"Zamijeniti postojeæe izvješæe "+idizv+" ?";
    if (JOptionPane.showConfirmDialog(null, message, "Potvrda", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
      if (!isNew) {
        Valid.getValid().runSQL("DELETE FROM joppda where IDIZV = '"+idizv+"'");
        Valid.getValid().runSQL("DELETE FROM joppdb where IDIZV = '"+idizv+"'");
      }
      QueryDataSet qsB = JoppdB.getDataModule().getFilteredDataSet(Condition.nil);
      qsB.open();
      for (strBset.first();strBset.inBounds(); strBset.next()) {
        qsB.insertRow(false);
        strBset.copyTo(qsB);
        qsB.setString("IDIZV", idizv);
        qsB.post();
      }
      qsA.insertRow(false);
      strAset.copyTo(qsA);
      qsA.setString("IDIZV", idizv);
      qsA.setTimestamp("DATUM", getDatumOd());
      qsA.post();
      if (raTransaction.saveChangesInTransaction(new QueryDataSet[] {qsA,qsB})) {
        JOptionPane.showMessageDialog(null, "Snimanje uspješno!");
      } else {
        JOptionPane.showMessageDialog(null, "Transakcija neuspješna! Ponovite postupak");
      }
    }
  }
  private void loadJOPPD(String idizv) {
    getStrAset();
    getStrBset();
    strAset.empty();
    strBset.empty();
    QueryDataSet qsA = JoppdA.getDataModule().getFilteredDataSet(Condition.equal("IDIZV", idizv));
    QueryDataSet qsB = JoppdB.getDataModule().getFilteredDataSet(Condition.equal("IDIZV", idizv));
    qsA.open();
    qsA.first();
    strAset.insertRow(false);
    qsA.copyTo(strAset);
    strAset.post();
    
    qsB.open();
    for (qsB.first(); qsB.inBounds(); qsB.next()) {
      strBset.insertRow(false);
      qsB.copyTo(strBset);
      strBset.post();
    }
    StringTokenizer toib = new StringTokenizer(idizv,"-");
    toib.nextToken(); //oznaka
    currOIB = toib.nextToken();
    stds.setTimestamp("DATUMOD", qsA.getTimestamp("DATUM"));
    okPress();
  }
}
