/****license*****************************************************************
**   file: cstdMenuUpit.java
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
/*
 * Created on 2005.06.09
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.cstd;

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * @author Racunalo4test
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class cstdMenuUpit extends JMenu {
	hr.restart.util.startFrame SF;
	JMenuItem jmIZV = new JMenuItem();
	JMenuItem jmKart = new JMenuItem();
	  public cstdMenuUpit(hr.restart.util.startFrame startframe) {
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
	    this.setText("Upiti");
	    jmIZV.setText("Izvadak knjigovodstvenih zapisa");
	    jmIZV.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    jmIZV_actionPerformed();
			}
		});
	    
	    jmKart.setText("Kartica po tarifnom broju");
	    jmKart.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    jmKart_actionPerformed();
			}
		});
		this.add(jmIZV);
		this.add(jmKart);
	  	
	  }
	  public void jmIZV_actionPerformed(){
			SF.showFrame("hr.restart.cstd.FrmIzvadak", jmIZV.getText());	      
	  }
	  public void jmKart_actionPerformed(){
			SF.showFrame("hr.restart.cstd.FrmTarKartica", jmKart.getText());
	      
	  }




}
