/****license*****************************************************************
**   file: raClassInspector.java
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Vector;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class raClassInspector {

  private raClassInspector() {
  }

  public static Vector getClasses() {
    ClassLoader loader = ClassLoader.getSystemClassLoader();
    Class loaderClass = loader.getClass();
    while (ClassLoader.class.isAssignableFrom(loaderClass.getSuperclass()))
      loaderClass = loaderClass.getSuperclass();

    try {
      Field classesField = loaderClass.getDeclaredField("classes");
      classesField.setAccessible(true);
      return (Vector) classesField.get(ClassLoader.getSystemClassLoader());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static ArrayList getRestartClasses() {
    Vector c = getClasses();
    ArrayList rc = new ArrayList(100);
    for (int i = 0; i < c.size(); i++)
      if (((Class) c.get(i)).getName().startsWith("hr.restart"))
        rc.add(c.get(i));
    return rc;
  }

  public static long total() {
    return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
  }

  public static int sizeof(Class c) {
    Object[] objectBag = new Object[10000];
    forcedGC();
    long usage = total(), iters = 10;
    int instances = 0;
    while (total() - usage < 100000 && iters <= 1000) {
      iters *= 10;
      try {
        while (instances++ < iters)
          objectBag[instances - 1] = c.newInstance();
      } catch (Exception e) {}
    }
    return Math.round(1.0f * (total() - usage) / iters);
  }

//  public static void ensureLotsOfMemory() {
//    int[][] dummy = new int[1000][1000];
//    forcedGC();
//  }

  public static void forcedGC() {
    System.gc();
    System.gc();
    System.runFinalization();
    System.gc();
    System.gc();
    System.runFinalization();
    System.gc();
    System.gc();
  }
}

