/****license*****************************************************************
**   file: Mailer.java
**   Copyright 2006 Rest Art
**
**   Licensed under the Apache License, Version 2.0 (the "License");
**   you may not use this file except in compliance with the License.
**   You may obtain a copy of the License at
**
**       http://www.apache.org/licenses/LICENSE-2.0
**
**   Unless required by applicable law or agreed to in writing, software
**   distributed under the License is distributed on an "AS IS" BASIS,
**   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
**   See the License for the specific language governing permissions and
**   limitations under the License.
**
****************************************************************************/
package hr.restart.util.mail;

import hr.restart.util.FileHandler;
import hr.restart.util.raProcess;
import hr.restart.util.mail.ui.MailFrame;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.swing.JOptionPane;

public abstract class Mailer {
  int attempts = 0;
  private static Properties mailprops;
  private String lastAttachment;
  private boolean sendMailUIsuccess = false;
  private String[] recipients;
  
  /**
   * Default constructor use:
   * <pre>
   * Mailer m = new Mailer() {
   *  //override abstract methods
   * };
   * m.mailLogUI(null); //or other public method
   * </pre>
   */
  public Mailer() {
    
  }
  
  public void sendMail(String mess) throws MessagingException {
    sendMail(mess, null, true, getSubject());
  }
  /**
   * File to attach to e-mail
   * @return 
   */
  public abstract File getAttachment();
  
  public void sendMail(String mess, File fattach, boolean send, String subj) throws MessagingException {
    SimpleMailer mailer = new SimpleMailer();
    File attachment;
    if (fattach == null) {
      attachment = getAttachment();
    } else {
      attachment = fattach;
    }
    lastAttachment = attachment.getAbsolutePath();
    mailer.addAttachement(lastAttachment);
    mailer.setFrom(getFrom());
    mailer.setSubject(subj);
    mailer.setMailHost(getMailHost());
    mailer.setRecipients(getRecipients());
    mailer.setMessage(mess+getChecksum());
    if (send) {
      mailer.sendMail();
    }
  }
  
  /**
   * @return
   */
  private String getChecksum() {
    return "\nSHA1:"+FileHandler.getSHA1(lastAttachment)+"\n";
  }

  /**
   * 
   * @param mess
   * @return
   */
  public boolean sendMailUI(String mess) {
    return sendMailUI(mess, null, true, getSubject());
  }
  /**
   * 
   * @param mess
   * @param fattach
   * @return
   */
  public boolean sendMailUI(String mess, final File fattach, final boolean send, final String subj) {
    final String messUI = (mess == null)?getMessageFromUser():mess;
    if (messUI == null) return false;
    raProcess.runChild("E-mail", "Slanje u tijeku...", new Runnable() {
      public void run() {
        sendMailUI_process(messUI, fattach, send, subj);
      }
    });
    if (!sendMailUIsuccess) {
      String moreinfo = ". Možete pokušati poslije opcijom E-Mail na ekranu Alati (Prozori -> Alati)";
      JOptionPane.showMessageDialog(null,"Neuspješno slanje e-mailom"+moreinfo);
    }
    saveForLater(messUI,sendMailUIsuccess);
    return sendMailUIsuccess;
  }

  /**
   * @param messUI
   * @param file
   */
  private void sendMailUI_process(String messUI, File fattach, boolean send, String subj) {
    try {
      raProcess.setMessage("Slanje e-mailom... Pokušaj "+(attempts+1), false);
      if (subj==null) subj = getSubject();
      sendMail(messUI, fattach, send, subj);
      sendMailUIsuccess = true;
    } catch (MessagingException e) {
      if (attempts > 10) {
        attempts = 0;
        /*if (raProcess.getDialog()!=null) {
          raProcess.getDialog().dispose();
        }*/
        sendMailUIsuccess = false;
        raProcess.fail();
      } else {
        attempts++;
        sendMailUI_process(messUI, new File(lastAttachment), send, subj);
      }
    }
  }
  /**
   * @param messUI
   */
  private void saveForLater(String messUI, boolean sent) {
/*    mailer.addAttachement(lastAttachment);
    mailer.setFrom(getFrom());
    mailer.setSubject(getSubject());
    mailer.setMailHost(getMailHost());
    mailer.setRecipients(getRecipients());
    mailer.setMessage(mess);*/
    File mbd = new File(getMailProperties().getProperty("mailboxdir"));
    if (!mbd.exists()) mbd.mkdirs();
    if (!mbd.isDirectory()) {
      mbd.delete();
      mbd.mkdirs();
    }
    String pfix = mbd.getAbsolutePath()+File.separator;
    String savedMailIdx = getSavedMailIdx();
    String savedMailInfo = pfix+savedMailIdx+".properties";
System.out.println(savedMailInfo);
    Properties savedMailProps = new Properties();
    savedMailProps.setProperty("from",getFrom());
    savedMailProps.setProperty("subject",getSubject());
    savedMailProps.setProperty("recipient",getRecipients()[0]);
    savedMailProps.setProperty("attachement",pfix+savedMailIdx);
    savedMailProps.setProperty("message",messUI);
    File att = new File(lastAttachment);
    File newatt = new File(pfix+savedMailIdx);
    att.renameTo(newatt);
    try {
      att.createNewFile();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    FileHandler.storeProperties(savedMailInfo,savedMailProps);
    Properties globalmailprops = FileHandler.getProperties(pfix+"mail.properties"); 
    globalmailprops.setProperty(savedMailIdx,new Boolean(sent).toString());
    FileHandler.storeProperties(pfix+"mail.properties",globalmailprops);
  }

  public String getSavedMailIdx() {
    return new File(lastAttachment).getName();
  }

  /**
   * gets short desc from user via JOptionPane
   * @return
   */
  private String getMessageFromUser() {
    return MailFrame.getMessage(this);//JOptionPane.showInputDialog("Unesite kratki opis problema. Sa opisom bit æe poslan i log file");
  }

  /**
   * 
   * @return
   */
  private String[] getRecipients() {
    return recipients;
  }
  
  public void setRecipients(String[] recipients) {
    this.recipients = recipients;
  }
  
  public void setRecipient(String recipient) {
    this.recipients = new String[] {recipient};
  }
  /**
   * @return
   */
  private String getMailHost() {
    return getMailProperties().getProperty("mailhost");
  }

  /**
   * @return
   */
  public abstract String getFrom();

  /**
   * @return
   */
  public abstract String getSubject();

  public static Properties getMailProperties() {
    if (mailprops != null) return mailprops; 
    mailprops = FileHandler.getProperties("mail.properties");
    boolean b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b14, b15;
    b1 = checkProperty(mailprops, "mailhost", "mail.t-com.hr", "mail.rest-art.hr");
    b2 = checkProperty(mailprops, "mailfrom", "errors@rest-art.hr");
    b3 = checkProperty(mailprops, "mailboxdir", "mailbox");
    b4 = checkProperty(mailprops, "mailrec", "razvoj@rest-art.hr");
    b5 = checkProperty(mailprops, "mailrec1", "ante@rest-art.hr");
    b6 = checkProperty(mailprops, "mailrec2", "andrej@rest-art.hr");
    b7 = checkProperty(mailprops, "mailrec3", "hrvoje@rest-art.hr");
    b8 = checkProperty(mailprops, "mailrec4", "mladen@rest-art.hr");
    b9 = checkProperty(mailprops, "mailrec5", "srky@rest-art.hr");
    b10 = checkProperty(mailprops, "mailrec6", "tomo@rest-art.hr");
    b11 = checkProperty(mailprops, "mailrec7", "vlado@rest-art.hr");
    b12 = checkProperty(mailprops, "mailrec8", "dborojevic@rest-art.hr");
    b14 = checkProperty(mailprops, "mailrec9", "kresimir@rest-art.hr");
    b15 =checkProperty(mailprops, "mailrec10", "marko@rest-art.hr");
    if (b1||b2||b3||b4||b5||b6||b7||b8||b9||b10||b11||b12||b14||b15) {//:)
      FileHandler.storeProperties("mail.properties", mailprops);
    }
    return mailprops;
  }
  private static boolean checkProperty(Properties p, String key, String value, String forceValueIf) {
    if (mailprops.getProperty(key) == null || mailprops.getProperty(key).equals(forceValueIf)) {
      mailprops.setProperty(key, value);
      return true;
    }
    return false;
  }
  private static boolean checkProperty(Properties p, String key, String value) {
    if (mailprops.getProperty(key) == null) {
      mailprops.setProperty(key, value);
      return true;
    }
    return false;
  }

}
