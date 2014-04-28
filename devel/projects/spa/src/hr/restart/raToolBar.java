/****license*****************************************************************
**   file: raToolBar.java
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
package hr.restart;

import hr.restart.help.raShortcutPanel;
import hr.restart.help.raUserDialog;

import java.util.LinkedList;
import java.util.ResourceBundle;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class raToolBar extends javax.swing.JWindow {
  ResourceBundle raRes1 = ResourceBundle.getBundle(start.RESBUNDLENAME);
  public static raToolBar rTB;
  public static int BUTTONSIZE=50;
  public static int TPOSITION;
  /*
  JPanel jPtool = new JPanel();
  private static GridLayout toolLayout = new GridLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPposition = new JPanel();
  GridLayout posLayout = new GridLayout();
  JToggleButton jTBexit = new JToggleButton(raImages.getImageIcon(raImages.IMGTBEXIT));
  JToggleButton jTBtop = new JToggleButton(raImages.getImageIcon(raImages.IMGTBTOP));
  JToggleButton jTBmenu = new JToggleButton(raImages.getImageIcon(raImages.IMGTBMENU));
  JToggleButton jTBright = new JToggleButton(raImages.getImageIcon(raImages.IMGTBRIGHT));
  JPopupMenu jPopupApps = new JPopupMenu();
  java.awt.event.ActionListener exitAction;
  java.awt.event.ActionListener topAction;
  java.awt.event.ActionListener rightAction;
  PopupAppListener popupAppListener = new PopupAppListener();
  JMenu jMPos = new JMenu();
  JMenuItem jMPosTop = new JMenuItem();
  JMenuItem jMPosRight = new JMenuItem();
  JMenuItem jMexit = new JMenuItem();
  */
  boolean showable = false;
  private raUserDialog userDialog;
  private raShortcutPanel applShPanel;

  public raToolBar() {
//    super(mainFrame.getMainFrame()); za dialog
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    rTB = this;
  }

  hr.restart.help.raShortcutContainer getAplShortcuts() {
    return applShPanel.getShortcuts();
  }
  
  Object waitForLoad = new Object();

  public void showTB() {
    try {
//      jbInit();
//      setTB();
//      updUI();
//      setEnabled(false);
      loadApps();
      synchronized (waitForLoad) {
        waitForLoad.wait();
      }
//      addPopup();
//      setEnabled(true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    if (start.isMainFrame()) {
      userDialog = new raUserDialog(mainFrame.getMainFrame());
    } else {
      userDialog = new raUserDialog();
    }
    applShPanel = new raShortcutPanel(null,null,null,false);
    userDialog.getUserPanel().addUserTab("Aplikacije",applShPanel);
  }

  private void loadApps() {
    hr.restart.zapod.dlgGetKnjig.loadDlgGetKnjig();
    new appLoader().start();
//    new appLoader().run();
  }
  class appLoader extends Thread {
      public void run() {
        String[][] rcontents = start.getResContents();
        for (int i=0;i<rcontents.length;i++) {
          if (rcontents[i][0].startsWith("APL")) {
//            SwingUtilities.invokeLater(new raLoad(rcontents[i][0]));
            new raLoad(rcontents[i][0]).run();
          }
        }
        synchronized (waitForLoad) {
          waitForLoad.notify();
        }
        showable = true;
        raSplashAWT.hideSplash();
        loadForOptimize();
        showIfShowable();
//        if (start.isMainFrame()) mainFrame.getMainFrame().userPanel.loadShortcutPanels();        
//-> doba\u010Deno iz starta da ne loada ipak sve istovremeno
        hr.restart.util.raLoader.lazyLoad(); //<- prebaceno iz start
      }
  }
  public static void loadForOptimize() {
    if (!start.checkArgs("optimize")) return;
    hr.restart.util.raUpit dummy1 = new hr.restart.util.raUpit() {
      public void componentShow() {      }
      public void firstESC() {      }
      public boolean runFirstESC() {
        return true;
      }
      public void okPress() {      }
    };    
    int dummy3 = hr.restart.util.raMasterDetail.DELDETAIL;        
    //hr.restart.util.reports.raElixirLoader.load();
    hr.restart.util.reports.raJasperLoader.load();
    dummy1 = null;
//    dummy2 = null;    
  }
  public void showIfShowable() {
    if (isShowable()) {
      //setVisible(true);
      start.stripPermittedApps();
      LinkedList ll = ((hr.restart.help.raAbstractShortcutContainer)getAplShortcuts()).listelements;
      if (ll.size() == 1) {
        hr.restart.help.raShortcutItem sitem = (hr.restart.help.raShortcutItem)ll.get(0);
        sitem.actionPerformed(null);
      } else {
        userDialog.show();
      }
    }
  }

  public raUserDialog getUserDialog() {
    return userDialog;
  }

  private boolean isShowable() {
    if (!showable) return false;
    if (hr.restart.start.getFrmPassword().isShowing()) return false;
    if (hr.restart.start.isMainFrame()) {
      return mainFrame.getMainFrame().isHidden();
    } else {
      return hr.restart.util.raLLFrames.getRaLLFrames().findMsgStartFrame() == null;
    }
  }

  public static raToolBar getRaToolBar() {
    if (rTB == null) {
      rTB=new raToolBar();
    }
    return rTB;
  }
}