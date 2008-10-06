/****license*****************************************************************
**   file: raCron.java
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
 * raCron.java
 *
 * Created on 2004. svibanj 25, 09:45
 */

package hr.restart.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Timer klasa koja prihvaca abstraktne klase i vrti ih u delayu, logira, podize ih kad puknu, ...
 * nalazi ih pri bootu preko classbrowsera
 * postoji hook kad rokne da se opet podigne
 * @author andrej
 */
public class raCron {
  PrintStream outlog;
  private Timer timer = new Timer();
  private Properties props;
  private String propsFile;
  private static raCron cronInstance;
  String logfile;
  private HashSet tasks = new HashSet();
  /**
   * Klasu je potrebno instancirati sa nazivom file-a u kojem su propertiesi
   */  
  protected raCron() {
    
  }
  /**
   * Creates a new instance of raCron
   * @param _propsFile system dependent file name where properties are stored
   */
  public raCron(String _propsFile) {
    cronInstance = this;
    //procitaj propertiese
    propsFile = _propsFile;
    props = FileHandler.getProperties(propsFile);
    //startaj log
    startLog();
    //instanciraj i sheduliraj cronTaskove
    findAndSchedule();
  }
  
  /**
   * Returns instance of raCron for calling public methods whitin raCronTasks
   * @return instance of raCron. If constructor raCron(String) is never called
   * returns null
   */  
  public static raCron getInstance() {
    return cronInstance;
  }
  
  /**
   * Returns value for specified key if value is found in propsFile.
   * If no value found returns defaultValue and stores it in propsFile.
   * @param key key for property
   * @param defaultValue devault value if not found
   * @return value or default value for specified key
   * @see #storeProperty(String, String)
   * @see java.util.Properties#getProperty(String, String)
   */  
  public String getProperty(String key, String defaultValue) {
    String value = props.getProperty(key);
    if (value == null) {
      value = defaultValue;
      storeProperty(key, value);
    }
    return value;
  }
  
  /**
   * Stores property in propsFile
   * @param key key of property
   * @param value value of property
   * @see #getProperty(String, String)
   */  
  public void storeProperty(String key, String value) {
    props.setProperty(key, value);
    FileHandler.storeProperties(propsFile, props);
  }
  private void startLog() {
    try {
      logfile = props.getProperty("cron.log","racron.log");
      FileOutputStream fos = new FileOutputStream(logfile);
      outlog = new PrintStream(fos);
      log("logging started");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private String[] nostartTasks = null;
  private String[] getNostartTasks() {
    if (nostartTasks!=null) return nostartTasks;
    String nostartProperty = getProperty("cron.nostart", "");
    if (nostartProperty.equals("")) {
      nostartTasks = new String[0];
      return nostartTasks;
    }
    StringTokenizer st = new StringTokenizer(nostartProperty,",");
    ArrayList list = new ArrayList();
    while (st.hasMoreTokens()) {
      list.add(st.nextToken().trim());
    }
    nostartTasks = (String[])list.toArray(new String[]{});
    return nostartTasks;
  }
  
  private boolean isStartTask(Class cct) {
    for (int i=0;i<getNostartTasks().length;i++) {
      if (cct.getName().equals(getNostartTasks()[i])) {
        logvisible("*** "+cct.getName()+" is disabled in properties");
        return false;
      }
    }
    return true;
  }
  private void findAndSchedule() {
    logvisible("Finding cron tasks ...");
    raCBrowser cbrw = new raCBrowser(hr.restart.util.raCronTask.class,null,null,new String[] {"Cron"},new String[] {"hr.restart.util.raCronTask"});
    logvisible(cbrw.getClasses().length+" found!");
    if (cbrw.getClasses().length == 0) {
      logvisible("No tasks defined, exiting");
      System.exit(0);
    }
    for (int i=0;i<cbrw.getClasses().length;i++) {
      Class cct = cbrw.getClasses()[i];
      if (isStartTask(cct)) {
        try {
          logvisible("instancing "+cct.getName()+"...");
          raCronTask rct = (raCronTask)cct.newInstance();
          logvisible("scheduling "+rct+" ...");
          schedule(rct);
          addTask(rct);
        } catch (Exception e) {
          logvisible(" Failed : "+e+" :: "+e.getMessage());
        }
      }
    }
    checkTasks();
    logvisible("----- "+tasks.size()+" TASKS STARTED -----");
  }
  
  /**
   * Writes specified string to specified PrintStream
   * etc. log(System.out, "aaa") equals System.out.println("aaa")
   * @param out PrintStream for writing
   * @param text text to write
   */  
  public void log(PrintStream out, String text) {
    out.print(new Date(System.currentTimeMillis()).toString());
    out.print(" :: ");
    out.println(text);
  }
  
  /**
   * Writes specified string to a log file defined in propsFile
   * @param text text to write
   */  
  public void log(String text) {
    log(outlog, text);
  }
  
  /**
   * Writes specified text to System.out and to log file
   * @param text text to write
   */  
  public void logvisible(String text) {
    log(System.out,text);
    log(text);
  }
  private void addTask(raCronTask rct) {
    tasks.add(rct);
  }
  private void removeTask(raCronTask rct) {
    tasks.remove(rct);
    checkTasks();
  }
  private void checkTasks() {
    if (tasks.isEmpty()) {
      log("No active tasks. Shuting down...");
      System.exit(0);
    }    
  }
  
  private void schedule(raCronTask rct) {
    long delay = rct.getDelay();
    long period = rct.getPeriod();
    Date startTime = rct.getStartTime();
    try {
      if (startTime == null) {
        timer.schedule(getRealTask(rct,false), delay, period);
      } else if (period > 0) {
        timer.schedule(getRealTask(rct,false), startTime, period);
      } else {
        timer.schedule(getRealTask(rct,true), startTime);
      }
    } catch (Exception e) {
      logvisible("ERROR failed scheduling "+rct+" exception :: "+e.getMessage());
      e.printStackTrace();
      e.printStackTrace(outlog);
    }
  }
  private TimerTask getRealTask(final raCronTask rct, final boolean cancelAfterExec) {
    TimerTask realTask = new TimerTask() {
      public void run() {
        try {
          rct.run();
          if (rct.logLine()!=null) log(rct.logLine());
          if (cancelAfterExec) cancel();
        } catch (Exception e) {
          if (!rct.onException(e)) {
            log("execution of task "+rct+" failed! Cancelig task!");
            cancel();
          }
        }
      }
      public boolean cancel() {
        boolean ret = super.cancel();
        removeTask(rct);
        return ret;
      }
    };
    return realTask;
  }
  /**
   * Renames log to log file name with specified extension.
   * Example: file name is racron.log storeLog("pero") renames log file to
   * racron.log.pero
   * @param ext extension to rename log file
   */  
  public void storeLog(String ext) {
    new File(logfile).renameTo(new File(logfile+"."+ext));
  }
  /**
   * Renames log with extension System.currentTimeMillis().
   * Calls storeLog(System.currentTimeMillis()+"")
   * @see #storeLog(String)
   */  
  public void storeLog() {
    storeLog(System.currentTimeMillis()+"");
  }
  private Thread getHook() {
    Thread hook = new Thread() {
      public void run() {
        log("Cron system is going down, trying to reexecute "+props.getProperty("cron.runcmd"));
        Process proc = null;
        try {
          storeLog();
          proc = Runtime.getRuntime().exec(props.getProperty("cron.runcmd", "echo 'no run command specified'"));
          return;
        } catch (Exception e) {
          log("Hook failed! ("+e.getMessage()+") Exiting");
        }
        
/*        if (proc!=null) {
          System.out.println("proc != null");
          try {
            int ch;
            while ((ch = proc.getInputStream().read()) > -1) System.out.write(ch);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }*/

        storeLog();
      }
    };
    return hook;
  }
  
  /**
   * Main method
   * @param args properties file name
   * @see #raCron(String)
   */  
  public static void main(String[] args) {
    String propsfile = "cron.properties";
    if (args.length>0 && args[0] != null) propsfile = args[0];
    raCron cron = new raCron(propsfile);
    Runtime.getRuntime().addShutdownHook(cron.getHook());
  }
}