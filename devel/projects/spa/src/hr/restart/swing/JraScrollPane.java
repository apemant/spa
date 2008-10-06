/****license*****************************************************************
**   file: JraScrollPane.java
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
 * Created on Sep 3, 2004
 */
package hr.restart.swing;

import java.awt.Component;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;


/**
 * @author abf
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JraScrollPane extends JScrollPane {

  /**
   * 
   */
  public JraScrollPane() {
    super();
  }

  /**
   * @param vsbPolicy
   * @param hsbPolicy
   */
  public JraScrollPane(int vsbPolicy, int hsbPolicy) {
    super(vsbPolicy, hsbPolicy);
  }

  /**
   * @param view
   */
  public JraScrollPane(Component view) {
    super(view); 
  }

  /**
   * @param view
   * @param vsbPolicy
   * @param hsbPolicy
   */
  public JraScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
    super(view, vsbPolicy, hsbPolicy);
  }
  
  public JScrollBar createHorizontalScrollBar() {
    return new ScrollBar(JScrollBar.HORIZONTAL) {
      public boolean isFocusTraversable() {
        return false;
      }
    };
  };
  public JScrollBar createVerticalScrollBar() {
    return new ScrollBar(JScrollBar.VERTICAL) {
      public boolean isFocusTraversable() {
        return false;
      }
    };
  };
}
