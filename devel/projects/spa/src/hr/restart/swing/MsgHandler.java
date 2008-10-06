/****license*****************************************************************
**   file: MsgHandler.java
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

import hr.restart.util.raStatusBar;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public abstract class MsgHandler {
  private static boolean off = false;
  public static void off() {
    off = true;
  }
  public static void on() {
    off = false;
  }
  public static void createHelpText(String hlptxt, javax.swing.ImageIcon hlpimg,com.borland.dx.dataset.Column col) {
    if (off) return;
    if (raStatusBar.getStatusBar().isProgBarAdded()) {
      hlptxt = "Obrada u tijeku ...";
    }
    if ((hlptxt == null)||(hlptxt.equals(""))) hlptxt = createDefaultHelpText(col);
/*
    if (hr.restart.start.isMainFrame()) {
      if (hlpimg == null) hlpimg = raImages.getImageIcon(raImages.DEFAULTHELPICON);
      hr.restart.mainFrame.getMainFrame().helpMsg(hlptxt,hlpimg);
    } else {
//      hr.restart.util.startFrame.getStartFrame().statusMSG(hlptxt);
      hr.restart.util.raLLFrames.getRaLLFrames().getMsgStartFrame().statusMSG(hlptxt);
    }
*///zamjenjuje ih ova
    raStatusBar.getStatusBar().statusMSG(hlptxt);//treba jos nesto s imageom isfurat
  }
  public static String createDefaultHelpText(com.borland.dx.dataset.Column col) {
    if (off) return "";
    if (col != null)
      return "Unesite podatak - " + col.getCaption().toUpperCase() + ".";
    else
      return "Unesite podatak";
  }
  public static String createDefaultErrText(com.borland.dx.dataset.Column col) {
    if (col != null)
      return "Neispravan unos - " + col.getCaption().toUpperCase() + ".";
    else
      return "Neispravan unos";
  }
  public static void createErrText(String errtxt, javax.swing.ImageIcon errimg,com.borland.dx.dataset.Column col) {
    if (off) return;
    if (raStatusBar.getStatusBar().isProgBarAdded()) {
      errtxt = "Obrada u tijeku ...";
    }
    if ((errtxt == null)||(errtxt.equals(""))) errtxt = createDefaultErrText(col);
/*
    if (hr.restart.start.isMainFrame()) {
      if (errimg == null) errimg = raImages.getImageIcon(raImages.DEFAULTERRICON);
      hr.restart.mainFrame.getMainFrame().helpMsg(errtxt,errimg);
    } else {
//      hr.restart.util.startFrame.getStartFrame().statusMSG(errtxt);
      hr.restart.util.raLLFrames.getRaLLFrames().getMsgStartFrame().statusMSG(errtxt);
    }
*///zamjenjuje ih ova
    raStatusBar.getStatusBar().statusMSG(errtxt);//treba jos nesto s imageom isfurat

  }
  public static void clearMsgText() {
    if (off) return;
    String errtxt = "";
    if (raStatusBar.getStatusBar().isProgBarAdded()) {
      errtxt = "Obrada u tijeku ...";
    }
    /*
    if (hr.restart.start.isMainFrame())
      hr.restart.mainFrame.getMainFrame().helpMsg(errtxt);
    else
      hr.restart.util.raLLFrames.getRaLLFrames().getMsgStartFrame().statusMSG(errtxt);
    *///zamjenjuje ih ova
    raStatusBar.getStatusBar().statusMSG(errtxt);//treba jos nesto s imageom isfurat
  }
}