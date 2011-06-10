package hr.restart.util;

import hr.restart.baza.Condition;
import hr.restart.baza.Expdata;
import hr.restart.baza.Exphead;
import hr.restart.baza.kreator;
import hr.restart.sisfun.TextFile;

import java.awt.Frame;
import java.io.File;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.Variant;


public class raExportData {
  static SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy.");
  public static void export() {
    if (Exphead.getDataModule().getRowCount() == 0) {
      JOptionPane.showMessageDialog(null, 
          "Nema datoteka za izvoz!", 
          "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
      
    kreator.SelectPathDialog spd = new kreator.SelectPathDialog(
        (Frame) null, "Putanja za spremanje podataka");
    String dir = IntParam.getTag("export.data.dir");
    spd.loadsave = (dir == null || dir.length() == 0) ? null : new File(dir);
    spd.show();
    if (spd.oksel) {
      File outdir = spd.loadsave;
      if (outdir != null)
        IntParam.setTag("export.data.dir", spd.loadsave.getAbsolutePath());
      else outdir = Aus.getCurrentDirectory();
      export(outdir);
    }
  }
  
  public static void export(File dir) {
    DataSet eh = Exphead.getDataModule().getTempSet();
    eh.open();
    for (eh.first(); eh.inBounds(); eh.next()) { 
      String fname = eh.getString("IMEDAT");
      if ("C".equals(eh.getString("TIPDAT")))
        exportFile(new File(dir, fname), 
            eh.getString("UPIT"), eh.getInt("CREP"));
    }
    JOptionPane.showMessageDialog(null, 
        "Izvoz podataka završen.", 
        "Izvoz podataka", JOptionPane.INFORMATION_MESSAGE);
  }
  
  public static void exportFile(File f, String upit, int crep) {
    if (f.exists() && !f.canWrite()) return;
    DataSet ed = Expdata.getDataModule().getTempSet(
        Condition.equal("CREP", crep));
    ed.open();
    ed.setSort(new SortDescriptor(new String[] {"RBR"}));
    if (ed.rowCount() == 0) return;
    
    int c = 0;
    OutColumn[] cols = new OutColumn[ed.rowCount()];
    for (ed.first(); ed.inBounds(); ed.next())
      cols[c++] = new OutColumn(ed);
    
    TextFile out = TextFile.write(f);
    if (out == null) return;
    
    VarStr line = new VarStr();
    String[] qs = new VarStr(upit).split(';');
    
    for (int q = 0; q < qs.length; q++) {
      DataSet ds = Aus.q(qs[q]);
      for (ds.first(); ds.inBounds(); ds.next()) {
        line.clear();
        for (int i = 0; i < cols.length; i++) {
          int type = ds.getColumn(cols[i].name).getDataType();
          if (type == Variant.STRING)
            line.append('"').append(ds.getString(cols[i].name).trim()).
                append("\",");
          else if (type == Variant.BIGDECIMAL)
            line.append('"').append(ds.getBigDecimal(cols[i].name)).
                append("\",");
          else if (type == Variant.TIMESTAMP)
            line.append('"').append(sd.format(
                ds.getTimestamp(cols[i].name))).append("\",");
          else if (type == Variant.INT)
            line.append(ds.getInt(cols[i].name)).append(',');
          else if (type == Variant.SHORT)
            line.append(ds.getShort(cols[i].name)).append(',');
          else if (type == Variant.LONG)
            line.append(ds.getLong(cols[i].name)).append(',');
        }
        line.chop();
        out.out(line.toString());
      }
    }
    out.close();
  }
  
  static class OutColumn {
    String name;
    int chars;
    String format;
    public OutColumn(DataSet ds) {
      name = ds.getString("IMEKOL");
      chars = ds.getInt("NUMCHAR");
      format = ds.getString("FORMAT");
    }
  }
}
