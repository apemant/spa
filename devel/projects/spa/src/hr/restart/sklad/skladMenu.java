/****license*****************************************************************
**   file: skladMenu.java
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

import hr.restart.robno.*;
import hr.restart.sisfun.frmParam;
import hr.restart.util.raLoader;

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * @author Racunalo4test
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class skladMenu extends JMenu {
	hr.restart.util.startFrame SF;
	JMenuItem jmPRI = new JMenuItem();
	JMenuItem jmZAH = new JMenuItem();
	JMenuItem jmOTP = new JMenuItem();
	JMenuItem jmIZD = new JMenuItem();
	JMenuItem jmREV = new JMenuItem();
	JMenuItem jmPRV = new JMenuItem();
	JMenuItem jmROT = new JMenuItem();
	JMenuItem jmDOS = new JMenuItem();

	public skladMenu(hr.restart.util.startFrame startframe) {
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
	    jmZAH.setText("Trebovanje");
	    jmZAH.addActionListener(new java.awt.event.ActionListener() {
          public void actionPerformed(ActionEvent e) {
            jmZAH_actionPerformed(e);
          }
        });
	    jmPRI.setText("Primke");
	    jmPRI.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmPRI_actionPerformed(e);
	      }
	    });
	    jmOTP.setText("Otpremnice");
	    jmOTP.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmOTP_actionPerformed(e);
	      }
	    });
	    jmIZD.setText("Izdatnice");
	    jmIZD.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmIZD_actionPerformed(e);
	      }
	    });
	    jmREV.setText("Reversi");
	    jmREV.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmREV_actionPerformed(e);
	      }
	    });
	    jmPRV.setText("Povratnice reversa");
	    jmPRV.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmPRV_actionPerformed(e);
	      }
	    });
	    jmROT.setText("Raèuni-otpremnice");
	    jmROT.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmROT_actionPerformed(e);
	      }
	    });
	    jmDOS.setText("Dostavnice");
	    jmDOS.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmDOS_actionPerformed(e);
	      }
	    });
	    this.add(jmZAH);
	    this.add(jmPRI);
	    this.add(jmDOS);
	    this.add(jmOTP);
	    this.add(jmIZD);
	    this.add(jmREV);
	    this.add(jmPRV);
	    this.addSeparator();
	    this.add(jmROT);
	}
	void jmZAH_actionPerformed(ActionEvent e) {
      raZAH fZAH = (raZAH)raLoader.load("hr.restart.robno.raZAH");
      presZAH.getPres().showJpSelectDoc("TRE", fZAH, true, "Trebovanje");
    }
	void jmPRI_actionPerformed(ActionEvent e) {
	    frmPRI fPRI = (frmPRI)raLoader.load("hr.restart.robno.frmPRI");
	    presPRI.getPres().showJpSelectDoc("PRI", fPRI, true, "Primke");
	}
	void jmDOS_actionPerformed(ActionEvent e) {
	    raDOS rados = (raDOS)raLoader.load("hr.restart.robno.raDOS");
	    presDOS.getPres().showJpSelectDoc("DOS", rados, true, "Dostavnice");
	}
	void jmOTP_actionPerformed(ActionEvent e) {
	    raOTPkol raotp = (raOTPkol)raLoader.load("hr.restart.robno.raOTPkol");
	    presOTP.getPres().showJpSelectDoc("OTP", raotp, true, "Otpremnice");
	}
	void jmIZD_actionPerformed(ActionEvent e) {
	  if (frmParam.getParam("sklad", "DOSorIZD", "IZD", "Na opciji 'Izdatnice' Skladisnog poslovanja zove se (IZD)kol ili (DOS)izd")
	        .equalsIgnoreCase("IZD")) {
	    raIZDkol raizd = (raIZDkol)raLoader.load("hr.restart.robno.raIZDkol");
	    presIZD.getPres().showJpSelectDoc("IZD", raizd, true, "Izadtnice");
	  } else {
	    raDOSIzd radosi = (raDOSIzd)raLoader.load("hr.restart.robno.raDOSIzd");
	    presDOSIzd.getPres().showJpSelectDoc("DOS", radosi, true, "Izdatnice (DOS)");
	  }
	}
	void jmPRV_actionPerformed(ActionEvent e) {
	    raPRVkol raprv = (raPRVkol)raLoader.load("hr.restart.robno.raPRVkol");
	    presPRV.getPres().showJpSelectDoc("PRV", raprv, true, "Povratnica reversa");
	}
	void jmREV_actionPerformed(ActionEvent e) {
	    raREVkol rarev = (raREVkol)raLoader.load("hr.restart.robno.raREVkol");
	    presREV.getPres().showJpSelectDoc("REV", rarev, true, "Revers");
	}
	void jmROT_actionPerformed(ActionEvent e) {
	    raROTkol rarot = (raROTkol)raLoader.load("hr.restart.robno.raROTkol");
	    presROT.getPres().showJpSelectDoc("ROT", rarot, true, jmROT.getText());
	}
}
