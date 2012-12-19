/****license*****************************************************************
**   file: menuIzvRac.java
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
 * Created on 2004.10.21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.rac;

import hr.restart.robno.repFISBIH;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * @author Racunalo4test
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class menuIzvRac extends JMenu {
	  hr.restart.util.startFrame SF;
	  JMenuItem jmUnFuckDoc = new JMenuItem();
	  JMenuItem jmRezKol = new JMenuItem();
	  JMenuItem jmMiniSaldak = new JMenuItem();
	  JMenuItem jmRmRezKol = new JMenuItem();
	  JMenuItem jmPreglProdArt = new JMenuItem();
	  JMenuItem jmNezaracunatiDoksi = new JMenuItem();
	  JMenuItem jmPregledKPRGRN = new JMenuItem();
	  JMenuItem jmPregledZaduzenjeKPR = new JMenuItem();
	  public JMenuItem jmFISBIH = new JMenuItem();
	  
	  public menuIzvRac(hr.restart.util.startFrame startframe) {
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
	    this.setText("Pregledi");
	    jmFISBIH.setText("Fiskalni izvještaji");
	    jmFISBIH.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmFISBIH_actionPerformed(e);
	      }
	    });

	    jmUnFuckDoc.setText("Pregled nezaraèunatih otpremnica");
	    jmUnFuckDoc.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmUnFuckDoc_actionPerformed(e);
	      }
	    });
	    jmMiniSaldak.setText("Pregled i ažuriranje naplate");
	    jmMiniSaldak.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmMiniSaldak_actionPerformed(e);
	      }
	    });
	    jmPreglProdArt.setText("Pregled prodaje po artiklima");
	    jmPreglProdArt.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            jmPreglProdArt_actionPerformed(e);
	        }
	      });
	    jmNezaracunatiDoksi.setText("Pregled neotpremljenih raèuna");
	    jmNezaracunatiDoksi.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmNezaracRac_actionPerformed(e);
	      }
	    });
	    jmRezKol.setText("Pregled rezervacija po artiklima");
	    jmRezKol.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmRezKol_actionPerformed(e);
	      }
	    });
	    jmPregledKPRGRN.setText("Pregled KPR za GRN i GOT");
	    jmPregledKPRGRN.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	      	jmPregledKPRGRN_actionPerformed(e);
	      }
	    });
	    jmPregledZaduzenjeKPR.setText("Pregled dokumenta zaduzenja za KPR iz GRN i GOT -a");
	    jmPregledZaduzenjeKPR.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	      	jmPregledZaduzenjeKPR_actionPerformed(e);
	      }
	    });
	    jmRmRezKol.setText("Poništavanje nevažeæih ponuda");
	    jmRmRezKol.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmRmRezKol_actionPerformed(e);
	      }
	    });
	    
	    this.add(jmUnFuckDoc);
	    this.add(jmPreglProdArt);
	    this.add(jmNezaracunatiDoksi);
	    this.add(jmRezKol);
	    this.addSeparator();
	    this.add(jmRmRezKol);
	    this.add(jmMiniSaldak);
//	    this.addSeparator();
//	    this.add(jmPregledKPRGRN);
//	    this.add(jmPregledZaduzenjeKPR);
	    if (repFISBIH.isFISBIH()) {
	      addSeparator();
	      add(jmFISBIH);
	    }
	  }
	  void jmUnFuckDoc_actionPerformed(ActionEvent e) {
	    SF.showFrame("hr.restart.robno.upUnFuckDoc", jmUnFuckDoc.getText());
	  }
	  void jmMiniSaldak_actionPerformed(ActionEvent e){
	    SF.showFrame("hr.restart.robno.raRobnoMiniSaldak", jmMiniSaldak.getText());
	  }
	  void jmPreglProdArt_actionPerformed(ActionEvent e){
	      SF.showFrame("hr.restart.robno.frmIzvArtObRac", jmPreglProdArt.getText());
	    }
	  void jmNezaracRac_actionPerformed(ActionEvent e){
	    SF.showFrame("hr.restart.robno.upNezaracunatiDokumentiObrRac", jmNezaracunatiDoksi.getText());
	  }
	  void jmRezKol_actionPerformed(ActionEvent e) {
	    SF.showFrame("hr.restart.robno.upRezKol", jmRezKol.getText());
	  }
	  void jmPregledKPRGRN_actionPerformed(ActionEvent e){
	    SF.showFrame("hr.restart.robno.upKPRFake", jmPregledKPRGRN.getText());
	  	
	  }
	  void jmPregledZaduzenjeKPR_actionPerformed(ActionEvent e) {
	    SF.showFrame("hr.restart.robno.UpPregledZaduzenjeKPR", jmPregledZaduzenjeKPR.getText());	  	
	  }
	  
	  void jmRmRezKol_actionPerformed(ActionEvent e) {
	    SF.showFrame("hr.restart.robno.upUnrealPonude", jmRmRezKol.getText()); // waas frmDelPon...
	  }
	  public void jmFISBIH_actionPerformed(ActionEvent e) {
	    // TODO Auto-generated method stub
	    SF.showFrame("hr.restart.robno.FISBIHIzvjestaji", jmFISBIH.getText());
	  }
}
