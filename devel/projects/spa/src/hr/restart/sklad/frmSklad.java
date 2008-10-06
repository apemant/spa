/****license*****************************************************************
**   file: frmSklad.java
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
 * Created on 2005.03.29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.sklad;
 
import hr.restart.robno._Main;
import hr.restart.util.startFrame;

import java.awt.AWTEvent;

import javax.swing.JMenuBar;

/**
 * @author Racunalo4test
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class frmSklad extends startFrame {
	public static frmSklad _frmSklad = null;
	public static skladMenu skladMnu;
	public static skladMenuUpit skladMnuUpit;
	public static skladMenuIzv skladMnuIzv;
	JMenuBar jmMain = new JMenuBar();
	
	public frmSklad() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
	    try {
	      this.jbInit();
	      _frmSklad=this;
	    }
	    catch(Exception e) {
	      e.printStackTrace();
	    }
	}
	
	public static frmSklad getFrmSklad() {
	    if (_frmSklad == null) {
	      _frmSklad = new frmSklad();
	    }
	    return _frmSklad;
	}
	public static javax.swing.JMenu getSkladMenu(startFrame startframe) {
	    skladMnu = new skladMenu(startframe);
	    return skladMnu;
	}
	public static javax.swing.JMenu getSkladMenuUpit(startFrame startframe) {
		skladMnuUpit = new skladMenuUpit(startframe);
	    return skladMnuUpit;
	}
	public static javax.swing.JMenu getSkladMenuIzv(startFrame startframe) {
		skladMnuIzv = new skladMenuIzv(startframe);
	    return skladMnuIzv;
	}
	private void jbInit() throws Exception  {
		jmMain.add(hr.restart.zapod.frmZapod.getJzpMenu(this));
		jmMain.add(_Main.getOpMenu(this));
		jmMain.add(frmSklad.getSkladMenu(this));
		jmMain.add(frmSklad.getSkladMenuUpit(this));
		jmMain.add(frmSklad.getSkladMenuIzv(this));
		this.setRaJMenuBar(jmMain);
	  }
}
