/****license*****************************************************************
**   file: menuPreglediRn.java
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

import hr.restart.util.PreSelect;

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

public class menuPreglediRn extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.util.startFrame SF;
  JMenuItem jmRekapTros = new JMenuItem();
  JMenuItem jmPregledTros = new JMenuItem();
  JMenuItem jmPregledNorm = new JMenuItem();
  JMenuItem jmStatRN = new JMenuItem();

  public menuPreglediRn(hr.restart.util.startFrame startframe) {
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
    this.setText("Pregledi");
    jmRekapTros.setText("Rekapitulacija troškova");
    jmRekapTros.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRekapTros_actionPerformed(e);
      }
    });
    jmPregledTros.setText("Pregled troškova");
    jmPregledTros.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPregledTros_actionPerformed(e);
      }
    });
    jmPregledNorm.setText("Pregled cijena po normativu");
    jmPregledNorm.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPregledNorm_actionPerformed(e);
      }
    });
    jmStatRN.setText("Pregled po radnim nalozima");
    jmStatRN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmStatRN_actionPerformed(e);
      }
    });
    this.add(jmPregledTros);
    this.add(jmRekapTros);
    this.add(jmStatRN);
    this.add(jmPregledNorm);
  }
  void jmStatRN_actionPerformed(ActionEvent e) {
    PreSelect.showPreselect("hr.restart.robno.presPregledRadnihNaloga","hr.restart.robno.frmPregledRadnihNaloga",jmStatRN.getText());
  }
  void jmRekapTros_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upRekapTros", jmRekapTros.getText());
  }
  void jmPregledTros_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upPregledTroskova", jmPregledTros.getText());
  }
  void jmPregledNorm_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upPregledNorm", jmPregledTros.getText());
  }
}