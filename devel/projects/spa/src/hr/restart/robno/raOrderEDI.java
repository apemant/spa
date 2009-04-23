package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.Pjpar;
import hr.restart.baza.VTCartPart;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


public class raOrderEDI {
  
  static DataRow last;
  
  public static void createOrder(String oj, File xml) { 
    try {
      Document doc = new SAXBuilder().build(xml);
      createOrder(oj, doc);
      xml.delete();
      Util.getUtil().showDocs(last.getString("CSKL"), 
          "", "NKU", last.getInt("BRDOK"), last.getString("GOD"));
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
  
  private static void createOrder(String oj, Document doc) {
    
    lookupData ld = lookupData.getlookupData();
    
    String gsif = frmParam.getParam("robno", "sifGetro", 
        "", "Šifra dobavljaèa za Getro");
    
    DataSet arts = VTCartPart.getDataModule().getTempSet(
        Condition.equal("CPAR", Integer.parseInt(gsif)));
    arts.open();
    
    DataSet pjg = Pjpar.getDataModule().getTempSet(
        Condition.equal("CPAR", Integer.parseInt(gsif)));
    pjg.open();
    
    System.out.println("arts opened");
    
    Element root = doc.getRootElement();
    if (!root.getName().equals("ORDERS05")) 
      throw new RuntimeException("Pogrešan format datoteke!");
    
    List docs = root.getChildren("IDOC");
    System.out.println("loop started");
    for (Iterator n = docs.iterator(); n.hasNext(); ) {
      Element nar = (Element) n.next();
      System.out.println("nar: " + nar);
      
      QueryDataSet zag = doki.getDataModule().getTempSet(Condition.nil);
      QueryDataSet st = stdoki.getDataModule().getTempSet(Condition.nil);
      
      zag.open();
      st.open();
      zag.insertRow(false);
      zag.setString("CSKL", oj);
      zag.setString("VRDOK", "NKU");
      zag.setInt("CPAR", Integer.parseInt(gsif));
      
      for (Iterator i = nar.getChildren("E1EDK03").iterator(); i.hasNext(); ) {
        Element di = (Element) i.next();
        String iddat = di.getChildText("IDDAT");
        if ("022".equals(iddat))
          zag.setTimestamp("DATDOK", getTimestamp(di, "DATUM", "UZEIT"));
        else if ("002".equals(iddat))
          zag.setTimestamp("DATDOSP", getTimestamp(di, "DATUM", "UZEIT"));
      }
      zag.setString("GOD", hr.restart.util.Util.getUtil().
          getYear(zag.getTimestamp("DATDOK")));
      
      String pj = null;
      String ramp = null;
      for (Iterator i = nar.getChildren("E1EDKA1").iterator(); i.hasNext(); ) {
        Element di = (Element) i.next();
        if ("WE".equals(di.getChildText("PARVW"))) {
          pj = di.getChildText("LIFNR");
          ramp = di.getChildText("ABLAD");
        }
      }
      
      if (pj != null && ld.raLocate(pjg, "PJ", pj))
        zag.setInt("PJ", Integer.parseInt(pj));
      
      for (Iterator i = nar.getChildren("E1EDK02").iterator(); i.hasNext(); ) {
        Element di = (Element) i.next();
        if ("001".equals(di.getChildText("QUALF")))
          zag.setString("BRNARIZ", di.getChildText("BELNR"));
      }
      
      String vri = null;
      for (Iterator i = nar.getChildren("E1EDK03").iterator(); i.hasNext(); ) {
        Element di = (Element) i.next();
        if ("002".equals(di.getChildText("IDDAT")))
          vri = di.getChildText("UZEIT");
      }
      
      if (vri != null && ramp != null) {
        zag.setString("OPIS", "VRIJEME ISPORUKE: " +
            vri.substring(0, 2) + ":" +
            vri.substring(2, 4) + ", RAMPA " + ramp);
      }
      
      
      String[] acc = {"CART", "CART1", "BC", "NAZART", "JM"};
      
      short rbr = 0;
      for (Iterator a = nar.getChildren("E1EDP01").iterator(); a.hasNext(); ) {
        Element da = (Element) a.next();
        
        st.insertRow(false);
        dM.copyColumns(zag, st, Util.mkey);
        st.setShort("RBR", ++rbr);
        st.setInt("RBSID", rbr);
        
        String kol = da.getChildText("BMNG2");
        st.setBigDecimal("KOL", Aus.getDecNumber(kol));
        
        String cart = null, ean = null;
        for (Iterator i = da.getChildren("E1EDP19").iterator(); i.hasNext(); ) {
          Element di = (Element) i.next();
          if ("002".equals(di.getChildText("QUALF")))
            cart = di.getChildText("IDTNR");
          else if ("003".equals(di.getChildText("QUALF")))
            ean = di.getChildText("IDTNR");
        }
        if (cart != null && ld.raLocate(arts, "CCPAR", cart) ||
            ean != null && ld.raLocate(arts, "BCPAR", ean)) {
          
          ld.raLocate(dM.getDataModule().getArtikli(), "CART",
              Integer.toString(arts.getInt("CART")));
          
          dM.copyColumns(dM.getDataModule().getArtikli(), st, acc);
          st.post();
        } else
          throw new RuntimeException("Nepoznata šifra artikla!");
      }
      
      saveOrder(zag, st);
    }
  }
  
  private static void saveOrder(final QueryDataSet zag, final QueryDataSet st) {
    if (!new raLocalTransaction() {
      public boolean transaction() throws Exception {
        Util.getUtil().getBrojDokumenta(zag);
        for (st.first(); st.inBounds(); st.next())
          st.setInt("BRDOK", zag.getInt("BRDOK"));
        
        raTransaction.saveChanges(zag);
        raTransaction.saveChanges(st);
        
        last = new DataRow(zag, Util.mkey);
        dM.copyColumns(zag, last, Util.mkey);
        
        return true;
      }
    }.execTransaction()) {
      JOptionPane.showMessageDialog(null, "Greška kod snimanja narudžbe!",
          "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  private static Timestamp getTimestamp(Element parent, 
      String date, String time) {
    System.out.println(parent);
    String sd = parent.getChildText(date);
    String st = parent.getChildText(time);
    Calendar cal = Calendar.getInstance();
    cal.set(cal.YEAR, Integer.parseInt(sd.substring(0, 4)));
    cal.set(cal.MONTH, Integer.parseInt(sd.substring(4, 6)) - 1);
    cal.set(cal.DAY_OF_MONTH, Integer.parseInt(sd.substring(6, 8)));
    if (time != null) {
      cal.set(cal.HOUR_OF_DAY, Integer.parseInt(st.substring(0, 2)));
      cal.set(cal.MINUTE, Integer.parseInt(st.substring(2, 4)));
      cal.set(cal.SECOND, Integer.parseInt(st.substring(4, 6)));
    } else {
      cal.set(cal.HOUR_OF_DAY, 0);
      cal.set(cal.MINUTE, 0);
      cal.set(cal.SECOND, 0);
    }
    cal.set(cal.MILLISECOND, 0);
    return new Timestamp(cal.getTime().getTime());
  }
}
