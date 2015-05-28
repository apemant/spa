/****license*****************************************************************
**   file: upReverStanje.java
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
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Aus;
import hr.restart.util.HashDataSet;
import hr.restart.util.HashSum;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raProcess;
import hr.restart.util.raUpit;
import hr.restart.util.raUpitFat;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class upReverStanje extends raUpitFat {
  _Main ma;
  dM dm;
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

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
  private static upReverStanje upd;

  public upReverStanje() {
    try {
      upd = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public static upReverStanje getInstance() {
    return upd;
  }
  public com.borland.dx.dataset.DataSet getUpit() {
    return upit;
  }
  public void componentShow() {
    showDefaults();
    rpcskl.setDefaultCSKL();    
  }

  String vrzal;
  public boolean Validacija() {
    if (!Aus.checkDateRange(jraDatumOd, jraDatumDo))
      return false;
    return true;
  }

  public void okPress() {
    
    StorageDataSet out = Aus.createSet("Doki.CPAR .CRADNIK Stdoki.CART .CART1 .BC .NAZART .JM " +
    		"{Prvi izlaz}@DATIZ {Zadnji povrat}@DATUL {Izlaz}KOLIZ.3 {Povrat}KOLUL.3 {Ostatak}KOL.3");
    
    
    
    StorageDataSet main = ut.getNewQueryDataSet("SELECT doki.datdok, doki.vrdok, doki.cpar, doki.cradnik, stdoki.cart, stdoki.kol" +
    		" FROM doki, stdoki WHERE " + Aus.join("doki", "stdoki", Util.mkey) +
    		" AND " + rpcskl.getCondition().and(Condition.between("DATDOK", upit, "DATDOKfrom", "DATDOKto")).
    		            and(Condition.in("VRDOK", "REV PRV")).qualified("doki"), false);
    
    raProcess.openScratchDataSet(main);
    
    System.out.println("Total rows " + main.getRowCount());
    
    if (main.rowCount() == 0) setNoDataAndReturnImmediately();
    
    KeyHashSum ulaz = new KeyHashSum(main);
    KeyHashSum izlaz = new KeyHashSum(main);
    KeyHashSum diff = new KeyHashSum(main);
    HashMap mind = new HashMap();
    HashMap maxd = new HashMap();
    
    for (main.first(); main.inBounds(); main.next()) {
      String k = key(main);
      Timestamp dat = main.getTimestamp("DATDOK");
      diff.add();
      if (main.getString("VRDOK").equals("REV")) {
        izlaz.add();
        Timestamp old = (Timestamp) mind.get(k);
        if (old == null || dat.before(old)) mind.put(k, dat);
      } else {
        ulaz.add();
        Timestamp old = (Timestamp) maxd.get(k);
        if (old == null || dat.after(old)) maxd.put(k, dat);
      }
    }
    raProcess.checkClosing();
    
    HashDataSet arts = new HashDataSet(dm.getArtikli(), "CART");
    
    for (Iterator i = diff.iterator(); i.hasNext(); ) {
      String k = (String) i.next();
      
      out.insertRow(false);
      int rp = k.indexOf('|');
      int cp = k.indexOf(':');
      if (rp > 0) {
        out.setString("CRADNIK", k.substring(0, rp));
        out.setInt("CART", Aus.getNumber(k.substring(rp + 1)));
      } else {
        out.setInt("CPAR", Aus.getNumber(k.substring(0, cp)));
        out.setInt("CART", Aus.getNumber(k.substring(cp + 1)));
      }
      if (arts.loc(out)) Aut.getAut().copyArtFields(out, arts.get());
      BigDecimal iz = izlaz.get(k);
      BigDecimal ul = ulaz.get(k);
      Timestamp min = (Timestamp) mind.get(k);
      Timestamp max = (Timestamp) maxd.get(k);
      if (iz != null) out.setBigDecimal("KOLIZ", iz);
      if (ul != null) out.setBigDecimal("KOLUL", ul.negate());
      Aus.sub(out, "KOL", "KOLIZ", "KOLUL");
      if (min != null) out.setTimestamp("DATIZ", min);
      if (max != null) out.setTimestamp("DATUL", max);
    }
    
    raProcess.checkClosing();
    System.out.println("Out rows " + out.getRowCount());
    
    if (rpcskl.getCSKL().length() > 0)
      this.setTitle("PREGLED REVERSA ZA SKLADIŠTE " + rpcskl.getCSKL() +
      		"  u periodu od " + Aus.formatTimestamp(upit.getTimestamp("DATDOKfrom")) 
            + " do " + Aus.formatTimestamp(upit.getTimestamp("DATDOKfrom")));
    else this.setTitle("PREGLED REVERSA" +
        "  u periodu od " + Aus.formatTimestamp(upit.getTimestamp("DATDOKfrom")) 
        + " do " + Aus.formatTimestamp(upit.getTimestamp("DATDOKfrom")));

    this.getJPTV().getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    this.getJPTV().addTableModifier(new raTableColumnModifier("CPAR", new String[] {"CPAR", "NAZPAR"}, dm.getPartneri()));
    this.getJPTV().setDataSet(out);
  }

  public void interrupted() {
    rcc.setLabelLaF(jraDatumOd, true);
    rcc.setLabelLaF(jraDatumDo, true);
    jraDatumOd.requestFocusLater();
  }

  public void firstESC() {
    if (this.getJPTV().getStorageDataSet() != null)  {
      this.getJPTV().clearDataSet();
      interrupted();
    } else {
      rcc.EnabDisabAll(rpcskl, true);
      rpcskl.Clear();
      rpcskl.jrfCSKL.requestFocusLater();
    }
  }
  
  public String navDoubleClickActionName() {
    return "Dok";
  }
  
  public int[] navVisibleColumns() {
    return new int[] {0,1,Aut.getAut().getCARTdependable(2, 3, 4),5,6,7,8,9,10,11};
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

  public String getNAZSKL() {
    return upit.getString("NAZSKL");
  }

  private void jbInit() throws Exception {
    dm = dM.getDataModule();

    CSKL = (Column) dm.getSklad().getColumn("CSKL").clone();
    NAZSKL = (Column) dm.getSklad().getColumn("NAZSKL").clone();
    DATDOKfrom = dm.createTimestampColumn("DATDOKfrom");
    DATDOKto = dm.createTimestampColumn("DATDOKto");
    upit.setColumns(new Column[] {CSKL, NAZSKL, DATDOKfrom, DATDOKto});
    rpcskl.setDisabAfter(true);

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
  
  String key(ReadRow row) {
    if (row.getString("CRADNIK").trim().length() != 0)
      return row.getString("CRADNIK") + "|" + row.getInt("CART");
    return row.getInt("CPAR") + ":" + row.getInt("CART");
  }
  
  class KeyHashSum extends HashSum {
    public KeyHashSum(StorageDataSet ds) {
      super.set(ds, "KOL");
    }
    protected Object getKey(ReadRow row) {
      return key(row);
    }
  }
}