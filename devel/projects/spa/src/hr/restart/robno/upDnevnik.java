/****license*****************************************************************
**   file: upDnevnik.java
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
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpit;

import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class upDnevnik extends raUpit {
  _Main ma;
  dM dm;
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();

  JLabel jlDatum = new JLabel();
  XYLayout xYLayout1 = new XYLayout();
  JraTextField jraDatumOd = new JraTextField();
  JPanel jpUpit = new JPanel();
  JraTextField jraDatumDo = new JraTextField();

  private rapancskl1 rpcskl = new rapancskl1(349) {
    public void findFocusAfter() {
      jraDatumOd.requestFocusLater();
    }
    public void MYpost_after_lookUp(){
      jraDatumOd.requestFocusLater();
    }
  };

  StorageDataSet upit = new StorageDataSet();
  Column CSKL;
  Column NAZSKL;
  Column DATDOKfrom;
  Column DATDOKto;
  BigDecimal totalUlaz, totalIzlaz;
  private static upDnevnik upd;

  public upDnevnik() {
    try {
      upd = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public static upDnevnik getInstance() {
    return upd;
  }
  public com.borland.dx.dataset.DataSet getQds() {
    return getJPTV().getMpTable().getDataSet();
  }
  public com.borland.dx.dataset.DataSet getUpit() {
    return upit;
  }
  public BigDecimal getTotUlaz() {
    return totalUlaz;
  }
  public BigDecimal getTotIzlaz() {
    return totalIzlaz;
  }

  public void componentShow() {
    showDefaults();
    rpcskl.setCSKL(hr.restart.sisfun.raUser.getInstance().getDefSklad());
  }

  String vrzal;
  public boolean Validacija() {
    if (rpcskl.getCSKL().equals("")) {
      JOptionPane.showMessageDialog(null,"Obavezan unos skladišta !","Greška",JOptionPane.ERROR_MESSAGE);
      rpcskl.jrfCSKL.requestFocusLater();
      return false;
    }
    if (!Aus.checkDateRange(jraDatumOd, jraDatumDo))
      return false;
    return true;
  }

  public void okPress() {

    lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL", rpcskl.getCSKL()/*upit.getString("CSKL")*/);
    vrzal = dm.getSklad().getString("VRZAL");
    setQDnevnik(/*upit.getString("CSKL")*/rpcskl.getCSKL(),
                rut.getTimestampValue(upit.getTimestamp("DATDOKfrom"), rut.NUM_FIRST),
                rut.getTimestampValue(upit.getTimestamp("DATDOKto"), rut.NUM_LAST));

    if (vl.RezSet.rowCount() == 0) setNoDataAndReturnImmediately();
//    if (vl.RezSet.rowCount() == 0) {
//      rpcskl.jrfCSKL.requestFocus();
//      JOptionPane.showMessageDialog(null,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.WARNING_MESSAGE);
//      return;
//    }
//
//    rcc.EnabDisabAll(jpUpit,false);
//
//
//      totalUlaz = new BigDecimal(0.);
//      totalIzlaz = new BigDecimal(0.);
//      for (vl.RezSet.first(); vl.RezSet.inBounds(); vl.RezSet.next()) {
//        totalUlaz = totalUlaz.add(vl.RezSet.getBigDecimal("IZAD"));
//        totalIzlaz = totalIzlaz.add(vl.RezSet.getBigDecimal("IRAZ"));
//      }
//      this.getJPTV().getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//      this.getJPTV().setDataSet(vl.RezSet);
//      this.getJPTV().setKumTak(true);
//      this.getJPTV().setStoZbrojiti(new String[] {"IZAD", "IRAZ", "SAL"});
//      this.getJPTV().init_kum();

    totalUlaz = new BigDecimal(0.);
    totalIzlaz = new BigDecimal(0.);
    for (vl.RezSet.first(); vl.RezSet.inBounds(); vl.RezSet.next()) {
      totalUlaz = totalUlaz.add(vl.RezSet.getBigDecimal("IZAD"));
      totalIzlaz = totalIzlaz.add(vl.RezSet.getBigDecimal("IRAZ"));
    }
    this.getJPTV().getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    this.getJPTV().setDataSetAndSums(vl.RezSet, new String[] {"IZAD", "IRAZ", "SAL"});
  }

  public void interrupted() {
    rcc.setLabelLaF(jraDatumOd, true);
    rcc.setLabelLaF(jraDatumDo, true);
    jraDatumOd.requestFocusLater();
  }

  public void firstESC() {
    if (this.getJPTV().getDataSet() != null)  {
      this.getJPTV().clearDataSet();
      interrupted();
    } else {
      rcc.EnabDisabAll(rpcskl, true);
      rpcskl.Clear();
      rpcskl.jrfCSKL.requestFocusLater();
    }
  }

  public boolean runFirstESC() {
    return !rpcskl.getCSKL().equals("");
//    if (rpcskl.getCSKL().equals("")) return false;
//    else return true;
  }

  private void showDefaults() {
    rcc.setLabelLaF(jraDatumDo, true);
    rcc.setLabelLaF(jraDatumOd, true);
    upit.setTimestamp("DATDOKfrom", rut.findFirstDayOfYear(Integer.valueOf(vl.findYear()).intValue()));
    upit.setTimestamp("DATDOKto", vl.findDate(false,0));
    this.getJPTV().clearDataSet();
    jraDatumOd.requestFocusLater();
  }

  private String getKI() {
    return " and doki.vrdok not in ('PON','NDO','NKU','RNL','TRE','ZAH')";
  }
  private String getZCPor(String st) {
    if (vrzal.equals("V"))
      return st + ".vc - " + st + ".svc as zc, ";
    else if (vrzal.equals("M"))
      return st + ".mc - " + st + ".smc as zc, ";
    else return "0. as zc, ";
  }
  private void setQDnevnik(String skl, String datfrom, String datto) {
    String SQLdnevnik =
  // ulaz primke
  "SELECT doku.cskl,doku.datdok,doku.vrdok,doku.brdok,stdoku.cart,stdoku.cart1,stdoku.bc,stdoku.nazart,"+
      "stdoku.kol as kolul,0. as koliz,stdoku.zc,stdoku.izad,0. as iraz,stdoku.izad as sal FROM doku,stdoku "+
      "WHERE "+rut.getDoc("doku", "stdoku")+" and doku.vrdok != 'POR' "+
      "and doku.cskl = '"+skl+"' and doku.datdok >= "+datfrom+" and doku.datdok <= "+datto+
    " UNION "+
  // poravnanje kod primke
  "SELECT doku.cskl,doku.datdok,'POR' as vrdok,doku.brdok,stdoku.cart,stdoku.cart1,stdoku.bc,stdoku.nazart,"+
      "0. as kolul,0. as koliz,"+getZCPor("stdoku")+"stdoku.porav as izad,0. as iraz,stdoku.porav as sal FROM doku,stdoku "+
      "WHERE "+rut.getDoc("doku", "stdoku")+" and doku.vrdok != 'POR' and stdoku.porav != 0 "+
      "and doku.cskl = '"+skl+"' and doku.datdok >= "+datfrom+" and doku.datdok <= "+datto+
    " UNION "+

  // poravnanje
  "SELECT doku.cskl,doku.datdok,doku.vrdok,doku.brdok,stdoku.cart,stdoku.cart1,stdoku.bc,stdoku.nazart,"+
      "0. as kolul,0. as koliz,"+getZCPor("stdoku")+"stdoku.porav as izad,0. as iraz,stdoku.porav as sal FROM doku,stdoku "+
      "WHERE "+rut.getDoc("doku", "stdoku")+" and doku.vrdok = 'POR' "+
      "and doku.cskl = '"+skl+"' and doku.datdok >= "+datfrom+" and doku.datdok <= "+datto+
    " UNION "+
  // ulaz me\u0111uskladišnice
  "SELECT meskla.csklul as cskl,meskla.datdok,meskla.vrdok,meskla.brdok,stmeskla.cart,stmeskla.cart1,stmeskla.bc,stmeskla.nazart,"+
      "stmeskla.kol as kolul,0. as koliz,stmeskla.zcul as zc,stmeskla.zadrazul as izad,0. as iraz,stmeskla.zadrazul as sal "+
      "FROM meskla,stmeskla WHERE meskla.csklul = stmeskla.csklul and meskla.cskliz = stmeskla.cskliz "+
      "and meskla.vrdok = stmeskla.vrdok and meskla.brdok = stmeskla.brdok and meskla.god = stmeskla.god "+
      "and meskla.vrdok in ('MES', 'MEU') "+
      "and meskla.csklul = '"+skl+"' and meskla.datdok >= "+datfrom+" and meskla.datdok <= "+datto+
    " UNION "+
  // poravnanje ulaza me\u0111uskladišnice
  "SELECT meskla.csklul as cskl,meskla.datdok,'POR' as vrdok,meskla.brdok,stmeskla.cart,stmeskla.cart1,stmeskla.bc,stmeskla.nazart,"+
      "0. as kolul,0. as koliz,"+getZCPor("stmeskla")+"stmeskla.porav as izad,0. as iraz,stmeskla.porav as sal FROM meskla,stmeskla "+
      "WHERE meskla.csklul = stmeskla.csklul and meskla.cskliz = stmeskla.cskliz "+
      "and meskla.vrdok = stmeskla.vrdok and meskla.brdok = stmeskla.brdok and meskla.god = stmeskla.god "+
      "and meskla.vrdok in ('MES', 'MEU') and stmeskla.porav != 0 "+
      "and meskla.csklul = '"+skl+"' and meskla.datdok >= "+datfrom+" and meskla.datdok <= "+datto+
    " UNION "+
  // izlaz me\u0111uskladišnice
  "SELECT meskla.cskliz as cskl,meskla.datdok,meskla.vrdok,meskla.brdok,stmeskla.cart,stmeskla.cart1,stmeskla.bc,stmeskla.nazart,"+
      "0. as kolul,stmeskla.kol as koliz,stmeskla.zc,0. as izad,stmeskla.zadraziz as iraz,-stmeskla.zadraziz as sal "+
      "FROM meskla,stmeskla WHERE meskla.csklul = stmeskla.csklul and meskla.cskliz = stmeskla.cskliz "+
      "and meskla.vrdok = stmeskla.vrdok and meskla.brdok = stmeskla.brdok and meskla.god = stmeskla.god "+
      "and meskla.vrdok in ('MES','MEI') "+
      "and meskla.cskliz = '"+skl+"' and meskla.datdok >= "+datfrom+" and meskla.datdok <= "+datto+
    " UNION "+
  // ostali izlazi
  "SELECT doki.cskl,doki.datdok,doki.vrdok,doki.brdok,stdoki.cart,stdoki.cart1,stdoki.bc,stdoki.nazart,"+
      "0. as kolul,stdoki.kol as koliz,stdoki.zc,0. as izad,stdoki.iraz,-stdoki.iraz as sal FROM doki,stdoki "+
      "WHERE "+rut.getDoc("doki", "stdoki")+" and doki.cskl = '"+skl+"'"+getKI()+
      " and doki.datdok >= "+datfrom+" and doki.datdok <= "+datto+
      " ORDER BY 2, 3, 4";

    vl.execSQL(SQLdnevnik);
    Column kolul = (Column) dm.getStdoku().getColumn("KOL").clone();
    kolul.setColumnName("KOLUL");
    kolul.setCaption("Ulazna koli\u010Dina");
    Column koliz = (Column) dm.getStdoku().getColumn("KOL").clone();
    koliz.setColumnName("KOLIZ");
    koliz.setCaption("Izlazna koli\u010Dina");
    Column saldo = (Column) dm.getStdoki().getColumn("IRAZ").clone();
    saldo.setColumnName("SAL");
    saldo.setCaption("Saldo");
    vl.RezSet.setColumns(new Column[] {
      (Column) dm.getDoku().getColumn("CSKL").clone(),
      (Column) dm.getDoku().getColumn("DATDOK").clone(),
      (Column) dm.getDoku().getColumn("VRDOK").clone(),
      (Column) dm.getDoku().getColumn("BRDOK").clone(),
      (Column) dm.getStdoku().getColumn("CART").clone(),
      (Column) dm.getStdoku().getColumn("CART1").clone(),
      (Column) dm.getStdoku().getColumn("BC").clone(),
      (Column) dm.getStdoku().getColumn("NAZART").clone(),
      kolul, koliz,
      (Column) dm.getStdoku().getColumn("ZC").clone(),
      (Column) dm.getStdoku().getColumn("IZAD").clone(),
      (Column) dm.getStdoki().getColumn("IRAZ").clone(),
      saldo
    });
    vl.RezSet.getColumn("CSKL").setVisible(0);
    vl.RezSet.getColumn("CART").setVisible(0);
    vl.RezSet.getColumn("CART1").setVisible(0);
    vl.RezSet.getColumn("BC").setVisible(0);
    vl.RezSet.getColumn("SAL").setVisible(0);
    vl.RezSet.getColumn(Aut.getAut().getCARTdependable("CART", "CART1", "BC")).setVisible(1);
//    System.err.println("opening...");
    openScratchDataSet(vl.RezSet);
//    System.err.println("finished opening...");
  }

  public String getNAZSKL() {
    return upit.getString("NAZSKL");
  }

  private void jbInit() throws Exception {
    dm = dM.getDataModule();
    this.addReport("hr.restart.robno.repDnevnik", "hr.restart.robno.repDnevnik", "Dnevnik", "Ispis dnevnika");

    CSKL = (Column) dm.getSklad().getColumn("CSKL").clone();
    NAZSKL = (Column) dm.getSklad().getColumn("NAZSKL").clone();
    DATDOKfrom = dm.createTimestampColumn("DATDOKfrom");
    DATDOKto = dm.createTimestampColumn("DATDOKto");
    upit.setColumns(new Column[] {CSKL, NAZSKL, DATDOKfrom, DATDOKto});
    rpcskl.setDisabAfter(true);

//    rpcskl.setNextFocusableComponent(jraDatumOd);

    jpUpit.setLayout(xYLayout1);

    jraDatumOd.setDataSet(upit);
    jraDatumOd.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumOd.setColumnName("DATDOKfrom");
    jraDatumDo.setDataSet(upit);
    jraDatumDo.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumDo.setColumnName("DATDOKto");
    jlDatum.setText("Datum (od - do)");

    xYLayout1.setHeight(87);
    xYLayout1.setWidth(655);
    jpUpit.add(jlDatum, new XYConstraints(15, 45, -1, -1));

    jpUpit.add(rpcskl,   new XYConstraints(0, 0, 655, -1));

    jpUpit.add(jraDatumOd, new XYConstraints(150, 50, 100, -1));
    jpUpit.add(jraDatumDo, new XYConstraints(255, 50, 100, -1));

    new raDateRange(jraDatumOd, jraDatumDo);
    this.setJPan(jpUpit);
  }
}