/****license*****************************************************************
**   file: raUpitDialog.java
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

import hr.restart.swing.JraDialog;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public abstract class raUpitDialog extends JraDialog {
  private javax.swing.JPanel jPan;
  hr.restart.util.reports.raRunReport RepRun;
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      okPress();
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };
  BorderLayout borderLayout1 = new BorderLayout();

  public raUpitDialog() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
//    this.setModal(true);
    this.getContentPane().setLayout(borderLayout1);
    this.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        this_keyPressed(e);
      }
    });
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        this_componentShown(e);
      }
    });
    this.getContentPane().add(okp, BorderLayout.SOUTH);
  }
  void this_keyPressed(KeyEvent e) {
    if (e.getKeyCode()==e.VK_F10) {
      okPress();
    }
    else if (e.getKeyCode()==e.VK_ESCAPE) {
      if (escPress()) {
        cancelPress();
      }
    }
    else if (e.getKeyCode()==e.VK_F6) {
      keyF6Press();
    }
  }
  void this_componentShown(ComponentEvent e) {
    componentShow();
  }
  public hr.restart.util.reports.raRunReport getRepRunner() {
    if (RepRun == null) RepRun = hr.restart.util.reports.raRunReport.getRaRunReport();
    return RepRun;
  }
/**
 * Kaj da se desi kad pritisnemo OK
 */
  public abstract void okPress();
/**
 * Kaj da se desi kad pritisnemo Cancel
 */
  public void cancelPress() {
    this.setVisible(false);
  }
/**
 * Kaj da radi na pritisak F6
 */
  public void keyF6Press() {
  }
/**
 * Kaj da se radi na kad se pojavi na ekranu
 */
  public abstract void componentShow();
/**
 * Setiranje panela
 * @param newJPan
 */
  public void setJPan(javax.swing.JPanel newJPan) {
    jPan = newJPan;
    this.getContentPane().add(jPan, BorderLayout.CENTER);
  }
  public javax.swing.JPanel getJPan() {
    return jPan;
  }
  public boolean escPress() {
    return true;
  }
  public hr.restart.util.OKpanel getOKPanel() {
    return okp;
  }
  public String provider;
  public String rTitle;
  public int dataSrcIdx = 0;
  public void addReport(String newProvider,String newRTitle) {
    addReport(newProvider,newRTitle, 0);
  }
  public void addReport(String newProvider, String newRTitle, int dsIdx) {
    provider = newProvider;
    rTitle = newRTitle;
    dataSrcIdx = dsIdx;
  }
  public void ispis(){
    System.out.println("providet: "+provider+" Title: "+rTitle);
    if (provider == null) return;
    if (rTitle == null) rTitle = "Ispis";
    getRepRunner().setDataSourceIndex(dataSrcIdx);
    getRepRunner().setProviderClassName(provider);
//    getRepRunner().setReportTitles(new String[] {rTitle});
    getRepRunner().go();
  }
}