package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;

import java.io.File;
import java.io.IOException;

import org.jdom.Document;
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
    QueryDataSet zag = doki.getDataModule().getTempSet(Condition.none);
    QueryDataSet st = stdoki.getDataModule().getTempSet(Condition.none);
    
    zag.insertRow(false);
    zag.setString("CSKL", oj);
    zag.setString("VRDOK", "NKU");
    
    
    
  }
}
