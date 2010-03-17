/****license*****************************************************************
**   file: ftpVersionWorker.java
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
 * ftpVersionWorker.java
 *
 * Created on 2004. veljaèa 19, 10:27
 */

package hr.restart;

import hr.restart.db.ftpTransfer;
import hr.restart.util.FileHandler;
import hr.restart.util.FileTransferUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import com.oroinc.net.ftp.FTPClient;
import com.oroinc.net.ftp.FTPReply;
/**
 * Changelog:
 dodan tag ftpdtype = 'bsd' ili bilo sta drugo odnosno da li koristi (neverificirajuci) ftpTransfer 
 java.net.URL za transfer -> mogucnost (sporog) windows ftp servera

 * @author  andrej
 */
public class ftpVersionWorker {
  /** Creates a new instance of ftpVersionWorker */
  public static String CHECKVERSIONFILE = "versions";
  public static String VERSIONPROPERTIES = "version.properties";
  private static Properties verProps;
  private static Properties pversions;
  private static Properties pversions_local;
  private static FileTransferUtil ftu = null;
  
  protected ftpVersionWorker() {
  }
  private static FileTransferUtil getFileTransferUtil() {
    if (verProps == null) verProps = getVersionProperties(); //malo vjerojatno :)
    if (ftu == null) ftu = new FileTransferUtil(verProps);
    return ftu;
  }
  static int start() throws Exception {
    if (verProps == null) verProps = getVersionProperties();
    ftpStart.splashMessg("Pokretanje sustava ...");
    Process proc = Runtime.getRuntime().exec(verProps.getProperty("starter"), null,new java.io.File(verProps.getProperty("starterworkdir")));
/*    int ch;
    while ((ch = proc.getErrorStream().read()) > -1) System.out.write(ch);
    while ((ch = proc.getInputStream().read()) > -1) System.out.write(ch);*/
    return 0;//proc.waitFor();
  }
  public static void transferNewResourcesAndStart() throws Exception {
    transferNewResources();
    start();
  }
  
  public static void transferNewResources() throws Exception {
    verProps = getVersionProperties();
    String CVFile = verProps.getProperty("locallib")+File.separator+CHECKVERSIONFILE;
    //prebaci stari versions u versions.local
    File lversions = new File(CVFile);
    File lversions_local = new File(CVFile+".local");
    if (lversions_local.exists()) lversions_local.delete();
    if (lversions.exists()) lversions.renameTo(lversions_local);
    //dohvati novi versions
    getFile(CHECKVERSIONFILE);
    pversions = FileHandler.getProperties(CVFile);
    pversions_local = FileHandler.getProperties(CVFile+".local");
    Enumeration pverkeys = pversions.propertyNames();
    Enumeration pverkeys_local = pversions_local.propertyNames();
    //dohvati sve fileove
    int no_curr = 1;
    int no_all = pversions.keySet().size();
    while (pverkeys.hasMoreElements()) {
      String pverkey = pverkeys.nextElement().toString();
      ftpStart.splashMessg("Provjera nove verzije ("+no_curr+"/"+no_all+") ...");
      if (!checkVersion(pverkey)) getFile(pverkey, "Instalacija nove verzije ("+no_curr+"/"+no_all+") ...");
      no_curr++;
    }
//    cleanNcftp();
    getFileTransferUtil().closeFTP();
    //obrisi kojih vise nema
    no_curr = 1;
    while (pverkeys_local.hasMoreElements()) {
      String pverkey = pverkeys_local.nextElement().toString();
      if (!pversions.containsKey(pverkey)) {
        ftpStart.splashMessg("Brisanje starih datoteka ("+no_curr+") ...");
        File delfin = new File(verProps.getProperty("locallib")+File.separator+pverkey);
        if (delfin.delete()) ftpStart.splashMessg(no_curr+" obrisan!");
        no_curr++;
      }
    }
  }
  
  public static void waitForAndExit() {
    if (verProps == null) verProps = getVersionProperties();
    long t = Long.parseLong(verProps.getProperty("waitbeforeexit"));
    System.out.println(new Date(System.currentTimeMillis()).toString()+" :: waiting for "+t+"ms");
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      public void run() {
        System.out.println(new Date(System.currentTimeMillis()).toString()+" :: closing ftpStart ...");
        System.exit(0);
      }
    }, t);
  }

  private static boolean checkVersion(String filename) {
    if (filename.equals(CHECKVERSIONFILE)) return true;
    return pversions.getProperty(filename, "").equals(pversions_local.getProperty(filename, ""));
  }
/*
    File destf = new File("./trans/SPA.jar");
    ftpTransfer.getftpTransfer(true).get("161.53.200.99",21,"restart","restart","/L/build/jnlplib/",destf);
 */  
  private static void getFile(String fileName) throws Exception {
    getFile(fileName, null);
  }
//  private static boolean downloadFromURL(String fileName) {
//    try {
//      java.net.URL url = new java.net.URL("ftp://" 
//            +verProps.getProperty("user")+":"
//            +verProps.getProperty("pass")+"@"
//            +verProps.getProperty("url")+":"
//            +verProps.getProperty("port")+"/"
//            +verProps.getProperty("serverlib")
//            +"/"
//            +fileName
//      );
//      java.io.InputStream stream = url.openStream();
//      File file = new File(verProps.getProperty("locallib")+File.separator+fileName);
//      FileOutputStream outstr = new FileOutputStream(file);
//      int ch;
//      while ((ch = stream.read())>-1) outstr.write(ch);
//      outstr.close();
//      return true;
//    } catch (Exception e) {
//      e.printStackTrace();
//      return false;
//    }
//  }
//  private static boolean downloadWithCustomClient(String fileName) {
//    File file = new File(verProps.getProperty("locallib")+File.separator+fileName);
//    return ftpTransfer.getftpTransfer(true).get(
//        verProps.getProperty("url"), 
//        Integer.parseInt(verProps.getProperty("port")), 
//        verProps.getProperty("user"), 
//        verProps.getProperty("pass"),
//        verProps.getProperty("serverlib"), 
//        file);
//  }
  private static boolean downloadFile(String fileName) throws Exception {
    try {
      File dlf = getFileTransferUtil().loadFile(fileName, true);
      File f = new File(verProps.getProperty("locallib")+File.separator+fileName);
//      dlf.renameTo(f);
      FileHandler.copy(dlf, f);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
  /*
  private static boolean downloadWithNetComponents(String fileName) throws Exception {
    FTPClient c = getNetComponentsFTPClient();
//    FileHandler fh = new FileHandler(verProps.getProperty("locallib")+File.separator+fileName);
    //fh.getWriter().
    String fn = verProps.getProperty("locallib")+File.separator+fileName;
    FileOutputStream fos = new FileOutputStream(fn);
    boolean ret = c.retrieveFile(fileName,fos);
    fos.flush();
    fos.close();
    return ret;
  }
  */
  //Netcomponents
//  private static FTPClient ncftp = null; 
//  private static FTPClient getNetComponentsFTPClient() throws Exception {
//    if (ncftp == null) {
//      ncftp = new FTPClient();
//      ncftp.connect(verProps.getProperty("url"));
//      int reply = ncftp.getReplyCode();
//      if (!FTPReply.isPositiveCompletion(reply)) {
//        throw new Exception("Server refuses connection");
//      }
//      ncftp.login(verProps.getProperty("user"),verProps.getProperty("pass"));
//      ncftp.setFileType(FTPClient.IMAGE_FILE_TYPE);
//      ncftp.changeWorkingDirectory(verProps.getProperty("serverlib"));
//    }
//    return ncftp;
//  }
//  private static void cleanNcftp() {
//    try {
//      if (ncftp != null) {
//        ncftp.disconnect();
//        ncftp = null;
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//  private static boolean isBSDftpd() {
//    return verProps.getProperty("ftpdtype").equalsIgnoreCase("bsd");
//  }
  private static void getFile(String fileName, String message) throws Exception {
    //provjeri jel ima lib
    File locallib = new File(verProps.getProperty("locallib"));
    if (!locallib.exists()) locallib.mkdirs();
    if (!locallib.isDirectory()) throw new Exception("Neispravno definiran locallib!");
    if (message == null) message = "Transfer "+fileName+"...";
    ftpStart.splashMessg(message);
    
    //boolean transfered = isBSDftpd()?downloadWithCustomClient(fileName):downloadFromURL(fileName);
    //boolean transfered = downloadWithCustomClient(fileName);
//    boolean transfered = downloadWithNetComponents(fileName);
    boolean transfered = downloadFile(fileName);
    if (!transfered) throw new Exception("Transfer datoteke "+fileName+" neuspjesan !!");
    ftpStart.splashMessg(message+" uspjeh!");
  }
   /**
   * Mudro prorice i sastavlja version.properties
   * @return properties
   */
  public static Properties getVersionProperties() {
    Properties vpr = FileHandler.getProperties(VERSIONPROPERTIES);
    if (vpr.getProperty("url") == null)
      vpr.setProperty("url", extractIPAddr(FileHandler.getProperties("restart.properties").getProperty("url")));
    if (vpr.getProperty("url","").equals("")) {
      vpr.setProperty("url","localhost");
//      throw new RuntimeException("Nije definiran server u version.properties");
    }
    if (vpr.getProperty("port") == null) vpr.setProperty("port", "21");
    if (vpr.getProperty("user") == null) vpr.setProperty("user", "restart");
    if (vpr.getProperty("pass") == null) vpr.setProperty("pass", "restart");
    String serverlib = "/opt/restart/lib";
    String url = vpr.getProperty("url","");
    if (url.equals("161.53.200.99")) serverlib = "/sda1/fileserver/restart/lib"; //REST ART
    if (url.equals("161.53.200.202")) serverlib = "/home/restart/lib"; //ECOS VITEZICEVA
    if (vpr.getProperty("serverlib") == null) vpr.setProperty("serverlib", serverlib);
    String locallib = "C:\\restart\\lib";
    String starter = "C:\\restart\\starter.exe";
    String starterworkdir = "C:\\restart";
    if (System.getProperty("os.name").toLowerCase().equals("linux")) {
      locallib = "/opt/restart/lib";
      starter = "/opt/restart/startSPA.sh";
      starterworkdir = "/opt/restart";
    }
    if (vpr.getProperty("locallib") == null) vpr.setProperty("locallib", locallib);
    if (vpr.getProperty("starter") == null) vpr.setProperty("starter", starter);
    if (vpr.getProperty("starterworkdir") == null) vpr.setProperty("starterworkdir", starterworkdir);
    if (vpr.getProperty("waitbeforeexit") == null) vpr.setProperty("waitbeforeexit", "3000");
    if (vpr.getProperty("ftpdtype") == null) vpr.setProperty("ftpdtype", "bsd");
    FileHandler.storeProperties(VERSIONPROPERTIES, vpr);
    return vpr;
  }
 
  public static String extractIPAddr(String dburl) {
    java.util.StringTokenizer tok = new java.util.StringTokenizer(dburl,".");
    String host = "";
    while (tok.hasMoreTokens()) {
      String t = tok.nextToken();
      //System.out.println("tok = "+t);
      try {
        String t1 = t.substring(t.length()-3);
        //System.out.println("t1 = "+t1);
        int _1 = Integer.parseInt(t1);
        host = host + _1;
        try {
          String t2 = tok.nextToken();
          //System.out.println("t2 = "+t2);
          int _2 = Integer.parseInt(t2);
          host = host + "." + _2;
          try {
            String t3 = tok.nextToken();
            //System.out.println("t3 = "+t3);
            int _3 = Integer.parseInt(t3);
            host = host + "." + _3;
            try {
              char[] t4_tmp = tok.nextToken().toCharArray();
              String t4 = "";
              for (int i = 0; i < t4_tmp.length; i++) {
                if (Character.isDigit(t4_tmp[i])) {
                  t4 = t4+t4_tmp[i];
                } else break;
              }
              //System.out.println("t4 = "+t4);
              int _4 = Integer.parseInt(t4);
              host = host + "." + _4;
              break;
            }
            catch (Exception ex) {
              host = "";
            }
          }
          catch (Exception ex) {
            host = "";
          }
        }
        catch (Exception ex) {
          host = "";
        }
      } catch (Exception ex) {
        host = "";
      }
    }
    return host;
  }
  
/*
 * main radi versions file za zadani lib dir 
 */
  public static void main(String[] args) {
    makeVersionsFile(args[0]);
  }
  
  public static void makeVersionsFile(String libdirpath) {
    File libdir = new File(libdirpath);
    Properties chkverprop = new Properties();
    if (!libdir.isDirectory()) throw new IllegalArgumentException(libdirpath + " nije direktorij! ");
    File[] filesindir = libdir.listFiles();
    for (int i=0; i<filesindir.length; i++) {
      if (filesindir[i].isFile()) {
//        System.out.println("Inspecting "+filesindir[i]);
        chkverprop.setProperty(filesindir[i].getName(), FileHandler.getSHA1(filesindir[i].getAbsolutePath()));
      }
    }
    FileHandler.storeProperties(libdirpath+File.separator+CHECKVERSIONFILE, chkverprop);
//    System.out.println(new java.util.Date(System.currentTimeMillis()).toString()+" Versions file created !");
  }

}
