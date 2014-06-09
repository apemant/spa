package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.Shkonta;
import hr.restart.baza.Skstavke;
import hr.restart.baza.UIstavke;
import hr.restart.baza.dM;
import hr.restart.baza.kreator;
import hr.restart.sisfun.Asql;
import hr.restart.sisfun.dlgUraIra;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraDialog;
import hr.restart.util.*;
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
  DateFormat df2 = new SimpleDateFormat("d.M.yyyy H:m:s");
  lookupData ld = lookupData.getlookupData();
  Valid vl = Valid.getValid();
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
        (Frame) null, "Putanja za import raèuna");
    String dir = IntParam.getTag("importxml.dir");
    spd.loadsave = (dir == null || dir.length() == 0) ? null : new File(dir);
    spd.show();
    if (spd.oksel) {
      raImportRac ir = new raImportRac();
      if (ir.shema.rowCount() == 0) {
        JOptionPane.showMessageDialog(null, 
            "Nije definirana shema za import!",
            "Greška", JOptionPane.ERROR_MESSAGE);
        return;
      }
      IntParam.setTag("importxml.dir", spd.loadsave.getAbsolutePath());
      File[] list = spd.loadsave.listFiles();
      for (int i = 0; i < list.length; i++)
        if (list[i].getName().toLowerCase().endsWith(".xml"))
          ir.importSingle(list[i]);
      
      JOptionPane.showMessageDialog(null, "Obrada završena.",
          "Poruka", JOptionPane.INFORMATION_MESSAGE);
    }
  }
  
  protected raImportRac() {
    cskl = frmParam.getParam("sk", "importShema", "",
        "Shema za import raèuna iz xml-ova");
    cknjige = frmParam.getParam("sk", "importKnjiga", "A",
        "Knjiga za import raèuna iz xml-ova");
    
    shema = Shkonta.getDataModule().getTempSet(
        Condition.equal("VRDOK", "IRN").and(
            Condition.equal("CSKL", cskl)));
    shema.open();
    
    String[][] data = {
        {"TotalAmount", "6"},
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
    "Automatsko poveæavanje dodatnog broja URA/IRA (D/N)").equalsIgnoreCase("D");
    bookDependant = frmParam.getParam("sk", "extKnjiga", "D", 
      "Ima li svaka knjiga zaseban brojaè (D/N)").equalsIgnoreCase("D");
    extSize = Aus.getNumber(frmParam.getParam("sk", "extSize", "0",
      "Minimalna velicina broja URA/IRA (popunjavanje vedeæim nulama)"));
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
      JOptionPane.showMessageDialog(null, "Greška: " + e.getMessage(),
          "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  boolean createRac(Document doc) {
    Element root = doc.getRootElement();
    if (!"NS_InvoiceOut".equals(root.getName())) return false;
    
    Element msg = root.getChild("NS_MessageHeader");
    if (msg == null) return false;
    
    String msgType = msg.getChildText("DocumentType");
    if (!"TDOC_InvoiceOut".equals(msgType)) return false;
    
    Element head = root.getChild("InvoiceOut_Header");
    if (head == null) return false;
    
    System.out.println("Otvorio root");
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
    setDate(sk, "DATUMKNJ", head, "InvoiceDate");
    setDate(sk, "DATPRI", head, "InvoiceDate");
    
    sk.setString("BROJDOK", head.getChildText("DocumentIdentifier") + "/" + ut.getYear(sk.getTimestamp("DATDOK")));
    System.out.println("Dokument: " + sk.getString("BROJDOK"));
    
    Element partn = head.getChild("Buyer");
    String oib = partn.getChildText("OIB");
    String nazpar = partn.getChildText("Name");
    if (ld.raLocate(dm.getPartneri(), "OIB", oib))
      sk.setInt("CPAR", dm.getPartneri().getInt("CPAR"));
    else throw new RuntimeException("Nepoznat OIB: "+ oib + 
        " dokument " + sk.getString("BROJDOK")); 
    
    sk.setString("BROJKONTA", getKonto("6"));
    sk.setString("CSKSTAVKE", raSaldaKonti.findCSK(sk));
    sk.setString("CSKL", cskl);
    sk.setString("CKNJIGE", cknjige);
    
    List stav = head.getChildren("NS_Amount");
    System.out.println("broj kategorija: " + stav.size());
    int rbs = 1;
    for (Iterator i = stav.iterator(); i.hasNext(); ) {
      Element e = (Element) i.next();
      String tip = e.getChildText("TypeCode");
      if (tip.equals("TotalAmount")) {
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
        ui.setBigDecimal("IP", Aus.zero2);
        sk.setBigDecimal("ID", amount);
        sk.setBigDecimal("IP", Aus.zero2);
        sk.setBigDecimal("SSALDO", amount);
        sk.setBigDecimal("SALDO", amount);
        sk.setBigDecimal("TECAJ", Aus.one0);
        sk.setString("OZNVAL", "kn");
        sk.setBigDecimal("PVID", amount);
        sk.setBigDecimal("PVIP", Aus.zero2);
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
    BigDecimal total = Aus.zero2;
    BigDecimal totalosn = Aus.zero2;
    BigDecimal totalpor = Aus.zero2;
    List itm = root.getChildren("InvoiceOut_Items");
    System.out.println("broj stavaka: " + itm.size());
    for (Iterator i = itm.iterator(); i.hasNext(); ) {
      Element e = (Element) i.next();
      
      Element camt = e.getChild("NS_Amount");
      if (camt == null) continue;
      String amt = camt.getChildText("Amount");
      if (amt == null) continue;
      BigDecimal iznos = Aus.getDecNumber(amt);
      if (iznos.signum() == 0) continue;
      
      String biz = e.getChildText("BizCode");
      String desc = e.getChildText("Description");
      String por = e.getChildText("VATRate");
      if (por == null || por.length() == 0) por = "0";

      System.out.println("bizcode " + biz);
      System.out.println("opis " + desc);
      String konto = null;
      if (biz != null && biz.length() > 0)
        konto = getKontoAll("#" + biz, por + "%");
      else if (desc != null && desc.length() > 0)
        konto = getKontoAll(desc, por + "%");
      else
        throw new RuntimeException("Nepoznata stavka dokumenta!");
      short kolona = shema.getShort("CKOLONE"); 
      
      if (konto == null) {
        String txt = "";
        if (biz != null && biz.length() > 0)
          txt = "(" + biz + ") ";
        if (desc != null && desc.length() > 0) 
          txt = txt + " " + desc;
        txt = txt + " - " + por + "%";
        dlg.jlText.setText(txt);
        dlg.jlNext.setText(sk.getString("BROJDOK") + ", " + nazpar);
        dlg.open(null);
        if (!dlg.ok) return false;
        
        shema.insertRow(false);
        shema.setString("APP", "sk");
        shema.setString("VRDOK", "IRN");
        shema.setString("CSKL", cskl);
        shema.setString("CSKLUL", cskl);
        if (txt.length() > 40) txt = txt.substring(0, 40);
        shema.setString("OPIS", txt);
        shema.setShort("STAVKA", (short) Asql.getNextRbs("SHKONTA", "STAVKA",
            Condition.equal("VRDOK", "IRN"). and(Condition.equal("CSKL", cskl))));
        shema.setString("POLJE", Short.toString(shema.getShort("STAVKA"))); 
        shema.setShort("CKOLONE", dlg.fields.getShort("CKOLONE"));
        shema.setString("BROJKONTA", konto = dlg.fields.getString("BROJKONTA"));
        shema.setString("KARAKTERISTIKA", "P");
        String tags = "|" + por + "%|";
        if (biz != null && biz.length() > 0)
          tags = tags + " |#" + biz + "|";
        if (desc != null && desc.length() > 0)
          tags = tags + " |" + desc + "|";
        shema.setString("SQLCONDITION", tags);
        kolona = shema.getShort("CKOLONE");
        shema.saveChanges();
      }
      total = total.add(iznos);
      addUi(sk, ui, konto, kolona, iznos);
      
      if (!"0".equals(por)) {
        totalosn = totalosn.add(iznos);
        totalpor = totalosn.multiply(new BigDecimal(por)).
          movePointLeft(2).setScale(2, BigDecimal.ROUND_HALF_UP);
        
        //total = total.add(porez);
        //addUi(sk, ui, getKonto("17"), (short) 17, porez);
      }
    }
    if (totalpor.signum() != 0) {
      total = total.add(totalpor);
      addUi(sk, ui, getKonto("22"), (short) 22, totalpor);
    }
    
    if (sk.getBigDecimal("SALDO").compareTo(total) != 0)
      throw new RuntimeException("Kriva suma stavaka dokumenta " + 
          sk.getString("BROJDOK")); 
    
    int next = Valid.getValid().findSeqInt(seqKey = 
      OrgStr.getKNJCORG(false) + "IRA-" +
      ut.getYear(sk.getTimestamp("DATDOK")) +
      (bookDependant ? "-" + sk.getString("CKNJIGE") : ""), 
      false, false);

    String result = Integer.toString(next);
    if (result.length() < extSize) 
      result = Aus.string(extSize - result.length(), '0') + result;
    sk.setString("EXTBRDOK", result);
    
    if (!raTransaction.saveChangesInTransaction(
        new QueryDataSet[] {sk, ui, dm.getSeq()}))
      throw new RuntimeException("Raèun veæ ranije prebaèen: " + 
          sk.getString("BROJDOK"));
    
    return true;
  }
  
  void addUi(DataSet sk, DataSet ui, 
      String konto, short kolona, BigDecimal iznos) {
    
    int rbs = 1;
    boolean exist = false;
    for (ui.first(); ui.inBounds(); ui.next()) {
      if (!exist && ui.getString("BROJKONTA").equals(konto) &&
          ui.getShort("CKOLONE") == kolona) {
        Aus.add(ui, "IP", iznos);
        exist = true;
      }
      if (ui.getInt("RBS") > rbs) rbs = ui.getInt("RBS");
    }
    if (!exist) {
      ui.insertRow(false);
      ui.setString("URAIRA", "I");
      dM.copyColumns(sk, ui, uicol);
      ui.setShort("CKOLONE", kolona);
      ui.setString("BROJKONTA", konto);
      ui.setInt("RBS", rbs + 1);
      ui.setString("DUGPOT", "P");
      ui.setBigDecimal("IP", iznos);
      ui.setBigDecimal("ID", Aus.zero2);
      ui.post();
    }
  }
  
  void setDate(DataSet ds, String col, Element el, String child) {
    try {
      String temp = el.getChildText(child);
      ds.setTimestamp(col, new Timestamp(df2.parse(temp).getTime()));
      return;
    } catch (Exception e) {
      
    }
    try {
      String temp = el.getChildText(child);
      ds.setTimestamp(col, new Timestamp(df.parse(temp).getTime()));
    } catch (Exception e) {
      
    }
  }
  
  String getKonto(String kol) {
    if (!ld.raLocate(shema, "CKOLONE", kol))
      throw new RuntimeException("Nedefinirana kolona " + kol);
    return shema.getString("BROJKONTA");
  }
  
  String getKontoAll(String txt, String por) {
    txt = "|" + txt.toLowerCase() + "|";
    por = "|" + por.toLowerCase() + "|";
    for (shema.first(); shema.inBounds(); shema.next()) {
      String desc = shema.getString("SQLCONDITION").toLowerCase();
      if (desc.indexOf(txt) >= 0 && desc.indexOf(por) >= 0)
        return shema.getString("BROJKONTA");
    }
    return null;
  }
  
  public class dlgUraIra {
    /*public static final int IRA = 1;
    public static final int URA = 2;*/

    //dlgUraIra dlg = new dlgUraIra();
    JraDialog jd;

    JPanel main = new JPanel();
    JPanel center = new JPanel();
    XYLayout xy = new XYLayout();

    JLabel jlknj = new JLabel();
    JlrNavField konto = new JlrNavField();
    JlrNavField nazkonta = new JlrNavField();
    JraButton jbselknj = new JraButton();
    JLabel jlkol = new JLabel();
    JlrNavField ckolone = new JlrNavField();
    JlrNavField nazkolone = new JlrNavField();
    JraButton jbselkol = new JraButton();
    
    JLabel jlOpis = new JLabel();
    JLabel jlText = new JLabel();
    JLabel jlNext = new JLabel();

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
      jd.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      jd.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          jd.getContentPane().remove(main);
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
      if (fields.getShort("CKOLONE") == 0)
        fields.setAssignedNull("CKOLONE");

      //cknjige.forceFocLost();
      //ckolone.forceFocLost();
      ok = false;
      System.out.println("otvorio novi ekran");
      jd.pack();
      jd.setLocationRelativeTo(jd.getOwner());
      jd.show();
      if (ok) {
//        DataSet.copyTo(cols, fields, cols, owner.getRaQueryDataSet());
//        owner.getRaQueryDataSet().setString("CKNJIGE", fields.getString("CKNJIGE"));
//        if (fields.isuna
//        owner.getRaQueryDataSet().setShort("CKOLONE", fields.getShort("CKOLONE"));
        //dM.copyColumns(fields, owner.getRaQueryDataSet(), cols);
      }
    }


    public void setUI() {
      konto.setRaDataSet(dM.getDataModule().getKonta());
      ckolone.setRaDataSet(dM.getDataModule().getIzlazneKolone());
    }

    private void OKPress() {
      if (vl.isEmpty(konto) || vl.isEmpty(ckolone)) return;
      
      ok = true;
      jd.getContentPane().remove(main);
      jd.dispose();
    }

    private void cancelPress() {
      jd.getContentPane().remove(main);
      jd.dispose();
    }

    private void jbInit() throws Exception {
      center.setLayout(xy);
      xy.setWidth(525);
      xy.setHeight(150);

      fields.setColumns(new Column[] {
        dM.createStringColumn("BROJKONTA", "Konto", 12),
        dM.createStringColumn("NAZIVKONTA", "Naziv konta", 50),
        dM.createShortColumn("CKOLONE", "Broj kolone"),
        dM.createStringColumn("NAZIVKOLONE", "Naziv kolone", 50)
      });
      fields.open();
      
      jlOpis.setText("Stavka raèuna");

      jlknj.setText("Konto");
      konto.setColumnName("BROJKONTA");
      konto.setDataSet(fields);
      konto.setColNames(new String[] {"NAZIVKONTA"});
      konto.setTextFields(new javax.swing.text.JTextComponent[] {nazkonta});
      konto.setVisCols(new int[] {0,4});
      konto.setSearchMode(3);
      konto.setRaDataSet(dM.getDataModule().getKnjigeUI());
      konto.setNavButton(jbselknj);

      jlkol.setText("Kolona knjige");
      nazkonta.setNavProperties(konto);
      nazkonta.setDataSet(fields);
      nazkonta.setColumnName("NAZIVKONTA");
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

      center.add(jlOpis, new XYConstraints(15, 20, -1, -1));
      center.add(jlText, new XYConstraints(150, 20, -1, -1));
      center.add(jlNext, new XYConstraints(150, 45, -1, -1));
      
      center.add(jlknj, new XYConstraints(15, 80, -1, -1));
      center.add(konto, new XYConstraints(150, 80, 75, -1));
      center.add(nazkonta, new XYConstraints(230, 80, 250, -1));
      center.add(jbselknj, new XYConstraints(485, 80, 21, 21));

      center.add(jlkol, new XYConstraints(15, 105, -1, -1));
      center.add(ckolone, new XYConstraints(150, 105, 75, -1));
      center.add(nazkolone, new XYConstraints(230, 105, 250, -1));
      center.add(jbselkol, new XYConstraints(485, 105, 21, 21));

      main.setLayout(new BorderLayout());
      main.add(center, BorderLayout.CENTER);
      main.add(okp, BorderLayout.SOUTH);
      
      setUI();
    }
  }
}
