/****license*****************************************************************
**   file: cstdMenuOp.java
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
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class cstdMenuOp extends JMenu {
	hr.restart.util.startFrame SF;

	JMenuItem jmCarinarnica = new JMenuItem();

	JMenuItem jmZemlje = new JMenuItem();

	JMenuItem jmFizosob = new JMenuItem();

	JMenuItem jmParitet1PP = new JMenuItem();

	JMenuItem jmParitet3PP = new JMenuItem();

	JMenuItem jmVrstePosla = new JMenuItem();

	JMenuItem jmVrstePrometa = new JMenuItem();

	JMenuItem jmJedMjer = new JMenuItem();

	JMenuItem jmPotPor = new JMenuItem();

	JMenuItem jmDozvole = new JMenuItem();

	JMenuItem jmOslobod = new JMenuItem();

	JMenuItem jmCarPost1pp37 = new JMenuItem();

	JMenuItem jmCarPost2pp37 = new JMenuItem();

	JMenuItem jmOdob = new JMenuItem();

	JMenuItem jmTrosOtprem = new JMenuItem();

	JMenuItem jmSifVrijed = new JMenuItem();

	JMenuItem jmVrDav = new JMenuItem();

	JMenuItem jmSifNacPl = new JMenuItem();

	JMenuItem jmCarTG = new JMenuItem();

	JMenuItem jmCarUgovor = new JMenuItem();

	JMenuItem jmCarTarifa = new JMenuItem();

	public cstdMenuOp(hr.restart.util.startFrame startframe) {
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
		jmCarinarnica.setText("Carinarnice i carinske ispostave");
		jmCarinarnica.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmCarinarnica_actionPerformed();
			}
		});
		jmZemlje.setText("Popis zemalja prema pravilniku");
		jmZemlje.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmZemlje_actionPerformed();
			}
		});
		jmFizosob.setText("Fizièke osobe");
		jmFizosob.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmFizosob_actionPerformed();
			}
		});
		jmParitet1PP.setText("Paritet prvo podpolje");
		jmParitet1PP.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmParitet1PP_actionPerformed();
			}
		});
		jmParitet3PP.setText("Paritet treæe podpolje");
		jmParitet3PP.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmParitet3PP_actionPerformed();
			}
		});
		jmVrstePosla.setText("Vrste posla");
		jmVrstePosla.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmVrstePosla_actionPerformed();
			}
		});
		jmVrstePrometa.setText("Vrste prometa");
		jmVrstePrometa.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmVrstePrometa_actionPerformed();
			}
		});
		jmJedMjer.setText("Jedinice mjere (carinske)");
		jmJedMjer.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmJedMjer_actionPerformed();
			}
		});
		jmDozvole.setText("Dozvole za uvoz i izvoz nadležnih tijela");
		jmDozvole.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmDozvole_actionPerformed();
			}
		});

		jmPotPor.setText("Potvrde o podrijetlu");
		jmPotPor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmPotPor_actionPerformed();
			}
		});

		jmOslobod.setText("Osloboðenja od plaæanja carine");
		jmOslobod.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmOslobod_actionPerformed();
			}
		});
		jmCarPost1pp37.setText("Carinski postupci (prvo podpolje polja 37)");
		jmCarPost1pp37.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmCarPost1pp37_actionPerformed();
			}
		});
		
		
		jmCarPost2pp37.setText("Carinski postupci (drugo podpolje polja 37)");
		jmCarPost2pp37.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmCarPost2pp37_actionPerformed();
			}
		});
		jmOdob.setText("Odobrenja");
		jmOdob.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmOdob_actionPerformed();
			}
		});
		
		jmTrosOtprem.setText("Troškovi otpreme");
		jmTrosOtprem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmTrosOtprem_actionPerformed();
			}
		});
		jmSifVrijed.setText("Šifre vrijednosti");
		jmSifVrijed.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmSifVrijed_actionPerformed();
			}
		});
		
		jmVrDav.setText("Vrste davanja");
		jmVrDav.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmVrDav_actionPerformed();
			}
		});
		jmSifNacPl.setText("Šifre naèina plaæanja prema pravilniku");
		jmSifNacPl.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmSifNacPl_actionPerformed();
			}
		});
		
		jmCarTG.setText("Tarifne grupe");
		jmCarTG.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmCarTG_actionPerformed();
			}
		});
		jmCarUgovor.setText("Ugovori o povlaštenom režimu");
		jmCarUgovor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmCarUgovor_actionPerformed();
			}
		});
		jmCarTarifa.setText("Carinske tarife po ugovorima");
		jmCarTarifa.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jmCarTarifa_actionPerformed();
			}
		});

		this.add(jmCarinarnica);
		this.add(jmZemlje);
		this.add(jmFizosob);
		this.add(jmParitet1PP);
		this.add(jmPotPor);
		this.add(jmParitet3PP);
		this.addSeparator();
		this.add(jmVrstePosla);
		this.add(jmVrstePrometa);
		this.addSeparator();
		this.add(jmJedMjer);
		this.add(jmDozvole);
		this.add(jmOslobod);
		this.add(jmCarPost1pp37);
		this.add(jmCarPost2pp37);
		this.add(jmOdob);
		this.add(jmTrosOtprem);
		this.add(jmSifVrijed);
		this.add(jmVrDav);
		this.add(jmSifNacPl);
		this.addSeparator();
		this.add(jmCarTG);
		this.add(jmCarUgovor);
		this.add(jmCarTarifa);

	}

	public void jmCarinarnica_actionPerformed() {
		SF.showFrame("hr.restart.cstd.RaCarinarnice", jmCarinarnica.getText());
	}

	public void jmZemlje_actionPerformed() {
		SF.showFrame("hr.restart.cstd.RaCarZemlje", jmZemlje.getText());
	}
	public void jmFizosob_actionPerformed(){
		SF.showFrame("hr.restart.cstd.RaFizosob", jmFizosob.getText());
	}
	public void jmParitet1PP_actionPerformed(){
		SF.showFrame("hr.restart.cstd.Paritet1PP", jmParitet1PP.getText());
	}
	public void jmParitet3PP_actionPerformed(){
		SF.showFrame("hr.restart.cstd.Paritet3PP", jmParitet3PP.getText());
	}
	
	public void	jmVrstePosla_actionPerformed(){
		SF.showFrame("hr.restart.cstd.RaCarVrstePosla", jmVrstePosla.getText());
	}//+
	public void jmVrstePrometa_actionPerformed(){
		SF.showFrame("hr.restart.cstd.RaCarVrstePrometa", jmVrstePrometa.getText());
	}//+ treba dovršiti
	public void jmJedMjer_actionPerformed(){
		SF.showFrame("hr.restart.cstd.RaCarJedMjer", jmJedMjer.getText());
	}//+		
	public void jmDozvole_actionPerformed(){
		SF.showFrame("hr.restart.cstd.RaCarDozvole", jmDozvole.getText());
	}//+
	public void jmPotPor_actionPerformed(){
		SF.showFrame("hr.restart.cstd.RaCarPotPor", jmPotPor.getText());
	}//+
	public void jmOslobod_actionPerformed(){
		SF.showFrame("hr.restart.cstd.RaCarOslobod", jmOslobod.getText());
	}//+
	public void jmCarPost1pp37_actionPerformed(){
		SF.showFrame("hr.restart.cstd.RaCarPost1pp37", jmCarPost1pp37.getText());
	}//+		
	public void jmCarPost2pp37_actionPerformed(){
		SF.showFrame("hr.restart.cstd.RaCarPost2pp37", jmCarPost2pp37.getText());
	}//+
	public void jmOdob_actionPerformed(){
		SF.showFrame("hr.restart.cstd.RaCarOdob", jmOdob.getText());
	}//+		
	public void jmTrosOtprem_actionPerformed(){
		SF.showFrame("hr.restart.cstd.RaCarTrosOtprem", jmTrosOtprem.getText());
	} //+		
	public void jmSifVrijed_actionPerformed(){
		SF.showFrame("hr.restart.cstd.RaCarSifVrijed", jmSifVrijed.getText());
	} // +		
	public void jmVrDav_actionPerformed(){
		SF.showFrame("hr.restart.cstd.RaCarVrDav", jmVrDav.getText());
	}
	public void jmSifNacPl_actionPerformed(){
		SF.showFrame("hr.restart.cstd.RaCarSifNacPl", jmSifNacPl.getText());
	}		
	public void jmCarTG_actionPerformed(){
		SF.showFrame("hr.restart.cstd.frmCarTG", jmCarTG.getText());
	}		
	public void jmCarUgovor_actionPerformed(){
		SF.showFrame("hr.restart.cstd.frmCarUgovor", jmCarUgovor.getText());
	}		
	public void jmCarTarifa_actionPerformed(){
		SF.showFrame("hr.restart.cstd.frmCarTarifa", jmCarTarifa.getText());
	}		
}
