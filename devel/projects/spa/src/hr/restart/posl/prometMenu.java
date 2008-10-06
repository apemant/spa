/****license*****************************************************************
**   file: prometMenu.java
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
 * Created on 2004.09.13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.posl;

import hr.restart.util.raLoader;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class prometMenu extends JMenu {
	ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");
	hr.restart.util.startFrame SF;
	JMenuItem jmNDO = new JMenuItem();
	JMenuItem jmNKU = new JMenuItem();
	JMenuItem jmPON = new JMenuItem();
	  
	public prometMenu(hr.restart.util.startFrame startframe) {
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
		this.setText("Prometi");
	    jmNDO.setText("Narudžbe dobavljaèu");
	    jmNDO.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmNDO_actionPerformed(e);
	      }
	    });
	    jmNKU.setText("Narudžbe kupca");
	    jmNKU.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmNKU_actionPerformed(e);
	      }
	    });
	    jmPON.setText("Ponude");
	    jmPON.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmPON_actionPerformed(e);
	      }
	    });
	    this.add(jmNDO);
	    this.add(jmNKU);
	    this.addSeparator();
	    this.add(jmPON);
	}
	  void jmNDO_actionPerformed(ActionEvent e) {
	    hr.restart.robno.frmNarDob frmND = (hr.restart.robno.frmNarDob) raLoader.load("hr.restart.robno.frmNarDob");
	    hr.restart.robno.presNDO.getPres().showJpSelectDoc("NDO", frmND, true, "Narudžbe dobavljaèu");
	  }
	  void jmNKU_actionPerformed(ActionEvent e) {
	  	hr.restart.robno.raNKU ranku = (hr.restart.robno.raNKU)raLoader.load("hr.restart.robno.raNKU");
	  	hr.restart.robno.presNKU.getPres().showJpSelectDoc("NKU", ranku, true, "Narudžbe kupca");
	  }
	  void jmPON_actionPerformed(ActionEvent e) {
	    hr.restart.robno.raPONOJ rapon = (hr.restart.robno.raPONOJ)raLoader.load("hr.restart.robno.raPONOJ");
	    hr.restart.robno.presPONOJ.getPres().showJpSelectDoc("PON", rapon, true, jmPON.getText());
	  }

}
