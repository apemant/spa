/****license*****************************************************************
**   file: JraDialog.java
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

import hr.restart.sisfun.frmParam;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;

import javax.swing.JDialog;

public class JraDialog extends JDialog {

  static {
    raRepaintManager.install();
  }

  private boolean showing;

  public JraDialog() {
    super();
  }

  public JraDialog(Dialog owner) {
    super(owner);
  }

  public JraDialog(Frame owner) {
    super(owner);
  }

  public JraDialog(Dialog owner,boolean modal) {
    super(owner,modal);
  }

  public JraDialog(Frame owner,boolean modal) {
    super(owner,modal);
  }

  public JraDialog(Dialog owner,String title) {
    super(owner,title);
  }

  public JraDialog(Frame owner,String title) {
    super(owner,title);
  }

  public JraDialog(Dialog owner,String title, boolean modal) {
    super(owner,title, modal);
  }

  public JraDialog(Frame owner,String title, boolean modal) {
    super(owner,title, modal);
  }
  public void paint(Graphics g) {
    showing = false;
    super.paint(g);
  }

  public boolean isSwingAllowed() {
    return !showing;
  }

  public void show() {
    if (!isShowing() && isTreperenjeHandled()) showing = true;
    super.show();
  }
  
  private static boolean trepHandled = true;
  private static boolean trepCached = false;
  private static int trepCounter = 0;
  private static boolean isTreperenjeHandled() {
    if (trepCached) return trepHandled;
    if (trepCounter++ < 5) return trepHandled;
    trepCached = true;
    //default true
    return trepHandled = frmParam
      .getParam("sisfun","trepHandled","D",
                "Ukljuciti 'teperenje hack'? (D/N)",
                true)//local!!
            .equalsIgnoreCase("D");
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

