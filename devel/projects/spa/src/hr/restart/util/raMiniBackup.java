/****license*****************************************************************
**   file: raMiniBackup.java
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
 * raMiniBackup.java
 *
 * Created on 2004. lipanj 09, 11:42
 */

package hr.restart.util;
import java.io.File;
import java.io.FileFilter;
import java.sql.Date;
import java.util.StringTokenizer;
/**
 * A) cita (i snima default) propertije iz restart.properties
 * minibackup=true|false  default ovisno jel localhost (true) ili nije (false)
 * minibackup.dir=... default iz url-a+minibackup \  bolje da ovo svaki put
 * minibackup.file=... default iz url-a            /  determinira ako nije zapisano u properties
 * minibackup.expdays=... broj dana koliko mogu biti stari backup fileovi (default 14)
 * B) provjeri da li danasnji file postoji, ako ne ...
 * C) zajara zadani file (bazu) u minibackup.dir/imefilea.YYYYMMDD.jar
 * D) Mozda opcija u alatima koja bi napravila minibackup na user request
 * @author  andrej
 */
public class raMiniBackup {
  private static String urlTag = "url";
  private File fileToBackup;
  private File backupFile;
  private File backupDir;
  private raDBURLParser urlParser;
  private int expdays = 14;
  private boolean force = false;
  public static boolean lastCopySucces = false;
  public static String lastBackupFile = null;
  public static String lastLog = "";
  /** Creates a new instance of raMiniBackup */
  public raMiniBackup() {
    this(false);
  }
  
  /**
   * Radi minibackup baze 
   * @param _force da li da ga napravi ako je vec napravljen
   */  
  public raMiniBackup(boolean _force) {
    force = _force;
    lastLog = "";
    lastCopySucces = false;
    lastBackupFile = null;
    if (!initProperties()) {
      log("ne pravim kopiju...");
      return;
    }
    delOldBackups();
    makeBackup();
  }
  
  /*
   * cita (i snima default) propertije iz restart.properties
   * minibackup=true|false  default ovisno jel localhost (true) ili nije (false)
   * minibackup.dir=... default iz url-a+minibackup \  bolje da ovo svaki put
   * minibackup.file=.. default iz url-a            /  determinira ako nije zapisano u properties
   */
  private boolean initProperties() {
    try {
      if (!isMiniBackup()) return false;
      if (urlParser==null) urlParser = new raDBURLParser(IntParam.getTag(urlTag));
      
      // filetobackup
      String sFileToBackup = IntParam.getTag("minibackup.file");
      String sep = File.separator.equals("/")?"/":"";
      if (sFileToBackup.equals("")) sFileToBackup = sep+urlParser.getPath();
      try {
        fileToBackup = new File(sFileToBackup);
        if (!fileToBackup.exists()) throw new RuntimeException(sFileToBackup+" ne postoji!");
      } catch (Exception e) {
        log("Neispravan file za backupiranje "+sFileToBackup);
        log("  backupiram "+sep+urlParser.getPath());
        try {
          fileToBackup = new File(sep+urlParser.getPath());
          if (!fileToBackup.exists()) throw new RuntimeException(sFileToBackup+" ne postoji!");
        } catch (Exception e2) {
          log("Ne mogu instancirati file za backup ("+urlParser.getPath()+")... odustajem!");
          log("  ex = "+e2);
          return false;
        }
      }
      
      //backupFile
      String sBackupDir = IntParam.getTag("minibackup.dir");
      if (sBackupDir.equals("")) {
        sBackupDir = (File.separator.equals("/")&&fileToBackup.getParent()!=null?"/":"")
          +(fileToBackup.getParent()==null?".":fileToBackup.getParent())
          +File.separator+"minibackup";
      }
      backupDir = new File(sBackupDir);
      if (!backupDir.exists()) backupDir.mkdirs();
      backupFile = new File(sBackupDir+File.separator+fileToBackup.getName()+"."+new Date(System.currentTimeMillis()).toString()+".zip");
      if (backupFile.exists() && !force) return false;
      
      //expdays
      String sexpdays = IntParam.getTag("minibackup.expdays");
      if (sexpdays.equals("")) {
        expdays = 14;
      } else {
        try {
          expdays = Integer.parseInt(sexpdays);
        } catch (Exception e) {
          expdays = 14;
        }
      }
      
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
  
  private boolean isMiniBackup() throws Exception {
    boolean mb = false;
    String smb = IntParam.getTag("minibackup");
    if (smb.equals("")) {
      String surl = IntParam.getTag(urlTag);
      if (surl.equals("")) {
        return false;
      }
      urlParser = new raDBURLParser(surl);
      mb = ((urlParser.getHost().equals("localhost") || urlParser.getHost().equals("127.0.0.1"))
          && urlParser.getType().equals("interbase"));
//System.out.println("mb = ("+urlParser.getHost().equals("localhost")+" or "+urlParser.getHost().equals("127.0.0.1")+") and "+urlParser.getType().equals("interbase")+"  = "+mb);
//      IntParam.setTag("minibackup", new Boolean(mb).toString());
    } else {
      try {
        mb = Boolean.valueOf(smb).booleanValue();
      } catch (Exception e) {
        mb = false;
      }
    }
    return mb;
  }
  private static void log(String txt) {
    System.out.println(txt);
    lastLog = lastLog + "\n" + txt;
  }
  private void delOldBackups() {
//long stt = System.currentTimeMillis();
//System.out.println("deloldbackups "+stt);
    File[] oldBackupFiles = backupDir.listFiles(new FileFilter() {
      public boolean accept(File file) {
        String name = file.getName();
        if (!name.startsWith(fileToBackup.getName())) { 
//System.out.println("ignoring by fileName "+name);
          return false;
        }
//System.out.println("checking for deletion ... "+name);
        Date expdate = new Date(System.currentTimeMillis()-(expdays*86400000));
        Date filedate = getDateFromFileName(name);
        boolean b = filedate!=null?filedate.before(expdate):false;
//System.out.println(b?"accpeted!":"NOT accepted");
        return b;
      }
    });
//System.out.println(oldBackupFiles.length + " files for deletion detected after "+(System.currentTimeMillis()-stt)+" ms");
    for (int i=0;i<oldBackupFiles.length;i++) {
//System.out.println("deleting "+oldBackupFiles[i]);
//long st2 = System.currentTimeMillis();
      oldBackupFiles[i].delete();
//System.out.println("done ("+(System.currentTimeMillis()-st2)+" ms)");
    }
//System.out.println("done after "+(System.currentTimeMillis()-stt)+" ms");
  }
  
  private Date getDateFromFileName(String name) {
    StringTokenizer nametokens = new StringTokenizer(name,".");
    while (nametokens.hasMoreTokens()) {
      String tok = nametokens.nextToken();
      try {
        Date date = Date.valueOf(tok);
        return date;
      } catch (Exception e) {
      }
    }
    return null;
  }

  private void makeBackup() {
    long then = System.currentTimeMillis();
    if (FileHandler.makeZippedFile(fileToBackup, backupFile)) {
      log("Backup je gotov nakon "+((System.currentTimeMillis()-then)/1000)+" sekundi");
      lastCopySucces = true;
      lastBackupFile = backupFile.getPath();
    } else {
      log("BACKUP neuspjesan! ");
      lastCopySucces = false;
    }
//    log("backupFile = "+backupFile);
//    log("fileToBackup = "+fileToBackup);
    /*
    try {
      java.util.zip.ZipOutputStream zout = new java.util.zip.ZipOutputStream(
        new FileOutputStream(backupFile));
      zout.setLevel(java.util.zip.Deflater.BEST_SPEED);
      zout.putNextEntry(new java.util.zip.ZipEntry(fileToBackup.getName()));
      FileInputStream reader = new FileInputStream(fileToBackup);
      long then = System.currentTimeMillis();
      int len = (int)fileToBackup.length();
      byte[] buffer = new byte[len];
      int readed = reader.read(buffer);
      if (readed != len) throw new RuntimeException("Neispravna duljina baze "+readed+" <> "+len);
      zout.write(buffer);
      zout.close();
      reader.close();
      log("Backup je gotov nakon "+((System.currentTimeMillis()-then)/1000)+" sekundi");
      lastCopySucces = true;
      lastBackupFile = backupFile.getPath();
    } catch (Exception e) {
      lastCopySucces = false;
      log("BACKUP neuspjesan! "+e);
      e.printStackTrace();
    }
    */
  }
  /**
   * T E S T I N G
   */
  public static void main(String[] args) {
    boolean force = false;
    if (args!=null && args.length>0) raMiniBackup.urlTag=args[0];
    if (args!=null && args.length>1) {
      force = args[1].equals("force");
    }
    new raMiniBackup(force);
  }
}
