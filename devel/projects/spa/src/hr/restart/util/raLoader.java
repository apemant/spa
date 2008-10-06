/****license*****************************************************************
**   file: raLoader.java
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
import java.util.Enumeration;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public abstract class raLoader {
  private static Vector classlist = new Vector();
  private static java.util.Properties loaderProps = new java.util.Properties();
  private static int loadMember = 0;
/*
  private static SwingWorker work = new SwingWorker() {
    public Object construct() {
      Object[] sortedValues = getSortedValues();
      for (int i=0;i<sortedValues.length;i++) {
        String clnm = getKeyFromValue((String)sortedValues[i]);
        load(clnm,false);
      }
      return null;
    }
  };
*/

  private static TimerTask lazyLoadProcess = new TimerTask() {
    public void run() {
      Object[] sortedValues = getSortedValues();
      raStatusBar status = raStatusBar.getStatusBar();
      status.setShowDelayWindow(false);
      status.progBar.setDelay(300);
      status.startTask(sortedValues.length,"U\u010Ditavanje...");
      for (int i=0;i<sortedValues.length;i++) {
        String clnm = getKeyFromValue((String)sortedValues[i]);
        load(clnm,false);
//System.out.println("U\u010Ditavanje "+clnm+"...");
        status.next("U\u010Ditavanje "+clnm+"...");
      }
      status.finnishTask();
      status.setShowDelayWindow(true);
    }
  };
  private static void add(String clname) {
    if (!isContaining(clname)) classlist.add(clname);
  }

  public static Object load(String clname) {
    return load(clname,true);
  }

  private static synchronized Object load(String clname,boolean writeProps) {

    if (!isContaining(clname)) add(clname);
    if (isLoaded(clname)) {
      loadProps();
      if (loaderProps.getProperty(clname) != null && writeProps) {
        addToProps(clname);
      }
      Object loadedObject = getLoadedObject(clname);
      if (loadedObject!=null) return getLoadedObject(clname);
    }
    try {
      long loadTime = System.currentTimeMillis();
      Object classinstance = Class.forName(clname).newInstance();
      loadTime = System.currentTimeMillis() - loadTime;
      classlist.setElementAt(classinstance,classlist.indexOf(clname));
//System.out.println("Time to load "+clname+" is "+loadTime);
      if (writeProps&&loadTime>500) {
        addToProps(clname);
      }
      return classinstance;
    } catch (Exception e) {
      System.out.println("raLoader.load: "+clname+" Class loading error: "+e);
      e.printStackTrace();
      if (clname!=null) {//izbaci iz vectora
        classlist.remove(clname);
      }
      System.err.println("Probaj ponovo!!! ");
//      if (writeProps) status.finnishTask();
      return null;
    }
  }

  private static Object getLoadedObject(String clname) {
    for (int i=0;i<classlist.size();i++) {
      if (classlist.get(i).getClass().getName().equals(clname)) return classlist.get(i);
    }
    return null;
  }

  private static boolean isContaining(String clname) {
    for (int i=0;i<classlist.size();i++) {
      String name = getName(i);
      if (name!=null) if (name.equals(clname)) return true;
    }
    return false;
  }

  private static String getName(int memb) {
    String cnam;
    Object member = classlist.get(memb);
    if (member == null) {
//      classlist.remove(memb);
      return null;
    }
    if (member instanceof java.lang.String) {
      cnam = (String)member;
    } else {
      cnam = member.getClass().getName();
    }
    return cnam;
  }
/**
 * Oprez!!! nikad ne pozivati ako prije nije provjeren isContaining
 * jer to sto string nije u classlistu moze znaciti da nije niti prijavljen (add) a kamoli loadan
 */
  private static boolean isLoaded(String cname) {
    return !classlist.contains(cname);
  }
  /**
   * @param cname ime klase za koju hocemo provjeriti jel uloadana
   * @return true ako je klasa loadana preko loadera i ako je uloadana
   */
  public static boolean isLoaderLoaded(String cname) {
    if (!isContaining(cname)) return false;
    return !classlist.contains(cname);
  }
  public static Properties getLoaderProperties() {
    loadProps();
    return loaderProps;
  }
  public static void lazyLoad() {
    lazyLoad(0);
  }

  public static void lazyLoad(long delay) {
    if (!hr.restart.start.checkArgs("lazyload")) return;
    Timer timer = new Timer();
    timer.schedule(lazyLoadProcess,delay);
  }

  private static Object[] getSortedValues() {
    loadProps();
    java.util.TreeSet sortVal = new java.util.TreeSet(
      new java.util.Comparator() {
        public int compare(Object o1,Object o2) {
          Long l1 = Long.valueOf(o1.toString());
          Long l2 = Long.valueOf(o2.toString());
          java.sql.Date d1 = new java.sql.Date(l1.longValue());
          java.sql.Date d2 = new java.sql.Date(l2.longValue());
//System.out.println(d1.toString()+" - "+d2.toString());
          if (d1.toString().equals(d2.toString())) {
//System.out.print(">>>L compare<<<");
            return l1.compareTo(l2);
          } else {
//System.out.print(">>>D compare<<<");
            return d2.compareTo(d1);
          }
//          return l1.compareTo(l2);
        }

    });
    java.util.Enumeration vals = loaderProps.elements();
    while (vals.hasMoreElements()) {
      sortVal.add(vals.nextElement());
    }
    return sortVal.toArray();
  }
  private static void addToProps(String key) {
    loadProps();
//    String val = Integer.toString(loadMember);
    String val = Integer.toString((int)System.currentTimeMillis());
    if (!loaderProps.containsValue(val)) {
      loaderProps.setProperty(key,val);
    } else {
      String rpcKey = getKeyFromValue(val);
      String rpcVal = getNewValue();
      loaderProps.setProperty(rpcKey,rpcVal);
      loaderProps.setProperty(key,val);
    }
    storeProps();
    loadMember++;
  }
  private static String getKeyFromValue(String val) {
    Enumeration kys = loaderProps.keys();
    while (kys.hasMoreElements()) {
      String currKey = (String)kys.nextElement();
      if (loaderProps.getProperty(currKey).equals(val)) return currKey;
    }
    return null;
  }
  private static String getNewValue() {
    int maxVal = 0;
    Enumeration vals = loaderProps.elements();
    while (vals.hasMoreElements()) {
      try {
        int currVal = Integer.parseInt(vals.nextElement().toString());
        if (currVal > maxVal) maxVal = currVal;
      } catch (Exception e) {
      }
    }
    return Integer.toString(maxVal+1);
  }
  private static void loadProps() {
    FileHandler FH = new FileHandler("loader.properties");
    FH.openRead();
    try {
      loaderProps.load(FH.fileInputStream);
      FH.close();
    } catch (Exception e) {
//      System.out.println("raLoader.loadProps Exception: "+e);
    }
  }
  private static void storeProps() {
    FileHandler FH = new FileHandler("loader.properties");
    FH.openWrite();
    try {
      loaderProps.store(FH.fileOutputStream,"Popis klasa sa redoslijedom loadanja");
      FH.close();
    } catch (Exception e) {
      System.out.println("raLoader.storeProps Exception: "+e);
    }
  }
}