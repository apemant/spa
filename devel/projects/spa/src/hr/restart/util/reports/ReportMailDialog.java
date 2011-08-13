package hr.restart.util.reports;

import hr.restart.baza.Partneri;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextArea;
import hr.restart.swing.JraTextField;
import hr.restart.util.FileHandler;
import hr.restart.util.JlrNavField;
import hr.restart.util.OKpanel;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.propsDialog;
import hr.restart.util.startFrame;
import hr.restart.util.mail.Mailer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class ReportMailDialog extends JraDialog {
  StorageDataSet retValue;
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      okPress();
    };
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };
  JlrNavField jrEMADR = new JlrNavField();
  JLabel jlKome = new JLabel("Kome");
  JPanel jpEMADR = new JPanel(new BorderLayout());
  
  JraButton jbGet = new JraButton();
  JPanel jpGet = new JPanel();
  
  JraTextField jtNaslov = new JraTextField();
  JLabel jlNaslov = new JLabel("Naslov");
  JraTextArea jtaText = new JraTextArea();
  JPanel jpNaslov = new JPanel(new BorderLayout());
  JPanel jpTxtNaslov = new JPanel(new BorderLayout());
  
  JraButton jbOptions = new JraButton();
  
  boolean valid = true;
  
  protected ReportMailDialog() {
    setModal(true);
    retValue = new StorageDataSet();
    retValue.addColumn(dM.createStringColumn("EMADR","e-Mail adresa", 60));
    retValue.addColumn(dM.createStringColumn("NASLOV","Naslov", 100));
    retValue.addColumn(dM.createStringColumn("TXT","Tekst poruke", 500));
    retValue.open();
    retValue.insertRow(false);
    
    jrEMADR.setDataSet(retValue);
    jrEMADR.setColumnName("EMADR");
    jrEMADR.setNavColumnName("EMADR");
    jrEMADR.setColNames(new String[] {"EMADR"});
    jrEMADR.setVisCols(new int[] {0,1,2,3});
    jrEMADR.setNavProperties(null);
    jrEMADR.setRaDataSet(getEmajlSet());
    jrEMADR.setSearchMode(1);
    jrEMADR.setNavButton(jbGet);
    jrEMADR.setHandleError(false);
    jpEMADR.add(jlKome, BorderLayout.WEST);
    jpEMADR.add(jrEMADR, BorderLayout.CENTER);
    
    jtNaslov.setColumnName("NASLOV");
    jtNaslov.setDataSet(retValue);

    setSize(jlKome, 60, 21);
    setSize(jlNaslov, 60, 21);
    
    jtaText.setColumnName("TXT");
    jtaText.setDataSet(retValue);
    jpNaslov.add(jlNaslov, BorderLayout.WEST);
    jpNaslov.add(jtNaslov, BorderLayout.CENTER);
    jpNaslov.add(getSpace(50,5), BorderLayout.SOUTH);
    setSize(jpNaslov,400, 26);
    jpTxtNaslov.add(jpNaslov, BorderLayout.NORTH);
    jpTxtNaslov.add(new JScrollPane(jtaText), BorderLayout.CENTER);
    jpTxtNaslov.add(getSpace(50, 5),BorderLayout.SOUTH);
    setSize(jpTxtNaslov, 400, 200);
    jpGet.setLayout(new BorderLayout());
    setSize(jbGet,21,21);
    jpGet.add(jbGet, BorderLayout.EAST);
    
    setSize(jpGet,26,26);
    jpEMADR.add(jpGet,BorderLayout.EAST);
    jpEMADR.add(getSpace(50,5), BorderLayout.SOUTH);
    setSize(jpEMADR,400, 26);
    JPanel content = new JPanel(new BorderLayout());
    content.setLayout(new BorderLayout());
    content.add(jpEMADR, BorderLayout.NORTH);
    content.add(jpTxtNaslov,BorderLayout.CENTER);
    content.add(okp, BorderLayout.SOUTH);
    content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(content, BorderLayout.CENTER);
    okp.registerOKPanelKeys(this);
    
    jbOptions.setText("Postavke");
    jbOptions.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showMailOptions();
      }
    });
    okp.add(jbOptions,BorderLayout.WEST);
  }
  
  private static boolean succ = false;
  public static boolean showMailOptions() {
    final Properties props = new Properties();
    final Properties allprops = Mailer.getMailProperties();
    final String[] keys = new String[] {"mailhost","mailfrom"};
    for (int i = 0; i < keys.length; i++) props.setProperty(keys[i], allprops.getProperty(keys[i]));
    propsDialog dmo = new propsDialog(props,"Postavke e-maila") {
      {
        succ = false;
      }
      public void action_jBOK() {
        for (int i = 0; i < keys.length; i++) allprops.setProperty(keys[i], props.getProperty(keys[i]));
        FileHandler.storeProperties("mail.properties", allprops);
        succ = true;
        setVisible(false);
        dispose();
      }
      public void action_jPrekid() {
        succ = false;
        setVisible(false);
        dispose();
      }
    };
    dmo.setModal(true);
    startFrame.getStartFrame().centerFrame((JDialog)dmo,0,"Postavke e-maila");
    dmo.setVisible(true);
    return succ;
  }

  private JComponent getSpace(int w, int h) {
    JPanel space = new JPanel();
    setSize(space,w,h);
    return space;
  }

  private static void setSize(JComponent jp,int w,int h) {
    jp.setSize(new Dimension(w,h));
    jp.setMinimumSize(new Dimension(w,h));
    jp.setPreferredSize(new Dimension(w,h));
  }
  
  public static DataSet getEmajlSet() {
    try {
      String qry = 
                "SELECT partneri.CPAR, partneri.NAZPAR, partneri.EMADR, partneri.KO " +
                "FROM partneri " +
                "WHERE AKTIV != 'N' and partneri.EMADR!=''" +
      		"UNION " +
      		  "SELECT partneri.CPAR, partneri.NAZPAR, kosobe.EMAIL as EMADR, kosobe.IME as KO " +
      		  "FROM partneri,kosobe " +
      		  "WHERE partneri.cpar = kosobe.cpar and partneri.AKTIV !='N' and kosobe.EMAIL != '' and kosobe.EMAIL!=partneri.EMADR";
      QueryDataSet mejlovi = Util.getNewQueryDataSet(qry, false);
      mejlovi.setColumns(new Column[] {
          Partneri.getDataModule().getColumn("CPAR").cloneColumn(),
          Partneri.getDataModule().getColumn("NAZPAR").cloneColumn(),
          Partneri.getDataModule().getColumn("EMADR").cloneColumn(),
          Partneri.getDataModule().getColumn("KO").cloneColumn()
      });
      System.out.println(qry);
      mejlovi.open();
      return mejlovi;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  protected void cancelPress() {
    retValue = null;
    this.dispose();
  }

  protected void okPress() {
    if (validation()) { 
      retValue.post();
      this.dispose();
    }
  }

  private boolean validation() {
    if (valid && Valid.getValid().isEmpty(jrEMADR)) return false;
    if (valid && Valid.getValid().isEmpty(jtNaslov)) return false;
    if (Mailer.getMailProperties().getProperty("mailfrom").equals("errors@rest-art.hr")) {
      if (!showMailOptions()) return false;
    }
    return true;
  }

  public static StorageDataSet showMailDialog() {
    return showMailDialog(null,null,null);
  }
  public static StorageDataSet showMailDialog(boolean check) {
    return showMailDialog(null,null,null,check);
  }
  public static StorageDataSet showMailDialog(String kome, String naslov, String txt) {
    return showMailDialog(kome, naslov, txt, true);
  }
  public static StorageDataSet showMailDialog(String kome, String naslov, String txt, boolean check) {
    ReportMailDialog rmd = new ReportMailDialog();
    if (kome!=null) rmd.retValue.setString("EMADR", kome);
    if (naslov!=null) rmd.retValue.setString("NASLOV", naslov);
    if (txt!=null) rmd.retValue.setString("TXT", txt);
//    rmd.pack();
    rmd.valid = check;
    startFrame.getStartFrame().centerFrame((JDialog)rmd,0,"Slanje e-mailom");
    rmd.setVisible(true);
    return rmd.retValue;
  }

  public static boolean sendMail(final File attachment, final StorageDataSet values) {
    VarStr title = new VarStr(values.getString("NASLOV"));
    String hr = "ÈÆŠŽèæšž";
    String eng = "CCSZccsz";
    for (int i = 0; i < hr.length(); i++)
      title.replaceAll(hr.charAt(i), eng.charAt(i));
    title.replaceAll("Ð", "DJ");
    title.replaceAll("ð", "dj");
    values.setString("NASLOV", title.toString());
    
      Mailer m = new Mailer() {

        public File getAttachment() {
          return attachment;
        }

        public String getFrom() {
          return Mailer.getMailProperties().getProperty("mailfrom");
        }

        public String getSubject() {
          return values.getString("NASLOV");
        }
        
      };
      m.setRecipient(values.getString("EMADR"));
      return m.sendMailUI(values.getString("TXT"), m.getAttachment(), true, m.getSubject());
  }
}
