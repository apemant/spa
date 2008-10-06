/****license*****************************************************************
**   file: DataMailer.java
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

import hr.restart.baza.KreirDrop;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.util.DesEncrypter;
import hr.restart.util.Encrypter;
import hr.restart.util.FileHandler;
import hr.restart.util.Util;

import java.io.File;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.borland.dx.dataset.DataSet;
/**
 * Uzme niz query-ja i istoci ga u dat/def i sve zapakira i e-maila kao attachment
 * <pre>
 * BeanShell primjer:
 * d = new hr.restart.util.mail.DataMailer();
 * d.setRecipient("priglup@gmail.com");
 * d.enableSenderNameEncryption(true); //ili d.setEncrypter(new hr.restart.util.DesEncrypter("passphrase"));//
 * d.addQuery("partneri", "select * from partneri where nazpar like 'A%'");
 * d.addQuery("orgstruktura", "select * from orgstruktura");
 * d.sendMailUI("Probni podaci...");
 * </pre>
 * @author andrej
 *
 */
public class DataMailer extends Mailer {

  private HashMap querys = null;
  private String subject;
  private String attachmentPrefix = null;
  private Encrypter encrypter;
  private boolean senderNameEncryption = true;
  public DataMailer() {
    super();
    setRecipient(frmParam.getParam("sisfun","datamailrec","testiranje@rest-art.hr", "Na koju e-mail adresu se šalju podaci e-mailom"));
  }
  
  public File getAttachment() {
    File[] files4att = prepareFiles();
    File attachment = new File(getDataAttachmentName());
    if (senderNameEncryption) {
      DesEncrypter des = new DesEncrypter(getFrom());
      FileHandler.makeZippedFile(des,files4att, attachment);
    } else {
      FileHandler.makeZippedFile(files4att, attachment);
    }
    return attachment;
  }
  private String getDataAttachmentName() {
    String _name = getAttachmentPrefix()+new Date(System.currentTimeMillis()).toString()+".zip";
    return _name;
  }

  /**
   * ubaci queryje u datasetove i zdumpa ih u dat/def zadanih imena, sve u neki temp direktorij 
   * @return
   */
  private File[] prepareFiles() {
    
    File dtmp = new File("datamailertemp");
    if (dtmp.exists()) {
      // delete old files
      if (dtmp.isDirectory()) {
        File[] _f = dtmp.listFiles();
        for (int i = 0; i < _f.length; i++) {
          _f[i].delete();
        }
      }
      dtmp.delete();
    }
    //make temp directory
    dtmp.mkdir();
    dM.getDataModule().loadModules();
    for (Iterator iter = querys.keySet().iterator(); iter.hasNext();) {
      String fn = (String) iter.next();
      Object qry = querys.get(fn);
      //continue with creating and dumping datasets
      DataSet ds = null;
      if (qry instanceof DataSet) {
        ds = (DataSet)qry;
      } else if (qry instanceof String){
        ds = Util.getNewQueryDataSet((String)qry);
      } else {
        throw new IllegalArgumentException("In querys map can exist only Strings and DataSets !!!");
      }
      KreirDrop.getModuleByName(fn).dumpTable(ds, dtmp);
    }
    
    return dtmp.listFiles();
  }

  public String getFrom() {
    return frmParam.getParam("sisfun","datamailfrom","test@from.hr", "Sa koje e-mail adrese se šalju podaci");
  }

  public String getSubject() {
    return subject==null?getDataAttachmentName():subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * 
   * @param filename module name @see KreirDrop.getModuleByName(String)
   * @param query query for creating dataset 
   */
  public void addQuery(String filename, String query) {
    getQuerys().put(filename, query);
  }

  /**
   * 
   * @param filename module name @see KreirDrop.getModuleByName(String)
   * @param ds allready prepared dataset
   */
  public void addQuery(String filename, DataSet ds) {
    getQuerys().put(filename, ds);
  }
  
  public HashMap getQuerys() {
    if (querys == null) querys = new HashMap();
    return querys;
  }

  public void setQuerys(HashMap querys) {
    this.querys = querys;
  }

  public void setEncrypter(Encrypter encrypter) {
    this.encrypter = encrypter;
  }
  
  public void enableSenderNameEncryption(boolean b) {
    senderNameEncryption = b;
  }
  public String getAttachmentPrefix() {
    if (attachmentPrefix == null) {
      attachmentPrefix = frmParam.getParam("sisfun", "datamailatt", "podaci", "Prefix naziva attachmenta za mailanje podataka");
    }
    return attachmentPrefix;
  }
  public void setAttachmentPrefix(String attachmentPrefix) {
    this.attachmentPrefix = attachmentPrefix;
  }
}
