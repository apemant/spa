/****license*****************************************************************
**   file: frmCstd.java
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

import hr.restart.util.startFrame;

import java.awt.AWTEvent;

import javax.swing.JMenuBar;

/**
 * @author Racunalo4test
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class frmCstd extends startFrame {
	public static frmCstd _frmCstd = null;
	public static cstdMenu cstdMnu;
	public static cstdMenuOp cstdMnuOp;
	public static cstdMenuUpit cstdMnuUpit;
//	public static cstdMenuIzv cstdMnuIzv;
	JMenuBar jmMain = new JMenuBar();
	
	public frmCstd() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
	    try {
	      this.jbInit();
	      _frmCstd=this;
	    }
	    catch(Exception e) {
	      e.printStackTrace();
	    }
	}
	
	public static frmCstd getFrmCstd() {
	    if (_frmCstd == null) {
	      _frmCstd = new frmCstd();
	    }
	    return _frmCstd;
	}
	public static javax.swing.JMenu getCstdMenu(startFrame startframe) {
	    cstdMnu = new cstdMenu(startframe);
	    return cstdMnu;
	}
	public static javax.swing.JMenu getCstdMenuOp(startFrame startframe) {
	    cstdMnuOp = new cstdMenuOp(startframe);
	    return cstdMnuOp;
	}
	public static javax.swing.JMenu getCstdMenuUpit(startFrame startframe) {
		cstdMnuUpit = new cstdMenuUpit(startframe);
	    return cstdMnuUpit;
	}
//	public static javax.swing.JMenu getCstdMenuIzv(startFrame startframe) {
//		cstdMnuIzv = new cstdMenuIzv(startframe);
//	    return cstdMnuIzv;
//	}
	private void jbInit() throws Exception  {
		jmMain.add(hr.restart.zapod.frmZapod.getJzpMenu(this));
		jmMain.add(frmCstd.getCstdMenuOp(this));
		jmMain.add(frmCstd.getCstdMenu(this));
		jmMain.add(frmCstd.getCstdMenuUpit(this));
//		jmMain.add(frmCstd.getCstdMenuIzv(this));
		this.setRaJMenuBar(jmMain);
	}
}
