/****license*****************************************************************
**   file: Borg.java
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

import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class Borg {
  private static Robot borg;
  private static int autoDelay;
  private static int mx, my;
  private static Keyboard keys;

  private Borg() {}

  public static void setMouseOrigin(int x, int y) {
    mx = x;
    my = y;
  }

  public static void setMouseOrigin(Point p) {
    mx = p.x;
    my = p.y;
  }

  public static void install() {
    if (borg != null) return;
    try {
      borg = new Robot();
      borg.setAutoDelay(0);
      keys = DefaultKeyboard.getInstance();
      DefaultKeyboard.getInstance().autoAdapt();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean installed() {
    return borg != null;
  }

  public static void delay(int delay) throws InterruptedException {
    if (delay > 0)
      Thread.sleep(delay);
  }

  public static void flush() throws InterruptedException {
    try {
      if (Toolkit.getDefaultToolkit() instanceof sun.awt.SunToolkit)
        ((sun.awt.SunToolkit) Toolkit.getDefaultToolkit()).flushPendingEvents();
      SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {}
      });
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public static void setAutoDelay(int autoDelay) {
    Borg.autoDelay = autoDelay;
  }

  public static void press(int keyCode) throws InterruptedException {
    borg.keyPress(keyCode);
    delay(autoDelay);
  }

  public static void release(int keyCode) throws InterruptedException {
    borg.keyRelease(keyCode);
    delay(autoDelay);
  }

  public static void type(int keyCode) throws InterruptedException {
    press(keyCode);
    release(keyCode);
    delay(autoDelay * 2);
  }

  public static void type(int keyCode, int keyMod) throws InterruptedException {
    if ((keyMod & KeyEvent.CTRL_MASK) > 0)
      press(KeyEvent.VK_CONTROL);
    if ((keyMod & KeyEvent.ALT_MASK) > 0)
      press(KeyEvent.VK_ALT);
    if ((keyMod & KeyEvent.SHIFT_MASK) > 0)
      press(KeyEvent.VK_SHIFT);
    if ((keyMod & KeyEvent.ALT_GRAPH_MASK) > 0)
      press(KeyEvent.VK_ALT_GRAPH);
    press(keyCode);
    release(keyCode);
    if ((keyMod & KeyEvent.ALT_GRAPH_MASK) > 0)
      release(KeyEvent.VK_ALT_GRAPH);
    if ((keyMod & KeyEvent.SHIFT_MASK) > 0)
      release(KeyEvent.VK_SHIFT);
    if ((keyMod & KeyEvent.ALT_MASK) > 0)
      release(KeyEvent.VK_ALT);
    if ((keyMod & KeyEvent.CTRL_MASK) > 0)
      release(KeyEvent.VK_CONTROL);
    delay(autoDelay * 2);
  }

  public static void type(char ch) throws InterruptedException {
    int code = keys.getKeyCode(ch);
    if (code != KeyEvent.VK_UNDEFINED)
      type(code, keys.getKeyModifiers(ch));
  }

  public static void move(int nx, int ny) throws InterruptedException {
    double range = Math.sqrt((mx - nx) * (mx - nx) + (my - ny) * (my - ny));
    int steps = (int) Math.round(Math.pow(range, 0.5) * 5);
    if (steps == 0) return;
    for (int i = 0; i <= steps; i++) {
      borg.mouseMove(mx + (int) Math.round((nx - mx) * Math.pow((double) i / steps, 0.6)),
                      my + (int) Math.round((ny - my) * Math.pow((double) i / steps, 0.6)));
      delay(autoDelay);
    }
    mx = nx;
    my = ny;
  }

  public static void click() throws InterruptedException {
    click(true);
  }

  public static void click(boolean left) throws InterruptedException {
    borg.mousePress(left ? KeyEvent.BUTTON1_MASK : KeyEvent.BUTTON1_MASK);
    delay(autoDelay);
    borg.mouseRelease(left ? KeyEvent.BUTTON1_MASK : KeyEvent.BUTTON1_MASK);
    delay(autoDelay);
  }
}
