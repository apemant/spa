/****license*****************************************************************
**   file: jcrmOpMenu.java
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
package hr.restart.crm;

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author REST-ART development team
 * @version 1.0
 */

public class jcrmOpMenu extends JMenu {
  
  hr.restart.util.startFrame SF;
  
  public JMenuItem jmKlijenti = new JMenuItem();
  public JMenuItem jmStatusi = new JMenuItem();
  public JMenuItem jmKanali = new JMenuItem();
  public JMenuItem jmSeg = new JMenuItem();
  

  public jcrmOpMenu(hr.restart.util.startFrame startframe) {
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
    this.setText("Osnovni podaci");
    jmStatusi.setText("Statusi klijenata");
    jmStatusi.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        SF.showFrame("hr.restart.crm.frmKlijentStatus", jmStatusi.getText());
      }
    });
    jmSeg.setText("Kanali komunikacije");
    jmSeg.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        SF.showFrame("hr.restart.crm.frmKanali", jmKanali.getText());
      }
    });
    jmKanali.setText("Segmentacija");
    jmKanali.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        SF.showFrame("hr.restart.crm.frmSegment", jmSeg.getText());
      }
    });
    
    jmKlijenti.setText("Klijenti");
    jmKlijenti.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        SF.showFrame("hr.restart.crm.frmKlijenti", jmKlijenti.getText());
      }
    });
    
    this.add(jmStatusi);
    this.add(jmKanali);
    this.add(jmSeg);
    this.addSeparator();
    this.add(jmKlijenti);
  }
}