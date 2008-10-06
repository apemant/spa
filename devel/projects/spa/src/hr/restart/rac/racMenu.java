/****license*****************************************************************
**   file: racMenu.java
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
package hr.restart.rac;

import hr.restart.util.raLoader;

import java.awt.event.ActionEvent;

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

public class racMenu extends JMenu {
  hr.restart.util.startFrame SF;

  JMenuItem jmPON = new JMenuItem();
  JMenuItem jmPRD = new JMenuItem();
  JMenuItem jmRAC = new JMenuItem();
  JMenuItem jmGRN = new JMenuItem();
  JMenuItem jmTER = new JMenuItem();
  JMenuItem jmODB = new JMenuItem();
  JMenuItem jmAutoFuck = new JMenuItem();
  JMenuItem jmAutoFuck2 = new JMenuItem();

  public racMenu(hr.restart.util.startFrame startframe) {
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
    this.setText("Obrada raèuna");
    jmPON.setText("Ponude");
    jmPON.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPON_actionPerformed(e);
      }
    });
    jmPRD.setText("Raèuni za predujam");
    jmPRD.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPRD_actionPerformed(e);
      }
    });
    jmRAC.setText("Bezgotovinski raèuni");
    jmRAC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRAC_actionPerformed(e);
      }
    });
    jmGRN.setText("Gotovinski raèuni");
    jmGRN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmGRN_actionPerformed(e);
      }
    });
    jmTER.setText("Tereæenja");
    jmTER.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmTER_actionPerformed(e);
      }
    });
    jmODB.setText("Odobrenja");
    jmODB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmODB_actionPerformed(e);
      }
    });
    jmAutoFuck.setText("Više raèuna iz ugovora");
    jmAutoFuck.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmAutoFuck_actionPerformed(e);
      }
    });
    
    jmAutoFuck2.setText("Više raèuna za partnere");
    jmAutoFuck2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmAutoFuck2_actionPerformed(e);
      }
    });

    this.add(jmPON);
    this.add(jmPRD);
    this.add(jmRAC);
    this.add(jmGRN);
    this.add(jmTER);
    this.add(jmODB);
    this.addSeparator();
    this.add(jmAutoFuck);
    this.add(jmAutoFuck2);
    
  }
  void jmPON_actionPerformed(ActionEvent e) {
    hr.restart.robno.raPONOJ rapon = (hr.restart.robno.raPONOJ)raLoader.load("hr.restart.robno.raPONOJ");
    hr.restart.robno.presPONOJ.getPres().showJpSelectDoc("PON", rapon, true, jmPON.getText());
  }
  void jmPRD_actionPerformed(ActionEvent e) {
    hr.restart.robno.raPRD raprd = (hr.restart.robno.raPRD)raLoader.load("hr.restart.robno.raPRD");
    hr.restart.robno.presPRD.getPres().showJpSelectDoc("PRD", raprd, true, jmPRD.getText());
  }

  void jmRAC_actionPerformed(ActionEvent e) {
    hr.restart.robno.raRAC rarac = (hr.restart.robno.raRAC)raLoader.load("hr.restart.robno.raRAC");
    hr.restart.robno.presRAC.getPres().showJpSelectDoc("RAC", rarac, true, jmRAC.getText());
  }
  void jmGRN_actionPerformed(ActionEvent e) {
    hr.restart.robno.raGRN ragrn = (hr.restart.robno.raGRN)raLoader.load("hr.restart.robno.raGRN");
    hr.restart.robno.presGRN.getPres().showJpSelectDoc("GRN", ragrn, true, jmGRN.getText());
  }
  void jmODB_actionPerformed(ActionEvent e) {
    hr.restart.robno.raOdobrenje fOdobrenje = (hr.restart.robno.raOdobrenje)raLoader.load("hr.restart.robno.raOdobrenje");
    hr.restart.robno.presODB.getPres().showJpSelectDoc("ODB", fOdobrenje, true, jmODB.getText());
  }
  void jmTER_actionPerformed(ActionEvent e) {
    hr.restart.robno.raTeret fTeretOdb = (hr.restart.robno.raTeret)raLoader.load("hr.restart.robno.raTeret");
    hr.restart.robno.presTER.getPres().showJpSelectDoc("TER", fTeretOdb, true, jmTER.getText());
  }
  void jmAutoFuck_actionPerformed(ActionEvent e) {
//    SF.showFrame("hr.restart.robno.frmAutomaticRacUgovor", jmAutoFuck.getText());
    SF.showFrame("hr.restart.robno.raAutomatRac", jmAutoFuck.getText());
  }
  void jmAutoFuck2_actionPerformed(ActionEvent e) {
      SF.showFrame("hr.restart.robno.raAutomatRacPartner", jmAutoFuck2.getText());
  }
}
