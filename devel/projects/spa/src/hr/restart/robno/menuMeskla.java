/****license*****************************************************************
**   file: menuMeskla.java
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

public class menuMeskla extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");

  hr.restart.util.startFrame SF;
  JMenuItem jmMES = new JMenuItem();
  JMenuItem jmMEU = new JMenuItem();
  JMenuItem jmMEI = new JMenuItem();

  public menuMeskla(hr.restart.util.startFrame startframe) {
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
    this.setText("Meðuskladišnice");
    jmMES.setText("Meðuskladišnice-UI");
    jmMES.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmMES_actionPerformed(e);
      }
    });
    jmMEU.setText("Meðuskladišnice-ulaz");
    jmMEU.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmMEU_actionPerformed(e);
      }
    });
    jmMEI.setText("Meðuskladišnice-izlaz");
    jmMEI.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmMEI_actionPerformed(e);
      }
    });
    this.add(jmMES);
    this.add(jmMEU);
    this.add(jmMEI);
  }
  void jmMES_actionPerformed(ActionEvent e) {
    raMeskla rameskla = (raMeskla)raLoader.load("hr.restart.robno.raMeskla");
    selectMES.getPres().showJpSelectDoc("MES", rameskla, true, ((javax.swing.JMenuItem)e.getSource()).getText());
  }
  void jmMEU_actionPerformed(ActionEvent e) {
    raMEU rameu = (raMEU)raLoader.load("hr.restart.robno.raMEU");
    selectMEU.getPres().showJpSelectDoc("MEU", rameu, true, ((javax.swing.JMenuItem)e.getSource()).getText());
  }
  void jmMEI_actionPerformed(ActionEvent e) {
    raMEI ramei = (raMEI)raLoader.load("hr.restart.robno.raMEI");
    selectMEI.getPres().showJpSelectDoc("MEI", ramei, true, ((javax.swing.JMenuItem)e.getSource()).getText());
  }

}