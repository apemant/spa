/****license*****************************************************************
**   file: GlobalEventListener.java
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

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class GlobalEventListener {
  private int mx, my;
  private Thread borg = null;

  private static GlobalEventListener inst = new GlobalEventListener();

  public static void install() {};

  private GlobalEventListener() {
    Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
      public void eventDispatched(AWTEvent e) {
        if (e instanceof MouseEvent) {
          if (e.getSource() instanceof Component &&
              ((Component) e.getSource()).isShowing()) {
            Point s = ((Component) e.getSource()).getLocationOnScreen();
            mx = s.x + ((MouseEvent) e).getX();
            my = s.y + ((MouseEvent) e).getY();
          }
        }
        if (e instanceof KeyEvent && borg != null) {
          KeyEvent ke = (KeyEvent) e;
          if (ke.getID() == ke.KEY_RELEASED && ke.isControlDown() && ke.getKeyCode() == ke.VK_Q)
            borg.interrupt();
        }
      }
    }, AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
  }

  public static void setBorgThread(Thread borgThread) {
    inst.borg = borgThread;
  }

  public static boolean isBorgThreadRunning() {
    return inst.borg != null;
  }

  public static int getMouseX() {
    return inst.mx;
  }

  public static int getMouseY() {
    return inst.my;
  }
}
