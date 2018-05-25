/****license*****************************************************************
**   file: raRemoteBackup.java
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

import hr.restart.baza.ConsoleCreator;
import hr.restart.baza.dM;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


public class raRemoteBackup {

  public raRemoteBackup() {
    dM.setMinimalMode();
  }
  
  public void perform(boolean console) {    
    String other = IntParam.getTag("backup.extended");
    List keys = new ArrayList();
    if ("true".equals(other)) {
      List k = console ? zipAndUpload(true) : zipAndUpload();
      if (k != null) keys.addAll(k);
    }
    String baze = IntParam.getTag("backup.baze");
    System.out.println("baze: " + baze);
    if (baze == null || baze.length() == 0) {
      if (console) dumpAndUploadCons("baza");
      else dumpAndUpload("baza");
      keys.add("baza");
      new AmazonHandler().maintenance((String[]) keys.toArray(new String[keys.size()]));
      return;
    }
    
    String[] barr = new VarStr(baze).splitTrimmed(',');
    
    Properties props = new Properties();
    FileHandler.loadProperties("base.properties", props);
    List baks = new ArrayList();
    
    String dtip = props.getProperty("tip");
    String duser = props.getProperty("user");
    String dpass = props.getProperty("pass");
    String ddbdialect = props.getProperty("dbdialect","");
    boolean any = false, all = true;
    for (int bi = 1; any || all; bi++) {
      String name = props.getProperty("name" + bi);
      String url = props.getProperty("url" + bi);
      String tip = props.getProperty("tip" + bi, dtip);
      String user = props.getProperty("user" + bi, duser);
      String pass = props.getProperty("pass" + bi, dpass);
      String params = props.getProperty("params" + bi, "");
      String dbdialect = props.getProperty("dbdialect" + bi,ddbdialect);
      all = name != null && url != null && tip != null && user != null && pass != null;
      any = name != null || url != null;
      if (all) {
        for (int i = 0; i < barr.length; i++)
          if (url.indexOf(barr[i] + ".") > 0) {
            baks.add(new raDbaseChooser.BaseDef(name, url, tip, user, pass, params, dbdialect));
            break;
          }
      }
    }
    List barrl = new ArrayList(keys);
    for (int i = 0; i < baks.size(); i++) {
      raDbaseChooser.BaseDef bd = (raDbaseChooser.BaseDef) baks.get(i);
      int p = bd.url.lastIndexOf('/');
      int wp = bd.url.lastIndexOf('\\');
      if (wp > p) p = wp;
      if (p < 0) continue;
      
      String name = bd.url.substring(p + 1);
      name = name.substring(0, name.indexOf('.'));
      System.out.println("backup: " + name);
      barrl.add(name);
      
      dM.getDataModule().setMinimalParams(bd.url, bd.tip, bd.user, bd.pass);
      dM.getDataModule().reconnect();
      
      if (console) dumpAndUploadCons(name);
      else dumpAndUpload(name);
    }
    new AmazonHandler().maintenance((String[]) barrl.toArray(new String[barrl.size()]));
  }
  
  void dumpAndUploadCons(String name) {
    try {
      File f = ConsoleCreator.dumpCurrentDatabase(name);
      if (f.exists())
        new AmazonHandler().putFile(f);
      f.delete();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  List zipAndUpload(boolean console) {
    try {
      Properties props = new Properties();
      FileHandler.loadProperties("backup.properties", props);
      List ret = new ArrayList();
            
      for (Iterator i = props.keySet().iterator(); i.hasNext(); ) {
        String key = (String) i.next();
        String files = props.getProperty(key);
        process(console, key, files);
        ret.add(key);
      }
      return ret;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  List zipAndUpload() {
    raProcess.runChild(new Runnable() {
      public void run() {
        raProcess.yield(zipAndUpload(false));
      }
    });
    return (List) raProcess.getReturnValue();
  }
  
  void process(boolean console, String key, String filestr) {
    String[] parts = new VarStr(filestr).splitTrimmed(';');
    List files = new ArrayList();
    for (int i = 0; i < parts.length; i++) {
      if (parts[i].indexOf('*') < 0) files.add(new File(parts[i]));
      else files.addAll(findFiles(parts[i]));
    }
    System.out.println(key + " = " + files);
    File zfile = getTempFile(key);
    if (!console) {
      raProcess.getDialog().setTitle("Upload " + key);
      raProcess.setMessage("Arhiviranje datoteka za " + key, true);
    }
    FileHandler.makeZippedFile((File[]) files.toArray(new File[files.size()]), zfile);
    new AmazonHandler().putFile(zfile);
    zfile.delete();
  }

  List findFiles(String desc) {
    int dirp = desc.lastIndexOf(File.separatorChar);
    if (dirp < 0) return findFiles(new File("."), desc);
    return findFiles(new File(desc.substring(0, dirp)), desc.substring(dirp + 1));
  }
  
  List findFiles(File dir, String desc) {
    if (!dir.exists() || !dir.isDirectory() || desc.indexOf('*') < 0) 
      return new ArrayList();
    
    raGlob glob = new raGlob(desc);
    
    List files = new ArrayList();
    File[] f = dir.listFiles();
    for (int i = 0; i < f.length; i++)
      if (f[i].isFile() && glob.matches(f[i].getName()))
        files.add(f[i]);
    return files;
  }

  void dumpAndUpload(final String name) {
    /*File bdir = new File(System.getProperty("user.dir")+File.separator+"backups");
    String sufix = ".zako";
    String bfname = "raBackup-"+name+"-"+new java.sql.Timestamp(System.currentTimeMillis()).toString().substring(0,10);
    int a = 1;
    File bfile = null;
    String orgsufix = sufix;
    while ((bfile = new File(bdir, bfname+sufix)).exists()) {
      sufix = "_"+a+orgsufix;
      a++;
      if (a > 99) break;
    }
    if (!bdir.isDirectory()) bdir.delete();
    if (!bdir.exists()) bdir.mkdirs();*/
    File bfile = getTempFile(name);
    String retVal = raDbaseCreator.dumpTo(bfile);
    if (retVal == null) {
      final File upfile = bfile;
      raProcess.runChild(new Runnable() {
        public void run() {
          raProcess.getDialog().setTitle("Upload " + name);
          new AmazonHandler().putFile(upfile);
        }
      });
      upfile.delete();
    }
  }
  
  File getTempFile(String name) {
    File bdir = new File(System.getProperty("user.dir")+File.separator+"backups");
    String sufix = ".zako";
    String bfname = "raBackup-"+name+"-"+new java.sql.Timestamp(System.currentTimeMillis()).toString().substring(0,10);
    int a = 1;
    File bfile = null;
    String orgsufix = sufix;
    while ((bfile = new File(bdir, bfname+sufix)).exists()) {
      sufix = "_"+a+orgsufix;
      a++;
      if (a > 99) break;
    }
    if (!bdir.isDirectory()) bdir.delete();
    if (!bdir.exists()) bdir.mkdirs();
    return bfile;
  }
}
