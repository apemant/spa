/****license*****************************************************************
**   file: Stopwatch.java
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


public class Stopwatch {
  
  public static final Stopwatch inactive = new Stopwatch();
  private static boolean active = false;
  private static int rbr = 0;
  static {
    checkInstall();
  }
  
  long startTime;
  int myRbr;

  private Stopwatch() {
    myRbr = ++rbr;
    startTime = System.currentTimeMillis();
  }
  
  private Stopwatch(String text) {
    myRbr = ++rbr;
    startTime = System.currentTimeMillis();
    System.out.println("STOPWATCH " + myRbr + " START: " + text);
  }
  
  private static String getClassName(Object o) {
    String cn = o.getClass().getName();
    int d = cn.lastIndexOf('$');
    if (d == -1 || !Aus.isDigit(cn.substring(d + 1))) return cn;
    return cn + "=" + o.getClass().getSuperclass().getName();
  }

  public static Stopwatch startInit(Object o) {
    return start("init " + getClassName(o));
  }
  
  public static Stopwatch start() {
    if (!active) return inactive;
    return new Stopwatch();
  }
  
  public static Stopwatch start(String text) {
    if (!active) return inactive;
    return new Stopwatch(text);
  }
  
  public void report() {
    if (!active) return;
    long elapsed = System.currentTimeMillis() - startTime;
    System.out.println("STOPWATCH " + myRbr + " REPORT: Time elapsed since start: " + elapsed);
  }
  
  public void report(String text) {
    if (!active) return;
    long elapsed = System.currentTimeMillis() - startTime;
    System.out.println("STOPWATCH " + myRbr + " REPORT: Time elapsed after " + text + ": " + elapsed);
  }
  
  public void report(String text, Object o) {
    if (!active) return;
    long elapsed = System.currentTimeMillis() - startTime;
    System.out.println("STOPWATCH " + myRbr + " REPORT: Time elapsed after " + 
        text + " (" + getClassName(o) + "): " + elapsed);
  }
  
  private static void checkInstall() {
    String sw = IntParam.getTag("stopwatch");
    active = sw != null && sw.equalsIgnoreCase("on");
  }
}
