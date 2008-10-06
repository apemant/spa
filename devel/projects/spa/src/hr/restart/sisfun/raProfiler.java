/****license*****************************************************************
**   file: raProfiler.java
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
package hr.restart.sisfun;

import hr.restart.util.Aus;

import java.util.HashMap;
import java.util.StringTokenizer;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class raProfiler {
  private static raProfiler instance = new raProfiler();
  private static boolean profilerActive;
  private static boolean anonymous = true;
  private HashMap loadedClasses = new HashMap();

  private raProfiler() {
  }

  public static raProfiler getInstance() {
    return instance;
  }

  public HashMap getClasses() {
    return loadedClasses;
  }

  public void dumpClasses(String path) {
    dumpClassesTo(path);
  }

  public void saveState() {
    findState(true);
  }

  public void getState() {
    findState(false);
  }

  private void findState(boolean diff) {
    if (!profilerActive)
      throw new UnsupportedOperationException("Profiler is not active");
//    Vector all = raClassInspector.getClasses();
//    System.out.println(all);
    dumpClassesTo("$$temp.tmp");
    TextFile tf = TextFile.read("$$temp.tmp").check();
    String line;
    while ((line = tf.in()) != null) {
      StringTokenizer st = new StringTokenizer(line, ",");
      String name = st.nextToken();
      if (!anonymous) {
        StringTokenizer sti = new StringTokenizer(name, "$");
        String last = sti.nextToken();
        while (sti.hasMoreTokens())
          last = sti.nextToken();
        if (Aus.isNumber(last)) continue;
      }
      int size = Aus.getNumber(st.nextToken());
      int instances = Aus.getNumber(st.nextToken());
      int allocations = Aus.getNumber(st.nextToken());
      int arraySize = Aus.getNumber(st.nextToken());
      int arrayInstances = Aus.getNumber(st.nextToken());
      int arrayAllocations = Aus.getNumber(st.nextToken());
      ClassData cd = (ClassData) loadedClasses.get(name);
      if (cd == null) cd = new ClassData();
      cd.difference += instances - cd.instances;
      if (diff) cd.difference = 0;
      cd.size = size;
      cd.instances = instances;
      cd.allocations = allocations;
      if (allocations > 0)
        loadedClasses.put(name, cd);

      ClassData acd = (ClassData) loadedClasses.get(name.concat("[]"));
      if (acd != null || arrayAllocations > 0) {
        if (acd == null) acd = new ClassData();
        acd.difference += arrayInstances - acd.instances;
        if (diff) acd.difference = 0;
        acd.size = arraySize;
        acd.instances  = arrayInstances;
        acd.allocations = arrayAllocations;
        if (arrayAllocations > 0)
          loadedClasses.put(name.concat("[]"), acd);
      }
    }
    tf.close();
/*    classes.empty();
    for (Iterator i = loadedClasses.keySet().iterator(); i.hasNext(); ) {
      String name = (String) i.next();
      classes.insertRow(false);
      classes.setString("NAME", name);
      ClassData cd = (ClassData) loadedClasses.get(name);
      classes.setInt("SIZE", cd.size);
      classes.setInt("INSTANCES", cd.instances);
      classes.setInt("ALLOCATIONS", cd.allocations);
      classes.setInt("DIFFERENCE", cd.difference);
    }
    classes.post();*/
  }

  native private static int getObjectInstances(String className);
  native private static int getObjectAllocations(String className);
  native private static int getObjectSize(String className);
  native private static boolean isProfilerActive();
  native private static boolean dumpClasses();
  native private static boolean dumpClassesTo(String path);

  static {
    try {
//      raClassInspector.ensureLotsOfMemory();
      System.loadLibrary("Profiler");
      profilerActive = isProfilerActive();
    } catch (Error e) {
      e.printStackTrace();
      profilerActive = false;
    }
  }

  public static class ClassData {
    public int instances, allocations, difference, size;
  }
}
