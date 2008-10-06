/****license*****************************************************************
**   file: skladMenuUpit.java
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
package hr.restart.sklad;

import hr.restart.robno.upKartica;
import hr.restart.robno.upSkladKartica;

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * @author Racunalo4test
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class skladMenuUpit extends JMenu {
	  hr.restart.util.startFrame SF;
	  JMenuItem jmStanje = new JMenuItem();
	  JMenuItem jmKartica = new JMenuItem();
	  JMenuItem jmKartKupArt = new JMenuItem();
	  JMenuItem jmArtSklad = new JMenuItem();
	  JMenuItem jmTotaliKartica = new JMenuItem();

	  public skladMenuUpit(hr.restart.util.startFrame startframe) {
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
	    jmStanje.setText("Stanje artikala");
	    jmStanje.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmStanje_actionPerformed(e);
	      }
	    });
	    jmKartica.setToolTipText("");
	    jmKartica.setText("Kartica artikla");
	    jmKartica.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmKartica_actionPerformed(e);
	      }
	    });
	    jmKartKupArt.setText("Kartica kupci-artikli");
	    jmKartKupArt.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmKartKupArt_actionPerformed(e);
	      }
	    });
	    jmArtSklad.setText("Artikli - skladišta");
	    jmArtSklad.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmArtSklad_actionPerformed(e);
	      }
	    });
	    jmTotaliKartica.setText("Kartice više artikala");
	    jmTotaliKartica.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmTotaliKartica_actionPerformed(e);
	      }
	    });
	    this.add(jmStanje);
	    this.add(jmKartica);
	    this.add(jmTotaliKartica);
	    this.add(jmKartKupArt);
	    this.add(jmArtSklad);
	  }
	  void jmStanje_actionPerformed(ActionEvent e) {
	    SF.showFrame("hr.restart.robno.upSklStanjeNaSkladistu", jmStanje.getText());
	  }
	  void jmKartica_actionPerformed(ActionEvent e) {
	    SF.showFrame("hr.restart.robno.upSkladKartica", 15, jmKartica.getText(), false);
	    upSkladKartica.getupsKartica().clearOutsideData();
	    SF.showFrame("hr.restart.robno.upSkladKartica", jmKartica.getText());
	  }
	  void jmKartKupArt_actionPerformed(ActionEvent e) {
	    SF.showFrame("hr.restart.robno.upSkladKarticaKupca", jmKartKupArt.getText());
	  }
	  void jmArtSklad_actionPerformed(ActionEvent e) {
	    SF.showFrame("hr.restart.robno.upSkladArtSklad", jmArtSklad.getText());
	  }
	  void jmTotaliKartica_actionPerformed(ActionEvent e) {
	    SF.showFrame("hr.restart.robno.upTotSkladKar", jmTotaliKartica.getText());
	  }

}
