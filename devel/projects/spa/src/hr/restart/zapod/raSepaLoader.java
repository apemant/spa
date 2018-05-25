package hr.restart.zapod;

import hr.restart.baza.Condition;
import hr.restart.baza.Orgstruktura;
import hr.restart.baza.Virmani;
import hr.restart.pl.frmVirmaniPlArh;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raFileFilter;
import hr.restart.util.raProcess;
import hr.restart.util.startFrame;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import com.borland.dx.sql.dataset.QueryDataSet;


public class raSepaLoader {

  static JFileChooser ch = new JFileChooser(".");
  static {
    ch.addChoosableFileFilter(new raFileFilter("SEPA pain.001 datoteke (*.xml)"));
    ch.setFileFilter(ch.getChoosableFileFilters()[1]);
  }
  
  private raSepaLoader() {
  }
  
  public static void load() {
    if (ch.showOpenDialog(startFrame.getStartFrame()) != JFileChooser.APPROVE_OPTION) return;
    if (ch.getSelectedFile() != null) load(ch.getSelectedFile());
  }
  
  public static void load(final File f) {
    if (f== null || !f.exists()) {
      JOptionPane.showMessageDialog(null, "Pogrešna datoteka!", "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    raProcess.runChild(new Runnable() {
      public void run() {
        raProcess.setMessage("Uèitavanje xml datoteke...", false);
        SAXBuilder builder = new SAXBuilder();
        try {
          Document doc = builder.build(f);
          raProcess.yield(doc);
        } catch (JDOMException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
        raProcess.fail();
      }
    });
    
    if (raProcess.isInterrupted()) return;
    if (raProcess.isFailed())
      JOptionPane.showMessageDialog(null, "Došlo je do greške kod uèitavanja xml datoteke!", "Greška", JOptionPane.ERROR_MESSAGE);
    
    load((Document) raProcess.getReturnValue());
    
  }
  
  static void load(Document doc) {
    Element root = doc.getRootElement();
    Namespace ns = root.getNamespace();
    System.out.println(root);
    if (root == null || !root.getName().equals("Document")) {
      JOptionPane.showMessageDialog(null, "Pogrešna xml datoteka!", "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    Element base = root.getChild("CstmrCdtTrfInitn", ns);
    System.out.println(base);
    if (base == null) {
      JOptionPane.showMessageDialog(null, "Pogrešna xml datoteka!", "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    int rbr = 0;
    String knjig = OrgStr.getKNJCORG(false);
    String mjesto = Orgstruktura.getDataModule().openTempSet(Condition.equal("CORG",knjig)).getString("MJESTO");
    QueryDataSet ds = Virmani.getDataModule().openEmptySet();
    String key = null;
    
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    for (Iterator i = base.getChildren("PmtInf", ns).iterator(); i.hasNext(); ) {
      Element pi = (Element) i.next();
      Timestamp dan = Valid.getValid().getToday();
      try {
        dan = new Timestamp(df.parse(pi.getChildText("ReqdExctnDt", ns)).getTime());
      } catch (ParseException e) {
        e.printStackTrace();
      }
      if (key == null) {
        key = knjig + "-" + Util.getUtil().getYear(dan) + "-" + Util.getUtil().getMonth(dan) + "-1";
      }
      Element debtor = pi.getChild("Dbtr", ns);
      String nateret = debtor.getChildText("Nm", ns) + "\n";
      Element adr = debtor.getChild("PstlAdr", ns);
      for (Iterator a = adr.getChildren("AdrLine", ns).iterator(); a.hasNext(); )
        nateret = nateret + ((Element) a.next()).getTextTrim() + " ";
      nateret = nateret.trim();
      String brracnt = pi.getChild("DbtrAcct", ns).getChild("Id", ns).getChildText("IBAN", ns);
      
      for (Iterator v = pi.getChildren("CdtTrfTxInf", ns).iterator(); v.hasNext(); ) {
        Element tx = (Element) v.next();
        String pnbz = tx.getChild("PmtId", ns).getChildText("EndToEndId", ns);
        BigDecimal iznos = new BigDecimal(tx.getChild("Amt", ns).getChildText("InstdAmt", ns));
        String ukorist = tx.getChild("Cdtr", ns).getChildText("Nm", ns);
        String brracuk = tx.getChild("CdtrAcct", ns).getChild("Id", ns).getChildText("IBAN", ns);
        Element dest = tx.getChild("RmtInf", ns).getChild("Strd", ns);
        String pnbo = dest.getChild("CdtrRefInf", ns).getChildText("Ref", ns);
        String svrha = dest.getChildText("AddtlRmtInf", ns);
        
        // utakanje
        ds.insertRow(false);
        ds.setString("APP", "pl");
        ds.setString("KNJIG", knjig);
        ds.setString("CKEY", key);
        ds.setShort("RBR", (short) ++rbr);
        ds.setString("JEDZAV", "NNDNN");
        ds.setString("NATERET", nateret);
        ds.setString("SVRHA", svrha);
        ds.setString("UKORIST", ukorist);
        ds.setString("BRRACNT", brracnt);
        if (pnbz.startsWith("HR") && pnbz.length() >= 4) {
          ds.setString("PNBZ1", pnbz.substring(2, 4));
          ds.setString("PNBZ2", pnbz.substring(4));
        }
        ds.setString("SIF1", "08");
        ds.setString("BRRACUK", brracuk);
        if (pnbo.startsWith("HR") && pnbo.length() >= 4) {
          ds.setString("PNBO1", pnbo.substring(2, 4));
          ds.setString("PNBO2", pnbo.substring(4));
        }
        ds.setBigDecimal("IZNOS", iznos);
        ds.setString("MJESTO", mjesto);
        ds.setTimestamp("DATUMIZV", dan);
        ds.setTimestamp("DATUMPR", dan);
        ds.post();
      }
      
    }
    ds.saveChanges();
    
    frmVirmaniPlArh virs = (frmVirmaniPlArh) startFrame.getStartFrame().showFrame("hr.restart.pl.frmVirmaniPlArh",15, "SEPA virmani", false);
    virs.ckey = key;
    virs.show();
  }
}
