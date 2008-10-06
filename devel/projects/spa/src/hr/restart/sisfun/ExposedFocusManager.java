/****license*****************************************************************
**   file: ExposedFocusManager.java
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

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.DefaultFocusManager;
import javax.swing.FocusManager;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class ExposedFocusManager extends DefaultFocusManager {
  private Component lastComponent = null;
  static {
    FocusManager.setCurrentManager(new ExposedFocusManager());
  }
  private ExposedFocusManager() {
  }
  public static void install() {
  }
  public static ExposedFocusManager getInstance() {
    return (ExposedFocusManager) FocusManager.getCurrentManager();
  }
  public Component getLastFocusedComponent() {
    return lastComponent;
  }
  public void processKeyEvent(Component c, KeyEvent e) {
    lastComponent = c;
    super.processKeyEvent(c, e);
  }
}
