/****license*****************************************************************
**   file: Main.java
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
/**
 * 
 */
package hr.restart.startup;

import hr.restart.ftpStart;
import hr.restart.ftpVersionWorker;
import hr.restart.util.FileHandler;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author andrej
 * Does everything to start SPA
 * - copies jars from ftp server if needed (ftpStart)
 * - adjusts classpath
 * - loads OS dependent tricks (AppleUtil)
 * - accepts patches, pilot reports etc in args or apple drag'n'drop
 * - uses startup.properties in user.dir
 */
public class Main {

  /**
   * @param args
   */
  public static void main(String[] args) {
    //properties
    Properties props = FileHandler.getProperties("startup.properties");
    
    //ftpStart
    if (new Boolean(props.getProperty("ftpStart","false")).booleanValue()) {
      try {
        ftpStart.redirectSystemOut("ftpstart-"+System.currentTimeMillis()+".log");
        ftpStart.showSplash();
        ftpVersionWorker.transferNewResources();
        ftpStart.hideSplash();
      } catch (Exception ex) {
        javax.swing.JOptionPane.showMessageDialog(null, "Greska pri provjeri verzije: "+ex.getMessage(), "Pozor!", javax.swing.JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
        System.exit(0);
      }        
    }
    
    try {
      startUP(args, props.getProperty("libdir","lib"));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.exit(0);
    }
  }

  private static void startUP(String[] args, String lib) throws Exception {
    File libdir = new File(lib);
    if (!libdir.isDirectory()) {
      throw new RuntimeException("Invalid lib directory "+lib);
    }
    File[] jars = libdir.listFiles();
    Arrays.sort(jars);
    URL[] urls = new URL[jars.length];
    String cp = "";
    for (int i = 0; i < jars.length; i++) {
      urls[i] = jars[i].toURL();
      cp = cp + jars[i].getAbsolutePath()+File.pathSeparator;
    }
    String oldcp = System.getProperty("java.class.path");
    System.setProperty("java.class.path",cp+File.pathSeparator+oldcp);
    ClassLoader cloader = new URLClassLoader(urls, Object.class.getClassLoader());
    Thread.currentThread().setContextClassLoader(cloader);
    Class start = cloader.loadClass("hr.restart.start");
    //Class start = Class.forName("hr.restart.start");
    Method main = start.getMethod("main", new Class[] {String[].class});
    main.invoke(null, new Object[] {args});
  }

}
