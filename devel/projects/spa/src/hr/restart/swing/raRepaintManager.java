/****license*****************************************************************
**   file: raRepaintManager.java
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
package hr.restart.swing;

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class raRepaintManager extends RepaintManager {
  static {
    RepaintManager.setCurrentManager(new raRepaintManager());
  }
  public static void install() {
  }
  public static boolean isSwingAllowed(JComponent c) {
    Container parent = c.getTopLevelAncestor();
    return !(parent instanceof JraFrame && !((JraFrame) parent).isSwingAllowed() ||
        parent instanceof JraDialog && !((JraDialog) parent).isSwingAllowed());
  }
  public synchronized void addDirtyRegion(JComponent c, int x, int y, int w, int h) {
    if (!isSwingAllowed(c)) return;
    super.addDirtyRegion(c, x, y, w, h);
  }
}
