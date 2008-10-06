/****license*****************************************************************
**   file: menuSklad.java
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
package hr.restart.robno;

import hr.restart.util.raLoader;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class menuSklad extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");

  hr.restart.util.startFrame SF;
  JMenuItem jmPRI = new JMenuItem();
  JMenuItem jmOTP = new JMenuItem();
  JMenuItem jmIZD = new JMenuItem();
  JMenuItem jmREV = new JMenuItem();
  JMenuItem jmPRV = new JMenuItem();

  public menuSklad(hr.restart.util.startFrame startframe) {
    SF = startframe;
    jbInit();
    this.addAncestorListener(new javax.swing.event.AncestorListener() {
      public void ancestorAdded(javax.swing.event.AncestorEvent e) {
      }
      public void ancestorMoved(javax.swing.event.AncestorEvent e) {
      }
      public void ancestorRemoved(javax.swing.event.AncestorEvent e) {
      }
    });
  }

  private void jbInit() {
    this.setText("Skladište");
    jmPRI.setText("Primke");
    jmPRI.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPRI_actionPerformed(e);
      }
    });
    jmOTP.setText("Otpremnice");
    jmOTP.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmOTP_actionPerformed(e);
      }
    });
    jmIZD.setText("Izdatnice");
    jmIZD.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmIZD_actionPerformed(e);
      }
    });
    jmREV.setText("Reversi");
    jmREV.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmREV_actionPerformed(e);
      }
    });
    jmPRV.setText("Povratnice reversa");
    jmPRV.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPRV_actionPerformed(e);
      }
    });
    this.add(jmPRI);
    this.add(jmOTP);
    this.add(jmIZD);
    this.add(jmREV);
    this.add(jmPRV);
  }
  void jmPRI_actionPerformed(ActionEvent e) {
    frmPRI fPRI = (frmPRI)raLoader.load("hr.restart.robno.frmPRI");
    presPRI.getPres().showJpSelectDoc("PRI", fPRI, true, "Primke");
  }
  void jmOTP_actionPerformed(ActionEvent e) {
    raOTP raotp = (raOTP)raLoader.load("hr.restart.robno.raOTP");
    presOTP.getPres().showJpSelectDoc("OTP", raotp, true, "Otpremnice");
  }
  void jmIZD_actionPerformed(ActionEvent e) {
    raIZD raizd = (raIZD)raLoader.load("hr.restart.robno.raIZD");
    presIZD.getPres().showJpSelectDoc("IZD", raizd, true, "Izadtnice");
  }
  void jmPRV_actionPerformed(ActionEvent e) {
    raPRV raprv = (raPRV)raLoader.load("hr.restart.robno.raPRV");
    presPRV.getPres().showJpSelectDoc("PRV", raprv, true, "Povratnica reversa");
  }
  void jmREV_actionPerformed(ActionEvent e) {
    raREV rarev = (raREV)raLoader.load("hr.restart.robno.raREV");
    presREV.getPres().showJpSelectDoc("REV", rarev, true, "Revers");
  }

}