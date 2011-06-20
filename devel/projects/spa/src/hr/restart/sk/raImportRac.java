package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.Shkonta;
import hr.restart.baza.Skstavke;
import hr.restart.baza.UIstavke;
import hr.restart.baza.dM;
import hr.restart.baza.kreator;
import hr.restart.sisfun.dlgUraIra;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraDialog;
import hr.restart.util.Aus;
import hr.restart.util.IntParam;
import hr.restart.util.JlrNavField;
import hr.restart.util.OKpanel;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class raImportRac {
  
  DateFormat df = new SimpleDateFormat("d.M.yyyy. H:m:s");
  lookupData ld = lookupData.getlookupData();
  Util ut = Util.getUtil();
  dM dm = dM.getDataModule();
  String cskl, cknjige, seqKey;
  QueryDataSet shema;
  Map kols;
  String[] uicol = {"KNJIG", "CPAR", "VRDOK", "BROJDOK", 
                   "CORG", "CKNJIGE", "CSKSTAVKE"};
  
  boolean bookDependant, autoinc;
  int extSize;
  dlgUraIra dlg = new dlgUraIra();

  
  public static void show() {
    kreator.SelectPathDialog spd = new kreator.SelectPathDialog(
        (Frame) null, "Putanja za import ra�una");
    String dir = IntParam.getTag("importxml.dir");
    spd.loadsave = (dir == null || dir.length() == 0) ? null : new File(dir);
    spd.show();
    if (spd.oksel) {
      raImportRac ir = new raImportRac();
      if (ir.shema.rowCount() == 0) {
        JOptionPane.showMessageDialog(null, 
            "Nije definirana shema za import!",
            "Gre�ka", JOptionPane.ERROR_MESSAGE);
        return;
      }
      IntParam.setTag("importxml.dir", spd.loadsave.getAbsolutePath());
      File[] list = spd.loadsave.listFiles();
      for (int i = 0; i < list.length; i++)
        if (list[i].getName().toLowerCase().endsWith(".xml"))
          ir.importSingle(list[i]);
    }
  }
  
  protected raImportRac() {
    cskl = frmParam.getParam("sk", "importShema", "",
        "Shema za import ra�una iz xml-ova");
    cknjige = frmParam.getParam("sk", "importKnjiga", "A",
        "Knjiga za import ra�una iz xml-ova");
    
    shema = Shkonta.getDataModule().getTempSet(
        Condition.equal("VRDOK", "IRN").and(
            Condition.equal("CSKL", cskl)));
    shema.open();
    
    String[][] data = {
        {"TotalBrutto", "6"},
        {"TotalExemption", "7"},
        {"TotalTaxFreeExp", "8"},
        {"TotalTaxFreeTransit", "9"},
        {"TotalTaxFreeInLand", "10"},
        {"TotalTaxFree", "11"},
        {"TotalRevenueBase1", "12"},
        {"TotalRevenueBase2", "13"},
        {"TotalRevenue2", "14"},
        {"TotalRevenueBase3", "17"},
        {"TotalRevenue3", "18"},
    };
    kols = new HashMap();
    for (int i = 0; i < data.length; i++)
      kols.put(data[i][0], data[i][1]);
    
    autoinc = frmParam.getParam("sk", "autoIncExt", "D", 
    "Automatsko pove�avanje dodatnog broja URA/IRA (D/N)").equalsIgnoreCase("D");
    bookDependant = frmParam.getParam("sk", "extKnjiga", "D", 
      "Ima li svaka knjiga zaseban broja� (D/N)").equalsIgnoreCase("D");
    extSize = Aus.getNumber(frmParam.getParam("sk", "extSize", "0",
      "Minimalna velicina broja URA/IRA (popunjavanje vede�im nulama)"));
    if (extSize > 8) extSize = 8;
    
  }
  
  void importSingle(File f) {
    try { 
      Document doc = new SAXBuilder().build(f);
      if (createRac(doc)) f.delete();
    } catch (JDOMException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (RuntimeException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "Gre�ka: " + e.getMessage(),
          "Gre�ka", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  boolean createRac(Document doc) {
    Element root = doc.getRootElement();
    if (!"NS_InvoiceOut".equals(root.getName())) return false;
    
    Element head = root.getChild("InvoiceOut_Header");
    if (head == null) return false;
    
    QueryDataSet sk = Skstavke.getDataModule().getTempSet("1=0");
    sk.open();
    QueryDataSet ui = UIstavke.getDataModule().getTempSet("1=0");
    ui.open();
    
    sk.setString("KNJIG", OrgStr.getKNJCORG(false));
    sk.setString("CORG", sk.getString("KNJIG"));
    sk.setInt("BROJIZV", 0);
    sk.setString("VRDOK", "IRN");
    
    setDate(sk, "DATUNOS", head, "ServiceDate");
    setDate(sk, "DATDOK", head, "InvoiceDate");
    setDate(sk, "DATDOSP", head, "InvoiceDueDate");
    setDate(sk, "DATUMKNJ", head, "BookedOn");
    setDate(sk, "DATPRI", head, "BookedOn");
    
    int next = Valid.getValid().findSeqInt(seqKey = 
      OrgStr.getKNJCORG(false) + "IRA-" +
      ut.getYear(sk.getTimestamp("DATDOK")) +
      (bookDependant ? "-" + sk.getString("CKNJIGE") : ""), 
      false, false);

    String result = Integer.toString(next);
    if (result.length() < extSize) 
      result = Aus.string(extSize - result.length(), '0') + result;
    sk.setString("EXTBRDOK", result);

    sk.setString("BROJDOK", head.getChildText("DocumentIdentifier"));
    
    Element partn = head.getChild("Buyer");
    String oib = partn.getChildText("OIB");
    if (ld.raLocate(dm.getPartneri(), "OIB", oib))
      sk.setInt("CPAR", dm.getPartneri().getInt("CPAR"));
    else throw new RuntimeException("Nepoznat OIB: "+ oib); 
    
    sk.setString("BROJKONTA", getKonto("6"));
    sk.setString("CSKSTAVKE", raSaldaKonti.findCSK(sk));
    sk.setString("CSKL", cskl);
    sk.setString("CKNJIGE", cknjige);
    
    List stav = head.getChildren("NS_Amount");
    int rbs = 1;
    for (Iterator i = stav.iterator(); i.hasNext(); ) {
      Element e = (Element) i.next();
      String tip = e.getChildText("TypeCode");
      if (tip.equals("TotalBrutto")) {
      	BigDecimal amount = Aus.getDecNumber(e.getChildText("Amount"));
      	ui.insertRow(false);
        ui.setString("URAIRA", "I");
        dM.copyColumns(sk, ui, uicol);
        String kol = (String) kols.get(tip);
        ui.setShort("CKOLONE", Short.parseShort(kol));
        ui.setString("BROJKONTA", getKonto(kol));
        ui.setInt("RBS", 1);
        ui.setString("DUGPOT", "D");
        ui.setBigDecimal("ID", amount);
        sk.setBigDecimal("ID", amount);
        sk.setBigDecimal("SSALDO", amount);
        sk.setBigDecimal("SALDO", amount);
        sk.setBigDecimal("PVID", amount);
        sk.setBigDecimal("PVSSALDO", amount);
        sk.setBigDecimal("PVSALDO", amount);
      }
      /*if (!kols.containsKey(tip)) continue;
      BigDecimal amount = Aus.getDecNumber(e.getChildText("Amount"));
      if (amount.signum() != 0) {
        ui.insertRow(false);
        ui.setString("URAIRA", "I");
        dM.copyColumns(sk, ui, uicol);
        String kol = (String) kols.get(tip);
        ui.setShort("CKOLONE", Short.parseShort(kol));
        ui.setString("BROJKONTA", getKonto(kol));
        if (kol.equals("6")) {
          ui.setInt("RBS", 1);
          ui.setString("DUGPOT", "D");
          ui.setBigDecimal("ID", amount);
          sk.setBigDecimal("ID", amount);
          sk.setBigDecimal("SSALDO", amount);
          sk.setBigDecimal("SALDO", amount);
          sk.setBigDecimal("PVID", amount);
          sk.setBigDecimal("PVSSALDO", amount);
          sk.setBigDecimal("PVSALDO", amount);
        } else {
          ui.setInt("RBS", ++rbs);
          ui.setString("DUGPOT", "P");
          ui.setBigDecimal("IP", amount);
        }
      }*/
    }
    List itm = head.getChildren("InvoiceOut_Items");
    for (Iterator i = itm.iterator(); i.hasNext(); ) {
      Element e = (Element) i.next();
      String biz = e.getChildText("BizCode");
      String desc = e.getChildText("Description");
      String por = e.getChildText("VATRate");
      
      
    }
    
    return false;
  }
  
  void setDate(DataSet ds, String col, Element el, String child) {
    try {
      String temp = el.getChildText(child);
      ds.setTimestamp(col, new Timestamp(df.parse(temp).getTime()));
    } catch (Exception e) {
      //
    }
  }
  
  String getKonto(String kol) {
    if (!ld.raLocate(shema, "CKOLONE", kol))
      throw new RuntimeException("Nedefinirana kolona " + kol);
    return shema.getString("BROJKONTA");
  }
  
  public class dlgUraIra {
    public static final int IRA = 1;
    public static final int URA = 2;

    //dlgUraIra dlg = new dlgUraIra();
    JDialog jd;

    JPanel main = new JPanel();
    JPanel center = new JPanel();
    XYLayout xy = new XYLayout();

    JLabel jlknj = new JLabel();
    JlrNavField konto = new JlrNavField();
    JlrNavField nazkonta = new JlrNavField();
    JraButton jbselknj = new JraButton();
    JLabel jlkol = new JLabel();
    JLabel jltex = new JLabel();
    JlrNavField ckolone = new JlrNavField();
    JlrNavField nazkolone = new JlrNavField();
    JraButton jbselkol = new JraButton();

    OKpanel okp = new OKpanel() {
      public void jBOK_actionPerformed() {
        OKPress();
      }
      public void jPrekid_actionPerformed() {
        cancelPress();
      }
    };

    StorageDataSet fields = new StorageDataSet();
    boolean ok;

    private dlgUraIra() {
      try {
        jbInit();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public void open(Window owner) {
      show(owner);
    }

    private void show(Window owner) {
      //String[] cols = new String[] {"CKNJIGE", "CKOLONE"};
      if (owner instanceof Dialog)
        jd = new JraDialog((Dialog) owner, "Konto i kolona", true);
      else jd = new JraDialog((Frame) owner, "Konto i kolona", true);
      jd.getContentPane().add(main);
      okp.registerOKPanelKeys(jd);
      jd.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
      jd.addWindowListener(new WindowAdapter() {
        public void windowClosed(WindowEvent e) {
          jd.dispose();
        }
      });
//      fields.setString("CKNJIGE", owner.getRaQueryDataSet().getString("CKNJIGE"));
//      fields.setShort("CKOLONE", owner.getRaQueryDataSet().getShort("CKOLONE"));
//      if (!owner.getRaQueryDataSet().isUnassignedNull("CKOLONE"))
//        fields.setShort("CKOLONE", owner.getRaQueryDataSet().getShort("CKOLONE"));
//      else fields.setUnassignedNull("CKOLONE");
//      DataSet.copyTo(cols, owner.getRaQueryDataSet(), cols, fields);
      //dM.copyColumns(owner.getRaQueryDataSet(), fields, cols);
      if (fields.getString("CKNJIGE") == null ||
          fields.getString("CKNJIGE").length() == 0)
        fields.setAssignedNull("CKOLONE");

      //cknjige.forceFocLost();
      //ckolone.forceFocLost();
      ok = false;
      jd.pack();
      jd.setLocationRelativeTo(jd.getOwner());
      jd.setVisible(true);
      if (ok) {
//        DataSet.copyTo(cols, fields, cols, owner.getRaQueryDataSet());
//        owner.getRaQueryDataSet().setString("CKNJIGE", fields.getString("CKNJIGE"));
//        if (fields.isuna
//        owner.getRaQueryDataSet().setShort("CKOLONE", fields.getShort("CKOLONE"));
        //dM.copyColumns(fields, owner.getRaQueryDataSet(), cols);
      }
    }

    public void setEnabled(boolean yesno) {
      raCommonClass.getraCommonClass().EnabDisabAll(main, yesno);
      raCommonClass.getraCommonClass().setLabelLaF(okp.jPrekid, true);
    }

    public void setUI() {
      konto.setRaDataSet(dM.getDataModule().getKonta());
      ckolone.setRaDataSet(dM.getDataModule().getIzlazneKolone());
    }

    private void OKPress() {
      ok = true;
      jd.dispose();
    }

    private void cancelPress() {
      jd.dispose();
    }

    private void jbInit() throws Exception {
      center.setLayout(xy);
      xy.setWidth(525);
      xy.setHeight(110);

      fields.setColumns(new Column[] {
        dM.createStringColumn("BROJKONTA", "Konto", 5),
        dM.createStringColumn("NAZIVKONTA", "Naziv konta", 50),
        dM.createShortColumn("CKOLONE", "Broj kolone"),
        dM.createStringColumn("NAZIVKOLONE", "Naziv kolone", 50)
      });
      fields.open();

      jlknj.setText("Konto");
      konto.setColumnName("KONTO");
      konto.setDataSet(fields);
      konto.setColNames(new String[] {"NAZKONTA"});
      konto.setTextFields(new javax.swing.text.JTextComponent[] {nazkonta});
      konto.setVisCols(new int[] {0,4});
      konto.setSearchMode(0);
      konto.setRaDataSet(dM.getDataModule().getKnjigeUI());
      konto.setNavButton(jbselknj);

      jlkol.setText("Kolona knjige");
      nazkonta.setNavProperties(konto);
      nazkonta.setDataSet(fields);
      nazkonta.setColumnName("NAZKONTO");
      nazkonta.setSearchMode(1);

      ckolone.setColumnName("CKOLONE");
      ckolone.setDataSet(fields);
      ckolone.setColNames(new String[] {"NAZIVKOLONE"});
      ckolone.setTextFields(new javax.swing.text.JTextComponent[] {nazkolone});
      ckolone.setVisCols(new int[] {0,1,2});
      ckolone.setSearchMode(0);
      ckolone.setRaDataSet(dM.getDataModule().getKoloneknjUI());
      ckolone.setNavButton(jbselkol);

      nazkolone.setNavProperties(ckolone);
      nazkolone.setDataSet(fields);
      nazkolone.setColumnName("NAZIVKOLONE");
      nazkolone.setSearchMode(1);

      center.add(jltex, new XYConstraints(150, 20, -1, -1));
      center.add(jlknj, new XYConstraints(15, 45, -1, -1));
      center.add(konto, new XYConstraints(150, 45, 50, -1));
      center.add(nazkonta, new XYConstraints(205, 45, 275, -1));
      center.add(jbselknj, new XYConstraints(485, 45, 21, 21));

      center.add(jlkol, new XYConstraints(15, 70, -1, -1));
      center.add(ckolone, new XYConstraints(150, 70, 50, -1));
      center.add(nazkolone, new XYConstraints(205, 70, 275, -1));
      center.add(jbselkol, new XYConstraints(485, 70, 21, 21));

      main.setLayout(new BorderLayout());
      main.add(center, BorderLayout.CENTER);
      main.add(okp, BorderLayout.SOUTH);
    }
  }
}
