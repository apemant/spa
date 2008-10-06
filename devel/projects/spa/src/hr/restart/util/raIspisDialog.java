/****license*****************************************************************
**   file: raIspisDialog.java
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

public abstract class raIspisDialog extends JraDialog {
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      ok_action();
    }
    public void jPrekid_actionPerformed() {
//      firstESC();
      cancelPress();
    }
  };
  hr.restart.util.reports.raRunReport RepRun;
  BorderLayout borderLayout1 = new BorderLayout();
  private javax.swing.JPanel jPan;

  public raIspisDialog() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public raIspisDialog(hr.restart.util.startFrame owner, boolean modal){
    super(owner, modal);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
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
    okp.jBOK.setText("Ispis");
    okp.jBOK.setIcon(raImages.getImageIcon(raImages.IMGPRINT));
    this.getContentPane().add(okp, BorderLayout.SOUTH);
  }

  void ok_action() {
    if (okPress()) {
      System.out.println("radim ispis");
      ispis();
//      firstESC();
    }
  }
  void this_keyPressed(KeyEvent e) {
    if (e.getKeyCode()==e.VK_F10) {
      ok_action();
    }
    else if (e.getKeyCode()==e.VK_ESCAPE) {
      if (runFirstESC()) {
        firstESC();
      }
      else {
        cancelPress();
      }
    }
  }
/**
 * Kaj da se desi kad pritisnemo OK
 */
  public abstract boolean okPress();
/**
 * Kaj da se desi kad pritisnemo Cancel
 */
  public void cancelPress() {
    this.setVisible(false);
  }
/**
 * Da li da potjera prvi escape
 */
  public abstract boolean runFirstESC();
/**
 * Kaj da se desi kad pritisnemo ESC, ako je runFirstESC()=true
 */
  public abstract void firstESC();
/**
 * Kaj da se radi na kad se pojavi na ekranu
 */
  public abstract void componentShow();
  /**
 * Setiranje panela
 */
  public void setJPan(javax.swing.JPanel newJPan) {
    jPan = newJPan;
    this.getContentPane().add(jPan);
  }
  public javax.swing.JPanel getJPan() {
    return jPan;
  }
  void this_componentShown(ComponentEvent e) {
    componentShow();
  }
  public hr.restart.util.reports.raRunReport getRepRunner() {
    if (RepRun == null) RepRun = hr.restart.util.reports.raRunReport.getRaRunReport();
    return RepRun;
  }
  private String provider;
  private String rTitle;
  private int dataSrcIdx = 0;

  public void addReport(String newProvider,String newRTitle) {
    addReport(newProvider,newRTitle, 0);
  }

  public void addReport(String newProvider, String newRTitle, int dsIdx) {
    provider = newProvider;
    rTitle = newRTitle;
    dataSrcIdx = dsIdx;
  }
  private void ispis(){
    if (provider == null) return;
    if (rTitle == null) rTitle = "Ispis";
    // (ab.f)
    getRepRunner().clearAllReports();
    getRepRunner().addReport(provider, rTitle, dataSrcIdx);
//    getRepRunner().setDataSourceIndex(dataSrcIdx);
//    getRepRunner().setProviderClassName(provider);
//    getRepRunner().setReportTitles(new String[] {rTitle});
    getRepRunner().go();
  }
}