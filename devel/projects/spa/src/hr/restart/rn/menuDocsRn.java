/****license*****************************************************************
**   file: menuDocsRn.java
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
package hr.restart.rn;

import hr.restart.robno.frmPRE;
import hr.restart.robno.presIZD;
import hr.restart.robno.presPOV;
import hr.restart.robno.presPRE;
import hr.restart.robno.presPRV;
import hr.restart.robno.presREV;
import hr.restart.robno.raIZD;
import hr.restart.robno.raPOV;
import hr.restart.robno.raPRV;
import hr.restart.robno.raREV;
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

public class menuDocsRn extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.util.startFrame SF;
  JMenuItem jmIZD = new JMenuItem();
  JMenuItem jmPOV = new JMenuItem();
  JMenuItem jmPRE = new JMenuItem();
  JMenuItem jmREV = new JMenuItem();
  JMenuItem jmPRV = new JMenuItem();

  public menuDocsRn(hr.restart.util.startFrame startframe) {
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
    this.setText("Dokumenti");
    jmIZD.setText("Izdatnice");
    jmIZD.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmIZD_actionPerformed(e);
      }
    });
    jmPOV.setText("Povratnice");
    jmPOV.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPOV_actionPerformed(e);
      }
    });
    jmPRE.setText("Predatnice");
    jmPRE.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPRE_actionPerformed(e);
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
    this.add(jmIZD);
    this.add(jmPOV);
    this.add(jmPRE);
    this.addSeparator();
    this.add(jmREV);
    this.add(jmPRV);
  }
  void jmPOV_actionPerformed(ActionEvent e) {
    raPOV rapov = (raPOV)raLoader.load("hr.restart.robno.raPOV");
    presPOV.getPres().showJpSelectDoc("POV", rapov, true, jmPOV.getText());
  }
  void jmIZD_actionPerformed(ActionEvent e) {
    raIZD raizd = (raIZD)raLoader.load("hr.restart.robno.raIZD");
    presIZD.getPres().showJpSelectDoc("IZD", raizd, true, jmIZD.getText());
  }
  void jmPRE_actionPerformed(ActionEvent e) {
    frmPRE rapre = (frmPRE)raLoader.load("hr.restart.robno.frmPRE");
    presPRE.getPres().showJpSelectDoc("PRE", rapre, true, jmPRE.getText());
  }
  void jmPRV_actionPerformed(ActionEvent e) {
    raPRV raprv = (raPRV)raLoader.load("hr.restart.robno.raPRV");
    presPRV.getPres().showJpSelectDoc("PRV", raprv, true, jmPRV.getText());
  }
  void jmREV_actionPerformed(ActionEvent e) {
    raREV rarev = (raREV)raLoader.load("hr.restart.robno.raREV");
    presREV.getPres().showJpSelectDoc("REV", rarev, true, jmREV.getText());
  }

}