/****license*****************************************************************
**   file: menuKonsig.java
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

public class menuKonsig extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");

  hr.restart.util.startFrame SF;
  JMenuItem jmKOD = new JMenuItem();
  
  public menuKonsig(hr.restart.util.startFrame startframe) {
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
    this.setText("Komisija");
    jmKOD.setText("Komisijska odjava");
    jmKOD.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
    	  jmKOD_actionPerformed(e);
      }
    });
    this.add(jmKOD);
  }
  void jmKOD_actionPerformed(ActionEvent e) {
	hr.restart.robno.raKOD rakon = 
		(hr.restart.robno.raKOD)raLoader.load("hr.restart.robno.raKOD");
    presKOD.getPres().showJpSelectDoc("KOD", rakon, true, jmKOD.getText());
  }

}