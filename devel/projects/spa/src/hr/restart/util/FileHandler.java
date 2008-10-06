/****license*****************************************************************
**   file: FileHandler.java
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

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileHandler {
  public FileOutputStream fileOutputStream;
  private Writer writer;
  public FileInputStream fileInputStream;
  private InputStreamReader inputStreamReader;
  private Reader reader;
  private String defEnc;
  private String fileName;
  private java.beans.PropertyChangeSupport propChSupp;
/**
 *
 * @param fileNameC path od filea
 */
  public FileHandler(String fileNameC) {
    this(fileNameC,true);
  }

  protected FileHandler(String fileNameC,boolean dbEnc) {
    if (dbEnc) //defEnc = hr.restart.baza.dM.getDataModule().getDatabase1().getConnection().getProperties().getProperty("charSet");
    defEnc = "Cp1250";
    if (defEnc == null) defEnc = System.getProperty("file.encoding");
    fileName = fileNameC;
  }
  public void addFileChangeListener(PropertyChangeListener pchLis) {
    if (propChSupp == null) propChSupp = new PropertyChangeSupport(this);
    propChSupp.addPropertyChangeListener("filechanged",pchLis);
  }
  public void removeFileChangeListener(PropertyChangeListener pchLis) {
    if (propChSupp == null) return;
    propChSupp.removePropertyChangeListener("filechanged",pchLis);
    if (propChSupp.hasListeners("filechanged")) propChSupp = null;
  }
  private void allToNull() {
    fileOutputStream = null;
    writer = null;
    fileInputStream = null;
    inputStreamReader = null;
    reader = null;
  }
/**
 * Otvara file za writanje
 * @param encoding npr 'cp1250' ili 'UTF8'
 */
  public void openWrite(String encoding) {
    allToNull();
    try {
      fileOutputStream = getListenedFileOutputStream();
      writer = new OutputStreamWriter(fileOutputStream, encoding);
    } catch (Exception e) {
//      System.out.println("FileHandler.openWrite Exception: "+e);
    }
  }

  private FileOutputStream getListenedFileOutputStream() throws IOException {
//    File _tmp_f = new File(fileName);
//    System.out.println("FileHandler.openWrite.getListenedFileOutputStream: "+_tmp_f.getAbsolutePath());
//    return new FileOutputStream(_tmp_f) {
    return new FileOutputStream(fileName);/* {
      public void write(int n) throws IOException {
        super.write(n);
//        if (propChSupp != null) propChSupp.firePropertyChange("filechanged","",newVal);
      }
      public void write(byte[] b,int c,int d) throws IOException {
        super.write(b,c,d);
//        if (propChSupp != null) propChSupp.firePropertyChange("filechanged","",newVal);
      }
      public void write(byte[] b) throws IOException {
        super.write(b);
//        if (propChSupp != null) propChSupp.firePropertyChange("filechanged",true,false);
      }
    };*/
  }
  
  private void fireLinePropertyChange(byte[] b) {
    
//    if (propChSupp != null) propChSupp.firePropertyChange("filechanged","",line);
  }
/**
 * Otvara file za writanje sa defaultnim encodingom
 */
  public void openWrite() {
    openWrite(defEnc);
  }
/**
 * Zatvara file
 */
  public void close() {
    try {
      if (writer != null) {
        writer.close();
      } else if (reader != null) {
        reader.close();
      } else {
        throw new java.lang.Exception("File not opened!");
      }
    } catch (Exception e) {
//      System.out.println("FileHandler.close Exception: "+e);
    }
  }
/**
 * Upisuje zadani string u file
 * @param str
 */
  public void write(String str) {
    try {
      write2(str);
    } catch (Exception e) {
//      System.out.println("FileHandler.write Exception: "+e);
    }
  }

  public void writeln(String str) {
    try {
      write2(str.concat(System.getProperty("line.separator","\n")));
    }
    catch (Exception e) {
//      System.out.println("FileHandler.writeln Exception: "+e);
    }

  }

  private void write2(String str) throws Exception {
    if (writer != null) {
      writer.write(str);
    } else {
      throw new java.lang.Exception("File not opened for write");
    }
  }

  public String getFileName() {
    return fileName;
  }
  public void openRead(String encoding) {
    allToNull();
    try {
//      File _tmp_f = new File(fileName);
//System.out.println("FileHandler.openRead: "+_tmp_f.getAbsolutePath());
//      fileInputStream = new FileInputStream(_tmp_f);
      fileInputStream = new FileInputStream(fileName);
      inputStreamReader = new InputStreamReader(fileInputStream,encoding);
      reader = new BufferedReader(inputStreamReader);
    } catch (Exception e) {
//      System.out.println("FileHandler.openRead Exception: "+e);
    }
  }

  public void openRead() {
    openRead(defEnc);
  }

  public String read() {
    try {
      return read2();
    } catch (Exception e) {
//      System.out.println("FileHandler.read Exception: "+e);
      return "";
    }
  }

  private String read2() throws Exception {
    if (reader != null) {
      StringBuffer buffer = new StringBuffer();
      int ch;
      while ((ch = reader.read()) > -1) {
        buffer.append((char)ch);
      }
      return buffer.toString();
    } else {
      throw new java.lang.Exception("File not opened for read");
    }
  }

  public static void writeConverted(String str,String filename,String enc) {
    try {
      FileHandler FH = new FileHandler(filename);
      FH.openWrite();
      java.io.OutputStreamWriter OSW;
      if (enc == null) {
        OSW = new java.io.OutputStreamWriter(FH.fileOutputStream);
      } else {
        OSW = new java.io.OutputStreamWriter(FH.fileOutputStream,enc);
      }
      OSW.write(str);
      OSW.close();
      FH.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String readFile(String filename, String enc) {
    FileHandler file = new FileHandler(filename);
    if (enc == null) {
      file.openRead();
    } else {
      file.openRead(enc);
    }
    String str = file.read();
    file.close();
    return str;
  }

  public static String readFile(String filename) {
    return readFile(filename,null);
  }

  public static String convert(String str, String enc) {
    try {
      byte[] bytes = sun.io.CharToByteConverter.getConverter(enc).convertAll(str.toCharArray());
      return new String(bytes);
    } catch (Exception e) {
//      System.out.println("Conversion failed Exeption: "+e);
      return str;
    }
  }
/**
 * Napuni propertiese zadane drugim parametrom iz filea zadanog prvim parametrom
 */
  public static void loadProperties(String file,Properties props) {
    loadProperties(file,props,true);
  }
  public static void loadProperties(String file,Properties props,boolean dbEnc) {
    try {
      FileHandler FH = new FileHandler(file,dbEnc);
      FH.openRead();
      props.load(FH.fileInputStream);
      FH.close();
    } catch (Exception e) {
//      System.out.println("FileHandler.getProperties Exception: "+e);
    }
  }

/**
 * Vrati Properties klasu napunjenu iz zadanog file-a
 */
  public static Properties getProperties(String file) {
    return getProperties(file,true);
  }
  public static Properties getProperties(String file,boolean dbEnc) {
    Properties props = new Properties();
    loadProperties(file,props,dbEnc);
    return props;
  }
  public static String getSHA1(String fileName) {
    try {
       FileHandler f = new FileHandler(fileName);
      f.openRead();
      return getSHA1(f.fileInputStream);
// 7fd1687ad5-14c91d149a-9be943e167-43c2d02883
  }
  catch (Exception ex) {
    ex.printStackTrace();
    return null;
  }

  }
  public static String getSHA1(FileInputStream fin) {
    try {
      java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA" );
      int len = fin.available();
      byte[] b = new byte[len];
      fin.read(b);
      md.update(b);
      byte[] digest = md.digest();
      String sha1 = "";
      for (int i = 0; i < digest.length; i++) {
        int val1 = new Byte(digest[i]).intValue();
        String str = Integer.toHexString(val1);
//        System.out.print("str = "+str);
        if (str.length() == 8) {
          str = str.substring(6);
        } else if (str.length() == 1) {
          str = "0".concat(str);
        }
//        System.out.println(" = "+str);
        sha1 = sha1.concat(str);
      }
      return sha1.toUpperCase();
      
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
/**
 * snima zadane propertie u zadani file
 */
  public static void storeProperties(String file,Properties props) {
    storeProperties(file,props,true);
  }
  
  public static void storeProperties(String file,Properties props, boolean dbEnc) {
    FileHandler FH = new FileHandler(file,dbEnc);
    FH.openWrite();
    try {
      props.store(FH.fileOutputStream,"");
      FH.fileOutputStream.close();
      FH.close();
//if (!file.endsWith(".properties")) new Throwable().printStackTrace();
    } catch (Exception e) {
      System.out.println("FleHandler.storeProperties Exception: "+e);
    }
  }
  /**
   * @return Returns the reader.
   */
  public Reader getReader() {
    return reader;
  }
  /**
   * @return Returns the writer.
   */
  public Writer getWriter() {
    return writer;
  }
//file handling
  /**
   * Brise sve fileove i direktorije u zadanom direktoriju
   * @param dir zadani direktorij
   */  
  public static void delTree(File dir) {
    File[] files = dir.listFiles();
    for (int i=0; i<files.length; i++) {
      if (files[i].isDirectory()) delTree(files[i]);
      files[i].delete();
    }
  }
  
  /**
   * Kopira gomilu fileova sa jedne lokacije na drugu
   * tako da sacuva njihov path relativan na sourcedir i destdir.
   * <pre>
   * npr. imamo HashSet fileset koji sadrzi fileove:
   *    c:\moj\source\dir\hr\restart\pl\raPero.java
   *    c:\moj\source\dir\hr\restart\robno\raJura.java
   *    c:\moj\source\dir\hr\restart\robno\raStipe.java
   *    c:\moj\source\dir\hr\restart\bl\raIvica.java
   * i ako pozovemo 
   *   copyFiles(fileset, "c:\moj\source\dir", "c:\njegov\dest\folder\")
   * kopirat ce se:
   * c:\moj\source\dir\hr\restart\pl\raPero.java u c:\njegov\dest\folder\hr\restart\pl\raPero.java
   * c:\moj\source\dir\hr\restart\robno\raJura.java u c:\njegov\dest\folder\hr\restart\robno\raJura.java
   * c:\moj\source\dir\hr\restart\robno\raStipe.java u c:\njegov\dest\folder\hr\restart\robno\raStipe.java
   * c:\moj\source\dir\hr\restart\bl\raIvica.java u c:\njegov\dest\folder\hr\restart\bl\raIvica.java
   *
   * svi destination direktoriji ce se napraviti ako ne postoje ukljucujuci i destRootDir
   *
   * </pre>
   * @param files fileovi koje treba kopirati
   * @param sourceRootDir direktorij iz kojeg treba kopirati
   * @param destRootDir direktorij u koji treba kopirati
   * @return jel uspio ili nije uz printstacktrace
   */  
  public static boolean copyFiles(HashSet files, String sourceRootDir, String destRootDir) {
//String difftest="";
    for (Iterator i = files.iterator(); i.hasNext(); ) {
      File item = (File)i.next();
      String destpath = new VarStr(item.getAbsolutePath()).replace(sourceRootDir,destRootDir).toString();
      File destFile = new File(destpath);
      /**@todo: nastavi */
      //        System.out.println("sourceFile = "+item.getAbsolutePath());
      //        System.out.println("destFile = "+destFile.getAbsolutePath());
      destFile.mkdirs();
      if (destFile.exists()) destFile.delete();
      try {
//difftest=difftest+"diff -q "+item.getAbsolutePath()+" "+destFile.getAbsolutePath()+"\n";
//System.out.println(item.getAbsolutePath()+" -> "+destFile.getAbsolutePath());        
        destFile.createNewFile();
        FileHandler fw = new FileHandler(destFile.getAbsolutePath());
        fw.openWrite();
        FileHandler fr = new FileHandler(item.getAbsolutePath());
        fr.openRead();
        int ch;
        while ((ch = fr.reader.read()) > -1) {
          fw.writer.write(ch);
        }
        fw.close();
        fr.close();
//System.out.println("1 fajls kopid");
      } catch (Exception ex) {
        ex.printStackTrace();
        return false;
      }
    }
//FileHandler.writeConverted(difftest, "difftest.sh", null);
    return true;
  }
  
  /**
   * Zips fileToZip into one zippedFile
   * @see makeZippedFile(File[], File)
   * @param fileToZip
   * @param zippedFile
   * @return true if success, false otherwise
   */
  public static boolean makeZippedFile(File fileToZip, File zippedFile) {
    return makeZippedFile(new File[] {fileToZip}, zippedFile);
  }
  
  /**
   * Zips filesToZip into one zippedFile
   * @param filesToZip
   * @param zippedFile
   * @return true if success, false otherwise
   */
  public static boolean makeZippedFile(File[] filesToZip, File zippedFile) {
    try {
      java.util.zip.ZipOutputStream zout = new java.util.zip.ZipOutputStream(
        new FileOutputStream(zippedFile));
      zout.setLevel(java.util.zip.Deflater.BEST_SPEED);
      for (int i = 0; i < filesToZip.length; i++) {
        File fileToZip = filesToZip[i];
	      zout.putNextEntry(new java.util.zip.ZipEntry(fileToZip.getName()));
	      FileInputStream reader = new FileInputStream(fileToZip);
	      int len = (int)fileToZip.length();
	      byte[] buffer = new byte[len];
	      int readed = reader.read(buffer);
	      if (readed != len) throw new RuntimeException("Neispravna duljina baze "+readed+" <> "+len);
	      zout.write(buffer);
	      reader.close();
      }
      zout.close();
      
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    
  }
  /**
   * Zips filesToZip into one zippedFile and encrypts it with given encrypter
   * @see Encrypter
   * @see DesEncrypter
   * @param encrypter encrypter to encrypt file
   * @param filesToZip
   * @param zippedFile
   * @return true if success, false otherwise
   */
  public static boolean makeZippedFile(Encrypter encrypter, File[] filesToZip, File zippedFile) {
    try {
      File plainZipFile = new File(zippedFile.getAbsolutePath()+"__plain.tmp");
      if (makeZippedFile(filesToZip, plainZipFile)) {
        encrypter.encrypt(plainZipFile, zippedFile);
        plainZipFile.delete();
        return true;
      }
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
  /**
   * Unzips specified file into specified dir
   * @param fileToUnzip file to unzip
   * @param dirToUnzip dir to unzip
   * @return rue if success, false otherwise
   */
  public static boolean unZipFile(File fileToUnzip, File dirToUnzip) {
    try {
      if (!dirToUnzip.exists()) dirToUnzip.mkdirs();
      if (!dirToUnzip.isDirectory()) throw new IllegalArgumentException(dirToUnzip.getAbsolutePath()+" is not a directory!");
      int bufferSize = 2048;
      byte[] bufferData = new byte[bufferSize];
      ZipInputStream zin = new ZipInputStream(
          new BufferedInputStream(new FileInputStream(fileToUnzip)));
      ZipEntry entry;
      while ((entry = zin.getNextEntry()) != null) {
        FileOutputStream fos = new FileOutputStream(dirToUnzip.getAbsolutePath()+File.separator+entry.getName());
        BufferedOutputStream dest = new BufferedOutputStream(fos, bufferSize);
        int count;
	      while ((count = zin.read(bufferData, 0, bufferSize)) 
	        != -1) {
	         dest.write(bufferData, 0, count);
	      }
	      dest.flush();
	      dest.close();
      }
      
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
  public static boolean unZipFile(Encrypter decripter, File fileToUnzip, File dirToUnzip) {
    try {
      File plainZipFile = new File(fileToUnzip.getAbsolutePath()+"__plain.tmp");
      decripter.decrypt(fileToUnzip, plainZipFile);
      boolean b = unZipFile(plainZipFile, dirToUnzip);
      plainZipFile.delete();
      return b;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

	public static void copy(File src, File dest) throws IOException {
		if (!dest.exists()) dest.createNewFile();
		FileInputStream fis = new FileInputStream(src);
		FileOutputStream fos = new FileOutputStream(dest);
		byte[] buffy = new byte[2048];
		int cnt = 0;
		while ((cnt = fis.read(buffy)) != -1) {
			fos.write(buffy, 0, cnt);
		}
		fos.flush();
		fos.close();
	}
}