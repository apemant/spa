package hr.restart.rn;

import hr.restart.baza.dM;
import hr.restart.robno.raDateUtil;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraDialog;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.startFrame;
import hr.restart.util.mail.Mailer;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class ServisMailer extends Mailer implements ActionListener {
  private StorageDataSet masterSet;
  private static final String txtSendAll = "Pošalji svima";
  private static final String txtCancel = "Prekid";
  private static final String txtSendO = "Odaberi primaoce";
  private static final String txtSend1 = "Pošalji";
  static ServisMailer smailer = null; 
  String servisertbl = frmParam.getParam("rn","serviserTbl","telemark","Naziv tablice u kojoj su serviseri");
  protected ServisMailer() {
    
  }
  public static ServisMailer getInstance() {
    if (smailer == null) smailer = new ServisMailer();
    return smailer;
  }
  private String[] recps = null;
  public static void sendMailRN(QueryDataSet ds) {
    sendMailRN(ds, false);
  }
  public static void sendMailRN(QueryDataSet ds, boolean force) {
    if (!(force || frmParam.getParam("rn", "autoSendMailRN", "N", "Slati automatski e-mail nakon promjene statusa RN").equalsIgnoreCase("D"))) {
      return;
    }
    getInstance().masterSet = ds;
    getInstance().askConfirmation(ds.getInt("CKUPAC"));
    if (getInstance().recps == null) return;
    getInstance().setRecipients(getInstance().recps);
    getInstance().sendMailUI(getInstance().getMessage(), getInstance().getAttachment(), true, getInstance().getSubject());
  }
  private void askConfirmation(int cpar) {
    String s1m = getServiser(cpar);
    JraDialog dlgConfirm = new JraDialog();
    dlgConfirm.setModal(true);
    dlgConfirm.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    JButton jbSendAll = new JButton(txtSendAll);
    JButton jbSend1 = new JButton();
    
    if (s1m!=null) {
      recps = new String[] {s1m};
      jbSend1.setText(txtSend1+": "+VarStr.join(recps, ','));
    } else {
      recps = null;
      jbSend1.setText(txtSend1);
      jbSend1.setEnabled(false);
    }
    JButton jbSendO = new JButton(txtSendO);
    jbSendO.setEnabled(false); //nije jos implementirano :)
    JButton jbCancel = new JButton(txtCancel);
    JPanel jpcont = new JPanel(new GridLayout(0,1,10,10));
    jbSendAll.addActionListener(this);
    jbSend1.addActionListener(this);
    jbSendO.addActionListener(this);
    jbCancel.addActionListener(this);
    jpcont.add(jbSendAll);
    jpcont.add(jbSend1);
    jpcont.add(jbSendO);
    jpcont.add(jbCancel);
    dlgConfirm.setContentPane(jpcont);
    dlgConfirm.setMinimumSize(new Dimension(500, 200));
    startFrame.getStartFrame().centerFrame(dlgConfirm, 0, "Slanje e-mail poruke serviseru");
    dlgConfirm.show();
  }
  
  private String getServiser(int cpar) {
    String serviserKey = servisertbl.equalsIgnoreCase("telemark")?"ctel":"cagent";
    QueryDataSet serv = Aus.q("SELECT "+servisertbl+".EMADR FROM Partneri, "+servisertbl+" WHERE partneri."+serviserKey+" = "+servisertbl+"."+serviserKey+" and cpar="+cpar);
    if (serv.getRowCount() == 0) return null;
    serv.first();
    return serv.getString("EMADR");
  }
  
  public File getAttachment() {
    try {
      File att = new File(masterSet.getString("STATUS")+"_"+new VarStr(masterSet.getString("CRADNAL")).replaceAll("/", "_"));
      att.createNewFile();
      return att;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String getFrom() {
    return Mailer.getMailProperties().getProperty("mailfrom");
  }
  private String getMessage() {
    String mess = frmParam.getParam("rn", "mailMess", "Radni nalog broj $B je $S. \n Kupac: $K \n Aparat: $A \n Datum: $D \n Opis: $O \n Operater: $U ","Poruka notif.maila RN $K-klijent,$S-status,$B-broj,$D-datum,$A-subjekt,$U-user,$O-opis");
    if (mess.trim().equals("")) return null;
    return replaceMnemonics(mess);
  }
  public String getSubject() {
    String subj = frmParam.getParam("rn", "mailSubj", "$S $B $K $A","Naslov notif.maila RN $K-klijent,$S-status,$B-broj,$D-datum,$A-subjekt,$U-user,$O-opis");
    return replaceMnemonics(subj);
  }
  
  
  
  private String replaceMnemonics(String txt) {
    dM.getDataModule().getPartneri().open();
    String _K = " ";
    if (lookupData.getlookupData().raLocate(dM.getDataModule().getPartneri(), "CPAR", masterSet.getInt("CKUPAC")+"")) {
      _K = dM.getDataModule().getPartneri().getString("NAZPAR")+", "+dM.getDataModule().getPartneri().getString("ADR")+","+dM.getDataModule().getPartneri().getString("MJ");
    }
//    String[] sts = new String[] {"P", "O", "F", "Z"};
    String _S = "";
    try {
      String sts = "POFZ";
      String[] stxt = new String[] {"Prijavljen", "Obraðen", "Fakturiran", "Zatvoren"};
      _S = stxt[sts.indexOf(masterSet.getString("STATUS"))];
    } catch (Exception e) {
      e.printStackTrace();
    }
    String _B = masterSet.getString("CRADNAL");
    String _D = raDateUtil.getraDateUtil().dataFormatter(masterSet.getTimestamp("DATDOK"));
    String _A = "";
    if (txt.indexOf("$A") > 0) {//heavy metla
      QueryDataSet dsn = Aus.q("SELECT rn_subjekt.broj from RN_subjekt, RN WHERE RN_subjekt.csubrn = rn.csubrn and rn.cRADNAL='"+masterSet.getString("CRADNAL")+"'");
      if (dsn.getRowCount()>0) {
        dsn.first();
        _A = dsn.getString("BROJ")+"/";
      }
      QueryDataSet dzn = Aus.q("SELECT cznac, vriznac from RN_znacsub where CRADNAL='"+masterSet.getString("CRADNAL")+"' ORDER BY cznac");
      for (dzn.first(); dzn.inBounds(); dzn.next()) {
        _A = _A + dzn.getString("VRIZNAC")+"/";
      }
    }
    String _U = masterSet.getString(masterSet.getString("STATUS").equals("P")?"CUSEROTVORIO":"CUSEROBRAC");
    String _O = masterSet.getString("OPIS");
    return new VarStr(txt)
        .replaceAll("$K", _K)
        .replaceAll("$S", _S)
        .replaceAll("$B", _B)
        .replaceAll("$D", _D)
        .replaceAll("$A", _A)
        .replaceAll("$U", _U)
        .replaceAll("$O", _O)
        .toString();
  }
  public void actionPerformed(ActionEvent e) {
    String cmnd = e.getActionCommand();
    System.out.println("KOMMAND : : "+ cmnd);
    if (cmnd.equals(txtCancel)) {
      recps = null;
    } else if (cmnd.equals(txtSendAll)) {
      QueryDataSet serv = Aus.q("SELECT "+servisertbl+".EMADR FROM "+servisertbl+"");
      ArrayList servemails = new ArrayList();
      for (serv.first(); serv.inBounds(); serv.next()) {
        String em = serv.getString("EMADR");
        if (!"".equals(em)) servemails.add(em);
      }
      if (servemails.size() > 0) recps = (String[])servemails.toArray(new String[] {});
      else recps = null;
    } else if (cmnd.startsWith(txtSend1) && cmnd.contains("@")) {
      //recps je vec pun
    }
    ((JComponent)e.getSource()).getTopLevelAncestor().setVisible(false);
  }
  
}
