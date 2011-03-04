package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.Shkonta;
import hr.restart.baza.Skstavke;
import hr.restart.baza.UIstavke;
import hr.restart.baza.dM;
import hr.restart.baza.kreator;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.IntParam;
import hr.restart.util.lookupData;
import hr.restart.zapod.OrgStr;

import java.awt.Frame;
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

import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


public class raImportRac {
  
  DateFormat df = new SimpleDateFormat("d.M.yyyy. H:m:s");
  lookupData ld = lookupData.getlookupData();
  dM dm = dM.getDataModule();
  String cskl, cknjige;
  DataSet shema;
  Map kols;
  String[] uicol = {"KNJIG", "CPAR", "VRDOK", "BROJDOK", 
                   "CORG", "CKNJIGE", "CSKSTAVKE"};
  
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
      if (!kols.containsKey(tip)) continue;
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
      }
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
}
