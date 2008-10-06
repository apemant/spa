/****license*****************************************************************
**   file: JraFrame.java
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

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;

public class JraFrame extends JFrame {
  static {
    raRepaintManager.install();
  }

  private boolean showing;

  public JraFrame() {
    super();
  }
  public JraFrame(GraphicsConfiguration gc) {
    super(gc);
  }
  public JraFrame(String title, GraphicsConfiguration gc) {
    super(title,gc);
  }
  public JraFrame(String title) {
    super(title);
  }

  public void paint(Graphics g) {
    showing = false;
    super.paint(g);
  }

  public boolean isSwingAllowed() {
    return !showing;
  }

  public void show() {
    if (!isShowing()) showing = true;
    super.show();
  }

  public void hide() {
    try {
      raTableCopyPopup.hideInstance();
      raDisabledPopup.hideInstance();
      raDatePopup.hideInstance();
    } catch (Exception e) {}
    super.hide();
  }
  
  public void dispose() {
    try {
      raTableCopyPopup.hideInstance();
      raDisabledPopup.hideInstance();
      raDatePopup.hideInstance();
    } catch (Exception e) {}
    AWTKeyboard.unregisterComponent(this);
    super.dispose();
  }
}
