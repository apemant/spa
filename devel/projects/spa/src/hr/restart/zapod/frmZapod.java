/****license*****************************************************************
**   file: frmZapod.java
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
package hr.restart.zapod;

import hr.restart.util.startFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class frmZapod extends startFrame {
  private static hr.restart.zapod.frmZapod myFZ;
  public static hr.restart.zapod.jzpMenu zpMnu;
  public static String RESBUNDLENAME="hr.restart.zapod.zpRes";
  static JMenu initMenu = new JMenu("");
  java.util.ResourceBundle Res = java.util.ResourceBundle.getBundle(RESBUNDLENAME);
  JMenuBar jzpMenuBar = new JMenuBar();
  JMenuItem jmiCheckZiroPar = new JMenuItem();
  JMenuItem jmiCheckKosobe = new JMenuItem();
  JMenu jmToolMnu = new JMenu();
  public frmZapod() {
    try {
      jbInit();
      this.setRaJMenuBar(jzpMenuBar);
      myFZ = this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public frmZapod(boolean isMain) {
    try {
      jbInit();
      if (isMain) this.setRaJMenuBar(jzpMenuBar);
      myFZ = this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jmiCheckZiroPar.setText("Kontrola i ispravak žiro ra\u010Duna partnera");
    jmiCheckZiroPar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiCheckZiroPar_actionPerformed(e);
      }
    });
    jmiCheckKosobe.setText("Dodavanje kontakt osoba partnera");
    jmiCheckKosobe.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiCheckKosobe_actionPerformed(e);
      }
    });
    jmToolMnu.setText("Servisne funkcije");
    jmToolMnu.add(jmiCheckKosobe);
    jmToolMnu.add(jmiCheckZiroPar);
    setToolMenu(jmToolMnu);
    jzpMenuBar.add(new jzpMenu(this));
    this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
  }

  public static hr.restart.zapod.frmZapod getFrmZapod() {
    if (myFZ == null) {
      myFZ = new frmZapod(false);
    }
    return myFZ;
  }

  void jmiCheckZiroPar_actionPerformed(ActionEvent e) {
    hr.restart.util.raProcess.runChild(this,new Runnable() {
      public void run() {
        hr.restart.gk.raGKKontrole.checkZiroPar();
      }
    });
  }
  void jmiCheckKosobe_actionPerformed(ActionEvent e) {
    int odg = JOptionPane.showConfirmDialog(this,"Dodati svim partnerima sa upisanom kontakt osobom zapis kontakt osobe ?", "Kontakt osobe", JOptionPane.YES_NO_OPTION);
    if (odg == 0) {
      hr.restart.util.raProcess.runChild(this,new Runnable() {
        public void run() {
          frmKosobe.autoAddKontaktGo();
        }
      });
    }
  }
  public static javax.swing.JMenu getJzpMenu(startFrame startframe) {
    if (hr.restart.start.isMainFrame()&&false) {//nemoj to nikad
      return initMenu;
    } else {
      zpMnu = new jzpMenu(startframe);
      return zpMnu;
    }
  }
/**
 * DEPRECATED
 * @deprecated
 */
  public static javax.swing.JMenu getJzpMenu() {
    if (hr.restart.start.FRAME_MODE==hr.restart.util.raFrame.PANEL) {
      return initMenu;
    } else {
      zpMnu = new jzpMenu(hr.restart.util.raLLFrames.getRaLLFrames().getMsgStartFrame());
      return zpMnu;
    }
  }
}