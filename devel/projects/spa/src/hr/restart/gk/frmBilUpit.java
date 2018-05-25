/****license*****************************************************************
**   file: frmBilUpit.java
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
package hr.restart.gk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Condition;
import hr.restart.baza.Gkrep;
import hr.restart.baza.Gkstavke;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCorg;
import hr.restart.swing.raDateRange;
import hr.restart.swing.raTableBoldModifier;
import hr.restart.util.*;
import hr.restart.zapod.OrgStr;


public class frmBilUpit extends raUpitFat {
  
  JPanel content = new JPanel();
  jpCorg jpc = new jpCorg(350, true);
  raComboBox rcb = new raComboBox();
  JraTextField jraOd = new JraTextField();
  JraTextField jraDo = new JraTextField();
  
  protected raNavAction rnvExport = new raNavAction("Izvoz u XSL", raImages.IMGMOVIE, java.awt.event.KeyEvent.VK_F7){
    public void actionPerformed(java.awt.event.ActionEvent e){
      export();
    }
  };
  
  StorageDataSet tds;
  
  JFileChooser jf = new JFileChooser();
  FileFilter filter = new raFileFilter("Excel datoteke (*.xls)");
  FileFilter filter2 = new raFileFilter("Excel XML datoteke (*.xlsx)");

  public frmBilUpit() {
    try {
      jbInit();
    } catch (Exception e) {
    // TODO: handle exception
    }
  }

  
  private void jbInit() throws Exception {
    tds = Aus.createSet("Gkstavke.CORG CPOZ:6 @DATUMOD @DATUMDO");
    rcb.setRaDataSet(tds);
    rcb.setRaColumn("CPOZ");
    QueryDataSet ds = Gkrep.getDataModule().openTempSet("CPOZ=CPRIP");
    ds.setSort(new SortDescriptor(new String[] {"CPOZ"}));
    rcb.setRaItems(ds, "CPOZ", "NAZIV");
    jpc.bind(tds);
    jraOd.setColumnName("DATUMOD");
    jraOd.setDataSet(tds);
    jraDo.setColumnName("DATUMDO");
    jraDo.setDataSet(tds);
    
    content.setLayout(new XYLayout(730, 120));
    
    content.add(new JLabel("Izvještaj"), new XYConstraints(15, 20, -1, -1));
    content.add(rcb, new XYConstraints(150, 20, 455, 21));
    content.add(jpc, new XYConstraints(0, 50, -1, -1));
    content.add(new JLabel("Datum (od - do)"), new XYConstraints(15, 75, -1, -1));
    content.add(jraOd, new XYConstraints(150, 75, 100, -1));
    content.add(jraDo, new XYConstraints(255, 75, 100, -1));
    new raDateRange(jraOd, jraDo);
    setJPan(content);
    
    jf.addChoosableFileFilter(filter);
    jf.addChoosableFileFilter(filter2);
    jf.setFileFilter(filter2);
    jf.setCurrentDirectory(new File("."));
    
    getJPTV().addTableModifier(new raTableBoldModifier("NAZIV", "BOLD", "D"));
    getJPTV().addTableModifier(new raTableBoldModifier("AOP", "BOLD", "D"));
    getJPTV().addTableModifier(new raTableBoldModifier("IZNOS", "BOLD", "D"));
  }

  public String navDoubleClickActionName() {
    return "Detalji";
  }

  public int[] navVisibleColumns() {
    // TODO Auto-generated method stub
    return new int[] {0, 1, 2};
  }

  public void okPress() {
    
    DataSet reps = Gkrep.getDataModule().openTempSet();
    HashDataSet hash = new HashDataSet(reps, "CPOZ");
    
    DataTree root = DataTree.getSubTree(tds.getString("CPOZ"), reps, "CPOZ", "CPRIP");
    root.sortDeep();
    if (root.isLeaf()) setNoDataAndReturnImmediately("Pogrešno definiran izvještaj!");
    
    StorageDataSet out = Aus.createSet("CPOZ:6 {Pozicija}NAZIV:500 AOP:6 {Iznos}IZNOS.2 SOLVED:1 BOLD:1 XLSPOZ:20");
    out.getColumn("CPOZ").setVisible(0);
    out.getColumn("SOLVED").setVisible(0);
    out.getColumn("BOLD").setVisible(0);
    out.getColumn("XLSPOZ").setVisible(0);
    fillTree(out, root, hash);
    
    StorageDataSet all = Aus.q("SELECT brojkonta, SUM(id) AS id, SUM(ip) as ip FROM gkstavke WHERE " +
          jpc.getCondition().and(Condition.between("DATUMKNJ", tds, "DATUMOD", "DATUMDO")) +
          " GROUP BY brojkonta");
    raProcess.openScratchDataSet(all);
    all.enableDataSetEvents(false);
    
    StorageDataSet nops = Aus.q("SELECT brojkonta, SUM(id) AS id, SUM(ip) as ip FROM gkstavke WHERE " +
        jpc.getCondition().and(Condition.between("DATUMKNJ", tds, "DATUMOD", "DATUMDO")).
        and(Condition.diff("CVRNAL", "00")) + " GROUP BY brojkonta");
    raProcess.openScratchDataSet(nops);
    nops.enableDataSetEvents(false);
    
    Calc calc = new Calc(true);
    HashMap sums = new HashMap();
    
    for (out.first(); out.inBounds(); out.next()) {
      DataTree dt = find(root, out.getString("CPOZ"));
      if (dt != null && hash.loc(dt.key)) {
        if (!dt.isLeaf()) fillSum(out, dt, hash, all, nops);
        else if (hash.get().getString("KALK").length() == 0) {
          out.setBigDecimal("IZNOS", Aus.zero2);
          out.setString("SOLVED", "D");
          out.post();
        }
        if (out.getString("SOLVED").equals("D")) {
          sums.put(out.getString("CPOZ"), out.getBigDecimal("IZNOS"));
          if (out.getString("AOP").length() > 0)
            calc.set(out.getString("AOP"), out.getBigDecimal("IZNOS"));
        }
      } 
    }
    
    boolean solved = false;
    int iters = 0;
    while (!solved) {
      solved = true;
      for (out.first(); out.inBounds(); out.next())
        if (!out.getString("SOLVED").equals("D")) {
          DataTree dt = find(root, out.getString("CPOZ"));    
          if (dt != null && hash.loc(dt.key))
            solved = calcSum(out, dt, hash, calc, sums) && solved;
        }
      if (iters++ > 20) break;
    }

    setDataSet(out);
    setTitle(rcb.getSelectedItem() + "  u razdoblju od " + 
          Aus.formatTimestamp(tds.getTimestamp("DATUMOD")) + " do " + Aus.formatTimestamp(tds.getTimestamp("DATUMDO")));
  }
  
  private DataTree find(DataTree dt, String key) {
    if (dt.key.equals(key)) return dt;
    for (Iterator i = dt.branches.iterator(); i.hasNext(); ) {
      DataTree st = (DataTree) i.next();
      if (st.key.equals(key)) return st;
      if (!st.isLeaf()) {
        st = find(st, key);
        if (st != null) return st;
      }
    }
    return null;
  }

  private void fillTree(StorageDataSet out, DataTree dt, HashDataSet ds) {
    for (Iterator i = dt.branches.iterator(); i.hasNext(); ) {
      DataTree st = (DataTree) i.next();
      if (ds.loc(st.key)) {
        if (ds.get().getString("RASPON").length() > 0) break;
        
        
        out.insertRow(false);
        out.setString("CPOZ", ds.get().getString("CPOZ"));
        out.setString("NAZIV", Aus.spc(ds.get().getInt("NIVO") * 4 - 4) + ds.get().getString("NAZIV"));
        out.setString("AOP", ds.get().getString("AOP"));
        out.setString("BOLD", ds.get().getString("BOLD"));
        out.setString("XLSPOZ", ds.get().getString("XLSPOZ"));
        out.post();
        System.out.println( out.getString("NAZIV"));
        if (!st.isLeaf()) fillTree(out, st, ds);
      }
    }
  }
  
  private boolean calcSum(StorageDataSet out, DataTree dt, HashDataSet ds, Calc calc, HashMap sums) {
    System.out.println(out.getString("NAZIV"));
    System.out.println("calc " + ds.get());
    if (ds.get().getString("KALK").length() == 0)
      return calcSimpleSum(out, dt, ds, calc, sums);
    
    try {
      BigDecimal val = calc.evaluate(ds.get().getString("KALK"));
      if (val.signum() < 0 && ds.get().getString("NEG").equalsIgnoreCase("N")) 
        val = Aus.zero2;
      out.setBigDecimal("IZNOS", val);
      out.setString("SOLVED", "D");
      sums.put(out.getString("CPOZ"), val);
      if (out.getString("AOP").length() > 0)
        calc.set(out.getString("AOP"), val);
      return true;
    } catch (Exception e) {
      System.out.println("error: " + e.getMessage());
    }
    return false;
  }
  
  private boolean calcSimpleSum(StorageDataSet out, DataTree dt, HashDataSet ds, Calc calc, HashMap sums) {
    BigDecimal sum = Aus.zero2;
    for (Iterator i = dt.branches.iterator(); i.hasNext(); ) {
      DataTree st = (DataTree) i.next();
      if (!sums.containsKey(st.key)) return false;
      sum = sum.add((BigDecimal) sums.get(st.key));
    }
    if (sum.signum() < 0 && ds.get().getString("NEG").equalsIgnoreCase("N")) 
      sum = Aus.zero2;
    out.setBigDecimal("IZNOS", sum);
    out.setString("SOLVED", "D");
    sums.put(out.getString("CPOZ"), sum);
    if (out.getString("AOP").length() > 0)
      calc.set(out.getString("AOP"), sum);
    return true;
  }
  
  private void fillSum(StorageDataSet out, DataTree dt, HashDataSet ds, StorageDataSet all, StorageDataSet nops) {
    BigDecimal sum = Aus.zero2;
    for (Iterator i = dt.branches.iterator(); i.hasNext(); ) {
      DataTree st = (DataTree) i.next();
      if (ds.loc(st.key)) {
        String raspon = ds.get().getString("RASPON");
        if (raspon.length() == 0) return;
        sum = sum.add(getValue(raspon, ds.get().getString("KALK"), 
            ds.get().getString("PS").equals("D") ? all : nops));
      }
    }
    out.setBigDecimal("IZNOS", sum);
    out.setString("SOLVED", "D");
    out.post();
  }
  
  private BigDecimal getValue(String raspon, String calc, StorageDataSet all) {
    HashSet single = new HashSet();
    ArrayList from = new ArrayList();
    ArrayList to = new ArrayList();
    String[] parts = new VarStr(raspon).splitTrimmed(',');
    for (int i = 0; i < parts.length; i++) {
      String[] range = new VarStr(parts[i]).splitTrimmed('-');
      if (range.length > 1) {
        from.add(range[0]);
        to.add(range[1]);
      } else if (range[0].length() <= 3) {
        from.add(range[0]);
        to.add(range[0]);
      } else single.add(parts[i]); 
    }

    BigDecimal sum = Aus.zero2;
    for (all.first(); all.inBounds(); all.next()) {
      String konto = all.getString("BROJKONTA");
      if (single.contains(konto))
        sum = sum.add(getCalc(calc, all));
      else for (int i = 0; i < from.size(); i++) 
        if (konto.compareTo((String) from.get(i)) >= 0 &&
            within(konto, (String) to.get(i))) {
          sum = sum.add(getCalc(calc, all));
          break;
        }
    }
    return sum;
  }
  
  private boolean within(String konto, String endk) {
    if (konto.length() < endk.length()) 
      return konto.compareTo(endk) <= 0;
    return konto.substring(0, endk.length()).compareTo(endk) <= 0;
  }
  
  private BigDecimal getCalc(String calc, StorageDataSet all) {
    if (calc.equals("ID") || calc.equals("IP")) 
      return all.getBigDecimal(calc);
    if (calc.equals("ID+IP")) 
      return all.getBigDecimal("ID").add(all.getBigDecimal("IP"));
    if (calc.equals("ID-IP"))
      return all.getBigDecimal("ID").subtract(all.getBigDecimal("IP"));
    if (calc.equals("-ID"))
      return all.getBigDecimal("ID").negate();
    if (calc.equals("-IP"))
      return all.getBigDecimal("IP").negate();
    if (calc.equals("-ID+IP"))
      return all.getBigDecimal("ID").negate().add(all.getBigDecimal("IP"));
    if (calc.equals("-ID-IP"))
      return all.getBigDecimal("ID").negate().subtract(all.getBigDecimal("IP"));
    return Aus.zero0;
  }
  
  public boolean runFirstESC() {
    return (jpc.getCorg().length() > 0);
  }

  public void firstESC() {
    if (getJPTV().getStorageDataSet() != null) {
      setDataSet(null);
      raCommonClass.getraCommonClass().EnabDisabAll(content,true);
      removeNav();
      setTitle("Definirani izvještaji");
      jraOd.requestFocusLater();
    } else {
      jpc.setCorg("");
      jpc.corg.requestFocusLater();
    }
  }

  public void componentShow() {
    if (tds.getString("CORG").length() == 0) {
      jpc.setCorg(OrgStr.getKNJCORG(false));
    }
    if (tds.isNull("DATUMOD")) {
      tds.setTimestamp("DATUMOD", Aus.getGkYear(Valid.getValid().getToday()));
      tds.setTimestamp("DATUMDO", Valid.getValid().getToday());
    }
    rcb.this_itemStateChanged();
    jraOd.requestFocusLater();
  }  
  
  void export() {
    if (getJPTV().getStorageDataSet() == null) return;
    if (jf.showOpenDialog(this.getWindow()) != jf.APPROVE_OPTION) return;
    
    File sel = jf.getSelectedFile();
    if (!sel.exists()) {
      JOptionPane.showMessageDialog(this.getWindow(), 
          "Datoteka ne postoji!",
          "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (!sel.canRead()) {
      JOptionPane.showMessageDialog(this.getWindow(), 
          "Datoteka se ne može otvoriti!",
          "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    Workbook wb = null;
    try {
      wb = WorkbookFactory.create(sel);
      if (fillData(sel, wb)) {
        JOptionPane.showMessageDialog(this.getWindow(), 
            "Izvještaj završen.", "Excel izvještaj", 
            JOptionPane.INFORMATION_MESSAGE);          
      }
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this.getWindow(), 
          "Greška kod otvaranja datoteke!",
          "Greška", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this.getWindow(), 
          e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
    }
    if (wb != null)
      try {
        wb.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
  }
  
  boolean fillData(final File orig, final Workbook wb) throws Exception {
    raProcess.runChild(this.getWindow(), new Runnable() {
      public void run() {
        fillDataProc(orig, wb);
      }
    });
    if (raProcess.isFailed())
      throw raProcess.getLastException();
    return raProcess.isCompleted();
  }
    
  void fillDataProc(File orig, Workbook wb) {
    raProcess.checkClosing();
    
    DataFormat df = wb.createDataFormat();
    
    DataSet ds = getJPTV().getStorageDataSet();
    for (ds.first(); ds.inBounds(); ds.next()) {
      String def = ds.getString("XLSPOZ").trim();
      if (def.length() == 0) continue;
      
      int sheet = 0;
      
      int colon = def.indexOf(':');
      if (colon > 0) {
        sheet = Aus.getNumber(def.substring(0, colon)) - 1;
        def = def.substring(colon + 1);
      }
      
      Sheet sh = wb.getSheetAt(sheet);
      if (sh == null) throw new RuntimeException("Greška u plahti za poziciju " + ds.getString("NAZIV") + "!");
      
      System.out.println(def);

      boolean alpha = true;
      int rbeg = 0;
      for (int i = 0; i < def.length(); i++) {
        char c = def.charAt(i);
        if ((alpha && (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z')) ||
            (i > 0 && c >= '0' && c <= '9')) {
          if (c >= '0' && c <= '9' && alpha) {
            alpha = false;
            rbeg = i; 
          }
        } else throw new RuntimeException("Greška u poziciji " + ds.getString("NAZIV") + "!");
      }
      if (alpha) throw new RuntimeException("Greška u poziciji " + ds.getString("NAZIV") + "!");
      
      int x = 0;
      for (int i = 0; i < rbeg; i++)
        x = x * 26 + Character.toUpperCase(def.charAt(i)) - 'A' + 1;
      int y = Integer.parseInt(def.substring(rbeg));

      Row hr = sh.getRow(y - 1);
      if (hr == null) 
        throw new RuntimeException("Nepostojeæi redak " + y + " u poziciji " + ds.getString("NAZIV") + "!");
      Cell cell = hr.getCell(x - 1);
      if (cell == null) 
        throw new RuntimeException("Nepostojeæa æelija " + x + ", " + y + " u poziciji " + ds.getString("NAZIV") + ")!");
      cell.setCellValue(ds.getBigDecimal("IZNOS").doubleValue());
      //cell.getCellStyle().setDataFormat(df.getFormat("#,##0.00"));
      
    }
    
    String oname = orig.getAbsolutePath();
    if (oname.endsWith(".xls"))
      oname = oname.substring(0, oname.length() - 4) + "-RA.xls";
    else oname = oname.substring(0, oname.length() - 5) + "-RA.xlsx";
    File ofile = new File(oname);
    
    FileOutputStream out = null;
    
    try {
      out = new FileOutputStream(ofile);
      wb.write(out);
      out.close();
      wb.close();
      orig.delete();
      ofile.renameTo(orig);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (out != null) 
        try {
          out.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
    }
  }
  
  
  protected void addNavBarOptions() {
    super.addNavBarOptions();
    getJPTV().getNavBar().removeOption(rnvDoubleClick);
    getJPTV().getNavBar().addOption(rnvExport, 0);
  }
  
  protected void navbarremoval() {
    super.navbarremoval();
    if (getJPTV().getNavBar().contains(rnvExport))
      this.getJPTV().getNavBar().removeOption(rnvExport);
  }
}
