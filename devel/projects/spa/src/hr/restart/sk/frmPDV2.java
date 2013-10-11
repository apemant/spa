package hr.restart.sk;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacpdv.v7_0.SRazdoblje;
import hr.restart.baza.Condition;
import hr.restart.baza.IzvjPDV;
import hr.restart.baza.StIzvjPDV;
import hr.restart.baza.dM;
import hr.restart.robno.raDateUtil;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.OKpanel;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpit;
import hr.restart.util.raUpitFat;
import hr.restart.util.startFrame;
import hr.restart.util.sysoutTEST;
import hr.restart.util.VarStr;
import hr.restart.zapod.dlgGetKnjig;

public class frmPDV2 extends raUpitFat {
  
  JPanel datePanel;
  JLabel jlPer = new JLabel("Razdoblje");
  JraTextField jraPoctDat = new JraTextField();
  JraTextField jraKrajDat = new JraTextField();
  JraComboBox jraObrazac = new JraComboBox(new String[] {"Obrazac PDV","Obrazac PDV-S","Obrazac ZP","Obrazac PDV-K"});
  XYLayout xYlay = new XYLayout();
  StorageDataSet stds = new StorageDataSet();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  Util ut = Util.getUtil();
  Valid vl = Valid.getValid();
  QueryDataSet izvjpdv = IzvjPDV.getDataModule().getFilteredDataSet("CIZ like 'Pod%'");
  
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

      jraKrajDat.setDataSet(stds);
      jraKrajDat.setColumnName("DATUMDO");
      datePanel.add(jlPer, new XYConstraints(15,15,-1,-1));
      datePanel.add(jraPoctDat, new XYConstraints(150, 15, 100, -1));
      datePanel.add(jraKrajDat, new XYConstraints(255, 15, 100, -1));
      datePanel.add(jraObrazac, new XYConstraints(360, 15, 150, -1));
      setJPan(datePanel);
      _this = this;
    } catch (Exception e) {
      e.printStackTrace();
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
      
    default:
      break;
    }

  }

  public boolean runFirstESC() {
    return true;
  }

  public void firstESC() {
    raCommonClass.getraCommonClass().EnabDisabAll(this.getJPan(),true);
    killAllReports();
    this.getJPTV().clearDataSet();
    removeNav();
    jraPoctDat.requestFocusLater();
  }

  public void componentShow() {
    stds.setTimestamp("DATUMOD", ut.getFirstDayOfMonth(ut.addMonths(vl.getToday(), -1)));
    stds.setTimestamp("DATUMDO", ut.getLastDayOfMonth(ut.addMonths(vl.getToday(), -1)));
  }

  
  
  private void doPDV_K() {
    // TODO Auto-generated method stub
    
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
    frmParam.getParam("sk", "PDVSCIZroba","Pod303o","Oznake definicija PDV-a iz kojih se prenosi vrijednost dobara u PDVS");
    frmParam.getParam("sk", "PDVSCIZusl","Pod304o","Oznake definicija PDV-a iz kojih se prenosi vrijednost usluga u PDVS");
    frmParam.getParam("sk", "ZPCIZdob","Pod103","Oznake definicija PDV-a iz kojih se prenosi vrijednost dobara u ZP");
    frmParam.getParam("sk", "ZPCIZdob42","Pod106","Oznake definicija PDV-a iz kojih se prenosi vrijednost dobara 42 i 63 u ZP");
    frmParam.getParam("sk", "ZPCIZdobTro","Pod107","Oznake definicija PDV-a iz kojih se prenosi vrijednost dobara tro.pos. u ZP");
    frmParam.getParam("sk", "ZPCIZusl","Pod104","Oznake definicija PDV-a iz kojih se prenosi vrijednost usluga u ZP");
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
 
    QueryDataSet stizvj = StIzvjPDV.getDataModule().getTempSet("CIZ like 'Pod%'");
    stizvj.open();
    for (stizvj.first(); stizvj.inBounds(); stizvj.next()) {
      HashSet filt = (HashSet)filters.get(stizvj.getString("CIZ"));
      if (filt == null) filt = new HashSet();
      filt.add(new colFilter(stizvj));
      filters.put(stizvj.getString("CIZ"), filt);
    }
    //query data
    
    // ova verzija je negdje 20 puta brža u gali... :)
    
    String kverchina = "SELECT uistavke.cknjige, uistavke.ckolone, uistavke.uraira, (uistavke.ID+uistavke.IP) as val " +
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
  
  private void recalcPDVSet(StorageDataSet mset) {
    izvjpdv.open();
    //StringTokenizer sums = new StringTokenizer("101+203+404+505", "+", true);
    for (izvjpdv.first(); izvjpdv.inBounds(); izvjpdv.next()) {
      if (!"".equals(izvjpdv.getString("PARAMETRI").trim()) && !izvjpdv.getString("CIZ").toLowerCase().trim().endsWith("p")) {
        String param = izvjpdv.getString("PARAMETRI").trim();
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
            String poz = "Pod"+tok;
            boolean located = lookupData.getlookupData().raLocate(mset, "POZ", poz);
            if (!located) {
              located = lookupData.getlookupData().raLocate(mset, "POZ", poz+"o");
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
        if (lookupData.getlookupData().raLocate(mset, "POZ", izvjpdv.getString("CIZ"))) {
          mset.setBigDecimal("OSN", osn);
          mset.setBigDecimal("PDV", pdv);
          mset.post();
        } else {
          System.err.println("Ne mogu naci "+izvjpdv.getString("CIZ")+" za snimanje sume");
        }
      }
    }
  }
  StorageDataSet setPDV = null;
  public StorageDataSet getSetPDV() {
    return setPDV;
  }
  private void doPDV() {
    try {
//      Object rPDVD = Class.forName("hr.restart.sk.repPDVDisk").newInstance();
//      HashMap data = (HashMap)rPDVD.getClass().getMethod("tijeloData", null).invoke(rPDVD, null);
      setPDV = new StorageDataSet();
      setPDV.addColumn(dM.createStringColumn("POZ","Pozicija",200));
      setPDV.addColumn(dM.createBigDecimalColumn("OSN", "Osnovica"));
      setPDV.addColumn(dM.createBigDecimalColumn("PDV", "PDV"));
      setPDV.open();
      //
      fillsetPDV(setPDV);
      recalcPDVSet(setPDV);
      recalcPDVSet(setPDV);
//      for (Iterator iterator = data.keySet().iterator(); iterator.hasNext();) {
//        String ciz = (String) iterator.next();
//        String poz;
//        boolean p = ciz.trim().endsWith("p");
//        if (p) {
//          poz = new VarStr(ciz).replaceLast("p", "o").toString();
//        } else {
//          poz = ciz;
//        }
//        if (!lookupData.getlookupData().raLocate(mset, "POZ", poz)) {
//          mset.insertRow(false);
//          mset.setString("POZ", poz);
//        }
//        mset.setBigDecimal(p?"PDV":"OSN", (BigDecimal)data.get(ciz));
//        mset.post();
//      }
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
      addReport("hr.restart.sk.repPDVDisk", "Datoteka PDV za e-poreznu");
      setTitle("Obrazac PDV za period "+raDateUtil.getraDateUtil().dataFormatter(getDatumOd())+" - "+raDateUtil.getraDateUtil().dataFormatter(getDatumDo()));
//      getJPTV().getColumnsBean().setSaveName("frmPDV2-PDV");
//      getJPTV().fireTableDataChanged();
    } catch (Exception e) {
      e.printStackTrace();
    }
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
  
  private class colFilter {
    private colFilter(ReadRow set) {
      ciz = set.getString("CIZ");
      cknjige = set.getString("CKNJIGE");
      ckolone = set.getShort("CKOLONE");
      uraira = set.getString("URAIRA");
    }
    private boolean pass(ReadRow uis) {
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
      
    default:
      break;
    }
  }

  private void updPDV_K() {
    // TODO Auto-generated method stub
    
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
    final JraDialog jdPDV = new JraDialog((JFrame)getWindow());
    JPanel jpPDV = new JPanel(new BorderLayout()); 
    JPanel jpcont = new JPanel();
    OKpanel okpan = new OKpanel() {
      
      public void jPrekid_actionPerformed() {
        setPDV.cancel();
        jdPDV.dispose();
      }
      
      public void jBOK_actionPerformed() {
        setPDV.post();
        int pos = setPDV.getRow();
        getJPTV().enableEvents(false);
        recalcPDVSet(setPDV);
        recalcPDVSet(setPDV);
//        frmPDV2.this.getJPTV().fireTableDataChanged();
        setPDV.goToRow(pos);
        getJPTV().enableEvents(true);
        jdPDV.dispose();
      }
    };
    XYLayout xyl = new XYLayout(270, 150);
    lookupData.getlookupData().raLocate(izvjpdv, "CIZ", setPDV.getString("POZ"));
    String capt = izvjpdv.getString("OPIS");
    JTextArea jta = new JTextArea(capt);
    jta.setLineWrap(true);
    jta.setWrapStyleWord(true);
    jta.setEditable(false);
    jta.setOpaque(false);
    
    JLabel jlOSN = new JLabel("Porezna osnovica");
    JLabel jlPDV = new JLabel("PDV");
    
    JraTextField jrOSN = new JraTextField();
    jrOSN.setDataSet(setPDV);
    jrOSN.setColumnName("OSN");

    JraTextField jrPDV = new JraTextField();
    jrPDV.setDataSet(setPDV);
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
