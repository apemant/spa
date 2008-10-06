/****license*****************************************************************
**   file: JraTableScrollPane.java
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

import java.awt.Component;

import javax.swing.JViewport;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
/*
public class JraTableScrollPane extends com.borland.dbswing.TableScrollPane {
}
*/
public class JraTableScrollPane extends JraScrollPane {
  public JraTableScrollPane() {
    super();
  }
  public JraTableScrollPane(Component view) {
    super(view);
  }
  public void setIgnoreRepaint(boolean ignore) {
    ((JraViewport) getViewport()).setIgnoreRepaint(ignore);
  }
  public JViewport createViewport() {
    return new JraViewport();
  }
}

class JraViewport extends JViewport {
  private boolean ignoreRepaint;
  public JraViewport() {
//    this.setScrollMode(this.BACKINGSTORE_SCROLL_MODE);
  }
  public void setIgnoreRepaint(boolean ignore) {
    ignoreRepaint = ignore;
  }
  public void paint(java.awt.Graphics g) {
    if (!ignoreRepaint && raRepaintManager.isSwingAllowed(this)) super.paint(g);
  }
  public void repaint(int tm, int x, int y, int w, int h) {
    if (!ignoreRepaint && raRepaintManager.isSwingAllowed(this))
      super.repaint(tm, x, y, w, h);
  }
}
