/****license*****************************************************************
**   file: frmReportxList.java
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
package hr.restart.sisfun;

import hr.restart.baza.Condition;
import hr.restart.baza.Gkstavke;
import hr.restart.baza.Repxdata;
import hr.restart.baza.Repxhead;
import hr.restart.baza.Skripte;
import hr.restart.baza.dM;
import hr.restart.baza.raDataSet;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCorg;
import hr.restart.swing.raDateRange;
import hr.restart.swing.raOptionDialog;
import hr.restart.util.*;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
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

import bsh.EvalError;
import bsh.Interpreter;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class frmReportxList extends raFrame {

  lookupData ld = lookupData.getlookupData();
  
  Interpreter bsh;
  
  raJPTableView jp = new raJPTableView() {
    public void mpTable_doubleClicked() {
      OKPress();
    }
  };

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      frmReportxList.this.hide();
    }
  };

  private QueryDataSet reps = new raDataSet();
  String app;
  
  JFileChooser jf = new JFileChooser();
  FileFilter filter = new raFileFilter("Excel datoteke (*.xls)");
  FileFilter filter2 = new raFileFilter("Excel XML datoteke (*.xlsx)");
  
  StorageDataSet fld = new StorageDataSet();
  JPanel pan = new JPanel(new BorderLayout());
  jpCorg jpc = new jpCorg(100, 290, true);
  JraTextField jraRbr = new JraTextField();
  JraTextField jraDfrom = new JraTextField();
  JraTextField jraDto = new JraTextField();
  raOptionDialog dlg = new raOptionDialog() {
    protected boolean checkOk() {
      if (!jpc.Validacija()) return false;
      if (!Aus.checkDateRange(jraDfrom, jraDto)) return false;
      return true;
    }
    protected void beforeShow() {
      if (fld.getString("CORG").length() == 0) {
        jpc.setCorg(OrgStr.getKNJCORG(false));
        fld.setTimestamp("DATFROM", Aus.getGkYear(Valid.getValid().getToday()));
        fld.setTimestamp("DATTO", Valid.getValid().getToday());
        //fld.setInt("SHEET", 1);
      }
    }
  };

  public frmReportxList() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setApp(String app) {
    this.app = app;
  }

  void OKPress() {
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
    
    if (reps.getString("TIP").equals("G") || reps.getString("TIP").equals("B") || reps.getString("TIP").equals("S")) {
      if (!dlg.show(this.getWindow(), pan, reps.getString("NAZREP")))
        return;
    }
    
    /*try {
      FileInputStream fis = new FileInputStream(sel);*/
      try {
        Workbook wb = WorkbookFactory.create(sel);
        if (fillData(sel, wb)) {
          JOptionPane.showMessageDialog(this.getWindow(), 
              "Izvještaj završen.", "Excel izvještaj", 
              JOptionPane.INFORMATION_MESSAGE);          
        }
      } catch (IOException e) {
        JOptionPane.showMessageDialog(this.getWindow(), 
            "Greška kod otvaranja datoteke!",
            "Greška", JOptionPane.ERROR_MESSAGE);
        return;
      } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this.getWindow(), 
            e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        return;
      } /*finally {
        try {
          fis.close();
        } catch (IOException e) {
          //
        }
      }
    } catch (FileNotFoundException e) {
      JOptionPane.showMessageDialog(this.getWindow(), 
          "Datoteka ne postoji!",
          "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }    */
  }
  
  boolean fillData(final File orig, final Workbook wb) throws Exception {
    raProcess.runChild(this.getWindow(), new Runnable() {
      public void run() {
        try {
          fillDataProc(orig, wb);
        } catch (Exception e) {
          e.printStackTrace();
          raProcess.fail();          
        }
      }
    });
    if (raProcess.isFailed())
      throw raProcess.getLastException();
    return raProcess.isCompleted();
  }
    
  void fillDataProc(File orig, Workbook wb) {
    DataSet logo = dM.getDataModule().getLogotipovi();
    DataSet orgs = dM.getDataModule().getOrgstruktura();
    DataSet kont = dM.getDataModule().getKonta();
    String corg = jpc.getCorg();
    while (!ld.raLocate(logo, "CORG", corg)) {
      if (!ld.raLocate(orgs, "CORG", corg)) 
        throw new RuntimeException("Greška u organizacijskim jedinicama!");
      
      if (orgs.getString("PRIPADNOST").equals(corg))
        throw new RuntimeException("Nije definiran logotip za knjigovodstvo!");

      corg = orgs.getString("PRIPADNOST");
    }
    raProcess.checkClosing();
    
    DataSet rep = Repxdata.getDataModule().getTempSet(
        Condition.equal("CREP", reps));
    rep.open();
    
    if (reps.getString("TIP").equals("S")) fillCustom(wb, rep);
    else {
      
      StorageDataSet gk = Gkstavke.getDataModule().getScopedSet("BROJKONTA DATUMKNJ ID IP");
      raProcess.fillScratchDataSet(gk, "SELECT brojkonta,datumknj,id,ip FROM gkstavke WHERE "+
          jpc.getCondition().and(Condition.between("DATUMKNJ", fld, "DATFROM", "DATTO")));
      StorageDataSet ogk = Gkstavke.getDataModule().getScopedSet("BROJKONTA DATUMKNJ ID IP");
      Timestamp old = Util.getUtil().addYears(fld.getTimestamp("DATFROM"), -1);
      raProcess.fillScratchDataSet(ogk, "SELECT brojkonta,datumknj,id,ip FROM gkstavke WHERE "+
          jpc.getCondition().and(Condition.between("DATUMKNJ", 
              Util.getUtil().getFirstDayOfYear(old),
              Util.getUtil().getLastDayOfYear(old))));
      gk.enableDataSetEvents(false);
      gk.setSort(new SortDescriptor(new String[] {"BROJKONTA"}));
      ogk.enableDataSetEvents(false);
      ogk.setSort(new SortDescriptor(new String[] {"BROJKONTA"}));
  
      DataFormat df = wb.createDataFormat();
      int sheet = fld.getInt("SHEET");
      if (sheet > 0) --sheet;
      
      raProcess.checkClosing();
      
      if (reps.getString("TIP").equals("G")) {
        Sheet sh = wb.getSheetAt(sheet);
        if (sh == null) throw new RuntimeException("Greška u plahti!");
        
        raProcess.checkClosing();
        for (rep.first(); rep.inBounds(); rep.next()) {
          Row hr = sh.getRow((short) (rep.getInt("RED") - 1));
          Cell cell = hr.getCell((short) (rep.getInt("KOL") - 1));
          if ("S".equals(rep.getString("TIP"))) {
            fillString(cell, logo, rep.getString("DATA"));
            cell.getCellStyle().setDataFormat(df.getFormat("text"));
          } else if ("2".equals(rep.getString("TIP"))) {
            fillNum(cell, gk, ogk, rep.getString("DATA"));
            cell.getCellStyle().setDataFormat(df.getFormat("#,##0.00"));
          } else if ("D".equals(rep.getString("TIP"))) {
            fillDate(cell, rep.getString("DATA"));
            cell.getCellStyle().setDataFormat(df.getFormat("dd.mm.yyyy"));
          }
          raProcess.checkClosing();
        }
      } else if (reps.getString("TIP").equals("B")) {
        Sheet sh = wb.getSheetAt(sheet);
        if (sh == null) throw new RuntimeException("Greška u plahti!");
        
        raProcess.checkClosing();
        String kontdef = "";
        for (rep.first(); rep.inBounds(); rep.next()) {
          if ("S".equals(rep.getString("TIP"))) {
            String def = rep.getString("DATA").toUpperCase().trim();
            if (def.startsWith("[[") && def.endsWith("]]"))
              kontdef = def.substring(2, def.length() - 2);
          }
        }
        raProcess.checkClosing();
        System.out.println("Definicija: " + kontdef);
        
        raGlob glob = new raGlob(new VarStr(kontdef).replace('X', '?').append('*').toString());
        String rule = new VarStr(kontdef).replace('X', '?').append("[]").toString();
  
        HashSet konta = new HashSet();      
        for (gk.first(); gk.inBounds(); gk.next()) {
          if (glob.matches(gk.getString("BROJKONTA")))
            konta.add(glob.morphLastMatch(rule));
        }
        ArrayList skonta = new ArrayList(konta);
        Collections.sort(skonta);
        System.out.println(skonta);
        
        for (rep.first(); rep.inBounds(); rep.next()) {
          Row hr = sh.getRow((short) (rep.getInt("RED") - 1));
          Cell cell = hr.getCell((short) (rep.getInt("KOL") - 1));
          if ("S".equals(rep.getString("TIP"))) {
            String def = rep.getString("DATA").toUpperCase().trim();
            if (def.startsWith("[[") && def.endsWith("]]")) {
              short row = (short) (rep.getInt("RED") - 1);
              for (Iterator i = skonta.iterator(); i.hasNext(); ) {
                String konto = (String) i.next();
                if (ld.raLocate(kont, "BROJKONTA",  konto)) {
                  cell.setCellValue(konto + " - " + kont.getString("NAZIVKONTA"));
                  
                  hr = sh.getRow(++row);
                  cell = hr.getCell((short) (rep.getInt("KOL") - 1));
                }
              }
            } else {
              fillString(cell, logo, rep.getString("DATA"));
              cell.getCellStyle().setDataFormat(df.getFormat("text"));
            }
          } else if ("2".equals(rep.getString("TIP"))) {
            String def = rep.getString("DATA");
            short row = (short) (rep.getInt("RED") - 1);
            for (Iterator i = skonta.iterator(); i.hasNext(); ) {
              String konto = (String) i.next();
              VarStr repl = new VarStr(def).trim();
              int beg, end;
              while ((beg = repl.indexOf('[')) >= 0 && (end = repl.indexOf(']')) > beg) {
                String tx = repl.mid(beg + 1, end);
                int mon = 0;
                if (tx.startsWith("M")) mon = Aus.getNumber(tx.substring(1));
                repl.replace(beg, end + 1, getMonthVal(gk, konto, mon).toString());
              }
              
              Double val = new MathEvaluator(repl.toString()).getValue();
              cell.setCellValue(val.doubleValue());
              cell.getCellStyle().setDataFormat(df.getFormat("#,##0.00"));
             
              hr = sh.getRow(++row);
              cell = hr.getCell((short) (rep.getInt("KOL") - 1));
            }
          } else if ("D".equals(rep.getString("TIP"))) {
            fillDate(cell, rep.getString("DATA"));
            cell.getCellStyle().setDataFormat(df.getFormat("dd.mm.yyyy"));
          }
        }
      } 
    }
    
    String oname = orig.getAbsolutePath();
    if (oname.endsWith(".xls"))
      oname = oname.substring(0, oname.length() - 4) + "-RA.xls";
    else oname = oname.substring(0, oname.length() - 5) + "-RA.xlsx";
    
    FileOutputStream out = null;
    
    try {
      out = new FileOutputStream(oname);
      wb.write(out);
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
  
  void fillCustom(Workbook wb, DataSet data) {
    int sheet = fld.getInt("SHEET");
    if (sheet > 0) --sheet;
    
    DataSet ds = null;
    
    String scr = Skripte.getDataModule().openTempSet(Condition.equal("CKEY", reps)).getString("TEKST");
    if (scr.startsWith("SELECT") || scr.startsWith("select"))  ds = Aus.q(scr);
    else {
      bsh = new Interpreter();
      try {
        bsh.set("thisBsh", bsh);
        frmSkripte.invoke("frmReportxList.init", bsh);
        bsh.set("dfrom", fld.getTimestamp("DATFROM"));
        bsh.set("dto", fld.getTimestamp("DATTO"));
        bsh.set("wb", wb);
      } catch (EvalError e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      Object o = frmSkripte.invoke(reps.getString("CKEY"), bsh);
      if (o != null & o instanceof DataSet)
        ds = (DataSet) o;
    }

  }
  
  void fillDate(Cell cell, String data) {
    data = data.trim().toUpperCase();
    if ("[DATFROM]".equals(data) ||
        "[DATUMFROM]".equals(data) ||
        "[DATUM-FROM]".equals(data)) {
      cell.setCellValue(fld.getTimestamp("DATFROM"));
    } else if ("[DATTO]".equals(data) ||
        "[DATUMTO]".equals(data) ||
        "[DATUM-TO]".equals(data)) {
      cell.setCellValue(fld.getTimestamp("DATTO"));
    } else if ("[TODAY]".equals(data)) {
      cell.setCellValue(Valid.getValid().getToday());
    }
  }
  
  void fillString(Cell cell, DataSet logo, String data) {
    VarStr repl = new VarStr(data);
    Variant v = new Variant();
    int beg, end;
    while ((beg = repl.indexOf('[')) >= 0 && (end = repl.indexOf(']')) > beg) {
      String tx = repl.mid(beg + 1, end);
      if (tx.length() > 0 && logo.hasColumn(tx) != null) {
        logo.getVariant(tx, v);
        tx = v.toString();
      }
      repl.replace(beg, end + 1, tx);
    }
    cell.setCellValue(repl.toString());
  }
  
  BigDecimal getMonthVal(DataSet gk, String konto, int month) {
    BigDecimal total = Aus.zero0;
    int year = Aus.getNumber(Util.getUtil().getYear(fld.getTimestamp("DATFROM")));
    Timestamp base = Aus.createTimestamp(year,  month == 0 ? 1 : month, 1);
    Timestamp beg = Util.getUtil().getFirstDayOfMonth(base);
    Timestamp end = Util.getUtil().getLastSecondOfDay(Util.getUtil().getLastDayOfMonth(base));
    for (gk.first(); gk.inBounds(); gk.next()) 
      if (gk.getString("BROJKONTA").startsWith(konto)) {
        Timestamp knj = gk.getTimestamp("DATUMKNJ");
        if (month == 0 || (!knj.before(beg) && !knj.after(end)))
          total = total.add(gk.getBigDecimal(gk.getBigDecimal("ID").signum() != 0 ? "ID" : "IP"));
      }
    return total;
  }
  
  void fillNum(Cell cell, DataSet gk, DataSet ogk, String data) {
    if (data.length() == 0) return;
    
    VarStr repl = new VarStr(data).trim();
    int beg, end;
    while ((beg = repl.indexOf('[')) >= 0 && (end = repl.indexOf(']')) > beg) {
      String tx = repl.mid(beg + 1, end);
      repl.replace(beg, end + 1, getVal(tx, gk));
    }
    while ((beg = repl.indexOf('{')) >= 0 && (end = repl.indexOf('}')) > beg) {
      String tx = repl.mid(beg + 1, end);
      repl.replace(beg, end + 1, getVal(tx, ogk));
    }
    Double val = new MathEvaluator(repl.toString()).getValue();
    cell.setCellValue(val.doubleValue());
  }

  String getVal(String kon, DataSet gk) {
    BigDecimal val = Aus.zero0;
    String[] parts = new VarStr(kon).split(',');
    for (int i = 0; i < parts.length; i++)
      val = val.add(getValPart(parts[i], gk));
    return val.toString();
  }
  
  BigDecimal getValPart(String kon, DataSet gk) {
    String konl = kon;
    int split = kon.indexOf('-');
    if (split > 0) {
      konl = kon.substring(split + 1);
      kon = kon.substring(0, split);
    }
    BigDecimal val = Aus.zero0;
    for (gk.first(); gk.inBounds(); gk.next()) {
      String k = gk.getString("BROJKONTA");
      if (k.length() > konl.length()) k = k.substring(0, konl.length());
      if (k.compareTo(konl) > 0) break;
      if (k.compareTo(kon) >= 0) {
        if (gk.getBigDecimal("ID").signum() != 0)
          val = val.add(gk.getBigDecimal("ID"));
        else val = val.add(gk.getBigDecimal("IP"));
      }
    }
    return val;
  }

  public void show() {
    if (isShowing()) return;
    if (app == null || app.length() == 0)
      Repxhead.getDataModule().setFilter(reps, "");
    else
      Repxhead.getDataModule().setFilter(reps, Condition.equal("APP", app));
    reps.open();
    System.out.println("count; "+reps.rowCount());
    if (reps.rowCount() == 0) {
      JOptionPane.showMessageDialog(this.getWindow(), "Ne postoji nijedan izvještaj!",
                                    "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }

    this.setTitle("Excel izvještaji");
    super.show();
  }

  public void setDataSet(QueryDataSet ds) {
    jp.setKumTak(true);
    jp.setDataSet(null);
    jp.setStoZbrojiti(new String[] {});
    jp.setKumTak(false);
    jp.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    Repxhead.getDataModule().setFilter(reps, "");
    setDataSet(reps);
    jp.getColumnsBean().setSaveSettings(false);
    jp.setVisibleCols(new int[] {1});
    jp.getNavBar().getColBean().initialize();
    jp.getNavBar().addOption(new raNavAction("Izlaz", raImages.IMGX, KeyEvent.VK_ESCAPE) {
      public void actionPerformed(ActionEvent e) {
        frmReportxList.this.hide();
      }
    });
    this.getContentPane().add(jp, BorderLayout.CENTER);
    this.getContentPane().add(okp, BorderLayout.SOUTH);
    jp.initKeyListener(this);
    okp.registerOKPanelKeys(this);
    
    jf.addChoosableFileFilter(filter);
    jf.addChoosableFileFilter(filter2);
    jf.setFileFilter(filter2);
    jf.setCurrentDirectory(new File("."));
    
    fld.setColumns(new Column[] {
        dM.createIntColumn("SHEET", "Rbr plahte"),
        dM.createStringColumn("CORG", "Org. jedinica", 12),
        dM.createTimestampColumn("DATFROM", "Poèetni datum"),
        dM.createTimestampColumn("DATTO", "Krajnji datum")
    });
    fld.open();
    fld.insertRow(false);
    jpc.bind(fld);
    jraDfrom.setColumnName("DATFROM");
    jraDfrom.setDataSet(fld);
    jraDto.setColumnName("DATTO");
    jraDto.setDataSet(fld);
    jraRbr.setColumnName("SHEET");
    jraRbr.setDataSet(fld);
    new raDateRange(jraDfrom, jraDto);
    
    JPanel up = new JPanel(new XYLayout(585, 90));
    
    up.add(jpc, new XYConstraints(0, 20, -1, -1));
    up.add(new JLabel("Datum"), new XYConstraints(15, 45, -1, -1));
    up.add(jraDfrom, new XYConstraints(150, 45, 100, -1));
    up.add(jraDto, new XYConstraints(255, 45, 100, -1));
    up.add(new JLabel("Plahta"), new XYConstraints(430, 45, -1, -1));
    up.add(jraRbr, new XYConstraints(495, 45, 50, -1));
    
    pan.add(up);
    pan.add(dlg.getOkPanel(), BorderLayout.SOUTH);
  }
}
