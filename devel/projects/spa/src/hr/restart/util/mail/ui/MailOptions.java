/****license*****************************************************************
**   file: MailOptions.java
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
/*
 * Created on May 23, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.util.mail.ui;

import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.util.FileHandler;
import hr.restart.util.VarStr;
import hr.restart.util.propsDialog;
import hr.restart.util.raLLFrames;
import hr.restart.util.startFrame;
import hr.restart.util.mail.LogMailer;
import hr.restart.util.mail.Mailer;
import hr.restart.util.reports.ReportModifier;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Properties;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MailOptions extends propsDialog {
  private String pathpx = LogMailer.getMailProperties().getProperty("mailboxdir")+File.separator;
  public MailOptions() {
    super(Mailer.getMailProperties().getProperty("mailboxdir")+File.separator+"mail.properties","Mailbox");
  }
  public void props_add() {
    new LogMailer().sendMailUI(null,null,false, null);
    refreshList();
  }
  private Mailer getMailer(final Properties props) {
    return new Mailer() {

      public File getAttachment() {
        return null;
      }

      public String getFrom() {
        return props.getProperty("from");
      }

      public String getSubject() {
        return props.getProperty("subject");
      }
      
    };
  }
  
  private void fillData(Properties props) {
    data = new StorageDataSet();
    data.setColumns(new Column[] {
        dM.createTimestampColumn("DATE",  "Vrijeme"),
        dM.createStringColumn("REC", "Primatelj", 50),
        dM.createStringColumn("FILE", "Datoteka", 100),
        dM.createStringColumn("SENT", "Poslano", 10)
    });
    //data.getColumn("PKEY").setPrecision(-1);
    //data.getColumn("PVAL").setPrecision(-1);
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
    
    //String fn = "SPA-ERP_ispis"+time_suffix+".pdf";
    
    data.open();

    for (Iterator i = props.keySet().iterator(); i.hasNext(); ) {
      String key = (String) i.next();
      data.insertRow(false);
      data.setString("FILE", key);
      if (props.getProperty(key).equals("false")) data.setString("SENT", "-X-");
      
      String savedMailInfo = pathpx+key+".properties";
      Properties detail = new Properties();
      FileHandler.loadProperties(savedMailInfo, detail);
      data.setString("REC", detail.getProperty("recipient"));
      
      if (key.startsWith("SPA-ERP_ispis")) {
        key = key.substring("SPA-ERP_ispis".length());
        key = key.substring(0, key.indexOf('.'));
        try {
          data.setTimestamp("DATE", new Timestamp(sdf.parse(key).getTime()));
        } catch (ParseException e) {
          // TODO Auto-generated catch block
          //e.printStackTrace();
        }
      }
    }
    data.post();
    data.setSort(new SortDescriptor(new String[] {"DATE"}));
    jp.setDataSet(data);
  }
  public void refreshList() {
    fillData(props4list);
  }
  
  
  public void props_upd() {
    String pdf = frmParam.getParam("sisfun", "openPdfComm", "", "Naredba za otvaranje PDF datoteka");
    if (pdf.length() > 0) {
      String comm = new VarStr(pdf).replace("%f", pathpx+data.getString("FILE")).toString();
      try {
        Process proc = Runtime.getRuntime().exec(comm);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    /*try {
      Class.forName("hr.restart.util.reports.ReportMailDialog");
      props_upd_repmail();
    } catch (Exception e) {
      props_upd_errmail();
    }*/
  }
  public void props_upd_repmail() {//kopipejst ubijte me!
/*    String pth = pathpx+getListSelectedKey()+".properties";
    System.out.println("props_upd.pth : "+pth);
    Properties props = FileHandler.getProperties(pth);
    props.list(System.out);
    String rec = props.getProperty("recipient");
    String msg = props.getProperty("message");
    String subj = props.getProperty("subject");
    StorageDataSet retSet = hr.restart.util.reports.ReportMailDialog.showMailDialog(rec,subj,msg);
    if (retSet!=null) {
      Mailer m = getMailer(props);
      m.setRecipient(retSet.getString("EMADR"));
      m.sendMailUI(retSet.getString("TXT"), new File(props.getProperty("attachement")),true,retSet.getString("NASLOV"));
    }
    refreshList();*/
  }
  public void props_upd_errmail() {
    String pth = pathpx+getListSelectedKey()+".properties";
    System.out.println("props_upd.pth : "+pth);
    Properties props = FileHandler.getProperties(pth);
    props.list(System.out);
    MailFrame mf = MailFrame.getInstance();
    String rec = props.getProperty("recipient");
    String msg = props.getProperty("message");
    mf.fill(rec, msg);
    startFrame sf = raLLFrames.getRaLLFrames().getMsgStartFrame();
    sf.centerFrame(mf,0,mf.getTitle());
    mf.setMailer(getMailer(props));
    mf.show();
    if (mf.description != null) {
      mf.getMailer().sendMailUI(mf.description, new File(props.getProperty("attachement")),true,props.getProperty("subject"));
    }
    refreshList();
  }
}
