package hr.restart.util.mail;

import hr.restart.baza.Orgstruktura;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.DesEncrypter;
import hr.restart.util.OKpanel;
import hr.restart.util.lookupData;
import hr.restart.util.raDbaseCreator;
import hr.restart.util.raFrame;
import hr.restart.zapod.OrgStr;

import java.awt.Frame;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYLayout;

public class DataBaseMailer extends DataMailer {

  public File getAttachment() {
    File dir = new File(System.getProperty("user.dir")+File.separator+"dumps");
    if (dir.exists() && !dir.isDirectory()) dir.delete();
    if (!dir.exists()) dir.mkdir();

    QueryDataSet knjs = Orgstruktura.getDataModule().getKnjig();
    knjs.open();
    lookupData.getlookupData().raLocate(knjs, "CORG", OrgStr.getKNJCORG());

    String prefix = getMailProperties().getProperty("dump.name", knjs.getString("NAZIV"));
    String dumpFileName = dir.getAbsolutePath()+File.separator+prefix+"-dbdump-"+new java.sql.Date(System.currentTimeMillis())+".zi_";
    File plainDumpFile = new File(dumpFileName+"__plain.tmp");
    File dumpFile = new File(dumpFileName);
    raDbaseCreator.dumpTo(plainDumpFile);
    DesEncrypter enc = new DesEncrypter(getFrom());
    enc.encrypt(plainDumpFile, dumpFile);
    return dumpFile;
  }
  /**
   * Ekran preko kojeg se postavlja:
   * frmParam.getParam("sisfun","datamailfrom","test@from.hr", "Sa koje e-mail adrese se šalju podaci");
   * frmParam.getParam("sisfun","datamailrec","testiranje@rest-art.hr", "Na koju e-mail adresu se šalju podaci e-mailom");
   * getMailProperties().getProperty("mailhost");
   * getMailProperties().getProperty("dump.name");
   * 
   */
  public static void showProperties() {
    JraDialog props = new JraDialog((Frame)null, true);
    JPanel panel = new JPanel();
    JLabel jlname = new JLabel("Naziv baze");
    JTextField jtname = new JTextField(getMailProperties().getProperty("dump.name"));
    JLabel jlfrom = new JLabel("Šalje");
    JTextField jtfrom = new JTextField(frmParam.getParam("sisfun","datamailfrom","test@from.hr", "Sa koje e-mail adrese se šalju podaci"));
    JLabel jlto = new JLabel("Poslati na");
    JTextField jtto = new JTextField(frmParam.getParam("sisfun","datamailrec","testiranje@rest-art.hr", "Na koju e-mail adresu se šalju podaci e-mailom"));
    JLabel jlhost = new JLabel("Server");
    JTextField jthost = new JTextField(getMailProperties().getProperty("mailhost"));
    JraButton jbunlock = new JraButton();
    OKpanel okp = new OKpanel() {
      public void jBOK_actionPerformed() {
        
      }
      public void jPrekid_actionPerformed() {
      }
    };
    XYLayout layout = new XYLayout();
    layout.setHeight(180);
    layout.setWidth(360);
    panel.setLayout(layout);
    
  }
  
  public String getSubject() {
    return "Podaci "+getMailProperties().getProperty("dump.name")+" od "+new java.sql.Date(System.currentTimeMillis());
  }
}
