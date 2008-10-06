/****license*****************************************************************
**   file: frmTemelj.java
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

import hr.restart.baza.dM;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.sisfun.raDelayWindow;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.swing.raDateRange;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.OKpanel;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raFrame;
import hr.restart.util.sysoutTEST;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmTemelj extends raFrame {
  _Main ma;
//  sysoutTEST sys = new sysoutTEST(false);
  raCommonClass rcc = raCommonClass.getraCommonClass();
  lookupData ld = lookupData.getlookupData();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();

  raDelayWindow work;
  JPanel jpMain = new JPanel();
  JPanel jpCenter = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlSkl = new JLabel();
  JLabel jlVrdok = new JLabel();
  JlrNavField jlrSkl = new JlrNavField();
  JlrNavField jlrNazSkl = new JlrNavField();
  JlrNavField jlrVrzal = new JlrNavField();
  JlrNavField jlrVrdok = new JlrNavField();
  JlrNavField jlrNazdok = new JlrNavField();
  JraButton jbSelSkl = new JraButton();
  JraButton jbSelDok = new JraButton();
  JraTextField jraDatumOd = new JraTextField();
  JraTextField jraDatumDo = new JraTextField();
  JLabel jlTol = new JLabel();
  JraTextField jraTol = new JraTextField();
  JLabel jlDatum = new JLabel();
  raButtonGroup bg = new raButtonGroup();
  JraRadioButton jrbUkupno = new JraRadioButton() {
    public void setSelected(boolean sel) {
      super.setSelected(sel);
      if (sel) selSvi();
    }
  };
  JraRadioButton jrbUlaz = new JraRadioButton() {
    public void setSelected(boolean sel) {
      super.setSelected(sel);
      if (sel) selUlaz();
    }
  };
  JraRadioButton jrbIzlaz = new JraRadioButton() {
    public void setSelected(boolean sel) {
      super.setSelected(sel);
      if (sel) selIzlaz();
    }
  };
  JraCheckBox jcbPST = new JraCheckBox();
  JLabel jlTemelj = new JLabel();
  JPanel jpDoc = new JPanel();
  XYLayout xyDoc = new XYLayout();
  int last = 0;

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };

  frmTableDataView view = new frmTableDataView(false, false, true);

  static frmTemelj tem;
  hr.restart.util.reports.raRunReport RepRun;
  QueryDataSet temelj = new QueryDataSet();
  QueryDataSet shema = new QueryDataSet();
  StorageDataSet upit = new StorageDataSet();
  Column VRDOK;
  Column NAZDOK;
  Column CSKL;
  Column NAZSKL;
  Column DATDOKfrom;
  Column DATDOKto;
  Column TOL;
  private String invalidList;
  private String unbalancedDok;
  private String naslov;
  private boolean prpor;

  private BigDecimal maxDif;


  public frmTemelj() {
    try {
      tem = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmTemelj getInstance() {
    return tem;
  }

  public DataSet getTemelj() {
    return temelj;
  }

  public String getNaslov() {
    return naslov;
  }

  public void SetFokus() {
    upit.setTimestamp("DATDOKfrom", rut.findFirstDayOfYear(Integer.valueOf(vl.findYear()).intValue()));
    upit.setTimestamp("DATDOKto", vl.findDate(false,0));
    jlrSkl.setText(hr.restart.sisfun.raUser.getInstance().getDefSklad());
    jlrSkl.forceFocLost();
    jlrSkl.requestFocus();
    bg.setSelected(jrbUkupno);
//    jrbUkupno.setSelected();
//    System.out.println("hereee");
  }

  public void cancelPress() {
    this.hide();
  }

  private String imar(String detail) {
    if (!jlrVrzal.getText().equals("N"))
      return "SUM(" + detail + ".imar) as imar, ";
    else return "0 as imar, ";
  }

  private String ipor(String detail) {
    if (jlrVrzal.getText().equals("M"))
      return "SUM(" + detail + ".ipor) as ipor, ";
    else return "0 as ipor, ";
  }

  public void OKPress() {
    jlrSkl.forceFocLost();
    if (jlrSkl.getText().trim().equals("")) {
      jlrSkl.requestFocus();
      JOptionPane.showMessageDialog(jlrSkl,"Obavezan unos skladišta !","Greška",JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (!Aus.checkDateRange(jraDatumOd, jraDatumDo))
      return;
    jlSkl.requestFocus();
    maxDif = upit.getBigDecimal("TOL");
    new Thread() {
      public void run() {
        perform();
      }
    }.start();
  }

  BigDecimal totalIn, totalOut;
  QueryDataSet invalidDocs;

  public boolean checkBad(String vrs) {
    if (invalidDocs.rowCount() == 0) return false;
    work.close();
    if (JOptionPane.showConfirmDialog(this.getWindow(),
      "Neki "+ vrs + " dokumenti nisu balansirani! Pregled?", "Greška",
      JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
      if (view.isShowing()) view.hide();

      invalidDocs.getColumn("OSTRAS").setCaption("Manjak");
      invalidDocs.getColumn("OSTPRI").setCaption("Višak");

      int ci = 0;
      int[] cols = new int[6];
      view.clearColumns();
      for (int i = 0; i < invalidDocs.columnCount(); i++) {
        view.addColumn(invalidDocs.getColumn(i));
        if (i < 4 || i >= invalidDocs.columnCount() - 2)
          cols[ci++] = i;
      }

      view.setDataSet(invalidDocs);
      view.setTitle("Pregled nebalansiranih "+vrs+"h dokumenata");
      view.setVisibleCols(cols);
      view.show();
      view.resizeLater();
    }
    return true;
  }

  public Column getBigDecimalColumn(String name) {
    Column col = new Column();
    col.setColumnName(name);
    col.setCaption(name);
    col.setDataType(Variant.BIGDECIMAL);
    col.setPrecision(15);
    col.setScale(2);
    return col;
  }

  private void perform() {
    work = raDelayWindow.show(this.getWindow());
    boolean ulazni = jrbUkupno.isSelected() || jrbUlaz.isSelected();
    boolean izlazni = jrbUkupno.isSelected() || jrbIzlaz.isSelected();
    if (ulazni && izlazni) naslov = "TEMELJNICA";
    else naslov = ulazni ? "TEMELJNICA - ULAZI" : "TEMELJNICA - IZLAZI";
    if (!upit.getString("VRDOK").equals("")) naslov = naslov + " (" + upit.getString("VRDOK") + ")";
    prpor = hr.restart.sisfun.frmParam.getParam("robno","temPrpor").equals("D");

    String datfrom = rut.getTimestampValue(upit.getTimestamp("DATDOKfrom"), rut.NUM_FIRST);
    String datto = rut.getTimestampValue(upit.getTimestamp("DATDOKto"), rut.NUM_LAST);
    String cskl = upit.getString("CSKL");
    String irab, irabval, iprp, iprpval;

    vl.runSQL("DELETE FROM temelj");
    dm.getTemelj().refresh();
    dm.getKonta_par().open();
    dm.getPartneri().open();

    if (hr.restart.sisfun.frmParam.getParam("robno","temRabat").equals("D")) {
      irab = "SUM(stdoku.irab) as irab, ";
      irabval = "";
    } else {
      irab = "0 as irab, ";
      irabval = " - SUM(stdoku.irab)";
    }
    if (prpor) {
      iprp = "MAX(doku.uiprpor) as prpor";
      iprpval = " + MAX(doku.uiprpor)";
    } else {
      iprp = "0 as prpor";
      iprpval = "";
    }

    System.out.println("Skladište "+cskl);
    invalidList = "";
    int rval = 1;

    if (ulazni) {
      work.setMessage("Knjiženje ulaznih dokumenata...", true, 750);
    String ulaz = "SELECT MAX(doku.vrdok) as vrdok, MAX(doku.cskl) as cskl, "+
                  "MAX(doku.god) as god, MAX(doku.brdok) as brdok, MAX(doku.cpar) as cpar, "+
                  "SUM(stdoku.izad)+SUM(stdoku.porav) as izad, "+
                  imar("stdoku")+ipor("stdoku")+irab+
                  "SUM(stdoku.inab) as inab, "+
                  "SUM(stdoku.idob)"+iprpval+irabval+" as idob, SUM(stdoku.izt) as izt, "+
                  "SUM(stdoku.diopormar) as diopormar, SUM(stdoku.dioporpor) as dioporpor, "+
                  iprp + ", 0 as ostras, 0 as ostpri "+
                  "FROM doku,stdoku "+
                  "WHERE "+rut.getDoc("doku","stdoku")+
                  " AND doku.cskl = '"+cskl+
                  "' AND doku.datdok >= "+datfrom+" and doku.datdok <= "+datto+
                  " GROUP BY doku.vrdok, doku.cskl, doku.god, doku.brdok ORDER BY doku.vrdok";

//    System.out.println(ulaz);
    rval = Math.min(rval, process(ulaz, new Column[] {dm.getDoku().getColumn("VRDOK"),
      dm.getDoku().getColumn("CSKL"),dm.getDoku().getColumn("GOD"),
      dm.getDoku().getColumn("BRDOK"),dm.getDoku().getColumn("CPAR")},
      new String[] {"IZAD", "IMAR", "IPOR", "IRAB", "INAB", "IDOB", "IZT", "DIOPORMAR",
                    "DIOPORPOR", "PRPOR", "OSTRAS", "OSTPRI"}));
      if (checkBad("ulazni")) return;
    }

    if (izlazni) {
      work.setMessage("Knjiženje izlaznih dokumenata...", true, 750);
    String izlaz = "SELECT MAX(doki.vrdok) as vrdok, MAX(doki.cskl) as cskl, "+
                   "MAX(doki.god) as god, MAX(doki.brdok) as brdok, "+
                   "MAX(doki.cvrtr) as cvrtr, MAX(doki.cnacpl) as cnacpl, "+
                   "SUM(stdoki.iraz) as iraz, SUM(stdoki.inab) as inab, "+
                   imar("stdoki")+ipor("stdoki")+" SUM(stdoki.iprodsp) as iprodsp, "+
                   "SUM(stdoki.iprodbp) as iprodbp, SUM(stdoki.por1) as por1, "+
                   "SUM(stdoki.por2) as por2, SUM(stdoki.por3) as por3, "+
                   "SUM(stdoki.uizt) as izt, 0 as ostras, 0 as ostpri "+
                   "FROM doki,stdoki "+
                   "WHERE "+rut.getDoc("doki","stdoki")+" AND stdoki.vrdok != 'RNL'"+
                   " AND doki.cskl = '"+cskl+
                   "' AND doki.datdok >= "+datfrom+" and doki.datdok <= "+datto+
                   " GROUP BY doki.vrdok, doki.cskl, doki.god, doki.brdok ORDER BY doki.vrdok";

    rval = Math.min(rval, process(izlaz, new Column[] {dm.getDoki().getColumn("VRDOK"),
      dm.getDoku().getColumn("CSKL"),dm.getDoku().getColumn("GOD"),
      dm.getDoku().getColumn("BRDOK"), dm.getDoki().getColumn("CVRTR"),
      dm.getDoki().getColumn("CNACPL")},
      new String[] {"IRAZ", "INAB", "IMAR", "IPOR", "IPRODSP", "IPRODBP",
                    "POR1", "POR2", "POR3", "IZT", "OSTRAS", "OSTPRI"}));
      if (checkBad("izlazni")) return;
    }


    if (ulazni) {
    String ulazmes = "SELECT 'MEU' as vrdok, "+
                     "SUM(stmeskla.zadrazul) + SUM(stmeskla.porav) as zadrazul, "+
                     "SUM(stmeskla.inabul) as inabul, SUM(stmeskla.imarul) as imarul, "+
                     "SUM(stmeskla.iporul) as iporul, SUM(stmeskla.dioporpor) as dioporpor, "+
                     "SUM(stmeskla.diopormar) as diopormar "+
                     "FROM meskla,stmeskla "+
                     "WHERE meskla.csklul = stmeskla.csklul AND meskla.cskliz = stmeskla.cskliz "+
                     "AND meskla.vrdok = stmeskla.vrdok AND meskla.brdok = stmeskla.brdok "+
                     "AND meskla.god = stmeskla.god AND meskla.csklul = '"+cskl+
                     "' AND meskla.datdok >= "+datfrom+" and meskla.datdok <= "+datto+
                     " GROUP BY meskla.vrdok, meskla.csklul, meskla.god, meskla.brdok ORDER BY meskla.vrdok";

    rval = Math.min(rval, process(ulazmes, new Column[] {dm.getMeskla().getColumn("VRDOK")},
      new String[] {"ZADRAZUL", "INABUL", "IMARUL", "IPORUL", "DIOPORPOR", "DIOPORMAR"}));
    }
    if (izlazni) {
    String izlames = "SELECT 'MEI' as vrdok, SUM(stmeskla.zadraziz) as zadraziz, "+
                     "SUM(stmeskla.inabiz) as inabiz, SUM(stmeskla.imariz) as imariz, "+
                     "SUM(stmeskla.iporiz) as iporiz "+
                     "FROM meskla,stmeskla "+
                     "WHERE meskla.csklul = stmeskla.csklul AND meskla.cskliz = stmeskla.cskliz "+
                     "AND meskla.vrdok = stmeskla.vrdok AND meskla.brdok = stmeskla.brdok "+
                     "AND meskla.god = stmeskla.god AND meskla.cskliz = '"+cskl+
                     "' AND meskla.datdok >= "+datfrom+" and meskla.datdok <= "+datto+
                     " GROUP BY meskla.vrdok, meskla.cskliz, meskla.god, meskla.brdok ORDER BY meskla.vrdok";

    rval = Math.min(rval, process(izlames, new Column[] {dm.getMeskla().getColumn("VRDOK")},
      new String[] {"ZADRAZIZ", "INABIZ", "IMARIZ", "IPORIZ"}));
    }

    /*if (ulazni) {
      String ulaz = "SELECT stdoku.vrdok, vtzavtr.czt, vtzavtr.izt, vtzavtr.uiprpor "+
                    "FROM doku, stdoku, vtzavtr WHERE "+rut.getDoc("doku", "stdoku")+" AND "+
                    rut.getDoc("stdoku", "vtzavtr")+" AND stdoku.rbr = vtzavtr.rbr AND "+
                    "doku.cskl = '"+cskl+"' AND doku.datdok >= "+datfrom+" and doku.datdok <= "+datto+
                    " ORDER BY stdoku.vrdok";
      processZavtr(ulaz, true);
    }
    if (izlazni) {
      String izlaz = "SELECT stdoki.vrdok, vtzavtr.czt, vtzavtr.izt, 0 as uiprpor "+
                     "FROM doki, stdoki, vtzavtr WHERE "+rut.getDoc("doki", "stdoki")+" AND "+
                     " stdoki.vrdok != 'RNL' AND "+
                     rut.getDoc("stdoki", "vtzavtr")+" AND stdoki.rbr = vtzavtr.rbr AND "+
                     "doki.cskl = '"+cskl+"' AND doki.datdok >= "+datfrom+" and doki.datdok <= "+datto+
                     " ORDER BY stdoki.vrdok";
      processZavtr(izlaz, false);
    }
*/
    vl.execSQL("SELECT * FROM temelj");
    vl.RezSet.open();

//    hr.restart.zapod.raKonta.isSaldak()


    if (vl.RezSet.rowCount() > 0 && rval == 0) {
      prepareForPrint();
      jlrSkl.requestFocus();
//      this.hide();
    } else {
      work.close();
      jlrSkl.requestFocus();
      if (rval == 1)
        JOptionPane.showMessageDialog(this.jpMain,"Nema prometa na skladištu !","Upozorenje",JOptionPane.WARNING_MESSAGE);
      else if (!invalidList.equals(""))
        JOptionPane.showMessageDialog(this.jpMain,"Nema sheme kontiranja za dokumente "+invalidList+"za traženo skladište!","Greška",JOptionPane.ERROR_MESSAGE);
      else
        JOptionPane.showMessageDialog(this.jpMain,"Dokumenti tipa "+unbalancedDok+" nisu balansirani a ne postoji konto za razliku!", "Greška",JOptionPane.ERROR_MESSAGE);
//      prepareForPrint();
//      this.hide();
      jlrSkl.requestFocus();
    }
  }

  private BigDecimal addToKonto(String konto, String karak, BigDecimal iznos) {
    String tip;
    System.out.println("ADD "+iznos+" to konto "+konto);
    if (karak.equals("D") || karak.equals("I")) tip = "IZNOSDUG";
    else tip = "IZNOSPOT";
    if (!ld.raLocate(dm.getTemelj(), new String[] {"CSKL", "BROJKONTA"},
          new String[] {upit.getString("CSKL"), konto})) {
      dm.getTemelj().insertRow(false);
      dm.getTemelj().setString("CSKL", upit.getString("CSKL"));
      dm.getTemelj().setString("UI", tip.equals("IZNOSDUG") ? "I" : "U");
      dm.getTemelj().setString("BROJKONTA", konto);
      dm.getTemelj().post();
    }
    dm.getTemelj().setBigDecimal(tip, dm.getTemelj().getBigDecimal(tip).add(iznos));
    if (tip.equals("IZNOSDUG")) return iznos;
    else return iznos.negate();
  }

  private String getKontoPar(String defaultKonto, int cpar, boolean ulaz) {
    String konto = defaultKonto;
    if (ld.raLocate(dm.getPartneri(), "CPAR", String.valueOf(cpar))) {
      boolean dom = !dm.getPartneri().getString("DI").equals("I");
      if (dm.getKonta_par().rowCount() > 0) {
        if (dom) if (ulaz) konto = dm.getKonta_par().getString("DOB_TUZ");
        else konto = dm.getKonta_par().getString("KUP_TUZ");
        else if (ulaz) konto = dm.getKonta_par().getString("DOB_INO");
        else konto = dm.getKonta_par().getString("KUP_INO");
        if (konto.equals("")) konto = defaultKonto;
      }
    }
    return konto;
  }

  private BigDecimal transferZT(String czt, BigDecimal izt, BigDecimal uiprpor, boolean ulaz) {
    String kontopar;
    BigDecimal retval = new BigDecimal(0);
    BigDecimal rest = izt.add(uiprpor);
    if (ld.raLocate(dm.getZavtr(), "CZT", czt)) {
      if (!dm.getZavtr().getString("BROJKONTA2").equals("") && uiprpor.signum() != 0) {
        retval = retval.add(uiprpor);
        rest = rest.subtract(uiprpor);
        addToKonto(dm.getZavtr().getString("BROJKONTA2"), shema.getString("KARAKTERISTIKA"), uiprpor);
      }
      if (!dm.getZavtr().getString("BROJKONTA").equals("") && izt.signum() != 0) {
        retval = retval.add(izt);
        rest = rest.subtract(izt);
        addToKonto(dm.getZavtr().getString("BROJKONTA"), shema.getString("KARAKTERISTIKA"), izt);
      }
      if (rest.signum() != 0) {
        kontopar = getKontoPar("", dm.getZavtr().getInt("CPAR"), ulaz);
        if (!kontopar.equals("")) {
          retval = retval.add(rest);
          addToKonto(kontopar, shema.getString("KARAKTERISTIKA"), rest);
        }
      }
    }
    return retval;
  }

  private void updateShemaZT(BigDecimal total) {
    if (total.signum() != 0) {
      if (ld.raLocate(dm.getTemelj(), new String[] {"CSKL", "BROJKONTA"},
          new String[] {upit.getString("CSKL"), shema.getString("BROJKONTA")})) {
          System.out.println("konto "+shema.getString("BROJKONTA")+"   dug "+dm.getTemelj().getBigDecimal("IZNOSDUG")+"   pot "+dm.getTemelj().getBigDecimal("IZNOSPOT")+"  total "+total);
        if (shema.getString("KARAKTERISTIKA").equals("D"))
          dm.getTemelj().setBigDecimal("IZNOSDUG", dm.getTemelj().getBigDecimal("IZNOSDUG").subtract(total));
        else
          dm.getTemelj().setBigDecimal("IZNOSPOT", dm.getTemelj().getBigDecimal("IZNOSPOT").subtract(total));
      }
    }
  }

  private void processZavtr(String sql, boolean ulaz) {
    sysoutTEST sys = new sysoutTEST(false);

    BigDecimal total = new BigDecimal(0);
    QueryDataSet dok = new QueryDataSet();
    String lastdok = "";
    dm.getPartneri().open();
    dm.getZavtr().open();
    dm.getPartneri().enableDataSetEvents(false);
    dm.getZavtr().enableDataSetEvents(false);

    dok.setQuery(new QueryDescriptor(dm.getDatabase1(),sql));
    dok.setColumns(new Column[] {
      (Column) dm.getVtzavtr().getColumn("VRDOK").clone(),
      (Column) dm.getZavtr().getColumn("CZT").clone(),
      dm.createBigDecimalColumn("IZT"),
      dm.createBigDecimalColumn("UIPRPOR")
    });
    dok.open();
    sys.prn(dok);

    for (dok.first(); dok.inBounds(); dok.next()) {
      if (!dok.getString("VRDOK").equals(lastdok)) {
        System.out.println("update "+total);
        updateShemaZT(total);
        lastdok = dok.getString("VRDOK");
        total = new BigDecimal(0);
        shema.close();
        shema.closeStatement();
        shema.setQuery(new QueryDescriptor(dm.getDatabase1(),
          "SELECT * FROM shkonta WHERE cskl = '"+upit.getString("CSKL")+
          "' AND vrdok = '"+lastdok+"' AND polje = 'IZT'"
        ));
        shema.open();
        System.out.println("novi dok "+lastdok);
      }
      total = total.add(transferZT(dok.getString("CZT"), dok.getBigDecimal("IZT"), dok.getBigDecimal("UIPRPOR"), ulaz));
    }
    updateShemaZT(total);
    dm.getPartneri().enableDataSetEvents(true);
    dm.getZavtr().enableDataSetEvents(true);
    dm.getTemelj().saveChanges();
  }

  private boolean knjiz(String vrdok) {
    if (vrdok.equals("PST") && jcbPST.isSelected()) return true;
    if (!upit.getString("VRDOK").equals("") && !upit.getString("VRDOK").equals(vrdok)) return false;
    if (TypeDoc.getTypeDoc().isDoc4Temelj(vrdok)) return true;
    return false;
  }

  BigDecimal total;
  String lastdok, ostras, ostpri;
  private int process(String sql, Column[] firstCols, String[] colNames) {
    QueryDataSet dok = new QueryDataSet();
    invalidDocs = new QueryDataSet();
    Column[] columnList = new Column[colNames.length + firstCols.length];
    int ret = 1;

    for (int i = 0; i < firstCols.length; i++)
      columnList[i] = (Column) firstCols[i].clone();
    for (int i = 0; i < colNames.length; i++)
      columnList[i + firstCols.length] = getBigDecimalColumn(colNames[i]);

    dok.setQuery(new QueryDescriptor(dm.getDatabase1(),sql));
    dok.setColumns(columnList);
    invalidDocs.setColumns(dok.cloneColumns());
    dok.open();
    invalidDocs.open();
    invalidDocs.empty();

//    sysoutTEST sys = new sysoutTEST(false);
//    sys.prn(dok);

    lastdok = "";
    total = new BigDecimal(0);
    ostras = null;
    ostpri = null;
    BigDecimal lastT;
    for (dok.first(); dok.inBounds(); dok.next()) {
      if (knjiz(dok.getString("VRDOK"))) {
        lastT = total;
        if (!dok.getString("VRDOK").equalsIgnoreCase(lastdok)) lastT = ma.nul;
        ret = Math.min(updateDok(dok), ret);
        lastT = lastT.subtract(total);
        if (lastT.abs().compareTo(maxDif) > 0) {
          invalidDocs.insertRow(false);
          dok.copyTo(invalidDocs);
          if (lastT.signum() > 0)
            invalidDocs.setBigDecimal("OSTPRI", lastT);
          else
            invalidDocs.setBigDecimal("OSTRAS", lastT.abs());
        }
      }
    }

    if (dok.rowCount() > 0 && !lastdok.equals("") && knjiz(lastdok))
      ret = Math.min(checkBal(lastdok, total), ret);

    dm.getTemelj().saveChanges();
/*    if (dok.rowCount() == 0)
      return 1;
    else */

    return ret;
  }

  private int checkBal(String vrdok, BigDecimal total) {
    String ost, setp, karak;
    if (total.signum() == 0) {
      System.out.println(vrdok + " balansiran!");
    } else {
      System.out.println(vrdok + " nije balansiran! dug = " + total);
      if (total.signum() > 0) {
        ost = ostpri;
        karak = "U";
      } else {
        ost = ostras;
        total = total.negate();
        karak = "I";
      }
      if (ost == null) {
        System.out.println("unbalanc "+vrdok);
        unbalancedDok = vrdok;
        return -2;
      } else addToKonto(ost, karak, total);
    }
    return 0;
  }

  private int updateDok(QueryDataSet dok) {
    String vrdok = dok.getString("VRDOK");
    String cskl = upit.getString("CSKL");

    String konto;
    BigDecimal iznos;
    int retval = 0;

//    System.out.println(dok);
//    System.out.println("shema " + vrdok + " za " + cskl);
    if (!vrdok.equals(lastdok)) {
      System.out.println("Novi dokument: " + vrdok);
      if (!lastdok.equals(""))
        retval = checkBal(lastdok, total);
//      System.out.println("retval "+retval);
      total = new BigDecimal(0);
      ostpri = null;
      ostras = null;
      shema.close();
      shema.closeStatement();
      shema.setQuery(new QueryDescriptor(dm.getDatabase1(),
        "SELECT * FROM shkonta WHERE cskl = '"+cskl+"' AND vrdok = '"+vrdok+"'"
      ));
      shema.open();
    }
    if (shema.rowCount() == 0) {
      if (!vrdok.equals(lastdok)) {
        lastdok = vrdok;
        invalidList = invalidList + vrdok + ", ";
        System.out.println(invalidList);
      }
//      System.out.println("return -1");
      return -1;
    }
    lastdok = vrdok;

    for (shema.first(); shema.inBounds(); shema.next()) {
      System.out.println(cskl + " " + vrdok + "  konto " + shema.getString("BROJKONTA") + " polje " + shema.getString("POLJE"));
      try {
       iznos = dok.getBigDecimal(shema.getString("POLJE"));
      } catch (Exception e) {
        e.printStackTrace();
       continue;
      }

// HARDCODED  ostatak pri zaokruživanju
      if (shema.getString("POLJE").equalsIgnoreCase("ostras"))
        ostras = shema.getString("BROJKONTA");
      if (shema.getString("POLJE").equalsIgnoreCase("ostpri"))
        ostpri = shema.getString("BROJKONTA");
// /HARDCODED

      if (iznos.signum() != 0) {
        iznos = iznos.setScale(2, BigDecimal.ROUND_HALF_UP);
//        System.out.print("    ... " + iznos);
        konto = shema.getString("BROJKONTA");
// HARDCODED  vrste troška
        if (shema.getString("POLJE").equalsIgnoreCase("INAB") &&
            dok.hasColumn("CVRTR") != null && !dok.getString("CVRTR").equals("")) {
          if (ld.raLocate(dm.getVrtros(), "CVRTR", dok.getString("CVRTR")))
            if (!dm.getVrtros().getString("BROJKONTA").equals(""))
              konto = dm.getVrtros().getString("BROJKONTA");
        }
// /HARDCODED

// HARDCODED  iznos dobavlja\u010Da
        if (shema.getString("POLJE").equalsIgnoreCase("IDOB") &&
            dok.hasColumn("CPAR") != null && dok.getInt("CPAR") != 0) {
          konto = getKontoPar(konto, dok.getInt("CPAR"), true);
        }
// /HARDCODED

// HARDCODED  na\u010Din pla\u0107anja
        if (shema.getString("POLJE").equalsIgnoreCase("IPRODSP") &&
            (vrdok.equalsIgnoreCase("POS") || vrdok.equalsIgnoreCase("GOT")))
          konto = findNacplKonto(konto);
// /HARDCODED

        total = total.add(addToKonto(konto, shema.getString("KARAKTERISTIKA"), iznos));
        System.out.println("      total: " + total);
      }
    }
    return retval;
  }

  private String findNacplKonto(String defaultKonto) {
    return defaultKonto;
  }

  private void prepareForPrint() {
    temelj.close();
    temelj.closeStatement();
    temelj.setQuery(new QueryDescriptor(dm.getDatabase1(),
      "SELECT temelj.*, konta.nazivkonta FROM temelj,konta WHERE temelj.brojkonta=konta.brojkonta"
    ));
    temelj.setColumns(new Column[] {
      (Column) dm.getTemelj().getColumn("CSKL").clone(),
      (Column) dm.getTemelj().getColumn("BROJKONTA").clone(),
      (Column) dm.getKonta().getColumn("NAZIVKONTA").clone(),
      (Column) dm.getTemelj().getColumn("IZNOSDUG").clone(),
      (Column) dm.getTemelj().getColumn("IZNOSPOT").clone()
    });
    temelj.open();
    work.setMessage("U\u010Ditavanje reporta...", false, 500);
    getRepRunner().clearAllReports();
    getRepRunner().addReport("hr.restart.robno.repTemelj", "Ispis temeljnice", 42);
    /*getRepRunner().setProviderClassName("hr.restart.robno.repTemelj");
    getRepRunner().setReportTitles(new String[] {"Ispis temeljnice"});
    getRepRunner().*/
    work.close();
    getRepRunner().go();
  }

  public java.sql.Timestamp getPocDatum() {
    return upit.getTimestamp("DATDOKfrom");
  }

  public java.sql.Timestamp getZavDatum() {
    return upit.getTimestamp("DATDOKto");
  }

  public String getNazSkl() {
    return upit.getString("NAZSKL");
  }

  public hr.restart.util.reports.raRunReport getRepRunner() {
    if (RepRun == null) {
      RepRun = hr.restart.util.reports.raRunReport.getRaRunReport();
      RepRun.setOwner(this.getWindow(), getClass().getName());
    }
    return RepRun;
  }

  private void jbInit() throws Exception {
    xYLayout1.setWidth(570);
    xYLayout1.setHeight(140);
    jpCenter.setLayout(xYLayout1);

    jlTemelj.setText("Dokumenti");
//    bg.setHorizontalTextPosition(SwingConstants.TRAILING);
    bg.setHorizontalAlignment(SwingConstants.LEADING);
    bg.add(jrbUkupno, " Svi ");
    bg.add(jrbUlaz, " Ulazni ");
    bg.add(jrbIzlaz, " Izlazni ");
    jcbPST.setText(" PST ");
    jcbPST.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbPST.setHorizontalAlignment(SwingConstants.LEADING);
    CSKL = (Column) dm.getSklad().getColumn("CSKL").clone();
    NAZSKL = (Column) dm.getSklad().getColumn("NAZSKL").clone();
    VRDOK = (Column) dm.getVrdokum().getColumn("VRDOK").clone();
    NAZDOK = (Column) dm.getVrdokum().getColumn("NAZDOK").clone();
    DATDOKfrom = dm.createTimestampColumn("DATDOKfrom");
    DATDOKto = dm.createTimestampColumn("DATDOKto");
    TOL = dm.createBigDecimalColumn("TOL");
    upit.setColumns(new Column[] {CSKL, NAZSKL, VRDOK, NAZDOK, DATDOKfrom, DATDOKto, TOL});

    jlrSkl.setColumnName("CSKL");
    jlrSkl.setTextFields(new JTextComponent[] {jlrNazSkl, jlrVrzal});
    jlrSkl.setColNames(new String[] {"NAZSKL", "VRZAL"});
    jlrSkl.setSearchMode(0);
    jlrSkl.setDataSet(upit);
    jlrSkl.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
    jlrSkl.setVisCols(new int[] {0,1});
    jlrSkl.setNavButton(jbSelSkl);

    jlrNazSkl.setColumnName("NAZSKL");
    jlrNazSkl.setNavProperties(jlrSkl);
    jlrNazSkl.setDataSet(upit);
    jlrNazSkl.setSearchMode(1);

    jlrVrdok.setColumnName("VRDOK");
    jlrVrdok.setTextFields(new JTextComponent[] {jlrNazdok});
    jlrVrdok.setColNames(new String[] {"NAZDOK"});
    jlrVrdok.setSearchMode(0);
    jlrVrdok.setDataSet(upit);
    jlrVrdok.setRaDataSet(dm.getVrdokum());
    jlrVrdok.setVisCols(new int[] {0,1});
    jlrVrdok.setNavButton(jbSelDok);

    jlrNazdok.setColumnName("NAZDOK");
    jlrNazdok.setNavProperties(jlrVrdok);
    jlrNazdok.setDataSet(upit);
    jlrNazdok.setSearchMode(1);

    jlrVrzal.setColumnName("VRZAL");
    jlrVrzal.setNavProperties(jlrSkl);
    jlrVrzal.setSearchMode(1);
    jlrVrzal.setVisible(false);
    jlrVrzal.setEnabled(false);

    jlrSkl.setHorizontalAlignment(SwingConstants.TRAILING);
    jlSkl.setText("Skladište");
    jlVrdok.setText("Vrsta");
    jbSelSkl.setText("...");
    jbSelDok.setText("...");
    jraDatumOd.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumOd.setDataSet(upit);
    jraDatumOd.setColumnName("DATDOKfrom");
    jraDatumDo.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumDo.setDataSet(upit);
    jraDatumDo.setColumnName("DATDOKto");
    jlDatum.setText("Datum (od - do)");
    jlTol.setText("Odstupanje");
    jraTol.setColumnName("TOL");
    jraTol.setDataSet(upit);
    jpCenter.add(jlSkl, new XYConstraints(15, 20, -1, -1));
    jpCenter.add(jlrSkl, new XYConstraints(150, 20, 100, -1));
    jpCenter.add(jlrNazSkl, new XYConstraints(255, 20, 275, -1));
    jpCenter.add(jbSelSkl, new XYConstraints(535, 20, 21, 21));
    jpCenter.add(jlrVrzal, new XYConstraints(400, 45, 50, -1));
    jpCenter.add(jraDatumOd, new XYConstraints(150, 45, 100, -1));
    jpCenter.add(jraDatumDo, new XYConstraints(255, 45, 100, -1));
    jpCenter.add(jlDatum, new XYConstraints(15, 45, -1, -1));
    jpCenter.add(jlTol, new XYConstraints(380, 45, -1, -1));
    jpCenter.add(jraTol, new XYConstraints(455, 45, 75, -1));

    jpCenter.add(jlTemelj, new XYConstraints(15, 75, -1, -1));

    jpCenter.add(jlVrdok, new XYConstraints(15, 103, -1, -1));
    jpCenter.add(jlrVrdok, new XYConstraints(150, 103, 100, -1));
    jpCenter.add(jlrNazdok, new XYConstraints(255, 103, 275, -1));
    jpCenter.add(jbSelDok, new XYConstraints(535, 103, 21, 21));
//    jpCenter.add(jrbUkupno, new XYConstraints(150, 70, -1, -1));
//    jpCenter.add(jrbUlaz, new XYConstraints(300, 70, -1, -1));
//    jpCenter.add(jrbIzlaz, new XYConstraints(450, 70, -1, -1));

    jpCenter.setBorder(BorderFactory.createEtchedBorder());

    jpDoc.setLayout(xyDoc);
    xyDoc.setWidth(376);
    xyDoc.setHeight(25);
    jpDoc.setBorder(BorderFactory.createEtchedBorder());
    jpDoc.add(jrbUkupno, new XYConstraints(10, 0, 60, -1));
    jpDoc.add(jrbUlaz, new XYConstraints(95, 0, 60, -1));
    jpDoc.add(jrbIzlaz, new XYConstraints(200, 0, 60, -1));
    jpDoc.add(jcbPST, new XYConstraints(320, 0, 55, -1));

    jpCenter.add(jpDoc, new XYConstraints(150, 70, -1, -1));

    jpMain.setLayout(new BorderLayout());
    jpMain.add(jpCenter, BorderLayout.CENTER);
    jpMain.add(okp, BorderLayout.SOUTH);

    view.setSize(640, 400);
    view.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 320, 200);
    view.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

    new raDateRange(jraDatumOd, jraDatumDo);
    this.getContentPane().add(jpMain);
//    this.getContentPane().add(new raMiniEditor());

    this.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        this_keyPressed(e);
      }
    });
    jrbUkupno.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        selSvi();
      }
    });
    jrbIzlaz.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        selIzlaz();
      }
    });
    jrbUlaz.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        selUlaz();
      }
    });
  }

  private void selSvi() {
    if (last != 1) {
      last = 1;
      if (!jcbPST.isEnabled()) jcbPST.setEnabled(true);
      jlrVrdok.setText("");
      jlrVrdok.forceFocLost();
      setVrdokEnabled(false);
    }
  }

  private void selUlaz() {
    if (last != 2) {
      last = 2;
      if (!jcbPST.isEnabled()) jcbPST.setEnabled(true);
      if (!jlrVrdok.isEnabled()) setVrdokEnabled(true);
      jlrVrdok.setText("");
      jlrVrdok.forceFocLost();
      jlrVrdok.setRaDataSet(dm.getVrdokumUlazni());
    }
  }

  private void selIzlaz() {
    if (last != 3) {
      last = 3;
      if (!jlrVrdok.isEnabled()) setVrdokEnabled(true);
      jcbPST.setSelected(false);
      jcbPST.setEnabled(false);
      jlrVrdok.setText("");
      jlrVrdok.forceFocLost();
      jlrVrdok.setRaDataSet(dm.getVrdokumIzlazni());
    }
  }

  private void this_keyPressed(KeyEvent e) {
    if (e.getKeyCode()==e.VK_F10) {
      OKPress();
    } else if (e.getKeyCode()==e.VK_ESCAPE) {
      cancelPress();
    }
  }

  private void setVrdokEnabled(boolean yesno) {
    rcc.setLabelLaF(jlrVrdok, yesno);
    rcc.setLabelLaF(jlrNazdok, yesno);
    rcc.setLabelLaF(jbSelDok, yesno);
  }

  public void show() {
    maxDif = new BigDecimal("0.1");
    try {
      maxDif = new BigDecimal(hr.restart.sisfun.frmParam.getParam("robno", "temMaxtol").replace(',', '.'));
    } catch (Exception e) {}
    upit.setBigDecimal("TOL", maxDif);
    pack();
    setVrdokEnabled(false);
    super.show();
    SetFokus();
  }
}
