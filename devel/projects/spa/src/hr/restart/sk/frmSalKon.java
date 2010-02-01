/****license*****************************************************************
**   file: frmSalKon.java
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
import hr.restart.baza.Doku;
import hr.restart.baza.KnjigeUI;
import hr.restart.baza.KreirDrop;
import hr.restart.baza.Shkonta;
import hr.restart.baza.Sklad;
import hr.restart.baza.Skstavke;
import hr.restart.baza.Skstavkerad;
import hr.restart.baza.Urdok;
import hr.restart.baza.VTZtr;
import hr.restart.baza.Vrshemek;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.blpn.frmUplIspl;
import hr.restart.robno.TypeDoc;
import hr.restart.sisfun.Asql;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raCurrencyTableModifier;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;
import hr.restart.util.raTransaction;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.dlgGetKnjig;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;


public class frmSalKon extends raMasterDetail {
//  sysoutTEST sys = new sysoutTEST(false);
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  Util ut = Util.getUtil();
  lookupData ld = lookupData.getlookupData();

  jpSalKonMaster jpMaster;
  jpSalKonDetail jpDetail;

  public presSalKon pres;

  protected boolean okajac = false;
  
  private boolean allowVirt = false;
  
  public boolean ira;
  QueryDataSet shk = Shkonta.getDataModule().copyDataSet();
  private BigDecimal b1 = new BigDecimal(1);
  private BigDecimal b100 = new BigDecimal(100);
  private Timestamp dfrom, dto;

  private static frmSalKon fsk = null;

  private static final int RHU = BigDecimal.ROUND_HALF_UP;

  public String knj, vrdok, vrdokSheme;
  
  private String[] mkey = {"KNJIG", "CPAR", "VRDOK", "BROJDOK", "BROJIZV"};
  
  String defShema = null;
  
  private boolean urTransfer;
  boolean bookDependant, autoinc;
  int extSize;
  
  private DataRow saveRow;
  private String[] urVals;
  private String[] checkCols = {"CPAR", "BROJDOK", "OZNVAL"};
  private BigDecimal saveVal;
  private raTableModifier parModif;
  
  protected raNavAction rnaPot, rnaVir, rnaPop;
  
  public static frmSalKon getInstance() {
    return fsk;
  }
  
  private frmUplIspl uplIspl = null;
  /**
   * ako je fuplIspl != null stavlja ekran za unos u mod unosa kroz blagajnu 
   * i obrnuto
   * @param fuplIspl
   */
  public void setBlagajna(frmUplIspl fuplIspl) {
    uplIspl = fuplIspl;
    //set presel values
    setBLPresel();
  }
  public frmUplIspl getBlagajna() {
    return uplIspl;
  }
  private JraTextField jrtZiro = new JraTextField();
  private void setBLPresel() {
//    PreSelect BLSKPreSel = new PreSelect() {
//      
//    };
//    BLSKPreSel.setSelDataSet(getMasterSet());
//    JPanel _selPanel = new JPanel();
    jrtZiro.setColumnName("ZIRO");
//    _selPanel.add(jrtZiro);
//    BLSKPreSel.setSelPanel(_selPanel);
//    pres = BLSKPreSel;
//    setPreSelect(BLSKPreSel);
    pres.getSelPanel().add(jrtZiro, new XYConstraints(0,0,0,0));
    jrtZiro.setVisible(false);
    jrtZiro.setEnabled(false);
    pres.setSelPanel(pres.getSelPanel());
    jrtZiro.getDataSet().setString("ZIRO", uplIspl.getBLUID());
//    raNavAction[] rnvs = raMaster.getNavActions();
//    for (int i = 0; i < rnvs.length; i++) {
//      if (rnvs[i].getIdentifier().equals("Predselekcija")) {
//        rnvs[i].setEnabled(false);
//      }
//    }
    setBLPreselValues();
    System.out.println(pres.getSelRow());
    pres.doSelect();
  }

  private void setBLPreselValues() {
    //ira = false;
    pres.jrbUraira1.setSelected(true);
    pres.jrbUraira2.setSelected(false);
//    vrdok = "URN";
//    vrdokSheme = "URN";
//    knj = uplIspl.getUIKnjige();
    pres.getSelRow().setString("CKNJIGE",uplIspl.getUIKnjige());
//    dfrom = uplIspl.getDetailSet().getTimestamp("DATUM");
//    dto = uplIspl.getDetailSet().getTimestamp("DATUM");
    pres.getSelRow().setTimestamp("DATPRI-from",uplIspl.getDetailSet().getTimestamp("DATUM"));
    pres.getSelRow().setTimestamp("DATPRI-to",uplIspl.getDetailSet().getTimestamp("DATUM"));
//    allowVirt = false;
    pres.getSelRow().setString("VRDOK", "URN");
    pres.getSelRow().setString("KNJIG", dlgGetKnjig.getKNJCORG());
    pres.getSelRow().setInt("RBS", 1);
  }
  
  public frmSalKon() {
    super(1, 2);
    try {
      fsk = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void setPreselectValues() {
    if (uplIspl != null) {
      setBLPreselValues();
    } 
    ira = pres.jrbUraira2.isSelected();
    vrdok = ira ? pres.ival : ((presSalKon)pres).uval;
    vrdokSheme = ira ? "IRN" : "URN";
    knj = pres.getSelRow().getString("CKNJIGE");
    dfrom = pres.getSelRow().getTimestamp("DATPRI-from");
    dto = pres.getSelRow().getTimestamp("DATPRI-to");
    allowVirt = addPotvrdi() && KnjigeUI.getDataModule().getRowCount(
      Condition.equal("VIRTUA", "D").and(Condition.equal("URAIRA", ira ? "I" : "U"))) > 0;
    jpMaster.jbSelVirtual.setVisible(allowVirt);
    jpMaster.jbSelVirtual.setEnabled(allowVirt);
  }

  protected void setTitleMaster() {
    if (knj.length() == 0)
      this.setNaslovMaster((ira ? "Izlazni" : "Ulazni" ) + " ra\u010Duni svih knjiga");
    else
      this.setNaslovMaster((ira ? "Izlazni" : "Ulazni" ) + " ra\u010Duni knjige "+knj);
  }

  public void beforeShowMaster() {
//    String addenum = " AND rbs = 1";
//    String org = getMasterSet().getQuery().getQueryString();     
     System.out.println(getMasterSet().getQuery().getQueryString());
     setPreselectValues();
     findDefaultShema();
//     jpMaster.jraIznos.setColumnName(MDP());
//    if (org.toLowerCase().indexOf("where") == -1)
//      addenum = " WHERE rbs = 1";
//    System.out.println(org);
//    getMasterSet().close();
//    getMasterSet().setQuery(new QueryDescriptor(dm.getDatabase1(),
//      getMasterSet().getQuery().getQueryString() + addenum,
//      null, true, Load.ALL));
//    getMasterSet().open();

   jpMaster.setVrsk(vrdokSheme);
//   jpMaster.jlaDatknj.setVisible(!ira);
//   jpMaster.jraDatknj.setVisible(!ira);
//   jpMaster.jraDatknj.setEnabled(!ira);

   if (ira) {
      jpMaster.jlrCknjige.setRaDataSet(dm.getKnjigeI());
//      jpMaster.jraIznos.setColumnName("ID");
      jpDetail.jlrCkolone.setRaDataSet(dm.getIzlazneKolone());
      jpMaster.jpp.setPartnerKup();
//      jpMaster.jlrCpar.setRaDataSet(dm.getPartneriKup());
    } else {
      jpMaster.jlrCknjige.setRaDataSet(dm.getKnjigeU());
//      jpMaster.jraIznos.setColumnName("IP");
      jpDetail.jlrCkolone.setRaDataSet(dm.getUlazneKolone());
//      jpMaster.jlrCpar.setRaDataSet(dm.getPartneriDob());
      jpMaster.jpp.setPartnerDob();
    }
    jpMaster.jraIznos.setColumnName("SALDO");
    setTitleMaster();
    setExtParams();
  }

  protected void setTitleDetail() {
    setNaslovDetail("Stavke ra\u010Duna "+this.getMasterSet().getString("BROJDOK")+" od "+
    hr.restart.robno.raDateUtil.getraDateUtil().dataFormatter(this.getMasterSet().getTimestamp("DATDOK")));
  }
  
  public void refilterDetailSet() {
    super.refilterDetailSet();
    setTitleDetail();
    Shkonta.getDataModule().setFilter(shk, "vrdok = '"+vrdokSheme+
        "' AND cskl IN ('0','00','000','0000','"+getMasterSet().getString("CSKL")+"')");
//    shk.close();
//    if (!getMasterSet().getString("CSKL").equals(""))
//      cskl = "cskl = '"+getMasterSet().getString("CSKL")+"' AND ";
//    shk.setQuery(new QueryDescriptor(dm.getDatabase1(),
//        "SELECT * FROM shkonta WHERE "+cskl+
//        "vrdok = '"+getMasterSet().getString("VRDOK")+"'"));
//    shk = hr.restart.sisfun.Asql.getStavkeShkonta("sk", getMasterSet().getString("CSKL"), getMasterSet().getString("VRDOK"));
    shk.open();
//    shk.refresh();
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jpMaster.jlrCknjige, false);
      rcc.setLabelLaF(jpMaster.jlrNazknjige, false);
      rcc.setLabelLaF(jpMaster.jbSelCknjige, false);
//      rcc.setLabelLaF(jpMaster.jlrCpar, false);
//      rcc.setLabelLaF(jpMaster.jlrNazpar, false);
//      rcc.setLabelLaF(jpMaster.jbSelCpar, false);
//      rcc.EnabDisabAll(jpMaster.jpo, false); //ai:17.02.2005
//      rcc.setLabelLaF(jpMaster.jlrCorg, false);
//      rcc.setLabelLaF(jpMaster.jlrNaziv, false);
//      rcc.setLabelLaF(jpMaster.jbSelCorg, false);
      rcc.setLabelLaF(jpMaster.jlrCskl, false);
      rcc.setLabelLaF(jpMaster.jbSelCskl, false);
//      rcc.setLabelLaF(jpMaster.jraBrojdok, false);      
      jpMaster.jpgetval.setValutaEditable(false);
      jpMaster.jpgetval.initJP('I');
    }
    if (allowVirt) jpMaster.jbSelVirtual.setEnabled(mode == 'N');
  }

  int oldCpar, newCpar;
  String oldBrojdok, newBrojdok, oldUuid,  oldExt, seqKey;
  Condition oldCond;
  
  public void SetFokusMaster(char mode) {
    saveRow = null;
    seqKey = null;
    if (getMasterSet().getString("CGKSTAVKE").length() == 0)
      getMasterSet().setString("CGKSTAVKE", "D");
    Timestamp td = Util.getUtil().getToday(dfrom, dto);
    if (mode != 'I') {
      jpMaster.jpgetval.setValutaEditable(mode == 'N');
      jpMaster.jpgetval.initJP(mode);
    }
    if (mode == 'N') {
      pres.copySelValues();
      // zbog buga
      this.getMasterSet().setString("URAIRA", ira ? "I" : "U");

/*      sys.prn(pres.getSelRow());
      System.out.println(this.getMasterSet().getString("URAIRA")); */
      jpMaster.jpo.setCorg(setDefaultCorg()?getMasterSet().getString("KNJIG"):"");
      
//      this.getMasterSet().setString("CORG", getMasterSet().getString("KNJIG"));
//      jpMaster.jlrCorg.forceFocLost();
      jpMaster.jpp.setCpar("");
//      jpMaster.jlrCpar.forceFocLost();
      if (!knj.equals("")) {
        jpMaster.jlrCknjige.forceFocLost();
        afterKnjig();
        rcc.setLabelLaF(jpMaster.jlrCknjige, false);
        rcc.setLabelLaF(jpMaster.jlrNazknjige, false);
        rcc.setLabelLaF(jpMaster.jbSelCknjige, false);
        if (setDefaultCorg()) {
          jpMaster.jpp.focusCpar();
        } else {
          jpMaster.jpo.corg.requestFocusLater();
        }
//        jpMaster.jlrCpar.requestFocus();
      } else {
        jpMaster.jlrCknjige.forceFocLost();
        jpMaster.jlrCknjige.requestFocus();
      }
//      this.getMasterSet().setString("VRDOK", "KRN");
      this.getMasterSet().setShort("STAVKA", (short) 1);
      this.getMasterSet().setTimestamp("DATDOK", td);
      datdokChanged();
      getMasterSet().setUnassignedNull("DATPRI");
      //this.getMasterSet().setTimestamp("DATPRI", td);
      this.getMasterSet().setInt("BROJIZV", 0);
      this.getMasterSet().setString("EXTBRDOK", "");
      jpMaster.jraIznos.setColumnName("SALDO");
      this.getMasterSet().setString("DUGPOT",
        this.getMasterSet().getString("URAIRA").equals("U") ? "P" : "D");
      fillExtBrdok();
      jpMaster.setEdit(true);
      if (knj.length() > 0) jpMaster.setKonto();
    } else if (mode == 'I') {
      oldCpar = getMasterSet().getInt("CPAR");
      oldBrojdok = getMasterSet().getString("BROJDOK");
      oldCond = Condition.whereAllEqual(mkey, getMasterSet());
      jpMaster.jraIznos.requestFocus();
    }
  }
  
  private boolean setDefaultCorg() {
     return frmParam.getParam("sk","defcorgUIRN","D","Da nudi knjigovodstvo kao defaultni corg pri unosu URA/IRA").equals("D");
  }
  
  void setExtParams() {
	autoinc = frmParam.getParam("sk", "autoIncExt", "D", 
      "Automatsko poveæavanje dodatnog broja URA/IRA (D/N)").equalsIgnoreCase("D");
    bookDependant = frmParam.getParam("sk", "extKnjiga", "D", 
        "Ima li svaka knjiga zaseban brojaè (D/N)").equalsIgnoreCase("D");
    extSize = Aus.getNumber(frmParam.getParam("sk", "extSize", "0",
        "Minimalna velicina broja URA/IRA (popunjavanje vedeæim nulama)"));
    if (extSize > 8) extSize = 8;
  }
  
  public void fillExtBrdok() {
    if (autoinc) {
      if (bookDependant && getMasterSet().getString("CKNJIGE").length() == 0) return;
      
      int next = Valid.getValid().findSeqInt(seqKey = 
    	  	  OrgStr.getKNJCORG(false) + (ira ? "IRA-" : "URA-") +
    		  ut.getYear(getMasterSet().getTimestamp("DATDOK")) +
    		  (bookDependant ? "-" + getMasterSet().getString("CKNJIGE") : ""), 
    		  false, false);
      
      if (next > 1) {
    	  String result = Integer.toString(next);
          if (result.length() < extSize) 
            result = Aus.string(extSize - result.length(), '0') + result;
          getMasterSet().setString("EXTBRDOK", result);
          return;
      }
      
      Condition cond = Aus.getKnjigCond().and(Aus.getVrdokCond(ira, true)).
         and(Aus.getYearCond("DATUMKNJ", ut.getYear(getMasterSet().getTimestamp("DATDOK")))).
         and(Condition.anyString("CKNJIGE"));
      Condition condrad = Aus.getKnjigCond().and(Aus.getVrdokCond(ira)).
        and(Aus.getYearCond("DATPRI", ut.getYear(getMasterSet().getTimestamp("DATDOK"))));
      if (bookDependant) {
        cond = cond.and(Condition.equal("CKNJIGE", getMasterSet()));
        condrad = condrad.and(Condition.equal("CKNJIGE", getMasterSet()));
      }
      
      int maxExt = 0;      
      DataSet sk = Skstavke.getDataModule().getTempSet("EXTBRDOK", cond);
      sk.open();
      for (sk.first(); sk.inBounds(); sk.next()) {
        int num = Aus.getNumber(sk.getString("EXTBRDOK"));
        if (num > maxExt) maxExt = num;
      }
      DataSet skr = Skstavkerad.getDataModule().getTempSet("EXTBRDOK", condrad);
      skr.open();
      for (skr.first(); skr.inBounds(); skr.next()) {
        int num = Aus.getNumber(skr.getString("EXTBRDOK"));
        if (num > maxExt) maxExt = num;
      }
      String result = Integer.toString(maxExt + 1);
      if (result.length() < extSize) 
        result = Aus.string(extSize - result.length(), '0') + result;
      getMasterSet().setString("EXTBRDOK", result);
    }
  }

  protected boolean checkIznosMaster() {
    if (!jpMaster.jcbKnj.isSelected()) return true;
    if (getMasterSet().getBigDecimal("SALDO").signum() <= 0) {
      jpMaster.jraIznos.requestFocus();
      JOptionPane.showMessageDialog(jpMaster, "Pogrešan iznos ra\u010Duna!", "Greška",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  
  boolean checkValuta(DataSet ds) {
    if (!raSaldaKonti.isDomVal(ds) && !ld.raLocate(
    		dm.getValute(), "OZNVAL", ds.getString("OZNVAL"))) {
      JOptionPane.showMessageDialog(jpMaster, "Valuta nije definirana!",
          "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  
  void calcPValues() {
    this.getMasterSet().setBigDecimal("PVID", raSaldaKonti.n0);
    this.getMasterSet().setBigDecimal("PVIP", raSaldaKonti.n0);
    if (raSaldaKonti.isDomVal(getMasterSet())) {
      this.getMasterSet().setBigDecimal("PV" + MDP(), getMasterSet().getBigDecimal("SALDO"));
    } else {
      ld.raLocate(dm.getValute(), "OZNVAL", getMasterSet().getString("OZNVAL"));
      BigDecimal jedval = new BigDecimal(dm.getValute().getInt("JEDVAL"));
      this.getMasterSet().setBigDecimal("PV" + MDP(),
        getMasterSet().getBigDecimal("SALDO").multiply(getMasterSet().getBigDecimal("TECAJ")).
        divide(jedval, 2, RHU));
    }
  }
  
  protected int getBrojizv() {
    return 0;
  }
  
  public static boolean docExists(raMasterDetail md, jpSalKonMaster pan) {
    String[] skey = {"KNJIG", "CPAR", "VRDOK", "BROJDOK", "BROJIZV"};
    if (Skstavkerad.getDataModule().getRowCount(Condition.whereAllEqual(
        skey, md.getMasterSet())) > 0) {
      pan.jraBrojdok.requestFocus();
      JOptionPane.showMessageDialog(pan, "Dokument istog broja za ovog partnera ve\u0107 postoji!", "Greška",
        JOptionPane.ERROR_MESSAGE);
      return true;
    }
    return false;
  }
  
  boolean docExists() {
    return docExists(this, jpMaster);
  }
  
  public static boolean docExistsInArh(raMasterDetail md, jpSalKonMaster pan) {
    String[] skey = {"KNJIG", "CPAR", "VRDOK", "BROJDOK", "BROJIZV"};
    if (Skstavke.getDataModule().getRowCount(Condition.whereAllEqual(
        skey, md.getMasterSet())) > 0) {
      pan.jraBrojdok.requestFocus();
      JOptionPane.showMessageDialog(pan, 
          "Dokument istog broja za ovog partnera veæ postoji u arhivi!", "Greška",
        JOptionPane.ERROR_MESSAGE);
      return true;
    }
    return false;
  }
  
  boolean docExistsInArh() {
    return docExistsInArh(this, jpMaster);
  }
  
  static boolean isKnjigOK(DataSet ds) {
    if (TypeDoc.getTypeDoc().isDocOJ(ds.getString("VRDOK"))) {
      String pripadknjig = OrgStr.getOrgStr().getPripKnjig(
            ds.getString("CSKL"));
      if (pripadknjig.equalsIgnoreCase(OrgStr.getKNJCORG(false)))
        return true;
    } else {
      DataSet sklad =Sklad.getDataModule().getTempSet(
            "cskl='" + ds.getString("CSKL") + "'");
      sklad.open();
      String pripadknjig = OrgStr.getOrgStr().getPripKnjig(sklad.getString("KNJIG"));
      if (pripadknjig.equalsIgnoreCase(OrgStr.getKNJCORG(true)))
        return true;
    }
    return false;
  }

  static boolean checkModule(KreirDrop module, String docOpis, String brdokcol,
      raMasterDetail md, jpSalKonMaster pan) {
    int cpar = md.getMasterSet().getInt("CPAR");
    String brdok = md.getMasterSet().getString("BROJDOK");
    DataSet ds = module.getTempSet("CSKL GOD VRDOK BRDOK",
        "cpar=" + cpar + " and "+brdokcol+"='" + brdok + "'");
    ds.open();
    for (ds.first(); ds.inBounds(); ds.next())
      if (isKnjigOK(ds)) {
        pan.jraBrojdok.requestFocus();
        JOptionPane.showMessageDialog(pan, "Dokument istog broja za ovog partnera veæ postoji u robnom knjigovodstvu!" +
            "\n"+docOpis+": "+ds.getString("VRDOK")+" - "+ds.getString("CSKL").trim()+" / "+
            ds.getString("GOD")+" - "+ds.getInt("BRDOK"), "Greška",JOptionPane.ERROR_MESSAGE);
        return true;
      }
    return false;
  }
  
  public static boolean docExistsInRobno(raMasterDetail md, jpSalKonMaster pan) {
    if (raVrdokMatcher.isUplataTip(md.getMasterSet())) return false;
    if (raVrdokMatcher.isKup(md.getMasterSet())) 
      return checkModule(doki.getDataModule(), "Izlazni dokument", "pnbz2", md, pan);
    if (checkModule(Doku.getDataModule(), "Ulazni dokument", "brrac", md, pan)) return true;
    if (checkModule(VTZtr.getDataModule(), "Zavisni trošak ulaznog dokumenta", "brrac", md, pan)) return true;
    return false;
  }
  
  boolean docExistsInRobno() {
    return docExistsInRobno(this, jpMaster);
  }
  
  
  public boolean isKnjigVirtual() {
    DataSet ds = KnjigeUI.getDataModule().getTempSet("VIRTUA",
        Condition.whereAllEqual(new String[] {"CKNJIGE", "URAIRA"}, getMasterSet()));
    ds.open();
    return ds.rowCount() > 0 && ds.getString("VIRTUA").equalsIgnoreCase("D");
  }

  boolean virtual;
  public boolean ValidacijaMaster(char mode) {
    getMasterSet().setInt("BROJIZV", getBrojizv()); // hack za odvajanje ur od sk
    urTransfer = saveRow != null;
    virtual = isKnjigVirtual();
    if (vl.isEmpty(jpMaster.jlrCknjige) || !jpMaster.jpp.Validacija()
        || !jpMaster.jpo.Validacija() || vl.isEmpty(jpMaster.jraBrojdok)
        || vl.isEmpty(jpMaster.jraDatdok) || vl.isEmpty(jpMaster.jraDatdosp)
        || (vl.isEmpty(jpMaster.jlrCskl) && !virtual) || vl.isEmpty(jpMaster.jraDatknj))
      return false;
    if (!Aus.checkDatAndDosp(jpMaster.jraDatdok, jpMaster.jraDatdosp)) return false;
    if (!Aus.checkSanityRange(jpMaster.jraDatknj)) return false;
    if (getMasterSet().getTimestamp("DATPRI").before(Util.getUtil().getFirstSecondOfDay(getMasterSet().getTimestamp("DATDOK")))) {
    	jpMaster.jraDatknj.requestFocus();
      JOptionPane.showMessageDialog(raMaster.getWindow(),
        "Datum primitka je ispred datuma dokumenta!",
        "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    if (!virtual && !checkIznosMaster()) return false;
    if (mode == 'N' && !virtual) {
      if (saveRow != null && !checkSaveRow()) return false;
      
      if (docExists() || docExistsInArh() || docExistsInRobno())
        return false;

      vl.execSQL("SELECT * FROM shkonta WHERE cskl = '"+getMasterSet().getString("CSKL")+
                 "' AND vrdok = '"+vrdokSheme+"' ORDER BY stavka");
      vl.RezSet.open();
      if (vl.RezSet.rowCount() == 0) {
        jpMaster.jlrCskl.requestFocus();
        JOptionPane.showMessageDialog(jpMaster, "Shema knjiženja nije definirana!", "Greška",
          JOptionPane.ERROR_MESSAGE);
        return false;
      }
      vl.RezSet.first();
      if (vl.RezSet.getShort("STAVKA") != 1) {
        jpMaster.jlrCskl.requestFocus();
        JOptionPane.showMessageDialog(jpMaster, "Neispravna shema knjiženja! (ne postoji stavka 1)",
          "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (!raSaldaKonti.isDirect())
        this.getMasterSet().setString("BROJKONTA", vl.RezSet.getString("BROJKONTA"));
//      this.getMasterSet().setString("DUGPOT", vl.RezSet.getString("KARAKTERISTIKA"));
      
      if (isR2CheckKnjig() &&
          !ira && ld.raLocate(dm.getPartneri(), "CPAR",
    		  Integer.toString(getMasterSet().getInt("CPAR")))) {
        boolean r2 = R2Handler.isR2Shema(getMasterSet().getString("CSKL"), vrdokSheme);
    	boolean r2p = dm.getPartneri().getString("OBRT").equalsIgnoreCase("D");
    	if (r2 != r2p) {
    		int r = JOptionPane.showConfirmDialog(raMaster.getWindow(),
    		    "Shema knjiženja " + (r2 ? "je" : "nije") + " R2, a partner se "+
    		    (r2p ? "" : "ne ") + "vodi kao (R2) obrtnik!\n" +
    		    (r2 ? "Oznaèiti partnera kao R2" : "Poništiti oznaku R2 na partneru") +
    		    " i nastaviti, ili prekinuti?", "R2 shema",
    		    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    		if (r != JOptionPane.OK_OPTION) return false;
    		
    		dm.getPartneri().setString("OBRT", r2 ? "D" : "N");
    		dm.getPartneri().saveChanges();
    	}
      }
    }
    if (mode == 'N') this.getMasterSet().setTimestamp("DATUNOS", vl.getToday());
    // VAZNO!! Za IRE DATPRI je uvijek isti kao DATDOK
    //if (ira) getMasterSet().setTimestamp("DATPRI", getMasterSet().getTimestamp("DATDOK"));

    this.getMasterSet().setBigDecimal("ID", raSaldaKonti.n0);
    this.getMasterSet().setBigDecimal("IP", raSaldaKonti.n0);
    this.getMasterSet().setBigDecimal(MDP(), this.getMasterSet().getBigDecimal("SALDO"));
    if (!checkValuta(getMasterSet())) return false; 
    calcPValues();
//    this.getMasterSet().setBigDecimal("SALDO", this.getMasterSet().getBigDecimal(MDP()));
//    this.getMasterSet().setBigDecimal("SALDO", this.getMasterSet().getBigDecimal("ID").
//          subtract(this.getMasterSet().getBigDecimal("IP")));
//    refilterDetailSet();
    newCpar = getMasterSet().getInt("CPAR");
    newBrojdok = getMasterSet().getString("BROJDOK");
    return true;
  }
  //false ako ne treba checkirati R2 flag na partneru za to knjigovodstvo
  private boolean isR2CheckKnjig() {
    return frmParam.getParam("sk", "r2check"+dlgGetKnjig.getKNJCORG(), "D", "Provjeravati R2 flag na partneru za knjigovodstvo "+dlgGetKnjig.getKNJCORG()).equalsIgnoreCase("D");
  }
  public boolean isNewDetailNeeded() {
    return false;
  }

  public void afterSetModeMaster(char bef, char aft) {
    if (aft == 'B') {
      jpMaster.setEdit(false);
      jpMaster.jpgetval.disableDohvat();
    }
  }

  public void afterSetModeDetail(char bef, char aft) {
    if (aft == 'B') jpDetail.setEdit(false);
  }

  String delsql;

//  public boolean DeleteCheckMaster() {
//    delsql = this.getDetailSet().getQuery().getQueryString();
//    delsql = "DELETE " + delsql.substring(delsql.toLowerCase().indexOf("from"));
//    return true;
//  }
//  public void AfterDeleteMaster() {
//    vl.runSQL(delsql);
//  }
  
  public boolean doBeforeSaveMaster(char mode) {
    if (mode == 'N' && urTransfer) {
      getMasterSet().setString("URUUID", urVals[0]);
    }
    return true;
  }

  public boolean doWithSaveMaster(char mode) {
    try {
//      this.getDetailSet().open();
      if (mode == 'B') {
        for (getDetailSet().first(); getDetailSet().inBounds();) {
          if (getDetailSet().getInt("RBS") == 1) getDetailSet().next();
          else getDetailSet().deleteRow();
        }
//      } else {
//        if (mode == 'N') fillStavkeSheme();
//        calcAllVeze((short) 1);
        raTransaction.saveChanges(this.getDetailSet());
        if (allowVirt) {
          if (oldUuid != null && oldUuid.length() > 0)
            raTransaction.runSQL("UPDATE urdok SET statknj='N' WHERE uuid = '" + oldUuid + "'");
            /*raTransaction.runSQL("UPDATE skstavkerad SET pokriveno='D' WHERE "+
              Aus.getKnjigCond() + " AND rbs = 1 AND vrdok = '" + vrdok + 
              "' AND brojdok = '" + oldBrojdok + "' AND cpar = " + oldCpar +
              " AND brojizv != "+getBrojizv());*/
        }
        if (autoinc && seqKey != null) {
          int next = Valid.getValid().findSeqInt(seqKey, false, false);
      	  int ext = Aus.getNumber(oldExt);
      	  if (ext == next - 1) {
      		dm.getSeq().setDouble("BROJ", ext - 1);
      		raTransaction.saveChanges(dm.getSeq());
      	  }
        }
      }
      if (mode == 'I') {
        if (oldCpar != newCpar || !oldBrojdok.equals(newBrojdok)) {
          QueryDataSet chg = Skstavkerad.getDataModule().getTempSet(oldCond);
          chg.open();
          for (chg.first(); chg.inBounds(); chg.next()) {
            if (chg.getInt("RBS") == 1) continue;
            if (oldCpar != newCpar) chg.setInt("CPAR", newCpar);
            if (!oldBrojdok.equals(newBrojdok)) chg.setString("BROJDOK", newBrojdok);
          }
          raTransaction.saveChanges(chg);
          boolean eq = raSaldaKonti.getDokSaldo(Skstavkerad.getDataModule().getTempSet(
              Condition.whereAllEqual(mkey, getMasterSet()))).signum() == 0;
          getMasterSet().setString("POKRIVENO", eq ? "D" : "N");
          raTransaction.saveChanges(getMasterSet());
        }
      }
      if (mode == 'N' && urTransfer) {
        raTransaction.runSQL("UPDATE urdok SET statknj='D' WHERE uuid = '" + urVals[0] + "'");
        /*raTransaction.runSQL("UPDATE skstavkerad SET pokriveno='K' WHERE " +
           Aus.getKnjigCond() + " AND rbs = 1 AND vrdok = '" + urVals[0] + 
           "' AND brojdok = '" + urVals[2] + "' AND cpar = " + urVals[1] +
           " AND brojizv != "+getBrojizv());*/
      }
//      sysoutTEST sys  =  new sysoutTEST(false);
//      sys.prn(this.getDetailSet());
      if (mode == 'N' && seqKey != null) {
    	  int next = Valid.getValid().findSeqInt(seqKey, false, false);
    	  int ext = Aus.getNumber(getMasterSet().getString("EXTBRDOK"));
    	  if (ext >= next) {
    		dm.getSeq().setDouble("BROJ", ext);
    		raTransaction.saveChanges(dm.getSeq());
    	  }
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean alreadyInside = false;
  public void AfterAfterSaveMaster(char mode) {
    if (!alreadyInside) {
      alreadyInside = true;
      super.AfterAfterSaveMaster(mode);
//      this.getDetailSet().refresh();
      if (mode == 'N') fillStavkeSheme();
      else {
        refilterDetailSet();
        getDetailSet().refresh();
      }
      if (mode == 'N') calcAllVeze((short) 1); //ai: ako sheme veze ne odgovaraju vec unesenim podacima da ne sjebe brojke
      this.getDetailSet().saveChanges();
      alreadyInside = false;
    } else super.AfterAfterSaveMaster(mode);
    alreadyInside = false;
  }
  
  /*private boolean checkBal(boolean refilter) {
    if (refilter) {
      this.refilterDetailSet();
      getDetailSet().refresh();
    }
    return raSaldaKonti.getDokSaldo(getDetailSet()).signum() == 0;
  }*/
  
  private BigDecimal getBalance() {
    return raSaldaKonti.getDokSaldo(Skstavkerad.getDataModule().getTempSet("ID IP", 
        Condition.whereAllEqual(mkey, getMasterSet())));
  }
  
  private void updateBalance() {
    int row = getDetailSet().getInt("RBS");
    raDetail.getJpTableView().enableEvents(false);
    this.refilterDetailSet();
    getDetailSet().refresh();
    BigDecimal diff = raSaldaKonti.getDokSaldo(getDetailSet());
    ld.raLocate(getDetailSet(), "RBS", Integer.toString(row));
    //getDetailSet().goToRow(row);
    raDetail.getJpTableView().enableEvents(true);
    if (diff.signum() == 0) {
      JOptionPane.showMessageDialog(getWindow(), "Raèun je veæ u balansu!",
        "Upozorenje", JOptionPane.INFORMATION_MESSAGE); 
      return;
    }
    if (this.getDetailSet().getInt("RBS") == 1) {
      JOptionPane.showMessageDialog(getWindow(), "Popravak se ne može izvesti na prvoj stavci!",
          "Greška", JOptionPane.ERROR_MESSAGE); 
        return;
    }
    if (JOptionPane.showConfirmDialog(getWindow(), "Želite li popraviti razliku (" + diff +
            ") na ovoj stavci?",
       "Popravak", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.CANCEL_OPTION)
      return;
    if (getDetailSet().getBigDecimal("ID").signum() != 0)
      getDetailSet().setBigDecimal("ID", getDetailSet().getBigDecimal("ID").subtract(diff));
    else if (getDetailSet().getBigDecimal("IP").signum() != 0)
      getDetailSet().setBigDecimal("IP", getDetailSet().getBigDecimal("IP").add(diff));
    else {
      boolean id = true;
      boolean ip = true;
      String kar = jpDetail.jlrKarKonto.getText();    
      if (kar.equalsIgnoreCase("D")) ip = false;
      else if (kar.equalsIgnoreCase("P")) id = false;
      kar = jpDetail.jlrKarkolone.getText();
      if (kar.equalsIgnoreCase("D")) ip = false;
      else if (kar.equalsIgnoreCase("P")) id = false;
      
      if (!ip) getDetailSet().setBigDecimal("ID", diff.negate());
      else if (!id || ira) getDetailSet().setBigDecimal("IP", diff);
      else getDetailSet().setBigDecimal("ID", diff.negate());
    }
    getMasterSet().setString("POKRIVENO", "D");
    if (!raTransaction.saveChangesInTransaction(new QueryDataSet[] 
                         {getDetailSet(), getMasterSet()})) {
      JOptionPane.showMessageDialog(getWindow(), "Popravak nije uspio!",
          "Greška", JOptionPane.ERROR_MESSAGE); 
      getMasterSet().refetchRow(getMasterSet());
      getDetailSet().refresh();
    } else {
      raDetail.getJpTableView().fireTableDataChanged();
      raMaster.getJpTableView().fireTableDataChanged();
    }
  }

  boolean isEq;
  public boolean ValidacijaPrijeIzlazaDetail() {
    boolean enabde = !this.getMasterSet().getString("POKRIVENO").equalsIgnoreCase("K");
    if (!enabde) return true;
    BigDecimal sal = getBalance();
    if (!(isEq = (sal.signum() == 0)) &&
        JOptionPane.showConfirmDialog(this.getWindow(), "Dugovna i potražna strana ra\u010Duna " +
           "nisu u balansu! (ID - IP = " + sal + ")",
          "Greška", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION)
      return false;
    return true;
  }
  public void ZatvoriOstaloMaster() {
    if (uplIspl != null) {
      uplIspl.raDetail.setEnabled(true);
      BigDecimal sum = getBlagIznos();
      if (sum.signum() != 0) {
        uplIspl.getDetailSet().setBigDecimal("IZDATAK", sum);
      }
      uplIspl.containsURA(sum.signum() != 0);
      pres.getSelPanel().remove(jrtZiro);
      pres.setSelPanel(pres.getSelPanel());
      uplIspl = null;
    }
  }
  /**
   * Sumirati unesene racune vezane uz stavku blagajne
   * @return
   */
  public BigDecimal getBlagIznos() {
    return getBlagIznos(getMasterSet());
  }
  public static BigDecimal getBlagIznos(StorageDataSet masterSet) {
    BigDecimal ret = Aus.zero2;
    for (masterSet.first(); masterSet.inBounds(); masterSet.next()) {
      ret = ret.add(masterSet.getBigDecimal("SALDO"));
    }
    return ret;
  }

  public void ZatvoriOstaloDetail() {
    boolean enabde = !this.getMasterSet().getString("POKRIVENO").equalsIgnoreCase("K");
    if (!enabde) return;
//    int lastr = this.getMasterSet().getRow();
//    raMaster.getJpTableView().enableEvents(false);
//    this.getMasterSet().refresh();
//    this.getMasterSet().goToClosestRow(lastr);
//    raMaster.getJpTableView().enableEvents(true);
//    raMaster.getJpTableView().repaint();
   /* getDetailSet().first();*/
    getMasterSet().setString("POKRIVENO", isEq ? "D" : "N");
    /*getMasterSet().setBigDecimal("ID", raSaldaKonti.n0);
    getMasterSet().setBigDecimal("IP", raSaldaKonti.n0);
    getMasterSet().setBigDecimal(MDP(), getDetailSet().getBigDecimal(MDP()));
    calcPValues();
    getMasterSet().setShort("CKOLONE", getDetailSet().getShort("CKOLONE"));*/
    getMasterSet().saveChanges();
    raMaster.getJpTableView().fireTableDataChanged();
  }

  public void EntryPointDetail(char mode) {
    if (mode == 'I') {
      if (this.getDetailSet().getInt("RBS") == 1) {
        rcc.EnabDisabAll(jpDetail, false);
        rcc.setLabelLaF(jpDetail.jlrCkolone, true);
        rcc.setLabelLaF(jpDetail.jlrNazivkolone, true);
        rcc.setLabelLaF(jpDetail.jbSelCkolone, true);
        rcc.setLabelLaF(jpDetail.jraIznos, true);
//      } else {
//        rcc.setLabelLaF(jpDetail.jlrStavka, false);
//        rcc.setLabelLaF(jpDetail.jlrOpis, false);
//        rcc.setLabelLaF(jpDetail.jbSelStavka, false);
      }
    }
  }
  
  BigDecimal origIznos = raSaldaKonti.n0;

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      copyMasterFields();
      jpDetail.jlrCkolone.forceFocLost();
//      this.getDetailSet().setString("CORG", this.getMasterSet().getString("CORG"));
      jpDetail.jlrCorg.forceFocLost();
      jpDetail.jlrStavka.forceFocLost();
      if (raSaldaKonti.isDirect())
        jpDetail.jlrKonto.requestFocus();
      else jpDetail.jlrStavka.requestFocus();
      validateIznos();
    } else if (mode == 'I') {
      if (this.getDetailSet().getInt("RBS") == 1)
        jpDetail.jlrCkolone.requestFocus();
      else if (raSaldaKonti.isDirect())
        jpDetail.jlrKonto.requestFocus();
      else jpDetail.jlrStavka.requestFocus();
      validateIznos();
      origIznos = getDetailSet().getBigDecimal("ID").add(getDetailSet().getBigDecimal("IP"));
    }
    if (mode != 'B') jpDetail.setEdit(true);
  }

  protected boolean checkIznosDetail() {
    /*if (this.getDetailSet().getBigDecimal(DDP()).signum() <= 0) {
      jpDetail.jraIznos.requestFocus();
      JOptionPane.showMessageDialog(jpDetail.jraIznos, "Pogrešan iznos!", "Greška",
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }*/
    return true;
  }

  public boolean ValidacijaDetail(char mode) {
    if ((!jpDetail.jlrCkolone.getText().equals("0") && vl.isEmpty(jpDetail.jlrCkolone))) return false;
    if (!raSaldaKonti.isDirect() && vl.isEmpty(jpDetail.jlrStavka)) return false;    
    if (raSaldaKonti.isDirect() && 
        getDetailSet().getBigDecimal("ID").signum() != 0 &&
        getDetailSet().getBigDecimal("IP").signum() != 0) {
      jpDetail.jraDug.requestFocus();
      JOptionPane.showMessageDialog(this.getWindow(), "Samo jedna strana konta smije biti popunjena!",
          "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (raSaldaKonti.isDirect()) {
      if (getDetailSet().getBigDecimal("ID").signum() != 0)
        getDetailSet().setString("DUGPOT", "D");
      else getDetailSet().setString("DUGPOT", "P");
    }
    if (!isKnjigVirtual() && !checkIznosDetail()) return false;    
    System.out.println("RBS="+getDetailSet().getInt("RBS"));
    System.out.println("DUGPOT="+getDetailSet().getString("DUGPOT"));
    System.out.println(jpDetail.jraIznos.getColumnName());
    if (this.getDetailSet().getInt("RBS") != 1)
      this.getDetailSet().setBigDecimal("SALDO", this.getDetailSet().getBigDecimal("ID").
          subtract(this.getDetailSet().getBigDecimal("IP")));
    else {
      this.getDetailSet().setBigDecimal("SALDO", this.getDetailSet().getBigDecimal(MDP()));
    }
    if (mode == 'N') {
      String qstr = this.getDetailSet().getQuery().getQueryString();
      qstr = "SELECT MAX(rbs) AS rbs "+qstr.substring(qstr.indexOf("*") + 1);
      vl.execSQL(qstr);
      vl.RezSet.open();
      this.getDetailSet().setInt("RBS", vl.RezSet.getInt("RBS") + 1);
    }
    if (raSaldaKonti.isDirect()) {
      if (!findOrCreateShema()) return false;
    } else {
      getDetailSet().setString("BROJKONTA", raSaldaKonti.getKonto(getDetailSet()));
    }
    jpDetail.setEdit(false);
    return true;
  }

  private void updateMasterSet(char mode) {
    boolean eq = raSaldaKonti.getDokSaldo(Skstavkerad.getDataModule().getTempSet(
        Condition.whereAllEqual(mkey, getMasterSet()))).signum() == 0;
    getMasterSet().setString("POKRIVENO", eq ? "D" : "N");
    if (mode == 'I' && getDetailSet().getInt("RBS") == 1) {
      getMasterSet().setBigDecimal("ID", raSaldaKonti.n0);
      getMasterSet().setBigDecimal("IP", raSaldaKonti.n0);
      getMasterSet().setBigDecimal(MDP(), getDetailSet().getBigDecimal(MDP()));
      getMasterSet().setBigDecimal("SALDO", getDetailSet().getBigDecimal(MDP()));
      calcPValues();
      getMasterSet().setShort("CKOLONE", getDetailSet().getShort("CKOLONE"));
    }
  }
  
  public boolean doWithSaveDetail(char mode) {
    try {
      if (mode == 'B') {
        vl.recountDataSet(this.getDetailSet(), "RBS", oldrbs, false);
        raTransaction.saveChanges(this.getDetailSet());
      } else if (mode == 'I') {
        BigDecimal nowIznos = getDetailSet().getBigDecimal("ID").add(getDetailSet().getBigDecimal("IP"));
        if (nowIznos.compareTo(origIznos) != 0 && nowIznos.signum() != 0) {
          calcAllVeze(this.getDetailSet().getShort("STAVKA"));
          raTransaction.saveChanges(this.getDetailSet());
        }
      }
      updateMasterSet(mode);
      raTransaction.saveChanges(getMasterSet());
      raMaster.getJpTableView().fireTableDataChanged();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

//  public void AfterSaveDetail(char mode) {
//    calcAllVeze(this.getDetailSet().getShort("STAVKA"));
//    this.getDetailSet().refresh();
//  }

  public boolean DeleteCheckMaster() {
    oldCpar = getMasterSet().getInt("CPAR");
    oldBrojdok = getMasterSet().getString("BROJDOK");
    oldUuid = getMasterSet().getString("URUUID");
    seqKey = OrgStr.getKNJCORG(false) + (ira ? "IRA-" : "URA-") +
	  ut.getYear(getMasterSet().getTimestamp("DATPRI")) +
	  (bookDependant ? "-" + getMasterSet().getString("CKNJIGE") : "");
    oldExt = getMasterSet().getString("EXTBRDOK");
    return true;
  }
  
  
  
  private int oldrbs;
  public boolean DeleteCheckDetail() {
    oldrbs = this.getDetailSet().getInt("RBS");
    if (oldrbs == 1) {
      JOptionPane.showMessageDialog(this.getWindow(), "Nije moguæe brisati prvu stavku!", 
          "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public void afterGet_val() {
  }
  
  void validateIznos() {
    if (!raSaldaKonti.isDirect()) return;
    boolean id = true;
    boolean ip = true;
    String kar = jpDetail.jlrKarKonto.getText();    
    if (kar.equalsIgnoreCase("D")) ip = false;
    else if (kar.equalsIgnoreCase("P")) id = false;
    kar = jpDetail.jlrKarkolone.getText();
    if (kar.equalsIgnoreCase("D")) ip = false;
    else if (kar.equalsIgnoreCase("P")) id = false;
    
    if (getDetailSet().getInt("RBS") == 1) {
      if (ira) ip = false;
      else id = false;
    }
    
    if (!id) getDetailSet().setBigDecimal("ID", raSaldaKonti.n0);
    if (!ip) getDetailSet().setBigDecimal("IP", raSaldaKonti.n0);
    jpDetail.enableIznos(id, ip);
  }
  
  void findDefaultShema() {
    Condition shem = Condition.equal("APP", "sk").and(Condition.equal("VRDOK", vrdokSheme));
    DataSet vsh = Vrshemek.getDataModule().getTempSet(shem);
    vsh.open();
    
    defShema = null;
    for (vsh.first(); vsh.inBounds(); vsh.next()) {
      String cvsh = vsh.getString("CVRSK");
      if (cvsh.equals("0") || cvsh.equals("00") ||
          cvsh.equals("000") || cvsh.equals("0000"))
        defShema = cvsh;
    }
    System.out.println("Default shema: "+defShema);
  }
  
  boolean findOrCreateShema() {
    if (vl.isEmpty(jpDetail.jlrKonto)) return false;
    if (getDetailSet().getInt("RBS") == 1) return true;
    if (raSaldaKonti.getKonto(getDetailSet()).equals(getDetailSet().getString("BROJKONTA"))) {
      System.out.println("Konto je u redu");
      return true;
    }
    if (defShema == null) {
      JOptionPane.showMessageDialog(this.getWindow(), "Nije definirana default shema za dodatna konta!", 
          "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    Condition main = Condition.equal("APP", "sk").and(Condition.equal("VRDOK", vrdokSheme));
    Condition shem = Condition.equal("CSKL", defShema);    
    Condition konto = Condition.equal("BROJKONTA", getDetailSet().getString("BROJKONTA")).
       and(Condition.equal("KARAKTERISTIKA", getDetailSet().getString("DUGPOT")));
    
    DataSet shm = Shkonta.getDataModule().getTempSet(main.and(shem).and(konto));    
    shm.open();
    if (shm.getRowCount() == 0) {
      try {
        shm.insertRow(false);
        shm.setString("APP", "sk");
        shm.setString("VRDOK", vrdokSheme);
        shm.setString("CSKL", defShema);
        shm.setShort("STAVKA", (short) Asql.getNextRbs("SHKONTA", "STAVKA",
            Condition.equal("VRDOK", vrdokSheme). and(Condition.equal("CSKL", defShema))));
        
        shm.setString("BROJKONTA", getDetailSet().getString("BROJKONTA"));
        shm.setString("KARAKTERISTIKA", getDetailSet().getString("DUGPOT"));
        System.out.println("Dodajem novu stavku "+shm.getShort("STAVKA")+" za shemu "+defShema);                   
        String opis = getDetailSet().getString("BROJKONTA") + " - " + 
                        jpDetail.jlrNazKonto.getText();
        shm.setString("OPIS", opis.length() > 40 ? opis.substring(0, 40) : opis);
        shm.setString("POLJE", jpDetail.jlrCkolone.getText());        
        shm.setShort("CKOLONE", getDetailSet().getShort("CKOLONE"));
        shm.setString("CSKLUL", "0");
        shm.setString("CKNJIGE", knj);
        shm.saveChanges();        
        dm.getSynchronizer().markAsDirty("SHKONTA");
      } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this.getWindow(), "Greška prilikom automatskog kreiranja sheme!", 
            "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    System.out.println("Punim shemom "+defShema+" stavka "+shm.getShort("STAVKA"));
    getDetailSet().setString("CSKL", defShema);
    getDetailSet().setShort("STAVKA", shm.getShort("STAVKA"));
    return true;
  }
  
  public void afterKonto() {
    validateIznos();
  }
  
  public void afterKolone() {
    validateIznos();
  }

  public void afterStavka() {
    this.getDetailSet().setString("CSKL", jpDetail.jlrShCskl.getText());
    this.getDetailSet().setString("DUGPOT", jpDetail.jlrShKar.getText());
    System.out.println("set "+getDetailSet().getString("DUGPOT"));
    detailSet_navigated(null);
    try {
      this.getDetailSet().setShort("CKOLONE", Short.parseShort(jpDetail.jlrShPolje.getText()));
      jpDetail.jlrCkolone.forceFocLost();
    } catch (Exception e) {}
  }

  private String MDP() {
    if (ira) return "ID";
    else return "IP";
  }

  private String DDP() {
    if (getDetailSet().getInt("RBS") == 1) return MDP();
    else if (getDetailSet().getString("DUGPOT").equals("D")) return "ID";
    else return "IP";
  }
  
  /*
  public void masterSet_navigated(NavigationEvent e) {
    R2Handler.handleDatPrimitkaUI(jpMaster);
  }*/

  private void Proknjizi() {
    if (getBlagajna() != null || getMasterSet().getString("ZIRO").startsWith("BL#:")) {
      JOptionPane.showMessageDialog(getContentPane(), "Raèuni kroz blagajnu se potvrðuju zajedno sa blagajnièkim izvještajem");
      return;
    }
    if (getMasterSet().rowCount() == 0) return;
    BigDecimal sal = getBalance();
    if (sal.signum() != 0) {
      JOptionPane.showMessageDialog(raMaster.getWindow(), "Dugovna i potražna strana " +
          "raèuna nisu u balansu! (ID - IP = " + sal + ")",
        "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
//    raSaldaKonti.setKumInvalid();
    refilterDetailSet();
    String knjui = getMasterSet().getString("CKNJIGE");
    if (isKnjigVirtual()) {
      DataSet ds = KnjigeUI.getDataModule().getTempSet(
          "uraira = " + (ira ? "'I'" : "'U'") + 
          " AND (virtua is null OR virtua='' OR virtua!='D')");
      String[] result = ld.lookUp(raMaster.getWindow(), ds,
          new String[] {"CKNJIGE"}, new String[] {""}, new int[] {0, 4});
      if (result == null) return;
      for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next())
        getDetailSet().setString("CKNJIGE", result[0]);
      knjui = result[0];
    }
    
    int retval = raSaldaKonti.knjiziStavku(this.getDetailSet());
    if (retval != raSaldaKonti.OK) {
      String gr = "Neindeficirana greška!";
      if (retval == raSaldaKonti.DUPLICATE_KEY)
        gr = "Identi\u010Dna stavka je ve\u0107 potvr\u0111ena!";
      else if (retval == raSaldaKonti.NO_KONTO || retval == raSaldaKonti.NO_SHEMA) 
        gr = "Greška u kontnom planu!";
      else if (retval == raSaldaKonti.NO_VALUTA) gr = "Nepostojeæa valuta!";

      JOptionPane.showMessageDialog(raMaster.getWindow(), gr, "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (JOptionPane.showConfirmDialog(raMaster.getWindow(), "Želite li potvrditi ra\u010Dun "+
           getMasterSet().getString("BROJDOK")+" u knjigu " + knjui + "?", "Potvrda",
       JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;

    hr.restart.util.raLocalTransaction knjiz = new hr.restart.util.raLocalTransaction() {
      public boolean transaction() throws Exception {
        delsql = frmSalKon.this.getDetailSet().getQuery().getQueryString();
        delsql = "DELETE " + delsql.substring(delsql.toLowerCase().indexOf("from"));
        raTransaction.runSQL(delsql);
        raTransaction.saveChanges(dm.getSkstavke());
        raTransaction.saveChanges(dm.getUIstavke());
        //raTransaction.saveChanges(dm.getSkkumulativi());
        return true;
      }
    };
    if (!knjiz.execTransaction()) {
      JOptionPane.showMessageDialog(raMaster.getWindow(), "Transakcija nije uspjela!", "Greška",
        JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (raSaldaKonti.matchLast())
      JOptionPane.showMessageDialog(raMaster.getWindow(), "Ra\u010Dun potvr\u0111en i automatski pokriven.",
         "Poruka", JOptionPane.INFORMATION_MESSAGE);
    else JOptionPane.showMessageDialog(raMaster.getWindow(), "Ra\u010Dun potvr\u0111en.",
         "Poruka", JOptionPane.INFORMATION_MESSAGE);
//    if (JOptionPane.showConfirmDialog(raMaster.getWindow(), "Dokument proknjižen. Automatsko pokrivanje?", "Knjiženje",
//       JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) return;

    this.getMasterSet().refresh();
    raMaster.getJpTableView().fireTableDataChanged();
  }


  private void copyMasterFields() {
    String[] cols = new String[] {"KNJIG", "CPAR", "CSKL", "VRDOK", "BROJDOK", "CORG",
        "CKNJIGE", "URAIRA", "BROJIZV"};
    DataSet.copyTo(cols, this.getMasterSet(), cols, this.getDetailSet());
  }

  private void calcVeze(QueryDataSet veze, LinkedList todo) {
    short stavka = this.getDetailSet().getShort("STAVKA");
    BigDecimal iznos = getDetailSet().getBigDecimal(DDP());
//    if (stavka == (short) 1)
//      iznos = ira ? getMasterSet().getBigDecimal("ID") : getMasterSet().getBigDecimal("IP");
    System.out.println(stavka + " " + iznos);
//    new sysoutTEST(false).prn(getDetailSet());
    veze.first();
    while (veze.inBounds()) {
      if (veze.getShort("STAVKA1") == stavka) {
        this.getDetailSet().first();
        while (this.getDetailSet().inBounds()) {
          if (veze.getShort("STAVKA2") == this.getDetailSet().getShort("STAVKA")) {
            BigDecimal result;
            if ("D".equalsIgnoreCase(veze.getString("INVERTNA")))
              result = iznos.divide(veze.getBigDecimal("STOPA").divide(b100, 10, RHU).add(b1), 10, RHU);
            else result = iznos.multiply(veze.getBigDecimal("STOPA")).divide(b100, 10, RHU);

//            System.out.println(result);
            if ("D".equalsIgnoreCase(veze.getString("DODATNA")))
              result = result.multiply(veze.getBigDecimal("STOPA2")).divide(b100, 10, RHU);

            this.getDetailSet().setBigDecimal(DDP(), result.setScale(2, RHU));
            this.getDetailSet().setBigDecimal("SALDO", this.getDetailSet().getBigDecimal("ID").
              subtract(this.getDetailSet().getBigDecimal("IP")));
            todo.addLast(new Short(veze.getShort("STAVKA2")));
          }
          this.getDetailSet().next();
        }
      }
      veze.next();
    }
//    new sysoutTEST(false).prn(getDetailSet());
  }

  public void recalculateEverything() {
    for (getMasterSet().first(); getMasterSet().inBounds(); getMasterSet().next()) {
      refilterDetailSet();
      calcAllVeze((short) 1);
      getDetailSet().saveChanges();
      if (getBalance().signum() != 0) {
        getMasterSet().setString("POKRIVENO", "N");
        BigDecimal id = raSaldaKonti.n0;
        BigDecimal ip = raSaldaKonti.n0;
        int numz = 0;
        for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next()) {
          if (getDetailSet().getShort("STAVKA") == 1 ||
              getDetailSet().getShort("STAVKA") >= 10) {
            id = id.add(getDetailSet().getBigDecimal("ID"));
            ip = ip.add(getDetailSet().getBigDecimal("IP"));
            if (getDetailSet().getShort("STAVKA") == 1 && getDetailSet().getBigDecimal("ID").
                add(getDetailSet().getBigDecimal("IP")).signum() == 0) numz += 2;
          } else numz++;
        }
        System.out.println(numz);
        if (numz == 1) {
          for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next())
            if (getDetailSet().getShort("STAVKA") > 1 &&
              getDetailSet().getShort("STAVKA") < 10) {
              getDetailSet().setBigDecimal(id.compareTo(ip) > 0 ? "IP" : "ID", id.subtract(ip).abs());
              getDetailSet().saveChanges();
              getMasterSet().setString("POKRIVENO", "D");
              break;
            }
        }
      } else getMasterSet().setString("POKRIVENO", "D");
    }
    this.getMasterSet().saveChanges();
  }

  private void calcAllVeze(short source) {
    short stavka;
    LinkedList todo = new LinkedList();

    vl.execSQL("SELECT * FROM shemevezeui WHERE cskl = '"+getMasterSet().getString("CSKL")+
               "' AND vrdok = '"+vrdokSheme+"'");
    vl.RezSet.open();
    int row = this.getDetailSet().getRow();
//    this.getDetailSet().enableDataSetEvents(false);

    todo.add(new Short(source));
    while (!todo.isEmpty()) {
      stavka = ((Short) todo.removeFirst()).shortValue();
      this.getDetailSet().first();
      while (this.getDetailSet().inBounds()) {
        if (this.getDetailSet().getShort("STAVKA") == stavka) {
          calcVeze(vl.RezSet, todo);
          break;
        }
        this.getDetailSet().next();
      }
    }
    this.getDetailSet().goToRow(row);
//    this.getDetailSet().enableDataSetEvents(true);
//    this.getDetailSet().saveChanges();
  }

  private void fillStavkeSheme() {
    int rbs = 1;

//    this.getDetailSet().open();

    vl.execSQL("SELECT * FROM shkonta WHERE cskl = '"+getMasterSet().getString("CSKL")+
               "' AND vrdok = '"+vrdokSheme+"' ORDER BY stavka");
    vl.RezSet.open();
    if (vl.RezSet.rowCount() == 0) return;
    vl.RezSet.first();
    while (vl.RezSet.inBounds()) {
      if (R2Handler.isShemaToUIStavka(vl.RezSet)) {//da li shema ima kolonu za R2, ako ima nemoj je dodati u stavke (Track+issue 24)
	      if (vl.RezSet.getShort("STAVKA") != 1) {
	        this.getDetailSet().insertRow(false);
	        copyMasterFields();
	        this.getDetailSet().setInt("RBS", ++rbs);
	        this.getDetailSet().setShort("STAVKA", vl.RezSet.getShort("STAVKA"));
	        this.getDetailSet().setString("CSKL", vl.RezSet.getString("CSKL"));
	        this.getDetailSet().setString("DUGPOT", vl.RezSet.getString("KARAKTERISTIKA"));
	        this.getDetailSet().setString("BROJKONTA", vl.RezSet.getString("BROJKONTA"));
	      }
	      short ckolone = 0;
	        try {
	          ckolone = Short.parseShort(vl.RezSet.getString("POLJE"));
	        } catch (Exception e) {}
	        this.getDetailSet().setShort("CKOLONE", ckolone);
      }
      vl.RezSet.next();
    }
    this.getDetailSet().post();
//    calcAllVeze((short) 1);
  }

  public void PartnerChangeD() {
    if (raMaster.getMode() != 'N') return;
    if (ld.raLocate(dm.getPartneri(), "CPAR",
        String.valueOf(this.getMasterSet().getInt("CPAR")))) {

        this.getMasterSet().setTimestamp("DATDOSP", new Timestamp(
           hr.restart.robno.raDateUtil.getraDateUtil().addDate(
             this.getMasterSet().getTimestamp("DATDOK"), dm.getPartneri().getShort("DOSP")
           ).getTime()));
    } else getMasterSet().setTimestamp("DATDOSP", getMasterSet().getTimestamp("DATDOK")); 
  }

  public void afterKnjig() {
    if (raMaster.getMode() == 'N' && getMasterSet().getString("CSKL").length() == 0) {
      this.getMasterSet().setString("CSKL", jpMaster.jlrShemaKnj.getText());
    }
    if (raMaster.getMode() == 'N') {
      String knjiz = jpMaster.jlrKnj.getText();
      if (getMasterSet().getString("CGKSTAVKE").length() == 0 || knjiz.length() > 0) {
        getMasterSet().setString("CGKSTAVKE", knjiz.equalsIgnoreCase("N") ? "N" : "D");
      }
    }
  }

  public void datdokChanged() {
    if (getMasterSet().isNull("DATPRI"))
      getMasterSet().setTimestamp("DATPRI", this.getMasterSet().getTimestamp("DATDOK"));
    PartnerChangeD();
    jpMaster.jpgetval.setTecajDate(getMasterSet().getTimestamp("DATDOK"));
  }

  private String[] masort = new String[] {"DATUNOS"};

  public void setMasterSetsort() {
    getMasterSet().setSort(new com.borland.dx.dataset.SortDescriptor(masort));
  }

  protected void initPreselect() {
    pres = new presSalKon(getMasterSet());
  }
  //{"CPAR", "BROJDOK", "OZNVAL", "ID", "IP"};
  private boolean checkSaveRow() {
    if (saveRow.getInt("CPAR") == getMasterSet().getInt("CPAR") &&
        getMasterSet().getString("BROJDOK").startsWith(saveRow.getString("BROJDOK")) &&
        saveRow.getString("OZNVAL").equals(getMasterSet().getString("OZNVAL")) &&
        saveVal.compareTo(getMasterSet().getBigDecimal(jpMaster.jraIznos.getColumnName())) == 0)
      return true;
    
    if (JOptionPane.showConfirmDialog(raMaster.getWindow(), 
        "Kljuène vrijednosti dokumenta su promijenjene! \n" +
        "Originalni dokument neæe biti oznaèen kao prenesen.", "Prijenos dokumenta",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
      urTransfer = false;
      return true;
    }
    return false;
  }
  
  public void selectDoc() {
    if (!allowVirt) return;
    
    String[] pkey = {"UUID"};
    DataSet ur = Urdok.getDataModule().getTempSet(Aus.getKnjigCond().
        and(jpMaster.jpp.getCondition()).and(Condition.equal("STATKNJ", "N")).
        and(Condition.equal("VRDOK", ira ? "IRA" : "URA")));
    ur.open();
        
    /*
    String[] pkey = {"VRDOK", "CPAR", "BROJDOK"};
    Skstavkerad.getDataModule().setFilter(
        Aus.getKnjigCond().and(jpMaster.jpp.getCondition()).and(Condition.in("CKNJIGE",
            KnjigeUI.getDataModule().getTempSet(Condition.equal("VIRTUA", "D").
                and(Condition.equal("VRDOK", vrdok)))))
        .and(Condition.equal("RBS", 1)));
    System.out.println(dm.getSkstavkerad().getQuery().getQueryString());
    dm.getSkstavkerad().open(); */

    try {
      ld.saveName = "dohvat-uru";
      List modif = new ArrayList();
      modif.add(parModif);
      ld.modifiers = modif;
      urVals = ld.lookUp(raMaster.getWindow(), ur,
          pkey, new String[] {""}, new int[] {1,2,3,5,7,16});
      System.out.println(urVals);
      if (urVals != null && urVals[0] != null && urVals[0].length() > 0 &&
          ld.raLocate(ur, pkey, urVals))
        prepareTransfer(ur);

    } finally {
      ld.saveName = null;
      ld.modifiers = null;
    }
  }
  
  private void prepareTransfer(DataSet ur) {
    saveRow = new DataRow(ur, checkCols);
    dM.copyColumns(ur, saveRow, checkCols);
    saveVal = ur.getBigDecimal("IZNOS");
    
    dM.copyColumns(ur, getMasterSet(), new String[] {"CORG", "CPAR", 
      "OPIS", "BROJDOK", "DATDOK", "DATDOSP", "DATPRI", "OZNVAL", "TECAJ"});
    
    int size = Aus.getNumber(frmParam.getParam("sk", "extSize", "0",
    "Minimalna velicina broja URA/IRA (popunjavanje vedeæim nulama)"));
    String result = Integer.toString(ur.getInt("RBR"));
    if (size > 20) size = 20;
    if (result.length() < size) 
      result = Aus.string(size - result.length(), '0') + result;
    getMasterSet().setString("EXTBRDOK", result);
    getMasterSet().setBigDecimal(jpMaster.jraIznos.getColumnName(), saveVal);
    
    jpMaster.jpo.corg.forceFocLost();
    jpMaster.jpp.cpar.forceFocLost();
    jpMaster.jlrCskl.requestFocusLater();
  }
  
  public void masterSet_navigated(com.borland.dx.dataset.NavigationEvent e) {
    if (raDetail.isShowing()) checkActions();
    else checkMasterActions();
  }

  public void detailSet_navigated(com.borland.dx.dataset.NavigationEvent e) {
    if (!raSaldaKonti.isDirect())
      jpDetail.jraIznos.setColumnName(DDP());
    checkActions();
  }

  public void checkActions() {
    if (!getDetailSet().isOpen()) return;
    boolean enabde = !this.getMasterSet().getString("POKRIVENO").equalsIgnoreCase("K");

    raDetail.getNavBar().getStandardOption(raNavBar.ACTION_UPDATE).setEnabled(enabde);
    raDetail.getNavBar().getStandardOption(raNavBar.ACTION_DELETE).setEnabled(enabde);
    raDetail.getNavBar().getStandardOption(raNavBar.ACTION_ADD).setEnabled(enabde);
    raDetail.getNavBar().getStandardOption(raNavBar.ACTION_TOGGLE_TABLE).setEnabled(enabde);
    if (rnaPop != null) rnaPop.setEnabled(enabde);
  }

  public void checkMasterActions() {
    boolean enabde = !getMasterSet().getString("POKRIVENO").equalsIgnoreCase("K");
    boolean enabp = getMasterSet().getString("POKRIVENO").equalsIgnoreCase("D");
    if (rnaPot != null) rnaPot.setEnabled(enabp);
    raMaster.getNavBar().getStandardOption(raNavBar.ACTION_UPDATE).setEnabled(enabde);
    raMaster.getNavBar().getStandardOption(raNavBar.ACTION_DELETE).setEnabled(enabde);
  }

  private void jbInit() throws Exception {
    
    this.setMasterSet(Skstavkerad.getDataModule().getFilteredDataSet("1=0"));
    //this.getMasterSet().getColumn("SALDO").setCaption("Iznos");
    //this.getMasterSet().open();
    this.setNaslovMaster("Ra\u010Dun salda konti");
    this.setVisibleColsMaster(new int[] {1, 5, 11, 21});
    this.setMasterKey(mkey);
    jpMaster = new jpSalKonMaster(this);
    jpMaster.BindComponents(getMasterSet());
    this.setJPanelMaster(jpMaster);

    this.setDetailSet(Skstavkerad.getDataModule().getFilteredDataSet("1=0"));
    this.setNaslovDetail("Stavke ra\u010Duna");
    this.setVisibleColsDetail(new int[] {6, 18, 19, 22});
    this.setDetailKey(new String[] {"KNJIG", "CPAR", "VRDOK", "BROJDOK", "BROJIZV", "RBS"});
    set_kum_detail(true);
    stozbrojiti_detail(new String[] {"ID", "IP"});
    
    jpDetail = new jpSalKonDetail(this);
    jpDetail.BindComponents(getDetailSet());
    this.setJPanelDetail(jpDetail);

    jpDetail.jlrStavka.setRaDataSet(shk);
    initPreselect();
    pres.setSelDataSet(this.getMasterSet());
    pres.setSelPanel(pres.jpDetail);
    raMaster.installSelectionTracker("BROJDOK");

    //    shk.setColumns(dm.getShkonta().cloneColumns());

    this.raMaster.getJpTableView().addTableModifier(parModif = 
        new raTableColumnModifier("CPAR", new String[] {"CPAR", "NAZPAR"}, dm.getPartneri()));
    this.raMaster.getJpTableView().addTableModifier(new raCurrencyTableModifier("SALDO"));

    if (addPotvrdi()) {
      this.raMaster.getJpTableView().addTableModifier(new raTableModifier() {
        private Variant v = new Variant();
        public boolean doModify() {
          ((JraTable2)getTable()).getDataSet().getVariant("POKRIVENO",getRow(),v);
          return "N".equalsIgnoreCase(v.getString());
        }
        public void modify() {
          JComponent comp = (JComponent)renderComponent;
  //     renderComponent
          if (!isSelected()) {
            comp.setForeground(Color.red);
            comp.setBackground(((JTable) raMaster.getJpTableView().getMpTable()).getBackground());
  //      jRenderComp.setForeground(getTable().getSelectionForeground());
          } else {
            comp.setForeground(((JTable) raMaster.getJpTableView().getMpTable()).getForeground());
  //      jRenderComp.setForeground(colorG);
            comp.setBackground(Color.red);
          }
        }
      });
    }
    if (addPotvrdi()) {
      this.raMaster.addOption(rnaPot = new raNavAction("Potvrdi", raImages.IMGIMPORT, KeyEvent.VK_F7) {
        public void actionPerformed(java.awt.event.ActionEvent ev) {
          Proknjizi();
        }
      },4);
    }
    if (addVirmani()) {
      raMaster.addOption(rnaVir = new raNavAction("Virmani", raImages.IMGALIGNRIGHT, KeyEvent.VK_F8) {
          public void actionPerformed(java.awt.event.ActionEvent ev) {
              virmani();
            }
          },6);
    }
    if (addPopravi()) {
      raDetail.addOption(rnaPop = new raNavAction("Popravi razliku", raImages.IMGIMPORT, KeyEvent.VK_F7) {
        public void actionPerformed(java.awt.event.ActionEvent ev) {
          updateBalance();
        }
      }, 5);
    }
  }
  public boolean addPopravi() {
    return true;
  }

  public boolean addVirmani() {
    return true;
  }

  public boolean addPotvrdi() {
    return true;
  }

/**
 * 
 */
	protected void virmani() {
		if (raMaster.getSelectionTracker().countSelected() == 0) {//nista odabrano
			JOptionPane.showMessageDialog(raMaster.getWindow(),"Potrebno je odabrati raèune tipkom Enter za ispis virmana!");
		} else {
			new VirmaniSK(raMaster).show();
		}
	}
    
    
}


// bonkgar
// XQFLIYIK