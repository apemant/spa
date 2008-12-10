/****license*****************************************************************
**   file: menuNar.java
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

public class menuNar extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");

  hr.restart.util.startFrame SF;
  JMenuItem jmZAH = new JMenuItem();
  JMenuItem jmUZP = new JMenuItem();
  JMenuItem jmNDO = new JMenuItem();
  JMenuItem jmNKU = new JMenuItem();

  public menuNar(hr.restart.util.startFrame startframe) {
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
    this.setText("Narud�be");
    jmZAH.setText("Trebovanje");
    jmZAH.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmZAH_actionPerformed(e);
      }
    });
    jmUZP.setText("Upit za ponudu");
    jmUZP.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmUZP_actionPerformed(e);
      }
    });
    jmNDO.setText("Narud�be dobavlja�u");
    jmNDO.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmNDO_actionPerformed(e);
      }
    });
    jmNKU.setText("Narud�be kupca");
    jmNKU.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmNKU_actionPerformed(e);
      }
    });
    this.add(jmZAH);
    this.add(jmUZP);
    this.add(jmNDO);
    this.add(jmNKU);
  }
  void jmZAH_actionPerformed(ActionEvent e) {
    raZAH fZAH = (raZAH) raLoader.load("hr.restart.robno.raZAH");
    presZAH.getPres().showJpSelectDoc("TRE", fZAH, true, "Trebovanje");
  }
  void jmUZP_actionPerformed(ActionEvent e) {
    raUZP fUZP = (raUZP) raLoader.load("hr.restart.robno.raUZP");
    presUZP.getPres().showJpSelectDoc("UZP", fUZP, true, "Upit za ponudu");
  }
  void jmNDO_actionPerformed(ActionEvent e) {
    frmNarDob frmND = (frmNarDob) raLoader.load("hr.restart.robno.frmNarDob");
    presNDO.getPres().showJpSelectDoc("NDO", frmND, true, "Narud�be dobavlja�u");
  }
  void jmNKU_actionPerformed(ActionEvent e) {
    raNKU ranku = (raNKU)raLoader.load("hr.restart.robno.raNKU");
    presNKU.getPres().showJpSelectDoc("NKU", ranku, true, "Narud�be kupca");
  }

}