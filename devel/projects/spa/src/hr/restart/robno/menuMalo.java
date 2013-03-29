/****license*****************************************************************
**   file: menuMalo.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Pos;
import hr.restart.baza.doki;
import hr.restart.pos.frmMasterBlagajna;
import hr.restart.util.raLoader;
import hr.restart.util.raProcess;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.borland.dx.dataset.DataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class menuMalo extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");

  hr.restart.util.startFrame SF;
  JMenuItem jmPON = new JMenuItem();
  JMenuItem jmPRD = new JMenuItem();
  JMenuItem jmGOT = new JMenuItem();
  JMenuItem jmGRN = new JMenuItem();
  JMenuItem jmOTP = new JMenuItem();
  JMenuItem jmPOD = new JMenuItem();

  public menuMalo(hr.restart.util.startFrame startframe) {
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
    this.setText("Maloprodaja");
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
    jmGOT.setText("Gotovinski raèuni - otpremnice");
    jmGOT.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmGOT_actionPerformed(e);
      }
    });
    jmOTP.setText("Otpremnice");
    jmOTP.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmOTP_actionPerformed(e);
      }
    });
    jmGRN.setText("Gotovinski raèuni");
    jmGRN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmGRN_actionPerformed(e);
      }
    });
    jmPOD.setText("Povratnice-odobrenja");
    jmPOD.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPOD_actionPerformed(e);
      }
    });
    this.add(jmPON);
    this.add(jmPRD);
    this.add(jmGOT);
    this.add(jmGRN);
    this.add(jmOTP);
    this.add(jmPOD);
  }
  void jmPON_actionPerformed(ActionEvent e) {
    raPONkup rapon = (raPONkup)raLoader.load("hr.restart.robno.raPONkup");
    presPONkup.getPres().showJpSelectDoc("PON", rapon, true, jmPON.getText());
  }
  void jmPRD_actionPerformed(ActionEvent e) {
    hr.restart.robno.raPRDkup raprd = (hr.restart.robno.raPRDkup)raLoader.load("hr.restart.robno.raPRDkup");
    hr.restart.robno.presPRDkup.getPres().showJpSelectDoc("PRD", raprd, true, jmPRD.getText());
  }
  
  
  void jmGOT_actionPerformed(ActionEvent e) {
    fiskRed();
    
    hr.restart.robno.raGOT ragot = (hr.restart.robno.raGOT)raLoader.load("hr.restart.robno.raGOT");
    hr.restart.robno.presGOT.getPres().showJpSelectDoc("GOT", ragot, true, jmGOT.getText());
  }
  void jmGRN_actionPerformed(ActionEvent e) {
    fiskRed();
    
    hr.restart.robno.raGRN ragrn = (hr.restart.robno.raGRN)raLoader.load("hr.restart.robno.raGRN");
    hr.restart.robno.presGRN.getPres().showJpSelectDoc("GRN", ragrn, true, jmGRN.getText());
  }
  
  long lastTrans = 0;
  void fiskRed() {
    if (lastTrans == 0 || System.currentTimeMillis() > lastTrans + 1000 * 60 * 60 * 8)
      raProcess.runChild(SF, "Fiskalizacija", "Fiskalizacija nepotpuno zakljuèenih raèuna...", new Runnable() {    
        public void run() {
          lastTrans = System.currentTimeMillis();
          DataSet ms = doki.getDataModule().getTempSet("fok='D' AND (jir='' or jir is NULL) AND " +
          		"(vrdok in ('GRN','GOT') OR (vrdok='PRD' AND param='K'))");
          ms.open();
          System.out.println("Fiskalizacija " + ms.rowCount() + " crvenih raèuna...");
          if (ms.rowCount() == 0) return;
          for (ms.first(); ms.inBounds(); ms.next()) {
            raIzlazTemplate.fisk(ms);
          }
        }
      });
  }
  
  void jmOTP_actionPerformed(ActionEvent e) {
    hr.restart.robno.raOTP ragot = (hr.restart.robno.raOTP)raLoader.load("hr.restart.robno.raOTP");
    hr.restart.robno.presOTP.getPres().showJpSelectDoc("OTP", ragot, true, jmOTP.getText());
  }
  void jmPOD_actionPerformed(ActionEvent e) {
//    frmPovratKupca frmdokizlaz = (frmPovratKupca)raLoader.load("hr.restart.robno.frmPovratKupca");
    raPODkup frmdokizlaz = (raPODkup)raLoader.load("hr.restart.robno.raPODkup");
    hr.restart.robno.presPODkup.getPres().showJpSelectDoc("POD", frmdokizlaz, true, jmPOD.getText());
  }

}