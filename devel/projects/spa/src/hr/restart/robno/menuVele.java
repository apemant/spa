/****license*****************************************************************
**   file: menuVele.java
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

public class menuVele extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");

  hr.restart.util.startFrame SF;
  JMenuItem jmPON = new JMenuItem();
  JMenuItem jmROT = new JMenuItem();
  JMenuItem jmPOD = new JMenuItem();
  JMenuItem jmPRD = new JMenuItem();

  public menuVele(hr.restart.util.startFrame startframe) {
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
    this.setText("Veleprodaja");
    jmPRD.setText("Raèuni za predujam");
    jmPRD.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPRD_actionPerformed(e);
      }
    });
    jmPON.setText("Ponude");
    jmPON.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPON_actionPerformed(e);
      }
    });
    jmROT.setText("Raèuni-otpremnice");
    jmROT.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmROT_actionPerformed(e);
      }
    });
    jmPOD.setText("Povratnice-odobrenja");
    jmPOD.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPOD_actionPerformed(e);
      }
    });
    this.add(jmPON);
    this.add(jmROT);
    this.add(jmPOD);
    this.add(jmPRD);
  }
  void jmROT_actionPerformed(ActionEvent e) {
    raROT rarot = (raROT)raLoader.load("hr.restart.robno.raROT");
    presROT.getPres().showJpSelectDoc("ROT", rarot, true, jmROT.getText());
  }
  void jmPOD_actionPerformed(ActionEvent e) {
    frmPovratKupca frmdokizlaz = (frmPovratKupca)raLoader.load("hr.restart.robno.frmPovratKupca");
    presPOD.getPres().showJpSelectDoc("POD", frmdokizlaz, true, jmPOD.getText());
  }
  void jmPON_actionPerformed(ActionEvent e) {
    raPON rapon = (raPON)raLoader.load("hr.restart.robno.raPON");
    presPON.getPres().showJpSelectDoc("PON", rapon, true, jmPON.getText());
  }
  void jmPRD_actionPerformed(ActionEvent e) {
	    hr.restart.robno.raPRD raprd = (hr.restart.robno.raPRD)raLoader.load("hr.restart.robno.raPRD");
	    hr.restart.robno.presPRD.getPres().showJpSelectDoc("PRD", raprd, true, jmPRD.getText());
	  }

}