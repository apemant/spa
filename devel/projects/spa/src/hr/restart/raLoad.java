/****license*****************************************************************
**   file: raLoad.java
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

import hr.restart.help.raShortcutItem;

import java.util.ResourceBundle;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class raLoad implements Runnable {
  ResourceBundle raRes = ResourceBundle.getBundle(start.RESBUNDLENAME);
  private raToolBar raTBar = raToolBar.getRaToolBar();
  private String raModule;
  private String resModule;
  private javax.swing.JPanel raToolPane;
  public static int TOP=0;
  public static int RIGHT=1;
  private int raPosition;
  hr.restart.util.startFrame modFrame;
//  javax.swing.JToggleButton tgb;
  javax.swing.JMenuItem jmi;
  public raLoad() {
  }
  public raLoad(String module) {
    setRaModule(module);
  }
  public void run() {
    loadModule();
  }
  /**
   * <pre>
   * Modul za uloadati
   * treba imati:
   * 1. sliku - images/<ramodule>.jpg
   * 2. entry u resource bundleu - "jB<ramodule>_text" za tooltip
   * </pre>
   */
  public void setRaModule(String myRaModule) {
    resModule=myRaModule;
    raModule=myRaModule.substring(3);
  }
  /**
   * Panel na koji se kelje buttoni
   */
  public void setRaToolPane(javax.swing.JPanel myRaToolPane) {
    raToolPane = myRaToolPane;
  }
  /**
   * pozicija toolbar windowa deklarira se varijablama u raLoad (TOP,RIGHT,...)
   */
  public void setRaPosition(int newRaPosition) {
    raPosition = newRaPosition;
  }


  private void loadModule() {
    if (!hr.restart.sisfun.raUser.getInstance().canAccessApp(raModule, "P")) return;
    int inst = hr.restart.start.checkInstaled(raModule);
    System.out.println("inst " + raModule + ": " + inst);
    if (inst < 0) return;
    raSplashAWT.splashMSG("U\u010Ditavam modul "+raRes.getString("jB"+raModule+"_text")+"...");
    javax.swing.ImageIcon iicon = null;

    try {
      iicon = hr.restart.util.raImages.getModuleIcom(raModule);
    } catch (Exception e) {
      raSplashAWT.splashMSG("ERR raLoad: slika za button nije prona\u0111ena, kreiram button bez slike");
    }
    try {
      final raAction raAc = new raAction(inst == 1);
      String name = hr.restart.start.getName(raModule);
      if (name == null || name.length() == 0)
        name = raRes.getString("jB"+raModule+"_text");
      jmi = new javax.swing.JMenuItem(name);
      jmi.addActionListener(raAc);
      raShortcutItem sItem = new raShortcutItem() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          raAc.actionPerformed(e);
        }
      };
      sItem.setText(jmi.getText());
      if (inst != 1) {
        jmi.setEnabled(false);
        sItem.setEnabled(false);
      }
      
//      sItem.setIndex(jmi.getText());
      sItem.setIndex(raModule);
      if (iicon!=null) sItem.setIcon(iicon);
      raTBar.getAplShortcuts().addItem(sItem);

      Class modStart = Class.forName(raRes.getString(resModule));
      //String modTitle = raRes.getString("jB"+raModule+"_text");
      modFrame = (hr.restart.util.startFrame)modStart.newInstance();
      modFrame.getJMenuBar().setToolTipText(name);
      modFrame.setTitle(name);
      //hr.restart.mainFrame.getMainFrame().menuTree.addMenuBar(modFrame.getJMenuBar(),modFrame);
      start.getUserDialog().getUserPanel().getMenuTree().addMenuBar(modFrame.getJMenuBar(),modFrame);
    } catch (Exception ex) {
//ex.printStackTrace();
      raSplashAWT.splashMSG(
          "ERR raLoad: Klasa " +
          raRes.getString(resModule) +
          " nije prona\u0111ena. Neuspješno u\u010Ditavanje modula" + raModule
          );
//      tgb.setEnabled(false);
      jmi.setEnabled(false);
    }
    hr.restart.mainFrame.getMainFrame().defaultMenu.add(jmi);
    raSplashAWT.splashMSG("Modul "+raRes.getString("jB"+raModule+"_text")+" uspješno u\u010Ditan!");
  }

  class raAction implements java.awt.event.ActionListener {
    boolean active = true;
    public raAction() {
    }
    public raAction(boolean act) {
      active = act;
    }
    public void actionPerformed(java.awt.event.ActionEvent e) {
      if (!active) return;
      if (start.isMainFrame())
        modFrame.ShowMeP(raRes.getString("jB"+raModule+"_text"));
      else
        modFrame.ShowMe(false,raRes.getString("jB"+raModule+"_text"));
    }
  }

}