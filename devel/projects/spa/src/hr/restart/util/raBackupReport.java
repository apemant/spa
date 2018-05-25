/****license*****************************************************************
**   file: raBackupReport.java
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
package hr.restart.util;

import hr.restart.robno.raDateUtil;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.raExtendedTable;
import hr.restart.util.mail.Mailer;
import hr.restart.util.mail.SimpleMailer;
import hr.restart.util.reports.repDynamicProvider;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;


public class raBackupReport {

  public raBackupReport() {

  }
  
  public void perform() {
    
    String mails = frmParam.getParam("sisfun", "backupto", "ante@rest-art.hr", "Mailovi za slati backup report");
    
    Set baks = new AmazonHandler().getEverything();
    
    Timestamp td = Valid.getValid().getToday();
    
    StorageDataSet out = Aus.createSet("{Tvrtka}COMP:50 {Baza}BASE:50 {Raniji b.}@DATUM DM2:2 DM1:2 TD:2");
    out.getColumn("TD").setCaption(Aus.formatTimestamp(td));
    out.getColumn("DM1").setCaption(Aus.formatTimestamp(Util.getUtil().addDays(td, -1)));
    out.getColumn("DM2").setCaption(Aus.formatTimestamp(Util.getUtil().addDays(td, -2)));
    
    out.getColumn("COMP").setWidth(20);
    out.getColumn("BASE").setWidth(20);
    out.getColumn("DM2").setWidth(15);
    out.getColumn("DM1").setWidth(15);
    out.getColumn("TD").setWidth(15);
    
    Set comps = new HashSet();
    for (Iterator i = baks.iterator(); i.hasNext(); ) {
      AmazonHandler.Entry e = (AmazonHandler.Entry) i.next();
      int s = e.name.indexOf('/');
      if (s > 0) comps.add(e.name.substring(0, s));
    }
    
    System.out.println(comps);

    List scomp = new ArrayList(comps);
    Collections.sort(scomp);
    
    System.out.println(scomp);
    
    for (int i = 0; i < scomp.size(); i++)
      fillCompany((String) scomp.get(i), baks, out);
    
    frmTableDataView tab = new frmTableDataView();
    tab.setDataSet(out);
    tab.setTitle("Pregled Amazon backupa  na dan "+ Aus.formatTimestamp(td));
    
    File tmpf = new File("backup-report.xls");
    tmpf.delete();
    
    repDynamicProvider.getInstance().prepareReport(tab.jp.getMpTable(), tab.getTitle());
    repDynamicProvider.getInstance().activate();
    ((raExtendedTable) tab.jp.getMpTable()).exportToXLS(tmpf);
        
    SimpleMailer mailer = new SimpleMailer();
    mailer.addAttachement(tmpf.getAbsolutePath());
    mailer.setFrom(Mailer.getMailProperties().getProperty("mailfrom"));
    mailer.setSubject("SPA BACKUP REPORT " + Aus.formatTimestamp(td));
    mailer.setMailHost(Mailer.getMailProperties().getProperty("mailhost"));
    mailer.setRecipients(new VarStr(mails).splitTrimmed(';'));
    mailer.setMessage("U attachmentu.");
    String user = Mailer.getMailProperties().getProperty("mailuser");
    if (user != null && user.length() > 0)
      mailer.setAuth(user, Mailer.getMailProperties().getProperty("mailpasswd"));
    
    String port = Mailer.getMailProperties().getProperty("mailport");
    if (port != null && port.length() > 0) mailer.setPort(port);
    mailer.setTLS("true".equals(Mailer.getMailProperties().getProperty("mailtls")));
    
    try {
      mailer.sendMail();
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    
    tmpf.delete();
    /*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Press RETURN");
    try {
      String s = br.readLine();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }*/
  }
  
  void fillCompany(String prefix, Set baks, DataSet out) {
    if (prefix.equalsIgnoreCase("backup")) return;
    for (Iterator i = baks.iterator(); i.hasNext(); ) {
      AmazonHandler.Entry e = (AmazonHandler.Entry) i.next();
      if (e.name.startsWith(prefix + "/")) {
        String[] parts = new VarStr(e.name).split('-');
        if (parts.length > 2) {
          String baza = parts[1];
          if (!lookupData.getlookupData().raLocate(out, new String[] {"COMP", "BASE"}, new String[] {prefix, baza})) {
            out.insertRow(false);
            out.setString("COMP", prefix);
            out.setString("BASE", baza);
            out.setString("TD", "XX");
            out.setString("DM1", "XX");
            out.setString("DM2", "XX");
          }
          int diff = raDateUtil.getraDateUtil().DateDifference(e.date, Valid.getValid().getToday());
          if (diff <= 2) {
            if (diff == 0) out.setString("TD", "1");
            else if (diff == 1) out.setString("DM1", "1");
            else out.setString("DM2", "1");
          } else if (out.isNull("DATUM") || e.date.after(out.getTimestamp("DATUM"))) 
            out.setTimestamp("DATUM", new Timestamp(e.date.getTime()));
        }
      }
    }
  }
}
