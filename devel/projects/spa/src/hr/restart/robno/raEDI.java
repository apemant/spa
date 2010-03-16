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


public class raEDI {
  
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
  	
  	Element root = doc.getRootElement();
    if (!root.getName().equals("Order")) 
      throw new RuntimeException("Pogrešan format datoteke: '"+root.getName()+"'");
    
//    List docs = root.getChildren("Order");
//    System.out.println("loop started");
//    for (Iterator n = docs.iterator(); n.hasNext(); ) {
      Element nar = root;//(Element) n.next();
      System.out.println("nar: " + nar);

      Element party = nar.getChild("OrderParty");
      Element buyer = party.getChild("BuyerParty");
      
      Element ship = party.getChild("ShipToParty");
      String pj = ship.getChildText("SellerShipToID");
      
      Element head = nar.getChild("OrderHeader");
      
      QueryDataSet zag = doki.getDataModule().getTempSet(Condition.nil);
      QueryDataSet st = stdoki.getDataModule().getTempSet(Condition.nil);
      
      zag.open();
      st.open();
      zag.insertRow(false);
      zag.setString("CSKL", oj);
      zag.setString("VRDOK", "NKU");
      zag.setInt("CPAR", Integer.parseInt(gsif));
      zag.setTimestamp("DATDOK", getTimestamp(head, "OrderIssueDate", null));
      zag.setTimestamp("DATDOSP", getTimestamp(head, "RequestedDeliverDate", null));
      
      zag.setString("GOD", hr.restart.util.Util.getUtil().
          getYear(zag.getTimestamp("DATDOK")));
      if (pj != null && pj.length() > 0) {
        try {
          zag.setInt("PJ", Integer.parseInt(pj));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      
      String vri = head.getChildText("RequestedDeliverByTime");
      if (vri != null) {
        zag.setString("OPIS", "VRIJEME ISPORUKE: " +
            vri.substring(0, 2) + ":" +
            vri.substring(2, 4));
      }
      
      String[] acc = {"CART", "CART1", "BC", "NAZART", "JM"};
      
      short rbr = 0;
      for (Iterator a = nar.getChild("OrderDetail").getChildren("Item").iterator(); a.hasNext(); ) {
      	Element da = (Element) a.next();
      	
      	st.insertRow(false);
        dM.copyColumns(zag, st, Util.mkey);
        st.setShort("RBR", ++rbr);
        st.setInt("RBSID", rbr);
        
        String kol = da.getChildText("QuantityValue");
        st.setBigDecimal("KOL", Aus.getDecNumber(kol));
        
        String cart = da.getChildText("BuyerItemID");
        if (cart != null && ld.raLocate(dM.getDataModule().getArtikli(), "CART", cart)) {
        	dM.copyColumns(dM.getDataModule().getArtikli(), st, acc);
          st.post();
        } else
          throw new RuntimeException("Nepoznata šifra artikla!");
        
      }
      
      saveOrder(zag, st);
//    }
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
    //2010-03-02T09:25:45
    cal.set(cal.YEAR, Integer.parseInt(sd.substring(0, 4)));
//    cal.set(cal.MONTH, Integer.parseInt(sd.substring(4, 6)) - 1);
    cal.set(cal.MONTH, Integer.parseInt(sd.substring(5, 7)) - 1);
//    cal.set(cal.DAY_OF_MONTH, Integer.parseInt(sd.substring(6, 8)));
    cal.set(cal.DAY_OF_MONTH, Integer.parseInt(sd.substring(8, 10)));
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
