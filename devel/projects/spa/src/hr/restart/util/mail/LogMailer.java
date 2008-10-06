/****license*****************************************************************
**   file: LogMailer.java
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
 * Created on Mar 22, 2005
 * Zips and mails log to some location
 * <pre> 
 * uses: SimpleMailer.java
 *       startFrame.java
 * 			 raMiniBackup.java
 * 3partyJars:
 * 	     activation.jar
 *       mail.jar
 * </pre>
 */
package hr.restart.util.mail;

import hr.restart.baza.Orgstruktura;
import hr.restart.sisfun.raUser;
import hr.restart.util.FileHandler;
import hr.restart.util.Util;
import hr.restart.util.lookupData;
import hr.restart.zapod.OrgStr;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * @author andrej
 * Zips and mails log to 'development' team 
 */
public class LogMailer extends Mailer {
  /**
   * zips logfile (getLogFileName()) and all *.properties files including System.properties
   * @return file getLogFileName().[1-n].zip
   */
  private static File getZippedLog() {
    //log file
    File logf = new File(Util.getLogFileName());
    //patch info
    FileHandler.writeConverted(hr.restart.util.versions.raVersionInfo.getPatchesInfo(),"patches_info.txt",null);
    File patchinfo = new File("patches_info.txt");
    //system properties
    Properties sysprop = new Properties();
    Hashtable sysh = (Hashtable)System.getProperties().clone();
    for (Iterator iter = sysh.keySet().iterator(); iter.hasNext();) {
      String key = iter.next().toString();
      sysprop.setProperty(key,sysh.get(key).toString());
    }
    sysprop.setProperty("SPA.version", hr.restart.util.versions.raVersionInfo.getCurrentVersion());
    FileHandler.storeProperties("system.properties",System.getProperties());
    //all properties
    File rundir = new File(System.getProperty("user.dir"));
    File[] propsfiles = rundir.listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".properties");
      }
    });
    File[] allfiles = new File[propsfiles.length+2];

    System.arraycopy(propsfiles,0,allfiles,0,propsfiles.length);
    allfiles[allfiles.length-2] = logf;
    allfiles[allfiles.length-1] = patchinfo;
    int idx = 1;
    boolean succ = false;
    File cplogf = null;
    while (!succ) {
      try {
        cplogf = new File(Util.getLogFileName()+"."+idx+".zip");
        if (!cplogf.createNewFile()) throw new IOException("File "+cplogf.getPath()+" already exists");
        succ = true;
      } catch (IOException e) {
        System.out.println("creation of new file "+Util.getLogFileName()+"."+idx+".zip unsuccesfull :) "+e);
        idx++;
      }
      if (idx>9) throw new RuntimeException("Logmailer: Unable to rename log file");
    }
    
    if (FileHandler.makeZippedFile(allfiles,cplogf)) {
      return cplogf;
    } else return null;
  }

  public File getAttachment() {
    return getZippedLog();
  }

  public String getFrom() {
    return getMailProperties().getProperty("mailfrom");
  }

  public String getSubject() {
    QueryDataSet knjs = Orgstruktura.getDataModule().getKnjig();
    knjs.open();
    lookupData.getlookupData().raLocate(knjs, "CORG", OrgStr.getKNJCORG());
    return "SPA: Automatski e-mail od "+raUser.getInstance().getUser()+"@"+knjs.getString("NAZIV");
  }
}
