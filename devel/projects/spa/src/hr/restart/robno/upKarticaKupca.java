/****license*****************************************************************
**   file: upKarticaKupca.java
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
package hr.restart.robno;

import hr.restart.baza.Vrart;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitFat;
import hr.restart.util.sysoutTEST;
import hr.restart.zapod.OrgStr;

import java.sql.Timestamp;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class upKarticaKupca extends raUpitFat {
  hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  Valid vl = hr.restart.util.Valid.getValid();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  java.sql.Date dateZ = null;
  java.sql.Date dateP = null;
  boolean podgrupe=false;
  boolean csklEnab, corgEnab;
  String oldCskl, oldCorg;
  QueryDataSet upMas = new QueryDataSet() {
    public boolean refreshSupported() {
      return false;
    }
    public boolean saveChangesSupported() {
      return false;
    }
  };
  QueryDataSet upDet = new QueryDataSet() {
    public boolean refreshSupported() {
      return false;
    }
    public boolean saveChangesSupported() {
      return false;
    }
  };

  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JlrNavField jlrCpar = new JlrNavField();
  JlrNavField jlrCorg = new JlrNavField();
  JraButton jbSelCorg = new JraButton();
  JLabel jLabel1 = new JLabel();
  JraButton jbDok = new JraButton();
  JlrNavField jlrNazskl = new JlrNavField();
  JlrNavField jlrCskl = new JlrNavField();
  JLabel jlCorg = new JLabel();
  JlrNavField jlrNazpar = new JlrNavField();
  JlrNavField jlrNaziv = new JlrNavField();
  JraButton jbSelCpar = new JraButton();
  JLabel jlCpar = new JLabel();
  JlrNavField jlrNazDok = new JlrNavField() {
    public void after_lookUp() {
      dokChanged();
    }
  };
  JlrNavField jlrDok = new JlrNavField() {
    public void after_lookUp() {
      dokChanged();
    }
  };
  JraButton jbSelCskl = new JraButton();
  raComboBox rcbULIZ = new raComboBox() {
    public void this_itemStateChanged() {
      super.this_itemStateChanged();
      if (this.getSelectedIndex() == 0) ulazSelected();
      else izlazSelected();
    }
  };
  JLabel jlCskl = new JLabel();
  JLabel jlVrArt = new JLabel();
  JLabel jlDatum1 = new JLabel();
  JraTextField jtfZavDatum = new JraTextField();
  raComboBox rcmbVrArt = new raComboBox();
  JraTextField jtfPocDatum = new JraTextField();
  rapancart rpcart = new rapancart(){
    public void metToDo_after_lookUp() {
//      System.out.println("rapancart - metodoafterlookup");
    }
    public void findFocusAfter() {
      //      System.out.println("rapancart - findfocusafter");
//      rpcart.setParam(hr.restart.sisfun.frmParam.getParam("robno","indiCart"));
//      rpcart.SetFocus(hr.restart.sisfun.frmParam.getParam("robno","focusCart"));
    }
  };
  TableDataSet tds = new TableDataSet();
  
  private static upKarticaKupca instanceOfMe = null;
  
  public static upKarticaKupca getInstance() {
    return instanceOfMe;
  }
  
  public upKarticaKupca() {
    try {
      jbInit();
      instanceOfMe = this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    createDataSets();
    tds.setColumns(new Column[] {
        dM.createTimestampColumn("pocDatum", "Po\u010Detni datum"),
        dM.createTimestampColumn("zavDatum", "Završni datum"),
        dM.createStringColumn("uliz", "Ulaz/Izlaz", "I", 1),
        dM.createStringColumn("vrdok", "Vrsta dokumenta", 3),
        dM.createStringColumn("cskl", "Skladište", 12),
        dM.createStringColumn("corg", "Org. jedinica", 12),
        dM.createIntColumn("cpar", "Partner"),
        dM.createIntColumn("cart", "Artikl"),
        dM.createStringColumn("cart1", "Oznaka", 20),
        dM.createStringColumn("bc", "Barcode", 20),
        dM.createStringColumn("cgrart", "Grupa artikla", 10),
        dM.createStringColumn("vrart", "Vrsta artikla", 1),
        dM.createStringColumn("nazart", "Naziv", 50)
    });
    tds.open();
    jlCskl.setText("Skladište");
    rcbULIZ.setRaItems(new String[][] {
      {"Ulazni", "U"},
      {"Izlazni", "I"},
    });
    rcbULIZ.setRaColumn("ULIZ");
    rcbULIZ.setRaDataSet(this.tds);
    jlrDok.setRaDataSet(dm.getVrdokumUlazni());
    jlrDok.setSearchMode(0);
    jlrDok.setVisCols(new int[] {0,1});
    jlrDok.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazDok});
    jlrDok.setColNames(new String[] {"NAZDOK"});
    jlrDok.setDataSet(this.tds);
    jlrDok.setColumnName("VRDOK");
    jlrDok.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrDok.setNavButton(jbDok);
    jlrNazDok.setSearchMode(1);
    jlrNazDok.setColumnName("NAZDOK");
    jlrNazDok.setNavProperties(jlrDok);
    jlCpar.setText("Poslovni partner");
    jlrNaziv.setSearchMode(1);
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setColumnName("NAZIV");
    jlrNazpar.setSearchMode(1);
    jlrNazpar.setNavProperties(jlrCpar);
    jlrNazpar.setColumnName("NAZPAR");
    jlCorg.setText("Org. jedinica");
    jlrCskl.setNavButton(jbSelCskl);
    jlrCskl.setRaDataSet(rut.getSkladFromCorg());
    jlrCskl.setSearchMode(0);
    jlrCskl.setVisCols(new int[] {0,1});
    jlrCskl.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazskl});
    jlrCskl.setColNames(new String[] {"NAZSKL"});
    jlrCskl.setDataSet(this.tds);
    jlrCskl.setColumnName("CSKL");
    jlrNazskl.setSearchMode(1);
    jlrNazskl.setNavProperties(jlrCskl);
    jlrNazskl.setColumnName("NAZSKL");
    jLabel1.setText("Dokumenti");
    jlrCorg.setNavButton(jbSelCorg);
    jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setSearchMode(0);
    jlrCorg.setVisCols(new int[] {0,1});
    jlrCorg.setTextFields(new javax.swing.text.JTextComponent[] {jlrNaziv});
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setDataSet(this.tds);
    jlrCorg.setColumnName("CORG");
    jlrCpar.setNavButton(jbSelCpar);
    jlrCpar.setRaDataSet(dm.getPartneri());
    jlrCpar.setSearchMode(0);
    jlrCpar.setVisCols(new int[] {0,1});
    jlrCpar.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazpar});
    jlrCpar.setColNames(new String[] {"NAZPAR"});
    jlrCpar.setDataSet(this.tds);
    jlrCpar.setColumnName("CPAR");
    jp.setLayout(xYLayout1);
    xYLayout1.setWidth(668);
    xYLayout1.setHeight(233);
    jlVrArt.setText("Vrsta artikla");
    jlDatum1.setText("Datum (od-do)");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setDataSet(tds);

    StorageDataSet exvr = Vrart.getDataModule().getScopedSet("cvrart nazvrart");
    exvr.open();
    exvr.insertRow(false);
    exvr.setString("CVRART", "X");
    exvr.setString("NAZVRART", "Sve vrste");
    hr.restart.util.Util.fillReadonlyData(exvr, 
        "select * FROM vrart WHERE aktiv='D'");

    rcmbVrArt.setRaDataSet(tds);
    rcmbVrArt.setRaColumn("VRART");
    rcmbVrArt.setRaItems(exvr, "CVRART", "NAZVRART");
    rcmbVrArt.setSelectedIndex(0);

    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(tds);
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    rpcart.setBorder(null);
    rpcart.setAllowUsluga(true);
    rpcart.setMode(new String("DOH"));

    jp.add(jLabel1, new XYConstraints(20, 15, -1, -1));
    jp.add(jlrCpar, new XYConstraints(155, 90, -1, -1));
    jp.add(jlrCorg, new XYConstraints(155, 40, -1, -1));
    jp.add(jbSelCorg, new XYConstraints(615, 40, 21, 21));
    jp.add(jbDok, new XYConstraints(615, 15, 21, 21));
    jp.add(jlrNazskl, new XYConstraints(260, 65, 350, -1));
    jp.add(jlrCskl, new XYConstraints(155, 65, -1, -1));
    jp.add(jlCorg, new XYConstraints(20, 40, -1, -1));
    jp.add(jlrNazpar, new XYConstraints(260, 90, 350, -1));
    jp.add(jlrNaziv, new XYConstraints(260, 40, 350, -1));
    jp.add(jbSelCpar, new XYConstraints(615, 90, 21, 21));
    jp.add(jlCpar, new XYConstraints(20, 90, -1, -1));
    jp.add(jlrNazDok, new XYConstraints(315, 15, 295, -1));
    jp.add(jlrDok, new XYConstraints(260, 15, 50, -1));
    jp.add(jbSelCskl, new XYConstraints(615, 65, 21, 21));
    jp.add(rcbULIZ, new XYConstraints(155, 15, 100, -1));
    jp.add(jlCskl, new XYConstraints(20, 65, -1, -1));
    jp.add(jlDatum1, new XYConstraints(20, 115, -1, -1));
    jp.add(jlVrArt, new XYConstraints(370, 117, 70, -1));
    jp.add(jtfPocDatum, new XYConstraints(155, 115, -1, -1));
    jp.add(jtfZavDatum, new XYConstraints(260, 115, -1, -1));
    jp.add(rcmbVrArt, new XYConstraints(459, 115, 150, 20));
    jp.add(rpcart,        new XYConstraints(5, 140, 645, 80));
    new raDateRange(jtfPocDatum, jtfZavDatum);
    this.setJPan(jp);
  }
  void dokChanged() {
    if (tds.getString("VRDOK").length() == 0) {
      if (rcbULIZ.getSelectedIndex() == 0) enabSkladCorg(true, false);
      else enabSkladCorg(true, true);
    } else if (rcbULIZ.getSelectedIndex() == 1) {
      enabSklad(TypeDoc.getTypeDoc().isDocSklad(tds.getString("VRDOK")));
      enabCorg(TypeDoc.getTypeDoc().isDocFinanc(tds.getString("VRDOK")) &&
               !TypeDoc.getTypeDoc().isDocSklad(tds.getString("VRDOK")));
    }
  }
  void ulazSelected() {
    enabSkladCorg(true, false);
    jlrDok.setRaDataSet(dm.getVrdokumUlazni());
    jlrDok.forceFocLost();
  }

  void izlazSelected() {
    enabSkladCorg(true, true);
    jlrDok.setRaDataSet(dm.getVrdokumIzlazni());
    jlrDok.forceFocLost();
  }
  void enabSkladCorg(boolean sklad, boolean corg) {
    enabSklad(sklad);
    enabCorg(corg);
  }
  void enabSklad(boolean yesno) {
    if (yesno == csklEnab) return;
    csklEnab = yesno;
    if (!yesno) {
      oldCskl = tds.getString("CSKL");
      jlrCskl.setText("");
      jlrNazskl.setText("");
    }
    rcc.setLabelLaF(jlrCskl, yesno);
    rcc.setLabelLaF(jlrNazskl, yesno);
    rcc.setLabelLaF(jbSelCskl, yesno);
    if (yesno && oldCskl != null) {
      jlrCskl.setText(oldCskl);
      jlrCskl.forceFocLost();
    }
  }

  void enabCorg(boolean yesno) {
    if (yesno == corgEnab) return;
    corgEnab = yesno;
    if (!yesno) {
      oldCorg = tds.getString("CORG");
      jlrCorg.setText("");
      jlrNaziv.setText("");
    }
    rcc.setLabelLaF(jlrCorg, yesno);
    rcc.setLabelLaF(jlrNaziv, yesno);
    rcc.setLabelLaF(jbSelCorg, yesno);
    if (yesno && oldCorg != null) {
      jlrCorg.setText(oldCorg);
      jlrCorg.forceFocLost();
    }
  }
  

//  private QueryDataSet old;
//  private boolean dblclcked;
  LinkedList ll = new LinkedList();
  
  
  public boolean runFirstESC() {
    return this.getJPTV().getDataSet() != null;
  }
  public void firstESC() {
    System.out.println("firstESC"); //XDEBUG delete when no more needed
    if (!ll.isEmpty()){
      rpcart.clearFields();
      rpcart.jrfCGRART.setText(ll.removeLast().toString());
      upMas.empty();
      upDet.empty();
      this.getJPTV().clearDataSet();
      ok_action();
      dblclc = false;
    } else {
    removeNav();
    rcc.EnabDisabAll(jp,true);
    showDefaultValues();
    }
  }
  
  public void componentShow() {
    tds.setTimestamp("pocDatum", hr.restart.robno.Util.getUtil().
        findFirstDayOfYear(Integer.valueOf(vl.findYear()).intValue()));
    tds.setTimestamp("zavDatum", vl.findDate(false,0));
    jlrCskl.setText(hr.restart.sisfun.raUser.getInstance().getDefSklad());
    jlrCskl.forceFocLost();
    jlrCorg.setText(OrgStr.getKNJCORG(false));
    jlrCorg.forceFocLost();
    oldCskl = oldCorg = null;
    csklEnab = corgEnab = true;
    rcc.EnabDisabAll(jp, true);
    showDefaultValues();
    dblclc = false;
//    jlrDok.requestFocus();
  }
  void showDefaultValues() {
    rcbULIZ.setSelectedIndex(1);
    rcbULIZ.this_itemStateChanged();
    rpcart.setCART();
    rpcart.jrfCART.emptyTextFields();
    jlrCpar.setText("");
    jlrCpar.emptyTextFields();
    jlrDok .setText("");
    jlrDok.emptyTextFields();
    upMas.empty();
    upDet.empty();
    this.getJPTV().clearDataSet();
    this.repaint();
    rcbULIZ.requestFocus();
  }
  
  private int[] vcols;
  
  private boolean dblclc;

  public void okPress() {
    dateP = new java.sql.Date(this.tds.getTimestamp("pocDatum").getTime());
    dateZ = new java.sql.Date(this.tds.getTimestamp("zavDatum").getTime());
    String qStr="";
    if (csklEnab) lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL", getCskl());
    String cartStr;
    if (rpcart.getCART().equals("")) {
      if (tds.getString("ULIZ").trim().equals("I")) {
        cartStr = rpcart.findCART("stdoki", podgrupe);
        if (cartStr.equals("")) {
          cartStr = "artikli.cart=stdoki.cart ";
        }
        qStr = "SELECT MAX(stdoki.cart) as cart, MAX(artikli.cart1) as cart1, MAX(artikli.bc) as bc, " +
            " MAX(artikli.nazart) as nazart, sum(stdoki.kol) as kol, avg(stdoki.fvc) as vc " +
            "FROM doki,stdoki,artikli " +
            "WHERE " + rut.getDoc("DOKI", "STDOKI") +
            findCSKLandCORG()+
            " and artikli.cart=stdoki.cart " +findVRART()+
            findCPAR("DOKI") + " " + findVRDOK("DOKI") + " and " + cartStr +
            " and doki.datdok >= " +
            rut.getTimestampValue(tds.getTimestamp("pocDatum"), rut.NUM_FIRST) +
            " and doki.datdok <= " +
            rut.getTimestampValue(tds.getTimestamp("zavDatum"), rut.NUM_LAST) +
            " " +
            "GROUP BY stdoki.cart";
        System.out.println(qStr);
      } else {
        cartStr = rpcart.findCART("stdoku", podgrupe);
        if (cartStr.equals("")) {
          cartStr = "artikli.cart=stdoku.cart ";
        }
        qStr = "SELECT MAX(stdoku.cart) as cart, MAX(artikli.cart1) as cart1, MAX(artikli.bc) as bc, " +
            " MAX(artikli.nazart) as nazart, sum(stdoku.kol) as kol, avg(stdoku.vc) as vc " +
            "FROM doku,stdoku,artikli " +
            "WHERE " + rut.getDoc("DOKU", "STDOKU") +
            findCSKLandCORGUl()+
//            " and doku.cskl='"+tds.getString("CSKL")+"' "+
            " and artikli.cart=stdoku.cart " +findVRART()+
            findCPAR("DOKU") + " " + findVRDOK("DOKU") + " and " + cartStr +
            " and doku.datdok >= " +
            rut.getTimestampValue(tds.getTimestamp("pocDatum"), rut.NUM_FIRST) +
            " and doku.datdok <= " +
            rut.getTimestampValue(tds.getTimestamp("zavDatum"), rut.NUM_LAST) +
            " " +
            "GROUP BY stdoku.cart";
        System.out.println(qStr);
      }
    } else {
      if (tds.getString("ULIZ").trim().equals("I")) {
        cartStr = rpcart.findCART("stdoki", podgrupe);
        if (cartStr.equals("")) {
          cartStr = "artikli.cart=stdoki.cart ";
        }
        qStr = "SELECT doki.datdok as datdok, doki.brdok as brdok, " +
            "doki.vrdok as vrdok, stdoki.kol as kol, stdoki.fvc as vc, doki.cpar, partneri.nazpar " +
            "FROM doki,stdoki,artikli,partneri " +
            "WHERE " + rut.getDoc("DOKI", "STDOKI") +
            findCSKLandCORG()+
//            " and doki.cskl='"+tds.getString("CSKL")+"' "+
            " and doki.cpar=partneri.cpar"+
            " and artikli.cart=stdoki.cart " +findVRART()+
            findCPAR("DOKI") + " " + findVRDOK("DOKI") + " and " + cartStr +
            " and doki.datdok >= " +
            rut.getTimestampValue(tds.getTimestamp("pocDatum"), rut.NUM_FIRST) +
            " and doki.datdok <= " +
            rut.getTimestampValue(tds.getTimestamp("zavDatum"), rut.NUM_LAST) +
            " " +
            "ORDER BY doki.datdok";
        System.out.println("String; "+qStr);
      } else {
        cartStr = rpcart.findCART("stdoku", podgrupe);
        if (cartStr.equals("")) {
          cartStr = "artikli.cart=stdoku.cart ";
        }
        qStr = "SELECT doku.datdok as datdok, doku.brdok as brdok, " +
            "doku.vrdok as vrdok, stdoku.kol as kol, stdoku.vc as vc, doku.cpar, partneri.nazpar " +
            "FROM doku,stdoku,artikli,partneri " +
            "WHERE " + rut.getDoc("DOKU", "STDOKU") +
            findCSKLandCORGUl()+
//            " and doku.cskl='"+tds.getString("CSKL")+"' "+
            " and doku.cpar=partneri.cpar"+
            " and artikli.cart=stdoku.cart " +findVRART()+
            findCPAR("DOKU") + " " + findVRDOK("DOKU") + " and " + cartStr +
            " and doku.datdok >= " +
            rut.getTimestampValue(tds.getTimestamp("pocDatum"), rut.NUM_FIRST) +
            " and doku.datdok <= " +
            rut.getTimestampValue(tds.getTimestamp("zavDatum"), rut.NUM_LAST) +
            " " +
            "ORDER BY doku.datdok";
        System.out.println(qStr);
      }
    }
    vl.execSQL(qStr);
    openScratchDataSet(vl.RezSet);
    if (vl.RezSet.rowCount() == 0) setNoDataAndReturnImmediately();

    this.killAllReports();

    if (rpcart.getCART().equals("")) {
      String[] cc = new String[] {
          "CART", "CART1", "BC", "NAZART", "KOL", "VC"};
      upMas.empty();
//      st.prn(vl.RezSet);
      for (vl.RezSet.first(); vl.RezSet.inBounds(); vl.RezSet.next()) {
        upMas.insertRow(false);
        dM.copyColumns(vl.RezSet, upMas, cc);
      }
      vl.RezSet = null;
      vcols = new int[] {0,1,2,3,4,5};
      addReportsAndHandleColumns();
      upMas.setTableName("master");
      setDataSet(upMas);
    } else {
      String[] cc = new String[] {
          "DATDOK", "BRDOK", "VRDOK", "KOL", "VC", "CPAR", "NAZPAR"};
      upDet.empty();
      for (vl.RezSet.first(); vl.RezSet.inBounds(); vl.RezSet.next()) {
        upDet.insertRow(false);
        dM.copyColumns(vl.RezSet, upDet, cc);
      }
      vl.RezSet = null;
      vcols = new int[] {0,1,2,3,4,5,6};
      addReportsAndHandleColumns();
      upDet.setTableName("detail");
      setDataSet(upDet);
    }
    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
    st.prn(upDet);
  }
  
  public DataSet getReportSet(){
    if (!rpcart.getCART().equals("")) return upDet;
    return upMas;
  }
  
  protected void addReportsAndHandleColumns(){
    if (rpcart.getCART().equals("")) {
      this.addReport("hr.restart.robno.repKarticaKupca", "hr.restart.robno.repKarticaKupca",
          "KarticaKupca", "Kartica kupca");
    } else {
      this.addReport("hr.restart.robno.repKarticaKupcaArtikl", "hr.restart.robno.repKarticaKupcaArtikl",
          "KarticaKupcaArtikl", "Kartica kupca");
//      this.addReport("hr.restart.robno.repKarticaKupcaArtikl", "hr.restart.robno.repKarticaKupcaArtikl",
//          "KarticaKupcaArtiklCijena", "Kartica kupca s cijenom");
      //this.addReport("hr.restart.robno.repUldok", "hr.restart.robno.repUldok",
        //  "Uldok", "Pregled ulaznih dokumenata");
    }
  }

  public boolean Validacija() {
    System.out.println("Validacija");
    if (csklEnab && vl.isEmpty(jlrCskl)) return false;
    if (corgEnab && vl.isEmpty(jlrCorg)) return false;
//    if (vl.isEmpty(jlrCpar)) return false;
    if (!Aus.checkDateRange(jtfPocDatum, jtfZavDatum)) return false;
    if (!rpcart.getCGRART().equals("") && (rpcart.getCART().equals("") && rpcart.getCART1().equals("")) && !dblclc){
      int grupe = JOptionPane.showConfirmDialog(this.jp,"Ukljuèiti i podgrupe?","Grupe artikala",JOptionPane.YES_NO_OPTION);
      if (grupe == JOptionPane.CANCEL_OPTION) return false;
      if (grupe == JOptionPane.NO_OPTION) {podgrupe = false;}
      else {podgrupe = true;}
    }
    return true;
  }
  /*
        dM.createTimestampColumn("pocDatum", "Po\u010Detni datum"),
        dM.createTimestampColumn("zavDatum", "Završni datum"),*/
  
  public Timestamp getOdDatuma(){
    return tds.getTimestamp("pocDatum");
  }
  public Timestamp getDoDatuma(){
    return tds.getTimestamp("zavDatum");
  }
  
  public String getCart(){
   return Aut.getAut().getCARTdependable(rpcart.getCART(),rpcart.getCART1(),rpcart.getBC()); 
  }
  
  public String getNazart(){
    return rpcart.getNAZART();
  }
  
  public String getCorg() {
    return tds.getString("CORG");
  }
  public String getNazorg() {
    return jlrNaziv.getText();
  }
  public String getCskl() {
    return tds.getString("CSKL");
  }
  public String getNazSkl() {
    return jlrNazskl.getText().trim();
  }
  public String getCpar() {
    return jlrCpar.getText().trim();
  }
  public String getNazPar() {
    return jlrNazpar.getText().trim();
  }
  public java.sql.Timestamp getPocDatum() {
    return tds.getTimestamp("pocDatum");
  }
  public java.sql.Timestamp getZavDatum() {
    return tds.getTimestamp("zavDatum");
  }
  private void createDataSets() {
    upMas.setColumns(new Column[] {
                     (Column) dm.getStdoki().getColumn("CART").clone(),
                     (Column) dm.getStdoki().getColumn("CART1").clone(),
                     (Column) dm.getStdoki().getColumn("BC").clone(),
                     (Column) dm.getStdoki().getColumn("NAZART").clone(),
                     (Column) dm.getStdoki().getColumn("KOL").clone(),
                     (Column) dm.getStdoki().getColumn("VC").clone()
    });
    upMas.open();
    upDet.setColumns(new Column[] {
                     (Column) dm.getDoku().getColumn("DATDOK").clone(),
                     (Column) dm.getDoku().getColumn("BRDOK").clone(),
                     (Column) dm.getDoku().getColumn("VRDOK").clone(),
                     (Column) dm.getStdoku().getColumn("KOL").clone(),
                     (Column) dm.getStdoku().getColumn("VC").clone(),
                     (Column) dm.getDoku().getColumn("CPAR").clone(),
                     (Column) dm.getPartneri().getColumn("NAZPAR").clone()
    });
    upDet.open();
  }
  String findCPAR(String baza) {
    if (getCpar().equals("")) return "";
    else return "AND " + baza.trim() + ".CPAR=" + getCpar() + " ";
  }
  String findCSKLandCORG() {
    String cVrati="";
    if (!getCskl().equals("")) {
      cVrati=cVrati+" and (doki.cskl='"+tds.getString("CSKL")+"'";
      if (!getCorg().equals(""))
        cVrati=cVrati+" or doki.cskl='"+tds.getString("CORG")+"'";
    }
    else {
      if (!getCorg().equals(""))
        cVrati=cVrati+" and (doki.cskl='"+tds.getString("CORG")+"'";
    }
    cVrati=cVrati+") ";
    return cVrati;
  }
  String findCSKLandCORGUl() {
	    String cVrati="";
	    if (!getCskl().equals("")) {
	      cVrati=cVrati+" and (doku.cskl='"+tds.getString("CSKL")+"'";
	      if (!getCorg().equals(""))
	        cVrati=cVrati+" or doku.cskl='"+tds.getString("CORG")+"'";
	    }
	    else {
	      if (!getCorg().equals(""))
	        cVrati=cVrati+" and (doku.cskl='"+tds.getString("CORG")+"'";
	    }
	    cVrati=cVrati+") ";
	    return cVrati;
	  }
  
  String findVRDOK(String baza) {
    if (tds.getString("VRDOK").trim().equals("")) {
      if (tds.getString("ULIZ").trim().equals("I")) return " and "+baza.trim()+".vrdok in('ROT', 'RAC', 'POD') ";
      else return " and "+baza.trim()+".vrdok in('PRK', 'POD', 'PST') ";
    }
    else return "and "+baza.trim()+".vrdok='"+tds.getString("VRDOK").trim()+"' ";
  }
  
  String findVRART() {
    System.out.println("VrArt: '"+tds.getString("VRART")+"'");
    if (tds.getString("VRART").equals("X") || tds.getString("VRART").equals("")) return "";
    else return " and artikli.vrart='" + tds.getString("VRART") + "' ";
  }
  
  public void jptv_doubleClick() {
    dblclc = true;
    if (rpcart.getCART().equals("")) {
      getJPTV().getColumnsBean().saveSettings();
      ll.addLast(rpcart.getCGRART());
      System.out.println("setCART: "+this.getJPTV().getDataSet().getInt("CART"));
      rpcart.setCART(this.getJPTV().getDataSet().getInt("CART"));
      upMas.empty();
      upDet.empty();
      this.getJPTV().clearDataSet();
      ok_action();
    }
    else {
      rut.showDocs(tds.getString("CORG"), this.getJPTV());
    }
  }
  
  public boolean isIspis() {
    return false;
  }
  
  public String navDoubleClickActionName() {
    return "Kupci";
  }
  public int[] navVisibleColumns() {
    return vcols;
  }
}

