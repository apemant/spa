/****license*****************************************************************
**   file: upUlazIzlaz.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sk.PartnerCache;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpit;
import hr.restart.util.raUpitFat;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:        Robno poslovanje
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      REST-ART 
 * @author REST-ART development team
 * @version 1.0
 *
 * NAPOMENA: Ako pochne rokati sa unknown column, samo mu tds.addColumn stavi prije raCheckBoxa
 *
 */

public class upUlazIzlaz extends raUpitFat {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();

  JraTextField jtfZavDatum = new JraTextField();
  java.sql.Date dateZ = null;
  java.sql.Date dateP = null;
  JPanel jPanel2 = new JPanel();
  JPanel jp = new JPanel();
  JraButton jbDok = new JraButton();

  JLabel jlCskl = new JLabel();
  JlrNavField jlrCskl = new JlrNavField();
  JlrNavField jlrNazskl = new JlrNavField();
  JraButton jbSelCskl = new JraButton();
  JLabel jlCorg = new JLabel();
  JlrNavField jlrCorg = new JlrNavField();
  JlrNavField jlrNaziv = new JlrNavField();
  JraButton jbSelCorg = new JraButton();
  JLabel jlCpar = new JLabel();
  JlrNavField jlrCpar = new JlrNavField();
  JlrNavField jlrNazpar = new JlrNavField();
  JraButton jbSelCpar = new JraButton();

  JlrNavField jlrDok = new JlrNavField() {
    public void after_lookUp() {
      dokChanged();
    }
  };
  JlrNavField jlrNazDok = new JlrNavField() {
    public void after_lookUp() {
      dokChanged();
    }
  };
  raComboBox rcbULIZ = new raComboBox() {
    public void this_itemStateChanged() {
      super.this_itemStateChanged();
      if (this.getSelectedIndex() == 0) ulazSelected();
      else izlazSelected();
    }
  };
  raComboBox rcbPLAC = new raComboBox();
  JraTextField jtfPocDatum = new JraTextField();
//  rapancskl rpcskl = new rapancskl();
  JLabel jLabel6 = new JLabel();
  raComboBox rcbKNJIZ = new raComboBox();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel1 = new JLabel();
  BorderLayout borderLayout1 = new BorderLayout();
  XYLayout xYLayout1 = new XYLayout();
  TableDataSet tds = new TableDataSet();
  Column column7;
  dM dm = hr.restart.baza.dM.getDataModule();
  Valid vl = hr.restart.util.Valid.getValid();
  
  QueryDataSet uldok = new QueryDataSet() {
    public boolean refreshSupported() {
      return false;
    }
    public boolean saveChangesSupported() {
      return false;
    }
  };
  QueryDataSet izdok = new QueryDataSet() {
    public boolean refreshSupported() {
      return false;
    }
    public boolean saveChangesSupported() {
      return false;
    }
  };
  private static upUlazIzlaz upui;
  boolean csklEnab, corgEnab;

  public upUlazIzlaz() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    upui=this;
  }
  public static upUlazIzlaz getInstance(){
    return upui;
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
    showDefaultValues();
  }
  private String getKI() {
    return " and doki.vrdok not in ('PON','NDO','NKU','RNL','DOS','TRE','ZAH')";
  }
  public boolean Validacija() {
//    if (csklEnab && vl.isEmpty(jlrCskl)) return false;
    if (corgEnab && vl.isEmpty(jlrCorg)) return false;
    if (!Aus.checkDateRange(jtfPocDatum, jtfZavDatum))
      return false;
    return true;
  }

  private void createDataSets() {
    izdok.setColumns(new Column[] {
        dm.getPartneri().getColumn("CPAR").cloneColumn(),
        dm.getPartneri().getColumn("NAZPAR").cloneColumn(),
        dm.getPartneri().getColumn("OIB").cloneColumn(),
        (Column) dm.getDoki().getColumn("CSKL").clone(),
        (Column) dm.getDoki().getColumn("DATDOK").clone(),
        (Column) dm.getDoki().getColumn("DATDOSP").clone(),
      (Column) dm.getDoki().getColumn("VRDOK").clone(),
        (Column) dm.getDoki().getColumn("BRDOK").clone(),
        dM.createStringColumn("FBR", "Fiskalni broj", 50),
        (Column) dm.getDoki().getColumn("CVRTR").clone(),
        (Column) dm.getStdoki().getColumn("INAB").clone(),
        (Column) dm.getStdoki().getColumn("IMAR").clone(),
        (Column) dm.getStdoki().getColumn("IPOR").clone(),
        (Column) dm.getStdoki().getColumn("IRAZ").clone(),
        (Column) dm.getStdoki().getColumn("UIRAB").clone(),
        dM.createBigDecimalColumn("IPRODBP","Prihod"),
        dM.createBigDecimalColumn("POREZ", "Porez"),
        dM.createBigDecimalColumn("IPRODSP","Ukupno"),
        dM.createBigDecimalColumn("ZARADA", "Stvarna RuC")
    });
    izdok.open();
    uldok.setColumns(new Column[] {
        dm.getPartneri().getColumn("CPAR").cloneColumn(),
        dm.getPartneri().getColumn("NAZPAR").cloneColumn(),
        dm.getPartneri().getColumn("OIB").cloneColumn(),
        (Column) dm.getDoku().getColumn("CSKL").clone(),
        (Column) dm.getDoku().getColumn("DATDOK").clone(),
      (Column) dm.getDoku().getColumn("VRDOK").clone(),
      (Column) dm.getDoku().getColumn("BRDOK").clone(),
      (Column) dm.getDoku().getColumn("UIPRPOR").clone(),
      (Column) dm.getStdoku().getColumn("IDOB").clone(),
      dM.createBigDecimalColumn("IRAB","Popust"),
      (Column) dm.getStdoku().getColumn("IZT").clone(),
      (Column) dm.getStdoku().getColumn("INAB").clone(),
      (Column) dm.getStdoku().getColumn("IMAR").clone(),
      (Column) dm.getStdoku().getColumn("IPOR").clone(),
      (Column) dm.getStdoku().getColumn("IZAD").clone()
    });
    uldok.open();
  }

  public void okPress() {
    dateP = new java.sql.Date(this.tds.getTimestamp("pocDatum").getTime());
    dateZ = new java.sql.Date(this.tds.getTimestamp("zavDatum").getTime());
    System.out.println("Ulaz izlaz: "+tds.getString("ULIZ"));
    String qStr;
    boolean showPar = "D".equalsIgnoreCase(frmParam.getParam("robno", "uiDokPar", 
        "D", "Prikaz partnera na pregledu ulaznih/izlaznih dokumenata (D,N)"));
        
    PartnerCache pc = showPar ? new PartnerCache() : null;
    izdok.getColumn("CPAR").setVisible(showPar ? 1 : 0);
    izdok.getColumn("NAZPAR").setVisible(showPar ? 1 : 0);
    izdok.getColumn("OIB").setVisible(showPar ? 1 : 0);
    uldok.getColumn("CPAR").setVisible(showPar ? 1 : 0);
    uldok.getColumn("NAZPAR").setVisible(showPar ? 1 : 0);
    uldok.getColumn("OIB").setVisible(showPar ? 1 : 0);
    /*if (csklEnab) lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL", getDokuCskl());
    String vrzal = csklEnab ? dm.getSklad().getString("VRZAL") : "";*/
    String rinab = raIzlazTemplate.isNabDirect() ? "RINAB" : "INAB";
    if (tds.getString("ULIZ").trim().equals("I")) {
      qStr="SELECT max(DOKI.CPAR) as CPAR, max(DOKI.DATDOK) as DATDOK, max(DOKI.DATDOSP) as DATDOSP, max(DOKI.VRDOK) as VRDOK, max(DOKI.CSKL) as CSKL, " +
      		"max(DOKI.BRDOK) as BRDOK, max(DOKI.CVRTR) as CVRTR, max(doki.fbr) as fbr, max(doki.fpp) as fpp, max(doki.fnu) as fnu,"+
      	   "sum(STDOKI.INAB) as INAB, "+findIRAZ("M")+", sum(STDOKI.UIRAB) as UIRAB, "+
           "sum(STDOKI.IPRODBP) as IPRODBP,  (sum(STDOKI.POR1)+sum(STDOKI.POR2)+sum(STDOKI.POR3)) as POREZ, "+
           "sum(STDOKI.IPRODSP) as IPRODSP, "+
           "(sum(STDOKI.IPRODBP)-sum(STDOKI."+rinab+")) as ZARADA "+
           "from STDOKI,DOKI "+
           "where DOKI.CSKL=STDOKI.CSKL AND DOKI.VRDOK=STDOKI.VRDOK AND DOKI.GOD=STDOKI.GOD AND DOKI.BRDOK=STDOKI.BRDOK "+getKI()+
           getCsklOrCorg()+" AND DOKI.DATDOK >= "+rut.getTimestampValue(tds.getTimestamp("pocDatum"), rut.NUM_FIRST)+
           " AND DOKI.DATDOK <= "+rut.getTimestampValue(tds.getTimestamp("zavDatum"), rut.NUM_LAST)+" "+
           findCPAR("DOKI")+findVRDOK("DOKI")+findPLAC("DOKI")+findKNJIZ("DOKI")+
           "GROUP BY STDOKI.CSKL, STDOKI.VRDOK, STDOKI.BRDOK, STDOKI.GOD "+
           findMesklaIzlaz()+
           "ORDER BY 2,4,3";
    }
    else {

      qStr="SELECT max(DOKU.CPAR) as CPAR, max(DOKU.DATDOK) as DATDOK, max(DOKU.BRDOK) as BRDOK, max(DOKU.VRDOK) as VRDOK, max(DOKU.CSKL) as CSKL, "+
           "max(DOKU.UIPRPOR) as UIPRPOR, sum(STDOKU.IDOB) as IDOB, sum(STDOKU.IRAB) as IRAB, "+
           "sum(STDOKU.IZT) as IZT, sum(STDOKU.INAB) as INAB "+findIZAD("M")+"from STDOKU,DOKU "+
           "where DOKU.CSKL=STDOKU.CSKL AND DOKU.VRDOK=STDOKU.VRDOK AND DOKU.GOD=STDOKU.GOD AND DOKU.BRDOK=STDOKU.BRDOK "+
           "AND "+getDokuCskl()+" AND DOKU.DATDOK >= "+rut.getTimestampValue(tds.getTimestamp("pocDatum"), rut.NUM_FIRST)+
           " AND DOKU.DATDOK <= "+rut.getTimestampValue(tds.getTimestamp("zavDatum"), rut.NUM_LAST)+" "+
           findCPAR("DOKU")+findVRDOK("DOKU")+findPLAC("DOKU")+findKNJIZ("DOKU")+" AND DOKU.VRDOK != 'POR' "+
           "GROUP BY STDOKU.CSKL, STDOKU.VRDOK, STDOKU.BRDOK, STDOKU.GOD "+
          // poravnanje na ulazu
      "UNION SELECT max(DOKU.CPAR) as CPAR, max(DOKU.DATDOK) as DATDOK, max(DOKU.BRDOK) as BRDOK, 'POR' as VRDOK, max(DOKU.CSKL) as CSKL, "+
           "sum(DOKU.UIPRPOR-DOKU.UIPRPOR) as UIPRPOR, sum(STDOKU.IDOB-STDOKU.IDOB) as IDOB, sum(STDOKU.IRAB-STDOKU.IRAB) as IRAB, "+
           "sum(STDOKU.IZT-STDOKU.IZT) as IZT, sum(STDOKU.INAB-STDOKU.INAB) as INAB, sum(STDOKU.DIOPORMAR) as IMAR, sum(STDOKU.DIOPORPOR) as IPOR, sum(STDOKU.PORAV) as IZAD from STDOKU,DOKU "+
           "where DOKU.CSKL=STDOKU.CSKL AND DOKU.VRDOK=STDOKU.VRDOK AND DOKU.GOD=STDOKU.GOD AND DOKU.BRDOK=STDOKU.BRDOK "+
           "AND "+getDokuCskl()+" AND DOKU.DATDOK >= "+rut.getTimestampValue(tds.getTimestamp("pocDatum"), rut.NUM_FIRST)+
           " AND DOKU.DATDOK <= "+rut.getTimestampValue(tds.getTimestamp("zavDatum"), rut.NUM_LAST)+" "+
           findCPAR("DOKU")+findVRDOKPOR()+findPLAC("DOKU")+findKNJIZ("DOKU")+
           "GROUP BY STDOKU.CSKL, STDOKU.VRDOK, STDOKU.BRDOK, STDOKU.GOD "+
          // poravnanja
      "UNION SELECT max(DOKU.CPAR) as CPAR, max(DOKU.DATDOK) as DATDOK, max(DOKU.BRDOK) as BRDOK, max(DOKU.VRDOK) as VRDOK, max(DOKU.CSKL) as CSKL, "+
           "sum(DOKU.UIPRPOR-DOKU.UIPRPOR) as UIPRPOR, sum(STDOKU.IDOB-STDOKU.IDOB) as IDOB, sum(STDOKU.IRAB-STDOKU.IRAB) as IRAB, "+
           "sum(STDOKU.IZT-STDOKU.IZT) as IZT, sum(STDOKU.INAB-STDOKU.INAB) as INAB, sum(STDOKU.DIOPORMAR) as IMAR, sum(STDOKU.DIOPORPOR) as IPOR, sum(STDOKU.PORAV) as IZAD from STDOKU,DOKU "+
           "where DOKU.CSKL=STDOKU.CSKL AND DOKU.VRDOK=STDOKU.VRDOK AND DOKU.GOD=STDOKU.GOD AND DOKU.BRDOK=STDOKU.BRDOK "+
           "AND "+getDokuCskl()+" AND DOKU.DATDOK >= "+rut.getTimestampValue(tds.getTimestamp("pocDatum"), rut.NUM_FIRST)+
           " AND DOKU.DATDOK <= "+rut.getTimestampValue(tds.getTimestamp("zavDatum"), rut.NUM_LAST)+" "+
           findCPAR("DOKU")+findVRDOK("DOKU")+findPLAC("DOKU")+findKNJIZ("DOKU")+" AND DOKU.VRDOK = 'POR' "+
           "GROUP BY STDOKU.CSKL, STDOKU.VRDOK, STDOKU.BRDOK, STDOKU.GOD "+
           findMesklaUlaz()+
           "ORDER BY 2,4,3";
    }
    
    System.out.println(qStr);
    
    vl.execSQL(qStr);

    openDataSet(vl.RezSet);
    if (vl.RezSet.getRowCount() == 0) setNoDataAndReturnImmediately();

      this.killAllReports();
      this.getJPTV().setNaslovi(null);

      if (tds.getString("ULIZ").trim().equals("I")) {
        String[] cc = new String[] {"CSKL", "DATDOK", "BRDOK", "CVRTR", "VRDOK","IPRODSP","POREZ","IPRODBP",
          "UIRAB","ZARADA","INAB","IMAR","IPOR","IRAZ"};
        izdok.empty();
        
        String fiskForm = frmParam.getParam("robno", "fiskForm", "[FBR]-[FPP]-[FNU]",
          "Format fiskalnog broja izlaznog dokumenta na ispisu");
        
        for (vl.RezSet.first(); vl.RezSet.inBounds(); vl.RezSet.next()) {
          izdok.insertRow(false);
          dM.copyColumns(vl.RezSet, izdok, cc);
          int cpar = vl.RezSet.getInt("CPAR");
          if (showPar && cpar > 0) {
            izdok.setInt("CPAR", cpar);
            izdok.setString("NAZPAR", pc.getNameNotNull(cpar));
            if (pc.getData(cpar) != null)
              izdok.setString("OIB", pc.getData(cpar).getOIB());
          }
          if (vl.RezSet.getString("FPP").length() > 0) {
            izdok.setString("FBR", Aus.formatBroj(vl.RezSet, fiskForm));
          }
          izdok.setTimestamp("DATDOK",hr.restart.util.Util.getUtil().clearTime(vl.RezSet.getTimestamp("DATDOK")));
          if (!vl.RezSet.isNull("DATDOK"))
            izdok.setTimestamp("DATDOSP",hr.restart.util.Util.getUtil().clearTime(vl.RezSet.getTimestamp("DATDOSP")));
          
          if (!TypeDoc.getTypeDoc().isDocSklad(izdok.getString("VRDOK")) &&
              !TypeDoc.getTypeDoc().isDocFinanc(izdok.getString("VRDOK"))) {
            izdok.setBigDecimal("UIRAB", _Main.nul);
            izdok.setBigDecimal("ZARADA", _Main.nul);
            System.out.println("Postavljam zaradu nula");
          }
        }
        vl.RezSet = null;
        izdok.close();
        izdok.setSort(new SortDescriptor(new String[] {"DATDOK","BRDOK","VRDOK"}));
        izdok.open();
        izdok.setTableName("izdok");
        izdok.last();
        this.getJPTV().setDataSetAndSums(izdok, new String[] {"IPRODSP", "POREZ",
            "IPRODBP", "UIRAB", "ZARADA", "INAB", "IMAR", "IPOR", "IRAZ"});
        this.addReport("hr.restart.robno.repIzdok", "hr.restart.robno.repIzdok",
                       "Izdok", "Pregled izlaznih dokumenata");
      }
      else {
        String[] cc = new String[] {"CSKL", "DATDOK","BRDOK","VRDOK","UIPRPOR",
          "IDOB","IRAB","IZT","INAB","IMAR","IPOR","IZAD"};
        uldok.empty();
        for (vl.RezSet.first(); vl.RezSet.inBounds(); vl.RezSet.next()) {
          uldok.insertRow(false);
          int cpar = vl.RezSet.getInt("CPAR");
          if (showPar && cpar > 0) {
            uldok.setInt("CPAR", cpar);
            uldok.setString("NAZPAR", pc.getNameNotNull(cpar));
            if (pc.getData(cpar) != null)
              uldok.setString("OIB", pc.getData(cpar).getOIB());
          }
          dM.copyColumns(vl.RezSet, uldok, cc);
        }
        vl.RezSet = null;
        uldok.close();
        uldok.setSort(new SortDescriptor(new String[] {"DATDOK","VRDOK","BRDOK"}));
        uldok.open();
        uldok.setTableName("uldok");
        uldok.last();
        this.getJPTV().setDataSetAndSums(uldok, new String[] {"UIPRPOR", "IDOB",
            "IRAB", "IZT", "INAB", "IMAR", "IPOR", "IZAD"});
        this.addReport("hr.restart.robno.repUldok", "hr.restart.robno.repUldok",
                       "Uldok", "Pregled ulaznih dokumenata");
      }
  }
  
  public String navDoubleClickActionName(){
    return "Prikaz dokumenta";
  }

  int[] visiz = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14};
  int[] visul = {0,1,2,3,4,5,6,7,8,9,10,11,12,13};
  
  public int[] navVisibleColumns() {
    if (tds.getString("ULIZ").trim().equals("I"))
      return visiz;
    return visul;
  }
  
  protected void addNavBarOptions() {
    super.addNavBarOptions();
//    getJPTV().getNavBar().removeOption(rnvDoubleClick);
  }

  public void firstESC() {
    this.getJPTV().clearDataSet();
    removeNav();
    showDefaultValues();
  }

  public boolean runFirstESC() {
    return this.getJPTV().getDataSet() != null;
  }

  private void jbInit() throws Exception {
    this.getTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    this.setJPan(jPanel2);

    createDataSets();
    jPanel2.setLayout(xYLayout1);
    tds.setColumns(new Column[] {
        dM.createTimestampColumn("pocDatum", "Po\u010Detni datum"),
        dM.createTimestampColumn("zavDatum", "Završni datum"),
        dM.createStringColumn("uliz", "Ulaz/Izlaz", "I", 1),
        dM.createStringColumn("vrdok", "Vrsta dokumenta", 3),
        dM.createStringColumn("plac", "", "O", 1),
        dM.createStringColumn("knjiz", "", "O", 1),
        dM.createStringColumn("cskl", "Skladište", 12),
        dM.createStringColumn("corg", "Org. jedinica", 12),
        dM.createIntColumn("cpar", "Partner")
    });
    tds.open();

    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setDataSet(tds);

    jlrDok.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrDok.setColumnName("VRDOK");
    jlrDok.setDataSet(this.tds);
    jlrDok.setColNames(new String[] {"NAZDOK"});
    jlrDok.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazDok});
    jlrDok.setVisCols(new int[] {0,1});
    jlrDok.setSearchMode(0);
    jlrDok.setRaDataSet(dm.getVrdokumUlazni());
    jlrDok.setNavButton(jbDok);

    jlrNazDok.setNavProperties(jlrDok);
    jlrNazDok.setColumnName("NAZDOK");
    jlrNazDok.setSearchMode(1);

    jlCskl.setText("Skladište");
    jlrCskl.setColumnName("CSKL");
    jlrCskl.setDataSet(this.tds);
    jlrCskl.setColNames(new String[] {"NAZSKL"});
    jlrCskl.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazskl});
    jlrCskl.setVisCols(new int[] {0,1});
    jlrCskl.setSearchMode(0);
    jlrCskl.setRaDataSet(rut.getSkladFromCorg());
    jlrCskl.setNavButton(jbSelCskl);

    jlrNazskl.setColumnName("NAZSKL");
    jlrNazskl.setNavProperties(jlrCskl);
    jlrNazskl.setSearchMode(1);

    jlCpar.setText("Poslovni partner");
    jlrCpar.setColumnName("CPAR");
    jlrCpar.setDataSet(this.tds);
    jlrCpar.setColNames(new String[] {"NAZPAR"});
    jlrCpar.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazpar});
    jlrCpar.setVisCols(new int[] {0,1});
    jlrCpar.setSearchMode(0);
    jlrCpar.setRaDataSet(dm.getPartneri());
    jlrCpar.setNavButton(jbSelCpar);

    jlrNazpar.setColumnName("NAZPAR");
    jlrNazpar.setNavProperties(jlrCpar);
    jlrNazpar.setSearchMode(1);

    jlCorg.setText("Org. jedinica");
    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(this.tds);
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new javax.swing.text.JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0,1});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbSelCorg);

    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String newk, String oldk) {
        jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);


    rcbULIZ.setRaDataSet(this.tds);
    rcbULIZ.setRaColumn("ULIZ");
    rcbULIZ.setRaItems(new String[][] {
      {"Ulazni", "U"},
      {"Izlazni", "I"},
    });
    rcbPLAC.setRaDataSet(this.tds);
    rcbPLAC.setRaColumn("PLAC");
    rcbPLAC.setRaItems(new String[][] {
      {" ", "O"},
      {"Pla\u0107eni", "D"},
      {"Nepla\u0107eni", "N"},
    });
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(tds);
    jtfPocDatum.setText("jraTextField1");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
//    rpcskl.setRaMode('A');
    jLabel6.setText("Status knjiženja");
    rcbKNJIZ.setRaDataSet(this.tds);
    rcbKNJIZ.setRaColumn("KNJIZ");
    rcbKNJIZ.setRaItems(new String[][] {
      {" ", "O"},
      {"Knjiženo", "D"},
      {"Neknjiženo", "N"},
    });
    jLabel5.setText("Datum (od - do)");
    jLabel3.setText("Status dokumenata");
    jLabel2.setText("Vrsta dokumenta");
    jLabel1.setText("Dokumenti");
    jbDok.setText("...");
    xYLayout1.setWidth(645);
    xYLayout1.setHeight(190);

    jPanel2.add(jLabel1, new XYConstraints(15, 20, -1, -1));
    jPanel2.add(jlrDok, new XYConstraints(255, 20, 50, -1));
    jPanel2.add(rcbULIZ, new XYConstraints(150, 20, 100, -1));
    jPanel2.add(jlrNazDok, new XYConstraints(310, 20, 295, -1));
    jPanel2.add(jbDok, new XYConstraints(610, 20, 21, 21));

    jPanel2.add(jlCorg, new XYConstraints(15, 45, -1, -1));
    jPanel2.add(jlrCorg, new XYConstraints(150, 45, 100, -1));
    jPanel2.add(jlrNaziv, new XYConstraints(255, 45, 350, -1));
    jPanel2.add(jbSelCorg, new XYConstraints(610, 45, 21, 21));

    jPanel2.add(jlCskl, new XYConstraints(15, 70, -1, -1));
    jPanel2.add(jlrCskl, new XYConstraints(150, 70, 100, -1));
    jPanel2.add(jlrNazskl, new XYConstraints(255, 70, 350, -1));
    jPanel2.add(jbSelCskl, new XYConstraints(610, 70, 21, 21));

    jPanel2.add(jlCpar, new XYConstraints(15, 95, -1, -1));
    jPanel2.add(jlrCpar, new XYConstraints(150, 95, 100, -1));
    jPanel2.add(jlrNazpar, new XYConstraints(255, 95, 350, -1));
    jPanel2.add(jbSelCpar, new XYConstraints(610, 95, 21, 21));

    jPanel2.add(rcbPLAC, new XYConstraints(150, 120, 100, -1));
    jPanel2.add(jtfZavDatum, new XYConstraints(255, 150, 100, -1));
    jPanel2.add(jtfPocDatum, new XYConstraints(150, 150, 100, -1));
    jPanel2.add(rcbKNJIZ, new XYConstraints(255, 120, 100, -1));
    jPanel2.add(jLabel5, new XYConstraints(15, 150, -1, -1));
    jPanel2.add(jLabel3, new XYConstraints(15, 120, -1, -1));

    new raDateRange(jtfPocDatum, jtfZavDatum);
  }

  String oldCskl, oldCorg;

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

  void dokChanged() {
    System.out.println("dok changed"); //XDEBUG delete when no more needed
    if (tds.getString("VRDOK").length() == 0) {
      if (rcbULIZ.getSelectedIndex() == 0) enabSkladCorg(true, false);
      else enabSkladCorg(true, true);
    } else if (rcbULIZ.getSelectedIndex() == 1) {
      enabSklad(TypeDoc.getTypeDoc().isDocSklad(tds.getString("VRDOK")));
      enabCorg(TypeDoc.getTypeDoc().isDocFinanc(tds.getString("VRDOK")) &&
               !TypeDoc.getTypeDoc().isDocSklad(tds.getString("VRDOK")));
    } else if (rcbULIZ.getSelectedIndex() == 0 && tds.getString("VRDOK").equals("KAL")){
      enabSkladCorg(false, true);
    } else if (rcbULIZ.getSelectedIndex() == 0){
      enabSkladCorg(true, false);
    }
  }

/*
 * Prikazivanje defaultnih vrijednosti
 */
  void showDefaultValues() {
    rcc.EnabDisabAll(jPanel2, true);
    csklEnab = corgEnab = true;
    rcbULIZ.this_itemStateChanged();
    rcbKNJIZ.this_itemStateChanged();
    rcbPLAC.this_itemStateChanged();

    izdok.empty();
    uldok.empty();
    this.getJPTV().clearDataSet();
    this.repaint();
    rcbULIZ.requestFocus();
  }
/*
 * Trazenje SQL query-a u svezi sa vrstom zaduzenja
 */
  String findIRAZ(String vrzal) {
    if (vrzal.equals("V")) {
      return "sum(STDOKI.IMAR) as IMAR, sum(STDOKI.IPOR-STDOKI.IPOR) as IPOR, sum(STDOKI.IRAZ) as IRAZ ";
    }
    else if (vrzal.equals("M")) {
      return "sum(STDOKI.IMAR) as IMAR, sum(STDOKI.IPOR) as IPOR, sum(STDOKI.IRAZ) as IRAZ ";
    }
    else {
      return "sum(STDOKI.IMAR-STDOKI.IMAR) as IMAR, sum(STDOKI.IPOR-STDOKI.IPOR) as IPOR ,sum(STDOKI.IRAZ) as IRAZ ";
    }
  }
/*
 * Pronalazenje iznosa za zaduzenje
 */
  String findIZAD(String vrzal) {
    if (vrzal.equals("V")) {
      return ", sum(STDOKU.IMAR) as IMAR, sum(STDOKU.IPOR-STDOKU.IPOR) as IPOR, sum(STDOKU.IZAD) as IZAD ";
    }
    else if (vrzal.equals("M")) {
      return ", sum(STDOKU.IMAR) as IMAR, sum(STDOKU.IPOR) as IPOR, sum(STDOKU.IZAD) as IZAD ";
    }
    else {
      return ", sum(STDOKU.IMAR-STDOKU.IMAR) as IMAR, sum(STDOKU.IPOR-STDOKU.IPOR) as IPOR, sum(STDOKU.IZAD) as IZAD ";
    }
  }
  String findCPAR(String baza) {
    if (getCpar().equals("")) return "";
    return "AND "+baza.trim()+".CPAR="+getCpar()+" ";
  }
  
  String findVRDOK(String baza) {
    if (tds.getString("VRDOK").trim().equals("")) return "";
    return "AND "+baza.trim()+".VRDOK='"+tds.getString("VRDOK").trim()+"' ";
  }

  String findVRDOKPOR() {
    if (tds.getString("VRDOK").trim().equals("") || tds.getString("VRDOK").trim().equals("POR"))
      return "AND DOKU.VRDOK != 'POR' AND STDOKU.PORAV != 0 ";
    
    return "AND DOKU.VRDOK='-' ";
  }

  String findPLAC(String baza) {
    if (tds.getString("PLAC").trim().equals("O")) return "";
    return "AND "+baza.trim()+".STATPLA='"+tds.getString("PLAC").trim()+"' ";
  }
  
  String findKNJIZ(String baza) {
    if (tds.getString("KNJIZ").trim().equals("O"))  return "";
     return "AND "+baza.trim()+".STATKNJ='"+tds.getString("KNJIZ").trim()+"' ";
  }
  String findMesklaUlaz() {
    if (!getCpar().equals("")) return "";
    if (tds.getString("VRDOK").trim().equals("MES") || tds.getString("VRDOK").trim().equals("MEU") || tds.getString("VRDOK").trim().equals("")) {
      return "UNION SELECT NULL as CPAR, max(MESKLA.DATDOK) as DATDOK, max(MESKLA.BRDOK) as BRDOK, max(MESKLA.VRDOK) as VRDOK, max(MESKLA.CSKLUL) as CSKL, sum(STMESKLA.INABUL-STMESKLA.INABUL) as UIPRPOR, sum(STMESKLA.INABUL-STMESKLA.INABUL) as IDOB, sum(STMESKLA.INABUL-STMESKLA.INABUL) as IRAB, sum(STMESKLA.INABUL-STMESKLA.INABUL) as IZT, sum(STMESKLA.INABUL) as INAB, sum(STMESKLA.IMARUL) as IMAR, sum(STMESKLA.IPORUL) as IPOR, sum(STMESKLA.ZADRAZUL) as IZAD "+
        "from STMESKLA,MESKLA "+
        "where MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ AND MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK "+
        "AND MESKLA.VRDOK in ('MES','MEU') "+
        "AND "+getMesklaCsklul()+" AND MESKLA.DATDOK >= "+rut.getTimestampValue(tds.getTimestamp("pocDatum"), rut.NUM_FIRST)+
        " AND MESKLA.DATDOK <= "+rut.getTimestampValue(tds.getTimestamp("zavDatum"), rut.NUM_LAST)+" "+
        "GROUP BY STMESKLA.CSKLUL, STMESKLA.CSKLIZ, STMESKLA.VRDOK, STMESKLA.BRDOK, STMESKLA.GOD " +
        // poravnanje
        "UNION SELECT 0 as CPAR, max(MESKLA.DATDOK) as DATDOK, max(MESKLA.BRDOK) as BRDOK, 'POR' as VRDOK, max(MESKLA.CSKLUL) as CSKL, sum(STMESKLA.INABUL-STMESKLA.INABUL) as UIPRPOR, sum(STMESKLA.INABUL-STMESKLA.INABUL) as IDOB, sum(STMESKLA.INABUL-STMESKLA.INABUL) as IRAB, sum(STMESKLA.INABUL-STMESKLA.INABUL) as IZT, sum(STMESKLA.INABUL-STMESKLA.INABUL) as INAB, sum(STMESKLA.DIOPORMAR) as IMAR, sum(STMESKLA.DIOPORPOR) as IPOR, sum(STMESKLA.PORAV) as IZAD "+
        "from STMESKLA,MESKLA "+
        "where MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ AND MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK AND STMESKLA.PORAV!=0 "+
        "AND MESKLA.VRDOK in ('MES','MEU') "+
        "AND "+getMesklaCsklul()+" AND MESKLA.DATDOK >= "+rut.getTimestampValue(tds.getTimestamp("pocDatum"), rut.NUM_FIRST)+
        " AND MESKLA.DATDOK <= "+rut.getTimestampValue(tds.getTimestamp("zavDatum"), rut.NUM_LAST)+" "+
        "GROUP BY STMESKLA.CSKLUL, STMESKLA.CSKLIZ, STMESKLA.VRDOK, STMESKLA.BRDOK, STMESKLA.GOD ";
    }
    return "";
  }
  // CAST('' as CHAR(12))
  String findMesklaIzlaz() {
    if (!getCpar().equals("")) return "";
    if ((tds.getString("VRDOK").trim().equals("MES")) || tds.getString("VRDOK").trim().equals("MEI") || (tds.getString("VRDOK").trim().equals(""))) {
      return "UNION SELECT NULL as CPAR, max(MESKLA.DATDOK) as DATDOK, max(MESKLA.DATDOK) as DATDOSP, max(MESKLA.VRDOK) as VRDOK, max(MESKLA.CSKLIZ) as CSKL, max(MESKLA.BRDOK) as BRDOK, NULL as CVRTR, NULL as FBR, NULL as FPP, NULL as FNU, sum(STMESKLA.INABIZ) as INAB, sum(STMESKLA.IMARIZ) as IMAR, sum(STMESKLA.IPORIZ) as IPOR, sum(STMESKLA.ZADRAZIZ) as IRAZ,  sum(STMESKLA.INABUL-STMESKLA.INABUL) as UIRAB, sum(STMESKLA.INABUL-STMESKLA.INABUL) as IPRODBP, sum(STMESKLA.INABUL-STMESKLA.INABUL) as POREZ, sum(STMESKLA.INABUL-STMESKLA.INABUL) as IPRODSP,  sum(STMESKLA.INABUL-STMESKLA.INABUL) as ZARADA "+
       "from STMESKLA,MESKLA "+
        "where MESKLA.CSKLIZ=STMESKLA.CSKLIZ AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK "+
        "AND MESKLA.VRDOK in ('MES','MEI') "+
        "AND "+getMesklaCskliz()+" AND MESKLA.DATDOK >= "+rut.getTimestampValue(tds.getTimestamp("pocDatum"), rut.NUM_FIRST)+
        " AND MESKLA.DATDOK <= "+rut.getTimestampValue(tds.getTimestamp("zavDatum"), rut.NUM_LAST)+" "+
        "GROUP BY STMESKLA.CSKLIZ, STMESKLA.CSKLUL, STMESKLA.VRDOK, STMESKLA.BRDOK, STMESKLA.GOD ";
    }
    return "";
  }

  String getCsklOrCorg() {
    if (!corgEnab) return " AND "+getDokiCskl()+" ";
    
    String inq;
    StorageDataSet corgs = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(getCorg());
    if (corgs.rowCount() == 0) inq = "1=1";
    else if (corgs.rowCount() == 1) inq = "DOKI.CSKL = '" + getCorg() + "'";
    else inq = "(DOKI.CSKL in " + hr.restart.zapod.OrgStr.getOrgStr().getInQuery(corgs,"DOKI.CSKL")+") ";
    if (!csklEnab) return " AND "+inq;
    
    Condition oj = Condition.in("DOKI.VRDOK", TypeDoc.araj_docsOJ);

    return "AND (("+oj+" AND "+inq+") OR ("+oj.not()+" AND "+getDokiCskl()+"))";
  }

  public void jptv_doubleClick() {
    try {
    rut.showDocs(this.getJPTV().getDataSet().getString("CSKL"), this.getJPTV());
    } catch (Exception exc){
      exc.printStackTrace();
    }
  }
  
  public com.borland.dx.dataset.DataSet getQds() {
    return getJPTV().getMpTable().getDataSet();
  }
  public String getAllLabels() {
    VarStr v = new VarStr();
    if (corgEnab) v.append("Org. jedinica :\n");
    if (csklEnab) v.append("Skladište :\n");
    if (getCpar().length() > 0) v.append("Partner :\n");
    if (jlrDok.getText().length() > 0) v.append("Dokumenti :");
    return v.toString();
  }
  public String getAllSifre() {
    VarStr v = new VarStr();
    if (corgEnab) v.append(getCorg()).append('\n');
    if (csklEnab) v.append(getCskl()).append('\n');
    if (getCpar().length() > 0) v.append(getCpar()).append('\n');
    if (jlrDok.getText().length() > 0) v.append(jlrDok.getText());
    return v.toString();
  }
  public String getAllOpisi() {
    VarStr v = new VarStr();
    if (corgEnab) v.append(getNazorg()).append('\n');
    if (csklEnab) {
      if (!getNazSkl().equals("")) v.append(getNazSkl()).append('\n');
      else v.append("Sva skladišta\n");
    }
    if (getCpar().length() > 0) v.append(getNazPar()).append('\n');
    if (jlrDok.getText().length() > 0) v.append(jlrNazDok.getText());
    return v.toString();
  }

  public String getCorg() {
    return tds.getString("CORG");
  }
  public String getNazorg() {
    return jlrNaziv.getText();
  }
  
  private String getCskl(){
    return tds.getString("CSKL");
  }
  
  public String getDokiCskl() {
    String dc = "DOKI.CSKL";
    if (!tds.getString("CSKL").equals("")){
      dc += "='"+tds.getString("CSKL")+"'";
    } else {
      
      QueryDataSet tempSkl = hr.restart.baza.Sklad.getDataModule().getTempSet("Knjig='"+hr.restart.zapod.OrgStr.getKNJCORG()+"'");
      tempSkl.open();
      tempSkl.first();
      String skls = "(";
      
      do {
        skls += "'"+tempSkl.getString("CSKL")+"'";
        if (tempSkl.next()) skls += ",";
        else break;
      } while (true);
      
      skls += ")";
      
      dc += " in "+skls+" ";
    }      
    return dc;
  }
  
  public String getDokuCskl() {
    if (tds.getString("VRDOK").equals("KAL")) return " DOKU.CSKL = '"+tds.getString("CORG")+"' ";
    String dc = "DOKU.CSKL";
    if (!tds.getString("CSKL").equals("")){
      dc += "='"+tds.getString("CSKL")+"'";
    } else {
      
      QueryDataSet tempSkl = hr.restart.baza.Sklad.getDataModule().getTempSet("Knjig='"+hr.restart.zapod.OrgStr.getKNJCORG()+"'");
      tempSkl.open();
      tempSkl.first();
      String skls = "(";
      
      do {
        skls += "'"+tempSkl.getString("CSKL")+"'";
        if (tempSkl.next()) skls += ",";
        else break;
      } while (true);
      
      skls += ")";
      
      dc += " in "+skls+" ";
    }
    return dc;
  }
  
  public String getMesklaCsklul() {
    String dc = "MESKLA.CSKLUL";
    if (!tds.getString("CSKL").equals("")){
      dc += "='"+tds.getString("CSKL")+"'";
    } else {
      
      QueryDataSet tempSkl = hr.restart.baza.Sklad.getDataModule().getTempSet("Knjig='"+hr.restart.zapod.OrgStr.getKNJCORG()+"'");
      tempSkl.open();
      tempSkl.first();
      String skls = "(";
      
      do {
        skls += "'"+tempSkl.getString("CSKL")+"'";
        if (tempSkl.next()) skls += ",";
        else break;
      } while (true);
      
      skls += ")";
      
      dc += " in "+skls+" ";
    }
    return dc;
  }
  
  public String getMesklaCskliz() {
    String dc = "MESKLA.CSKLIZ";
    if (!tds.getString("CSKL").equals("")){
      dc += "='"+tds.getString("CSKL")+"'";
    } else {
      
      QueryDataSet tempSkl = hr.restart.baza.Sklad.getDataModule().getTempSet("Knjig='"+hr.restart.zapod.OrgStr.getKNJCORG()+"'");
      tempSkl.open();
      tempSkl.first();
      String skls = "(";
      
      do {
        skls += "'"+tempSkl.getString("CSKL")+"'";
        if (tempSkl.next()) skls += ",";
        else break;
      } while (true);
      
      skls += ")";
      
      dc += " in "+skls+" ";
    }
    return dc;
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
}