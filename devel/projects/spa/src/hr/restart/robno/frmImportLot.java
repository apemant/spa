package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raOptionDialog;
import hr.restart.util.Aus;
import hr.restart.util.IntParam;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.raProcess;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmImportLot {
  
  Valid vl = Valid.getValid();
  Util rut = Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  lookupData ld = lookupData.getlookupData();
  dM dm = dM.getDataModule();
  TypeDoc td = TypeDoc.getTypeDoc();

  static frmImportLot inst = new frmImportLot();
  
  JFileChooser fc = new JFileChooser();
  
  JraTextField jraFilename = new JraTextField();
  JraButton fDoh = new JraButton();
  JPanel cont = new JPanel();
  
  raOptionDialog od = new raOptionDialog() {
    protected void beforeShow() {
      center();
      jraFilename.requestFocusLater();
    }
    protected boolean checkOk() {
      return checkFile();
    }
  };
  
  StorageDataSet indat = new StorageDataSet();
  StorageDataSet workdat = new StorageDataSet();
  
  frmTableDataView view = new frmTableDataView(false, false, true) {
    protected void OKPress() {
      pohrani();
    }
  };
  
  File inf;
  
  private frmImportLot() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    fc.setCurrentDirectory(new File("."));
    String fn = IntParam.getTag("lotFile");
    if (fn != null) {
      File oldf = new File(fn);
      if (oldf.exists() && oldf.isFile())
        jraFilename.setText(oldf.getAbsolutePath());
      if (oldf.exists() || oldf.getParentFile() != null)
        fc.setCurrentDirectory(oldf);
    }
    
    JPanel pan = new JPanel(new XYLayout(550, 50));
    pan.add(new JLabel("Ulazna datoteka"), new XYConstraints(15, 18, -1, -1));
    pan.add(jraFilename, new XYConstraints(150, 15, 360, -1));
    pan.add(fDoh, new XYConstraints(515, 15, 21, 21));
    
    cont.setLayout(new BorderLayout());
    cont.add(pan);
    cont.add(od.getOkPanel(), BorderLayout.SOUTH);
    
    fDoh.setText("...");
    fDoh.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        selectFile();
      }
    });
    //Broj vozaèa;Broj dokumenta;Barkod;Kolièina;Lot;Rok trajanja;Datum
    indat.setColumns(new Column[] {
        dM.createStringColumn("CVOZ", 24),
        dM.createStringColumn("BRDOK", 32),
        dM.createStringColumn("BC", 20),
        dM.createBigDecimalColumn("KOL", 3),
        dM.createStringColumn("LOT", 12),
        dM.createTimestampColumn("ROKTRAJ"),
        dM.createTimestampColumn("DATDOK")
    });
    indat.open();
    
    workdat.setColumns(new Column[] {
        dM.createStringColumn("CVOZ", "Vozaè", 24),
        dM.createStringColumn("BRDOK", "Broj dokumenta", 32),
        dM.createStringColumn("BC", "Barkod", 20),
        dM.createStringColumn("NAZART", "Naziv artikla", 50),
        dM.createBigDecimalColumn("KOL", "Kolièina", 3),
        dM.createStringColumn("LOT", "Lot", 12),
        dM.createStringColumn("STATUS", "Akcija", 100),
        dM.createTimestampColumn("ROKTRAJ", "Rok trajanja"),
        dM.createStringColumn("ID", "Id stavke", 50),
        dM.createStringColumn("OST", "Orig. akcija", 100),
        dM.createStringColumn("OID", "Orig. id", 50),
    });
    workdat.open();
    workdat.getColumn("BRDOK").setWidth(16);
    workdat.getColumn("BC").setWidth(15);
    workdat.getColumn("ID").setWidth(25);
    workdat.getColumn("STATUS").setWidth(40);
    workdat.getColumn("OST").setVisible(0);
    workdat.getColumn("OID").setVisible(0);
    
    view.setSaveName("report-lot");
    view.setTitle("Podaci s vanjskog ureðaja");
    view.setVisibleCols(new int[] {1,2,3,4,6,8});
    
    view.jp.getNavBar().addOption(new raNavAction("Ruèno povezivanje", raImages.IMGMOVIE, KeyEvent.VK_F8) {
      public void actionPerformed(ActionEvent e) {
        connect();
      }
    }, 0);
    view.jp.getNavBar().addOption(new raNavAction("Izbaci / vrati", raImages.IMGPROPERTIES, KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
        toggle();
      }
    }, 1);
  }
  
  
  public static void show(Container parent) {
    inst.showPreselect(parent);
  }
  
  void selectFile() {
    if (fc.showOpenDialog(jraFilename) == fc.APPROVE_OPTION) {
      File sf = fc.getSelectedFile();
      if (sf.exists()) 
        jraFilename.setText(sf.getAbsolutePath());
    }
  }
  
  boolean checkFile() {
    String fname = jraFilename.getText().trim();
    if (fname.length() == 0) {
      jraFilename.requestFocus();
      JOptionPane.showMessageDialog(jraFilename, 
          "Odaberite ulaznu datoteku!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    inf = new File(fname);
    if (!inf.exists() || !inf.isFile()) {
      jraFilename.requestFocus();
      JOptionPane.showMessageDialog(jraFilename, 
          "Ulazna datoteka ne postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    if (!inf.canRead()) {
      jraFilename.requestFocus();
      JOptionPane.showMessageDialog(jraFilename, 
          "Ne mogu otvoriti ulaznu datoteku!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    return true;
  }
  
  private void showPreselect(Container parent) {
    if (view.isShowing()) view.hide();
    
    if (!od.show(parent, cont, "Odabir datoteke za unos")) return;

    String enc = frmParam.getParam("robno", "lotEnc", "Cp1250",
        "Encoding datoteke za unos lot-a", true);
    
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(
          new FileInputStream(inf), enc));
      
      try {
        importData(br);
      } finally {
        br.close();
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(parent, 
          "Pogrešno kodiranje datoteke!", "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(parent, 
          "Ulazna datoteka ne postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    } catch (IOException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(parent, 
          "Greška kod èitanja datoteke!", "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    IntParam.setTag("lotFile", inf.getAbsolutePath());
    
    bindData();
  }
  
  void importData(BufferedReader br) throws IOException {
    String line;
    indat.empty();
    while (null != (line = br.readLine())) {
      if (line.trim().length() == 0) continue;
      
      String[] parts = new VarStr(line).splitTrimmed(';');
      if (parts.length < 7)
        throw new IOException("Invalid line " + line);
      
      //Broj vozaèa;Broj dokumenta;Barkod;Kolièina;Lot;Rok trajanja;Datum
      indat.insertRow(false);
      indat.setString("CVOZ", trunc(parts[0].trim(), 24));
      indat.setString("BRDOK", trunc(parts[1].trim(), 32));
      indat.setString("BC", trunc(parts[2].trim(), 20));
      indat.setBigDecimal("KOL", Aus.getDecNumber(parts[3]));
      indat.setString("LOT", trunc(parts[4].trim(), 12));
      Timestamp rt = getDate(parts[5]);
      if (rt != null) indat.setTimestamp("ROKTRAJ", rt);
      Timestamp dat = getDate(parts[6]);
      if (dat != null) indat.setTimestamp("DATDOK", dat);
      indat.post();
    }
  }
  
  String trunc(String orig, int len) {
    if (orig.length() <= len) return orig;
    return orig.substring(0, len);
  }
  
  Calendar cal = Calendar.getInstance();
  private Timestamp getDate(String date) {
    try {
      int d = Aus.getNumber(date.substring(0, 2));
      int m = Aus.getNumber(date.substring(2, 4)) - 1;
      int y = Aus.getNumber(date.substring(4, 6));
      if (y < 37) y = 2000 + y;
      else if (y < 100) y = 1900 + y;
      cal.clear();
      cal.set(y, m, d);
      return new Timestamp(cal.getTime().getTime());
    } catch (Exception e) {
      return null;
    }
  }
  
  void postRow() {
    workdat.setString("OST", workdat.getString("STATUS"));
    workdat.setString("OID", workdat.getString("OID"));
    workdat.post();
  }
  
  //Condition minc;
  void bindData() {
    /*String dok = frmParam.getParam("robno", "lotDok", "ROT",
        "Vrsta dokumenta na koju se puni lot");
    String sklad = frmParam.getParam("robno", "lotCskl", "206",
        "Skladište dokumenata za lot");*/
    
    /*Set god = new HashSet();
    Set br = new HashSet();
    for (indat.first(); indat.inBounds(); indat.next())
      br.add(indat.getString("BRDOK"));
    
     minc = Condition.equal("CSKL", sklad).and(Condition.equal("VRDOK", dok));*/
    
    indat.setSort(new SortDescriptor(new String[] {"BRDOK"}));
    
    DataSet doh = null;
    String lastDok = "";
    String err = null;
    String dohs = null;
    String[] keyStdoki = {"VRDOK", "CSKL", "GOD", "BRDOK"};
    //String[] keySmeskla = {"VRDOK", "CSKLIZ", "CSKLUL", "GOD"};
    String[] ccols = {"CVOZ", "BRDOK", "BC", "KOL", "LOT", "ROKTRAJ"};
    workdat.empty();
    for (indat.first(); indat.inBounds(); indat.next()) {
      String dok = indat.getString("BRDOK");
      if (!dok.equals(lastDok)) {
        doh = null;
        err = dohs = null;
        lastDok = dok;
        String[] parts = new VarStr(dok).replaceAll('/', '-').split('-');
        if (td.isDocUlaz(parts[0]) || !td.isDocSklad(parts[0]))
          err = "kriva vrsta dokumenta";
        else if (td.isDocStdoki(parts[0])) {
          if (parts.length != 4)
            err = "pogrešan broj izlaznog dokumenta";
          else dohs = "SELECT doki.datdok, stdoki.cart, stdoki.bc, " +
          		"stdoki.nazart, stdoki.kol, stdoki.lot, stdoki.id_stavka " +
          		"FROM doki, stdoki WHERE " + rut.getDoc("doki", "stdoki") +
          		" AND " + Condition.whereAllEqual(keyStdoki, parts).
          		and(Condition.equal("BRDOK", Integer.parseInt(parts[3]))).
          		    qualified("doki");
        } else if (td.isDocStmeskla(parts[0])) {
          if (parts.length != 5)
            err = "pogrešan broj meðuskladišnice";
          else err = "meðuskladišnice se ne mogu povezati";
        }
        if (dohs != null) {
          System.out.println("query: "+dohs);
          doh = Aus.q(dohs);
        }
      }
      workdat.insertRow(false);
      dM.copyColumns(indat, workdat, ccols);
      if (err != null) {
        workdat.setString("STATUS", "Greška: "+err);
        postRow();
        continue;
      }
      if (!ld.raLocate(dm.getArtikli(), "BC", indat.getString("BC"))) {
        workdat.setString("STATUS", "Greška: nepoznat artikl");
        postRow();
        continue;
      }
      workdat.setString("NAZART", dm.getArtikli().getString("NAZART"));
      if (!ld.raLocate(doh, "BC", indat.getString("BC"))) {
        workdat.setString("STATUS", "Greška: artikla nema na dokumentu");
        postRow();
        continue;
      }
      String status = "";
      if (doh.getString("LOT").length() > 0) status += "Lot popunjen? ";
      if (doh.getBigDecimal("KOL").compareTo(indat.getBigDecimal("KOL")) != 0)
        status += "Nejednaka kolièina? ";
      if (!ut.sameDay(doh.getTimestamp("DATDOK"), indat.getTimestamp("DATDOK")))
        status += "Datum nije isti? ";
      if (status.length() == 0) status = "Sve OK";
      workdat.setString("STATUS", status);
      workdat.setString("ID", doh.getString("ID_STAVKA"));
    }
    postRow();
    
    view.setDataSet(workdat);
    view.jp.getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    view.show();
  }
  
  void toggle() {
    if (workdat.rowCount() == 0) return;
    
    if (workdat.getString("STATUS").equals("Ruèno povezana stavka") ||
        workdat.getString("STATUS").equals("Preskoèena stavka")) {
      workdat.setString("STATUS", workdat.getString("OST"));
      workdat.setString("ID", workdat.getString("OID"));
    } else {
      workdat.setString("STATUS", "Preskoèena stavka");
      workdat.setString("ID", "*");
    }
    view.jp.repaint();
  }
  
  void connect() {
    if (workdat.rowCount() == 0) return;
    
    String god = ut.getYear(vl.getToday());
    Condition gc = Condition.in("GOD", new String[] {
        god, Integer.toString(Aus.getNumber(god) + 1)});
    
    String qstr = "SELECT doki.cskl, doki.vrdok, doki.brdok, doki.datdok, " +
        "stdoki.cart, stdoki.bc, stdoki.nazart, stdoki.kol, " +
        "stdoki.lot, stdoki.id_stavka FROM doki,stdoki WHERE " +
        rut.getDoc("doki", "stdoki") + " AND " + 
        gc.and(Condition.equal("BC", workdat));
    
    StorageDataSet doh = new StorageDataSet();
    doh.setColumns(new Column[] {
        doki.getDataModule().getColumn("CSKL").cloneColumn(),
        doki.getDataModule().getColumn("VRDOK").cloneColumn(),
        doki.getDataModule().getColumn("BRDOK").cloneColumn(),
        doki.getDataModule().getColumn("DATDOK").cloneColumn(),
        stdoki.getDataModule().getColumn("CART").cloneColumn(),
        stdoki.getDataModule().getColumn("BC").cloneColumn(),
        stdoki.getDataModule().getColumn("NAZART").cloneColumn(),
        stdoki.getDataModule().getColumn("KOL").cloneColumn(),
        stdoki.getDataModule().getColumn("LOT").cloneColumn(),
        stdoki.getDataModule().getColumn("ID_STAVKA").cloneColumn()
    });
    ut.fillReadonlyData(doh, qstr);
    if (doh.rowCount() == 0) {
      return;
    }
    
    ld.saveName = "dohvat-lot-manual";
    ld.frameTitle = "Odabir stavke izlaza";
    try {
      String[] result = ld.lookUp(view, doh, 
          new String[] {"ID_STAVKA"},
          new String[] {""}, new int[] {0,1,2,3,4,5,6,7});
      if (result != null) {
        workdat.setString("ID", result[0]);
        workdat.setString("STATUS", "Ruèno povezana stavka");
        view.jp.repaint();
      }
    } finally {
      lookupData.getlookupData().saveName = null;
      lookupData.getlookupData().frameTitle = "Dohvat";
    }
  }
  
  void pohrani() {
    if (workdat.rowCount() == 0) return;
    
    boolean empty = false;
    int marked = 0;
    final Set ids = new HashSet();
    workdat.enableDataSetEvents(false);
    try {
      for (workdat.first(); workdat.inBounds(); workdat.next()) {
        String id = workdat.getString("ID");
        if (empty = (id.length() == 0)) break;
        if (!id.equals("*")) ids.add(id);
        else ++marked;
      }
    } finally {
      workdat.enableDataSetEvents(true);
    }
    if (empty) {
      view.jp.fireTableDataChanged();
      JOptionPane.showMessageDialog(view, "Stavka nije povezana!", 
          "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (ids.size() != workdat.rowCount() - marked) {
      JOptionPane.showMessageDialog(view, "Postoje duplikati!", 
          "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    raProcess.runChild(view, new Runnable() {
      public void run() {
        raProcess.setMessage("Dohvat stavaka za povezivanje...", true);
        QueryDataSet upd = stdoki.getDataModule().getTempSet(
            Condition.in("ID_STAVKA", ids.toArray()));
        upd.open();
        workdat.enableDataSetEvents(false);
        try {
          for (workdat.first(); workdat.inBounds(); workdat.next()) {
            if (ld.raLocate(upd, "ID_STAVKA", workdat.getString("ID"))) {
              upd.setString("LOT", workdat.getString("LOT"));
              upd.setString("CVOZ", workdat.getString("CVOZ"));
              if (!workdat.isNull("ROKTRAJ"))
                upd.setTimestamp("ROKTRAJ", workdat.getTimestamp("ROKTRAJ"));
              upd.post();
            }
          }
        } finally {
          workdat.enableDataSetEvents(true);
        }
        raProcess.setMessage("Snimanje promjena...", true);
        upd.saveChanges();
      }
    });
    if (raProcess.isInterrupted()) return;
    if (raProcess.isCompleted()) {
      view.hide();
      inf.delete();
      JOptionPane.showMessageDialog(null, "Snimanje uspješno završeno.",
          "Poruka", JOptionPane.INFORMATION_MESSAGE);
    } else {
      JOptionPane.showMessageDialog(null, "Snimanje neuspješno!",
          "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }
}
