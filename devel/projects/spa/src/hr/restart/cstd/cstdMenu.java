/****license*****************************************************************
**   file: cstdMenu.java
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
public class cstdMenu extends JMenu {
	hr.restart.util.startFrame SF;
	JMenuItem jmPRI = new JMenuItem();
	JMenuItem jmKnjiZapis = new JMenuItem();
	  public cstdMenu(hr.restart.util.startFrame startframe) {
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
	    this.setText("Dokumenti");
	    jmPRI.setText("Primke");
	    jmPRI.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    jmPRI_actionPerformed();
			}
		});
	    
	    jmKnjiZapis.setText("Knjigovodstveni zapisi");
	    jmKnjiZapis.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    jmKnjiZapis_actionPerformed();
			}
		});
		this.add(jmPRI);
		this.add(jmKnjiZapis);
	    
	  }
	  
	  public void jmPRI_actionPerformed(){
			SF.showFrame("hr.restart.cstd.FrmCarPrimka", jmPRI.getText());	      
	  }
	  public void jmKnjiZapis_actionPerformed(){
			SF.showFrame("hr.restart.cstd.FrmCarKnjZapis", jmKnjiZapis.getText());
	      
	  }



}
