package hr.restart.sk;

import hr.restart.baza.kreator;
import hr.restart.util.IntParam;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class raImportRac {
  
  public static void show(Frame parent) {
    kreator.SelectPathDialog spd = new kreator.SelectPathDialog(
        parent, "Putanja za import raèuna");
    String dir = IntParam.getTag("importxml.dir");
    spd.loadsave = (dir == null || dir.length() == 0) ? null : new File(dir);
    spd.show();
    if (spd.oksel) {
      raImportRac ir = new raImportRac();
      IntParam.setTag("importxml.dir", spd.loadsave.getAbsolutePath());
      File[] list = spd.loadsave.listFiles();
      for (int i = 0; i < list.length; i++)
        if (list[i].getName().toLowerCase().endsWith(".xml"))
          ir.importSingle(list[i]);
    }
  }
  
  void importSingle(File f) {
    try { 
      Document doc = new SAXBuilder().build(f);
      createRac(doc);
      //f.delete();
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
    return true;
    
  }
}
