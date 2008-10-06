/****license*****************************************************************
**   file: raScreenHandler.java
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

import hr.restart.start;
import hr.restart.help.raUserDialog;
import hr.restart.help.raUserPanel;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public abstract class raScreenHandler {
  private static boolean enableHandler = true;
  private static JFrame getMain() {
    JFrame mainFr;
    if (hr.restart.start.isMainFrame()) {
      mainFr = hr.restart.mainFrame.getMainFrame();
    } else {
      mainFr = raLLFrames.getRaLLFrames().findMsgStartFrame();
      if (mainFr == null) mainFr = startFr;
      if (mainFr == null) mainFr = raLLFrames.getRaLLFrames().getMsgStartFrame();
    }
    return mainFr;
  }
  private static startFrame startFr;
  private static int getHeight(raUserDialog userD) {
    if (hr.restart.start.isMainFrame()) {
      return userD.getHeight();
    } else {
      return getMain().getHeight();
    }
  }

  private static Dimension calcSize(raUserDialog userD,boolean showing) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int w;
    if (raUserPanel.getAutoHideOption()) {
      w = screenSize.width - 10;
    } else {
      w = showing?screenSize.width-userD.getWidth():screenSize.width;
    } 
    return new Dimension(w,getHeight(userD));
  }

  private static Point calcLocation(raUserDialog userD) {
    return new Point(left?userD.getWidth():0,getSFRy(0));
  }

  private static void checkBoxes(boolean sel) {
    if (start.isMainFrame()) {
//      mainFrame.getMainFrame().
    } else {
      LinkedList startframes = raLLFrames.getRaLLFrames().getStartFrames();
      for (Iterator i = startframes.iterator(); i.hasNext(); ) {
        startFrame item = (startFrame)i.next();
        item.setHelpOptionChecked(sel);
      }
    }
  }
  //neki parametri os lijevo desno itd. za sada lijevo za mainFrame, desno za startFrame
  static boolean left = false;//start.isMainFrame();

  private static void hideOtherFrames(startFrame frm) {
    for (Iterator i = raLLFrames.getRaLLFrames().getStartFrames().iterator(); i.hasNext(); ) {
      startFrame item = (startFrame)i.next();
      if (!item.equals(frm) && item.isVisible()) item.hide();
    }
  }

  public static void recalcSizeOfMain() {
    Window main = getMain();
    if (main.isShowing()) {
      main.setSize(calcSize(raUserDialog.getInstance(),raUserDialog.getInstance().isShowing()));
      main.setLocation(0,getSFRy(0));
    }
  }

  public static void showingUserDialog(raUserDialog userD) {
    if (!enableHandler) return;
    JFrame main = getMain();
    userD.calcSizeAndPosition(left);
    main.setSize(calcSize(userD,true));
    main.setLocation(calcLocation(userD));
    checkBoxes(true);
  }

  public static boolean hidingUserDialog(raUserDialog userD) {
    if (!enableHandler) return false;
    Window main = getMain();
    if (main.isShowing()) {
      main.setSize(calcSize(userD,false));
      main.setLocation(0,getSFRy(0));
      checkBoxes(false);
      return true;
    } else {
      start.exit();
      return false;
    }
  }

  public static void hidingMainDialog(startFrame frm) {
    if (!enableHandler) return;
    startFr = frm;
    if (frm.SFMain && hr.restart.sisfun.raUser.getInstance().getUser().equals("test")) return;
    if (!start.getUserDialog().isShowing() && !raUserPanel.getAutoHideOption()) {
//      start.getUserDialog().show();
      start.exit();
      if (frm != null) start.getUserDialog().show();
    } else {
//      start.getUserDialog().show();
    }
    raUserDialog.getInstance().autoShow();
  }
  /*
   * Vraca y od lokacije ovisno o OS-u (mac os ili ne)
   */
  public static int getSFRy(int oldy) {
	  Integer _i = (Integer)start.invokeAppleUtilMethod("getStartFrameRelativeY",null,null);
	  int sfry = (_i==null)?0:_i.intValue();//StartFrameRelativeY a ne ...:)
	  return sfry;
  }
  public static void showingMainDialog(startFrame frm) {
    if (!enableHandler) return;
    startFr = frm;
    if (frm == null) {
      //getMain().setSize(calcSize(start.getUserDialog(),start.getUserDialog().isShowing()));
      getMain().setSize(calcSize(start.getUserDialog(),!raUserDialog.getInstance().autoHide()));
      getMain().setLocation(0,getSFRy(0));
    } else {
      //frm.setSize(calcSize(start.getUserDialog(),start.getUserDialog().isShowing()));
      frm.setSize(calcSize(start.getUserDialog(),!raUserDialog.getInstance().autoHide()));
      
      frm.setLocation(0,getSFRy(0));
      disableHandler();
      hideOtherFrames(frm);
      enableHandler();
    }
  }
  public static void disableHandler() {
    enableHandler = false;
  }
  public static void enableHandler() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        enableHandler = true;
      }
    });
  }
}