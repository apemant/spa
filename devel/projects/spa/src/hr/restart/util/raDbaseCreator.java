package hr.restart.util;

import hr.restart.DbUpdater;
import hr.restart.baza.Condition;
import hr.restart.baza.ConsoleCreator;
import hr.restart.baza.KreirDrop;
import hr.restart.baza.dM;
import hr.restart.baza.raTransferNotifier;

import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;

public class raDbaseCreator {
  private File source;
  
  private raDbaseCreator() {
    // private
  }
  
  private raDbaseCreator(File src) {
    source = src;
  }
  
  public static void initFrom(File src) {
    new raDbaseCreator(src).init();
  }
  
  public static String dumpTo(File dest) {
    return new raDbaseCreator(dest).dump();
  }
  
  void init() {
    raProcess.runChild(new Runnable() {
      public void run() {
        raProcess.disableInterrupt();
        try {
          initInProcess();
        } finally {
          cleanup();
        }
      }
    });
  }
  
  String dump() {
    raProcess.runChild(new Runnable() {
      public void run() {
        raProcess.disableInterrupt();
        try {
          raProcess.yield(dumpInProcess());
        } finally {
          cleanup();
        }
      }
    });
    return (String) raProcess.getReturnValue();
  }
  
  String dumpInProcess() {
    installDumpNotifier();
    
    raProcess.setMessage("Priprema arhiviranja baze podataka ...", true);
    
    String retVal = null;
    
    File dest = source;
    if (!dest.isDirectory()) {
      int i = 0;
      File tmpDir = new File("dbcre.tmp");
      while (tmpDir.exists())
        tmpDir = new File("dbcre" + (++i) + ".tmp");
      dest = tmpDir;
      dest.mkdir();
    }
    
    String[] modules = ConsoleCreator.getModuleClasses();
    KreirDrop kdp;
    
    for (int i = 0; i < modules.length; i++)
      try {
        kdp = KreirDrop.getModule(modules[i].toString());
        if (kdp == null)
          kdp = (KreirDrop) Class.forName(modules[i]).newInstance();
        dumpData(kdp, dest);
      } catch (Exception e) {
        e.printStackTrace();
      }
    
    if (!source.isDirectory()) {
      raProcess.setMessage("Arhiviranje podataka baze ...", false);
      
      File[] dats = dest.listFiles();
      if (!FileHandler.makeZippedFile(dats, source))
        retVal = "Neuspješno kreiranje ZIP datoteke!";

      raProcess.setMessage("Brisanje privremenih datoteka ...", false);
      for (int i = 0; i < dats.length; i++)
        dats[i].delete();
      dest.delete();
    }
    
    return retVal;
  }
  
  public String dumpData(KreirDrop kdp, File dir) {
    try {
      String tname = kdp.getColumns()[0].getTableName().toLowerCase();
      int totalRows = kdp.getRowCount();
      if (totalRows * kdp.getColumns().length < ConsoleCreator.maxLoad) {
        raProcess.setMessage("Otvaranje tablice "+kdp.Naziv+" ...", false);
        DataSet ds = Util.getNewQueryDataSet("SELECT * FROM " + tname);
        if (kdp.dumpTable(ds, dir) > 0 || (ds.close() && false))
            return tname + ".dat - spremljen!";
        return "Tablica prazna!";              
      }
      
      raProcess.setMessage("Segmentiranje tablice "+kdp.Naziv+" ...", false);
      Int2 sgt = kdp.findBestKeyForSegments();
      if (sgt == null) return "Greška kod spremanja!";
        
      String bestCol = kdp.getColumns()[sgt.one].getColumnName().toLowerCase();
      int bestNum = sgt.two;
      int minSegments = totalRows * kdp.getColumns().length / ConsoleCreator.maxLoad + 2;
      if (bestNum / 10 <= minSegments)
        return "Greška kod spremanja!";

      Condition[] conds = kdp.createSegments(bestCol, minSegments);
      if (kdp.dumpSegments(dir, conds) > 0)
        return tname + ".dat - spremljen!";
      return "Greška kod spremanja!";
    } catch (Exception e) {
      e.printStackTrace();
      return "Greška kod spremanja!";
    }
  }
  
  void initInProcess() {
    installInitNotifier();
    
    raProcess.setMessage("Priprema inicijalizacije baze podataka ...", true);
    dM.setMinimalMode();
    dM dm = dM.getDataModule();
    
    HashMap valid = new HashMap();
    
    String[] modules = ConsoleCreator.getModuleClasses();
    KreirDrop kdp;
    
    for (int i = 0; i < modules.length; i++)
      try {
        kdp = (KreirDrop) Class.forName(modules[i]).newInstance();
        raProcess.setMessage("Kreiranje tablice " + kdp.Naziv, false);
        kdp.DropTable();
        kdp.KreirTable();
        kdp.KreirIdx();
        
        valid.put(kdp.getColumns()[0].getTableName().toLowerCase(), kdp);
      } catch (Exception e) {
        e.printStackTrace();
      }
      
    File src = source;
    if (!src.isDirectory()) {
      raProcess.setMessage("Raspakiravanje arhive podataka ...", false);
      int i = 0;
      File tmpDir = new File("dbcre.tmp");
      while (tmpDir.exists())
        tmpDir = new File("dbcre" + (++i) + ".tmp");
      src = tmpDir;
      src.mkdir();
      
      try {
        ZipInputStream zin = new ZipInputStream(
          new BufferedInputStream(new FileInputStream(source)));
        try {
          ZipEntry entry;
          while ((entry = zin.getNextEntry()) != null)
            if (!entry.getName().endsWith(File.separator))
              unzipFile(zin, entry.getName(), src);
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          try {
            zin.close();
          } catch (IOException e) {
            // empty
          }
        }
      } catch (Exception ze) {
        ze.printStackTrace();
      }
    }
    
    raProcess.setMessage("Punjenje podataka u bazu ...", false);
    File[] dats = src.listFiles();
    for (int i = 0; i < dats.length; i++)
      if (dats[i].getName().endsWith(".dat")) {
        String name = dats[i].getName();
        name = name.substring(0, name.indexOf('.'));
        kdp = (KreirDrop) valid.get(name);
        try {
          if (kdp != null) kdp.insertData(src);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    
    DbUpdater.updateDBVersion();
    if (!source.isDirectory()) {
      raProcess.setMessage("Brisanje privremenih datoteka ...", false);
      for (int i = 0; i < dats.length; i++)
        dats[i].delete();
      src.delete();
    }
    dm.getParametri().refresh();
  }

  int bufSize = 8192;
  byte[] buf = new byte[bufSize];
  
  void unzipFile(ZipInputStream zin, String name, File dir) {
    int last = name.lastIndexOf(File.separatorChar);
    if (last >= 0) name = name.substring(last + 1);
    
    try {
      BufferedOutputStream dest = new BufferedOutputStream(
                  new FileOutputStream(new File(dir, name)));
      
      try {
        int count;
        while ((count = zin.read(buf, 0, bufSize)) != -1)
          dest.write(buf, 0, count);
        dest.flush();
      } finally {
        dest.close();
      }
    } catch (Exception eo) {
      eo.printStackTrace();
    }
  }
  
  void installDumpNotifier() {
    KreirDrop.installNotifier(new raTransferNotifier() {
      private int total;
      public void rowTransfered(String table, int action, int row, Object data) {
        int ev;
        switch (action) {
          case raTransferNotifier.DUMP_STARTED:
            total = row;
            break;
          case raTransferNotifier.ROW_STORED:
            ev = Math.min(17, (int) Math.round(Math.sqrt(total)) + 1);
            if (row % ev == 1)
              raProcess.setMessage(table + ": spremljeno "+row+"/"+total+" redova", false);
            break;
          case raTransferNotifier.DUMP_SEGMENT_FINISHED:
            raProcess.setMessage(table + ": spremljeno "+row+"/"+total+" redova", false);
            break;
          case raTransferNotifier.DUMP_FINISHED:
            raProcess.setMessage(table + ": spremljeno "+row+"/"+total+" redova", false);
            break;
        }
      }
    });
  }

  void installInitNotifier() {
    KreirDrop.installNotifier(new raTransferNotifier() {
      private int total, errs;
      public void rowTransfered(String table, int action, int row, Object data) {
        int ev;
        switch (action) {
          case raTransferNotifier.INSERT_STARTED:
            total = errs = 0;
            break;
          case raTransferNotifier.ROW_INSERT_FAILED:
            ++errs;
            break;
          case raTransferNotifier.ROW_INSERTED:
            ev = Math.min(17, (int) Math.round(Math.sqrt(++total)) + 1);
            if (row % ev == 0)
              raProcess.setMessage(table + ": napunjeno "+row+"("+total+") redova", false);
            break;
          case raTransferNotifier.INSERT_FINISHED:
            raProcess.setMessage(table + ": napunjeno "+row+"("+total+") redova", false);
            break;
          default:
            break;
        }
      }
    });
  }
  
  void cleanup() {
    KreirDrop.removeNotifier();
  }
  
  public static void displayDumpDialog(Component parent) {
    JFileChooser fc = new JFileChooser(new File("."));
    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    if (fc.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return;
    
    File out = fc.getSelectedFile();
    if (!out.isDirectory() && !out.getName().toLowerCase().endsWith(".zip"))
      out = new File(out.getParent(), out.getName() + ".zip");
    
    if (out.exists() && !out.isDirectory()) {
      if (JOptionPane.showConfirmDialog(parent, 
          "Datoteka \""+out.getName()+"\" veæ postoji. Prebrisati?",
          "Datoteka postoji", JOptionPane.OK_CANCEL_OPTION,
          JOptionPane.WARNING_MESSAGE) != JOptionPane.OK_OPTION) return;
      
      if (!out.delete()) {
        JOptionPane.showMessageDialog(parent,
            "Neuspješno brisanje stare datoteke!", "Greška",
            JOptionPane.ERROR_MESSAGE);
        return;
      }
    }
    
    String ret = dumpTo(out);
    if (ret == null)
      JOptionPane.showMessageDialog(parent,
          "Baza pohranjena u datoteku \"" + out.getName() + "\".", "Poruka",
          JOptionPane.INFORMATION_MESSAGE);
    else
      JOptionPane.showMessageDialog(parent, ret, "Greška",
          JOptionPane.INFORMATION_MESSAGE);
  }
}
