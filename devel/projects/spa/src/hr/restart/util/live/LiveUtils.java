/****license*****************************************************************
**   file: LiveUtils.java
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
package hr.restart.util.live;

import hr.restart.baza.kreator;
import hr.restart.util.FileHandler;
import hr.restart.util.raProcess;
import hr.restart.util.startFrame;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;


/**
 * @author andrej
 * <pre>
 * Klasa koja je niz statickih metoda koje pruzaju automatiku za 
 * sistemske i instalacijske funkcije: 
 * - punjenje i azuriranje baze
 * - dohvacanje default propertiesa
 * - live update (u svijetloj buducnosti)
 * - itd itd
 * </pre>
 */
public class LiveUtils {
	private static String checkPropfile = "restart.properties";
	private static String[] installPropsFiles = {
	    	checkPropfile,
	    	"knjig_img.properties",
	    	"laf.properties",
	    	"login.properties",
	    	"themes.properties"
	    };//dodati ako jos koji file treba exstrahirati iz jara

	//puni se samo ako je checkPropfile u classpathu a nije na file sistemu, znaci u jaru
	private static URL checkPropFileURL; 

  /**
   * 
   */
  protected LiveUtils() {
  }
  /**
   * metoda koju treba pozveti prije stvarnog starta aplikacije i ona bi trebala:
   * <pre>
   * 1. extrahirati propertiese
   * 2. extrahirati defaultne podatke
   * 3. konektirati se na bazu
   * 4. uloadati defaultne podatke
   * 5. nastaviti dalje s radom 
   * </pre>
   */
  public static void liveStart() {
    if (checkExistsProps() == 2) {//samo ako je pronasao propertiese u samo u jaru
      copyPropsFromCP(); //1.
      if (extractDefDataUI()) { //2.
        loadDefData(); //3. i 4.
      }
    }
  }

  /**
   * @return
   */
  private static boolean extractDefDataUI() {
    res = false;
    startFrame.raLookAndFeel();
    raProcess.runChild(new Runnable() {
      public void run() {
        res = extractDefData();
      }
    });
    return res;
  }
  private static boolean res;

  /**
   * uloadava podatke
   */
  private static void loadDefData() {
    kreator.getKreator().initialize(null);
  }
  
  /**
   * extrahira direktorij defdata iz jara
   */
  private static boolean extractDefData() {
    JarFile jarF = getJarFileFromUrl(checkPropFileURL);
    Enumeration allEntries = jarF.entries();
    if (new File("defdata").mkdir()) {
	  	try {
        while( allEntries.hasMoreElements() ) {
          ZipEntry zipEntry = (ZipEntry)allEntries.nextElement();
          if (zipEntry.getName().startsWith("defdata/") && !zipEntry.getName().equals("defdata/")) {
            copyFromUrl(Class.class.getResource("/"+zipEntry.getName()),zipEntry.getName());
          }
        }
        return true;
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    } else {
      System.out.println("directory defdata allready exist. Nothing expanded");
      return false;
    }
  }
  /**
   * <pre>
   * Provjerava da li je aplikacija setupirana. Konkretno: ima li file-a restart.properties
   * ako ga nema nigdje najbolje niti ne startati aplikaciju:)
   * ako postoji u jaru, znaci radi se o samoinstalirajucem paketu, treba ga exstrahirati, 
   * kreirati i napuniti in memory bazu (hsql) sa defaultnim podacima (ako postoje)
   * </pre> 
   * @return postoji li restart.properties ili ne u start diru 0-ne postoji nigdje, 1-postoji na file sistemu, 2-postoji u classpathu (jaru) 
   */
  public static int checkExistsProps() {
    if (checkExistsPropsFS()) return 1;
    if (checkExistsPropsCP()) return 2;
    return 0;
  }
  /**
   * Provjerava postoje li propertiesi u class pathu
   * @return true postoje, false ne
   */
  private static boolean checkExistsPropsCP() {
    try {
      checkPropFileURL = Class.class.getResource("/"+checkPropfile);
      return checkPropFileURL!=null;
    } catch (Exception e) {
      System.out.println(e);
      return false;
    }
  }
  /**
   * Provjeva postoje li propertiesi na sistemu 
   * @return true postoje, false ne
   */
  private static boolean checkExistsPropsFS() {
    try {
      return new File(checkPropfile).exists();
    } catch (Exception e) {
      System.out.println(e);
      return false;
    }
  }
  /**
   * kopira sve properties fileove iz jara na disk
   */
  public static void copyPropsFromCP() {
    for (int i = 0; i < installPropsFiles.length; i++) {
      copyFromUrl(Class.class.getResource("/"+installPropsFiles[i]),installPropsFiles[i]);
    }
  }
  /**
   * @param resource
   * @param string
   */
  private static void copyFromUrl(URL resource, String filenameTo) {
    BufferedReader reader = getReader(resource);
    if (reader != null) {
      writeToFile(filenameTo, reader);
    } 
  }
  /**
   * @param filenameTo
   * @param reader
   * @throws IOException
   */
  private static void writeToFile(String filenameTo, BufferedReader reader) {
    try {
      FileHandler fh = new FileHandler(filenameTo);
      fh.openWrite();
      int ch;
      while ((ch = reader.read()) > -1) {
        fh.getWriter().write(ch);
      }
      fh.close();
      reader.close();
    } catch (Exception e) {
      System.out.println("LiveUtils.writeToFile: unable to write to file "+filenameTo+" :"+e);
    }
  }
  /**
   * @param resource
   */
  private static BufferedReader getReader(URL resource) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream()));
      return reader;
    } catch (Exception e) {
      System.out.println("LiveUtils.getReader: unable to open resource "+resource+" :"+e);
      return null;
    }
  }

  /**
   * Ne koristi se za sada
   * @param resURL
   * @return
   */
  private static JarFile getJarFileFromUrl(URL resURL) {
    if (resURL == null) throw new RuntimeException("Resource not found");
    System.out.println("Protocol:: "+resURL.getProtocol());
    System.out.println("Path:: "+resURL.getPath());
    String jarPath = new StringTokenizer(resURL.getPath(),"!").nextToken();
    System.out.println(jarPath);
    jarPath = jarPath.substring(5);
    System.out.println(jarPath);
    File fileJar = new File(jarPath);
    System.out.println("fileJar.exist? "+fileJar.exists());
    try {
      JarFile jarFile = new JarFile(fileJar);
      return jarFile;
//    JarFile jarfile = new JarFile()
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  }
  /**
   * Za test postojecih funkcija
   * @param args
   */
  public static void main(String[] args) {
//    System.out.println(System.getProperty("java.class.path"));
    System.out.println("*************************************");
/*    System.out.println("LiveUtils.checkExistsProps()::"+checkExistsProps());
    System.out.println("checkExistsPropsCP()::"+checkExistsPropsCP());
    System.out.println("checkExistsPropsFS()::"+checkExistsPropsFS());*/
    liveStart();
  }
}
