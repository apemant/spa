package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.util.Aus;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.borland.dx.sql.dataset.QueryDataSet;


public class raOrderEDI {
  public static void main(String[] args) {

    try {
      Document doc =
      new SAXBuilder().build(
          new File("/home/abf/projekti/devel/build/build.xml"));
      new XMLOutputter(Format.getPrettyFormat()).output(doc, System.out);
      
    } catch (JDOMException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static void createOrder(String oj, File xml) { 
    try {
      Document doc = new SAXBuilder().build(xml);
      
    } catch (JDOMException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  private static void createOrder(String oj, Document doc) {
    
    
    Element root = doc.getRootElement();
    if (!root.getName().equals("ORDERS05")) 
      throw new RuntimeException("Pogrešan format datoteke!");
    
    List docs = root.getChildren("IDOC");
    for (Iterator i = docs.iterator(); i.hasNext(); ) {
      Element nar = (Element) i.next();
      
      QueryDataSet zag = doki.getDataModule().getTempSet(Condition.none);
      QueryDataSet st = stdoki.getDataModule().getTempSet(Condition.none);
      
      zag.insertRow(false);
      zag.setString("CSKL", oj);
      zag.setString("VRDOK", "NKU");
      zag.setTimestamp("DATDOK", getTimestamp(
          nar.getChild("EDI_DC40"), "CREDAT", "CRETIM"));
      zag.setString("GOD", hr.restart.util.Util.getUtil().
          getYear(zag.getTimestamp("DATDOK")));
      
      
      
    }
    
  }
  
  private static Timestamp getTimestamp(Element parent, 
      String date, String time) {
    String sd = parent.getChildText(date);
    String st = parent.getChildText(time);
    Calendar cal = Calendar.getInstance();
    cal.set(cal.YEAR, Integer.parseInt(sd.substring(0, 4)));
    cal.set(cal.MONTH, Integer.parseInt(sd.substring(4, 6)) - 1);
    cal.set(cal.DAY_OF_MONTH, Integer.parseInt(sd.substring(6, 8)));
    cal.set(cal.HOUR_OF_DAY, Integer.parseInt(st.substring(0, 2)));
    cal.set(cal.MINUTE, Integer.parseInt(st.substring(2, 4)));
    cal.set(cal.SECOND, Integer.parseInt(st.substring(4, 6)));
    cal.set(cal.MILLISECOND, 0);
    return new Timestamp(cal.getTime().getTime());
  }
}
