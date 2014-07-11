/****license*****************************************************************
**   file: jcrmMenu.java
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

import hr.restart.util.PreSelect;

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

public class jcrmMenu extends JMenu {
  
  hr.restart.util.startFrame SF;
  
  public JMenuItem jmKontakti = new JMenuItem();
  public JMenuItem jmKampanje = new JMenuItem();
  public JMenuItem jmKampanjeKre = new JMenuItem();
  

  public jcrmMenu(hr.restart.util.startFrame startframe) {
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
    this.setText("CRM");
    jmKontakti.setText("Kontakti");
    jmKontakti.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        SF.showFrame("hr.restart.crm.frmKontakti", jmKontakti.getText());
      }
    });
    jmKampanje.setText("Kampanje");
    jmKampanje.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        PreSelect.showPreselect("hr.restart.crm.presKampanje","hr.restart.crm.frmKampanje","Kampanje", true);
      }
    });
    
    jmKampanjeKre.setText("Pokretanje kampanje");
    jmKampanjeKre.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        SF.showFrame("hr.restart.crm.frmKampanjeKreator","Pokretanje kampanje");
      }
    });
    
    this.add(jmKontakti);
    this.add(jmKampanje);
    this.addSeparator();
    this.add(jmKampanjeKre);
  }
}