/****license*****************************************************************
**   file: jrnMenu.java
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
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class jrnMenu extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.util.startFrame SF;
  JMenuItem jmRN = new JMenuItem();
  JMenuItem jmRN_Proizvodnja = new JMenuItem();
  JMenuItem jmRN_Subjekt = new JMenuItem();

  public jrnMenu(hr.restart.util.startFrame startframe) {
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
    this.setText("Radni nalozi");
    this.add(jmRN);
    this.add(jmRN_Proizvodnja);
    this.addSeparator();
    this.add(jmRN_Subjekt);

    jmRN.setText("Servis");
    jmRN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRN_actionPerformed(e);
      }
    });
    jmRN_Proizvodnja.setText("Proizvodnja");
    jmRN_Proizvodnja.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRN_Proizvodnja_actionPerformed(e);
      }
    });
    jmRN_Subjekt.setText("Subjekti radnog naloga");
    jmRN_Subjekt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRN_Subjekt_actionPerformed(e);
      }
    });
  }
  void jmRN_Subjekt_actionPerformed(ActionEvent e) {
    hr.restart.robno.frmSubjekti fSubjekti = (hr.restart.robno.frmSubjekti) SF.showFrame("hr.restart.robno.frmSubjekti", 0, res.getString("frmSubjekti_title"), false);
    fSubjekti.SetInner(false);
    fSubjekti.pres.showPreselect(fSubjekti, res.getString("frmSubjekti_title"));
  }
  void jmRN_actionPerformed(ActionEvent e) {
    final hr.restart.robno.frmServis fRadniNalog = (hr.restart.robno.frmServis) SF.showFrame("hr.restart.robno.frmServis", 0, "Servis", false);
    fRadniNalog.pres.showPreselect(fRadniNalog, res.getString("frmRadniNalog_title"));
//    PreSelect.showPreselect("hr.restart.robno.presProizvodnja", "hr.restart.robno.frmProizvodnja", "Radni nalozi - proizvodnja");
  }
  void jmRN_Proizvodnja_actionPerformed(ActionEvent e) {
    PreSelect.showPreselect("hr.restart.robno.presProizvodnja", "hr.restart.robno.frmProizvodnja", "Radni nalozi - proizvodnja");
  }
  
  public void enableSubjekt(boolean enab) {
    jmRN_Subjekt.setEnabled(enab);
  }
}