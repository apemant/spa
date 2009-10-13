/****license*****************************************************************
**   file: raVersionInfo.java
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
 * raVersionInfo.java
 *
 * Created on 2004. sije?anj 05, 13:36
 */

package hr.restart.util.versions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
/**
 *
 * @author  andrej
 */
public class raVersionInfo {
  
  /** Creates a new instance of raVersionInfo */
  protected raVersionInfo() {
  }
  /**
   * Daje sve vrijednosti i nazive tagova u common sekciji manifesta
   * @return sve vrijednosti i nazive tagova u common sekciji manifesta
   */
  public static String getVersionInfo() {
    return getCommonManifestEntry(getJarPathFromClassPath(getAppJarName()),null);
  }
  /**
   * Vrati vrijednost Date taga u manifestu jar file-a
   * Ako nema taga vraca string 'N/A' not available
   * @param jarFile jar file u kojem je manifest
   * @return vrijednost Date taga u manifestu jar file-a ili 'N/A'
   */
  public static String getBuildDateMF(String jarFile) {
  	return getCommonManifestEntry(jarFile, "Date");
  }
  /**
   * Vrati vrijednost Date taga u manifestu ra-spa.jar file-a
   * u classpathu
   * Ako nema taga vraca string 'N/A' not available
   * @return vrijednost Date taga u manifestu jar file-a ili 'N/A'
   */  
  public static String getBuildDateMF() {
    return getBuildDateMF(getJarPathFromClassPath(getAppJarName()));
  }
  /**
   * Vrati vrijednost Version taga u manifestu jar file-a
   * Ako nema taga vraca string 'N/A' not available
   * @param jarFile jar file u kojem je manifest
   * @return vrijednost Version taga u manifestu jar file-a ili 'N/A'
   */
  public static String getBuildVersionMF(String jarFile) {
  	return getCommonManifestEntry(jarFile, "Version");
  }
  /**
   * Vrati vrijednost Version taga u manifestu ra-spa.jar file-a
   * u classpathu
   * Ako nema taga vraca string 'N/A' not available
   * @return vrijednost Version taga u manifestu jar file-a ili 'N/A'
   */  
  public static String getBuildVersionMF() {
    return getBuildDateMF(getJarPathFromClassPath(getAppJarName()));
  }
  /**
   * Vrati vrijednost zadanog taga 'common' sekcije u manifestu jar file-a
   * Ako nema taga vraca string 'N/A' not available
   * @param jarFile jar file u kojem je manifest
   * @param tag tag ciju vrijednost vraca, ako je null vraca formatirani 
   * string svih tagova i vrijednosti (za help-about)  
   * @return vrijednost Date taga u manifestu jar file-a ili 'N/A'
   */
  public static String getCommonManifestEntry(String jarFile, String tag) {
    try {
	  	JarFile jarfile = new JarFile(jarFile,false);
	  	Manifest mf = jarfile.getManifest();
	  	String v = null;
	  	if (tag == null) {
	  	  for (Iterator iter = mf.getAttributes("common").keySet().iterator(); iter.hasNext();) {
          Object key = iter.next();
          v=v==null?"":v;
          v = v+"\n"+key+": "+mf.getAttributes("common").get(key);
        }
	  	} else {
	  	  v = mf.getAttributes("common").getValue(tag);
	  	}
	  	if (v!=null) return v;
		} catch (Exception e) {
		  System.out.println("jarfile = "+jarFile);
			e.printStackTrace();
		}
  	return "N/A";
  }
  /**
   * @deprecated stari nacin trazenja verzije u ra.jar/version.info novo: getBuildVersionMF()
   * @param ra_jar_path
   * @return
   */
  public static String getVersion(String ra_jar_path) {
    ZipEntry ze = null;
    ZipFile zf = null;
    try {
      zf = new ZipFile(ra_jar_path);
      Enumeration zfEntries = zf.entries();
      while (zfEntries.hasMoreElements()) {
        ze = (ZipEntry)zfEntries.nextElement();
        if (ze.getName().toLowerCase().endsWith("/version.info")) break;
        ze = null;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    if (zf == null) return "Unknown :(";
    if (ze == null) return "Unknown :(";
    try {
      InputStream zi = zf.getInputStream(ze);
      StringBuffer buffer = new StringBuffer();
      int ch;
      while ((ch = zi.read()) > -1) {
        buffer.append((char)ch);
      }
      return buffer.toString();      
    } catch (Exception ex) {
      ex.printStackTrace();
      return "Unknown :(";
    }
  }
  /**
   * @deprecated stari nacin trazenja verzije u ra.jar/version.info novo: getBuildDateMF()
   * @param ra_jar_path
   * @return
   */
  public static Timestamp getVersionDate(String ra_jar_path) {
    String sver = getVersion(ra_jar_path);
    try {
      return Timestamp.valueOf(sver);
    } catch (Exception ex) {
      ex.printStackTrace();
      return new Timestamp(0);
    }
  }
  /**
   * Vraca ra-spa.jar ako nije drukcije zapisano u restart.properties 
   * (npr. appjarname=pra-spa.jar)
   * @return ra-spa.jar ako nije drukcije zapisano u restart.properties
   */
  public static String getAppJarName() {
    try {
      String r = (String)Class.forName("hr.restart.util.IntParam")
        .getMethod("getTag", new Class[] {String.class})
        .invoke(null, new Object[] {"appjarname"});
      System.out.println("appjarname = "+r);
      return r.equals("")?"ra-spa.jar":r;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "ra-spa.jar";
  }
  
  public static String getCurrentVersion() {
    return getBuildVersionMF(getJarPathFromClassPath(getAppJarName()));
  }
  /**
   * @deprecated stari nacin trazenja verzije u ra.jar/version.info novo: getBuildDateMF()
   * @return
   */
  public static Timestamp getCurrentVersionDate() {
    return getVersionDate(getCurrentRa_jarPath());
  }
  /**
   * @deprecated
   * @return
   */
  private static String getCurrentRa_jarPath() {
    return getJarPathFromClassPath("ra.jar");
  }
  
	private static String getJarPathFromClassPath(String name) {
		StringTokenizer tokens = getClassPathTokens();
    while (tokens.hasMoreTokens()) {
      String tok = tokens.nextToken();
      if (tok.toLowerCase().endsWith(File.separator+name)) return tok;
    }
    return null;
	}
	private static StringTokenizer getClassPathTokens() {
    String classpath = System.getProperty("java.class.path")+File.pathSeparator+System.getProperty("sun.boot.class.path");
    StringTokenizer tokens = new StringTokenizer(classpath,File.pathSeparator);
    return tokens;
  }
  /**
	 * Searches all 0*.jar files in classpath and writes it's names and entries 
	 * @return
	 */
	public static String getPatchesInfo() {
	  String ret = "SPA verzija "+getCurrentVersion()+"\n";
	  ret = ret + "Informacije o zakrpama:\n";
		StringTokenizer tokens = getClassPathTokens();
    while (tokens.hasMoreTokens()) {
      String tok = tokens.nextToken();
//System.out.println("Looking "+tok);
      if (tok.toLowerCase().endsWith(".jar")) {
        String fn = tok.substring(tok.lastIndexOf(File.separatorChar)+1);
//System.out.println("Examine jar file "+fn);
        if (fn.startsWith("0")) {
//System.out.println("Processing patch "+fn);
	        try {
	          JarFile jar = new JarFile(tok);
	          ret = ret + "-----------------------------------\n"
	          					+fn+"\n"
	          					+ "-----------------------------------\n"
	          					+ "  content: \n";
	          Enumeration jarentries = jar.entries();
	          while (jarentries.hasMoreElements()) {
              JarEntry element = (JarEntry)jarentries.nextElement();
//System.out.println("Processing entry "+element);
							if (!element.isDirectory()) {
							  ret = ret + "  "+new Timestamp(element.getTime())+" "+element+"\n";
							}
	          }
	          ret = ret + "-----------------------------------\n";
	        } catch (IOException e) {
	          // TODO Auto-generated catch block
	          e.printStackTrace();
	        }
        }
      }
    }	  
    return ret;
	}
public static void main(String[] args) {
    //System.out.println("raVersioninfo "+raVersionInfo.getCurrentVersionDate());
  	System.out.println(raVersionInfo.getBuildVersionMF());//"/home/andrej/eworkspace/devel/projects/spa/spa-jars/ra-spa.jar"));
  }
}
