/****license*****************************************************************
**   file: frmBlagajnici.java
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
 * Created on 2004.11.02
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.robno;

import hr.restart.pos.frmBlagajnaPassword;
import hr.restart.swing.JraButton;
import hr.restart.util.Aus;
import hr.restart.util.raImages;
import hr.restart.util.raSifraNaziv;

import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * @author Racunalo4test
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class frmBlagajnici extends raSifraNaziv {
	JPanel jp = new JPanel();
	JLabel jlLozinka = new JLabel();
	frmBlagajnaPassword dlgPassw;

	XYLayout xYLayout1 = new XYLayout();
	JraButton jbChangePsw = new JraButton();
	JPasswordField jpswd = new JPasswordField() {
		public void addNotify() {
			super.addNotify();      
			Aus.installEnterRelease(this);
	    }
	};

	hr.restart.baza.dM dm;
	hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
	public frmBlagajnici() {
		try {
			jbInit();
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
	private void jbInit() throws Exception {
		dlgPassw=new frmBlagajnaPassword();
		dm = hr.restart.baza.dM.getDataModule();
		xYLayout1.setHeight(40);
		jp.setLayout(xYLayout1);
	    this.setRaDataSet(dm.getBlagajnici());
	    this.setRaColumnSifra("CBLAGAJNIK");
	    this.setRaColumnNaziv("NAZBLAG");
	    this.setRaText("Blagajnik");
	    jbChangePsw.setText("Promjena zaporke");
	    jbChangePsw.setIcon(raImages.getImageIcon(raImages.IMGALIGNCENTER));
	    jbChangePsw.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	          jbChangePsw_actionPerformed();
	        }
	      });
	    jlLozinka.setText("Zaporka");
	    jp.add(jlLozinka, new XYConstraints(15, 0, -1, -1));
	    jp.add(jbChangePsw, new XYConstraints(255, 0, 285, 21));
	    jp.add(jpswd, new XYConstraints(150, 0, 100, -1));
		this.jpRoot.add(jp, java.awt.BorderLayout.SOUTH);

	}
	void jbChangePsw_actionPerformed() {
		if(jpswd.getText().equals(dm.getBlagajnici().getString("LOZINKA"))) {
			dlgPassw.show();
		}
		else {
		      JOptionPane.showMessageDialog(this.getWindow(), "Pogrešna zaporka!", "Greška",
                    JOptionPane.ERROR_MESSAGE);
			
		}
		jpswd.setText("");
	}
	public boolean doBeforeSave(char mode) {
		jpswd.setText("");
		return true;
	}
	
	
}
