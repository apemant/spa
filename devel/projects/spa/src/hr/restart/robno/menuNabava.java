/****license*****************************************************************
**   file: menuNabava.java
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

public class menuNabava extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");

  hr.restart.util.startFrame SF;
  public JMenuItem jmPRK = new JMenuItem();
  public JMenuItem jmPTE = new JMenuItem();
  public JMenuItem jmPOR = new JMenuItem();
  public JMenuItem jmKAL = new JMenuItem();

  public menuNabava(hr.restart.util.startFrame startframe) {
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
    this.setText("Nabava");
    jmPRK.setText("Primke-kalkulacije");
    jmPRK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPRK_actionPerformed(e);
      }
    });
    jmPTE.setText("Povratnice-tereæenja");
    jmPTE.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPTE_actionPerformed(e);
      }
    });
    jmPOR.setText("Poravnanja-nivelacije");
    jmPOR.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPOR_actionPerformed(e);
      }
    });
    jmKAL.setText("Kalkulacije");
    jmKAL.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmKAL_actionPerformed(e);
      }
    });
    this.add(jmPRK);
    this.add(jmPTE);
    this.add(jmPOR);
    this.add(jmKAL);
  }
  public void jmPRK_actionPerformed(ActionEvent e) {
    frmPRK fPRK = (frmPRK)raLoader.load("hr.restart.robno.frmPRK");
    presPRK.getPres().showJpSelectDoc("PRK", fPRK, true, jmPRK.getText());
  }
  public void jmPTE_actionPerformed(ActionEvent e) {
    frmPTE fPTE = (frmPTE)raLoader.load("hr.restart.robno.frmPTE");
    presPTE.getPres().showJpSelectDoc("PTE", fPTE, true, jmPTE.getText());
  }
  public void jmPOR_actionPerformed(ActionEvent e) {
    frmNivelacija frmNivel = (frmNivelacija)raLoader.load("hr.restart.robno.frmNivelacija");
    presPOR.getPres().showJpSelectDoc("POR", frmNivel, true, jmPOR.getText());
  }
  public void jmKAL_actionPerformed(ActionEvent e) {
    frmKAL fKAL = (frmKAL)raLoader.load("hr.restart.robno.frmKAL");
    presKAL.getPres().showJpSelectDoc("KAL", fKAL, true, jmKAL.getText());
  }

}