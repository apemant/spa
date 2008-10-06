/****license*****************************************************************
**   file: ftpVersionNotifier.java
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
 * ftpVersionNotifier.java
 *
 * Created on 2004. veljaèa 20, 12:01
 */

package hr.restart;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
/**
 *
 * @author  andrej
 */
public class ftpVersionNotifier {
  static boolean debug = false;
  String dirName;
  FileInfo[] oldlist;
  FileInfo[] newlist;
  
  public void makeVersions() {
    ftpVersionWorker.makeVersionsFile(dirName);
  }
  public boolean runTimerTask() {
    if (debug) System.out.println(new Date(System.currentTimeMillis()).toString()+": checking "+dirName);
    File watchDir = new File(dirName);
    if (oldlist == null) oldlist = new FileInfo[0];
    newlist = getInfoList(watchDir);
    if (isDirChanged(watchDir)) {
      makeVersions();
      oldlist = getInfoList(watchDir);
      newlist = getInfoList(watchDir);
      return true;
    }
    return false;
  }
  private FileInfo[] getInfoList(File dir) {
    File[] files = dir.listFiles();
    Arrays.sort(files);
    FileInfo[] ret = new FileInfo[files.length];
    for (int i=0; i<files.length; i++) {
      ret[i] = new FileInfo(files[i]);
    }
    return ret;
  }
  private boolean isDirChanged(File watchDir) {
    if (!Arrays.equals(oldlist, newlist)) return true;
/*    for (int i=0; i<newlist.length; i++) {
      if (newlist[i].length()!=oldlist[i].length()) return true;
      if (newlist[i].lastModified()!=oldlist[i].lastModified()) return true;
      if (debug) {
        System.out.println(newlist[i].getName()+" :");
        System.out.println("-- "+oldlist[i].getName());
        System.out.println("length :: "+newlist[i].length()+" = "+oldlist[i].length());
        System.out.println("lastModified :: "+newlist[i].lastModified()+" = "+oldlist[i].lastModified());
      }
    }*/
    return false;
  }
  
  /**
   * Creates a new instance of ftpVersionNotifier
   * @param _dirName
   */  
  public ftpVersionNotifier(String _dirName) {
    dirName = _dirName;
  }
  /**
   * Creates a new instance of ftpVersionNotifier
   * @deprecated use hr.restart.util.raCron and ftpCronVersionNotifier
   * @param _dirName
   * @param _period
   */
  public ftpVersionNotifier(String _dirName, long _period) {
    dirName = _dirName;
    start(_period);
  }
  private void start(long period) {
    System.out.println(new java.util.Date(System.currentTimeMillis())+":: Starting ftpVersionNotifier ");
    System.out.println("              :: dir    = "+dirName);
    System.out.println("              :: period = "+period);
    TimerTask task = new TimerTask() {
      public void run() {
        runTimerTask();
      }
    };
    Timer timer = new Timer();
    timer.schedule(task,0, period);
  }
  
  class FileInfo {
    String name;
    long lastModified;
    long length;
    public FileInfo(File f) {
      name = f.getAbsolutePath();
      lastModified = f.lastModified();
      length = f.length();
    }
    public boolean equals(Object o) {
      if (o instanceof FileInfo) {
        FileInfo finfo = (FileInfo)o;
        return name.equals(finfo.name) 
          && lastModified == finfo.lastModified 
          && length == finfo.length;
      }
      if (o instanceof File) return equals(new FileInfo((File)o));
      return super.equals(o);
    }
  }
  
  private static void printUsage() {
    System.out.println("Usage ftpVersionNotifier <dir> [long period] [\"debug\"] [logdir/]");
  }
  
  /**
   * Starts ftpVersionNotifier in deprecated way
   * @deprecated use hr.restart.util.raCron and ftpCronVersionNotifier
   * @param args <dir> [long period] [\"debug\"] [logdir/]
   */  
  public static void main(String[] args) {
    try {
      ftpVersionNotifier.debug = (args.length>2 && args[2]!=null && args[2].equals("debug"));
      long period;
      if (args.length > 1) period = Long.parseLong(args[1]);
      else period = 1000;
      String dir = args[0];
      String logdir = "";
      if (args.length>2) logdir = args[2];
      ftpStart.redirectSystemOut(logdir+"versionnotifier-"+System.currentTimeMillis()+".log");
      new ftpVersionNotifier(dir,period);
    } catch (Exception ex) {
      printUsage();
      ex.printStackTrace();
      System.exit(-1);
    }
  }
}
