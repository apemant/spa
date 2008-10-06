/****license*****************************************************************
**   file: JraMultyKolPopup.java
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
 * Created on 2005.04.18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.swing;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class JraMultyKolPopup extends JPopupMenu {
	
	  MouseListener popup;  
	  JraTextField owner= null;
	  private Action selectMultyKol = new AbstractAction("Paralelne kolièine") {
	    public void actionPerformed(ActionEvent e) {
	    	new JraMultyKolChooser((JraTextMultyKolField)owner).show();
	    }
 
	  };	  
	  static JraMultyKolPopup currInst = null;
	  JMenuItem jmiSelectMultyKol = new JMenuItem();
	  
	  static {
	    AWTKeyboard.registerKeyStroke(AWTKeyboard.ESC, new KeyAction() {
	      public boolean actionPerformed() {
	        if (currInst == null || !currInst.isVisible()) return false;
	        currInst.setVisible(false);
	        return true;
	      }
	    });
	  }

	  private JraMultyKolPopup() {}

	  public static void hideInstance() {
	    if (currInst != null && currInst.isVisible())
	      currInst.setVisible(false);
	  }

	  public JraMultyKolPopup(JraTextField owner) {
	    try {
	      this.owner = owner;
	      init();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }	
	  
	  public void init(){
	  	add(jmiSelectMultyKol);	
	  	jmiSelectMultyKol.setAction(selectMultyKol);
	  	setOwnerListener();
	  
	  }

	  private void setOwnerListener() {
	    owner.setEnablePopupMenu(false);
	    owner.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_K, MouseEvent.CTRL_MASK), "MUK");
	    owner.getActionMap().put("MUK", selectMultyKol);
	    owner.addMouseListener(popup = new MouseAdapter() {
	      public void mousePressed(MouseEvent e) {
	        checkPopup(e);
	      }
	      public void mouseReleased(MouseEvent e) {
	        checkPopup(e);
	      }
	    });
	  }

	  private void checkPopup(MouseEvent e) {
	    if (e.isPopupTrigger() && owner.isEnabled())
	      (currInst = this).show(e.getComponent(), e.getX(), e.getY());
	  }
}